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
import org.modelsphere.jack.baseDb.screen.plugins.DbSemanticalObjectEditor;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.db.util.AnyORObject;

public class DbORDatabaseEditor extends DbSemanticalObjectEditor {

    protected Collection getSelectionSet(DbObject parentDbo) throws DbException {
        ArrayList databases = new ArrayList();
        DbEnumeration dbEnum = parentDbo.getProject().componentTree(DbORDatabase.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORDatabase dbo = (DbORDatabase) dbEnum.nextElement();
            if (AnyORObject.getTargetSystem(parentDbo) == AnyORObject.getTargetSystem(dbo)) {
                if (parentDbo instanceof DbORDataModel && dbo.getSchema() != null)
                    continue;
                if (parentDbo instanceof DbORDomainModel
                        && (dbo.getUdtModel() != null || dbo.getDefaultDomainModel() == parentDbo))
                    continue;
                if (parentDbo instanceof DbOROperationLibrary && dbo.getOperationLibrary() != null)
                    continue;
                databases.add(dbo);
            }
        }
        dbEnum.close();
        return databases;
    }
}
