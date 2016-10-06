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

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEContextGo.html">DbBEContextGo</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public final class DbBEContextCell extends DbObject {

    //Meta

    public static final MetaField fX = new MetaField(LocaleMgr.db.getString("x"));
    public static final MetaField fY = new MetaField(LocaleMgr.db.getString("y"));
    public static final MetaField fDescription = new MetaField(LocaleMgr.db
            .getString("description"));
    public static final MetaField fFont = new MetaField(LocaleMgr.db.getString("font"));
    public static final MetaField fRightBorder = new MetaField(LocaleMgr.db
            .getString("rightBorder"));
    public static final MetaField fBottomBorder = new MetaField(LocaleMgr.db
            .getString("bottomBorder"));
    public static final MetaField fVerticalJustification2 = new MetaField(LocaleMgr.db
            .getString("verticalJustification2"));
    public static final MetaField fHorizontalJustification2 = new MetaField(LocaleMgr.db
            .getString("horizontalJustification2"));
    public static final MetaField fXweight = new MetaField(LocaleMgr.db.getString("xweight"));
    public static final MetaField fYweight = new MetaField(LocaleMgr.db.getString("yweight"));
    public static final MetaRelationN fUsecaseGos = new MetaRelationN(LocaleMgr.db
            .getString("usecaseGos"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fStoreGos = new MetaRelationN(LocaleMgr.db
            .getString("storeGos"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fActorGos = new MetaRelationN(LocaleMgr.db
            .getString("actorGos"), 0, MetaRelationN.cardN);
    public static final MetaField fVerticalJustification = new MetaField(LocaleMgr.db
            .getString("verticalJustification"));
    public static final MetaField fHorizontalJustification = new MetaField(LocaleMgr.db
            .getString("horizontalJustification"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbBEContextCell"), DbBEContextCell.class, new MetaField[] { fX, fY,
            fDescription, fFont, fRightBorder, fBottomBorder, fVerticalJustification2,
            fHorizontalJustification2, fXweight, fYweight, fUsecaseGos, fStoreGos, fActorGos,
            fVerticalJustification, fHorizontalJustification }, MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbObject.metaClass);
            metaClass.setIcon("dbbecontextcell.gif");

            fX.setJField(DbBEContextCell.class.getDeclaredField("m_x"));
            fY.setJField(DbBEContextCell.class.getDeclaredField("m_y"));
            fDescription.setJField(DbBEContextCell.class.getDeclaredField("m_description"));
            fFont.setJField(DbBEContextCell.class.getDeclaredField("m_font"));
            //fFont.setVisibleInScreen(false); 
            fRightBorder.setJField(DbBEContextCell.class.getDeclaredField("m_rightBorder"));
            fBottomBorder.setJField(DbBEContextCell.class.getDeclaredField("m_bottomBorder"));
            fVerticalJustification2.setJField(DbBEContextCell.class
                    .getDeclaredField("m_verticalJustification2"));
            fHorizontalJustification2.setJField(DbBEContextCell.class
                    .getDeclaredField("m_horizontalJustification2"));
            fXweight.setJField(DbBEContextCell.class.getDeclaredField("m_xweight"));
            fYweight.setJField(DbBEContextCell.class.getDeclaredField("m_yweight"));
            fUsecaseGos.setJField(DbBEContextCell.class.getDeclaredField("m_usecaseGos"));
            fStoreGos.setJField(DbBEContextCell.class.getDeclaredField("m_storeGos"));
            fActorGos.setJField(DbBEContextCell.class.getDeclaredField("m_actorGos"));
            fVerticalJustification.setJField(DbBEContextCell.class
                    .getDeclaredField("m_verticalJustification"));
            fVerticalJustification.setVisibleInScreen(false);
            fHorizontalJustification.setJField(DbBEContextCell.class
                    .getDeclaredField("m_horizontalJustification"));
            fHorizontalJustification.setVisibleInScreen(false);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    int m_x;
    int m_y;
    String m_description;
    SrFont m_font;
    boolean m_rightBorder;
    boolean m_bottomBorder;
    SMSVerticalAlignment m_verticalJustification2;
    SMSHorizontalAlignment m_horizontalJustification2;
    double m_xweight;
    double m_yweight;
    DbRelationN m_usecaseGos;
    DbRelationN m_storeGos;
    DbRelationN m_actorGos;
    int m_verticalJustification;
    int m_horizontalJustification;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbBEContextCell() {
    }

    /**
     * Creates an instance of DbBEContextCell.
     * 
     * @param composite
     *            org.modelsphere.jack.baseDb.db.DbObject
     * @param x
     *            int
     * @param y
     *            int
     **/
    public DbBEContextCell(DbObject composite, int x, int y) throws DbException {
        super(composite);
        setX(new Integer(x));
        setY(new Integer(y));
        org.modelsphere.sms.db.util.DbInitialization.initCellValues(this);
    }

    private void setDefaultInitialValues() throws DbException {
    }

    //Setters

    /**
     * Sets the "column" property of a DbBEContextCell's instance.
     * 
     * @param value
     *            the "column" property
     **/
    public final void setX(Integer value) throws DbException {
        basicSet(fX, value);
    }

    /**
     * Sets the "row" property of a DbBEContextCell's instance.
     * 
     * @param value
     *            the "row" property
     **/
    public final void setY(Integer value) throws DbException {
        basicSet(fY, value);
    }

    /**
     * Sets the "description" property of a DbBEContextCell's instance.
     * 
     * @param value
     *            the "description" property
     **/
    public final void setDescription(String value) throws DbException {
        basicSet(fDescription, value);
    }

    /**
     * Sets the "font" property of a DbBEContextCell's instance.
     * 
     * @param value
     *            the "font" property
     **/
    public final void setFont(Font value) throws DbException {
        basicSet(fFont, value);
    }

    /**
     * Sets the "right border" property of a DbBEContextCell's instance.
     * 
     * @param value
     *            the "right border" property
     **/
    public final void setRightBorder(Boolean value) throws DbException {
        basicSet(fRightBorder, value);
    }

    /**
     * Sets the "bottom border" property of a DbBEContextCell's instance.
     * 
     * @param value
     *            the "bottom border" property
     **/
    public final void setBottomBorder(Boolean value) throws DbException {
        basicSet(fBottomBorder, value);
    }

    /**
     * Sets the "vertical alignment" property of a DbBEContextCell's instance.
     * 
     * @param value
     *            the "vertical alignment" property
     **/
    public final void setVerticalJustification2(SMSVerticalAlignment value) throws DbException {
        basicSet(fVerticalJustification2, value);
    }

    /**
     * Sets the "horizontal alignment" property of a DbBEContextCell's instance.
     * 
     * @param value
     *            the "horizontal alignment" property
     **/
    public final void setHorizontalJustification2(SMSHorizontalAlignment value) throws DbException {
        basicSet(fHorizontalJustification2, value);
    }

    /**
     * Sets the "x weight" property of a DbBEContextCell's instance.
     * 
     * @param value
     *            the "x weight" property
     **/
    public final void setXweight(Double value) throws DbException {
        basicSet(fXweight, value);
    }

    /**
     * Sets the "y weight" property of a DbBEContextCell's instance.
     * 
     * @param value
     *            the "y weight" property
     **/
    public final void setYweight(Double value) throws DbException {
        basicSet(fYweight, value);
    }

    /**
     * Sets the "vertical alignment" property of a DbBEContextCell's instance.
     * 
     * @param value
     *            the "vertical alignment" property
     **/
    public final void setVerticalJustification(Integer value) throws DbException {
        basicSet(fVerticalJustification, value);
    }

    /**
     * Sets the "horizontal alignment" property of a DbBEContextCell's instance.
     * 
     * @param value
     *            the "horizontal alignment" property
     **/
    public final void setHorizontalJustification(Integer value) throws DbException {
        basicSet(fHorizontalJustification, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fUsecaseGos)
                ((DbBEUseCaseGo) value).setCell(this);
            else if (metaField == fStoreGos)
                ((DbBEStoreGo) value).setCell(this);
            else if (metaField == fActorGos)
                ((DbBEActorGo) value).setCell(this);
            else
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
     * Gets the "column" property of a DbBEContextCell's instance.
     * 
     * @return the "column" property
     **/
    public final Integer getX() throws DbException {
        return (Integer) get(fX);
    }

    /**
     * Gets the "row" property of a DbBEContextCell's instance.
     * 
     * @return the "row" property
     **/
    public final Integer getY() throws DbException {
        return (Integer) get(fY);
    }

    /**
     * Gets the "description" property of a DbBEContextCell's instance.
     * 
     * @return the "description" property
     **/
    public final String getDescription() throws DbException {
        return (String) get(fDescription);
    }

    /**
     * Gets the "font" of a DbBEContextCell's instance.
     * 
     * @return the "font"
     **/
    public final Font getFont() throws DbException {
        return (Font) get(fFont);
    }

    /**
     * Gets the "right border" property's Boolean value of a DbBEContextCell's instance.
     * 
     * @return the "right border" property's Boolean value
     * @deprecated use isRightBorder() method instead
     **/
    public final Boolean getRightBorder() throws DbException {
        return (Boolean) get(fRightBorder);
    }

    /**
     * Tells whether a DbBEContextCell's instance is rightBorder or not.
     * 
     * @return boolean
     **/
    public final boolean isRightBorder() throws DbException {
        return getRightBorder().booleanValue();
    }

    /**
     * Gets the "bottom border" property's Boolean value of a DbBEContextCell's instance.
     * 
     * @return the "bottom border" property's Boolean value
     * @deprecated use isBottomBorder() method instead
     **/
    public final Boolean getBottomBorder() throws DbException {
        return (Boolean) get(fBottomBorder);
    }

    /**
     * Tells whether a DbBEContextCell's instance is bottomBorder or not.
     * 
     * @return boolean
     **/
    public final boolean isBottomBorder() throws DbException {
        return getBottomBorder().booleanValue();
    }

    /**
     * Gets the "vertical alignment" of a DbBEContextCell's instance.
     * 
     * @return the "vertical alignment"
     **/
    public final SMSVerticalAlignment getVerticalJustification2() throws DbException {
        return (SMSVerticalAlignment) get(fVerticalJustification2);
    }

    /**
     * Gets the "horizontal alignment" of a DbBEContextCell's instance.
     * 
     * @return the "horizontal alignment"
     **/
    public final SMSHorizontalAlignment getHorizontalJustification2() throws DbException {
        return (SMSHorizontalAlignment) get(fHorizontalJustification2);
    }

    /**
     * Gets the "x weight" property of a DbBEContextCell's instance.
     * 
     * @return the "x weight" property
     **/
    public final Double getXweight() throws DbException {
        return (Double) get(fXweight);
    }

    /**
     * Gets the "y weight" property of a DbBEContextCell's instance.
     * 
     * @return the "y weight" property
     **/
    public final Double getYweight() throws DbException {
        return (Double) get(fYweight);
    }

    /**
     * Gets the list of process graphical objects associated to a DbBEContextCell's instance.
     * 
     * @return the list of process graphical objects.
     **/
    public final DbRelationN getUsecaseGos() throws DbException {
        return (DbRelationN) get(fUsecaseGos);
    }

    /**
     * Gets the list of store graphical objects associated to a DbBEContextCell's instance.
     * 
     * @return the list of store graphical objects.
     **/
    public final DbRelationN getStoreGos() throws DbException {
        return (DbRelationN) get(fStoreGos);
    }

    /**
     * Gets the list of actor graphical objects associated to a DbBEContextCell's instance.
     * 
     * @return the list of actor graphical objects.
     **/
    public final DbRelationN getActorGos() throws DbException {
        return (DbRelationN) get(fActorGos);
    }

    /**
     * Gets the "vertical alignment" property of a DbBEContextCell's instance.
     * 
     * @return the "vertical alignment" property
     **/
    public final Integer getVerticalJustification() throws DbException {
        return (Integer) get(fVerticalJustification);
    }

    /**
     * Gets the "horizontal alignment" property of a DbBEContextCell's instance.
     * 
     * @return the "horizontal alignment" property
     **/
    public final Integer getHorizontalJustification() throws DbException {
        return (Integer) get(fHorizontalJustification);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
