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

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.graphic.tool.BoxTool;
import org.modelsphere.jack.graphic.zone.TableCell;
import org.modelsphere.jack.graphic.zone.TableZone;
import org.modelsphere.sms.be.db.DbBEContextCell;
import org.modelsphere.sms.be.db.DbBEContextGo;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEStyle;
import org.modelsphere.sms.be.db.util.BEUtility;
import org.modelsphere.sms.db.srtypes.SMSHorizontalAlignment;
import org.modelsphere.sms.db.srtypes.SMSVerticalAlignment;
import org.modelsphere.sms.db.util.DbInitialization;

/**
 * Superclass of BEUseCaseTool and BEStoreTool to share common behaviors
 */

public abstract class BEContextTool extends BoxTool {

    public BEContextTool(String text, Image image) {
        super(0, text, image);
    }

    // Compute m_rowHeights & m_rowHeights
    private double[] m_rowHeights = null;
    private double[] m_colWidths = null;

    protected double[] getRowHeights() {
        return m_rowHeights;
    }

    protected double[] getColWidths() {
        return m_colWidths;
    }

    protected void computeCellDimensions(DbBEContextGo go) throws DbException {
        // display title in box in current notation?
        DbBEDiagram diagram = (DbBEDiagram) go.getCompositeOfType(DbBEDiagram.metaClass);
        DbBENotation notation = diagram.getNotation();
        boolean titleBoxed = false, centerDisplay = false;

        if (notation != null) {
            titleBoxed = isTrue(notation.getCellTitleBoxed());
            centerDisplay = isTrue(notation.getCenterDisplay());
        } // end if

        DbBEStyle style = (DbBEStyle) go.getStyle();
        if (style == null)
            style = (DbBEStyle) diagram.getStyle();
        Color bgColor = (style == null) ? Color.white : style.getBackgroundColorDbBEContextGo();
        Color fgColor = (style == null) ? Color.black : style.getLineColorDbBEContextCell();
        Color bxColor = (style == null) ? Color.black : style.getLineColorDbBEContextGo();
        Color txColor = (style == null) ? Color.black : style.getTextColorDbBEContextGo();

        // for each cell, add it in table zone
        TableZone zone = new TableZone((String) null);
        DbRelationN relN = go.getComponents();
        DbEnumeration dbEnum = relN.elements(DbBEContextCell.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEContextCell cell = (DbBEContextCell) dbEnum.nextElement();
            int x = cell.getX().intValue();
            int y = cell.getY().intValue();
            double wx = cell.getXweight().doubleValue();
            double wy = cell.getYweight().doubleValue();
            boolean rightBorder = cell.isRightBorder();
            boolean bottomBorder = cell.isBottomBorder();
            String text = cell.getDescription();
            Font font = cell.getFont();
            SMSVerticalAlignment verticalAlign = cell.getVerticalJustification2();
            SMSHorizontalAlignment horizontalAlign = cell.getHorizontalJustification2();
            int vJustification = DbInitialization.convertToVerticalAlignment(verticalAlign);
            int hJustification = DbInitialization.convertToHorizontalAlignment(horizontalAlign);

            TableCell tableCell = new TableCell(x, y, wx, wy, rightBorder, bottomBorder, text,
                    font, hJustification, vJustification, titleBoxed, bgColor, fgColor, bxColor,
                    txColor, centerDisplay);
            zone.add(tableCell);
        } // end while
        dbEnum.close();

        zone.computeSizeOfRowsAndColumns();
        m_rowHeights = zone.getRowHeights();
        m_colWidths = zone.getColWidths();
    } // end getNbOfCells()

    private boolean isTrue(Boolean b) {
        return (b == null) ? false : b.booleanValue();
    } // end isTrue

} // end BEContextTool

