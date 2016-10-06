/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.plugins.ansi.forward;

import java.awt.Frame;
import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.gui.task.DefaultController;
import org.modelsphere.jack.plugins.PluginMgr;
import org.modelsphere.jack.plugins.PluginServices;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.*;
import org.modelsphere.sms.features.forward.GenerateDialog;
import org.modelsphere.sms.or.features.dbms.DBMSForwardOptions;
import org.modelsphere.sms.templates.BasicForwardToolkit;
import org.modelsphere.sms.templates.GenericForwardEngineeringPlugin;
import org.modelsphere.sms.templates.SQLForwardEngineeringPlugin;

public final class GenericDDLBasicForwardToolkit extends BasicForwardToolkit {
    private static final PluginSignature signature = new PluginSignature(LocaleMgr.misc
            .getString("generateddlonline"), "$Revision: 6 $",
            ApplicationContext.APPLICATION_AUTHOR, "$Date: 2009/04/14 14:00p $", 212);//NOT LOCALIZABLE
    private static final String DIALOG_TITLE = "Generate DDL Statements";
    private ForwardOptions m_options = null;

    public PluginSignature getSignature() {
        return signature;
    }

    public Class getForwardClass() {
        return GenericDDLForwardEngineeringPlugin.class;
    }

    protected ForwardWorker createForwardWorker(ForwardOptions options) {
        ForwardWorker worker = new GenericDDLGenerateWorker(this,
                (GenericDDLForwardOptions) options);
        m_options = options;
        return worker;
    } //end createForwardWorker()

    /////////////////////////////////
    // OVERRIDES  BasicForwardToolkit
    public void generateFile(ForwardOptions options) {
        GenericForwardEngineeringPlugin forward = (GenericForwardEngineeringPlugin) options
                .getForward();

        String defaultDirectory = getRootDirFromUserProp();
        Frame frame = getMainFrame();
        GenerateDialog dialog = new GenerateDialog(frame, forward, defaultDirectory, DIALOG_TITLE);
        AwtUtil.centerWindow(dialog);
        dialog.setVisible(true);

        File actualDirectory = dialog.getActualDirectory();
        if (actualDirectory != null) {
            options.setActualDirectory(actualDirectory);
            Controller controller = new DefaultController(getTitle(), false, DEFAULT_LOG_FILENAME);
            ForwardWorker worker = createForwardWorker(options);
            controller.start(worker);
        } //end if
    } //end generateFile()

    //
    /////////////////////////////////

    /////////////////////////////////
    // OVERRIDES  BasicForwardToolkit
    protected File selectActualDirectory(String defaultDirectory) {
        return null;
    } //end selectActualDirectory()

    public File getSelectedDirectory() {
        return null;
    }

    //
    /////////////////////////////////

    //INNER CLASS
    private static final class GenericDDLGenerateWorker extends ForwardWorker {
        private static final int SUCCESSFUL = 0;
        private static final int CANCELLED = 1;
        private static final int ERRORNEOUS = 2;
        GenericDDLForwardOptions m_options;
        BasicForwardToolkit m_toolkit;

        GenericDDLGenerateWorker(BasicForwardToolkit toolkit, GenericDDLForwardOptions options) {
            m_toolkit = toolkit;
            m_options = options;
        }

        // Return this job's title
        protected String getJobTitle() {
            return m_toolkit.getTitle();
        }

        // IMPORTANT:  Any exceptions that cannot be handled by the subclass or that
        //    can corrupt the operation should be thrown by this method (not cought by this method)
        // If more threads are needed, make sure they make a checkPoint() on a regular basis on the controler
        //    (To allow a return from their run() method).
        protected void runJob() throws Exception {
            if (m_options == null)
                return;

            SQLForwardEngineeringPlugin sqlForward = (SQLForwardEngineeringPlugin) m_options
                    .getForward();
            DbObject[] objects = m_options.getObjects();
            PluginServices.multiDbBeginTrans(Db.READ_TRANS, null);
            File actualDirectory = m_options.getActualDirectory();
            String path = (actualDirectory == null) ? sqlForward.getForwardDirectory()
                    : actualDirectory.getPath();
            GenerateInFileInfo info = sqlForward.getGenerateInFileInfo();
            Controller controller = getController();

            boolean expanded = false;
            int nb = objects.length;
            ArrayList generatedFiles = new ArrayList();
            for (int i = 0; i < nb; i++) {
                DbSemanticalObject object = (DbSemanticalObject) objects[i];
                int status = generateFile(object, sqlForward, path, info, controller,
                        generatedFiles);

                if (status == CANCELLED) {
                    break;
                }
            }

            PluginServices.multiDbCommitTrans();
            super.terminateRunJob(sqlForward, generatedFiles);
        }

