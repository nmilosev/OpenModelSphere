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

package org.modelsphere.sms.be.graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;

import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbRefreshPassListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.shape.RoundRectShape;
import org.modelsphere.jack.graphic.zone.CellRenderer;
import org.modelsphere.jack.graphic.zone.CellRenderingOption;
import org.modelsphere.jack.graphic.zone.MatrixZone;
import org.modelsphere.jack.graphic.zone.SingletonZone;
import org.modelsphere.jack.graphic.zone.StringCellRenderer;
import org.modelsphere.jack.graphic.zone.TableCell;
import org.modelsphere.jack.graphic.zone.TableZone;
import org.modelsphere.jack.graphic.zone.Zone;
import org.modelsphere.jack.graphic.zone.ZoneCell;
import org.modelsphere.jack.srtool.actions.RefreshAllAction;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.be.db.DbBEContextCell;
import org.modelsphere.sms.be.db.DbBEContextGo;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEStyle;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.DbBEUseCaseGo;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.srtypes.SMSHorizontalAlignment;
import org.modelsphere.sms.db.srtypes.SMSVerticalAlignment;
import org.modelsphere.sms.db.util.DbInitialization;

public class BEContext extends GraphicNode implements ActionInformation {

    static {
        BoxRefreshTg useCaseRefreshTg = new BoxRefreshTg();
        Db.addDbRefreshPassListener(useCaseRefreshTg);
        MetaField.addDbRefreshListener(useCaseRefreshTg, (DbProject) null, new MetaField[] {
                DbObject.fComponents, DbSemanticalObject.fName, DbSemanticalObject.fAlias,
                DbSemanticalObject.fPhysicalName, DbBEUseCase.fClassifierGos,
                DbBEUseCase.fAlphanumericIdentifier, DbBEUseCase.fNumericIdentifier,
                DbBEContextGo.fComponents });
    }

    // the db reference
    protected DbBEContextGo contextGO;
    protected DbBEUseCase useCaseSO;
    protected DbBENotation notation;
    private SingletonZone nameZone;
    private TableZone tableZone;
    private HashMap zonesMap = new HashMap();
    private boolean displayFrameBox;

    // listeners
    transient private BoxGoRefreshTg useCaseGoRefreshTg;
    private MetaField[] refreshMetaFields = new MetaField[] { DbBEContextGo.fComponents,
            DbBEContextCell.fX, DbBEContextCell.fY, DbBEContextCell.fDescription,
            DbBEContextCell.fFont, DbBEContextCell.fRightBorder, DbBEContextCell.fBottomBorder,
            DbBEContextCell.fVerticalJustification, DbBEContextCell.fHorizontalJustification,
            DbBEContextCell.fXweight, DbBEContextCell.fYweight };

    //
    // Constructor and methods called by the constructor
    //
    public BEContext(Diagram diag, DbBEContextGo newBoxGO) throws DbException {
        super(diag, RoundRectShape.singleton);
        this.setLineStyle(true, false);
        useCaseGoRefreshTg = new BoxGoRefreshTg(this, newBoxGO);
        contextGO = newBoxGO;
        useCaseSO = (DbBEUseCase) newBoxGO.getSO();
        setAutoFit(contextGO.isAutoFit());
        setRectangle(contextGO.getRectangle());
        notation = ((DbBEDiagram) newBoxGO.getComposite()).findMasterNotation();
        objectInit();
    } // end BEContext()

    private void objectInit() throws DbException {
        contextGO.setGraphicPeer(this);
        contextGO.addDbRefreshListener(useCaseGoRefreshTg);
        DbProject proj = contextGO.getProject();
        MetaField.addDbRefreshListener(useCaseGoRefreshTg, proj, refreshMetaFields);
        Db.addDbRefreshPassListener(useCaseGoRefreshTg);
        setZones();
        initStyleElements();
        populateContents();
        displayFrameBox = performDisplay();
    } // end objectInit()

