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
package org.modelsphere.sms.oo.graphic;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.zone.ZoneCell;
import org.modelsphere.jack.srtool.graphic.DbTextFieldCellEditor;
import org.modelsphere.jack.srtool.graphic.SrLineLabel;
import org.modelsphere.sms.db.DbSMSStructuralFeature;
import org.modelsphere.sms.oo.international.LocaleMgr;

public class RoleLabel extends SrLineLabel {
    public RoleLabel(Diagram diag, DbSMSStructuralFeature newDm, OOAssociation newOoAssociation,
            MetaField offsetMF) throws DbException {
        super(diag, newDm, newOoAssociation, offsetMF, "");
    }

    // overriding to instal an editor
    public void setValue(String value) {
        DbTextFieldCellEditor tfEditor = new DbTextFieldCellEditor(
                LocaleMgr.misc.getString("Role"), DbSemanticalObject.fName, false);
        zone.setValue(new ZoneCell(semObj, value, null, tfEditor));
    }
}
