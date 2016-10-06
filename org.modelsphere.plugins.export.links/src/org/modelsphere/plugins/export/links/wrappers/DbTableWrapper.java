/*************************************************************************

Copyright (C) 2009 neosapiens inc./MSSS

This file is part of Link Report plug-in.

Link Report plug-in is free software; you can redistribute it and/or modify
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

Link Report plug-in has been developed by neosapiens inc. for
the purposes of the Ministère de la Santé et des Services Sociaux
du Québec (MSSS).
 
You can reach neosapiens inc. at: 
  http://www.neosapiens.com

 **********************************************************************/
package org.modelsphere.plugins.export.links.wrappers;

import java.util.HashMap;
import java.util.Map;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORTable;

public class DbTableWrapper {
    private DbDataModelWrapper m_parent;
    private DbORTable m_table;

    DbTableWrapper(DbDataModelWrapper parent, DbORTable table) {
        m_parent = parent;
        m_table = table;
    }

    public DbDataModelWrapper getParent() {
        return m_parent;
    }

    public DbDataModelWrapper getDataModel(DbProjectWrapper project) throws DbException {
        return m_parent;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getName() {
        String name;
        try {
            name = m_table.getName();
        } catch (DbException ex) {
            name = super.toString();
        } //end try

        return name;
    }

    private Map<DbORColumn, DbColumnWrapper> m_columns = new HashMap<DbORColumn, DbColumnWrapper>();

    public DbColumnWrapper getColumn(DbORColumn col) throws DbException {
        DbColumnWrapper column = m_columns.get(col);
        if (column == null) {
            column = new DbColumnWrapper(this, col);
            m_columns.put(col, column);
        }

        return column;
    }

}
