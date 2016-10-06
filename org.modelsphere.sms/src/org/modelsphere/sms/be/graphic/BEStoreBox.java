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
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEQualifier;
import org.modelsphere.sms.be.db.DbBESingleZoneDisplay;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEStoreGo;
import org.modelsphere.sms.be.db.DbBEStoreQualifier;
import org.modelsphere.sms.be.db.DbBEStyle;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.srtypes.SMSNotationShape;
import org.modelsphere.sms.db.srtypes.SMSZoneOrientation;
import org.modelsphere.sms.graphic.SMSStereotypeEditor;
import org.modelsphere.sms.oo.java.db.DbJVClass;

public class BEStoreBox extends GraphicNode implements ActionInformation, DbRefreshListener {

    static {
        BoxRefreshTg storeRefreshTg = new BoxRefreshTg();
        Db.addDbRefreshPassListener(storeRefreshTg);
        MetaField.addDbRefreshListener(storeRefreshTg, null, new MetaField[] {
                DbObject.fComponents, DbObject.fComposite, DbSemanticalObject.fName,
                DbSemanticalObject.fAlias, DbSemanticalObject.fPhysicalName,
                DbBEStore.fClassifierGos, DbBEStore.fControl, DbUDFValue.fValue,
                DbBEStore.fIdentifier, DbBEStore.fDescription, DbBEStore.fVolume,
                DbBEStore.fUmlStereotype, DbBEStore.fUmlConstraints });

    }

    // the db reference
    protected DbBEStoreGo storeGO;
    protected DbBEStore storeSO;
    protected DbBENotation notation;
    private HashMap zonesMap = null;
    private DbBEDiagram beDiagram;
    private ArrayList zones = null;
    private BEQualifier qualifiersGo = null;
    private ArrayList qualifiers = null;
    private String boxName = null;

    // listeners
    transient private BoxGoRefreshTg storeGoRefreshTg = new BoxGoRefreshTg();

