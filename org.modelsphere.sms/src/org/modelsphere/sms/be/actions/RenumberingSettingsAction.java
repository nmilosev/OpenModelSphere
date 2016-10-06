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
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEContextGo;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.features.Renumbering;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.db.DbSMSDiagram;

public final class RenumberingSettingsAction extends AbstractApplicationAction implements
        SelectionActionListener {
    private static final String ACTION_NAME = LocaleMgr.action.getString("RenumberingParameters");

    RenumberingSettingsAction() {
        super(ACTION_NAME + "...");
        setEnabled(false);
    }

    protected final void doActionPerformed() {
        // get dialog
        MainFrame mainframe = MainFrame.getSingleton();
        Renumbering.showDialog(mainframe);
    } // end doActionPerformed()

    // Check that all objects in the selection belong to a single project.
    public final void updateSelectionAction() throws DbException {
        DbObject[] semObjs = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();

        boolean state = false;
        if (semObjs.length > 0 && ApplicationContext.getFocusManager().getCurrentProject() != null) {
            // state = false;
            for (int i = 0; i < semObjs.length; i++) {
                DbObject semObj = semObjs[i];
                if (((semObj instanceof DbBEModel) || (semObj instanceof DbBEContextGo)
                        || (semObj instanceof DbBEFlow) || (semObj instanceof DbBEActor)
                        || (semObj instanceof DbBEStore) || (semObj instanceof DbBEUseCase))) {
                    state = true;
                    break;
                }
            }
        } else {
            ApplicationDiagram appDiag = ApplicationContext.getFocusManager().getActiveDiagram();
            if (appDiag != null) {
                DbSMSDiagram diagGo = (DbSMSDiagram) appDiag.getDiagramGO();
                if (diagGo instanceof DbBEDiagram)
                    state = true;
            }
        }
        setEnabled(state);
    }

    protected int getFeatureSet() {
        return SMSFilter.BPM;
    }
}
