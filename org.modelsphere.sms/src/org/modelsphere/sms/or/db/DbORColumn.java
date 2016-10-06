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
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.jack.baseDb.util.*;
import org.modelsphere.jack.baseDb.db.srtypes.SrBoolean;

/**
<b>Direct subclass(es)/subinterface(s) : </b><A HREF="../../../../../org/modelsphere/sms/or/oracle/db/DbORAColumn.html">DbORAColumn</A>, <A HREF="../../../../../org/modelsphere/sms/or/ibm/db/DbIBMColumn.html">DbIBMColumn</A>, <A HREF="../../../../../org/modelsphere/sms/or/informix/db/DbINFColumn.html">DbINFColumn</A>, <A HREF="../../../../../org/modelsphere/sms/or/generic/db/DbGEColumn.html">DbGEColumn</A>.<br>

    <b>Composites : </b>none.<br>
    <b>Components : </b><A HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbORColumn extends DbORAttribute {

  //Meta

  public static final MetaField fNull
    = new MetaField(LocaleMgr.db.getString("null"));
  public static final MetaField fForeign
    = new MetaField(LocaleMgr.db.getString("foreign"));
  public static final MetaRelation1 fCommonItem
    = new MetaRelation1(LocaleMgr.db.getString("commonItem"), 0);
  public static final MetaRelationN fIndexKeys
    = new MetaRelationN(LocaleMgr.db.getString("indexKeys"), 0, MetaRelationN.cardN);
  public static final MetaRelationN fTriggers
    = new MetaRelationN(LocaleMgr.db.getString("triggers"), 0, MetaRelationN.cardN);
  public static final MetaRelationN fPrimaryUniques
    = new MetaRelationN(LocaleMgr.db.getString("primaryUniques"), 0, MetaRelationN.cardN);
  public static final MetaRelationN fChecks
    = new MetaRelationN(LocaleMgr.db.getString("checks"), 0, MetaRelationN.cardN);
  public static final MetaRelationN fFKeyColumns
    = new MetaRelationN(LocaleMgr.db.getString("fKeyColumns"), 0, MetaRelationN.cardN);
  public static final MetaRelationN fDestFKeyColumns
    = new MetaRelationN(LocaleMgr.db.getString("destFKeyColumns"), 0, MetaRelationN.cardN);
  public static final MetaField fReference
    = new MetaField(LocaleMgr.db.getString("reference"));
  public static final MetaRelation1 fChoiceOrSpecialization
    = new MetaRelation1(LocaleMgr.db.getString("choiceOrSpecialization"), 0);

  public static final MetaClass metaClass = new MetaClass(
    LocaleMgr.db.getString("DbORColumn"), DbORColumn.class,
    new MetaField[] {fNull,
      fForeign,
      fCommonItem,
      fIndexKeys,
      fTriggers,
      fPrimaryUniques,
      fChecks,
      fFKeyColumns,
      fDestFKeyColumns,
      fReference,
      fChoiceOrSpecialization}, MetaClass.UML_EXTENSIBILITY_FILTER);

/**
    For internal use only.
 **/
  public static void initMeta() {
    try {
      metaClass.setSuperMetaClass(DbORAttribute.metaClass);
      metaClass.setIcon("dborcolumn.gif");

      fNull.setJField(DbORColumn.class.getDeclaredField("m_null"));
        fNull.setRendererPluginName("Boolean");
      fForeign.setJField(DbORColumn.class.getDeclaredField("m_foreign"));
        fForeign.setEditable(false);
      fCommonItem.setJField(DbORColumn.class.getDeclaredField("m_commonItem"));
        fCommonItem.setFlags(MetaField.COPY_REFS | MetaField.INTEGRABLE_BY_NAME);
      fIndexKeys.setJField(DbORColumn.class.getDeclaredField("m_indexKeys"));
      fTriggers.setJField(DbORColumn.class.getDeclaredField("m_triggers"));
      fPrimaryUniques.setJField(DbORColumn.class.getDeclaredField("m_primaryUniques"));
      fChecks.setJField(DbORColumn.class.getDeclaredField("m_checks"));
      fFKeyColumns.setJField(DbORColumn.class.getDeclaredField("m_fKeyColumns"));
      fDestFKeyColumns.setJField(DbORColumn.class.getDeclaredField("m_destFKeyColumns"));
      fReference.setJField(DbORColumn.class.getDeclaredField("m_reference"));
      fChoiceOrSpecialization.setJField(DbORColumn.class.getDeclaredField("m_choiceOrSpecialization"));

      fCommonItem.setOppositeRel(DbORCommonItem.fColumns);
      fTriggers.setOppositeRel(DbORTrigger.fColumns);
      fPrimaryUniques.setOppositeRel(DbORPrimaryUnique.fColumns);
      fChoiceOrSpecialization.setOppositeRel(DbORChoiceOrSpecialization.fColumns);

    }
    catch (Exception e) { throw new RuntimeException("Meta init"); }
  }

  static final long serialVersionUID = 0;

  //Instance variables
  boolean m_null;
  boolean m_foreign;
  DbORCommonItem m_commonItem;
  DbRelationN m_indexKeys;
  DbRelationN m_triggers;
  DbRelationN m_primaryUniques;
  DbRelationN m_checks;
  DbRelationN m_fKeyColumns;
  DbRelationN m_destFKeyColumns;
  boolean m_reference;
  DbORChoiceOrSpecialization m_choiceOrSpecialization;

  //Constructors

