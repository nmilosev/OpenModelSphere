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

package org.modelsphere.jack.plugins.dialog;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import org.modelsphere.jack.plugins.PluginConfigurationHandler;
import org.modelsphere.jack.plugins.PluginDescriptor;
import org.modelsphere.jack.plugins.PluginListener;
import org.modelsphere.jack.plugins.dialog.PluginsListItem.STATE;

@SuppressWarnings("serial")
class PluginsList extends JComponent implements MouseListener, Scrollable {
    private PluginsListItem selectedItem;

    private class NavigationAction extends AbstractAction {
        private int increments;

        NavigationAction(int increments) {
            this.increments = increments;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (getComponentCount() == 0) {
                return;
            }
            if (selectedItem == null) {
                setSelectedItem((PluginsListItem) getComponent(0));
            } else {
                Component[] components = getComponents();
                int index = -1;
                for (int i = 0; i < components.length; i++) {
                    if (components[i] == selectedItem) {
                        index = i;
                        break;
                    }
                }
                index += increments;
                if (index < 0) {
                    setSelectedItem((PluginsListItem) components[0]);
                } else if (index > -1) {
                    if (index > components.length - 1)
                        index = components.length - 1;
                    setSelectedItem((PluginsListItem) getComponent(index));
                }
            }

        }

    }

    private Action upAction = new NavigationAction(-1);
    private Action downAction = new NavigationAction(1);
    private Action upPageAction = new NavigationAction(-5);
    private Action downPageAction = new NavigationAction(5);
    private Action homeAction = new NavigationAction(-1000000);
    private Action endAction = new NavigationAction(1000000);

    private ArrayList<PluginDescriptor> removed = new ArrayList<PluginDescriptor>();
    private ArrayList<PluginDescriptor> added = new ArrayList<PluginDescriptor>();

    PluginListener pluginListener = new PluginListener() {

        @Override
        public void pluginAdded(PluginDescriptor pluginDescriptor) {
            added.add(pluginDescriptor);
            removed.remove(pluginDescriptor);
        }

        @Override
        public void pluginRemoved(PluginDescriptor pluginDescriptor) {
            removed.add(pluginDescriptor);
            added.remove(pluginDescriptor);
        }
    };

    private PluginConfigurationHandler configurationHandler;

    PluginsList(PluginConfigurationHandler configurationHandler) {
        this.configurationHandler = configurationHandler;
        setLayout(null);

        // register keyboard navigation
        InputMap inputMap = getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        install(inputMap);
        inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        install(inputMap);

        getActionMap().put("Up", upAction);
        getActionMap().put("Down", downAction);
        getActionMap().put("PageUp", upPageAction);
        getActionMap().put("PageDown", downPageAction);
        getActionMap().put("Home", homeAction);
        getActionMap().put("End", endAction);
    }

