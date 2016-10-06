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

package org.modelsphere.sms.or.generic;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.tool.LineTool;
import org.modelsphere.jack.graphic.tool.Tool;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.CurrentFocusListener;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.GraphicComponentFactory;
import org.modelsphere.sms.SMSToolkit;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.graphic.tool.SMSGraphicalLinkTool;
import org.modelsphere.sms.graphic.tool.SMSNoticeTool;
import org.modelsphere.sms.or.ORGraphicComponentFactory;
import org.modelsphere.sms.or.db.DbORCheck;
import org.modelsphere.sms.or.db.DbORChoiceOrSpecialization;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTrigger;
import org.modelsphere.sms.or.db.DbORView;
import org.modelsphere.sms.or.db.srtypes.ORChoiceSpecializationCategory;
import org.modelsphere.sms.or.generic.db.DbGECheck;
import org.modelsphere.sms.or.generic.db.DbGEColumn;
import org.modelsphere.sms.or.generic.db.DbGEDataModel;
import org.modelsphere.sms.or.generic.db.DbGEIndex;
import org.modelsphere.sms.or.generic.db.DbGEPrimaryUnique;
import org.modelsphere.sms.or.generic.db.DbGETable;
import org.modelsphere.sms.or.generic.db.DbGETrigger;
import org.modelsphere.sms.or.generic.db.DbGEView;
import org.modelsphere.sms.or.graphic.tool.ORAssociationTool;
import org.modelsphere.sms.or.graphic.tool.ORKeyTool;
import org.modelsphere.sms.or.graphic.tool.ORSpecializationTool;
import org.modelsphere.sms.or.graphic.tool.ORSubModelTool;
import org.modelsphere.sms.or.graphic.tool.ORTableTool;
import org.modelsphere.sms.or.graphic.tool.ORViewTool;

public class GEDataModelToolkit extends SMSToolkit implements CurrentFocusListener {
    private static final String kGraphicalLink = SMSGraphicalLinkTool.GRAPHICAL_LINKS;
    private static final Image kImageAngularGraphicalLinkTool = GraphicUtil.loadImage(Tool.class,
            "resources/angulardependency.gif"); // NOT
    // LOCALIZABLE
    // -
    // tool
    // image
    private static final Image kImageRightAngleGraphicalLinkTool = GraphicUtil.loadImage(
            Tool.class, "resources/rightangledependency.gif"); // NOT

    // LOCALIZABLE
    // -
    // tool
    // image

    public GEDataModelToolkit() {

        ApplicationContext.getFocusManager().addCurrentFocusListener(this);

    }

    public GraphicComponentFactory createGraphicalComponentFactory() {
        return new ORGraphicComponentFactory();
    }

    protected MetaClass[] getSupportedPackage() {
        return new MetaClass[] { DbGEDataModel.metaClass };
    }

    public DbObject createDbObject(MetaClass abstractMetaClass, DbObject composite,
            Object[] parameters) throws DbException {
        if (composite != null) {
            if (abstractMetaClass.equals(DbORView.metaClass))
                return composite.createComponent(DbGEView.metaClass);
            else if (abstractMetaClass.equals(DbORTable.metaClass))
                return composite.createComponent(DbGETable.metaClass);
            else if (abstractMetaClass.equals(DbORChoiceOrSpecialization.metaClass)) 
            	return composite.createComponent(DbORChoiceOrSpecialization.metaClass);
            else if (abstractMetaClass.equals(DbORTrigger.metaClass))
                return composite.createComponent(DbGETrigger.metaClass);
            else if (abstractMetaClass.equals(DbORColumn.metaClass))
                return composite.createComponent(DbGEColumn.metaClass);
            else if (abstractMetaClass.equals(DbORCheck.metaClass))
                return composite.createComponent(DbGECheck.metaClass);
            else if (abstractMetaClass.equals(DbORPrimaryUnique.metaClass))
                return composite.createComponent(DbGEPrimaryUnique.metaClass);
            else if (abstractMetaClass.equals(DbORIndex.metaClass))
                return composite.createComponent(DbGEIndex.metaClass);
            else if (abstractMetaClass.equals(DbORDataModel.metaClass)) {
                if (composite instanceof DbORDataModel) {
                    DbORDataModel dataModel = (DbORDataModel) composite;
                    return new DbGEDataModel(composite, dataModel.getTargetSystem(), dataModel
                            .getOperationalMode());
                } else
                    return composite.createComponent(DbGEDataModel.metaClass, parameters,
                            new Class[] { DbSMSTargetSystem.class });
            }
        }
        return null;
    }

    public DbObject createDbObject(DbObject baseobject, DbObject composite, Object[] parameters)
            throws DbException {
        return null;
    }

