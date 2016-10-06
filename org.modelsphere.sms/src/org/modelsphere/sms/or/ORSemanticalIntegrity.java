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
package org.modelsphere.sms.or;

import java.util.HashMap;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.db.event.DbUpdateListener;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.db.DbSMSAbstractPackage;
import org.modelsphere.sms.db.DbSMSAssociationEnd;
import org.modelsphere.sms.db.DbSMSBuiltInTypePackage;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.oo.db.DbOODataMember;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORBuiltInType;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORConstraint;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORFKeyColumn;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbORIndexKey;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORView;
import org.modelsphere.sms.plugins.TargetSystemInfo;
import org.modelsphere.sms.plugins.TargetSystemManager;

public final class ORSemanticalIntegrity implements DbUpdateListener {
    //private static final String UNVALIDATED = "{0} Unvalidated";
    private static final String ENTITY_RELATIONSHIP_TXT = org.modelsphere.sms.or.international.LocaleMgr.misc
            .getString("EntityRelationship");

    public ORSemanticalIntegrity() {
        MetaField.addDbUpdateListener(this, 0, new MetaField[] { DbObject.fComposite,
                DbObject.fComponents, DbSemanticalObject.fName, DbSMSAssociationEnd.fMultiplicity,
                DbSMSAssociationEnd.fSpecificRangeMultiplicity,

                DbORTable.fIsAssociation, DbORColumn.fForeign, DbORPrimaryUnique.fColumns,
                DbORPrimaryUnique.fPrimary, DbORPrimaryUnique.fAssociationDependencies,
                DbORIndex.fConstraint, DbORIndexKey.fIndexedElement,

                DbORDomain.fPropagated,

                DbORDomain.fSourceType, DbORDomain.fLength, DbORDomain.fNbDecimal,
                DbORDomain.fNull, DbORDomain.fReference,

                DbORColumn.fType, DbORColumn.fLength, DbORColumn.fNbDecimal, DbORColumn.fNull,
                DbORColumn.fReference,

                DbORCommonItem.fType, DbORCommonItem.fLength, DbORCommonItem.fNbDecimal,
                DbORCommonItem.fNull,

                DbSMSTargetSystem.fName, DbSMSTargetSystem.fVersion,

                DbORBuiltInType.fName, });

    } //end ORSemanticalIntegrity()

    ////////////////////////////////
    // DbUpdateListener SUPPORT
    //
    public void dbUpdated(DbUpdateEvent event) throws DbException {
        if (event.metaField == DbObject.fComposite) {
            if (event.dbo instanceof DbORIndex && event.dbo.getTransStatus() == Db.OBJ_ADDED)
                updateIndexCols((DbORIndex) event.dbo);
        } else if (event.metaField == DbObject.fComponents) {
            if (event.dbo instanceof DbORIndex)
                updateIndexCols((DbORIndex) event.dbo);
            else if (event.dbo instanceof DbORForeign) {
                DbORIndex index = ((DbORForeign) event.dbo).getIndex();
                if (index != null)
                    updateIndexCols(index, (DbORForeign) event.dbo);
            }
        } else if (event.metaField == DbORTable.fIsAssociation) {
            DbORTable table = (DbORTable) event.dbo;
            updateTableName(table);
        } else if (event.metaField == DbORPrimaryUnique.fColumns) {
            DbORIndex index = ((DbORPrimaryUnique) event.dbo).getIndex();
            if (index != null)
                updateIndexCols(index, (DbORPrimaryUnique) event.dbo);
        } else if (event.metaField == DbORIndex.fConstraint) {
            updateIndexCols((DbORIndex) event.dbo);
        } else if (event.metaField == DbORIndexKey.fIndexedElement) {
            if (event.dbo.getTransStatus() != Db.OBJ_REMOVED)
                updateIndexCols((DbORIndex) event.dbo.getComposite());
        } else if (event.dbo instanceof DbSMSTargetSystem) {
            if (event.metaField == DbSMSTargetSystem.fName
                    || event.metaField == DbSMSTargetSystem.fVersion)
                updateBuiltInTypePackage((DbSMSTargetSystem) event.dbo);
        } else if (event.metaField == DbSemanticalObject.fName
                || event.metaField == DbSemanticalObject.fPhysicalName) {
            if ((event.dbo instanceof DbORAbsTable))
                updateRoleName(event);
            else if (event.dbo instanceof DbORBuiltInType)
                ((DbORBuiltInType) event.dbo).setPhysicalName((String) event.dbo.getName());
        }

        updateModelValidity(event);
        updateTableCalculatedFields(event);
        updateAssociationCalculatedFields(event);
        updateSpecificMultiplicity(event);

        if (event.dbo instanceof DbORDomain || event.dbo instanceof DbORColumn
                || event.dbo instanceof DbORCommonItem)
            updatePropagatedDomainValues(event);
    }

