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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.StringTokenizer;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.gui.task.Worker;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.or.db.DbORTable;

import com.neosapiens.plugins.codegen.international.LocaleMgr;
import com.neosapiens.plugins.codegen.wrappers.DbClassWrapper;
import com.neosapiens.plugins.codegen.wrappers.DbPackageWrapper;
import com.neosapiens.plugins.codegen.wrappers.DbTableWrapper;

public class CodeGenerationWorker extends Worker {
    private GenerationTarget m_target;
    private File m_outputFolder;
    private DbObject[] m_dbos;
    private int m_nbFiles = 0;

    public CodeGenerationWorker(GenerationTarget target, File outputFolder, DbObject[] dbos) {
        m_target = target;
        m_outputFolder = outputFolder;
        m_dbos = dbos;
    }

    @Override
    protected String getJobTitle() {
        String title = LocaleMgr.misc.getString("CodeGeneration");
        return title;
    }

    @Override
    protected void runJob() throws Exception {
        //reset the counter
        m_nbFiles = 0;

        //inform the user which target is executed
        Controller controller = this.getController();
        String name = m_target.getName();
        String pattern = LocaleMgr.misc.getString("Generating0");
        String msg = MessageFormat.format(pattern, name);

        for (DbObject dbo : m_dbos) {
            if (dbo instanceof DbJVClassModel) {
                DbJVClassModel cm = (DbJVClassModel) dbo;
                exportClassModel(cm, m_outputFolder, controller);
            } else if (dbo instanceof DbJVPackage) {
                DbJVPackage pack = (DbJVPackage) dbo;
                exportPackage(pack, m_outputFolder, controller);
            } else if (dbo instanceof DbJVClass) {
                DbJVClass claz = (DbJVClass) dbo;
                exportClass(claz, m_outputFolder, controller);
            } else if (dbo instanceof DbORTable) {
                DbORTable tab = (DbORTable) dbo;
                exportTable(tab, m_outputFolder, controller);
            }
        } //end for

        //if success
        if (controller.getErrorsCount() == 0) {
            pattern = LocaleMgr.misc.getString("Success0");
            msg = MessageFormat.format(pattern, m_nbFiles);
            controller.println();
            controller.println(msg);
        }
    } //end runJob()

    private void exportClassModel(DbJVClassModel cm, File outputFolder, Controller controller)
            throws DbException, IOException {

        //for each class
        DbRelationN relN = cm.getComponents();
        DbEnumeration enu = relN.elements(DbJVClass.metaClass);
        while (enu.hasMoreElements()) {
            DbJVClass claz = (DbJVClass) enu.nextElement();
            exportClass(claz, outputFolder, controller);
        }
        enu.close();

        //for each package
        relN = cm.getComponents();
        enu = relN.elements(DbJVPackage.metaClass);
        while (enu.hasMoreElements()) {
            DbJVPackage pack = (DbJVPackage) enu.nextElement();
            String name = pack.getName();
            File folder = createFile(outputFolder, name);
            exportPackage(pack, folder, controller);
        }
        enu.close();
    }

    private void exportPackage(DbJVPackage pack, File outputFolder, Controller controller)
            throws DbException, IOException {

        String folderName = pack.buildFullNameString().replace('.', '/');
        String packageName = pack.getName();
        File templateFolder = m_target.getTemplateFile().getParentFile();

        //for each package directive
        List<GenerationTarget.Directive> directives = m_target.getDirectives();
        for (GenerationTarget.Directive directive : directives) {
            if (DbJVPackage.metaClass.equals(directive.m_metaClass)) {
                String pattern = directive.getOutputFilePattern();
                String filename = MessageFormat.format(pattern, folderName, packageName);
                File file = createFile(outputFolder, filename);
                String templateName = directive.getTemplateName();
                File templateFile = new File(templateFolder, templateName);

                VelocityWriter vw = new VelocityWriter(templateFile, m_target.getRoot(),
                        new FileWriter(file));
                vw.setVariable("package", new DbPackageWrapper(pack), controller);
                vw.writeObject(controller);
                vw.close();

                pattern = LocaleMgr.misc.getString("Generating0");
                String msg = MessageFormat.format(pattern, file.getPath());
                controller.println(msg);
                m_nbFiles++;
            }
        } //end for

        //for each class
        DbRelationN relN = pack.getComponents();
        DbEnumeration enu = relN.elements(DbJVClass.metaClass);
        while (enu.hasMoreElements()) {
            DbJVClass claz = (DbJVClass) enu.nextElement();
            exportClass(claz, outputFolder, controller);
        }
        enu.close();

        //for each package
        relN = pack.getComponents();
        enu = relN.elements(DbJVPackage.metaClass);
        while (enu.hasMoreElements()) {
            DbJVPackage subpack = (DbJVPackage) enu.nextElement();
            String name = subpack.getName();
            //File folder = createFile(packageFolder, name); 
            exportPackage(subpack, outputFolder, controller);
        }
        enu.close();
    }

