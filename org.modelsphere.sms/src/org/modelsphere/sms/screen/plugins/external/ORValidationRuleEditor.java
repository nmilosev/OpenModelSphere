/*************************************************************************

Copyright (C) 2009 Grandite

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

You can redistribute and/or modify this particular file even under the
terms of the GNU Lesser General Public License (LGPL) as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

You should have received a copy of the GNU Lesser General Public License 
(LGPL) along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.screen.plugins.external;

import java.awt.Component;

import org.modelsphere.jack.awt.AbstractTableCellEditor;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.screen.ScreenView;
import org.modelsphere.jack.baseDb.screen.model.*;
import org.modelsphere.jack.baseDb.screen.plugins.DomainEditor;
import org.modelsphere.sms.db.DbSMSAssociationEnd;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.features.ReferentialRules;

/**
 * 
 * 
 */
@SuppressWarnings("serial")
public final class ORValidationRuleEditor extends DomainEditor {

    private ORValidationRuleRenderer renderer;

    public ORValidationRuleEditor() {
        renderer = new ORValidationRuleRenderer();
        setRenderer(renderer);
    }

    @Override
    public Component getTableCellEditorComponent(ScreenView screenView,
            AbstractTableCellEditor tableCellEditorListener, Object value, boolean isSelected,
            int row, int column) {
        Component editor = super.getTableCellEditorComponent(screenView, tableCellEditorListener,
                value, isSelected, row, column);

        SMSMultiplicity multiplicity = null;
        SMSMultiplicity oppositeMultiplicity = null;
        Boolean dependency = null;
        Boolean child = null;
        int action = -1;

        ScreenModel model = screenView.getModel();

        // We usually avoid transaction in getTableCellEditorComponent() but since it is an editor, it will 
        // be called only once when the editor is requested in the UI.  And this is currently the only way 
        // to obtain the information we need (oppositeEnd multiplicity)
        DbObject dbo = null;

        MetaField metafield = null;
        if (model instanceof DbDescriptionModel) {
            dbo = ((DbDescriptionModel) model).getDbObject();
            DescriptionField field = ((DbDescriptionModel) model).getDescriptionField(row);
            if (field instanceof MetaFieldDescriptionField) {
                metafield = ((MetaFieldDescriptionField) field).getMetaField();
            }
        } else if (model instanceof DbListModel) {
            DbDescriptionModel descriptionModel = ((DbListModel) model)
                    .getDbDescriptionModelAt(row);
            dbo = descriptionModel.getDbObject();
            DescriptionField field = descriptionModel.getDescriptionField(column);
            if (field instanceof MetaFieldDescriptionField) {
                metafield = ((MetaFieldDescriptionField) field).getMetaField();
            }
        }

        if (dbo != null) {
            try {
                multiplicity = null;
                oppositeMultiplicity = null;
                dependency = null;
                child = null;
                dbo.getDb().beginReadTrans();
                if (dbo instanceof DbORAssociationEnd) {
                    DbORAssociationEnd oppositeEnd = ((DbORAssociationEnd) dbo).getOppositeEnd();
                    if (oppositeEnd != null) {
                        multiplicity = ((DbSMSAssociationEnd) dbo).getMultiplicity();
                        oppositeMultiplicity = ((DbSMSAssociationEnd) oppositeEnd)
                                .getMultiplicity();

                        child = ((DbORAssociationEnd) dbo).isNavigable();
                        DbORAssociationEnd childEnd = child ? ((DbORAssociationEnd) dbo)
                                : oppositeEnd;
                        DbRelationN dependencies = childEnd.getDependentConstraints();
                        dependency = dependencies != null && dependencies.size() > 0;

                        dbo.getDb().commitTrans();

                        if (metafield == DbORAssociationEnd.fInsertRule) {
                            action = ReferentialRules.INSERT;
                        } else if (metafield == DbORAssociationEnd.fUpdateRule) {
                            action = ReferentialRules.UPDATE;
                        } else if (metafield == DbORAssociationEnd.fDeleteRule) {
                            action = ReferentialRules.DELETE;
                        } else {
                            action = -1;
                        }
                    }
                }
            } catch (DbException e) {
                // reset values and rethrow the exception so it is handled by the table component
                // (top level component handling the source event).
                multiplicity = null;
                oppositeMultiplicity = null;
                dependency = null;
                child = null;
                action = -1;
                throw new RuntimeException(e);
            }
        }

        renderer.setMultiplicity(multiplicity);
        renderer.setOppositeMultiplicity(oppositeMultiplicity);
        renderer.setDependency(dependency);
        renderer.setAction(action);
        renderer.setChild(child);

        return editor;
    }

}
