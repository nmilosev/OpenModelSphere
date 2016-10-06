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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.modelsphere.jack.awt.tree.CheckTreeModel;
import org.modelsphere.jack.awt.tree.CheckTreeNode;
import org.modelsphere.jack.awt.tree.CheckableTree;
import org.modelsphere.jack.gui.wizard.WizardPage;
import org.modelsphere.jack.srtool.plugins.generic.dbms.ObjectScope;
import org.modelsphere.jack.srtool.plugins.generic.dbms.ObjectSelection;
import org.modelsphere.jack.srtool.plugins.generic.dbms.UserInfo;
import org.modelsphere.jack.srtool.services.ConnectionMessage;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.international.LocaleMgr;

/**
 * 
 * For each kind of database object (tablespace, table, index, etc.), a user can decide if he/she
 * wants to reverse it. Only selected kinds of objects will be reverse engineered.
 * 
 */
public class DefaultReverseWizardObjectsPage extends WizardPage {
    private static final String kRootName = LocaleMgr.screen.getString("Root");
    private static final String kSave = LocaleMgr.screen.getString("Save");
    private static final String kSaveReverseOptions = LocaleMgr.screen
            .getString("SaveReverseOptions");
    private static final String kReverse = LocaleMgr.screen.getString("Reverse");
    private static final String kObjectsHeader = LocaleMgr.screen.getString("ObjectstoReverse");
    private static final String kOccurenceHeader = LocaleMgr.screen
            .getString("SelectedUserObjects");

    // GUI Components
    private GridBagLayout thisLayout = new GridBagLayout();
    private CardLayout buttonsPanelLayout = new CardLayout();
    private JPanel buttonsPanel = new JPanel(buttonsPanelLayout);
    private GridBagLayout objectsPanelLayout = new GridBagLayout();
    private JPanel objectsPanel = new JPanel(objectsPanelLayout);
    private CheckableTree userTree = new CheckableTree();
    private JScrollPane userTreeScrollPane = new JScrollPane(userTree);
    private JLabel userTreeScrollPaneLabel = new JLabel(kObjectsHeader);

    private JPanel occurrenceTreePanel = new JPanel();
    private JLabel occurrencePaneLabel = new JLabel(kOccurenceHeader);
    private CardLayout cardLayoutOccurrence = new CardLayout();

    int cardLayoutCurrentItem = 0, cardLayoutLastItem = 0;
    Hashtable cardLayoutComponentTable = new Hashtable();

    public DefaultReverseWizardObjectsPage() {
        super();
    }

