/*************************************************************************

Copyright (C) 2010 Grandite

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
import java.util.*;
import org.modelsphere.jack.srtool.*;
import org.modelsphere.jack.baseDb.util.*;

/**
<b>Direct subclass(es)/subinterface(s) : </b><A HREF="../../../../../org/modelsphere/sms/or/oracle/db/DbORADataModel.html">DbORADataModel</A>, <A HREF="../../../../../org/modelsphere/sms/or/ibm/db/DbIBMDataModel.html">DbIBMDataModel</A>, <A HREF="../../../../../org/modelsphere/sms/or/informix/db/DbINFDataModel.html">DbINFDataModel</A>, <A HREF="../../../../../org/modelsphere/sms/or/generic/db/DbGEDataModel.html">DbGEDataModel</A>.<br>

    <b>Composites : </b><A HREF="../../../../../org/modelsphere/sms/or/db/DbORDataModel.html">DbORDataModel</A>, <A HREF="../../../../../org/modelsphere/sms/db/DbSMSProject.html">DbSMSProject</A>, <A HREF="../../../../../org/modelsphere/sms/db/DbSMSUserDefinedPackage.html">DbSMSUserDefinedPackage</A>.<br>
    <b>Components : </b><A HREF="../../../../../org/modelsphere/sms/or/db/DbORAssociation.html">DbORAssociation</A>, <A HREF="../../../../../org/modelsphere/sms/or/db/DbORDataModel.html">DbORDataModel</A>, <A HREF="../../../../../org/modelsphere/sms/or/db/DbORDiagram.html">DbORDiagram</A>, <A HREF="../../../../../org/modelsphere/sms/or/db/DbORChoiceOrSpecialization.html">DbORChoiceOrSpecialization</A>, <A HREF="../../../../../org/modelsphere/sms/db/DbSMSNotice.html">DbSMSNotice</A>, <A HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbORDataModel extends DbORModel {

  //Meta

  public static final MetaRelation1 fDeploymentDatabase
    = new MetaRelation1(LocaleMgr.db.getString("deploymentDatabase"), 0);
  public static final MetaField fLogicalMode
    = new MetaField(LocaleMgr.db.getString("logicalMode"));
  public static final MetaField fIsConverted
    = new MetaField(LocaleMgr.db.getString("isConverted"));

  public static final MetaClass metaClass = new MetaClass(
    LocaleMgr.db.getString("DbORDataModel"), DbORDataModel.class,
    new MetaField[] {fDeploymentDatabase,
      fLogicalMode,
      fIsConverted}, MetaClass.UML_EXTENSIBILITY_FILTER);

/**
    For internal use only.
 **/
  public static void initMeta() {
    try {
      metaClass.setSuperMetaClass(DbORModel.metaClass);
      metaClass.setComponentMetaClasses(new MetaClass[] {DbORAssociation.metaClass, DbORDataModel.metaClass, DbORDiagram.metaClass, DbORChoiceOrSpecialization.metaClass});
      metaClass.setIcon("dbordatamodel.gif");

      fDeploymentDatabase.setJField(DbORDataModel.class.getDeclaredField("m_deploymentDatabase"));
      fLogicalMode.setJField(DbORDataModel.class.getDeclaredField("m_logicalMode"));
        fLogicalMode.setVisibleInScreen(false);
      fIsConverted.setJField(DbORDataModel.class.getDeclaredField("m_isConverted"));
        fIsConverted.setVisibleInScreen(false);

      fDeploymentDatabase.setOppositeRel(DbORDatabase.fSchema);

    }
    catch (Exception e) { throw new RuntimeException("Meta init"); }
  }

  static final long serialVersionUID = 0;
  public static final int LOGICAL_MODE_ENTITY_RELATIONSHIP = 1;
  public static final int LOGICAL_MODE_OBJECT_RELATIONAL = 2;

  //Instance variables
  DbORDatabase m_deploymentDatabase;
  int m_logicalMode;
  boolean m_isConverted;

  //Constructors

/**
    Parameter-less constructor.  Required by Java Beans Conventions.
 **/
  public DbORDataModel() {}

/**
    Creates an instance of DbORDataModel.

    @param composite org.modelsphere.jack.baseDb.db.DbObject
    @param targetsystem org.modelsphere.sms.db.DbSMSTargetSystem
    @param logicalmode int
 **/
  public DbORDataModel(DbObject composite, DbSMSTargetSystem targetSystem, int logicalMode) throws DbException {
    super(composite, targetSystem);
    setDefaultInitialValues();
    set(fLogicalMode, new Integer(logicalMode));

  }

/**
    Creates an instance of DbORDataModel.

    @param composite org.modelsphere.jack.baseDb.db.DbObject
    @param targetsystem org.modelsphere.sms.db.DbSMSTargetSystem
 **/
  public DbORDataModel(DbObject composite, DbSMSTargetSystem targetSystem) throws DbException {
    super(composite, targetSystem);
    setDefaultInitialValues();

  }

  private void setDefaultInitialValues() throws DbException {
    setLogicalMode(new Integer(LOGICAL_MODE_OBJECT_RELATIONAL));
    setIsConverted(Boolean.FALSE);
    TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();  
      setName(terminologyUtil.findModelTerminology(this).getTerm(metaClass));
  }

/**

    @param metareln org.modelsphere.jack.baseDb.meta.MetaRelationN
    @return boolean
 **/
  public final boolean isHugeRelN(MetaRelationN metaRelN) {
    if (metaRelN == DbObject.fComponents)
      return true;
    return super.isHugeRelN(metaRelN);
  }

/**

    @return int
 **/
  public final int getOperationalMode() throws DbException {
      return getLogicalMode().intValue();
  }

  //Setters

/**
    Sets the deployment database object associated to a DbORDataModel's instance.

    @param value the deployment database object to be associated

 **/
  public final void setDeploymentDatabase(DbORDatabase value) throws DbException {
    basicSet(fDeploymentDatabase, value);
  }

/**
    Sets the "logical mode" property of a DbORDataModel's instance.

    @param value the "logical mode" property

 **/
  public final void setLogicalMode(Integer value) throws DbException {
    basicSet(fLogicalMode, value);
  }

/**
    Sets the "is converted" property of a DbORDataModel's instance.

    @param value the "is converted" property

 **/
  public final void setIsConverted(Boolean value) throws DbException {
    basicSet(fIsConverted, value);
  }

/**
    
 **/
  public void set(MetaField metaField, Object value) throws DbException {
    if (metaField.getMetaClass() == metaClass) {
      basicSet(metaField, value);
    }
    else super.set(metaField, value);
  }

/**
    
 **/
  public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
    super.set(relation, neighbor, op);
  }

  //Getters

/**
    Gets the deployment database object associated to a DbORDataModel's instance.

    @return the deployment database object

 **/
  public final DbORDatabase getDeploymentDatabase() throws DbException {
    return (DbORDatabase)get(fDeploymentDatabase);
  }

/**
    Gets the "logical mode" property of a DbORDataModel's instance.

    @return the "logical mode" property

 **/
  public final Integer getLogicalMode() throws DbException {
    return (Integer)get(fLogicalMode);
  }

/**
    Gets the "is converted" property's Boolean value of a DbORDataModel's instance.

    @return the "is converted" property's Boolean value

    @deprecated use isIsConverted() method instead
 **/
  public final Boolean getIsConverted() throws DbException {
    return (Boolean)get(fIsConverted);
  }

/**
    Tells whether a DbORDataModel's instance is isConverted or not.

    @return boolean

 **/
  public final boolean isIsConverted() throws DbException {
    return getIsConverted().booleanValue();
  }

}
