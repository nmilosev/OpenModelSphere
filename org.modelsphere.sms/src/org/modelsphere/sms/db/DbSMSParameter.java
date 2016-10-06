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
 * HREF="../../../../org/modelsphere/sms/oo/db/DbOOParameter.html">DbOOParameter</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/db/DbORParameter.html">DbORParameter</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbSMSParameter extends DbSMSTypedElement {

    //Meta

    public static final MetaField fPassingConvention = new MetaField(LocaleMgr.db
            .getString("passingConvention"));
    public static final MetaField fDefaultValue = new MetaField(LocaleMgr.db
            .getString("defaultValue"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbSMSParameter"), DbSMSParameter.class, new MetaField[] {
            fPassingConvention, fDefaultValue }, MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSTypedElement.metaClass);
            metaClass.setIcon("dbsmsparameter.gif");

            fPassingConvention.setJField(DbSMSParameter.class
                    .getDeclaredField("m_passingConvention"));
            fDefaultValue.setJField(DbSMSParameter.class.getDeclaredField("m_defaultValue"));
            fDefaultValue.setVisibleInScreen(false);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    SMSPassingConvention m_passingConvention;
    String m_defaultValue;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSParameter() {
    }

    /**
     * Creates an instance of DbSMSParameter.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbSMSParameter(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setPassingConvention(SMSPassingConvention.getInstance(SMSPassingConvention.IN));
    }

    //Setters

    /**
     * Sets the "passing convention" property of a DbSMSParameter's instance.
     * 
     * @param value
     *            the "passing convention" property
     **/
    public final void setPassingConvention(SMSPassingConvention value) throws DbException {
        basicSet(fPassingConvention, value);
    }

    /**
     * Sets the "default value" property of a DbSMSParameter's instance.
     * 
     * @param value
     *            the "default value" property
     **/
    public final void setDefaultValue(String value) throws DbException {
        basicSet(fDefaultValue, value);
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
     * Gets the "passing convention" of a DbSMSParameter's instance.
     * 
     * @return the "passing convention"
     **/
    public final SMSPassingConvention getPassingConvention() throws DbException {
        return (SMSPassingConvention) get(fPassingConvention);
    }

    /**
     * Gets the "default value" property of a DbSMSParameter's instance.
     * 
     * @return the "default value" property
     **/
    public final String getDefaultValue() throws DbException {
        return (String) get(fDefaultValue);
    }

}
