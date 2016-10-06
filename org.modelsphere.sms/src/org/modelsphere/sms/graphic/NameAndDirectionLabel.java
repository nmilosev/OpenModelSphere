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

package org.modelsphere.sms.graphic;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Polygon;
import java.awt.Rectangle;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.Line;
import org.modelsphere.jack.srtool.graphic.SrLine;
import org.modelsphere.sms.db.DbSMSAssociation;
import org.modelsphere.sms.db.DbSMSAssociationGo;
import org.modelsphere.sms.db.DbSMSLineGo;

public class NameAndDirectionLabel extends NameAndDuplicateLabel {

    public NameAndDirectionLabel(Diagram diag, DbObject semObj, SrLine association,
            MetaField offsetMF, boolean editable, DbSMSLineGo go, String sName) throws DbException {
        super(diag, semObj, association, offsetMF, editable, go, sName);
    } // end NameAndDirectionLabel()

    // overrides GraphicalComponent
    public Rectangle getContentRect() {
        Rectangle rect = super.getContentRect();
        return rect;
    }

    // overrides NameAndDuplicateLabel
    protected void prepaint(Graphics g, DiagramView diagView) {
        Rectangle contentRect = getContentRect();
        if (contentRect != null) {
            if (diagView != null) {
                contentRect = diagView.zoom(contentRect);
            }

            int x = contentRect.x + contentRect.width + 1;
            int y = contentRect.y;
            int angle = computeAngle();
            paintTriangle(g, contentRect, angle);
        }
    } // end prepaint()

    //
    // private methods
    // 
    private int computeAngle() {
        boolean frontEnd = true;
        try {
            DbSMSLineGo go = super.getGo();
            go.getDb().beginReadTrans();
            if (go instanceof DbSMSAssociationGo) {
                DbSMSAssociationGo assocGo = (DbSMSAssociationGo) go;
                DbSMSAssociation assoc = assocGo.getAssociation();
                frontEnd = assoc.isToFrontEnd();
            } // end if
            go.getDb().commitTrans();
        } catch (DbException ex) {
            frontEnd = true;
        } // end try

        Line line = this.getLine();
        Polygon poly = line.getPoly();
        int x1 = poly.xpoints[0];
        int x2 = poly.xpoints[poly.npoints - 1];
        int y1 = poly.ypoints[0];
        int y2 = poly.ypoints[poly.npoints - 1];
        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);

        // if delta X higher than delta Y, then EAST or WEST
        int angle = GridBagConstraints.EAST;
        if (dx > dy) {
            angle = (x1 < x2) ? GridBagConstraints.EAST : GridBagConstraints.WEST;
        } else { // else, then NORTH OR SOUTH
            angle = (y1 < y2) ? GridBagConstraints.SOUTH : GridBagConstraints.NORTH;
        }

        if (!frontEnd) {
            angle = getOpposite(angle);
        }

        return angle;
    } // end computeAngle()

    private int getOpposite(int angle) {
        int opposite;

        switch (angle) {
        case GridBagConstraints.EAST:
            opposite = GridBagConstraints.WEST;
            break;
        case GridBagConstraints.SOUTH:
            opposite = GridBagConstraints.NORTH;
            break;
        case GridBagConstraints.WEST:
            opposite = GridBagConstraints.EAST;
            break;
        case GridBagConstraints.NORTH:
        default:
            opposite = GridBagConstraints.SOUTH;
            break;
        } // end switch()

        return opposite;
    } // end getOpposite()

    private void paintTriangle(Graphics g, Rectangle contentRect, int angle) {
        int x, y;
        int size = (int) (contentRect.height * 0.7);

        // draw a triangle
        int nbPoints = 3;
        int xpoints[], ypoints[];

        switch (angle) {
        case GridBagConstraints.EAST:
            x = contentRect.x + contentRect.width + 1;
            y = contentRect.y;
            xpoints = new int[] { x, x + size, x };
            ypoints = new int[] { y, y + size / 2, y + size };
            break;
        case GridBagConstraints.SOUTH:
            x = contentRect.x + (contentRect.width / 2);
            y = contentRect.y + contentRect.height + 1;
            xpoints = new int[] { x, x + size, x + size / 2 };
            ypoints = new int[] { y, y, y + size };
            break;
        case GridBagConstraints.WEST:
            x = contentRect.x - contentRect.height - 1;
            y = contentRect.y;
            xpoints = new int[] { x + size, x, x + size };
            ypoints = new int[] { y, y + size / 2, y + size };
            break;
        case GridBagConstraints.NORTH:
        default:
            x = contentRect.x + (contentRect.width / 2);
            y = contentRect.y - contentRect.height;
            xpoints = new int[] { x, x + size / 2, x + size };
            ypoints = new int[] { y + size, y, y + size };
            break;
        } // end switch()

        Polygon triangle = new Polygon(xpoints, ypoints, nbPoints);
        g.fillPolygon(triangle);
    } // end paintDirection()

} // end NameAndDirectionLabel()
