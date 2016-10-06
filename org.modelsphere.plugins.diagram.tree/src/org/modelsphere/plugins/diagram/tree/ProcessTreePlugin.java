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

package org.modelsphere.plugins.diagram.tree;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.print.PageFormat;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JDialog;
import javax.swing.tree.DefaultMutableTreeNode;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginAction;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.actions.PluginDefaultAction;
import org.modelsphere.jack.srtool.forward.PropertyScreenPreviewInfo;
import org.modelsphere.jack.srtool.forward.Template;
import org.modelsphere.jack.srtool.forward.VariableScope;
import org.modelsphere.jack.srtool.graphic.DiagramInternalFrame;
import org.modelsphere.plugins.diagram.tree.international.LocaleMgr;
import org.modelsphere.sms.be.db.DbBEContextGo;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.DbBEUseCaseGo;
import org.modelsphere.sms.be.db.util.BEUtility;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSLink;
import org.modelsphere.sms.db.DbSMSLinkGo;
import org.modelsphere.sms.db.DbSMSLinkModel;
import org.modelsphere.sms.db.DbSMSPackageGo;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.oo.db.DbOOAdtGo;
import org.modelsphere.sms.oo.db.DbOOClass;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.db.DbOOInheritance;
import org.modelsphere.sms.oo.forward.OOForwardEngineeringPlugin;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVInheritance;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORModel;
import org.modelsphere.sms.templates.BasicForwardToolkit;

public final class ProcessTreePlugin extends OOForwardEngineeringPlugin implements Plugin2 {
    private static final String TREE_DIAGRAM = LocaleMgr.misc.getString("TreeDiagram");
    private static final String TREE_DIAGRAM_CREATION = LocaleMgr.misc
            .getString("TreeDiagramCreation");
    private static final String TREE_DIAGRAM_LINKS = LocaleMgr.misc.getString("TreeDiagramLinks");

    //////////////////
    //OVERRIDES Plugin
    public PluginSignature getSignature() {
        return null;
    }

    public Class[] getSupportedClasses() {
        return new Class[] { DbBEDiagram.class, DbJVPackage.class, DbJVClass.class, DbORModel.class };
    }

    //
    //////////////////

    //Variable scope
    private VariableScope m_varScope = new VariableScope("TITLE");

    public VariableScope getVarScope() {
        return m_varScope;
    }

    public void setVarScope(VariableScope varScope) {
        m_varScope = varScope;
    }

    ////////////////////////////////////////////////////////////////////////
    // OVERRIDES Class Forward

    /*
     * This plug-in adds a tab in the window opened when the user selects "Properties" the pop-up
     * menu. The name of this tab is PLUGIN_DISPLAY_NAME. When selected, this tab displays an output
     * in plain text.
     */

    //private static final String PLAIN_CONTENT_TYPE   = "text/plain";
    private TreeDiagramOptions m_options = new TreeDiagramOptions();
    private PropertyScreenPreviewInfo m_propertyScreenPreviewInfo = null;

    public PropertyScreenPreviewInfo getPropertyScreenPreviewInfo() {
        return m_propertyScreenPreviewInfo;
    }

    //
    ////////////////////////////////////

    /*
     * Set the output in plain text
     */
    public Writer createNewPanelWriter(boolean isHTMLformat) {
        return new StringWriter();
    } //end createNewPanelWriter

