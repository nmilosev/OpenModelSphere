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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginAction;
import org.modelsphere.jack.plugins.PluginServices;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.srtool.forward.ForwardOptions;
import org.modelsphere.jack.srtool.forward.ForwardToolkitInterface;
import org.modelsphere.jack.srtool.forward.JackForwardEngineeringPlugin;
import org.modelsphere.jack.srtool.forward.PropertyScreenPreviewInfo;
import org.modelsphere.jack.srtool.forward.Rule;
import org.modelsphere.jack.srtool.forward.Template;
import org.modelsphere.jack.srtool.forward.VariableScope;
import org.modelsphere.sms.or.db.DbORCheck;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORProcedure;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTrigger;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.db.DbORView;
import org.modelsphere.sms.or.generic.db.DbGECheck;
import org.modelsphere.sms.or.generic.db.DbGEColumn;
import org.modelsphere.sms.or.generic.db.DbGEDataModel;
import org.modelsphere.sms.or.generic.db.DbGEForeign;
import org.modelsphere.sms.or.generic.db.DbGEIndex;
import org.modelsphere.sms.or.generic.db.DbGEOperationLibrary;
import org.modelsphere.sms.or.generic.db.DbGEPrimaryUnique;
import org.modelsphere.sms.or.generic.db.DbGEProcedure;
import org.modelsphere.sms.or.generic.db.DbGETable;
import org.modelsphere.sms.or.generic.db.DbGETrigger;
import org.modelsphere.sms.or.generic.db.DbGEView;
import org.modelsphere.sms.templates.SQLForwardEngineeringPlugin;

/**
 * This plug-in will is generating DDL from the generic application relational concepts. -
 * identifies generic concepts for activation - declares entry points string in the template file -
 * provide SQL preview - provide DDL file generation
 */
public class GenericDDLForwardEngineeringPlugin extends SQLForwardEngineeringPlugin implements Plugin2{
    private static final String PREVIEW_NAME = LocaleMgr.misc.getString("sql"); //NOT LOCALIZABLE
    private static URL ANSI_TEMPLATE_URL;
    private static final String ANSI_TEMPLATE_FILENAME = "generateddl.tpl"; //NOT LOCALIZABLE
    private static final String TITLE = "ANSI"; //NOT LOCALIZABLE

    protected URL getTemplateURL() {
        //        URL url = GenericDDLForwardEngineeringPlugin.class.getResource(ANSI_TEMPLATE_FILENAME);
        //        String retVal = null;
        //        try {
        //            retVal = URLDecoder.decode(url.getFile(), "UTF-8");
        //        } catch (Exception e) {
        //        }
        return ANSI_TEMPLATE_URL;
    }

    //Required by the unit test. Do not remove!
    static {
    	int i=0;
    	Class<?> claz =  GenericDDLForwardEngineeringPlugin.class;
    	ANSI_TEMPLATE_URL = JackForwardEngineeringPlugin.tryToFind(claz, ANSI_TEMPLATE_FILENAME);
    }

    //Variable scope
    private VariableScope m_varScope = new VariableScope(TITLE);

    public VariableScope getVarScope() {
        return m_varScope;
    }

    public void setVarScope(VariableScope varScope) {
        m_varScope = varScope;
    }

    public GenericDDLForwardEngineeringPlugin() throws Exception {
        VariableScope scope = getVarScope();
        URL templateUrl = getTemplateURL();
        lazyInit(scope, templateUrl, SQLForwardEngineeringPlugin.PARSE_IF_NECESSARY);
    }

    protected String getTemplateFileName() {
    	String templateFilename = ANSI_TEMPLATE_URL.getFile();
        return templateFilename;
    }

    public PluginSignature getSignature() {
        return null;
    }

    //Supported classes : generic & Db2
    private Class[] supportedClasses = new Class[] { DbGEDataModel.class, //DbIBMDataModel.class,
            DbGETable.class, //DbIBMTable.class,
            DbGEView.class, //DbIBMView.class,
            DbGEColumn.class, //DbIBMColumn.class,
            DbGEPrimaryUnique.class, //DbIBMPrimaryUnique.class,
            DbGEForeign.class, //DbIBMForeign.class,
            DbGECheck.class, //DbIBMCheck.class,
            DbGEIndex.class, //DbIBMIndex.class,
            DbGETrigger.class, //DbIBMTrigger.class,
            DbGEOperationLibrary.class, //DbIBMOperationLibrary.class,
            DbGEProcedure.class, //DbIBMProcedure.class,
            DbORUser.class, };

    private Class[] supportedClassesForPreview = new Class[] { DbGEDataModel.class, //DbIBMDataModel.class,
            DbGETable.class, //DbIBMTable.class,
            DbGEView.class, //DbIBMView.class,
            DbGEColumn.class, //DbIBMColumn.class,
            DbGEPrimaryUnique.class, //DbIBMPrimaryUnique.class,
            DbGEForeign.class, //DbIBMForeign.class,
            DbGECheck.class, //DbIBMCheck.class,
            DbGEIndex.class, //DbIBMIndex.class,
            DbGETrigger.class, //DbIBMTrigger.class,
            DbGEOperationLibrary.class, //DbIBMOperationLibrary.class,
            DbGEProcedure.class, //DbIBMProcedure.class,
    //DbORUser.class,
    };

