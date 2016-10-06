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

package org.modelsphere.plugins.ansi.forward.toolkit;

//Java
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.gui.wizard.WizardPage;
import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginAction;
import org.modelsphere.jack.plugins.PluginMgr;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.srtool.forward.ForwardOptions;
import org.modelsphere.jack.srtool.plugins.generic.dbms.ObjectScope;
import org.modelsphere.sms.db.DbSMSAbstractPackage;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.db.DbORPackage;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORProcedure;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTrigger;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.db.DbORView;
import org.modelsphere.sms.or.features.dbms.DBMSDefaultForwardWizardModel;
import org.modelsphere.sms.or.features.dbms.DBMSForwardOptions;
import org.modelsphere.sms.or.features.dbms.DefaultForwardWizardObjectsPage;
import org.modelsphere.sms.or.features.dbms.DefaultForwardWizardStartPage;
import org.modelsphere.sms.or.features.dbms.ForwardToolkitPlugin;
import org.modelsphere.sms.plugins.TargetSystem;
import org.modelsphere.sms.plugins.TargetSystemInfo;
import org.modelsphere.sms.plugins.TargetSystemManager;
import org.modelsphere.sms.templates.SQLForwardEngineeringPlugin;

public final class AnsiForwardToolkitPlugin extends ForwardToolkitPlugin implements Plugin2{
    public AnsiForwardToolkitPlugin() {
    }

    // Toolkit identification
    private static int[] g_genericTargetSystems = null;

    protected int[] getSupportedTargetIds() {
        if (g_genericTargetSystems == null) {
            TargetSystem ts = TargetSystemManager.getSingleton();
            Collection c = ts.getAllTargetSystemInfos();
            int nbGenerics = c.size();
            g_genericTargetSystems = new int[nbGenerics];
            int idx = 0;
            Iterator iter = c.iterator();
            while (iter.hasNext()) {
                TargetSystemInfo info = (TargetSystemInfo) iter.next();
                int id = info.getID();
                g_genericTargetSystems[idx++] = info.getID();
            } //end while
        } //end if

        return g_genericTargetSystems;
    } //end getSupportedTargetIds()

    public Class getForwardClass() {
    	String className = "org.modelsphere.plugins.ansi.forward.GenericDDLForwardEngineeringPlugin"; //NOT LOCALIZABLE, class name
    	PluginMgr mgr = PluginMgr.getSingleInstance();
    	Class claz = mgr.getPluginClass(className);
    	
     	
     	return claz;
        //return PluginMgr.getSingleInstance().getPluginClass(
        //        "org.modelsphere.plugins.ansi.forward.GenericDDLForwardEngineeringPlugin"); //NOT LOCALIZABLE, class name
    }

    // Override this method if the forward have more statement radio button
    public int[] getSupportedStatements() {
        return new int[] { DBMSForwardOptions.CREATE_STATEMENTS,
                DBMSForwardOptions.DROP_STATEMENTS, DBMSForwardOptions.DROP_CREATE_STATEMENTS };
    }

    public String getTitle() {
        return "ANSI Forward Engineering";
    } //LocaleMgr.misc.getString("OracleForwardEngineering");}

    ///////////////////////////
    // OVERRIDES ForwardToolkit
    public void doForward(ForwardOptions options) {
        DBMSForwardOptions dbmsOptions = (DBMSForwardOptions) options;
        super.doForward(dbmsOptions);
    }

    public WizardPage getPage(int pageid) {
        switch (pageid) {
        case DBMSDefaultForwardWizardModel.PAGE_FIRST:
            return new DefaultForwardWizardStartPage();
        case DBMSDefaultForwardWizardModel.PAGE_OBJECTS_SELECTION:
            return new DefaultForwardWizardObjectsPage();
        }
        return null;
    }

    public boolean dropPKUKIndex() {
        return false;
    }

    public boolean canRemovePhysicalSpecs() {
        return true;
    }

    /*
     * public SQLForward getSQLForwardPlugins(){ PluginMgr mgr = PluginMgr.getSingleInstance();
     * ArrayList pluginsInstances = mgr.getPluginInstances(SQLOracleForward.class); return
     * (pluginsInstances.size() > 0? (SQLGenericForward)pluginsInstances.get(0): null); }
     */

    public Object getDbEnumerationForConcept(MetaClass metaClass,
            DbSMSAbstractPackage abstractPackage) throws DbException {
        DbEnumeration dbEnum = null;
        if (metaClass == DbORProcedure.metaClass || metaClass == DbORPackage.metaClass) {
            DbORDatabase database = null;
            if (abstractPackage instanceof DbORDataModel) {
                database = ((DbORDataModel) abstractPackage).getDeploymentDatabase();
            } else if (abstractPackage instanceof DbORDatabase) {
                database = (DbORDatabase) abstractPackage;
            } else if (abstractPackage instanceof DbOROperationLibrary) {
                DbOROperationLibrary lib = (DbOROperationLibrary) abstractPackage;
                database = lib.getDeploymentDatabase();
            }
            if (database != null) {
                ArrayList arrayListEnum = new ArrayList();
                //DbORProgramUnitLibrary operationLibrary = (DbORAProgramUnitLibrary)database.getOperationLibrary();
                //if (operationLibrary != null)
                //  arrayListEnum.add(operationLibrary.getComponents().elements(metaClass));

                return arrayListEnum;

            }
        } else if ((abstractPackage instanceof DbORDatabase)
                && ((DbORDatabase) abstractPackage).getSchema() != null) {
            dbEnum = ((DbORDatabase) abstractPackage).getSchema().componentTree(metaClass,
                    new MetaClass[] { DbSMSPackage.metaClass });
        } else {
            dbEnum = abstractPackage.componentTree(metaClass,
                    new MetaClass[] { DbSMSPackage.metaClass });
        }

        return dbEnum;
    }

