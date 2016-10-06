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

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.awt.beans.impl.PropertyDialog;
import org.modelsphere.jack.awt.tree.CheckTreeModel;
import org.modelsphere.jack.awt.tree.CheckTreeNode;
import org.modelsphere.jack.awt.tree.CheckableTree;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.gui.wizard.WizardPage;
import org.modelsphere.jack.plugins.PluginMgr;
import org.modelsphere.jack.plugins.PluginsRegistry;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.VariableScope;
import org.modelsphere.jack.srtool.integrate.IntegrateScopeDialog;
import org.modelsphere.jack.srtool.plugins.generic.dbms.ObjectScope;
import org.modelsphere.jack.srtool.plugins.generic.dbms.ObjectSelection;
import org.modelsphere.jack.srtool.plugins.generic.dbms.UserInfo;
import org.modelsphere.jack.templates.TemplateDialog;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.SMSIntegrateModelUtil;
import org.modelsphere.sms.db.DbSMSAbstractPackage;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.preference.DirectoryOptionGroup;
import org.modelsphere.sms.templates.SQLForwardEngineeringPlugin;

public class DefaultForwardWizardObjectsPage extends WizardPage {

    private static final String kUnspecifiedUser = LocaleMgr.screen.getString("Unspecified");
    private static final String kRootName = "Objects"; //NOT LOCALIZABLE
    private static final String kGeneralScope = LocaleMgr.screen.getString("GeneralScope");
    private static final String kUsers = LocaleMgr.screen.getString("Users");
    private static final String kSelectedUserObjects = LocaleMgr.screen
            .getString("SelectedUserObjects");
    private static final String kSaveSQLScriptAs = LocaleMgr.screen.getString("SaveSQLScriptAs");
    private static final String kFilterAttribute = LocaleMgr.screen.getString("FilterAttributes");
    private static final String kOptions = LocaleMgr.screen.getString("Options");
    private static final String kGenerationParams = org.modelsphere.jack.international.LocaleMgr.screen
            .getString("SetGenerationVariables");
    private static final String CLOSE = org.modelsphere.jack.international.LocaleMgr.screen
            .getString("Close");

    private JPanel generalScopePanel = new JPanel(new GridBagLayout());
    private JPanel generateOptionsPanel = new JPanel(new GridBagLayout());
    private CheckableTree userTree = new CheckableTree();
    private CheckableTree objectsTree = new CheckableTree();
    private JScrollPane userScrollPane = new JScrollPane(userTree);
    private JPanel occurrenceTreePanel = new JPanel();
    private CardLayout cardLayoutOccurrence = new CardLayout();

    private TitledBorder generalScopeTitledBorder = new TitledBorder(kGeneralScope);
    private JLabel userLabel = new JLabel(kUsers);
    private JLabel objectsLabel = new JLabel(kSelectedUserObjects);
    //private JButton m_filterAttributesBtn;
    private JButton m_generationParametersBtn;
    //private FilterAttributesAction m_filterAttributesAction;
    private GenerationParametersAction m_generationParametersAction;

    int cardLayoutCurrentItem = 0, cardLayoutLastItem = 0;
    Hashtable cardLayoutComponentTable = new Hashtable();

    private DatabaseNamePanel databaseNamePanel = null;

    public DefaultForwardWizardObjectsPage() {
        super();
        //m_filterAttributesAction = new FilterAttributesAction();
        //m_filterAttributesBtn = new JButton(m_filterAttributesAction);
        //m_filterAttributesAction.setOwner(this);

        m_generationParametersAction = new GenerationParametersAction();
        m_generationParametersBtn = new JButton(m_generationParametersAction);
        m_generationParametersAction.setOwner(this);
    }

