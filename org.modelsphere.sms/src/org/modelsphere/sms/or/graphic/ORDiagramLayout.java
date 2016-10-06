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

package org.modelsphere.sms.or.graphic;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.graphic.GraphLayoutLink;
import org.modelsphere.jack.graphic.GraphLayoutNode;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.srtool.graphic.DiagramLayout;
import org.modelsphere.sms.db.DbSMSAssociationGo;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSCommonItemGo;
import org.modelsphere.sms.db.DbSMSLineGo;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;

public final class ORDiagramLayout extends DiagramLayout {

    public ORDiagramLayout(DbObject diagGO, GraphicComponent[] selComps) throws DbException {
        super(diagGO, selComps);
    }

    protected final boolean isNode(DbObject go) {
        return (go instanceof DbSMSClassifierGo || go instanceof DbSMSCommonItemGo);
    }

    // Mark the association as an <hierarchical link> and give a higher priority
    // if dependency
    protected final void setLinkHierarchy(GraphLayoutLink link, DbObject lineGo, DbObject frontGo,
            GraphLayoutNode frontNode, DbObject backGo, GraphLayoutNode backNode)
            throws DbException {
        if (lineGo instanceof DbSMSAssociationGo) {
            DbORAssociation assoc = (DbORAssociation) ((DbSMSAssociationGo) lineGo)
                    .getAssociation();
            DbORAssociationEnd end = assoc.getFrontEnd();
            if (end.isNavigable()) {
                link.setHierar(backNode, frontNode, getPrio(end));
            } else {
                end = assoc.getBackEnd();
                if (end.isNavigable())
                    link.setHierar(frontNode, backNode, getPrio(end));
            }
        }
    }

    private int getPrio(DbORAssociationEnd end) throws DbException {
        return (end.getDependentConstraints().size() != 0 ? 2 : 1);
    }

    protected final void resetLabelPos(DbObject lineGo) throws DbException {
        ((DbSMSLineGo) lineGo).resetLabelsPosition();
    }
}
