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
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.graphic.*;
import org.modelsphere.jack.graphic.zone.*;

public class SrAttachment extends Attachment implements ActionInformation, DbRefreshListener {

    protected DbObject semObj;
    protected DbObject nodeGo;
    protected MetaField offsetMF;
    protected MetaField maxWidthMF; // may be null
    protected SingletonZone zone;

    public SrAttachment(Diagram diag, GraphicNode node, MetaField offsetMF) throws DbException {
        this(diag, node, offsetMF, null);
    }

    public SrAttachment(Diagram diag, GraphicNode node, MetaField offsetMF, MetaField maxWidthMF)
            throws DbException {
        super(diag, node);
        semObj = ((ActionInformation) node).getSemanticalObject();
        nodeGo = ((ActionInformation) node).getGraphicalObject();
        this.offsetMF = offsetMF;
        this.maxWidthMF = maxWidthMF;
        zone = new SingletonZone("");
        addZone(zone);
        setOffset((Point) nodeGo.get(offsetMF));
        if (maxWidthMF != null) {
            setMaxWidth(((Integer) nodeGo.get(maxWidthMF)).intValue());
            zone.setCellRenderer(new StringCellRenderer(false, true));
        }
        nodeGo.addDbRefreshListener(this);
    }

    // BEWARE: overriding methods must call super.delete as last action
    public void delete(boolean all) {
        nodeGo.removeDbRefreshListener(this);
        super.delete(all);
    }

    public final void translate(int dx, int dy) throws DbException {
        Rectangle nodeRect = node.getRectangle();
        Rectangle rectangle = getRectangle();
        int x = coordToOffset(rectangle.x + dx, nodeRect.x, nodeRect.x + nodeRect.width);
        int y = coordToOffset(rectangle.y + dy, nodeRect.y, nodeRect.y + nodeRect.height);
        nodeGo.set(offsetMF, new Point(x, y));
    }

    public final void resetPosition() throws DbException {
        nodeGo.set(offsetMF, null);
    }

    public final void resize(int width) throws DbException {
        nodeGo.set(maxWidthMF, new Integer(width));
    }

    public final DbObject getSemanticalObject() {
        return semObj;
    }

    public final DbObject getGraphicalObject() {
        return nodeGo;
    }

    public final Db getDb() {
        return nodeGo.getDb();
    }

    // /////////////////////////////////////////////
    // DbRefreshListener SUPPORT
    //
    public final void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
        if (event.metaField == offsetMF) {
            setOffset((Point) nodeGo.get(offsetMF));
            diagram.setComputePos(this);
        } else if (event.metaField == maxWidthMF) {
            setMaxWidth(((Integer) nodeGo.get(maxWidthMF)).intValue());
            diagram.setComputePos(this);
        }
    }
    //
    // End of DbRefreshListener SUPPORT
    // /////////////////////////////////////////////
}
