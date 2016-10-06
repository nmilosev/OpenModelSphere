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

    <b>Composites : </b><A HREF="../../../../../org/modelsphere/sms/db/DbSMSProject.html">DbSMSProject</A>.<br>
    <b>Components : </b>none.<br>
 **/
public final class DbORNotation extends DbSMSNotation {

  //Meta

  public static final MetaField fNumericDisplay
    = new MetaField(LocaleMgr.db.getString("numericDisplay"));
  public static final MetaField fSymbolicDisplay
    = new MetaField(LocaleMgr.db.getString("symbolicDisplay"));
  public static final MetaField fNumericPosition
    = new MetaField(LocaleMgr.db.getString("numericPosition"));
  public static final MetaField fSymbolicPosition
    = new MetaField(LocaleMgr.db.getString("symbolicPosition"));
  public static final MetaField fSymbolicChildRolePosition
    = new MetaField(LocaleMgr.db.getString("symbolicChildRolePosition"));
  public static final MetaRelationN fDiagrams
    = new MetaRelationN(LocaleMgr.db.getString("diagrams"), 0, MetaRelationN.cardN);
  public static final MetaRelation1 fReferringProjectOr
    = new MetaRelation1(LocaleMgr.db.getString("referringProjectOr"), 0);
  public static final MetaField fMin0Symbol
    = new MetaField(LocaleMgr.db.getString("min0Symbol"));
  public static final MetaField fMin1Symbol
    = new MetaField(LocaleMgr.db.getString("min1Symbol"));
  public static final MetaField fMax1Symbol
    = new MetaField(LocaleMgr.db.getString("max1Symbol"));
  public static final MetaField fMaxNSymbol
    = new MetaField(LocaleMgr.db.getString("maxNSymbol"));
  public static final MetaField fKeyDependencyVisible
    = new MetaField(LocaleMgr.db.getString("keyDependencyVisible"));
  public static final MetaField fKeyDependencySymbol
    = new MetaField(LocaleMgr.db.getString("keyDependencySymbol"));
  public static final MetaField fChildRoleVisible
    = new MetaField(LocaleMgr.db.getString("childRoleVisible"));
  public static final MetaField fChildRoleSymbol
    = new MetaField(LocaleMgr.db.getString("childRoleSymbol"));
  public static final MetaField fBuiltIn
    = new MetaField(LocaleMgr.db.getString("builtIn"));
  public static final MetaField fNumericRepresentation
    = new MetaField(LocaleMgr.db.getString("numericRepresentation"));
  public static final MetaRelation1 fReferringProjectEr
    = new MetaRelation1(LocaleMgr.db.getString("referringProjectEr"), 0);
  public static final MetaField fTerminologyName
    = new MetaField(LocaleMgr.db.getString("terminologyName"));
  public static final MetaField fChoiceShape
    = new MetaField(LocaleMgr.db.getString("choiceShape"));
  public static final MetaField fSpecializationShape
    = new MetaField(LocaleMgr.db.getString("specializationShape"));
  public static final MetaField fShowDependentTables
    = new MetaField(LocaleMgr.db.getString("showDependentTables"));
  public static final MetaField fUnidentifyingAssociationsAreDashed
    = new MetaField(LocaleMgr.db.getString("unidentifyingAssociationsAreDashed"));
  public static final MetaField fSeparateCompartmentForPKs
    = new MetaField(LocaleMgr.db.getString("separateCompartmentForPKs"));

