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

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.net.URL;

import javax.swing.ImageIcon;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.sms.Application;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.db.DbSMSAssociation;
import org.modelsphere.sms.db.DbSMSAssociationEnd;
import org.modelsphere.sms.db.DbSMSInheritance;
import org.modelsphere.sms.db.DbSMSLink;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSUmlConstraint;
import org.modelsphere.sms.db.DbSMSUmlExtensibility;
import org.modelsphere.sms.oo.db.DbOOClass;
import org.modelsphere.sms.oo.db.DbOODataMember;
import org.modelsphere.sms.oo.db.DbOOMethod;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORTable;

public class Extensibility {
    private static final String EXTENSION = ".gif"; //NOT LOCALIZABLE, file extension

    public static final String ENUMERATION = "enumeration";

    public Extensibility() {
    }

    /**
     * Called by DbSMSProject.initUMLExtensibility() & SMSVersionConverter.initUMLExtensibility()
     * List of stereotypes in version 2
     * 
     * WARNING : Adding new stereotypes in a new version means having to check if there are present
     * in previous version's models (see addMissingStereotypes)
     **/

    public static void createBuiltInStereotypes(DbSMSUmlExtensibility umlExtensibility)
            throws DbException {

        DbSMSStereotype stereotype = null;

        addMissingStereotypesV6(umlExtensibility);
        addMissingStereotypesV7(umlExtensibility);

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("actor"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("actor.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbOOClass.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("actor"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("actor.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbBEStore.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("association"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("association.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbSMSAssociationEnd.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("becomes"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(null);
        stereotype.setMetaClassName(DbBEFlow.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("boundary"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("boundary.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbSMSPackage.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("boundary"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("boundary.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbOOClass.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("calls"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(null);
        stereotype.setMetaClassName(DbBEFlow.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("communicates"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(null);
        stereotype.setMetaClassName(DbBEFlow.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("control"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("control.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbSMSPackage.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("control"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("control.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbOOClass.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("create"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("create.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbOOMethod.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("dataType"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("dataType.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbOOClass.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("decision"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("decision.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbBEUseCase.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("derive"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("derive.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbSMSInheritance.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("destroy"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("destroy.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbOOMethod.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName(ENUMERATION);
        stereotype.setIcon(getImage("enumeration.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbOOClass.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("end"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("end.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbBEUseCase.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("entity"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("entity.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbSMSPackage.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("entity"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("entity.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbOOClass.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("extends"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(null);
        stereotype.setMetaClassName(DbBEFlow.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("facade"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("facade.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbSMSPackage.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("framework"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("framework.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbSMSPackage.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("global"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("global.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbSMSAssociationEnd.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("helper"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("helper.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbOOMethod.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("implementation"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("implementation.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbSMSInheritance.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("implementationClass"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("implementationClass.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbOOClass.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("implementor"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("implementor.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbOOMethod.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("implicit"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("implicit.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbSMSAssociation.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("includes"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(null);
        stereotype.setMetaClassName(DbBEFlow.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("invariant"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("invariant.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbOOMethod.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("local"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("local.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbSMSAssociationEnd.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("manager"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("manager.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbOOMethod.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("metaclass"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("metaclass.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbOOClass.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("model"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("triangle.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbSMSPackage.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("nested"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("nested.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbSMSLink.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("parameter"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("parameter.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbSMSAssociationEnd.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("postcondition"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("postcondition.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbOOMethod.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("powertype"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("powertype.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbOOClass.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("precondition"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("precondition.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbOOMethod.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("primitive"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("primitive.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbOOClass.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("process"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("process.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbOOClass.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("self"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("self.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbSMSAssociationEnd.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("start"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("start.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbBEUseCase.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("structure"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("structure.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbOOClass.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("subsystem"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("subsystem.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbSMSPackage.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("stub"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("stub.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbSMSPackage.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("supports"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(null);
        stereotype.setMetaClassName(DbBEFlow.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("horizontal synchro"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("horiz-synchro.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbBEUseCase.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("vertical synchro"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("verti-synchro.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbBEUseCase.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("thread"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("thread.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbOOClass.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("type"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("type.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbOOClass.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("uses"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(null);
        stereotype.setMetaClassName(DbBEFlow.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("utility"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("utility.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbOOClass.metaClass.getJClass().getName());
    } //end

    public static DbSMSStereotype findByName(DbSMSUmlExtensibility umlExtensibility, String name)
            throws DbException {
        DbSMSStereotype stereotype = null;

        DbRelationN relN = umlExtensibility.getComponents();
        DbEnumeration dbEnum = relN.elements(DbSMSStereotype.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSStereotype s = (DbSMSStereotype) dbEnum.nextElement();
            if (name.equals(s.getName())) {
                stereotype = s;
                break;
            }
        } //end while
        dbEnum.close();

        return stereotype;
    }

    /**
     * Called by DbSMSProject.initUMLExtensibility() & SMSVersionConverter.initUMLExtensibility()
     **/
    public static void createBuiltInUmlConstraints(DbSMSUmlExtensibility umlExtensibility)
            throws DbException {
        // This content of this method must synchronized with SMSVersionConverter.createBuildInUmlConstraints()
        DbSMSUmlConstraint constraint = null;

        constraint = new DbSMSUmlConstraint(umlExtensibility);
        constraint.setBuiltIn(Boolean.TRUE);
        constraint.setName("complete"); //NOT LOCALIZABLE, stereotype name
        constraint.setMetaClassName(DbSMSInheritance.metaClass.getJClass().getName());

        constraint = new DbSMSUmlConstraint(umlExtensibility);
        constraint.setBuiltIn(Boolean.TRUE);
        constraint.setName("destroyed"); //NOT LOCALIZABLE, stereotype name
        constraint.setMetaClassName(DbSMSAssociationEnd.metaClass.getJClass().getName());

        constraint = new DbSMSUmlConstraint(umlExtensibility);
        constraint.setBuiltIn(Boolean.TRUE);
        constraint.setName("disjoint"); //NOT LOCALIZABLE, stereotype name
        constraint.setMetaClassName(DbSMSInheritance.metaClass.getJClass().getName());

        constraint = new DbSMSUmlConstraint(umlExtensibility);
        constraint.setBuiltIn(Boolean.TRUE);
        constraint.setName("incomplete"); //NOT LOCALIZABLE, stereotype name
        constraint.setMetaClassName(DbSMSInheritance.metaClass.getJClass().getName());

        constraint = new DbSMSUmlConstraint(umlExtensibility);
        constraint.setBuiltIn(Boolean.TRUE);
        constraint.setName("new"); //NOT LOCALIZABLE, stereotype name
        constraint.setMetaClassName(DbSMSAssociationEnd.metaClass.getJClass().getName());

        constraint = new DbSMSUmlConstraint(umlExtensibility);
        constraint.setBuiltIn(Boolean.TRUE);
        constraint.setName("overlapping"); //NOT LOCALIZABLE, stereotype name
        constraint.setMetaClassName(DbSMSInheritance.metaClass.getJClass().getName());

        constraint = new DbSMSUmlConstraint(umlExtensibility);
        constraint.setBuiltIn(Boolean.TRUE);
        constraint.setName("transient");
        constraint.setMetaClassName(DbSMSAssociationEnd.metaClass.getJClass().getName());

        constraint = new DbSMSUmlConstraint(umlExtensibility);
        constraint.setBuiltIn(Boolean.TRUE);
        constraint.setName("xor"); //NOT LOCALIZABLE, stereotype name
        constraint.setMetaClassName(DbSMSAssociation.metaClass.getJClass().getName());

        constraint = new DbSMSUmlConstraint(umlExtensibility);
        constraint.setBuiltIn(Boolean.TRUE);
        constraint.setName("pk"); //NOT LOCALIZABLE, stereotype name
        constraint.setMetaClassName(DbOODataMember.metaClass.getJClass().getName());

        constraint = new DbSMSUmlConstraint(umlExtensibility);
        constraint.setBuiltIn(Boolean.TRUE);
        constraint.setName("ordered"); //NOT LOCALIZABLE, stereotype name
        constraint.setMetaClassName(DbOODataMember.metaClass.getJClass().getName());

        constraint = new DbSMSUmlConstraint(umlExtensibility);
        constraint.setBuiltIn(Boolean.TRUE);
        constraint.setName("unordered"); //NOT LOCALIZABLE, stereotype name
        constraint.setMetaClassName(DbOODataMember.metaClass.getJClass().getName());

        constraint = new DbSMSUmlConstraint(umlExtensibility);
        constraint.setBuiltIn(Boolean.TRUE);
        constraint.setName("unique"); //NOT LOCALIZABLE, stereotype name
        constraint.setMetaClassName(DbOODataMember.metaClass.getJClass().getName());

        constraint = new DbSMSUmlConstraint(umlExtensibility);
        constraint.setBuiltIn(Boolean.TRUE);
        constraint.setName("nonunique"); //NOT LOCALIZABLE, stereotype name
        constraint.setMetaClassName(DbOODataMember.metaClass.getJClass().getName());
    } //end createBuiltInUmlConstraints()

    public static DbSMSUmlConstraint findConstraintByName(DbSMSUmlExtensibility umlExtensibility,
            String name) throws DbException {
        DbSMSUmlConstraint constr = null;

        DbRelationN relN = umlExtensibility.getComponents();
        DbEnumeration dbEnum = relN.elements(DbSMSUmlConstraint.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSUmlConstraint c = (DbSMSUmlConstraint) dbEnum.nextElement();
            if (c.getName().equals(name)) {
                constr = c;
                break;
            }
        } //end while
        dbEnum.close();

        return constr;
    }

    private static ImageIcon getImageIcon(String name) {
        ImageIcon icon = null;
        //Class claz = Extensibility.class;
        URL url = Application.class.getResource("db/resources/" + name); //NOT LOCALIZABLE, folder name
        if (url != null) {
            //Toolkit toolkit = Toolkit.getDefaultToolkit();
            icon = new ImageIcon(url);
        }

        return icon;
    } //end getImageIcon()

    //May return null if stereotype's icon not found
    public static Image getDefaultIcon(DbSMSStereotype stereotype) throws DbException {
        String name = stereotype.getName();
        if (name == null)
            return null;

        String imageName = name + EXTENSION;
        Image image = getImage(imageName);
        return image;
    } //end getDefautIcon()

    //May return null if not found
    public static Image getImage(String filename) {
        Image image = null;
        ImageIcon icon = getImageIcon(filename);
        if (icon != null) {
            image = icon.getImage();
        }

        return image;
    }

    //utility method : make sure the image is loaded before to continue
    private void waitForImage(Component comp, Image image) {
        MediaTracker tracker = new MediaTracker(comp);
        try {
            tracker.addImage(image, 0);
            tracker.waitForID(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    } //end waitForImage()

    /*
     * for each stereotype, add if it was missing in the initial list, if its image != null then set
     * its new image
     */
    public static void addMissingStereotypes(DbSMSUmlExtensibility umlExtensibility)
            throws DbException {
        DbRelationN relN = umlExtensibility.getComponents();
        DbEnumeration dbEnum = relN.elements(DbSMSStereotype.metaClass);
        boolean actorFound = false;
        while (dbEnum.hasMoreElements()) {
            DbSMSStereotype stereotype = (DbSMSStereotype) dbEnum.nextElement();
            String name = stereotype.getName();
            if (name.equals("actor")) //NOT LOCALIZABLE, stereotype name
                actorFound = true;

            if (stereotype.getIcon() == null) {
                if (name != null) {
                    stereotype.setIcon(getImage(name + ".gif")); //NOT LOCALIZABLE, file extension
                }
            } //end if
        } //end while
        dbEnum.close();

        if (!actorFound) {
            DbSMSStereotype stereotype = new DbSMSStereotype(umlExtensibility);
            stereotype.setBuiltIn(Boolean.TRUE);
            stereotype.setName("actor"); //NOT LOCALIZABLE, stereotype name
            stereotype.setIcon(getImage("actor.gif")); //NOT LOCALIZABLE, file name
            stereotype.setMetaClassName(DbOOClass.metaClass.getJClass().getName());
        } //end if

    }

    public static void addMissingStereotypesV6(DbSMSUmlExtensibility umlExtensibility)
            throws DbException {
        DbSMSStereotype stereotype = null;

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("lookup table"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(null); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbORTable.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("associative table"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(null); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbORTable.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("auto generated"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(null); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbORColumn.metaClass.getJClass().getName());

    }

    public static void addMissingStereotypesV7(DbSMSUmlExtensibility umlExtensibility)
            throws DbException {
        DbSMSStereotype stereotype = null;

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("fact"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("fact.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbORTable.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("dimension"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(getImage("dimension.gif")); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbORTable.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("group"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(null); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbORTable.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("test group"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(null); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbORTable.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("message request"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(null); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbORTable.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("message response"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(null); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbORTable.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("test message request"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(null); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbORTable.metaClass.getJClass().getName());

        stereotype = new DbSMSStereotype(umlExtensibility);
        stereotype.setBuiltIn(Boolean.TRUE);
        stereotype.setName("test message response"); //NOT LOCALIZABLE, stereotype name
        stereotype.setIcon(null); //NOT LOCALIZABLE, stereotype name
        stereotype.setMetaClassName(DbORTable.metaClass.getJClass().getName());
    }

    //end addMissingStereotypes

    public static void main(String[] args) {
        Image image = Extensibility.getImage("zzz.gif");
        System.out.println(image.toString());
    } //end main()
} //end Extensibility
