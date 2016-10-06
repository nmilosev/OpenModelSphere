/*************************************************************************
Copyright (C) 2009 by neosapiens inc.

This file is part of Open ModelSphere.

Open ModelSphere is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can redistribute and/or modify this particular file even under the
terms of the GNU Lesser General Public License (LGPL) as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

You should have received a copy of the GNU Lesser General Public License 
(LGPL) along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can reach neosapiens inc. at: 

neosapiens inc.
1236 Gaudias-Petitclerc
Qu&eacute;bec, Qc, G1Y 3G2
CANADA
Telephone: 418-561-8403
Fax: 418-650-2375
http://www.neosapiens.com/
Email: marco.savard@neosapiens.com
       gino.pelletier@neosapiens.com

 **********************************************************************/
package com.neosapiens.plugins.reverse.javasource;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.TokenStream;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.gui.task.Worker;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVPackage;

import com.neosapiens.plugins.reverse.javasource.actions.ModelSemanticAction;
import com.neosapiens.plugins.reverse.javasource.actions.SemanticAction;
import com.neosapiens.plugins.reverse.javasource.actions.SemanticActionError;
import com.neosapiens.plugins.reverse.javasource.diagrams.DiagramCreator;
import com.neosapiens.plugins.reverse.javasource.international.JavaSourceReverseLocaleMgr;
import com.neosapiens.plugins.reverse.javasource.parsing.Java1_6Lexer;
import com.neosapiens.plugins.reverse.javasource.parsing.Java1_6Parser;
import com.neosapiens.plugins.reverse.javasource.ui.result.ShowResultDialog;
import com.neosapiens.plugins.reverse.javasource.utils.ModelHelper;

public class ReverseJavaSourcecodeWorker extends Worker implements ActionListener {

    private DbSMSProject m_project;
    private ReverseJavaSourcecodeParameters m_params;
    private JButton okButton = null;
    private String modelName;
    
    private List<DbObject> dbObjects;

    public ReverseJavaSourcecodeWorker(ReverseJavaSourcecodeParameters params) {
        m_params = params;
    }

    @Override
    protected String getJobTitle() {
        return JavaSourceReverseLocaleMgr.misc.getString("ImportJavaSourcecode");
    }

    @Override
    protected void runJob() throws Exception {
        Controller controller = getController();
        if(controller instanceof DefaultController1) {
            DefaultController1 dc = (DefaultController1) controller;
            okButton = dc.getOKButton();
            okButton.addActionListener(this);
        }

        DefaultMainFrame mainFrame = ApplicationContext.getDefaultMainFrame();
        List<DbJVPackage> ownedPackages = new ArrayList<DbJVPackage>();
        m_project = new DbSMSProject();
        m_project = (DbSMSProject) mainFrame.createDefaultProject(null);
        Db db = m_project.getDb();
        db.beginWriteTrans(JavaSourceReverseLocaleMgr.misc.getString("ImportJavaSourcecode"));
        DbJVClassModel model = new DbJVClassModel(m_project);
        modelName = model.getName();

        List<File> filesToImport = m_params.getFilesToImport();

        int filesScanPercent;
        int finalizeCreationPercent;
        int diagramCreationPercent;

        if (m_params.mustCreateDiagrams()) {
            finalizeCreationPercent = 20;
            if (m_params.mustCreateFields())
                finalizeCreationPercent += 15;
            if (m_params.createMethods)
                finalizeCreationPercent += 15;
            diagramCreationPercent = 15;
            filesScanPercent = 100 - finalizeCreationPercent - diagramCreationPercent;
        } else {
            finalizeCreationPercent = 30;
            if (m_params.mustCreateFields())
                finalizeCreationPercent += 15;
            if (m_params.createMethods)
                finalizeCreationPercent += 15;
            diagramCreationPercent = 0;
            filesScanPercent = 100 - finalizeCreationPercent - diagramCreationPercent;
        }

        try {
            SemanticAction semAct = new ModelSemanticAction(model, ownedPackages, controller,
                    filesScanPercent, filesScanPercent + finalizeCreationPercent,
                    m_params.mustCreateMethods(), m_params.mustCreateFields());
            String pattern = JavaSourceReverseLocaleMgr.misc.getString("FailedOn0");
            
            for (int i = 0; i < filesToImport.size(); i++) {
            	File file = filesToImport.get(i);
                ANTLRFileStream input = new ANTLRFileStream(filesToImport.get(i).getPath());
                Java1_6Lexer lexer = new Java1_6Lexer(input);
                TokenStream tokenStream = new CommonTokenStream(lexer);
                Java1_6Parser parser = new Java1_6Parser(tokenStream, semAct);
                
                parser.compilationUnit();
                /*
                try {
                	parser.compilationUnit();
                } catch (RuntimeException ex) {
                	String msg = MessageFormat.format(pattern, file.getName()); 
                	controller.println(msg);
                	controller.incrementErrorsCounter();
                }*/
                
                file = null;
                
                if (controller.getState() == Controller.STATE_ABORTING) 
                	throw new SemanticActionError(null);
                
                controller.checkPoint(i * filesScanPercent / filesToImport.size());
            }

            semAct.finalizeCreation();
            
            
            //------------
            dbObjects = new ArrayList<DbObject>();
            for (DbJVPackage pack : ownedPackages) {
            	DbObject composite = (pack == null) ? model : pack;
                dbObjects.addAll(ModelHelper.getCreatedObjects(composite, true,true,true,true));
            }
            
            /*
            SearchFiltering filter = new SearchFiltering(dbObjects);
            
            List<DbObject> result = filter.search("CreatePackage",false,true,true,true);
            
            for (DbObject obj : result) {
                System.out.println(obj.getName() + " : " + obj.getMetaClass().getGUIName());
            }
            */
            //-------------
            
            
            if (m_params.mustCreateDiagrams()) {
                DiagramCreator diagramCreator = new DiagramCreator(model, ownedPackages, controller,
                        filesScanPercent + finalizeCreationPercent, 100);
                diagramCreator.createDiagrams();
            }
            controller.checkPoint(100);
            db.commitTrans();
        } catch (SemanticActionError e) {
            db.abortTrans();
            if (e.getWrappedException() != null && !(e.getWrappedException() instanceof DbException)) {
                ExceptionHandler.processUncatchedException(null, e.getWrappedException());
            }
        }
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if(o.equals(okButton)) {
            JDialog dialog = new JDialog(ApplicationContext.getDefaultMainFrame(), 
                    JavaSourceReverseLocaleMgr.misc.getString("ImportJavaSourcecode"),
                    true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setLocationRelativeTo(ApplicationContext.getDefaultMainFrame());
            new ShowResultDialog(dialog, modelName, dbObjects, m_project);
            dialog.pack();
            dialog.setVisible(true);
        }
    }
}
