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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.swing.JLabel;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import org.modelsphere.jack.international.LocaleChangeManager;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.screen.design.DesignPanel;
import org.modelsphere.jack.srtool.screen.design.DesignTableModel;

/**
 * @author nicolask
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences -
 *         Java - Code Style - Code Templates
 */
public class MultiTimestampRenderer extends DefaultTableCellRenderer {

    public static final MultiTimestampRenderer singleton = new MultiTimestampRenderer();
    public static final String DATETIME_FORMAT;

    static {
        String path = ApplicationContext.getApplicationDirectory();
        if (LocaleChangeManager.getLocale().getLanguage().compareTo("fr") == 0)
            DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
        else
            DATETIME_FORMAT = "MM/dd/yyyy HH:mm:ss";
    }

    protected MultiTimestampRenderer() {
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        if (value != null) {
            DateFormat df = new SimpleDateFormat(DATETIME_FORMAT);
            df.setTimeZone(TimeZone.getDefault());
            setText(df.format((Long) value));
        } else
            setText("");

        setHorizontalAlignment(JLabel.LEFT);
        TableModel model = table.getModel();

        boolean valuesDiffer = model instanceof DesignTableModel ? ((DesignTableModel) model)
                .isValuesDiffer(row) : false;
        if (valuesDiffer && !isSelected) {
            setForeground(table.getForeground());
            setBackground(DesignPanel.DIFFER_NO_FOCUS_COLOR);
        } else if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }

        return this;
    }

}
