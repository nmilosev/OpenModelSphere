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

package org.modelsphere.jack.awt.checklist;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.modelsphere.jack.awt.CheckMultiIcon;

final class CheckListCellRenderer extends JCheckBox implements ListCellRenderer {
    private CheckMultiIcon nonSelectedIconEnabled = new CheckMultiIcon(false, true);
    private CheckMultiIcon selectedIconEnabled = new CheckMultiIcon(true, true);
    private CheckMultiIcon nonSelectedIconDisabled = new CheckMultiIcon(false, false);
    private CheckMultiIcon selectedIconDisabled = new CheckMultiIcon(true, false);
    private int checkIconWidth = UIManager.getIcon("CheckBox.icon").getIconWidth(); // NOT LOCALIZABLE - property
    private static Border NO_FOCUS_BORDER;

    public CheckListCellRenderer() {
        super();
        if (NO_FOCUS_BORDER == null) {
            NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
        }
        setOpaque(true);
        setBorder(NO_FOCUS_BORDER);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        if (value instanceof CheckListItem) {
            CheckListItem item = (CheckListItem) value;
            setSelected(item.selected);
            CheckMultiIcon icon = null;
            if (item.selected) {
                icon = list.isEnabled() ? selectedIconEnabled : selectedIconDisabled;
            } else {
                icon = list.isEnabled() ? nonSelectedIconEnabled : nonSelectedIconDisabled;
            }
            icon.setUserIcon(item.icon);
            setIcon(icon);
        } else {
            setIcon(null);
        }
        setText((value == null) ? "" : value.toString());

        // setEnabled(list.isEnabled());
        setFont(list.getFont());
        setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder")
                : NO_FOCUS_BORDER); // NOT
        // LOCALIZABLE
        return this;
    }

    // Overrided for performance reasons
    public void validate() {
    }

    public void revalidate() {
    }

    public void repaint(long tm, int x, int y, int width, int height) {
    }

    public void repaint(Rectangle r) {
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        // Strings get interned...
        if (propertyName == "text") // NOT LOCALIZABLE
            super.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {
    }

    public void firePropertyChange(String propertyName, char oldValue, char newValue) {
    }

    public void firePropertyChange(String propertyName, short oldValue, short newValue) {
    }

    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
    }

    public void firePropertyChange(String propertyName, long oldValue, long newValue) {
    }

    public void firePropertyChange(String propertyName, float oldValue, float newValue) {
    }

    public void firePropertyChange(String propertyName, double oldValue, double newValue) {
    }

    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
    }

}
