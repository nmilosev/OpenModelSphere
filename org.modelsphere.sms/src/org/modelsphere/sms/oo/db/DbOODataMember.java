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
package org.modelsphere.sms.oo.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.oo.db.srtypes.*;
import org.modelsphere.sms.oo.international.LocaleMgr;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORDomain;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../../org/modelsphere/sms/oo/java/db/DbJVDataMember.html">DbJVDataMember</A>.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/oo/db/DbOOClass.html">DbOOClass</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbOODataMember extends DbSMSStructuralFeature {

    //Meta

    public static final MetaRelation1 fAssociationEnd = new MetaRelation1(LocaleMgr.db
            .getString("associationEnd"), 0);
    public static final MetaField fVisibility = new MetaField(LocaleMgr.db.getString("visibility"));
    public static final MetaRelation1 fType = new MetaRelation1(LocaleMgr.db.getString("type"), 0);
    public static final MetaRelation1 fElementType = new MetaRelation1(LocaleMgr.db
            .getString("elementType"), 0);
    public static final MetaField fTypeUse = new MetaField(LocaleMgr.db.getString("typeUse"));
    public static final MetaField fTypeUseStyle = new MetaField(LocaleMgr.db
            .getString("typeUseStyle"));
    public static final MetaField fStatic = new MetaField(LocaleMgr.db.getString("static"));
    public static final MetaRelation1 fCommonItem = new MetaRelation1(LocaleMgr.db
            .getString("commonItem"), 0);
    public static final MetaRelation1 fDomain = new MetaRelation1(LocaleMgr.db.getString("domain"),
            0);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbOODataMember"), DbOODataMember.class, new MetaField[] { fAssociationEnd,
            fVisibility, fType, fElementType, fTypeUse, fTypeUseStyle, fStatic, fCommonItem,
            fDomain }, MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSStructuralFeature.metaClass);
            metaClass.setIcon("dboodatamember.gif");

            fAssociationEnd.setJField(DbOODataMember.class.getDeclaredField("m_associationEnd"));
            fAssociationEnd.setVisibleInScreen(false);
            fVisibility.setJField(DbOODataMember.class.getDeclaredField("m_visibility"));
            fVisibility.setScreenOrder("<initialValue");
            fType.setJField(DbOODataMember.class.getDeclaredField("m_type"));
            fType.setFlags(MetaField.COPY_REFS | MetaField.INTEGRABLE_BY_NAME);
            fType.setRendererPluginName("DbFullNameInTip;DbOOAdt");
            fElementType.setJField(DbOODataMember.class.getDeclaredField("m_elementType"));
            fElementType.setFlags(MetaField.COPY_REFS | MetaField.INTEGRABLE_BY_NAME);
            fTypeUse.setJField(DbOODataMember.class.getDeclaredField("m_typeUse"));
            fTypeUseStyle.setJField(DbOODataMember.class.getDeclaredField("m_typeUseStyle"));
            fStatic.setJField(DbOODataMember.class.getDeclaredField("m_static"));
            fCommonItem.setJField(DbOODataMember.class.getDeclaredField("m_commonItem"));
            fDomain.setJField(DbOODataMember.class.getDeclaredField("m_domain"));

            fAssociationEnd.setOppositeRel(DbOOAssociationEnd.fAssociationMember);
            fType.setOppositeRel(DbOOAdt.fTypedDataMembers);
            fElementType.setOppositeRel(DbOOAdt.fTypedElementDataMembers);
            fCommonItem.setOppositeRel(DbORCommonItem.fFields);
            fDomain.setOppositeRel(DbORDomain.fFields);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbOOAssociationEnd m_associationEnd;
    OOVisibility m_visibility;
    DbOOAdt m_type;
    DbOOAdt m_elementType;
    String m_typeUse;
    OOTypeUseStyle m_typeUseStyle;
    boolean m_static;
    DbORCommonItem m_commonItem;
    DbORDomain m_domain;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbOODataMember() {
    }

    /**
     * Creates an instance of DbOODataMember.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbOODataMember(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    /**
     * @return string
     **/
    public final String buildDisplayString() throws DbException {
        String str = getName();
        String typeUse = getTypeUse();
        if (typeUse != null && !isTypeUseAfterType())
            str = str + typeUse;
        return str;
    }

    /**
     * @return string
     **/
    public final String buildTypeDisplayString() throws DbException {
        DbOOAdt type = getType();
        String str = (type != null ? type.getName() : LocaleMgr.misc.getString("<undef>"));
        if (getElementType() != null)
            str = str + "(" + getElementType().getName() + ")";
        if (getTypeUse() != null && isTypeUseAfterType())
            str = str + getTypeUse();
        return str;
    }

    /**
     * @return boolean
     **/
    public final boolean isTypeUseAfterType() throws DbException {
        OOTypeUseStyle style = getTypeUseStyle();
        return (style == null || style.getValue() == OOTypeUseStyle.AFTER_TYPE);
    }

    /**
     * @param metarel
     *            org.modelsphere.jack.baseDb.meta.MetaRelationship
     * @return int
     **/
    public final int getMinCard(MetaRelationship metaRel) throws DbException {
        if (metaRel == DbOODataMember.fAssociationEnd) {
            DbOOAssociationEnd assocEnd = getAssociationEnd();
            return (assocEnd == null || assocEnd.isNavigable() ? 0 : 1);
        }
        return metaRel.getMinCard();
    }

    //Setters

    /**
     * Sets the association end object associated to a DbOODataMember's instance.
     * 
     * @param value
     *            the association end object to be associated
     **/
    public final void setAssociationEnd(DbOOAssociationEnd value) throws DbException {
        basicSet(fAssociationEnd, value);
    }

    /**
     * Sets the "visibility" property of a DbOODataMember's instance.
     * 
     * @param value
     *            the "visibility" property
     **/
    public final void setVisibility(OOVisibility value) throws DbException {
        basicSet(fVisibility, value);
    }

    /**
     * Sets the type object associated to a DbOODataMember's instance.
     * 
     * @param value
     *            the type object to be associated
     **/
    public final void setType(DbOOAdt value) throws DbException {
        basicSet(fType, value);
    }

    /**
     * Sets the element type object associated to a DbOODataMember's instance.
     * 
     * @param value
     *            the element type object to be associated
     **/
    public final void setElementType(DbOOAdt value) throws DbException {
        basicSet(fElementType, value);
    }

    /**
     * Sets the "type use" property of a DbOODataMember's instance.
     * 
     * @param value
     *            the "type use" property
     **/
    public final void setTypeUse(String value) throws DbException {
        basicSet(fTypeUse, value);
    }

    /**
     * Sets the "type use style" property of a DbOODataMember's instance.
     * 
     * @param value
     *            the "type use style" property
     **/
    public final void setTypeUseStyle(OOTypeUseStyle value) throws DbException {
        basicSet(fTypeUseStyle, value);
    }

    /**
     * Sets the "static" property of a DbOODataMember's instance.
     * 
     * @param value
     *            the "static" property
     **/
    public final void setStatic(Boolean value) throws DbException {
        basicSet(fStatic, value);
    }

    /**
     * Sets the common item object associated to a DbOODataMember's instance.
     * 
     * @param value
     *            the common item object to be associated
     **/
    public final void setCommonItem(DbORCommonItem value) throws DbException {
        basicSet(fCommonItem, value);
    }

    /**
     * Sets the domain object associated to a DbOODataMember's instance.
     * 
     * @param value
     *            the domain object to be associated
     **/
    public final void setDomain(DbORDomain value) throws DbException {
        basicSet(fDomain, value);
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
     * Gets the association end object associated to a DbOODataMember's instance.
     * 
     * @return the association end object
     **/
    public final DbOOAssociationEnd getAssociationEnd() throws DbException {
        return (DbOOAssociationEnd) get(fAssociationEnd);
    }

    /**
     * Gets the "visibility" of a DbOODataMember's instance.
     * 
     * @return the "visibility"
     **/
    public final OOVisibility getVisibility() throws DbException {
        return (OOVisibility) get(fVisibility);
    }

    /**
     * Gets the type object associated to a DbOODataMember's instance.
     * 
     * @return the type object
     **/
    public final DbOOAdt getType() throws DbException {
        return (DbOOAdt) get(fType);
    }

    /**
     * Gets the element type object associated to a DbOODataMember's instance.
     * 
     * @return the element type object
     **/
    public final DbOOAdt getElementType() throws DbException {
        return (DbOOAdt) get(fElementType);
    }

    /**
     * Gets the "type use" property of a DbOODataMember's instance.
     * 
     * @return the "type use" property
     **/
    public final String getTypeUse() throws DbException {
        return (String) get(fTypeUse);
    }

    /**
     * Gets the "type use style" of a DbOODataMember's instance.
     * 
     * @return the "type use style"
     **/
    public final OOTypeUseStyle getTypeUseStyle() throws DbException {
        return (OOTypeUseStyle) get(fTypeUseStyle);
    }

    /**
     * Gets the "static" property's Boolean value of a DbOODataMember's instance.
     * 
     * @return the "static" property's Boolean value
     * @deprecated use isStatic() method instead
     **/
    public final Boolean getStatic() throws DbException {
        return (Boolean) get(fStatic);
    }

    /**
     * Tells whether a DbOODataMember's instance is static or not.
     * 
     * @return boolean
     **/
    public final boolean isStatic() throws DbException {
        return getStatic().booleanValue();
    }

    /**
     * Gets the common item object associated to a DbOODataMember's instance.
     * 
     * @return the common item object
     **/
    public final DbORCommonItem getCommonItem() throws DbException {
        return (DbORCommonItem) get(fCommonItem);
    }

    /**
     * Gets the domain object associated to a DbOODataMember's instance.
     * 
     * @return the domain object
     **/
    public final DbORDomain getDomain() throws DbException {
        return (DbORDomain) get(fDomain);
    }

}