    private void setZones() throws DbException {
        // get options
        DbBEDiagram diagram = (DbBEDiagram) contextGO.getCompositeOfType(DbBEDiagram.metaClass);
        DbBENotation notation = diagram.findMasterNotation();
        Boolean tb = (notation == null) ? null : notation.getCellTitleBoxed();
        Boolean cd = (notation == null) ? null : notation.getCenterDisplay();
        boolean titleBoxed = (tb == null) ? false : tb.booleanValue();
        boolean centerDisplay = (cd == null) ? false : cd.booleanValue();
        DbBEStyle style = (DbBEStyle) contextGO.getStyle();
        if (style == null)
            style = (DbBEStyle) diagram.getStyle();
        Color bgColor = (style == null) ? Color.white : style.getBackgroundColorDbBEContextGo();
        Color fgColor = (style == null) ? Color.black : style.getLineColorDbBEContextCell();
        Color bxColor = (style == null) ? Color.black : style.getLineColorDbBEContextGo();
        Color txColor = (style == null) ? Color.black : style.getTextColorDbBEContextGo();

        nameZone = new SingletonZone("Name"); // NOT LOCALIZABLE
        nameZone.setHasBottomLine(true);
        addZone(nameZone);

        tableZone = new TableZone("CenterZone"); // NOT LOCALIZABLE
        addZone(tableZone);

        contextGO.getDb().beginReadTrans();
        DbRelationN relN = contextGO.getComponents();
        contextGO.getDb().commitTrans();
        DbEnumeration dbEnum = relN.elements(DbBEContextCell.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEContextCell contextCell = (DbBEContextCell) dbEnum.nextElement();
            int x = contextCell.getX().intValue();
            int y = contextCell.getY().intValue();
            double xw = contextCell.getXweight().doubleValue();
            double yw = contextCell.getYweight().doubleValue();
            boolean rightBorder = contextCell.isRightBorder();
            boolean bottomBorder = contextCell.isBottomBorder();
            String text = contextCell.getDescription();
            Font font = contextCell.getFont();

            SMSVerticalAlignment verticalAlign = contextCell.getVerticalJustification2();
            SMSHorizontalAlignment horizontalAlign = contextCell.getHorizontalJustification2();
            int vJustification = DbInitialization.convertToVerticalAlignment(verticalAlign);
            int hJustification = DbInitialization.convertToHorizontalAlignment(horizontalAlign);

            TableCell tableCell = new TableCell(x, y, xw, yw, rightBorder, bottomBorder, text,
                    font, hJustification, vJustification, titleBoxed, bgColor, fgColor, bxColor,
                    txColor, centerDisplay);
            tableZone.add(tableCell);
        } // end while
        dbEnum.close();

    } // end setZones()

    private void initStyleElements() throws DbException {
        setTextColor();
        setBoxColor();
        setLineStyle();

    } // end initStyleElements()

    private void setTextColor() throws DbException {
        Color color = (Color) contextGO.find(DbBEContextGo.fTextColor);
        Enumeration enumeration = this.displayedZones();
        while (enumeration.hasMoreElements()) {
            Zone zone = (Zone) enumeration.nextElement();
            zone.setTextColor(color);
        }
    } // end setTextColor()

    private void setBoxColor() throws DbException {
        setFillColor((Color) contextGO.find(DbBEContextGo.fBackgroundColor));
        setLineColor((Color) contextGO.find(DbBEContextGo.fLineColor));
    } // end setBoxColor()

    private void setLineStyle() throws DbException {
        this.setLineStyle((Boolean) contextGO.find(DbBEContextGo.fHighlight), (Boolean) contextGO
                .find(DbBEContextGo.fDashStyle));
    } // end setLineStyle()

    //
    // Public methods
    // 

    // called by SMSSelectTool
    public TableZone.BoundaryInfoStruct isOverCellBoundary(int x, int y, Rectangle rect,
            DiagramView view) {
        return tableZone.isOverCellBoundary(x, y, rect, view);
    }