    // Activated for the following classes
    public Class[] getSupportedClasses() {
        return supportedClasses;
    }

    // The SQL Preview
    private PropertyScreenPreviewInfo m_propertyScreenPreviewInfo = null;

    public PropertyScreenPreviewInfo getPropertyScreenPreviewInfo() {
        if (m_propertyScreenPreviewInfo == null) {
            m_propertyScreenPreviewInfo = new PropertyScreenPreviewInfo() {
                public String getTabName() {
                    return PREVIEW_NAME;
                }

                public String getContentType() {
                    return "text/html";
                } //NOT LOCALIZABLE, standard content type

                public Class[] getSupportedClasses() {
                    return supportedClassesForPreview;
                }
            };
        }

        return m_propertyScreenPreviewInfo;
    }

    protected ForwardOptions createForwardOptions(DbObject[] semObjs) {
        GenericDDLBasicForwardToolkit.GenericDDLForwardOptions options;

        try {
            PluginServices.multiDbBeginTrans(Db.READ_TRANS, null);
            options = new GenericDDLBasicForwardToolkit.GenericDDLForwardOptions(m_toolkit);
            options.setObjects(semObjs);
            options.setObjectsScope(null);
            options.setNameList(null);
            PluginServices.multiDbCommitTrans();

        } catch (DbException ex) {
            options = null;
        }

        return options;
    }

    private GenericDDLBasicForwardToolkit m_toolkit = new GenericDDLBasicForwardToolkit();

    protected ForwardToolkitInterface getToolkit() {
        return m_toolkit;
    }

    // Concepts IDs
    public static final Integer GenericDatabaseId = new Integer(1);
    public static final Integer GenericDataModelId = new Integer(2);
    public static final Integer GenericTableId = new Integer(3);
    public static final Integer GenericViewId = new Integer(4);
    public static final Integer GenericPrimaryId = new Integer(5);
    public static final Integer GenericUniqueId = new Integer(6);
    public static final Integer GenericForeignId = new Integer(7);
    public static final Integer GenericCheckId = new Integer(8);
    public static final Integer GenericIndexId = new Integer(9);
    public static final Integer GenericTriggerId = new Integer(10);
    public static final Integer GenericOperationLibId = new Integer(11);
    public static final Integer GenericProcedureId = new Integer(12);
    public static final Integer GenericDomainModelId = new Integer(13);
    public static final Integer GenericDomainId = new Integer(14);
    public static final Integer GenericUserId = new Integer(15);
    public static final Integer GenericColumnId = new Integer(16);

    private static final HashMap ENTRY_POINT_MAP;

    private static final String DATABASE_CREATE_ENTRY = "databaseEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String DATAMODEL_CREATE_ENTRY_POINT = "dataModelEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String TABLE_CREATE_ENTRY_POINT = "tableEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String VIEW_CREATE_ENTRY_POINT = "viewEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String COLUMN_CREATE_ENTRY_POINT = "columnEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String PK_CREATE_ENTRY_POINT = "pkEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String UK_CREATE_ENTRY_POINT = "ukEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String FK_CREATE_ENTRY_POINT = "fkEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String CK_CREATE_ENTRY_POINT = "ckEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String INDEX_CREATE_ENTRY_POINT = "indexEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String TRIG_CREATE_ENTRY_POINT = "triggerEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String OPLIB_CREATE_ENTRY_POINT = "operationLibraryEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String PROC_CREATE_ENTRY_POINT = "procedureEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String DOMAINMODEL_CREATE_ENTRY_POINT = "domainModelEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String DOMAIN_CREATE_ENTRY_POINT = "domainEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String USER_CREATE_ENTRY_POINT = "userEntryPoint"; //NOT LOCALIZABLE, template entry point
    private static final String TABLE_DROP_ENTRY_POINT = "dropTableEntryPoint"; //NOT LOCALIZABLE, template entry point

