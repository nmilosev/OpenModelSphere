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
import java.awt.Point;
import java.awt.Rectangle;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.ZoneBox;
import org.modelsphere.jack.graphic.tool.BoxTool;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.SMSModule;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSNotice;
import org.modelsphere.sms.db.DbSMSNoticeGo;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.graphic.NoticeBox;
import org.modelsphere.sms.international.LocaleMgr;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SMSNoticeTool extends BoxTool {
    private static final String kCreationTool = LocaleMgr.misc.getString("notice");
    private static final Image kImageCreationTool = GraphicUtil.loadImage(SMSModule.class,
            "db/resources/dbsmsnotice.gif"); // NOT LOCALIZABLE

    // - tool image

    // Constructor
    public SMSNoticeTool() {
        super(0, kCreationTool, kImageCreationTool);
    } // end SMSNoticeTool()

    protected Cursor loadDefaultCursor() {
        return org.modelsphere.jack.awt.AwtUtil.loadCursor(SMSModule.class,
                "db/resources/dbsmsnotice.gif", new Point(8, 8), kCreationTool);// NOT
        // LOCALIZABLE
    } // end loadDefaultCursor()

    // Overrides BoxTool
    protected void createBox(int x, int y) {
        try {
            DbSMSDiagram diagGO = (DbSMSDiagram) ((ApplicationDiagram) model).getDiagramGO();
            diagGO.getDb().beginTrans(Db.WRITE_TRANS, kCreationTool);

            // get composite (valid composites are packages and processes)
            DbSMSSemanticalObject composite;
            composite = (DbBEUseCase) diagGO.getCompositeOfType(DbBEUseCase.metaClass);

            if (composite == null) {
                composite = (DbSMSPackage) diagGO.getCompositeOfType(DbSMSPackage.metaClass);
            }

            if (composite == null) {
                return;
            }

            // create notice
            DbSMSNotice notice = new DbSMSNotice(composite);
            initNotice(notice);

            // and its graphical representation
            DbSMSNoticeGo noticeGo = new DbSMSNoticeGo(diagGO);
            noticeGo.setNotice(notice);
            noticeGo.setRectangle(new Rectangle(x, y - 30, 100, 70));
            ApplicationDiagram.lockGridAlignment = true;
            diagGO.getDb().commitTrans();

            GraphicComponent gcPressed = model.graphicAt(this.view, x, y, 0xffffffff, false);
            if (gcPressed != null) {
                if (gcPressed instanceof ZoneBox) {
                    if (gcPressed instanceof NoticeBox) {
                        NoticeBox box = (NoticeBox) gcPressed;
                        if (box != null) {
                            try {
                                box.enterEditMode(model, this.view);
                            } catch (Exception ex) { /* do nothing */
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        } // end try
    } // end createBox()

    //
    // private method
    //

    private void initNotice(DbSMSNotice notice) throws DbException {
        String name = DbSMSNotice.metaClass.getGUIName();
        notice.setName(name);
        notice.setDescription(" ");
        notice.setDescription("");
    } // end initNotive()

} // end SMSNoticeTool
