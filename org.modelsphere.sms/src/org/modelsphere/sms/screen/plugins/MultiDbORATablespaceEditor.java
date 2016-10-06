/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.screen.plugins;

import java.util.ArrayList;
import java.util.Collection;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.screen.plugins.MultiDbSemanticalObjectEditor;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.oracle.db.DbORATablespace;

public class MultiDbORATablespaceEditor extends MultiDbSemanticalObjectEditor {

    private DbORDatabase dbDatabase;
    private DbEnumeration dbEnum;

    protected Collection getSelectionSet(DbObject parentDbo) throws DbException {
        ArrayList tablespaces = new ArrayList();
        DbORDataModel model = (DbORDataModel) parentDbo.getCompositeOfType(DbORDataModel.metaClass);
        if (model != null) {
            DbORDatabase database = model.getDeploymentDatabase();
            if (database != null) {
                dbEnum = database.getComponents().elements(DbORATablespace.metaClass);
                while (dbEnum.hasMoreElements())
                    tablespaces.add(dbEnum.nextElement());
                dbEnum.close();
            }
        }
        /** Composite classes of Database */
        else {
            dbEnum = parentDbo.getCompositeOfType(DbORDatabase.metaClass).getComponents().elements(
                    DbORATablespace.metaClass);
            while (dbEnum.hasMoreElements())
                tablespaces.add(dbEnum.nextElement());
            dbEnum.close();
        }
        return tablespaces;
    }

    protected final boolean isDisplayFullName() {
        return false;
    }
}
