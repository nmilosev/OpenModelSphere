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
 * HREF="../../../../../org/modelsphere/sms/oo/java/db/DbJVMethod.html">DbJVMethod</A>.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/oo/db/DbOOClass.html">DbOOClass</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/oo/db/DbOOParameter.html">DbOOParameter</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbOOMethod extends DbOOAbstractMethod {

    //Meta

    public static final MetaRelation1 fReturnType = new MetaRelation1(LocaleMgr.db
            .getString("returnType"), 0);
    public static final MetaRelation1 fReturnElementType = new MetaRelation1(LocaleMgr.db
            .getString("returnElementType"), 0);
    public static final MetaField fTypeUse = new MetaField(LocaleMgr.db.getString("typeUse"));
    public static final MetaField fAbstract = new MetaField(LocaleMgr.db.getString("abstract"));
    public static final MetaField fTypeUseStyle = new MetaField(LocaleMgr.db
            .getString("typeUseStyle"));
    public static final MetaField fStatic = new MetaField(LocaleMgr.db.getString("static"));
    public static final MetaRelation1 fDomain = new MetaRelation1(LocaleMgr.db.getString("domain"),
            0);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbOOMethod"),
            DbOOMethod.class, new MetaField[] { fReturnType, fReturnElementType, fTypeUse,
                    fAbstract, fTypeUseStyle, fStatic, fDomain },
            MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbOOAbstractMethod.metaClass);
            metaClass.setIcon("dboomethod.gif");

            fReturnType.setJField(DbOOMethod.class.getDeclaredField("m_returnType"));
            fReturnType.setFlags(MetaField.COPY_REFS | MetaField.INTEGRABLE_BY_NAME);
            fReturnType.setRendererPluginName("DbFullNameInTip;DbOOAdt");
            fReturnElementType.setJField(DbOOMethod.class.getDeclaredField("m_returnElementType"));
            fReturnElementType.setFlags(MetaField.COPY_REFS | MetaField.INTEGRABLE_BY_NAME);
            fTypeUse.setJField(DbOOMethod.class.getDeclaredField("m_typeUse"));
            fTypeUse.setScreenOrder("<typeUseStyle");
            fAbstract.setJField(DbOOMethod.class.getDeclaredField("m_abstract"));
            fTypeUseStyle.setJField(DbOOMethod.class.getDeclaredField("m_typeUseStyle"));
            fTypeUseStyle.setScreenOrder("<abstract");
            fStatic.setJField(DbOOMethod.class.getDeclaredField("m_static"));
            fDomain.setJField(DbOOMethod.class.getDeclaredField("m_domain"));

            fReturnType.setOppositeRel(DbOOAdt.fTypedMethods);
            fReturnElementType.setOppositeRel(DbOOAdt.fTypedElementMethods);
            fDomain.setOppositeRel(DbORDomain.fMethods);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbOOAdt m_returnType;
    DbOOAdt m_returnElementType;
    String m_typeUse;
    boolean m_abstract;
    OOTypeUseStyle m_typeUseStyle;
    boolean m_static;
    DbORDomain m_domain;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbOOMethod() {
    }

    /**
     * Creates an instance of DbOOMethod.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbOOMethod(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    /**
     * @return string
     **/
    public final String buildTypeDisplayString() throws DbException {
        DbOOAdt type = getReturnType();
        String str = (type != null ? type.getName() : "void");
        if (getReturnElementType() != null)
            str = str + "(" + getReturnElementType().getName() + ")";
        if (getTypeUse() != null)
            str = str + getTypeUse();
        return str;
    }

    //Setters

    /**
     * Sets the return type object associated to a DbOOMethod's instance.
     * 
     * @param value
     *            the return type object to be associated
     **/
    public final void setReturnType(DbOOAdt value) throws DbException {
        basicSet(fReturnType, value);
    }

    /**
     * Sets the return element type object associated to a DbOOMethod's instance.
     * 
     * @param value
     *            the return element type object to be associated
     **/
    public final void setReturnElementType(DbOOAdt value) throws DbException {
        basicSet(fReturnElementType, value);
    }

    /**
     * Sets the "type use" property of a DbOOMethod's instance.
     * 
     * @param value
     *            the "type use" property
     **/
    public final void setTypeUse(String value) throws DbException {
        basicSet(fTypeUse, value);
    }

    /**
     * Sets the "abstract" property of a DbOOMethod's instance.
     * 
     * @param value
     *            the "abstract" property
     **/
    public final void setAbstract(Boolean value) throws DbException {
        basicSet(fAbstract, value);
    }

    /**
     * Sets the "type use style" property of a DbOOMethod's instance.
     * 
     * @param value
     *            the "type use style" property
     **/
    public final void setTypeUseStyle(OOTypeUseStyle value) throws DbException {
        basicSet(fTypeUseStyle, value);
    }

    /**
     * Sets the "static" property of a DbOOMethod's instance.
     * 
     * @param value
     *            the "static" property
     **/
    public final void setStatic(Boolean value) throws DbException {
        basicSet(fStatic, value);
    }

    /**
     * Sets the domain object associated to a DbOOMethod's instance.
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
     * Gets the return type object associated to a DbOOMethod's instance.
     * 
     * @return the return type object
     **/
    public final DbOOAdt getReturnType() throws DbException {
        return (DbOOAdt) get(fReturnType);
    }

    /**
     * Gets the return element type object associated to a DbOOMethod's instance.
     * 
     * @return the return element type object
     **/
    public final DbOOAdt getReturnElementType() throws DbException {
        return (DbOOAdt) get(fReturnElementType);
    }

    /**
     * Gets the "type use" property of a DbOOMethod's instance.
     * 
     * @return the "type use" property
     **/
    public final String getTypeUse() throws DbException {
        return (String) get(fTypeUse);
    }

    /**
     * Gets the "abstract" property's Boolean value of a DbOOMethod's instance.
     * 
     * @return the "abstract" property's Boolean value
     * @deprecated use isAbstract() method instead
     **/
    public final Boolean getAbstract() throws DbException {
        return (Boolean) get(fAbstract);
    }

    /**
     * Tells whether a DbOOMethod's instance is abstract or not.
     * 
     * @return boolean
     **/
    public final boolean isAbstract() throws DbException {
        return getAbstract().booleanValue();
    }

    /**
     * Gets the "type use style" of a DbOOMethod's instance.
     * 
     * @return the "type use style"
     **/
    public final OOTypeUseStyle getTypeUseStyle() throws DbException {
        return (OOTypeUseStyle) get(fTypeUseStyle);
    }

    /**
     * Gets the "static" property's Boolean value of a DbOOMethod's instance.
     * 
     * @return the "static" property's Boolean value
     * @deprecated use isStatic() method instead
     **/
    public final Boolean getStatic() throws DbException {
        return (Boolean) get(fStatic);
    }

    /**
     * Tells whether a DbOOMethod's instance is static or not.
     * 
     * @return boolean
     **/
    public final boolean isStatic() throws DbException {
        return getStatic().booleanValue();
    }

    /**
     * Gets the domain object associated to a DbOOMethod's instance.
     * 
     * @return the domain object
     **/
    public final DbORDomain getDomain() throws DbException {
        return (DbORDomain) get(fDomain);
    }

}
