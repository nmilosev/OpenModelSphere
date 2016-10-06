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
package org.modelsphere.sms.oo;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.db.event.DbUpdateListener;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.sms.db.DbSMSAssociationEnd;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.oo.db.DbOOAbsPackage;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class OOSemanticalIntegrity implements DbUpdateListener {

    public OOSemanticalIntegrity() {
        MetaField.addDbUpdateListener(this, 0, new MetaField[] { DbObject.fComposite,
                DbObject.fComponents, DbSMSAssociationEnd.fMultiplicity,
                DbSMSAssociationEnd.fSpecificRangeMultiplicity,

        //DbORPrimaryUnique.fColumns, DbORIndex.fConstraint, DbORIndexKey.fIndexedElement,
                //DbSemanticalObject.fName
                });
    } //end ORSemanticalIntegrity()

    //Supports DbUpdateListener
    public void dbUpdated(DbUpdateEvent event) throws DbException {
        updateModelValidity(event);
    }

    private void updateModelValidity(DbUpdateEvent event) throws DbException {
        DbObject obj = event.dbo;

        if (obj != null)
            if (obj.getTransStatus() == Db.OBJ_REMOVED)
                return;

        //model is no longer validated if model element is modified 
        obj.getDb().beginWriteTrans("");
        DbOOAbsPackage model = (DbOOAbsPackage) obj.getCompositeOfType(DbOOAbsPackage.metaClass);
        if (model != null) {
            if (event.dbo instanceof DbSemanticalObject) {
                model.setIsValidated(Boolean.FALSE);
            }
        } //end if

        updateSpecificMultiplicity(event);

        obj.getDb().commitTrans();

    } //end updateModelValidity()

    private void updateSpecificMultiplicity(DbUpdateEvent event) throws DbException {
        //if specific multiplicity changes
        if (event.metaField.equals(DbSMSAssociationEnd.fSpecificRangeMultiplicity)) {
            SMSMultiplicity mult = SMSMultiplicity.getInstance(SMSMultiplicity.SPECIFIC);
            event.dbo.set(DbSMSAssociationEnd.fMultiplicity, mult);
            //if multiplicity changes
        } else if (event.metaField.equals(DbSMSAssociationEnd.fMultiplicity)) {
            SMSMultiplicity mult = (SMSMultiplicity) event.dbo
                    .get(DbSMSAssociationEnd.fMultiplicity);

            if (mult.getValue() == SMSMultiplicity.SPECIFIC) {
                String spec = SMSMultiplicity.getInstance(SMSMultiplicity.SPECIFIC)
                        .getUMLMultiplicityLabel();
                event.dbo.set(DbSMSAssociationEnd.fSpecificRangeMultiplicity, spec);
            } else {
                event.dbo.set(DbSMSAssociationEnd.fSpecificRangeMultiplicity, "");
            }
        } //end if
    } //end updateSpecificMultiplicity()

} //end OOSemanticalIntegrity()
