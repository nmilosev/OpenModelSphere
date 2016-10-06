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

package org.modelsphere.sms.be.graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.DbUDF;
import org.modelsphere.jack.baseDb.db.DbUDFValue;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbRefreshPassListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.db.srtypes.ZoneJustification;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelation;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.zone.CellEditor;
import org.modelsphere.jack.graphic.zone.CellRenderer;
import org.modelsphere.jack.graphic.zone.CellRenderingOption;
import org.modelsphere.jack.graphic.zone.ColumnCellsOption;
import org.modelsphere.jack.graphic.zone.MatrixCellID;
import org.modelsphere.jack.graphic.zone.MatrixZone;
import org.modelsphere.jack.graphic.zone.StringCellRenderer;
import org.modelsphere.jack.graphic.zone.Zone;
import org.modelsphere.jack.graphic.zone.ZoneCell;
import org.modelsphere.jack.srtool.graphic.DbTextAreaCellEditor;
import org.modelsphere.jack.srtool.graphic.DbTextFieldCellEditor;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.StringUtil;
import org.modelsphere.sms.SMSSemanticalModel;
import org.modelsphere.sms.be.db.DbBEContextGo;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEQualifier;
import org.modelsphere.sms.be.db.DbBEResource;
import org.modelsphere.sms.be.db.DbBESingleZoneDisplay;
import org.modelsphere.sms.be.db.DbBEStyle;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.DbBEUseCaseGo;
import org.modelsphere.sms.be.db.DbBEUseCaseQualifier;
import org.modelsphere.sms.be.db.DbBEUseCaseResource;
import org.modelsphere.sms.be.db.srtypes.BETimeUnit;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.srtypes.SMSNotationShape;
import org.modelsphere.sms.db.srtypes.SMSZoneOrientation;
import org.modelsphere.sms.graphic.SMSStereotypeEditor;

public class BEUseCaseBox extends GraphicNode implements ActionInformation, DbRefreshListener {

    static {
        BoxRefreshTg useCaseRefreshTg = new BoxRefreshTg();
        Db.addDbRefreshPassListener(useCaseRefreshTg);
        MetaField.addDbRefreshListener(useCaseRefreshTg, null, new MetaField[] {
                DbObject.fComponents, DbSemanticalObject.fName, DbSemanticalObject.fAlias,
                DbSemanticalObject.fPhysicalName, DbBEUseCase.fClassifierGos, DbUDFValue.fValue,
                DbBEUseCase.fAlphanumericIdentifier, DbBEUseCase.fDescription,
                DbBEUseCase.fFixedCost, DbBEUseCase.fFixedTime, DbBEUseCase.fFixedTimeUnit,
                DbBEUseCase.fNumericIdentifier, DbBEUseCase.fPartialCost, DbBEUseCase.fPartialTime,
                DbBEUseCase.fPartialTimeUnit, DbBEUseCase.fResourceCost, DbBEUseCase.fResourceTime,
                DbBEUseCase.fResourceTimeUnit, DbBEUseCase.fSynchronizationRule,
                DbBEUseCase.fTotalCost, DbBEUseCase.fTotalTime, DbBEUseCase.fTotalTimeUnit,
                DbBEUseCase.fExternal, DbBEUseCase.fControl,
                DbBEUseCase.fSourceAlphanumericIdentifier, DbBEUseCase.fSourceNumericIdentifier,
                DbBEUseCase.fUmlStereotype, DbBEUseCase.fUmlConstraints });

    }
    String EXPLODED_SUFFIX = ".*"; // NOT LOCALIZABLE
    // the db reference
    protected DbBEUseCaseGo useCaseGO;
    protected DbBEUseCase useCaseSO;
    protected DbBENotation notation;
    private HashMap zonesMap = null;
    private DbBEDiagram beDiagram;
    private ArrayList zones = null;
    private ArrayList resources = null;
    private BEQualifier qualifiersGo = null;
    private ArrayList qualifiers = null;
    private String boxName = null;
    private DbObject composite = null;

    private DbBEStyle m_style = null;
    private Boolean m_iconOnly = null;
    private Boolean m_icon = null;
    private DbSMSStereotype m_stereotype = null;
    private Image m_stereotypeIcon = null;
    private Rectangle m_boxRect = null;

    // listeners
    transient private BoxGoRefreshTg useCaseGoRefreshTg = new BoxGoRefreshTg();

