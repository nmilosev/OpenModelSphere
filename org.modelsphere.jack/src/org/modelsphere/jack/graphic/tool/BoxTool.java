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

package org.modelsphere.jack.graphic.tool;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JComponent;

import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.Line;
import org.modelsphere.jack.graphic.ZoneBox;
import org.modelsphere.jack.graphic.zone.CellEditor;
import org.modelsphere.jack.graphic.zone.CellID;
import org.modelsphere.jack.graphic.zone.Zone;
import org.modelsphere.jack.graphic.zone.ZoneCell;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;

public abstract class BoxTool extends Tool {
    private static final String HELP = LocaleMgr.misc.getString("HelpBox");

    private Cursor cursor;

    public BoxTool(int userId, String text, Image image) {
        super(userId, text, image);
        cursor = loadDefaultCursor();
    }

    protected Cursor loadDefaultCursor() {
        return org.modelsphere.jack.awt.AwtUtil.loadCursor(BoxTool.class, "resources/box.gif",
                new Point(8, 8), "box");// NOT LOCALIZABLE
    }

    protected abstract void createBox(int x, int y);

    public final void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (model.getDrawingArea().contains(x, y)) {
            ApplicationDiagram.lockGridAlignment = false;
            createBox(x, y);
            ApplicationDiagram.lockGridAlignment = true;

            GraphicComponent gcPressed = model.graphicAt(this.view, x, y, 0xffffffff, false);
            if (gcPressed != null) {
                if (gcPressed instanceof ZoneBox) {
                    ZoneBox box = (ZoneBox) gcPressed;
                    try {
                        Zone zone = box.getNameZone();
                        if (zone != null) {
                            CellID cellID = zone.cellAt(0, 0, 0);
                            if (zone != null && cellID != null)
                                ((ApplicationDiagram) model).setEditor(view, zone.getBox(), cellID);
                        }
                    } catch (Exception ex) { /* do nothing */
                    }
                }
            }
        }

        view.toolCompleted();
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public void updateHelp() {
        setHelpText(HELP);
    }

}
