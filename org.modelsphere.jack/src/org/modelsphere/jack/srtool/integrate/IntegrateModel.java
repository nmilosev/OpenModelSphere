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

import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.modelsphere.jack.awt.TextViewerDialog;
import org.modelsphere.jack.awt.tree.CheckTreeNode;
import org.modelsphere.jack.awt.treeTable.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.DbUDF.UDFMap;
import org.modelsphere.jack.baseDb.db.srtypes.UDFValueType;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.screen.LookupDialog;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.explorer.Explorer;
import org.modelsphere.jack.srtool.explorer.GroupParams;
import org.modelsphere.jack.srtool.integrate.IntegrateNode.IntegrateProperty;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.*;

public class IntegrateModel extends DefaultTreeModel implements TreeTableModel {

    public static final int COL_LEFT = 0;
    public static final int COL_RIGHT = 1;
    public static final int COL_ACTION = 2;

    TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

    // Must be in the order defined by the constants COL_*
    private static final String[] defaultColHeaders = new String[] {
            LocaleMgr.screen.getString("model1"), LocaleMgr.screen.getString("model2"),
            LocaleMgr.screen.getString("action") };

    public static final int PROP_NAME = 0;
    public static final int PROP_LEFT = 1;
    public static final int PROP_RIGHT = 2;
    public static final int PROP_ACTION = 3;

    // Must be in the order defined by the constants PROP_*
    private static final String[] defaultPropHeaders = new String[] {
            LocaleMgr.screen.getString("property"), LocaleMgr.screen.getString("model1Value"),
            LocaleMgr.screen.getString("model2Value"), LocaleMgr.screen.getString("action") };

    public static final int ACT_NONE = 0;
    public static final int ACT_ADD_IN_LEFT = 1;
    public static final int ACT_DELETE_LEFT = 2;
    public static final int ACT_MERGE_IN_LEFT = 3;
    public static final int ACT_REPLACE_LEFT = 4;
    public static final int ACT_MODIFY_LEFT = 5;
    public static final int ACT_LAST_LEFT = ACT_MODIFY_LEFT;

    public static final int ACT_ADD_IN_RIGHT = 6;
    public static final int ACT_DELETE_RIGHT = 7;
    public static final int ACT_MERGE_IN_RIGHT = 8;
    public static final int ACT_REPLACE_RIGHT = 9;
    public static final int ACT_MODIFY_RIGHT = 10;

    // Must be in the order defined by the constants ACT_*
    private static final String[] defaultActionNames = new String[] {
            LocaleMgr.screen.getString("none"), LocaleMgr.screen.getString("addInModel1"),
            LocaleMgr.screen.getString("deleteInModel1"),
            LocaleMgr.screen.getString("mergeInModel1"),
            LocaleMgr.screen.getString("replaceInModel1"),
            LocaleMgr.screen.getString("modifyInModel1"),
            LocaleMgr.screen.getString("addInModel2"),
            LocaleMgr.screen.getString("deleteInModel2"),
            LocaleMgr.screen.getString("mergeInModel2"),
            LocaleMgr.screen.getString("replaceInModel2"),
            LocaleMgr.screen.getString("modifyInModel2") };

    public static final int[] leftActions = new int[] { ACT_ADD_IN_RIGHT, ACT_DELETE_LEFT, ACT_NONE };
    public static final int[] rightActions = new int[] { ACT_ADD_IN_LEFT, ACT_DELETE_RIGHT,
            ACT_NONE };
    public static final int[] combineActions = new int[] { ACT_MERGE_IN_RIGHT, ACT_REPLACE_RIGHT,
            ACT_MERGE_IN_LEFT, ACT_REPLACE_LEFT, ACT_NONE };
    public static final int[] propertyActions = new int[] { ACT_MODIFY_RIGHT, ACT_MODIFY_LEFT,
            ACT_NONE };
    public static final int[] noActions = new int[] { ACT_NONE };

    private static final String listOfDifferences = LocaleMgr.screen.getString("listDiff");
    private static final String defaultFrameTitle = LocaleMgr.screen.getString("integration");
    private static final String defaultIntegButtonName = LocaleMgr.screen.getString("integrate");
    private static final String defaultIntegActionName = LocaleMgr.action.getString("integration");
    private static final String levelStr = "- "; //NOT LOCALIZABLE
    private static final String valSep = "          "; //NOT LOCALIZABLE

    protected String[] colHeaders = defaultColHeaders;
    protected String[] propHeaders = defaultPropHeaders;
    protected String[] actionNames = defaultActionNames;
    protected String frameTitle = listOfDifferences;
    protected String integButtonName = defaultIntegButtonName;
    protected String integActionName = defaultIntegActionName;
    protected String matchActionName = null;
    protected DbObject leftModel;
    protected DbObject rightModel;
    protected CheckTreeNode fieldTree;
    protected boolean usePhysName;
    protected boolean ignoreCase;
    protected boolean noRightUpdate; // do not modify right for synchro
    protected boolean externalMatchByName; // if no match possible, match by names
    protected UDFMap leftUdfMap;
    protected UDFMap rightUdfMap;
    protected IntegrateFrame frame;
    protected DeepCopyCustomizer leftCopyCustom;
    protected DeepCopyCustomizer rightCopyCustom;

    // A matchingObject sesssion must be started before instantiating the IntegrateModel.
    public IntegrateModel(DbObject leftModel, DbObject rightModel, CheckTreeNode fieldTree,
            boolean usePhysName, boolean ignoreCase, boolean noRightUpdate) throws DbException {
        this(leftModel, rightModel, fieldTree, false, usePhysName, ignoreCase, noRightUpdate);
    }

    // A matchingObject sesssion must be started before instantiating the IntegrateModel.
    public IntegrateModel(DbObject leftModel, DbObject rightModel, CheckTreeNode fieldTree,
            boolean externalMatchByName, boolean usePhysName, boolean ignoreCase,
            boolean noRightUpdate) throws DbException {
        super(new IntegrateNode()); // temporary root
        this.leftModel = leftModel;
        this.rightModel = rightModel;
        this.fieldTree = fieldTree;
        this.externalMatchByName = externalMatchByName;
        this.usePhysName = usePhysName;
        this.ignoreCase = ignoreCase;
        this.noRightUpdate = noRightUpdate;
        DbProject leftProject = leftModel.getProject();
        DbProject rightProject = rightModel.getProject();
        leftUdfMap = new UDFMap(leftProject);
        rightUdfMap = (leftProject == rightProject ? leftUdfMap : new UDFMap(rightProject));
        leftProject.setMatchingObject(rightProject);
        leftModel.setMatchingObject(rightModel);
    }

    // Subclass calls <populate> in the constructor.
    public final void populate() throws DbException {
        IntegrateNode rootNode = createLeftNode(null, leftModel);
        rootNode.setAction(getRootDefaultAction());
        setRoot(rootNode);

        rootNode.setClassNode(findClassNode(leftModel.getMetaClass()));
        // Load the complete model and perform object association.
        loadChildren(rootNode, true);

        // Compare the properties of all associated objects; must be done in a separate pass after loading,
        // because it requires that the association process be completed for comparing relation properties.
        buildProperties(rootNode);
    }

    public final void setFrame(IntegrateFrame frame) {
        this.frame = frame;
    }

    public final IntegrateFrame getFrame() {
        return frame;
    }

    public final String getFrameTitle() {
        return frameTitle;
    }

    public final String getIntegButtonName() {
        return integButtonName;
    }

    public final String[] getColHeaders() {
        return colHeaders;
    }

    public final String[] getPropHeaders() {
        return propHeaders;
    }

    public final String[] getActionNames() {
        return actionNames;
    }