    // Add all concepts to be forwarded
    public void initializeObjectsScope(DBMSForwardOptions options) {
        if (options.getObjectsScope() != null)
            return;

        options.setObjectsScope(new ObjectScope[] {
                //              Concept Name                                              ConceptID                  ParentID          ShowConcept  ShowOccurences  MetaClass                       IsOwnedObject
                new ObjectScope(DbORUser.metaClass.getGUIName(),
                        SQLForwardEngineeringPlugin.ORUserId, null, false, false,
                        DbORUser.metaClass, false),
                //              Concept Name                                              ConceptID                  ParentID          ChildrenID                                                                                                                            ShowConcept  ShowOccurences  MetaClass             IsOwnedObject
                new ObjectScope(DbORTable.metaClass.getGUIName(false, false),
                        SQLForwardEngineeringPlugin.GETableId, null, new Integer[] {
                                SQLForwardEngineeringPlugin.ORAPrimaryId,
                                SQLForwardEngineeringPlugin.ORAUniqueId,
                                SQLForwardEngineeringPlugin.ORACheckId,
                                SQLForwardEngineeringPlugin.ORAIndexId }, true, true,
                        DbORTable.metaClass, true),
                new ObjectScope("Index", DbORIndex.metaClass.getGUIName(false, false),
                        SQLForwardEngineeringPlugin.GEIndexId,
                        SQLForwardEngineeringPlugin.GETableId, true, false, DbORIndex.metaClass,
                        true), //NOT LOCALIZABLE
                //              Condition     Concept Name                                ConceptID                  ParentID          ShowConcept  ShowOccurences  MetaClass                       IsOwnedObject
                //
                new ObjectScope("PrimaryKey", org.modelsphere.sms.or.international.LocaleMgr.misc.getString("primaryKey"),
                        SQLForwardEngineeringPlugin.PrimaryId,
                        SQLForwardEngineeringPlugin.GETableId, true, false,
                        DbORPrimaryUnique.metaClass, true), //NOT LOCALIZABLE
                new ObjectScope("UniqueKey", org.modelsphere.sms.or.international.LocaleMgr.misc.getString("uniqueKey"),
                        SQLForwardEngineeringPlugin.UniqueId,
                        SQLForwardEngineeringPlugin.GETableId, true, false,
                        DbORPrimaryUnique.metaClass, true), //NOT LOCALIZABLE
                //              Concept Name                                              ConceptID                    ParentID         ShowConcept  ShowOccurences  MetaClass                       IsOwnedObject
                new ObjectScope(DbORForeign.metaClass.getGUIName(false, false),
                        SQLForwardEngineeringPlugin.GEForeignId,
                        SQLForwardEngineeringPlugin.GETableId, true, false, DbORForeign.metaClass,
                        true),
                //              Condition     Concept Name                                ConceptID                    ParentID         ShowConcept  ShowOccurences  MetaClass                       IsOwnedObject
                //new ObjectScope("Check", DbORCheck.metaClass.getGUIName(false, false),    SQLForward.GECheckId,      SQLForward.GETableId,  true, false,    DbORCheck.metaClass,           true),    //NOT LOCALIZABLE
                new ObjectScope(DbORView.metaClass.getGUIName(false, false),
                        SQLForwardEngineeringPlugin.GEViewId, null, true, true, DbORView.metaClass,
                        true),
                new ObjectScope(DbORProcedure.metaClass.getGUIName(false, false),
                        SQLForwardEngineeringPlugin.GEProcedureId, null, true, true,
                        DbORProcedure.metaClass, true),
                //new ObjectScope(DbORPackage.metaClass.getGUIName(false, false),           SQLForward.GEPackageId,    null,            true,        true,           DbORPackage.metaClass,         true),
                //              Concept Name                                              ConceptID                    ParentID                      ShowConcept     ShowOccurences                  MetaClass                       IsOwnedObject
                new ObjectScope(DbORTrigger.metaClass.getGUIName(false, false),
                        SQLForwardEngineeringPlugin.GETriggerId,
                        SQLForwardEngineeringPlugin.ORATableId, true, false, DbORTrigger.metaClass,
                        true), });
    }

    public PluginSignature getSignature() {
        return null;
    }

    //
    // PRIVATE METHODS
    //
    public boolean isContainedInArray(int[] array, int elem) {
        boolean contained = false;
        int nb = array.length;
        for (int i = 0; i < nb; i++) {
            if (array[i] == elem) {
                contained = true;
                break;
            } //end if
        } //end for

        return contained;
    } //end isContainedInArray()
    
	@Override
	public OptionGroup getOptionGroup() {
		return null;
	}

	@Override
	public PluginAction getPluginAction() {
		return null;
	}

} //end of OracleForwardToolkit
