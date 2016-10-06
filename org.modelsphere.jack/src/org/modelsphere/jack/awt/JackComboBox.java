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

package org.modelsphere.jack.awt;

import java.awt.Component;
import java.beans.PropertyChangeListener;

import javax.swing.*;

/**
 * Add support for displaying SeparatorIcon, or other icons
 */

public class JackComboBox extends JComboBox {

    public JackComboBox() {
        super();
        setRenderer(new DomainListCellRenderer());
    }

    // Overrided to prevent selection of separator items and to prevent
    // triggering of a non change item
    public void setSelectedItem(Object anObject) {
        if (!(anObject instanceof SeparatorIcon)) {
            super.setSelectedItem(anObject);
        }
    }


	static class DomainListCellRenderer extends DefaultListCellRenderer implements ListCellRenderer<Object> {
        public DomainListCellRenderer() {
            super();
        }

        public Component getListCellRendererComponent(JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            if (value instanceof SeparatorIcon) {
                setComponentOrientation(list.getComponentOrientation());
                setFont(list.getFont());
                setText("");
                setBorder(null);
                setIcon((Icon) value);
                setBackground(list.getBackground());
                setForeground(list.getForeground());
                setHorizontalAlignment(SwingConstants.LEFT);
                return this;
            }

            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Icon)
                setHorizontalAlignment(SwingConstants.CENTER);
            else if (value instanceof Integer)
                setHorizontalAlignment(SwingConstants.RIGHT);
            else
                // String or other text component (value.toString())
                setHorizontalAlignment(SwingConstants.LEFT);
            return this;
        }
    }

    protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
        // should be evorrided -- if needed, we can provide one
        org.modelsphere.jack.debug.Debug
                .assert2(false,
                        "JackComboBox:  createActionPropertyChangeListener -- should not be in this method");
        return null;
    }

}
