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

package org.modelsphere.jack.baseDb.screen.model;

import java.util.ArrayList;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.DbUDF;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.baseDb.screen.DbListView;
import org.modelsphere.jack.baseDb.screen.DefaultRenderer;
import org.modelsphere.jack.baseDb.screen.Renderer;
import org.modelsphere.jack.baseDb.screen.ScreenPlugins;
import org.modelsphere.jack.baseDb.screen.ScreenView;
import org.modelsphere.jack.baseDb.screen.plugins.DbSemObjFullNameRenderer;
import org.modelsphere.jack.baseDb.screen.plugins.DbSemanticalObjectRenderer;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.SemanticalModel;

public class ReflectionDescriptionModel extends DbDescriptionModel {

    private static final String kObjectType = LocaleMgr.screen.getString("ObjectType");

    protected DbProject project;
    protected DbObject parentObj = null; // null for a DescriptionView, not null for a ListView
    protected MetaClass metaClass;
    protected MetaRelationN[] listRelations = null; // null for a DescriptionView, not null for a ListView
    protected ReflectionDescriptionModel columnModel = null;
    protected SemanticalModel semanticalModel;
    protected MetaField nameField = DbSemanticalObject.fName;

    /*
     * Use of constructors:
     * 
     * To create a DescriptionView model: new ReflectionDescriptionModel(descrView, semObj);
     * 
     * To create a ListView header: new ReflectionDescriptionModel(listView, parentObj, metaClass,
     * listRelations); To create a ListView row: new ReflectionDescriptionModel(listView, childObj,
     * listView.getColumnModel());
     */
    public ReflectionDescriptionModel(ScreenView sv, DbObject semObj) throws DbException {
        super(sv, semObj);
        project = semObj.getProject();
        metaClass = semObj.getMetaClass();
        initModel();
    }

    public ReflectionDescriptionModel(ScreenView sv, DbObject parentObj, MetaClass metaClass,
            MetaRelationN[] listRelations) throws DbException {
        super(sv, null);
        project = parentObj.getProject();
        this.parentObj = parentObj;
        this.metaClass = metaClass;
        this.listRelations = listRelations;
        if (((DbListView) sv).getParentRel() != null)
            nameField = ((DbListView) sv).getParentRel();
        initModel();
    }

    public ReflectionDescriptionModel(ScreenView sv, DbObject childObj,
            ReflectionDescriptionModel columnModel) throws DbException {
        super(sv, childObj);
        project = columnModel.project;
        parentObj = columnModel.parentObj;
        metaClass = columnModel.metaClass;
        listRelations = columnModel.listRelations;
        nameField = columnModel.nameField;
        this.columnModel = columnModel;
        initModel();
    }

    private void initModel() throws DbException {
        semanticalModel = ApplicationContext.getSemanticalModel();
        if (listRelations != null)
            createRowSelectionColumn();
        if ((screenView.getActionMode() & ScreenView.NAME_ONLY) != 0 && listRelations != null) {
            createDescriptionField(nameField, -1);
        } else {
            if ((screenView.getActionMode() & ScreenView.DISPLAY_PARENT_NAME) != 0
                    && listRelations != null) {
                createDescriptionField(nameField, -1);
            }
            createTextAndDescriptionFields();
            createUDFFields();
        }
    }

    // Overridden
    protected void createRowSelectionColumn() throws DbException {
        DescriptionField dField;
        if ((screenView.getActionMode() & ScreenView.DISPLAY_CLASS) != 0) {
            String value = null;
            if (semObj != null) {
                DbObject dbo = semObj;
                MetaRelationship parentRel = ((DbListView) screenView).getParentRel();
                if (parentRel != null)
                    dbo = (DbObject) dbo.get(parentRel);
                value = semanticalModel.getClassDisplayText(dbo, null);
            }
            dField = new ImmutableDescriptionField(this, semObj, value, kObjectType);
        } else {
            dField = new ImmutableDescriptionField(this, semObj, null, null);
            dField.setDisplayWidth(15);
        }
        addDescriptionField(dField);
    }

    // Overridden
    protected void createTextAndDescriptionFields() throws DbException {
        boolean bShowPhysicalFields = true;
        DbObject semObj = getDbObject();
        if (semObj != null)
            bShowPhysicalFields = TerminologyUtil
                    .getShowPhysicalConcepts(new DbObject[] { semObj });

        if (columnModel != null) { // ListView row: same fields as header
            int nb = columnModel.getSize();
            ArrayList fieldsToDelete = new ArrayList();
            for (int i = getSize(); i < nb; i++) {
                DescriptionField dfc = columnModel.getDescriptionFieldAt(i);
                if (!(dfc instanceof UDFDescriptionField)) {
                    DescriptionField df = createDescriptionField(null, i);
                    if (df.isEnabled() == false && bShowPhysicalFields == false)
                        fieldsToDelete.add(dfc);
                }
            }
            for (int j = 0; j < fieldsToDelete.size(); j++) {
                Object o = (Object) fieldsToDelete.get(j);
                columnModel.removeDescriptionField((DescriptionField) o);
            }
        } else {
            ArrayList fields = metaClass.getScreenMetaFields();
            ArrayList fieldsToDelete = new ArrayList();
            int nb = fields.size();
            fieldsToDelete = new ArrayList();
            for (int i = 0; i < nb; i++) {
                MetaField field = (MetaField) fields.get(i);
                if (!semanticalModel.isVisibleOnScreen(metaClass, field,
                        (listRelations != null ? null : semObj), null))
                    continue;
                if (listRelations != null && listRelations[0].getOppositeRel(null) == field)
                    continue;

                DescriptionField df = createDescriptionField(field, -1);
                if (df.isEnabled() == false && bShowPhysicalFields == false)
                    fieldsToDelete.add(df);
            }
            for (int j = 0; j < fieldsToDelete.size(); j++) {
                removeDescriptionField((DescriptionField) fieldsToDelete.get(j));
            }
        }
        long i = 0;
    }

