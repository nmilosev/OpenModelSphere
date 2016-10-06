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

import java.awt.event.ActionEvent;
import javax.swing.*;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.awt.SRSystemClipboard;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.DbGraphic;
import org.modelsphere.jack.srtool.graphic.FreeText;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;

public class CopyAction extends AbstractApplicationAction implements SelectionActionListener {
    public CopyAction() {
        super(LocaleMgr.action.getString("copy"), LocaleMgr.action.getImageIcon("copy"));
        this.setMnemonic(LocaleMgr.action.getMnemonic("copy"));
        this.setAccelerator(KeyStroke.getKeyStroke(LocaleMgr.action.getAccelerator("copy"),
                ActionEvent.CTRL_MASK));
        setEnabled(false);
        setVisible(ScreenPerspective.isFullVersion());
    }

    public CopyAction(String name) {
        super(name);
        setEnabled(false);
        setVisible(ScreenPerspective.isFullVersion());
    }

    protected void doActionPerformed() {
        try {
            emptyClipboards();

            Object[] objects = ApplicationContext.getFocusManager().getSelectedObjects();
            if (objects.length == 1 && objects[0] instanceof FreeText) {
                DbObject dbo = ((FreeText) objects[0]).getGraphicalObject();
                dbo.getDb().beginTrans(Db.READ_TRANS);
                SRSystemClipboard.setClipboardText((String) dbo.get(DbGraphic.fUserTextGoText));
                dbo.getDb().commitTrans();
            } else { // the semantical copy
                DbObject[] semObjs = ApplicationContext.getFocusManager()
                        .getSelectedSemanticalObjects();
                ApplicationContext.getDefaultMainFrame().getClipboard().copy(semObjs);
            }
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    protected final void emptyClipboards() {
        // semantical clipboard
        ApplicationContext.getDefaultMainFrame().getClipboard().emptyClipboard();
        // text and image (system clipboard)
        SRSystemClipboard.emptyClipboard();
    }

    public void updateSelectionAction() throws DbException {
        Object[] objects = ApplicationContext.getFocusManager().getSelectedObjects();
        if (objects.length == 1 && objects[0] instanceof FreeText)
            setEnabled(true);
        else {
            DbObject[] semObjs = ApplicationContext.getFocusManager()
                    .getSelectedSemanticalObjects();
            setEnabled(ApplicationContext.getSemanticalModel().isValidForCopy(semObjs));
        }
    }
}
