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

package org.modelsphere.sms.or.actions;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.gui.wizard.Wizard;
import org.modelsphere.jack.srtool.reverse.jdbc.ActiveConnectionListener;
import org.modelsphere.jack.srtool.reverse.jdbc.ActiveConnectionManager;
import org.modelsphere.jack.srtool.services.ConnectionMessage;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.or.features.dbms.DBMSDefaultReverseWizardModel;
import org.modelsphere.sms.or.international.LocaleMgr;

public class ReverseDBAction extends AbstractApplicationAction implements ActiveConnectionListener {
    private static final String kReverseDB = LocaleMgr.action.getString("ReverseEngineer_");
    private static final String kProceed = LocaleMgr.screen.getString("Proceed");

    ReverseDBAction() {
        super(kReverseDB);
        this.setMnemonic(LocaleMgr.action.getMnemonic("ReverseEngineer_"));
        setEnabled(false);
        ActiveConnectionManager.addActiveConnectionListener(this);
    }

    protected final void doActionPerformed(java.awt.event.ActionEvent e) {
        doActionPerformed();
    }

    protected final void doActionPerformed() {
        //get current connection
        ConnectionMessage connectionMessage = ActiveConnectionManager.getActiveConnectionMessage();

        DBMSDefaultReverseWizardModel model = new DBMSDefaultReverseWizardModel(connectionMessage);
        Wizard wizard = new Wizard(model, kProceed);
        wizard.setVisible(true);
        //create graphical objects and show the diagram using the action factories

    }

    protected int getFeatureSet() {
        return SMSFilter.RELATIONAL;
    }

    public void activeConnectionChanged(ConnectionMessage cm) {
        setEnabled(cm != null);
    }
}