    //
    // End of DbUpdateListener SUPPORT
    //////////////////////////////////

    //
    // Private methods
    // 
    private void updateSpecificMultiplicity(DbUpdateEvent event) throws DbException {
        //if specific multiplicity changes
        if (event.metaField.equals(DbSMSAssociationEnd.fSpecificRangeMultiplicity)) {
            SMSMultiplicity mult = SMSMultiplicity.getInstance(SMSMultiplicity.SPECIFIC);
            event.dbo.set(DbSMSAssociationEnd.fMultiplicity, mult);
            //if multiplicity changes
        } else if (event.metaField.equals(DbSMSAssociationEnd.fMultiplicity)) {
            SMSMultiplicity mult = (SMSMultiplicity) event.dbo
                    .get(DbSMSAssociationEnd.fMultiplicity);

            if (mult.getValue() == SMSMultiplicity.SPECIFIC) {
                String spec = getSpecificMultiplicityDefault();
                event.dbo.set(DbSMSAssociationEnd.fSpecificRangeMultiplicity, spec);
            } else {
                event.dbo.set(DbSMSAssociationEnd.fSpecificRangeMultiplicity, "");
            }
        } //end if
    } //updateSpecificMultiplicity()

    private String getSpecificMultiplicityDefault() {
        String specific = SMSMultiplicity.getInstance(SMSMultiplicity.SPECIFIC)
                .getUMLMultiplicityLabel();
        return specific;
    }

    private void updateBuiltInTypePackage(DbSMSTargetSystem target) throws DbException {
        DbSMSBuiltInTypePackage pack = target.getBuiltInTypePackage();
        pack.setName(target.getName() + ' ' + target.getVersion());
        TargetSystemInfo tsInfo = TargetSystemManager.getSingleton().getTargetSystemInfo(target);
        tsInfo.setName(target.getName());
        tsInfo.setVersion(target.getVersion());
        ApplicationContext.getDefaultMainFrame().getExplorerPanel().getExplorer().refresh();
    }

    //if table is named 'entity', and if table is turned into an association table, 
    //  then rename it 'relationship' (and rename it back entity if no longer an association table) 
    private void updateTableName(DbORTable table) throws DbException {
        Terminology terminology = Terminology.findTerminologyByName(ENTITY_RELATIONSHIP_TXT);
        if (terminology == null)
            return;

        boolean isAssociationTable = table.isIsAssociation();
        String tablename = table.getName();
        MetaClass mc = isAssociationTable ? DbORTable.metaClass : DbORAssociation.metaClass;
        String term = terminology.getTerm(mc);
        if ((tablename != null) && (tablename.equals(term))) {
            mc = isAssociationTable ? DbORAssociation.metaClass : DbORTable.metaClass;
            String newTerm = terminology.getTerm(mc);
            table.setName(newTerm);
        } //end if
    } //end updateTableName()

    private void updateIndexCols(DbORIndex index) throws DbException {
        DbORConstraint pufKey = index.getConstraint();
        if (pufKey != null)
            updateIndexCols(index, pufKey);
    }

