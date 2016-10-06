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

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.meta.*;

public final class SrxDbObjectElement implements SrxElement {
    // This option specify that if an object defined in the srx
    // (SrxDbObjectElement) is not found, it must be created
    // The default value for this option is disabled
    // Be careful using this option. MetaClasses must be instanciable.
    public static final int CREATE_IF_NOT_FOUND = 0x01;

    private DbObject dbo0; // The first object to used for searching the
    // matching dbo
    // Usually dataModel, database, ... (root object)
    private MetaClass dbo1MetaClass; // The MetaClass of the component dbo1 (in
    // composite dbo0)
    private String dbo1ValueTag; // The Tag naming dbo1.
    private Constraint[] dbo1Constraints; // The Constraints to apply when
    // searching dbo1 within dbo0
    private MetaClass dbo2MetaClass; // The MetaClass of the component dbo2 (in
    // composite dbo1)
    private String dbo2ValueTag; // The Tag naming dbo2
    private Constraint[] dbo2Constraints; // The Constraints to apply when
    // searching dbo2 within dbo1
    private boolean useComponentsTree = false;
    private int options = 0;

    // If dbo1 parameters are null, dbo0 will be consider as the dbo for setting
    // the metafield value.
    // If dbo2 parameters are null and dbo1 parameters are not null, dbo1 will
    // be consider as the dbo for setting the metafield value.
    // If dbo1 and dbo2 parameters are not null, dbo2 will be consider as the
    // dbo for setting the metafield value.

    // If useComponentsTree == true, the find methods will search for the
    // dbobject in all the components tree (dbobject.componentTree(MetaClass)).
    // You can set this value to true to skip a level of search and when you
    // know that on the target system, the composition allow this.
    // For example: In Oracle, an index name is unique for the database. The SMS
    // composition (Datamodel .. table .. index) is different.
    // In this case, you can set useComponentsTree to true and start the search
    // (dbo0) on the datamodel and define dbo1** fields
    // for the index, skipping the table. You can be sure that only one index
    // will be found with that name since it is not allowed
    // to duplicate the index name in Oracle.

    public SrxDbObjectElement(DbObject dbo0, MetaClass dbo1MetaClass, String dbo1ValueTag,
            Constraint[] dbo1Constraints, MetaClass dbo2MetaClass, String dbo2ValueTag,
            Constraint[] dbo2Constraints, boolean useComponentsTree, int options) {
        this.dbo0 = dbo0;
        this.dbo1MetaClass = dbo1MetaClass;
        this.dbo1ValueTag = dbo1ValueTag;
        this.dbo1Constraints = dbo1Constraints;
        this.dbo2MetaClass = dbo2MetaClass;
        this.dbo2ValueTag = dbo2ValueTag;
        this.dbo2Constraints = dbo2Constraints;
        this.useComponentsTree = useComponentsTree;
        this.options = options;
    }

    public SrxDbObjectElement(DbObject dbo0, MetaClass dbo1MetaClass, String dbo1ValueTag,
            Constraint[] dbo1Constraints, MetaClass dbo2MetaClass, String dbo2ValueTag,
            Constraint[] dbo2Constraints, boolean useComponentsTree) {
        this(dbo0, dbo1MetaClass, dbo1ValueTag, dbo1Constraints, dbo2MetaClass, dbo2ValueTag,
                dbo2Constraints, useComponentsTree, 0);
    }

    public SrxDbObjectElement(DbObject dbo0, MetaClass dbo1MetaClass, String dbo1ValueTag,
            Constraint[] dbo1Constraints, MetaClass dbo2MetaClass, String dbo2ValueTag,
            Constraint[] dbo2Constraints) {
        this(dbo0, dbo1MetaClass, dbo1ValueTag, dbo1Constraints, dbo2MetaClass, dbo2ValueTag,
                dbo2Constraints, false, 0);
    }

    public SrxDbObjectElement(DbObject dbo0, MetaClass dbo1MetaClass, String dbo1ValueTag,
            Constraint[] dbo1Constraints, MetaClass dbo2MetaClass, String dbo2ValueTag,
            Constraint[] dbo2Constraints, int options) {
        this(dbo0, dbo1MetaClass, dbo1ValueTag, dbo1Constraints, dbo2MetaClass, dbo2ValueTag,
                dbo2Constraints, false, options);
    }

    // see fields for comments
    public SrxDbObjectElement(DbObject dbo0) {
        this(dbo0, null, null, null, null, null, null, false, 0);
    }

    // see fields for comments
    public SrxDbObjectElement(DbObject dbo0, int options) {
        this(dbo0, null, null, null, null, null, null, false, options);
    }

    // see fields for comments
    public SrxDbObjectElement(DbObject dbo0, MetaClass dbo1MetaClass, String dbo1ValueTag,
            Constraint[] dbo1Constraints) {
        this(dbo0, dbo1MetaClass, dbo1ValueTag, dbo1Constraints, null, null, null, false, 0);
    }

    // see fields for comments
    public SrxDbObjectElement(DbObject dbo0, MetaClass dbo1MetaClass, String dbo1ValueTag,
            Constraint[] dbo1Constraints, int options) {
        this(dbo0, dbo1MetaClass, dbo1ValueTag, dbo1Constraints, null, null, null, false, options);
    }

    // see fields for comments
    public SrxDbObjectElement(DbObject dbo0, MetaClass dbo1MetaClass, String dbo1ValueTag,
            Constraint[] dbo1Constraints, boolean useComponentTree) {
        this(dbo0, dbo1MetaClass, dbo1ValueTag, dbo1Constraints, null, null, null,
                useComponentTree, 0);
    }

