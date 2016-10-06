/*************************************************************************

Copyright (C) 2009 by neosapiens inc.

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

You can reach neosapiens inc. at: 

neosapiens inc.
1236 Gaudias-Petitclerc
Qu&eacute;bec, Qc, G1Y 3G2
CANADA
Telephone: 418-561-8403
Fax: 418-650-2375
http://www.neosapiens.com/
Email: marco.savard@neosapiens.com
       gino.pelletier@neosapiens.com

 **********************************************************************/
package com.neosapiens.plugins.reverse.java;

import java.awt.Point;
import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.gui.task.Worker;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.sms.actions.CreateMissingGraphicsAction;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.oo.db.*;
import org.modelsphere.sms.oo.java.db.*;
import org.modelsphere.sms.oo.java.db.srtypes.JVClassCategory;
import org.modelsphere.sms.oo.java.db.srtypes.JVVisibility;
import org.modelsphere.sms.oo.java.graphic.JVDiagramLayout;

import com.neosapiens.plugins.reverse.java.international.LocaleMgr;
import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.classfile.*;
import com.sun.org.apache.bcel.internal.generic.*;

class ReverseJavaBytecodeWorker extends Worker {
    private DbSMSProject m_project;
    private DbJVClassModel m_classModel;
    private List<DbJVPackage> m_importedPackages = new ArrayList<DbJVPackage>();
    private Map<String, DbJVPackage> packages = new HashMap<String, DbJVPackage>();
    private ReverseJavaBytecodeParameters m_params;

    public ReverseJavaBytecodeWorker(ReverseJavaBytecodeParameters params) {
        m_params = params;
    }

    @Override
    protected String getJobTitle() {
        return LocaleMgr.misc.getString("ImportJavaBytecode");
    }

    @Override
    protected void runJob() throws Exception {

        Controller controller = getController();

        //is invoked head less?
        DefaultMainFrame mainFrame = ApplicationContext.getDefaultMainFrame();
        boolean headless = (mainFrame == null);
        Db db = null;

        try {
            if (!headless) {
                //create new project, if it was not specific by the user
                m_project = m_params.getOutputProject();
                if (m_project == null) {
                    m_project = (DbSMSProject) mainFrame.createDefaultProject(db);
                }

                db = m_project.getDb();
                db.beginWriteTrans(LocaleMgr.misc.getString("ImportJavaBytecode"));

                //create class model
                m_classModel = new DbJVClassModel(m_project);
            }

            //import Java classes files (jobDone 0% to 80%)
            DbJVPackage topMostPackage = importFiles(controller, 0, 80);

            //show success/failure message
            if (controller.getErrorsCount() == 0) {
                controller.println(LocaleMgr.misc.getString("Success"));
            } else {
                controller.println(LocaleMgr.misc.getString("Failed"));
            } //end if

            //create and reveal diagram ((jobDone 80% to 100%)
            if (!headless) {
                if ((topMostPackage != null) && (m_params.createDiagrams)) {
                    createAndRevealDiagram(mainFrame, topMostPackage, controller, 80, 100);
                }

                db.commitTrans();
            } //end if      

            controller.checkPoint(100);

        } catch (DbException ex) {
            controller.println(ex.toString());
            controller.cancel();
        } //end try

    } //end runJob()

    //
    // private methods
    //