    /*
     * Load the complete node hierarchy from <parentNode>, and perform object association.
     * Associated objects keep a reference one to the other by means of the matchingObject facility.
     * Recursive method.
     */
    private void loadChildren(IntegrateNode parentNode, boolean loading) throws DbException {
        if (!parentNode.getAllowsChildren())
            return;
        boolean preMatched = preMatch(parentNode, loading);

        // Get all the left children and sort them.
        int i;
        SrVector childNodes = new SrVector();
        DbObject parent = parentNode.getLeftDbo();
        ArrayList children = getChildren(parent);
        for (i = 0; i < children.size(); i++) {
            DbObject child = (DbObject) children.get(i);
            // If not pre-matched, set explicitly to <no matching object>;
            // initially it is set to <matching object not searched yet>.
            if (child.getMatchingObject() == null)
                child.setMatchingObject(null); // insure the status is <no matching object>.
            childNodes.add(createLeftNode(parent, child));
        }
        childNodes.sort();

        // Get all the right children; if an unmatched right child has same name as an unmatched left child, match them;
        // otherwise, insert the right child in the sorted list.
        parent = parentNode.getRightDbo();
        children = getChildren(parent);
        for (i = 0; i < children.size(); i++) {
            DbObject child = (DbObject) children.get(i);
            if (child.getMatchingObject() != null)
                continue; // already matched
            IntegrateNode childNode = createRightNode(parent, child);
            // binarySearch returns the first node with a comparable name ignoring case and accents.
            int index = childNodes.binarySearch(childNode);
            boolean isAdd = (preMatched || index < 0);
            while (!isAdd) {
                IntegrateNode leftNode = (IntegrateNode) childNodes.get(index);
                if (equalsName(leftNode.getLeftName(), childNode.getRightName())
                        && leftNode.getRightDbo() == null
                        && isMatchable(leftNode.getLeftDbo(), child)) {
                    leftNode.setRightDbo(child, childNode.getRightName());
                    leftNode.getLeftDbo().setMatchingObject(child);
                    postMatch(leftNode);
                    break;
                }
                index++;
                isAdd = (index == childNodes.size() || childNode.compareTo(childNodes.get(index)) != 0);
            }
            if (isAdd) {
                child.setMatchingObject(null); // insure the status is <no matching object>.
                if (index < 0)
                    index = -(index + 1);
                childNodes.add(index, childNode);
            }
        }

        // Attach all the child nodes to their corresponding group nodes under the parent node.
        // The children list is already sorted by group and name.
        IntegrateNode groupNode = null;
        for (i = 0; i < childNodes.size(); i++) {
            IntegrateNode childNode = (IntegrateNode) childNodes.get(i);
            DbObject leftDbo = childNode.getLeftDbo();
            DbObject rightDbo = childNode.getRightDbo();
            MetaClass metaClass = (leftDbo != null ? leftDbo.getMetaClass() : rightDbo
                    .getMetaClass());
            childNode.setClassNode(findClassNode(metaClass));
            childNode.setAllowsChildren(leftDbo != null && rightDbo != null
                    && allowsChildren(leftDbo));
            childNode.setAction(getDefaultAction(childNode, parentNode.getAction()));

            GroupParams groupParams = childNode.getGroupParams();
            if (groupParams.name == null) { // no group, the child goes directly under the parent node.
                parentNode.add(childNode);
                groupNode = null;
            } else {
                if (groupNode == null || !groupNode.getGroupParams().equals(groupParams)) {
                    groupNode = new IntegrateNode(groupParams); // passing to the following group
                    groupNode.setAction(parentNode.getAction());
                    parentNode.add(groupNode);
                }
                groupNode.add(childNode);
            }
            // Load recursvely the hierarchy  of the child node.
            loadChildren(childNode, loading);
        }
    }

    private IntegrateNode createLeftNode(DbObject parent, DbObject leftDbo) throws DbException {
        DbObject rightDbo = leftDbo.getMatchingObject();
        String rightName = (rightDbo != null ? getDisplayName(rightDbo) : null);
        Icon icon = null;
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        if (terminologyUtil.isDataModel(leftDbo)) {
            if (terminologyUtil.getModelLogicalMode(leftDbo) == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                icon = terminologyUtil.getConceptualModelIcon();
            } else {
                icon = leftDbo.getSemanticalIcon(DbObject.SHORT_FORM);
            }
        } else if (terminologyUtil.isObjectUseCaseOrBEModel(leftDbo)) {
            if (terminologyUtil.isObjectUseCase(leftDbo))
                icon = terminologyUtil.getUseCaseIcon(leftDbo);
            else
                icon = terminologyUtil.findModelTerminology(leftDbo)
                        .getIcon(leftDbo.getMetaClass());
        } else if (terminologyUtil.isObjectUseCaseOrBEModel(leftDbo.getComposite())) {
            if (terminologyUtil.isObjectUseCase(leftDbo))
                icon = terminologyUtil.getUseCaseIcon(leftDbo);
            else
                icon = terminologyUtil.findModelTerminology(leftDbo)
                        .getIcon(leftDbo.getMetaClass());
        }

        if (icon == null)
            return new IntegrateNode(leftDbo, getDisplayName(leftDbo), rightDbo, rightName, leftDbo
                    .getSemanticalIcon(DbObject.SHORT_FORM), getGroupParams(parent, leftDbo));
        else
            return new IntegrateNode(leftDbo, getDisplayName(leftDbo), rightDbo, rightName, icon,
                    getGroupParams(parent, leftDbo));
    }

    private IntegrateNode createRightNode(DbObject parent, DbObject rightDbo) throws DbException {
        return new IntegrateNode(null, null, rightDbo, getDisplayName(rightDbo), rightDbo
                .getSemanticalIcon(DbObject.SHORT_FORM), getGroupParams(parent, rightDbo));
    }

    /*
     * Build the list of properties whose values differ for all associated objects int the hierarchy
     * of <node>. Set the <isDifferent> flag: set to true for: - an unassociated object, -
     * associated objects with properties that differ or with at least one child whose <isDifferent>
     * flag is set. Recursive method.
     */
    private boolean buildProperties(IntegrateNode node) throws DbException {
        boolean isDifferent = false;
        if (!node.isGroup()) {
            if (node.getLeftDbo() == null || node.getRightDbo() == null)
                isDifferent = true;
            else if (node != getRoot()) // do not load properties for model node
                isDifferent = buildPropertiesAux(node);
            else if (node == getRoot() && allowRootPropertiesCompare(node.getLeftDbo())) // allow load properties for some specific cases (database)
                isDifferent = buildPropertiesAux(node);
        }
        int nb = node.getChildCount();
        for (int i = 0; i < nb; i++) {
            IntegrateNode childNode = (IntegrateNode) node.getChildAt(i);
            if (buildProperties(childNode))
                isDifferent = true;
        }
        node.setDifferent(isDifferent);
        return isDifferent;
    }

    /*
     * Return true to allow properties to be compare for the root DbObject
     */
    protected boolean allowRootPropertiesCompare(DbObject rootDbObject) {
        return false;
    }

    // Build the list of properties that differ for a single node; only properties selected in the scope are compared.
    private boolean buildPropertiesAux(IntegrateNode node) throws DbException {
        DbObject leftDbo = node.getLeftDbo();
        DbObject rightDbo = node.getRightDbo();
        int action = getBasicAction(node.getAction());
        ArrayList propList = new ArrayList();
        CheckTreeNode classNode = node.getClassNode();
        if (classNode != null && classNode.isSelected()) {
            int nb = classNode.getChildCount();
            for (int i = 0; i < nb; i++) {
                CheckTreeNode fieldNode = (CheckTreeNode) classNode.getChildAt(i);
                if (!fieldNode.isSelected())
                    continue;
                if (fieldNode.getUserObject() == null)
                    buildUdfs(propList, leftDbo, rightDbo, action);
                else
                    buildProperty(propList, leftDbo, rightDbo, fieldNode, action);
            }
        }
        IntegrateProperty[] props = null;
        if (propList.size() != 0) {
            props = new IntegrateProperty[propList.size()];
            propList.toArray(props);
        }
        node.setProperties(props);
        return (props != null);
    }

