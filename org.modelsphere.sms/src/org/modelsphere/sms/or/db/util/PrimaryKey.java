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
package org.modelsphere.sms.or.db.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.sms.db.DbSMSBuiltInTypePackage;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORBuiltInType;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORIndexKey;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.screen.DeleteKeysAndRulesFrame.DeleteKeysOptions;

public class PrimaryKey {
    //members
    private TypeComparator typeComparator = new TypeComparator();

    //singleton
    private static PrimaryKey instance = null;

    private PrimaryKey() {
    }

    public static PrimaryKey getInstance() {
        if (instance == null) {
            instance = new PrimaryKey();
        }

        return instance;
    }

    //
    // long types
    //
    //associate long type for each target system
    Map<DbSMSTargetSystem, DbORBuiltInType> longTypes = null;

    private void putLongType(DbSMSTargetSystem target) throws DbException {
        DbSMSBuiltInTypePackage pack = target.getBuiltInTypePackage();
        DbORBuiltInType longType = (DbORBuiltInType) pack.findComponentByName(
                DbORBuiltInType.metaClass, "LONG");
        if (longType == null) {
            longType = (DbORBuiltInType) pack.findComponentByName(DbORBuiltInType.metaClass,
                    "LONG INTEGER");
        }

        if (longType != null) {
            longTypes.put(target, longType);
        }
    }

    public DbORBuiltInType getLongType(DbSMSTargetSystem target) throws DbException {
        if (longTypes == null) {
            longTypes = new HashMap<DbSMSTargetSystem, DbORBuiltInType>();
            putLongType(target);
        }

        DbORBuiltInType type = longTypes.get(target);
        if (type == null) {
            putLongType(target);
            type = longTypes.get(target);
        }

        return type;
    }

    //
    // built-in types
    //
    private Map<DbSMSTargetSystem, List<TypeInfo>> m_types;

    public List<TypeInfo> getTypes(DbSMSTargetSystem target) throws DbException {
        if (m_types == null) {
            m_types = new HashMap<DbSMSTargetSystem, List<TypeInfo>>();
            addBuiltInTypes(target);
        }

        List<TypeInfo> types = m_types.get(target);
        if (types == null) {
            addBuiltInTypes(target);
            types = m_types.get(target);
        }

        return types;
    }

    private void addBuiltInTypes(DbSMSTargetSystem target) throws DbException {
        DbSMSBuiltInTypePackage pack = target.getBuiltInTypePackage();
        List<TypeInfo> types = new ArrayList<TypeInfo>();
        DbEnumeration enu = pack.getComponents().elements(DbORBuiltInType.metaClass);
        while (enu.hasMoreElements()) {
            DbORBuiltInType type = (DbORBuiltInType) enu.nextElement();
            String typename = type.buildFullNameString();
            TypeInfo info = new TypeInfo(type, typename);
            types.add(info);
        } //end while
        enu.close();

        Collections.sort(types, typeComparator);
        m_types.put(target, types);
    }

    //
    // generate
    //
    public int generate(DbORDataModel dataModel, Options options, String actionName) {
        int nbGenerated = 0;

        try {
            //start transaction
            dataModel.getDb().beginWriteTrans(actionName);

            //get metaclasses
            DbSMSTargetSystem target = dataModel.getTargetSystem();
            int rootID = AnyORObject.getRootIDFromTargetSystem(target);
            MetaClass[] metaClasses = AnyORObject.getTargetMetaClasses(rootID);

            //for each table
            DbEnumeration enu = dataModel.getComponents().elements(DbORTable.metaClass);
            while (enu.hasMoreElements()) {
                DbORTable table = (DbORTable) enu.nextElement();
                boolean generated = generatePK(metaClasses, table, options);
                if (generated) {
                    nbGenerated++;
                }
            }
            enu.close();

            //commit transaction
            dataModel.getDb().commitTrans();
        } catch (DbException ex) {
            //report exceptions
        }

        return nbGenerated;
    } //end generate()

