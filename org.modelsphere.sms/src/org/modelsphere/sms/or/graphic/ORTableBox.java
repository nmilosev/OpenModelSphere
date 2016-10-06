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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.ZoneBox;
import org.modelsphere.jack.graphic.shape.GraphicShape;
import org.modelsphere.jack.graphic.shape.RectangleShape;
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
import org.modelsphere.jack.util.StringUtil;
import org.modelsphere.sms.db.DbSMSClassifier;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSStyle;
import org.modelsphere.sms.db.DbSMSUmlConstraint;
import org.modelsphere.sms.db.srtypes.SMSDisplayDescriptor;
import org.modelsphere.sms.features.DisplayRecentModifications;
import org.modelsphere.sms.graphic.SMSStereotypeEditor;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORCheck;
import org.modelsphere.sms.or.db.DbORChoiceOrSpecialization;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORConstraint;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORFKeyColumn;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbORIndexKey;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORStyle;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTableGo;
import org.modelsphere.sms.or.db.DbORTrigger;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.db.DbORView;
import org.modelsphere.sms.or.db.srtypes.ORChoiceSpecializationCategory;
import org.modelsphere.sms.or.db.srtypes.ORDescriptorFormat;
import org.modelsphere.sms.or.db.util.AnyORObject;
import org.modelsphere.sms.or.informix.db.DbINFIndex;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.style.PrefixOptionComponent;

