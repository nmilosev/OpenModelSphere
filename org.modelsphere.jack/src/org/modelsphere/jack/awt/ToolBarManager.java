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

import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.*;
import javax.swing.plaf.ToolBarUI;
import javax.swing.plaf.basic.BasicToolBarUI;

import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.ApplicationContext;

/**
 * This class provides a way for managing multiple toolbar
 * 
 * 
 */

public final class ToolBarManager implements ActionListener, /* PropertiesProvider, */
        ComponentListener {
    public static final int INSTALL_IN_DEFAULT_CONTAINER = 0;
    public static final int INSTALL_IN_DIALOG = 1;

    static final String VISIBLE_PROPERTY_EXTENTION = "_visible"; // NOT LOCALIZABLE, property key
    static final String FLOATING_PROPERTY_EXTENTION = "_floating"; // NOT LOCALIZABLE, property key
    static final String FLOATING_LOC_X_PROPERTY_EXTENTION = "_floatingX"; // NOT LOCALIZABLE, property key
    static final String FLOATING_LOC_Y_PROPERTY_EXTENTION = "_floatingY"; // NOT LOCALIZABLE, property key

    private static final String DIALOG_LOC_X = "_x"; // NOT LOCALIZABLE, property key
    private static final String DIALOG_LOC_Y = "_y"; // NOT LOCALIZABLE, property key
    private static final String DIALOG_LOC_WIDTH = "_width"; // NOT LOCALIZABLE, property key
    private static final String DIALOG_LOC_HEIGHT = "_height"; // NOT LOCALIZABLE, property key

    private Vector toolBars = new Vector(15);
    private JPopupMenu popup = new JPopupMenu();
    private JMenuItem showAllToolBarItem = new JMenuItem(LocaleMgr.action
            .getString("showAllToolBar"));
    private JMenuItem hideAllToolBarItem = new JMenuItem(LocaleMgr.action
            .getString("hideAllToolBar"));
    private JMenuItem selectToolBarItem = new JMenuItem(LocaleMgr.action
            .getString("SelectToolBars_"));
    private PopupMouseListener popupListener = new PopupMouseListener();

    private Vector dialogToolBars = new Vector(5); // contains toolbars dialogs

    private static final ToolBarManager singleton;

    static {
        singleton = new ToolBarManager();
    }

    public ToolBarManager() {
        hideAllToolBarItem.setMnemonic(LocaleMgr.action.getMnemonic("hideAllToolBar"));
        hideAllToolBarItem.addActionListener(this);
        showAllToolBarItem.setMnemonic(LocaleMgr.action.getMnemonic("showAllToolBar"));
        showAllToolBarItem.addActionListener(this);
        selectToolBarItem.addActionListener(this);
    }

    public static final void registerForPopupMenu(JComponent comp) {
        comp.addMouseListener(singleton.popupListener);
    }

    /**
     * 
     * @param installLocation
     *            : INSTALL_IN_DEFAULT_CONTAINER or INSTALL_IN_INTERNAL_FRAME
     * @param toolBar
     * @param key
     */
    public static final void installToolBarComponent(int installLocation, JToolBar toolBar,
            String key) {
        if ((key != null) && (toolBar != null)) {
            if (!PropertiesSet.isValidKey(ToolBarPanel.class, key)) { // valid key for preferences?
                org.modelsphere.jack.debug.Debug
                        .assert2(false,
                                "Invalid ToolBar Key (Remove spaces or '//').  Preferences for this toolbar will not be saved.");
            }
            String toolBarName = toolBar.getName();
            if ((toolBarName == null) || toolBarName.equals(""))
                toolBarName = new String("NONAME"); //NOT LOCALIZABLE
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(toolBarName, toolBar.isVisible());
            singleton.toolBars.addElement(new PopupItem(toolBar, item, key));
            toolBar.addMouseListener(singleton.popupListener);
            if (installLocation == INSTALL_IN_DIALOG) {
                ToolBarDialog dialog = new ToolBarDialog(ApplicationContext.getDefaultMainFrame(),
                        toolBar);
                singleton.dialogToolBars.add(toolBar);
                dialog.addComponentListener(new ComponentAdapter() {
                    public void componentHidden(ComponentEvent e) {
                        ToolBarDialog dialog = (ToolBarDialog) e.getComponent();
                        PopupItem item = ToolBarManager.findItem(dialog.toolbar);
                        item.setToolBarVisible(false, true);
                    }
                });
            } else {
                ApplicationContext.getDefaultMainFrame().getToolBarPanel().add(toolBar);
                String propertyName = key + VISIBLE_PROPERTY_EXTENTION;
                PropertiesSet propSet = PropertiesManager.APPLICATION_PROPERTIES_SET;
                if (propSet != null) {
                    singleton.setToolBarVisible(key, propSet.getPropertyBoolean(ToolBarPanel.class,
                            propertyName, Boolean.TRUE).booleanValue());
                } //end if
            } //end if
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == hideAllToolBarItem) {
            setToolBarsVisible(false);
        } else if (e.getSource() == showAllToolBarItem) {
            setToolBarsVisible(true);
        } else if (e.getSource() == selectToolBarItem) {
            int count = toolBars.size();
            SelectDialogItem[] selectItems = new SelectDialogItem[count];
            for (int i = 0; i < count; i++) {
                PopupItem item = (PopupItem) toolBars.get(i);
                JComponent tb = item.toolBar;
                selectItems[i] = new SelectDialogItem(tb.getName(), null,
                        isToolBarVisible(item.key), item.key);
            }
            int result = SelectDialog.select(ApplicationContext.getDefaultMainFrame(),
                    LocaleMgr.screen.getString("SelectToolBars"), selectItems,
                    SelectDialog.OPTION_SORT_ITEMS);
            if (result == SelectDialog.ACTION_OK) {
                for (int i = 0; i < selectItems.length; i++) {
                    setToolBarVisible((String) selectItems[i].object, selectItems[i].selected);
                }
            }
        }
    }

    private static final int MIN_WIDTH = 20;
    private static final int MIN_HEIGHT = 20;

    public static final void initDialogToolBars() {
        /*
         * We cannot use desktop pane location to position the dialogs on screen ... the returned
         * Point is not the correct position on screen! (as of 1.4.01) JDesktopPane desktop =
         * ApplicationContext.getDefaultMainFrame().getJDesktopPane(); Point refLoc =
         * desktop.getLocationOnScreen();
         */
        ToolBarPanel toolbarpanel = ApplicationContext.getDefaultMainFrame().getToolBarPanel();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point refLoc = null;
        Dimension adjSize = toolbarpanel.getSize();
        if (toolbarpanel.isShowing())
            refLoc = toolbarpanel.getLocationOnScreen();
        else
            refLoc = ApplicationContext.getDefaultMainFrame().getLocationOnScreen();

        Iterator iter = singleton.dialogToolBars.iterator();
        int deltax = 0;
        int deltay = 0;
        while (iter.hasNext()) {
            JToolBar toolbar = (JToolBar) iter.next();
            PopupItem item = findItem(toolbar);
            JDialog diag = (JDialog) SwingUtilities.getAncestorOfClass(JDialog.class, toolbar);
            PropertiesSet propSet = PropertiesManager.APPLICATION_PROPERTIES_SET;

            Dimension defaultSize = diag.getPreferredSize();
            int x = propSet.getPropertyInteger(ToolBarManager.class, item.key + DIALOG_LOC_X,
                    new Integer(refLoc.x + deltax + (adjSize.width * 2 / 3))).intValue();
            int y = propSet.getPropertyInteger(ToolBarManager.class, item.key + DIALOG_LOC_Y,
                    new Integer(refLoc.y + deltay + (adjSize.height + 10))).intValue();
            int w = propSet.getPropertyInteger(ToolBarManager.class, item.key + DIALOG_LOC_WIDTH,
                    new Integer(defaultSize.width)).intValue();
            int h = propSet.getPropertyInteger(ToolBarManager.class, item.key + DIALOG_LOC_HEIGHT,
                    new Integer(defaultSize.height)).intValue();
            w = Math.max(w, MIN_WIDTH);
            h = Math.max(h, MIN_HEIGHT);

            if (x < 0 || (x + w) > screenSize.width) {
                x = screenSize.width - w;
            }
            if (y < 0 || (y + h) > screenSize.height) {
                y = screenSize.height - h;
            }

            diag.setSize(w, h);
            diag.setLocation(x, y);

            String propertyName = item.key + VISIBLE_PROPERTY_EXTENTION;
            boolean visible = propSet.getPropertyBoolean(ToolBarPanel.class, propertyName,
                    Boolean.FALSE).booleanValue();
            singleton.setToolBarVisible(item.key, visible);
            if (diag != null)
                diag.setVisible(visible);

            deltax += 20;
            deltay += 20;
            diag.addComponentListener(singleton); // add this for updating properties on user move or resize
        }
    }

    public void updateProperties(Component component) {
        Iterator iter = dialogToolBars.iterator();
        while (iter.hasNext()) {
            JToolBar tb = (JToolBar) iter.next();
            JDialog diag = (JDialog) SwingUtilities.getAncestorOfClass(JDialog.class, tb);
            if (diag != component)
                continue;
            if (diag == null)
                continue;
            PopupItem item = findItem(tb);
            if (item == null)
                continue;
            PropertiesSet propSet = PropertiesManager.APPLICATION_PROPERTIES_SET;
            Point loc = diag.getLocationOnScreen();
            Dimension dim = diag.getSize();
            propSet.setProperty(ToolBarManager.class, item.key + DIALOG_LOC_X, loc.x);
            propSet.setProperty(ToolBarManager.class, item.key + DIALOG_LOC_Y, loc.y);
            propSet.setProperty(ToolBarManager.class, item.key + DIALOG_LOC_WIDTH, dim.width);
            propSet.setProperty(ToolBarManager.class, item.key + DIALOG_LOC_HEIGHT, dim.height);
        }
    }

    private void saveToolBarFloatingState(JToolBar tb) {
        if (!tb.isVisible())
            return;
        PopupItem item = findItem(tb);
        if (item == null)
            return;
        ToolBarUI ui = tb.getUI();
        if (!(ui instanceof BasicToolBarUI))
            return;
        boolean floating = ((BasicToolBarUI) ui).isFloating();
        PropertiesManager.APPLICATION_PROPERTIES_SET.setProperty(ToolBarPanel.class, item.key
                + FLOATING_PROPERTY_EXTENTION, floating);
        if (!floating)
            return;
        //Point loc = tb.getLocationOnScreen();
        Point loc = tb.getParent().getLocationOnScreen();
        System.out.println(loc);
        //SwingUtilities.convertPointToScreen(loc, tb);
        //SwingUtilities.convertPointToScreen(loc, ApplicationContext.getDefaultMainFrame().getToolBarPanel()); //tb.getLocation();
        PropertiesManager.APPLICATION_PROPERTIES_SET.setProperty(ToolBarPanel.class, item.key
                + FLOATING_LOC_X_PROPERTY_EXTENTION, loc.x);
        PropertiesManager.APPLICATION_PROPERTIES_SET.setProperty(ToolBarPanel.class, item.key
                + FLOATING_LOC_Y_PROPERTY_EXTENTION, loc.y);
    }

    public static final void restoreFloatingState() {
        PropertiesSet propSet = PropertiesManager.APPLICATION_PROPERTIES_SET;
        for (int i = 0; i < singleton.toolBars.size(); i++) {
            PopupItem item = (PopupItem) (singleton.toolBars.elementAt(i));
            if (!(item.toolBar instanceof JToolBar))
                continue;
            final JToolBar toolBar = (JToolBar) item.toolBar;
            if (toolBar == null)
                continue;
            if (item.isToolBarVisible()) {
                boolean floating = propSet.getPropertyBoolean(ToolBarPanel.class,
                        item.key + FLOATING_PROPERTY_EXTENTION, Boolean.FALSE).booleanValue();
                if (floating && toolBar.isFloatable()) {
                    final ToolBarUI ui = toolBar.getUI();
                    if (!(ui instanceof BasicToolBarUI))
                        return;
                    Point p = toolBar.getLocation();
                    int x = propSet.getPropertyInteger(ToolBarPanel.class,
                            item.key + FLOATING_LOC_X_PROPERTY_EXTENTION, new Integer(p.x + 20))
                            .intValue();
                    int y = propSet.getPropertyInteger(ToolBarPanel.class,
                            item.key + FLOATING_LOC_Y_PROPERTY_EXTENTION, new Integer(p.y + 40))
                            .intValue();
                    Point p2 = new Point(x, y);
                    final Point p3 = new Point(p2);
                    /*
                     * toolBar.addMouseMotionListener(new MouseMotionListener(){ public void
                     * mouseDragged(MouseEvent e){ System.out.println(e.getPoint());
                     * System.out.println(e.getClickCount()); System.out.println(e.getModifiers());
                     * }
                     * 
                     * public void mouseMoved(MouseEvent e){ } });
                     */
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            // simulate a user dragging event
                            //SwingUtilities.convertPointFromScreen(p3, ApplicationContext.getDefaultMainFrame().getToolBarPanel());
                            System.out.println(p3);
                            //toolBar.dispatchEvent(new MouseEvent(toolBar, MouseEvent.MOUSE_DRAGGED, 0, 16, p3.x, p3.y, 0, false));
                            //toolBar.dispatchEvent(new MouseEvent(toolBar, MouseEvent.MOUSE_RELEASED, 0, 16, p3.x, p3.y, 0, false));
                            Point p = toolBar.getLocationOnScreen();
                            Point parentp = toolBar.getParent().getLocationOnScreen();
                            Point loc = new Point(p3);
                            loc.translate(-p.x, -p.y);
                            //loc.translate(parentp.x, parentp.y);
                            ((BasicToolBarUI) ui).setFloatingLocation(loc.x, loc.y);
                            toolBar.dispatchEvent(new MouseEvent(toolBar, MouseEvent.MOUSE_DRAGGED,
                                    0, 16, loc.x, loc.y, 0, false));
                            toolBar.dispatchEvent(new MouseEvent(toolBar,
                                    MouseEvent.MOUSE_RELEASED, 0, 16, loc.x, loc.y, 0, false));
                            //((BasicToolBarUI)ui).setFloating(true, p3);
                            //((BasicToolBarUI)ui).setFloating(true, new Point(x,y));
                        }
                    });
                }
            }
            toolBar.addComponentListener(new ComponentListener() {
                public void componentResized(ComponentEvent e) {
                }

                public void componentMoved(ComponentEvent e) {
                    if (!(e.getComponent() instanceof JToolBar))
                        return;
                    JToolBar tb = (JToolBar) e.getComponent();
                    singleton.saveToolBarFloatingState(tb);
                }

                public void componentShown(ComponentEvent e) {
                }

                public void componentHidden(ComponentEvent e) {
                }
            });
        }
    }

    public static final JToolBar getInstallToolBar(String key) {
        PopupItem item = findItem(key);
        if (item != null)
            return (JToolBar) item.toolBar;
        else
            return null;
    }

    private static PopupItem findItem(String key) {
        PopupItem itemfound = null;
        for (int i = 0; i < singleton.toolBars.size(); i++) {
            PopupItem item = (PopupItem) (singleton.toolBars.elementAt(i));
            if (key.equals(item.key)) {
                itemfound = item;
                break;
            }
        }
        return itemfound;
    }

    private static PopupItem findItem(JToolBar tb) {
        PopupItem itemfound = null;
        for (int i = 0; i < singleton.toolBars.size(); i++) {
            PopupItem item = (PopupItem) (singleton.toolBars.elementAt(i));
            if (tb == item.toolBar) {
                itemfound = item;
                break;
            }
        }
        return itemfound;
    }

    public final void setToolBarVisible(String key, boolean b) {
        PopupItem item = findItem(key);
        org.modelsphere.jack.debug.Debug.assert2(item != null,
                "Invalid ToolBar key.  Method:  ToolBarPanel.setToolBarVisible()");
        if (item != null)
            item.setToolBarVisible(b, false);
    }

    public final boolean isToolBarVisible(String key) {
        PopupItem item = findItem(key);
        org.modelsphere.jack.debug.Debug.assert2(item != null,
                "Invalid ToolBar key.  Method:  ToolBarPanel.isToolBarVisible()");
        if (item != null)
            return item.isToolBarVisible();
        else
            return false;
    }

    public final void setToolBarsVisible(boolean b) {
        Enumeration enumeration = toolBars.elements();
        while (enumeration.hasMoreElements()) {
            Object o = enumeration.nextElement();
            if (o instanceof PopupItem) {
                ((PopupItem) o).setToolBarVisible(b, false);
            }
        }
    }

    /**
     * Use this method for adding to a JMenu the menu items for managing the toolbars Warning: Call
     * this method when MenuListener.menuSelected() is triggered.
     */
    public static void populateMenu(JMenu menu) {
        singleton.transferComponents(menu);
    }

    private void showPopup(Component c, int x, int y) {
        transferComponents(popup);
        popup.show(c, x, y);
    }

    ///////////////////////////////////////////////////////////////////////
    //  These next 2 methods must both be maintain.  The methods

    /**
     * Transfert the components to the popup menu
     */
    private void transferComponents(JPopupMenu target) {
        target.removeAll();
        Enumeration enumeration = toolBars.elements();
        while (enumeration.hasMoreElements()) {
            Object o = enumeration.nextElement();
            if (o instanceof PopupItem) {
                PopupItem pop = (PopupItem) o;
                target.add(pop.item);
            }
        }
        target.addSeparator();
        target.add(showAllToolBarItem);
        target.add(hideAllToolBarItem);
        target.add(selectToolBarItem);
    }

    /**
     * Transfert the components to the menu
     */
    private void transferComponents(JMenu target) {
        target.removeAll();
        Enumeration enumeration = toolBars.elements();
        while (enumeration.hasMoreElements()) {
            Object o = enumeration.nextElement();
            if (o instanceof PopupItem) {
                PopupItem pop = (PopupItem) o;
                target.add(pop.item);
            }
        }
        target.addSeparator();
        target.add(showAllToolBarItem);
        target.add(hideAllToolBarItem);
        target.add(selectToolBarItem);
    }

    //
    ////////////////////////////////////////////////////////////////////////

    class PopupMouseListener extends MouseAdapter {
        // WE MUST LISTEN FOR BOTH MOUSE PRESSED AND MOUSE RELEASED IN ORDER TO BE PLATFORM INDEPENDANT
        // WHEN USING METHOD ISPOPUPTRIGGER()
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) {
                showPopup(e.getComponent(), e.getPoint().x, e.getPoint().y);
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                showPopup(e.getComponent(), e.getPoint().x, e.getPoint().y);
            }
        }
    }

    public void componentResized(ComponentEvent e) {
        if (!e.getComponent().isShowing())
            return;
        updateProperties(e.getComponent());
    }

    public void componentMoved(ComponentEvent e) {
        if (!e.getComponent().isShowing())
            return;
        updateProperties(e.getComponent());
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

}