    // We have an index linked to a constraint; insure that the index has the same
    // composition as the constraint.
    private void updateIndexCols(DbORIndex index, DbORConstraint pufKey) throws DbException {
        if (index.matchesConstraint(pufKey))
            return;
        // it does not match; recreate the index keys from the constraint columns.
        int i;
        DbRelationN indCols = index.getComponents();
        HashMap old = new HashMap();
        for (i = indCols.size(); --i >= 0;) {
            DbObject dbo = indCols.elementAt(i);
            if (dbo instanceof DbORIndexKey) {
                if (((DbORIndexKey) dbo).getIndexedElement() != null)
                    old.put(((DbORIndexKey) dbo).getIndexedElement(), dbo);
                dbo.remove();
            }
        }
        DbRelationN pufCols = (pufKey instanceof DbORPrimaryUnique ? ((DbORPrimaryUnique) pufKey)
                .getColumns() : pufKey.getComponents());
        for (i = 0; i < pufCols.size(); i++) {
            DbORColumn pufCol = (pufKey instanceof DbORPrimaryUnique ? (DbORColumn) pufCols
                    .elementAt(i) : ((DbORFKeyColumn) pufCols.elementAt(i)).getColumn());
            DbORIndexKey indCol = new DbORIndexKey(index);
            indCol.setIndexedElement(pufCol);
            Object oldIndexKey = old.get(pufCol);
            if (oldIndexKey != null) {
                indCol.setSortOption(((DbORIndexKey) oldIndexKey).getSortOption());
                indCol.setExpression(((DbORIndexKey) oldIndexKey).getExpression());
            }
        }
        old.clear();
    }

    // This method updates roles and associations name to reflect changes on tables name.
    // For triggering the same effects on physical names, add the listener on
    // DbSemanticalObject.fName in the constructor
    private void updateRoleName(DbUpdateEvent event) throws DbException {

        DbORAbsTable table = (DbORAbsTable) event.dbo;
        String oldName = (String) table.get(event.metaField, Db.OLD_VALUE);
        String newName = (String) table.get(event.metaField, Db.NEW_VALUE);

        DbEnumeration dbEnum = table.getAssociationEnds().elements();
        while (dbEnum.hasMoreElements()) {
            DbORAssociationEnd end = ((DbORAssociationEnd) dbEnum.nextElement()).getOppositeEnd();
            String name = (String) end.get(event.metaField);
            if ((name != null && name.equals(oldName)) || (name == null && oldName == null))
                end.set(event.metaField, newName);
            DbORAssociation association = (DbORAssociation) end
                    .getCompositeOfType(DbORAssociation.metaClass);
            if (association == null)
                continue;
            else {
                //we must update the association in the explorer pane

                if (event.metaField == DbSemanticalObject.fName) {
                    DbUpdateEvent eventAss = new DbUpdateEvent(association, DbORAssociation.fName);
                    ApplicationContext.getDefaultMainFrame().getExplorerPanel().getExplorer()
                            .refreshAfterDbUpdate(eventAss);
                }
            }

            DbORAbsTable oppositeTable = end.getClassifier();
            if (oppositeTable == null)
                continue;
            String oppositeTableName = (String) oppositeTable.get(event.metaField);

            String oldAssociationName = null;
            if (association.getFrontEnd().getClassifier() == table)
                oldAssociationName = (oldName == null ? "" : oldName) + "-"
                        + (oppositeTableName == null ? "" : oppositeTableName);
            else
                oldAssociationName = (oppositeTableName == null ? "" : oppositeTableName) + "-"
                        + (oldName == null ? "" : oldName);

            String associationName = (String) association.get(event.metaField);
            if (oldAssociationName.equals(associationName)
                    || (associationName == null && oppositeTableName == null && oldName == null)) {
                String newAssociationName = null;
                if (association.getFrontEnd().getClassifier() == table)
                    newAssociationName = (newName == null ? "" : newName) + "-"
                            + (oppositeTableName == null ? "" : oppositeTableName);
                else
                    newAssociationName = (oppositeTableName == null ? "" : oppositeTableName) + "-"
                            + (newName == null ? "" : newName);
                association.set(event.metaField, newAssociationName);
            }
        }
        dbEnum.close();
    } //end updateRoleName()

