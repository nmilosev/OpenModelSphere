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

import org.modelsphere.jack.actions.AbstractDomainAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.DbGraphic;
import org.modelsphere.jack.srtool.graphic.DiagramImage;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public final class SetImageOpacityAction extends AbstractDomainAction implements
        SelectionActionListener {

    private static final String kImageOpacity = LocaleMgr.action.getString("imageOpacity");
    private static final String kSetImageOpacity = LocaleMgr.action.getString("setImageOpacity");

    private static final float[] possibleOpacityValues = { 0.2f, 0.4f, 0.6f, 0.8f, 1.0f };
    private static final String[] possibleOpacityLabels = { "20%", "40%", "60%", "80%", "100%" };

    public SetImageOpacityAction() {
        super(kImageOpacity);
        setDomainValues(possibleOpacityLabels);
    }

    protected final void doActionPerformed() {
        try {
            Object[] objects = ApplicationContext.getFocusManager().getSelectedObjects();
            Float selValue = new Float(possibleOpacityValues[getSelectedIndex()]);
            DbMultiTrans.beginTrans(Db.WRITE_TRANS, objects, kSetImageOpacity);
            for (int i = 0; i < objects.length; i++) {
                ((DiagramImage) objects[i]).getGraphicalObject().set(
                        DbGraphic.fImageGoOpacityFactor, selValue);
            }
            DbMultiTrans.commitTrans(objects);
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    public final void updateSelectionAction() throws DbException {
        Object[] objects = ApplicationContext.getFocusManager().getSelectedObjects();
        Float selValue = null;
        for (int i = 0; i < objects.length; i++) {
            Float value = (Float) ((DiagramImage) objects[i]).getGraphicalObject().get(
                    DbGraphic.fImageGoOpacityFactor);
            if (selValue == null)
                selValue = value;
            else if (!selValue.equals(value)) {
                selValue = null;
                break;
            }
        }
        int selInd = -1;
        if (selValue != null) {
            for (int i = 0; i < possibleOpacityValues.length; i++) {
                if (possibleOpacityValues[i] == selValue.floatValue()) {
                    selInd = i;
                    break;
                }
            }
        }
        setSelectedIndex(selInd);
    }
}
