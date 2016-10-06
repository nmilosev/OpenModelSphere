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
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFProcedure.html"
 * >DbINFProcedure</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbINFParameter extends DbORParameter {

    //Meta

    public static final MetaField fDefaultNull = new MetaField(LocaleMgr.db
            .getString("defaultNull"));
    public static final MetaField fReturn = new MetaField(LocaleMgr.db.getString("return"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbINFParameter"), DbINFParameter.class, new MetaField[] { fDefaultNull,
            fReturn }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORParameter.metaClass);

            fDefaultNull.setJField(DbINFParameter.class.getDeclaredField("m_defaultNull"));
            fReturn.setJField(DbINFParameter.class.getDeclaredField("m_return"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    boolean m_defaultNull;
    boolean m_return;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbINFParameter() {
    }

    /**
     * Creates an instance of DbINFParameter.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbINFParameter(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setDefaultNull(Boolean.FALSE);
        setReturn(Boolean.FALSE);
        setName(LocaleMgr.misc.getString("parameter"));
    }

    //Setters

    /**
     * Sets the "default null" property of a DbINFParameter's instance.
     * 
     * @param value
     *            the "default null" property
     **/
    public final void setDefaultNull(Boolean value) throws DbException {
        basicSet(fDefaultNull, value);
    }

    /**
     * Sets the "return" property of a DbINFParameter's instance.
     * 
     * @param value
     *            the "return" property
     **/
    public final void setReturn(Boolean value) throws DbException {
        basicSet(fReturn, value);
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
     * Gets the "default null" property's Boolean value of a DbINFParameter's instance.
     * 
     * @return the "default null" property's Boolean value
     * @deprecated use isDefaultNull() method instead
     **/
    public final Boolean getDefaultNull() throws DbException {
        return (Boolean) get(fDefaultNull);
    }

    /**
     * Tells whether a DbINFParameter's instance is defaultNull or not.
     * 
     * @return boolean
     **/
    public final boolean isDefaultNull() throws DbException {
        return getDefaultNull().booleanValue();
    }

    /**
     * Gets the "return" property's Boolean value of a DbINFParameter's instance.
     * 
     * @return the "return" property's Boolean value
     * @deprecated use isReturn() method instead
     **/
    public final Boolean getReturn() throws DbException {
        return (Boolean) get(fReturn);
    }

    /**
     * Tells whether a DbINFParameter's instance is return or not.
     * 
     * @return boolean
     **/
    public final boolean isReturn() throws DbException {
        return getReturn().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
