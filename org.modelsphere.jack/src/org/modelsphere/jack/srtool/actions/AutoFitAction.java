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

import org.modelsphere.jack.actions.AbstractTriStatesAction;
import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.graphic.*;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.DbGraphic;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public class AutoFitAction extends AbstractTriStatesAction implements SelectionActionListener {

    public AutoFitAction() {
        super(LocaleMgr.action.getString("autoFit"));
    }

    protected final void doActionPerformed() {
        performTriState(ApplicationContext.getFocusManager().getSelectedObjects(), LocaleMgr.action
                .getString("autoFit"));
    }

    protected final void setObjectValue(Object obj, Boolean value) throws DbException {
        GraphicComponent gc = (GraphicComponent) obj;
        if (!(gc instanceof ZoneBox) || gc instanceof LineLabel || gc instanceof Attachment)
            return;
        DbObject go = (DbObject) ((ActionInformation) gc).getGraphicalObject();
        go.set(DbGraphic.fGraphicalObjectAutoFit, value);
        if (!value.booleanValue())
            go.set(DbGraphic.fGraphicalObjectRectangle, gc.getRectangle());
    }

    public final void updateSelectionAction() throws DbException {
        int state = STATE_NOT_APPLICABLE;
        Object[] objects = ApplicationContext.getFocusManager().getSelectedObjects();
        for (int i = 0; i < objects.length; i++) {
            if (!(objects[i] instanceof GraphicComponent)) {
                state = STATE_NOT_APPLICABLE;
                break;
            }
            GraphicComponent gc = (GraphicComponent) objects[i];
            if (gc instanceof ZoneBox && !(gc instanceof LineLabel || gc instanceof Attachment))
                state = updateTriState(state, gc.isAutoFit());
        }
        setState(state);

        // In most case, this action will be in state STATE_NOT_APPLICABLE only
        // when selected gos don't support this feature (line)
        // So hide this action
        if (state == STATE_NOT_APPLICABLE)
            setVisible(false);
        else
            setVisible(true);
    }
}
