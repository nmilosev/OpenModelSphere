/*************************************************************************

Copyright (C) 2009 by neosapiens inc.

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

You can reach neosapiens inc. at: 

neosapiens inc.
1236 Gaudias-Petitclerc
Qu&eacute;bec, Qc, G1Y 3G2
CANADA
Telephone: 418-561-8403
Fax: 418-650-2375
http://www.neosapiens.com/
Email: marco.savard@neosapiens.com
       gino.pelletier@neosapiens.com

 **********************************************************************/

package com.neosapiens.plugins.codegen.wrappers;

import java.util.*;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.sms.or.db.*;

public class DbTableWrapper extends DbObjectWrapper {
    private DbORTable m_table;

    public DbTableWrapper(DbORTable table) {
        m_table = table;
    }

    public StringWrapper getName() throws DbException {
        StringWrapper sw = new StringWrapper(m_table.getName());
        return sw;
    }

    public StringWrapper getPhysicalName() throws DbException {
        StringWrapper sw = new StringWrapper(m_table.getPhysicalName());
        return sw;
    }

    public StringWrapper getNamespace() throws DbException {
        DbORDataModel model = (DbORDataModel) m_table.getCompositeOfType(DbORDataModel.metaClass);
        String name = model.getName();
        StringWrapper sw = new StringWrapper(name);
        return sw;
    }

    //attribute: a column which is not a primary nor a foreign column
    public List<DbColumnWrapper> getAttributes() throws DbException {
        List<DbColumnWrapper> columns = new ArrayList<DbColumnWrapper>();

        //for each field
        DbRelationN relN = m_table.getComponents();
        DbEnumeration enu = relN.elements(DbORColumn.metaClass);
        while (enu.hasMoreElements()) {
            DbORColumn col = (DbORColumn) enu.nextElement();

            if (!isPrimary(col) && (!isForeign(col)) && (!isVersion(col))) {
                DbColumnWrapper cw = new DbColumnWrapper(col);
                columns.add(cw);
            }
        } //end while
        enu.close();

        return columns;
    } //end getAttributes()

    private boolean isVersion(DbORColumn col) throws DbException {
        String name = col.getPhysicalName();
        boolean version = "version".equals(name);
        return version;
    }

    private Map<DbORTable, DbORPrimaryUnique> tablePK = new HashMap<DbORTable, DbORPrimaryUnique>();

    private boolean isPrimary(DbORColumn column) throws DbException {
        DbORTable table = (DbORTable) column.getCompositeOfType(DbORTable.metaClass);
        DbORPrimaryUnique pk = tablePK.get(table);
        if (pk == null) {
            DbEnumeration enu = table.getComponents().elements(DbORPrimaryUnique.metaClass);
            while (enu.hasMoreElements()) {
                DbORPrimaryUnique puk = (DbORPrimaryUnique) enu.nextElement();
                if (puk.isPrimary()) {
                    pk = puk;
                    break;
                }
            } //end while
            enu.close();

            tablePK.put(table, pk);
        } //end if

        boolean primary = false;
        if (pk != null) {
            DbEnumeration enu = pk.getColumns().elements(DbORColumn.metaClass);
            while (enu.hasMoreElements()) {
                DbORColumn c = (DbORColumn) enu.nextElement();
                if (column.equals(c)) {
                    primary = true;
                    break;
                }
            } //end while
            enu.close();
        }

        return primary;
    } //end isPrimary()

    private boolean isForeign(DbORColumn column) throws DbException {
        boolean isForeign = false;

        DbEnumeration enu = column.getFKeyColumns().elements(DbORFKeyColumn.metaClass);
        isForeign = enu.hasMoreElements();
        enu.close();

        return isForeign;
    }

    public List<DbORAssociationEndWrapper> getReferences() throws DbException {
        List<DbORAssociationEndWrapper> references = new ArrayList<DbORAssociationEndWrapper>();

        DbRelationN relN = m_table.getAssociationEnds();
        DbEnumeration enu = relN.elements(DbORAssociationEnd.metaClass);
        while (enu.hasMoreElements()) {
            DbORAssociationEnd end = (DbORAssociationEnd) enu.nextElement();
            DbORAssociationEndWrapper ref = new DbORAssociationEndWrapper(end);
            references.add(ref);
        } //end while
        enu.close();

        return references;
    } //end getReferences()

} //end ProjectWrapper