        private int generateFile(DbSemanticalObject object, SQLForwardEngineeringPlugin sqlForward,
                String path, GenerateInFileInfo info, Controller controller,
                ArrayList generatedFiles) throws DbException { //TODO catch exceptions
            int status = SUCCESSFUL;

            StringWriter stringWriter = new StringWriter();
            Rule rule = sqlForward.getRuleOf(object);
            String errMsg = null;
            String filename = "." + info.getDefaultExtension();
            File file = null;
            String baseName = object.getName();

            if (rule == null) {
                status = ERRORNEOUS;
                String pattern = NO_RULE_FOR_NAME; //"No rule for {0}.";
                errMsg = MessageFormat.format(pattern, new Object[] { baseName });
            } else {
                try {
                    boolean expanded = rule.expand(stringWriter, object);

                    if (!expanded) {
                        //TODO: tell why
                        status = CANCELLED;
                        return status;
                    }

                    //process edition codes
                    String unprocessed = stringWriter.toString();
                    String processed = EditionCode.processEditionCode(unprocessed);

                    //new filewriter
                    filename = path + SEPARATOR + baseName + "." + info.getDefaultExtension();
                    sqlForward.backupFile(filename);
                    file = new File(filename);
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(processed);
                    fileWriter.close();
                } catch (IOException ex) {
                    errMsg = ex.toString();
                    status = ERRORNEOUS;
                } catch (RuleException ex) {
                    errMsg = ex.toString();
                    status = ERRORNEOUS;
                }

                if (!controller.checkPoint()) {
                    status = CANCELLED;
                } //end if
            } //end if

            switch (status) {
            case SUCCESSFUL:
                String pattern = GENERATING; //"Generating {0}...";
                String msg = MessageFormat.format(pattern, new Object[] { filename });
                controller.println(msg);
                generatedFiles.add(filename);
                break;
            case CANCELLED:
                if (file != null) {
                    file.delete();
                }
                break;
            case ERRORNEOUS:
                String pattern2 = CANNOT_GENERATE; //"Cannot generate {0}:";
                String msg2 = MessageFormat.format(pattern2, new Object[] { filename });
                controller.println(msg2);
                controller.println("  " + errMsg);
                controller.println();
                break;
            } //end switch

            return status;
        } //end generateFile()
    }

    static final class GenericDDLForwardOptions extends DBMSForwardOptions {
        GenericDDLBasicForwardToolkit m_toolkit;
        SQLForwardEngineeringPlugin m_forward = null;

        GenericDDLForwardOptions(GenericDDLBasicForwardToolkit toolkit) {
            m_toolkit = toolkit;
            PluginMgr pluginMgr = PluginMgr.getSingleInstance();
            ArrayList plugins = pluginMgr.getPluginsRegistry().getActivePluginInstances(
                    m_toolkit.getForwardClass());
            if (plugins != null && plugins.size() > 0)
                m_forward = (SQLForwardEngineeringPlugin) plugins.get(0);
        }

        private File m_actualDirectory = null;

        File getActualDirectory() {
            return m_actualDirectory;
        }

        public void setActualDirectory(File actualDirectory) {
            m_actualDirectory = actualDirectory;
        }

        public JackForwardEngineeringPlugin getForward() {
            return m_forward;
        } //end getForward()
    } //end GenericDDLForwardOptions

} //end GenericDDLBasicForwardToolkit