  public static final MetaClass metaClass = new MetaClass(
    LocaleMgr.db.getString("DbORNotation"), DbORNotation.class,
    new MetaField[] {fNumericDisplay,
      fSymbolicDisplay,
      fNumericPosition,
      fSymbolicPosition,
      fSymbolicChildRolePosition,
      fDiagrams,
      fReferringProjectOr,
      fMin0Symbol,
      fMin1Symbol,
      fMax1Symbol,
      fMaxNSymbol,
      fKeyDependencyVisible,
      fKeyDependencySymbol,
      fChildRoleVisible,
      fChildRoleSymbol,
      fBuiltIn,
      fNumericRepresentation,
      fReferringProjectEr,
      fTerminologyName,
      fChoiceShape,
      fSpecializationShape,
      fShowDependentTables,
      fUnidentifyingAssociationsAreDashed,
      fSeparateCompartmentForPKs}, MetaClass.CLUSTER_ROOT | MetaClass.MATCHABLE | MetaClass.NO_UDF);

/**
    For internal use only.
 **/
  public static void initMeta() {
    try {
      metaClass.setSuperMetaClass(DbSMSNotation.metaClass);

      fNumericDisplay.setJField(DbORNotation.class.getDeclaredField("m_numericDisplay"));
      fSymbolicDisplay.setJField(DbORNotation.class.getDeclaredField("m_symbolicDisplay"));
      fNumericPosition.setJField(DbORNotation.class.getDeclaredField("m_numericPosition"));
      fSymbolicPosition.setJField(DbORNotation.class.getDeclaredField("m_symbolicPosition"));
      fSymbolicChildRolePosition.setJField(DbORNotation.class.getDeclaredField("m_symbolicChildRolePosition"));
      fDiagrams.setJField(DbORNotation.class.getDeclaredField("m_diagrams"));
        fDiagrams.setFlags(MetaField.HUGE_RELN);
      fReferringProjectOr.setJField(DbORNotation.class.getDeclaredField("m_referringProjectOr"));
      fMin0Symbol.setJField(DbORNotation.class.getDeclaredField("m_min0Symbol"));
      fMin1Symbol.setJField(DbORNotation.class.getDeclaredField("m_min1Symbol"));
      fMax1Symbol.setJField(DbORNotation.class.getDeclaredField("m_max1Symbol"));
      fMaxNSymbol.setJField(DbORNotation.class.getDeclaredField("m_maxNSymbol"));
      fKeyDependencyVisible.setJField(DbORNotation.class.getDeclaredField("m_keyDependencyVisible"));
      fKeyDependencySymbol.setJField(DbORNotation.class.getDeclaredField("m_keyDependencySymbol"));
      fChildRoleVisible.setJField(DbORNotation.class.getDeclaredField("m_childRoleVisible"));
      fChildRoleSymbol.setJField(DbORNotation.class.getDeclaredField("m_childRoleSymbol"));
      fBuiltIn.setJField(DbORNotation.class.getDeclaredField("m_builtIn"));
      fNumericRepresentation.setJField(DbORNotation.class.getDeclaredField("m_numericRepresentation"));
      fReferringProjectEr.setJField(DbORNotation.class.getDeclaredField("m_referringProjectEr"));
      fTerminologyName.setJField(DbORNotation.class.getDeclaredField("m_terminologyName"));
      fChoiceShape.setJField(DbORNotation.class.getDeclaredField("m_choiceShape"));
      fSpecializationShape.setJField(DbORNotation.class.getDeclaredField("m_specializationShape"));
      fShowDependentTables.setJField(DbORNotation.class.getDeclaredField("m_showDependentTables"));
      fUnidentifyingAssociationsAreDashed.setJField(DbORNotation.class.getDeclaredField("m_unidentifyingAssociationsAreDashed"));
      fSeparateCompartmentForPKs.setJField(DbORNotation.class.getDeclaredField("m_separateCompartmentForPKs"));

      fReferringProjectOr.setOppositeRel(DbSMSProject.fOrDefaultNotation);
      fReferringProjectEr.setOppositeRel(DbSMSProject.fErDefaultNotation);

    }
    catch (Exception e) { throw new RuntimeException("Meta init"); }
  }

