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

import java.awt.Image;
import java.awt.Polygon;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.tool.Tool;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSInheritanceGo;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSUmlExtensibility;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVInheritance;
import org.modelsphere.sms.oo.java.graphic.AdtBox;
import org.modelsphere.sms.oo.java.international.LocaleMgr;

public class JVInheritanceTool extends JVLineTool {
    public static final String kInheritanceCreationTool = LocaleMgr.screen
            .getString("InheritanceCreationTool");
    public static final Image kImageInheritanceCreationTool = GraphicUtil.loadImage(Tool.class,
            "resources/angulararrowlineend.gif"); // NOT
    // LOCALIZABLE
    // -
    // tool
    // image
    public static final Image kImageRightAngleInheritanceCreationTool = GraphicUtil.loadImage(
            Tool.class, "resources/rightanglearrowlineend.gif"); // NOT

    // LOCALIZABLE
    // -
    // tool
    // image

    public JVInheritanceTool(String text, String[] tooltips, Image image, Image[] secondaryImages) {
        super(text, tooltips, image, secondaryImages, 1);
    }

    protected final boolean isDestAcceptable(GraphicNode source, GraphicNode dest) {
        boolean isAcceptable = true;
        if (dest == null) {
            isAcceptable = false;
        } else {
            DbJVClass subclass = ((AdtBox) source).getAdtSO();
            DbJVClass superclass = ((AdtBox) dest).getAdtSO();
            if (subclass == superclass) { // recursive inheritance
                isAcceptable = false;
            } else {
                try {
                    subclass.getDb().beginReadTrans();
                    if (subclass.isInterface() && (!superclass.isInterface())) {
                        isAcceptable = false; // interface cannot inherits from
                        // an actual class
                    }
                    subclass.getDb().commitTrans();
                } catch (DbException ex) {
                    isAcceptable = false;
                }
            }
        } // end if
        return isAcceptable;
    } // end isDestAcceptable()

    protected final void createLine(GraphicNode source, GraphicNode dest, Polygon poly) {
        try {
            createInheritance((AdtBox) source, (AdtBox) dest, poly);
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
    }

    // If the inheritance already exists, creates a new graphical representation
    // (unless a graphical representation already exists between the same 2
    // boxes).
    private void createInheritance(AdtBox box1, AdtBox box2, Polygon poly) throws DbException {
        DbSMSDiagram diagGO = (DbSMSDiagram) ((ApplicationDiagram) model).getDiagramGO();
        diagGO.getDb()
                .beginTrans(Db.WRITE_TRANS, LocaleMgr.action.getString("InheritanceCreation"));
        DbJVClass adtSub = (DbJVClass) box1.getAdtSO();
        DbJVClass adtSuper = (DbJVClass) box2.getAdtSO();
        DbJVInheritance inhSO = null;
        DbEnumeration dbEnum = adtSub.getSuperInheritances().elements();
        while (dbEnum.hasMoreElements()) {
            DbJVInheritance curInhSO = (DbJVInheritance) dbEnum.nextElement();
            if (curInhSO.getSuperClass() == adtSuper) {
                inhSO = curInhSO;
                break;
            }
        }
        dbEnum.close();
        if (inhSO == null) {
            if (adtSuper.isCollection())
                adtSub.setCollection(Boolean.TRUE);
            inhSO = new DbJVInheritance(adtSub, adtSuper);
        }

        DbSMSClassifierGo go1 = box1.getAdtGO();
        DbSMSClassifierGo go2 = box2.getAdtGO();
        DbSMSInheritanceGo inhGO = null;
        dbEnum = inhSO.getInheritanceGos().elements();
        while (dbEnum.hasMoreElements()) {
            DbSMSInheritanceGo curInhGO = (DbSMSInheritanceGo) dbEnum.nextElement();
            if (go1 == curInhGO.getFrontEndGo() && go2 == curInhGO.getBackEndGo()) {
                inhGO = curInhGO;
                break;
            }
        }
        dbEnum.close();
        if (inhGO == null) {
            inhGO = new DbSMSInheritanceGo(diagGO, go1, go2, inhSO);
            inhGO.setPolyline(poly);
            inhGO.setRightAngle(rightAngle ? Boolean.TRUE : Boolean.FALSE);
        }

        // inher is not a semantical object, so cannot have a stereotype (for
        // now) [MS]
        // DbSMSStereotype realize = getRealizeStereotype(diag);
        // inher.setStereotype(realize);
        diagGO.getDb().commitTrans();
    }

    private static DbSMSStereotype g_realizeStereotype = null;

    private DbSMSStereotype getRealizeStereotype(DbOODiagram diag) throws DbException {
        if (g_realizeStereotype == null) {
            DbSMSProject proj = (DbSMSProject) diag.getProject();
            DbSMSUmlExtensibility ext = proj.getUmlExtensibility();
            DbRelationN reln = ext.getComponents();
            DbEnumeration dbEnum = reln.elements(DbSMSStereotype.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbSMSStereotype stereotype = (DbSMSStereotype) dbEnum.nextElement();
                String name = stereotype.getName();
                if (name.equals("realize")) { // NOT LOCALIZABLE, stereotype
                    // name
                    g_realizeStereotype = stereotype;
                    break;
                }
            } // end while
            dbEnum.close();
        } // end if

        return g_realizeStereotype;
    } // end getRealizeStereotype()
} // end JvInheritanceTool
