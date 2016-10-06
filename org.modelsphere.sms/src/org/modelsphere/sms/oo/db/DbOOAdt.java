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
package org.modelsphere.sms.oo.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.oo.db.srtypes.*;
import org.modelsphere.sms.oo.international.LocaleMgr;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORDomain;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../../org/modelsphere/sms/oo/db/DbOOClass.html">DbOOClass</A>, <A
 * HREF="../../../../../org/modelsphere/sms/oo/db/DbOOPrimitiveType.html">DbOOPrimitiveType</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbOOAdt extends DbSMSTypeClassifier {

    //Meta

    public static final MetaRelationN fTypedDataMembers = new MetaRelationN(LocaleMgr.db
            .getString("typedDataMembers"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fTypedMethods = new MetaRelationN(LocaleMgr.db
            .getString("typedMethods"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fTypedParameters = new MetaRelationN(LocaleMgr.db
            .getString("typedParameters"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fTypedElementDataMembers = new MetaRelationN(LocaleMgr.db
            .getString("typedElementDataMembers"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fTypedElementMethods = new MetaRelationN(LocaleMgr.db
            .getString("typedElementMethods"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fTypedElementParameters = new MetaRelationN(LocaleMgr.db
            .getString("typedElementParameters"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fCommonItems = new MetaRelationN(LocaleMgr.db
            .getString("commonItems"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fUsedByDomains = new MetaRelationN(LocaleMgr.db
            .getString("usedByDomains"), 0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbOOAdt"),
            DbOOAdt.class, new MetaField[] { fTypedDataMembers, fTypedMethods, fTypedParameters,
                    fTypedElementDataMembers, fTypedElementMethods, fTypedElementParameters,
                    fCommonItems, fUsedByDomains }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSTypeClassifier.metaClass);

            fTypedDataMembers.setJField(DbOOAdt.class.getDeclaredField("m_typedDataMembers"));
            fTypedDataMembers.setFlags(MetaField.HUGE_RELN);
            fTypedMethods.setJField(DbOOAdt.class.getDeclaredField("m_typedMethods"));
            fTypedMethods.setFlags(MetaField.HUGE_RELN);
            fTypedParameters.setJField(DbOOAdt.class.getDeclaredField("m_typedParameters"));
            fTypedParameters.setFlags(MetaField.HUGE_RELN);
            fTypedElementDataMembers.setJField(DbOOAdt.class
                    .getDeclaredField("m_typedElementDataMembers"));
            fTypedElementMethods.setJField(DbOOAdt.class.getDeclaredField("m_typedElementMethods"));
            fTypedElementParameters.setJField(DbOOAdt.class
                    .getDeclaredField("m_typedElementParameters"));
            fCommonItems.setJField(DbOOAdt.class.getDeclaredField("m_commonItems"));
            fUsedByDomains.setJField(DbOOAdt.class.getDeclaredField("m_usedByDomains"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbRelationN m_typedDataMembers;
    DbRelationN m_typedMethods;
    DbRelationN m_typedParameters;
    DbRelationN m_typedElementDataMembers;
    DbRelationN m_typedElementMethods;
    DbRelationN m_typedElementParameters;
    DbRelationN m_commonItems;
    DbRelationN m_usedByDomains;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbOOAdt() {
    }

    /**
     * Creates an instance of DbOOAdt.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbOOAdt(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    //Setters

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fTypedDataMembers)
                ((DbOODataMember) value).setType(this);
            else if (metaField == fTypedMethods)
                ((DbOOMethod) value).setReturnType(this);
            else if (metaField == fTypedParameters)
                ((DbOOParameter) value).setType(this);
            else if (metaField == fTypedElementDataMembers)
                ((DbOODataMember) value).setElementType(this);
            else if (metaField == fTypedElementMethods)
                ((DbOOMethod) value).setReturnElementType(this);
            else if (metaField == fTypedElementParameters)
                ((DbOOParameter) value).setElementType(this);
            else if (metaField == fCommonItems)
                ((DbORCommonItem) value).setOoType(this);
            else if (metaField == fUsedByDomains)
                ((DbORDomain) value).setOoSourceType(this);
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
     * Gets the list of typed fields associated to a DbOOAdt's instance.
     * 
     * @return the list of typed fields.
     **/
    public final DbRelationN getTypedDataMembers() throws DbException {
        return (DbRelationN) get(fTypedDataMembers);
    }

    /**
     * Gets the list of typed methods associated to a DbOOAdt's instance.
     * 
     * @return the list of typed methods.
     **/
    public final DbRelationN getTypedMethods() throws DbException {
        return (DbRelationN) get(fTypedMethods);
    }

    /**
     * Gets the list of typed parameters associated to a DbOOAdt's instance.
     * 
     * @return the list of typed parameters.
     **/
    public final DbRelationN getTypedParameters() throws DbException {
        return (DbRelationN) get(fTypedParameters);
    }

    /**
     * Gets the list of typed field elements associated to a DbOOAdt's instance.
     * 
     * @return the list of typed field elements.
     **/
    public final DbRelationN getTypedElementDataMembers() throws DbException {
        return (DbRelationN) get(fTypedElementDataMembers);
    }

    /**
     * Gets the list of typed elements methods associated to a DbOOAdt's instance.
     * 
     * @return the list of typed elements methods.
     **/
    public final DbRelationN getTypedElementMethods() throws DbException {
        return (DbRelationN) get(fTypedElementMethods);
    }

    /**
     * Gets the list of typed element parameters associated to a DbOOAdt's instance.
     * 
     * @return the list of typed element parameters.
     **/
    public final DbRelationN getTypedElementParameters() throws DbException {
        return (DbRelationN) get(fTypedElementParameters);
    }

    /**
     * Gets the list of common items associated to a DbOOAdt's instance.
     * 
     * @return the list of common items.
     **/
    public final DbRelationN getCommonItems() throws DbException {
        return (DbRelationN) get(fCommonItems);
    }

    /**
     * Gets the list of used by domains associated to a DbOOAdt's instance.
     * 
     * @return the list of used by domains.
     **/
    public final DbRelationN getUsedByDomains() throws DbException {
        return (DbRelationN) get(fUsedByDomains);
    }

}
