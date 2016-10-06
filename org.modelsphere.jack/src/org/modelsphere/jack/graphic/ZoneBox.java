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

package org.modelsphere.jack.graphic;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.Vector;

import org.modelsphere.jack.graphic.shape.GraphicShape;
import org.modelsphere.jack.graphic.zone.*;
import org.modelsphere.jack.util.SrVector;

public class ZoneBox extends GraphicComponent {

    protected SrVector zones = new SrVector();
    protected int leftColPct = 0; // percentage occupied by the left column in a
    // 2-column zone box (0 means single column)
    protected int nbLeftZones = 0; // nb of zones in the left column (2-column
    // zone box)
    protected boolean vertLine = false; // draw a vertical line between the

    // columns (2-column zone box)

    public ZoneBox(Diagram diagram, GraphicShape shape) {
        super(diagram, shape);
    }

    public final void addZone(Zone zone) {
        zones.add(zone);
        zone.setBox(this);
    }

    public final Zone getZoneAt(String name) {
        int i, nb;
        for (i = 0, nb = zones.size(); i < nb; i++) {
            Zone zone = (Zone) zones.get(i);
            if (name.equals(zone.getNameID()))
                return zone;
        }
        return null;
    }

    public final Zone getNameZone() {
        int i, nb;
        for (i = 0, nb = zones.size(); i < nb; i++) {
            Zone zone = (Zone) zones.get(i);
            String val = zone.getNameID();
            if (val != null)
                if (val.endsWith("Name"))
                    return zone;
        }
        return null;
    }

    public final void removeZone(Zone zone) {
        if (zone != null && zones.contains(zone)) {
            zone.setBox(null);
            zones.remove(zone);
        }
    }

    public final void clearAllZones() {
        for (int i = 0; i < zones.size(); i++) {
            Zone zone = (Zone) zones.get(i);
            zone.setBox(null);
        }
        zones.clear();
    }

    // Enumerate the zones in display order.
    public final Enumeration displayedZones() {
        return new Enumeration() {
            private int i = 0;
            private int nb = zones.size();
            private Zone zone = null;

            public boolean hasMoreElements() {
                while (true) {
                    if (zone != null && zone.isVisible())
                        return true;
                    if (i == nb)
                        return false;
                    zone = (Zone) zones.get(i);
                    i++;
                }
            }

            public Object nextElement() {
                Zone ret = zone;
                zone = null;
                return ret;
            }
        };
    }

    public final void setTwoColumns(int leftColPct, int nbLeftZones, boolean vertLine) {
        this.leftColPct = leftColPct;
        this.nbLeftZones = nbLeftZones;
        this.vertLine = vertLine;
    }

    public final void setVerticalLine(boolean b) {
        this.vertLine = b;
    }

    public final void setOneColumn() {
        leftColPct = nbLeftZones = 0;
        vertLine = false;
    }

    // BEWARE: diagView is null for printing
    protected void paintZones(Graphics g, DiagramView diagView, int drawingMode, int renderingFlags) {
        paintZones(g, diagView, drawingMode, false, renderingFlags);
    }

    protected void paintZonesSelection(Graphics g, DiagramView diagView) {
        paintZones(g, diagView, GraphicComponent.DRAW_WHOLE, true, 0);
    }

    protected void paintZones(Graphics g, DiagramView diagView, int drawingMode, boolean drawSel,
            int renderingFlags) {
        Rectangle savedClip = g.getClipBounds();
        Rectangle contentRect = getContentRect();
        int width = (leftColPct != 0 ? contentRect.width * leftColPct / 100 : contentRect.width);
        Rectangle rect = new Rectangle(contentRect.x, contentRect.y, width, contentRect.height);
        Rectangle zoomRect = (diagView == null ? rect : diagView.zoom(rect));
        Rectangle clipRect = savedClip.intersection(zoomRect);
        g.setClip(clipRect.x, clipRect.y, clipRect.width, clipRect.height);
        for (int i = 0; i < zones.size(); i++) {
            if (leftColPct != 0 && i == nbLeftZones) {
                rect.x += rect.width;
                rect.width = contentRect.width - rect.width;
                zoomRect = (diagView == null ? rect : diagView.zoom(rect));
                clipRect = savedClip.intersection(zoomRect);
                g.setClip(clipRect.x, clipRect.y, clipRect.width, clipRect.height);
                if (vertLine && !drawSel) {
                    g.setColor(lineColor);
                    g.drawLine(zoomRect.x, zoomRect.y, zoomRect.x, zoomRect.y + zoomRect.height);
                }
            } // end if

            Zone zone = (Zone) zones.get(i);
            if (!zone.isVisible())
                continue;

            Rectangle zRect = zone.getRectangle(rect); // zone.getRectangle(clipRect);
            if (zRect != null) {
                if (zRect.y >= rect.height)
                    continue; // skip the rest of the zones in this column, they
                // are outside the frame

                Rectangle zoneRect = new Rectangle(rect.x, rect.y + zRect.y, rect.width,
                        rect.height - zRect.y);
                boolean bottomClipped = true; // on the printer, the clip rect
                // clips only partially the
                // bottom line
                if (zoneRect.height > zRect.height) {
                    zoneRect.height = zRect.height;
                    bottomClipped = false;
                }

                zone.paint(g, diagView, drawingMode, zoneRect, drawSel, bottomClipped);
            } // end if

        } // end for

        g.setClip(savedClip.x, savedClip.y, savedClip.width, savedClip.height);
    } // end paintZones()

