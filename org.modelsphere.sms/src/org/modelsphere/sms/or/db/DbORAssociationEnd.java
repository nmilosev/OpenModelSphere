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
import org.modelsphere.jack.baseDb.util.*;

/**
<b>Direct subclass(es)/subinterface(s) : </b>none.<br>

    <b>Composites : </b><A HREF="../../../../../org/modelsphere/sms/or/db/DbORAssociation.html">DbORAssociation</A>.<br>
    <b>Components : </b><A HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbORAssociationEnd extends DbSMSAssociationEnd {

  //Meta

  public static final MetaField fInsertRule
    = new MetaField(LocaleMgr.db.getString("insertRule"));
  public static final MetaField fUpdateRule
    = new MetaField(LocaleMgr.db.getString("updateRule"));
  public static final MetaField fDeleteRule
    = new MetaField(LocaleMgr.db.getString("deleteRule"));
  public static final MetaRelation1 fClassifier
    = new MetaRelation1(LocaleMgr.db.getString("classifier"), 1);
  public static final MetaRelation1 fMember
    = new MetaRelation1(LocaleMgr.db.getString("member"), 0);
  public static final MetaField fNavigable
    = new MetaField(LocaleMgr.db.getString("navigable"));
  public static final MetaField fConstraintType
    = new MetaField(LocaleMgr.db.getString("constraintType"));
  public static final MetaRelationN fDependentConstraints
    = new MetaRelationN(LocaleMgr.db.getString("dependentConstraints"), 0, MetaRelationN.cardN);
  public static final MetaRelation1 fReferencedConstraint
    = new MetaRelation1(LocaleMgr.db.getString("referencedConstraint"), 0);

  public static final MetaClass metaClass = new MetaClass(
    LocaleMgr.db.getString("DbORAssociationEnd"), DbORAssociationEnd.class,
    new MetaField[] {fInsertRule,
      fUpdateRule,
      fDeleteRule,
      fClassifier,
      fMember,
      fNavigable,
      fConstraintType,
      fDependentConstraints,
      fReferencedConstraint}, 0);

/**
    For internal use only.
 **/
  public static void initMeta() {
    try {
      metaClass.setSuperMetaClass(DbSMSAssociationEnd.metaClass);
      metaClass.setIcon("dborassociationend.gif");

      fInsertRule.setJField(DbORAssociationEnd.class.getDeclaredField("m_insertRule"));
      fUpdateRule.setJField(DbORAssociationEnd.class.getDeclaredField("m_updateRule"));
      fDeleteRule.setJField(DbORAssociationEnd.class.getDeclaredField("m_deleteRule"));
      fClassifier.setJField(DbORAssociationEnd.class.getDeclaredField("m_classifier"));
        fClassifier.setEditable(false);
      fMember.setJField(DbORAssociationEnd.class.getDeclaredField("m_member"));
        fMember.setEditable(false);
      fNavigable.setJField(DbORAssociationEnd.class.getDeclaredField("m_navigable"));
      fConstraintType.setJField(DbORAssociationEnd.class.getDeclaredField("m_constraintType"));
      fDependentConstraints.setJField(DbORAssociationEnd.class.getDeclaredField("m_dependentConstraints"));
      fReferencedConstraint.setJField(DbORAssociationEnd.class.getDeclaredField("m_referencedConstraint"));

      fClassifier.setOppositeRel(DbORAbsTable.fAssociationEnds);
      fMember.setOppositeRel(DbORForeign.fAssociationEnd);
      fDependentConstraints.setOppositeRel(DbORPrimaryUnique.fAssociationDependencies);
      fReferencedConstraint.setOppositeRel(DbORPrimaryUnique.fAssociationReferences);

    }
    catch (Exception e) { throw new RuntimeException("Meta init"); }
  }

  static final long serialVersionUID = 0;

  //Instance variables
  ORValidationRule m_insertRule;
  ORValidationRule m_updateRule;
  ORValidationRule m_deleteRule;
  DbORAbsTable m_classifier;
  DbORForeign m_member;
  boolean m_navigable;
  ORConstraintType m_constraintType;
  DbRelationN m_dependentConstraints;
  DbORPrimaryUnique m_referencedConstraint;

  //Constructors

