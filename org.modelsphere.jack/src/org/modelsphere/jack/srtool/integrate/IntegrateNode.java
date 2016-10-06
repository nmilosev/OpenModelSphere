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

package org.modelsphere.jack.srtool.integrate;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

import org.modelsphere.jack.awt.tree.CheckTreeNode;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.explorer.GroupParams;
import org.modelsphere.jack.util.CollationComparator;

public class IntegrateNode extends DefaultMutableTreeNode implements Comparable {

    private DbObject leftDbo;
    private String leftName;
    private DbObject rightDbo;
    private String rightName;
    private Icon icon;
    private GroupParams groupParams;
    private CheckTreeNode classNode = null;
    private int action = IntegrateModel.ACT_NONE;
    private boolean isGroup;
    private boolean isDifferent = false;
    private IntegrateProperty[] properties = null;

    public IntegrateNode() {
        super();
    }

    // Constructor for a normal node
    public IntegrateNode(DbObject leftDbo, String leftName, DbObject rightDbo, String rightName,
            Icon icon, GroupParams groupParams) throws DbException {
        super();
        this.leftDbo = leftDbo;
        this.leftName = (leftDbo != null ? leftName : rightName);
        this.rightDbo = rightDbo;
        this.rightName = (rightDbo != null ? rightName : leftName);
        this.icon = icon;
        this.groupParams = groupParams;
        isGroup = false;

        if (rightDbo != null && leftDbo == null) {
            TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
            Terminology terminology = terminologyUtil.findModelTerminology(rightDbo.getComposite());

            if (terminologyUtil.isObjectUseCaseOrBEModel(rightDbo)) {
                if (terminologyUtil.isObjectUseCase(rightDbo))
                    this.icon = terminologyUtil.getUseCaseIcon(rightDbo);
                else
                    this.icon = terminologyUtil.findModelTerminology(rightDbo).getIcon(
                            rightDbo.getMetaClass());
            } else if (terminologyUtil.isObjectUseCaseOrBEModel(rightDbo.getComposite())) {
                if (terminologyUtil.isObjectUseCase(rightDbo))
                    this.icon = terminologyUtil.getUseCaseIcon(rightDbo);
                else
                    this.icon = terminologyUtil.findModelTerminology(rightDbo).getIcon(
                            rightDbo.getMetaClass());
            }
        }

    }

    // Constructor for a group node
    public IntegrateNode(GroupParams groupParams) {
        super();
        this.groupParams = groupParams;
        leftName = groupParams.name;
        rightName = "";
        icon = groupParams.icon;
        isGroup = true;
    }

    public final DbObject getLeftDbo() {
        return leftDbo;
    }

    public final String getLeftName() {
        return leftName;
    }

    public final DbObject getRightDbo() {
        return rightDbo;
    }

    public final String getRightName() {
        return rightName;
    }

    public final void setRightDbo(DbObject rightDbo, String rightName) {
        this.rightDbo = rightDbo;
        this.rightName = (rightDbo != null ? rightName : leftName);
    }

    public final Icon getIcon() {
        return icon;
    }

    public final GroupParams getGroupParams() {
        return groupParams;
    }

    public final CheckTreeNode getClassNode() {
        return classNode;
    }

    public final void setClassNode(CheckTreeNode classNode) {
        this.classNode = classNode;
    }

    public final int getAction() {
        return action;
    }

    public final void setAction(int action) {
        this.action = action;
    }

    public final boolean isGroup() {
        return isGroup;
    }

    public final boolean isDifferent() {
        return isDifferent;
    }

    public final void setDifferent(boolean state) {
        isDifferent = state;
    }

    public final IntegrateProperty[] getProperties() {
        return properties;
    }

    public final void setProperties(IntegrateProperty[] properties) {
        this.properties = properties;
    }

    public final String toString() {
        return leftName;
    }

    public final int compareTo(Object obj) {
        IntegrateNode otherNode = (IntegrateNode) obj;
        if (groupParams.index != otherNode.groupParams.index)
            return (groupParams.index < otherNode.groupParams.index ? -1 : 1);
        return CollationComparator.getDefaultCollator().compare(leftName, otherNode.leftName);
    }

    public static class IntegrateProperty {

        private Object property; // 4 cases: MetaField, MetaClass (filter on
        // fComponents), MetaField[], DbUDF
        private String name;
        private String leftVal;
        private String leftTip;
        private String rightVal;
        private String rightTip;
        private int action;

        public IntegrateProperty(Object property, String name, String leftVal, String rightVal,
                int action) {
            this.property = property;
            this.name = name;
            this.leftVal = leftVal;
            this.leftTip = leftVal;
            this.rightVal = rightVal;
            this.rightTip = rightVal;
            this.action = action;
        }

        public final Object getProperty() {
            return property;
        }

        public final String getName() {
            return name;
        }

        public final String getLeftVal() {
            return leftVal;
        }

        public final String getLeftTip() {
            return leftTip;
        }

        public final void setLeftTip(String leftTip) {
            this.leftTip = leftTip;
        }

        public final String getRightVal() {
            return rightVal;
        }

        public final String getRightTip() {
            return rightTip;
        }

        public final void setRightTip(String rightTip) {
            this.rightTip = rightTip;
        }

        public final int getAction() {
            return action;
        }

        public final void setAction(int action) {
            this.action = action;
        }

        public final String toString() {
            return name;
        }
    } // End of class IntegrateProperty
}
