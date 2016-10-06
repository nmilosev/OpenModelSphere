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
package org.modelsphere.sms.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.international.LocaleMgr;
import java.util.HashMap;

/**
<b>Direct subclass(es)/subinterface(s) : </b><A HREF="../../../../org/modelsphere/sms/oo/db/DbOOStyle.html">DbOOStyle</A>, <A HREF="../../../../org/modelsphere/sms/or/db/DbORStyle.html">DbORStyle</A>, <A HREF="../../../../org/modelsphere/sms/or/db/DbORDomainStyle.html">DbORDomainStyle</A>, <A HREF="../../../../org/modelsphere/sms/or/db/DbORCommonItemStyle.html">DbORCommonItemStyle</A>, <A HREF="../../../../org/modelsphere/sms/be/db/DbBEStyle.html">DbBEStyle</A>.<br>

    <b>Composites : </b><A HREF="../../../../org/modelsphere/sms/db/DbSMSProject.html">DbSMSProject</A>.<br>
    <b>Components : </b>none.<br>
 **/
public abstract class DbSMSStyle extends DbObject implements DbStyleI {

  //Meta

  public static final MetaField fName
    = new MetaField(LocaleMgr.db.getString("DbSMSStyle.name"));
  public static final MetaRelationN fDescendants
    = new MetaRelationN(LocaleMgr.db.getString("descendants"), 0, MetaRelationN.cardN);
  public static final MetaRelation1 fAncestor
    = new MetaRelation1(LocaleMgr.db.getString("ancestor"), 0);
  public static final MetaRelationN fDiagrams
    = new MetaRelationN(LocaleMgr.db.getString("diagrams"), 0, MetaRelationN.cardN);
  public static final MetaRelationN fGraphicalObjects
    = new MetaRelationN(LocaleMgr.db.getString("graphicalObjects"), 0, MetaRelationN.cardN);
  public static final MetaField fLineColorDbSMSInheritance
    = new MetaField(LocaleMgr.db.getString("lineColorDbSMSInheritance"));
  public static final MetaField fLineColorDbSMSAssociation
    = new MetaField(LocaleMgr.db.getString("lineColorDbSMSAssociation"));
  public static final MetaField fDashStyleDbSMSAssociation
    = new MetaField(LocaleMgr.db.getString("dashStyleDbSMSAssociation"));
  public static final MetaField fHighlightDbSMSInheritance
    = new MetaField(LocaleMgr.db.getString("highlightDbSMSInheritance"));
  public static final MetaField fDashStyleDbSMSInheritance
    = new MetaField(LocaleMgr.db.getString("dashStyleDbSMSInheritance"));
  public static final MetaField fHighlightDbSMSAssociation
    = new MetaField(LocaleMgr.db.getString("highlightDbSMSAssociation"));
  public static final MetaField fSms_dataModelDescriptorFont
    = new MetaField(LocaleMgr.db.getString("sms_dataModelDescriptorFont"));
  public static final MetaField fSms_dataModelDescriptor
    = new MetaField(LocaleMgr.db.getString("sms_dataModelDescriptor"));
  public static final MetaField fSms_dataModelDescriptorColor
    = new MetaField(LocaleMgr.db.getString("sms_dataModelDescriptorColor"));
  public static final MetaField fSms_dataModelBorderColor
    = new MetaField(LocaleMgr.db.getString("sms_dataModelBorderColor"));
  public static final MetaField fSms_dataModelBackgroundColor
    = new MetaField(LocaleMgr.db.getString("sms_dataModelBackgroundColor"));
  public static final MetaField fDefaultStyle
    = new MetaField(LocaleMgr.db.getString("DbSMSStyle.defaultStyle"));
  public static final MetaField fCategory
    = new MetaField(LocaleMgr.db.getString("DbSMSStyle.category"));
  public static final MetaField fDashStyleDbSMSPackage
    = new MetaField(LocaleMgr.db.getString("dashStyleDbSMSPackage"));
  public static final MetaField fBackgroundColorDbSMSPackage
    = new MetaField(LocaleMgr.db.getString("backgroundColorDbSMSPackage"));
  public static final MetaField fLineColorDbSMSPackage
    = new MetaField(LocaleMgr.db.getString("lineColorDbSMSPackage"));
  public static final MetaField fHighlightDbSMSPackage
    = new MetaField(LocaleMgr.db.getString("highlightDbSMSPackage"));
  public static final MetaField fTextColorDbSMSPackage
    = new MetaField(LocaleMgr.db.getString("textColorDbSMSPackage"));
  public static final MetaField fSms_packageNameFont
    = new MetaField(LocaleMgr.db.getString("sms_packageNameFont"));
  public static final MetaField fBackgroundColorDbSMSNotice
    = new MetaField(LocaleMgr.db.getString("backgroundColorDbSMSNotice"));
  public static final MetaField fTextColorDbSMSNotice
    = new MetaField(LocaleMgr.db.getString("textColorDbSMSNotice"));
  public static final MetaField fLineColorDbSMSNotice
    = new MetaField(LocaleMgr.db.getString("lineColorDbSMSNotice"));
  public static final MetaField fDashStyleDbSMSNotice
    = new MetaField(LocaleMgr.db.getString("dashStyleDbSMSNotice"));
  public static final MetaField fHighlightDbSMSNotice
    = new MetaField(LocaleMgr.db.getString("highlightDbSMSNotice"));
  public static final MetaField fLineColorDbSMSGraphicalLink
    = new MetaField(LocaleMgr.db.getString("lineColorDbSMSGraphicalLink"));
  public static final MetaField fHighlightDbSMSGraphicalLink
    = new MetaField(LocaleMgr.db.getString("highlightDbSMSGraphicalLink"));
  public static final MetaField fDashStyleDbSMSGraphicalLink
    = new MetaField(LocaleMgr.db.getString("dashStyleDbSMSGraphicalLink"));
  public static final MetaRelationN fUsedInNotations
    = new MetaRelationN(LocaleMgr.db.getString("usedInNotations"), 0, MetaRelationN.cardN);
  public static final MetaField fWarningPrefix
    = new MetaField(LocaleMgr.db.getString("warningPrefix"));
  public static final MetaField fErrorPrefix
    = new MetaField(LocaleMgr.db.getString("errorPrefix"));
  public static final MetaField fSourcePrefix
    = new MetaField(LocaleMgr.db.getString("sourcePrefix"));
  public static final MetaField fTargetPrefix
    = new MetaField(LocaleMgr.db.getString("targetPrefix"));

