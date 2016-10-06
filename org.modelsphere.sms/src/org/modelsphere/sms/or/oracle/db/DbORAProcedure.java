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
package org.modelsphere.sms.or.oracle.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.oracle.db.srtypes.*;
import org.modelsphere.sms.or.oracle.international.LocaleMgr;
import org.modelsphere.sms.or.db.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORAProgramUnitLibrary.html"
 * >DbORAProgramUnitLibrary</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORAParameter.html">DbORAParameter</A>,
 * <A HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbORAProcedure extends DbORProcedure {

    //Meta

    public static final MetaField fInvokerRights = new MetaField(LocaleMgr.db
            .getString("invokerRights"));
    public static final MetaField fDeterministic = new MetaField(LocaleMgr.db
            .getString("deterministic"));
    public static final MetaField fParallel = new MetaField(LocaleMgr.db.getString("parallel"));
    public static final MetaField fLanguage = new MetaField(LocaleMgr.db.getString("language"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbORAProcedure"), DbORAProcedure.class, new MetaField[] { fInvokerRights,
            fDeterministic, fParallel, fLanguage }, MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORProcedure.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbORAParameter.metaClass });

            fInvokerRights.setJField(DbORAProcedure.class.getDeclaredField("m_invokerRights"));
            fInvokerRights.setScreenOrder("<javaMethod");
            fDeterministic.setJField(DbORAProcedure.class.getDeclaredField("m_deterministic"));
            fParallel.setJField(DbORAProcedure.class.getDeclaredField("m_parallel"));
            fLanguage.setJField(DbORAProcedure.class.getDeclaredField("m_language"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    ORAInvokerRights m_invokerRights;
    boolean m_deterministic;
    boolean m_parallel;
    ORALanguage m_language;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORAProcedure() {
    }

    /**
     * Creates an instance of DbORAProcedure.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORAProcedure(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setDeterministic(Boolean.FALSE);
        setParallel(Boolean.FALSE);
        setName(LocaleMgr.misc.getString("procedure"));
    }

    //Setters

    /**
     * Sets the "invoker rights" property of a DbORAProcedure's instance.
     * 
     * @param value
     *            the "invoker rights" property
     **/
    public final void setInvokerRights(ORAInvokerRights value) throws DbException {
        basicSet(fInvokerRights, value);
    }

    /**
     * Sets the "deterministic" property of a DbORAProcedure's instance.
     * 
     * @param value
     *            the "deterministic" property
     **/
    public final void setDeterministic(Boolean value) throws DbException {
        basicSet(fDeterministic, value);
    }

    /**
     * Sets the "parallel" property of a DbORAProcedure's instance.
     * 
     * @param value
     *            the "parallel" property
     **/
    public final void setParallel(Boolean value) throws DbException {
        basicSet(fParallel, value);
    }

    /**
     * Sets the "language" property of a DbORAProcedure's instance.
     * 
     * @param value
     *            the "language" property
     **/
    public final void setLanguage(ORALanguage value) throws DbException {
        basicSet(fLanguage, value);
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
     * Gets the "invoker rights" of a DbORAProcedure's instance.
     * 
     * @return the "invoker rights"
     **/
    public final ORAInvokerRights getInvokerRights() throws DbException {
        return (ORAInvokerRights) get(fInvokerRights);
    }

    /**
     * Gets the "deterministic" property's Boolean value of a DbORAProcedure's instance.
     * 
     * @return the "deterministic" property's Boolean value
     * @deprecated use isDeterministic() method instead
     **/
    public final Boolean getDeterministic() throws DbException {
        return (Boolean) get(fDeterministic);
    }

    /**
     * Tells whether a DbORAProcedure's instance is deterministic or not.
     * 
     * @return boolean
     **/
    public final boolean isDeterministic() throws DbException {
        return getDeterministic().booleanValue();
    }

    /**
     * Gets the "parallel" property's Boolean value of a DbORAProcedure's instance.
     * 
     * @return the "parallel" property's Boolean value
     * @deprecated use isParallel() method instead
     **/
    public final Boolean getParallel() throws DbException {
        return (Boolean) get(fParallel);
    }

    /**
     * Tells whether a DbORAProcedure's instance is parallel or not.
     * 
     * @return boolean
     **/
    public final boolean isParallel() throws DbException {
        return getParallel().booleanValue();
    }

    /**
     * Gets the "language" of a DbORAProcedure's instance.
     * 
     * @return the "language"
     **/
    public final ORALanguage getLanguage() throws DbException {
        return (ORALanguage) get(fLanguage);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
