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

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.modelsphere.sms.plugins.report.LocaleMgr;

public class DefaultRenderer extends JLabel implements Renderer {

    public static final DefaultRenderer singleton = new DefaultRenderer();
    private static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    public final static String kUnspecified = LocaleMgr.misc.getString("unspecified");
    public final static String kNone = LocaleMgr.misc.getString("none");

    //private static Border noFocusBorder = new EmptyBorder(1, 2, 1, 2);

    protected DefaultRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        /*
         * Since the renderer is reusable, we must reinitialize it each time.
         */
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            /* setBackground(screenView.getBackground()); */
            setBackground(Color.white);
        }

        //setFont(screenView.getFont());

        if (hasFocus) {
            //setBorder( UIManager.getBorder("Table.focusCellHighlightBorder") );//NOT LOCALIZABLE
            if (table.isCellEditable(row, column)) {
                setForeground(table.getSelectionForeground()/*
                                                             * UIManager.getColor("Table.focusCellForeground"
                                                             * )
                                                             */);//NOT LOCALIZABLE
                setBackground(table.getSelectionBackground()/*
                                                             * UIManager.getColor("Table.focusCellBackground"
                                                             * )
                                                             */);//NOT LOCALIZABLE
            }
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder")); // NOT LOCALIZABLE
        } else
            setBorder(noFocusBorder);

        setIcon(null);
        setText("");
        setToolTipText(null);

        setValue(table, row, column, value);

        return this;
    }

    protected void setValue(JTable table, int row, int column, Object value) {
        if (value == null)
            return;
        String text = value.toString();
        if (text.length() == 0)
            return;
        setText(text);
        setToolTipText(text);
    }

    public int getDisplayWidth() {
        return 80;
    }
}
