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

package org.modelsphere.sms.or.db.util;

import java.util.Arrays;
import java.util.Collection;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaRelation1;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.sms.db.DbSMSAbstractPackage;
import org.modelsphere.sms.db.DbSMSBuiltInTypePackage;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.oo.db.DbOOClass;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAbstractMethod;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORAttribute;
import org.modelsphere.sms.or.db.DbORBuiltInType;
import org.modelsphere.sms.or.db.DbORCheck;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.db.DbORParameter;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORProcedure;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTrigger;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.db.DbORView;
import org.modelsphere.sms.or.db.srtypes.ORConstraintType;
import org.modelsphere.sms.or.db.srtypes.ORDomainCategory;
import org.modelsphere.sms.or.generic.db.DbGECheck;
import org.modelsphere.sms.or.generic.db.DbGEColumn;
import org.modelsphere.sms.or.generic.db.DbGEDataModel;
import org.modelsphere.sms.or.generic.db.DbGEDatabase;
import org.modelsphere.sms.or.generic.db.DbGEForeign;
import org.modelsphere.sms.or.generic.db.DbGEIndex;
import org.modelsphere.sms.or.generic.db.DbGEOperationLibrary;
import org.modelsphere.sms.or.generic.db.DbGEParameter;
import org.modelsphere.sms.or.generic.db.DbGEPrimaryUnique;
import org.modelsphere.sms.or.generic.db.DbGEProcedure;
import org.modelsphere.sms.or.generic.db.DbGETable;
import org.modelsphere.sms.or.generic.db.DbGETrigger;
import org.modelsphere.sms.or.generic.db.DbGEView;
import org.modelsphere.sms.or.ibm.db.DbIBMCheck;
import org.modelsphere.sms.or.ibm.db.DbIBMColumn;
import org.modelsphere.sms.or.ibm.db.DbIBMDataModel;
import org.modelsphere.sms.or.ibm.db.DbIBMDatabase;
import org.modelsphere.sms.or.ibm.db.DbIBMForeign;
import org.modelsphere.sms.or.ibm.db.DbIBMIndex;
import org.modelsphere.sms.or.ibm.db.DbIBMOperationLibrary;
import org.modelsphere.sms.or.ibm.db.DbIBMParameter;
import org.modelsphere.sms.or.ibm.db.DbIBMPrimaryUnique;
import org.modelsphere.sms.or.ibm.db.DbIBMProcedure;
import org.modelsphere.sms.or.ibm.db.DbIBMTable;
import org.modelsphere.sms.or.ibm.db.DbIBMTrigger;
import org.modelsphere.sms.or.ibm.db.DbIBMView;
import org.modelsphere.sms.or.informix.db.DbINFCheck;
import org.modelsphere.sms.or.informix.db.DbINFColumn;
import org.modelsphere.sms.or.informix.db.DbINFDataModel;
import org.modelsphere.sms.or.informix.db.DbINFDatabase;
import org.modelsphere.sms.or.informix.db.DbINFForeign;
import org.modelsphere.sms.or.informix.db.DbINFIndex;
import org.modelsphere.sms.or.informix.db.DbINFOperationLibrary;
import org.modelsphere.sms.or.informix.db.DbINFParameter;
import org.modelsphere.sms.or.informix.db.DbINFPrimaryUnique;
import org.modelsphere.sms.or.informix.db.DbINFProcedure;
import org.modelsphere.sms.or.informix.db.DbINFTable;
import org.modelsphere.sms.or.informix.db.DbINFTrigger;
import org.modelsphere.sms.or.informix.db.DbINFView;
import org.modelsphere.sms.or.oracle.db.DbORACheck;
import org.modelsphere.sms.or.oracle.db.DbORAColumn;
import org.modelsphere.sms.or.oracle.db.DbORADataModel;
import org.modelsphere.sms.or.oracle.db.DbORADatabase;
import org.modelsphere.sms.or.oracle.db.DbORAForeign;
import org.modelsphere.sms.or.oracle.db.DbORAIndex;
import org.modelsphere.sms.or.oracle.db.DbORAParameter;
import org.modelsphere.sms.or.oracle.db.DbORAPrimaryUnique;
import org.modelsphere.sms.or.oracle.db.DbORAProcedure;
import org.modelsphere.sms.or.oracle.db.DbORAProgramUnitLibrary;
import org.modelsphere.sms.or.oracle.db.DbORASequence;
import org.modelsphere.sms.or.oracle.db.DbORATable;
import org.modelsphere.sms.or.oracle.db.DbORATrigger;
import org.modelsphere.sms.or.oracle.db.DbORAView;
import org.modelsphere.sms.plugins.TargetSystem;

