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
package org.modelsphere.jack.awt.tree;

import javax.swing.tree.*;
import javax.swing.Icon;

// This class add to the DefaultMutableTreeNode a support for the selected property.
// It can be used with the CheckableTree and the CheckRenderer to provide a checkbox in
// a tree node.
public class CheckTreeNode extends DefaultMutableTreeNode {
    private boolean selected = false;
    private Icon icon;
    private String displayText;

    public CheckTreeNode(Object value, boolean allowchildren, boolean selected) {
        this(value, allowchildren, selected, (value == null ? null : value.toString()), null);
    }

    public CheckTreeNode(Object value, boolean allowchildren, boolean selected, String displayText) {
        this(value, allowchildren, selected, displayText, null);
    }

    public CheckTreeNode(Object value, boolean allowchildren, boolean selected, String displayText,
            Icon icon) {
        super(value, allowchildren);
        this.selected = selected;
        this.displayText = (displayText == null) ? "" : displayText;
        this.icon = icon;
    }

    public final boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean b) {
        selected = b;
    }

    public final Icon getIcon() {
        return icon;
    }

    public final void setIcon(Icon icon) {
        this.icon = icon;
    }

    public final void setDisplayText(String text) {
        displayText = (text == null ? "" : text);
    }

    public String toString() {
        return displayText;
    }

}
