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

/**
<b>Direct subclass(es)/subinterface(s) : </b><A HREF="../../../../org/modelsphere/sms/or/db/DbORNotation.html">DbORNotation</A>, <A HREF="../../../../org/modelsphere/sms/be/db/DbBENotation.html">DbBENotation</A>.<br>

    <b>Composites : </b><A HREF="../../../../org/modelsphere/sms/db/DbSMSProject.html">DbSMSProject</A>.<br>
    <b>Components : </b>none.<br>
 **/
public abstract class DbSMSNotation extends DbObject {

  //Meta

  public static final MetaField fName
    = new MetaField(LocaleMgr.db.getString("name"));
  public static final MetaRelation1 fDefaultStyle
    = new MetaRelation1(LocaleMgr.db.getString("defaultStyle"), 0);
  public static final MetaField fNotationID
    = new MetaField(LocaleMgr.db.getString("notationID"));
  public static final MetaField fNotationMode
    = new MetaField(LocaleMgr.db.getString("notationMode"));
  public static final MetaField fMasterNotationID
    = new MetaField(LocaleMgr.db.getString("masterNotationID"));

  public static final MetaClass metaClass = new MetaClass(
    LocaleMgr.db.getString("DbSMSNotation"), DbSMSNotation.class,
    new MetaField[] {fName,
      fDefaultStyle,
      fNotationID,
      fNotationMode,
      fMasterNotationID}, 0);

/**
    For internal use only.
 **/
  public static void initMeta() {
    try {
      metaClass.setSuperMetaClass(DbObject.metaClass);
      metaClass.setIcon("dbsmsnotation.gif");

      fName.setJField(DbSMSNotation.class.getDeclaredField("m_name"));
      fDefaultStyle.setJField(DbSMSNotation.class.getDeclaredField("m_defaultStyle"));
      fNotationID.setJField(DbSMSNotation.class.getDeclaredField("m_notationID"));
      fNotationMode.setJField(DbSMSNotation.class.getDeclaredField("m_notationMode"));
      fMasterNotationID.setJField(DbSMSNotation.class.getDeclaredField("m_masterNotationID"));
        fMasterNotationID.setVisibleInScreen(false);
        fMasterNotationID.setEditable(false);

      fDefaultStyle.setOppositeRel(DbSMSStyle.fUsedInNotations);

    }
    catch (Exception e) { throw new RuntimeException("Meta init"); }
  }

  static final long serialVersionUID = 0;
  public static final int ER_MODE = 1;
  public static final int OR_MODE = 2;
  public static final int BE_MODE = 3;

  //Instance variables
  String m_name;
  DbSMSStyle m_defaultStyle;
  int m_notationID;
  int m_notationMode;
  int m_masterNotationID;

  //Constructors

/**
    Parameter-less constructor.  Required by Java Beans Conventions.
 **/
  public DbSMSNotation() {}

/**
    Creates an instance of DbSMSNotation.
    @param composite the object which will contain the newly-created instance
 **/
  public DbSMSNotation(DbObject composite) throws DbException {
    super(composite);
    setDefaultInitialValues();
  }

  private void setDefaultInitialValues() throws DbException {
  }

/**

    @param dbo org.modelsphere.jack.baseDb.db.DbObject
    @return boolean
 **/
  public boolean matches(DbObject dbo) throws DbException {
    return DbObject.valuesAreEqual(getName(), dbo.getName());
  }

  //Setters

/**
    Sets the "name" property of a DbSMSNotation's instance.

    @param value the "name" property

 **/
  public final void setName(String value) throws DbException {
    basicSet(fName, value);
  }

/**
    Sets the default style object associated to a DbSMSNotation's instance.

    @param value the default style object to be associated

 **/
  public final void setDefaultStyle(DbSMSStyle value) throws DbException {
    basicSet(fDefaultStyle, value);
  }

/**
    Sets the "notation id" property of a DbSMSNotation's instance.

    @param value the "notation id" property

 **/
  public final void setNotationID(Integer value) throws DbException {
    basicSet(fNotationID, value);
  }

/**
    Sets the "notation mode" property of a DbSMSNotation's instance.

    @param value the "notation mode" property

 **/
  public final void setNotationMode(Integer value) throws DbException {
    basicSet(fNotationMode, value);
  }

/**
    Sets the "master notation id" property of a DbSMSNotation's instance.

    @param value the "master notation id" property

 **/
  public final void setMasterNotationID(Integer value) throws DbException {
    basicSet(fMasterNotationID, value);
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
    Gets the "name" property of a DbSMSNotation's instance.

    @return the "name" property

 **/
  public final String getName() throws DbException {
    return (String)get(fName);
  }

/**
    Gets the default style object associated to a DbSMSNotation's instance.

    @return the default style object

 **/
  public final DbSMSStyle getDefaultStyle() throws DbException {
    return (DbSMSStyle)get(fDefaultStyle);
  }

/**
    Gets the "notation id" property of a DbSMSNotation's instance.

    @return the "notation id" property

 **/
  public final Integer getNotationID() throws DbException {
    return (Integer)get(fNotationID);
  }

/**
    Gets the "notation mode" property of a DbSMSNotation's instance.

    @return the "notation mode" property

 **/
  public final Integer getNotationMode() throws DbException {
    return (Integer)get(fNotationMode);
  }

/**
    Gets the "master notation id" property of a DbSMSNotation's instance.

    @return the "master notation id" property

 **/
  public final Integer getMasterNotationID() throws DbException {
    return (Integer)get(fMasterNotationID);
  }

}
