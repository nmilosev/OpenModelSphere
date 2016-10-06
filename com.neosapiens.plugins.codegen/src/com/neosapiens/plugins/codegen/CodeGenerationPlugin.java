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

package com.neosapiens.plugins.codegen;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.gui.task.DefaultController;
import org.modelsphere.jack.gui.task.Worker;
import org.modelsphere.jack.plugins.*;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.or.db.DbORTable;

import com.neosapiens.plugins.codegen.international.LocaleMgr;

public class CodeGenerationPlugin implements Plugin2 {
    private CodeGenerationDialog exportDialog;

    public CodeGenerationPlugin() {
        JFrame mainFrame = ApplicationContext.getDefaultMainFrame();
        exportDialog = new CodeGenerationDialog(mainFrame);
    }

    @Override
    public PluginSignature getSignature() {
        return null;
    }

    @Override
    public String installAction(DefaultMainFrame frame, MainFrameMenu menuManager) {
        return null;
    }

    @Override
    public Class<?>[] getSupportedClasses() {
        Class<?>[] classes = new Class[] { DbJVClassModel.class, DbJVPackage.class,
                DbJVClass.class, DbORTable.class };
        return classes;
    }

    @Override
    public OptionGroup getOptionGroup() {
        return null;
    }

    @Override
    public PluginAction getPluginAction() {
        String name = LocaleMgr.misc.getString("CodeGeneration") + "...";
        PluginAction action = new PluginAction(this, name);
        return action;
    }

    @Override
    public void execute(ActionEvent ev) throws Exception {

        //get generation targets
        List<GenerationTarget> targets = GenerationTarget.getTargets();
        DbObject[] dbos = PluginServices.getSelectedSemanticalObjects();
        exportDialog.setTargets(targets, dbos);

        //show dialog
        exportDialog.setVisible(true);
        GenerationTarget target = exportDialog.getSelectedTarget();
        File outputFolder = exportDialog.getSelectedFile();

        //execute the export task
        if (outputFolder != null) {
            DefaultController controller = new DefaultController(LocaleMgr.misc
                    .getString("CodeGeneration"), false, null);

            PluginServices.multiDbBeginTrans(Db.READ_TRANS, null);

            //create and start the worker
            Worker worker = new CodeGenerationWorker(target, outputFolder, dbos);
            controller.start(worker);

            PluginServices.multiDbCommitTrans();
        } //end if
    } //end execute()
    
    public boolean doListenSelection() { return (this instanceof PluginSelectionListener); }
}
