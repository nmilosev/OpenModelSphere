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

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.plugins.export.links.international.LocaleMgr;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTypeClassifier;

public class DbColumnWrapper {
    private static final String UNDEFINED = LocaleMgr.misc.getString("Undefined");
    private static final String YES = LocaleMgr.misc.getString("Yes");
    private static final String NO = LocaleMgr.misc.getString("No");
    private static final String RUBRIQUE_SUFFIX = "(RUBRIQUE)";
    private static final String RUBRIQUE_STEREOTYPE = "rubrique";

    private DbTableWrapper m_parent;
    private DbORColumn m_column;

    DbColumnWrapper(DbTableWrapper parent, DbORColumn column) throws DbException {
        m_parent = parent;
        m_column = column;
    }

    public DbTableWrapper getParent() {
        return m_parent;
    }

    public DbORColumn getDbColumn() {
        return m_column;
    }

    public String getName() {
        String tableName = m_parent.getName();
        String columnName;

        try {
            columnName = m_column.getName();
        } catch (DbException ex) {
            columnName = "???";
        }

        String name = tableName + "." + columnName;
        return name;
    }

    public String getDescription() {
        String description;

        try {
            description = m_column.getDescription();
        } catch (DbException ex) {
            description = null;
        }

        return description;
    }

    public String getModelName() throws DbException {
        DbORDataModel model = (DbORDataModel) m_column.getCompositeOfType(DbORDataModel.metaClass);
        String name = model.getName();
        return name;
    }

    @Override
    public String toString() {
        String name;
        try {
            name = m_parent.getName() + "." + m_column.getName();
        } catch (DbException ex) {
            name = super.toString();
        } //end try

        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DbColumnWrapper)) {
            return false;
        }

        DbColumnWrapper that = (DbColumnWrapper) o;
        boolean equals = this.m_column.equals(that.m_column);
        return equals;
    }

    DbColumnWrapper getSuperColumn(DbProjectWrapper project) throws DbException {
        DbORColumn sc = (DbORColumn) m_column.getSuperCopy(); //physical super-column

        //if no physical super-column, look for logical super-column
        if (sc == null) {
            sc = getLogicalSuperColumn();
        }

        //if neither physical nor logical super-column, return null
        if (sc == null) {
            return null;
        }

        DbORTable t = (DbORTable) sc.getCompositeOfType(DbORTable.metaClass);
        DbORDataModel dm = (DbORDataModel) t.getCompositeOfType(DbORDataModel.metaClass);
        DbDataModelWrapper dataModel = project.getDataModel(dm);
        DbTableWrapper table = dataModel.getTable(t);
        DbColumnWrapper superColumn = new DbColumnWrapper(table, sc);
        return superColumn;
    }

    private DbORColumn getLogicalSuperColumn() throws DbException {
        DbORColumn logicalSuperColumn = null;
        DbORTable t1 = (DbORTable) m_column.getCompositeOfType(DbORTable.metaClass);
        boolean rubrique = isRubrique(t1);

        if (rubrique) {
            DbORTable t2 = findParentTable(t1);
            DbORTable t0 = (t2 == null) ? null : (DbORTable) t2.getSuperCopy();
            logicalSuperColumn = (t0 == null) ? null : findColumn(t0, t1.getName());
        }

        return logicalSuperColumn;
    }

    private DbORTable findParentTable(DbORTable t1) throws DbException {
        DbORTable parent = null;
        DbRelationN relN = t1.getAssociationEnds();
        DbEnumeration enu = relN.elements(DbORAssociationEnd.metaClass);
        while (enu.hasMoreElements()) {
            DbORAssociationEnd end = (DbORAssociationEnd) enu.nextElement();
            int mult = end.getMultiplicity().getValue();
            if (mult == SMSMultiplicity.EXACTLY_ONE) {
                DbORAssociationEnd oppEnd = end.getOppositeEnd();
                DbORAbsTable t = oppEnd.getClassifier();

                if (t instanceof DbORTable) {
                    parent = (DbORTable) t;
                    break;
                }
            } //end if
        } //end while
        enu.close();

        return parent;
    }

    //find, if any, the column of 'table' whose name is 'columnName' 
    private DbORColumn findColumn(DbORTable table, String columnName) throws DbException {
        DbORColumn namedColumn = null;
        DbRelationN relN = table.getComponents();
        DbEnumeration enu = relN.elements(DbORColumn.metaClass);
        while (enu.hasMoreElements()) {
            DbORColumn c = (DbORColumn) enu.nextElement();
            if (columnName.equals(c.getName())) {
                namedColumn = c;
                break;
            }
        } //end while
        enu.close();

        return namedColumn;
    }

    public String getFormat() {
        String format;

        try {
            DbORTypeClassifier type = m_column.getType();
            String typename = (type == null) ? UNDEFINED : type.getName();
            String len = m_column.getLengthNbDecimal();
            len = (len == null) ? "" : len;
            format = typename + len;
        } catch (DbException ex) {
            format = "???";
        } //end if

        return format;
    }

    public String getNullable() {
        String nullable;

        try {
            nullable = m_column.isNull() ? YES : NO;
        } catch (DbException ex) {
            nullable = "???";
        } //end if

        return nullable;
    }

    public String getUdfValue(String udfName) {
        String value;

        try {
            Object o = m_column.getUDF(udfName);
            value = (o == null) ? "" : o.toString();
        } catch (DbException ex) {
            value = "???";
        }

        return value;
    }

    private boolean isRubrique(DbORTable table) {
        boolean rubrique;

        try {
            String tableName = table.getName();
            DbSMSStereotype stereotype = table.getUmlStereotype();
            rubrique = (tableName == null) ? false : tableName.endsWith(RUBRIQUE_SUFFIX);
            rubrique |= (stereotype == null) ? false : RUBRIQUE_STEREOTYPE.equals(stereotype
                    .getName());
        } catch (DbException ex) {
            rubrique = false;
        }

        return rubrique;
    }

    public boolean isRubrique() {
        boolean rubrique;

        try {
            String name = m_column.getName();
            DbSMSStereotype stereotype = m_column.getUmlStereotype();
            rubrique = (name == null) ? false : name.endsWith(RUBRIQUE_SUFFIX);
            rubrique |= (stereotype == null) ? false : RUBRIQUE_STEREOTYPE.equals(stereotype
                    .getName());
        } catch (DbException ex) {
            rubrique = false;
        }

        return rubrique;
    }

}
