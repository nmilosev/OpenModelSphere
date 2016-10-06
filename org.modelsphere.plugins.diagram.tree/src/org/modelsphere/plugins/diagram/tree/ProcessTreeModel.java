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

package org.modelsphere.plugins.diagram.tree;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.sms.db.DbSMSSemanticalObject;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
class ProcessTreeModel extends DefaultTreeModel {
    private ArrayList[] m_levels = null;
    private int m_widestLevel = -1;

    public ProcessTreeModel(DefaultMutableTreeNode root) {
        super(root);
    }

    public int getDepth() {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.getRoot();
        int depth = root.getDepth();
        return depth;
    }

    public void computeWidestLevel(boolean isCenter, boolean isSpan) throws DbException {
        int depth = 1 + getDepth();
        m_levels = new ArrayList[depth];
        for (int i = 0; i < depth; i++) {
            m_levels[i] = new ArrayList();
        } //end for

        DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.getRoot();
        getWeight(root, isSpan); //compute weights..
        setPosition(root, isCenter, isSpan, 0);
        Enumeration enumeration = root.depthFirstEnumeration();
        while (enumeration.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
            int level = node.getLevel();
            m_levels[level].add(node);
        } //end while
    } //end computeWidestLevel()

    static int getWeight(DefaultMutableTreeNode node, boolean isSpan) throws DbException {
        ProcessTreeModel.NodeContent content = (ProcessTreeModel.NodeContent) node.getUserObject();
        int weight = 0;

        if (content.weight != null) { //if already computed
            weight = content.weight.intValue();
        } else { //compute weigth
            Enumeration enumeration = node.children();
            if (!enumeration.hasMoreElements()) { //leaf has a weigth of 1
                content.weight = new Integer(1);
                weight = 1;
            } else {
                while (enumeration.hasMoreElements()) {
                    DefaultMutableTreeNode child = (DefaultMutableTreeNode) enumeration
                            .nextElement();
                    int childWeight = getWeight(child, isSpan);
                    weight += isSpan ? childWeight : 1;
                } //end while
                content.weight = new Integer(weight);
            } //end if
        } //end if

        return weight;
    } //end computeWeight()

    static void setPosition(DefaultMutableTreeNode node, boolean isCenter, boolean isSpan, int gap)
            throws DbException {
        ProcessTreeModel.NodeContent content = (ProcessTreeModel.NodeContent) node.getUserObject();

        if (content.weight == null) { //if not already computed
            getWeight(node, isSpan);
        }

        //int gap = 0;
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
        if (parent == null) { //if root
            gap = isCenter ? (content.weight.intValue() / 2) : 0;
            content.position = new Integer(gap);
        } else {
            ProcessTreeModel.NodeContent parentContent = (ProcessTreeModel.NodeContent) parent
                    .getUserObject();
        } //end if

        Enumeration enumeration = node.children();
        int cumulatedPosition = 0;
        int parentOrigin = content.position.intValue() - gap;
        while (enumeration.hasMoreElements()) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) enumeration.nextElement();
            ProcessTreeModel.NodeContent childContent = (ProcessTreeModel.NodeContent) child
                    .getUserObject();
            gap = isCenter ? (childContent.weight.intValue() / 2) : 0;
            childContent.position = new Integer(parentOrigin + cumulatedPosition + gap);

            cumulatedPosition += childContent.weight.intValue();
            setPosition(child, isCenter, isSpan, gap);
        } //end while

    } //end setPosition()

    public int getLargestWidth(boolean isSpan) throws DbException {
        int depth = 1 + getDepth();
        int largestWidth = -1;
        for (int i = 0; i < depth; i++) {
            int width = getWidth(i, isSpan);
            if (width > largestWidth) {
                largestWidth = width;
            } //end if
        } //end for 

        return largestWidth;
    } //end getLargestWidth

    public int getWidth(int level, boolean isSpan) throws DbException {
        int totalWeight = 0;
        ArrayList list = m_levels[level];
        if (!isSpan) {
            totalWeight = list.size();
        } else {
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) iter.next();
                int weight = getWeight(node, isSpan);
                totalWeight += weight;
            } //end while
        } //end if

        return totalWeight;
    }

    public ArrayList getNodesByLevels(int level) {
        return m_levels[level];
    }

    public int getWidestLevel() {
        return m_widestLevel;
    }

    public static void main(String[] args) {

    }

    //
    // Inner class
    //
    static class NodeContent {
        DbSMSSemanticalObject semObj;
        Integer weight = null;
        Integer position = null;

        NodeContent(DbSMSSemanticalObject semObj) {
            this.semObj = semObj;
        }
    } //end NodeContent
} //end ProcessTreeModel()
