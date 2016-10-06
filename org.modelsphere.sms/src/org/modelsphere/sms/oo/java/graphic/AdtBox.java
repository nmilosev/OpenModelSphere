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

package org.modelsphere.sms.oo.java.graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.JComponent;

import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbRefreshPassListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.db.srtypes.DbtPrefix;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.Line;
import org.modelsphere.jack.graphic.ZoneBox;
import org.modelsphere.jack.graphic.shape.GraphicShape;
import org.modelsphere.jack.graphic.shape.RectangleShape;
import org.modelsphere.jack.graphic.shape.ShadowRectShape;
import org.modelsphere.jack.graphic.zone.CellEditor;
import org.modelsphere.jack.graphic.zone.CellID;
import org.modelsphere.jack.graphic.zone.CellRenderer;
import org.modelsphere.jack.graphic.zone.CellRenderingOption;
import org.modelsphere.jack.graphic.zone.ColumnCellsOption;
import org.modelsphere.jack.graphic.zone.CompositeCellRenderer;
import org.modelsphere.jack.graphic.zone.CompositeCellRenderer.CompositeCellRendererElement;
import org.modelsphere.jack.graphic.zone.ImageCellRenderer;
import org.modelsphere.jack.graphic.zone.MatrixZone;
import org.modelsphere.jack.graphic.zone.StringCellRenderer;
import org.modelsphere.jack.graphic.zone.ZoneCell;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DbTextFieldCellEditor;
import org.modelsphere.jack.srtool.graphic.DomainCellEditor;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.jack.util.SrVector;
import org.modelsphere.sms.db.DbSMSClassifier;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSFeature;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSStructuralFeature;
import org.modelsphere.sms.db.DbSMSStyle;
import org.modelsphere.sms.db.DbSMSUmlConstraint;
import org.modelsphere.sms.db.util.Extensibility;
import org.modelsphere.sms.features.DisplayRecentModifications;
import org.modelsphere.sms.graphic.SMSClassifierBox;
import org.modelsphere.sms.graphic.SMSStereotypeEditor;
import org.modelsphere.sms.oo.db.DbOOAbstractMethod;
import org.modelsphere.sms.oo.db.DbOOAdt;
import org.modelsphere.sms.oo.db.DbOOAdtGo;
import org.modelsphere.sms.oo.db.DbOOAssociationEnd;
import org.modelsphere.sms.oo.db.DbOOClass;
import org.modelsphere.sms.oo.db.DbOOConstructor;
import org.modelsphere.sms.oo.db.DbOODataMember;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.db.DbOOMethod;
import org.modelsphere.sms.oo.db.DbOOParameter;
import org.modelsphere.sms.oo.db.DbOOStyle;
import org.modelsphere.sms.oo.db.srtypes.OOVisibility;
import org.modelsphere.sms.oo.db.util.OoUtilities;
import org.modelsphere.sms.oo.graphic.OOAssociation;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVConstructor;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVInheritance;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.DbJVParameter;
import org.modelsphere.sms.oo.java.db.srtypes.JVClassCategory;
import org.modelsphere.sms.oo.java.db.srtypes.JVVisibility;
import org.modelsphere.sms.oo.java.db.util.AnyAdt;
import org.modelsphere.sms.oo.java.db.util.AnyAdt.Method;
import org.modelsphere.sms.oo.java.db.util.AnyAdt.MethodsWithSameSignature;
import org.modelsphere.sms.oo.java.international.LocaleMgr;
import org.modelsphere.sms.style.PrefixOptionComponent;

public final class AdtBox extends GraphicNode implements ActionInformation, SMSClassifierBox {
    // zone columns index , WARNING if the values change you must also change
    // the
    // addColumn order in setZones
    // the adt prefix
    private static final int ADT_PREFIX_IDX = 0;
    // the adt name
    private static final int ADT_NAME_IDX = 1;
    // the field & inherited field prefix
    private static final int ADT_DUPLICATE_IDX = 2;

    // Field indexes
    private static final int FIELD_PREFIX_IDX = 0;
    private static final int FIELD_UMLSTEREOTYPE_IDX = 1;

    private int getFieldNameIndex() throws DbException {
        return isUMLOrder() ? 2 : 3;
    }

    private int getFieldTypeIndex() throws DbException {
        return isUMLOrder() ? 3 : 2;
    }

    private static final int FIELD_UMLCONSTRAINTS_IDX = 4;

    // Inherited field indexes
    private static final int INHERITED_FIELD_PREFIX_IDX = 0;

    private int getInheritedFieldNamePrefix() throws DbException {
        return isUMLOrder() ? 1 : 2;
    }

    private int getInheritedFieldTypePrefix() throws DbException {
        return isUMLOrder() ? 2 : 1;
    }

    // Method indexes
    private static final int METHOD_PREFIX_IDX = 0;
    private static final int METHOD_UMLSTEREOTYPE_IDX = 1;

    private int getMethodNameIndex() throws DbException {
        return isUMLOrder() ? 2 : 3;
    }

    private int getMethodTypeIndex() throws DbException {
        return isUMLOrder() ? 3 : 2;
    }

    private static final int METHOD_UMLCONSTRAINTS_IDX = 4;

    // Inherited field indexes
    private static final int INHERITED_METHOD_PREFIX_IDX = 0;

    private int getInheritedMethodNamePrefix() throws DbException {
        return isUMLOrder() ? 1 : 2;
    }

    private int getInheritedMethodTypePrefix() throws DbException {
        return isUMLOrder() ? 2 : 1;
    }

    // the Inner Class & inherited Inner Class prefix
    private static final int INNER_CLASS_PREFIX_IDX = 0;
    // the Inner Class & inherited InnerClass name
    private static final int INNER_CLASS_NAME_IDX = 1;

    private static final String ADT_STEREO_ZONE_ID = "Adt Stereotype"; // NOT
    // LOCALIZABLE
    private static final String ADT_NAME_ZONE_ID = "Adt Name"; // NOT
    // LOCALIZABLE
    private static final String ADT_INH_FIELD_ZONE_ID = "Adt Inherited Attributes"; // NOT
    // LOCALIZABLE
    private static final String ADT_FIELD_ZONE_ID = "Adt Attributes"; // NOT
    // LOCALIZABLE
    private static final String ADT_INH_METHOD_ZONE_ID = "Adt Inherited Methods"; // NOT
    // LOCALIZABLE
    private static final String ADT_METHOD_ZONE_ID = "Adt Methods"; // NOT
    // LOCALIZABLE
    private static final String ADT_INH_INNER_CLASS_ID = "Adt Inherited Inner Classes"; // NOT
    // LOCALIZABLE
    private static final String ADT_INNER_CLASS_ID = "Adt Inner Classes"; // NOT
    // LOCALIZABLE
    private static final String ADT_CONSTRAINT_CLASS_ID = "Adt Constraints"; // NOT
    // LOCALIZABLE

    private static final Color DIAGRAM_BACKGROUND = Color.white; // We can take
    // for
    // granted
    // that the
    // diagram
    // background
    // is always
    // white
    // TODO: set the interface b/g transparent. = new Color(0, 0, 0, 0);
    // Can be done only if association links are drawn correctly, ie their ends
    // terminate at the box border [MS]

    private static final BoxRefreshTg boxRefreshTg = new BoxRefreshTg();

    static {
        Db.addDbRefreshPassListener(boxRefreshTg);
        MetaField.addDbRefreshListener(boxRefreshTg, null, new MetaField[] {
                DbObject.fComponents,
                DbSemanticalObject.fName,
                DbSMSSemanticalObject.fUmlConstraints,
                DbSMSSemanticalObject.fUmlStereotype,
                // DbSMSProject.fRecentModificationDisplay,
                DbOOClass.fAbstract, DbJVClass.fVisibility, DbJVClass.fStatic,
                DbSMSClassifier.fClassifierGos, DbJVClass.fFinal, DbJVClass.fStereotype,
                DbOODataMember.fAssociationEnd, DbOOAssociationEnd.fNavigable,
                DbJVDataMember.fTransient, DbOODataMember.fVisibility, DbJVDataMember.fStatic,
                DbSMSFeature.fOverriddenFeatures, DbOODataMember.fType,
                DbOODataMember.fElementType, DbOODataMember.fTypeUse, DbOODataMember.fTypeUseStyle,
                DbJVDataMember.fFinal, DbOOMethod.fAbstract, DbOOMethod.fTypeUse,
                DbOOAbstractMethod.fVisibility, DbOOMethod.fReturnType,
                DbOOMethod.fReturnElementType, DbJVMethod.fStatic, DbJVMethod.fFinal,
                DbJVParameter.fType, DbJVParameter.fTypeUse, DbJVParameter.fTypeUseStyle,
                DbJVParameter.fElementType, DbSMSSemanticalObject.fSourceLinks,
                DbSMSSemanticalObject.fTargetLinks, DbSMSSemanticalObject.fModificationTime, });
    }

    // the db reference
    protected DbOOAdtGo adtGO;
    protected DbJVClass adtSO;

    // the zones
    private static final int MINIMAL_HEIGHT = 16;
    transient private MatrixZone stereotypeZone = new MatrixZone(ADT_STEREO_ZONE_ID,
            GraphicUtil.CENTER_ALIGNMENT);
    transient private MatrixZone adtZone = new MatrixZone(ADT_NAME_ZONE_ID,
            GraphicUtil.CENTER_ALIGNMENT);
    transient private MatrixZone inheritedFieldZone = new MatrixZone(ADT_INH_FIELD_ZONE_ID);
    transient private MatrixZone fieldZone = new MatrixZone(ADT_FIELD_ZONE_ID,
            GraphicUtil.LEFT_ALIGNMENT, MINIMAL_HEIGHT);
    transient private MatrixZone inheritedMethodZone = new MatrixZone(ADT_INH_METHOD_ZONE_ID);
    transient private MatrixZone methodZone = new MatrixZone(ADT_METHOD_ZONE_ID,
            GraphicUtil.LEFT_ALIGNMENT, MINIMAL_HEIGHT);
    transient private MatrixZone inheritedInnerClassZone = new MatrixZone(ADT_INH_INNER_CLASS_ID);
    transient private MatrixZone innerClassZone = new MatrixZone(ADT_INNER_CLASS_ID);
    transient private MatrixZone constraintZone = new MatrixZone(ADT_CONSTRAINT_CLASS_ID);

    // listeners
    transient private AdtGoRefreshTg adtGoRefreshTg = new AdtGoRefreshTg();

