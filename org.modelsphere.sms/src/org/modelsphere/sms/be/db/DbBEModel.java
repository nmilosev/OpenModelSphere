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
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSProject.html">DbSMSProject</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSUserDefinedPackage.html"
 * >DbSMSUserDefinedPackage</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEUseCase.html">DbBEUseCase</A>, <A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEActor.html">DbBEActor</A>, <A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEStore.html">DbBEStore</A>, <A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEQualifier.html">DbBEQualifier</A>, <A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEResource.html">DbBEResource</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSNotice.html">DbSMSNotice</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbBEModel extends DbSMSPackage {

    //Meta

    public static final MetaField fIsLocked = new MetaField(LocaleMgr.db.getString("isLocked"));
    public static final MetaField fTerminologyName = new MetaField(LocaleMgr.db
            .getString("terminologyName"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbBEModel"),
            DbBEModel.class, new MetaField[] { fIsLocked, fTerminologyName }, MetaClass.ACCESS_CTL
                    | MetaClass.CLUSTER_ROOT | MetaClass.NAMING_ROOT | MetaClass.MATCHABLE
                    | MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSPackage.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbBEUseCase.metaClass,
                    DbBEActor.metaClass, DbBEStore.metaClass, DbBEQualifier.metaClass,
                    DbBEResource.metaClass });
            metaClass.setIcon("dbbemodel.gif");

            fIsLocked.setJField(DbBEModel.class.getDeclaredField("m_isLocked"));
            fIsLocked.setVisibleInScreen(false);
            fTerminologyName.setJField(DbBEModel.class.getDeclaredField("m_terminologyName"));
            fTerminologyName.setEditable(false);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    boolean m_isLocked;
    String m_terminologyName;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbBEModel() {
    }

    /**
     * Creates an instance of DbBEModel.
     * 
     * @param composite
     *            org.modelsphere.jack.baseDb.db.DbObject
     **/
    public DbBEModel(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setName(LocaleMgr.misc.getString("bpmmodel"));
    }

    /**
     * @param metareln
     *            org.modelsphere.jack.baseDb.meta.MetaRelationN
     * @return boolean
     **/
    public final boolean isHugeRelN(MetaRelationN metaRelN) {
        if (metaRelN == DbObject.fComponents)
            return true;
        return super.isHugeRelN(metaRelN);
    }

    /**
     * @return int
     **/
    protected final int getFeatureSet() {
        return SMSFilter.BPM;

    }

    /**
     * @param numerichierid
     *            java.lang.String
     * @return dbobject
     **/
    public DbObject findUseCaseComponentByNumericHierID(String numericHierId) throws DbException {
        DbBEUseCase childFound = null;
        DbEnumeration dbEnum = componentTree(DbBEUseCase.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEUseCase child = (DbBEUseCase) dbEnum.nextElement();
            if (numericHierId.equals(child.getNumericHierID())) {
                childFound = child;
                break;
            }
        }
        dbEnum.close();
        return childFound;
    }

    /**
     * @param alphanumerichierid
     *            java.lang.String
     * @return dbobject
     **/
    public DbObject findUseCaseComponentByAlphanumericHierID(String alphanumericHierId)
            throws DbException {
        DbBEUseCase childFound = null;
        DbEnumeration dbEnum = componentTree(DbBEUseCase.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEUseCase child = (DbBEUseCase) dbEnum.nextElement();
            if (alphanumericHierId.equals(child.getSemanticalName(DbObject.LONG_FORM))) {
                childFound = child;
                break;
            }
        }
        dbEnum.close();
        return childFound;
    }

    /**
     * @return integer
     **/
    protected Integer getNextStoreIdentifier() throws DbException {
        int maxId = 0;
        DbEnumeration dbEnum = getComponents().elements(DbBEStore.metaClass);
        while (dbEnum.hasMoreElements()) {
            Integer intValue = ((DbBEStore) dbEnum.nextElement()).getIdentifier();
            if ((intValue != null) && (intValue.intValue() > maxId))
                maxId = intValue.intValue();
        }
        dbEnum.close();
        return new Integer(maxId + 1);
    }

    /**
     * @return integer
     **/
    protected Integer getNextActorIdentifier() throws DbException {
        int maxId = 0;
        DbEnumeration dbEnum = getComponents().elements(DbBEActor.metaClass);
        while (dbEnum.hasMoreElements()) {
            Integer intValue = ((DbBEActor) dbEnum.nextElement()).getIdentifier();
            if ((intValue != null) && (intValue.intValue() > maxId))
                maxId = intValue.intValue();
        }
        dbEnum.close();
        return new Integer(maxId + 1);
    }

    /**
     * @param metafield
     *            org.modelsphere.jack.baseDb.meta.MetaField
     * @param value
     *            java.lang.Object
     **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (value instanceof DbSMSNotation)
            basicSet(metaField, ((DbSMSNotation) value).getName());
        else if (metaField.getMetaClass() == metaClass) {
            basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    //Setters

    /**
     * Sets the "locked" property of a DbBEModel's instance.
     * 
     * @param value
     *            the "locked" property
     **/
    public final void setIsLocked(Boolean value) throws DbException {
        basicSet(fIsLocked, value);
    }

    /**
     * Sets the "terminology" property of a DbBEModel's instance.
     * 
     * @param value
     *            the "terminology" property
     **/
    public final void setTerminologyName(String value) throws DbException {
        basicSet(fTerminologyName, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the "locked" property's Boolean value of a DbBEModel's instance.
     * 
     * @return the "locked" property's Boolean value
     * @deprecated use isIsLocked() method instead
     **/
    public final Boolean getIsLocked() throws DbException {
        return (Boolean) get(fIsLocked);
    }

    /**
     * Tells whether a DbBEModel's instance is isLocked or not.
     * 
     * @return boolean
     **/
    public final boolean isIsLocked() throws DbException {
        return getIsLocked().booleanValue();
    }

    /**
     * Gets the "terminology" property of a DbBEModel's instance.
     * 
     * @return the "terminology" property
     **/
    public final String getTerminologyName() throws DbException {
        return (String) get(fTerminologyName);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
