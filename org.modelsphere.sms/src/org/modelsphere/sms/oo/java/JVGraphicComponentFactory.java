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

package org.modelsphere.sms.oo.java;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.Line;
import org.modelsphere.sms.SMSComponentFactory;
import org.modelsphere.sms.db.DbSMSAssociationGo;
import org.modelsphere.sms.db.DbSMSClassifier;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSInheritanceGo;
import org.modelsphere.sms.db.DbSMSPackageGo;
import org.modelsphere.sms.oo.db.DbOOAdtGo;
import org.modelsphere.sms.oo.graphic.OOAssociation;
import org.modelsphere.sms.oo.graphic.OOInheritance;
import org.modelsphere.sms.oo.graphic.OOPackage;
import org.modelsphere.sms.oo.java.graphic.AdtBox;

public final class JVGraphicComponentFactory extends SMSComponentFactory {

    public GraphicComponent createGraphic(Diagram diag, DbObject go) throws DbException {
        GraphicComponent gc = null;
        if (go instanceof DbSMSClassifierGo) {
            DbSMSClassifier adt = ((DbSMSClassifierGo) go).getClassifier();
            gc = new AdtBox(diag, (DbOOAdtGo) go);
        } else if (go instanceof DbSMSPackageGo) {
            gc = new OOPackage(diag, (DbSMSPackageGo) go);
        } else {
            gc = super.createGraphic(diag, go);
        } // end createGraphic()

        return gc;
    } // end createGraphic()

    public Line createLine(Diagram diag, DbObject go, GraphicNode node1, GraphicNode node2)
            throws DbException {
        Line line = null;

        if (go instanceof DbSMSAssociationGo)
            line = new OOAssociation(diag, (DbSMSAssociationGo) go, node1, node2);
        else if (go instanceof DbSMSInheritanceGo)
            line = new OOInheritance(diag, (DbSMSInheritanceGo) go, node1, node2);
        else {
            line = super.createLine(diag, go, node1, node2);
        } // end if

        return line;
    } // end createLine()
} // end JVGraphicComponent

