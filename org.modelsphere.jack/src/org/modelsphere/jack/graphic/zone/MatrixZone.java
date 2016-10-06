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

import java.util.*;
import java.awt.*;

import org.modelsphere.jack.graphic.*;

public class MatrixZone extends Zone {
    private int m_zoneMinimumHeight;
    private ArrayList rows = new ArrayList();
    private ArrayList colOptions = new ArrayList();
    private int alignment;
    private int wrappingCol = -1; // no wrapping col
    private int[] widths;
    private int[] heights;

    public MatrixZone(String newNameID) {
        this(newNameID, GraphicUtil.LEFT_ALIGNMENT, 0);
    }

    public MatrixZone(String newNameID, int newAlignment) {
        this(newNameID, newAlignment, 0);
    }

    public MatrixZone(String newNameID, int newAlignment, int zoneMinimumHeight) {
        super(newNameID);
        alignment = newAlignment;
        m_zoneMinimumHeight = zoneMinimumHeight;
    }

    public final int getAligment() {
        return alignment;
    }

    public final void setWrappingCol(int col) {
        wrappingCol = col;
    }

    public final int getRowCount() {
        return rows.size();
    }

    public final int getColumnCount() {
        return colOptions.size();
    }

    public final ZoneCell get(int row, int col) {
        return ((ZoneCell[]) rows.get(row))[col];
    }

    public final void set(int row, int col, ZoneCell data) {
        ZoneCell[] cells = (ZoneCell[]) rows.get(row);
        cells[col] = data;
    }

    public final void addColumn(ColumnCellsOption option) {
        if (getRowCount() != 0)
            throw new RuntimeException("MatrixZone: cannot add a column after adding rows"); // NOT
        // LOCALIZABLE
        // RuntimeException
        colOptions.add(option);
    }

    public final void addRow() {
        cancelEditorInZone();
        int cnt = getColumnCount();
        ZoneCell[] cells = new ZoneCell[cnt];
        rows.add(cells);
    }

    public final void addRow(int row) {
        cancelEditorInZone();
        int cnt = getColumnCount();
        ZoneCell[] cells = new ZoneCell[cnt];
        rows.add(row, cells);
    }

    public final void removeRow(int row) {
        cancelEditorInZone();
        rows.remove(row);
    }

    public final void removeAllRows() {
        cancelEditorInZone();
        rows.clear();
    }

    public final void clear() {
        removeAllRows();
        colOptions.clear();
    }

    // If there is an edition currently opened in this zone, cancel it.
    private void cancelEditorInZone() {
        ZoneBox b = getBox();
        if (b != null) {
            Diagram diagram = b.getDiagram();
            CellID editorCell = (diagram == null) ? null : diagram.getEditorCell();
            if (editorCell != null && editorCell.zone == this)
                diagram.removeEditor(CellEditor.CANCEL);
        }
    }

    // compute the position data independantly of the zone box size.
    public final int computePositionData(Graphics g, int top, int fixedWidth, int fixedHeight) {
        int rowCount = getRowCount();
        heights = new int[rowCount];
        int colCount = getColumnCount();
        widths = new int[colCount];
        int row, col;
        for (row = 0; row < rowCount; row++) {
            for (col = 0; col < colCount; col++) {
                if (col != wrappingCol)
                    computeCellDimension(g, row, col, 0);
            }
        }
        int totalWidth = 0;
        for (col = 0; col < colCount; col++)
            totalWidth += widths[col];
        if (wrappingCol != -1) {
            if (fixedWidth != 0)
                fixedWidth = Math.max(1, fixedWidth - totalWidth);
            for (row = 0; row < rowCount; row++)
                computeCellDimension(g, row, wrappingCol, fixedWidth);
            totalWidth += widths[wrappingCol];
        }
        int totalHeight = 0;
        for (row = 0; row < rowCount; row++)
            totalHeight += heights[row];

        if (totalHeight == 0)
            totalHeight = m_zoneMinimumHeight;

        m_rectangle = new Rectangle(0, top, totalWidth, totalHeight);
        return top + totalHeight;
    }

    private void computeCellDimension(Graphics g, int row, int col, int fixedWidth) {
        ZoneCell value = get(row, col);

        if (value == null) {
            return;
        } // Return to avoid a NullPointerException

        CellRenderingOption option = value.getCellRenderingOption();
        if (option == null)
            option = ((ColumnCellsOption) colOptions.get(col)).cellOption;

        if (option == null) // if option is still null, return to avoid a
            // NullPointerException [MS]
            return;

        Dimension dim = null;
        CellRenderer renderer = option.getCellRenderer();
        dim = renderer.getDimension(g, option.getFont(), option.getMargin(), value, fixedWidth);

        if (heights[row] < dim.height)
            heights[row] = dim.height;
        if (widths[col] < dim.width)
            widths[col] = dim.width;
    }