public abstract class AnyORObject {

    public final static int I_TABLE = 0;
    public final static int I_VIEW = 1;
    public final static int I_COLUMN = 2;
    public final static int I_PRIMARYUNIQUE = 3;
    public final static int I_FOREIGN = 4;
    public final static int I_CHECK = 5;
    public final static int I_INDEX = 6;
    public final static int I_TRIGGER = 7;
    public final static int I_PROCEDURE = 8;
    public final static int I_PARAMETER = 9;
    public final static int I_DATAMODEL = 10;
    public final static int I_OPERATIONLIBRARY = 11;
    public final static int I_DATABASE = 12;

    public final static MetaClass[] ORMetaClasses = new MetaClass[] { DbORTable.metaClass,
            DbORView.metaClass, DbORColumn.metaClass, DbORPrimaryUnique.metaClass,
            DbORForeign.metaClass, DbORCheck.metaClass, DbORIndex.metaClass, DbORTrigger.metaClass,
            DbORProcedure.metaClass, DbORParameter.metaClass, DbORDataModel.metaClass,
            DbOROperationLibrary.metaClass, DbORDatabase.metaClass };

    // All classes must be final in the following list of classes per target system
    public final static MetaClass[] GEMetaClasses = new MetaClass[] { DbGETable.metaClass,
            DbGEView.metaClass, DbGEColumn.metaClass, DbGEPrimaryUnique.metaClass,
            DbGEForeign.metaClass, DbGECheck.metaClass, DbGEIndex.metaClass, DbGETrigger.metaClass,
            DbGEProcedure.metaClass, DbGEParameter.metaClass, DbGEDataModel.metaClass,
            DbGEOperationLibrary.metaClass, DbGEDatabase.metaClass };

    public final static MetaClass[] IBMMetaClasses = new MetaClass[] { DbIBMTable.metaClass,
            DbIBMView.metaClass, DbIBMColumn.metaClass, DbIBMPrimaryUnique.metaClass,
            DbIBMForeign.metaClass, DbIBMCheck.metaClass, DbIBMIndex.metaClass,
            DbIBMTrigger.metaClass, DbIBMProcedure.metaClass, DbIBMParameter.metaClass,
            DbIBMDataModel.metaClass, DbIBMOperationLibrary.metaClass, DbIBMDatabase.metaClass };

    public final static MetaClass[] INFMetaClasses = new MetaClass[] { DbINFTable.metaClass,
            DbINFView.metaClass, DbINFColumn.metaClass, DbINFPrimaryUnique.metaClass,
            DbINFForeign.metaClass, DbINFCheck.metaClass, DbINFIndex.metaClass,
            DbINFTrigger.metaClass, DbINFProcedure.metaClass, DbINFParameter.metaClass,
            DbINFDataModel.metaClass, DbINFOperationLibrary.metaClass, DbINFDatabase.metaClass };

    public final static MetaClass[] ORAMetaClasses = new MetaClass[] { DbORATable.metaClass,
            DbORAView.metaClass, DbORAColumn.metaClass, DbORAPrimaryUnique.metaClass,
            DbORAForeign.metaClass, DbORACheck.metaClass, DbORAIndex.metaClass,
            DbORATrigger.metaClass, DbORAProcedure.metaClass, DbORAParameter.metaClass,
            DbORADataModel.metaClass, DbORAProgramUnitLibrary.metaClass, DbORADatabase.metaClass };

