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

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.SrAttachment;
import org.modelsphere.jack.srtool.graphic.SrLineLabel;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public final class RepositionLabelAction extends AbstractApplicationAction implements
        SelectionActionListener {

    public static final String kRepositionLabel = LocaleMgr.action.getString("repositionLabel");
    public static final String kRepositionLabelAction = LocaleMgr.action
            .getString("repositionLabelAction");

    public RepositionLabelAction() {
        super(kRepositionLabel);
        this.setMnemonic(LocaleMgr.action.getMnemonic("repositionLabel"));
    }

    protected final void doActionPerformed() {
        Object[] objects = ApplicationContext.getFocusManager().getSelectedObjects();
        try {
            DbMultiTrans.beginTrans(Db.WRITE_TRANS, objects, kRepositionLabelAction);
            for (int i = 0; i < objects.length; i++) {
                if (objects[i] instanceof SrLineLabel)
                    ((SrLineLabel) objects[i]).resetLabelPosition();
                else if (objects[i] instanceof SrAttachment)
                    ((SrAttachment) objects[i]).resetPosition();
            }
            DbMultiTrans.commitTrans(objects);
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    public final void updateSelectionAction() throws DbException {
        if (!(ApplicationContext.getFocusManager().getFocusObject() instanceof ApplicationDiagram)) {
            setEnabled(false);
            return;
        }

        Object[] objects = ApplicationContext.getFocusManager().getSelectedObjects();
        boolean state = (objects.length > 0);
        for (int i = 0; i < objects.length; i++) {
            if (!((objects[i] instanceof SrLineLabel) || (objects[i] instanceof SrAttachment))) {
                state = false;
                break;
            }
        }
        setEnabled(state);
    }

}
