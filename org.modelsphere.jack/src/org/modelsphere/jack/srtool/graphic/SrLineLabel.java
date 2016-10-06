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

import java.awt.Font;
import java.awt.Point;

import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.LineLabel;
import org.modelsphere.jack.graphic.shape.GraphicShape;
import org.modelsphere.jack.graphic.shape.RectangleShape;
import org.modelsphere.jack.graphic.zone.MatrixZone;
import org.modelsphere.jack.graphic.zone.SingletonZone;
import org.modelsphere.jack.graphic.zone.StringCellRenderer;
import org.modelsphere.jack.graphic.zone.ZoneCell;

public abstract class SrLineLabel extends LineLabel implements ActionInformation, DbRefreshListener {

    protected DbObject semObj; // may be null
    protected DbObject lineGo;
    protected MetaField offsetMF;
    protected MetaField maxWidthMF; // may be null
    protected SingletonZone zone;

    public SrLineLabel(Diagram diag, DbObject semObj, SrLine line, MetaField offsetMF,
            String labelZoneName) throws DbException {
        this(diag, semObj, line, offsetMF, null, RectangleShape.singleton_erasebackground, 0,
                labelZoneName);
    }

    public SrLineLabel(Diagram diag, DbObject semObj, SrLine line, MetaField offsetMF,
            int minimalHeight, String labelZoneName) throws DbException {
        this(diag, semObj, line, offsetMF, null, RectangleShape.singleton_erasebackground,
                minimalHeight, labelZoneName);
    }

    public SrLineLabel(Diagram diag, DbObject semObj, SrLine line, MetaField offsetMF,
            GraphicShape shape, int minimalHeight, String labelZoneName) throws DbException {
        this(diag, semObj, line, offsetMF, null, shape, minimalHeight, labelZoneName);
    }

    public SrLineLabel(Diagram diag, DbObject semObj, SrLine line, MetaField offsetMF,
            MetaField maxWidthMF, GraphicShape shape, int minimalHeight, String labelZoneName)
            throws DbException {
        super(diag, line, shape);
        this.semObj = semObj;
        lineGo = line.getGraphicalObject();
        this.offsetMF = offsetMF;
        this.maxWidthMF = maxWidthMF;
        zone = new SingletonZone(labelZoneName);
        zone.setHasBottomLine(true);// /
        addZone(zone);

        if (minimalHeight > 0) {
            MatrixZone columnZone = new MatrixZone("", GraphicUtil.LEFT_ALIGNMENT, minimalHeight);
            addZone(columnZone);
        }

        Point p = (Point) lineGo.get(offsetMF);
        setOffset(p);
        if (maxWidthMF != null) {
            setMaxWidth(((Integer) lineGo.get(maxWidthMF)).intValue());
            zone.setCellRenderer(new StringCellRenderer(false, true));
        }
        lineGo.addDbRefreshListener(this);
    }

    // BEWARE: overriding methods must call super.delete as last action
    public void delete(boolean all) {
        lineGo.removeDbRefreshListener(this);
        super.delete(all);
    }

    // Overridden
    public void setValue(String value) {
        zone.setValue(new ZoneCell(semObj, value));
    }

    public final void setFont(Font font) {
        zone.setFont(font);
    }

    public final void setAlignment(int alignment) {
        zone.setAlignment(alignment);
    }

    public final void translateLabel(int dx, int dy) throws DbException {
        Point pt = new Point(xOffset + dx, yOffset + dy);
        lineGo.set(offsetMF, pt);
    }

    public final void resizeLabel(int width) throws DbException {
        lineGo.set(maxWidthMF, new Integer(width));
    }

    public final DbObject getSemanticalObject() {
        return semObj;
    }

    public final DbObject getGraphicalObject() {
        return lineGo;
    }

    public final Db getDb() {
        return lineGo.getDb();
    }

    public final void resetLabelPosition() throws DbException {
        lineGo.set(offsetMF, null);
    }

    // /////////////////////////////////////////////
    // DbRefreshListener SUPPORT
    //
    public final void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
        if (event.metaField == offsetMF) {
            setOffset((Point) lineGo.get(offsetMF));
            diagram.setComputePos(line);
        } else if (event.metaField == maxWidthMF) {
            setMaxWidth(((Integer) lineGo.get(maxWidthMF)).intValue());
            diagram.setComputePos(line);
        }
    }
    //
    // End of DbRefreshListener SUPPORT
    // /////////////////////////////////////////////
}
