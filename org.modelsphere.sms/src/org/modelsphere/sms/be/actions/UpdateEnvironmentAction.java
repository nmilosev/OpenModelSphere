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

import javax.swing.JOptionPane;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.features.UpdateEnvironment;
import org.modelsphere.sms.be.international.LocaleMgr;

public final class UpdateEnvironmentAction extends AbstractApplicationAction implements
        SelectionActionListener {

    public static final String kUpdateEnv = LocaleMgr.action.getString("UpdateEnvironment");
    public static final String kUpdateEnvMsg = LocaleMgr.action
            .getString("UpdateEnvironmentMessage");

    UpdateEnvironmentAction() {
        super(LocaleMgr.action.getString("UpdateEnvironment"));
        setEnabled(false);
        setVisible(ScreenPerspective.isFullVersion());
    }

    protected final void doActionPerformed() {
        DbObject selectedDbObjs[] = ApplicationContext.getFocusManager()
                .getSelectedSemanticalObjects();
        Object selectedObj = null;
        if (selectedDbObjs.length == 0)
            selectedObj = ApplicationContext.getFocusManager().getActiveDiagram();

        if (selectedDbObjs.length == 0 && selectedObj != null) {
            ApplicationDiagram diag = (ApplicationDiagram) selectedObj;
            if (!(diag.getDiagramGO() instanceof DbBEDiagram))
                return;
            selectedObj = diag.getSemObj();
        } else
            selectedObj = selectedDbObjs[0];

        DbObject dbo = (DbObject) selectedObj;
        try {
            dbo.getDb().beginWriteTrans(kUpdateEnv);
            UpdateEnvironment updateEnv = new UpdateEnvironment();
            boolean updated = updateEnv.execute((DbBEUseCase) dbo);
            dbo.getDb().commitTrans();
            if (!updated) {
                JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(),
                        kUpdateEnvMsg, ApplicationContext.getApplicationName(),
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (DbException e) {
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
        }
    }

    public final void updateSelectionAction() throws DbException {
        setEnabled(false);
        DbObject selectedDbObjs[] = ApplicationContext.getFocusManager()
                .getSelectedSemanticalObjects();
        Object selectedObj = null;
        if (selectedDbObjs == null)
            return;

        if (selectedDbObjs.length == 0) {
            selectedObj = ApplicationContext.getFocusManager().getActiveDiagram();
            if (selectedObj == null)
                return;
        }

        if (selectedDbObjs.length == 0 && selectedObj != null) {
            ApplicationDiagram diag = (ApplicationDiagram) selectedObj;
            if (diag.getDiagramGO() instanceof DbBEDiagram)
                setVisible(true);
            else
                setVisible(false);
            selectedObj = diag.getSemObj();
        } else
            selectedObj = selectedDbObjs[0];

        if (!(selectedObj instanceof DbBEUseCase)) {
            setEnabled(false);
            return;
        }
        setEnabled(true);
        setVisible(true);
    }

    protected int getFeatureSet() {
        return SMSFilter.BPM;
    }
}
