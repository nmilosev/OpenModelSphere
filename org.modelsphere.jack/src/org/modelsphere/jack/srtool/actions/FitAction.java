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

import java.awt.*;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.graphic.*;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.DbGraphic;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public class FitAction extends AbstractApplicationAction implements SelectionActionListener {

    public FitAction() {
        super(LocaleMgr.action.getString("fit"));
    }

    protected final void doActionPerformed() {
        Object[] objects = ApplicationContext.getFocusManager().getSelectedObjects();
        try {
            DbMultiTrans.beginTrans(Db.WRITE_TRANS, objects, LocaleMgr.action.getString("fit"));
            for (int i = 0; i < objects.length; i++) {
                GraphicComponent gc = (GraphicComponent) objects[i];
                if (!(gc instanceof ZoneBox) || gc instanceof LineLabel || gc instanceof Attachment)
                    continue;
                if (gc.getAutoFitMode() == GraphicComponent.TOTAL_FIT)
                    continue;
                Dimension size = gc.getPreferredSize();
                Rectangle rect = GraphicUtil.rectangleResize(gc.getRectangle(), size.width,
                        size.height);
                DbObject go = (DbObject) ((ActionInformation) gc).getGraphicalObject();
                go.set(DbGraphic.fGraphicalObjectRectangle, rect);
            }
            DbMultiTrans.commitTrans(objects);
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    public final void updateSelectionAction() throws DbException {
        Object[] objects = ApplicationContext.getFocusManager().getSelectedObjects();
        int nb = 0;
        for (int i = 0; i < objects.length; i++) {
            if (!(objects[i] instanceof GraphicComponent)) {
                nb = 0;
                break;
            }
            GraphicComponent gc = (GraphicComponent) objects[i];
            if (gc instanceof ZoneBox && !(gc instanceof LineLabel || gc instanceof Attachment)
                    && gc.getAutoFitMode() != GraphicComponent.TOTAL_FIT)
                nb++;
        }
        setEnabled(nb > 0);

        // In most case, this action will be disabled only when selected gos
        // don't support this feature (line)
        // So hide this action
        if (!isEnabled())
            setVisible(false);
        else
            setVisible(true);

    }
}
