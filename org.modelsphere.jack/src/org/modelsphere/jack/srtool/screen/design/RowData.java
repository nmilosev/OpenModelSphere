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
package org.modelsphere.jack.srtool.screen.design;

import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.UDFValueType;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.baseDb.screen.ScreenPlugins;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.DefaultComparableElement;

final class RowData {
    Object[] values;
    MetaField metafield;
    MetaClass metaclass; // This value is null if metaclasses for selection
    // are different
    DbUDF udf;
    Class udfType;
    String udfName;
    boolean equalValues = true;
    boolean editable = true;
    boolean marked = false;
    DefaultComparableElement sValue = null; // Apply to DbObject value only
    // (to avoid a transaction for
    // each cell to repaint)
    String dbValueFullQualifiedName = null; // Apply to DbObject value only
    // (to avoid a transaction for
    // each cell to repaint)

    String renderer = null;
    String editor = null;

    RowData(MetaField metafield) {
        this.metafield = metafield;
    }

    RowData(DbUDF udf) {
        this.metafield = DbUDFValue.fValue;
        this.udf = udf;
    }

    void load(DbObject[] dbos) throws DbException {
        DbMultiTrans.beginTrans(Db.READ_TRANS, dbos, "");
        values = new Object[dbos.length];
        Object value0 = null;
        for (int i = 0; i < dbos.length; i++) {
            if (udf == null)
                values[i] = dbos[i].get(metafield);
            else {
                values[i] = dbos[i].get(udf);
            }

            if (i == 0) {
                value0 = values[0];
                metaclass = dbos[i].getMetaClass();
            } else if (equalValues && !(value0 == null && values[i] == null)) {
                if (value0 == null || values[i] == null || !values[i].equals(value0))
                    equalValues = false;
            }

            MetaClass metaclass2 = dbos[i].getMetaClass();
            if (metaclass != null && metaclass2 != metaclass)
                metaclass = null;

        }
        if (udf != null) {
            udfName = udf.getName();
            udfType = udf.getValueClass();
        }

        if (equalValues && value0 instanceof DbObject && metafield != DbUDFValue.fValue) {
            sValue = new DefaultComparableElement(value0, ApplicationContext.getSemanticalModel()
                    .getDisplayText((DbObject) value0, DesignPanel.class), ApplicationContext
                    .getSemanticalModel().getImage((DbObject) value0, DesignPanel.class));
        }
        if (equalValues && value0 instanceof DbSemanticalObject && metafield != DbUDFValue.fValue) {
            dbValueFullQualifiedName = ApplicationContext.getSemanticalModel().getDisplayText(
                    (DbObject) value0, DesignPanel.class);
        }
        Db db = null;
        for (int i = 0; i < dbos.length && editable && metafield != DbUDFValue.fValue; i++) {
            editable = ApplicationContext.getSemanticalModel().isEditable(metafield, dbos[i],
                    DesignPanel.class);

            if (!editable || !(metafield instanceof MetaRelationship))
                continue;
            if (db == null)
                db = dbos[i].getDb();
            else if (dbos[i].getDb() != db)
                editable = false;
        }
        installEditorAndRenderer();
        DbMultiTrans.commitTrans(dbos);

    }

    private void installEditorAndRenderer() throws DbException {
        String pluginName = null;
        if (metafield == DbUDFValue.fValue) {
            if (udf.getValueType().getValue() == UDFValueType.TEXT)
                pluginName = "LookupDescription"; // NOT LOCALIZABLE
            else {
                pluginName = udf.getValueClass().getName();
                pluginName = pluginName.substring(pluginName.lastIndexOf('.') + 1);
            }
        } else
            pluginName = ScreenPlugins.getPluginName(metafield);
        int i = pluginName != null ? pluginName.indexOf(';') : -1;
        if (i != -1) {
            renderer = pluginName.substring(0, i);
            editor = pluginName.substring(i + 1);
        } else {
            renderer = editor = pluginName;
        }
    }

}
