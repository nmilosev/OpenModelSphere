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
import org.modelsphere.sms.SMSFilter;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/oo/java/db/DbJVCompilationUnit.html"
 * >DbJVCompilationUnit</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public final class DbJVImport extends DbObject {

    //Meta

    public static final MetaChoice fImportedObject = new MetaChoice(LocaleMgr.db
            .getString("importedObject"), 1);
    public static final MetaField fAll = new MetaField(LocaleMgr.db.getString("all"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbJVImport"),
            DbJVImport.class, new MetaField[] { fImportedObject, fAll }, MetaClass.NO_UDF
                    | MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbObject.metaClass);

            fImportedObject.setJField(DbJVImport.class.getDeclaredField("m_importedObject"));
            fImportedObject.setFlags(MetaField.COPY_REFS | MetaField.INTEGRABLE_BY_NAME);
            fImportedObject.setEditable(false);
            fAll.setJField(DbJVImport.class.getDeclaredField("m_all"));

            fImportedObject.setOppositeRels(new MetaRelation[] { DbJVPackage.fImports,
                    DbJVClass.fImports });

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbSMSSemanticalObject m_importedObject;
    boolean m_all;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbJVImport() {
    }

    /**
     * Creates an instance of DbJVImport.
     * 
     * @param composite
     *            org.modelsphere.sms.oo.java.db.DbJVCompilationUnit
     * @param semobj
     *            org.modelsphere.sms.db.DbSMSSemanticalObject
     * @param all
     *            java.lang.Boolean
     **/
    public DbJVImport(DbJVCompilationUnit composite, DbSMSSemanticalObject semObj, Boolean all)
            throws DbException {
        super(composite);
        basicSet(fImportedObject, semObj);
        basicSet(fAll, (semObj instanceof DbJVPackage ? Boolean.TRUE : all));
    }

    private void setDefaultInitialValues() throws DbException {
    }

    /**
     * @return string
     **/
    public final String getName() throws DbException {
        return getImportedObject().getName();
    }

    /**
     * @param value
     *            java.lang.Boolean
     **/
    public final void setAll(Boolean value) throws DbException {
        if (getImportedObject() instanceof DbJVClass)
            basicSet(fAll, value);
    }

    /**
     * @param form
     *            int
     * @return string
     **/
    public final String getSemanticalName(int form) throws DbException {
        return getImportedObject().getSemanticalName(form);
    }

    /**
     * @return int
     **/
    protected final int getFeatureSet() {
        return SMSFilter.JAVA;

    }

    //Setters

    /**
     * Sets the imported object object associated to a DbJVImport's instance.
     * 
     * @param value
     *            the imported object object to be associated
     **/
    public final void setImportedObject(DbSMSSemanticalObject value) throws DbException {
        basicSet(fImportedObject, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fAll)
                setAll((Boolean) value);
            else
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
     * Gets the imported object object associated to a DbJVImport's instance.
     * 
     * @return the imported object object
     **/
    public final DbSMSSemanticalObject getImportedObject() throws DbException {
        return (DbSMSSemanticalObject) get(fImportedObject);
    }

    /**
     * Gets the "*" property's Boolean value of a DbJVImport's instance.
     * 
     * @return the "*" property's Boolean value
     * @deprecated use isAll() method instead
     **/
    public final Boolean getAll() throws DbException {
        return (Boolean) get(fAll);
    }

    /**
     * Tells whether a DbJVImport's instance is all or not.
     * 
     * @return boolean
     **/
    public final boolean isAll() throws DbException {
        return getAll().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