    private DbJVPackage importFiles(Controller controller, int startJobDone, int endJobDone)
            throws IOException, DbException {
        controller.checkPoint(startJobDone);
        DbJVPackage topMostPackage = null;

        List<File> filesToImport = m_params.getFilesToImport();
        List<ErrorReport> errorLog = new ArrayList<ErrorReport>();

        int i = 0, nb = filesToImport.size();

        String pattern = LocaleMgr.misc.getString("ThereAreNbFilesToImport");
        String msg = MessageFormat.format(pattern, nb);
        controller.println(msg);
        int span = endJobDone - startJobDone;

        for (File file : filesToImport) {
            String filename = file.toString();
            int idx = filename.lastIndexOf('.');
            String ext = (idx == -1) ? null : filename.substring(idx + 1);
            int jobDone = startJobDone + (i * span) / nb;

            try {
                if ("class".equals(ext)) {
                    DbJVClass claz = importClassFile(filename, controller);
                    if (claz != null) {
                        DbJVPackage pack = (DbJVPackage) claz
                                .getCompositeOfType(DbJVPackage.metaClass);
                        topMostPackage = findTopMostPackage(topMostPackage, pack);
                        addToImportedPackage(pack);
                    } //end if
                } else if ("jar".equals(ext)) {
                    int nextJobDone = startJobDone + ((i + 1) * span) / nb;
                    topMostPackage = importJarFile(file, topMostPackage, controller, jobDone,
                            nextJobDone);
                } //end if
            } catch (Throwable th) {
                ErrorReport report = new ErrorReport(file, th);
                errorLog.add(report);
                controller.incrementErrorsCounter();
            } //end try 

            //check job done
            i++;
            controller.checkPoint(jobDone);

            //stop to reverse engineer if user has cancelled
            boolean finished = controller.isFinalState();
            if (finished) {
                break;
            }
        } //end for

        if (!errorLog.isEmpty()) {
            reportErrors(errorLog, controller);
        } //end if 

        controller.checkPoint(endJobDone);
        return topMostPackage;
    } //end importFiles()

    private void reportErrors(List<ErrorReport> errorLog, Controller controller) {
        String pattern = LocaleMgr.misc.getString("FailedOn0");
        for (ErrorReport report : errorLog) {
            String filename = report.m_file.toString();
            String msg = MessageFormat.format(pattern, filename);
            controller.println(msg);

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            report.m_th.printStackTrace(pw);
            String s = sw.toString();
            controller.println(s);
        } //end for

        pattern = LocaleMgr.misc.getString("FailedOn0Files");
        int nb = errorLog.size();
        String msg = MessageFormat.format(pattern, nb);
        controller.println(msg);

        for (ErrorReport report : errorLog) {
            String filename = report.m_file.toString();
            msg = MessageFormat.format(pattern, filename);
            controller.println(msg);
        } //end for
    } //end reportErrors()

    private DbJVPackage findTopMostPackage(DbJVPackage pack1, DbJVPackage pack2) throws DbException {
        DbJVPackage topMostPackage;

        if (pack1 == null) {
            topMostPackage = pack2;
        } else if (pack2 == null) {
            topMostPackage = pack1;
        } else {
            String s1 = pack1.buildFullNameString();
            String s2 = pack2.buildFullNameString();
            int p1 = count(s1, '.');
            int p2 = count(s2, '.');
            topMostPackage = (p1 <= p2) ? pack1 : pack2;
        } //end if

        return topMostPackage;
    }

    private int count(String buildFullNameString, char c) {
        int counter = 0;

        for (int i = 0; i < buildFullNameString.length(); i++) {
            if (buildFullNameString.charAt(i) == c) {
                counter++;
            }
        }
        return counter;
    }

    private DbJVClass importClassFile(String filename, Controller controller) throws DbException {
        DbJVClass dbClass;

        try {
            ClassParser parser = new ClassParser(filename);
            JavaClass claz = parser.parse();
            dbClass = importClass(claz, controller);

        } catch (IOException ex) {
            controller.println(ex.toString());
            dbClass = null;
        }

        return dbClass;
    } //end importClassFile()