    public BEUseCaseBox(Diagram diag, DbBEUseCaseGo newBoxGO) throws DbException {
        super(diag, null);
        useCaseGO = newBoxGO;

        setAutoFit(useCaseGO.isAutoFit());
        setRectangle(useCaseGO.getRectangle());
        beDiagram = ((DbBEDiagram) newBoxGO.getComposite());
        useCaseSO = (DbBEUseCase) newBoxGO.getSO();
        if (useCaseSO != null) {
            composite = useCaseSO.getComposite();
            objectInit();

            DbBEUseCase process = (DbBEUseCase) getSemanticalObject();
            DbBEUseCaseGo processGo = (DbBEUseCaseGo) getGraphicalObject();
            m_style = (DbBEStyle) useCaseGO.findStyle();
            m_iconOnly = m_style.getDisplayStereotypeOnly();
            m_icon = m_style.getDisplayStereotypeIcon();
            m_stereotype = process.getUmlStereotype();
            m_stereotypeIcon = (m_stereotype == null) ? null : m_stereotype.getIcon();
            m_boxRect = processGo.getRectangle();
        } // end if
    } // end BEUseCaseBox()

    // BEWARE: diagView is null for printing
    // overridden
    private static final int GAP = 4;

    public void paint(Graphics g, DiagramView diagView, int drawingMode, int renderingFlags) {
        boolean boxDrawn = false;
        Rectangle innerRect = null;
        Rectangle boxRect = getSelectionAreaRectangle();

        // do not paint box if icon_only is true and an iconed stereotype exists
        boolean iconOnly = (m_iconOnly == null) ? false : m_iconOnly.booleanValue();
        if (!(iconOnly) || (m_stereotype == null) || (m_stereotypeIcon == null)) {
            super.paint(g, diagView, drawingMode, renderingFlags);
            boxDrawn = true;
        }

        boolean iconDrawn = false;
        if (m_stereotype != null) {
            Image image = m_stereotypeIcon;
            if (image != null) {
                Enumeration enumeration = displayedZones();
                while (enumeration.hasMoreElements()) {
                    Zone zone = (Zone) enumeration.nextElement();
                    String id = zone.getNameID();
                    if (id != null) {
                        String stereotypeName = DbBEUseCase.fUmlStereotype.getGUIName();
                        if (id.equals(stereotypeName)) {

                            float zoomFactor = 1.0f;
                            if (diagView != null) {
                                zoomFactor = diagView.getZoomFactor();
                            }

                            innerRect = zone.getRectangle();
                            int width = (int) (zoomFactor * image.getWidth(null));
                            int height = (int) (zoomFactor * image.getHeight(null));
                            int gap = (int) (GAP / zoomFactor);
                            int x, y;
                            if (!boxDrawn) { // if draw icon only, paint it in
                                // the center
                                x = (int) (zoomFactor * (boxRect.x + boxRect.width / 2 - (width / 2)));
                                y = (int) (zoomFactor * (boxRect.y + boxRect.height / 2 - (height / 2)));
                            } else {
                                x = (int) (zoomFactor * (boxRect.x + boxRect.width - height - gap));
                                y = (int) (zoomFactor * (boxRect.y + innerRect.y + gap));
                            } // end if

                            if (m_iconOnly.booleanValue() || m_icon.booleanValue())
                                g.drawImage(image, x, y, width, height, null);

                            iconDrawn = true;
                        } // end if
                    } // end if
                } // end while
            } // end if
        } // end if

        // if neither box nor icon was drawn, draw at least the box
        if ((!iconDrawn) && (!boxDrawn)) {
            super.paint(g, diagView, drawingMode, renderingFlags);
        } // end if

    } // end paint()

    //
    // private & protected methods
    //

    private void updateNotation() throws DbException {
        if (notation != null) {
            notation.removeDbRefreshListener(this);
        }
        notation = beDiagram.findNotation();
        notation.addDbRefreshListener(this, DbRefreshListener.CALL_FOR_EVERY_FIELD);
    }

    private void setRefreshListener() throws DbException {
        composite.addDbRefreshListener(this);
        beDiagram.addDbRefreshListener(this, DbRefreshListener.CALL_FOR_EVERY_FIELD);
        updateNotation();
        beDiagram.getProject().addDbRefreshListener(this, DbRefreshListener.CALL_FOR_EVERY_FIELD);
        useCaseGO.addDbRefreshListener(useCaseGoRefreshTg);
    }

