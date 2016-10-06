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

package org.modelsphere.jack.srtool.list;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

import org.modelsphere.jack.awt.SelectDialog;
import org.modelsphere.jack.awt.SelectDialogItem;
import org.modelsphere.jack.awt.ThinBevelBorder;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbUDFValue;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.baseDb.screen.MultiDefaultRenderer;
import org.modelsphere.jack.baseDb.screen.ScreenPlugins;
import org.modelsphere.jack.baseDb.screen.plugins.MultiDbSemanticalObjectRenderer;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.util.ExceptionHandler;

public class ListTable extends JTable implements ActionListener, MouseMotionListener, MouseListener {
    private static final String kRessetDefault = LocaleMgr.action.getString("ressetSetting");
    private static final String kSaveDefault = LocaleMgr.action.getString("saveSetting");
    private static final String kInsertColumn = LocaleMgr.action.getString("insertColumn");
    private static final String kRemoveColumn = LocaleMgr.action.getString("removeColumn");
    private static final String kSelectColumns = LocaleMgr.action.getString("selectColumns");
    private static final String kShowHorizontalGrid = LocaleMgr.action
            .getString("showHorizontalGrid");
    private static final String kShowVerticalGrid = LocaleMgr.action.getString("showVerticalGrid");

    private static final String AMP = "&amp;"; //NOT LOCALIZABLE - HTML tag
    private static final String LT = "&lt;"; //NOT LOCALIZABLE - HTML tag
    private static final String GT = "&gt;"; //NOT LOCALIZABLE - HTML tag
    private static final String QUOT = "&quot;"; //NOT LOCALIZABLE - HTML tag
    private static final String COPY = "&copy;"; //NOT LOCALIZABLE - HTML tag
    private static final String REG = "&reg;"; //NOT LOCALIZABLE - HTML tag
    private static final String NBSP = "&nbsp;"; //NOT LOCALIZABLE - HTML tag

    // Properties for this list
    public static final String SHOW_HORIZONTAL_GRID_PROPERTY = "ShowHorizontalGrid"; //NOT LOCALIZABLE, property
    public static final Boolean SHOW_HORIZONTAL_GRID_DEFAULT = Boolean.FALSE;
    public static final String SHOW_VERTICAL_GRID_PROPERTY = "ShowVerticalGrid"; //NOT LOCALIZABLE, property
    public static final Boolean SHOW_VERTICAL_GRID_DEFAULT = Boolean.TRUE;

    private static final int ROW_EXTRA_HEIGHT = 0;

    protected static final Integer ASC = new Integer(1);
    protected static final Integer DESC = new Integer(-1);

    private static final int MAX_LINE_COUNT_IN_TOOLTIPS = 12;
    private static final int MAX_CHAR_COUNT_IN_TOOLTIPS = 800;
    private static final int MAX_CHAR_PER_LINE = 70;
    private static final int LONG_TEXT_TOOLTIPS_DELAY = 25000;

    public static Color SORTED_COLUMN_COLOR = null;

    private boolean ascending = true;

    private ListTableCellRenderer renderer;

    private JPopupMenu configPopup = new JPopupMenu();
    private JMenu insertMenu = new JMenu(kInsertColumn);
    private JMenu removeMenu = new JMenu(kRemoveColumn);
    private JMenuItem selectItem = new JMenuItem(kSelectColumns + "..."); // NOT LOCALIZABLE
    private JMenuItem saveMenuItem = new JMenuItem(kSaveDefault);
    private JMenuItem ressetMenuItem = new JMenuItem(kRessetDefault);
    private JMenuItem showHorizontalGridItem = new JCheckBoxMenuItem(kShowHorizontalGrid);
    private JMenuItem showVerticalGridItem = new JCheckBoxMenuItem(kShowVerticalGrid);

    private int mousePressedColumn = -1;
    private int popupColumn = -1;

    private int redF = SORTED_COLUMN_COLOR.getRed();
    private int greenF = SORTED_COLUMN_COLOR.getGreen();
    private int blueF = SORTED_COLUMN_COLOR.getBlue();
    private int redB = getBackground().getRed();
    private int greenB = getBackground().getGreen();
    private int blueB = getBackground().getBlue();

    private int defaultTooltipDismissDelay = 0;

