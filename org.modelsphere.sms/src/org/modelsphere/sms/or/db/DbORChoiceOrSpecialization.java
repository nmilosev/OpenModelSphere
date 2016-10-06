/*************************************************************************

Copyright (C) 2008 Grandite

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
<b>Direct subclass(es)/subinterface(s) : </b>none.<br>

    <b>Composites : </b><A HREF="../../../../../org/modelsphere/sms/or/db/DbORDataModel.html">DbORDataModel</A>.<br>
    <b>Components : </b><A HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbORChoiceOrSpecialization extends DbORAbsTable {

  //Meta

  public static final MetaField fCategory
    = new MetaField(LocaleMgr.db.getString("category"));
  public static final MetaRelation1 fParentTable
    = new MetaRelation1(LocaleMgr.db.getString("parentTable"), 1);
  public static final MetaRelationN fColumns
    = new MetaRelationN(LocaleMgr.db.getString("columns"), 0, MetaRelationN.cardN);
  public static final MetaField fGenerateColumn
    = new MetaField(LocaleMgr.db.getString("generateColumn"));
  public static final MetaField fMultiplicity
    = new MetaField(LocaleMgr.db.getString("multiplicity"));
  public static final MetaField fSpecificRangeMultiplicity
    = new MetaField(LocaleMgr.db.getString("specificRangeMultiplicity"));
  public static final MetaRelationN fAssociations
    = new MetaRelationN(LocaleMgr.db.getString("associations"), 0, MetaRelationN.cardN);

  public static final MetaClass metaClass = new MetaClass(
    LocaleMgr.db.getString("DbORChoiceOrSpecialization"), DbORChoiceOrSpecialization.class,
    new MetaField[] {fCategory,
      fParentTable,
      fColumns,
      fGenerateColumn,
      fMultiplicity,
      fSpecificRangeMultiplicity,
      fAssociations}, MetaClass.MATCHABLE);

/**
    For internal use only.
 **/
  public static void initMeta() {
    try {
      metaClass.setSuperMetaClass(DbORAbsTable.metaClass);

      fCategory.setJField(DbORChoiceOrSpecialization.class.getDeclaredField("m_category"));
      fParentTable.setJField(DbORChoiceOrSpecialization.class.getDeclaredField("m_parentTable"));
      fColumns.setJField(DbORChoiceOrSpecialization.class.getDeclaredField("m_columns"));
      fGenerateColumn.setJField(DbORChoiceOrSpecialization.class.getDeclaredField("m_generateColumn"));
      fMultiplicity.setJField(DbORChoiceOrSpecialization.class.getDeclaredField("m_multiplicity"));
      fSpecificRangeMultiplicity.setJField(DbORChoiceOrSpecialization.class.getDeclaredField("m_specificRangeMultiplicity"));
      fAssociations.setJField(DbORChoiceOrSpecialization.class.getDeclaredField("m_associations"));

      fParentTable.setOppositeRel(DbORTable.fChoicesAndSpecializations);

    }
    catch (Exception e) { throw new RuntimeException("Meta init"); }
  }

  static final long serialVersionUID = 0;

  //Instance variables
  ORChoiceSpecializationCategory m_category;
  DbORTable m_parentTable;
  DbRelationN m_columns;
  boolean m_generateColumn;
  SMSMultiplicity m_multiplicity;
  String m_specificRangeMultiplicity;
  DbRelationN m_associations;

  //Constructors

/**
    Parameter-less constructor.  Required by Java Beans Conventions.
 **/
  public DbORChoiceOrSpecialization() {}

/**
    Creates an instance of DbORChoiceOrSpecialization.
    @param composite the object which will contain the newly-created instance
 **/
  public DbORChoiceOrSpecialization(DbObject composite) throws DbException {
    super(composite);
    setDefaultInitialValues();
  }

  private void setDefaultInitialValues() throws DbException {
    setGenerateColumn(Boolean.TRUE);
    //init choice or specialization
    org.modelsphere.sms.db.util.DbInitialization.initChoiceOrSpecialization(this);
  }

