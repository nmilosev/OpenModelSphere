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

package org.modelsphere.jack.srtool.integrate;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.table.*;
import javax.swing.tree.TreePath;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.HotKeysSupport;
import org.modelsphere.jack.awt.JTextAreaFix;
import org.modelsphere.jack.awt.ThinBevelBorder;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.integrate.IntegrateNode.IntegrateProperty;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;

public class IntegrateFrame extends JDialog implements ActionListener, TreeSelectionListener {
    private static final String kObjectsDiffer = LocaleMgr.screen.getString("objectsDiffer");
    private static final String kOnlyIn0 = LocaleMgr.screen.getString("onlyIn0");
    private static final String kNoDiff = LocaleMgr.screen.getString("NoDiff");

    private static final int ACT_DISPLAY_VALS = -1;

    private IntegrateModel integModel;
    private IntegrateView treeTable;
    private JTable propTable;
    private IntegrateNode currentNode = null;
    private Font propFont;
    private Font UDFFont;
    private JPanel containerPanel = new JPanel();
    private JPanel controlBtnPanel = new JPanel();
    private JButton integrateBtn = new JButton(); // text obtained from model
    private JButton cancelBtn = new JButton(LocaleMgr.screen.getString("cancel"));
    private JButton reportBtn = new JButton(LocaleMgr.screen.getString("report"));

    private JPanel ColorInfoPanel = new JPanel(new GridBagLayout());
    private JLabel greenLabel = new JLabel(); // text obtained from model
    private JLabel blueLabel = new JLabel(); // text obtained from model
    private JLabel redLabel = new JLabel(kObjectsDiffer);