    private void updateModelValidity(DbUpdateEvent event) throws DbException {
        DbObject obj = event.dbo;

        if (obj != null)
            if (obj.getTransStatus() == Db.OBJ_REMOVED)
                return;

        //model is no longer validated if model element is modified
        Db db = obj.getDb();
        db.beginWriteTrans("");
        DbORDataModel model = (DbORDataModel) obj.getCompositeOfType(DbORDataModel.metaClass);
        if (model != null)
            if (event.dbo instanceof DbSemanticalObject)
                model.setIsValidated(Boolean.FALSE);
        db.commitTrans();
    } //end updateModelValidity()

    /*
     * update the isAssociation and isDependent fields of a table:
     * 
     * is dependant table if at least one FK
     * 
     * is association table if : - it is connected to at least two other tables - all its columns
     * are part of FKs
     */
    private void updateTableCalculatedFields(DbUpdateEvent event) throws DbException {
        if ((event.dbo == null) || !(event.dbo instanceof DbORTable))
            return;

        if (event.op == Db.REMOVE_FROM_RELN)
            return;

        //if table is going to be deleted, return
        DbORTable table = (DbORTable) event.dbo;
        DbObject composite = table.getComposite();
        if (composite == null)
            return;

        //get diagram notation
        DbORDataModel dataModel = (DbORDataModel) table.getCompositeOfType(DbORDataModel.metaClass);
        //dataModel.get 

        //count number of associated tables
        int nbAssoc = 0;
        DbRelationN relN = table.getAssociationEnds();
        DbEnumeration dbEnum = relN.elements(DbORAssociationEnd.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORAssociationEnd assocEnd = (DbORAssociationEnd) dbEnum.nextElement();
            nbAssoc++;
        }
        dbEnum.close();

        //an association table must have at least two associations

        ////
        // this prevents the copy/paste of associations from working
        // also, in ERX, associations can have 0 or one or more arcs 

        /*
         * if (nbAssoc < 2) { table.setIsAssociation(Boolean.FALSE); return; }
         */

        //for each FK
        DbORAbsTable associatedTable = null;
        relN = table.getComponents();
        dbEnum = relN.elements(DbORForeign.metaClass);
        boolean hasAtLeastOneFK = false;
        boolean hasAtLeastTwoAssocTables = false;
        while (dbEnum.hasMoreElements()) {
            hasAtLeastOneFK = true;
            DbORForeign fk = (DbORForeign) dbEnum.nextElement();
            DbORAssociationEnd end = fk.getAssociationEnd();
            DbORAssociationEnd oppEnd = end.getOppositeEnd();
            DbORAbsTable oppTable = oppEnd.getClassifier();
            if (associatedTable == null) {
                associatedTable = oppTable;
            } else {
                if (!associatedTable.equals(oppTable)) {
                    hasAtLeastTwoAssocTables = true;
                    break;
                }
            } //end if
        } //end while   
        dbEnum.close();
        table.setIsDependant(hasAtLeastOneFK ? Boolean.TRUE : Boolean.FALSE);

        //a table containing one or more PUKs is not an association table 
        // mais seulement si cette clé est constituée de colonnes de cette table et non liée à une dépendance via l'AssociationEnd.
        int nbPKCol = 0;
        relN = table.getComponents();
        dbEnum = relN.elements(DbORPrimaryUnique.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORPrimaryUnique pk = (DbORPrimaryUnique) dbEnum.nextElement();
            nbPKCol = pk.getColumns().size();
            //int nbDependance = pk.getAssociationDependencies().size();
        }
        dbEnum.close();
        if (nbPKCol > 0) {
            table.setIsAssociation(Boolean.FALSE);
        }

        //a table without any value columns (non-FK columns) is an association table 
        boolean valueColumnFound = false;
        dbEnum = relN.elements(DbORColumn.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORColumn col = (DbORColumn) dbEnum.nextElement();
            boolean isForeign = col.isForeign();
            if (!isForeign) {
                valueColumnFound = true;
                break;
            }
        } //end while
        dbEnum.close();

    } //end updateTableCalculatedFields()

