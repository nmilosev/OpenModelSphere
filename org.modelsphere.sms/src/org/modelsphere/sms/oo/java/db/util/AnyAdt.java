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

package org.modelsphere.sms.oo.java.db.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.jack.util.DiscreteValuesComparator;
import org.modelsphere.jack.util.SrVector;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.oo.db.DbOOAbstractMethod;
import org.modelsphere.sms.oo.db.DbOOAdt;
import org.modelsphere.sms.oo.db.DbOOAssociationEnd;
import org.modelsphere.sms.oo.db.DbOOClass;
import org.modelsphere.sms.oo.db.DbOODataMember;
import org.modelsphere.sms.oo.db.DbOOInheritance;
import org.modelsphere.sms.oo.db.DbOOMethod;
import org.modelsphere.sms.oo.db.DbOOParameter;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVConstructor;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.DbJVParameter;
import org.modelsphere.sms.oo.java.db.DbJVPrimitiveType;
import org.modelsphere.sms.oo.java.db.srtypes.JVVisibility;

public abstract class AnyAdt {
    public static final int PACKAGE_ADT = 1;
    public static final int NESTED_ADT = 2;
    public static final int INNER_CLASS = 3;

    public static final int MEMBER_FIELD = 1;
    public static final int MEMBER_INNER_CLASS = 2;
    public static final int MEMBER_METHOD = 3;

    private static int LOOPS_MAX = 2048;

    public static DbEnumeration getClassEnum(final DbObject root, final int mask)
            throws DbException {
        return new DbEnumeration() {
            private DbEnumeration dbEnum = root.componentTree(DbJVClass.metaClass);
            private DbObject nextDbo = null;

            public boolean hasMoreElements() throws DbException {
                if (nextDbo != null)
                    return true;
                while (true) {
                    if (!dbEnum.hasMoreElements())
                        return false;
                    DbJVClass nextClass = (DbJVClass) dbEnum.nextElement();
                    int cat = nextClass.getStereotype().getValue();
                    if ((mask & 1 << cat) != 0) {
                        nextDbo = nextClass;
                        return true;
                    }
                }
            }

            public DbObject nextElement() throws DbException {
                if (!hasMoreElements())
                    throw new RuntimeException("getClassEnum: nextElement without hasMoreElements");
                DbObject dbo = nextDbo;
                nextDbo = null;
                return dbo;
            }

            public void close() {
                dbEnum.close();
            }
        };
    }

    public static Collection getAdtsAux(Collection adts, DbObject semObj) throws DbException {
        int counter = 0;
        DbEnumeration dbEnum = semObj.componentTree(DbOOAdt.metaClass);
        while (dbEnum.hasMoreElements()) {
            adts.add(dbEnum.nextElement());
            counter++;
            if (counter > LOOPS_MAX) {
                break; //endless loop detection
            }
        } //end while
        dbEnum.close();
        return adts;
    }

    public static Collection getClassesAux(Collection classes, DbObject semObj) throws DbException {
        int counter = 0;
        DbEnumeration dbEnum = semObj.componentTree(DbJVClass.metaClass);
        while (dbEnum.hasMoreElements()) {
            classes.add(dbEnum.nextElement());
            counter++;
            if (counter > LOOPS_MAX) {
                break; //endless loop detection
            }
        }
        dbEnum.close();
        return classes;
    }

    public static Collection getCollectionAdtsAux(Collection adts, DbObject semObj)
            throws DbException {
        DbEnumeration dbEnum = semObj.componentTree(DbJVClass.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVClass adt = (DbJVClass) dbEnum.nextElement();
            if (adt.isCollection())
                adts.add(adt);
        }
        dbEnum.close();
        return adts;
    }

    public static Collection getInterfaceAux(Collection interfaces, DbObject semObj)
            throws DbException {
        DbEnumeration dbEnum = semObj.componentTree(DbJVClass.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVClass adt = (DbJVClass) dbEnum.nextElement();
            if (adt.isInterface())
                interfaces.add(adt);
        }
        dbEnum.close();
        return interfaces;
    }

    public static MetaRelationship getTypeField(DbObject dbo) {
        if (dbo instanceof DbOODataMember)
            return DbOODataMember.fType;
        if (dbo instanceof DbOOMethod)
            return DbOOMethod.fReturnType;
        if (dbo instanceof DbOOParameter)
            return DbOOParameter.fType;
        return null;
    }

