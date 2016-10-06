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

package org.modelsphere.sms.plugins.report.screen;

// Swing
import javax.swing.table.AbstractTableModel;

import org.modelsphere.sms.plugins.report.model.Property;
import org.modelsphere.sms.plugins.report.model.PropertyGroup;

// Sms

public class PropertiesTableModel extends AbstractTableModel {

    //String[] column1Values;
    //Object[] column2Values;
    private Property[] properties;

    public PropertiesTableModel(PropertyGroup group) {
        properties = group.properties;

        /*
         * column1Values = new String[properties.length]; column2Values = new
         * Object[properties.length];
         * 
         * for(int i=0 ; i<properties.length ; i++){ column1Values[i] = properties[i].toString();
         * column2Values[i] = properties[i].getValue(); }
         */
    }

    public int getRowCount() {
        return properties.length;//column1Values.length;
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getValueAt(int row, int column) {
        if (column == 0)
            return properties[row].toString();//column1Values[row];

        if (column == 1)
            return properties[row].getValue();//column2Values[row];

        return null;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // columnIndex should be 2
        //column2Values[rowIndex] = aValue;
        properties[rowIndex].setValue(aValue);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 1)
            return true;

        return false;
    }

    public final String getColumnName(int col) {
        return (col == 0 ? "Property" : "Value");
        //return (col == 0 ? LocaleMgr.screen.getString("Property") : LocaleMgr.screen.getString("Value"));
    }
}
