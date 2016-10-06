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
package org.modelsphere.sms.oo.java.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.oo.java.db.srtypes.*;
import org.modelsphere.sms.oo.java.international.LocaleMgr;
import org.modelsphere.sms.oo.db.*;
import org.modelsphere.sms.oo.db.srtypes.*;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import javax.swing.Icon;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.be.db.DbBEStore;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/oo/java/db/DbJVClassModel.html">DbJVClassModel</A>,
 * <A HREF="../../../../../../org/modelsphere/sms/oo/java/db/DbJVPackage.html">DbJVPackage</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/oo/db/DbOOClass.html">DbOOClass</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/oo/db/DbOODataMember.html">DbOODataMember</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/oo/db/DbOOClass.html">DbOOClass</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/oo/db/DbOOOperation.html">DbOOOperation</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/oo/db/DbOOInheritance.html">DbOOInheritance</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/oo/db/DbOOAssociation.html">DbOOAssociation</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbJVClass extends DbOOClass {

    //Meta

    public static final MetaRelationN fThrowingMethods = new MetaRelationN(LocaleMgr.db
            .getString("throwingMethods"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fThrowingConstructors = new MetaRelationN(LocaleMgr.db
            .getString("throwingConstructors"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fImports = new MetaRelationN(LocaleMgr.db
            .getString("imports"), 0, MetaRelationN.cardN);
    public static final MetaRelation1 fReferringProject = new MetaRelation1(LocaleMgr.db
            .getString("referringProject"), 0);
    public static final MetaField fStereotype = new MetaField(LocaleMgr.db.getString("stereotype"));
    public static final MetaField fVisibility = new MetaField(LocaleMgr.db.getString("visibility"));
    public static final MetaField fStatic = new MetaField(LocaleMgr.db.getString("static"));
    public static final MetaField fFinal = new MetaField(LocaleMgr.db.getString("final"));
    public static final MetaField fCollection = new MetaField(LocaleMgr.db.getString("collection"));
    public static final MetaRelation1 fCompilationUnit = new MetaRelation1(LocaleMgr.db
            .getString("compilationUnit"), 0);
    public static final MetaField fStrictfp = new MetaField(LocaleMgr.db.getString("strictfp"));
    public static final MetaRelationN fDbBEStores = new MetaRelationN(LocaleMgr.db
            .getString("dbBEStores"), 0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbJVClass"),
            DbJVClass.class, new MetaField[] { fThrowingMethods, fThrowingConstructors, fImports,
                    fReferringProject, fStereotype, fVisibility, fStatic, fFinal, fCollection,
                    fCompilationUnit, fStrictfp, fDbBEStores }, MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbOOClass.metaClass);
            metaClass.setIcon("dbjvclass.gif");

            fThrowingMethods.setJField(DbJVClass.class.getDeclaredField("m_throwingMethods"));
            fThrowingConstructors.setJField(DbJVClass.class
                    .getDeclaredField("m_throwingConstructors"));
            fImports.setJField(DbJVClass.class.getDeclaredField("m_imports"));
            fImports.setFlags(MetaField.HUGE_RELN);
            fReferringProject.setJField(DbJVClass.class.getDeclaredField("m_referringProject"));
            fReferringProject.setFlags(MetaField.NO_WRITE_CHECK);
            fReferringProject.setVisibleInScreen(false);
            fStereotype.setJField(DbJVClass.class.getDeclaredField("m_stereotype"));
            fStereotype.setScreenOrder("<abstract");
            fVisibility.setJField(DbJVClass.class.getDeclaredField("m_visibility"));
            fVisibility.setScreenOrder("<abstract");
            fStatic.setJField(DbJVClass.class.getDeclaredField("m_static"));
            fFinal.setJField(DbJVClass.class.getDeclaredField("m_final"));
            fCollection.setJField(DbJVClass.class.getDeclaredField("m_collection"));
            fCompilationUnit.setJField(DbJVClass.class.getDeclaredField("m_compilationUnit"));
            fCompilationUnit.setFlags(MetaField.INTEGRABLE_BY_NAME);
            fStrictfp.setJField(DbJVClass.class.getDeclaredField("m_strictfp"));
            fDbBEStores.setJField(DbJVClass.class.getDeclaredField("m_dbBEStores"));

            fThrowingMethods.setOppositeRel(DbJVMethod.fJavaExceptions);
            fThrowingConstructors.setOppositeRel(DbJVConstructor.fJavaExceptions);
            fReferringProject.setOppositeRel(DbSMSProject.fDefaultCollectionType);
            fCompilationUnit.setOppositeRel(DbJVCompilationUnit.fClasses);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    private static Icon classIcon = org.modelsphere.jack.graphic.GraphicUtil.loadIcon(
            DbJVClass.class, "resources/dbjvclass.gif");
    private static Icon interfaceIcon = org.modelsphere.jack.graphic.GraphicUtil.loadIcon(
            DbJVClass.class, "resources/dbjvinterface.gif");
    private static Icon exceptionIcon = org.modelsphere.jack.graphic.GraphicUtil.loadIcon(
            DbJVClass.class, "resources/dbjvexception.gif");
    static final long serialVersionUID = 0;

    //Instance variables
    DbRelationN m_throwingMethods;
    DbRelationN m_throwingConstructors;
    DbRelationN m_imports;
    DbSMSProject m_referringProject;
    JVClassCategory m_stereotype;
    JVVisibility m_visibility;
    boolean m_static;
    boolean m_final;
    boolean m_collection;
    DbJVCompilationUnit m_compilationUnit;
    boolean m_strictfp;
    DbRelationN m_dbBEStores;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbJVClass() {
    }

    /**
     * Creates an instance of DbJVClass.
     * 
     * @param composite
     *            org.modelsphere.jack.baseDb.db.DbObject
     * @param stereotype
     *            org.modelsphere.sms.oo.java.db.srtypes.JVClassCategory
     **/
    public DbJVClass(DbObject composite, JVClassCategory stereotype) throws DbException {
        super(composite);
        setDefaultInitialValues();
        setStereotype(stereotype);
        switch (stereotype.getValue()) {
        case JVClassCategory.CLASS:
            setName(LocaleMgr.misc.getString("Class"));
            break;
        case JVClassCategory.INTERFACE:
            setName(LocaleMgr.misc.getString("Interface"));
            break;
        case JVClassCategory.EXCEPTION:
            setName(LocaleMgr.misc.getString("Exception"));
            break;
        }
    }

    private void setDefaultInitialValues() throws DbException {
        setVisibility(JVVisibility.getInstance(JVVisibility.PUBLIC));
    }

    /**
     * @return boolean
     **/
    public final boolean isClass() throws DbException {
        return (getStereotype().getValue() == JVClassCategory.CLASS);
    }

    /**
     * @return boolean
     **/
    public final boolean isInterface() throws DbException {
        return (getStereotype().getValue() == JVClassCategory.INTERFACE);
    }

    /**
     * @return boolean
     **/
    public final boolean isException() throws DbException {
        return (getStereotype().getValue() == JVClassCategory.EXCEPTION);
    }

    /**
     * @param name
     *            java.lang.String
     **/
    public final void setName(String name) throws DbException {
        String oldName = getName();
        if (DbObject.valuesAreEqual(name, oldName))
            return;
        super.setName(name);
        if (oldName == null)
            return;

        DbJVCompilationUnit compilUnit = getCompilationUnit();
        if (compilUnit != null && compilUnit.getName().equals(oldName + ".java"))
            compilUnit.setName(name + ".java");

        DbEnumeration dbEnum = getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            DbObject comp = dbEnum.nextElement();
            if (comp instanceof DbOODataMember) {
                DbOOAssociationEnd assocEnd = ((DbOODataMember) comp).getAssociationEnd();
                if (assocEnd == null)
                    continue;
                DbOODataMember oppMember = assocEnd.getOppositeEnd().getAssociationMember();
                if (oppMember.getName().equalsIgnoreCase(oldName))
                    oppMember.setName(Character.toLowerCase(name.charAt(0)) + name.substring(1));
            } else if (comp instanceof DbOOConstructor)
                ((DbOOConstructor) comp).setName(name);
        }
        dbEnum.close();
    }

    /**
     * @param value
     *            org.modelsphere.sms.oo.java.db.DbJVCompilationUnit
     **/
    public final void setCompilationUnit(DbJVCompilationUnit value) throws DbException {
        if (value != null) {
            //an inner class cannot have a compilation unit
            if (getComposite() instanceof DbJVClass)
                return;
            if (value.getClasses().size() == 0)
                value.setName(getName() + ".java");
        }
        basicSet(fCompilationUnit, value);
    }

    /**
     * @param form
     *            int
     * @return icon
     **/
    public final Icon getSemanticalIcon(int form) throws DbException {
        switch (getStereotype().getValue()) {
        case JVClassCategory.CLASS:
            return classIcon;
        case JVClassCategory.INTERFACE:
            return interfaceIcon;
        case JVClassCategory.EXCEPTION:
            return exceptionIcon;
        }
        return null;
    }

    /**
     * @return int
     **/
    protected final int getFeatureSet() {
        return SMSFilter.JAVA;

    }

    //Setters

    /**
     * Adds an element to or removes an element from the list of throwing methods associated to a
     * DbJVClass's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setThrowingMethods(DbJVMethod value, int op) throws DbException {
        setRelationNN(fThrowingMethods, value, op);
    }

    /**
     * Adds an element to the list of throwing methods associated to a DbJVClass's instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToThrowingMethods(DbJVMethod value) throws DbException {
        setThrowingMethods(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of throwing methods associated to a DbJVClass's instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromThrowingMethods(DbJVMethod value) throws DbException {
        setThrowingMethods(value, Db.REMOVE_FROM_RELN);
    }

    /**
     * Adds an element to or removes an element from the list of throwing constructors associated to
     * a DbJVClass's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setThrowingConstructors(DbJVConstructor value, int op) throws DbException {
        setRelationNN(fThrowingConstructors, value, op);
    }

    /**
     * Adds an element to the list of throwing constructors associated to a DbJVClass's instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToThrowingConstructors(DbJVConstructor value) throws DbException {
        setThrowingConstructors(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of throwing constructors associated to a DbJVClass's
     * instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromThrowingConstructors(DbJVConstructor value) throws DbException {
        setThrowingConstructors(value, Db.REMOVE_FROM_RELN);
    }

    /**
     * Sets the project object associated to a DbJVClass's instance.
     * 
     * @param value
     *            the project object to be associated
     **/
    public final void setReferringProject(DbSMSProject value) throws DbException {
        basicSet(fReferringProject, value);
    }

    /**
     * Sets the "classifier type" property of a DbJVClass's instance.
     * 
     * @param value
     *            the "classifier type" property
     **/
    public final void setStereotype(JVClassCategory value) throws DbException {
        basicSet(fStereotype, value);
    }

    /**
     * Sets the "visibility" property of a DbJVClass's instance.
     * 
     * @param value
     *            the "visibility" property
     **/
    public final void setVisibility(JVVisibility value) throws DbException {
        basicSet(fVisibility, value);
    }

    /**
     * Sets the "static" property of a DbJVClass's instance.
     * 
     * @param value
     *            the "static" property
     **/
    public final void setStatic(Boolean value) throws DbException {
        basicSet(fStatic, value);
    }

    /**
     * Sets the "final" property of a DbJVClass's instance.
     * 
     * @param value
     *            the "final" property
     **/
    public final void setFinal(Boolean value) throws DbException {
        basicSet(fFinal, value);
    }

    /**
     * Sets the "collection" property of a DbJVClass's instance.
     * 
     * @param value
     *            the "collection" property
     **/
    public final void setCollection(Boolean value) throws DbException {
        basicSet(fCollection, value);
    }

    /**
     * Sets the "strictfp" property of a DbJVClass's instance.
     * 
     * @param value
     *            the "strictfp" property
     **/
    public final void setStrictfp(Boolean value) throws DbException {
        basicSet(fStrictfp, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fThrowingMethods)
                setThrowingMethods((DbJVMethod) value, Db.ADD_TO_RELN);
            else if (metaField == fThrowingConstructors)
                setThrowingConstructors((DbJVConstructor) value, Db.ADD_TO_RELN);
            else if (metaField == fImports)
                ((DbJVImport) value).setImportedObject(this);
            else if (metaField == fCompilationUnit)
                setCompilationUnit((DbJVCompilationUnit) value);
            else if (metaField == fDbBEStores)
                ((DbBEStore) value).setDbJVClass(this);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        if (relation == fThrowingMethods)
            setThrowingMethods((DbJVMethod) neighbor, op);
        else if (relation == fThrowingConstructors)
            setThrowingConstructors((DbJVConstructor) neighbor, op);
        else
            super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the list of throwing methods associated to a DbJVClass's instance.
     * 
     * @return the list of throwing methods.
     **/
    public final DbRelationN getThrowingMethods() throws DbException {
        return (DbRelationN) get(fThrowingMethods);
    }

    /**
     * Gets the list of throwing constructors associated to a DbJVClass's instance.
     * 
     * @return the list of throwing constructors.
     **/
    public final DbRelationN getThrowingConstructors() throws DbException {
        return (DbRelationN) get(fThrowingConstructors);
    }

    /**
     * Gets the list of imports associated to a DbJVClass's instance.
     * 
     * @return the list of imports.
     **/
    public final DbRelationN getImports() throws DbException {
        return (DbRelationN) get(fImports);
    }

    /**
     * Gets the project object associated to a DbJVClass's instance.
     * 
     * @return the project object
     **/
    public final DbSMSProject getReferringProject() throws DbException {
        return (DbSMSProject) get(fReferringProject);
    }

    /**
     * Gets the "classifier type" of a DbJVClass's instance.
     * 
     * @return the "classifier type"
     **/
    public final JVClassCategory getStereotype() throws DbException {
        return (JVClassCategory) get(fStereotype);
    }

    /**
     * Gets the "visibility" of a DbJVClass's instance.
     * 
     * @return the "visibility"
     **/
    public final JVVisibility getVisibility() throws DbException {
        return (JVVisibility) get(fVisibility);
    }

    /**
     * Gets the "static" property's Boolean value of a DbJVClass's instance.
     * 
     * @return the "static" property's Boolean value
     * @deprecated use isStatic() method instead
     **/
    public final Boolean getStatic() throws DbException {
        return (Boolean) get(fStatic);
    }

    /**
     * Tells whether a DbJVClass's instance is static or not.
     * 
     * @return boolean
     **/
    public final boolean isStatic() throws DbException {
        return getStatic().booleanValue();
    }

    /**
     * Gets the "final" property's Boolean value of a DbJVClass's instance.
     * 
     * @return the "final" property's Boolean value
     * @deprecated use isFinal() method instead
     **/
    public final Boolean getFinal() throws DbException {
        return (Boolean) get(fFinal);
    }

    /**
     * Tells whether a DbJVClass's instance is final or not.
     * 
     * @return boolean
     **/
    public final boolean isFinal() throws DbException {
        return getFinal().booleanValue();
    }

    /**
     * Gets the "collection" property's Boolean value of a DbJVClass's instance.
     * 
     * @return the "collection" property's Boolean value
     * @deprecated use isCollection() method instead
     **/
    public final Boolean getCollection() throws DbException {
        return (Boolean) get(fCollection);
    }

    /**
     * Tells whether a DbJVClass's instance is collection or not.
     * 
     * @return boolean
     **/
    public final boolean isCollection() throws DbException {
        return getCollection().booleanValue();
    }

    /**
     * Gets the compilation unit object associated to a DbJVClass's instance.
     * 
     * @return the compilation unit object
     **/
    public final DbJVCompilationUnit getCompilationUnit() throws DbException {
        return (DbJVCompilationUnit) get(fCompilationUnit);
    }

    /**
     * Gets the "strictfp" property's Boolean value of a DbJVClass's instance.
     * 
     * @return the "strictfp" property's Boolean value
     * @deprecated use isStrictfp() method instead
     **/
    public final Boolean getStrictfp() throws DbException {
        return (Boolean) get(fStrictfp);
    }

    /**
     * Tells whether a DbJVClass's instance is strictfp or not.
     * 
     * @return boolean
     **/
    public final boolean isStrictfp() throws DbException {
        return getStrictfp().booleanValue();
    }

    /**
     * Gets the list of roles associated to a DbJVClass's instance.
     * 
     * @return the list of roles.
     **/
    public final DbRelationN getDbBEStores() throws DbException {
        return (DbRelationN) get(fDbBEStores);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
