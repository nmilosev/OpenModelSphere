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
package org.modelsphere.sms.or.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import javax.swing.Icon;
import org.modelsphere.sms.or.db.util.AnyORObject;
import org.modelsphere.sms.oo.java.db.DbJVMethod;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../../org/modelsphere/sms/or/oracle/db/DbORAProcedure.html" >DbORAProcedure</A>,
 * <A HREF="../../../../../org/modelsphere/sms/or/ibm/db/DbIBMProcedure.html" >DbIBMProcedure</A>,
 * <A HREF= "../../../../../org/modelsphere/sms/or/informix/db/DbINFProcedure.html"
 * >DbINFProcedure</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/generic/db/DbGEProcedure.html" >DbGEProcedure</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html"
 * >DbSMSObjectImport</A>.<br>
 **/
public abstract class DbORProcedure extends DbORAbstractMethod {

    // Meta

    public static final MetaField fFunction = new MetaField(LocaleMgr.db.getString("function"));
    public static final MetaRelation1 fReturnType = new MetaRelation1(LocaleMgr.db
            .getString("returnType"), 0);
    public static final MetaField fReturnTypeReference = new MetaField(LocaleMgr.db
            .getString("returnTypeReference"));
    public static final MetaRelation1 fJavaMethod = new MetaRelation1(LocaleMgr.db
            .getString("javaMethod"), 0);
    public static final MetaField fLength = new MetaField(LocaleMgr.db.getString("length"));
    public static final MetaField fPrecision = new MetaField(LocaleMgr.db.getString("precision"));

    public static final MetaClass metaClass = new MetaClass(
            LocaleMgr.db.getString("DbORProcedure"), DbORProcedure.class,
            new MetaField[] { fFunction, fReturnType, fReturnTypeReference, fJavaMethod, fLength,
                    fPrecision }, MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORAbstractMethod.metaClass);
            metaClass.setIcon("dborprocedure.gif");

            fFunction.setJField(DbORProcedure.class.getDeclaredField("m_function"));
            fFunction.setScreenOrder("<body");
            fReturnType.setJField(DbORProcedure.class.getDeclaredField("m_returnType"));
            fReturnType.setFlags(MetaField.COPY_REFS | MetaField.INTEGRABLE_BY_NAME);
            fReturnType.setRendererPluginName("DbFullNameInTip;DbORTypeClassifier");
            fReturnTypeReference.setJField(DbORProcedure.class
                    .getDeclaredField("m_returnTypeReference"));
            fJavaMethod.setJField(DbORProcedure.class.getDeclaredField("m_javaMethod"));
            fJavaMethod.setFlags(MetaField.COPY_REFS | MetaField.INTEGRABLE_BY_NAME);
            fJavaMethod.setRendererPluginName("DbFullNameInTip;DbJVMethod");
            fLength.setJField(DbORProcedure.class.getDeclaredField("m_length"));
            fPrecision.setJField(DbORProcedure.class.getDeclaredField("m_precision"));

            fReturnType.setOppositeRel(DbORTypeClassifier.fReturnTypedProcedures);
            fJavaMethod.setOppositeRel(DbJVMethod.fProcedures);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;
    private static Icon procedureIcon = org.modelsphere.jack.graphic.GraphicUtil.loadIcon(
            DbORProcedure.class, "resources/dborprocedure.gif");
    private static Icon functionIcon = org.modelsphere.jack.graphic.GraphicUtil.loadIcon(
            DbORProcedure.class, "resources/dborfunction.gif");

    // Instance variables
    boolean m_function;
    DbORTypeClassifier m_returnType;
    boolean m_returnTypeReference;
    DbJVMethod m_javaMethod;
    int m_length;
    int m_precision;

    // Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORProcedure() {
    }

    /**
     * Creates an instance of DbORProcedure.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORProcedure(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setFunction(Boolean.FALSE);
        setReturnTypeReference(Boolean.FALSE);
    }

    /**
     * @param form
     *            int
     * @return icon
     **/
    public final Icon getSemanticalIcon(int form) throws DbException {

        if (isFunction())
            return functionIcon;
        else
            return procedureIcon;

    }

    /**
     * @return string
     **/
    public final String buildSignature() throws DbException {
        StringBuffer signature = new StringBuffer();
        signature.append(getName() + "(");
        DbEnumeration dbEnum = getComponents().elements(DbORParameter.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORParameter param = (DbORParameter) dbEnum.nextElement();
            DbORTypeClassifier type = param.getType();
            if (type != null)
                signature.append(type.getName());
            else
                signature.append("<undef>");

            if (dbEnum.hasMoreElements())
                signature.append(", ");
        }
        dbEnum.close();
        signature.append(")");
        return signature.toString();
    }

    // Setters

    /**
     * Sets the "function" property of a DbORProcedure's instance.
     * 
     * @param value
     *            the "function" property
     **/
    public final void setFunction(Boolean value) throws DbException {
        basicSet(fFunction, value);
    }

    /**
     * Sets the return type object associated to a DbORProcedure's instance.
     * 
     * @param value
     *            the return type object to be associated
     **/
    public final void setReturnType(DbORTypeClassifier value) throws DbException {
        basicSet(fReturnType, value);
    }

    /**
     * Sets the "return type reference" property of a DbORProcedure's instance.
     * 
     * @param value
     *            the "return type reference" property
     **/
    public final void setReturnTypeReference(Boolean value) throws DbException {
        basicSet(fReturnTypeReference, value);
    }

    /**
     * Sets the java method object associated to a DbORProcedure's instance.
     * 
     * @param value
     *            the java method object to be associated
     **/
    public final void setJavaMethod(DbJVMethod value) throws DbException {
        basicSet(fJavaMethod, value);
    }

    /**
     * Sets the "length" property of a DbORProcedure's instance.
     * 
     * @param value
     *            the "length" property
     **/
    public final void setLength(Integer value) throws DbException {
        basicSet(fLength, value);
    }

    /**
     * Sets the "precision" property of a DbORProcedure's instance.
     * 
     * @param value
     *            the "precision" property
     **/
    public final void setPrecision(Integer value) throws DbException {
        basicSet(fPrecision, value);
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

    // Getters

    /**
     * Gets the "function" property's Boolean value of a DbORProcedure's instance.
     * 
     * @return the "function" property's Boolean value
     * @deprecated use isFunction() method instead
     **/
    public final Boolean getFunction() throws DbException {
        return (Boolean) get(fFunction);
    }

    /**
     * Tells whether a DbORProcedure's instance is function or not.
     * 
     * @return boolean
     **/
    public final boolean isFunction() throws DbException {
        return getFunction().booleanValue();
    }

    /**
     * Gets the return type object associated to a DbORProcedure's instance.
     * 
     * @return the return type object
     **/
    public final DbORTypeClassifier getReturnType() throws DbException {
        return (DbORTypeClassifier) get(fReturnType);
    }

    /**
     * Gets the "return type reference" property's Boolean value of a DbORProcedure's instance.
     * 
     * @return the "return type reference" property's Boolean value
     * @deprecated use isReturnTypeReference() method instead
     **/
    public final Boolean getReturnTypeReference() throws DbException {
        return (Boolean) get(fReturnTypeReference);
    }

    /**
     * Tells whether a DbORProcedure's instance is returnTypeReference or not.
     * 
     * @return boolean
     **/
    public final boolean isReturnTypeReference() throws DbException {
        return getReturnTypeReference().booleanValue();
    }

    /**
     * Gets the java method object associated to a DbORProcedure's instance.
     * 
     * @return the java method object
     **/
    public final DbJVMethod getJavaMethod() throws DbException {
        return (DbJVMethod) get(fJavaMethod);
    }

    /**
     * Gets the "length" property of a DbORProcedure's instance.
     * 
     * @return the "length" property
     **/
    public final Integer getLength() throws DbException {
        return (Integer) get(fLength);
    }

    /**
     * Gets the "precision" property of a DbORProcedure's instance.
     * 
     * @return the "precision" property
     **/
    public final Integer getPrecision() throws DbException {
        return (Integer) get(fPrecision);
    }

}
