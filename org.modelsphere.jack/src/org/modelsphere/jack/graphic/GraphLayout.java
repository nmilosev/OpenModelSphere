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

package org.modelsphere.jack.graphic;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import org.modelsphere.jack.util.SrVector;

public final class GraphLayout {

    private static final int NODE_MARGIN = (int) (Diagram.PIXELS_PER_MM * 15);
    private static final int TREE_MARGIN = (int) (Diagram.PIXELS_PER_MM * 30);
    private static final double MAX_CHILD_ARC = 3 * Math.PI / 2;

    private SrVector nodes = new SrVector(100);
    private SrVector links = new SrVector(100);
    private GraphLayoutNode rootNode = new GraphLayoutNode();
    private int nbResolved;
    private int nbNewlyResolved;
    private Rectangle treeArea;
    private Point layoutPos;
    private Dimension layoutSize;

    public GraphLayout() {
    }

    public final GraphLayoutNode addNode(Rectangle rect, Object userNode) {
        GraphLayoutNode node = new GraphLayoutNode(rect, userNode);
        nodes.addElement(node);
        return node;
    }

    public final GraphLayoutLink addLink(GraphLayoutNode parentNode, GraphLayoutNode childNode,
            Object userLink) {
        GraphLayoutLink link;
        if (parentNode == childNode)
            return null; /* ignore recursive links */
        /* Keep only one link per pair of nodes */
        int nb = parentNode.links.size();
        for (int i = 0; i < nb; i++) {
            link = (GraphLayoutLink) parentNode.links.elementAt(i);
            if (link.parentNode == childNode || link.childNode == childNode) {
                link.userLinks.addElement(userLink);
                return link;
            }
        }
        link = new GraphLayoutLink(parentNode, childNode, userLink);
        links.addElement(link);
        return link;
    }

    /* Break circular references. */
    public final void dispose() {
        rootNode.children = null;
        int nb = nodes.size();
        for (int i = 0; i < nb; i++) {
            GraphLayoutNode node = (GraphLayoutNode) nodes.elementAt(i);
            node.links = null;
            node.constraintLinks = null;
            node.children = null;
        }
    }

    public final SrVector getNodes() {
        return nodes;
    }

    public final SrVector getLinks() {
        return links;
    }

    public final Dimension doLayout(Point layoutPos) {
        this.layoutPos = (layoutPos != null ? layoutPos : new Point(TREE_MARGIN, TREE_MARGIN));
        buildSpanningTrees();
        moveCloser();
        layoutTrees();
        return layoutSize;
    }

    /*
     * Distribute the nodes in a number of trees, giving to every node a parent node; a node without
     * parent is a tree root and is given as parent the rootNode.
     * 
     * The following algorithm assigns a parent node to every node:
     * 
     * 1) Assign the parent node to the child node of every hierarchical link; remove the link from
     * the constraint links of both nodes.
     * 
     * 2) Scan the vector of nodes until all nodes have a parent:
     * 
     * If a node has no constraint link, it is a tree root; assign it as parent the rootNode.
     * 
     * If a node has one constraint link, assign it as parent the opposite node on this link; remove
     * the link from the constraint links of both nodes; then check if the opposite node has zero or
     * one constraint link left; if so, reapply recursively the algorithm on it.
     * 
     * If the node having the least constraint links has more than one constraint link, remove all
     * but one constraint links, then apply the previous algorithm on this node.
     */
    private final void buildSpanningTrees() {
        nbNewlyResolved = nbResolved = 0;
        int i;
        for (i = nodes.size(); --i >= 0;) {
            GraphLayoutNode node = (GraphLayoutNode) nodes.elementAt(i);
            node.constraintLinks = (SrVector) node.links.clone();
        }
        links.sort();
        /*
         * Scan link vector backward, because it is sorted in incresing order of prio, and we want
         * to scan it in decreasing order.
         */
        for (i = links.size(); --i >= 0;) {
            GraphLayoutLink link = (GraphLayoutLink) links.elementAt(i);
            if (link.isHierar && link.childNode.parent == null)
                setParent(link.childNode, link);
        }
        int maxLink = 1;
        while (true) {
            nbNewlyResolved = 0;
            for (i = nodes.size(); --i >= 0;) {
                GraphLayoutNode node = (GraphLayoutNode) nodes.elementAt(i);
                removeCircularity(node, maxLink);
            }
            if (nbResolved == nodes.size())
                break;
            if (maxLink == 1 || nbNewlyResolved == 0)
                maxLink++;
        }
    }