    /*
     * The entry point
     */
    public void execute(DbObject[] semObjs) throws DbException {
        DbObject root = (DbObject) semObjs[0];
        DiagramInternalFrame frame = null;
        root.getDb().beginWriteTrans(TREE_DIAGRAM_CREATION);

        if (root instanceof DbBEUseCase) {
            DbBEUseCase process = (DbBEUseCase) root;
            //frame = executeProcess(process);
        } else if (root instanceof DbBEDiagram) {
            DbBEDiagram diagram = (DbBEDiagram) root;
            TreeDiagramOptionPanel panel = new TreeDiagramOptionPanel(m_options);
            String title = panel.getDialogTitle(diagram);

            DbBEDiagram oldDiagram = null;
            DbBEDiagram parentDiagram = (DbBEDiagram) diagram.getParentDiagram();
            if (parentDiagram != null) {
                oldDiagram = diagram;
                diagram = parentDiagram;
            }

            //display option dialog
            Frame mf = BasicForwardToolkit.getMainFrame();
            boolean modal = true;
            JDialog dialog = new JDialog(mf, title, modal);
            panel.setDialog(dialog);
            dialog.pack();
            AwtUtil.centerWindow(dialog);
            dialog.setVisible(true);
            TreeDiagramOptions options = panel.getOptions(); //returns null if CANCELled

            if (options != null) {
                if (oldDiagram != null) {
                    oldDiagram.remove();
                }

                m_options = options;
                DbBEUseCase process = (DbBEUseCase) diagram
                        .getCompositeOfType(DbBEUseCase.metaClass);
                frame = executeProcess(process, diagram, options);
            } //end if
        } else if (root instanceof DbJVPackage) {
            DbJVPackage pack = (DbJVPackage) root;
            TreeDiagramOptions options = new TreeDiagramOptions();
            frame = executePackage(pack, options);
        } else if (root instanceof DbJVClass) {
            DbJVClass claz = (DbJVClass) root;
            TreeDiagramOptions options = new TreeDiagramOptions();
            frame = executeClass(claz, options);
        } else if (root instanceof DbORDataModel) {
            DbORDataModel model = (DbORDataModel) root;
            TreeDiagramOptions options = new TreeDiagramOptions();
            frame = executeOrModel(model, options);
        } //end if

        root.getDb().commitTrans();

        //refresh frame (must occur in an other transaction */
        if (frame != null) {
            root.getDb().beginReadTrans();
            frame.refresh();
            root.getDb().commitTrans();
        }

    } //end execute()

    //
    // private methods : process part
    //
    /*
     * private DiagramInternalFrame executeProcess(DbBEUseCase process) throws DbException {
     * DbBEDiagram diagram = null;
     * 
     * DbRelationN relN = process.getComponents(); DbEnumeration dbEnum =
     * relN.elements(DbBEDiagram.metaClass); while (dbEnum.hasMoreElements()) { diagram =
     * (DbBEDiagram)dbEnum.nextElement(); break; } //end while dbEnum.close();
     * 
     * DiagramInternalFrame frame = executeProcess(process, diagram); return frame; } //end
     * executeProcess()
     */

    private DiagramInternalFrame executeProcess(DbBEUseCase process, DbBEDiagram parentDiagram,
            TreeDiagramOptions options) throws DbException {
        DbBENotation notation = parentDiagram.getNotation();
        BEUtility util = BEUtility.getSingleInstance();
        DbBEDiagram newDiagram = util.createBEDiagram(process, notation);
        newDiagram.setName(TREE_DIAGRAM);
        newDiagram.setParentDiagram(parentDiagram);
        cleanUpDiagram(newDiagram);

        //build the tree
        ProcessTreeModel.NodeContent content = new ProcessTreeModel.NodeContent(process);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(content);
        ProcessTreeModel model = new ProcessTreeModel(root);
        performProcess(newDiagram, process, root, options.onlyExploded());

        //get tree size
        model.computeWidestLevel(options.isCenter(), options.isSpan());

        //draw and display tree diagram
        if (notation == null) {
            DbSMSProject project = (DbSMSProject) process.getProject();
            notation = project.getBeDefaultNotation();
        }
        int boxWidth = notation.getUseCaseDefaultWidth().intValue();
        int boxHeight = notation.getUseCaseDefaultWidth().intValue();
        Dimension dim = new Dimension(boxWidth, boxHeight);
        drawTreeDiagram(newDiagram, model, dim, options);
        DefaultMainFrame mainFrame = ApplicationContext.getDefaultMainFrame();
        DiagramInternalFrame frame = mainFrame.addDiagramInternalFrame(newDiagram);
        return frame;
    } //end executeProcess()

