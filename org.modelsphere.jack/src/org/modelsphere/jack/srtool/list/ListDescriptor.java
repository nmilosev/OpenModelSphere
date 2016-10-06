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

package org.modelsphere.jack.srtool.list;

import javax.swing.Icon;

import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.SemanticalModel;

public final class ListDescriptor {
    MetaClass neighborsMetaClass;

    // The default metafields to display in the list (user configuration will override these values but the available fields list will be these fields.
    // If not specified (null), getScreenMetaFields of the componentsmetaclass
    // Must be instanceof MetaField, DbPath or ListColumn
    Object[] defaultColumns;

    // The default association used for populating the list is the composition tree.
    MetaRelationship association = DbObject.fComponents;
    // The boundsMetaClasses specified boundaries for the composition tree (Does not apply if association != DbObject.fComponents)
    MetaClass[] boundsMetaClasses;
    // Include a column for object order within the association (Usefull only for  association = DbObject.fComponents)
    // NOTE:  This feature will be activated if needed (ListTableModel is ready, just uncomment the Constructors in this class)
    boolean includeSequences = false;

    // If compositeVisible == false, default behavior will apply (ie. if neighborsMetaClass not a direct sub component class of root object metaclass, this value will
    // be display, otherwise, it will not be displayed)
    boolean compositeVisible = false;

    // If compositeVisible == true, this name will be displayed for the composite column.  If not specified and compositeVisible == true,
    // The List will try to deduce a common name for listed components composite or will display 'composite'
    String compositeName = null;

    Boolean bHasRelationship = null;

    // if Null, the metaclass prural GUI name will be used by action gui components, same for icon
    private String displayText = null;
    private Icon icon = null;

    // Add a column to display the object metaclass GUI name
    boolean includeObjectType = false;

    private ListDescriptor(MetaRelationship association, MetaClass neighborsMetaClass,
            MetaClass[] boundsMetaClasses, Object[] defaultColumns, boolean includeSequences) {
        this.neighborsMetaClass = neighborsMetaClass;
        this.boundsMetaClasses = boundsMetaClasses;
        this.includeSequences = includeSequences;
        this.defaultColumns = defaultColumns;
        if (association != null)
            this.association = association;
    }

    /*
     * public ListDescriptor(MetaClass componentsMetaClass, boolean includeSequences){ this(null,
     * componentsMetaClass, null, null, includeSequences); }
     */

    public Boolean HasRelationship() {
        return bHasRelationship;
    }

    //copy constructor
    public ListDescriptor(ListDescriptor toCopy, Boolean bHasRelationship) {
        this.bHasRelationship = bHasRelationship;
        this.association = toCopy.association;
        this.boundsMetaClasses = toCopy.boundsMetaClasses;
        this.compositeVisible = toCopy.compositeVisible;
        this.defaultColumns = toCopy.defaultColumns;
        this.displayText = toCopy.displayText;
        this.icon = toCopy.icon;
        this.includeObjectType = toCopy.includeObjectType;
        this.includeSequences = toCopy.includeSequences;
        this.neighborsMetaClass = toCopy.neighborsMetaClass;
    }

    public ListDescriptor(MetaClass neighborsMetaClass, MetaClass[] boundsMetaClasses) {
        this(null, neighborsMetaClass, boundsMetaClasses, null, false);
    }

    public ListDescriptor(MetaClass neighborsMetaClass, MetaClass[] boundsMetaClasses,
            boolean compositeVisible) {
        this(null, neighborsMetaClass, boundsMetaClasses, null, false);
        this.compositeVisible = compositeVisible;
    }

    /*
     * public ListDescriptor(MetaClass componentsMetaClass, MetaClass[] boundsMetaClasses, boolean
     * includeSequences){ this(null, componentsMetaClass, boundsMetaClasses, null,
     * includeSequences); }
     */

    public ListDescriptor(MetaClass neighborsMetaClass) {
        this(null, neighborsMetaClass, null, null, false);
    }

    public ListDescriptor(MetaClass neighborsMetaClass, String displayText, Icon icon) {
        this(null, neighborsMetaClass, null, null, false);
        this.displayText = displayText;
        this.icon = icon;
    }

    public ListDescriptor(MetaClass neighborsMetaClass, boolean compositeVisible) {
        this(null, neighborsMetaClass, null, null, false);
        this.compositeVisible = compositeVisible;
    }

