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
package org.modelsphere.sms.db.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.srtypes.DbtPrefix;
import org.modelsphere.jack.baseDb.db.srtypes.ZoneJustification;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.tool.KeyTool;
import org.modelsphere.jack.graphic.zone.TableCell;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.Module;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEContextCell;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBESingleZoneDisplay;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEStyle;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.DbBEUseCaseResource;
import org.modelsphere.sms.be.db.srtypes.BEZoneStereotype;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.db.DbSMSAssociation;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSNotation;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSStyle;
import org.modelsphere.sms.db.srtypes.SMSDisplayDescriptor;
import org.modelsphere.sms.db.srtypes.SMSHorizontalAlignment;
import org.modelsphere.sms.db.srtypes.SMSNotationShape;
import org.modelsphere.sms.db.srtypes.SMSVerticalAlignment;
import org.modelsphere.sms.db.srtypes.SMSZoneOrientation;
import org.modelsphere.sms.db.util.or.OrStyle;
import org.modelsphere.sms.oo.db.DbOOStyle;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationGo;
import org.modelsphere.sms.or.db.DbORChoiceOrSpecialization;
import org.modelsphere.sms.or.db.DbORCommonItemStyle;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORDomainStyle;
import org.modelsphere.sms.or.db.DbORNotation;
import org.modelsphere.sms.or.db.DbORStyle;
import org.modelsphere.sms.or.db.srtypes.ORChoiceSpecializationCategory;
import org.modelsphere.sms.or.db.srtypes.ORConnectivitiesDisplay;
import org.modelsphere.sms.or.db.srtypes.ORConnectivityPosition;
import org.modelsphere.sms.or.db.srtypes.ORNotationSymbol;
import org.modelsphere.sms.or.db.srtypes.ORNumericRepresentation;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DbInitialization {

    // Notation unique IDs
    public static final int DATARUN_BPM = 0;
    public static final int DATARUN_SPM = 1;
    public static final int DATARUN_ISA = 2;
    public static final int GANE_SARSON = 3;
    public static final int MERISE = 4;
    public static final int YOURDON_DEMARCO = 5;
    public static final int WARD_MELLOR = 6;
    public static final int FUNCTIONAL_DIAGRAM = 7;
    public static final int MERISE_MCT = 8;
    public static final int MERISE_FLOW_SCHEMA = 9;
    public static final int MERISE_OOM = 10;
    public static final int P_PLUS = 11;
    public static final int P_PLUS_OPAL = 12;
    public static final int UML_USE_CASE = 13;
    public static final int UML_SEQUENCE_DIAGRAM = 14;
    public static final int UML_STATE_DIAGRAM = 15;
    public static final int UML_COLLABORATION_DIAGRAM = 16;
    public static final int UML_ACTIVITY_DIAGRAM = 17;
    public static final int UML_COMPONENT_DIAGRAM = 18;
    public static final int UML_DEPLOYMENT_DIAGRAM = 19;
    public static final int MESSAGE_MODELING = 20;
    public static final int OBJECT_LIFE_CYCLE = 21;

    public static final int DATARUN = 100;
    public static final int INFORMATION_ENGINEERING = 101;
    public static final int INFORMATION_ENGINEERING_PLUS = 102;
    public static final int LOGICAL_DATA_STRUCTURE = 103;
    public static final int UML = 104;
    public static final int ENTITY_RELATIONSHIP = 105;

    public static final int NEW_NOTATION_START_ID = 1000;

    // public constants
    public static final String UML_ACTIVITY_DIAGRAM_TXT = LocaleMgr.misc
            .getString("uml_activity_diagram");
    public static final String UML_COMPONENT_DIAGRAM_TXT = LocaleMgr.misc
            .getString("uml_component_diagram");
    public static final String UML_DEPLOYMENT_DIAGRAM_TXT = LocaleMgr.misc
            .getString("uml_deployment_diagram");

    private static final Image kKeyImage = GraphicUtil.loadImage(KeyTool.class, "resources/pk.gif"); // NOT LOCALIZABLE - image
    private static final Image kSourceImage = GraphicUtil.loadImage(Module.class,
            "db/resources/source.gif"); // NOT LOCALIZABLE - image
    private static final Image kTargetImage = GraphicUtil.loadImage(Module.class,
            "db/resources/target.gif"); // NOT LOCALIZABLE - image
    private static final Image kErrorImage = GraphicUtil.loadImage(Module.class,
            "db/resources/message_error.gif"); // NOT LOCALIZABLE
    // - image
    private static final Image kWarningImage = GraphicUtil.loadImage(Module.class,
            "db/resources/message_warning.gif"); // NOT
    // LOCALIZABLE -
    // image

    private static final DbtPrefix keyPrefix = new DbtPrefix(DbtPrefix.DISPLAY_IMAGE, "PK", null,
            kKeyImage);
    private static final DbtPrefix sourcePrefix = new DbtPrefix(DbtPrefix.DISPLAY_IMAGE, "S", null,
            kSourceImage);
    private static final DbtPrefix targetPrefix = new DbtPrefix(DbtPrefix.DISPLAY_IMAGE, "D", null,
            kTargetImage);
    private static final DbtPrefix errorPrefix = new DbtPrefix(DbtPrefix.DISPLAY_IMAGE, "!", null,
            kErrorImage);
    private static final DbtPrefix warningPrefix = new DbtPrefix(DbtPrefix.DISPLAY_IMAGE, "!",
            null, kWarningImage);

    public static DbtPrefix getKeyPrefix() {
        return keyPrefix;
    }

    public static DbtPrefix getSourcePrefix() {
        return sourcePrefix;
    }

    public static DbtPrefix getTargetPrefix() {
        return targetPrefix;
    }

    public static DbtPrefix getErrorPrefix() {
        return errorPrefix;
    }

    public static DbtPrefix getWarningPrefix() {
        return warningPrefix;
    }

    public static void initBeStyle() {
        // Display options
        DbBEStyle.be_displayOptions = new MetaField[] {
                DbBEStyle.fDisplayHierAlphanumericIdDbBEUseCase,
                DbBEStyle.fDisplayHierNumericIdDbBEUseCase, DbBEStyle.fDisplayQualiferDbBEActor,
                DbBEStyle.fDisplayQualiferDbBEFlow, DbBEStyle.fDisplayLabelDbBEFlow,
                DbBEStyle.fDisplayQualiferDbBEStore, DbBEStyle.fDisplayQualiferDbBEUseCase,
                DbBEStyle.fDisplayStereotypeIcon, DbBEStyle.fDisplayStereotypeName,
                DbBEStyle.fDisplayStereotypeOnly, };

        DbBEStyle.be_displayOptionDefaultValues = new Boolean[] { Boolean.TRUE, // fDisplayHierAlphanumericIdDbBEUseCase
                Boolean.TRUE, // fDisplayHierNumericIdDbBEUseCase
                Boolean.TRUE, // fDisplayQualiferDbBEActor,
                Boolean.TRUE, // fDisplayQualiferDbBEFlow,
                Boolean.TRUE, // fDisplayLabelDbBEFlow,
                Boolean.TRUE, // fDisplayQualiferDbBEStore,
                Boolean.TRUE, // fDisplayQualiferDbBEUseCase,
                Boolean.FALSE, // fDisplayStereotypeIcon,
                Boolean.TRUE, // fDisplayStereotypeName,
                Boolean.FALSE, // fDisplayStereotypeOnly,
        };

        // Font options
        DbBEStyle.be_fontOptions = new MetaField[] { DbBEStyle.fFrameHeaderFont,
                DbBEStyle.fAlphanumericIdentifierFontDbBEUseCase,
                DbBEStyle.fNumericIdentifierFontDbBEUseCase, DbBEStyle.fNameFontDbBEUseCase,
                DbBEStyle.fAliasFontDbBEUseCase, DbBEStyle.fPhysicalNameFontDbBEUseCase,
                DbBEStyle.fSynchronizationRuleFontDbBEUseCase,
                DbBEStyle.fDescriptionFontDbBEUseCase, DbBEStyle.fFixedCostFontDbBEUseCase,
                DbBEStyle.fFixedTimeFontDbBEUseCase, DbBEStyle.fPartialCostFontDbBEUseCase,
                DbBEStyle.fPartialTimeFontDbBEUseCase, DbBEStyle.fResourcesFontDbBEUseCase,
                DbBEStyle.fResourceCostFontDbBEUseCase, DbBEStyle.fResourceTimeFontDbBEUseCase,
                DbBEStyle.fSynchronizationRuleFontDbBEUseCase, DbBEStyle.fTotalCostFontDbBEUseCase,
                DbBEStyle.fTotalTimeFontDbBEUseCase, DbBEStyle.fUdfFontDbBEUseCase,
                DbBEStyle.fNameFontDbBEStore, DbBEStyle.fIdentifierFontDbBEStore,
                DbBEStyle.fAliasFontDbBEStore, DbBEStyle.fPhysicalNameFontDbBEStore,
                DbBEStyle.fDescriptionFontDbBEStore, DbBEStyle.fVolumeFontDbBEStore,
                DbBEStyle.fUdfFontDbBEStore, DbBEStyle.fNameFontDbBEActor,
                DbBEStyle.fIdentifierFontDbBEActor, DbBEStyle.fAliasFontDbBEActor,
                DbBEStyle.fPhysicalNameFontDbBEActor, DbBEStyle.fDescriptionFontDbBEActor,
                DbBEStyle.fDefinitionFontDbBEActor, DbBEStyle.fUdfFontDbBEActor,
                DbBEStyle.fIdentifierFontDbBEFlow, DbBEStyle.fNameFontDbBEFlow,
                DbBEStyle.fAliasFontDbBEFlow, DbBEStyle.fPhysicalNameFontDbBEFlow,
                DbBEStyle.fFrequencyFontDbBEFlow, DbBEStyle.fEmissionConditionFontDbBEFlow,
                DbBEStyle.fDescriptionFontDbBEFlow, DbBEStyle.fUdfFontDbBEFlow, };

        Font plainFont = new Font("Arial", Font.PLAIN, 8);
        //Font italicFont = new Font("Arial", Font.ITALIC, 8);
        DbBEStyle.be_fontOptionDefaultValues = new Font[] { plainFont, // fFrameHeaderFont,
                plainFont, // fAlphanumericIdentifierFontDbBEUseCase,
                plainFont, // fNumericIdentifierFontDbBEUseCase,
                plainFont, // fNameFontDbBEUseCase,
                plainFont, // fAliasFontDbBEUseCase,
                plainFont, // fPhysicalNameFontDbBEUseCase,
                plainFont, // fSynchronizationRuleFontDbBEUseCase,
                plainFont, // fDescriptionFontDbBeUseCase,
                plainFont, // fFixedCostFontDbBEUseCase,
                plainFont, // fFixedTimeFontDbBEUseCase,
                plainFont, // fPartialCostFontDbBEUseCase,
                plainFont, // fPartialTimeFontDbBEUseCase,
                plainFont, // fResourcesFontDbBEUseCase,
                plainFont, // fResourceCostFontDbBEUseCase,
                plainFont, // fResourceTimeFontDbBEUseCase,
                plainFont, // fSynchronizationRuleFontDbBEUseCase,
                plainFont, // fTotalCostFontDbBEUseCase,
                plainFont, // fTotalTimeFontDbBEUseCase,
                plainFont, // fUdfFontDbBeUseCase,
                plainFont, // fNameFontDbBEStore,
                plainFont, // fIdentifierFontDbBEStore,
                plainFont, // fAliasFontDbBEStore,
                plainFont, // fPhysicalNameFontDbBEStore,
                plainFont, // fDescriptionFontDbBeStore,
                plainFont, // fVolumeFontDbBeStore,
                plainFont, // fUdfFontDbBeStore,
                plainFont, // fNameFontDbBEActor,
                plainFont, // fIdentifierFontDbBEActor,
                plainFont, // fAliasFontDbBEActor,
                plainFont, // fPhysicalNameFontDbBEActor,
                plainFont, // fDescriptionFontDbBeActor,
                plainFont, // fDefinitionFontDbBeActor,
                plainFont, // fUdfFontDbBeActor,
                plainFont, // fIdentifierFontDbBEFlow,
                plainFont, // fNameFontDbBEFlow,
                plainFont, // fAliasFontDbBEFlow,
                plainFont, // fPhysicalNameFontDbBEFlow,
                plainFont, // fFrequencyFontDbBEFlow,
                plainFont, // fEmissionConditionFontDbBEFlow,
                plainFont, // fDescriptionFontDbBeFlow,
                plainFont, // fUdfFontDbBeFlow,
        };

        // Color options
        DbBEStyle.be_colorOptions = new MetaField[] { DbBEStyle.fBackgroundColorDbBEActor,
                DbBEStyle.fLineColorDbBEActor, DbBEStyle.fTextColorDbBEActor,
                DbBEStyle.fLineColorDbBEFlow, DbBEStyle.fBackgroundColorDbBEContextGo,
                DbBEStyle.fLineColorDbBEContextGo, DbBEStyle.fTextColorDbBEContextGo,
                DbBEStyle.fBackgroundColorDbBEStore, DbBEStyle.fLineColorDbBEStore,
                DbBEStyle.fTextColorDbBEStore, DbBEStyle.fBackgroundColorDbBEUseCase,
                DbBEStyle.fLineColorDbBEUseCase, DbBEStyle.fTextColorDbBEUseCase,
                DbBEStyle.fLineColorDbBEContextCell, DbBEStyle.fTextColorDbSMSNotice,
                DbBEStyle.fBackgroundColorDbSMSNotice, DbBEStyle.fLineColorDbSMSNotice,
                DbSMSStyle.fLineColorDbSMSGraphicalLink };

        Color darkGreen = new Color(0, 153, 153);
        Color lightGreen = new Color(223, 255, 233);
        Color darkMauve = new Color(5, 5, 161);
        Color lightMagenta = new Color(240, 240, 255);
        Color transparentWhite = new Color(255, 255, 255, 100);
        Color lightYellow = new Color(255, 255, 204);

        DbBEStyle.be_colorOptionDefaultValues = new Color[] { Color.lightGray, // fBackgroundColorDbBEActor
                Color.black, // fLineColorDbBEActor
                Color.black, // fTextColorDbBEActor
                Color.black, // fLineColorDbBEFlow
                transparentWhite, // fBackgroundColorDbBEContextGo,
                Color.black, // fLineColorDbBEContextGo,
                Color.black, // fTextColorDbBEContextGo,
                lightMagenta, // fBackgroundColorDbBEStore
                darkMauve, // fLineColorDbBEStore
                Color.black, // fTextColorDbBEStore
                lightGreen, // fBackgroundColorDbBEUseCase
                darkGreen, // fLineColorDbBEUseCase
                Color.black, // fTextColorDbBEUseCase
                Color.gray, // fLineColorDbBEContextCell
                Color.black, // fTextColorDbSMSNotice
                lightYellow, // fBackgroundColorDbSMSNotice
                Color.black, // fLineColorDbSMSNotice
                Color.black, // fLineColorDbSMSGraphicalLink,
        };

        // Line Style Options
        DbBEStyle.be_lineOptions = new MetaField[] { DbBEStyle.fDashStyleDbBEActor,
                DbBEStyle.fHighlightDbBEActor, DbBEStyle.fDashStyleDbBEFlow,
                DbBEStyle.fHighlightDbBEFlow, DbBEStyle.fDashStyleDbBEContextGo,
                DbBEStyle.fHighlightDbBEContextGo, DbBEStyle.fDashStyleDbBEStore,
                DbBEStyle.fHighlightDbBEStore, DbBEStyle.fDashStyleDbBEUseCase,
                DbBEStyle.fHighlightDbBEUseCase, DbBEStyle.fDashStyleDbSMSNotice,
                DbBEStyle.fHighlightDbSMSNotice, DbSMSStyle.fDashStyleDbSMSGraphicalLink,
                DbSMSStyle.fHighlightDbSMSGraphicalLink };

        DbBEStyle.be_lineOptionDefaultValues = new Boolean[] { Boolean.FALSE, // fDashStyleDbBEActor
                Boolean.FALSE, // fHighlightDbBEActor
                Boolean.FALSE, // fDashStyleDbBEFlow
                Boolean.FALSE, // fHighlightDbBEFlow
                Boolean.FALSE, // fDashStyleDbBEContextGo,
                Boolean.TRUE, // fHighlightDbBEContextGo,
                Boolean.FALSE, // fDashStyleDbBEStore
                Boolean.FALSE, // fHighlightDbBEStore
                Boolean.FALSE, // fDashStyleDbBEUseCase
                Boolean.FALSE, // fHighlightDbBEUseCase
                Boolean.FALSE, // fDashStyleDbSMSNotice
                Boolean.FALSE, // fHighlightDbSMSNotice
                Boolean.TRUE, // fDashStyleDbSMSGraphicalLink
                Boolean.FALSE, // fHighlightDbSMSGraphicalLink
        };

        DbBEStyle.be_listOptionTabs = new String[] { "be_optionGroups", "be_optionValueGroups",
                "be_optionGroupHeaders" };

        DbBEStyle.be_optionGroups = new MetaField[][] { DbBEStyle.be_displayOptions,
                DbBEStyle.be_fontOptions, DbBEStyle.be_colorOptions, DbBEStyle.be_lineOptions };

        DbBEStyle.be_optionValueGroups = new Object[][] { DbBEStyle.be_displayOptionDefaultValues,
                DbBEStyle.be_fontOptionDefaultValues, DbBEStyle.be_colorOptionDefaultValues,
                DbBEStyle.be_lineOptionDefaultValues };

        DbBEStyle.be_optionGroupHeaders = new String[] {
                org.modelsphere.sms.be.international.LocaleMgr.screen.getString("StructureDisplay"),
                org.modelsphere.sms.be.international.LocaleMgr.screen.getString("Font"),
                org.modelsphere.sms.be.international.LocaleMgr.screen.getString("Color"),
                org.modelsphere.sms.be.international.LocaleMgr.screen.getString("LineStyle") };
    } // end initBeStyle()

    private static Map<DbSMSProject, List<DbBENotation>> g_projectBPMNotationMap = new HashMap<DbSMSProject, List<DbBENotation>>();
    private static Map<DbSMSProject, List<DbBENotation>> g_projectBPMUmlNotationMap = new HashMap<DbSMSProject, List<DbBENotation>>();

    public static List<DbBENotation> getCommonNotations(DbSMSProject thisProject)
            throws DbException {
        initBeNotations(thisProject);
        List<DbBENotation> commonNotations = (List<DbBENotation>) g_projectBPMNotationMap
                .get(thisProject);
        if (commonNotations == null) {
            commonNotations = new ArrayList<DbBENotation>();
        }

        if (commonNotations.isEmpty()) {
            DbRelationN relN = thisProject.getComponents();
            DbEnumeration dbEnum = relN.elements(DbBENotation.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbBENotation notation = (DbBENotation) dbEnum.nextElement();

                if (notation.equals(thisProject.getBeDefaultNotation()))
                    continue;

                if (commonNotations.contains(notation))
                    continue;

            } // end while
            dbEnum.close();
        }

        return commonNotations;
    } // end getCommonNotations()

    public static List<DbBENotation> getCommonUmlNotations(DbSMSProject thisProject)
            throws DbException {
        initBeNotations(thisProject);
        List<DbBENotation> commonNotations = (List<DbBENotation>) g_projectBPMUmlNotationMap
                .get(thisProject);
        if (commonNotations == null) {
            commonNotations = new ArrayList<DbBENotation>();
        }

        if (commonNotations.isEmpty()) {
            DbRelationN relN = thisProject.getComponents();
            DbEnumeration dbEnum = relN.elements(DbBENotation.metaClass);

            while (dbEnum.hasMoreElements()) {
                DbBENotation notation = (DbBENotation) dbEnum.nextElement();

                if (notation.equals(thisProject.getBeDefaultNotation()))
                    continue;

                if (commonNotations.contains(notation))
                    continue;

                if (notation.isBuiltIn()) {
                    int notaId = notation.getNotationID().intValue();
                    switch (notaId) {
                    case DbInitialization.UML_USE_CASE: {
                        commonNotations.add(notation);
                    }
                        break;
                    case DbInitialization.UML_SEQUENCE_DIAGRAM: {
                        commonNotations.add(notation);
                    }
                        break;
                    case DbInitialization.UML_ACTIVITY_DIAGRAM: {
                        commonNotations.add(notation);
                    }
                        break;
                    case DbInitialization.UML_STATE_DIAGRAM: {
                        commonNotations.add(notation);
                    }
                        break;
                    case DbInitialization.UML_COLLABORATION_DIAGRAM: {
                        commonNotations.add(notation);
                    }
                        break;
                    case DbInitialization.UML_DEPLOYMENT_DIAGRAM: {
                        commonNotations.add(notation);
                    }
                        break;
                    case DbInitialization.UML_COMPONENT_DIAGRAM: {
                        commonNotations.add(notation);
                    }
                        break;
                    default: {
                        // do nothing
                    }
                        break;

                    }
                } // end if
            } // end while
            dbEnum.close();
        }

        return commonNotations;
    } // end getCommonNotations()

    public static int getNextNewNotationId() throws DbException {
        int nextId = 0;
        int lastId = NEW_NOTATION_START_ID;

        DbProject project = ApplicationContext.getFocusManager().getCurrentProject();
        DbRelationN relN = project.getComponents();
        DbEnumeration dbEnum = relN.elements(DbSMSNotation.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSNotation notation = (DbSMSNotation) dbEnum.nextElement();
            if (notation.getNotationID().intValue() >= lastId) {
                lastId += 1;
            }
            nextId = lastId;
        } // end while
        dbEnum.close();
        return nextId;
    }// end getNextNewNotationId

    public static void initCellValues(DbBEContextCell cell) throws DbException {
        cell.setXweight(new Double(1.0));
        cell.setYweight(new Double(1.0));
        SMSVerticalAlignment verticalAlign = convertToVerticalAlignment(0);
        SMSHorizontalAlignment horizontalAlign = convertToHorizontalAlignment(0);
        cell.setVerticalJustification2(verticalAlign);
        cell.setHorizontalJustification2(horizontalAlign);
        cell.setFont(new Font("Arial", Font.PLAIN, 8));
        cell.setRightBorder(Boolean.TRUE);
        cell.setBottomBorder(Boolean.TRUE);
    } // end initCellValues()

    public static SMSHorizontalAlignment convertToHorizontalAlignment(int horizontalJustification) {
        SMSHorizontalAlignment horizontalAlign = SMSHorizontalAlignment
                .getInstance(SMSHorizontalAlignment.CENTER);

        switch (horizontalJustification) {
        case TableCell.START:
            horizontalAlign = SMSHorizontalAlignment.getInstance(SMSHorizontalAlignment.LEFT);
            break;
        case TableCell.END:
            horizontalAlign = SMSHorizontalAlignment.getInstance(SMSHorizontalAlignment.RIGHT);
            break;
        } // end switch

        return horizontalAlign;
    } // end convertToHorizontalAlignment()

    public static SMSVerticalAlignment convertToVerticalAlignment(int verticalJustification) {
        SMSVerticalAlignment verticalAlign = SMSVerticalAlignment
                .getInstance(SMSVerticalAlignment.CENTER);

        switch (verticalJustification) {
        case TableCell.START:
            verticalAlign = SMSVerticalAlignment.getInstance(SMSVerticalAlignment.TOP);
            break;
        case TableCell.END:
            verticalAlign = SMSVerticalAlignment.getInstance(SMSVerticalAlignment.BOTTOM);
            break;
        } // end switch

        return verticalAlign;
    } // end convertToVerticalAlignment()

    public static int convertToHorizontalAlignment(SMSHorizontalAlignment horizontalAlign) {
        int horizontal = TableCell.MIDDLE;

        switch (horizontalAlign.getValue()) {
        case SMSHorizontalAlignment.LEFT:
            horizontal = TableCell.START;
            break;
        case SMSHorizontalAlignment.RIGHT:
            horizontal = TableCell.END;
            break;
        }

        return horizontal;
    }

    public static int convertToVerticalAlignment(SMSVerticalAlignment verticalAlign) {
        int vertical = TableCell.MIDDLE;

        switch (verticalAlign.getValue()) {
        case SMSVerticalAlignment.TOP:
            vertical = TableCell.START;
            break;
        case SMSVerticalAlignment.BOTTOM:
            vertical = TableCell.END;
            break;
        }

        return vertical;
    }

    public static void initBeNotations(DbSMSProject thisProject) throws DbException {
        if (thisProject.getBeDefaultNotation() == null) {
            // store common notation for this project here
            g_projectBPMNotationMap.put(thisProject, new ArrayList<DbBENotation>());
            g_projectBPMUmlNotationMap.put(thisProject, new ArrayList<DbBENotation>());

            // Datarun BPM
            {
                DbBENotation datarunBPMNotation = new DbBENotation(thisProject);
                datarunBPMNotation.setName(DbBENotation.DATARUN_BPM);
                datarunBPMNotation.setTerminologyName(DbBENotation.DATARUN_BPM);
                datarunBPMNotation.setNotationID(new Integer(DATARUN_BPM));
                datarunBPMNotation.setMasterNotationID(new Integer(DATARUN_BPM));
                datarunBPMNotation.setNotationMode(new Integer(DbSMSNotation.BE_MODE));
                datarunBPMNotation.setBuiltIn(Boolean.TRUE);
                datarunBPMNotation.setHasFrame(Boolean.FALSE);
                // UseCase
                datarunBPMNotation.setUseCaseShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE));
                datarunBPMNotation.setUseCaseZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(datarunBPMNotation, DbBEUseCase.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), true,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                // ZONE 2
                new DbBESingleZoneDisplay(datarunBPMNotation, true, ZoneJustification
                        .getInstance(ZoneJustification.CENTER), true, BEZoneStereotype
                        .getInstance(BEZoneStereotype.USECASE));
                // ZONE 3
                new DbBESingleZoneDisplay(datarunBPMNotation, DbBEUseCaseResource.fResource, true,
                        ZoneJustification.getInstance(ZoneJustification.RIGHT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                datarunBPMNotation.setUseCaseDefaultWidth(new Integer(95));
                datarunBPMNotation.setUseCaseDefaultHeight(new Integer(95));
                // Store
                datarunBPMNotation.setStoreShape(SMSNotationShape
                        .getInstance(SMSNotationShape.OPENED_RECTANGLE_LEFT_RIGHT));
                datarunBPMNotation.setStoreZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(datarunBPMNotation, DbBEStore.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), true,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                datarunBPMNotation.setStoreDefaultWidth(new Integer(160));
                datarunBPMNotation.setStoreDefaultHeight(new Integer(30));
                datarunBPMNotation.setStoreIdPrefix(DbBENotation.STORE_ID_PREFIX);
                // Actor
                datarunBPMNotation.setActorShape(SMSNotationShape
                        .getInstance(SMSNotationShape.SHADOW_RECTANGLE));
                datarunBPMNotation.setActorZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(datarunBPMNotation, DbBEActor.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                datarunBPMNotation.setActorDefaultWidth(new Integer(95));
                datarunBPMNotation.setActorDefaultHeight(new Integer(95));
                datarunBPMNotation.setActorIdPrefix(DbBENotation.ACTOR_ID_PREFIX);
                // FLOW
                // ZONE 1
                new DbBESingleZoneDisplay(datarunBPMNotation, DbBEFlow.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                // ZONE 2
                new DbBESingleZoneDisplay(datarunBPMNotation, DbBEFlow.fEmissionCondition, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                datarunBPMNotation.setFlowIdPrefix(DbBENotation.FLOW_ID_PREFIX);
            }

            // Datarun SPM
            {
                DbBENotation datarunSPMNotation = new DbBENotation(thisProject);
                datarunSPMNotation.setName(DbBENotation.DATARUN_SPM);
                datarunSPMNotation.setTerminologyName(DbBENotation.DATARUN_SPM);
                datarunSPMNotation.setNotationID(new Integer(DATARUN_SPM));
                datarunSPMNotation.setMasterNotationID(new Integer(DATARUN_SPM));
                datarunSPMNotation.setNotationMode(new Integer(DbSMSNotation.BE_MODE));
                datarunSPMNotation.setBuiltIn(Boolean.TRUE);
                datarunSPMNotation.setHasFrame(Boolean.TRUE);
                // UseCase
                datarunSPMNotation.setUseCaseShape(SMSNotationShape
                        .getInstance(SMSNotationShape.ROUND_RECTANGLE));
                datarunSPMNotation.setUseCaseZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(datarunSPMNotation, DbBEUseCase.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), true,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                // ZONE 2
                new DbBESingleZoneDisplay(datarunSPMNotation, DbBEUseCase.fAlphanumericIdentifier,
                        true, ZoneJustification.getInstance(ZoneJustification.LEFT), true,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                // ZONE 3
                new DbBESingleZoneDisplay(datarunSPMNotation, DbBEUseCaseResource.fResource, true,
                        ZoneJustification.getInstance(ZoneJustification.RIGHT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                datarunSPMNotation.setUseCaseDefaultWidth(new Integer(95));
                datarunSPMNotation.setUseCaseDefaultHeight(new Integer(95));
                // Store
                datarunSPMNotation.setStoreShape(SMSNotationShape
                        .getInstance(SMSNotationShape.OPENED_RECTANGLE_RIGHT));
                datarunSPMNotation.setStoreZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(datarunSPMNotation, DbBEStore.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), true,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                datarunSPMNotation.setStoreDefaultWidth(new Integer(160));
                datarunSPMNotation.setStoreDefaultHeight(new Integer(30));
                datarunSPMNotation.setStoreIdPrefix(DbBENotation.STORE_ID_PREFIX);
                // Actor
                datarunSPMNotation.setActorShape(SMSNotationShape
                        .getInstance(SMSNotationShape.SHADOW_RECTANGLE));
                datarunSPMNotation.setActorZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(datarunSPMNotation, DbBEActor.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                datarunSPMNotation.setActorDefaultWidth(new Integer(95));
                datarunSPMNotation.setActorDefaultHeight(new Integer(95));
                datarunSPMNotation.setActorIdPrefix(DbBENotation.ACTOR_ID_PREFIX);
                // FLOW
                // ZONE 1
                new DbBESingleZoneDisplay(datarunSPMNotation, DbBEFlow.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                // ZONE 2
                new DbBESingleZoneDisplay(datarunSPMNotation, DbBEFlow.fEmissionCondition, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                datarunSPMNotation.setFlowIdPrefix(DbBENotation.FLOW_ID_PREFIX);
            }

            // Datarun ISA
            {
                DbBENotation datarunISANotation = new DbBENotation(thisProject);
                datarunISANotation.setName(DbBENotation.DATARUN_ISA);
                datarunISANotation.setTerminologyName(DbBENotation.DATARUN_ISA);
                datarunISANotation.setNotationID(new Integer(DATARUN_ISA));
                datarunISANotation.setMasterNotationID(new Integer(DATARUN_ISA));
                datarunISANotation.setNotationMode(new Integer(DbSMSNotation.BE_MODE));
                datarunISANotation.setBuiltIn(Boolean.TRUE);
                datarunISANotation.setHasFrame(Boolean.TRUE);
                // UseCase
                datarunISANotation.setUseCaseShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE));
                datarunISANotation.setUseCaseZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(datarunISANotation, DbBEUseCase.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), true,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                datarunISANotation.setUseCaseDefaultWidth(new Integer(95));
                datarunISANotation.setUseCaseDefaultHeight(new Integer(95));
                // Store
                datarunISANotation.setStoreShape(SMSNotationShape
                        .getInstance(SMSNotationShape.OPENED_RECTANGLE_RIGHT));
                datarunISANotation.setStoreZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                datarunISANotation.setStoreDefaultWidth(new Integer(160));
                datarunISANotation.setStoreDefaultHeight(new Integer(30));
                datarunISANotation.setStoreIdPrefix(DbBENotation.STORE_ID_PREFIX);
                // Actor
                datarunISANotation.setActorShape(SMSNotationShape
                        .getInstance(SMSNotationShape.SHADOW_RECTANGLE));
                datarunISANotation.setActorZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                datarunISANotation.setActorDefaultWidth(new Integer(95));
                datarunISANotation.setActorDefaultHeight(new Integer(95));
                datarunISANotation.setActorIdPrefix(DbBENotation.ACTOR_ID_PREFIX);
                // FLOW
                // ZONE 1
                new DbBESingleZoneDisplay(datarunISANotation, DbBEFlow.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                datarunISANotation.setFlowIdPrefix(DbBENotation.FLOW_ID_PREFIX);
            }

            // Gane-Sarson
            {
                DbBENotation ganeSarsonNotation = new DbBENotation(thisProject);
                ganeSarsonNotation.setName(DbBENotation.GANE_SARSON);
                ganeSarsonNotation.setTerminologyName(DbBENotation.GANE_SARSON);
                ganeSarsonNotation.setNotationID(new Integer(GANE_SARSON));
                ganeSarsonNotation.setMasterNotationID(new Integer(GANE_SARSON));
                ganeSarsonNotation.setNotationMode(new Integer(DbSMSNotation.BE_MODE));
                ganeSarsonNotation.setBuiltIn(Boolean.TRUE);
                ganeSarsonNotation.setHasFrame(Boolean.TRUE);
                // UseCase
                ganeSarsonNotation.setUseCaseShape(SMSNotationShape
                        .getInstance(SMSNotationShape.ROUND_RECTANGLE));
                ganeSarsonNotation.setUseCaseZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(ganeSarsonNotation, DbBEUseCase.fNumericIdentifier, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), true,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                // ZONE 2
                new DbBESingleZoneDisplay(ganeSarsonNotation, DbBEUseCase.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                new DbBESingleZoneDisplay(ganeSarsonNotation, DbBEUseCase.fAlias, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                new DbBESingleZoneDisplay(ganeSarsonNotation, DbBEUseCase.fPhysicalName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), true,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                // ZONE 3
                new DbBESingleZoneDisplay(ganeSarsonNotation, DbBEUseCaseResource.fResource, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                ganeSarsonNotation.setUseCaseDefaultWidth(new Integer(95));
                ganeSarsonNotation.setUseCaseDefaultHeight(new Integer(95));
                // Store
                ganeSarsonNotation.setStoreShape(SMSNotationShape
                        .getInstance(SMSNotationShape.OPENED_RECTANGLE_RIGHT));
                ganeSarsonNotation.setStoreZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.PERPENDICULAR));
                // ZONE 1
                new DbBESingleZoneDisplay(ganeSarsonNotation, DbBEStore.fIdentifier, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), true,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                // ZONE 2
                new DbBESingleZoneDisplay(ganeSarsonNotation, DbBEStore.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                ganeSarsonNotation.setStoreDefaultWidth(new Integer(160));
                ganeSarsonNotation.setStoreDefaultHeight(new Integer(30));
                ganeSarsonNotation.setStoreIdPrefix(DbBENotation.STORE_ID_PREFIX);
                // Actor
                ganeSarsonNotation.setActorShape(SMSNotationShape
                        .getInstance(SMSNotationShape.SHADOW_RECTANGLE));
                ganeSarsonNotation.setActorZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(ganeSarsonNotation, DbBEActor.fIdentifier, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                // ZONE 2
                new DbBESingleZoneDisplay(ganeSarsonNotation, DbBEActor.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                new DbBESingleZoneDisplay(ganeSarsonNotation, DbBEActor.fAlias, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                new DbBESingleZoneDisplay(ganeSarsonNotation, DbBEActor.fPhysicalName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                ganeSarsonNotation.setActorDefaultWidth(new Integer(95));
                ganeSarsonNotation.setActorDefaultHeight(new Integer(95));
                ganeSarsonNotation.setActorIdPrefix(DbBENotation.ACTOR_ID_PREFIX);
                // FLOW
                // ZONE 1
                new DbBESingleZoneDisplay(ganeSarsonNotation, DbBEFlow.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                ganeSarsonNotation.setFlowIdPrefix(DbBENotation.FLOW_ID_PREFIX);
            }

            // Merise
            {
                DbBENotation meriseNotation = new DbBENotation(thisProject);
                meriseNotation.setName(DbBENotation.MERISE);
                meriseNotation.setTerminologyName(DbBENotation.MERISE);
                meriseNotation.setNotationID(new Integer(MERISE));
                meriseNotation.setMasterNotationID(new Integer(MERISE));
                meriseNotation.setNotationMode(new Integer(DbSMSNotation.BE_MODE));
                meriseNotation.setBuiltIn(Boolean.TRUE);
                meriseNotation.setHasFrame(Boolean.TRUE);
                // UseCase
                meriseNotation.setUseCaseShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE_WITH_REVERSED_TRIANGLE));
                meriseNotation.setUseCaseZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(meriseNotation, DbBEUseCase.fSynchronizationRule, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), true,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                // ZONE 2
                new DbBESingleZoneDisplay(meriseNotation, DbBEUseCase.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), true,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                // ZONE 3
                new DbBESingleZoneDisplay(meriseNotation, DbBEUseCase.fNumericIdentifier, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                meriseNotation.setUseCaseDefaultWidth(new Integer(95));
                meriseNotation.setUseCaseDefaultHeight(new Integer(95));
                // Store
                meriseNotation.setStoreShape(SMSNotationShape
                        .getInstance(SMSNotationShape.OPENED_RECTANGLE_RIGHT));
                meriseNotation.setStoreZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.PERPENDICULAR));
                // ZONE 1
                new DbBESingleZoneDisplay(meriseNotation, true, ZoneJustification
                        .getInstance(ZoneJustification.LEFT), true, BEZoneStereotype
                        .getInstance(BEZoneStereotype.STORE));
                // ZONE 2
                new DbBESingleZoneDisplay(meriseNotation, DbBEStore.fName, true, ZoneJustification
                        .getInstance(ZoneJustification.CENTER), false, BEZoneStereotype
                        .getInstance(BEZoneStereotype.STORE));
                meriseNotation.setStoreDefaultWidth(new Integer(160));
                meriseNotation.setStoreDefaultHeight(new Integer(30));
                meriseNotation.setStoreIdPrefix(DbBENotation.STORE_ID_PREFIX);
                // Actor
                meriseNotation.setActorShape(SMSNotationShape.getInstance(SMSNotationShape.OVALE));
                meriseNotation.setActorZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(meriseNotation, DbBEActor.fName, true, ZoneJustification
                        .getInstance(ZoneJustification.CENTER), false, BEZoneStereotype
                        .getInstance(BEZoneStereotype.ACTOR));
                meriseNotation.setActorDefaultWidth(new Integer(95));
                meriseNotation.setActorDefaultHeight(new Integer(95));
                meriseNotation.setActorIdPrefix(DbBENotation.ACTOR_ID_PREFIX);
                // FLOW
                // ZONE 1
                new DbBESingleZoneDisplay(meriseNotation, DbBEFlow.fName, true, ZoneJustification
                        .getInstance(ZoneJustification.LEFT), false, BEZoneStereotype
                        .getInstance(BEZoneStereotype.FLOW));
                // ZONE 2
                new DbBESingleZoneDisplay(meriseNotation, DbBEFlow.fEmissionCondition, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                meriseNotation.setFlowIdPrefix(DbBENotation.FLOW_ID_PREFIX);
            }

            // Yourdon DeMarco
            {
                DbBENotation yourdonDeMarcoNotation = new DbBENotation(thisProject);
                yourdonDeMarcoNotation.setName(DbBENotation.YOURDON_DEMARCO);
                yourdonDeMarcoNotation.setTerminologyName(DbBENotation.YOURDON_DEMARCO);
                yourdonDeMarcoNotation.setNotationID(new Integer(YOURDON_DEMARCO));
                yourdonDeMarcoNotation.setMasterNotationID(new Integer(YOURDON_DEMARCO));
                yourdonDeMarcoNotation.setNotationMode(new Integer(DbSMSNotation.BE_MODE));
                yourdonDeMarcoNotation.setBuiltIn(Boolean.TRUE);
                yourdonDeMarcoNotation.setHasFrame(Boolean.FALSE);
                // UseCase
                yourdonDeMarcoNotation.setUseCaseShape(SMSNotationShape
                        .getInstance(SMSNotationShape.OVALE));
                yourdonDeMarcoNotation.setUseCaseZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(yourdonDeMarcoNotation, DbBEUseCase.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                yourdonDeMarcoNotation.setUseCaseDefaultWidth(new Integer(95));
                yourdonDeMarcoNotation.setUseCaseDefaultHeight(new Integer(95));
                // Store
                yourdonDeMarcoNotation.setStoreShape(SMSNotationShape
                        .getInstance(SMSNotationShape.OPENED_RECTANGLE_LEFT_RIGHT));
                yourdonDeMarcoNotation.setStoreZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(yourdonDeMarcoNotation, DbBEStore.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                yourdonDeMarcoNotation.setStoreDefaultWidth(new Integer(160));
                yourdonDeMarcoNotation.setStoreDefaultHeight(new Integer(30));
                yourdonDeMarcoNotation.setStoreIdPrefix(DbBENotation.STORE_ID_PREFIX);
                // Actor
                yourdonDeMarcoNotation.setActorShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE));
                yourdonDeMarcoNotation.setActorZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(yourdonDeMarcoNotation, DbBEActor.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                yourdonDeMarcoNotation.setActorDefaultWidth(new Integer(95));
                yourdonDeMarcoNotation.setActorDefaultHeight(new Integer(95));
                yourdonDeMarcoNotation.setActorIdPrefix(DbBENotation.ACTOR_ID_PREFIX);
                // FLOW
                // ZONE 1
                new DbBESingleZoneDisplay(yourdonDeMarcoNotation, DbBEFlow.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                yourdonDeMarcoNotation.setFlowIdPrefix(DbBENotation.FLOW_ID_PREFIX);
            }

            // Ward Mellor
            {
                DbBENotation wardMellorNotation = new DbBENotation(thisProject);
                wardMellorNotation.setName(DbBENotation.WARD_MELLOR);
                wardMellorNotation.setTerminologyName(DbBENotation.WARD_MELLOR);
                wardMellorNotation.setNotationID(new Integer(WARD_MELLOR));
                wardMellorNotation.setMasterNotationID(new Integer(WARD_MELLOR));
                wardMellorNotation.setNotationMode(new Integer(DbSMSNotation.BE_MODE));
                wardMellorNotation.setBuiltIn(Boolean.TRUE);
                wardMellorNotation.setHasFrame(Boolean.FALSE);
                // UseCase
                wardMellorNotation.setUseCaseShape(SMSNotationShape
                        .getInstance(SMSNotationShape.OVALE));
                wardMellorNotation.setUseCaseZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(wardMellorNotation, DbBEUseCase.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                // ZONE 2
                new DbBESingleZoneDisplay(wardMellorNotation, DbBEUseCase.fNumericIdentifier, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                wardMellorNotation.setUseCaseDefaultWidth(new Integer(95));
                wardMellorNotation.setUseCaseDefaultHeight(new Integer(95));
                // Store
                wardMellorNotation.setStoreShape(SMSNotationShape
                        .getInstance(SMSNotationShape.OPENED_RECTANGLE_LEFT_RIGHT));
                wardMellorNotation.setStoreZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(wardMellorNotation, DbBEStore.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                wardMellorNotation.setStoreDefaultWidth(new Integer(160));
                wardMellorNotation.setStoreDefaultHeight(new Integer(30));
                wardMellorNotation.setStoreIdPrefix(DbBENotation.STORE_ID_PREFIX);
                // Actor
                wardMellorNotation.setActorShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE));
                wardMellorNotation.setActorZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(wardMellorNotation, DbBEActor.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                wardMellorNotation.setActorDefaultWidth(new Integer(95));
                wardMellorNotation.setActorDefaultHeight(new Integer(95));
                wardMellorNotation.setActorIdPrefix(DbBENotation.ACTOR_ID_PREFIX);
                // FLOW
                // ZONE 1
                new DbBESingleZoneDisplay(wardMellorNotation, DbBEFlow.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                wardMellorNotation.setFlowIdPrefix(DbBENotation.FLOW_ID_PREFIX);
            }

            // Functional Diagram
            {
                DbBENotation functionalDiagramNotation = new DbBENotation(thisProject);
                functionalDiagramNotation.setName(DbBENotation.FUNCTIONAL_DIAGRAM);
                functionalDiagramNotation.setTerminologyName(DbBENotation.FUNCTIONAL_DIAGRAM);
                functionalDiagramNotation.setNotationID(new Integer(FUNCTIONAL_DIAGRAM));
                functionalDiagramNotation.setMasterNotationID(new Integer(FUNCTIONAL_DIAGRAM));
                functionalDiagramNotation.setNotationMode(new Integer(DbSMSNotation.BE_MODE));
                functionalDiagramNotation.setBuiltIn(Boolean.TRUE);
                functionalDiagramNotation.setHasFrame(Boolean.TRUE);
                // UseCase
                functionalDiagramNotation.setUseCaseShape(SMSNotationShape
                        .getInstance(SMSNotationShape.ROUND_RECTANGLE));
                functionalDiagramNotation.setUseCaseZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(functionalDiagramNotation, DbBEUseCase.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                functionalDiagramNotation.setUseCaseDefaultWidth(new Integer(95));
                functionalDiagramNotation.setUseCaseDefaultHeight(new Integer(95));
                // Store
                functionalDiagramNotation.setStoreShape(SMSNotationShape
                        .getInstance(SMSNotationShape.OPENED_RECTANGLE_LEFT_RIGHT));
                functionalDiagramNotation.setStoreZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(functionalDiagramNotation, DbBEStore.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                functionalDiagramNotation.setStoreDefaultWidth(new Integer(160));
                functionalDiagramNotation.setStoreDefaultHeight(new Integer(30));
                functionalDiagramNotation.setStoreIdPrefix(DbBENotation.STORE_ID_PREFIX);
                // Actor
                functionalDiagramNotation.setActorShape(SMSNotationShape
                        .getInstance(SMSNotationShape.OVALE));
                functionalDiagramNotation.setActorZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(functionalDiagramNotation, DbBEActor.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                functionalDiagramNotation.setActorDefaultWidth(new Integer(95));
                functionalDiagramNotation.setActorDefaultHeight(new Integer(95));
                functionalDiagramNotation.setActorIdPrefix(DbBENotation.ACTOR_ID_PREFIX);
                // FLOW
                // ZONE 1
                new DbBESingleZoneDisplay(functionalDiagramNotation, DbBEFlow.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                functionalDiagramNotation.setFlowIdPrefix(DbBENotation.FLOW_ID_PREFIX);
            }

            // Merise-MCT
            {
                DbBENotation meriseMCTNotation = new DbBENotation(thisProject);
                meriseMCTNotation.setName(DbBENotation.MERISE_MCT);
                meriseMCTNotation.setTerminologyName(DbBENotation.MERISE_MCT);
                meriseMCTNotation.setNotationID(new Integer(MERISE_MCT));
                meriseMCTNotation.setMasterNotationID(new Integer(MERISE_MCT));
                meriseMCTNotation.setNotationMode(new Integer(DbSMSNotation.BE_MODE));
                meriseMCTNotation.setBuiltIn(Boolean.TRUE);
                meriseMCTNotation.setHasFrame(Boolean.TRUE);
                // UseCase
                meriseMCTNotation.setUseCaseShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE_WITH_REVERSED_TRIANGLE));
                meriseMCTNotation.setUseCaseZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(meriseMCTNotation, DbBEUseCase.fSynchronizationRule,
                        true, ZoneJustification.getInstance(ZoneJustification.CENTER), true,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                // ZONE 2
                new DbBESingleZoneDisplay(meriseMCTNotation, DbBEUseCase.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), true,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                // ZONE 3
                new DbBESingleZoneDisplay(meriseMCTNotation, DbBEUseCase.fNumericIdentifier, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                meriseMCTNotation.setUseCaseDefaultWidth(new Integer(95));
                meriseMCTNotation.setUseCaseDefaultHeight(new Integer(95));
                // Store
                meriseMCTNotation.setStoreShape(SMSNotationShape
                        .getInstance(SMSNotationShape.ROUND_RECTANGLE));
                meriseMCTNotation.setStoreZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(meriseMCTNotation, DbBEStore.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                meriseMCTNotation.setStoreDefaultWidth(new Integer(160));
                meriseMCTNotation.setStoreDefaultHeight(new Integer(50));
                meriseMCTNotation.setStoreIdPrefix(DbBENotation.STORE_ID_PREFIX);
                // Actor
                meriseMCTNotation.setActorShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE_WITH_ACTOR));
                meriseMCTNotation.setActorZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(meriseMCTNotation, DbBEActor.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                meriseMCTNotation.setActorDefaultWidth(new Integer(130));
                meriseMCTNotation.setActorDefaultHeight(new Integer(95));
                meriseMCTNotation.setActorIdPrefix(DbBENotation.ACTOR_ID_PREFIX);
                // FLOW
                // ZONE 1
                new DbBESingleZoneDisplay(meriseMCTNotation, DbBEFlow.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                // ZONE 2
                new DbBESingleZoneDisplay(meriseMCTNotation, DbBEFlow.fEmissionCondition, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                meriseMCTNotation.setFlowIdPrefix(DbBENotation.FLOW_ID_PREFIX);
            }

            // Merise Flow Schema
            {
                DbBENotation meriseFlowSchemaNotation = new DbBENotation(thisProject);
                meriseFlowSchemaNotation.setName(DbBENotation.MERISE_FLOW_SCHEMA);
                meriseFlowSchemaNotation.setTerminologyName(DbBENotation.MERISE_FLOW_SCHEMA);
                meriseFlowSchemaNotation.setNotationID(new Integer(MERISE_FLOW_SCHEMA));
                meriseFlowSchemaNotation.setMasterNotationID(new Integer(MERISE_FLOW_SCHEMA));
                meriseFlowSchemaNotation.setNotationMode(new Integer(DbSMSNotation.BE_MODE));
                meriseFlowSchemaNotation.setBuiltIn(Boolean.TRUE);
                meriseFlowSchemaNotation.setHasFrame(Boolean.TRUE);
                // UseCase
                meriseFlowSchemaNotation.setUseCaseShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE));
                meriseFlowSchemaNotation.setUseCaseZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(meriseFlowSchemaNotation,
                        DbBEUseCase.fSynchronizationRule, true, ZoneJustification
                                .getInstance(ZoneJustification.CENTER), true, BEZoneStereotype
                                .getInstance(BEZoneStereotype.USECASE));
                // ZONE 2
                new DbBESingleZoneDisplay(meriseFlowSchemaNotation, DbBEUseCase.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), true,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                // ZONE 3
                new DbBESingleZoneDisplay(meriseFlowSchemaNotation, DbBEUseCase.fNumericIdentifier,
                        true, ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                meriseFlowSchemaNotation.setUseCaseDefaultWidth(new Integer(95));
                meriseFlowSchemaNotation.setUseCaseDefaultHeight(new Integer(95));
                // Store
                meriseFlowSchemaNotation.setStoreShape(SMSNotationShape
                        .getInstance(SMSNotationShape.OPENED_RECTANGLE_RIGHT));
                meriseFlowSchemaNotation.setStoreZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.PERPENDICULAR));
                // ZONE 1
                new DbBESingleZoneDisplay(meriseFlowSchemaNotation, true, ZoneJustification
                        .getInstance(ZoneJustification.LEFT), true, BEZoneStereotype
                        .getInstance(BEZoneStereotype.STORE));
                // ZONE 2
                new DbBESingleZoneDisplay(meriseFlowSchemaNotation, DbBEStore.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                meriseFlowSchemaNotation.setStoreDefaultWidth(new Integer(160));
                meriseFlowSchemaNotation.setStoreDefaultHeight(new Integer(50));
                meriseFlowSchemaNotation.setStoreIdPrefix(DbBENotation.STORE_ID_PREFIX);
                // Actor
                meriseFlowSchemaNotation.setActorShape(SMSNotationShape
                        .getInstance(SMSNotationShape.OVALE));
                meriseFlowSchemaNotation.setActorZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(meriseFlowSchemaNotation, DbBEActor.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                meriseFlowSchemaNotation.setActorDefaultWidth(new Integer(95));
                meriseFlowSchemaNotation.setActorDefaultHeight(new Integer(95));
                meriseFlowSchemaNotation.setActorIdPrefix(DbBENotation.ACTOR_ID_PREFIX);
                // FLOW
                // ZONE 1
                new DbBESingleZoneDisplay(meriseFlowSchemaNotation, DbBEFlow.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                // ZONE 2
                new DbBESingleZoneDisplay(meriseFlowSchemaNotation, DbBEFlow.fEmissionCondition,
                        true, ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                meriseFlowSchemaNotation.setFlowIdPrefix(DbBENotation.FLOW_ID_PREFIX);
            }

            // Merise OOM
            {
                DbBENotation meriseOOMNotation = new DbBENotation(thisProject);
                meriseOOMNotation.setName(DbBENotation.MERISE_OOM);
                meriseOOMNotation.setTerminologyName(DbBENotation.MERISE_OOM);
                meriseOOMNotation.setNotationID(new Integer(MERISE_OOM));
                meriseOOMNotation.setMasterNotationID(new Integer(MERISE_OOM));
                meriseOOMNotation.setNotationMode(new Integer(DbSMSNotation.BE_MODE));
                meriseOOMNotation.setBuiltIn(Boolean.TRUE);
                meriseOOMNotation.setHasFrame(Boolean.FALSE);
                // UseCase
                meriseOOMNotation.setUseCaseShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE_WITH_REVERSED_TRIANGLE));
                meriseOOMNotation.setUseCaseZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(meriseOOMNotation, DbBEUseCase.fSynchronizationRule,
                        true, ZoneJustification.getInstance(ZoneJustification.CENTER), true,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                // ZONE 2
                new DbBESingleZoneDisplay(meriseOOMNotation, true, ZoneJustification
                        .getInstance(ZoneJustification.CENTER), true, BEZoneStereotype
                        .getInstance(BEZoneStereotype.USECASE));
                // ZONE 3
                new DbBESingleZoneDisplay(meriseOOMNotation, DbBEUseCaseResource.fResource, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                meriseOOMNotation.setUseCaseDefaultWidth(new Integer(150));
                meriseOOMNotation.setUseCaseDefaultHeight(new Integer(150));
                // Store
                meriseOOMNotation.setStoreShape(SMSNotationShape
                        .getInstance(SMSNotationShape.ROUND_RECTANGLE));
                meriseOOMNotation.setStoreZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(meriseOOMNotation, true, ZoneJustification
                        .getInstance(ZoneJustification.LEFT), true, BEZoneStereotype
                        .getInstance(BEZoneStereotype.STORE));
                // ZONE 2
                new DbBESingleZoneDisplay(meriseOOMNotation, DbBEStore.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                meriseOOMNotation.setStoreDefaultWidth(new Integer(75));
                meriseOOMNotation.setStoreDefaultHeight(new Integer(75));
                meriseOOMNotation.setStoreIdPrefix(DbBENotation.STORE_ID_PREFIX);
                // Actor
                meriseOOMNotation.setActorShape(SMSNotationShape
                        .getInstance(SMSNotationShape.OVALE));
                meriseOOMNotation.setActorZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(meriseOOMNotation, DbBEActor.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                meriseOOMNotation.setActorDefaultWidth(new Integer(95));
                meriseOOMNotation.setActorDefaultHeight(new Integer(75));
                meriseOOMNotation.setActorIdPrefix(DbBENotation.ACTOR_ID_PREFIX);
                // FLOW
                // ZONE 1
                new DbBESingleZoneDisplay(meriseOOMNotation, DbBEFlow.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                meriseOOMNotation.setFlowIdPrefix(DbBENotation.FLOW_ID_PREFIX);
            }

            // P+
            {
                DbBENotation pPlusNotation = new DbBENotation(thisProject);
                pPlusNotation.setName(DbBENotation.P_PLUS);
                pPlusNotation.setTerminologyName(DbBENotation.P_PLUS);
                pPlusNotation.setNotationID(new Integer(P_PLUS));
                pPlusNotation.setMasterNotationID(new Integer(P_PLUS));
                pPlusNotation.setNotationMode(new Integer(DbSMSNotation.BE_MODE));
                pPlusNotation.setBuiltIn(Boolean.TRUE);
                pPlusNotation.setHasFrame(Boolean.TRUE);
                // UseCase
                pPlusNotation.setUseCaseShape(SMSNotationShape
                        .getInstance(SMSNotationShape.ROUND_RECTANGLE));
                pPlusNotation.setUseCaseZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(pPlusNotation, DbBEUseCase.fNumericIdentifier, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), true,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                // ZONE 2
                new DbBESingleZoneDisplay(pPlusNotation, DbBEUseCase.fName, true, ZoneJustification
                        .getInstance(ZoneJustification.CENTER), false, BEZoneStereotype
                        .getInstance(BEZoneStereotype.USECASE));
                new DbBESingleZoneDisplay(pPlusNotation, DbBEUseCase.fAlias, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                new DbBESingleZoneDisplay(pPlusNotation, DbBEUseCase.fPhysicalName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), true,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                // ZONE 3
                new DbBESingleZoneDisplay(pPlusNotation, DbBEUseCaseResource.fResource, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                pPlusNotation.setUseCaseDefaultWidth(new Integer(95));
                pPlusNotation.setUseCaseDefaultHeight(new Integer(95));
                // Store
                pPlusNotation.setStoreShape(SMSNotationShape
                        .getInstance(SMSNotationShape.OPENED_RECTANGLE_RIGHT));
                pPlusNotation.setStoreZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.PERPENDICULAR));
                // ZONE 1
                new DbBESingleZoneDisplay(pPlusNotation, DbBEStore.fIdentifier, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), true,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                // ZONE 2
                new DbBESingleZoneDisplay(pPlusNotation, DbBEStore.fName, true, ZoneJustification
                        .getInstance(ZoneJustification.LEFT), false, BEZoneStereotype
                        .getInstance(BEZoneStereotype.STORE));
                pPlusNotation.setStoreDefaultWidth(new Integer(160));
                pPlusNotation.setStoreDefaultHeight(new Integer(50));
                pPlusNotation.setStoreIdPrefix(DbBENotation.STORE_ID_PREFIX);
                // Actor
                pPlusNotation.setActorShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE));
                pPlusNotation.setActorZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(pPlusNotation, DbBEActor.fIdentifier, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                // ZONE 2
                new DbBESingleZoneDisplay(pPlusNotation, DbBEActor.fName, true, ZoneJustification
                        .getInstance(ZoneJustification.CENTER), false, BEZoneStereotype
                        .getInstance(BEZoneStereotype.ACTOR));
                new DbBESingleZoneDisplay(pPlusNotation, DbBEActor.fAlias, true, ZoneJustification
                        .getInstance(ZoneJustification.CENTER), false, BEZoneStereotype
                        .getInstance(BEZoneStereotype.ACTOR));
                new DbBESingleZoneDisplay(pPlusNotation, DbBEActor.fPhysicalName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                pPlusNotation.setActorDefaultWidth(new Integer(95));
                pPlusNotation.setActorDefaultHeight(new Integer(95));
                pPlusNotation.setActorIdPrefix(DbBENotation.ACTOR_ID_PREFIX);
                // FLOW
                // ZONE 1
                new DbBESingleZoneDisplay(pPlusNotation, DbBEFlow.fName, true, ZoneJustification
                        .getInstance(ZoneJustification.LEFT), false, BEZoneStereotype
                        .getInstance(BEZoneStereotype.FLOW));
                // ZONE 2
                new DbBESingleZoneDisplay(pPlusNotation, DbBEFlow.fEmissionCondition, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                pPlusNotation.setFlowIdPrefix(DbBENotation.FLOW_ID_PREFIX);
            }

            // P+ Opal
            {
                DbBENotation pPlusOpalNotation = new DbBENotation(thisProject);
                pPlusOpalNotation.setName(DbBENotation.P_PLUS_OPAL);
                pPlusOpalNotation.setTerminologyName(DbBENotation.P_PLUS_OPAL);
                pPlusOpalNotation.setNotationID(new Integer(P_PLUS_OPAL));
                pPlusOpalNotation.setMasterNotationID(new Integer(P_PLUS_OPAL));
                pPlusOpalNotation.setNotationMode(new Integer(DbSMSNotation.BE_MODE));
                pPlusOpalNotation.setBuiltIn(Boolean.TRUE);
                pPlusOpalNotation.setHasFrame(Boolean.FALSE);
                // UseCase
                pPlusOpalNotation.setUseCaseShape(SMSNotationShape
                        .getInstance(SMSNotationShape.OVALE));
                pPlusOpalNotation.setUseCaseZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(pPlusOpalNotation, DbBEUseCase.fSynchronizationRule,
                        true, ZoneJustification.getInstance(ZoneJustification.CENTER), true,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                // ZONE 2
                new DbBESingleZoneDisplay(pPlusOpalNotation, DbBEUseCase.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                new DbBESingleZoneDisplay(pPlusOpalNotation, DbBEUseCase.fNumericIdentifier, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), true,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                // ZONE 3
                new DbBESingleZoneDisplay(pPlusOpalNotation, DbBEUseCaseResource.fResource, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                pPlusOpalNotation.setUseCaseDefaultWidth(new Integer(150));
                pPlusOpalNotation.setUseCaseDefaultHeight(new Integer(150));
                // Store
                pPlusOpalNotation.setStoreShape(SMSNotationShape
                        .getInstance(SMSNotationShape.ROUND_RECTANGLE));
                pPlusOpalNotation.setStoreZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(pPlusOpalNotation, true, ZoneJustification
                        .getInstance(ZoneJustification.CENTER), true, BEZoneStereotype
                        .getInstance(BEZoneStereotype.STORE));
                // ZONE 2
                new DbBESingleZoneDisplay(pPlusOpalNotation, DbBEStore.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                pPlusOpalNotation.setStoreDefaultWidth(new Integer(75));
                pPlusOpalNotation.setStoreDefaultHeight(new Integer(95));
                pPlusOpalNotation.setStoreIdPrefix(DbBENotation.STORE_ID_PREFIX);
                // Actor
                pPlusOpalNotation.setActorShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE));
                pPlusOpalNotation.setActorZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(pPlusOpalNotation, DbBEActor.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                pPlusOpalNotation.setActorDefaultWidth(new Integer(75));
                pPlusOpalNotation.setActorDefaultHeight(new Integer(75));
                pPlusOpalNotation.setActorIdPrefix(DbBENotation.ACTOR_ID_PREFIX);
                // FLOW
                // ZONE 1
                new DbBESingleZoneDisplay(pPlusOpalNotation, DbBEFlow.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                // ZONE 2
                new DbBESingleZoneDisplay(pPlusOpalNotation, DbBEFlow.fAlias, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                pPlusOpalNotation.setFlowIdPrefix(DbBENotation.FLOW_ID_PREFIX);
            }

            { // UML USE CASE
                DbBENotation umlUseCaseNotation = new DbBENotation(thisProject);
                umlUseCaseNotation.setName(DbBENotation.UML_USE_CASE);
                umlUseCaseNotation.setTerminologyName(DbBENotation.UML_USE_CASE);
                umlUseCaseNotation.setNotationID(new Integer(UML_USE_CASE));
                umlUseCaseNotation.setMasterNotationID(new Integer(UML_USE_CASE));
                umlUseCaseNotation.setNotationMode(new Integer(DbSMSNotation.BE_MODE));
                umlUseCaseNotation.setBuiltIn(Boolean.TRUE);
                umlUseCaseNotation.setHasFrame(Boolean.TRUE);
                // UseCase
                umlUseCaseNotation.setUseCaseShape(SMSNotationShape
                        .getInstance(SMSNotationShape.OVALE));
                umlUseCaseNotation.setUseCaseZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONES 1,2
                new DbBESingleZoneDisplay(umlUseCaseNotation, DbBEUseCase.fUmlStereotype, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                new DbBESingleZoneDisplay(umlUseCaseNotation, DbBEUseCase.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                umlUseCaseNotation.setUseCaseDefaultWidth(new Integer(125));
                umlUseCaseNotation.setUseCaseDefaultHeight(new Integer(75));
                // Store
                umlUseCaseNotation.setStoreShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE));
                umlUseCaseNotation.setStoreZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONES 1, 2
                new DbBESingleZoneDisplay(umlUseCaseNotation, DbBEStore.fUmlStereotype, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                new DbBESingleZoneDisplay(umlUseCaseNotation, DbBEStore.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                umlUseCaseNotation.setStoreDefaultWidth(new Integer(120));
                umlUseCaseNotation.setStoreDefaultHeight(new Integer(80));
                umlUseCaseNotation.setStoreIdPrefix(DbBENotation.STORE_ID_PREFIX);
                // Actor
                umlUseCaseNotation.setActorShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE_WITH_UML_ACTOR));
                umlUseCaseNotation.setActorZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONES 1, 2
                new DbBESingleZoneDisplay(umlUseCaseNotation, DbBEActor.fUmlStereotype, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                new DbBESingleZoneDisplay(umlUseCaseNotation, DbBEActor.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                umlUseCaseNotation.setActorDefaultWidth(new Integer(95));
                umlUseCaseNotation.setActorDefaultHeight(new Integer(130));
                umlUseCaseNotation.setActorIdPrefix(DbBENotation.ACTOR_ID_PREFIX);
                // FLOW
                // ZONE 1
                new DbBESingleZoneDisplay(umlUseCaseNotation, DbBEFlow.fUmlStereotype, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                umlUseCaseNotation.setFlowIdPrefix(DbBENotation.FLOW_ID_PREFIX);
            }

            { // UML SEQUENCE DIAGRAM
                DbBENotation umlSequenceDiagramNotation = new DbBENotation(thisProject);
                umlSequenceDiagramNotation.setName(DbBENotation.UML_SEQUENCE_DIAGRAM);
                umlSequenceDiagramNotation.setTerminologyName(DbBENotation.UML_SEQUENCE_DIAGRAM);
                umlSequenceDiagramNotation.setNotationID(new Integer(UML_SEQUENCE_DIAGRAM));
                umlSequenceDiagramNotation.setMasterNotationID(new Integer(UML_SEQUENCE_DIAGRAM));
                umlSequenceDiagramNotation.setNotationMode(new Integer(DbSMSNotation.BE_MODE));
                umlSequenceDiagramNotation.setBuiltIn(Boolean.TRUE);
                umlSequenceDiagramNotation.setDisplayFrameBox(Boolean.FALSE);
                umlSequenceDiagramNotation.setCellTitleBoxed(Boolean.FALSE);
                umlSequenceDiagramNotation.setCenterDisplay(Boolean.TRUE);
                umlSequenceDiagramNotation.setConstraintCenter(Boolean.TRUE);
                umlSequenceDiagramNotation.setDefButtomBorder(Boolean.FALSE);
                umlSequenceDiagramNotation.setDefVerticalAlignment(new Integer(TableCell.START));
                umlSequenceDiagramNotation.setHasFrame(Boolean.TRUE);

                //
                // Activation box
                umlSequenceDiagramNotation.setUseCaseShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE));
                umlSequenceDiagramNotation.setUseCaseZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                umlSequenceDiagramNotation.setDisplayFrameBox(Boolean.FALSE);
                // ZONE 1
                // new DbBESingleZoneDisplay(umlSequenceDiagramNotation,
                // DbBEUseCase.fName, true,
                // ZoneJustification.getInstance(ZoneJustification.CENTER),
                // false,
                // BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                umlSequenceDiagramNotation.setUseCaseDefaultWidth(new Integer(10));
                umlSequenceDiagramNotation.setUseCaseDefaultHeight(new Integer(80));

                // Class Role
                umlSequenceDiagramNotation.setStoreShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE));
                umlSequenceDiagramNotation.setStoreZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(umlSequenceDiagramNotation, DbBEUseCase.fUmlStereotype,
                        true, ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                new DbBESingleZoneDisplay(umlSequenceDiagramNotation, DbBEStore.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                umlSequenceDiagramNotation.setStoreDefaultWidth(new Integer(120));
                umlSequenceDiagramNotation.setStoreDefaultHeight(new Integer(32));
                // umlSequenceDiagramNotation.setStoreIdPrefix(DbBENotation.STORE_ID_PREFIX);
                umlSequenceDiagramNotation.setStoreIdPrefix("{0}: {1}");

                // Actor
                umlSequenceDiagramNotation.setActorShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE_WITH_UML_ACTOR));
                umlSequenceDiagramNotation.setActorZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));

                // ZONE 1
                new DbBESingleZoneDisplay(umlSequenceDiagramNotation, DbBEActor.fUmlStereotype,
                        true, ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                new DbBESingleZoneDisplay(umlSequenceDiagramNotation, DbBEActor.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                umlSequenceDiagramNotation.setActorDefaultWidth(new Integer(95));
                umlSequenceDiagramNotation.setActorDefaultHeight(new Integer(130));
                umlSequenceDiagramNotation.setActorIdPrefix(DbBENotation.ACTOR_ID_PREFIX);

                // FLOW
                // ZONE 1
                // umlSequenceDiagramNotation.setFlowZoneOrientation(SMSZoneOrientation.getInstance(SMSZoneOrientation.PERPENDICULAR));
                new DbBESingleZoneDisplay(umlSequenceDiagramNotation, DbBEFlow.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                // ZONE 2
                new DbBESingleZoneDisplay(umlSequenceDiagramNotation, DbBEFlow.fIdentifier, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                // ZONE 3
                new DbBESingleZoneDisplay(umlSequenceDiagramNotation, DbBEFlow.fEmissionCondition,
                        true, ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                // umlSequenceDiagramNotation.setFlowIdPrefix(DbBENotation.FLOW_ID_PREFIX);
                umlSequenceDiagramNotation.setFlowIdPrefix("{0}:");
            }

            { // UML STATE DIAGRAM
                DbBENotation umlStateDiagramNotation = new DbBENotation(thisProject);
                umlStateDiagramNotation.setName(DbBENotation.UML_STATE_DIAGRAM);
                umlStateDiagramNotation.setTerminologyName(DbBENotation.UML_STATE_DIAGRAM);
                umlStateDiagramNotation.setNotationID(new Integer(UML_STATE_DIAGRAM));
                umlStateDiagramNotation.setMasterNotationID(new Integer(UML_STATE_DIAGRAM));
                umlStateDiagramNotation.setNotationMode(new Integer(DbSMSNotation.BE_MODE));
                umlStateDiagramNotation.setBuiltIn(Boolean.TRUE);
                umlStateDiagramNotation.setDisplayFrameBox(Boolean.FALSE);
                umlStateDiagramNotation.setDisplayStereotypesAsIcons(Boolean.TRUE);
                umlStateDiagramNotation.setHasFrame(Boolean.FALSE);

                // UseCase
                umlStateDiagramNotation.setUseCaseShape(SMSNotationShape
                        .getInstance(SMSNotationShape.ROUND_RECTANGLE));
                umlStateDiagramNotation.setUseCaseZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONES 1, 2
                new DbBESingleZoneDisplay(umlStateDiagramNotation, DbBEUseCase.fUmlStereotype,
                        true, ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                new DbBESingleZoneDisplay(umlStateDiagramNotation, DbBEUseCase.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), true,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                // ZONE 3
                new DbBESingleZoneDisplay(umlStateDiagramNotation, DbBEUseCase.fDescription, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                umlStateDiagramNotation.setUseCaseDefaultWidth(new Integer(95));
                umlStateDiagramNotation.setUseCaseDefaultHeight(new Integer(65));
                // Store
                umlStateDiagramNotation.setStoreShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE));
                umlStateDiagramNotation.setStoreZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(umlStateDiagramNotation, DbBEUseCase.fUmlStereotype,
                        true, ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                new DbBESingleZoneDisplay(umlStateDiagramNotation, DbBEUseCase.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                umlStateDiagramNotation.setStoreDefaultWidth(new Integer(120));
                umlStateDiagramNotation.setStoreDefaultHeight(new Integer(80));
                umlStateDiagramNotation.setStoreIdPrefix(DbBENotation.STORE_ID_PREFIX);
                // Actor
                umlStateDiagramNotation.setActorShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE_WITH_UML_ACTOR));
                umlStateDiagramNotation.setActorZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(umlStateDiagramNotation, DbBEActor.fUmlStereotype, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                new DbBESingleZoneDisplay(umlStateDiagramNotation, DbBEActor.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                umlStateDiagramNotation.setActorDefaultWidth(new Integer(95));
                umlStateDiagramNotation.setActorDefaultHeight(new Integer(130));
                umlStateDiagramNotation.setActorIdPrefix(DbBENotation.ACTOR_ID_PREFIX);
                // FLOW
                // ZONE 1
                new DbBESingleZoneDisplay(umlStateDiagramNotation, DbBEFlow.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                // ZONE 2
                new DbBESingleZoneDisplay(umlStateDiagramNotation, DbBEFlow.fEmissionCondition,
                        true, ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                umlStateDiagramNotation.setFlowIdPrefix(DbBENotation.FLOW_ID_PREFIX);
            }

            { // UML COLLABORATION DIAGRAM
                DbBENotation umlCollaborationDiagramNotation = new DbBENotation(thisProject);
                umlCollaborationDiagramNotation.setName(DbBENotation.UML_COLLABORATION_DIAGRAM);
                umlCollaborationDiagramNotation
                        .setTerminologyName(DbBENotation.UML_COLLABORATION_DIAGRAM);
                umlCollaborationDiagramNotation
                        .setNotationID(new Integer(UML_COLLABORATION_DIAGRAM));
                umlCollaborationDiagramNotation.setMasterNotationID(new Integer(
                        UML_COLLABORATION_DIAGRAM));
                umlCollaborationDiagramNotation.setNotationMode(new Integer(DbSMSNotation.BE_MODE));
                umlCollaborationDiagramNotation.setBuiltIn(Boolean.TRUE);
                umlCollaborationDiagramNotation.setHasFrame(Boolean.FALSE);
                // UseCase
                umlCollaborationDiagramNotation.setUseCaseShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE));
                umlCollaborationDiagramNotation.setUseCaseZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(umlCollaborationDiagramNotation, DbBEUseCase.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), true,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                // ZONE 2
                new DbBESingleZoneDisplay(umlCollaborationDiagramNotation, true, ZoneJustification
                        .getInstance(ZoneJustification.CENTER), false, BEZoneStereotype
                        .getInstance(BEZoneStereotype.USECASE));
                umlCollaborationDiagramNotation.setUseCaseDefaultWidth(new Integer(95));
                umlCollaborationDiagramNotation.setUseCaseDefaultHeight(new Integer(60));
                // Store
                umlCollaborationDiagramNotation.setStoreShape(SMSNotationShape
                        .getInstance(SMSNotationShape.OPENED_RECTANGLE_LEFT_RIGHT));
                umlCollaborationDiagramNotation.setStoreZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(umlCollaborationDiagramNotation, DbBEStore.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                umlCollaborationDiagramNotation.setStoreDefaultWidth(new Integer(160));
                umlCollaborationDiagramNotation.setStoreDefaultHeight(new Integer(30));
                umlCollaborationDiagramNotation.setStoreIdPrefix(DbBENotation.STORE_ID_PREFIX);
                // Actor
                umlCollaborationDiagramNotation.setActorShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE_WITH_UML_ACTOR));
                umlCollaborationDiagramNotation.setActorZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(umlCollaborationDiagramNotation, DbBEActor.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                umlCollaborationDiagramNotation.setActorDefaultWidth(new Integer(95));
                umlCollaborationDiagramNotation.setActorDefaultHeight(new Integer(130));
                umlCollaborationDiagramNotation.setActorIdPrefix(DbBENotation.ACTOR_ID_PREFIX);
                // FLOW
                // ZONE 1
                new DbBESingleZoneDisplay(umlCollaborationDiagramNotation, DbBEFlow.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                umlCollaborationDiagramNotation.setFlowIdPrefix(DbBENotation.FLOW_ID_PREFIX);
            }

            { // UML ACTIVITY DIAGRAM
                DbBENotation umlActivityDiagramNotation = new DbBENotation(thisProject);
                umlActivityDiagramNotation.setName(UML_ACTIVITY_DIAGRAM_TXT);
                umlActivityDiagramNotation.setTerminologyName(UML_ACTIVITY_DIAGRAM_TXT);
                umlActivityDiagramNotation.setNotationID(new Integer(UML_ACTIVITY_DIAGRAM));
                umlActivityDiagramNotation.setMasterNotationID(new Integer(UML_ACTIVITY_DIAGRAM));
                umlActivityDiagramNotation.setNotationMode(new Integer(DbSMSNotation.BE_MODE));
                umlActivityDiagramNotation.setBuiltIn(Boolean.TRUE);
                umlActivityDiagramNotation.setDisplayFrameBox(Boolean.FALSE);
                umlActivityDiagramNotation.setCellTitleBoxed(Boolean.TRUE);
                umlActivityDiagramNotation.setCenterDisplay(Boolean.FALSE);
                umlActivityDiagramNotation.setConstraintCenter(Boolean.FALSE);
                umlActivityDiagramNotation.setDefButtomBorder(Boolean.FALSE);
                umlActivityDiagramNotation.setDefVerticalAlignment(new Integer(TableCell.START));
                umlActivityDiagramNotation.setHasFrame(Boolean.TRUE);

                // UseCase
                umlActivityDiagramNotation.setUseCaseShape(SMSNotationShape
                        .getInstance(SMSNotationShape.UML_ACTIVITY_PILL));
                umlActivityDiagramNotation.setUseCaseZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONES 1 & 2
                new DbBESingleZoneDisplay(umlActivityDiagramNotation, DbBEUseCase.fUmlStereotype,
                        true, ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                new DbBESingleZoneDisplay(umlActivityDiagramNotation, DbBEUseCase.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                umlActivityDiagramNotation.setUseCaseDefaultWidth(new Integer(125));
                umlActivityDiagramNotation.setUseCaseDefaultHeight(new Integer(55));
                // Store
                umlActivityDiagramNotation.setStoreShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE));
                umlActivityDiagramNotation.setStoreZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONES 1 & 2
                new DbBESingleZoneDisplay(umlActivityDiagramNotation, DbBEUseCase.fUmlStereotype,
                        true, ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                new DbBESingleZoneDisplay(umlActivityDiagramNotation, DbBEUseCase.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                umlActivityDiagramNotation.setStoreDefaultWidth(new Integer(120));
                umlActivityDiagramNotation.setStoreDefaultHeight(new Integer(80));

                // Actor
                umlActivityDiagramNotation.setActorShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE_WITH_UML_ACTOR));
                umlActivityDiagramNotation.setActorZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(umlActivityDiagramNotation, DbBEActor.fUmlStereotype,
                        true, ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                new DbBESingleZoneDisplay(umlActivityDiagramNotation, DbBEActor.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                umlActivityDiagramNotation.setActorDefaultWidth(new Integer(95));
                umlActivityDiagramNotation.setActorDefaultHeight(new Integer(130));
                umlActivityDiagramNotation.setActorIdPrefix(DbBENotation.ACTOR_ID_PREFIX);
                // FLOW
                // ZONE 1
                new DbBESingleZoneDisplay(umlActivityDiagramNotation, DbBEFlow.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                // ZONE 2
                new DbBESingleZoneDisplay(umlActivityDiagramNotation, DbBEFlow.fEmissionCondition,
                        true, ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                umlActivityDiagramNotation.setFlowIdPrefix(DbBENotation.FLOW_ID_PREFIX);
            }

            { // UML COMPONENT DIAGRAM
                DbBENotation umlComponentDiagramNotation = new DbBENotation(thisProject);
                umlComponentDiagramNotation.setName(UML_COMPONENT_DIAGRAM_TXT);
                umlComponentDiagramNotation.setTerminologyName(UML_COMPONENT_DIAGRAM_TXT);
                umlComponentDiagramNotation.setNotationID(new Integer(UML_COMPONENT_DIAGRAM));
                umlComponentDiagramNotation.setMasterNotationID(new Integer(UML_COMPONENT_DIAGRAM));
                umlComponentDiagramNotation.setNotationMode(new Integer(DbSMSNotation.BE_MODE));
                umlComponentDiagramNotation.setBuiltIn(Boolean.TRUE);
                umlComponentDiagramNotation.setDisplayFrameBox(Boolean.TRUE);
                umlComponentDiagramNotation.setCellTitleBoxed(Boolean.FALSE);
                umlComponentDiagramNotation.setCenterDisplay(Boolean.FALSE);
                umlComponentDiagramNotation.setConstraintCenter(Boolean.FALSE);
                umlComponentDiagramNotation.setDefButtomBorder(Boolean.FALSE);
                umlComponentDiagramNotation.setDefVerticalAlignment(new Integer(TableCell.START));
                umlComponentDiagramNotation.setHasFrame(Boolean.TRUE);

                // UseCase
                umlComponentDiagramNotation.setUseCaseShape(SMSNotationShape
                        .getInstance(SMSNotationShape.UML_COMPONENT));
                umlComponentDiagramNotation.setUseCaseZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONES 1 & 2
                new DbBESingleZoneDisplay(umlComponentDiagramNotation, DbBEUseCase.fUmlStereotype,
                        true, ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                new DbBESingleZoneDisplay(umlComponentDiagramNotation, DbBEUseCase.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                umlComponentDiagramNotation.setUseCaseDefaultWidth(new Integer(120));
                umlComponentDiagramNotation.setUseCaseDefaultHeight(new Integer(80));
                // Store
                umlComponentDiagramNotation.setStoreShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE));
                umlComponentDiagramNotation.setStoreZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONES 1 & 2
                new DbBESingleZoneDisplay(umlComponentDiagramNotation, DbBEUseCase.fUmlStereotype,
                        true, ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                new DbBESingleZoneDisplay(umlComponentDiagramNotation, DbBEUseCase.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                umlComponentDiagramNotation.setStoreDefaultWidth(new Integer(120));
                umlComponentDiagramNotation.setStoreDefaultHeight(new Integer(80));

                // Actor
                umlComponentDiagramNotation.setActorShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE_WITH_UML_ACTOR));
                umlComponentDiagramNotation.setActorZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(umlComponentDiagramNotation, DbBEActor.fUmlStereotype,
                        true, ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                new DbBESingleZoneDisplay(umlComponentDiagramNotation, DbBEActor.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                umlComponentDiagramNotation.setActorDefaultWidth(new Integer(95));
                umlComponentDiagramNotation.setActorDefaultHeight(new Integer(130));
                umlComponentDiagramNotation.setActorIdPrefix(DbBENotation.ACTOR_ID_PREFIX);
                // FLOW
                // ZONE 1
                new DbBESingleZoneDisplay(umlComponentDiagramNotation, DbBEFlow.fUmlStereotype,
                        true, ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                // ZONE 2
                new DbBESingleZoneDisplay(umlComponentDiagramNotation, DbBEFlow.fEmissionCondition,
                        true, ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                umlComponentDiagramNotation.setFlowIdPrefix(DbBENotation.FLOW_ID_PREFIX);
                // ZONE 3
                new DbBESingleZoneDisplay(umlComponentDiagramNotation, DbBEFlow.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));

            } // end if

            { // UML DEPLOYMENT DIAGRAM
                DbBENotation umlDeploymentDiagramNotation = new DbBENotation(thisProject);
                umlDeploymentDiagramNotation.setName(UML_DEPLOYMENT_DIAGRAM_TXT);
                umlDeploymentDiagramNotation.setTerminologyName(UML_DEPLOYMENT_DIAGRAM_TXT);
                umlDeploymentDiagramNotation.setNotationID(new Integer(UML_DEPLOYMENT_DIAGRAM));
                umlDeploymentDiagramNotation
                        .setMasterNotationID(new Integer(UML_DEPLOYMENT_DIAGRAM));
                umlDeploymentDiagramNotation.setNotationMode(new Integer(DbSMSNotation.BE_MODE));
                umlDeploymentDiagramNotation.setBuiltIn(Boolean.TRUE);
                umlDeploymentDiagramNotation.setDisplayFrameBox(Boolean.TRUE);
                umlDeploymentDiagramNotation.setCellTitleBoxed(Boolean.FALSE);
                umlDeploymentDiagramNotation.setCenterDisplay(Boolean.FALSE);
                umlDeploymentDiagramNotation.setConstraintCenter(Boolean.FALSE);
                umlDeploymentDiagramNotation.setDefButtomBorder(Boolean.FALSE);
                umlDeploymentDiagramNotation.setDefVerticalAlignment(new Integer(TableCell.START));
                umlDeploymentDiagramNotation.setHasFrame(Boolean.FALSE);

                // UseCase
                umlDeploymentDiagramNotation.setUseCaseShape(SMSNotationShape
                        .getInstance(SMSNotationShape.UML_NODE));
                umlDeploymentDiagramNotation.setUseCaseZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONES 1 & 2
                new DbBESingleZoneDisplay(umlDeploymentDiagramNotation, DbBEUseCase.fUmlStereotype,
                        true, ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                new DbBESingleZoneDisplay(umlDeploymentDiagramNotation, DbBEUseCase.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                umlDeploymentDiagramNotation.setUseCaseDefaultWidth(new Integer(120));
                umlDeploymentDiagramNotation.setUseCaseDefaultHeight(new Integer(80));
                // Store
                umlDeploymentDiagramNotation.setStoreShape(SMSNotationShape
                        .getInstance(SMSNotationShape.UML_COMPONENT));
                umlDeploymentDiagramNotation.setStoreZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONES 1 & 2
                new DbBESingleZoneDisplay(umlDeploymentDiagramNotation, DbBEUseCase.fUmlStereotype,
                        true, ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                new DbBESingleZoneDisplay(umlDeploymentDiagramNotation, DbBEUseCase.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                umlDeploymentDiagramNotation.setStoreDefaultWidth(new Integer(120));
                umlDeploymentDiagramNotation.setStoreDefaultHeight(new Integer(80));

                // Actor
                umlDeploymentDiagramNotation.setActorShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE_WITH_UML_ACTOR));
                umlDeploymentDiagramNotation.setActorZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(umlDeploymentDiagramNotation, DbBEActor.fUmlStereotype,
                        true, ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                new DbBESingleZoneDisplay(umlDeploymentDiagramNotation, DbBEActor.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                umlDeploymentDiagramNotation.setActorDefaultWidth(new Integer(95));
                umlDeploymentDiagramNotation.setActorDefaultHeight(new Integer(130));
                umlDeploymentDiagramNotation.setActorIdPrefix(DbBENotation.ACTOR_ID_PREFIX);
                // FLOW
                // ZONE 1
                new DbBESingleZoneDisplay(umlDeploymentDiagramNotation, DbBEFlow.fUmlStereotype,
                        true, ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
                // ZONE 2
                new DbBESingleZoneDisplay(umlDeploymentDiagramNotation,
                        DbBEFlow.fEmissionCondition, true, ZoneJustification
                                .getInstance(ZoneJustification.LEFT), false, BEZoneStereotype
                                .getInstance(BEZoneStereotype.FLOW));
                umlDeploymentDiagramNotation.setFlowIdPrefix(DbBENotation.FLOW_ID_PREFIX);
                // ZONE 3
                new DbBESingleZoneDisplay(umlDeploymentDiagramNotation, DbBEFlow.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.LEFT), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));

            } // end if

            // MESSAGE MODELING
            {
                DbBENotation messageModelingNotation = new DbBENotation(thisProject);
                messageModelingNotation.setName(DbBENotation.MESSAGE_MODELING);
                messageModelingNotation.setTerminologyName(DbBENotation.MESSAGE_MODELING);
                messageModelingNotation.setNotationID(new Integer(MESSAGE_MODELING));
                messageModelingNotation.setMasterNotationID(new Integer(MESSAGE_MODELING));
                messageModelingNotation.setNotationMode(new Integer(DbSMSNotation.BE_MODE));
                messageModelingNotation.setBuiltIn(Boolean.TRUE);
                messageModelingNotation.setHasFrame(Boolean.TRUE);
                // UseCase
                messageModelingNotation.setUseCaseShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE));
                messageModelingNotation.setUseCaseZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(messageModelingNotation, DbBEUseCase.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                messageModelingNotation.setUseCaseDefaultWidth(new Integer(95));
                messageModelingNotation.setUseCaseDefaultHeight(new Integer(60));
                // Store
                messageModelingNotation.setStoreShape(SMSNotationShape
                        .getInstance(SMSNotationShape.OPENED_RECTANGLE_LEFT_RIGHT));
                messageModelingNotation.setStoreZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(messageModelingNotation, DbBEStore.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                messageModelingNotation.setStoreDefaultWidth(new Integer(160));
                messageModelingNotation.setStoreDefaultHeight(new Integer(30));
                messageModelingNotation.setStoreIdPrefix(DbBENotation.STORE_ID_PREFIX);
                // Actor
                messageModelingNotation.setActorShape(SMSNotationShape
                        .getInstance(SMSNotationShape.SHADOW_RECTANGLE));
                messageModelingNotation.setActorZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(messageModelingNotation, DbBEActor.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                messageModelingNotation.setActorDefaultWidth(new Integer(95));
                messageModelingNotation.setActorDefaultHeight(new Integer(95));
                messageModelingNotation.setActorIdPrefix(DbBENotation.ACTOR_ID_PREFIX);
                // FLOW
                messageModelingNotation.setFlowIdPrefix(DbBENotation.FLOW_ID_PREFIX);
            }

            // Object Life Cycle
            {
                DbBENotation objectLifeCycleNotation = new DbBENotation(thisProject);
                objectLifeCycleNotation.setName(DbBENotation.OBJECT_LIFE_CYCLE);
                objectLifeCycleNotation.setTerminologyName(DbBENotation.OBJECT_LIFE_CYCLE);
                objectLifeCycleNotation.setNotationID(new Integer(OBJECT_LIFE_CYCLE));
                objectLifeCycleNotation.setMasterNotationID(new Integer(OBJECT_LIFE_CYCLE));
                objectLifeCycleNotation.setNotationMode(new Integer(DbSMSNotation.BE_MODE));
                objectLifeCycleNotation.setBuiltIn(Boolean.TRUE);
                objectLifeCycleNotation.setHasFrame(Boolean.TRUE);
                // UseCase
                objectLifeCycleNotation.setUseCaseShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE));
                objectLifeCycleNotation.setUseCaseZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(objectLifeCycleNotation, DbBEUseCase.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
                objectLifeCycleNotation.setUseCaseDefaultWidth(new Integer(95));
                objectLifeCycleNotation.setUseCaseDefaultHeight(new Integer(60));
                // Store
                objectLifeCycleNotation.setStoreShape(SMSNotationShape
                        .getInstance(SMSNotationShape.RECTANGLE));
                objectLifeCycleNotation.setStoreZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(objectLifeCycleNotation, DbBEStore.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
                objectLifeCycleNotation.setStoreDefaultWidth(new Integer(95));
                objectLifeCycleNotation.setStoreDefaultHeight(new Integer(35));
                objectLifeCycleNotation.setStoreIdPrefix(DbBENotation.STORE_ID_PREFIX);
                // Actor
                objectLifeCycleNotation.setActorShape(SMSNotationShape
                        .getInstance(SMSNotationShape.OVALE));
                objectLifeCycleNotation.setActorZoneOrientation(SMSZoneOrientation
                        .getInstance(SMSZoneOrientation.HORIZONTAL));
                // ZONE 1
                new DbBESingleZoneDisplay(objectLifeCycleNotation, DbBEActor.fName, true,
                        ZoneJustification.getInstance(ZoneJustification.CENTER), false,
                        BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
                objectLifeCycleNotation.setActorDefaultWidth(new Integer(80));
                objectLifeCycleNotation.setActorDefaultHeight(new Integer(80));
                objectLifeCycleNotation.setActorIdPrefix(DbBENotation.ACTOR_ID_PREFIX);
                // FLOW
                objectLifeCycleNotation.setFlowIdPrefix(DbBENotation.FLOW_ID_PREFIX);
            }

            // set default notation
            DbBENotation defGaneSarsonNotation = (DbBENotation) thisProject.findComponentByName(
                    DbBENotation.metaClass, DbBENotation.GANE_SARSON);
            thisProject.setBeDefaultNotation(defGaneSarsonNotation);

        } // end if
    } // end initBeNotations()

    public static void initOoStyle() {
        // Display options
        DbOOStyle.oojv_displayOptions = new MetaField[] {
                DbOOStyle.fOojv_classQualifiedNameDisplayed, DbOOStyle.fOojv_attributeDisplayed,
                DbOOStyle.fOojv_inheritedAttributeDisplayed, DbOOStyle.fOojv_methodDisplayed,
                DbOOStyle.fOojv_methodSignatureDisplayed,
                DbOOStyle.fOojv_hideParameterNamesInSignatures,
                DbOOStyle.fOojv_inheritedMethodDisplayed,
                DbOOStyle.fOojv_inheritedMethodSignatureDisplayed,
                DbOOStyle.fOojv_innerClassDisplayed, DbOOStyle.fOojv_inheritedInnerClassDisplayed,
                DbOOStyle.fOojv_associationNameDisplayed,
                DbOOStyle.fOojv_associationAttributeDisplayed,
                DbOOStyle.fOojv_associationAttributeNameLabelDisplayed,
                DbOOStyle.fOojv_associationAttributeTypeLabelDisplayed,
                DbOOStyle.fOojv_umlAssociationDirection, DbOOStyle.fOojv_umlConstraintDisplayed,
                DbOOStyle.fOojv_umlStereotypeDisplayed, DbOOStyle.fOojv_umlStereotypeIconDisplayed, };

        DbOOStyle.oojv_displayOptionDefaultValues = new Boolean[] { Boolean.TRUE, // fOojv_classQualifiedNameDisplayed,
                Boolean.TRUE, // fOojv_attributeDisplayed
                Boolean.FALSE, // fOojv_inheritedAttributeDisplayed
                Boolean.TRUE, // fOojv_methodDisplayed
                Boolean.TRUE, // fOojv_methodSignatureDisplayed
                Boolean.TRUE, // fOojv_hideParameterNamesInSignatures
                Boolean.FALSE, // fOojv_inheritedMethodDisplayed
                Boolean.FALSE, // fOojv_inheritedMethodSignatureDisplayed
                Boolean.TRUE, // fOojv_innerClassDisplayed
                Boolean.FALSE, // fOojv_inheritedInnerClassDisplayed
                Boolean.FALSE, // fOojv_associationNameDisplayed,
                Boolean.TRUE, // fOojv_associationAttributeDisplayed
                Boolean.FALSE, // fOojv_associationAttributeNameLabelDisplayed
                Boolean.FALSE, // fOojv_associationAttributeTypeLabelDisplayed
                Boolean.TRUE, // fOojv_umlAssociationDirection
                Boolean.TRUE, // fOojv_umlConstraintDisplayed,
                Boolean.TRUE, // fOojv_umlStereotypeDisplayed,
                Boolean.TRUE, // fOojv_umlStereotypeIconDisplayed
        };

        // Modifier display options
        DbOOStyle.oojv_modifierDisplayOptions = new MetaField[] {
                DbOOStyle.fOojv_publicModifierDisplayed, DbOOStyle.fOojv_privateModifierDisplayed,
                DbOOStyle.fOojv_protectedModifierDisplayed,
                DbOOStyle.fOojv_packageModifierDisplayed,
                DbOOStyle.fOojv_abstractModifierDisplayed,
                DbOOStyle.fOojv_nonAbstractModifierDisplayed,
                DbOOStyle.fOojv_staticModifierDisplayed,
                DbOOStyle.fOojv_nonStaticModifierDisplayed, DbOOStyle.fOojv_finalModifierDisplayed,
                DbOOStyle.fOojv_nonFinalModifierDisplayed,
                DbOOStyle.fOojv_transientModifierDisplayed,
                DbOOStyle.fOojv_nonTransientModifierDisplayed };

        DbOOStyle.oojv_modifierDisplayOptionDefaultValues = new Boolean[] { Boolean.TRUE, // fOojv_publicModifierDisplayed
                Boolean.TRUE, // fOojv_privateModifierDisplayed
                Boolean.TRUE, // fOojv_protectedModifierDisplayed
                Boolean.TRUE, // fOojv_packageModifierDisplayed
                Boolean.TRUE, // fOojv_abstractModifierDisplayed
                Boolean.TRUE, // fOojv_nonAbstractModifierDisplayed
                Boolean.TRUE, // fOojv_staticModifierDisplayed
                Boolean.TRUE, // fOojv_nonStaticModifierDisplayed
                Boolean.TRUE, // fOojv_finalModifierDisplayed
                Boolean.TRUE, // fOojv_nonFinalModifierDisplayed
                Boolean.TRUE, // fOojv_transientModifierDisplayed
                Boolean.TRUE }; // fOojv_nonTransientModifierDisplayed

        // Advanced style options
        DbOOStyle.oojv_advancedDisplayOptions = new MetaField[] {
                DbOOStyle.fOojv_umlForceUnderlineOnStatic,
                DbOOStyle.fOojv_umlForceItalicOnAbstract, DbOOStyle.fOojv_umlTypeBeforeName,
                DbOOStyle.fOojv_umlInterfaceShownAsCircle };

        DbOOStyle.oojv_advancedDisplayOptionDefaultValues = new Boolean[] { Boolean.TRUE, // fOojv_umlForceUnderlineOnStatic,
                Boolean.TRUE, // fOojv_umlForceItalicOnAbstract,
                Boolean.FALSE, // fOojv_umlTypeBeforeName,
                Boolean.FALSE, // fOojv_umlInterfaceShownAsCircle
        };

        // Font options
        DbOOStyle.oojv_fontOptions = new MetaField[] {
                DbOOStyle.fOojv_associationAttributeLabelFont, DbOOStyle.fOojv_associationNameFont,
                DbOOStyle.fOojv_multiplicityFont, DbOOStyle.fSms_packageNameFont,
                DbOOStyle.fOojv_stereotypeNameFont, DbOOStyle.fOojv_classNameFont,
                DbOOStyle.fOojv_abstractClassNameFont, DbOOStyle.fOojv_interfaceNameFont,
                DbOOStyle.fOojv_exceptionNameFont, DbOOStyle.fOojv_fieldFont,
                DbOOStyle.fOojv_methodFont, DbOOStyle.fOojv_innerClassFont,
                DbOOStyle.fOojv_inheritedMemberFont };

        Font plainFont = new Font("Arial", Font.PLAIN, 8);
        Font italicFont = new Font("Arial", Font.ITALIC, 8);
        Font boldFont = new Font("Arial", Font.BOLD, 8);
        DbOOStyle.oojv_fontOptionDefaultValues = new Font[] { plainFont, // fOojv_associationAttributeLabelFont,
                plainFont, // fOojv_associationNameFont,
                plainFont, // fOojv_multiplicityFont,
                plainFont, // fSms_packageNameFont
                italicFont, // fOojv_stereotypeNameFont
                boldFont, // fOojv_classNameFont
                italicFont, // fOojv_abstractClassNameFont
                boldFont, // fOojv_interfaceNameFont
                boldFont, // fOojv_exceptionNameFont
                plainFont, // fOojv_fieldFont
                plainFont, // fOojv_methodFont
                plainFont, // fOojv_innerClassFont
                italicFont // fOojv_inheritedMemberFont
        };

        // Prefix options
        DbOOStyle.oojv_prefixOptions = new MetaField[] { DbOOStyle.fOojv_publicVisibilityPrefix,
                DbOOStyle.fOojv_protectedVisibilityPrefix, DbOOStyle.fOojv_privateVisibilityPrefix,
                DbOOStyle.fOojv_packageVisibilityPrefix, DbOOStyle.fOojv_importedClassPrefix,
                DbOOStyle.fOojv_abstractModifierPrefix, DbOOStyle.fOojv_staticModifierPrefix,
                DbOOStyle.fOojv_finalModifierPrefix, DbOOStyle.fOojv_transientModifierPrefix,
                DbOOStyle.fOojv_overridingMemberPrefix, DbOOStyle.fOojv_hidingMemberPrefix,
                DbOOStyle.fOojv_memberInheritedFromInterfacePrefix, DbOOStyle.fSourcePrefix,
                DbOOStyle.fTargetPrefix, DbOOStyle.fErrorPrefix, DbOOStyle.fWarningPrefix, };

        //final Image kSourceImage = GraphicUtil.loadImage(Module.class, "db/resources/source.gif"); // NOT LOCALIZABLE - image
        //final Image kTargetImage = GraphicUtil.loadImage(Module.class, "db/resources/target.gif"); // NOT LOCALIZABLE - image
        // final DbtPrefix sourcePrefix = new DbtPrefix(DbtPrefix.DISPLAY_IMAGE,
        // "S", null, kSourceImage);

        DbOOStyle.oojv_prefixOptionDefaultValues = new DbtPrefix[] {
                new DbtPrefix(DbtPrefix.DISPLAY_TEXT, "+", null, null), // fPublicVisibilityPrefix
                new DbtPrefix(DbtPrefix.DISPLAY_TEXT, "#", null, null), // fProtectedVisibilityPrefix
                new DbtPrefix(DbtPrefix.DISPLAY_TEXT, "-", null, null), // fPrivateVisibilityPrefix
                new DbtPrefix(DbtPrefix.DISPLAY_TEXT, "~", null, null), // fPackageVisibilityPrefix
                new DbtPrefix(DbtPrefix.DISPLAY_NONE, "*", null, null), // fImportedClassPrefix
                new DbtPrefix(DbtPrefix.DISPLAY_TEXT, "A", null, null), // fAbstractModifierPrefix
                new DbtPrefix(DbtPrefix.DISPLAY_TEXT, "$", null, null), // fStaticModifierPrefix
                new DbtPrefix(DbtPrefix.DISPLAY_TEXT, "F", null, null), // fFinalModifierPrefix
                new DbtPrefix(DbtPrefix.DISPLAY_TEXT, "T", null, null), // fTransientModifierPrefix
                new DbtPrefix(DbtPrefix.DISPLAY_TEXT, "^", null, null), // fOverridingMemberPrefix
                new DbtPrefix(DbtPrefix.DISPLAY_TEXT, "^", null, null), // fHidingMemberPrefix
                new DbtPrefix(DbtPrefix.DISPLAY_TEXT, "i", null, null), // fMemberInheritedFromInterfacePrefix
                sourcePrefix, // fSourcePrefix,
                targetPrefix, // fTargetPrefix,
                errorPrefix, // fErrorPrefix,
                warningPrefix // fWarningPrefix
        };

        // Color options
        DbOOStyle.oojv_colorOptions = new MetaField[] { DbOOStyle.fTextColorDbSMSPackage,
                DbOOStyle.fBackgroundColorDbSMSPackage, DbOOStyle.fLineColorDbSMSPackage,
                DbOOStyle.fTextColorClass, DbOOStyle.fBackgroundColorClass,
                DbOOStyle.fLineColorClass, DbOOStyle.fTextColorInterface,
                DbOOStyle.fBackgroundColorInterface, DbOOStyle.fLineColorInterface,
                DbOOStyle.fTextColorException, DbOOStyle.fBackgroundColorException,
                DbOOStyle.fLineColorException, DbOOStyle.fLineColorDbSMSAssociation,
                DbOOStyle.fLineColorAggregation, DbOOStyle.fLineColorComposition,
                DbOOStyle.fLineColorDbSMSInheritance, DbOOStyle.fTextColorDbSMSNotice,
                DbOOStyle.fBackgroundColorDbSMSNotice, DbOOStyle.fLineColorDbSMSNotice,
                DbSMSStyle.fLineColorDbSMSGraphicalLink,
        //DbSMSStyle.fTextColorRecentModifications
        };

        Color lightYellow = new Color(255, 255, 204);
        Color darkYellow = new Color(255, 185, 31, 140);
        Color darkRed = new Color(153, 0, 0);

        DbOOStyle.oojv_colorOptionDefaultValues = new Color[] { Color.black, // fTextColorDbSMSPackage
                darkYellow, // fBackgroundColorDbSMSPackage
                Color.black, // fLineColorDbSMSPackage
                Color.black, // fOojv_classTextColor
                lightYellow, // fOojv_classBackgroundColor
                darkRed, // fOojv_classBorderColor
                Color.black, // fOojv_interfaceTextColor
                lightYellow, // fOojv_interfaceBackgroundColor
                darkRed, // fOojv_interfaceBorderColor
                Color.black, // fOojv_exceptionTextColor
                lightYellow, // fOojv_exceptionBackgroundColor
                darkRed, // fOojv_exceptionBorderColor
                Color.black, // fsms_associationColor
                Color.black, // fOojv_aggregationColor
                Color.black, // fOojv_compositionColor
                Color.black, // fOojv_inheritanceColor
                Color.black, // fTextColorDbSMSNotice
                lightYellow, // fBackgroundColorDbSMSNotice
                Color.black, // fLineColorDbSMSNotice
                Color.black, // fLineColorDbSMSGraphicalLink
        //Color.blue, // fTextColorRecentModifications
        };

        // Line Style Options
        DbOOStyle.oojv_lineOptions = new MetaField[] { DbOOStyle.fHighlightDbSMSPackage,
                DbOOStyle.fDashStyleDbSMSPackage, DbOOStyle.fHighlightClass,
                DbOOStyle.fDashStyleClass, DbOOStyle.fHighlightInterface,
                DbOOStyle.fDashStyleInterface, DbOOStyle.fHighlightException,
                DbOOStyle.fDashStyleException, DbOOStyle.fHighlightDbSMSAssociation,
                DbOOStyle.fDashStyleDbSMSAssociation, DbOOStyle.fHighlightAggregation,
                DbOOStyle.fDashStyleAggregation, DbOOStyle.fHighlightComposition,
                DbOOStyle.fDashStyleComposition, DbOOStyle.fHighlightDbSMSInheritance,
                DbOOStyle.fDashStyleDbSMSInheritance, DbOOStyle.fHighlightRealizationInheritance,
                DbOOStyle.fDashStyleRealizationInheritance, DbOOStyle.fHighlightDbSMSNotice,
                DbOOStyle.fDashStyleDbSMSNotice, DbSMSStyle.fDashStyleDbSMSGraphicalLink,
                DbSMSStyle.fHighlightDbSMSGraphicalLink };

        DbOOStyle.oojv_lineOptionDefaultValues = new Boolean[] { Boolean.FALSE, // fHighlightDbSMSPackage
                Boolean.FALSE, // fDashStyleDbSMSPackage
                Boolean.FALSE, // fOojv_classHighlight
                Boolean.FALSE, // fOojv_classDashStyle
                Boolean.FALSE, // fOojv_interfaceHighlight
                Boolean.FALSE, // fOojv_interfaceDashStyle
                Boolean.FALSE, // fOojv_exceptionHighlight
                Boolean.FALSE, // fOojv_exceptionDashStyle
                Boolean.FALSE, // fSms_associationHighlight
                Boolean.FALSE, // fSms_associationDashStyle
                Boolean.FALSE, // fOojv_aggregationHighlight
                Boolean.FALSE, // fOojv_aggregationDashStyle
                Boolean.FALSE, // fOojv_compositionHighlight
                Boolean.FALSE, // fOojv_compositionDashStyle
                Boolean.FALSE, // fSms_inheritanceHighlight
                Boolean.FALSE, // fSms_inheritanceDashStyle
                Boolean.FALSE, // fHighlightRealizationInheritance
                Boolean.TRUE, // fDashStyleRealizationInheritance
                Boolean.FALSE, // fHighlightDbSMSNotice
                Boolean.FALSE, // fDashStyleDbSMSNotice
                Boolean.TRUE, // fDashStyleDbSMSGraphicalLink
                Boolean.FALSE, // fHighlightDbSMSGraphicalLink
        };

        DbOOStyle.oojv_listOptionTabs = new String[] { "oojv_optionGroups",
                "oojv_optionValueGroups", "oojv_optionGroupHeaders" };

        DbOOStyle.oojv_optionGroups = new MetaField[][] { DbOOStyle.oojv_displayOptions,
                DbOOStyle.oojv_modifierDisplayOptions, DbOOStyle.oojv_fontOptions,
                DbOOStyle.oojv_prefixOptions, DbOOStyle.oojv_colorOptions,
                DbOOStyle.oojv_lineOptions, DbOOStyle.oojv_advancedDisplayOptions };

        DbOOStyle.oojv_optionValueGroups = new Object[][] {
                DbOOStyle.oojv_displayOptionDefaultValues,
                DbOOStyle.oojv_modifierDisplayOptionDefaultValues,
                DbOOStyle.oojv_fontOptionDefaultValues, DbOOStyle.oojv_prefixOptionDefaultValues,
                DbOOStyle.oojv_colorOptionDefaultValues, DbOOStyle.oojv_lineOptionDefaultValues,
                DbOOStyle.oojv_advancedDisplayOptionDefaultValues };

        DbOOStyle.oojv_optionGroupHeaders = new String[] {
                org.modelsphere.sms.oo.international.LocaleMgr.screen.getString("StructureDisplay"),
                org.modelsphere.sms.oo.international.LocaleMgr.screen
                        .getString("ModifierBasedDisplay"),
                org.modelsphere.sms.oo.international.LocaleMgr.screen.getString("Font"),
                org.modelsphere.sms.oo.international.LocaleMgr.screen.getString("Prefix"),
                org.modelsphere.sms.oo.international.LocaleMgr.screen.getString("Color"),
                org.modelsphere.sms.oo.international.LocaleMgr.screen.getString("LineStyle"),
                org.modelsphere.sms.oo.international.LocaleMgr.screen.getString("Advanced") };
    } // end initOoStyle()

    public static void initOrNotations(DbSMSProject project) throws DbException {

        createDefaultStyles(project);
        DbORStyle orStyle = (DbORStyle) project.getOrDefaultStyle();

        { // Datarun notation (default)
            DbORNotation datarunNotation = new DbORNotation(project);
            datarunNotation.initOptions();
            datarunNotation.setName(DbORNotation.DATARUN);
            datarunNotation.setTerminologyName(DbORNotation.DATARUN);
            datarunNotation.setNotationID(new Integer(DATARUN));
            datarunNotation.setMasterNotationID(new Integer(DATARUN));
            datarunNotation.setNotationMode(new Integer(DbSMSNotation.OR_MODE));
            datarunNotation.setNumericDisplay(ORConnectivitiesDisplay
                    .getInstance(ORConnectivitiesDisplay.MIN_MAX));
            datarunNotation.setSymbolicDisplay(ORConnectivitiesDisplay
                    .getInstance(ORConnectivitiesDisplay.NONE));
            datarunNotation.setNumericPosition(ORConnectivityPosition
                    .getInstance(ORConnectivityPosition.CLOSE_BY));
            datarunNotation.setKeyDependencyVisible(Boolean.FALSE);
            datarunNotation.setKeyDependencySymbol(ORNotationSymbol
                    .getInstance(ORNotationSymbol.NONE));

            datarunNotation.setChoiceShape(SMSNotationShape.getInstance(SMSNotationShape.DIAMOND));
            datarunNotation.setSpecializationShape(SMSNotationShape
                    .getInstance(SMSNotationShape.TRIANGLE));

            datarunNotation.setBuiltIn(Boolean.TRUE);

            project.setOrDefaultNotation(datarunNotation);
            if (orStyle != null) {
                datarunNotation.setDefaultStyle(orStyle);
            }
        }

        { // Information Engineering
            DbORNotation infoEngNotation = new DbORNotation(project);
            infoEngNotation.initOptions();
            infoEngNotation.setName(DbORNotation.INFORMATION_ENGINEERING);
            infoEngNotation.setTerminologyName(DbORNotation.INFORMATION_ENGINEERING);
            infoEngNotation.setNotationID(new Integer(INFORMATION_ENGINEERING));
            infoEngNotation.setNotationMode(new Integer(DbSMSNotation.OR_MODE));
            infoEngNotation.setNumericDisplay(ORConnectivitiesDisplay
                    .getInstance(ORConnectivitiesDisplay.NONE));
            infoEngNotation.setNumericPosition(ORConnectivityPosition
                    .getInstance(ORConnectivityPosition.FAR_AWAY));
            infoEngNotation.setSymbolicDisplay(ORConnectivitiesDisplay
                    .getInstance(ORConnectivitiesDisplay.MIN_MAX));
            infoEngNotation.setSymbolicPosition(ORConnectivityPosition
                    .getInstance(ORConnectivityPosition.FAR_AWAY));
            infoEngNotation.setSymbolicChildRolePosition(ORConnectivityPosition
                    .getInstance(ORConnectivityPosition.FAR_AWAY));
            infoEngNotation.setMin0Symbol(ORNotationSymbol
                    .getInstance(ORNotationSymbol.EMPTY_SMALL_CIRCLE));
            infoEngNotation.setMin1Symbol(ORNotationSymbol
                    .getInstance(ORNotationSymbol.SINGLE_LINE));
            infoEngNotation.setMax1Symbol(ORNotationSymbol
                    .getInstance(ORNotationSymbol.SINGLE_LINE));
            infoEngNotation.setMaxNSymbol(ORNotationSymbol
                    .getInstance(ORNotationSymbol.EMPTY_HALF_LARGE_DIAMOND));
            infoEngNotation.setChildRoleVisible(Boolean.FALSE);
            infoEngNotation.setKeyDependencyVisible(Boolean.FALSE);
            infoEngNotation.setKeyDependencySymbol(ORNotationSymbol
                    .getInstance(ORNotationSymbol.NONE));

            infoEngNotation.setChoiceShape(SMSNotationShape.getInstance(SMSNotationShape.DIAMOND));
            infoEngNotation.setSpecializationShape(SMSNotationShape
                    .getInstance(SMSNotationShape.TRIANGLE));

            infoEngNotation.setBuiltIn(Boolean.TRUE);
            if (orStyle != null) {
                infoEngNotation.setDefaultStyle(orStyle);
            }

        }

        { // Information Engineering+
            DbORNotation infoEngPlusNotation = new DbORNotation(project);
            infoEngPlusNotation.initOptions();
            infoEngPlusNotation.setName(DbORNotation.INFORMATION_ENGINEERING_PLUS);
            infoEngPlusNotation.setTerminologyName(DbORNotation.INFORMATION_ENGINEERING_PLUS);
            infoEngPlusNotation.setNotationID(new Integer(INFORMATION_ENGINEERING_PLUS));
            infoEngPlusNotation.setMasterNotationID(new Integer(INFORMATION_ENGINEERING_PLUS));
            infoEngPlusNotation.setNotationMode(new Integer(DbSMSNotation.OR_MODE));
            infoEngPlusNotation.setNumericDisplay(ORConnectivitiesDisplay
                    .getInstance(ORConnectivitiesDisplay.NONE));
            infoEngPlusNotation.setSymbolicDisplay(ORConnectivitiesDisplay
                    .getInstance(ORConnectivitiesDisplay.MIN_MAX));
            infoEngPlusNotation.setSymbolicPosition(ORConnectivityPosition
                    .getInstance(ORConnectivityPosition.FAR_AWAY));
            infoEngPlusNotation.setSymbolicChildRolePosition(ORConnectivityPosition
                    .getInstance(ORConnectivityPosition.CLOSE_BY));
            infoEngPlusNotation.setMin0Symbol(ORNotationSymbol
                    .getInstance(ORNotationSymbol.EMPTY_SMALL_CIRCLE));
            infoEngPlusNotation.setMin1Symbol(ORNotationSymbol
                    .getInstance(ORNotationSymbol.SINGLE_LINE));
            infoEngPlusNotation.setMax1Symbol(ORNotationSymbol
                    .getInstance(ORNotationSymbol.SINGLE_LINE));
            infoEngPlusNotation.setMaxNSymbol(ORNotationSymbol
                    .getInstance(ORNotationSymbol.EMPTY_HALF_LARGE_DIAMOND));
            infoEngPlusNotation.setChildRoleVisible(Boolean.FALSE);
            infoEngPlusNotation.setKeyDependencyVisible(Boolean.TRUE);
            infoEngPlusNotation.setKeyDependencySymbol(ORNotationSymbol
                    .getInstance(ORNotationSymbol.FILLED_SMALL_DIAMOND));

            infoEngPlusNotation.setChoiceShape(SMSNotationShape
                    .getInstance(SMSNotationShape.DIAMOND));
            infoEngPlusNotation.setSpecializationShape(SMSNotationShape
                    .getInstance(SMSNotationShape.TRIANGLE));

            infoEngPlusNotation.setBuiltIn(Boolean.TRUE);
            if (orStyle != null) {
                infoEngPlusNotation.setDefaultStyle(orStyle);
            }

        }

        { // Logical Data Structure
            DbORNotation logicalDSNotation = new DbORNotation(project);
            logicalDSNotation.initOptions();
            logicalDSNotation.setName(DbORNotation.LOGICAL_DATA_STRUCTURE);
            logicalDSNotation.setTerminologyName(DbORNotation.LOGICAL_DATA_STRUCTURE);
            logicalDSNotation.setNotationID(new Integer(LOGICAL_DATA_STRUCTURE));
            logicalDSNotation.setMasterNotationID(new Integer(LOGICAL_DATA_STRUCTURE));
            logicalDSNotation.setNotationMode(new Integer(DbSMSNotation.OR_MODE));
            logicalDSNotation.setNumericDisplay(ORConnectivitiesDisplay
                    .getInstance(ORConnectivitiesDisplay.NONE));
            logicalDSNotation.setSymbolicDisplay(ORConnectivitiesDisplay
                    .getInstance(ORConnectivitiesDisplay.MAX));
            logicalDSNotation.setSymbolicPosition(ORConnectivityPosition
                    .getInstance(ORConnectivityPosition.FAR_AWAY));
            logicalDSNotation.setSymbolicChildRolePosition(ORConnectivityPosition
                    .getInstance(ORConnectivityPosition.CLOSE_BY));
            logicalDSNotation.setMin0Symbol(ORNotationSymbol.getInstance(ORNotationSymbol.NONE));
            logicalDSNotation.setMin1Symbol(ORNotationSymbol.getInstance(ORNotationSymbol.NONE));
            logicalDSNotation.setMax1Symbol(ORNotationSymbol.getInstance(ORNotationSymbol.NONE));
            logicalDSNotation.setMaxNSymbol(ORNotationSymbol
                    .getInstance(ORNotationSymbol.EMPTY_HALF_LARGE_DIAMOND));
            logicalDSNotation.setChildRoleVisible(Boolean.FALSE);
            logicalDSNotation.setKeyDependencyVisible(Boolean.TRUE);
            logicalDSNotation.setKeyDependencySymbol(ORNotationSymbol
                    .getInstance(ORNotationSymbol.SINGLE_LINE));

            logicalDSNotation
                    .setChoiceShape(SMSNotationShape.getInstance(SMSNotationShape.DIAMOND));
            logicalDSNotation.setSpecializationShape(SMSNotationShape
                    .getInstance(SMSNotationShape.TRIANGLE));

            logicalDSNotation.setBuiltIn(Boolean.TRUE);
            if (orStyle != null) {
                logicalDSNotation.setDefaultStyle(orStyle);
            }

        }

        { // UML-like
            DbORNotation umlNotation = new DbORNotation(project);
            umlNotation.initOptions();
            umlNotation.setName(DbORNotation.UML);
            umlNotation.setTerminologyName(DbORNotation.UML);
            umlNotation.setNotationID(new Integer(UML));
            umlNotation.setMasterNotationID(new Integer(UML));
            umlNotation.setNotationMode(new Integer(DbSMSNotation.OR_MODE));
            umlNotation.setNumericDisplay(ORConnectivitiesDisplay
                    .getInstance(ORConnectivitiesDisplay.MIN_MAX));
            umlNotation.setNumericPosition(ORConnectivityPosition
                    .getInstance(ORConnectivityPosition.FAR_AWAY));
            umlNotation.setSymbolicDisplay(ORConnectivitiesDisplay
                    .getInstance(ORConnectivitiesDisplay.NONE));
            umlNotation.setSymbolicPosition(ORConnectivityPosition
                    .getInstance(ORConnectivityPosition.FAR_AWAY));
            umlNotation.setSymbolicChildRolePosition(ORConnectivityPosition
                    .getInstance(ORConnectivityPosition.CLOSE_BY));
            umlNotation.setChildRoleVisible(Boolean.FALSE);
            umlNotation.setKeyDependencyVisible(Boolean.FALSE);
            umlNotation.setKeyDependencySymbol(ORNotationSymbol.getInstance(ORNotationSymbol.NONE));
            umlNotation.setNumericRepresentation(ORNumericRepresentation
                    .getInstance(ORNumericRepresentation.UML));

            umlNotation.setChoiceShape(SMSNotationShape.getInstance(SMSNotationShape.DIAMOND));
            umlNotation.setSpecializationShape(SMSNotationShape
                    .getInstance(SMSNotationShape.TRIANGLE));

            umlNotation.setBuiltIn(Boolean.TRUE);
            if (orStyle != null) {
                umlNotation.setDefaultStyle(orStyle);
            }

        }

        {
            //
            // PRIVATE METHODS
            //
            /*
             * private void createIdefixNotation(DbORDataModel dataModel) throws DbException {
             * 
             * DbSMSProject proj = (DbSMSProject)dataModel.getProject(); DbObject o =
             * proj.findComponentByName(DbORNotation.metaClass, IDEFIX_NOTATION); if(o != null)
             * m_idefixNotation = (DbORNotation)o; else{ m_idefixNotation = new DbORNotation(proj);
             * m_idefixNotation.initOptions(); m_idefixNotation.setName(IDEFIX_NOTATION);
             * m_idefixNotation.setTerminologyName(IDEFIX_NOTATION);
             * m_idefixNotation.setNotationMode(new Integer(DbSMSNotation.OR_MODE));
             * m_idefixNotation.setNotationID(new Integer(IDEF1X));
             * m_idefixNotation.setMasterNotationID(new Integer(IDEF1X)); m_idefixNotation
             * .setNumericDisplay(ORConnectivitiesDisplay.getInstance
             * (ORConnectivitiesDisplay.NONE)); m_idefixNotation.setSymbolicDisplay
             * (ORConnectivitiesDisplay.getInstance (ORConnectivitiesDisplay.MIN_MAX));
             * m_idefixNotation.setMaxNSymbol (ORNotationSymbol.getInstance(ORNotationSymbol
             * .FILLED_SMALL_CIRCLE)); m_idefixNotation.setMin0Symbol(ORNotationSymbol
             * .getInstance(ORNotationSymbol.EMPTY_SMALL_DIAMOND)); m_idefixNotation
             * .setSymbolicPosition(ORConnectivityPosition.getInstance
             * (ORConnectivityPosition.FAR_AWAY)); m_idefixNotation.setBuiltIn(Boolean.FALSE); } }
             */// end createIdefixNotation()
        }

        initErNotations(project);

        // TODO : IDEF1X
    } // end initOrNotations()

    public static void initErNotations(DbSMSProject project) throws DbException {
        DbORStyle erStyle = (DbORStyle) project.getErDefaultStyle();
        { // Entity-Relationship
            DbORNotation erNotation = new DbORNotation(project);
            erNotation.initOptions();
            erNotation.setName(DbORNotation.ENTITY_RELATIONSHIP);
            erNotation.setTerminologyName(DbORNotation.ENTITY_RELATIONSHIP);
            erNotation.setNotationID(new Integer(ENTITY_RELATIONSHIP));
            erNotation.setMasterNotationID(new Integer(ENTITY_RELATIONSHIP));
            erNotation.setNotationMode(new Integer(DbSMSNotation.ER_MODE));
            erNotation.setNumericDisplay(ORConnectivitiesDisplay
                    .getInstance(ORConnectivitiesDisplay.MIN_MAX));
            erNotation.setNumericPosition(ORConnectivityPosition
                    .getInstance(ORConnectivityPosition.CLOSE_BY));
            erNotation.setSymbolicDisplay(ORConnectivitiesDisplay
                    .getInstance(ORConnectivitiesDisplay.NONE));
            erNotation.setKeyDependencyVisible(Boolean.FALSE);
            erNotation.setKeyDependencySymbol(ORNotationSymbol.getInstance(ORNotationSymbol.NONE));
            erNotation.setBuiltIn(Boolean.TRUE);
            project.setErDefaultNotation(erNotation);
            if (erStyle != null) {
                erNotation.setDefaultStyle(erStyle);
                erStyle.setDefaultStyle(Boolean.TRUE);
            }
        }
    } // end initErNotations()

    public static void initOrStyle() {
    	
    	OrStyle.init();
    	
        
    } // end initOrStyle()

    public static void initCommonItemStyle() {

        // Display options
        DbORCommonItemStyle.commonItem_displayOptions = new MetaField[] {
                DbORCommonItemStyle.fOr_commonItemLengthDecimalsDisplay,
                DbORCommonItemStyle.fOr_commonItemNullValueDisplay,
                DbORCommonItemStyle.fOr_commonItemTypeDisplay, };

        DbORCommonItemStyle.commonItem_displayOptionDefaultValues = new Boolean[] { Boolean.TRUE, // fOr_commonItemLengthDecimalsDisplay,
                Boolean.TRUE, // fOr_commonItemNullValueDisplay,
                Boolean.TRUE, // fOr_commonItemTypeDisplay,
        };

        // Display Descriptor options
        DbORCommonItemStyle.commonItem_descriptorOptions = new MetaField[] { DbORCommonItemStyle.fOr_nameDescriptor, };

        DbORCommonItemStyle.commonItem_descriptorOptionDefaultValues = new SMSDisplayDescriptor[] { SMSDisplayDescriptor
                .getInstance(SMSDisplayDescriptor.NAME), // fOr_nameDescriptor
        };

        // Font options
        DbORCommonItemStyle.commonItem_fontOptions = new MetaField[] {
                DbORCommonItemStyle.fDescriptorFontDbORCommonItem,
                DbORCommonItemStyle.fOr_commonItemLengthDecimalsFont,
                DbORCommonItemStyle.fOr_commonItemNullValueFont,
                DbORCommonItemStyle.fOr_commonItemTypeFont };

        Font plainFont = new Font("Arial", Font.PLAIN, 8);
        //Font italicFont = new Font("Arial", Font.ITALIC, 8);
        DbORCommonItemStyle.commonItem_fontOptionDefaultValues = new Font[] { plainFont, // fDescriptorFontDbORCommonItem,
                plainFont, // fOr_commonItemLengthDecimalsFont,
                plainFont, // fOr_commonItemNullValueFont
                plainFont, // fOr_commonItemTypeFont
        };

        // Color options
        Color lightYellow = new Color(255, 255, 204);
        DbORCommonItemStyle.commonItem_colorOptions = new MetaField[] {
                DbORCommonItemStyle.fBackgroundColorDbORCommonItem,
                DbORCommonItemStyle.fLineColorDbORCommonItem,
                DbORCommonItemStyle.fTextColorDbORCommonItem,
                DbORCommonItemStyle.fTextColorDbSMSNotice,
                DbORCommonItemStyle.fBackgroundColorDbSMSNotice,
                DbORCommonItemStyle.fLineColorDbSMSNotice,
 DbSMSStyle.fLineColorDbSMSGraphicalLink };

        DbORCommonItemStyle.commonItem_colorOptionDefaultValues = new Color[] { Color.white, // fBackgroundColorDbORCommonItem
                Color.red, // fLineColorDbORCommonItem
                Color.black, // fTextColorDbORCommonItem
                Color.black, // fTextColorDbSMSNotice
                lightYellow, // fBackgroundColorDbSMSNotice
                Color.black, // fLineColorDbSMSNotice
                Color.black, // fLineColorDbSMSGraphicalLink,
        };

        // Line Style Options
        DbORCommonItemStyle.commonItem_lineOptions = new MetaField[] {
                DbORCommonItemStyle.fDashStyleDbORCommonItem,
                DbORCommonItemStyle.fHighlightDbORCommonItem,
                DbORCommonItemStyle.fHighlightDbSMSNotice,
                DbORCommonItemStyle.fDashStyleDbSMSNotice,
 DbSMSStyle.fDashStyleDbSMSGraphicalLink,
                DbSMSStyle.fHighlightDbSMSGraphicalLink };

        DbORCommonItemStyle.commonItem_lineOptionDefaultValues = new Boolean[] { Boolean.FALSE, // fDashStyleDbORCommonItem
                Boolean.FALSE, // fHighlightDbORCommonItem
                Boolean.FALSE, // fHighlightDbSMSNotice
                Boolean.FALSE, // fDashStyleDbSMSNotice
                Boolean.TRUE, // fDashStyleDbSMSGraphicalLink
                Boolean.FALSE, // fHighlightDbSMSGraphicalLink
        };

        DbORCommonItemStyle.commonItem_listOptionTabs = new String[] { "commonItem_optionGroups",
                "commonItem_optionValueGroups", "commonItem_optionGroupHeaders" };

        DbORCommonItemStyle.commonItem_optionGroups = new MetaField[][] {
                DbORCommonItemStyle.commonItem_displayOptions,
                DbORCommonItemStyle.commonItem_descriptorOptions,
                DbORCommonItemStyle.commonItem_fontOptions,
                DbORCommonItemStyle.commonItem_colorOptions,
                DbORCommonItemStyle.commonItem_lineOptions };

        DbORCommonItemStyle.commonItem_optionValueGroups = new Object[][] {
                DbORCommonItemStyle.commonItem_displayOptionDefaultValues,
                DbORCommonItemStyle.commonItem_descriptorOptionDefaultValues,
                DbORCommonItemStyle.commonItem_fontOptionDefaultValues,
                DbORCommonItemStyle.commonItem_colorOptionDefaultValues,
                DbORCommonItemStyle.commonItem_lineOptionDefaultValues };

        DbORCommonItemStyle.commonItem_optionGroupHeaders = new String[] {
                org.modelsphere.sms.or.international.LocaleMgr.screen.getString("StructureDisplay"),
                org.modelsphere.sms.or.international.LocaleMgr.screen
                        .getString("DescriptorsDisplay"),
                org.modelsphere.sms.or.international.LocaleMgr.screen.getString("Font"),
                org.modelsphere.sms.or.international.LocaleMgr.screen.getString("Color"),
                org.modelsphere.sms.or.international.LocaleMgr.screen.getString("LineStyle") };
    } // end initCommonItemStyle()

    public static void initDomainStyle() {

        // Display options
        DbORDomainStyle.domain_displayOptions = new MetaField[] {
                DbORDomainStyle.fOr_domainOwnerDisplay,
                DbORDomainStyle.fOr_domainLengthDecimalsDisplay, DbORDomainStyle.fOr_fieldDisplay,
                DbORDomainStyle.fOr_fieldDefaultValueDisplay,
                DbORDomainStyle.fOr_fieldLengthDecimalsDisplay,
                DbORDomainStyle.fOr_fieldTypeDisplay, };

        DbORDomainStyle.domain_displayOptionDefaultValues = new Boolean[] { Boolean.FALSE, // or_domainOwnerDisplay
                Boolean.TRUE, // fOr_domainLengthDecimalsDisplay
                Boolean.TRUE, // fOr_fieldDisplay
                Boolean.TRUE, // fOr_fieldDefaultValueDisplay
                Boolean.TRUE, // fOr_fieldLengthDecimalsDisplay
                Boolean.TRUE, // fOr_fieldTypeDisplay
        };

        // Display Descriptor options
        DbORDomainStyle.domain_descriptorOptions = new MetaField[] { DbORDomainStyle.fOr_nameDescriptor };

        DbORDomainStyle.domain_descriptorOptionDefaultValues = new SMSDisplayDescriptor[] { SMSDisplayDescriptor
                .getInstance(SMSDisplayDescriptor.NAME) };

        Font plainFont = new Font("Arial", Font.PLAIN, 8);
        //Font italicFont = new Font("Arial", Font.ITALIC, 8);
        // Font options
        DbORDomainStyle.domain_fontOptions = new MetaField[] {
                DbORDomainStyle.fDescriptorFontDbORDomain,
                DbORDomainStyle.fOr_domainLengthDecimalsFont,
                DbORDomainStyle.fOr_fieldDescriptorFont, DbORDomainStyle.fOr_fieldDefaultValueFont,
                DbORDomainStyle.fOr_fieldLengthDecimalsFont, DbORDomainStyle.fOr_fieldTypeFont };

        DbORDomainStyle.domain_fontOptionDefaultValues = new Font[] { plainFont, // fDescriptorFontDbORDomain,
                plainFont, // fOr_domainLengthDecimalsFont
                plainFont, // fOr_fieldDescriptorFont,
                plainFont, // fOr_fieldDefaultValueFont,
                plainFont, // fOr_fieldLengthDecimalsFont,
                plainFont, // fOr_fieldTypeFont,
        };

        // Color options
        Color lightYellow = new Color(255, 255, 204);
        Color lightBlue = new Color(204, 240, 255);
        DbORDomainStyle.domain_colorOptions = new MetaField[] {
                DbORDomainStyle.fBackgroundColorDbORDomain, DbORDomainStyle.fLineColorDbORDomain,
                DbORDomainStyle.fTextColorDbORDomain, DbORDomainStyle.fTextColorDbSMSNotice,
                DbORDomainStyle.fBackgroundColorDbSMSNotice, DbORDomainStyle.fLineColorDbSMSNotice,
                DbSMSStyle.fLineColorDbSMSGraphicalLink };

        DbORDomainStyle.domain_colorOptionDefaultValues = new Color[] { lightBlue, // fBackgroundColorDbORDomain
                Color.blue, // fLineColorDbORDomain
                Color.black, // fTextColorDbORDomain
                Color.black, // fTextColorDbSMSNotice
                lightYellow, // fBackgroundColorDbSMSNotice
                Color.black, // fLineColorDbSMSNotice
                Color.black, // fLineColorDbSMSGraphicalLink,
        };

        // Line Style Options
        DbORDomainStyle.domain_lineOptions = new MetaField[] {
                DbORDomainStyle.fDashStyleDbORDomain, DbORDomainStyle.fHighlightDbORDomain,
                DbORDomainStyle.fHighlightDbSMSNotice, DbORDomainStyle.fDashStyleDbSMSNotice,
                DbSMSStyle.fDashStyleDbSMSGraphicalLink, DbSMSStyle.fHighlightDbSMSGraphicalLink };

        DbORDomainStyle.domain_lineOptionDefaultValues = new Boolean[] { Boolean.FALSE, // fDashStyleDbORDomain
                Boolean.FALSE, // fHighlightDbORDomain
                Boolean.FALSE, // fHighlightDbSMSNotice
                Boolean.FALSE, // fDashStyleDbSMSNotice
                Boolean.TRUE, // fDashStyleDbSMSGraphicalLink
                Boolean.FALSE, // fHighlightDbSMSGraphicalLink
        };

        // Prefix options
        DbORDomainStyle.domain_prefixOptions = new MetaField[] {
                DbORDomainStyle.fOr_domainNonOrderedCollectionPrefix,
                DbORDomainStyle.fOr_domainOrderedCollectionPrefix, };

        DbORDomainStyle.domain_prefixOptionDefaultValues = new DbtPrefix[] {
                new DbtPrefix(DbtPrefix.DISPLAY_NONE, "(NON-ORD)", null, null), // fOr_domainNonOrderedCollectionPrefix,
                new DbtPrefix(DbtPrefix.DISPLAY_NONE, "(ORD)", null, null), // fOr_domainOrderedCollectionPrefix,
        };

        DbORDomainStyle.domain_optionGroups = new MetaField[][] {
                DbORDomainStyle.domain_displayOptions, DbORDomainStyle.domain_descriptorOptions,
                DbORDomainStyle.domain_fontOptions, DbORDomainStyle.domain_prefixOptions,
                DbORDomainStyle.domain_colorOptions, DbORDomainStyle.domain_lineOptions };

        DbORDomainStyle.domain_optionValueGroups = new Object[][] {
                DbORDomainStyle.domain_displayOptionDefaultValues,
                DbORDomainStyle.domain_descriptorOptionDefaultValues,
                DbORDomainStyle.domain_fontOptionDefaultValues,
                DbORDomainStyle.domain_prefixOptionDefaultValues,
                DbORDomainStyle.domain_colorOptionDefaultValues,
                DbORDomainStyle.domain_lineOptionDefaultValues };

        DbORDomainStyle.domain_optionGroupHeaders = new String[] {
                org.modelsphere.sms.or.international.LocaleMgr.screen.getString("StructureDisplay"),
                org.modelsphere.sms.or.international.LocaleMgr.screen
                        .getString("DescriptorsDisplay"),
                org.modelsphere.sms.or.international.LocaleMgr.screen.getString("Font"),
                org.modelsphere.sms.or.international.LocaleMgr.screen.getString("Prefix"),
                org.modelsphere.sms.or.international.LocaleMgr.screen.getString("Color"),
                org.modelsphere.sms.or.international.LocaleMgr.screen.getString("LineStyle") };

        DbORDomainStyle.domain_listOptionTabs = new String[] { "domain_optionGroups",
                "domain_optionValueGroups", "domain_optionGroupHeaders" };

    } // end initDomainStyle()

    private static final String UML_STYLE = "UML"; // NOT LOCALIZABLE
    private static final String JAVA_STYLE = "Java"; // NOT LOCALIZABLE
    private static final String KEYS_AND_LOCKS = "Keys and Locks";

    private static final Color lightBeige = new Color(240, 240, 228);
    private static final Color brown = new Color(100, 50, 0);

    public static void createErStyle(DbSMSProject project) throws DbException {
        // OR ER Style
        DbORStyle erStyle = (DbORStyle) project.getErDefaultStyle();
        // if ((Debug.isDebug()) && (erStyle == null)) {
        if (erStyle == null) {
            erStyle = new DbORStyle(project);
            DbORStyle dfStyle = project.getOrDefaultStyle();
            erStyle.copyOptions(dfStyle);
            erStyle.setName(DbORNotation.ENTITY_RELATIONSHIP);
            erStyle.setOr_associationDescriptorDisplay(Boolean.TRUE);
            erStyle.setOr_checkDisplay(Boolean.FALSE);
            erStyle.setOr_fkColumnsDisplay(Boolean.FALSE);
            erStyle.setOr_fkDisplay(Boolean.FALSE);
            erStyle.setOr_fkColumnsPrefix(new DbtPrefix(DbtPrefix.DISPLAY_NONE, null, null, null));
            erStyle.setOr_indexDisplay(Boolean.FALSE);
            erStyle
                    .setOr_indexColumnsPrefix(new DbtPrefix(DbtPrefix.DISPLAY_NONE, null, null,
                            null));
            erStyle.setOr_triggerDisplay(Boolean.FALSE);
            erStyle.setOr_associationAsRelationships(Boolean.TRUE);
            erStyle.setOr_associationTablesAsRelationships(Boolean.TRUE);
            erStyle.setOr_associationDescriptorDisplay(Boolean.TRUE);
            erStyle.setOr_showDependentTables(Boolean.FALSE);
            erStyle.setOr_UnidentifyingAssociationsAreDashed(Boolean.FALSE);
            erStyle.setOr_columnNullValueDisplay(Boolean.FALSE);
            erStyle.setBackgroundColorDbORTable(lightBeige);
            erStyle.setLineColorDbORTable(brown);
            erStyle.setOr_pkDisplay(Boolean.FALSE);
            erStyle.setOr_ukDisplay(Boolean.FALSE);
            erStyle.setOr_fkDisplay(Boolean.FALSE);
            project.setErDefaultStyle(erStyle);

            Font boldFont = new Font("Arial", Font.PLAIN, 8);
            erStyle.setOr_associationDescriptorFont(boldFont);
        }
    }

    public static void createUMLStyles(DbSMSProject project, boolean createAlways)
            throws DbException {

        DbBEStyle bestyle = (DbBEStyle) project.findComponentByName(DbBEStyle.metaClass,
                DbBEStyle.UML_SEQUENCE_STYLE_NAME);

        if (bestyle != null && !createAlways)
            return;

        DbBEStyle beStyle = new DbBEStyle(project);
        beStyle.setName(DbBEStyle.UML_USE_CASE_STYLE_NAME);
        beStyle.setDefaultStyle(Boolean.TRUE);
        beStyle.initOptions();
        beStyle.setBackgroundColorDbBEUseCase(new Color(255, 255, 225));
        beStyle.setLineColorDbBEUseCase(Color.BLACK);

        beStyle = new DbBEStyle(project);
        beStyle.setName(DbBEStyle.UML_SEQUENCE_STYLE_NAME);
        beStyle.setDefaultStyle(Boolean.TRUE);
        beStyle.initOptions();
        beStyle.setBackgroundColorDbBEUseCase(Color.WHITE);
        beStyle.setLineColorDbBEUseCase(new Color(5, 5, 161));

        beStyle = new DbBEStyle(project);
        beStyle.setName(DbBEStyle.UML_STATE_STYLE_NAME);
        beStyle.setDefaultStyle(Boolean.TRUE);
        beStyle.initOptions();
        beStyle.setDisplayStereotypeOnly(Boolean.TRUE);
        beStyle.setBackgroundColorDbBEUseCase(new Color(240, 240, 255));
        beStyle.setLineColorDbBEUseCase(Color.BLACK);

        beStyle = new DbBEStyle(project);
        beStyle.setName(DbBEStyle.UML_COLLABORATION_STYLE_NAME);
        beStyle.setDefaultStyle(Boolean.TRUE);
        beStyle.initOptions();
        beStyle.setBackgroundColorDbBEUseCase(new Color(255, 204, 153));
        beStyle.setLineColorDbBEUseCase(Color.BLACK);

        beStyle = new DbBEStyle(project);
        beStyle.setName(DbBEStyle.UML_ACTIVITY_STYLE_NAME);
        beStyle.setDefaultStyle(Boolean.TRUE);
        beStyle.initOptions();
        beStyle.setDisplayStereotypeOnly(Boolean.TRUE);
        beStyle.setBackgroundColorDbBEUseCase(Color.WHITE);
        beStyle.setLineColorDbBEUseCase(new Color(102, 0, 153));

        beStyle = new DbBEStyle(project);
        beStyle.setName(DbBEStyle.UML_COMPONENT_STYLE_NAME);
        beStyle.setDefaultStyle(Boolean.TRUE);
        beStyle.initOptions();
        beStyle.setBackgroundColorDbBEUseCase(new Color(102, 102, 255));
        beStyle.setLineColorDbBEUseCase(new Color(0, 51, 204));

        beStyle = new DbBEStyle(project);
        beStyle.setName(DbBEStyle.UML_DEPLOYMENT_STYLE_NAME);
        beStyle.setDefaultStyle(Boolean.TRUE);
        beStyle.initOptions();
        beStyle.setBackgroundColorDbBEUseCase(new Color(102, 102, 255));
        beStyle.setLineColorDbBEUseCase(new Color(0, 51, 204));

    }

    public static void createDefaultStyles(DbSMSProject project) throws DbException {
        // OO Default Style
        if (project.getOoDefaultStyle() == null) {
            DbOOStyle ooStyle = new DbOOStyle(project);
            // ooStyle.setName(DbOOStyle.DEFAULT_STYLE_NAME);
            ooStyle.setName(UML_STYLE);
            ooStyle.setDefaultStyle(Boolean.TRUE);
            ooStyle.initOptions();
            ooStyle.setOojv_associationNameDisplayed(Boolean.TRUE);
            ooStyle.setOojv_associationAttributeDisplayed(Boolean.FALSE);
            ooStyle.setOojv_associationAttributeNameLabelDisplayed(Boolean.FALSE);
            ooStyle.setOojv_abstractModifierPrefix(new DbtPrefix(DbtPrefix.DISPLAY_NONE, "", null,
                    null));
            ooStyle.setOojv_staticModifierPrefix(new DbtPrefix(DbtPrefix.DISPLAY_NONE, "", null,
                    null));
            ooStyle.setOojv_finalModifierPrefix(new DbtPrefix(DbtPrefix.DISPLAY_TEXT, "\\", null,
                    null));
            ooStyle.setOojv_transientModifierPrefix(new DbtPrefix(DbtPrefix.DISPLAY_NONE, "", null,
                    null));
            ooStyle.setOojv_umlInterfaceShownAsCircle(Boolean.TRUE);
            // ooStyle.setRecentModificationDisplay(SMSRecentModificationOption.getInstance(SMSRecentModificationOption.DO_NOT_SHOW_RECENT_MODIFS));
            project.setOoDefaultStyle(ooStyle);
        }

        DbOOStyle javaStyle = (DbOOStyle) project.findComponentByName(DbOOStyle.metaClass,
                JAVA_STYLE);
        if (javaStyle == null) {
            javaStyle = new DbOOStyle(project);
            javaStyle.setName(JAVA_STYLE);
            javaStyle.initOptions();
            javaStyle.setOojv_associationNameDisplayed(Boolean.TRUE);
            javaStyle.setOojv_associationAttributeDisplayed(Boolean.TRUE);
            javaStyle.setOojv_associationAttributeNameLabelDisplayed(Boolean.TRUE);
            javaStyle.setOojv_packageVisibilityPrefix(new DbtPrefix(DbtPrefix.DISPLAY_NONE, "",
                    null, null));
            javaStyle.setOojv_umlForceItalicOnAbstract(Boolean.FALSE);
            javaStyle.setOojv_umlForceUnderlineOnStatic(Boolean.FALSE);
            javaStyle.setOojv_umlInterfaceShownAsCircle(Boolean.FALSE);
            javaStyle.setOojv_umlTypeBeforeName(Boolean.TRUE);
            javaStyle.setDefaultStyle(Boolean.TRUE);

        } // end if

        // KEYS_AND_LOCKS STYLE
        DbOOStyle roseStyle = (DbOOStyle) project.findComponentByName(DbOOStyle.metaClass,
                KEYS_AND_LOCKS);
        if (roseStyle == null) {
            roseStyle = new DbOOStyle(project);
            roseStyle.setName(KEYS_AND_LOCKS);
            roseStyle.initOptions();
            roseStyle.setOojv_associationNameDisplayed(Boolean.TRUE);
            roseStyle.setOojv_associationAttributeDisplayed(Boolean.FALSE);
            roseStyle.setOojv_associationAttributeNameLabelDisplayed(Boolean.FALSE);
            roseStyle.setDefaultStyle(Boolean.TRUE);

            ImageIcon privateIcon = org.modelsphere.sms.oo.international.LocaleMgr.screen
                    .getImageIcon("private");
            ImageIcon packageIcon = org.modelsphere.sms.oo.international.LocaleMgr.screen
                    .getImageIcon("package");
            ImageIcon protectedIcon = org.modelsphere.sms.oo.international.LocaleMgr.screen
                    .getImageIcon("protected");
            ImageIcon publicIcon = org.modelsphere.sms.oo.international.LocaleMgr.screen
                    .getImageIcon("public");
            roseStyle.setOojv_privateVisibilityPrefix(new DbtPrefix(DbtPrefix.DISPLAY_IMAGE, "",
                    "private", privateIcon.getImage()));
            roseStyle.setOojv_packageVisibilityPrefix(new DbtPrefix(DbtPrefix.DISPLAY_IMAGE, "",
                    "package", packageIcon.getImage()));
            roseStyle.setOojv_protectedVisibilityPrefix(new DbtPrefix(DbtPrefix.DISPLAY_IMAGE, "",
                    "protected", protectedIcon.getImage()));
            roseStyle.setOojv_publicVisibilityPrefix(new DbtPrefix(DbtPrefix.DISPLAY_IMAGE, "",
                    "public", publicIcon.getImage()));
        } // end if

        // OR Default Style
        if (project.getOrDefaultStyle() == null) {
            DbORStyle orStyle = new DbORStyle(project);
            orStyle.setName(DbORStyle.DEFAULT_STYLE_NAME);
            orStyle.setDefaultStyle(Boolean.TRUE);
            orStyle.initOptions();
            orStyle.setOr_associationDescriptorDisplay(Boolean.TRUE);
            project.setOrDefaultStyle(orStyle);

            createErStyle(project);
        }

        // Domain Default Style
        if (project.getOrDefaultDomainStyle() == null) {
            DbORDomainStyle domainStyle = new DbORDomainStyle(project);
            domainStyle.setName(DbORDomainStyle.DEFAULT_STYLE_NAME);
            domainStyle.setDefaultStyle(Boolean.TRUE);
            domainStyle.initOptions();
            project.setOrDefaultDomainStyle(domainStyle);
        }

        // Common Item Default Style
        if (project.getOrDefaultCommonItemStyle() == null) {
            DbORCommonItemStyle CommonItemStyle = new DbORCommonItemStyle(project);
            CommonItemStyle.setName(DbORCommonItemStyle.DEFAULT_STYLE_NAME);
            CommonItemStyle.setDefaultStyle(Boolean.TRUE);
            CommonItemStyle.initOptions();
            project.setOrDefaultCommonItemStyle(CommonItemStyle);
        }

        // Use Case Default Style
        if (project.getBeDefaultStyle() == null) {
            DbBEStyle beStyle = new DbBEStyle(project);
            beStyle.setName(DbBEStyle.DEFAULT_STYLE_NAME);
            beStyle.setDefaultStyle(Boolean.TRUE);
            beStyle.initOptions();
            project.setBeDefaultStyle(beStyle);

            createUMLStyles(project, false);

        }

    } // end createDefaultStyles

    public static void initAssociation(DbSMSAssociation thisAssoc) throws DbException {
        thisAssoc.setToFrontEnd(Boolean.TRUE);
    } // end initAssociation()
    
    public static void initChoiceOrSpecialization(DbORAbsTable absTable) throws DbException {
        DbORChoiceOrSpecialization choiceSpec = (DbORChoiceOrSpecialization) absTable;
        ORChoiceSpecializationCategory categ = ORChoiceSpecializationCategory
                .getInstance(ORChoiceSpecializationCategory.SPECIALIZATION);
        choiceSpec.setCategory(categ);
    } // end initAssociation()

    //may return null
    public static Object findStyleElement(DbSMSStyle thisStyle, MetaField metaField)
            throws DbException {
        Object value = null;

        Db db = thisStyle.getDb();
        db.beginReadTrans();
        while (true) {
            value = thisStyle.get(metaField);
            if (value != null)
                break;
            thisStyle = thisStyle.getAncestor();
            if (thisStyle == null) {
                //db.commitTrans();
                //throw new RuntimeException("Root style with a null value");
                break;
            }
        }
        db.commitTrans();
        return value;
    } // end initAssociation()

    public static Object findStyleElement(DbSMSGraphicalObject thisFigure, MetaField metaField)
            throws DbException {
        if (thisFigure.hasField(metaField) && thisFigure.get(metaField) != null)
            return thisFigure.get(metaField);

        DbSMSStyle style = thisFigure.findStyle();
        MetaField styleMF = null;
        String nameMetaField = null;
        String nameGoField = metaField.getJName();

        styleMF = style.getMetaField(nameGoField);
        if (styleMF != null)
            return style.find(styleMF);
        else {
            MetaClass goMetaClass = thisFigure.getMetaClass();
            while (goMetaClass != null) {
                nameMetaField = goMetaClass.getJClass().getName();
                nameMetaField = nameMetaField.substring(nameMetaField.lastIndexOf('.') + 1);
                //styleMF = style.getMetaField(nameGoField.concat(nameMetaField));
                String metafieldName = buildMetaFieldName(goMetaClass, metaField); 
                styleMF = style.getMetaField(metafieldName);
                
                if (styleMF != null)
                    return style.find(styleMF);
                goMetaClass = goMetaClass.getSuperMetaClass();

            }
            DbObject so = thisFigure.getSO();
            if (so != null) {
                MetaClass soMetaClass = so.getMetaClass();
                while (soMetaClass != null) {
                    nameMetaField = soMetaClass.getJClass().getName();
                    nameMetaField = nameMetaField.substring(nameMetaField.lastIndexOf('.') + 1);
                    //nameGoField.concat(nameMetaField);
                    String metafieldName = buildMetaFieldName(soMetaClass, metaField); 
                    styleMF = style.getMetaField(metafieldName);
                    if (styleMF != null)
                        return style.find(styleMF);
                    soMetaClass = soMetaClass.getSuperMetaClass();
                }
            }
            return null;
        }
    }

    private static String buildMetaFieldName(MetaClass soMetaClass, MetaField metaField) {
        String nameMetaField = soMetaClass.getJClass().getName();
        if (soMetaClass.equals(DbORChoiceOrSpecialization.metaClass)) {
           // nameMetaField = "DbORChoiceOrSpecialization"; //bug fixed
            int i=0;
            i++;
        }
        
        String nameGoField = metaField.getJName();
        nameMetaField = nameMetaField.substring(nameMetaField.lastIndexOf('.') + 1);
        String metafieldName = nameGoField.concat(nameMetaField);
        return metafieldName;
    }

    public static Rectangle computeRectangle(DbSMSGraphicalObject thisFigure, Rectangle oldValue)
            throws DbException {
        Rectangle newValue = oldValue;

        /*
         * if (!ApplicationDiagram.lockGridAlignment && Grid.getGrid().isActive()) { int x =
         * value.x; int y = value.y; Point pt = Grid.getGrid().getClosestGridBoundary(new Point(x,
         * y));
         * 
         * //if the values changed,we need to compensate for the offset if (pt.x != x || pt.y != y)
         * { value.x = pt.x; value.y = pt.y; } }
         */

        return newValue;
    }

	public static void removeChoiceSpec(DbORChoiceOrSpecialization choiceOrSpecialization) throws DbException {
	    //for each related association
	    DbORDiagram diagram = null;
		DbEnumeration enu = choiceOrSpecialization.getAssociations().elements(DbORAssociation.metaClass);  
		while (enu.hasMoreElements()) {
			DbORAssociation assoc = (DbORAssociation)enu.nextElement(); 
			assoc.setChoiceOrSpecialization(null); //disconnect from choice/spec
			
			//delete existing gos
			DbEnumeration enu2 = assoc.getAssociationGos().elements(DbORAssociationGo.metaClass);
			while (enu2.hasMoreElements()) {
			    DbORAssociationGo go = (DbORAssociationGo)enu2.nextElement();
			    diagram = (DbORDiagram)go.getCompositeOfType(DbORDiagram.metaClass);
			    go.remove();
			}
			enu2.close();
			
			//create a new go
			if (diagram != null) {
			  AnyGo.createORAssociationGo(diagram, assoc, (Point)null);    
			}
			//assoc.remove(); 
		} //end while
		enu.close(); 
	}

} // end DbInitialization
