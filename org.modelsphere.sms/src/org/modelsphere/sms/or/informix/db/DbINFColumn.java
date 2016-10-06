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
package org.modelsphere.sms.or.informix.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.informix.db.srtypes.*;
import org.modelsphere.sms.or.informix.international.LocaleMgr;
import org.modelsphere.sms.or.db.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.jack.baseDb.util.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFTable.html">DbINFTable</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFView.html">DbINFView</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbINFColumn extends DbORColumn {

    //Meta

    public static final MetaRelationN fSbspaces = new MetaRelationN(LocaleMgr.db
            .getString("sbspaces"), 0, MetaRelationN.cardN);
    public static final MetaRelation1 fFragmentationKeyTable = new MetaRelation1(LocaleMgr.db
            .getString("fragmentationKeyTable"), 0);
    public static final MetaRelationN fFragmentationKeyIndexes = new MetaRelationN(LocaleMgr.db
            .getString("fragmentationKeyIndexes"), 0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbINFColumn"),
            DbINFColumn.class, new MetaField[] { fSbspaces, fFragmentationKeyTable,
                    fFragmentationKeyIndexes }, MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORColumn.metaClass);

            fSbspaces.setJField(DbINFColumn.class.getDeclaredField("m_sbspaces"));
            fSbspaces.setFlags(MetaField.INTEGRABLE | MetaField.WRITE_CHECK);
            fFragmentationKeyTable.setJField(DbINFColumn.class
                    .getDeclaredField("m_fragmentationKeyTable"));
            fFragmentationKeyTable.setVisibleInScreen(false);
            fFragmentationKeyIndexes.setJField(DbINFColumn.class
                    .getDeclaredField("m_fragmentationKeyIndexes"));

            fSbspaces.setOppositeRel(DbINFSbspace.fLobColumns);
            fFragmentationKeyTable.setOppositeRel(DbINFTable.fFragmentationKeyColumns);
            fFragmentationKeyIndexes.setOppositeRel(DbINFIndex.fFragmentationKeyColumns);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbRelationN m_sbspaces;
    DbINFTable m_fragmentationKeyTable;
    DbRelationN m_fragmentationKeyIndexes;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbINFColumn() {
    }

    /**
     * Creates an instance of DbINFColumn.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbINFColumn(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        DbORDataModel dataModel = (DbORDataModel) getCompositeOfType(DbORDataModel.metaClass);
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology term = terminologyUtil.findModelTerminology(dataModel);
        setName(term.getTerm(metaClass));
    }

    //Setters

    /**
     * Adds an element to or removes an element from the list of sbspaces associated to a
     * DbINFColumn's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setSbspaces(DbINFSbspace value, int op) throws DbException {
        setRelationNN(fSbspaces, value, op);
    }

    /**
     * Adds an element to the list of sbspaces associated to a DbINFColumn's instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToSbspaces(DbINFSbspace value) throws DbException {
        setSbspaces(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of sbspaces associated to a DbINFColumn's instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromSbspaces(DbINFSbspace value) throws DbException {
        setSbspaces(value, Db.REMOVE_FROM_RELN);
    }

    /**
     * Sets the table object associated to a DbINFColumn's instance.
     * 
     * @param value
     *            the table object to be associated
     **/
    public final void setFragmentationKeyTable(DbINFTable value) throws DbException {
        basicSet(fFragmentationKeyTable, value);
    }

    /**
     * Adds an element to or removes an element from the list of indexes associated to a
     * DbINFColumn's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setFragmentationKeyIndexes(DbINFIndex value, int op) throws DbException {
        setRelationNN(fFragmentationKeyIndexes, value, op);
    }

    /**
     * Adds an element to the list of indexes associated to a DbINFColumn's instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToFragmentationKeyIndexes(DbINFIndex value) throws DbException {
        setFragmentationKeyIndexes(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of indexes associated to a DbINFColumn's instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromFragmentationKeyIndexes(DbINFIndex value) throws DbException {
        setFragmentationKeyIndexes(value, Db.REMOVE_FROM_RELN);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fSbspaces)
                setSbspaces((DbINFSbspace) value, Db.ADD_TO_RELN);
            else if (metaField == fFragmentationKeyIndexes)
                setFragmentationKeyIndexes((DbINFIndex) value, Db.ADD_TO_RELN);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        if (relation == fSbspaces)
            setSbspaces((DbINFSbspace) neighbor, op);
        else if (relation == fFragmentationKeyIndexes)
            setFragmentationKeyIndexes((DbINFIndex) neighbor, op);
        else
            super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the list of sbspaces associated to a DbINFColumn's instance.
     * 
     * @return the list of sbspaces.
     **/
    public final DbRelationN getSbspaces() throws DbException {
        return (DbRelationN) get(fSbspaces);
    }

    /**
     * Gets the table object associated to a DbINFColumn's instance.
     * 
     * @return the table object
     **/
    public final DbINFTable getFragmentationKeyTable() throws DbException {
        return (DbINFTable) get(fFragmentationKeyTable);
    }

    /**
     * Gets the list of indexes associated to a DbINFColumn's instance.
     * 
     * @return the list of indexes.
     **/
    public final DbRelationN getFragmentationKeyIndexes() throws DbException {
        return (DbRelationN) get(fFragmentationKeyIndexes);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
