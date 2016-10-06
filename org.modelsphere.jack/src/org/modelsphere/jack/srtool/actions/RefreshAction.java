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

import java.awt.event.*;
import javax.swing.*;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.screen.DbDataEntryFrame;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.CurrentFocusListener;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.explorer.*;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.srtool.list.ListInternalFrame;
import org.modelsphere.jack.text.MessageFormat;

public class RefreshAction extends AbstractApplicationAction implements CurrentFocusListener {

    public RefreshAction() {
        super(LocaleMgr.action.getString("refresh"), LocaleMgr.action.getImageIcon("refresh"));
        this.setMnemonic(LocaleMgr.action.getMnemonic("refresh"));
        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));

        ApplicationContext.getFocusManager().addCurrentFocusListener(this);
        setEnabled(false);
        setDefaultToolBarVisibility(false);
    }

    protected final void doActionPerformed() {
        DefaultMainFrame mf = ApplicationContext.getDefaultMainFrame();
        try {
            Object focusObject = ApplicationContext.getFocusManager().getFocusObject();
            if (focusObject instanceof ApplicationDiagram) {
                ApplicationDiagram diag = (ApplicationDiagram) focusObject;
                Db db = diag.getDiagramGO().getDb();
                db.beginTrans(Db.READ_TRANS);
                diag.getDiagramInternalFrame().refresh();
                db.commitTrans();
            } else if (focusObject instanceof DbDataEntryFrame) {
                DbDataEntryFrame frame = (DbDataEntryFrame) focusObject;
                Db db = frame.getProject().getDb();
                db.beginTrans(Db.READ_TRANS);
                frame.refresh();
                db.commitTrans();
            } else if (focusObject instanceof ListInternalFrame) {
                ListInternalFrame frame = (ListInternalFrame) focusObject;
                Db db = frame.getProject().getDb();
                db.beginTrans(Db.READ_TRANS);
                frame.refresh();
                db.commitTrans();
            } else if (focusObject instanceof ExplorerView) {
                mf.getExplorerPanel().getExplorer().refresh();
            }
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(mf, ex);
        }
    }

    // //////////////////////////////////////////////
    // CurrentFocusListener Support
    //
    public final void currentFocusChanged(Object oldFocusObject, Object focusObject)
            throws DbException {
        String name = null;
        if (focusObject instanceof ApplicationDiagram)
            name = ((ApplicationDiagram) focusObject).getDiagramInternalFrame().getTitle();
        else if (focusObject instanceof DbDataEntryFrame) {
            if (((DbDataEntryFrame) focusObject).getProject() != null)
                name = ((DbDataEntryFrame) focusObject).getTitle();
        } else if (focusObject instanceof ExplorerView)
            name = LocaleMgr.action.getString("explorer");
        else if (focusObject instanceof ListInternalFrame)
            name = LocaleMgr.action.getString("list");

        if (name != null) {
            putValue(Action.NAME, MessageFormat.format(LocaleMgr.action.getString("refresh0"),
                    new Object[] { name }));
            setEnabled(true);
        } else {
            putValue(Action.NAME, LocaleMgr.action.getString("refresh"));
            setEnabled(false);
        }
    }
    //
    // End of CurrentFocusListener Support
    // //////////////////////////////////////////
}
