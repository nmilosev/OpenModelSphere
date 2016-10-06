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
import java.util.Vector;

import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.shape.FolderShape;
import org.modelsphere.jack.graphic.zone.CellRenderer;
import org.modelsphere.jack.graphic.zone.CellRenderingOption;
import org.modelsphere.jack.graphic.zone.CompositeCellRenderer;
import org.modelsphere.jack.graphic.zone.ImageCellRenderer;
import org.modelsphere.jack.graphic.zone.MatrixZone;
import org.modelsphere.jack.graphic.zone.SingletonZone;
import org.modelsphere.jack.graphic.zone.StringCellRenderer;
import org.modelsphere.jack.graphic.zone.ZoneCell;
import org.modelsphere.jack.graphic.zone.CompositeCellRenderer.CompositeCellRendererElement;
import org.modelsphere.jack.srtool.graphic.DbTextFieldCellEditor;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.db.DbSMSPackageGo;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSStyle;

public abstract class PackageBox extends GraphicNode implements ActionInformation {

    private static final String PACKAGE_STEREO_ZONE_ID = "Package Stereotype"; // NOT
    // LOCALIZABLE

    private SingletonZone nameZone = null;
    protected MatrixZone stereotypeZone = new MatrixZone(PACKAGE_STEREO_ZONE_ID,
            GraphicUtil.CENTER_ALIGNMENT);
    protected DbSMSPackageGo packageGO;
    protected DbSMSPackage packageSO;

    // rendering options
    private CellRenderingOption packageNameCRO = new CellRenderingOption(new StringCellRenderer(),
            GraphicUtil.CENTER_ALIGNMENT);
    protected CellRenderingOption stereotypeNameCRO;
    protected CellRenderingOption stereotypeImageCRO;

    // listeners
    private BoxRefreshTg boxRefreshTg = new BoxRefreshTg();
    private PackageGoRefreshTg packageGoRefreshTg = new PackageGoRefreshTg();

    public PackageBox(Diagram diag, DbSMSPackageGo newPackageGo) throws DbException {
        super(diag, FolderShape.singleton);
        packageGO = newPackageGo;
        packageSO = packageGO.getPackage();
        setAutoFit(packageGO.isAutoFit());
        setRectangle(packageGO.getRectangle());

        objectInit();
    }

    public SingletonZone getPackageNameZone() {
        return nameZone;
    }

    public DbSMSPackageGo getPackageGO() {
        return packageGO;
    }

    public DbSMSPackage getPackageSO() {
        return packageSO;
    }

    protected void objectInit() throws DbException {
        packageGO.setGraphicPeer(this);
        packageSO.addDbRefreshListener(boxRefreshTg, DbRefreshListener.CALL_ONCE);
        packageGO.addDbRefreshListener(packageGoRefreshTg);

        stereotypeZone.setHasBottomLine(false);
        addZone(stereotypeZone);

        nameZone = new SingletonZone("Name");
        addZone(nameZone);
        initCellRenderingOptions();
        populateContents();
    }