    private PreferencesChangeListener preferencesChangeListener = new PreferencesChangeListener();

    ListTable(ListTableModel model) throws DbException {
        super(model);
        setColumnSelectionAllowed(false);
        renderer = new ListTableCellRenderer();
        setDefaultRenderer(Object.class, renderer);
        this.setCellSelectionEnabled(false);
        this.setRowSelectionAllowed(true);
        this.setShowHorizontalLines(true);
        this.setShowVerticalLines(true);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.getTableHeader().setDefaultRenderer(new HeaderCellRenderer());
        getListTableModel().addMouseListenerToHeaderInTable(this);
        getListTableModel().setListTable(this);
        this.getTableHeader().addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
                if (!e.isPopupTrigger())
                    return;

                popupColumn = ListTable.this.columnAtPoint(e.getPoint());
                configPopup.show(ListTable.this.getTableHeader(), e.getPoint().x, e.getPoint().y);
            }

            public void mouseReleased(MouseEvent e) {
                if (!e.isPopupTrigger())
                    return;

                popupColumn = ListTable.this.columnAtPoint(e.getPoint());
                configPopup.show(ListTable.this.getTableHeader(), e.getPoint().x, e.getPoint().y);
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });

        configPopup.add(insertMenu);
        configPopup.add(removeMenu);
        configPopup.add(selectItem);
        configPopup.addSeparator();
        configPopup.add(showHorizontalGridItem);
        configPopup.add(showVerticalGridItem);
        configPopup.addSeparator();
        configPopup.add(saveMenuItem);
        configPopup.add(ressetMenuItem);

        updateShowGrid();
        showVerticalGridItem.addActionListener(this);
        showHorizontalGridItem.addActionListener(this);
        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
        preferences.addPropertyChangeListener(ListTable.class, SHOW_HORIZONTAL_GRID_PROPERTY,
                preferencesChangeListener);
        preferences.addPropertyChangeListener(ListTable.class, SHOW_VERTICAL_GRID_PROPERTY,
                preferencesChangeListener);

        addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
                if (!e.isPopupTrigger())
                    return;
                DbObject[] selobjs = getSelectedDbObjects();
                if (selobjs == null || selobjs.length == 0)
                    return;
                JPopupMenu selectionPopup = ApplicationContext.getApplPopupMenu().getPopupMenu(
                        false);
                if (selectionPopup != null)
                    selectionPopup.show(ListTable.this, e.getPoint().x, e.getPoint().y);
            }

            public void mouseReleased(MouseEvent e) {
                if (!e.isPopupTrigger())
                    return;
                DbObject[] selobjs = getSelectedDbObjects();
                if (selobjs == null || selobjs.length == 0)
                    return;
                JPopupMenu selectionPopup = ApplicationContext.getApplPopupMenu().getPopupMenu(
                        false);
                if (selectionPopup != null)
                    selectionPopup.show(ListTable.this, e.getPoint().x, e.getPoint().y);
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
        getTableHeader().addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)
                        || e.isPopupTrigger()
                        || getTableHeader().getResizingColumn() != null
                        || (getTableHeader().getDraggedColumn() != null && getTableHeader()
                                .getDraggedDistance() > 0))
                    mousePressedColumn = -1;
                else
                    mousePressedColumn = ListTable.this.columnAtPoint(e.getPoint());
                getTableHeader().repaint();

            }

            public void mouseReleased(MouseEvent e) {
                mousePressedColumn = -1;
                getTableHeader().repaint();
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
                mousePressedColumn = -1;
            }
        });

        getTableHeader().addMouseListener(this);
        getTableHeader().addMouseMotionListener(this);

        saveMenuItem.addActionListener(this);
        ressetMenuItem.addActionListener(this);
        selectItem.addActionListener(this);

        addMouseMotionListener(this);
        addMouseListener(this);

        defaultTooltipDismissDelay = ToolTipManager.sharedInstance().getDismissDelay();

        updateConfigPopup();
    }

    private class PreferencesChangeListener implements PropertyChangeListener {
        PreferencesChangeListener() {
        }

        public void propertyChange(PropertyChangeEvent e) {
            ListTable.this.updateShowGrid();
        }
    }

    public void updateUI() {
        super.updateUI();
        setRowHeight(new JLabel("LAZY").getPreferredSize().height + ROW_EXTRA_HEIGHT); // NOT LOCALIZABLE - not visible in GUI

        if (configPopup != null)
            configPopup.updateUI();
        if (saveMenuItem != null)
            saveMenuItem.updateUI();
        if (ressetMenuItem != null)
            ressetMenuItem.updateUI();
        if (insertMenu != null)
            insertMenu.updateUI();
        if (removeMenu != null)
            removeMenu.updateUI();

        if (renderer != null)
            renderer.updateUI();

        Color refFColor = UIManager.getColor("Panel.background"); // NOT LOCALIZABLE - property
        Color refBColor = UIManager.getColor("Table.background"); // NOT LOCALIZABLE - property
        if (refFColor == null || refBColor == null)
            return;

        SORTED_COLUMN_COLOR = new Color(Math.min(255, refFColor.getRed() + 20), Math.min(255,
                refFColor.getGreen() + 20), Math.min(255, refFColor.getBlue() + 20));
        redF = SORTED_COLUMN_COLOR.getRed();
        greenF = SORTED_COLUMN_COLOR.getGreen();
        blueF = SORTED_COLUMN_COLOR.getBlue();
        redB = refBColor.getRed();
        greenB = refBColor.getGreen();
        blueB = refBColor.getBlue();
    }

    ListTableModel getListTableModel() {
        TableModel model = getModel();
        if (model != null && (model instanceof ListTableModel))
            return (ListTableModel) model;
        return null;
    }

    private class HeaderCellRenderer extends JButton implements TableCellRenderer {
        private Icon ascIcon = new ArrowIcon(ArrowIcon.NORTH);
        private Icon descIcon = new ArrowIcon(ArrowIcon.SOUTH);
        private Border normal = new ThinBevelBorder(ThinBevelBorder.RAISED);
        private Border pressed = new ThinBevelBorder(ThinBevelBorder.LOWERED);

        public HeaderCellRenderer() {
            super();
            setOpaque(true);
            setMargin(new Insets(0, 0, 0, 0));

            //setBorder(noFocusBorder);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            ListTableModel model = (ListTableModel) table.getModel();
            int modelcolumn = table.convertColumnIndexToModel(column);
            if (modelcolumn == model.sortedColumn && model.getColumnOrdering(modelcolumn) != null) {
                setIcon(model.getColumnOrdering(modelcolumn) == ASC ? ascIcon : descIcon);
            } else
                setIcon(null);

            JTableHeader header = table.getTableHeader();
            if (header != null) {
                setForeground(UIManager.getColor("TableHeader.foreground")); //NOT LOCALIZABLE
                setBackground(/* SORTED_COLUMN_COLOR */UIManager.getColor("TableHeader.background")); //NOT LOCALIZABLE
                Font font = header.getFont();
                setFont(font/* new Font(font.getName(), font.getStyle() | Font.BOLD, font.getSize()) */);

                if (model.isColumnEditable(column) == false && !isSelected) {
                    TableColumn firstColumn = table.getColumnModel().getColumn(column);
                    DefaultTableCellRenderer columnRenderer = new DefaultTableCellRenderer();
                    columnRenderer.setForeground(TerminologyUtil.getMarkedMetaFieldsFontColor());
                    columnRenderer.setBackground(TerminologyUtil.getMarkedMetaFieldsBackColor());
                    //setForeground(TerminologyUtil.getMarkedMetaFieldsHeaderFontColor());
                    firstColumn.setCellRenderer(columnRenderer);
                }
            }
            setText((value == null) ? "" : value.toString());
            if (ListTable.this.mousePressedColumn == column) {
                this.getModel().setArmed(true);
                this.getModel().setPressed(true);
            } else {
                this.getModel().setArmed(false);
                this.getModel().setPressed(false);
            }
            //setBorder(UIManager.getBorder("TableHeader.cellBorder"));  // NOT LOCALIZABLE - property
            //setBorder(ListTable.this.mousePressedColumn == column ? pressed : normal);
            //this.setsetIconTextGap(5);

            setHorizontalAlignment(JLabel.CENTER);
            setHorizontalTextPosition(SwingConstants.LEADING);
            return this;
        }

        public void validate() {
        }

        public void revalidate() {
        }

        public void repaint(long tm, int x, int y, int width, int height) {
        }

        public void repaint(Rectangle r) {
        }

        protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
            // Strings get interned...
            if (propertyName == "text") { //NOT LOCALIZABLE
                super.firePropertyChange(propertyName, oldValue, newValue);
            }
        }

        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        }

        protected void setValue(Object value) {
            setText((value == null) ? "" : value.toString());
        }

    }

    private class ListTableCellRenderer extends JLabel implements TableCellRenderer {
        protected Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
        int tempRed = 0;
        int tempGreen = 0;
        int tempBlue = 0;
        int tempRowCount = 0;

        public ListTableCellRenderer() {
            super();
            setOpaque(true);
            setBorder(noFocusBorder);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            ListTableModel model = (ListTableModel) table.getModel();
            if (column > -1) {
                int modelcolumn = table.convertColumnIndexToModel(column);
                String pluginName = model.getRendererAt(modelcolumn);
                if (pluginName != null) {
                    TableCellRenderer delegate = ScreenPlugins.getMultiRenderer(pluginName);
                    MetaField mf = model.getMetaFieldAt(modelcolumn);
                    if (delegate == null) {
                        delegate = (mf instanceof MetaRelationship ? MultiDbSemanticalObjectRenderer.singleton
                                : MultiDefaultRenderer.singleton);
                    }
                    if (delegate != null) {

                        Component delegatedComponent = delegate.getTableCellRendererComponent(
                                table, value, isSelected, false, row, column);

                        if (model.isColumnEditable(column) == false && !isSelected) {
                            TableColumn firstColumn = table.getColumnModel().getColumn(column);
                            DefaultTableCellRenderer columnRenderer = new DefaultTableCellRenderer();
                            columnRenderer.setForeground(TerminologyUtil
                                    .getMarkedMetaFieldsFontColor());
                            firstColumn.setCellRenderer(columnRenderer);
                        }

                        if (!isSelected && delegatedComponent != null
                                && modelcolumn == model.sortedColumn) {
                            tempRowCount = model.getRowCount();
                            boolean invertR = false;
                            boolean invertG = false;
                            boolean invertB = false;
                            if (redB < redF)
                                invertR = true;
                            if (greenB < greenF)
                                invertG = true;
                            if (blueB < blueF)
                                invertB = true;
                            if (model.getColumnOrdering(modelcolumn) == ASC) {
                                tempRed = (invertR ? 0 : 255)
                                        - (int) ((((double) (redB - redF)) / ((double) tempRowCount)) * row);
                                tempGreen = (invertG ? 0 : 255)
                                        - (int) ((((double) (greenB - greenF)) / ((double) tempRowCount)) * row);
                                tempBlue = (invertB ? 0 : 255)
                                        - (int) ((((double) (blueB - blueF)) / ((double) tempRowCount)) * row);
                            } else {
                                tempRed = (invertR ? 0 : 255)
                                        - (int) ((((double) (redB - redF)) / ((double) tempRowCount)) * (tempRowCount - row));
                                tempGreen = (invertG ? 0 : 255)
                                        - (int) ((((double) (greenB - greenF)) / ((double) tempRowCount)) * (tempRowCount - row));
                                tempBlue = (invertB ? 0 : 255)
                                        - (int) ((((double) (blueB - blueF)) / ((double) tempRowCount)) * (tempRowCount - row));
                            }
                            delegatedComponent
                                    .setBackground(new Color(tempRed, tempGreen, tempBlue));
                        } else if (!isSelected && delegatedComponent != null)
                            delegatedComponent.setBackground(table.getBackground());
                        return delegatedComponent;
                    }
                }
            }

            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }

            Font font = table.getFont();

            if (font != null && model.getMetaFieldAt(column) == DbUDFValue.fValue) {
                int style = font.getStyle();
                if ((style & Font.ITALIC) != 0)
                    style = style & ~Font.ITALIC;
                else
                    style = style | Font.ITALIC;
                font = new Font(font.getName(), style, font.getSize());
            }

            setFont(font);

            setBorder(noFocusBorder);

            setValue(value);

            // ---- begin optimization to avoid painting background ----
            //Color back = getBackground();
            //boolean colorMatch = (back != null) && ( back.equals(table.getBackground()) ) && table.isOpaque();
            //setOpaque(!colorMatch);
            // ---- end optimization to aviod painting background ----

            return this;
        }

        public void validate() {
        }

        public void revalidate() {
        }

        public void repaint(long tm, int x, int y, int width, int height) {
        }

        public void repaint(Rectangle r) {
        }

        protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
            if (propertyName == "text") { // NOT LOCALIZABLE - property
                super.firePropertyChange(propertyName, oldValue, newValue);
            }
        }

        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        }

        protected void setValue(Object value) {
            setText((value == null) ? "" : value.toString());
        }

    }

    // This class draw an arrow pointing in the south direction.
    // The main algo for this come from javax.swing.plaf.basic.DefaultArrowButton.
    // In JDK 1.3, it is not possible to get this icon (or button) using the UI properties
    //  table (no entry for this).
    private static class ArrowIcon implements Icon {
        static final int NORTH = 0;
        static final int SOUTH = 1;
        int height = 16;
        int width = 16;
        int direction = NORTH;

        ArrowIcon(int direction) {
            this.direction = direction;
        }

        public int getIconWidth() {
            return width;
        }

        public int getIconHeight() {
            return height;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Color oldColor = g.getColor();
            boolean enabled = c.isEnabled();
            int insetx = 4;
            int insety = 4;
            if (c instanceof JComponent) {
                Insets borderinset = ((JComponent) c).getBorder().getBorderInsets(c);
                insetx = borderinset.right + borderinset.left;
                insety = borderinset.top + borderinset.bottom;
            }

            int i = 0;
            int j = 0;
            int w = Math.min(c.getWidth(), width);
            int h = c.getHeight();
            if (h < 5 || w < 5) { // not enough space for the arrow
                g.setColor(oldColor);
                return;
            }

            int size = Math.min((h - insety) / 2, (w - insetx) / 2);
            size = Math.max(size, 2);
            int mid = size / 2 + x;

            x = (w - size) / 2; // center arrow
            y = (h - size) / 2; // center arrow
            g.translate(x, y); // move the x,y origin in the graphic

            if (enabled)
                g.setColor(UIManager.getColor("controlDkShadow")); // NOT LOCALIZABLE
            else
                g.setColor(UIManager.getColor("controlShadow")); // NOT LOCALIZABLE

            if (!enabled) {
                g.translate(1, 1);
                g.setColor(UIManager.getColor("controlLtHighlight")); // NOT LOCALIZABLE
                for (i = size - 1; i >= 0; i--) {
                    g.drawLine(mid - i, j, mid + i, j);
                    j++;
                }
                g.translate(-1, -1);
                g.setColor(UIManager.getColor("controlShadow")); // NOT LOCALIZABLE
            }

            j = 0;

            switch (direction) {
            case NORTH:
                for (i = 0; i < size; i++) {
                    g.drawLine(mid - i, j, mid + i, j);
                    j++;
                }
                break;
            case SOUTH:
                for (i = size - 1; i >= 0; i--) {
                    g.drawLine(mid - i, j, mid + i, j);
                    j++;
                }
                break;
            }

            g.translate(-x, -y);
            g.setColor(oldColor);
        }

    }

    private static class ColumnMenuItem extends JMenuItem {
        ColumnDescriptor descriptor;
        boolean insert;

        ColumnMenuItem(ColumnDescriptor descriptor, boolean insert) {
            super(descriptor.toString());
            this.descriptor = descriptor;
            this.insert = insert;
        }
    }

    private void updateShowGrid() {
        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
        boolean showhorizontal = preferences.getPropertyBoolean(ListTable.class,
                SHOW_HORIZONTAL_GRID_PROPERTY, SHOW_HORIZONTAL_GRID_DEFAULT).booleanValue();
        boolean showvertical = preferences.getPropertyBoolean(ListTable.class,
                SHOW_VERTICAL_GRID_PROPERTY, SHOW_VERTICAL_GRID_DEFAULT).booleanValue();

        setShowHorizontalLines(showhorizontal);
        setShowVerticalLines(showvertical);
        if (showhorizontal != showHorizontalGridItem.isSelected())
            showHorizontalGridItem.setSelected(showhorizontal);
        if (showvertical != showVerticalGridItem.isSelected())
            showVerticalGridItem.setSelected(showvertical);
    }

    private void updateConfigPopup() {
        // Clear insert and remove Menu
        Component[] components = insertMenu.getComponents();
        if (components != null) {
            for (int i = 0; i < components.length; i++) {
                if (!(components[i] instanceof JMenuItem))
                    continue;
                ((JMenuItem) components[i]).removeActionListener(this);
            }
        }
        insertMenu.removeAll();
        //
        components = removeMenu.getComponents();
        if (components != null) {
            for (int i = 0; i < components.length; i++) {
                if (!(components[i] instanceof JMenuItem))
                    continue;
                ((JMenuItem) components[i]).removeActionListener(this);
            }
        }
        removeMenu.removeAll();

        // Populate insert and remove Menu
        ListTableModel model = getListTableModel();
        ColumnDescriptor[] descriptors = model.getColumnDescriptors();
        if (descriptors != null && descriptors.length > 0) {
            for (int i = 0; i < descriptors.length; i++) {
                int columnindex = model.indexInModelOf(descriptors[i]);
                JMenuItem item = null;
                if (columnindex > -1) { // remove
                    item = new ColumnMenuItem(descriptors[i], false);
                    removeMenu.add(item);
                } else { // add
                    item = new ColumnMenuItem(descriptors[i], true);
                    insertMenu.add(item);
                }
                item.addActionListener(this);
            }
        }

        insertMenu.setEnabled(insertMenu.getMenuComponentCount() > 0);
        removeMenu.setEnabled(removeMenu.getMenuComponentCount() > 1); // do not remove last column

    }

    public void actionPerformed(ActionEvent e) {

        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();

        if (e.getSource() == showVerticalGridItem) {
            preferences.setProperty(ListTable.class, SHOW_VERTICAL_GRID_PROPERTY,
                    showVerticalGridItem.isSelected());
            return;
        }

        if (e.getSource() == showHorizontalGridItem) {
            preferences.setProperty(ListTable.class, SHOW_HORIZONTAL_GRID_PROPERTY,
                    showHorizontalGridItem.isSelected());
            return;
        }
        if (e.getSource() == this.saveMenuItem) {
            this.getListTableModel().saveStucture();
            return;
        }
        if (e.getSource() == this.ressetMenuItem) {
            try {
                getListTableModel().ressetStucture();
            } catch (DbException ex) {
                JInternalFrame container = (JInternalFrame) SwingUtilities.getAncestorOfClass(
                        JInternalFrame.class, this);
                if (container != null)
                    container.dispose();
                ExceptionHandler.processUncatchedException(
                        ApplicationContext.getDefaultMainFrame(), ex);
            }
            return;
        }

        if (e.getSource() == selectItem) {
            ListTableModel model = getListTableModel();
            ColumnDescriptor[] descriptors = getListTableModel().getColumnDescriptors();
            SelectDialogItem[] items = new SelectDialogItem[descriptors.length];
            for (int i = 0; i < descriptors.length; i++) {
                items[i] = new SelectDialogItem(descriptors[i].toString(), null, model
                        .indexInModelOf(descriptors[i]) > -1, descriptors[i]);
            }
            int result = SelectDialog.select(ApplicationContext.getDefaultMainFrame(),
                    kSelectColumns, items, SelectDialog.OPTION_SELECT_MIN_1);
            if (result == SelectDialog.ACTION_CANCEL)
                return;
            ArrayList newcols = new ArrayList();
            for (int i = 0; i < items.length; i++) {
                if (items[i].selected)
                    newcols.add(items[i].object);
            }
            descriptors = new ColumnDescriptor[newcols.size()];
            for (int i = 0; i < descriptors.length; i++) {
                descriptors[i] = (ColumnDescriptor) newcols.get(i);
            }
            try {
                getListTableModel().setColumns(descriptors, popupColumn);
                updateConfigPopup();
            } catch (DbException ex) {
                JInternalFrame container = (JInternalFrame) SwingUtilities.getAncestorOfClass(
                        JInternalFrame.class, this);
                if (container != null)
                    container.dispose();
                ExceptionHandler.processUncatchedException(
                        ApplicationContext.getDefaultMainFrame(), ex);
            }
            return;
        }

        if (!(e.getSource() instanceof ColumnMenuItem))
            return;

        ColumnDescriptor descriptor = ((ColumnMenuItem) e.getSource()).descriptor;
        boolean insert = ((ColumnMenuItem) e.getSource()).insert;

        try {
            if (insert)
                getListTableModel().insertColumn(descriptor, popupColumn);
            else
                // remove
                getListTableModel().removeColumn(descriptor);
            updateConfigPopup();
        } catch (DbException ex) {
            JInternalFrame container = (JInternalFrame) SwingUtilities.getAncestorOfClass(
                    JInternalFrame.class, this);
            if (container != null)
                container.dispose();
            ExceptionHandler
                    .processUncatchedException(ApplicationContext.getDefaultMainFrame(), ex);
            return;
        }
        repaint();
    }

    void setSelectedDbObjects(DbObject[] dbos) {
        Rectangle visibleRect = getVisibleRect();
        clearSelection();
        if (dbos == null || dbos.length == 0)
            return;
        DbObject dbo = null;
        int idx = -1;
        for (int i = 0; i < dbos.length; i++) {
            idx = getListTableModel().indexOf(dbos[i]);
            if (idx > -1) {
                addRowSelectionInterval(idx, idx);
            }
        }
        // visible rect is calculated using a fix row height
        scrollRectToVisible(new Rectangle(visibleRect == null ? 0 : visibleRect.x, getRowHeight()
                * getSelectedRow(), 10, getRowHeight() + 2));
    }

    DbObject[] getSelectedDbObjects() {
        int[] selections = getSelectedRows();
        if (selections == null) {
            return null;
        }
        DbObject[] selobjs = new DbObject[selections.length];
        for (int i = 0; i < selections.length; i++) {
            selobjs[i] = getListTableModel().getDbObjectAt(selections[i]);
        }
        return selobjs;
    }

    // MOUSES SUPPORT (Tooltips)

    public final void mouseDragged(MouseEvent e) {
    }

    public final void mouseMoved(MouseEvent e) {
        Point ePoint = e.getPoint();
        int row = -1;
        int col = -1;
        if (e.getSource() == getTableHeader()) {
            col = getTableHeader().columnAtPoint(ePoint);
            ToolTipManager.sharedInstance().setDismissDelay(defaultTooltipDismissDelay);
            String colTitle = "";
            if (col > -1) {
                colTitle = getModel().getColumnName(col);
            }
            getTableHeader().setToolTipText(colTitle);
            return;
        }

        row = rowAtPoint(ePoint);
        col = columnAtPoint(ePoint);
        if (row < 0 || col < 0) {
            ToolTipManager.sharedInstance().setDismissDelay(defaultTooltipDismissDelay);
            setToolTipText("");
            return;
        }
        Object value = getValueAt(row, col);
        String colheader = this.getColumnName(col).toString();
        String text = "";
        text += "<html>"; // NOT LOCALIZABLE
        text += "<b>" + colheader + "</b><br>"; // NOT LOCALIZABLE

        if (value == null || value instanceof Image) {
            ListTableModel model = getListTableModel();
            ToolTipManager.sharedInstance().setDismissDelay(defaultTooltipDismissDelay);
            setToolTipText(text + "</html>"); // NOT LOCALIZABLE
            return;
        }

        String sValue = value.toString();
        if (sValue.indexOf("\n") < 0) { // NOT LOCALIZABLE
            text += sValue;
            ToolTipManager.sharedInstance().setDismissDelay(defaultTooltipDismissDelay);
        } else {
            StringTokenizer st = new StringTokenizer(sValue, ",.©®(;:?!<>& \n\"", true); // NOT LOCALIZABLE
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
                    text += "<br>"; // NOT LOCALIZABLE
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
        setToolTipText(text);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
        ToolTipManager.sharedInstance().setDismissDelay(defaultTooltipDismissDelay);
    }

    void uninstall() {
        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
        preferences.removePropertyChangeListener(ListTable.class, SHOW_HORIZONTAL_GRID_PROPERTY,
                preferencesChangeListener);
        preferences.removePropertyChangeListener(ListTable.class, SHOW_VERTICAL_GRID_PROPERTY,
                preferencesChangeListener);
    }

}
