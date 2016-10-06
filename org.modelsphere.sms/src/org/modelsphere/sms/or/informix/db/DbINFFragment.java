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
package org.modelsphere.sms.or.informix.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.informix.db.srtypes.*;
import org.modelsphere.sms.or.informix.international.LocaleMgr;
import org.modelsphere.sms.or.db.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFIndex.html">DbINFIndex</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFTable.html">DbINFTable</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public final class DbINFFragment extends DbObject {

    //Meta

    public static final MetaField fExpression = new MetaField(LocaleMgr.db.getString("expression"));
    public static final MetaRelation1 fDbspace = new MetaRelation1(LocaleMgr.db
            .getString("dbspace"), 0);
    public static final MetaField fRemainder = new MetaField(LocaleMgr.db.getString("remainder"));

    public static final MetaClass metaClass = new MetaClass(
            LocaleMgr.db.getString("DbINFFragment"), DbINFFragment.class, new MetaField[] {
                    fExpression, fDbspace, fRemainder }, MetaClass.NO_UDF
                    | MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbObject.metaClass);

            fExpression.setJField(DbINFFragment.class.getDeclaredField("m_expression"));
            fDbspace.setJField(DbINFFragment.class.getDeclaredField("m_dbspace"));
            fDbspace.setFlags(MetaField.INTEGRABLE_BY_NAME);
            fDbspace.setRendererPluginName("DbINFDbspace");
            fRemainder.setJField(DbINFFragment.class.getDeclaredField("m_remainder"));

            fDbspace.setOppositeRel(DbINFDbspace.fFragments);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    String m_expression;
    DbINFDbspace m_dbspace;
    boolean m_remainder;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbINFFragment() {
    }

    /**
     * Creates an instance of DbINFFragment.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbINFFragment(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setRemainder(Boolean.FALSE);
    }

    //Setters

    /**
     * Sets the "expression" property of a DbINFFragment's instance.
     * 
     * @param value
     *            the "expression" property
     **/
    public final void setExpression(String value) throws DbException {
        basicSet(fExpression, value);
    }

    /**
     * Sets the dbspace object associated to a DbINFFragment's instance.
     * 
     * @param value
     *            the dbspace object to be associated
     **/
    public final void setDbspace(DbINFDbspace value) throws DbException {
        basicSet(fDbspace, value);
    }

    /**
     * Sets the "remainder" property of a DbINFFragment's instance.
     * 
     * @param value
     *            the "remainder" property
     **/
    public final void setRemainder(Boolean value) throws DbException {
        basicSet(fRemainder, value);
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
     * Gets the "expression" property of a DbINFFragment's instance.
     * 
     * @return the "expression" property
     **/
    public final String getExpression() throws DbException {
        return (String) get(fExpression);
    }

    /**
     * Gets the dbspace object associated to a DbINFFragment's instance.
     * 
     * @return the dbspace object
     **/
    public final DbINFDbspace getDbspace() throws DbException {
        return (DbINFDbspace) get(fDbspace);
    }

    /**
     * Gets the "remainder" property's Boolean value of a DbINFFragment's instance.
     * 
     * @return the "remainder" property's Boolean value
     * @deprecated use isRemainder() method instead
     **/
    public final Boolean getRemainder() throws DbException {
        return (Boolean) get(fRemainder);
    }

    /**
     * Tells whether a DbINFFragment's instance is remainder or not.
     * 
     * @return boolean
     **/
    public final boolean isRemainder() throws DbException {
        return getRemainder().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
