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
 * HREF="../../../../../../org/modelsphere/sms/oo/java/db/DbJVPackage.html">DbJVPackage</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/oo/java/db/DbJVClassModel.html">DbJVClassModel</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/oo/java/db/DbJVImport.html">DbJVImport</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbJVCompilationUnit extends DbSMSSemanticalObject {

    //Meta

    public static final MetaRelationN fClasses = new MetaRelationN(LocaleMgr.db
            .getString("classes"), 0, MetaRelationN.cardN);
    public static final MetaField fCompiled = new MetaField(LocaleMgr.db.getString("compiled"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbJVCompilationUnit"), DbJVCompilationUnit.class, new MetaField[] {
            fClasses, fCompiled }, MetaClass.MATCHABLE | MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSSemanticalObject.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbJVImport.metaClass });
            metaClass.setIcon("dbjvcompilationunit.gif");

            fClasses.setJField(DbJVCompilationUnit.class.getDeclaredField("m_classes"));
            fCompiled.setJField(DbJVCompilationUnit.class.getDeclaredField("m_compiled"));
            fCompiled.setEditable(false);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = -2443169515704907352L;

    //Instance variables
    DbRelationN m_classes;
    boolean m_compiled;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbJVCompilationUnit() {
    }

    /**
     * Creates an instance of DbJVCompilationUnit.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbJVCompilationUnit(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setName(LocaleMgr.misc.getString("file.java"));
    }

    /**
     * @return int
     **/
    protected final int getFeatureSet() {
        return SMSFilter.JAVA;

    }

    //Setters

    /**
     * Sets the "is compiled" property of a DbJVCompilationUnit's instance.
     * 
     * @param value
     *            the "is compiled" property
     **/
    public final void setCompiled(Boolean value) throws DbException {
        basicSet(fCompiled, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fClasses)
                ((DbJVClass) value).setCompilationUnit(this);
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
     * Gets the list of classes associated to a DbJVCompilationUnit's instance.
     * 
     * @return the list of classes.
     **/
    public final DbRelationN getClasses() throws DbException {
        return (DbRelationN) get(fClasses);
    }

    /**
     * Gets the "is compiled" property's Boolean value of a DbJVCompilationUnit's instance.
     * 
     * @return the "is compiled" property's Boolean value
     * @deprecated use isCompiled() method instead
     **/
    public final Boolean getCompiled() throws DbException {
        return (Boolean) get(fCompiled);
    }

    /**
     * Tells whether a DbJVCompilationUnit's instance is compiled or not.
     * 
     * @return boolean
     **/
    public final boolean isCompiled() throws DbException {
        return getCompiled().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
