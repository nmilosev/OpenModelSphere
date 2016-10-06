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
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.explorer.ExplorerView;
import org.modelsphere.jack.srtool.graphic.DiagramInternalFrame;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public final class ShowDiagramAction extends AbstractApplicationAction implements
        SelectionActionListener {
    public ShowDiagramAction() {
        super(LocaleMgr.action.getString("showdiagram"), LocaleMgr.action
                .getImageIcon("showdiagram"));
        this.setMnemonic(LocaleMgr.action.getMnemonic("showdiagram"));
        setEnabled(false);
        setDefaultToolBarVisibility(false);
    }

    protected final void doActionPerformed() {
        DefaultMainFrame mainFrame = ApplicationContext.getDefaultMainFrame();
        try {
            DbObject[] objects = ApplicationContext.getFocusManager()
                    .getSelectedSemanticalObjects();

            DbMultiTrans.beginTrans(Db.READ_TRANS, objects, "");
            for (int i = 0; i < objects.length; i++) {
                mainFrame.addDiagramInternalFrame(objects[i]);
            }
            DbMultiTrans.commitTrans(objects);
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(mainFrame, e);
        }
    }

    public DiagramInternalFrame performAction(DbObject diagram) {
        if (diagram == null)
            return null;

        DefaultMainFrame mainFrame = ApplicationContext.getDefaultMainFrame();
        DiagramInternalFrame dif = null;
        try {
            diagram.getDb().beginReadTrans();
            dif = mainFrame.addDiagramInternalFrame(diagram);
            diagram.getDb().commitTrans();
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(mainFrame, e);
        }
        return dif;
    }

    public void updateSelectionAction() throws DbException {
        if (!(ApplicationContext.getFocusManager().getFocusObject() instanceof ExplorerView)) {
            setEnabled(false);
        }
        DbObject[] objs = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        if (objs.length == 1 && !(objs[0] instanceof DbSemanticalObject)) {
            setEnabled(true);
        } else {
            setEnabled(false);
        }
    }

}
