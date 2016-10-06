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

package org.modelsphere.jack.srtool.screen.design;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.table.*;

import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.Header;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.db.srtypes.UDFValueType;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.screen.MultiDefaultRenderer;
import org.modelsphere.jack.baseDb.screen.ScreenPlugins;
import org.modelsphere.jack.baseDb.screen.plugins.MultiDbSemanticalObjectRenderer;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.*;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.jack.util.ExceptionHandler;

public class DesignPanel extends JPanel implements AncestorListener, SelectionListener,
        MouseMotionListener, MouseListener {

    static final String kProperties = LocaleMgr.screen.getString("property");
    static final String kValues = LocaleMgr.screen.getString("value");
    static final String kUpdate0 = LocaleMgr.screen.getString("Update0");
    static final String kRename = LocaleMgr.screen.getString("Rename");
    private static final String kValuesDiffer = LocaleMgr.screen.getString("ValueDiffer");
    private static final String kTitle = LocaleMgr.screen.getString("DesignPanel");

    private static final String AMP = "&amp;"; // NOT LOCALIZABLE - HTML tag
    private static final String LT = "&lt;"; // NOT LOCALIZABLE - HTML tag
    private static final String GT = "&gt;"; // NOT LOCALIZABLE - HTML tag
    private static final String QUOT = "&quot;"; // NOT LOCALIZABLE - HTML tag
    private static final String COPY = "&copy;"; // NOT LOCALIZABLE - HTML tag
    private static final String REG = "&reg;"; // NOT LOCALIZABLE - HTML tag
    private static final String NBSP = "&nbsp;"; // NOT LOCALIZABLE - HTML tag

    private static final int COLUMN_PROPERTIES_MIN_WIDTH = 80;
    private static final int COLUMN_PROPERTIES_RIGHT_MARGIN = 10;

    private static final int MAX_LINE_COUNT_IN_TOOLTIPS = 12;
    private static final int MAX_CHAR_COUNT_IN_TOOLTIPS = 800;
    private static final int MAX_CHAR_PER_LINE = 70;
    private static final int LONG_TEXT_TOOLTIPS_DELAY = 25000;

    public static Color DIFFER_NO_FOCUS_COLOR = null;

    private JPanel outerPanel = new JPanel(new BorderLayout());
    private Header title = new Header(kTitle);
    private DesignTable table;
    private JScrollPane scrollPane;

    private boolean designPanelVisible = false;

    private int defaultTooltipDismissDelay = 0;

    private int rowHeight = -1;

    private DbRefreshListener refreshListener = new DbRefreshListener() {

        @Override
        public void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
            DbObject obj = event.dbo;
            if (event.metaField == DbUDFValue.fValue) {
                TableCellEditor editor = table.getCellEditor();
                if (editor != null) {
                    editor.cancelCellEditing();
                }
                refresh();
            } else if (event.dbo != null && (event.dbo.getTransStatus() != Db.OBJ_REMOVED)) {
                TableCellEditor editor = table.getCellEditor();
                if (editor != null) {
                    editor.cancelCellEditing();
                }
                loadData();
                refreshComponents();
            } else {
                TableCellEditor editor = table.getCellEditor();
                if (editor != null) {
                    editor.cancelCellEditing();
                }
                refresh();
            }
        }

    };

    public DesignPanel() {
        setLayout(new BorderLayout());

        table = new DesignTable(this);
        scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        outerPanel.add(scrollPane, BorderLayout.CENTER);

        scrollPane.setBorder(null);

        add(title, BorderLayout.NORTH);
        add(outerPanel, BorderLayout.CENTER);

        setDesignPanelVisible(false);
        addAncestorListener(this);
        table.addMouseMotionListener(this);
        table.addMouseListener(this);

        defaultTooltipDismissDelay = ToolTipManager.sharedInstance().getDismissDelay();
        JTextField textfield = new JTextField("LAZY"); // NOT LOCALIZABLE
        rowHeight = textfield.getPreferredSize().height;

        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
        if (preferences != null) {
            preferences.addPropertyChangeListener(DefaultMainFrame.class,
                    DefaultMainFrame.COMPONENTS_HEADER_VISIBILITY, new PropertyChangeListener() {
                        public void propertyChange(PropertyChangeEvent evt) {
                            if (evt.getNewValue() == null
                                    || !(evt.getNewValue() instanceof Boolean))
                                return;
                            setHeaderVisible(((Boolean) evt.getNewValue()).booleanValue());
                        }
                    });
        }

    }

    private void installListeners() {
        ApplicationContext.getFocusManager().addSelectionListener(this);
    }

    private void removeListeners() {
        ApplicationContext.getFocusManager().removeSelectionListener(this);
    }

    private void installDbListeners() {
        DbObject[] dbos = table.getDesignTableModel().getDbObjects();
        for (int i = 0; i < dbos.length; i++) {
            dbos[i].addDbRefreshListener(refreshListener);
        }
        DbUDFValue.fValue.addDbRefreshListener(refreshListener);
    }

    private void removeDbListeners() {
        DbObject[] dbos = table.getDesignTableModel().getDbObjects();
        for (int i = 0; i < dbos.length; i++) {
            dbos[i].removeDbRefreshListener(refreshListener);
        }
        DbUDFValue.fValue.removeDbRefreshListener(refreshListener);
    }

    public void selectionChanged(SelectionChangedEvent e) throws DbException {

        final TableCellEditor editor = table.getCellEditor();
        if (editor != null) {
            // In this case we apply the changes before updating with the new
            // selection.
            // We must post on event queue since applying changes needs a write
            // trans and
            // we are in a read only transaction when selectionChanged is
            // invoked.
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    editor.stopCellEditing();
                    refresh();
                }
            });
        } else
            refresh();
    }

    public final void setDesignPanelVisible(boolean visible) {
        designPanelVisible = visible;
        setVisible(visible);
    }

    public final boolean isDesignPanelVisible() {
        return designPanelVisible;
    }

    public final void setHeaderVisible(boolean visible) {
        if (title.isVisible() != visible)
            title.setVisible(visible);
    }

    public void updateUI() {
        super.updateUI();

        // Fix a bug in the swings updateUI mechanism (as of JDK 1.4.0)
        if (table != null && table.getTableHeader() != null && !isDesignPanelVisible())
            table.getTableHeader().updateUI();

        JTextField textfield = new JTextField("LAZY"); // NOT LOCALIZABLE
        rowHeight = textfield.getPreferredSize().height;

        Color refColor = UIManager.getColor("TableHeader.background"); // NOT LOCALIZABLE - property
        if (refColor != null)
            DIFFER_NO_FOCUS_COLOR = refColor;
        else
            DIFFER_NO_FOCUS_COLOR = UIManager.getColor("Table.background");
    }

    public void ancestorAdded(AncestorEvent event) {
        installListeners();
        refresh();
    }

    public void ancestorRemoved(AncestorEvent event) {
        removeListeners();
        removeDbListeners();
        table.getDesignTableModel().clear();
    }

    public void ancestorMoved(AncestorEvent event) {
    }

    private void refresh() {
        table.clearSelection();
        removeDbListeners();
        table.getDesignTableModel().init();
        loadData();
        refreshComponents();
        installDbListeners();
    }

    private void loadData() {
        CellEditor editor = table.getCellEditor();
        if (editor != null)
            editor.cancelCellEditing();
        try {
            DbObject[] dbos = table.getDesignTableModel().getDbObjects();
            DbMultiTrans.beginTrans(Db.READ_TRANS, dbos, null);
            table.getDesignTableModel().load();
            DbMultiTrans.commitTrans(dbos);
        } catch (DbException e) {
            ExceptionHandler.processUncatchedException(this, e);
        }
    }

    private void refreshComponents() {
        if (table.getRowCount() > 0) {
            // GP - TODO Evaluate the necessity and performance cost for this code.
            // This seem to be executed each time the selection change
            int availableWidth = (int) (table.getWidth() * 0.5);
            if (availableWidth == 0)
                availableWidth = 1000; // getWidth may not have been calculated
            int col0MaxWidth = 0;

            JLabel label = (JLabel) table.getCellRenderer(0, 0);
            if (label != null) {
                ArrayList<RowData> data = table.getDesignTableModel().getData();
                DbObject[] dbos = table.getDesignTableModel().getDbObjects();
                for (int i = 0; i < data.size() && col0MaxWidth < availableWidth; i++) {
                    RowData row = ((RowData) data.get(i));
                    if (row.udf == null) {
                        String sRetVal = null;
                        try {
                            sRetVal = ApplicationContext.getSemanticalModel().getDisplayText(
                                    row.metaclass, row.metafield, dbos[0], DesignPanel.class, true);

                            if (sRetVal == null) {
                                sRetVal = ApplicationContext.getSemanticalModel().getDisplayText(
                                        row.metaclass, row.metafield, dbos[0], DesignPanel.class);
                                if (sRetVal == null)
                                    sRetVal = row.metafield.getGUIName();
                            }
                        } catch (DbException dbe) {
                            sRetVal = ApplicationContext.getSemanticalModel().getDisplayText(
                                    row.metaclass, row.metafield, DesignPanel.class);
                            if (sRetVal == null)
                                sRetVal = row.metafield.getGUIName();
                        }

                        DesignTableModel model = (DesignTableModel) table.getModel();
                        Font font = label.getFont();
                        if (model.isValuesDiffer(i) && font != null) {

                            int style = font.getStyle();
                            if ((style & Font.BOLD) != 0)
                                style = style & ~Font.BOLD;
                            else
                                style = style | Font.BOLD;
                            font = new Font(font.getName(), style, font.getSize());
                        }

                        col0MaxWidth = Math.max(SwingUtilities.computeStringWidth(label
                                .getFontMetrics(font), sRetVal)
                                + COLUMN_PROPERTIES_RIGHT_MARGIN, col0MaxWidth);
                    } else
                        col0MaxWidth = Math.max(SwingUtilities.computeStringWidth(label
                                .getFontMetrics(label.getFont()), row.udfName)
                                + COLUMN_PROPERTIES_RIGHT_MARGIN, col0MaxWidth);
                }
            }
            TableColumn column = table.getColumnModel().getColumn(0);
            int width = Math.max(col0MaxWidth, COLUMN_PROPERTIES_MIN_WIDTH);
            width = Math.min(width, availableWidth);
            column.setPreferredWidth(width);
            column.setMaxWidth(width);
            column.setMinWidth(width);
            column.setPreferredWidth(width);
            column.setWidth(width);
            column.sizeWidthToFit();
            table.sizeColumnsToFit(0);
        }

        table.setRowHeight(Math.max(rowHeight - 1, 16));
        table.revalidate();
        table.repaint();
    }

    public final void mouseDragged(MouseEvent e) {
    }

    public final void mouseMoved(MouseEvent e) {
        Point ePoint = e.getPoint();
        int row = table.rowAtPoint(ePoint);
        if (row < 0) {
            ToolTipManager.sharedInstance().setDismissDelay(defaultTooltipDismissDelay);
            table.setToolTipText("");
            return;
        }
        Object value = table.getValueAt(row, 1);
        String rowheader = table.getValueAt(row, 0).toString();
        String text = "";
        text += "<html>"; // NOT LOCALIZABLE
        text += "<b>" + rowheader + "</b><br>"; // NOT LOCALIZABLE

        DesignTableModel model = (DesignTableModel) table.getModel();
        if (value == null || value instanceof Image) {
            if (value == null && model.isValuesDiffer(row))
                text += "&lt;" + kValuesDiffer + "&gt;"; // NOT LOCALIZABLE
            ToolTipManager.sharedInstance().setDismissDelay(defaultTooltipDismissDelay);
            table.setToolTipText(text + "</html>"); // NOT LOCALIZABLE
            return;
        }

        String sValue = ApplicationContext.getSemanticalModel().getDisplayValue(value,
                model.getMetaFieldAt(row), DesignTableModel.class).toString();
        if (sValue.indexOf("\n") < 0) { // NOT LOCALIZABLE
            text += sValue;
            ToolTipManager.sharedInstance().setDismissDelay(defaultTooltipDismissDelay);
        } else {
            StringTokenizer st = new StringTokenizer(sValue, ",.©®(;:?!<>& \n\"", true); // NOT
            // LOCALIZABLE
            int count = 0;
            int linecount = 0;
            int charinline = 0;
            while (st.hasMoreTokens() && count < MAX_CHAR_COUNT_IN_TOOLTIPS
                    && linecount < MAX_LINE_COUNT_IN_TOOLTIPS) {
                String token = st.nextToken();
                if (token.equals("\n")) { // NOT LOCALIZABLE
                    text += "<br>"; // NOT LOCALIZABLE
                    linecount++;
                    charinline = 0;
                } else if (token.equals("<")) {
                    text += LT;
                    charinline += 1;
                } else if (token.equals(">")) {
                    text += GT;
                    charinline += 1;
                } else if (token.equals("&")) {
                    text += AMP;
                    charinline += 1;
                } else if (token.equals("\"")) {
                    text += QUOT;
                    charinline += 1;
                } else if (token.equals("©")) {
                    text += COPY;
                    charinline += 1;
                } else if (token.equals("®")) {
                    text += REG;
                    charinline += 1;
                } else if (charinline >= MAX_CHAR_PER_LINE) { // NOT LOCALIZABLE
                    text += "<br>";
                    // NOT LOCALIZABLE
                    linecount++;
                    charinline = 0;
                    text += (token.equals(" ") ? NBSP : token);
                    charinline += token.length();
                } else {
                    text += (token.equals(" ") ? NBSP : token);
                    charinline += token.length();
                }
                count += token.length();
            }
            if (count >= MAX_CHAR_COUNT_IN_TOOLTIPS || linecount >= MAX_LINE_COUNT_IN_TOOLTIPS)
                text += "<br><b>...</b>"; // NOT LOCALIZABLE
            ToolTipManager.sharedInstance().setDismissDelay(LONG_TEXT_TOOLTIPS_DELAY);
        }

        text += "</html>"; // NOT LOCALIZABLE
        table.setToolTipText(text);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        ApplicationContext.getDefaultMainFrame().getExplorerPanel().getMainView().cancelEditing();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
        ToolTipManager.sharedInstance().setDismissDelay(defaultTooltipDismissDelay);
    }

    public void lockGUI() {
        if (!isDesignPanelVisible())
            return;
        removeListeners();
        removeDbListeners();
    }

    public void unlockGUI() {
        if (!isDesignPanelVisible())
            return;
        installListeners();
        refresh();
    }
}
