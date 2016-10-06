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

package org.modelsphere.sms.plugins;

import java.awt.Component;
import java.io.File;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.screen.DbListView;
import org.modelsphere.jack.baseDb.screen.LookupDialog;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.CollationComparator;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.jack.util.SrSort;
import org.modelsphere.sms.db.DbSMSBuiltInTypePackage;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.or.db.util.AnyORObject;

public final class TargetSystem {

    /**
     * Those numbers are associated to the TARGET-ID an ROOT-ID in the target system ".typ" files.
     * Register all new DBMS here, with a new unique number.
     */
    // ROOT ID
    public static final int SGBD_JAVA = 0;
    public static final int SGBD_LOGICAL = 1;
    public static final int SGBD_ANSI_SQL_ROOT = 2;
    public static final int SGBD_DBASE4_ROOT = 3;
    public static final int SGBD_IBM_DB2_ROOT = 4;
    public static final int SGBD_INFORMIX_ROOT = 5;
    public static final int SGBD_INGRES_ROOT = 6;
    public static final int SGBD_INTERBASE_ROOT = 7;
    public static final int SGBD_MSACCESS_ROOT = 8;
    public static final int SGBD_NSTOPSQL_ROOT = 9;
    public static final int SGBD_OMNIS_ROOT = 10;
    public static final int SGBD_ORACLE_ROOT = 11;
    public static final int SGBD_PARADOX_ROOT = 12;
    public static final int SGBD_PROGRESS_ROOT = 13;
    public static final int SGBD_RDB_ROOT = 14;
    public static final int SGBD_SQL400_ROOT = 15;
    public static final int SGBD_SQLDS_ROOT = 16;
    public static final int SGBD_SQLANY_ROOT = 17;
    public static final int SGBD_SQLBASE_ROOT = 18;
    public static final int SGBD_SQLSERVER_ROOT = 19;
    public static final int SGBD_SYBASE_ROOT = 20;
    public static final int SGBD_SYNON_ROOT = 21;
    public static final int SGBD_TERADATA_ROOT = 22;
    public static final int SGBD_UNIFACE_ROOT = 23;
    public static final int SGBD_WATCOM_ROOT = 24;
    public static final int SGBD_XDB_ROOT = 25;
    public static final int SGBD_POWERBUILDER_ROOT = 26;
    public static final int SGBD_MYSQL_ROOT = 27;
    public static final int SGBD_POSTGRE_SQL_ROOT = 28;
    public static final int SGBD_HSQLDB_ROOT = 29;