    private void removeRefreshListener() {
        if (composite != null)
            composite.removeDbRefreshListener(this);

        if (notation != null)
            notation.removeDbRefreshListener(this);

        beDiagram.removeDbRefreshListener(this);
        beDiagram.getProject().removeDbRefreshListener(this);
        useCaseGO.removeDbRefreshListener(useCaseGoRefreshTg);
        removeResoucesListener();
    }

    // overridden
    public int getDefaultFitMode() {
        return GraphicComponent.TOTAL_FIT;
    }

    protected void objectInit() throws DbException {
        useCaseGO.setGraphicPeer(this);
        setRefreshListener();
        initShapeAndBehavior();
        setZones();
        initStyleElements();
        populateContents();
        populateQualifiers();
    }

    protected void initShapeAndBehavior() throws DbException {
        setShape(SMSNotationShape.getShape(notation.getUseCaseShape().getValue()));
        if (notation.getUseCaseZoneOrientation().equals(
                SMSZoneOrientation.getInstance(SMSZoneOrientation.PERPENDICULAR)))
            this.setTwoColumns(33, 1, true);
        else
            this.setOneColumn();
    }

    private MetaField getStyleMetaField(String genericName) throws DbException {
        MetaField mf = null;
        String nameMetaField;

        MetaClass soMetaClass = useCaseSO.getMetaClass();
        nameMetaField = soMetaClass.getJClass().getName();
        nameMetaField = nameMetaField.substring(nameMetaField.lastIndexOf('.') + 1);

        return useCaseGO.findStyle().getMetaField(genericName.concat(nameMetaField));
    }

    private CellRenderingOption getCellRenderingOptionForConcept(DbBESingleZoneDisplay zoneDisplay,
            CellRenderer renderer, int alignment) throws DbException {
        Font font = null;
        MetaField mf = getFontStyleMetaField(zoneDisplay);
        if (mf != null)
            font = (Font) useCaseGO.find(mf);

        CellRenderingOption cellOption = new CellRenderingOption(renderer != null ? renderer
                : new StringCellRenderer(false, true), font, alignment);
        return cellOption;
    }

    private MetaField getFontStyleMetaField(DbBESingleZoneDisplay zoneDisplay) throws DbException {
        String suffix = "FontDbBEUseCase";
        MetaField metaField = null;
        DbBEStyle style = (DbBEStyle) useCaseGO.findStyle();
        if (zoneDisplay.getMetaField() != null
                && zoneDisplay.getMetaField().equals(DbBEUseCaseResource.fResource))
            metaField = DbBEStyle.fResourcesFontDbBEUseCase;
        else if (zoneDisplay.getMetaField() != null) {
            MetaField mf = zoneDisplay.getMetaField();
            metaField = style.getMetaField(mf.getJName() + suffix);
            if (metaField == null) {
                metaField = DbBEStyle.fNameFontDbBEUseCase;
                if (Debug.isDebug())
                    System.out.println("*************** MetaField without FONT (" + mf.getJName()
                            + suffix + ")**************");
            }
        } else if (zoneDisplay.getUdf() != null)
            metaField = DbBEStyle.fUdfFontDbBEUseCase;
        else {
            metaField = DbBEStyle.fNameFontDbBEUseCase;
            if (Debug.isDebug())
                System.out.println("************* DbBESingleZoneDisplay without FONT ("
                        + (zoneDisplay.getUdf() != null ? "UDF)" : "OTHER)") + "*************");
        }
        return metaField;
    }

    public Db getDb() {
        return useCaseGO.getDb();
    }

    public final DbObject getSemanticalObject() {
        return useCaseSO;
    }

    public final DbObject getGraphicalObject() {
        return useCaseGO;
    }

    public final boolean selectAtCellLevel() {
        return true;
    }

    // BEWARE: overriding triggers must call super.delete as last action
    public void delete(boolean all) {
        useCaseGO.setGraphicPeer(null);
        removeRefreshListener();
        removeZonesListener();
        removeQualifiersListener();
        Enumeration enumeration = this.displayedZones();
        while (enumeration.hasMoreElements()) {
            Zone zone = (Zone) enumeration.nextElement();
            if (zone instanceof MatrixZone)
                try {
                    ((MatrixZone) zone).removeAllRows();
                } catch (Exception e) {
                }
        }
        super.delete(all);
    }

