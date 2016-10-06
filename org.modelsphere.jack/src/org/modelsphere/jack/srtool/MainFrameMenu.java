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

package org.modelsphere.jack.srtool;

import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.*;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.awt.JackMenu;
import org.modelsphere.jack.preference.RecentFiles;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.util.StringUtil;

public class MainFrameMenu {
    public static final String MENU_FILE = "file"; // NOT LOCALIZABLE - Property Key
    public static final String MENU_EDIT = "edit"; // NOT LOCALIZABLE - Property Key
    public static final String MENU_DISPLAY = "display"; // NOT LOCALIZABLE - Property Key
    public static final String MENU_FORMAT = "format"; // NOT LOCALIZABLE - Property Key
    public static final String MENU_TOOLS = "tools"; // NOT LOCALIZABLE - Property Key
    public static final String MENU_HELP = "help"; // NOT LOCALIZABLE - Property Key
    public static final String MENU_DEBUG = "debug"; // NOT LOCALIZABLE - Property Key
    
    public static final String WINDOW = "window"; // NOT LOCALIZABLE - Property Key
    private static final String RECENT_FILES = "recentFiles"; // NOT LOCALIZABLE - Property Key

    static final String RFKeyWord = LocaleMgr.misc.getString("RecentFile");
    private DefaultMainFrame mf;

    JMenuBar menuBar1 = new JMenuBar();
    //menu file
    JackMenu menuForRecentFile = new JackMenu(RECENT_FILES);

    private HashMap keyMapping = new HashMap(8);

    protected MainFrameMenu(DefaultMainFrame mf) {
        this.mf = mf;
    }

    public void initMenus() {
    }

    protected void addMenuToMenuBar(JMenu menu, String key) {
        menuBar1.add(menu);
        if (key != null)
            keyMapping.put(key, menu);
    }

    protected final void setMenuForRecentFile(JackMenu menuForRecentFile) {
        this.menuForRecentFile = menuForRecentFile;
    }

    final void setDefaultMainFrame(DefaultMainFrame mf) {
        this.mf = mf;
    }

    protected final DefaultMainFrame getDefaultMainFrame() {
        return mf;
    }

    public JMenu getMenuForKey(String key) {
        return (JMenu) keyMapping.get(key);
    }

    public final void updateRecentFiles() {
        updateRecentFiles(menuForRecentFile);
    }

    public final void updateRecentFiles(JackMenu menu) {
        if (menu != null) {
            RecentFiles recentFiles;

            menu.removeAll();
            recentFiles = mf.getRecentFiles();
            if (recentFiles.isVisible()) {
                int i = 0;

                recentFiles.clean();

                int max = recentFiles.getNbVisibleRF() < recentFiles.size() ? recentFiles
                        .getNbVisibleRF() : recentFiles.size();

                while (i < max) {
                    String fileName = recentFiles.elementAt(i);
                    JMenuItem rfMenuItem = new JMenuItem(StringUtil.truncateFileName(fileName));
                    rfMenuItem.setActionCommand(RFKeyWord + fileName);
                    menu.add(rfMenuItem);

                    rfMenuItem.addActionListener(new java.awt.event.ActionListener() {
                        public final void actionPerformed(ActionEvent e) {
                            mf.doOpenFromFile(e.getActionCommand().substring(RFKeyWord.length()));
                        }
                    });

                    i++;
                }
            }
        }
    }

    public final JMenuBar getMenuBar() {
        return menuBar1;
    }

    /**
     * -typeMenu support JMENU and JackMenu -objects
     * 
     * [0] = Name of Menu NOT NULL [1] = Mnemonic of Menu NOT NULL for each items { if not null { if
     * instance of Menu objects[x] = subMenu objects[x + 1] = name of subMenu NOT NULL else
     * objects[x] = action of itemMenu if instanceof accelerator objects[x + 1] = accelerator of
     * itemMenu if instanceof String objects[x + (1 or 2)] = Mnemonic of itemMenu } else it's a
     * separator }
     */
    protected static final JackMenu createMenu(String menuKey, Object[] objects) {
        int i = 0;
        JackMenu menu = null;

        try {
            menu = new JackMenu(menuKey);
            menu.setText((String) objects[i]);
            i++;
            menu.setMnemonic(((String) objects[i]).charAt(0));

            for (i++; i < objects.length; i++) {
                if (objects[i] != null) {
                    if (objects[i] instanceof JMenu) {
                        menu.add((JackMenu) objects[i]);
                        i++;
                        ((JackMenu) (objects[i - 1])).setText((String) objects[i]);
                    } else if (objects[i] instanceof AbstractApplicationAction) {
                        AbstractApplicationAction action = (AbstractApplicationAction)objects[i]; 
                        
                        if (action.isVisible()) {
                            JMenuItem menuItem = menu.add(action); 
                            
                            if (i + 1 < objects.length && objects[i + 1] instanceof KeyStroke) {
                                i++;
                                menuItem.setAccelerator((KeyStroke) objects[i]);
                            }
                            if (i + 1 < objects.length && objects[i + 1] instanceof String) {
                                i++;
                                menuItem.setMnemonic(((String) objects[i]).charAt(0));
                            }
                        } //end if
                    } else {
                       //ignore
                    }
                } else {
                    menu.addSeparator();
                } //end if
            } //end for
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                    org.modelsphere.jack.srtool.ApplicationContext.getDefaultMainFrame(), e);
        }
        return menu;
    }

}
