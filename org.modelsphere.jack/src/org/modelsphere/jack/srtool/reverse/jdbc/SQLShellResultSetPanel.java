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
package org.modelsphere.jack.srtool.reverse.jdbc;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

import org.modelsphere.jack.srtool.services.StatementMessage;

public final class SQLShellResultSetPanel extends JPanel {

    private static final int MAX_LINE_COUNT_IN_TOOLTIPS = 12;
    private static final int MAX_CHAR_COUNT_IN_TOOLTIPS = 800;
    private static final int MAX_CHAR_PER_LINE = 70;
    private static final int LONG_TEXT_TOOLTIPS_DELAY = 25000;

    private static final String AMP = "&amp;"; // NOT LOCALIZABLE - HTML tag
    private static final String LT = "&lt;"; // NOT LOCALIZABLE - HTML tag
    private static final String GT = "&gt;"; // NOT LOCALIZABLE - HTML tag
    private static final String QUOT = "&quot;"; // NOT LOCALIZABLE - HTML tag
    private static final String COPY = "&copy;"; // NOT LOCALIZABLE - HTML tag
    private static final String REG = "&reg;"; // NOT LOCALIZABLE - HTML tag
    private static final String NBSP = "&nbsp;"; // NOT LOCALIZABLE - HTML tag

    private int defaultTooltipDismissDelay = 0;

    private class ResultSetTable extends JTable implements MouseMotionListener, MouseListener {
        ResultSetTable(StatementMessage statementMessage) {
            super();
            TableModel model = new ResultSetTableModel(statementMessage);
            setModel(model);
            setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            setCellSelectionEnabled(false);
            setColumnSelectionAllowed(false);
            setRowSelectionAllowed(true);
            defaultTooltipDismissDelay = ToolTipManager.sharedInstance().getDismissDelay();
            addMouseMotionListener(this);
            addMouseListener(this);

        }

        // MOUSES SUPPORT (Tooltips)

        public final void mouseDragged(MouseEvent e) {
        }

        public final void mouseMoved(MouseEvent e) {
            Point ePoint = e.getPoint();
            int row = rowAtPoint(ePoint);
            int col = columnAtPoint(ePoint);
            if (row < 0 || col < 0) {
                ToolTipManager.sharedInstance().setDismissDelay(defaultTooltipDismissDelay);
                setToolTipText("");
                return;
            }
            Object value = getValueAt(row, col);
            String colheader = this.getColumnName(col).toString();
            String text = "";
            text += "<html>"; // NOT LOCALIZABLE
            text += "<b>" + colheader + "</b><br>"; // NOT LOCALIZABLE

            if (value == null) {
                ResultSetTableModel model = (ResultSetTableModel) getModel();
                ToolTipManager.sharedInstance().setDismissDelay(defaultTooltipDismissDelay);
                setToolTipText(text + "</html>"); // NOT LOCALIZABLE
                return;
            }

            String sValue = value.toString();
            if (sValue.indexOf("\n") < 0) { // NOT LOCALIZABLE
                text += sValue;
                ToolTipManager.sharedInstance().setDismissDelay(defaultTooltipDismissDelay);
            } else {
                StringTokenizer st = new StringTokenizer(sValue, ",.©®(;:?!<>& \n\"", true); // NOT LOCALIZABLE
                int count = 0;
                int linecount = 0;
                int charinline = 0;
                while (st.hasMoreTokens() && count < MAX_CHAR_COUNT_IN_TOOLTIPS
                        && linecount < MAX_LINE_COUNT_IN_TOOLTIPS) {
                    String token = st.nextToken();
                    if (token.equals("\n")) { // NOT LOCALIZABLE
                        text += "<br>"; // NOT LOCALIZABLE
                        linecount++;
                        charinline = 0;
                    } else if (token.equals("<")) {
                        text += LT;
                        charinline += 1;
                    } else if (token.equals(">")) {
                        text += GT;
                        charinline += 1;
                    } else if (token.equals("&")) {
                        text += AMP;
                        charinline += 1;
                    } else if (token.equals("\"")) {
                        text += QUOT;
                        charinline += 1;
                    } else if (token.equals("©")) {
                        text += COPY;
                        charinline += 1;
                    } else if (token.equals("®")) {
                        text += REG;
                        charinline += 1;
                    } else if (charinline >= MAX_CHAR_PER_LINE) { // NOT
                        // LOCALIZABLE
                        text += "<br>"; // NOT LOCALIZABLE
                        linecount++;
                        charinline = 0;
                        text += (token.equals(" ") ? NBSP : token);
                        charinline += token.length();
                    } else {
                        text += (token.equals(" ") ? NBSP : token);
                        charinline += token.length();
                    }
                    count += token.length();
                }
                if (count >= MAX_CHAR_COUNT_IN_TOOLTIPS || linecount >= MAX_LINE_COUNT_IN_TOOLTIPS)
                    text += "<br><b>...</b>"; // NOT LOCALIZABLE
                ToolTipManager.sharedInstance().setDismissDelay(LONG_TEXT_TOOLTIPS_DELAY);
            }

            text += "</html>"; // NOT LOCALIZABLE
            setToolTipText(text);
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
            ToolTipManager.sharedInstance().setDismissDelay(defaultTooltipDismissDelay);
        }
    }

    private class ResultSetTableModel extends DefaultTableModel {
        ArrayList columns = new ArrayList();
        ArrayList rows = new ArrayList();

        ResultSetTableModel(StatementMessage statementMessage) {
            if (statementMessage.resultList.size() < 1 || statementMessage.columnList.size() < 1) {
                return;
            }

            int count = statementMessage.columnList.size();
            for (int i = 0; i < count; i++) {
                columns.add(statementMessage.columnList.get(i));
            }

            count = statementMessage.resultList.size();
            for (int i = 0; i < count; i++) {
                rows.add(statementMessage.resultList.get(i));
            }

            fireTableStructureChanged();
            fireTableDataChanged();
        }

        public int getColumnCount() {
            if (columns == null)
                return 0;
            return columns.size();
        }

        public String getColumnName(int column) {
            if (columns == null)
                return "";
            Object title = columns.get(column);
            return title == null ? "" : title.toString();
        }

        public Object getValueAt(int row, int column) {
            if (rows == null)
                return "";
            ArrayList r = (ArrayList) rows.get(row);
            if (r.size() <= column)
                return "";
            return r.get(column);
        }

        public int getRowCount() {
            if (rows == null)
                return 0;
            return rows.size();
        }

        public void setValueAt(Object aValue, int row, int column) {
        }

        public boolean isCellEditable(int row, int column) {
            return false;
        }

    }

    SQLShellResultSetPanel(StatementMessage statementMessage) {
        super(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(new ResultSetTable(statementMessage));
        add(scrollPane, BorderLayout.CENTER);
    }

}
