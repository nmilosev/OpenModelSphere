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
import org.modelsphere.jack.baseDb.util.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/or/db/DbORForeign.html">DbORForeign</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public final class DbORFKeyColumn extends DbObject {

    //Meta

    public static final MetaRelation1 fColumn = new MetaRelation1(LocaleMgr.db.getString("column"),
            1);
    public static final MetaRelation1 fSourceColumn = new MetaRelation1(LocaleMgr.db
            .getString("sourceColumn"), 1);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbORFKeyColumn"), DbORFKeyColumn.class, new MetaField[] { fColumn,
            fSourceColumn }, MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbObject.metaClass);
            metaClass.setIcon("dborcolumn.gif");

            fColumn.setJField(DbORFKeyColumn.class.getDeclaredField("m_column"));
            fColumn.setEditable(false);
            fSourceColumn.setJField(DbORFKeyColumn.class.getDeclaredField("m_sourceColumn"));
            fSourceColumn.setEditable(false);

            fColumn.setOppositeRel(DbORColumn.fFKeyColumns);
            fSourceColumn.setOppositeRel(DbORColumn.fDestFKeyColumns);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbORColumn m_column;
    DbORColumn m_sourceColumn;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORFKeyColumn() {
    }

    /**
     * Creates an instance of DbORFKeyColumn.
     * 
     * @param foreignkey
     *            org.modelsphere.sms.or.db.DbORForeign
     * @param column
     *            org.modelsphere.sms.or.db.DbORColumn
     * @param sourcecolumn
     *            org.modelsphere.sms.or.db.DbORColumn
     **/
    public DbORFKeyColumn(DbORForeign foreignKey, DbORColumn column, DbORColumn sourceColumn)
            throws DbException {
        super(foreignKey);
        setColumn(column);
        setSourceColumn(sourceColumn);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        DbORDataModel dataModel = (DbORDataModel) getCompositeOfType(DbORDataModel.metaClass);
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology term = terminologyUtil.findModelTerminology(dataModel);
        setName(term.getTerm(metaClass));

    }

    /**
     * @param form
     *            int
     * @return string
     **/
    public final String getSemanticalName(int form) throws DbException {
        return getColumn().getSemanticalName(form);
    }

    /**
     * @return string
     **/
    public final String getName() throws DbException {
        return getColumn().getName();
    }

    //Setters

    /**
     * Sets the column object associated to a DbORFKeyColumn's instance.
     * 
     * @param value
     *            the column object to be associated
     **/
    public final void setColumn(DbORColumn value) throws DbException {
        basicSet(fColumn, value);
    }

    /**
     * Sets the source column object associated to a DbORFKeyColumn's instance.
     * 
     * @param value
     *            the source column object to be associated
     **/
    public final void setSourceColumn(DbORColumn value) throws DbException {
        basicSet(fSourceColumn, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the column object associated to a DbORFKeyColumn's instance.
     * 
     * @return the column object
     **/
    public final DbORColumn getColumn() throws DbException {
        return (DbORColumn) get(fColumn);
    }

    /**
     * Gets the source column object associated to a DbORFKeyColumn's instance.
     * 
     * @return the source column object
     **/
    public final DbORColumn getSourceColumn() throws DbException {
        return (DbORColumn) get(fSourceColumn);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