  public static final String DATARUN = LocaleMgr.misc.getString("datarun");
  public static final String INFORMATION_ENGINEERING_PLUS = LocaleMgr.misc.getString("infoengplus");
  public static String[] listOptionTabs;
  public static MetaField[][] optionGroups;
  public static Object[][] optionValueGroups;
  public static String[] optionGroupHeaders;
  public static String[] optionGroupComponents;
  public static MetaField[] numericOptions;
  public static Object[] numericOptionDefaultValues;
  public static MetaField[] symbolicOptions;
  public static Object[] symbolicOptionDefaultValues;
  public static final String INFORMATION_ENGINEERING = LocaleMgr.misc.getString("infoeng");
  public static final String LOGICAL_DATA_STRUCTURE = LocaleMgr.misc.getString("logicalDataStruct");
  public static final String UML = LocaleMgr.misc.getString("uml");
  static final long serialVersionUID = -4322034152823977527L;
  public static final String ENTITY_RELATIONSHIP = LocaleMgr.misc.getString("EntityRelationship");

    
  static{
    // Numeric options
    numericOptions = new MetaField[]
      {fNumericDisplay,
      fNumericPosition,
      fNumericRepresentation,
      };

    numericOptionDefaultValues = new Object[]{
      ORConnectivitiesDisplay.getInstance(ORConnectivitiesDisplay.MIN_MAX),
      ORConnectivityPosition.getInstance(ORConnectivityPosition.CLOSE_BY),
      ORNumericRepresentation.getInstance(ORNumericRepresentation.DATARUN)
    };

    // Symbolic options
    symbolicOptions = new MetaField[]
      {fSymbolicDisplay,
      fSymbolicPosition,
      fSymbolicChildRolePosition,
      fMin0Symbol,
      fMin1Symbol,
      fMax1Symbol,
      fMaxNSymbol,
      fKeyDependencyVisible,
      fKeyDependencySymbol,
      fChildRoleVisible,
      fChildRoleSymbol,
      };

    symbolicOptionDefaultValues = new Object[]{
      ORConnectivitiesDisplay.getInstance(ORConnectivitiesDisplay.NONE),
      ORConnectivityPosition.getInstance(ORConnectivityPosition.CLOSE_BY),
      ORConnectivityPosition.getInstance(ORConnectivityPosition.CLOSE_BY),
      ORNotationSymbol.getInstance(ORNotationSymbol.NONE),
      ORNotationSymbol.getInstance(ORNotationSymbol.NONE),
      ORNotationSymbol.getInstance(ORNotationSymbol.NONE),
      ORNotationSymbol.getInstance(ORNotationSymbol.NONE),
      Boolean.FALSE,
      ORNotationSymbol.getInstance(ORNotationSymbol.NONE),
      Boolean.FALSE,
      ORNotationSymbol.getInstance(ORNotationSymbol.NONE),
    };

    listOptionTabs = new String[] {
        "optionGroups", "optionValueGroups", "optionGroupHeaders", "optionGroupComponents"};

    optionGroups = new MetaField[][] {
       numericOptions, symbolicOptions};

    optionValueGroups = new Object[][] {
       numericOptionDefaultValues, symbolicOptionDefaultValues};

    optionGroupHeaders = new String[] {
       LocaleMgr.screen.getString("Numeric"), LocaleMgr.screen.getString("Symbolic")};

    optionGroupComponents = new String[] {
       "org.modelsphere.sms.or.notation.NotationNumericComponent", "org.modelsphere.sms.or.notation.NotationSymbolComponent"};      // NOT LOCALIZABLE - Class name

  }



  //Instance variables
  ORConnectivitiesDisplay m_numericDisplay;
  ORConnectivitiesDisplay m_symbolicDisplay;
  ORConnectivityPosition m_numericPosition;
  ORConnectivityPosition m_symbolicPosition;
  ORConnectivityPosition m_symbolicChildRolePosition;
  DbRelationN m_diagrams;
  DbSMSProject m_referringProjectOr;
  ORNotationSymbol m_min0Symbol;
  ORNotationSymbol m_min1Symbol;
  ORNotationSymbol m_max1Symbol;
  ORNotationSymbol m_maxNSymbol;
  boolean m_keyDependencyVisible;
  ORNotationSymbol m_keyDependencySymbol;
  boolean m_childRoleVisible;
  ORNotationSymbol m_childRoleSymbol;
  boolean m_builtIn;
  ORNumericRepresentation m_numericRepresentation;
  DbSMSProject m_referringProjectEr;
  String m_terminologyName;
  SMSNotationShape m_choiceShape;
  SMSNotationShape m_specializationShape;
  SrBoolean m_showDependentTables;
  SrBoolean m_unidentifyingAssociationsAreDashed;
  SrBoolean m_separateCompartmentForPKs;

  //Constructors

/**
    Parameter-less constructor.  Required by Java Beans Conventions.
 **/
  public DbORNotation() {}

/**
    Creates an instance of DbORNotation.
    @param composite the object which will contain the newly-created instance
 **/
  public DbORNotation(DbObject composite) throws DbException {
    super(composite);
    setDefaultInitialValues();
  }

  private void setDefaultInitialValues() throws DbException {
    setBuiltIn(Boolean.FALSE);
  }

/**

 **/
  public void initOptions() throws DbException {

    for (int i = 0;  i < optionGroups.length;  i++) {
      for (int j = 0;  j < optionGroups[i].length;  j++) {
        basicSet(optionGroups[i][j], optionValueGroups[i][j]);
      }
    }

  }

/**

    @param srcnotation org.modelsphere.sms.or.db.DbORNotation
 **/
  public void copyOptions(DbORNotation srcNotation) throws DbException {

    for (int i = 0;  i < optionGroups.length;  i++) {
      for (int j = 0;  j < optionGroups[i].length;  j++) {
        MetaField metaField = optionGroups[i][j];
        basicSet(metaField, srcNotation.get(metaField));
      }
    }

  }

