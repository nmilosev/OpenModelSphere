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

package org.modelsphere.sms;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.awt.tree.CheckTreeNode;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.srtool.integrate.IntegrateNode;
import org.modelsphere.jack.srtool.integrate.IntegrateNode.IntegrateProperty;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAbstractMethod;
import org.modelsphere.sms.or.db.DbORCheck;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.preference.DirectoryOptionGroup;

public abstract class SMSSynchroModel extends SMSIntegrateModel {

    private static final String kSaveSQL = LocaleMgr.screen.getString("SaveSQL");
    private static final String kDefaultFileName = LocaleMgr.misc.getString("SynchroFileName");

    private static String lastUsedDirectory = null;

    private static final Class[] defaultClassOrder = new Class[] { DbORDatabase.class,
            DbORDomain.class, DbORAbsTable.class, DbORColumn.class, DbORIndex.class,
            DbORPrimaryUnique.class, DbORForeign.class, DbORCheck.class, DbORAbstractMethod.class };

    protected Class[] classOrder = defaultClassOrder;
    protected ArrayList syncObjs;
    protected File sqlFile;
    protected StringBuffer reportBuffer;

    protected SMSSynchroModel(DbObject physModel, DbObject refModel, CheckTreeNode fieldTree,
            boolean ignoreCase, boolean syncUser) throws DbException {
        super(refModel, physModel, fieldTree, true, true, ignoreCase, syncUser, true);
    }

    // No transaction started
    protected final void postIntegrate(StringBuffer reportBuffer) throws DbException {
        this.reportBuffer = reportBuffer;
        syncObjs = new ArrayList();
        fillSyncObjs((IntegrateNode) getRoot());
        if (syncObjs.size() == 0)
            return;
        Collections.sort(syncObjs);

        String ddlDirectoryName = lastUsedDirectory;
        if (ddlDirectoryName == null)
            ddlDirectoryName = DirectoryOptionGroup.getDDLGenerationDirectory();
        if (ddlDirectoryName == null || ddlDirectoryName.length() == 0)
            ddlDirectoryName = DirectoryOptionGroup.getDefaultWorkingDirectory();
        ddlDirectoryName += System.getProperty("file.separator") + kDefaultFileName;
        ExtensionFileFilter[] filters = null;
        AwtUtil.FileAndFilter selection = AwtUtil.showSaveAsDialog(frame, kSaveSQL, ExtensionFileFilter.sqlFileFilter,
                filters, new File(ddlDirectoryName));
        sqlFile = (selection == null) ? null : selection.getFile();
            
        if (sqlFile == null)
            return;

        lastUsedDirectory = sqlFile.getParent();

        // Open a write transaction to allow temporary modifs to the models; the
        // transaction is aborted at the end.
        leftModel.getDb().beginTrans(Db.WRITE_TRANS);
        rightModel.getDb().beginTrans(Db.WRITE_TRANS);

        /*
         * Modify (temporarily) the ref model with all the differences marked with the action
         * ACT_NONE, (as if the action was ACT_MODIFY_LEFT) so that the differences between the ref
         * model and the physical model are exactly the ones to be generated. This way, the ALTER
         * templates can take all their data from the ref model.
         */
        integrateAux(true);

        genSyncObjs();

        leftModel.getDb().abortTrans();
        rightModel.getDb().abortTrans();
    }

    protected int getRootDefaultAction() throws DbException {
        return ACT_REPLACE_RIGHT;
    }

    protected abstract void genSyncObjs() throws DbException;

    private void fillSyncObjs(IntegrateNode node) {
        int syncAction = 0;
        ArrayList metafields = new ArrayList();
        ArrayList customfields = new ArrayList();
        if (node.getAction() == ACT_DELETE_RIGHT)
            syncAction = SynchroObject.ACT_DELETE;
        else if (node.getAction() == ACT_ADD_IN_RIGHT)
            syncAction = SynchroObject.ACT_ADD;
        else if (node.getProperties() != null) {
            IntegrateProperty[] props = node.getProperties();
            for (int i = 0; i < props.length; i++) {
                if (props[i].getAction() == ACT_MODIFY_RIGHT) {
                    syncAction |= getSyncAction(props[i].getProperty());
                    if (props[i].getProperty() instanceof MetaField)
                        metafields.add(props[i].getProperty());
                    if (props[i].getProperty() instanceof String)
                        customfields.add(props[i].getProperty());
                }
            }
        }
        if (syncAction != 0) {
            int order = getSyncOrder(node.getRightDbo(), node.getLeftDbo(), syncAction);
            MetaField[] modifiedFields = new MetaField[metafields.size()];
            for (int i = 0; i < modifiedFields.length; i++)
                modifiedFields[i] = (MetaField) metafields.get(i);
            String[] modifiedCustomFields = new String[customfields.size()];
            for (int i = 0; i < modifiedCustomFields.length; i++)
                modifiedCustomFields[i] = (String) customfields.get(i);
            syncObjs.add(new SynchroObject(node.getRightDbo(), node.getLeftDbo(), syncAction,
                    order, modifiedFields.length == 0 ? null : modifiedFields,
                    modifiedCustomFields.length == 0 ? null : modifiedCustomFields));
        }

        int nb = node.getChildCount();
        for (int i = 0; i < nb; i++)
            fillSyncObjs((IntegrateNode) node.getChildAt(i));
    }