    private DbJVPackage importJarFile(File jarFile, DbJVPackage topMostPackage,
            Controller controller, int startJobDone, int endJobDone) throws DbException {
        DbJVClass dbClass;
        String filename = jarFile.getName();

        try {
            ZipFile zip = new ZipFile(jarFile);
            int i = 0, nb = zip.size();
            int span = endJobDone - startJobDone;

            for (Enumeration<?> e = zip.entries(); e.hasMoreElements();) {
                int jobDone = startJobDone + (i * span) / nb;

                ZipEntry entry = (ZipEntry) e.nextElement();
                String entryName = entry.getName();
                int idx = entryName.lastIndexOf('.');
                String ext = (idx == -1) ? null : entryName.substring(idx + 1);
                if ("class".equals(ext)) {
                    InputStream is = zip.getInputStream(entry);

                    ClassParser parser = new ClassParser(is, filename);
                    JavaClass claz = parser.parse();
                    dbClass = importClass(claz, controller);

                    if (dbClass != null) {
                        DbJVPackage pack = (DbJVPackage) dbClass
                                .getCompositeOfType(DbJVPackage.metaClass);
                        topMostPackage = findTopMostPackage(topMostPackage, pack);
                        addToImportedPackage(pack);
                    } //end if
                } //end if

                //check job done
                controller.checkPoint(jobDone);
                i++;

                //stop to reverse engineer if user has cancelled
                boolean finished = controller.isFinalState();
                if (finished) {
                    break;
                }
            } //end for

            zip.close();

        } catch (IOException ex) {
            controller.println(ex.toString());
            dbClass = null;
        }

        return topMostPackage;
    } //end importClassFile()

    private void addToImportedPackage(DbJVPackage pack) {
        if ((pack != null) && (!m_importedPackages.contains(pack))) {
            m_importedPackages.add(pack);
        }
    } //end addToImportedPackage()

    private DbJVClass importClass(JavaClass claz, Controller controller) throws DbException {

        String packName = claz.getPackageName();
        String qualifiedName = claz.getClassName();
        int idx = qualifiedName.lastIndexOf('.');
        String classname = qualifiedName.substring(idx + 1);
        DbJVClass dbClaz = null;

        try {
            if (m_classModel != null) {
                DbJVPackage pack = findPackageByName(packName);

                //create class or interface
                int value = claz.isInterface() ? JVClassCategory.INTERFACE : JVClassCategory.CLASS;
                JVClassCategory catg = JVClassCategory.getInstance(value);
                dbClaz = (pack == null) ? new DbJVClass(this.m_classModel, catg) : new DbJVClass(
                        pack, catg);
                dbClaz.setName(classname);

                //set class modifiers 
                dbClaz.setAbstract(claz.isAbstract());
                dbClaz.setFinal(claz.isFinal());
                dbClaz.setStatic(claz.isStatic());
                dbClaz.setStrictfp(claz.isStrictfp());
                dbClaz.setVisibility(toVisibility(claz));

                //create inheritances
                importInheritances(dbClaz, claz);

                //create fields
                if (m_params.createFields) {
                    Field[] fields = claz.getFields();
                    for (Field field : fields) {
                        importField(dbClaz, field);
                    }
                }

                //create methods
                if (m_params.createMethods) {
                    Method[] methods = claz.getMethods();
                    for (Method method : methods) {
                        importMethod(dbClaz, method);
                    }
                }

                //keep user informed of progression
                String pattern = LocaleMgr.misc.getString("0SuccessfullyCreated");
                String msg = MessageFormat.format(pattern, qualifiedName);
                controller.println(msg);
            } //end if  
        } catch (DbException ex) {
            controller.println(ex.toString());
        } //end try

        return dbClaz;
    } //end importClass()

    private void importInheritances(DbJVClass dbClaz, JavaClass claz) throws DbException {
        String qualifiedName = claz.getSuperclassName();
        if (!"java.lang.Object".equals(qualifiedName)) {
            importInheritance(dbClaz, qualifiedName, false);
        }

        String[] qualifiedNames = claz.getInterfaceNames();
        for (String qn : qualifiedNames) {
            importInheritance(dbClaz, qn, true);
        } //end for

    }