    private void initGUI() {
        this.setLayout(new GridBagLayout());
        occurrenceTreePanel.setLayout(cardLayoutOccurrence);
        ForwardToolkitPlugin toolkit = ForwardToolkitPlugin.getToolkit();
        TitledBorder generationOptionsTitledBorder = new TitledBorder(kOptions);

        generalScopePanel.setBorder(generalScopeTitledBorder);
        generateOptionsPanel.setBorder(generationOptionsTitledBorder);

        this
                .add(generalScopePanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 12, 6, 12), 0, 0));
        generalScopePanel.add(userLabel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 12, 0, 6), 0, 0));
        generalScopePanel
                .add(userScrollPane, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 12, 12, 6), 0, 0));
        generalScopePanel.add(objectsLabel, new GridBagConstraints(2, 0, 3, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 6, 0, 12), 0, 0));
        generalScopePanel
                .add(occurrenceTreePanel, new GridBagConstraints(2, 1, 3, 2, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 6, 12, 12), 0, 0));

        this.add(generateOptionsPanel,
                new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                        GridBagConstraints.BOTH, new Insets(3, 12, 12, 12), 0, 0));

        /*
         * generateOptionsPanel.add(m_filterAttributesBtn, new GridBagConstraints(0, 0, 1, 1, 1.0,
         * 0.0 ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 6), 0, 0));
         */

        generateOptionsPanel.add(m_generationParametersBtn, new GridBagConstraints(1, 0, 1, 1, 1.0,
                0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 12, 11, 11),
                0, 0));

        this.add(databaseNamePanel, new GridBagConstraints(0, 0, 4, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(9, 9, 0, 9), 0, 0));

        setNextEnabled(true);
    }

    public final boolean initialize(Object opt) {
        DBMSForwardOptions options = (DBMSForwardOptions) opt;
        databaseNamePanel = new DatabaseNamePanel(options.databaseName);
        //m_filterAttributesAction.setOptions(options);

        // init GUI from options
        initGUI();
        initTrees(options);

        return true;
    }

    private static final String DEFAULT_GEN_FILE_NAME = org.modelsphere.sms.international.LocaleMgr.forward
            .getString("genddl.sql");

    //getDefaultGenFilename : add .html if HTML tags
    private String getDefaultGenFilename(DBMSForwardOptions options) {
        String defaultGenFileName = DEFAULT_GEN_FILE_NAME;

        if (options.getOutputInHtml()) {
            defaultGenFileName = defaultGenFileName + ".html"; //NOT LOCALIZABLE, file extension
        }

        return defaultGenFileName;
    } //end getDefaultGenFilename()

    private static File g_previousGenerationDirectory = null;
    private static boolean g_previousIsHtmlOutput = false;

    public final boolean terminate(Object opt, boolean saveData) {
        DBMSForwardOptions options = (DBMSForwardOptions) opt;
        if (!saveData)
            return true;

        terminateSpecific(options);

        //get the actual SQL forward
        ForwardToolkitPlugin toolkit = ForwardToolkitPlugin.getToolkit();
        PluginMgr mgr = PluginMgr.getSingleInstance();
        ArrayList plugins = mgr.getPluginsRegistry().getActivePluginInstances(
                toolkit.getForwardClass());
        SQLForwardEngineeringPlugin sqlForward = null;
        if (plugins != null && plugins.size() > 0)
            sqlForward = (SQLForwardEngineeringPlugin) plugins.get(0);

        //is output in HTML?
        if (sqlForward != null) {
            boolean inHtml = sqlForward.isOutputInHTMLFormat();
            options.setOutputInHtml(inHtml);
        }

        // remember previous generation directory, if any [MS]
        File defaultGenFile;
        if (g_previousGenerationDirectory == null) {
            String ddlGenerationDirectory = DirectoryOptionGroup.getDDLGenerationDirectory();
            String defaultGenFileName = ddlGenerationDirectory + File.separatorChar
                    + getDefaultGenFilename(options);
            defaultGenFile = new File(defaultGenFileName);
        } else {
            defaultGenFile = g_previousGenerationDirectory;
        }

        ExtensionFileFilter filter = options.getOutputInHtml() ? ExtensionFileFilter.htmlFileFilter
                : ExtensionFileFilter.sqlFileFilter;
        ExtensionFileFilter[] optionalFilters = null;
        AwtUtil.FileAndFilter selection = AwtUtil.showSaveAsDialog(this, kSaveSQLScriptAs, filter, optionalFilters, defaultGenFile);
        File file = (selection == null) ? null : selection.getFile(); 
        
        if (file != null) {
            options.setOutputFile(file.getPath());
            g_previousGenerationDirectory = file; //remember selected directory for a next call [MS]

            if (g_previousIsHtmlOutput != options.getOutputInHtml()) {
                g_previousGenerationDirectory = null; //previous name not relevant if output format has changed
            }
            g_previousIsHtmlOutput = options.getOutputInHtml();
        } else {
            return false;
        }

        return true;
    }

    private void initTrees(final DBMSForwardOptions options) {
        // Create user tree
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(kRootName, true);
        UserTreeModel model = new UserTreeModel(treeNode, options);
        userTree.setModel(model);
        userTree.setRootVisible(false);
        userTree.setShowsRootHandles(true);
        userTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = e.getNewLeadSelectionPath();
                redrawOccurrencePanel(path, options);
            }
        });

        createOccurenceTrees(options);
    }

    // Show the selected occurrence panel
    private void redrawOccurrencePanel(TreePath path, DBMSForwardOptions options) {
        if (path != null) {
            Object component = path.getLastPathComponent();
            if (component != null) {
                if (component instanceof UserNode) {
                    showCardLayoutComponent(occurrenceTreePanel, ((UserNode) component)
                            .getConceptId());
                } else if (component instanceof DefaultMutableTreeNode) {
                    if (((DefaultMutableTreeNode) component).getUserObject() instanceof ObjectScope) {
                        ObjectScope scope = (ObjectScope) ((DefaultMutableTreeNode) component)
                                .getUserObject();
                        DbORUser user = null;
                        TreePath treepath = path.getParentPath();
                        Object obj = treepath.getLastPathComponent();
                        if (obj instanceof UserNode) {
                            UserNode userNode = (UserNode) obj;
                            user = (DbORUser) userNode.getUserObject();
                            UserInfo findUser = null;
                            boolean found = false;
                            int nb = scope.occurences.size();
                            for (int j = 0; j < nb; j++) {
                                findUser = (UserInfo) scope.occurences.get(j);
                                if (findUser != null && findUser.user == user) {
                                    found = true;
                                    break;
                                }
                            } //end for

                            if (found) {
                                showCardLayoutComponent(occurrenceTreePanel, findUser.id);
                            } //end if
                        } //end if
                    } //end if
                } //end if
            } //end if
        } else {
            showCardLayoutComponent(occurrenceTreePanel, -1);
        } //end if
    } //end redrawOccurrencePanel()

    // Create all occurence trees
    private void createOccurenceTrees(DBMSForwardOptions options) {
        String s = kRootName;
        ObjectScope[] scope = options.getObjectsScope();

        // Create an empty panel...
        occurrenceTreePanel.add("empty", new JScrollPane()); //NOT LOCALIZABLE
        CardLayout layout = (CardLayout) occurrenceTreePanel.getLayout();
        cardLayoutLastItem++;
        cardLayoutComponentTable.put("empty", new Integer(cardLayoutLastItem - 1)); //NOT LOCALIZABLE, associate name & rank

        // For unowned objects...
        for (int i = 0; i < scope.length; i++) {
            ObjectScope concept = scope[i];
            if (!concept.isOwnedObject && concept.isSelected && concept.showOccurences) {
                s += "." + concept.conceptName;
                addComponentToCardLayout(occurrenceTreePanel, concept.conceptID, options,
                        concept.occurences);
            }
        } //end for

        // For owned objects...
        for (int i = 0; i < scope.length; i++) {
            ObjectScope concept = scope[i];
            boolean createOccurenceTree = false;
            if (concept.isOwnedObject && concept.isSelected && concept.showOccurences) {
                createOccurenceTree = true;
            } else if (concept.isOwnedObject && !concept.isSelected && concept.showOccurences
                    && concept.childrenID != null) {
                for (int z = 0; z < concept.childrenID.length; z++) {
                    ObjectScope child = ObjectScope.findConceptInObjectScopeWithConceptID(scope,
                            concept.childrenID[z]);
                    if (child != null && child.isSelected) {
                        createOccurenceTree = true;
                        break;
                    }
                }
            }
            if (createOccurenceTree) {
                for (int j = 0; j < concept.occurences.size(); j++) {
                    Object user = (Object) concept.occurences.get(j);
                    if (user instanceof UserInfo) {
                        addComponentToCardLayout(occurrenceTreePanel, ((UserInfo) user).id,
                                options, ((UserInfo) user).occurences);
                    }
                }
            }
        } //end for
    } //end createOccurenceTrees()

    //Sort objects by their physical name
    private void sortObjArrayByName(ArrayList arrayObjs) {
        Comparator comparator = new Comparator() {
            public int compare(Object obj1, Object obj2) {
                ObjectSelection sel1 = (ObjectSelection) obj1;
                ObjectSelection sel2 = (ObjectSelection) obj2;

                int comparaison = 0;
                String s1 = sel1.name.toUpperCase();
                String s2 = sel2.name.toUpperCase();
                comparaison = s1.compareTo(s2);

                return comparaison;
            }
        };

        Collections.sort(arrayObjs, comparator);
    } //end sortObjArrayByName()

    // Create the Occurences Tree for unowned and owned objects
    private void addComponentToCardLayout(JPanel panel, int id, DBMSForwardOptions options,
            ArrayList arrayObjs) {
        sortObjArrayByName(arrayObjs);
        CheckableTree occurrenceTree = new CheckableTree();
        occurrenceTree.setModel(new OccurrenceTreeModel(
                new DefaultMutableTreeNode(kRootName, true), arrayObjs, options));
        JScrollPane comp = new JScrollPane(occurrenceTree);
        occurrenceTree.setRootVisible(false);
        occurrenceTree.setShowsRootHandles(true);

        //add newly-created component to panel
        panel.add(String.valueOf(id), comp);
        CardLayout layout = (CardLayout) panel.getLayout();
        cardLayoutLastItem++;
        cardLayoutComponentTable.put(new Integer(id), new Integer(cardLayoutLastItem - 1)); //associate name & rank
    }

    private void showCardLayoutComponent(JPanel panel, int id) {
        CardLayout layout = (CardLayout) panel.getLayout();

        //another way to implement layout.show()
        Integer iPosition = (Integer) cardLayoutComponentTable.get(new Integer(id));

        //iPositon null if user selects a non-leaf node: do nothing in this case
        if (iPosition == null)
            iPosition = (Integer) cardLayoutComponentTable.get("empty"); //NOT LOCALIZABLE

        cardLayoutCurrentItem = iPosition.intValue();
        layout.first(panel);
        for (int i = 0; i < cardLayoutCurrentItem; i++) {
            layout.next(panel);
        }
        panel.repaint();
    }

    protected void initSpecific(DBMSForwardOptions options) {
    }

    protected void terminateSpecific(DBMSForwardOptions options) {
        ForwardToolkitPlugin toolkit = ForwardToolkitPlugin.getToolkit();
        toolkit.configureSpecificOptions(options);
    }

    public void treeOptionChanged(int conceptId, boolean selected) {
    }

    public void userTreeOptionChanged(int conceptId, boolean selected) {
    }

    // INNER CLASS
    private class UserTreeModel extends CheckTreeModel {
        UserTreeModel(DefaultMutableTreeNode root, DBMSForwardOptions options) {
            super(root);
            int id = 0;
            DefaultMutableTreeNode parent;
            ObjectScope[] scope = options.getObjectsScope();
            ObjectScope userScope = ObjectScope.findConceptInObjectScopeWithConceptName(scope,
                    DbORUser.metaClass.getGUIName());
            DbSMSAbstractPackage abstractPackage = options.getAbstractPackage();
            if (abstractPackage != null) {
                try {
                    abstractPackage.getDb().beginTrans(Db.READ_TRANS);
                    // First, add all unowned objects...
                    for (int i = 0; i < scope.length; i++) {
                        ObjectScope concept = scope[i];
                        if (!concept.isOwnedObject && concept.isSelected && concept.showOccurences) {
                            concept.conceptID = id;
                            id++;
                            parent = new DefaultMutableTreeNode(concept, true);
                            root.add(parent);
                        }
                    } //end for

                    // Second, create all user nodes...
                    ArrayList listNode = new ArrayList();
                    ArrayList userList = (userScope == null) ? null : userScope.occurences;
                    if (userList != null) {
                        for (int i = 0; i < userList.size(); i++) {
                            ObjectSelection userObj = (ObjectSelection) userScope.occurences.get(i);
                            userObj.id = id;
                            id++;
                            parent = new UserNode(userObj, true, userObj.getIsSelected());
                            ((UserNode) parent).setSelected(true);
                            if (userObj.occurence == null)
                                listNode.add(0, parent);
                            else
                                listNode.add(parent);
                        } //end for
                    } //end if

                    for (int i = 0; i < listNode.size(); i++) {
                        root.add((UserNode) listNode.get(i));
                    } //end for

                    // Third, add all owned objects associated with the user...
                    for (int i = 0; i < scope.length; i++) {
                        ObjectScope concept = scope[i];
                        boolean addConcept = false;
                        if (concept.isOwnedObject && concept.isSelected && concept.showOccurences) {
                            addConcept = true;
                        } else if (concept.isOwnedObject && !concept.isSelected
                                && concept.showOccurences && concept.childrenID != null) {
                            for (int z = 0; z < concept.childrenID.length; z++) {
                                ObjectScope child = ObjectScope
                                        .findConceptInObjectScopeWithConceptID(scope,
                                                concept.childrenID[z]);
                                if (child != null && child.isSelected) {
                                    addConcept = true;
                                    break;
                                }
                            }
                        } //end if

                        if (addConcept) {
                            // add username
                            for (int j = 0; j < concept.occurences.size(); j++) {
                                UserInfo userInfo = (UserInfo) concept.occurences.get(j);
                                userInfo.id = id;
                                id++;
                                // find the user node already created
                                int index = findUserNode(root, userInfo);
                                if (index == -1)
                                    continue;
                                else
                                    parent = (DefaultMutableTreeNode) root.getChildAt(index);

                                // add concept name
                                DefaultMutableTreeNode child = new DefaultMutableTreeNode(concept,
                                        true);
                                parent.add(child);
                            }
                        }
                    } //end for
                    options.getAbstractPackage().getDb().commitTrans();
                } catch (DbException e) {
                    ExceptionHandler.processUncatchedException(ApplicationContext
                            .getDefaultMainFrame(), e);
                } //end try
            } //end if
        } // end UserTreeModel()

        // Find a specific user node, return -1 if not exist
        private int findUserNode(DefaultMutableTreeNode root, UserInfo userInfo) {
            int count = getChildCount(root);
            for (int i = 0; i < count; i++) {
                Object node = (Object) getChild(root, i);
                if (node instanceof UserNode) {
                    if (((UserNode) node).userObj.occurence == userInfo.user)
                        return ((DefaultMutableTreeNode) root)
                                .getIndex((DefaultMutableTreeNode) node);
                }
            }
            return -1;
        }
    }

    // INNER CLASS
    private static final class UserNode extends CheckTreeNode {
        private ObjectSelection userObj;
        private int conceptId;

        public UserNode(ObjectSelection aUserObj, boolean allowchildren, boolean selected)
                throws DbException {
            super(aUserObj.occurence, allowchildren, selected);
            String name = null;
            if (aUserObj.occurence != null) {
                DbSemanticalObject semanticalObj = (DbSemanticalObject) aUserObj.occurence;
                name = aUserObj.name;
            } else
                name = kUnspecifiedUser;
            setDisplayText(name);
            this.userObj = aUserObj;
        }

        public int getConceptId() {
            return conceptId;
        }

        public void setSelected(boolean b) {
            userObj.setIsSelected(b);
            super.setSelected(b);
        }
    }

    // INNER CLASS
    private class OccurrenceTreeModel extends CheckTreeModel {
        OccurrenceTreeModel(DefaultMutableTreeNode root, ArrayList arrayObjs,
                DBMSForwardOptions options) {
            super(root);
            DefaultMutableTreeNode parent;
            try {
                options.getAbstractPackage().getDb().beginTrans(Db.READ_TRANS);
                for (int i = 0; i < arrayObjs.size(); i++) {
                    ObjectSelection obj = (ObjectSelection) arrayObjs.get(i);
                    parent = new OccurrenceNode(obj, 0, false, true);
                    root.add(parent);
                }
                options.getAbstractPackage().getDb().commitTrans();
            } catch (DbException e) {
                ExceptionHandler.processUncatchedException(
                        ApplicationContext.getDefaultMainFrame(), e);
            }
        }
    }

    // INNER CLASS
    private static final class OccurrenceNode extends CheckTreeNode {
        private ObjectSelection occurence;
        private int conceptId;

        public OccurrenceNode(ObjectSelection aOccurence, int aConceptId, boolean allowchildren,
                boolean selected) throws DbException {
            super(aOccurence, allowchildren, selected);
            conceptId = aConceptId;
            occurence = aOccurence;
            String name = null;
            if (occurence.occurence != null) {
                DbSemanticalObject semanticalObj = (DbSemanticalObject) occurence.occurence;
                name = aOccurence.name;
            } else
                name = kUnspecifiedUser;
            setDisplayText(name);
        }

        public int getConceptId() {
            return conceptId;
        }

        public void setSelected(boolean b) {
            occurence.setIsSelected(b);
            super.setSelected(b);
        }
    } //end OccurrenceNode

    private static final class FilterAttributesAction extends AbstractAction {
        WizardPage m_wizardPage = null;
        DBMSForwardOptions m_options = null;

        FilterAttributesAction() {
            super(kFilterAttribute + "...");
        }

        public void actionPerformed(ActionEvent ev) {
            if ((m_wizardPage != null) && (m_options != null)) {
                SMSIntegrateModelUtil util = SMSIntegrateModelUtil.getSingleInstance();
                DbSMSAbstractPackage abstractPackage = m_options.getAbstractPackage(); //either datamodel, procedure library or database
                try {
                    if (abstractPackage instanceof DbORDatabase) {
                        Db db = abstractPackage.getDb();
                        db.beginTrans(Db.READ_TRANS);
                        DbORDatabase database = (DbORDatabase) abstractPackage;
                        DbORDataModel datamodel = database.getSchema();
                        if (datamodel != null) {
                            abstractPackage = datamodel;
                        }
                        db.commitTrans();

                        boolean doDeepTraversal = true;
                        CheckTreeNode fieldTree = util.getFieldTree(abstractPackage,
                                doDeepTraversal); //abstractPackage either a datamodel or a database
                        File scopeParamFile = null; //may be null
                        String scopedir = "";
                        JDialog owner = m_wizardPage.getWizard();
                        IntegrateScopeDialog dialog = new IntegrateScopeDialog(fieldTree,
                                scopeParamFile, owner, scopedir, IntegrateScopeDialog.GENERATION);
                        dialog.pack();
                        dialog.setVisible(true);
                        //TODO : change button name?
                        m_options.setCheckTreeNode(fieldTree);
                    } //end if
                } catch (DbException ex) {
                    //should not happen : either datamodel, procedure library or database has a composite
                }
            } //end if
        } //end actionPerformed()

        void setOwner(WizardPage wizardPage) {
            m_wizardPage = wizardPage;
        }

        void setOptions(DBMSForwardOptions options) {
            m_options = options;
        }
    } //end FilterAttributesAction

    private static final class GenerationParametersAction extends AbstractAction {
        private VariableScope m_variableScope;
        private ArrayList m_propertyList;
        WizardPage m_wizardPage = null;

        GenerationParametersAction() {
            super(kGenerationParams + "...");
            ForwardToolkitPlugin toolkit = ForwardToolkitPlugin.getToolkit();
            PluginMgr mgr = PluginMgr.getSingleInstance();
            PluginsRegistry registry = mgr.getPluginsRegistry();
            Class fwdClass = toolkit.getForwardClass();
            List plugins = registry.getActivePluginInstances(fwdClass); 
            
            //ArrayList plugins = mgr.getPluginsRegistry().getActivePluginInstances(
            //        toolkit.getForwardClass());
            SQLForwardEngineeringPlugin sqlForward = null;
            if (plugins != null && plugins.size() > 0)
                sqlForward = (SQLForwardEngineeringPlugin) plugins.get(0);

            if (sqlForward == null) {
                setEnabled(false);
                return;
            }

            //Get forward's varList
            m_variableScope = sqlForward.getVarScope();

            //build a property list from variables
            m_propertyList = TemplateDialog.buildPropertyListFromVariables(m_variableScope);
            if (m_propertyList.isEmpty()) {
                this.setEnabled(false);
            }
        } //end GenerationParametersAction()

        void setOwner(WizardPage wizardPage) {
            m_wizardPage = wizardPage;
        }

        public void actionPerformed(ActionEvent ev) {
            if (!m_propertyList.isEmpty()) {
                //create property panel
                JDialog owner = m_wizardPage.getWizard();
                JDialog dialog2 = new PropertyDialog(owner, kGenerationParams, CLOSE,
                        m_propertyList);

                //and display it
                dialog2.pack();
                dialog2.setSize((int) (dialog2.getWidth() * 3), dialog2.getHeight()); //it was not wide enough
                AwtUtil.centerWindow(dialog2);
                dialog2.setVisible(true);

                //reset variables' value according property list
                TemplateDialog.resetValue(m_variableScope, m_propertyList);
            } //end if
        } //end actionPerformed()
    } //end GenerationParametersAction
} //end DefaultForwardWizardObjectsPage

