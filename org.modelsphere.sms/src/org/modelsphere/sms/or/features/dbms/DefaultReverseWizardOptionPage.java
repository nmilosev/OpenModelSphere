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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import org.modelsphere.jack.awt.tree.CheckEvent;
import org.modelsphere.jack.awt.tree.CheckListener;
import org.modelsphere.jack.awt.tree.CheckableTree;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.gui.wizard.WizardPage;
import org.modelsphere.jack.srtool.plugins.generic.dbms.ObjectScope;
import org.modelsphere.jack.srtool.plugins.generic.dbms.ObjectSelection;
import org.modelsphere.jack.srtool.plugins.generic.dbms.UserInfo;
import org.modelsphere.jack.srtool.services.ConnectionMessage;
import org.modelsphere.jack.srtool.services.NameListMessage;
import org.modelsphere.jack.srtool.services.ServiceProtocolList;
import org.modelsphere.jack.util.TargetRuntimeException;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.international.LocaleMgr;

/**
 * 
 * For each kind of database object (tablespace, table, index, etc.), a user can decide if he/she
 * wants to reverse it. Only selected kinds of objects will be reverse engineered.
 * 
 */
public class DefaultReverseWizardOptionPage extends WizardPage {
    private static final String kReverseObjectUser = LocaleMgr.screen.getString("ReverseUser");
    private static final String kObjectsHeader = LocaleMgr.screen.getString("ConcepttoReverse");
    private static final String kOptions = LocaleMgr.screen.getString("Options");

    private JPanel optionPanel = new JPanel(new GridBagLayout());
    private CheckableTree tree = new CheckableTree();
    private JScrollPane treeScrollPane = new JScrollPane(tree);
    private JLabel treeScrollPaneLabel = new JLabel(kObjectsHeader);

    private TitledBorder genericOptionsPanelBorder = new TitledBorder(kOptions);
    private JPanel genericOptionsPanel = new JPanel(new GridBagLayout());
    protected JCheckBox reverseObjectUserCheckBox = new JCheckBox(kReverseObjectUser);

    private DBMSReverseOptions options;

    public DefaultReverseWizardOptionPage() {
        super();
    }

