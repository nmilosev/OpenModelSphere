/*************************************************************************

Copyright (C) 2009 Grandite

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

You can redistribute and/or modify this particular file even under the
terms of the GNU Lesser General Public License (LGPL) as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

You should have received a copy of the GNU Lesser General Public License 
(LGPL) along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.jack.srtool.graphic;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.Domain;
import org.modelsphere.jack.baseDb.db.srtypes.SrBoolean;
import org.modelsphere.jack.baseDb.db.srtypes.SrType;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.graphic.zone.CellEditor;
import org.modelsphere.jack.graphic.zone.ComboBoxCellEditor;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;

public class DomainCellEditor extends ComboBoxCellEditor {

    private MetaField metaField;

    public DomainCellEditor(MetaField metaField) {
        this.metaField = metaField;
    }

    public final void populateCombo() {
        DbObject semObj = (DbObject) value.getObject();
        try {
            semObj.getDb().beginTrans(Db.READ_TRANS);

            // Domain domValue = (Domain)semObj.get(metaField);

            Domain domValue = null;
            Object o = semObj.get(metaField);
            if (o instanceof Boolean)
                domValue = new SrBoolean((Boolean) o);
            else
                domValue = (Domain) o;

            semObj.getDb().commitTrans();
            Domain[] domValues = domValue.getObjectPossibleValues();
            for (int i = 0; i < domValues.length; i++)
                combo.addItem(domValues[i]);
            combo.setSelectedItem(domValue);
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                    ((ApplicationDiagram) diagram).getMainView(), ex);
        }
    }

    public final void stopEditing(int endCode) {
        if (endCode == CellEditor.CANCEL)
            return;
        DbObject semObj = (DbObject) value.getObject();
        try {
            String pattern = LocaleMgr.action.getString("change01");
            semObj.getDb().beginTrans(
                    Db.WRITE_TRANS,
                    MessageFormat.format(pattern, new Object[] {
                            semObj.getMetaClass().getGUIName(), metaField.getGUIName() }));
            semObj.set(metaField, combo.getSelectedItem());
            semObj.getDb().commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                    ((ApplicationDiagram) diagram).getMainView(), ex);
        }
    }
}
