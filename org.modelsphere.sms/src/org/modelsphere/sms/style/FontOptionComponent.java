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
import java.awt.Component;
import java.awt.Font;
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
import org.modelsphere.jack.awt.FontChooserDialog;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.sms.international.LocaleMgr;

public class FontOptionComponent extends OptionComponent {

    private JTable table;
    private static final String CHOOSE_FONT = LocaleMgr.screen.getString("ChooseFont");
    private static final String FONT_FOR = LocaleMgr.screen.getString("FontFor");
    private static final String FONT = LocaleMgr.screen.getString("Font");

    public FontOptionComponent(StyleComponent StyleComponent, MetaField[] metaFields) {
        super(StyleComponent, metaFields);
        jbInit();
    }

    public final void setStyle(DbObject style, boolean refresh) throws DbException {
        super.setStyle(style, refresh);
        ((AbstractTableModel) table.getModel()).fireTableDataChanged();
    }

    private void jbInit() {
        table = new JTable(new FontModel());
        table.setRowSelectionAllowed(false);
        optionPanel = new JScrollPane(table);
        // Show fonts with a lookup button.
        // /*
        AbstractTableCellEditor fontEditor = new AbstractTableCellEditor() {
            private Font newFont;

            public Component getTableCellEditorComponent(final JTable table, final Object value,
                    boolean isSelected, int row, int column) {
                newFont = (Font) value;
                JPanel panel = new JPanel(new BorderLayout());
                JButton lookupBtn = new JButton("...");
                JLabel label = new JLabel(fontToString((Font) value));
                panel.setBackground(table.getBackground());
                label.setFont(table.getFont());
                label.setForeground(table.getForeground());
                panel.add(new JLabel("  "), BorderLayout.WEST);
                panel.add(label, BorderLayout.CENTER);
                panel.add(lookupBtn, BorderLayout.EAST);
                lookupBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        FontChooserDialog fc = new FontChooserDialog(table, CHOOSE_FONT,
                                (Font) value);
                        fc.setVisible(true);
                        if (fc.getSelectedFont() != null) {
                            newFont = fc.getSelectedFont();
                        }
                        stopCellEditing();
                    }
                });
                return panel;
            }

            public Object getCellEditorValue() {
                return newFont;
            }
        };

        /* If called to get a tool tip, value is null */
        TableCellRenderer fontRenderer = new TableCellRenderer() {
            JPanel panel;
            JLabel label;
            JLabel labelSpace;

            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                if (panel == null) {
                    labelSpace = new JLabel("  ");
                    panel = new JPanel(new BorderLayout());
                    label = new JLabel();
                    panel.setBackground(table.getBackground());
                    panel.add(labelSpace, BorderLayout.WEST);
                    panel.add(label, BorderLayout.CENTER);
                    panel.add(new JButton("..."), BorderLayout.EAST);
                }
                label.setText(fontToString((Font) value));
                label.setFont(table.getFont());
                label.setForeground(table.getForeground());
                return panel;
            }
        };

        table.setDefaultEditor(Font.class, fontEditor);
        table.setDefaultRenderer(Font.class, fontRenderer);
        table.setRowHeight(20);
    }

    class FontModel extends AbstractTableModel {

        public String getColumnName(int col) {
            return (col == 0 ? FONT_FOR : FONT);
        }

        public Class getColumnClass(int col) {
            return (col == 0 ? String.class : Font.class);
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

    private String fontToString(Font f) {
        if (f == null)
            return "";
        String strStyle;

        if (f.isBold()) {
            strStyle = f.isItalic() ? "BoldItalic" : "Bold"; // NOT LOCALIZABLE
        } else {
            strStyle = f.isItalic() ? "Italic" : "Plain"; // NOT LOCALIZABLE
        }
        return f.getName() + ", " + LocaleMgr.screen.getString(strStyle) + ", " + f.getSize();
    }
}
