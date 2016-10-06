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

package org.modelsphere.sms;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.LineLabel;
import org.modelsphere.jack.graphic.ZoneBox;
import org.modelsphere.jack.srtool.features.layout.GraphFactory;
import org.modelsphere.jack.srtool.graphic.*;
import org.modelsphere.jack.srtool.popupMenu.DbPopupMenu;
import org.modelsphere.jack.srtool.popupMenu.StampPopupMenu;
import org.modelsphere.jack.srtool.popupMenu.TextItemPopupMenu;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.features.layout.SMSGraphFactory;
import org.modelsphere.sms.popup.SMSPopupMenuPool;

public final class SMSModule extends Module {
    private static SMSModule singleton;

    static {
        singleton = new SMSModule();
    }

    private SMSModule() {
    }

    public static final SMSModule getSingleton() {
        return singleton;
    }

    public void initIntegrity() {
    }

    public void loadMeta() {
        ApplClasses.getFinalClasses();
    }

    public static void initAll() {
        GraphFactory.setDefault(new SMSGraphFactory());
        try {
            Class claz = Class.forName("org.modelsphere.sms.be.db.DbBEResource");
            Method method = claz.getMethod("initMeta", null);
            method.invoke(null, null);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.toString());
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.toString());
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.toString());
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.toString());
        } // end try
    } // end initAll()

    public void initMeta() {
        DbGraphic.fProjectDefaultStyle = DbSMSProject.fOoDefaultStyle; // Temporary
        DbGraphic.fStyleName = DbSMSStyle.fName;
        DbGraphic.fDiagramName = DbSMSDiagram.fName;
        DbGraphic.fDiagramPageFormat = DbSMSDiagram.fPageFormat;
        DbGraphic.fDiagramPrintScale = DbSMSDiagram.fPrintScale;
        DbGraphic.fDiagramNbPages = DbSMSDiagram.fNbPages;
        DbGraphic.fDiagramStyle = DbSMSDiagram.fStyle;
        DbGraphic.fDiagramPageNoFont = DbSMSDiagram.fPageNoFont;
        DbGraphic.fDiagramPageNoPsition = DbSMSDiagram.fPageNoPosition;
        DbGraphic.fGraphicalObjectRectangle = DbSMSGraphicalObject.fRectangle;
        DbGraphic.fGraphicalObjectAutoFit = DbSMSGraphicalObject.fAutoFit;
        DbGraphic.fGraphicalObjectFrontEndLineGos = DbSMSGraphicalObject.fFrontEndLineGos;
        DbGraphic.fGraphicalObjectBackEndLineGos = DbSMSGraphicalObject.fBackEndLineGos;
        DbGraphic.fGraphicalObjectStyle = DbSMSGraphicalObject.fStyle;
        DbGraphic.fLineGoPolyline = DbSMSLineGo.fPolyline;
        DbGraphic.fLineGoRightAngle = DbSMSLineGo.fRightAngle;
        DbGraphic.fLineGoFrontEndGo = DbSMSLineGo.fFrontEndGo;
        DbGraphic.fLineGoBackEndGo = DbSMSLineGo.fBackEndGo;
        DbGraphic.fFreeLineGoPolyline = DbSMSFreeLineGo.fPolyline;
        DbGraphic.fFreeLineGoRightAngle = DbSMSFreeLineGo.fRightAngle;
        DbGraphic.fUserTextGoText = DbSMSUserTextGo.fText;
        DbGraphic.fUserTextGoFont = DbSMSUserTextGo.fFont;
        DbGraphic.fUserTextGoFillColor = DbSMSUserTextGo.fBackgroundColor;
        DbGraphic.fUserTextGoTextColor = DbSMSUserTextGo.fTextColor;
        DbGraphic.fImageGoDiagramImage = DbSMSImageGo.fDiagramImage;
        DbGraphic.fImageGoOpacityFactor = DbSMSImageGo.fOpacityFactor;
        DbGraphic.fStampGoCompanyLogo = DbSMSStampGo.fCompanyLogo;
        DbGraphic.fFreeFormGoBackgroundColor = DbSMSFreeFormGo.fBackgroundColor;
        DbGraphic.fFreeGraphicGoLineColor = DbSMSFreeGraphicGo.fLineColor;
        DbGraphic.fFreeGraphicGoDashStyle = DbSMSFreeGraphicGo.fDashStyle;
        DbGraphic.fFreeGraphicGoHighlight = DbSMSFreeGraphicGo.fHighlight;
        DbGraphic.fDbDiagramName = DbSMSDiagram.fName;

        DbGraphic.installTriggers();
    }

    public Object[] getPopupMenuMapping() {
        return new Object[] {
                // semantical object
                DbSMSDiagram.class, SMSPopupMenuPool.smsDiagram, DbSMSLinkModel.class,
                SMSPopupMenuPool.smsLinkModel, DbSMSProject.class, SMSPopupMenuPool.smsProject,
                DbSMSSemanticalObject.class, SMSPopupMenuPool.smsSemanticalObject,
                DbSMSPackage.class, SMSPopupMenuPool.smsPackage, DbSMSUserDefinedPackage.class,
                SMSPopupMenuPool.smsUserDefinedPackage, DbSMSStereotype.class,
                SMSPopupMenuPool.smsUmlStereotype, DbSMSUmlConstraint.class,
                SMSPopupMenuPool.smsUmlConstraint, DbSMSUserTextGo.class,
                TextItemPopupMenu.actionsName, DbSMSUmlExtensibility.class,
                SMSPopupMenuPool.smsUmlExtensionsNode,
                DbSMSNotice.class,
                SMSPopupMenuPool.smsNotice,
                Db.class,
                DbPopupMenu.actionsName,

                // graphical object
                GraphicComponent.class, SMSPopupMenuPool.graphicComponent, Stamp.class,
                StampPopupMenu.actionsName, FreeText.class, TextItemPopupMenu.actionsName,
                DiagramImage.class, SMSPopupMenuPool.diagramImage, SrLine.class,
                SMSPopupMenuPool.srLine, LineLabel.class, SMSPopupMenuPool.lineLabel,
                ZoneBox.class, SMSPopupMenuPool.zoneBox };
    }

}