    private void install(InputMap inputMap) {
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Up");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Down");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Down");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Up");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), "PageUp");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), "PageDown");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0), "Home");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0), "End");
    }

    @Override
    public void updateUI() {
        super.updateUI();
    }

    void setPlugins(java.util.List<PluginDescriptor> plugins) {
        removeAll();

        Collections.sort(plugins);

        for (PluginDescriptor plugin : plugins) {
            addImpl(plugin);
        }
    }

    void add(PluginDescriptor pluginDescriptor) {
        Component[] components = getComponents();
        PluginsListItem installedItem = null;
        for (int i = 0; i < components.length; i++) {
            PluginsListItem item = (PluginsListItem) components[i];
            if (item.getPluginDescriptor() == pluginDescriptor) {
                installedItem = item;
                break;
            }
        }
        if (installedItem == null) {
            installedItem = addImpl(pluginDescriptor);
            sort();
            setSelectedItem(installedItem);
        }
        installedItem.setPluginState(STATE.ADDED);
    }

    private void sort() {
        java.util.List<PluginDescriptor> pluginDescriptors = new ArrayList<PluginDescriptor>();
        Component[] components = getComponents();
        Map<PluginDescriptor, Component> componentsMap = new HashMap<PluginDescriptor, Component>();
        for (Component component : components) {
            PluginDescriptor pluginDescriptor = ((PluginsListItem) component).getPluginDescriptor();
            pluginDescriptors.add(pluginDescriptor);
            componentsMap.put(pluginDescriptor, component);
        }
        removeAll();
        Collections.sort(pluginDescriptors);
        for (PluginDescriptor plugin : pluginDescriptors) {
            add(componentsMap.get(plugin));
        }
    }

    private PluginsListItem addImpl(PluginDescriptor pluginDescriptor) {
        PluginsListItem item = new PluginsListItem(configurationHandler);
        item.setPluginDescriptor(pluginDescriptor);
        item.addMouseListener(this);
        add(item);
        return item;
    }

    void remove(PluginDescriptor pluginDescriptor) {
        Component[] components = getComponents();
        PluginsListItem installedItem = null;
        for (int i = 0; i < components.length; i++) {
            PluginsListItem item = (PluginsListItem) components[i];
            if (item.getPluginDescriptor() == pluginDescriptor) {
                installedItem = item;
                break;
            }
        }
        if (installedItem == null) {
            return;
        }
        installedItem.setPluginState(STATE.REMOVED);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        setSelectedItem((PluginsListItem) e.getSource());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    PluginsListItem getSelectedItem() {
        return selectedItem;
    }

    void setSelectedItem(PluginsListItem selectedItem) {
        if (this.selectedItem == selectedItem)
            return;
        if (this.selectedItem != null) {
            this.selectedItem.setSelected(false);
            invalidate();
        }
        this.selectedItem = selectedItem;
        if (this.selectedItem != null) {
            this.selectedItem.setSelected(true);
            invalidate();
            ((JViewport) getParent()).revalidate();
            if (getParent() instanceof JViewport) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        Rectangle visibleRectangle = ((JViewport) getParent()).getViewRect();
                        Rectangle bounds = PluginsList.this.selectedItem.getBounds();
                        if (!visibleRectangle.contains(bounds)) {
                            ((JViewport) getParent()).getParent().validate();
                            int y = 0;
                            if (bounds.y < visibleRectangle.y)
                                y = bounds.y;
                            else
                                y = bounds.y - visibleRectangle.height + bounds.height;
                            ((JViewport) getParent()).setViewPosition(new Point(0, y));
                        }
                    }
                });

            }
        }

    }

    @Override
    public Dimension getPreferredSize() {
        Component[] components = getComponents();
        int width = 0;
        int height = 0;
        for (Component component : components) {
            Dimension prefsize = component.getPreferredSize();
            height += prefsize.height;
            width = Math.max(prefsize.width, width);
        }

        return new Dimension(0, height);
    }

    @Override
    public void doLayout() {
        Component[] components = getComponents();
        int width = getWidth();
        int y = 0;
        for (Component component : components) {
            Dimension prefsize = component.getPreferredSize();
            component.setBounds(0, y, width, prefsize.height);
            y += prefsize.height;
        }
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        Component component = null;
        if (direction < 0) {
            component = getComponentAt(visibleRect.x, visibleRect.y);
        } else {
            component = getComponentAt(visibleRect.x, visibleRect.y + visibleRect.height);
        }
        if (component == null)
            return 0;
        Rectangle bounds = component.getBounds();
        int increment = 0;
        if (direction < 0) {
            increment = visibleRect.y - bounds.y;
        } else {
            increment = bounds.y + bounds.height - (visibleRect.y + visibleRect.height);
        }
        if (increment == 0) {
            component = getNextComponent(component, direction);
            bounds = component.getBounds();
            if (direction < 0) {
                increment = visibleRect.y - bounds.y;
            } else {
                increment = bounds.y + bounds.height - (visibleRect.y + visibleRect.height);
            }
        }
        return increment;
    }

    private Component getNextComponent(Component component, int direction) {
        if (getComponentCount() == 1)
            return component;
        int index = -1;
        Component[] components = getComponents();
        for (int i = 0; i < components.length; i++) {
            if (component == components[i]) {
                index = i;
                break;
            }
        }
        if (direction < -1)
            direction = -1;
        if (direction > 1)
            direction = 1;
        index += direction;
        if (index < 0 || index >= components.length) {
            return component;
        }
        return components[index];
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        Component component = null;
        if (direction < 0) {
            component = getComponentAt(visibleRect.x, visibleRect.y);
        } else {
            component = getComponentAt(visibleRect.x, visibleRect.y + visibleRect.height);
        }
        if (component == null)
            return 0;
        Rectangle bounds = component.getBounds();
        int increment = 0;
        if (direction < 0) {
            increment = visibleRect.y - bounds.y;
        } else {
            increment = bounds.y + bounds.height - (visibleRect.y + visibleRect.height);
        }
        if (increment == 0) {
            component = getNextComponent(component, direction);
            bounds = component.getBounds();
            if (direction < 0) {
                increment = visibleRect.y - bounds.y;
            } else {
                increment = bounds.y + bounds.height - (visibleRect.y + visibleRect.height);
            }
        }
        return increment;
    }

    boolean changed() {
        boolean changed = false;
        Component[] components = getComponents();
        for (int i = 0; i < components.length; i++) {
            if (((PluginsListItem) components[i]).changed()) {
                changed = true;
                break;
            }
        }
        return changed;
    }

}