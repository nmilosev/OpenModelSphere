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

import javax.swing.tree.*;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.CurrentFocusListener;
import org.modelsphere.jack.srtool.explorer.*;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public class ExpandAllAction extends AbstractApplicationAction implements CurrentFocusListener {

    public ExpandAllAction() {
        super(LocaleMgr.action.getString("ExpandAll"), LocaleMgr.action.getImageIcon("ExpandAll"));
        setEnabled(false);
        setDefaultToolBarVisibility(false);
        ApplicationContext.getFocusManager().addCurrentFocusListener(this);
    }

    protected final void doActionPerformed() {
        Object obj = ApplicationContext.getFocusManager().getFocusObject();
        if (!(obj instanceof ExplorerView))
            return;

        ExplorerView explorerTree = ((ExplorerView) (obj));
        TreePath[] treePaths = explorerTree.getSelectionPaths();
        explorerTree.expandAllUnder(treePaths);
    }

    public void performAction(DbObject dbObject) {
        try {
            if (dbObject == null)
                doActionPerformed();
            else {
                ExplorerView explorerView = ApplicationContext.getDefaultMainFrame()
                        .getExplorerPanel().getMainView();
                explorerView.find(dbObject);
                TreePath[] treePaths = explorerView.getSelectionPaths();
                explorerView.expandAllUnder(treePaths);
            }
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
    }

    public final void currentFocusChanged(Object oldFocusObject, Object focusObject)
            throws DbException {
        setEnabled(focusObject instanceof ExplorerView);
    }
}