    // rendering options
    transient private CellRenderingOption stereotypeNameCRO;
    transient private CellRenderingOption stereotypeImageCRO;
    transient private CellRenderingOption classPrefixCRO;
    transient private CellRenderingOption classNameCRO;
    transient private CellRenderingOption duplicateCRO = new CellRenderingOption(
            new StringCellRenderer(), new Font("SansSerif", Font.PLAIN, 8), // NOT
            // LOCALIZABLE
            GraphicUtil.RIGHT_ALIGNMENT);
    transient private CellRenderingOption abstractClassPrefixCRO;
    transient private CellRenderingOption abstractClassNameCRO;
    transient private CellRenderingOption interfacePrefixCRO;
    transient private CellRenderingOption interfaceNameCRO;
    transient private CellRenderingOption exceptionPrefixCRO;
    transient private CellRenderingOption exceptionNameCRO;
    transient private CellRenderingOption fieldNameCRO;
    transient private CellRenderingOption methodNameCRO;
    transient private CellRenderingOption innerClassNameCRO;
    transient private CellRenderingOption fieldPrefixCRO;
    transient private CellRenderingOption fieldUMLStereotypeCRO;
    transient private CellRenderingOption fieldUMLConstraintsCRO;
    transient private CellRenderingOption methodPrefixCRO;
    transient private CellRenderingOption methodUMLStereotypeCRO;
    transient private CellRenderingOption methodUMLConstraintsCRO;
    transient private CellRenderingOption innerClassPrefixCRO;
    transient private CellRenderingOption inheritedFieldNameCRO;
    transient private CellRenderingOption inheritedMethodNameCRO;
    transient private CellRenderingOption inheritedInnerClassNameCRO;
    transient private CellRenderingOption inheritedFieldPrefixCRO;
    transient private CellRenderingOption inheritedMethodPrefixCRO;
    transient private CellRenderingOption inheritedInnerClassPrefixCRO;
    transient private CellRenderingOption constraintNameCRO;
    transient private ColumnCellsOption classNameCellsOption;
    transient private ColumnCellsOption interfaceNameCellsOption;
    transient private ColumnCellsOption abstractClassNameCellsOption;
    transient private ColumnCellsOption exceptionNameCellsOption;
    transient private ColumnCellsOption abstractExceptionNameCellsOption;

    public AdtBox(Diagram diag, DbOOAdtGo newAdtGO) throws DbException {
        super(diag, RectangleShape.singleton);
        adtGO = newAdtGO;
        adtSO = (DbJVClass) adtGO.getClassifier();
        setAutoFit(adtGO.isAutoFit());
        setRectangle(adtGO.getRectangle());
        objectInit();
    }

    protected void objectInit() throws DbException {
        adtGO.setGraphicPeer(this);
        adtGO.addDbRefreshListener(adtGoRefreshTg);
        if (adtSO.isInterface()) {
            fieldZone = new MatrixZone(ADT_FIELD_ZONE_ID, GraphicUtil.LEFT_ALIGNMENT); // field zone not displayed by
            // default
        }

        initShape();
        initCellRenderingOptions();
        setZones();
        populateContents();
    }

    private void initCellRenderingOptions() throws DbException {
        Color colorText = (Color) (adtGO.find(DbSMSClassifierGo.fTextColor));

        duplicateCRO.setColor(colorText);
        stereotypeNameCRO = getCellRenderingOptionForConcept(stereotypeNameCRO,
                DbOOStyle.fOojv_stereotypeNameFont, null, colorText);
        // stereotypeNameCRO.setAlignment(GraphicUtil.CENTER_ALIGNMENT); //ZONE
        // Center by default [LL]

        stereotypeImageCRO = getCellRenderingOptionForConcept(stereotypeImageCRO,
                DbOOStyle.fOojv_stereotypeNameFont, new CompositeCellRenderer(), colorText);
        stereotypeImageCRO.setAlignment(GraphicUtil.RIGHT_ALIGNMENT);

        classNameCRO = getCellRenderingOptionForConcept(classNameCRO,
                DbOOStyle.fOojv_classNameFont, null, colorText);
        classNameCRO.setAlignment(GraphicUtil.CENTER_ALIGNMENT);

        abstractClassNameCRO = getCellRenderingOptionForConcept(abstractClassNameCRO,
                DbOOStyle.fOojv_abstractClassNameFont, null, colorText);
        abstractClassNameCRO.setAlignment(GraphicUtil.CENTER_ALIGNMENT);

        interfaceNameCRO = getCellRenderingOptionForConcept(interfaceNameCRO,
                DbOOStyle.fOojv_interfaceNameFont, null, colorText);
        interfaceNameCRO.setAlignment(GraphicUtil.CENTER_ALIGNMENT);

        exceptionNameCRO = getCellRenderingOptionForConcept(exceptionNameCRO,
                DbOOStyle.fOojv_exceptionNameFont, null, colorText);
        exceptionNameCRO.setAlignment(GraphicUtil.CENTER_ALIGNMENT);

        fieldNameCRO = getCellRenderingOptionForConcept(fieldNameCRO, DbOOStyle.fOojv_fieldFont,
                null, colorText);
        fieldUMLStereotypeCRO = getCellRenderingOptionForConcept(fieldUMLStereotypeCRO,
                DbOOStyle.fOojv_fieldFont, null, colorText);
        fieldUMLConstraintsCRO = getCellRenderingOptionForConcept(fieldUMLConstraintsCRO,
                DbOOStyle.fOojv_fieldFont, null, colorText);
        methodNameCRO = getCellRenderingOptionForConcept(methodNameCRO, DbOOStyle.fOojv_methodFont,
                null, colorText);
        methodUMLStereotypeCRO = getCellRenderingOptionForConcept(methodUMLStereotypeCRO,
                DbOOStyle.fOojv_methodFont, null, colorText);
        methodUMLConstraintsCRO = getCellRenderingOptionForConcept(methodUMLConstraintsCRO,
                DbOOStyle.fOojv_methodFont, null, colorText);
        innerClassNameCRO = getCellRenderingOptionForConcept(innerClassNameCRO,
                DbOOStyle.fOojv_innerClassFont, null, colorText);
        inheritedFieldNameCRO = getCellRenderingOptionForConcept(inheritedFieldNameCRO,
                DbOOStyle.fOojv_inheritedMemberFont, null, colorText);
        inheritedMethodNameCRO = getCellRenderingOptionForConcept(inheritedMethodNameCRO,
                DbOOStyle.fOojv_inheritedMemberFont, null, colorText);
        inheritedInnerClassNameCRO = getCellRenderingOptionForConcept(inheritedInnerClassNameCRO,
                DbOOStyle.fOojv_inheritedMemberFont, null, colorText);
        inheritedInnerClassNameCRO.setColor(colorText);

        constraintNameCRO = getCellRenderingOptionForConcept(stereotypeNameCRO,
                DbOOStyle.fOojv_stereotypeNameFont, null, colorText);
        constraintNameCRO.setAlignment(GraphicUtil.CENTER_ALIGNMENT);

        // Prefix
        classPrefixCRO = getCellRenderingOptionForConcept(classPrefixCRO,
                DbOOStyle.fOojv_classNameFont, new CompositeCellRenderer(), colorText);
        abstractClassPrefixCRO = getCellRenderingOptionForConcept(abstractClassPrefixCRO,
                DbOOStyle.fOojv_abstractClassNameFont, new CompositeCellRenderer(), colorText);
        interfacePrefixCRO = getCellRenderingOptionForConcept(interfacePrefixCRO,
                DbOOStyle.fOojv_interfaceNameFont, new CompositeCellRenderer(), colorText);
        exceptionPrefixCRO = getCellRenderingOptionForConcept(exceptionPrefixCRO,
                DbOOStyle.fOojv_exceptionNameFont, new CompositeCellRenderer(), colorText);
        fieldPrefixCRO = getCellRenderingOptionForConcept(fieldPrefixCRO,
                DbOOStyle.fOojv_fieldFont, new CompositeCellRenderer(), colorText);
        methodPrefixCRO = getCellRenderingOptionForConcept(methodPrefixCRO,
                DbOOStyle.fOojv_methodFont, new CompositeCellRenderer(), colorText);
        innerClassPrefixCRO = getCellRenderingOptionForConcept(innerClassPrefixCRO,
                DbOOStyle.fOojv_innerClassFont, new CompositeCellRenderer(), colorText);
        inheritedFieldPrefixCRO = getCellRenderingOptionForConcept(inheritedFieldPrefixCRO,
                DbOOStyle.fOojv_inheritedMemberFont, new CompositeCellRenderer(), colorText);
        inheritedMethodPrefixCRO = getCellRenderingOptionForConcept(inheritedMethodPrefixCRO,
                DbOOStyle.fOojv_inheritedMemberFont, new CompositeCellRenderer(), colorText);
        inheritedInnerClassPrefixCRO = getCellRenderingOptionForConcept(
                inheritedInnerClassPrefixCRO, DbOOStyle.fOojv_inheritedMemberFont,
                new CompositeCellRenderer(), colorText);
    }

    private CellRenderingOption getCellRenderingOptionForConcept(CellRenderingOption cellRO,
            MetaField metaField, CellRenderer renderer, Color color) throws DbException {
        Font font = (Font) adtGO.find(metaField);
        if (cellRO == null) {
            CellRenderingOption cellOption = new CellRenderingOption(renderer != null ? renderer
                    : new StringCellRenderer(), font, GraphicUtil.LEFT_ALIGNMENT);
            cellOption.setColor(color);
            return cellOption;
        } else {
            cellRO.setFont(font);
            cellRO.setColor(color);
            return cellRO;
        }
    }

    public MatrixZone getStereotypeZone() {
        return stereotypeZone;
    }

    public MatrixZone getAdtZone() {
        return adtZone;
    }

    public MatrixZone getFieldZone() {
        return fieldZone;
    }

    public MatrixZone getMethodZone() {
        return methodZone;
    }

    public MatrixZone getInnerClassZone() {
        return innerClassZone;
    }

    public DbSMSClassifierGo getAdtGO() {
        return adtGO;
    }

    public Db getDb() {
        return getAdtGO().getDb();
    }

    public final DbObject getSemanticalObject() {
        return getAdtSO();
    }

    public final DbObject getGraphicalObject() {
        return getAdtGO();
    }

    public DbJVClass getAdtSO() {
        return adtSO;
    }

    public final boolean selectAtCellLevel() {
        return true;
    }

    // BEWARE: overriding methods must call super.delete as last action
    public void delete(boolean all) {
        adtGO.setGraphicPeer(null);
        adtGO.removeDbRefreshListener(adtGoRefreshTg);
        stereotypeZone.removeAllRows();
        adtZone.removeAllRows();
        inheritedFieldZone.removeAllRows();
        fieldZone.removeAllRows();
        inheritedMethodZone.removeAllRows();
        methodZone.removeAllRows();
        inheritedInnerClassZone.removeAllRows();
        innerClassZone.removeAllRows();
        constraintZone.removeAllRows();

        super.delete(all);
    }

    private boolean isInterfaceShownAsCircle() throws DbException {
        if (adtSO.isInterface()) {
            DbOOStyle style = (DbOOStyle) adtGO.getStyle();
            if (style == null) {
                DbOODiagram diag = (DbOODiagram) adtGO.getCompositeOfType(DbOODiagram.metaClass);
                style = (DbOOStyle) diag.getStyle();
            } // end if

            if (style != null) {
                Boolean b = style.getOojv_umlInterfaceShownAsCircle();
                if ((b != null) && b.booleanValue()) {
                    return true;
                }
            }
        } // end if

        return false;
    } // end isInterfaceShownAsCircle()

    private boolean isAssociationClassShownAsDiamond() throws DbException {
        if (adtSO.isAssociationClass()) {
            DbOOStyle style = (DbOOStyle) adtGO.getStyle();
            if (style == null) {
                DbOODiagram diag = (DbOODiagram) adtGO.getCompositeOfType(DbOODiagram.metaClass);
                style = (DbOOStyle) diag.getStyle();
            } // end if

            if (style != null) {
                Boolean b = style.getOojv_umlInterfaceShownAsCircle();
                if ((b != null) && b.booleanValue()) {
                    return true;
                }
            } // end if
        } // end if

        return false;
    } // end

