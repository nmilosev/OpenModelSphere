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
package org.modelsphere.sms.oo.java.screen;

import javax.swing.JButton;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.baseDb.screen.DbListView;
import org.modelsphere.jack.baseDb.screen.DbTreeLookupDialog;
import org.modelsphere.jack.baseDb.screen.model.DbListModel;
import org.modelsphere.jack.srtool.SrScreenContext;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVCompilationUnit;
import org.modelsphere.sms.oo.java.db.DbJVConstructor;
import org.modelsphere.sms.oo.java.db.DbJVImport;
import org.modelsphere.sms.oo.java.db.DbJVInheritance;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.oo.java.db.srtypes.JVClassCategory;
import org.modelsphere.sms.oo.java.db.util.AnyAdt;
import org.modelsphere.sms.oo.java.international.LocaleMgr;
import org.modelsphere.sms.oo.java.screen.model.JVListModel;

public class JVListView extends DbListView {

    public JVListView(DbObject semObj, MetaClass listClass) throws DbException {
        this(semObj, DbObject.fComponents, listClass, ADD_DEL_ACTIONS | REINSERT_ACTION);
    }

    public JVListView(DbObject semObj, MetaRelationN listRelation, MetaClass listClass,
            int actionMode) throws DbException {
        super(SrScreenContext.singleton, semObj, listRelation, listClass, actionMode);
    }

    public JVListView(DbObject semObj, MetaRelationN[] listRelations, MetaClass listClass,
            int actionMode) throws DbException {
        super(SrScreenContext.singleton, semObj, listRelations, listClass, actionMode);
    }

    protected DbListModel createListModel() throws DbException {
        return new JVListModel(this, semObj, listRelations, listClass);
    }

    protected final void initLinkButton(JButton button) {
        if (listClass == DbJVImport.metaClass)
            LocaleMgr.screen.initAbstractButton(button, "AddImports");
        else
            super.initLinkButton(button);
    }

    protected final void initUnlinkButton(JButton button) {
        if (listClass == DbJVImport.metaClass)
            LocaleMgr.screen.initAbstractButton(button, "DeleteImports");
        else
            super.initUnlinkButton(button);
    }

    protected final void createLinkComponent(DbObject parent) throws DbException {
        if (listClass == DbJVInheritance.metaClass) {
            new DbJVInheritance((DbJVClass) semObj, (DbJVClass) parent);
        } else if (listClass == DbJVImport.metaClass) {
            new DbJVImport((DbJVCompilationUnit) semObj, (DbSMSSemanticalObject) parent,
                    Boolean.FALSE);
        }
    }

    protected final DbObject[] showLinkDialog() {
        if (listClass == DbJVImport.metaClass)
            return showImportLinkDialog(); // display a tree of packages and
        // classes
        return super.showLinkDialog(); // display a flat list of linkable
        // objects
    }

    private DbObject[] showImportLinkDialog() {
        return DbTreeLookupDialog.selectMany(this, DbJVImport.metaClass.getGUIName(true, false),
                new DbObject[] { semObj.getProject() }, new MetaClass[] { DbJVPackage.metaClass,
                        DbJVClass.metaClass }, null);
    }

    protected final DbEnumeration getLinkableEnum() throws DbException {
        if (listClass == DbJVInheritance.metaClass) {
            int mask = 1 << JVClassCategory.INTERFACE;
            int cat = ((DbJVClass) semObj).getStereotype().getValue();
            if (cat == JVClassCategory.CLASS)
                mask |= 1 << JVClassCategory.CLASS;
            if (cat == JVClassCategory.EXCEPTION)
                mask |= 1 << JVClassCategory.EXCEPTION;
            return AnyAdt.getClassEnum(semObj.getProject(), mask);
        }
        if (listRelations[0] == DbJVMethod.fJavaExceptions
                || listRelations[0] == DbJVConstructor.fJavaExceptions) {
            return AnyAdt.getClassEnum(semObj.getProject(), 1 << JVClassCategory.EXCEPTION);
        }
        if (listRelations[0] == DbJVCompilationUnit.fClasses) {
            return semObj.getComposite().getComponents().elements(DbJVClass.metaClass);
        }
        return super.getLinkableEnum();
    }

    protected final String getLinkableObjName(DbObject dbo) throws DbException {
        if (listRelations[0] == DbJVCompilationUnit.fClasses) {
            return dbo.getSemanticalName(DbObject.SHORT_FORM);
        }
        return super.getLinkableObjName(dbo);
    }
}