    public static void addGetMethods(DbJVClass adt) throws DbException {
        if (adt.isInterface())
            return;
        DbEnumeration dbEnum = adt.getComponents().elements(DbJVDataMember.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVDataMember field = (DbJVDataMember) dbEnum.nextElement();
            if (field.getAssociationEnd() != null && !field.getAssociationEnd().isNavigable())
                continue;
            if (field.isStatic())
                continue;
            addGetMethod(field);
        }
        dbEnum.close();
    }

    public static DbJVMethod addGetMethod(DbJVDataMember field) throws DbException {
        DbJVClass adt = (DbJVClass) field.getComposite();
        String fieldName = field.getName();
        DbOOAdt type = field.getType();
        if (DbObject.valuesAreEqual(fieldName, null) || type == null)
            return null;
        boolean isBool = (type instanceof DbJVPrimitiveType && type.getName().equals("boolean") && //NOT LOCALIZABLE
        DbObject.valuesAreEqual(field.getTypeUse(), null));
        String nethodName = (isBool ? "is" : "get") + Character.toUpperCase(fieldName.charAt(0))
                + fieldName.substring(1); //NOT LOCALIZABLE
        if (adt.findComponentByName(DbJVMethod.metaClass, nethodName) != null)
            return null;
        DbJVMethod method = new DbJVMethod(adt);
        method.setName(nethodName);
        method.setVisibility(JVVisibility.getInstance(JVVisibility.PUBLIC));
        if (field.isStatic())
            method.setStatic(Boolean.TRUE);
        else
            method.setFinal(Boolean.TRUE);
        method.setReturnType(type);
        method.setReturnElementType(field.getElementType());
        method.setTypeUse(field.getTypeUse());
        method.setTypeUseStyle(field.getTypeUseStyle());
        method.setBody("return " + fieldName + ";"); //NOT LOCALIZABLE
        return method;
    }

    public static void addSetMethods(DbJVClass adt) throws DbException {
        if (adt.isInterface())
            return;
        DbEnumeration dbEnum = adt.getComponents().elements(DbJVDataMember.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVDataMember field = (DbJVDataMember) dbEnum.nextElement();
            if (field.getAssociationEnd() != null && !field.getAssociationEnd().isNavigable())
                continue;
            if (field.isStatic())
                continue;
            addSetMethod(field);
        }
        dbEnum.close();
    }

    public static DbJVMethod addSetMethod(DbJVDataMember field) throws DbException {
        if (field.isFinal())
            return null;
        DbJVClass adt = (DbJVClass) field.getComposite();
        String fieldName = field.getName();
        DbOOAdt type = field.getType();
        if (DbObject.valuesAreEqual(fieldName, null) || type == null)
            return null;
        String nethodName = "set" + Character.toUpperCase(fieldName.charAt(0))
                + fieldName.substring(1); //NOT LOCALIZABLE
        if (adt.findComponentByName(DbJVMethod.metaClass, nethodName) != null)
            return null;
        DbJVMethod method = new DbJVMethod(adt);
        method.setName(nethodName);
        method.setVisibility(JVVisibility.getInstance(JVVisibility.PUBLIC));
        if (field.isStatic())
            method.setStatic(Boolean.TRUE);
        else
            method.setFinal(Boolean.TRUE);
        DbJVParameter param = new DbJVParameter(method);
        param.setName(fieldName);
        param.setType(type);
        param.setElementType(field.getElementType());
        param.setTypeUse(field.getTypeUse());
        param.setTypeUseStyle(field.getTypeUseStyle());
        method.setBody("this." + fieldName + " = " + fieldName + ";"); //NOT LOCALIZABLE
        return method;
    }

    public static int getCategory(DbJVClass adt) throws DbException {
        int adtCategory = PACKAGE_ADT;
        if (adt.getComposite() instanceof DbJVClass) { // nested or inner adt
            if (adt.isInterface())
                adtCategory = NESTED_ADT; // nested interface is implicitly static
            else if (((DbJVClass) adt.getComposite()).isInterface())
                adtCategory = NESTED_ADT; // class in an interface is implicitly static
            else
                adtCategory = (adt.isStatic() ? NESTED_ADT : INNER_CLASS);
        }
        return adtCategory;
    }

    public static String getNameWithEnclosingAdts(DbOOClass adt) throws DbException {
        String hierarchicalName = adt.getName();
        DbObject parent = adt.getComposite();
        while (parent instanceof DbOOClass) {
            hierarchicalName = ((DbSemanticalObject) parent).getName() + "." + hierarchicalName;
            parent = parent.getComposite();
        }
        return hierarchicalName;
    }

