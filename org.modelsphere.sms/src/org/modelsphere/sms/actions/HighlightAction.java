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

import org.modelsphere.jack.actions.AbstractTriStatesAction;
import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.DiagramImage;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.international.LocaleMgr;

public final class HighlightAction extends AbstractTriStatesAction implements
        SelectionActionListener {

    public HighlightAction() {
        super(LocaleMgr.action.getString("Highlight"), LocaleMgr.action.getImageIcon("Highlight"));
        setEnabled(false);
    }

    protected final void doActionPerformed() {
        performTriState(ApplicationContext.getFocusManager().getSelectedObjects(), LocaleMgr.action
                .getString("HighlightUpdate"));
    }

    public final void updateSelectionAction() throws DbException {
        updateTriState(ApplicationContext.getFocusManager().getSelectedObjects());
    }

    protected final Boolean getObjectValue(Object obj) throws DbException {
        if ((obj instanceof DiagramImage)
                || !(obj instanceof ActionInformation)
                || !(((ActionInformation) obj).getGraphicalObject() instanceof DbSMSGraphicalObject)) {
            return null;
        }
        DbSMSGraphicalObject go = (DbSMSGraphicalObject) ((ActionInformation) obj)
                .getGraphicalObject();
        if (go == null)
            return null;
        MetaField metaField = go.getMetaField("m_highlight"); // NOT LOCALIZABLE
        if (metaField != null)
            return (Boolean) go.find(metaField);
        return null;

    }

    protected final void setObjectValue(Object obj, Boolean value) throws DbException {
        if ((obj instanceof DiagramImage)
                || !(obj instanceof ActionInformation)
                || !(((ActionInformation) obj).getGraphicalObject() instanceof DbSMSGraphicalObject)) {
            return;
        }
        DbSMSGraphicalObject go = (DbSMSGraphicalObject) ((ActionInformation) obj)
                .getGraphicalObject();
        MetaField metaField = go.getMetaField("m_highlight"); // NOT LOCALIZABLE
        if (metaField != null)
            go.set(metaField, value);
    }

}
