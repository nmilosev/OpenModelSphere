package com.neosapiens.plugins.reverse.javasource.actions;

import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.sms.db.DbSMSBuiltInTypeNode;
import org.modelsphere.sms.db.DbSMSBuiltInTypePackage;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.db.util.Extensibility;
import org.modelsphere.sms.oo.db.DbOOAdt;
import org.modelsphere.sms.oo.db.srtypes.OOTypeUseStyle;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVConstructor;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVInheritance;
import org.modelsphere.sms.oo.java.db.DbJVInitBlock;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.oo.java.db.DbJVParameter;
import org.modelsphere.sms.oo.java.db.DbJVPrimitiveType;
import org.modelsphere.sms.oo.java.db.srtypes.JVClassCategory;
import org.modelsphere.sms.oo.java.db.srtypes.JVVisibility;

import com.neosapiens.plugins.reverse.javasource.international.JavaSourceReverseLocaleMgr;
import com.neosapiens.plugins.reverse.javasource.utils.DeclarationOfClassOrInterface;
import com.neosapiens.plugins.reverse.javasource.utils.DeclarationOfMethod;
import com.neosapiens.plugins.reverse.javasource.utils.FieldOfClass;
import com.neosapiens.plugins.reverse.javasource.utils.InitializationBlockOfClass;
import com.neosapiens.plugins.reverse.javasource.utils.ModelHelper;
import com.neosapiens.plugins.reverse.javasource.utils.ParameterOfMethode;
import com.neosapiens.plugins.reverse.javasource.utils.PutClassesFirstComparator;
import com.neosapiens.plugins.reverse.javasource.utils.SemanticDeclaration;

public class ModelSemanticAction implements SemanticAction {

    private final static String[] PRIMITIVES = { "boolean", "byte", "char", "double", "float",
            "int", "long", "short", "void" };
    private DbJVClassModel model;
    /**
     * This map is used to find already declared types, lazily, because the key is the short class
     * name
     */
    private Map<String, DeclarationOfClassOrInterface> lazyTypes;
    /** This map is used to find already declared types. Key is the full name (Ex: java.lang.String) */
    private Map<String, DeclarationOfClassOrInterface> fullTypes;
    private Map<String, DbJVPrimitiveType> primitiveTypes;
    private Map<String, DbJVClass> createdTypes;
    /** This map contains packages created by the files parsed */
    private Map<String, DbJVPackage> createdPackages;
    /**
     * Items added to this list are declarations parsed that will be executed once every file has
     * been parsed
     */
    private List<SemanticDeclaration> modelObjects;
    private SemanticAction realAction;
    /**
     * Objects used by parsed files are placed in this unknown package when there's no way to find
     * where to place it
     */
    private DbJVPackage unknownPackage;
    /** This list will contains packages where we parsed java code */
    private final List<DbJVPackage> ownedPackages;
    private List<String> currentImports;
    /** This set contains all the imports parsed in all .java files */
    private HashSet<String> globalImports;
    /** This map contains imports grouped by class. Key is full class name */
    private Map<String, List<String>> importsByClass;

    private Controller controller;
    private boolean createMethods;
    private boolean createFields;
    private int startPercent;
    private int endPercent;

    private String pattern = JavaSourceReverseLocaleMgr.misc.getString("0SuccessfullyCreated");

    private class RealModelSemanticAction extends SemanticActionAdapter {

        @SuppressWarnings("unused")
        @Override
        public void createClass(DeclarationOfClassOrInterface decl) throws SemanticActionError {
            try {
                DbJVClass clazz;
                if (!decl.getClassOrInterfaceName().equals(decl.getCurrentClass())) {
                    DbJVClass parent = (DbJVClass) tryGetTypeOrCreateClass(decl.getCurrentClass(),
                            decl.getCurrentPackage(), getLongClassName(decl));
                    clazz = (DbJVClass) parent.findComponentByName(DbJVClass.metaClass,
                            decl.getClassOrInterfaceName());
                } else {
                    clazz = (DbJVClass) tryGetTypeOrCreateClass(decl.getClassOrInterfaceName(),
                            decl.getCurrentPackage(), getLongClassName(decl));
                }

                JVClassCategory interfaceCat = JVClassCategory
                        .getInstance(JVClassCategory.INTERFACE);
                for (String impl : decl.getExtendsList()) {
                    DbObject obj = tryGetTypeOrCreateClass(impl, decl.getCurrentPackage(),
                            getLongClassName(decl));
                    if (obj instanceof DbJVClass) {

                        /* If sub-class is an interface, then super-class should be an interface */
                        if (((DbJVClass) clazz).getStereotype().equals(interfaceCat))
                            ((DbJVClass) obj).setStereotype(interfaceCat);
                        DbJVInheritance herit = new DbJVInheritance(clazz, (DbJVClass) obj);
                    }
                }

                for (String impl : decl.getImplementsList()) {
                    DbObject obj = tryGetTypeOrCreateClass(impl, decl.getCurrentPackage(),
                            getLongClassName(decl));
                    if (obj instanceof DbJVClass) {
                        ((DbJVClass) obj).setStereotype(interfaceCat);
                        DbJVInheritance herit = new DbJVInheritance(clazz, (DbJVClass) obj);
                    }
                }
                clazz.setDescription(decl.getJavadoc());
            } catch (DbException e) {
                throw new SemanticActionError(e);
            }
        }