    public final static int[] targetSystemRootIDs = new int[] { TargetSystem.SGBD_LOGICAL,
            TargetSystem.SGBD_IBM_DB2_ROOT, TargetSystem.SGBD_INFORMIX_ROOT,
            TargetSystem.SGBD_ORACLE_ROOT };
    public final static MetaClass[][] targetSystemMetaClasses = new MetaClass[][] { GEMetaClasses,
            IBMMetaClasses, INFMetaClasses, ORAMetaClasses };

    private static int[] metaClassesMap = null;

    private static void initMetaClassesMap() {
        if (metaClassesMap != null)
            return;
        metaClassesMap = new int[MetaClass.getNbMetaClasses()];
        Arrays.fill(metaClassesMap, -1);
        for (int i = 0; i < targetSystemMetaClasses.length; i++)
            for (int j = 0; j < targetSystemMetaClasses[i].length; j++)
                metaClassesMap[targetSystemMetaClasses[i][j].getSeqNo()] = (i << 8) + j;
    }

    public static DbSMSTargetSystem getTargetSystem(DbObject dbo) throws DbException {
        DbSMSAbstractPackage pack = (DbSMSAbstractPackage) (dbo instanceof DbSMSAbstractPackage ? dbo
                : dbo.getCompositeOfType(DbSMSAbstractPackage.metaClass));
        return (pack != null ? pack.getTargetSystem() : null);
    }

    public static MetaClass[] getTargetMetaClasses(DbObject dbo) throws DbException {
        DbSMSTargetSystem target = getTargetSystem(dbo);
        if (target == null)
            return null;
        int rootID = getRootIDFromTargetSystem(target);
        MetaClass[] mcs = getTargetMetaClasses(rootID);
        return mcs;
    }

    public static MetaClass[] getTargetMetaClasses(int rootID) {
        if (rootID == TargetSystem.SGBD_JAVA)
            return null;
        for (int i = 0; i < targetSystemRootIDs.length; i++) {
            if (rootID == targetSystemRootIDs[i])
                return targetSystemMetaClasses[i];
        }
        return GEMetaClasses;
    }

    // <metaClass> must be final.
    public static int getMetaClassIndex(MetaClass metaClass) {
        initMetaClassesMap();
        int i = metaClassesMap[metaClass.getSeqNo()];
        return (i == -1 ? -1 : i & 255);
    }

    // <metaClass> must be final.
    public static int getMetaClassRootID(MetaClass metaClass) {
        initMetaClassesMap();
        int i = metaClassesMap[metaClass.getSeqNo()];
        return (i == -1 ? -1 : targetSystemRootIDs[i >> 8]);
    }

    // <metaClass> must be final.
    public static MetaClass translateMetaClass(MetaClass metaClass, int rootID) {
        int index = getMetaClassIndex(metaClass);
        if (index == -1)
            return metaClass;
        return getTargetMetaClasses(rootID)[index];
    }

    public static DbORDataModel createDataModel(DbObject composite, DbSMSTargetSystem target)
            throws DbException {
        int rootID = getRootIDFromTargetSystem(target);

        switch (rootID) {
        case TargetSystem.SGBD_ORACLE_ROOT:
            return new DbORADataModel(composite, target);
        case TargetSystem.SGBD_INFORMIX_ROOT:
            return new DbINFDataModel(composite, target);
        case TargetSystem.SGBD_IBM_DB2_ROOT:
            return new DbIBMDataModel(composite, target);
        default:
            return new DbGEDataModel(composite, target);
        }
    }

    public static DbORDataModel createConceptualDataModel(DbObject composite,
            DbSMSTargetSystem target) throws DbException {
        return new DbGEDataModel(composite, target, DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP);
    }

    public static DbORDatabase createDatabase(DbObject composite, DbSMSTargetSystem target)
            throws DbException {
        int rootID = getRootIDFromTargetSystem(target);

        switch (rootID) {
        case TargetSystem.SGBD_ORACLE_ROOT:
            return new DbORADatabase(composite, target);
        case TargetSystem.SGBD_INFORMIX_ROOT:
            return new DbINFDatabase(composite, target);
        case TargetSystem.SGBD_IBM_DB2_ROOT:
            return new DbIBMDatabase(composite, target);
        default:
            return new DbGEDatabase(composite, target);
        }
    }

