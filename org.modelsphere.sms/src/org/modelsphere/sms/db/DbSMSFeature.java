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
package org.modelsphere.sms.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.international.LocaleMgr;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSStructuralFeature.html">DbSMSStructuralFeature</A>,
 * <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSBehavioralFeature.html">DbSMSBehavioralFeature</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbSMSFeature extends DbSMSTypedElement {

    //Meta

    public static final MetaRelationN fOverriddenFeatures = new MetaRelationN(LocaleMgr.db
            .getString("overriddenFeatures"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fOverridingFeatures = new MetaRelationN(LocaleMgr.db
            .getString("overridingFeatures"), 0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbSMSFeature"),
            DbSMSFeature.class, new MetaField[] { fOverriddenFeatures, fOverridingFeatures }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSTypedElement.metaClass);

            fOverriddenFeatures.setJField(DbSMSFeature.class
                    .getDeclaredField("m_overriddenFeatures"));
            fOverridingFeatures.setJField(DbSMSFeature.class
                    .getDeclaredField("m_overridingFeatures"));

            fOverriddenFeatures.setOppositeRel(DbSMSFeature.fOverridingFeatures);
            fOverridingFeatures.setOppositeRel(DbSMSFeature.fOverriddenFeatures);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbRelationN m_overriddenFeatures;
    DbRelationN m_overridingFeatures;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSFeature() {
    }

    /**
     * Creates an instance of DbSMSFeature.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbSMSFeature(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    //Setters

    /**
     * Adds an element to or removes an element from the list of overridden features associated to a
     * DbSMSFeature's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setOverriddenFeatures(DbSMSFeature value, int op) throws DbException {
        setRelationNN(fOverriddenFeatures, value, op);
    }

    /**
     * Adds an element to the list of overridden features associated to a DbSMSFeature's instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToOverriddenFeatures(DbSMSFeature value) throws DbException {
        setOverriddenFeatures(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of overridden features associated to a DbSMSFeature's
     * instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromOverriddenFeatures(DbSMSFeature value) throws DbException {
        setOverriddenFeatures(value, Db.REMOVE_FROM_RELN);
    }

    /**
     * Adds an element to or removes an element from the list of overriding features associated to a
     * DbSMSFeature's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setOverridingFeatures(DbSMSFeature value, int op) throws DbException {
        setRelationNN(fOverridingFeatures, value, op);
    }

    /**
     * Adds an element to the list of overriding features associated to a DbSMSFeature's instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToOverridingFeatures(DbSMSFeature value) throws DbException {
        setOverridingFeatures(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of overriding features associated to a DbSMSFeature's
     * instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromOverridingFeatures(DbSMSFeature value) throws DbException {
        setOverridingFeatures(value, Db.REMOVE_FROM_RELN);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fOverriddenFeatures)
                setOverriddenFeatures((DbSMSFeature) value, Db.ADD_TO_RELN);
            else if (metaField == fOverridingFeatures)
                setOverridingFeatures((DbSMSFeature) value, Db.ADD_TO_RELN);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        if (relation == fOverriddenFeatures)
            setOverriddenFeatures((DbSMSFeature) neighbor, op);
        else if (relation == fOverridingFeatures)
            setOverridingFeatures((DbSMSFeature) neighbor, op);
        else
            super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the list of overridden features associated to a DbSMSFeature's instance.
     * 
     * @return the list of overridden features.
     **/
    public final DbRelationN getOverriddenFeatures() throws DbException {
        return (DbRelationN) get(fOverriddenFeatures);
    }

    /**
     * Gets the list of overriding features associated to a DbSMSFeature's instance.
     * 
     * @return the list of overriding features.
     **/
    public final DbRelationN getOverridingFeatures() throws DbException {
        return (DbRelationN) get(fOverridingFeatures);
    }

}
