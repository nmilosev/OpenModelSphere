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

package org.modelsphere.sms.or.features.dbms;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import org.modelsphere.jack.awt.tree.CheckTreeNode;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.plugins.PluginMgr;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.EditionCode;
import org.modelsphere.jack.srtool.forward.Rule;
import org.modelsphere.jack.srtool.forward.RuleException;
import org.modelsphere.jack.srtool.forward.VariableDecl;
import org.modelsphere.jack.srtool.forward.VariableScope;
import org.modelsphere.jack.srtool.integrate.IntegrateNode;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.TargetRuntimeException;
import org.modelsphere.sms.SMSSynchroModel;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.srtypes.ORValidationRule;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.templates.SQLForwardEngineeringPlugin;

public final class DefaultSynchroModel extends SMSSynchroModel {
    private static final String EOL = System.getProperty("line.separator");
    private static final String kDDLGenMessages = LocaleMgr.misc.getString("DDLGenMessages");
    private static final String kNotAdded01 = LocaleMgr.message.getString("NotAdded01");
    private static final String kNotModified01 = LocaleMgr.message.getString("NotModified01");
    private static final String kNotDeleted01 = LocaleMgr.message.getString("NotDeleted01");
    private static final String kNotRenamed01 = LocaleMgr.message.getString("NotRenamed01");
    private static final String kHeader = LocaleMgr.message.getString("syncHeader");
    private static final String kOnline0 = LocaleMgr.message.getString("online0");
    private static final String kNotLoaded = LocaleMgr.screen.getString("NotLoadedPattern");
    private static final String COMMENT_SEPARATOR = "***********************************************************";

    private static final String GENERATE_ON = LocaleMgr.misc.getString("generateOnForwardHeader");
    private static final String PLUGINS_PATTERN = LocaleMgr.misc.getString("PLUGINS_PATTERN");
    private static final String GENERATED_BY_PATTERN = LocaleMgr.misc
            .getString("GENERATED_BY_PATTERN");
    private static final String FOR_INFO_PATTERN = LocaleMgr.misc.getString("FOR_INFO_PATTERN");

    public static final int OPTION_DROP_TABLE_CASCADE_FK = 0x0001; // If a table is dropped, fk referencing this table wont be dropped
    // before dropping this table.
    public static final int OPTION_DROP_PK_UK_CASCADE_FK = 0x0002; // If a pk/uk is dropped, fk referencing this pk/uk wont be dropped
    // before dropping this pk/uk.
    public static final int OPTION_IGNORE_CREATE_PK_UK_INDEX = 0x0004; // Ignore Create statements for pk/uk indexes.
    public static final int OPTION_IGNORE_DROP_PK_UK_INDEX = 0x0008; // Ignore Drop statements for pk/uk indexes.

    private static SynchroObject tempSynchroObject = null;

    private DBMSReverseOptions options;
    private SQLForwardEngineeringPlugin generator;
    private StringBuffer report;
    private int genOptions = 0;
    private MetaRelationship[] dependencies = null;

    protected DefaultSynchroModel(DbObject physModel, DbObject refModel, CheckTreeNode fieldTree,
            boolean ignoreCase, boolean syncUser, DBMSReverseOptions options) throws DbException {
        super(physModel, refModel, fieldTree, ignoreCase, syncUser);
        this.options = options;

        // Init with toolkit
        ReverseToolkitPlugin toolkit = ReverseToolkitPlugin.getToolkit();
        Class[] tempOrder = toolkit.getGenClassOrder_Synchro(options);
        if (tempOrder != null)
            classOrder = tempOrder;
        genOptions = toolkit.getSynchroOptions(options);

        Class generatorClass = toolkit.getForwardPluginClass(options);
        if (generatorClass == null) {
            PluginSignature signature = toolkit.getSignature();
            String pluginName = signature.getName();
            String msg = MessageFormat.format(kNotLoaded, new Object[] { pluginName });
            throw new RuntimeException(msg);
        } //end if

        PluginMgr mgr = PluginMgr.getSingleInstance();
        ArrayList genlist = mgr.getPluginsRegistry().getActivePluginInstances(generatorClass);
        if (genlist.size() > 0)
            generator = (SQLForwardEngineeringPlugin) genlist.get(0);

        dependencies = ReverseToolkitPlugin.getToolkit().getDependencies_Synchro();
        if (dependencies == null)
            dependencies = new MetaRelationship[] {};

        if (generator != null) {
            generator.setTemplateCondition("GenObjNameWithOwner", options.genObjectWithUser); // NOT LOCALIZABLE
            generator.setTemplateCondition("Check", true); // NOT LOCALIZABLE
            generator.setTemplateCondition("PrimaryKey", true); // NOT LOCALIZABLE
            generator.setTemplateCondition("UniqueKey", true); // NOT LOCALIZABLE
            generator.setTemplateCondition("Index", true); // NOT LOCALIZABLE
        }
    }

