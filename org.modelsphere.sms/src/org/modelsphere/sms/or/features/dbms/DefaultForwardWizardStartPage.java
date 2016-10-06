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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import org.modelsphere.jack.awt.tree.CheckEvent;
import org.modelsphere.jack.awt.tree.CheckListener;
import org.modelsphere.jack.awt.tree.CheckTreeModel;
import org.modelsphere.jack.awt.tree.CheckTreeNode;
import org.modelsphere.jack.awt.tree.CheckableTree;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.gui.wizard.WizardPage;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.plugins.generic.dbms.ObjectScope;
import org.modelsphere.jack.srtool.plugins.generic.dbms.ObjectSelection;
import org.modelsphere.jack.srtool.plugins.generic.dbms.UserInfo;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.jack.util.StringUtil;
import org.modelsphere.sms.db.DbSMSAbstractPackage;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.or.db.DbORConstraint;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.db.util.AnyORObject;
import org.modelsphere.sms.templates.SQLForwardEngineeringPlugin;

public class DefaultForwardWizardStartPage extends WizardPage {
    private GridBagLayout startGridBagLayout = new GridBagLayout();
    private JPanel statementPanel = new JPanel();
    private GridBagLayout statementPanelGridBagLayout = new GridBagLayout();
    private static final String kEmptyPhysicalName = LocaleMgr.screen.getString("emptyToken");
    private TitledBorder statementTitledBorder = new TitledBorder(LocaleMgr.screen
            .getString("StatementToGenerate"));
    private JRadioButton createRadioButton = new JRadioButton(LocaleMgr.screen.getString("Create"));
    private JRadioButton dropRadioButton = new JRadioButton(LocaleMgr.screen.getString("Drop"));
    private JRadioButton dropCreateRadioButton = new JRadioButton(LocaleMgr.screen
            .getString("DropCreate"));
    private ButtonGroup statementRadioGroup = new ButtonGroup();
    private CheckableTree conceptTree = new CheckableTree();
    private JScrollPane conceptTreeScrollPane = new JScrollPane(conceptTree);

    private DatabaseNamePanel databaseNamePanel = null;

    public DefaultForwardWizardStartPage() {
        super();
    }

    private void initGUI(DBMSForwardOptions options) {
        this.setLayout(startGridBagLayout);
        statementPanel.setLayout(statementPanelGridBagLayout);
        statementPanel.setBorder(statementTitledBorder);
        statementRadioGroup.add(createRadioButton);
        statementRadioGroup.add(dropRadioButton);
        statementRadioGroup.add(dropCreateRadioButton);
        ForwardToolkitPlugin.setActiveDiagramTarget(options.getTargetSystemId());
        int[] supportedStatements = ForwardToolkitPlugin.getToolkit().getSupportedStatements();

        this.add(databaseNamePanel, new GridBagConstraints(0, 0, 2, 1, 1.0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(9, 9, 0, 9), 0, 0));
        this.add(statementPanel,
                new GridBagConstraints(0, 1, 2, 2, 1.0, 1.0, GridBagConstraints.CENTER,
                        GridBagConstraints.BOTH, new Insets(0, 12, 12, 12), 0, 0));
        for (int i = 0; i < supportedStatements.length; i++) {
            JRadioButton radioButton = null;
            switch (supportedStatements[i]) {
            case DBMSForwardOptions.CREATE_STATEMENTS:
                radioButton = createRadioButton;
                break;
            case DBMSForwardOptions.DROP_STATEMENTS:
                radioButton = dropRadioButton;
                break;
            case DBMSForwardOptions.DROP_CREATE_STATEMENTS:
                radioButton = dropCreateRadioButton;
                break;
            default:
                break;
            }
            if (radioButton != null) {
                statementRadioGroup.add(radioButton);
                int topInset = (i == 0 ? 6 : 0);
                statementPanel.add(radioButton, new GridBagConstraints(0, i, 1, 1, 0.5, 0.0,
                        GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(topInset,
                                10, 0, 0), 0, 0));
            }
        }
        statementPanel.add(conceptTreeScrollPane,
                new GridBagConstraints(1, 0, 1, 8, 0.5, 1.0, GridBagConstraints.CENTER,
                        GridBagConstraints.BOTH, new Insets(6, 10, 12, 10), 0, 0));
        setNextEnabled(true);
    }

    protected int getStatementToGenerate() {
        if (createRadioButton.isSelected())
            return DBMSForwardOptions.CREATE_STATEMENTS;
        else if (dropRadioButton.isSelected())
            return DBMSForwardOptions.DROP_STATEMENTS;
        else if (dropCreateRadioButton.isSelected())
            return DBMSForwardOptions.DROP_CREATE_STATEMENTS;
        else
            return -1;
    }

