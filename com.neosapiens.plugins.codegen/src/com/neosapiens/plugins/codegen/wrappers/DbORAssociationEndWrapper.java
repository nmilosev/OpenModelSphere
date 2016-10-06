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

import java.util.ArrayList;
import java.util.List;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.or.db.*;

public class DbORAssociationEndWrapper extends DbObjectWrapper {
    private DbORAssociationEnd m_end;

    public DbORAssociationEndWrapper(DbORAssociationEnd end) {
        m_end = end;
    }

    public List<DbColumnWrapper> getColumns() throws DbException {
        List<DbColumnWrapper> columnList = new ArrayList<DbColumnWrapper>();
        DbORForeign f = m_end.getMember();
        if (f != null) {
            DbEnumeration enu = f.getComponents().elements(DbORFKeyColumn.metaClass);

            while (enu.hasMoreElements()) {
                DbORFKeyColumn fColumn = (DbORFKeyColumn) enu.nextElement();
                DbORColumn c = fColumn.getColumn();
                DbColumnWrapper col = new DbColumnWrapper(c);
                columnList.add(col);
            }
            enu.close();
        } //end if

        return columnList;
    }

    public StringWrapper getName() throws DbException {
        String s = m_end.getName();
        return new StringWrapper(s);
    }

    public StringWrapper getPhysicalName() throws DbException {
        StringWrapper sw = new StringWrapper(m_end.getPhysicalName());
        return sw;
    }

    public StringWrapper getType() throws DbException {
        DbORAssociationEnd oppEnd = m_end.getOppositeEnd();
        DbORTable oppTable = (DbORTable) oppEnd.getClassifier();
        String s = oppTable.getPhysicalName();
        return new StringWrapper(s);
    }

    public DbORAssociationEndWrapper getOpposite() throws DbException {
        DbORAssociationEnd oppEnd = m_end.getOppositeEnd();
        DbORAssociationEndWrapper opposite = new DbORAssociationEndWrapper(oppEnd);
        return opposite;
    }

    public String getMin() throws DbException {
        SMSMultiplicity mult = m_end.getMultiplicity();
        String min = mult.getDatarunMinimumMultiplicityLabel();
        return min;
    }

    public String getMax() throws DbException {
        SMSMultiplicity mult = m_end.getMultiplicity();
        String max = mult.getDatarunMaximumMultiplicityLabel();
        return max;
    }

    public boolean isNull() throws DbException {
        int mult = m_end.getMultiplicity().getValue();
        boolean nullable = (mult == SMSMultiplicity.OPTIONAL) || (mult == SMSMultiplicity.MANY);
        return nullable;
    }

    public boolean isMandatory() throws DbException {
        int mult = m_end.getMultiplicity().getValue();
        boolean mandatory = (mult == SMSMultiplicity.EXACTLY_ONE)
                || (mult == SMSMultiplicity.ONE_OR_MORE);
        return mandatory;
    }

    public boolean isMany() throws DbException {
        int mult = m_end.getMultiplicity().getValue();
        boolean many = (mult == SMSMultiplicity.MANY) || (mult == SMSMultiplicity.ONE_OR_MORE);
        return many;
    }

    public boolean isNavigable() throws DbException {
        boolean navigable = m_end.isNavigable();
        return navigable;
    }

}
