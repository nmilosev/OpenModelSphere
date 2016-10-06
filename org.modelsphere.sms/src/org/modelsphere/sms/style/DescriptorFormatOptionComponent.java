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

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.or.db.srtypes.ORDescriptorFormat;

public class DescriptorFormatOptionComponent extends OptionComponent {

    private JTable table;
    private static final String CATEGORY = LocaleMgr.screen.getString("Category");
    private static final String BOLD = LocaleMgr.screen.getString("BoldFormat");
    private static final String ITALIC = LocaleMgr.screen.getString("ItalicFormat");
    private static final String UNDERLINE = LocaleMgr.screen.getString("UnderlineFormat");

    public DescriptorFormatOptionComponent(StyleComponent StyleComponent, MetaField[] metaFields) {
        super(StyleComponent, metaFields);
        table = new JTable(new DescriptorFormatModel());
        table.setRowSelectionAllowed(false);
        optionPanel = new JScrollPane(table);
    }

    public final void setStyle(DbObject style, boolean refresh) throws DbException {
        super.setStyle(style, refresh);
        ((AbstractTableModel) table.getModel()).fireTableDataChanged();
    }

    class DescriptorFormatModel extends AbstractTableModel {

        public String getColumnName(int col) {
            String columnName = null;
            switch (col) {
            case 0:
                columnName = CATEGORY;
                break;
            case 1:
                columnName = BOLD;
                break;
            case 2:
                columnName = ITALIC;
                break;
            case 3:
                columnName = UNDERLINE;
                break;
            default:
                Debug.trace("Column #" + Integer.toString(col)
                        + " is not managed in DescriptorFormatModel.getColumnName()"); // NOT
                // LOCALIZABLE
            }
            return columnName;
        }

        public Class getColumnClass(int col) {
            Class columnClass = null;
            switch (col) {
            case 0:
                columnClass = String.class;
                break;
            default:
                columnClass = Boolean.class;
            }
            return columnClass;
        }

        public boolean isCellEditable(int row, int col) {
            return (col == 1 || col == 2 || col == 3);
        }

        public void setValueAt(Object value, int row, int col) {
            ORDescriptorFormat oldValue = (ORDescriptorFormat) values[row];
            boolean bold = oldValue.isBold();
            boolean italic = oldValue.isItalic();
            boolean underline = oldValue.isUnderline();

            switch (col) {
            case 1:
                bold = ((Boolean) value).booleanValue();
                break;
            case 2:
                italic = ((Boolean) value).booleanValue();
                break;
            case 3:
                underline = ((Boolean) value).booleanValue();
                break;
            default:
                Debug.trace("Column #" + Integer.toString(col)
                        + " is not managed in DescriptorFormatModel.setValueAt()"); // NOT
                // LOCALIZABLE
            }
            setValue(new ORDescriptorFormat(bold, italic, underline), row);
        }

        public int getColumnCount() {
            return 4;
        }

        public int getRowCount() {
            return metaFields.length;
        }

        public Object getValueAt(int row, int col) {
            if (col == 0)
                return metaFields[row].getGUIName();
            ORDescriptorFormat value = (ORDescriptorFormat) values[row];
            boolean colValue = false;
            switch (col) {
            case 1:
                colValue = value.isBold();
                break;
            case 2:
                colValue = value.isItalic();
                break;
            case 3:
                colValue = value.isUnderline();
                break;
            default:
                Debug.trace("Column #" + Integer.toString(col)
                        + " is not managed in DescriptorFormatModel.getValueAt()"); // NOT
                // LOCALIZABLE
                return null;
            }
            return new Boolean(colValue);
        }
    }
}
