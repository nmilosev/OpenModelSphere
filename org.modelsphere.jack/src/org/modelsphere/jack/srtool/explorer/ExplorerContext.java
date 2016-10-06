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

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.preference.context.ContextComponent;
import org.modelsphere.jack.preference.context.ContextUtils;
import org.w3c.dom.*;

class ExplorerContext implements ContextComponent {
    private static final String EXPANDED_TAG = "expanded";
    private static final String EXPANDED_TYPE = "type";
    private static final String GROUP_TAG = "group";

    private static final String VIEW_TAG = "view";

    private static final String VALUE = "value";
    private static final String VIEW_MAIN = "main";
    private static final String VIEW_SECONDARY = "secondary";

    // EXPANDED_TYPE values
    private static final String GROUP = "group";
    private static final String OBJECT = "object";

    private static final String ID = "id";

    private ExplorerPanel explorer = null;

    ExplorerContext(ExplorerPanel explorer) {
        this.explorer = explorer;
    }

    @Override
    public String getID() {
        return "Explorer";
    }

    @Override
    public void loadContext(Element configurationElement) throws DbException {
        if (explorer == null) {
            return;
        }

        NodeList nodes = configurationElement.getChildNodes();
        int count = nodes.getLength();
        for (int i = 0; i < count; i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(VIEW_TAG)) {
                NamedNodeMap attributes = node.getAttributes();
                if (attributes == null)
                    continue;
                Node valueNode = attributes.getNamedItem(VALUE);
                if (valueNode == null)
                    continue;
                String view = valueNode.getNodeValue();
                if (view == null)
                    continue;
                if (view.equals(VIEW_MAIN)) {
                    loadContext(node, explorer.getMainView());
                } else if (view.equals(VIEW_SECONDARY)) {
                    loadContext(node, explorer.getSecondView());
                }
            }
        }

    }

    public void loadContext(Node viewElement, ExplorerView view) throws DbException {
        if (view == null) {
            return;
        }

        NodeList nodes = viewElement.getChildNodes();
        int count = nodes.getLength();
        for (int i = 0; i < count; i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(EXPANDED_TAG)) {
                NamedNodeMap attributes = node.getAttributes();
                if (attributes == null)
                    continue;
                Node typeNode = attributes.getNamedItem(EXPANDED_TYPE);
                if (typeNode == null)
                    continue;
                String type = typeNode.getNodeValue();
                if (type == null)
                    continue;
                if (type.equals(OBJECT)) {
                    loadObject(node, view);
                } else if (type.equals(GROUP)) {
                    loadGroup(node, view);
                }
            }
        }

    }

    private void loadObject(Node expandedNode, ExplorerView view) throws DbException {
        DbObject dbo = ContextUtils.readDOMElement((Element) expandedNode);

        if (dbo == null)
            return;

        Explorer explorer = (Explorer) view.getModel();
        if (explorer == null)
            return;
        DynamicNode node = explorer.getDynamicNode(dbo, true);
        if (node != null) {
            TreePath path = new TreePath(node.getPath());
            view.expandPath(path);
        }
    }

    private void loadGroup(Node expandedNode, ExplorerView view) throws DbException {
        NodeList nodes = expandedNode.getChildNodes();
        int count = nodes.getLength();
        String groupID = null;
        for (int i = 0; i < count; i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(GROUP_TAG)) {
                NamedNodeMap attributes = node.getAttributes();
                if (attributes == null)
                    continue;
                Node idNode = attributes.getNamedItem(ID);
                if (idNode == null)
                    continue;
                String id = idNode.getNodeValue();
                if (id == null || id.trim().length() == 0)
                    continue;
                groupID = id;
                break;
            }
        }

        if (groupID == null)
            return;

        DbObject parent = ContextUtils.readDOMElement((Element) expandedNode);

        if (parent == null) {
            return;
        }

        Explorer explorer = (Explorer) view.getModel();
        if (explorer == null)
            return;
        DynamicNode node = explorer.getDynamicNode(parent, true);
        if (node != null) {
            TreePath path = new TreePath(node.getPath());
            view.expandPath(path);

            int childcount = node.getChildCount();
            for (int i = 0; i < childcount; i++) {
                TreeNode child = node.getChildAt(i);
                if (!(child instanceof DynamicNode))
                    continue;
                Object userObject = ((DynamicNode) child).getUserObject();
                if (!(userObject instanceof GroupParams)) {
                    continue;
                }
                if (((GroupParams) userObject).getName().equals(groupID)) {
                    TreePath groupPath = new TreePath(((DynamicNode) child).getPath());
                    view.expandPath(groupPath);
                    break;
                }
            }
        }
    }

    @Override
    public boolean saveContext(Document document, Element componentConfigurationElement)
            throws DbException {
        if (explorer == null) {
            return true;
        }

        ExplorerView mainView = explorer.getMainView();
        if (mainView != null) {
            Element viewElement = document.createElement(VIEW_TAG);
            viewElement.setAttribute(VALUE, VIEW_MAIN);
            if (saveContext(document, viewElement, mainView)) {
                componentConfigurationElement.appendChild(viewElement);
            }
        }

        ExplorerView secondaryView = explorer.getSecondView();
        if (secondaryView != null) {
            Element viewElement = document.createElement(VIEW_TAG);
            viewElement.setAttribute(VALUE, VIEW_SECONDARY);
            if (saveContext(document, viewElement, secondaryView)) {
                componentConfigurationElement.appendChild(viewElement);
            }
        }

        return true;
    }

    private boolean saveContext(Document document, Element viewElement, ExplorerView view)
            throws DbException {
        if (view == null || view.getRowCount() == 0)
            return true;

        TreePath rootPath = view.getPathForRow(0);
        while (rootPath != null && rootPath.getParentPath() != null) {
            rootPath = rootPath.getParentPath();
        }

        Enumeration<TreePath> expanded = view.getExpandedDescendants(rootPath);
        if (expanded == null)
            return true;

        ArrayList<DynamicNode> expandedNodes = new ArrayList<DynamicNode>();

        while (expanded.hasMoreElements()) {
            TreePath treePath = expanded.nextElement();
            Object obj = treePath.getLastPathComponent();
            if (obj instanceof DynamicNode) {
                Object userObject = ((DynamicNode) obj).getUserObject();
                if (userObject instanceof DbObject && !(userObject instanceof DbProject)) {
                    expandedNodes.add((DynamicNode) obj);
                } else if (userObject instanceof GroupParams) {
                    TreeNode parentNode = ((DynamicNode) obj).getParent();
                    if (parentNode instanceof DynamicNode) {
                        Object parent = ((DynamicNode) parentNode).getUserObject();
                        if (parent instanceof DbObject) {
                            expandedNodes.add((DynamicNode) obj);
                        }
                    }
                }
            }
        }

        // reduce the list by removing nodes for which we already have a child present.
        for (int index = expandedNodes.size() - 1; index >= 0; index--) {
            DynamicNode dynamicNode = expandedNodes.get(index);
            // remove all the ancestors of dynamicNode
            TreeNode node = dynamicNode.getParent();
            while (node != null) {
                boolean removed = expandedNodes.remove(node);
                if (removed) {
                    index--;
                }
                if (node instanceof DefaultMutableTreeNode) {
                    node = ((DefaultMutableTreeNode) node).getParent();
                }
            }
        }

        // store the configs for the expanded nodes but limit to 100 entries.
        for (int i = 0; i < expandedNodes.size() && i < 100; i++) {
            DynamicNode dynamicNode = expandedNodes.get(i);
            Object userObject = dynamicNode.getUserObject();
            if (userObject instanceof DbObject) {
                saveObject(document, viewElement, (DbObject) userObject);
            } else if (userObject instanceof GroupParams) {
                TreeNode parentNode = dynamicNode.getParent();
                Object parent = ((DynamicNode) parentNode).getUserObject();
                saveGroup(document, viewElement, (DbObject) parent, (GroupParams) userObject);
            }
        }

        return true;
    }

    private void saveObject(Document document, Element componentConfigurationElement, DbObject dbo) {
        if (dbo == null)
            return;

        Element expandedElement = document.createElement(EXPANDED_TAG);
        if (!ContextUtils.writeDOMElement(document, expandedElement, dbo)) {
            return;
        }
        expandedElement.setAttribute(EXPANDED_TYPE, OBJECT);

        componentConfigurationElement.appendChild(expandedElement);
    }

    private void saveGroup(Document document, Element componentConfigurationElement,
            DbObject parent, GroupParams group) {
        if (group == null)
            return;

        String name = group.getName();
        if (name == null || name.trim().length() == 0) {
            return;
        }
        Element expandedElement = document.createElement(EXPANDED_TAG);
        expandedElement.setAttribute(EXPANDED_TYPE, GROUP);

        Element groupElement = document.createElement(GROUP_TAG);
        groupElement.setAttribute(ID, name);

        expandedElement.appendChild(groupElement);

        if (!ContextUtils.writeDOMElement(document, expandedElement, parent)) {
            return;
        }

        componentConfigurationElement.appendChild(expandedElement);

    }

}