/**
    Parameter-less constructor.  Required by Java Beans Conventions.
 **/
  public DbORAssociationEnd() {}

/**
    Creates an instance of DbORAssociationEnd.

    @param composite org.modelsphere.sms.or.db.DbORAssociation
    @param classifier org.modelsphere.sms.or.db.DbORAbsTable
    @param multiplicity org.modelsphere.sms.db.srtypes.SMSMultiplicity
    @param name java.lang.String
 **/
  protected DbORAssociationEnd(DbORAssociation composite, DbORAbsTable classifier, SMSMultiplicity multiplicity, String name) throws DbException {
    super(composite);
    setDefaultInitialValues();
    setClassifier(classifier);
    setMultiplicity(multiplicity);
    
    DbORDataModel dataModel = (DbORDataModel)getCompositeOfType(DbORDataModel.metaClass);
    TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();  
    Terminology term = terminologyUtil.findModelTerminology(dataModel);

    setName(name.concat(" ").concat(term.getTerm(metaClass)));
    
    
    if (multiplicity.getValue() == SMSMultiplicity.EXACTLY_ONE  ||  multiplicity.getValue() == SMSMultiplicity.OPTIONAL)
      setNavigable(Boolean.TRUE);
  }

  private void setDefaultInitialValues() throws DbException {
  }

/**

    @return boolean
 **/
  public final boolean isFrontEnd() throws DbException {

    DbORAssociation assoc = (DbORAssociation)getComposite();
    return (this == assoc.getFrontEnd());

  }

/**

    @return role
 **/
  public final DbORAssociationEnd getOppositeEnd() throws DbException {

    DbORAssociation assoc = (DbORAssociation)getComposite();
    DbRelationN ends = assoc.getComponents();
    DbORAssociationEnd oppEnd = (DbORAssociationEnd)ends.elementAt(0);
    if (oppEnd == this)
      oppEnd = (DbORAssociationEnd)ends.elementAt(1);
    return oppEnd;

  }

  //Setters

/**
    Sets the "insert rule" property of a DbORAssociationEnd's instance.

    @param value the "insert rule" property

 **/
  public final void setInsertRule(ORValidationRule value) throws DbException {
    basicSet(fInsertRule, value);
  }

/**
    Sets the "update rule" property of a DbORAssociationEnd's instance.

    @param value the "update rule" property

 **/
  public final void setUpdateRule(ORValidationRule value) throws DbException {
    basicSet(fUpdateRule, value);
  }

/**
    Sets the "delete rule" property of a DbORAssociationEnd's instance.

    @param value the "delete rule" property

 **/
  public final void setDeleteRule(ORValidationRule value) throws DbException {
    basicSet(fDeleteRule, value);
  }

/**
    Sets the classifier object associated to a DbORAssociationEnd's instance.

    @param value the classifier object to be associated

 **/
  public final void setClassifier(DbORAbsTable value) throws DbException {
    basicSet(fClassifier, value);
  }

/**
    Sets the foreign key object associated to a DbORAssociationEnd's instance.

    @param value the foreign key object to be associated

 **/
  public final void setMember(DbORForeign value) throws DbException {
    basicSet(fMember, value);
  }

/**
    Sets the "navigability" property of a DbORAssociationEnd's instance.

    @param value the "navigability" property

 **/
  public final void setNavigable(Boolean value) throws DbException {
    basicSet(fNavigable, value);
  }

/**
    Sets the "constraint type" property of a DbORAssociationEnd's instance.

    @param value the "constraint type" property

 **/
  public final void setConstraintType(ORConstraintType value) throws DbException {
    basicSet(fConstraintType, value);
  }

/**
    Adds an element to or removes an element from the list of dependent constraints associated to a DbORAssociationEnd's instance.

    @param value an element to be added to or removed from the list.
    @param op Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
 **/
  public final void setDependentConstraints(DbORPrimaryUnique value, int op) throws DbException {
    setRelationNN(fDependentConstraints, value, op);
  }

