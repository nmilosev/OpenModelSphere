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

package org.modelsphere.sms.oo.graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.zone.CellEditor;
import org.modelsphere.jack.graphic.zone.CellID;
import org.modelsphere.jack.graphic.zone.CellRenderingOption;
import org.modelsphere.jack.graphic.zone.ColumnCellsOption;
import org.modelsphere.jack.graphic.zone.ImageCellRenderer;
import org.modelsphere.jack.graphic.zone.MatrixZone;
import org.modelsphere.jack.graphic.zone.StringCellRenderer;
import org.modelsphere.jack.graphic.zone.ZoneCell;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DbTextAreaCellEditor;
import org.modelsphere.jack.srtool.graphic.DbTextFieldCellEditor;
import org.modelsphere.jack.srtool.graphic.Stamp;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.db.DbSMSStampGo;
import org.modelsphere.sms.graphic.ImageCellEditor;
import org.modelsphere.sms.oo.international.LocaleMgr;

public class OOStamp extends Stamp implements DbRefreshListener {

    private static String HEADER_ZONE_ID = "Stamp Header Zone"; // NOT
    // LOCALIZABLE,internal
    // code
    private static String MIDDLE_ZONE_ID = "Stamp Middle Zone"; // NOT
    // LOCALIZABLE,internal
    // code
    private static String END_ZONE_ID = "Stamp End Zone"; // NOT
    // LOCALIZABLE,internal
    // code
    private static String DESC_ZONE_ID = "Stamp Description Zone"; // NOT
    // LOCALIZABLE,internal
    // code
    private Font labelFont = new Font("SansSerif", Font.BOLD, 8); // NOT
    // LOCALIZABLE,internal
    // code
    private Font valueFont = new Font("SansSerif", Font.ITALIC, 8); // NOT
    // LOCALIZABLE,internal
    // code
    private MatrixZone headerZone;
    private MatrixZone middleZone;
    private MatrixZone endZone;
    private MatrixZone descriptionZone;
    private Image image = null;

    // Cell Rendering Option
    private CellRenderingOption CRO_Label = new CellRenderingOption(new StringCellRenderer(),
            labelFont, GraphicUtil.LEFT_ALIGNMENT, 4);

    private CellRenderingOption CRO_Value = new CellRenderingOption(new StringCellRenderer(),
            valueFont, GraphicUtil.LEFT_ALIGNMENT, 4);

    // cell position
    private static int imageRow = 0;
    private static int imageCol = 0;
    private static int projectRow = 0;
    private static int projectLabelCol = 1;
    private static int projectValueCol = 2;
    private static int packageRow = 0;
    private static int packageLabelCol = 0;
    private static int packageValueCol = 1;
    private static int packageVersionLabelCol = 2;
    private static int packageVersionValueCol = 3;
    private static int packageVersionRow = 0;
    private static int authorRow = 0;
    private static int authorValueCol = 1;
    private static int authorLabelCol = 0;
    private static int descriptionRow = 0;
    private static int descriptionValueCol = 1;
    private static int descriptionLabelCol = 0;

    private DbProject project;
    private DbSMSPackage thePackage;
    private DbSMSStampGo theStampGo;

    // called by SMSSelectTool.mouseReleased()
    public void mouseReleased(CellID cellID) {

    } // end mouseReleased()

    public OOStamp(Diagram diag, DbObject OOStampGo) throws DbException {
        super(diag, OOStampGo);

        CRO_Label.setCanReceiveFocus(false);
        theStampGo = (DbSMSStampGo) this.getGraphicalObject();
        buildHeaderZone();
        buildMiddleZone();
        buildEndZone();
        buildDescriptionZone();

        thePackage = (DbSMSPackage) theStampGo.getComposite().getComposite();
        project = thePackage.getProject();

        populateContents();

        thePackage.addDbRefreshListener(this);
        project.addDbRefreshListener(this);
        theStampGo.addDbRefreshListener(this);
    }

