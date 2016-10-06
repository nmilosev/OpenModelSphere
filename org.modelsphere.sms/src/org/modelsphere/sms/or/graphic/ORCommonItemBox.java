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

package org.modelsphere.sms.or.graphic;

//Java
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
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
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.ZoneBox;
import org.modelsphere.jack.graphic.shape.RectangleShape;
import org.modelsphere.jack.graphic.zone.CellEditor;
import org.modelsphere.jack.graphic.zone.CellID;
import org.modelsphere.jack.graphic.zone.CellRenderer;
import org.modelsphere.jack.graphic.zone.CellRenderingOption;
import org.modelsphere.jack.graphic.zone.ColumnCellsOption;
import org.modelsphere.jack.graphic.zone.CompositeCellRenderer;
import org.modelsphere.jack.graphic.zone.ImageCellRenderer;
import org.modelsphere.jack.graphic.zone.MatrixZone;
import org.modelsphere.jack.graphic.zone.StringCellRenderer;
import org.modelsphere.jack.graphic.zone.ZoneCell;
import org.modelsphere.jack.graphic.zone.CompositeCellRenderer.CompositeCellRendererElement;
import org.modelsphere.jack.srtool.graphic.DbTextFieldCellEditor;
import org.modelsphere.jack.srtool.graphic.DomainCellEditor;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.db.DbSMSCommonItemGo;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.srtypes.SMSDisplayDescriptor;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORCommonItemStyle;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.international.LocaleMgr;

public class ORCommonItemBox extends GraphicNode implements ActionInformation {

    private static final String DESCRIPTOR_FONT = "m_descriptorFont"; //NOT LOCALIZABLE

    //    the commonItem prefix
    private static final int C_I_PREFIX_IDX = 0;
    //    the commonItem name
    private static final int C_I_NAME_IDX = 1;
    //    the commonItem type
    private static final int C_I_TYPE_IDX = 2;
    //    the commonItem lenght & decimal
    private static final int C_I_LENGHT_DECIMAL_IDX = 3;
    //    the commonItem null value possible
    private static final int C_I_NULL_VALUE_IDX = 4;
    //    the commonItem duplicate
    private static final int C_I_DUPLICATE_IDX = 5;

    private static final String C_I_NAME_ZONE_ID = "Box Name"; //NOT LOCALIZABLE, property key

    static {
        BoxRefreshTg commonItemRefreshTg = new BoxRefreshTg();
        Db.addDbRefreshPassListener(commonItemRefreshTg);
        MetaField.addDbRefreshListener(commonItemRefreshTg, null, new MetaField[] {
                DbObject.fComponents, DbSemanticalObject.fName, DbSemanticalObject.fAlias,
                DbSemanticalObject.fPhysicalName, DbORCommonItem.fCommonItemGos,
                DbORCommonItem.fType, DbORCommonItem.fLength, DbORCommonItem.fNbDecimal,
                DbORCommonItem.fNull });

    }

    //the db reference
    protected DbSMSCommonItemGo commonItemGO;
    protected DbORCommonItem commonItemSO;
    // the zones
    transient private MatrixZone commonItemZone = new MatrixZone(C_I_NAME_ZONE_ID,
            GraphicUtil.CENTER_ALIGNMENT);

    //listeners
    transient private BoxGoRefreshTg commonItemGoRefreshTg = new BoxGoRefreshTg();

    //rendering options
    transient private CellRenderingOption commonItemPrefixCRO;
    transient private CellRenderingOption commonItemNameCRO;
    transient private CellRenderingOption commonItemTypeCRO;
    transient private CellRenderingOption commonItemLenghtDecimalCRO;
    transient private CellRenderingOption commonItemNullValueCRO;
    transient private CellRenderingOption duplicateCRO = new CellRenderingOption(
            new StringCellRenderer(), new Font("SansSerif", Font.PLAIN, 8), //NOT LOCALIZABLE
            GraphicUtil.RIGHT_ALIGNMENT);

    transient private ColumnCellsOption commonItemNameCellsOption;

    public ORCommonItemBox(Diagram diag, DbSMSCommonItemGo newBoxGO) throws DbException {
        super(diag, RectangleShape.singleton);
        commonItemGO = newBoxGO;
        commonItemSO = (DbORCommonItem) newBoxGO.getSO();
        setAutoFit(commonItemGO.isAutoFit());
        setRectangle(commonItemGO.getRectangle());
        objectInit();
    }