    private void setZones() throws DbException {
        boolean interfaceShownAsCircle = isInterfaceShownAsCircle();
        boolean associationClassShownAsDiamond = isAssociationClassShownAsDiamond();
        setZonesVisibility(interfaceShownAsCircle, associationClassShownAsDiamond);

        CellEditor adtVisibilityEditor = new DomainCellEditor(DbJVClass.fVisibility);
        CellEditor memberVisibilityEditor = new DomainCellEditor(DbJVDataMember.fVisibility);
        CellEditor methodVisibilityEditor = new DomainCellEditor(DbOOAbstractMethod.fVisibility);
        CellEditor memberTypeEditor = new MemberTypeCellEditor();

        // stereotype zone
        DbOOStyle style = getStyleFrom(adtGO);
        if (style != null) {
            Boolean b = style.getOojv_umlStereotypeDisplayed();
            if ((b != null) && (b.booleanValue())) {
                stereotypeZone
                        .addColumn(new ColumnCellsOption(stereotypeNameCRO, (CellEditor) null));
            }
            b = style.getOojv_umlStereotypeIconDisplayed();
            if ((b != null) && (b.booleanValue())) {
                stereotypeZone.addColumn(new ColumnCellsOption(stereotypeImageCRO,
                        (CellEditor) null));
            }
        } // end if

        adtZone.addColumn(new ColumnCellsOption(classPrefixCRO, adtVisibilityEditor));
        CellEditor tfEditor = new DbTextFieldCellEditor(DbSemanticalObject.fName, false);
        classNameCellsOption = new ColumnCellsOption(classNameCRO, tfEditor);
        interfaceNameCellsOption = new ColumnCellsOption(interfaceNameCRO, tfEditor);
        exceptionNameCellsOption = new ColumnCellsOption(exceptionNameCRO, tfEditor);
        abstractClassNameCellsOption = new ColumnCellsOption(abstractClassNameCRO, tfEditor);
        abstractExceptionNameCellsOption = mergeColumnCellsOption(exceptionNameCellsOption,
                abstractClassNameCellsOption);

        adtZone.addColumn(classNameCellsOption);
        adtZone.addColumn(new ColumnCellsOption(duplicateCRO, null));

        inheritedFieldZone.addColumn(new ColumnCellsOption(inheritedFieldPrefixCRO, null));
        inheritedFieldZone.addColumn(new ColumnCellsOption(inheritedFieldNameCRO, null));
        inheritedFieldZone.addColumn(new ColumnCellsOption(inheritedFieldNameCRO, null));

        fieldZone.addColumn(new ColumnCellsOption(fieldPrefixCRO, memberVisibilityEditor));
        tfEditor = new DbTextFieldCellEditor(DbSemanticalObject.fName, true);
        fieldZone.addColumn(new ColumnCellsOption(fieldUMLStereotypeCRO, null));

        if (isUMLOrder()) {
            fieldZone.addColumn(new ColumnCellsOption(fieldNameCRO, tfEditor));
            fieldZone.addColumn(new ColumnCellsOption(fieldNameCRO, memberTypeEditor));
        } else {
            fieldZone.addColumn(new ColumnCellsOption(fieldNameCRO, memberTypeEditor));
            fieldZone.addColumn(new ColumnCellsOption(fieldNameCRO, tfEditor));
        }

        fieldZone.addColumn(new ColumnCellsOption(fieldUMLConstraintsCRO, null));

        inheritedMethodZone.addColumn(new ColumnCellsOption(inheritedMethodPrefixCRO, null));
        inheritedMethodZone.addColumn(new ColumnCellsOption(inheritedMethodNameCRO, null));
        inheritedMethodZone.addColumn(new ColumnCellsOption(inheritedMethodNameCRO, null));

        methodZone.addColumn(new ColumnCellsOption(methodPrefixCRO, methodVisibilityEditor));
        methodZone.addColumn(new ColumnCellsOption(methodUMLStereotypeCRO, null));
        tfEditor = new DbTextFieldCellEditor(DbSemanticalObject.fName, true);

        if (isUMLOrder()) {
            methodZone.addColumn(new ColumnCellsOption(methodNameCRO, tfEditor));
            methodZone.addColumn(new ColumnCellsOption(methodNameCRO, memberTypeEditor));
        } else {
            methodZone.addColumn(new ColumnCellsOption(methodNameCRO, memberTypeEditor));
            methodZone.addColumn(new ColumnCellsOption(methodNameCRO, tfEditor));
        }

        methodZone.addColumn(new ColumnCellsOption(methodUMLConstraintsCRO, null));

        inheritedInnerClassZone
                .addColumn(new ColumnCellsOption(inheritedInnerClassPrefixCRO, null));
        inheritedInnerClassZone.addColumn(new ColumnCellsOption(inheritedInnerClassNameCRO, null));

        innerClassZone.addColumn(new ColumnCellsOption(innerClassPrefixCRO, adtVisibilityEditor));
        tfEditor = new DbTextFieldCellEditor(LocaleMgr.misc.getString("InnerClass"),
                DbSemanticalObject.fName, true);
        innerClassZone.addColumn(new ColumnCellsOption(innerClassNameCRO, tfEditor));

        constraintZone.addColumn(new ColumnCellsOption(constraintNameCRO, null));

        stereotypeZone.setHasBottomLine(false);

        addZone(stereotypeZone);
        addZone(adtZone);
        addZone(inheritedFieldZone);
        addZone(fieldZone);
        addZone(inheritedMethodZone);
        addZone(methodZone);
        addZone(inheritedInnerClassZone);
        addZone(innerClassZone);
        addZone(constraintZone); // Contraint zone is usually the last
        // compartment in UML [MS]
    }

    private DbOOStyle getStyleFrom(DbOOAdtGo adtGO) throws DbException {
        DbOOStyle style = (DbOOStyle) adtGO.getStyle();
        if (style == null) {
            DbOODiagram diag = (DbOODiagram) adtGO.getCompositeOfType(DbOODiagram.metaClass);
            style = (DbOOStyle) diag.getStyle();
        }

        return style;
    } // end getStyleFrom()

    private ColumnCellsOption mergeColumnCellsOption(ColumnCellsOption mainOption,
            ColumnCellsOption fontToMergeOption) {
        ColumnCellsOption mergedOption = mainOption;
        Font font = fontToMergeOption.cellOption.getFont();
        if (font.getStyle() != Font.PLAIN) {
            CellRenderingOption mainCRO = mainOption.cellOption;
            Font mainFont = mainCRO.getFont();
            Font newFont = new Font(mainFont.getName(), font.getStyle() | mainFont.getStyle(),
                    mainFont.getSize());
            CellRenderingOption newMainCRO = new CellRenderingOption(mainCRO.getCellRenderer(),
                    newFont, mainCRO.getAlignment());
            mergedOption = new ColumnCellsOption(newMainCRO, mainOption.editor);
        }
        return mergedOption;
    }

    protected void setZonesVisibility(boolean interfaceShownAsCircle,
            boolean associationClassShownAsDiamond) throws DbException {
        if (interfaceShownAsCircle || associationClassShownAsDiamond) {
            inheritedFieldZone.setVisible(false);
            fieldZone.setVisible(false);
            inheritedMethodZone.setVisible(false);
            methodZone.setVisible(false);
            inheritedInnerClassZone.setVisible(false);
            innerClassZone.setVisible(false);
            constraintZone.setVisible(false);
        } else {
            inheritedFieldZone.setVisible(((Boolean) adtGO
                    .find(DbOOStyle.fOojv_inheritedAttributeDisplayed)).booleanValue());
            fieldZone.setVisible(((Boolean) adtGO.find(DbOOStyle.fOojv_attributeDisplayed))
                    .booleanValue());
            inheritedMethodZone.setVisible(((Boolean) adtGO
                    .find(DbOOStyle.fOojv_inheritedMethodDisplayed)).booleanValue());
            methodZone.setVisible(((Boolean) adtGO.find(DbOOStyle.fOojv_methodDisplayed))
                    .booleanValue());
            inheritedInnerClassZone.setVisible(((Boolean) adtGO
                    .find(DbOOStyle.fOojv_inheritedInnerClassDisplayed)).booleanValue());
            innerClassZone.setVisible(((Boolean) adtGO.find(DbOOStyle.fOojv_innerClassDisplayed))
                    .booleanValue());
            constraintZone
                    .setVisible(((Boolean) adtGO.find(DbOOStyle.fOojv_umlConstraintDisplayed))
                            .booleanValue());
        }
    }

    protected void setBoxColor(boolean interfaceShownAsCircle,
            boolean associationClassShownAsDiamond) throws DbException {
        if (interfaceShownAsCircle) {
            setFillColor(null);
            setLineColor(null);
        } else if (associationClassShownAsDiamond) {
            setFillColor(DIAGRAM_BACKGROUND);
            setLineColor(DIAGRAM_BACKGROUND);
        } else {
            setFillColor((Color) (adtGO.find(DbSMSClassifierGo.fBackgroundColor)));
            setLineColor((Color) (adtGO.find(DbSMSClassifierGo.fLineColor)));
        }
    }

    protected void setLineStyle() throws DbException {
        this.setLineStyle((Boolean) (adtGO.find(DbSMSClassifierGo.fHighlight)), (Boolean) (adtGO
                .find(DbSMSClassifierGo.fDashStyle)));
    }

    private void initShape() throws DbException {
        GraphicShape shape = RectangleShape.singleton; // default

        boolean isUtility = adtSO.isStatic();
        DbSMSStereotype stereotype = adtSO.getUmlStereotype();

        if (stereotype != null) {
            String name = stereotype.getName();
            if (name.equals("utility")) { // NOT LOCALIZABLE, stereotype name
                isUtility = true;
            } // end if
        } // end if

        if (isUtility) {
            shape = ShadowRectShape.southWestSingleton;
        } // end if

        setShape(shape);
    } // end initShape()

    protected void initStyleElements() throws DbException {
        boolean interfaceShownAsCircle = isInterfaceShownAsCircle();
        boolean associationClassShownAsDiamond = isAssociationClassShownAsDiamond();
        setZonesVisibility(interfaceShownAsCircle, associationClassShownAsDiamond);
        setBoxColor(interfaceShownAsCircle, associationClassShownAsDiamond);
        setLineStyle();
        initCellRenderingOptions();
    }

    protected void populateContents() throws DbException {
        initShape();
        initStyleElements();
        ZoneBoxSelection savedSel = new ZoneBoxSelection(this);
        boolean interfaceShownAsCircle = isInterfaceShownAsCircle();
        boolean associationClassShownAsDiamond = isAssociationClassShownAsDiamond();
        populateStereotype(interfaceShownAsCircle, associationClassShownAsDiamond);
        populateName();
        populateFields();
        populateMethods();
        populateInnerClass();
        populateInheritedMembers();
        populateConstraints();
        savedSel.restore();
        diagram.setComputePos(this);
    }

