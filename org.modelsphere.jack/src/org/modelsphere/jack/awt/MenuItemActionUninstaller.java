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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

/**
 * This class ensure that the links between an action and a menu item will be broken. Details of the
 * problems solved by the use of this class: This apply only to items contained in a popup menu. If
 * we don't do this, menu items will never be garbage collected because the singleton action still
 * contain a link to the component(property change listener). Worst, these unused components will
 * still be triggered for property change on the action. And each time an action is added to the
 * popup, a new component is created (adding on more listener on the action). This can cause memory
 * problem and lost of performance getting worst each time a popup is created.
 */

final class MenuItemActionUninstaller implements PropertyChangeListener {
    private JMenuItem item;

    MenuItemActionUninstaller(JMenuItem mi) {
        item = mi;
        item.addPropertyChangeListener(this);
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().equals("ancestor") && (e.getNewValue() == null)) { // NOT
            // LOCALIZABLE
            // -
            // property
            if ((item instanceof JackMenu) && ((JackMenu) item).isPopupSubMenu()) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (item != null) {
                            if ((item.getParent() != null) && !item.getParent().isVisible()) {
                                // Check for all sub menu components that may be
                                // linked to an action and remove this link
                                Component[] comps = ((JackMenu) item).getMenuComponents();
                                for (int i = 0; i < comps.length; i++) {
                                    Object subitem = comps[i];
                                    if (subitem instanceof JMenuItem)
                                        ((JMenuItem) subitem).setAction(null);
                                }
                                item.removePropertyChangeListener(MenuItemActionUninstaller.this);
                                item = null;
                            }
                        }
                    }
                });
                return;
            }
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    if (item != null) {
                        if ((item.getParent() != null) && !item.getParent().isVisible()) {
                            item.setAction(null);
                            item.removePropertyChangeListener(MenuItemActionUninstaller.this);
                            item = null;
                        }
                    }
                }
            });
        }
    }
}
