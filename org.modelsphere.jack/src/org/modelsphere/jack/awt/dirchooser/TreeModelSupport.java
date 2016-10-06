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

package org.modelsphere.jack.awt.dirchooser;

import javax.swing.event.*;
import java.util.*;

class TreeModelSupport {
    private Vector vector = new Vector();

    public void addTreeModelListener(TreeModelListener listener) {
        if (listener != null && !vector.contains(listener)) {
            vector.addElement(listener);
        }
    }

    public void removeTreeModelListener(TreeModelListener listener) {
        if (listener != null) {
            vector.removeElement(listener);
        }
    }

    public void fireTreeNodesChanged(TreeModelEvent e) {
        Enumeration listeners = vector.elements();
        while (listeners.hasMoreElements()) {
            TreeModelListener listener = (TreeModelListener) listeners.nextElement();
            listener.treeNodesChanged(e);
        }
    }

    public void fireTreeNodesInserted(TreeModelEvent e) {
        Enumeration listeners = vector.elements();
        while (listeners.hasMoreElements()) {
            TreeModelListener listener = (TreeModelListener) listeners.nextElement();
            listener.treeNodesInserted(e);
        }
    }

    public void fireTreeNodesRemoved(TreeModelEvent e) {
        Enumeration listeners = vector.elements();
        while (listeners.hasMoreElements()) {
            TreeModelListener listener = (TreeModelListener) listeners.nextElement();
            listener.treeNodesRemoved(e);
        }
    }

    public void fireTreeStructureChanged(TreeModelEvent e) {
        Enumeration listeners = vector.elements();
        while (listeners.hasMoreElements()) {
            TreeModelListener listener = (TreeModelListener) listeners.nextElement();
            listener.treeStructureChanged(e);
        }
    }
}
