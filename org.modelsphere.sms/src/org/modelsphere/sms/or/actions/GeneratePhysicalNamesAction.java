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
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORModel;
import org.modelsphere.sms.or.db.DbORUserNode;
import org.modelsphere.sms.or.features.PhysicalNameGeneration;
import org.modelsphere.sms.or.international.LocaleMgr;

final class GeneratePhysicalNamesAction extends AbstractApplicationAction implements
        SelectionActionListener {

    private static final String kGenPhysicalNames = LocaleMgr.action.getString("genPhysicalNames");

    GeneratePhysicalNamesAction() {
        super(kGenPhysicalNames);
        this.setMnemonic(LocaleMgr.action.getMnemonic("genPhysicalNames"));
        setEnabled(false);
    }

    public final void updateSelectionAction() throws DbException {
        DbObject[] semObjs = ORActionFactory.getActiveObjects();
        boolean enable = (semObjs == null) ? false : (semObjs.length > 0);

        if (enable) {
            for (int i = 0; i < semObjs.length; i++) {
                DbObject semObj = semObjs[i];

                if (!((semObj instanceof DbORModel) || (semObj instanceof DbORCommonItemModel)
                        || (semObj instanceof DbORUserNode) || (semObj instanceof DbBEModel)
                        || (semObj instanceof DbBEUseCase) || (semObj instanceof DbORDiagram) || (semObj instanceof DbBEDiagram))) {
                    enable = false;
                    break;
                } // end if
            } // end for
        } // end if

        setEnabled(enable);
    } // end updateSelectionAction()

    protected final void doActionPerformed() {
        try {
            DbObject[] semObjs = ORActionFactory.getActiveObjects();
            PhysicalNameGeneration generator = new PhysicalNameGeneration();
            generator.generate(ApplicationContext.getDefaultMainFrame(), semObjs);
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    } // end doActionPerformed()

    protected int getFeatureSet() {
        return SMSFilter.RELATIONAL | SMSFilter.BPM;
    }
} // end GeneratePhysicalNamesAction