    private final void removeCircularity(GraphLayoutNode node, int maxLink) {
        if (node.parent != null)
            return; /* already resolved */
        int nbLink = node.constraintLinks.size();
        if (nbLink > maxLink)
            return;
        if (nbLink == 0) /* root node of a tree */
            setParent(node, null);
        else if (nbLink == 1) {
            /*
             * Assign as parent the opposite node, remove this constraint from the opposite node,
             * and resolve the opposite node if zero or one constraint left.
             */
            GraphLayoutNode parentNode = setParent(node, (GraphLayoutLink) node.constraintLinks
                    .elementAt(0));
            if (parentNode == null)
                setParent(node, null); /*
                                        * if the parent is a child of <node>, <node> becomes a tree
                                        * root
                                        */
            else
                removeCircularity(parentNode, nbLink);
        } else {
            /*
             * Circularity case: remove any constraint to break the circularity, then reappply the
             * algorithm on the two nodes linked by this constraint.
             */
            GraphLayoutLink link = (GraphLayoutLink) node.constraintLinks.elementAt(0);
            GraphLayoutNode parentNode = (node == link.childNode ? link.parentNode : link.childNode);
            node.constraintLinks.removeElement(link);
            parentNode.constraintLinks.removeElement(link);
            link.isMoveCloser = true; /* change it to a <moveCloser> constraint */
            removeCircularity(node, nbLink - 1);
            removeCircularity(parentNode, nbLink - 1);
        }
    }

    private final GraphLayoutNode setParent(GraphLayoutNode childNode, GraphLayoutLink link) {
        GraphLayoutNode parentNode;
        if (link != null) {
            parentNode = (childNode == link.childNode ? link.parentNode : link.childNode);
            childNode.constraintLinks.removeElement(link);
            parentNode.constraintLinks.removeElement(link);
            /* Do not set the parent if the parent is already a child of <childNode>. */
            for (GraphLayoutNode node = parentNode; (node = node.parent) != null;) {
                if (node == childNode)
                    return null;
            }
        } else {
            parentNode = rootNode;
        }
        childNode.setParent(parentNode);
        nbNewlyResolved++;
        nbResolved++;
        return parentNode;
    }

    /*
     * Mark <isMoveCloser> all the links that are not in the <parent-child> hierarchy. Then, for
     * each such link, try to move as close as possible the 2 linked nodes by changing the order in
     * the children lists.
     */
    private final void moveCloser() {
        int i;
        for (i = nodes.size(); --i >= 0;) {
            GraphLayoutNode node = (GraphLayoutNode) nodes.elementAt(i);
            for (int i2 = node.constraintLinks.size(); --i2 >= 0;) {
                GraphLayoutLink link = (GraphLayoutLink) node.constraintLinks.elementAt(i2);
                link.isMoveCloser = true;
            }
        }
        setLevelSubTree(rootNode, 0);
        /*
         * Scan link vector backward, because it is sorted in incresing order of prio, and we want
         * to scan it in decreasing order.
         */
        for (i = links.size(); --i >= 0;) {
            GraphLayoutLink link = (GraphLayoutLink) links.elementAt(i);
            if (!link.isMoveCloser)
                continue;
            GraphLayoutNode leftNode = link.parentNode;
            GraphLayoutNode rightNode = link.childNode;
            GraphLayoutNode leftParentNode = leftNode;
            GraphLayoutNode rightParentNode = rightNode;
            while (leftParentNode.level > rightParentNode.level)
                leftParentNode = leftParentNode.parent;
            while (rightParentNode.level > leftParentNode.level)
                rightParentNode = rightParentNode.parent;
            if (leftParentNode == rightParentNode)
                continue; /* if one is ancestor of the other, do not put them closer. */
            while (leftParentNode.parent != rightParentNode.parent) {
                leftParentNode = leftParentNode.parent;
                rightParentNode = rightParentNode.parent;
            }
            SrVector children = leftParentNode.parent.children;
            int iLeft = children.indexOf(leftParentNode);
            int iRight = children.indexOf(rightParentNode);
            if (iLeft > iRight) {
                GraphLayoutNode node = leftNode;
                leftNode = rightNode;
                rightNode = node;
                node = leftParentNode;
                leftParentNode = rightParentNode;
                rightParentNode = node;
                int index = iLeft;
                iLeft = iRight;
                iRight = index;
            }
            if (iLeft != iRight - 1) {
                if (!leftParentNode.movedCloser) {
                    children.removeElementAt(iLeft);
                    children.insertElementAt(leftParentNode, iRight - 1);
                } else if (!rightParentNode.movedCloser) {
                    children.removeElementAt(iRight);
                    children.insertElementAt(rightParentNode, iLeft + 1);
                } else
                    continue;
            }
            leftParentNode.movedCloser = true;
            rightParentNode.movedCloser = true;
            while (leftNode != leftParentNode) {
                if (!leftNode.movedCloser) {
                    children = leftNode.parent.children;
                    children.removeElement(leftNode);
                    children.addElement(leftNode);
                }
                leftNode.movedCloser = true;
                leftNode = leftNode.parent;
            }
            while (rightNode != rightParentNode) {
                if (!rightNode.movedCloser) {
                    children = rightNode.parent.children;
                    children.removeElement(rightNode);
                    children.insertElementAt(rightNode, 0);
                }
                rightNode.movedCloser = true;
                rightNode = rightNode.parent;
            }
        }
    }

