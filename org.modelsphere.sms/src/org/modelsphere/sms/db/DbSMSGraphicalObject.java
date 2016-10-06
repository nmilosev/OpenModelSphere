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

import java.awt.Rectangle;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbGraphicalObjectI;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.srtypes.SrRectangle;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelation1;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.sms.international.LocaleMgr;

/**
<b>Direct subclass(es)/subinterface(s) : </b><A HREF="../../../../org/modelsphere/sms/db/DbSMSImageGo.html">DbSMSImageGo</A>, <A HREF="../../../../org/modelsphere/sms/db/DbSMSStampGo.html">DbSMSStampGo</A>, <A HREF="../../../../org/modelsphere/sms/db/DbSMSClassifierGo.html">DbSMSClassifierGo</A>, <A HREF="../../../../org/modelsphere/sms/db/DbSMSLineGo.html">DbSMSLineGo</A>, <A HREF="../../../../org/modelsphere/sms/db/DbSMSUserTextGo.html">DbSMSUserTextGo</A>, <A HREF="../../../../org/modelsphere/sms/db/DbSMSPackageGo.html">DbSMSPackageGo</A>, <A HREF="../../../../org/modelsphere/sms/db/DbSMSFreeGraphicGo.html">DbSMSFreeGraphicGo</A>, <A HREF="../../../../org/modelsphere/sms/db/DbSMSCommonItemGo.html">DbSMSCommonItemGo</A>, <A HREF="../../../../org/modelsphere/sms/db/DbSMSNoticeGo.html">DbSMSNoticeGo</A>.<br>

    <b>Composites : </b>none.<br>
    <b>Components : </b>none.<br>
 **/
public abstract class DbSMSGraphicalObject extends DbObject implements DbGraphicalObjectI {

  //Meta

  public static final MetaField fRectangle
    = new MetaField(LocaleMgr.db.getString("rectangle"));
  public static final MetaField fAutoFit
    = new MetaField(LocaleMgr.db.getString("autoFit"));
  public static final MetaRelation1 fStyle
    = new MetaRelation1(LocaleMgr.db.getString("style"), 0);
  public static final MetaRelationN fFrontEndLineGos
    = new MetaRelationN(LocaleMgr.db.getString("frontEndLineGos"), 0, MetaRelationN.cardN);
  public static final MetaRelationN fBackEndLineGos
    = new MetaRelationN(LocaleMgr.db.getString("backEndLineGos"), 0, MetaRelationN.cardN);

  public static final MetaClass metaClass = new MetaClass(
    LocaleMgr.db.getString("DbSMSGraphicalObject"), DbSMSGraphicalObject.class,
    new MetaField[] {fRectangle,
      fAutoFit,
      fStyle,
      fFrontEndLineGos,
      fBackEndLineGos}, 0);

/**
    For internal use only.
 **/
  public static void initMeta() {
    try {
      metaClass.setSuperMetaClass(DbObject.metaClass);

      fRectangle.setJField(DbSMSGraphicalObject.class.getDeclaredField("m_rectangle"));
        fRectangle.setVisibleInScreen(false);
      fAutoFit.setJField(DbSMSGraphicalObject.class.getDeclaredField("m_autoFit"));
        fAutoFit.setVisibleInScreen(false);
      fStyle.setJField(DbSMSGraphicalObject.class.getDeclaredField("m_style"));
        fStyle.setFlags(MetaField.COPY_REFS);
        fStyle.setVisibleInScreen(false);
      fFrontEndLineGos.setJField(DbSMSGraphicalObject.class.getDeclaredField("m_frontEndLineGos"));
      fBackEndLineGos.setJField(DbSMSGraphicalObject.class.getDeclaredField("m_backEndLineGos"));

      fStyle.setOppositeRel(DbSMSStyle.fGraphicalObjects);

    }
    catch (Exception e) { throw new RuntimeException("Meta init"); }
  }

  static final long serialVersionUID = -8131281879204345984L;
  public static final Rectangle DEFAULT_RECT = new Rectangle(100, 100, 0, 0);
  private transient Object graphicPeer = null;

  //Instance variables
  SrRectangle m_rectangle;
  boolean m_autoFit;
  DbSMSStyle m_style;
  DbRelationN m_frontEndLineGos;
  DbRelationN m_backEndLineGos;

  //Constructors

/**
    Parameter-less constructor.  Required by Java Beans Conventions.
 **/
  public DbSMSGraphicalObject() {}

/**
    Creates an instance of DbSMSGraphicalObject.
    @param composite the object which will contain the newly-created instance
 **/
  public DbSMSGraphicalObject(DbObject composite) throws DbException {
    super(composite);
    setDefaultInitialValues();
  }

