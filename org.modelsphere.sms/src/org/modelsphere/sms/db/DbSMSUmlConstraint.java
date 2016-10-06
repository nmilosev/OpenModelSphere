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

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSUmlExtensibility.html">DbSMSUmlExtensibility</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public final class DbSMSUmlConstraint extends DbSemanticalObject {

    //Meta

    public static final MetaRelationN fConstrainedObjects = new MetaRelationN(LocaleMgr.db
            .getString("constrainedObjects"), 0, MetaRelationN.cardN);
    public static final MetaField fBuiltIn = new MetaField(LocaleMgr.db.getString("builtIn"));
    public static final MetaField fExpression = new MetaField(LocaleMgr.db.getString("expression"));
    public static final MetaField fMetaClassName = new MetaField(LocaleMgr.db
            .getString("DbSMSUmlConstraint.metaClassName"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbSMSUmlConstraint"), DbSMSUmlConstraint.class, new MetaField[] {
            fConstrainedObjects, fBuiltIn, fExpression, fMetaClassName }, MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSemanticalObject.metaClass);
            metaClass.setIcon("dbsmsumlconstraint.gif");

            fConstrainedObjects.setJField(DbSMSUmlConstraint.class
                    .getDeclaredField("m_constrainedObjects"));
            fBuiltIn.setJField(DbSMSUmlConstraint.class.getDeclaredField("m_builtIn"));
            fBuiltIn.setEditable(false);
            fExpression.setJField(DbSMSUmlConstraint.class.getDeclaredField("m_expression"));
            fMetaClassName.setJField(DbSMSUmlConstraint.class.getDeclaredField("m_metaClassName"));
            fMetaClassName.setRendererPluginName("SMSSemanticalMetaClass");

            fConstrainedObjects.setOppositeRel(DbSMSSemanticalObject.fUmlConstraints);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;
    private transient MetaClass umlConstraintMetaClass;

    //Instance variables
    DbRelationN m_constrainedObjects;
    boolean m_builtIn;
    String m_expression;
    String m_metaClassName;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSUmlConstraint() {
    }

    /**
     * Creates an instance of DbSMSUmlConstraint.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbSMSUmlConstraint(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setBuiltIn(Boolean.FALSE);
        setName(LocaleMgr.misc.getString("umlConstraint"));
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
    public MetaClass getUmlConstraintMetaClass() throws DbException {
        if (umlConstraintMetaClass == null) {
            umlConstraintMetaClass = MetaClass.find(getMetaClassName());
            if (umlConstraintMetaClass == null)
                return DbSMSSemanticalObject.metaClass;
        }
        return umlConstraintMetaClass;

    }

    //Setters

    /**
     * Adds an element to or removes an element from the list of constrained objects associated to a
     * DbSMSUmlConstraint's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setConstrainedObjects(DbSMSSemanticalObject value, int op) throws DbException {
        setRelationNN(fConstrainedObjects, value, op);
    }

    /**
     * Adds an element to the list of constrained objects associated to a DbSMSUmlConstraint's
     * instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToConstrainedObjects(DbSMSSemanticalObject value) throws DbException {
        setConstrainedObjects(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of constrained objects associated to a DbSMSUmlConstraint's
     * instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromConstrainedObjects(DbSMSSemanticalObject value) throws DbException {
        setConstrainedObjects(value, Db.REMOVE_FROM_RELN);
    }

    /**
     * Sets the "built in?" property of a DbSMSUmlConstraint's instance.
     * 
     * @param value
     *            the "built in?" property
     **/
    public final void setBuiltIn(Boolean value) throws DbException {
        basicSet(fBuiltIn, value);
    }

    /**
     * Sets the "expression" property of a DbSMSUmlConstraint's instance.
     * 
     * @param value
     *            the "expression" property
     **/
    public final void setExpression(String value) throws DbException {
        basicSet(fExpression, value);
    }

    /**
     * Sets the "applicable on" property of a DbSMSUmlConstraint's instance.
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
            if (metaField == fConstrainedObjects)
                setConstrainedObjects((DbSMSSemanticalObject) value, Db.ADD_TO_RELN);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        if (relation == fConstrainedObjects)
            setConstrainedObjects((DbSMSSemanticalObject) neighbor, op);
        else
            super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the list of constrained objects associated to a DbSMSUmlConstraint's instance.
     * 
     * @return the list of constrained objects.
     **/
    public final DbRelationN getConstrainedObjects() throws DbException {
        return (DbRelationN) get(fConstrainedObjects);
    }

    /**
     * Gets the "built in?" property's Boolean value of a DbSMSUmlConstraint's instance.
     * 
     * @return the "built in?" property's Boolean value
     * @deprecated use isBuiltIn() method instead
     **/
    public final Boolean getBuiltIn() throws DbException {
        return (Boolean) get(fBuiltIn);
    }

    /**
     * Tells whether a DbSMSUmlConstraint's instance is builtIn or not.
     * 
     * @return boolean
     **/
    public final boolean isBuiltIn() throws DbException {
        return getBuiltIn().booleanValue();
    }

    /**
     * Gets the "expression" property of a DbSMSUmlConstraint's instance.
     * 
     * @return the "expression" property
     **/
    public final String getExpression() throws DbException {
        return (String) get(fExpression);
    }

    /**
     * Gets the "applicable on" property of a DbSMSUmlConstraint's instance.
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
