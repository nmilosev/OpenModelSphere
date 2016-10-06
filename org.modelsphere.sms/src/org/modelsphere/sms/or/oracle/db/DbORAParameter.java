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
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORAProcedure.html">DbORAProcedure</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbORAParameter extends DbORParameter {

    //Meta

    public static final MetaField fNocopy = new MetaField(LocaleMgr.db.getString("nocopy"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbORAParameter"), DbORAParameter.class, new MetaField[] { fNocopy }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORParameter.metaClass);

            fNocopy.setJField(DbORAParameter.class.getDeclaredField("m_nocopy"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    boolean m_nocopy;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORAParameter() {
    }

    /**
     * Creates an instance of DbORAParameter.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORAParameter(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setNocopy(Boolean.FALSE);
        setName(LocaleMgr.misc.getString("parameter"));
    }

    //Setters

    /**
     * Sets the "no copy" property of a DbORAParameter's instance.
     * 
     * @param value
     *            the "no copy" property
     **/
    public final void setNocopy(Boolean value) throws DbException {
        basicSet(fNocopy, value);
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
     * Gets the "no copy" property's Boolean value of a DbORAParameter's instance.
     * 
     * @return the "no copy" property's Boolean value
     * @deprecated use isNocopy() method instead
     **/
    public final Boolean getNocopy() throws DbException {
        return (Boolean) get(fNocopy);
    }

    /**
     * Tells whether a DbORAParameter's instance is nocopy or not.
     * 
     * @return boolean
     **/
    public final boolean isNocopy() throws DbException {
        return getNocopy().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
