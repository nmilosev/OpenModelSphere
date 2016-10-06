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
package org.modelsphere.sms.or.oracle.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.oracle.db.srtypes.*;
import org.modelsphere.sms.or.oracle.international.LocaleMgr;
import org.modelsphere.sms.or.db.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSUserDefinedPackage.html"
 * >DbSMSUserDefinedPackage</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSProject.html">DbSMSProject</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORATablespace.html"
 * >DbORATablespace</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORARedoLogGroup.html"
 * >DbORARedoLogGroup</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORARollbackSegment.html"
 * >DbORARollbackSegment</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSNotice.html">DbSMSNotice</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbORADatabase extends DbORDatabase {

    //Meta

    public static final MetaField fReuseControlFiles = new MetaField(LocaleMgr.db
            .getString("reuseControlFiles"));
    public static final MetaField fMaxLogFiles = new MetaField(LocaleMgr.db
            .getString("maxLogFiles"));
    public static final MetaField fMaxLogMembers = new MetaField(LocaleMgr.db
            .getString("maxLogMembers"));
    public static final MetaField fMaxLogHistory = new MetaField(LocaleMgr.db
            .getString("maxLogHistory"));
    public static final MetaField fMaxDataFiles = new MetaField(LocaleMgr.db
            .getString("maxDataFiles"));
    public static final MetaField fMaxInstances = new MetaField(LocaleMgr.db
            .getString("maxInstances"));
    public static final MetaField fArchiveLog = new MetaField(LocaleMgr.db.getString("archiveLog"));
    public static final MetaField fCharacterSet = new MetaField(LocaleMgr.db
            .getString("characterSet"));
    public static final MetaField fNationalCharacterSet = new MetaField(LocaleMgr.db
            .getString("nationalCharacterSet"));

    public static final MetaClass metaClass = new MetaClass(
            LocaleMgr.db.getString("DbORADatabase"),
            DbORADatabase.class,
            new MetaField[] { fReuseControlFiles, fMaxLogFiles, fMaxLogMembers, fMaxLogHistory,
                    fMaxDataFiles, fMaxInstances, fArchiveLog, fCharacterSet, fNationalCharacterSet },
            MetaClass.ACCESS_CTL | MetaClass.CLUSTER_ROOT | MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORDatabase.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbORATablespace.metaClass,
                    DbORARedoLogGroup.metaClass, DbORARollbackSegment.metaClass });

            fReuseControlFiles.setJField(DbORADatabase.class
                    .getDeclaredField("m_reuseControlFiles"));
            fMaxLogFiles.setJField(DbORADatabase.class.getDeclaredField("m_maxLogFiles"));
            fMaxLogMembers.setJField(DbORADatabase.class.getDeclaredField("m_maxLogMembers"));
            fMaxLogHistory.setJField(DbORADatabase.class.getDeclaredField("m_maxLogHistory"));
            fMaxDataFiles.setJField(DbORADatabase.class.getDeclaredField("m_maxDataFiles"));
            fMaxInstances.setJField(DbORADatabase.class.getDeclaredField("m_maxInstances"));
            fArchiveLog.setJField(DbORADatabase.class.getDeclaredField("m_archiveLog"));
            fCharacterSet.setJField(DbORADatabase.class.getDeclaredField("m_characterSet"));
            fNationalCharacterSet.setJField(DbORADatabase.class
                    .getDeclaredField("m_nationalCharacterSet"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    boolean m_reuseControlFiles;
    SrInteger m_maxLogFiles;
    SrInteger m_maxLogMembers;
    SrInteger m_maxLogHistory;
    SrInteger m_maxDataFiles;
    SrInteger m_maxInstances;
    boolean m_archiveLog;
    String m_characterSet;
    String m_nationalCharacterSet;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORADatabase() {
    }

    /**
     * Creates an instance of DbORADatabase.
     * 
     * @param composite
     *            org.modelsphere.jack.baseDb.db.DbObject
     * @param targetsystem
     *            org.modelsphere.sms.db.DbSMSTargetSystem
     **/
    public DbORADatabase(DbObject composite, DbSMSTargetSystem targetSystem) throws DbException {
        super(composite, targetSystem);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setReuseControlFiles(Boolean.FALSE);
        setArchiveLog(Boolean.FALSE);
        setName(LocaleMgr.misc.getString("database"));
    }

    //Setters

    /**
     * Sets the "reuse control files" property of a DbORADatabase's instance.
     * 
     * @param value
     *            the "reuse control files" property
     **/
    public final void setReuseControlFiles(Boolean value) throws DbException {
        basicSet(fReuseControlFiles, value);
    }

    /**
     * Sets the "max. log files" property of a DbORADatabase's instance.
     * 
     * @param value
     *            the "max. log files" property
     **/
    public final void setMaxLogFiles(Integer value) throws DbException {
        basicSet(fMaxLogFiles, value);
    }

    /**
     * Sets the "max. log members" property of a DbORADatabase's instance.
     * 
     * @param value
     *            the "max. log members" property
     **/
    public final void setMaxLogMembers(Integer value) throws DbException {
        basicSet(fMaxLogMembers, value);
    }

    /**
     * Sets the "max. log history" property of a DbORADatabase's instance.
     * 
     * @param value
     *            the "max. log history" property
     **/
    public final void setMaxLogHistory(Integer value) throws DbException {
        basicSet(fMaxLogHistory, value);
    }

    /**
     * Sets the "max. data files" property of a DbORADatabase's instance.
     * 
     * @param value
     *            the "max. data files" property
     **/
    public final void setMaxDataFiles(Integer value) throws DbException {
        basicSet(fMaxDataFiles, value);
    }

    /**
     * Sets the "max. instances" property of a DbORADatabase's instance.
     * 
     * @param value
     *            the "max. instances" property
     **/
    public final void setMaxInstances(Integer value) throws DbException {
        basicSet(fMaxInstances, value);
    }

    /**
     * Sets the "archive log" property of a DbORADatabase's instance.
     * 
     * @param value
     *            the "archive log" property
     **/
    public final void setArchiveLog(Boolean value) throws DbException {
        basicSet(fArchiveLog, value);
    }

    /**
     * Sets the "character set" property of a DbORADatabase's instance.
     * 
     * @param value
     *            the "character set" property
     **/
    public final void setCharacterSet(String value) throws DbException {
        basicSet(fCharacterSet, value);
    }

    /**
     * Sets the "national character set" property of a DbORADatabase's instance.
     * 
     * @param value
     *            the "national character set" property
     **/
    public final void setNationalCharacterSet(String value) throws DbException {
        basicSet(fNationalCharacterSet, value);
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
     * Gets the "reuse control files" property's Boolean value of a DbORADatabase's instance.
     * 
     * @return the "reuse control files" property's Boolean value
     * @deprecated use isReuseControlFiles() method instead
     **/
    public final Boolean getReuseControlFiles() throws DbException {
        return (Boolean) get(fReuseControlFiles);
    }

    /**
     * Tells whether a DbORADatabase's instance is reuseControlFiles or not.
     * 
     * @return boolean
     **/
    public final boolean isReuseControlFiles() throws DbException {
        return getReuseControlFiles().booleanValue();
    }

    /**
     * Gets the "max. log files" of a DbORADatabase's instance.
     * 
     * @return the "max. log files"
     **/
    public final Integer getMaxLogFiles() throws DbException {
        return (Integer) get(fMaxLogFiles);
    }

    /**
     * Gets the "max. log members" of a DbORADatabase's instance.
     * 
     * @return the "max. log members"
     **/
    public final Integer getMaxLogMembers() throws DbException {
        return (Integer) get(fMaxLogMembers);
    }

    /**
     * Gets the "max. log history" of a DbORADatabase's instance.
     * 
     * @return the "max. log history"
     **/
    public final Integer getMaxLogHistory() throws DbException {
        return (Integer) get(fMaxLogHistory);
    }

    /**
     * Gets the "max. data files" of a DbORADatabase's instance.
     * 
     * @return the "max. data files"
     **/
    public final Integer getMaxDataFiles() throws DbException {
        return (Integer) get(fMaxDataFiles);
    }

    /**
     * Gets the "max. instances" of a DbORADatabase's instance.
     * 
     * @return the "max. instances"
     **/
    public final Integer getMaxInstances() throws DbException {
        return (Integer) get(fMaxInstances);
    }

    /**
     * Gets the "archive log" property's Boolean value of a DbORADatabase's instance.
     * 
     * @return the "archive log" property's Boolean value
     * @deprecated use isArchiveLog() method instead
     **/
    public final Boolean getArchiveLog() throws DbException {
        return (Boolean) get(fArchiveLog);
    }

    /**
     * Tells whether a DbORADatabase's instance is archiveLog or not.
     * 
     * @return boolean
     **/
    public final boolean isArchiveLog() throws DbException {
        return getArchiveLog().booleanValue();
    }

    /**
     * Gets the "character set" property of a DbORADatabase's instance.
     * 
     * @return the "character set" property
     **/
    public final String getCharacterSet() throws DbException {
        return (String) get(fCharacterSet);
    }

    /**
     * Gets the "national character set" property of a DbORADatabase's instance.
     * 
     * @return the "national character set" property
     **/
    public final String getNationalCharacterSet() throws DbException {
        return (String) get(fNationalCharacterSet);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
