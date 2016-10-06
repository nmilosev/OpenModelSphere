package org.modelsphere.sms.be.db.util;

import java.awt.Dimension;
import java.awt.Rectangle;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.sms.be.db.DbBEActorGo;
import org.modelsphere.sms.be.db.DbBEContextCell;
import org.modelsphere.sms.be.db.DbBEContextGo;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEStoreGo;
import org.modelsphere.sms.be.db.DbBEUseCaseGo;
import org.modelsphere.sms.be.graphic.tool.BESelectToolCommand;
import org.modelsphere.sms.db.srtypes.SMSHorizontalAlignment;
import org.modelsphere.sms.db.srtypes.SMSVerticalAlignment;
import org.modelsphere.sms.db.util.DbInitialization;

public class CellUtility {

    public static void insertColumn(DbBEContextGo frame, BEUtility.InsertAction type,
            int nbColumns, int nbRows) throws DbException {
        DbBEDiagram diagram = (DbBEDiagram) frame.getCompositeOfType(DbBEDiagram.metaClass);
        DbBENotation notation = diagram.getNotation();
        BEUtility util = BEUtility.getSingleInstance();

        if (nbRows == 0) {
            DbBEContextCell cell = util.createCell(frame, notation, 0, 0,
                    BEUtility.INSERT_CELL_CALLER, type);
            initValue(cell, notation);
        } else {
            for (int i = 0; i < nbRows; i++) {
                DbBEContextCell cell = util.createCell(frame, notation, nbColumns, i,
                        BEUtility.INSERT_CELL_CALLER, type);
                initValue(cell, notation);
            } // end for
            reorganizeExistingCells(frame, nbColumns, false);
        }
    } // end insertColumn()

    public static void insertRow(DbBEContextGo frame, int nbColumns, int nbRows) throws DbException {
        DbBEDiagram diagram = (DbBEDiagram) frame.getCompositeOfType(DbBEDiagram.metaClass);
        DbBENotation notation = diagram.getNotation();
        BEUtility util = BEUtility.getSingleInstance();

        if (nbColumns == 0) {
            DbBEContextCell cell = util.createCell(frame, notation, 0, 0,
                    BEUtility.INSERT_CELL_CALLER, BEUtility.InsertAction.NO_ACTION);
            initValue(cell, notation);
        } else {
            for (int i = 0; i < nbColumns; i++) {
                DbBEContextCell cell = util.createCell(frame, notation, i, nbRows,
                        BEUtility.INSERT_CELL_CALLER, BEUtility.InsertAction.NO_ACTION);
                initValue(cell, notation);
            } // end for
            reorganizeExistingCells(frame, nbRows, true);
        }
    } // end insertRow()

    public static void removeRow(DbBEContextGo frame, int nbRows) throws DbException {
        DbRelationN relN = frame.getComponents();
        DbEnumeration dbEnum = relN.elements(DbBEContextCell.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEContextCell cell = (DbBEContextCell) dbEnum.nextElement();
            int y = cell.getY().intValue();

            if (y >= nbRows - 1) {
                cell.remove();
            } // end if
        } // end while
        dbEnum.close();
    } // end removeRow()

    public static void removeColumn(DbBEContextGo frame, int nbColumns) throws DbException {
        DbRelationN relN = frame.getComponents();
        DbEnumeration dbEnum = relN.elements(DbBEContextCell.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEContextCell cell = (DbBEContextCell) dbEnum.nextElement();
            int x = cell.getX().intValue();

            if (x >= nbColumns - 1) {
                cell.remove();
            }
        } // end while
        dbEnum.close();
    } // end removeColumn()

    public static void initValue(DbBEContextCell cell, DbBENotation notation) throws DbException {
        Integer ha = (notation == null) ? null : notation.getDefHorizontalAlignment();
        Integer va = (notation == null) ? null : notation.getDefVerticalAlignment();
        Boolean bb = (notation == null) ? null : notation.getDefButtomBorder();
        Boolean rb = (notation == null) ? null : notation.getDefRightBorder();
        int horAligment = (ha == null) ? 0 : ha.intValue();
        int verAligment = (va == null) ? 0 : va.intValue();
        boolean buttomBorder = (bb == null) ? true : bb.booleanValue();
        boolean rightBorder = (rb == null) ? true : rb.booleanValue();
        cell.setXweight(new Double(1.0));
        cell.setYweight(new Double(1.0));

        SMSHorizontalAlignment horizontalAlign = DbInitialization
                .convertToHorizontalAlignment(horAligment);
        SMSVerticalAlignment verticalAlign = DbInitialization
                .convertToVerticalAlignment(verAligment);
        cell.setHorizontalJustification2(horizontalAlign);
        cell.setVerticalJustification2(verticalAlign);
        cell.setBottomBorder(buttomBorder ? Boolean.TRUE : Boolean.FALSE);
        cell.setRightBorder(rightBorder ? Boolean.TRUE : Boolean.FALSE);
    } // end initValue()

