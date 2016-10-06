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

package org.modelsphere.jack.actions;

import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.Icon;
import javax.swing.SwingUtilities;

/**
 * This class provides offer an additionnal feature over AbstractApplicationAction. It can manage a
 * list of values (domain) and a selected value from this list of values. When adding to a JackMenu,
 * a new Menu is created and for each domain value, a menu item is created. When adding to a
 * JackToolbar, a new DomainComboBox is created with an item for each domain value.
 * 
 * NOTE: Read carefully comments for each fields and methods before using this class.
 */

public abstract class AbstractDomainAction extends AbstractApplicationAction {

    // property name for the domain
    // Contains Color, Icon or null values. Other type will be consider as
    // 'toString()'
    // null value may be represented as a separator in GUI depending of the GUI
    // component
    // this property can be set to null
    // Note: These values are the displayed values in the ui.
    public static final String VALUES = "values"; // NOT LOCALIZABLE, property
    // key

    // property name for the selected object index
    // cannot be null - type is Integer
    // Use value -1 for no selection
    // null object of property VALUES will never become the value represented by
    // this index
    public static final String SELECTED_VALUE = "selected value"; // NOT
    // LOCALIZABLE,
    // property
    // key

    // Specify if the ui components created for this action domain values must
    // show the selection
    // For example: setting this value to true will display radio menu items in
    // this action menus,
    // otherwise, normal menu item will be displayed.
    private boolean uiSelectionVisible = true;

    // Used to control action events and property change events
    private boolean preventEvent = false;

    public AbstractDomainAction(String name, Icon icon, Object[] values, boolean uiselectionvisible) {
        super(name, icon);
        this.uiSelectionVisible = uiselectionvisible;
        setDomainValues(values);
    }

    public AbstractDomainAction(String name, Icon icon, Object[] values) {
        this(name, icon, values, true);
    }

    public AbstractDomainAction(String name, Object[] values) {
        this(name, null, values, true);
    }

    public AbstractDomainAction(String name) {
        this(name, null, null, true);
    }

    public AbstractDomainAction(String name, Object[] values, boolean uiselectionvisible) {
        this(name, null, values, uiselectionvisible);
    }

    public AbstractDomainAction(String name, boolean uiselectionvisible) {
        this(name, null, null, uiselectionvisible);
    }

    public final void setDomainValues(Object[] newValue) {
        putValue(SELECTED_VALUE, new Integer(-1));
        putValue(VALUES, newValue);
    }

    public final Object[] getDomainValues() {
        Object[] value = (Object[]) getValue(VALUES);
        return value;
    }

    private final void setSelectedImpl(int idx) {
        putValue(SELECTED_VALUE, new Integer(idx));
    }

    // IMPORTANT: DO NOT USE THIS METHOD.
    // THIS METHOD IS CALLED BY THIS ACTION COMPONENTS FOR TRIGGERING THE ACTION
    // PERFORMED
    public final void setSelectedPrivate_(int idx, Object source) {
        if (!preventEvent) {
            preventEvent = true;
            setSelectedImpl(idx);
            actionPerformed(new ActionEvent(source, ActionEvent.ACTION_PERFORMED, "")); // NOT LOCALIZABLE -
            // empty string
            SwingUtilities.invokeLater(new Runnable() { // Make sure that
                        // changes made during
                        // actionPerformed are
                        // propagated to this
                        // action's components
                        public void run() {
                            preventEvent = false;
                            firePropertyChange(SELECTED_VALUE, null, getValue(SELECTED_VALUE));
                        }
                    });
        }
    }

    // Overrided to control events when property SELECTED_VALUE is updated
    // during actionPerform()
    protected final void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (preventEvent && propertyName.equals(SELECTED_VALUE)) // This
            // property
            // cannot be
            // updated
            // in some
            // components
            // during
            // actionPerformed.
            // The property change listeners will be fired after actionPerformed
            // to ensure
            // that their selected values are valid.
            return;
        super.firePropertyChange(propertyName, oldValue, newValue);
    }

    protected final void setSelectedObject(Object selObj) {
        if (selObj == null)
            setSelectedImpl(-1);
        else {
            setSelectedImpl(getDomainValues() == null ? -1 : Arrays.asList(getDomainValues())
                    .indexOf(selObj));
        }
    }

    protected final void setSelectedIndex(int selidx) {
        Object[] values = getDomainValues();
        if (values != null && selidx > -2 && values.length > selidx)
            setSelectedImpl(selidx);
        else
            setSelectedImpl(-1);
    }

    public final Object getSelectedObject() {
        Integer idx = (Integer) getValue(SELECTED_VALUE);
        if (idx != null && idx.intValue() > -1) {
            Object[] value = (Object[]) getValue(VALUES);
            return value == null ? null : Arrays.asList(value).get(idx.intValue());
        }
        return null;
    }

    public final int getSelectedIndex() {
        Integer value = (Integer) getValue(SELECTED_VALUE);
        // make sure that nobody set the property using the putValue method
        return value == null ? -1 : value.intValue();
    }

    public final boolean isUISelectionVisible() {
        return uiSelectionVisible;
    }

}