        @Override
        public void createField(FieldOfClass fieldOfClass) throws SemanticActionError {
            try {
            	String pack = fieldOfClass.getCurrentPackage();
                DbObject clazz = ModelHelper.findObjectInPackage(model, createdPackages,
                        DbJVClass.metaClass, pack,
                        fieldOfClass.getCurrentClass());
                DbJVDataMember member = new DbJVDataMember(clazz);

                member.setName(fieldOfClass.getFieldName());
                member.setInitialValue(fieldOfClass.getFieldValue());
                member.setVolatile(fieldOfClass.isVolatile());
                member.setTransient(fieldOfClass.isTransient());
                member.setStatic(fieldOfClass.isStatic());
                member.setFinal(fieldOfClass.isFinal());
                member.setVisibility(getVisibility(fieldOfClass));
                member.setDescription(fieldOfClass.getJavadoc());

                DbObject type;
                if (typeIsArray(fieldOfClass.getFieldType())) {
                    int mid = fieldOfClass.getFieldType().indexOf('[');
                    String tmpType = fieldOfClass.getFieldType().substring(0, mid);
                    String arrayString = fieldOfClass.getFieldType().substring(tmpType.length());
                    type = tryGetTypeOrCreateClass(
                            tmpType,
                            fieldOfClass.getCurrentPackage(),
                            getLongClassName(fieldOfClass.getCurrentClass(),
                                    fieldOfClass.getCurrentPackage()));
                    member.setTypeUse(arrayString);
                    member.setTypeUseStyle(OOTypeUseStyle.getInstance(OOTypeUseStyle.AFTER_TYPE));
                } else {
                    type = tryGetTypeOrCreateClass(
                            fieldOfClass.getFieldType(),
                            fieldOfClass.getCurrentPackage(),
                            getLongClassName(fieldOfClass.getCurrentClass(),
                                    fieldOfClass.getCurrentPackage()));
                }
                if (type != null)
                    member.setType((DbOOAdt) type);

            } catch (DbException e) {
                throw new SemanticActionError(e);
            }
        }

        @Override
        public void createMethod(DeclarationOfMethod declarationOfMethod)
                throws SemanticActionError {
            try {
                if (declarationOfMethod.getMethodReturnType() == null)
                    createConstructor(declarationOfMethod);
                else {
                    DbObject clazz = ModelHelper.findObjectInPackage(model, createdPackages,
                            DbJVClass.metaClass, declarationOfMethod.getCurrentPackage(),
                            declarationOfMethod.getCurrentClass());
                    DbJVMethod method;

                    method = new DbJVMethod(clazz);
                    method.setName(declarationOfMethod.getMethodName());
                    method.setStatic(declarationOfMethod.isStatic());
                    method.setFinal(declarationOfMethod.isFinal());
                    method.setVisibility(getVisibility(declarationOfMethod));
                    method.setDescription(declarationOfMethod.getJavadoc());
                    method.setBody(declarationOfMethod.getBody());

                    DbObject type;
                    for (String e : declarationOfMethod.getMethodExceptionsList()) {
                        type = tryGetTypeOrCreateClass(
                                e,
                                null,
                                getLongClassName(declarationOfMethod.getCurrentClass(),
                                        declarationOfMethod.getCurrentPackage()));
                        if (type != null)
                            method.addToJavaExceptions((DbJVClass) type);
                    }

                    for (ParameterOfMethode param : declarationOfMethod.getMethodParametersList()) {
                        DbJVParameter mParam = new DbJVParameter(method);
                        mParam.setName(param.getNameParameter());
                        if (typeIsArray(param.getTypeParameter())) {
                            int mid = param.getTypeParameter().indexOf('[');
                            String tmpType = param.getTypeParameter().substring(0, mid);
                            String arrayString = param.getTypeParameter().substring(
                                    tmpType.length());
                            type = tryGetTypeOrCreateClass(
                                    tmpType,
                                    null,
                                    getLongClassName(declarationOfMethod.getCurrentClass(),
                                            declarationOfMethod.getCurrentPackage()));
                            mParam.setTypeUse(arrayString);
                            mParam.setTypeUseStyle(OOTypeUseStyle
                                    .getInstance(OOTypeUseStyle.AFTER_TYPE));
                        } else {
                            type = tryGetTypeOrCreateClass(
                                    param.getTypeParameter(),
                                    null,
                                    getLongClassName(declarationOfMethod.getCurrentClass(),
                                            declarationOfMethod.getCurrentPackage()));
                        }
                        if (type != null)
                            mParam.setType((DbOOAdt) type);
                    }

                    if (typeIsArray(declarationOfMethod.getMethodReturnType())) {
                        int mid = declarationOfMethod.getMethodReturnType().indexOf('[');
                        String tmpType = declarationOfMethod.getMethodReturnType()
                                .substring(0, mid);
                        String arrayString = declarationOfMethod.getMethodReturnType().substring(
                                tmpType.length());
                        type = tryGetTypeOrCreateClass(
                                tmpType,
                                null,
                                getLongClassName(declarationOfMethod.getCurrentClass(),
                                        declarationOfMethod.getCurrentPackage()));
                        method.setTypeUse(arrayString);
                        method.setTypeUseStyle(OOTypeUseStyle
                                .getInstance(OOTypeUseStyle.AFTER_TYPE));
                    } else {
                        type = tryGetTypeOrCreateClass(
                                declarationOfMethod.getMethodReturnType(),
                                null,
                                getLongClassName(declarationOfMethod.getCurrentClass(),
                                        declarationOfMethod.getCurrentPackage()));
                    }
                    if (type != null)
                        method.setReturnType((DbOOAdt) type);
                }
            } catch (DbException e) {
                throw new SemanticActionError(e);
            }
        }