    // Compare a single property of <leftDbo> and <rightDbo> and add an antry to the property list if values differ.
    private void buildProperty(ArrayList propList, DbObject leftDbo, DbObject rightDbo,
            CheckTreeNode fieldNode, int action) throws DbException {
        Object property = fieldNode.getUserObject();
        if (property instanceof MetaField[]) { // if navigation path, navigate to the last dbObject of the path.
            MetaField[] fields = (MetaField[]) property;
            for (int i = 0; i < fields.length - 1; i++) {
                if (leftDbo != null)
                    leftDbo = (DbObject) leftDbo.get(fields[i]);
                if (rightDbo != null)
                    rightDbo = (DbObject) rightDbo.get(fields[i]);
            }
            property = fields[fields.length - 1];
        }

        // BEWARE: order of instanceof's important because MetaRelationN is subclass of MetaRelationship, itself subclass of MetaField
        if (property instanceof MetaClass) {
            // Multiple value made up of all the components of the given class.
            MetaClass metaClass = (MetaClass) property;
            DbObject[] leftVal = getRelNProperty(leftDbo, metaClass);
            DbObject[] rightVal = getRelNProperty(rightDbo, metaClass);
            if (!equalProperty(metaClass, leftVal, rightVal)) {
                propList.add(new IntegrateProperty(fieldNode.getUserObject(), fieldNode.toString(),
                        getPropertyString(metaClass, leftVal), getPropertyString(metaClass,
                                rightVal), action));
            }
        } else if (property instanceof MetaRelationN) {
            // Multiple value made up of all the neighbors.
            MetaRelationN metaRelN = (MetaRelationN) property;
            DbObject[] leftVal = getRelNProperty(leftDbo, metaRelN);
            DbObject[] rightVal = getRelNProperty(rightDbo, metaRelN);
            if (!equalProperty(metaRelN, leftVal, rightVal)) {
                propList.add(new IntegrateProperty(fieldNode.getUserObject(), fieldNode.toString(),
                        getPropertyString(metaRelN, leftVal),
                        getPropertyString(metaRelN, rightVal), action));
            }
        } else if (property instanceof MetaRelationship) { // MetaRelation1 or MetaChoice
            MetaRelationship metaRel = (MetaRelationship) property;
            DbObject leftVal = (leftDbo != null ? (DbObject) leftDbo.get(metaRel) : null);
            DbObject rightVal = (rightDbo != null ? (DbObject) rightDbo.get(metaRel) : null);
            if (!equalProperty(metaRel, leftVal, rightVal)) {
                String leftStr = (leftVal != null ? getName(leftVal) : null);
                String rightStr = (rightVal != null ? getName(rightVal) : null);
                IntegrateProperty prop = new IntegrateProperty(fieldNode.getUserObject(), fieldNode
                        .toString(), leftStr, rightStr, action);
                prop.setLeftTip(getQualifiedName(leftVal, leftModel));
                prop.setRightTip(getQualifiedName(rightVal, rightModel));
                propList.add(prop);
            }
        } else if (property instanceof MetaField) { // ordinary field
            MetaField field = (MetaField) property;
            if (!(field.equals(DbObject.fCreationTime) || field.equals(DbObject.fModificationTime))) {
                Object leftVal = (leftDbo != null ? leftDbo.get(field) : null);
                Object rightVal = (rightDbo != null ? rightDbo.get(field) : null);
                if (usePhysName && field == DbSemanticalObject.fPhysicalName) {
                    leftVal = getName(leftDbo);
                    rightVal = getName(rightDbo);
                }
                boolean isEqual;
                if (field == DbSemanticalObject.fName || field == DbSemanticalObject.fPhysicalName)
                    isEqual = equalsName((String) leftVal, (String) rightVal);
                else
                    isEqual = DbObject.valuesAreEqual(leftVal, rightVal);
                if (!isEqual && !isEquivalent(field, leftDbo, leftVal, rightDbo, rightVal)) {
                    String leftStr = (leftVal != null ? leftVal.toString() : null);
                    String rightStr = (rightVal != null ? rightVal.toString() : null);
                    propList.add(new IntegrateProperty(fieldNode.getUserObject(), fieldNode
                            .toString(), leftStr, rightStr, action));
                }
            }
        } else {
            buildCustomProperty(propList, leftDbo, rightDbo, fieldNode, action);
        }
    }

    // This method is used for non standard path (non metafield or metaclass) elements
    protected void buildCustomProperty(ArrayList propList, DbObject leftDbo, DbObject rightDbo,
            CheckTreeNode fieldNode, int action) throws DbException {
    }

    private DbObject[] getRelNProperty(DbObject dbo, MetaRelationN metaRelN) throws DbException {
        if (dbo == null)
            return new DbObject[0];
        DbRelationN relN = (DbRelationN) dbo.get(metaRelN);
        DbObject[] dbos = new DbObject[relN.size()];
        for (int i = 0; i < dbos.length; i++)
            dbos[i] = relN.elementAt(i);
        return dbos;
    }

    private DbObject[] getRelNProperty(DbObject dbo, MetaClass metaClass) throws DbException {
        ArrayList dboList = new ArrayList();
        if (dbo != null) {
            DbEnumeration dbEnum = dbo.getComponents().elements(metaClass);
            while (dbEnum.hasMoreElements())
                dboList.add(dbEnum.nextElement());
            dbEnum.close();
        }
        DbObject[] dbos = new DbObject[dboList.size()];
        dboList.toArray(dbos);
        return dbos;
    }

    private String getQualifiedName(DbObject dbo, DbObject model) throws DbException {
        if (dbo == null)
            return null;
        String name = getName(dbo);
        while (true) {
            dbo = dbo.getComposite();
            if (dbo == null || dbo == model
                    || (dbo.getMetaClass().getFlags() & MetaClass.NAMING_ROOT) != 0)
                break;
            name = getName(dbo) + '.' + name;
        }
        return name;
    }

    protected final String getName(DbObject dbo) throws DbException {
        if (dbo == null)
            return null;
        if (usePhysName && dbo instanceof DbSemanticalObject) {
            String physName = ((DbSemanticalObject) dbo).getPhysicalName();
            if (physName != null && physName.length() != 0)
                return physName;
        }
        if (terminologyUtil.isObjectLine(dbo))
            return ApplicationContext.getSemanticalModel().getDisplayText(dbo, Explorer.class);
        else
            return dbo.getName();
    }

    protected final boolean equalsName(String name1, String name2) {
        if (name1 == null)
            return (name2 == null || name2.length() == 0);
        if (name2 == null)
            return (name1.length() == 0);
        return (ignoreCase ? name1.equalsIgnoreCase(name2) : name1.equals(name2));
    }

    // Add to the property list an entry for each UDF whose values differ between <leftDbo> and <rightDbo>.
    private void buildUdfs(ArrayList propList, DbObject leftDbo, DbObject rightDbo, int action)
            throws DbException {
        boolean sameUdf = (leftDbo.getProject() == rightDbo.getProject() && leftDbo.getMetaClass() == rightDbo
                .getMetaClass());
        DbRelationN leftVals = leftDbo.getUdfValues();
        DbRelationN rightVals = rightDbo.getUdfValues();
        int i;
        // First get a list of all UDF's having a value in <rightDbo>
        int nb = rightVals.size();
        DbUDF[] rightUdfs = new DbUDF[nb];
        for (i = 0; i < nb; i++) {
            rightUdfs[i] = (DbUDF) rightVals.elementAt(i).getComposite();
        }
        // Scan all the UDF's having a value in <leftDbo>; for each one, find if <rightDbo> has a value for this UDF;
        // if found in right UDF's list, remove from right list and compare the values; add to the property list if values differ.
        // If not found, add to the property list the UDF  as defined only in the left.
        for (i = 0, nb = leftVals.size(); i < nb; i++) {
            DbUDFValue leftVal = (DbUDFValue) leftVals.elementAt(i);
            DbUDF udf = (DbUDF) leftVal.getComposite();
            if (!sameUdf)
                udf = rightUdfMap.find(rightDbo.getMetaClass(), udf.getName(), udf.getValueType());
            DbUDFValue rightVal = null;
            if (udf != null) {
                for (int j = 0; j < rightUdfs.length; j++) {
                    if (udf == rightUdfs[j]) {
                        rightVal = (DbUDFValue) rightVals.elementAt(j);
                        rightUdfs[j] = null; // mark as matched
                        break;
                    }
                }
            }
            if (rightVal == null || !leftVal.getValue().equals(rightVal.getValue())) {
                udf = (DbUDF) leftVal.getComposite();
                String leftStr = leftVal.getValue().toString();
                String rightStr = (rightVal != null ? rightVal.getValue().toString() : null);
                propList.add(new IntegrateProperty(udf, udf.getName(), leftStr, rightStr, action));
            }
        }
        // Finally, add to the property list all remaining UDF's in the right list (UDF's defined only in the right)
        for (i = 0; i < rightUdfs.length; i++) {
            if (rightUdfs[i] != null) {
                DbUDFValue rightVal = (DbUDFValue) rightVals.elementAt(i);
                String rightStr = rightVal.getValue().toString();
                propList.add(new IntegrateProperty(rightUdfs[i], rightUdfs[i].getName(), null,
                        rightStr, action));
            }
        }
    }

