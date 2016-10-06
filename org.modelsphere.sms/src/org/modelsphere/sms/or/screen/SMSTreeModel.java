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

package org.modelsphere.sms.or.screen;

import javax.swing.Icon;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.screen.model.DbLookupNode;
import org.modelsphere.jack.baseDb.screen.model.DbTreeModel;
import org.modelsphere.jack.baseDb.screen.model.DbTreeModelListener;
import org.modelsphere.jack.baseDb.util.TerminologyManager;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.CollationComparator;
import org.modelsphere.jack.util.SrVector;
import org.modelsphere.sms.SMSExplorer;
import org.modelsphere.sms.or.db.DbORDataModel;

/**
 * @author nicolask
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences -
 *         Java - Code Style - Code Templates
 */
public class SMSTreeModel extends DbTreeModel {

    private int leftnodemode = 0;

    public SMSTreeModel(DbObject[] roots, MetaClass[] metaClasses, DbTreeModelListener listener,
            String nullStr, int nLeftMode) throws DbException {
        super(roots, metaClasses, listener, nullStr);

        leftnodemode = nLeftMode;
    }

    protected DbLookupNode createNode(DbObject dbo) throws DbException {
        boolean ancestor = isAncestor(dbo);
        boolean selectable = isInstance(dbo);
        if (!(ancestor || selectable))
            return null;
        String name = null;
        Icon icon = null;
        if (listener == null) {
            name = ApplicationContext.getSemanticalModel().getDisplayText(dbo, DbObject.SHORT_FORM,
                    null, SMSTreeModel.class);
            if (dbo instanceof DbORDataModel) {
                DbORDataModel dataModel = (DbORDataModel) dbo;
                if (dataModel.getOperationalMode() == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                    icon = SMSExplorer.erModelIcon;
                else
                    icon = dbo.getSemanticalIcon(DbObject.SHORT_FORM);
            } else
                icon = SMSExplorer.erModelIcon;
        } else {
            if (!listener.filterNode(dbo))
                return null;
            name = listener.getDisplayText(dbo, this);
            if (dbo instanceof DbORDataModel) {
                DbORDataModel dataModel = (DbORDataModel) dbo;
                if (dataModel.getOperationalMode() == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                    icon = SMSExplorer.erModelIcon;
                else
                    icon = listener.getIcon(dbo);
            } else
                icon = listener.getIcon(dbo);

            if (selectable)
                selectable = listener.isSelectable(dbo);
        }
        return new DbLookupNode(dbo, name, icon, ancestor, selectable);
    }

    public final void loadChildren(DbLookupNode parentNode) throws DbException {
        if (parentNode.loaded || !parentNode.ancestor)
            return;

        parentNode.loaded = true;
        TerminologyManager terminologManager = TerminologyManager.getInstance();
        DbObject parent = (DbObject) parentNode.getUserObject();
        parent.getDb().beginTrans(Db.READ_TRANS);
        SrVector nodes = new SrVector(10);
        DbEnumeration dbEnum = parent.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            DbObject dbObject = dbEnum.nextElement();
            if (dbObject instanceof DbORDataModel) {
                DbORDataModel dataModel = (DbORDataModel) dbObject;
                dataModel.getDb().beginReadTrans();
                if (dataModel.getOperationalMode() == leftnodemode) {
                    DbLookupNode node = createNode(dbObject);
                    if (node != null)
                        nodes.add(node);
                }
                dataModel.getDb().commitTrans();
            } else {
                DbLookupNode node = createNode(dbObject);
                if (node != null)
                    nodes.add(node);
            }
        }
        dbEnum.close();
        parent.getDb().commitTrans();
        nodes.sort(new CollationComparator());
        int nb = nodes.size();
        for (int i = 0; i < nb; i++)
            parentNode.add((DbLookupNode) nodes.get(i));

    }
}
