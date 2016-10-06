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
package org.modelsphere.sms.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.java.db.*;
import org.modelsphere.sms.or.db.*;
import org.modelsphere.sms.oo.db.*;
import org.modelsphere.sms.be.db.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.plugins.TargetSystem;
import org.modelsphere.sms.plugins.TargetSystemManager;
import org.modelsphere.sms.be.db.srtypes.BEZoneStereotype;
import org.modelsphere.sms.db.util.Extensibility;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A HREF="../../../../org/modelsphere/sms/db/DbSMSStyle.html">DbSMSStyle</A>,
 * <A HREF="../../../../org/modelsphere/sms/db/DbSMSTargetSystem.html">DbSMSTargetSystem</A>, <A
 * HREF="../../../../org/modelsphere/sms/oo/java/db/DbJVClassModel.html">DbJVClassModel</A>, <A
 * HREF=
 * "../../../../org/modelsphere/sms/db/DbSMSUserDefinedPackage.html">DbSMSUserDefinedPackage</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/db/DbORDataModel.html">DbORDataModel</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSBuiltInTypeNode.html">DbSMSBuiltInTypeNode</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/db/DbORCommonItemModel.html">DbORCommonItemModel</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/db/DbORDomainModel.html">DbORDomainModel</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/db/DbORDatabase.html">DbORDatabase</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/db/DbOROperationLibrary.html">DbOROperationLibrary</A>,
 * <A HREF="../../../../org/modelsphere/sms/db/DbSMSNotation.html">DbSMSNotation</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/db/DbORUserNode.html">DbORUserNode</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSLinkModel.html">DbSMSLinkModel</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSUmlExtensibility.html">DbSMSUmlExtensibility</A>,
 * <A HREF="../../../../org/modelsphere/sms/be/db/DbBEModel.html">DbBEModel</A>, <A
 * HREF="../../../../org/modelsphere/jack/baseDb/db/DbUDF.html">DbUDF</A>.<br>
 **/
public final class DbSMSProject extends DbProject {

    //Meta

