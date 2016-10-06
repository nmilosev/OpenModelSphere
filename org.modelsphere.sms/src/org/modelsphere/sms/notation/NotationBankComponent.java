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

package org.modelsphere.sms.notation;

//Java
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.screen.DbTreeLookupDialog;
import org.modelsphere.jack.baseDb.screen.LookupDialog;
import org.modelsphere.jack.baseDb.screen.model.DbTreeModel;
import org.modelsphere.jack.baseDb.screen.model.DbTreeModelListener;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyManager;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DbApplication;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.CollationComparator;
import org.modelsphere.jack.util.StringUtil;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.util.BEUtility;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSNotation;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.util.DbInitialization;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORNotation;

public class NotationBankComponent extends JPanel implements DbRefreshListener,
        TreeSelectionListener, TreeModelListener {

    private static final String BTN_NEW = LocaleMgr.screen.getString("new");
    private static final String BTN_DELETE = LocaleMgr.screen.getString("Delete");
    private static final String BTN_IMPORT = LocaleMgr.screen.getString("import");
    private static final String BTN_USED = LocaleMgr.screen.getString("usedInDiagrams");
    private static final String NOTATION_ADD = LocaleMgr.action.getString("addNotation");
    private static final String NEW_NOTATION = LocaleMgr.misc.getString("newNotation"); // TODO: should be set in DbORNotation
    private static final String DELETE_NOTATION = LocaleMgr.action.getString("deleteNotation");
    private static final String RENAME_NOTATION = LocaleMgr.action.getString("renameNotation");
    private static final String IMPORT_NOTATION = LocaleMgr.action.getString("importNotation");
    private static final String WARNING = LocaleMgr.screen.getString("warning");
    private static final String DELETE_USED_NOTATION = LocaleMgr.message
            .getString("deleteUsedNotation");
    private static final String DELETE_DEFAULT_NOTATION = LocaleMgr.message
            .getString("deleteDefaultNotation");
    private static final String NOTATION_MODIFICATION = LocaleMgr.action
            .getString("updateNotation");
    private static final String SELECT_SOURCE_NOTATION = LocaleMgr.screen
            .getString("selectSourceNotations");
    private static final String kDiagsUsingNotation = LocaleMgr.screen
            .getString("diagsUsingNotation");
    private static final String kCancel = LocaleMgr.screen.getString("Cancel");
    private static final String kConceptualMode = LocaleMgr.message.getString("conceptualMode");
    private static final String kRelationalMode = LocaleMgr.message.getString("relationalMode");
    private static final String kMsgNewNotationMode = LocaleMgr.message
            .getString("msgNewNotationMode");

    private JButton newBtn = new JButton(BTN_NEW);
    private JButton deleteBtn = new JButton(BTN_DELETE);
    private JButton importBtn = new JButton(BTN_IMPORT);
    private JButton usedBtn = new JButton(BTN_USED);
    private JButton[] jButtonList = new JButton[] { newBtn, deleteBtn, importBtn };
    private String nodeName;
    private MetaClass notationClass;
    private DbProject project;
    private NotationFrame notationFrame;
    private NotationNode notationNode;
    private NotationTree notationTree;
    private NotationComponent notationComponent;
    private DefaultTreeModel notationTreeModel;
    private boolean m_isUML = false;

    private TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

    public NotationBankComponent(NotationFrame notationFrame, MetaClass notationClass,
            String[] optionsName, boolean isUML) {
        this.notationFrame = notationFrame;
        this.notationClass = notationClass;
        this.nodeName = optionsName[2];
        this.m_isUML = isUML;
        if (isUML) {
            newBtn.setEnabled(false);
        }
        jbInit();
    }

    private void jbInit() {
        this.setLayout(new BorderLayout());
        /** Notation Tree */
        notationNode = new NotationNode("Notations"); // NOT LOCALIZABLE
        notationTreeModel = new NotationDefaultTreeModel(notationNode);
        notationTreeModel.addTreeModelListener(this);

        notationTree = new NotationTree(notationTreeModel);
        notationTree.addTreeSelectionListener(this);
        this.add(new JScrollPane(notationTree), BorderLayout.CENTER);

        /** Tree control panel */
        JPanel ctrlPanel = new JPanel(new BorderLayout());
        this.add(ctrlPanel, BorderLayout.SOUTH);
        JPanel btnPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnPanel1.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        btnPanel1.add(deleteBtn);
        if (!m_isUML) {
            btnPanel1.add(newBtn);
        }
        btnPanel1.add(importBtn);
        ctrlPanel.add(btnPanel1, BorderLayout.NORTH);
        JPanel btnPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnPanel2.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        btnPanel2.add(usedBtn);
        ctrlPanel.add(btnPanel2, BorderLayout.SOUTH);
        AwtUtil.normalizeComponentDimension(jButtonList);

        newBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newbtn_actionPerformed(e);
            }
        });
        deleteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deletebtn_actionPerformed(e);
            }
        });
        deleteBtn.setEnabled(false);
        importBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                importBtn_actionPerformed(e);
            }
        });
        usedBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                usedBtn_actionPerformed(e);
            }
        });
        usedBtn.setEnabled(false);
    }

    public void setProject(NotationComponent currentNotationComponent, DbObject notation,
            DbProject aProject) throws DbException {
        notationComponent = currentNotationComponent;
        project = aProject;
        loadNotationBank();
    }

    // Create all notation nodes
    private void loadNotationBank() throws DbException {
        NotationNode componentNode = null;

        DbEnumeration dbEnum = project.getComponents().elements(notationClass);
        while (dbEnum.hasMoreElements()) {
            DbObject notation = dbEnum.nextElement();
            if (notation.getClass() == notationClass.getJClass()) {
                if (notation instanceof DbBENotation) {
                    int masterNotationID = ((DbBENotation) notation).getMasterNotationID()
                            .intValue();
                    if (m_isUML == true) {
                        if (masterNotationID >= 13 && masterNotationID <= 19) {
                            componentNode = new NotationNode(notation);
                            loadNotationComponentNode(componentNode);
                            notationNode.add(componentNode);
                        }
                    } else {
                        if (!(masterNotationID >= 13 && masterNotationID <= 19)) {
                            componentNode = new NotationNode(notation);
                            loadNotationComponentNode(componentNode);
                            notationNode.add(componentNode);
                        }
                    }
                } else {
                    componentNode = new NotationNode(notation);
                    loadNotationComponentNode(componentNode);
                    notationNode.add(componentNode);
                }
            }
        }
        dbEnum.close();
        // To ensure that the RootNode is expended because it is not visible
        notationTree.expandPath(new TreePath(notationNode));
    }

    // Create all leaves (options) for a Notation node
    private void loadNotationComponentNode(NotationNode componentNode) throws DbException {
        String[] optionGroupHeaders;
        NotationNode subComponentNode = null;
        DbObject notation = (DbObject) componentNode.getUserObject();
        if (notation instanceof DbBENotation) {
            DbBENotation benota = BEUtility.getSingleInstance().getMasterNotation(
                    (DbBENotation) notation);
            Terminology terminology = TerminologyManager.getInstance().findTerminologyByName(
                    benota.getName());
            optionGroupHeaders = new String[] { terminology.getTerm(DbBEUseCase.metaClass),
                    terminology.getTerm(DbBEActor.metaClass),
                    terminology.getTerm(DbBEStore.metaClass),
                    terminology.getTerm(DbBEFlow.metaClass) };
        } else
            optionGroupHeaders = NotationFrame.getOptionGroupHeaders(notationClass.getJClass(),
                    nodeName);

        for (int i = 0; i < optionGroupHeaders.length; i++) {
            subComponentNode = new NotationNode(optionGroupHeaders[i]);
            componentNode.add(subComponentNode);
        }
    }

    // Create the new notation at the end of the Tree
    private void createNewNotation(DbObject newDbNotation) throws DbException {
        int i = notationTreeModel.getChildCount(notationNode);
        NotationNode newNotationComponent = new NotationNode(newDbNotation);

        loadNotationComponentNode(newNotationComponent);
        notationTreeModel.insertNodeInto(newNotationComponent, notationNode, i);
    }

    private void newbtn_actionPerformed(ActionEvent e) {
        try {
            project.getDb().beginTrans(Db.WRITE_TRANS, NOTATION_ADD);
            DbObject newNotation = project.createComponent(notationClass);
            newNotation.set(DbSMSNotation.fName, NEW_NOTATION); // TODO: Move
            // this value in
            // init default
            // value of
            // DbSMSNotation
            if (newNotation instanceof DbORNotation) {
                int answer = JOptionPane.showOptionDialog(ApplicationContext.getDefaultMainFrame(),
                        kMsgNewNotationMode, ApplicationContext.getApplicationName(),
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                        new String[] { kConceptualMode, kRelationalMode, kCancel }, kCancel);
                if (answer == JOptionPane.YES_OPTION) {// Conceptual Mode
                    ((DbORNotation) newNotation)
                            .setNotationMode(new Integer(DbSMSNotation.ER_MODE));
                } else if (answer == JOptionPane.NO_OPTION) {// Relational Mode
                    ((DbORNotation) newNotation)
                            .setNotationMode(new Integer(DbSMSNotation.OR_MODE));
                } else if (answer == JOptionPane.CANCEL_OPTION) {
                    project.getDb().abortTrans();
                    return;
                }
                ((DbORNotation) newNotation).initOptions();
            } else {
                ((DbBENotation) newNotation).setNotationMode(new Integer(DbSMSNotation.BE_MODE));
                ((DbBENotation) newNotation).initOptions();
            }
            newNotation.set(DbSMSNotation.fNotationID, new Integer(DbInitialization
                    .getNextNewNotationId()));
            newNotation.set(DbSMSNotation.fMasterNotationID, new Integer(
                    DbInitialization.GANE_SARSON));
            createNewNotation(newNotation);
            project.getDb().commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(notationFrame, ex);
        }
    }

    private void deletebtn_actionPerformed(ActionEvent e) {
        try {
            Object parentNodeInfo;
            NotationNode parentNode;
            NotationNode selectedNode = (NotationNode) notationTree.getLastSelectedPathComponent();

            if (selectedNode == null)
                return;
            if (selectedNode.isLeaf())
                parentNode = (NotationNode) selectedNode.getParent();
            else
                parentNode = selectedNode;
            parentNodeInfo = parentNode.getUserObject();

            // check if the selected notation is used or is the default notation
            // for the project
            project.getDb().beginTrans(Db.READ_TRANS);
            DbObject notation = (DbObject) parentNodeInfo;
            DbObject projectdefaultnotation = ((DbSMSProject) project).getOrDefaultNotation();
            int nblinkeddiagram = 0;
            if (notation instanceof DbORNotation)
                nblinkeddiagram = notation.getNbNeighbors(DbORNotation.fDiagrams);
            else
                nblinkeddiagram = notation.getNbNeighbors(DbBENotation.fDiagrams);
            project.getDb().commitTrans();

            if (projectdefaultnotation == notation) {
                JOptionPane.showMessageDialog(notationFrame, DELETE_DEFAULT_NOTATION,
                        ApplicationContext.getApplicationName(), JOptionPane.OK_OPTION);
                return;
            }

            if (nblinkeddiagram != 0) {
                int userResponse = JOptionPane.showConfirmDialog(notationFrame,
                        DELETE_USED_NOTATION, WARNING, JOptionPane.YES_NO_OPTION);
                if (userResponse == JOptionPane.NO_OPTION) {
                    return;
                }
            }

            project.getDb().beginTrans(Db.WRITE_TRANS, DELETE_NOTATION);
            // just to be sure! (1,1) relation can cascade the delete on the
            // project
            if (((DbSMSProject) project).getOrDefaultNotation() == notation) {
                project.getDb().abortTrans();
            } else {
                // if the notation is UML, reset the notation to the
                // corresponding master notation
                if (notation instanceof DbBENotation) {
                    DbBENotation beNotation = BEUtility.getSingleInstance().getMasterNotation(
                            (DbBENotation) notation);
                    DbEnumeration dbenumdiagrams = ((DbBENotation) notation).getDiagrams()
                            .elements();
                    while (dbenumdiagrams.hasMoreElements()) {
                        DbBEDiagram diagram = (DbBEDiagram) dbenumdiagrams.nextElement();
                        diagram.setNotation(beNotation);
                    }
                    dbenumdiagrams.close();
                }
                notation.remove();
                project.getDb().commitTrans();
            }

            // delete this notation from the tree
            notationTreeModel.removeNodeFromParent(parentNode);
            notationComponent.nameTextField.setText("");
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(notationFrame, ex);
        }
    }

    // Shows in a list all the diagrams using the selected notation.
    private void usedBtn_actionPerformed(ActionEvent e) {
        try {
            NotationNode selectedNode = (NotationNode) notationTree.getLastSelectedPathComponent();
            if (selectedNode == null)
                return;
            NotationNode parentNode = (selectedNode.isLeaf() ? (NotationNode) selectedNode
                    .getParent() : selectedNode);
            DbSMSNotation notation = (DbSMSNotation) parentNode.getUserObject();

            notation.getDb().beginTrans(Db.READ_TRANS);
            ArrayList diagList = new ArrayList();
            // Add to diagList all the diagrams linked to this notation.
            DbEnumeration dbEnum = null;
            if (notation instanceof DbORNotation)
                dbEnum = ((DbORNotation) notation).getDiagrams().elements();
            else
                dbEnum = ((DbBENotation) notation).getDiagrams().elements();
            while (dbEnum.hasMoreElements())
                diagList.add(dbEnum.nextElement());
            dbEnum.close();
            // If default notation, add to diagList all the diagrams that are
            // not linked to a notation.
            if (notation instanceof DbORNotation
                    && ((DbORNotation) notation).getReferringProjectOr() != null) {
                dbEnum = project.componentTree(DbORDataModel.metaClass);
                while (dbEnum.hasMoreElements()) {
                    DbEnumeration enum2 = dbEnum.nextElement().getComponents().elements(
                            DbORDiagram.metaClass);
                    while (enum2.hasMoreElements()) {
                        DbORDiagram diag = (DbORDiagram) enum2.nextElement();
                        if (diag.getNotation() == null)
                            diagList.add(diag);
                    }
                    enum2.close();
                }
                dbEnum.close();
            } else if (notation instanceof DbBENotation
                    && ((DbBENotation) notation).getReferringProjectBe() != null) {
                dbEnum = project.componentTree(DbBEModel.metaClass);
                while (dbEnum.hasMoreElements()) {
                    DbEnumeration enum2 = dbEnum.nextElement().getComponents().elements(
                            DbBEDiagram.metaClass);
                    while (enum2.hasMoreElements()) {
                        DbBEDiagram diag = (DbBEDiagram) enum2.nextElement();
                        if (diag.getNotation() == null)
                            diagList.add(diag);
                    }
                    enum2.close();
                }
                dbEnum.close();
            }

            String[] items = new String[diagList.size()];
            Iterator iter = diagList.iterator();
            for (int i = 0; iter.hasNext(); i++) {
                DbSMSDiagram diag = (DbSMSDiagram) iter.next();
                String packName = diag.getComposite().getSemanticalName(DbObject.LONG_FORM);
                int ind = packName.lastIndexOf('.');
                packName = packName + '.' + diag.getName();
                if (ind != -1)
                    packName = packName.substring(ind + 1) + " (" + packName.substring(0, ind)
                            + ")"; // NOT
                // LOCALIZABLE
                items[i] = packName;
            }
            String title = MessageFormat.format(kDiagsUsingNotation, new Object[] { notation
                    .getName() });
            notation.getDb().commitTrans(); // close transaction before showing
            // dialog

            Comparator comparator = new CollationComparator();
            Arrays.sort(items, comparator);
            LookupDialog dialog = new LookupDialog(notationFrame, title, null, items, -1,
                    LookupDialog.SELECT_NONE, comparator);
            dialog.setVisible(true);
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(notationFrame, ex);
        }
    }

    private static final String COPY_OF = org.modelsphere.sms.international.LocaleMgr.message
            .getString("CopyOf");

    private void importBtn_actionPerformed(ActionEvent e) {
        DbObject[] notations = showNotationsLookupDialog();
        if (notations != null && notations.length != 0) {
            try {
                project.getDb().beginTrans(Db.WRITE_TRANS, IMPORT_NOTATION);
                int i;
                for (i = 0; i < notations.length; i++) {
                    DbObject srcNotation = notations[i];
                    srcNotation.getDb().beginTrans(Db.READ_TRANS);
                    DbObject newNotation = project.createComponent(notationClass);
                    String name = srcNotation.getName();
                    name = MessageFormat.format(COPY_OF, new String[] { name });
                    newNotation.setName(name);
                    if (newNotation instanceof DbORNotation) {
                        ((DbORNotation) newNotation).copyOptions((DbORNotation) srcNotation);
                        ((DbORNotation) newNotation).setNotationMode(((DbORNotation) srcNotation)
                                .getNotationMode());
                    } else
                        ((DbBENotation) newNotation).copyOptions((DbBENotation) srcNotation);

                    newNotation.set(DbSMSNotation.fNotationID, new Integer(DbInitialization
                            .getNextNewNotationId()));
                    newNotation.set(DbSMSNotation.fMasterNotationID, ((DbSMSNotation) srcNotation)
                            .getMasterNotationID());
                    createNewNotation(newNotation);
                }
                for (i = 0; i < notations.length; i++)
                    notations[i].getDb().commitTrans();
                project.getDb().commitTrans();
            } catch (Exception ex) {
                org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(notationFrame,
                        ex);
            }
        }
    }

    private DbObject[] showNotationsLookupDialog() {

        NotationNode selectedNode = (NotationNode) notationTree.getLastSelectedPathComponent();
        NotationNode parentNode = null;

        if (selectedNode != null) {
            if (selectedNode.isLeaf())
                parentNode = (NotationNode) selectedNode.getParent();
            else
                parentNode = selectedNode;
        } // end if

        DbObject dbNotation = (parentNode == null) ? null : (DbObject) parentNode.getUserObject();
        DbProject projs[] = DbApplication.getProjects();
        Object obj = null;
        try {
            DbTreeModel model = new DbTreeModel(projs, new MetaClass[] { notationClass },
                    (DbTreeModelListener) null, null);
            if (!m_isUML) {
                DbTreeLookupDialog treeLookupDialog = terminologyUtil.getTreeLookupDialog(this,
                        SELECT_SOURCE_NOTATION, model, false, false);
                obj = treeLookupDialog.selectOneUML(this, SELECT_SOURCE_NOTATION, projs,
                        new MetaClass[] { notationClass }, (DbTreeModelListener) null, null,
                        dbNotation, false);
            } else {
                DbTreeLookupDialog treeLookupDialog = terminologyUtil.getTreeLookupDialog(this,
                        SELECT_SOURCE_NOTATION, model, false, true);
                obj = treeLookupDialog.selectOneUML(this, SELECT_SOURCE_NOTATION, projs,
                        new MetaClass[] { notationClass }, (DbTreeModelListener) null, null,
                        dbNotation, true);
            }
        } catch (DbException dbe) {
        }

        DbObject notation = (obj instanceof DbObject) ? (DbObject) obj : null;
        DbObject[] notations = (notation == null) ? null : new DbObject[] { notation };
        return notations;
    } // end showNotationsLookupDialog()

    // TO DO: use this method to refresh the tree....
    public final void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
        if (!event.dbo.hasField(DbSMSNotation.fName))
            return;

        DbObject notation = event.dbo;
        if (notation.getTransStatus() == Db.OBJ_ADDED) {
        } else if (notation.getTransStatus() == Db.OBJ_REMOVED) {
        } else { // rename...
        }
    }

    // ///////////////////////////////////////
    // TreeSelectionListener SUPPORT
    //
    public void valueChanged(TreeSelectionEvent e) {
        Object parentNodeInfo = null;
        NotationNode selectedNode = (NotationNode) notationTree.getLastSelectedPathComponent();

        // nothing selected
        if (selectedNode == null) {
            deleteBtn.setEnabled(false);
            usedBtn.setEnabled(false);
            return;
        }
        try {
            if (selectedNode.isLeaf()) {
                NotationNode parentNode = (NotationNode) selectedNode.getParent();
                parentNodeInfo = parentNode.getUserObject();

                if (parentNodeInfo instanceof DbObject) {
                    ((DbObject) parentNodeInfo).getDb().beginTrans(Db.READ_TRANS);
                    notationComponent.setNotation((DbObject) parentNodeInfo, selectedNode, false);
                    ((DbObject) parentNodeInfo).getDb().commitTrans();
                }
                notationTree.setEditable(false);
            } else {
                parentNodeInfo = selectedNode.getUserObject();
                ((DbSMSNotation) parentNodeInfo).getDb().beginTrans(Db.READ_TRANS);
                notationComponent.setNotation((DbObject) parentNodeInfo);
                ((DbSMSNotation) parentNodeInfo).getDb().commitTrans();
                notationTree.setEditable(true);
            }
            // Set the new selected notation in notationFrame
            notationFrame.setNotation((DbSMSNotation) parentNodeInfo);

            // Enables (or disables) the buttons.
            usedBtn.setEnabled(true);
            ((DbObject) parentNodeInfo).getDb().beginTrans(Db.READ_TRANS);
            if (parentNodeInfo instanceof DbORNotation)
                deleteBtn.setEnabled(!selectedNode.isLeaf()
                        && !((DbORNotation) parentNodeInfo).isBuiltIn());
            else
                deleteBtn.setEnabled(!selectedNode.isLeaf()
                        && !((DbBENotation) parentNodeInfo).isBuiltIn());
            ((DbSMSNotation) parentNodeInfo).getDb().commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(notationFrame, ex);
        }
    }

    //
    // End of TreeSelectionListener SUPPORT
    // ///////////////////////////////////////

    // ///////////////////////////////////////
    // TreeModelListener SUPPORT
    //
    public void treeNodesChanged(TreeModelEvent e) {
    }

    public void treeNodesInserted(TreeModelEvent e) {
    }

    public void treeNodesRemoved(TreeModelEvent e) {
    }

    public void treeStructureChanged(TreeModelEvent e) {
    }

    //
    // End of TreeModelListener SUPPORT
    // ///////////////////////////////////////

    public class NotationDefaultTreeModel extends DefaultTreeModel {

        public NotationDefaultTreeModel(NotationNode notationNode) {
            super(notationNode);
        }

        // Called at the end of a cell edition, to process the edition result.
        public final void valueForPathChanged(TreePath path, Object newValue) {
            NotationNode selectedNode = (NotationNode) notationTree.getLastSelectedPathComponent();
            DbObject nodeInfo = (DbObject) selectedNode.getUserObject();
            try {
                if (!StringUtil.isEmptyString(newValue.toString())) {
                    nodeInfo.getDb().beginTrans(Db.WRITE_TRANS, RENAME_NOTATION);
                    nodeInfo.set(DbSMSNotation.fName, newValue.toString());
                    nodeInfo.getDb().commitTrans();

                    selectedNode.setDisplayText(newValue.toString());
                    notationTreeModel.nodeChanged(selectedNode);
                    NotationNode parentNode = (NotationNode) selectedNode.getParent();
                    int index = parentNode.getIndex(selectedNode);
                    boolean expanded = notationTree.isExpanded(index);
                    notationTreeModel.removeNodeFromParent(selectedNode);
                    notationTreeModel.insertNodeInto(selectedNode, parentNode, index);
                    if (expanded) {
                        TreePath notationPath = notationTree.getPathForRow(index);
                        notationTree.expandPath(notationPath);
                    }
                }
            } catch (Exception ex) {
                org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(notationFrame,
                        ex);
            }
        }
    }
}