    // if return true the member is shown
    private boolean modifierFinalFilter(DbSemanticalObject member) throws DbException {
        boolean isFinal = false;
        if (member instanceof DbJVDataMember)
            isFinal = ((DbJVDataMember) member).isFinal();
        else if (member instanceof DbJVClass)
            isFinal = ((DbJVClass) member).isFinal();
        else if (member instanceof DbJVMethod)
            isFinal = ((DbJVMethod) member).isFinal();

        Boolean b1 = (Boolean) adtGO.find(DbOOStyle.fOojv_finalModifierDisplayed);
        Boolean b2 = (Boolean) adtGO.find(DbOOStyle.fOojv_nonFinalModifierDisplayed);
        return (isFinal && (b1 != null) && (b1.booleanValue()))
                || (!isFinal && (b2 != null) && (b2.booleanValue()));
    }

    // if return true the member is shown
    private boolean modifierAdtMemberFilter(DbSemanticalObject clMember) throws DbException {
        boolean isStatic = false;
        if (clMember instanceof DbJVDataMember)
            isStatic = ((DbJVDataMember) clMember).isStatic();
        else if (clMember instanceof DbJVClass)
            isStatic = ((DbJVClass) clMember).isStatic();
        else if (clMember instanceof DbJVMethod)
            isStatic = ((DbJVMethod) clMember).isStatic();
        // GP: Que doit-on faire si static ne s'applique pas à Constructeur???

        Boolean b1 = (Boolean) adtGO.find(DbOOStyle.fOojv_staticModifierDisplayed);
        Boolean b2 = (Boolean) adtGO.find(DbOOStyle.fOojv_nonStaticModifierDisplayed);

        boolean isShown = (isStatic && (b1 != null) && (b1.booleanValue()))
                || (!isStatic && (b2 != null) && (b2.booleanValue()));

        if (isShown) {

            OOVisibility visibility = JVVisibility.getInstance(JVVisibility.PUBLIC);
            if (clMember instanceof DbJVDataMember)
                visibility = ((DbJVDataMember) clMember).getVisibility();
            else if (clMember instanceof DbJVClass)
                visibility = ((DbJVClass) clMember).getVisibility();
            else if (clMember instanceof DbJVMethod)
                visibility = ((DbJVMethod) clMember).getVisibility();
            else if (clMember instanceof DbJVConstructor)
                visibility = ((DbJVConstructor) clMember).getVisibility();

            if (visibility != null) {
                Boolean b;
                switch (visibility.getValue()) {
                case JVVisibility.PUBLIC:
                    b = (Boolean) adtGO.find(DbOOStyle.fOojv_publicModifierDisplayed);
                    isShown = (b != null) && (b.booleanValue());
                    break;
                case JVVisibility.PROTECTED:
                    b = (Boolean) adtGO.find(DbOOStyle.fOojv_protectedModifierDisplayed);
                    isShown = (b != null) && (b.booleanValue());
                    break;
                case JVVisibility.PRIVATE:
                    b = (Boolean) adtGO.find(DbOOStyle.fOojv_privateModifierDisplayed);
                    isShown = (b != null) && (b.booleanValue());
                    break;
                case JVVisibility.DEFAULT:
                    b = (Boolean) adtGO.find(DbOOStyle.fOojv_packageModifierDisplayed);
                    isShown = (b != null) && (b.booleanValue());
                    break;
                }
            }
        }

        if (clMember instanceof DbJVClass) {
            if (((DbJVClass) clMember).isInterface())
                return isShown;
        }

        return isShown && modifierFinalFilter(clMember);

    }

    private boolean modifierDataMemberFilter(DbJVDataMember dtMember) throws DbException {

        boolean val = !dtMember.isTransient();

        boolean isShown = val
                && ((Boolean) adtGO.find(DbOOStyle.fOojv_nonTransientModifierDisplayed))
                        .booleanValue()
                || !val
                && ((Boolean) adtGO.find(DbOOStyle.fOojv_transientModifierDisplayed))
                        .booleanValue();
        if (isShown) {

            if (dtMember.getAssociationEnd() != null) {
                isShown = ((Boolean) adtGO.find(DbOOStyle.fOojv_associationAttributeDisplayed))
                        .booleanValue()
                        && dtMember.getAssociationEnd().isNavigable();
            }

        }
        return isShown && modifierAdtMemberFilter(dtMember);
    }

    private boolean modifierOperationMemberFilter(DbOOAbstractMethod methodMember)
            throws DbException {
        return modifierAbstractMemberFilter(methodMember) && modifierAdtMemberFilter(methodMember);
    }

    private boolean modifierAbstractMemberFilter(DbSemanticalObject member) throws DbException {
        boolean val = false;
        if (member instanceof DbOOMethod)
            val = ((DbOOMethod) member).isAbstract();
        else if (member instanceof DbOOClass)
            val = ((DbOOClass) member).isAbstract();
        // GP: Que doit-on faire si abstract ne s'applique pas à Constructeur???

        Boolean b1 = (Boolean) adtGO.find(DbOOStyle.fOojv_abstractModifierDisplayed);
        Boolean b2 = (Boolean) adtGO.find(DbOOStyle.fOojv_nonAbstractModifierDisplayed);
        return (val && ((b1 != null) && (b1.booleanValue())) || (!val && ((b2 != null) && (b2
                .booleanValue()))));
    }

    private String getUMLStereotypeName(DbSMSSemanticalObject semObject) throws DbException {
        String stereotypeName = null;
        DbSMSStereotype stereotype = semObject.getUmlStereotype();
        if (stereotype != null) {
            stereotypeName = "«" + stereotype.getName() + "»"; // NOT
            // LOCALIZABLE
        }

        return stereotypeName;
    }

    private String getUMLConstraintsName(DbSMSSemanticalObject semObject) throws DbException {
        String constraintsName = "";

        DbEnumeration dbEnum = semObject.getUmlConstraints().elements(DbSMSUmlConstraint.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSUmlConstraint constraint = (DbSMSUmlConstraint) dbEnum.nextElement();
            String constraintName = constraint.getName();
            if (constraintName == null)
                constraintName = "";
            else
                constraintName = "{" + constraintName + "}"; // NOT LOCALIZABLE
            constraintsName = constraintsName + constraintName;
        } // end while
        dbEnum.close();
        return constraintsName;
    }

    private String getClassStereotypeName(DbJVClass adtSO) throws DbException {
        String stereotypeName = null;

        MetaField mf = DbOOStyle.fOojv_umlStereotypeDisplayed;
        Boolean b = (Boolean) adtGO.find(mf);
        if (b.booleanValue()) {
            // note: I don't use static field for <<Interface>> and
            // <<exception>>
            // because I don't want to listen to localeChange [author?]

            // Display interface or exception, if applicable [MS]
            if (adtSO.isInterface()) {
                stereotypeName = LocaleMgr.misc.getString("<<Interface>>");
            } else if (adtSO.isException()) {
                stereotypeName = LocaleMgr.misc.getString("<<Exception>>");
            } // end if

            // For standard classes, display the stereotype if any [MS]
            if (stereotypeName == null) {
                stereotypeName = getUMLStereotypeName(adtSO);
            } // end if
        } // end if

        return stereotypeName;
    } // end getStereotype()

    // must be within a read transaction
    private void populateStereotype(boolean interfaceShownAsCircle,
            boolean associationClassShownAsDiamond) throws DbException {
        String stereotypeName = getClassStereotypeName(adtSO);
        Vector composite = getStereotypeImageData(adtSO);
        DbOOStyle style = getStyleFrom(adtGO);
        stereotypeZone.clear();

        int stereotypeStyle = 0;
        if (interfaceShownAsCircle || associationClassShownAsDiamond) {
            stereotypeZone.addColumn(new ColumnCellsOption(stereotypeImageCRO, (CellEditor) null));
            stereotypeStyle = 2;
        } else {
            Boolean b = (style == null) ? Boolean.TRUE : style.getOojv_umlStereotypeDisplayed();
            if ((b != null) && (b.booleanValue())) {
                stereotypeZone.addColumn(new ColumnCellsOption(stereotypeNameCRO,
                        new SMSStereotypeEditor(DbJVClass.fUmlStereotype)));
                stereotypeStyle += 1;
            }
            b = (style == null) ? Boolean.TRUE : style.getOojv_umlStereotypeIconDisplayed();
            if ((b != null) && (b.booleanValue())) {
                // stereotypeZone.addColumn(new
                // ColumnCellsOption(stereotypeImageCRO, (CellEditor)null));
                stereotypeZone.addColumn(new ColumnCellsOption(stereotypeImageCRO,
                        new SMSStereotypeEditor(DbJVClass.fUmlStereotype)));
                stereotypeStyle += 2;
            }
        } // end if

        stereotypeZone.addRow();

        switch (stereotypeStyle) {
        case 0:
            getStereotypeZone().setVisible(false);
            break;
        case 1:
            stereotypeZone.set(0, 0, new ZoneCell(adtSO, stereotypeName));
            getStereotypeZone().setVisible(true);
            break;
        case 2:
            stereotypeZone.set(0, 0, new ZoneCell(adtSO, composite));
            getStereotypeZone().setVisible(true);
            break;
        case 1 + 2:
            stereotypeZone.set(0, 0, new ZoneCell(adtSO, stereotypeName));
            stereotypeZone.set(0, 1, new ZoneCell(adtSO, composite));
            getStereotypeZone().setVisible(true);
            break;
        } // end switch
    } // end populateStereotype()

    private Vector getStereotypeImageData(DbJVClass adtSO) throws DbException {
        Vector compositeElements = new Vector();

        Image image = null;

        JVClassCategory category = adtSO.getStereotype();
        if (category.getValue() == JVClassCategory.INTERFACE) {
            image = Extensibility.getImage("interface.gif"); // NOT LOCALIZABLE.
            // file name
        } else if (category.getValue() == JVClassCategory.EXCEPTION) {
            image = Extensibility.getImage("exception.gif"); // NOT LOCALIZABLE.
            // file name
        } else if (adtSO.isAssociationClass()) {
            image = Extensibility.getImage("associationClass.gif"); // NOT
            // LOCALIZABLE.
            // file name
        } else { // an actual class
            DbSMSStereotype stereotype = adtSO.getUmlStereotype();
            if (stereotype != null) {
                image = stereotype.getIcon();
            } // end if
        } // end if

        if (image != null) {
            compositeElements.addElement(new CompositeCellRendererElement(new ImageCellRenderer(),
                    image));
        } // end if

        return compositeElements;
    } // end getStereotypeImageData()

    // must be within a read transaction
    private void populateName() throws DbException {

        CellEditor visibilityEditor = new DomainCellEditor(DbJVClass.fVisibility);

        ColumnCellsOption prefixOption;
        ColumnCellsOption cellsOption;
        if (adtSO.isInterface()) {
            cellsOption = interfaceNameCellsOption;
            prefixOption = new ColumnCellsOption(interfacePrefixCRO, visibilityEditor);
        } else if (adtSO.isException()) {
            if (adtSO.isAbstract())
                cellsOption = abstractExceptionNameCellsOption;
            else
                cellsOption = exceptionNameCellsOption;
            prefixOption = new ColumnCellsOption(exceptionPrefixCRO, visibilityEditor);
        } else if (adtSO.isClass() && adtSO.isAbstract()) {
            cellsOption = abstractClassNameCellsOption;
            prefixOption = new ColumnCellsOption(abstractClassPrefixCRO, visibilityEditor);
        } else {
            cellsOption = classNameCellsOption;
            prefixOption = new ColumnCellsOption(classPrefixCRO, visibilityEditor);
        }

        adtZone.clear();
        adtZone.addColumn(prefixOption);
        adtZone.addColumn(cellsOption);
        adtZone.addColumn(new ColumnCellsOption(duplicateCRO, null));

        adtZone.addRow();
        adtZone.set(0, ADT_PREFIX_IDX, new ZoneCell(adtSO, getPrefixData(adtSO)));
        String name = AnyAdt.getNameWithEnclosingAdts(adtSO);
        if (((Boolean) adtGO.find(DbOOStyle.fOojv_classQualifiedNameDisplayed)).booleanValue()) {
            if (!adtGO.isInSourceDiagram()) {
            	name = getQualifiedName(adtSO);
            }
        }
        adtZone.set(0, ADT_NAME_IDX, new ZoneCell(adtSO, name));
        adtZone.set(0, ADT_DUPLICATE_IDX, new ZoneCell(adtSO, calculateDuplicate(adtSO
                .getClassifierGos(), adtGO)));

    }