  public static final MetaClass metaClass = new MetaClass(
    LocaleMgr.db.getString("DbSMSStyle"), DbSMSStyle.class,
    new MetaField[] {fName,
      fDescendants,
      fAncestor,
      fDiagrams,
      fGraphicalObjects,
      fLineColorDbSMSInheritance,
      fLineColorDbSMSAssociation,
      fDashStyleDbSMSAssociation,
      fHighlightDbSMSInheritance,
      fDashStyleDbSMSInheritance,
      fHighlightDbSMSAssociation,
      fSms_dataModelDescriptorFont,
      fSms_dataModelDescriptor,
      fSms_dataModelDescriptorColor,
      fSms_dataModelBorderColor,
      fSms_dataModelBackgroundColor,
      fDefaultStyle,
      fCategory,
      fDashStyleDbSMSPackage,
      fBackgroundColorDbSMSPackage,
      fLineColorDbSMSPackage,
      fHighlightDbSMSPackage,
      fTextColorDbSMSPackage,
      fSms_packageNameFont,
      fBackgroundColorDbSMSNotice,
      fTextColorDbSMSNotice,
      fLineColorDbSMSNotice,
      fDashStyleDbSMSNotice,
      fHighlightDbSMSNotice,
      fLineColorDbSMSGraphicalLink,
      fHighlightDbSMSGraphicalLink,
      fDashStyleDbSMSGraphicalLink,
      fUsedInNotations,
      fWarningPrefix,
      fErrorPrefix,
      fSourcePrefix,
      fTargetPrefix}, 0);

/**
    For internal use only.
 **/
  public static void initMeta() {
    try {
      metaClass.setSuperMetaClass(DbObject.metaClass);
      metaClass.setIcon("dbsmsstyle.gif");

      fName.setJField(DbSMSStyle.class.getDeclaredField("m_name"));
      fDescendants.setJField(DbSMSStyle.class.getDeclaredField("m_descendants"));
      fAncestor.setJField(DbSMSStyle.class.getDeclaredField("m_ancestor"));
        fAncestor.setFlags(MetaField.COPY_REFS);
      fDiagrams.setJField(DbSMSStyle.class.getDeclaredField("m_diagrams"));
        fDiagrams.setFlags(MetaField.HUGE_RELN);
      fGraphicalObjects.setJField(DbSMSStyle.class.getDeclaredField("m_graphicalObjects"));
        fGraphicalObjects.setFlags(MetaField.HUGE_RELN);
      fLineColorDbSMSInheritance.setJField(DbSMSStyle.class.getDeclaredField("m_lineColorDbSMSInheritance"));
      fLineColorDbSMSAssociation.setJField(DbSMSStyle.class.getDeclaredField("m_lineColorDbSMSAssociation"));
      fDashStyleDbSMSAssociation.setJField(DbSMSStyle.class.getDeclaredField("m_dashStyleDbSMSAssociation"));
      fHighlightDbSMSInheritance.setJField(DbSMSStyle.class.getDeclaredField("m_highlightDbSMSInheritance"));
      fDashStyleDbSMSInheritance.setJField(DbSMSStyle.class.getDeclaredField("m_dashStyleDbSMSInheritance"));
      fHighlightDbSMSAssociation.setJField(DbSMSStyle.class.getDeclaredField("m_highlightDbSMSAssociation"));
      fSms_dataModelDescriptorFont.setJField(DbSMSStyle.class.getDeclaredField("m_sms_dataModelDescriptorFont"));
      fSms_dataModelDescriptor.setJField(DbSMSStyle.class.getDeclaredField("m_sms_dataModelDescriptor"));
      fSms_dataModelDescriptorColor.setJField(DbSMSStyle.class.getDeclaredField("m_sms_dataModelDescriptorColor"));
      fSms_dataModelBorderColor.setJField(DbSMSStyle.class.getDeclaredField("m_sms_dataModelBorderColor"));
      fSms_dataModelBackgroundColor.setJField(DbSMSStyle.class.getDeclaredField("m_sms_dataModelBackgroundColor"));
      fDefaultStyle.setJField(DbSMSStyle.class.getDeclaredField("m_defaultStyle"));
      fCategory.setJField(DbSMSStyle.class.getDeclaredField("m_category"));
      fDashStyleDbSMSPackage.setJField(DbSMSStyle.class.getDeclaredField("m_dashStyleDbSMSPackage"));
      fBackgroundColorDbSMSPackage.setJField(DbSMSStyle.class.getDeclaredField("m_backgroundColorDbSMSPackage"));
      fLineColorDbSMSPackage.setJField(DbSMSStyle.class.getDeclaredField("m_lineColorDbSMSPackage"));
      fHighlightDbSMSPackage.setJField(DbSMSStyle.class.getDeclaredField("m_highlightDbSMSPackage"));
      fTextColorDbSMSPackage.setJField(DbSMSStyle.class.getDeclaredField("m_textColorDbSMSPackage"));
      fSms_packageNameFont.setJField(DbSMSStyle.class.getDeclaredField("m_sms_packageNameFont"));
      fBackgroundColorDbSMSNotice.setJField(DbSMSStyle.class.getDeclaredField("m_backgroundColorDbSMSNotice"));
      fTextColorDbSMSNotice.setJField(DbSMSStyle.class.getDeclaredField("m_textColorDbSMSNotice"));
      fLineColorDbSMSNotice.setJField(DbSMSStyle.class.getDeclaredField("m_lineColorDbSMSNotice"));
      fDashStyleDbSMSNotice.setJField(DbSMSStyle.class.getDeclaredField("m_dashStyleDbSMSNotice"));
      fHighlightDbSMSNotice.setJField(DbSMSStyle.class.getDeclaredField("m_highlightDbSMSNotice"));
      fLineColorDbSMSGraphicalLink.setJField(DbSMSStyle.class.getDeclaredField("m_lineColorDbSMSGraphicalLink"));
      fHighlightDbSMSGraphicalLink.setJField(DbSMSStyle.class.getDeclaredField("m_highlightDbSMSGraphicalLink"));
      fDashStyleDbSMSGraphicalLink.setJField(DbSMSStyle.class.getDeclaredField("m_dashStyleDbSMSGraphicalLink"));
      fUsedInNotations.setJField(DbSMSStyle.class.getDeclaredField("m_usedInNotations"));
      fWarningPrefix.setJField(DbSMSStyle.class.getDeclaredField("m_warningPrefix"));
      fErrorPrefix.setJField(DbSMSStyle.class.getDeclaredField("m_errorPrefix"));
      fSourcePrefix.setJField(DbSMSStyle.class.getDeclaredField("m_sourcePrefix"));
      fTargetPrefix.setJField(DbSMSStyle.class.getDeclaredField("m_targetPrefix"));

      fAncestor.setOppositeRel(DbSMSStyle.fDescendants);

    }
    catch (Exception e) { throw new RuntimeException("Meta init"); }
  }

