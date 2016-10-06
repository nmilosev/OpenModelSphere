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

package org.modelsphere.jack.baseDb.screen.plugins;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.screen.*;
import org.modelsphere.jack.srtool.ApplicationContext;

// This renderer may be used on <relation 1> fields and on the fields <DbSemanticalObject.fName, fPhysicalName>.
// Supports also <relation-N> and renders only the first neighbor; a <relation-N> is not editable.
public class DbSemObjFullNameRenderer extends DefaultRenderer {

    public static final DbSemObjFullNameRenderer singleton = new DbSemObjFullNameRenderer();

    private static class WrappedValue {
        final Object object;
        final String name;
        final String toolTipText;

        WrappedValue(Object object, String name, String toolTipText) {
            this.object = object;
            this.name = name;
            this.toolTipText = toolTipText;
        }

        public final String toString() {
            return name;
        }
    }

    protected DbSemObjFullNameRenderer() {
    }

    public final Object wrapValue(DbObject dbo, Object value) throws DbException {
        String name, fullName;
        if (value instanceof DbRelationN) {
            DbRelationN dbRelN = (DbRelationN) value;
            value = (dbRelN.size() == 0 ? null : dbRelN.elementAt(0));
        }
        if (value == null) {
            name = fullName = getStringForNullValue(dbo);
        } else {
            int i;
            if (value instanceof DbObject) {
                fullName = getFullName((DbObject) value);
            } else { // fName, fPhysicalName
                fullName = getFullName(dbo);
                i = fullName.lastIndexOf('.');
                fullName = fullName.substring(0, i + 1) + value; // works even if i == -1
            }
            i = fullName.lastIndexOf('.');
            if (i == -1) {
                name = fullName;
            } else {
                name = fullName.substring(i + 1);
                fullName = name + "  (" + fullName.substring(0, i) + ")"; //NOT LOCALIZABLE
            }
        }

        if (isDisplayFullName())
            name = fullName;
        return new WrappedValue(value, name, fullName);
    }

    public final Object unwrapValue(Object value) {
        return ((WrappedValue) value).object;
    }

    protected final void setValue(ScreenView screenView, int row, int column, Object value) {
        WrappedValue wrappedValue = (WrappedValue) value;
        setText(wrappedValue.name);
        if (wrappedValue.toolTipText.length() != 0)
            setToolTipText(wrappedValue.toolTipText);
    }

    protected String getFullName(DbObject dbo) throws DbException {
        return ApplicationContext.getSemanticalModel().getDisplayText(dbo, DbObject.LONG_FORM,
                null, DbSemObjFullNameRenderer.class);
    }

    protected String getStringForNullValue(DbObject dbo) throws DbException {
        return "";
    }

    protected boolean isDisplayFullName() {
        return true;
    }

    public int getDisplayWidth() {
        return (isDisplayFullName() ? 200 : 120);
    }
}
