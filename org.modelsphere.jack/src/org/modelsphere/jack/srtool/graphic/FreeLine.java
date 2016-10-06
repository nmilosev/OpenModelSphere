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

import java.awt.Polygon;
import java.awt.Color;

import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.graphic.*;

public class FreeLine extends Line implements DbRefreshListener, ActionInformation {

    protected DbObject lineGo;

    public FreeLine(Diagram diag, DbObject lineGo) throws DbException {
        super(diag, (Polygon) lineGo.get(DbGraphic.fFreeLineGoPolyline));
        this.lineGo = lineGo;
        setRightAngle(((Boolean) lineGo.get(DbGraphic.fFreeLineGoRightAngle)).booleanValue());
        setStyle();
        ((DbGraphicalObjectI) lineGo).setGraphicPeer(this);
        lineGo.addDbRefreshListener(this);
    }

    private final void setStyle() throws DbException {
        setLineColor((Color) lineGo.get(DbGraphic.fFreeGraphicGoLineColor));
        setLineStyle(((Boolean) lineGo.get(DbGraphic.fFreeGraphicGoHighlight)).booleanValue(),
                ((Boolean) lineGo.get(DbGraphic.fFreeGraphicGoDashStyle)).booleanValue());
    }

    public final DbObject getGraphicalObject() {
        return lineGo;
    }

    public final DbObject getSemanticalObject() {
        return null;
    }

    public final Db getDb() {
        return lineGo.getDb(); // semObj may be null
    }

    // BEWARE: overriding methods must call super.delete as last action
    public void delete(boolean all) {
        ((DbGraphicalObjectI) lineGo).setGraphicPeer(null);
        lineGo.removeDbRefreshListener(this);
        super.delete(all);
    }

    public final void refreshAfterDbUpdate(DbUpdateEvent e) throws DbException {
        if (DbGraphic.fFreeGraphicGoLineColor == e.metaField
                || DbGraphic.fFreeGraphicGoHighlight == e.metaField
                || DbGraphic.fFreeGraphicGoDashStyle == e.metaField) {

            setStyle();
        }
    }
}
