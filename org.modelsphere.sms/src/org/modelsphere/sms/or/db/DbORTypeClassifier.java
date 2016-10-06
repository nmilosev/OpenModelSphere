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
import org.modelsphere.sms.or.informix.db.DbINFProcedure;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../../org/modelsphere/sms/or/db/DbORDomain.html">DbORDomain</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/db/DbORBuiltInType.html">DbORBuiltInType</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbORTypeClassifier extends DbSMSTypeClassifier {

    //Meta

    public static final MetaRelationN fTypedAttributes = new MetaRelationN(LocaleMgr.db
            .getString("typedAttributes"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fTypedParameters = new MetaRelationN(LocaleMgr.db
            .getString("typedParameters"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fReturnTypedProcedures = new MetaRelationN(LocaleMgr.db
            .getString("returnTypedProcedures"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fDomains = new MetaRelationN(LocaleMgr.db
            .getString("domains"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fCommonItems = new MetaRelationN(LocaleMgr.db
            .getString("commonItems"), 0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbORTypeClassifier"), DbORTypeClassifier.class, new MetaField[] {
            fTypedAttributes, fTypedParameters, fReturnTypedProcedures, fDomains, fCommonItems }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSTypeClassifier.metaClass);

            fTypedAttributes.setJField(DbORTypeClassifier.class
                    .getDeclaredField("m_typedAttributes"));
            fTypedAttributes.setFlags(MetaField.HUGE_RELN);
            fTypedParameters.setJField(DbORTypeClassifier.class
                    .getDeclaredField("m_typedParameters"));
            fTypedParameters.setFlags(MetaField.HUGE_RELN);
            fReturnTypedProcedures.setJField(DbORTypeClassifier.class
                    .getDeclaredField("m_returnTypedProcedures"));
            fReturnTypedProcedures.setFlags(MetaField.HUGE_RELN);
            fDomains.setJField(DbORTypeClassifier.class.getDeclaredField("m_domains"));
            fDomains.setFlags(MetaField.HUGE_RELN);
            fCommonItems.setJField(DbORTypeClassifier.class.getDeclaredField("m_commonItems"));
            fCommonItems.setFlags(MetaField.HUGE_RELN);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbRelationN m_typedAttributes;
    DbRelationN m_typedParameters;
    DbRelationN m_returnTypedProcedures;
    DbRelationN m_domains;
    DbRelationN m_commonItems;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORTypeClassifier() {
    }

    /**
     * Creates an instance of DbORTypeClassifier.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORTypeClassifier(DbObject composite) throws DbException {
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
            if (metaField == fTypedAttributes)
                ((DbORAttribute) value).setType(this);
            else if (metaField == fTypedParameters)
                ((DbORParameter) value).setType(this);
            else if (metaField == fReturnTypedProcedures)
                ((DbORProcedure) value).setReturnType(this);
            else if (metaField == fDomains)
                ((DbORDomain) value).setSourceType(this);
            else if (metaField == fCommonItems)
                ((DbORCommonItem) value).setType(this);
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
     * Gets the list of typed attributes associated to a DbORTypeClassifier's instance.
     * 
     * @return the list of typed attributes.
     **/
    public final DbRelationN getTypedAttributes() throws DbException {
        return (DbRelationN) get(fTypedAttributes);
    }

    /**
     * Gets the list of typed parameters associated to a DbORTypeClassifier's instance.
     * 
     * @return the list of typed parameters.
     **/
    public final DbRelationN getTypedParameters() throws DbException {
        return (DbRelationN) get(fTypedParameters);
    }

    /**
     * Gets the list of return typed procedures associated to a DbORTypeClassifier's instance.
     * 
     * @return the list of return typed procedures.
     **/
    public final DbRelationN getReturnTypedProcedures() throws DbException {
        return (DbRelationN) get(fReturnTypedProcedures);
    }

    /**
     * Gets the list of domains associated to a DbORTypeClassifier's instance.
     * 
     * @return the list of domains.
     **/
    public final DbRelationN getDomains() throws DbException {
        return (DbRelationN) get(fDomains);
    }

    /**
     * Gets the list of common items associated to a DbORTypeClassifier's instance.
     * 
     * @return the list of common items.
     **/
    public final DbRelationN getCommonItems() throws DbException {
        return (DbRelationN) get(fCommonItems);
    }

}
