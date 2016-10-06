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

package org.modelsphere.jack.srtool.graphic;

import java.awt.Rectangle;
import java.awt.Font;
import java.awt.Color;

import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.graphic.*;
import org.modelsphere.jack.graphic.shape.RectangleShape;
import org.modelsphere.jack.graphic.zone.*;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public class FreeText extends ExtZoneBox implements DbRefreshListener, ActionInformation {
    private static final Color TRANSPARENT = new Color(255, 255, 255, 0);

    private SingletonZone zone;
    private DbObject textGo;

    public FreeText(Diagram diag, DbObject textGo) throws DbException {
        super(diag, RectangleShape.singleton);
        this.textGo = textGo;

        setAutoFit(((Boolean) textGo.get(DbGraphic.fGraphicalObjectAutoFit)).booleanValue());

        setRectangle((Rectangle) textGo.get(DbGraphic.fGraphicalObjectRectangle));
        setLineColor(TRANSPARENT);
        zone = new SingletonZone("");
        zone.setAlignment(GraphicUtil.LEFT_ALIGNMENT);
        addZone(zone);
        populateContents();
        ((DbGraphicalObjectI) textGo).setGraphicPeer(this);
        textGo.addDbRefreshListener(this);
    }

    public void mouseReleased(CellID cellID) {
        try {
            ApplicationDiagram diag = ApplicationContext.getFocusManager().getActiveDiagram();
            diag.setEditor(diag.getMainView(), zone.getBox(), zone.cellAt(0, 0, 0));
        } catch (Exception e) {/* do nothing */
        }
    }

    // BEWARE: overriding methods must call super.delete as last action
    public final void delete(boolean all) {
        ((DbGraphicalObjectI) textGo).setGraphicPeer(null);
        textGo.removeDbRefreshListener(this);
        super.delete(all);
    }

    public int getDefaultFitMode() {
        return GraphicComponent.HEIGHT_FIT;
    }

    private void populateContents() throws DbException {
        CellRenderingOption cellOption = new CellRenderingOption(
                new StringCellRenderer(false, true), GraphicUtil.LEFT_ALIGNMENT);
        ZoneCell cell = new ZoneCell(textGo, (String) textGo.get(DbGraphic.fUserTextGoText),
                cellOption, new DbTextAreaCellEditor(DbGraphic.fUserTextGoText));
        zone.setValue(cell);

        zone.setTextColor((Color) textGo.get(DbGraphic.fUserTextGoTextColor));
        setFillColor((Color) textGo.get(DbGraphic.fUserTextGoFillColor));
        zone.setFont((Font) textGo.get(DbGraphic.fUserTextGoFont));
        cellOption.setFont((Font) textGo.get(DbGraphic.fUserTextGoFont));
        diagram.setComputePos(this);
    }

    public final Font getFont() {
        return zone.getFont();
    }

    public final void startEditing(DiagramView view) {
        diagram.setEditor(view, this, new SingletonCellID(zone));
    }

    public final String getToolTipText() {
        return (String) zone.getValue().getCellData();

    }

    public final void refreshAfterDbUpdate(DbUpdateEvent e) throws DbException {
        if (DbGraphic.fUserTextGoText == e.metaField || DbGraphic.fUserTextGoFont == e.metaField
                || DbGraphic.fUserTextGoFillColor == e.metaField
                || DbGraphic.fUserTextGoTextColor == e.metaField) {
            populateContents();
        }
    }

    public final Db getDb() {
        return textGo.getDb();
    }

    public final DbObject getSemanticalObject() {
        return null;
    }

    public final DbObject getGraphicalObject() {
        return textGo;
    }

    private static class FreeTextCellEditor extends TextAreaCellEditor {
        FreeTextCellEditor() {
        }

        public final void stopEditing(int endCode) {
            if (endCode == CellEditor.CANCEL)
                return;
            DbObject textGo = (DbObject) value.getObject();
            try {
                textGo.getDb().beginTrans(Db.WRITE_TRANS, LocaleMgr.action.getString("changeText"));
                textGo.set(DbGraphic.fUserTextGoText, textArea.getText());
                textGo.getDb().commitTrans();
            } catch (Exception ex) {
                org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                        ((ApplicationDiagram) diagram).getMainView(), ex);
            }
        }
    }
}
