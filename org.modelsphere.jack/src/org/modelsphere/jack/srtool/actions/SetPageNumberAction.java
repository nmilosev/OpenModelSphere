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

import java.awt.Font;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.srtypes.PageNoPosition;
import org.modelsphere.jack.graphic.SetPageNumberPositionDialog;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DbGraphic;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public class SetPageNumberAction extends AbstractApplicationAction implements
        SelectionActionListener {
    public SetPageNumberAction() {
        super(LocaleMgr.action.getString("SetPageNumberAction"));
    }

    protected final void doActionPerformed() {
        ApplicationDiagram diag = (ApplicationDiagram) ApplicationContext.getFocusManager()
                .getFocusObject();;
        SetPageNumberPositionDialog dialog = new SetPageNumberPositionDialog(diag.getMainView()
                .getParent(), diag);
        org.modelsphere.jack.awt.AwtUtil.centerWindow(dialog);
        dialog.setVisible(true);

        if (dialog.isAccepted()) {

            DbObject diagGO = diag.getDiagramGO();
            try {
                diagGO.getDb().beginTrans(Db.WRITE_TRANS,
                        LocaleMgr.action.getString("SetPageNumberAction"));
                int pageNoPos = dialog.getPageNoPos();
                PageNoPosition pos = PageNoPosition.getInstance(pageNoPos);
                Font font = dialog.getPageNoFont();

                diagGO.set(DbGraphic.fDiagramPageNoPsition, pos);
                diagGO.set(DbGraphic.fDiagramPageNoFont, font);
                diagGO.getDb().commitTrans();
            } catch (Exception ex) {
                org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                        ApplicationContext.getDefaultMainFrame(), ex);
            }
        }
    }

    public final void updateSelectionAction() throws org.modelsphere.jack.baseDb.db.DbException {
        this.setEnabled(this.isApplicationDiagramHaveFocus());
    }
}
