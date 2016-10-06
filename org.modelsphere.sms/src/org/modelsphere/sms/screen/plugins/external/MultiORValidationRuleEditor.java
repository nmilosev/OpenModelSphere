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

import javax.swing.JTable;

import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.screen.plugins.MultiDomainEditor;
import org.modelsphere.jack.srtool.screen.design.DesignTableModel;
import org.modelsphere.sms.db.DbSMSAssociationEnd;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.features.ReferentialRules;

/**
 * 
 * 
 */
@SuppressWarnings("serial")
public final class MultiORValidationRuleEditor extends MultiDomainEditor {

    private ORValidationRuleRenderer renderer;

    public MultiORValidationRuleEditor() {
        renderer = new ORValidationRuleRenderer();
        setRenderer(renderer);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
            int row, int column) {
        Component editor = super.getTableCellEditorComponent(table, value, isSelected, row, column);

        DesignTableModel model = (DesignTableModel) table.getModel();

        SMSMultiplicity multiplicity = null;
        SMSMultiplicity oppositeMultiplicity = null;
        Boolean dependency = null;
        Boolean child = null;
        int action = -1;

        // We usually avoid transaction in getTableCellEditorComponent() but since it is an editor, it will 
        // be called only once when the editor is requested in the UI.  And this is currently the only way 
        // to obtain the information we need (oppositeEnd multiplicity)
        DbObject[] dbos = model.getDbObjects();
        if (dbos != null) {
            try {
                DbMultiTrans.beginTrans(Db.READ_TRANS, dbos, null);
                for (DbObject dbo : dbos) {
                    if (!(dbo instanceof DbORAssociationEnd)) {
                        multiplicity = null;
                        oppositeMultiplicity = null;
                        break;
                    }
                    DbORAssociationEnd oppositeEnd = ((DbORAssociationEnd) dbo).getOppositeEnd();
                    if (oppositeEnd == null) {
                        multiplicity = null;
                        oppositeMultiplicity = null;
                        break;
                    }
                    SMSMultiplicity tempMultiplicity = ((DbSMSAssociationEnd) dbo)
                            .getMultiplicity();
                    SMSMultiplicity tempOppositeMultiplicity = ((DbSMSAssociationEnd) oppositeEnd)
                            .getMultiplicity();
                    if (tempMultiplicity != null && multiplicity == null) {
                        multiplicity = tempMultiplicity;
                    } else if (tempMultiplicity != null && multiplicity != null
                            && tempMultiplicity.equals(multiplicity)) {
                        multiplicity = tempMultiplicity;
                    } else if (multiplicity != null) {
                        multiplicity = null;
                        break;
                    } else {
                        multiplicity = tempMultiplicity;
                    }
                    if (tempOppositeMultiplicity != null && oppositeMultiplicity == null) {
                        oppositeMultiplicity = tempOppositeMultiplicity;
                    } else if (tempOppositeMultiplicity != null && oppositeMultiplicity != null
                            && tempOppositeMultiplicity.equals(oppositeMultiplicity)) {
                        oppositeMultiplicity = tempOppositeMultiplicity;
                    } else if (oppositeMultiplicity != null) {
                        oppositeMultiplicity = null;
                        break;
                    } else {
                        oppositeMultiplicity = tempOppositeMultiplicity;
                    }

                    boolean tempChild = ((DbORAssociationEnd) dbo).isNavigable();
                    if (child != null && !child.equals(tempChild)) {
                        child = null;
                        break;
                    } else {
                        child = tempChild;
                    }
                    DbORAssociationEnd childEnd = tempChild ? ((DbORAssociationEnd) dbo)
                            : oppositeEnd;
                    DbRelationN dependencies = childEnd.getDependentConstraints();
                    boolean tempDependency = dependencies != null && dependencies.size() > 0;
                    if (dependency != null && !dependency.equals(tempDependency)) {
                        dependency = null;
                        break;
                    } else {
                        dependency = tempDependency;
                    }
                    if (dependency == null) {
                        dependency = Boolean.FALSE;
                    }

                }
                DbMultiTrans.commitTrans(dbos);

                MetaField metafield = model.getMetaFieldAt(row);
                if (metafield == DbORAssociationEnd.fInsertRule) {
                    action = ReferentialRules.INSERT;
                } else if (metafield == DbORAssociationEnd.fUpdateRule) {
                    action = ReferentialRules.UPDATE;
                } else if (metafield == DbORAssociationEnd.fDeleteRule) {
                    action = ReferentialRules.DELETE;
                } else {
                    action = -1;
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
