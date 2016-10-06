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

package org.modelsphere.sms.plugins.generic.repository;

import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.Connector;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORTable;

/**
 * For a table, lists all its columns by the alphabetical order of their names. <br>
 * Target System : <b>All Except Java</b><br>
 * Type : <b>Connector</b><br>
 */
public final class OrderedTableColumns extends OrderedConnector {

    private PluginSignature m_signature = null;

    public PluginSignature getSignature() {
        if (m_signature == null) {
            m_signature = new PluginSignature("OrderedTableColumns", "$Revision: 4 $",
                    ApplicationContext.APPLICATION_AUTHOR, "$Date: 2009/04/14 14:00p $", 212); // NOT LOCALIZABLE
        }

        return m_signature;
    }

    public OrderedTableColumns() {
    } //Parameter-less constructor required by jack.io.Plugins

    public Connector createInstance(String childRule, String oneChildRule) {
        Connector conn = super.createInstance(DbORTable.fComponents, childRule, oneChildRule,
                DbORColumn.metaClass);
        return conn;
    }
} //end of OrderedTableColumns