    // TARGET ID
    // public static final int SGBD_JAVA = 0; // Same as Root Id
    // public static final int SGBD_LOGICAL = 1; // Same as Root Id
    public static final int SGBD_ANSI_SQL92 = 2;
    public static final int SGBD_ANSI_SQL99 = 3;
    public static final int SGBD_DBASE4 = 4;
    public static final int SGBD_DB2_6000 = 5;
    public static final int SGBD_DB2_AS37 = 6;
    public static final int SGBD_DB2_AS41 = 7;
    public static final int SGBD_DB2_AS42 = 8;
    public static final int SGBD_DB2_AS44 = 9;
    public static final int SGBD_DB2_MVS21 = 10;
    public static final int SGBD_DB2_MVS31 = 11;
    public static final int SGBD_DB2_MVS41 = 12;
    public static final int SGBD_DB2_MVS51 = 13;
    public static final int SGBD_DB2_UNIVERSAL_DB_61 = 14;
    public static final int SGBD_INFORMIX_OL = 15;
    public static final int SGBD_INFORMIX_SE = 16;
    public static final int SGBD_INFORMIX_IUS9 = 17;
    public static final int SGBD_INFORMIX_IDS_2000 = 18;
    public static final int SGBD_INGRES = 19;
    public static final int SGBD_INTERBASE40 = 20;
    public static final int SGBD_INTERBASE41 = 21;
    public static final int SGBD_MSACCESS = 22;
    public static final int SGBD_MSACCESS95 = 23;
    public static final int SGBD_MSACCESS97 = 24;
    public static final int SGBD_NSTOPSQL = 25;
    public static final int SGBD_OMNIS = 26;// N'existe plus
    public static final int SGBD_ORACLE6 = 27;
    public static final int SGBD_ORACLE7 = 28;
    public static final int SGBD_ORACLE8 = 29;
    public static final int SGBD_PARADOX = 30;
    public static final int SGBD_PROGRESS6 = 31;
    public static final int SGBD_PROGRESS7 = 32;
    public static final int SGBD_PROGRESS8 = 33;
    public static final int SGBD_RDB = 34;
    public static final int SGBD_SQL400 = 35;
    public static final int SGBD_SQLDS = 36;
    public static final int SGBD_SQLANY50 = 37;
    public static final int SGBD_SQLBASE = 38;
    public static final int SGBD_SQLSERVER42 = 39;
    public static final int SGBD_SQLSERVER60 = 40;
    public static final int SGBD_SQLSERVER65 = 41;
    public static final int SGBD_SQLSERVER7 = 42;
    public static final int SGBD_SYBASE49 = 43;
    public static final int SGBD_SYBASE10 = 44;
    public static final int SGBD_SYBASE11 = 45;
    public static final int SGBD_SYBASE115 = 46;
    public static final int SGBD_SYBASE119 = 47;
    public static final int SGBD_SYNON = 48;// N'existe plus
    public static final int SGBD_TERADATA = 49;
    public static final int SGBD_TERADATA30 = 50;
    public static final int SGBD_UNIFACE5 = 51;// N'existe plus
    public static final int SGBD_UNIFACE6 = 52;// N'existe plus
    public static final int SGBD_UNIFACE7 = 53;// N'existe plus
    public static final int SGBD_WATCOM = 54;
    public static final int SGBD_XDB = 55;
    public static final int SGBD_POWERBUILDER = 56;
    public static final int SGBD_MSACCESS2000 = 57;
    public static final int SGBD_SQLSERVER2000 = 58;
    public static final int SGBD_ORACLE81 = 59;
    public static final int SGBD_MYSQL_32 = 60;
    public static final int SGBD_SYBASE12 = 61;// N'existe plus
    public static final int SGBD_DB2_UNIVERSAL_DB_72 = 62;
    public static final int SGBD_ORACLE90 = 63;
    public static final int SGBD_SYBASE125 = 64;
    public static final int SGBD_FRONTBASE35 = 65;// N'existe plus
    public static final int SGBD_DB2_UNIVERSAL_DB_81 = 66;
    public static final int SGBD_ORACLE10 = 67;
    public static final int SGBD_POSTGRE_SQL8 = 68;
    public static final int SGBD_SQLSERVER2005 = 69;
    public static final int SGBD_INFORMIX_IDS_10 = 70;
    public static final int SGBD_HSQLDB = 71;
    public static final int SGBD_TERADATA50 = 72;
    public static final int SGBD_TERADATA60 = 73;
    public static final int SGBD_ORACLE11 = 74;
    public static final int SGBD_SQLSERVER2008 = 75;
    public static final int SGBD_INFORMIX_IDS_115 = 76;
    public static final int SGBD_DB2_UNIVERSAL_DB_9 = 77; 
    public static final int SGBD_ORACLE12 = 78;
    public static final int SGBD_SQLSERVER2012 = 79;
    public static final int SGBD_SQL_AZURE = 80;
    public static final int SGBD_DB2_UNIVERSAL_DB_10 = 81;
    public static final int SGBD_TERADATA12 = 82;
    public static final int SGBD_TERADATA13 = 83;
    public static final int SGBD_TERADATA14 = 84;
    

    /*
     * ****************************************************************
     */

