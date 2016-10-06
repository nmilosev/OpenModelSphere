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
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.tool.ButtonSelectionPanelTool;
import org.modelsphere.jack.graphic.tool.Tool;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSFreeFormGo;
import org.modelsphere.sms.db.srtypes.SMSFreeForm;
import org.modelsphere.sms.international.LocaleMgr;

public class SMSFreeFormTool extends ButtonSelectionPanelTool {

    private static final String kShapeTool = LocaleMgr.screen.getString("ShapeTool"); // NOT LOCALIZABLE
    private static final String kFreeRectangleTool = LocaleMgr.screen.getString("FreeRectangle"); // NOT LOCALIZABLE
    private static final Image kImageFreeRectangleTool = GraphicUtil.loadImage(Tool.class,
            "resources/freerectangletool.gif"); // NOT LOCALIZABLE
    private static final String kFreeRoundRectTool = LocaleMgr.screen.getString("FreeRoundRect"); // NOT LOCALIZABLE
    private static final Image kImageFreeRoundRectTool = GraphicUtil.loadImage(Tool.class,
            "resources/freeroundrecttool.gif"); // NOT LOCALIZABLE
    private static final String kFreeOvalTool = LocaleMgr.screen.getString("FreeOval"); // NOT LOCALIZABLE
    private static final Image kImageFreeOvalTool = GraphicUtil.loadImage(Tool.class,
            "resources/freeovaltool.gif"); // NOT LOCALIZABLE

    private SMSFreeForm[] categories;

    public SMSFreeFormTool() {
        super(0, kShapeTool,
                new String[] { kFreeRectangleTool, kFreeRoundRectTool, kFreeOvalTool },
                kImageFreeRectangleTool, new Image[] { kImageFreeRectangleTool,
                        kImageFreeRoundRectTool, kImageFreeOvalTool }, 0);
        this.categories = new SMSFreeForm[] { SMSFreeForm.getInstance(SMSFreeForm.RECTANGLE),
                SMSFreeForm.getInstance(SMSFreeForm.ROUND_RECTANGLE),
                SMSFreeForm.getInstance(SMSFreeForm.OVAL) };
    }

    protected Cursor loadDefaultCursor() {
        return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
    }

    // Overriden for QA purposes
    /*
     * protected final JComponent createUIComponent(){ JComponent comp = super.createUIComponent();
     * if (comp == null) return comp; if (category == null) return comp; if (category.getValue() ==
     * SMSFreeForm.getInstance(SMSFreeForm.OVAL).getValue()) comp.setName(getClass().getName() +
     * "_OVAL"); // NOT LOCALIZABLE - component windows id QA else if (category.getValue() ==
     * SMSFreeForm.getInstance(SMSFreeForm.RECTANGLE).getValue()) comp.setName(getClass().getName()
     * + "_RECTANGLE"); // NOT LOCALIZABLE - component windows id QA else if (category.getValue() ==
     * SMSFreeForm.getInstance(SMSFreeForm.ROUND_RECTANGLE).getValue())
     * comp.setName(getClass().getName() + "_ROUND_RECTANGLE"); // NOT LOCALIZABLE - component
     * windows id QA return comp; }
     */

    public final void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (model.getDrawingArea().contains(x, y))
            createBox(x, y);
        view.toolCompleted();
    }

    protected final void createBox(int x, int y) {
        try {
            DbSMSDiagram diagGO = (DbSMSDiagram) ((ApplicationDiagram) model).getDiagramGO();
            diagGO.getDb().beginTrans(Db.WRITE_TRANS, LocaleMgr.action.getString("CreateFreeBox")); // NOT
            // LOCALIZABLE

            DbSMSFreeFormGo boxGo = new DbSMSFreeFormGo(diagGO,
                    categories[this.getAuxiliaryIndex()]);
            boxGo.setRectangle(new Rectangle(x - 30, y - 30, 60, 60));
            diagGO.getDb().commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(view, ex);
        }
    }
}