    private CellRenderingOption getCellRenderingOptionForConcept(CellRenderingOption cellRO,
            MetaField metaField, CellRenderer renderer, Color color) throws DbException {
        Font font = (Font) packageGO.find(metaField);
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

    // BEWARE: overriding methods must call super.delete as last action
    public void delete(boolean all) {
        packageGO.setGraphicPeer(null);
        packageSO.removeDbRefreshListener(boxRefreshTg);
        packageGO.removeDbRefreshListener(packageGoRefreshTg);

        super.delete(all);
    }

    private void initCellRenderingOptions() throws DbException {
        Font font = (Font) (packageGO.find(DbSMSStyle.fSms_packageNameFont));
        Color colorName = (Color) (packageGO.find(DbSMSPackageGo.fTextColor));

        packageNameCRO.setFont(font);
        packageNameCRO.setColor(colorName);

        Color colorText = (Color) (packageGO.find(DbSMSPackageGo.fTextColor));
        stereotypeNameCRO = getCellRenderingOptionForConcept(stereotypeNameCRO,
                DbSMSStyle.fSms_packageNameFont, null, colorText);
        stereotypeNameCRO.setAlignment(GraphicUtil.CENTER_ALIGNMENT);
        stereotypeImageCRO = getCellRenderingOptionForConcept(stereotypeImageCRO,
                DbSMSStyle.fSms_packageNameFont, new CompositeCellRenderer(), colorText);
        stereotypeImageCRO.setAlignment(GraphicUtil.RIGHT_ALIGNMENT);
    } // end initCellRenderingOptions()

    protected String getStereotypeName(DbSMSPackage pack) throws DbException {
        DbSMSStereotype stereotype = pack.getUmlStereotype();
        String stereotypeName = null;
        if (stereotype != null) {
            stereotypeName = "«" + stereotype.getName() + "»"; // NOT
            // LOCALIZABLE
        }

        return stereotypeName;
    }

    protected void populateContents() throws DbException {

        nameZone.setValue(new ZoneCell(packageSO, packageSO.getName(), packageNameCRO,
                new DbTextFieldCellEditor(DbSemanticalObject.fName, false)));

        // Populate stereotype
        populateStereotype();

        diagram.setComputePos(this);

        setFillColor((Color) (packageGO.find(DbSMSPackageGo.fBackgroundColor)));
        setLineColor((Color) (packageGO.find(DbSMSPackageGo.fLineColor)));
        setLineStyle((Boolean) (packageGO.find(DbSMSPackageGo.fHighlight)), (Boolean) (packageGO
                .find(DbSMSPackageGo.fDashStyle)));
    } // end populateContents()

    public MatrixZone getStereotypeZone() {
        return stereotypeZone;
    }

    protected abstract void populateStereotype() throws DbException;

    protected Vector getStereotypeImageData(DbSMSPackage packageSO) throws DbException {
        Vector compositeElements = new Vector();

        DbSMSStereotype stereotype = packageSO.getUmlStereotype();
        if (stereotype != null) {
            Image image = stereotype.getIcon();
            if (image != null) {
                compositeElements.addElement(new CompositeCellRendererElement(
                        new ImageCellRenderer(), image));
            }
        } // end if

        return compositeElements;
    } // end getStereotypeImageData()

    public final String getToolTipText() {
        return (String) nameZone.getValue().getCellData();
    }

    class BoxRefreshTg implements DbRefreshListener {
        // ///////////////////////////////////
        // DbRefreshListener SUPPORT
        //
        public void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
            // If package removed or moved to another package, the packageGo is
            // removed.
            if (packageGO.getTransStatus() != Db.OBJ_REMOVED)
                populateContents();
        }
        //
        // End of DbRefreshListener SUPPORT
        // ///////////////////////////////////
    }

    class PackageGoRefreshTg implements DbRefreshListener {
        // ///////////////////////////////////
        // DbRefreshListener SUPPORT
        //
        public void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
            refreshAfterDbUpdate(event.dbo, event.metaField);
        }

        private void refreshAfterDbUpdate(DbObject dbo, MetaField mf) throws DbException {

            if (DbSMSGraphicalObject.fStyle == mf || mf == DbSMSPackageGo.fBackgroundColor
                    || mf == DbSMSPackageGo.fDashStyle || mf == DbSMSPackageGo.fHighlight
                    || mf == DbSMSPackageGo.fLineColor || mf == DbSMSPackageGo.fTextColor) {
                initCellRenderingOptions();
                populateContents();
            }
        }
        //
        // End of DbRefreshListener SUPPORT
        // ///////////////////////////////////
    }

    public final DbObject getSemanticalObject() {
        return getPackageSO();
    }

    public final DbObject getGraphicalObject() {
        return getPackageGO();
    }

    public final Db getDb() {
        return getPackageSO().getDb();
    }
}
