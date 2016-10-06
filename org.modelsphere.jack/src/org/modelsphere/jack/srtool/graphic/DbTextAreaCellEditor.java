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
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.graphic.zone.*;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;

public class DbTextAreaCellEditor extends TextAreaCellEditor {

    private String className;
    private MetaField metaField;

    public DbTextAreaCellEditor(MetaField metaField) {
        this.className = null;
        this.metaField = metaField;
    }

    public DbTextAreaCellEditor(String className, MetaField metaField) {
        this.className = className;
        this.metaField = metaField;
    }

    public final void setText() {
        String text = "";
        DbObject semObj = (DbObject) value.getObject();
        try {
            semObj.getDb().beginTrans(Db.READ_TRANS);
            text = (String) semObj.get(metaField);
            semObj.getDb().commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                    ((ApplicationDiagram) diagram).getMainView(), ex);
        }
        textArea.setText(text);
    }

    public final void stopEditing(int endCode) {
        if (endCode == CellEditor.CANCEL)
            return;
        DbObject semObj = (DbObject) value.getObject();
        Db db = semObj.getDb();
        String clsName = (className != null ? className : semObj.getMetaClass().getGUIName());
        try {
            String pattern = LocaleMgr.action.getString("change01");
            db.beginTrans(Db.WRITE_TRANS, MessageFormat.format(pattern, new Object[] { clsName,
                    metaField.getGUIName() }));
            semObj.set(metaField, textArea.getText());
            db.commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                    ((ApplicationDiagram) diagram).getMainView(), ex);
        }
    }
}