    private final void buildHeaderZone() {
        headerZone = new MatrixZone(HEADER_ZONE_ID, GraphicUtil.LEFT_ALIGNMENT);
        addZone(headerZone);
        headerZone.addColumn(new ColumnCellsOption(new CellRenderingOption(new ImageCellRenderer(),
                GraphicUtil.CENTER_ALIGNMENT), null));
        headerZone.addColumn(new ColumnCellsOption(CRO_Label, null, false));
        headerZone.addColumn(new ColumnCellsOption(CRO_Value, null, false));
        headerZone.addRow();
        headerZone.set(projectRow, projectLabelCol, new ZoneCell(null, LocaleMgr.screen
                .getString("StampProject")));

    }

    private final void buildMiddleZone() {
        middleZone = new MatrixZone(MIDDLE_ZONE_ID, GraphicUtil.LEFT_ALIGNMENT);
        addZone(middleZone);
        middleZone.addColumn(new ColumnCellsOption(CRO_Label, null, false));
        middleZone.addColumn(new ColumnCellsOption(CRO_Value, null, false));
        middleZone.addColumn(new ColumnCellsOption(CRO_Label, null, true));
        middleZone.addColumn(new ColumnCellsOption(CRO_Value, null, false));
        middleZone.addRow();

        ZoneCell packageZoneCell = new ZoneCell(null, LocaleMgr.screen.getString("StampPackage"));
        middleZone.set(packageRow, packageLabelCol, packageZoneCell);
        packageZoneCell.setCellEditor(new DbTextFieldCellEditor(DbSMSPackage.fName, true));

        ZoneCell versionZoneCell = new ZoneCell(null, LocaleMgr.screen.getString("StampPackageVer"));
        middleZone.set(packageVersionRow, packageVersionLabelCol, versionZoneCell);
        versionZoneCell.setCellEditor(new DbTextFieldCellEditor(DbSMSPackage.fVersion, true));

    }

    private final void buildEndZone() {
        endZone = new MatrixZone(END_ZONE_ID, GraphicUtil.LEFT_ALIGNMENT);
        addZone(endZone);
        endZone.addColumn(new ColumnCellsOption(CRO_Label, null, false));
        endZone.addColumn(new ColumnCellsOption(CRO_Value, null, false));
        endZone.addRow();
        endZone.set(authorRow, authorLabelCol, new ZoneCell(null, LocaleMgr.screen
                .getString("StampPackageAuthor")));
    }

    private final void buildDescriptionZone() {
        descriptionZone = new MatrixZone(DESC_ZONE_ID, GraphicUtil.LEFT_ALIGNMENT);
        addZone(descriptionZone);
        descriptionZone.setHasBottomLine(false);
        CellEditor descriptionCellEditor = new DbTextAreaCellEditor(LocaleMgr.action
                .getString("StampName"), DbSMSStampGo.fDescription);
        descriptionZone.addColumn(new ColumnCellsOption(CRO_Label, null, false));
        descriptionZone.addColumn(new ColumnCellsOption(CRO_Value, descriptionCellEditor, false));
        descriptionZone.addRow();
        descriptionZone.set(descriptionRow, descriptionLabelCol, new ZoneCell(theStampGo,
                LocaleMgr.screen.getString("StampDescription")));
    }

    public final void delete(boolean all) {
        theStampGo.removeDbRefreshListener(this);
        project.removeDbRefreshListener(this);
        thePackage.removeDbRefreshListener(this);
        super.delete(all);
    }