        private void createConstructor(DeclarationOfMethod declarationOfMethod) throws DbException {
            DbObject clazz = ModelHelper.findObjectInPackage(model, createdPackages,
                    DbJVClass.metaClass, declarationOfMethod.getCurrentPackage(),
                    declarationOfMethod.getCurrentClass());
            DbJVConstructor constructor;

            constructor = new DbJVConstructor(clazz);
            constructor.setName(declarationOfMethod.getMethodName());
            constructor.setVisibility(getVisibility(declarationOfMethod));
            constructor.setBody(declarationOfMethod.getBody());
            constructor.setDescription(declarationOfMethod.getJavadoc());

            DbObject type;
            for (String e : declarationOfMethod.getMethodExceptionsList()) {
                type = tryGetTypeOrCreateClass(
                        e,
                        null,
                        getLongClassName(declarationOfMethod.getCurrentClass(),
                                declarationOfMethod.getCurrentPackage()));
                if (type != null)
                    constructor.addToJavaExceptions((DbJVClass) type);
            }

            for (ParameterOfMethode param : declarationOfMethod.getMethodParametersList()) {
                DbJVParameter mParam = new DbJVParameter(constructor);
                mParam.setName(param.getNameParameter());
                if (typeIsArray(param.getTypeParameter())) {
                    int mid = param.getTypeParameter().indexOf('[');
                    String tmpType = param.getTypeParameter().substring(0, mid);
                    String arrayString = param.getTypeParameter().substring(tmpType.length());
                    type = tryGetTypeOrCreateClass(
                            tmpType,
                            null,
                            getLongClassName(declarationOfMethod.getCurrentClass(),
                                    declarationOfMethod.getCurrentPackage()));
                    mParam.setTypeUse(arrayString);
                    mParam.setTypeUseStyle(OOTypeUseStyle.getInstance(OOTypeUseStyle.AFTER_TYPE));
                } else {
                    type = tryGetTypeOrCreateClass(
                            param.getTypeParameter(),
                            null,
                            getLongClassName(declarationOfMethod.getCurrentClass(),
                                    declarationOfMethod.getCurrentPackage()));
                }
                if (type != null)
                    mParam.setType((DbOOAdt) type);
            }
        }

        private boolean typeIsArray(String typeParameter) {
            if (typeParameter != null) {
                if (typeParameter.contains("["))
                    return true;
            }
            return false;
        }
    }

