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
package org.modelsphere.sms.oo.java.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.oo.java.db.srtypes.*;
import org.modelsphere.sms.oo.java.international.LocaleMgr;
import org.modelsphere.sms.oo.db.*;
import org.modelsphere.sms.oo.db.srtypes.*;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.SMSFilter;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/oo/db/DbOOAssociation.html">DbOOAssociation</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbJVAssociationEnd extends DbOOAssociationEnd {

    //Meta

    public static final MetaField fOrdered = new MetaField(LocaleMgr.db.getString("ordered"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbJVAssociationEnd"), DbJVAssociationEnd.class,
            new MetaField[] { fOrdered }, MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbOOAssociationEnd.metaClass);

            fOrdered.setJField(DbJVAssociationEnd.class.getDeclaredField("m_ordered"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    boolean m_ordered;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbJVAssociationEnd() {
    }

    /**
     * Creates an instance of DbJVAssociationEnd.
     * 
     * @param composite
     *            org.modelsphere.sms.oo.java.db.DbJVAssociation
     * @param associationmember
     *            org.modelsphere.sms.oo.java.db.DbJVDataMember
     * @param multiplicity
     *            org.modelsphere.sms.db.srtypes.SMSMultiplicity
     **/
    protected DbJVAssociationEnd(DbJVAssociation composite, DbJVDataMember associationMember,
            SMSMultiplicity multiplicity) throws DbException {
        super(composite, associationMember, multiplicity);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    /**
     * @param mult
     *            org.modelsphere.sms.db.srtypes.SMSMultiplicity
     **/
    public final void setMultiplicity(SMSMultiplicity mult) throws DbException {
        super.setMultiplicity(mult);
        DbJVDataMember member = (DbJVDataMember) getAssociationMember();
        DbOOAdt type = member.getType();
        if (!(type instanceof DbJVClass))
            return;
        if (mult.getValue() == SMSMultiplicity.EXACTLY_ONE
                || mult.getValue() == SMSMultiplicity.OPTIONAL) {
            if (((DbJVClass) type).isCollection()) {
                DbOOAdt elementType = member.getElementType();
                if (elementType != null)
                    member.setType(elementType);
            }
            member.setElementType(null);
            member.setTypeUse(null);
        } else {
            if (!((DbJVClass) type).isCollection()
                    && DbObject.valuesAreEqual(member.getTypeUse(), null)) {
                DbOOAdt collType = ((DbSMSProject) member.getProject()).getDefaultCollectionType();
                if (collType != null) {
                    member.setType(collType);
                    member.setElementType(type);
                } else
                    member.setTypeUse(DbJVDataMember.ARRAY_TYPE_USE);
            }
        }
    }

    /**
     * @return boolean
     **/
    public final boolean hasWriteAccess() throws DbException {
        if (writeAccess == 0) {
            writeAccess = (getAssociationMember().hasWriteAccess() ? ACCESS_GRANTED
                    : ACCESS_NOT_GRANTED);
        }
        return (writeAccess == ACCESS_GRANTED);
    }

    /**
     * @return int
     **/
    protected final int getFeatureSet() {
        return SMSFilter.JAVA;

    }

    //Setters

    /**
     * Sets the "ordered" property of a DbJVAssociationEnd's instance.
     * 
     * @param value
     *            the "ordered" property
     **/
    public final void setOrdered(Boolean value) throws DbException {
        basicSet(fOrdered, value);
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
     * Gets the "ordered" property's Boolean value of a DbJVAssociationEnd's instance.
     * 
     * @return the "ordered" property's Boolean value
     * @deprecated use isOrdered() method instead
     **/
    public final Boolean getOrdered() throws DbException {
        return (Boolean) get(fOrdered);
    }

    /**
     * Tells whether a DbJVAssociationEnd's instance is ordered or not.
     * 
     * @return boolean
     **/
    public final boolean isOrdered() throws DbException {
        return getOrdered().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