  static final long serialVersionUID =  -8958192070188200162L;
  private static HashMap fieldMap;
  public static String DEFAULT_STYLE_NAME = LocaleMgr.misc.getString( "DefaultStyle");

 

  //Instance variables
  String m_name;
  DbRelationN m_descendants;
  DbSMSStyle m_ancestor;
  DbRelationN m_diagrams;
  DbRelationN m_graphicalObjects;
  SrColor m_lineColorDbSMSInheritance;
  SrColor m_lineColorDbSMSAssociation;
  SrBoolean m_dashStyleDbSMSAssociation;
  SrBoolean m_highlightDbSMSInheritance;
  SrBoolean m_dashStyleDbSMSInheritance;
  SrBoolean m_highlightDbSMSAssociation;
  SrFont m_sms_dataModelDescriptorFont;
  SMSDisplayDescriptor m_sms_dataModelDescriptor;
  SrColor m_sms_dataModelDescriptorColor;
  SrColor m_sms_dataModelBorderColor;
  SrColor m_sms_dataModelBackgroundColor;
  boolean m_defaultStyle;
  SMSStyleCategory m_category;
  SrBoolean m_dashStyleDbSMSPackage;
  SrColor m_backgroundColorDbSMSPackage;
  SrColor m_lineColorDbSMSPackage;
  SrBoolean m_highlightDbSMSPackage;
  SrColor m_textColorDbSMSPackage;
  SrFont m_sms_packageNameFont;
  SrColor m_backgroundColorDbSMSNotice;
  SrColor m_textColorDbSMSNotice;
  SrColor m_lineColorDbSMSNotice;
  SrBoolean m_dashStyleDbSMSNotice;
  SrBoolean m_highlightDbSMSNotice;
  SrColor m_lineColorDbSMSGraphicalLink;
  SrBoolean m_highlightDbSMSGraphicalLink;
  SrBoolean m_dashStyleDbSMSGraphicalLink;
  DbRelationN m_usedInNotations;
  DbtPrefix m_warningPrefix;
  DbtPrefix m_errorPrefix;
  DbtPrefix m_sourcePrefix;
  DbtPrefix m_targetPrefix;