    private void updatePropagatedDomainValues(DbUpdateEvent event) throws DbException {

        ////
        // if a modification is made to one of the type attributes of domain
        // then update all linked columns and common items

        if (event.metaField.equals(DbORDomain.fPropagated)) {
            if (((DbORDomain) event.dbo).isPropagated() == true) {
                DbORDomain domain = (DbORDomain) event.dbo;
                DbEnumeration columns = domain.getTypedAttributes().elements(DbORColumn.metaClass);
                while (columns.hasMoreElements()) {
                    DbORColumn column = (DbORColumn) columns.nextElement();
                    column.setLength(domain.getLength());
                    column.setNbDecimal(domain.getNbDecimal());
                    column.setNull(Boolean.valueOf(domain.isNull()));
                    column.setReference(Boolean.valueOf(domain.isReference()));
                }
                columns.close();
                DbEnumeration domains = domain.getDomains().elements();
                while (domains.hasMoreElements()) {
                    DbORDomain targetDomain = (DbORDomain) domains.nextElement();
                    targetDomain.setLength(domain.getLength());
                    targetDomain.setNbDecimal(domain.getNbDecimal());
                    targetDomain.setNull(Boolean.valueOf(domain.isNull()));
                    targetDomain.setReference(Boolean.valueOf(domain.isReference()));
                }
                domains.close();
                DbEnumeration commonitems = domain.getCommonItems().elements();
                while (commonitems.hasMoreElements()) {
                    DbORCommonItem orCommonItem = (DbORCommonItem) commonitems.nextElement();
                    orCommonItem.setLength(domain.getLength());
                    orCommonItem.setNbDecimal(domain.getNbDecimal());
                    orCommonItem.setNull(Boolean.valueOf(domain.isNull()));
                }
                commonitems.close();
            }
        }
        if (event.metaField.equals(DbORDomain.fLength)) {
            if (((DbORDomain) event.dbo).isPropagated() == true) {
                DbORDomain domain = (DbORDomain) event.dbo;
                DbEnumeration columns = domain.getTypedAttributes().elements(DbORColumn.metaClass);
                while (columns.hasMoreElements()) {
                    DbORColumn column = (DbORColumn) columns.nextElement();
                    column.setLength(domain.getLength());
                }
                columns.close();
                DbEnumeration domains = domain.getDomains().elements();
                while (domains.hasMoreElements()) {
                    DbORDomain targetDomain = (DbORDomain) domains.nextElement();
                    targetDomain.setLength(domain.getLength());
                }
                domains.close();
                DbEnumeration commonitems = domain.getCommonItems().elements();
                while (commonitems.hasMoreElements()) {
                    DbORCommonItem orCommonItem = (DbORCommonItem) commonitems.nextElement();
                    orCommonItem.setLength(domain.getLength());
                }
                commonitems.close();
            }
        }
        if (event.metaField.equals(DbORDomain.fNbDecimal)) {
            if (((DbORDomain) event.dbo).isPropagated() == true) {
                DbORDomain domain = (DbORDomain) event.dbo;
                DbEnumeration columns = domain.getTypedAttributes().elements(DbORColumn.metaClass);
                while (columns.hasMoreElements()) {
                    DbORColumn column = (DbORColumn) columns.nextElement();
                    column.setNbDecimal(domain.getNbDecimal());
                }
                columns.close();
                DbEnumeration domains = domain.getDomains().elements();
                while (domains.hasMoreElements()) {
                    DbORDomain targetDomain = (DbORDomain) domains.nextElement();
                    targetDomain.setNbDecimal(domain.getNbDecimal());
                }
                domains.close();
                DbEnumeration commonitems = domain.getCommonItems().elements();
                while (commonitems.hasMoreElements()) {
                    DbORCommonItem orCommonItem = (DbORCommonItem) commonitems.nextElement();
                    orCommonItem.setNbDecimal(domain.getNbDecimal());
                }
                commonitems.close();
            }
        }
        if (event.metaField.equals(DbORDomain.fNull)) {
            if (((DbORDomain) event.dbo).isPropagated() == true) {
                DbORDomain domain = (DbORDomain) event.dbo;
                DbEnumeration columns = domain.getTypedAttributes().elements(DbORColumn.metaClass);
                while (columns.hasMoreElements()) {
                    DbORColumn column = (DbORColumn) columns.nextElement();
                    column.setNull(Boolean.valueOf(domain.isNull()));
                }
                columns.close();
                DbEnumeration domains = domain.getDomains().elements();
                while (domains.hasMoreElements()) {
                    DbORDomain targetDomain = (DbORDomain) domains.nextElement();
                    targetDomain.setNull(Boolean.valueOf(domain.isNull()));
                }
                domains.close();
                DbEnumeration commonitems = domain.getCommonItems().elements();
                while (commonitems.hasMoreElements()) {
                    DbORCommonItem orCommonItem = (DbORCommonItem) commonitems.nextElement();
                    orCommonItem.setNull(Boolean.valueOf(domain.isNull()));
                }
                commonitems.close();
            }
        }
        if (event.metaField.equals(DbORDomain.fReference)) {
            if (((DbORDomain) event.dbo).isPropagated() == true) {
                DbORDomain domain = (DbORDomain) event.dbo;
                DbEnumeration columns = domain.getTypedAttributes().elements(DbORColumn.metaClass);
                while (columns.hasMoreElements()) {
                    DbORColumn column = (DbORColumn) columns.nextElement();
                    column.setReference(Boolean.valueOf(domain.isReference()));
                }
                columns.close();
                DbEnumeration domains = domain.getDomains().elements();
                while (domains.hasMoreElements()) {
                    DbORDomain targetDomain = (DbORDomain) domains.nextElement();
                    targetDomain.setReference(Boolean.valueOf(domain.isReference()));
                }
                domains.close();
            }
        }

        ////
        // if a modification is made to one of the type attributes of column,
        // update the value from that of the domain

        else if (event.metaField.equals(DbORColumn.fType)) {
            DbObject dbo = ((DbORColumn) event.dbo).getType();
            if (dbo != null && dbo instanceof DbORDomain) {
                DbORDomain domain = (DbORDomain) dbo;
                if (domain.isPropagated() == true) {
                    event.dbo.set(DbORColumn.fLength, domain.get(DbORDomain.fLength));
                    event.dbo.set(DbORColumn.fNbDecimal, domain.get(DbORDomain.fNbDecimal));
                    event.dbo.set(DbORColumn.fNull, domain.get(DbORDomain.fNull));
                    event.dbo.set(DbORColumn.fReference, domain.get(DbORDomain.fReference));
                }
            }
        } else if (event.metaField.equals(DbORColumn.fLength)) {
            DbObject dbo = ((DbORColumn) event.dbo).getType();
            if (dbo != null && dbo instanceof DbORDomain) {
                DbORDomain domain = (DbORDomain) dbo;
                if (domain.isPropagated() == true)
                    event.dbo.set(DbORColumn.fLength, domain.get(DbORDomain.fLength));
            }
        } else if (event.metaField.equals(DbORColumn.fNbDecimal)) {
            DbObject dbo = ((DbORColumn) event.dbo).getType();
            if (dbo != null && dbo instanceof DbORDomain) {
                DbORDomain domain = (DbORDomain) dbo;
                if (domain.isPropagated() == true)
                    event.dbo.set(DbORColumn.fNbDecimal, domain.get(DbORDomain.fNbDecimal));
            }
        } else if (event.metaField.equals(DbORColumn.fNull)) {
            DbObject dbo = ((DbORColumn) event.dbo).getType();
            if (dbo != null && dbo instanceof DbORDomain) {
                DbORDomain domain = (DbORDomain) dbo;
                if (domain.isPropagated() == true)
                    event.dbo.set(DbORColumn.fNull, domain.get(DbORDomain.fNull));
            }
        } else if (event.metaField.equals(DbORColumn.fReference)) {
            DbObject dbo = ((DbORColumn) event.dbo).getType();
            if (dbo != null && dbo instanceof DbORDomain) {
                DbORDomain domain = (DbORDomain) dbo;
                if (domain.isPropagated() == true)
                    event.dbo.set(DbORColumn.fReference, domain.get(DbORDomain.fReference));
            }
        }

        ////
        // if a modification is made to one of the type attributes of common item,
        // update the value from that of the domain

        else if (event.metaField.equals(DbORCommonItem.fType)) {
            DbObject dbo = ((DbORCommonItem) event.dbo).getType();
            if (dbo != null && dbo instanceof DbORDomain) {
                DbORDomain domain = (DbORDomain) dbo;
                if (domain.isPropagated() == true) {
                    event.dbo.set(DbORCommonItem.fLength, domain.get(DbORDomain.fLength));
                    event.dbo.set(DbORCommonItem.fNbDecimal, domain.get(DbORDomain.fNbDecimal));
                    event.dbo.set(DbORCommonItem.fNull, domain.get(DbORDomain.fNull));
                }
            }
        } else if (event.metaField.equals(DbORCommonItem.fLength)) {
            DbObject dbo = ((DbORCommonItem) event.dbo).getType();
            if (dbo != null && dbo instanceof DbORDomain) {
                DbORDomain domain = (DbORDomain) dbo;
                if (domain.isPropagated() == true)
                    event.dbo.set(DbORCommonItem.fLength, domain.get(DbORDomain.fLength));
            }
        } else if (event.metaField.equals(DbORCommonItem.fNbDecimal)) {
            DbObject dbo = ((DbORCommonItem) event.dbo).getType();
            if (dbo != null && dbo instanceof DbORDomain) {
                DbORDomain domain = (DbORDomain) dbo;
                if (domain.isPropagated() == true)
                    event.dbo.set(DbORCommonItem.fNbDecimal, domain.get(DbORDomain.fNbDecimal));
            }
        } else if (event.metaField.equals(DbORCommonItem.fNull)) {
            DbObject dbo = ((DbORCommonItem) event.dbo).getType();
            if (dbo != null && dbo instanceof DbORDomain) {
                DbORDomain domain = (DbORDomain) dbo;
                if (domain.isPropagated() == true)
                    event.dbo.set(DbORCommonItem.fNull, domain.get(DbORDomain.fNull));
            }
        }
    }