    private void populateContents() throws DbException {
        // Style
        setLineColor((Color) theStampGo.find(DbSMSStampGo.fLineColor));
        setLineStyle((Boolean) theStampGo.find(DbSMSStampGo.fHighlight), (Boolean) theStampGo
                .find(DbSMSStampGo.fDashStyle));
        setFillColor((Color) theStampGo.find(DbSMSStampGo.fBackgroundColor));
        CRO_Label.setColor((Color) theStampGo.find(DbSMSStampGo.fTextColor));
        CRO_Value.setColor((Color) theStampGo.find(DbSMSStampGo.fTextColor));

        // header Zone
        headerZone.removeAllRows();
        headerZone.addRow();

        headerZone.set(projectRow, projectLabelCol, new ZoneCell(null, LocaleMgr.screen
                .getString("StampProject")));
        ZoneCell projectZoneCell = new ZoneCell(project, project.getName());
        headerZone.set(projectRow, projectValueCol, projectZoneCell);
        projectZoneCell.setCellEditor(new DbTextFieldCellEditor(DbProject.fName, true));

        image = ((DbSMSStampGo) this.getGraphicalObject()).getCompanyLogo();

        ZoneCell imageZoneCell = new ZoneCell(null, image);
        headerZone.set(imageRow, imageCol, imageZoneCell);
        imageZoneCell.setCellEditor(new ImageCellEditor());

        // Middle Zone
        middleZone.removeAllRows();
        middleZone.addRow();
        middleZone.set(packageRow, packageLabelCol, new ZoneCell(null, LocaleMgr.screen
                .getString("StampPackage")));

        ZoneCell middleZoneCell = new ZoneCell(thePackage, thePackage
                .getSemanticalName(DbObject.SHORT_FORM));
        middleZone.set(packageRow, packageValueCol, middleZoneCell);
        middleZoneCell.setCellEditor(new DbTextFieldCellEditor(DbSMSPackage.fName, true));

        middleZone.set(packageVersionRow, packageVersionLabelCol, new ZoneCell(null,
                LocaleMgr.screen.getString("StampPackageVer")));
        String version = thePackage.getVersion();
        ZoneCell versionZoneCell = new ZoneCell(thePackage, (version == null ? "" : version));
        middleZone.set(packageVersionRow, packageVersionValueCol, versionZoneCell);
        versionZoneCell.setCellEditor(new DbTextFieldCellEditor(DbSMSPackage.fVersion, true));

        // End Zone
        endZone.removeAllRows();
        endZone.addRow();
        endZone.set(authorRow, authorLabelCol, new ZoneCell(null, LocaleMgr.screen
                .getString("StampPackageAuthor")));
        ZoneCell authorZoneCell = new ZoneCell(thePackage,
                (((DbSMSPackage) thePackage).getAuthor() == null ? "" : ((DbSMSPackage) thePackage)
                        .getAuthor()));
        endZone.set(authorRow, authorValueCol, authorZoneCell);
        authorZoneCell.setCellEditor(new DbTextFieldCellEditor(DbSMSPackage.fAuthor, true));

        // Description Zone
        descriptionZone.removeAllRows();
        descriptionZone.addRow();
        descriptionZone.set(descriptionRow, descriptionLabelCol, new ZoneCell(theStampGo,
                LocaleMgr.screen.getString("StampDescription")));
        descriptionZone.set(descriptionRow, descriptionValueCol, new ZoneCell(theStampGo,
                (theStampGo.getDescription() == null ? "" : theStampGo.getDescription())));

        diagram.setComputePos(this);
    }

    public final void refreshAfterDbUpdate(DbUpdateEvent e) throws DbException {
        if (DbSemanticalObject.fName == e.metaField || DbSMSPackage.fAuthor == e.metaField
                || DbSMSPackage.fVersion == e.metaField || DbSMSStampGo.fCompanyLogo == e.metaField
                || DbSMSStampGo.fDescription == e.metaField
                || DbSMSStampGo.fLineColor == e.metaField
                || DbSMSStampGo.fBackgroundColor == e.metaField
                || DbSMSStampGo.fHighlight == e.metaField || DbSMSStampGo.fDashStyle == e.metaField
                || DbSMSStampGo.fTextColor == e.metaField) {

            populateContents();
        }

    }

}