/**
    Parameter-less constructor.  Required by Java Beans Conventions.
 **/
  public DbORColumn() {}

/**
    Creates an instance of DbORColumn.
    @param composite the object which will contain the newly-created instance
 **/
  public DbORColumn(DbObject composite) throws DbException {
    super(composite);
    setDefaultInitialValues();
  }

  private void setDefaultInitialValues() throws DbException {
    setNull(Boolean.TRUE);
    setReference(Boolean.FALSE);
    DbORDataModel dataModel = (DbORDataModel)getCompositeOfType(DbORDataModel.metaClass);
    TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();  
    Terminology term = terminologyUtil.findModelTerminology(dataModel);
    setName(term.getTerm(metaClass));

  }

/**

    @return int
 **/
  protected final int getFeatureSet() {
  return SMSFilter.RELATIONAL;

  }

/**

    @param metafield org.modelsphere.jack.baseDb.meta.MetaField
    @param value java.lang.Object
 **/
  public void set(MetaField metaField, Object value) throws DbException {
    if (metaField.getMetaClass() == metaClass) {
      if (metaField == fIndexKeys)
        ((DbORIndexKey)value).setIndexedElement(this);
      else if (metaField == fTriggers)
        setTriggers((DbORTrigger)value, Db.ADD_TO_RELN);
      else if (metaField == fPrimaryUniques)
        setPrimaryUniques((DbORPrimaryUnique)value, Db.ADD_TO_RELN);
      else if (metaField == fChecks)
        ((DbORCheck)value).setColumn(this);
      else if (metaField == fFKeyColumns)
        ((DbORFKeyColumn)value).setColumn(this);
      else if (metaField == fDestFKeyColumns)
        ((DbORFKeyColumn)value).setSourceColumn(this);
      else if (metaField == fNull){
        if(value instanceof Boolean)
            setNull((Boolean)value);
        else if (value instanceof SrBoolean)
            setNull((Boolean)((SrBoolean)value).toApplType());
        else
            basicSet(metaField, value);
      }
      else basicSet(metaField, value);
    }
    else super.set(metaField, value);
  }

  //Setters

/**
    Sets the "null possible" property of a DbORColumn's instance.

    @param value the "null possible" property

 **/
  public final void setNull(Boolean value) throws DbException {
    basicSet(fNull, value);
  }

/**
    Sets the "foreign" property of a DbORColumn's instance.

    @param value the "foreign" property

 **/
  public final void setForeign(Boolean value) throws DbException {
    basicSet(fForeign, value);
  }

/**
    Sets the common item object associated to a DbORColumn's instance.

    @param value the common item object to be associated

 **/
  public final void setCommonItem(DbORCommonItem value) throws DbException {
    basicSet(fCommonItem, value);
  }

/**
    Adds an element to or removes an element from the list of triggers associated to a DbORColumn's instance.

    @param value an element to be added to or removed from the list.
    @param op Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
 **/
  public final void setTriggers(DbORTrigger value, int op) throws DbException {
    setRelationNN(fTriggers, value, op);
  }

/**
    Adds an element to the list of triggers associated to a DbORColumn's instance.

    @param value the element to be added.
 **/
  public final void addToTriggers(DbORTrigger value) throws DbException {
    setTriggers(value, Db.ADD_TO_RELN);
  }

/**
    Removes an element from the list of triggers associated to a DbORColumn's instance.

    @param value the element to be removed.
 **/
  public final void removeFromTriggers(DbORTrigger value) throws DbException {
    setTriggers(value, Db.REMOVE_FROM_RELN);
  }

/**
    Adds an element to or removes an element from the list of primary / unique keys associated to a DbORColumn's instance.

    @param value an element to be added to or removed from the list.
    @param op Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
 **/
  public final void setPrimaryUniques(DbORPrimaryUnique value, int op) throws DbException {
    setRelationNN(fPrimaryUniques, value, op);
  }

