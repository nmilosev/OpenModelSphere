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

package org.modelsphere.sms.or.screen.model;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.baseDb.screen.ScreenView;
import org.modelsphere.jack.baseDb.screen.model.DbListModel;
import org.modelsphere.jack.baseDb.screen.model.DescriptionModel;
import org.modelsphere.jack.baseDb.screen.model.ReflectionDescriptionModel;
import org.modelsphere.sms.or.db.DbORAbstractMethod;
import org.modelsphere.sms.or.db.DbORCheck;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTrigger;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.oracle.db.DbORANestedTableStorage;

public class ORListModel extends DbListModel {

    public ORListModel(ScreenView screenView, DbObject semObj, MetaRelationN[] listRelations,
            MetaClass listClass) throws DbException {
        super(screenView, semObj, listRelations, listClass);
    }

    protected final ReflectionDescriptionModel createColumnDescriptionModel() throws DbException {
        return new ORDescriptionModel(screenView, semObj, listClass, listRelations);
    }

    protected final DescriptionModel createRowModel(DbObject dbObj) throws DbException {
        return new ORDescriptionModel(screenView, dbObj, columnModel);
    }

    protected boolean dbObjectIsAllowed(DbObject dbObj) throws DbException {
        if ((listClass == DbORAbstractMethod.metaClass)
                && (listRelations[0] == DbORUser.fOperations) && (dbObj instanceof DbORTrigger))
            return false;

        if (this.semObj instanceof DbORTrigger && dbObj instanceof DbORColumn)
            return true;
        if (this.semObj instanceof DbORTrigger && dbObj instanceof DbORTrigger)
            return true;
        if (this.semObj instanceof DbORTable && dbObj instanceof DbORTable
                && listClass.equals(DbORANestedTableStorage.metaClass))
            return true;
        if (this.semObj instanceof DbORCheck && dbObj instanceof DbORCheck)
            return true;
        if (this.semObj instanceof DbORPrimaryUnique && dbObj instanceof DbORPrimaryUnique)
            return true;

        return super.dbObjectIsAllowed(dbObj);
    }
}