    protected void objectInit() throws DbException {
        commonItemGO.setGraphicPeer(this);
        commonItemGO.addDbRefreshListener(commonItemGoRefreshTg);
        initCellRenderingOptions();
        setZones();
        initStyleElements();
        populateContents();

    }

    private void initCellRenderingOptions() throws DbException {
        Color color = (Color) commonItemGO.find(DbSMSCommonItemGo.fTextColor);
        commonItemNameCRO = getCellRenderingOptionForConcept(commonItemNameCRO,
                getStyleMetaField(DESCRIPTOR_FONT), null, color);
        commonItemNameCRO.setAlignment(GraphicUtil.CENTER_ALIGNMENT);
        duplicateCRO.setColor(color);

        commonItemTypeCRO = getCellRenderingOptionForConcept(commonItemTypeCRO,
                DbORCommonItemStyle.fOr_commonItemTypeFont, null, color);

        commonItemLenghtDecimalCRO = getCellRenderingOptionForConcept(commonItemLenghtDecimalCRO,
                DbORCommonItemStyle.fOr_commonItemLengthDecimalsFont, null, color);
        commonItemNullValueCRO = getCellRenderingOptionForConcept(commonItemNullValueCRO,
                DbORCommonItemStyle.fOr_commonItemNullValueFont, null, color);

        //prefix
        commonItemPrefixCRO = getCellRenderingOptionForConcept(commonItemPrefixCRO,
                getStyleMetaField(DESCRIPTOR_FONT), new CompositeCellRenderer(), color);
    }

    private MetaField getStyleMetaField(String genericName) throws DbException {
        MetaField mf = null;
        String nameMetaField;

        MetaClass soMetaClass = commonItemSO.getMetaClass();
        nameMetaField = soMetaClass.getJClass().getName();
        nameMetaField = nameMetaField.substring(nameMetaField.lastIndexOf('.') + 1);

        return commonItemGO.findStyle().getMetaField(genericName.concat(nameMetaField));
    }