/**
    Adds an element to the list of dependent constraints associated to a DbORAssociationEnd's instance.

    @param value the element to be added.
 **/
  public final void addToDependentConstraints(DbORPrimaryUnique value) throws DbException {
    setDependentConstraints(value, Db.ADD_TO_RELN);
  }

/**
    Removes an element from the list of dependent constraints associated to a DbORAssociationEnd's instance.

    @param value the element to be removed.
 **/
  public final void removeFromDependentConstraints(DbORPrimaryUnique value) throws DbException {
    setDependentConstraints(value, Db.REMOVE_FROM_RELN);
  }

/**
    Sets the referenced constraint object associated to a DbORAssociationEnd's instance.

    @param value the referenced constraint object to be associated

 **/
  public final void setReferencedConstraint(DbORPrimaryUnique value) throws DbException {
    basicSet(fReferencedConstraint, value);
  }

/**
    
 **/
  public void set(MetaField metaField, Object value) throws DbException {
    if (metaField.getMetaClass() == metaClass) {
      if (metaField == fDependentConstraints)
        setDependentConstraints((DbORPrimaryUnique)value, Db.ADD_TO_RELN);
      else basicSet(metaField, value);
    }
    else super.set(metaField, value);
  }

/**
    
 **/
  public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
    if (relation == fDependentConstraints)
      setDependentConstraints((DbORPrimaryUnique)neighbor, op);
    else super.set(relation, neighbor, op);
  }

  //Getters

/**
    Gets the "insert rule" of a DbORAssociationEnd's instance.

    @return the "insert rule"

 **/
  public final ORValidationRule getInsertRule() throws DbException {
    return (ORValidationRule)get(fInsertRule);
  }

/**
    Gets the "update rule" of a DbORAssociationEnd's instance.

    @return the "update rule"

 **/
  public final ORValidationRule getUpdateRule() throws DbException {
    return (ORValidationRule)get(fUpdateRule);
  }

/**
    Gets the "delete rule" of a DbORAssociationEnd's instance.

    @return the "delete rule"

 **/
  public final ORValidationRule getDeleteRule() throws DbException {
    return (ORValidationRule)get(fDeleteRule);
  }

/**
    Gets the classifier object associated to a DbORAssociationEnd's instance.

    @return the classifier object

 **/
  public final DbORAbsTable getClassifier() throws DbException {
    return (DbORAbsTable)get(fClassifier);
  }

/**
    Gets the foreign key object associated to a DbORAssociationEnd's instance.

    @return the foreign key object

 **/
  public final DbORForeign getMember() throws DbException {
    return (DbORForeign)get(fMember);
  }

/**
    Gets the "navigability" property's Boolean value of a DbORAssociationEnd's instance.

    @return the "navigability" property's Boolean value

    @deprecated use isNavigable() method instead
 **/
  public final Boolean getNavigable() throws DbException {
    return (Boolean)get(fNavigable);
  }

/**
    Tells whether a DbORAssociationEnd's instance is navigable or not.

    @return boolean

 **/
  public final boolean isNavigable() throws DbException {
    return getNavigable().booleanValue();
  }

/**
    Gets the "constraint type" of a DbORAssociationEnd's instance.

    @return the "constraint type"

 **/
  public final ORConstraintType getConstraintType() throws DbException {
    return (ORConstraintType)get(fConstraintType);
  }

/**
    Gets the list of dependent constraints associated to a DbORAssociationEnd's instance.

    @return the list of dependent constraints.

 **/
  public final DbRelationN getDependentConstraints() throws DbException {
    return (DbRelationN)get(fDependentConstraints);
  }

/**
    Gets the referenced constraint object associated to a DbORAssociationEnd's instance.

    @return the referenced constraint object

 **/
  public final DbORPrimaryUnique getReferencedConstraint() throws DbException {
    return (DbORPrimaryUnique)get(fReferencedConstraint);
  }

  public MetaClass getMetaClass() {
    return metaClass;
  }

}
