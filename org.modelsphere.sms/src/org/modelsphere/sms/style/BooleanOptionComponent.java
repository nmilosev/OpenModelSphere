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
import org.modelsphere.sms.international.LocaleMgr;

public class BooleanOptionComponent extends OptionComponent {

    private JTable table;
    private static final String PROPERTY = LocaleMgr.screen.getString("Property");
    private static final String VALUE = LocaleMgr.screen.getString("Value");

    public BooleanOptionComponent(StyleComponent StyleComponent, MetaField[] metaFields) {
        super(StyleComponent, metaFields);
        table = new JTable(new BooleanModel());
        table.setRowSelectionAllowed(false);
        optionPanel = new JScrollPane(table);
    }

    public final void setStyle(DbObject style, boolean refresh) throws DbException {
        super.setStyle(style, refresh);
        ((AbstractTableModel) table.getModel()).fireTableDataChanged();
    }

    class BooleanModel extends AbstractTableModel {

        public String getColumnName(int col) {
            return (col == 0 ? PROPERTY : VALUE);
        }

        public Class getColumnClass(int col) {
            return (col == 0 ? String.class : Boolean.class);
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
        	Object value; 
        	
        	if (col == 0) {
        		MetaField mf =  metaFields[row];
        		value = mf.getGUIName();
        	} else {
        		value = values[row];
        	}
        	
            return value;
        }
    }
}
