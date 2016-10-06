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

package org.modelsphere.sms.db.util;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Polygon;
import java.util.HashMap;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.graphic.Line;
import org.modelsphere.jack.srtool.graphic.DbGraphic;
import org.modelsphere.sms.be.db.*;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.oo.db.*;
import org.modelsphere.sms.oo.java.db.*;
import org.modelsphere.sms.or.db.*;

/**
 * 
 * Utility methods to create graphical representations
 * 
 */

public abstract class AnyGo {

    public static DbSMSDiagram getDiagram(HashMap diagMap, DbSMSPackage pack) throws DbException {
        Object diag = diagMap.get(pack);
        if (diag == null) { // null means we did not map yet this package to a diagram
            // If a package has no diagram, we map it to a dummy object, to avoid remapping it each time.
            DbEnumeration dbEnum = pack.getComponents().elements(DbSMSDiagram.metaClass);
            diag = (dbEnum.hasMoreElements() ? (Object) dbEnum.nextElement() : (Object) "None");
            dbEnum.close();
            diagMap.put(pack, diag);
        }
        return (diag instanceof DbSMSDiagram ? (DbSMSDiagram) diag : null);
    }

    // <pos> may be null (in which case the graphic rep is created with rect = DbSMSGraphicalObject.DEFAULT_RECT)
    public static DbSMSClassifierGo createClassifierGo(DbSMSDiagram diagram, DbSMSClassifier adt,
            Point pos) throws DbException {
        DbSMSClassifierGo adtGo;
        if (adt instanceof DbOOAdt)
            adtGo = new DbOOAdtGo(diagram, (DbOOAdt) adt);
        else if (adt instanceof DbBEUseCase)
            adtGo = new DbBEUseCaseGo(diagram, adt);
        else if (adt instanceof DbBEActor)
            adtGo = new DbBEActorGo(diagram, adt);
        else if (adt instanceof DbBEStore)
            adtGo = new DbBEStoreGo(diagram, adt);
        else
            adtGo = new DbORTableGo(diagram, adt);
        if (pos != null) {
            if (adtGo.isAutoFit())
                adtGo.setRectangle(new Rectangle(pos));
            else {
                Dimension size = adtGo.getRectangle().getSize();
                adtGo.setRectangle(new Rectangle(pos, size));
            }
        }
        return adtGo;
    }

    // Do not create a graphic rep if inner class.
    public static void createClassifierGo(HashMap diagMap, DbSMSClassifier adt) throws DbException {
        DbObject parent = adt.getComposite();
        if (parent instanceof DbSMSPackage) {
            DbSMSDiagram diag = getDiagram(diagMap, (DbSMSPackage) parent);
            if (diag != null)
                createClassifierGo(diag, adt, null);
        }
    }

    // <pos> may be null.
    public static DbSMSInheritanceGo createOOInheritanceGo(DbSMSDiagram diagram,
            DbOOInheritance inher, Point pos) throws DbException {
        DbSMSInheritanceGo inherGo = null;
        DbJVClass subAdt = (DbJVClass) inher.getSubClass();
        DbJVClass superAdt = (DbJVClass) inher.getSuperClass();
        // if (subAdt.getStereotype().equals(superAdt.getStereotype())) {  // Create a graphic rep only if "extends" inheritance.
        DbSMSClassifierGo[] adtGos = getLineClassifierGos(diagram, subAdt, superAdt, pos);
        inherGo = new DbSMSInheritanceGo(diagram, adtGos[0], adtGos[1], inher);
        //  }
        return inherGo;
    }

    // Create a graphic rep in the subclass diagram only if "extends" inheritance.
    public static void createOOInheritanceGo(HashMap diagMap, DbOOInheritance inher)
            throws DbException {
        DbSMSPackage pack = (DbSMSPackage) inher.getSubClass().getCompositeOfType(
                DbSMSPackage.metaClass);
        DbSMSDiagram diag = getDiagram(diagMap, pack);
        if (diag != null)
            createOOInheritanceGo(diag, inher, null);
    }

