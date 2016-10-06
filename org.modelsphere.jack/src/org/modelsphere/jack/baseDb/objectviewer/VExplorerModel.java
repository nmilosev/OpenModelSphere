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

package org.modelsphere.jack.baseDb.objectviewer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.SrType;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.util.ExceptionHandler;

class VExplorerModel extends DefaultTreeModel {
    private static class RootNode extends DefaultMutableTreeNode {
        RootNode() {
            super("ROOT", true); // NOT LOCALIZABLE
        }
    }

    private static class ObjectTreeNode extends DefaultMutableTreeNode {
        String display;

        ObjectTreeNode(Object value, String display, boolean allowsChildren) {
            super(value, allowsChildren);
            this.display = display;
        }

        public String toString() {
            return display;
        }
    }

    VExplorerModel(Object[] rootObjects) {
        super(null, true);
        if (rootObjects == null) {
            rootObjects = Db.getDbs();
        }
        ArrayList roots = new ArrayList();
        try {
            for (int i = 0; rootObjects != null && i < rootObjects.length; i++) {
                Db db = null;
                if (rootObjects[i] instanceof Db)
                    db = (Db) rootObjects[i];
                else if (rootObjects[i] instanceof DbObject)
                    db = ((DbObject) rootObjects[i]).getDb();
                else if (rootObjects[i] instanceof DbRelationN)
                    db = ((DbRelationN) rootObjects[i]).getParent().getDb();
                if (db == null)
                    continue;
                db.beginReadTrans();
                roots.add(new ObjectTreeNode(rootObjects[i], getDisplayText(rootObjects[i], null,
                        null), isExpandable(rootObjects[i])));
                db.commitTrans();
            }
        } catch (DbException e) {
            ExceptionHandler.processUncatchedException(null, e);
        }
        RootNode root = new RootNode();
        setRoot(root);
        Iterator iter = roots.iterator();
        while (iter.hasNext()) {
            root.add((ObjectTreeNode) iter.next());
        }
    }

    void loadChildren(TreeNode node) {
        if (!(node instanceof ObjectTreeNode) || !node.getAllowsChildren())
            return;
        try {
            Db db = null;
            Object obj = ((ObjectTreeNode) node).getUserObject();
            if (obj instanceof Db)
                db = (Db) obj;
            else if (obj instanceof DbObject)
                db = ((DbObject) obj).getDb();
            else if (obj instanceof DbRelationN) {
                db = ((DbRelationN) obj).getParent().getDb();
            }
            if (db != null)
                db.beginReadTrans();

            if (!(obj instanceof DbObject)
                    || (obj instanceof DbObject && ((DbObject) obj).isAlive())) {
                expand(((ObjectTreeNode) node));
            }

            if (db != null)
                db.commitTrans();
        } catch (DbException e) {
            ExceptionHandler.processUncatchedException(null, e);
        }
    }

    private void expand(ObjectTreeNode node) throws DbException {
        Object obj = node.getUserObject();
        if (obj instanceof Db) {
            expandDb(node);
        } else if (obj instanceof DbObject) {
            expandDbObject(node);
        } else if (obj instanceof DbRelationN) {
            expandDbRelationN(node);
        } else {
            expandDefault(node);
        }
    }

    private void expandDb(ObjectTreeNode parent) throws DbException {
        Db db = (Db) parent.getUserObject();
        DbRoot root = db.getRoot();
        String display = getDisplayText(root, null, null);
        parent.add(new ObjectTreeNode(root, display, true));
    }

    private void expandDbObject(ObjectTreeNode parent) throws DbException {
        // First add meta fields (they must be accessed within a transaction if
        // we want to be able to access repository info)
        DbObject dbo = (DbObject) parent.getUserObject();
        MetaClass metaclass = dbo.getMetaClass();
        MetaField[] fields = metaclass.getAllMetaFields();
        for (int i = 0; i < fields.length; i++) {
            Object value = dbo.get(fields[i]);
            String display = getDisplayText(value, fields[i], null);
            parent.add(new ObjectTreeNode(value, display, isExpandable(value)));
        }
        // Include other fields
        ObjectTreeNode[] reflexionField = getReflexionFieldNodes(dbo);
        for (int i = 0; reflexionField != null && i < reflexionField.length; i++) {
            parent.add(reflexionField[i]);
        }
    }

    private void expandDbRelationN(ObjectTreeNode parent) throws DbException {
        DbRelationN relN = (DbRelationN) parent.getUserObject();
        DbEnumeration dbEnum = relN.elements();
        while (dbEnum.hasMoreElements()) {
            DbObject value = dbEnum.nextElement();
            String display = getDisplayText(value, null, null);
            parent.add(new ObjectTreeNode(value, display, true));
        }
        dbEnum.close();
    }

    private void expandDefault(ObjectTreeNode parent) throws DbException {
        ObjectTreeNode[] reflexionField = getReflexionFieldNodes(parent.getUserObject());
        for (int i = 0; reflexionField != null && i < reflexionField.length; i++) {
            parent.add(reflexionField[i]);
        }
    }

    private ObjectTreeNode[] getReflexionFieldNodes(Object obj) {
        ObjectTreeNode[] nodes = null;
        ArrayList nodesAux = new ArrayList();
        String display = null;
        Object value = null;
        try {
            Class c = obj.getClass();
            Field[] fields = c.getDeclaredFields();
            for (int i = 0; fields != null && i < fields.length; i++) {
                int modifiers = fields[i].getModifiers();
                if (!Modifier.isPublic(modifiers)
                        || // We don't hava access to others
                        Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)
                        || fields[i].getName().startsWith("m_")) // ignore meta
                    // value or
                    // static
                    // fields or
                    // final
                    // fields //
                    // NOT
                    // LOCALIZABLE
                    continue;
                value = fields[i].get(obj);
                display = getDisplayText(value, null, fields[i]);
                nodesAux.add(new ObjectTreeNode(value, display, isExpandable(value)));
            }

        } catch (Exception e) {
            Debug.trace(e);
        }
        for (int i = 0; nodes != null && i < nodes.length; i++) {
            if (nodes == null)
                nodes = new ObjectTreeNode[nodesAux.size()];
            nodes[i] = (ObjectTreeNode) nodesAux.get(i);
        }
        return nodes;
    }

    private String getDisplayText(Object value, MetaField metafield, Field jfield)
            throws DbException {
        if (value instanceof Db)
            return ((Db) value).getDBMSName();

        String display = "";
        if (metafield != null)
            display += metafield.getJName() + ": "; // NOT LOCALIZABLE
        if (jfield != null)
            display += jfield.getName() + ": "; // NOT LOCALIZABLE

        if (value == null)
            display += "<NULL>"; // NOT LOCALIZABLE
        else
            display += value.toString();

        if (value instanceof DbObject) {
            DbObject dbo = (DbObject) value;
            String dboName = dbo.getName();
            display += " {name=" + (dboName == null ? "<NULL>" : dboName) + "}"; // NOT
            // LOCALIZABLE
        }
        return display;
    }

    private boolean isExpandable(Object value) throws DbException {
        Class c = value == null ? null : value.getClass();
        return (value != null) && !c.isPrimitive() && !Number.class.isAssignableFrom(c)
                && c != String.class && c != Boolean.class && !SrType.class.isAssignableFrom(c);
    }
}
