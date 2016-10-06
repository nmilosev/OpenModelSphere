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

package org.modelsphere.sms.screen.model;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.baseDb.screen.ScreenView;
import org.modelsphere.jack.baseDb.screen.model.DescriptionField;
import org.modelsphere.jack.baseDb.screen.model.MetaFieldDescriptionField;
import org.modelsphere.jack.baseDb.screen.model.ReflectionDescriptionModel;
import org.modelsphere.jack.baseDb.screen.plugins.DbSemObjFullNameRenderer;
import org.modelsphere.sms.db.DbSMSLink;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.screen.SMSPropertiesFrame;

public class SMSDescriptionModel extends ReflectionDescriptionModel {

    public SMSDescriptionModel(ScreenView sv, DbObject parentObj, MetaClass metaClass,
            MetaRelationN[] listRelations) throws DbException {
        super(sv, parentObj, metaClass, listRelations);
    }

    public SMSDescriptionModel(ScreenView sv, DbObject childObj,
            ReflectionDescriptionModel columnModel) throws DbException {
        super(sv, childObj, columnModel);
    }

    protected final void createTextAndDescriptionFields() throws DbException {
        if (listRelations == SMSPropertiesFrame.linksTabRelations) { // it is
            // the
            // <Links>
            // tab.
            createDescriptionField(nameField, -1);

            Object value = (semObj != null ? semObj.get(DbSMSLink.fSourceObjects) : null);
            DescriptionField dField = new MetaFieldDescriptionField(this, DbSMSLink.fSourceObjects,
                    "", DbSemObjFullNameRenderer.singleton, value, semObj, LocaleMgr.screen
                            .getString("sourceObj1"));
            dField.setEditable(false);
            addDescriptionField(dField);

            value = (semObj != null ? semObj.get(DbSMSLink.fTargetObjects) : null);
            dField = new MetaFieldDescriptionField(this, DbSMSLink.fTargetObjects, "",
                    DbSemObjFullNameRenderer.singleton, value, semObj, LocaleMgr.screen
                            .getString("targetObj1"));
            dField.setEditable(false);
            addDescriptionField(dField);

            createDescriptionField(DbSemanticalObject.fDescription, -1);
        } else
            super.createTextAndDescriptionFields();
    }
}
