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

package org.modelsphere.jack.baseDb.screen.model;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.Icon;
import javax.swing.tree.DefaultTreeModel;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.CollationComparator;
import org.modelsphere.jack.util.SrVector;

public class DbTreeModel extends DefaultTreeModel {

    private DbObject[] roots;
    private MetaClass[] metaClasses;
    private boolean[] compositeAncestors;
    protected DbTreeModelListener listener;
    protected DbLookupNode nullNode = null;
    protected TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

    public DbTreeModel(DbObject[] roots, MetaClass[] metaClasses, DbTreeModelListener listener,
            String nullStr) throws DbException {
        super(new DbLookupNode(null, "", null, true, false));
        this.roots = roots;
        this.metaClasses = metaClasses;
        compositeAncestors = MetaClass.markCompositePaths(metaClasses);
        this.listener = listener;

        DbLookupNode rootNode = (DbLookupNode) getRoot();
        rootNode.loaded = true;
        int i;
        ArrayList rootNodes = new ArrayList();
        for (i = 0; i < roots.length; i++) {
            roots[i].getDb().beginTrans(Db.READ_TRANS);
            DbLookupNode node = createNode(roots[i]);
            if (node != null)
                rootNodes.add(node);
        }
        Object[] temp = rootNodes.toArray();
        Arrays.sort(temp);
        for (int j = 0; j < temp.length; j++) {
            rootNode.add((DbLookupNode) temp[j]);
        }

        for (i = 0; i < roots.length; i++)
            roots[i].getDb().commitTrans();

        if (nullStr != null) {
            nullNode = new DbLookupNode(null, nullStr, null, false, true);
            rootNode.add(nullNode);
        }
    }

    public void loadChildren(DbLookupNode parentNode) throws DbException {
        if (parentNode.loaded || !parentNode.ancestor)
            return;
        parentNode.loaded = true;
        DbObject parent = (DbObject) parentNode.getUserObject();
        parent.getDb().beginTrans(Db.READ_TRANS);
        SrVector nodes = new SrVector(10);
        DbEnumeration dbEnum = parent.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            DbLookupNode node = createNode(dbEnum.nextElement());
            if (node != null)
                nodes.add(node);
        }
        dbEnum.close();

        parent.getDb().commitTrans();

        nodes.sort(new CollationComparator());
        int nb = nodes.size();
        for (int i = 0; i < nb; i++)
            parentNode.add((DbLookupNode) nodes.get(i));
    }

    public final DbLookupNode findNode(DbObject dbo) throws DbException {
        if (dbo == null)
            return nullNode;
        DbLookupNode nodeFound = null;
        SrVector path = new SrVector(10);
        Db db = dbo.getDb();
        db.beginTrans(Db.READ_TRANS);
        buildPath: while (dbo != null) {
            path.add(dbo);
            for (int i = 0; i < roots.length; i++)
                if (dbo == roots[i])
                    break buildPath;
            dbo = dbo.getComposite();
        }

        DbLookupNode parentNode = (DbLookupNode) getRoot();
        for (int i = path.size(); --i >= 0;) {
            dbo = (DbObject) path.get(i);
            loadChildren(parentNode);
            nodeFound = null;
            for (int j = getChildCount(parentNode); --j >= 0;) {
                DbLookupNode node = (DbLookupNode) getChild(parentNode, j);
                if (node.getUserObject() == dbo) {
                    nodeFound = node;
                    break;
                }
            }
            if (nodeFound == null)
                break;
            parentNode = nodeFound;
        }
        db.commitTrans();
        return nodeFound;
    }

    protected DbLookupNode createNode(DbObject dbo) throws DbException {
        boolean ancestor = isAncestor(dbo);
        boolean selectable = isInstance(dbo);
        if (!(ancestor || selectable))
            return null;
        String name;
        Icon icon = null;
        if (listener == null) {
            name = ApplicationContext.getSemanticalModel().getDisplayText(dbo, DbObject.SHORT_FORM,
                    null, DbTreeModel.class);
            icon = dbo.getSemanticalIcon(DbObject.SHORT_FORM);
        } else {
            if (!listener.filterNode(dbo))
                return null;
            name = listener.getDisplayText(dbo, this);
            if (terminologyUtil.isDataModel(dbo)) {
                if (terminologyUtil.getModelLogicalMode(dbo) == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                    icon = terminologyUtil.getConceptualModelIcon();
            } else if (terminologyUtil.isObjectEntityOrAssociation(dbo)) {
                if (terminologyUtil.isObjectAssociation(dbo))
                    icon = terminologyUtil.getAssociationIcon();
            } else if (terminologyUtil.isObjectArc(dbo)) {
                DbObject dataModel = terminologyUtil.isCompositeDataModel(dbo);
                if (dataModel != null)
                    if (terminologyUtil.getModelLogicalMode(dataModel) == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                        icon = terminologyUtil.getArcIcon();
            } else if (terminologyUtil.isObjectRole(dbo)) {
                if (!terminologyUtil.isObjectArcEndRole(dbo)) {
                    DbObject dataModel = terminologyUtil.isCompositeDataModel(dbo);
                    if (dataModel != null)
                        if (terminologyUtil.getModelLogicalMode(dataModel) == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                            return null;
                }
            } else if (terminologyUtil.isObjectUseCaseOrBEModel(dbo)) {
                if (terminologyUtil.isObjectUseCase(dbo))
                    icon = terminologyUtil.getUseCaseIcon(dbo);
                else
                    icon = terminologyUtil.findModelTerminology(dbo).getIcon(dbo.getMetaClass());
            } else
                icon = listener.getIcon(dbo);

            if (icon == null)
                icon = listener.getIcon(dbo);

            if (selectable)
                selectable = listener.isSelectable(dbo);
        }
        return new DbLookupNode(dbo, name, icon, ancestor, selectable);
    }

    // Return true if <dbo> may be an ancestor of the wanted classes.
    protected boolean isAncestor(DbObject dbo) {
        return compositeAncestors[dbo.getMetaClass().getSeqNo()];
    }

    // Return true if <dbo> is an instance of the wanted classes.
    protected boolean isInstance(DbObject dbo) {
        for (int i = 0; i < metaClasses.length; i++) {
            if (metaClasses[i].getJClass().isInstance(dbo))
                return true;
        }
        return false;
    }

}