    public static DbOROperationLibrary createOperationLibrary(DbObject composite,
            DbSMSTargetSystem target) throws DbException {
        int rootID = getRootIDFromTargetSystem(target);

        switch (rootID) {
        case TargetSystem.SGBD_ORACLE_ROOT:
            return new DbORAProgramUnitLibrary(composite, target);
        case TargetSystem.SGBD_INFORMIX_ROOT:
            return new DbINFOperationLibrary(composite, target);
        case TargetSystem.SGBD_IBM_DB2_ROOT:
            return new DbIBMOperationLibrary(composite, target);
        default:
            return new DbGEOperationLibrary(composite, target);
        }
    }

    public static DbORForeign createForeignKey(DbORAssociationEnd assocEnd, DbSMSTargetSystem target)
            throws DbException {
        int rootID = getRootIDFromTargetSystem(target);

        switch (rootID) {
        case TargetSystem.SGBD_ORACLE_ROOT:
            return new DbORAForeign(assocEnd);
        case TargetSystem.SGBD_INFORMIX_ROOT:
            return new DbINFForeign(assocEnd);
        case TargetSystem.SGBD_IBM_DB2_ROOT:
            return new DbIBMForeign(assocEnd);
        default:
            return new DbGEForeign(assocEnd);
        }
    }

    public static DbORTable createERRelationship(DbORDataModel dataModel) throws DbException {
        DbORTable erRelationship = null;
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology terminology = terminologyUtil.findModelTerminology(dataModel);

        if (dataModel instanceof DbGEDataModel)
            erRelationship = new DbGETable(dataModel);
        else if (dataModel instanceof DbORADataModel)
            erRelationship = new DbORATable(dataModel);
        else if (dataModel instanceof DbINFDataModel)
            erRelationship = new DbINFTable(dataModel);
        else if (dataModel instanceof DbIBMDataModel)
            erRelationship = new DbIBMTable(dataModel);
        else
            throw new RuntimeException("Unknown composite.");

        erRelationship.setIsAssociation(Boolean.TRUE);
        String term = terminology.getTerm(DbOOClass.metaClass);
        erRelationship.setName(term);

        return erRelationship;
    }

    //encapsulate targetSystem to rootID mapping
    public static int getRootIDFromTargetSystem(DbSMSTargetSystem target) throws DbException {
        int rootID = target.getRootID().intValue();
        int tsID = target.getID().intValue();
        rootID = getActualRootID(rootID, tsID);
        return rootID;
    } //end getRootIDFromTargetSystem

    public static int getActualRootID(int rootID, int tsID) {
        //DB2-UDB is a special case
        if (rootID == TargetSystem.SGBD_IBM_DB2_ROOT) {

            if (tsID >= TargetSystem.SGBD_DB2_UNIVERSAL_DB_81) {
                rootID = TargetSystem.SGBD_IBM_DB2_ROOT;
            } else {
                rootID = TargetSystem.SGBD_LOGICAL;
            }
        } //end if

        return rootID;
    } //end getActualRootID()

    public static MetaRelationship getTypeField(DbObject dbo) {
        if (dbo instanceof DbORCommonItem)
            return DbORCommonItem.fType;
        if (dbo instanceof DbORDomain)
            return DbORDomain.fSourceType;
        if (dbo instanceof DbORAttribute)
            return DbORAttribute.fType;
        if (dbo instanceof DbORParameter)
            return DbORParameter.fType;
        if (dbo instanceof DbORProcedure)
            return DbORProcedure.fReturnType;
        return null;
    }

    public static DbORTypeClassifier getTargetSystemType(DbObject dbo) throws DbException {
        MetaRelationship metaRel = getTypeField(dbo);
        if (metaRel == null)
            return null;
        metaRel.getMetaClass().getJClass();
        DbORTypeClassifier directType = (DbORTypeClassifier) dbo.get(metaRel);
        if (directType instanceof DbORBuiltInType)
            return directType;
        if (directType instanceof DbORDomain) {
            ORDomainCategory categ = (ORDomainCategory) ((DbORDomain) directType).getCategory();
            if (categ.getValue() == ORDomainCategory.DOMAIN) {
                return ((DbORDomain) directType).getSourceType();
            }
            return directType;
        }
        return null;
    }

