/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/
package org.modelsphere.sms.oo.java.db.util;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.sms.db.DbSMSBuiltInTypeNode;
import org.modelsphere.sms.db.DbSMSBuiltInTypePackage;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSLinkModel;
import org.modelsphere.sms.db.DbSMSNotice;
import org.modelsphere.sms.db.DbSMSNoticeGo;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.db.srtypes.SMSHorizontalAlignment;
import org.modelsphere.sms.oo.db.DbOOAbsPackage;
import org.modelsphere.sms.oo.db.DbOOAdt;
import org.modelsphere.sms.oo.db.DbOOAdtGo;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVInheritance;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.oo.java.db.DbJVParameter;
import org.modelsphere.sms.oo.java.db.DbJVPrimitiveType;
import org.modelsphere.sms.oo.java.db.srtypes.JVClassCategory;
import org.modelsphere.sms.oo.java.features.JavaToolkit;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class JavaUtility {

    // Implements Singleton Design Pattern
    private JavaUtility() {
    }

    private static JavaUtility g_singleInstance = null;

    public static JavaUtility getSingleton() {
        if (g_singleInstance == null) {
            g_singleInstance = new JavaUtility();
        }

        return g_singleInstance;
    } // end getSingleton()

    // ************************************************************************
    // Utility public methods
    // ************************************************************************

    public String uncapitalize(String originalName) {
        int len = originalName.length();
        if (len == 0)
            return "";

        char ch = Character.toLowerCase(originalName.charAt(0));
        String newName = (len > 1) ? (ch + originalName.substring(1)) : ch + "";
        return newName;
    }

    public String capitalize(String originalName) {
        int len = originalName.length();
        if (len == 0)
            return "";

        char ch = Character.toUpperCase(originalName.charAt(0));
        String newName = (len > 1) ? (ch + originalName.substring(1)) : ch + "";
        return newName;
    }

    public DbOOAdt findReflectClass(DbJVClassModel classModel, String className, JavaInfo info)
            throws DbException {
        DbOOAbsPackage container = classModel;

        // check for built-in types
        DbOOAdt reflectClass = findBuiltinType(classModel, className);
        if (reflectClass != null) {
            return reflectClass;
        }

        // find class, create it does not exist
        StringTokenizer st = new StringTokenizer(className, ".");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();

            if (st.hasMoreTokens()) {
                // find package, create it does not exist
                DbOOAbsPackage pack = (DbOOAbsPackage) container.findComponentByName(
                        DbJVPackage.metaClass, token);
                if (pack == null) {
                    pack = new DbJVPackage(container);
                    pack.setName(token);

                    if (info != null) {
                        pack.setVersion(info.fVersion);
                        pack.setAuthor(info.fAuthor);
                    }
                }

                container = pack;
            } else {
                // find class
                reflectClass = (DbJVClass) container
                        .findComponentByName(DbJVClass.metaClass, token);
                if (reflectClass == null) {
                    reflectClass = createReflectClass(classModel, container, className, info);
                }
            } // end if
        } // end while

        return reflectClass;
    } // end findReflectClass()

    public DbJVClass createReflectClass(DbJVClassModel classModel, DbOOAbsPackage container,
            String className, JavaInfo info) throws DbException {
        DbJVClass reflectClass = null;

        try {
            Class claz = Class.forName(className);
            String name = claz.getName();
            int idx = name.lastIndexOf('.');
            name = name.substring(idx + 1);
            JVClassCategory category = (claz.isInterface()) ? JVClassCategory
                    .getInstance(JVClassCategory.INTERFACE) : JVClassCategory
                    .getInstance(JVClassCategory.CLASS);
            reflectClass = new DbJVClass(container, category);
            reflectClass.setName(name);

            // create contents
            createSupertypes(classModel, reflectClass, claz, info);
            createFields(classModel, reflectClass, claz, info);
            createMethods(classModel, reflectClass, claz, info);

        } catch (ClassNotFoundException ex) {
            // ignore
        } // end try

        return reflectClass;
    } // end createReflectClass()

    private static final String OBJECT_CLASS = "java.lang.Object";

    private void createSupertypes(DbJVClassModel classModel, DbJVClass reflectClass, Class claz,
            JavaInfo info) throws DbException {
        Class superclass = claz.getSuperclass();
        String classname = superclass == null ? null : superclass.getName();

        if ((classname != null) && !(OBJECT_CLASS.equals(classname))) {
            DbOOAdt reflectType = findReflectClass(classModel, classname, info);
            if (reflectType instanceof DbJVClass) {
                new DbJVInheritance(reflectClass, (DbJVClass) reflectType);
            }
        } //end if

        Class[] superinterfaces = claz.getInterfaces();
        for (Class superinterface : superinterfaces) {
            classname = superinterface == null ? null : superinterface.getName();

            if ((classname != null) && !(OBJECT_CLASS.equals(classname))) {
                DbOOAdt reflectType = findReflectClass(classModel, classname, info);
                if (reflectType instanceof DbJVClass) {
                    new DbJVInheritance(reflectClass, (DbJVClass) reflectType);
                }
            } //end if
        } //end for
    } //end createSupertypes()

    private void createFields(DbJVClassModel classModel, DbJVClass reflectClass, Class claz,
            JavaInfo info) throws DbException {
        // for each field
        Field[] fields = claz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            int modifiers = field.getModifiers();
            if ((modifiers & Modifier.PUBLIC) != 0) {
                String fieldName = field.getName();
                DbJVDataMember reflectField = new DbJVDataMember(reflectClass);
                reflectField.setName(fieldName);

                Class type = field.getType();
                if (type != null) {
                    String typeName = type.getName();
                    String typeUse = "";
                    DbOOAdt reflectType = null;

                    if (typeName.charAt(0) == '[') {
                        typeUse = getTypeUse(typeName);
                        reflectType = extractActualName(classModel, typeName, typeUse, info);
                    } else {
                        reflectType = findReflectClass(classModel, typeName, info);
                    } // end if

                    if (reflectType != null) {
                        reflectField.setType(reflectType);
                        reflectField.setTypeUse(typeUse);
                    } // end if
                } // end if
            } // end if
        } // end for
    } // end createFields()

    private void createMethods(DbJVClassModel classModel, DbJVClass reflectClass, Class claz,
            JavaInfo info) throws DbException {
        // for each method
        Method[] methods = claz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            int modifiers = method.getModifiers();

            boolean doCreate = (((modifiers & Modifier.PUBLIC) != 0) && !method.isSynthetic());

            if (doCreate) {
                // set name
                String methodName = method.getName();
                DbJVMethod reflectMethod = new DbJVMethod(reflectClass);
                reflectMethod.setName(methodName);

                // set static
                reflectMethod.setStatic((modifiers & Modifier.STATIC) != 0);

                // set return type
                Class returnType = method.getReturnType();
                TypeAndTypeUseStruct struct = getTypeAndTypeUse(classModel, returnType, info);

                if (struct != null) {
                    reflectMethod.setReturnType(struct.fType);
                    reflectMethod.setTypeUse(struct.fTypeUse);
                } // end if

                createParameters(classModel, reflectMethod, method, info);
            } // end if, javaVersion
        } // end for

    } // end createMethods()

    private TypeAndTypeUseStruct getTypeAndTypeUse(DbJVClassModel classModel, Class returnType,
            JavaInfo info) throws DbException {
        TypeAndTypeUseStruct struct = null;

        if (returnType != null) {
            String typeName = returnType.getName();
            String typeUse = "";
            DbOOAdt reflectType = null;

            if (typeName.charAt(0) == '[') {
                typeUse = getTypeUse(typeName);
                reflectType = extractActualName(classModel, typeName, typeUse, info);
            } else {
                reflectType = findReflectClass(classModel, typeName, info);
            } // end if

            struct = new TypeAndTypeUseStruct(reflectType, typeUse);

        } // end if
        return struct;
    }

    private void createParameters(DbJVClassModel classModel, DbJVMethod reflectMethod,
            Method method, JavaInfo info) throws DbException {
        Class[] paramTypes = method.getParameterTypes();

        int i = 0;
        for (Class paramType : paramTypes) {
            i++;
            TypeAndTypeUseStruct struct = getTypeAndTypeUse(classModel, paramType, info);
            String name = "p" + i;

            DbJVParameter reflectParam = new DbJVParameter(reflectMethod);
            reflectParam.setType(struct.fType);
            reflectParam.setTypeUse(struct.fTypeUse);
            reflectParam.setName(name);
        } //end for

    } //end createParameters()

    public DbOOAdt findBuiltinType(DbJVClassModel classModel, String typename) throws DbException {
        DbOOAdt builtinType = null;
        DbSMSTargetSystem ts = classModel.getTargetSystem();
        String version = "";
        String nodeName = ts.getName() + ' ' + version;

        DbSMSProject project = (DbSMSProject) classModel.getCompositeOfType(DbSMSProject.metaClass);
        DbRelationN relN = project.getComponents();
        DbEnumeration dbEnum = relN.elements(DbSMSBuiltInTypeNode.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSBuiltInTypeNode node = (DbSMSBuiltInTypeNode) dbEnum.nextElement();
            DbSMSBuiltInTypePackage pack = (DbSMSBuiltInTypePackage) node.findComponentByName(
                    DbSMSBuiltInTypePackage.metaClass, nodeName);
            if (pack != null) {
                DbJVPrimitiveType type = (DbJVPrimitiveType) pack.findComponentByName(
                        DbJVPrimitiveType.metaClass, typename);
                if (type != null) {
                    builtinType = type;
                    break;
                } // end if
            } // end if
        } // end while
        dbEnum.close();

        return builtinType;
    }

    public String getTypeUse(String typeName) {
        String typeUse = "";
        for (int i = 0; i < typeName.length(); i++) {
            char c = typeName.charAt(i);
            if (c == '[') {
                typeUse += "[]";
            } else {
                break;
            }
        } // end for

        return typeUse;
    }

    public DbOOAdt extractActualName(DbJVClassModel classModel, String typeName, String typeUse,
            JavaInfo info) throws DbException {
        int idx = (typeUse.length() / 2);
        // String actualTypeName = typeName.substring(idx);
        DbOOAdt actualType = null;
        char ch = typeName.charAt(idx);
        switch (ch) {
        case 'B':
            actualType = findBuiltinType(classModel, "byte");
            break;
        case 'C':
            actualType = findBuiltinType(classModel, "char");
            break;
        case 'D':
            actualType = findBuiltinType(classModel, "double");
            break;
        case 'F':
            actualType = findBuiltinType(classModel, "float");
            break;
        case 'I':
            actualType = findBuiltinType(classModel, "int");
            break;
        case 'J':
            actualType = findBuiltinType(classModel, "long");
            break;
        case 'L':
            idx++;
            String actualName = typeName.substring(idx, typeName.length() - 1);
            actualType = findReflectClass(classModel, actualName, info);
            break;
        case 'S':
            actualType = findBuiltinType(classModel, "short");
            break;
        case 'Z':
            actualType = findBuiltinType(classModel, "boolean");
            break;
        // case 'V'oid, returns null
        } // end switch

        return actualType;
    } // end extractActualName()

    public DbOOAdtGo createClassAndGO(DbOODiagram diagram, DbJVClass relativeClass,
            JVClassCategory category, int xpos, int ypos) throws DbException {
        DbOOAdtGo newClassGo = null;
        DbOOAdtGo relativeClassGo = getGraphicalObject(relativeClass, diagram);
        Rectangle relativeClassGoRect = relativeClassGo.getRectangle();

        DbObject composite = relativeClass.getComposite(); // Package or class
        // model

        DbJVClass newClass = null;
        if (composite instanceof DbJVPackage) {
            newClass = new DbJVClass((DbJVPackage) composite, category);
        } else if (composite instanceof DbJVClassModel) {
            newClass = new DbJVClass((DbJVClassModel) composite, category);
        } // end composite()

        if (newClass == null)
            return newClassGo;

        newClassGo = new DbOOAdtGo(diagram, newClass);
        moveGraphicalRep(newClassGo, relativeClassGo, xpos, ypos);
        /*
         * Rectangle newClassGoRect = newClassGo.getRectangle(); int x = relativeClassGoRect.x +
         * (xpos * 60); int y = relativeClassGoRect.y + (ypos * 60); x += (xpos > 0) ?
         * relativeClassGoRect.width : 0; y += (ypos > 0) ? relativeClassGoRect.height : 0;
         * newClassGoRect = new Rectangle(x, y, newClassGoRect.width, newClassGoRect.height);
         * newClassGo.setRectangle(newClassGoRect);
         */

        return newClassGo;
    } // end createClass()

    public void moveGraphicalRep(DbSMSGraphicalObject graphicalObject,
            DbSMSGraphicalObject referenceObject, int xpos, int ypos) throws DbException {
        Rectangle referenceObjectRect = referenceObject.getRectangle();
        int x = referenceObjectRect.x + (xpos * 60);
        int y = referenceObjectRect.y + (ypos * 60);
        x += (xpos > 0) ? referenceObjectRect.width : 0;
        y += (ypos > 0) ? referenceObjectRect.height : 0;

        Rectangle graphicalObjectRect = referenceObject.getRectangle();
        Rectangle rect = new Rectangle(x, y, graphicalObjectRect.width, graphicalObjectRect.height);
        graphicalObject.setRectangle(rect);
    }

    public DbOOAdtGo getGraphicalObject(DbJVClass claz, DbOODiagram diagram) throws DbException {
        DbOOAdtGo graphicalClass = null;

        DbRelationN relN = claz.getClassifierGos();
        DbEnumeration dbEnum = relN.elements(DbOOAdtGo.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbOOAdtGo go = (DbOOAdtGo) dbEnum.nextElement();
            DbOODiagram diag = (DbOODiagram) go.getCompositeOfType(DbOODiagram.metaClass);
            if (diag.equals(diagram)) {
                graphicalClass = go;
                break;
            }
        } // end while
        dbEnum.close();

        return graphicalClass;
    } // end getGraphicalObject()

    public DbSMSLinkModel getLinkModel(DbSMSClassifierGo go) throws DbException {
        DbSMSProject project = (DbSMSProject) go.getCompositeOfType(DbSMSProject.metaClass);
        DbSMSLinkModel linkModel = (DbSMSLinkModel) project.findComponentByName(
                DbSMSLinkModel.metaClass, "linkModel");
        if (linkModel == null) {
            linkModel = new DbSMSLinkModel(project);
            linkModel.setName("linkModel");
        }

        return linkModel;
    } // end getLinkModel()

    public DbSMSNoticeGo createNoteAndGO(DbOODiagram diagram, DbSMSGraphicalObject referenceObject,
            String desc, int xpos, int ypos) throws DbException {
        // create notice
        DbJVClassModel model = (DbJVClassModel) diagram
                .getCompositeOfType(DbJVClassModel.metaClass);
        DbSMSNotice notice = new DbSMSNotice(model);
        SMSHorizontalAlignment alignment = SMSHorizontalAlignment
                .getInstance(SMSHorizontalAlignment.LEFT);
        notice.setAlignment(alignment);
        notice.setDescription(desc);

        // and its graphical representation
        DbSMSNoticeGo noticeGo = new DbSMSNoticeGo(diagram);
        noticeGo.setNotice(notice);
        moveGraphicalRep(noticeGo, referenceObject, xpos, ypos);

        return noticeGo;
    } // end createNoteAndGO()

    /*
     * Create built-in classes by reflection; only classes in packageList will be created. Note :
     * can be long, in such case, consider to implement a worker
     * 
     * @param classModel : the destination class model in which classes will be created
     * 
     * @param packageList : an array of strings, such as "java.lang" and "java.util".
     */
    public void createBuiltInClasses(DbJVClassModel classModel, ArrayList packageList)
            throws DbException {
        JavaToolkit toolkit = JavaToolkit.getSingleton();

        Object[] jarFiles = toolkit.getSystemJarFiles();
        ArrayList classList = new ArrayList();
        toolkit.fillClassList(jarFiles, packageList, classList);

        Iterator iter = classList.iterator();
        while (iter.hasNext()) {
            String className = (String) iter.next();
            findBuiltinType(classModel, className);
        } // end while
    } // end createBuiltInClasses()

    public JavaInfo getJavaVersion(File jreHome) {
        JavaInfo info = new JavaInfo(jreHome);
        return info;
    }

    public static class JavaInfo {
        public String fVersion;
        public String fAuthor;

        public JavaInfo(File jreHome) {
            // set version
            if (jreHome != null && jreHome.exists()) {
                File rtfile = new File(jreHome, "lib" + File.separator + "rt.jar");
                if (rtfile.exists()) {
                    try {
                        JarFile rtjar = new JarFile(rtfile);
                        Manifest manifest = rtjar.getManifest();
                        if (manifest != null) {
                            Attributes attributes = manifest.getMainAttributes();
                            if (attributes != null) {
                                fVersion = attributes.getValue("Implementation-Title") + " "
                                        + attributes.getValue("Implementation-Version");
                                fAuthor = attributes.getValue("Implementation-Vendor");
                            } // end if
                        } // end if
                        rtjar.close();
                    } catch (IOException ex) {
                        fVersion = null;
                        fAuthor = null;
                    } // end try
                } // end if
            } // end if
        }
    }

    private static class TypeAndTypeUseStruct {
        public DbOOAdt fType;
        public String fTypeUse;

        public TypeAndTypeUseStruct(DbOOAdt type, String typeUse) {
            fType = type;
            fTypeUse = typeUse;

        }
    }

} // end JavaUtility
