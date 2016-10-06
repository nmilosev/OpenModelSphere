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
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A HREF="../../../../../org/modelsphere/sms/db/DbSMSUserDefinedPackage.html">
 * DbSMSUserDefinedPackage</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSProject.html">DbSMSProject</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/or/db/DbORDomain.html">DbORDomain</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/db/DbORDiagram.html">DbORDiagram</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSLinkModel.html">DbSMSLinkModel</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSNotice.html">DbSMSNotice</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbORDomainModel extends DbORModel {

    //Meta

    public static final MetaRelation1 fDeploymentDatabase = new MetaRelation1(LocaleMgr.db
            .getString("deploymentDatabase"), 0);
    public static final MetaRelationN fDatabasesDefaultDomains = new MetaRelationN(LocaleMgr.db
            .getString("databasesDefaultDomains"), 0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbORDomainModel"), DbORDomainModel.class, new MetaField[] {
            fDeploymentDatabase, fDatabasesDefaultDomains }, MetaClass.ACCESS_CTL
            | MetaClass.CLUSTER_ROOT | MetaClass.MATCHABLE | MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORModel.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbORDomain.metaClass,
                    DbORDiagram.metaClass });
            metaClass.setIcon("dbordomainmodel.gif");

            fDeploymentDatabase.setJField(DbORDomainModel.class
                    .getDeclaredField("m_deploymentDatabase"));
            fDatabasesDefaultDomains.setJField(DbORDomainModel.class
                    .getDeclaredField("m_databasesDefaultDomains"));

            fDeploymentDatabase.setOppositeRel(DbORDatabase.fUdtModel);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbORDatabase m_deploymentDatabase;
    DbRelationN m_databasesDefaultDomains;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORDomainModel() {
    }

    /**
     * Creates an instance of DbORDomainModel.
     * 
     * @param composite
     *            org.modelsphere.jack.baseDb.db.DbObject
     * @param targetsystem
     *            org.modelsphere.sms.db.DbSMSTargetSystem
     **/
    public DbORDomainModel(DbObject composite, DbSMSTargetSystem targetSystem) throws DbException {
        super(composite, targetSystem);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setName(LocaleMgr.misc.getString("domainmodel"));
    }

    /**
     * @param metareln
     *            org.modelsphere.jack.baseDb.meta.MetaRelationN
     * @return boolean
     **/
    public final boolean isHugeRelN(MetaRelationN metaRelN) {
        if (metaRelN == DbObject.fComponents)
            return true;
        return super.isHugeRelN(metaRelN);
    }

    //Setters

    /**
     * Sets the deployment database object associated to a DbORDomainModel's instance.
     * 
     * @param value
     *            the deployment database object to be associated
     **/
    public final void setDeploymentDatabase(DbORDatabase value) throws DbException {
        basicSet(fDeploymentDatabase, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fDatabasesDefaultDomains)
                ((DbORDatabase) value).setDefaultDomainModel(this);
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
     * Gets the deployment database object associated to a DbORDomainModel's instance.
     * 
     * @return the deployment database object
     **/
    public final DbORDatabase getDeploymentDatabase() throws DbException {
        return (DbORDatabase) get(fDeploymentDatabase);
    }

    /**
     * Gets the list of databases default domains associated to a DbORDomainModel's instance.
     * 
     * @return the list of databases default domains.
     **/
    public final DbRelationN getDatabasesDefaultDomains() throws DbException {
        return (DbRelationN) get(fDatabasesDefaultDomains);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
