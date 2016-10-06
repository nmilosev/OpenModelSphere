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

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbUDFValue;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.LineEnd;
import org.modelsphere.jack.srtool.graphic.SrLine;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.or.db.DbORAssociationGo;

public final class SMSGraphicalLink extends SrLine implements DbRefreshListener {

    private HashSet zonesSources;
    private ArrayList zones = null;

    public SMSGraphicalLink(Diagram diag, DbSMSLinkGo newLinkGO, GraphicNode node1,
            GraphicNode node2) throws DbException {
        super(diag, newLinkGO, newLinkGO.getSO(), node1, node2);
        objectInit(newLinkGO);
    }

    // overriden
    public boolean isStandAloneSupported() {
        return true;
    }

    private void objectInit(DbSMSLinkGo newLinkGO) throws DbException {
        MetaField.addDbRefreshListener(this, semObj.getProject(),
                new MetaField[] { DbUDFValue.fValue });
        semObj.addDbRefreshListener(this);
        lineGo.addDbRefreshListener(this);
        refreshLine();
        refreshLineEnds();
        refreshStereotype(newLinkGO);
    }

    // BEWARE: overriding methods must call super.delete as last action
    public void delete(boolean all) {
        MetaField.removeDbRefreshListener(this, new MetaField[] { DbUDFValue.fValue });
        lineGo.removeDbRefreshListener(this);
        semObj.removeDbRefreshListener(this);
        super.delete(all);
    }

    public final void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
        if (DbSMSGraphicalObject.fStyle == event.metaField
                || event.metaField == DbSMSLinkGo.fDashStyle
                || event.metaField == DbSMSLinkGo.fHighlight
                || event.metaField == DbSMSLinkGo.fLineColor) {
            refreshLine();
        }
    }

    public final DbSMSLinkGo getLinkGO() {
        return (DbSMSLinkGo) lineGo;
    }

    private final void refreshLine() throws DbException {
        DbSMSLinkGo linkGO = getLinkGO();
        setLineColor((Color) linkGO.find(DbSMSLinkGo.fLineColor));

        Boolean highlight = (Boolean) linkGO.find(DbSMSLineGo.fHighlight);
        if (highlight == null) {
            highlight = Boolean.FALSE;
        }
        Boolean dashStyle = (Boolean) linkGO.find(DbSMSLineGo.fDashStyle);
        if (dashStyle == null) {
            dashStyle = Boolean.TRUE;
        }
        setLineStyle(highlight, dashStyle);
    }

    private final void refreshLineEnds() throws DbException {
        LineEnd end = LineEnd.createArrowLineEnd(null);
        setEnd2(end);
    }

    private final void refreshStereotype(DbSMSLinkGo newLinkGO) throws DbException {
        boolean nameDisplayed = true;
        Point p = newLinkGO.getCenterOffset();
        if (p == null)
            return;

        // get stereotype name, if any
        String label = "";
        DbSMSLink link = (DbSMSLink) newLinkGO.getSO();
        DbSMSStereotype stereotype = link.getUmlStereotype();
        if (stereotype != null) {
            label = stereotype.getName();
            if (label != null) {
                label = "«" + label + "»";
            }
        }

        // draw label
        NameAndDuplicateLabel currentLabel = (NameAndDuplicateLabel) getCenterLabel();
        if (label == "") {
            if (currentLabel != null) {
                setCenterLabel(null);
                currentLabel.delete(false);
            }
        } else {
            if (currentLabel == null) {
                Diagram diagram = getDiagram();
                currentLabel = new NameAndDuplicateLabel(diagram, semObj, this,
                        DbSMSLinkGo.fCenterOffset, nameDisplayed, newLinkGO, "");
                setCenterLabel(currentLabel);
            }
            // currentLabel.setFont(font);
            currentLabel.setValue(label);
        }

    } // end refreshStereotype()

} // end SMSGraphicalLink

