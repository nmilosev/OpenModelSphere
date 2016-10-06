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

package org.modelsphere.jack.preference;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.io.IoUtil;
import org.modelsphere.jack.io.PathFile;
import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginMgr;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.ExceptionHandler;

public final class OptionDialog extends JDialog implements ActionListener {
    private static final String kPluginsOptionGroup = LocaleMgr.screen
            .getString("PluginsOptionGroup");

    private static String MESSAGE_RESTART_REQUIRED = LocaleMgr.screen
            .getString("RestartRequiredMessage");
    private static String TITLE_RESTART_REQUIRED = LocaleMgr.screen
            .getString("RestartRequiredTitle");

    private boolean bChangeRequiresRestart = false;
    private boolean bWasAppliedInvoked = false;

    private JPanel buttonPanel = new JPanel(new GridBagLayout());
    private JButton applyButton = new JButton(LocaleMgr.screen.getString("Apply"));
    private JButton closeButton = new JButton(LocaleMgr.screen.getString("Close"));
    private JButton resetButton = new JButton(LocaleMgr.screen.getString("Reset"));

    private JPanel optionPanelContainer = new JPanel(new BorderLayout());

    private RootOptionGroup rootOptionGroup = new RootOptionGroup();
    private JTree tree = new JTree();

    private HashMap optionPanelLoaded = new HashMap(); // The key is the option group.

    private JSplitPane splitPane = new JSplitPane() {
        public void updateUI() {
            super.updateUI();
            if (getLeftComponent() instanceof JScrollPane)
                ((JComponent) getLeftComponent()).updateUI();
            else
                return;
            if (getRightComponent() instanceof JComponent)
                ((JComponent) getRightComponent()).updateUI();
            if (rootOptionGroup == null || optionPanelLoaded == null)
                return;
            // update all options panel
            updateGroupUI(rootOptionGroup);
            if (buttonPanel != null) {
                buttonPanel.updateUI();
                applyButton.updateUI();
                closeButton.updateUI();
                applyButton.validate();
                closeButton.validate();
                AwtUtil.normalizeComponentDimension(new JButton[] { applyButton, closeButton });
                tree.setCellRenderer(new OptionTreeCellRenderer());
            }
        }

        private void updateGroupUI(OptionGroup group) {
            if (group == null)
                return;
            JComponent panel = (JComponent) optionPanelLoaded.get(group);
            if (panel != null && optionPanelContainer.getComponentCount() > 0
                    && panel != optionPanelContainer.getComponent(0))
                optionPanelLoaded.remove(group);
            OptionGroup[] subgroups = group.getOptionGroups();
            if (subgroups == null)
                return;
            for (int i = 0; i < subgroups.length; i++) {
                updateGroupUI(subgroups[i]);
            }
        }
    };

    private ArrayList unsavedProperties = new ArrayList();