    private void updateAssociationCalculatedFields(DbUpdateEvent event) throws DbException {
        if (event.dbo == null)
            return;

        boolean isIdentifying = false;

        if (event.dbo instanceof DbORPrimaryUnique) {
            DbORPrimaryUnique pk = (DbORPrimaryUnique) event.dbo;
            if (pk.isPrimary()) {
                DbRelationN relN = null;
                DbORAbsTable table = (DbORAbsTable) pk.getCompositeOfType(DbORAbsTable.metaClass);
                if (table != null) {
                    relN = table.getAssociationEnds();
                } else {
                    DbORView view = (DbORView) pk.getCompositeOfType(DbORView.metaClass);
                    if (view != null)
                        relN = view.getAssociationEnds();
                }

                if (relN != null) {
                    DbEnumeration dbEnum = relN.elements(DbORAssociationEnd.metaClass);
                    while (dbEnum.hasMoreElements()) {
                        DbORAssociationEnd assocEnd = (DbORAssociationEnd) dbEnum.nextElement();
                        DbORAssociation assoc = (DbORAssociation) assocEnd
                                .getCompositeOfType(DbORAssociation.metaClass);
                        assoc.setIsIdentifying(Boolean.FALSE);

                        DbORAssociationEnd backEnd = assoc.getBackEnd();
                        DbRelationN relN2 = backEnd.getDependentConstraints();
                        DbEnumeration enum2 = relN2.elements(DbORPrimaryUnique.metaClass);
                        if (enum2.hasMoreElements())
                            assoc.setIsIdentifying(Boolean.TRUE);

                        enum2.close();
                        DbORAssociationEnd frontEnd = assoc.getFrontEnd();
                        relN2 = frontEnd.getDependentConstraints();
                        enum2 = relN2.elements(DbORPrimaryUnique.metaClass);
                        if (enum2.hasMoreElements())
                            assoc.setIsIdentifying(Boolean.TRUE);

                        enum2.close();
                    } //end while
                    dbEnum.close();
                }

                /*
                 * relN = pk.getAssociationDependencies(); dbEnum =
                 * relN.elements(DbORAssociationEnd.metaClass); while (dbEnum.hasMoreElements()) {
                 * DbORAssociationEnd assocEnd = (DbORAssociationEnd)dbEnum.nextElement();
                 * DbORAssociation assoc =
                 * (DbORAssociation)assocEnd.getCompositeOfType(DbORAssociation.metaClass);
                 * assoc.setIsIdentifying(Boolean.TRUE); } //end while dbEnum.close();
                 */

            }
        } //end if
    } //end updateAssociationCalculatedFields()

