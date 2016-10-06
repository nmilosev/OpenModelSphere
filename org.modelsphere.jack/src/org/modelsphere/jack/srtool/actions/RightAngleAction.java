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

package org.modelsphere.jack.srtool.actions;

import java.awt.Polygon;

import org.modelsphere.jack.actions.AbstractTriStatesAction;
import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.graphic.Line;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.DbGraphic;
import org.modelsphere.jack.srtool.graphic.SrLine;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public final class RightAngleAction extends AbstractTriStatesAction implements
        SelectionActionListener {

    public RightAngleAction() {
        super(LocaleMgr.action.getString("rightAngle"));
    }

    protected final void doActionPerformed() {
        performTriState(ApplicationContext.getFocusManager().getSelectedObjects(), LocaleMgr.action
                .getString("rightAngle"));
    }

    protected final void setObjectValue(Object obj, Boolean value) throws DbException {
        DbObject lineGo = ((ActionInformation) obj).getGraphicalObject();
        lineGo.set(DbGraphic.fLineGoRightAngle, value);
        if (value.booleanValue())
            convertToRightAngle(lineGo);
    }

    public final void updateSelectionAction() throws DbException {
        int state = STATE_NOT_APPLICABLE;
        Object[] objects = ApplicationContext.getFocusManager().getSelectedObjects();
        for (int i = 0; i < objects.length; i++) {
            if (!(objects[i] instanceof SrLine)) {
                state = STATE_NOT_APPLICABLE;
                break;
            }
            state = updateTriState(state, ((SrLine) objects[i]).isRightAngle());
        }
        setState(state);
    }

    private final void convertToRightAngle(DbObject lineGo) throws DbException {
        Polygon poly = (Polygon) lineGo.get(DbGraphic.fLineGoPolyline);
        int i = 1;
        int nb = poly.npoints;
        poly.addPoint(poly.xpoints[nb - 1], poly.ypoints[nb - 1]);
        if (Line.startHorz(poly)) {
            poly.ypoints[1] = poly.ypoints[0];
            i++;
        }
        while (true) {
            if (i == nb)
                break;
            poly.xpoints[i] = poly.xpoints[i - 1];
            i++;
            if (i == nb)
                break;
            poly.ypoints[i] = poly.ypoints[i - 1];
            i++;
        }
        if (nb > 2 && poly.xpoints[nb - 1] == poly.xpoints[nb]
                && poly.ypoints[nb - 1] == poly.ypoints[nb])
            poly.npoints--;
        lineGo.set(DbGraphic.fLineGoPolyline, poly);
    }
}