    /**
     * @param model
     *            The model in which the parsed objects will be created
     * @param ownedPackages
     *            Out parameter. Will contains packages that have been created by the parsing
     * @param createFields
     * @param createMethods
     * @param controller
     * @throws DbException
     */
    public ModelSemanticAction(DbJVClassModel model, List<DbJVPackage> ownedPackages,
            Controller controller, int startPercent, int endPercent, boolean createMethods,
            boolean createFields) throws DbException {
        this.ownedPackages = ownedPackages;
        this.lazyTypes = new HashMap<String, DeclarationOfClassOrInterface>();
        this.fullTypes = new HashMap<String, DeclarationOfClassOrInterface>();
        this.primitiveTypes = new HashMap<String, DbJVPrimitiveType>();
        this.createdTypes = new HashMap<String, DbJVClass>();
        this.createdPackages = new HashMap<String, DbJVPackage>();
        this.importsByClass = new HashMap<String, List<String>>();
        this.currentImports = new ArrayList<String>();
        this.globalImports = new HashSet<String>();
        this.model = model;
        this.modelObjects = new ArrayList<SemanticDeclaration>();
        this.realAction = new RealModelSemanticAction();
        this.createFields = createFields;
        this.createMethods = createMethods;
        this.startPercent = startPercent;
        this.endPercent = endPercent;
        this.controller = controller;

        addJavaLangTypes();

        for (int i = 0; i < PRIMITIVES.length; i++) {
            primitiveTypes.put(PRIMITIVES[i], null);
        }
        getJavaTypes();
        this.controller.println();
    }

    @Override
    public void createPackage(String packageName) throws SemanticActionError {
        try {
            if (!createdPackages.containsKey(packageName)) {
                ModelHelper.createPackage(packageName, model, createdPackages);
                controller.println(MessageFormat.format(pattern, packageName));
            }
        } catch (DbException e) {
            throw new SemanticActionError(e);
        }
    }

    /**
     * This method should only be called from the grammar. It contains specific code for parsed
     * packages
     */
    @Override
    public void createClass(DeclarationOfClassOrInterface declarationOfClassOrInterface)
            throws SemanticActionError {
        try {
            DbObject pack = ModelHelper.findPackage(
                    declarationOfClassOrInterface.getCurrentPackage(), model, createdPackages);
            if (pack == null) {
                createPackage(declarationOfClassOrInterface.getCurrentPackage());
                pack = ModelHelper.findPackage(declarationOfClassOrInterface.getCurrentPackage(),
                        model, createdPackages);
            }

            if (!ownedPackages.contains(pack))
                ownedPackages.add((DbJVPackage) pack);

            modelObjects.add(declarationOfClassOrInterface);
            DbJVClass clazz = doCreateClass(declarationOfClassOrInterface, pack);
            lazyTypes.put(clazz.getName(), declarationOfClassOrInterface);

            /* Check if class is a nested class, then get imports of parent class */
            if (currentImports.isEmpty()
                    && !declarationOfClassOrInterface.getClassOrInterfaceName().equals(
                            declarationOfClassOrInterface.getCurrentClass())) {
                currentImports = importsByClass
                        .get(getLongParentClassName(declarationOfClassOrInterface));
            }
            importsByClass.put(getLongClassName(declarationOfClassOrInterface), currentImports);

            currentImports = new ArrayList<String>();

            controller.println(MessageFormat.format(pattern,
                    declarationOfClassOrInterface.getClassOrInterfaceName()));
        } catch (DbException e) {
            throw new SemanticActionError(e);
        }
    }

    @Override
    public void createField(FieldOfClass fieldOfClass) {
        /* Fields will be treated once all classes are created */
        if (createFields) {
            modelObjects.add(fieldOfClass);
            controller.println(MessageFormat.format(pattern, fieldOfClass.getFieldName()));
        }
    }

    @Override
    public void createMethod(DeclarationOfMethod declarationOfMethod) {
        if (createMethods)
            /* Methods will be treated once all classes are created */
            modelObjects.add(declarationOfMethod);
    }

    /**
     * This method is used to search for an object in current model DB. Uses every ways possibles to
     * find the matching object. If it can't find it, then a new DbObject is created in the right
     * package
     * 
     * @param type
     *            The type to search
     * @param container
     *            The container package of the referencing class referencing
     * @param referenceClass
     *            The referencing class
     * @return The DbObject matching the search, or the newly created one
     * @throws DbException
     */
    private DbObject tryGetTypeOrCreateClass(String type, String container, String referenceClass)
            throws DbException {
        if (type == null)
            return primitiveTypes.get("void");
        DbObject obj = null;
        if (createdTypes.containsKey(getLongClassName(type, container))) {
            obj = createdTypes.get(getLongClassName(type, container));
        } else if (createdTypes.containsKey(type)) {
            obj = createdTypes.get(type);
        } else if (createdTypes.containsKey(getLongClassName(type, "java.lang"))) {
            obj = createdTypes.get(getLongClassName(type, "java.lang"));
        } else if (fullTypes.containsKey(getLongClassName(type, "java.lang"))) {
            obj = doCreateClass(fullTypes.get(getLongClassName(type, "java.lang")), null);
        } else {
            String importedName = findImport(referenceClass, type);
            if (importedName == null) {
                if (type.contains(".")) {
                    obj = tryGetOrCreateClassWithDot(type);
                } else {
                    DeclarationOfClassOrInterface decl = lazyTypes.get(type);
                    if (decl == null) {
                        obj = tryGetOrCreatePrimitiveOrUnknown(type, obj);
                    } else {
                        obj = createdTypes.get(getLongClassName(decl));
                    }
                }
            } else if (!createdTypes.containsKey(importedName)) {
                DeclarationOfClassOrInterface decl = getDeclarationOfClassOrInterfaceFromImport(importedName);
                obj = this.doCreateClass(decl, null);
            } else {
                obj = createdTypes.get(importedName);
            }
        }

        return obj;
    }