    protected void genSyncObjs() throws DbException {
        ArrayList toGenObjs = new ArrayList();
        ArrayList addedGenObjs = new ArrayList();

        // Split actions to simple action - for modify
        ArrayList temp = new ArrayList();
        Iterator iter = syncObjs.iterator();
        while (iter.hasNext()) {
            SynchroObject synobj = (SynchroObject) iter.next();
            if (((synobj.action & SynchroObject.ACT_MODIFY) == 0)
                    || ((synobj.action & SynchroObject.ACT_RENAME) == 0)) {
                temp.add(synobj);
                continue;
            }
            SynchroObject modify = new SynchroObject(synobj.physDbo, synobj.refDbo,
                    SynchroObject.ACT_MODIFY, getSyncOrder(synobj.physDbo, synobj.refDbo,
                            SynchroObject.ACT_MODIFY), synobj.modifiedMetaFields);
            SynchroObject rename = new SynchroObject(synobj.physDbo, synobj.refDbo,
                    SynchroObject.ACT_RENAME, getSyncOrder(synobj.physDbo, synobj.refDbo,
                            SynchroObject.ACT_RENAME), synobj.modifiedMetaFields);
            temp.add(modify);
            temp.add(rename);
        }
        syncObjs = temp;

        // Replace modif on fComponents (sequence of components in the composite) with drop/create
        temp = new ArrayList();
        iter = syncObjs.iterator();
        while (iter.hasNext()) {
            SynchroObject synobj = (SynchroObject) iter.next();
            if ((synobj.action & SynchroObject.ACT_MODIFY) == 0) {
                temp.add(synobj);
                continue;
            }
            // Modif on fComponents represents sequence of the components in the composite.  Replace this one with a drop/create
            MetaField[] modifiedFields = synobj.modifiedMetaFields;
            ArrayList newModifiedFields = new ArrayList();
            boolean sequenceModified = false;
            if (modifiedFields != null) {
                for (int i = 0; i < modifiedFields.length; i++) {
                    if (modifiedFields[i] != DbObject.fComponents)
                        newModifiedFields.add(modifiedFields[i]);
                    else
                        sequenceModified = true;
                }
            }

            // Process custom FK Update Rules (opposite end of the FK association End)
            if (synobj.customFields != null) {
                for (int i = 0; i < synobj.customFields.length; i++) {
                    if (synobj.customFields[i].equals(FK_PARENT_DELETE_RULE))
                        newModifiedFields.add(DbORAssociationEnd.fDeleteRule);
                }
            }

            modifiedFields = new MetaField[newModifiedFields.size()];
            for (int i = 0; i < modifiedFields.length; i++)
                modifiedFields[i] = (MetaField) newModifiedFields.get(i);

            if (sequenceModified) { // add equivalent drop/create
                ArrayList equivalences = getEquivalentDropCreateFor(synobj.refDbo, synobj.physDbo);
                if (equivalences != null && equivalences.size() > 0) {
                    Iterator iterator = equivalences.iterator();
                    while (iterator.hasNext()) {
                        temp.add(iterator.next());
                    }
                }
            }

            // If no other fields are modified (other than fComponents), ignore the modify
            if (modifiedFields.length == 0)
                continue;

            temp.add(new SynchroObject(synobj.physDbo, synobj.refDbo, synobj.action, getSyncOrder(
                    synobj.physDbo, synobj.refDbo, synobj.action), modifiedFields));

        }
        syncObjs = temp;

        ReverseToolkitPlugin toolkit = ReverseToolkitPlugin.getToolkit();

        iter = syncObjs.iterator();
        while (iter.hasNext()) {
            SynchroObject synobj = (SynchroObject) iter.next();
            DbObject pDbo = synobj.physDbo;
            DbObject rDbo = synobj.refDbo;
            int action = synobj.action;

            // check if a rule support this action for this object class
            Rule rule = getRuleOf(rDbo, pDbo, action);

            // If Modify and a modify rule exists, check with the toolkit if the modified metafields are supported in the dbms sql clause
            // If not, the modify clause may be replaced with a drop and create
            // This isSupportedInModify_Synchro method will exclude all user metafields (default behavior)
            if (rule != null && ((action & SynchroObject.ACT_MODIFY) != 0)) {
                MetaField[] modifiedFields = synobj.modifiedMetaFields;
                if (modifiedFields == null)
                    continue;
                boolean allowModify = true;

                // If all modified metafields isSupportedInModify_Synchro, we must include the modify clause
                // If at least one modified metafield !isSupportedInModify_Synchro, we must try a drop/create
                for (int i = 0; i < modifiedFields.length && allowModify; i++) {
                    allowModify = toolkit.isSupportedInModify_Synchro(rDbo != null ? rDbo : pDbo,
                            modifiedFields[i]);
                }

                if (allowModify) {
                    toGenObjs.add(synobj);
                    continue;
                }

            } else if (rule != null) {
                toGenObjs.add(synobj);
                continue;
            }

            // Try dropping and creating this object if the action is modify or rename
            if ((action & (SynchroObject.ACT_RENAME | SynchroObject.ACT_MODIFY)) != 0) {
                Rule ruleDelete = getRuleOf(rDbo, pDbo, SynchroObject.ACT_DELETE);
                Rule ruleCreate = getRuleOf(rDbo, pDbo, SynchroObject.ACT_ADD);
                if (ruleDelete != null && ruleCreate != null) {
                    // For replacing a modify, we need to drop and create the object if possible.
                    if (pDbo == null) {
                        IntegrateNode node = findNode2((IntegrateNode) getRoot(), rDbo, false);
                        if (node != null)
                            pDbo = node.getRightDbo();
                    } else if (rDbo == null) {
                        IntegrateNode node = findNode2((IntegrateNode) getRoot(), pDbo, true);
                        if (node != null)
                            rDbo = node.getLeftDbo();
                    }
                    ArrayList equivalences = getEquivalentDropCreateFor(rDbo, pDbo);
                    if (equivalences != null && equivalences.size() > 0) {
                        Iterator iterator = equivalences.iterator();
                        while (iterator.hasNext()) {
                            addedGenObjs.add(iterator.next());
                        }
                        continue;
                    }
                }
            }

            // Allow unsupported component generation to be replaced with drop-create of their composite
            if (toolkit.isAllowCompositeDropCreateForComponentUpdate_Synchro(pDbo == null ? rDbo
                    .getMetaClass() : pDbo.getMetaClass())) {
                pDbo = (pDbo == null ? null : pDbo.getComposite());
                rDbo = (rDbo == null ? null : rDbo.getComposite());
            } else {
                Debug.trace("No rule for SynchroObject: "
                        + (synobj.physDbo != null ? synobj.physDbo.getMetaClass()
                                : (synobj.refDbo == null ? null : synobj.refDbo.getMetaClass()))
                        + " - Action: " + synobj.action); // NOT LOCALIZABLE - debug
                synobj.reportOnly = true;
                toGenObjs.add(synobj); // must be kept for further repporting - this will generate nothing
                continue;
            }

            Rule ruleDelete = getRuleOf(rDbo, pDbo, SynchroObject.ACT_DELETE);
            Rule ruleCreate = getRuleOf(rDbo, pDbo, SynchroObject.ACT_ADD);
            if (ruleDelete != null && ruleCreate != null) {
                // For replacing a modify, we need to drop and create the composite.  To avoid conflict in case the composite
                // has been renamed, we must drop the composite using the physical name (in the physical model).
                if (pDbo == null) {
                    IntegrateNode node = findNode2((IntegrateNode) getRoot(), rDbo, false);
                    if (node != null)
                        pDbo = node.getRightDbo();
                } else if (rDbo == null) {
                    IntegrateNode node = findNode2((IntegrateNode) getRoot(), pDbo, true);
                    if (node != null)
                        rDbo = node.getLeftDbo();
                }
                Debug.assert2(rDbo != null && pDbo != null,
                        "DefaultSynchroModel:  No matching object found in IntegrateModel."); // NOT LOCALIZABLE
                ArrayList equivalences = getEquivalentDropCreateFor(rDbo, pDbo);
                if (equivalences != null && equivalences.size() > 0) {
                    Iterator iterator = equivalences.iterator();
                    while (iterator.hasNext()) {
                        addedGenObjs.add(iterator.next());
                    }
                } else {
                    Debug
                            .trace("No rule for SynchroObject: "
                                    + (synobj.physDbo != null ? synobj.physDbo.getMetaClass()
                                            : (synobj.refDbo == null ? null : synobj.refDbo
                                                    .getMetaClass())) + " - Action: "
                                    + synobj.action); // NOT LOCALIZABLE  - debug
                    synobj.reportOnly = true;
                    toGenObjs.add(synobj); // must be kept for further repporting - this will generate nothing
                }
            } else {
                Debug.trace("No rule for SynchroObject: "
                        + (synobj.physDbo != null ? synobj.physDbo.getMetaClass()
                                : (synobj.refDbo == null ? null : synobj.refDbo.getMetaClass()))
                        + " - Action: " + synobj.action); // NOT LOCALIZABLE  - debug
                synobj.reportOnly = true;
                toGenObjs.add(synobj); // must be kept for further repporting - this will generate nothing
            }
        }

        // For each created table, add the fks SynchroObjects for creating the Fks of the created table
        Iterator iter4 = toGenObjs.iterator();
        while (iter4.hasNext()) {
            SynchroObject syncobj = (SynchroObject) iter4.next();
            if (syncobj.action != SynchroObject.ACT_ADD || syncobj.refDbo == null
                    || !(syncobj.refDbo instanceof DbORTable))
                continue;
            DbORTable reftable = (DbORTable) syncobj.refDbo;
            DbEnumeration fkeys = reftable.getComponents().elements(DbORForeign.metaClass);
            while (fkeys.hasMoreElements()) {
                DbORForeign fk = (DbORForeign) fkeys.nextElement();
                addedGenObjs.add(new SynchroObject(null, fk, SynchroObject.ACT_ADD, getSyncOrder(
                        null, fk, SynchroObject.ACT_ADD)));
            }
            fkeys.close();
        }

        // Before considering the new statement, check if not already included
        Iterator iter2 = addedGenObjs.iterator();
        while (iter2.hasNext()) {
            SynchroObject syncobj = (SynchroObject) iter2.next();
            if (!toGenObjs.contains(syncobj))
                toGenObjs.add(syncobj);
        }

        // process option DROP_TABLE_CASCADE_FK - fk from other tables referencing the droped table
        // If cascade suported, make sure no fk referencing a dropped table are marked as 'to generate a drop fk'
        if ((genOptions & OPTION_DROP_TABLE_CASCADE_FK) != 0) {
            Iterator iter3 = toGenObjs.iterator();
            while (iter3.hasNext()) {
                SynchroObject syncobj = (SynchroObject) iter3.next();
                if (syncobj.action != SynchroObject.ACT_DELETE || syncobj.physDbo == null
                        || !(syncobj.physDbo instanceof DbORForeign))
                    continue;
                DbORForeign fk = (DbORForeign) syncobj.physDbo;
                DbORTable phystable = (DbORTable) fk.getAssociationEnd().getReferencedConstraint()
                        .getComposite();

                // If a table is referenced by a deleted fk, don't need to remove the fk if this reference table is also deleted.
                // Check if the current generation objects include delete of the reftable.  If included, it will cascade the drop
                // on this fk.  If so, remove the syncobj.
                if (toGenObjs.contains(new SynchroObject(phystable, null, SynchroObject.ACT_DELETE,
                        getSyncOrder(phystable, null, SynchroObject.ACT_DELETE)))) {
                    iter3.remove();
                }
            }
        }

        // Ensure each child of all added objects has a chance to be generated if not included in the parent CREATE statement
        for (int k = 0; k < toGenObjs.size(); k++) {
            SynchroObject syncobj = (SynchroObject) toGenObjs.get(k);
            if (syncobj.action != SynchroObject.ACT_ADD)
                continue;

            addChildren(toGenObjs, syncobj.refDbo);
        }

        // Check dependencies
        addDependencies(toGenObjs);

        // Ask the toolkit for any specific replacement statement for each SynchroObject
        ArrayList newGenObjs = new ArrayList();
        iter2 = toGenObjs.iterator();
        while (iter2.hasNext()) {
            SynchroObject syncobj = (SynchroObject) iter2.next();
            SynchroObject[] equivalentObjs = toolkit.getEquivalentSynchroObject(syncobj);
            if (equivalentObjs == null)
                continue;
            for (int i = 0; i < equivalentObjs.length; i++)
                newGenObjs.add(equivalentObjs[i]);
        }
        toGenObjs = newGenObjs;

        // Remove invalid Modify or Rename (if drop create has been added, no need to modify or rename)
        toGenObjs = removeInvalidModifyRename(toGenObjs);

        // We don't need to modify, rename, drop or create an object if the parent is dropped (This case
        // may only occurs for previously added drop create statements.  Integration will not list modify/rename when
        // the composite is added or removed.
        toGenObjs = removeFromAncestorInclusion(toGenObjs);

        removeDuplicates(toGenObjs);

        // Reorder toGenObjs
        Collections.sort(toGenObjs);

        generate(toGenObjs);
    }

