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

package org.modelsphere.jack.baseDb.screen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.screen.model.DbListModel;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.*;

public class DbListView extends ListView {

    public static final String k0Creation = LocaleMgr.screen.getString("{0}Creation");
    public static final String kDeletion = LocaleMgr.screen.getString("Deletion");
    public static final String k01Update = LocaleMgr.screen.getString("{0}{1}Update");
    public static final String k01Reordering = LocaleMgr.screen.getString("{0}{1}Reordering");

    private static final String kLookupDlgTitle = LocaleMgr.misc.getString("PUKLookupDialogTitle");
    private static final String kTransactionID = LocaleMgr.misc
            .getString("LinkKeyTransactionIdentifier");

    protected DbObject semObj;
    protected MetaRelationN[] listRelations;
    protected MetaClass listClass;
    protected MetaRelationship parentRel = null; // relation to 2nd parent for an intersection object.
    protected Terminology terminology = null;

    public DbListView(ScreenContext screenContext, DbObject semObj, MetaRelationN listRelation,
            MetaClass listClass, Terminology terminology, int actionMode) throws DbException {
        this(screenContext, semObj, new MetaRelationN[] { listRelation }, listClass, terminology,
                actionMode);
    }

    public DbListView(ScreenContext screenContext, DbObject semObj, MetaRelationN listRelation,
            MetaClass listClass, int actionMode) throws DbException {
        this(screenContext, semObj, new MetaRelationN[] { listRelation }, listClass, actionMode);
    }

    public DbListView(ScreenContext screenContext, DbObject semObj, MetaRelationN[] listRelations,
            MetaClass listClass, int actionMode) throws DbException {
        super(screenContext);
        this.semObj = semObj;
        this.listRelations = listRelations;
        this.listClass = listClass;

        if (listRelations.length > 1) {
            actionMode |= SORTED_LIST;
            actionMode &= ~(ADD_BTN | LINK_BTN | UNLINK_BTN | REINSERT_ACTION);
        } else if (listRelations[0] != DbObject.fComponents) {
            actionMode &= ~ADD_BTN;
        }
        if ((actionMode & SORTED_LIST) != 0)
            actionMode &= ~REINSERT_ACTION;
        this.actionMode = actionMode;

        if (listRelations.length == 1) {
            if (listRelations[0] == DbObject.fComponents)
                setTabName(listClass);
            else
                setTabName(listRelations[0].getGUIName());
        }
    }

    public DbListView(ScreenContext screenContext, DbObject semObj, MetaRelationN[] listRelations,
            MetaClass listClass, Terminology terminology, int actionMode) throws DbException {
        super(screenContext);
        this.terminology = terminology;
        this.semObj = semObj;
        this.listRelations = listRelations;
        this.listClass = listClass;

        if (listRelations.length > 1) {
            actionMode |= SORTED_LIST;
            actionMode &= ~(ADD_BTN | LINK_BTN | UNLINK_BTN | REINSERT_ACTION);
        } else if (listRelations[0] != DbObject.fComponents) {
            actionMode &= ~ADD_BTN;
        }
        if ((actionMode & SORTED_LIST) != 0)
            actionMode &= ~REINSERT_ACTION;
        this.actionMode = actionMode;

        if (listRelations.length == 1) {
            if (listRelations[0] == DbObject.fComponents)
                setTabName(listClass);
            else
                setTabName(listRelations[0].getGUIName());
        }
    }

    public final void setTabName(MetaClass metaClass, MetaField metaField) {
        if (terminology != null) {
        	String term = terminology.getTerm(metaClass, metaField, false);
            setTabName(term);
        } else {
            setTabName(metaField.getGUIName());
        }
    }

    public final void setTabName(MetaClass metaClass) {
        if (terminology != null)
            setTabName(terminology.getTerm(metaClass, true));
        else
            setTabName(metaClass.getGUIName(true));
    }

    public final MetaRelationship getParentRel() {
        return parentRel;
    }

    public final void setParentRel(MetaRelationship parentRel) {
        this.parentRel = parentRel;
    }

    protected DbListModel createListModel() throws DbException {
        return new DbListModel(this, semObj, listRelations, listClass);
    }

