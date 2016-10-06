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

import java.awt.event.MouseEvent;
import java.awt.*;

import org.modelsphere.jack.graphic.*;
import org.modelsphere.jack.graphic.zone.*;

public abstract class KeyTool extends ButtonSelectionPanelTool {

    public KeyTool(int userId, String text, String[] tooltips, Image image, Image[] secondaryimages) {
        super(userId, text, tooltips, image, secondaryimages);
    }

    public final void mousePressed(MouseEvent e) {
        Point pt = e.getPoint();
        GraphicComponent gc = model.graphicAt(view, pt.x, pt.y, 1 << Diagram.LAYER_GRAPHIC, false);
        if (gc instanceof ZoneBox) {
            ZoneBox box = (ZoneBox) gc;
            CellID cellid = box.cellAt(view, pt.x, pt.y);
            if (cellid != null) {
                ZoneCell zonecell = box.getCell(cellid);
                if (zonecell != null) {
                    createKey(gc, zonecell.getObject());
                } else
                    Toolkit.getDefaultToolkit().beep();
            } else
                Toolkit.getDefaultToolkit().beep();
        } else {
            GraphicComponent label = model.graphicAt(view, pt.x, pt.y,
                    1 << Diagram.LAYER_LINE_LABEL, false);
            if (label != null) {
                createDependency(label);
            } else
                Toolkit.getDefaultToolkit().beep();
        }

        view.toolCompleted();
    }

    // gc is the ZoneBox
    // semobj is the row semantical object
    protected abstract void createKey(GraphicComponent gc, Object semobj);

    protected abstract void createDependency(GraphicComponent label);

}
