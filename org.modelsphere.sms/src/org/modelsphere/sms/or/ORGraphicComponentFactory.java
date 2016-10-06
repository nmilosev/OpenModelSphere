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
package org.modelsphere.sms.or;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.Line;
import org.modelsphere.sms.SMSComponentFactory;
import org.modelsphere.sms.db.DbSMSClassifier;
import org.modelsphere.sms.db.DbSMSCommonItemGo;
import org.modelsphere.sms.db.DbSMSPackageGo;
import org.modelsphere.sms.or.db.DbORAssociationGo;
import org.modelsphere.sms.or.db.DbORChoiceOrSpecialization;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTableGo;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.db.DbORView;
import org.modelsphere.sms.or.db.srtypes.ORChoiceSpecializationCategory;
import org.modelsphere.sms.or.graphic.ORAssociation;
import org.modelsphere.sms.or.graphic.ORChoiceSpecialization;
import org.modelsphere.sms.or.graphic.ORCommonItemBox;
import org.modelsphere.sms.or.graphic.ORModelBox;
import org.modelsphere.sms.or.graphic.ORTable;
import org.modelsphere.sms.or.graphic.ORTypeBox;
import org.modelsphere.sms.or.graphic.ORView;

public final class ORGraphicComponentFactory extends SMSComponentFactory {

    public GraphicComponent createGraphic(Diagram diag, DbObject go) throws DbException {
        GraphicComponent gc = null;
        if (go instanceof DbORTableGo) {
            DbSMSClassifier classifier = ((DbORTableGo) go).getClassifier();
            if (classifier instanceof DbORView)
                gc = new ORView(diag, (DbORTableGo) go);
            else if (classifier instanceof DbORTable)
                gc = new ORTable(diag, (DbORTableGo) go);
            else if (classifier instanceof DbORTypeClassifier)
                gc = new ORTypeBox(diag, (DbORTableGo) go);
            if (classifier instanceof DbORChoiceOrSpecialization) 
                gc = createChoiceOrSpecialization(diag, (DbORTableGo) go);
        } else if (go instanceof DbSMSCommonItemGo)
            gc = new ORCommonItemBox(diag, (DbSMSCommonItemGo) go);
        else if (go instanceof DbSMSPackageGo)
            gc = new ORModelBox(diag, (DbSMSPackageGo) go);
        else {
            gc = super.createGraphic(diag, go);
        } // end if

        return gc;
    } // end createGraphic()

    public final Line createLine(org.modelsphere.jack.graphic.Diagram diag, DbObject go,
            GraphicNode node1, GraphicNode node2) throws DbException {
        Line line = null;
        if (go instanceof DbORAssociationGo) {
            DbORAssociationGo assocGo = (DbORAssociationGo) go;
            line = new ORAssociation(diag, assocGo, node1, node2);
        } else {
            line = super.createLine(diag, go, node1, node2);
        } // end if

        return line;
    } // end createLine()

    private GraphicComponent createChoiceOrSpecialization(Diagram diag, DbORTableGo go)
            throws DbException {
        DbORChoiceOrSpecialization choiceSpec = (DbORChoiceOrSpecialization) go.getClassifier();
        ORChoiceSpecializationCategory categ = choiceSpec.getCategory();
        int value = (categ == null) ? 0 : categ.getValue();
        GraphicComponent gc;

        if (value == ORChoiceSpecializationCategory.CHOICE) {
            gc = ORChoiceSpecialization.createChoice(diag, go);
        } else if (value == ORChoiceSpecializationCategory.SPECIALIZATION) {
            gc = ORChoiceSpecialization.createSpecialization(diag, go);
        } else {
            gc = null;
        }

        return gc;
    }
} // end ORGraphicComponentFActory

