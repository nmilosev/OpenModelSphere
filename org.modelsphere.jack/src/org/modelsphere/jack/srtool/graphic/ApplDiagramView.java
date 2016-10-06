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

package org.modelsphere.jack.srtool.graphic;

import java.awt.*;

import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.graphic.*;
import org.modelsphere.jack.graphic.zone.CellID;
import org.modelsphere.jack.graphic.zone.Zone;
import org.modelsphere.jack.srtool.features.SrDropTarget;

public final class ApplDiagramView extends DiagramView implements SrDropTarget {

    private Rectangle dropRect = null; // highlighted rectangle for drop target
    private ZoneBox dropBox = null; // box and cell for object returned by getSemObjAtLocation
    private CellID dropCellID = null;

    public ApplDiagramView(ApplicationDiagram model) {
        super(model);
    }

    //////////////////////////////////////
    // SrDropTarget SUPPORT
    //
    public final DbObject getSemObjAtLocation(Point pt) {

        int x = (int) (pt.x / getZoomFactor());
        int y = (int) (pt.y / getZoomFactor());
        GraphicComponent gc = getModel().graphicAt(this, x, y, 1 << Diagram.LAYER_GRAPHIC, false);
        if (gc == null) {
            Diagram diag = this.getModel();
            if (diag instanceof ApplicationDiagram) {
                ApplicationDiagram ad = (ApplicationDiagram) diag;
                highlightCell(false, true);
                return ad.getDiagramGO();
            }
            return null;
        } else {
            if (!(gc instanceof ZoneBox)) {
                return null;
            }

            if (dropBox != null)
                if (!dropBox.equals((ZoneBox) gc)) {
                    highlightCell(false, true);
                    dropRect = null;
                    dropCellID = null;
                }

            dropBox = (ZoneBox) gc;

            dropCellID = dropBox.cellAt(this, x, y);
            if (dropCellID == null) {
                Zone zone = dropBox.getNameZone();
                if (zone != null)
                    dropCellID = zone.cellAt(0, 0, 0);
            }
            if (dropCellID == null) {
                return null;
            }

            if (dropBox.selectAtCellLevel())
                return (DbObject) dropBox.getCell(dropCellID).getObject();
            return ((ActionInformation) dropBox).getSemanticalObject();
        }
    }

    public final void highlightCell(boolean state) {
        highlightCell(state, false);
    }

    public final void highlightCell(boolean state, boolean leavingArea) {
        Rectangle newRect = null;

        if (dropBox == null)
            return;
        if (dropCellID == null)
            return;

        if (state) {
            newRect = dropBox.getCellRect(dropCellID);
            Rectangle rect = dropBox.getContentRect();
            newRect.x = rect.x;
            newRect.width = rect.width;
            newRect = zoom(newRect);

            if (dropRect != null)
                if (dropRect.equals(newRect))
                    return;

            Graphics2D g2 = (Graphics2D) getGraphics();
            if (g2 == null)
                return;

            if (dropRect != null) {
                dropRect.grow(1, 1);
                repaint(dropRect.x, dropRect.y + 1, dropRect.width, dropRect.height - 2);
            }
            if (newRect != null) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));
                g2.setColor(getBackground());
                g2.fillRect(newRect.x, newRect.y, newRect.width, newRect.height - 1);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.70f));
                g2.setColor(getForeground());
                g2.drawRect(newRect.x, newRect.y, newRect.width, newRect.height - 1);
            }
            g2.dispose();
            dropRect = newRect;
        } else {
            if (dropRect == null)
                return;

            if (leavingArea == true) {
                dropBox = null;
            }

            if (dropRect != null) {
                dropRect.grow(1, 1);
                repaint(dropRect.x, dropRect.y + 1, dropRect.width, dropRect.height - 2);
            }

            dropRect = null;
        }
    }
    //
    // End of SrDropTarget SUPPORT
    //////////////////////////////////////
}
