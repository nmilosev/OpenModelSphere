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

package org.modelsphere.sms.templates;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.io.HTMLStringWriter;
import org.modelsphere.jack.srtool.forward.EditionCode;
import org.modelsphere.jack.srtool.forward.GenerateInFileInfo;
import org.modelsphere.jack.srtool.forward.PropertyScreenPreviewInfo;
import org.modelsphere.jack.srtool.forward.Rule;
import org.modelsphere.jack.srtool.forward.RuleException;
import org.modelsphere.jack.srtool.forward.Template;
import org.modelsphere.jack.srtool.forward.VariableDecl;
import org.modelsphere.jack.srtool.forward.VariableScope;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.features.dbms.DBMSForwardOptions;
import org.modelsphere.sms.or.generic.db.DbGEColumn;
import org.modelsphere.sms.or.generic.db.DbGEDataModel;
import org.modelsphere.sms.or.generic.db.DbGEIndex;
import org.modelsphere.sms.or.generic.db.DbGEPrimaryUnique;
import org.modelsphere.sms.or.generic.db.DbGEProcedure;
import org.modelsphere.sms.or.generic.db.DbGETable;
import org.modelsphere.sms.or.generic.db.DbGETrigger;
import org.modelsphere.sms.or.generic.db.DbGEView;
import org.modelsphere.sms.preference.DirectoryOptionGroup;

/**
 * This class provides common services for SQL generation plug-ins.
 * 
 */
public abstract class SQLForwardEngineeringPlugin extends GenericForwardEngineeringPlugin {

    public int getFeatureSet() {
        return SMSFilter.RELATIONAL;
    }

