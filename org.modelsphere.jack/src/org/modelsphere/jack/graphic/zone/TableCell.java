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
package org.modelsphere.jack.graphic.zone;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import org.modelsphere.jack.graphic.GraphicComponent;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TableCell {
    // Justification constants, as defined in the .b2s file
    // DO NOT change their values, unless adapting the BPM application in sync.
    public static final int MIDDLE = 0;
    public static final int START = 1;
    public static final int END = 2;

    private int m_x;
    private int m_y;
    private double m_xw;
    private double m_yw;
    private boolean m_rightBorder;
    private boolean m_bottomBorder;
    private String m_text;
    private Font m_font;
    private int m_hJustification;
    private int m_vJustification;
    private boolean m_titleBoxed;
    private Color m_bgColor;
    private Color m_fgColor;
    private Color m_bxColor;
    private Color m_txColor;
    private boolean m_centerDisplay;

    public TableCell(int x, int y, double xw, double yw, boolean rightBorder, boolean bottomBorder,
            String text, Font font, int hJustification, int vJustification) {
        this(x, y, xw, yw, rightBorder, bottomBorder, text, font, hJustification, vJustification,
                false, Color.white, Color.black, Color.black, Color.black, false);
    } // end TableCell()

    public TableCell(int x, int y, double xw, double yw, boolean rightBorder, boolean bottomBorder,
            String text, Font font, int hJustification, int vJustification, boolean titleBoxed,
            Color bgColor, Color fgColor, Color bxColor, Color txColor, boolean centerDisplay) {
        m_x = x;
        m_y = y;
        m_xw = xw;
        m_yw = yw;
        m_rightBorder = rightBorder;
        m_bottomBorder = bottomBorder;
        m_text = text;
        m_font = font;
        m_hJustification = hJustification;
        m_vJustification = vJustification;
        m_titleBoxed = titleBoxed;
        m_bgColor = bgColor;
        m_fgColor = fgColor;
        m_bxColor = bxColor;
        m_txColor = txColor;
        m_centerDisplay = centerDisplay;
    } // end TableCell()

    int getX() {
        return m_x;
    }

    int getY() {
        return m_y;
    }

    double getXW() {
        return m_xw;
    }

    double getYW() {
        return m_yw;
    }

    // called by TableZone.paint()
    // all drawing must stay confined within the limits of zoomedFrame
    private static final int GAP = 8;

    public void paint(Graphics g, Rectangle zoomedFrame, double[] rowSizes, double[] colSizes) {
        // Compute cell boundaries
        Rectangle rect = computeCellRectangle(zoomedFrame, m_x, m_y, rowSizes, colSizes);
        int xPos = rect.x;
        int yPos = rect.y;
        xPos += m_centerDisplay ? (rect.width / 2) : rect.width;
        yPos += m_centerDisplay ? (rect.height / 2) : rect.height;

        // Keep current graphic values
        Graphics2D g2D = (Graphics2D) g;
        Stroke oldStroke = g2D.getStroke();
        Color oldBgColor = g2D.getBackground();
        Color oldFgColor = g2D.getColor();

        // set new values
        int lineStyle = m_centerDisplay ? GraphicComponent.LINE_DASHED
                : GraphicComponent.LINE_PLAIN;
        BasicStroke newStroke = GraphicComponent.lineStrokes[lineStyle];
        g2D.setStroke(newStroke);
        g2D.setColor(m_fgColor);

        // Draw cell boundaries
        if (m_rightBorder) {
            if ((m_centerDisplay == false) && (m_x == 0)) {
                g.drawLine(rect.x, rect.y, rect.x, rect.y + rect.height);
            }

            g.drawLine(xPos - 1, rect.y, xPos - 1, rect.y + rect.height);
        }

        if (m_bottomBorder) {
            if ((m_centerDisplay == false) && (m_y == 0)) {
                g.drawLine(rect.x, rect.y, rect.x + rect.width, rect.y);
            }

            g.drawLine(rect.x, yPos - 1, rect.x + rect.width, yPos - 1);
        }

        // Draw cell text, if any
        if ((m_text != null) && (m_text.length() > 0)) {
            Font previous = g.getFont();
            g.setFont(m_font); // font must be set BEFORE calling getPosition()
            int x = getPosition(g, 0, m_hJustification, rect.x, rect.width, m_text);
            int y = getPosition(g, 1, m_vJustification, rect.y, rect.height, m_text);

            if (m_titleBoxed) {
                // get box dimension
                FontMetrics metrics = g.getFontMetrics();
                Rectangle2D box = metrics.getStringBounds(m_text, g);
                int height = (int) box.getHeight();
                int width = (int) box.getWidth();

                // fill box
                g2D.setColor(m_bgColor);
                g.fillRect(x - GAP, y - height, width + (GAP * 2), height + GAP);

                // draw box
                newStroke = GraphicComponent.lineStrokes[GraphicComponent.LINE_PLAIN];
                g2D.setStroke(newStroke);
                g2D.setColor(m_bxColor);
                g.drawRect(x - GAP, y - height, width + (GAP * 2), height + GAP);
            } // end if

            g2D.setColor(m_txColor);
            g.drawString(m_text, x, y);
            g.setFont(previous);
        } // end if

        // restore values
        g2D.setStroke(oldStroke);
        g2D.setBackground(oldBgColor);
        g2D.setColor(oldFgColor);
    } // end paint()

    //
    // Utility methods
    //  
    // direction == 0 for horizontal, 1 for vertical
    private static int getPosition(Graphics g, int direction, int justification, int start,
            int size, String text) {
        int pos = 0;

        FontMetrics metrics = g.getFontMetrics();
        Rectangle2D rect = metrics.getStringBounds(text, g);
        int gap = (direction == 0) ? (int) rect.getWidth() : (int) rect.getHeight();

        switch (justification) {
        case START:
            pos = (direction == 0) ? start : start + gap;
            break;
        case MIDDLE:
            pos = start + (size / 2) - (gap / 2);
            break;
        case END:
            pos = (direction == 0) ? (start + size - gap) : (start + size);
            break;
        }

        return pos;
    } // end getXPosition()

    public static Rectangle computeCellRectangle(Rectangle frame, int x, int y,
            double[] rowHeights, double[] colWidths) {
        double totalWidth = sum(colWidths, colWidths.length);
        double totalHeight = sum(rowHeights, rowHeights.length);

        // relative widths and heights between 0.0 and 1.0
        double relWidth1 = sum(colWidths, x) / totalWidth;
        double relHeight1 = sum(rowHeights, y) / totalHeight;
        double relWidth2 = sum(colWidths, x + 1) / totalWidth;
        double relHeight2 = sum(rowHeights, y + 1) / totalHeight;

        int x1 = frame.x + (int) (relWidth1 * frame.width);
        int y1 = frame.y + (int) (relHeight1 * frame.height);

        int width = (int) (frame.width * (relWidth2 - relWidth1));
        int height = (int) (frame.height * (relHeight2 - relHeight1));

        Rectangle rect = new Rectangle(x1, y1, width, height);
        return rect;
    } // end computeCellRectangle()

    private static double sum(double[] array, int idx) {
        double total = 0.0;
        if (idx == 0)
            return total;

        for (int i = 1; i <= idx; i++) {
            total += array[i - 1];
        }
        return total;
    } // end sum()
} // end TableCell