/**
    Adds an element to the list of primary / unique keys associated to a DbORColumn's instance.

    @param value the element to be added.
 **/
  public final void addToPrimaryUniques(DbORPrimaryUnique value) throws DbException {
    setPrimaryUniques(value, Db.ADD_TO_RELN);
  }

/**
    Removes an element from the list of primary / unique keys associated to a DbORColumn's instance.

    @param value the element to be removed.
 **/
  public final void removeFromPrimaryUniques(DbORPrimaryUnique value) throws DbException {
    setPrimaryUniques(value, Db.REMOVE_FROM_RELN);
  }

/**
    Sets the "reference" property of a DbORColumn's instance.

    @param value the "reference" property

 **/
  public final void setReference(Boolean value) throws DbException {
    basicSet(fReference, value);
  }

/**
    Sets the choice or specialization object associated to a DbORColumn's instance.

    @param value the choice or specialization object to be associated

 **/
  public final void setChoiceOrSpecialization(DbORChoiceOrSpecialization value) throws DbException {
    basicSet(fChoiceOrSpecialization, value);
  }

/**
    
 **/
  public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
    if (relation == fTriggers)
      setTriggers((DbORTrigger)neighbor, op);
    else if (relation == fPrimaryUniques)
      setPrimaryUniques((DbORPrimaryUnique)neighbor, op);
    else super.set(relation, neighbor, op);
  }

  //Getters

/**
    Gets the "null possible" property's Boolean value of a DbORColumn's instance.

    @return the "null possible" property's Boolean value

    @deprecated use isNull() method instead
 **/
  public final Boolean getNull() throws DbException {
    return (Boolean)get(fNull);
  }

/**
    Tells whether a DbORColumn's instance is null or not.

    @return boolean

 **/
  public final boolean isNull() throws DbException {
    return getNull().booleanValue();
  }

/**
    Gets the "foreign" property's Boolean value of a DbORColumn's instance.

    @return the "foreign" property's Boolean value

    @deprecated use isForeign() method instead
 **/
  public final Boolean getForeign() throws DbException {
    return (Boolean)get(fForeign);
  }

/**
    Tells whether a DbORColumn's instance is foreign or not.

    @return boolean

 **/
  public final boolean isForeign() throws DbException {
    return getForeign().booleanValue();
  }

/**
    Gets the common item object associated to a DbORColumn's instance.

    @return the common item object

 **/
  public final DbORCommonItem getCommonItem() throws DbException {
    return (DbORCommonItem)get(fCommonItem);
  }

/**
    Gets the list of index keys associated to a DbORColumn's instance.

    @return the list of index keys.

 **/
  public final DbRelationN getIndexKeys() throws DbException {
    return (DbRelationN)get(fIndexKeys);
  }

/**
    Gets the list of triggers associated to a DbORColumn's instance.

    @return the list of triggers.

 **/
  public final DbRelationN getTriggers() throws DbException {
    return (DbRelationN)get(fTriggers);
  }

/**
    Gets the list of primary / unique keys associated to a DbORColumn's instance.

    @return the list of primary / unique keys.

 **/
  public final DbRelationN getPrimaryUniques() throws DbException {
    return (DbRelationN)get(fPrimaryUniques);
  }

/**
    Gets the list of check constraints associated to a DbORColumn's instance.

    @return the list of check constraints.

 **/
  public final DbRelationN getChecks() throws DbException {
    return (DbRelationN)get(fChecks);
  }

/**
    Gets the list of foreign keys associated to a DbORColumn's instance.

    @return the list of foreign keys.

 **/
  public final DbRelationN getFKeyColumns() throws DbException {
    return (DbRelationN)get(fFKeyColumns);
  }

/**
    Gets the list of dest foreign keys associated to a DbORColumn's instance.

    @return the list of dest foreign keys.

 **/
  public final DbRelationN getDestFKeyColumns() throws DbException {
    return (DbRelationN)get(fDestFKeyColumns);
  }

/**
    Gets the "reference" property's Boolean value of a DbORColumn's instance.

    @return the "reference" property's Boolean value

    @deprecated use isReference() method instead
 **/
  public final Boolean getReference() throws DbException {
    return (Boolean)get(fReference);
  }

/**
    Tells whether a DbORColumn's instance is reference or not.

    @return boolean

 **/
  public final boolean isReference() throws DbException {
    return getReference().booleanValue();
  }

/**
    Gets the choice or specialization object associated to a DbORColumn's instance.

    @return the choice or specialization object

 **/
  public final DbORChoiceOrSpecialization getChoiceOrSpecialization() throws DbException {
    return (DbORChoiceOrSpecialization)get(fChoiceOrSpecialization);
  }

}