    /*
     * Load the model and build the panel the first time it is activated.
     */
    public final void activateTab() {
        if (getModel() == null) {
            try {
                semObj.getDb().beginTrans(Db.READ_TRANS);
                setModel(createListModel());
                semObj.getDb().commitTrans();
            } catch (DbException ex) {
                ExceptionHandler.processUncatchedException(this, ex);
            }
        }
    }

    public final DbObject getDbObject() {
        return semObj;
    }

    // BEWARE: this method returns the FocusManager selection used for action management,
    // not the internal selection used by the ListView buttons.
    // If you do not want this ListView to affect the FocusManager selection,
    // override this method to return an empty selection.
    public DbObject[] getSelection() {
        if (parentRel != null)
            return new DbObject[0];

        DbListModel model = (DbListModel) getModel();
        int[] selRows = getSelectedRows();
        DbObject[] selObjs = new DbObject[selRows.length];
        for (int i = 0; i < selRows.length; i++)
            selObjs[i] = (DbObject) model.getParentValue(selRows[i]);

        adjustControlPanel(selRows.length > 0);
        return selObjs;
    }

    private void adjustControlPanel(boolean rowIsSelected) {
        if (deleteBtn != null)
            deleteBtn.setEnabled(rowIsSelected);
        if (unlinkBtn != null)
            unlinkBtn.setEnabled(rowIsSelected);
        if (propertiesBtn != null)
            propertiesBtn.setEnabled(rowIsSelected);
        if (moveUpBtn != null)
            moveUpBtn.setEnabled(rowIsSelected);
        if (moveDownBtn != null)
            moveDownBtn.setEnabled(rowIsSelected);
    }

    // called by PropertiesFrame.commit
    public final void commit() throws DbException {
        stopEditing(); // close current cell edition to have the edited value copied to the model.
        DbListModel model = (DbListModel) getModel();
        if (model != null)
            model.commit();
    }

    public final void resetHasChanged() {
        DbListModel model = (DbListModel) getModel();
        if (model != null)
            model.resetHasChanged();
    }

    public final void refresh() throws DbException {
        stopEditing();
        DbListModel model = (DbListModel) getModel();
        if (model != null)
            model.refresh();
    }

    public void addAction() {
        try {
            Db db = semObj.getDb();
            db.beginTrans(Db.WRITE_TRANS, MessageFormat.format(k0Creation, new Object[] { listClass
                    .getGUIName() }));
            commit();
            createComponent();
            db.commitTrans();
        } catch (DbException ex) {
            ExceptionHandler.processUncatchedException(this, ex);
        }
    }

    protected void createComponent() throws DbException {
        semObj.createComponent(listClass);
    }

    public void deleteAction() {
        DbListModel model = (DbListModel) getModel();
        try {
            int[] selRows = getSelectedRows();
            Db db = semObj.getDb();
            db.beginTrans(Db.WRITE_TRANS, kDeletion);
            for (int i = 0; i < selRows.length; i++) {
                DbObject dbo = (DbObject) model.getParentValue(selRows[i]);
                if (dbo.isDeletable())
                    dbo.remove();
            }
            db.commitTrans();
        } catch (DbException ex) {
            ExceptionHandler.processUncatchedException(this, ex);
        }
    }

    public void propertiesAction() {
        DbListModel model = (DbListModel) getModel();
        try {
            int[] selRows = getSelectedRows();
            Db db = semObj.getDb();
            db.beginTrans(Db.READ_TRANS);
            for (int i = 0; i < selRows.length; i++) {
                DbObject dbo = (DbObject) model.getParentValue(selRows[i]);
                if (parentRel != null)
                    dbo = (DbObject) dbo.get(parentRel);
                screenContext.showPropertyFrame(dbo);
            }
            db.commitTrans();
        } catch (DbException ex) {
            ExceptionHandler.processUncatchedException(this, ex);
        }
    }

