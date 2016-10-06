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

package org.modelsphere.jack.baseDb.screen.model;

import java.util.Vector;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.screen.Renderer;
import org.modelsphere.jack.baseDb.screen.ScreenView;

public class DbDescriptionModel extends javax.swing.AbstractListModel implements DescriptionModel,
        DbRefreshListener {

    protected ScreenView screenView;
    protected DbObject semObj;
    private Vector fields = new Vector();
    private boolean hasChanged = false;

    public DbDescriptionModel(ScreenView newScreenView, DbObject newSemObject) {
        screenView = newScreenView;
        semObj = newSemObject;
        installTriggers();
    }

    private void installTriggers() {
        if (semObj != null) {
            semObj.addDbRefreshListener(this);
            DbUDFValue.fValue.addDbRefreshListener(this);
        }
    }

    private void removeTriggers() {
        if (semObj != null) {
            semObj.removeDbRefreshListener(this);
            DbUDFValue.fValue.removeDbRefreshListener(this);
        }
    }

    // //////////////////////////////////////
    // DbRefreshListener SUPPORT
    //
    public final void refreshAfterDbUpdate(DbUpdateEvent evt) throws DbException {
        if (evt.metaField == DbUDFValue.fValue) {
            DbUDFValue udfValue = (DbUDFValue) evt.dbo;
            if (semObj == udfValue.getDbObject())
                refreshUDFValue(udfValue);
        } else if (evt.metaField == DbObject.fUdfValues) {
            refreshUDFValue((DbUDFValue) evt.neighbor);
        } else {
            int row = getPropertyRowIndex(evt.metaField);
            if (row != -1) {
                DescriptionField field = (DescriptionField) fields.elementAt(row);
                Object value = field.getRenderer().wrapValue(evt.dbo, evt.dbo.get(evt.metaField));
                setElementAt(value, row, true);

                int nb = fields.size();
                for (int i = 0; i < nb; i++) {
                    Object cField = fields.elementAt(i);
                    if (cField instanceof ComputedDescriptionField
                            && ((ComputedDescriptionField) cField).affectsValue(evt.metaField)) {
                        ((ComputedDescriptionField) cField).computeValue();
                        fireContentsChanged(this, i, i);
                    }
                }
            }
        }
    }

    private void refreshUDFValue(DbUDFValue udfValue) throws DbException {
        DbUDF udf;
        Object val;
        if (udfValue.getTransStatus() == Db.OBJ_REMOVED) {
            udf = (DbUDF) udfValue.getOld(DbObject.fComposite);
            val = null;
        } else {
            udf = (DbUDF) udfValue.getComposite();
            val = udfValue.getValue();
        }
        int row = getPropertyRowIndex(udf);
        if (row != -1) {
            DescriptionField field = (DescriptionField) fields.elementAt(row);
            Object value = field.getRenderer().wrapValue(semObj, val);
            setElementAt(value, row, true);
        }
    }

    //
    // End of DbRefreshListener SUPPORT
    // //////////////////////////////////////

    public final int getPropertyRowIndex(MetaField metaField) {
        for (int i = 0; i < fields.size(); i++) {
            Object field = fields.elementAt(i);
            if (field instanceof MetaFieldDescriptionField
                    && ((MetaFieldDescriptionField) field).getMetaField() == metaField)
                return i;
        }
        return -1;
    }

    public final int getPropertyRowIndex(DbUDF udf) {
        for (int i = 0; i < fields.size(); i++) {
            Object field = fields.elementAt(i);
            if (field instanceof UDFDescriptionField
                    && ((UDFDescriptionField) field).getDbUDF() == udf)
                return i;
        }
        return -1;
    }

    public final DbObject getDbObject() {
        return semObj;
    }

    public final String getGUIName(int index) {
        DescriptionField df = getDescriptionFieldAt(index);
        if (df != null)
            return df.getGUIName();
        else
            return new String("");
    }

    public final Class getPropertyClass(int index) {
        return getDescriptionFieldAt(index).getPropertyClass();
    }

    public final String getEditorName(int index) {
        DescriptionField f = getDescriptionFieldAt(index);
        String name = f.getEditorName();
        return name;
    }

    public final Renderer getRenderer(int row, int col) {
        return getDescriptionFieldAt(row).getRenderer();
    }

    public final Object getParentValue(int row) {
        return semObj;
    }

    public final int getSize() {
        if (fields == null)
            return 0;
        return fields.size();
    }

    public final Object getElementAt(int index) {
        return getDescriptionFieldAt(index).getWrappedValue();
    }

    public final DescriptionField getDescriptionField(int index) {
        return getDescriptionFieldAt(index);
    }

    public final boolean isEnabled(int index) {
        return getDescriptionFieldAt(index).isEnabled();
    }

    public final boolean isEditable(int index) {
        return getDescriptionFieldAt(index).isEditable();
    }

    public final void setElementAt(Object value, int index) {
        setElementAt(value, index, false);
    }

    private void setElementAt(Object value, int index, boolean refresh) {
        DescriptionField field = (DescriptionField) fields.elementAt(index);
        if (refresh) {
            if (field.hasChanged())
                return;
        } else {
            if (DbObject.valuesAreEqual(field.getValue(), field.getRenderer().unwrapValue(value)))
                return;
            field.setHasChanged();
            hasChanged = true; // set hasChanged before firing contentsChanged
        }
        field.setWrappedValue(value);
        fireContentsChanged(this, index, index);
    }

    public final ScreenView getScreenView() {
        return screenView;
    }

    public final boolean hasChanged() {
        return hasChanged;
    }

    public final void commit() throws DbException {
        int i, nb;
        if (hasChanged) {
            for (i = 0, nb = fields.size(); i < nb; i++) {
                DescriptionField field = (DescriptionField) fields.elementAt(i);
                if (field.hasChanged())
                    field.setValueToDbObject();
            }
        }
    }

    public final void resetHasChanged() {
        hasChanged = false;
        int i, nb;
        for (i = 0, nb = fields.size(); i < nb; i++)
            ((DescriptionField) fields.elementAt(i)).resetHasChanged();
    }

    public final void refresh() throws DbException {
        for (int i = 0; i < fields.size(); i++) {
            DescriptionField field = (DescriptionField) fields.elementAt(i);
            if (field instanceof MetaFieldDescriptionField) {
                MetaField metaField = ((MetaFieldDescriptionField) field).getMetaField();
                Object value = field.getRenderer().wrapValue(semObj, semObj.get(metaField));
                setElementAt(value, i, true);
            } else if (field instanceof ComputedDescriptionField) {
                ((ComputedDescriptionField) field).computeValue();
                fireContentsChanged(this, i, i);
            } else if (field instanceof UDFDescriptionField) {
                DbUDF udf = ((UDFDescriptionField) field).getDbUDF();
                if (udf.isAlive()
                        && ((UDFDescriptionField) field).getPropertyClass() == udf.getValueClass()) {
                    Object value = field.getRenderer().wrapValue(semObj, semObj.get(udf));
                    setElementAt(value, i, true);
                }
            }
        }
    }

    public final DescriptionField getDescriptionFieldAt(int index) {
        DescriptionField f = (DescriptionField) fields.elementAt(index);
        return f;
    }

    public final void addDescriptionField(DescriptionField field) {
        fields.addElement(field);
        int indexAdd = fields.size();
        fireIntervalAdded(this, indexAdd, indexAdd);
    }

    public final void removeDescriptionField(DescriptionField field) {
        fields.remove(field);
        int indexAdd = fields.size();
        fireIntervalAdded(this, indexAdd, indexAdd);
    }

    public void dispose() {
        removeTriggers();
        fields = null;
    }
}