    private void initGUI() {
        setLayout(thisLayout);
        occurrenceTreePanel.setLayout(cardLayoutOccurrence);
        buttonsPanel.setBorder(BorderFactory.createEtchedBorder());

        this.add(userTreeScrollPaneLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(12, 12, 0, 6),
                0, 0));
        this.add(userTreeScrollPane,
                new GridBagConstraints(0, 1, 1, 1, 0.4, 1.0, GridBagConstraints.CENTER,
                        GridBagConstraints.BOTH, new Insets(0, 12, 12, 6), 20, 0));
        this.add(occurrencePaneLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(12, 6, 0, 12),
                0, 0));
        this.add(objectsPanel,
                new GridBagConstraints(1, 1, 1, 1, 0.6, 1.0, GridBagConstraints.CENTER,
                        GridBagConstraints.BOTH, new Insets(0, 6, 12, 12), 80, 0));
        objectsPanel.add(occurrenceTreePanel, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        setNextEnabled(true);
    }

    public final boolean initialize(Object opt) {
        DBMSReverseOptions options = (DBMSReverseOptions) opt;
        initGUI();
        initTrees(options);
        initSpecific(options);
        return true;
    }

    private void initTrees(final DBMSReverseOptions options) {
        // Create user tree
        userTree.setModel(new UserTreeModel(new DefaultMutableTreeNode(kRootName, true), options));
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
    private void redrawOccurrencePanel(TreePath path, DBMSReverseOptions options) {
        if (path == null)
            return;
        Object component = path.getLastPathComponent();
        if (component != null) {
            String compName = getComponentName(path.toString());
            showCardLayoutComponent(occurrenceTreePanel, compName);
        }
    }

    // Create all occurence trees
    private void createOccurenceTrees(DBMSReverseOptions options) {
        String s = kRootName;
        ObjectScope[] scope = options.getObjectsScope();

        // Create an empty panel...
        occurrenceTreePanel.add("empty", new JScrollPane()); // NOT LOCALIZABLE
        CardLayout layout = (CardLayout) occurrenceTreePanel.getLayout();
        cardLayoutLastItem++;
        cardLayoutComponentTable.put("empty", new Integer(cardLayoutLastItem - 1)); // associate name & rank // NOT
        // LOCALIZABLE

        // For unowned objects...
        for (int i = 0; i < scope.length; i++) {
            ObjectScope concept = scope[i];
            if (!concept.isOwnedObject && concept.isSelected && concept.showOccurences) {
                s += "." + concept.conceptName; // NOT LOCALIZABLE
                addComponentToCardLayout(occurrenceTreePanel, s, options, concept.conceptName,
                        concept.occurences);
            }
        }

        // For owned objects...
        for (int i = 0; i < scope.length; i++) {
            ObjectScope concept = scope[i];
            if (concept.isOwnedObject && concept.isSelected && concept.showOccurences) {

                for (int j = 0; j < concept.occurences.size(); j++) {
                    Object user = (Object) concept.occurences.get(j);
                    if (user instanceof UserInfo) {
                        String qObj = kRootName + "." + ((UserInfo) user).username + "."
                                + concept.conceptName; // NOT LOCALIZABLE
                        addComponentToCardLayout(occurrenceTreePanel, qObj, options,
                                ((UserInfo) user).username, ((UserInfo) user).occurences);
                    }
                }
            }
        }
    }

    // Create the Occurences Tree for unowned and owned objects
    private void addComponentToCardLayout(JPanel panel, String name, DBMSReverseOptions options,
            String objName, ArrayList arrayObjs) {

        CheckableTree occurrenceTree = new CheckableTree();
        occurrenceTree.setModel(new OccurrenceTreeModel(
                new DefaultMutableTreeNode(kRootName, true), arrayObjs));
        JScrollPane comp = new JScrollPane(occurrenceTree);
        occurrenceTree.setRootVisible(false);
        occurrenceTree.setShowsRootHandles(true);

        // add newly-created component to panel
        panel.add(name, comp);
        CardLayout layout = (CardLayout) panel.getLayout();
        cardLayoutLastItem++;
        cardLayoutComponentTable.put(name, new Integer(cardLayoutLastItem - 1)); // associate
        // name
        // &
        // rank
    }

    private void showCardLayoutComponent(JPanel panel, String key) {
        CardLayout layout = (CardLayout) panel.getLayout();

        // another way to implement layout.show()
        Integer iPosition = (Integer) cardLayoutComponentTable.get(key);

        // iPositon null if user selects a non-leaf node: do nothing in this
        // case
        if (iPosition == null)
            iPosition = (Integer) cardLayoutComponentTable.get("empty"); // NOT
        // LOCALIZABLE

        cardLayoutCurrentItem = iPosition.intValue();
        layout.first(panel);
        for (int i = 0; i < cardLayoutCurrentItem; i++) {
            layout.next(panel);
        }
        panel.repaint();
    }

    private String getComponentName(String pathName) {
        String compName = "";

        StringTokenizer st = new StringTokenizer(pathName, "["); // NOT
        // LOCALIZABLE
        while (st.hasMoreTokens()) {
            String s = st.nextToken(",]"); // NOT LOCALIZABLE
            int pos = s.indexOf("["); // NOT LOCALIZABLE
            if (pos != -1)
                s = s.substring(pos + 1);
            compName += s.trim();
            if (st.hasMoreTokens())
                compName += "."; // NOT LOCALIZABLE
        }
        return compName;
    }

    public final boolean terminate(Object opt, boolean saveData) {
        DBMSReverseOptions options = (DBMSReverseOptions) opt;
        if (!saveData)
            return true;
        // saveTreeOption((TreeNode)tree.getModel().getRoot());
        // TODO release cross-link objects

        ConnectionMessage connectionMessage = options.getConnection();
        ReverseToolkitPlugin kit = ReverseToolkitPlugin.getToolkit();
        if (connectionMessage == null)
            return false;

        String sQLFileName_Xtr = kit.getSQLFileName_Xtr(options);
        options.setRequestFile(sQLFileName_Xtr);

        terminateSpecific(options);
        return true;
    }

    protected void initSpecific(DBMSReverseOptions options) {
    }

    protected void terminateSpecific(DBMSReverseOptions options) {
    }

    public void treeOptionChanged(int conceptId, boolean selected) {
    }

    public void userTreeOptionChanged(int conceptId, boolean selected) {
    }

    // INNER CLASS
    private class UserTreeModel extends CheckTreeModel {
        UserTreeModel(DefaultMutableTreeNode root, DBMSReverseOptions options) {
            super(root);

            DefaultMutableTreeNode parent;
            ObjectScope[] scope = options.getObjectsScope();
            ObjectScope userScope = ObjectScope.findConceptInObjectScopeWithConceptName(scope,
                    DbORUser.metaClass.getGUIName());

            // First, add all unowned objects...
            for (int i = 0; i < scope.length; i++) {
                ObjectScope concept = scope[i];
                if (!concept.isOwnedObject && concept.isSelected && concept.showOccurences) {
                    parent = new DefaultMutableTreeNode(concept, true);
                    root.add(parent);
                }
            }

            // Second, create all user nodes...
            for (int i = 0; i < userScope.occurences.size(); i++) {
                ObjectSelection userObj = (ObjectSelection) userScope.occurences.get(i);
                parent = new UserNode(userObj, 0, true, userObj.getIsSelected());
                // ((UserNode)parent).setSelected(true);
                root.add(parent);
            }

            // Third, add all owned objects associated with the user...
            for (int i = 0; i < scope.length; i++) {
                ObjectScope concept = scope[i];
                if (concept.isOwnedObject && concept.isSelected && concept.showOccurences) {
                    // add username
                    for (int j = 0; j < concept.occurences.size(); j++) {
                        UserInfo userInfo = (UserInfo) concept.occurences.get(j);
                        // find the user node already created
                        int index = findUserNode(root, userInfo);
                        if (index == -1)
                            continue;
                        else
                            parent = (DefaultMutableTreeNode) root.getChildAt(index);

                        // add concept name
                        DefaultMutableTreeNode child = new DefaultMutableTreeNode(concept, true);
                        parent.add(child);
                    }
                }
            }
        }

        // Find a specific user node, return -1 if not exist
        private int findUserNode(DefaultMutableTreeNode root, UserInfo userInfo) {
            int count = getChildCount(root);
            for (int i = 0; i < count; i++) {
                Object node = (Object) getChild(root, i);
                if (node instanceof UserNode) {
                    if (((UserNode) node).userObj.name.equals(userInfo.username))
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

        public UserNode(ObjectSelection aUserObj, int aConceptId, boolean allowchildren,
                boolean selected) {
            super(aUserObj.name, allowchildren, selected);
            this.userObj = aUserObj;
            this.conceptId = aConceptId;
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
        OccurrenceTreeModel(DefaultMutableTreeNode root, ArrayList arrayObjs) {
            super(root);
            DefaultMutableTreeNode parent;

            for (int i = 0; i < arrayObjs.size(); i++) {
                ObjectSelection obj = (ObjectSelection) arrayObjs.get(i);
                parent = new OccurrenceNode(obj, 0, false, true);
                root.add(parent);
            }
        }
    }

    // INNER CLASS
    private static final class OccurrenceNode extends CheckTreeNode {
        private ObjectSelection occurence;
        private int conceptId;

        public OccurrenceNode(ObjectSelection aOccurence, int aConceptId, boolean allowchildren,
                boolean selected) {
            super(aOccurence, allowchildren, selected);
            conceptId = aConceptId;
            occurence = aOccurence;
        }

        public int getConceptId() {
            return conceptId;
        }

        public void setSelected(boolean b) {
            occurence.setIsSelected(b);
            super.setSelected(b);
        }
    }
}
