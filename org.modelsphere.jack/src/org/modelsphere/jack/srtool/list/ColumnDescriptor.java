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

package org.modelsphere.jack.srtool.list;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.util.DbPath;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.list.ListTableModel;

/**
 * This class represent a column available for the ListTableModel. A ColumnDescriptor object is
 * created for each columns defined in model ListDescriptor object
 */
final class ColumnDescriptor {
    Object object; // MetaField, DbUDF, DbPath, ListColumn or MetaClass.class (for object type)
    String name; // The column displayed name
    public boolean bDisabled = false;
    ListTableModel model;

    ColumnDescriptor(ListTableModel model, DbObject dbo, Object obj) throws DbException {
        this.model = model;
        object = obj;
        if (object instanceof MetaField) {
            MetaField mf = (MetaField) object;
            if (dbo == null) {
                name = mf.getGUIName();
            } else {
                name = ApplicationContext.getSemanticalModel().getDisplayText(
                        model.neighborsMetaClass, (MetaField) object, dbo, ColumnDescriptor.class,
                        true);
                if (name == null) {
                    bDisabled = true;
                    name = ApplicationContext.getSemanticalModel().getDisplayText(
                            model.neighborsMetaClass, mf, dbo, ColumnDescriptor.class);
                    if (name == null)
                        name = mf.getGUIName();
                }
            }
        }

        else if (object instanceof DbPath)
            name = ((DbPath) object).toString();
        else if (object instanceof ListColumn)
            name = ((ListColumn) object).getTitle();
        else if (object instanceof DbUDF)
            name = ((DbUDF) object).getName();
        else
            name = "";
    }

    ColumnDescriptor(ListTableModel model, Object obj, String name) {
        this.model = model;
        object = obj;
        this.name = name;
    }

    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof ColumnDescriptor)) {
            return false;
        }
        return ((ColumnDescriptor) obj).object == object;
    }

    public String getID() {
        String id = ""; // NOT LOCALIZABLE
        if (object instanceof MetaField)
            id += ((MetaField) object).getJField().getName();
        else if (object instanceof DbPath)
            id += ((DbPath) object).getMetaField().getJField().getName();
        else if (object instanceof DbUDF)
            id += "udf_" + name; // NOT LOCALIZABLE
        else if (object instanceof ListColumn)
            id += ((ListColumn) object).getID();
        else
            // object type
            id += "metaclass"; // NOT LOCALIZABLE
        return id;
    }

    public String toString() {
        return name;
    }

}
