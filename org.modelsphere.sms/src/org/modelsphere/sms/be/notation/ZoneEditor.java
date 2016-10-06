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

package org.modelsphere.sms.be.notation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.modelsphere.jack.awt.ThinBevelBorder;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.DbUDF;
import org.modelsphere.jack.baseDb.db.srtypes.ZoneJustification;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.SemanticalModel;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.be.BEModule;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBESingleZoneDisplay;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.srtypes.BEZoneStereotype;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.db.srtypes.SMSZoneOrientation;

public final class ZoneEditor extends JPanel implements ActionListener {
    private static final int HORIZONTAL_ORIENTATION = 0;
    private static final int VERTICAL_ORIENTATION = 1;

    private static final int FLOW_MAX_ZONE_COUNT = 4; // Graphic restriction

    private static final int INNACTIVE_TRANSPARENCY_COLOR_LEVEL = 80; // 0-255

    private static final Icon UP = GraphicUtil.loadIcon(BEModule.class,
            "international/resources/zone_move_up.gif");
    private static final Icon DOWN = GraphicUtil.loadIcon(BEModule.class,
            "international/resources/zone_move_down.gif");
    private static final Icon ADD = GraphicUtil.loadIcon(BEModule.class,
            "international/resources/zone_add.gif");
    private static final Icon REMOVE = GraphicUtil.loadIcon(BEModule.class,
            "international/resources/zone_remove.gif");

    /*
     * static{ GraphicUtil.waitForImage();
     * 
     * 
     * }
     */

    // Cell GUI settings
    private Color selectedForeground = UIManager.getColor("Table.selectionForeground");
    private Color selectedBackground = UIManager.getColor("Table.selectionBackground");
    private Color foreground = UIManager.getColor("Table.foreground");
    private Color inactiveForeground = null;
    private Color background = UIManager.getColor("Table.background");
    private Font font = UIManager.getFont("Table.font");

    private DbBENotation notation;
    private boolean builtIn = false;
    private BEZoneStereotype stereotype;

    private JPanel editor = new JPanel(new ZoneLayout());
    private JButton addB = new JButton(ADD);
    private JButton removeB = new JButton(REMOVE);
    private JButton upB = new JButton(UP);
    private JButton downB = new JButton(DOWN);

    private JLabel fieldsL = new JLabel(LocaleMgr.screen.getString("field"));
    private JComboBox fieldsC = new JComboBox();
    private JCheckBox separatorC = new JCheckBox(LocaleMgr.screen.getString("separator"));
    private JCheckBox activeC = new JCheckBox(LocaleMgr.screen.getString("active"));
    private JLabel justificationL = new JLabel(LocaleMgr.screen.getString("justification"));
    private JComboBox justificationC = new JComboBox();
    private JLabel positionL = new JLabel(LocaleMgr.screen.getString("location"));
    private JRadioButton topR = new JRadioButton(LocaleMgr.screen.getString("top"));
    private JRadioButton leftR = new JRadioButton(LocaleMgr.screen.getString("left"));

    private Cell selectedCell = null;

    private boolean init = false;

    private SelectionListener selectionListener = new SelectionListener();

    ZoneEditor() {
        initGUI();
    }