  private void setDefaultInitialValues() throws DbException {
    setAutoFit(Boolean.TRUE);
  }

/**

    @return style
 **/
  public final DbSMSStyle findStyle() throws DbException {
   DbSMSStyle style = org.modelsphere.sms.db.util.AnyGo.findStyle(this);
   return style;
  }

/**

    @param style org.modelsphere.sms.db.DbSMSStyle
 **/
  public void setStyle(DbSMSStyle style) throws DbException {
basicSet(fStyle, style);
  }

/**

    @return object
 **/
  public final Object getGraphicPeer() {
return graphicPeer;
  }

/**

    @param graphicpeer java.lang.Object
 **/
  public final void setGraphicPeer(Object graphicPeer) {
this.graphicPeer = graphicPeer;
  }

/**

    @return dbobject
 **/
  public abstract DbObject getSO() throws DbException;

/**

    @param metafield org.modelsphere.jack.baseDb.meta.MetaField
    @return object
 **/
  public Object find(MetaField metaField) throws DbException {
    Object element = org.modelsphere.sms.db.util.DbInitialization.findStyleElement(this, metaField);  
    return element;

  }

/**

    @param metafield java.lang.String
    @return metafield
 **/
  public final MetaField getMetaField(String metaField) throws DbException {
    MetaField[] allMetaFields = getMetaClass().getAllMetaFields();
    MetaField field = null;
    for (int i = 0;  i < allMetaFields.length;  i++) {
      if (metaField.equals(allMetaFields[i].getJName())) {
        field = allMetaFields[i];
        break;
      }
    }
    return field;
  }

/**

    @return boolean
 **/
  public boolean isDeleteCascadeLineGo() throws DbException {
  return true;
  }

/**
    Sets the "rectangle" property of a DbSMSGraphicalObject's instance.

    @param value java.awt.Rectangle
 **/
  public final void setRectangle(Rectangle value) throws DbException {
        Rectangle newValue = org.modelsphere.sms.db.util.DbInitialization.computeRectangle(this,
                value);
      basicSet(fRectangle, newValue);

  }

  //Setters

/**
    Sets the "auto fit" property of a DbSMSGraphicalObject's instance.

    @param value the "auto fit" property

 **/
  public final void setAutoFit(Boolean value) throws DbException {
    basicSet(fAutoFit, value);
  }

/**
    
 **/
  public void set(MetaField metaField, Object value) throws DbException {
    if (metaField.getMetaClass() == metaClass) {
      if (metaField == fRectangle)
        setRectangle((Rectangle)value);
      else if (metaField == fStyle)
        setStyle((DbSMSStyle)value);
      else if (metaField == fFrontEndLineGos)
        ((DbSMSLineGo)value).setFrontEndGo(this);
      else if (metaField == fBackEndLineGos)
        ((DbSMSLineGo)value).setBackEndGo(this);
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
    Gets the "rectangle" of a DbSMSGraphicalObject's instance.

    @return the "rectangle"

 **/
  public final Rectangle getRectangle() throws DbException {
    return (Rectangle)get(fRectangle);
  }

/**
    Gets the "auto fit" property's Boolean value of a DbSMSGraphicalObject's instance.

    @return the "auto fit" property's Boolean value

    @deprecated use isAutoFit() method instead
 **/
  public final Boolean getAutoFit() throws DbException {
    return (Boolean)get(fAutoFit);
  }

/**
    Tells whether a DbSMSGraphicalObject's instance is autoFit or not.

    @return boolean

 **/
  public final boolean isAutoFit() throws DbException {
    return getAutoFit().booleanValue();
  }

/**
    Gets the style object associated to a DbSMSGraphicalObject's instance.

    @return the style object

 **/
  public final DbSMSStyle getStyle() throws DbException {
    return (DbSMSStyle)get(fStyle);
  }

/**
    Gets the list of front end lines graphical objects associated to a DbSMSGraphicalObject's instance.

    @return the list of front end lines graphical objects.

 **/
  public final DbRelationN getFrontEndLineGos() throws DbException {
    return (DbRelationN)get(fFrontEndLineGos);
  }

/**
    Gets the list of back end line graphical objects associated to a DbSMSGraphicalObject's instance.

    @return the list of back end line graphical objects.

 **/
  public final DbRelationN getBackEndLineGos() throws DbException {
    return (DbRelationN)get(fBackEndLineGos);
  }

}