    // compute preferred position data, not actual
    // (i.e. this computation is done independantly of the zone box size)
    private void computeZonesPositionData(Graphics g) {
        int fixedWidth, fixedHeight; // may be used by wrapping renderers.

        if (this instanceof LineLabel) {
            fixedWidth = ((LineLabel) this).getMaxWidth();
            fixedHeight = fixedWidth;
        } else if (this instanceof Attachment) {
            fixedWidth = ((Attachment) this).getMaxWidth();
            fixedHeight = fixedWidth;
        } else {
            fixedWidth = (autoFitMode == GraphicComponent.TOTAL_FIT ? 0 : getContentRect().width);
            fixedHeight = (autoFitMode == GraphicComponent.TOTAL_FIT ? 0 : getContentRect().height);
        } // end if

        int width = (leftColPct != 0 ? fixedWidth * leftColPct / 100 : fixedWidth);
        int top = 0;

        for (int i = 0; i < zones.size(); i++) {
            if (leftColPct != 0 && i == nbLeftZones) {
                width = fixedWidth - width;
                top = 0;
            }
            Zone zone = (Zone) zones.get(i);
            if (zone.isVisible())
                top = zone.computePositionData(g, top, width, fixedHeight);
        }
    }

    protected final Dimension getZonesPreferredSize() {
        Dimension size = new Dimension(0, 0);
        Dimension leftSize = null;
        for (int i = 0; i < zones.size(); i++) {
            if (leftColPct != 0 && i == nbLeftZones) {
                leftSize = size;
                size = new Dimension(0, 0);
            }
            Zone zone = (Zone) zones.get(i);
            if (!zone.isVisible())
                continue;
            Rectangle rect = zone.getRectangle((Rectangle) null);
            if (rect == null)
                continue;
            size.width = Math.max(size.width, rect.width);
            size.height = rect.y + rect.height;
        }
        if (leftSize != null) {
            size.width = Math.max(leftSize.width * 100 / leftColPct, size.width * 100
                    / (100 - leftColPct));
            size.height = Math.max(leftSize.height, size.height);
        }
        return size;
    }

    // BEWARE: diagView is null for printing
    // overridden
    public void paint(Graphics g, DiagramView diagView, int drawingMode, int renderingFlags) {
        super.paint(g, diagView, drawingMode, renderingFlags);
        paintZones(g, diagView, drawingMode, renderingFlags);
    }

    // overridden
    public void paintSelHandles(Graphics g, DiagramView diagView) {
        super.paintSelHandles(g, diagView);
        paintZonesSelection(g, diagView);
    }

    // <g> = any screen graphics, only used to get font metrics
    // overridden
    void computePositionData(Graphics g) {
        computeZonesPositionData(g);
        if (!(this instanceof LineLabel || this instanceof Attachment)) {
            if (autoFitMode == GraphicComponent.NO_FIT)
                repaint();
            else {
                Rectangle rectangle = getRectangle();
                Dimension size = getPreferredSize();
                // Preserve width if Fit mode = HEIGHT_FIT. If current rect
                // width == 0
                // use preferred width to avoid hidden object. This typically
                // occurs because width has not been set
                // by the user yet. For example, this can be the result from a
                // text paste operation where the size
                // of the new FreeText is set to 0,0 by PasteAction.
                if (autoFitMode == GraphicComponent.HEIGHT_FIT && rectangle.width > 0)
                    size.width = rectangle.width;

                setRectangle(GraphicUtil.rectangleResize(rectangle, size.width, size.height));

                /*
                 * Rectangle newRect = GraphicUtil.rectangleResize(rectangle, size.width,
                 * size.height); newRect.x = rectangle.x; newRect.y = rectangle.y;
                 * setRectangle(newRect);
                 */
            }
        }
    }

    public final Dimension getPreferredSize() {
        Dimension ps = getZonesPreferredSize();
        Dimension size = getShapeSize(ps);
        Dimension minSize = getMinimumSize();
        size.width = Math.max(size.width, minSize.width);
        size.height = Math.max(size.height, minSize.height);
        return size;
    }