    public BEStoreBox(Diagram diag, DbBEStoreGo newBoxGO) throws DbException {
        super(diag, null);
        storeGO = newBoxGO;
        storeSO = (DbBEStore) newBoxGO.getSO();
        setAutoFit(storeGO.isAutoFit());
        setRectangle(storeGO.getRectangle());
        newBoxGO.getComposite();
        beDiagram = ((DbBEDiagram) newBoxGO.getComposite());
        objectInit();
    }

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
        storeGO.addDbRefreshListener(storeGoRefreshTg);
    }

    private void removeRefreshListener() {
        beDiagram.removeDbRefreshListener(this);
        notation.removeDbRefreshListener(this);
        beDiagram.getProject().removeDbRefreshListener(this);
        storeGO.removeDbRefreshListener(storeGoRefreshTg);
    }

    // overridden
    public int getDefaultFitMode() {
        return GraphicComponent.HEIGHT_FIT;
    }

    protected void objectInit() throws DbException {
        storeGO.setGraphicPeer(this);
        setRefreshListener();
        initShapeAndBehavior();
        setZones();
        initStyleElements();
        populateContents();
    }

    protected void initShapeAndBehavior() throws DbException {
        setShape(SMSNotationShape.getShape(notation.getStoreShape().getValue()));
        if (notation.getStoreZoneOrientation().equals(
                SMSZoneOrientation.getInstance(SMSZoneOrientation.PERPENDICULAR)))
            this.setTwoColumns(33, 1, false);
        else
            this.setOneColumn();
    }

    private MetaField getStyleMetaField(String genericName) throws DbException {
        MetaField mf = null;
        String nameMetaField;

        MetaClass soMetaClass = storeSO.getMetaClass();
        nameMetaField = soMetaClass.getJClass().getName();
        nameMetaField = nameMetaField.substring(nameMetaField.lastIndexOf('.') + 1);

        return storeGO.findStyle().getMetaField(genericName.concat(nameMetaField));
    }

    private CellRenderingOption getCellRenderingOptionForConcept(DbBESingleZoneDisplay zoneDisplay,
            CellRenderer renderer, int alignment) throws DbException {
        Font font = null;
        MetaField mf = getFontStyleMetaField(zoneDisplay);
        if (mf != null)
            font = (Font) storeGO.find(mf);

        CellRenderingOption cellOption = new CellRenderingOption(renderer != null ? renderer
                : new StringCellRenderer(false, true), font, alignment);
        return cellOption;
    }

    private MetaField getFontStyleMetaField(DbBESingleZoneDisplay zoneDisplay) throws DbException {
        String suffix = "FontDbBEStore";
        MetaField metaField = null;
        DbBEStyle style = (DbBEStyle) storeGO.findStyle();
        if (zoneDisplay.getMetaField() != null) {
            MetaField mf = zoneDisplay.getMetaField();
            metaField = style.getMetaField(mf.getJName() + suffix);
            if (metaField == null) {
                metaField = DbBEStyle.fNameFontDbBEStore;
                if (Debug.isDebug())
                    System.out.println("*************** MetaField without FONT (" + mf.getJName()
                            + suffix + ")**************");
            }
        } else if (zoneDisplay.getUdf() != null)
            metaField = DbBEStyle.fUdfFontDbBEStore;
        else {
            metaField = DbBEStyle.fNameFontDbBEStore;
            if (Debug.isDebug())
                System.out.println("************* DbBESingleZoneDisplay without FONT ("
                        + (zoneDisplay.getUdf() != null ? "UDF)" : "OTHER)") + "*************");
        }
        return metaField;
    }

    public Db getDb() {
        return storeGO.getDb();
    }

    public final DbObject getSemanticalObject() {
        return storeSO;
    }

    public final DbObject getGraphicalObject() {
        return storeGO;
    }

    public final boolean selectAtCellLevel() {
        return true;
    }

    // BEWARE: overriding triggers must call super.delete as last action
    public void delete(boolean all) {
        storeGO.setGraphicPeer(null);
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
    }

    private void setZones() throws DbException {
        clearAllZones();
        if (zonesMap != null)
            removeZonesListener();
        zonesMap = new HashMap();
        zones = notation.getStoreZones();
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
                    && notation.getStoreZoneOrientation().equals(
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
        boxName = storeSO.getName();
        int zonePos = 0;
        ArrayList zones = notation.getStoreZones();
        Iterator i = zones.iterator();
        while (i.hasNext()) {
            DbBESingleZoneDisplay zoneDisplay = (DbBESingleZoneDisplay) i.next();
            if (!zoneDisplay.isDisplayed())
                continue;
            MatrixZone zone = (MatrixZone) zonesMap.get(zoneDisplay);
            if (zone != null) {
                zone.removeAllRows();
                int row = 0; // todo pour l'utilisation pour les enum...
                zone.addRow();
                Object value = zoneDisplay.getValue(storeSO);
                boolean bSetEditor = false;
                String strValue = null;
                Object obj = zoneDisplay.getSourceObject();
                if (obj instanceof MetaField) {
                    MetaField mf = (MetaField) obj;
                    if (mf.equals(DbBEStore.fIdentifier) || (mf.equals(DbBEStore.fName))) {
                        String pattern = notation.getStoreIdPrefix();
                        strValue = (value != null ? value.toString() : "");
                        DbJVClass claz = storeSO.getDbJVClass();
                        String clazName = (claz == null) ? "" : claz.getName();
                        if (pattern != null)
                            strValue = MessageFormat.format(pattern, new Object[] { strValue,
                                    clazName });
                    } else if (mf.equals(DbBEStore.fUmlStereotype)) {
                        DbSMSStereotype stereotype = storeSO.getUmlStereotype();
                        bSetEditor = true;
                        if (stereotype != null) {
                            strValue = "«" + stereotype.getName() + "»";
                        } // end if
                    } else {
                        strValue = (value != null ? value.toString() : "");
                    } // end if
                } // end if

                if (zonePos == 0) {
                    String duplicate = this.calculateDuplicate(storeSO.getClassifierGos(), storeGO);
                    if (strValue == null)
                        strValue = duplicate;
                    else if (duplicate != null)
                        strValue = strValue + " " + duplicate;
                }
                boolean editable = false;
                if (zoneDisplay.getMetaField() != null && !StringUtil.isEmptyString(strValue))
                    editable = true;

                if (StringUtil.isEmptyString(strValue))
                    zone.set(row, 0, new ZoneCell(storeSO, null, false));
                else
                    zone.set(row, 0, new ZoneCell(storeSO, strValue != null ? strValue : "",
                            editable));
                if (bSetEditor) {
                    MatrixCellID cellID = zone.getCellID(storeSO, 0);
                    if (cellID != null)
                        zone.setCellEditor(cellID,
                                new SMSStereotypeEditor(DbBEStore.fUmlStereotype));
                }
                row++;
                zonePos++;
            }
        }
    }

    protected void setTextColor() throws DbException {
        Color color = (Color) storeGO.find(DbBEStoreGo.fTextColor);
        Enumeration enumeration = this.displayedZones();
        while (enumeration.hasMoreElements()) {
            Zone zone = (Zone) enumeration.nextElement();
            zone.setTextColor(color);
        }
    }

    protected void setBoxColor() throws DbException {
        setFillColor((Color) storeGO.find(DbBEStoreGo.fBackgroundColor));
        setLineColor((Color) storeGO.find(DbBEStoreGo.fLineColor));
    }

    protected void setLineStyle() throws DbException {
        Boolean dashStyle = storeSO.isControl() == true ? Boolean.TRUE : (Boolean) storeGO
                .find(DbBEStoreGo.fDashStyle);
        setLineStyle((Boolean) storeGO.find(DbBEStoreGo.fHighlight), dashStyle);
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
        if (((Boolean) storeGO.find(DbBEStyle.fDisplayQualiferDbBEStore)).booleanValue()) {
            DbEnumeration dbEnum = storeSO.getComponents().elements(DbBEStoreQualifier.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbBEStoreQualifier storeQualifier = (DbBEStoreQualifier) dbEnum.nextElement();
                if (qualifiers == null) {
                    qualifiers = new ArrayList();
                }
                qualifiers.add(storeQualifier.getQualifier());
            }
            dbEnum.close();
            if (qualifiers != null) {
                qualifiersGo = new BEQualifier(diagram, this, DbBEStoreGo.fQualifiersOffset,
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
        if (storeGO.getTransStatus() == Db.OBJ_REMOVED)
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
                && ((e.neighbor instanceof DbBEStoreQualifier && e.neighbor.getTransStatus() != Db.OBJ_REMOVED) || (e.neighbor == null)))
            populateQualifiers();
    }

    public final String getToolTipText() {
        return boxName;
    }

    /*
     * Unique listener listening to any change in the semantical part of the database that affect
     * the data needed to draw an BEStoreBox.
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
            if (dbo instanceof DbBEStore) {
                refreshBox((DbBEStore) dbo);
            } else if (e.metaField == DbObject.fComponents) {
                if (dbo instanceof DbUDF && e.neighbor instanceof DbUDFValue
                        && ((DbUDFValue) e.neighbor).getDbObject() instanceof DbBEStore)
                    refreshBox((DbBEStore) ((DbUDFValue) e.neighbor).getDbObject());
            } else if (dbo instanceof DbUDFValue
                    && ((DbUDFValue) dbo).getDbObject() instanceof DbBEStore) {
                refreshBox((DbBEStore) ((DbUDFValue) dbo).getDbObject());
            }
        }

        /*
         * Rebuild the data of all the BEStoreBoxes
         */
        private void refreshBox(DbBEStore store) throws DbException {
            if (refreshedBoxs == null)
                refreshedBoxs = new HashSet();
            if (!refreshedBoxs.add(store)) // already done in this transaction
                return;
            DbRelationN storeGos = store.getClassifierGos();
            int nb = storeGos.size();
            for (int i = 0; i < nb; i++) {
                DbBEStoreGo storeGo = (DbBEStoreGo) storeGos.elementAt(i);
                BEStoreBox beStoreBox = (BEStoreBox) storeGo.getGraphicPeer();
                if (beStoreBox != null)
                    beStoreBox.populateContents();
            }
        }
    }

    private class BoxGoRefreshTg implements DbRefreshListener {
        BoxGoRefreshTg() {
        }

        public void refreshAfterDbUpdate(DbUpdateEvent e) throws DbException {
            if (DbSMSGraphicalObject.fStyle == e.metaField
                    || e.metaField == DbBEStoreGo.fBackgroundColor
                    || e.metaField == DbBEStoreGo.fDashStyle
                    || e.metaField == DbBEStoreGo.fHighlight
                    || e.metaField == DbBEStoreGo.fLineColor
                    || e.metaField == DbBEStoreGo.fTextColor) {
                initStyleElements();
                populateContents();
            }
        }
    }
}