    private DbObject tryGetOrCreatePrimitiveOrUnknown(String type, DbObject obj) throws DbException {
        if (primitiveTypes.containsKey(type)) {
            obj = primitiveTypes.get(type);
        } else {
            obj = tryGetOrCreateClassInUnknownPackage(type, obj);
        }
        return obj;
    }

    private DbObject tryGetOrCreateClassWithDot(String type) throws DbException {
        DeclarationOfClassOrInterface decl = fullTypes.get(type);
        DbObject obj;
        if (decl == null) {
            String container;
            String importedName;
            String split[] = splitPackageAndClass(type);
            type = split[1];
            container = split[0];
            /* This will check if parent class can be found in an import */
            importedName = findImport(null, container);
            if (importedName != null) {
                DeclarationOfClassOrInterface parent = fullTypes.get(importedName);
                obj = tryGetOrCreateInnerClass(parent, type);
            } else {
                obj = tryGetOrCreateClassLazy(type, container);
            }
        } else {
            obj = doCreateClass(decl, null);
        }

        return obj;
    }

    private DeclarationOfClassOrInterface getDeclarationOfClassOrInterfaceFromImport(
            String importedName) {
        DeclarationOfClassOrInterface decl;
        if (!fullTypes.containsKey(importedName)) {
            decl = new DeclarationOfClassOrInterface();
            String[] split = splitPackageAndClass(importedName);
            decl.setClass(!split[1].endsWith("Exception"));
            decl.setCurrentPackage(split[0]);
            decl.setClassOrInterfaceName(split[1]);
        } else {
            decl = fullTypes.get(importedName);
        }

        return decl;
    }

    private DbObject tryGetOrCreateClassInUnknownPackage(String type, DbObject obj)
            throws DbException {
        DeclarationOfClassOrInterface decl;
        if (unknownPackage != null)
            obj = unknownPackage.findComponentByName(DbJVClass.metaClass, type);
        if (obj == null) {
            decl = createUnknownDeclarationOfClass(type);
            obj = this.doCreateClass(decl, null);
        }
        return obj;
    }

    private DbObject tryGetOrCreateClassLazy(String type, String container) throws DbException {
        DbObject obj;
        DeclarationOfClassOrInterface decl;
        decl = lazyTypes.get(type);
        if (decl != null) {
            obj = createdTypes.get(getLongClassName(decl));
        } else {
            decl = new DeclarationOfClassOrInterface();
            decl.setClass(!type.endsWith("Exception"));
            decl.setClassOrInterfaceName(type);
            decl.setCurrentPackage(container);
            obj = doCreateClass(decl, null);
        }
        return obj;
    }

    private DbObject tryGetOrCreateInnerClass(DeclarationOfClassOrInterface parent, String type)
            throws DbException {
        DbObject obj = createdTypes.get(getLongClassName(type, getLongClassName(parent)));
        if (obj == null) {
            DeclarationOfClassOrInterface newDecl = new DeclarationOfClassOrInterface();
            newDecl.setClassOrInterfaceName(type);
            newDecl.setCurrentClass(parent.getClassOrInterfaceName());
            newDecl.setCurrentPackage(parent.getCurrentPackage());
            newDecl.setClass(!type.contains("Exception"));
            obj = doCreateClass(newDecl, null);
        }
        return obj;
    }

    private DeclarationOfClassOrInterface createUnknownDeclarationOfClass(String type)
            throws DbException {
        DeclarationOfClassOrInterface decl = new DeclarationOfClassOrInterface();
        decl.setClass(!type.endsWith("Exception"));
        decl.setClassOrInterfaceName(type);
        if (unknownPackage == null) {
            unknownPackage = new DbJVPackage(model);
            unknownPackage.setName(JavaSourceReverseLocaleMgr
                    .getResourceEquivalent("UnknownPackage"));
        }
        decl.setCurrentPackage(unknownPackage.getName());
        lazyTypes.put(type, decl);

        return decl;
    }

    /**
     * Search for the type in imports used by referenceClass. If it can't find any match, search
     * through the global imports list
     * 
     * @param referenceClass
     *            The class to use
     * @param type
     *            The type to search
     * @return If found, the import string, else null
     */
    private String findImport(String referenceClass, String type) {
        if (importsByClass.containsKey(referenceClass)) {
            String dotType = "." + type;
            for (String imported : importsByClass.get(referenceClass)) {
                if (imported.endsWith(dotType))
                    return imported;
            }
        }

        String dotType = "." + type;
        for (String imported : globalImports) {
            if (imported.endsWith(dotType))
                return imported;
        }

        return null;
    }