    private void importInheritance(DbJVClass dbClaz, String qualifiedName, boolean assumeInterface)
            throws DbException {
        DbOOAdt adt = findClassByName(qualifiedName);

        if (adt instanceof DbJVClass) {
            DbJVClass superType = (DbJVClass) adt;
            JVClassCategory categ = assumeInterface ? JVClassCategory
                    .getInstance(JVClassCategory.INTERFACE) : JVClassCategory
                    .getInstance(JVClassCategory.CLASS);
            superType.setStereotype(categ);
            new DbJVInheritance(dbClaz, superType);
        } //end if
    } //end findClassByName()

    private DbOOAdt findClassByName(String qualifiedName) throws DbException {
        int idx = qualifiedName.lastIndexOf('.');
        String packName = (idx == -1) ? null : qualifiedName.substring(0, idx);
        String clazName = (idx == -1) ? qualifiedName : qualifiedName.substring(idx + 1);

        DbJVPackage pack = findPackageByName(packName);
        DbOOAdt adt = findClassByName(pack, clazName);
        return adt;
    }

    private void importField(DbJVClass dbClaz, Field field) throws DbException {
        if (dbClaz == null) {
            return;
        }

        DbJVDataMember member = new DbJVDataMember(dbClaz);
        member.setName(field.getName());

        //set field type
        Type type = field.getType();
        member.setType(toAdt(type));
        member.setTypeUse(toTypeUse(type));

        //set field modifiers
        member.setFinal(field.isFinal());
        member.setStatic(field.isStatic());
        member.setTransient(field.isTransient());
        member.setVisibility(toVisibility(field));
        member.setVolatile(field.isVolatile());
    }

    private void importMethod(DbJVClass dbClaz, Method method) throws DbException {
        if (dbClaz == null) {
            return;
        }

        String methodName = method.getName();
        boolean isConstructor = "<init>".equals(methodName);
        boolean isInitBlock = "<clinit>".equals(methodName);
        DbOOAbstractMethod oper = null;

        if (isInitBlock) {
            new DbJVInitBlock(dbClaz);
        } else if (isConstructor) {
            DbJVConstructor constr = new DbJVConstructor(dbClaz);
            importExceptions(constr, method);
            oper = constr;
        } else {
            //create method and return type
            DbJVMethod meth = new DbJVMethod(dbClaz);
            Type type = method.getReturnType();
            meth.setReturnType(toAdt(type));
            meth.setTypeUse(toTypeUse(type));

            //set method modifiers
            meth.setAbstract(method.isAbstract());
            meth.setFinal(method.isFinal());
            meth.setNative(method.isNative());
            meth.setStatic(method.isStatic());
            meth.setStrictfp(method.isStrictfp());
            meth.setSynchronized(method.isSynchronized());
            //method.isTransient()
            //method.isVolatile()
            importExceptions(meth, method);

            oper = meth;
        }

        //set name and visibility
        if (oper != null) {
            oper.setName(methodName);
            oper.setVisibility(toVisibility(method));

            //create parameters
            Type[] args = method.getArgumentTypes();
            for (Type arg : args) {
                DbJVParameter param = new DbJVParameter(oper);
                param.setType(toAdt(arg));
                param.setTypeUse(toTypeUse(arg));
            } //end for
        } //end if
    } //end importMethod()

    private void importExceptions(DbOOAbstractMethod oper, Method method) throws DbException {

        ExceptionTable table = method.getExceptionTable();
        if (table != null) {
            String[] exceptionNames = table.getExceptionNames();

            for (String exceptionName : exceptionNames) {
                DbOOAdt adt = findClassByName(exceptionName);
                if (adt instanceof DbJVClass) {
                    DbJVClass exception = (DbJVClass) adt;
                    if (oper instanceof DbJVConstructor) {
                        DbJVConstructor constr = (DbJVConstructor) oper;
                        constr.addToJavaExceptions(exception);
                    } else if (oper instanceof DbJVMethod) {
                        DbJVMethod meth = (DbJVMethod) oper;
                        meth.addToJavaExceptions(exception);
                    } //end if
                } //end if 
            } //end for
        } //end if
    } //end importExceptions()

