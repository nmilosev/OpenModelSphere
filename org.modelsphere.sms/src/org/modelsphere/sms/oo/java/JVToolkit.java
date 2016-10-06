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

package org.modelsphere.sms.oo.java;

import java.awt.Image;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.tool.LineTool;
import org.modelsphere.jack.graphic.tool.Tool;
import org.modelsphere.jack.srtool.graphic.GraphicComponentFactory;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.SMSToolkit;
import org.modelsphere.sms.graphic.tool.SMSFreeFormTool;
import org.modelsphere.sms.graphic.tool.SMSGraphicalLinkTool;
import org.modelsphere.sms.graphic.tool.SMSNoticeTool;
import org.modelsphere.sms.oo.db.DbOOAdt;
import org.modelsphere.sms.oo.db.DbOOConstructor;
import org.modelsphere.sms.oo.db.DbOODataMember;
import org.modelsphere.sms.oo.db.DbOOInitBlock;
import org.modelsphere.sms.oo.db.DbOOMethod;
import org.modelsphere.sms.oo.db.DbOOPackage;
import org.modelsphere.sms.oo.db.DbOOParameter;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVConstructor;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVInitBlock;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.oo.java.db.DbJVParameter;
import org.modelsphere.sms.oo.java.db.srtypes.JVClassCategory;
import org.modelsphere.sms.oo.java.db.srtypes.JVVisibility;
import org.modelsphere.sms.oo.java.db.util.JavaUtility;
import org.modelsphere.sms.oo.java.graphic.tool.JVAggregationTool;
import org.modelsphere.sms.oo.java.graphic.tool.JVAssociationTool;
import org.modelsphere.sms.oo.java.graphic.tool.JVClassTool;
import org.modelsphere.sms.oo.java.graphic.tool.JVCompositionTool;
import org.modelsphere.sms.oo.java.graphic.tool.JVExceptionTool;
import org.modelsphere.sms.oo.java.graphic.tool.JVInheritanceTool;
import org.modelsphere.sms.oo.java.graphic.tool.JVInterfaceTool;
import org.modelsphere.sms.oo.java.graphic.tool.JVOtherClassifierTool;
import org.modelsphere.sms.oo.java.graphic.tool.JVPackageTool;
import org.modelsphere.sms.oo.java.international.LocaleMgr;

public final class JVToolkit extends SMSToolkit {
    private static final String kClassCreationTool = LocaleMgr.screen
            .getString("ClassCreationTool");
    private static final Image kImageClassCreationTool = GraphicUtil.loadImage(JavaModule.class,
            "db/resources/dbjvclass.gif"); // NOT LOCALIZABLE - tool image
    private static final String kInterfaceCreationTool = LocaleMgr.screen
            .getString("InterfaceCreationTool");
    private static final Image kImageInterfaceCreationTool = GraphicUtil.loadImage(
            JavaModule.class, "db/resources/dbjvinterface.gif"); // NOT LOCALIZABLE - tool image
    private static final String kExceptionCreationTool = LocaleMgr.screen
            .getString("ExceptionCreationTool");
    private static final Image kImageExceptionCreationTool = GraphicUtil.loadImage(
            JavaModule.class, "db/resources/dbjvexception.gif"); // NOT LOCALIZABLE - tool image
    private static final String kPackageCreationTool = LocaleMgr.screen
            .getString("PackageCreationTool");
    private static final Image kImagePackageCreationTool = GraphicUtil.loadImage(JavaModule.class,
            "db/resources/dbjvpackage.gif"); // NOT LOCALIZABLE - tool image
    private static final Image kImageAngularGraphicalLinkTool = GraphicUtil.loadImage(Tool.class,
            "resources/angulardependency.gif"); // NOT LOCALIZABLE - tool image
    private static final Image kImageRightAngleGraphicalLinkTool = GraphicUtil.loadImage(
            Tool.class, "resources/rightangledependency.gif"); // NOT LOCALIZABLE - tool image

    //Constants for graphical links
    private static final String kGraphicalLink = SMSGraphicalLinkTool.GRAPHICAL_LINKS;
    private static final String[] kGraphicalLinkTooltips = null; //new String[] {};
    private static final Image[] kGraphicalLinkSecondaryImages = null; //new Image[] {}; 

    public GraphicComponentFactory createGraphicalComponentFactory() {
        return new JVGraphicComponentFactory();
    }

    protected MetaClass[] getSupportedPackage() {
        return new MetaClass[] { DbJVClassModel.metaClass, DbJVPackage.metaClass };
    }