    // Add to the collection all the types of the project belonging to the target system
    public static Collection<DbObject> getAllTypes(Collection<DbObject> collTypes,
            DbSMSTargetSystem target)
            throws DbException {
        getBuiltIns(collTypes, target.getBuiltInTypePackage());
        getDomains(collTypes, (DbSMSProject) target.getProject(), target);
        return collTypes;
    }

    // Add to the collection all the types of the project
    public static Collection<DbObject> getAllTypes(Collection<DbObject> collTypes,
            DbSMSProject project)
            throws DbException {
        DbEnumeration dbEnum = project.getBuiltInTypeNode().getComponents().elements(
                DbSMSBuiltInTypePackage.metaClass);
        while (dbEnum.hasMoreElements())
            getBuiltIns(collTypes, (DbSMSBuiltInTypePackage) dbEnum.nextElement());
        dbEnum.close();
        getDomains(collTypes, project, null);
        return collTypes;
    }

    private static void getBuiltIns(Collection<DbObject> collTypes, DbSMSBuiltInTypePackage pack)
            throws DbException {
        DbEnumeration dbEnum = pack.getComponents().elements(DbORBuiltInType.metaClass);
        while (dbEnum.hasMoreElements())
            collTypes.add(dbEnum.nextElement());
        dbEnum.close();
    }

    private static void getDomains(Collection<DbObject> collTypes, DbSMSProject project,
            DbSMSTargetSystem target) throws DbException {
        DbEnumeration enumModel = project.componentTree(DbORDomainModel.metaClass);
        while (enumModel.hasMoreElements()) {
            DbORDomainModel model = (DbORDomainModel) enumModel.nextElement();
            if (target != null && model.getTargetSystem() != target)
                continue;
            DbEnumeration dbEnum = model.getComponents().elements(DbORDomain.metaClass);
            while (dbEnum.hasMoreElements())
                collTypes.add(dbEnum.nextElement());
            dbEnum.close();
        }
        enumModel.close();
    }

    public static DbORPrimaryUnique getPrimaryKey(DbORAbsTable table) throws DbException {
        DbORPrimaryUnique primaryKey = null;
        DbEnumeration dbEnum = table.getComponents().elements(DbORPrimaryUnique.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORPrimaryUnique key = (DbORPrimaryUnique) dbEnum.nextElement();
            if (key.isPrimary()) {
                primaryKey = key;
                break;
            }
        }
        dbEnum.close();
        return primaryKey;
    }

    public static boolean isForeignAssocEnd(DbORAssociationEnd assocEnd) throws DbException {
        if (!assocEnd.isNavigable())
            return false;
        ORConstraintType type = assocEnd.getConstraintType();
        if (type != null && type.getValue() != ORConstraintType.FOREIGNKEY)
            return false;
        int mult = assocEnd.getMultiplicity().getValue();
        return (mult == SMSMultiplicity.OPTIONAL || mult == SMSMultiplicity.EXACTLY_ONE);
    }

    public static MetaRelation1 getUserField(DbObject dbo) {
        if (dbo instanceof DbORAbsTable)
            return DbORAbsTable.fUser;
        if (dbo instanceof DbORAbstractMethod)
            return DbORAbstractMethod.fUser;
        if (dbo instanceof DbORDomain)
            return DbORDomain.fUser;
        if (dbo instanceof DbORIndex)
            return DbORIndex.fUser;
        if (dbo instanceof DbORASequence)
            return DbORASequence.fUser;
        if (dbo instanceof DbINFCheck)
            return DbINFCheck.fUser;
        if (dbo instanceof DbINFForeign)
            return DbINFForeign.fUser;
        if (dbo instanceof DbINFPrimaryUnique)
            return DbINFPrimaryUnique.fUser;
        return null;
    }
}