    // BEWARE: diagView is null for printing
    public final void paint(Graphics g, DiagramView diagView, int drawingMode, Rectangle rect,
            boolean drawSel, boolean bottomClipped) {
        if (!drawSel)
            paintBackground(g, diagView, drawingMode, rect, bottomClipped);
        if (drawingMode == GraphicComponent.DRAW_FRAME)
            return;
        Rectangle cellRect = new Rectangle(rect);
        int left = rect.x
                + GraphicUtil.getAlignmentOffset(alignment, rect.width, m_rectangle.width);
        float stretchFactor = (alignment == GraphicUtil.JUSTIFY && rect.width > m_rectangle.width ? (float) rect.width
                / m_rectangle.width
                : 1.0f);
        int rowCount = getRowCount();
        int colCount = getColumnCount();
        for (int row = 0; row < rowCount; row++) {
            int x = left;
            cellRect.height = (row < heights.length) ? heights[row] : 0;
            for (int col = 0; col < colCount; col++) {
                int width = (int) (widths[col] * stretchFactor);
                ZoneCell value = get(row, col);
                CellRenderingOption option = value.getCellRenderingOption();
                if (option == null)
                    option = ((ColumnCellsOption) colOptions.get(col)).cellOption;
                CellRenderer renderer = option.getCellRenderer();
                if (drawSel) {
                    if (value.isSelected()) {
                        cellRect.x = x;
                        cellRect.width = width;
                        if (option.isFocusRectangleAllowed())
                            renderer.paintSelection(g, diagView, cellRect);
                    }
                } else {
                    cellRect.width = value.getDataWidth();
                    cellRect.x = x
                            + GraphicUtil.getAlignmentOffset(option.getAlignment(), width,
                                    cellRect.width);
                    Color color = (option.getColor() != null ? option.getColor() : getTextColor());
                    if (!option.isFocusRectangleAllowed())
                        g.setPaintMode(); // this ensure that the clip rectangle
                    // is not drawn and text is original
                    // color !
                    renderer.paint(g, diagView, cellRect, option.getFont(), color, option
                            .getMargin(), option.getAlignment(), value);
                }
                x += width;
            }
            int height = (row < heights.length) ? heights[row] : 0;
            cellRect.y += height;
            if (cellRect.y >= rect.y + rect.height)
                break; // the rest of the zone is clipped.
        }
    }

    public final void paintBackground(Graphics g, DiagramView diagView, int drawingMode,
            Rectangle rect, boolean bottomClipped) {
        super.paintBackground(g, diagView, drawingMode, rect, bottomClipped);

        g.setColor(getForeColor());
        int x = rect.x + GraphicUtil.getAlignmentOffset(alignment, rect.width, m_rectangle.width);
        float stretchFactor = (alignment == GraphicUtil.JUSTIFY && rect.width > m_rectangle.width ? (float) rect.width
                / m_rectangle.width
                : 1.0f);
        float zoomFactor = (diagView != null ? diagView.getZoomFactor() : 1.0f);
        int top = (int) (rect.y * zoomFactor);
        int bottom = (int) ((rect.y + rect.height) * zoomFactor);
        int colCount = getColumnCount();
        for (int col = 0; col < colCount; col++) {
            if (((ColumnCellsOption) colOptions.get(col)).hasLeftLine) {
                int zoomX = (int) (x * zoomFactor);
                g.drawLine(zoomX, top, zoomX, bottom);
            }
            x += (int) (widths[col] * stretchFactor);
        }
    }

    public final CellID cellAt(int x, int y, int width) {
        float stretchFactor = 1.0f;
        if (alignment == GraphicUtil.JUSTIFY) {
            if (width > m_rectangle.width)
                stretchFactor = (float) width / m_rectangle.width;
        } else {
            x -= GraphicUtil.getAlignmentOffset(alignment, width, m_rectangle.width);
            if (x < 0 || x >= m_rectangle.width)
                return null;
        }
        int lastRow = getRowCount() - 1;
        int lastCol = getColumnCount() - 1;
        int row, col;
        for (row = 0; row < lastRow; row++) {
            y -= heights[row];
            if (y < 0)
                break;
        }
        for (col = 0; col < lastCol; col++) {
            x -= (int) (widths[col] * stretchFactor);
            if (x < 0)
                break;
        }
        return new MatrixCellID(this, row, col);
    }

    public final Rectangle getCellRect(CellID cellId, Rectangle zonesRect) {
        int row = ((MatrixCellID) cellId).row;
        int col = ((MatrixCellID) cellId).col;
        int i;
        int x = zonesRect.x
                + GraphicUtil.getAlignmentOffset(alignment, zonesRect.width, m_rectangle.width);
        float stretchFactor = (alignment == GraphicUtil.JUSTIFY
                && zonesRect.width > m_rectangle.width ? (float) zonesRect.width
                / m_rectangle.width : 1.0f);
        for (i = 0; i < col; i++)
            x += (int) (widths[i] * stretchFactor);
        int y = zonesRect.y + m_rectangle.y;
        for (i = 0; i < row; i++)
            y += heights[i];
        return new Rectangle(x, y, (int) (widths[col] * stretchFactor), heights[row]);
    }

    public final CellEditor getCellEditor(CellID cellID) {
        ZoneCell value = getCell(cellID);
        if (!value.isEditable())
            return null;
        CellEditor editor = value.getCellEditor();
        if (editor == null)
            editor = ((ColumnCellsOption) colOptions.get(((MatrixCellID) cellID).col)).editor;
        return editor;
    }

    public final void setCellEditor(CellID cellID, CellEditor cellEditor) {
        getCell(cellID).setCellEditor(cellEditor);
    }

    public final ZoneCell getCell(CellID cellId) {
        if (rows.size() != 0)
            return get(((MatrixCellID) cellId).row, ((MatrixCellID) cellId).col);
        else
            return null;
    }

    public final void getSelectedCells(Vector selCells) {
        int rowCount = getRowCount();
        int colCount = getColumnCount();
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                ZoneCell value = get(i, j);
                if (value != null) {
                    if (value.isSelected()) {
                        selCells.addElement(new MatrixCellID(this, i, j));
                    } // end if
                } // end if
            } // end for
        } // end for
    } // end getSelectedCells()

    public final MatrixCellID getCellID(Object object, int col) {
        int rowCount = getRowCount();
        for (int row = 0; row < rowCount; row++) {
            ZoneCell zoneCell = get(row, col);
            if (zoneCell == null)
                return null;
            if (zoneCell.getObject() == object)
                return new MatrixCellID(this, row, col);
        }
        return null;
    }
}