    private boolean generatePK(MetaClass[] metaClasses, DbORTable table, Options options)
            throws DbException {
        boolean PKGenerated = false;

        //find PK, if any
        boolean pkFound = false;
        DbEnumeration enu = table.getComponents().elements(DbORPrimaryUnique.metaClass);
        while (enu.hasMoreElements()) {
            DbORPrimaryUnique puk = (DbORPrimaryUnique) enu.nextElement();
            if (puk.isPrimary()) {
                pkFound = true;
                break;
            }
        }
        enu.close();

        if (!pkFound) {
            DbORPrimaryUnique pk = (DbORPrimaryUnique) table
                    .createComponent(metaClasses[AnyORObject.I_PRIMARYUNIQUE]);
            PKGenerated = true;

            if (options.generateSurrogate) {
                //create the long 'id'
                DbORColumn idColumn = (DbORColumn) table
                        .createComponent(metaClasses[AnyORObject.I_COLUMN]);
                idColumn.setName(options.surrogateName);

                //set its type
                idColumn.setType(options.surrogateType);

                //TODO set as 1st column
                MetaRelationN relN = DbORTable.fComponents;
                int oldIndex = table.getNbNeighbors(relN) - 1;
                table.reinsert(relN, oldIndex, 0);

                //add to pk
                pk.addToColumns(idColumn);
                idColumn.setNull(false);
            } //end if
        } //end if

        return PKGenerated;
    } //end generatePK()

    public static int deleteKeys(DbORDataModel dataModel, DeleteKeysOptions options)
            throws DbException {
        int nCount = 0;
        DbEnumeration dbEnum = dataModel.getComponents().elements(DbORAbsTable.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbEnumeration enum2 = dbEnum.nextElement().getComponents().elements();
            while (enum2.hasMoreElements()) {
                DbObject dbo = enum2.nextElement();
                if (dbo instanceof DbORPrimaryUnique) {
                    DbORPrimaryUnique puk = (DbORPrimaryUnique) dbo;

                    //delete key?
                    boolean deleteKey = puk.isPrimary() ? options.deletePrimaryKeys
                            : options.deleteUniqueKeys;
                    if (deleteKey) {
                        boolean deleteCols = puk.isPrimary() ? options.deletePrimaryColumns
                                : options.deleteUniqueColumns;

                        if (deleteCols) {
                            DbEnumeration enu = puk.getColumns().elements(DbORColumn.metaClass);
                            while (enu.hasMoreElements()) {
                                DbORColumn col = (DbORColumn) enu.nextElement();
                                deleteColumn(col);
                            }
                            enu.close();
                        }

                        puk.remove();
                        nCount++;
                    }
                }
            }
            enum2.close();
        } //end if

        dbEnum.close();
        return nCount;
    }

    private static void deleteColumn(DbORColumn col) throws DbException {
        //list column-index links
        List<DbORIndexKey> keys = new ArrayList<DbORIndexKey>();
        DbRelationN relN = col.getIndexKeys();
        DbEnumeration enu = relN.elements(DbORIndexKey.metaClass);
        while (enu.hasMoreElements()) {
            DbORIndexKey key = (DbORIndexKey) enu.nextElement();
            keys.add(key);
        }
        enu.close();

        //delete column-index links
        for (DbORIndexKey key : keys) {
            key.remove();
        }

        //delete column
        col.remove();

    }

    //
    // inner classes
    //
    public static class TypeInfo {
        public DbORBuiltInType m_builtInType;
        public String m_typename;

        TypeInfo(DbORBuiltInType builtInType, String typename) {
            m_builtInType = builtInType;
            m_typename = typename;
        }
    }

    public static class Options {
        public boolean generateSurrogate = false;
        public String surrogateName = "id";
        public DbORBuiltInType surrogateType = null;
    }

    private static class TypeComparator implements Comparator<TypeInfo> {
        @Override
        public int compare(TypeInfo o1, TypeInfo o2) {
            String s1 = o1.m_typename;
            String s2 = o2.m_typename;
            int comparison = s1.compareTo(s2);
            return comparison;
        }

    }

}