    private class SelectionListener implements MouseListener {
        SelectionListener() {
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e))
                return;
            setSelectedAt(e.getPoint());
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }

    private class SeparatorBorder implements Border {
        int orientation;

        SeparatorBorder(int orientation) {
            this.orientation = orientation;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Color oldColor = g.getColor();
            Color color = Color.black;
            boolean active = ((Cell) c).active;
            boolean separator = ((Cell) c).separator;
            if (!separator || !active)
                color = new Color(color.getRed(), color.getGreen(), color.getBlue(),
                        INNACTIVE_TRANSPARENCY_COLOR_LEVEL);
            g.setColor(color);
            g.translate(x, y);
            if (orientation == HORIZONTAL_ORIENTATION) {
                g.drawLine(0, height - 1, width - 1, height - 1);
                g.drawLine(0, height - 2, width - 1, height - 2);
            } else {
                g.drawLine(width - 1, 0, width - 1, height - 1);
                g.drawLine(width - 2, 0, width - 2, height - 1);
            }
            g.translate(-x, -y);
            g.setColor(oldColor);
        }

        public Insets getBorderInsets(Component c) {
            return orientation == HORIZONTAL_ORIENTATION ? new Insets(0, 0, 3, 0) : new Insets(0,
                    0, 0, 3);
        }

        public boolean isBorderOpaque() {
            return false;
        }
    }

    private class Cell extends JLabel {
        private boolean selected = false;
        private boolean separator = true;
        private boolean separatorEnabled = true;
        private boolean active = true;
        SelectedZoneElement zoneElement;
        int orientation = 0;
        ZoneJustification justification = null;
        boolean first = false;

        Cell(SelectedZoneElement zoneElement, boolean separatorEnabled) {
            this.zoneElement = zoneElement;
            setBackground(background);
            setForeground(foreground);
            if (inactiveForeground == null)
                inactiveForeground = new Color(foreground.getRed(), foreground.getGreen(),
                        foreground.getBlue(), INNACTIVE_TRANSPARENCY_COLOR_LEVEL);
            setOpaque(true);
            setFont(font);
            first = (zoneElement.index == 0);
            setOrientation(zoneElement.orientation);
            setActive(zoneElement.active);
            setText(zoneElement.name);
            setJustification(zoneElement.justification);
            setSeparator(zoneElement.separator);
            if (separatorEnabled)
                setBorder(new SeparatorBorder(orientation));
        }

        public void setJustification(ZoneJustification justification) {
            if (this.justification == justification)
                return;
            this.justification = justification;
            if (justification.equals(ZoneJustification.getInstance(ZoneJustification.LEFT)))
                setHorizontalAlignment(SwingConstants.LEFT);
            else if (justification.equals(ZoneJustification.getInstance(ZoneJustification.CENTER)))
                setHorizontalAlignment(SwingConstants.CENTER);
            else if (justification.equals(ZoneJustification.getInstance(ZoneJustification.RIGHT)))
                setHorizontalAlignment(SwingConstants.RIGHT);
        }

        public void setActive(boolean active) {
            if (this.active == active)
                return;
            this.active = active;
            setForeground(selected ? selectedForeground
                    : (active ? foreground : inactiveForeground));
        }

        public void setOrientation(int orientation) {
            if (this.orientation == orientation)
                return;
            this.orientation = orientation;
            if (separatorEnabled)
                setBorder(new SeparatorBorder(orientation));
        }

        public void setSeparator(boolean separator) {
            if (this.separator == separator)
                return;
            this.separator = separator;
        }

        public void setSelected(boolean selected) {
            if (this.selected == selected)
                return;
            this.selected = selected;
            setBackground(selected ? selectedBackground : background);
            setForeground(selected ? selectedForeground
                    : (active ? foreground : inactiveForeground));
        }

    }

    private class ZoneLayout implements LayoutManager {
        ZoneLayout() {
        }

        public void addLayoutComponent(String name, Component comp) {
        }

        public void removeLayoutComponent(Component comp) {
        }

        public Dimension preferredLayoutSize(Container parent) {
            return new Dimension(200, 300);
        }

        public Dimension minimumLayoutSize(Container parent) {
            return new Dimension(100, 100);
        }

        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                Dimension dim = parent.getSize();
                Insets insets = parent.getInsets();
                Component[] components = parent.getComponents();
                if (components == null || components.length == 0)
                    return;
                Cell firstCell = (Cell) components[0];
                int col0width = 0;
                int col1width = 0;
                int cellheight = 0;
                int x = insets.left;
                int y = insets.top;
                if (firstCell.orientation == VERTICAL_ORIENTATION) {
                    col0width = (dim.width - insets.left - insets.right) * 30 / 100;
                    col1width = (dim.width - insets.left - insets.right) - col0width;
                    cellheight = components.length > 1 ? (dim.height - insets.top - insets.bottom)
                            / (components.length - 1) : (dim.height - insets.top - insets.bottom);
                    firstCell.setBounds(x, y, col0width, dim.height - insets.top - insets.bottom);
                    x += col0width;
                } else {
                    col1width = (dim.width - insets.left - insets.right);
                    cellheight = components.length > 0 ? (dim.height - insets.top - insets.bottom)
                            / components.length : (dim.height - insets.top - insets.bottom);
                    firstCell.setBounds(x, y, col1width, cellheight);
                    y += cellheight;
                }

                for (int i = 1; i < components.length; i++) {
                    Cell cell = (Cell) components[i];
                    if (i == components.length - 1)
                        cell.setBounds(x, y, col1width, dim.height - insets.bottom - y); // give remainding space
                    else
                        cell.setBounds(x, y, col1width, cellheight);
                    y += cellheight;
                }
            }
        }
    }

    private class SelectedZoneElement implements Comparable, ZoneElement {
        DbBESingleZoneDisplay zone;
        String name;
        Object element;
        boolean active = false;
        boolean separator = false;
        ZoneJustification justification;
        int orientation = HORIZONTAL_ORIENTATION;
        int index = -1;

        SelectedZoneElement(DbBESingleZoneDisplay zone, int index, int orientation)
                throws DbException {
            this.zone = zone;
            element = zone.getSourceObject();
            MetaField metafield = zone.getMetaField();
            if (metafield != null)
                name = ApplicationContext.getSemanticalModel().getDisplayText(
                        metafield.getMetaClass(), metafield, ZoneEditor.class);
            else
                name = zone.getGUIName();
            active = zone.isDisplayed();
            separator = zone.isSeparator();
            justification = zone.getJustification();
            this.index = index;
            this.orientation = orientation;
        }

        public Object getElement() {
            return element;
        }

        public boolean equals(Object o) {
            if (!(o instanceof SelectedZoneElement) && !(o instanceof AvailableZoneElement))
                return false;
            if (o instanceof SelectedZoneElement)
                return ((SelectedZoneElement) o).element == this.element;
            return ((AvailableZoneElement) o).element == this.element;
        }

        public int compareTo(Object o) {
            if (!(o instanceof SelectedZoneElement))
                return 100;
            return this.index - ((SelectedZoneElement) o).index;
        }

        public String toString() {
            return name;
        }
    }

    private static interface ZoneElement {
        Object getElement();
    }

    private class AvailableZoneElement implements Comparable, ZoneElement {
        String name;
        Object element;

        AvailableZoneElement(Object element, MetaClass metaclass) throws DbException {
            this.element = element;
            if (element instanceof MetaField)
                name = ApplicationContext.getSemanticalModel().getDisplayText(metaclass,
                        (MetaField) element, ZoneEditor.class);
            else
                name = ((DbUDF) element).getName();
        }

        public Object getElement() {
            return element;
        }

        public int compareTo(Object o) {
            if (!(o instanceof AvailableZoneElement))
                return 100;
            return this.name.compareTo(((AvailableZoneElement) o).name);
        }

        public boolean equals(Object o) {
            if (!(o instanceof AvailableZoneElement) && !(o instanceof SelectedZoneElement))
                return false;
            if (o instanceof SelectedZoneElement)
                return ((SelectedZoneElement) o).element == this.element;
            return ((AvailableZoneElement) o).element == this.element;
        }

        public String toString() {
            return name;
        }
    }

    private void initGUI() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder(new ThinBevelBorder(ThinBevelBorder.LOWERED),
                LocaleMgr.screen.getString("Zones")));
        editor.setBackground(UIManager.getColor("Table.background"));
        editor.setBorder(new CompoundBorder(new LineBorder(Color.black, 1), new EmptyBorder(3, 3,
                3, 3)));

        fieldsC.setEditable(false);

        ButtonGroup group = new ButtonGroup();
        group.add(topR);
        group.add(leftR);

        addB.setToolTipText(LocaleMgr.screen.getString("Add"));
        removeB.setToolTipText(LocaleMgr.screen.getString("Remove"));
        upB.setToolTipText(LocaleMgr.screen.getString("MoveUp"));
        downB.setToolTipText(LocaleMgr.screen.getString("MoveDown"));
        addB.setMargin(new Insets(1, 1, 1, 1));
        removeB.setMargin(new Insets(1, 1, 1, 1));
        upB.setMargin(new Insets(1, 1, 1, 1));
        downB.setMargin(new Insets(1, 1, 1, 1));

        JPanel zoneInfoPanel = new JPanel(new GridBagLayout());
        JPanel buttonPanel = new JPanel(new GridBagLayout());

        buttonPanel.add(addB, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(0, 12, 12, 6), 0, 0));
        buttonPanel.add(removeB, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 12, 6), 0, 0));
        buttonPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 12, 0), 0, 0));
        buttonPanel.add(upB, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(0, 0, 12, 6), 0, 0));
        buttonPanel.add(downB, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 12, 6), 0, 0));

        zoneInfoPanel.add(fieldsL, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 9, 6), 0, 0));
        zoneInfoPanel.add(fieldsC, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 6, 0), 0,
                0));
        zoneInfoPanel.add(justificationL, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 6, 6), 0, 0));
        zoneInfoPanel.add(justificationC, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 6, 0), 0,
                0));
        zoneInfoPanel.add(activeC, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 6, 0), 0, 0));
        zoneInfoPanel.add(separatorC, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 6, 0), 0, 0));
        zoneInfoPanel.add(positionL, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 0), 0, 0));
        zoneInfoPanel.add(topR, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 0), 0, 0));
        zoneInfoPanel.add(leftR, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        zoneInfoPanel.add(Box.createVerticalGlue(), new GridBagConstraints(0, 6, 2, 1, 0.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        add(editor, new GridBagConstraints(0, 0, 1, 1, 0.6, 1.0, GridBagConstraints.EAST,
                GridBagConstraints.BOTH, new Insets(12, 12, 6, 6), 10, 0));
        add(buttonPanel, new GridBagConstraints(0, 1, 1, 1, 0.6, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        add(zoneInfoPanel, new GridBagConstraints(2, 0, 1, 2, 0.4, 1.0, GridBagConstraints.EAST,
                GridBagConstraints.BOTH, new Insets(12, 6, 12, 12), 10, 0));

        justificationC.setModel(new DefaultComboBoxModel(
                (Object[]) ZoneJustification.objectPossibleValues.clone()));
        updateFields();
        editor.addMouseListener(selectionListener);
        topR.addActionListener(this);
        leftR.addActionListener(this);
        activeC.addActionListener(this);
        separatorC.addActionListener(this);
        justificationC.addActionListener(this);
        fieldsC.addActionListener(this);
        addB.addActionListener(this);
        removeB.addActionListener(this);
        upB.addActionListener(this);
        downB.addActionListener(this);
    }

    void initValues(DbBENotation notation, BEZoneStereotype stereotype) throws DbException {
        this.notation = notation;
        this.stereotype = stereotype;
        builtIn = notation.isBuiltIn();
        init = true;
        loadSelected();
        loadAvailable();
        selectedCell = null;
        updateFields();
        init = false;
    }

    private void loadSelected() throws DbException {
        editor.removeAll();
        ArrayList listvalues = new ArrayList();
        ArrayList zones = null;
        int orientation = 0;
        SMSZoneOrientation horizontalOrientation = SMSZoneOrientation
                .getInstance(SMSZoneOrientation.HORIZONTAL);

        if (stereotype.equals(BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR))) {
            zones = notation.getActorZones();
            SMSZoneOrientation currentActorOrientation = notation.getActorZoneOrientation();
            if (currentActorOrientation == null)
                orientation = HORIZONTAL_ORIENTATION;
            else
                orientation = currentActorOrientation.equals(horizontalOrientation) ? HORIZONTAL_ORIENTATION
                        : VERTICAL_ORIENTATION;
        } else if (stereotype.equals(BEZoneStereotype.getInstance(BEZoneStereotype.FLOW))) {
            zones = notation.getFlowZones();
            orientation = HORIZONTAL_ORIENTATION;
        } else if (stereotype.equals(BEZoneStereotype.getInstance(BEZoneStereotype.STORE))) {
            zones = notation.getStoreZones();
            SMSZoneOrientation currentStoreOrientation = notation.getStoreZoneOrientation();
            if (currentStoreOrientation == null)
                orientation = VERTICAL_ORIENTATION;
            else
                orientation = currentStoreOrientation.equals(horizontalOrientation) ? HORIZONTAL_ORIENTATION
                        : VERTICAL_ORIENTATION;
        } else {
            zones = notation.getUseCaseZones();
            SMSZoneOrientation currentUseCaseOrientation = notation.getUseCaseZoneOrientation();
            if (currentUseCaseOrientation == null)
                orientation = HORIZONTAL_ORIENTATION;
            else
                orientation = currentUseCaseOrientation.equals(horizontalOrientation) ? HORIZONTAL_ORIENTATION
                        : VERTICAL_ORIENTATION;
        }

        Iterator iter = zones.iterator();
        for (int i = 0; i < zones.size(); i++) {
            DbBESingleZoneDisplay zone = (DbBESingleZoneDisplay) zones.get(i);
            listvalues.add(new SelectedZoneElement(zone, i, i == 0 ? orientation
                    : HORIZONTAL_ORIENTATION));
        }
        Collections.sort(listvalues);
        iter = listvalues.iterator();
        while (iter.hasNext()) {
            Cell cell = new Cell((SelectedZoneElement) iter.next(), !stereotype
                    .equals(BEZoneStereotype.getInstance(BEZoneStereotype.FLOW)));
            editor.add(cell);
        }
    }

    private void loadAvailable() throws DbException {
        ArrayList listvalues = new ArrayList();
        MetaClass metaclass = null;
        if (stereotype.equals(BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR)))
            metaclass = DbBEActor.metaClass;
        else if (stereotype.equals(BEZoneStereotype.getInstance(BEZoneStereotype.FLOW)))
            metaclass = DbBEFlow.metaClass;
        else if (stereotype.equals(BEZoneStereotype.getInstance(BEZoneStereotype.STORE)))
            metaclass = DbBEStore.metaClass;
        else
            metaclass = DbBEUseCase.metaClass;

        ArrayList zones = new ArrayList();
        try {
            Class claz = metaclass.getJClass();
            Field field = (Field) claz.getField("notationFields"); // NOT LOCALIZABLE
            MetaField[] mfields = (MetaField[]) field.get(null);
            // MetaField[] fields = (MetaField[])metaclass.getJClass().getField("notationFields").get(null);
            zones.addAll(Arrays.asList(mfields));
        } catch (Exception e) {
            //
        } //end try

        DbEnumeration dbEnum = notation.getProject().getComponents().elements(DbUDF.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbUDF udf = (DbUDF) dbEnum.nextElement();
            if (udf.getUDFMetaClass() == metaclass)
                zones.add(udf);
        }
        dbEnum.close();

        Iterator iter = zones.iterator();
        int index = -1;
        SemanticalModel model = ApplicationContext.getSemanticalModel();
        while (iter.hasNext()) {
            Object element = iter.next();
            if (element instanceof MetaField
                    && !model.isVisibleOnScreen(metaclass, (MetaField) element, null,
                            ZoneEditor.class))
                continue;
            AvailableZoneElement zoneElement = new AvailableZoneElement(element, metaclass);
            listvalues.add(zoneElement);
        }
        Collections.sort(listvalues);
        fieldsC.setModel(new DefaultComboBoxModel(listvalues.toArray()));
    }

    private DbBESingleZoneDisplay[] getZones() throws DbException {
        ArrayList zonelist = new ArrayList();
        DbEnumeration dbEnum = notation.getComponents().elements(DbBESingleZoneDisplay.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBESingleZoneDisplay zone = (DbBESingleZoneDisplay) dbEnum.nextElement();
            if (zone.getStereotype() != null && zone.getStereotype().equals(stereotype))
                zonelist.add(zone);
        }

        DbBESingleZoneDisplay[] zones = new DbBESingleZoneDisplay[zonelist.size()];
        for (int i = 0; i < zones.length; i++)
            zones[i] = (DbBESingleZoneDisplay) zonelist.get(i);

        return zones;
    }

    private void setSelectedAt(Point p) {
        Component comp = editor.getComponentAt(p);
        Cell cell = null;
        if (comp instanceof Cell)
            cell = (Cell) comp;
        setSelectedCell(cell);
    }

    private void setSelectedCell(Cell newSelectedCell) {
        if (selectedCell == newSelectedCell)
            return;
        selectedCell = newSelectedCell;
        Component[] components = editor.getComponents();
        for (int i = 0; i < components.length; i++) {
            Cell cell = (Cell) components[i];
            if (cell.selected && cell == selectedCell)
                return;
            cell.setSelected(cell == selectedCell);
        }
        editor.grabFocus();
        init = true;
        updateFields();
        init = false;
        editor.repaint();
    }

    private void updateFields() {
        updateAdd();
        updateUpDown();
        if (stereotype != null
                && stereotype.equals(BEZoneStereotype.getInstance(BEZoneStereotype.FLOW))) {
            topR.setVisible(false);
            leftR.setVisible(false);
            positionL.setVisible(false);
            separatorC.setVisible(false);
        } else {
            topR.setVisible(true);
            leftR.setVisible(true);
            positionL.setVisible(true);
            separatorC.setVisible(true);
        }
        if (builtIn) {
            fieldsC.setEnabled(false);
            separatorC.setEnabled(false);
            activeC.setEnabled(false);
            justificationC.setEnabled(false);
            topR.setEnabled(false);
            leftR.setEnabled(false);
            //addB.setEnabled(false);
            removeB.setEnabled(false);
            return;
        }
        //addB.setEnabled(true);
        if (selectedCell == null) {
            fieldsC.setEnabled(false);
            separatorC.setEnabled(false);
            activeC.setEnabled(false);
            justificationC.setEnabled(false);
            topR.setEnabled(false);
            leftR.setEnabled(false);
            removeB.setEnabled(false);
        } else {
            removeB.setEnabled(true);
            fieldsC.setEnabled(true);
            separatorC.setEnabled(true);
            activeC.setEnabled(true);
            justificationC.setEnabled(true);

            fieldsC.setSelectedItem(selectedCell.zoneElement);
            separatorC.setSelected(selectedCell.separator);
            activeC.setSelected(selectedCell.active);
            justificationC.setSelectedItem(selectedCell.justification);

            if (selectedCell.first) {
                topR.setEnabled(true);
                leftR.setEnabled(true);
                if (selectedCell.orientation == HORIZONTAL_ORIENTATION)
                    topR.setSelected(true);
                else
                    leftR.setSelected(true);
            } else {
                topR.setEnabled(false);
                leftR.setEnabled(false);
            }
        }
    }

    private void updateAdd() {
        if (stereotype == null || editor == null)
            return;
        if (builtIn
                || (stereotype.equals(BEZoneStereotype.getInstance(BEZoneStereotype.FLOW)) && (editor
                        .getComponentCount() >= FLOW_MAX_ZONE_COUNT))) {
            addB.setEnabled(false);
            upB.setEnabled(false);
            downB.setEnabled(false);
        } else {
            addB.setEnabled(true);
        }
    }

    private void updateUpDown() {
        if (builtIn || selectedCell == null) {
            upB.setEnabled(false);
            downB.setEnabled(false);
            return;
        }
        downB.setEnabled(editor.getComponent(editor.getComponentCount() - 1) != selectedCell);
        upB
                .setEnabled((editor.getComponentCount() > 0)
                        && (editor.getComponent(0) != selectedCell));
    }

    public void actionPerformed(ActionEvent e) {
        if (init)
            return;
        Object source = e.getSource();
        try {
            int index = -1;
            Db db = notation.getDb();
            db.beginWriteTrans(LocaleMgr.misc.getString("zoneUpdate"));
            if (source == addB) {
                DbBESingleZoneDisplay zone = new DbBESingleZoneDisplay(notation,
                        DbSemanticalObject.fName, true, ZoneJustification
                                .getInstance(ZoneJustification.CENTER), true, stereotype);
                index = editor.getComponentCount();
            } else if (source == removeB) {
                selectedCell.zoneElement.zone.remove();
            } else if (source == topR) {
                if (stereotype.equals(BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR)))
                    notation.setActorZoneOrientation(SMSZoneOrientation
                            .getInstance(SMSZoneOrientation.HORIZONTAL));
                else if (stereotype.equals(BEZoneStereotype.getInstance(BEZoneStereotype.STORE)))
                    notation.setStoreZoneOrientation(SMSZoneOrientation
                            .getInstance(SMSZoneOrientation.HORIZONTAL));
                else if (stereotype.equals(BEZoneStereotype.getInstance(BEZoneStereotype.USECASE)))
                    notation.setUseCaseZoneOrientation(SMSZoneOrientation
                            .getInstance(SMSZoneOrientation.HORIZONTAL));
                index = indexOf(selectedCell);
            } else if (source == leftR) {
                if (stereotype.equals(BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR)))
                    notation.setActorZoneOrientation(SMSZoneOrientation
                            .getInstance(SMSZoneOrientation.PERPENDICULAR));
                else if (stereotype.equals(BEZoneStereotype.getInstance(BEZoneStereotype.STORE)))
                    notation.setStoreZoneOrientation(SMSZoneOrientation
                            .getInstance(SMSZoneOrientation.PERPENDICULAR));
                else if (stereotype.equals(BEZoneStereotype.getInstance(BEZoneStereotype.USECASE)))
                    notation.setUseCaseZoneOrientation(SMSZoneOrientation
                            .getInstance(SMSZoneOrientation.PERPENDICULAR));
                index = indexOf(selectedCell);
            } else if (source == fieldsC) {
                ZoneElement field = (ZoneElement) fieldsC.getSelectedItem();
                DbBESingleZoneDisplay zone = selectedCell.zoneElement.zone;
                if (field != null) {
                    if (field.getElement() instanceof MetaField) {
                        zone.setMetaField((MetaField) field.getElement());
                        zone.setUdf(null);
                    } else {
                        zone.setUdf((DbUDF) field.getElement());
                        zone.setMetaField(null);
                    }
                    index = indexOf(selectedCell);
                }
            } else if (source == separatorC) {
                DbBESingleZoneDisplay zone = selectedCell.zoneElement.zone;
                zone.setSeparator(separatorC.isSelected() ? Boolean.TRUE : Boolean.FALSE);
                index = indexOf(selectedCell);
            } else if (source == activeC) {
                DbBESingleZoneDisplay zone = selectedCell.zoneElement.zone;
                zone.setDisplayed(activeC.isSelected() ? Boolean.TRUE : Boolean.FALSE);
                index = indexOf(selectedCell);
            } else if (source == justificationC) {
                DbBESingleZoneDisplay zone = selectedCell.zoneElement.zone;
                int selidx = justificationC.getSelectedIndex();
                zone.setJustification(ZoneJustification.objectPossibleValues[selidx]);
                index = indexOf(selectedCell);
            } else if (source == upB) {
                int actualindex = indexOf(selectedCell);
                if (actualindex > 0) { // cannot move 0 (top)
                    // we cannot use this index as index in the notation components because notation may contains other object's type
                    Cell refComponent = (Cell) editor.getComponent(actualindex - 1);
                    int newindexinnotation = notation.getComponents().indexOf(
                            refComponent.zoneElement.zone);
                    int oldindexinnotation = notation.getComponents().indexOf(
                            selectedCell.zoneElement.zone);
                    notation.reinsert(DbObject.fComponents, oldindexinnotation, newindexinnotation);
                    index = actualindex - 1;
                }
            } else if (source == downB) {
                int actualindex = indexOf(selectedCell);
                if (actualindex < (editor.getComponentCount() - 1)) { // cannot move down last component
                    // we cannot use this index as index in the notation components because notation may contains other object's type
                    Cell refComponent = (Cell) editor.getComponent(actualindex + 1);
                    int newindexinnotation = notation.getComponents().indexOf(
                            refComponent.zoneElement.zone);
                    int oldindexinnotation = notation.getComponents().indexOf(
                            selectedCell.zoneElement.zone);
                    notation.reinsert(DbObject.fComponents, oldindexinnotation, newindexinnotation);
                    index = actualindex + 1;
                }
            }
            initValues(notation, stereotype);
            notation.getDb().commitTrans();
            if (index == -1)
                setSelectedCell(null);
            else
                setSelectedCell((Cell) editor.getComponents()[index]);
            editor.repaint();
        } catch (Exception ex) {
            ExceptionHandler.processUncatchedException(this, ex);
        }
    }

    private int indexOf(Cell cell) {
        if (cell == null)
            return -1;
        Component[] comp = editor.getComponents();
        for (int i = 0; i < comp.length; i++) {
            if (cell == comp[i])
                return i;
        }
        return -1;
    }
}
