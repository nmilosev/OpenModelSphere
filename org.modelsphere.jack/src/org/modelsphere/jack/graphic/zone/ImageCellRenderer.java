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

import java.awt.*;

import org.modelsphere.jack.graphic.*;

public class ImageCellRenderer extends AbstractSelectionCellRenderer {

    // BEWARE: diagView is null for printing
    public final void paint(Graphics g, DiagramView diagView, Rectangle rect, Font font,
            Color color, int margin, int alignment, Cell cell) {
        Image image = (Image) cell.getPaintData();
        if (image == null)
            return;
        float zoomFactor = (diagView != null ? diagView.getZoomFactor() : 1.0f);
        g.drawImage(image, (int) ((rect.x + margin + GraphicUtil.GAP_FROM_BORDER) * zoomFactor),
                (int) (rect.y * zoomFactor), (int) (image.getWidth(null) * zoomFactor),
                (int) (image.getHeight(null) * zoomFactor), null);
    }

    public final Dimension getDimension(Graphics g, Font font, int margin, Cell cell, int fixedWidth) {
        Image image = (Image) cell.getCellData();
        Dimension dim = (image == null ? new Dimension(0, 0) : new Dimension(image.getWidth(null)
                + (margin + GraphicUtil.GAP_FROM_BORDER) * 2, image.getHeight(null)));
        cell.setPaintData(image);
        cell.setDataWidth(dim.width);
        return dim;
    }
}