    protected void setStatementToGenerate(int statement) {
        switch (statement) {
        case DBMSForwardOptions.CREATE_STATEMENTS:
            createRadioButton.setSelected(true);
            break;
        case DBMSForwardOptions.DROP_STATEMENTS:
            dropRadioButton.setSelected(true);
            break;
        case DBMSForwardOptions.DROP_CREATE_STATEMENTS:
            dropCreateRadioButton.setSelected(true);
            break;
        }
    }

    public final boolean initialize(Object opt) {
        DBMSForwardOptions options = (DBMSForwardOptions) opt;
        databaseNamePanel = new DatabaseNamePanel(options.databaseName);
        initGUI(options);

        conceptTree.setModel(new ConceptTreeModel(new DefaultMutableTreeNode("JTree", true),
                options));
        conceptTree.setRootVisible(false);
        conceptTree.setShowsRootHandles(true);
        conceptTree.addCheckListener(new CheckListener() {
            public void itemChecked(CheckEvent e) {
                updateNextButton();
            }
        });
        // init GUI from options
        setStatementToGenerate(options.statementToGenerate);
        setNextEnabled(true);
        return true;
    }

    public final boolean terminate(Object opt, boolean saveData) {
        DBMSForwardOptions options = (DBMSForwardOptions) opt;
        if (!saveData)
            return true;

        // save GUI setting
        options.statementToGenerate = getStatementToGenerate();

        try {
            populateObjectScopeOccurences(options);
        } catch (DbException e) {
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
        }

        return true;
    }

    private void updateNextButton() {
        boolean found = false;

        for (int i = 0; i < conceptTree.getRowCount(); i++) {
            ConceptTreeNode conceptNode = (ConceptTreeNode) conceptTree.getCheckTreeNodeForRow(i);
            ObjectScope opt = conceptNode.getObjectScope();
            if (opt.isSelected && conceptTree.isEnabled(conceptNode)) {
                found = true;
                break;
            }
        }
        setNextEnabled(found);
    }

    protected void populateObjectScopeOccurences(DBMSForwardOptions options) throws DbException {

        ObjectScope[] scope = options.getObjectsScope();
        ObjectScope userScope = ObjectScope.findConceptInObjectScopeWithConceptName(scope,
                DbORUser.metaClass.getGUIName());
        if (userScope == null)
            return;
        // DbORDataModel dataModel = options.getDataModel();
        DbSMSAbstractPackage pack = options.getAbstractPackage();
        pack.getDb().beginTrans(Db.READ_TRANS);

        for (int i = 0; i < scope.length; i++) {
            ObjectScope concept = scope[i];
            concept.occurences.clear();

            if (concept.metaClass != null) {
                Object dbEnum = null;
                if (concept.metaClass == DbORUser.metaClass) {
                    dbEnum = ((DbSMSProject) pack.getProject()).getUserNode().getComponents()
                            .elements();
                } else if (DbORDatabase.metaClass.isAssignableFrom(concept.metaClass)) {
                    DbORDatabase database = null;

                    if (pack instanceof DbORDatabase) {
                        database = (DbORDatabase) pack;
                    } else if (pack instanceof DbORDataModel) {
                        DbORDataModel dm = (DbORDataModel) pack;
                        database = dm.getDeploymentDatabase();
                    } else if (pack instanceof DbOROperationLibrary) {
                        DbOROperationLibrary ol = (DbOROperationLibrary) pack;
                        database = ol.getDeploymentDatabase();
                    }

                    if (database != null) {
                        ObjectSelection occurence = new ObjectSelection(database);
                        occurence.name = (StringUtil.isEmptyString(database.getPhysicalName()) ? kEmptyPhysicalName
                                : database.getPhysicalName());
                        concept.occurences.add(occurence);
                    }
                } else {
                    ForwardToolkitPlugin toolkit = ForwardToolkitPlugin.getToolkit();
                    dbEnum = toolkit.getDbEnumerationForConcept(concept.metaClass, pack);
                }

                // enum...
                if (dbEnum != null) {
                    if (dbEnum instanceof DbEnumeration)
                        populateObjectScopeWithEnum((DbEnumeration) dbEnum, options, concept,
                                userScope);
                    else if (dbEnum instanceof ArrayList) {
                        ArrayList listEnum = (ArrayList) dbEnum;
                        for (int j = 0; j < listEnum.size(); j++) {
                            populateObjectScopeWithEnum((DbEnumeration) listEnum.get(j), options,
                                    concept, userScope);
                        }
                    }
                }
            }
        }
        pack.getDb().commitTrans();
    }

