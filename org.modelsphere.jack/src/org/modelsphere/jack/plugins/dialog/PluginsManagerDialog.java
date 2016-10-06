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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.*;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.plugins.*;
import org.modelsphere.jack.srtool.ApplicationContext;

@SuppressWarnings("serial")
public class PluginsManagerDialog extends JDialog implements Comparator<PluginDescriptor> {
    private static final String TITLE = LocaleMgr.screen.getString("PluginManager");
    private static final String CLOSE = LocaleMgr.screen.getString("Close");
    private static final String DETAILS = LocaleMgr.screen.getString("Details_");
    private static final String PLUGINSENABLED = LocaleMgr.screen.getString("PluginsEnabled");
    private static final String RESTART = LocaleMgr.screen.getString("Restart");
    private static final String RESTART0 = LocaleMgr.screen.getString("Restart0");

    private PluginsList list;
    private JPanel restartPanel;
    private JPanel buttonPanel;
    private JScrollPane scrollPane;
    private JCheckBox pluginsEnabled;
    private JButton closeButton;

    private Boolean pluginEnabledInitialValue;

    private ArrayList<PluginDescriptor> removed = new ArrayList<PluginDescriptor>();
    private ArrayList<PluginDescriptor> added = new ArrayList<PluginDescriptor>();

    PluginListener pluginListener = new PluginListener() {

        @Override
        public void pluginAdded(PluginDescriptor pluginDescriptor) {
            added.add(pluginDescriptor);
            removed.remove(pluginDescriptor);

            list.add(pluginDescriptor);

            updateState();
        }

        @Override
        public void pluginRemoved(PluginDescriptor pluginDescriptor) {
            removed.add(pluginDescriptor);
            added.remove(pluginDescriptor);

            list.remove(pluginDescriptor);

            updateState();
        }
    };

    PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent e) {
            if (e.getPropertyName().equals("enabled")) {
                updateState();
            }
        }
    };

    public PluginsManagerDialog(java.util.List<PluginDescriptor> pluginDescriptors,
            PluginConfigurationHandler configurationHandler) {
        super(ApplicationContext.getDefaultMainFrame(), TITLE, true);

        init(configurationHandler);
        load(pluginDescriptors);

        updateState();

        setSize(AwtUtil.getBestDialogSize());
        setLocationRelativeTo(ApplicationContext.getDefaultMainFrame());

        PluginMgr.getSingleInstance().getPluginsRegistry().addPluginListener(pluginListener);
    }

    public void load(java.util.List<PluginDescriptor> pluginDescriptors) {
        
        //sort the plugins according their names
        Collections.sort(pluginDescriptors, this); 
        
        for (Iterator<PluginDescriptor> iterator = pluginDescriptors.iterator(); iterator.hasNext();) {
            PluginDescriptor pluginDescriptor = iterator.next();
            pluginDescriptor.addPropertyChangeListener(propertyChangeListener);
        }

        list.setPlugins(pluginDescriptors);

        pluginEnabledInitialValue = PluginsActivator.isPluginsEnabled();
        pluginsEnabled.setSelected(pluginEnabledInitialValue);
    }

    private void init(PluginConfigurationHandler configurationHandler) {
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        getContentPane().setLayout(new GridBagLayout());
        list = new PluginsList(configurationHandler);

        restartPanel = createRestartPanel();
        buttonPanel = new JPanel(new GridBagLayout());

        closeButton = new JButton(CLOSE);
        JButton addButton = new JButton(new AddPluginAction(configurationHandler));
        JButton detailsButton = new JButton(DETAILS);

        pluginsEnabled = new JCheckBox(PLUGINSENABLED);
        pluginsEnabled.setEnabled(true);

        scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        buttonPanel.add(detailsButton, new GridBagConstraints(0, 0, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        buttonPanel.add(addButton, new GridBagConstraints(1, 0, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 6, 0, 0), 0, 0));
        buttonPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(2, 0, 1, 1, 1, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0,
                0));
        buttonPanel.add(closeButton, new GridBagConstraints(3, 0, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 6, 0, 0), 0, 0));

        getContentPane().add(
                restartPanel,
                new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.WEST,
                        GridBagConstraints.HORIZONTAL, new Insets(12, 6, 0, 6), 0, 0));
        getContentPane().add(
                scrollPane,
                new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.WEST,
                        GridBagConstraints.BOTH, new Insets(12, 6, 6, 6), 0, 0));
        getContentPane().add(
                pluginsEnabled,
                new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.WEST,
                        GridBagConstraints.NONE, new Insets(0, 6, 6, 6), 0, 0));
        getContentPane().add(
                buttonPanel,
                new GridBagConstraints(0, 3, 1, 1, 1, 0, GridBagConstraints.WEST,
                        GridBagConstraints.HORIZONTAL, new Insets(6, 6, 6, 6), 0, 0));

        getRootPane().setDefaultButton(closeButton);

        closeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                close();
            }
        });

        detailsButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PluginMgr mgr = PluginMgr.getSingleInstance();
                mgr.showPluginsDialog(PluginsManagerDialog.this);
            }
        });

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }

            @Override
            public void windowOpened(WindowEvent e) {
                closeButton.requestFocus();
            }

        });

        pluginsEnabled.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                updateState();
            }
        });
    }

    private JPanel createRestartPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        JButton restartButton = new JButton(RESTART);
        JLabel restartLabel = new JLabel(MessageFormat.format(RESTART0,
                new Object[] { ApplicationContext.getApplicationName() }));

        Icon icon = UIManager.getIcon("OptionPane.informationIcon");
        if (icon instanceof ImageIcon) {
            Image image = ((ImageIcon) icon).getImage();
            if (image != null) {
                image = image.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                if (image != null)
                    icon = new ImageIcon(image);
            }
        }

        restartLabel.setIcon(icon);
        restartLabel.setIconTextGap(6);

        panel.add(restartLabel, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 6), 0, 0));
        panel.add(restartButton, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        restartButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO - this needs to be done once we get an uniform packaging
                // mechanism across all platforms
            }
        });

        restartButton.setVisible(false);
        return panel;
    }

    private void updateState() {
        boolean restart = list.changed()
                || pluginEnabledInitialValue != pluginsEnabled.isSelected();
        restart = restart || this.added.size() > 0 || this.removed.size() > 0;
        restartPanel.setVisible(restart);
    }

    private void reset() {
        list.setSelectedItem(null);
        scrollPane.getViewport().setViewPosition(new Point(0, 0));
    }

    private void close() {
        PluginsActivator.setPluginsEnabled(pluginsEnabled.isSelected());
        reset();
    }

    @Override
    public int compare(PluginDescriptor plugin1, PluginDescriptor plugin2) {
        String name1 = plugin1.getName(); 
        String name2 = plugin2.getName(); 
        int comparison = name1.compareTo(name2); 
        return comparison;
    }

}