    //
    // clean up diagram by removing any non-necessary objects, like contexts. 
    //
    private void cleanUpDiagram(DbBEDiagram diagram) throws DbException {
        DbRelationN relN = diagram.getComponents();
        DbEnumeration dbEnum = relN.elements(DbBEContextGo.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEContextGo go = (DbBEContextGo) dbEnum.nextElement();
            go.remove();
        } //end while
        dbEnum.close();
    } //end removeContent()

    private void performProcess(DbBEDiagram diagram, DbBEUseCase process,
            DefaultMutableTreeNode node, boolean onlyExploded) throws DbException {
        DbRelationN relN = process.getComponents();
        DbEnumeration dbEnum = relN.elements(DbBEUseCase.metaClass);

        while (dbEnum.hasMoreElements()) {
            DbBEUseCase subprocess = (DbBEUseCase) dbEnum.nextElement();
            if ((!onlyExploded) || (isExploded(subprocess))) {
                ProcessTreeModel.NodeContent content = new ProcessTreeModel.NodeContent(subprocess);
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(content);
                node.add(childNode);
                performProcess(diagram, subprocess, childNode, onlyExploded);
            }
        } //end while
        dbEnum.close();
    } //end performProcess()

    private boolean isExploded(DbBEUseCase subprocess) throws DbException {
        DbRelationN relN = subprocess.getComponents();
        DbEnumeration dbEnum = relN.elements(DbBEUseCase.metaClass);
        boolean isExploded = dbEnum.hasMoreElements();
        dbEnum.close();

        return isExploded;
    }

    /**
     ** private methods : class part
     **/
    private DiagramInternalFrame executePackage(DbJVPackage pack, TreeDiagramOptions options)
            throws DbException {
        DbOODiagram diagram = new DbOODiagram(pack);
        diagram.setName(TREE_DIAGRAM);

        //build the tree
        ProcessTreeModel.NodeContent content = new ProcessTreeModel.NodeContent(pack);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(content);
        ProcessTreeModel model = new ProcessTreeModel(root);
        performPackage(diagram, pack, root);

        //get tree size
        model.computeWidestLevel(options.isCenter(), options.isSpan());

        //draw and display tree diagram
        Dimension dim = new Dimension(100, 100);
        drawTreeDiagram(diagram, model, dim, options);
        DefaultMainFrame mainFrame = ApplicationContext.getDefaultMainFrame();
        DiagramInternalFrame frame = mainFrame.addDiagramInternalFrame(diagram);
        return frame;
    } //end executePackage()

    private void performPackage(DbOODiagram diagram, DbJVPackage pack, DefaultMutableTreeNode node)
            throws DbException {
        DbRelationN relN = pack.getComponents();
        DbEnumeration dbEnum = relN.elements(DbJVPackage.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVPackage subpack = (DbJVPackage) dbEnum.nextElement();
            ProcessTreeModel.NodeContent content = new ProcessTreeModel.NodeContent(subpack);
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(content);
            node.add(childNode);
            performPackage(diagram, subpack, childNode);
        } //end while
        dbEnum.close();
    } //end performPackage()

    private DiagramInternalFrame executeClass(DbJVClass claz, TreeDiagramOptions options)
            throws DbException {
        DbSMSSemanticalObject composite = (DbSMSSemanticalObject) claz.getComposite();
        if (!(composite instanceof DbJVPackage) && !(composite instanceof DbJVClassModel))
            return null;

        DbOODiagram diagram = new DbOODiagram(composite);
        diagram.setName(TREE_DIAGRAM);

        //build the tree
        ProcessTreeModel.NodeContent content = new ProcessTreeModel.NodeContent(claz);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(content);
        ProcessTreeModel model = new ProcessTreeModel(root);
        performClass(diagram, claz, root);

        //get tree size
        model.computeWidestLevel(options.isCenter(), options.isSpan());

        //draw and display tree diagram
        Dimension dim = new Dimension(100, 100);
        drawTreeDiagram(diagram, model, dim, options);
        DefaultMainFrame mainFrame = ApplicationContext.getDefaultMainFrame();
        DiagramInternalFrame frame = mainFrame.addDiagramInternalFrame(diagram);
        return frame;
    } //end executeClass()  

