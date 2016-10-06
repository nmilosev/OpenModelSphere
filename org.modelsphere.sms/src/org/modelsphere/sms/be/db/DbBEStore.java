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
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.baseDb.util.Terminology;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEModel.html">DbBEModel</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEStoreQualifier.html">DbBEStoreQualifier</A>,
 * <A HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbBEStore extends DbSMSClassifier {

    //Meta

    public static final MetaRelationN fFirstEndFlows = new MetaRelationN(LocaleMgr.db
            .getString("firstEndFlows"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fSecondEndFlows = new MetaRelationN(LocaleMgr.db
            .getString("secondEndFlows"), 0, MetaRelationN.cardN);
    public static final MetaField fIdentifier = new MetaField(LocaleMgr.db.getString("identifier"));
    public static final MetaField fControl = new MetaField(LocaleMgr.db.getString("control"));
    public static final MetaField fVolume = new MetaField(LocaleMgr.db.getString("volume"));
    public static final MetaRelation1 fDbJVClass = new MetaRelation1(LocaleMgr.db
            .getString("dbJVClass"), 0);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbBEStore"),
            DbBEStore.class, new MetaField[] { fFirstEndFlows, fSecondEndFlows, fIdentifier,
                    fControl, fVolume, fDbJVClass }, MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSClassifier.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbBEStoreQualifier.metaClass });
            metaClass.setIcon("dbbestore.gif");

            fFirstEndFlows.setJField(DbBEStore.class.getDeclaredField("m_firstEndFlows"));
            fSecondEndFlows.setJField(DbBEStore.class.getDeclaredField("m_secondEndFlows"));
            fIdentifier.setJField(DbBEStore.class.getDeclaredField("m_identifier"));
            fControl.setJField(DbBEStore.class.getDeclaredField("m_control"));
            fVolume.setJField(DbBEStore.class.getDeclaredField("m_volume"));
            fDbJVClass.setJField(DbBEStore.class.getDeclaredField("m_dbJVClass"));

            fDbJVClass.setOppositeRel(DbJVClass.fDbBEStores);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;
    public static final MetaField[] notationFields = new MetaField[] { fName, fPhysicalName,
            fAlias, fDescription, fVolume, fIdentifier, fUmlStereotype };

    //Instance variables
    DbRelationN m_firstEndFlows;
    DbRelationN m_secondEndFlows;
    SrInteger m_identifier;
    boolean m_control;
    SrInteger m_volume;
    DbJVClass m_dbJVClass;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbBEStore() {
    }

    /**
     * Creates an instance of DbBEStore.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbBEStore(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology term = terminologyUtil.findModelTerminology(getComposite());
        setName(term.getTerm(metaClass));
        setIdentifier(((DbBEModel) getComposite()).getNextStoreIdentifier());
    }

    /**
     * @return int
     **/
    protected final int getFeatureSet() {
        return SMSFilter.BPM;

    }

    //Setters

    /**
     * Sets the "identifier" property of a DbBEStore's instance.
     * 
     * @param value
     *            the "identifier" property
     **/
    public final void setIdentifier(Integer value) throws DbException {
        basicSet(fIdentifier, value);
    }

    /**
     * Sets the "control" property of a DbBEStore's instance.
     * 
     * @param value
     *            the "control" property
     **/
    public final void setControl(Boolean value) throws DbException {
        basicSet(fControl, value);
    }

    /**
     * Sets the "volume" property of a DbBEStore's instance.
     * 
     * @param value
     *            the "volume" property
     **/
    public final void setVolume(Integer value) throws DbException {
        basicSet(fVolume, value);
    }

    /**
     * Sets the class name object associated to a DbBEStore's instance.
     * 
     * @param value
     *            the class name object to be associated
     **/
    public final void setDbJVClass(DbJVClass value) throws DbException {
        basicSet(fDbJVClass, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fFirstEndFlows)
                ((DbBEFlow) value).setFirstEnd(this);
            else if (metaField == fSecondEndFlows)
                ((DbBEFlow) value).setSecondEnd(this);
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
     * Gets the list of flows associated to a DbBEStore's instance.
     * 
     * @return the list of flows.
     **/
    public final DbRelationN getFirstEndFlows() throws DbException {
        return (DbRelationN) get(fFirstEndFlows);
    }

    /**
     * Gets the list of flows associated to a DbBEStore's instance.
     * 
     * @return the list of flows.
     **/
    public final DbRelationN getSecondEndFlows() throws DbException {
        return (DbRelationN) get(fSecondEndFlows);
    }

    /**
     * Gets the "identifier" of a DbBEStore's instance.
     * 
     * @return the "identifier"
     **/
    public final Integer getIdentifier() throws DbException {
        return (Integer) get(fIdentifier);
    }

    /**
     * Gets the "control" property's Boolean value of a DbBEStore's instance.
     * 
     * @return the "control" property's Boolean value
     * @deprecated use isControl() method instead
     **/
    public final Boolean getControl() throws DbException {
        return (Boolean) get(fControl);
    }

    /**
     * Tells whether a DbBEStore's instance is control or not.
     * 
     * @return boolean
     **/
    public final boolean isControl() throws DbException {
        return getControl().booleanValue();
    }

    /**
     * Gets the "volume" of a DbBEStore's instance.
     * 
     * @return the "volume"
     **/
    public final Integer getVolume() throws DbException {
        return (Integer) get(fVolume);
    }

    /**
     * Gets the class name object associated to a DbBEStore's instance.
     * 
     * @return the class name object
     **/
    public final DbJVClass getDbJVClass() throws DbException {
        return (DbJVClass) get(fDbJVClass);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
