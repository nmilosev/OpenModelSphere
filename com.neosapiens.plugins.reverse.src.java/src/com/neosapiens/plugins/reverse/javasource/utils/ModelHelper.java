package com.neosapiens.plugins.reverse.javasource.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVConstructor;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVInitBlock;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.oo.java.db.DbJVParameter;

public class ModelHelper {

    /**
     * Search for a package contained in root object
     * 
     * @param packageName
     *            The package to search
     * @param root
     *            The root of the search
     * @param createdPackages
     *            List of already created packages
     * @return If found, the package, else null
     * @throws DbException
     */
    public static DbObject findPackage(String packageName, DbObject root,
            Map<String, DbJVPackage> createdPackages) throws DbException {
        String shortName;
        DbObject foundPackage;
        
        if (packageName == null) {
        	return null;
        }
        
        if (createdPackages != null && createdPackages.containsKey(packageName)) {
            return createdPackages.get(packageName);
        } else if (packageName.contains(".")) {
            int index = packageName.indexOf(".");
            shortName = packageName.substring(0, index);
            DbObject newRoot = root.findComponentByName(DbJVPackage.metaClass, shortName);
            if (newRoot != null)
                foundPackage = findPackage(packageName.substring(index + 1), newRoot, null);
            else
                foundPackage = null;
        } else {
            shortName = packageName;
            foundPackage = root.findComponentByName(DbJVPackage.metaClass, shortName);
        }

        return foundPackage;
    }

    /**
     * Search for an object in a package
     * 
     * @param root
     *            The root DbObject
     * @param createdPackages
     *            The list of already created packages
     * @param metaClass
     *            Metaclass of object to search
     * @param packageName
     *            The package where object should be sought
     * @param objectName
     *            The name of the object to search
     * @return If found, the object, else null
     * @throws DbException
     */
    public static DbObject findObjectInPackage(DbObject root,
            Map<String, DbJVPackage> createdPackages, MetaClass metaClass, String packageName,
            String objectName) throws DbException {
        DbObject foundObject;
        if (objectName == null) {
            foundObject = null;
        } else if (packageName == null) {
        	foundObject = findObjectRecursive(root, metaClass, objectName);
        } else {
            DbObject pack = findPackage(packageName, root, createdPackages);
            if (pack == null) {
                ModelHelper.createPackage(packageName, root, createdPackages);
                pack = findPackage(packageName, root, createdPackages);
            }
            foundObject = findObjectRecursive(pack, metaClass, objectName);
        }

        return foundObject;
    }

    private static DbObject findObjectRecursive(DbObject root, MetaClass metaClass, String name)
            throws DbException {
        DbObject childFound = null;
        DbEnumeration dbEnum = root.getComponents().elements(metaClass);
        while (dbEnum.hasMoreElements()) {
            DbObject child = dbEnum.nextElement();
            String childName = child.getSemanticalName(DbObject.SHORT_FORM);
            boolean sameName = name.equals(childName);

            if (sameName) {
                childFound = child;
                break;
            } else {
                childFound = findObjectRecursive(child, metaClass, name);
                if (childFound != null)
                    break;
            }
        }
        dbEnum.close();
        return childFound;
    }

    public static List<DbObject> getCreatedObjects(DbObject root, boolean classes, boolean methods, boolean fields, boolean parameters) throws DbException {
        List<DbObject> list = new ArrayList<DbObject>();
        DbEnumeration dbEnum;
        
        if (root instanceof DbJVClassModel) {
        	DbJVClassModel model = (DbJVClassModel)root;
        	dbEnum = root.getComponents().elements(DbJVClass.metaClass);
        } else {
        	dbEnum = root.getComponents().elements(DbJVClass.metaClass);
        }
        
        while (dbEnum.hasMoreElements()) {
            DbObject child = dbEnum.nextElement();
            if (methods) list.addAll(getCreatedMethodsInObject(child));
            if (fields) list.addAll(getCreatedFieldsInObject(child));
            if (parameters) list.addAll(getCreatedParametersInObject(child));
            if (classes) list.add(child);
            list.addAll(getCreatedObjects(child,classes,methods,fields,parameters));
        }
        dbEnum.close();
        return list;
    }

    private static List<DbObject> getCreatedMethodsInObject(DbObject root) throws DbException {
        List<DbObject> list = new ArrayList<DbObject>();
        DbEnumeration dbEnum = root.getComponents().elements(DbJVMethod.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbObject child = dbEnum.nextElement();
            list.add(child);
        }
        dbEnum.close();
        dbEnum = root.getComponents().elements(DbJVConstructor.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbObject child = dbEnum.nextElement();
            list.add(child);
        }
        dbEnum.close();
        return list;
    }

    private static List<DbObject> getCreatedFieldsInObject(DbObject root) throws DbException {
        List<DbObject> list = new ArrayList<DbObject>();
        DbEnumeration dbEnum = root.getComponents().elements(DbJVDataMember.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbObject child = dbEnum.nextElement();
            list.add(child);
        }
        dbEnum.close();
        return list;
    }

    private static List<DbObject> getCreatedParametersInObject(DbObject root) throws DbException {
        List<DbObject> list = new ArrayList<DbObject>();
        DbEnumeration dbEnum = root.getComponents().elements(DbJVParameter.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbObject child = dbEnum.nextElement();
            list.add(child);
        }
        dbEnum.close();
        return list;
    }

    /**
     * Create all the hierarchy of a package
     * 
     * @param packageName
     *            The long name of the package
     * @param root
     *            The root model object
     * @param createdPackages
     *            The list of already created packages
     * @param accumulator
     *            Should be null
     * @throws DbException
     */
    public static void createPackage(String packageName, DbObject root,
            Map<String, DbJVPackage> createdPackages) throws DbException {
        createPackageRecursive(packageName, root, createdPackages, "");
    }

    private static void createPackageRecursive(String packageName, DbObject root,
            Map<String, DbJVPackage> createdPackages, String accumulator) throws DbException {
    	
        if ((packageName == null) || packageName.equals(""))
            return;
        
        String shortName;
        DbJVPackage pack;
        if (packageName.contains(".")) {
            int index = packageName.indexOf(".");
            shortName = packageName.substring(0, index);
            DbObject newRoot = root.findComponentByName(null, shortName);
            accumulator += shortName;
            if (newRoot == null) {
                pack = new DbJVPackage(root);
                pack.setName(shortName);
                newRoot = pack;
                createdPackages.put(accumulator, pack);
            }
            accumulator += ".";
            createPackageRecursive(packageName.substring(index + 1), newRoot, createdPackages,
                    accumulator);
        } else {
            shortName = packageName;
            accumulator += shortName;
            if (root.findComponentByName(null, shortName) == null) {
                pack = new DbJVPackage(root);
                pack.setName(shortName);
                createdPackages.put(accumulator, pack);
            }
        }
    }

    //    public static List<DbJVPackage> getPackages(DbObject root) throws DbException {
    //        List<DbJVPackage> list = new ArrayList<DbJVPackage>();
    //        DbEnumeration dbEnum = root.getComponents().elements(DbJVPackage.metaClass);
    //        while (dbEnum.hasMoreElements()) {
    //            DbObject child = dbEnum.nextElement();
    //            if (child instanceof DbJVPackage)
    //                list.add((DbJVPackage) child);
    //        }
    //        dbEnum.close();
    //        return list;
    //    }

}
