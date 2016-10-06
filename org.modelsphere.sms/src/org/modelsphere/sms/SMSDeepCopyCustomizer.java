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

package org.modelsphere.sms;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DeepCopyCustomizer;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEContextGo;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEFlowGo;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBEQualifier;
import org.modelsphere.sms.be.db.DbBEResource;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSCommonItemGo;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.db.DbSMSPackageGo;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.db.util.AnySemObject;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOOConstructor;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORBuiltInType;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.db.util.AnyORObject;
import org.modelsphere.sms.or.ibm.db.DbIBMColumn;
import org.modelsphere.sms.or.oracle.db.DbORATrigger;
import org.modelsphere.sms.or.oracle.db.srtypes.ORATriggerTime;
import org.modelsphere.sms.plugins.TargetSystem;
import org.modelsphere.sms.plugins.TargetSystemInfo;
import org.modelsphere.sms.plugins.TargetSystemManager;

public class SMSDeepCopyCustomizer implements DeepCopyCustomizer {

    protected DbSMSTargetSystem targetSystem; // composite target system.
    protected int rootID; // root ID of the composite target system (-1 if not
    // in a OR model).
    private HashMap typeMap; // built-in types of the composite target system.
    private HashMap domainMap; // domains of the composite target system.

    private DbBEModel sourceBEModel;
    private DbBEModel targetBEModel;

    // Complete definition of ordinary (non-relation) fields.
    // Complete definition of ordinary (non-relation) fields.
    private static final String COPY_OF = LocaleMgr.message.getString("CopyOf");
    private static final String COPY_OF_NUMBERED = LocaleMgr.message.getString("CopyOfNumbered");

    /*
     * If (rootID != -1), the composite under which we are copying is a DbORModel or a descendant of
     * a DbORModel; in this case, we have to instantiate all copied objects in the classes of the
     * composite target system, and we have to translate the types of typed objects to corresponding
     * types of the composite target system.
     */
    public SMSDeepCopyCustomizer(DbProject srcProject, DbObject destComposite) throws DbException {
        rootID = -1;
        typeMap = null;
        domainMap = null;
        targetSystem = AnyORObject.getTargetSystem(destComposite);
        if (targetSystem == null) {
            // Copying under a project or a user package: if inter-project copy,
            // add to the target project all the nneded target systems.
            if (srcProject != destComposite.getProject())
                createTargetSystems(srcProject, destComposite.getProject());
        } else {
            rootID = AnyORObject.getRootIDFromTargetSystem(targetSystem);
            if (rootID == TargetSystem.SGBD_JAVA)
                rootID = -1;
        }

        if (destComposite instanceof DbBEModel) {
            targetBEModel = (DbBEModel) destComposite;
        } else {
            targetBEModel = (DbBEModel) destComposite.getCompositeOfType(DbBEModel.metaClass);
        }
    }

    private void createTargetSystems(DbProject srcProject, DbProject destProject)
            throws DbException {
        TargetSystem ts = TargetSystemManager.getSingleton();
        List<Serializable> srcTargets = TargetSystem.getTargetSystems(srcProject, true);
        for (int i = 0; i < srcTargets.size(); i++) {
            int targetID = ((DbSMSTargetSystem) srcTargets.get(i)).getID().intValue();
            if (TargetSystem.getSpecificTargetSystem(destProject, targetID) == null)
                ts.createTargetSystem(destProject, targetID);
        }
    }

    public final MetaClass getDestMetaClass(DbObject srcObj, DbObject destComposite)
            throws DbException {
        MetaClass metaClass = srcObj.getMetaClass();
        if (rootID != -1)
            metaClass = AnyORObject.translateMetaClass(metaClass, rootID);
        return metaClass;
    }

    private int findNextSequenceId(DbObject composite, MetaClass objectMetaClass)
            throws DbException {

        ////
        // look for these two prefixes and collect the sequence numbers

        int prefixLength = 6;
        int length = COPY_OF.length();
        String COPYOF = COPY_OF.substring(0, 5) + '(';

        ArrayList<Boolean> sequenceIdList = new ArrayList<Boolean>();
        sequenceIdList.add(Boolean.TRUE);
        sequenceIdList.add(Boolean.TRUE);

        DbEnumeration dbEnumeration = composite.getComponents().elements(objectMetaClass);
        while (dbEnumeration.hasMoreElements()) {
            DbObject obj = dbEnumeration.nextElement();
            String localObjectName = obj.getName();
            int localLength = localObjectName.length();
            if (localLength >= prefixLength) {
                if (localObjectName.substring(0, prefixLength).equals(COPYOF)) {

                    ////
                    // we have a candidate sequence id, attempt extracting it and place it in our list

                    int extractedId = extractId(prefixLength - 1, localObjectName);

                    if (sequenceIdList.size() < extractedId + 1)
                        while (sequenceIdList.size() < extractedId + 1)
                            sequenceIdList.add(Boolean.FALSE);

                    sequenceIdList.set(extractedId, Boolean.TRUE);
                }
            }
        }
        dbEnumeration.close();

        ////
        // find the first gap in the sequence id list and return that Id  
        int i;
        for (i = 0; i < sequenceIdList.size(); i++) {
            Boolean retval = sequenceIdList.get(i);
            if (retval.booleanValue() == false)
                return i;
        }
        return i;
    }

