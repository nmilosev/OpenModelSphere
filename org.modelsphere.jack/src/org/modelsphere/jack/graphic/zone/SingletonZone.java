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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Vector;

import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.GraphicUtil;

public class SingletonZone extends Zone {

    protected ZoneCell m_singleZone = null;
    protected CellRenderingOption cellOption = new CellRenderingOption(new StringCellRenderer(),
            GraphicUtil.CENTER_ALIGNMENT);

    public SingletonZone(String newNameID) {
        super(newNameID);
    }

    public final void setValue(ZoneCell newValue) {
        m_singleZone = newValue;
    }

    public final ZoneCell getValue() {
        return m_singleZone;
    }

    public final void setFont(Font newFont) {
        cellOption.setFont(newFont);
    }

    public final Font getFont() {
        return cellOption.getFont();
    }

    public final void setAlignment(int alignment) {
        cellOption.setAlignment(alignment);
    }

    public final int getAlignment() {
        return cellOption.getAlignment();
    }

    public final void setCellRenderer(CellRenderer renderer) {
        cellOption.setCellRenderer(renderer);
    }

    // compute the position data independantly of the zone box size.
    public int computePositionData(Graphics g, int top, int fixedWidth, int fixedHeight) {
        if (m_singleZone == null)
            m_rectangle = new Rectangle(0, top, 0, 0);
        else {
            CellRenderingOption option = m_singleZone.getCellRenderingOption();
            if (option == null)
                option = cellOption;
            CellRenderer renderer = option.getCellRenderer();
            Dimension dim = renderer.getDimension(g, option.getFont(), option.getMargin(),
                    m_singleZone, fixedWidth);
            m_rectangle = new Rectangle(0, top, dim.width, dim.height);
        } // end if

        int position = top + m_rectangle.height;
        return position;
    } // end computePositionData()

    // BEWARE: diagView is null for printing
    public void paint(Graphics g, DiagramView diagView, int drawingMode, Rectangle rect,
            boolean drawSel, boolean bottomClipped) {
        if (!drawSel)
            paintBackground(g, diagView, drawingMode, rect, bottomClipped);
        if (drawingMode == GraphicComponent.DRAW_FRAME)
            return;
        if (m_singleZone == null)
            return;
        Rectangle cellRect = new Rectangle(rect);
        CellRenderingOption option = m_singleZone.getCellRenderingOption();
        if (option == null)
            option = cellOption;
        CellRenderer renderer = option.getCellRenderer();
        if (drawSel) {
            if (m_singleZone.isSelected()) {
                if (option.isFocusRectangleAllowed())
                    renderer.paintSelection(g, diagView, cellRect);
            }
        } else {
            cellRect.width = m_singleZone.getDataWidth();
            cellRect.x = rect.x
                    + GraphicUtil.getAlignmentOffset(option.getAlignment(), rect.width,
                            cellRect.width);
            Color color = (option.getColor() != null ? option.getColor() : getTextColor());
            renderer.paint(g, diagView, cellRect, option.getFont(), color, option.getMargin(),
                    option.getAlignment(), m_singleZone);
        }
    }

    public final CellID cellAt(int x, int y, int width) {
        return new SingletonCellID(this);
    }

    public final Rectangle getCellRect(CellID cellId, Rectangle zonesRect) {
        return new Rectangle(zonesRect.x, zonesRect.y + m_rectangle.y, zonesRect.width,
                m_rectangle.height);
    }

    public final CellEditor getCellEditor(CellID cellID) {
        if (!m_singleZone.isEditable())
            return null;
        else
            return m_singleZone.getCellEditor();
    }

    public final void setCellEditor(CellID cellID, CellEditor cellEditor) {
        m_singleZone.setCellEditor(cellEditor);
    }

    public final ZoneCell getCell(CellID cellId) {
        return m_singleZone;
    }

    public final void getSelectedCells(Vector selCells) {
        if (m_singleZone != null && m_singleZone.isSelected())
            selCells.addElement(new SingletonCellID(this));
    } // end getSelectedCells()
} // end SingletonZone()
