/*************************************************************************

Copyright (C) 2009 by neosapiens inc.

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

You can reach neosapiens inc. at: 

neosapiens inc.
1236 Gaudias-Petitclerc
Qu&eacute;bec, Qc, G1Y 3G2
CANADA
Telephone: 418-561-8403
Fax: 418-650-2375
http://www.neosapiens.com/
Email: marco.savard@neosapiens.com
       gino.pelletier@neosapiens.com

 **********************************************************************/

package com.neosapiens.plugins.codegen.wrappers;

import java.util.ArrayList;
import java.util.List;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.util.Extensibility;
import org.modelsphere.sms.oo.db.DbOOAssociationEnd;
import org.modelsphere.sms.oo.java.db.*;
import org.modelsphere.sms.oo.java.db.srtypes.JVVisibility;

public class DbClassWrapper extends DbObjectWrapper {
    private DbJVClass m_class;

    public DbClassWrapper(DbJVClass claz) {
        m_class = claz;
    }

    public boolean isClass() throws DbException {
        boolean isInterface = m_class.isInterface();
        return !(isInterface) && !(isEnum());
    }

    public boolean isInterface() throws DbException {
        return (m_class.isInterface());
    }

    public boolean isEnum() throws DbException {
        DbSMSStereotype stereotype = m_class.getUmlStereotype();
        String name = (stereotype == null) ? null : stereotype.getName();
        boolean isEnum = Extensibility.ENUMERATION.equals(name);
        return isEnum;
    }

    public boolean isDatatype() throws DbException {
        DbSMSStereotype stereotype = m_class.getUmlStereotype();
        String name = (stereotype == null) ? null : stereotype.getName();
        boolean isEnum = "dataType".equals(name);
        return isEnum;
    }

    public DbPackageWrapper getPackage() throws DbException {
        DbJVPackage p = (DbJVPackage) m_class.getCompositeOfType(DbJVPackage.metaClass);
        DbPackageWrapper pack = new DbPackageWrapper(p);
        return pack;
    }

    public List<String> getJavaVisibility() throws DbException {
        List<String> list = new ArrayList<String>();
        JVVisibility visib = m_class.getVisibility();
        list.add(toString(visib));
        return list;
    }

    public List<String> getJavaModifiers() throws DbException {
        List<String> list = new ArrayList<String>();

        if (m_class.isStatic()) {
            list.add("static");
        }

        //cannot be both abstract and final
        if (m_class.isAbstract()) {
            list.add("abstract");
        } else if (m_class.isFinal()) {
            list.add("final");
        } //end if

        if (m_class.isStrictfp()) {
            list.add("strictfp");
        }

        return list;
    }
    
    public String getEmfAbstract() throws DbException {
    	String emfAbstract = m_class.isAbstract() ? " abstract=\"true\"" : "";
    	return emfAbstract;
    }

    public StringWrapper getName() throws DbException {
        String s = m_class.getName();
        return new StringWrapper(s);
    }

    public List<String> getSuperClasses() throws DbException {
        List<String> superclasses = new ArrayList<String>();

        DbRelationN relN = m_class.getSuperInheritances();
        DbEnumeration enu = relN.elements(DbJVInheritance.metaClass);
        while (enu.hasMoreElements()) {
            DbJVInheritance inher = (DbJVInheritance) enu.nextElement();
            DbJVClass superType = (DbJVClass) inher.getSuperClass();
            if (!superType.isInterface()) {
                superclasses.add(superType.getName());
            }
        } //end while
        enu.close();

        return superclasses;
    } //end getSuperInterfaces()

    public List<String> getSuperInterfaces() throws DbException {
        List<String> superinterfaces = new ArrayList<String>();

        DbRelationN relN = m_class.getSuperInheritances();
        DbEnumeration enu = relN.elements(DbJVInheritance.metaClass);
        while (enu.hasMoreElements()) {
            DbJVInheritance inher = (DbJVInheritance) enu.nextElement();
            DbJVClass superType = (DbJVClass) inher.getSuperClass();
            if (superType.isInterface()) {
                superinterfaces.add(superType.getName());
            }
        } //end while
        enu.close();

        return superinterfaces;
    } //end getSuperInterfaces()

