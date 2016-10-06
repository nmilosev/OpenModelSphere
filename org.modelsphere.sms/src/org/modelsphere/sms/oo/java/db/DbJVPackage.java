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
 * HREF="../../../../../../org/modelsphere/sms/oo/java/db/DbJVCompilationUnit.html"
 * >DbJVCompilationUnit</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/oo/java/db/DbJVClass.html">DbJVClass</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/oo/java/db/DbJVPackage.html">DbJVPackage</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/oo/db/DbOODiagram.html">DbOODiagram</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSNotice.html">DbSMSNotice</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbJVPackage extends DbOOPackage {

    //Meta

    public static final MetaRelationN fImports = new MetaRelationN(LocaleMgr.db
            .getString("imports"), 0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbJVPackage"),
            DbJVPackage.class, new MetaField[] { fImports }, MetaClass.ACCESS_CTL
                    | MetaClass.CLUSTER_ROOT | MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbOOPackage.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbJVCompilationUnit.metaClass,
                    DbJVClass.metaClass, DbJVPackage.metaClass });
            metaClass.setIcon("dbjvpackage.gif");

            fImports.setJField(DbJVPackage.class.getDeclaredField("m_imports"));
            fImports.setFlags(MetaField.HUGE_RELN);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;
    private static transient boolean forceDiagramLayout = true;

    //Instance variables
    DbRelationN m_imports;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbJVPackage() {
    }

    /**
     * Creates an instance of DbJVPackage.
     * 
     * @param composite
     *            org.modelsphere.jack.baseDb.db.DbObject
     **/
    public DbJVPackage(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
        setTargetSystem(org.modelsphere.sms.plugins.TargetSystemManager.getSingleton()
                .getTargetSystemJava((DbSMSProject) composite.getProject()));
    }

    private void setDefaultInitialValues() throws DbException {
        setName(LocaleMgr.misc.getString("package"));
    }

    /**
     * @return boolean
     **/
    public final boolean forceDiagramLayout() {
        return forceDiagramLayout;
    }

    /**
     * @param value
     *            boolean
     **/
    public final void setForceDiagramLayout(boolean value) {
        forceDiagramLayout = value;
    }

    /**
     * @return int
     **/
    protected final int getFeatureSet() {
        return SMSFilter.JAVA;

    }

    //Setters

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fImports)
                ((DbJVImport) value).setImportedObject(this);
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
     * Gets the list of imports associated to a DbJVPackage's instance.
     * 
     * @return the list of imports.
     **/
    public final DbRelationN getImports() throws DbException {
        return (DbRelationN) get(fImports);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
