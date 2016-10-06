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

package org.modelsphere.jack.srtool;

import javax.swing.Icon;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;

/**
 * This class is a delegate for representing the GUI value to display for adding a specific object's
 * type and for executing the Add action.
 */
public class AddElement {
    protected MetaClass[] compositeMetaclasses;
    protected MetaClass componentMetaclass;
    protected Object[] parameters;
    protected boolean multiSelection;
    protected String transName;
    protected String name; // default is componentMetaclass GUI name
    protected boolean targetVisibleInName = false;
    protected boolean visible = true;
    protected boolean enabled = true;
    protected Icon icon = null; // default is componentMetaclass icon

    // null subValues indicate a final AddElement
    protected Object[] choiceValues = null;
    // The selected value is set by the Action during actionPerformed before
    // delegating the creation with createElement()
    protected int choiceValuesSelectedIndex = -1;

    public AddElement(Object[] choiceValues, MetaClass componentMetaclass,
            MetaClass[] compositeMetaclasses, String transName) {
        this(componentMetaclass, compositeMetaclasses, transName, null, true, false);
        this.choiceValues = choiceValues;
    }

    public AddElement(MetaClass componentMetaclass, MetaClass[] compositeMetaclasses,
            String transName) {
        this(componentMetaclass, compositeMetaclasses, transName, null, true, false);
    }

    public AddElement(MetaClass componentMetaclass, MetaClass[] compositeMetaclasses) {
        this(componentMetaclass, compositeMetaclasses, null, null, true, false);
    }

    public AddElement(MetaClass componentMetaclass, MetaClass[] compositeMetaclasses, Icon icon) {
        this(componentMetaclass, compositeMetaclasses, null, null, true, false);
        this.icon = icon;
    }

    public AddElement(Object[] choiceValues, MetaClass componentMetaclass,
            MetaClass[] compositeMetaclasses) {
        this(componentMetaclass, compositeMetaclasses, null, null, true, false);
        this.choiceValues = choiceValues;
    }

    public AddElement(MetaClass componentMetaclass, MetaClass compositeMetaclass, String transName) {
        this(componentMetaclass, new MetaClass[] { compositeMetaclass }, transName, null, true,
                false);
    }

    public AddElement(MetaClass componentMetaclass, MetaClass compositeMetaclass) {
        this(componentMetaclass, new MetaClass[] { compositeMetaclass }, null, null, true, false);
    }

    public AddElement(MetaClass componentMetaclass, MetaClass compositeMetaclass,
            boolean targetVisibleInName) {
        this(componentMetaclass, new MetaClass[] { compositeMetaclass }, null, null, true,
                targetVisibleInName);
    }

    public AddElement(MetaClass componentMetaclass, MetaClass[] compositeMetaclasses,
            String transName, Object[] parameters) {
        this(componentMetaclass, compositeMetaclasses, transName, parameters, true, false);
    }

    public AddElement(MetaClass componentMetaclass, MetaClass[] compositeMetaclasses, String name,
            String transName, Icon icon, Object[] parameters) {
        this(componentMetaclass, compositeMetaclasses, transName, parameters, true, false);
        this.name = name;
        this.icon = icon;
    }

    public AddElement(MetaClass componentMetaclass, MetaClass[] compositeMetaclasses,
            String transName, Object[] parameters, boolean multiSelection,
            boolean targetVisibleInName) {
        this.componentMetaclass = componentMetaclass;
        this.compositeMetaclasses = compositeMetaclasses;
        this.targetVisibleInName = targetVisibleInName;
        this.parameters = parameters;
        this.multiSelection = multiSelection;
        this.transName = transName;
    }

    public final MetaClass getMetaClass() {
        return componentMetaclass;
    }

    public String getTransName() {
        return transName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Icon getIcon() {
        if (icon != null)
            return icon;
        return getMetaClass().getIcon();
    }

    public final Object[] getChoiceValues() {
        return choiceValues;
    }

    public final void setChoiceValues(Object[] choiceValues) {
        this.choiceValues = choiceValues;
    }

    // This method is one way (This will not show a selection on the GUI
    // component)
    // This method is used by the AddAction to prepare the AddElement before
    // preCreateElement()
    public final void setChoiceValuesSelectedIndex(int index) {
        this.choiceValuesSelectedIndex = index;
    }

    public final void setVisible(boolean visible) {
        this.visible = visible;
    }

    public final boolean isVisible() {
        return visible;
    }

    public final void setEnabled(boolean enable) {
        this.enabled = enable;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    public void update() throws DbException {
        visible = enabled = true;
    }

    public final MetaClass[] getCompositeMetaClasses() {
        return compositeMetaclasses;
    }

    public DbObject createElement(DbObject composite) throws DbException {
        if (composite == null || componentMetaclass == null)
            return null;
        if (!componentMetaclass.compositeIsAllowed(composite.getMetaClass()))
            return null;
        preCreateElement(composite);
        Class[] paramClasses = parameters == null ? null : new Class[parameters.length];
        for (int i = 0; parameters != null && i < parameters.length; i++) {
            paramClasses[i] = parameters[i] == null ? Object.class : parameters[i].getClass();
        }
        DbObject component = composite
                .createComponent(componentMetaclass, parameters, paramClasses);
        postCreateElement(component);
        return component;
    }

    protected void preCreateElement(DbObject composite) throws DbException {
    }

    protected void postCreateElement(DbObject component) throws DbException {
    }

    public final String toString() {
        if (name != null)
            return name;
        if (componentMetaclass == null)
            return super.toString();
        return componentMetaclass.getGUIName(false, targetVisibleInName);
    }

}