    private final void setLevelSubTree(GraphLayoutNode node, int level) {
        node.level = level;
        for (int i = node.children.size(); --i >= 0;) {
            GraphLayoutNode childNode = (GraphLayoutNode) node.children.elementAt(i);
            setLevelSubTree(childNode, level + 1);
        }
    }

    /*
     * Do the layout of each tree independantly, then move each tree at the right of the previous
     * tree.
     */
    private final void layoutTrees() {
        layoutSize = new Dimension(layoutPos.x, layoutPos.y);
        int nb = rootNode.children.size(); /*
                                            * children lists must be scanned forward for
                                            * positionning.
                                            */
        for (int i = 0; i < nb; i++) {
            GraphLayoutNode node = (GraphLayoutNode) rootNode.children.elementAt(i);
            layoutTree(node);
            moveSubTree(node, layoutSize.width - treeArea.x, layoutPos.y - treeArea.y);
            layoutSize.width += treeArea.width + TREE_MARGIN;
            layoutSize.height = Math.max(layoutSize.height, layoutPos.y + treeArea.height
                    + TREE_MARGIN);
        }
    }

    /*
     * Calculate the rectangle of all the nodes of a tree, centered on the root node. Calculate in
     * <treeArea> the rectangle of the whole tree.
     */
    private final void layoutTree(GraphLayoutNode node) {
        calcArcSubTree(node);
        if (node.arcAngle > 2 * Math.PI)
            node.arcAngle = 2 * Math.PI;
        node.startAngle = (3 * Math.PI - node.arcAngle) / 2;
        node.rect.x = -node.rect.width / 2;
        node.rect.y = -node.rect.height / 2;
        treeArea = new Rectangle(node.rect);
        calcRectSubTree(node);
    }

    private final void calcArcSubTree(GraphLayoutNode node) {
        node.bigWidth = node.rect.width + NODE_MARGIN;
        node.bigHeight = node.rect.height + NODE_MARGIN;
        node.radius = Math.sqrt((double) node.bigWidth * node.bigWidth + (double) node.bigHeight
                * node.bigHeight) / 2;
        if (node.parent == rootNode) {
            node.distance = 0;
            node.arcAngle = 0;
        } else {
            node.distance = node.parent.distance + node.parent.radius + node.radius;
            node.arcAngle = 2 * Math.asin(node.radius / node.distance);
        }
        node.childrenArcAngle = 0;
        int nb = node.children.size();
        for (int i = 0; i < nb; i++) {
            GraphLayoutNode childNode = (GraphLayoutNode) node.children.elementAt(i);
            calcArcSubTree(childNode);
            node.childrenArcAngle += childNode.arcAngle;
        }
        if (node.arcAngle < node.childrenArcAngle)
            node.arcAngle = node.childrenArcAngle;
    }

    private final void calcRectSubTree(GraphLayoutNode node) {
        int nb = node.children.size(); /* children lists must be scanned forward for positionning. */
        if (nb == 0)
            return;
        double startAngle = node.startAngle;
        double arcFactor = node.arcAngle / node.childrenArcAngle;
        for (int i = 0; i < nb; i++) {
            GraphLayoutNode childNode = (GraphLayoutNode) node.children.elementAt(i);
            childNode.startAngle = startAngle;
            childNode.arcAngle *= arcFactor;
            startAngle += childNode.arcAngle;
            if (childNode.arcAngle > MAX_CHILD_ARC) {
                childNode.startAngle += (childNode.arcAngle - MAX_CHILD_ARC) / 2;
                childNode.arcAngle = MAX_CHILD_ARC;
            }
            calcRectNode(childNode);
            treeArea.add(childNode.rect);
            calcRectSubTree(childNode);
        }
    }

    private final void calcRectNode(GraphLayoutNode node) {
        node.distance = node.parent.distance + node.parent.radius + node.radius;
        if (node.arcAngle < Math.PI) {
            double distance = node.radius / Math.sin(node.arcAngle / 2);
            if (node.distance < distance)
                node.distance = distance;
        }
        double theta = node.startAngle + node.arcAngle / 2;
        node.rect.x = (int) (node.distance * Math.cos(theta)) - node.rect.width / 2;
        node.rect.y = (-(int) (node.distance * Math.sin(theta))) - node.rect.height / 2;
    }

    private final void moveSubTree(GraphLayoutNode node, int x, int y) {
        node.rect.x += x;
        node.rect.y += y;
        int nb = node.children.size();
        for (int i = 0; i < nb; i++) {
            GraphLayoutNode childNode = (GraphLayoutNode) node.children.elementAt(i);
            moveSubTree(childNode, x, y);
        }
    }
}
