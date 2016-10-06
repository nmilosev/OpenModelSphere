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

package org.modelsphere.sms.style;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
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
import org.modelsphere.jack.baseDb.db.DbStyleI;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.screen.DbTreeLookupDialog;
import org.modelsphere.jack.baseDb.screen.LookupDialog;
import org.modelsphere.jack.baseDb.screen.model.DbTreeModelListener;
import org.modelsphere.jack.srtool.DbApplication;
import org.modelsphere.jack.srtool.graphic.DbGraphic;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.CollationComparator;
import org.modelsphere.jack.util.StringUtil;
import org.modelsphere.sms.be.db.DbBEStyle;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSStyle;
import org.modelsphere.sms.oo.db.DbOOAbsPackage;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORCommonItemStyle;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORDomainStyle;
import org.modelsphere.sms.or.db.DbORStyle;

public class StyleBankComponent extends JPanel implements DbRefreshListener, TreeSelectionListener,
        TreeModelListener {

    private static final String BTN_NEW = LocaleMgr.screen.getString("new"); // "Nouveau";//
    private static final String BTN_DELETE = LocaleMgr.screen.getString("delete");// "Supprimer";//
    private static final String BTN_IMPORT = LocaleMgr.screen.getString("import");
    private static final String BTN_DEFAULT = LocaleMgr.screen.getString("default");
    private static final String BTN_USED = LocaleMgr.screen.getString("usedInDiagrams");
    private static final String STYLE_ADD = LocaleMgr.action.getString("styleAdd");
    private static final String NEW_STYLE = LocaleMgr.misc.getString("newStyle");
    private static final String DELETE_STYLE = LocaleMgr.action.getString("styleDelete");
    private static final String RENAME_STYLE = LocaleMgr.action.getString("styleRename");
    private static final String IMPORT_STYLE = LocaleMgr.action.getString("styleImport");
    private static final String WARNING = LocaleMgr.screen.getString("warning");
    private static final String DELETE_USED_STYLE = LocaleMgr.message.getString("deleteUsedStyle");
    private static final String STYLE_MODIFICATION = LocaleMgr.action
            .getString("styleModification");
    private static final String SELECT_SOURCE_STYLE = LocaleMgr.screen
            .getString("selectSourceStyles");
    private static final String kDiagsUsingStyle = LocaleMgr.screen.getString("diagsUsingStyle");

    private JButton newBtn = new JButton(BTN_NEW);
    private JButton deleteBtn = new JButton(BTN_DELETE);
    private JButton importBtn = new JButton(BTN_IMPORT);
    private JButton defaultBtn = new JButton(BTN_DEFAULT);
    private JButton usedBtn = new JButton(BTN_USED);
    private JButton[] jButtonList = new JButton[] { newBtn, deleteBtn, importBtn, defaultBtn };
    private String nodeName;
    private MetaClass styleClass;
    private DbProject project;
    private StyleFrame styleFrame;
    private StyleNode styleNode;
    private StyleTree styleTree;
    private StyleComponent styleComponent;
    private DefaultTreeModel styleTreeModel;

    public StyleBankComponent(StyleFrame styleFrame, MetaClass styleClass, String[] optionsName) {
        this.styleFrame = styleFrame;
        this.styleClass = styleClass;
        this.nodeName = optionsName[2];
        jbInit();
    }

    private void jbInit() {
        this.setLayout(new BorderLayout());
        /** Style Tree */
        styleNode = new StyleNode("Styles"); // NOT LOCALIZABLE
        styleTreeModel = new StyleDefaultTreeModel(styleNode);
        styleTreeModel.addTreeModelListener(this);

        styleTree = new StyleTree(styleTreeModel);
        styleTree.addTreeSelectionListener(this);
        this.add(new JScrollPane(styleTree), BorderLayout.CENTER);

        /** Tree control panel */
        JPanel ctrlPanel = new JPanel(new BorderLayout());
        this.add(ctrlPanel, BorderLayout.SOUTH);
        JPanel btnPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnPanel1.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        btnPanel1.add(deleteBtn);
        btnPanel1.add(newBtn);
        btnPanel1.add(importBtn);
        ctrlPanel.add(btnPanel1, BorderLayout.NORTH);
        JPanel btnPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnPanel2.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        btnPanel2.add(usedBtn);
        btnPanel2.add(defaultBtn);
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
        defaultBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                defaultBtn_actionPerformed(e);
            }
        });
        defaultBtn.setEnabled(false);
        usedBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                usedBtn_actionPerformed(e);
            }
        });
        usedBtn.setEnabled(false);
    }

    public void setProject(StyleComponent currentStyleComponent, DbObject style, DbProject aProject)
            throws DbException {
        styleComponent = currentStyleComponent;
        project = aProject;
        loadStyleBank();
    }

    // Create all style nodes
    private void loadStyleBank() throws DbException {
        StyleNode componentNode = null;

        DbEnumeration dbEnum = project.getComponents().elements(styleClass);
        while (dbEnum.hasMoreElements()) {
            DbObject style = dbEnum.nextElement();

            if (style.getClass() == styleClass.getJClass()) {
                componentNode = new StyleNode(style);
                loadStyleComponentNode(componentNode);
                styleNode.add(componentNode);
            }
        }
        dbEnum.close();
        // To ensure that the RootNode is expended because it is not visible
        styleTree.expandPath(new TreePath(styleNode));
    }

    // Create all leaves (options) for a Style node
    private void loadStyleComponentNode(StyleNode componentNode) throws DbException {
        String[] optionGroupHeaders;
        StyleNode subComponentNode = null;

        optionGroupHeaders = StyleFrame.getOptionGroupHeaders(styleClass.getJClass(), nodeName);
        for (int i = 0; i < optionGroupHeaders.length; i++) {
            subComponentNode = new StyleNode(optionGroupHeaders[i]);
            componentNode.add(subComponentNode);
        }
    }

    // Create the new style at the end of the Tree
    private void createNewStyle(DbObject newDbStyle) throws DbException {
        int i = styleTreeModel.getChildCount(styleNode);
        StyleNode newStyleComponent = new StyleNode(newDbStyle);

        loadStyleComponentNode(newStyleComponent);
        styleTreeModel.insertNodeInto(newStyleComponent, styleNode, i);
    }

    private void newbtn_actionPerformed(ActionEvent e) {
        try {
            project.getDb().beginTrans(Db.WRITE_TRANS, STYLE_ADD);
            DbObject newStyle = project.createComponent(styleClass);
            newStyle.set(DbSMSStyle.fName, NEW_STYLE);
            ((DbStyleI) newStyle).initOptions();
            createNewStyle(newStyle);
            project.getDb().commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(styleFrame, ex);
        }
    }

    private void deletebtn_actionPerformed(ActionEvent e) {
        try {
            Object parentNodeInfo;
            StyleNode parentNode;
            StyleNode selectedNode = (StyleNode) styleTree.getLastSelectedPathComponent();

            if (selectedNode == null)
                return;
            if (selectedNode.isLeaf())
                parentNode = (StyleNode) selectedNode.getParent();
            else
                parentNode = selectedNode;
            parentNodeInfo = parentNode.getUserObject();

            // check if the selected style is used
            project.getDb().beginTrans(Db.READ_TRANS);
            DbObject style = (DbObject) parentNodeInfo;
            boolean usedbydiagram = style.getNbNeighbors(DbGraphic.fDiagramStyle
                    .getOppositeRel(null)) != 0;
            boolean usedbygo = style.getNbNeighbors(DbGraphic.fGraphicalObjectStyle
                    .getOppositeRel(null)) != 0;
            project.getDb().commitTrans();

            if (usedbydiagram || usedbygo) {
                int userResponse = JOptionPane.showConfirmDialog(styleFrame, DELETE_USED_STYLE,
                        WARNING, JOptionPane.YES_NO_OPTION);
                if (userResponse == JOptionPane.NO_OPTION) {
                    return;
                }
            }

            // delete this style
            project.getDb().beginTrans(Db.WRITE_TRANS, DELETE_STYLE);
            style.remove();
            project.getDb().commitTrans();

            // delete this style from the tree
            styleTreeModel.removeNodeFromParent(parentNode);
            styleComponent.nameTextField.setText("");
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(styleFrame, ex);
        }
    }

    private void defaultBtn_actionPerformed(ActionEvent e) {
        try {
            StyleNode selectedNode = (StyleNode) styleTree.getLastSelectedPathComponent();
            if (selectedNode == null)
                return;
            StyleNode parentNode = (selectedNode.isLeaf() ? (StyleNode) selectedNode.getParent()
                    : selectedNode);
            DbSMSStyle style = (DbSMSStyle) parentNode.getUserObject();
            style.getDb().beginTrans(Db.WRITE_TRANS, STYLE_MODIFICATION);
            style.initOptions();
            style.getDb().commitTrans();

            // refresh the selected panel
            if (selectedNode.isLeaf()) {
                style.getDb().beginTrans(Db.READ_TRANS);
                styleComponent.setStyle(style, selectedNode, false);
                style.getDb().commitTrans();
            }
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(styleFrame, ex);
        }
    }

    // Shows in a list all the diagrams using the selected style.
    private void usedBtn_actionPerformed(ActionEvent e) {
        try {
            StyleNode selectedNode = (StyleNode) styleTree.getLastSelectedPathComponent();
            if (selectedNode == null)
                return;
            StyleNode parentNode = (selectedNode.isLeaf() ? (StyleNode) selectedNode.getParent()
                    : selectedNode);
            DbSMSStyle style = (DbSMSStyle) parentNode.getUserObject();

            style.getDb().beginTrans(Db.READ_TRANS);
            HashSet diagSet = new HashSet(); // use a Set to eliminate
            // duplicates
            // Add to the Set all the diagrams linked to this style.
            DbEnumeration dbEnum = style.getDiagrams().elements();
            while (dbEnum.hasMoreElements())
                diagSet.add(dbEnum.nextElement());
            dbEnum.close();
            // Add to the Set all the diagrams containing objects using this
            // style.
            dbEnum = style.getGraphicalObjects().elements();
            while (dbEnum.hasMoreElements())
                diagSet.add(dbEnum.nextElement().getComposite());
            dbEnum.close();
            // If default style, add to the Set all the diagrams that are not
            // linked to a style.
            if (style.isDefaultStyle()) {
                MetaClass packClass;
                if (style instanceof DbORStyle)
                    packClass = DbORDataModel.metaClass;
                else if (style instanceof DbORDomainStyle)
                    packClass = DbORDomainModel.metaClass;
                else if (style instanceof DbORCommonItemStyle)
                    packClass = DbORCommonItemModel.metaClass;
                else if (style instanceof DbBEStyle)
                    packClass = DbBEUseCase.metaClass;
                else
                    packClass = DbOOAbsPackage.metaClass;

                dbEnum = project.componentTree(packClass);
                while (dbEnum.hasMoreElements()) {
                    DbEnumeration enum2 = dbEnum.nextElement().getComponents().elements(
                            DbSMSDiagram.metaClass);
                    while (enum2.hasMoreElements()) {
                        DbSMSDiagram diag = (DbSMSDiagram) enum2.nextElement();
                        if (diag.getStyle() == null)
                            diagSet.add(diag);
                    }
                    enum2.close();
                }
                dbEnum.close();
            }

            String[] items = new String[diagSet.size()];
            Iterator iter = diagSet.iterator();
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
            String title = MessageFormat.format(kDiagsUsingStyle, new Object[] { style.getName() });
            style.getDb().commitTrans(); // close transaction before showing
            // dialog

            Comparator comparator = new CollationComparator();
            Arrays.sort(items, comparator);
            LookupDialog dialog = new LookupDialog(styleFrame, title, null, items, -1,
                    LookupDialog.SELECT_NONE, comparator);
            dialog.setVisible(true);
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(styleFrame, ex);
        }
    }

    private static final String COPY_OF = org.modelsphere.sms.international.LocaleMgr.message
            .getString("CopyOf");

    private void importBtn_actionPerformed(ActionEvent e) {
        DbObject[] styles = showStylesLookupDialog();
        if (styles != null && styles.length != 0) {
            try {
                project.getDb().beginTrans(Db.WRITE_TRANS, IMPORT_STYLE);
                int i;
                for (i = 0; i < styles.length; i++) {
                    DbObject srcStyle = styles[i];
                    srcStyle.getDb().beginTrans(Db.READ_TRANS);
                    DbObject newStyle = project.createComponent(styleClass);
                    String name = srcStyle.getName();
                    name = MessageFormat.format(COPY_OF, new String[] { name });
                    newStyle.setName(name);
                    ((DbStyleI) newStyle).copyOptions(srcStyle);
                    createNewStyle(newStyle);
                }
                for (i = 0; i < styles.length; i++)
                    styles[i].getDb().commitTrans();
                project.getDb().commitTrans();
            } catch (Exception ex) {
                org.modelsphere.jack.util.ExceptionHandler
                        .processUncatchedException(styleFrame, ex);
            }
        }
    }

    private DbObject[] showStylesLookupDialog() {
        StyleNode selectedNode = (StyleNode) styleTree.getLastSelectedPathComponent();
        StyleNode parentNode = null;

        if (selectedNode != null) {
            if (selectedNode.isLeaf())
                parentNode = (StyleNode) selectedNode.getParent();
            else
                parentNode = selectedNode;
        } // end if

        DbObject dbStyle = (parentNode == null) ? null : (DbObject) parentNode.getUserObject();
        DbProject projs[] = DbApplication.getProjects();
        Object obj = DbTreeLookupDialog.selectOne(this, SELECT_SOURCE_STYLE, projs,
                new MetaClass[] { styleClass }, (DbTreeModelListener) null, null, dbStyle);

        DbObject style = (obj instanceof DbObject) ? (DbObject) obj : null;
        DbObject[] styles = (style == null) ? null : new DbObject[] { style };
        return styles;
    }

    // TO DO: use this method to refresh the tree....
    public final void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
        if (!event.dbo.hasField(DbSMSStyle.fName))
            return;

        DbObject style = event.dbo;
        if (style.getTransStatus() == Db.OBJ_ADDED) {
        } else if (style.getTransStatus() == Db.OBJ_REMOVED) {
        } else { // rename...
        }
    }

    // ///////////////////////////////////////
    // TreeSelectionListener SUPPORT
    //
    public void valueChanged(TreeSelectionEvent e) {
        Object parentNodeInfo = null;
        StyleNode selectedNode = (StyleNode) styleTree.getLastSelectedPathComponent();

        // nothing selected
        if (selectedNode == null) {
            deleteBtn.setEnabled(false);
            defaultBtn.setEnabled(false);
            usedBtn.setEnabled(false);
            return;
        }
        try {
            if (selectedNode.isLeaf()) {
                StyleNode parentNode = (StyleNode) selectedNode.getParent();
                parentNodeInfo = parentNode.getUserObject();

                if (parentNodeInfo instanceof DbObject) {
                    ((DbObject) parentNodeInfo).getDb().beginTrans(Db.READ_TRANS);
                    styleComponent.setStyle((DbObject) parentNodeInfo, selectedNode, false);
                    ((DbObject) parentNodeInfo).getDb().commitTrans();
                }
                styleTree.setEditable(false);
            } else {
                parentNodeInfo = selectedNode.getUserObject();
                ((DbObject) parentNodeInfo).getDb().beginTrans(Db.READ_TRANS);
                styleComponent.setStyle((DbObject) parentNodeInfo);
                ((DbObject) parentNodeInfo).getDb().commitTrans();
                styleTree.setEditable(true);
            }
            // Set the new selected style in styleFrame
            styleFrame.setStyle((DbObject) parentNodeInfo);

            // Enables (or disables) the buttons.
            ((DbObject) parentNodeInfo).getDb().beginTrans(Db.READ_TRANS);
            defaultBtn.setEnabled(true);
            usedBtn.setEnabled(true);
            deleteBtn.setEnabled(!selectedNode.isLeaf()
                    && !((DbSMSStyle) parentNodeInfo).isDefaultStyle());
            ((DbObject) parentNodeInfo).getDb().commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(styleFrame, ex);
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

    public class StyleDefaultTreeModel extends DefaultTreeModel {

        public StyleDefaultTreeModel(StyleNode styleNode) {
            super(styleNode);
        }

        // Called at the end of a cell edition, to process the edition result.
        public final void valueForPathChanged(TreePath path, Object newValue) {
            StyleNode selectedNode = (StyleNode) styleTree.getLastSelectedPathComponent();
            DbObject nodeInfo = (DbObject) selectedNode.getUserObject();
            try {
                if (!StringUtil.isEmptyString(newValue.toString())) {
                    nodeInfo.getDb().beginTrans(Db.WRITE_TRANS, RENAME_STYLE);
                    nodeInfo.set(DbSMSStyle.fName, newValue.toString());
                    nodeInfo.getDb().commitTrans();

                    selectedNode.setDisplayText(newValue.toString());
                    styleTreeModel.nodeChanged(selectedNode);
                    StyleNode parentNode = (StyleNode) selectedNode.getParent();
                    int index = parentNode.getIndex(selectedNode);
                    boolean expanded = styleTree.isExpanded(index);
                    styleTreeModel.removeNodeFromParent(selectedNode);
                    styleTreeModel.insertNodeInto(selectedNode, parentNode, index);
                    if (expanded) {
                        TreePath stylePath = styleTree.getPathForRow(index);
                        styleTree.expandPath(stylePath);
                    }
                }
            } catch (Exception ex) {
                org.modelsphere.jack.util.ExceptionHandler
                        .processUncatchedException(styleFrame, ex);
            }
        }
    }
}
