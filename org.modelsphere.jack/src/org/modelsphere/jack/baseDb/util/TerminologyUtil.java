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

package org.modelsphere.jack.baseDb.util;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.screen.DbTreeLookupDialog;
import org.modelsphere.jack.baseDb.screen.model.DbTreeModel;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesProvider;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;

public abstract class TerminologyUtil implements PropertiesProvider {

    public static final String DISPLAY_RELATIONAL_ATTRIBUTES_FOR_ER_MODELS_KEY = "DisplayRelationalAttributes"; // NOT LOCALIZABLE, property key
    public static final Boolean DISPLAY_RELATIONAL_ATTRIBUTES_FOR_ER_MODELS_DEFAULT = Boolean.FALSE;

    public void updateProperties() {
        PropertiesSet appPref = PropertiesManager.APPLICATION_PROPERTIES_SET;
        appPref.removeProperty(TerminologyUtil.class,
                DISPLAY_RELATIONAL_ATTRIBUTES_FOR_ER_MODELS_KEY);
        appPref.setProperty(TerminologyUtil.class, DISPLAY_RELATIONAL_ATTRIBUTES_FOR_ER_MODELS_KEY,
                Boolean.toString(true));
    }

    // Singleton
    protected static TerminologyUtil g_singleInstance = null;

    public static TerminologyUtil getInstance() {
        return g_singleInstance;
    } // getSingleInstance()

    public static int LOGICAL_MODE_ENTITY_RELATIONSHIP = 1;
    public static int LOGICAL_MODE_OBJECT_RELATIONAL = 2;
    public static int PROCESS_MODELING = 3;

    // //
    // we need a static method to return the color of physical fields

    public static Color getMarkedMetaFieldsFontColor() {
        return Color.BLACK;
    }

    public static Color getMarkedMetaFieldsHeaderFontColor() {
        return Color.WHITE;
    }

    // //
    // we need a static method to return the color of physical fields

    public static Color getMarkedMetaFieldsBackColor() {
        return new Color(192, 192, 192);
    }

    // //
    // we need another static method to determine the display preference for
    // metafields

    private static boolean getDisplayMarkedMetaFields() {

        // a flag should be set in the data model to indicate whether
        // the model was already converted to a conceptual model
        // if the flag is set, the physical concept are shown in the ER mode
        // however, a value set in the preferences can override this behavior,
        // by specifying that the physical concepts and fields should be hidden
        // always in ER mode

        PropertiesSet applOptions = PropertiesManager.APPLICATION_PROPERTIES_SET;
        boolean displayRelationalAttributes = applOptions.getPropertyBoolean(TerminologyUtil.class,
                DISPLAY_RELATIONAL_ATTRIBUTES_FOR_ER_MODELS_KEY,
                DISPLAY_RELATIONAL_ATTRIBUTES_FOR_ER_MODELS_DEFAULT).booleanValue();

        return displayRelationalAttributes;

    }

    public static boolean getShowPhysicalConcepts(DbObject[] dbos) {
        boolean containsConvertedModel = false;
        int nMode = TerminologyUtil.LOGICAL_MODE_OBJECT_RELATIONAL;

        TerminologyUtil DMUtil = getInstance();

        // find out if the parent model was previously converted
        for (int idx = 0; idx < dbos.length; idx++) {
            DbObject obj = dbos[idx];
            if (DMUtil.isDataModel(obj) == true) {
                nMode = DMUtil.getModelLogicalMode(obj);
                if (nMode == LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                    if (DMUtil.isConvertedDataModel(obj)) {
                        containsConvertedModel = true;
                        break;
                    }
                }
            } else {
                DbObject parentModel = DMUtil.isCompositeDataModel(obj);
                if (parentModel != null) {
                    nMode = DMUtil.getModelLogicalMode(parentModel);
                    if (nMode == LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                        if (DMUtil.isConvertedDataModel(parentModel)) {
                            containsConvertedModel = true;
                            break;
                        }
                    }
                }
            }
        }
        if (nMode == LOGICAL_MODE_ENTITY_RELATIONSHIP) {
            boolean bShowMarkedFields = containsConvertedModel;
            if (containsConvertedModel == true) {
                bShowMarkedFields = getDisplayMarkedMetaFields();
            }
            return bShowMarkedFields;
        }
        return true;
    }

    abstract public Terminology findModelTerminology(DbObject orModel) throws DbException;

    abstract public int validateSelectionModel();

    abstract public int getModelLogicalMode(DbObject dbObject);

    abstract public boolean isDataModel(DbObject dbObject);

    abstract public DbObject isCompositeDataModel(DbObject dbObject);

    abstract public boolean isConvertedDataModel(DbObject dbObject);

    abstract public Icon getConceptualModelIcon();

    abstract public Icon getDiagramIcon(ApplicationDiagram diagram) throws DbException;

    abstract public Icon getArcIcon();

    abstract public Icon getAssociationIcon();

    abstract public boolean isObjectEntityOrAssociation(DbObject dbObject);

    abstract public boolean isObjectAssociation(DbObject dbObject);

    abstract public boolean isObjectArc(DbObject dbObject);

    abstract public boolean isObjectArcEndRole(DbObject dbObjec);

    abstract public boolean isObjectRole(DbObject dbObject);

    abstract public boolean isObjectLine(DbObject dbObject);

    abstract public boolean isObjectUseCaseOrBEModel(DbObject dbObject);

    abstract public boolean isPureERSet(DbObject[] dbos);

    abstract public Icon getUseCaseIcon(DbObject object) throws DbException;

    abstract public boolean isObjectUseCase(DbObject dbObject);

    abstract public boolean isObjectUseCaseContext(DbObject dbObject) throws DbException;

    abstract public DbTreeLookupDialog getTreeLookupDialog(Component comp, String title,
            DbTreeModel model, boolean multipleSelection, boolean isUML);

    abstract public boolean isUML(DbObject dbObject) throws DbException;

    abstract public boolean isClassModel(DbObject dbObject) throws DbException;

    abstract public boolean isObjectDiagram(DbObject dbObject) throws DbException;
}