    private DbOOAdt toAdt(Type type) throws DbException {
        DbOOAdt adt = null;

        if (type instanceof BasicType) {
            BasicType basicType = (BasicType) type;
            adt = toBasicAdt(basicType);
        } else if (type instanceof ObjectType) {
            ObjectType ot = (ObjectType) type;
            String classname = ot.getClassName();
            adt = toAdt(classname, ot);
        } else if (type instanceof ArrayType) {
            ArrayType at = (ArrayType) type;
            adt = toAdt(at.getBasicType());
        } else {
            //other cases?
        } //end if

        return adt;
    }

    private String toTypeUse(Type type) {
        String typeUse = null;

        if (type instanceof ArrayType) {
            ArrayType at = (ArrayType) type;
            typeUse = "";
            int dims = at.getDimensions();
            for (int i = 0; i < dims; i++) {
                typeUse = typeUse.concat("[]");
            }
        } //end if 

        return typeUse;
    }

    private JVVisibility toVisibility(AccessFlags obj) {
        int value = JVVisibility.DEFAULT;

        if (obj.isPublic()) {
            value = JVVisibility.PUBLIC;
        } else if (obj.isProtected()) {
            value = JVVisibility.PROTECTED;
        } else if (obj.isPrivate()) {
            value = JVVisibility.PRIVATE;
        }

        JVVisibility visib = JVVisibility.getInstance(value);
        return visib;
    }

    private Map<String, DbOOAdt> types = new HashMap<String, DbOOAdt>();

    private DbOOAdt toAdt(String signature, ObjectType ot) throws DbException {
        DbOOAdt adt = types.get(signature);
        if (adt == null) {
            int idx = signature.lastIndexOf('.');
            String packageName = (idx == -1) ? "" : signature.substring(0, idx);
            String classname = (idx == -1) ? signature : signature.substring(idx + 1);
            DbJVPackage pack = findPackageByName(packageName);
            adt = findClassByName(pack, classname);
        }

        return adt;
    } //end toAdt()

    private DbOOAdt findClassByName(DbJVPackage pack, String classname) throws DbException {
        DbJVClass foundClass = null;

        DbRelationN relN = (pack == null) ? m_classModel.getComponents() : pack.getComponents();
        DbEnumeration enu = relN.elements(DbJVClass.metaClass);
        while (enu.hasMoreElements()) {
            DbJVClass claz = (DbJVClass) enu.nextElement();
            String name = claz.getName();
            if (classname.equals(name)) {
                foundClass = claz;
                break;
            }
        } //end while
        enu.close();

        if (foundClass == null) {
            JVClassCategory catg = JVClassCategory.getInstance(JVClassCategory.CLASS);
            foundClass = (pack == null) ? new DbJVClass(m_classModel, catg) : new DbJVClass(pack,
                    catg);
            foundClass.setName(classname);
            types.put(classname, foundClass);
        }

        return foundClass;
    } //end findClassByName()

    private DbOOAdt toBasicAdt(BasicType basicType) throws DbException {
        Map<Byte, DbJVPrimitiveType> javaTypes = getJavaTypes();
        DbJVPrimitiveType type = javaTypes.get(basicType.getType());
        return type;
    }

    private Map<Byte, DbJVPrimitiveType> m_javaTypes = null;