    public List<String> getSupertypes() throws DbException {
        List<String> supertypes = new ArrayList<String>();

        DbRelationN relN = m_class.getSuperInheritances();
        DbEnumeration enu = relN.elements(DbJVInheritance.metaClass);
        while (enu.hasMoreElements()) {
            DbJVInheritance inher = (DbJVInheritance) enu.nextElement();
            DbJVClass superType = (DbJVClass) inher.getSuperClass();
            supertypes.add(superType.getName());
        } //end while
        enu.close();

        return supertypes;
    } //end getSupertypes()

    public List<DbFieldWrapper> getFields() throws DbException {
        List<DbFieldWrapper> fields = new ArrayList<DbFieldWrapper>();

        //for each field
        DbRelationN relN = m_class.getComponents();
        DbEnumeration enu = relN.elements(DbJVDataMember.metaClass);
        while (enu.hasMoreElements()) {
            DbJVDataMember o = (DbJVDataMember) enu.nextElement();
            DbOOAssociationEnd end = o.getAssociationEnd();
            if (end != null) {
                if (end.isNavigable()) {
                    DbFieldWrapper field = new DbFieldWrapper(o);
                    fields.add(field);
                } //end if
            } else {
                DbFieldWrapper field = new DbFieldWrapper(o);
                fields.add(field);
            } //end while
        } //end while
        enu.close();

        return fields;
    } //end getFields()

    public List<DbFieldWrapper> getAttributes() throws DbException {
        List<DbFieldWrapper> attributes = new ArrayList<DbFieldWrapper>();

        //for each attribute field
        DbRelationN relN = m_class.getComponents();
        DbEnumeration enu = relN.elements(DbJVDataMember.metaClass);
        while (enu.hasMoreElements()) {
            DbJVDataMember o = (DbJVDataMember) enu.nextElement();
            DbOOAssociationEnd end = o.getAssociationEnd();
            if (end == null) {
                DbFieldWrapper field = new DbFieldWrapper(o);
                attributes.add(field);
            } //end if
        } //end while
        enu.close();

        return attributes;
    } //end getAttributes()

    public List<DbFieldWrapper> getReferences() throws DbException {
        List<DbFieldWrapper> references = new ArrayList<DbFieldWrapper>();

        //for each reference field
        DbRelationN relN = m_class.getComponents();
        DbEnumeration enu = relN.elements(DbJVDataMember.metaClass);
        while (enu.hasMoreElements()) {
            DbJVDataMember o = (DbJVDataMember) enu.nextElement();
            DbOOAssociationEnd end = o.getAssociationEnd();
            if ((end != null) && (end.isNavigable())) {
                DbFieldWrapper field = new DbFieldWrapper(o);
                references.add(field);
            } //end if
        } //end while
        enu.close();

        return references;
    } //end getReferences()

    public List<DbConstructorWrapper> getConstructors() throws DbException {
        List<DbConstructorWrapper> constructors = new ArrayList<DbConstructorWrapper>();

        //for each field
        DbRelationN relN = m_class.getComponents();
        DbEnumeration enu = relN.elements(DbJVConstructor.metaClass);
        while (enu.hasMoreElements()) {
            DbJVConstructor constructor = (DbJVConstructor) enu.nextElement();
            DbConstructorWrapper cw = new DbConstructorWrapper(constructor);
            constructors.add(cw);
        } //end while
        enu.close();

        return constructors;
    } //end getMethods()

    public List<DbMethodWrapper> getMethods() throws DbException {
        List<DbMethodWrapper> methods = new ArrayList<DbMethodWrapper>();

        //for each field
        DbRelationN relN = m_class.getComponents();
        DbEnumeration enu = relN.elements(DbJVMethod.metaClass);
        while (enu.hasMoreElements()) {
            DbJVMethod method = (DbJVMethod) enu.nextElement();
            DbMethodWrapper mw = new DbMethodWrapper(method);
            methods.add(mw);
        } //end while
        enu.close();

        return methods;
    } //end getMethods()

    public List<DbClassWrapper> getInnerClasses() throws DbException {
        List<DbClassWrapper> inners = new ArrayList<DbClassWrapper>();

        //for each field
        DbRelationN relN = m_class.getComponents();
        DbEnumeration enu = relN.elements(DbJVClass.metaClass);
        while (enu.hasMoreElements()) {
            DbJVClass c = (DbJVClass) enu.nextElement();
            DbClassWrapper inner = new DbClassWrapper(c);
            inners.add(inner);
        } //end while
        enu.close();

        return inners;
    } //end getMethods()

    //
    // private methods
    //

} //end ProjectWrapper