/**
 * This class profides a simple way for managing a toolbar, its key and its corresponding menu item.
 */

class PopupItem implements ActionListener {
    JComponent toolBar;
    JCheckBoxMenuItem item;
    String key;

    PopupItem(JComponent p0, JCheckBoxMenuItem p1, String p2) {
        toolBar = p0;
        item = p1;
        key = p2;
        item.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        setToolBarVisible(item.isSelected(), false);
    }

    public void setToolBarVisible(final boolean visible, final boolean external) {
        // We must prevent dialogs to become visible before the mainframe
        if (!SwingUtilities.isEventDispatchThread()
                && SwingUtilities.getAncestorOfClass(JDialog.class, toolBar) != null) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    setToolBarVisible_(visible, external);
                }
            });
        } else
            setToolBarVisible_(visible, external);
    }

    private void setToolBarVisible_(boolean visible, boolean external) {
        // if external, proceed anyway to update item state (this may occur if an external event has trigger a hide component event (the x in
        // upper right dialog)
        if ((toolBar.isVisible() != visible) || external) {
            toolBar.setVisible(visible);
            item.setSelected(visible);
            PropertiesSet propSet = PropertiesManager.APPLICATION_PROPERTIES_SET;
            if (PropertiesSet.isValidKey(ToolBarPanel.class, key)) { // valid key for preferences?
                String propertyName = key + ToolBarManager.VISIBLE_PROPERTY_EXTENTION;
                propSet.setProperty(ToolBarPanel.class, propertyName, visible);
            }
        }
    }

    public boolean isToolBarVisible() {
        return toolBar.isVisible();
    }

}