    public static boolean isEnclosingAdt(DbOOClass adt) throws DbException {
        return isEnclosingAdtFor(adt, DbOOClass.metaClass);
    }

    public static boolean isEnclosingAdtFor(DbOOClass adt, MetaClass metaClass) throws DbException {
        DbEnumeration dbEnum = adt.getComponents().elements(metaClass);
        boolean found = dbEnum.hasMoreElements();
        dbEnum.close();
        return found;
    }

    public static boolean isOtherAdtAccessible(DbJVClass adt, DbJVClass otherAdt)
            throws DbException {
        boolean isAccessible = false;

        // containment hierarchy --> access for nested/inner adts (whatever level deep):
        //    - nested/inner adts can access all adts of their containing adt.
        //    - a containing adt can access all adts of the adts it contains.
        //    - two nested/inner adts enclosed by the same third adt can access each other's member adts.
        if (AnyAdt.isOtherAdtInSameContainmentHierarchy(adt, otherAdt)) {
            isAccessible = true;
        } else {
            AccessData accessData = AnyAdt.getAccessData(otherAdt);
            if (accessData.visibility.getValue() == JVVisibility.PUBLIC) {
                isAccessible = true;
            } else if (accessData.visibility.getValue() == JVVisibility.DEFAULT) {
                if (adt.getCompositeOfType(DbSMSPackage.metaClass) == otherAdt
                        .getCompositeOfType(DbSMSPackage.metaClass))
                    isAccessible = true;
            } else if (accessData.visibility.getValue() == JVVisibility.PROTECTED) {
                if (adt.getCompositeOfType(DbSMSPackage.metaClass) == otherAdt
                        .getCompositeOfType(DbSMSPackage.metaClass))
                    isAccessible = true;
                else {
                    Vector adtAncestors = (Vector) AnyAdt.findAncestors(adt, new Vector());
                    Vector otherAdtEnclosingAdts = AnyAdt.getEnclosingAdts(accessData.adt);
                    for (Enumeration enumeration = otherAdtEnclosingAdts.elements(); enumeration
                            .hasMoreElements();) {
                        Object otherAdtEnclosingAdt = enumeration.nextElement();
                        if (adtAncestors.contains(otherAdtEnclosingAdt)) {
                            isAccessible = true;
                            break;
                        }
                    }
                }
            }
        }
        return isAccessible;
    }

    public static boolean isOtherAdtInSameContainmentHierarchy(DbOOClass adt, DbOOClass otherAdt)
            throws DbException {
        Vector enclosingAdtsOfAdt = AnyAdt.getEnclosingAdts(adt);
        Vector enclosingAdtsOfOtherAdt = AnyAdt.getEnclosingAdts(otherAdt);
        return (enclosingAdtsOfAdt.lastElement() == enclosingAdtsOfOtherAdt.lastElement());
    }

    // return a vector of enclosing adts, including parameter "adt" and package top-level adt
    public static Vector getEnclosingAdts(DbOOClass adt) throws DbException {
        Vector enclosingAdts = new Vector();
        enclosingAdts.addElement(adt);
        DbObject adtContainer = adt.getComposite();
        while (adtContainer instanceof DbOOClass) {
            enclosingAdts.addElement(adtContainer);
            adtContainer = adtContainer.getComposite();
        }
        return enclosingAdts;
    }

    public static final class AccessData {
        public DbJVClass adt;
        public JVVisibility visibility;

        public AccessData(DbJVClass newAdt, JVVisibility newVisibility) {
            adt = newAdt;
            visibility = newVisibility;
        }
    }

    // return adt visibility for an other adt not in the same containment hierarchy
    public static AccessData getAccessData(DbJVClass adt) throws DbException {
        JVVisibility accessVisibility = getImplicitAccessVisibility(adt);
        DbJVClass accessAdt = adt;
        DbObject container = adt.getComposite();
        while (container instanceof DbJVClass
                && accessVisibility.getValue() != JVVisibility.PRIVATE
                && accessVisibility.getValue() != JVVisibility.PROTECTED) {
            DbJVClass adtContainer = (DbJVClass) container;
            JVVisibility adtContainerVisibility = getImplicitAccessVisibility(adtContainer);
            int compare = JVVisibility.getComparator().compare(adtContainerVisibility,
                    accessVisibility);
            if (compare == DiscreteValuesComparator.LESS_THAN) { //adtContainer.getVisibility() < accessVisibility
                accessVisibility = adtContainerVisibility;
                accessAdt = adtContainer;
            }
            container = adtContainer.getComposite();
        }
        return new AccessData(accessAdt, accessVisibility);
    }

