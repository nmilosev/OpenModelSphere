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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.GraphicComponent;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TableZone extends SingletonZone {
    private ArrayList m_cellList = new ArrayList();

    private ArrayList getCellList() {
        return m_cellList;
    }

    public TableZone(String name) {
        super(name);
    } // end TableZone()

    //
    // Overrides Zone
    // 
    public Rectangle getRectangle(Rectangle containerRect) {
        if (m_rectangle == null)
            return null;

        int x = m_rectangle.x;
        int y = m_rectangle.y;
        int width = (containerRect == null) ? m_rectangle.width : containerRect.width;
        int height = (containerRect == null) ? m_rectangle.height : containerRect.height - y;
        Rectangle rect = new Rectangle(x, y, width, height);
        return rect;
    } // end getRectangle()

    // BEWARE: diagView is null for printing
    public void paint(Graphics g, DiagramView diagView, int drawingMode, Rectangle rect,
            boolean drawSel, boolean bottomClipped) {
        computeSizeOfRowsAndColumns();

        Iterator iter = m_cellList.iterator();
        Rectangle zoomedRect = (diagView == null) ? rect : diagView.zoom(rect);
        while (iter.hasNext()) {
            TableCell cell = (TableCell) iter.next();
            cell.paint(g, zoomedRect, m_rowHeights, m_colWidths);
        } // end while

        if (diagView != null) {
            diagView.invalidate();
        } // end if
    } // end paint()

    public int computePositionData(Graphics g, int top, int fixedWidth, int fixedHeight) {
        return super.computePositionData(g, top, fixedWidth, fixedHeight);
    } // end computePositionData()

    //
    // Other public methods
    //

    public void add(TableCell cell) {
        m_cellList.add(cell);
    } // end addCell()

    private double[] m_rowHeights = null;
    private double[] m_colWidths = null;

    public double[] getRowHeights() {
        return m_rowHeights;
    }

    public double[] getColWidths() {
        return m_colWidths;
    }

    // Called by BEUtility.computeCellDimensions()
    public void computeSizeOfRowsAndColumns() {
        Dimension dim = computeNbRowsAndColumns();
        m_rowHeights = new double[dim.height];
        m_colWidths = new double[dim.width];

        Iterator iter = m_cellList.iterator();
        while (iter.hasNext()) {
            TableCell cell = (TableCell) iter.next();
            int x = cell.getX();
            int y = cell.getY();
            double xw = cell.getXW();
            double yw = cell.getYW();
            m_rowHeights[y] = Math.max(m_rowHeights[y], yw);
            m_colWidths[x] = Math.max(m_colWidths[x], xw);
        } // end while

        double totalWidth = sum(m_colWidths);
        double totalHeight = sum(m_rowHeights);
    } // end computeSizeOfRowsAndColumns()

    public BoundaryInfoStruct isOverCellBoundary(int coorX, int coorY, Rectangle frameRect,
            DiagramView diagView) {
        BoundaryInfoStruct boundaryInfo = null;
        Rectangle zoomedFrame = (diagView == null) ? frameRect : diagView.zoom(frameRect);
        if ((m_rowHeights == null) || (m_colWidths == null)) {
            computeSizeOfRowsAndColumns();
        }

        Iterator iter = m_cellList.iterator();
        while (iter.hasNext()) {
            TableCell cell = (TableCell) iter.next();
            int x = cell.getX();
            int y = cell.getY();

            Rectangle cellRect = TableCell.computeCellRectangle(zoomedFrame, x, y, m_rowHeights,
                    m_colWidths);
            if ((x > 0) && almostEqual(cellRect.x, coorX)) {
                boundaryInfo = new BoundaryInfoStruct(this, x, y, GraphicComponent.LEFT_BORDER);
                break;
            }

            if ((y > 0) && almostEqual(cellRect.y, coorY)) {
                boundaryInfo = new BoundaryInfoStruct(this, x, y, GraphicComponent.TOP_BORDER);
                break;
            }
        } // end while

        return boundaryInfo;
    } // end isOverCellBoundary()

    //
    // private methods
    //
    private static final int GAP = 8;

    private static boolean almostEqual(int a, int b) {
        int diff = Math.abs(a - b);

        return (diff <= GAP) ? true : false;
    }

    private Dimension computeNbRowsAndColumns() {
        int maxX = -1;
        int maxY = -1;
        Iterator iter = m_cellList.iterator();
        while (iter.hasNext()) {
            TableCell cell = (TableCell) iter.next();
            int x = cell.getX();
            int y = cell.getY();
            if (x > maxX)
                maxX = x;

            if (y > maxY)
                maxY = y;
        } // end while

        Dimension dim = new Dimension(maxX + 1, maxY + 1);
        return dim;
    } // end computeNbRowsAndColumns()

    private static double sum(double[] array) {
        double total = 0.0;
        int nb = array.length;
        for (int i = 0; i < nb; i++) {
            total += array[i];
        }
        return total;
    } // end computeNbRowsAndColumns()

    //
    // inner class
    //
    public static final class BoundaryInfoStruct {
        private BoundaryInfoStruct(TableZone zn, int x, int y, int b) {
            zone = zn;
            xPos = x;
            yPos = y;
            boundary = b;
        }

        public int xPos; // the X position of the cell
        public int yPos; // the Y position of the cell
        public int boundary; // which cell boundary among (GraphicComponent.TOP,
        // BOTTOM, LEFT, RIGHT}
        private Point mousePosition = null;
        private TableZone zone;

        public void setMousePosition(int x, int y) {
            mousePosition = new Point(x, y);
        }

        public Point getMousePosition() {
            return mousePosition;
        }

        public void resizeCells() {
            Iterator iter = zone.getCellList().iterator();
            while (iter.hasNext()) {
                TableCell cell = (TableCell) iter.next();
                // cell.
            } // end while
        } // end resizeCells()

    } // end BoundaryInfoStruct
} // end TableZone
