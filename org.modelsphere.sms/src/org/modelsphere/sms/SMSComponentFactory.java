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
/*
 * Created on Oct 28, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.modelsphere.sms;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.Line;
import org.modelsphere.jack.graphic.shape.OvalShape;
import org.modelsphere.jack.graphic.shape.RectangleShape;
import org.modelsphere.jack.graphic.shape.RoundRectShape;
import org.modelsphere.jack.srtool.graphic.DiagramImage;
import org.modelsphere.jack.srtool.graphic.FreeBox;
import org.modelsphere.jack.srtool.graphic.FreeLine;
import org.modelsphere.jack.srtool.graphic.FreeText;
import org.modelsphere.jack.srtool.graphic.GraphicComponentFactory;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.graphic.BEStamp;
import org.modelsphere.sms.db.DbSMSFreeFormGo;
import org.modelsphere.sms.db.DbSMSFreeLineGo;
import org.modelsphere.sms.db.DbSMSImageGo;
import org.modelsphere.sms.db.DbSMSLinkGo;
import org.modelsphere.sms.db.DbSMSNoticeGo;
import org.modelsphere.sms.db.DbSMSStampGo;
import org.modelsphere.sms.db.DbSMSUserTextGo;
import org.modelsphere.sms.db.srtypes.SMSFreeForm;
import org.modelsphere.sms.graphic.NoticeBox;
import org.modelsphere.sms.graphic.SMSGraphicalLink;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.graphic.OOStamp;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.graphic.ORStamp;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SMSComponentFactory implements GraphicComponentFactory {

    public GraphicComponent createGraphic(org.modelsphere.jack.graphic.Diagram diag, DbObject go)
            throws DbException {
        GraphicComponent gc = null;

        if (go instanceof DbSMSUserTextGo)
            gc = new FreeText(diag, go);
        else if (go instanceof DbSMSStampGo) {
            DbObject dbo = go.getComposite();
            if (dbo instanceof DbOODiagram)
                gc = new OOStamp(diag, go);
            else if (dbo instanceof DbORDiagram)
                gc = new ORStamp(diag, go);
            else if (dbo instanceof DbBEDiagram)
                gc = new BEStamp(diag, go);
            else
                gc = new OOStamp(diag, go);
        } else if (go instanceof DbSMSImageGo)
            gc = new DiagramImage(diag, go);
        else if (go instanceof DbSMSFreeLineGo)
            gc = new FreeLine(diag, go);
        else if (go instanceof DbSMSFreeFormGo)
            gc = createFreeBox(diag, (DbSMSFreeFormGo) go);
        else if (go instanceof DbSMSNoticeGo)
            gc = new NoticeBox(diag, (DbSMSNoticeGo) go);

        return gc;
    } // end createGraphic()

    public Line createLine(org.modelsphere.jack.graphic.Diagram diag, DbObject go,
            GraphicNode node1, GraphicNode node2) throws DbException {
        Line line = null;

        if (go instanceof DbSMSLinkGo)
            line = new SMSGraphicalLink(diag, (DbSMSLinkGo) go, node1, node2);

        return line;
    } // end createLine()

    /*
     * Private methods
     */
    private final FreeBox createFreeBox(org.modelsphere.jack.graphic.Diagram diag,
            DbSMSFreeFormGo go) throws DbException {
        FreeBox gc = null;
        switch (((SMSFreeForm) go.getCategory()).getValue()) {
        case SMSFreeForm.RECTANGLE:
            gc = new FreeBox(diag, go, RectangleShape.singleton);
            break;
        case SMSFreeForm.ROUND_RECTANGLE:
            gc = new FreeBox(diag, go, RoundRectShape.singleton);
            break;
        case SMSFreeForm.OVAL:
            gc = new FreeBox(diag, go, OvalShape.singleton);
            break;
        }
        return gc;
    } // end createFreeBox()

} // end SMSCompenntFactory
