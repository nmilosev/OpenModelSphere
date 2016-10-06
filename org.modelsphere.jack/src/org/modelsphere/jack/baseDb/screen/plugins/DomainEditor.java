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

package org.modelsphere.jack.baseDb.screen.plugins;

import javax.swing.JComboBox;

import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.srtypes.Domain;
import org.modelsphere.jack.baseDb.screen.ScreenView;
import org.modelsphere.jack.baseDb.screen.model.*;

public class DomainEditor extends JComboBox implements org.modelsphere.jack.baseDb.screen.Editor {

    protected Domain oldValue;
    protected DbObject dbo;

    public java.awt.Component getTableCellEditorComponent(ScreenView screenView,
            org.modelsphere.jack.awt.AbstractTableCellEditor tableCellEditorListener, Object value,
            boolean isSelected, int row, int column) {
        oldValue = (Domain) value;
        ScreenModel model = screenView.getModel();
        dbo = (DbObject) model.getParentValue(row);

        /*
         * Normally the domain class is given by the data type of the field. But there is one
         * unfortunate exception: for Java visibility, the field type is OOVisibility, and the
         * domain to use is JVVisibility. To get around this problem, we take the data type of the
         * actual field value; we rely on the fact that the visiblity field is never null.
         * 
         * To avoid this problem in the future, do not subclass domains.
         */
        Domain domain = oldValue;
        if (domain == null) {
            Class domainClass = (model instanceof DbDescriptionModel ? ((DbDescriptionModel) model)
                    .getPropertyClass(row) : ((DbListModel) model).getColumnClass(column));
            domain = Domain.getAnyInstance(domainClass);
        }
        Domain[] values = domain.getObjectPossibleValues();
        for (int i = 0; i < values.length; i++)
            addItem(values[i]);
        String nullStr = domain.getStringForNullValue();
        if (nullStr != null)
            addItem(nullStr);
        setMaximumRowCount(getItemCount());

        if (oldValue != null)
            setSelectedItem(oldValue);
        else
            setSelectedIndex(nullStr != null ? getItemCount() - 1 : 0);

        addActionListener(tableCellEditorListener);
        return this;
    }

    public boolean stopCellEditing() {
        return true;
    }

    public final Object getCellEditorValue() {
        Object value = getSelectedItem();
        return (value instanceof Domain ? value : null);
    }

    @Override
    public int getClickCountForEditing() {
        return 2;
    }
}
