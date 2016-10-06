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
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBENotation.html">DbBENotation</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public final class DbBESingleZoneDisplay extends DbSingleZoneDisplay {

    //Meta

    public static final MetaField fStereotype = new MetaField(LocaleMgr.db.getString("stereotype"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbBESingleZoneDisplay"), DbBESingleZoneDisplay.class,
            new MetaField[] { fStereotype }, MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSingleZoneDisplay.metaClass);

            fStereotype.setJField(DbBESingleZoneDisplay.class.getDeclaredField("m_stereotype"));
            fStereotype.setVisibleInScreen(false);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    BEZoneStereotype m_stereotype;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbBESingleZoneDisplay() {
    }

    /**
     * Creates an instance of DbBESingleZoneDisplay.
     * 
     * @param composite
     *            org.modelsphere.jack.baseDb.db.DbObject
     * @param metafield
     *            org.modelsphere.jack.baseDb.meta.MetaField
     * @param displayed
     *            boolean
     * @param justification
     *            org.modelsphere.jack.baseDb.db.srtypes.ZoneJustification
     * @param separator
     *            boolean
     * @param stereotype
     *            org.modelsphere.sms.be.db.srtypes.BEZoneStereotype
     **/
    public DbBESingleZoneDisplay(DbObject composite, MetaField metaField, boolean displayed,
            ZoneJustification justification, boolean separator, BEZoneStereotype stereotype)
            throws DbException {
        super(composite, metaField, displayed, justification, separator);
        setStereotype(stereotype);
    }

    /**
     * Creates an instance of DbBESingleZoneDisplay.
     * 
     * @param composite
     *            org.modelsphere.jack.baseDb.db.DbObject
     * @param udf
     *            org.modelsphere.jack.baseDb.db.DbUDF
     * @param displayed
     *            boolean
     * @param justification
     *            org.modelsphere.jack.baseDb.db.srtypes.ZoneJustification
     * @param separator
     *            boolean
     * @param stereotype
     *            org.modelsphere.sms.be.db.srtypes.BEZoneStereotype
     **/
    public DbBESingleZoneDisplay(DbObject composite, DbUDF udf, boolean displayed,
            ZoneJustification justification, boolean separator, BEZoneStereotype stereotype)
            throws DbException {
        super(composite, udf, displayed, justification, separator);
        setStereotype(stereotype);
    }

    /**
     * Creates an instance of DbBESingleZoneDisplay.
     * 
     * @param composite
     *            org.modelsphere.jack.baseDb.db.DbObject
     * @param displayed
     *            boolean
     * @param justification
     *            org.modelsphere.jack.baseDb.db.srtypes.ZoneJustification
     * @param separator
     *            boolean
     * @param stereotype
     *            org.modelsphere.sms.be.db.srtypes.BEZoneStereotype
     **/
    public DbBESingleZoneDisplay(DbObject composite, boolean displayed,
            ZoneJustification justification, boolean separator, BEZoneStereotype stereotype)
            throws DbException {
        super(composite, displayed, justification, separator);
        setStereotype(stereotype);
    }

    private void setDefaultInitialValues() throws DbException {
    }

    //Setters

    /**
     * Sets the "stereotype" property of a DbBESingleZoneDisplay's instance.
     * 
     * @param value
     *            the "stereotype" property
     **/
    public final void setStereotype(BEZoneStereotype value) throws DbException {
        basicSet(fStereotype, value);
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
     * Gets the "stereotype" of a DbBESingleZoneDisplay's instance.
     * 
     * @return the "stereotype"
     **/
    public final BEZoneStereotype getStereotype() throws DbException {
        return (BEZoneStereotype) get(fStereotype);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
