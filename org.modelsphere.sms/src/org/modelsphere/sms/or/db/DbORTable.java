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
import org.modelsphere.jack.baseDb.util.*;

/**
<b>Direct subclass(es)/subinterface(s) : </b><A HREF="../../../../../org/modelsphere/sms/or/oracle/db/DbORAAbsTable.html">DbORAAbsTable</A>, <A HREF="../../../../../org/modelsphere/sms/or/ibm/db/DbIBMTable.html">DbIBMTable</A>, <A HREF="../../../../../org/modelsphere/sms/or/informix/db/DbINFTable.html">DbINFTable</A>, <A HREF="../../../../../org/modelsphere/sms/or/generic/db/DbGETable.html">DbGETable</A>.<br>

    <b>Composites : </b>none.<br>
    <b>Components : </b><A HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbORTable extends DbORAbsTable {

  //Meta

  public static final MetaField fIsDependant
    = new MetaField(LocaleMgr.db.getString("isDependant"));
  public static final MetaField fIsAssociation
    = new MetaField(LocaleMgr.db.getString("isAssociation"));
  public static final MetaRelationN fChoicesAndSpecializations
    = new MetaRelationN(LocaleMgr.db.getString("choicesAndSpecializations"), 0, MetaRelationN.cardN);

  public static final MetaClass metaClass = new MetaClass(
    LocaleMgr.db.getString("DbORTable"), DbORTable.class,
    new MetaField[] {fIsDependant,
      fIsAssociation,
      fChoicesAndSpecializations}, 0);

/**
    For internal use only.
 **/
  public static void initMeta() {
    try {
      metaClass.setSuperMetaClass(DbORAbsTable.metaClass);
      metaClass.setIcon("dbortable.gif");

      fIsDependant.setJField(DbORTable.class.getDeclaredField("m_isDependant"));
      fIsAssociation.setJField(DbORTable.class.getDeclaredField("m_isAssociation"));
        fIsAssociation.setVisibleInScreen(false);
      fChoicesAndSpecializations.setJField(DbORTable.class.getDeclaredField("m_choicesAndSpecializations"));

    }
    catch (Exception e) { throw new RuntimeException("Meta init"); }
  }

  static final long serialVersionUID = 0;

  //Instance variables
  boolean m_isDependant;
  boolean m_isAssociation;
  DbRelationN m_choicesAndSpecializations;

  //Constructors

/**
    Parameter-less constructor.  Required by Java Beans Conventions.
 **/
  public DbORTable() {}

/**
    Creates an instance of DbORTable.
    @param composite the object which will contain the newly-created instance
 **/
  public DbORTable(DbObject composite) throws DbException {
    super(composite);
    setDefaultInitialValues();
  }

  private void setDefaultInitialValues() throws DbException {
    setIsDependant(Boolean.FALSE);
    setIsAssociation(Boolean.FALSE);
    DbORDataModel dataModel = (DbORDataModel)getCompositeOfType(DbORDataModel.metaClass);
    TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();  
    Terminology term = terminologyUtil.findModelTerminology(dataModel);
    setName(term.getTerm(metaClass));
  }

  //Setters

/**
    Sets the "is dependant" property of a DbORTable's instance.

    @param value the "is dependant" property

 **/
  public final void setIsDependant(Boolean value) throws DbException {
    basicSet(fIsDependant, value);
  }

/**
    Sets the "is association table" property of a DbORTable's instance.

    @param value the "is association table" property

 **/
  public final void setIsAssociation(Boolean value) throws DbException {
    basicSet(fIsAssociation, value);
  }

/**
    
 **/
  public void set(MetaField metaField, Object value) throws DbException {
    if (metaField.getMetaClass() == metaClass) {
      if (metaField == fChoicesAndSpecializations)
        ((DbORChoiceOrSpecialization)value).setParentTable(this);
      else basicSet(metaField, value);
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
    Gets the "is dependant" property's Boolean value of a DbORTable's instance.

    @return the "is dependant" property's Boolean value

    @deprecated use isIsDependant() method instead
 **/
  public final Boolean getIsDependant() throws DbException {
    return (Boolean)get(fIsDependant);
  }

/**
    Tells whether a DbORTable's instance is isDependant or not.

    @return boolean

 **/
  public final boolean isIsDependant() throws DbException {
    return getIsDependant().booleanValue();
  }

/**
    Gets the "is association table" property's Boolean value of a DbORTable's instance.

    @return the "is association table" property's Boolean value

    @deprecated use isIsAssociation() method instead
 **/
  public final Boolean getIsAssociation() throws DbException {
    return (Boolean)get(fIsAssociation);
  }

/**
    Tells whether a DbORTable's instance is isAssociation or not.

    @return boolean

 **/
  public final boolean isIsAssociation() throws DbException {
    return getIsAssociation().booleanValue();
  }

/**
    Gets the list of choice or specializations associated to a DbORTable's instance.

    @return the list of choice or specializations.

 **/
  public final DbRelationN getChoicesAndSpecializations() throws DbException {
    return (DbRelationN)get(fChoicesAndSpecializations);
  }

}
