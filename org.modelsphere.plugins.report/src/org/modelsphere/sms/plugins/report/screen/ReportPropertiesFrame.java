/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.plugins.report.screen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.TreeSelectionModel;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.awt.HotKeysSupport;
import org.modelsphere.jack.awt.tree.CheckTreeModel;
import org.modelsphere.jack.awt.tree.CheckTreeNode;
import org.modelsphere.jack.awt.tree.CheckableTree;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.plugins.report.LocaleMgr;
import org.modelsphere.sms.plugins.report.PropertiesSet;
import org.modelsphere.sms.plugins.report.model.Properties;
import org.modelsphere.sms.plugins.report.model.PropertyGroup;
import org.modelsphere.sms.plugins.report.model.ReportModel;

public class ReportPropertiesFrame extends JDialog implements TreeSelectionListener, ActionListener {
    public static final int CLOSE = 0;
    public static final int GENERATE_REPORT = 1;

    public int returnCode = CLOSE;
    public static final ExtensionFileFilter propertiesFileFilter = new ExtensionFileFilter(
            "properties", LocaleMgr.misc.getString("propertiesExtDesc")); //NOT LOCALIZABLE

    // Panels
    private JPanel containerPanel = new JPanel();
    private JPanel tabsPanel = new JPanel();
    private JPanel tablePropertiesPanel = new JPanel();
    private JPanel columnPropertiesPanel = new JPanel();
    private JPanel controlBtnPanel = new JPanel(new GridBagLayout());
    private JPanel tableColorsPanel = new JPanel();
    private JPanel tableDimensionsPanel = new JPanel();
    private JPanel tableUnitsPanel = new JPanel();
    private JPanel columnDimensionsPanel = new JPanel();
    private JPanel columnColorsPanel = new JPanel();
    private ReportOptionsPane optionsPane;

    private JScrollPane treeScrollPane = new JScrollPane();

    // Labels
    private JLabel tableHeaderColorLabel = new JLabel(LocaleMgr.misc.getString("Header")); // NOT LOCALIZABLE
    private JLabel tableBorderColorLabel = new JLabel(LocaleMgr.misc.getString("Border")); // NOT LOCALIZABLE
    private JLabel tableWidthLabel = new JLabel(LocaleMgr.misc.getString("Width")); // NOT LOCALIZABLE
    private JLabel tableHeightLabel = new JLabel(LocaleMgr.misc.getString("Height")); // NOT LOCALIZABLE
    private JLabel tableBorderLabel = new JLabel(LocaleMgr.misc.getString("Border")); // NOT LOCALIZABLE
    private JLabel tableCellSpacingLabel = new JLabel(LocaleMgr.misc.getString("CellSpacing")); // NOT LOCALIZABLE
    private JLabel tableCellPaddingLabel = new JLabel(LocaleMgr.misc.getString("CellPadding")); // NOT LOCALIZABLE
    private JLabel tableHorizontalMarginLabel = new JLabel(LocaleMgr.misc
            .getString("HorizontalMargin")); // NOT LOCALIZABLE
    private JLabel tableVerticalMarginLabel = new JLabel(LocaleMgr.misc.getString("VerticalMargin")); // NOT LOCALIZABLE
    private JLabel tableWidthUnitLabel = new JLabel(LocaleMgr.misc.getString("Width")); // NOT LOCALIZABLE
    private JLabel tableHeightUnitLabel = new JLabel(LocaleMgr.misc.getString("Height")); // NOT LOCALIZABLE

    // Buttons
    private JButton generateButton = new JButton(LocaleMgr.misc.getString("Generate"));// NOT LOCALIZABLE
    private JButton closeButton = new JButton(LocaleMgr.misc.getString("Close")); // NOT LOCALIZABLE
    private JButton loadButton = new JButton(LocaleMgr.misc.getString("Load"));// NOT LOCALIZABLE
    private JButton saveButton = new JButton(LocaleMgr.misc.getString("Save")); // NOT LOCALIZABLE

    // Layouts
    private CardLayout cardLayout = new CardLayout();

    // Borders
    private TitledBorder tableColorsBorder = new TitledBorder(LocaleMgr.misc
            .getString("TableColors")); // NOT LOCALIZABLE
    private TitledBorder tableDimensionsBorder = new TitledBorder(LocaleMgr.misc
            .getString("TableDimensions")); // NOT LOCALIZABLE
    private TitledBorder tableUnitsBorder = new TitledBorder(LocaleMgr.misc.getString("TableUnits")); // NOT LOCALIZABLE
    private TitledBorder columnColorsBorder = new TitledBorder(LocaleMgr.misc
            .getString("ColumnColors")); // NOT LOCALIZABLE

    BorderLayout borderLayout1 = new BorderLayout();
    private JDesktopPane desktopPane = new JDesktopPane();

    private ReportModel m_reportModel;

