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

package org.modelsphere.jack.baseDb.screen;

import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.modelsphere.jack.baseDb.PackageProperties;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.baseDb.screen.model.*;
import org.modelsphere.jack.baseDb.screen.model.ListModel;
import org.modelsphere.jack.baseDb.screen.plugins.MultiDbSemanticalObjectRenderer;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.graphic.GraphicUtil;

public class ListView extends ScreenView {

    /**
     * approximative width of scrollpane by default used for recalibrate the column width or add
     * another column fName at the end of table. Best solution will follow...
     */
    private static final Icon ROWDRAG_ICON = GraphicUtil.loadIcon(PackageProperties.class,
            "international/resources/reorder.gif");

    private static final int TABLE_WIDTH = 600;

    private static final int CELL_MARGE = 6;
    private static final int REORDERRING_COLUMN_WIDTH = 20;
    private static final int COLUMN_WIDTHEST = 300;

    protected ListViewTable table = null;
    protected ListViewTable prefixTable = null;
    private boolean isPopupGesture;
    private int firstDraggedRow;
    private int nbDraggedRows;
    private int dropPos;
    private int totalColWidth = 0;

    private JLabel reorderIconRenderer = new JLabel();

    public ListView(ScreenContext screenContext) {
        super(screenContext);
    }

