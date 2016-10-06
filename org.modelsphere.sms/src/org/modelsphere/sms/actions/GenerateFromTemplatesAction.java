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

package org.modelsphere.sms.actions;

import java.beans.BeanInfo;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.awt.tree.CheckTreeNode;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.debug.TestException;
import org.modelsphere.jack.debug.Testable;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.gui.task.DefaultController;
import org.modelsphere.jack.gui.task.GuiController;
import org.modelsphere.jack.io.PathFile;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.EditionCode;
import org.modelsphere.jack.srtool.forward.FileModifier;
import org.modelsphere.jack.srtool.forward.ForwardWorker;
import org.modelsphere.jack.srtool.forward.ImportClause;
import org.modelsphere.jack.srtool.forward.Rule;
import org.modelsphere.jack.srtool.forward.RuleException;
import org.modelsphere.jack.srtool.forward.Template;
import org.modelsphere.jack.srtool.forward.VariableDecl;
import org.modelsphere.jack.srtool.forward.VariableScope;
import org.modelsphere.jack.templates.TemplateDialog;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.SMSIntegrateModelUtil;
import org.modelsphere.sms.db.DbSMSAbstractPackage;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.features.dbms.DBMSForwardOptions;
import org.modelsphere.sms.preference.DirectoryOptionGroup;
import org.modelsphere.sms.templates.GenericForwardEngineeringPlugin;

/**
 * 
 * Must be used in debug only
 * 
 */
