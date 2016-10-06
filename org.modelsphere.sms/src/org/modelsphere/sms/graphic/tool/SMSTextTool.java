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

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.graphic.tool.BoxTool;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.FreeText;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSUserTextGo;
import org.modelsphere.sms.international.LocaleMgr;

public class SMSTextTool extends BoxTool {

    public SMSTextTool(String text, Image image) {
        super(0, text, image);
    }

    protected Cursor loadDefaultCursor() {
        return Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
    }

    protected final void createBox(int x, int y) {
        try {
            DbSMSDiagram diagGO = (DbSMSDiagram) ((ApplicationDiagram) model).getDiagramGO();
            diagGO.getDb().beginTrans(Db.WRITE_TRANS, LocaleMgr.action.getString("TextCreation")); // NOT
            // LOCALIZABLE
            DbSMSUserTextGo textGO = new DbSMSUserTextGo(diagGO);
            textGO.setText(LocaleMgr.message.getString("EnterYourText"));// NOT
            // LOCALIZABLE
            textGO.setRectangle(new Rectangle(x - 40, y - 10, 80, 20));
            diagGO.getDb().commitTrans();
            ((FreeText) textGO.getGraphicPeer()).startEditing(view);
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(view, ex);
        }
    }

}