    private CheckTreeNode findClassNode(MetaClass metaClass) {
        int nb = fieldTree.getChildCount();
        for (int i = 0; i < nb; i++) {
            CheckTreeNode classNode = (CheckTreeNode) fieldTree.getChildAt(i);
            MetaClass curClass = (MetaClass) classNode.getUserObject();
            if (curClass.getJClass().isAssignableFrom(metaClass.getJClass()))
                return classNode;
        }
        return null;
    }

    /*
     * <node> represents an unassociated object; present to the user a list of compatible
     * unassociated objects from the associated parent and let him choose one. If user choose one,
     * associate the objects: replace the 2 unassociated nodes by an associated node, then load the
     * complete node hierarchy from this node.
     */
    public final void doMatch(IntegrateNode node) {
        try {
            leftModel.getDb().beginTrans(Db.READ_TRANS);
            rightModel.getDb().beginTrans(Db.READ_TRANS);
            ArrayList matchNodes = new ArrayList();
            IntegrateNode groupNode = (IntegrateNode) node.getParent();
            int nb = groupNode.getChildCount();
            for (int i = 0; i < nb; i++) {
                IntegrateNode matchNode = (IntegrateNode) groupNode.getChildAt(i);
                if (matchNode.isGroup())
                    continue;
                if (node.getLeftDbo() != null) {
                    if (matchNode.getLeftDbo() != null
                            || !isMatchable(node.getLeftDbo(), matchNode.getRightDbo()))
                        continue;
                } else {
                    if (matchNode.getRightDbo() != null
                            || !isMatchable(matchNode.getLeftDbo(), node.getRightDbo()))
                        continue;
                }
                matchNodes.add(matchNode);
            }
            leftModel.getDb().commitTrans();
            rightModel.getDb().commitTrans();

            int index = LookupDialog.selectOne(frame,
                    LocaleMgr.screen.getString("selectAssociate"), null, matchNodes.toArray(), -1,
                    new CollationComparator());
            if (index == -1)
                return;

            leftModel.getDb().beginTrans(Db.WRITE_TRANS, matchActionName);
            rightModel.getDb().beginTrans(Db.WRITE_TRANS, matchActionName);

            IntegrateNode matchNode = (IntegrateNode) matchNodes.get(index);
            if (node.getLeftDbo() != null)
                match(node, matchNode);
            else
                match(matchNode, node);

            leftModel.getDb().commitTrans();
            rightModel.getDb().commitTrans();
        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(frame, e);
        }
    }

    // Associate 2 unassociated nodes and replace them by a single node, then load the node hierarchy from this node.
    protected final void matchAux(IntegrateNode leftNode, IntegrateNode rightNode)
            throws DbException {
        DbObject leftDbo = leftNode.getLeftDbo();
        DbObject rightDbo = rightNode.getRightDbo();
        removeNodeFromParent(rightNode);
        leftNode.setRightDbo(rightDbo, rightNode.getRightName());
        leftDbo.setMatchingObject(rightDbo);
        postMatch(leftNode);
        leftNode.setAllowsChildren(allowsChildren(leftDbo));
        IntegrateNode groupNode = (IntegrateNode) leftNode.getParent();
        leftNode.setAction(getDefaultAction(leftNode, groupNode.getAction()));
        loadChildren(leftNode, false);
        buildProperties(leftNode);
        nodeChanged(leftNode);
        frame.refreshProperties(leftNode);

        if (!leftNode.isDifferent())
            propagDifferent(groupNode, false);
    }

    // Replace an associated node by 2 unassociated nodes; the node hierarchy under the removed node is automatically removed.
    public final void doUnmatch(IntegrateNode node) {
        try {
            preUnmatch(node); // may perform a write transaction.

            leftModel.getDb().beginTrans(Db.READ_TRANS);
            rightModel.getDb().beginTrans(Db.READ_TRANS);
            unmatch(node);
            leftModel.getDb().commitTrans();
            rightModel.getDb().commitTrans();
        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(frame, e);
        }
    }

    // Replace an associated node by 2 unassociated nodes; the node hierarchy under the removed node is automatically removed.
    // In the matchingObject facility, break association on all associated objects in the node hierarchy
    protected final void unmatchAux(IntegrateNode node) throws DbException {
        propagUnmatch(node);

        IntegrateNode groupNode = (IntegrateNode) node.getParent();
        IntegrateNode parentNode = (groupNode.isGroup() ? (IntegrateNode) groupNode.getParent()
                : groupNode);
        removeNodeFromParent(node);

        IntegrateNode childNode = createLeftNode(parentNode.getLeftDbo(), node.getLeftDbo());
        childNode.setClassNode(node.getClassNode());
        childNode.setAllowsChildren(false);
        childNode.setAction(getDefaultAction(childNode, node.getAction()));
        childNode.setDifferent(true);
        int index = binarySearch(groupNode, childNode);
        if (index < 0)
            index = -(index + 1);
        insertNodeInto(childNode, groupNode, index);

        childNode = createRightNode(parentNode.getRightDbo(), node.getRightDbo());
        childNode.setClassNode(node.getClassNode());
        childNode.setAllowsChildren(false);
        childNode.setAction(getDefaultAction(childNode, node.getAction()));
        childNode.setDifferent(true);
        index = binarySearch(groupNode, childNode);
        if (index < 0)
            index = -(index + 1);
        insertNodeInto(childNode, groupNode, index);

        propagDifferent(groupNode, true);
    }

    // In the matchingObject facility, break association on all associated objects in the node hierarchy
    private void propagUnmatch(IntegrateNode node) {
        if (node.getLeftDbo() != null && node.getRightDbo() != null)
            node.getLeftDbo().setMatchingObject(null);

        int nb = node.getChildCount();
        for (int i = 0; i < nb; i++)
            propagUnmatch((IntegrateNode) node.getChildAt(i));
    }

    // Rebuild property list for all nodes int <node> hierarchy.
    // This action is triggered by the user to recompare relation properties that may have become
    // equal or different after associating or unassociating objects.
    public final void refreshProperties(IntegrateNode node) {
        try {
            leftModel.getDb().beginTrans(Db.READ_TRANS);
            rightModel.getDb().beginTrans(Db.READ_TRANS);

            if (node == null)
                node = (IntegrateNode) getRoot();

            boolean oldDifferent = node.isDifferent();
            buildProperties(node);
            if (oldDifferent != node.isDifferent() && node != getRoot())
                propagDifferent((IntegrateNode) node.getParent(), node.isDifferent());
            rowChanged(node, true);
            frame.refreshProperties(null);

            leftModel.getDb().commitTrans();
            rightModel.getDb().commitTrans();
        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(frame, e);
        }
    }