    private PropertyChangeListener preferencesListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            String property = evt.getPropertyName();
            if (property == null)
                return;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    SwingUtilities.updateComponentTreeUI(OptionDialog.this);
                }
            });
        }
    };

    private class OptionTreeCellRenderer extends DefaultTreeCellRenderer {
        OptionTreeCellRenderer() {
        }

        public Icon getOpenIcon() {
            return null;
        }

        public Icon getClosedIcon() {
            return null;
        }

        public Icon getLeafIcon() {
            return null;
        }
    }

    public OptionDialog(Frame owner, String title) {
        super(owner, title, true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        initGUI();

        PropertiesSet applPref = PropertiesManager.APPLICATION_PROPERTIES_SET;
        applPref.addPropertyChangeListener(ApplicationContext.class,
                ApplicationContext.LF_PROPERTY, preferencesListener);
        applPref.addPropertyChangeListener(ApplicationContext.class,
                ApplicationContext.THEME_PROPERTY, preferencesListener);

        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath selpath = e.getPath();
                if (selpath == null)
                    return;
                OptionTreeNode treenode = (OptionTreeNode) selpath.getLastPathComponent();
                if (treenode == null)
                    return;
                OptionGroup group = (OptionGroup) treenode.getUserObject();
                if (group == null)
                    return;
                setOptionGroup(group);
            }
        });

        // tree configurations
        tree.setCellRenderer(new OptionTreeCellRenderer());

        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setRootVisible(false);
        tree.putClientProperty("JTree.lineStyle", "Angled"); //NOT LOCALIZABLE
        tree.setShowsRootHandles(true);
    }

    private static class RootOptionGroup extends OptionGroup {
        RootOptionGroup() {
            super(""); // never visible to user
        }

        protected OptionPanel getOptionPanel() {
            return null;
        }
    }

    private class OptionTreeNode extends DefaultMutableTreeNode {
        OptionTreeNode(OptionGroup group) {
            super(group);

            OptionGroup[] groups = group.getOptionGroups();
            for (int i = 0; i < groups.length; i++) {
                add(new OptionTreeNode(groups[i]));
            }
        }

    }

    private class OptionTreeModel extends DefaultTreeModel {
        OptionTreeModel(OptionTreeNode root) {
            super(root);

        }
    }

    private void setOptionGroup(OptionGroup optionGroup) {
        OptionPanel panel = null;
        if (optionGroup != null) {
            // get used panel for this optionGroup or get a new one from optionGroup
            panel = (OptionPanel) optionPanelLoaded.get(optionGroup);
            if (panel == null) {
                panel = optionGroup.createOptionPanel();
                if (panel != null) {
                    optionPanelLoaded.put(optionGroup, panel);
                    panel.init();
                    panel.setBorder(new TitledBorder(new EmptyBorder(12, 6, 6, 6), " "
                            + optionGroup.toString() + " ", TitledBorder.CENTER,
                            TitledBorder.BELOW_TOP));
                }
            }
        }

        optionPanelContainer.removeAll();
        if (panel != null) {
            optionPanelContainer.add(panel, BorderLayout.CENTER);
        }
        optionPanelContainer.revalidate();
        optionPanelContainer.repaint();

    }

    public void dispose() {
        super.dispose();
        PropertiesSet applPref = PropertiesManager.APPLICATION_PROPERTIES_SET;
        applPref.removePropertyChangeListener(ApplicationContext.class,
                ApplicationContext.LF_PROPERTY, preferencesListener);
        applPref.removePropertyChangeListener(ApplicationContext.class,
                ApplicationContext.THEME_PROPERTY, preferencesListener);
        if (bChangeRequiresRestart && bWasAppliedInvoked) {
            bChangeRequiresRestart = false;
            JOptionPane.showMessageDialog(this, MESSAGE_RESTART_REQUIRED, TITLE_RESTART_REQUIRED,
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void initGUI() {
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        JPanel marginPanel = new JPanel(new BorderLayout());
        marginPanel.setBorder(new EmptyBorder(12, 12, 0, 12));

        // add listeners
        applyButton.addActionListener(this);
        closeButton.addActionListener(this);
        resetButton.addActionListener(this);

        // init buttonPanel
        AwtUtil
                .normalizeComponentDimension(new JButton[] { resetButton, applyButton, closeButton });
        buttonPanel.add(resetButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(17, 12, 11, 0),
                0, 0));
        buttonPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(17, 12, 11, 11),
                0, 0));
        buttonPanel.add(applyButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(17, 12, 11, 0),
                0, 0));
        buttonPanel.add(closeButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(17, 6, 11, 11),
                0, 0));

        JScrollPane scrollPane = new JScrollPane(tree);
        splitPane.setLeftComponent(scrollPane);
        splitPane.setRightComponent(optionPanelContainer);

        marginPanel.add(splitPane, BorderLayout.CENTER);
        contentPane.add(marginPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        setSize(AwtUtil.getBestDialogSize());
        synchronized(getTreeLock()){
        	validateTree();			// This must be called in order to use setDividerLocation(x.xx)
        } 
        splitPane.setDividerLocation(0.25);

        applyButton.setEnabled(false);
    }

    public void addOptionGroup(OptionGroup group) {
        rootOptionGroup.addOptionGroup(group);
    }

    public void showOptions() {
        showOptions(0);
    }

    public void showOptions(int rowSelection) {

        // Insert plugin groups
        OptionGroup pluginGroup = new OptionGroup(kPluginsOptionGroup);
        PluginMgr mgr = PluginMgr.getSingleInstance();
        Map pluginsOptionsGroups = mgr.getPluginOptionGroups();
        Iterator iter = pluginsOptionsGroups.keySet().iterator();
        while (iter.hasNext()) {
            Plugin2 plugin = (Plugin2) iter.next();
            OptionGroup optiongroup = (OptionGroup) pluginsOptionsGroups.get(plugin);
            if (optiongroup != null) {
                pluginGroup.addOptionGroup(optiongroup);
            }
        }
        if (pluginGroup.getOptionGroups().length > 0)
            rootOptionGroup.addOptionGroup(pluginGroup);

        OptionTreeModel optModel = new OptionTreeModel(new OptionTreeNode(rootOptionGroup));
        tree.setModel(optModel);

        if (rowSelection == 14) { //grid options
            tree.setSelectionRow(3);
            TreePath selectionPath = tree.getSelectionPath();
            tree.setExpandsSelectedPaths(true);
            tree.expandPath(selectionPath);
            tree.setSelectionRow(7);
            tree.setExpandsSelectedPaths(false);
        } else
            tree.setSelectionRow(0);

        super.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton)
            dispose();
        else if (e.getSource() == applyButton)
            applyChanges();
        else if (e.getSource() == resetButton) {
            performReset();
        }
    }

    private void applyChanges() {
        Iterator iter = unsavedProperties.iterator();
        while (iter.hasNext()) {
            OptionUpdate update = (OptionUpdate) iter.next();
            if (update.value == null)
                continue;
            if (update.value instanceof String) {
                update.set.setProperty(update.cls, update.key, (String) update.value);
                //Cas spécial Pour ne faire le setLocale qu'au APPLY seulement
                if (update.key.equalsIgnoreCase("Language")) {
                    try {
                        if (update.value.toString().equalsIgnoreCase("fr"))
                            LocaleMgr.setLocaleInPreferences(Locale.FRENCH);
                        else
                            // par défaut : anglais
                            LocaleMgr.setLocaleInPreferences(Locale.ENGLISH);
                    } catch (IOException ex) {
                        ExceptionHandler.processUncatchedException(ApplicationContext
                                .getDefaultMainFrame(), ex);
                    }
                }
            } else if (update.value instanceof Integer)
                update.set.setProperty(update.cls, update.key, ((Integer) update.value).intValue());
            else if (update.value instanceof Boolean)
                update.set.setProperty(update.cls, update.key, ((Boolean) update.value)
                        .booleanValue());
            else if (update.value instanceof Long)
                update.set.setProperty(update.cls, update.key, ((Long) update.value).longValue());
            else if (update.value instanceof Float)
                update.set.setProperty(update.cls, update.key, ((Float) update.value).floatValue());

            bWasAppliedInvoked = true;
        }
        unsavedProperties.clear();
        applyButton.setEnabled(false);

        //notify OptionPanel
        Component[] components = optionPanelContainer.getComponents();
        for (int i = 0; i < components.length; i++) {
            Component c = components[i];
            if (c instanceof OptionPanel) {
                OptionPanel panel = (OptionPanel) c;
                panel.terminate();
            }
        } //end for
    } //end applyChanges

    public void terminate() {
    }

    final void optionChanged(PropertiesSet set, Class cls, String key, Object value) {
        optionChanged(set, cls.getName(), key, value);
    }

    final void optionChanged(PropertiesSet set, String cls, String key, Object value) {
        if (set == null || cls == null || key == null || value == null)
            return;
        OptionUpdate update = new OptionUpdate(set, cls, key, value);
        int index = unsavedProperties.indexOf(update);
        if (index == -1) {
            String oldvalue = set.getProperty(cls, key);
            if (!value.toString().equals(oldvalue))
                unsavedProperties.add(update);
        } else {
            update = (OptionUpdate) unsavedProperties.get(index);
            // if value equals initial value, remove the update object
            String oldvalue = update.set.getProperty(cls, key);
            if (value.toString().equals(oldvalue))
                unsavedProperties.remove(update);
            else
                update.value = value;
        }

        applyButton.setEnabled(unsavedProperties.size() > 0);

    }

    private void performReset() {
        int result = JOptionPane.showConfirmDialog(this, LocaleMgr.message
                .getString("ResetConfirm")
                + "\n ", ApplicationContext.getApplicationName(), JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (result != JOptionPane.YES_OPTION)
            return;

        //remame folder properties to properties~
        String propertiesFolder = ApplicationContext.getPropertiesFolderPath();
        File file = new File(propertiesFolder);
        if (file.exists()) {
            String newName = file.getName() + PathFile.BACKUP_EXTENSION;
            File newFile = new File(file.getParent(), newName);
            if (newFile.exists()) {
                IoUtil.deleteRecusively(newFile);
            }

            file.renameTo(newFile);

            JOptionPane.showMessageDialog(this, MessageFormat.format(LocaleMgr.message
                    .getString("ResetToApply0"),
                    new Object[] { ApplicationContext.getApplicationName() }),
                    ApplicationContext.getApplicationName(), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class OptionUpdate {
        PropertiesSet set;
        String cls;
        String key;
        Object value;

        OptionUpdate(PropertiesSet set, String cls, String key, Object value) {
            this.set = set;
            this.cls = cls;
            this.key = key;
            this.value = value;
        }

        public boolean equals(Object o) {
            if (o == null || !(o instanceof OptionUpdate))
                return false;
            if (o == this)
                return true;
            OptionUpdate update = (OptionUpdate) o;
            if (set == update.set && cls.equals(update.cls) && key.equals(update.key))
                return true;
            return false;
        }

    }

    /**
     * @param changeRequiresRestart
     *            The bChangeRequiresRestart to set.
     */
    void setBChangeRequiresRestart(boolean changeRequiresRestart) {
        bChangeRequiresRestart = changeRequiresRestart;
    }
}