/**

 **/
  public void remove() throws DbException {
    //customized delete()
    org.modelsphere.sms.db.util.DbInitialization.removeChoiceSpec(this);
    super.remove();
  }

  //Setters

/**
    Sets the "category" property of a DbORChoiceOrSpecialization's instance.

    @param value the "category" property

 **/
  public final void setCategory(ORChoiceSpecializationCategory value) throws DbException {
    basicSet(fCategory, value);
  }

/**
    Sets the parent table object associated to a DbORChoiceOrSpecialization's instance.

    @param value the parent table object to be associated

 **/
  public final void setParentTable(DbORTable value) throws DbException {
    basicSet(fParentTable, value);
  }

/**
    Sets the "generate column" property of a DbORChoiceOrSpecialization's instance.

    @param value the "generate column" property

 **/
  public final void setGenerateColumn(Boolean value) throws DbException {
    basicSet(fGenerateColumn, value);
  }

/**
    Sets the "multiplicity" property of a DbORChoiceOrSpecialization's instance.

    @param value the "multiplicity" property

 **/
  public final void setMultiplicity(SMSMultiplicity value) throws DbException {
    basicSet(fMultiplicity, value);
  }

/**
    Sets the "specific-range multiplicity" property of a DbORChoiceOrSpecialization's instance.

    @param value the "specific-range multiplicity" property

 **/
  public final void setSpecificRangeMultiplicity(String value) throws DbException {
    basicSet(fSpecificRangeMultiplicity, value);
  }

/**
    
 **/
  public void set(MetaField metaField, Object value) throws DbException {
    if (metaField.getMetaClass() == metaClass) {
      if (metaField == fColumns)
        ((DbORColumn)value).setChoiceOrSpecialization(this);
      else if (metaField == fAssociations)
        ((DbORAssociation)value).setChoiceOrSpecialization(this);
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
    Gets the "category" of a DbORChoiceOrSpecialization's instance.

    @return the "category"

 **/
  public final ORChoiceSpecializationCategory getCategory() throws DbException {
    return (ORChoiceSpecializationCategory)get(fCategory);
  }

/**
    Gets the parent table object associated to a DbORChoiceOrSpecialization's instance.

    @return the parent table object

 **/
  public final DbORTable getParentTable() throws DbException {
    return (DbORTable)get(fParentTable);
  }

/**
    Gets the list of columns associated to a DbORChoiceOrSpecialization's instance.

    @return the list of columns.

 **/
  public final DbRelationN getColumns() throws DbException {
    return (DbRelationN)get(fColumns);
  }

/**
    Gets the "generate column" property's Boolean value of a DbORChoiceOrSpecialization's instance.

    @return the "generate column" property's Boolean value

    @deprecated use isGenerateColumn() method instead
 **/
  public final Boolean getGenerateColumn() throws DbException {
    return (Boolean)get(fGenerateColumn);
  }

/**
    Tells whether a DbORChoiceOrSpecialization's instance is generateColumn or not.

    @return boolean

 **/
  public final boolean isGenerateColumn() throws DbException {
    return getGenerateColumn().booleanValue();
  }

/**
    Gets the "multiplicity" of a DbORChoiceOrSpecialization's instance.

    @return the "multiplicity"

 **/
  public final SMSMultiplicity getMultiplicity() throws DbException {
    return (SMSMultiplicity)get(fMultiplicity);
  }

/**
    Gets the "specific-range multiplicity" property of a DbORChoiceOrSpecialization's instance.

    @return the "specific-range multiplicity" property

 **/
  public final String getSpecificRangeMultiplicity() throws DbException {
    return (String)get(fSpecificRangeMultiplicity);
  }

/**
    Gets the list of associations objects associated to a DbORChoiceOrSpecialization's instance.

    @return the list of associations objects.

 **/
  public final DbRelationN getAssociations() throws DbException {
    return (DbRelationN)get(fAssociations);
  }

  public MetaClass getMetaClass() {
    return metaClass;
  }

}
