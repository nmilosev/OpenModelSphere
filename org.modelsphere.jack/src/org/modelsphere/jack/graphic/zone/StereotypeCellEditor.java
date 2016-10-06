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

/*
 * Created on Aug 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.modelsphere.jack.graphic.zone;

import java.util.ArrayList;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;

/**
 * @author nicolask
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences -
 *         Java - Code Style - Code Templates
 */
public abstract class StereotypeCellEditor extends ComboBoxCellEditor {

    protected MetaField metaField = null;
    protected ArrayList<DbObject> domValues = null;

    public StereotypeCellEditor(MetaField mf) {
        metaField = mf;
    }

    public void populateCombo() {
        // TODO Auto-generated method stub
    }

    public void stopEditing(int endCode) {
        if (endCode == CellEditor.CANCEL)
            return;
        DbObject semObj = (DbObject) value.getObject();
        try {
            String pattern = LocaleMgr.action.getString("change01");
            semObj.getDb().beginTrans(
                    Db.WRITE_TRANS,
                    MessageFormat.format(pattern, new Object[] {
                            semObj.getMetaClass().getGUIName(), metaField.getGUIName() }));
            int idx = combo.getSelectedIndex();
            if (idx != -1) {
                if (domValues.size() > 0) {
                    DbObject dbo = domValues.get(idx);
                    semObj.set(metaField, dbo);
                }
            } else
                semObj.set(metaField, null);

            semObj.getDb().commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                    ((ApplicationDiagram) diagram).getMainView(), ex);
        }

    }

    public String toString() {
        if (value != null)
            return value.toString();
        else
            return null;
    }
}
