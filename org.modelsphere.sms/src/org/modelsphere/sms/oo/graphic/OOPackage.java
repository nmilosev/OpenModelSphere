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

import java.util.Vector;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.graphic.zone.CellEditor;
import org.modelsphere.jack.graphic.zone.ColumnCellsOption;
import org.modelsphere.jack.graphic.zone.StereotypeCellEditor;
import org.modelsphere.jack.graphic.zone.ZoneCell;
import org.modelsphere.sms.db.DbSMSPackageGo;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSUmlExtensibility;
import org.modelsphere.sms.graphic.PackageBox;
import org.modelsphere.sms.graphic.SMSStereotypeEditor;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.db.DbOOPackage;
import org.modelsphere.sms.oo.db.DbOOStyle;
import org.modelsphere.sms.or.db.DbORTable;

public class OOPackage extends PackageBox {
    public OOPackage(org.modelsphere.jack.graphic.Diagram diag, DbSMSPackageGo newPackageGO)
            throws DbException {
        super(diag, newPackageGO);
    }

    protected void populateStereotype() throws DbException {
        String stereotypeName = getStereotypeName(packageSO);
        Vector composite = getStereotypeImageData(packageSO);
        DbOOStyle style = getStyleFrom(packageGO);
        stereotypeZone.clear();

        int stereotypeStyle = 0;
        Boolean b = (style == null) ? Boolean.TRUE : style.getOojv_umlStereotypeDisplayed();
        if ((b != null) && b.booleanValue()) {
            stereotypeZone.addColumn(new ColumnCellsOption(stereotypeNameCRO,
                    new SMSStereotypeEditor(DbOOPackage.fUmlStereotype)));
            stereotypeStyle += 1;
        }
        b = (style == null) ? Boolean.TRUE : style.getOojv_umlStereotypeIconDisplayed();
        if ((b != null) && b.booleanValue()) {
            stereotypeZone.addColumn(new ColumnCellsOption(stereotypeImageCRO,
                    new SMSStereotypeEditor(DbOOPackage.fUmlStereotype)));
            stereotypeStyle += 2;
        }

        stereotypeZone.addRow();

        switch (stereotypeStyle) {
        case 0:
            getStereotypeZone().setVisible(false);
            break;
        case 1:
            stereotypeZone.set(0, 0, new ZoneCell(packageSO, stereotypeName));
            getStereotypeZone().setVisible(true);
            break;
        case 2:
            stereotypeZone.set(0, 0, new ZoneCell(packageSO, composite));
            getStereotypeZone().setVisible(true);
            break;
        case 1 + 2:
            stereotypeZone.set(0, 0, new ZoneCell(packageSO, stereotypeName));
            stereotypeZone.set(0, 1, new ZoneCell(packageSO, composite));
            getStereotypeZone().setVisible(true);
            break;
        } // end switch
    } // end populateStereotype()

    private DbOOStyle getStyleFrom(DbSMSPackageGo packageGo) throws DbException {
        DbOOStyle style = (DbOOStyle) packageGo.getStyle();
        if (style == null) {
            DbOODiagram diag = (DbOODiagram) packageGO.getCompositeOfType(DbOODiagram.metaClass);
            style = (DbOOStyle) diag.getStyle();
        }

        return style;
    } // end getStyleFrom()

}