    public static JVVisibility getImplicitAccessVisibility(DbJVClass adt) throws DbException {
        if (adt.getComposite() instanceof DbJVClass
                && ((DbJVClass) adt.getComposite()).isInterface())
            return JVVisibility.getInstance(JVVisibility.PUBLIC);
        return adt.getVisibility();
    }

    public static JVVisibility getVisibility(DbSemanticalObject semObj) throws DbException {
        if (semObj instanceof DbJVClass)
            return ((DbJVClass) semObj).getVisibility();
        if (semObj instanceof DbJVDataMember)
            return (JVVisibility) ((DbJVDataMember) semObj).getVisibility();
        if (semObj instanceof DbJVMethod)
            return (JVVisibility) ((DbJVMethod) semObj).getVisibility();
        if (semObj instanceof DbJVConstructor)
            return (JVVisibility) ((DbJVConstructor) semObj).getVisibility();
        return null;
    }

    // superMember.getComposite() must be an instanceof DbOOClass
    // superMember should be an instance of:
    //      * DbDataMember,
    //      * DbOperation (but not DbConstructor)
    //      * DbOOClass if DbOOClass.getComposite() instanceof DbOOClass (nested and inner classes)
    // superMember.getComposite() should be in ancestors tree of adt
    // note: if superMember.getComposite() is an interface, superMember is always inherited (implicitly public)
    public static boolean isSuperMemberInherited(DbJVClass adt, DbSemanticalObject superMember)
            throws DbException {
        boolean isInherited = false;
        DbJVClass superAdt = (DbJVClass) superMember.getComposite();
        if (superAdt.isInterface()) {
            isInherited = true;
        } else {
            int superMemberVisibility = AnyAdt.getVisibility(superMember).getValue();
            if (superMemberVisibility == JVVisibility.PUBLIC
                    || superMemberVisibility == JVVisibility.PROTECTED
                    || (superMemberVisibility == JVVisibility.DEFAULT && adt
                            .getCompositeOfType(DbSMSPackage.metaClass) == superAdt
                            .getCompositeOfType(DbSMSPackage.metaClass))) {
                isInherited = true;
            }
        }
        return isInherited;
    }

    // member.getComposite() must be an instanceof DbOOClass
    // member should be an instance of:
    //      * DbDataMember,
    //      * DbOperation (but not DbConstructor)
    //      * DbOOClass if DbOOClass.getComposite() instanceof DbOOClass (nested and inner classes)
    public static Vector foundDirectOverriddenHiddenMembers(DbSemanticalObject member,
            Vector circularAdts) throws DbException {
        return searchOverriddenHiddenMembersInBranch((DbJVClass) member.getComposite(), member,
                circularAdts);
    }

    private static Vector searchOverriddenHiddenMembersInBranch(DbJVClass startAdt,
            DbSemanticalObject member, Vector circularAdts) throws DbException {
        Vector overriddenMembers = new Vector();
        if (!(member instanceof DbJVDataMember || member instanceof DbJVClass || member instanceof DbJVMethod))
            return overriddenMembers;
        if (member instanceof DbJVDataMember) {
            DbOOAssociationEnd assocEnd = ((DbJVDataMember) member).getAssociationEnd();
            if (assocEnd != null && !assocEnd.isNavigable())
                return overriddenMembers;
        }
        DbJVClass memberAdt = (DbJVClass) member.getComposite();
        boolean found = false;
        if (startAdt != memberAdt) {
            String name = member.getName();
            DbEnumeration components = startAdt.getComponents().elements(member.getMetaClass());
            while (components.hasMoreElements()) {
                DbSemanticalObject component = (DbSemanticalObject) components.nextElement();
                if (!DbObject.valuesAreEqual(name, component.getName()))
                    continue;
                if (component instanceof DbJVDataMember) {
                    DbOOAssociationEnd assocEnd = ((DbJVDataMember) component).getAssociationEnd();
                    if (assocEnd != null && !assocEnd.isNavigable())
                        continue;
                } else if (component instanceof DbJVMethod) {
                    if (!((DbOOAbstractMethod) member)
                            .hasSameSignatureAs((DbOOAbstractMethod) component))
                        continue;
                }
                if (AnyAdt.isSuperMemberInherited(memberAdt, component)) {
                    found = true;
                    overriddenMembers.addElement(component);
                    break;
                }
            }
            components.close();
        }
        if (!found) {
            DbEnumeration ancestors = startAdt.getSuperInheritances().elements();
            while (ancestors.hasMoreElements()) {
                DbJVClass ancestor = (DbJVClass) ((DbOOInheritance) ancestors.nextElement())
                        .getSuperClass();
                if (!(circularAdts.contains(ancestor))) {
                    Vector ancestorOverriddenMembers = searchOverriddenHiddenMembersInBranch(
                            ancestor, member, circularAdts);
                    for (Enumeration e = ancestorOverriddenMembers.elements(); e.hasMoreElements();) {
                        Object overriddenMember = e.nextElement();
                        if (!(overriddenMembers.contains(overriddenMember))) {
                            overriddenMembers.addElement(overriddenMember);
                        }
                    }
                }
            }
            ancestors.close();
        }
        return overriddenMembers;
    }