    private static Map<Integer, TargetSystemInfo> targetMap = new HashMap<Integer, TargetSystemInfo>();
    private static final String TARGET_DIRECTORY = "targets"; // NOT
    // LOCALIZABLE,
    // directory
    // name
    private static final String FILE_SEP = System.getProperty("file.separator"); // NOT
    // LOCALIZABLE,
    // property
    private static final String MSG_PATTERN = "Bad Installation:  Cannot locate {0} directory."; // NOT

    // LOCALIZABLE,
    // runtime
    // exception

    public TargetSystem() {
        // Retrieve all <.typ> files from the "target" folder
        String appDir = ApplicationContext.getApplicationDirectory();
        String tarDir = appDir + FILE_SEP + TARGET_DIRECTORY;
        File dirFile = new File(tarDir);

        if (!dirFile.exists() || !dirFile.isDirectory()) {
            String msg = MessageFormat.format(MSG_PATTERN, new Object[] { tarDir });
            throw new RuntimeException(msg);
        }

        String[] fileNames = dirFile.list();
        if (fileNames != null) {
            for (int i = 0; i < fileNames.length; i++) {
                if (fileNames[i].endsWith(".typ")) { // NOT LOCALIZABLE, file
                    // extension
                    File file = new File(dirFile, fileNames[i]);
                    try {
                        TargetSystemInfo targetInfo = new TargetSystemInfo(file);
                        targetMap.put(new Integer(targetInfo.getID()), targetInfo);
                    } catch (Exception e) {
                        Debug.trace(e);
                    }
                }
            }

        }
    }

    /*
     * public File getUserTargetSystemTemplate(){ String tarDir =
     * ApplicationContext.getApplicationDirectory() + FILE_SEP + TARGET_DIRECTORY; File dirFile =
     * new File(tarDir); File file = new File(dirFile, "UserTS.tpl");//NOT LOCALIZABLE return file;
     * }
     */

    public final Collection<TargetSystemInfo> getAllTargetSystemInfos() {
        return targetMap.values();
    }

    public final TargetSystemInfo getTargetSystemInfo(DbSMSTargetSystem target) throws DbException {
        TargetSystemInfo targetInfo = (TargetSystemInfo) targetMap.get(target.getID());
        if (targetInfo == null)
            throw new RuntimeException("Type file not found for " + target.getName() + " "
                    + target.getVersion()); // NOT
        // LOCALIZABLE,
        // runtime
        // exception
        return targetInfo;
    }

    public static void removeUserTargetSystem(Integer targetID) {
        targetMap.remove(targetID);
    }

    public static boolean isUserTargetSystem(DbSMSTargetSystem target) throws DbException {
        return target.getRootID().intValue() > 999;
    }

    // Return the DbSMSTargetSystem specify in targetID (if installed).
    // If the specified target system is not installed, return null.
    public static DbSMSTargetSystem getSpecificTargetSystem(DbProject project, int targetID)
            throws DbException {
        // Faster to enumerate from the built-in node
        DbSMSTargetSystem foundDbts = null;
        DbEnumeration dbEnum = ((DbSMSProject) project).getBuiltInTypeNode().getComponents()
                .elements(DbSMSBuiltInTypePackage.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSTargetSystem dbts = ((DbSMSBuiltInTypePackage) dbEnum.nextElement())
                    .getTargetSystem();
            if (dbts.getID().intValue() == targetID) {
                foundDbts = dbts;
                break;
            }
        }
        dbEnum.close();
        return foundDbts;
    }

    // Retrieve all OR target system,
    // return a ArrayList of DbSMSTargetSystem.
    public static List<Serializable> getORTargetSystem(DbProject project) throws DbException {
        return getTargetSystems(project, false);
    }

    public static List<Serializable> getTargetSystems(DbProject project, boolean inclJava)
            throws DbException {

        List<Serializable> arrayTS = new ArrayList<Serializable>();

        // Faster to enumerate from the built-in node
        DbEnumeration dbEnum = ((DbSMSProject) project).getBuiltInTypeNode().getComponents()
                .elements(DbSMSBuiltInTypePackage.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSTargetSystem dbts = ((DbSMSBuiltInTypePackage) dbEnum.nextElement())
                    .getTargetSystem();
            int rootID = AnyORObject.getRootIDFromTargetSystem(dbts);
            if (inclJava || (rootID != SGBD_JAVA)) {
                arrayTS.add(dbts);
            }
        }
        dbEnum.close();

        return arrayTS;
    }