    public static Class getApplicationClass() {
        Class claz = null;
        try {
            claz = Class.forName("org.modelsphere.sms.Application"); //NOT LOCALIZABLE
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
        return claz;
    }

    private static final String FORWARD_DISPLAY_NAME = LocaleMgr.misc.getString("SQL");

    //Required by the unit test. Do not remove!
    static {
        try {
            Class<?> claz = getApplicationClass();
            java.lang.reflect.Method method = claz.getDeclaredMethod("initMeta", new Class[] {}); //NOT LOCALIZABLE, unit test
            method.invoke(null, new Object[] {});
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    ////////////////////
    // OVERRIDES Forward
    private PropertyScreenPreviewInfo m_propertyScreenPreviewInfo = null;

    public PropertyScreenPreviewInfo getPropertyScreenPreviewInfo() {
        if (m_propertyScreenPreviewInfo == null) {
            m_propertyScreenPreviewInfo = new PropertyScreenPreviewInfo() {
                public String getTabName() {
                    return FORWARD_DISPLAY_NAME;
                }

                public String getContentType() {
                    return "text/html";
                } //NOT LOCALIZABLE, standard content type

                public Class[] getSupportedClasses() {
                    return new Class[] {};
                } ////nothing is supported yet
            };
        }

        return m_propertyScreenPreviewInfo;
    }

    private static final String SQL_EXTENSION = "sql"; //NOT LOCALIZABLE, file extension
    private static final String HTML_EXTENSION = "html"; //NOT LOCALIZABLE, file extension

    private static final String ITEM_TITLE = LocaleMgr.misc.getString("DDLFile");
    private static final Integer ITEM_MNEMONIC = new Integer(LocaleMgr.misc.getMnemonic("DDLFile"));
    private GenerateInFileInfo m_generateInFileInfo = null;

    public final GenerateInFileInfo getGenerateInFileInfo() {
        if (m_generateInFileInfo == null) {
            m_generateInFileInfo = new GenerateInFileInfo() {
                public String getPopupItemTitle() {
                    return ITEM_TITLE;
                }

                public Integer getPopupItemMnemonic() {
                    return ITEM_MNEMONIC;
                }

                public String getDefaultExtension() {
                    return isOutputInHTMLFormat() ? HTML_EXTENSION : SQL_EXTENSION;
                }
            };
        }

        return m_generateInFileInfo;
    }

    public String getRootDirFromUserProp() {
        return getForwardDirectory();
    }

    public String getForwardDirectory() {
        String defDir = DirectoryOptionGroup.getDDLGenerationDirectory();
        return defDir;
    }

    //
    ///////////////

    public final void setOutputToHTMLFormat() {
        //look for the html variable
        setTemplateCondition("html", true); //NOT LOCALIZABLE, 'html' being a template variable
    }

    public final void setOutputToASCIIFormat() {
        //look for the html variable
        setTemplateCondition("html", false); //NOT LOCALIZABLE, template variable
    }

    public final boolean isOutputInHTMLFormat() {
        boolean value = false;
        VariableScope varScope = getVarScope();
        if (varScope.isDefined("html")) {
            VariableDecl.VariableStructure varStruct = varScope.getVariable("html");
            Boolean b = (Boolean) varStruct.getValue();
            value = b.booleanValue();
        }

        return value;
    }

    public final Writer createNewPanelWriter(boolean isHTMLformat) {
        //override write() methods to replace '<' by &lt;,
        //'>' by &gt; and '&' by &amp;

        if (isHTMLformat) {
            setOutputToHTMLFormat();
            return new HTMLStringWriter();
        } else {
            return new StringWriter();
        }
    }

    // Concepts ID - Generic
    public static final Integer GETableId = new Integer(1);
    public static final Integer GEViewId = new Integer(2);
    public static final Integer GEColumnId = new Integer(3);
    public static final Integer GEIndexId = new Integer(4);
    public static final Integer GETriggerId = new Integer(5);
    public static final Integer ORUserId = new Integer(6);
    public static final Integer ORDomainId = new Integer(7);
    public static final Integer PrimaryId = new Integer(60);
    public static final Integer UniqueId = new Integer(70);
    public static final Integer GEForeignId = new Integer(80);
    public static final Integer GEProcedureId = new Integer(8);
    public static final Integer GEDataModelId = new Integer(11);

    // DB2 UDB
    public static final Integer IBMTableId = new Integer(1);
    public static final Integer IBMViewId = new Integer(2);
    public static final Integer IBMColumnId = new Integer(3);
    public static final Integer IBMIndexId = new Integer(4);
    public static final Integer IBMTriggerId = new Integer(5);
    public static final Integer IBMUserId = new Integer(6);
    public static final Integer IBMDomainId = new Integer(7);
    public static final Integer IBMPrimaryId = new Integer(60);
    public static final Integer IBMUniqueId = new Integer(70);
    public static final Integer IBMProcedureId = new Integer(8);
    public static final Integer IBMDataModelId = new Integer(11);
    public static final Integer IBMSequenceId = new Integer(12);
    public static final Integer IBMForeignId = new Integer(13);
    public static final Integer IBMCheckId = new Integer(14);
    public static final Integer IBMDbPartGroupId = new Integer(20);
    public static final Integer IBMBufferPoolId = new Integer(21);
    public static final Integer IBMTablespaceId = new Integer(22);
    public static final Integer IBMDatabaseId = new Integer(23);
    public static final Integer IBMOperationLibId = new Integer(24);

    // Oracle
    public static final Integer ORATableId = new Integer(1);
    public static final Integer ORAViewId = new Integer(2);
    public static final Integer ORAColumnId = new Integer(3);
    public static final Integer ORAIndexId = new Integer(4);
    public static final Integer ORATriggerId = new Integer(5);
    public static final Integer ORAPrimaryId = PrimaryId;
    public static final Integer ORAUniqueId = UniqueId;
    public static final Integer ORAForeignId = new Integer(8);
    public static final Integer ORAProcedureId = new Integer(9);
    public static final Integer ORACheckId = new Integer(12);
    public static final Integer ORADatabaseId = new Integer(13);
    public static final Integer ORAPartitionId = new Integer(14);
    public static final Integer ORASubPartitionId = new Integer(15);
    public static final Integer ORASequenceId = new Integer(16);
    public static final Integer ORATablespaceId = new Integer(17);
    public static final Integer ORADataFileId = new Integer(18);
    public static final Integer ORARollbackSegmentId = new Integer(19);
    public static final Integer ORAPackageId = new Integer(20);
    public static final Integer ORADataModelId = new Integer(21);
    public static final Integer ORARedoLogFileId = new Integer(22);
    public static final Integer ORARedoLogGroupId = new Integer(23);
    public static final Integer ORALobStorageId = new Integer(24);
    public static final Integer ORANestedTableId = new Integer(25);

    // Informix
    public static final Integer INFTableId = new Integer(1);
    public static final Integer INFViewId = new Integer(2);
    public static final Integer INFColumnId = new Integer(3);
    public static final Integer INFIndexId = new Integer(4);
    public static final Integer INFTriggerId = new Integer(5);
    public static final Integer INFPrimaryId = PrimaryId;
    public static final Integer INFUniqueId = UniqueId;
    public static final Integer INFForeignId = new Integer(8);
    public static final Integer INFProcedureId = new Integer(9);
    public static final Integer INFCheckId = new Integer(12);
    public static final Integer INFDatabaseId = new Integer(13);
    public static final Integer INFDataModelId = new Integer(14);

    private static final HashMap ENTRY_POINT_MAP;
    //Data
    private static final String PUKEY_CREATE_ENTRY_POINT = "pukeyEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String FKEY_CREATE_ENTRY_POINT = "fkEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String TRIG_CREATE_ENTRY_POINT = "triggerEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String INDEX_CREATE_ENTRY_POINT = "indexEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String COL_CREATE_ENTRY_POINT = "columnEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String VIEW_CREATE_ENTRY_POINT = "viewEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String TABLE_CREATE_ENTRY_POINT = "tableEntryPoint"; //NOT LOCALIZABLE, template entry point

    //Non-data
    private static final String PROC_CREATE_ENTRY_POINT = "procedureEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String USER_CREATE_ENTRY_POINT = "userEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String DOMAIN_CREATE_ENTRY_POINT = "domainEntryPoint"; //NOT LOCALIZABLE, template entry point
    //General
    private static final String MODEL_CREATE_ENTRY_POINT = "modelEntryPoint"; //NOT LOCALIZABLE, template entry point

    static {
        ENTRY_POINT_MAP = new HashMap();
        //                  MetaClass                        CREATE_ADD_FORWARD         CREATE_ADD_SYNCHRO  DROP/DELETE   ALTER/MODIFY  RENAME
        ENTRY_POINT_MAP.put(GETableId, new String[] { TABLE_CREATE_ENTRY_POINT, null, null, null,
                null });
        ENTRY_POINT_MAP.put(GEViewId, new String[] { VIEW_CREATE_ENTRY_POINT, null, null, null,
                null });
        ENTRY_POINT_MAP.put(GEColumnId, new String[] { COL_CREATE_ENTRY_POINT, null, null, null,
                null });
        ENTRY_POINT_MAP.put(GEIndexId, new String[] { INDEX_CREATE_ENTRY_POINT, null, null, null,
                null });
        ENTRY_POINT_MAP.put(GETriggerId, new String[] { TRIG_CREATE_ENTRY_POINT, null, null, null,
                null });
        ENTRY_POINT_MAP.put(PrimaryId, new String[] { PUKEY_CREATE_ENTRY_POINT, null, null, null,
                null });
        ENTRY_POINT_MAP.put(UniqueId, new String[] { PUKEY_CREATE_ENTRY_POINT, null, null, null,
                null });
        ENTRY_POINT_MAP.put(GEForeignId, new String[] { FKEY_CREATE_ENTRY_POINT, null, null, null,
                null });
        ENTRY_POINT_MAP.put(GEProcedureId, new String[] { PROC_CREATE_ENTRY_POINT, null, null,
                null, null });
        ENTRY_POINT_MAP.put(ORUserId, new String[] { USER_CREATE_ENTRY_POINT, null, null, null,
                null });
        ENTRY_POINT_MAP.put(ORDomainId, new String[] { DOMAIN_CREATE_ENTRY_POINT, null, null, null,
                null });
        ENTRY_POINT_MAP.put(GEDataModelId, new String[] { MODEL_CREATE_ENTRY_POINT, null, null,
                null, null });
    }

    public Rule getRuleOf(DbObject obj, int action) throws DbException {
        String[] entryPoints = null;
        Rule rule = null;

        if (obj instanceof DbGETable) {
            entryPoints = (String[]) ENTRY_POINT_MAP.get(GETableId);
        } else if (obj instanceof DbGEView) {
            entryPoints = (String[]) ENTRY_POINT_MAP.get(GEViewId);
        } else if (obj instanceof DbGEColumn) {
            entryPoints = (String[]) ENTRY_POINT_MAP.get(GEColumnId);
        } else if (obj instanceof DbGEIndex) {
            entryPoints = (String[]) ENTRY_POINT_MAP.get(GEIndexId);
        } else if (obj instanceof DbGETrigger) {
            entryPoints = (String[]) ENTRY_POINT_MAP.get(GETriggerId);
        } else if (obj instanceof DbGEPrimaryUnique) {
            obj.getDb().beginTrans(Db.READ_TRANS);
            if (((DbGEPrimaryUnique) obj).isPrimary())
                entryPoints = (String[]) ENTRY_POINT_MAP.get(PrimaryId);
            else
                entryPoints = (String[]) ENTRY_POINT_MAP.get(UniqueId);
            obj.getDb().commitTrans();
        } else if (obj instanceof DbGEProcedure) {
            entryPoints = (String[]) ENTRY_POINT_MAP.get(GEProcedureId);
        } else if (obj instanceof DbORUser) {
            entryPoints = (String[]) ENTRY_POINT_MAP.get(ORUserId);
        } else if (obj instanceof DbORDomain) {
            entryPoints = (String[]) ENTRY_POINT_MAP.get(ORDomainId);
        } else if (obj instanceof DbGEDataModel) {
            entryPoints = (String[]) ENTRY_POINT_MAP.get(GEDataModelId);
        }
        if (entryPoints != null && entryPoints[action] != null)
            rule = getRule(entryPoints[action], getTemplateURL());
        return rule;
    }

    public final Rule getRuleOf(DbObject obj) throws DbException {
        return getRuleOf(obj, DBMSForwardOptions.CREATE_ADD_FORWARD);
    }

    public final Rule getCreateAddForwardRuleOf(DbObject obj) throws DbException {
        return getRuleOf(obj, DBMSForwardOptions.CREATE_ADD_FORWARD);
    }

    public final Rule getCreateAddSynchroRuleOf(DbObject obj) throws DbException {
        return getRuleOf(obj, DBMSForwardOptions.CREATE_ADD_SYNCHRO);
    }

    public final Rule getDropRuleOf(DbObject obj) throws DbException {
        return getRuleOf(obj, DBMSForwardOptions.DROP_DELETE);
    }

    public final Rule getAlterRuleOf(DbObject obj) throws DbException {
        return getRuleOf(obj, DBMSForwardOptions.ALTER_MODIFY);
    }

    public final Rule getRenameRuleOf(DbObject obj) throws DbException {
        return getRuleOf(obj, DBMSForwardOptions.RENAME);
    }

    public String getCommentTag() {
        return "";
    }

    private static final String FILE_NOT_FOUND = LocaleMgr.misc.getString("FileNotFound");

    protected Template getFileNotFoundRule() {
    	URL templateUrl = getTemplateURL();
    	String template = templateUrl.getFile();
        Template tmpl = new Template(null, FILE_NOT_FOUND + ": " + template == null ? null : template);
        return tmpl;
    }

    public void setTemplateCondition(String condition, boolean value) {
        if (getVarScope().isDefined(condition)) {
            Boolean val = new Boolean(value);
            getVarScope().setVariable(condition, val);
        }
    } //end setTemplateCondition()

    protected final void forwardTo(DbObject semObj, ArrayList generatedFiles) throws DbException,
            IOException, RuleException {
        if (semObj instanceof DbORTable) {
            forwardSemObj((DbSemanticalObject) semObj, generatedFiles);
        } else if (semObj instanceof DbGEDataModel) {
            forwardSemObj((DbSemanticalObject) semObj, generatedFiles);
        }
    }

    private void forwardSemObj(DbSemanticalObject semObj, ArrayList generatedFiles)
            throws DbException, IOException, RuleException {
        //build file name
        String filename = semObj.getPhysicalName();
        if (filename == null) {
            filename = semObj.getName();
        }
        filename += "." + getGenerateInFileInfo().getDefaultExtension();
        String pathname = getForwardDirectory() + System.getProperty("file.separator") + filename;

        //forward engineer
        StringWriter writer = new StringWriter();
        Rule rule = getRuleOf(semObj);
        setOutputToASCIIFormat();
        Rule.RuleOptions options = null;
        rule.expand(writer, semObj, options);

        String unprocessed = writer.toString();
        String processed = EditionCode.processEditionCode(unprocessed);

        //create file
        backupFile(pathname);
        FileWriter filewriter = new FileWriter(pathname);
        filewriter.write(processed);
        filewriter.close();
        generatedFiles.add(pathname);
    }
}
