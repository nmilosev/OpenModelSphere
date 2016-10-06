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
import org.modelsphere.sms.or.db.DbORProcedure;
import org.modelsphere.sms.SMSFilter;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/oo/db/DbOOClass.html">DbOOClass</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/oo/db/DbOOParameter.html">DbOOParameter</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbJVMethod extends DbOOMethod {

    //Meta

    public static final MetaRelationN fJavaExceptions = new MetaRelationN(LocaleMgr.db
            .getString("javaExceptions"), 0, MetaRelationN.cardN);
    public static final MetaField fFinal = new MetaField(LocaleMgr.db.getString("final"));
    public static final MetaField fNative = new MetaField(LocaleMgr.db.getString("native"));
    public static final MetaField fSynchronized = new MetaField(LocaleMgr.db
            .getString("synchronized"));
    public static final MetaRelationN fProcedures = new MetaRelationN(LocaleMgr.db
            .getString("procedures"), 0, MetaRelationN.cardN);
    public static final MetaField fStrictfp = new MetaField(LocaleMgr.db.getString("strictfp"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbJVMethod"),
            DbJVMethod.class, new MetaField[] { fJavaExceptions, fFinal, fNative, fSynchronized,
                    fProcedures, fStrictfp }, MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbOOMethod.metaClass);

            fJavaExceptions.setJField(DbJVMethod.class.getDeclaredField("m_javaExceptions"));
            fJavaExceptions.setFlags(MetaField.COPY_REFS | MetaField.INTEGRABLE
                    | MetaField.WRITE_CHECK | MetaField.INTEGRABLE_BY_NAME);
            fFinal.setJField(DbJVMethod.class.getDeclaredField("m_final"));
            fNative.setJField(DbJVMethod.class.getDeclaredField("m_native"));
            fSynchronized.setJField(DbJVMethod.class.getDeclaredField("m_synchronized"));
            fProcedures.setJField(DbJVMethod.class.getDeclaredField("m_procedures"));
            fStrictfp.setJField(DbJVMethod.class.getDeclaredField("m_strictfp"));

            fJavaExceptions.setOppositeRel(DbJVClass.fThrowingMethods);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;
    public static final String ARRAY_TYPE_USE = "[]";

    //Instance variables
    DbRelationN m_javaExceptions;
    boolean m_final;
    boolean m_native;
    boolean m_synchronized;
    DbRelationN m_procedures;
    boolean m_strictfp;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbJVMethod() {
    }

    /**
     * Creates an instance of DbJVMethod.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbJVMethod(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setName(LocaleMgr.misc.getString("method"));
        setVisibility(JVVisibility.getInstance(JVVisibility.PUBLIC));
    }

    /**
     * @return int
     **/
    protected final int getFeatureSet() {
        return SMSFilter.JAVA;

    }

    //Setters

    /**
     * Adds an element to or removes an element from the list of exceptions associated to a
     * DbJVMethod's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setJavaExceptions(DbJVClass value, int op) throws DbException {
        setRelationNN(fJavaExceptions, value, op);
    }

    /**
     * Adds an element to the list of exceptions associated to a DbJVMethod's instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToJavaExceptions(DbJVClass value) throws DbException {
        setJavaExceptions(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of exceptions associated to a DbJVMethod's instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromJavaExceptions(DbJVClass value) throws DbException {
        setJavaExceptions(value, Db.REMOVE_FROM_RELN);
    }

    /**
     * Sets the "final" property of a DbJVMethod's instance.
     * 
     * @param value
     *            the "final" property
     **/
    public final void setFinal(Boolean value) throws DbException {
        basicSet(fFinal, value);
    }

    /**
     * Sets the "native" property of a DbJVMethod's instance.
     * 
     * @param value
     *            the "native" property
     **/
    public final void setNative(Boolean value) throws DbException {
        basicSet(fNative, value);
    }

    /**
     * Sets the "synchronized" property of a DbJVMethod's instance.
     * 
     * @param value
     *            the "synchronized" property
     **/
    public final void setSynchronized(Boolean value) throws DbException {
        basicSet(fSynchronized, value);
    }

    /**
     * Sets the "strictfp" property of a DbJVMethod's instance.
     * 
     * @param value
     *            the "strictfp" property
     **/
    public final void setStrictfp(Boolean value) throws DbException {
        basicSet(fStrictfp, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fJavaExceptions)
                setJavaExceptions((DbJVClass) value, Db.ADD_TO_RELN);
            else if (metaField == fProcedures)
                ((DbORProcedure) value).setJavaMethod(this);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        if (relation == fJavaExceptions)
            setJavaExceptions((DbJVClass) neighbor, op);
        else
            super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the list of exceptions associated to a DbJVMethod's instance.
     * 
     * @return the list of exceptions.
     **/
    public final DbRelationN getJavaExceptions() throws DbException {
        return (DbRelationN) get(fJavaExceptions);
    }

    /**
     * Gets the "final" property's Boolean value of a DbJVMethod's instance.
     * 
     * @return the "final" property's Boolean value
     * @deprecated use isFinal() method instead
     **/
    public final Boolean getFinal() throws DbException {
        return (Boolean) get(fFinal);
    }

    /**
     * Tells whether a DbJVMethod's instance is final or not.
     * 
     * @return boolean
     **/
    public final boolean isFinal() throws DbException {
        return getFinal().booleanValue();
    }

    /**
     * Gets the "native" property's Boolean value of a DbJVMethod's instance.
     * 
     * @return the "native" property's Boolean value
     * @deprecated use isNative() method instead
     **/
    public final Boolean getNative() throws DbException {
        return (Boolean) get(fNative);
    }

    /**
     * Tells whether a DbJVMethod's instance is native or not.
     * 
     * @return boolean
     **/
    public final boolean isNative() throws DbException {
        return getNative().booleanValue();
    }

    /**
     * Gets the "synchronized" property's Boolean value of a DbJVMethod's instance.
     * 
     * @return the "synchronized" property's Boolean value
     * @deprecated use isSynchronized() method instead
     **/
    public final Boolean getSynchronized() throws DbException {
        return (Boolean) get(fSynchronized);
    }

    /**
     * Tells whether a DbJVMethod's instance is synchronized or not.
     * 
     * @return boolean
     **/
    public final boolean isSynchronized() throws DbException {
        return getSynchronized().booleanValue();
    }

    /**
     * Gets the list of procedures associated to a DbJVMethod's instance.
     * 
     * @return the list of procedures.
     **/
    public final DbRelationN getProcedures() throws DbException {
        return (DbRelationN) get(fProcedures);
    }

    /**
     * Gets the "strictfp" property's Boolean value of a DbJVMethod's instance.
     * 
     * @return the "strictfp" property's Boolean value
     * @deprecated use isStrictfp() method instead
     **/
    public final Boolean getStrictfp() throws DbException {
        return (Boolean) get(fStrictfp);
    }

    /**
     * Tells whether a DbJVMethod's instance is strictfp or not.
     * 
     * @return boolean
     **/
    public final boolean isStrictfp() throws DbException {
        return getStrictfp().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