    protected DescriptionField createDescriptionField(MetaField field, int fieldIndex)
            throws DbException {
        String editorName;
        Renderer renderer;
        String guiName;

        boolean bIsHideableField = false;

        DescriptionField headField = null;
        if (columnModel != null) { // ListView row: take values from column header
            int size = getSize();
            if (fieldIndex == -1)
                headField = (DescriptionField) columnModel.getDescriptionFieldAt(size);
            else
                headField = (DescriptionField) columnModel.getDescriptionFieldAt(fieldIndex);

            field = ((MetaFieldDescriptionField) headField).getMetaField();

            editorName = headField.getEditorName();
            renderer = headField.getRenderer();
            guiName = semanticalModel.getDisplayText(metaClass, field,
                    ReflectionDescriptionModel.class, true);
        } else { // DescriptionView or ListView header
            editorName = ScreenPlugins.getPluginName(field, parentObj);
            String rendererName = editorName;
            int i = editorName.indexOf(';');
            if (i != -1) {
                rendererName = editorName.substring(0, i);
                editorName = editorName.substring(i + 1);
            }
            renderer = ScreenPlugins.getRenderer(rendererName);
            if (renderer == null)
                renderer = (field instanceof MetaRelationship ? DbSemanticalObjectRenderer.singleton
                        : DefaultRenderer.singleton);
            if (field == nameField) {
                if ((screenView.getActionMode() & ScreenView.FULL_NAME) != 0)
                    renderer = DbSemObjFullNameRenderer.singleton;
                renderer = getNameFieldRenderer(renderer);
            }
            guiName = semanticalModel.getDisplayText(metaClass, field, semObj,
                    ReflectionDescriptionModel.class, true);

        }
        Object value = null;

        try {
            value = (semObj != null ? semObj.get(field) : null);
        } catch (RuntimeException e) {/* do nothing */
        }

        boolean bShow = true;
        if (semObj != null)
            bShow = TerminologyUtil.getShowPhysicalConcepts(new DbObject[] { semObj });

        DescriptionField dField = new MetaFieldDescriptionField(this, field, editorName, renderer,
                value, semObj, guiName);
        if (semObj != null)
            dField.setEditable(semanticalModel.isEditable(field, semObj, null));

        boolean bNotEditable = false;
        if (guiName == null) {
            bNotEditable = true;
            if (bShow == false)
                bIsHideableField = true;
            if (columnModel != null) {
                guiName = semanticalModel.getDisplayText(metaClass, field,
                        ReflectionDescriptionModel.class);
                if (guiName == null)
                    guiName = field.getGUIName();
            } else {
                guiName = semanticalModel.getDisplayText(metaClass, field,
                        ReflectionDescriptionModel.class);
                if (guiName == null)
                    guiName = field.getGUIName();
            }
        }
        dField.setGUIName(guiName);

        if (bNotEditable)
            dField.setEditable(false);

        if (field == nameField) {
            int width = ((screenView.getActionMode() & ScreenView.FULL_NAME) != 0 ? 300 : 180);
            dField.setDisplayWidth(-width); // negative means calculate width from contents
        } else if (field == DbSemanticalObject.fPhysicalName)
            dField.setDisplayWidth(-120); // negative means calculate width from contents

        if (!bIsHideableField)
            addDescriptionField(dField);

        return dField;
    }

    // To override the default renderer for the <name> field.
    protected Renderer getNameFieldRenderer(Renderer renderer) {
        return renderer;
    }

    private void createUDFFields() throws DbException {
        // If adding a new row to a list model, insure to have the same UDFs as in the header;
        // UDFs may have been added or removed after opening of this screen.
        if (columnModel != null) {
            int i = getSize();
            int nb = columnModel.getSize();
            for (; i < nb; i++) {
                DescriptionField df = columnModel.getDescriptionFieldAt(i);
                if (df != null) {
                    if (df instanceof UDFDescriptionField) {
                        UDFDescriptionField dField = (UDFDescriptionField) df;
                        addDescriptionField(new UDFDescriptionField(this, semObj, dField));
                    }
                }
            }
        } else if (project != null) {
            // Some components might use a description model but are not project components
            DbRelationN components = project.getComponents();
            for (int i = 0; i < components.size(); i++) {
                Object comp = components.elementAt(i);
                if (comp instanceof DbUDF && ((DbUDF) comp).getUDFMetaClass() == metaClass) {
                    UDFDescriptionField udfField = new UDFDescriptionField(this, semObj,
                            (DbUDF) comp);
                    if (udfField.isEnabled())
                        addDescriptionField(udfField);
                }
            }
        }
    }

	public MetaClass getMetaClass() {
		return metaClass;
	}
}