    public void linkAction() {
        DbObject[] selObjs = showLinkDialog();
        if (selObjs == null || selObjs.length == 0)
            return;
        try {
            Db db = semObj.getDb();
            db.beginTrans(Db.WRITE_TRANS, MessageFormat.format(k01Update, new Object[] {
                    semObj.getMetaClass().getGUIName(), listRelations[0].getGUIName() }));
            MetaRelationship oppositeRel = listRelations[0].getOppositeRel(null);
            for (int i = 0; i < selObjs.length; i++) {
                DbObject selObj = selObjs[i];
                if (parentRel != null)
                    createLinkComponent(selObj);
                else
                    selObj.set(oppositeRel, semObj);
            }
            db.commitTrans();
        } catch (DbException ex) {
            ExceptionHandler.processUncatchedException(this, ex);
        }
    }

    protected void createLinkComponent(DbObject parent) throws DbException {
    }

    public void unlinkAction() {
        DbListModel model = (DbListModel) getModel();
        try {
            int[] selRows = getSelectedRows();
            Db db = semObj.getDb();
            db.beginTrans(Db.WRITE_TRANS, MessageFormat.format(kTransactionID, new Object[] {
                    semObj.getMetaClass().getGUIName(), listRelations[0].getGUIName() }));
            MetaRelationship oppositeRel = listRelations[0].getOppositeRel(null);
            for (int i = 0; i < selRows.length; i++) {
                DbObject child = (DbObject) model.getParentValue(selRows[i]);
                if (parentRel != null)
                    child.remove();
                else {
                    if (oppositeRel instanceof MetaRelationN)
                        semObj.set(listRelations[0], child, Db.REMOVE_FROM_RELN);
                    else
                        child.set(oppositeRel, null);
                }
            }
            db.commitTrans();
        } catch (DbException ex) {
            ExceptionHandler.processUncatchedException(this, ex);
        }
    }

    public void moveUpAction() {
        DbListModel model = (DbListModel) getModel();
        int[] selRows = getSelectedRows();
        for (int i = 0; i < selRows.length; i++) {
            int pos = selRows[i];
            if (pos > 0) {
                reinsertAction(pos, 1, pos - 1);
                table.setRowSelectionInterval(pos - 1, pos - 1);
                prefixTable.setRowSelectionInterval(pos - 1, pos - 1);
            }
        } //end for
    } //end moveUpAction()

    public void moveDownAction() {
        DbListModel model = (DbListModel) getModel();
        int maxRow = model.getRowCount() - 1;
        int[] selRows = getSelectedRows();
        for (int i = 0; i < selRows.length; i++) {
            int pos = selRows[i];
            if (pos < maxRow) {
                reinsertAction(pos, 1, pos + 2);
                table.setRowSelectionInterval(pos + 1, pos + 1);
                prefixTable.setRowSelectionInterval(pos + 1, pos + 1);
            }
        } //end for
    } //end moveDownAction()

    public final void reinsertAction(int firstDraggedRow, int nbDraggedRows, int dropPos) {
        DbListModel model = (DbListModel) getModel();
        try {
            Db db = semObj.getDb();
            db.beginTrans(Db.WRITE_TRANS, MessageFormat.format(k01Reordering, new Object[] {
                    semObj.getMetaClass().getGUIName(), listRelations[0].getGUIName() }));
            DbRelationN dbRelN = (DbRelationN) semObj.get(listRelations[0]);
            int newIndex = dropPos;
            int nbRows = model.getRowCount();

            if ((dropPos != 0) && (dropPos <= nbRows)) {
                newIndex = 1 + dbRelN.indexOf((DbObject) model.getParentValue(dropPos - 1));
            }

            for (; nbDraggedRows != 0; firstDraggedRow++, newIndex++, nbDraggedRows--) {
                int oldIndex = dbRelN.indexOf((DbObject) model.getParentValue(firstDraggedRow));
                if (newIndex > oldIndex)
                    newIndex--;
                semObj.reinsert(listRelations[0], oldIndex, newIndex);
            }
            db.commitTrans();
        } catch (DbException ex) {
            ExceptionHandler.processUncatchedException(this, ex);
        }
    }

    protected DbObject[] showLinkDialog() {
        SrVector linkObjs = getLinkableObjects();
        CollationComparator comparator = new CollationComparator();
        linkObjs.sort(comparator);
        String title = null;
        if (!(parentRel instanceof MetaChoice)) {
            MetaClass linkClass = (parentRel != null ? parentRel.getOppositeRel(null)
                    .getMetaClass() : listClass);
            title = getLinkDialogTitle(linkClass);
        }
        int[] indices = LookupDialog.selectMany(this, title, null, linkObjs.toArray(), -1,
                comparator);
        DbObject[] selObjs = new DbObject[indices.length];
        for (int i = 0; i < selObjs.length; i++)
            selObjs[i] = (DbObject) ((DefaultComparableElement) linkObjs.elementAt(indices[i])).object;
        return selObjs;
    }

