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
package org.modelsphere.sms.or.graphic.tool;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.tool.BoxTool;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.SMSToolkit;
import org.modelsphere.sms.or.ORModule;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTableGo;
import org.modelsphere.sms.or.international.LocaleMgr;

public class ORTableTool extends BoxTool {

    public static final String kTableCreationTool = LocaleMgr.screen.getString("TableCreationTool");
    public static final Image kImageTableCreationTool = GraphicUtil.loadImage(ORModule.class,
            "db/resources/dbortable.gif"); // Temporary change
    // with new image //
    // NOT LOCALIZABLE -
    // tool image
    // public static final Image kImageTableCreationTool =
    // GraphicUtil.loadImage(ORModule.class,
    // "international/resources/tabletool.gif");

    public static final String kEntityCreationTool = LocaleMgr.screen
            .getString("EntityCreationTool");
    public static final Image kImageEntityCreationTool = GraphicUtil.loadImage(ORModule.class,
            "db/resources/dbortable.gif"); // Temporary change

    // with new image //
    // NOT LOCALIZABLE -
    // tool image

    public ORTableTool(String text, Image image) {
        super(0, text, image);
        setVisible(ScreenPerspective.isFullVersion()); 
    }

    protected Cursor loadDefaultCursor() {
        // return AwtUtil.loadCursor(ORModule.class,
        // "international/resources/tablecursor.gif", new Point(8,8), "box");
        return AwtUtil.createCursor(kImageTableCreationTool, new Point(8, 8), "box", true); // NOT LOCALIZABLE, not yet
    }

    protected final void createBox(int x, int y) {
        try {
            DbORDiagram diagGO = (DbORDiagram) ((ApplicationDiagram) model).getDiagramGO();
            diagGO.getDb().beginTrans(Db.WRITE_TRANS, LocaleMgr.action.getString("CreateTable"));
            SMSToolkit tk = SMSToolkit.getToolkit(diagGO);
            DbORTable tableSO = (DbORTable) tk.createDbObject(DbORTable.metaClass, diagGO
                    .getComposite(), null);
            DbORTableGo tableGO = new DbORTableGo(diagGO, tableSO);
            tableGO.setRectangle(new Rectangle(x - 30, y - 30, 60, 60));
            diagGO.getDb().commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
    }
}
