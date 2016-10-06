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

package org.modelsphere.jack.srtool.explorer;

import java.awt.*;
import java.awt.dnd.Autoscroll;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.modelsphere.jack.actions.AbstractActionsStore;
import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.awt.GradientTopBorder;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.objectviewer.Viewer;
import org.modelsphere.jack.srtool.*;
import org.modelsphere.jack.srtool.FocusManager;
import org.modelsphere.jack.srtool.features.SrDropTarget;
import org.modelsphere.jack.util.ExceptionHandler;

public class ExplorerView extends JTree implements Autoscroll, TreeSelectionListener,
        TreeWillExpandListener, SrDropTarget, SelectionListener {

    private Font nodeFont;

    private Font groupFont;

    private JPanel viewPanel;
    private TreePath hitPath;
    private Rectangle dropRect = null; // highlighted rectangle for drop target
    private TreePath dropPath = null; // path for object returned by
    // getSemObjAtLocation
    private JScrollPane scrollPane = null;
    private int margin = 25;

    // Vertical ScrollBar Tooltips management
    private int delay = -1;
    private int lastY = -1;
    private final VerticalScrollBarToolTipsManager tipsManager = new VerticalScrollBarToolTipsManager();

    // GANDALF keyword activator index
    private int next = 0;

    public void selectionChanged(SelectionChangedEvent e) throws DbException {
        if (!(e.focusObject instanceof ExplorerView)) {
            cancelEditing();
        }
    }

    private class VerticalScrollBarToolTipsManager implements MouseListener, MouseMotionListener {
        VerticalScrollBarToolTipsManager() {
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
            scrollPane.getVerticalScrollBar().setToolTipText(null);
        }

        public void mouseEntered(MouseEvent e) {
            ToolTipManager.sharedInstance().registerComponent(scrollPane.getVerticalScrollBar());
            scrollPane.getVerticalScrollBar().setToolTipText(null);
            if (delay == -1) {
                delay = ToolTipManager.sharedInstance().getInitialDelay();
                ToolTipManager.sharedInstance().setInitialDelay(0);
            }
        }

        public void mouseExited(MouseEvent e) {
            ToolTipManager.sharedInstance().unregisterComponent(scrollPane.getVerticalScrollBar());
            scrollPane.getVerticalScrollBar().setToolTipText(null);
            if (delay > 0) {
                ToolTipManager.sharedInstance().setInitialDelay(delay);
                delay = -1;
            }
            lastY = -1;
        }

        public void mouseDragged(MouseEvent event) {
            Point eventPoint = event.getPoint();
            Point p = SwingUtilities.convertPoint(scrollPane.getVerticalScrollBar(), eventPoint,
                    ExplorerView.this);
            if (Math.abs(p.y - lastY) < 5)
                return;
            lastY = p.y;
            int y = scrollPane.getViewport().getViewRect().y;
            int selRow = ExplorerView.this.getClosestRowForLocation(p.x, y + 5);
            TreeCellRenderer r = ExplorerView.this.getCellRenderer();

            if (selRow != -1 && r != null) {
                TreePath path = ExplorerView.this.getPathForRow(selRow);
                TreePath parent = path;
                while (parent != null) {
                    if (!(parent.getLastPathComponent() instanceof DynamicNode)) {
                        parent = parent.getParentPath();
                        continue;
                    }
                    Object last = parent.getLastPathComponent();
                    if (((Explorer) getModel()).isContainerRoot(((DynamicNode) last)
                            .getRealObject()))
                        break;
                    parent = parent.getParentPath();
                }
                if (parent != null) {
                    Object lastPath = parent.getLastPathComponent();
                    Component rComponent = r.getTreeCellRendererComponent(ExplorerView.this,
                            lastPath, ExplorerView.this.isRowSelected(selRow), ExplorerView.this
                                    .isExpanded(selRow), ExplorerView.this.getModel().isLeaf(
                                    lastPath), selRow, true);

                    if (rComponent instanceof JComponent) {
                        MouseEvent newEvent;
                        Rectangle pathBounds = getPathBounds(path);

                        p.translate(-pathBounds.x, -pathBounds.y);
                        newEvent = new MouseEvent(rComponent, event.getID(), event.getWhen(), event
                                .getModifiers(), p.x, p.y, event.getClickCount(), event
                                .isPopupTrigger());
                        String ttext = ((JComponent) rComponent).getToolTipText(newEvent);
                        scrollPane.getVerticalScrollBar().setToolTipText(ttext);
                        ToolTipManager.sharedInstance().mouseMoved(
                                new MouseEvent(scrollPane.getVerticalScrollBar(), event.getID(),
                                        event.getWhen(), event.getModifiers(), -1, -20, event
                                                .getClickCount(), event.isPopupTrigger()));
                        return;
                    }
                }
            }
            scrollPane.getVerticalScrollBar().setToolTipText(null);
        }

        public void mouseMoved(MouseEvent event) {
        }
    }

    public void autoscroll(Point cursorLocn) {
        int realrow = this.getClosestRowForLocation(cursorLocn.x, cursorLocn.y);

        if (realrow == getRowCount())
            return;

        Rectangle outer = getBounds();
        realrow = (cursorLocn.y + outer.y <= margin ? realrow < 1 ? 0 : realrow - 1
                : realrow < getRowCount() - 1 ? realrow + 1 : realrow);

        if (realrow <= 0 || realrow > getRowCount())
            scrollRowToVisible(getRowCount());

        scrollRowToVisible(realrow);
    }

    public Insets getAutoscrollInsets() {
        Rectangle outer = getBounds();
        Rectangle inner = getParent().getBounds();
        return new Insets(inner.y - outer.y + margin, inner.x - outer.x + margin, outer.height
                - inner.height - inner.y + outer.y + margin, outer.width - inner.width - inner.x
                + outer.x + margin);
    }

    public ExplorerView(Explorer model) {
        super(model);
        nodeFont = getFont();
        groupFont = nodeFont.deriveFont(Font.ITALIC);
        // putClientProperty("JTree.lineStyle", "Angled"); //NOT LOCALIZABLE
        addTreeSelectionListener(this);
        ApplicationContext.getFocusManager().addSelectionListener(this);
        addTreeWillExpandListener(this);
        ToolTipManager.sharedInstance().registerComponent(this);

        invokesStopCellEditing = true;

        // seting up the renderer
        setCellRenderer(createTreeCellRenderer());

        // seting up the editor
        setEditable(true);
        setCellEditor(createTreeCellEditor());

        TreeSelectionModel tm = getSelectionModel();
        tm.setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

        setRootVisible(false);
        setShowsRootHandles(true);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mousePressedAction(e);
            }

            public void mouseClicked(MouseEvent e) {
                mouseClickedAction(e);
            }

            public void mouseReleased(MouseEvent e) {
                mouseReleasedAction(e);
            }
        });

        // Uncomment following line to activate Drag&Drop
        ApplicationContext.getDragDrop().enableDrag(this);
        ApplicationContext.getDragDrop().enableDrop(this);
        viewPanel = new JPanel(new BorderLayout());
        scrollPane = new JScrollPane(this);
        scrollPane.getVerticalScrollBar().setComponentOrientation(
                ComponentOrientation.RIGHT_TO_LEFT);
        // Uncomment to activate scroll Tips
        // scrollPane.getVerticalScrollBar().addMouseListener(tipsManager);
        // scrollPane.getVerticalScrollBar().addMouseMotionListener(tipsManager);

        viewPanel.add(scrollPane, BorderLayout.CENTER);

        addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (!e.isControlDown() || !e.isAltDown()) {
                    next = 0;
                    return;
                }
                int c = e.getKeyCode();
                int[] gandalf = new int[] { KeyEvent.VK_G, KeyEvent.VK_A, KeyEvent.VK_N,
                        KeyEvent.VK_D, KeyEvent.VK_A, KeyEvent.VK_L, KeyEvent.VK_F };
                if (c == gandalf[next]) {
                    next++;
                    e.consume();
                } else
                    next = 0;
                if (next == gandalf.length) {
                    next = 0;
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            TreePath[] selectedPath = getSelectionPaths();
                            if (selectedPath == null)
                                return;
                            ArrayList tempRoots = new ArrayList();
                            for (int i = 0; i < selectedPath.length; i++) {
                                Object obj = selectedPath[i].getLastPathComponent();
                                tempRoots.add(((DefaultMutableTreeNode) obj).getUserObject());
                            }
                            Viewer.showViewer(ApplicationContext.getDefaultMainFrame(), tempRoots
                                    .toArray());
                        }
                    });
                }
            }
        });
        setDragEnabled(true);
        ToolTipsServices.register(this);
    }

    protected TreeCellRenderer createTreeCellRenderer() {
        TreeCellRenderer newRenderer = new DefaultTreeCellRenderer() {
            public Component getTreeCellRendererComponent(JTree tree, Object value,
                    boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded,
                        leaf, row, hasFocus);
                if (c instanceof JLabel) {
                    JLabel label = (JLabel) c;
                    label.setToolTipText(label.getText());
                    if (value instanceof DynamicNode) {
                        DynamicNode node = (DynamicNode) value;
                        label.setIcon(node.getIcon());
                        label.setToolTipText(node.getToolTips());
                        label.setFont(node.getUserObject() instanceof GroupParams ? groupFont
                                : nodeFont);
                    }
                }

                return c;
            }
        };
        return newRenderer;
    }

    protected TreeCellEditor createTreeCellEditor() {
        TreeCellEditor newEditor = new DefaultTreeCellEditor(this, null) {
            public boolean isCellEditable(EventObject event) {
                if (ApplicationContext.getDragDrop().isDragging()) // because
                    // editing
                    // after a
                    // delay
                    // does not
                    // check if
                    // mouse has
                    // moved
                    return false;
                if (event instanceof MouseEvent) {
                    DbObject dbo = getSemObjAtLocation(((MouseEvent) event).getPoint());
                    if (dbo == null)
                        return false;

                    boolean editable = false;
                    try {
                        dbo.getDb().beginReadTrans();
                        editable = ApplicationContext.getSemanticalModel().isNameEditable(dbo,
                                getModel().getClass());
                        dbo.getDb().commitTrans();
                    } catch (Exception e) {
                        ExceptionHandler.processUncatchedException(ExplorerView.this, e);
                        editable = false;
                    }
                    if (!editable)
                        return false;
                }
                return super.isCellEditable(event);
            }

            public Component getTreeCellEditorComponent(JTree tree, Object value,
                    boolean isSelected, boolean expanded, boolean leaf, int row) {
                Component editor = super.getTreeCellEditorComponent(tree, ((DynamicNode) value)
                        .getEditText(), isSelected, expanded, leaf, row);
                return editor;
            }

            public Object getCellEditorValue() {
                return super.getCellEditorValue();
            }

            public boolean shouldSelectCell(EventObject anEvent) {
                return super.shouldSelectCell(anEvent);
            }

            public boolean stopCellEditing() {
                return super.stopCellEditing();
            }

            public void cancelCellEditing() {
                super.cancelCellEditing();
            }

            public void addCellEditorListener(CellEditorListener l) {
                super.addCellEditorListener(l);
            }

            public void removeCellEditorListener(CellEditorListener l) {
                super.removeCellEditorListener(l);
            }
        };
        return newEditor;
    }

    public final JPanel getViewPanel() {
        return viewPanel;
    }

    public final boolean isShown() {
        ExplorerPanel expPanel = (ExplorerPanel) SwingUtilities.getAncestorOfClass(
                ExplorerPanel.class, viewPanel);
        return (expPanel != null && expPanel.isVisible());
    }

    private void mousePressedAction(MouseEvent e) {
        ApplicationContext.getFocusManager().setFocusToExplorer(this);

        e.consume();
        if (e.isPopupTrigger()) {
            hitPath = getPathForLocation(e.getX(), e.getY());
            if (hitPath != null && !isPathSelected(hitPath))
                setSelectionPath(hitPath);
            if (hitPath != null) {
                boolean useContainer = false;
                TreePath[] treePaths = getSelectionPaths();
                if (treePaths != null && treePaths.length == 1) {
                    Object userObject = ((DynamicNode) treePaths[0].getLastPathComponent())
                            .getRealObject();
                    if (userObject == Explorer.DB_RAM || userObject instanceof Db)
                        useContainer = true;
                }
                JPopupMenu popup = getPopupMenu(useContainer);
                if (popup != null)
                    popup.show(this, e.getX(), e.getY());
            }
        } else {
            boolean isMultiSelect = false;
            Object obj = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
            if (obj != null) {
                DbObject[] semObjs = (DbObject[]) obj;
                if (semObjs != null) {
                    if (semObjs.length > 0) {
                        isMultiSelect = true;
                    }
                }
            }
        }
    }

    private void mouseReleasedAction(MouseEvent e) {
        ApplicationContext.getFocusManager().setFocusToExplorer(this);

        if (e.isPopupTrigger()) {
            hitPath = getPathForLocation(e.getX(), e.getY());
            if (hitPath != null && !isPathSelected(hitPath))
                setSelectionPath(hitPath);
            if (hitPath != null) {
                boolean useContainer = false;
                TreePath[] treePaths = getSelectionPaths();
                if (treePaths != null && treePaths.length == 1) {
                    Object userObject = ((DynamicNode) treePaths[0].getLastPathComponent())
                            .getRealObject();
                    if (userObject == Explorer.DB_RAM || userObject instanceof Db)
                        useContainer = true;
                }
                JPopupMenu popup = getPopupMenu(useContainer);
                if (popup != null)
                    popup.show(this, e.getX(), e.getY());
            }
        }
    }

    /*
     * If only the RAM or Repository node is selected, show the popup menu at container level. toDo:
     * it would be better to attach a popup directly to the RAM and Repository nodes.
     */
    private void mouseClickedAction(MouseEvent e) {
        if (e.getClickCount() == 2) {
            TreePath treePath = this.getPathForLocation(e.getPoint().x, e.getPoint().y);
            if (treePath != null) {
                DynamicNode node = (DynamicNode) treePath.getLastPathComponent();
                if (node != null && node.getDefaultAction() != null) {
                    node.getDefaultAction().performAction();
                }
            }
        }
    }

    private JPopupMenu getPopupMenu(boolean useContainer) {
        return ApplicationContext.getApplPopupMenu().getPopupMenu(useContainer);
    }

    private int nodeExpanded = -1;
    private static final int MAX_NODE_EXPANDED = 2000;

    public final void expandAllUnder(TreePath[] treePaths) {
        nodeExpanded = -1;
        if (treePaths != null) {
            for (int i = 0; i < treePaths.length; i++) {
                expandAllUnder(treePaths[i]);
            }
        }
    }

    private void expandAllUnder(TreePath treePath) {
        DynamicNode node = (DynamicNode) treePath.getLastPathComponent();
        Object userObject = node.getUserObject();
        if (userObject == Explorer.DB_RAM || userObject instanceof Db
                || userObject instanceof DbProject)
            return; // cannot expand a database or a project node.
        try {
            DynamicNode dbNode = (userObject instanceof GroupParams ? (DynamicNode) node
                    .getParent() : node);
            Db db = ((DbObject) dbNode.getRealObject()).getDb();
            db.beginTrans(Db.READ_TRANS); // expand recursively in a single
            // transaction.
            expandAllUnderAux(node);
            db.commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(this, ex);
        }
    }

    private void expandAllUnderAux(DynamicNode node) throws DbException {
        if (node.isLeaf())
            return;
        if (nodeExpanded++ > MAX_NODE_EXPANDED)
            return;
        ((Explorer) getModel()).loadChildren(node);
        expandPath(new TreePath(node.getPath()));
        int count = node.getChildCount();
        for (int i = 0; i < count; i++)
            expandAllUnderAux((DynamicNode) node.getChildAt(i));
    }

    public final void collapseAllUnder(TreePath[] treePaths) {
        if (treePaths != null) {
            for (int i = 0; i < treePaths.length; i++) {
                DynamicNode node = (DynamicNode) treePaths[i].getLastPathComponent();
                Enumeration enumeration = node.depthFirstEnumeration();
                while (enumeration.hasMoreElements()) {
                    DynamicNode nodeElem = (DynamicNode) enumeration.nextElement();
                    if (!nodeElem.isLeaf()) {
                        TreePath treePath = new TreePath(nodeElem.getPath());
                        collapsePath(treePath);
                    }
                }
            }
        }
    }

    // ////////////////////////////////////
    // TreeSelectionListener SUPPORT
    //
    public final void valueChanged(TreeSelectionEvent e) {
        ApplicationContext.getFocusManager().selectionChanged(this);
    }

    //
    // End of TreeSelectionListener SUPPORT
    // ////////////////////////////////////

    // ////////////////////////////////////
    // TreeWillExpandListener SUPPORT
    //
    public final void treeWillExpand(TreeExpansionEvent event) {
        DynamicNode node = (DynamicNode) event.getPath().getLastPathComponent();
        try {
            ((Explorer) getModel()).loadChildren(node);
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(this, ex);
        }
    }

    public final void treeWillCollapse(TreeExpansionEvent event) {

    }

    //
    // End of TreeWillExpandListener SUPPORT
    // ////////////////////////////////////

    // ////////////////////////////////////
    // SrDropTarget SUPPORT
    //
    public final DbObject getSemObjAtLocation(Point pt) {
        dropPath = getPathForLocation(pt.x, pt.y);
        if (dropPath == null)
            return null;
        Object obj = ((DynamicNode) dropPath.getLastPathComponent()).getRealObject();
        return (obj instanceof DbObject ? (DbObject) obj : null);
    }

    public final void highlightCell(boolean state) {
        Rectangle newRect = (state ? getPathBounds(dropPath) : null);
        if (dropRect == null ? newRect == null : dropRect.equals(newRect))
            return;

        if (dropRect != null) {
            dropRect.grow(1, 1);
            repaint(dropRect.x, dropRect.y + 1, dropRect.width, dropRect.height - 2);
        }
        if (newRect != null) {
            Graphics2D g = (Graphics2D) getGraphics();
            if (g == null)
                return;
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));
            g.setColor(getBackground());
            g.fillRect(newRect.x, newRect.y, newRect.width, newRect.height - 1);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.70f));
            g.setColor(getForeground());
            g.drawRect(newRect.x, newRect.y, newRect.width, newRect.height - 1);
            g.dispose();
        }
        dropRect = newRect;
    }

    //
    // End of SrDropTarget SUPPORT
    // ////////////////////////////////////

    public final void startEditing(DbObject dbo) {
        DynamicNode node = null;
        try {
            node = ((Explorer) getModel()).getDynamicNode(dbo, true);
        } catch (DbException e) {
        }
        if (node != null) {
            startEditingAtPath(new TreePath(node.getPath()));
        }
    }

    public final boolean find(DbObject dbo) throws DbException {
        DynamicNode node = ((Explorer) getModel()).getDynamicNode(dbo, true);
        if (node == null)
            return false;
        TreePath path = new TreePath(node.getPath());
        addSelectionPath(path);
        scrollPathToVisible(path);
        return true;
    }

    // There may be more than one node representing the same object;
    // so we use a HashSet to eliminate the duplicates.
    public final Object[] getSelection() {
        TreePath[] treePaths = getSelectionPaths();
        if (treePaths == null)
            return new Object[0];
        HashSet objs = new HashSet();
        for (int i = 0; i < treePaths.length; i++) {
            DynamicNode node = (DynamicNode) treePaths[i].getLastPathComponent();
            if (node.getUserObject() instanceof GroupParams) {
                int nb = node.getChildCount();
                for (int j = 0; j < nb; j++) {
                    DynamicNode childNode = (DynamicNode) node.getChildAt(j);
                    objs.add(childNode.getRealObject());
                }
            } else
                objs.add(node.getRealObject());
        }
        return objs.toArray();
    }

    /*
     * Returns the current selected project, null if nothing selected, or Explorer.ROOT if more than
     * one project selected.
     */
    public final Object getSelectedProject() {
        TreePath[] paths = getSelectionPaths();
        if (paths == null)
            return null;
        Object selProject = null;
        for (int i = 0; i < paths.length; i++) {
            for (DynamicNode node = (DynamicNode) paths[i].getLastPathComponent(); node != null; node = (DynamicNode) node
                    .getParent()) {
                Object userObject = node.getUserObject();
                if (userObject instanceof GroupParams)
                    continue;
                if (!(userObject instanceof DbObject || userObject instanceof DynamicNode))
                    return Explorer.ROOT;
                if (userObject instanceof DbProject) {
                    if (selProject != null && selProject != userObject)
                        return Explorer.ROOT;
                    selProject = userObject;
                    break;
                }
            }
        }
        return selProject;
    }

    // If more than one db, expand all db nodes.
    final void expandDbNodes() {
        DynamicNode rootNode = (DynamicNode) getModel().getRoot();
        if (rootNode.getUserObject() != Explorer.ROOT)
            return;
        TreePath rootPath = new TreePath(rootNode);
        int nb = rootNode.getChildCount();
        for (int i = 0; i < nb; i++) {
            DynamicNode node = (DynamicNode) rootNode.getChildAt(i);
            TreePath path = rootPath.pathByAddingChild(node);
            expandPath(path);
        }
    }

    public final void updateUI() {

        super.updateUI();

        nodeFont = getFont();
        groupFont = nodeFont.deriveFont(Font.ITALIC);

        // Ensure that UI for renderer and editor are updated
        setCellRenderer(createTreeCellRenderer());
        setCellEditor(createTreeCellEditor());

        // Flush copy and paste accelerator from this component inputMap
        // - Avoid copy of the displayed text instead of the copy action of the
        // application
        InputMap inputMap = getInputMap();
        if (inputMap != null) {
            AbstractApplicationAction copyAction = ApplicationContext.getActionStore().getAction(
                    AbstractActionsStore.COPY);
            AbstractApplicationAction pasteAction = ApplicationContext.getActionStore().getAction(
                    AbstractActionsStore.PASTE);
            KeyStroke copyKey = copyAction.getAccelerator();
            KeyStroke pasteKey = pasteAction.getAccelerator();
            while (inputMap != null) {
                if (copyKey != null)
                    inputMap.remove(copyKey);
                if (pasteKey != null)
                    inputMap.remove(pasteKey);
                inputMap = inputMap.getParent();
            }
        }
    }

    public boolean hasFocusState() {
        return FocusManager.getSingleton().getFocusObject() == this;
    }
}
