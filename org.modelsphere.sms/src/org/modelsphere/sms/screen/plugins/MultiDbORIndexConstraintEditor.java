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
import org.modelsphere.jack.baseDb.screen.MultiDefaultRenderer;
import org.modelsphere.jack.baseDb.screen.plugins.MultiDbSemanticalObjectShortEditor;
import org.modelsphere.sms.or.db.DbORConstraint;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbORIndexKey;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;

/**
 * 
 * Editor for fields: DbORIndex.fConstraint, DbORPrimaryUnique.fIndex, DbORForeign.fIndex
 * 
 */
public final class MultiDbORIndexConstraintEditor extends MultiDbSemanticalObjectShortEditor {

    protected final Collection getSelectionSet(DbObject parentDbo) throws DbException {
        ArrayList dbos = new ArrayList();
        DbEnumeration dbEnum = parentDbo.getComposite().getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            DbObject dbo = dbEnum.nextElement();
            if (parentDbo instanceof DbORIndex) {
                DbORIndex index = null;
                if (dbo instanceof DbORPrimaryUnique)
                    index = ((DbORPrimaryUnique) dbo).getIndex();
                else if (dbo instanceof DbORForeign)
                    index = ((DbORForeign) dbo).getIndex();
                else
                    continue;

                if (index != null && index != parentDbo)
                    continue;
                if (!match((DbORIndex) parentDbo, (DbORConstraint) dbo))
                    continue;
            } else { // parentDbo instanceof DbORPrimaryUnique or DbORForeign
                if (!(dbo instanceof DbORIndex))
                    continue;
                DbORConstraint pufKey = ((DbORIndex) dbo).getConstraint();
                if (pufKey != null && pufKey != parentDbo)
                    continue;
                if (!match((DbORIndex) dbo, (DbORConstraint) parentDbo))
                    continue;
            }
            dbos.add(dbo);
        }
        dbEnum.close();
        return dbos;
    }

    // The index matches the constraint if the index is empty or if it has the
    // same columns as the constraint.
    private boolean match(DbORIndex index, DbORConstraint pufKey) throws DbException {
        DbEnumeration dbEnum = index.getComponents().elements(DbORIndexKey.metaClass);
        boolean match = !dbEnum.hasMoreElements();
        dbEnum.close();
        if (!match)
            match = index.matchesConstraint(pufKey);
        return match;
    }

    protected final String getStringForNullValue() {
        return MultiDefaultRenderer.kNone;
    }
}