  //Setters

/**
    Sets the "numeric connectivities displayed" property of a DbORNotation's instance.

    @param value the "numeric connectivities displayed" property

 **/
  public final void setNumericDisplay(ORConnectivitiesDisplay value) throws DbException {
    basicSet(fNumericDisplay, value);
  }

/**
    Sets the "symbolic connectivities displayed" property of a DbORNotation's instance.

    @param value the "symbolic connectivities displayed" property

 **/
  public final void setSymbolicDisplay(ORConnectivitiesDisplay value) throws DbException {
    basicSet(fSymbolicDisplay, value);
  }

/**
    Sets the "numeric connectivity position" property of a DbORNotation's instance.

    @param value the "numeric connectivity position" property

 **/
  public final void setNumericPosition(ORConnectivityPosition value) throws DbException {
    basicSet(fNumericPosition, value);
  }

/**
    Sets the "symbolic connectivity position" property of a DbORNotation's instance.

    @param value the "symbolic connectivity position" property

 **/
  public final void setSymbolicPosition(ORConnectivityPosition value) throws DbException {
    basicSet(fSymbolicPosition, value);
  }

/**
    Sets the "child role position" property of a DbORNotation's instance.

    @param value the "child role position" property

 **/
  public final void setSymbolicChildRolePosition(ORConnectivityPosition value) throws DbException {
    basicSet(fSymbolicChildRolePosition, value);
  }

/**
    Sets the project object associated to a DbORNotation's instance.

    @param value the project object to be associated

 **/
  public final void setReferringProjectOr(DbSMSProject value) throws DbException {
    basicSet(fReferringProjectOr, value);
  }

/**
    Sets the "minimum 0" property of a DbORNotation's instance.

    @param value the "minimum 0" property

 **/
  public final void setMin0Symbol(ORNotationSymbol value) throws DbException {
    basicSet(fMin0Symbol, value);
  }

/**
    Sets the "minimum 1" property of a DbORNotation's instance.

    @param value the "minimum 1" property

 **/
  public final void setMin1Symbol(ORNotationSymbol value) throws DbException {
    basicSet(fMin1Symbol, value);
  }

/**
    Sets the "maximum 1" property of a DbORNotation's instance.

    @param value the "maximum 1" property

 **/
  public final void setMax1Symbol(ORNotationSymbol value) throws DbException {
    basicSet(fMax1Symbol, value);
  }

/**
    Sets the "maximum n" property of a DbORNotation's instance.

    @param value the "maximum n" property

 **/
  public final void setMaxNSymbol(ORNotationSymbol value) throws DbException {
    basicSet(fMaxNSymbol, value);
  }

/**
    Sets the "key dependency visible" property of a DbORNotation's instance.

    @param value the "key dependency visible" property

 **/
  public final void setKeyDependencyVisible(Boolean value) throws DbException {
    basicSet(fKeyDependencyVisible, value);
  }

/**
    Sets the "key dependency symbol" property of a DbORNotation's instance.

    @param value the "key dependency symbol" property

 **/
  public final void setKeyDependencySymbol(ORNotationSymbol value) throws DbException {
    basicSet(fKeyDependencySymbol, value);
  }

/**
    Sets the "child role visible" property of a DbORNotation's instance.

    @param value the "child role visible" property

 **/
  public final void setChildRoleVisible(Boolean value) throws DbException {
    basicSet(fChildRoleVisible, value);
  }

/**
    Sets the "child role symbol" property of a DbORNotation's instance.

    @param value the "child role symbol" property

 **/
  public final void setChildRoleSymbol(ORNotationSymbol value) throws DbException {
    basicSet(fChildRoleSymbol, value);
  }

/**
    Sets the "built in?" property of a DbORNotation's instance.

    @param value the "built in?" property

 **/
  public final void setBuiltIn(Boolean value) throws DbException {
    basicSet(fBuiltIn, value);
  }

/**
    Sets the "numeric style" property of a DbORNotation's instance.

    @param value the "numeric style" property

 **/
  public final void setNumericRepresentation(ORNumericRepresentation value) throws DbException {
    basicSet(fNumericRepresentation, value);
  }

/**
    Sets the referringprojecter object associated to a DbORNotation's instance.

    @param value the referringprojecter object to be associated

 **/
  public final void setReferringProjectEr(DbSMSProject value) throws DbException {
    basicSet(fReferringProjectEr, value);
  }

/**
    Sets the "terminology" property of a DbORNotation's instance.

    @param value the "terminology" property

 **/
  public final void setTerminologyName(String value) throws DbException {
    basicSet(fTerminologyName, value);
  }

/**
    Sets the "shape" property of a DbORNotation's instance.

    @param value the "shape" property

 **/
  public final void setChoiceShape(SMSNotationShape value) throws DbException {
    basicSet(fChoiceShape, value);
  }

/**
    Sets the "shape" property of a DbORNotation's instance.

    @param value the "shape" property

 **/
  public final void setSpecializationShape(SMSNotationShape value) throws DbException {
    basicSet(fSpecializationShape, value);
  }

/**
    Sets the "show dependent tables" property of a DbORNotation's instance.

    @param value the "show dependent tables" property

 **/
  public final void setShowDependentTables(Boolean value) throws DbException {
    basicSet(fShowDependentTables, value);
  }

/**
    Sets the "unidentifying associations are dashed" property of a DbORNotation's instance.

    @param value the "unidentifying associations are dashed" property

 **/
  public final void setUnidentifyingAssociationsAreDashed(Boolean value) throws DbException {
    basicSet(fUnidentifyingAssociationsAreDashed, value);
  }

/**
    Sets the "unidentifying associations are dashed" property of a DbORNotation's instance.

    @param value the "unidentifying associations are dashed" property

 **/
  public final void setSeparateCompartmentForPKs(Boolean value) throws DbException {
    basicSet(fSeparateCompartmentForPKs, value);
  }

/**
    
 **/
  public void set(MetaField metaField, Object value) throws DbException {
    if (metaField.getMetaClass() == metaClass) {
      if (metaField == fDiagrams)
        ((DbORDiagram)value).setNotation(this);
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
    Gets the "numeric connectivities displayed" of a DbORNotation's instance.

    @return the "numeric connectivities displayed"

 **/
  public final ORConnectivitiesDisplay getNumericDisplay() throws DbException {
    return (ORConnectivitiesDisplay)get(fNumericDisplay);
  }

/**
    Gets the "symbolic connectivities displayed" of a DbORNotation's instance.

    @return the "symbolic connectivities displayed"

 **/
  public final ORConnectivitiesDisplay getSymbolicDisplay() throws DbException {
    return (ORConnectivitiesDisplay)get(fSymbolicDisplay);
  }

/**
    Gets the "numeric connectivity position" of a DbORNotation's instance.

    @return the "numeric connectivity position"

 **/
  public final ORConnectivityPosition getNumericPosition() throws DbException {
    return (ORConnectivityPosition)get(fNumericPosition);
  }

/**
    Gets the "symbolic connectivity position" of a DbORNotation's instance.

    @return the "symbolic connectivity position"

 **/
  public final ORConnectivityPosition getSymbolicPosition() throws DbException {
    return (ORConnectivityPosition)get(fSymbolicPosition);
  }

/**
    Gets the "child role position" of a DbORNotation's instance.

    @return the "child role position"

 **/
  public final ORConnectivityPosition getSymbolicChildRolePosition() throws DbException {
    return (ORConnectivityPosition)get(fSymbolicChildRolePosition);
  }

/**
    Gets the list of diagrams associated to a DbORNotation's instance.

    @return the list of diagrams.

 **/
  public final DbRelationN getDiagrams() throws DbException {
    return (DbRelationN)get(fDiagrams);
  }

/**
    Gets the project object associated to a DbORNotation's instance.

    @return the project object

 **/
  public final DbSMSProject getReferringProjectOr() throws DbException {
    return (DbSMSProject)get(fReferringProjectOr);
  }

/**
    Gets the "minimum 0" of a DbORNotation's instance.

    @return the "minimum 0"

 **/
  public final ORNotationSymbol getMin0Symbol() throws DbException {
    return (ORNotationSymbol)get(fMin0Symbol);
  }

/**
    Gets the "minimum 1" of a DbORNotation's instance.

    @return the "minimum 1"

 **/
  public final ORNotationSymbol getMin1Symbol() throws DbException {
    return (ORNotationSymbol)get(fMin1Symbol);
  }

/**
    Gets the "maximum 1" of a DbORNotation's instance.

    @return the "maximum 1"

 **/
  public final ORNotationSymbol getMax1Symbol() throws DbException {
    return (ORNotationSymbol)get(fMax1Symbol);
  }

/**
    Gets the "maximum n" of a DbORNotation's instance.

    @return the "maximum n"

 **/
  public final ORNotationSymbol getMaxNSymbol() throws DbException {
    return (ORNotationSymbol)get(fMaxNSymbol);
  }

/**
    Gets the "key dependency visible" property's Boolean value of a DbORNotation's instance.

    @return the "key dependency visible" property's Boolean value

    @deprecated use isKeyDependencyVisible() method instead
 **/
  public final Boolean getKeyDependencyVisible() throws DbException {
    return (Boolean)get(fKeyDependencyVisible);
  }

/**
    Tells whether a DbORNotation's instance is keyDependencyVisible or not.

    @return boolean

 **/
  public final boolean isKeyDependencyVisible() throws DbException {
    return getKeyDependencyVisible().booleanValue();
  }

/**
    Gets the "key dependency symbol" of a DbORNotation's instance.

    @return the "key dependency symbol"

 **/
  public final ORNotationSymbol getKeyDependencySymbol() throws DbException {
    return (ORNotationSymbol)get(fKeyDependencySymbol);
  }

/**
    Gets the "child role visible" property's Boolean value of a DbORNotation's instance.

    @return the "child role visible" property's Boolean value

    @deprecated use isChildRoleVisible() method instead
 **/
  public final Boolean getChildRoleVisible() throws DbException {
    return (Boolean)get(fChildRoleVisible);
  }

/**
    Tells whether a DbORNotation's instance is childRoleVisible or not.

    @return boolean

 **/
  public final boolean isChildRoleVisible() throws DbException {
    return getChildRoleVisible().booleanValue();
  }

/**
    Gets the "child role symbol" of a DbORNotation's instance.

    @return the "child role symbol"

 **/
  public final ORNotationSymbol getChildRoleSymbol() throws DbException {
    return (ORNotationSymbol)get(fChildRoleSymbol);
  }

/**
    Gets the "built in?" property's Boolean value of a DbORNotation's instance.

    @return the "built in?" property's Boolean value

    @deprecated use isBuiltIn() method instead
 **/
  public final Boolean getBuiltIn() throws DbException {
    return (Boolean)get(fBuiltIn);
  }

/**
    Tells whether a DbORNotation's instance is builtIn or not.

    @return boolean

 **/
  public final boolean isBuiltIn() throws DbException {
    return getBuiltIn().booleanValue();
  }

/**
    Gets the "numeric style" of a DbORNotation's instance.

    @return the "numeric style"

 **/
  public final ORNumericRepresentation getNumericRepresentation() throws DbException {
    return (ORNumericRepresentation)get(fNumericRepresentation);
  }

/**
    Gets the referringprojecter object associated to a DbORNotation's instance.

    @return the referringprojecter object

 **/
  public final DbSMSProject getReferringProjectEr() throws DbException {
    return (DbSMSProject)get(fReferringProjectEr);
  }

/**
    Gets the "terminology" property of a DbORNotation's instance.

    @return the "terminology" property

 **/
  public final String getTerminologyName() throws DbException {
    return (String)get(fTerminologyName);
  }

/**
    Gets the "shape" of a DbORNotation's instance.

    @return the "shape"

 **/
  public final SMSNotationShape getChoiceShape() throws DbException {
    return (SMSNotationShape)get(fChoiceShape);
  }

/**
    Gets the "shape" of a DbORNotation's instance.

    @return the "shape"

 **/
  public final SMSNotationShape getSpecializationShape() throws DbException {
    return (SMSNotationShape)get(fSpecializationShape);
  }

/**
    Gets the "show dependent tables" of a DbORNotation's instance.

    @return the "show dependent tables"

 **/
  public final Boolean getShowDependentTables() throws DbException {
    return (Boolean)get(fShowDependentTables);
  }

/**
    Gets the "unidentifying associations are dashed" of a DbORNotation's instance.

    @return the "unidentifying associations are dashed"

 **/
  public final Boolean getUnidentifyingAssociationsAreDashed() throws DbException {
    return (Boolean)get(fUnidentifyingAssociationsAreDashed);
  }

/**
    Gets the "unidentifying associations are dashed" of a DbORNotation's instance.

    @return the "unidentifying associations are dashed"

 **/
  public final Boolean getSeparateCompartmentForPKs() throws DbException {
    return (Boolean)get(fSeparateCompartmentForPKs);
  }

  public MetaClass getMetaClass() {
    return metaClass;
  }

}
