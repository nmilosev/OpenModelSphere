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

package org.modelsphere.sms.be.graphic;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.zone.CellEditor;
import org.modelsphere.jack.graphic.zone.ComboBoxCellEditor;
import org.modelsphere.jack.graphic.zone.StereotypeCellEditor;
import org.modelsphere.jack.graphic.zone.ZoneCell;
import org.modelsphere.jack.srtool.graphic.DbTextAreaCellEditor;
import org.modelsphere.jack.srtool.graphic.DbTextFieldCellEditor;
import org.modelsphere.jack.srtool.graphic.DomainCellEditor;
import org.modelsphere.jack.srtool.graphic.SrLineLabel;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSUmlExtensibility;
import org.modelsphere.sms.graphic.SMSStereotypeEditor;
import org.modelsphere.sms.screen.plugins.DbSMSStereotypeEditor;

public class BEFlowLabel extends SrLineLabel {
    private boolean editable = false;
    private MetaField mf = null;

    public BEFlowLabel(Diagram diag, DbObject semObj, BEFlow flow, MetaField offsetMF,
            String labelZoneName) throws DbException {
        super(diag, semObj, flow, offsetMF, labelZoneName);
    }

    public BEFlowLabel(Diagram diag, DbObject semObj, BEFlow flow, MetaField offsetMF,
            boolean editable, MetaField mf, String labelZoneName) throws DbException {
        super(diag, semObj, flow, offsetMF, labelZoneName);
        this.editable = editable;
        this.mf = mf;
    }

    // overriding to instal an editor
    public void setValue(String value) {
        CellEditor editor = null;
        if (editable) {
            if (mf == DbBEFlow.fDescription)
                editor = new DbTextAreaCellEditor(LocaleMgr.action.getString("flowLabel"), mf);
            else if (mf == DbBEFlow.fUmlStereotype)
                editor = new SMSStereotypeEditor(mf);
            else
                editor = new DbTextFieldCellEditor(LocaleMgr.action.getString("flowLabel"), mf,
                        false);
        }
        zone.setValue(new ZoneCell(semObj, value, null, editor));
    }
}
