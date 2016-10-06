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
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.CurrentProjectEvent;
import org.modelsphere.jack.srtool.CurrentProjectListener;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.style.StyleFrame;

public final class StyleAction extends AbstractApplicationAction implements CurrentProjectListener {

    public StyleAction() {
        super(LocaleMgr.action.getString("projectStyles"));
        this.setMnemonic(LocaleMgr.action.getMnemonic("projectStyles"));
        setEnabled(false);
        ApplicationContext.getFocusManager().addCurrentProjectListener(this);
    }

    protected final void doActionPerformed() {
        StyleFrame.showStyleFrame((DbSMSProject) ApplicationContext.getFocusManager()
                .getCurrentProject());
    }

    public final void currentProjectChanged(CurrentProjectEvent cpe) throws DbException {
        setEnabled(cpe.getProject() != null);
    }
}
