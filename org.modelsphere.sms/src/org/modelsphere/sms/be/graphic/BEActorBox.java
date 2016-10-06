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
import java.awt.Image;
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
import org.modelsphere.jack.srtool.graphic.DbTextFieldCellEditor;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.StringUtil;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEActorGo;
import org.modelsphere.sms.be.db.DbBEActorQualifier;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEQualifier;
import org.modelsphere.sms.be.db.DbBESingleZoneDisplay;
import org.modelsphere.sms.be.db.DbBEStyle;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.srtypes.SMSNotationShape;
import org.modelsphere.sms.db.srtypes.SMSZoneOrientation;
import org.modelsphere.sms.graphic.SMSStereotypeEditor;

public class BEActorBox extends GraphicNode implements ActionInformation, DbRefreshListener {

    static {
        BoxRefreshTg actorRefreshTg = new BoxRefreshTg();
        Db.addDbRefreshPassListener(actorRefreshTg);
        MetaField.addDbRefreshListener(actorRefreshTg, null, new MetaField[] {
                DbObject.fComponents, DbSemanticalObject.fName, DbSemanticalObject.fAlias,
                DbSemanticalObject.fPhysicalName, DbBEActor.fClassifierGos, DbBEActor.fControl,
                DbUDFValue.fValue, DbBEActor.fIdentifier, DbBEActor.fDefinition,
                DbBEActor.fDescription, DbBEActor.fUmlStereotype, DbBEActor.fUmlConstraints });

    }

    // the db reference
    protected DbBEActorGo actorGO;
    protected DbBEActor actorSO;
    protected DbBENotation notation;
    private HashMap zonesMap = null;
    private DbBEDiagram beDiagram;
    private ArrayList zones = null;
    private BEQualifier qualifiersGo = null;
    private ArrayList qualifiers = null;
    private String boxName = null;

    // listeners
    transient private BoxGoRefreshTg actorGoRefreshTg = new BoxGoRefreshTg();

    public BEActorBox(Diagram diag, DbBEActorGo newBoxGO) throws DbException {
        super(diag, null);
        actorGO = newBoxGO;
        actorSO = (DbBEActor) newBoxGO.getSO();
        setAutoFit(actorGO.isAutoFit());
        setRectangle(actorGO.getRectangle());
        newBoxGO.getComposite();
        beDiagram = ((DbBEDiagram) newBoxGO.getComposite());
        objectInit();
    }

    //
    // public methods
    // 

    // overridden
    public int getDefaultFitMode() {
        return GraphicComponent.TOTAL_FIT;
    }

    public Db getDb() {
        return actorGO.getDb();
    }

    public final DbObject getSemanticalObject() {
        return actorSO;
    }

    public final DbObject getGraphicalObject() {
        return actorGO;
    }

    public final boolean selectAtCellLevel() {
        return true;
    }