    private CellRenderingOption getCellRenderingOptionForConcept(CellRenderingOption cellRO,
            MetaField metaField, CellRenderer renderer, Color color) throws DbException {
        Font font = (Font) commonItemGO.find(metaField);
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

    public MatrixZone getBoxZone() {
        return commonItemZone;
    }

    public Db getDb() {
        return commonItemGO.getDb();
    }

    public final DbObject getSemanticalObject() {
        return commonItemSO;
    }

    public final DbObject getGraphicalObject() {
        return commonItemGO;
    }

    public final boolean selectAtCellLevel() {
        return true;
    }

    // BEWARE: overriding triggers must call super.delete as last action
    public void delete(boolean all) {
        commonItemGO.setGraphicPeer(null);
        commonItemGO.removeDbRefreshListener(commonItemGoRefreshTg);
        commonItemZone.removeAllRows();

        super.delete(all);
    }

    private void setZones() throws DbException {
        setZonesVisibility();

        commonItemZone.addColumn(new ColumnCellsOption(commonItemPrefixCRO, null));
        commonItemZone.addColumn(new ColumnCellsOption(commonItemNameCRO, null));
        commonItemZone.addColumn(new ColumnCellsOption(commonItemTypeCRO, new TypeCellEditor()));
        commonItemZone.addColumn(new ColumnCellsOption(commonItemLenghtDecimalCRO, null));
        commonItemZone.addColumn(new ColumnCellsOption(commonItemNullValueCRO, null));
        commonItemZone.addColumn(new ColumnCellsOption(duplicateCRO, null));

        addZone(commonItemZone);
    }

    protected void setZonesVisibility() throws DbException {
        // Modify with zone
    }

    protected void setBoxColor() throws DbException {
        setFillColor((Color) commonItemGO.find(DbSMSCommonItemGo.fBackgroundColor));
        setLineColor((Color) commonItemGO.find(DbSMSCommonItemGo.fLineColor));
    }

    protected void setLineStyle() throws DbException {
        this.setLineStyle((Boolean) commonItemGO.find(DbSMSCommonItemGo.fHighlight),
                (Boolean) commonItemGO.find(DbSMSCommonItemGo.fDashStyle));
    }

    protected void initStyleElements() throws DbException {
        setZonesVisibility();
        setBoxColor();
        setLineStyle();
        initCellRenderingOptions();
    }

    protected void populateContents() throws DbException {
        ZoneBoxSelection savedSel = new ZoneBoxSelection(this);
        populateName();
        savedSel.restore();
        diagram.setComputePos(this);
    }

    //must be within a read transaction
    private void populateName() throws DbException {
        commonItemZone.clear();
        commonItemZone.addColumn(new ColumnCellsOption(commonItemPrefixCRO, null));
        commonItemZone.addColumn(new ColumnCellsOption(commonItemNameCRO, null));
        commonItemZone.addColumn(new ColumnCellsOption(commonItemTypeCRO, new TypeCellEditor()));
        commonItemZone.addColumn(new ColumnCellsOption(commonItemLenghtDecimalCRO, null));
        commonItemZone.addColumn(new ColumnCellsOption(commonItemNullValueCRO,
                new DomainCellEditor(DbORCommonItem.fNull)));
        commonItemZone.addColumn(new ColumnCellsOption(duplicateCRO, null));

        commonItemZone.addRow();
        commonItemZone.set(0, C_I_PREFIX_IDX, new ZoneCell(commonItemSO,
                getPrefixData(commonItemSO)));
        String name = commonItemSO.getSemanticalName(DbObject.SHORT_FORM);

        commonItemZone.set(0, C_I_NAME_IDX, getNameZoneCell(commonItemSO,
                DbORCommonItemStyle.fOr_nameDescriptor, null));

        DbORTypeClassifier type = null;
        if (((Boolean) commonItemGO.find(DbORCommonItemStyle.fOr_commonItemTypeDisplay))
                .booleanValue())
            type = commonItemSO.getType();
        if (type != null) {
            ZoneCell zc = getNameZoneCell(type, DbORCommonItemStyle.fOr_nameDescriptor,
                    commonItemTypeCRO);
            zc.setObject(commonItemSO); //change the object for the type editor
            zc.setCellEditor(new TypeCellEditor()); //change for the good editor
            commonItemZone.set(0, C_I_TYPE_IDX, zc);
        } else
            commonItemZone.set(0, C_I_TYPE_IDX, new ZoneCell(commonItemSO, null));
        String lengthNbDecimal = null;
        if (((Boolean) commonItemGO.find(DbORCommonItemStyle.fOr_commonItemLengthDecimalsDisplay))
                .booleanValue())
            lengthNbDecimal = getLengthNbDecimal();
        commonItemZone.set(0, C_I_LENGHT_DECIMAL_IDX, new ZoneCell(commonItemSO, lengthNbDecimal));
        String nullValuePossible = null;
        if (((Boolean) commonItemGO.find(DbORCommonItemStyle.fOr_commonItemNullValueDisplay))
                .booleanValue())
            nullValuePossible = (commonItemSO.getNull() ? LocaleMgr.screen.getString("Null")
                    : LocaleMgr.screen.getString("NotNull"));
        commonItemZone.set(0, C_I_NULL_VALUE_IDX, new ZoneCell(commonItemSO, nullValuePossible));
        commonItemZone.set(0, C_I_DUPLICATE_IDX, new ZoneCell(commonItemSO, calculateDuplicate(
                commonItemSO.getCommonItemGos(), commonItemGO)));
    }

    private final ZoneCell getNameZoneCell(DbObject source, MetaField mfDescriptor,
            CellRenderingOption CRO) throws DbException {
        String name;
        MetaField descriptorMF = null;

        boolean editable = true;
        switch (((SMSDisplayDescriptor) commonItemGO.find(mfDescriptor)).getValue()) {
        case SMSDisplayDescriptor.NAME:
            name = ((DbSemanticalObject) source).getSemanticalName(DbObject.SHORT_FORM);
            descriptorMF = DbSemanticalObject.fName;
            break;
        case SMSDisplayDescriptor.ALIAS:
            name = ((DbSemanticalObject) source).getAlias();
            descriptorMF = DbSemanticalObject.fAlias;
            break;
        case SMSDisplayDescriptor.PHYSICAL_NAME:
            name = ((DbSemanticalObject) source).getPhysicalName();
            descriptorMF = DbSemanticalObject.fPhysicalName;
            break;
        default:
            name = null;
            editable = false;
            break;
        }
        if (source instanceof DbORTypeClassifier) {
            name = (name == null ? ": " : ": " + name); //NOT LOCALIZABLE
        }

        CellEditor tfEditor = null;
        if (editable)
            tfEditor = new DbTextFieldCellEditor(descriptorMF, false);

        ZoneCell zc = null;

        if (CRO != null)
            zc = new ZoneCell(source, name, CRO, tfEditor);
        else {
            zc = new ZoneCell(source, name);
            zc.setCellEditor(tfEditor);
        }
        zc.setEditable(editable);

        return zc;
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

    private Vector getPrefixData(DbObject semObj) throws DbException {
        Debug
                .assert2(semObj != null,
                        "null argument not allowed. ORCommonItemBox.getPrefixData().");
        Vector compositeElements = new Vector();

        /****
         * MODIFY Add prefix or Icon
         */
        return compositeElements;
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

    public String getLengthNbDecimal() throws DbException {
        String lengthNbDecimal;

        Integer length = commonItemSO.getLength();
        if (length == null)
            return null;
        lengthNbDecimal = "(" + length.toString(); //NOT LOCALIZABLE
        Integer nbDecimal = commonItemSO.getNbDecimal();
        if (nbDecimal == null)
            lengthNbDecimal = lengthNbDecimal + ")"; //NOT LOCALIZABLE
        else
            lengthNbDecimal = lengthNbDecimal + ", " + nbDecimal.toString() + ")"; //NOT LOCALIZABLE

        return lengthNbDecimal;
    }

    public final String getToolTipText() {
        return (String) commonItemZone.get(0, C_I_NAME_IDX).getCellData();
    }

    /*
     * Unique listener listening to any change in the semantical part of the database that affect
     * the data needed to draw an ORCommonItemBox.
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
            if (e.metaField == DbObject.fComponents) {
                if (dbo instanceof DbORCommonItem)
                    refreshBox((DbORCommonItem) dbo);
            } else if (dbo instanceof DbORCommonItem) {
                refreshBox((DbORCommonItem) dbo);
            } else if (dbo instanceof DbORTypeClassifier
                    && (e.metaField == DbSemanticalObject.fName
                            || e.metaField == DbSemanticalObject.fAlias || e.metaField == DbSemanticalObject.fPhysicalName)) {
                refreshCommonItemTypedBy(((DbORTypeClassifier) dbo).getCommonItems());
            }
        }

        private void refreshCommonItemTypedBy(DbRelationN commonItems) throws DbException {
            int nb = commonItems.size();
            for (int i = 0; i < nb; i++) {
                refreshBox((DbORCommonItem) commonItems.elementAt(i));
            }
        }

        /*
         * Rebuild the data of all the ORCommonItemBoxes
         */
        private void refreshBox(DbORCommonItem commonItem) throws DbException {
            if (refreshedBoxs == null)
                refreshedBoxs = new HashSet();
            if (!refreshedBoxs.add(commonItem)) // already done in this transaction
                return;
            DbRelationN commonItemGos = commonItem.getCommonItemGos();
            int nb = commonItemGos.size();
            for (int i = 0; i < nb; i++) {
                DbSMSCommonItemGo commonItemGo = (DbSMSCommonItemGo) commonItemGos.elementAt(i);
                ORCommonItemBox ORCommonItemBox = (ORCommonItemBox) commonItemGo.getGraphicPeer();
                if (ORCommonItemBox != null)
                    ORCommonItemBox.populateContents();
            }
        }
    }

    private class BoxGoRefreshTg implements DbRefreshListener {
        BoxGoRefreshTg() {
        }

        public void refreshAfterDbUpdate(DbUpdateEvent e) throws DbException {
            if (DbSMSGraphicalObject.fStyle == e.metaField
                    || e.metaField == DbSMSCommonItemGo.fBackgroundColor
                    || e.metaField == DbSMSCommonItemGo.fDashStyle
                    || e.metaField == DbSMSCommonItemGo.fHighlight
                    || e.metaField == DbSMSCommonItemGo.fLineColor
                    || e.metaField == DbSMSCommonItemGo.fTextColor) {
                initStyleElements();
                populateContents();
            }
        }
    }

    private static class TypeCellEditor implements CellEditor {
        TypeCellEditor() {
        }

        public final JComponent getComponent(ZoneBox box, CellID cellID, ZoneCell value,
                DiagramView view, Rectangle cellRect) {
            Point pt = view.getLocationOnScreen();
            pt.x += cellRect.x + cellRect.width / 2;
            pt.y += cellRect.y + cellRect.height / 2;
            org.modelsphere.sms.or.actions.SetTypeAction.setType(view,
                    new DbObject[] { (DbObject) value.getObject() }, pt);
            return null; // no editor necessary, all is done.
        }

        public final void stopEditing(int endCode) {
        } // never called
    }

}