    private void setZones() throws DbException {
        clearAllZones();
        if (zonesMap != null)
            removeZonesListener();
        zonesMap = new HashMap();
        zones = notation.getUseCaseZones();
        Iterator i = zones.iterator();
        int index = 0;
        while (i.hasNext()) {
            DbBESingleZoneDisplay zoneDisplay = (DbBESingleZoneDisplay) i.next();

            zoneDisplay.getDb().beginReadTrans();
            boolean displayed = zoneDisplay.isDisplayed();
            zoneDisplay.getDb().commitTrans();
            if (!displayed)
                continue;
            MatrixZone zone = null;
            if (zoneDisplay.getMetaField() != null
                    && zoneDisplay.getMetaField().equals(DbBEUseCaseResource.fResource)) {
                zone = getResourcesZone(zoneDisplay);
            } else {
                int alignment = ZoneJustification.getGraphicUtilAligmentValue(zoneDisplay
                        .getJustification().getValue());
                zone = new MatrixZone(zoneDisplay.getGUIName(), alignment);

                CellEditor tfEditor = null;
                if (zoneDisplay.getMetaField() != null
                        && !(zoneDisplay.getMetaField() instanceof MetaRelation))
                    if (zoneDisplay.getMetaField() == DbBEUseCase.fDescription)
                        tfEditor = new DbTextAreaCellEditor(zoneDisplay.getMetaField());
                    else
                        tfEditor = new DbTextFieldCellEditor(zoneDisplay.getMetaField(), false);

                Object value = zoneDisplay.getValue(useCaseSO);
                CellRenderingOption cro = this.getCellRenderingOptionForConcept(zoneDisplay, null,
                        alignment);
                if (value instanceof String || value == null) {
                    if (value == null)
                        value = new String("");
                    if (cro == null)
                        cro = new CellRenderingOption(new StringCellRenderer(false, true),
                                alignment);
                    String stringValue = value.toString();
                    if (stringValue == null)
                        cro.setCanReceiveFocus(false);
                    else if (stringValue.equals(""))
                        cro.setCanReceiveFocus(false);
                }
                zone.addColumn(new ColumnCellsOption(cro, tfEditor, false));
                zone.setWrappingCol(0);
                notation.getDb().beginReadTrans();
                if (index == 0
                        && notation.getUseCaseZoneOrientation().equals(
                                SMSZoneOrientation.getInstance(SMSZoneOrientation.PERPENDICULAR))) {
                    this.setVerticalLine(zoneDisplay.isSeparator());
                    zone.setHasBottomLine(false);
                } else
                    zone.setHasBottomLine(zoneDisplay.isSeparator());
                notation.getDb().commitTrans();
            }
            if (zone != null) {
                zonesMap.put(zoneDisplay, zone);
                addZone(zone);
                index++;
            }
        }
        addZonesListener();
    }

    private MatrixZone getResourcesZone(DbBESingleZoneDisplay zoneDisplay) throws DbException {
        int alignment = ZoneJustification.getGraphicUtilAligmentValue(zoneDisplay
                .getJustification().getValue());
        MatrixZone zone = new MatrixZone(zoneDisplay.getGUIName(), alignment);
        CellEditor tfEditor = null;
        zone.addColumn(new ColumnCellsOption(this.getCellRenderingOptionForConcept(zoneDisplay,
                null, alignment), tfEditor, false));
        zone.setWrappingCol(0);
        zone.setHasBottomLine(zoneDisplay.isSeparator());
        return zone;
    }

    private void addZonesListener() {

        Iterator i = zones.iterator();
        while (i.hasNext()) {
            DbBESingleZoneDisplay zoneDisplay = (DbBESingleZoneDisplay) i.next();
            zoneDisplay.addDbRefreshListener(this);
        }
    }

    private void removeZonesListener() {
        if (zones == null)
            return;

        Iterator i = zones.iterator();
        while (i.hasNext()) {
            DbBESingleZoneDisplay zoneDisplay = (DbBESingleZoneDisplay) i.next();
            zoneDisplay.removeDbRefreshListener(this);
        }
    } // end removeZonesListener()

