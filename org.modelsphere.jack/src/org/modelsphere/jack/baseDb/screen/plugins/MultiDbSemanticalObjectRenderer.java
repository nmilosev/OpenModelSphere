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

package org.modelsphere.jack.baseDb.screen.plugins;

import java.awt.Component;
import java.awt.Image;
import javax.swing.*;

import org.modelsphere.jack.baseDb.screen.MultiDefaultRenderer;
import org.modelsphere.jack.util.DefaultComparableElement;

public class MultiDbSemanticalObjectRenderer extends MultiDefaultRenderer {

    public static final MultiDbSemanticalObjectRenderer singleton = new MultiDbSemanticalObjectRenderer();

    protected MultiDbSemanticalObjectRenderer() {
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value != null && value instanceof DefaultComparableElement) {
            if (((DefaultComparableElement) value).object2 != null) {
                setIcon(new ImageIcon((Image) ((DefaultComparableElement) value).object2));
                setHorizontalTextPosition(SwingConstants.LEFT);
            }
        }
        return this;
    }

    protected String getStringForNullValue() {
        return "";
    }

}
