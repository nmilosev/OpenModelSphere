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

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.screen.*;

public abstract class DescriptionField {

    private String guiName;
    private String editorName;
    private Renderer renderer = DefaultRenderer.singleton;
    private Object wrappedValue = null;
    private DbObject dbObject;
    private int displayWidth = 80;
    private DescriptionModel model;
    protected boolean editable = true;
    private boolean hasChanged = false;
    private boolean bEnabled = true;

    public boolean isEnabled() {
        return bEnabled;
    }

    public void setEnabled(boolean enabled) {
        bEnabled = enabled;
    }

    public DescriptionField(DescriptionModel newModel, DbObject newDbObject) throws DbException {
        model = newModel;
        dbObject = newDbObject;
    }

    public final void setEditable(boolean state) {
        editable = state;
    }

    public final DescriptionModel getDescriptionModel() {
        return model;
    }

    public final DbObject getDbObject() {
        return dbObject;
    }

    // overriden
    public String getGUIName() {
        return guiName;
    }

    public final void setGUIName(String name) {
        guiName = name;
    }

    public final String getEditorName() {
        return editorName;
    }

    public final void setEditorName(String editorName) {
        this.editorName = editorName;
    }

    public final Renderer getRenderer() {
        return renderer;
    }

    public final void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public final int getDisplayWidth() {
        return displayWidth;
    }

    public final void setDisplayWidth(int width) {
        displayWidth = width;
    }

    public final Object getWrappedValue() {
        return wrappedValue;
    }

    public final Object getValue() {
        return renderer.unwrapValue(wrappedValue);
    }

    public final void setWrappedValue(Object value) {
        wrappedValue = value;
    }

    public final void setValue(Object value) throws DbException {
        if (dbObject != null)
            wrappedValue = renderer.wrapValue(dbObject, value);
    }

    public final boolean hasChanged() {
        return hasChanged;
    }

    public final void setHasChanged() {
        hasChanged = true;
    }

    public final void resetHasChanged() {
        hasChanged = false;
    }

    public final boolean isEditable() {
        return editable;
    }

    // must be overridden if field editable
    public void setValueToDbObject() throws DbException {
    }

    public abstract Class getPropertyClass();
}