    private void populateZones() throws DbException {
        boxName = useCaseSO.getName();
        int zonePos = 0;
        ArrayList zones = notation.getUseCaseZones();
        Iterator i = zones.iterator();
        while (i.hasNext()) {
            DbBESingleZoneDisplay zoneDisplay = (DbBESingleZoneDisplay) i.next();
            if (!zoneDisplay.isDisplayed())
                continue;

            boolean bSetStereotypeEditor = false;

            MatrixZone zone = (MatrixZone) zonesMap.get(zoneDisplay);
            if (zone != null) {
                zone.removeAllRows();
                if (zoneDisplay.getMetaField() != null
                        && zoneDisplay.getMetaField().equals(DbBEUseCaseResource.fResource))
                    populateResourcesZone(zone);
                else {
                    zone.addRow();
                    CellEditor newEditor = null;
                    Object value = null;
                    DbBEStyle style = (DbBEStyle) useCaseGO.findStyle();

                    MetaField mf = zoneDisplay.getMetaField();

                    if (mf != null) {
                        // fNumericIdentifier
                        if (mf.equals(DbBEUseCase.fNumericIdentifier)) {
                            if (style.getDisplayHierNumericIdDbBEUseCase().booleanValue()) {
                                value = useCaseSO.getSourceNumericIdentifier();
                                if (StringUtil.isEmptyString((String) value)) {
                                    value = useCaseSO.getNumericHierID();
                                    newEditor = new DbTextFieldCellEditor(
                                            DbBEUseCase.fNumericIdentifier, false);
                                } else
                                    newEditor = new DbTextFieldCellEditor(
                                            DbBEUseCase.fSourceNumericIdentifier, false);
                            } else
                                value = useCaseSO.getNumericIdentifier();
                        }
                        // fAlphanumericIdentifier
                        else if (mf.equals(DbBEUseCase.fAlphanumericIdentifier)) {
                            if (style.getDisplayHierAlphanumericIdDbBEUseCase().booleanValue()) {
                                value = useCaseSO.getSourceAlphanumericIdentifier();
                                if (StringUtil.isEmptyString((String) value)) {
                                    value = useCaseSO.getAlphanumericHierID();
                                    newEditor = new DbTextFieldCellEditor(
                                            DbBEUseCase.fAlphanumericIdentifier, false);
                                } else
                                    newEditor = new DbTextFieldCellEditor(
                                            DbBEUseCase.fSourceAlphanumericIdentifier, false);
                            } else
                                value = useCaseSO.getAlphanumericIdentifier();
                        }
                        // fFixedTime
                        else if (mf.equals(DbBEUseCase.fFixedTime)) {
                            value = useCaseSO.getFixedTime();
                            if (value != null) {
                                value = StringUtil.getDisplayString(value);
                                BETimeUnit timeUnit = useCaseSO.getFixedTimeUnit();
                                if (timeUnit != null) {
                                    value = ((String) value) + " " + timeUnit.toString(); // NOT
                                    // LOCALIZABLE
                                }
                            }
                        }
                        // fPartialTime
                        else if (mf.equals(DbBEUseCase.fPartialTime)) {
                            value = useCaseSO.getPartialTime();
                            if (value != null) {
                                value = StringUtil.getDisplayString(value);
                                BETimeUnit timeUnit = useCaseSO.getPartialTimeUnit();
                                if (timeUnit != null) {
                                    value = ((String) value) + " " + timeUnit.toString(); // NOT
                                    // LOCALIZABLE
                                }
                            }
                        }
                        // fResourceTime
                        else if (mf.equals(DbBEUseCase.fResourceTime)) {
                            value = useCaseSO.getResourceTime();
                            if (value != null) {
                                value = StringUtil.getDisplayString(value);
                                BETimeUnit timeUnit = useCaseSO.getResourceTimeUnit();
                                if (timeUnit != null) {
                                    value = ((String) value) + " " + timeUnit.toString(); // NOT
                                    // LOCALIZABLE
                                }
                            }
                        }
                        // fTotalTime
                        else if (mf.equals(DbBEUseCase.fTotalTime)) {
                            value = useCaseSO.getTotalTime();
                            if (value != null) {
                                value = StringUtil.getDisplayString(value);
                                BETimeUnit timeUnit = useCaseSO.getTotalTimeUnit();
                                if (timeUnit != null) {
                                    value = ((String) value) + " " + timeUnit.toString(); // NOT
                                    // LOCALIZABLE
                                }
                            }
                        }
                        // fUmlStereotype
                        else if (mf.equals(DbBEUseCase.fUmlStereotype)) {
                            DbSMSStereotype stereotype = useCaseSO.getUmlStereotype();
                            if (stereotype != null) {
                                value = "«" + stereotype.getName() + "»";
                                bSetStereotypeEditor = true;
                            }
                        }
                    }

                    if (value == null)
                        value = zoneDisplay.getValue(useCaseSO);

                    String strValue = StringUtil.getDisplayString(value);
                    if (zonePos == 0) {
                        String duplicate = this.calculateDuplicate(useCaseSO.getClassifierGos(),
                                useCaseGO);
                        if (strValue == null)
                            strValue = duplicate;
                        else if (duplicate != null)
                            strValue = strValue + duplicate;
                    }
                    boolean editable = false;
                    if (mf != null && !StringUtil.isEmptyString(strValue))
                        editable = true;
                    if (strValue == null)
                        strValue = "";
                    if (useCaseSO.isExternal() && zonePos == 0)
                        strValue += SMSSemanticalModel.kExternalSuffix;
                    else if (isUseCaseExploded(useCaseSO) && zonePos == 0)
                        strValue += EXPLODED_SUFFIX;
                    if (newEditor != null)
                        zone.set(0, 0, new ZoneCell(useCaseSO, strValue,
                                getCellRenderingOptionForConcept(zoneDisplay, null,
                                        ZoneJustification.getGraphicUtilAligmentValue(zoneDisplay
                                                .getJustification().getValue())), newEditor));
                    else {
                        if (StringUtil.isEmptyString(strValue))
                            zone.set(0, 0, new ZoneCell(useCaseSO, "", editable));
                        else
                            zone.set(0, 0, new ZoneCell(useCaseSO, strValue, editable));
                    }
                }

                if (bSetStereotypeEditor) {
                    MatrixCellID cellID = zone.getCellID(useCaseSO, 0);
                    if (cellID != null)
                        zone.setCellEditor(cellID, new SMSStereotypeEditor(
                                DbBEUseCase.fUmlStereotype));
                }

                zonePos++;
            }
        }
    }

