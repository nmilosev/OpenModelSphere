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
package org.modelsphere.sms.oo.java.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.oo.java.db.srtypes.*;
import org.modelsphere.sms.oo.java.international.LocaleMgr;
import org.modelsphere.sms.oo.db.*;
import org.modelsphere.sms.oo.db.srtypes.*;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.SMSFilter;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/oo/db/DbOOClass.html">DbOOClass</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbJVDataMember extends DbOODataMember {

    //Meta

    public static final MetaField fFinal = new MetaField(LocaleMgr.db.getString("final"));
    public static final MetaField fTransient = new MetaField(LocaleMgr.db.getString("transient"));
    public static final MetaField fVolatile = new MetaField(LocaleMgr.db.getString("volatile"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbJVDataMember"), DbJVDataMember.class, new MetaField[] { fFinal,
            fTransient, fVolatile }, MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbOODataMember.metaClass);

            fFinal.setJField(DbJVDataMember.class.getDeclaredField("m_final"));
            fTransient.setJField(DbJVDataMember.class.getDeclaredField("m_transient"));
            fVolatile.setJField(DbJVDataMember.class.getDeclaredField("m_volatile"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;
    public static final String ARRAY_TYPE_USE = "[]";

    //Instance variables
    boolean m_final;
    boolean m_transient;
    boolean m_volatile;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbJVDataMember() {
    }

    /**
     * Creates an instance of DbJVDataMember.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbJVDataMember(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setName(LocaleMgr.misc.getString("field"));
        if (((DbJVClass) getComposite()).isInterface())
            setVisibility(JVVisibility.getInstance(JVVisibility.DEFAULT));
        else
            setVisibility(JVVisibility.getInstance(JVVisibility.PRIVATE));
    }

    /**
     * @param oppmember
     *            org.modelsphere.sms.oo.java.db.DbJVDataMember
     * @param mult
     *            org.modelsphere.sms.db.srtypes.SMSMultiplicity
     **/
    public final void setRoleDefaultInitialValues(DbJVDataMember oppMember, SMSMultiplicity mult)
            throws DbException {
        DbJVClass oppClass = (DbJVClass) oppMember.getComposite();
        setType(oppClass);
        if (mult.getValue() == SMSMultiplicity.MANY
                || mult.getValue() == SMSMultiplicity.ONE_OR_MORE) {
            DbJVClass collType = ((DbSMSProject) getProject()).getDefaultCollectionType();
            if (collType != null) {
                setType(collType);
                setElementType(oppClass);
            } else
                setTypeUse(ARRAY_TYPE_USE);
        }
        String name = oppClass.getName();
        setName(Character.toLowerCase(name.charAt(0)) + name.substring(1));
    }

    /**
     * @return int
     **/
    protected final int getFeatureSet() {
        return SMSFilter.JAVA;

    }

    //Setters

    /**
     * Sets the "final" property of a DbJVDataMember's instance.
     * 
     * @param value
     *            the "final" property
     **/
    public final void setFinal(Boolean value) throws DbException {
        basicSet(fFinal, value);
    }

    /**
     * Sets the "transient" property of a DbJVDataMember's instance.
     * 
     * @param value
     *            the "transient" property
     **/
    public final void setTransient(Boolean value) throws DbException {
        basicSet(fTransient, value);
    }

    /**
     * Sets the "volatile" property of a DbJVDataMember's instance.
     * 
     * @param value
     *            the "volatile" property
     **/
    public final void setVolatile(Boolean value) throws DbException {
        basicSet(fVolatile, value);
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
     * Gets the "final" property's Boolean value of a DbJVDataMember's instance.
     * 
     * @return the "final" property's Boolean value
     * @deprecated use isFinal() method instead
     **/
    public final Boolean getFinal() throws DbException {
        return (Boolean) get(fFinal);
    }

    /**
     * Tells whether a DbJVDataMember's instance is final or not.
     * 
     * @return boolean
     **/
    public final boolean isFinal() throws DbException {
        return getFinal().booleanValue();
    }

    /**
     * Gets the "transient" property's Boolean value of a DbJVDataMember's instance.
     * 
     * @return the "transient" property's Boolean value
     * @deprecated use isTransient() method instead
     **/
    public final Boolean getTransient() throws DbException {
        return (Boolean) get(fTransient);
    }

    /**
     * Tells whether a DbJVDataMember's instance is transient or not.
     * 
     * @return boolean
     **/
    public final boolean isTransient() throws DbException {
        return getTransient().booleanValue();
    }

    /**
     * Gets the "volatile" property's Boolean value of a DbJVDataMember's instance.
     * 
     * @return the "volatile" property's Boolean value
     * @deprecated use isVolatile() method instead
     **/
    public final Boolean getVolatile() throws DbException {
        return (Boolean) get(fVolatile);
    }

    /**
     * Tells whether a DbJVDataMember's instance is volatile or not.
     * 
     * @return boolean
     **/
    public final boolean isVolatile() throws DbException {
        return getVolatile().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