    // <pos> may be null.
    public static DbOOAssociationGo createOOAssociationGo(DbSMSDiagram diagram,
            DbOOAssociation assoc, Point pos) throws DbException {
        DbOOClass frontAdt = (DbOOClass) assoc.getFrontEnd().getAssociationMember().getComposite();
        DbOOClass backAdt = (DbOOClass) assoc.getBackEnd().getAssociationMember().getComposite();
        DbSMSClassifierGo[] adtGos = getLineClassifierGos(diagram, frontAdt, backAdt, pos);
        return new DbOOAssociationGo(diagram, adtGos[0], adtGos[1], assoc);
    }

    // Create a graphic rep in the diagram of each navigable member.
    public static void createOOAssociationGo(HashMap diagMap, DbOOAssociation assoc)
            throws DbException {
        DbSMSPackage frontPack = null;
        DbOOAssociationEnd end = assoc.getFrontEnd();
        if (end.isNavigable()) {
            frontPack = (DbSMSPackage) end.getAssociationMember().getCompositeOfType(
                    DbSMSPackage.metaClass);
            DbSMSDiagram diag = getDiagram(diagMap, frontPack);
            if (diag != null)
                createOOAssociationGo(diag, assoc, null);
        }
        end = assoc.getBackEnd();
        if (end.isNavigable()) {
            DbSMSPackage backPack = (DbSMSPackage) end.getAssociationMember().getCompositeOfType(
                    DbSMSPackage.metaClass);
            if (backPack != frontPack) {
                DbSMSDiagram diag = getDiagram(diagMap, backPack);
                if (diag != null)
                    createOOAssociationGo(diag, assoc, null);
            }
        }
    }

    // <pos> may be null.
    public static DbBEFlowGo createBEFlowGo(DbSMSDiagram diagram, DbBEFlow flow, Point pos)
            throws DbException {
        DbSMSClassifier frontAdt = flow.getFirstEnd();
        DbSMSClassifier backAdt = flow.getSecondEnd();
        DbSMSClassifierGo[] adtGos = getLineClassifierGos(diagram, frontAdt, backAdt, pos);
        return new DbBEFlowGo(diagram, adtGos[0], adtGos[1], flow);
    }

    // <pos> may be null.
    public static DbORAssociationGo createORAssociationGo(DbSMSDiagram diagram,
            DbORAssociation assoc, Point pos) throws DbException {
        DbSMSClassifier frontAdt = assoc.getFrontEnd().getClassifier();
        DbSMSClassifier backAdt = assoc.getBackEnd().getClassifier();
        
        DbSMSClassifierGo[] adtGos;
    	DbORChoiceOrSpecialization choiceSpec = assoc.getChoiceOrSpecialization(); 
    	if (choiceSpec != null) {
    	    boolean parentAssoc = isParentAssociation(assoc, choiceSpec);
    	    if (parentAssoc) {
    	        //link between the choice and the parent
    	        adtGos = getLineClassifierGos(diagram, frontAdt, backAdt, pos);
    	    } else {
    	        //link between the choice and the child
    	        DbORTable parent = choiceSpec.getParentTable();
    	        DbORAbsTable child = findChildTable(parent, frontAdt, backAdt); 
    	        adtGos = getLineClassifierGos(diagram, choiceSpec, child, pos);
    	    }
    	} else {
    	    //link between the parent and the child
    		adtGos = getLineClassifierGos(diagram, frontAdt, backAdt, pos);
    	}
    	
    	DbORAssociationGo assocGo =  new DbORAssociationGo(diagram, adtGos[0], adtGos[1], assoc);
        return assocGo;
    }

    private static boolean isParentAssociation(DbORAssociation assoc,
            DbORChoiceOrSpecialization choiceSpec) throws DbException {
        DbSMSClassifier frontAdt = assoc.getFrontEnd().getClassifier();
        DbSMSClassifier backAdt = assoc.getBackEnd().getClassifier();
        DbORTable parent = choiceSpec.getParentTable();
        
        //it is a parent assoc if we have the parent and one end and the choiceSpec at the opposite end
        boolean isParent = (frontAdt.equals(parent)) && (backAdt.equals(choiceSpec)) ||
             (backAdt.equals(parent)) && (frontAdt.equals(choiceSpec));
        return isParent;
    }

