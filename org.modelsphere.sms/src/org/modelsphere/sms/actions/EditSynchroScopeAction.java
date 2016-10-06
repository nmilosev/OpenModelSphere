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

import org.modelsphere.jack.actions.AbstractDomainAction;
import org.modelsphere.sms.SMSIntegrateModel;
import org.modelsphere.sms.plugins.TargetSystem;

// Internal action from Debug menu
public class EditSynchroScopeAction extends AbstractDomainAction {

    private static final int[] rootIds = new int[] { TargetSystem.SGBD_IBM_DB2_ROOT,
            TargetSystem.SGBD_INFORMIX_ROOT, TargetSystem.SGBD_ORACLE_ROOT };
    private static final String[] rootNames = new String[] { "IBM", "Informix", "Oracle" }; // NOT LOCALIZABLE, hidden feature

    public EditSynchroScopeAction() {
        super("Edit synchro scope", false); // NOT LOCALIZABLE, hidden feature
        setDomainValues(rootNames);
        setEnabled(true);
    }

    protected final void doActionPerformed() {
        SMSIntegrateModel.editSynchroScope(rootIds[getSelectedIndex()]);
    }
}