    // build tree of ancestors or descendants, not including ancestors/descendants if circularity path
    public static Vector buildAncestorsTree(DbOOClass adt) throws DbException {
        return buildInheritanceTree(adt, new Vector(), new Vector(), DbOOClass.fSuperInheritances,
                DbOOInheritance.fSuperClass);
    }

    public static Vector buildDescendantsTree(DbOOClass adt) throws DbException {
        return buildInheritanceTree(adt, new Vector(), new Vector(), DbOOClass.fSubInheritances,
                DbOOInheritance.fSubClass);
    }

    private static Vector buildInheritanceTree(DbOOClass adt, Vector tree, Vector path,
            MetaField fInherit, MetaField fInheritClass) throws DbException {
        path.addElement(adt);
        DbEnumeration dbEnum = ((DbRelationN) adt.get(fInherit)).elements();
        while (dbEnum.hasMoreElements()) {
            DbOOClass adtInherit = (DbOOClass) dbEnum.nextElement().get(fInheritClass);
            if (!path.contains(adtInherit)) {
                if (!tree.contains(adtInherit)) {
                    tree.addElement(adtInherit);
                    buildInheritanceTree(adtInherit, tree, path, fInherit, fInheritClass);
                }
            } else {
                for (int i = path.indexOf(adtInherit); i < path.size(); i++)
                    tree.removeElement(path.elementAt(i));
            }
        }
        dbEnum.close();
        path.removeElement(adt);
        return tree;
    }

    // add to ancestors vector all ancestors of adt, including adt
    public static Collection findAncestors(DbOOClass adt, Collection ancestors) throws DbException {
        if (!ancestors.contains(adt)) {
            ancestors.add(adt);

            DbEnumeration dbEnum = adt.getSuperInheritances().elements();
            while (dbEnum.hasMoreElements()) {
                DbOOClass adtSuper = ((DbOOInheritance) dbEnum.nextElement()).getSuperClass();
                findAncestors(adtSuper, ancestors);
            }
            dbEnum.close();
        }
        return ancestors;
    }

