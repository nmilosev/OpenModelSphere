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
import javax.swing.KeyStroke;

import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.features.DbFindSession;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public class FindPreviousAction extends AbstractFindAction implements SelectionActionListener {

    public FindPreviousAction() {
        super(LocaleMgr.action.getString("findPrevious"), LocaleMgr.action
                .getImageIcon("findPrevious"));
        this.setMnemonic(LocaleMgr.action.getMnemonic("findPrevious"));
        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, ActionEvent.SHIFT_MASK));
        setEnabled(false);
        setDefaultToolBarVisibility(false);
    }

    protected final void doActionPerformed() {
        findNext(DbFindSession.BACKWARD);
    }

    // need one project.
    public final void updateSelectionAction() throws DbException {
        setEnabled(isValidForFindNext()
                && ApplicationContext.getFocusManager().getCurrentProject() != null);
    }
}