    public DbObject createDbObject(MetaClass abstractMetaClass, DbObject composite,
            Object[] parameters) throws DbException {
        DbObject dbObject = null;

        if (composite != null) {
            if (abstractMetaClass.equals(DbOOPackage.metaClass)) {
                dbObject = composite.createComponent(DbJVPackage.metaClass);
            } else if (abstractMetaClass.equals(DbOOConstructor.metaClass)) {
                dbObject = composite.createComponent(DbJVConstructor.metaClass);
            } else if (abstractMetaClass.equals(DbOODataMember.metaClass)) {
                dbObject = composite.createComponent(DbJVDataMember.metaClass);
                initField((DbJVDataMember) dbObject, (DbJVClass) composite);
            } else if (abstractMetaClass.equals(DbOOInitBlock.metaClass)) {
                dbObject = composite.createComponent(DbJVInitBlock.metaClass);
            } else if (abstractMetaClass.equals(DbOOMethod.metaClass)) {
                dbObject = composite.createComponent(DbJVMethod.metaClass);
            } else if (abstractMetaClass.equals(DbOOParameter.metaClass)) {
                dbObject = composite.createComponent(DbJVParameter.metaClass);
                initParameter((DbOOParameter) dbObject, (DbOOMethod) composite);
            } //end if
        } //end if 

        return dbObject;
    } //end createDbObject()

    // Does not apply to the object model (java only)
    public DbObject createDbObject(DbObject baseobject, DbObject composite, Object[] parameters)
            throws DbException {
        return null;
    }

    public boolean isMetaClassSupported(MetaClass metaClass) {
        return (isAssignableFrom(DbOOPackage.metaClass, metaClass)
                || isAssignableFrom(DbOOConstructor.metaClass, metaClass)
                || isAssignableFrom(DbOODataMember.metaClass, metaClass)
                || isAssignableFrom(DbOOInitBlock.metaClass, metaClass)
                || isAssignableFrom(DbOOMethod.metaClass, metaClass) || isAssignableFrom(
                DbOOParameter.metaClass, metaClass));
    }

    public Tool[] createTools() {
        return new Tool[] {
                new JVClassTool(kClassCreationTool, kImageClassCreationTool),
                new JVInterfaceTool(kInterfaceCreationTool, kImageInterfaceCreationTool),
                new JVOtherClassifierTool(),

                //new JVExceptionTool(kExceptionCreationTool,kImageExceptionCreationTool),
                new JVPackageTool(kPackageCreationTool, kImagePackageCreationTool),
                new JVAssociationTool(JVAssociationTool.kAssociationCreationTool, new String[] {
                        LineTool.kRightAngleTooltips, LineTool.kAngularLineTooltips },
                        JVAssociationTool.kImageRightAngleAssociationCreationTool, new Image[] {
                                JVAssociationTool.kImageRightAngleAssociationCreationTool,
                                JVAssociationTool.kImageAssociationCreationTool }),
                new JVAggregationTool(JVAggregationTool.kAggregationCreationTool, new String[] {
                        LineTool.kRightAngleTooltips, LineTool.kAngularLineTooltips },
                        JVAggregationTool.kImageAggregationCreationTool, new Image[] {
                                JVAggregationTool.kImageRightAngleAggregationCreationTool,
                                JVAggregationTool.kImageAggregationCreationTool }),
                new JVCompositionTool(JVCompositionTool.kCompositionCreationTool, new String[] {
                        LineTool.kRightAngleTooltips, LineTool.kAngularLineTooltips },
                        JVCompositionTool.kImageCompositionCreationTool, new Image[] {
                                JVCompositionTool.kImageRightAngleCompositionCreationTool,
                                JVCompositionTool.kImageCompositionCreationTool }),
                new JVInheritanceTool(JVInheritanceTool.kInheritanceCreationTool, new String[] {
                        LineTool.kRightAngleTooltips, LineTool.kAngularLineTooltips },
                        JVInheritanceTool.kImageInheritanceCreationTool, new Image[] {
                                JVInheritanceTool.kImageRightAngleInheritanceCreationTool,
                                JVInheritanceTool.kImageInheritanceCreationTool }),
                new SMSNoticeTool(),
                new SMSGraphicalLinkTool(kGraphicalLink, new String[] { kGraphicalLink,
                        kGraphicalLink }, kImageAngularGraphicalLinkTool, new Image[] {
                        kImageRightAngleGraphicalLinkTool, kImageAngularGraphicalLinkTool }), };
    } //end createTools()

    //
    // private methods
    //
    private void initField(DbJVDataMember field, DbJVClass composite) throws DbException {
        JVClassCategory category = composite.getStereotype();

        if (category.getValue() == JVClassCategory.INTERFACE) {
            field.setStatic(Boolean.TRUE);
            field.setFinal(Boolean.TRUE);
            field.setVisibility(JVVisibility.getInstance(JVVisibility.DEFAULT));
        } else {
            field.setVisibility(JVVisibility.getInstance(JVVisibility.PRIVATE));
        } //end if

        //integer as the default value
        DbJVClassModel classModel = (DbJVClassModel) field
                .getCompositeOfType(DbJVClassModel.metaClass);
        DbOOAdt intType = JavaUtility.getSingleton().findBuiltinType(classModel, "int");
        field.setType(intType);
    } //end initField() 

    private void initParameter(DbOOParameter param, DbOOMethod method) throws DbException {
        //integer as the default value
        DbJVClassModel classModel = (DbJVClassModel) param
                .getCompositeOfType(DbJVClassModel.metaClass);
        DbOOAdt intType = JavaUtility.getSingleton().findBuiltinType(classModel, "int");
        param.setType(intType);
    }

}