    private void performClass(DbOODiagram diagram, DbOOClass claz, DefaultMutableTreeNode node)
            throws DbException {
        DbRelationN relN = claz.getSubInheritances();
        DbEnumeration dbEnum = relN.elements(DbJVInheritance.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbOOInheritance inher = (DbOOInheritance) dbEnum.nextElement();
            DbOOClass subClaz = inher.getSubClass();
            ProcessTreeModel.NodeContent content = new ProcessTreeModel.NodeContent(subClaz);
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(content);
            node.add(childNode);
            performClass(diagram, subClaz, childNode);
        } //end while
        dbEnum.close();
    } //end performPackage()

    private void arrangeLevel(DbSMSDiagram diagram, ProcessTreeModel model, int level,
            TreeDiagramOptions options) throws DbException {
        ArrayList list = model.getNodesByLevels(level);
        int nb = list.size();
        for (int i = 0; i < nb; i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) list.get(i);
            ProcessTreeModel.NodeContent content = (ProcessTreeModel.NodeContent) node
                    .getUserObject();
            DbSMSSemanticalObject obj = content.semObj;
            if (obj instanceof DbJVPackage) {
                DbJVPackage pack = (DbJVPackage) obj;
                MetaClass mc = DbSMSPackageGo.metaClass;
                DbSMSPackageGo packGo = (DbSMSPackageGo) getGraphicalRep(pack, diagram, mc, options);
                Rectangle rect = packGo.getRectangle();
                int childrenCenter = computeChidrenCenter(diagram, node, options);
                if (childrenCenter != -1) {
                    Rectangle newRect = new Rectangle(childrenCenter, rect.y, rect.width,
                            rect.height);
                    packGo.setRectangle(newRect);
                }
            } //end if
        } //end for

    } //end arrangeLevel()

    private int computeChidrenCenter(DbSMSDiagram diagram, DefaultMutableTreeNode node,
            TreeDiagramOptions options) throws DbException {
        Enumeration enumeration = node.children();
        int cumulX = 0;
        int nbX = 0;
        while (enumeration.hasMoreElements()) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) enumeration.nextElement();
            ProcessTreeModel.NodeContent content = (ProcessTreeModel.NodeContent) child
                    .getUserObject();
            DbSMSSemanticalObject obj = content.semObj;
            if (obj instanceof DbJVPackage) {
                DbJVPackage pack = (DbJVPackage) obj;
                MetaClass mc = DbSMSPackageGo.metaClass;
                DbSMSPackageGo packGo = (DbSMSPackageGo) getGraphicalRep(pack, diagram, mc, options);
                Rectangle rect = packGo.getRectangle();
                cumulX += rect.x;
                nbX++;
            }
        } //end while

        int childrenCenter = -1;
        if (nbX > 0) {
            childrenCenter = cumulX / nbX;
        }

        return childrenCenter;
    } //end computeChidrenCenter()

    /**
     ** Relational part
     **/
    private DiagramInternalFrame executeOrModel(DbORDataModel orModel, TreeDiagramOptions options)
            throws DbException {
        DbORDiagram diagram = new DbORDiagram(orModel);
        diagram.setName(TREE_DIAGRAM);

        //build the tree
        ProcessTreeModel.NodeContent content = new ProcessTreeModel.NodeContent(orModel);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(content);
        ProcessTreeModel model = new ProcessTreeModel(root);
        performOrModel(diagram, orModel, root);

        //get tree size
        model.computeWidestLevel(options.isCenter(), options.isSpan());

        //draw and display tree diagram
        Dimension dim = new Dimension(100, 100);
        drawTreeDiagram(diagram, model, dim, options);
        DefaultMainFrame mainFrame = ApplicationContext.getDefaultMainFrame();
        DiagramInternalFrame frame = mainFrame.addDiagramInternalFrame(diagram);
        return frame;
    } //end executeOrModel()

    private void performOrModel(DbORDiagram diagram, DbORModel orModel, DefaultMutableTreeNode node)
            throws DbException {
        DbRelationN relN = orModel.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORModel.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORModel submodel = (DbORModel) dbEnum.nextElement();
            ProcessTreeModel.NodeContent content = new ProcessTreeModel.NodeContent(submodel);
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(content);
            node.add(childNode);
            performOrModel(diagram, submodel, childNode);
        } //end while
        dbEnum.close();
    } //end performPackage()

    /**
     ** Common methods
     **/
    private void drawTreeDiagram(DbSMSDiagram diagram, ProcessTreeModel model, Dimension dim,
            TreeDiagramOptions options) throws DbException {
        int largestWidth = model.getLargestWidth(options.isSpan());
        int widestLevel = model.getWidestLevel();
        int depth = model.getDepth();

        //init  
        int[] positionInLevel = new int[1 + depth];
        for (int i = 0; i < depth; i++) {
            positionInLevel[i] = 0;
        }

        //compute diagram size
        computeDiagramSize(diagram, largestWidth, depth, dim, options.isHorizontal());
        DbProject project = diagram.getProject();
        DbSMSLinkModel linkModel = getLinkModel(project);

        //  draw process boxes
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        Enumeration enumeration = root.breadthFirstEnumeration();
        while (enumeration.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
            ProcessTreeModel.NodeContent content = (ProcessTreeModel.NodeContent) node
                    .getUserObject();
            int position = content.position.intValue();
            DbSMSSemanticalObject semObj = content.semObj;
            int level = node.getLevel();
            int width = model.getWidth(level, options.isSpan());
            DbSMSGraphicalObject go = createGraphicalRep(diagram, semObj, options.fitProcesses());
            Rectangle rect = go.getRectangle();
            //Dimension boxDim = new Dimension(rect.width, rect.height);
            int relativePosition = options.isSpan() ? position : positionInLevel[level];
            Rectangle newRect = computeRectangle(level, relativePosition, width, largestWidth, dim,
                    options);
            go.setRectangle(newRect);

            //draw composition link, if any
            drawCompositionLink(diagram, linkModel, go, options);

            int weight = ProcessTreeModel.getWeight(node, options.isSpan());
            positionInLevel[level] += options.isSpan() ? weight : 1;
        } //end while

        //arrange diagram upward
        for (int i = widestLevel - 1; i >= 0; i--) {
            arrangeLevel(diagram, model, i, options);
        } //end for

        //  arrange diagram backward
        for (int i = widestLevel; i < depth; i++) {
            //arrangeLevel(diagram, model, i);
        } //end for

    } //end drawTreeDiagram()

    private DbSMSLinkModel getLinkModel(DbProject project) throws DbException {
        DbSMSLinkModel model = (DbSMSLinkModel) project.findComponentByName(
                DbSMSLinkModel.metaClass, TREE_DIAGRAM_LINKS);
        if (model == null) {
            model = new DbSMSLinkModel(project);
            model.setName(TREE_DIAGRAM_LINKS);
        }

        //clean-up link model
        DbRelationN relN = model.getComponents();
        DbEnumeration dbEnum = relN.elements(DbSMSLink.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSLink link = (DbSMSLink) dbEnum.nextElement();
            DbRelationN relN2 = link.getLinkGos();
            boolean hasDiagram = false;
            DbEnumeration dbEnum2 = relN2.elements(DbSMSLinkGo.metaClass);
            while (dbEnum2.hasMoreElements()) {
                DbSMSLinkGo linkGo = (DbSMSLinkGo) dbEnum2.nextElement();
                DbSMSDiagram diagram = (DbSMSDiagram) linkGo
                        .getCompositeOfType(DbSMSDiagram.metaClass);
                if (diagram != null) {
                    hasDiagram = true;
                    break;
                }
            }
            dbEnum2.close();

            if (!hasDiagram) {
                link.remove();
            }
        } //end while
        dbEnum.close();

        return model;
    } //end getLinkModel()

    private DbSMSGraphicalObject createGraphicalRep(DbSMSDiagram diagram,
            DbSMSSemanticalObject semObj, boolean autoFit) throws DbException {
        DbSMSGraphicalObject go = null;

        if (semObj instanceof DbJVPackage) {
            go = new DbSMSPackageGo(diagram, (DbJVPackage) semObj);
        } else if (semObj instanceof DbBEUseCase) {
            go = new DbBEUseCaseGo(diagram, (DbBEUseCase) semObj);
        } else if (semObj instanceof DbJVClass) {
            go = new DbOOAdtGo(diagram, (DbJVClass) semObj);
        } else if (semObj instanceof DbORModel) {
            go = new DbSMSPackageGo(diagram, (DbORModel) semObj);
        } //end if

        if (go != null) {
            go.setAutoFit(Boolean.valueOf(autoFit));
        }

        return go;
    } //end createGraphicalRep()

    //draw composition link, if any
    private void drawCompositionLink(DbSMSDiagram diagram, DbSMSLinkModel model,
            DbSMSGraphicalObject go, TreeDiagramOptions options) // isHorizontal) 
            throws DbException {
        DbSMSSemanticalObject pack = (DbSMSSemanticalObject) go.getSO();
        DbSMSSemanticalObject composite = getParentObject(pack);
        if (composite != null) {
            MetaClass mc = go.getMetaClass();
            DbSMSGraphicalObject compositeGo = (DbSMSGraphicalObject) getGraphicalRep(composite,
                    diagram, mc, options);
            if (compositeGo != null) {
                DbSMSLink link = new DbSMSLink(model);
                link.addToSourceObjects(pack);
                link.addToTargetObjects(composite);
                String linkName = pack.getName() + ">" + composite.getName();
                link.setName(linkName);
                DbSMSLinkGo linkGo = new DbSMSLinkGo(diagram, go, compositeGo, link);

                Rectangle box1 = go.getRectangle();
                Rectangle box2 = compositeGo.getRectangle();

                //draw path
                int nbPoints = 4;
                int[] xpoints, ypoints;
                if (options.isHorizontal()) {
                    xpoints = new int[] { box1.x, box1.x, box2.x, box2.x };
                    ypoints = new int[] { box1.y, mid(box1.y, box2.y), mid(box1.y, box2.y), box2.y };
                } else {
                    xpoints = new int[] { box1.x, mid(box1.x, box2.x), mid(box1.x, box2.x), box2.x };
                    ypoints = new int[] { box1.y, box1.y, box2.y, box2.y };
                } //end if

                Polygon polyline = new Polygon(xpoints, ypoints, nbPoints);
                linkGo.setPolyline(polyline);
                linkGo.setRightAngle(Boolean.TRUE);
                linkGo.setDashStyle(Boolean.FALSE);
            } //end if
        } //end if
    } //end drawCompositionLink()

    private DbSMSSemanticalObject getParentObject(DbSMSSemanticalObject obj) throws DbException {
        DbSMSSemanticalObject parent = null;

        if (obj instanceof DbJVPackage) {
            parent = (DbJVPackage) obj.getCompositeOfType(DbJVPackage.metaClass);
        } else if (obj instanceof DbBEUseCase) {
            parent = (DbBEUseCase) obj.getCompositeOfType(DbBEUseCase.metaClass);
        } else if (obj instanceof DbOOClass) {
            DbOOClass claz = (DbOOClass) obj;
            DbRelationN relN = claz.getSuperInheritances();
            DbEnumeration dbEnum = relN.elements(DbOOInheritance.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbOOInheritance inher = (DbOOInheritance) dbEnum.nextElement();
                DbJVClass superClass = (DbJVClass) inher.getSuperClass();
                if (!superClass.isInterface()) {
                    parent = superClass;
                    break;
                }

            } //end while
            dbEnum.close();
        } else if (obj instanceof DbORModel) {
            parent = (DbORModel) obj.getCompositeOfType(DbORModel.metaClass);
        } //end if

        return parent;
    }

    private DbSMSGraphicalObject getGraphicalRep(DbSMSSemanticalObject semObj,
            DbSMSDiagram diagram, MetaClass mc, TreeDiagramOptions options) throws DbException {
        DbSMSGraphicalObject go = null;
        DbRelationN relN = getGraphicalRelN(semObj);
        if (relN == null) {
            go = createGraphicalRep(diagram, semObj, options.fitProcesses());
        } else {
            DbEnumeration dbEnum = relN.elements(mc);
            while (dbEnum.hasMoreElements()) {
                DbSMSGraphicalObject aGo = (DbSMSGraphicalObject) dbEnum.nextElement();
                DbSMSDiagram aDiag = (DbSMSDiagram) aGo.getCompositeOfType(DbSMSDiagram.metaClass);
                if (aDiag != null) {
                    if (aDiag.equals(diagram)) {
                        go = aGo;
                        break;
                    }
                }
            } //end while
            dbEnum.close();
        }

        return go;
    } //end getGraphicalRep()

    private DbRelationN getGraphicalRelN(DbSMSSemanticalObject semObj) throws DbException {
        DbRelationN relN = null;

        if (semObj instanceof DbJVPackage) {
            DbJVPackage pack = (DbJVPackage) semObj;
            relN = pack.getPackageGos();
        } else if (semObj instanceof DbBEUseCase) {
            DbBEUseCase process = (DbBEUseCase) semObj;
            relN = process.getClassifierGos();
        } else if (semObj instanceof DbJVClass) {
            DbJVClass claz = (DbJVClass) semObj;
            relN = claz.getClassifierGos();
        } else if (semObj instanceof DbORDataModel) {
            DbORDataModel orModel = (DbORDataModel) semObj;
            relN = orModel.getPackageGos();
        } //end if

        return relN;
    } //end getGraphicalRelN()

    private void computeDiagramSize(DbSMSDiagram diagram, int largestWidth, int depth,
            Dimension box, boolean isHorizontal) throws DbException {
        int width, height;

        if (isHorizontal) {
            width = box.width * 2 + largestWidth * box.width * 2;
            height = box.height + depth * box.height * 2;
        } else {
            height = box.width * 2 + largestWidth * box.width * 2;
            width = box.height + depth * box.height * 2;
        } //end if 

        PageFormat fmt = diagram.getPageFormat();
        double pageWidth = fmt.getWidth();
        double pageHeight = fmt.getHeight();

        int xPages = (int) Math.ceil(width / pageWidth);
        int yPages = (int) Math.ceil(height / pageHeight);

        Dimension nbPages = new Dimension(xPages, yPages);
        diagram.setNbPages(nbPages);
    } //end computeDiagramSize()

    private Rectangle computeRectangle(int level, int positionInLevel, int width, int largestWidth,
            Dimension box, TreeDiagramOptions options) throws DbException {
        boolean isHorizontal = options.isHorizontal();
        boolean isCenter = options.isCenter();
        boolean isSpan = options.isSpan();

        int x, y, gap = 0;

        if (isCenter && !isSpan) {
            gap = isHorizontal ? ((largestWidth - width) * box.width)
                    : ((largestWidth - width) * box.height);
        }

        if (isHorizontal) {
            x = box.width * 2 + gap + positionInLevel * box.width * 2;
            y = box.height / 2 + (level * box.height * 2);
        } else {
            x = box.width * 2 + (level * box.width * 2);
            y = box.height / 2 + gap + positionInLevel * box.height * 2;
        } //end if 

        Rectangle rect = new Rectangle(x, y, box.width, box.height);
        return rect;
    } //getRectangle()

    private int mid(int low, int high) {
        return (int) (low + high) / 2;
    }

    protected Template getFileNotFoundRule() {
        Template templ = new Template("fileNotFoundRule", "File not Found"); //NOT LOCALIZABLE
        return templ;
    }

	@Override
	public OptionGroup getOptionGroup() {
		return null;
	}

	@Override
	public PluginAction getPluginAction() {
		return new PluginDefaultAction(this);
	}
} //end ProcessTree