    private boolean isUseCaseExploded(DbBEUseCase useCase) throws DbException {
        boolean isExploded = false;
        DbEnumeration dbEnum = useCase.getComponents().elements(DbBEDiagram.metaClass);
        while (dbEnum.hasMoreElements()) {
            isExploded = true;
            break;
        }
        dbEnum.close();
        if (isExploded)
            return isExploded;
        dbEnum = useCase.getComponents().elements(DbBEUseCase.metaClass);
        while (dbEnum.hasMoreElements()) {
            isExploded = true;
            break;
        }
        dbEnum.close();
        if (isExploded)
            return isExploded;
        dbEnum = useCase.getComponents().elements(DbBEFlow.metaClass);
        while (dbEnum.hasMoreElements()) {
            isExploded = true;
            break;
        }
        dbEnum.close();
        return isExploded;
    }

    private void populateResourcesZone(MatrixZone zone) throws DbException {
        if (resources != null)
            removeResoucesListener();
        resources = new ArrayList();
        DbEnumeration dbEnum = useCaseSO.getComponents().elements(DbBEUseCaseResource.metaClass);
        int row = 0;
        while (dbEnum.hasMoreElements()) {
            DbBEUseCaseResource useCaseResource = (DbBEUseCaseResource) dbEnum.nextElement();
            zone.addRow();
            zone.set(row, 0, new ZoneCell(useCaseSO, useCaseResource.getResource().getName()));
            resources.add(useCaseResource.getResource());
            row++;
        }
        dbEnum.close();
        addResoucesListener();
    }

    private void addResoucesListener() {
        if (resources == null)
            return;
        Iterator i = resources.iterator();
        while (i.hasNext()) {
            DbBEResource resource = (DbBEResource) i.next();
            resource.addDbRefreshListener(this);
        }
    }

    private void removeResoucesListener() {
        if (resources == null)
            return;
        Iterator i = resources.iterator();
        while (i.hasNext()) {
            DbBEResource resource = (DbBEResource) i.next();
            resource.removeDbRefreshListener(this);
        }
    }

    private void addQualifiersListener() {
        if (qualifiers == null)
            return;
        Iterator i = qualifiers.iterator();
        while (i.hasNext()) {
            DbBEQualifier qualifier = (DbBEQualifier) i.next();
            qualifier.addDbRefreshListener(this);
        }
    }

    private void removeQualifiersListener() {
        if (qualifiers == null)
            return;
        Iterator i = qualifiers.iterator();
        while (i.hasNext()) {
            DbBEQualifier qualifier = (DbBEQualifier) i.next();
            qualifier.removeDbRefreshListener(this);
        }
        qualifiers = null;
    }