    private void propagDifferent(IntegrateNode node, boolean isDifferent) {
        if (isDifferent) {
            if (node.isDifferent())
                return;
        } else {
            if (node.getProperties() != null)
                return;
            int nb = node.getChildCount();
            for (int i = 0; i < nb; i++) {
                if (((IntegrateNode) node.getChildAt(i)).isDifferent())
                    return;
            }
        }
        node.setDifferent(isDifferent);
        rowChanged(node, false);
        if (node != getRoot())
            propagDifferent((IntegrateNode) node.getParent(), isDifferent);
    }

    public final IntegrateNode findNode(IntegrateNode parentNode, DbObject dbo, boolean isRight)
            throws DbException {
        IntegrateNode childNode = (isRight ? createRightNode(parentNode.getRightDbo(), dbo)
                : createLeftNode(parentNode.getLeftDbo(), dbo));
        return findNode(parentNode, childNode);
    }

    public final IntegrateNode findNode(IntegrateNode parentNode, IntegrateNode childNode) {
        int index;
        GroupParams groupParams = childNode.getGroupParams();
        if (groupParams.name != null) {
            // Node within a group, find first the group node
            IntegrateNode groupNode = new IntegrateNode(groupParams);
            index = binarySearch(parentNode, groupNode);
            if (index < 0)
                return null;
            parentNode = (IntegrateNode) parentNode.getChildAt(index);
        }
        index = binarySearch(parentNode, childNode);
        if (index < 0)
            return null;
        // Begin with the first node having the searched name and loop until encountering a different name
        while (true) {
            IntegrateNode node = (IntegrateNode) parentNode.getChildAt(index);
            if (childNode.compareTo(node) != 0)
                return null;
            if (childNode.getLeftDbo() != null ? childNode.getLeftDbo() == node.getLeftDbo()
                    : childNode.getRightDbo() == node.getRightDbo())
                return node;
            if (++index == parentNode.getChildCount())
                return null;
        }
    }

    /**
     * Returns the index of the first element if more than one are found. If none is found, returns
     * a negative result: the expression -(result+1) gives the index where to insert the element.
     */
    private int binarySearch(IntegrateNode parentNode, IntegrateNode childNode) {
        boolean found = false;
        int lo = -1;
        int hi = parentNode.getChildCount();
        while (lo + 1 != hi) {
            int mid = (lo + hi) / 2;
            IntegrateNode node = (IntegrateNode) parentNode.getChildAt(mid);
            int cmp = childNode.compareTo(node);
            if (cmp <= 0) {
                if (cmp == 0)
                    found = true;
                hi = mid;
            } else
                lo = mid;
        }
        return (found ? hi : -(hi + 1));
    }

    // Reduce the action to one of 3 basic actions: ACT_NONE, ACT_MODIFY_LEFT, ACT_MODIFY_RIGHT
    public final int getBasicAction(int action) {
        if (action == ACT_NONE)
            return ACT_NONE;
        return (action > ACT_LAST_LEFT ? ACT_MODIFY_RIGHT : ACT_MODIFY_LEFT);
    }

    public final int[] getPossibleActions(IntegrateNode[] nodes) {
        int[] actions = getPossibleActions(nodes[0]);
        for (int i = 1; i < nodes.length; i++) {
            if (actions != getPossibleActions(nodes[i])) {
                actions = noActions;
                break;
            }
        }
        return actions;
    }

    private int[] getPossibleActions(IntegrateNode node) {
        if (node.isGroup() || node.getAllowsChildren())
            return combineActions;
        if (node.getClassNode() == null || !node.getClassNode().isSelected())
            return noActions;
        if (node.getLeftDbo() == null)
            return rightActions;
        if (node.getRightDbo() == null)
            return leftActions;
        return propertyActions;
    }

    // Deduce a default action from the parent action.
    private int getDefaultAction(IntegrateNode node, int parentAction) {
        int action = ACT_NONE;
        if (node.isGroup() || node.getAllowsChildren()) {
            action = parentAction;
        } else if (node.getClassNode() != null && node.getClassNode().isSelected()) {
            if (node.getLeftDbo() == null) {
                if (parentAction == ACT_MERGE_IN_LEFT || parentAction == ACT_REPLACE_LEFT)
                    action = ACT_ADD_IN_LEFT;
                else if (parentAction == ACT_REPLACE_RIGHT)
                    action = ACT_DELETE_RIGHT;
            } else if (node.getRightDbo() == null) {
                if (parentAction == ACT_MERGE_IN_RIGHT || parentAction == ACT_REPLACE_RIGHT)
                    action = ACT_ADD_IN_RIGHT;
                else if (parentAction == ACT_REPLACE_LEFT)
                    action = ACT_DELETE_LEFT;
            } else
                action = getBasicAction(parentAction);
        }
        return action;
    }

    // Change the action of the node and of its properties, then propagate action change to the complete node hierarchy.
    // Recursive method.
    public final void propagAction(IntegrateNode node, int action) {
        if (action == node.getAction())
            return;
        IntegrateProperty[] props = node.getProperties();
        if (props != null && props.length != 0) {
            int propAction = getBasicAction(action);
            if (propAction != getBasicAction(node.getAction())) {
                for (int i = 0; i < props.length; i++)
                    props[i].setAction(propAction);
                frame.refreshProperties(node);
            }
        }
        node.setAction(action);
        rowChanged(node, false);

        int nb = node.getChildCount();
        for (int i = 0; i < nb; i++) {
            IntegrateNode childNode = (IntegrateNode) node.getChildAt(i);
            propagAction(childNode, getDefaultAction(childNode, action));
        }
    }

    // If only cells are changed, use this instead of nodeChanged; nodeChanged causes a collapsing of the node
    private void rowChanged(IntegrateNode node, boolean inclChildren) {
        IntegrateView treeTable = frame.getTreeTable();
        int row = treeTable.getTree().getRowForPath(new TreePath(node.getPath()));
        if (row != -1)
            ((TreeTableModelAdapter) treeTable.getModel()).fireTableRowsUpdated(row,
                    (inclChildren ? Integer.MAX_VALUE : row));
    }

    //////////////////////////////////////
    // TreeTableModel SUPPORT
    //
    public final int getColumnCount() {
        return 3;
    }

    public final String getColumnName(int col) {
        return colHeaders[col];
    }

    public final Class getColumnClass(int col) {
        switch (col) {
        case IntegrateModel.COL_LEFT:
            return TreeTableModel.class;
        default:
            return String.class;
        }
    }

    public final Object getValueAt(Object node, int col) {
        switch (col) {
        case IntegrateModel.COL_LEFT:
            return this;
        case IntegrateModel.COL_RIGHT:
            return ((IntegrateNode) node).getRightName();

        default:
            IntegrateNode theNode = (IntegrateNode) node;
            if (!theNode.isDifferent())
                return "";
            int action = theNode.getAction();
            return (action == ACT_NONE ? "" : actionNames[action]);
        }
    }

    public final boolean isCellEditable(Object node, int col) {
        return (col == COL_LEFT);
    }

    public final void setValueAt(Object value, Object node, int col) {
    }

    //
    // End of TreeTableModel SUPPORT
    //////////////////////////////////////

    public final void doReport() {
        try {
            leftModel.getDb().beginTrans(Db.READ_TRANS);
            rightModel.getDb().beginTrans(Db.READ_TRANS);
            StringBuffer reportBuffer = new StringBuffer(1000);
            report(reportBuffer, false);
            leftModel.getDb().commitTrans();
            rightModel.getDb().commitTrans();

            TextViewerDialog.showTextDialog(frame, frameTitle, reportBuffer.toString());
        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(frame, e);
        }
    }

    // If <inInteg> is true, integration is done and not yet committed (transaction still opened).
    // In this case, we can take new values from the database; otherwise we take all the data from the nodes.
    private void report(StringBuffer reportBuffer, boolean inInteg) throws DbException {
        reportHeader(reportBuffer);
        if (((IntegrateNode) getRoot()).isDifferent()) {
            reportDifferences(reportBuffer);
            reportModifications(reportBuffer, ACT_MODIFY_LEFT, inInteg);
            reportModifications(reportBuffer, ACT_MODIFY_RIGHT, (inInteg && !noRightUpdate));
        }
    }