    // overrides GraphicComponent
    public int getDefaultFitMode() {
        return GraphicComponent.NO_FIT;
    }

    // overrides GraphicComponent
    public int getLayer() {
        return Diagram.LAYER_BACKGROUND;
    }

    // overrides GraphicComponent
    public final boolean selectAtCellLevel() {
        return true;
    }

    // overrides ActionInformation
    public Db getDb() {
        return contextGO.getDb();
    }

    // overrides ActionInformation
    public final DbObject getSemanticalObject() {
        return useCaseSO;
    }

    // overrides ActionInformation
    public final DbObject getGraphicalObject() {
        return contextGO;
    }

    // overrides ZoneBox
    public void paint(Graphics g, DiagramView diagView, int drawingMode, int renderingFlags) {

        if (displayFrameBox) {
            super.paint(g, diagView, drawingMode, renderingFlags);
        } else {
            boolean drawSel = true; // simulates draw selection to avoid to
            // print a line
            paintZones(g, diagView, GraphicComponent.DRAW_FRAME, drawSel, renderingFlags);
        } // end if
    } // and paint()

    private boolean performDisplay() throws DbException {
        boolean displayFrameBox = false;

        if (notation != null) {
            Boolean b = notation.getDisplayFrameBox();
            displayFrameBox = (b == null) ? true : b.booleanValue();
        }

        return displayFrameBox;
    } // end performDisplay()

    // Overrides GraphicNode
    // BEWARE: overriding triggers must call super.delete as last action
    public void delete(boolean all) {
        contextGO.setGraphicPeer(null);
        contextGO.removeDbRefreshListener(useCaseGoRefreshTg);
        MetaField.removeDbRefreshListener(useCaseGoRefreshTg, refreshMetaFields);
        Enumeration enumeration = this.displayedZones();
        while (enumeration.hasMoreElements()) {
            Zone zone = (Zone) enumeration.nextElement();
            if (zone instanceof MatrixZone)
                ((MatrixZone) zone).removeAllRows();
        }
        super.delete(all);
    } // end delete()

    /*
     * for 2.1 //return ArrayList of ArrayList private ArrayList getCellsMatrix() throws
     * DbException{ ArrayList cellsMatrix = new ArrayList(); DbEnumeration dbEnum =
     * contextGO.getComponents().elements(DbBEContextCell.metaClass); while
     * (dbEnum.hasMoreElements()){ ArrayList columnsList = null; DbBEContextCell cell =
     * (DbBEContextCell)dbEnum.nextElement(); int cellRow = cell.getY().intValue(); int cellCol =
     * cell.getX().intValue();
     * 
     * if (cellsMatrix.size() > cellRow && cellsMatrix.get(cellRow) != null) columnsList =
     * (ArrayList)cellsMatrix.get(cellRow); else { columnsList = new ArrayList(cellCol + 1);
     * cellsMatrix.ensureCapacity(cellRow + 1); cellsMatrix.add(cellRow, columnsList); }
     * columnsList.ensureCapacity(cellCol + 1); columnsList.add(cellCol, cell); } dbEnum.close();
     * return cellsMatrix; }
     * 
     * public int getNbColumn() throws DbException{ ArrayList matrix = getCellsMatrix(); if
     * (matrix.size() != 0) return ((ArrayList)matrix.get(0)).size(); else return 0; }
     * 
     * public int getNbRow() throws DbException{ ArrayList matrix = getCellsMatrix(); return
     * matrix.size(); }
     * 
     * public Rectangle getMaxCellCreationRectangle(int x, int y) throws DbException{ ArrayList
     * cellsMatrix = getCellsMatrix(); Rectangle contentRect = getContentRect(); Rectangle rect =
     * null; if (cellsMatrix.size() == 0) rect = contentRect; else if (cellsMatrix.size() == 1 &&
     * ((ArrayList)cellsMatrix.get(0)).size() == 1) rect = contentRect; else { if
     * (getOverWhichBorder(x, y) == RIGHT_BORDER){ ArrayList row = (ArrayList)cellsMatrix.get(0);
     * DbBEContextCell cell = (DbBEContextCell)row.get(row.size() -1); int newX = contentRect.x +
     * contentRect.width - cell.getWidth().intValue(); rect = new Rectangle(newX, contentRect.y,
     * cell.getWidth().intValue(), contentRect.height); } if (getOverWhichBorder(x, y) ==
     * LEFT_BORDER){ ArrayList row = (ArrayList)cellsMatrix.get(0); DbBEContextCell cell =
     * (DbBEContextCell)row.get(0); rect = new Rectangle(contentRect.x, contentRect.y,
     * cell.getWidth().intValue(), contentRect.height); } else if (getOverWhichBorder(x, y) ==
     * BOTTOM_BORDER){ ArrayList row = (ArrayList)cellsMatrix.get(cellsMatrix.size() -1);
     * DbBEContextCell cell = (DbBEContextCell)row.get(0); int newY = contentRect.y +
     * contentRect.height - cell.getHeigth().intValue(); rect = new Rectangle(contentRect.x, newY,
     * contentRect.width, cell.getHeigth().intValue()); } } return rect; }
     */