    protected void setTextColor() throws DbException {
        Color color = (Color) useCaseGO.find(DbBEUseCaseGo.fTextColor);
        Enumeration enumeration = this.displayedZones();
        while (enumeration.hasMoreElements()) {
            Zone zone = (Zone) enumeration.nextElement();
            zone.setTextColor(color);
        }
    }

    protected void setBoxColor() throws DbException {
        setFillColor((Color) useCaseGO.find(DbBEUseCaseGo.fBackgroundColor));
        setLineColor((Color) useCaseGO.find(DbBEUseCaseGo.fLineColor));
    }

    protected void setLineStyle() throws DbException {
        Boolean dashStyle = useCaseSO.isControl() == true ? Boolean.TRUE : (Boolean) useCaseGO
                .find(DbBEUseCaseGo.fDashStyle);
        setLineStyle((Boolean) useCaseGO.find(DbBEUseCaseGo.fHighlight), dashStyle);
    }

    protected void initStyleElements() throws DbException {
        setTextColor();
        setBoxColor();
        setLineStyle();
    }

    protected void populateContents() throws DbException {

        ZoneBoxSelection savedSel = new ZoneBoxSelection(this);
        setLineStyle();
        populateZones();
        populateQualifiers();
        savedSel.restore();
        diagram.setComputePos(this);
    }

    private void populateQualifiers() throws DbException {
        if (qualifiersGo != null)
            qualifiersGo.delete(false);
        qualifiersGo = null;
        removeQualifiersListener();
        if (((Boolean) useCaseGO.find(DbBEStyle.fDisplayQualiferDbBEUseCase)).booleanValue()) {
            DbEnumeration dbEnum = useCaseSO.getComponents().elements(
                    DbBEUseCaseQualifier.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbBEUseCaseQualifier useCaseQualifier = (DbBEUseCaseQualifier) dbEnum.nextElement();
                if (qualifiers == null) {
                    qualifiers = new ArrayList();
                }
                qualifiers.add(useCaseQualifier.getQualifier());
            }
            dbEnum.close();
            if (qualifiers != null) {
                qualifiersGo = new BEQualifier(diagram, this, DbBEUseCaseGo.fQualifiersOffset,
                        qualifiers);
                diagram.setComputePos(qualifiersGo);
                addQualifiersListener();
            }
        }
    }

    private String calculateDuplicate(DbRelationN gosRelation, DbSMSGraphicalObject dboG)
            throws DbException {
        DbObject diag = dboG.getComposite();
        int index = 0;
        int count = 0;
        DbEnumeration dbEnum = gosRelation.elements();
        while (dbEnum.hasMoreElements()) {
            DbSMSGraphicalObject elem = (DbSMSGraphicalObject) dbEnum.nextElement();
            if (elem.getComposite() == diag && elem instanceof DbBEUseCaseGo)
                count++;
            if (elem == dboG)
                index = count;
        }
        dbEnum.close();
        if (count < 2)
            return null;
        String pattern = "({0}/{1})";
        return MessageFormat.format(pattern,
                new Object[] { new Integer(index), new Integer(count) });
    }

    public final String getToolTipText() {
        return boxName;
    }

    public void refreshAfterDbUpdate(DbUpdateEvent e) throws DbException {
        if (useCaseGO.getTransStatus() == Db.OBJ_REMOVED)
            return;
        if (e.dbo == composite
                && e.dbo.getTransStatus() != Db.OBJ_REMOVED
                && (e.metaField == DbBEUseCase.fNumericIdentifier
                        || e.metaField == DbBEUseCase.fAlphanumericIdentifier
                        || e.metaField == DbBEUseCase.fSourceAlphanumericIdentifier || e.metaField == DbBEUseCase.fSourceNumericIdentifier))
            populateContents();
        else if ((e.dbo == beDiagram && e.metaField == DbBEDiagram.fNotation)
                || (e.dbo == beDiagram.getProject() && e.metaField == DbSMSProject.fBeDefaultNotation)
                || e.dbo == notation) {
            if (e.dbo == notation && e.neighbor instanceof DbBEDiagram && e.neighbor != beDiagram)
                return;
            updateNotation();
            initShapeAndBehavior();
            setZones();
            initStyleElements();
            populateContents();
        } else if (e.dbo instanceof DbBESingleZoneDisplay) {
            setZones();
            initStyleElements();
            populateContents();
        } else if (e.dbo instanceof DbBEResource
                && ((e.neighbor instanceof DbBEUseCaseResource && e.neighbor.getTransStatus() != Db.OBJ_REMOVED) || (e.neighbor == null)))
            populateContents();
        else if (e.dbo instanceof DbBEQualifier
                && ((e.neighbor instanceof DbBEUseCaseQualifier && e.neighbor.getTransStatus() != Db.OBJ_REMOVED) || (e.neighbor == null)))
            populateQualifiers();

    }