    // Find the node containing 'ref' object.
    // Start searching in the specified node.
    // Match the 'ref' object with the node objects according to 'isPhys'
    private IntegrateNode findNode2(IntegrateNode node, DbObject ref, boolean isPhys) {
        if (node == null)
            return null;
        if (node.isLeaf()) {
            if ((isPhys ? node.getRightDbo() : node.getLeftDbo()) == ref)
                return node;
            return null;
        }
        if ((isPhys ? node.getRightDbo() : node.getLeftDbo()) == ref) {
            return node;
        }
        int childcount = node.getChildCount();
        for (int i = 0; i < childcount; i++) {
            IntegrateNode child = (IntegrateNode) node.getChildAt(i);
            if (child == null)
                continue;
            if ((isPhys ? child.getRightDbo() : child.getLeftDbo()) == ref)
                return child;
            IntegrateNode childfind = findNode2(child, ref, isPhys);
            if (childfind != null)
                return childfind;
        }
        return null;
    }

    private ArrayList getEquivalentDropCreateFor(DbObject rDbo, DbObject pDbo) throws DbException {
        ArrayList objs = new ArrayList(5);
        if (rDbo == null && pDbo == null)
            return objs;
        SynchroObject objAdd = new SynchroObject(pDbo, rDbo, SynchroObject.ACT_ADD, getSyncOrder(
                pDbo, rDbo, SynchroObject.ACT_ADD));
        SynchroObject objDelete = new SynchroObject(pDbo, rDbo, SynchroObject.ACT_DELETE,
                getSyncOrder(pDbo, rDbo, SynchroObject.ACT_DELETE));
        objs.add(objDelete);
        objs.add(objAdd);

        DbObject absoluteDbo = (rDbo == null ? pDbo : rDbo);

        // Forcing drop create of a table implies adding statements for fk and optionnaly a drop statement for the same fk
        if (pDbo != null && (absoluteDbo instanceof DbORTable)) {
            DbORTable table = (DbORTable) pDbo;
            DbEnumeration dbEnum = table.getAssociationEnds().elements();
            while (dbEnum.hasMoreElements()) {
                DbORAssociationEnd end = (DbORAssociationEnd) dbEnum.nextElement();
                if (end.isNavigable()) // child
                    continue;
                if (!end.getOppositeEnd().isNavigable())
                    continue;
                DbORForeign fk = end.getOppositeEnd().getMember();
                if (fk == null)
                    continue;
                // If cascade not supported, we must make sure that each dropped table
                // has corresponding drop fk statements for child tables' fks referencing the dropped table.
                if ((genOptions & OPTION_DROP_TABLE_CASCADE_FK) == 0) {
                    objs.add(new SynchroObject(fk, null, SynchroObject.ACT_DELETE, getSyncOrder(fk,
                            null, SynchroObject.ACT_DELETE)));
                }

                // Add a create fk for this fk since it has been dropped by the drop table
                // This apply for both DROP_TABLE_CASCADE_FK and not DROP_TABLE_CASCADE_FK.
                // Before adding the create fk, make sure the parent table is not marked to be dropped.
                IntegrateNode node = findNode2((IntegrateNode) getRoot(), fk, true);
                if (node == null || node.getLeftDbo() == null)
                    continue;
                DbObject refFK = node.getLeftDbo();
                DbObject physTable = fk.getComposite();
                node = findNode2((IntegrateNode) getRoot(), physTable, true);
                if (node != null) {
                    DbObject refTable = node.getLeftDbo();
                    if (refTable != null) // If there is a corresponding refTable, this means that the physical Table is not marked as DROP
                        objs.add(new SynchroObject(null, refFK, SynchroObject.ACT_ADD,
                                getSyncOrder(null, refFK, SynchroObject.ACT_ADD)));
                }
            }
            dbEnum.close();

        }
        return objs;
    }