    private void reportDifferences(StringBuffer reportBuffer) {
        reportBuffer.append(LocaleMgr.screen.getString("listDiff") + "\n\n"); //NOT LOCALIZABLE
        reportBuffer.append(valSep + propHeaders[PROP_NAME] + valSep + propHeaders[PROP_LEFT]
                + valSep + propHeaders[PROP_RIGHT] + "\n\n"); //NOT LOCALIZABLE
        reportDiffChildren(reportBuffer, (IntegrateNode) getRoot(), "");
    }

    private void reportDiffChildren(StringBuffer reportBuffer, IntegrateNode parentNode,
            String indent) {
        if (parentNode == getRoot() && allowRootPropertiesCompare(parentNode.getLeftDbo())) {
            reportBuffer.append(getObjectReportStr(parentNode, indent, ACT_MODIFY_LEFT));
            IntegrateProperty[] props = parentNode.getProperties();
            if (props != null)
                reportBuffer.append(' ' + LocaleMgr.screen.getString("modified")); //NOT LOCALIZABLE
            reportBuffer.append('\n'); //NOT LOCALIZABLE
            if (props != null) {
                for (int j = 0; j < props.length; j++) {
                    reportBuffer.append(valSep + props[j].getName() + valSep
                            + getValueReportStr(props[j].getLeftVal()) + valSep
                            + getValueReportStr(props[j].getRightVal()) + '\n'); //NOT LOCALIZABLE
                }
            }
        }
        int nb = parentNode.getChildCount();
        for (int i = 0; i < nb; i++) {
            IntegrateNode node = (IntegrateNode) parentNode.getChildAt(i);
            if (!node.isDifferent())
                continue;
            if (node.isGroup()) {
                reportDiffChildren(reportBuffer, node, indent);
            } else if (node.getLeftDbo() == null || node.getRightDbo() == null) {
                String suffix = MessageFormat
                        .format(LocaleMgr.screen.getString("onlyIn0"),
                                new Object[] { colHeaders[node.getRightDbo() == null ? COL_LEFT
                                        : COL_RIGHT] });
                reportBuffer.append(getObjectReportStr(node, indent, ACT_MODIFY_LEFT) + ' '
                        + suffix.toLowerCase() + '\n'); //NOT LOCALIZABLE
            } else {
                reportBuffer.append(getObjectReportStr(node, indent, ACT_MODIFY_LEFT));
                IntegrateProperty[] props = node.getProperties();
                if (props != null)
                    reportBuffer.append(' ' + LocaleMgr.screen.getString("modified")); //NOT LOCALIZABLE
                reportBuffer.append('\n'); //NOT LOCALIZABLE
                if (props != null) {
                    for (int j = 0; j < props.length; j++) {
                        reportBuffer.append(valSep + props[j].getName() + valSep
                                + getValueReportStr(props[j].getLeftVal()) + valSep
                                + getValueReportStr(props[j].getRightVal()) + '\n'); //NOT LOCALIZABLE
                    }
                }
                reportDiffChildren(reportBuffer, node, indent + levelStr);
            }
        }
    }

    private void reportModifications(StringBuffer reportBuffer, int action, boolean inInteg)
            throws DbException {
        String prefix = '\n'
                + //NOT LOCALIZABLE
                MessageFormat
                        .format(LocaleMgr.screen.getString("modifsIn0"),
                                new Object[] { colHeaders[action == ACT_MODIFY_LEFT ? COL_LEFT
                                        : COL_RIGHT] }) + "\n\n"
                + //NOT LOCALIZABLE
                valSep + propHeaders[PROP_NAME] + valSep + LocaleMgr.screen.getString("newValue")
                + valSep + LocaleMgr.screen.getString("oldValue") + "\n\n"; //NOT LOCALIZABLE
        reportModifChildren(reportBuffer, (IntegrateNode) getRoot(), prefix, "", action, inInteg);
    }

    private boolean reportModifChildren(StringBuffer reportBuffer, IntegrateNode parentNode,
            String prefix, String indent, int action, boolean inInteg) throws DbException {
        int nb = parentNode.getChildCount();
        for (int i = 0; i < nb; i++) {
            IntegrateNode node = (IntegrateNode) parentNode.getChildAt(i);
            if (!node.isDifferent())
                continue;
            if (node.isGroup()) {
                if (reportModifChildren(reportBuffer, node, prefix, indent, action, inInteg))
                    prefix = "";
            } else if (node.getLeftDbo() == null || node.getRightDbo() == null) {
                if (action != getBasicAction(node.getAction()))
                    continue;
                String actName = (node.getAction() == ACT_ADD_IN_LEFT
                        || node.getAction() == ACT_ADD_IN_RIGHT ? LocaleMgr.screen
                        .getString("added") : LocaleMgr.screen.getString("deleted"));
                reportBuffer.append(prefix + getObjectReportStr(node, indent, action) + ' '
                        + actName + '\n'); //NOT LOCALIZABLE
                prefix = "";
            } else {
                String downPrefix;
                if (reportModifNode(reportBuffer, node, prefix, indent, action, inInteg))
                    downPrefix = prefix = "";
                else
                    downPrefix = prefix + getObjectReportStr(node, indent, action) + '\n'; //NOT LOCALIZABLE

                if (reportModifChildren(reportBuffer, node, downPrefix, indent + levelStr, action,
                        inInteg))
                    prefix = "";
            }
        }
        return (prefix.length() == 0);
    }

    private boolean reportModifNode(StringBuffer reportBuffer, IntegrateNode node, String prefix,
            String indent, int action, boolean inInteg) throws DbException {
        boolean hasModif = false;
        IntegrateProperty[] props = node.getProperties();
        if (props != null) {
            for (int i = 0; i < props.length; i++) {
                if (action != props[i].getAction())
                    continue;
                String oldVal, newVal;
                if (action == ACT_MODIFY_LEFT) {
                    oldVal = props[i].getLeftVal();
                    newVal = props[i].getRightVal();
                } else {
                    oldVal = props[i].getRightVal();
                    newVal = props[i].getLeftVal();
                }
                if (inInteg) { // if integration done, recalculate the string <newVal> from actual values in Db (only for relations).
                    DbObject dbo = (action == ACT_MODIFY_LEFT ? node.getLeftDbo() : node
                            .getRightDbo());
                    Object property = props[i].getProperty();
                    if (property instanceof MetaClass)
                        newVal = getPropertyString((MetaClass) property, getRelNProperty(dbo,
                                (MetaClass) property));
                    else if (property instanceof MetaRelationN)
                        newVal = getPropertyString((MetaRelationN) property, getRelNProperty(dbo,
                                (MetaRelationN) property));
                    else if (property instanceof MetaRelationship) {
                        if (!dbo.hasChanged((MetaRelationship) property))
                            continue;
                        dbo = (DbObject) dbo.get((MetaRelationship) property);
                        newVal = (dbo == null ? null : getName(dbo));
                    }
                }
                if (!hasModif) {
                    reportBuffer.append(prefix + getObjectReportStr(node, indent, action) + ' '
                            + LocaleMgr.screen.getString("modified") + '\n'); //NOT LOCALIZABLE
                    hasModif = true;
                }
                reportBuffer.append(valSep + props[i].getName() + valSep
                        + getValueReportStr(newVal) + valSep + getValueReportStr(oldVal) + '\n'); //NOT LOCALIZABLE
            }
        }
        return hasModif;
    }

    private String getObjectReportStr(IntegrateNode node, String indent, int action) {
        DbObject dbo = (node.getLeftDbo() != null ? node.getLeftDbo() : node.getRightDbo());
        String className = dbo.getMetaClass().getGUIName(false, false);
        String name = (action == ACT_MODIFY_LEFT ? node.getLeftName() : node.getRightName());
        return indent
                + MessageFormat.format(LocaleMgr.screen.getString("01"), new Object[] { className,
                        name });
    }

    private String getValueReportStr(String str) {
        if (str == null)
            return "";
        int i = str.indexOf('\n');
        return (i == -1 ? str : str.substring(0, i));
    }

