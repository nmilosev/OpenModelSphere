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
package org.modelsphere.sms.be.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.be.db.srtypes.*;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.SMSFilter;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEDiagram.html">DbBEDiagram</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public final class DbBEStoreGo extends DbSMSClassifierGo {

    //Meta

    public static final MetaField fQualifiersOffset = new MetaField(LocaleMgr.db
            .getString("qualifiersOffset"));
    public static final MetaRelation1 fCell = new MetaRelation1(LocaleMgr.db.getString("cell"), 0);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbBEStoreGo"),
            DbBEStoreGo.class, new MetaField[] { fQualifiersOffset, fCell }, MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSClassifierGo.metaClass);

            fQualifiersOffset.setJField(DbBEStoreGo.class.getDeclaredField("m_qualifiersOffset"));
            fCell.setJField(DbBEStoreGo.class.getDeclaredField("m_cell"));

            fCell.setOppositeRel(DbBEContextCell.fStoreGos);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    SrPoint m_qualifiersOffset;
    DbBEContextCell m_cell;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbBEStoreGo() {
    }

    /**
     * Creates an instance of DbBEStoreGo.
     * 
     * @param composite
     *            org.modelsphere.sms.db.DbSMSDiagram
     * @param classifier
     *            org.modelsphere.sms.db.DbSMSClassifier
     **/
    public DbBEStoreGo(DbSMSDiagram composite, DbSMSClassifier classifier) throws DbException {
        super(composite, classifier);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setAutoFit(Boolean.FALSE);
        DbBEDiagram diag = (DbBEDiagram) getCompositeOfType(DbBEDiagram.metaClass);
        DbBENotation notation = diag.findNotation();
        Rectangle rect = getRectangle(); // Default rectangle
        if (rect == null)
            return;
        Rectangle newRect = new Rectangle(rect);
        newRect.setSize(notation.getStoreDefaultWidth().intValue(), notation
                .getStoreDefaultHeight().intValue());
        setRectangle(newRect);
    }

    /**
     * @return int
     **/
    protected final int getFeatureSet() {
        return SMSFilter.BPM;

    }

    //Setters

    /**
     * Sets the "qualifiers offset" property of a DbBEStoreGo's instance.
     * 
     * @param value
     *            the "qualifiers offset" property
     **/
    public final void setQualifiersOffset(Point value) throws DbException {
        basicSet(fQualifiersOffset, value);
    }

    /**
     * Sets the cell object associated to a DbBEStoreGo's instance.
     * 
     * @param value
     *            the cell object to be associated
     **/
    public final void setCell(DbBEContextCell value) throws DbException {
        basicSet(fCell, value);
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
     * Gets the "qualifiers offset" of a DbBEStoreGo's instance.
     * 
     * @return the "qualifiers offset"
     **/
    public final Point getQualifiersOffset() throws DbException {
        return (Point) get(fQualifiersOffset);
    }

    /**
     * Gets the cell object associated to a DbBEStoreGo's instance.
     * 
     * @return the cell object
     **/
    public final DbBEContextCell getCell() throws DbException {
        return (DbBEContextCell) get(fCell);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
