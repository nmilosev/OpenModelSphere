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

package org.modelsphere.jack.desktop;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;

import org.modelsphere.jack.actions.AbstractActionsStore;
import org.modelsphere.jack.actions.JInternalFrameAbstractAction;
import org.modelsphere.jack.awt.JackMenu;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.StringUtil;

public class JInternalFrameManagerMenu extends JackMenu {
    private final int WINDOW_PERMANENT_ITEM_COUNT;
    private JDesktopPane desktop;
    private final String internalFramePatern = "({0}) {1}";

    public JInternalFrameManagerMenu(JDesktopPane desktoppane) {
        super(LocaleMgr.action.getString("Window"));
        Debug.assert2(desktoppane != null,
                "JDesktopPane parameter in JInternalFrameManagerMenu() can't be null");
        desktop = desktoppane;

        setMnemonic(LocaleMgr.action.getMnemonic("Window"));

        JInternalFrameAbstractAction action = (JInternalFrameAbstractAction) ApplicationContext
                .getActionStore().getAction(AbstractActionsStore.DIAGRAM_LAYOUT_CASCADE);
        JMenuItem item = add(action);
        action.setJDesktopPane(desktop);

        action = (JInternalFrameAbstractAction) ApplicationContext.getActionStore().getAction(
                AbstractActionsStore.DIAGRAM_LAYOUT_TILE_HORIZONTAL);
        item = add(action);
        action.setJDesktopPane(desktop);

        action = (JInternalFrameAbstractAction) ApplicationContext.getActionStore().getAction(
                AbstractActionsStore.DIAGRAM_LAYOUT_TILE_VERTICAL);
        item = add(action);
        action.setJDesktopPane(desktop);

        action = (JInternalFrameAbstractAction) ApplicationContext.getActionStore().getAction(
                AbstractActionsStore.DIAGRAM_CLOSE_ALL);
        item = add(action);
        action.setJDesktopPane(desktop);

        action = (JInternalFrameAbstractAction) ApplicationContext.getActionStore().getAction(
                AbstractActionsStore.DIAGRAM_ARRANGE_ICONS);
        item = add(action);
        action.setJDesktopPane(desktop);

        addSeparator();
        WINDOW_PERMANENT_ITEM_COUNT = getItemCount();
    }

    public final void menuSelected(MenuEvent e) {
        super.menuSelected(e);
        buildWindowMenuItems();
    }

    public final JDesktopPane getJDesktopPane() {
        return desktop;
    }

    private final void buildWindowMenuItems() {
        JInternalFrame[] frames = desktop.getAllFrames();

        int tempItemsNb = getItemCount() - WINDOW_PERMANENT_ITEM_COUNT;
        if (tempItemsNb > 0) {
            for (int i = 0; i < tempItemsNb; i++) {
                remove(WINDOW_PERMANENT_ITEM_COUNT);
            }
        }

        // TODO: check in further releases of swing (as of jdk 1.2.2) if menu
        // can be displayed larger than almost half of
        // the screen width. In 1.2.2, the menu don't display well if an item is
        // larger than the available space
        // on screen to the right or to the left of its location x. For now,
        // JMenu can locate the popup out of screen to
        // the left (negative x). So I assume here that there will be at least
        // almost haft the screen width available at the right
        // or at the left of the location x of the menu. Almost all Screen width
        // should be the itemMaxWidth.
        // the - 30 represents an approximate size of an icon and menu borders
        // width

        int itemMaxWidth = Math.max(
                (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2) - 30, 100);

        for (int i = 0; i < frames.length; i++) {
            String name = MessageFormat.format(internalFramePatern, new Object[] {
                    String.valueOf(i), frames[i].getTitle() });
            InternalFrameMenuItem item = new InternalFrameMenuItem(name, frames[i]);
            name = StringUtil.truncateString(name, item.getFontMetrics(item.getFont()),
                    itemMaxWidth);
            item.setText(name);
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    InternalFrameMenuItem menuitem = (InternalFrameMenuItem) e.getSource();
                    JInternalFrame intframe = menuitem.getInternalFrame();
                    try {
                        intframe.setIcon(false);
                        intframe.setSelected(true);
                    } catch (Exception ex) {
                        org.modelsphere.jack.debug.Debug.handleException(ex);
                    }
                }
            });
            item.setMnemonic(String.valueOf(i).charAt(0));
            add(item);
        }
    }

    private static class InternalFrameMenuItem extends JMenuItem {
        JInternalFrame frame;

        InternalFrameMenuItem(String name, JInternalFrame ifr) {
            super(name);
            this.frame = ifr;
            org.modelsphere.jack.debug.Debug.assert2(ifr != null,
                    "JInternalFrameManagerMenu:  Item created with null JIngernalFrame");
            if (frame == null)
                setEnabled(false);
            else if (frame.getFrameIcon() != null)
                setIcon(frame.getFrameIcon());
        }

        final JInternalFrame getInternalFrame() {
            return frame;
        }

    }

}