    //
    // getter
    // 
    public final TableZone getCenterZone() {
        return tableZone;
    }

    //
    // private methods : populate
    // 

    private void populateContents() throws DbException {
        ZoneBoxSelection savedSel = new ZoneBoxSelection(this);
        populateFrameName();
        populateCells();
        // populateZones();
        savedSel.restore();
        diagram.setComputePos(this);
    } // end populateContents()

    private void populateFrameName() throws DbException {
        String frameName = useCaseSO.getNumericHierID();
        String useCaseName = useCaseSO.getName();
        if (frameName != null && useCaseName != null)
            frameName += " " + useCaseName; // NOT LOCALIZABLE
        else if (frameName == null && useCaseName != null)
            frameName = useCaseName;

        nameZone.setValue(new ZoneCell(useCaseSO, frameName, getCellRenderingOptionForConcept(
                useCaseSO, null, GraphicUtil.CENTER_ALIGNMENT), null));
    } // end populateFrameName()

    private void populateCells() throws DbException {
        /*
         * //remove rows and columns centerZone.clear();
         * 
         * //add 3 columns in center zone CellRenderer renderer = new StringCellRenderer(); int
         * alignment = GraphicUtil.CENTER_ALIGNMENT; CellRenderingOption newCellOption = new
         * CellRenderingOption(renderer, alignment); CellEditor newEditor = null; boolean
         * hasLeftLine = false;
         * 
         * ColumnCellsOption option = new ColumnCellsOption(newCellOption, newEditor, hasLeftLine);
         * centerZone.addColumn(option); centerZone.addColumn(option); centerZone.addColumn(option);
         * 
         * //add 3 rows centerZone.addRow(); centerZone.addRow(); centerZone.addRow(); int row = 0;
         * int col = 0; ZoneCell data = new ZoneCell(useCaseSO, "cell"); centerZone.set(0, 0, data);
         * centerZone.set(0, 1, data); centerZone.set(0, 2, data); centerZone.set(1, 0, data);
         * centerZone.set(1, 1, data); centerZone.set(1, 2, data); centerZone.set(2, 0, data);
         * centerZone.set(2, 1, data); centerZone.set(2, 2, data);
         */
    } // end populateCells()

