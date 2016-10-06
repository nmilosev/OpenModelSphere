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

import java.awt.Component;
import javax.swing.JCheckBox;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.screen.model.*;

public class BooleanRenderer extends JCheckBox implements
        org.modelsphere.jack.baseDb.screen.Renderer {

    public static final BooleanRenderer singleton = new BooleanRenderer();

    protected BooleanRenderer() {
    }

    public final Component getTableCellRendererComponent(
            org.modelsphere.jack.baseDb.screen.ScreenView screenView, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setSelected(value != null && ((Boolean) value).booleanValue());
        setHorizontalAlignment(javax.swing.JLabel.CENTER);
        if (isSelected) {
            setBackground(screenView.getSelectionBackground());
            setForeground(screenView.getSelectionForeground());
        } else {
            // setBackground(Color.white);
            setBackground(screenView.getBackground());
            setForeground(screenView.getForeground());
        }

        ScreenModel model = screenView.getModel();
        if (model instanceof DbDescriptionModel) {
            setEnabled(((DbDescriptionModel) model).isEditable(row));
        } else if (model instanceof DbListModel) {
            setEnabled(((DbListModel) model).isCellEditable(row, column));
        } else
            setEnabled(true);

        return this;
    }

    public final Object wrapValue(DbObject dbo, Object value) throws DbException {
        return value;
    }

    public final Object unwrapValue(Object value) {
        return value;
    }

    public int getDisplayWidth() {
        return 40;
    }
}
