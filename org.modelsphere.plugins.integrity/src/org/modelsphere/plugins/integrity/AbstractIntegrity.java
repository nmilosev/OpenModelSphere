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

package org.modelsphere.plugins.integrity;

import java.util.ArrayList;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.plugins.PluginSelectionListener;
import org.modelsphere.sms.db.DbSMSAbstractPackage;
import org.modelsphere.sms.or.db.*;

/**
 * @author pierrem
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class AbstractIntegrity {
    //verification mode
    protected static final int INTEGRITY = 0;
    protected static final int CLEANUP = 1;
    //model type
    protected static final int OR_DATAMODEL = 0;
    protected static final int DOMAIN_MODEL = 1;
    protected static final int COMMONITEM_MODEL = 2;
    protected static final int OPERATION_LIBRARY = 3;
    protected static final int ER_DATAMODEL = 4;

    protected int modelErrorCount = 0;
    protected int modelWarningCount = 0;
    protected ModelIntegrityReport modelIntegrityReport;

    /*
     * Integrity
     */
    protected static final String kSolution1 = LocaleMgr.misc.getString("Solution1");
    protected static final String kSolution2 = LocaleMgr.misc.getString("Solution2");
    protected static final String kSolution3 = LocaleMgr.misc.getString("Solution3");

    /*
     * CleanUp
     */
    public static final String DELETE_ONE = "DELETE_ONE"; // NOT LOCALIZABLE 
    static final String DELETE_ALL = "DELETE_ALL"; // NOT LOCALIZABLE  
    protected static ArrayList[] occurencesToClean = null; //utiliser avec les constantes de ruleIndex

    /*
     * public methods
     */

    public ArrayList<DbObject> getOccurrences(DbObject model, MetaClass metaclass)
            throws DbException {
        ArrayList<DbObject> occurences = new ArrayList<DbObject>();

        if (model == null || metaclass == null)
            return occurences;

        DbEnumeration dbEnum = getDbObjectsToVerify(model, metaclass);
        while (dbEnum.hasMoreElements()) {
            DbObject dbo = dbEnum.nextElement();
            dbo.setValidationStatus(DbObject.VALIDATION_OK);
            occurences.add(dbo);
        }

        dbEnum.close();
        return occurences;
    }

    public static final DbEnumeration getDbObjectsToVerify(DbObject composite, MetaClass metaclass)
            throws DbException {
        return composite.componentTree(metaclass,
                new MetaClass[] { DbSMSAbstractPackage.metaClass });
    }

    public ArrayList getEntitiesAssociations(DbObject model, MetaClass metaclass,
            boolean isAssociation) throws DbException {
        ArrayList occurences = new ArrayList();
        DbORTable dbTable = null;

        if (model == null || metaclass == null)
            return occurences;

        DbEnumeration dbEnum = model.getComponents().elements(DbORTable.metaClass);
        while (dbEnum.hasMoreElements()) {
            dbTable = (DbORTable) dbEnum.nextElement();
            if (isAssociation) {
                if (dbTable.isIsAssociation())
                    occurences.add(dbTable);
            } else {
                if (!dbTable.isIsAssociation())
                    occurences.add(dbTable);
            }
        }
        dbEnum.close();
        return occurences;
    }

    public ArrayList getErRoles(DbObject model, MetaClass metaclass) throws DbException {
        ArrayList occurences = new ArrayList();
        DbORAssociation association = null;

        if (model == null || metaclass == null)
            return occurences;

        DbEnumeration dbEnum = model.getComponents().elements(DbORAssociation.metaClass);
        while (dbEnum.hasMoreElements()) {
            association = (DbORAssociation) dbEnum.nextElement();
            occurences.add(association.getArcEnd());
        }
        dbEnum.close();
        return occurences;
    }

    /*
     * protected methods
     */

    protected int getModelType(DbObject model) throws Exception {
        if (model instanceof DbORDataModel) {
            int mode = ((DbORDataModel) model).getLogicalMode().intValue();
            int modelType = 0;

            if (mode == DbORDataModel.LOGICAL_MODE_OBJECT_RELATIONAL)
                modelType = OR_DATAMODEL;
            else
                modelType = ER_DATAMODEL;
            return modelType;
        } else if (model instanceof DbORDomainModel)
            return DOMAIN_MODEL;
        else if (model instanceof DbORCommonItemModel)
            return COMMONITEM_MODEL;
        else if (model instanceof DbOROperationLibrary)
            return OPERATION_LIBRARY;
        else
            return -1;
    }

    /**
     * CleanUp 1 occurence
     */
    protected static boolean deleteOneOccurence(DbObject dbo) throws DbException {
        dbo.remove();
        int status = dbo.getTransStatus();
        if (status == Db.OBJ_REMOVED) {
            return true;
        }
        return false;
    }

    /**
     * CleanUp occurences of a rule section
     */
    protected static boolean deleteSectionsOccurences(int ruleIndx) throws DbException {
        for (int i = 0; i < occurencesToClean[ruleIndx].size(); i++) {
            DbObject dbo = (DbObject) (occurencesToClean[ruleIndx].get(i));
            dbo.remove();
            int status = dbo.getTransStatus();
            if (status != Db.OBJ_REMOVED)
                return false;
        }
        return true;
    }

    /**
     * CleanUp All occurences
     */
    protected static boolean deleteAllSectionOccurences() throws DbException {
        for (int i = 0; i < occurencesToClean.length; i++) {
            for (int j = 0; j < occurencesToClean[i].size(); j++) {
                DbObject dbo = (DbObject) (occurencesToClean[i].get(j));
                dbo.remove();
                int status = dbo.getTransStatus();
                if (status != Db.OBJ_REMOVED)
                    return false;
            }
        }
        return true;
    }
    
    public boolean doListenSelection() { return (this instanceof PluginSelectionListener); }
}
