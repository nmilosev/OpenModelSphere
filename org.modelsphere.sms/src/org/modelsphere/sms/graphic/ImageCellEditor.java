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
package org.modelsphere.sms.graphic;

import java.awt.Rectangle;

import javax.swing.JComponent;

import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.ZoneBox;
import org.modelsphere.jack.graphic.zone.CellEditor;
import org.modelsphere.jack.graphic.zone.CellID;
import org.modelsphere.jack.graphic.zone.ZoneCell;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.actions.ChangeStampImageAction;
import org.modelsphere.sms.actions.SMSActionsStore;

/**
 * @author nicolask
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences -
 *         Java - Code Style - Code Templates
 */
public class ImageCellEditor implements CellEditor {

    public ImageCellEditor() {
    }

    public JComponent getComponent(ZoneBox box, CellID cellID, ZoneCell value, DiagramView view,
            Rectangle cellRect) {

        ChangeStampImageAction smsAction = (ChangeStampImageAction) ApplicationContext
                .getActionStore().get(SMSActionsStore.CHANGE_STAMP_IMAGE);
        smsAction.performAction();

        return null;
    }

    public void stopEditing(int endCode) {

    }

}
