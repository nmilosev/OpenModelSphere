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
package org.modelsphere.sms.or.screen.model;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.baseDb.screen.Renderer;
import org.modelsphere.jack.baseDb.screen.ScreenView;
import org.modelsphere.jack.baseDb.screen.model.DescriptionField;
import org.modelsphere.jack.baseDb.screen.model.ReflectionDescriptionModel;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.or.informix.db.DbINFFragment;
import org.modelsphere.sms.screen.plugins.DbSubObjQualNameRenderer;

public class ORDescriptionModel extends ReflectionDescriptionModel {

    public ORDescriptionModel(ScreenView sv, DbObject parentObj, MetaClass metaClass,
            MetaRelationN[] listRelations) throws DbException {
        super(sv, parentObj, metaClass, listRelations);
    }

    public ORDescriptionModel(ScreenView sv, DbObject childObj,
            ReflectionDescriptionModel columnModel) throws DbException {
        super(sv, childObj, columnModel);
    }

    protected final Renderer getNameFieldRenderer(Renderer renderer) {
        if (listRelations != null && listRelations[0] == DbSMSSemanticalObject.fSubCopies)
            return DbSubObjQualNameRenderer.singleton;
        return renderer;
    }

    protected final DescriptionField createDescriptionField(MetaField field) throws DbException {
        DescriptionField dField = null;
        dField = super.createDescriptionField(field, -1);

        if (field == DbINFFragment.fExpression)
            dField.setDisplayWidth(-120); // negative means calculate width from
        // contents

        return dField;
    }

}