public class GenerateFromTemplatesAction extends AbstractApplicationAction implements
        SelectionActionListener, Testable {
    private static GenerateFromTemplatesAction singleton;
    // Actions
    private static final String TITLE = MessageFormat.format(LocaleMgr.action
            .getString("Generate0"), new Object[] { LocaleMgr.action.getString("Templates") });
    private static final String HELP = MessageFormat.format(
            LocaleMgr.action.getString("Generate0"), new Object[] { LocaleMgr.action
                    .getString("Templates") });
    private static final String TPL_DESC = LocaleMgr.action.getString("TemplateFileDescription");
    private static final String SELECT = LocaleMgr.screen.getString("Select");
    private static final String TPL_CHOOSER_TITLE = LocaleMgr.action
            .getString("TemplateChooserTitle");
    // Messages
    private static final String SYN_ERR_PATTRN = LocaleMgr.message.getString("SYN_ERR_PATTRN");
    private static final String SYN_ERR_MSG = LocaleMgr.message.getString("SYN_ERR_MSG");
    private static final String ONE_SUCC_PATTRN = LocaleMgr.message.getString("ONE_SUCC_PATTRN");
    private static final String MANY_SUCC_PATTRN = LocaleMgr.message.getString("MANY_SUCC_PATTRN");
    private static final String ERR_MSG_PATTERN = LocaleMgr.message.getString("ERR_MSG_PATTERN");
    private static final String FILE_NOT_FOUND_PATTERN = LocaleMgr.message
            .getString("FILE_NOT_FOUND_PATTERN");
    private static final String ERROR = LocaleMgr.message.getString("Error");
    private static final String SUCCESS = LocaleMgr.message.getString("SUCCESS");
    // Misc
    private static final String ERROR_LOG = LocaleMgr.misc.getString("ERROR_LOG");
    private static final int TEMPLATE_DIAG_WIDTH = 350;
    private static final int TEMPLATE_DIAG_HEIGHT = 350;

    private static final String PROCESSING_0 = LocaleMgr.message.getString("PROCESSING_0");
    private static final String READING_0 = LocaleMgr.message.getString("READING_0");
    private static final String TEMPLATE_PROCESSING = LocaleMgr.message
            .getString("TEMPLATE_PROCESSING");

    protected static String TPL_EXTENSION = "tpl"; // NOT LOCALIZABLE

    // not set static final because it must be updated each time the action is
    // performed
    protected static String g_outputDir = DirectoryOptionGroup.getDefaultWorkingDirectory();
    protected static String g_templateDir = DirectoryOptionGroup.getDefaultWorkingDirectory();

    GenerateFromTemplatesAction() {
        super(TITLE);
        this.setMnemonic(LocaleMgr.action.getMnemonic("Templates"));
        setEnabled(false);
        this.setHelpText(HELP);
    }

    protected GenerateFromTemplatesAction(String title) {
        super(title);
        this.setMnemonic(LocaleMgr.action.getMnemonic("Templates"));
        setEnabled(false);
        this.setHelpText(HELP);
    }

    public void updateSelectionAction() throws DbException {
        DbObject[] semObjs = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        boolean enable = (semObjs.length == 1);
        if (enable) {
            DbObject semObj = semObjs[0];
            if (!(semObj instanceof DbSMSPackage)) {
                enable = false;
            } // end if
        } // end if

        setEnabled(enable);
    }

    protected File selectTemplateFile() {
        JFileChooser fc = new JFileChooser(g_templateDir);
        fc.setDialogTitle(TPL_CHOOSER_TITLE);
        ExtensionFileFilter filter = new ExtensionFileFilter(new String[] { TPL_EXTENSION },
                TPL_DESC);
        fc.addChoosableFileFilter(filter);
        fc.setFileFilter(filter);
        File file = null;

        if (JFileChooser.APPROVE_OPTION == fc.showDialog(MainFrame.getSingleton(), SELECT)) {
            g_templateDir = fc.getSelectedFile().getParent();
            file = fc.getSelectedFile();
        }

        return file;
    } // end chooseWorkingDirectory()

    public static ArrayList getConditions(VariableScope varList) {
        ArrayList conditions = new ArrayList();

        // get conditions
        VariableScope map = varList;
        Set set = map.getKeySet();
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            VariableDecl.VariableStructure variable = (VariableDecl.VariableStructure) map
                    .getVariable(key);
            if (variable.isExtern()) {
                // TODO : and if variable is a BOOLEAN..
                conditions.add(variable);
            }
        } // end while

        return conditions;
    }

    protected final void doActionPerformed() {

        // Select a .tpl file with a File Dialog
        File file = selectTemplateFile();

        // if no file selected, exit (user has CANCELed the file selection)
        if (file == null) {
            return;
        }

        DbObject[] objects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();

        if (objects[0] instanceof DbSMSPackage) {
            DbSMSPackage abstractPackage = (DbSMSPackage) objects[0];
            processObjectsWithTemplate(abstractPackage, file);
        } // end if

    } // end doActionPerformed()

    private TemplateGenericForward m_fwd = new TemplateGenericForward();

    private void processObjectsWithTemplate(DbSMSPackage abstractPackage, File file) {
        //
        // Spawn the worker
        //
        TemplateForwardOptions options = new TemplateForwardOptions(abstractPackage, file, m_fwd);
        TemplateForwardWorker worker = new TemplateForwardWorker(options);
        boolean progressBar = false;
        DefaultController controller = new DefaultController(TEMPLATE_PROCESSING, progressBar,
                ERROR_LOG);
        controller.start(worker);
    } // end processObjectsWithTemplate()

    //
    // INNER CLASSES
    //
    private static class TemplateGenericForward extends GenericForwardEngineeringPlugin {
        String m_filename = null;

        private VariableScope m_varScope = new VariableScope(TITLE);

        public VariableScope getVarScope() {
            return m_varScope;
        }

        public void setVarScope(VariableScope varScope) {
            m_varScope = varScope;
        }

        public void execute(DbObject[] semObjs) throws DbException {
        }

        public void setOutputToASCIIFormat() {
        }

        protected void forwardTo(DbObject semObj, ArrayList generatedFiles) throws DbException,
                IOException, RuleException {
            // has to be coded
        }

        // /////////////////
        // OVERRIDES Plugin
        public PluginSignature getSignature() {
            return null; // not a plug-in, altough extends GenericForward
        }

        public Class[] getSupportedClasses() {
            return null;
        }

        //
        // /////////////////

        private static int cnt = 0;

        TemplateGenericForward() {
        } // end of TemplateGenericForward()

        private void setFile(File file) {
            m_filename = file.getPath();
        }

        protected Template getFileNotFoundRule() {
            String msg = MessageFormat.format(FILE_NOT_FOUND_PATTERN, new Object[] { m_filename });
            Template tmpl = new Template(null, msg);
            return tmpl;
        }

        protected String getTemplateFileName() {
            return m_filename;
        }

        public Rule getRuleOf(DbObject obj) {
            Rule rule = null;

            if (obj instanceof DbSMSProject) {
                rule = getProjectRule();
            } else if (obj instanceof DbORDataModel) {
                rule = getDataModelRule();
            }

            return rule;
        } // end getRuleOf()

        protected Rule getRule(String rulename, URL url, Controller controller) {
            Rule rule = super.getRule(rulename, url); // Parse the .tpl here
            return rule;
        }

        private void getExternalItems(Controller controller, ArrayList externalRules) {

            // empty rule table
            m_ruletable.clear();
            m_successfullyInitialized = false; // to re-init
            Rule.RuleOptions options = null;

            // fill rule table
            String rulename = null;
            String filename = getTemplateFileName();
            File file = new File(filename);
            try {
                URL url = file.toURI().toURL();
                
                //parse the .tpl here
                Rule errorRule = getRule(rulename, url, controller); 
         
                if (errorRule != null) { // if syntax error
                    externalRules.add(errorRule);
                    controller.incrementErrorsCounter();
                    return; // rules;
                }

                Enumeration enumeration = m_ruletable.elements();
                while (enumeration.hasMoreElements()) {
                    Rule rule = (Rule) enumeration.nextElement();
                    if (rule.externModifier != null) {
                        externalRules.add(rule);
                    }
                } // end while
            } catch (Exception e) {
                //
            }
        } // end getExternalItems()

        // may return null
        private ImportClause getImportClause() {
            ImportClause importClause = null;
            Enumeration enumeration = m_ruletable.elements();
            while (enumeration.hasMoreElements()) {
                Rule rule = (Rule) enumeration.nextElement();
                if (rule instanceof ImportClause) {
                    importClause = (ImportClause) rule;
                    break;
                }
            } // end while
            return importClause;
        } // end getImportClause()

        //
        // Bean support for parameters
        //
        private ImportClause m_importClause = null;

        public void setBeanFields() {
            try {
                m_importClause = getImportClause();
                if (m_importClause != null) {
                    Writer writer = null;
                    DbObject obj = null;
                    Rule.RuleOptions options = null;
                    m_importClause.expand(writer, obj, options);
                }
            } catch (RuleException ex) {
                m_importClause = null;
            }
        } // end setBeanFields

        private Serializable getBean() {
            Serializable bean = null;

            if (m_importClause != null) {
                bean = m_importClause.getBean();
            }

            return bean;
        }

        private BeanInfo getBeanInfo() {
            BeanInfo beanInfo = null;

            if (m_importClause != null) {
                beanInfo = m_importClause.getBeanInfo();
            }

            return beanInfo;
        }

        private String getFileName() {
            String filename = null;

            if (m_importClause != null) {
                filename = m_importClause.getFileName();
            }

            return filename;
        }

    } // end TemplateGenericForward

    //
    // TemplateForwardOptions
    //
    private static final class TemplateForwardOptions {
        DbSMSPackage m_abstractPackage;
        File m_file;
        TemplateGenericForward m_fwd;

        TemplateForwardOptions(DbSMSPackage abstractPackage, File file, TemplateGenericForward fwd) {
            m_abstractPackage = abstractPackage;
            m_file = file;
            m_fwd = fwd;
        }
    }

    //
    // TemplateForwardWorker
    //
    private static final class TemplateForwardWorker extends ForwardWorker {
        TemplateForwardOptions m_options;
        CheckTreeNode m_fieldTree = null;

        TemplateForwardWorker(TemplateForwardOptions options) {
            m_options = options;
        }

        protected String getJobTitle() {
            return TEMPLATE_PROCESSING;
        }

        private void processError(Rule rule, Serializable obj) {

            if (obj instanceof DbObject) {
                DbObject dbObj = (DbObject) obj;
                Db db = dbObj.getDb();
                try {
                    db.beginTrans(Db.READ_TRANS);
                } catch (DbException ex) {
                    return;
                } // end try
            } // end if

            StringWriter swriter = new StringWriter();
            String s;

            try {
                Rule.RuleOptions options = null;
                rule.expand(swriter, obj, options);
                String unprocessedEditionCode = swriter.toString();
                s = EditionCode.processEditionCode(unprocessedEditionCode);
            } catch (RuleException ex) {
                s = ex.toString();
            } catch (IOException ex) {
                s = ex.toString();
            } // end try

            getController().println(s);

            if (obj instanceof DbObject) {
                DbObject dbObj = (DbObject) obj;
                Db db = dbObj.getDb();
                try {
                    db.commitTrans();
                } catch (DbException ex) {
                    // ignore
                } // end try
            } // end if
        } // end processError

        // called by runJob()
        // modifies m_fieldTree
        private ArrayList displayExternalRules(ArrayList rules, DbSMSPackage abstractPackage,
                VariableScope varList, VariableScope variableList, String scopeDir)
                throws DbException {

            ArrayList externalRules = new ArrayList();

            // get EXTERNal rules
            Iterator iter = rules.iterator();
            while (iter.hasNext()) {
                Rule rule = (Rule) iter.next();
                if (rule.externModifier != null) {
                    externalRules.add(rule);
                }
            } // end while

            // get controller's dialog
            Controller controller = getController();
            if (!(controller instanceof GuiController)) {
                return externalRules;
            } else {
                GuiController guiController = (GuiController) controller;

                JDialog parentDialog = guiController.getDialog();

                // get conditions
                ArrayList conditions = getConditions(varList);

                SMSIntegrateModelUtil util = SMSIntegrateModelUtil.getSingleInstance();
                boolean deepTraversal = true;
                m_fieldTree = util.getFieldTree(abstractPackage, deepTraversal);

                TemplateDialog diag = new TemplateDialog(parentDialog, externalRules, conditions,
                        variableList, m_fieldTree, scopeDir);
                diag.pack();
                diag.center(); // center the dialog ONCE its size is set
                diag.setModal(true);
                diag.setVisible(true); // the user enters the values HERE

                try {
                    // sleep this thread until diag ceases to be visible
                    while (diag.isVisible()) {
                        Thread.sleep(250);
                    } // end while
                } catch (InterruptedException ex) {
                    getController().cancel();
                }

                ArrayList selectedRules = diag.getSelectedRules();
                diag.getSetConditions(varList); // get IF and IFNOT conditions

                return selectedRules;
            }
        } // displayExternalRules

        // called by runJob()
        private ArrayList getExternalRules(DbSMSPackage abstractPackage, File file)
                throws DbException {

            // get EXTERN rules
            TemplateGenericForward fwd = m_options.m_fwd;

            fwd.setFile(file);
            Controller controller = getController();
            ArrayList externalRules = new ArrayList();
            ArrayList externalVariables = new ArrayList();
            fwd.getExternalItems(controller, externalRules); // Parse the .tpl
            // here
            boolean errorWhileReadingFile = false;

            if (externalRules.size() == 0) {
                controller.println(SYN_ERR_MSG);
                return null;
            } else if (externalRules.size() == 1) {
                Rule rule = (Rule) externalRules.get(0);
                if (rule.isError) {
                    errorWhileReadingFile = true;
                    processError(rule, abstractPackage);
                    return null;
                }
            } // end if

            ArrayList selectedRules = null; // and conditions?
            if (!errorWhileReadingFile) {
                VariableScope variableScope = fwd.getVarScope();
                String filename = fwd.getFileName();
                String scopedir = file.getParent(); // scope directory is the
                // same as template
                // directory
                VariableScope varList = fwd.getVarScope();
                selectedRules = displayExternalRules(externalRules, abstractPackage, varList,
                        variableScope, scopedir);
                if ((selectedRules == null) || (selectedRules.size() == 0)) {
                    return null; // no rules selected if the user has CANCELled
                    // the operation
                }
            } else {
                selectedRules = new ArrayList();
                Rule rule = (Rule) externalRules.get(0);
                selectedRules.add(rule);
            } // end if

            return selectedRules;
        } // end displayExternalRules()

        // called by runJob()
        private boolean generateTemplate(DbObject selectedObject, VariableScope varList,
                ArrayList selectedRules, Controller controller) {

            String errorMessage = null;
            boolean isSuccessful;
            String errorMessagePrinted = null;
            String filename = null; // if null, output in worker's display
            FileModifier.g_generatedFiles.clear();

            Db db = null;
            if (selectedObject != null) {
                db = selectedObject.getDb();
                try {
                    db.beginTrans(Db.READ_TRANS);
                } catch (DbException ex) {
                    return false;
                } // end try
            } // end if

            Iterator iter = selectedRules.iterator();
            isSuccessful = true;
            try {
                while (iter.hasNext()) {
                    Rule rule = (Rule) iter.next();

                    FileModifier mod = rule.fileModifier;
                    Writer writer = null;
                    try {
                        if (mod != null) {
                            Rule filerule = mod.getRule();
                            StringWriter sw = new StringWriter();
                            Rule.RuleOptions options = null;
                            filerule.expand(sw, selectedObject, options);
                            filename = sw.toString();
                        } // end if

                        StringWriter swriter = new StringWriter();
                        ArrayList excludeList = null;
                        if (m_fieldTree != null) {
                            excludeList = DBMSForwardOptions.getExcludeList(m_fieldTree);
                        }

                        DbObject refObject = null;
                        MetaField[] metaFields = null;
                        Rule.RuleOptions options = new Rule.RuleOptions(refObject, metaFields,
                                controller, excludeList);
                        rule.expand(swriter, selectedObject, options);
                        String s = null;

                        if (rule.fileModifier == null) { // otherwise already
                            // generated in file
                            String unprocessedEditionCode = swriter.toString();
                            s = EditionCode.processEditionCode(unprocessedEditionCode);
                            if (filename != null) {
                                if (!PathFile.isAbsolutePath(filename)) {
                                    filename = g_outputDir + PathFile.SEPARATOR + filename;
                                } // end if
                            } // end if
                        } // end if

                        if (filename == null) {
                            writer = new StringWriter();
                        } // end if

                        if (rule.fileModifier == null) { // otherwise already
                            // generated in file
                            if (s != null) {
                                writer.write(s);
                                writer.flush();
                            } // end if

                            writer.close();
                            String actualFileName = filename;
                            FileModifier.g_generatedFiles.add(actualFileName);
                        } // end if

                    } catch (RuleException ex) {
                        if (writer == null) {
                            if (filename != null) {
                                if (!PathFile.isAbsolutePath(filename)) {
                                    filename = g_outputDir + PathFile.SEPARATOR + filename;
                                } // end if

                                File outputFile = PathFile.createFile(filename, true);
                                writer = new FileWriter(outputFile);
                            } // end if
                        } // end if

                        if (writer != null) {
                            writer.write(ex.toString());
                            writer.close();
                        }

                        errorMessage = ex.toString();
                        getController().println(errorMessage);
                        errorMessagePrinted = errorMessage;
                        isSuccessful = false;
                        break;
                    } // end try

                    // display result in the controller's diplay instead of in a
                    // file
                    if (writer instanceof StringWriter) {
                        getController().println();
                        String result = writer.toString();
                        getController().println(result);
                        getController().println();
                    } // end if
                } // end while

            } catch (IOException ex) {
                errorMessage = ex.toString();
                isSuccessful = false;
            } // end try

            if (db != null) {
                try {
                    db.commitTrans();
                } catch (DbException ex) {
                    errorMessage = ex.toString();
                    isSuccessful = false;
                }
            } // end if

            // TODO: Display a successful message, according isSuccessful
            if (isSuccessful) {
                String message = null;
                int nb_files = FileModifier.g_generatedFiles.size();
                if (nb_files == 1) {
                    String genFilename = (String) FileModifier.g_generatedFiles.get(0);
                    if (genFilename != null) {
                        message = MessageFormat.format(ONE_SUCC_PATTRN,
                                new Object[] { genFilename });
                    }
                } else {
                    String genFilenames = "";
                    int fileCounter = 0;
                    for (int i = 0; i < nb_files; i++) {
                        String currFile = (String) FileModifier.g_generatedFiles.get(i);
                        if (currFile != null) {
                            genFilenames += currFile;
                            fileCounter++;
                            if (i < (nb_files - 1)) {
                                genFilenames += ",\n"; // NOT LOCALIZABLE,
                                // escape code
                            } // end if
                        } // end if
                    } // end for

                    if (fileCounter > 0) {
                        message = MessageFormat.format(MANY_SUCC_PATTRN,
                                new Object[] { new Integer(fileCounter) });
                        message += ": " + genFilenames;
                    }
                } // end if

                if (message != null) {
                    getController().println(message);
                }
            } else { // if not successful
                if (errorMessage == null) {
                    errorMessage = MessageFormat.format(ERR_MSG_PATTERN, new Object[] { filename });
                }

                // Avoid to write twice the same message
                if (errorMessagePrinted != null) {
                    if (!errorMessagePrinted.equals(errorMessage)) {
                        getController().println(errorMessage);
                    }
                }
            } // end if

            FileModifier.g_generatedFiles.clear();
            return isSuccessful;
        } // generateTemplate()

        // perform the forward
        public void runJob() throws Exception {
            String filename = m_options.m_file.getPath();
            String message = MessageFormat.format(READING_0, new Object[] { filename });
            getController().println(message);

            DbSMSPackage abstractPackage = m_options.m_abstractPackage;
            TemplateGenericForward fwd = m_options.m_fwd;
            ArrayList selectedRules = getExternalRules(m_options.m_abstractPackage,
                    m_options.m_file); // Parse the
            // .tpl here
            ImportClause importClause = fwd.getImportClause();
            if (importClause != null) {
                Writer writer = null;
                DbObject obj = null;
                Rule.RuleOptions options = null;
                importClause.expand(writer, obj, options);
            } // end if

            message = MessageFormat.format(PROCESSING_0, new Object[] { filename });
            Controller controller = getController();
            controller.println(message);

            // if == null, user has cancelled: not successful
            boolean isSuccessful = true;
            if (selectedRules == null) {
                controller.cancel();
            } else {
                isSuccessful &= generateTemplate(abstractPackage, fwd.getVarScope(), selectedRules,
                        controller);
            } // end if

            if (!isSuccessful) {
                controller.cancel();
                // controller.setStatusText("Error"); doesn't seem to work
            } // end if
        } // end runJob()
    } // end TemplateForwardWorker

    // ////////////////////////////////////////////////////////////////////////////
    // MAIN (unit tests)
    // ////////////////////////////////////////////////////////////////////////////

    // This array contains all the templates to be tested by the unit test
    //
    private static final File[] g_templatesToTest = new File[] { new File(
            "C:/Documents and Settings/Open ModelSphere/templates/positiveTesting/boolean.tpl") // NOT
    // LOCALIZABLE,
    // unit
    // test
    // Add other templates to test here
    };

    //
    // UNIT TEST: create a buffer, and print it line by line
    //
    public void test() throws TestException {
        if (Testable.ENABLE_TEST) {
            org.modelsphere.sms.Application.initMeta();
            boolean success = true;

            for (int i = 0; i < g_templatesToTest.length; i++) {
                File file = g_templatesToTest[i];

                String obj = "HelloWorld"; // NOT LOCALIZABLE, unit test
                DbSMSAbstractPackage absPack = null;
                TemplateGenericForward fwd = new TemplateGenericForward();
                TemplateForwardOptions options = new TemplateForwardOptions(absPack, file, fwd);
                TemplateForwardWorker worker = new TemplateForwardWorker(options);
                boolean progressBar = false;
                String logFilename = "error.log"; // NOT LOCALIZABLE, unit test
                String title = "Template Processing"; // NOT LOCALIZABLE, unit
                // test

                Controller controller = new Controller();
                controller.start(worker);
                boolean finalState;
                do {
                    finalState = controller.isFinalState();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        break;
                    } // end try
                } while (!finalState);

                int state = controller.getState();
                if (state != Controller.STATE_COMPLETED) {
                    String msg = "not completed"; // NOT LOCALIZABLE, unit test
                    throw new TestException(msg);
                } // end if

                if (controller.getErrorsCount() > 0) {
                    String msg = "errorCount = " + controller.getErrorsCount(); // NOT
                    // LOCALIZABLE,
                    // unit
                    // test
                    throw new TestException(msg);
                } // end if
            } // end for
        } // end if
    } // end test()

    public static Testable createInstanceForTesting() throws TestException {
        GenerateFromTemplatesAction action = new GenerateFromTemplatesAction();
        return action;
    }

    public static void main(String[] args) {
        try {
            Testable testcase = createInstanceForTesting();
            testcase.test();
            System.out.println("success.."); // NOT LOCALIZABLE, unit test
        } catch (TestException ex) {
            System.out.println("fails"); // NOT LOCALIZABLE, unit test
        } // end try
    } // end main()
} // end GenerateFromTemplateAction
