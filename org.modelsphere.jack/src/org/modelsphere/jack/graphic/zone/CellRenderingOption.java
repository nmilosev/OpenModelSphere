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

/**
 * Each cell of a zone can contains a CellRenderingOption thru the ZoneCell Class. A
 * CellRenderingOption is also specify in a ColumnCellsOption.
 */
public class CellRenderingOption {

    private CellRenderer renderer;
    private Font font;
    private Color color = null; // null means to take the zone's textColor
    private int margin;
    private int alignment;
    private boolean canReceiveFocus = true;

    public CellRenderingOption(CellRenderer newRenderer, Font newFont, int newAlignment) {
        this(newRenderer, newFont, newAlignment, 0);
    }

    public CellRenderingOption(CellRenderer newRenderer, int newAlignment) {
        this(newRenderer, Diagram.defaultFont, newAlignment, 0);
    }

    public CellRenderingOption(CellRenderer newRenderer, Font newFont, int newAlignment, int margin) {
        renderer = newRenderer;
        font = newFont;
        alignment = newAlignment;
        this.margin = margin;
    }

    public void setCanReceiveFocus(boolean focusable) {
        canReceiveFocus = focusable;
    }

    public boolean isFocusRectangleAllowed() {
        return canReceiveFocus;
    }

    public CellRenderingOption(CellRenderingOption cro) {
        renderer = cro.getCellRenderer();
        font = cro.getFont();
        alignment = cro.getAlignment();
        margin = cro.getMargin();
        color = cro.getColor();
    }

    public final Font getFont() {
        return font;
    }

    public final void setFont(Font newFont) {
        font = newFont;
    }

    public final Color getColor() {
        return color;
    }

    public final void setColor(Color newColor) {
        color = newColor;
    }

    public final CellRenderer getCellRenderer() {
        return renderer;
    }

    public final void setCellRenderer(CellRenderer newCellRenderer) {
        renderer = newCellRenderer;
    }

    public final int getMargin() {
        return margin;
    }

    public final void setMargin(int margin) {
        this.margin = margin;
    }

    public final int getAlignment() {
        return alignment;
    }

    public final void setAlignment(int newAlignment) {
        alignment = newAlignment;
    }
}
