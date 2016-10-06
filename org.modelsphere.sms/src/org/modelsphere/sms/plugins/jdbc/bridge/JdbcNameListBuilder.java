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

package org.modelsphere.sms.plugins.jdbc.bridge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.modelsphere.jack.srtool.reverse.jdbc.JdbcBuilder;

/**
 * This service executes JDBC API calls
 **/
public class JdbcNameListBuilder extends JdbcBuilder {
    private static final String USERSEP = ".";
    private static final String OCCSEP = ",";
    private ArrayList nameList = new ArrayList();
    private ArrayList typeList = new ArrayList();
    private ArrayList tableSortList = new ArrayList();
    private ArrayList viewSortList = new ArrayList();
    private ArrayList procSortList = new ArrayList();
    private String sortBuffer = null;
    private String userNames = null;

    JdbcNameListBuilder() {
    }

    /* Types */
    protected void buildTypeName(String typeName) throws Exception {
        typeList.add(typeName.concat(OCCSEP));
    };

    /* Table Owner */
    protected void buildTableSchema(String schemaName) throws Exception {
        sortBuffer = null;
        sortBuffer = schemaName == null ? "" : schemaName.concat(USERSEP);
    };

    /* Table Name */
    protected final void buildTableName(String tableName) throws Exception {
        sortBuffer = sortBuffer.concat(tableName + OCCSEP);
        tableSortList.add(sortBuffer);
    };

    /* View Owner */
    protected void buildViewSchema(String schemaName) throws Exception {
        sortBuffer = null;
        sortBuffer = schemaName == null ? "" : schemaName.concat(USERSEP);
    };

    /* View Name */
    protected final void buildViewName(String viewName) throws Exception {
        sortBuffer = sortBuffer.concat(viewName + OCCSEP);
        viewSortList.add(sortBuffer);
    };

    /* Procedure Owner */
    protected void buildProcSchema(String tokenProcSchema) throws Exception {
        sortBuffer = null;
        sortBuffer = tokenProcSchema == null ? "" : tokenProcSchema.concat(USERSEP);
    };

    /* Procedure Name */
    protected void buildProcName(String tokenProcName) throws Exception {
        sortBuffer = sortBuffer.concat(tokenProcName + OCCSEP);
        procSortList.add(sortBuffer);
    };

    void installUser(int index) {
        nameList.add(index, userNames);
    }

    void installType(int index) {
        String typeNames = new String();
        Collections.sort(typeList);
        for (int i = 0; i < typeList.size(); i++) {
            typeNames = typeNames.concat((String) typeList.get(i));
        }
        nameList.add(index, typeNames);
    }

    void installTable(int index) {
        String tableNames = new String();
        Collections.sort(tableSortList);
        for (int i = 0; i < tableSortList.size(); i++) {
            tableNames = tableNames.concat((String) tableSortList.get(i));
        }
        nameList.add(index, tableNames);
    }

    void installView(int index) {
        String viewNames = new String();
        Collections.sort(viewSortList);
        for (int i = 0; i < viewSortList.size(); i++) {
            viewNames = viewNames.concat((String) viewSortList.get(i));
        }
        nameList.add(index, viewNames);
    }

    void installProc(int index) {
        String procNames = new String();
        Collections.sort(procSortList);
        for (int i = 0; i < procSortList.size(); i++) {
            procNames = procNames.concat((String) procSortList.get(i));
        }
        nameList.add(index, procNames);
    }

    List getNameList() {
        return nameList;
    }
}
