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
import org.modelsphere.sms.oo.db.DbOOAdt;
import org.modelsphere.sms.oo.db.DbOODataMember;
import org.modelsphere.sms.oo.db.srtypes.OOTypeUseStyle;
import org.modelsphere.sms.oo.java.db.srtypes.JVVisibility;
import org.modelsphere.jack.baseDb.db.srtypes.SrBoolean;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A HREF="../../../../../org/modelsphere/sms/or/db/DbORCommonItemModel.html"
 * >DbORCommonItemModel</A>.<br>
 * <b>Components : </b><A HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html"
 * >DbSMSObjectImport</A>.<br>
 **/
public final class DbORCommonItem extends DbSMSCommonItem {

    // Meta

    public static final MetaField fLength = new MetaField(LocaleMgr.db.getString("length"));
    public static final MetaField fNbDecimal = new MetaField(LocaleMgr.db.getString("nbDecimal"));
    public static final MetaField fNull = new MetaField(LocaleMgr.db.getString("null"));
    public static final MetaRelationN fColumns = new MetaRelationN(LocaleMgr.db
            .getString("columns"), 0, MetaRelationN.cardN);
    public static final MetaRelation1 fType = new MetaRelation1(LocaleMgr.db.getString("type"), 0);
    public static final MetaField fVisibility = new MetaField(LocaleMgr.db.getString("visibility"));
    public static final MetaField fTypeUse = new MetaField(LocaleMgr.db.getString("typeUse"));
    public static final MetaField fTypeUseStyle = new MetaField(LocaleMgr.db
            .getString("typeUseStyle"));
    public static final MetaField fStatic = new MetaField(LocaleMgr.db.getString("static"));
    public static final MetaField fFinal = new MetaField(LocaleMgr.db.getString("final"));
    public static final MetaField fTransient = new MetaField(LocaleMgr.db.getString("transient"));
    public static final MetaField fVolatile = new MetaField(LocaleMgr.db.getString("volatile"));
    public static final MetaRelation1 fOoType = new MetaRelation1(LocaleMgr.db.getString("ooType"),
            0);
    public static final MetaRelationN fFields = new MetaRelationN(LocaleMgr.db.getString("fields"),
            0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbORCommonItem"), DbORCommonItem.class, new MetaField[] { fLength,
            fNbDecimal, fNull, fColumns, fType, fVisibility, fTypeUse, fTypeUseStyle, fStatic,
            fFinal, fTransient, fVolatile, fOoType, fFields }, MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSCommonItem.metaClass);
            metaClass.setIcon("dborcommonitem.gif");

            fLength.setJField(DbORCommonItem.class.getDeclaredField("m_length"));
            fNbDecimal.setJField(DbORCommonItem.class.getDeclaredField("m_nbDecimal"));
            fNull.setJField(DbORCommonItem.class.getDeclaredField("m_null"));
            fColumns.setJField(DbORCommonItem.class.getDeclaredField("m_columns"));
            fType.setJField(DbORCommonItem.class.getDeclaredField("m_type"));
            fType.setFlags(MetaField.COPY_REFS | MetaField.INTEGRABLE_BY_NAME);
            fType.setRendererPluginName("DbFullNameInTip;DbORTypeClassifier");
            fVisibility.setJField(DbORCommonItem.class.getDeclaredField("m_visibility"));
            fTypeUse.setJField(DbORCommonItem.class.getDeclaredField("m_typeUse"));
            fTypeUseStyle.setJField(DbORCommonItem.class.getDeclaredField("m_typeUseStyle"));
            fStatic.setJField(DbORCommonItem.class.getDeclaredField("m_static"));
            fFinal.setJField(DbORCommonItem.class.getDeclaredField("m_final"));
            fTransient.setJField(DbORCommonItem.class.getDeclaredField("m_transient"));
            fVolatile.setJField(DbORCommonItem.class.getDeclaredField("m_volatile"));
            fOoType.setJField(DbORCommonItem.class.getDeclaredField("m_ooType"));
            fFields.setJField(DbORCommonItem.class.getDeclaredField("m_fields"));

            fType.setOppositeRel(DbORTypeClassifier.fCommonItems);
            fOoType.setOppositeRel(DbOOAdt.fCommonItems);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    // Instance variables
    SrInteger m_length;
    SrInteger m_nbDecimal;
    boolean m_null;
    DbRelationN m_columns;
    DbORTypeClassifier m_type;
    JVVisibility m_visibility;
    String m_typeUse;
    OOTypeUseStyle m_typeUseStyle;
    boolean m_static;
    boolean m_final;
    boolean m_transient;
    boolean m_volatile;
    DbOOAdt m_ooType;
    DbRelationN m_fields;

    // Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORCommonItem() {
    }

    /**
     * Creates an instance of DbORCommonItem.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORCommonItem(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setNull(Boolean.TRUE);
        setName(LocaleMgr.misc.getString("commonitem"));
        fNull.setRendererPluginName("Boolean");

    }

    /**
     * @param metafield
     *            org.modelsphere.jack.baseDb.meta.MetaField
     * @param value
     *            java.lang.Object
     **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fColumns)
                ((DbORColumn) value).setCommonItem(this);
            else if (metaField == fFields)
                ((DbOODataMember) value).setCommonItem(this);
            else if (metaField == fNull) {
                if (value instanceof Boolean)
                    setNull((Boolean) value);
                else if (value instanceof SrBoolean)
                    setNull((Boolean) ((SrBoolean) value).toApplType());
                else
                    basicSet(metaField, value);
            } else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    // Setters

    /**
     * Sets the "length" property of a DbORCommonItem's instance.
     * 
     * @param value
     *            the "length" property
     **/
    public final void setLength(Integer value) throws DbException {
        basicSet(fLength, value);
    }

    /**
     * Sets the "nbr. decimals" property of a DbORCommonItem's instance.
     * 
     * @param value
     *            the "nbr. decimals" property
     **/
    public final void setNbDecimal(Integer value) throws DbException {
        basicSet(fNbDecimal, value);
    }

    /**
     * Sets the "null possible" property of a DbORCommonItem's instance.
     * 
     * @param value
     *            the "null possible" property
     **/
    public final void setNull(Boolean value) throws DbException {
        basicSet(fNull, value);
    }

    /**
     * Sets the type object associated to a DbORCommonItem's instance.
     * 
     * @param value
     *            the type object to be associated
     **/
    public final void setType(DbORTypeClassifier value) throws DbException {
        basicSet(fType, value);
    }

    /**
     * Sets the "oo visibility" property of a DbORCommonItem's instance.
     * 
     * @param value
     *            the "oo visibility" property
     **/
    public final void setVisibility(JVVisibility value) throws DbException {
        basicSet(fVisibility, value);
    }

    /**
     * Sets the "oo type use" property of a DbORCommonItem's instance.
     * 
     * @param value
     *            the "oo type use" property
     **/
    public final void setTypeUse(String value) throws DbException {
        basicSet(fTypeUse, value);
    }

    /**
     * Sets the "oo type use style" property of a DbORCommonItem's instance.
     * 
     * @param value
     *            the "oo type use style" property
     **/
    public final void setTypeUseStyle(OOTypeUseStyle value) throws DbException {
        basicSet(fTypeUseStyle, value);
    }

    /**
     * Sets the "oo static" property of a DbORCommonItem's instance.
     * 
     * @param value
     *            the "oo static" property
     **/
    public final void setStatic(Boolean value) throws DbException {
        basicSet(fStatic, value);
    }

    /**
     * Sets the "oo final" property of a DbORCommonItem's instance.
     * 
     * @param value
     *            the "oo final" property
     **/
    public final void setFinal(Boolean value) throws DbException {
        basicSet(fFinal, value);
    }

    /**
     * Sets the "oo transient" property of a DbORCommonItem's instance.
     * 
     * @param value
     *            the "oo transient" property
     **/
    public final void setTransient(Boolean value) throws DbException {
        basicSet(fTransient, value);
    }

    /**
     * Sets the "oo volatile" property of a DbORCommonItem's instance.
     * 
     * @param value
     *            the "oo volatile" property
     **/
    public final void setVolatile(Boolean value) throws DbException {
        basicSet(fVolatile, value);
    }

    /**
     * Sets the oo type object associated to a DbORCommonItem's instance.
     * 
     * @param value
     *            the oo type object to be associated
     **/
    public final void setOoType(DbOOAdt value) throws DbException {
        basicSet(fOoType, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        super.set(relation, neighbor, op);
    }

    // Getters

    /**
     * Gets the "length" of a DbORCommonItem's instance.
     * 
     * @return the "length"
     **/
    public final Integer getLength() throws DbException {
        return (Integer) get(fLength);
    }

    /**
     * Gets the "nbr. decimals" of a DbORCommonItem's instance.
     * 
     * @return the "nbr. decimals"
     **/
    public final Integer getNbDecimal() throws DbException {
        return (Integer) get(fNbDecimal);
    }

    /**
     * Gets the "null possible" of a DbORCommonItem's instance.
     * 
     * @return the "null possible"
     **/
    public final Boolean getNull() throws DbException {
        return (Boolean) get(fNull);
    }

    /**
     * Gets the list of columns associated to a DbORCommonItem's instance.
     * 
     * @return the list of columns.
     **/
    public final DbRelationN getColumns() throws DbException {
        return (DbRelationN) get(fColumns);
    }

    /**
     * Gets the type object associated to a DbORCommonItem's instance.
     * 
     * @return the type object
     **/
    public final DbORTypeClassifier getType() throws DbException {
        return (DbORTypeClassifier) get(fType);
    }

    /**
     * Gets the "oo visibility" of a DbORCommonItem's instance.
     * 
     * @return the "oo visibility"
     **/
    public final JVVisibility getVisibility() throws DbException {
        return (JVVisibility) get(fVisibility);
    }

    /**
     * Gets the "oo type use" property of a DbORCommonItem's instance.
     * 
     * @return the "oo type use" property
     **/
    public final String getTypeUse() throws DbException {
        return (String) get(fTypeUse);
    }

    /**
     * Gets the "oo type use style" of a DbORCommonItem's instance.
     * 
     * @return the "oo type use style"
     **/
    public final OOTypeUseStyle getTypeUseStyle() throws DbException {
        return (OOTypeUseStyle) get(fTypeUseStyle);
    }

    /**
     * Gets the "oo static" property's Boolean value of a DbORCommonItem's instance.
     * 
     * @return the "oo static" property's Boolean value
     * @deprecated use isStatic() method instead
     **/
    public final Boolean getStatic() throws DbException {
        return (Boolean) get(fStatic);
    }

    /**
     * Tells whether a DbORCommonItem's instance is static or not.
     * 
     * @return boolean
     **/
    public final boolean isStatic() throws DbException {
        return getStatic().booleanValue();
    }

    /**
     * Gets the "oo final" property's Boolean value of a DbORCommonItem's instance.
     * 
     * @return the "oo final" property's Boolean value
     * @deprecated use isFinal() method instead
     **/
    public final Boolean getFinal() throws DbException {
        return (Boolean) get(fFinal);
    }

    /**
     * Tells whether a DbORCommonItem's instance is final or not.
     * 
     * @return boolean
     **/
    public final boolean isFinal() throws DbException {
        return getFinal().booleanValue();
    }

    /**
     * Gets the "oo transient" property's Boolean value of a DbORCommonItem's instance.
     * 
     * @return the "oo transient" property's Boolean value
     * @deprecated use isTransient() method instead
     **/
    public final Boolean getTransient() throws DbException {
        return (Boolean) get(fTransient);
    }

    /**
     * Tells whether a DbORCommonItem's instance is transient or not.
     * 
     * @return boolean
     **/
    public final boolean isTransient() throws DbException {
        return getTransient().booleanValue();
    }

    /**
     * Gets the "oo volatile" property's Boolean value of a DbORCommonItem's instance.
     * 
     * @return the "oo volatile" property's Boolean value
     * @deprecated use isVolatile() method instead
     **/
    public final Boolean getVolatile() throws DbException {
        return (Boolean) get(fVolatile);
    }

    /**
     * Tells whether a DbORCommonItem's instance is volatile or not.
     * 
     * @return boolean
     **/
    public final boolean isVolatile() throws DbException {
        return getVolatile().booleanValue();
    }

    /**
     * Gets the oo type object associated to a DbORCommonItem's instance.
     * 
     * @return the oo type object
     **/
    public final DbOOAdt getOoType() throws DbException {
        return (DbOOAdt) get(fOoType);
    }

    /**
     * Gets the list of fields associated to a DbORCommonItem's instance.
     * 
     * @return the list of fields.
     **/
    public final DbRelationN getFields() throws DbException {
        return (DbRelationN) get(fFields);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
