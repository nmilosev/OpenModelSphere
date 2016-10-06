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

import java.awt.Image;
import java.awt.Polygon;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.tool.LineTool;
import org.modelsphere.jack.graphic.tool.Tool;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSFreeLineGo;
import org.modelsphere.sms.international.LocaleMgr;

public class SMSFreeLineTool extends LineTool {

    public static final String kFreeLineTool = LocaleMgr.screen.getString("FreeLineTool"); // NOT LOCALIZABLE
    public static final Image kImageFreeRightAngleLineTool = GraphicUtil.loadImage(Tool.class,
            "resources/freerightanglelinetool.gif"); // NOT
    // LOCALIZABLE
    public static final Image kImageFreeLineTool = GraphicUtil.loadImage(Tool.class,
            "resources/freelinetool.gif"); // NOT LOCALIZABLE

    // public static final Image kImageFreeLineTool =
    // org.modelsphere.sms.international.LocaleMgr.screen.getImage("FreeLineTool");

    public SMSFreeLineTool(String text, String[] tooltips, Image image, Image[] secondaryimages) {
        super(0, text, tooltips, image, secondaryimages, true, 0);
    }

    protected final void createFreeLine(Polygon poly) {
        try {
            DbSMSDiagram diagGO = (DbSMSDiagram) ((ApplicationDiagram) model).getDiagramGO();
            diagGO.getDb().beginTrans(Db.WRITE_TRANS,
                    LocaleMgr.action.getString("FreeLineCreation")); // NOT
            // LOCALIZABLE

            DbSMSFreeLineGo lineGo = new DbSMSFreeLineGo(diagGO);
            lineGo.setPolyline(poly);
            lineGo.setRightAngle(new Boolean(rightAngle));

            diagGO.getDb().commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(view, ex);
        }
    }

}
