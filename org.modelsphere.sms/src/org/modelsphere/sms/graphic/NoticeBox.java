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
package org.modelsphere.sms.graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.awt.choosers.ExternalDocumentDialog;
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
import org.modelsphere.jack.graphic.ExtZoneBox;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.zone.CellEditor;
import org.modelsphere.jack.graphic.zone.CellID;
import org.modelsphere.jack.graphic.zone.CellRenderer;
import org.modelsphere.jack.graphic.zone.CellRenderingOption;
import org.modelsphere.jack.graphic.zone.ColumnCellsOption;
import org.modelsphere.jack.graphic.zone.CompositeCellRenderer;
import org.modelsphere.jack.graphic.zone.ImageCellRenderer;
import org.modelsphere.jack.graphic.zone.MatrixZone;
import org.modelsphere.jack.graphic.zone.SingletonZone;
import org.modelsphere.jack.graphic.zone.StereotypeCellEditor;
import org.modelsphere.jack.graphic.zone.StringCellRenderer;
import org.modelsphere.jack.graphic.zone.Zone;
import org.modelsphere.jack.graphic.zone.ZoneCell;
import org.modelsphere.jack.graphic.zone.CompositeCellRenderer.CompositeCellRendererElement;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DbTextAreaCellEditor;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.SMSHorizontalAlignment;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOOStyle;
import org.modelsphere.sms.or.db.DbORPackage;
import org.modelsphere.sms.or.db.DbORStyle;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class NoticeBox extends GraphicNode implements ActionInformation, DbRefreshListener {
    private static final String STEREO_ZONE_ID = "Note Stereotype"; //NOT LOCALIZABLE, property key
    private static final String NAME_ZONE_ID = "Note Name"; //NOT LOCALIZABLE
    private static final String EXTERNAL_DOC_ZONE_ID = "External Document"; //NOT LOCALIZABLE

    private DbSMSNoticeGo m_noticeGo;
    private DbSMSNotice m_notice;

    //the zones
    private transient MatrixZone m_stereotypeZone = new MatrixZone(STEREO_ZONE_ID,
            GraphicUtil.CENTER_ALIGNMENT);
    private transient MatrixZone m_nameZone = new MatrixZone(NAME_ZONE_ID,
            GraphicUtil.LEFT_ALIGNMENT);
    private transient MatrixZone m_documentZone = new MatrixZone(EXTERNAL_DOC_ZONE_ID,
            GraphicUtil.LEFT_ALIGNMENT);
    private SingletonZone m_descZone = null;

    //rendering options
    private transient CellRenderingOption stereotypeNameCRO;
    private transient CellRenderingOption stereotypeImageCRO;
    private transient CellRenderingOption descCRO;
    private transient CellRenderingOption externalNameCRO;
    private transient CellRenderingOption externalImageCRO;
    private transient CellRenderingOption nameCRO;
    private transient CellRenderingOption duplicateCRO = new CellRenderingOption(
            new StringCellRenderer(), new Font("SansSerif", Font.PLAIN, 8), //NOT LOCALIZABLE
            GraphicUtil.LEFT_ALIGNMENT);

    static {
        NoticeRefreshTg noticeRefreshTg = new NoticeRefreshTg();
        Db.addDbRefreshPassListener(noticeRefreshTg);
        MetaField.addDbRefreshListener(noticeRefreshTg, (DbProject) null, new MetaField[] {
                DbObject.fComponents, DbSMSNotice.fNoticeGos, DbSMSNotice.fUmlStereotype,
                DbSMSNotice.fName, DbSMSNotice.fDescription, DbSMSNotice.fAlignment,
                DbSMSNotice.fExternalDocument });
    }

    public NoticeBox(Diagram diag, DbSMSNoticeGo go) throws DbException {
        super(diag, NoticeShape.singleton);

        m_noticeGo = go;
        m_notice = go.getNotice();
        Rectangle rect = go.getRectangle();
        setRectangle(rect);

        if (m_noticeGo.isAutoFit())
            setAutoFitMode(HEIGHT_FIT);
        else
            setAutoFitMode(NO_FIT);
        objectInit();
    } //end NoticeBox

    // BEWARE: overriding methods must call super.delete as last action
    public void delete(boolean all) {
        m_noticeGo.setGraphicPeer(null);
        m_noticeGo.removeDbRefreshListener(this);
        //m_notice.removeDbRefreshListener(noticeRefreshTg);
        //m_noticeGo.removeDbRefreshListener(packageGoRefreshTg);

        super.delete(all);
    }

    // overridden
    public int getDefaultFitMode() {
        return GraphicComponent.HEIGHT_FIT;
    }

    //
    // Implements ActionInformation
    //

    public Db getDb() {
        return getSemanticalObject().getDb();
    }

    public DbObject getSemanticalObject() {
        return m_notice;
    }

    public DbObject getGraphicalObject() {
        return m_noticeGo;
    }

    //
    // Other public methods
    //

    //called by SMSSelectTool.mouseReleased()
    public void mouseReleased(CellID cellID) {
        if (cellID != null) {
            try {
                Zone zone = cellID.zone;
                String nameID = zone.getNameID();
                //If click on external document cell, open external document
                if (nameID.equals(EXTERNAL_DOC_ZONE_ID)) {
                    DbSMSNotice notice = (DbSMSNotice) this.getSemanticalObject();
                    notice.getDb().beginReadTrans();
                    String externalDoc = notice.getExternalDocument();
                    ExternalDocumentDialog.invoke(externalDoc);
                    notice.getDb().commitTrans();
                } //end if
                /*
                 * else if (nameID.equals(STEREO_ZONE_ID)) { try { ApplicationDiagram diag =
                 * ApplicationContext.getFocusManager().getActiveDiagram();
                 * diag.setEditor(diag.getMainView(), m_stereotypeZone.getBox(),
                 * m_stereotypeZone.cellAt(0, 0, 0)); } catch(Exception e){/*do nothing
                 *///}
                //}
            } catch (DbException ex) {
                //do nothing
            }
        } //end if
        else {
            try {
                ApplicationDiagram diag = ApplicationContext.getFocusManager().getActiveDiagram();
                diag.setEditor(diag.getMainView(), m_descZone.getBox(), m_descZone.cellAt(0, 0, 0));
            } catch (Exception e) {/* do nothing */
            }
        }

    } //end mouseReleased()

    //
    // private & protected methods
    //   
    private void objectInit() throws DbException {
        m_noticeGo.setGraphicPeer(this);
        m_noticeGo.addDbRefreshListener(this);
        refresh();
        initCellRenderingOptions();
        setZones();
        populateContents();
    } //end objectInit()

    private void refresh() throws DbException {
        setFillColor((Color) m_noticeGo.find(DbSMSNoticeGo.fBackgroundColor));
        setLineColor((Color) m_noticeGo.find(DbSMSNoticeGo.fLineColor));
        setTextColor((Color) m_noticeGo.find(DbSMSNoticeGo.fTextColor));
        Boolean highlight = (Boolean) m_noticeGo.find(DbSMSNoticeGo.fHighlight);
        if (highlight == null) {
            highlight = Boolean.FALSE;
        }
        Boolean dashStyle = (Boolean) m_noticeGo.find(DbSMSNoticeGo.fDashStyle);
        if (dashStyle == null) {
            dashStyle = Boolean.TRUE;
        }
        setLineStyle(highlight, dashStyle);
    }

    private void initCellRenderingOptions() throws DbException {
        Font stereotypeFont = new Font("SansSerif", Font.PLAIN, 8); //NOT LOCALIZABLE, font name
        Font nameFont = new Font("SansSerif", Font.BOLD, 8); //NOT LOCALIZABLE, font name
        Color textColor;
        try {
            textColor = (Color) (m_noticeGo.find(DbSMSNoticeGo.fTextColor));
        } catch (DbException ex) {
            textColor = Color.BLACK;
        } //end try

        //stereotype
        MetaField boxDescriptorFont = DbORStyle.fOr_viewDescriptorFont;
        stereotypeNameCRO = getCellRenderingOptionForConcept(stereotypeNameCRO,
                new StringCellRenderer(), stereotypeFont, textColor);
        stereotypeNameCRO.setAlignment(GraphicUtil.CENTER_ALIGNMENT);
        stereotypeImageCRO = getCellRenderingOptionForConcept(stereotypeImageCRO,
                new CompositeCellRenderer(), stereotypeFont, textColor);
        stereotypeImageCRO.setAlignment(GraphicUtil.RIGHT_ALIGNMENT);

        //name
        nameCRO = getCellRenderingOptionForConcept(nameCRO, new StringCellRenderer(), nameFont,
                textColor);
        nameCRO.setAlignment(GraphicUtil.LEFT_ALIGNMENT);

        //deccription
        descCRO = new CellRenderingOption(new StringCellRenderer(false, true),
                GraphicUtil.RIGHT_ALIGNMENT);

        //external document
        Font externalNameFont = new Font("Arial", Font.ITALIC, 8);
        externalNameCRO = getCellRenderingOptionForConcept(externalNameCRO,
                new StringCellRenderer(), externalNameFont, textColor);
        externalNameCRO.setAlignment(GraphicUtil.LEFT_ALIGNMENT);
        externalImageCRO = getCellRenderingOptionForConcept(externalImageCRO,
                new CompositeCellRenderer(), null, textColor);
        externalImageCRO.setAlignment(GraphicUtil.LEFT_ALIGNMENT);

        stereotypeNameCRO.setCanReceiveFocus(true);
        stereotypeImageCRO.setCanReceiveFocus(true);
        descCRO.setCanReceiveFocus(false);
        externalNameCRO.setCanReceiveFocus(true);
        externalImageCRO.setCanReceiveFocus(true);
        nameCRO.setCanReceiveFocus(false);
        duplicateCRO.setCanReceiveFocus(false);

    } //end initCellRenderingOptions()

    private void setZones() throws DbException {

        //add name and duplicate in the second compartment 
        m_nameZone.addColumn(new ColumnCellsOption(descCRO, null)); //editor set in populateName()
        m_nameZone.addColumn(new ColumnCellsOption(duplicateCRO, null));
        m_nameZone.setHasBottomLine(false);

        addZone(m_nameZone);

        //add stereotype in the first compartment 
        //m_stereotypeZone.addColumn(new ColumnCellsOption(stereotypeNameCRO, null));
        m_stereotypeZone.addColumn(new ColumnCellsOption(stereotypeNameCRO, (CellEditor) null));
        m_stereotypeZone.addColumn(new ColumnCellsOption(stereotypeImageCRO, (CellEditor) null));
        m_stereotypeZone.setHasBottomLine(false);
        addZone(m_stereotypeZone);

        //add description in the third compartment  
        m_descZone = new SingletonZone("");
        addZone(m_descZone);

        //add external doc in the fourth compartment
        m_documentZone.setHasBottomLine(false);
        addZone(m_documentZone);
    } //end setZones()

    private void populateContents() throws DbException {
        //populate stereotype
    	if(ScreenPerspective.isFullVersion())
    		populateStereotype();

        //populate name
        populateName();

        //get content alignment
        SMSHorizontalAlignment alignment = m_notice.getAlignment();
        int align = GraphicUtil.LEFT_ALIGNMENT; //default alignment
        if (alignment != null) {
            switch (alignment.getValue()) {
            case SMSHorizontalAlignment.LEFT:
                align = GraphicUtil.LEFT_ALIGNMENT;
                break;
            case SMSHorizontalAlignment.CENTER:
                align = GraphicUtil.CENTER_ALIGNMENT;
                break;
            case SMSHorizontalAlignment.RIGHT:
                align = GraphicUtil.RIGHT_ALIGNMENT;
                break;
            } //end switch
        } //end if

        //create compartments
        descCRO = new CellRenderingOption(new StringCellRenderer(false, true), align);
        descCRO.setCanReceiveFocus(false);
        CellEditor editor = new DbTextAreaCellEditor(DbSemanticalObject.fDescription);
        ZoneCell cell = new ZoneCell(m_notice, m_notice.getDescription(), descCRO, editor);
        m_descZone.setValue(cell);

        //populate external document
        populateExternalDoc();

        m_descZone.setHasBottomLine(false);
        diagram.setComputePos(this);
    } //end populateContents()

    public void enterEditMode(Diagram diag, DiagramView view) throws DbException {

        m_notice.getDb().beginReadTrans();

        SMSHorizontalAlignment alignment = m_notice.getAlignment();
        String description = m_notice.getDescription();

        m_notice.getDb().commitTrans();

        int align = GraphicUtil.LEFT_ALIGNMENT; //default alignment
        if (alignment != null) {
            switch (alignment.getValue()) {
            case SMSHorizontalAlignment.LEFT:
                align = GraphicUtil.LEFT_ALIGNMENT;
                break;
            case SMSHorizontalAlignment.CENTER:
                align = GraphicUtil.CENTER_ALIGNMENT;
                break;
            case SMSHorizontalAlignment.RIGHT:
                align = GraphicUtil.RIGHT_ALIGNMENT;
                break;
            } //end switch
        } //end if

        //create compartments
        descCRO = new CellRenderingOption(new StringCellRenderer(false, true), align);
        descCRO.setCanReceiveFocus(false);
        CellEditor editor = new DbTextAreaCellEditor(DbSemanticalObject.fDescription);
        ZoneCell cell = new ZoneCell(m_notice, description, descCRO, editor);
        m_descZone.setValue(cell);

        ((ApplicationDiagram) diag)
                .setEditor(view, m_descZone.getBox(), m_descZone.cellAt(0, 0, 0));

    }

    //must be within a read transaction
    private void populateStereotype() throws DbException {
        //clear zone
        m_stereotypeZone.clear();

        //is stereotype displayed according the current style?
        boolean stereotypeDisplayed = true;
        DbSMSStyle style = getStyleFrom(m_noticeGo);
        if (style != null) {
            if (style instanceof DbORStyle) {
                stereotypeDisplayed = ((DbORStyle) style).getOr_umlStereotypeDisplayed()
                        .booleanValue();
            } else if (style instanceof DbOOStyle) {
                stereotypeDisplayed = ((DbOOStyle) style).getOojv_umlStereotypeDisplayed()
                        .booleanValue();
            }
        } //end if

        if (!stereotypeDisplayed)
            return;

        String stereotypeName = getTableStereotypeName(m_notice);
        Vector composite = getStereotypeImageData(m_notice);

        int stereotypeStyle = 0;
        m_stereotypeZone.addColumn(new ColumnCellsOption(stereotypeNameCRO,
                new SMSStereotypeEditor(DbSMSNotice.fUmlStereotype)));
        stereotypeStyle += 1;

        m_stereotypeZone.addColumn(new ColumnCellsOption(stereotypeImageCRO,
                new SMSStereotypeEditor(DbSMSNotice.fUmlStereotype)));
        stereotypeStyle += 2;

        if (stereotypeName == null)
            stereotypeStyle = 0;

        m_stereotypeZone.addRow();

        switch (stereotypeStyle) {
        case 0:
            m_stereotypeZone.setVisible(false);
            break;
        case 1:
            m_stereotypeZone.set(0, 0, new ZoneCell(m_notice, stereotypeName));
            m_stereotypeZone.setVisible(true);
            break;
        case 2:
            m_stereotypeZone.set(0, 0, new ZoneCell(m_notice, composite));
            m_stereotypeZone.setVisible(true);
            break;
        case 1 + 2:
            m_stereotypeZone.set(0, 0, new ZoneCell(m_notice, stereotypeName));
            m_stereotypeZone.set(0, 1, new ZoneCell(m_notice, composite));
            m_stereotypeZone.setVisible(true);
            break;
        } //end switch

    } //end populateStereotype()

    private Vector getStereotypeImageData(DbSMSNotice notice) throws DbException {
        Vector compositeElements = new Vector();

        DbSMSStereotype stereotype = notice.getUmlStereotype();
        if (stereotype != null) {
            Image image = stereotype.getIcon();
            if (image != null) {
                compositeElements.addElement(new CompositeCellRendererElement(
                        new ImageCellRenderer(), image));
            }
        } //end if

        return compositeElements;
    } //end getStereotypeImageData()

    private void populateName() throws DbException {
        m_nameZone.clear();
        //m_nameZone.addColumn(new ColumnCellsOption(nameCRO, null));
        m_nameZone.addColumn(new ColumnCellsOption(duplicateCRO, null));

        m_nameZone.addRow();
        String name = m_notice.getName();
        //m_nameZone.set(0, 0, new ZoneCell(m_notice, name));
        //m_nameZone.set(0, 1, new ZoneCell(m_notice, calculateDuplicate(m_notice.getNoticeGos(), m_noticeGo)));
        m_nameZone.set(0, 0, new ZoneCell(m_notice, calculateDuplicate(m_notice.getNoticeGos(),
                m_noticeGo)));
        m_nameZone.setVisible(true);
    } //end populateName()

    private void populateExternalDoc() throws DbException {
        m_documentZone.clear();

        //add row with two columns
        m_documentZone.addColumn(new ColumnCellsOption(externalImageCRO, (CellEditor) null));
        m_documentZone.addColumn(new ColumnCellsOption(externalNameCRO, (CellEditor) null));
        m_documentZone.addRow();

        String externalDoc = m_notice.getExternalDocument();
        if (externalDoc != null) {
            String pathname = getPath(externalDoc);
            File file = new File(pathname);
            String filename = file.getName();
            Vector composite = getDocumentImageData(m_notice, file);
            m_documentZone.set(0, 0, new ZoneCell(m_notice, composite));
            m_documentZone.set(0, 1, new ZoneCell(m_notice, filename));
            m_documentZone.setVisible(true);
        } else {
            m_documentZone.setVisible(false);
        } //end if
    } //end populateExternalDoc()

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

    //TODO : parse 'cmd /c ""'
    private String getPath(String externalDoc) {
        int idx1 = externalDoc.indexOf('\"');
        int idx2 = externalDoc.lastIndexOf('\"');

        String filename = externalDoc.substring(idx1 + 1, idx2);
        return filename;
    } //end getActualName()

    protected Vector getDocumentImageData(DbSMSNotice notice, File file) throws DbException {
        Vector compositeElements = new Vector();

        ImageIcon icon;
        Icon systemIcon = FileSystemView.getFileSystemView().getSystemIcon(file);
        if ((systemIcon != null) && (systemIcon instanceof ImageIcon)) {
            icon = (ImageIcon) systemIcon;
        } else {
            icon = getImageIcon("leafnode.gif"); //NOT LOCALIZABLE, file name 
        } //end if

        Image image = icon.getImage();
        if (image != null) {
            compositeElements.addElement(new CompositeCellRendererElement(new ImageCellRenderer(),
                    image));
        }

        return compositeElements;
    } //end getStereotypeImageData()

    private DbSMSStyle getStyleFrom(DbSMSNoticeGo noticeGo) throws DbException {
        DbSMSStyle style = (DbSMSStyle) noticeGo.getStyle();
        if (style == null) {
            DbSMSDiagram diag = (DbSMSDiagram) noticeGo.getCompositeOfType(DbSMSDiagram.metaClass);
            style = (DbSMSStyle) diag.getStyle();
        }

        return style;
    } //end getStyleFrom()

    private static ImageIcon getImageIcon(String name) {
        ImageIcon icon = null;
        //Class claz = Extensibility.class;
        URL url = LocaleMgr.class.getResource("resources/" + name); //NOT LOCALIZABLE, folder name
        if (url != null) {
            icon = new ImageIcon(url);
        }

        return icon;
    } //end getImageIcon()

    private CellRenderingOption getCellRenderingOptionForConcept(CellRenderingOption cellRO,
            CellRenderer renderer, Font font, Color color) {
        //Font font = new Font("Arial", Font.ITALIC, 12);

        if (cellRO == null) {
            CellRenderingOption cellOption = new CellRenderingOption(renderer != null ? renderer
                    : new StringCellRenderer(false, true), font, GraphicUtil.LEFT_ALIGNMENT);
            cellOption.setColor(color);
            return cellOption;
        } else {
            cellRO.setFont(font);
            cellRO.setColor(color);
            return cellRO;
        } //end if
    } //end  getCellRenderingOptionForConcept()  

    private String getTableStereotypeName(DbSMSNotice notice) throws DbException {
        String stereotypeName = null;

        //MetaField mf = DbORStyle.fOr_umlStereotypeDisplayed;
        //Boolean b = (Boolean)tableGO.find(mf);
        //if ((b != null) && b.booleanValue()) {
        stereotypeName = getUMLStereotypeName(notice);
        //} //end if

        return stereotypeName;
    } //end getStereotype()

    private String getUMLStereotypeName(DbSMSSemanticalObject semObject) throws DbException {
        String stereotypeName = null;
        DbSMSStereotype stereotype = semObject.getUmlStereotype();
        if (stereotype != null) {
            stereotypeName = "«" + stereotype.getName() + "»"; //NOT LOCALIZABLE
        }

        return stereotypeName;
    } //end getUMLStereotypeName()

    /*
     * Unique listener listening to any change in the semantical part of the database that affect
     * the data needed to draw an BEContext.
     */
    private static class NoticeRefreshTg implements DbRefreshListener, DbRefreshPassListener {

        public void beforeRefreshPass(Db db) throws DbException {
        }

        public void afterRefreshPass(Db db) throws DbException {
            int i = 0;
        }

        public void refreshAfterDbUpdate(DbUpdateEvent e) throws DbException {
            DbObject dbo = e.dbo;

            if (dbo instanceof DbSMSNotice) {
                refreshBox((DbSMSNotice) dbo);
            }

        } //end refreshAfterDbUpdate()

        private void refreshBox(DbSMSNotice notice) throws DbException {
            DbRelationN noticeGos = notice.getNoticeGos();
            int nb = noticeGos.size();
            for (int i = 0; i < nb; i++) {
                DbObject element = noticeGos.elementAt(i);
                if (element instanceof DbSMSNoticeGo) {
                    DbSMSNoticeGo noticeGo = (DbSMSNoticeGo) element;
                    NoticeBox noticeBox = (NoticeBox) noticeGo.getGraphicPeer();
                    if (noticeBox != null) {
                        noticeBox.populateContents();

                        if (noticeBox.isAutoFit())
                            noticeBox.setAutoFitMode(HEIGHT_FIT);
                        else
                            noticeBox.setAutoFitMode(NO_FIT);
                    }

                }
            } //end for
        } //end refreshBox
    } //end NoticeRefreshTg

    @Override
    public void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
        if (DbSMSGraphicalObject.fStyle == event.metaField
                || event.metaField == DbSMSNoticeGo.fDashStyle
                || event.metaField == DbSMSNoticeGo.fHighlight
                || event.metaField == DbSMSNoticeGo.fLineColor
                || event.metaField == DbSMSNoticeGo.fBackgroundColor
                || event.metaField == DbSMSNoticeGo.fTextColor) {
            refresh();
        }
    }

} //end NoticeBox