    private String getQualifiedName(DbJVClass adtSO) throws DbException {
    	String name = adtSO.getSemanticalName(DbObject.LONG_FORM);
    	
    	DbObject composite = adtSO.getComposite(); 
    	if (composite instanceof DbJVClassModel) {
    		DbJVClassModel model = (DbJVClassModel)composite;
    		name = model.buildFullNameString() + "." + name;
    	}
    	
		return name;
	}

	private String calculateDuplicate(DbRelationN gosRelation, DbSMSGraphicalObject dboG)
            throws DbException {
        DbObject diag = dboG.getComposite();
        int index = 0;
        int count = 0;
        DbEnumeration dbEnum = gosRelation.elements();
        while (dbEnum.hasMoreElements()) {
            DbSMSGraphicalObject elem = (DbSMSGraphicalObject) dbEnum.nextElement();
            if (elem.getComposite() == diag)
                count++;
            if (elem == dboG)
                index = count;
        }
        dbEnum.close();
        if (count < 2)
            return null;
        String pattern = "{0}/{1}";
        return MessageFormat.format(pattern,
                new Object[] { new Integer(index), new Integer(count) });
    }

    // must be within a read transaction
    private void populateFields() throws DbException {
        if (fieldZone.isVisible()) {
            fieldZone.removeAllRows();
            DbEnumeration ins = adtSO.getComponents().elements(DbJVDataMember.metaClass);
            int rowCtr = 0;
            FieldDisplayOptions options = new FieldDisplayOptions();
            options.forceUnderlineForStatic = doForceUnderlineOnStatic(adtGO); // computed
            // outside
            // the
            // loop
            // for
            // efficiency
            options.isEnumeration = isEnumeration(adtSO);

            while (ins.hasMoreElements()) {
                DbJVDataMember field = (DbJVDataMember) ins.nextElement();
                boolean rowAdded = populateField(field, rowCtr, options);
                if (rowAdded) {
                    rowCtr++;
                } // end if
            } // end while
            ins.close();
        } // end if
    } // end populateFields()

    private boolean isEnumeration(DbJVClass clas) throws DbException {
        DbSMSStereotype stereotype = clas.getUmlStereotype();
        String name = (stereotype == null) ? null : stereotype.getName();
        boolean isEnumeration = Extensibility.ENUMERATION.equals(name);
        return isEnumeration;
    }

    private boolean populateField(DbJVDataMember field, int rowCtr, FieldDisplayOptions options)
            throws DbException {
        ZoneCell zoneCell;
        boolean rowAdded = false;
        CellRenderingOption option = getFieldRenderingOption(field, options.forceUnderlineForStatic);

        if (modifierDataMemberFilter(field)) {
            fieldZone.addRow();
            rowAdded = true;

            // prefix
            zoneCell = new ZoneCell(field, getPrefixData(field));
            fieldZone.set(rowCtr, FIELD_PREFIX_IDX, zoneCell);

            // UML stereotype
            boolean showSteteotype = ((Boolean) adtGO.find(DbOOStyle.fOojv_umlStereotypeDisplayed))
                    .booleanValue();
            String stereotype = showSteteotype ? getUMLStereotypeName(field) : null;
            zoneCell = new ZoneCell(field, stereotype, option, null);
            zoneCell.setObject(field);
            zoneCell.setCellEditor(new SMSStereotypeEditor(DbJVMethod.fUmlStereotype));
            fieldZone.set(rowCtr, FIELD_UMLSTEREOTYPE_IDX, zoneCell);

            // field name
            String name = field.buildDisplayString();
            zoneCell = new ZoneCell(field, name, option, null);
            fieldZone.set(rowCtr, getFieldNameIndex(), zoneCell);

            // field type
            if (options.isEnumeration) {
                zoneCell = new ZoneCell(field, null);
            } else {
                name = (isUMLOrder()) ? ": " : "";
                name += buildTypeDisplayString(field);
                zoneCell = new ZoneCell(field, name, option, null);
            }
            fieldZone.set(rowCtr, getFieldTypeIndex(), zoneCell);

            // UML constraint
            boolean showConstraint = ((Boolean) adtGO.find(DbOOStyle.fOojv_umlConstraintDisplayed))
                    .booleanValue();
            String constraintName = showConstraint ? getUMLConstraintsName(field) : null;
            fieldZone.set(rowCtr, FIELD_UMLCONSTRAINTS_IDX, new ZoneCell(field, constraintName));
        }

        return rowAdded;
    } // end populateField

    private String buildTypeDisplayString(DbOODataMember field) throws DbException {
        DbOOAdt type = field.getType();
        String str = (type != null ? type.getName() : LocaleMgr.misc.getString("<undef>"));
        if (field.getElementType() != null) {
            str = str + "<" + field.getElementType().getName() + ">";
        }

        if (field.getTypeUse() != null && field.isTypeUseAfterType()) {
            str = str + field.getTypeUse();
        }

        return str;
    }

    private String buildTypeDisplayString(DbOOMethod method) throws DbException {
        DbOOAdt type = method.getReturnType();
        String str = (type != null ? type.getName() : "void");
        if (method.getReturnElementType() != null) {
            str = str + "<" + method.getReturnElementType().getName() + ">";
        }

        if (method.getTypeUse() != null) {
            str = str + method.getTypeUse();
        }

        return str;
    }

    private boolean doForceUnderlineOnStatic(DbOOAdtGo adtGO) throws DbException {
        DbOOStyle style = getStyleFrom(adtGO);
        Boolean b = (style == null) ? Boolean.FALSE : style.getOojv_umlForceUnderlineOnStatic();
        boolean forceUnderlineOnStatic = (b != null) && (b.booleanValue());
        return forceUnderlineOnStatic;
    }

    private boolean doForceItalicOnAbstract(DbOOAdtGo adtGO) throws DbException {
        DbOOStyle style = getStyleFrom(adtGO);
        Boolean b = (style == null) ? Boolean.FALSE : style.getOojv_umlForceItalicOnAbstract();
        boolean forceItalicOnAbstract = (b != null) && (b.booleanValue());
        return forceItalicOnAbstract;
    }

    private static final StringCellRenderer g_plainRenderer = new StringCellRenderer(false, false);
    private static final StringCellRenderer g_underlineRenderer = new StringCellRenderer(true,
            false);

    private CellRenderingOption getFieldRenderingOption(DbJVDataMember field,
            boolean forceUnderlineOnStatic) throws DbException {

        Font font = methodNameCRO.getFont();
        int alignment = methodNameCRO.getAlignment();
        int margin = methodNameCRO.getMargin();

        // should be displayed in underline?
        StringCellRenderer stringRenderer = (forceUnderlineOnStatic && field.isStatic()) ? g_underlineRenderer
                : g_plainRenderer;

        CellRenderingOption option = new CellRenderingOption(stringRenderer, font, alignment,
                margin);
        Color colorText = (Color) (adtGO.find(DbSMSClassifierGo.fTextColor));
        option.setColor(colorText);

        PropertiesSet applOptions = PropertiesManager.APPLICATION_PROPERTIES_SET;
        boolean recentlyModifiedFields = applOptions.getPropertyBoolean(
                DisplayRecentModifications.class,
                DisplayRecentModifications.DISP_RECENTLY_MODIFIED_FIELDS_PROPERTY, true);

        if (recentlyModifiedFields) {
            DisplayRecentModifications recentDisplay = DisplayRecentModifications.getInstance();
            option = recentDisplay.getRenderingOptionForRecent(field, option);
        }

        return option;
    } // end getFieldRenderingOption()