    /*
     * public ArrayList getUserTargetSystems(DbProject project) throws DbException { ArrayList
     * arrayTS = new ArrayList();
     * 
     * // Énumère les DbSMSTargetSystem qui ont été créés mais qui ne sont pas nécessairement
     * présent dans le BuiltInTypeNode DbEnumeration dbEnum = ((DbSMSProject
     * )project).getComponents().elements(DbSMSTargetSystem.metaClass); while
     * (dbEnum.hasMoreElements()) { DbSMSTargetSystem dbts =
     * ((DbSMSTargetSystem)dbEnum.nextElement()); int rootID = dbts.getRootID().intValue
     * ();//AnyORObject.getRootIDFromTargetSystem(dbts); if(rootID > 999) arrayTS.add(dbts); }
     * dbEnum.close(); return arrayTS; }
     */

    // Retreive the Java target system if exist,
    // if not, create it.
    public DbSMSTargetSystem getTargetSystemJava(DbSMSProject project) throws DbException {
        DbSMSTargetSystem dbts = getSpecificTargetSystem(project, SGBD_JAVA);
        if (dbts == null) // if not created....
            dbts = createTargetSystem(project, SGBD_JAVA);
        return dbts;
    }

    // Create a new Target System
    public DbSMSTargetSystem createTargetSystem(DbProject project, int targetID) throws DbException {
        TargetSystemInfo targetInfo = (TargetSystemInfo) targetMap.get(new Integer(targetID));
        if (targetID == 999) {// UserTS.typ, doit demeurer
            int newUserTargetID = ((DbSMSProject) project).getNewUserTargetID();
            targetInfo = new TargetSystemInfo(newUserTargetID);
            targetMap.put(new Integer(targetInfo.getID()), targetInfo);
        }
        if (targetInfo == null)
            throw new RuntimeException("Type file not found for target ID " + targetID); // NOT LOCALIZABLE, runtime exception
        return createTargetSystem(project, targetInfo);
    }

    public DbSMSTargetSystem createTargetSystem(DbProject project, TargetSystemInfo targetInfo)
            throws DbException {
        DbSMSTargetSystem ts = null;
        //int targetInfoID = targetInfo.getID();
        //int userTsID;

        /*
         * ArrayList userDbTarget = getUserTargetSystems(project); for (int i = 0; i <
         * userDbTarget.size(); i++) { DbSMSTargetSystem userTs =
         * (DbSMSTargetSystem)userDbTarget.get(i); userTsID = userTs.getID().intValue();
         * if(targetInfoID == userTsID) return userTs; }
         */
        ts = new DbSMSTargetSystem((DbSMSProject) project, targetInfo.getName(), targetInfo
                .getVersion(), new Integer(targetInfo.getID()), new Integer(targetInfo.getRootID()));
        return ts;
    }