    // see fields for comments
    public SrxDbObjectElement(DbObject dbo0, MetaClass dbo1MetaClass, String dbo1ValueTag,
            Constraint[] dbo1Constraints, boolean useComponentTree, int options) {
        this(dbo0, dbo1MetaClass, dbo1ValueTag, dbo1Constraints, null, null, null,
                useComponentTree, options);
    }

    public Object getValue(Object hookContainer, HashMap currentObject) throws Exception {
        boolean create = (options & CREATE_IF_NOT_FOUND) != 0;
        if (dbo0 == null)
            return null;

        if (dbo1MetaClass == null)
            return dbo0;

        // Search level 1 - Search dbo1 in composite dbo0
        DbObject dbo1 = null;
        String dbo1Name = (String) currentObject.get(dbo1ValueTag);
        if (dbo1Name == null)
            return create ? dbo0.createComponent(dbo1MetaClass) : null;
        dbo1 = findComponentByName(hookContainer, currentObject, dbo0, dbo1MetaClass, dbo1Name,
                dbo1Constraints, useComponentsTree);
        if (dbo1 == null) {
            DbObject result = null;
            if (!create)
                result = null;
            else {
                result = dbo0.createComponent(dbo1MetaClass);
                if (result != null)
                    result.setName(dbo1Name);
            }
            return result;
        }

        if (dbo2MetaClass == null)
            return dbo1;

        // Search level 2 - Search dbo2 in composite dbo1
        String dbo2Name = (String) currentObject.get(dbo2ValueTag);
        if (dbo2Name == null)
            return create ? dbo1.createComponent(dbo2MetaClass) : null;
        DbObject dbo2 = findComponentByName(hookContainer, currentObject, dbo1, dbo2MetaClass,
                dbo2Name, dbo2Constraints, useComponentsTree);
        if (dbo2 == null) {
            DbObject result = null;
            if (!create)
                result = null;
            else {
                result = dbo1.createComponent(dbo2MetaClass);
                if (result != null)
                    result.setName(dbo2Name);
            }
            return result;
        }

        return dbo2;
    }

    // Find a component of type <componentMetaClass> in the specified
    // <composite> dbobject.
    // The component must have a name equals to <name> and respects the
    // specified <constraints>.
    public static final DbObject findComponentByName(Object hookContainer, HashMap currentObject,
            DbObject composite, MetaClass componentMetaClass, String name, Constraint[] constraints)
            throws Exception {
        return findComponentByName(hookContainer, currentObject, composite, componentMetaClass,
                name, constraints, false);
    }

    // Find a component of type <componentMetaClass> in the specified
    // <composite> dbobject.
    // The component must have a name equals to <name> and respects the
    // specified <constraints>.
    // useComponentsTree value of true will search in all the component tree of
    // the composite object (not only direct components of the composite)
    public static final DbObject findComponentByName(Object hookContainer, HashMap currentObject,
            DbObject composite, MetaClass componentMetaClass, String name,
            Constraint[] constraints, boolean useComponentsTree) throws Exception {
        if (composite == null)
            return null;
        DbObject childFound = null;
        DbEnumeration dbEnum = useComponentsTree ? composite.componentTree(componentMetaClass)
                : composite.getComponents().elements(componentMetaClass);
        while (dbEnum.hasMoreElements()) {
            DbObject child = dbEnum.nextElement();
            if (name != null) {
                if (name.equals(child.getSemanticalName(DbObject.SHORT_FORM))) {
                    // verify constraints
                    if (constraints == null) {
                        childFound = child;
                        break;
                    }
                    boolean constraintok = true;
                    for (int i = 0; (i < constraints.length) && constraintok; i++) {
                        Constraint constraint = constraints[i];
                        if (constraint != null)
                            constraintok = constraint.dBEqualsSRX(hookContainer, currentObject,
                                    child);
                    }
                    if (constraintok) {
                        childFound = child;
                        break;
                    }
                }
            }
        }
        dbEnum.close();
        return childFound;
    }

    public String getDebugInfo(HashMap currentObject) throws Exception {
        String s = "[SrxDbObjectElement] "; // NOT LOCALIZABLE
        if (dbo0 != null) {
            s += "\n dbo0 = " + dbo0.getName(); // NOT LOCALIZABLE
        }
        if (dbo1ValueTag != null) {
            s += "\n dbo1Tag =" + dbo1ValueTag; // NOT LOCALIZABLE
            s += "\n dbo1MetaClass =" + dbo1MetaClass.getGUIName(); // NOT
            // LOCALIZABLE
            s += "\n dbo1TagValue =" + currentObject.get(dbo1ValueTag); // NOT
            // LOCALIZABLE
            if (dbo1Constraints != null) { // NOT LOCALIZABLE
                s += "\n dbo1Constraints = {";
                for (int i = 0; i < dbo1Constraints.length; i++) {
                    if (dbo1Constraints[i] != null)
                        s += dbo1Constraints[i].getDebugInfo(currentObject);
                }
                s += "}";
            }
        }
        if (dbo2ValueTag != null) {
            s += "\n dbo2Tag =" + dbo2ValueTag; // NOT LOCALIZABLE
            s += "\n dbo2MetaClass =" + dbo2MetaClass.getGUIName(); // NOT
            // LOCALIZABLE
            s += "\n dbo2TagValue =" + currentObject.get(dbo2ValueTag); // NOT
            // LOCALIZABLE
            if (dbo2Constraints != null) {
                s += "\n dbo2Constraints = {"; // NOT LOCALIZABLE
                for (int i = 0; i < dbo2Constraints.length; i++) {
                    if (dbo2Constraints[i] != null)
                        s += dbo2Constraints[i].getDebugInfo(currentObject);
                }
                s += "}"; // NOT LOCALIZABLE
            }
        }
        return s;
    }
}