    public boolean isMetaClassSupported(MetaClass metaClass) {
        return (isAssignableFrom(DbORView.metaClass, metaClass)
                || isAssignableFrom(DbORTable.metaClass, metaClass)
                || isAssignableFrom(DbORTrigger.metaClass, metaClass)
                || isAssignableFrom(DbORColumn.metaClass, metaClass)
                || isAssignableFrom(DbORCheck.metaClass, metaClass)
                || isAssignableFrom(DbORPrimaryUnique.metaClass, metaClass) || isAssignableFrom(
                DbORIndex.metaClass, metaClass));
    }

    // /////////////////////////////////////
    // CurrentFocusListener support
    //
    public void currentFocusChanged(Object oldFocusObject, Object focusObject) throws DbException {
        if (focusObject instanceof ApplicationDiagram) {
            ApplicationDiagram appDiag = (ApplicationDiagram) focusObject;
            DiagramView dgmView = appDiag.getMainView();
            DbObject dbo = ((ApplicationDiagram) focusObject).getSemanticalObject();
            if (dbo instanceof DbORDataModel) {
                DbORDataModel dataModel = (DbORDataModel) dbo;
                int mode = dataModel.getOperationalMode();
                Tool[] tools = dgmView.getTools();
                if (mode == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                    for (int i = 0; i < tools.length; i++) {
                        if (tools[i] instanceof ORTableTool)
                            tools[i].setText(ORTableTool.kEntityCreationTool);
                        if (tools[i] instanceof ORAssociationTool)
                            tools[i].setText(ORAssociationTool.kRelationshipCreationTool);
                        if (tools[i] instanceof ORSubModelTool)
                            tools[i].setImage(ORSubModelTool.kConceptualSubModels);
                        if (tools[i] instanceof ORViewTool)
                            dgmView.getToolGroup().setEnabled(i, false);
                    }
                } else {
                    for (int i = 0; i < tools.length; i++) {
                        if (tools[i] instanceof ORTableTool)
                            tools[i].setText(ORTableTool.kTableCreationTool);
                        if (tools[i] instanceof ORAssociationTool)
                            tools[i].setText(ORAssociationTool.kAssociationCreationTool);
                        if (tools[i] instanceof ORSubModelTool)
                            tools[i].setImage(ORSubModelTool.kRelationalSubModels);
                        if (tools[i] instanceof ORViewTool)
                            dgmView.getToolGroup().setEnabled(i, true);
                    }
                }
                dgmView.setTools(tools);
                dgmView.getToolGroup().currentFocusChanged(oldFocusObject, focusObject);
            }
        }

    }

    //
    // End of CurrentFocusListener support
    // /////////////////////////////////////

    public Tool[] createTools() {
        List<Tool> tools = new ArrayList<Tool>(); 
        
        tools.add(new ORTableTool(ORTableTool.kTableCreationTool, ORTableTool.kImageTableCreationTool));
        tools.add(new ORViewTool(ORViewTool.kViewCreationTool, ORViewTool.kImageViewCreationTool));
        
        tools.add(new ORSpecializationTool(ORSpecializationTool.kSpecializationCreationTool,
                ORSpecializationTool.kImageSpecializationCreationTool,
                ORChoiceSpecializationCategory.SPECIALIZATION));
        
        tools.add(new ORSpecializationTool(ORSpecializationTool.kChoiceCreationTool,
                ORSpecializationTool.kImageChoiceCreationTool,
                ORChoiceSpecializationCategory.CHOICE));

        tools.add(new ORAssociationTool(ORAssociationTool.kAssociationCreationTool, new String[] {
                LineTool.kRightAngleTooltips, LineTool.kAngularLineTooltips },
                ORAssociationTool.kImageAssociationRightAngleCreationTool, new Image[] {
                        ORAssociationTool.kImageAssociationRightAngleCreationTool,
                        ORAssociationTool.kImageAssociationCreationTool }));
        
        tools.add(new ORKeyTool(ORKeyTool.kKeyCreationTool, ORKeyTool.kImageKeyTool));
        //tools.add(new ORKeyTool(ORKeyTool.kKeyCreationTool, ORKeyTool.kImageKeyTool));
        tools.add(new ORSubModelTool(ORSubModelTool.kRelationalSubModels));
        tools.add(new SMSNoticeTool());
        tools.add(new SMSGraphicalLinkTool(kGraphicalLink, new String[] { kGraphicalLink,
                kGraphicalLink }, kImageAngularGraphicalLinkTool, new Image[] {
                kImageRightAngleGraphicalLinkTool, kImageAngularGraphicalLinkTool }));
  
        List<Tool> visibleTools = new ArrayList<Tool>(); 
        for (Tool tool : tools) {
            if (tool.isVisible()) {
                visibleTools.add(tool);
            }
        } //end for
        
        Tool[] array = new Tool[visibleTools.size()];
        visibleTools.toArray(array);
        return array;
    } // end createTools()

} // end GEDataModelToolkit
