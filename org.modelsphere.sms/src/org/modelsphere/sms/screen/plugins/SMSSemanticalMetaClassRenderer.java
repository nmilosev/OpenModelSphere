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

package org.modelsphere.sms.screen.plugins;

import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.screen.DefaultRenderer;
import org.modelsphere.jack.baseDb.screen.ScreenView;
import org.modelsphere.sms.db.DbSMSAssociation;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORColumn;

/**
 * 
 * Editor for fields: DbORIndex.fConstraint, DbORPrimaryUnique.fIndex, DbORForeign.fIndex
 * 
 */
public final class SMSSemanticalMetaClassRenderer extends DefaultRenderer {
    public static final SMSSemanticalMetaClassRenderer singleton = new SMSSemanticalMetaClassRenderer();

    private static final String kTableOrEntity = org.modelsphere.sms.international.LocaleMgr.screen
            .getString("TableOrEntity");
    private static final String kAssociationOrArc = org.modelsphere.sms.international.LocaleMgr.screen
            .getString("AssociationOrArc");
    private static final String kColumnOrAttribute = org.modelsphere.sms.international.LocaleMgr.screen
            .getString("ColumnOrAttribute");

    protected void setValue(ScreenView screenView, int row, int column, Object value) {
        MetaClass metaclass = null;
        if (value != null)
            metaclass = MetaClass.find((String) value);
        if (metaclass == null)
            metaclass = DbSMSSemanticalObject.metaClass;

        String text = "";// metaclass.getGUIName(false, true);
        if (metaclass == DbORAbsTable.metaClass)
            text = kTableOrEntity;
        else if (metaclass == DbSMSAssociation.metaClass)
            text = kAssociationOrArc;
        else if (metaclass == DbORColumn.metaClass)
            text = kColumnOrAttribute;
        else
            text = metaclass.getGUIName(false, true);
        // String text = metaclass.getGUIName(false, true);
        if (text.length() == 0)
            return;
        setText(text);
        setToolTipText(text);
    }

}