    protected int getSyncAction(Object property) {
        return (property == DbSemanticalObject.fPhysicalName ? SynchroObject.ACT_RENAME
                : SynchroObject.ACT_MODIFY);
    }

    public int getSyncOrder(DbObject physDbo, DbObject refDbo, int action) {
        DbObject dbo = (physDbo != null ? physDbo : refDbo);
        int i = 0;
        while (i < classOrder.length && !classOrder[i].isInstance(dbo))
            i++;
        i++; // starting from 1
        if (action == SynchroObject.ACT_DELETE)
            i = -i; // the DROP's first, in reverse order.
        return i;
    }

    public static class SynchroObject implements Comparable {

        public static final int ACT_ADD = 0x0001; // this flag is always alone
        public static final int ACT_DELETE = 0x0002; // this flag is always
        // alone
        public static final int ACT_MODIFY = 0x0004; // flags from here are
        // combinable
        public static final int ACT_RENAME = 0x0008;

        public DbObject physDbo;
        public DbObject refDbo;
        public int action;
        public int order;
        public MetaField[] modifiedMetaFields = null;
        public String[] customFields = null;
        public boolean reportOnly = false; // flag this SynchroObject as for

        // reporting only (no sql
        // generation)

        public SynchroObject(DbObject physDbo, DbObject refDbo, int action, int order) {
            this.physDbo = physDbo;
            this.refDbo = refDbo;
            this.action = action;
            this.order = order;
        }

        public SynchroObject(DbObject physDbo, DbObject refDbo, int action, int order,
                MetaField[] modifiedMetaFields) {
            this.physDbo = physDbo;
            this.refDbo = refDbo;
            this.action = action;
            this.order = order;
            this.modifiedMetaFields = modifiedMetaFields;
        }

        public SynchroObject(DbObject physDbo, DbObject refDbo, int action, int order,
                MetaField[] modifiedMetaFields, String[] customFields) {
            this.physDbo = physDbo;
            this.refDbo = refDbo;
            this.action = action;
            this.order = order;
            this.modifiedMetaFields = modifiedMetaFields;
            this.customFields = customFields;
        }

        public final int compareTo(Object obj) {
            return order - ((SynchroObject) obj).order;
        }

        public final boolean equals(Object obj) {
            if (obj == this)
                return true;
            if (obj == null || ((obj != null) && !(obj instanceof SynchroObject)))
                return false;
            SynchroObject syncobj = (SynchroObject) obj;
            return (((syncobj.refDbo == refDbo && refDbo != null) || (syncobj.physDbo == physDbo && physDbo != null)) && syncobj.action == action);
        }

        // For debug purpose only
        public String toString() {
            if (!Debug.isDebug())
                return super.toString();
            String s = "";
            s += "actions=[ "; // NOT LOCALIZABLE, debug purpose
            if ((action & ACT_ADD) != 0)
                s += " add "; // NOT LOCALIZABLE, debug purpose
            if ((action & ACT_DELETE) != 0)
                s += " delete "; // NOT LOCALIZABLE, debug purpose
            if ((action & ACT_MODIFY) != 0)
                s += " modify "; // NOT LOCALIZABLE, debug purpose
            if ((action & ACT_RENAME) != 0)
                s += " rename "; // NOT LOCALIZABLE, debug purpose
            s += " ]\n"; // NOT LOCALIZABLE, escape code

            s += "physDbo=" + (physDbo == null ? "null" : physDbo.toString()); // NOT
            // LOCALIZABLE,
            // debug
            // purpose
            s += "\n"; // NOT LOCALIZABLE, escape code
            s += "refDbo=" + (refDbo == null ? "null" : refDbo.toString()); // NOT
            // LOCALIZABLE,
            // debug
            // purpose
            s += "\n\n"; // NOT LOCALIZABLE, escape code
            return s;
        }
    } // End of class SynchroObject
}
