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

package org.modelsphere.jack.srtool.reverse.engine;

import java.util.*;
import java.lang.reflect.*;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.debug.*;

public final class DbElement {
    // Fields used to access the DB Value or to set the DB Value
    private MetaRelation1[] path; // Path to the metafield to set.
    // The path doesn't include the metafield to set. Only the path to follow
    // for reaching the object
    // containing the specified metaField.
    MetaField metaField; // Metafield to set.

    public DbElement(MetaRelation1[] path, MetaField metafield) {
        this.path = path;
        this.metaField = metafield;
    }

    // see fields for comments
    public DbElement(MetaField metafield) {
        this(null, metafield);
    }

    // Follow the path starting with the DbObject 'start' parameter.
    private DbObject navigateToPath(DbObject start) throws DbException {
        if (path == null || path.length == 0 || start == null)
            return start;
        DbObject end = start;
        int pathlength = path.length;
        for (int i = 0; i < pathlength; i++) {
            if (!end.hasField(path[i])) {
                end = null;
                Debug.trace("Cannot reach MetaField: <"
                        + (metaField == null ? "" : metaField.getJName())
                        + ">.  Invalid path MetaField: <" + path[i].getJName() + ">.");
                break;
            }
            Object temp = end.get(path[i]);
            if ((temp == null) || !(temp instanceof DbObject)) {
                end = null;
                Debug.trace("Invalid MetaField in path: <" + path[i].getJName()
                        + "> : Expecting non null DbObject instance. ");
                break;
            }
            end = (DbObject) temp;
        }
        return end;
    }

    // Return the value of metaField for the currentObject
    public final Object getValue(Object hookContainer, HashMap currentObject) throws DbException {
        DbObject pathobject = (DbObject) currentObject.get(ReverseBuilder.CURRENT_OBJECT);
        pathobject = navigateToPath(pathobject);
        if ((pathobject != null) && pathobject.hasField(metaField)) {
            return pathobject.get(metaField);
        }
        return null;
    }

    // Return the value of metaField for the dbObject
    public final Object getValue(Object hookContainer, DbObject dbObject) throws DbException {
        DbObject pathobject = dbObject;
        pathobject = navigateToPath(pathobject);
        if ((dbObject != null) && dbObject.hasField(metaField)) {
            return dbObject.get(metaField);
        }
        return null;
    }

    public final void setValue(Object hookContainer, HashMap currentObject, Object value)
            throws DbException {
        if (metaField != null) {
            DbObject pathobject = (DbObject) currentObject.get(ReverseBuilder.CURRENT_OBJECT);
            pathobject = navigateToPath(pathobject);
            if ((pathobject != null) && pathobject.hasField(metaField)) {
                if (metaField instanceof MetaRelationN) {
                    if (value != null)
                        pathobject.set(metaField, value);
                } else {
                    if (Debug.isDebug()) {
                        Field field = metaField.getJField();
                        Class c = field.getType();
                        if (value == null) {
                            if ((c != null) && c.isPrimitive()) {
                                Debug.trace("Expecting Bad Meta for metafield: "
                                        + (metaField.getGUIName() == null ? metaField.getJField()
                                                .getName() : metaField.getGUIName())
                                        + ".  Value will be set to null on a primitive type. "); // NOT
                                // LOCALIZABLE
                                // -
                                // Debug
                            } else if (c == null) {
                                Debug.trace("Unable to identify type for field: "
                                        + metaField.getJField().getName() + ".  Field Type: " + c);
                            }
                            // Else it will be a set null on a null possible
                            // field (not primitive)
                        } else {
                            if ((c != null) && c.isPrimitive()) {
                                Class primclass = getEquivalentPrimitiveClass(value.getClass());
                                if (primclass != null && primclass != c)
                                    Debug
                                            .trace("Expecting Bad Meta for metafield: "
                                                    + (metaField.getGUIName() == null ? metaField
                                                            .getJField().getName() : metaField
                                                            .getGUIName()) + ".  Value class: "
                                                    + value.getClass()); // NOT
                                // LOCALIZABLE
                                // -
                                // Debug
                                else if (primclass == null && value.getClass().isPrimitive()) {
                                    Debug
                                            .trace("Unable to verify type compatibility for metafield:  "
                                                    + metaField.getJField().getName()
                                                    + ".  Field Type: "
                                                    + c
                                                    + ".  No primitive match in getEquivalentPrimitiveClass() for class: "
                                                    + value.getClass().getName());
                                } else if (primclass == null) {
                                    Debug
                                            .trace("Expecting Bad Meta for metafield: "
                                                    + (metaField.getGUIName() == null ? metaField
                                                            .getJField().getName() : metaField
                                                            .getGUIName()) + ".  Value class: "
                                                    + value.getClass()); // NOT
                                    // LOCALIZABLE
                                    // -
                                    // Debug
                                }
                            } else if ((c != null)
                                    && (!c.isAssignableFrom(getEquivalentClass(value.getClass())))) {
                                Debug.trace("Expecting Bad Meta for metafield: "
                                        + (metaField.getGUIName() == null ? metaField.getJField()
                                                .getName() : metaField.getGUIName())
                                        + ".  Value class: " + value.getClass()); // NOT
                                // LOCALIZABLE
                                // -
                                // Debug
                            } else if (c == null) {
                                Debug.trace("Unable to identify type for field: "
                                        + metaField.getJField().getName() + ".  Field Type: " + c);
                            }
                        }
                    }
                    pathobject.set(metaField, value);
                }
            }
        }
    }

    // For debug purpose only
    private Class getEquivalentClass(Class c) {
        if (c == Boolean.class)
            return SrBoolean.class;
        if (c == Double.class)
            return SrDouble.class;
        if (c == Integer.class)
            return SrInteger.class;
        if (c == Long.class)
            return SrLong.class;
        return c;
    }

    // For debug purpose only
    private Class getEquivalentPrimitiveClass(Class c) {
        if (c == Integer.class)
            return Integer.TYPE;
        if (c == Boolean.class)
            return Boolean.TYPE;
        if (c == Double.class)
            return Double.TYPE;
        if (c == Long.class)
            return Long.TYPE;
        return null;
    }

}
