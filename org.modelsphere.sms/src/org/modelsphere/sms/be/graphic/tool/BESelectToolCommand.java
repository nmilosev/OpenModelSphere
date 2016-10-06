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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.zone.TableZone;
import org.modelsphere.sms.be.db.DbBEContextCell;
import org.modelsphere.sms.be.db.DbBEContextGo;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEStoreGo;
import org.modelsphere.sms.be.db.DbBEUseCaseGo;
import org.modelsphere.sms.be.db.util.BEUtility;
import org.modelsphere.sms.be.graphic.BEContext;
import org.modelsphere.sms.be.graphic.BEStoreBox;
import org.modelsphere.sms.be.graphic.BEUseCaseBox;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.db.DbSMSClassifierGo;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class BESelectToolCommand {

    private static final String RESIZE_CELLS = LocaleMgr.action.getString("ResizeCells");

    // Called by SMSSelectTool.translateComponentsAfter()
    public void execute(DbBEDiagram diagram, GraphicComponent[] gcs, int dx, int dy)
            throws DbException {

        int nb = gcs.length;
        for (int i = 0; i < nb; i++) {
            GraphicComponent gc = gcs[i];
            if (gc instanceof BEContext) {
                BEContext context = (BEContext) gc;
                translateContextContent(context, dx, dy);
            } else if (gc instanceof BEUseCaseBox) {
                BEUseCaseBox box = (BEUseCaseBox) gc;
                DbSMSClassifierGo go = (DbSMSClassifierGo) box.getGraphicalObject();
                constraintCellBoundary(diagram, go, dx, dy);
            } else if (gc instanceof BEStoreBox) {
                BEStoreBox box = (BEStoreBox) gc;
                DbSMSClassifierGo go = (DbSMSClassifierGo) box.getGraphicalObject();
                constraintCellBoundary(diagram, go, dx, dy);
            } // end if
        } // end for
    } // end execute()

    // Called by SMSSelectTool.mouseReleased()
    public void resizeFrameCell(DbBEContextGo frame, TableZone.BoundaryInfoStruct boundaryInfo,
            MouseEvent e) {
        // set new dimensions to cells
        TableZone.BoundaryInfoStruct releasedCellBoundaryInfo = boundaryInfo;
        Point releasedPoint = new Point(e.getX(), e.getY());
        resizeFrameCells(frame, boundaryInfo, releasedPoint);
    } // end resizeFrameCell()

    //
    // service methods
    //

    // Called by BEActorTool.createBox() to ensure box is centered at the
    // creation time
    static void stayCenterCell(DbBEDiagram diag, DbSMSClassifierGo go, Rectangle cellRect)
            throws DbException {
        if (cellRect == null)
            return;

        DbBENotation notation = diag.getNotation();
        boolean constraintCenter = (notation == null) ? false : notation.getConstraintCenter()
                .booleanValue();
        boolean rightBorder = (notation == null) ? false : notation.getDefRightBorder()
                .booleanValue();
        boolean bottomBorder = (notation == null) ? false : notation.getDefButtomBorder()
                .booleanValue();
        Rectangle boxRect = go.getRectangle();
        int xPos = boxRect.x, yPos = boxRect.y;

        if (constraintCenter && !bottomBorder)
            xPos = cellRect.x + (cellRect.width / 2) - (boxRect.width / 2);

        if (constraintCenter && !rightBorder)
            yPos = cellRect.y + (cellRect.height / 2) - (boxRect.height / 2);

        boxRect = new Rectangle(xPos, yPos, boxRect.width, boxRect.height);
        go.setRectangle(boxRect);
    } // end stayCenterCell()

    // Called by BEActorTool.createBox() to ensure box is centered at the
    // creation time
    static DbBEContextCell isInsideACell(DbBEDiagram diagram, DbSMSClassifierGo go)
            throws DbException {
        // for each frame in the diagram
        Rectangle boxRect = go.getRectangle();
        if (boxRect == null) {
            return null;
        }

        BEUtility util = BEUtility.getSingleInstance();
        DbRelationN relN = diagram.getComponents();
        DbEnumeration dbEnum = relN.elements(DbBEContextGo.metaClass);
        while (dbEnum.hasMoreElements()) {

            // get tablezone rectangle
            DbBEContextGo frame = (DbBEContextGo) dbEnum.nextElement();
            Rectangle frameRect = frame.getRectangle();
            BEContext context = (BEContext) frame.getGraphicPeer();
            TableZone zone = context.getCenterZone();
            Rectangle zoneRect = zone.getRectangle(frameRect);

            // if boxRect partly or totally inside frame rectangle
            if ((zoneRect != null) && zoneRect.intersects(boxRect)) {

                // for each cell of the frame
                DbRelationN relN2 = frame.getComponents();
                DbEnumeration enum2 = relN2.elements(DbBEContextCell.metaClass);
                while (enum2.hasMoreElements()) {
                    DbBEContextCell cell = (DbBEContextCell) enum2.nextElement();
                    Rectangle cellRect = util.getCellRectangle(cell);

                    if (cellRect.intersects(boxRect)) {
                        setCell(go, cell);
                        break;
                    } // end if
                } // end while
                enum2.close();
            } // end if

            if (getCell(go) != null)
                break;
        } // end while
        dbEnum.close();

        DbBEContextCell cell = getCell(go);
        if (cell != null) {
            Rectangle cellRect = util.getCellRectangle(cell);
            moveInsideCell(diagram, go, cellRect);
        }

        return cell;
    } // end isInsideACell()

    //
    // private methods
    //  
    private void translateContextContent(BEContext context, int dx, int dy) throws DbException {
        DbBEContextGo go = (DbBEContextGo) context.getGraphicalObject();
        DbRelationN relN = go.getComponents();
        DbEnumeration dbEnum = relN.elements(DbBEContextCell.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEContextCell cell = (DbBEContextCell) dbEnum.nextElement();
            DbRelationN relN2 = cell.getUsecaseGos();
            DbEnumeration enum2 = relN2.elements(DbBEUseCaseGo.metaClass);
            while (enum2.hasMoreElements()) {
                DbBEUseCaseGo processGo = (DbBEUseCaseGo) enum2.nextElement();
                Rectangle newPos = processGo.getRectangle();
                newPos.translate(dx, dy);
                processGo.setRectangle(newPos);
            } // end while
            enum2.close();

            relN2 = cell.getStoreGos();
            enum2 = relN2.elements(DbBEStoreGo.metaClass);
            while (enum2.hasMoreElements()) {
                DbBEStoreGo storeGo = (DbBEStoreGo) enum2.nextElement();
                Rectangle newPos = storeGo.getRectangle();
                newPos.translate(dx, dy);
                storeGo.setRectangle(newPos);
            } // end while
            enum2.close();
        } // end while
        dbEnum.close();
    } // end translateContextContent()

    private void constraintCellBoundary(DbBEDiagram diagram, DbSMSClassifierGo go, int dx, int dy)
            throws DbException {
        // get process rectangle
        Rectangle boxRect = go.getRectangle();

        // get cell rectangle
        BEUtility util = BEUtility.getSingleInstance();
        DbBEContextCell cell = getCell(go);
        if (cell == null) { // if not attached to any cell
            // if boxRect is partly inside a frame rect
            cell = isInsideACell(diagram, go);
        } // end if

        if (cell == null) {
            return;
        }

        Rectangle cellRect = util.getCellRectangle(cell);

        // if boxRect is not completly inside cellRect
        if (!cellRect.contains(boxRect)) {

            // but its center is still inside cellRect, then re-place it within
            // the cell
            Point center = new Point((int) boxRect.getCenterX(), (int) boxRect.getCenterY());
            if (cellRect.contains(center)) {
                moveInsideCell(diagram, go, cellRect);
            } else {
                moveOutsideCell(diagram, go, cellRect, cell);
            }
        } else {
            // no effect if notation.constraintCenter == false
            stayCenterCell(diagram, go, cellRect);
        } // end if
    } // end checkCellBoundary()

    private void moveOutsideCell(DbBEDiagram diagram, DbSMSClassifierGo go, Rectangle cellRect,
            DbBEContextCell cell) throws DbException {
        Rectangle boxRect = go.getRectangle();
        int xPos = boxRect.x;
        int yPos = boxRect.y;
        int border = -1;

        // if exceeds cell bondaries, determine which border is concerned
        if (boxRect.x < cellRect.x) {
            border = GraphicComponent.LEFT_BORDER;
            xPos = cellRect.x - boxRect.width;
        } else if (boxRect.y < cellRect.y) {
            border = GraphicComponent.TOP_BORDER;
            yPos = cellRect.y - boxRect.height;
        } else if ((boxRect.x + boxRect.width) > (cellRect.x + cellRect.width)) {
            border = GraphicComponent.RIGHT_BORDER;
            xPos = cellRect.x + cellRect.width;
        } else if ((boxRect.y + boxRect.height) > (cellRect.y + cellRect.height)) {
            border = GraphicComponent.BOTTOM_BORDER;
            yPos = cellRect.y + cellRect.height;
        } // end if

        // connect to the new cell
        DbBEContextCell newCell = getAdjacentCell(cell, border);
        setCell(go, newCell);

        // move inside the new cell
        if (newCell != null) {
            BEUtility util = BEUtility.getSingleInstance();
            cellRect = util.getCellRectangle(newCell);
            moveInsideCell(diagram, go, cellRect);
        } // end if
    } // end moveOutsideCell()

    private DbBEContextCell getAdjacentCell(DbBEContextCell cell, int border) throws DbException {
        int x = cell.getX().intValue();
        int y = cell.getY().intValue();

        switch (border) {
        case GraphicComponent.TOP_BORDER:
            y--;
            break;
        case GraphicComponent.LEFT_BORDER:
            x--;
            break;
        case GraphicComponent.RIGHT_BORDER:
            x++;
            break;
        case GraphicComponent.BOTTOM_BORDER:
            y++;
            break;
        } // end switch

        DbBEContextGo frameGo = (DbBEContextGo) cell.getCompositeOfType(DbBEContextGo.metaClass);
        DbBEContextCell newCell = getCell(frameGo, x, y);
        return newCell;
    } // end getAdjacentCell()

    private DbBEContextCell getCell(DbBEContextGo frameGo, int xPos, int yPos) throws DbException {
        DbBEContextCell cell = null;
        if ((xPos < 0) || (yPos < 0))
            return null;

        DbRelationN relN = frameGo.getComponents();
        DbEnumeration dbEnum = relN.elements(DbBEContextCell.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEContextCell c = (DbBEContextCell) dbEnum.nextElement();
            int x = c.getX().intValue();
            int y = c.getY().intValue();
            if ((xPos == x) && (yPos == y)) {
                cell = c;
                break;
            }
        } // end while
        dbEnum.close();

        return cell;
    } // end getCell()

    public static void moveInsideCell(DbBEDiagram diag, DbSMSClassifierGo go, Rectangle cellRect)
            throws DbException {
        DbBENotation notation = diag.getNotation();
        boolean constraintCenter = (notation == null) ? false : notation.getConstraintCenter()
                .booleanValue();
        boolean rightBorder = (notation == null) ? false : notation.getDefRightBorder()
                .booleanValue();
        boolean bottomBorder = (notation == null) ? false : notation.getDefButtomBorder()
                .booleanValue();

        if (constraintCenter) {
            stayCenterCell(diag, go, cellRect);
            return;
        }

        Rectangle boxRect = go.getRectangle();
        int xDelta = 0, yDelta = 0;

        if (boxRect == null || cellRect == null)
            return;

        if (boxRect.x < cellRect.x)
            xDelta = cellRect.x - boxRect.x;

        if (boxRect.y < cellRect.y)
            yDelta = cellRect.y - boxRect.y;

        if ((boxRect.x + boxRect.width) > (cellRect.x + cellRect.width))
            xDelta = (cellRect.x + cellRect.width) - (boxRect.x + boxRect.width);

        if ((boxRect.y + boxRect.height) > (cellRect.y + cellRect.height))
            yDelta = (cellRect.y + cellRect.height) - (boxRect.y + boxRect.height);

        Dimension translation = new Dimension(xDelta, yDelta);
        boxRect.translate(translation.width, translation.height);
        go.setRectangle(boxRect);
    } // end moveInsideCell()

    private static void setCell(DbSMSClassifierGo go, DbBEContextCell cell) throws DbException {
        if (go instanceof DbBEUseCaseGo) {
            ((DbBEUseCaseGo) go).setCell(cell);
        } else if (go instanceof DbBEStoreGo) {
            ((DbBEStoreGo) go).setCell(cell);
        } // end if
    } // end setCell

    private static DbBEContextCell getCell(DbSMSClassifierGo go) throws DbException {
        DbBEContextCell cell = null;

        if (go instanceof DbBEUseCaseGo) {
            cell = ((DbBEUseCaseGo) go).getCell();
        } else if (go instanceof DbBEStoreGo) {
            cell = ((DbBEStoreGo) go).getCell();
        } // end if

        return cell;
    } // end getCell()

    private void resizeFrameCells(DbBEContextGo frame, TableZone.BoundaryInfoStruct boundaryInfo,
            Point releasedPoint) {
        try {
            frame.getDb().beginWriteTrans(RESIZE_CELLS);

            // compute which proportion the boundary translation represents
            // compared to the whole frame; range : 0.0 <= ratio <= totalWeight
            double ratio = computeRatioOfFrame(frame, boundaryInfo, releasedPoint);

            if (ratio != 0.0) {
                // map each graphical process or store to its current cell
                HashMap map = new HashMap();
                DbRelationN relN = frame.getComponents();
                DbEnumeration dbEnum = relN.elements(DbBEContextCell.metaClass);
                while (dbEnum.hasMoreElements()) {
                    DbBEContextCell cell = (DbBEContextCell) dbEnum.nextElement();
                    DbRelationN relN2 = cell.getUsecaseGos();
                    DbEnumeration enum2 = relN2.elements(DbBEUseCaseGo.metaClass);
                    while (enum2.hasMoreElements()) {
                        DbBEUseCaseGo go = (DbBEUseCaseGo) enum2.nextElement();
                        map.put(go, cell);
                    } // end while
                    enum2.close();

                    relN2 = cell.getStoreGos();
                    enum2 = relN2.elements(DbBEStoreGo.metaClass);
                    while (enum2.hasMoreElements()) {
                        DbBEStoreGo go = (DbBEStoreGo) enum2.nextElement();
                        map.put(go, cell);
                    } // end while
                    enum2.close();
                } // end while
                dbEnum.close();

                // resize each cell
                dbEnum = relN.elements(DbBEContextCell.metaClass);
                while (dbEnum.hasMoreElements()) {
                    // resize cell if concerned by the boundary translation
                    DbBEContextCell cell = (DbBEContextCell) dbEnum.nextElement();
                    resizeCell(frame, cell, ratio, boundaryInfo, releasedPoint);
                } // end while
                dbEnum.close();

                // for each mapped process or store, reput it in its original
                // cell
                Set keySet = map.keySet();
                Iterator iter = keySet.iterator();
                BEUtility util = BEUtility.getSingleInstance();
                DbBEDiagram diagram = (DbBEDiagram) frame.getCompositeOfType(DbBEDiagram.metaClass);
                while (iter.hasNext()) {
                    DbSMSClassifierGo go = (DbSMSClassifierGo) iter.next();
                    DbBEContextCell cell = (DbBEContextCell) map.get(go);
                    Rectangle newCellRect = util.getCellRectangle(cell);
                    moveInsideCell(diagram, go, newCellRect);
                } // end while
            } // end if

            frame.getDb().commitTrans();
        } catch (DbException ex) {
            // abort the operation
        } // end try

    } // end resizeFrameCells()

    // ratio : compute which proportion the boundary translation represents
    // compared to the whole frame; range : 0.0 <= ratio <= totalWeight
    private void resizeCell(DbBEContextGo frame, DbBEContextCell cell, double ratio,
            TableZone.BoundaryInfoStruct boundaryInfo, Point releasedPoint) throws DbException {
        BEUtility util = BEUtility.getSingleInstance();
        int cellX = cell.getX().intValue();
        int cellY = cell.getY().intValue();
        int xPos = boundaryInfo.xPos;
        int yPos = boundaryInfo.yPos;

        int boundaryConcerned = boundaryInfo.boundary;
        if (boundaryConcerned == GraphicComponent.TOP_BORDER) {
            if (cellY == yPos - 1) {
                cellY = yPos;
                ratio *= -1; // sign change
            } // end if

            if (cellY == yPos) {
                double oldFrameSize = getFrameSize(frame, boundaryInfo.boundary);
                double oldCellYWeight = cell.getYweight().doubleValue();
                Rectangle oldCellRect = util.getCellRectangle(cell);
                double newCellYWeight = oldCellYWeight + ratio;
                cell.setYweight(new Double(newCellYWeight));
                double newHeight = (oldCellRect.height / oldCellYWeight) * newCellYWeight;
                Rectangle newCellRect = new Rectangle(oldCellRect.x, oldCellRect.y,
                        oldCellRect.width, (int) newHeight);

                int p1 = boundaryInfo.getMousePosition().y;
                int p2 = releasedPoint.y;

                moveCellContent(frame, cell, p1, p2, oldCellRect, newCellRect, boundaryConcerned);
            } // end if
        } // end if

        if (boundaryConcerned == GraphicComponent.LEFT_BORDER) {
            if (cellX == xPos - 1) {
                cellX = xPos;
                ratio *= -1; // sign change
            } // end if

            if (cellX == xPos) {
                double oldFrameSize = getFrameSize(frame, boundaryInfo.boundary);
                double oldCellXWeight = cell.getXweight().doubleValue();
                Rectangle oldCellRect = util.getCellRectangle(cell);
                double newCellXWeight = oldCellXWeight + ratio;
                cell.setXweight(new Double(newCellXWeight));
                double newWidth = (oldCellRect.width / oldCellXWeight) * newCellXWeight;
                Rectangle newCellRect = new Rectangle(oldCellRect.x, oldCellRect.y, (int) newWidth,
                        oldCellRect.height);

                int p1 = boundaryInfo.getMousePosition().x;
                int p2 = releasedPoint.x;
                moveCellContent(frame, cell, p1, p2, oldCellRect, newCellRect, boundaryConcerned);
            }
        } // end if
    } // end resizeCell()

    private void moveCellContent(DbBEContextGo frame, DbBEContextCell cell, int p1, int p2,
            Rectangle oldCellRect, Rectangle newCellRect, int boundaryConcerned) throws DbException {

        DbRelationN relN = cell.getUsecaseGos();
        DbEnumeration dbEnum = relN.elements(DbSMSClassifierGo.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSClassifierGo go = (DbSMSClassifierGo) dbEnum.nextElement();
            moveClassifierGo(go, p1, oldCellRect, newCellRect, boundaryConcerned);
        } // end while
        dbEnum.close();

        relN = cell.getStoreGos();
        dbEnum = relN.elements(DbSMSClassifierGo.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSClassifierGo go = (DbSMSClassifierGo) dbEnum.nextElement();
            moveClassifierGo(go, p1, oldCellRect, newCellRect, boundaryConcerned);
        } // end while
        dbEnum.close();
    } // end moveCellContent()

    private void moveClassifierGo(DbSMSClassifierGo go, int p1, Rectangle oldCellRect,
            Rectangle newCellRect, int boundaryConcerned) throws DbException {
        Rectangle rect = go.getRectangle();

        int center;
        double relativePos;
        if (boundaryConcerned == GraphicComponent.LEFT_BORDER) {
            center = rect.x + (rect.width / 2);
            relativePos = (center - p1) / (double) oldCellRect.width;
        } else { // TOP_BORDER
            center = rect.y + (rect.height / 2);
            relativePos = (center - p1) / (double) oldCellRect.height;
        } // end if

        if (relativePos < 0.0)
            relativePos += 1.0;

        if (boundaryConcerned == GraphicComponent.LEFT_BORDER) {
            int newCenterX = newCellRect.x + (int) (relativePos * newCellRect.width);
            rect.x = newCenterX - (rect.width / 2);
            go.setRectangle(rect);
        } else { // TOP_BORDER
            int newCenterY = newCellRect.y + (int) (relativePos * newCellRect.height);
            rect.y = newCenterY - (rect.height / 2);
            go.setRectangle(rect);
        } // end if
    } // end moveCellContent()

    // compute which proportion the boundary translation represents compared to
    // the whole frame
    private double computeRatioOfFrame(DbBEContextGo frame,
            TableZone.BoundaryInfoStruct boundaryInfo, Point releasedPoint) throws DbException {
        Rectangle rect = frame.getRectangle();
        double ratio;
        if ((boundaryInfo.boundary == GraphicComponent.BOTTOM_BORDER)
                || (boundaryInfo.boundary == GraphicComponent.TOP_BORDER)) {
            double delta = boundaryInfo.getMousePosition().y - releasedPoint.getY();
            ratio = (delta / rect.height) * getFrameSize(frame, boundaryInfo.boundary);
        } else { // LEFT_BORDER or RIGHT_BORDER
            double delta = boundaryInfo.getMousePosition().x - releasedPoint.getX();
            ratio = (delta / rect.width) * getFrameSize(frame, boundaryInfo.boundary);
        } // end if

        return ratio;
    } // end computeRatioOfFrame()

    // get the frame size (width or heigth depending of boundary)
    private double getFrameSize(DbBEContextGo frame, int boundary) throws DbException {
        BEUtility util = BEUtility.getSingleInstance();
        util.computeCellDimensions(frame);
        double frameSize = 0;
        switch (boundary) {
        case GraphicComponent.BOTTOM_BORDER:
        case GraphicComponent.TOP_BORDER:
            double[] heights = util.getRowHeights();
            frameSize = sum(heights); // sum?
            break;
        case GraphicComponent.LEFT_BORDER:
        case GraphicComponent.RIGHT_BORDER:
            double[] widths = util.getColWidths();
            frameSize = sum(widths);
            break;
        } // end switch

        return frameSize;
    } // getFrameSize()

    private double sum(double[] sizes) {
        double total = 0;
        for (int i = 0; i < sizes.length; i++) {
            total += sizes[i];
        }

        return total;
    } // end sum()

} // end BESelectToolCommand