  //Constructors

/**
    Parameter-less constructor.  Required by Java Beans Conventions.
 **/
  public DbSMSStyle() {}

/**
    Creates an instance of DbSMSStyle.

    @param composite org.modelsphere.jack.baseDb.db.DbObject
 **/
  public DbSMSStyle(DbObject composite) throws DbException {
    super(composite);
    setDefaultInitialValues();
  }

/**
    Creates an instance of DbSMSStyle.

    @param composite org.modelsphere.jack.baseDb.db.DbObject
    @param category org.modelsphere.sms.db.srtypes.SMSStyleCategory
 **/
  public DbSMSStyle(DbObject composite, SMSStyleCategory category) throws DbException {
    this(composite);
    setCategory(category);
  }

  private void setDefaultInitialValues() throws DbException {
    setDefaultStyle(Boolean.FALSE);
  }

/**

    @param metafield org.modelsphere.jack.baseDb.meta.MetaField
    @return object
 **/
  public final Object find(MetaField metaField) throws DbException {
    Object element = org.modelsphere.sms.db.util.DbInitialization.findStyleElement(this, metaField); 
    return element;

  }

/**

    @param name java.lang.String
    @return metafield
 **/
  public abstract MetaField getMetaField(String name);

/**

    @param dbo org.modelsphere.jack.baseDb.db.DbObject
    @return boolean
 **/
  public boolean matches(DbObject dbo) throws DbException {
    return DbObject.valuesAreEqual(getName(), dbo.getName());
  }

