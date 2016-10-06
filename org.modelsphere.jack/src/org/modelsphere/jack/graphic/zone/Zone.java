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
import java.util.Vector;

import org.modelsphere.jack.graphic.*;

/**
 * This abstract class define an standard zone interface and common implementation to render data in
 * a ZoneBox. A ZoneBox can contains many Zones. This class define the way a single zone in a box is
 * paint. A zone paints it's data in cells. The number of cells and the way they are render depends
 * on the Zone sub-classes.
 */
public abstract class Zone {
    protected String nameID;
    protected ZoneBox box = null;
    protected Color foreColor = null;
    protected Color backColor = null;
    protected Color textColor = null;
    protected boolean hasBottomLine = true;
    protected boolean visible = true;

    protected Rectangle m_rectangle = null;

    /**
     * Each zone contains in a ZoneBox must have a unique name, this name is use internally the user
     * won't see the name unless he use a future zone editor.
     * 
     * @param newNameID
     *            the zone name, if this zone is add to a zoneBox, the name must be unique for all
     *            the zone in zoneBox
     */
    public Zone(String newNameID) {
        setNameID(newNameID);
    }

    public final void setNameID(String newNameID) {
        nameID = newNameID;
    }

    public final String getNameID() {
        return nameID;
    }

    public final ZoneBox getBox() {
        return box;
    }

    // For internal use of ZoneBox
    public final void setBox(ZoneBox box) {
        this.box = box;
    }

    public final boolean isVisible() {
        return visible;
    }

    public final void setVisible(boolean value) {
        visible = value;
    }

    // By default, ignore containerRect paramater, but some subclasses
    // of Zone could use it.
    public Rectangle getRectangle(Rectangle containerRect) {
        return m_rectangle;
    }

    public Rectangle getRectangle() {
        return this.getRectangle((Rectangle) null);
    }

    public final void setHasBottomLine(boolean value) {
        hasBottomLine = value;
    }

    public final void setForeColor(Color color) {
        foreColor = color;
    }

    public final void setBackColor(Color color) {
        backColor = color;
    }

    public final void setTextColor(Color color) {
        textColor = color;
    }

    public final Color getForeColor() {
        return (foreColor != null ? foreColor : box.getLineColor());
    }

    public final Color getTextColor() {
        return (textColor != null ? textColor : box.getTextColor());
    }

    // compute the position data independantly of the zone box size.
    // <fixedWidth>, if not zero, is the zone width for wrapping renderers.
    // <g> = any screen graphics, only used to get font metrics
    public abstract int computePositionData(Graphics g, int top, int fixedWidth, int fixedHeight);

    // BEWARE: diagView is null for printing
    public abstract void paint(Graphics g, DiagramView diagView, int drawingMode, Rectangle rect,
            boolean drawSel, boolean bottomClipped);

    // BEWARE: overriding methods must call super.paintBackground as first
    // action
    public void paintBackground(Graphics g, DiagramView diagView, int drawingMode, Rectangle rect,
            boolean bottomClipped) {
        Rectangle r = (diagView == null ? rect : diagView.zoom(rect));
        if (backColor != null && r.height > 1) {
            g.setColor(backColor);
            g.fillRect(r.x + 1, r.y + 1, r.width - 1, r.height - 1);
        }
        if (hasBottomLine && !bottomClipped) {
            g.setColor(getForeColor());
            g.drawLine(r.x, r.y + r.height, r.x + r.width, r.y + r.height);
        }
    }

    public abstract ZoneCell getCell(CellID cellId);

    // For internal use of ZoneBox
    public abstract void getSelectedCells(Vector selCells);

    // For internal use of ZoneBox
    // Return null if no cell at position <x,y>.
    // <x,y> is relative to the zone and is inside the zone.
    public abstract CellID cellAt(int x, int y, int width);

    // For internal use of ZoneBox
    public abstract Rectangle getCellRect(CellID cellId, Rectangle zonesRect);

    // For internal use of ZoneBox
    public abstract CellEditor getCellEditor(CellID cellID);

    public abstract void setCellEditor(CellID cellID, CellEditor cellEditor);
}
