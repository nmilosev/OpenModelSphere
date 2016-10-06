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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORTable;

public class DbDataModelWrapper implements Comparable<DbDataModelWrapper> {
    private List<DbDataModelWrapper> m_subModels = new ArrayList<DbDataModelWrapper>();
    private Map<DbORDataModel, DbDataModelWrapper> m_subModelMap = new HashMap<DbORDataModel, DbDataModelWrapper>();
    private DbORDataModel m_dataModel;

    private DbDataModelWrapper() {
    }

    public static DbDataModelWrapper getInstance(DbORDataModel dbDataModel) {
        DbDataModelWrapper dataModel = new DbDataModelWrapper();
        dataModel.m_dataModel = dbDataModel;

        return dataModel;
    }

    public String getName() {
        String name;
        try {
            name = m_dataModel.getName();
        } catch (DbException ex) {
            name = super.toString();
        } //end try

        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    private Map<DbORTable, DbTableWrapper> m_tables = new HashMap<DbORTable, DbTableWrapper>();

    public DbTableWrapper getTable(DbORTable t) {
        DbTableWrapper table = m_tables.get(t);
        if (table == null) {
            table = new DbTableWrapper(this, t);
            m_tables.put(t, table);
        }

        return table;
    }

    @Override
    public int compareTo(DbDataModelWrapper that) {
        String s1 = this.getName();
        String s2 = that.getName();
        int comparaison = s1.compareTo(s2);
        return comparaison;
    }

    public List<DbDataModelWrapper> getSubModels() {

        try {
            DbRelationN relN = m_dataModel.getComponents();
            DbEnumeration enu = relN.elements(DbORDataModel.metaClass);
            while (enu.hasMoreElements()) {
                DbORDataModel dbModel = (DbORDataModel) enu.nextElement();
                DbDataModelWrapper subModel = m_subModelMap.get(dbModel);

                if (subModel == null) {
                    DbProjectWrapper project = DbProjectWrapper.getInstance();
                    subModel = project.getDataModel(dbModel);
                    m_subModelMap.put(dbModel, subModel);
                    m_subModels.add(subModel);
                }

            } //end while
            enu.close();
        } catch (DbException ex) {
            m_subModelMap.clear();
            m_subModels.clear();
        } //end try 

        return m_subModels;
    } //end getSubModels()

    public boolean isProjectChild() {
        boolean projectChild;

        try {
            DbObject composite = m_dataModel.getComposite();
            projectChild = (composite instanceof DbSMSProject);
        } catch (DbException ex) {
            projectChild = false;
        } //end try 

        return projectChild;
    } //end isProjectChild()
}
