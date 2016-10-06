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

package org.modelsphere.sms.oo.java.graphic.tool;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.tool.ButtonSelectionPanelTool;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSUmlExtensibility;
import org.modelsphere.sms.db.util.Extensibility;
import org.modelsphere.sms.oo.java.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOOAdtGo;
import org.modelsphere.sms.oo.java.JavaModule;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.srtypes.JVClassCategory;

public class JVOtherClassifierTool extends ButtonSelectionPanelTool {

    private static final String kShapeTool = LocaleMgr.screen.getString("ClassifierCreationTool"); //NOT LOCALIZABLE

    private static final int EXCEPTION = 0;
    private static final String kExceptionCreationTool = LocaleMgr.screen
            .getString("ExceptionCreationTool");
    private static final Image kImageExceptionCreationTool = GraphicUtil.loadImage(
            JavaModule.class, "db/resources/dbjvexception.gif"); // NOT LOCALIZABLE - tool image

    private static final int ASSOCIATION = 1;
    private static final String kAssociationCreationTool = LocaleMgr.screen
            .getString("ClassAssociationCreationTool");
    private static final Image kImageAssociationCreationTool = GraphicUtil.loadImage(
            JavaModule.class, "db/resources/dbjvassociation.gif"); // NOT LOCALIZABLE - tool image
    private static final String kAssociationName = LocaleMgr.screen.getString("Association");

    private static final int ENUMERATION = 2;
    private static final String kEnumerationCreationTool = LocaleMgr.screen
            .getString("EnumerationCreationTool");
    private static final Image kImageEnumerationCreationTool = GraphicUtil.loadImage(
            JavaModule.class, "db/resources/dbjvenumeration.gif"); // NOT LOCALIZABLE - tool image
    private static final String kEnumerationName = LocaleMgr.screen.getString("Enumeration");

    private static final int UTILITY = 3;
    private static final String kUtilityCreationTool = LocaleMgr.screen
            .getString("UtilityClassCreationTool");
    private static final Image kImageUtilityCreationTool = GraphicUtil.loadImage(JavaModule.class,
            "db/resources/dbjvutilityclass.gif"); // NOT LOCALIZABLE - tool image
    private static final String kUtilityName = LocaleMgr.screen.getString("Utility");

    private static final String[] CREATION_TOOLS = { kExceptionCreationTool,
            kAssociationCreationTool, kEnumerationCreationTool, kUtilityCreationTool };

    public JVOtherClassifierTool() {
        super(0, kShapeTool, new String[] { kExceptionCreationTool, kAssociationCreationTool,
                kEnumerationCreationTool, kUtilityCreationTool }, kImageExceptionCreationTool,
                new Image[] { kImageExceptionCreationTool, kImageAssociationCreationTool,
                        kImageEnumerationCreationTool, kImageUtilityCreationTool }, 0);
    }

    protected Cursor loadDefaultCursor() {
        return AwtUtil.createCursor(GraphicUtil.loadImage(JavaModule.class,
                "db/resources/dbjvexception.gif"), new Point(8, 8), "box", true); //NOT LOCALIZABLE, not yet
    }

    public final void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (model.getDrawingArea().contains(x, y))
            createBox(x, y);
        view.toolCompleted();
    }

    protected final void createBox(int x, int y) {
        try {
            //start transaction
            DbSMSDiagram diagGO = (DbSMSDiagram) ((ApplicationDiagram) model).getDiagramGO();
            int category = getAuxiliaryIndex();
            String transName = CREATION_TOOLS[category];
            diagGO.getDb().beginTrans(Db.WRITE_TRANS, transName);

            //create classifier
            switch (category) {
            case EXCEPTION:
                createException(x, y, diagGO);
                break;
            case ASSOCIATION:
                createAssociation(x, y, diagGO);
                break;
            case ENUMERATION:
                createEnumeration(x, y, diagGO);
                break;
            case UTILITY:
                createUtility(x, y, diagGO);
                break;
            }

            //commit transaction
            diagGO.getDb().commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(view, ex);
        }
    } //end createBox()

    private void createException(int x, int y, DbSMSDiagram diagGO) throws DbException {
        int category = JVClassCategory.EXCEPTION;
        DbJVClass adtSO = new DbJVClass(diagGO.getComposite(), JVClassCategory
                .getInstance(category));
        DbSMSClassifierGo adtGO = new DbOOAdtGo(diagGO, adtSO);
        adtGO.setRectangle(new Rectangle(x - 30, y - 30, 60, 60));

    }

    private void createAssociation(int x, int y, DbSMSDiagram diagGO) throws DbException {
        int category = JVClassCategory.CLASS;
        DbJVClass adtSO = new DbJVClass(diagGO.getComposite(), JVClassCategory
                .getInstance(category));
        DbSMSClassifierGo adtGO = new DbOOAdtGo(diagGO, adtSO);
        adtGO.setRectangle(new Rectangle(x - 30, y - 30, 60, 60));
        adtSO.setAssociationClass(true);
        adtSO.setName(kAssociationName);

    }

    private void createEnumeration(int x, int y, DbSMSDiagram diagGO) throws DbException {
        int category = JVClassCategory.CLASS;
        DbJVClass adtSO = new DbJVClass(diagGO.getComposite(), JVClassCategory
                .getInstance(category));
        DbSMSClassifierGo adtGO = new DbOOAdtGo(diagGO, adtSO);
        adtGO.setRectangle(new Rectangle(x - 30, y - 30, 60, 60));
        setEnumStereotype(adtSO);
        adtSO.setName(kEnumerationName);
    }

    private void setEnumStereotype(DbJVClass adtSO) throws DbException {
        DbSMSProject proj = (DbSMSProject) adtSO.getCompositeOfType(DbSMSProject.metaClass);
        DbRelationN relN = proj.getComponents();
        DbEnumeration enu = relN.elements(DbSMSUmlExtensibility.metaClass);
        while (enu.hasMoreElements()) {
            DbSMSUmlExtensibility umlExtensibility = (DbSMSUmlExtensibility) enu.nextElement();
            DbSMSStereotype stereo = Extensibility.findByName(umlExtensibility,
                    Extensibility.ENUMERATION);
            adtSO.setUmlStereotype(stereo);
        } //end while
        enu.close();
    }

    private void createUtility(int x, int y, DbSMSDiagram diagGO) throws DbException {
        int category = JVClassCategory.CLASS;
        DbJVClass adtSO = new DbJVClass(diagGO.getComposite(), JVClassCategory
                .getInstance(category));
        DbSMSClassifierGo adtGO = new DbOOAdtGo(diagGO, adtSO);
        adtGO.setRectangle(new Rectangle(x - 30, y - 30, 60, 60));
        adtSO.setStatic(true);
        adtSO.setName(kUtilityName);
    }

}
