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

/**
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../../org/modelsphere/sms/or/oracle/db/DbORADatabase.html">DbORADatabase</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/ibm/db/DbIBMDatabase.html">DbIBMDatabase</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/informix/db/DbINFDatabase.html">DbINFDatabase</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/generic/db/DbGEDatabase.html">DbGEDatabase</A>.<br>
 * 
 * <b>Composites : </b><A HREF="../../../../../org/modelsphere/sms/db/DbSMSUserDefinedPackage.html">
 * DbSMSUserDefinedPackage</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSProject.html">DbSMSProject</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSLinkModel.html">DbSMSLinkModel</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSNotice.html">DbSMSNotice</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbORDatabase extends DbORModel {

    //Meta

    public static final MetaRelation1 fSchema = new MetaRelation1(LocaleMgr.db.getString("schema"),
            0);
    public static final MetaRelation1 fOperationLibrary = new MetaRelation1(LocaleMgr.db
            .getString("operationLibrary"), 0);
    public static final MetaRelation1 fUdtModel = new MetaRelation1(LocaleMgr.db
            .getString("udtModel"), 0);
    public static final MetaRelation1 fDefaultDomainModel = new MetaRelation1(LocaleMgr.db
            .getString("defaultDomainModel"), 0);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbORDatabase"),
            DbORDatabase.class, new MetaField[] { fSchema, fOperationLibrary, fUdtModel,
                    fDefaultDomainModel }, MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORModel.metaClass);
            metaClass.setIcon("dbordatabase.gif");

            fSchema.setJField(DbORDatabase.class.getDeclaredField("m_schema"));
            fSchema.setFlags(MetaField.INTEGRABLE_BY_NAME);
            fOperationLibrary.setJField(DbORDatabase.class.getDeclaredField("m_operationLibrary"));
            fOperationLibrary.setFlags(MetaField.INTEGRABLE_BY_NAME);
            fUdtModel.setJField(DbORDatabase.class.getDeclaredField("m_udtModel"));
            fUdtModel.setFlags(MetaField.INTEGRABLE_BY_NAME);
            fUdtModel.setRendererPluginName("DbSemanticalObject");
            fDefaultDomainModel.setJField(DbORDatabase.class
                    .getDeclaredField("m_defaultDomainModel"));
            fDefaultDomainModel.setFlags(MetaField.INTEGRABLE_BY_NAME);
            fDefaultDomainModel.setRendererPluginName("DbSemanticalObject");

            fSchema.setOppositeRel(DbORDataModel.fDeploymentDatabase);
            fOperationLibrary.setOppositeRel(DbOROperationLibrary.fDeploymentDatabase);
            fUdtModel.setOppositeRel(DbORDomainModel.fDeploymentDatabase);
            fDefaultDomainModel.setOppositeRel(DbORDomainModel.fDatabasesDefaultDomains);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbORDataModel m_schema;
    DbOROperationLibrary m_operationLibrary;
    DbORDomainModel m_udtModel;
    DbORDomainModel m_defaultDomainModel;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORDatabase() {
    }

    /**
     * Creates an instance of DbORDatabase.
     * 
     * @param composite
     *            org.modelsphere.jack.baseDb.db.DbObject
     * @param targetsystem
     *            org.modelsphere.sms.db.DbSMSTargetSystem
     **/
    public DbORDatabase(DbObject composite, DbSMSTargetSystem targetSystem) throws DbException {
        super(composite, targetSystem);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    //Setters

    /**
     * Sets the schema object associated to a DbORDatabase's instance.
     * 
     * @param value
     *            the schema object to be associated
     **/
    public final void setSchema(DbORDataModel value) throws DbException {
        basicSet(fSchema, value);
    }

    /**
     * Sets the operation library object associated to a DbORDatabase's instance.
     * 
     * @param value
     *            the operation library object to be associated
     **/
    public final void setOperationLibrary(DbOROperationLibrary value) throws DbException {
        basicSet(fOperationLibrary, value);
    }

    /**
     * Sets the user-defined types model object associated to a DbORDatabase's instance.
     * 
     * @param value
     *            the user-defined types model object to be associated
     **/
    public final void setUdtModel(DbORDomainModel value) throws DbException {
        basicSet(fUdtModel, value);
    }

    /**
     * Sets the default domain model object associated to a DbORDatabase's instance.
     * 
     * @param value
     *            the default domain model object to be associated
     **/
    public final void setDefaultDomainModel(DbORDomainModel value) throws DbException {
        basicSet(fDefaultDomainModel, value);
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
     * Gets the schema object associated to a DbORDatabase's instance.
     * 
     * @return the schema object
     **/
    public final DbORDataModel getSchema() throws DbException {
        return (DbORDataModel) get(fSchema);
    }

    /**
     * Gets the operation library object associated to a DbORDatabase's instance.
     * 
     * @return the operation library object
     **/
    public final DbOROperationLibrary getOperationLibrary() throws DbException {
        return (DbOROperationLibrary) get(fOperationLibrary);
    }

    /**
     * Gets the user-defined types model object associated to a DbORDatabase's instance.
     * 
     * @return the user-defined types model object
     **/
    public final DbORDomainModel getUdtModel() throws DbException {
        return (DbORDomainModel) get(fUdtModel);
    }

    /**
     * Gets the default domain model object associated to a DbORDatabase's instance.
     * 
     * @return the default domain model object
     **/
    public final DbORDomainModel getDefaultDomainModel() throws DbException {
        return (DbORDomainModel) get(fDefaultDomainModel);
    }

}