    private static DbORAbsTable findChildTable(DbORTable parent,
			DbSMSClassifier frontAdt, DbSMSClassifier backAdt) {
    	DbORAbsTable child = null; 
    	
    	if (parent.equals(frontAdt)) {
    		child = (DbORAbsTable)backAdt;
    	} else if (parent.equals(backAdt)) {
    		child = (DbORAbsTable)frontAdt;
    	} 
    	
		return child;
	}

	// If we create a graphic for one of the two classes of the association,
    // place the new graphic above the existing one.
    private static DbSMSClassifierGo[] getLineClassifierGos(DbSMSDiagram diagram,
            DbSMSClassifier frontAdt, DbSMSClassifier backAdt, Point pos) throws DbException {
        DbSMSClassifierGo[] adtGos = new DbSMSClassifierGo[2];
        if (frontAdt != null && frontAdt instanceof DbBEUseCase)
            adtGos[0] = (DbSMSClassifierGo) DbGraphic.getFirstGraphicalObject(diagram, frontAdt,
                    null, DbBEUseCaseGo.metaClass);
        else
            adtGos[0] = (DbSMSClassifierGo) DbGraphic.getFirstGraphicalObject(diagram, frontAdt);
        if (frontAdt == backAdt) { // recursive association
            if ((adtGos[0] == null) && (frontAdt != null))
                adtGos[0] = createClassifierGo(diagram, frontAdt, pos);
            adtGos[1] = adtGos[0];
        } else {
            if (backAdt != null && backAdt instanceof DbBEUseCase)
                adtGos[1] = (DbSMSClassifierGo) DbGraphic.getFirstGraphicalObject(diagram, backAdt,
                        null, DbBEUseCaseGo.metaClass);
            else
                adtGos[1] = (DbSMSClassifierGo) DbGraphic.getFirstGraphicalObject(diagram, backAdt);
            if ((adtGos[0] == null) && (frontAdt != null)) {
                if ((adtGos[1] == null) && (backAdt != null))
                    adtGos[1] = createClassifierGo(diagram, backAdt, pos);
                adtGos[0] = createClassifierGo(diagram, frontAdt, getNearPos(adtGos[1]));
            } else if ((adtGos[1] == null) && (backAdt != null)) {
                adtGos[1] = createClassifierGo(diagram, backAdt, getNearPos(adtGos[0]));
            }
        }
        return adtGos;
    }

    private static Point getNearPos(DbSMSGraphicalObject go) throws DbException {
        if (go == null)
            return null;
        Rectangle rect = DbGraphic.getRectangle(go);
        return new Point(rect.x + rect.width / 2, Math.max(0, rect.y - rect.height / 2 - 20));
    }

    public static final void convertToRightAngle(DbObject lineGo) throws DbException {
        Polygon poly = (Polygon) lineGo.get(DbGraphic.fLineGoPolyline);
        int i = 1;
        int nb = poly.npoints;
        poly.addPoint(poly.xpoints[nb - 1], poly.ypoints[nb - 1]);
        if (Line.startHorz(poly)) {
            poly.ypoints[1] = poly.ypoints[0];
            i++;
        }
        while (true) {
            if (i == nb)
                break;
            poly.xpoints[i] = poly.xpoints[i - 1];
            i++;
            if (i == nb)
                break;
            poly.ypoints[i] = poly.ypoints[i - 1];
            i++;
        }
        if (nb > 2 && poly.xpoints[nb - 1] == poly.xpoints[nb]
                && poly.ypoints[nb - 1] == poly.ypoints[nb])
            poly.npoints--;
        lineGo.set(DbGraphic.fLineGoPolyline, poly);
    } //end convertToRightAngle()

    public static DbSMSStyle findStyle(DbSMSGraphicalObject go) throws DbException {
        go.getDb().beginReadTrans();
        DbSMSStyle style = go.getStyle();
        if (style == null) {
            DbSMSDiagram diagram = (DbSMSDiagram) go.getCompositeOfType(DbSMSDiagram.metaClass);
            style = diagram.findStyle();
        }
        go.getDb().commitTrans();
        return style;
    } //end findStyle()
}