public class ORTableBox extends /* ExtZoneBox */GraphicNode implements ActionInformation,
        org.modelsphere.sms.graphic.SMSClassifierBox {
    //zone columns index , WARNING if the values change you must also change the
    // addColumn order in setZones
    //    the table prefix
    private static final int TABLE_PREFIX_IDX = 0;
    //    the table additional descriptor
    private static final int TABLE_ADD_DESCRIPTOR_IDX = 1;
    //    the table name
    private static final int TABLE_NAME_IDX = 2;
    //    the table duplicate
    private static final int TABLE_DUPLICATE_IDX = 3;

    //    the column prefix
    private static final int COLUMN_PREFIX_IDX = 0;
    //    the column additional descriptor
    private static final int COLUMN_ADD_DESCRIPTOR_IDX = 1;
    //    the column name
    private static final int COLUMN_NAME_IDX = 2;
    //    the column domain
    private static final int COLUMN_TYPE_IDX = 3;
    //    the column data type
    private static final int COLUMN_SOURCE_TYPE_IDX = 4;
    //    the column length  + nbr. decimal
    private static final int COLUMN_LENGTH_DECIMAL_IDX = 5;
    //    the column null value possible
    private static final int COLUMN_NULL_VALUE_IDX = 6;
    //    the column default value
    private static final int COLUMN_DEFAULT_VALUE_IDX = 7;
    //the column steretotype value
    private static final int COLUMN_STEREOTYPE_IDX = 8;

    //    the primary key prefix
    private static final int PK_PREFIX_IDX = 0;
    //    the primary key name
    private static final int PK_NAME_IDX = 1;
    //    the primary key columns
    private static final int PK_COLUMNS_IDX = 2;

    //    the unique key constraint prefix
    private static final int UK_PREFIX_IDX = 0;
    //    the unique key constraint name
    private static final int UK_NAME_IDX = 1;
    //    the unique key constraint columns
    private static final int UK_COLUMNS_IDX = 2;

    //    the foreign key constraint prefix
    private static final int FK_PREFIX_IDX = 0;
    //    the foreign key constraint name
    private static final int FK_NAME_IDX = 1;
    //    the foreign key constraint columns
    private static final int FK_COLUMNS_IDX = 2;

    //    the index prefix
    private static final int INDEX_PREFIX_IDX = 0;
    //    the index name
    private static final int INDEX_NAME_IDX = 1;
    //    the index columns
    private static final int INDEX_COLUMNS_IDX = 2;
    //    the index type
    private static final int INDEX_TYPE_IDX = 3;
    //    the index unique
    private static final int INDEX_UNIQUE_IDX = 4;

    //    the check constraint prefix
    private static final int CHECK_PREFIX_IDX = 0;
    //    the check constraint name
    private static final int CHECK_NAME_IDX = 1;
    //    the check constraint columns
    private static final int CHECK_COLUMNS_IDX = 2;

    //    the trigger prefix
    private static final int TRIGGER_PREFIX_IDX = 0;
    //    the trigger name
    private static final int TRIGGER_NAME_IDX = 1;
    //    the trigger columns
    private static final int TRIGGER_COLUMNS_IDX = 2;

    private static final String TABLE_STEREO_ZONE_ID = "Table Stereotype"; //NOT LOCALIZABLE, property key
    private static final String TABLE_NAME_ZONE_ID = "Table Name"; //NOT LOCALIZABLE, property key
    private static final String TABLE_COLUMN_ZONE_ID = "Table Columns"; //NOT LOCALIZABLE, property key
    private static final String PRIMARY_KEY_CONSTRAINT_ZONE_ID = "PK Constraint"; //NOT LOCALIZABLE, property key
    private static final String UNIQUE_KEY_CONSTRAINT_ZONE_ID = "UK Constraint"; //NOT LOCALIZABLE, property key
    private static final String FOREIGN_KEY_CONSTRAINT_ZONE_ID = "FK Constraint"; //NOT LOCALIZABLE, property key
    private static final String INDEX_ZONE_ID = "Index"; //NOT LOCALIZABLE, property key
    private static final String CHECK_CONSTRAINT_ZONE_ID = "Check Constraint"; //NOT LOCALIZABLE, property key
    private static final String TABLE_TRIGGER_ZONE_ID = "Table Triggers"; //NOT LOCALIZABLE, property key
    private static final String TABLE_CONSTRAINT_ZONE_ID = "Table UML Constraints";//NOT LOCALIZABLE

    static {
        BoxRefreshTg boxRefreshTg = new BoxRefreshTg();
        Db.addDbRefreshPassListener(boxRefreshTg);
        MetaField.addDbRefreshListener(boxRefreshTg, null, new MetaField[] { DbObject.fComponents,
                DbSemanticalObject.fName, DbSMSSemanticalObject.fUmlConstraints,
                DbSMSSemanticalObject.fUmlStereotype, DbSemanticalObject.fAlias,
                DbSemanticalObject.fPhysicalName, DbSMSSemanticalObject.fSuperCopy,
                DbSMSSemanticalObject.fSubCopies, DbSemanticalObject.fValidationStatus,
                DbSMSClassifier.fClassifierGos, DbORAbsTable.fUser, DbORColumn.fLength,
                DbORColumn.fNbDecimal, DbORColumn.fNull, DbORColumn.fInitialValue,
                DbORColumn.fType, DbORColumn.fIndexKeys, DbORColumn.fForeign,
                DbORColumn.fChoiceOrSpecialization, DbORPrimaryUnique.fColumns,
                DbORPrimaryUnique.fPrimary, DbORCheck.fColumn, DbORTrigger.fColumns,
                DbORIndexKey.fExpression, DbORIndex.fUnique, DbINFIndex.fCluster,
                DbSMSSemanticalObject.fSourceLinks, DbSMSSemanticalObject.fTargetLinks,
                DbORChoiceOrSpecialization.fCategory });
    }

    //the db reference
    protected DbORTableGo tableGO;
    protected DbORAbsTable tableSO;

    private DbORPrimaryUnique primaryKey;
    private ArrayList<DbORPrimaryUnique> uniqueKeys;
    private ArrayList<DbORForeign> foreignKeys;
    private ArrayList<DbORIndex> indexes;

    // the zones
    private static final int MINIMAL_HEIGHT = 16;
    transient private MatrixZone stereotypeZone = new MatrixZone(TABLE_STEREO_ZONE_ID,
            GraphicUtil.CENTER_ALIGNMENT);
    transient private MatrixZone tableZone = new MatrixZone(TABLE_NAME_ZONE_ID,
            GraphicUtil.CENTER_ALIGNMENT);
    transient private MatrixZone columnZone = new MatrixZone(TABLE_COLUMN_ZONE_ID,
            GraphicUtil.LEFT_ALIGNMENT, MINIMAL_HEIGHT);
    transient private MatrixZone pKConstraintZone = new MatrixZone(PRIMARY_KEY_CONSTRAINT_ZONE_ID);
    transient private MatrixZone uKConstraintZone = new MatrixZone(UNIQUE_KEY_CONSTRAINT_ZONE_ID);
    transient private MatrixZone fKConstraintZone = new MatrixZone(FOREIGN_KEY_CONSTRAINT_ZONE_ID);
    transient private MatrixZone indexZone = new MatrixZone(INDEX_ZONE_ID);
    transient private MatrixZone checkConstraintZone = new MatrixZone(CHECK_CONSTRAINT_ZONE_ID);
    transient private MatrixZone triggerZone = new MatrixZone(TABLE_TRIGGER_ZONE_ID);
    transient private MatrixZone constraintZone = new MatrixZone(TABLE_CONSTRAINT_ZONE_ID);

    //listeners
    transient private TableGoRefreshTg tableGoRefreshTg = new TableGoRefreshTg();

    //rendering options
    transient private CellRenderingOption stereotypeNameCRO;
    transient private CellRenderingOption stereotypeImageCRO;
    transient private CellRenderingOption tablePrefixCRO;
    transient private CellRenderingOption tableAddDescriptorCRO;
    transient private CellRenderingOption tableNameCRO;
    transient private CellRenderingOption duplicateCRO = new CellRenderingOption(
            new StringCellRenderer(), new Font("SansSerif", Font.PLAIN, 8), //NOT LOCALIZABLE
            GraphicUtil.RIGHT_ALIGNMENT);

    transient private CellRenderingOption columnPrefixCRO;
    transient private CellRenderingOption columnAddDescriptorCRO;
    transient private CellRenderingOption columnNameCRO;
    transient private CellRenderingOption columnTypeCRO;
    transient private CellRenderingOption columnSourceTypeCRO;
    transient private CellRenderingOption columnLengthDecimalCRO;
    transient private CellRenderingOption columnNullValueCRO;
    transient private CellRenderingOption columnDefaultValueCRO;
    transient private CellRenderingOption columnStereotypeNameCRO;

    transient private CellRenderingOption pKConstraintPrefixCRO;
    transient private CellRenderingOption pKConstraintNameCRO;
    transient private CellRenderingOption pKConstraintColumnsCRO;

    transient private CellRenderingOption uKConstraintPrefixCRO;
    transient private CellRenderingOption uKConstraintNameCRO;
    transient private CellRenderingOption uKConstraintColumnsCRO;

    transient private CellRenderingOption fKConstraintPrefixCRO;
    transient private CellRenderingOption fKConstraintNameCRO;
    transient private CellRenderingOption fKConstraintColumnsCRO;

    transient private CellRenderingOption indexPrefixCRO;
    transient private CellRenderingOption indexNameCRO;
    transient private CellRenderingOption indexColumnsCRO;
    transient private CellRenderingOption indexTypeCRO;
    transient private CellRenderingOption indexUniqueCRO;

    transient private CellRenderingOption checkConstraintPrefixCRO;
    transient private CellRenderingOption checkConstraintNameCRO;
    transient private CellRenderingOption checkConstraintColumnsCRO;

    transient private CellRenderingOption triggerPrefixCRO;
    transient private CellRenderingOption triggerNameCRO;
    transient private CellRenderingOption triggerColumnsCRO;

    transient private CellRenderingOption constraintNameCRO;

    public ORTableBox(Diagram diag, DbORTableGo newTableGO) throws DbException {
        super(diag, RectangleShape.singleton);
        tableGO = newTableGO;
        tableSO = (DbORAbsTable) tableGO.getClassifier();
        setAutoFit(tableGO.isAutoFit());
        setRectangle(tableGO.getRectangle());
        objectInit();
    }

    protected void objectInit() throws DbException {
        tableGO.setGraphicPeer(this);
        tableGO.addDbRefreshListener(tableGoRefreshTg);
        initCellRenderingOptions();
        setZones();
        initStyleElements();
        populateContents();
    }

    private void initCellRenderingOptions() throws DbException {
        Color color = (Color) tableGO.find(DbSMSClassifierGo.fTextColor);
        duplicateCRO.setColor(color);

        MetaField boxAddDescriptorFont = (tableSO instanceof DbORTable ? DbORStyle.fOr_tableAdditionnalDescriptorFont
                : DbORStyle.fOr_viewAdditionnalDescriptorFont);
        MetaField boxDescriptorFont = (tableSO instanceof DbORTable ? DbORStyle.fOr_tableDescriptorFont
                : DbORStyle.fOr_viewDescriptorFont);

        stereotypeNameCRO = getCellRenderingOptionForConcept(stereotypeNameCRO, boxDescriptorFont,
                null, color);
        stereotypeNameCRO.setAlignment(GraphicUtil.CENTER_ALIGNMENT);

        stereotypeImageCRO = getCellRenderingOptionForConcept(stereotypeImageCRO,
                DbORStyle.fOr_tablePrefixFont, new CompositeCellRenderer(), color);
        stereotypeImageCRO.setAlignment(GraphicUtil.RIGHT_ALIGNMENT);

        tableAddDescriptorCRO = getCellRenderingOptionForConcept(tableAddDescriptorCRO,
                boxAddDescriptorFont, null, color);
        tableAddDescriptorCRO.setAlignment(GraphicUtil.RIGHT_ALIGNMENT);
        tableNameCRO = getCellRenderingOptionForConcept(tableNameCRO, boxDescriptorFont, null,
                color);
        tableNameCRO.setAlignment(GraphicUtil.LEFT_ALIGNMENT);

        columnAddDescriptorCRO = getCellRenderingOptionForConcept(columnAddDescriptorCRO,
                DbORStyle.fOr_columnAdditionnalDescriptorFont, null, color);
        columnNameCRO = getCellRenderingOptionForConcept(columnNameCRO,
                DbORStyle.fOr_columnDescriptorFont, null, color);
        columnTypeCRO = getCellRenderingOptionForConcept(columnTypeCRO,
                DbORStyle.fOr_columnTypeFont, null, color);
        columnSourceTypeCRO = getCellRenderingOptionForConcept(columnSourceTypeCRO,
                DbORStyle.fOr_columnDomainSourceTypeFont, null, color);
        columnLengthDecimalCRO = getCellRenderingOptionForConcept(columnLengthDecimalCRO,
                DbORStyle.fOr_columnLengthDecimalsFont, null, color);
        columnNullValueCRO = getCellRenderingOptionForConcept(columnNullValueCRO,
                DbORStyle.fOr_columnNullValueFont, null, color);
        columnDefaultValueCRO = getCellRenderingOptionForConcept(columnDefaultValueCRO,
                DbORStyle.fOr_columnDefaultValueFont, null, color);
        columnStereotypeNameCRO = getCellRenderingOptionForConcept(columnStereotypeNameCRO,
                boxDescriptorFont, null, color);

        pKConstraintNameCRO = getCellRenderingOptionForConcept(pKConstraintNameCRO,
                DbORStyle.fOr_pkDescriptorFont, null, color);
        pKConstraintColumnsCRO = getCellRenderingOptionForConcept(pKConstraintColumnsCRO,
                DbORStyle.fOr_pkColumnsFont, null, color);

        uKConstraintNameCRO = getCellRenderingOptionForConcept(uKConstraintNameCRO,
                DbORStyle.fOr_ukDescriptorFont, null, color);
        uKConstraintColumnsCRO = getCellRenderingOptionForConcept(uKConstraintColumnsCRO,
                DbORStyle.fOr_ukColumnsFont, null, color);

        fKConstraintNameCRO = getCellRenderingOptionForConcept(fKConstraintNameCRO,
                DbORStyle.fOr_fkDescriptorFont, null, color);
        fKConstraintColumnsCRO = getCellRenderingOptionForConcept(fKConstraintColumnsCRO,
                DbORStyle.fOr_fkColumnsFont, null, color);

        indexNameCRO = getCellRenderingOptionForConcept(indexNameCRO,
                DbORStyle.fOr_indexDescriptorFont, null, color);
        indexColumnsCRO = getCellRenderingOptionForConcept(indexColumnsCRO,
                DbORStyle.fOr_indexColumnsFont, null, color);
        indexTypeCRO = getCellRenderingOptionForConcept(indexTypeCRO, DbORStyle.fOr_indexTypeFont,
                null, color);
        indexUniqueCRO = getCellRenderingOptionForConcept(indexUniqueCRO,
                DbORStyle.fOr_indexUniqueFont, null, color);

        checkConstraintNameCRO = getCellRenderingOptionForConcept(checkConstraintNameCRO,
                DbORStyle.fOr_checkConstraintFont, null, color);
        checkConstraintColumnsCRO = getCellRenderingOptionForConcept(checkConstraintColumnsCRO,
                DbORStyle.fOr_checkParametersFont, null, color);

        triggerNameCRO = getCellRenderingOptionForConcept(triggerNameCRO,
                DbORStyle.fOr_triggerDescriptorFont, null, color);
        triggerColumnsCRO = getCellRenderingOptionForConcept(triggerColumnsCRO,
                DbORStyle.fOr_triggerColumnsFont, null, color);

        constraintNameCRO = getCellRenderingOptionForConcept(stereotypeNameCRO, boxDescriptorFont,
                null, color);
        constraintNameCRO.setAlignment(GraphicUtil.CENTER_ALIGNMENT);

        //prefix
        tablePrefixCRO = getCellRenderingOptionForConcept(tablePrefixCRO,
                DbORStyle.fOr_tablePrefixFont, new CompositeCellRenderer(), color);
        columnPrefixCRO = getCellRenderingOptionForConcept(columnPrefixCRO,
                DbORStyle.fOr_columnPrefixFont, new CompositeCellRenderer(), color);
        pKConstraintPrefixCRO = getCellRenderingOptionForConcept(pKConstraintPrefixCRO,
                DbORStyle.fOr_pkPrefixFont, new CompositeCellRenderer(), color);
        uKConstraintPrefixCRO = getCellRenderingOptionForConcept(uKConstraintPrefixCRO,
                DbORStyle.fOr_ukPrefixFont, new CompositeCellRenderer(), color);
        fKConstraintPrefixCRO = getCellRenderingOptionForConcept(fKConstraintPrefixCRO,
                DbORStyle.fOr_fkPrefixFont, new CompositeCellRenderer(), color);
        indexPrefixCRO = getCellRenderingOptionForConcept(indexPrefixCRO,
                DbORStyle.fOr_indexPrefixFont, new CompositeCellRenderer(), color);
        checkConstraintPrefixCRO = getCellRenderingOptionForConcept(checkConstraintPrefixCRO,
                DbORStyle.fOr_checkConstraintPrefixFont, new CompositeCellRenderer(), color);
        triggerPrefixCRO = getCellRenderingOptionForConcept(triggerPrefixCRO,
                DbORStyle.fOr_triggerPrefixFont, new CompositeCellRenderer(), color);
    }

    private CellRenderingOption getCellRenderingOptionForConcept(CellRenderingOption cellRO,
            MetaField metaField, CellRenderer renderer, Color color) throws DbException {
        Font font = (Font) tableGO.find(metaField);
        if (cellRO == null) {
            cellRO = new CellRenderingOption(
                    renderer != null ? renderer : new StringCellRenderer(), font,
                    GraphicUtil.LEFT_ALIGNMENT);
            cellRO.setColor(color);
        } else {
            cellRO.setFont(font);
            cellRO.setColor(color);
        }

        return cellRO;
    }

    public MatrixZone getStereotypeZone() {
        return stereotypeZone;
    }

    public MatrixZone getTableZone() {
        return tableZone;
    }

    public MatrixZone getColumnZone() {
        return columnZone;
    }

    public MatrixZone getPrimaryKeyZone() {
        return pKConstraintZone;
    }

    public MatrixZone getUniqueKeyZone() {
        return uKConstraintZone;
    }

    public MatrixZone getForeignKeyZone() {
        return fKConstraintZone;
    }

    public MatrixZone getIndexZone() {
        return indexZone;
    }

    public MatrixZone getCheckConstraintZone() {
        return checkConstraintZone;
    }

    public MatrixZone getTriggerZone() {
        return triggerZone;
    }

    public DbORTableGo getTableGO() {
        return tableGO;
    }

    public Db getDb() {
        return getTableGO().getDb();
    }

    public final DbObject getSemanticalObject() {
        return getTableSO();
    }

    public final DbObject getGraphicalObject() {
        return getTableGO();
    }

    public DbORAbsTable getTableSO() {
        return tableSO;
    }

    public final boolean selectAtCellLevel() {
        return true;
    }

    // BEWARE: overriding triggers must call super.delete as last action
    public void delete(boolean all) {
        tableGO.setGraphicPeer(null);
        tableGO.removeDbRefreshListener(tableGoRefreshTg);
        stereotypeZone.removeAllRows();
        tableZone.removeAllRows();
        columnZone.removeAllRows();
        pKConstraintZone.removeAllRows();
        uKConstraintZone.removeAllRows();
        fKConstraintZone.removeAllRows();
        indexZone.removeAllRows();
        checkConstraintZone.removeAllRows();
        triggerZone.removeAllRows();
        constraintZone.removeAllRows();

        super.delete(all);
    }

    private void setZones() throws DbException {
        setZonesVisibility();
        DbORStyle style = getStyleFrom(tableGO);

        //stereotype zone
        Boolean b = (style == null) ? Boolean.TRUE : style.getOr_umlStereotypeDisplayed();
        if ((b != null) && b.booleanValue()) {
            stereotypeZone.addColumn(new ColumnCellsOption(stereotypeNameCRO, (CellEditor) null));
        }
        b = (style == null) ? Boolean.TRUE : style.getOr_umlStereotypeIconDisplayed();
        if ((b != null) && b.booleanValue()) {
            stereotypeZone.addColumn(new ColumnCellsOption(stereotypeImageCRO, (CellEditor) null));
        }

        tableZone.addColumn(new ColumnCellsOption(tablePrefixCRO, null));
        tableZone.addColumn(new ColumnCellsOption(tableAddDescriptorCRO, null)); //editor set in populateName()
        tableZone.addColumn(new ColumnCellsOption(tableNameCRO, null)); //editor set in populateName()
        tableZone.addColumn(new ColumnCellsOption(duplicateCRO, null));

        columnZone.addColumn(new ColumnCellsOption(columnPrefixCRO, null));
        columnZone.addColumn(new ColumnCellsOption(columnAddDescriptorCRO, null)); //editor set in populateColumns()
        columnZone.addColumn(new ColumnCellsOption(columnNameCRO, null)); //editor set in populateColumns()
        columnZone.addColumn(new ColumnCellsOption(columnTypeCRO, null));
        columnZone
                .addColumn(new ColumnCellsOption(columnSourceTypeCRO, new MemberTypeCellEditor()));
        columnZone.addColumn(new ColumnCellsOption(columnLengthDecimalCRO, null));
        columnZone.addColumn(new ColumnCellsOption(columnNullValueCRO, new DomainCellEditor(
                DbORColumn.fNull)));
        columnZone.addColumn(new ColumnCellsOption(columnDefaultValueCRO, null));
        columnZone.addColumn(new ColumnCellsOption(columnStereotypeNameCRO, null));

        //CellEditor adtVisibilityEditor = new DomainCellEditor(DbJVClass.fVisibility);

        pKConstraintZone.addColumn(new ColumnCellsOption(pKConstraintPrefixCRO, null));
        pKConstraintZone.addColumn(new ColumnCellsOption(pKConstraintNameCRO, null)); //editor set in populatePKConstraint()
        pKConstraintZone.addColumn(new ColumnCellsOption(pKConstraintColumnsCRO, null));

        uKConstraintZone.addColumn(new ColumnCellsOption(uKConstraintPrefixCRO, null));
        uKConstraintZone.addColumn(new ColumnCellsOption(uKConstraintNameCRO, null)); //editor set in populatePKConstraint()
        uKConstraintZone.addColumn(new ColumnCellsOption(uKConstraintColumnsCRO, null));

        fKConstraintZone.addColumn(new ColumnCellsOption(fKConstraintPrefixCRO, null));
        fKConstraintZone.addColumn(new ColumnCellsOption(fKConstraintNameCRO, null)); //editor set in populatePKConstraint()
        fKConstraintZone.addColumn(new ColumnCellsOption(fKConstraintColumnsCRO, null));

        indexZone.addColumn(new ColumnCellsOption(indexPrefixCRO, null));
        indexZone.addColumn(new ColumnCellsOption(indexNameCRO, null)); //editor set in populatePKConstraint()
        indexZone.addColumn(new ColumnCellsOption(indexColumnsCRO, null));
        indexZone.addColumn(new ColumnCellsOption(indexTypeCRO, null));
        indexZone.addColumn(new ColumnCellsOption(indexUniqueCRO, null));

        checkConstraintZone.addColumn(new ColumnCellsOption(checkConstraintPrefixCRO, null));
        checkConstraintZone.addColumn(new ColumnCellsOption(checkConstraintNameCRO, null)); //editor set in populateCheckConstraint()
        checkConstraintZone.addColumn(new ColumnCellsOption(checkConstraintColumnsCRO, null));

        triggerZone.addColumn(new ColumnCellsOption(triggerPrefixCRO, null));
        triggerZone.addColumn(new ColumnCellsOption(triggerNameCRO, null)); //editor set in populateTrigger()
        triggerZone.addColumn(new ColumnCellsOption(triggerColumnsCRO, null));

        constraintZone.addColumn(new ColumnCellsOption(constraintNameCRO, null));

        stereotypeZone.setHasBottomLine(false);
        addZone(stereotypeZone);
        addZone(tableZone);
        addZone(columnZone);
        addZone(pKConstraintZone);
        addZone(uKConstraintZone);
        addZone(fKConstraintZone);
        addZone(indexZone);
        addZone(checkConstraintZone);
        addZone(triggerZone);
        addZone(constraintZone); //Contraint zone is usually the last compartment in UML [MS]
    }

    private DbORStyle getStyleFrom(DbORTableGo tableGo) throws DbException {
        DbORStyle style = (DbORStyle) tableGo.getStyle();
        if (style == null) {
            DbORDiagram diag = (DbORDiagram) tableGo.getCompositeOfType(DbORDiagram.metaClass);
            style = (DbORStyle) diag.getStyle();
        }

        return style;
    } //end getStyleFrom()

    protected void setZonesVisibility() throws DbException {

        Boolean displayColsElem = (Boolean) tableGO.find(DbORStyle.fOr_columnDisplay);
        Boolean displayPKsElem = (Boolean) tableGO.find(DbORStyle.fOr_pkDisplay);
        Boolean displayUKsElem = (Boolean) tableGO.find(DbORStyle.fOr_ukDisplay);
        Boolean displayFKsElem = (Boolean) tableGO.find(DbORStyle.fOr_fkDisplay);
        Boolean displayIdxElem = (Boolean) tableGO.find(DbORStyle.fOr_indexDisplay);
        Boolean displayChksElem = (Boolean) tableGO.find(DbORStyle.fOr_checkDisplay);
        Boolean displayTrgsElem = (Boolean) tableGO.find(DbORStyle.fOr_triggerDisplay);
        Boolean displayConsElem = (Boolean) tableGO.find(DbORStyle.fOr_umlConstraintDisplayed);

        boolean displayCols = (displayColsElem == null) ? true : displayColsElem.booleanValue();
        boolean displayPKs = (displayPKsElem == null) ? true : displayPKsElem.booleanValue();
        boolean displayUKs = (displayUKsElem == null) ? true : displayUKsElem.booleanValue();
        boolean displayFKs = (displayFKsElem == null) ? true : displayFKsElem.booleanValue();
        boolean displayIdx = (displayIdxElem == null) ? true : displayIdxElem.booleanValue();
        boolean displayChks = (displayChksElem == null) ? true : displayChksElem.booleanValue();
        boolean displayTrgs = (displayTrgsElem == null) ? true : displayTrgsElem.booleanValue();
        boolean displayCons = (displayConsElem == null) ? true : displayConsElem.booleanValue();

        columnZone.setVisible(displayCols);
        pKConstraintZone.setVisible(displayPKs);
        uKConstraintZone.setVisible(displayUKs);
        fKConstraintZone.setVisible(displayFKs);
        indexZone.setVisible(displayIdx);
        checkConstraintZone.setVisible(displayChks);
        triggerZone.setVisible(displayTrgs);
        constraintZone.setVisible(displayCons);
    }

    protected void setBoxColor() throws DbException {

        setFillColor((Color) tableGO.find(DbSMSClassifierGo.fBackgroundColor));
        setLineColor((Color) tableGO.find(DbSMSClassifierGo.fLineColor));
    }

    protected void setLineStyle() throws DbException {
        this.setLineStyle((Boolean) tableGO.find(DbSMSClassifierGo.fHighlight), (Boolean) tableGO
                .find(DbSMSClassifierGo.fDashStyle));
    }

    protected void initStyleElements() throws DbException {
        setZonesVisibility();
        setBoxColor();
        setLineStyle();
        initCellRenderingOptions();
    }

    protected void populateContents() throws DbException {
        populateBox();
        ZoneBoxSelection savedSel = new ZoneBoxSelection(this);
        initKeys();
        populateStereotype();
        populateName();
        populateColumns();
        populatePKConstaints();
        populateUKConstaints();
        populateFKConstaints();
        populateIndex();
        populateCheckConstaints();
        populateTriggers();
        populateConstraints();
        savedSel.restore();
        diagram.setComputePos(this);
    }

    private void populateBox() throws DbException {
    	boolean showDependentTable = ORTable.doShowDependentTable(tableGO); 
    	
        if (showDependentTable) {
            if (tableSO instanceof DbORTable) {
                DbORTable table = (DbORTable) tableSO;
                boolean dependant = table.isIsDependant();
                GraphicShape shape = RectangleShape.singleton;
                if (dependant)
                    shape = RectangleShape.singleton;

                this.setShape(shape);
            } //end if
        } //end if
    } //end populateBox()

    private void initKeys() throws DbException {
        primaryKey = null;
        uniqueKeys = new ArrayList<DbORPrimaryUnique>();
        foreignKeys = new ArrayList<DbORForeign>();
        indexes = new ArrayList<DbORIndex>();
        DbEnumeration dbEnum = tableSO.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            DbObject dbo = dbEnum.nextElement();
            if (dbo instanceof DbORPrimaryUnique) {
                if (primaryKey == null && ((DbORPrimaryUnique) dbo).isPrimary())
                    primaryKey = (DbORPrimaryUnique) dbo;
                else
                    uniqueKeys.add((DbORPrimaryUnique) dbo);
            } else if (dbo instanceof DbORForeign)
                foreignKeys.add((DbORForeign) dbo);
            else if (dbo instanceof DbORIndex)
                indexes.add((DbORIndex) dbo);
        }
        dbEnum.close();
    }

    private String getUMLStereotypeName(DbSMSSemanticalObject semObject) throws DbException {
        String stereotypeName = null;
        DbSMSStereotype stereotype = semObject.getUmlStereotype();
        if (stereotype != null) {
            stereotypeName = "«" + stereotype.getName() + "»"; //NOT LOCALIZABLE
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
                constraintName = "{" + constraintName + "}"; //NOT LOCALIZABLE
            constraintsName = constraintsName + constraintName;
        } //end while
        dbEnum.close();
        return constraintsName;
    }

    private String getTableStereotypeName(DbORAbsTable tableSO) throws DbException {
        String stereotypeName = null;

        MetaField mf = DbORStyle.fOr_umlStereotypeDisplayed;
        Boolean b = (Boolean) tableGO.find(mf);
        if ((b != null) && b.booleanValue()) {
            stereotypeName = getUMLStereotypeName(tableSO);
        } //end if

        return stereotypeName;
    } //end getStereotype()

    private String getColumnStereotypeName(DbORColumn columnSO) throws DbException {
        String stereotypeName = null;

        MetaField mf = DbORStyle.fOr_umlStereotypeDisplayed;
        Boolean b = (Boolean) tableGO.find(mf);
        if ((b != null) && b.booleanValue()) {
            stereotypeName = getUMLStereotypeName(columnSO);
        } //end if

        return stereotypeName;
    } //end getStereotype()

    //must be within a read transaction
    private void populateStereotype() throws DbException {
        String stereotypeName = getTableStereotypeName(tableSO);
        Vector<CompositeCellRendererElement> composite = getStereotypeImageData(tableSO);
        DbORStyle style = getStyleFrom(tableGO);
        stereotypeZone.clear();

        int stereotypeStyle = 0;
        Boolean b = (style == null) ? Boolean.TRUE : style.getOr_umlStereotypeDisplayed();
        if ((b != null) && b.booleanValue()) {
            stereotypeZone.addColumn(new ColumnCellsOption(stereotypeNameCRO,
                    new SMSStereotypeEditor(DbORTable.fUmlStereotype)));
            stereotypeStyle += 1;
        }
        b = (style == null) ? Boolean.TRUE : style.getOr_umlStereotypeIconDisplayed();
        if ((b != null) && b.booleanValue()) {
            stereotypeZone.addColumn(new ColumnCellsOption(stereotypeImageCRO,
                    new SMSStereotypeEditor(DbORTable.fUmlStereotype)));
            stereotypeStyle += 2;
        }

        if (stereotypeName == null)
            stereotypeStyle = 0;

        stereotypeZone.addRow();

        switch (stereotypeStyle) {
        case 0:
            getStereotypeZone().setVisible(false);
            break;
        case 1:
            stereotypeZone.set(0, 0, new ZoneCell(tableSO, stereotypeName));
            getStereotypeZone().setVisible(true);
            break;
        case 2:
            stereotypeZone.set(0, 0, new ZoneCell(tableSO, composite));
            getStereotypeZone().setVisible(true);
            break;
        case 1 + 2:
            stereotypeZone.set(0, 0, new ZoneCell(tableSO, stereotypeName));
            stereotypeZone.set(0, 1, new ZoneCell(tableSO, composite));
            getStereotypeZone().setVisible(true);
            break;
        } //end switch

    } //end populateStereotype()

    private Vector<CompositeCellRendererElement> getStereotypeImageData(DbORAbsTable tableSO)
            throws DbException {
        Vector<CompositeCellRendererElement> compositeElements = new Vector<CompositeCellRendererElement>();

        DbSMSStereotype stereotype = tableSO.getUmlStereotype();
        if (stereotype != null) {
            Image image = stereotype.getIcon();
            if (image != null) {
                CompositeCellRendererElement elem = new CompositeCellRendererElement(
                        new ImageCellRenderer(), image);
                compositeElements.addElement(elem);
            }
        } //end if

        return compositeElements;
    } //end getStereotypeImageData()

    //must be within a read transaction
    private void populateName() throws DbException {
        MetaField additionnalDescriptor = (tableSO instanceof DbORTable ? DbORStyle.fOr_tableAdditionnalDescriptor
                : DbORStyle.fOr_viewAdditionnalDescriptor);

        tableZone.clear();
        tableZone.addColumn(new ColumnCellsOption(tablePrefixCRO, null));
        tableZone.addColumn(new ColumnCellsOption(tableAddDescriptorCRO, null));
        tableZone.addColumn(new ColumnCellsOption(tableNameCRO, null));
        tableZone.addColumn(new ColumnCellsOption(duplicateCRO, null));

        tableZone.addRow();
        tableZone.set(0, TABLE_PREFIX_IDX, new ZoneCell(tableSO, getPrefixData(tableSO)));
        tableZone.set(0, TABLE_ADD_DESCRIPTOR_IDX, getNameZoneCell(tableSO, additionnalDescriptor,
                null));
        tableZone.set(0, TABLE_NAME_IDX, getNameZoneCell(tableSO, DbORStyle.fOr_nameDescriptor,
                null, ((Boolean) tableGO.find(DbORStyle.fOr_tableOwnerDisplay)).booleanValue()));

        tableZone.set(0, TABLE_DUPLICATE_IDX, new ZoneCell(tableSO, calculateDuplicate(tableSO
                .getClassifierGos(), tableGO)));
    }

    private final ZoneCell getNameZoneCell(DbObject source, MetaField mfDescriptor,
            CellRenderingOption CRO) throws DbException {
        return getNameZoneCell(source, mfDescriptor, CRO, false);
    }

    private MetaField getDescriptorMF(int descriptor) {
        MetaField descriptorMF;

        switch (descriptor) {
        case SMSDisplayDescriptor.NAME:
            descriptorMF = DbSemanticalObject.fName;
            break;
        case SMSDisplayDescriptor.ALIAS:
            descriptorMF = DbSemanticalObject.fAlias;
            break;
        case SMSDisplayDescriptor.PHYSICAL_NAME:
            descriptorMF = DbSemanticalObject.fPhysicalName;
            break;
        default:
            descriptorMF = null;
            break;
        } //end switch 

        return descriptorMF;
    } //end getDescriptorMF()

    private final ZoneCell getNameZoneCell(DbObject source, MetaField mfDescriptor,
            CellRenderingOption CRO, boolean withOwner) throws DbException {
        String name = null;

        boolean editable = true;
        int descriptor = ((SMSDisplayDescriptor) tableGO.find(mfDescriptor)).getValue();
        MetaField descriptorMF = getDescriptorMF(descriptor);
        DbSemanticalObject semObj = (DbSemanticalObject) source;
        if (descriptorMF != null) {
            if (descriptorMF.equals(DbSemanticalObject.fName)) {
                name = semObj.getSemanticalName(DbObject.SHORT_FORM);
            } else {
                name = (String) semObj.get(descriptorMF);
            } //end if
        } //end if

        //if (name == null)
        //name = "";  //NOT LOCALIZABLE

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
            if (!(source instanceof DbORTable) && !(source instanceof DbORPrimaryUnique))
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

    public final int calculateIndexID(DbORIndex index) throws DbException {
        return indexes.indexOf(index) + 1;
    }

    public final int calculateForeignConstraintID(DbORForeign key) throws DbException {
        return foreignKeys.indexOf(key) + 1;
    }

    public final int calculateUniqueConstraintID(DbORPrimaryUnique key) throws DbException {
        return (key == primaryKey ? 0 : uniqueKeys.indexOf(key) + 1);
    }

    public final DbORPrimaryUnique getUniqueConstraintAt(int id) throws DbException {
        if (id == 0)
            return primaryKey;
        if (id > uniqueKeys.size())
            return null;
        return uniqueKeys.get(id - 1);
    }

    //must be within a read transaction
    private void populateColumns() throws DbException {

        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        DbObject table = getSemanticalObject();
        DbObject composite = terminologyUtil.isCompositeDataModel(table);

        boolean bHideForeignAttributes = false;
        if (composite != null)
            bHideForeignAttributes = (terminologyUtil.getModelLogicalMode(composite) == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP);

        if (columnZone.isVisible()) {
            columnZone.removeAllRows();
            DbEnumeration ins = tableSO.getComponents().elements(DbORColumn.metaClass);
            int rowCtr = 0;
            while (ins.hasMoreElements()) {
                DbORColumn column = (DbORColumn) ins.nextElement();

                if (true == bHideForeignAttributes)
                    if (true == column.isForeign())
                        continue;

                populateColumn(column, rowCtr);
                rowCtr++;
            } //end while
            ins.close();
        }
    } //end populateColumns()

    private void populateColumn(DbORColumn column, int rowCtr) throws DbException {
        //Column name
        CellRenderingOption addDescriptorRendering = getDescriptorCRO(column,
                columnAddDescriptorCRO);
        CellRenderingOption nameRendering = getDescriptorCRO(column, columnNameCRO);

        columnZone.addRow();
        columnZone.set(rowCtr, COLUMN_PREFIX_IDX, new ZoneCell(column, getPrefixData(column)));
        columnZone.set(rowCtr, COLUMN_ADD_DESCRIPTOR_IDX, getNameZoneCell(column,
                DbORStyle.fOr_columnAdditionnalDescriptor, addDescriptorRendering));
        columnZone.set(rowCtr, COLUMN_NAME_IDX, getNameZoneCell(column,
                DbORStyle.fOr_nameDescriptor, nameRendering));

        String LengthNbDecimal = null, nullValuePossible = null, defaultValue = null, columnStereotype = null;

        //Column Type Cell Visibility
        DbORTypeClassifier typeClassifier = column.getType();
        if (typeClassifier != null
                && ((Boolean) tableGO.find(DbORStyle.fOr_columnTypeDisplay)).booleanValue()) {
            CellRenderingOption typeRendering = getDescriptorCRO(column, columnTypeCRO);
            ZoneCell zc = getNameZoneCell(typeClassifier, DbORStyle.fOr_nameDescriptor,
                    typeRendering);
            zc.setObject(column); //change the object for the type editor
            zc.setCellEditor(new MemberTypeCellEditor()); //change for the good editor
            columnZone.set(rowCtr, COLUMN_TYPE_IDX, zc);
        } else {
            ZoneCell zoneCell = new ZoneCell(column, null);
            zoneCell.setObject(column); //change the object for the type editor
            zoneCell.setCellEditor(new MemberTypeCellEditor()); //change for the good editor
            columnZone.set(rowCtr, COLUMN_TYPE_IDX, zoneCell);
        }

        //Column source type Cell visibility
        DbORTypeClassifier sourceType = null;
        if (((Boolean) tableGO.find(DbORStyle.fOr_columnDomainSourceTypeDisplay)).booleanValue()) {
            if (typeClassifier != null && typeClassifier instanceof DbORDomain)
                sourceType = ((DbORDomain) typeClassifier).getSourceType();
        }
        if (sourceType != null) {
            CellRenderingOption sourceTypeRendering = getDescriptorCRO(column, columnSourceTypeCRO);
            ZoneCell zc = getNameZoneCell(sourceType, DbORStyle.fOr_nameDescriptor,
                    sourceTypeRendering);
            zc.setObject(column); //change the object for the type editor
            zc.setCellEditor(new MemberTypeCellEditor()); //change for the good editor
            zc.setEditable(false);
            columnZone.set(rowCtr, COLUMN_SOURCE_TYPE_IDX, zc);
        } else
            columnZone.set(rowCtr, COLUMN_SOURCE_TYPE_IDX, new ZoneCell(column, null));

        //Column Length Nb Decimal Cell visibility
        if (((Boolean) tableGO.find(DbORStyle.fOr_columnLengthDecimalsDisplay)).booleanValue()) {
            LengthNbDecimal = column.getLengthNbDecimal();
        }
        CellRenderingOption rendering = getDescriptorCRO(column, columnLengthDecimalCRO);
        columnZone.set(rowCtr, COLUMN_LENGTH_DECIMAL_IDX, new ZoneCell(column, LengthNbDecimal,
                rendering, null));

        //Column null Value Possible Cell visibility
        if (((Boolean) tableGO.find(DbORStyle.fOr_columnNullValueDisplay)).booleanValue()) {
            nullValuePossible = (column.isNull() ? LocaleMgr.screen.getString("Null")
                    : LocaleMgr.screen.getString("NotNull"));
        }
        rendering = getDescriptorCRO(column, columnNullValueCRO);
        columnZone.set(rowCtr, COLUMN_NULL_VALUE_IDX, new ZoneCell(column, nullValuePossible,
                rendering, null));

        //Column default Value Cell visibility
        if (((Boolean) tableGO.find(DbORStyle.fOr_columnDefaultValueDisplay)).booleanValue()) {
            defaultValue = column.getInitialValue();
        }
        rendering = getDescriptorCRO(column, columnDefaultValueCRO);
        columnZone.set(rowCtr, COLUMN_DEFAULT_VALUE_IDX, new ZoneCell(column, defaultValue,
                rendering, null));

        columnStereotype = getColumnStereotypeName(column);
        rendering = getDescriptorCRO(column, columnStereotypeNameCRO);
        columnZone.set(rowCtr, COLUMN_STEREOTYPE_IDX, new ZoneCell(column, columnStereotype,
                rendering, null));
    }

    private final List<DbORColumn> getColumnList(DbSMSSemanticalObject columnAggegrate,
            MetaRelationN relN) throws DbException {
        List<DbORColumn> columnsList = new ArrayList<DbORColumn>();
        if (columnAggegrate != null) {
            /*
             * //MetaRelationN relCols = null; //if (constraint instanceof DbORPrimaryUnique)
             * //relCols = DbORPrimaryUnique.fColumns; else if (constraint instanceof DbORForeign)
             * relCols = DbObject.fComponents; else { DbObject dbo =
             * ((DbORCheck)constraint).getColumn(); if (dbo != null) columnsList.add(dbo); }
             */

            if (relN == null) {
                DbORColumn dbo = ((DbORCheck) columnAggegrate).getColumn();
                if (dbo != null) {
                    columnsList.add(dbo);
                }
            } else {
                DbEnumeration columns = ((DbRelationN) columnAggegrate.get(relN)).elements();
                while (columns.hasMoreElements()) {
                    DbObject dbo = columns.nextElement();
                    if (dbo instanceof DbORFKeyColumn)
                        dbo = ((DbORFKeyColumn) dbo).getColumn();
                    columnsList.add((DbORColumn) dbo);
                }
                columns.close();
            }
        }

        return columnsList;
    }

    // return string format like (column1, column2)
    private final String getColumnsString(List<DbORColumn> columnList) throws DbException {
        String columnsString = null;

        int descriptor = ((SMSDisplayDescriptor) tableGO.find(DbORStyle.fOr_nameDescriptor))
                .getValue();
        MetaField descriptorMF = getDescriptorMF(descriptor);

        Iterator<DbORColumn> iterator = columnList.iterator();
        boolean firstColumn = true;
        columnsString = "("; //NOT LOCALIZABLE
        while (iterator.hasNext()) {
            DbORColumn column = (DbORColumn) iterator.next();
            if (!firstColumn)
                columnsString = columnsString + ", "; //NOT LOCALIZABLE

            String columnDesc = (descriptorMF == null) ? "" : (String) column.get(descriptorMF);
            columnsString = columnsString + columnDesc;
            firstColumn = false;
        }
        columnsString = columnsString + ")"; //NOT LOCALIZABLE

        return columnsString;
    }

    //must be within a read transaction
    private void populatePKConstaints() throws DbException {
        if (pKConstraintZone.isVisible()) {
            pKConstraintZone.removeAllRows();

            if (primaryKey != null) {
                pKConstraintZone.addRow();
                pKConstraintZone.set(0, PK_PREFIX_IDX, new ZoneCell(primaryKey,
                        getPrefixData(primaryKey)));
                pKConstraintZone.set(0, PK_NAME_IDX, getNameZoneCell(primaryKey,
                        DbORStyle.fOr_nameDescriptor, null));

                String columns = null;
                if (((Boolean) tableGO.find(DbORStyle.fOr_pkColumnsDisplay)).booleanValue()) {
                    List<DbORColumn> columnList = getColumnList(primaryKey,
                            DbORPrimaryUnique.fColumns);
                    columns = getColumnsString(columnList);
                }

                pKConstraintZone.set(0, PK_COLUMNS_IDX, new ZoneCell(primaryKey, columns));
            }
        }
    }

    //must be within a read transaction
    private void populateUKConstaints() throws DbException {
        if (uKConstraintZone.isVisible()) {
            uKConstraintZone.removeAllRows();

            for (int rowCtr = 0; rowCtr < uniqueKeys.size(); rowCtr++) {
                DbORPrimaryUnique unique = (DbORPrimaryUnique) uniqueKeys.get(rowCtr);
                uKConstraintZone.addRow();
                uKConstraintZone.set(rowCtr, UK_PREFIX_IDX, new ZoneCell(unique,
                        getPrefixData(unique)));
                uKConstraintZone.set(rowCtr, UK_NAME_IDX, getNameZoneCell(unique,
                        DbORStyle.fOr_nameDescriptor, null));

                String columns = null;
                if (((Boolean) tableGO.find(DbORStyle.fOr_ukColumnsDisplay)).booleanValue()) {
                    List<DbORColumn> columnList = getColumnList(unique, DbORPrimaryUnique.fColumns);
                    columns = getColumnsString(columnList);
                }

                uKConstraintZone.set(rowCtr, UK_COLUMNS_IDX, new ZoneCell(unique, columns));
            }
        }
    }

    //must be within a read transaction
    private void populateFKConstaints() throws DbException {
        if (fKConstraintZone.isVisible()) {
            fKConstraintZone.removeAllRows();

            for (int rowCtr = 0; rowCtr < foreignKeys.size(); rowCtr++) {
                DbORForeign fk = (DbORForeign) foreignKeys.get(rowCtr);
                fKConstraintZone.addRow();
                fKConstraintZone.set(rowCtr, FK_PREFIX_IDX, new ZoneCell(fk, getPrefixData(fk)));
                fKConstraintZone.set(rowCtr, FK_NAME_IDX, getNameZoneCell(fk,
                        DbORStyle.fOr_nameDescriptor, null));

                String columns = null;
                if (((Boolean) tableGO.find(DbORStyle.fOr_fkColumnsDisplay)).booleanValue()) {
                    List<DbORColumn> columnList = getColumnList(fk, DbObject.fComponents);
                    columns = getColumnsString(columnList);
                }

                fKConstraintZone.set(rowCtr, FK_COLUMNS_IDX, new ZoneCell(fk, columns));
            }
        }
    }

    //must be within a read transaction
    private void populateIndex() throws DbException {
        if (indexZone.isVisible()) {
            indexZone.removeAllRows();

            for (int rowCtr = 0; rowCtr < indexes.size(); rowCtr++) {
                DbORIndex index = indexes.get(rowCtr);
                indexZone.addRow();
                indexZone.set(rowCtr, INDEX_PREFIX_IDX, new ZoneCell(index, getPrefixData(index)));
                indexZone.set(rowCtr, INDEX_NAME_IDX, getNameZoneCell(index,
                        DbORStyle.fOr_nameDescriptor, null));

                String columnsString = null;
                if (((Boolean) tableGO.find(DbORStyle.fOr_indexColumnsDisplay)).booleanValue()) {
                    List<DbORColumn> columnList = new ArrayList<DbORColumn>();
                    DbEnumeration indexKeys = index.getComponents()
                            .elements(DbORIndexKey.metaClass);
                    while (indexKeys.hasMoreElements()) {
                        DbORIndexKey indexKey = (DbORIndexKey) indexKeys.nextElement();
                        DbORColumn column = indexKey.getIndexedElement();
                        if (column != null)
                            columnList.add(column);
                    } //end while
                    indexKeys.close();

                    columnsString = getColumnsString(columnList);
                } //end if 

                indexZone.set(rowCtr, INDEX_COLUMNS_IDX, new ZoneCell(index, columnsString));
                String type = null;
                if (((Boolean) tableGO.find(DbORStyle.fOr_indexTypeDisplay)).booleanValue()
                        && index instanceof DbINFIndex)
                    type = ((DbINFIndex) index).isCluster() ? LocaleMgr.screen.getString("CLUSTER")
                            : null;
                indexZone.set(rowCtr, INDEX_TYPE_IDX, new ZoneCell(index, type));
                String unique = null;
                if (((Boolean) tableGO.find(DbORStyle.fOr_indexUniqueDisplay)).booleanValue())
                    unique = (index.isUnique() ? LocaleMgr.screen.getString("Unique")
                            : LocaleMgr.screen.getString("NotUnique"));
                indexZone.set(rowCtr, INDEX_UNIQUE_IDX, new ZoneCell(index, unique));
            }
        }
    }

    //must be within a read transaction
    private void populateCheckConstaints() throws DbException {
        if (checkConstraintZone.isVisible()) {
            checkConstraintZone.removeAllRows();
            DbEnumeration ins = tableSO.getComponents().elements(DbORCheck.metaClass);
            int rowCtr = 0;
            while (ins.hasMoreElements()) {
                DbORCheck check = (DbORCheck) ins.nextElement();
                checkConstraintZone.addRow();
                checkConstraintZone.set(rowCtr, CHECK_PREFIX_IDX, new ZoneCell(check,
                        getPrefixData(check)));
                checkConstraintZone.set(rowCtr, CHECK_NAME_IDX, getNameZoneCell(check,
                        DbORStyle.fOr_nameDescriptor, null));
                String columns = null;
                if (((Boolean) tableGO.find(DbORStyle.fOr_checkParametersDisplay)).booleanValue()) {
                    List<DbORColumn> columnList = getColumnList(check, null);
                    columns = getColumnsString(columnList);
                }

                checkConstraintZone.set(rowCtr, CHECK_COLUMNS_IDX, new ZoneCell(check, columns));
                rowCtr++;
            }
            ins.close();
        }

    }

    //must be within a read transaction
    private void populateTriggers() throws DbException {

        if (triggerZone.isVisible()) {
            triggerZone.removeAllRows();
            DbEnumeration ins = tableSO.getComponents().elements(DbORTrigger.metaClass);
            int rowCtr = 0;
            while (ins.hasMoreElements()) {
                DbORTrigger trigger = (DbORTrigger) ins.nextElement();
                triggerZone.addRow();
                triggerZone.set(rowCtr, TRIGGER_PREFIX_IDX, new ZoneCell(trigger,
                        getPrefixData(trigger)));
                triggerZone.set(rowCtr, TRIGGER_NAME_IDX, getNameZoneCell(trigger,
                        DbORStyle.fOr_nameDescriptor, null));

                String columnsString = null;
                if (((Boolean) tableGO.find(DbORStyle.fOr_triggerColumnsDisplay)).booleanValue()) {
                    List<DbORColumn> columnList = getColumnList(trigger, DbORTrigger.fColumns);
                    columnsString = getColumnsString(columnList);
                }

                triggerZone.set(rowCtr, TRIGGER_COLUMNS_IDX, new ZoneCell(trigger, columnsString));
                rowCtr++;
            }
            ins.close();
        }

    }

    private void populateConstraints() throws DbException {
        if (constraintZone.isVisible()) {
            constraintZone.removeAllRows();
            DbEnumeration dbEnum = tableSO.getUmlConstraints().elements(
                    DbSMSUmlConstraint.metaClass);
            int rowCtr = 0;
            while (dbEnum.hasMoreElements()) {
                DbSMSUmlConstraint constraint = (DbSMSUmlConstraint) dbEnum.nextElement();
                constraintZone.addRow();
                String constraintName = constraint.getName();
                if (constraintName == null)
                    constraintName = "";
                else
                    constraintName = "{" + constraintName + "}"; //NOT LOCALIZABLE

                ZoneCell cell = new ZoneCell(constraint, constraintName);
                constraintZone.set(rowCtr, 0, cell);
                rowCtr++;
            } //end while
            dbEnum.close();
        }
    } //end populateConstraints()

    private Vector<CompositeCellRendererElement> getPrefixData(DbSemanticalObject semObj)
            throws DbException {
        Debug.assert2(semObj != null, "null argument not allowed. ORTableBox.getPrefixData().");
        Vector<CompositeCellRendererElement> compositeElements = new Vector<CompositeCellRendererElement>();

        // Semantic Object prefix
        int status = semObj.getValidationStatus();
        if (status == DbObject.VALIDATION_ERROR) {
            DbtPrefix prefix = getPrefix(tableGO, DbORStyle.fErrorPrefix);
            if (prefix != null) {
                addToCompositeElements(compositeElements, prefix, -1);
            }
        } else if (status == DbObject.VALIDATION_WARNING) {
            DbtPrefix prefix = getPrefix(tableGO, DbORStyle.fWarningPrefix);
            if (prefix != null) {
                addToCompositeElements(compositeElements, prefix, -1);
            }
        } //end if

        // Table Prefix
        if (semObj instanceof DbORAbsTable) {
            DbORAbsTable table = (DbORAbsTable) semObj;
            if (table instanceof DbORView)
                addToCompositeElements(compositeElements, (DbtPrefix) tableGO
                        .find(DbORStyle.fOr_viewPrefix), -1);
            if (table.getSuperCopy() != null)
                addToCompositeElements(compositeElements, (DbtPrefix) tableGO
                        .find(DbORStyle.fOr_tableLinkedToSuperModelPrefix), -1);
            if (table.getSubCopies().size() != 0)
                addToCompositeElements(compositeElements, (DbtPrefix) tableGO
                        .find(DbORStyle.fOr_tableLinkedToSubModelPrefix), -1);
        }
        // Column Prefix
        else if (semObj instanceof DbORColumn) {
            DbORColumn column = (DbORColumn) semObj;
            getColumnPrefixData(column, compositeElements);
        }
        // Primary & Unique Constraint Prefix
        else if (semObj instanceof DbORPrimaryUnique) {
            if (semObj == primaryKey)
                addToCompositeElements(compositeElements, (DbtPrefix) tableGO
                        .find(DbORStyle.fOr_pkColumnsPrefix), -1);
            else
                addToCompositeElements(compositeElements, (DbtPrefix) tableGO
                        .find(DbORStyle.fOr_ukColumnsPrefix),
                        calculateUniqueConstraintID((DbORPrimaryUnique) semObj));
        }
        // Foreign Key Prefix
        else if (semObj instanceof DbORForeign)
            addToCompositeElements(compositeElements, (DbtPrefix) tableGO
                    .find(DbORStyle.fOr_fkColumnsPrefix),
                    calculateForeignConstraintID((DbORForeign) semObj));
        //Index Prefix
        else if (semObj instanceof DbORIndex)
            addToCompositeElements(compositeElements, (DbtPrefix) tableGO
                    .find(DbORStyle.fOr_indexColumnsPrefix), calculateIndexID((DbORIndex) semObj));

        return compositeElements;
    }

    private void getColumnPrefixData(DbORColumn column,
            Vector<CompositeCellRendererElement> compositeElements) throws DbException {
        //Link Prefix
        DbEnumeration enu = column.getSourceLinks().elements();
        boolean hasSources = enu.hasMoreElements();
        enu.close();

        enu = column.getTargetLinks().elements();
        boolean hasTargets = enu.hasMoreElements();
        enu.close();

        if (hasSources) {
            DbtPrefix prefix = getPrefix(tableGO, DbORStyle.fSourcePrefix);
            if (prefix != null) {
                addToCompositeElements(compositeElements, prefix, -1);
            }
        }

        if (hasTargets) {
            DbtPrefix prefix = getPrefix(tableGO, DbORStyle.fTargetPrefix);
            if (prefix != null) {
                addToCompositeElements(compositeElements, prefix, -1);
            }
        }

        //Unique Constraints & Foreign Column
        List<Integer> ukOrderedList = new ArrayList<Integer>();
        DbEnumeration ukEnum = column.getPrimaryUniques().elements();
        while (ukEnum.hasMoreElements()) {
            DbORPrimaryUnique uniqueKey = (DbORPrimaryUnique) ukEnum.nextElement();
            if (uniqueKey == primaryKey)
                addToCompositeElements(compositeElements, (DbtPrefix) tableGO
                        .find(DbORStyle.fOr_pkColumnsPrefix), -1);
            else
                ukOrderedList.add(new Integer(calculateUniqueConstraintID(uniqueKey)));
        }
        ukEnum.close();

        List<Integer> fkOrderedList = new ArrayList<Integer>();
        DbEnumeration fkEnum = column.getFKeyColumns().elements();
        while (fkEnum.hasMoreElements()) {
            DbORForeign foreignKey = (DbORForeign) fkEnum.nextElement().getComposite();
            fkOrderedList.add(new Integer(calculateForeignConstraintID(foreignKey)));
        }
        fkEnum.close();

        Object[] orderedArray = ukOrderedList.toArray();
        Arrays.sort(orderedArray);
        for (int i = 0; i < orderedArray.length; i++) {
            addToCompositeElements(compositeElements, (DbtPrefix) tableGO
                    .find(DbORStyle.fOr_ukColumnsPrefix), ((Integer) orderedArray[i]).intValue());
        }

        orderedArray = fkOrderedList.toArray();
        Arrays.sort(orderedArray);
        for (int i = 0; i < orderedArray.length; i++) {
            addToCompositeElements(compositeElements, (DbtPrefix) tableGO
                    .find(DbORStyle.fOr_fkColumnsPrefix), ((Integer) orderedArray[i]).intValue());
        }

        if (column.isForeign() && orderedArray.length == 0)
            addToCompositeElements(compositeElements, (DbtPrefix) tableGO
                    .find(DbORStyle.fOr_fkColumnsPrefix), -1);

        //Index Column
        DbEnumeration indexKeys = column.getIndexKeys().elements(DbORIndexKey.metaClass);
        List<Integer> OrderedList = new ArrayList<Integer>();
        while (indexKeys.hasMoreElements()) {
            DbORIndexKey indexKey = (DbORIndexKey) indexKeys.nextElement();
            OrderedList.add(new Integer(calculateIndexID((DbORIndex) indexKey.getComposite())));
        }
        indexKeys.close();

        orderedArray = OrderedList.toArray();
        Arrays.sort(orderedArray);
        for (int i = 0; i < orderedArray.length; i++) {
            addToCompositeElements(compositeElements, (DbtPrefix) tableGO
                    .find(DbORStyle.fOr_indexColumnsPrefix), ((Integer) orderedArray[i]).intValue());
        }

        //Super-Sub columns
        if (column.getSuperCopy() != null)
            addToCompositeElements(compositeElements, (DbtPrefix) tableGO
                    .find(DbORStyle.fOr_columnLinkedToSuperModelPrefix), -1);
        if (column.getSubCopies().size() != 0)
            addToCompositeElements(compositeElements, (DbtPrefix) tableGO
                    .find(DbORStyle.fOr_columnLinkedToSubModelPrefix), -1);

        //Null prefix
        if (column.isNull()) {
            DbtPrefix pref = (DbtPrefix) tableGO.find(DbORStyle.fOr_columnNullValuePrefix);
            addToCompositeElements(compositeElements, pref, -1);
        }

        //Specialisation
        DbORChoiceOrSpecialization spec = column.getChoiceOrSpecialization();
        if (spec != null) {
            ORChoiceSpecializationCategory categ = spec.getCategory();
            int value = categ.getValue();

            if (value == ORChoiceSpecializationCategory.SPECIALIZATION) {
                DbtPrefix pref = (DbtPrefix) tableGO.find(DbORStyle.fOr_columnSpecializationPrefix);
                addToCompositeElements(compositeElements, pref, -1);
            } else if (value == ORChoiceSpecializationCategory.CHOICE) {
                DbtPrefix pref = (DbtPrefix) tableGO.find(DbORStyle.fOr_columnChoicePrefix);
                addToCompositeElements(compositeElements, pref, -1);
            }
        } //end if
    } //end getColumnPrefixData()

    private DbtPrefix getPrefix(DbORTableGo tableGO2, MetaField prefixMF) throws DbException {
        DbtPrefix prefix = null;
        DbSMSStyle style = tableGO2.findStyle();
        DbSMSStyle ancestorStyle = style.getAncestor();

        if (ancestorStyle == null) {
            Object o = style.get(prefixMF);
            if (o instanceof DbtPrefix) {
                prefix = (DbtPrefix) o;
            } else {
                prefix = PrefixOptionComponent.getDefaultPrefix(style, prefixMF);
            } //end if

        } else {
            prefix = (DbtPrefix) tableGO2.find(prefixMF);
        }

        return prefix;
    }

    //param int nb is value of DbORStyle.SEQUENCE_KEYWORD, if value is -1 then DbORStyle.SEQUENCE_KEYWORD is change for ''
    private void addToCompositeElements(Vector<CompositeCellRendererElement> compositeElements,
            DbtPrefix prefix, int nb) {
        if (prefix.getDisplayedComponent() == DbtPrefix.DISPLAY_TEXT) {
            String textPrefix = prefix.getText();
            if (textPrefix != null && textPrefix.length() > 0) {
                if ((textPrefix.indexOf(DbORStyle.SEQUENCE_KEYWORD) != -1)) {
                    String number = (nb != -1 ? Integer.toString(nb) : "");
                    textPrefix = StringUtil.replaceWords(textPrefix, DbORStyle.SEQUENCE_KEYWORD,
                            number);
                }

                CompositeCellRendererElement elem = new CompositeCellRendererElement(
                        new StringCellRenderer(), textPrefix);
                compositeElements.addElement(elem);
            }
        } else if (prefix.getDisplayedComponent() == DbtPrefix.DISPLAY_IMAGE) {
            Image imagePrefix = prefix.getImage();
            if (imagePrefix != null)
                compositeElements.addElement(new CompositeCellRendererElement(
                        new ImageCellRenderer(), imagePrefix));
        }
    }

    private final CellRenderingOption getDescriptorCRO(DbORColumn column,
            CellRenderingOption sourceCRO) throws DbException {
        ArrayList<MetaField> metafields = new ArrayList<MetaField>();
        CellRenderingOption cro = new CellRenderingOption(sourceCRO);
        DbEnumeration puKeys = column.getPrimaryUniques().elements();
        while (puKeys.hasMoreElements()) {
            DbORPrimaryUnique puKey = (DbORPrimaryUnique) puKeys.nextElement();
            if (puKey == primaryKey)
                metafields.add(DbORStyle.fOr_pkDescriptorFormat);
            else
                metafields.add(DbORStyle.fOr_ukDescriptorFormat);
        }
        puKeys.close();

        if (column.isForeign())
            metafields.add(DbORStyle.fOr_fkDescriptorFormat);

        if (metafields.size() > 0) {
            ORDescriptorFormat format = getColumnDescriptorFormat(metafields.toArray());

            if (format.isBold() || format.isItalic() || format.isUnderline()) {
                if (format.isUnderline())
                    cro.setCellRenderer(new StringCellRenderer(true, false));
                if (format.isBold() && format.isItalic())
                    cro.setFont(cro.getFont().deriveFont(Font.BOLD + Font.ITALIC));
                else if (format.isBold()) {
                    Font font = cro.getFont();
                    cro.setFont(font.isItalic() ? font.deriveFont(Font.BOLD + Font.ITALIC) : font
                            .deriveFont(Font.BOLD));
                } else if (format.isItalic()) {
                    Font font = cro.getFont();
                    cro.setFont(font.isBold() ? font.deriveFont(Font.BOLD + Font.ITALIC) : font
                            .deriveFont(Font.ITALIC));
                }
            } //end if
        } //end if

        PropertiesSet applOptions = PropertiesManager.APPLICATION_PROPERTIES_SET;
        boolean recentlyModifiedFields = applOptions.getPropertyBoolean(
                DisplayRecentModifications.class,
                DisplayRecentModifications.DISP_RECENTLY_MODIFIED_COLUMNS_PROPERTY, true);

        if (recentlyModifiedFields) {
            DisplayRecentModifications recentDisplay = DisplayRecentModifications.getInstance();
            cro = recentDisplay.getRenderingOptionForRecent(column, cro);
        }

        return cro;
    }

    private final ORDescriptorFormat getColumnDescriptorFormat(Object[] metaFields)
            throws DbException {
        boolean bold = false, italic = false, underline = false;
        int i = 0;
        if (metaFields != null) {
            while ((i < metaFields.length)
                    && !(bold == true && italic == true && underline == true)) {
                ORDescriptorFormat format = (ORDescriptorFormat) tableGO
                        .find((MetaField) metaFields[i]);
                if (format.isBold())
                    bold = true;
                if (format.isItalic())
                    italic = true;
                if (format.isUnderline())
                    underline = true;
                i++;
            }
        }

        return new ORDescriptorFormat(bold, italic, underline);
    }

    // Used by AddAbstractAction
    public final void editMember(DbSemanticalObject so) {
        MatrixZone zone = null;
        int col = 0;

        if (so instanceof DbORAbsTable) {
            zone = getTableZone();
            col = TABLE_NAME_IDX;
        } else if (so instanceof DbORColumn) {
            zone = getColumnZone();
            col = COLUMN_NAME_IDX;
        } else if (so instanceof DbORIndex) {
            zone = getIndexZone();
            col = INDEX_NAME_IDX;
        } else if (so instanceof DbORCheck) {
            zone = getCheckConstraintZone();
            col = CHECK_NAME_IDX;
        } else if (so instanceof DbORTrigger) {
            zone = getTriggerZone();
            col = TRIGGER_NAME_IDX;
        } else if (so instanceof DbORPrimaryUnique) {
            if (so == primaryKey) {
                zone = getPrimaryKeyZone();
                col = PK_NAME_IDX;
            } else {
                zone = getUniqueKeyZone();
                col = UK_NAME_IDX;
            }
        }

        if (zone != null)
            ((ApplicationDiagram) diagram).editCell(this, zone.getCellID(so, col));
    }

    public final String getToolTipText() {
        return (String) tableZone.get(0, TABLE_NAME_IDX).getCellData();
    }

    /*
     * Unique listener listening to any change in the semantical part of the database that affect
     * the data needed to draw an ORTableBox.
     */
    private static class BoxRefreshTg implements DbRefreshListener, DbRefreshPassListener {

        private Set<DbORAbsTable> refreshedTables = null;

        BoxRefreshTg() {
        }

        public void beforeRefreshPass(Db db) throws DbException {
            refreshedTables = null;
        }

        public void afterRefreshPass(Db db) throws DbException {
            refreshedTables = null; // for gc
        }

        public void refreshAfterDbUpdate(DbUpdateEvent e) throws DbException {
            refreshAfterDbUpdate(e.dbo, e.metaField);
        }

        private void refreshAfterDbUpdate(DbObject dbo, MetaField mf) throws DbException {

            if (mf == DbObject.fComponents) {
                if (dbo instanceof DbORAbsTable)
                    refreshTable((DbORAbsTable) dbo);
                else if (dbo instanceof DbORIndex || dbo instanceof DbORForeign)
                    refreshTable((DbORAbsTable) dbo.getComposite());
            } else if (dbo instanceof DbORAbsTable) {
                refreshTable((DbORAbsTable) dbo);
            } else if (dbo instanceof DbORColumn) {
                refreshTable((DbORAbsTable) dbo.getComposite());
            } else if (dbo instanceof DbORConstraint) {
                refreshTable((DbORAbsTable) dbo.getComposite());
            } else if (dbo instanceof DbORIndex) {
                refreshTable((DbORAbsTable) dbo.getComposite());
            } else if (dbo instanceof DbORIndexKey) {
                refreshTable((DbORAbsTable) dbo.getComposite().getComposite());
            } else if (dbo instanceof DbORTrigger) {
                refreshTable((DbORAbsTable) dbo.getComposite());
            } else if (dbo instanceof DbORTypeClassifier
                    && (mf == DbSemanticalObject.fName || mf == DbSemanticalObject.fAlias || mf == DbSemanticalObject.fPhysicalName)) {
                refreshAttributeTableTypedBy(((DbORTypeClassifier) dbo).getTypedAttributes());
            } else if (dbo instanceof DbORUser) {
                refreshTableOwnedBy(((DbORUser) dbo).getTables());
            }
        }

        private void refreshTableOwnedBy(DbRelationN objects) throws DbException {
            int nb = objects.size();
            for (int i = 0; i < nb; i++) {
                refreshTable((DbORAbsTable) objects.elementAt(i));
            }
        }

        private void refreshAttributeTableTypedBy(DbRelationN attributes) throws DbException {
            int nb = attributes.size();
            for (int i = 0; i < nb; i++) {
                DbObject attibute = attributes.elementAt(i);
                DbORAbsTable table = (DbORAbsTable) attibute
                        .getCompositeOfType(DbORAbsTable.metaClass);
                if (table != null)
                    refreshTable(table);
            }
        }

        /*
         * Rebuild the data of all the ORTableBoxes of an Table.
         */
        private void refreshTable(DbORAbsTable table) throws DbException {
            if (refreshedTables == null)
                refreshedTables = new HashSet<DbORAbsTable>();
            if (!refreshedTables.add(table)) // already done in this transaction
                return;

            DbRelationN tableGos = table.getClassifierGos();
            int nb = tableGos.size();
            for (int i = 0; i < nb; i++) {
                DbORTableGo tableGo = (DbORTableGo) tableGos.elementAt(i);
                GraphicNode node = (GraphicNode) tableGo.getGraphicPeer();

                if (node instanceof ORTableBox) {
                    ORTableBox tableBox = (ORTableBox) node;
                    tableBox.populateContents();
                }
            } //end for

            //if choice changes to specialization, or viceversa, refresh '@' and 'c' column prefixes 
            if (table instanceof DbORChoiceOrSpecialization) {
                DbORChoiceOrSpecialization spec = (DbORChoiceOrSpecialization) table;
                DbEnumeration enu = spec.getColumns().elements();
                while (enu.hasMoreElements()) {
                    DbORColumn col = (DbORColumn) enu.nextElement();
                    DbORTable assocTable = (DbORTable) col.getCompositeOfType(DbORTable.metaClass);
                    DbEnumeration enu2 = assocTable.getClassifierGos().elements();
                    while (enu2.hasMoreElements()) {
                        DbORTableGo go = (DbORTableGo) enu2.nextElement();
                        GraphicNode node = (GraphicNode) go.getGraphicPeer();

                        if (node instanceof ORTableBox) {
                            ORTableBox tableBox = (ORTableBox) node;
                            tableBox.populateContents();
                        }
                    } //end while
                    enu2.close();
                } //end while
                enu.close();
            } //end if
        } //end refreshTable()
    }

    private class TableGoRefreshTg implements DbRefreshListener {
        TableGoRefreshTg() {
        }

        public void refreshAfterDbUpdate(DbUpdateEvent e) throws DbException {
            if (DbSMSGraphicalObject.fStyle == e.metaField) {
                initStyleElements();
                populateContents();

            } else if (e.metaField == DbSMSClassifierGo.fBackgroundColor
                    || e.metaField == DbSMSClassifierGo.fDashStyle
                    || e.metaField == DbSMSClassifierGo.fHighlight
                    || e.metaField == DbSMSClassifierGo.fLineColor
                    || e.metaField == DbSMSClassifierGo.fTextColor) {
                initStyleElements();
                populateContents();
            }
        }
    }

    private static class MemberTypeCellEditor implements CellEditor {
        MemberTypeCellEditor() {
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