    // BEWARE: overriding triggers must call super.delete as last action
    public void delete(boolean all) {
        actorGO.setGraphicPeer(null);
        removeRefreshListener();
        removeZonesListener();
        removeQualifiersListener();
        Enumeration enumeration = this.displayedZones();
        while (enumeration.hasMoreElements()) {
            Zone zone = (Zone) enumeration.nextElement();
            if (zone instanceof MatrixZone)
                ((MatrixZone) zone).removeAllRows();
        }
        super.delete(all);
    } // end delete()

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
        beDiagram.addDbRefreshListener(this, DbRefreshListener.CALL_FOR_EVERY_FIELD);
        updateNotation();
        beDiagram.getProject().addDbRefreshListener(this, DbRefreshListener.CALL_FOR_EVERY_FIELD);
        actorGO.addDbRefreshListener(actorGoRefreshTg);
    }

    private void removeRefreshListener() {
        beDiagram.removeDbRefreshListener(this);
        notation.removeDbRefreshListener(this);
        beDiagram.getProject().removeDbRefreshListener(this);
        actorGO.removeDbRefreshListener(actorGoRefreshTg);
    }

    protected void objectInit() throws DbException {
        actorGO.setGraphicPeer(this);
        setRefreshListener();
        initShapeAndBehavior();
        setZones();
        initStyleElements();
        populateContents();
    }

    protected void initShapeAndBehavior() throws DbException {
        setShape(SMSNotationShape.getShape(notation.getActorShape().getValue()));
        if (notation.getActorZoneOrientation().equals(
                SMSZoneOrientation.getInstance(SMSZoneOrientation.PERPENDICULAR)))
            this.setTwoColumns(33, 1, true);
        else
            this.setOneColumn();
    }

    private MetaField getStyleMetaField(String genericName) throws DbException {
        MetaField mf = null;
        String nameMetaField;

        MetaClass soMetaClass = actorSO.getMetaClass();
        nameMetaField = soMetaClass.getJClass().getName();
        nameMetaField = nameMetaField.substring(nameMetaField.lastIndexOf('.') + 1);

        return actorGO.findStyle().getMetaField(genericName.concat(nameMetaField));
    }

    private CellRenderingOption getCellRenderingOptionForConcept(DbBESingleZoneDisplay zoneDisplay,
            CellRenderer renderer, int alignment) throws DbException {
        Font font = null;
        MetaField mf = getFontStyleMetaField(zoneDisplay);
        if (mf != null)
            font = (Font) actorGO.find(mf);

        CellRenderingOption cellOption = new CellRenderingOption(renderer != null ? renderer
                : new StringCellRenderer(false, true), font, alignment);
        return cellOption;
    }

    private MetaField getFontStyleMetaField(DbBESingleZoneDisplay zoneDisplay) throws DbException {
        String suffix = "FontDbBEActor";
        MetaField metaField = null;
        DbBEStyle style = (DbBEStyle) actorGO.findStyle();
        if (zoneDisplay.getMetaField() != null) {
            MetaField mf = zoneDisplay.getMetaField();
            metaField = style.getMetaField(mf.getJName() + suffix);
            if (metaField == null) {
                metaField = DbBEStyle.fNameFontDbBEActor;
                if (Debug.isDebug())
                    System.out.println("*************** MetaField without FONT (" + mf.getJName()
                            + suffix + ")**************");
            }
        } else if (zoneDisplay.getUdf() != null)
            metaField = DbBEStyle.fUdfFontDbBEActor;
        else {
            metaField = DbBEStyle.fNameFontDbBEUseCase;
            if (Debug.isDebug())
                System.out.println("************* DbBESingleZoneDisplay without FONT ("
                        + (zoneDisplay.getUdf() != null ? "UDF)" : "OTHER)") + "*************");
        }
        return metaField;
    }

    private void setZones() throws DbException {
        clearAllZones();
        if (zonesMap != null)
            removeZonesListener();
        zonesMap = new HashMap();
        zones = notation.getActorZones();
        Iterator i = zones.iterator();
        int index = 0;
        while (i.hasNext()) {
            DbBESingleZoneDisplay zoneDisplay = (DbBESingleZoneDisplay) i.next();
            if (!zoneDisplay.isDisplayed())
                continue;
            int alignment = ZoneJustification.getGraphicUtilAligmentValue(zoneDisplay
                    .getJustification().getValue());
            MatrixZone zone = new MatrixZone(zoneDisplay.getGUIName(), alignment);
            zonesMap.put(zoneDisplay, zone);
            addZone(zone);
            CellEditor tfEditor = null;
            if (zoneDisplay.getMetaField() != null
                    && !(zoneDisplay.getMetaField() instanceof MetaRelation))
                tfEditor = new DbTextFieldCellEditor(zoneDisplay.getMetaField(), false);
            zone.addColumn(new ColumnCellsOption(this.getCellRenderingOptionForConcept(zoneDisplay,
                    null, alignment), tfEditor, false));
            zone.setWrappingCol(0);
            if (index == 0
                    && notation.getActorZoneOrientation().equals(
                            SMSZoneOrientation.getInstance(SMSZoneOrientation.PERPENDICULAR))) {
                this.setVerticalLine(zoneDisplay.isSeparator());
                zone.setHasBottomLine(false);
            } else
                zone.setHasBottomLine(zoneDisplay.isSeparator());
            index++;
        }
        addZonesListener();
    }

    private void removeZonesListener() {
        Iterator i = zones.iterator();
        while (i.hasNext()) {
            DbBESingleZoneDisplay zoneDisplay = (DbBESingleZoneDisplay) i.next();
            zoneDisplay.removeDbRefreshListener(this);
        }
    }

    private void addZonesListener() {
        Iterator i = zones.iterator();
        while (i.hasNext()) {
            DbBESingleZoneDisplay zoneDisplay = (DbBESingleZoneDisplay) i.next();
            zoneDisplay.addDbRefreshListener(this);
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

    private void populateZones() throws DbException {
        boxName = actorSO.getName();
        int zonePos = 0;
        ArrayList zones = notation.getActorZones();
        Iterator i = zones.iterator();
        while (i.hasNext()) {
            DbBESingleZoneDisplay zoneDisplay = (DbBESingleZoneDisplay) i.next();
            if (!zoneDisplay.isDisplayed())
                continue;

            boolean bSetStereotypeEditor = false;

            MatrixZone zone = (MatrixZone) zonesMap.get(zoneDisplay);
            if (zone != null) {
                zone.removeAllRows();
                int row = 0; // todo pour l'utilisation pour les enum...
                zone.addRow();
                Object value = zoneDisplay.getValue(actorSO);

                String strValue = null;
                Object obj = zoneDisplay.getSourceObject();
                if (obj instanceof MetaField) {
                    MetaField mf = (MetaField) obj;
                    if (mf.equals(DbBEActor.fIdentifier)) {
                        String pattern = notation.getActorIdPrefix();
                        strValue = (value != null ? value.toString() : "");
                        strValue = MessageFormat.format(pattern, new Object[] { strValue });
                        // strValue = notation.getActorIdPrefix();
                        // strValue = strValue + (value != null?
                        // value.toString(): "");
                    } else if (mf.equals(DbBEActor.fUmlStereotype)) {
                        DbSMSStereotype stereotype = actorSO.getUmlStereotype();
                        if (stereotype != null) {
                            strValue = "«" + stereotype.getName() + "»";
                            bSetStereotypeEditor = true;
                            Image image = stereotype.getIcon();
                        } // end if
                    } else {
                        strValue = (value != null ? value.toString() : "");
                    } // end if
                } // end if

                if (zonePos == 0) {
                    String duplicate = this.calculateDuplicate(actorSO.getClassifierGos(), actorGO);
                    if (strValue == null)
                        strValue = duplicate;
                    else if (duplicate != null)
                        strValue = strValue + duplicate;
                }
                boolean editable = false;
                if (zoneDisplay.getMetaField() != null && !StringUtil.isEmptyString(strValue))
                    editable = true;
                zone.set(row, 0, new ZoneCell(actorSO, strValue != null ? strValue : "", editable));

                if (bSetStereotypeEditor) {
                    MatrixCellID cellID = zone.getCellID(actorSO, 0);
                    if (cellID != null)
                        zone.setCellEditor(cellID,
                                new SMSStereotypeEditor(DbBEActor.fUmlStereotype));
                }

                row++;
                zonePos++;
            }
        }
    }

    protected void setTextColor() throws DbException {
        Color color = (Color) actorGO.find(DbBEActorGo.fTextColor);
        Enumeration enumeration = this.displayedZones();
        while (enumeration.hasMoreElements()) {
            Zone zone = (Zone) enumeration.nextElement();
            zone.setTextColor(color);
        }
    }

    protected void setBoxColor() throws DbException {
        setFillColor((Color) actorGO.find(DbBEActorGo.fBackgroundColor));
        setLineColor((Color) actorGO.find(DbBEActorGo.fLineColor));
    }

    protected void setLineStyle() throws DbException {
        Boolean dashStyle = actorSO.isControl() == true ? Boolean.TRUE : (Boolean) actorGO
                .find(DbBEActorGo.fDashStyle);
        setLineStyle((Boolean) actorGO.find(DbBEActorGo.fHighlight), dashStyle);
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
        if (((Boolean) actorGO.find(DbBEStyle.fDisplayQualiferDbBEActor)).booleanValue()) {
            DbEnumeration dbEnum = actorSO.getComponents().elements(DbBEActorQualifier.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbBEActorQualifier actorQualifier = (DbBEActorQualifier) dbEnum.nextElement();
                if (qualifiers == null) {
                    qualifiers = new ArrayList();
                }
                qualifiers.add(actorQualifier.getQualifier());
            }
            dbEnum.close();
            if (qualifiers != null) {
                qualifiersGo = new BEQualifier(diagram, this, DbBEActorGo.fQualifiersOffset,
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
            if (elem.getComposite() == diag)
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

    public void refreshAfterDbUpdate(DbUpdateEvent e) throws DbException {
        if (actorGO.getTransStatus() == Db.OBJ_REMOVED)
            return;
        if ((e.dbo == beDiagram && e.metaField == DbBEDiagram.fNotation)
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
        } else if (e.dbo instanceof DbBEQualifier
                && ((e.neighbor instanceof DbBEActorQualifier && e.neighbor.getTransStatus() != Db.OBJ_REMOVED) || (e.neighbor == null)))
            populateQualifiers();
    }

    public final String getToolTipText() {
        return boxName;
    }

    /*
     * Unique listener listening to any change in the semantical part of the database that affect
     * the data needed to draw an BEActorBox.
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
            if (dbo instanceof DbBEActor) {
                refreshBox((DbBEActor) dbo);
            } else if (e.metaField == DbObject.fComponents) {
                if (dbo instanceof DbUDF && e.neighbor instanceof DbUDFValue
                        && ((DbUDFValue) e.neighbor).getDbObject() instanceof DbBEActor)
                    refreshBox((DbBEActor) ((DbUDFValue) e.neighbor).getDbObject());
            } else if (dbo instanceof DbUDFValue
                    && ((DbUDFValue) dbo).getDbObject() instanceof DbBEActor) {
                refreshBox((DbBEActor) ((DbUDFValue) dbo).getDbObject());
            }
        }

        /*
         * Rebuild the data of all the BEActorBoxes
         */
        private void refreshBox(DbBEActor actor) throws DbException {
            if (refreshedBoxs == null)
                refreshedBoxs = new HashSet();
            if (!refreshedBoxs.add(actor)) // already done in this transaction
                return;
            DbRelationN actorGos = actor.getClassifierGos();
            int nb = actorGos.size();
            for (int i = 0; i < nb; i++) {
                DbBEActorGo actorGo = (DbBEActorGo) actorGos.elementAt(i);
                BEActorBox beActorBox = (BEActorBox) actorGo.getGraphicPeer();
                if (beActorBox != null)
                    beActorBox.populateContents();
            }
        }
    }

    private class BoxGoRefreshTg implements DbRefreshListener {
        BoxGoRefreshTg() {
        }

        public void refreshAfterDbUpdate(DbUpdateEvent e) throws DbException {
            if (DbSMSGraphicalObject.fStyle == e.metaField
                    || e.metaField == DbBEActorGo.fBackgroundColor
                    || e.metaField == DbBEActorGo.fDashStyle
                    || e.metaField == DbBEActorGo.fHighlight
                    || e.metaField == DbBEActorGo.fLineColor
                    || e.metaField == DbBEActorGo.fTextColor) {
                initStyleElements();
                populateContents();
            }
        }
    }
}