    public int extractId(int beginPos, String name) {

        // //
        // begin pos indicates the position at which the '(' is located
        // we must collect the substring between this position and the immediate
        // next ')' character
        // and return it as an integer

        String sequenceId = "0";
        int i;
        for (i = beginPos + 1; i < name.length(); i++) {
            if (name.charAt(i) == ')')
                break;
        }
        if (i != name.length()) {
            sequenceId = name.substring(beginPos + 1, i);
        }

        int retval = 0;

        try {
            retval = new Integer(sequenceId).intValue();
        } catch (Exception e) {
        }

        return retval;
    }

    private String getCopyOfName(String name, DbObject newObject, DbObject composite)
            throws DbException {

        // if the composite does not contain this name occurrence, return the
        // name itself...

        boolean bAlreadyPrefixed = false;
        int nameOccursCount = 0;
        MetaClass objectMetaClass = newObject.getMetaClass();

        DbEnumeration dbEnumeration = composite.getComponents().elements(objectMetaClass);
        while (dbEnumeration.hasMoreElements()) {
            DbObject obj = dbEnumeration.nextElement();
            String localObjectName = obj.getName();
            if (localObjectName != null) {
                if (localObjectName.equals(name)) {
                    nameOccursCount++;
                }
            }
        }
        dbEnumeration.close();
        if (nameOccursCount == 1)
            return name;

        // check whether the name is already prefixed with "Copy of "

        // //
        // find if any of the strings in the composite components list already
        // has the prefix

        int prefixLength = 7;
        String COPYOF = COPY_OF.substring(0, prefixLength);

        dbEnumeration = composite.getComponents().elements(objectMetaClass);
        while (dbEnumeration.hasMoreElements()) {
            DbObject obj = dbEnumeration.nextElement();
            String localObjectName = obj.getName();
            if (localObjectName != null) {
                if (!localObjectName.equals(name)) {
                    if (localObjectName.length() >= prefixLength) {
                        String prefix = localObjectName.substring(0, prefixLength);
                        if (prefix.equals(COPYOF)) {
                            bAlreadyPrefixed = true;
                            break;
                        }
                    }
                }
            }
        }
        dbEnumeration.close();

        if (bAlreadyPrefixed == false) {

            name = MessageFormat.format(COPY_OF, new String[] { name });
            return name;
        }

        // //
        // if "Copy of" was indeed found, we must find the next valid sequence
        // Id and build the name accordingly

        Integer nextSeqId = new Integer(findNextSequenceId(composite, objectMetaClass));
        return MessageFormat.format(COPY_OF_NUMBERED, new String[] { nextSeqId.toString(), name });
    }

    public final void initFields(DbObject srcObj, DbObject destObj, boolean namePrefixedWithCopyOf)
            throws DbException {
        String name;

        if (srcObj == null) {
            name = "";
        } else {
            name = srcObj.getName();
        }

        if (name == null)
            name = "";
        else if (name.equalsIgnoreCase(""))
            name = "";
        else if (namePrefixedWithCopyOf) {
            name = getCopyOfName(name, srcObj, destObj.getComposite());
            // name = MessageFormat.format(COPY_OF, new String[] {name});
        }

        destObj.setName(name);

        if (destObj instanceof DbOOConstructor) {
            destObj.setName(null); // a constructor has the name of the class
        } else if (srcObj.getMetaClass() != destObj.getMetaClass()) {
            if (destObj instanceof DbORATrigger) {
                ((DbORATrigger) destObj).setTime(ORATriggerTime.getInstance(ORATriggerTime.BEFORE));
                ((DbORATrigger) destObj).setRowTrigger(Boolean.TRUE);
            } else if (destObj instanceof DbIBMColumn) {
                ((DbIBMColumn) destObj).setLogged(Boolean.TRUE);
            } // end if
        } // end uf
    } // end initFields()