    private Vector getPrefixData(DbSemanticalObject semObj) throws DbException {
        Debug.assert2(semObj != null, "null argument not allowed. AdtBox.getPrefixData().");
        Vector compositeElements = new Vector();

        // Semantic Object prefix
        int status = semObj.getValidationStatus();
        if (status == DbObject.VALIDATION_ERROR) {
            DbtPrefix prefix = getPrefix(adtGO, DbOOStyle.fErrorPrefix);
            if (prefix != null) {
                addToCompositeElements(compositeElements, prefix);
            }
        } else if (status == DbObject.VALIDATION_WARNING) {
            DbtPrefix prefix = getPrefix(adtGO, DbOOStyle.fWarningPrefix);
            if (prefix != null) {
                addToCompositeElements(compositeElements, prefix);
            }
        } // end if

        // Link Prefix
        if (semObj instanceof DbSMSStructuralFeature) {
            DbSMSStructuralFeature feature = (DbSMSStructuralFeature) semObj;
            DbEnumeration enu = feature.getSourceLinks().elements();
            boolean hasSources = enu.hasMoreElements();
            enu.close();

            enu = feature.getTargetLinks().elements();
            boolean hasTargets = enu.hasMoreElements();
            enu.close();

            if (hasSources) {
                addToCompositeElements(compositeElements,
                        (DbtPrefix) adtGO.find(DbOOStyle.fSourcePrefix));
            }

            if (hasTargets) {
                addToCompositeElements(compositeElements,
                        (DbtPrefix) adtGO.find(DbOOStyle.fTargetPrefix));
            }

        } // end if

        // Hiding and Overriding Prefix
        if (semObj instanceof DbJVDataMember && semObj.getComposite() == adtSO
                && ((DbJVDataMember) semObj).getOverriddenFeatures().size() > 0) {
            if (((DbJVDataMember) semObj).isStatic()) {
                addToCompositeElements(compositeElements, (DbtPrefix) adtGO
                        .find(DbOOStyle.fOojv_hidingMemberPrefix));
            } else {
                addToCompositeElements(compositeElements, (DbtPrefix) adtGO
                        .find(DbOOStyle.fOojv_overridingMemberPrefix));
            }
        } else if (semObj instanceof DbJVMethod && semObj.getComposite() == adtSO
                && ((DbJVMethod) semObj).getOverriddenFeatures().size() > 0) {
            if (((DbJVMethod) semObj).isStatic()) {
                addToCompositeElements(compositeElements, (DbtPrefix) adtGO
                        .find(DbOOStyle.fOojv_hidingMemberPrefix));
            } else {
                addToCompositeElements(compositeElements, (DbtPrefix) adtGO
                        .find(DbOOStyle.fOojv_overridingMemberPrefix));
            }
        } // end if

        if (semObj.getComposite() != adtSO && semObj.getComposite() instanceof DbJVClass) {
            if (((DbJVClass) semObj.getComposite()).isInterface())
                addToCompositeElements(compositeElements, (DbtPrefix) adtGO
                        .find(DbOOStyle.fOojv_memberInheritedFromInterfacePrefix));
        } // end if

        int semObjVisibility = JVVisibility.DEFAULT;

        if (semObj instanceof DbJVDataMember) {
            DbJVDataMember field = (DbJVDataMember) semObj;
            OOVisibility visib = field.getVisibility();
            semObjVisibility = (visib == null) ? OOVisibility.PRIVATE : visib.getValue();
        } else if (semObj instanceof DbJVClass) {
            DbJVClass claz = (DbJVClass) semObj;
            OOVisibility visib = claz.getVisibility();
            semObjVisibility = (visib == null) ? OOVisibility.PRIVATE : visib.getValue();
        } else if (semObj instanceof DbJVMethod) {
            DbJVMethod method = (DbJVMethod) semObj;
            OOVisibility visib = method.getVisibility();
            semObjVisibility = (visib == null) ? OOVisibility.PRIVATE : visib.getValue();
        } else if (semObj instanceof DbJVConstructor) {
            DbJVConstructor constr = (DbJVConstructor) semObj;
            OOVisibility visib = constr.getVisibility();
            semObjVisibility = (visib == null) ? OOVisibility.PRIVATE : visib.getValue();
        } // end if

        DbtPrefix prefix = null;
        switch (semObjVisibility) {
        case JVVisibility.PUBLIC:
            prefix = (DbtPrefix) adtGO.find(DbOOStyle.fOojv_publicVisibilityPrefix);
            break;
        case JVVisibility.PROTECTED:
            prefix = (DbtPrefix) adtGO.find(DbOOStyle.fOojv_protectedVisibilityPrefix);
            break;
        case JVVisibility.PRIVATE:
            prefix = (DbtPrefix) adtGO.find(DbOOStyle.fOojv_privateVisibilityPrefix);
            break;
        case JVVisibility.DEFAULT:
            prefix = (DbtPrefix) adtGO.find(DbOOStyle.fOojv_packageVisibilityPrefix);
            break;
        }
        addToCompositeElements(compositeElements, prefix);

        boolean isAbstract = false;

        if (semObj instanceof DbOOMethod) {
            isAbstract = ((DbOOMethod) semObj).isAbstract();
        } else if (semObj != adtSO && semObj instanceof DbJVClass) {
            isAbstract = ((DbJVClass) semObj).isAbstract();
        }

        if (isAbstract)
            addToCompositeElements(compositeElements, (DbtPrefix) adtGO
                    .find(DbOOStyle.fOojv_abstractModifierPrefix));

        boolean isStatic = false;

        if (semObj instanceof DbJVDataMember) {
            isStatic = ((DbJVDataMember) semObj).isStatic();
        } else if (semObj instanceof DbJVClass) {
            isStatic = ((DbJVClass) semObj).isStatic();
        } else if (semObj instanceof DbJVMethod) {
            isStatic = ((DbJVMethod) semObj).isStatic();
        }

        if (isStatic)
            addToCompositeElements(compositeElements, (DbtPrefix) adtGO
                    .find(DbOOStyle.fOojv_staticModifierPrefix));

        boolean isFinal = false;

        if (semObj instanceof DbJVDataMember) {
            isFinal = ((DbJVDataMember) semObj).isFinal();
        } else if (semObj instanceof DbJVClass) {
            isFinal = ((DbJVClass) semObj).isFinal();
        } else if (semObj instanceof DbJVMethod) {
            isFinal = ((DbJVMethod) semObj).isFinal();
        }

        if (isFinal)
            addToCompositeElements(compositeElements, (DbtPrefix) adtGO
                    .find(DbOOStyle.fOojv_finalModifierPrefix));

        boolean isTransient = false;

        if (semObj instanceof DbJVDataMember) {
            isTransient = ((DbJVDataMember) semObj).isTransient();
        }

        if (isTransient)
            addToCompositeElements(compositeElements, (DbtPrefix) adtGO
                    .find(DbOOStyle.fOojv_transientModifierPrefix));

        boolean isImported = false;

        if (semObj == adtSO && !adtGO.isInSourceDiagram()) {
            isImported = true;
        }

        if (isImported) {
            addToCompositeElements(compositeElements, (DbtPrefix) adtGO
                    .find(DbOOStyle.fOojv_importedClassPrefix));
        }

        // bean Session or Entity
        /*
         * 16 oct 2001 Les attributs liés aux Java Bean on été détruits if( semObj instanceof
         * DbJVClass ) { JVEJBType ejbtype = ((DbJVClass)semObj).getEjbType(); if (ejbtype != null){
         * if (ejbtype.getValue() == JVEJBType.ENTITY) addToCompositeElements(compositeElements,
         * (DbtPrefix)adtGO.find(DbOOStyle.fOojv_entityBeanClassPrefix)); else
         * addToCompositeElements(compositeElements,
         * (DbtPrefix)adtGO.find(DbOOStyle.fOojv_sessionBeanClassPrefix)); } }
         */

        return compositeElements;
    }

    private DbtPrefix getPrefix(DbOOAdtGo adtGO2, MetaField prefixMF) throws DbException {
        DbtPrefix prefix = null;
        DbSMSStyle style = adtGO2.findStyle();
        DbSMSStyle ancestorStyle = style.getAncestor();

        if (ancestorStyle == null) {
            Object o = style.get(prefixMF);
            if (o instanceof DbtPrefix) {
                prefix = (DbtPrefix) o;
            } else {
                prefix = PrefixOptionComponent.getDefaultPrefix(style, prefixMF);
            } // end if

        } else {
            prefix = (DbtPrefix) adtGO2.find(prefixMF);
        }

        return prefix;
    }

    private void addToCompositeElements(Vector compositeElements, DbtPrefix prefix) {
        if (prefix.getDisplayedComponent() == DbtPrefix.DISPLAY_TEXT) {
            String textPrefix = prefix.getText();
            if (textPrefix != null && textPrefix.length() > 0)
                compositeElements.addElement(new CompositeCellRendererElement(
                        new StringCellRenderer(), textPrefix));
        } else if (prefix.getDisplayedComponent() == DbtPrefix.DISPLAY_IMAGE) {
            Image imagePrefix = prefix.getImage();
            if (imagePrefix != null)
                compositeElements.addElement(new CompositeCellRendererElement(
                        new ImageCellRenderer(), imagePrefix));
        }
    }

    // must be within a read transaction
    private void populateMethods() throws DbException {
        if (methodZone.isVisible()) {
            Vector orderedVector = new Vector(); // Constructor first!
            Vector methodVector = new Vector();
            methodZone.removeAllRows();
            DbEnumeration ins = adtSO.getComponents().elements(DbOOAbstractMethod.metaClass);

            while (ins.hasMoreElements()) {
                DbOOAbstractMethod method = (DbOOAbstractMethod) ins.nextElement();
                if (method instanceof DbJVConstructor)
                    orderedVector.add(method);
                else
                    methodVector.add(method);
            }
            ins.close();

            for (int i = 0; i < methodVector.size(); i++) {
                orderedVector.add(methodVector.get(i));
            }

            int rowCtr = 0;
            boolean forceUnderlineOnStatic = doForceUnderlineOnStatic(adtGO); // computed
            // outside
            // the
            // loop
            // for
            // efficiency
            boolean forceItalicOnAbstract = doForceItalicOnAbstract(adtGO); // computed
            // outside
            // the
            // loop
            // for
            // efficiency
            for (int j = 0; j < orderedVector.size(); j++) {
                DbOOAbstractMethod method = (DbOOAbstractMethod) orderedVector.get(j);
                boolean rowAdded = populateMethod(method, rowCtr, forceUnderlineOnStatic,
                        forceItalicOnAbstract);
                if (rowAdded) {
                    rowCtr++;
                }
            } // end for
        } // end if
    } // end populateMethods()

    private boolean populateMethod(DbOOAbstractMethod method, int rowCtr,
            boolean forceUnderlineOnStatic, boolean forceItalicOnAbstract) throws DbException {
        boolean rowAdded = false;

        if (modifierOperationMemberFilter(method)) {
            methodZone.addRow();
            rowAdded = true;

            methodZone.set(rowCtr, METHOD_PREFIX_IDX, new ZoneCell(method, getPrefixData(method)));
            if (((Boolean) adtGO.find(DbOOStyle.fOojv_umlStereotypeDisplayed)).booleanValue()) {
                ZoneCell zoneCell = new ZoneCell(method, getUMLStereotypeName(method));
                zoneCell.setObject(method);
                zoneCell.setCellEditor(new SMSStereotypeEditor(DbJVMethod.fUmlStereotype));
                methodZone.set(rowCtr, METHOD_UMLSTEREOTYPE_IDX, zoneCell);
            } else {
                ZoneCell zoneCell = new ZoneCell(method, null);
                zoneCell.setObject(method);
                zoneCell.setCellEditor(new SMSStereotypeEditor(DbJVMethod.fUmlStereotype));
                methodZone.set(rowCtr, METHOD_UMLSTEREOTYPE_IDX, zoneCell);
            }

            // method name
            String name;
            boolean displaySignature = ((Boolean) adtGO
                    .find(DbOOStyle.fOojv_methodSignatureDisplayed)).booleanValue();
            boolean hideNameSignature = ((Boolean) adtGO
                    .find(DbOOStyle.fOojv_hideParameterNamesInSignatures)).booleanValue();

            if (displaySignature) {
                OoUtilities ooUtilities = OoUtilities.getSingleton();
                name = ooUtilities.buildDisplayString(method, isUMLOrder(), hideNameSignature);
            } else {
                name = method.getName();
            }

            if (method instanceof DbJVConstructor) {
                CellRenderingOption option = getConstructorRenderingOption((DbJVConstructor) method);
                ZoneCell zoneCell = new ZoneCell(method, name, option, null);
                methodZone.set(rowCtr, getMethodNameIndex(), zoneCell);
            } else if (method instanceof DbJVMethod) {
                CellRenderingOption option = getMethodRenderingOption((DbJVMethod) method,
                        forceUnderlineOnStatic, forceItalicOnAbstract);
                ZoneCell zoneCell = new ZoneCell(method, name, option, null);
                methodZone.set(rowCtr, getMethodNameIndex(), zoneCell);
            }

            // method type
            // note: for an overriding method, it's impossible to override the
            // return type
            if (method instanceof DbJVConstructor) {
                name = "";
                methodZone.set(rowCtr, getMethodTypeIndex(), new ZoneCell(method, name, false));
            } else if (method instanceof DbOOMethod) {
                DbOOMethod ooMethod = (DbOOMethod) method;
                name = (isUMLOrder()) ? ": " : "";
                name += buildTypeDisplayString(ooMethod);
                CellRenderingOption option = getMethodRenderingOption((DbJVMethod) method,
                        forceUnderlineOnStatic, forceItalicOnAbstract);
                ZoneCell cell = new ZoneCell(method, name, option, null);
                methodZone.set(rowCtr, getMethodTypeIndex(), cell);
            } // end if

            if (((Boolean) adtGO.find(DbOOStyle.fOojv_umlConstraintDisplayed)).booleanValue())
                methodZone.set(rowCtr, METHOD_UMLCONSTRAINTS_IDX, new ZoneCell(method,
                        getUMLConstraintsName(method)));
            else
                methodZone.set(rowCtr, METHOD_UMLCONSTRAINTS_IDX, new ZoneCell(method, null));
        }

        return rowAdded;
    }

