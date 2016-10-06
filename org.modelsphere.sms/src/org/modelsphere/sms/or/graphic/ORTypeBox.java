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
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
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
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DbTextFieldCellEditor;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.db.DbSMSClassifier;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.srtypes.SMSDisplayDescriptor;
import org.modelsphere.sms.or.db.DbORAllowableValue;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORDomainStyle;
import org.modelsphere.sms.or.db.DbORField;
import org.modelsphere.sms.or.db.DbORTableGo;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.db.srtypes.ORDomainCategory;
import org.modelsphere.sms.or.db.util.AnyORObject;

public class ORTypeBox extends GraphicNode implements ActionInformation,
        org.modelsphere.sms.graphic.SMSClassifierBox {

    private static final String DESCRIPTOR_FONT = "m_descriptorFont"; //NOT LOCALIZABLE

    private static final String REFERENCE = "REF()"; //NOT LOCALIZABLE

    //    the type prefix
    private static final int TYPE_PREFIX_IDX = 0;
    //    the type name
    private static final int TYPE_NAME_IDX = 1;
    //    the type type!
    private static final int TYPE_TYPE_IDX = 2;
    //    the type Length & Decimal
    private static final int TYPE_LENGTH_DECIMAL_IDX = 3;
    //    the type duplicate
    private static final int TYPE_DUPLICATE_IDX = 4;
    //    the field name
    private static final int FIELD_NAME_IDX = 0;
    //    the field type
    private static final int FIELD_TYPE_IDX = 1;
    //    the field Length & Decimal
    private static final int FIELD_LENGTH_DECIMAL_IDX = 2;
    //    the field Default Value
    private static final int FIELD_DEFAULT_VALUE_IDX = 3;

    private static final String TYPE_NAME_ZONE_ID = "Type Name"; //NOT LOCALIZABLE, property key
    private static final String FIELD_ZONE_ID = "Field Zone"; //NOT LOCALIZABLE, property key

    static {
        BoxRefreshTg boxRefreshTg = new BoxRefreshTg();
        Db.addDbRefreshPassListener(boxRefreshTg);
        MetaField.addDbRefreshListener(boxRefreshTg, null, new MetaField[] { DbObject.fComponents,
                DbSemanticalObject.fName, DbSemanticalObject.fAlias,
                DbSemanticalObject.fPhysicalName, DbORDomain.fUser, DbORDomain.fLength,
                DbORDomain.fNbDecimal, DbORDomain.fSourceType, DbORField.fType, DbORField.fLength,
                DbORField.fInitialValue, DbORField.fNbDecimal, DbORDomain.fOrderedCollection,
                DbSMSClassifier.fClassifierGos });

    }

    //the db reference
    protected DbORTableGo typeGO;
    protected DbORTypeClassifier typeSO;
    // the zones
    transient private MatrixZone typeZone = new MatrixZone(TYPE_NAME_ZONE_ID,
            GraphicUtil.CENTER_ALIGNMENT);
    transient private MatrixZone fieldZone = new MatrixZone(FIELD_ZONE_ID);

    //listeners
    transient private TypeGoRefreshTg typeGoRefreshTg = new TypeGoRefreshTg();

    //rendering options
    transient private CellRenderingOption typePrefixCRO;
    transient private CellRenderingOption typeNameCRO;
    transient private CellRenderingOption typeTypeCRO;
    transient private CellRenderingOption typeLengthDecimalCRO;
    transient private CellRenderingOption duplicateCRO = new CellRenderingOption(
            new StringCellRenderer(), new Font("SansSerif", Font.PLAIN, 8), //NOT LOCALIZABLE
            GraphicUtil.RIGHT_ALIGNMENT);

    transient private ColumnCellsOption typeNameCellsOption;

    transient private CellRenderingOption fieldNameCRO;
    transient private CellRenderingOption fieldTypeCRO;
    transient private CellRenderingOption fieldLengthDecimalCRO;
    transient private CellRenderingOption fieldDefaultValueCRO;

    public ORTypeBox(Diagram diag, DbORTableGo newTypeGO) throws DbException {
        super(diag, RectangleShape.singleton);
        typeGO = newTypeGO;
        typeSO = (DbORTypeClassifier) typeGO.getClassifier();
        setAutoFit(typeGO.isAutoFit());
        setRectangle(typeGO.getRectangle());
        objectInit();
    }

    protected void objectInit() throws DbException {
        typeGO.setGraphicPeer(this);
        typeGO.addDbRefreshListener(typeGoRefreshTg);
        initCellRenderingOptions();
        setZones();
        initStyleElements();
        populateContents();
    }

    private void initCellRenderingOptions() throws DbException {
        Color color = (Color) typeGO.find(DbSMSClassifierGo.fTextColor);

        duplicateCRO.setColor(color);
        typeNameCRO = getCellRenderingOptionForConcept(typeNameCRO,
                getStyleMetaField(DESCRIPTOR_FONT), null, color);
        typeNameCRO.setAlignment(GraphicUtil.CENTER_ALIGNMENT);
        typeTypeCRO = getCellRenderingOptionForConcept(typeTypeCRO,
                getStyleMetaField(DESCRIPTOR_FONT), null, color);
        typeLengthDecimalCRO = getCellRenderingOptionForConcept(typeLengthDecimalCRO,
                DbORDomainStyle.fOr_domainLengthDecimalsFont, null, color);

        fieldNameCRO = getCellRenderingOptionForConcept(fieldNameCRO,
                DbORDomainStyle.fOr_fieldDescriptorFont, null, color);
        fieldTypeCRO = getCellRenderingOptionForConcept(fieldTypeCRO,
                DbORDomainStyle.fOr_fieldTypeFont, null, color);
        fieldLengthDecimalCRO = getCellRenderingOptionForConcept(fieldLengthDecimalCRO,
                DbORDomainStyle.fOr_fieldLengthDecimalsFont, null, color);
        fieldDefaultValueCRO = getCellRenderingOptionForConcept(fieldDefaultValueCRO,
                DbORDomainStyle.fOr_fieldDefaultValueFont, null, color);

        //prefix
        typePrefixCRO = getCellRenderingOptionForConcept(typePrefixCRO,
                getStyleMetaField(DESCRIPTOR_FONT), new CompositeCellRenderer(), color);
    }

    private MetaField getStyleMetaField(String genericName) throws DbException {
        MetaField mf = null;
        String nameMetaField;

        MetaClass soMetaClass = typeSO.getMetaClass();
        nameMetaField = soMetaClass.getJClass().getName();
        nameMetaField = nameMetaField.substring(nameMetaField.lastIndexOf('.') + 1);

        return typeGO.findStyle().getMetaField(genericName.concat(nameMetaField));
    }

    private CellRenderingOption getCellRenderingOptionForConcept(CellRenderingOption cellRO,
            MetaField metaField, CellRenderer renderer, Color color) throws DbException {
        Font font = (Font) typeGO.find(metaField);
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

    public MatrixZone getTypeZone() {
        return typeZone;
    }

    public MatrixZone getFieldZone() {
        return fieldZone;
    }

    public DbORTableGo getTypeGO() {
        return typeGO;
    }

    public Db getDb() {
        return getTypeGO().getDb();
    }

    public final DbObject getSemanticalObject() {
        return getTypeSO();
    }

    public final DbObject getGraphicalObject() {
        return getTypeGO();
    }

    public DbORTypeClassifier getTypeSO() {
        return typeSO;
    }

    public final boolean selectAtCellLevel() {
        return true;
    }

    // BEWARE: overriding triggers must call super.delete as last action
    public void delete(boolean all) {
        typeGO.setGraphicPeer(null);
        typeGO.removeDbRefreshListener(typeGoRefreshTg);
        typeZone.removeAllRows();

        super.delete(all);
    }

    private void setZones() throws DbException {
        setZonesVisibility();

        typeZone.addColumn(new ColumnCellsOption(typePrefixCRO, null));
        typeZone.addColumn(new ColumnCellsOption(typeNameCRO, null)); //editor set in populateTypeCells()
        FieldTypeCellEditor typeEditor = new FieldTypeCellEditor();
        typeZone.addColumn(new ColumnCellsOption(typeTypeCRO, typeEditor));
        typeZone.addColumn(new ColumnCellsOption(typeLengthDecimalCRO, null));
        typeZone.addColumn(new ColumnCellsOption(duplicateCRO, null));

        addZone(typeZone);
        addZone(fieldZone); //Zones set in populateFields()
    }

    protected void setZonesVisibility() throws DbException {
        fieldZone.setVisible(((Boolean) typeGO.find(DbORDomainStyle.fOr_fieldDisplay))
                .booleanValue());
    }

    protected void setBoxColor() throws DbException {
        setFillColor((Color) typeGO.find(DbSMSClassifierGo.fBackgroundColor));
        setLineColor((Color) typeGO.find(DbSMSClassifierGo.fLineColor));
    }

    protected void setLineStyle() throws DbException {
        this.setLineStyle((Boolean) typeGO.find(DbSMSClassifierGo.fHighlight), (Boolean) typeGO
                .find(DbSMSClassifierGo.fDashStyle));
    }

    protected void initStyleElements() throws DbException {
        setZonesVisibility();
        setBoxColor();
        setLineStyle();
        initCellRenderingOptions();
    }

    protected void populateContents() throws DbException {
        ZoneBoxSelection savedSel = new ZoneBoxSelection(this);
        populateTypeCells();
        populateFields();
        savedSel.restore();
        diagram.setComputePos(this);
    }

    //must be within a read transaction
    private final void populateTypeCells() throws DbException {

        typeZone.clear();
        typeZone.addColumn(new ColumnCellsOption(typePrefixCRO, null));
        typeZone.addColumn(new ColumnCellsOption(typeNameCRO, null));
        FieldTypeCellEditor typeEditor = new FieldTypeCellEditor();
        typeZone.addColumn(new ColumnCellsOption(typeTypeCRO, typeEditor));
        typeZone.addColumn(new ColumnCellsOption(typeLengthDecimalCRO, null));
        typeZone.addColumn(new ColumnCellsOption(duplicateCRO, null));

        typeZone.addRow();
        typeZone.set(0, TYPE_PREFIX_IDX, new ZoneCell(typeSO, getPrefixData(typeSO)));
        typeZone.set(0, TYPE_NAME_IDX, getNameZoneCell(typeSO, DbORDomainStyle.fOr_nameDescriptor,
                null, ((Boolean) typeGO.find(DbORDomainStyle.fOr_domainOwnerDisplay))
                        .booleanValue()));
        DbORTypeClassifier type = null;
        if (typeSO instanceof DbORDomain) {
            type = ((DbORDomain) typeSO).getSourceType();
            if (type != null) {
                ZoneCell zc = getNameZoneCell(type, DbORDomainStyle.fOr_nameDescriptor, typeTypeCRO);
                String value = (String) zc.getCellData();
                value = (value == null ? ": " : ": " + value);//NOT LOCALIZABLE
                zc.setCellData(value);
                zc.setObject(typeSO); //change the object for the type editor
                zc.setCellEditor(new FieldTypeCellEditor()); //change for the good editor
                typeZone.set(0, TYPE_TYPE_IDX, zc);
            } else
                typeZone.set(0, TYPE_TYPE_IDX, new ZoneCell(typeSO, null));
        }
        if (typeSO instanceof DbORDomain
                && ((Boolean) typeGO.find(DbORDomainStyle.fOr_domainLengthDecimalsDisplay))
                        .booleanValue()) {
            typeZone.set(0, TYPE_LENGTH_DECIMAL_IDX, new ZoneCell(typeSO,
                    getLengthNbDecimal((DbORDomain) typeSO)));
        } else
            typeZone.set(0, TYPE_LENGTH_DECIMAL_IDX, new ZoneCell(typeSO, null));

        typeZone.set(0, TYPE_DUPLICATE_IDX, new ZoneCell(typeSO, calculateDuplicate(typeSO
                .getClassifierGos(), typeGO)));
    }

    public String getLengthNbDecimal(DbORDomain domain) throws DbException {
        String lengthNbDecimal;

        Integer length = domain.getLength();
        if (length == null)
            return null;
        lengthNbDecimal = "(" + length.toString(); //NOT LOCALIZABLE
        Integer nbDecimal = domain.getNbDecimal();
        if (nbDecimal == null)
            lengthNbDecimal = lengthNbDecimal + ")"; //NOT LOCALIZABLE
        else
            lengthNbDecimal = lengthNbDecimal + ", " + nbDecimal.toString() + ")"; //NOT LOCALIZABLE

        return lengthNbDecimal;
    }

    private final ZoneCell getNameZoneCell(DbObject source, MetaField mfDescriptor,
            CellRenderingOption CRO) throws DbException {
        return getNameZoneCell(source, mfDescriptor, CRO, false);
    }

    private final ZoneCell getNameZoneCell(DbObject source, MetaField mfDescriptor,
            CellRenderingOption CRO, boolean withOwner) throws DbException {
        String name;
        MetaField descriptorMF = null;

        boolean editable = true;
        switch (((SMSDisplayDescriptor) typeGO.find(mfDescriptor)).getValue()) {
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
        if (name == null)
            name = ""; //NOT LOCALIZABLE
        if (withOwner && name != null) {
            MetaRelationship mfUser = AnyORObject.getUserField(source);
            if (mfUser != null) {
                DbORUser user = (DbORUser) source.get(mfUser);
                if (user != null) {
                    String userName = (String) user.get(descriptorMF);
                    if (userName == null)
                        userName = ""; //NOT LOCALIZABLE
                    name = userName + '.' + name; //NOT LOCALIZABLE
                }
            }
        }

        CellEditor tfEditor = null;
        if (editable) {
            boolean addOnEnter = false;
            if (!(source instanceof DbORDomain))
                addOnEnter = true;
            tfEditor = new DbTextFieldCellEditor(descriptorMF, addOnEnter);
        }

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

    private final String calculateDuplicate(DbRelationN gosRelation, DbSMSGraphicalObject dboG)
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

    private final Vector getPrefixData(DbSemanticalObject semObj) throws DbException {
        Debug.assert2(semObj != null, "null argument not allowed. ORTypeBox.getPrefixData().");
        Vector compositeElements = new Vector();

        // Domain Prefix
        if (semObj instanceof DbORDomain) {
            DbORDomain domain = (DbORDomain) semObj;
            if (domain.getCategory().getValue() == ORDomainCategory.COLLECTION) {
                if (domain.isOrderedCollection())
                    addToCompositeElements(compositeElements, (DbtPrefix) typeGO
                            .find(DbORDomainStyle.fOr_domainOrderedCollectionPrefix));
                else
                    addToCompositeElements(compositeElements, (DbtPrefix) typeGO
                            .find(DbORDomainStyle.fOr_domainNonOrderedCollectionPrefix));
            }
        }

        return compositeElements;
    }

    private final void addToCompositeElements(Vector compositeElements, DbtPrefix prefix) {
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

    private final void populateFields() throws DbException {
        fieldZone.clear();

        if (fieldZone.isVisible()) {
            fieldZone.addColumn(new ColumnCellsOption(fieldNameCRO, null));
            fieldZone.addColumn(new ColumnCellsOption(fieldTypeCRO, new FieldTypeCellEditor()));
            fieldZone.addColumn(new ColumnCellsOption(fieldLengthDecimalCRO, null));
            fieldZone.addColumn(new ColumnCellsOption(fieldDefaultValueCRO, null));

            DbEnumeration enu = typeSO.getComponents().elements(DbORField.metaClass);
            int rowCtr = 0;
            while (enu.hasMoreElements()) {
                DbORField field = (DbORField) enu.nextElement();
                addFieldRow(field, rowCtr);
                rowCtr++;
            } //end while
            enu.close();

            enu = typeSO.getComponents().elements(DbORAllowableValue.metaClass);
            while (enu.hasMoreElements()) {
                DbORAllowableValue value = (DbORAllowableValue) enu.nextElement();
                addValueRow(value, rowCtr);
                rowCtr++;
            } //end while
            enu.close();
        } //end if
    } //end populateFields()

    private void addFieldRow(DbORField field, int rowCtr) throws DbException {
        fieldZone.addRow();
        fieldZone.set(rowCtr, FIELD_NAME_IDX, getNameZoneCell(field,
                DbORDomainStyle.fOr_nameDescriptor, null));

        String defaultValue = null, lengthNbDecimal = null;
        // Field Type cell
        DbORTypeClassifier type = null;
        if (((Boolean) typeGO.find(DbORDomainStyle.fOr_fieldTypeDisplay)).booleanValue()) {
            type = field.getType();
        }
        if (type != null) {
            ZoneCell zc = getNameZoneCell(type, DbORDomainStyle.fOr_nameDescriptor, fieldTypeCRO);
            zc.setObject(field); //change the object for the type editor
            zc.setCellEditor(new FieldTypeCellEditor()); //change for the good editor
            fieldZone.set(rowCtr, FIELD_TYPE_IDX, zc);
        } else
            fieldZone.set(rowCtr, FIELD_TYPE_IDX, new ZoneCell(field, null));

        // Field Length decimal cell
        if (((Boolean) typeGO.find(DbORDomainStyle.fOr_fieldLengthDecimalsDisplay)).booleanValue()) {
            lengthNbDecimal = field.getLengthNbDecimal();
        }
        fieldZone.set(rowCtr, FIELD_LENGTH_DECIMAL_IDX, new ZoneCell(field, lengthNbDecimal));
        // Field Devault Value cell
        if (((Boolean) typeGO.find(DbORDomainStyle.fOr_fieldDefaultValueDisplay)).booleanValue()) {
            defaultValue = field.getInitialValue();
        }
        fieldZone.set(rowCtr, FIELD_DEFAULT_VALUE_IDX, new ZoneCell(field, defaultValue));
    }

    private void addValueRow(DbORAllowableValue value, int rowCtr) throws DbException {
        fieldZone.addRow();

        String name = value.getDefinition();
        String literal = value.getValue();
        ZoneCell nameCell = new ZoneCell(value, name);
        ZoneCell valueCell = new ZoneCell(value, literal);
        fieldZone.set(rowCtr, FIELD_NAME_IDX, nameCell);
        fieldZone.set(rowCtr, FIELD_TYPE_IDX, new ZoneCell(value, null));
        fieldZone.set(rowCtr, FIELD_LENGTH_DECIMAL_IDX, new ZoneCell(value, ""));
        fieldZone.set(rowCtr, FIELD_DEFAULT_VALUE_IDX, valueCell);
    }

    // Used by AddAbstractAction
    public final void editMember(DbSemanticalObject typeM) {
        MatrixZone zone = null;
        int col = 0;

        if (typeM instanceof DbORField) {
            zone = getFieldZone();
            col = FIELD_NAME_IDX;
        }
        if (zone != null)
            ((ApplicationDiagram) diagram).editCell(this, zone.getCellID(typeM, col));
    }

    public final String getToolTipText() {
        return (String) typeZone.get(0, TYPE_NAME_IDX).getCellData();
    }

    /*
     * Unique listener listening to any change in the semantical part of the database that affect
     * the data needed to draw an ORTypeBox.
     */
    private static class BoxRefreshTg implements DbRefreshListener, DbRefreshPassListener {

        private HashSet refreshedTypes = null;

        BoxRefreshTg() {
        }

        public void beforeRefreshPass(Db db) throws DbException {
            refreshedTypes = null;
        }

        public void afterRefreshPass(Db db) throws DbException {
            refreshedTypes = null; // for gc
        }

        public void refreshAfterDbUpdate(DbUpdateEvent e) throws DbException {

            DbObject dbo = e.dbo;
            if (e.metaField == DbObject.fComponents) {
                if (dbo instanceof DbORTypeClassifier)
                    refreshType((DbORTypeClassifier) dbo);
            } else if (dbo instanceof DbORTypeClassifier) {
                refreshType((DbORTypeClassifier) dbo);
                if (e.metaField == DbSemanticalObject.fName
                        || e.metaField == DbSemanticalObject.fAlias
                        || e.metaField == DbSemanticalObject.fPhysicalName) {
                    refreshFieldsTypedBy(((DbORTypeClassifier) dbo).getTypedAttributes());
                    refreshTypesTypedBy(((DbORTypeClassifier) dbo).getDomains());

                }
            } else if (dbo instanceof DbORField) {
                refreshType((DbORTypeClassifier) dbo.getComposite());
            } else if (dbo instanceof DbORUser) {
                refreshTypesTypedBy(((DbORUser) dbo).getDomains());
            }
        }

        private void refreshFieldsTypedBy(DbRelationN objects) throws DbException {
            int nb = objects.size();
            for (int i = 0; i < nb; i++) {
                DbObject object = objects.elementAt(i);
                DbORTypeClassifier type = (DbORTypeClassifier) object
                        .getCompositeOfType(DbORTypeClassifier.metaClass);
                if (type != null)
                    refreshType(type);
            }
        }

        private void refreshTypesTypedBy(DbRelationN objects) throws DbException {
            int nb = objects.size();
            for (int i = 0; i < nb; i++) {
                refreshType((DbORTypeClassifier) objects.elementAt(i));
            }
        }

        /*
         * Rebuild the data of all the ORTypeBoxes of an Type.
         */
        private void refreshType(DbORTypeClassifier type) throws DbException {
            if (refreshedTypes == null)
                refreshedTypes = new HashSet();
            if (!refreshedTypes.add(type)) // already done in this transaction
                return;
            DbRelationN typeGos = type.getClassifierGos();
            int nb = typeGos.size();
            for (int i = 0; i < nb; i++) {
                DbORTableGo typeGo = (DbORTableGo) typeGos.elementAt(i);
                ORTypeBox ORTypeBox = (ORTypeBox) typeGo.getGraphicPeer();
                if (ORTypeBox != null)
                    ORTypeBox.populateContents();
            }
        }
    }

    private class TypeGoRefreshTg implements DbRefreshListener {
        TypeGoRefreshTg() {
        }

        public void refreshAfterDbUpdate(DbUpdateEvent e) throws DbException {
            if (DbSMSGraphicalObject.fStyle == e.metaField
                    || e.metaField == DbSMSClassifierGo.fBackgroundColor
                    || e.metaField == DbSMSClassifierGo.fDashStyle
                    || e.metaField == DbSMSClassifierGo.fHighlight
                    || e.metaField == DbSMSClassifierGo.fLineColor
                    || e.metaField == DbSMSClassifierGo.fTextColor) {
                initStyleElements();
                populateContents();
            }
        }
    }

    private static class FieldTypeCellEditor implements CellEditor {
        FieldTypeCellEditor() {
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