    /*
     * Perform integration. Order of operations is important: perform deletes first, then adds,
     * finally property updates. Property updates must be done after adds, to be able to resolve
     * relations pointing to added objects.
     */
    public final void integrate() {
        try {
            leftModel.getDb().beginTrans(Db.WRITE_TRANS, integActionName);
            rightModel.getDb().beginTrans(Db.WRITE_TRANS, integActionName);

            integrateAux(false);
            StringBuffer reportBuffer = new StringBuffer(1000);
            report(reportBuffer, true);

            leftModel.getDb().commitTrans();
            rightModel.getDb().commitTrans();

            postIntegrate(reportBuffer);

            TextViewerDialog.showTextDialog(frame, frameTitle, reportBuffer.toString());
        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(frame, e);
        }
    }

    protected final void integrateAux(boolean leftIfNone) throws DbException {
        initCopyCustom();
        doDelete((IntegrateNode) getRoot(), leftIfNone);
        doAdd(leftIfNone);
        integrateProperties((IntegrateNode) getRoot(), leftIfNone);
        leftCopyCustom = rightCopyCustom = null; // gcol
    }

    // Recursive method: perform delete action on the complete hierarchy of <node>.
    private void doDelete(IntegrateNode node, boolean leftIfNone) throws DbException {
        if (leftIfNone) {
            if (node.getAction() == ACT_NONE && !node.isGroup() && node.getRightDbo() == null)
                delete(node.getLeftDbo());
        } else if (node.getAction() == ACT_DELETE_LEFT)
            delete(node.getLeftDbo());
        else if (node.getAction() == ACT_DELETE_RIGHT && !noRightUpdate)
            delete(node.getRightDbo());

        int nb = node.getChildCount();
        for (int i = 0; i < nb; i++)
            doDelete((IntegrateNode) node.getChildAt(i), leftIfNone);
    }

    // Perform add action on left and right models, by using the deepCopy facility.
    private void doAdd(boolean leftIfNone) throws DbException {
        DeepCopy leftDeepCopy = new DeepCopy(rightModel.getProject(), leftModel.getProject(),
                leftCopyCustom);
        leftDeepCopy.setUdfMap(leftUdfMap);
        DeepCopy rightDeepCopy = null;
        if (!noRightUpdate) {
            rightDeepCopy = new DeepCopy(leftModel.getProject(), rightModel.getProject(),
                    rightCopyCustom);
            rightDeepCopy.setUdfMap(rightUdfMap);
        }
        // Create all objects whose action is Add, in both models
        doAddAux((IntegrateNode) getRoot(), leftDeepCopy, rightDeepCopy, leftIfNone);
        // Now copy relations on all added objects.
        leftDeepCopy.fill();
        if (!noRightUpdate)
            rightDeepCopy.fill();
    }

    // Recursive method: perform add action on the complete hierarchy of <node>.
    // Use leftDeepCopy for adds in left model, rightDeepCopy for adds in right model
    private void doAddAux(IntegrateNode node, DeepCopy leftDeepCopy, DeepCopy rightDeepCopy,
            boolean leftIfNone) throws DbException {
        if (leftIfNone) {
            if (node.getAction() == ACT_NONE && !node.isGroup() && node.getLeftDbo() == null)
                add(leftDeepCopy, node.getRightDbo());
        } else if (node.getAction() == ACT_ADD_IN_LEFT) {
            DbObject srcObj = node.getRightDbo();
            if (srcObj.getTransStatus() != Db.OBJ_REMOVED) // insure not removed by the delete action
                add(leftDeepCopy, srcObj);
        } else if (node.getAction() == ACT_ADD_IN_RIGHT && !noRightUpdate) {
            DbObject srcObj = node.getLeftDbo();
            if (srcObj.getTransStatus() != Db.OBJ_REMOVED) // insure not removed by the delete action
                add(rightDeepCopy, srcObj);
        }

        int nb = node.getChildCount();
        for (int i = 0; i < nb; i++)
            doAddAux((IntegrateNode) node.getChildAt(i), leftDeepCopy, rightDeepCopy, leftIfNone);
    }

    // Recursive method: modify properties whose action is Modify, on the complete hierarchy of <node>.
    private void integrateProperties(IntegrateNode node, boolean leftIfNone) throws DbException {
        IntegrateProperty[] props = node.getProperties();
        if (props != null) {
            for (int i = 0; i < props.length; i++) {
                int action = props[i].getAction();
                if (leftIfNone) {
                    if (action != ACT_NONE)
                        continue;
                    action = ACT_MODIFY_LEFT;
                } else {
                    if (action == ACT_NONE)
                        continue;
                    if (action == ACT_MODIFY_RIGHT && noRightUpdate)
                        continue;
                }
                integrateProperty(node.getLeftDbo(), node.getRightDbo(), props[i].getProperty(),
                        action);
            }
        }
        int nb = node.getChildCount();
        for (int i = 0; i < nb; i++)
            integrateProperties((IntegrateNode) node.getChildAt(i), leftIfNone);
    }

    // Modify a single property.
    private void integrateProperty(DbObject leftDbo, DbObject rightDbo, Object property, int action)
            throws DbException {
        DbObject sourceDbo, targetDbo;
        if (action == ACT_MODIFY_RIGHT) {
            sourceDbo = leftDbo;
            targetDbo = rightDbo;
        } else {
            sourceDbo = rightDbo;
            targetDbo = leftDbo;
        }

        if (property instanceof MetaField[]) {// if navigation path, navigate to the last dbObject of the path.
            MetaField[] fields = (MetaField[]) property;
            for (int i = 0; i < fields.length - 1; i++) {
                sourceDbo = (DbObject) sourceDbo.get(fields[i]);
                if (sourceDbo == null)
                    return;
                targetDbo = (DbObject) targetDbo.get(fields[i]);
                if (targetDbo == null)
                    return;
            }
            property = fields[fields.length - 1];
        }

        // BEWARE: order of instanceof's important because MetaRelationN is subclass of MetaRelationship, itself subclass of MetaField
        if (property instanceof MetaClass) {
            setRelationN(sourceDbo, targetDbo, (MetaClass) property, action);
        } else if (property instanceof MetaRelationN) {
            setRelationN(sourceDbo, targetDbo, (MetaRelationN) property, action);
        } else if (property instanceof MetaRelationship) { // MetaRelation1 or MetaChoice
            setRelation(sourceDbo, targetDbo, (MetaRelationship) property, action);
        } else if (property instanceof MetaField) {
            MetaField field = (MetaField) property;
            targetDbo.set(field, sourceDbo.get(field));
        } else if (property instanceof String) { // custom
            integrateCustomProperty(sourceDbo, targetDbo, (String) property);
        } else { // DbUDF
            DbUDF sourceUdf = (DbUDF) property;
            DbUDF targetUdf = sourceUdf;
            if (sourceDbo.getProject() != targetDbo.getProject()
                    || sourceDbo.getMetaClass() != targetDbo.getMetaClass()) {
                // Not the same UDF for source and target (different project or different class)
                String udfName = sourceUdf.getName();
                UDFValueType udfType = sourceUdf.getValueType();
                if (action == ACT_MODIFY_RIGHT) {
                    sourceUdf = leftUdfMap.find(sourceDbo.getMetaClass(), udfName, udfType);
                    targetUdf = rightUdfMap.findAdd(targetDbo.getMetaClass(), udfName, udfType);
                } else { // invert UdfMaps
                    sourceUdf = rightUdfMap.find(sourceDbo.getMetaClass(), udfName, udfType);
                    targetUdf = leftUdfMap.findAdd(targetDbo.getMetaClass(), udfName, udfType);
                }
            }
            Object value = (sourceUdf != null ? sourceDbo.get(sourceUdf) : null);
            targetDbo.set(targetUdf, value);
        }
    }

    // update the targetDbo value with sourceDbo value
    protected void integrateCustomProperty(DbObject sourceDbo, DbObject targetDbo, String property)
            throws DbException {
    }

