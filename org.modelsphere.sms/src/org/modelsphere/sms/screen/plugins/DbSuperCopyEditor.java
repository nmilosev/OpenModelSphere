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
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.screen.DefaultRenderer;
import org.modelsphere.jack.baseDb.screen.plugins.DbSemanticalObjectEditor;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.db.util.AnySemObject;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.util.AnyORObject;

public final class DbSuperCopyEditor extends DbSemanticalObjectEditor {

    protected final Collection getSelectionSet(DbObject dbo) throws DbException {
        ArrayList superDbos = new ArrayList();
        if (dbo instanceof DbORAssociation) {
            // An association can have as super any association in the
            // super-schema
            // linking the super-tables of the association tables.
            DbORAbsTable frontTable = ((DbORAssociation) dbo).getFrontEnd().getClassifier();
            DbORAbsTable backTable = ((DbORAssociation) dbo).getBackEnd().getClassifier();
            DbORAbsTable frontSuperTable = (DbORAbsTable) frontTable.getSuperCopy();
            DbORAbsTable backSuperTable = (DbORAbsTable) backTable.getSuperCopy();
            if (frontSuperTable == null || backSuperTable == null)
                return superDbos; // no possible super if the association tables
            // have no super-tables
            DbEnumeration dbEnum = frontSuperTable.getAssociationEnds().elements();
            while (dbEnum.hasMoreElements()) {
                DbORAssociationEnd end = (DbORAssociationEnd) dbEnum.nextElement();
                if (end.getOppositeEnd().getClassifier() != backSuperTable)
                    continue;
                if (frontSuperTable == backSuperTable && end.isFrontEnd())
                    continue; // if recursive association, skip one of the ends
                // to not have it twice in the list
                DbORAssociation superDbo = (DbORAssociation) end.getComposite();
                // If the candidate super-association has already a
                // sub-association in this schema, skip it.
                DbSMSSemanticalObject subDbo = AnySemObject.getSubCopy(superDbo,
                        (DbSMSSemanticalObject) dbo.getComposite());
                if (subDbo == null || subDbo == dbo)
                    superDbos.add(superDbo);
            }
            dbEnum.close();
        } else {
            // An object can have a super only if its parent has a super.
            DbSMSSemanticalObject parent = (DbSMSSemanticalObject) dbo.getComposite();
            DbSMSSemanticalObject superParent = AnySemObject.getSuperCopy(parent);
            if (superParent == null)
                return superDbos;
            MetaClass metaClass = dbo.getMetaClass();
            int i = AnyORObject.getMetaClassIndex(metaClass);
            if (i != -1)
                metaClass = AnyORObject.ORMetaClasses[i];
            DbEnumeration dbEnum = superParent.getComponents().elements(metaClass);
            while (dbEnum.hasMoreElements()) {
                DbSMSSemanticalObject superDbo = (DbSMSSemanticalObject) dbEnum.nextElement();
                // If the candidate super has already a sub in this schema, skip
                // it.
                DbSMSSemanticalObject subDbo = AnySemObject.getSubCopy(superDbo, parent);
                if (subDbo == null || subDbo == dbo)
                    superDbos.add(superDbo);
            }
            dbEnum.close();
        }
        return superDbos;
    }

    protected final String getStringForNullValue() {
        return DefaultRenderer.kNone;
    }

    protected final boolean isDisplayFullName() {
        return false;
    }
}