    private void initGUI(DBMSReverseOptions options) {
        this.options = options;
        setLayout(new GridBagLayout());
        genericOptionsPanel.setBorder(genericOptionsPanelBorder);
        genericOptionsPanel.add(reverseObjectUserCheckBox, new GridBagConstraints(0, 0, 1, 1, 0.0,
                0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 12), 0,
                0));
        genericOptionsPanel.add(Box.createVerticalGlue(),
                new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                        GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        add(treeScrollPaneLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(12, 12, 0, 6), 0, 0));
        add(treeScrollPane, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, new Insets(0, 12, 13, 5), 0, 0));
        add(optionPanel, new GridBagConstraints(1, 0, 1, 2, 1.0, 1.0, GridBagConstraints.NORTH,
                GridBagConstraints.BOTH, new Insets(12, 6, 12, 12), 0, 0));

        JPanel specificPanel = getSpecificOptionsPanel();
        if (specificPanel != null) {
            optionPanel.add(genericOptionsPanel,
                    new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
                            GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
            optionPanel.add(specificPanel,
                    new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
                            GridBagConstraints.BOTH, new Insets(6, 0, 0, 0), 0, 0));
        } else {
            optionPanel.add(genericOptionsPanel,
                    new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
                            GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        }
    }

    protected JPanel getSpecificOptionsPanel() {
        return null;
    }

    public final boolean initialize(Object opt) {
        DBMSReverseOptions options = (DBMSReverseOptions) opt;
        initGUI(options);
        tree.setModel(new ScopeTreeModel(new DefaultMutableTreeNode("JTree", true), options)); // NOT LOCALIZABLE
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.addCheckListener(new CheckListener() {
            public void itemChecked(CheckEvent e) {
                updateNextButton();
            }
        });
        // init GUI from options
        reverseObjectUserCheckBox.setSelected(options.reverseObjectUser);

        updateNextButton();
        initSpecific(options);
        return true;
    }

    public boolean terminate(Object opt, boolean saveData) {
        DBMSReverseOptions options = (DBMSReverseOptions) opt;
        if (!saveData) {
            options = null;
            return true;
        }

        // save GUI setting
        options.reverseObjectUser = reverseObjectUserCheckBox.isSelected();
        ObjectScope userScope = null;
        userScope = ObjectScope.findConceptInObjectScopeWithConceptName(options.getObjectsScope(),
                DbORUser.metaClass.getGUIName());
        userScope.isSelected = options.reverseObjectUser;

        ReverseToolkitPlugin kit = ReverseToolkitPlugin.getToolkit();

        ConnectionMessage connectionMessage = options.getConnection();
        if (connectionMessage == null)
            return false;
        terminateSpecific(options);

        String requestFile = kit.getSQLFileName_Gets(options);
        options.setRequestFile(requestFile);

        NameListMessage nameListMessage = getNameList(options);
        ArrayList nameList = nameListMessage.nameList;
        if (nameList.size() == 0)
            return false;

        kit.processNameListMessage(options, nameListMessage);

        options.nameList = nameList; // store results in options

        restructureNameListData(options);

        optionPanel = null;
        options = null;
        return true;
    }

    // This method is also used by DefaultSynchroOptionPage
    static NameListMessage getNameList(DBMSReverseOptions options) {
        NameListMessage nameListMessage = null;
        String errMsg = null;
        Throwable th = null;
        try {
            String addressIP = ServiceProtocolList.getServerIP();
            int port = ServiceProtocolList.NAME_LIST_SERVICE;
            Socket s = new Socket(addressIP, port);
            ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());

            // Build a list of statement id (order in get file) to skip based on
            // the concept scope
            ObjectScope[] scope = options.getObjectsScope();
            ArrayList ignoredconceptsids = new ArrayList();
            for (int i = 0; i < scope.length; i++) {
                ObjectScope concept = scope[i];
                if (concept.SQLGetID != null
                        && !ObjectScope.verifyConceptSelection(scope, concept, false))
                    ignoredconceptsids.add(concept.SQLGetID);
            }

            ReverseToolkitPlugin tk = ReverseToolkitPlugin.getToolkit();
            String file = options.getRequestFile();
            URL url = tk.getClass().getResource(file);
            // TODO try to remove some parameters in this constructor ...
            // already contained in options
            nameListMessage = new NameListMessage(options.getConnection().connectionId, url, true,
                    ignoredconceptsids);

            output.writeObject(nameListMessage);
            ObjectInputStream input = new ObjectInputStream(s.getInputStream());
            nameListMessage = (NameListMessage) input.readObject();
            errMsg = nameListMessage.errorMessage;
        } catch (IOException ex) {
            errMsg = ex.toString();
            th = ex;
        } catch (ClassNotFoundException ex) {
            errMsg = ex.toString();
            th = ex;
        }

        if (errMsg != null) {
            Debug.trace(errMsg);
            if (th != null)
                throw new TargetRuntimeException(th);
            else {
                throw new RuntimeException(errMsg);
            }
        }

        return nameListMessage;
    }

    protected static void restructureNameListData(DBMSReverseOptions options) {
        ObjectScope[] scope = options.getObjectsScope();
        ObjectScope userScope = ObjectScope.findConceptInObjectScopeWithConceptName(scope,
                DbORUser.metaClass.getGUIName());
        if (userScope == null)
            return;

        // get user; if null, objects' names are qualified
        int nameSize = options.nameList.size();
        int id = ((Integer) userScope.SQLGetID).intValue();
        if (id >= nameSize) {
            return;
        }

        String user = (String) options.nameList.get(id);
        if (user != null) {
            int commaPos = user.lastIndexOf(","); // NOT LOCALIZABLE
            if (commaPos > 0) {
                user = user.substring(0, commaPos);
            }
            user = user.trim();
        }

        for (int i = 0; i < scope.length; i++) {
            ObjectScope concept = scope[i];
            concept.occurences.clear();

            if (concept.SQLGetID != null) {
                int id2 = ((Integer) concept.SQLGetID).intValue();
                if (id2 >= nameSize) {
                    break;
                }

                String s = (String) options.nameList.get(id2);
                if (s == null)
                    continue;
                StringTokenizer stok = new StringTokenizer(s, ","); // NOT
                // LOCALIZABLE
                while (stok.hasMoreTokens()) {
                    // For Owned Objects, use the username
                    if (concept.isOwnedObject) {
                        String owner = user;
                        String obj;

                        if (owner == null) {
                            String qualObj = stok.nextToken(); // get
                            // 'owner.objname'
                            if (qualObj.indexOf(".") != -1)
                                owner = qualObj.substring(0, qualObj.indexOf(".")); // NOT LOCALIZABLE
                            obj = qualObj.substring(qualObj.indexOf(".") + 1); // NOT
                            // LOCALIZABLE
                            if (owner != null)
                                owner = owner.trim();
                            else
                                owner = DBMSUtil.NULL_USER;
                        } else {
                            obj = stok.nextToken(); // get 'objname'
                        }
                        if (obj != null)
                            obj = obj.trim();

                        if (owner == null || owner.length() == 0)
                            owner = DBMSUtil.NULL_USER;

                        boolean found = false;
                        UserInfo findUser = null;
                        ObjectSelection occurence = new ObjectSelection(obj, 0);
                        for (int j = 0; j < concept.occurences.size(); j++) {
                            findUser = (UserInfo) concept.occurences.get(j);
                            if (findUser != null && findUser.username.equals(owner)) {
                                found = true;
                                break;
                            }
                        }

                        if (found)
                            findUser.occurences.add(occurence);
                        else {
                            // add the user in the user Scope (select only the
                            // user corresponding to the connected user name)
                            if (!ObjectScope.findOccurenceByName(userScope, owner)) {
                                ObjectSelection userOccurence = new ObjectSelection(owner, 0);
                                if (options.getConnection() != null
                                        && options.getConnection().userName != null
                                        && options.getConnection().userName.equalsIgnoreCase(owner)) {
                                    userOccurence.setIsSelected(true);
                                } else
                                    userOccurence.setIsSelected(false);
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
                        ObjectSelection occurence = new ObjectSelection(stok.nextToken(), 0);
                        concept.occurences.add(occurence);
                    }
                }
            }
        }

        // ensure that at least one user is selected in the scope (ie may be
        // unspecified or dbo)
        if (userScope != null && userScope.occurences != null) {
            if (userScope.occurences.size() == 1) {
                Object first = userScope.occurences.get(0);
                if (first instanceof ObjectSelection) {
                    ((ObjectSelection) first).setIsSelected(true);
                }
            }
        }

    }

    /**
     * This method is called after this page is added to the wizard.
     * 
     * This method should be used to init the GUI with the options.
     * 
     */
    protected void initSpecific(DBMSReverseOptions options) {
    }

    /**
     * This method is called before this page is removed from the wizard.
     * 
     * This method should be used to save GUI setting in the options and to remove any references
     * that may prevent garbage collection.
     * 
     */
    protected void terminateSpecific(DBMSReverseOptions options) {
    }

    private void updateNextButton() {
        if (options.synchro) {
            setNextEnabled(true);
            return;
        }

        boolean found = false;

        for (int i = 0; i < tree.getRowCount(); i++) {
            ScopeTreeNode scopeNode = (ScopeTreeNode) tree.getCheckTreeNodeForRow(i);
            ObjectScope opt = scopeNode.getObjectScope();
            if (opt.isSelected && tree.isEnabled(scopeNode)) {
                found = true;
                break;
            }
        }
        setNextEnabled(found);
    }

}
