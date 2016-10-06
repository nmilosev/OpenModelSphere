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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;

import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.tool.LineTool;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.Stamp;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSLink;
import org.modelsphere.sms.db.DbSMSLinkGo;
import org.modelsphere.sms.db.DbSMSLinkModel;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.db.DbSMSStyle;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.or.graphic.ORStamp;

public final class SMSGraphicalLinkTool extends LineTool {
    private Cursor cursor;
    private static final String m_actionName = LocaleMgr.misc.getString("GraphicalLink");
    private static final Point HOTSPOT = new Point(6, 6); // center point is the
    // hotspot
    public static final String GRAPHICAL_LINKS = LocaleMgr.misc.getString("GraphicalLinks");

    public SMSGraphicalLinkTool(String text, String[] tooltips, Image image, Image[] secondaryimages) {
        super(0, text, tooltips, image, secondaryimages);
        cursor = loadDefaultCursor();
    }

    public Cursor getCursor() {
        return cursor;
    }

    protected final boolean isSourceAcceptable(GraphicNode source) {
        boolean acceptable = ((source instanceof ActionInformation) && !(((GraphicComponent) source) instanceof Stamp));
        return acceptable;
    }

    protected boolean isDestAcceptable(GraphicNode source, GraphicNode dest) {
        boolean acceptable = ((dest instanceof ActionInformation) && !(((GraphicComponent) dest) instanceof Stamp));
        return acceptable;
    }

    protected final void createLine(GraphicNode source, GraphicNode dest, Polygon poly) {
        try {
            createLink((ActionInformation) source, (ActionInformation) dest, poly);
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
    } // end createLine()

    //
    // private methods
    //

    private void createLink(ActionInformation source, ActionInformation dest, Polygon poly)
            throws DbException {
        DbSMSDiagram diagram = (DbSMSDiagram) ((ApplicationDiagram) model).getDiagramGO();
        diagram.getDb().beginTrans(Db.WRITE_TRANS, m_actionName);

        DbSMSGraphicalObject backEndGo = (DbSMSGraphicalObject) source.getGraphicalObject();
        DbSMSGraphicalObject frontEndGo = (DbSMSGraphicalObject) dest.getGraphicalObject();
        DbSMSSemanticalObject backEnd = (DbSMSSemanticalObject) backEndGo.getSO();
        DbSMSSemanticalObject frontEnd = (DbSMSSemanticalObject) frontEndGo.getSO();

        DbSMSLinkModel model = getLinkModel(diagram);
        DbSMSLink link = new DbSMSLink(model);
        if (backEnd != null && frontEnd != null) {
            link.addToSourceObjects(backEnd);
            link.addToTargetObjects(frontEnd);
            DbSMSLinkGo linkGo = new DbSMSLinkGo(diagram, backEndGo, frontEndGo, link);
            linkGo.setPolyline(poly);
            Point p = new Point(0, 0);
            linkGo.setCenterOffset(p);
            linkGo.setRightAngle(new Boolean(this.rightAngle));
            
            //set color of default style
            DbSMSStyle style = diagram.getStyle();
            linkGo.setStyle(style);
            Color color = style.getLineColorDbSMSGraphicalLink(); 
            linkGo.setLineColor(color);
        }
        diagram.getDb().commitTrans();
    } // end createFlow()

    private DbSMSLinkModel getLinkModel(DbSMSDiagram diagram) throws DbException {
        DbProject project = diagram.getProject();

        DbSMSLinkModel model = (DbSMSLinkModel) project.findComponentByName(
                DbSMSLinkModel.metaClass, GRAPHICAL_LINKS);
        if (model == null) {
            model = new DbSMSLinkModel(project);
            model.setDescription(GRAPHICAL_LINKS);

        }

        return model;
    } // end getLinkModel()

} // end SMSGraphicalLinkTool()
