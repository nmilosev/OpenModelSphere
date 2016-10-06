/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.actions;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DiagramImage;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.style.FormatFrame;

@SuppressWarnings("serial")
public final class FormatAction extends AbstractApplicationAction implements
        SelectionActionListener {

    public FormatAction() {
        super(LocaleMgr.action.getString("formatStyles"));
        this.setMnemonic(LocaleMgr.action.getMnemonic("formatStyles"));
        setEnabled(false);
    }

    protected final void doActionPerformed() {
        DbSMSProject proj = (DbSMSProject)ApplicationContext.getFocusManager().getCurrentProject(); 
        FormatFrame.showFormatFrame(proj);
    }

    public final void updateSelectionAction() throws DbException {
        if (!(ApplicationContext.getFocusManager().getFocusObject() instanceof ApplicationDiagram)) {
            setEnabled(false);
            return;
        }

        Object[] objects = ApplicationContext.getFocusManager().getSelectedObjects();
        for (int i = 0; i < objects.length; i++) {
            if ((objects[i] instanceof DiagramImage) || !(objects[i] instanceof ActionInformation)) {
                setEnabled(false);
                setVisible(false);
                return;
            }
        }
        setEnabled(true);
        setVisible(true);
    }
}
