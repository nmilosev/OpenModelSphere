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

package org.modelsphere.sms.style;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import org.modelsphere.jack.awt.AbstractTableCellEditor;
import org.modelsphere.jack.awt.ColorIcon;
import org.modelsphere.jack.awt.JackColorChooser;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.sms.international.LocaleMgr;

public class ColorOptionComponent extends OptionComponent {

    private static final Dimension DIMENSION_COLOR = new Dimension(20, 10);
    private JTable table;
    private static final String CHOOSE_COLOR = LocaleMgr.screen.getString("ChooseColor");
    private static final String COLOR_FOR = LocaleMgr.screen.getString("ColorFor");
    private static final String COLOR = LocaleMgr.screen.getString("Color");

    public ColorOptionComponent(StyleComponent StyleComponent, MetaField[] metaFields) {
        super(StyleComponent, metaFields);
        jbInit();
    }

    public final void setStyle(DbObject style, boolean refresh) throws DbException {
        super.setStyle(style, refresh);
        ((AbstractTableModel) table.getModel()).fireTableDataChanged();
    }

    private void jbInit() {
        table = new JTable(new ColorModel());
        table.setRowSelectionAllowed(false);
        optionPanel = new JScrollPane(table);

        // Show colors with a lookup button.
        // /*
        AbstractTableCellEditor colorEditor = new AbstractTableCellEditor() {
            private Color newColor;

            public Component getTableCellEditorComponent(final JTable table, final Object value,
                    boolean isSelected, int row, int column) {
                Dimension dim;
                newColor = (Color) value;
                // remove alpha for Gui display
                Color guiColor = (newColor == null ? null : new Color(newColor.getRed(), newColor
                        .getGreen(), newColor.getBlue()));

                JPanel panel = new JPanel(new BorderLayout());
                JButton lookupBtn = new JButton("..."); // NOT LOCALIZABLE
                panel.add(lookupBtn, BorderLayout.CENTER);

                lookupBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        newColor = JackColorChooser.showDialog(table, CHOOSE_COLOR, (Color) value,
                                true);
                        if (newColor == null)
                            newColor = (Color) value;
                        stopCellEditing();
                    }
                });

                lookupBtn.setIcon(new ColorIcon(guiColor, 50, 12));
                // lookupBtn.setBackground(guiColor);
                // lookupBtn.setForeground(AwtUtil.getContrastBlackOrWhite((Color)value));

                return panel;
            }

            public Object getCellEditorValue() {
                return newColor;
            }
        };

        /* If called to get a tool tip, value is null */
        TableCellRenderer colorRenderer = new TableCellRenderer() {
            JPanel panel;
            java.awt.Dimension dim;
            JButton colorBtn;

            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                // remove alpha for Gui display
                Color guiColor = (value == null ? null : new Color(((Color) value).getRed(),
                        ((Color) value).getGreen(), ((Color) value).getBlue()));
                if (panel == null) {
                    panel = new JPanel(new BorderLayout());
                    colorBtn = new JButton("..."); // NOT LOCALIZABLE
                    colorBtn.setHorizontalAlignment(JLabel.CENTER);
                    panel.add(colorBtn, BorderLayout.CENTER);
                }
                colorBtn.setIcon(new ColorIcon(guiColor, 50, 12));
                return panel;
            }
        };

        table.setDefaultEditor(Color.class, colorEditor);
        table.setDefaultRenderer(Color.class, colorRenderer);
        table.setRowHeight(20);
    }

    class ColorModel extends AbstractTableModel {

        public String getColumnName(int col) {
            return (col == 0 ? COLOR_FOR : COLOR);
        }

        public Class getColumnClass(int col) {
            return (col == 0 ? String.class : Color.class);
        }

        public boolean isCellEditable(int row, int col) {
            return col == 1;
        }

        public void setValueAt(Object value, int row, int col) {
            setValue(value, row);
        }

        public int getColumnCount() {
            return 2;
        }

        public int getRowCount() {
            return metaFields.length;
        }

        public Object getValueAt(int row, int col) {
            if (col == 0)
                return metaFields[row].getGUIName();
            return values[row];
        }
    }
}