    // overriding methods must call super.setModel
    public void setModel(ListModel model) {

        if (model == null) {
            table = null;
            return;
        }
        JComboBox lazyCombo = new JComboBox();
        int rowheight = lazyCombo.getPreferredSize().height;

        if (table != null)
            deinstallPanel();
        initPanel();
        prefixTable = null;
        table = new ListViewTable(model);
        table.setName("List"); // NOT LOCALIZABLE - For QA / They need a fix
        // name
        table.setRowHeight(rowheight); // for comboBox of cellEditor
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableColumnModel columnModel = table.getColumnModel();
        totalColWidth = columnModel.getTotalColumnWidth();
        installEditorAndRenderer(model);
        adjustTableColumnWidth(model);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (getPropertiesFrame().getCurrentPanel() == ListView.this)
                    screenContext.selectionChanged(getPropertiesFrame());
            }
        });

        int nbFixedCols = model.getFixedColumnCount();
        if (nbFixedCols != 0) {
            prefixTable = new ListViewTable(model);
            prefixTable.setName("Prefix"); // NOT LOCALIZABLE - For QA / They
            // need a fix name
            prefixTable.setRowHeight(rowheight);// for comboBox of cellEditor
            installDragListeners();
            prefixTable.getTableHeader().setReorderingAllowed(false);
            prefixTable.getTableHeader().setResizingAllowed(false);

            reorderIconRenderer.setIcon(ROWDRAG_ICON);
            reorderIconRenderer.setText(null);
            reorderIconRenderer.setOpaque(false);
            reorderIconRenderer.setFocusable(false);

            prefixTable.getTableHeader().add(reorderIconRenderer);
            prefixTable.getTableHeader().addComponentListener(new ComponentAdapter() {

                @Override
                public void componentResized(ComponentEvent e) {
                    reorderIconRenderer.setVisible(((actionMode & REINSERT_ACTION) != 0));
                    Rectangle rect = prefixTable.getTableHeader().getHeaderRect(0);
                    rect.x += 1;
                    reorderIconRenderer.setBounds(rect);
                }
            });

            TableColumnModel prefixColumnModel = new DefaultTableColumnModel();
            for (int i = 0; i < nbFixedCols; i++) {
                TableColumn column = columnModel.getColumn(0);
                columnModel.removeColumn(column);
                prefixColumnModel.addColumn(column);
            }
            /** Adding another fName at the end of table */
            // ***BUG lorsqu'on déplace le duplicata, c'est l'original qui se
            // déplace (modelIndex !!)
            /*
             * if (totalColWidth > TABLE_WIDTH){ TableColumn duplicatedColumn =
             * columnModel.getColumn(0); columnModel.addColumn(duplicatedColumn); }
             */

            prefixTable.setColumnModel(prefixColumnModel);

            TableColumn column0 = prefixTable.getColumnModel().getColumn(0);
            if (column0 != null && ((actionMode & REINSERT_ACTION) != 0))
                column0.setPreferredWidth(REORDERRING_COLUMN_WIDTH);

            prefixTable.setPreferredScrollableViewportSize(prefixTable.getPreferredSize());
            prefixTable.setSelectionModel(table.getSelectionModel());
        }

        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.getHorizontalScrollBar().setName("HScrollBar"); // NOT
        // LOCALIZABLE
        // - For QA
        scrollpane.getVerticalScrollBar().setName("VScrollBar"); // NOT
        // LOCALIZABLE
        // - For QA
        if (prefixTable != null) {
            scrollpane.setRowHeaderView(prefixTable);
            scrollpane.setCorner(JScrollPane.UPPER_LEFT_CORNER, prefixTable.getTableHeader());
        }
        Container contentPanel = getContentPanel();
        contentPanel.setLayout(new BorderLayout());
        Object[] partitions = model.getPartitions();
        if (partitions == null) {
            contentPanel.add(scrollpane, BorderLayout.CENTER);
        } else {
            JList list = new JList(partitions);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            int index = model.getPartitionIndex();
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
            list.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    int index = ((JList) e.getSource()).getSelectedIndex();
                    ((ListModel) getModel()).setPartitionIndex(index);
                }
            });
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false,
                    new JScrollPane(list), scrollpane);
            // JDK 1.3: splitPane.setResizeWeight(0.25);
            // use this method until we are 1.3 minimum... !! setResizeWeight
            // does not work in 1.4rc!!!
            splitPane.setDividerLocation(175);

            contentPanel.add(splitPane, BorderLayout.CENTER);
        }
        contentPanel.validate();

        model.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (((ListModel) getModel()).hasChanged()) // user change, not
                    // db refresh
                    setHasChanged();
            }
        });
    }

    private void installDragListeners() {
        /*
         * Implementation of a drag operation allowing to move rows in a ListView. The
         * REINSERT_ACTION must be specified in the action mode parameter of the constructor, and
         * the <reinsertAction> method must be implemented in the ListView sub-class. The user must
         * initiate the drag in the selection column of the list with the right button pressed.
         * 
         * A right button click in the selection column also triggers the contextual popup menu.
         */
        firstDraggedRow = -1;
        prefixTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                isPopupGesture = false;
                if (!SwingUtilities.isRightMouseButton(evt))
                    return;
                /*
                 * if (prefixTable.columnAtPoint(evt.getPoint()) != 0) return;
                 */
                int iRow = prefixTable.rowAtPoint(evt.getPoint());
                if (iRow == -1)
                    return;
                isPopupGesture = true;
                if (!prefixTable.isRowSelected(iRow))
                    prefixTable.setRowSelectionInterval(iRow, iRow);

                if ((actionMode & REINSERT_ACTION) == 0)
                    return;
                int[] selRows = prefixTable.getSelectedRows();
                if (selRows[selRows.length - 1] == selRows[0] + selRows.length - 1) {
                    firstDraggedRow = selRows[0];
                    nbDraggedRows = selRows.length;
                } else
                    firstDraggedRow = -1;
                dropPos = -1;
            }

            public void mouseReleased(MouseEvent evt) {
                if (firstDraggedRow == -1)
                    return;
                refreshDropPos(evt.getPoint(), true);
                prefixTable.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                if (dropPos != -1)
                    reinsertAction(firstDraggedRow, nbDraggedRows, dropPos);
                firstDraggedRow = -1;
            }

            public void mouseClicked(MouseEvent evt) {
                if (isPopupGesture) {
                    JPopupMenu popup = screenContext.getPopupMenu();
                    if (popup != null)
                        popup.show(ListView.this, evt.getX(), evt.getY());
                }
            }
        });

        prefixTable.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent evt) {
                if (firstDraggedRow == -1)
                    return;
                refreshDropPos(evt.getPoint(), false);
                prefixTable.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                isPopupGesture = false;
                if (!SwingUtilities.isRightMouseButton(evt))
                    return;
                int iRow = table.rowAtPoint(evt.getPoint());
                if (iRow == -1)
                    return;
                table.setRowSelectionInterval(iRow, iRow);
                isPopupGesture = true;
            }

            public void mouseClicked(MouseEvent evt) {
                if (isPopupGesture) {
                    JPopupMenu popup = screenContext.getPopupMenu();
                    if (popup != null)
                        popup.show(ListView.this, evt.getX(), evt.getY());
                }
            }
        });

    }

    /*
     * Draw a small arrow head at the left of col 0, in the interspace between 2 rows, to indicate
     * the insertion point.
     */
    private void refreshDropPos(Point point, boolean end) {
        boolean scroll = false;
        int newDropPos = dropPos;
        int height = prefixTable.getRowHeight() + prefixTable.getRowMargin();
        if (!end) {
            Rectangle viewRect = ((JViewport) prefixTable.getParent()).getViewRect();
            if (point.y < viewRect.y || point.y > viewRect.y + viewRect.height) {
                scroll = true;
                newDropPos = -1;
            } else {
                newDropPos = (point.y + height / 2) / height;
                if (newDropPos > prefixTable.getRowCount())
                    newDropPos = prefixTable.getRowCount();
                if (newDropPos >= firstDraggedRow && newDropPos <= firstDraggedRow + nbDraggedRows)
                    newDropPos = -1; // no move, the rows remain at the same
                // position.
            }
        }
        if (newDropPos != dropPos || end) {
            Graphics g = prefixTable.getGraphics();
            if (g != null) {
                g.setColor(Color.black);
                g.setXORMode(Color.white);
                if (dropPos != -1)
                    drawDropPos(g, dropPos, height);
                if (newDropPos != -1 && !end)
                    drawDropPos(g, newDropPos, height);
                g.dispose();
            }
        }
        dropPos = newDropPos;
        if (scroll) { // scroll <table> instead of <prefixTable> because of a
            // bug in Swing
            int row = prefixTable.rowAtPoint(point);
            if (row != -1) {
                table.scrollRectToVisible(table.getCellRect(row, 0, false));
            }
        }
    }

    private void drawDropPos(Graphics g, int row, int height) {
        int y = row * height;
        g.fillPolygon(new int[] { 0, 0, 4 }, new int[] { y - 4, y + 4, y }, 3);
    }

    /**
     * must be called before splitting the TableColumnModel between the 2 JTables
     */
    private void installEditorAndRenderer(ListModel model) {
        TableColumnModel columnModel = table.getColumnModel();

        for (int i = columnModel.getColumnCount(); --i >= 0;) {
            TableColumn column = columnModel.getColumn(i);
            column.setCellEditor(new ListEditor(model.getEditorName(i)));

            Renderer renderer = model.getRenderer(0, i);

            column.setCellRenderer(new ListRenderer(renderer));
        }
    }

    /**
     * Adjusting table column depend on : 1- title in header 2- longer value of field (name firstly)
     * 3- maximum value allowed for a column (name firstly) 4- default value in model
     */
    private void adjustTableColumnWidth(ListModel model) {
        /** Adjusting all columns in the table if they don't fill it */
        if ((TABLE_WIDTH - totalColWidth) > 0) {
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        }

        /** Adjusting of each column depend on some factors */
        FontMetrics fm = getFontMetrics(getFont());
        TableColumnModel columnModel = table.getColumnModel();
        for (int i = columnModel.getColumnCount(); --i >= 0;) {
            TableColumn column = columnModel.getColumn(i);
            int displayWidth = model.getDisplayWidth(i);
            int widestValue = (displayWidth > 0 ? displayWidth : -displayWidth);

            /** header value */
            String headerStr = (String) column.getHeaderValue();
            if (headerStr != null) {
                int headerWidth = fm.stringWidth(headerStr) + CELL_MARGE;
                if (widestValue < headerWidth)
                    widestValue = headerWidth;
            }

            /**
             * negative displayWidth means to calculate width from column contents
             */
            if (displayWidth < 0) {
                for (int j = 0; j < model.getRowCount() && widestValue < COLUMN_WIDTHEST; j++) {
                    Object nameObj = model.getValueAt(j, i);
                    if (nameObj != null) {
                        String nameStr = nameObj.toString();
                        if (nameStr != null) {
                            int nameWidth = fm.stringWidth(nameStr) + CELL_MARGE;
                            if (widestValue < nameWidth)
                                widestValue = nameWidth;
                        }
                    }
                }
            }
            if (widestValue > COLUMN_WIDTHEST)
                widestValue = COLUMN_WIDTHEST;
            column.setPreferredWidth(widestValue);
        }
    }

    public final ScreenModel getModel() {
        return (table != null ? (ScreenModel) table.getModel() : null);
    }

    public final int[] getSelectedRows() {
        return table.getSelectedRows();
    }

    public final void clearSelection() {
        table.clearSelection();
    }

    public final Color getSelectionBackground() {
        return table.getSelectionBackground();
    }

    public final Color getSelectionForeground() {
        return table.getSelectionForeground();
    }

    public final boolean isCellEditable(int row, int column) {
        return table.getModel().isCellEditable(row, column);
    }

    public final void stopEditing() {
        if (table != null && table.isEditing())
            table.getCellEditor().stopCellEditing();
        if (prefixTable != null && prefixTable.isEditing())
            prefixTable.getCellEditor().stopCellEditing();
    }

    // Called when the user performs a drag in column 0 of a list view with the
    // right button pressed.
    public void reinsertAction(int firstDraggedRow, int nbDraggedRows, int dropPos) {
    }

    /*
     * Use a subclass of JTable in order to close the current edition when clicking on the other
     * table or when the model is changed.
     */
    class ListViewTable extends JTable {

        public ListViewTable(ListModel model) {
            super(model);
            setTableHeader(new ListViewTableHeader(columnModel));

        }

        public final DescriptionField elementAt(int row, int column) {
            return ((DbListModel) dataModel).getDescriptionFieldAt(row, column);
        }

        public final boolean editCellAt(int row, int column, EventObject e) {
            JTable otherTable = (this == ListView.this.table ? ListView.this.prefixTable
                    : ListView.this.table);
            if (otherTable != null && otherTable.isEditing()) {
                if (!otherTable.getCellEditor().stopCellEditing())
                    return false;
            }
            return super.editCellAt(row, column, e);
        }

        public final void tableChanged(TableModelEvent e) {
            if (isEditing())
                getCellEditor().cancelCellEditing();
            if (e.getColumn() == TableModelEvent.ALL_COLUMNS)
                this.clearSelection(); // insert, delete, reinsert
            super.tableChanged(e);
        }
    }

    // Use a subclass of JTableHeader in order to display a tool tip for each
    // column header.
    static class ListViewTableHeader extends JTableHeader {

        public ListViewTableHeader(TableColumnModel cm) {
            super(cm);
        }

        public final String getToolTipText(MouseEvent event) {
            String tip = null;
            int i = columnModel.getColumnIndexAtX(event.getPoint().x);
            if (i != -1) {
                tip = (String) columnModel.getColumn(i).getHeaderValue();
                if (tip != null && tip.length() == 0)
                    tip = null;
            }
            return tip;
        }
    }

    class ListEditor extends org.modelsphere.jack.awt.AbstractTableCellEditor {

        private String editorName;
        transient private Editor delegate;

        public ListEditor(String newEditorName) {
            editorName = newEditorName;
            delegate = ScreenPlugins.getEditor(editorName); /*
                                                             * get a new instance for configuration
                                                             */
            if (delegate != null) {
                setClickCountForEditing(delegate.getClickCountForEditing());
            }
        }

        public final Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            delegate = ScreenPlugins.getEditor(editorName); /*
                                                             * get a new instance for each edition
                                                             */
            if (delegate != null) {
                return delegate.getTableCellEditorComponent(ListView.this, this, value, isSelected,
                        row, table.convertColumnIndexToModel(column));
            }
            return null;
        }

        public final boolean stopCellEditing() {
            if (!delegate.stopCellEditing())
                return false;
            fireEditingStopped();
            return true;
        }

        public final Object getCellEditorValue() {
            return delegate.getCellEditorValue();
        }
    }

    class ListRenderer implements TableCellRenderer {

        private Renderer delegate;

        public ListRenderer(Renderer renderer) {
            delegate = renderer; /* Use always the same renderer instance */
        }

        public final Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            DbListModel model = (DbListModel) table.getModel();
            DescriptionField mfdf = null;
            column = table.convertColumnIndexToModel(column);
            if (column > 0) {
                Renderer renderer = model.getRenderer(row, column);
                String pluginName = renderer.toString();
                if (pluginName != null) {
                    TableCellRenderer delegate = ScreenPlugins.getMultiRenderer(pluginName);
                    if (delegate == null) {
                        mfdf = model.getDescriptionFieldAt(row, column);
                        if (mfdf != null) {
                            if (!(mfdf instanceof UDFDescriptionField)) {
                                MetaField mf = ((MetaFieldDescriptionField) mfdf).getMetaField();
                                delegate = mf instanceof MetaRelationship ? MultiDbSemanticalObjectRenderer.singleton
                                        : MultiDefaultRenderer.singleton;
                            }
                        }
                    }
                }

                if (delegate != null) {

                    Component delegatedComponent = delegate.getTableCellRendererComponent(model
                            .getScreenView(), value, isSelected, hasFocus, row, column);

                    if (isSelected) {
                        delegatedComponent.setForeground(table.getSelectionForeground());
                        delegatedComponent.setBackground(table.getSelectionBackground());
                    } else {
                        if (mfdf != null) {
                            if (false == mfdf.isEnabled()) {
                                mfdf.setEditable(false);
                                delegatedComponent.setForeground(TerminologyUtil
                                        .getMarkedMetaFieldsFontColor());
                                delegatedComponent.setBackground(TerminologyUtil
                                        .getMarkedMetaFieldsBackColor());
                            } else {
                                delegatedComponent.setForeground(table.getForeground());
                                delegatedComponent.setBackground(table.getBackground());
                            }
                        }
                    }
                    return delegatedComponent;
                }
            }
            return delegate.getTableCellRendererComponent(model.getScreenView(), value, isSelected,
                    hasFocus, row, table.convertColumnIndexToModel(column));
        }
    }

}
