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

package org.modelsphere.sms.graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.shape.GraphicShape;
import org.modelsphere.jack.graphic.shape.RectangleShape;
import org.modelsphere.jack.graphic.zone.ZoneCell;
import org.modelsphere.jack.srtool.graphic.DbTextFieldCellEditor;
import org.modelsphere.jack.srtool.graphic.SrLine;
import org.modelsphere.jack.srtool.graphic.SrLineLabel;
import org.modelsphere.sms.db.DbSMSAssociationGo;
import org.modelsphere.sms.db.DbSMSLineGo;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORNotation;
import org.modelsphere.sms.or.db.DbORStyle;
import org.modelsphere.sms.or.db.DbORTable;

public class NameAndDuplicateLabel extends SrLineLabel {
    private static final int NO_DISPLAY = 0;
    private static final int NORMAL_DISPLAY = 1;

    private boolean editable = true;
    private DbSMSLineGo go = null;

    public NameAndDuplicateLabel(Diagram diag, DbObject semObj, SrLine association,
            MetaField offsetMF, boolean editable, DbSMSLineGo go, String sName) throws DbException {
        super(diag, semObj, association, offsetMF, defineShape(go), getMinimalHeight(go), sName);
        this.editable = editable;
        this.go = go;
    }

    private static GraphicShape defineShape(DbSMSLineGo go) throws DbException {
        int display = displayAsRelationship(go, true);

        GraphicShape shape = RectangleShape.singleton;

        return shape;
    } // end defineShape()

    private static final int MINIMAL_HEIGHT = 16;

    private static int getMinimalHeight(DbSMSLineGo go) throws DbException {
        int height = 0;

        DbORDiagram orDiag = (DbORDiagram) go.getCompositeOfType(DbORDiagram.metaClass);
        if (orDiag != null) {
            DbORNotation notation = orDiag.getNotation();
        }

        return height;
    } // end getMinimalHeight

    protected DbSMSLineGo getGo() {
        return go;
    }

    // overrides LineLabel
    public final void paint(Graphics g, DiagramView diagView, int drawingMode, int renderingFlags) {

        Rectangle rect = getRectangle();
        if (diagView != null) {
            rect = diagView.zoom(rect);
        }

        GraphicShape shape;
        Color bgColor, fgColor;

        bgColor = this.getFillColor();
        fgColor = this.getLineColor();

        if (!getFillColor().equals(bgColor)) {
            setFillColor(bgColor);
        }

        if (!getLineColor().equals(fgColor)) {
            setLineColor(fgColor);
        }

        prepaint(g, diagView);
        super.paint(g, diagView, drawingMode, renderingFlags);
        postpaint(g, diagView);
    } // end paint()

    protected void prepaint(Graphics g, DiagramView diagView) {
    }

    protected void postpaint(Graphics g, DiagramView diagView) {
    }

    protected void setRectangle(Point pos, Dimension dim) {
        super.setRectangle(pos, dim);
        super.setRectangle(pos, dim);

    } // end setRectangle()

    // overriding to install an editor
    public void setValue(String value) {
        DbTextFieldCellEditor tfEditor = null;
        if (editable)
            tfEditor = new DbTextFieldCellEditor(LocaleMgr.misc.getString("association"),
                    DbSemanticalObject.fName, false);
        zone.setValue(new ZoneCell(semObj, value, null, tfEditor));
    }

    //
    // private methods
    // 
    private Color getColor(DbSMSLineGo go, MetaField mf) {
        Color bgColor;

        try {
            go.getDb().beginReadTrans();
            bgColor = (Color) go.find(mf);
            go.getDb().commitTrans();
        } catch (DbException ex) {
            bgColor = Color.WHITE;
        } // end try

        return bgColor;
    } // end getBgColor()

    private static int displayAsRelationship(DbSMSLineGo go, boolean withinTransaction) {
        int display = NORMAL_DISPLAY;
        boolean displayAsRelationship = false;

        try {
            if (!withinTransaction) {
                go.getDb().beginReadTrans();
            }

            Boolean assocAsRelationship = (Boolean) go
                    .find(DbORStyle.fOr_associationAsRelationships);
            displayAsRelationship = (assocAsRelationship == null) ? false : assocAsRelationship
                    .booleanValue();

            if (displayAsRelationship) {
                if (go instanceof DbSMSAssociationGo) {
                    DbSMSAssociationGo assocGo = (DbSMSAssociationGo) go;
                    DbORAssociation assoc = (DbORAssociation) assocGo.getAssociation();
                    DbORAssociationEnd backEnd = assoc.getBackEnd();
                    DbORAbsTable backTable = backEnd.getClassifier();
                    DbORAssociationEnd frontEnd = assoc.getFrontEnd();
                    DbORAbsTable frontTable = frontEnd.getClassifier();
                    if ((backTable instanceof DbORTable) && (frontTable instanceof DbORTable)) {
                        if (((DbORTable) backTable).isIsAssociation())
                            display = NO_DISPLAY;

                        if (((DbORTable) frontTable).isIsAssociation())
                            display = NO_DISPLAY;
                    } // end if
                } // end if
            } // end if

            if (!withinTransaction) {
                go.getDb().commitTrans();
            }
        } catch (DbException ex) {
            display = NORMAL_DISPLAY;
        } // end catch()

        if (display != NO_DISPLAY) {
            display = NORMAL_DISPLAY;
        }

        return display;
    } // end displayAsRelationship()
}
