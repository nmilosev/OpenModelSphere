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

package org.modelsphere.sms.graphic.tool;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.Line;
import org.modelsphere.jack.graphic.LineLabel;
import org.modelsphere.jack.graphic.ZoneBox;
import org.modelsphere.jack.graphic.shape.RectangleShape;
import org.modelsphere.jack.graphic.zone.CellID;
import org.modelsphere.jack.graphic.zone.SingletonZone;
import org.modelsphere.jack.graphic.zone.TableZone;
import org.modelsphere.jack.graphic.zone.Zone;
import org.modelsphere.jack.graphic.zone.ZoneCell;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DbGraphic;
import org.modelsphere.jack.srtool.graphic.DbSelectTool;
import org.modelsphere.jack.srtool.graphic.DbTextFieldCellEditor;
import org.modelsphere.jack.srtool.graphic.FreeText;
import org.modelsphere.jack.srtool.graphic.SrLine;
import org.modelsphere.jack.srtool.graphic.Stamp;
import org.modelsphere.sms.be.db.DbBEContextGo;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEFlowGo;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.graphic.BEContext;
import org.modelsphere.sms.be.graphic.BEFlow;
import org.modelsphere.sms.be.graphic.BEFlowLabel;
import org.modelsphere.sms.be.graphic.BEStamp;
import org.modelsphere.sms.be.graphic.tool.BESelectToolCommand;
import org.modelsphere.sms.db.DbSMSClassifier;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.srtypes.SMSDisplayDescriptor;
import org.modelsphere.sms.graphic.NoticeBox;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.graphic.OOAssociation;
import org.modelsphere.sms.oo.graphic.OOStamp;
import org.modelsphere.sms.or.db.DbORStyle;
import org.modelsphere.sms.or.graphic.ORAssociation;
import org.modelsphere.sms.or.graphic.ORStamp;

public class SMSSelectTool extends DbSelectTool {
    private BESelectToolCommand m_selectToolCommand = new BESelectToolCommand();

    public SMSSelectTool(String text, Image icon) {
        super(0, text, icon);
    }

    protected void setLineNode(Line line, GraphicNode node, Polygon poly, int where) {
        if (((ActionInformation) line).getGraphicalObject() instanceof DbBEFlowGo) {
            try {
                DbBEFlowGo lineGo = (DbBEFlowGo) ((ActionInformation) line).getGraphicalObject();
                DbBEFlow lineSo = (DbBEFlow) ((ActionInformation) line).getSemanticalObject();
                lineGo.getDb().beginReadTrans();
                boolean onlyOneGo = (lineSo.getFlowGos().size() == 1 ? true : false);
                DbObject nodeGo = null;
                DbObject nodeSo = null;
                if (node != null) {
                    nodeGo = ((ActionInformation) node).getGraphicalObject();
                    nodeSo = ((ActionInformation) node).getSemanticalObject();
                }
                DbObject oldNodeSo = null;
                DbObject oldNodeGo = null;
                if (where == Line.AT_END1) {
                    oldNodeGo = lineGo.getFrontEndGo();
                    oldNodeSo = lineSo.getFirstEnd();
                } else {
                    oldNodeGo = lineGo.getBackEndGo();
                    oldNodeSo = lineSo.getSecondEnd();
                }
                lineGo.getDb().commitTrans();
                if (oldNodeGo == nodeGo && nodeGo != null)
                    return;
                if (!onlyOneGo && oldNodeSo != nodeSo)
                    return;

                lineGo.getDb()
                        .beginTrans(Db.WRITE_TRANS, LocaleMgr.action.getString("lineReshape"));
                if (where == Line.AT_END1) {
                    lineGo.setFrontEndGo((DbSMSGraphicalObject) nodeGo);
                    lineSo.setFirstEnd(null);
                    if (nodeSo instanceof DbSMSClassifier) {
                        lineSo.setFirstEnd((DbSMSClassifier) nodeSo);
                    }
                } else {
                    lineGo.setBackEndGo((DbSMSGraphicalObject) nodeGo);
                    lineSo.setSecondEnd(null);
                    if (nodeSo instanceof DbSMSClassifier) {
                        lineSo.setSecondEnd((DbSMSClassifier) nodeSo);
                    }
                }
                lineGo.set(DbGraphic.fLineGoPolyline, poly);
                lineGo.getDb().commitTrans();
            } catch (Exception ex) {
                org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(view, ex);
            }
        } else
            super.setLineNode(line, node, poly, where);
    } // end