    // Return null if not linkable
    // BEWARE: relations of <destObj> are not yet specified, except fComposite.
    // Overridden for integration
    public DbObject resolveLink(DbObject srcObj, MetaRelationship metaRel, DbObject srcNeighbor)
            throws DbException {
        if ((metaRel.getFlags() & MetaField.COPY_REFS) == 0)
            return null;
        if (rootID != -1) {
            if (srcNeighbor instanceof DbSMSTargetSystem)
                return targetSystem;
            if (srcNeighbor instanceof DbORTypeClassifier)
                return translateType((DbORTypeClassifier) srcNeighbor);
        }

        if ((srcNeighbor instanceof DbBEQualifier) || (srcNeighbor instanceof DbBEResource)
                || (srcNeighbor instanceof DbBEActor) || (srcNeighbor instanceof DbBEStore)) {
            sourceBEModel = (DbBEModel) srcObj.getCompositeOfType(DbBEModel.metaClass);
            if (sourceBEModel != null) {
                // We must ensure that the corresponding object in the target
                // Model is linked instead of the source model object
                // If not the same project
                if (sourceBEModel != targetBEModel) {
                    if (srcNeighbor instanceof DbBEQualifier) {
                        return targetBEModel.findComponentByName(DbBEQualifier.metaClass,
                                srcNeighbor.getName());
                    } else if (srcNeighbor instanceof DbBEResource) {
                        return targetBEModel.findComponentByName(DbBEResource.metaClass,
                                srcNeighbor.getName());
                    } else if (srcNeighbor instanceof DbBEActor) {
                        return targetBEModel.findComponentByName(DbBEActor.metaClass, srcNeighbor
                                .getName());
                    } else if (srcNeighbor instanceof DbBEStore) {
                        return targetBEModel.findComponentByName(DbBEStore.metaClass, srcNeighbor
                                .getName());
                    }
                }
            }
        }

        if (srcObj instanceof DbBEContextGo) {
            // must be in same usecase, otherwise, this context go must not be
            // paste
            DbObject composite = srcObj.getCompositeOfType(DbBEDiagram.metaClass);
            DbObject compositeMatch = composite.getMatchingObject();
            // source and target composite (for diagram) should be the same,
            // otherwise this context go is invalid in the target diagram
            if (compositeMatch.getComposite() != composite.getComposite())
                return null;
        }

        DbObject neighbor = srcNeighbor.findMatchingObject();
        if (neighbor == null)
            return null;
        if (srcObj instanceof DbSMSGraphicalObject) {
            if (metaRel == DbSMSClassifierGo.fClassifier
                    || metaRel == DbSMSCommonItemGo.fCommonItem
                    || metaRel == DbSMSPackageGo.fPackage || metaRel == DbBEFlowGo.fFlow) {
                DbSMSDiagram diag = (DbSMSDiagram) srcObj.getMatchingObject().getComposite();
                if (diag instanceof DbORDiagram) {
                    if (diag.getComposite() != neighbor.getCompositeOfType(DbSMSPackage.metaClass))
                        neighbor = null;
                }
                if (diag instanceof DbBEDiagram) {
                    if (diag.getCompositeOfType(DbSMSPackage.metaClass) != neighbor
                            .getCompositeOfType(DbSMSPackage.metaClass))
                        neighbor = null;
                }
            }
        }
        return neighbor;
    }

    public final DbORTypeClassifier translateType(DbORTypeClassifier srcType) throws DbException {
        if (rootID == -1)
            return (DbORTypeClassifier) srcType.findMatchingObject();
        DbSMSTargetSystem srcTarget = AnyORObject.getTargetSystem(srcType);
        if (srcTarget.getID().equals(targetSystem.getID()))
            return (DbORTypeClassifier) srcType.findMatchingObject();

        // Type from another target system, look for a type with the same name
        // in the dest target system.
        DbORTypeClassifier type;
        String name = srcType.getName();
        if (srcType instanceof DbORBuiltInType) {
            int srcID = AnyORObject.getRootIDFromTargetSystem(srcTarget);
            if (rootID != srcID) {
                // Not the same target system supplier, get the equivalent type
                // name in the dest target system.
                TargetSystemInfo targetInfo = TargetSystemManager.getSingleton()
                        .getTargetSystemInfo(targetSystem);
                TargetSystemInfo srcTargetInfo = TargetSystemManager.getSingleton()
                        .getTargetSystemInfo(srcTarget);
                name = srcTargetInfo.mapType(name, targetInfo); // returns null
                // if no mapping
            }
            type = getTypeFromName(name);
        } else
            // DbORDomain
            type = getDomainFromName(name);
        return type;
    }

    // Returns null if name is null or there is no type of this name in the
    // target system
    public final DbORBuiltInType getTypeFromName(String name) throws DbException {
        if (typeMap == null) {
            typeMap = new HashMap();
            DbEnumeration dbEnum = targetSystem.getBuiltInTypePackage().getComponents().elements(
                    DbORBuiltInType.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbORBuiltInType type = (DbORBuiltInType) dbEnum.nextElement();
                typeMap.put(type.getName(), type);
            }
            dbEnum.close();
        }
        return (DbORBuiltInType) typeMap.get(name);
    }