    public ReportPropertiesFrame(Frame owner, boolean modal, ReportModel reportModel) {
        super(owner, modal);
        m_reportModel = reportModel;

        this.setTitle(LocaleMgr.misc.getString("ReportGenerationFrameTitle")); // NOT LOCALIZABLE
        jbInit();

    }

    public final ReportModel getReportModel() {
        return m_reportModel;
    }

    public void jbInit() {

        JTabbedPane tabbedPane = new JTabbedPane();
        treeScrollPane.setViewportView(createNewTree(m_reportModel.getTreeReprentation()));

        tableColorsPanel.setBorder(tableColorsBorder);
        tableDimensionsPanel.setBorder(tableDimensionsBorder);
        tableUnitsPanel.setBorder(tableUnitsBorder);
        columnColorsPanel.setBorder(columnColorsBorder);

        tabsPanel.setLayout(cardLayout);
        tabsPanel.add(tablePropertiesPanel, "TableProperties"); // NOT LOCALIZABLE
        tabsPanel.add(columnPropertiesPanel, "ColumnProperties"); // NOT LOCALIZABLE
        this.getContentPane().setLayout(new GridBagLayout());
        this.getContentPane().add(
                tabbedPane,
                new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                        GridBagConstraints.BOTH, new Insets(5, 12, 12, 12), 0, 0));