    public ListDescriptor(MetaClass neighborsMetaClass, boolean compositeVisible,
            String compositeName) {
        this(null, neighborsMetaClass, null, null, false);
        this.compositeVisible = compositeVisible;
        this.compositeName = compositeName;
    }

    public ListDescriptor(MetaClass neighborsMetaClass, boolean compositeVisible,
            String displayText, Icon icon) {
        this(null, neighborsMetaClass, null, null, false);
        this.compositeVisible = compositeVisible;
        this.displayText = displayText;
        this.icon = icon;
    }

    public ListDescriptor(MetaClass neighborsMetaClass, boolean compositeVisible,
            String compositeName, String displayText, Icon icon) {
        this(null, neighborsMetaClass, null, null, false);
        this.compositeVisible = compositeVisible;
        this.displayText = displayText;
        this.compositeName = compositeName;
        this.icon = icon;
    }

    public ListDescriptor(MetaRelationship association, MetaClass neighborsMetaClass) {
        this(association, neighborsMetaClass, null, null, false);
    }

    public ListDescriptor(MetaRelationship association, MetaClass neighborsMetaClass,
            String displayText, Icon icon) {
        this(association, neighborsMetaClass, null, null, false);
        this.displayText = displayText;
        this.icon = icon;
    }

    public ListDescriptor(MetaClass neighborsMetaClass, Object[] defaultColumns) {
        this(null, neighborsMetaClass, null, defaultColumns, false);
    }

    public ListDescriptor(MetaClass neighborsMetaClass, Object[] defaultColumns,
            boolean compositeVisible) {
        this(null, neighborsMetaClass, null, defaultColumns, false);
        this.compositeVisible = compositeVisible;
    }

    public ListDescriptor(MetaClass neighborsMetaClass, Object[] defaultColumns,
            boolean compositeVisible, String compositeName) {
        this(null, neighborsMetaClass, null, defaultColumns, false);
        this.compositeVisible = compositeVisible;
        this.compositeName = compositeName;
    }

    public ListDescriptor(MetaRelationship association, MetaClass neighborsMetaClass,
            Object[] defaultColumns) {
        this(association, neighborsMetaClass, null, defaultColumns, false);
    }

    public ListDescriptor(MetaRelationship association, MetaClass neighborsMetaClass,
            Object[] defaultColumns, boolean compositeVisible) {
        this(association, neighborsMetaClass, null, defaultColumns, false);
        this.compositeVisible = compositeVisible;
    }

    public ListDescriptor(MetaRelationship association, MetaClass neighborsMetaClass,
            Object[] defaultColumns, boolean compositeVisible, String compositeName) {
        this(association, neighborsMetaClass, null, defaultColumns, false);
        this.compositeVisible = compositeVisible;
        this.compositeName = compositeName;
    }

    public ListDescriptor(MetaClass neighborsMetaClass, MetaClass[] boundsMetaClasses,
            Object[] defaultColumns) {
        this(null, neighborsMetaClass, boundsMetaClasses, defaultColumns, false);
    }

    public ListDescriptor(MetaClass neighborsMetaClass, MetaClass[] boundsMetaClasses,
            Object[] defaultColumns, boolean compositeVisible, String displayText, Icon icon) {
        this(null, neighborsMetaClass, boundsMetaClasses, defaultColumns, false);
        this.compositeVisible = compositeVisible;
        this.displayText = displayText;
        this.icon = icon;
    }

    public MetaClass getNeighborsMetaClass() {
        return neighborsMetaClass;
    }

    public MetaClass[] getBoundsMetaClasses() {
        return boundsMetaClasses;
    }

    public Object[] getDefaultColumns() {
        return defaultColumns;
    }

    public MetaRelationship getAssociation() {
        return association;
    }

    public boolean isIncludeSequences() {
        return includeSequences;
    }

    public void setIncludeObjectType(boolean include) {
        this.includeObjectType = include;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public void setDisplayText(String sDisplayText) {
        this.displayText = sDisplayText;
    }

    public String toString() {
        if (displayText != null)
            return displayText;

        SemanticalModel sm = ApplicationContext.getSemanticalModel();
        String name = sm.getDisplayText(neighborsMetaClass, null, ListDescriptor.class, true);
        if (name == null)
            name = neighborsMetaClass.getGUIName(true);

        return name;
    }

    public Icon getIcon() {
        if (icon != null)
            return icon;
        return neighborsMetaClass.getIcon();
    }

}