  //Setters

/**
    Sets the "name" property of a DbSMSStyle's instance.

    @param value the "name" property

 **/
  public final void setName(String value) throws DbException {
    basicSet(fName, value);
  }

/**
    Sets the ancestor object associated to a DbSMSStyle's instance.

    @param value the ancestor object to be associated

 **/
  public final void setAncestor(DbSMSStyle value) throws DbException {
    basicSet(fAncestor, value);
  }

/**
    Sets the "inheritance line" property of a DbSMSStyle's instance.

    @param value the "inheritance line" property

 **/
  public final void setLineColorDbSMSInheritance(Color value) throws DbException {
    basicSet(fLineColorDbSMSInheritance, value);
  }

/**
    Sets the "association line" property of a DbSMSStyle's instance.

    @param value the "association line" property

 **/
  public final void setLineColorDbSMSAssociation(Color value) throws DbException {
    basicSet(fLineColorDbSMSAssociation, value);
  }

/**
    Sets the "association dash style" property of a DbSMSStyle's instance.

    @param value the "association dash style" property

 **/
  public final void setDashStyleDbSMSAssociation(Boolean value) throws DbException {
    basicSet(fDashStyleDbSMSAssociation, value);
  }

/**
    Sets the "inheritance highlight" property of a DbSMSStyle's instance.

    @param value the "inheritance highlight" property

 **/
  public final void setHighlightDbSMSInheritance(Boolean value) throws DbException {
    basicSet(fHighlightDbSMSInheritance, value);
  }

/**
    Sets the "inheritance dash style" property of a DbSMSStyle's instance.

    @param value the "inheritance dash style" property

 **/
  public final void setDashStyleDbSMSInheritance(Boolean value) throws DbException {
    basicSet(fDashStyleDbSMSInheritance, value);
  }

/**
    Sets the "association highlight" property of a DbSMSStyle's instance.

    @param value the "association highlight" property

 **/
  public final void setHighlightDbSMSAssociation(Boolean value) throws DbException {
    basicSet(fHighlightDbSMSAssociation, value);
  }

/**
    Sets the "data model descriptor" property of a DbSMSStyle's instance.

    @param value the "data model descriptor" property

 **/
  public final void setSms_dataModelDescriptorFont(Font value) throws DbException {
    basicSet(fSms_dataModelDescriptorFont, value);
  }

/**
    Sets the "data model descriptor" property of a DbSMSStyle's instance.

    @param value the "data model descriptor" property

 **/
  public final void setSms_dataModelDescriptor(SMSDisplayDescriptor value) throws DbException {
    basicSet(fSms_dataModelDescriptor, value);
  }

/**
    Sets the "data model descriptor" property of a DbSMSStyle's instance.

    @param value the "data model descriptor" property

 **/
  public final void setSms_dataModelDescriptorColor(Color value) throws DbException {
    basicSet(fSms_dataModelDescriptorColor, value);
  }

/**
    Sets the "data model border" property of a DbSMSStyle's instance.

    @param value the "data model border" property

 **/
  public final void setSms_dataModelBorderColor(Color value) throws DbException {
    basicSet(fSms_dataModelBorderColor, value);
  }

/**
    Sets the "data model background" property of a DbSMSStyle's instance.

    @param value the "data model background" property

 **/
  public final void setSms_dataModelBackgroundColor(Color value) throws DbException {
    basicSet(fSms_dataModelBackgroundColor, value);
  }

/**
    Sets the "default style?" property of a DbSMSStyle's instance.

    @param value the "default style?" property

 **/
  public final void setDefaultStyle(Boolean value) throws DbException {
    basicSet(fDefaultStyle, value);
  }

/**
    Sets the "category" property of a DbSMSStyle's instance.

    @param value the "category" property

 **/
  public final void setCategory(SMSStyleCategory value) throws DbException {
    basicSet(fCategory, value);
  }

/**
    Sets the "package dash style" property of a DbSMSStyle's instance.

    @param value the "package dash style" property

 **/
  public final void setDashStyleDbSMSPackage(Boolean value) throws DbException {
    basicSet(fDashStyleDbSMSPackage, value);
  }

/**
    Sets the "package background" property of a DbSMSStyle's instance.

    @param value the "package background" property

 **/
  public final void setBackgroundColorDbSMSPackage(Color value) throws DbException {
    basicSet(fBackgroundColorDbSMSPackage, value);
  }

/**
    Sets the "package border" property of a DbSMSStyle's instance.

    @param value the "package border" property

 **/
  public final void setLineColorDbSMSPackage(Color value) throws DbException {
    basicSet(fLineColorDbSMSPackage, value);
  }

/**
    Sets the "package highlight" property of a DbSMSStyle's instance.

    @param value the "package highlight" property

 **/
  public final void setHighlightDbSMSPackage(Boolean value) throws DbException {
    basicSet(fHighlightDbSMSPackage, value);
  }

/**
    Sets the "package name" property of a DbSMSStyle's instance.

    @param value the "package name" property

 **/
  public final void setTextColorDbSMSPackage(Color value) throws DbException {
    basicSet(fTextColorDbSMSPackage, value);
  }

/**
    Sets the "package name" property of a DbSMSStyle's instance.

    @param value the "package name" property

 **/
  public final void setSms_packageNameFont(Font value) throws DbException {
    basicSet(fSms_packageNameFont, value);
  }

/**
    Sets the "notice background" property of a DbSMSStyle's instance.

    @param value the "notice background" property

 **/
  public final void setBackgroundColorDbSMSNotice(Color value) throws DbException {
    basicSet(fBackgroundColorDbSMSNotice, value);
  }

/**
    Sets the "notice text" property of a DbSMSStyle's instance.

    @param value the "notice text" property

 **/
  public final void setTextColorDbSMSNotice(Color value) throws DbException {
    basicSet(fTextColorDbSMSNotice, value);
  }

/**
    Sets the "notice border" property of a DbSMSStyle's instance.

    @param value the "notice border" property

 **/
  public final void setLineColorDbSMSNotice(Color value) throws DbException {
    basicSet(fLineColorDbSMSNotice, value);
  }

/**
    Sets the "notice dash style" property of a DbSMSStyle's instance.

    @param value the "notice dash style" property

 **/
  public final void setDashStyleDbSMSNotice(Boolean value) throws DbException {
    basicSet(fDashStyleDbSMSNotice, value);
  }

/**
    Sets the "notice highlight" property of a DbSMSStyle's instance.

    @param value the "notice highlight" property

 **/
  public final void setHighlightDbSMSNotice(Boolean value) throws DbException {
    basicSet(fHighlightDbSMSNotice, value);
  }

/**
    Sets the "graphical link color" property of a DbSMSStyle's instance.

    @param value the "graphical link color" property

 **/
  public final void setLineColorDbSMSGraphicalLink(Color value) throws DbException {
    basicSet(fLineColorDbSMSGraphicalLink, value);
  }

/**
    Sets the "graphical link highlight" property of a DbSMSStyle's instance.

    @param value the "graphical link highlight" property

 **/
  public final void setHighlightDbSMSGraphicalLink(Boolean value) throws DbException {
    basicSet(fHighlightDbSMSGraphicalLink, value);
  }

/**
    Sets the "graphical link dash style" property of a DbSMSStyle's instance.

    @param value the "graphical link dash style" property

 **/
  public final void setDashStyleDbSMSGraphicalLink(Boolean value) throws DbException {
    basicSet(fDashStyleDbSMSGraphicalLink, value);
  }

/**
    Sets the warning object associated to a DbSMSStyle's instance.

    @param value the warning object to be associated

 **/
  public final void setWarningPrefix(DbtPrefix value) throws DbException {
    basicSet(fWarningPrefix, value);
  }

/**
    Sets the error object associated to a DbSMSStyle's instance.

    @param value the error object to be associated

 **/
  public final void setErrorPrefix(DbtPrefix value) throws DbException {
    basicSet(fErrorPrefix, value);
  }

/**
    Sets the source object associated to a DbSMSStyle's instance.

    @param value the source object to be associated

 **/
  public final void setSourcePrefix(DbtPrefix value) throws DbException {
    basicSet(fSourcePrefix, value);
  }

/**
    Sets the target object associated to a DbSMSStyle's instance.

    @param value the target object to be associated

 **/
  public final void setTargetPrefix(DbtPrefix value) throws DbException {
    basicSet(fTargetPrefix, value);
  }

/**
    
 **/
  public void set(MetaField metaField, Object value) throws DbException {
    if (metaField.getMetaClass() == metaClass) {
      if (metaField == fDescendants)
        ((DbSMSStyle)value).setAncestor(this);
      else if (metaField == fDiagrams)
        ((DbSMSDiagram)value).setStyle(this);
      else if (metaField == fGraphicalObjects)
        ((DbSMSGraphicalObject)value).setStyle(this);
      else if (metaField == fUsedInNotations)
        ((DbSMSNotation)value).setDefaultStyle(this);
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
    Gets the "name" property of a DbSMSStyle's instance.

    @return the "name" property

 **/
  public final String getName() throws DbException {
    return (String)get(fName);
  }

/**
    Gets the list of descendants associated to a DbSMSStyle's instance.

    @return the list of descendants.

 **/
  public final DbRelationN getDescendants() throws DbException {
    return (DbRelationN)get(fDescendants);
  }

/**
    Gets the ancestor object associated to a DbSMSStyle's instance.

    @return the ancestor object

 **/
  public final DbSMSStyle getAncestor() throws DbException {
    return (DbSMSStyle)get(fAncestor);
  }

/**
    Gets the list of diagrams associated to a DbSMSStyle's instance.

    @return the list of diagrams.

 **/
  public final DbRelationN getDiagrams() throws DbException {
    return (DbRelationN)get(fDiagrams);
  }

/**
    Gets the list of graphical objects associated to a DbSMSStyle's instance.

    @return the list of graphical objects.

 **/
  public final DbRelationN getGraphicalObjects() throws DbException {
    return (DbRelationN)get(fGraphicalObjects);
  }

/**
    Gets the "inheritance line" of a DbSMSStyle's instance.

    @return the "inheritance line"

 **/
  public final Color getLineColorDbSMSInheritance() throws DbException {
    return (Color)get(fLineColorDbSMSInheritance);
  }

/**
    Gets the "association line" of a DbSMSStyle's instance.

    @return the "association line"

 **/
  public final Color getLineColorDbSMSAssociation() throws DbException {
    return (Color)get(fLineColorDbSMSAssociation);
  }

/**
    Gets the "association dash style" of a DbSMSStyle's instance.

    @return the "association dash style"

 **/
  public final Boolean getDashStyleDbSMSAssociation() throws DbException {
    return (Boolean)get(fDashStyleDbSMSAssociation);
  }

/**
    Gets the "inheritance highlight" of a DbSMSStyle's instance.

    @return the "inheritance highlight"

 **/
  public final Boolean getHighlightDbSMSInheritance() throws DbException {
    return (Boolean)get(fHighlightDbSMSInheritance);
  }

/**
    Gets the "inheritance dash style" of a DbSMSStyle's instance.

    @return the "inheritance dash style"

 **/
  public final Boolean getDashStyleDbSMSInheritance() throws DbException {
    return (Boolean)get(fDashStyleDbSMSInheritance);
  }

/**
    Gets the "association highlight" of a DbSMSStyle's instance.

    @return the "association highlight"

 **/
  public final Boolean getHighlightDbSMSAssociation() throws DbException {
    return (Boolean)get(fHighlightDbSMSAssociation);
  }

/**
    Gets the "data model descriptor" of a DbSMSStyle's instance.

    @return the "data model descriptor"

 **/
  public final Font getSms_dataModelDescriptorFont() throws DbException {
    return (Font)get(fSms_dataModelDescriptorFont);
  }

/**
    Gets the "data model descriptor" of a DbSMSStyle's instance.

    @return the "data model descriptor"

 **/
  public final SMSDisplayDescriptor getSms_dataModelDescriptor() throws DbException {
    return (SMSDisplayDescriptor)get(fSms_dataModelDescriptor);
  }

/**
    Gets the "data model descriptor" of a DbSMSStyle's instance.

    @return the "data model descriptor"

 **/
  public final Color getSms_dataModelDescriptorColor() throws DbException {
    return (Color)get(fSms_dataModelDescriptorColor);
  }

/**
    Gets the "data model border" of a DbSMSStyle's instance.

    @return the "data model border"

 **/
  public final Color getSms_dataModelBorderColor() throws DbException {
    return (Color)get(fSms_dataModelBorderColor);
  }

/**
    Gets the "data model background" of a DbSMSStyle's instance.

    @return the "data model background"

 **/
  public final Color getSms_dataModelBackgroundColor() throws DbException {
    return (Color)get(fSms_dataModelBackgroundColor);
  }

/**
    Gets the "default style?" property's Boolean value of a DbSMSStyle's instance.

    @return the "default style?" property's Boolean value

    @deprecated use isDefaultStyle() method instead
 **/
  public final Boolean getDefaultStyle() throws DbException {
    return (Boolean)get(fDefaultStyle);
  }

/**
    Tells whether a DbSMSStyle's instance is defaultStyle or not.

    @return boolean

 **/
  public final boolean isDefaultStyle() throws DbException {
    return getDefaultStyle().booleanValue();
  }

/**
    Gets the "category" of a DbSMSStyle's instance.

    @return the "category"

 **/
  public final SMSStyleCategory getCategory() throws DbException {
    return (SMSStyleCategory)get(fCategory);
  }

/**
    Gets the "package dash style" of a DbSMSStyle's instance.

    @return the "package dash style"

 **/
  public final Boolean getDashStyleDbSMSPackage() throws DbException {
    return (Boolean)get(fDashStyleDbSMSPackage);
  }

/**
    Gets the "package background" of a DbSMSStyle's instance.

    @return the "package background"

 **/
  public final Color getBackgroundColorDbSMSPackage() throws DbException {
    return (Color)get(fBackgroundColorDbSMSPackage);
  }

/**
    Gets the "package border" of a DbSMSStyle's instance.

    @return the "package border"

 **/
  public final Color getLineColorDbSMSPackage() throws DbException {
    return (Color)get(fLineColorDbSMSPackage);
  }

/**
    Gets the "package highlight" of a DbSMSStyle's instance.

    @return the "package highlight"

 **/
  public final Boolean getHighlightDbSMSPackage() throws DbException {
    return (Boolean)get(fHighlightDbSMSPackage);
  }

/**
    Gets the "package name" of a DbSMSStyle's instance.

    @return the "package name"

 **/
  public final Color getTextColorDbSMSPackage() throws DbException {
    return (Color)get(fTextColorDbSMSPackage);
  }

/**
    Gets the "package name" of a DbSMSStyle's instance.

    @return the "package name"

 **/
  public final Font getSms_packageNameFont() throws DbException {
    return (Font)get(fSms_packageNameFont);
  }

/**
    Gets the "notice background" of a DbSMSStyle's instance.

    @return the "notice background"

 **/
  public final Color getBackgroundColorDbSMSNotice() throws DbException {
    return (Color)get(fBackgroundColorDbSMSNotice);
  }

/**
    Gets the "notice text" of a DbSMSStyle's instance.

    @return the "notice text"

 **/
  public final Color getTextColorDbSMSNotice() throws DbException {
    return (Color)get(fTextColorDbSMSNotice);
  }

/**
    Gets the "notice border" of a DbSMSStyle's instance.

    @return the "notice border"

 **/
  public final Color getLineColorDbSMSNotice() throws DbException {
    return (Color)get(fLineColorDbSMSNotice);
  }

/**
    Gets the "notice dash style" of a DbSMSStyle's instance.

    @return the "notice dash style"

 **/
  public final Boolean getDashStyleDbSMSNotice() throws DbException {
    return (Boolean)get(fDashStyleDbSMSNotice);
  }

/**
    Gets the "notice highlight" of a DbSMSStyle's instance.

    @return the "notice highlight"

 **/
  public final Boolean getHighlightDbSMSNotice() throws DbException {
    return (Boolean)get(fHighlightDbSMSNotice);
  }

/**
    Gets the "graphical link color" of a DbSMSStyle's instance.

    @return the "graphical link color"

 **/
  public final Color getLineColorDbSMSGraphicalLink() throws DbException {
    return (Color)get(fLineColorDbSMSGraphicalLink);
  }

/**
    Gets the "graphical link highlight" of a DbSMSStyle's instance.

    @return the "graphical link highlight"

 **/
  public final Boolean getHighlightDbSMSGraphicalLink() throws DbException {
    return (Boolean)get(fHighlightDbSMSGraphicalLink);
  }

/**
    Gets the "graphical link dash style" of a DbSMSStyle's instance.

    @return the "graphical link dash style"

 **/
  public final Boolean getDashStyleDbSMSGraphicalLink() throws DbException {
    return (Boolean)get(fDashStyleDbSMSGraphicalLink);
  }

/**
    Gets the list of used in notations associated to a DbSMSStyle's instance.

    @return the list of used in notations.

 **/
  public final DbRelationN getUsedInNotations() throws DbException {
    return (DbRelationN)get(fUsedInNotations);
  }

/**
    Gets the warning object associated to a DbSMSStyle's instance.

    @return the warning object

 **/
  public final DbtPrefix getWarningPrefix() throws DbException {
    return (DbtPrefix)get(fWarningPrefix);
  }

/**
    Gets the error object associated to a DbSMSStyle's instance.

    @return the error object

 **/
  public final DbtPrefix getErrorPrefix() throws DbException {
    return (DbtPrefix)get(fErrorPrefix);
  }

/**
    Gets the source object associated to a DbSMSStyle's instance.

    @return the source object

 **/
  public final DbtPrefix getSourcePrefix() throws DbException {
    return (DbtPrefix)get(fSourcePrefix);
  }

/**
    Gets the target object associated to a DbSMSStyle's instance.

    @return the target object

 **/
  public final DbtPrefix getTargetPrefix() throws DbException {
    return (DbtPrefix)get(fTargetPrefix);
  }

}
