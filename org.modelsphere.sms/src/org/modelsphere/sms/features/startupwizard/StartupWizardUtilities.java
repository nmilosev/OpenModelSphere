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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/
package org.modelsphere.sms.features.startupwizard;

import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.*;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.tool.Tool;
import org.modelsphere.sms.SMSToolkit;
import org.modelsphere.sms.be.db.*;
import org.modelsphere.sms.be.graphic.tool.*;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.be.text.BETextUtil;
import org.modelsphere.sms.db.DbSMSNotation;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.util.DbInitialization;
import org.modelsphere.sms.or.graphic.tool.*;

public class StartupWizardUtilities {

    static void displayAvailableTools(StartupWizardModel model, JPanel iconPanel, MetaClass modelMC)
            throws DbException {
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

        int nMode = terminologyUtil.getModelLogicalMode(model.getChosenModel());

        iconPanel.removeAll();
        Tool[] tools = SMSToolkit.getTools();

        String notationName = null;
        Image icoUseCase = null;
        String icoUseCaseTxt = null;
        Image icoStore = null;
        String icoStoreTxt = null;

        String icoFlowTxt = null;

        Image icoActor = null;
        String icoActorTxt = null;

        DbSMSNotation notation = model.getNotation();

        if (notation != null) {
            notation.getDb().beginReadTrans();
            notationName = notation.getName();
            Terminology terminology = Terminology.findTerminologyByName(notationName);
            notation.getDb().commitTrans();

            Icon ico = terminology.getIcon(DbBEUseCase.metaClass);
            icoUseCase = ((ImageIcon) ico).getImage();
            BETextUtil beTextUtil = BETextUtil.getSingleton();

            icoUseCaseTxt = beTextUtil.getCreationText(DbBEUseCase.metaClass, terminology);

            ico = terminology.getIcon(DbBEStore.metaClass);
            icoStore = ((ImageIcon) ico).getImage();
            icoStoreTxt = beTextUtil.getCreationText(DbBEStore.metaClass, terminology);

            icoFlowTxt = beTextUtil.getCreationText(DbBEFlow.metaClass, terminology);

            ico = terminology.getIcon(DbBEActor.metaClass);
            icoActor = ((ImageIcon) ico).getImage();
            icoActorTxt = beTextUtil.getCreationText(DbBEActor.metaClass, terminology);

        }

        for (int i = 0; i < tools.length; i++) {
            Tool tool = tools[i];
            String toolbar = tool.getToolBar();
            if (toolbar == null) {
                MetaClass[] mcs = tool.getSupportedDiagramMetaClasses();
                boolean applicable = toolIsApplicable(mcs, modelMC);
                Image image = null;
                ImageIcon icon = null;
                String text = "";
                if (applicable) {
                    text = (notation == null) ? tool.getText() : tool.getText(notation);
                    if (tool instanceof ORTableTool
                            && nMode == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                        image = tool.getIcon();
                        text = ORTableTool.kEntityCreationTool;
                    }
                    if (tool instanceof ORAssociationTool
                            && nMode == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                        image = tool.getIcon();
                        text = ORAssociationTool.kRelationshipCreationTool;
                    }
                    if (tool instanceof ORViewTool
                            && nMode == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                        image = null;
                    else if (tool instanceof ORSubModelTool
                            && nMode == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                        image = ((ImageIcon) terminologyUtil.getConceptualModelIcon()).getImage();
                    else if (tool instanceof BEActorTool) {
                        image = icoActor;
                        text = icoActorTxt;
                    } else if (tool instanceof BEFlowTool) {
                        // image = icoFlow;
                        text = icoFlowTxt;
                    } else if (tool instanceof BEStoreTool) {
                        image = icoStore;
                        text = icoStoreTxt;
                    } else if (tool instanceof BEUseCaseTool) {
                        if (tool instanceof BEInitialStateTool) {
                            image = GraphicUtil.loadImage(DbSMSStereotype.class,
                                    "resources/start-small.gif");
                            text = LocaleMgr.action.getString("InitialStateCreation");;
                        } else if (tool instanceof BEFinalStateTool) {
                            image = GraphicUtil.loadImage(DbSMSStereotype.class,
                                    "resources/end-small.gif");
                            text = LocaleMgr.action.getString("FinalStateCreation");;
                        } else if (tool instanceof BEDecisionTool) {
                            image = GraphicUtil.loadImage(DbSMSStereotype.class,
                                    "resources/decision-small.gif");
                            text = LocaleMgr.action.getString("DecisionCreation");
                        } else if (tool instanceof BESynchronizationTool) {
                            image = GraphicUtil.loadImage(DbSMSStereotype.class,
                                    "resources/horiz-synchro-small.gif");
                            text = LocaleMgr.action.getString("Synchronization");
                        } else {
                            image = icoUseCase;
                            text = icoUseCaseTxt;
                        }
                    } else
                        image = tool.getIcon();

                    if (image != null)
                        icon = new ImageIcon(image);

                    if (icon != null) {

                        boolean isActivity = false, isStatechart = false;

                        if (notation != null) {

                            notation.getDb().beginReadTrans();
                            int masterId = notation.getMasterNotationID().intValue();
                            notation.getDb().commitTrans();
                            if (masterId == DbInitialization.UML_STATE_DIAGRAM)
                                isStatechart = true;
                            if (masterId == DbInitialization.UML_ACTIVITY_DIAGRAM)
                                isActivity = true;

                            if (tool instanceof BEInitialStateTool && !(isStatechart || isActivity))
                                continue;
                            if (tool instanceof BEFinalStateTool && !(isStatechart || isActivity))
                                continue;
                            if (tool instanceof BEDecisionTool && !(isActivity))
                                continue;
                            if (tool instanceof BESynchronizationTool && !(isStatechart))
                                continue;
                        }

                        JLabel label = new JLabel(text, icon, SwingConstants.LEFT);
                        GridBagConstraints constr = new GridBagConstraints(0, i, 1, 1, 1.0, 0.0,
                                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(
                                        6, 24, 6, 6), 0, 0);
                        iconPanel.add(label, constr);
                    }
                } // end if
            } // end if
        } // end for
        iconPanel.add(Box.createVerticalGlue(), new GridBagConstraints(0, tools.length, 1, 1, 1.0,
                1.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 12, 0, 12), 0,
                0));

    }

    private static boolean toolIsApplicable(MetaClass[] mcs, MetaClass mc) {
        boolean applicable = (mcs == null) ? true : false;

        if (mcs != null) {
            for (int i = 0; i < mcs.length; i++) {
                if (mcs[i] == mc) {
                    applicable = true;
                    break;
                }
            }
        }

        return applicable;
    }

}
