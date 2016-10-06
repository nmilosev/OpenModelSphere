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
import java.awt.event.*;
import java.util.EventListener;
import javax.swing.*;
import javax.swing.event.*;

public class CheckList extends JList implements ListSelectionListener, MouseListener {
    private Rectangle iconRect;

    public CheckList() {
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addListSelectionListener(this);
        addMouseListener(this);
        setCellRenderer(new CheckListCellRenderer());
    }

    public void addCheckListListener(CheckListListener listener) {
        listenerList.add(CheckListListener.class, listener);
    }

    public void removeCheckListListener(CheckListListener listener) {
        listenerList.remove(CheckListListener.class, listener);
    }

    public void mouseClicked(MouseEvent e) {
        if (iconRect == null) {
            Icon checkIcon = UIManager.getIcon("CheckBox.icon"); // NOT
            // LOCALIZABLE
            if (checkIcon != null) {
                iconRect = new Rectangle(checkIcon.getIconWidth(), checkIcon.getIconHeight());
            } else {
                iconRect = new Rectangle(16, 16);
            }
        }
        int index = locationToIndex(e.getPoint());
        if (index == -1)
            return;

        Object value = getModel().getElementAt(index);
        if (!(value instanceof CheckListItem))
            return;
        CheckListItem item = (CheckListItem) value;
        if (iconRect != null) {
            if (iconRect.width + ((JComponent) getCellRenderer()).getInsets().left < e.getPoint().x)
                return;
        }
        item.selected = !item.selected;
        fireCheckListListener(item);
        repaint();
    }

    private void fireCheckListListener(CheckListItem item) {
        EventListener[] listeners = listenerList.getListeners(CheckListListener.class);
        for (int i = 0; listeners != null && i < listeners.length; i++) {
            ((CheckListListener) listeners[i]).itemSelectedStateChanged(item);
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void valueChanged(ListSelectionEvent e) {
        if (iconRect == null) {
            Icon checkIcon = UIManager.getIcon("CheckBox.icon"); // NOT
            // LOCALIZABLE
            if (checkIcon != null) {
                iconRect = new Rectangle(checkIcon.getIconWidth(), checkIcon.getIconHeight());
            } else {
                iconRect = new Rectangle(16, 16);
            }
        }
    }

    public void updateUI() {
        iconRect = null;
        super.updateUI();
    }

}
