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

package org.modelsphere.jack.srtool.explorer;

import java.util.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.*;
import javax.swing.tree.*;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.event.DbListener;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.SemanticalModel;
import org.modelsphere.jack.srtool.ToolTipsServices;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.srtool.preference.DisplayToolTipsOptionGroup;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.jack.util.SrVector;

public class Explorer extends DefaultTreeModel implements DbRefreshListener {
    public static final String kUpdate0 = LocaleMgr.screen.getString("Update0");
    private static final Icon kLocalIcon = GraphicUtil.loadIcon(ApplicationContext.class,
            "international/resources/localnode.gif");
    private static final Icon kRepositoryIcon = GraphicUtil.loadIcon(ApplicationContext.class,
            "international/resources/repositorynode.gif");

    public static final String ROOT = "ROOT"; // NOT LOCALIZABLE, property key
    public static final String DB_RAM = "RAM"; // NOT LOCALIZABLE, property key

    private ArrayList tooltipsFields = new ArrayList();

    class PreferencesListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            try {
                Explorer.this.refresh();
            } catch (Exception e) {
                ExceptionHandler.processUncatchedException(
                        ApplicationContext.getDefaultMainFrame(), e);
            }
        }
    }

    class DbsListener implements DbListener {
        public void dbCreated(Db db) {
            try {
                if (db instanceof DbRAM) {
                    Explorer.this.refresh();
                } else {
                    Explorer.this.reloadAll();
                }
            } catch (Exception e) {
                ExceptionHandler.processUncatchedException(
                        ApplicationContext.getDefaultMainFrame(), e);
            }
        }

        public void dbTerminated(Db db) {
            try {
                Explorer.this.refresh();
            } catch (Exception e) {
                ExceptionHandler.processUncatchedException(
                        ApplicationContext.getDefaultMainFrame(), e);
            }
        }
    }

    private DbsListener dbslistener = new DbsListener();

    public Explorer() {
        super(new DynamicNode(ROOT));
        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
        if (preferences != null) {
            preferences.addPrefixPropertyChangeListener(DisplayToolTipsOptionGroup.class,
                    DisplayToolTipsOptionGroup.TOOLTIPS_FIELD_VISIBILITY_PREFIX,
                    new PreferencesListener());
            Db.addDbListener(dbslistener);
            tooltipsFields.addAll(Arrays
                    .asList(DisplayToolTipsOptionGroup.getAvailableMetaFields()));
        }
    }

    public final void showContent() {
        setRoot(createRootNode());
        installDbListeners(true);
    }

    public final void hideContent() {
        setRoot(new DynamicNode(ROOT));
        installDbListeners(false);
    }

    // Overridden
    public void installDbListeners(boolean install) {
        MetaField[] tipsFields = DisplayToolTipsOptionGroup.getAvailableMetaFields();
        MetaField[] metaFields = new MetaField[] { DbObject.fComponents, DbSemanticalObject.fName };
        if (install) {
            MetaField.addDbRefreshListener(this, null, metaFields);
            MetaField.addDbRefreshListener(this, null, tipsFields);
        } else {
            MetaField.removeDbRefreshListener(this, metaFields);
            MetaField.removeDbRefreshListener(this, tipsFields);
        }

    }

    /**
     * If CS application, create a root node with 2 subnodes: RAM and repository; the RAM subnode
     * contains the list of RAM projects as subnodes. If RAM application, create a root node with
     * the list of projects as subnodes.
     */
    private DynamicNode createRootNode() {
        DynamicNode rootNode = null;
        DynamicNode RAMNode = null;
        try {
            RAMNode = new DynamicNode(DB_RAM);
            RAMNode.setDisplayText(DbRAM.DISPLAY_NAME);
            RAMNode.setIcon(kLocalIcon);
            loadChildren(RAMNode);
            Db[] dbs = Db.getDbs();
            for (int i = 0; i < dbs.length; i++) {
                if (dbs[i] instanceof DbRAM)
                    continue;
                if (rootNode == null) {
                    rootNode = new DynamicNode(ROOT);
                    rootNode.setHasLoaded();
                    rootNode.add(RAMNode);
                }
                DynamicNode dbNode = new DynamicNode(dbs[i]);
                dbNode.setDisplayText(dbs[i].getDBMSName());
                dbNode.setIcon(kRepositoryIcon);
                rootNode.add(dbNode);
                loadChildren(dbNode);
            }
        } catch (DbException ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
        return (rootNode != null ? rootNode : RAMNode);
    }

    protected final DynamicNode createNode(DbObject dbParent, DbObject dbo) throws DbException {
        return (dbParent == getDbParent(dbo, Db.NEW_VALUE) ? createPrimaryNode(dbParent, dbo)
                : createSecondaryNode(dbParent, dbo));
    }

    // Overridden
    protected DynamicNode createPrimaryNode(DbObject dbParent, DbObject dbo) throws DbException {
        DynamicNode node = new DynamicNode(dbo, getSequence(dbParent, dbo));
        String displayText = getDisplayText(dbParent, dbo);
        String editText = getEditText(dbParent, dbo);
        String tooltips = getToolTipsText(dbParent, dbo);
        node.setDisplayText(displayText, editText);
        node.setToolTips(tooltips);
        node.setIcon(getIcon(dbo));
        node.setGroupParams(getGroupParams(dbParent, dbo));
        node.setIsLeaf(isLeaf(dbParent, dbo));
        node.setDefaultAction(getDefaultAction(dbo));
        return node;
    }

    // Overridden
    protected DynamicNode createSecondaryNode(DbObject dbParent, DbObject dbo) throws DbException {
        DynamicNode primNode = getDynamicNode(dbo, true);
        DynamicNode node = new DynamicNode(primNode, primNode.insertIndex);
        node.setGroupParams(getGroupParams(dbParent, dbo));
        node.setDefaultAction(getDefaultAction(dbo));
        return node;
    }

    protected String getDisplayText(DbObject dbParent, DbObject dbo) throws DbException {
        return ApplicationContext.getSemanticalModel().getDisplayText(dbo, Explorer.class);
    }

    protected String getToolTipsText(DbObject dbParent, DbObject dbo) throws DbException {
        return ToolTipsServices.getToolTips(dbo, Explorer.class);
    }

    protected String getEditText(DbObject dbParent, DbObject dbo) throws DbException {
        MetaField editableMetafield = getEditableMetaField(dbo);
        String editText = null;
        if (editableMetafield == null)
            editText = dbo.getName();
        else
            editText = (String) dbo.get(editableMetafield);
        return editText == null ? "" : editText;
    }

    protected int getSequence(DbObject dbParent, DbObject dbo) throws DbException {
        return dbParent.getComponents().indexOf(dbo);
    }

    // Overridden
    protected GroupParams getGroupParams(DbObject dbParent, DbObject dbo) throws DbException {
        return GroupParams.defaultGroupParams;
    }

    /*
     * Specifies an action to activate on double click. Beware that it must not conflict with the
     * default double click behavior of JTree (expand, collapse). Recommanded to avoid returning an
     * action for a 'not leaf' object.
     */
    protected AbstractApplicationAction getDefaultAction(DbObject dbo) throws DbException {
        return null;
    }

    // Overridden
    protected boolean isLeaf(DbObject dbParent, DbObject dbo) throws DbException {
        return false;
    }

    // Overridden
    protected boolean isContainerRoot(Object object) {
        return false;
    }

    protected final void loadChildren(DynamicNode node) throws DbException {
        if (node.hasLoaded() || node.isLeaf())
            return;
        node.setHasLoaded();
        Object userObject = node.getUserObject();
        if (userObject == ROOT)
            return;
        SrVector children = new SrVector(10);
        boolean isSorted = true;
        DbObject dbParent = null;
        if (userObject == DB_RAM) {
            Db[] dbs = Db.getDbs();
            for (int i = 0; i < dbs.length; i++) {
                if (dbs[i] instanceof DbRAM)
                    insertProjects(children, dbs[i]);
            }
        } else if (userObject instanceof Db) {
            insertProjects(children, (Db) userObject);
        } else {
            dbParent = (DbObject) userObject;
            dbParent.getDb().beginTrans(Db.READ_TRANS);
            insertComponents(children, dbParent);
            isSorted = childrenAreSorted(dbParent);
            dbParent.getDb().commitTrans();
        }

        if (isSorted) {
            children.sort(getComparator(dbParent));
        }
        
        ArrayList groupNodeList = new ArrayList();
        DynamicNode groupNode = null;
        Enumeration enumeration = children.elements();
        while (enumeration.hasMoreElements()) {
            DynamicNode childNode = (DynamicNode) enumeration.nextElement();
            GroupParams group = childNode.getGroupParams();
            if (group.name == null) {
                node.add(childNode);
            } 
            else {           	
                if (groupNode == null) {
                    groupNode = createGroupNode(group);
                    node.add(groupNode);
                    groupNodeList.add(groupNode);
                }
                else if (!groupNode.toString().equals(group.name)){
                	boolean groupFound = false;
                	for (int i = 0; i < groupNodeList.size(); i++){
                		groupNode = (DynamicNode)groupNodeList.get(i);
                		if (groupNode.toString().equals(group.name)){
                			groupFound = true;
                			break;
                		}
                	}
                	if (!groupFound){
                        groupNode = createGroupNode(group);
                        node.add(groupNode);
                        groupNodeList.add(groupNode);
                	}
                }
                groupNode.add(childNode);
            }
        }
        groupNodeList.clear();
    }

    private DynamicNode createGroupNode(GroupParams group) {
        DynamicNode groupNode = new DynamicNode(group);
        groupNode.setDisplayText(group.name);
        groupNode.setIcon(group.icon);
        groupNode.setGroupParams(group);
        groupNode.setHasLoaded();
        return groupNode;
    }

    private void insertProjects(SrVector children, Db db) throws DbException {
        db.beginTrans(Db.READ_TRANS);
        DbObject parent = db.getRoot();
        DbEnumeration dbEnum = parent.getComponents().elements(DbProject.metaClass);
        while (dbEnum.hasMoreElements())
            children.addElement(createPrimaryNode(parent, dbEnum.nextElement()));
        dbEnum.close();
        db.commitTrans();
    }

    // Overridden
    protected void insertComponents(SrVector children, DbObject dbParent) throws DbException {
        SemanticalModel model = ApplicationContext.getSemanticalModel();

        DbEnumeration dbEnum = dbParent.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            DbObject dbo = dbEnum.nextElement();
            boolean isVisible = model.isVisibleOnScreen(dbParent, dbo, Explorer.class);
            if (isVisible) {
                children.addElement(createPrimaryNode(dbParent, dbo));
            } // end if
        } // end while
        dbEnum.close();
    } // end insertComponents()

    // Rebuilds the hierarchy of loaded nodes.
    public final void refresh() throws DbException {
        refresh((DynamicNode) getRoot());
    }

    // Reload all
    // Use this method instead of refresh() if refresh() doesn't work.
    public final void reloadAll() throws DbException {
        setRoot(createRootNode());
    }

    protected final void refresh(DynamicNode parentNode) throws DbException {
        if (ApplicationContext.getFocusManager().isGuiLocked())
            return;

        if (parentNode == null)
            return;
        if (!parentNode.hasLoaded() || parentNode.isLeaf()) {
            if (parentNode.getUserObject() instanceof DbObject)
                updateNode((DbObject) parentNode.getUserObject());
            return;
        }
        Object userObject = parentNode.getUserObject();
        if (userObject == ROOT) {
            int count = getChildCount(parentNode);
            for (int i = 0; i < count; i++) {
                DynamicNode node = (DynamicNode) getChild(parentNode, i);
                userObject = node.getUserObject();
                if (userObject == DB_RAM)
                    refresh(node);
                else {
                    if (((Db) userObject).isValid()) {
                        ((Db) userObject).beginTrans(Db.READ_TRANS);
                        refresh(node);
                        ((Db) userObject).commitTrans();
                    } else {
                        removeNode(node);
                    }
                }
            }
            return;
        }
        if (userObject == DB_RAM) {
            int count = getChildCount(parentNode);
            for (int i = count - 1; i >= 0; i--) {
                DynamicNode node = (DynamicNode) getChild(parentNode, i);
                Db db = ((DbObject) node.getUserObject()).getDb();
                if (!db.isValid()) {
                    removeNode(node);
                    continue;
                }
                db.beginTrans(Db.READ_TRANS);
                refresh(node);
                // refresh the display text for the projects - we do not want to
                // apply a full update
                // using update(dbo) because we want to preserve the expanded
                // state for projects.
                node.setDisplayText(getDisplayText(null, (DbObject) node.getUserObject()));
                db.commitTrans();
            }
            // check for missing Db
            Db[] dbs = Db.getDbs();
            for (int i = 0; i < dbs.length; i++) {
                if (!dbs[i].isValid() || !(dbs[i] instanceof DbRAM))
                    continue;
                DynamicNode dbNode = getDynamicNode(parentNode, dbs[i], 0);
                if (dbNode != null)
                    continue;
                dbs[i].beginTrans(Db.READ_TRANS);
                DbEnumeration dbEnum = dbs[i].getRoot().getComponents().elements(
                        DbProject.metaClass);
                if (dbEnum.hasMoreElements()) {
                    getDynamicNode(dbEnum.nextElement(), false);
                }
                dbEnum.close();
                dbs[i].commitTrans();
            }
            return;
        }

        SrVector children = new SrVector(10);
        if (userObject instanceof Db) {
            insertProjects(children, (Db) userObject);
            children.sort();
        } else if (((DbObject) userObject).isAlive()) {
            insertComponents(children, (DbObject) userObject);
            if (childrenAreSorted((DbObject) userObject))
                children.sort(getComparator((DbObject) userObject));
        }
        
        DynamicNode groupNode = null;
        int index = 0;
        int iGroup = 0;
        for (int i = 0; i < children.size(); i++) {
            DynamicNode srcNode = (DynamicNode) children.elementAt(i);
            GroupParams group = srcNode.getGroupParams();
            if (group.name == null) {
                refreshNode(srcNode, parentNode, index);
                index++;
            } else {
                if (groupNode == null || !groupNode.toString().equals(group.name)) {
                    if (groupNode != null)
                        deleteNodes(groupNode, iGroup);
                    groupNode = getGroupNode(parentNode, group, index);
                    if (groupNode == null) {
                        groupNode = createGroupNode(group);
                        insertNodeInto(groupNode, parentNode, index);
                    } else if (groupNode != getChild(parentNode, index)) {
                        removeNodeFromParent(groupNode);
                        insertNodeInto(groupNode, parentNode, index);
                    }
                    index++;
                    iGroup = 0;
                }
                refreshNode(srcNode, groupNode, iGroup);
                iGroup++;
            }
        }
        if (groupNode != null)
            deleteNodes(groupNode, iGroup);
        deleteNodes(parentNode, index);

        // Refresh subnodes in a separate pass to avoid interference from
        // automatic
        // adding of a primary node when adding a secondary node.
        refreshChildren(parentNode);
    }

    private void refreshNode(DynamicNode srcNode, DynamicNode parentNode, int index)
            throws DbException {
        DynamicNode dstNode = getDynamicNode(parentNode, srcNode.getUserObject(), index);
        if (dstNode == null) {
            insertNodeInto(srcNode, parentNode, index);
        } else {
            if (dstNode != getChild(parentNode, index)) {
                removeNodeFromParent(dstNode);
                insertNodeInto(dstNode, parentNode, index);
            }
            if (dstNode.getUserObject() instanceof DynamicNode) {
                nodeChanged(dstNode); // in case text of primary node has
                // changed
            } else if (!dstNode.toString().equals(srcNode.toString())
                    || dstNode.getIcon() != srcNode.getIcon()
                    || (dstNode.getToolTips() != null && !dstNode.getToolTips().equals(
                            srcNode.getToolTips()))) {
                dstNode.setDisplayText(srcNode.toString(), srcNode.getEditText());
                dstNode.setIcon(srcNode.getIcon());
                dstNode.setToolTips(srcNode.getToolTips());
                nodeChanged(dstNode);
            }
        }
    }

    private void deleteNodes(DynamicNode parentNode, int iStart) {
        while (iStart < getChildCount(parentNode))
            removeNodeFromParent((DynamicNode) getChild(parentNode, iStart));
    }

    protected final void refreshChildren(DynamicNode parentNode) throws DbException {
        int count = getChildCount(parentNode);
        for (int i = 0; i < count; i++) {
            DynamicNode node = (DynamicNode) getChild(parentNode, i);
            if (node.getUserObject() instanceof GroupParams)
                refreshChildren(node);
            else
                refresh(node);
        }
    }

    private DynamicNode getGroupNode(DynamicNode parentNode, GroupParams group, int iStart) {
        DynamicNode nodeFound = null;
        int count = getChildCount(parentNode);
        for (int i = iStart; i < count; i++) {
            DynamicNode node = (DynamicNode) getChild(parentNode, i);
            if (node.getUserObject() instanceof GroupParams && node.toString().equals(group.name)) {
                nodeFound = node;
                break;
            }
        }
        return nodeFound;
    }

    /*
     * Search an object in the whole tree. For optimization, we build the inverse path of <dbo>
     * (from the child node to the root node), by getting the information from the database. Then,
     * instead of scanning the whole tree, we just follow the path obtained from the database. This
     * way, we always find a primary node (anyway if a secondary node exists, its primary node
     * necessarily exists).
     */
    public final DynamicNode getDynamicNode(DbObject dbo, boolean load) throws DbException {
        return getDynamicNode(dbo, Db.NEW_VALUE, load);
    }

    public final DynamicNode getDynamicNode(DbObject dbo, int which, boolean load)
            throws DbException {
        SrVector path = new SrVector(10);
        Db db = dbo.getDb();
        db.beginTrans(Db.READ_TRANS);
        while (dbo != null && !(dbo instanceof DbRoot)) {
            path.addElement(dbo);
            dbo = getDbParent(dbo, which);
        }
        if (db instanceof DbRAM) {
            if (DB_RAM != ((DynamicNode) getRoot()).getUserObject())
                path.addElement(DB_RAM);
        } else
            path.addElement(db);

        DynamicNode nodeFound = (DynamicNode) getRoot();
        for (int i = path.size(); --i >= 0;) {
            if (load)
                loadChildren(nodeFound);
            Object userObj = path.elementAt(i);
            dbo = null;
            DbObject dbParent = null;
            GroupParams group = GroupParams.defaultGroupParams;
            DynamicNode groupNode = null;
            DynamicNode node = null;
            if (userObj instanceof DbObject) {
                dbo = (DbObject) userObj;
                dbParent = getDbParent(dbo, which);
                group = getGroupParams(dbParent, dbo);
            }
            if (group.name == null) {
                node = getDynamicNode(nodeFound, userObj, 0);
            } else {
                groupNode = getGroupNode(nodeFound, group, 0);
                if (groupNode != null)
                    node = getDynamicNode(groupNode, userObj, 0);
            }
            if (node == null && nodeFound.hasLoaded() && !nodeFound.isLeaf()
                    && which == Db.NEW_VALUE) {
                
                SemanticalModel model = ApplicationContext.getSemanticalModel(); 
                boolean visible = model.isVisibleOnScreen(dbParent, dbo, Explorer.class);
                
                if (visible) {
                    node = createPrimaryNode(dbParent, dbo);
                    if (group.name == null) {
                        insertNodeInto(node, nodeFound, getInsertionIndex(dbParent, dbo, nodeFound,
                                node));
                    } else {
                        if (groupNode == null) {
                            groupNode = createGroupNode(group);
                            insertNodeInto(groupNode, nodeFound, getSortedIndex(nodeFound,
                                    groupNode));
                        }
                        insertNodeInto(node, groupNode, getInsertionIndex(dbParent, dbo, groupNode,
                                node));
                    }
                }
            }
            nodeFound = node;
            if (nodeFound == null)
                break;
        }
        db.commitTrans();
        return nodeFound;
    }

    private DynamicNode getDynamicNode(DynamicNode parentNode, Object userObj, int iStart)
            throws DbException {
        DynamicNode nodeFound = null;
        int count = getChildCount(parentNode);
        for (int i = iStart; i < count; i++) {
            DynamicNode node = (DynamicNode) getChild(parentNode, i);
            if (node.getUserObject() == userObj) {
                nodeFound = node;
                break;
            }
        }
        return nodeFound;
    }

    // The value type for the returned MetaField MUST be String.
    // Default will use getName and setName defined on DbObject
    protected MetaField getEditableMetaField(DbObject dbo) {
        return null;
    }

    // Called at the end of a cell edition, to process the edition result.
    public final void valueForPathChanged(TreePath path, Object newValue) {
        DynamicNode node = (DynamicNode) path.getLastPathComponent();
        Object obj = node.getRealObject();
        if (obj instanceof DbObject) {
            boolean editable = false;
            try {
                Db db = ((DbObject) obj).getDb();
                if (db.isValid()) {
                    db.beginReadTrans();
                    MetaField editableMetaField = getEditableMetaField((DbObject) obj);
                    if (editableMetaField == null)
                        editable = ApplicationContext.getSemanticalModel().isNameEditable(
                                (DbObject) obj, Explorer.class);
                    else
                        editable = ApplicationContext.getSemanticalModel().isEditable(
                                editableMetaField, (DbObject) obj, Explorer.class);
                    db.commitTrans();
                }
            } catch (DbException ex) {
                org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                        ApplicationContext.getDefaultMainFrame(), ex);
                editable = false;
            }
            if (!editable)
                return;
        } else {
            return;
        }
        DbObject semObj = (DbObject) obj;
        try {
            MetaField editableMetaField = getEditableMetaField(semObj);
            String transName = (editableMetaField == null ? LocaleMgr.action.getString("rename")
                    : MessageFormat.format(kUpdate0,
                            new Object[] { editableMetaField.getGUIName() }));
            semObj.getDb().beginTrans(Db.WRITE_TRANS, transName);
            if (editableMetaField == null) {
                if (semObj.getTransStatus() == Db.OBJ_REMOVED)
                    return;
                semObj.setName((String) newValue);
            } else
                semObj.set(editableMetaField, (String) newValue);
            semObj.getDb().commitTrans();
            nodeChanged(node); // in case it is a secondary node (only the
            // primary node is updated by the refresh
            // listener).
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
    }

    // ////////////////////////////////////
    // DbRefreshListener SUPPORT
    //
    // Overridden
    public void refreshAfterDbUpdate(DbUpdateEvent evt) throws DbException {
        if (evt.metaField == DbObject.fComponents) { // add, remove, or move to
            // another parent
            if (evt.neighbor instanceof DbGraphicalObjectI) // optimization:
                // discard
                // immediately the
                // graphical objects
                return;
            if (evt.op == Db.REMOVE_FROM_RELN) {
                // DbObject removed or with a new parent: remove the node from
                // its old parent if loaded.
                DynamicNode node = getDynamicNode(evt.neighbor, Db.OLD_VALUE, false);
                if (node != null)
                    removeNode(node);
            } else if (evt.op == Db.ADD_TO_RELN) {
                // DbObject added or with a new parent: if its new parent has
                // its chidren loaded,
                // add a node for the new child.
                getDynamicNode(evt.neighbor, false);
            } else { // Db.REINSERT_IN_RELN
                DynamicNode node = getDynamicNode(evt.neighbor, false);
                if ((node != null && !childrenAreSorted(evt.dbo))
                        || (node != null && !node.getGroupParams().sorted)) {
                    DynamicNode parentNode = (DynamicNode) node.getParent();
                    updateInsertIndexInChildrenOfNode(parentNode);
                    removeNodeFromParent(node);
                    int index = getInsertionIndex(evt.dbo, evt.neighbor, parentNode, node);
                    insertNodeInto(node, parentNode, index);
                }
            }
        }
        // name refresh
        else if (evt.metaField == DbSemanticalObject.fName
                || tooltipsFields.contains(evt.metaField)) {
            updateNode(evt.dbo);
        }
    }

    //
    // End of DbRefreshListener SUPPORT
    // ////////////////////////////////////

    protected final void updateInsertIndexInChildrenOfNode(DynamicNode parentNode)
            throws DbException {
        Enumeration childrenEnum = parentNode.children();
        while (childrenEnum.hasMoreElements()) {
            DynamicNode node = (DynamicNode) childrenEnum.nextElement();
            DbObject dbo = (DbObject) node.getRealObject();
            DbObject dbParent = dbo.getComposite();
            node.insertIndex = getSequence(dbParent, dbo);
        }
    }

    // Remove the group node if this node is the last of its group.
    protected final void removeNode(DynamicNode node) {
        DynamicNode parentNode = (DynamicNode) node.getParent();
        if (parentNode.getUserObject() instanceof GroupParams && getChildCount(parentNode) == 1)
            removeNodeFromParent(parentNode);
        else
            removeNodeFromParent(node);
    }

    // This method refreshes the display text of a node.
    protected final void updateNode(DbObject dbo) throws DbException {
        DynamicNode node = getDynamicNode(dbo, false);
        if (node == null)
            return;

        // backup selection and restore later
        Object focusObject = ApplicationContext.getFocusManager().getFocusObject();
        TreePath[] selPaths = null;
        if (focusObject instanceof ExplorerView) {
            selPaths = ((ExplorerView) focusObject).getSelectionPaths();
        }

        String displayText = getDisplayText(null, dbo);
        String editText = getEditText(null, dbo);
        String toolTipText = getToolTipsText(null, dbo);
        Icon icon = getIcon(dbo);

        node.setDisplayText(displayText, editText);
        node.setToolTips(toolTipText);
        node.setIcon(icon);
        nodeChanged(node);
        DynamicNode parentNode = (DynamicNode) node.getParent();
        Object parent = parentNode.getUserObject();
        if (!(parent instanceof DbObject) || childrenAreSorted((DbObject) parent)) {
            removeNodeFromParent(node);
            int index = getSortedIndex(parentNode, node);
            insertNodeInto(node, parentNode, index);
        }

        // Restore selection
        if (focusObject instanceof ExplorerView && selPaths != null) {
            ExplorerView explorerView = (ExplorerView) focusObject;
            try { // Should not occurs
                explorerView.setSelectionPaths(selPaths);
            } catch (Exception e) {
                explorerView.setSelectionPaths(new TreePath[] {});
            }
        }
    }

    // This method updates the explorer for changes in a relation N where all
    // children are secondary nodes.
    // IMPORTANT: a node whose children are secondary nodes cannot have grouping
    // nodes.
    protected final void updateSecondaryChildren(DbUpdateEvent evt) throws DbException {
        if (!ApplicationContext.getSemanticalModel().isVisibleOnScreen(evt.dbo, evt.neighbor,
                Explorer.class))
            return;
        DynamicNode parentNode = getDynamicNode(evt.dbo, false);
        if (parentNode == null || !parentNode.hasLoaded())
            return;
        DynamicNode childNode = null;
        int nb = getChildCount(parentNode);
        for (int i = 0; i < nb; i++) {
            DynamicNode node = (DynamicNode) getChild(parentNode, i);
            if (((DynamicNode) node.getUserObject()).getUserObject() == evt.neighbor) {
                childNode = node;
                break;
            }
        }
        if (evt.op == Db.REMOVE_FROM_RELN) {
            if (childNode != null)
                removeNodeFromParent(childNode);
        } else if (evt.op == Db.ADD_TO_RELN) {
            if (childNode == null) {
                childNode = createSecondaryNode(evt.dbo, evt.neighbor);
                int index = getInsertionIndex(evt.dbo, evt.neighbor, parentNode, childNode);
                insertNodeInto(childNode, parentNode, index);
            }
        } else { // Db.REINSERT_IN_RELN
            if (!childrenAreSorted(evt.dbo) && childNode != null) {
                removeNodeFromParent(childNode);
                int index = getInsertionIndex(evt.dbo, evt.neighbor, parentNode, childNode);
                insertNodeInto(childNode, parentNode, index);
            }
        }
    }

    protected final int getInsertionIndex(DbObject dbParent, DbObject dbo, DynamicNode parentNode,
            DynamicNode node) throws DbException {
        if (childrenAreSorted(dbParent))
            return getSortedIndex(parentNode, node);
        return Math.max(0, Math.min(getIndex(dbParent, dbo), getChildCount(parentNode)));
    }

    // Proceed by binary search because comparison may be long.
    protected final int getSortedIndex(DynamicNode parentNode, DynamicNode childNode) {
        int lo = -1;
        int hi = getChildCount(parentNode);
        while (lo + 1 != hi) {
            int mid = (lo + hi) / 2;
            DynamicNode node = (DynamicNode) getChild(parentNode, mid);
            if (childNode.compareTo(node) < 0)
                hi = mid;
            else
                lo = mid;
        }
        return hi;
    }

    // Overridden
    protected boolean childrenAreSorted(DbObject dbo) throws DbException {
        return true;
    }

    // Override to provide a custom Comparator.
    // If null, a default Comparator will be used.
    // parent may be null.
    // Note, this one should be moved in the group (providing a GUI name for
    // each available comparators for the group
    // and an option, may be an Action, for user selection of the comparator)
    protected Comparator getComparator(DbObject parent) throws DbException {
        return null;
    }

    protected Icon getIcon(DbObject dbo) throws DbException {
        return dbo.getSemanticalIcon(DbObject.SHORT_FORM);
    }

    // Must return the index in the children list if childrenAreSorted(dbParent)
    // returns false.
    // Overridden
    protected int getIndex(DbObject dbParent, DbObject dbo) throws DbException {
        return dbParent.getComponents().indexOf(dbo);
    }

    // Overridden
    protected DbObject getDbParent(DbObject dbo, int which) throws DbException {
        return (DbObject) dbo.get(DbObject.fComposite, which);
    }

}