    /**
     * This method is used to create any class object in the model. This is the method to call to
     * create class that the grammar did not parse
     * 
     * @param declarationOfClassOrInterface
     * @param pack
     * @return The created DbJVClass
     * @throws DbException
     */
    private DbJVClass doCreateClass(DeclarationOfClassOrInterface declarationOfClassOrInterface,
            DbObject pack) throws DbException {
        if (pack == null) {
            pack = ModelHelper.findPackage(declarationOfClassOrInterface.getCurrentPackage(),
                    model, createdPackages);
            if (pack == null) {
                createPackage(declarationOfClassOrInterface.getCurrentPackage());
                pack = ModelHelper.findPackage(declarationOfClassOrInterface.getCurrentPackage(),
                        model, createdPackages);
                /* If package is still null, then class should be an inner class */
                if (pack == null) {
                	String cp = declarationOfClassOrInterface.getCurrentPackage();
                	if (cp != null) {
                		String[] split = splitPackageAndClass(cp);
                    	declarationOfClassOrInterface.setCurrentClass(split[1]);
                    	declarationOfClassOrInterface.setCurrentPackage(split[0]);
                	}
                }
            }
        }

        int cat;
        if (!declarationOfClassOrInterface.getClassOrInterfaceName().endsWith("Exception")) {
            if (declarationOfClassOrInterface.isInterface())
                cat = JVClassCategory.INTERFACE;
            else if (declarationOfClassOrInterface.isClass()
                    || declarationOfClassOrInterface.isEnum())
                cat = JVClassCategory.CLASS;
            else
                cat = JVClassCategory.EXCEPTION;
        } else {
            cat = JVClassCategory.EXCEPTION;
        }

        DbJVClass clazz;
        String longName;
        if (declarationOfClassOrInterface.getCurrentClass() == null
                || declarationOfClassOrInterface.getClassOrInterfaceName().equals(
                        declarationOfClassOrInterface.getCurrentClass())) {
        	
        	if (pack == null) {
        		clazz = new DbJVClass(model, JVClassCategory.getInstance(cat));
        	} else {
        		clazz = new DbJVClass(pack, JVClassCategory.getInstance(cat));
        	}
            
            longName = getLongClassName(declarationOfClassOrInterface);
        } else {
            clazz = new DbJVClass(tryGetTypeOrCreateClass(
                    declarationOfClassOrInterface.getCurrentClass(),
                    declarationOfClassOrInterface.getCurrentPackage(),
                    getLongParentClassName(declarationOfClassOrInterface)),
                    JVClassCategory.getInstance(cat));
            longName = getLongClassName(declarationOfClassOrInterface.getClassOrInterfaceName(),
                    getLongParentClassName(declarationOfClassOrInterface));
        }
        createdTypes.put(longName, clazz);
        fullTypes.put(longName, declarationOfClassOrInterface);
        clazz.setName(declarationOfClassOrInterface.getClassOrInterfaceName());
        clazz.setAbstract(declarationOfClassOrInterface.isAbstract());
        clazz.setStatic(declarationOfClassOrInterface.isStatic());
        clazz.setStrictfp(declarationOfClassOrInterface.isStrictfp());
        clazz.setFinal(declarationOfClassOrInterface.isFinal());
        clazz.setVisibility(getVisibility(declarationOfClassOrInterface));
        if (declarationOfClassOrInterface.isEnum())
            clazz.setUmlStereotype(Extensibility.findByName(
                    ((DbSMSProject) model.getProject()).getUmlExtensibility(),
                    Extensibility.ENUMERATION));
        controller.println(MessageFormat.format(pattern, longName));
        return clazz;
    }

    private String getLongClassName(DeclarationOfClassOrInterface declarationOfClassOrInterface) {
        return declarationOfClassOrInterface.getCurrentPackage() + "."
                + declarationOfClassOrInterface.getClassOrInterfaceName();
    }

    private String getLongParentClassName(
            DeclarationOfClassOrInterface declarationOfClassOrInterface) {
        return declarationOfClassOrInterface.getCurrentPackage() + "."
                + declarationOfClassOrInterface.getCurrentClass();
    }

    private String getLongClassName(String type, String packageName) {
        return packageName + "." + type;
    }

    /**
     * Splits the full name into String array
     * 
     * @param longType
     * @return Array of String containing package at index 0, class at index 1
     */
    private String[] splitPackageAndClass(String longType) {
        int classDot = longType.lastIndexOf(".");
        String split[] = { longType.substring(0, classDot), longType.substring(classDot + 1) };
        return split;
    }