    // Return null if no cell at <x,y>
    public final CellID cellAt(DiagramView diagView, int x, int y) {
        Rectangle rect = getContentRect();
        x -= rect.x;
        y -= rect.y;
        // Keep a margin at the top and bottom of the zones rectangle
        // to allow the user to select the box without selecting a cell.
        int d = diagView.getTopBottomInsets();
        if (y < d || y >= rect.height - d)
            return null;

        int width = rect.width;
        int i = 0;
        int nb = zones.size();
        if (leftColPct != 0) {
            width = rect.width * leftColPct / 100;
            if (x < width) {
                nb = nbLeftZones;
            } else {
                x -= width;
                width = rect.width - width;
                i = nbLeftZones;
            }
        }
        for (; i < nb; i++) {
            Zone zone = (Zone) zones.get(i);
            if (!zone.isVisible())
                continue;
            Rectangle zRect = zone.getRectangle((Rectangle) null);
            if ((zRect != null) && (y < zRect.y + zRect.height))
                return zone.cellAt(x, y - zRect.y, width);
        }
        return null;
    }

    // Return null if the zone is not displayed.
    // May return a rectangle outside of the box frame.
    public final Rectangle getCellRect(CellID cellId) {
        if (!cellId.zone.isVisible())
            return null;
        Rectangle contentRect = getContentRect();
        Rectangle rect = contentRect;
        if (leftColPct != 0) {
            rect = new Rectangle(contentRect.x, contentRect.y,
                    contentRect.width * leftColPct / 100, contentRect.height);
            if (zones.indexOf(cellId.zone) >= nbLeftZones) {
                rect.x += rect.width;
                rect.width = contentRect.width - rect.width;
            }
        }
        return cellId.zone.getCellRect(cellId, rect);
    }

    public final CellEditor getCellEditor(CellID cellID) {
        return cellID.zone.getCellEditor(cellID);
    }

    public final void setCellEditor(CellID cellID, CellEditor cellEditor) {
        cellID.zone.setCellEditor(cellID, cellEditor);
    }

    public final ZoneCell getCell(CellID cellID) {
        return cellID.zone.getCell(cellID);
    }

    public final CellID[] getSelectedCells() {
        Vector vecSelCells = new Vector();
        int nb = zones.size();
        for (int i = 0; i < nb; i++) {
            Zone zone = (Zone) zones.get(i);
            zone.getSelectedCells(vecSelCells);
        }
        CellID[] selCells = new CellID[vecSelCells.size()];
        vecSelCells.copyInto(selCells);
        return selCells;
    }

    public final void deselectAllCells() {
        CellID[] selCells = getSelectedCells();
        for (int i = 0; i < selCells.length; i++)
            selectCell(selCells[i], false);
    }

    public final boolean isCellSelected(CellID cellID) {
        ZoneCell value = getCell(cellID);
        return value.isSelected();
    }

    public final void selectCell(CellID cellID, boolean state) {
        ZoneCell cell = getCell(cellID);
        boolean cellSelected = cell.isSelected();

        if (state != cellSelected) {
            cell.setSelected(state);
            diagram.fireSelectionChanged();
        }
    }

    /*
     * This object keeps the current selection of the ZoneBox, in order to restore it after a
     * populateContents. For this to work, the ZoneBox must always keep the same zones; the contents
     * of the zones may change completely. In a matrix zone, we retrieve the row using the field
     * <object> of ZoneCell.
     */
    public static class ZoneBoxSelection {
        private ZoneBox box;
        private CellID[] selCells;
        private Object[] selObjs;

        public ZoneBoxSelection(ZoneBox box) {
            this.box = box;
            selCells = box.getSelectedCells();
            selObjs = new Object[selCells.length];
            for (int i = 0; i < selCells.length; i++)
                selObjs[i] = box.getCell(selCells[i]).getObject();
        }

        public final void restore() {
            boolean selChanged = false;
            for (int i = 0; i < selCells.length; i++) {
                ZoneCell value = null;
                if (selCells[i].zone instanceof MatrixZone) {
                    MatrixCellID cellID = (MatrixCellID) selCells[i];
                    MatrixZone zone = (MatrixZone) cellID.zone;
                    if (cellID.row >= zone.getRowCount() || zone.getCell(cellID) != selObjs[i])
                        cellID = zone.getCellID(selObjs[i], cellID.col);
                    if (cellID != null)
                        value = zone.getCell(cellID);
                } else
                    value = box.getCell(selCells[i]);

                if (value != null)
                    value.setSelected(true);
                else
                    selChanged = true;
            }
            if (selChanged)
                box.getDiagram().fireSelectionChanged();
        }
    }
}
