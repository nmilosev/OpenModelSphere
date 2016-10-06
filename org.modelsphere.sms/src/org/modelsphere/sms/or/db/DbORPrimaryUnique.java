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
package org.modelsphere.sms.or.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import javax.swing.Icon;
import org.modelsphere.jack.baseDb.util.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../../org/modelsphere/sms/or/oracle/db/DbORAPrimaryUnique.html"
 * >DbORAPrimaryUnique</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/ibm/db/DbIBMPrimaryUnique.html"
 * >DbIBMPrimaryUnique</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/informix/db/DbINFPrimaryUnique.html"
 * >DbINFPrimaryUnique</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/generic/db/DbGEPrimaryUnique.html"
 * >DbGEPrimaryUnique</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbORPrimaryUnique extends DbORConstraint {

    //Meta

    public static final MetaField fPrimary = new MetaField(LocaleMgr.db.getString("primary"));
    public static final MetaRelationN fAssociationDependencies = new MetaRelationN(LocaleMgr.db
            .getString("associationDependencies"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fAssociationReferences = new MetaRelationN(LocaleMgr.db
            .getString("associationReferences"), 0, MetaRelationN.cardN);
    public static final MetaRelation1 fIndex = new MetaRelation1(LocaleMgr.db.getString("index"), 0);
    public static final MetaRelationN fColumns = new MetaRelationN(LocaleMgr.db
            .getString("columns"), 0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbORPrimaryUnique"), DbORPrimaryUnique.class, new MetaField[] { fPrimary,
            fAssociationDependencies, fAssociationReferences, fIndex, fColumns },
            MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORConstraint.metaClass);
            metaClass.setIcon("dborprimary.gif");

            fPrimary.setJField(DbORPrimaryUnique.class.getDeclaredField("m_primary"));
            fAssociationDependencies.setJField(DbORPrimaryUnique.class
                    .getDeclaredField("m_associationDependencies"));
            fAssociationDependencies.setFlags(MetaField.INTEGRABLE | MetaField.WRITE_CHECK);
            fAssociationReferences.setJField(DbORPrimaryUnique.class
                    .getDeclaredField("m_associationReferences"));
            fIndex.setJField(DbORPrimaryUnique.class.getDeclaredField("m_index"));
            fIndex.setRendererPluginName("DbORIndexConstraint");
            fColumns.setJField(DbORPrimaryUnique.class.getDeclaredField("m_columns"));
            fColumns.setFlags(MetaField.INTEGRABLE | MetaField.WRITE_CHECK);

            fAssociationDependencies.setOppositeRel(DbORAssociationEnd.fDependentConstraints);
            fColumns.setOppositeRel(DbORColumn.fPrimaryUniques);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;
    private static Icon primaryKeyIcon = org.modelsphere.jack.graphic.GraphicUtil.loadIcon(
            DbORPrimaryUnique.class, "resources/dborprimary.gif");
    private static Icon uniqueKeyIcon = org.modelsphere.jack.graphic.GraphicUtil.loadIcon(
            DbORPrimaryUnique.class, "resources/dborunique.gif");

    //Instance variables
    boolean m_primary;
    DbRelationN m_associationDependencies;
    DbRelationN m_associationReferences;
    DbORIndex m_index;
    DbRelationN m_columns;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORPrimaryUnique() {
    }

    /**
     * Creates an instance of DbORPrimaryUnique.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORPrimaryUnique(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setPrimary(Boolean.TRUE);
        DbORDataModel dataModel = (DbORDataModel) getCompositeOfType(DbORDataModel.metaClass);
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology term = terminologyUtil.findModelTerminology(dataModel);
        setName(term.getTerm(metaClass));
    }

    /**
     * @param form
     *            int
     * @return icon
     **/
    public final Icon getSemanticalIcon(int form) throws DbException {
        if (isPrimary())
            return primaryKeyIcon;
        else
            return uniqueKeyIcon;

    }

    //Setters

    /**
     * Sets the "primary" property of a DbORPrimaryUnique's instance.
     * 
     * @param value
     *            the "primary" property
     **/
    public final void setPrimary(Boolean value) throws DbException {
        basicSet(fPrimary, value);
    }

    /**
     * Adds an element to or removes an element from the list of dependencies associated to a
     * DbORPrimaryUnique's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setAssociationDependencies(DbORAssociationEnd value, int op)
            throws DbException {
        setRelationNN(fAssociationDependencies, value, op);
    }

    /**
     * Adds an element to the list of dependencies associated to a DbORPrimaryUnique's instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToAssociationDependencies(DbORAssociationEnd value) throws DbException {
        setAssociationDependencies(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of dependencies associated to a DbORPrimaryUnique's
     * instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromAssociationDependencies(DbORAssociationEnd value)
            throws DbException {
        setAssociationDependencies(value, Db.REMOVE_FROM_RELN);
    }

    /**
     * Sets the index object associated to a DbORPrimaryUnique's instance.
     * 
     * @param value
     *            the index object to be associated
     **/
    public final void setIndex(DbORIndex value) throws DbException {
        basicSet(fIndex, value);
    }

    /**
     * Adds an element to or removes an element from the list of columns associated to a
     * DbORPrimaryUnique's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setColumns(DbORColumn value, int op) throws DbException {
        setRelationNN(fColumns, value, op);
    }

    /**
     * Adds an element to the list of columns associated to a DbORPrimaryUnique's instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToColumns(DbORColumn value) throws DbException {
        setColumns(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of columns associated to a DbORPrimaryUnique's instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromColumns(DbORColumn value) throws DbException {
        setColumns(value, Db.REMOVE_FROM_RELN);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fAssociationDependencies)
                setAssociationDependencies((DbORAssociationEnd) value, Db.ADD_TO_RELN);
            else if (metaField == fAssociationReferences)
                ((DbORAssociationEnd) value).setReferencedConstraint(this);
            else if (metaField == fColumns)
                setColumns((DbORColumn) value, Db.ADD_TO_RELN);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        if (relation == fAssociationDependencies)
            setAssociationDependencies((DbORAssociationEnd) neighbor, op);
        else if (relation == fColumns)
            setColumns((DbORColumn) neighbor, op);
        else
            super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the "primary" property's Boolean value of a DbORPrimaryUnique's instance.
     * 
     * @return the "primary" property's Boolean value
     * @deprecated use isPrimary() method instead
     **/
    public final Boolean getPrimary() throws DbException {
        return (Boolean) get(fPrimary);
    }

    /**
     * Tells whether a DbORPrimaryUnique's instance is primary or not.
     * 
     * @return boolean
     **/
    public final boolean isPrimary() throws DbException {
        return getPrimary().booleanValue();
    }

    /**
     * Gets the list of dependencies associated to a DbORPrimaryUnique's instance.
     * 
     * @return the list of dependencies.
     **/
    public final DbRelationN getAssociationDependencies() throws DbException {
        return (DbRelationN) get(fAssociationDependencies);
    }

    /**
     * Gets the list of referenced bys associated to a DbORPrimaryUnique's instance.
     * 
     * @return the list of referenced bys.
     **/
    public final DbRelationN getAssociationReferences() throws DbException {
        return (DbRelationN) get(fAssociationReferences);
    }

    /**
     * Gets the index object associated to a DbORPrimaryUnique's instance.
     * 
     * @return the index object
     **/
    public final DbORIndex getIndex() throws DbException {
        return (DbORIndex) get(fIndex);
    }

    /**
     * Gets the list of columns associated to a DbORPrimaryUnique's instance.
     * 
     * @return the list of columns.
     **/
    public final DbRelationN getColumns() throws DbException {
        return (DbRelationN) get(fColumns);
    }

}
