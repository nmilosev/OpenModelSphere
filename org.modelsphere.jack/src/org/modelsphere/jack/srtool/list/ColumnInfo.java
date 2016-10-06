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
import org.modelsphere.jack.baseDb.db.srtypes.UDFValueType;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.screen.ScreenPlugins;
import org.modelsphere.jack.baseDb.util.DbPath;

final class ColumnInfo implements Comparable {
    // A column Info represent a metafield value, an udf, a custom ListColumn or
    // the type of the object.
    MetaField metafield;
    DbUDF udf;
    DbPath path;
    ListColumn customColumn; // This one act as a delegate to obtain all column informations
    boolean objectType = false;
    boolean stringUdf = false;
    public boolean bDisabled = false;

    int preferredIndex = -1; // init index, used if index == -1
    int index = -1; // current index
    int width = -1; // current width
    String title;
    String renderer;
    Integer ordering;

    ColumnInfo(ColumnDescriptor descriptor, int preferredIndex, Integer ordering)
            throws DbException {
        if (descriptor.object instanceof MetaField)
            metafield = (MetaField) descriptor.object;
        else if (descriptor.object instanceof DbUDF) {
            udf = (DbUDF) descriptor.object;
            UDFValueType valuetype = udf.getValueType();
            if (valuetype != null)
                stringUdf = (valuetype.equals(UDFValueType.getInstance(UDFValueType.STRING)) || valuetype
                        .equals(UDFValueType.getInstance(UDFValueType.TEXT)));
        } else if (descriptor.object instanceof DbPath) {
            path = (DbPath) descriptor.object;
        } else if (descriptor.object instanceof ListColumn) {
            customColumn = (ListColumn) descriptor.object;
        } else
            objectType = true;

        this.preferredIndex = preferredIndex;
        title = descriptor.name == null ? "" : descriptor.name;
        this.ordering = ordering;
        initRenderer();
    }

    private void initRenderer() throws DbException {
        String pluginName = null;
        if (metafield == DbObject.fComponents) {
            // sequence
            pluginName = "Integer"; // NOT LOCALIZABLE
        } else if (udf != null) {
            // TODO: NOT EDITABLE - should not use this one
            if (udf.getValueType().getValue() == UDFValueType.TEXT)
                pluginName = "LookupDescription"; // NOT LOCALIZABLE
            else {
                pluginName = udf.getValueClass().getName();
                pluginName = pluginName.substring(pluginName.lastIndexOf('.') + 1); // NOT
                // LOCALIZABLE
            }
        } else if (metafield != null) {
            pluginName = ScreenPlugins.getPluginName(metafield);
        } else if (path != null) {
            pluginName = ScreenPlugins.getPluginName(path.getMetaField());
        } else if (customColumn != null) {
            pluginName = customColumn.getRenderer();
        }

        if (pluginName == null) {
            pluginName = "Default"; // NOT LOCALIZABLE
        }

        int index = pluginName != null ? pluginName.indexOf(';') : -1; // NOT
        // LOCALIZABLE
        if (index != -1)
            renderer = pluginName.substring(0, index);
        else
            renderer = pluginName;

    }

    public int compareTo(Object obj) {
        if (obj == null || !(obj instanceof ColumnInfo))
            return -1;
        ColumnInfo ci = (ColumnInfo) obj;
        int thisindex = index == -1 ? preferredIndex : index;
        int objindex = ci.index == -1 ? ci.preferredIndex : ci.index;
        if (thisindex == objindex)
            return 0;
        if (thisindex < objindex)
            return -1;
        return 1;
    }

    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ColumnInfo)) {
            return false;
        }
        ColumnInfo ci = (ColumnInfo) obj;
        if (this.metafield != null && ci.metafield == this.metafield)
            return true;
        else if (this.udf != null && ci.udf == this.udf)
            return true;
        else if (this.customColumn != null && ci.customColumn == this.customColumn)
            return true;
        else if (ci.objectType && this.objectType)
            return true;
        return false;
    }
}
