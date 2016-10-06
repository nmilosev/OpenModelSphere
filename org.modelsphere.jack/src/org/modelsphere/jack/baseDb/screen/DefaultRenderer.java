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

package org.modelsphere.jack.baseDb.screen;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.screen.model.DbDescriptionModel;
import org.modelsphere.jack.baseDb.screen.model.DbListModel;
import org.modelsphere.jack.baseDb.screen.model.ScreenModel;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.util.StringUtil;

/**
 * The default class for rendering (displaying) individual cells in a JTable.
 * <p>
 */

public class DefaultRenderer extends JLabel implements Renderer {

    public static final DefaultRenderer singleton = new DefaultRenderer();

    public final static String kUnspecified = LocaleMgr.misc.getString("unspecified");
    public final static String kNone = LocaleMgr.misc.getString("none");

    private static Border noFocusBorder = new EmptyBorder(1, 2, 1, 2);

    protected DefaultRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(ScreenView screenView, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        /*
         * Since the renderer is reusable, we must reinitialize it each time.
         */
        if (isSelected) {
            setForeground(screenView.getSelectionForeground());
            setBackground(screenView.getSelectionBackground());
        } else {
            setForeground(screenView.getForeground());
            setBackground(screenView.getBackground());
            // setBackground(Color.white);
        }

        setFont(screenView.getFont());

        if (hasFocus) {
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));// NOT
            // LOCALIZABLE
            if (screenView.isCellEditable(row, column)) {
                setForeground(UIManager.getColor("Table.focusCellForeground"));// NOT
                // LOCALIZABLE
                setBackground(UIManager.getColor("Table.focusCellBackground"));// NOT
                // LOCALIZABLE
            }
        } else {
            setBorder(noFocusBorder);
        }

        setIcon(null);
        setText("");
        setToolTipText(null);

        setValue(screenView, row, column, value);

        ScreenModel model = screenView.getModel();
        if (model instanceof DbDescriptionModel) {
            setEnabled(((DbDescriptionModel) model).isEditable(row));
        } else if (model instanceof DbListModel) {
            DbListModel dblm = (DbListModel) model;
            boolean editable = dblm.isCellEditable(row, column);
            boolean enabled = dblm.isCellEnabled(row, column);
            if (!enabled) {
                int i = 0;
            } else if (editable)
                setEnabled(editable);
        } else
            setEnabled(true);

        return this;
    }

    protected void setValue(ScreenView screenView, int row, int column, Object value) {
        if (value == null)
            return;

        String text = StringUtil.getDisplayString(value);
        if (text.length() == 0)
            return;

        setText(text);
        setToolTipText(text);
    }

    public Object wrapValue(DbObject dbo, Object value) throws DbException {
        return value;
    }

    public Object unwrapValue(Object value) {
        return value;
    }

    public int getDisplayWidth() {
        return 80;
    }
}