    // add to types vector all adt types of the semObj members (parameter, field, method...)
    public static Collection findTypingAdts(DbObject semObj, Collection types) throws DbException {
        DbEnumeration dbEnum = semObj.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            DbObject dbo = dbEnum.nextElement();
            DbOOAdt type = null;
            if (dbo instanceof DbOODataMember)
                type = ((DbOODataMember) dbo).getType();
            else if (dbo instanceof DbOOMethod)
                type = ((DbOOMethod) dbo).getReturnType();
            else if (dbo instanceof DbOOParameter)
                type = ((DbOOParameter) dbo).getType();
            if (type != null && !types.contains(type)) {
                types.add(type);
                findTypingAdts(type, types);
            }
            findTypingAdts(dbo, types);
        }
        dbEnum.close();
        return types;
    }

    // add to exceptions vector all exception throwed by the adt operations (methods and constructor)
    public static Collection findThrowedExceptions(DbJVClass adt, Collection exceptions)
            throws DbException {
        DbEnumeration operations = adt.getComponents().elements(DbOOAbstractMethod.metaClass);
        while (operations.hasMoreElements()) {
            DbOOAbstractMethod operation = (DbOOAbstractMethod) operations.nextElement();
            MetaField fExceptions = (operation instanceof DbJVMethod ? DbJVMethod.fJavaExceptions
                    : DbJVConstructor.fJavaExceptions);
            DbEnumeration javaExceptions = ((DbRelationN) operation.get(fExceptions)).elements();
            while (javaExceptions.hasMoreElements()) {
                DbJVClass javaException = (DbJVClass) javaExceptions.nextElement();
                if (!exceptions.contains(javaException)) {
                    exceptions.add(javaException);
                    findThrowedExceptions(javaException, exceptions);
                }
            }
            javaExceptions.close();
        }
        operations.close();
        return exceptions;
    }

    // validate if an adt is a circularity adt by the hierarchy
    // add to the circularAdts vector all all adts in the circularity path
    public static Vector validateCircularity(DbOOClass adt, Vector circularAdts, Vector path,
            Vector validatedAdts, InheritanceCircularityListener listener) throws DbException {
        path.addElement(adt);
        if (!validatedAdts.contains(adt)) {
            validatedAdts.addElement(adt);

            DbEnumeration dbEnum = adt.getSuperInheritances().elements();
            while (dbEnum.hasMoreElements()) {
                DbOOClass adtSuper = ((DbOOInheritance) dbEnum.nextElement()).getSuperClass();
                if (path.contains(adtSuper)) {
                    Vector circularityPath = new Vector();
                    for (int i = path.indexOf(adtSuper); i < path.size(); i++) {
                        DbOOClass circularAdt = (DbOOClass) path.elementAt(i);
                        circularityPath.addElement(circularAdt);
                        if (!circularAdts.contains(circularAdt))
                            circularAdts.addElement(circularAdt);
                    }
                    if (listener != null)
                        listener.circularityPathFound(circularityPath);
                } else
                    validateCircularity(adtSuper, circularAdts, path, validatedAdts, listener);
            }
            dbEnum.close();
        }
        path.removeElement(adt);
        return circularAdts;
    }

    // return a SrVector of DefaultComparableElements
    // containing all members( including all inherited members)
    // of type "memberInstance" and sort it by name for fields and inner classes
    // and by signature for methods
    // note: METHOD include only methods, not constructors
    public static SrVector getAllMembers(DbJVClass adt, int memberInstance) throws DbException {
        SrVector allMembers = new SrVector();
        DbEnumeration dbEnum = adt.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            DbObject dbo = dbEnum.nextElement();
            switch (memberInstance) {
            case MEMBER_FIELD:
                if (dbo instanceof DbJVDataMember) {
                    DbOOAssociationEnd assocEnd = ((DbJVDataMember) dbo).getAssociationEnd();
                    if (assocEnd == null || assocEnd.isNavigable())
                        allMembers.addElement(new DefaultComparableElement(dbo,
                                ((DbSemanticalObject) dbo).getName()));
                }
                break;
            case MEMBER_INNER_CLASS:
                if (dbo instanceof DbJVClass)
                    allMembers.addElement(new DefaultComparableElement(dbo,
                            ((DbSemanticalObject) dbo).getName()));
                break;
            case MEMBER_METHOD:
                if (dbo instanceof DbJVMethod)
                    allMembers.addElement(new DefaultComparableElement(dbo, ((DbJVMethod) dbo)
                            .buildSignature(DbObject.LONG_FORM)));
                break;
            }
        }
        dbEnum.close();
        return getInheritedMembers(adt, allMembers, memberInstance);
    }

    // add all inheritedMembers (as DefaultComparableElement)
    // of type "memberInstance" (even if overridden)
    // to inheritedMembers and sort it by name or fields and inner classes
    // and by signature for methods
    public static SrVector getInheritedMembers(DbJVClass adt, SrVector inheritedMembers,
            int memberInstance) throws DbException {
        Vector ancestorsTree = buildAncestorsTree(adt);
        for (Enumeration enumeration = ancestorsTree.elements(); enumeration.hasMoreElements();) {
            DbJVClass superAdt = (DbJVClass) enumeration.nextElement();
            DbEnumeration components = superAdt.getComponents().elements();
            while (components.hasMoreElements()) {
                DbObject dbo = components.nextElement();
                switch (memberInstance) {
                case MEMBER_FIELD:
                    if (dbo instanceof DbJVDataMember
                            && isSuperMemberInherited(adt, (DbSemanticalObject) dbo)) {
                        DbOOAssociationEnd assocEnd = ((DbJVDataMember) dbo).getAssociationEnd();
                        if (assocEnd == null || assocEnd.isNavigable())
                            inheritedMembers.addElement(new DefaultComparableElement(dbo,
                                    ((DbSemanticalObject) dbo).getName()));
                    }
                    break;
                case MEMBER_INNER_CLASS:
                    if (dbo instanceof DbJVClass
                            && isSuperMemberInherited(adt, (DbSemanticalObject) dbo))
                        inheritedMembers.addElement(new DefaultComparableElement(dbo,
                                ((DbSemanticalObject) dbo).getName()));
                    break;
                case MEMBER_METHOD:
                    if (dbo instanceof DbJVMethod
                            && isSuperMemberInherited(adt, (DbSemanticalObject) dbo))
                        inheritedMembers.addElement(new DefaultComparableElement(dbo,
                                ((DbJVMethod) dbo).buildSignature(DbObject.LONG_FORM)));
                    break;
                }
            }
            components.close();
        }
        inheritedMembers.sort();
        return inheritedMembers;
    }

    // For an adt, process all methods of adt (including inherited methods) and set
    // hiding and overriding (overriding or hiding by multiple inheritance branches
    // is considered).
    // Return a vector of MethodsWithSameSignature (a vector of vectors).
    // After processing, a MethodsWithSameSignature object is a vector of Method objects,
    // grouping methods with same signature. For each Method object, overriding and
    // hiding status is to date.
    public static Vector getAbsoluteMethods(DbJVClass adt) throws DbException {
        Vector circularAdts = AnyAdt.validateCircularity(adt, new Vector(), new Vector(),
                new Vector(), null);
        SrVector allMethods = AnyAdt.getAllMembers(adt, AnyAdt.MEMBER_METHOD);
        Vector absoluteMethods = new Vector();
        Vector methodsWithSameSignature = new Vector();
        for (int i = 0; i < allMethods.size(); i++) {
            boolean isNextSame = false;
            DefaultComparableElement elem = (DefaultComparableElement) allMethods.elementAt(i);
            DbJVMethod method = (DbJVMethod) elem.object;
            if (i + 1 < allMethods.size()
                    && elem.compareTo((DefaultComparableElement) allMethods.elementAt(i + 1)) == 0) {
                methodsWithSameSignature.addElement(method);
                isNextSame = true;
            }
            if (!isNextSame) {
                methodsWithSameSignature.addElement(method);
                absoluteMethods.addElement(processMethodsWithSameSignature(
                        methodsWithSameSignature, circularAdts));
                methodsWithSameSignature = new Vector();
            }
        }
        return absoluteMethods;
    }

    // Process a vector of methods with the same signature of an adt.
    // Return a MethodsWithSameSignature object: a vector of Method containing
    // for an adt alls methods with the same signature, including inherited methods
    // and "real" methods of adt. For each Method object, status is set (overridden, hidden or
    // not overridden or hidden). Overriding and hiding by multiple inheritance branches
    // is considered. In a not valid case (because multiple inheritance branches),
    // a Method object has a status OVERRIDDEN or HIDDEN and overridingMethod is null.
    //
    private static MethodsWithSameSignature processMethodsWithSameSignature(
            Vector methodsWithSameSignature, Vector circularAdts) throws DbException {

        MethodsWithSameSignature methods = new MethodsWithSameSignature(methodsWithSameSignature);

        // set overriding or hiding inside a same inheritance branch
        for (Enumeration enumeration = methods.elements(); enumeration.hasMoreElements();) {
            DbJVMethod method = ((Method) enumeration.nextElement()).method;
            Vector hiddenOrOverridenMethods = foundDirectOverriddenHiddenMembers(method,
                    circularAdts);
            for (Enumeration e = hiddenOrOverridenMethods.elements(); e.hasMoreElements();) {
                DbJVMethod hiddenOrOverriddenMethod = (DbJVMethod) e.nextElement();
                if (methodsWithSameSignature.contains(hiddenOrOverriddenMethod)) {
                    Method inheritedMethod = methods.getMethod(hiddenOrOverriddenMethod);
                    inheritedMethod.overridingMethods.addElement(method);
                }
            }
        }

        // set overriding or hiding by multiple inheritance branches
        Vector methodsNotOverridden = methods.getNotOverriddenNotHiddenMethods();
        if (methodsNotOverridden.size() > 1) {
            for (Enumeration enumeration2 = methodsNotOverridden.elements(); enumeration2
                    .hasMoreElements();) {
                DbJVMethod method = ((Method) enumeration2.nextElement()).method;
                if (!(method.isAbstract() || ((DbJVClass) method.getComposite()).isInterface())) {
                    for (Enumeration e = methodsNotOverridden.elements(); e.hasMoreElements();) {
                        Method el = (Method) e.nextElement();
                        if (el.method.isAbstract()
                                || ((DbJVClass) el.method.getComposite()).isInterface()) {
                            el.overridingMethods.addElement(method);
                            el.directOverriding = false;
                        }
                    }
                }
            }
        }

        return methods;
    }

    public static final class Method {

        public DbJVMethod method;
        public Vector overridingMethods = new Vector(); // or hiding methods // vector of DbJVMethod
        public boolean directOverriding = true;

        public Method(DbJVMethod newMethod) {
            method = newMethod;
        }
    }

    // MethodsWithSameSignature: Vector class of Method class
    // This class is used to arrange in a vector all methods with the same signature,
    // including inherited methods and "direct" methods of adt.
    // After processing (processMethodsWithSameSignature() method):
    //    1/ this class contains for an adt alls
    //    2/ each method of Method class has its status (overridden, hidden or
    //    not overridden or hidden), including overriding and hiding
    //    by multiple inheritance branches.
    public static final class MethodsWithSameSignature extends Vector {

        public String signature = null;

        public MethodsWithSameSignature(Vector methodsWithSameSignature) throws DbException {
            super();
            for (Enumeration withSameSignature = methodsWithSameSignature.elements(); withSameSignature
                    .hasMoreElements();) {
                DbJVMethod method = (DbJVMethod) withSameSignature.nextElement();
                addMethod(method);
                if (signature == null)
                    signature = method.buildSignature(DbObject.LONG_FORM);
            }
        }

        public final Method addMethod(DbJVMethod method) {
            Method meth = new Method(method);
            addElement(meth);
            return meth;
        }

        public final Method getMethod(DbJVMethod methodToFind) {
            for (Enumeration enumeration = this.elements(); enumeration.hasMoreElements();) {
                Method elem1 = (Method) enumeration.nextElement();
                if (elem1.method == methodToFind)
                    return elem1;
            }
            return null;
        }

        // return not overridden (by an instance method) static methods of a DbClass
        // (not of a DbInterface)
        public final Vector getNotOverriddenStaticMethods() throws DbException {
            Vector notOverriddenStaticMethods = new Vector();
            for (Enumeration e2 = this.elements(); e2.hasMoreElements();) {
                Method elem2 = (Method) e2.nextElement();
                if (elem2.method.isStatic()
                        && !((DbJVClass) elem2.method.getComposite()).isInterface()) {
                    boolean overridden = false;
                    for (Enumeration enumeration = elem2.overridingMethods.elements(); enumeration
                            .hasMoreElements();) {
                        DbJVMethod overridingMethod = (DbJVMethod) enumeration.nextElement();
                        if (!overridingMethod.isStatic()) {
                            overridden = true;
                            break;
                        }
                    }
                    if (!overridden)
                        notOverriddenStaticMethods.addElement(elem2);
                }
            }
            return notOverriddenStaticMethods;
        }

        // return not overridden and not hidden non-static methods
        public final Vector getNotOverriddenNotHiddenInstanceMethods() throws DbException {
            Vector notOverriddenMethods = new Vector();
            for (Enumeration e3 = this.elements(); e3.hasMoreElements();) {
                Method elem3 = (Method) e3.nextElement();
                if ((!elem3.method.isStatic() || ((DbJVClass) elem3.method.getComposite())
                        .isInterface())
                        && elem3.overridingMethods.size() == 0) {
                    notOverriddenMethods.addElement(elem3);
                }
            }
            return notOverriddenMethods;
        }

        // return not overridden and not hidden (non-static or static) methods
        public final Vector getNotOverriddenNotHiddenMethods() throws DbException {
            Vector notOverriddenMethods = new Vector();
            for (Enumeration e3 = this.elements(); e3.hasMoreElements();) {
                Method elem3 = (Method) e3.nextElement();
                if (elem3.overridingMethods.size() == 0)
                    notOverriddenMethods.addElement(elem3);
            }
            return notOverriddenMethods;
        }

        public final Vector getOverriddenHiddenMethodsBy(DbJVMethod method) {
            Vector overridenHiddenMethods = new Vector();
            for (Enumeration e4 = this.elements(); e4.hasMoreElements();) {
                Method elem4 = (Method) e4.nextElement();
                if (elem4.overridingMethods.contains(method))
                    overridenHiddenMethods.addElement(elem4);
            }
            return overridenHiddenMethods; // vector of Methods
        }

    }

}
