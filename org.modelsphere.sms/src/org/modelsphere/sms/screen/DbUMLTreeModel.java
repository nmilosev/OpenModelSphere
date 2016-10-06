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

package org.modelsphere.sms.screen;

import javax.swing.Icon;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.screen.model.DbLookupNode;
import org.modelsphere.jack.baseDb.screen.model.DbTreeModel;
import org.modelsphere.jack.baseDb.screen.model.DbTreeModelListener;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.be.db.DbBENotation;

public class DbUMLTreeModel extends DbTreeModel {

    private boolean isUML = false;

    public DbUMLTreeModel(DbObject[] roots, MetaClass[] metaClasses, DbTreeModelListener listener,
            String nullStr, boolean isUML) throws DbException {
        super(roots, metaClasses, listener, nullStr);
        setUML(isUML);
    }

    public boolean isUML() {
        return isUML;
    }

    public void setUML(boolean isUML) {
        this.isUML = isUML;
    }

    protected DbLookupNode createNode(DbObject dbo) throws DbException {
        boolean ancestor = isAncestor(dbo);
        boolean selectable = isInstance(dbo);
        if (!(ancestor || selectable))
            return null;
        String name;
        Icon icon = null;
        if (listener == null) {
            name = ApplicationContext.getSemanticalModel().getDisplayText(dbo, DbObject.SHORT_FORM,
                    null, DbUMLTreeModel.class);
            icon = dbo.getSemanticalIcon(DbObject.SHORT_FORM);
        } else {
            if (!listener.filterNode(dbo))
                return null;
            name = listener.getDisplayText(dbo, this);
            if (terminologyUtil.isDataModel(dbo)) {
                if (terminologyUtil.getModelLogicalMode(dbo) == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                    icon = terminologyUtil.getConceptualModelIcon();
            } else if (terminologyUtil.isObjectEntityOrAssociation(dbo)) {
                if (terminologyUtil.isObjectAssociation(dbo))
                    icon = terminologyUtil.getAssociationIcon();
            } else if (terminologyUtil.isObjectArc(dbo)) {
                DbObject dataModel = terminologyUtil.isCompositeDataModel(dbo);
                if (dataModel != null)
                    if (terminologyUtil.getModelLogicalMode(dataModel) == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                        icon = terminologyUtil.getArcIcon();
            } else if (terminologyUtil.isObjectRole(dbo)) {
                if (!terminologyUtil.isObjectArcEndRole(dbo)) {
                    DbObject dataModel = terminologyUtil.isCompositeDataModel(dbo);
                    if (dataModel != null)
                        if (terminologyUtil.getModelLogicalMode(dataModel) == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                            return null;
                }
            } else if (terminologyUtil.isObjectUseCaseOrBEModel(dbo)) {
                if (terminologyUtil.isObjectUseCase(dbo))
                    icon = terminologyUtil.getUseCaseIcon(dbo);
                else
                    icon = terminologyUtil.findModelTerminology(dbo).getIcon(dbo.getMetaClass());
            } else
                icon = listener.getIcon(dbo);

            if (icon == null)
                icon = listener.getIcon(dbo);

            if (selectable)
                selectable = listener.isSelectable(dbo);
        }
        if (dbo instanceof DbBENotation) {
            int notationID = ((DbBENotation) dbo).getMasterNotationID().intValue();
            if (isUML) {
                if (notationID >= 13 && notationID <= 19)
                    return new DbLookupNode(dbo, name, icon, ancestor, selectable);
            } else {
                if (notationID < 13 || notationID > 19)
                    return new DbLookupNode(dbo, name, icon, ancestor, selectable);
            }
            return null;
        }
        return new DbLookupNode(dbo, name, icon, ancestor, selectable);
    }

}
