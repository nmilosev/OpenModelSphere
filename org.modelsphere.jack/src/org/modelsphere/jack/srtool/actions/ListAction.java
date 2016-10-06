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

import org.modelsphere.jack.actions.AbstractMenuAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.srtool.list.ListDescriptor;

public final class ListAction extends AbstractMenuAction implements SelectionActionListener {
    private ListDescriptor[] descriptors;

    public ListAction() {
        super(LocaleMgr.action.getString("list"), LocaleMgr.action.getImageIcon("list"),
                new String[] {});
        this.setMnemonic(LocaleMgr.action.getMnemonic("list"));
        setVisibilityMode(VISIBILITY_ALWAYS_VISIBLE_IN_MENU | VISIBILITY_ALWAYS_VISIBLE_IN_TOOLBAR);
        setEnabled(false);
        setVisible(false);
    }

    protected final void doActionPerformed() {
        DbObject focusDbo = getFocusDbObject();
        if (focusDbo == null) // should not happen (no accelerator - action
            // should be up to date)
            return;
        int index = getSelectedIndex();
        if (index < 0)
            return;
        DefaultMainFrame mainFrame = ApplicationContext.getDefaultMainFrame();
        try {
            focusDbo.getDb().beginReadTrans();
            mainFrame.addListInternalFrame(focusDbo, descriptors[index]);
            focusDbo.getDb().commitTrans();
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(mainFrame, e);
        }
    }

    private DbObject getFocusDbObject() {
        DbObject focusDbo = null;
        DbObject[] objects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        Object focus = ApplicationContext.getFocusManager().getFocusObject();
        if (objects != null && objects.length > 0
                && (objects.length > 1 || !(objects[0] instanceof DbSemanticalObject))) {
            return null;
        }

        if ((objects == null || objects.length == 0) && focus instanceof ApplicationDiagram)
            focusDbo = ((ApplicationDiagram) focus).getSemanticalObject();
        else if (objects != null && objects.length == 1)
            focusDbo = objects[0];
        return focusDbo;
    }

    public final void updateSelectionAction() throws DbException {
        DbObject focusDbo = getFocusDbObject();
        if (focusDbo == null) {
            setEnabled(false);
            setVisible(false);
            return;
        }

        descriptors = ApplicationContext.getSemanticalModel().getListDescriptors(focusDbo);

        if (descriptors != null)
            setDomainValues(descriptors);

        boolean visible = descriptors != null && descriptors.length > 0;
        setEnabled(visible);
        setVisible(visible);
    }

}