    protected SrVector getLinkableObjects() {
        SrVector linkObjs = new SrVector(100);
        try {
            semObj.getDb().beginTrans(Db.READ_TRANS);
            // Get in <listObjs> all the objects already in the ListVIew
            DbRelationN dbRelN = (DbRelationN) semObj.get(listRelations[0]);
            int nb = dbRelN.size();
            SrVector listObjs = new SrVector(nb);
            for (int i = 0; i < nb; i++) {
                DbObject dbo = dbRelN.elementAt(i);
                MetaClass dboMc = dbo.getMetaClass();
                if (listClass.isAssignableFrom(dboMc)) {
                    if (parentRel != null) {
                        dbo = (DbObject) dbo.get(parentRel);
                    }
                    listObjs.addElement(dbo);
                }
            }
            // Get in <linkSet> all the linkable objects
            Collection linkSet = getLinkableSet();
            if (linkSet == null) {
                linkSet = new ArrayList();
                DbEnumeration dbEnum = getLinkableEnum();
                if (dbEnum != null) {
                    while (dbEnum.hasMoreElements())
                        linkSet.add(dbEnum.nextElement());
                    dbEnum.close();
                }
            }
            // Now <linkObjs> = <linkSet> - <listObjs>, i.e eliminate the objects already linked
            MetaRelationship oppositeRel = listRelations[0].getOppositeRel(null);
            Iterator iter = linkSet.iterator();
            while (iter.hasNext()) {
                DbObject dbo = (DbObject) iter.next();
                if (dbo == semObj)
                    continue;
                if (parentRel != null || oppositeRel instanceof MetaRelationN) {
                    if (listObjs.contains(dbo)) // fast: SrVector does not use the method equals
                        continue;
                } else {
                    if (dbo.get(oppositeRel) != null)
                        continue;
                }
                String name = getLinkableObjName(dbo);
                if (name != null)
                    linkObjs.addElement(new DefaultComparableElement(dbo, name));
            }
            semObj.getDb().commitTrans();
        } catch (DbException ex) {
            ExceptionHandler.processUncatchedException(this, ex);
        }
        return linkObjs;
    }

    // Overridden
    protected Collection getLinkableSet() throws DbException {
        return null;
    }

    // Overridden
    protected DbEnumeration getLinkableEnum() throws DbException {
        if (parentRel instanceof MetaChoice)
            throw new RuntimeException("getLinkableEnum() must be overridden for a choice"); // NOT LOCALIZABLE RuntimeException
        MetaClass linkClass = (parentRel != null ? parentRel.getOppositeRel(null).getMetaClass()
                : listClass);
        return semObj.getProject().componentTree(linkClass);
    }

    // Last chance filter: return null if object is not linkable
    // Overridden
    protected String getLinkableObjName(DbObject dbo) throws DbException {
        return ApplicationContext.getSemanticalModel().getDisplayText(dbo, DbListView.class);
    }

    // If you want to change the dialog list dialog title i.e. in case we have DbObject
    // Overridden
    protected String getLinkDialogTitle(MetaClass linkClass) {
        String sTitle = kLookupDlgTitle;
        String sTerm = "";
        try {
            semObj.getDb().beginReadTrans();
            DbObject obj = semObj.getComposite().getComposite();
            semObj.getDb().commitTrans();
            TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
            obj.getDb().beginReadTrans();
            Terminology terminology = terminologyUtil.findModelTerminology(obj);
            obj.getDb().commitTrans();
            if (terminology != null)
                sTerm = terminology.getTerm(linkClass, true);
            else
                sTerm = linkClass.getGUIName(true, false);
        } catch (DbException dbe) {
            sTerm = linkClass.getGUIName(true, false);
        }

        return MessageFormat.format(kLookupDlgTitle, new Object[] { sTerm.toLowerCase() });
    }

}
