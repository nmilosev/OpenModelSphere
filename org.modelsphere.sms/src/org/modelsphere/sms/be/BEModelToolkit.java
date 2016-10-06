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

package org.modelsphere.sms.be;

import java.awt.Component;
import java.awt.Image;
import java.awt.Point;
import java.text.MessageFormat;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.tool.LineTool;
import org.modelsphere.jack.graphic.tool.Tool;
import org.modelsphere.jack.graphic.tool.ToolButtonGroup;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.CurrentFocusListener;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.GraphicComponentFactory;
import org.modelsphere.sms.SMSToolkit;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEQualifier;
import org.modelsphere.sms.be.db.DbBEResource;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.graphic.tool.BEActorTool;
import org.modelsphere.sms.be.graphic.tool.BEDecisionTool;
import org.modelsphere.sms.be.graphic.tool.BEExplodeTool;
import org.modelsphere.sms.be.graphic.tool.BEFinalStateTool;
import org.modelsphere.sms.be.graphic.tool.BEFlowTool;
import org.modelsphere.sms.be.graphic.tool.BEInitialStateTool;
import org.modelsphere.sms.be.graphic.tool.BEQualifierLinkTool;
import org.modelsphere.sms.be.graphic.tool.BEQualifierSetLinksTool;
import org.modelsphere.sms.be.graphic.tool.BEQualifierUnlinkTool;
import org.modelsphere.sms.be.graphic.tool.BERenumberingTool;
import org.modelsphere.sms.be.graphic.tool.BEResourceLinkTool;
import org.modelsphere.sms.be.graphic.tool.BEResourceSetLinksTool;
import org.modelsphere.sms.be.graphic.tool.BEResourceUnlinkTool;
import org.modelsphere.sms.be.graphic.tool.BEStoreTool;
import org.modelsphere.sms.be.graphic.tool.BESynchronizationTool;
import org.modelsphere.sms.be.graphic.tool.BEUseCaseTool;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.be.text.BETextUtil;
import org.modelsphere.sms.db.util.DbInitialization;
import org.modelsphere.sms.graphic.tool.SMSGraphicalLinkTool;
import org.modelsphere.sms.graphic.tool.SMSNoticeTool;

public final class BEModelToolkit extends SMSToolkit implements CurrentFocusListener {

    BEModelToolkit() {
        ApplicationContext.getFocusManager().addCurrentFocusListener(this);
    }

    // SET IMAGES
    private static final Image kImageProcessCreationTool = GraphicUtil.loadImage(BEModule.class,
            "db/resources/dbbeusecase.gif"); // NOT
    // LOCALIZABLE
    // -
    // tool
    // image
    private static final Image kImageEntityCreationTool = GraphicUtil.loadImage(BEModule.class,
            "db/resources/dbbeactor.gif"); // NOT
    // LOCALIZABLE
    // -
    // tool
    // image
    private static final Image kImageStoreCreationTool = GraphicUtil.loadImage(BEModule.class,
            "db/resources/dbbestore.gif"); // NOT LOCALIZABLE -
    // tool image
    private static final Image kImageRenumberingTool = GraphicUtil.loadImage(BEModule.class,
            "international/resources/renum.gif"); // NOT
    // LOCALIZABLE
    // - tool
    // image
    private static final Image kImageAngularGraphicalLinkTool = GraphicUtil.loadImage(Tool.class,
            "resources/angulardependency.gif"); // NOT
    // LOCALIZABLE
    // -
    // tool
    // image
    private static final Image kImageRightAngleGraphicalLinkTool = GraphicUtil.loadImage(
            Tool.class, "resources/rightangledependency.gif"); // NOT
    // LOCALIZABLE
    // -
    // tool
    // image

    // SET TEXT INDEPENDENT FROM NOTATION
    private static final String kRenumberingTool = LocaleMgr.screen.getString("RenumberingTool");
    private static final String kExplodeTool = LocaleMgr.screen.getString("ExplodeTool");
    private static final String kInitialState = LocaleMgr.action.getString("InitialStateCreation");
    private static final String kFinalState = LocaleMgr.action.getString("FinalStateCreation");
    private static final String kDecisionState = LocaleMgr.action.getString("DecisionCreation");
    private static final String kSynchronization = LocaleMgr.action.getString("Synchronization");
    private static final String kGraphicalLink = SMSGraphicalLinkTool.GRAPHICAL_LINKS;

    private static final String kAddLinkTool = LocaleMgr.misc.getString("addLinkTool");
    private static final Image kImageAddLinkTool = GraphicUtil.loadImage(BEModule.class,
            "international/resources/addlink.gif"); // NOT
    // LOCALIZABLE
    // - tool
    // image

