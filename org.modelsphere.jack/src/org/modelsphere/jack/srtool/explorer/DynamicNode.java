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

package org.modelsphere.jack.srtool.explorer;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.util.CollationComparator;

public class DynamicNode extends DefaultMutableTreeNode implements Comparable {

    protected GroupParams groupParams = GroupParams.defaultGroupParams;
    protected int insertIndex = -1;
    protected String displayText = ""; // not used for secondary nodes
    private String tooltips = null; // not used for secondary nodes
    protected String editText = null; // not used for secondary nodes
    protected Icon icon = null; // not used for secondary nodes
    protected boolean hasLoaded = false;// not used for secondary nodes
    protected boolean isLeaf = false; // not used for secondary nodes
    protected AbstractApplicationAction defaultAction = null; // action to perform on double click

    /*
     * A DbObject may appear more than once in a tree; in this case, we have a primary node and a
     * number of secondary nodes. The primary node refers to the DbObject itself and its parent node
     * is the one given by the method <getDbParent>. The secondary nodes refer to the primary node;
     * they are not expandable. To create a secondary node, we must first create the primary node,
     * i.e. perform <loadChildren> on the parent of the primary node.
     */
    public DynamicNode(Object o) {
        super(o);
    }

    public DynamicNode(Object o, int insertIndex) {
        super(o);
        this.insertIndex = insertIndex;
    }

    public final Object getRealObject() {
        return (userObject instanceof DynamicNode ? ((DynamicNode) userObject).userObject
                : userObject);
    }

    public final int getInsertIndex() {
        return insertIndex;
    }

    public final GroupParams getGroupParams() {
        return groupParams;
    }

    public final void setGroupParams(GroupParams groupParams) {
        this.groupParams = groupParams;
    }

    public final AbstractApplicationAction getDefaultAction() {
        return defaultAction;
    }

    public final void setDefaultAction(AbstractApplicationAction defaultAction) {
        this.defaultAction = defaultAction;
    }

    public final void setDisplayText(String text) {
        setDisplayText(text, text);
    }

    public final void setToolTips(String tooltips) {
        this.tooltips = tooltips; // tooltips text is never null.
    }

    public final void setDisplayText(String displayText, String editText) {
        this.displayText = (displayText == null ? "" : displayText); // display
        // text
        // is
        // never
        // null.
        this.editText = editText;
    }

    public final String toString() {
        return (userObject instanceof DynamicNode ? ((DynamicNode) userObject).displayText
                : displayText);
    }

    public final String getEditText() {
        return (userObject instanceof DynamicNode ? ((DynamicNode) userObject).editText : editText);
    }

    public final String getToolTips() {
        if (userObject instanceof DynamicNode) {
            return (((DynamicNode) userObject).tooltips == null ? ((DynamicNode) userObject).displayText
                    : ((DynamicNode) userObject).tooltips);
        }
        return (tooltips == null ? displayText : tooltips);
    }

    public final Icon getIcon() {
        return (userObject instanceof DynamicNode ? ((DynamicNode) userObject).icon : icon);
    }

    public final void setIcon(Icon icon) {
        this.icon = icon;
    }

    public final boolean hasLoaded() {
        return hasLoaded;
    }

    public final void setHasLoaded() {
        hasLoaded = true;
    }

    public final boolean isLeaf() {
        return (isLeaf || userObject instanceof DynamicNode);
    }

    public final void setIsLeaf(boolean state) {
        isLeaf = state;
    }

    public final int compareTo(Object obj) {
        DynamicNode otherNode = (DynamicNode) obj;
        if (groupParams.index != otherNode.groupParams.index)
            return (groupParams.index < otherNode.groupParams.index ? -1 : 1);
        if (groupParams.sorted)
            return CollationComparator.getDefaultCollator().compare(toString(),
                    otherNode.toString());
        else
            return (insertIndex < otherNode.insertIndex ? -1 : 1);

    }
}
