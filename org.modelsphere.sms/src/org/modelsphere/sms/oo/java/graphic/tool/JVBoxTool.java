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
package org.modelsphere.sms.oo.java.graphic.tool;

import java.awt.Image;
import java.awt.Rectangle;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.graphic.tool.BoxTool;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.oo.db.DbOOAdtGo;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.srtypes.JVClassCategory;

public abstract class JVBoxTool extends BoxTool {

    public JVBoxTool(String text, Image image) {
        super(0, text, image);
    }

    protected final void createClass(int category, String transName, int x, int y)
            throws DbException {
        DbSMSDiagram diagGO = (DbSMSDiagram) ((ApplicationDiagram) model).getDiagramGO();
        diagGO.getDb().beginTrans(Db.WRITE_TRANS, transName);
        DbJVClass adtSO = new DbJVClass(diagGO.getComposite(), JVClassCategory
                .getInstance(category));
        DbSMSClassifierGo adtGO = new DbOOAdtGo(diagGO, adtSO);
        adtGO.setRectangle(new Rectangle(x - 30, y - 30, 60, 60));
        diagGO.getDb().commitTrans();
    }

}