    public ArrayList<DbSMSTargetSystem> addTargetSystem(Component parent, DbObject semObj,
            boolean many,
            boolean showAll) throws DbException {
        ArrayList<DbSMSTargetSystem> added = new ArrayList<DbSMSTargetSystem>();
        if (semObj == null)
            return added;

        semObj.getDb().beginTrans(Db.READ_TRANS);
        TargetSystem targetSystem = TargetSystemManager.getSingleton();
        TargetSystemInfo targetInfo;
        DbProject project = (semObj instanceof DbProject) ? (DbProject) semObj : semObj
                .getProject();
        ArrayList<DefaultComparableElement> targets = new ArrayList<DefaultComparableElement>();
        Iterator<TargetSystemInfo> iter = targetSystem.getAllTargetSystemInfos().iterator();
        while (iter.hasNext()) {
            targetInfo = iter.next();
            // if show all, add every target system in the list
            // if not show all, just add new target systems
            if ((showAll)
                    || (TargetSystem.getSpecificTargetSystem(project, targetInfo.getID()) == null)) {
                String name = targetInfo.getName() + " " + targetInfo.getVersion();
                targets.add(new DefaultComparableElement(targetInfo, name));
            } // end if
        } // end while
        semObj.getDb().commitTrans();

        DefaultComparableElement[] items = new DefaultComparableElement[targets.size()];
        targets.toArray(items);
        CollationComparator comparator = new CollationComparator();
        SrSort.sortArray(items, items.length, comparator);
        int[] indices = null;
        if (!many) {
            indices = new int[1];
            indices[0] = LookupDialog.selectOne(parent, DbSMSTargetSystem.metaClass
                    .getGUIName(true), null, items, -1, comparator);
        } else {
            indices = LookupDialog.selectMany(parent, DbSMSTargetSystem.metaClass.getGUIName(true),
                    null, items, -1, comparator);
        } // end if

        if (indices.length == 0 || indices[0] < 0) {
            return added;
        } // end if

        semObj.getDb().beginTrans(
                Db.WRITE_TRANS,
                MessageFormat.format(DbListView.k0Creation,
                        new Object[] { DbSMSTargetSystem.metaClass.getGUIName() }));
        for (int i = 0; i < indices.length; i++) {
            targetInfo = (TargetSystemInfo) items[indices[i]].object;
            int targetID = targetInfo.getID();
            DbSMSTargetSystem smstargetsystem = TargetSystem.getSpecificTargetSystem(project,
                    targetID);
            if (targetID == 999) {
                smstargetsystem = targetSystem.createTargetSystem(project, targetID);
            }
            if (smstargetsystem == null) {
                smstargetsystem = targetSystem.createTargetSystem(project, targetInfo);
            } // end if
            added.add(smstargetsystem);
        } // end for
        semObj.getDb().commitTrans();
        return added;
    } // end addTargetSystem()

    // Original method
    public ArrayList<DbSMSTargetSystem> addTargetSystem(Component parent, DbObject semObj,
            boolean many)
            throws DbException {
        boolean showAll = false;
        ArrayList<DbSMSTargetSystem> list = addTargetSystem(parent, semObj, many, showAll);
        return list;
    } // end addTargetSystem()

    public TargetSystemInfo getNewUserTargetInfo(int newUserTargetID) throws DbException {
        return this.getNewUserTargetInfo(newUserTargetID, null, null);
    } // end getNewUserTargetInfo()

    public TargetSystemInfo getNewUserTargetInfo(int newUserTargetID, String targetName,
            String targetVersion) throws DbException {
        TargetSystemInfo targetInfo = new TargetSystemInfo(newUserTargetID, targetName,
                targetVersion);
        targetMap.put(new Integer(targetInfo.getID()), targetInfo);
        return targetInfo;
    } // end getNewUserTargetInfo()

    public void updateUserTargetSystem(DbProject project) throws DbException {
        TargetSystemInfo targetInfo = null;
        String targetName = null;
        String targetVersion = null;
        DbEnumeration dbEnum = ((DbSMSProject) project).getBuiltInTypeNode().getComponents()
                .elements(DbSMSBuiltInTypePackage.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSTargetSystem dbts = ((DbSMSBuiltInTypePackage) dbEnum.nextElement())
                    .getTargetSystem();
            if (dbts.getRootID().intValue() > 999) {
                targetName = dbts.getName();
                targetVersion = dbts.getVersion();
                // int newUserTargetID =
                // ((DbSMSProject)project).getNewUserTargetID();
                targetInfo = new TargetSystemInfo(dbts.getID().intValue(), targetName,
                        targetVersion);
                targetMap.put(new Integer(targetInfo.getID()), targetInfo);
            }
        }
        dbEnum.close();

    } // end getNewUserTargetInfo()

} // end TargetSystem