    public static final MetaRelation1 fDefaultCollectionType = new MetaRelation1(LocaleMgr.db
            .getString("defaultCollectionType"), 0);
    public static final MetaRelation1 fOrDefaultStyle = new MetaRelation1(LocaleMgr.db
            .getString("orDefaultStyle"), 1);
    public static final MetaRelation1 fOoDefaultStyle = new MetaRelation1(LocaleMgr.db
            .getString("ooDefaultStyle"), 1);
    public static final MetaRelation1 fOrDefaultNotation = new MetaRelation1(LocaleMgr.db
            .getString("orDefaultNotation"), 1);
    public static final MetaRelation1 fOrDefaultDomainStyle = new MetaRelation1(LocaleMgr.db
            .getString("orDefaultDomainStyle"), 1);
    public static final MetaRelation1 fOrDefaultCommonItemStyle = new MetaRelation1(LocaleMgr.db
            .getString("orDefaultCommonItemStyle"), 1);
    public static final MetaRelation1 fDefaultLinkModel = new MetaRelation1(LocaleMgr.db
            .getString("defaultLinkModel"), 0);
    public static final MetaRelation1 fBeDefaultStyle = new MetaRelation1(LocaleMgr.db
            .getString("beDefaultStyle"), 1);
    public static final MetaRelation1 fBeDefaultNotation = new MetaRelation1(LocaleMgr.db
            .getString("beDefaultNotation"), 1);
    public static final MetaField fIsLocked = new MetaField(LocaleMgr.db.getString("isLocked"));
    public static final MetaRelation1 fErDefaultNotation = new MetaRelation1(LocaleMgr.db
            .getString("erDefaultNotation"), 1);
    public static final MetaRelation1 fErDefaultStyle = new MetaRelation1(LocaleMgr.db
            .getString("erDefaultStyle"), 1);
    public static final MetaField fUserTargetID = new MetaField(LocaleMgr.db
            .getString("userTargetID"));
    public static final MetaField fRecentModificationDisplay = new MetaField(LocaleMgr.db
            .getString("recentModificationDisplay"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbSMSProject"),
            DbSMSProject.class, new MetaField[] { fDefaultCollectionType, fOrDefaultStyle,
                    fOoDefaultStyle, fOrDefaultNotation, fOrDefaultDomainStyle,
                    fOrDefaultCommonItemStyle, fDefaultLinkModel, fBeDefaultStyle,
                    fBeDefaultNotation, fIsLocked, fErDefaultNotation, fErDefaultStyle,
                    fUserTargetID, fRecentModificationDisplay }, MetaClass.ACCESS_CTL
                    | MetaClass.CLUSTER_ROOT | MetaClass.NAMING_ROOT);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbProject.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbSMSStyle.metaClass,
                    DbSMSTargetSystem.metaClass, DbJVClassModel.metaClass,
                    DbSMSUserDefinedPackage.metaClass, DbORDataModel.metaClass,
                    DbSMSBuiltInTypeNode.metaClass, DbORCommonItemModel.metaClass,
                    DbORDomainModel.metaClass, DbORDatabase.metaClass,
                    DbOROperationLibrary.metaClass, DbSMSNotation.metaClass,
                    DbORUserNode.metaClass, DbSMSLinkModel.metaClass,
                    DbSMSUmlExtensibility.metaClass, DbBEModel.metaClass });
            metaClass.setIcon("dbsmsproject.gif");

            fDefaultCollectionType.setJField(DbSMSProject.class
                    .getDeclaredField("m_defaultCollectionType"));
            fDefaultCollectionType.setRendererPluginName("DbProjectDefaultCollectionType");
            fOrDefaultStyle.setJField(DbSMSProject.class.getDeclaredField("m_orDefaultStyle"));
            fOrDefaultStyle.setVisibleInScreen(false);
            fOoDefaultStyle.setJField(DbSMSProject.class.getDeclaredField("m_ooDefaultStyle"));
            fOoDefaultStyle.setVisibleInScreen(false);
            fOrDefaultNotation
                    .setJField(DbSMSProject.class.getDeclaredField("m_orDefaultNotation"));
            fOrDefaultNotation.setVisibleInScreen(false);
            fOrDefaultDomainStyle.setJField(DbSMSProject.class
                    .getDeclaredField("m_orDefaultDomainStyle"));
            fOrDefaultDomainStyle.setVisibleInScreen(false);
            fOrDefaultCommonItemStyle.setJField(DbSMSProject.class
                    .getDeclaredField("m_orDefaultCommonItemStyle"));
            fOrDefaultCommonItemStyle.setVisibleInScreen(false);
            fDefaultLinkModel.setJField(DbSMSProject.class.getDeclaredField("m_defaultLinkModel"));
            fBeDefaultStyle.setJField(DbSMSProject.class.getDeclaredField("m_beDefaultStyle"));
            fBeDefaultStyle.setVisibleInScreen(false);
            fBeDefaultNotation
                    .setJField(DbSMSProject.class.getDeclaredField("m_beDefaultNotation"));
            fBeDefaultNotation.setVisibleInScreen(false);
            fIsLocked.setJField(DbSMSProject.class.getDeclaredField("m_isLocked"));
            fErDefaultNotation
                    .setJField(DbSMSProject.class.getDeclaredField("m_erDefaultNotation"));
            fErDefaultNotation.setVisibleInScreen(false);
            fErDefaultStyle.setJField(DbSMSProject.class.getDeclaredField("m_erDefaultStyle"));
            fErDefaultStyle.setVisibleInScreen(false);
            fUserTargetID.setJField(DbSMSProject.class.getDeclaredField("m_userTargetID"));
            fUserTargetID.setVisibleInScreen(false);
            fRecentModificationDisplay.setJField(DbSMSProject.class
                    .getDeclaredField("m_recentModificationDisplay"));

            fDefaultCollectionType.setOppositeRel(DbJVClass.fReferringProject);
            fOrDefaultStyle.setOppositeRel(DbORStyle.fReferringProjectOr);
            fOoDefaultStyle.setOppositeRel(DbOOStyle.fReferringProjectOo);
            fOrDefaultNotation.setOppositeRel(DbORNotation.fReferringProjectOr);
            fOrDefaultDomainStyle.setOppositeRel(DbORDomainStyle.fReferringProjectDomain);
            fOrDefaultCommonItemStyle
                    .setOppositeRel(DbORCommonItemStyle.fReferringProjectCommonItem);
            fDefaultLinkModel.setOppositeRel(DbSMSLinkModel.fLinkModelProject);
            fBeDefaultStyle.setOppositeRel(DbBEStyle.fReferringProjectBe);
            fBeDefaultNotation.setOppositeRel(DbBENotation.fReferringProjectBe);
            fErDefaultNotation.setOppositeRel(DbORNotation.fReferringProjectEr);
            fErDefaultStyle.setOppositeRel(DbORStyle.fReferringProjectEr);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;
    private transient DbSMSBuiltInTypeNode builtInTypeNode;
    private transient DbORUserNode userNode;
    private transient DbSMSUmlExtensibility umlExtensibility;

    //Instance variables
    DbJVClass m_defaultCollectionType;
    DbORStyle m_orDefaultStyle;
    DbOOStyle m_ooDefaultStyle;
    DbORNotation m_orDefaultNotation;
    DbORDomainStyle m_orDefaultDomainStyle;
    DbORCommonItemStyle m_orDefaultCommonItemStyle;
    DbSMSLinkModel m_defaultLinkModel;
    DbBEStyle m_beDefaultStyle;
    DbBENotation m_beDefaultNotation;
    boolean m_isLocked;
    DbORNotation m_erDefaultNotation;
    DbORStyle m_erDefaultStyle;
    int m_userTargetID;
    SMSRecentModificationOption m_recentModificationDisplay;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSProject() {
    }

    /**
     * Creates an instance of DbSMSProject.
     * 
     * @param composite
     *            org.modelsphere.jack.baseDb.db.DbObject
     **/
    public DbSMSProject(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
        initUserDefinedTargetID();
        builtInTypeNode = new DbSMSBuiltInTypeNode(this);
        userNode = new DbORUserNode(this);
        TargetSystem targetSystem = TargetSystemManager.getSingleton();
        targetSystem.createTargetSystem(this, targetSystem.SGBD_LOGICAL);
        createDefaultStyle();
        createBuiltInORNotations();
        createBuiltInBENotations();
        initUMLExtensibility();

    }

    private void setDefaultInitialValues() throws DbException {
    }

    /**

 **/
    public final void createDefaultStyle() throws DbException {
        org.modelsphere.sms.db.util.DbInitialization.createDefaultStyles(this);
    }

    /**
     * @return built in type node
     **/
    public final DbSMSBuiltInTypeNode getBuiltInTypeNode() throws DbException {
        if (builtInTypeNode == null) {
            DbEnumeration dbEnum = getComponents().elements(DbSMSBuiltInTypeNode.metaClass);
            dbEnum.hasMoreElements();
            builtInTypeNode = (DbSMSBuiltInTypeNode) dbEnum.nextElement();
            dbEnum.close();
        }
        return builtInTypeNode;
    }

    /**
     * @return user node
     **/
    public final DbORUserNode getUserNode() throws DbException {
        if (userNode == null) {
            DbEnumeration dbEnum = getComponents().elements(DbORUserNode.metaClass);
            dbEnum.hasMoreElements();
            userNode = (DbORUserNode) dbEnum.nextElement();
            dbEnum.close();
        }
        return userNode;
    }

    /**

 **/
    private void createBuiltInORNotations() throws DbException {
        org.modelsphere.sms.db.util.DbInitialization.initOrNotations(this);

    }

    /**
     * @return uml extensibility
     **/
    public final DbSMSUmlExtensibility getUmlExtensibility() throws DbException {
        if (umlExtensibility == null) {
            DbEnumeration dbEnum = getComponents().elements(DbSMSUmlExtensibility.metaClass);
            dbEnum.hasMoreElements();
            umlExtensibility = (DbSMSUmlExtensibility) dbEnum.nextElement();
            dbEnum.close();
        }
        return umlExtensibility;
    }

    /**

 **/
    public void initUMLExtensibility() throws DbException {
        DbEnumeration dbEnum = this.getComponents().elements(DbSMSUmlExtensibility.metaClass);
        if (dbEnum.hasMoreElements()) {
            umlExtensibility = (DbSMSUmlExtensibility) dbEnum.nextElement();
        }
        dbEnum.close();
        if (umlExtensibility != null)
            return;

        umlExtensibility = new DbSMSUmlExtensibility(this);
        Extensibility.createBuiltInStereotypes(umlExtensibility);
        Extensibility.createBuiltInUmlConstraints(umlExtensibility);
    }

    /**

 **/
    public void createBuiltInBENotations() throws DbException {

        org.modelsphere.sms.db.util.DbInitialization.initBeNotations(this);

    }

    /**
     * @return int
     **/
    public int getNewUserTargetID() {
        m_userTargetID += 1;
        return m_userTargetID;
    }

    /**

 **/
    private void initUserDefinedTargetID() {
        m_userTargetID = 999;
    }

    //Setters

    /**
     * Sets the default collection type object associated to a DbSMSProject's instance.
     * 
     * @param value
     *            the default collection type object to be associated
     **/
    public final void setDefaultCollectionType(DbJVClass value) throws DbException {
        basicSet(fDefaultCollectionType, value);
    }

    /**
     * Sets the relational model default style object associated to a DbSMSProject's instance.
     * 
     * @param value
     *            the relational model default style object to be associated
     **/
    public final void setOrDefaultStyle(DbORStyle value) throws DbException {
        basicSet(fOrDefaultStyle, value);
    }

    /**
     * Sets the object models default style object associated to a DbSMSProject's instance.
     * 
     * @param value
     *            the object models default style object to be associated
     **/
    public final void setOoDefaultStyle(DbOOStyle value) throws DbException {
        basicSet(fOoDefaultStyle, value);
    }

    /**
     * Sets the data - default notation object associated to a DbSMSProject's instance.
     * 
     * @param value
     *            the data - default notation object to be associated
     **/
    public final void setOrDefaultNotation(DbORNotation value) throws DbException {
        basicSet(fOrDefaultNotation, value);
    }

    /**
     * Sets the domain models default style object associated to a DbSMSProject's instance.
     * 
     * @param value
     *            the domain models default style object to be associated
     **/
    public final void setOrDefaultDomainStyle(DbORDomainStyle value) throws DbException {
        basicSet(fOrDefaultDomainStyle, value);
    }

    /**
     * Sets the common item models default style object associated to a DbSMSProject's instance.
     * 
     * @param value
     *            the common item models default style object to be associated
     **/
    public final void setOrDefaultCommonItemStyle(DbORCommonItemStyle value) throws DbException {
        basicSet(fOrDefaultCommonItemStyle, value);
    }

    /**
     * Sets the default link model object associated to a DbSMSProject's instance.
     * 
     * @param value
     *            the default link model object to be associated
     **/
    public final void setDefaultLinkModel(DbSMSLinkModel value) throws DbException {
        basicSet(fDefaultLinkModel, value);
    }

    /**
     * Sets the business process default style object associated to a DbSMSProject's instance.
     * 
     * @param value
     *            the business process default style object to be associated
     **/
    public final void setBeDefaultStyle(DbBEStyle value) throws DbException {
        basicSet(fBeDefaultStyle, value);
    }

    /**
     * Sets the bpm - default notation object associated to a DbSMSProject's instance.
     * 
     * @param value
     *            the bpm - default notation object to be associated
     **/
    public final void setBeDefaultNotation(DbBENotation value) throws DbException {
        basicSet(fBeDefaultNotation, value);
    }

    /**
     * Sets the "locked" property of a DbSMSProject's instance.
     * 
     * @param value
     *            the "locked" property
     **/
    public final void setIsLocked(Boolean value) throws DbException {
        basicSet(fIsLocked, value);
    }

    /**
     * Sets the entity relationship - default notation object associated to a DbSMSProject's
     * instance.
     * 
     * @param value
     *            the entity relationship - default notation object to be associated
     **/
    public final void setErDefaultNotation(DbORNotation value) throws DbException {
        basicSet(fErDefaultNotation, value);
    }

    /**
     * Sets the entity relationship model default style object associated to a DbSMSProject's
     * instance.
     * 
     * @param value
     *            the entity relationship model default style object to be associated
     **/
    public final void setErDefaultStyle(DbORStyle value) throws DbException {
        basicSet(fErDefaultStyle, value);
    }

    /**
     * Sets the "user defined target system" property of a DbSMSProject's instance.
     * 
     * @param value
     *            the "user defined target system" property
     **/
    public final void setUserTargetID(Integer value) throws DbException {
        basicSet(fUserTargetID, value);
    }

    /**
     * Sets the "display of recent modifications" property of a DbSMSProject's instance.
     * 
     * @param value
     *            the "display of recent modifications" property
     **/
    public final void setRecentModificationDisplay(SMSRecentModificationOption value)
            throws DbException {
        basicSet(fRecentModificationDisplay, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the default collection type object associated to a DbSMSProject's instance.
     * 
     * @return the default collection type object
     **/
    public final DbJVClass getDefaultCollectionType() throws DbException {
        return (DbJVClass) get(fDefaultCollectionType);
    }

    /**
     * Gets the relational model default style object associated to a DbSMSProject's instance.
     * 
     * @return the relational model default style object
     **/
    public final DbORStyle getOrDefaultStyle() throws DbException {
        return (DbORStyle) get(fOrDefaultStyle);
    }

    /**
     * Gets the object models default style object associated to a DbSMSProject's instance.
     * 
     * @return the object models default style object
     **/
    public final DbOOStyle getOoDefaultStyle() throws DbException {
        return (DbOOStyle) get(fOoDefaultStyle);
    }

    /**
     * Gets the data - default notation object associated to a DbSMSProject's instance.
     * 
     * @return the data - default notation object
     **/
    public final DbORNotation getOrDefaultNotation() throws DbException {
        return (DbORNotation) get(fOrDefaultNotation);
    }

    /**
     * Gets the domain models default style object associated to a DbSMSProject's instance.
     * 
     * @return the domain models default style object
     **/
    public final DbORDomainStyle getOrDefaultDomainStyle() throws DbException {
        return (DbORDomainStyle) get(fOrDefaultDomainStyle);
    }

    /**
     * Gets the common item models default style object associated to a DbSMSProject's instance.
     * 
     * @return the common item models default style object
     **/
    public final DbORCommonItemStyle getOrDefaultCommonItemStyle() throws DbException {
        return (DbORCommonItemStyle) get(fOrDefaultCommonItemStyle);
    }

    /**
     * Gets the default link model object associated to a DbSMSProject's instance.
     * 
     * @return the default link model object
     **/
    public final DbSMSLinkModel getDefaultLinkModel() throws DbException {
        return (DbSMSLinkModel) get(fDefaultLinkModel);
    }

    /**
     * Gets the business process default style object associated to a DbSMSProject's instance.
     * 
     * @return the business process default style object
     **/
    public final DbBEStyle getBeDefaultStyle() throws DbException {
        return (DbBEStyle) get(fBeDefaultStyle);
    }

    /**
     * Gets the bpm - default notation object associated to a DbSMSProject's instance.
     * 
     * @return the bpm - default notation object
     **/
    public final DbBENotation getBeDefaultNotation() throws DbException {
        return (DbBENotation) get(fBeDefaultNotation);
    }

    /**
     * Gets the "locked" property's Boolean value of a DbSMSProject's instance.
     * 
     * @return the "locked" property's Boolean value
     * @deprecated use isIsLocked() method instead
     **/
    public final Boolean getIsLocked() throws DbException {
        return (Boolean) get(fIsLocked);
    }

    /**
     * Tells whether a DbSMSProject's instance is isLocked or not.
     * 
     * @return boolean
     **/
    public final boolean isIsLocked() throws DbException {
        return getIsLocked().booleanValue();
    }

    /**
     * Gets the entity relationship - default notation object associated to a DbSMSProject's
     * instance.
     * 
     * @return the entity relationship - default notation object
     **/
    public final DbORNotation getErDefaultNotation() throws DbException {
        return (DbORNotation) get(fErDefaultNotation);
    }

    /**
     * Gets the entity relationship model default style object associated to a DbSMSProject's
     * instance.
     * 
     * @return the entity relationship model default style object
     **/
    public final DbORStyle getErDefaultStyle() throws DbException {
        return (DbORStyle) get(fErDefaultStyle);
    }

    /**
     * Gets the "user defined target system" property of a DbSMSProject's instance.
     * 
     * @return the "user defined target system" property
     **/
    public final Integer getUserTargetID() throws DbException {
        return (Integer) get(fUserTargetID);
    }

    /**
     * Gets the "display of recent modifications" of a DbSMSProject's instance.
     * 
     * @return the "display of recent modifications"
     **/
    public final SMSRecentModificationOption getRecentModificationDisplay() throws DbException {
        return (SMSRecentModificationOption) get(fRecentModificationDisplay);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
