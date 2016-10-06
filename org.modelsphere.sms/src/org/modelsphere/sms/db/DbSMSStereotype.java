/*************************************************************************

Copyright (C) 2009 Grandite

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
package org.modelsphere.sms.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.or.db.DbORCommonItem;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSUmlExtensibility.html">DbSMSUmlExtensibility</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public final class DbSMSStereotype extends DbSemanticalObject {

    //Meta

    public static final MetaRelationN fStereotypedObjects = new MetaRelationN(LocaleMgr.db
            .getString("stereotypedObjects"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fSubStereotypes = new MetaRelationN(LocaleMgr.db
            .getString("subStereotypes"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fSuperStereotypes = new MetaRelationN(LocaleMgr.db
            .getString("superStereotypes"), 0, MetaRelationN.cardN);
    public static final MetaField fIcon = new MetaField(LocaleMgr.db.getString("icon"));
    public static final MetaField fBuiltIn = new MetaField(LocaleMgr.db.getString("builtIn"));
    public static final MetaField fMetaClassName = new MetaField(LocaleMgr.db
            .getString("DbSMSStereotype.metaClassName"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbSMSStereotype"), DbSMSStereotype.class, new MetaField[] {
            fStereotypedObjects, fSubStereotypes, fSuperStereotypes, fIcon, fBuiltIn,
            fMetaClassName }, MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSemanticalObject.metaClass);
            metaClass.setIcon("dbsmsumlstereotype.gif");

            fStereotypedObjects.setJField(DbSMSStereotype.class
                    .getDeclaredField("m_stereotypedObjects"));
            fSubStereotypes.setJField(DbSMSStereotype.class.getDeclaredField("m_subStereotypes"));
            fSuperStereotypes.setJField(DbSMSStereotype.class
                    .getDeclaredField("m_superStereotypes"));
            fIcon.setJField(DbSMSStereotype.class.getDeclaredField("m_icon"));
            fIcon.setRendererPluginName("SrImage;StereotypeIcon");
            fBuiltIn.setJField(DbSMSStereotype.class.getDeclaredField("m_builtIn"));
            fBuiltIn.setEditable(false);
            fMetaClassName.setJField(DbSMSStereotype.class.getDeclaredField("m_metaClassName"));
            fMetaClassName.setRendererPluginName("SMSSemanticalMetaClass");

            fSubStereotypes.setOppositeRel(DbSMSStereotype.fSuperStereotypes);
            fSuperStereotypes.setOppositeRel(DbSMSStereotype.fSubStereotypes);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;
    private transient MetaClass stereotypeMetaClass;

    //Instance variables
    DbRelationN m_stereotypedObjects;
    DbRelationN m_subStereotypes;
    DbRelationN m_superStereotypes;
    SrImage m_icon;
    boolean m_builtIn;
    String m_metaClassName;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSStereotype() {
    }

    /**
     * Creates an instance of DbSMSStereotype.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbSMSStereotype(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setBuiltIn(Boolean.FALSE);
        setName(LocaleMgr.misc.getString("umlStereotype"));
    }

    /**
     * @return boolean
     **/
    public final boolean isDeletable() throws DbException {
        return !isBuiltIn();

    }

    /**
     * @return metaclass
     **/
    public MetaClass getStereotypeMetaClass() throws DbException {
        if (stereotypeMetaClass == null) {
            stereotypeMetaClass = MetaClass.find(getMetaClassName());
            if (stereotypeMetaClass == null)
                return DbSMSSemanticalObject.metaClass;
        }
        return stereotypeMetaClass;

    }

    //Setters

    /**
     * Adds an element to or removes an element from the list of substereotypes associated to a
     * DbSMSStereotype's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setSubStereotypes(DbSMSStereotype value, int op) throws DbException {
        setRelationNN(fSubStereotypes, value, op);
    }

    /**
     * Adds an element to the list of substereotypes associated to a DbSMSStereotype's instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToSubStereotypes(DbSMSStereotype value) throws DbException {
        setSubStereotypes(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of substereotypes associated to a DbSMSStereotype's
     * instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromSubStereotypes(DbSMSStereotype value) throws DbException {
        setSubStereotypes(value, Db.REMOVE_FROM_RELN);
    }

    /**
     * Adds an element to or removes an element from the list of superstereotypes associated to a
     * DbSMSStereotype's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setSuperStereotypes(DbSMSStereotype value, int op) throws DbException {
        setRelationNN(fSuperStereotypes, value, op);
    }

    /**
     * Adds an element to the list of superstereotypes associated to a DbSMSStereotype's instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToSuperStereotypes(DbSMSStereotype value) throws DbException {
        setSuperStereotypes(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of superstereotypes associated to a DbSMSStereotype's
     * instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromSuperStereotypes(DbSMSStereotype value) throws DbException {
        setSuperStereotypes(value, Db.REMOVE_FROM_RELN);
    }

    /**
     * Sets the "icon" property of a DbSMSStereotype's instance.
     * 
     * @param value
     *            the "icon" property
     **/
    public final void setIcon(Image value) throws DbException {
        basicSet(fIcon, value);
    }

    /**
     * Sets the "built in?" property of a DbSMSStereotype's instance.
     * 
     * @param value
     *            the "built in?" property
     **/
    public final void setBuiltIn(Boolean value) throws DbException {
        basicSet(fBuiltIn, value);
    }

    /**
     * Sets the "applicable on" property of a DbSMSStereotype's instance.
     * 
     * @param value
     *            the "applicable on" property
     **/
    public final void setMetaClassName(String value) throws DbException {
        basicSet(fMetaClassName, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fStereotypedObjects)
                ((DbSMSSemanticalObject) value).setUmlStereotype(this);
            else if (metaField == fSubStereotypes)
                setSubStereotypes((DbSMSStereotype) value, Db.ADD_TO_RELN);
            else if (metaField == fSuperStereotypes)
                setSuperStereotypes((DbSMSStereotype) value, Db.ADD_TO_RELN);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        if (relation == fSubStereotypes)
            setSubStereotypes((DbSMSStereotype) neighbor, op);
        else if (relation == fSuperStereotypes)
            setSuperStereotypes((DbSMSStereotype) neighbor, op);
        else
            super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the list of stereotyped objects associated to a DbSMSStereotype's instance.
     * 
     * @return the list of stereotyped objects.
     **/
    public final DbRelationN getStereotypedObjects() throws DbException {
        return (DbRelationN) get(fStereotypedObjects);
    }

    /**
     * Gets the list of substereotypes associated to a DbSMSStereotype's instance.
     * 
     * @return the list of substereotypes.
     **/
    public final DbRelationN getSubStereotypes() throws DbException {
        return (DbRelationN) get(fSubStereotypes);
    }

    /**
     * Gets the list of superstereotypes associated to a DbSMSStereotype's instance.
     * 
     * @return the list of superstereotypes.
     **/
    public final DbRelationN getSuperStereotypes() throws DbException {
        return (DbRelationN) get(fSuperStereotypes);
    }

    /**
     * Gets the "icon" of a DbSMSStereotype's instance.
     * 
     * @return the "icon"
     **/
    public final Image getIcon() throws DbException {
        return (Image) get(fIcon);
    }

    /**
     * Gets the "built in?" property's Boolean value of a DbSMSStereotype's instance.
     * 
     * @return the "built in?" property's Boolean value
     * @deprecated use isBuiltIn() method instead
     **/
    public final Boolean getBuiltIn() throws DbException {
        return (Boolean) get(fBuiltIn);
    }

    /**
     * Tells whether a DbSMSStereotype's instance is builtIn or not.
     * 
     * @return boolean
     **/
    public final boolean isBuiltIn() throws DbException {
        return getBuiltIn().booleanValue();
    }

    /**
     * Gets the "applicable on" property of a DbSMSStereotype's instance.
     * 
     * @return the "applicable on" property
     **/
    public final String getMetaClassName() throws DbException {
        return (String) get(fMetaClassName);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
