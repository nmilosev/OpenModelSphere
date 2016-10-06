/*************************************************************************

Copyright (C) 2009 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.or.features.dbms;

//Java
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.modelsphere.jack.awt.tree.CheckTreeNode;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.plugins.PluginMgr;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.plugins.PluginDescriptor;
import org.modelsphere.jack.plugins.PluginsRegistry;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.*;
import org.modelsphere.jack.srtool.plugins.generic.dbms.ObjectScope;
import org.modelsphere.jack.srtool.plugins.generic.dbms.ObjectSelection;
import org.modelsphere.jack.srtool.plugins.generic.dbms.UserInfo;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.CollationComparator;
import org.modelsphere.jack.util.StringUtil;
import org.modelsphere.sms.db.DbSMSAbstractPackage;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.templates.SQLForwardEngineeringPlugin;

public final class DBMSForwardWorker extends ForwardWorker {
    private static final String EOL = System.getProperty("line.separator"); // NOT
    // LOCALIZABLE,
    // property
    // key
    private static final String TAB = "    "; // 4 spaces use as TAB NOT
    // LOCALIZABLE, property key
    private static final String COMMENT_SEPARATOR = "***********************************************************"
            + EOL; // NOT LOCALIZABLE, property key
    private static final String END_COMMENT = TAB + LocaleMgr.misc.getString("End") + EOL + EOL; // NOT LOCALIZABLE,
    // property key
    private static final String CREATE_STATEMENT_COMMENT = TAB
            + LocaleMgr.misc.getString("CreateStatement") + EOL; // NOT
    // LOCALIZABLE,
    // property
    // key
    private static final String DROP_STATEMENT_COMMENT = TAB
            + LocaleMgr.misc.getString("DropStatement") + EOL; // NOT
    // LOCALIZABLE,
    // property key
    private static final String EMPTY_NAME = LocaleMgr.misc.getString("emptyToken"); // NOT LOCALIZABLE, property key
    private static final String DATABASE = LocaleMgr.misc.getString("databaseForwardHeader"); // NOT LOCALIZABLE, property
    // key
    private static final String SMS_FILE = LocaleMgr.misc.getString("sourceFileForwardHeader"); // NOT LOCALIZABLE, property
    // key
    private static final String GENERATE_ON = LocaleMgr.misc.getString("generateOnForwardHeader"); // NOT LOCALIZABLE, property
    // key
    private static final String PLUGINS_NAME = LocaleMgr.misc.getString("pluginsNameForwardHeader"); // NOT LOCALIZABLE, property
    // key
    private static final String SQL_FWD_NOT_INSTALLED = LocaleMgr.misc
            .getString("SQLForwardNotProperlyInst"); // NOT LOCALIZABLE,
    // property key

    private static final String PLUGIN_PATTERN = LocaleMgr.misc.getString("PLUGIN_PATTERN");
    private static final String GENERATED_BY_PATTERN2 = LocaleMgr.misc
            .getString("GENERATED_BY_PATTERN2");
    private static final String FOR_INFO_PATTERN2 = LocaleMgr.misc.getString("FOR_INFO_PATTERN2");
    private static final String FORWARD_ENG = LocaleMgr.misc.getString("ForwardEng");

    private DBMSForwardOptions m_options;

    public DBMSForwardWorker(DBMSForwardOptions options) {
        if (options == null)
            throw new NullPointerException();
        this.m_options = options;
    }

    protected String getJobTitle() {
        return FORWARD_ENG;
    }

    private void displayError(ForwardToolkitPlugin kit) {
        PluginMgr mgr = PluginMgr.getSingleInstance();

        Class claz = kit.getForwardClass();
        PluginDescriptor pluginDescriptor = mgr.getPluginsRegistry().getPluginInfo(claz);
        String msg = (pluginDescriptor != null ? pluginDescriptor.getStatusText()
                : SQL_FWD_NOT_INSTALLED);
        Controller controller = getController();
        controller.println(msg);
        controller.cancel();
    } // end displayError()

    // perform the forward
    public void runJob() throws Exception {
        ForwardToolkitPlugin.setActiveDiagramTarget(m_options.getTargetSystemId());
        ForwardToolkitPlugin kit = ForwardToolkitPlugin.getToolkit();
        PluginMgr mgr = PluginMgr.getSingleInstance();
        PluginsRegistry registry = mgr.getPluginsRegistry();
        Class filterClass = kit.getForwardClass(); 
        List plugins = registry.getActivePluginInstances(filterClass);
        
        //ArrayList plugins = mgr.getPluginsRegistry()
        //        .getActivePluginInstances(kit.getForwardClass());
        SQLForwardEngineeringPlugin sqlForward = null;
        if (plugins != null && plugins.size() > 0)
            sqlForward = (SQLForwardEngineeringPlugin) plugins.get(0);
        if (sqlForward == null) {
            displayError(kit);
            return;
        }

        ArrayList generatedFiles = new ArrayList();
        String filename = m_options.getOutputFile();
        FileWriter fileWriter = new FileWriter(filename);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        boolean isHtml = m_options.getOutputInHtml();
        writeHeader(printWriter, sqlForward, isHtml);
        ObjectScope[] scope = m_options.getObjectsScope();
        ObjectScope userScope = ObjectScope.findConceptInObjectScopeWithConceptName(scope,
                DbORUser.metaClass.getGUIName());
        CheckTreeNode treeNode = m_options.getCheckTreeNode();
        ArrayList excludeList = DBMSForwardOptions.getExcludeList(treeNode);
        DbSMSAbstractPackage pack = m_options.getAbstractPackage();
        pack.getDb().beginTrans(Db.READ_TRANS);

        Controller controller = getController();
        controller.println(TAB + m_options.getOutputFile());
        switch (m_options.statementToGenerate) {
        case DBMSForwardOptions.CREATE_STATEMENTS:
            doCreationStatements(fileWriter, scope, userScope,
                    DBMSForwardOptions.CREATE_ADD_FORWARD, sqlForward, controller, excludeList,
                    isHtml);
            break;
        case DBMSForwardOptions.DROP_STATEMENTS:
            doDropStatements(fileWriter, scope, userScope, DBMSForwardOptions.DROP_DELETE,
                    sqlForward, controller, excludeList, isHtml);
            break;
        case DBMSForwardOptions.DROP_CREATE_STATEMENTS:
            doDropStatements(fileWriter, scope, userScope, DBMSForwardOptions.DROP_DELETE,
                    sqlForward, controller, excludeList, isHtml);
            if (getController().checkPoint())
                doCreationStatements(fileWriter, scope, userScope,
                        DBMSForwardOptions.CREATE_ADD_FORWARD, sqlForward, controller, excludeList,
                        isHtml);
            break;
        default:
            break;
        }
        fileWriter.write(EOL + EOL + sqlForward.getCommentTag() + END_COMMENT);
        fileWriter.close();
        fileWriter = null;
        if (!controller.checkPoint()) {
            File outputFile = new File(filename);
            outputFile.delete();
            outputFile = null;
        }
        generatedFiles.add(filename);

        pack.getDb().commitTrans();
        super.terminateRunJob(sqlForward, generatedFiles);
    }

    private final void doCreationStatements(FileWriter fileWriter, ObjectScope[] scope,
            ObjectScope userScope, int statementType, SQLForwardEngineeringPlugin sqlForward,
            Controller controller, ArrayList excludeList, boolean isHtml) throws DbException,
            IOException, RuleException {
        writeStatementTypeComment(fileWriter, DBMSForwardOptions.CREATE_STATEMENTS, sqlForward,
                isHtml);
        int nb = scope.length;
        for (int i = 0; i < nb && getController().checkPoint(); i++) {
            ObjectScope currScope = scope[i];
            doStatement(fileWriter, currScope, userScope, scope, statementType, sqlForward,
                    controller, excludeList);
        }
    }

    private final void doDropStatements(FileWriter fileWriter, ObjectScope[] scope,
            ObjectScope userScope, int statementType, SQLForwardEngineeringPlugin sqlForward,
            Controller controller, ArrayList excludeList, boolean isHtml) throws DbException,
            IOException, RuleException {
        writeStatementTypeComment(fileWriter, DBMSForwardOptions.DROP_STATEMENTS, sqlForward,
                isHtml);
        int nb = scope.length;
        for (int i = nb - 1; i >= 0 && getController().checkPoint(); i--) {
            ObjectScope currScope = scope[i];
            doStatement(fileWriter, currScope, userScope, scope, statementType, sqlForward,
                    controller, excludeList);
        }
    }

    private final void doStatement(FileWriter fileWriter, ObjectScope objectScope,
            ObjectScope userScope, ObjectScope[] scope, int statementType,
            SQLForwardEngineeringPlugin sqlForward, Controller controller, ArrayList excludeList)
            throws DbException, IOException, RuleException {

        String conceptName = objectScope.conceptName;
        Debug.trace(conceptName);

        if (objectScope == userScope || !objectScope.isSelected)
            return;
        Rule rule = null;

        ObjectScope parent = null;
        boolean sendToTemplate = true;
        if (objectScope.parentID != null) {
            parent = ObjectScope.findConceptInObjectScopeWithConceptID(scope, objectScope.parentID);
        } // end if

        if (!sendToTemplate) {
            return;
        }

        ArrayList objectSelectionToForward = new ArrayList();
        ArrayList occurences = objectScope.occurences;

        // Owned Object (objects that can be owned by a user, like table, view,
        // column, index, pk, uk, ect.)
        if (objectScope.isOwnedObject) {
            int nbOccurrences = occurences.size();
            for (int j = 0; j < nbOccurrences && getController().checkPoint(); j++) {
                UserInfo userInfo = (UserInfo) occurences.get(j);
                if (ObjectScope.isOccurenceIsSelectedByObject(userScope, userInfo.user)) {
                    ArrayList userOccurences = userInfo.occurences;
                    for (int k = 0; k < userOccurences.size() && getController().checkPoint(); k++) {
                        ObjectSelection objectSelection = (ObjectSelection) userOccurences.get(k);
                        if (objectSelection.getIsSelected()) {
                            objectSelectionToForward.add(objectSelection);
                        } // end if
                    } // end for
                } // end if
            } // end for
        } else { // Unowned Object (objects that cannot be owned by a user, like
            // db, tbsp, user and rollback)
            int nbOccurrences = occurences.size();
            for (int j = 0; j < nbOccurrences && getController().checkPoint(); j++) {
                ObjectSelection objectSelection = (ObjectSelection) occurences.get(j);
                if (objectSelection.getIsSelected()) {
                    objectSelectionToForward.add(objectSelection);
                } // end if
            } // end for
        } // end if

        Collections.sort(objectSelectionToForward, new CollationComparator());
        int objectSelectionToForwardSize = objectSelectionToForward.size();
        for (int i = 0; i < objectSelectionToForwardSize; i++) {
            ObjectSelection objectSelection = (ObjectSelection) objectSelectionToForward.get(i);
            if (parent != null) {
                DbObject composite = objectSelection.occurence.getComposite();
                boolean isSelected = ObjectScope.isOccurenceIsSelectedByObject(parent, composite);
                if (isSelected) {
                    generateStatement(fileWriter, objectScope, scope, statementType, sqlForward,
                            objectSelection, controller, excludeList);
                }
            } else {
                generateStatement(fileWriter, objectScope, scope, statementType, sqlForward,
                        objectSelection, controller, excludeList);
            }
        } // end for
    } // end doStatement()

    private final void generateStatement(FileWriter fileWriter, ObjectScope objectScope,
            ObjectScope[] scope, int statementType, SQLForwardEngineeringPlugin sqlForward,
            ObjectSelection objectSelection, Controller controller, ArrayList excludeList)
            throws DbException, IOException, RuleException {

        if (objectScope.childrenID != null) {
            for (int x = 0; x < objectScope.childrenID.length; x++) {
                ObjectScope child = ObjectScope.findConceptInObjectScopeWithConceptID(scope,
                        objectScope.childrenID[x]);
                if (child != null) {
                    sqlForward.setTemplateCondition(child.mappingName, child.isSelected);
                } // end if
            } // end for
        } // end if

        Rule rule = null;
        if (objectSelection.occurence instanceof DbORIndex
                && ((DbORIndex) objectSelection.occurence).getConstraint() instanceof DbORPrimaryUnique) {
            if (!ForwardToolkitPlugin.getToolkit().createPKUKIndex()
                    && statementType == DBMSForwardOptions.CREATE_ADD_FORWARD)
                rule = null;
            else if (!ForwardToolkitPlugin.getToolkit().dropPKUKIndex()
                    && statementType == DBMSForwardOptions.DROP_DELETE)
                rule = null;
            else
                rule = sqlForward.getRuleOf(objectSelection.occurence, statementType);
        } else
            rule = sqlForward.getRuleOf(objectSelection.occurence, statementType);

        if (rule != null) {
            expandRule(rule, fileWriter, objectSelection.occurence, objectScope, sqlForward,
                    controller, excludeList);
        } // end if
    } // end generateStatement()

    private final void writeHeader(PrintWriter writer, SQLForwardEngineeringPlugin sqlForward,
            boolean isHtml) throws IOException, DbException {

        if (isHtml) {
            writer.write("<pre>"); // NOT LOCALIZABLE
        }

        String commentTag = sqlForward.getCommentTag();
        String line;

        line = MessageFormat.format("{0}{1}{2}{3}{4}", new Object[] { // NOT
                // LOCALIZABLE
                        commentTag, COMMENT_SEPARATOR, commentTag, TAB, getJobTitle() });
        writer.println(line);

        // REM Plugins :Oracle Reverse Engineering/Synchronization $Revision 29
        // $
        PluginDescriptor descriptor = PluginMgr.getSingleInstance().getPluginsRegistry()
                .getPluginInfo(sqlForward);

        line = MessageFormat.format(PLUGIN_PATTERN, new Object[] { commentTag,
                descriptor.toString() });
        writer.println(line);

        // Empty line
        writer.println(commentTag);

        line = MessageFormat.format("{0}{1}{2} \"{3}\"", new Object[] { // NOT
                // LOCALIZABLE
                        commentTag, TAB, DATABASE, m_options.databaseName });
        writer.println(line);

        DbSMSAbstractPackage pack = m_options.getAbstractPackage();
        pack.getProject().getRamFileName();
        String fileName = pack.getProject().getRamFileName() == null ? EMPTY_NAME : pack
                .getProject().getRamFileName();

        line = MessageFormat.format("{0}{1}{2} \"{3}\"", new Object[] { // NOT
                // LOCALIZABLE
                        commentTag, TAB, SMS_FILE, fileName });
        writer.println(line);

        // Empty line
        writer.println(commentTag);

        // REM Generated on (date)
        String date = java.text.DateFormat.getDateTimeInstance().format(
                new java.util.Date(System.currentTimeMillis()));
        line = MessageFormat.format("{0}{1}{2} {3}", new Object[] { // NOT
                // LOCALIZABLE
                        commentTag, TAB, GENERATE_ON, date });
        writer.println(line);

        line = MessageFormat.format(GENERATED_BY_PATTERN2, new Object[] { commentTag,
                ApplicationContext.getApplicationName(), ApplicationContext.getApplicationVersion() });
        writer.println(line);
        writer.write(commentTag + COMMENT_SEPARATOR);

        if (isHtml) {
            writer.write("</pre>"); // NOT LOCALIZABLE
        }
    } // end writeHeader()

    private final void writeStatementTypeComment(FileWriter fileWriter, int statementType,
            SQLForwardEngineeringPlugin sqlForward, boolean isHtml) throws IOException {

        if (isHtml) {
            fileWriter.write("<pre>"); // NOT LOCALIZABLE
        }

        fileWriter.write(EOL + EOL + sqlForward.getCommentTag() + COMMENT_SEPARATOR);
        switch (statementType) {
        case DBMSForwardOptions.CREATE_STATEMENTS:
            getController().println(CREATE_STATEMENT_COMMENT);
            fileWriter.write(sqlForward.getCommentTag() + CREATE_STATEMENT_COMMENT);
            break;
        case DBMSForwardOptions.DROP_STATEMENTS:
            getController().println(DROP_STATEMENT_COMMENT);
            fileWriter.write(sqlForward.getCommentTag() + DROP_STATEMENT_COMMENT);
            break;
        default:
            break;
        }
        fileWriter.write(sqlForward.getCommentTag() + COMMENT_SEPARATOR + EOL + EOL);

        if (isHtml) {
            fileWriter.write("</pre>"); // NOT LOCALIZABLE
        }
    } // end writeStatementTypeComment()

    private final void writeObjectComment(FileWriter fileWriter, DbObject obj,
            ObjectScope objectScope, SQLForwardEngineeringPlugin sqlForward) throws DbException,
            IOException {

        String nameLine = TAB + objectScope.conceptName + " \""; // NOT
        // LOCALIZABLE
        String objName = StringUtil.isEmptyString(((DbSemanticalObject) obj).getPhysicalName()) ? EMPTY_NAME
                : ((DbSemanticalObject) obj).getPhysicalName();
        objName = objName + "\"" + EOL; // NOT LOCALIZABLE
        nameLine = nameLine + objName;
        getController().print(TAB + nameLine);
    }

    private final void expandRule(Rule rule, FileWriter fileWriter, DbObject obj,
            ObjectScope objectScope, SQLForwardEngineeringPlugin sqlForward, Controller controller,
            ArrayList excludeList) throws DbException, IOException, RuleException {
        StringWriter stringWriter = new StringWriter();

        DbObject refObject = null;
        MetaField[] metafields = null;
        Rule.RuleOptions options = new Rule.RuleOptions(refObject, metafields, controller,
                excludeList);
        rule.expand(stringWriter, obj, options);
        writeObjectComment(fileWriter, obj, objectScope, sqlForward);
        fileWriter.write(EditionCode.processEditionCode(stringWriter.toString()));
        stringWriter.close();
    }
}