    /*
     * private void populateCells() throws DbException { int zonePos = 0; ArrayList zones =
     * notation.getUseCaseZones(); Iterator i = zones.iterator(); while (i.hasNext()){
     * DbBESingleZoneDisplay zoneDisplay = (DbBESingleZoneDisplay)i.next(); if
     * (!zoneDisplay.isDisplayed() ||
     * zoneDisplay.getMetaField().equals(DbBEUseCaseResource.fResource)) //todo pour ressource cas
     * spécifique!!!!!! continue;
     * 
     * Object srcObj = zoneDisplay.getSourceObject(); MatrixZone zone =
     * (MatrixZone)zonesMap.get(srcObj); if (zone != null){ zone.removeAllRows(); int row = 0;
     * //todo pour l'utilisation pour les enum... zone.addRow(); Object value =
     * zoneDisplay.getValue(useCaseSO); String strValue = (value != null? value.toString(): null);
     * 
     * if (zonePos == 0){ String duplicate = this.calculateDuplicate(useCaseSO.getClassifierGos(),
     * contextGO); if (strValue == null) strValue = duplicate; else if (duplicate != null) strValue
     * = strValue + duplicate; } zone.set(row, 0, new ZoneCell(useCaseSO, strValue != null?
     * strValue: "", zoneDisplay.getMetaField() != null)); row++; zonePos++; } //end if } //end
     * while } //end populateCells()
     */

    /*
     * private MetaField getStyleMetaField(String genericName) throws DbException{ MetaField mf =
     * null; String nameMetaField;
     * 
     * MetaClass soMetaClass = useCaseSO.getMetaClass(); nameMetaField =
     * soMetaClass.getJClass().getName(); nameMetaField =
     * nameMetaField.substring(nameMetaField.lastIndexOf('.') + 1);
     * 
     * return contextGO.findStyle().getMetaField(genericName.concat(nameMetaField));; }
     */
    private CellRenderingOption getCellRenderingOptionForConcept(Object source,
            CellRenderer renderer, int alignment) throws DbException {
        Font font = (Font) contextGO.find(DbBEStyle.fFrameHeaderFont);
        CellRenderingOption cellOption = new CellRenderingOption(renderer != null ? renderer
                : new StringCellRenderer(false, true), font, alignment);
        return cellOption;
    }

    /*
     * private void setCells() throws DbException{ zonesMap = new HashMap(); ArrayList zones =
     * notation.getUseCaseZones(); Iterator i = zones.iterator(); while (i.hasNext()){
     * DbBESingleZoneDisplay zoneDisplay = (DbBESingleZoneDisplay)i.next(); if
     * (!zoneDisplay.isDisplayed() ||
     * zoneDisplay.getMetaField().equals(DbBEUseCaseResource.fResource)) //todo pour ressource cas
     * spécifique!!!!!! continue; int alignment; switch (zoneDisplay.getJustification().getValue()){
     * case ZoneJustification.CENTER: alignment = GraphicUtil.CENTER_ALIGNMENT; break; case
     * ZoneJustification.RIGHT: alignment = GraphicUtil.RIGHT_ALIGNMENT; break; default: alignment =
     * GraphicUtil.LEFT_ALIGNMENT; } MatrixZone zone = new MatrixZone(zoneDisplay.getGUIName(),
     * alignment); zonesMap.put(zoneDisplay.getSourceObject(), zone); addZone(zone); CellEditor
     * tfEditor = null; if (zoneDisplay.getMetaField() != null && !(zoneDisplay.getMetaField()
     * instanceof MetaRelation)) tfEditor = new DbTextFieldCellEditor(zoneDisplay.getMetaField(),
     * false); zone.addColumn(new ColumnCellsOption(this.getCellRenderingOptionForConcept
     * (zoneDisplay.getSourceObject(), null, alignment), tfEditor, false)); zone.setWrappingCol(0);
     * zone.setHasBottomLine(zoneDisplay.isSeparator()); } } //end setCells()
     */