    /*
     * Unique listener listening to any change in the semantical part of the database that affect
     * the data needed to draw an BEUseCaseBox.
     */
    private static class BoxRefreshTg implements DbRefreshListener, DbRefreshPassListener {

        private HashSet refreshedBoxs = null;

        BoxRefreshTg() {
        }

        public void beforeRefreshPass(Db db) throws DbException {
            refreshedBoxs = null;
        }

        public void afterRefreshPass(Db db) throws DbException {
            refreshedBoxs = null; // for gc
        }

        public void refreshAfterDbUpdate(DbUpdateEvent e) throws DbException {

            DbObject dbo = e.dbo;
            if (dbo instanceof DbBEUseCase && dbo.getTransStatus() == Db.OBJ_REMOVED)
                return;
            if (dbo instanceof DbBEUseCase) {
                refreshBox((DbBEUseCase) dbo);
            } else if (e.metaField == DbObject.fComponents) {
                if (dbo instanceof DbBEUseCase)
                    refreshBox((DbBEUseCase) dbo);
                if (dbo instanceof DbUDF && e.neighbor instanceof DbUDFValue
                        && ((DbUDFValue) e.neighbor).getDbObject() instanceof DbBEUseCase)
                    refreshBox((DbBEUseCase) ((DbUDFValue) e.neighbor).getDbObject());
            } else if (dbo instanceof DbUDFValue
                    && ((DbUDFValue) dbo).getDbObject() instanceof DbBEUseCase) {
                refreshBox((DbBEUseCase) ((DbUDFValue) dbo).getDbObject());
            }
        }

        /*
         * Rebuild the data of all the BEUseCaseBoxes
         */
        private void refreshBox(DbBEUseCase useCase) throws DbException {
            if (refreshedBoxs == null)
                refreshedBoxs = new HashSet();
            if (!refreshedBoxs.add(useCase)) // already done in this transaction
                return;
            useCase.getDb().beginReadTrans();
            DbRelationN useCaseGos = useCase.getClassifierGos();
            useCase.getDb().commitTrans();
            int nb = useCaseGos.size();
            for (int i = 0; i < nb; i++) {
                DbObject element = useCaseGos.elementAt(i);
                if (element instanceof DbBEContextGo)
                    continue;
                DbBEUseCaseGo useCaseGo = (DbBEUseCaseGo) element;
                BEUseCaseBox beUseCaseBox = (BEUseCaseBox) useCaseGo.getGraphicPeer();
                if (beUseCaseBox != null) {
                    beUseCaseBox.m_icon = beUseCaseBox.m_style.getDisplayStereotypeIcon();
                    beUseCaseBox.m_stereotype = useCase.getUmlStereotype();
                    beUseCaseBox.m_stereotypeIcon = (beUseCaseBox.m_stereotype == null) ? null
                            : beUseCaseBox.m_stereotype.getIcon();

                    beUseCaseBox.initShapeAndBehavior();
                    beUseCaseBox.setZones();
                    beUseCaseBox.initStyleElements();
                    beUseCaseBox.populateContents();
                }
            }
        }
    }

    private class BoxGoRefreshTg implements DbRefreshListener {
        BoxGoRefreshTg() {
        }

        public void refreshAfterDbUpdate(DbUpdateEvent e) throws DbException {
            if (DbSMSGraphicalObject.fStyle == e.metaField
                    || e.metaField == DbBEUseCaseGo.fBackgroundColor
                    || e.metaField == DbBEUseCaseGo.fDashStyle
                    || e.metaField == DbBEUseCaseGo.fHighlight
                    || e.metaField == DbBEUseCaseGo.fLineColor
                    || e.metaField == DbBEUseCaseGo.fTextColor) {
                initStyleElements();
                populateContents();
            }
        }
    }
}