    private Map<Byte, DbJVPrimitiveType> getJavaTypes() throws DbException {

        //if first call, build the map
        if (m_javaTypes == null) {
            m_javaTypes = new HashMap<Byte, DbJVPrimitiveType>();
            DbRelationN relN = m_project.getComponents();
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
                            if ("boolean".equals(name)) {
                                m_javaTypes.put(Constants.T_BOOLEAN, type);
                            } else if ("byte".equals(name)) {
                                m_javaTypes.put(Constants.T_BYTE, type);
                            } else if ("short".equals(name)) {
                                m_javaTypes.put(Constants.T_SHORT, type);
                            } else if ("char".equals(name)) {
                                m_javaTypes.put(Constants.T_CHAR, type);
                            } else if ("int".equals(name)) {
                                m_javaTypes.put(Constants.T_INT, type);
                            } else if ("long".equals(name)) {
                                m_javaTypes.put(Constants.T_LONG, type);
                            } else if ("double".equals(name)) {
                                m_javaTypes.put(Constants.T_DOUBLE, type);
                            } else if ("float".equals(name)) {
                                m_javaTypes.put(Constants.T_FLOAT, type);
                            } //end if
                        } //end while
                        enu3.close();
                    } //end if
                    pack.getAlias();
                } //end while
                enu2.close();
            } //end while
            enu.close();
        } //end if

        return m_javaTypes;
    }

    private DbJVPackage findPackageByName(String qualifiedPackageName) throws DbException {
        DbJVPackage pack = packages.get(qualifiedPackageName);

        if (pack == null) {
            DbOOAbsPackage parent = m_classModel;
            StringTokenizer st = new StringTokenizer(qualifiedPackageName, ".");

            while (st.hasMoreElements()) {
                String token = st.nextToken();
                pack = findPackageByName(parent, token);
                if (pack == null) {
                    pack = new DbJVPackage(parent);
                    pack.setName(token);
                } //end if 
                parent = pack;
            } //end while 

            packages.put(qualifiedPackageName, pack);
        } //end if

        return pack;
    }

    private DbJVPackage findPackageByName(DbOOAbsPackage parent, String packageName)
            throws DbException {
        DbJVPackage foundPackage = null;

        DbRelationN relN = parent.getComponents();
        DbEnumeration enu = relN.elements(DbJVPackage.metaClass);
        while (enu.hasMoreElements()) {
            DbJVPackage pack = (DbJVPackage) enu.nextElement();
            String name = pack.getName();
            if (packageName.equals(name)) {
                foundPackage = pack;
                break;
            }
        } //end while
        enu.close();

        return foundPackage;
    }

    private void createAndRevealDiagram(DefaultMainFrame mainFrame, DbJVPackage topMostPackage,
            Controller controller, int startJobDone, int endJobDone) throws DbException {

        controller.checkPoint(startJobDone);
        DbOODiagram topMostDiagram = null;

        //create a diagram for each imported package
        int i = 0, nb = m_importedPackages.size();
        int span = endJobDone - startJobDone;
        for (DbJVPackage pack : m_importedPackages) {
            DbOODiagram diagram = new DbOODiagram(pack);
            CreateMissingGraphicsAction.createOOGraphics(diagram, (Point) null);
            new JVDiagramLayout(diagram, (GraphicComponent[]) null);

            if (pack.equals(topMostPackage)) {
                topMostDiagram = diagram;
            }

            int jobDone = startJobDone + (i * span) / nb;
            controller.checkPoint(jobDone);
            i++;
        } //end for

        if (topMostDiagram != null) {
            mainFrame.addDiagramInternalFrame(topMostDiagram);
            mainFrame.findInExplorer(topMostDiagram);
        }

        controller.checkPoint(endJobDone);
    } //end createAndRevealDiagram()

    private static class ErrorReport {
        File m_file;
        Throwable m_th;

        public ErrorReport(File file, Throwable th) {
            m_file = file;
            m_th = th;
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            File root = new File(
                    "C:/development5/Javaforge Repositories/OpenModelSphere_905/trunk/workspace/org.modelsphere.jack/bin/org/modelsphere/jack/io");
            boolean exists = root.exists();

            if (exists) {
                Controller controller = new TextController();
                ReverseJavaBytecodeParameters params = new ReverseJavaBytecodeParameters();
                params.setSelectedFolder(root);
                Worker worker = new ReverseJavaBytecodeWorker(params);
                controller.start(worker);
            } //end if
        } catch (Exception ex) {
            ex.toString();
        }
    } //end main()

    private static class TextController extends Controller {
        public void start(final Worker worker) {
            setState(STATE_IN_PROGRESS);
            super.start(worker);
        } //end start()
    }

} //end ImportJavaWorker