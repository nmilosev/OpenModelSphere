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
 * HREF="../../../../org/modelsphere/sms/oo/db/DbOOAssociationEnd.html">DbOOAssociationEnd</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/db/DbORAssociationEnd.html">DbORAssociationEnd</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbSMSAssociationEnd extends DbSMSAbstractRelationshipEnd {

    //Meta

    public static final MetaField fMultiplicity = new MetaField(LocaleMgr.db
            .getString("multiplicity"));
    public static final MetaField fSpecificRangeMultiplicity = new MetaField(LocaleMgr.db
            .getString("specificRangeMultiplicity"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbSMSAssociationEnd"), DbSMSAssociationEnd.class, new MetaField[] {
            fMultiplicity, fSpecificRangeMultiplicity }, MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSAbstractRelationshipEnd.metaClass);

            fMultiplicity.setJField(DbSMSAssociationEnd.class.getDeclaredField("m_multiplicity"));
            fSpecificRangeMultiplicity.setJField(DbSMSAssociationEnd.class
                    .getDeclaredField("m_specificRangeMultiplicity"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    SMSMultiplicity m_multiplicity;
    String m_specificRangeMultiplicity;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSAssociationEnd() {
    }

    /**
     * Creates an instance of DbSMSAssociationEnd.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbSMSAssociationEnd(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    /**
     * @param mult
     *            org.modelsphere.sms.db.srtypes.SMSMultiplicity
     **/
    public void setMultiplicity(SMSMultiplicity mult) throws DbException {
        basicSet(fMultiplicity, mult);
    }

    //Setters

    /**
     * Sets the "specific-range multiplicity" property of a DbSMSAssociationEnd's instance.
     * 
     * @param value
     *            the "specific-range multiplicity" property
     **/
    public final void setSpecificRangeMultiplicity(String value) throws DbException {
        basicSet(fSpecificRangeMultiplicity, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fMultiplicity)
                setMultiplicity((SMSMultiplicity) value);
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
     * Gets the "multiplicity" of a DbSMSAssociationEnd's instance.
     * 
     * @return the "multiplicity"
     **/
    public final SMSMultiplicity getMultiplicity() throws DbException {
        return (SMSMultiplicity) get(fMultiplicity);
    }

    /**
     * Gets the "specific-range multiplicity" property of a DbSMSAssociationEnd's instance.
     * 
     * @return the "specific-range multiplicity" property
     **/
    public final String getSpecificRangeMultiplicity() throws DbException {
        return (String) get(fSpecificRangeMultiplicity);
    }

}