    // Add all children not included in the parent CREATE STATEMENT clause
    private void addChildren(ArrayList list, DbObject parent) throws DbException {
        if (parent == null || list == null)
            return;
        ReverseToolkitPlugin toolkit = ReverseToolkitPlugin.getToolkit();
        DbEnumeration dbEnum = parent.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            DbObject child = dbEnum.nextElement();
            if (DbORForeign.metaClass.isAssignableFrom(child.getMetaClass())) // This case has been previously processed
                continue;
            if (toolkit.isSupportedInCreate_Synchro(parent.getMetaClass(), child.getMetaClass()))
                continue;
            // If not supported in the composite create statement, add a create statement for the component
            SynchroObject childSyncObj = new SynchroObject(null, child, SynchroObject.ACT_ADD,
                    getSyncOrder(null, child, SynchroObject.ACT_ADD));
            if (list.contains(childSyncObj))
                continue;
            list.add(childSyncObj);
        }
        dbEnum.close();

        // If Database, we must add the datamodel and operation lib components
        if (parent instanceof DbORDatabase) {
            DbORDataModel datamodel = ((DbORDatabase) parent).getSchema();
            if (datamodel != null)
                addChildren(list, datamodel);
            DbOROperationLibrary operationLib = ((DbORDatabase) parent).getOperationLibrary();
            if (operationLib != null)
                addChildren(list, operationLib);
        }
    }

    private void addDependencies(ArrayList list) throws DbException {
        // This is important to use list.size() since adding a dependant object will allow a
        // dependencies check for this added object (recursively)
        // We don't need to check for recursive dependencies (2 opposites dependencies relations because
        // an object will be added to list only if not already checked.
        for (int k = 0; k < list.size(); k++) {
            SynchroObject syncobj = (SynchroObject) list.get(k);
            // Dependencies are added for drop /create only
            if ((syncobj.action & (SynchroObject.ACT_MODIFY | SynchroObject.ACT_RENAME)) != 0)
                continue;
            for (int i = 0; i < dependencies.length; i++) {
                MetaRelationship dependency = dependencies[i];
                MetaClass metaclass = dependency.getMetaClass();

                MetaRelationship oppositeRel = dependency
                        .getOppositeRel(syncobj.refDbo == null ? syncobj.physDbo : syncobj.refDbo);
                if (oppositeRel == null)
                    continue;
                MetaClass oppositeMetaClass = oppositeRel.getMetaClass();

                if (oppositeMetaClass == null)
                    continue;
                MetaClass objMetaClass = syncobj.refDbo == null ? syncobj.physDbo.getMetaClass()
                        : syncobj.refDbo.getMetaClass();
                // Is this relation valid for current obj?
                if (!oppositeMetaClass.isAssignableFrom(objMetaClass))
                    continue;

                if ((syncobj.action == SynchroObject.ACT_ADD && syncobj.refDbo == null)
                        || (syncobj.action == SynchroObject.ACT_DELETE && syncobj.physDbo == null))
                    continue;
                Object value = (syncobj.action == SynchroObject.ACT_DELETE) ? syncobj.physDbo
                        .get(dependency.getOppositeRel(syncobj.physDbo)) : syncobj.refDbo
                        .get(dependency.getOppositeRel(syncobj.refDbo));
                if (value == null)
                    continue;
                ArrayList toAdd = new ArrayList();
                if (value instanceof DbRelationN) {
                    DbRelationN values = (DbRelationN) value;
                    for (int j = 0; j < values.size(); j++)
                        toAdd.add(values.elementAt(j));
                } else if (value instanceof DbObject) {
                    toAdd.add(value);
                } else {
                    Debug.trace("Unexpected Value in DefaultSynchroModel: addDependencies");
                    continue;
                }
                Iterator iter = toAdd.iterator();
                while (iter.hasNext()) {
                    DbObject dbo = (DbObject) iter.next();
                    DbObject ref = ((syncobj.action & SynchroObject.ACT_ADD) != 0) ? dbo : null;
                    DbObject phys = ((syncobj.action & SynchroObject.ACT_DELETE) != 0) ? dbo : null;
                    SynchroObject newObj = new SynchroObject(phys, ref, syncobj.action,
                            getSyncOrder(phys, ref, syncobj.action));
                    if (!list.contains(newObj)) {
                        list.add(newObj);
                        // add children of this dependency object
                        if (syncobj.action == SynchroObject.ACT_ADD)
                            addChildren(list, ref);
                    }
                }
            }
            addPKUKDependencies(list, syncobj);
        }
    }

    private void addPKUKDependencies(ArrayList list, SynchroObject syncobj) throws DbException {
        // check dependency for FK referencing PK/UK
        if (syncobj.refDbo instanceof DbORPrimaryUnique
                || syncobj.physDbo instanceof DbORPrimaryUnique) {
            DbORPrimaryUnique puk = syncobj.action == SynchroObject.ACT_ADD ? (DbORPrimaryUnique) syncobj.refDbo
                    : (DbORPrimaryUnique) syncobj.physDbo;
            DbEnumeration dbEnum = puk.getAssociationReferences().elements();
            while (dbEnum.hasMoreElements()) {
                DbORAssociationEnd end = (DbORAssociationEnd) dbEnum.nextElement();
                DbORForeign fk = end.getMember();
                if (fk == null)
                    continue;
                // If cascade not supported, we must make sure that each dropped table
                // has corresponding drop fk statements for child tables' fks referencing the dropped table.
                if (syncobj.action == SynchroObject.ACT_DELETE
                        && (genOptions & OPTION_DROP_PK_UK_CASCADE_FK) == 0) {
                    SynchroObject objFK = new SynchroObject(fk, null, SynchroObject.ACT_DELETE,
                            getSyncOrder(fk, null, SynchroObject.ACT_DELETE));
                    if (!list.contains(objFK))
                        list.add(objFK);
                }

                // This apply for both DROP_PK_UK_CASCADE_FK and not DROP_PK_UK_CASCADE_FK.
                // Before adding the create ref fk, make sure the parent phys table is not marked to be dropped.
                else if (syncobj.action == SynchroObject.ACT_ADD) {
                    IntegrateNode node = findNode2((IntegrateNode) getRoot(), fk, false);
                    if (node == null || node.getRightDbo() == null) // If no matching in physical, marked as to drop or none or does not exist in phys ... skip
                        continue;
                    SynchroObject objFK = new SynchroObject(null, fk, SynchroObject.ACT_ADD,
                            getSyncOrder(null, fk, SynchroObject.ACT_ADD));
                    if (!list.contains(objFK))
                        list.add(objFK);
                }
            }
            dbEnum.close();
        }
    }

    private void removeDuplicates(ArrayList list) throws DbException {
        ArrayList newList = new ArrayList();
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            SynchroObject synobj = (SynchroObject) iter.next();
            if (!newList.contains(synobj))
                newList.add(synobj);
        }
        list.clear();
        list.addAll(newList);
    }

    // Remove invalid Modify or Rename (if drop create has been added, no need to modify or rename)
    private ArrayList removeInvalidModifyRename(ArrayList list) throws DbException {
        ArrayList newlist = new ArrayList();
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            SynchroObject synobj = (SynchroObject) iter.next();
            if (((synobj.action & SynchroObject.ACT_MODIFY) != 0)
                    || ((synobj.action & SynchroObject.ACT_RENAME) != 0)) {
                // just need to check for a create (if a create exists, a drop will exist) - check for the object and for the composite
                if (!list.contains(new SynchroObject(synobj.physDbo, synobj.refDbo,
                        SynchroObject.ACT_ADD, getSyncOrder(synobj.physDbo, synobj.refDbo,
                                SynchroObject.ACT_ADD)))) {
                    newlist.add(synobj);
                }
            } else
                newlist.add(synobj);
        }
        return newlist;
    }

    // We don't need to modify, rename, drop or create an object if the parent is dropped.  For ACT_ADD (create),
    // this applies to an object only if the create clause of the composite supports the component object inclusion.
    // (This case may only occur for previously added drop create statements.  Integration will not list modify/rename/add/delete when
    // the composite is added or removed.)
    private ArrayList removeFromAncestorInclusion(ArrayList list) throws DbException {
        ReverseToolkitPlugin toolkit = ReverseToolkitPlugin.getToolkit();
        ArrayList newlist = new ArrayList();
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            SynchroObject synobj = (SynchroObject) iter.next();
            // just need to check for a create (if a create exists, a drop will exist)
            // If add, check if this add clause is included in the add composite
            MetaClass composite = synobj.refDbo == null ? synobj.physDbo.getComposite()
                    .getMetaClass() : synobj.refDbo.getComposite().getMetaClass();
            MetaClass component = synobj.refDbo == null ? synobj.physDbo.getMetaClass()
                    : synobj.refDbo.getMetaClass();
            // If this is a root object (table, proc, ...), we don't need to check for the composite add clause
            // Root objects are the databases and any objects directly under Datamodel, operation lib or domain model in the composition tree.
            // We skip these objects because it is not possible to have an sql statement on their composite.
            // FK are processed elsewhere.
            if (DbORDataModel.metaClass.isAssignableFrom(composite)
                    || DbOROperationLibrary.metaClass.isAssignableFrom(composite)
                    || DbORDomainModel.metaClass.isAssignableFrom(composite)
                    || DbORDatabase.metaClass.isAssignableFrom(component)
                    || DbORForeign.metaClass.isAssignableFrom(component)) {
                newlist.add(synobj);
                continue;
            }
            if (((synobj.action & SynchroObject.ACT_ADD) != 0)) {
                // If add of the component is supported in the add clause of the composite and if the composite is marked has to be generated, skip
                // the component clause
                if (toolkit.isSupportedInCreate_Synchro(composite, component)
                        && list.contains(new SynchroObject(synobj.physDbo == null ? null
                                : synobj.physDbo.getComposite(), synobj.refDbo == null ? null
                                : synobj.refDbo.getComposite(), SynchroObject.ACT_ADD,
                                getSyncOrder(synobj.physDbo == null ? null : synobj.physDbo
                                        .getComposite(), synobj.refDbo == null ? null
                                        : synobj.refDbo.getComposite(), SynchroObject.ACT_ADD)))) {
                    continue;
                }
                newlist.add(synobj);
            }
            // Any modify-rename is invalid if there is a create on the composite
            else if (((synobj.action & (SynchroObject.ACT_MODIFY | SynchroObject.ACT_RENAME)) != 0)
                    && !list.contains(new SynchroObject(synobj.physDbo == null ? null
                            : synobj.physDbo.getComposite(), synobj.refDbo == null ? null
                            : synobj.refDbo.getComposite(), SynchroObject.ACT_ADD, getSyncOrder(
                            synobj.physDbo == null ? null : synobj.physDbo.getComposite(),
                            synobj.refDbo == null ? null : synobj.refDbo.getComposite(),
                            SynchroObject.ACT_ADD)))) {
                newlist.add(synobj);
            } else if (((synobj.action & SynchroObject.ACT_DELETE) != 0)) {
                newlist.add(synobj);
            }
        }
        return newlist;
    }

    private void generate(ArrayList genobjs) throws DbException {
        if (generator == null)
            return;

        // Open a transaction to be cancelled latter  ... to apply changes to physical objects
        //rightModel.getDb().beginWriteTrans("");
        // Apply all modified metafield values to the physicial object (This will be canceled later)
        /*
         * Iterator iterator = genobjs.iterator(); while (iterator.hasNext()){ SynchroObject synobj
         * = (SynchroObject)iterator.next(); if (synobj.action != SynchroObject.ACT_MODIFY ||
         * synobj.modifiedMetaFields == null || synobj.physDbo == null || synobj.refDbo == null)
         * continue; MetaField[] modifiedFields = synobj.modifiedMetaFields; for (int i = 0; i<
         * modifiedFields.length; i++){ if (modifiedFields[i] == DbObject.fComponents ||
         * modifiedFields[i] == DbObject.fComposite || modifiedFields[i] == DbSemanticalObject.fName
         * || modifiedFields[i] == DbSemanticalObject.fPhysicalName) continue; Object value =
         * synobj.refDbo.get(modifiedFields[i]); if (value instanceof DbRelationN) continue; if
         * (value instanceof DbObject && ((DbObject)value).getDb() != synobj.physDbo.getDb())
         * continue; if (modifiedFields[i] instanceof MetaRelation1 &&
         * ((MetaRelation1)modifiedFields[i]).getOppositeRel(null) instanceof MetaRelation1)
         * continue; synobj.physDbo.set(modifiedFields[i], value); } }
         */

        report = new StringBuffer();

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(sqlFile);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            if (genobjs.size() > 0)
                writeSQLFileHeader(printWriter);
            Iterator iter = genobjs.iterator();
            while (iter.hasNext()) {
                SynchroObject obj = (SynchroObject) iter.next();
                generate(fileWriter, obj);
            }
            fileWriter.close();
            if (report.length() > 0) {
                reportBuffer.append(EOL + EOL + EOL + kDDLGenMessages + EOL);
                reportBuffer.append(report.toString());
            }
        } catch (RuleException re) {
            throw new TargetRuntimeException(re);
        } catch (IOException ioe) {
            throw new TargetRuntimeException(ioe);
        }

        // Cancel all temporary update on the physical objects
        //rightModel.getDb().abortTrans();

    }

    private void generate(Writer writer, SynchroObject obj) throws DbException, RuleException,
            IOException {
        if (obj == null)
            return;
        DbObject refDbo = obj.refDbo;
        DbObject physDbo = obj.physDbo;
        int action = obj.action;
        boolean reportOnly = obj.reportOnly;
        // Ignore PK/ UK Indexes for drop and create
        if (((genOptions & OPTION_IGNORE_CREATE_PK_UK_INDEX) != 0)
                && ((action & SynchroObject.ACT_ADD) != 0) && refDbo instanceof DbORIndex) {
            if (((DbORIndex) refDbo).getConstraint() instanceof DbORPrimaryUnique)
                return;
        } else if (((genOptions & OPTION_IGNORE_DROP_PK_UK_INDEX) != 0)
                && ((action & SynchroObject.ACT_DELETE) != 0) && physDbo instanceof DbORIndex) {
            if (((DbORIndex) physDbo).getConstraint() instanceof DbORPrimaryUnique)
                return;
        }

        Rule rule = getRuleOf(refDbo, physDbo, action);
        StringWriter stringWriter = new StringWriter();
        if (generator != null)
            generator.setOutputToASCIIFormat();
        String temporaryVariable = null;
        Rule.RuleOptions options = null;

        if (rule == null || reportOnly)
            appendErrorToRepport(refDbo, physDbo, action);
        else if (generator != null) {
            try {
                VariableScope scope = generator.getVarScope();
                tempSynchroObject = obj;
                switch (action) {
                case SynchroObject.ACT_ADD:
                    Debug.assert2(refDbo != null,
                            "Programmatic error in DefaultSynchroModel.generate()"); //NOT LOCALIZABLE, debug code
                    rule.expand(stringWriter, refDbo, options);
                    break;
                case SynchroObject.ACT_DELETE:
                    Debug.assert2(physDbo != null,
                            "Programmatic error in DefaultSynchroModel.generate()"); //NOT LOCALIZABLE, debug code
                    rule.expand(stringWriter, physDbo, options);
                    break;
                case SynchroObject.ACT_MODIFY:
                    rule.expand(stringWriter, refDbo, options);
                    break;
                case SynchroObject.ACT_RENAME:
                    String newname = (refDbo == null ? "???" : ((DbSemanticalObject) refDbo)
                            .getPhysicalName());
                    temporaryVariable = "var0"; //NOT LOCALIZABLE, variable name
                    rule.expand(stringWriter, physDbo, scope, options,
                            new String[] { temporaryVariable + "=" + newname }); // NOT LOCALIZABLE
                    break;
                }
                generator.setOutputToASCIIFormat();
                String s = stringWriter.toString();
                writer.write(EditionCode.processEditionCode(s));

                //remove temporary variable to restore to previous context
                if (temporaryVariable != null) {
                    VariableDecl.undeclare(scope, temporaryVariable);
                }
            } finally {
                tempSynchroObject = null;
            }
        } //end if
    } //end generate()

    private void writeSQLFileHeader(PrintWriter writer) throws DbException, IOException {
        String commentTag = generator == null ? "//" : generator.getCommentTag(); // NOT LOCALIZABLE

        String line;

        //REM ***********************************************************
        writer.println(commentTag + COMMENT_SEPARATOR);

        //REM Synchronization
        line = MessageFormat.format("{0}{1}", new Object[] { commentTag, kHeader });
        writer.println(line);

        if (generator != null) {
            line = MessageFormat.format(PLUGINS_PATTERN, new Object[] { commentTag,
                    ReverseToolkitPlugin.getToolkit().getSignature().toString() });
            writer.println(line);
        }

        if (generator != null) {
            line = MessageFormat.format("{0}          {1}", new Object[] { commentTag, //NOT LOCALIZABLE, sql file
                    generator.getSignature().toString() });
            writer.println(line);
        }

        //Empty line
        writer.println(commentTag);

        //REM SMS Model: tablespace_add.sms: Oracle.Data Model <Oracle 8.1>
        line = MessageFormat
                .format("{0}{1}: {2}{3}", new Object[] { commentTag, getColHeaders()[COL_LEFT],
                        leftModel.getFullDisplayName(), getTSSuffix(leftModel) });
        writer.println(line);

        //REM Physical Model: rev_ora_unitaire_check.sms: Oracle.Data Model <Oracle 8.1>
        if (options.synchroOnline) {
            String connection = MessageFormat.format(kOnline0, new Object[] { DBMSUtil
                    .getConnectionAsString(options.getConnection()) });
            line = MessageFormat.format("{0}{1}: {2}", new Object[] { commentTag,
                    getColHeaders()[COL_RIGHT], connection });
            writer.println(line);
        } else {
            line = MessageFormat.format("{0}{1}: {2}{3}", new Object[] { commentTag,
                    getColHeaders()[COL_RIGHT], rightModel.getFullDisplayName(),
                    getTSSuffix(rightModel) });
            writer.println(line);
        }

        //Empty line
        writer.println(commentTag);

        //REM Generated on: Oct 31, 2001 2:27:46 PM
        String date = DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis()));
        line = MessageFormat.format("{0}{1} {2}", new Object[] { commentTag, GENERATE_ON, date });
        writer.println(line);

        line = MessageFormat.format(GENERATED_BY_PATTERN, new Object[] { commentTag,
                ApplicationContext.getApplicationName(), ApplicationContext.getApplicationVersion() });
        writer.println(line);

        //REM ***********************************************************
        writer.println(commentTag + COMMENT_SEPARATOR);

        writer.println();
    }

    protected void reportHeader(StringBuffer reportBuffer) throws DbException {
        reportBuffer.append(kHeader + EOL);
        super.reportHeader(reportBuffer);
    }

    private void appendErrorToRepport(DbObject refDbo, DbObject physDbo, int syncAction)
            throws DbException {
        switch (syncAction) {
        case SynchroObject.ACT_ADD:
            if (refDbo != null) {
                report.append(MessageFormat.format(kNotAdded01, new Object[] {
                        refDbo.getMetaClass().getGUIName(),
                        refDbo.getSemanticalName(DbObject.SHORT_FORM) })
                        + EOL);
            }
            break;
        case SynchroObject.ACT_DELETE:
            if (physDbo != null) {
                report.append(MessageFormat.format(kNotDeleted01, new Object[] {
                        physDbo.getMetaClass().getGUIName(),
                        physDbo.getSemanticalName(DbObject.SHORT_FORM) })
                        + EOL);
            }
            break;
        case SynchroObject.ACT_MODIFY:
            if (physDbo != null) {
                report.append(MessageFormat.format(kNotModified01, new Object[] {
                        physDbo.getMetaClass().getGUIName(),
                        physDbo.getSemanticalName(DbObject.SHORT_FORM) })
                        + EOL);
            }
            break;
        case SynchroObject.ACT_RENAME:
            if (physDbo != null) {
                report.append(MessageFormat.format(kNotRenamed01, new Object[] {
                        physDbo.getMetaClass().getGUIName(),
                        physDbo.getSemanticalName(DbObject.SHORT_FORM) })
                        + EOL);
            }
            break;
        }
    }

    private Rule getRuleOf(DbObject refDbo, DbObject physDbo, int syncAction) throws DbException {
        if (generator == null)
            return null;
        DbObject ruleDbo = refDbo == null ? physDbo : refDbo; // only object type is important for getRuleOf
        if (ruleDbo == null)
            return null;
        Rule rule = null;
        int genFwdAction = -1;
        switch (syncAction) {
        case SynchroObject.ACT_ADD:
            genFwdAction = DBMSForwardOptions.CREATE_ADD_SYNCHRO;
            break;
        case SynchroObject.ACT_DELETE:
            genFwdAction = DBMSForwardOptions.DROP_DELETE;
            break;
        case SynchroObject.ACT_MODIFY:
            genFwdAction = DBMSForwardOptions.ALTER_MODIFY;
            break;
        case SynchroObject.ACT_RENAME:
            genFwdAction = DBMSForwardOptions.RENAME;
            break;
        default:
            throw new RuntimeException("Invalid Synchronization Action. "); // NOT LOCALIZABLE
        }
        rule = generator.getRuleOf(ruleDbo, genFwdAction); // Works with the class
        return rule;
    }

    // Overrided from IntegrateModel.  Delegate this task to the toolkit.
    protected boolean isEquivalent(MetaField field, DbObject sourceDbo, Object sourceVal,
            DbObject targetDbo, Object targetVal) throws DbException {
        if (field == DbORAssociationEnd.fInsertRule || field == DbORAssociationEnd.fUpdateRule
                || field == DbORAssociationEnd.fDeleteRule) {
            if (sourceVal == null ^ targetVal == null) {
                return sourceVal == null ? targetVal.equals(ORValidationRule
                        .getInstance(ORValidationRule.NONE)) : sourceVal.equals(ORValidationRule
                        .getInstance(ORValidationRule.NONE));
            }
            return false;
        }
        return ReverseToolkitPlugin.getToolkit().isEquivalentInSynchro(field, sourceDbo, sourceVal,
                targetDbo, targetVal);
    }

    public static final SynchroObject getSynchroObject() {
        return tempSynchroObject;
    }

}