    private static void reorganizeExistingCells(DbBEContextGo frame, int nbExistingCells,
            boolean isHorizontal) throws DbException {
        Rectangle frameRect = frame.getRectangle();
        DbBEDiagram diagram = (DbBEDiagram) frame.getCompositeOfType(DbBEDiagram.metaClass);
        BEUtility util = BEUtility.getSingleInstance();

        // for each cell
        DbRelationN relN = frame.getComponents();
        DbEnumeration dbEnum = relN.elements(DbBEContextCell.metaClass);
        while (dbEnum.hasMoreElements()) {
            // reorganize cell content
            DbBEContextCell cell = (DbBEContextCell) dbEnum.nextElement();
            Rectangle cellRect = util.getCellRectangle(cell);

            // for each user case (process)..
            DbRelationN relN2 = cell.getUsecaseGos();
            DbEnumeration enum2 = relN2.elements(DbBEUseCaseGo.metaClass);
            while (enum2.hasMoreElements()) {
                DbBEUseCaseGo go = (DbBEUseCaseGo) enum2.nextElement();
                Rectangle rect = go.getRectangle();
                rect = computeNewPosition(frameRect, rect, nbExistingCells, isHorizontal);
                go.setRectangle(rect);
                BESelectToolCommand.moveInsideCell(diagram, go, cellRect);
            } // end while
            enum2.close();

            // for each store (role)..
            relN2 = cell.getStoreGos();
            enum2 = relN2.elements(DbBEStoreGo.metaClass);
            while (enum2.hasMoreElements()) {
                DbBEStoreGo go = (DbBEStoreGo) enum2.nextElement();
                Rectangle rect = go.getRectangle();
                rect = computeNewPosition(frameRect, rect, nbExistingCells, isHorizontal);
                go.setRectangle(rect);
                BESelectToolCommand.moveInsideCell(diagram, go, cellRect);
            } // end while
            enum2.close();

            // for each actor..
            relN2 = cell.getActorGos();
            enum2 = relN2.elements(DbBEActorGo.metaClass);
            while (enum2.hasMoreElements()) {
                DbBEActorGo go = (DbBEActorGo) enum2.nextElement();
                Rectangle rect = go.getRectangle();
                rect = computeNewPosition(frameRect, rect, nbExistingCells, isHorizontal);
                go.setRectangle(rect);
                BESelectToolCommand.moveInsideCell(diagram, go, cellRect);
            } // end while
            enum2.close();
        } // end while
        dbEnum.close();
    } // end reorganizeExistingCells()

    private static Rectangle computeNewPosition(Rectangle frameRect, Rectangle rect,
            int nbExistingCells, boolean isHorizontal) {
        if (isHorizontal) { // insert new row
            int delta = rect.y - frameRect.y;
            delta = (delta / (nbExistingCells + 1)) * nbExistingCells;
            rect.y = frameRect.y + delta;
        } else { // if vertical, insert new column
            int delta = rect.x - frameRect.x;
            delta = (delta / (nbExistingCells + 1)) * nbExistingCells;
            rect.x = frameRect.x + delta;
        } // end if

        return rect;
    } // end computeNewPosition()

    public static Dimension getDimension(DbBEContextGo frame) throws DbException {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = -1, maxY = -1;

        DbRelationN relN = frame.getComponents();
        DbEnumeration dbEnum = relN.elements(DbBEContextCell.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEContextCell cell = (DbBEContextCell) dbEnum.nextElement();
            int x = cell.getX().intValue();
            int y = cell.getY().intValue();
            if (x < minX)
                minX = x;
            if (y < minY)
                minY = y;
            if (x > maxX)
                maxX = x;
            if (y > maxY)
                maxY = y;
        } // end while
        dbEnum.close();

        Dimension dim = new Dimension(maxX + 1, maxY + 1);
        return dim;
    } // end getDimension()

    // return column, whose range is [0..n], n being the nb of columns in the
    // frame
    public static int findColumnToInsert(DbBEContextGo frame, Dimension frameDim, int x)
            throws DbException {
        Rectangle frameRect = frame.getRectangle();
        double cellWidth = frameRect.width / frameDim.width;
        double delta = x - frameRect.x;
        int column = (int) Math.round(delta / cellWidth);
        return column;
    }

}