    private CellRenderingOption getConstructorRenderingOption(DbJVConstructor constr)
            throws DbException {
        Font font = methodNameCRO.getFont();
        int alignment = methodNameCRO.getAlignment();
        int margin = methodNameCRO.getMargin();

        StringCellRenderer stringRenderer = g_plainRenderer;
        CellRenderingOption option = new CellRenderingOption(stringRenderer, font, alignment,
                margin);
        Color colorText = (Color) (adtGO.find(DbSMSClassifierGo.fTextColor));
        option.setColor(colorText);

        PropertiesSet applOptions = PropertiesManager.APPLICATION_PROPERTIES_SET;
        boolean recentlyModifiedMethods = applOptions.getPropertyBoolean(
                DisplayRecentModifications.class,
                DisplayRecentModifications.DISP_RECENTLY_MODIFIED_METHODS_PROPERTY, true);

        if (recentlyModifiedMethods) {
            DisplayRecentModifications recentDisplay = DisplayRecentModifications.getInstance();
            option = recentDisplay.getRenderingOptionForRecent(constr, option);
        }

        return option;
    } // end getMethodRenderingOption()

    private CellRenderingOption getMethodRenderingOption(DbJVMethod method,
            boolean forceUnderlineForStatic, boolean forceItalicOnAbstract) throws DbException {
        Font font = methodNameCRO.getFont();
        int alignment = methodNameCRO.getAlignment();
        int margin = methodNameCRO.getMargin();

        // if forceUnderlineOnStatic
        StringCellRenderer stringRenderer = (forceUnderlineForStatic && method.isStatic()) ? g_underlineRenderer
                : g_plainRenderer;

        // if forceItalicOnAbstract
        if (forceItalicOnAbstract && method.isAbstract()) {
            String name = font.getName();
            int style = font.getStyle() | Font.ITALIC;
            int size = font.getSize();
            font = new Font(name, style, size);
        }

        CellRenderingOption option = new CellRenderingOption(stringRenderer, font, alignment,
                margin);
        Color colorText = (Color) (adtGO.find(DbSMSClassifierGo.fTextColor));
        option.setColor(colorText);

        PropertiesSet applOptions = PropertiesManager.APPLICATION_PROPERTIES_SET;
        boolean recentlyModifiedMethods = applOptions.getPropertyBoolean(
                DisplayRecentModifications.class,
                DisplayRecentModifications.DISP_RECENTLY_MODIFIED_METHODS_PROPERTY, true);

        if (recentlyModifiedMethods) {
            DisplayRecentModifications recentDisplay = DisplayRecentModifications.getInstance();
            option = recentDisplay.getRenderingOptionForRecent(method, option);
        }

        return option;
    } // end getMethodRenderingOption()

    /*
     * private DisplayRecentChanges createRecentDisplay() throws DbException { Color recentColor;
     * SMSRecentModificationOption recentOption; long date;
     * 
     * try { recentColor = ((Color)adtGO.find(DbSMSStyle.fTextColorRecentModifications));
     * recentOption =((SMSRecentModificationOption)adtGO.find(DbSMSStyle.
     * fRecentModificationDisplay)); date = ((Long)adtGO.find(DbSMSStyle.fRecentModificationDate));
     * } catch (NullPointerException ex) { recentColor = Color.black; recentOption =
     * SMSRecentModificationOption
     * .getInstance(SMSRecentModificationOption.DO_NOT_SHOW_RECENT_MODIFS); date = 0L; }
     * 
     * DisplayRecentChanges recentDisplay = new DisplayRecentChanges(recentOption, date,
     * recentColor); return recentDisplay; }
     */

    // must be within a read transaction
    private void populateInnerClass() throws DbException {
        if (innerClassZone.isVisible()) {
            innerClassZone.removeAllRows();
            DbEnumeration ins = adtSO.getComponents().elements(DbJVClass.metaClass);
            int rowCtr = 0;
            while (ins.hasMoreElements()) {
                DbJVClass innerClass = (DbJVClass) ins.nextElement();
                innerClassZone.addRow();
                String name = innerClass.getName();
                if (name == null)
                    name = "";
                innerClassZone.set(rowCtr, INNER_CLASS_PREFIX_IDX, new ZoneCell(innerClass,
                        getPrefixData(innerClass)));
                innerClassZone.set(rowCtr, INNER_CLASS_NAME_IDX, new ZoneCell(innerClass, name));
                rowCtr++;
            }
            ins.close();
        }
    }

    // must be within a read transaction
    private void populateInheritedMembers() throws DbException {
        populateInheritedFields();
        populateInheritedMethods();
        populateInheritedInnerClasses();
    }

    private void populateInheritedFields() throws DbException {
        if (inheritedFieldZone.isVisible()) {
            if (fieldZone.isVisible())
                inheritedFieldZone.setHasBottomLine(false);
            inheritedFieldZone.removeAllRows();
            int rowCtr = 0;
            SrVector allFields = AnyAdt.getAllMembers(adtSO, AnyAdt.MEMBER_FIELD);
            boolean forceUnderlineForStatic = doForceUnderlineOnStatic(adtGO); // computed
            // outside
            // the
            // loop
            // for
            // efficiency

            for (int i = 0; i < allFields.size(); i++) {
                boolean hidden = false;
                DefaultComparableElement elem = (DefaultComparableElement) (allFields.elementAt(i));
                DbJVDataMember field = (DbJVDataMember) (elem.object);
                if (i + 1 < allFields.size()
                        && (elem.compareTo((DefaultComparableElement) (allFields.elementAt(i + 1))) == 0)) {
                    hidden = true;
                } else if (i - 1 >= 0
                        && (elem.compareTo((DefaultComparableElement) (allFields.elementAt(i - 1))) == 0)) {
                    hidden = true;
                }
                if (field.getComposite() != adtSO && modifierDataMemberFilter(field)) {
                    displayInheritedField(field, hidden, rowCtr, forceUnderlineForStatic);
                    rowCtr++;
                }
            }
        }
    }

    private void displayInheritedField(DbJVDataMember inheritedField, boolean hidden, int rowCtr,
            boolean forceUnderlineForStatic) throws DbException {
        String adtNameToCast = "";
        DbJVClass superAdt = (DbJVClass) inheritedField.getComposite();
        if (hidden) {

            if (inheritedField.isStatic() || (superAdt.isInterface())) {
                adtNameToCast = AnyAdt.getNameWithEnclosingAdts(superAdt) + ".";
            } else {
                adtNameToCast = "(" + AnyAdt.getNameWithEnclosingAdts(superAdt) + ")."; // NOT LOCALIZABLE
            }

        }

        inheritedFieldZone.addRow();
        inheritedFieldZone.set(rowCtr, INHERITED_FIELD_PREFIX_IDX, new ZoneCell(inheritedField,
                getPrefixData(inheritedField)));

        CellRenderingOption option = getFieldRenderingOption(inheritedField,
                forceUnderlineForStatic);
        ZoneCell zoneCell = new ZoneCell(inheritedField, adtNameToCast
                + inheritedField.buildDisplayString(), option, null);
        inheritedFieldZone.set(rowCtr, getInheritedFieldNamePrefix(), zoneCell);

        String fieldName = buildTypeDisplayString(inheritedField);
        inheritedFieldZone.set(rowCtr, getInheritedFieldTypePrefix(), new ZoneCell(inheritedField,
                fieldName));
    }

    private void populateInheritedInnerClasses() throws DbException {
        if (inheritedInnerClassZone.isVisible()) {
            if (innerClassZone.isVisible())
                inheritedInnerClassZone.setHasBottomLine(false);
            inheritedInnerClassZone.removeAllRows();
            int rowCtr = 0;
            SrVector allInnerClasses = AnyAdt.getAllMembers(adtSO, AnyAdt.MEMBER_INNER_CLASS);
            for (int i = 0; i < allInnerClasses.size(); i++) {
                boolean hidden = false;
                DefaultComparableElement elem = (DefaultComparableElement) (allInnerClasses
                        .elementAt(i));
                DbJVClass innerClass = (DbJVClass) (elem.object);
                if (i + 1 < allInnerClasses.size()
                        && (elem.compareTo((DefaultComparableElement) (allInnerClasses
                                .elementAt(i + 1))) == 0)) {
                    hidden = true;
                } else if (i - 1 >= 0
                        && (elem.compareTo((DefaultComparableElement) (allInnerClasses
                                .elementAt(i - 1))) == 0)) {
                    hidden = true;
                }
                if (innerClass.getComposite() != adtSO && modifierAdtMemberFilter(innerClass)) {
                    displayInheritedInnerClass(innerClass, hidden, rowCtr);
                    rowCtr++;
                }
            }
        }
    }

    private void displayInheritedInnerClass(DbJVClass inheritedInnerClass, boolean hidden,
            int rowCtr) throws DbException {
        String innerClassDisplayString = inheritedInnerClass.getName();
        if (hidden) {
            if (AnyAdt.getCategory(inheritedInnerClass) == AnyAdt.NESTED_ADT) {
                innerClassDisplayString = AnyAdt.getNameWithEnclosingAdts(inheritedInnerClass);
            } else {
                innerClassDisplayString = "("
                        + AnyAdt.getNameWithEnclosingAdts((DbJVClass) inheritedInnerClass
                                .getComposite()) + ")." + inheritedInnerClass.getName();
            }
        }
        inheritedInnerClassZone.addRow();
        inheritedInnerClassZone.set(rowCtr, INNER_CLASS_PREFIX_IDX, new ZoneCell(
                inheritedInnerClass, getPrefixData(inheritedInnerClass)));
        inheritedInnerClassZone.set(rowCtr, INNER_CLASS_NAME_IDX, new ZoneCell(inheritedInnerClass,
                innerClassDisplayString));
    }

    private void populateInheritedMethods() throws DbException {
        if (inheritedMethodZone.isVisible()) {
            if (methodZone.isVisible())
                inheritedMethodZone.setHasBottomLine(false);
            inheritedMethodZone.removeAllRows();
            int rowCtr = 0;
            boolean forceUnderlineOnStatic = doForceUnderlineOnStatic(adtGO); // computed
            // outside
            // the
            // loop
            // for
            // efficiency
            boolean forceItalicOnAbstract = doForceItalicOnAbstract(adtGO); // computed
            // outside
            // the
            // loop
            // for
            // efficiency

            for (Enumeration enumeration = AnyAdt.getAbsoluteMethods(adtSO).elements(); enumeration
                    .hasMoreElements();) {
                MethodsWithSameSignature withSameSignature = (MethodsWithSameSignature) enumeration
                        .nextElement();
                rowCtr = displayInheritedStaticMethods(withSameSignature, rowCtr,
                        forceUnderlineOnStatic);
                rowCtr = displayInheritedNotOverriddenMethods(withSameSignature, rowCtr,
                        forceUnderlineOnStatic, forceItalicOnAbstract);
            }
        }
    }

