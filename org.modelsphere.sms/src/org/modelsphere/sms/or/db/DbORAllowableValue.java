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
package org.modelsphere.sms.or.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.jack.baseDb.util.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/or/db/DbORDomain.html">DbORDomain</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public final class DbORAllowableValue extends DbObject {

    //Meta

    public static final MetaField fValue = new MetaField(LocaleMgr.db.getString("value"));
    public static final MetaField fDefinition = new MetaField(LocaleMgr.db.getString("definition"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbORAllowableValue"), DbORAllowableValue.class, new MetaField[] { fValue,
            fDefinition }, MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbObject.metaClass);
            metaClass.setIcon("dballowablevalue.gif");

            fValue.setJField(DbORAllowableValue.class.getDeclaredField("m_value"));
            fDefinition.setJField(DbORAllowableValue.class.getDeclaredField("m_definition"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    String m_value;
    String m_definition;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORAllowableValue() {
    }

    /**
     * Creates an instance of DbORAllowableValue.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORAllowableValue(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        DbORDataModel dataModel = (DbORDataModel) getCompositeOfType(DbORDataModel.metaClass);
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology term = terminologyUtil.findModelTerminology(dataModel);
        setName(term.getTerm(metaClass));
    }

    /**
     * @return string
     **/
    public final String getName() throws DbException {
        return getValue();
    }

    //Setters

    /**
     * Sets the "value" property of a DbORAllowableValue's instance.
     * 
     * @param value
     *            the "value" property
     **/
    public final void setValue(String value) throws DbException {
        basicSet(fValue, value);
    }

    /**
     * Sets the "definition" property of a DbORAllowableValue's instance.
     * 
     * @param value
     *            the "definition" property
     **/
    public final void setDefinition(String value) throws DbException {
        basicSet(fDefinition, value);
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
     * Gets the "value" property of a DbORAllowableValue's instance.
     * 
     * @return the "value" property
     **/
    public final String getValue() throws DbException {
        return (String) get(fValue);
    }

    /**
     * Gets the "definition" property of a DbORAllowableValue's instance.
     * 
     * @return the "definition" property
     **/
    public final String getDefinition() throws DbException {
        return (String) get(fDefinition);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
