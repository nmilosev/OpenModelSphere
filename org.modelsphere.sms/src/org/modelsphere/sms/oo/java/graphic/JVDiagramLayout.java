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
package org.modelsphere.sms.oo.java.graphic;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.graphic.GraphLayoutLink;
import org.modelsphere.jack.graphic.GraphLayoutNode;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.srtool.graphic.DiagramLayout;
import org.modelsphere.sms.db.DbSMSAssociationGo;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSInheritanceGo;
import org.modelsphere.sms.db.DbSMSLineGo;
import org.modelsphere.sms.db.DbSMSPackageGo;
import org.modelsphere.sms.oo.db.DbOOAssociation;
import org.modelsphere.sms.oo.db.DbOOAssociationEnd;
import org.modelsphere.sms.oo.db.srtypes.OOAggregation;
import org.modelsphere.sms.oo.java.db.DbJVClass;

public final class JVDiagramLayout extends DiagramLayout {

    public JVDiagramLayout(DbObject diagGO, GraphicComponent[] selComps) throws DbException {
        super(diagGO, selComps);
    }

    protected final boolean isNode(DbObject go) {
        return (go instanceof DbSMSClassifierGo || go instanceof DbSMSPackageGo);
    }

    /*
     * If directional link, mark it as an <hierarchical link> with the following priority: 1
     * aggregation, 2 composite, 3 implements, 4 extends.
     */
    protected final void setLinkHierarchy(GraphLayoutLink link, DbObject lineGo, DbObject frontGo,
            GraphLayoutNode frontNode, DbObject backGo, GraphLayoutNode backNode)
            throws DbException {
        if (lineGo instanceof DbSMSInheritanceGo) {
            DbJVClass subClass = (DbJVClass) ((DbSMSClassifierGo) frontGo).getClassifier();
            DbJVClass superClass = (DbJVClass) ((DbSMSClassifierGo) backGo).getClassifier();
            int prio = (!subClass.isInterface() && superClass.isInterface() ? 3 : 4);
            link.setHierar(backNode, frontNode, prio);
        } else if (lineGo instanceof DbSMSAssociationGo) {
            DbOOAssociation assoc = (DbOOAssociation) ((DbSMSAssociationGo) lineGo)
                    .getAssociation();
            DbOOAssociationEnd assocEnd = assoc.getFrontEnd();
            OOAggregation aggreg = assocEnd.getAggregation();
            if (aggreg == null || aggreg.getValue() == OOAggregation.NONE) {
                assocEnd = assoc.getBackEnd();
                aggreg = assocEnd.getAggregation();
                if (aggreg == null || aggreg.getValue() == OOAggregation.NONE)
                    return;
            }
            int prio = (aggreg.getValue() == OOAggregation.COMPOSITE ? 2 : 1);
            if (assocEnd.getAssociationMember().getComposite() == ((DbSMSClassifierGo) backGo)
                    .getClassifier())
                link.setHierar(backNode, frontNode, prio);
            else
                link.setHierar(frontNode, backNode, prio);
        }
    }

    protected final void resetLabelPos(DbObject lineGo) throws DbException {
        ((DbSMSLineGo) lineGo).resetLabelsPosition();
    }
}