    // Returns null if name is null or there is no domain of this name in the
    // target system
    public final DbORDomain getDomainFromName(String name) throws DbException {
        if (domainMap == null) {
            domainMap = new HashMap();
            DbEnumeration enumModel = targetSystem.getPackages()
                    .elements(DbORDomainModel.metaClass);
            while (enumModel.hasMoreElements()) {
                DbEnumeration enumDomain = enumModel.nextElement().getComponents().elements(
                        DbORDomain.metaClass);
                while (enumDomain.hasMoreElements()) {
                    DbORDomain domain = (DbORDomain) enumDomain.nextElement();
                    domainMap.put(domain.getName(), domain);
                }
                enumDomain.close();
            }
            enumModel.close();
        }
        return (DbORDomain) domainMap.get(name);
    }

    // If copy from a super-schema to a sub-schema, or the opposite side, set
    // the superCopy link whenever possible.
    public final void endCopy(ArrayList srcRoots) throws DbException {
        int i;
        for (i = 0; i < srcRoots.size(); i++) {
            DbObject dbo = (DbObject) srcRoots.get(i);
            if (!AnySemObject.supportsSuper(dbo.getMetaClass()))
                continue;
            if (dbo instanceof DbORAssociation)
                continue; // process the associations in a further step
            DbSMSSemanticalObject srcObj = (DbSMSSemanticalObject) dbo;
            DbSMSSemanticalObject destObj = (DbSMSSemanticalObject) srcObj.getMatchingObject();
            if (destObj == null)
                continue;
            DbSMSSemanticalObject srcComposite = (DbSMSSemanticalObject) srcObj.getComposite();
            DbSMSSemanticalObject destComposite = (DbSMSSemanticalObject) destObj.getComposite();
            if (srcComposite == AnySemObject.getSuperCopy(destComposite)) {
                // Copy from a super-schema to a sub-schema: link if the object
                // in the super-schema
                // is not already linked to an object in the sub-schema.
                if (AnySemObject.getSubCopy(srcObj, destComposite) == null)
                    setSuperCopy(srcObj, true);
            } else if (destComposite == AnySemObject.getSuperCopy(srcComposite)) {
                // Copy from a sub-schema to a super-schema.
                if (srcObj.getSuperCopy() == null)
                    setSuperCopy(srcObj, false);
            }
        }

        // Link an association only if both its tables are linked.
        for (i = 0; i < srcRoots.size(); i++) {
            DbObject dbo = (DbObject) srcRoots.get(i);
            if (!(dbo instanceof DbORAssociation))
                continue;
            DbORAssociation srcAssoc = (DbORAssociation) dbo;
            DbORAssociation destAssoc = (DbORAssociation) srcAssoc.getMatchingObject();
            DbORAbsTable destFrontTable = destAssoc.getFrontEnd().getClassifier();
            DbORAbsTable destBackTable = destAssoc.getBackEnd().getClassifier();
            if (destFrontTable == null || destBackTable == null)
                continue; // association copied without its tables, will be
            // deleted in a further step.
            DbORAbsTable srcFrontTable = (DbORAbsTable) destFrontTable.getMatchingObject();
            DbORAbsTable srcBackTable = (DbORAbsTable) destBackTable.getMatchingObject();
            if (srcFrontTable == destFrontTable.getSuperCopy()
                    && srcBackTable == destBackTable.getSuperCopy()) {
                if (AnySemObject.getSubCopy(srcAssoc, (DbSMSSemanticalObject) destAssoc
                        .getComposite()) == null)
                    destAssoc.setSuperCopy(srcAssoc);
            } else if (destFrontTable == srcFrontTable.getSuperCopy()
                    && destBackTable == srcBackTable.getSuperCopy()) {
                if (srcAssoc.getSuperCopy() == null)
                    srcAssoc.setSuperCopy(destAssoc);
            }
        }
    }

    // Set the superCopy link on a copied object and on all its descendants.
    private void setSuperCopy(DbSMSSemanticalObject srcObj, boolean srcIsSuper) throws DbException {
        DbSMSSemanticalObject destObj = (DbSMSSemanticalObject) srcObj.getMatchingObject();

        try {
            if (srcIsSuper)
                destObj.setSuperCopy(srcObj);
            else
                srcObj.setSuperCopy(destObj);
        } catch (Exception e) {
        };

        DbEnumeration dbEnum = srcObj.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            DbObject dbo = dbEnum.nextElement();
            if (AnySemObject.supportsSuper(dbo.getMetaClass()))
                setSuperCopy((DbSMSSemanticalObject) dbo, srcIsSuper);
        }
        dbEnum.close();
    }
}
