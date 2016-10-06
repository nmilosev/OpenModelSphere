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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.plugins.export.links.international.LocaleMgr;
import org.modelsphere.sms.db.DbSMSLink;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORTable;

public class DbLinkWrapper {
    private static final String NO_DESTINATION = LocaleMgr.misc.getString("NoDestination");
    private static final String X_COLUMNS = LocaleMgr.misc.getString("0Columns");

    private DbLinkModelWrapper m_parent;
    private DbSMSLink m_link;

    public DbLinkWrapper(DbLinkModelWrapper parent, DbSMSLink link) {
        m_parent = parent;
        m_link = link;
    }

    public List<DbColumnWrapper> getSourceColumns() throws DbException {
        List<DbColumnWrapper> columns = new ArrayList<DbColumnWrapper>();
        DbProjectWrapper project = m_parent.getParent();

        DbRelationN relN = m_link.getSourceObjects();
        DbEnumeration enu = relN.elements(DbORColumn.metaClass);
        while (enu.hasMoreElements()) {
            DbORColumn col = (DbORColumn) enu.nextElement();
            DbORTable t = (DbORTable) col.getCompositeOfType(DbORTable.metaClass);
            DbORDataModel dm = (DbORDataModel) t.getCompositeOfType(DbORDataModel.metaClass);
            DbDataModelWrapper dataModel = project.getDataModel(dm);
            DbTableWrapper table = dataModel.getTable(t);
            DbColumnWrapper column = table.getColumn(col);
            columns.add(column);
        } //end while
        enu.close();

        return columns;
    }

    public List<DbColumnWrapper> getDestinationColumns() throws DbException {
        List<DbColumnWrapper> columns = new ArrayList<DbColumnWrapper>();
        DbProjectWrapper project = m_parent.getParent();

        DbRelationN relN = m_link.getTargetObjects();
        DbEnumeration enu = relN.elements(DbORColumn.metaClass);
        while (enu.hasMoreElements()) {
            DbORColumn col = (DbORColumn) enu.nextElement();
            DbORTable t = (DbORTable) col.getCompositeOfType(DbORTable.metaClass);
            DbORDataModel dm = (DbORDataModel) t.getCompositeOfType(DbORDataModel.metaClass);
            DbDataModelWrapper dataModel = project.getDataModel(dm);
            DbTableWrapper table = dataModel.getTable(t);
            DbColumnWrapper column = table.getColumn(col);
            columns.add(column);

        } //end while
        enu.close();

        return columns;
    }

    public String getDestinationText() throws DbException {
        List<DbColumnWrapper> destinations = new ArrayList<DbColumnWrapper>();
        DbProjectWrapper project = m_parent.getParent();

        DbRelationN relN = m_link.getTargetObjects();
        DbEnumeration enu = relN.elements(DbORColumn.metaClass);
        while (enu.hasMoreElements()) {
            DbORColumn col = (DbORColumn) enu.nextElement();
            DbORTable t = (DbORTable) col.getCompositeOfType(DbORTable.metaClass);
            DbORDataModel dm = (DbORDataModel) t.getCompositeOfType(DbORDataModel.metaClass);
            DbDataModelWrapper dataModel = project.getDataModel(dm);
            DbTableWrapper table = dataModel.getTable(t);
            DbColumnWrapper column = table.getColumn(col);
            destinations.add(column);
        } //end while
        enu.close();

        String destinationName;
        if (destinations.size() == 0) {
            destinationName = NO_DESTINATION;
        } else if (destinations.size() == 1) {
            DbColumnWrapper col = destinations.get(0);
            destinationName = col.getName();
        } else {
            destinationName = MessageFormat.format(X_COLUMNS, destinations.size());
        } //end if 

        return destinationName;
    }

    public String getName() throws DbException {
        return m_link.getName();
    }

    public String getAlias() throws DbException {
        return m_link.getAlias();
    }

}
