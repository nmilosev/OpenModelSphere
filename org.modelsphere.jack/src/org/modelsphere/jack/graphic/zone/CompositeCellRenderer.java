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

import java.util.Vector;
import java.awt.*;

import org.modelsphere.jack.graphic.*;

/**
 * A CompositeCellRenderer is a renderer that use vector of other cellrenderer as an object data,
 * it's scan the vector of CompositeCellRendererElement an call the specifiy renderer on each
 * instance of the vector
 */
public class CompositeCellRenderer extends AbstractSelectionCellRenderer {

    // BEWARE: diagView is null for printing
    public final void paint(Graphics g, DiagramView diagView, Rectangle rect, Font font,
            Color color, int margin, int alignment, Cell cell) {
        Vector renderers = (Vector) cell.getPaintData();
        if (renderers == null)
            return;
        Rectangle subRect = new Rectangle(rect);
        subRect.x += margin;
        int count = renderers.size();
        for (int i = 0; i < count; i++) {
            CompositeCellRendererElement element = (CompositeCellRendererElement) renderers
                    .elementAt(i);
            subRect.width = element.getDataWidth();
            element.renderer.paint(g, diagView, subRect, font, color, 0, alignment, element);
            subRect.x += subRect.width;
        }
    }

    public final Dimension getDimension(Graphics g, Font font, int margin, Cell cell, int fixedWidth) {
        Vector renderers = (Vector) cell.getCellData();
        Dimension dim = new Dimension(0, 0);
        if (renderers != null) {
            dim.width = margin * 2;
            int count = renderers.size();
            for (int i = 0; i < count; i++) {
                CompositeCellRendererElement element = (CompositeCellRendererElement) renderers
                        .elementAt(i);
                Dimension d = element.renderer.getDimension(g, font, 0, element, 0);
                dim.width += d.width;
                if (dim.height < d.height)
                    dim.height = d.height;
            }
        }
        cell.setPaintData(renderers);
        cell.setDataWidth(dim.width);
        return dim;
    }

    public static class CompositeCellRendererElement implements Cell {

        private CellRenderer renderer;
        private Object data;
        private Object paintData = null; // data calculated by
        // renderer.getDimension to be
        // passed to renderer.paint
        private int dataWidth = 0; // calculated by renderer.getDimension

        public CompositeCellRendererElement(CellRenderer renderer, Object data) {
            this.renderer = renderer;
            this.data = data;
        }

        public final Object getCellData() {
            return data;
        }

        public final void setCellData(Object data) {
            this.data = data;
        }

        public final Object getPaintData() {
            return paintData;
        }

        public final void setPaintData(Object paintData) {
            this.paintData = paintData;
        }

        public final int getDataWidth() {
            return dataWidth;
        }

        public final void setDataWidth(int dataWidth) {
            this.dataWidth = dataWidth;
        }
    } // End of class CompositeCellRendererElement
}
