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

package org.modelsphere.sms.be.graphic.tool;

import java.awt.Image;
import java.awt.Polygon;

import javax.swing.JOptionPane;

import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.tool.LineTool;
import org.modelsphere.jack.graphic.tool.Tool;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEFlowGo;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.util.BEUtility;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.be.text.BETextUtil;
import org.modelsphere.sms.db.DbSMSClassifier;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSNotice;
import org.modelsphere.sms.db.DbSMSSemanticalObject;

public class BEFlowTool extends LineTool {
    public static final Image kImageFlowCreationTool = GraphicUtil.loadImage(Tool.class,
            "resources/angulararrowfill.gif"); // NOT LOCALIZABLE -
    // tool image
    public static final Image kImageRightAngleFlowCreationTool = GraphicUtil.loadImage(Tool.class,
            "resources/rightanglearrowfill.gif"); // NOT
    // LOCALIZABLE
    // -
    // tool
    // image
    private String m_actionName;

    public BEFlowTool(String text, String[] tooltips, Image image, Image[] secondaryImages) {
        super(0, text, tooltips, image, secondaryImages, 0);
        m_actionName = BETextUtil.getSingleton().getCreationText(DbBEFlow.metaClass);
    }

    protected final boolean isSourceAcceptable(GraphicNode source) {
        return ((source instanceof ActionInformation) || source == null);
    }

    protected boolean isDestAcceptable(GraphicNode source, GraphicNode dest) {
        return ((dest instanceof ActionInformation) || dest == null);
    }

    protected final void createLine(GraphicNode source, GraphicNode dest, Polygon poly) {
        try {
            createFlow((ActionInformation) source, (ActionInformation) dest, poly);
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
    }

    public String getText(DbObject notation) {
        String pattern = LocaleMgr.action.getString("0Creation");

        Terminology terminology = null;
        try {
            notation.getDb().beginReadTrans();
            terminology = Terminology.findTerminology(notation);
            notation.getDb().commitTrans();
        } catch (DbException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        MetaClass mc = DbBEFlow.metaClass;
        String term = (terminology == null) ? mc.getGUIName() : terminology.getTerm(mc);
        String text = MessageFormat.format(pattern, new Object[] { term });
        return text;
    } // end getText()

    private void createFlow(ActionInformation source, ActionInformation dest, Polygon poly)
            throws DbException {
        DbBEDiagram diagGO = (DbBEDiagram) ((ApplicationDiagram) model).getDiagramGO();
        diagGO.getDb().beginTrans(Db.WRITE_TRANS, m_actionName);
        DbSMSDiagram parent = diagGO.getParentDiagram();
        // if a tree diagram
        if (parent != null) {
            String ERROR = LocaleMgr.screen.getString("Error");
            JOptionPane.showMessageDialog(MainFrame.getSingleton(), BEActorTool.CANNOT_CREATE,
                    ERROR, JOptionPane.ERROR_MESSAGE);
        } else {

            DbSMSClassifier firstEnd = null, secondEnd = null;
            if (source != null) {
                DbSMSSemanticalObject semObj = (DbSMSSemanticalObject) ((ActionInformation) source)
                        .getSemanticalObject();
                if (semObj instanceof DbSMSClassifier) {
                    firstEnd = (DbSMSClassifier) semObj;
                }
            } // end if

            if (dest != null) {
                DbSMSSemanticalObject semObj = (DbSMSSemanticalObject) ((ActionInformation) dest)
                        .getSemanticalObject();
                if (semObj instanceof DbSMSClassifier) {
                    secondEnd = (DbSMSClassifier) semObj;
                }
            } // end if

            { // create flow
                BEUtility util = BEUtility.getSingleInstance();
                DbObject composite = (DbObject) diagGO.getComposite();
                DbBENotation notation = diagGO.findNotation();
                DbBEFlow flow = util.createFlow(composite, notation);

                if (flow != null) {
                    flow.setFirstEnd(firstEnd);
                    flow.setSecondEnd(secondEnd);

                    String pattern = notation.getFlowIdPrefix();
                    if (pattern != null) {
                        String strValue = flow.getIdentifier();
                        if (strValue == null) {
                            strValue = "";
                        } // end if
                        String id = MessageFormat.format(pattern, new Object[] { strValue });
                        flow.setIdentifier(id);
                    } // end if

                    DbSMSGraphicalObject sourceGo = null;
                    DbSMSGraphicalObject destGo = null;

                    if (source != null && firstEnd != null)
                        sourceGo = (DbSMSGraphicalObject) source.getGraphicalObject();

                    if (dest != null && secondEnd != null)
                        destGo = (DbSMSGraphicalObject) dest.getGraphicalObject();

                    util.initFlow(flow, notation);
                    DbBEFlowGo flowGo = new DbBEFlowGo(diagGO, sourceGo, destGo, flow);
                    flowGo.setPolyline(poly);
                    flowGo.setRightAngle(new Boolean(rightAngle));
                } // end if
            } // end if
        } // end if
        diagGO.getDb().commitTrans();
    }

}