    public IntegrateFrame(IntegrateModel model) {
        super(ApplicationContext.getDefaultMainFrame(), model.getFrameTitle(), true);
        integModel = model;
        model.setFrame(this);
        treeTable = new IntegrateView(model);
        treeTable.addTreeSelectionListener(this);
        propTable = new JTable(new PropertyModel());
        propTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        propTable.setGridColor(AwtUtil.darker(propTable.getBackground(), 0.9f));
        TableColumnModel columnModel = propTable.getColumnModel();
        columnModel.getColumn(IntegrateModel.PROP_NAME).setPreferredWidth(125);
        columnModel.getColumn(IntegrateModel.PROP_LEFT).setPreferredWidth(175);
        columnModel.getColumn(IntegrateModel.PROP_RIGHT).setPreferredWidth(175);
        columnModel.getColumn(IntegrateModel.PROP_ACTION).setPreferredWidth(125);
        propTable.getTableHeader().setUpdateTableInRealTime(false);
        propTable.getTableHeader().setReorderingAllowed(false);
        propTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        propTable.setDefaultRenderer(IntegrateProperty.class, new PropertyRenderer());
        propFont = propTable.getFont();
        UDFFont = propFont.deriveFont(Font.ITALIC);

        propTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    int iRow = propTable.rowAtPoint(evt.getPoint());
                    if (iRow == -1)
                        return;
                    if (!propTable.isRowSelected(iRow))
                        propTable.setRowSelectionInterval(iRow, iRow);
                    displayPopup(evt);
                }
            }

            public void mouseReleased(MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    int iRow = propTable.rowAtPoint(evt.getPoint());
                    if (iRow == -1)
                        return;
                    if (!propTable.isRowSelected(iRow))
                        propTable.setRowSelectionInterval(iRow, iRow);
                    displayPopup(evt);
                }
            }
        });

        treeTable.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
            }

            public void mouseMoved(MouseEvent e) {
                int row = treeTable.rowAtPoint(e.getPoint());
                TreePath path = treeTable.getTree().getPathForRow(row);
                if (path == null) {
                    treeTable.setToolTipText("");
                    return;
                }

                Object node = path.getLastPathComponent();
                if (node instanceof IntegrateNode) {
                    IntegrateNode integrateNode = (IntegrateNode) node;
                    int id = treeTable.getDifferenceId(integrateNode);
                    String tips = "";
                    String header = "";

                    switch (treeTable.getDifferenceId(integrateNode)) {
                    case IntegrateView.ONLY_IN_LEFT:
                        header = treeTable.getColumnName(IntegrateModel.COL_LEFT);
                        tips = MessageFormat.format(kOnlyIn0, new Object[] { header });
                        break;
                    case IntegrateView.ONLY_IN_RIGHT:
                        header = treeTable.getColumnName(IntegrateModel.COL_RIGHT);
                        tips = MessageFormat.format(kOnlyIn0, new Object[] { header });
                        break;
                    case IntegrateView.OBJECTS_DIFFERS:
                        tips = kObjectsDiffer;
                        break;
                    default:
                        tips = kNoDiff;
                    }
                    treeTable.setToolTipText(tips);
                } else
                    treeTable.setToolTipText("");
            }

        });

        jbInit();
        // this.pack();
        setSize(1100, 500);
        this.setLocationRelativeTo(ApplicationContext.getDefaultMainFrame());
    }

    void jbInit() {
        containerPanel.setLayout(new GridBagLayout());
        controlBtnPanel.setLayout(new GridBagLayout());

        blueLabel.setBorder(new ThinBevelBorder(ThinBevelBorder.LOWERED));
        greenLabel.setBorder(new ThinBevelBorder(ThinBevelBorder.LOWERED));
        redLabel.setBorder(new ThinBevelBorder(ThinBevelBorder.LOWERED));

        getContentPane().add(containerPanel);

        // Main Panel
        JSplitPane mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(treeTable), new JScrollPane(propTable));
        Dimension size = new Dimension(800, 400);
        mainPanel.setPreferredSize(size);
        mainPanel.setDividerLocation(size.height * 3 / 4);
        containerPanel.add(mainPanel,
                new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0, GridBagConstraints.NORTH,
                        GridBagConstraints.BOTH, new Insets(12, 12, 12, 12), 0, 0));

        // Color Info Panel
        blueLabel.setText(MessageFormat.format(kOnlyIn0,
                new Object[] { integModel.getColHeaders()[IntegrateModel.COL_LEFT] }));
        blueLabel.setForeground(Color.blue);
        blueLabel.setFont(propFont);
        greenLabel.setText(MessageFormat.format(kOnlyIn0,
                new Object[] { integModel.getColHeaders()[IntegrateModel.COL_RIGHT] }));
        greenLabel.setForeground(new Color(0, 128, 0));// dark green
        greenLabel.setFont(propFont);
        redLabel.setForeground(Color.red);
        redLabel.setFont(propFont);

        greenLabel.setHorizontalAlignment(SwingConstants.CENTER);
        blueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        redLabel.setHorizontalAlignment(SwingConstants.CENTER);

        ColorInfoPanel.add(blueLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 6), 6,
                2));
        ColorInfoPanel.add(greenLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 6), 6,
                2));
        ColorInfoPanel.add(redLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 6,
                2));
        containerPanel.add(ColorInfoPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 12, 12, 12), 0, 0));

        // Control Button Panel
        integrateBtn.setText(integModel.getIntegButtonName());
        controlBtnPanel.add(integrateBtn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 6), 0, 0));
        controlBtnPanel.add(reportBtn, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 6), 0, 0));
        controlBtnPanel.add(cancelBtn, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        AwtUtil.normalizeComponentDimension(new JButton[] { cancelBtn, reportBtn, integrateBtn });

        integrateBtn.addActionListener(this);
        reportBtn.addActionListener(this);
        cancelBtn.addActionListener(this);
        containerPanel.add(controlBtnPanel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 12, 12), 0, 0));

        getRootPane().setDefaultButton(integrateBtn);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // new HotKeysSupport(this, cancelButton, helpButton);
    }

    public final IntegrateView getTreeTable() {
        return treeTable;
    }

    public final void refreshProperties(IntegrateNode node) {
        if (node == null || node == currentNode)
            ((PropertyModel) propTable.getModel()).fireTableDataChanged();
    }

    private class PropertyModel extends AbstractTableModel {
        PropertyModel() {
        }

        public String getColumnName(int col) {
            return integModel.getPropHeaders()[col];
        }

        public Class getColumnClass(int col) {
            return IntegrateProperty.class;
        }

        public boolean isCellEditable(int row, int col) {
            return false;
        }

        public int getColumnCount() {
            return 4;
        }

        public int getRowCount() {
            if (currentNode == null)
                return 0;
            IntegrateProperty[] props = currentNode.getProperties();
            return (props == null ? 0 : props.length);
        }

        public Object getValueAt(int row, int col) {
            return currentNode.getProperties()[row];
        }
    } // End of class PropertyModel

    private class PropertyRenderer extends DefaultTableCellRenderer {
        PropertyRenderer() {
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            IntegrateProperty prop = (IntegrateProperty) value;
            Font font = propFont;
            switch (column) {
            case IntegrateModel.PROP_NAME:
                setText(prop.getName());
                setToolTipText(null);
                if (prop.getProperty() instanceof DbUDF)
                    font = UDFFont;
                break;
            case IntegrateModel.PROP_LEFT:
                setText(prop.getLeftVal());
                setToolTipText(prop.getLeftTip());
                break;
            case IntegrateModel.PROP_RIGHT:
                setText(prop.getRightVal());
                setToolTipText(prop.getRightTip());
                break;
            case IntegrateModel.PROP_ACTION:
                String text = (prop.getAction() == IntegrateModel.ACT_NONE ? "" : integModel
                        .getActionNames()[prop.getAction()]);
                setText(text);
                setToolTipText(null);
                break;
            }
            setFont(font);
            return this;
        }
    } // End of class PropertyRenderer

    public final void actionPerformed(ActionEvent e) {
        if (e.getSource() == integrateBtn) {
            integModel.integrate();
            dispose();
        } else if (e.getSource() == reportBtn)
            integModel.doReport();
        else if (e.getSource() == cancelBtn)
            dispose();
    }

    // ////////////////////////////////////
    // TreeSelectionListener SUPPORT
    //
    public final void valueChanged(TreeSelectionEvent e) {
        IntegrateNode[] nodes = treeTable.getSelectedNodes();
        IntegrateNode node = (nodes.length == 1 ? nodes[0] : null);
        if (currentNode != node) {
            currentNode = node;
            ((PropertyModel) propTable.getModel()).fireTableDataChanged();
        }
    }

    //
    // End of TreeSelectionListener SUPPORT
    // ////////////////////////////////////

    private void displayPopup(MouseEvent evt) {
        int[] selRows = propTable.getSelectedRows();
        int[] actions = IntegrateModel.propertyActions;
        JPopupMenu popup = new JPopupMenu();
        JMenu actionMenu = new JMenu(LocaleMgr.screen.getString("action"));
        popup.add(actionMenu);
        for (int i = 0; i < actions.length; i++) {
            actionMenu.add(new PopupItem(integModel.getActionNames()[actions[i]], actions[i],
                    selRows));
        }
        PopupItem displayItem = new PopupItem(LocaleMgr.screen.getString("displayValuesDots"),
                ACT_DISPLAY_VALS, selRows);
        displayItem.setEnabled(selRows.length == 1);
        popup.add(displayItem);
        popup.show(propTable, evt.getX(), evt.getY());
    }

    private class PopupItem extends JMenuItem implements ActionListener {
        private int[] selRows;
        private int action;

        PopupItem(String name, int action, int[] selRows) {
            super(name);
            this.action = action;
            this.selRows = selRows;
            addActionListener(this);
        }

        public final void actionPerformed(ActionEvent e) {
            IntegrateProperty[] props = currentNode.getProperties();
            if (action == ACT_DISPLAY_VALS) {
                IntegrateProperty prop = props[selRows[0]];
                String title = MessageFormat.format(LocaleMgr.screen.getString("property0"),
                        new Object[] { prop.getName() });
                DisplayValsDialog displayDialog = new DisplayValsDialog(IntegrateFrame.this, title,
                        integModel.getPropHeaders()[IntegrateModel.PROP_LEFT], prop.getLeftVal(),
                        integModel.getPropHeaders()[IntegrateModel.PROP_RIGHT], prop.getRightVal());
                displayDialog.setVisible(true);
                getLayeredPane().repaint(); // refresh bug when a modal dialog
                // is called from a popup
            } else {
                for (int i = 0; i < selRows.length; i++) {
                    props[selRows[i]].setAction(action);
                    ((PropertyModel) propTable.getModel()).fireTableCellUpdated(selRows[i],
                            IntegrateModel.PROP_ACTION);
                }
            }
        }
    } // End of class PopupItem

    private static class DisplayValsDialog extends JDialog implements ActionListener {

        private JPanel containerPanel = new JPanel();
        private JPanel controlBtnPanel = new JPanel();
        private JButton OkBtn = new JButton(LocaleMgr.screen.getString("OK"));
        private GridLayout gridLayout1 = new GridLayout();
        private String leftHdr, leftVal, rightHdr, rightVal;

        DisplayValsDialog(JDialog parent, String title, String aLeftHdr, String aLeftVal,
                String aRightHdr, String aRightVal) {
            super(parent, title, true);
            leftHdr = aLeftHdr;
            leftVal = aLeftVal;
            rightHdr = aRightHdr;
            rightVal = aRightVal;

            jbInit2();
            this.pack();
            this.setLocationRelativeTo(parent);
        }

        void jbInit2() {
            containerPanel.setLayout(new GridBagLayout());
            controlBtnPanel.setLayout(gridLayout1);
            gridLayout1.setHgap(5);
            getContentPane().add(containerPanel);

            JTextArea leftArea = new JTextAreaFix(leftVal);
            leftArea.setEditable(false);
            JPanel leftPanel = new JPanel(new BorderLayout());
            String leftHdrLabel = " ".concat(leftHdr);
            leftPanel.add(new JLabel(leftHdrLabel), BorderLayout.NORTH);
            leftPanel.add(new JScrollPane(leftArea), BorderLayout.CENTER);

            JTextArea rightArea = new JTextAreaFix(rightVal);
            rightArea.setEditable(false);
            JPanel rightPanel = new JPanel(new BorderLayout());
            String rightHdrLabel = " ".concat(rightHdr);
            rightPanel.add(new JLabel(rightHdrLabel), BorderLayout.NORTH);
            rightPanel.add(new JScrollPane(rightArea), BorderLayout.CENTER);

            // Main Panel
            JSplitPane mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, leftPanel, rightPanel);
            Dimension size = new Dimension(600, 400);
            mainPanel.setPreferredSize(size);
            mainPanel.setDividerLocation(size.height / 2);
            containerPanel.add(mainPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                    GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(12, 12, 12, 12),
                    0, 0));

            // Control Button Panel
            controlBtnPanel.add(OkBtn, null);
            containerPanel.add(controlBtnPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(10, 12, 12,
                            12), 0, 0));
            OkBtn.addActionListener(this);

            getRootPane().setDefaultButton(OkBtn);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            new HotKeysSupport(this, OkBtn, null);
        }

        public final void actionPerformed(ActionEvent e) {
            if (e.getSource() == OkBtn)
                dispose();
        }
    } // End of class DisplayValsDialog
}
