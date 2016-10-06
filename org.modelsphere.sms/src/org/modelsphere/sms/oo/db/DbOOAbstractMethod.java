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

/**
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../../org/modelsphere/sms/oo/db/DbOOMethod.html">DbOOMethod</A>, <A
 * HREF="../../../../../org/modelsphere/sms/oo/db/DbOOConstructor.html">DbOOConstructor</A>.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/oo/db/DbOOClass.html">DbOOClass</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/oo/db/DbOOParameter.html">DbOOParameter</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbOOAbstractMethod extends DbOOOperation {

    //Meta

    public static final MetaField fVisibility = new MetaField(LocaleMgr.db.getString("visibility"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbOOAbstractMethod"), DbOOAbstractMethod.class,
            new MetaField[] { fVisibility }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbOOOperation.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbOOParameter.metaClass });

            fVisibility.setJField(DbOOAbstractMethod.class.getDeclaredField("m_visibility"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    OOVisibility m_visibility;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbOOAbstractMethod() {
    }

    /**
     * Creates an instance of DbOOAbstractMethod.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbOOAbstractMethod(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    /**
     * @param dbo
     *            org.modelsphere.jack.baseDb.db.DbObject
     * @return boolean
     **/
    public boolean matches(DbObject dbo) throws DbException {
        DbOOAbstractMethod that = (DbOOAbstractMethod) dbo;
        if (!DbObject.valuesAreEqual(getName(), that.getName()))
            return false;

        boolean sameParms = false;
        DbEnumeration dbEnum = getComponents().elements(DbOOParameter.metaClass);
        DbEnumeration enumThat = that.getComponents().elements(DbOOParameter.metaClass);
        while (true) {
            DbOOParameter parm = (dbEnum.hasMoreElements() ? (DbOOParameter) dbEnum.nextElement()
                    : null);
            DbOOParameter parmThat = (enumThat.hasMoreElements() ? (DbOOParameter) enumThat
                    .nextElement() : null);
            if (parm == null) {
                sameParms = (parmThat == null);
                break;
            }
            if (parmThat == null)
                break;
            DbOOAdt type = parm.getType();
            if (type == null)
                break;
            type = (DbOOAdt) type.findMatchingObject();
            if (type == null)
                break;
            if (type != parmThat.getType())
                break;
            if (!DbObject.valuesAreEqual(parm.getTypeUse(), parmThat.getTypeUse()))
                break;
        }
        dbEnum.close();
        enumThat.close();
        return sameParms;
    }

    /**
     * @param form
     *            int
     * @return string
     **/
    public final String buildSignature(int form) throws DbException {
        StringBuffer signature = new StringBuffer();
        signature.append(getName() + "(");
        DbEnumeration dbEnum = getComponents().elements(DbOOParameter.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbOOParameter param = (DbOOParameter) dbEnum.nextElement();
            DbOOAdt type = param.getType();
            if (type != null)
                signature.append(type.getSemanticalName(form));
            else
                signature.append("<undef>");

            String typeUse = param.getTypeUse();
            if (typeUse != null)
                signature.append(typeUse);

            if (dbEnum.hasMoreElements())
                signature.append(", ");
        }
        dbEnum.close();
        signature.append(")");
        return signature.toString();
    }

    /**
     * @return string
     **/
    public final String buildDisplayString() throws DbException {
        StringBuffer signature = new StringBuffer();
        signature.append(getName() + "(");
        DbEnumeration dbEnum = getComponents().elements(DbOOParameter.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbOOParameter param = (DbOOParameter) dbEnum.nextElement();
            signature.append(param.buildTypeDisplayString() + " " + param.buildDisplayString());
            if (dbEnum.hasMoreElements())
                signature.append(", ");
        }
        dbEnum.close();
        signature.append(")");
        return signature.toString();
    }

    /**
     * @return boolean
     **/
    public final boolean hasParameter() throws DbException {
        DbEnumeration dbEnum = getComponents().elements(DbOOParameter.metaClass);
        boolean ret = dbEnum.hasMoreElements();
        dbEnum.close();
        return ret;
    }

    /**
     * @param other
     *            org.modelsphere.sms.oo.db.DbOOAbstractMethod
     * @return boolean
     **/
    public final boolean hasSameSignatureAs(DbOOAbstractMethod other) throws DbException {
        if (!DbObject.valuesAreEqual(getName(), other.getName()))
            return false;
        boolean same = false;
        DbEnumeration dbEnum = getComponents().elements(DbOOParameter.metaClass);
        DbEnumeration enumOther = other.getComponents().elements(DbOOParameter.metaClass);
        while (true) {
            DbOOParameter parm = (dbEnum.hasMoreElements() ? (DbOOParameter) dbEnum.nextElement()
                    : null);
            DbOOParameter parmOther = (enumOther.hasMoreElements() ? (DbOOParameter) enumOther
                    .nextElement() : null);
            if (parm == null || parmOther == null) {
                same = (parm == parmOther);
                break;
            }
            if (parm.getType() != parmOther.getType())
                break;
        }
        dbEnum.close();
        enumOther.close();
        return same;
    }

    //Setters

    /**
     * Sets the "visibility" property of a DbOOAbstractMethod's instance.
     * 
     * @param value
     *            the "visibility" property
     **/
    public final void setVisibility(OOVisibility value) throws DbException {
        basicSet(fVisibility, value);
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
     * Gets the "visibility" of a DbOOAbstractMethod's instance.
     * 
     * @return the "visibility"
     **/
    public final OOVisibility getVisibility() throws DbException {
        return (OOVisibility) get(fVisibility);
    }

}
