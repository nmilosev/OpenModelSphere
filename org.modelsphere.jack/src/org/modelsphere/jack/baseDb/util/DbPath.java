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

package org.modelsphere.jack.baseDb.util;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelation1;

public final class DbPath {
    private MetaField metafield;
    private MetaRelation1[] path;
    private String guiName;
    private DbPath parent;

    public DbPath(MetaField metafield) {
        this(null, metafield, null);
    }

    public DbPath(MetaRelation1[] path, MetaField metafield) {
        this(path, metafield, null);
    }

    public DbPath(MetaRelation1[] path, MetaField metafield, String guiName) {
        this.path = path;
        this.metafield = metafield;
        this.guiName = guiName;
    }

    public DbPath(DbPath parent, MetaRelation1[] path, MetaField metafield, String guiName) {
        this.path = path;
        this.metafield = metafield;
        this.guiName = guiName;
        this.parent = parent;
    }

    public Object get(DbObject target) throws DbException {
        if (target == null)
            return null;
        if (metafield == null)
            return null;

        DbObject dbo = target;

        if (parent != null) {
            Object parentResult = parent.get(dbo);
            if (parentResult instanceof DbObject)
                dbo = (DbObject) parentResult;
            else
                return null;
        }
        if (dbo == null)
            return null;

        if (path != null) {
            for (int i = 0; dbo != null && i < path.length; i++) {
                if (!dbo.hasField(path[i])) {
                    dbo = null;
                    break;
                }
                dbo = (DbObject) dbo.get(path[i]);
            }
        }
        if (dbo == null || !dbo.hasField(metafield))
            return null;

        return dbo.get(metafield);
    }

    public MetaRelation1[] getPath() {
        return path;
    }

    public MetaField getMetaField() {
        return metafield;
    }

    public String toString() {
        if (guiName != null)
            return guiName;
        if (metafield == null)
            return null;
        return metafield.getGUIName();
    }
}