    private void populateZones() {
        /*
         * try {
         * 
         * //TODO.. centerZone.addRow(); centerZone.addRow(); centerZone.addRow();
         * 
         * //CellRenderer newRenderer = new CellRenderer( //CellRenderingOption render = new
         * CellRenderingOption(); //ColumnCellsOption options = new ColumnCellsOption(render,
         * editor);
         * 
         * Object source = zoneDisplay.getSourceObject(); int alignment = 0; CellRenderingOption
         * renderingOptions = getCellRenderingOptionForConcept(source, null, alignment);
         * 
         * CellEditor editor = null; ColumnCellsOption options = new
         * ColumnCellsOption(renderingOptions, editor); centerZone.addColumn(options); } catch
         * (DbException ex) { } //end try
         */
    } // end populateZones()

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

    // overriden
    public Rectangle getSelectionAreaRectangle() {
        Rectangle rect = new Rectangle(getContentRect());
        rect.height = nameZone.getRectangle((Rectangle) null).height;
        return rect;
    }

    public final String getToolTipText() {
        return null;
    }

    /*
     * Unique listener listening to any change in the semantical part of the database that affect
     * the data needed to draw an BEContext.
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
            if (dbo instanceof DbBEUseCase) {
                refreshBox((DbBEUseCase) dbo);
            } else if (e.metaField == DbObject.fComponents) {
                if (dbo instanceof DbBEUseCase)
                    refreshBox((DbBEUseCase) dbo);
                else if (dbo instanceof DbBEContextGo) {
                    DbBEContextGo contextGo = (DbBEContextGo) dbo;
                    BEContext beContextBox = (BEContext) contextGo.getGraphicPeer();
                    if (beContextBox != null) {
                        beContextBox.populateContents();
                    }
                }
            } else if (dbo instanceof DbBEUseCase && ((DbBEUseCase) dbo).isContext()) {
                refreshBox((DbBEUseCase) dbo);
            } // end if
        } // end refreshAfterDbUpdate

        /*
         * Rebuild the data of all the BEContexts
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
                if (element instanceof DbBEUseCaseGo)
                    continue;
                DbBEContextGo contextGo = (DbBEContextGo) element;
                BEContext beContextBox = (BEContext) contextGo.getGraphicPeer();
                if (beContextBox != null)
                    beContextBox.populateContents();
            }
        }
    }

    private static class BoxGoRefreshTg implements DbRefreshListener, DbRefreshPassListener {
        private BEContext m_context;
        private DbBEContextGo m_boxGO;
        private Rectangle m_oldRect;
        private RefreshAllAction refreshAction = new RefreshAllAction();

        BoxGoRefreshTg(BEContext context, DbBEContextGo newBoxGO) throws DbException {
            m_context = context;
            m_boxGO = newBoxGO;
            m_oldRect = m_boxGO.getRectangle(); // for future comparison
        } // BoxGoRefreshTg

        public void refreshAfterDbUpdate(DbUpdateEvent ev) throws DbException {
            MetaField mf = ev.metaField;
            if (mf == DbSMSGraphicalObject.fStyle || mf == DbBEContextGo.fBackgroundColor
                    || mf == DbBEContextGo.fDashStyle || mf == DbBEContextGo.fHighlight
                    || mf == DbBEContextGo.fLineColor || mf == DbBEContextGo.fTextColor
                    || mf == DbBEContextGo.fComponents || mf == DbBEContextGo.fRectangle
                    || mf == DbBEContextCell.fX || mf == DbBEContextCell.fY
                    || mf == DbBEContextCell.fDescription || mf == DbBEContextCell.fFont
                    || mf == DbBEContextCell.fRightBorder || mf == DbBEContextCell.fBottomBorder
                    || mf == DbBEContextCell.fVerticalJustification
                    || mf == DbBEContextCell.fHorizontalJustification
                    || mf == DbBEContextCell.fXweight || mf == DbBEContextCell.fYweight) {
                m_context.initStyleElements();
                m_context.populateContents();
                refreshAction.doActionPerformed();
            } // end if
        } // end refreshAfterDbUpdate()

        public void beforeRefreshPass(Db db) throws DbException {
            int i = 0;
        }

        public void afterRefreshPass(Db db) throws DbException {
            int i = 0;
        }
    } // end BoxGoRefreshTg
} // end BEContext

