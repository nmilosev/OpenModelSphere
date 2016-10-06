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

import java.awt.*;
import java.awt.event.*;
import javax.swing.JList;

import org.modelsphere.jack.graphic.*;

public abstract class LinkUnlinkTool extends Tool {
    private Cursor cursor;
    /*
     * protected static final int ADD_LINK = 0; protected static final int REMOVE_LINK = 1;
     * protected static final int SET_LINK = 2;
     */
    private JList list = null;

    public LinkUnlinkTool(int userId, String name, Image icon) {
        super(userId, name, icon);
        cursor = loadDefaultCursor();
    }

    protected abstract Cursor loadDefaultCursor();

    public Cursor getCursor() {
        return cursor;
    }

    // set the list GUI component assiated to this tool. This list will provide
    // this tool with
    // an objects selection for link-unlink with the semantical object of the
    // graphical object.
    public void setListComponent(JList list) {
        this.list = list;
    }

    public final void mousePressed(MouseEvent e) {
        Point pt = e.getPoint();
        GraphicComponent gc = model.graphicAt(view, pt.x, pt.y, 1 << Diagram.LAYER_GRAPHIC, false);
        if (gc == null) {
            gc = model.graphicAt(view, pt.x, pt.y, 1 << Diagram.LAYER_LINE, false);
        }
        if (gc == null) {
            gc = model.graphicAt(view, pt.x, pt.y, 1 << Diagram.LAYER_LINE_LABEL, false);
        }
        if (gc != null && list != null) {
            Object[] selected = list.getSelectedValues();
            if (selected != null && selected.length > 0) {
                addLink(gc, selected);
                removelink(gc, selected);
                setlink(gc, selected);
            }
        } else
            Toolkit.getDefaultToolkit().beep();
        view.toolCompleted();
    }

    // relative add: add linkObjs as linked objs for gc semantical object
    // subclasses should override one of the following method only.
    protected void addLink(GraphicComponent gc, Object[] linkObjs) {
    }

    // relative remove: remove linkObjs as linked objs for gc semantical object
    // subclasses should override one of the following method only.
    protected void removelink(GraphicComponent gc, Object[] linkObjs) {
    }

    // absolute link: remove existing links for gc semantical object and add
    // linkObjs as linked objs for gc semantical object
    // subclasses should override one of the following method only.
    protected void setlink(GraphicComponent gc, Object[] linkObjs) {
    }
}
