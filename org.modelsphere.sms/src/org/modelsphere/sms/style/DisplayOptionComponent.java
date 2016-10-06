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

import java.awt.Component;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import org.modelsphere.jack.awt.AbstractTableCellEditor;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.sms.db.srtypes.SMSDisplayDescriptor;
import org.modelsphere.sms.international.LocaleMgr;

public class DisplayOptionComponent extends OptionComponent {

    private static final int COL_PROPERTY = 0;
    private static final int COL_VALUE = 1;
    private static final String PROPERTY = LocaleMgr.screen.getString("Property");
    private static final String VALUE = LocaleMgr.screen.getString("Value");
    private static final String[] displayName = SMSDisplayDescriptor.stringPossibleValues;

    private JTable table;

    public DisplayOptionComponent(StyleComponent StyleComponent, MetaField[] metaFields) {
        super(StyleComponent, metaFields);
        jbInit();
    }

    public final void setStyle(DbObject style, boolean refresh) throws DbException {
        super.setStyle(style, refresh);
        ((AbstractTableModel) table.getModel()).fireTableDataChanged();
    }

    private void jbInit() {
        table = new JTable(new DisplayModel());
        table.setRowSelectionAllowed(false);
        optionPanel = new JScrollPane(table);

        AbstractTableCellEditor choiceEditor = new AbstractTableCellEditor() {
            private JComboBox combo = new JComboBox();
            {
                combo.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        fireEditingStopped();
                    }
                });

                combo.addItem(displayName[0]); // NOT_DISPLAYED
                combo.addItem(displayName[1]); // NAME
                combo.addItem(displayName[2]); // PHYSICAL_NAME
                combo.addItem(displayName[3]); // ALIAS
            }

            public Component getTableCellEditorComponent(final JTable table, final Object value,
                    boolean isSelected, int row, int column) {

                combo.setSelectedItem(((ChoiceCellData) value).value);
                return combo;
            }

            public Object getCellEditorValue() {
                return combo.getSelectedObjects()[0];
            }
        };

        TableCellRenderer choiceRenderer = new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                String s = ((ChoiceCellData) value).value;
                Component c = new JLabel(s);
                if (table != null) {
                    TableCellRenderer defaultRanderer = table.getDefaultRenderer(String.class);
                    if (defaultRanderer != null) {
                        Component defaultComp = defaultRanderer.getTableCellRendererComponent(
                                table, value, isSelected, hasFocus, row, column);
                        if (defaultComp != null) {
                            if (defaultComp instanceof Label) {
                                ((Label) defaultComp).setText(s);
                                c = defaultComp;
                            } else if (defaultComp instanceof JLabel) {
                                ((JLabel) defaultComp).setText(s);
                                c = defaultComp;
                            }
                        }
                    }
                }
                return c;
            }
        };

        table.setDefaultEditor(ChoiceCellData.class, choiceEditor);
        table.setDefaultRenderer(ChoiceCellData.class, choiceRenderer);
        table.setRowHeight(20);
    }

    class DisplayModel extends AbstractTableModel {

        public String getColumnName(int col) {
            return (col == COL_PROPERTY ? PROPERTY : VALUE);
        }

        public Class getColumnClass(int col) {
            if (col == COL_VALUE)
                return ChoiceCellData.class;
            else
                return String.class;
        }

        public boolean isCellEditable(int row, int col) {
            return col > COL_PROPERTY;
        }

        public void setValueAt(Object aValue, int row, int col) {
            if (col == COL_VALUE) {
                String s = (String) aValue;
                int newDisplayedSelected;
                if (s.equals(displayName[0])) // NOT_DISPLAYED
                    newDisplayedSelected = SMSDisplayDescriptor.NOT_DISPLAYED;
                else if (s.equals(displayName[1])) // NAME
                    newDisplayedSelected = SMSDisplayDescriptor.NAME;
                else if (s.equals(displayName[2])) // PHYSICAL_NAME
                    newDisplayedSelected = SMSDisplayDescriptor.PHYSICAL_NAME;
                else
                    // ALIAS
                    newDisplayedSelected = SMSDisplayDescriptor.ALIAS;
                setValue(SMSDisplayDescriptor.getInstance(newDisplayedSelected), row);
            }
        }

        public int getColumnCount() {
            return 2;
        }

        public int getRowCount() {
            return metaFields.length;
        }

        public Object getValueAt(int row, int col) {
            SMSDisplayDescriptor descriptor = (SMSDisplayDescriptor) values[row];
            if (col == COL_PROPERTY)
                return metaFields[row].getGUIName();
            else
                return new ChoiceCellData(descriptor.getValue());
        }
    }

    class ChoiceCellData {
        public String value;

        ChoiceCellData(int choiceValue) {
            switch (choiceValue) {
            case SMSDisplayDescriptor.NOT_DISPLAYED:
                value = displayName[0];
                break;
            case SMSDisplayDescriptor.NAME:
                value = displayName[1];
                break;
            case SMSDisplayDescriptor.PHYSICAL_NAME:
                value = displayName[2];
                break;
            case SMSDisplayDescriptor.ALIAS:
                value = displayName[3];
                break;
            }
        }
    }
}