    private void exportClass(DbJVClass dbClaz, File folder, Controller controller)
            throws DbException {

        //get class name
        DbJVPackage pack = (DbJVPackage) dbClaz.getCompositeOfType(DbJVPackage.metaClass);
        String foldername = ".";
        if (pack != null) {
            foldername = pack.buildFullNameString().replace('.', '/');
        }
        String classname = dbClaz.getName();
        File templateFolder = m_target.getTemplateFile().getParentFile();

        //for each target's class directive
        try {
            List<GenerationTarget.Directive> directives = m_target.getDirectives();
            for (GenerationTarget.Directive directive : directives) {
                if (DbJVClass.metaClass.equals(directive.m_metaClass)) {
                    DbClassWrapper claz = new DbClassWrapper(dbClaz);
                    boolean condition = directive.checkClassCondition(claz);

                    if (condition) {
                        String pattern = directive.getOutputFilePattern();
                        String filename = MessageFormat.format(pattern, foldername, classname);
                        File file = createFile(folder, filename);

                        String templateName = directive.getTemplateName();
                        folder.mkdirs();

                        //fill with a Velocity script
                        File templateFile = createFile(templateFolder, templateName);

                        //get templateName
                        VelocityWriter vw = new VelocityWriter(templateFile, m_target.getRoot(),
                                new FileWriter(file));
                        vw.setVariable("class", claz, controller);
                        vw.writeObject(controller);
                        vw.close();

                        pattern = LocaleMgr.misc.getString("Generating0");
                        String msg = MessageFormat.format(pattern, file.getPath());
                        controller.println(msg);
                        m_nbFiles++;
                    }//end if
                }//end if
            } //end for

        } catch (Exception ex) {
            controller.println(ex.toString());
            controller.incrementErrorsCounter();
        } //end try
    } //end exportClass()

    private void exportTable(DbORTable dbTable, File folder, Controller controller)
            throws DbException {
        String foldername = "folder";
        String tablename = dbTable.getName();
        File templateFolder = m_target.getTemplateFile().getParentFile();

        //for each target's table directive
        try {
            List<GenerationTarget.Directive> directives = m_target.getDirectives();
            for (GenerationTarget.Directive directive : directives) {
                if (DbORTable.metaClass.equals(directive.m_metaClass)) {
                    DbTableWrapper tab = new DbTableWrapper(dbTable);

                    String pattern = directive.getOutputFilePattern();
                    String filename = MessageFormat.format(pattern, foldername, tablename);
                    File file = createFile(folder, filename);

                    String templateName = directive.getTemplateName();
                    folder.mkdirs();

                    //fill with a Velocity script
                    File templateFile = createFile(templateFolder, templateName);

                    //get templateName
                    VelocityWriter vw = new VelocityWriter(templateFile, m_target.getRoot(),
                            new FileWriter(file));
                    vw.setVariable("table", tab, controller);
                    vw.writeObject(controller);
                    vw.close();

                    pattern = LocaleMgr.misc.getString("Generating0");
                    String msg = MessageFormat.format(pattern, file.getPath());
                    controller.println(msg);
                    m_nbFiles++;
                }//end if
            } //end for

        } catch (Exception ex) {
            controller.println(ex.toString());
            controller.incrementErrorsCounter();
        } //end try

    }

    private File createFile(File folder, String pathname) {
        StringTokenizer st = new StringTokenizer(pathname, "/");
        File file = null;

        while (st.hasMoreTokens()) {
            String token = st.nextToken();

            if (st.hasMoreElements()) {
                folder = new File(folder, token);
                folder.mkdir();
            } else {
                file = new File(folder, token);
            } //end if
        } //end while

        return file;
    }

}