        this.getContentPane().add(
                controlBtnPanel,
                new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTHEAST,
                        GridBagConstraints.HORIZONTAL, new Insets(5, 12, 12, 12), 0, 0));

        tabbedPane.add(LocaleMgr.misc.getString("conceptTab"), containerPanel);
        optionsPane = new ReportOptionsPane(m_reportModel, this);
        tabbedPane.add(LocaleMgr.misc.getString("optionTab"), optionsPane);
        containerPanel.setLayout(new GridBagLayout());
        treeScrollPane.setMinimumSize(new Dimension(130, 100));
        treeScrollPane.setPreferredSize(new Dimension(130, 100));

        containerPanel
                .add(treeScrollPane, new GridBagConstraints(0, 0, 1, 1, 0.3, 1.0,
                        GridBagConstraints.WEST, GridBagConstraints.BOTH,
                        new Insets(12, 12, 12, 12), 0, 0));

        containerPanel.add(tabsPanel, new GridBagConstraints(1, 0, GridBagConstraints.REMAINDER, 1,
                0.7, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets(9, 0, 9, 12), 0, 0));

        // *************************************************************************
        // Table Properties
        tablePropertiesPanel.setLayout(new GridBagLayout());

        controlBtnPanel.add(loadButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
        controlBtnPanel.add(saveButton, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
        controlBtnPanel.add(generateButton, new GridBagConstraints(4, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
        controlBtnPanel.add(closeButton, new GridBagConstraints(5, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        loadButton.addActionListener(this);
        saveButton.addActionListener(this);
        generateButton.addActionListener(this);
        closeButton.addActionListener(this);

        getRootPane().setDefaultButton(generateButton);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        new HotKeysSupport(this, closeButton, null);
    }

    private JTable createNewTable(PropertyGroup group) {
        DefaultTableColumnModel tableColumnModel = new DefaultTableColumnModel();
        // set columns
        tableColumnModel.addColumn(new TableColumn(0));
        tableColumnModel.addColumn(new TableColumn(1));
        // set editors and renderers
        tableColumnModel.getColumn(0).setCellRenderer(new PropertiesRenderer());
        tableColumnModel.getColumn(1).setCellRenderer(
                tableColumnModel.getColumn(0).getCellRenderer());
        tableColumnModel.getColumn(1).setCellEditor(new PropertiesEditor(this));

        // create a new table (each time the user click on a different node)
        JTable table = new JTable(new PropertiesTableModel(group), tableColumnModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);
        int height = table.getRowHeight();
        table.setRowHeight(height + 2); // we must add extra space for editors (combo)
        table.setBackground(this.getBackground());
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setVisible(false);
        return table;
    }

    private JPanel createNewTablePanel(PropertyGroup group) {

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        panel.add(createNewTable(group), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 12, 0, 12), 0, 0));

        panel.setBorder(new TitledBorder(group.toString()));

        return panel;
    }

    private JTree createNewTree(CheckTreeModel treeModel) {

        CheckableTree tree = new CheckableTree(treeModel);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.addTreeSelectionListener(this);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        return tree;
    }

    private void saveProperties() {
        File currentDir = new File(MainFrame.getSingleton().getApplicationDirectory());
        //  JFileChooser fileChooser = new JFileChooser(currentDir);
        // fileChooser.setDialogTitle(LocaleMgr.misc.getString("SaveProperties"));
        // fileChooser.setMultiSelectionEnabled(false);

        // fileChooser.addChoosableFileFilter(propertiesFileFilter);
        // fileChooser.setFileFilter(propertiesFileFilter);
        //fileChooser.setDialogTitle(kExportToJPEG);

        //int returnValue          = fileChooser.showSaveDialog(this);
        AwtUtil.FileAndFilter selection = AwtUtil.showSaveAsDialog(this,
                LocaleMgr.misc.getString("SaveProperties"), 
                propertiesFileFilter, (ExtensionFileFilter[])null, currentDir);
        File chosenFile = selection.getFile(); 
        // File chosenFile = fileChooser.getSelectedFile();

        if (chosenFile != null) {
            // refresh ...
            m_reportModel = optionsPane.setOptions(m_reportModel);
            PropertiesSet set = new PropertiesSet(chosenFile);

            CheckableTree tree = (CheckableTree) treeScrollPane.getViewport().getView();
            update(tree, set);
            //tree.repaint();
            set.save();
        }
    }

    private void update(CheckableTree tree, PropertiesSet set) {
        CheckTreeNode root = (CheckTreeNode) tree.getModel().getRoot();

        Enumeration enumeration = root.preorderEnumeration();
        //enum.nextElement(); // skip root
        while (enumeration.hasMoreElements()) {
            PropertiesTreeNode node = (PropertiesTreeNode) enumeration.nextElement();
            node.update(set);
            /*
             * CheckTreeNode node = (CheckTreeNode)dbEnum.nextElement(); if(node.getUserObject()
             * instanceof Properties){ Properties properties = (Properties)node.getUserObject();
             * properties.update(set); }
             */
        }
    }

    private void openProperties() {
        File currentDir = new File(MainFrame.getSingleton().getApplicationDirectory());
        JFileChooser fileChooser = new JFileChooser(currentDir);
        fileChooser.setDialogTitle(LocaleMgr.misc.getString("LoadProperties"));
        fileChooser.setMultiSelectionEnabled(false);

        fileChooser.addChoosableFileFilter(propertiesFileFilter);
        fileChooser.setFileFilter(propertiesFileFilter);
        //fileChooser.setDialogTitle(kExportToJPEG);

        int returnValue = fileChooser.showOpenDialog(this);
        File chosenFile = fileChooser.getSelectedFile();

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            // refresh ...
            PropertiesSet set = new PropertiesSet(chosenFile);

            CheckableTree tree = (CheckableTree) treeScrollPane.getViewport().getView();
            refresh(tree, set);
            tree.repaint();

            if (tree.getSelectionPath() != null) {
                Object[] objects = tree.getSelectionPath().getPath();
                CheckTreeNode selectedNode = (CheckTreeNode) objects[objects.length - 1];
                // refresh tables (panel on the right)
                refreshPropertiesPanel(selectedNode);
            }
            optionsPane.refreshOptions(m_reportModel);
        }
    }

    private void refresh(CheckableTree tree, PropertiesSet set) {
        CheckTreeNode root = (CheckTreeNode) tree.getModel().getRoot();

        Enumeration enumeration = root.preorderEnumeration();
        //enum.nextElement(); // skip root
        while (enumeration.hasMoreElements()) {
            PropertiesTreeNode node = (PropertiesTreeNode) enumeration.nextElement();
            node.refresh(set);
            /*
             * CheckTreeNode node = (CheckTreeNode)dbEnum.nextElement(); if(node.getUserObject()
             * instanceof Properties){ Properties properties = (Properties)node.getUserObject();
             * properties.refresh(set); }
             */
        }
    }

    private void close() {
        returnCode = ReportPropertiesFrame.CLOSE;
        dispose();
    }

    private void generateHtmlFiles() {
        m_reportModel = optionsPane.setOptions(m_reportModel);
        returnCode = ReportPropertiesFrame.GENERATE_REPORT;
        dispose();
    }

    // ***************************************************************************
    // implements AtionListener
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == closeButton)
            close();
        else if (source == loadButton)
            openProperties();
        else if (source == saveButton)
            saveProperties();
        else if (source == generateButton)
            generateHtmlFiles();
    }

    // end - implements AtionListener
    // ***************************************************************************

    // ***************************************************************************
    // TreeSelectionListener implementation

    public void valueChanged(TreeSelectionEvent e) {
        if (e.getNewLeadSelectionPath() != null) {
            Object[] objects = e.getNewLeadSelectionPath().getPath();
            CheckTreeNode selectedNode = (CheckTreeNode) objects[objects.length - 1];

            // refresh table (panel on the right)
            //updateTables(selectedNode);

            refreshPropertiesPanel(selectedNode);
        }
    }

    // End - TreeSelectionListener implementation
    // ***************************************************************************

    private void refreshPropertiesPanel(CheckTreeNode node) {
        Properties properties = (Properties) node.getUserObject();

        PropertyGroup[] groups = properties.getProperties();

        tablePropertiesPanel.removeAll();

        for (int i = 0; i < groups.length; i++) {
            tablePropertiesPanel.add(createNewTablePanel(groups[i]), new GridBagConstraints(0, i,
                    1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
                            0, 0, 0), 0, 0));
        }

        tablePropertiesPanel.revalidate();
        tablePropertiesPanel.repaint();
    }
}