    private static final String kRemoveLinkTool = LocaleMgr.misc.getString("removeLinkTool");
    private static final Image kImageRemoveLinkTool = GraphicUtil.loadImage(BEModule.class,
            "international/resources/removelink.gif"); // NOT
    // LOCALIZABLE
    // -
    // tool
    // image
    private static final String kSpecify0 = LocaleMgr.misc.getString("Specify0");
    private static final String kAssignmentTool = LocaleMgr.misc.getString("assignmentTool");
    private static final Image kImageAssignmentTool = GraphicUtil.loadImage(BEModule.class,
            "international/resources/setlink.gif"); // NOT
    // LOCALIZABLE
    // - tool
    // image

    // Constants for graphical links
    private static final String[] kGraphicalLinkTooltips = null; // new String[]
    // {};
    private static final Image[] kGraphicalLinkSecondaryImages = null; // new

    // Image[]
    // {};

    public GraphicComponentFactory createGraphicalComponentFactory() {
        return new BEGraphicComponentFactory();
    }

    protected MetaClass[] getSupportedPackage() {
        return new MetaClass[] { DbBEModel.metaClass, DbBEUseCase.metaClass };
    }

    public DbObject createDbObject(MetaClass abstractMetaClass, DbObject composite,
            Object[] parameters) throws DbException {
        return null;
    }

    public DbObject createDbObject(DbObject baseobject, DbObject composite, Object[] parameters)
            throws DbException {
        return null;
    }

    public boolean isMetaClassSupported(MetaClass metaClass) {
        return (isAssignableFrom(DbBEActor.metaClass, metaClass)
                || isAssignableFrom(DbBEFlow.metaClass, metaClass)
                || isAssignableFrom(DbBEUseCase.metaClass, metaClass)
                || isAssignableFrom(DbBEQualifier.metaClass, metaClass)
                || isAssignableFrom(DbBEResource.metaClass, metaClass) || isAssignableFrom(
                DbBEStore.metaClass, metaClass));
    }

    public Tool[] createTools() {
        BETextUtil util = BETextUtil.getSingleton();
        String processCreationTool = util.getCreationText(DbBEUseCase.metaClass);
        String entityCreationTool = util.getCreationText(DbBEActor.metaClass);
        String storeCreationTool = util.getCreationText(DbBEStore.metaClass);
        String flowCreationTool = util.getCreationText(DbBEFlow.metaClass);

        Tool usecaseTool = new BEUseCaseTool(processCreationTool, kImageProcessCreationTool);
        Tool actorTool = new BEActorTool(entityCreationTool, kImageEntityCreationTool);
        Tool storeTool = new BEStoreTool(storeCreationTool, kImageStoreCreationTool);
        Tool flowTool = new BEFlowTool(flowCreationTool, new String[] {
                LineTool.kRightAngleTooltips, LineTool.kAngularLineTooltips },
                BEFlowTool.kImageFlowCreationTool, new Image[] {
                        BEFlowTool.kImageRightAngleFlowCreationTool,
                        BEFlowTool.kImageFlowCreationTool });

        Tool[] tools = new Tool[] {
                actorTool,
                usecaseTool,
                new BEInitialStateTool(kInitialState),
                new BEFinalStateTool(kFinalState),
                new BEDecisionTool(kDecisionState),
                new BESynchronizationTool(kSynchronization),
                storeTool,
                flowTool,
                new SMSNoticeTool(),
                new BEExplodeTool(kExplodeTool),
                new BERenumberingTool(kRenumberingTool, kImageRenumberingTool),
                new SMSGraphicalLinkTool(kGraphicalLink, new String[] { kGraphicalLink,
                        kGraphicalLink }, kImageAngularGraphicalLinkTool, new Image[] {
                        kImageRightAngleGraphicalLinkTool, kImageAngularGraphicalLinkTool }),
                new BEResourceLinkTool(kAddLinkTool, kImageAddLinkTool),
                new BEResourceUnlinkTool(kRemoveLinkTool, kImageRemoveLinkTool),
                new BEResourceSetLinksTool(kAssignmentTool, kImageAssignmentTool),
                new BEQualifierLinkTool(kAddLinkTool, kImageAddLinkTool),
                new BEQualifierUnlinkTool(kRemoveLinkTool, kImageRemoveLinkTool),
                new BEQualifierSetLinksTool(kAssignmentTool, kImageAssignmentTool), };
        return tools;
    } // end createTools()