    /*
     * update the isIdentifying field of an association :
     * 
     * is identifying association if : - if the association-end column of the child table is part of
     * the PK // private void updateAssociationCalculatedFields(DbUpdateEvent event) throws
     * DbException { if (event.dbo == null) return;
     * 
     * DbORAssociation assoc = null; boolean isIdentifying = false;
     * 
     * if (event.dbo instanceof DbORFKeyColumn) { DbORFKeyColumn fkCol = (DbORFKeyColumn)event.dbo;
     * DbORColumn col = fkCol.getSourceColumn(); if (col != null) { DbRelationN relN =
     * col.getPrimaryUniques(); DbEnumeration dbEnum = relN.elements(DbORPrimaryUnique.metaClass);
     * while (dbEnum.hasMoreElements()) { DbORPrimaryUnique puk =
     * (DbORPrimaryUnique)dbEnum.nextElement(); if (puk.isPrimary()) { isIdentifying = true; break;
     * } } //end while dbEnum.close();
     * 
     * DbORForeign fk = (DbORForeign)fkCol.getCompositeOfType(DbORForeign.metaClass);
     * DbORAssociationEnd end = fk.getAssociationEnd(); assoc =
     * (DbORAssociation)end.getCompositeOfType(DbORAssociation.metaClass);
     * assoc.setIsIdentifying(isIdentifying ? Boolean.TRUE : Boolean.FALSE); } //end if } else if
     * (event.dbo instanceof DbORPrimaryUnique) { DbORPrimaryUnique puk =
     * (DbORPrimaryUnique)event.dbo; isIdentifying = puk.isPrimary(); DbRelationN relN =
     * puk.getAssociationReferences(); DbEnumeration dbEnum =
     * relN.elements(DbORAssociationEnd.metaClass); while (dbEnum.hasMoreElements()) {
     * DbORAssociationEnd end = (DbORAssociationEnd)dbEnum.nextElement(); assoc =
     * (DbORAssociation)end.getCompositeOfType(DbORAssociation.metaClass);
     * assoc.setIsIdentifying(isIdentifying ? Boolean.TRUE : Boolean.FALSE); } //end while
     * dbEnum.close(); } //end if
     */
    /*
     * //get around for a NullPointerException in assoc.getBackEnd(); DbRelationN relN =
     * assoc.getComponents(); int size = relN.size(); if (size < 2) { return; }
     * 
     * DbORAssociationEnd assocEnd = assoc.getBackEnd(); DbORPrimaryUnique puk =
     * assocEnd.getReferencedConstraint(); if ((puk != null) && puk.isPrimary()) { isIdentifying =
     * true; } //end if
     * 
     * assocEnd = assoc.getFrontEnd(); puk = assocEnd.getReferencedConstraint(); if ((puk != null)
     * && puk.isPrimary()) { isIdentifying = true; } //end if
     * 
     * assoc.setIsIdentifying(isIdentifying ? Boolean.TRUE : Boolean.FALSE);
     */
    // //end updateAssociationCalculatedFields()
} //end ORSemanticalIntegrity

