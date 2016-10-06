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

package org.modelsphere.jack.baseDb.db;

import org.modelsphere.jack.baseDb.db.srtypes.ZoneJustification;
import org.modelsphere.jack.baseDb.international.LocaleMgr;
import org.modelsphere.jack.baseDb.meta.*;

/**
 * Defines display attributes of a single zone for graphical objects. A single zone contains only
 * one element (for instance the zone to display the name of a process), as opposite to multi-valued
 * zones that contain several element. This is an element contained in a notation.
 * 
 * See DbBESingleZoneDisplay in sms.be.db for a concrete class that extends a DbZoneDisplay.
 **/
public abstract class DbSingleZoneDisplay extends DbZoneDisplay {
    transient MetaField metaField = null;
    private static final String METAFIELD_SEPARATOR = "@"; // NOT LOCALIZABLE

    // Meta
    public static final MetaField fJustification = new MetaField(LocaleMgr.db
            .getString("justification"));
    public static final MetaField fSeparator = new MetaField(LocaleMgr.db.getString("separator"));
    public static final MetaField fUseExtraSpace = new MetaField(LocaleMgr.db
            .getString("useExtraSpace"));
    public static final MetaField fMetaField = new MetaField(LocaleMgr.db.getString("metaField"));
    public static final MetaRelation1 fUdf = new MetaRelation1(LocaleMgr.db.getString("udf"), 0);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbSingleZoneDisplay"), DbSingleZoneDisplay.class, new MetaField[] { fUdf,
            fJustification, fMetaField, fSeparator, fUseExtraSpace }, MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbZoneDisplay.metaClass);
            // Meta fields
            fUdf.setJField(DbSingleZoneDisplay.class.getDeclaredField("m_udf"));
            fUdf.setFlags(MetaField.COPY_REFS);
            fJustification.setJField(DbSingleZoneDisplay.class.getDeclaredField("m_justification"));
            fSeparator.setJField(DbSingleZoneDisplay.class.getDeclaredField("m_separator"));
            fUseExtraSpace.setJField(DbSingleZoneDisplay.class.getDeclaredField("m_useExtraSpace"));
            fMetaField.setJField(DbSingleZoneDisplay.class.getDeclaredField("m_metaField"));
            // Opposite relations
            fUdf.setOppositeRel(DbUDF.fDisplayZones);
        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    // Instance variables
    String m_metaField;
    DbUDF m_udf;
    ZoneJustification m_justification;
    boolean m_separator;
    boolean m_useExtraSpace;

    // Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSingleZoneDisplay() {
    }

    /**
     * Creates an instance of DbSingleZoneDisplay.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbSingleZoneDisplay(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    /**
     * Creates an instance of DbSingleZoneDisplay.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     * @param MetaField
     *            metaField
     * @param boolean displayed
     * @param ZoneJustification
     * @param justification
     * @param boolean separator
     **/
    public DbSingleZoneDisplay(DbObject composite, MetaField metaField, boolean displayed,
            ZoneJustification justification, boolean separator) throws DbException {
        super(composite);
        setMetaField(metaField);
        setDisplayed(new Boolean(displayed));
        setJustification(justification);
        setSeparator(new Boolean(separator));
        setDefaultInitialValues();
    }

    /**
     * Creates an instance of DbSingleZoneDisplay.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     * @param DbUDF
     *            udf
     * @param boolean displayed
     * @param ZoneJustification
     *            justification
     * @param boolean separator
     **/
    public DbSingleZoneDisplay(DbObject composite, DbUDF udf, boolean displayed,
            ZoneJustification justification, boolean separator) throws DbException {
        super(composite);
        setUdf(udf);
        setDisplayed(new Boolean(displayed));
        setJustification(justification);
        setSeparator(new Boolean(separator));
        setDefaultInitialValues();
    }

    /**
     * Creates an empty instance of DbSingleZoneDisplay.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     * @param boolean displayed
     * @param ZoneJustification
     *            justification
     * @param boolean separator
     **/
    public DbSingleZoneDisplay(DbObject composite, boolean displayed,
            ZoneJustification justification, boolean separator) throws DbException {
        super(composite);
        setUdf(null);
        setMetaField(null);
        setDisplayed(new Boolean(displayed));
        setJustification(justification);
        setSeparator(new Boolean(separator));
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    // Setters
    /**
     * Sets the metafield object associated to a DbDisplayField's instance.
     * 
     * @param value
     *            the metafield object to be set
     **/
    public final void setMetaField(MetaField value) throws DbException {
        String metaFieldName = (value == null ? null : value.getMetaClass().getJClass().getName()
                + METAFIELD_SEPARATOR + value.getJName());
        basicSet(fMetaField, metaFieldName);
        this.metaField = value;
    }

    /**
     * Sets the udf object associated to a DbDisplayField's instance.
     * 
     * @param value
     *            the udf object to be associated
     **/
    public final void setUdf(DbUDF value) throws DbException {
        basicSet(fUdf, value);
    }

    /**
     * Sets the "justification" property of a DbZoneDisplay's instance.
     * 
     * @param value
     *            the "justification" property
     **/
    public final void setJustification(ZoneJustification value) throws DbException {
        basicSet(fJustification, value);
    }

    /**
     * Sets the "separator" property of a DbZoneDisplay's instance.
     * 
     * @param value
     *            the "separator" property
     **/
    public final void setSeparator(Boolean value) throws DbException {
        basicSet(fSeparator, value);
    }

    /**
     * Sets the "useExtraSpace" property of a DbZoneDisplay's instance.
     * 
     * @param value
     *            the "useExtraSpace" property
     **/
    public final void setUseExtraSpace(Boolean value) throws DbException {
        basicSet(fUseExtraSpace, value);
    }

    /**
     * @deprecated use the appropriate setXXX() method instead.
     **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
     * @deprecated use the appropriate addToXXX() or removeFromXXX() method instead.
     **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        super.set(relation, neighbor, op);
    }

    // Getters
    /**
     * Get Field GUIName or UDF Name
     * 
     * @return String: the name.
     * @throws DbException
     */
    public final String getGUIName() throws DbException {
        MetaField mField = getMetaField();
        if (mField != null)
            return mField.getGUIName();
        else if (getUdf() != null) {
            DbUDF udf = getUdf();
            return udf.getName();
        } else
            return null;
    }

    /**
     * Get the value of the MetaField or the UDF
     * 
     * @return Object: the value
     * @throws DbException
     */
    public final Object getValue(DbObject dbo) throws DbException {
        MetaField mField = getMetaField();
        if (mField != null)
            return dbo.get(mField);
        else if (getUdf() != null) {
            DbUDF udf = getUdf();
            return dbo.get(udf);
        } else
            return null;
    }

    /**
     * Gets the metafield object associated to a DbDisplayField's instance.
     * 
     * @return the metafield object
     **/
    public final MetaField getMetaField() throws DbException {
        if (this.metaField == null) {
            String metaFieldName = (String) get(fMetaField);
            if (metaFieldName == null)
                return null;
            MetaClass fieldMetaClass = MetaClass.find(metaFieldName.substring(0, metaFieldName
                    .indexOf(METAFIELD_SEPARATOR)));
            if (fieldMetaClass == null)
                throw new RuntimeException("Unknown metaClass for metaField "
                        + metaFieldName.substring(0, metaFieldName.indexOf(METAFIELD_SEPARATOR))); // NOT
            // LOCALIZABLE
            // RuntimeException
            else {
                this.metaField = fieldMetaClass.getMetaField(metaFieldName.substring(metaFieldName
                        .indexOf(METAFIELD_SEPARATOR) + 1, metaFieldName.length()));
                if (this.metaField == null)
                    throw new RuntimeException("Unknown metaField \""
                            + metaFieldName.substring(
                                    metaFieldName.indexOf(METAFIELD_SEPARATOR) + 1, metaFieldName
                                            .length())
                            + "\" for MetaClass "
                            + metaFieldName
                                    .substring(0, metaFieldName.indexOf(METAFIELD_SEPARATOR))); // NOT
                // LOCALIZABLE
                // RuntimeException
            }
        }
        return this.metaField;
    }

    /**
     * Gets the udf object associated to a DbDisplayField's instance.
     * 
     * @return the udf object
     **/
    public final DbUDF getUdf() throws DbException {
        return (DbUDF) get(fUdf);
    }

    /**
     * Gets the source object associated to a DbDisplayField's instance.
     * 
     * @return the source object
     **/
    public final Object getSourceObject() throws DbException {
        if (getMetaField() != null)
            return getMetaField();
        else
            return getUdf();
    }

    /**
     * Gets the "justification" of a DbZoneDisplay's instance.
     * 
     * @return the "justification"
     **/
    public final ZoneJustification getJustification() throws DbException {
        return (ZoneJustification) get(fJustification);
    }

    /**
     * Gets the "separator" property's Boolean value of a DbZoneDisplay's instance.
     * 
     * @return the "separator" property's Boolean value
     * @deprecated use isSeparator() method instead
     **/
    public final Boolean getSeparator() throws DbException {
        return (Boolean) get(fSeparator);
    }

    /**
     * Gets the "useExtraSpace" property's Boolean value of a DbZoneDisplay's instance.
     * 
     * @return the "useExtraSpace" property's Boolean value
     * @deprecated use isUseExtraSpace() method instead
     **/
    public final Boolean getUseExtraSpace() throws DbException {
        return (Boolean) get(fUseExtraSpace);
    }

    /**
     * Tells whether a DbZoneDisplay's instance is separator or not.
     * 
     * @return boolean
     **/
    public final boolean isSeparator() throws DbException {
        return getSeparator().booleanValue();
    }

    /**
     * Tells whether a DbZoneDisplay's instance is useExtraSpace or not.
     * 
     * @return boolean
     **/
    public final boolean isUseExtraSpace() throws DbException {
        return getUseExtraSpace().booleanValue();
    }

    /**
     * Tells whether a DbZoneDisplay's instance is empty or not.
     * 
     * @return boolean
     **/
    public final boolean isEmpty() throws DbException {
        return (getUdf() == null && getMetaField() == null);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