    public void currentFocusChanged(Object oldFocusObject, Object focusObject) throws DbException {
        if (focusObject instanceof ApplicationDiagram) {
            ApplicationDiagram appDiag = (ApplicationDiagram) focusObject;
            DiagramView dgmView = appDiag.getMainView();
            DbObject dbo = ((ApplicationDiagram) focusObject).getSemanticalObject();
            Tool[] tools = dgmView.getTools();
            int specialToolIndex = -1;

            if (dbo instanceof DbBEUseCase) {

                DbBEUseCase useCase = (DbBEUseCase) dbo;
                BETextUtil beTextUtil = BETextUtil.getSingleton();
                DbBENotation beNotation = ((DbBEDiagram) appDiag.getDiagramGO())
                        .findMasterNotation();

                boolean isActivity = false, isStatechart = false;

                int masterId = beNotation.getMasterNotationID().intValue();
                if (masterId == DbInitialization.UML_STATE_DIAGRAM)
                    isStatechart = true;
                if (masterId == DbInitialization.UML_ACTIVITY_DIAGRAM)
                    isActivity = true;

                Terminology terminology = Terminology.findTerminologyByName(beNotation.getName());

                Icon ico = terminology.getIcon(DbBEUseCase.metaClass);
                Image icoUseCase = ((ImageIcon) ico).getImage();
                String icoUseCaseTxt = beTextUtil.getCreationText(DbBEUseCase.metaClass,
                        terminology);

                ico = terminology.getIcon(DbBEStore.metaClass);
                Image icoStore = ((ImageIcon) ico).getImage();
                String icoStoreTxt = beTextUtil.getCreationText(DbBEStore.metaClass, terminology);

                // ico = terminology.getIcon(DbBEFlow.metaClass);
                // Image icoFlow = ((ImageIcon)ico).getImage();
                String icoFlowTxt = beTextUtil.getCreationText(DbBEFlow.metaClass, terminology);

                ico = terminology.getIcon(DbBEActor.metaClass);
                Image icoActor = ((ImageIcon) ico).getImage();
                String icoActorTxt = beTextUtil.getCreationText(DbBEActor.metaClass, terminology);

                ToolButtonGroup toolButtonGroup = dgmView.getToolGroup();
                for (int i = 0; i < tools.length; i++) {

                    if (tools[i] instanceof BEInitialStateTool) {
                        toolButtonGroup.setVisible(i, isActivity || isStatechart);
                    } else if (tools[i] instanceof BEFinalStateTool) {
                        toolButtonGroup.setVisible(i, isActivity || isStatechart);
                    } else if (tools[i] instanceof BEDecisionTool) {
                        toolButtonGroup.setVisible(i, isActivity);
                    } else if (tools[i] instanceof BESynchronizationTool) {
                        toolButtonGroup.setVisible(i, isActivity);
                    }

                    else if (tools[i] instanceof BEUseCaseTool) {
                        tools[i].setImage(icoUseCase);
                        tools[i].setText(icoUseCaseTxt);
                        ((BEUseCaseTool) tools[i]).setCursor(AwtUtil.createCursor(icoUseCase,
                                new Point(8, 8), icoUseCaseTxt, true));
                    } else if (tools[i] instanceof BEStoreTool) {
                        tools[i].setImage(icoStore);
                        tools[i].setText(icoStoreTxt);
                        ((BEStoreTool) tools[i]).setCursor(AwtUtil.createCursor(icoStore,
                                new Point(8, 8), icoStoreTxt, true));
                    } else if (tools[i] instanceof BEFlowTool) {
                        // tools[i].setImage(icoFlow);
                        tools[i].setText(icoFlowTxt);
                        /*
                         * if(isSequenceDiagram){ BEFlowTool flowTool = (BEFlowTool)tools[i];
                         * flowTool.setAuxiliaryIndex(0);
                         * 
                         * }
                         */
                    } else if (tools[i] instanceof BEExplodeTool) {
                        String text = MessageFormat.format(kSpecify0,
                                new Object[] { terminology.getTerm(DbBEUseCase.metaClass) });

                        tools[i].setText(text);
                    } else if (tools[i] instanceof BEActorTool) {
                        tools[i].setImage(icoActor);
                        tools[i].setText(icoActorTxt);
                        ((BEActorTool) tools[i]).setCursor(AwtUtil.createCursor(icoActor,
                                new Point(8, 8), icoActorTxt, true));
                    }

                }
                dgmView.setTools(tools);
                toolButtonGroup.currentFocusChanged(oldFocusObject, focusObject);
            }
        }
    }
} // end BeModelToolkit