    static {
        ENTRY_POINT_MAP = new HashMap();
        //                  MetaClass               CREATE_ADD_FORWARD                         CREATE_ADD_SYNCHRO  DROP/DELETE   ALTER/MODIFY  RENAME
        ENTRY_POINT_MAP.put(GenericDatabaseId, new String[] { DATABASE_CREATE_ENTRY, null, null,
                null, null });
        ENTRY_POINT_MAP.put(GenericDataModelId, new String[] { DATAMODEL_CREATE_ENTRY_POINT, null,
                null, null, null });
        ENTRY_POINT_MAP.put(GenericTableId, new String[] { TABLE_CREATE_ENTRY_POINT, null,
                TABLE_DROP_ENTRY_POINT, null, null });
        ENTRY_POINT_MAP.put(GenericViewId, new String[] { VIEW_CREATE_ENTRY_POINT, null, null,
                null, null });
        ENTRY_POINT_MAP.put(GenericColumnId, new String[] { COLUMN_CREATE_ENTRY_POINT, null, null,
                null, null });
        ENTRY_POINT_MAP.put(GenericPrimaryId, new String[] { PK_CREATE_ENTRY_POINT, null, null,
                null, null });
        ENTRY_POINT_MAP.put(GenericUniqueId, new String[] { UK_CREATE_ENTRY_POINT, null, null,
                null, null });
        ENTRY_POINT_MAP.put(GenericForeignId, new String[] { FK_CREATE_ENTRY_POINT, null, null,
                null, null });
        ENTRY_POINT_MAP.put(GenericCheckId, new String[] { CK_CREATE_ENTRY_POINT, null, null, null,
                null });
        ENTRY_POINT_MAP.put(GenericIndexId, new String[] { INDEX_CREATE_ENTRY_POINT, null, null,
                null, null });
        ENTRY_POINT_MAP.put(GenericTriggerId, new String[] { TRIG_CREATE_ENTRY_POINT, null, null,
                null, null });
        ENTRY_POINT_MAP.put(GenericOperationLibId, new String[] { OPLIB_CREATE_ENTRY_POINT, null,
                null, null, null });
        ENTRY_POINT_MAP.put(GenericProcedureId, new String[] { PROC_CREATE_ENTRY_POINT, null, null,
                null, null });
        ENTRY_POINT_MAP.put(GenericDomainModelId, new String[] { DOMAINMODEL_CREATE_ENTRY_POINT,
                null, null, null, null });
        ENTRY_POINT_MAP.put(GenericDomainId, new String[] { DOMAIN_CREATE_ENTRY_POINT, null, null,
                null, null });
        ENTRY_POINT_MAP.put(GenericUserId, new String[] { USER_CREATE_ENTRY_POINT, null, null,
                null, null });
    }

    public Rule getRuleOf(DbObject obj, int action) throws DbException {
        String[] entryPoints = null;
        Rule rule = null;

        if (obj instanceof DbORDatabase) {
            entryPoints = (String[]) ENTRY_POINT_MAP.get(GenericDatabaseId);
        } else if (obj instanceof DbORDataModel) {
            entryPoints = (String[]) ENTRY_POINT_MAP.get(GenericDataModelId);
        } else if (obj instanceof DbORTable) {
            entryPoints = (String[]) ENTRY_POINT_MAP.get(GenericTableId);
        } else if (obj instanceof DbORView) {
            entryPoints = (String[]) ENTRY_POINT_MAP.get(GenericViewId);
        } else if (obj instanceof DbORPrimaryUnique) {
            obj.getDb().beginTrans(Db.READ_TRANS);
            DbORPrimaryUnique puk = (DbORPrimaryUnique) obj;

            if (puk.isPrimary())
                entryPoints = (String[]) ENTRY_POINT_MAP.get(GenericPrimaryId);
            else
                entryPoints = (String[]) ENTRY_POINT_MAP.get(GenericUniqueId);
            obj.getDb().commitTrans();
        } else if (obj instanceof DbORForeign) {
            entryPoints = (String[]) ENTRY_POINT_MAP.get(GenericForeignId);
        } else if (obj instanceof DbORCheck) {
            entryPoints = (String[]) ENTRY_POINT_MAP.get(GenericCheckId);
        } else if (obj instanceof DbORIndex) {
            entryPoints = (String[]) ENTRY_POINT_MAP.get(GenericIndexId);
        } else if (obj instanceof DbORTrigger) {
            entryPoints = (String[]) ENTRY_POINT_MAP.get(GenericTriggerId);
        } else if (obj instanceof DbOROperationLibrary) {
            entryPoints = (String[]) ENTRY_POINT_MAP.get(GenericOperationLibId);
        } else if (obj instanceof DbORProcedure) {
            entryPoints = (String[]) ENTRY_POINT_MAP.get(GenericProcedureId);
        } else if (obj instanceof DbORDomainModel) {
            entryPoints = (String[]) ENTRY_POINT_MAP.get(GenericDomainModelId);
        } else if (obj instanceof DbORDomain) {
            entryPoints = (String[]) ENTRY_POINT_MAP.get(GenericDomainId);
        } else if (obj instanceof DbORUser) {
            entryPoints = (String[]) ENTRY_POINT_MAP.get(GenericUserId);
        } else if (obj instanceof DbORColumn) {
            entryPoints = (String[]) ENTRY_POINT_MAP.get(GenericColumnId);
        }

        if (entryPoints != null && entryPoints[action] != null) {
            String entryPoint = entryPoints[action];
            rule = getRule(entryPoint, getTemplateURL());
        }

        return rule;
    }

    protected Template getFileNotFoundRule() {
    	String filename = ANSI_TEMPLATE_URL.getFile();
        Template tmpl = new Template(null, "File not found: " + filename); //NOT LOCALIZABLE
        return tmpl;
    }
    
	@Override
	public OptionGroup getOptionGroup() {
		return null;
	}

	@Override
	public PluginAction getPluginAction() {
		return null;
	}

}
