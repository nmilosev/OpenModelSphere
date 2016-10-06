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
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEUseCase.html">DbBEUseCase</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public final class DbBEUseCaseQualifier extends DbBEQualifierLink {

    //Meta

    public static final MetaRelation1 fQualifier = new MetaRelation1(LocaleMgr.db
            .getString("qualifier"), 1);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbBEUseCaseQualifier"), DbBEUseCaseQualifier.class,
            new MetaField[] { fQualifier }, MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbBEQualifierLink.metaClass);
            metaClass.setIcon("dbbeusecasequalifier.gif");

            fQualifier.setJField(DbBEUseCaseQualifier.class.getDeclaredField("m_qualifier"));
            fQualifier.setFlags(MetaField.COPY_REFS | MetaField.INTEGRABLE_BY_NAME);
            fQualifier.setScreenOrder("<rate");
            fQualifier.setRendererPluginName("DbBEQualifier");
            fQualifier.setEditable(false);

            fQualifier.setOppositeRel(DbBEQualifier.fQualifiedUseCases);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbBEQualifier m_qualifier;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbBEUseCaseQualifier() {
    }

    /**
     * Creates an instance of DbBEUseCaseQualifier.
     * 
     * @param composite
     *            org.modelsphere.jack.baseDb.db.DbObject
     * @param semobj
     *            org.modelsphere.sms.db.DbSMSSemanticalObject
     **/
    public DbBEUseCaseQualifier(DbObject composite, DbSMSSemanticalObject semObj)
            throws DbException {
        super(composite);
        basicSet(fQualifier, semObj);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    /**
     * @param form
     *            int
     * @return string
     **/
    public final String getSemanticalName(int form) throws DbException {
        DbBEQualifier qualifier = this.getQualifier();
        org.modelsphere.sms.be.db.util.BEUtility util = org.modelsphere.sms.be.db.util.BEUtility
                .getSingleInstance();
        String name = util.getSemanticalName(form, qualifier, getRate2());
        return name;
    }

    /**
     * @return string
     **/
    public final String getName() throws DbException {
        return getSemanticalName(0);
    }

    //Setters

    /**
     * Sets the qualifier object associated to a DbBEUseCaseQualifier's instance.
     * 
     * @param value
     *            the qualifier object to be associated
     **/
    public final void setQualifier(DbBEQualifier value) throws DbException {
        basicSet(fQualifier, value);
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
     * Gets the qualifier object associated to a DbBEUseCaseQualifier's instance.
     * 
     * @return the qualifier object
     **/
    public final DbBEQualifier getQualifier() throws DbException {
        return (DbBEQualifier) get(fQualifier);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