    private int displayInheritedStaticMethods(MethodsWithSameSignature methods, int rowCtr,
            boolean forceUnderlineOnStatic) throws DbException {

        for (Enumeration enumeration3 = methods.getNotOverriddenStaticMethods().elements(); enumeration3
                .hasMoreElements();) {
            Method notOverriddenStaticMethod = (Method) (enumeration3.nextElement());
            DbOOAbstractMethod staticMethod = (DbOOAbstractMethod) notOverriddenStaticMethod.method;
            boolean hidden = false;
            if (notOverriddenStaticMethod.overridingMethods.size() > 0) {
                hidden = true;
            }

            if (staticMethod instanceof DbJVMethod) {
                DbJVMethod method = (DbJVMethod) staticMethod;
                if (method.getComposite() != adtSO && modifierOperationMemberFilter(method)) {
                    displayInheritedMethod(method, hidden, rowCtr, forceUnderlineOnStatic, false);
                    rowCtr++;
                }
            }
        }

        return rowCtr;
    }

    private int displayInheritedNotOverriddenMethods(MethodsWithSameSignature methods, int rowCtr,
            boolean forceUnderlineOnStatic, boolean forceItalicOnAbstract) throws DbException {

        for (Enumeration enumeration4 = methods.getNotOverriddenNotHiddenInstanceMethods()
                .elements(); enumeration4.hasMoreElements();) {
            DbOOAbstractMethod notOverriddenMethod = (DbOOAbstractMethod) ((Method) (enumeration4
                    .nextElement())).method;

            if (notOverriddenMethod instanceof DbJVMethod) {
                DbJVMethod actualMethod = (DbJVMethod) notOverriddenMethod;
                if (actualMethod.getComposite() != adtSO
                        && modifierOperationMemberFilter(actualMethod)) {
                    displayInheritedMethod(actualMethod, false, rowCtr, forceUnderlineOnStatic,
                            forceItalicOnAbstract);
                    rowCtr++;
                }
            }
        }

        return rowCtr;
    }

    private void displayInheritedMethod(DbJVMethod inheritedMethod, boolean hidden, int rowCtr,
            boolean forceUnderlineOnStatic, boolean forceItalicOnAbstract) throws DbException {
        String adtNameToCast = "";
        if (hidden) {
            adtNameToCast = AnyAdt.getNameWithEnclosingAdts((DbJVClass) inheritedMethod
                    .getComposite())
                    + ".";
        }
        inheritedMethodZone.addRow();
        inheritedMethodZone.set(rowCtr, INHERITED_METHOD_PREFIX_IDX, new ZoneCell(inheritedMethod,
                getPrefixData(inheritedMethod)));
        String name = (inheritedMethod instanceof DbOOMethod ? buildTypeDisplayString((DbOOMethod) inheritedMethod)
                : "");
        inheritedMethodZone.set(rowCtr, getInheritedMethodTypePrefix(), new ZoneCell(
                inheritedMethod, name));
        if (((Boolean) adtGO.find(DbOOStyle.fOojv_inheritedMethodSignatureDisplayed))
                .booleanValue())
            name = inheritedMethod.buildDisplayString();
        else
            name = inheritedMethod.getName();

        CellRenderingOption option = getMethodRenderingOption(inheritedMethod,
                forceUnderlineOnStatic, forceItalicOnAbstract);
        ZoneCell zoneCell = new ZoneCell(inheritedMethod, adtNameToCast + name, option, null);
        inheritedMethodZone.set(rowCtr, getInheritedMethodNamePrefix(), zoneCell);
    }

    private void populateConstraints() throws DbException {
        if (constraintZone.isVisible()) {
            constraintZone.removeAllRows();
            DbEnumeration dbEnum = adtSO.getUmlConstraints().elements(DbSMSUmlConstraint.metaClass);
            int rowCtr = 0;
            while (dbEnum.hasMoreElements()) {
                DbSMSUmlConstraint constraint = (DbSMSUmlConstraint) dbEnum.nextElement();
                constraintZone.addRow();
                String constraintName = constraint.getName();
                if (constraintName == null)
                    constraintName = "";
                else
                    constraintName = "{" + constraintName + "}"; // NOT
                // LOCALIZABLE

                ZoneCell cell = new ZoneCell(constraint, constraintName);
                constraintZone.set(rowCtr, 0, cell);
                rowCtr++;
            } // end while
            dbEnum.close();
        }
    } // end populateConstraints()

    // Used by AddAbstractAction
    public final void editMember(DbSemanticalObject adtM) {
        MatrixZone zone = null;
        int col = 0;

        if (adtM instanceof DbOODataMember) {
            zone = getFieldZone();
            try {
                col = getFieldNameIndex();
            } catch (DbException ex) {
                col = 2; // default
            }
        } else if (adtM instanceof DbOOAbstractMethod) {
            zone = getMethodZone();
            try {
                col = getMethodNameIndex();
            } catch (DbException ex) {
                col = 2; // default value
            }
        } else if (adtM instanceof DbJVClass) {
            zone = getInnerClassZone();
            col = INNER_CLASS_NAME_IDX;
        }
        if (zone != null && zone.isVisible())
            ((ApplicationDiagram) diagram).editCell(this, zone.getCellID(adtM, col));
    }

    private boolean isUMLOrder() throws DbException {
        boolean UMLOrder = true;

        // get style
        DbOOStyle style = getStyleFrom(adtGO);
        if (style != null) {
            Boolean b = style.getOojv_umlTypeBeforeName();
            UMLOrder = !((b != null) && (b.booleanValue()));
        } // end if

        return UMLOrder;
    }

    public final String getToolTipText() {
        return (String) adtZone.get(0, ADT_NAME_IDX).getCellData();
    }

    /*
     * Unique listener listening to any change in the semantical part of the database that affect
     * the data needed to draw an AdtBox.
     */
    private static class BoxRefreshTg implements DbRefreshListener, DbRefreshPassListener {

        private HashSet refreshedAdts = null;

        BoxRefreshTg() {
        }

        public void beforeRefreshPass(Db db) throws DbException {
            refreshedAdts = null;
        }

        public void afterRefreshPass(Db db) throws DbException {
            refreshedAdts = null; // for gc
        }

        public void refreshAfterDbUpdate(DbUpdateEvent e) throws DbException {
            refreshAfterDbUpdate(e.dbo, e.metaField);
        }

        private void refreshAfterDbUpdate(DbObject dbo, MetaField mf) throws DbException {

            if (mf == DbObject.fComponents) {
                if (dbo instanceof DbOOClass)
                    refreshAdt((DbOOClass) dbo);
                else if (dbo instanceof DbOOAbstractMethod)
                    refreshAdt((DbOOClass) dbo.getComposite());
            } else if (dbo instanceof DbOOClass) {
                refreshAdt((DbOOClass) dbo);
                DbObject composite = dbo.getComposite();
                if (composite instanceof DbOOClass)
                    refreshAdt((DbOOClass) composite);
                if (mf == DbSemanticalObject.fName)
                    refreshAdtsTypedBy((DbOOClass) dbo);
            } else if (dbo instanceof DbOODataMember) {
                refreshAdt((DbOOClass) dbo.getComposite());
            } else if (dbo instanceof DbOOMethod) {
                refreshAdt((DbOOClass) dbo.getComposite());
            } else if (dbo instanceof DbOOConstructor) {
                refreshAdt((DbOOClass) dbo.getComposite());
            } else if (dbo instanceof DbOOParameter) {
                refreshAdt((DbOOClass) dbo.getComposite().getComposite());
            } else if (dbo instanceof DbOOAssociationEnd) {
                refreshAdt((DbOOClass) ((DbOOAssociationEnd) dbo).getAssociationMember()
                        .getComposite());
            }
        } // end refreshAfterDbUpdate()

        /*
         * Rebuild the data of all the AdtBoxes of an Adt.
         */
        private void refreshAdt(DbOOClass adt) throws DbException {
            if (refreshedAdts == null)
                refreshedAdts = new HashSet();
            if (!refreshedAdts.add(adt)) // already done in this transaction
                return;
            DbRelationN adtGos = adt.getClassifierGos();
            int nb = adtGos.size();
            for (int i = 0; i < nb; i++) {
                DbSMSClassifierGo adtGo = (DbSMSClassifierGo) adtGos.elementAt(i);
                AdtBox adtBox = (AdtBox) adtGo.getGraphicPeer();
                if (adtBox != null)
                    adtBox.populateContents();
            }
            refreshSubAdts(adt);
        }

        /*
         * Refresh all the Adts having members whose type is the renamed Adt.
         */
        private void refreshAdtsTypedBy(DbOOClass adt) throws DbException {
            refreshAdtsTypedBy(adt.getTypedDataMembers());
            refreshAdtsTypedBy(adt.getTypedElementDataMembers());
            refreshAdtsTypedBy(adt.getTypedMethods());
            refreshAdtsTypedBy(adt.getTypedElementMethods());
            refreshAdtsTypedBy(adt.getTypedParameters());
            refreshAdtsTypedBy(adt.getTypedElementParameters());
        }

        private void refreshAdtsTypedBy(DbRelationN members) throws DbException {
            int nb = members.size();
            for (int i = 0; i < nb; i++) {
                DbObject member = members.elementAt(i);
                DbOOClass adt = (DbOOClass) member.getCompositeOfType(DbOOClass.metaClass);
                refreshAdt(adt);
            }
        }

        private void refreshSubAdts(DbOOClass adt) throws DbException {
            DbEnumeration dbEnum = adt.getSubInheritances().elements();
            while (dbEnum.hasMoreElements()) {
                DbJVInheritance inheritance = (DbJVInheritance) dbEnum.nextElement();
                DbOOClass subAdt = inheritance.getSubClass();
                refreshAdt(subAdt);
            }
            dbEnum.close();
        }
    } // end BoxRefreshTg

    private class AdtGoRefreshTg implements DbRefreshListener {
        AdtGoRefreshTg() {
        }

        public void refreshAfterDbUpdate(DbUpdateEvent e) throws DbException {
            if (DbSMSGraphicalObject.fStyle == e.metaField) {
                populateContents();
                Line[] lines = getLines();
                for (int i = 0; i < lines.length; i++) {
                    if (lines[i] instanceof OOAssociation) {
                        ((OOAssociation) lines[i]).updateLabelsVisibility();
                    }
                }
            } else if (e.metaField == DbSMSClassifierGo.fBackgroundColor
                    || e.metaField == DbSMSClassifierGo.fDashStyle
                    || e.metaField == DbSMSClassifierGo.fHighlight
                    || e.metaField == DbSMSClassifierGo.fLineColor
                    || e.metaField == DbSMSClassifierGo.fTextColor) {
                initShape();
                initStyleElements();
                populateContents();
            }
        } // end
    } // end AdtGoRefreshTg

    private static class MemberTypeCellEditor implements CellEditor {
        MemberTypeCellEditor() {
        }

        public final JComponent getComponent(ZoneBox box, CellID cellID, ZoneCell value,
                DiagramView view, Rectangle cellRect) {
            Point pt = view.getLocationOnScreen();
            pt.x += cellRect.x + cellRect.width / 2;
            pt.y += cellRect.y + cellRect.height / 2;
            org.modelsphere.sms.oo.actions.SetTypeAction.setType(view,
                    new DbObject[] { (DbObject) value.getObject() }, pt);
            return null; // no editor necessary, all is done.
        }

        public final void stopEditing(int endCode) {
        } // never called
    } // end MemberTypeCellEditor

    private static class FieldDisplayOptions {
        public boolean forceUnderlineForStatic;
        boolean isEnumeration;
    }
} // end AdtBox

