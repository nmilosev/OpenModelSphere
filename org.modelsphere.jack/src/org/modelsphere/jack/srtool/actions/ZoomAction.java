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

import javax.swing.KeyStroke;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.tool.ZoomCombo;
import org.modelsphere.jack.srtool.*;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public class ZoomAction extends AbstractApplicationAction implements CurrentFocusListener {
    static final float[] zoomfactors = new float[] { 0.01f, 0.02f, 0.03f, 0.05f, 0.07f, 0.10f,
            0.15f, 0.20f, 0.25f, 0.30f, 0.35f, 0.40f, 0.45f, 0.50f, 0.55f, 0.60f, 0.70f, 0.80f,
            0.90f, 1.00f, 1.10f, 1.25f, 1.50f, 1.75f, 2.00f, 2.50f, 3.00f, 3.60f, 4.30f, 5.0f };

    private boolean in = true;
    private ZoomCombo zoomCombo;

    public void updateZoomCombo() {
        if (zoomCombo != null)
            zoomCombo.update();
    }

    public ZoomAction(boolean in) {
        super(in ? LocaleMgr.action.getString("zoomin") : LocaleMgr.action.getString("zoomout"));
        setIcon(in ? LocaleMgr.action.getImageIcon("zoomin") : LocaleMgr.action
                .getImageIcon("zoomout"));
        setEnabled(false);
        this.in = in;
        if (in)
            setAccelerator(KeyStroke.getKeyStroke('+'));
        else
            setAccelerator(KeyStroke.getKeyStroke('-'));

        ApplicationContext.getFocusManager().addCurrentFocusListener(this);
        setPreferenceID(in ? "in" : "out"); // NOT LOCALIZABLE - properties
    }

    protected final void doActionPerformed() {
        Object focus = ApplicationContext.getFocusManager().getFocusObject();
        if (!(focus instanceof ApplicationDiagram))
            return;
        DiagramView diagView = ((ApplicationDiagram) focus).getMainView();
        float zoom = diagView.getZoomFactor();

        float next = 0f;
        for (int i = 0; i < zoomfactors.length && next == 0f; i++) {
            if (in && zoom < zoomfactors[i])
                next = zoomfactors[i];
            else if (!in && (i < zoomfactors.length - 1) && (zoom <= zoomfactors[i + 1]))
                next = zoomfactors[i];
        }
        if (next == 0f && in && zoom < zoomfactors[0])
            next = zoomfactors[0];
        else if (next == 0f && !in && zoom > zoomfactors[zoomfactors.length - 1])
            next = zoomfactors[zoomfactors.length - 1];
        if (next == 0f)
            return;

        diagView.setZoomFactor(next);
        if (zoomCombo != null)
            zoomCombo.update();
    }

    public void currentFocusChanged(Object oldFocusObject, Object focusObject) throws DbException {
        if (!(focusObject instanceof ApplicationDiagram)) {
            setEnabled(false);
            return;
        }
        setEnabled(true);
    }

    public void setZoomCombo(ZoomCombo zoomcombo) {
        if (zoomCombo == null)
            zoomCombo = zoomcombo;
        else if ((zoomCombo.equals(zoomcombo) == true))
            return;
        else
            zoomCombo = zoomcombo;
    }

}
