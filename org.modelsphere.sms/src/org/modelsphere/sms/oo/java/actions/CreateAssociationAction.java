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

package org.modelsphere.sms.oo.java.actions;

import java.awt.event.ActionEvent;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.db.DbSMSAssociation;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.oo.db.DbOOAssociationEnd;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.international.LocaleMgr;

final class CreateAssociationAction extends AbstractApplicationAction implements
        SelectionActionListener {

    CreateAssociationAction() {
        super(LocaleMgr.action.getString("createAssociation"));
    }

    protected final void doActionPerformed(ActionEvent e) {
        GraphicComponent gc = getGraphicComponent(e);
        if (gc == null)
            return;
        DbSMSClassifierGo go = (DbSMSClassifierGo) ((ActionInformation) gc).getGraphicalObject();
        DbObject[] semObjs = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        org.modelsphere.sms.oo.java.features.JVCreateAssociation.doIt(go,
                (DbJVDataMember) semObjs[0]);
    }

    // This action is available only from a diagram, with a single data member
    // selected.
    public final void updateSelectionAction() throws DbException {
        ApplicationDiagram diag = ApplicationContext.getFocusManager().getActiveDiagram();
        DbObject[] semObjs = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        boolean state = false;
        if (diag != null && semObjs.length == 1 && semObjs[0] instanceof DbJVDataMember) {
            DbOOAssociationEnd end = ((DbJVDataMember) semObjs[0]).getAssociationEnd();
            if (end == null
                    || ((DbSMSAssociation) end.getComposite()).getAssociationGos().size() == 0)
                state = true;
        }
        setEnabled(state);
    }

    protected int getFeatureSet() {
        return SMSFilter.JAVA;
    }

}
