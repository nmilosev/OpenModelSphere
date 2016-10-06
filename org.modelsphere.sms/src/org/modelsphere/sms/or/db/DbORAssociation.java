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

    <b>Composites : </b><A HREF="../../../../../org/modelsphere/sms/or/db/DbORDataModel.html">DbORDataModel</A>.<br>
    <b>Components : </b><A HREF="../../../../../org/modelsphere/sms/or/db/DbORAssociationEnd.html">DbORAssociationEnd</A>, <A HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbORAssociation extends DbSMSAssociation {

  //Meta

  public static final MetaField fIsIdentifying
    = new MetaField(LocaleMgr.db.getString("isIdentifying"));
  public static final MetaField fIsArc
    = new MetaField(LocaleMgr.db.getString("isArc"));
  public static final MetaRelation1 fChoiceOrSpecialization
    = new MetaRelation1(LocaleMgr.db.getString("choiceOrSpecialization"), 0);

  public static final MetaClass metaClass = new MetaClass(
    LocaleMgr.db.getString("DbORAssociation"), DbORAssociation.class,
    new MetaField[] {fIsIdentifying,
      fIsArc,
      fChoiceOrSpecialization}, 0);

/**
    For internal use only.
 **/
  public static void initMeta() {
    try {
      metaClass.setSuperMetaClass(DbSMSAssociation.metaClass);
      metaClass.setComponentMetaClasses(new MetaClass[] {DbORAssociationEnd.metaClass});
      metaClass.setIcon("dborassociation.gif");

      fIsIdentifying.setJField(DbORAssociation.class.getDeclaredField("m_isIdentifying"));
      fIsArc.setJField(DbORAssociation.class.getDeclaredField("m_isArc"));
        fIsArc.setVisibleInScreen(false);
      fChoiceOrSpecialization.setJField(DbORAssociation.class.getDeclaredField("m_choiceOrSpecialization"));

      fChoiceOrSpecialization.setOppositeRel(DbORChoiceOrSpecialization.fAssociations);

    }
    catch (Exception e) { throw new RuntimeException("Meta init"); }
  }

  static final long serialVersionUID = 0;

  //Instance variables
  boolean m_isIdentifying;
  boolean m_isArc;
  DbORChoiceOrSpecialization m_choiceOrSpecialization;

  //Constructors

/**
    Parameter-less constructor.  Required by Java Beans Conventions.
 **/
  public DbORAssociation() {}

/**
    Creates an instance of DbORAssociation.

    @param frontendclassifier org.modelsphere.sms.or.db.DbORAbsTable
    @param frontendmult org.modelsphere.sms.db.srtypes.SMSMultiplicity
    @param backendclassifier org.modelsphere.sms.or.db.DbORAbsTable
    @param backendmult org.modelsphere.sms.db.srtypes.SMSMultiplicity
 **/
  public DbORAssociation(DbORAbsTable frontEndClassifier, SMSMultiplicity frontEndMult, DbORAbsTable backEndClassifier, SMSMultiplicity backEndMult) throws DbException {
    super(frontEndClassifier.getComposite());
    setDefaultInitialValues();

    DbORDataModel dataModel = (DbORDataModel)getCompositeOfType(DbORDataModel.metaClass);
    TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();  

    if(terminologyUtil.getModelLogicalMode(dataModel) == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP){
	    new DbORAssociationEnd(this, frontEndClassifier, frontEndMult, backEndClassifier.getName());
	    new DbORAssociationEnd(this, backEndClassifier, backEndMult, frontEndClassifier.getName());
    }
    else{
	    new DbORAssociationEnd(this, frontEndClassifier, frontEndMult, frontEndClassifier.getName());
	    new DbORAssociationEnd(this, backEndClassifier, backEndMult, backEndClassifier.getName());
    }
  }

  private void setDefaultInitialValues() throws DbException {
    setIsArc(Boolean.FALSE);
    setIsArc(Boolean.FALSE);

  }

/**

    @return role
 **/
  public final DbORAssociationEnd getFrontEnd() throws DbException {
    return (DbORAssociationEnd)getComponents().elementAt(0);
  }

/**

    @return role
 **/
  public final DbORAssociationEnd getBackEnd() throws DbException {
    return (DbORAssociationEnd)getComponents().elementAt(1);
  }

/**

    @return role
 **/
  public final DbORAssociationEnd getArcEnd() throws DbException {
    return (DbORAssociationEnd)getBackEnd();
  }

/**

    @param metarel org.modelsphere.jack.baseDb.meta.MetaRelationship
    @return int
 **/
  public final int getMinCard(MetaRelationship metaRel) throws DbException {
    if (metaRel == DbObject.fComponents)
      return 2;
    return metaRel.getMinCard();
  }

  //Setters

/**
    Sets the "is identifying" property of a DbORAssociation's instance.

    @param value the "is identifying" property

 **/
  public final void setIsIdentifying(Boolean value) throws DbException {
    basicSet(fIsIdentifying, value);
  }

/**
    Sets the "is an arc" property of a DbORAssociation's instance.

    @param value the "is an arc" property

 **/
  public final void setIsArc(Boolean value) throws DbException {
    basicSet(fIsArc, value);
  }

/**
    Sets the choiceorspecialization object associated to a DbORAssociation's instance.

    @param value the choiceorspecialization object to be associated

 **/
  public final void setChoiceOrSpecialization(DbORChoiceOrSpecialization value) throws DbException {
    basicSet(fChoiceOrSpecialization, value);
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
    Gets the "is identifying" property's Boolean value of a DbORAssociation's instance.

    @return the "is identifying" property's Boolean value

    @deprecated use isIsIdentifying() method instead
 **/
  public final Boolean getIsIdentifying() throws DbException {
    return (Boolean)get(fIsIdentifying);
  }

/**
    Tells whether a DbORAssociation's instance is isIdentifying or not.

    @return boolean

 **/
  public final boolean isIsIdentifying() throws DbException {
    return getIsIdentifying().booleanValue();
  }

/**
    Gets the "is an arc" property's Boolean value of a DbORAssociation's instance.

    @return the "is an arc" property's Boolean value

    @deprecated use isIsArc() method instead
 **/
  public final Boolean getIsArc() throws DbException {
    return (Boolean)get(fIsArc);
  }

/**
    Tells whether a DbORAssociation's instance is isArc or not.

    @return boolean

 **/
  public final boolean isIsArc() throws DbException {
    return getIsArc().booleanValue();
  }

/**
    Gets the choiceorspecialization object associated to a DbORAssociation's instance.

    @return the choiceorspecialization object

 **/
  public final DbORChoiceOrSpecialization getChoiceOrSpecialization() throws DbException {
    return (DbORChoiceOrSpecialization)get(fChoiceOrSpecialization);
  }

  public MetaClass getMetaClass() {
    return metaClass;
  }

}