    // Following methods are overridden by the application to customize the integration.

    protected int getRootDefaultAction() throws DbException {
        return ACT_MERGE_IN_RIGHT;
    }

    protected boolean isMatchable(DbObject dbo1, DbObject dbo2) throws DbException {
        return (dbo1.getMetaClass() == dbo2.getMetaClass());
    }

    // Return true if pre-match is complete, i.e. loadChildren does not attempt additionnal matching.
    protected boolean preMatch(IntegrateNode parentNode, boolean loading) throws DbException {
        return false;
    }

    protected void postMatch(IntegrateNode node) throws DbException {
    }

    protected void match(IntegrateNode leftNode, IntegrateNode rightNode) throws DbException {
        matchAux(leftNode, rightNode);
    }

    protected boolean isUnmatchable(IntegrateNode node) {
        return true;
    }

    // No transaction started
    protected void preUnmatch(IntegrateNode node) throws DbException {
    }

    protected void unmatch(IntegrateNode node) throws DbException {
        unmatchAux(node);
    }

    protected boolean allowsChildren(DbObject dbo) throws DbException {
        return true;
    }

    protected ArrayList getChildren(DbObject parent) throws DbException {
        ArrayList children = new ArrayList();
        DbEnumeration dbEnum = parent.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            children.add(dbEnum.nextElement());
        }
        dbEnum.close();
        return children;
    }

    protected String getDisplayName(DbObject dbo) throws DbException {
        return getName(dbo);
    }

    protected GroupParams getGroupParams(DbObject parent, DbObject child) throws DbException {
        return GroupParams.defaultGroupParams;
    }

    protected void initCopyCustom() throws DbException {
        leftCopyCustom = rightCopyCustom = null;
    }

    protected boolean equalProperty(MetaRelationship metaRel, DbObject leftVal, DbObject rightVal)
            throws DbException {
        if (leftVal == rightVal)
            return true;
        if (externalMatchByName
                && (metaRel instanceof MetaRelation1 || metaRel instanceof MetaChoice)
                && ((metaRel.getFlags() & MetaField.INTEGRABLE_BY_NAME) != 0) && leftVal != null
                && rightVal != null) {
            if ((leftVal.getMatchingObject() == null && rightVal.getMatchingObject() == null)
                    || (leftVal == leftVal.getMatchingObject())) {
                String leftName = getNameForMatchByName(leftVal);
                String rightName = getNameForMatchByName(rightVal);
                return equalsName(leftName, rightName);
            }
        }
        if (leftVal == null || rightVal == null)
            return false;
        return (leftVal.findMatchingObject() == rightVal);
    }

    // Define the name for matching with option externalMatchByName
    protected String getNameForMatchByName(DbObject obj) throws DbException {
        return getName(obj);
    }

    protected boolean equalProperty(MetaRelationN metaRelN, DbObject[] leftVal, DbObject[] rightVal)
            throws DbException {
        if (leftVal.length != rightVal.length)
            return false;
        for (int i = 0; i < leftVal.length; i++) {
            if (externalMatchByName
                    && ((metaRelN.getFlags() & MetaField.INTEGRABLE_BY_NAME) != 0)
                    && leftVal[i] != null
                    && rightVal[i] != null
                    && ((leftVal[i].getMatchingObject() == null && rightVal[i].getMatchingObject() == null) || (leftVal[i] == leftVal[i]
                            .getMatchingObject()))) {
                String leftName = getNameForMatchByName(leftVal[i]);
                String rightName = getNameForMatchByName(rightVal[i]);
                if (equalsName(leftName, rightName))
                    continue;
            }
            if (leftVal[i] != rightVal[i] && leftVal[i].findMatchingObject() != rightVal[i])
                return false;
        }
        return true;
    }

    // No default implementation for intersection table case
    protected boolean equalProperty(MetaClass metaClass, DbObject[] leftVal, DbObject[] rightVal)
            throws DbException {
        return false;
    }

    // We've already checked that the values are different.
    protected boolean isEquivalent(MetaField field, DbObject leftDbo, Object leftVal,
            DbObject rightDbo, Object rightVal) throws DbException {
        return false;
    }

    protected String getPropertyString(MetaRelationN metaRelN, DbObject[] value) throws DbException {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < value.length; i++) {
            if (i > 0)
                buffer.append(", ");
            buffer.append(getName(value[i]));
        }
        return buffer.toString();
    }

    // No default implementation for intersection table case
    protected String getPropertyString(MetaClass metaClass, DbObject[] value) throws DbException {
        return null;
    }

    protected void delete(DbObject dbo) throws DbException {
        dbo.remove();
    }

    protected void add(DeepCopy deepCopy, DbObject srcObj) throws DbException {
        deepCopy.create(srcObj, srcObj.getComposite().getMatchingObject(), false);
    }

    protected void setRelation(DbObject sourceDbo, DbObject targetDbo, MetaRelationship metaRel,
            int action) throws DbException {
        DbObject neighbor = (DbObject) sourceDbo.get(metaRel);
        if (neighbor != null) {
            neighbor = neighbor.findMatchingObject();
            if (neighbor == null) // if neighbor does not exist in the target, do not change the value
                return;
            MetaRelationship oppRel = metaRel.getOppositeRel(neighbor);
            if (oppRel.getMaxCard() == 1)
                neighbor.set(oppRel, null); // if 1-1 relation, must disconnect neighbor before connecting it to targetDbo.
        }
        targetDbo.set(metaRel, neighbor);
    }

    protected void setRelationN(DbObject sourceDbo, DbObject targetDbo, MetaRelationN metaRelN,
            int action) throws DbException {
        // If fComponents, this is a reorderring of the components
        if (metaRelN == DbObject.fComponents) {
            DbRelationN sourceRelN = (DbRelationN) sourceDbo.get(DbObject.fComponents);
            DbRelationN targetRelN = (DbRelationN) targetDbo.get(DbObject.fComponents);
            DbEnumeration dbEnum = sourceRelN.elements();
            while (dbEnum.hasMoreElements()) {
                DbObject sourceComponent = dbEnum.nextElement();
                DbObject targetComponent = sourceComponent.getMatchingObject();
                if (targetComponent == null)
                    continue;
                int oldIndex = targetRelN.indexOf(targetComponent);
                int newIndex = sourceRelN.indexOf(sourceComponent);
                if (newIndex == -1 || oldIndex == -1)
                    continue;
                targetDbo.reinsert(metaRelN, oldIndex, newIndex);
            }
            dbEnum.close();
            return;
        }

        // First disconnect all neighbors from the target object.
        MetaRelationship oppRel = metaRelN.getOppositeRel(null);
        DbEnumeration dbEnum = ((DbRelationN) targetDbo.get(metaRelN)).elements();
        while (dbEnum.hasMoreElements()) {
            DbObject neighbor = dbEnum.nextElement();
            if (oppRel instanceof MetaRelationN)
                targetDbo.set(metaRelN, neighbor, Db.REMOVE_FROM_RELN);
            else
                neighbor.set(oppRel, null);
        }
        dbEnum.close();

        // Then reconnect to the target object all neighbors connected to the source object (in the same order)
        dbEnum = ((DbRelationN) sourceDbo.get(metaRelN)).elements();
        while (dbEnum.hasMoreElements()) {
            DbObject neighbor = dbEnum.nextElement().findMatchingObject();
            if (neighbor != null) { // if neighbor does not exist in the target, do not connect it
                if (oppRel instanceof MetaRelationN)
                    targetDbo.set(metaRelN, neighbor, Db.ADD_TO_RELN);
                else
                    neighbor.set(oppRel, targetDbo);
            }
        }
        dbEnum.close();
    }

    // No default implementation for intersection table case
    protected void setRelationN(DbObject sourceDbo, DbObject targetDbo, MetaClass metaClass,
            int action) throws DbException {
    }

    protected void reportHeader(StringBuffer reportBuffer) throws DbException {
    }

    // No transaction started
    protected void postIntegrate(StringBuffer reportBuffer) throws DbException {
    }
}