    // Called by translateComponents()
    protected final void translateComponentsAfter(DbObject diagramGo, GraphicComponent[] gcs,
            int dx, int dy) throws DbException {
        // TODO : move related notices accordingly

        if (diagramGo instanceof DbBEDiagram) {
            m_selectToolCommand.execute((DbBEDiagram) diagramGo, gcs, dx, dy);
        }
    } // end translateComponents()

    private TableZone.BoundaryInfoStruct m_pressedCellBoundaryInfo = null;

    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        TableZone.BoundaryInfoStruct boundaryInfo = isMouseOverCellBoundary(view, e);
        if (boundaryInfo != null) {
            if ((m_pressedCellBoundaryInfo == null) && (boundaryInfo.boundary != -1)) {
                boundaryInfo.setMousePosition(e.getX(), e.getY());
                m_pressedCellBoundaryInfo = boundaryInfo; // to be used in
                // mouseRelease()
                Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
                view.setCursor(cursor);
                // change mouse Aspect?
            } // end if
        } // end if
    } // end mousePressed()

    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        GraphicComponent gc = model.graphicAt(view, e.getX(), e.getY(), 0xffffffff, true);
        if ((gc != null) && (gc instanceof NoticeBox)) {
            NoticeBox noticeBox = (NoticeBox) gc;
            int xPressed = e.getX();
            int yPressed = e.getY();
            CellID cellID = noticeBox.cellAt(view, xPressed, yPressed);
            if (e != null)
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1)
                    noticeBox.mouseReleased(cellID);
        } // end if
        else if ((gc != null) && (gc instanceof FreeText)) {
            int xPressed = e.getX();
            int yPressed = e.getY();
            FreeText freeText = (FreeText) gc;
            CellID cellID = freeText.cellAt(view, xPressed, yPressed);
            if (e != null)
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1)
                    freeText.mouseReleased(cellID);
        } else if ((gc != null)
                && (gc instanceof ORStamp || gc instanceof BEStamp || gc instanceof OOStamp)) {
            Stamp stamp = null;
            if (gc instanceof ORStamp)
                stamp = (ORStamp) gc;
            else if (gc instanceof OOStamp)
                stamp = (OOStamp) gc;
            else if (gc instanceof BEStamp)
                stamp = (BEStamp) gc;
            int xPressed = e.getX();
            int yPressed = e.getY();
            CellID cellID = stamp.cellAt(view, xPressed, yPressed);
            if (e != null)
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    if (gc instanceof ORStamp)
                        ((ORStamp) stamp).mouseReleased(cellID);
                    else if (gc instanceof OOStamp)
                        ((OOStamp) stamp).mouseReleased(cellID);
                    else if (gc instanceof BEStamp)
                        ((BEStamp) stamp).mouseReleased(cellID);
                }
        } // end if

        int nClickCount = e.getClickCount();
        if (nClickCount >= 2) {
            if (gc instanceof OOAssociation || gc instanceof ORAssociation || gc instanceof BEFlow) {
                try {
                    SrLine srline = (SrLine) gc;
                    LineLabel centerLabel = null;
                    centerLabel = srline.getCenterLabel();
                    if (centerLabel == null) {

                        // //
                        // if the line's "displayed descriptor" is a name,
                        // proceed
                        // with the installation of the editor

                        DbSMSGraphicalObject lineGo = (DbSMSGraphicalObject) srline
                                .getGraphicalObject();
                        lineGo.getDb().beginReadTrans();
                        int currentDescriptor = -1;
                        if (gc instanceof BEFlow) {
                            DbBEDiagram diagram = (DbBEDiagram) lineGo
                                    .getCompositeOfType(DbSMSDiagram.metaClass);
                            DbBENotation notation = diagram.findNotation();
                            if (notation.getName().equals(DbBENotation.UML_USE_CASE))
                                currentDescriptor = -1;
                            else
                                currentDescriptor = SMSDisplayDescriptor.NAME;
                        } else if (gc instanceof ORAssociation)
                            currentDescriptor = ((SMSDisplayDescriptor) lineGo
                                    .find(DbORStyle.fOr_nameDescriptor)).getValue();
                        else if (gc instanceof OOAssociation) {
                            currentDescriptor = SMSDisplayDescriptor.NAME;
                        }
                        lineGo.getDb().commitTrans();
                        if (SMSDisplayDescriptor.NAME == currentDescriptor) {

                            // //
                            // the zone is for a name, so we can create an
                            // editor ourselves !

                            Rectangle rect = srline.getContentRect();
                            centerLabel = new LineLabel(model, srline, RectangleShape.singleton);

                            SingletonZone newZone = new SingletonZone("Name");
                            DbTextFieldCellEditor tfEditor = new DbTextFieldCellEditor(
                                    LocaleMgr.misc.getString("association"),
                                    DbSemanticalObject.fName, true);
                            newZone.setValue(new ZoneCell(srline.getSemanticalObject(), "", null,
                                    tfEditor));
                            newZone.computePositionData(this.getGraphics(), 0, 0, 0);

                            centerLabel.addZone(newZone);
                            centerLabel.setRectangle(new Rectangle(e.getX(), e.getY(), 0, 0));
                        }
                    }
                    if (centerLabel != null) {
                        ZoneBox box = (ZoneBox) centerLabel;
                        Zone zone = box.getNameZone();
                        if (zone == null)
                            if (centerLabel instanceof BEFlowLabel)
                                zone = box.getZoneAt("Stereotype");

                        if (zone != null) {
                            CellID cellID = zone.cellAt(0, 0, 0);
                            if (zone != null && cellID != null)
                                ((ApplicationDiagram) model).setEditor(view, zone.getBox(), cellID);
                        }
                    }
                } catch (DbException ex) { /* do nothing */
                }
            }
        }

        if (m_pressedCellBoundaryInfo != null) {
            if ((gc != null) && (gc instanceof BEContext)) {
                BEContext context = (BEContext) gc;
                DbObject obj = context.getGraphicalObject();
                if (obj instanceof DbBEContextGo) {
                    // get context frame object
                    DbBEContextGo frame = (DbBEContextGo) obj;
                    BESelectToolCommand cmd = new BESelectToolCommand();
                    cmd.resizeFrameCell(frame, m_pressedCellBoundaryInfo, e);

                    m_pressedCellBoundaryInfo = null;
                } // end if
            } // end if
        } // end if
    } // end mousePressed()

    private Cursor oldCursor = null;
    private boolean inCellCreationSituation = false;

    public void mouseMoved(MouseEvent e) {
        TableZone.BoundaryInfoStruct boundaryInfo = isMouseOverCellBoundary(view, e);
        Cursor cursor = null;

        if (boundaryInfo != null) {
            if (boundaryInfo.boundary == GraphicComponent.LEFT_BORDER) {
                cursor = Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);
            } else if (boundaryInfo.boundary == GraphicComponent.TOP_BORDER) {
                cursor = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
            } // end if
        } // end if

        boolean doRestoreDefaultCursor = false;
        if (cursor != null) { // if pointer on a cell boundary
            if (oldCursor == null)
                oldCursor = view.getCursor();

            view.setCursor(cursor);
        } else { // if pointer within context, but not on a cell boundary
            doRestoreDefaultCursor = true;
        } // end if

        if (doRestoreDefaultCursor) {
            if (oldCursor != null)
                view.setCursor(oldCursor);

            oldCursor = null;
        } // end if
    } // end mouseMoved()

    private TableZone.BoundaryInfoStruct isMouseOverCellBoundary(DiagramView diagView, MouseEvent e) {
        TableZone.BoundaryInfoStruct boundaryInfo = null;
        int x = e.getX();
        int y = e.getY();

        GraphicComponent gc = model.graphicAt(view, x, y, 0xffffffff, true);
        if ((gc != null) && (gc instanceof BEContext)) {
            BEContext context = (BEContext) gc;
            Rectangle contextRect = context.getContentRect();
            Rectangle zoomedRect = (diagView == null) ? contextRect : diagView.unzoom(contextRect);
            Diagram diag = context.getDiagram();
            // y-8 because mouse arrow points actually at a higher point than
            // its center
            boundaryInfo = context.isOverCellBoundary(x, y - 8, zoomedRect, view);
        } // end if

        return boundaryInfo;
    } // end isMouseOverCellBoundary()
} // end SMSSelectTool