    private void populateObjectScopeWithEnum(DbEnumeration dbEnum, DBMSForwardOptions options,
            ObjectScope concept, ObjectScope userScope) throws DbException {
        while (dbEnum.hasMoreElements()) {
            // For Owned Objects, use the username
            DbObject obj = dbEnum.nextElement();
            if (obj instanceof DbORPrimaryUnique) {
                if (concept.conceptID == SQLForwardEngineeringPlugin.UniqueId.intValue()
                        && ((DbORPrimaryUnique) obj).isPrimary())
                    continue;
                else if (concept.conceptID == SQLForwardEngineeringPlugin.PrimaryId.intValue()
                        && !((DbORPrimaryUnique) obj).isPrimary())
                    continue;
            }
            if (concept.isOwnedObject) {
                DbORUser owner = null;

                MetaField metaField = null;
                if (obj instanceof DbORConstraint) {
                    metaField = AnyORObject.getUserField(obj.getComposite());
                    if (metaField != null)
                        owner = (DbORUser) obj.getComposite().get(metaField);
                } else {
                    metaField = AnyORObject.getUserField(obj);
                    if (metaField != null)
                        owner = (DbORUser) obj.get(metaField);
                }

                boolean found = false;
                UserInfo findUser = null;
                ObjectSelection occurence = new ObjectSelection(obj);
                if (obj instanceof DbSemanticalObject)
                    occurence.name = (StringUtil.isEmptyString(((DbSemanticalObject) obj)
                            .getPhysicalName()) ? kEmptyPhysicalName : ((DbSemanticalObject) obj)
                            .getPhysicalName());
                else
                    occurence.name = kEmptyPhysicalName;
                for (int j = 0; j < concept.occurences.size(); j++) {
                    findUser = (UserInfo) concept.occurences.get(j);
                    if (findUser != null && findUser.user == owner) {
                        found = true;
                        break;
                    }
                }

                if (found)
                    findUser.occurences.add(occurence);
                else {
                    // add the user in the user Scope
                    if (!ObjectScope.findOccurenceByObject(userScope, owner)) {
                        ObjectSelection userOccurence = new ObjectSelection(owner);
                        if (owner != null)
                            userOccurence.name = (StringUtil.isEmptyString(owner.getPhysicalName()) ? kEmptyPhysicalName
                                    : owner.getPhysicalName());
                        else
                            userOccurence.name = kEmptyPhysicalName;
                        userScope.occurences.add(userOccurence);
                    }

                    // add the occurences to the owned object
                    UserInfo userInfo = new UserInfo(owner);
                    userInfo.occurences.add(occurence);
                    concept.occurences.add(userInfo);
                }
            }
            // For Unowned Objects
            else {
                ObjectSelection occurence = new ObjectSelection(obj);
                if (obj instanceof DbSemanticalObject)
                    occurence.name = (StringUtil.isEmptyString(((DbSemanticalObject) obj)
                            .getPhysicalName()) ? kEmptyPhysicalName : ((DbSemanticalObject) obj)
                            .getPhysicalName());
                else
                    occurence.name = kEmptyPhysicalName;
                concept.occurences.add(occurence);
            }
        }
        dbEnum.close();
    }

    // INNER CLASS
    private class ConceptTreeModel extends CheckTreeModel {
        ConceptTreeModel(DefaultMutableTreeNode root, DBMSForwardOptions options) {
            super(root);

            Hashtable table = new Hashtable();
            ObjectScope[] scope = options.getObjectsScope();

            for (int i = 0; i < scope.length; i++) {
                ObjectScope concept = scope[i];
                if (concept.showConcept) {
                    ConceptTreeNode conceptNode = new ConceptTreeNode(concept, true);
                    table.put(concept.toString(), conceptNode);

                    root.add(conceptNode);

                }
            }
        }
    }

    // INNER CLASS
    private static final class ConceptTreeNode extends CheckTreeNode {
        private int conceptId;
        private ObjectScope objectScope;

        ConceptTreeNode(ObjectScope scope, boolean allowchildren) {
            super(scope.toString(), allowchildren, scope.isSelected);
            conceptId = scope.conceptID;
            objectScope = scope;
        }

        int getConceptId() {
            return conceptId;
        }

        ObjectScope getObjectScope() {
            return objectScope;
        }

        public void setSelected(boolean b) {
            objectScope.isSelected = b;
            super.setSelected(b);
        }
    }

}
