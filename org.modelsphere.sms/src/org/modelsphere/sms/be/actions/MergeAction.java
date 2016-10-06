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

package org.modelsphere.sms.be.actions;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.features.MergeDialog;
import org.modelsphere.sms.be.features.MergeUseCase;
import org.modelsphere.sms.be.international.LocaleMgr;

public final class MergeAction extends AbstractApplicationAction implements SelectionActionListener {
    MergeAction() {
        super(LocaleMgr.action.getString("Merge") + "...");
        setEnabled(false);
    }

    protected final void doActionPerformed() {
        DbObject[] seldbos = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        DbBEUseCase source = (DbBEUseCase) seldbos[0];
        DbBEUseCase target = MergeDialog.selectTarget(source);
        if (target == null)
            return;
        new MergeUseCase(source, target, MergeDialog.update, MergeDialog.mergeQualifiers,
                MergeDialog.mergeResources, MergeDialog.mergeComments);
    }

    public final void updateSelectionAction() throws DbException {
        DbObject[] seldbos = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        if (seldbos == null || seldbos.length != 1 || !(seldbos[0] instanceof DbBEUseCase)) {
            setEnabled(false);
            return;
        }
        DbBEUseCase usecase = (DbBEUseCase) seldbos[0];
        usecase.getDb().beginReadTrans();
        setEnabled(usecase.isContext());
        usecase.getDb().commitTrans();
    }

    protected int getFeatureSet() {
        return SMSFilter.BPM;
    }
}