    private JVVisibility getVisibility(DeclarationOfClassOrInterface object) {
        if (object.isPrivate())
            return JVVisibility.getInstance(JVVisibility.PRIVATE);
        else if (object.isProtected())
            return JVVisibility.getInstance(JVVisibility.PROTECTED);
        else if (object.isPublic())
            return JVVisibility.getInstance(JVVisibility.PUBLIC);
        else
            return JVVisibility.getInstance(JVVisibility.DEFAULT);
    }

    private JVVisibility getVisibility(DeclarationOfMethod method) {
        if (method.isPrivate())
            return JVVisibility.getInstance(JVVisibility.PRIVATE);
        else if (method.isProtected())
            return JVVisibility.getInstance(JVVisibility.PROTECTED);
        else if (method.isPublic())
            return JVVisibility.getInstance(JVVisibility.PUBLIC);
        else
            return JVVisibility.getInstance(JVVisibility.DEFAULT);
    }

    private JVVisibility getVisibility(FieldOfClass field) {
        if (field.isPrivate())
            return JVVisibility.getInstance(JVVisibility.PRIVATE);
        else if (field.isProtected())
            return JVVisibility.getInstance(JVVisibility.PROTECTED);
        else if (field.isPublic())
            return JVVisibility.getInstance(JVVisibility.PUBLIC);
        else
            return JVVisibility.getInstance(JVVisibility.DEFAULT);
    }

    @Override
    public void finalizeCreation() {
        int count = modelObjects.size();
        int span = endPercent - startPercent;
        int i = 0;
        Collections.sort(modelObjects, new PutClassesFirstComparator());
        for (SemanticDeclaration decl : modelObjects) {
            decl.doAction(realAction);
            controller.checkPoint(i * span / count + startPercent);
            i++;
            if (controller.getState() == Controller.STATE_ABORTING)
                throw new SemanticActionError(null);
        }
    }

    @Override
    public void createImport(String importName) {
        if (!importName.endsWith(".*")) {
            currentImports.add(importName);
            globalImports.add(importName);
            int pos = importName.lastIndexOf('.') + 1;
            String packageName = importName.substring(0, pos - 1);
            String className = importName.substring(pos, importName.length());
            if (!fullTypes.containsKey(importName)) {
                DeclarationOfClassOrInterface decl = new DeclarationOfClassOrInterface();
                decl.setCurrentPackage(packageName);
                decl.setClassOrInterfaceName(className);
                if (!className.endsWith("Exception")) {
                    decl.setClass(true);
                }
                fullTypes.put(importName, decl);
            }
        }

        controller.println(MessageFormat.format(pattern, importName));
    }

    private void getJavaTypes() throws DbException {
        DbRelationN relN = model.getProject().getComponents();
        DbEnumeration enu = relN.elements(DbSMSBuiltInTypeNode.metaClass);
        while (enu.hasMoreElements()) {
            DbSMSBuiltInTypeNode typeNode = (DbSMSBuiltInTypeNode) enu.nextElement();
            DbRelationN relN2 = typeNode.getComponents();
            DbEnumeration enu2 = relN2.elements(DbSMSBuiltInTypePackage.metaClass);
            while (enu2.hasMoreElements()) {
                DbSMSBuiltInTypePackage pack = (DbSMSBuiltInTypePackage) enu2.nextElement();
                DbSMSTargetSystem ts = pack.getTargetSystem();
                if ("Java".equals(ts.getName())) {
                    DbRelationN relN3 = pack.getComponents();
                    DbEnumeration enu3 = relN3.elements(DbJVPrimitiveType.metaClass);
                    while (enu3.hasMoreElements()) {
                        DbJVPrimitiveType type = (DbJVPrimitiveType) enu3.nextElement();
                        String name = type.getName();
                        if (primitiveTypes.containsKey(name)) {
                            primitiveTypes.put(name, type);
                        }
                    } //end while
                    enu3.close();
                } //end if
                pack.getAlias();
            } //end while
            enu2.close();
        } //end while
        enu.close();
    }

