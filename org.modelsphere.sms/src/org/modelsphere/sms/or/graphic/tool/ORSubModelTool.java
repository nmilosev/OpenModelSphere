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
import org.modelsphere.sms.SMSExplorer;
import org.modelsphere.sms.SMSToolkit;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSPackageGo;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.or.ORModule;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.preference.DiagramAutomaticCreationOptionGroup;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class ORSubModelTool extends BoxTool {

    public static final String kCreationTool = LocaleMgr.screen.getString("subModels");
    public static final Image kRelationalSubModels = GraphicUtil.loadImage(ORModule.class,
            "db/resources/dbordatamodel.gif"); // Temporary
    // change with
    // new image //
    // NOT
    // LOCALIZABLE -
    // tool image
    public static final Image kConceptualSubModels = GraphicUtil.loadImage(SMSExplorer.class,
            "or/db/resources/dborconceptualmodel.gif");

    public ORSubModelTool(Image kImageTool) {
        super(0, kCreationTool, kImageTool);
        setVisible(ScreenPerspective.isFullVersion()); 
    } // end ORTableTool()

    protected Cursor loadDefaultCursor() {
        // return AwtUtil.loadCursor(ORModule.class,
        // "international/resources/tablecursor.gif", new Point(8,8), "box");
        return AwtUtil.createCursor(icon, new Point(8, 8), "box", true); // NOT
        // LOCALIZABLE,
        // not
        // yet
    }

    protected final void createBox(int x, int y) {
        try {
            DbSMSDiagram diagGO = (DbSMSDiagram) ((ApplicationDiagram) model).getDiagramGO();
            diagGO.getDb().beginTrans(Db.WRITE_TRANS, kCreationTool);

            // create data model
            SMSToolkit toolkit = SMSToolkit.getToolkit(diagGO);
            DbORDataModel model = (DbORDataModel) diagGO.getComposite();
            DbSMSTargetSystem ts = model.getTargetSystem();
            Object[] params = new Object[] { ts };
            DbORDataModel submodel = (DbORDataModel) toolkit.createDbObject(
                    DbORDataModel.metaClass, model, params);

            // and its graphical representation
            DbSMSPackageGo packGo = new DbSMSPackageGo(diagGO, submodel);
            packGo.setRectangle(new Rectangle(x - 30, y - 30, 60, 60));

            // and its diagram (depending of the options)
            if (DiagramAutomaticCreationOptionGroup.isCreateDataModelDiagram()) {
                new DbORDiagram(submodel);
            }

            diagGO.getDb().commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        } // end try

    } // end createBox()

} // end ORSubModelTool