    /**
     * This method add all java.lang classes to fullTypes map The content of this class should be
     * generated from JavaLangExtractor.java
     */
    private void addJavaLangTypes() {
        DeclarationOfClassOrInterface decl;
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Appendable");
        decl.setInterface(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Appendable", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("CharSequence");
        decl.setInterface(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.CharSequence", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Cloneable");
        decl.setInterface(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Cloneable", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Object");
        decl.setInterface(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Object", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Iterable");
        decl.setInterface(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Iterable", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Readable");
        decl.setInterface(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Readable", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Runnable");
        decl.setInterface(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Runnable", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Boolean");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Boolean", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Byte");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Byte", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Character");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Character", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Class");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Class", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("ClassLoader");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.ClassLoader", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Compiler");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Compiler", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Double");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Double", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Enum");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Enum", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Float");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Float", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("InheritableThreadLocal");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.InheritableThreadLocal", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Integer");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Integer", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Long");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Long", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Math");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Math", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Number");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Number", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Object");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Object", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Package");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Package", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Process");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Process", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("ProcessBuilder");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.ProcessBuilder", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Runtime");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Runtime", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("RuntimePermission");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.RuntimePermission", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("SecurityManager");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.SecurityManager", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Short");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Short", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("StackTraceElement");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.StackTraceElement", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Throwable");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Throwable", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("String");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.String", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("StringBuffer");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.StringBuffer", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("StringBuilder");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.StringBuilder", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("System");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.System", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Thread");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Thread", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("ThreadGroup");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.ThreadGroup", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("ThreadLocal");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.ThreadLocal", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Throwable");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Throwable", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Void");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Void", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("ArithmeticException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.ArithmeticException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("ArrayIndexOutOfBoundsException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.ArrayIndexOutOfBoundsException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("ArrayStoreException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.ArrayStoreException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("ClassCastException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.ClassCastException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("ClassNotFoundException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.ClassNotFoundException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("CloneNotSupportedException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.CloneNotSupportedException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("EnumConstantNotPresentException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.EnumConstantNotPresentException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Exception");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Exception", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("IllegalAccessException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.IllegalAccessException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("IllegalArgumentException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.IllegalArgumentException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("IllegalMonitorStateException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.IllegalMonitorStateException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("IllegalStateException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.IllegalStateException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("IllegalThreadStateException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.IllegalThreadStateException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("IndexOutOfBoundsException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.IndexOutOfBoundsException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("InstantiationException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.InstantiationException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("InterruptedException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.InterruptedException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("NegativeArraySizeException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.NegativeArraySizeException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("NoSuchFieldException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.NoSuchFieldException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("NoSuchMethodException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.NoSuchMethodException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("NullPointerException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.NullPointerException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("NumberFormatException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.NumberFormatException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("RuntimeException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.RuntimeException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("SecurityException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.SecurityException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("StringIndexOutOfBoundsException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.StringIndexOutOfBoundsException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("TypeNotPresentException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.TypeNotPresentException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("UnsupportedOperationException");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.UnsupportedOperationException", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("AbstractMethodError");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.AbstractMethodError", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("AssertionError");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.AssertionError", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("ClassCircularityError");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.ClassCircularityError", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("ClassFormatError");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.ClassFormatError", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Error");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Error", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("ExceptionInInitializerError");
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.ExceptionInInitializerError", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("IllegalAccessError");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.IllegalAccessError", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("IncompatibleClassChangeError");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.IncompatibleClassChangeError", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("InstantiationError");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.InstantiationError", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("InternalError");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.InternalError", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("LinkageError");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.LinkageError", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("NoClassDefFoundError");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.NoClassDefFoundError", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("NoSuchFieldError");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.NoSuchFieldError", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("NoSuchMethodError");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.NoSuchMethodError", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("OutOfMemoryError");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.OutOfMemoryError", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("StackOverflowError");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.StackOverflowError", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("ThreadDeath");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.ThreadDeath", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("UnknownError");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.UnknownError", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("UnsatisfiedLinkError");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.UnsatisfiedLinkError", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("UnsupportedClassVersionError");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.UnsupportedClassVersionError", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("VerifyError");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.VerifyError", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("VirtualMachineError");
        decl.setClass(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.VirtualMachineError", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Deprecated");
        decl.setInterface(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Deprecated", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("Override");
        decl.setInterface(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.Override", decl);
        decl = new DeclarationOfClassOrInterface();
        decl.setClassOrInterfaceName("SuppressWarnings");
        decl.setInterface(true);
        decl.setCurrentPackage("java.lang");
        decl.setModifiers(Modifier.PUBLIC);
        fullTypes.put("java.lang.SuppressWarnings", decl);
    }

    @Override
    public void createInitializationBlock(InitializationBlockOfClass initBlock) {
        try {
            DbJVClass clazz = createdTypes.get(getLongClassName(initBlock.getCurrentClass(),
                    initBlock.getCurrentPackage()));
            if (clazz == null)
                clazz = (DbJVClass) ModelHelper.findObjectInPackage(model, createdPackages,
                        DbJVClass.metaClass, initBlock.getCurrentPackage(),
                        initBlock.getCurrentClass());

            DbJVInitBlock block = new DbJVInitBlock(clazz);
            block.setBody(initBlock.getBody());
            block.setDescription(initBlock.getJavadoc());
            block.setName(initBlock.getInitializationBlockName());
            block.setStatic(initBlock.isStatic());
        } catch (DbException e) {
            throw new SemanticActionError(e);
        }
    }

}
