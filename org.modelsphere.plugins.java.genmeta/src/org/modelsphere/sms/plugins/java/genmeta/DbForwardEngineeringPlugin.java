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
package org.modelsphere.sms.plugins.java.genmeta;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginAction;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.actions.PluginDefaultAction;
import org.modelsphere.jack.srtool.forward.*;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.oo.db.*;
import org.modelsphere.sms.oo.db.srtypes.OOAggregation;
import org.modelsphere.sms.oo.db.srtypes.OOVisibility;
import org.modelsphere.sms.oo.forward.OOForwardEngineeringPlugin;
import org.modelsphere.sms.oo.java.db.*;

public final class DbForwardEngineeringPlugin extends OOForwardEngineeringPlugin implements Plugin2 {

    //one variable list per forward module
    private static final String DB_FORWARD = "Generic Forward"; //NOT LOCALIZABLE, debug info only
    protected VariableScope m_varList = new VariableScope(DB_FORWARD);

    public VariableScope getVarScope() {
        return m_varList;
    }

    public void setVarScope(VariableScope varScope) {
    }

    private static final String PROP_ROOT_DIR = "RootDir";
    private static final String JACK_PACKAGE = "org.modelsphere.jack";
    private static final String DB_PACK_NAME = "db";
    private static final String INIT_STATIC = "initStatic";
    private static final String SET_DEFAULT_INITIAL_VALUES = "setDefaultInitialValues";
    private static final String SP2 = "  ";
    private static final String SP4 = "    ";
    private static final String SP6 = "      ";
    private static final String SP8 = "        ";
    private static final String EOL = System.getProperty("line.separator");

    private static final int META_NONE = 0;
    private static final int META_FIELD = 1;
    private static final int META_REL1 = 2;
    private static final int META_RELN = 3;
    private static final int META_CHOICE = 4;

    private static final int REL_NONE = 0;
    private static final int REL_ONE_TO_ONE = 1;
    private static final int REL_ONE_TO_MANY = 2;
    private static final int REL_MANY_TO_ONE = 3;
    private static final int REL_MANY_TO_MANY = 4;

    //Language-related constants
    private static final int ENGLISH = 0;
    private static final int FRENCH = 1;
    private static final int DEFAULT_LANGUAGE = ENGLISH;
    private static final int LAST_LANGUAGE = FRENCH;

    // Steps for supporting a new language (eg German) [MS]
    // In DbForward.java
    // 1) get the ISO language code for the language (for German it's 'de')
    // 2) add a constant for German just above (GERMAN = 1), and increment LAST_LANGUAGE
    // 3) add a dictionary in m_dictionaries (the ISO code is passed to the constructor)
    // 4) adapt getMetaGUINameStr() method
    //
    // In the application model (the .sms file)
    // 1) add a new user-defined field named "de alias" besides "fr alias"
    // 2) fill all the new fields with German translations
    //
    // Notes
    // 1) this should work for all European languages; an analysis has to be
    //   done to support a non-European language like Japenese.
    // 2) getDisplayName() should be adapted for support different manners to
    //   form the plural (both English & French just add a 's' to form the plural,
    //   but it's not necessary the case for all the languages).

    //static member
    private static String g_generated;

    //Support of two languages, English and French
    private LocaleDictionary[] m_dictionaries = new LocaleDictionary[] {
            new LocaleDictionary("en"), new LocaleDictionary("fr") };
    private String m_fileSep = System.getProperty("file.separator");

    //members set by initializer
    private DbJVPackage m_dbPack;
    private String m_packPrefix;
    private String m_packQualName;
    private HashMap m_choices;
    //members set by methods
    private String m_pathPrefix = null;
    private String m_pathName = null;

    //////////////////
    //CONSTRUCTOR & INITIALIZER
    public DbForwardEngineeringPlugin() {
    }

    //must be called to have a valid DbForward instance
    public void init(DbJVPackage dbPack) throws DbException {
        m_dbPack = dbPack;
        m_packPrefix = dbPack.getComposite().getSemanticalName(DbObject.LONG_FORM);
        m_packQualName = m_packPrefix + "." + DB_PACK_NAME;
        m_choices = new HashMap();
    }

    //
    //////////////////

    //variable scope
    private VariableScope m_scope = new VariableScope("");

    public VariableScope getVarList() {
        return m_scope;
    }

    //////////////////
    //OVERRIDES Plugin
    public PluginSignature getSignature() {
        return null;
    }

    public Class[] getSupportedClasses() {
        return new Class[] { DbJVPackage.class };
    }

    private PropertyScreenPreviewInfo m_propertyScreenPreviewInfo = null;

    public PropertyScreenPreviewInfo getPropertyScreenPreviewInfo() {
        if (m_propertyScreenPreviewInfo == null) {
            m_propertyScreenPreviewInfo = new PropertyScreenPreviewInfo() {
                public String getTabName() {
                    return "GenMeta Preview";
                }

                public String getContentType() {
                    return "text/plain";
                } //NOT LOCALIZABLE, content type

                public Class[] getSupportedClasses() {
                    return new Class[] { DbJVClass.class };
                }
            };
        }

        return m_propertyScreenPreviewInfo;
    }

    private GenerateInFileInfo m_generateInFileInfo = null;

    public GenerateInFileInfo getGenerateInFileInfo() {
        if (m_generateInFileInfo == null) {
            m_generateInFileInfo = new GenerateInFileInfo() {
                public String getDefaultExtension() {
                    return "txt";
                }

                public String getPopupItemTitle() {
                    return "Meta model";
                }
            };
        }

        return m_generateInFileInfo;
    }

    //OVERRIDES Plugin
    //////////////////

    //////////////////
    //OVERRIDES Forward
    public Rule getRuleOf(DbObject so) throws DbException {
        Rule rule = null;

        if (so instanceof DbJVClass) {
            rule = g_classifier;
        }

        return rule;
    }

    public String getRootDirFromUserProp() {
        PropertiesSet appSet = PropertiesManager.APPLICATION_PROPERTIES_SET;
        String rootDir = appSet.getPropertyString(DbForwardEngineeringPlugin.class, PROP_ROOT_DIR,
                "");
        return rootDir;
    }

    private File m_actualDirectory = null;

    public String getFeedBackMessage(ArrayList generatedList) {
        String message;
        int nbForwards = generatedList.size();

        //No files forwarded
        if (nbForwards == 0) {
            message = LocaleMgr.message.getString("noFileForwarded");
        } else if (nbForwards == 1) {
            String pattern = LocaleMgr.message.getString("oneFileGenerated");
            String filename = convertSeparators((String) generatedList.get(0));
            message = MessageFormat.format(pattern, new Object[] { filename });
        } else {
            String rootDir = (m_actualDirectory == null) ? getRootDirFromUserProp()
                    : m_actualDirectory.getPath();
            String pattern = LocaleMgr.message.getString("nFilesForwardedIn");
            message = MessageFormat.format(pattern,
                    new Object[] { new Integer(nbForwards), rootDir });
        }

        return message;
    }

    protected ForwardOptions createForwardOptions(DbObject[] semObjs) {
        ForwardOptions options = new DbForwardOptions(this, semObjs);
        return options;
    }

    private ForwardToolkitInterface m_toolkit = new DbForwardToolkit();

    protected ForwardToolkitInterface getToolkit() {
        return m_toolkit;
    }

    //OVERRIDES Forward
    //////////////////

    //called by DbForwardToolkit
    static PluginSignature getPluginSignature() {
        return null;
    }

    private static ClassifierRule g_classifier = new ClassifierRule();

    // ***
    // PACKAGE-BASED METHODS
    // ***

    //
    // ENTRY POINT, called by DbForwardWorker.runJob()
    //
    void genPackages(DbJVPackage dbPack, File actualDirectory, Controller controller,
            ArrayList generatedFiles) throws DbException, IOException {
        init(dbPack);
        m_actualDirectory = actualDirectory;
        genPackage(dbPack, actualDirectory, controller, generatedFiles);
        DbEnumeration dbEnum = dbPack.getComponents().elements(DbJVPackage.metaClass);
        while (dbEnum.hasMoreElements()) {
            genPackages((DbJVPackage) dbEnum.nextElement(), actualDirectory, controller,
                    generatedFiles);

            if (!controller.checkPoint()) { //User has cancelled the job
                break;
            } //end if
        } //end while
        dbEnum.close();
    }

    //
    // All the methods below are private to this class
    //
    private void genPackage(DbJVPackage dbPack, File actualDirectory, Controller controller,
            ArrayList generatedFiles) throws DbException, IOException {
        if (!dbPack.getName().equals(DB_PACK_NAME))
            return;

        //Clear previous entries in all the dictionaries
        for (int i = 0; i < m_dictionaries.length; i++) {
            LocaleDictionary dictionary = m_dictionaries[i];
            dictionary.m_localeEntries.clear();
        } //end for

        m_pathPrefix = getPathName(dbPack.getComposite(), actualDirectory, m_fileSep);
        m_pathName = m_pathPrefix + DB_PACK_NAME + m_fileSep;
        deleteFiles();
        genFinalClasses(controller, generatedFiles);
        collectChoices();

        DbEnumeration dbEnum = dbPack.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            DbObject dbo = dbEnum.nextElement();
            if (dbo instanceof DbJVClass) {
                if (((DbJVClass) dbo).getCompilationUnit() == null) {
                    genFile(dbo, controller, generatedFiles);
                } //end if
            } else if (dbo instanceof DbJVCompilationUnit) {
                if (dbo.getNbNeighbors(DbJVCompilationUnit.fClasses) != 0) {
                    genFile(dbo, controller, generatedFiles);
                } //end if
            } //end if

            if (!controller.checkPoint()) { //User has cancelled the job
                break;
            } //end if
        } //end while
        dbEnum.close();

        genLocaleStrs(controller, generatedFiles);
        g_generated = g_generated + '\n' + m_packQualName;
    }

    private String getPathName(DbObject dbo, File actualDirectory, String fileSep)
            throws DbException {
        String pathName = "";
        while (dbo instanceof DbJVPackage) {
            pathName = dbo.getName() + fileSep + pathName;
            dbo = dbo.getComposite();
        }

        String rootDir = actualDirectory.getPath();

        if (!rootDir.endsWith(fileSep))
            rootDir = rootDir + fileSep;
        return rootDir + pathName;
    }

    private void genFile(DbObject dbo, Controller controller, ArrayList generatedFiles)
            throws DbException, IOException {
        String name = (dbo instanceof DbJVCompilationUnit ? dbo.getName() : dbo.getName() + ".java");
        String filename = m_pathName + name;
        Writer writer = new FileWriter(filename);
        genClassifier(writer, dbo);
        writer.close();
        generatedFiles.add(filename);
        controller.println(filename);
    }

    private void deleteFiles() throws IOException {
        File dbDir = new File(m_pathName);
        dbDir.mkdirs();
        File[] files = dbDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile())
                files[i].delete();
        }
    }

    private void genFinalClasses(Controller controller, ArrayList generatedFiles)
            throws DbException, IOException {
        String filename = m_pathName + "ApplClasses.java";
        PrintWriter output = new PrintWriter(new FileWriter(filename));
        output.println("package " + m_packQualName + ";");
        output.println();
        output.println("import " + JACK_PACKAGE + ".baseDb.meta.MetaClass;");
        output.println();
        output.println("public abstract class ApplClasses {");
        output.println("  private static MetaClass[] finalClasses = {");
        String sep = "    ";
        DbEnumeration dbEnumClasses = m_dbPack.getComponents().elements(DbJVClass.metaClass);
        while (dbEnumClasses.hasMoreElements()) {
            DbJVClass cls = (DbJVClass) dbEnumClasses.nextElement();
            if (cls.isInterface() || cls.getNbNeighbors(DbOOClass.fSubInheritances) != 0)
                continue;
            output.write(sep + cls.getName() + ".metaClass");
            sep = "," + EOL + "    ";
        }
        dbEnumClasses.close();

        output.println("};");
        output.println();
        output.println("  public static MetaClass[] getFinalClasses() {return finalClasses;}");
        output.println("}");
        output.close();
        generatedFiles.add(filename);
        controller.println(filename);
    }

    private void genLocaleStrs(Controller controller, ArrayList generatedFiles) throws IOException {
        for (int i = 0; i <= LAST_LANGUAGE; i++) {
            LocaleDictionary dictionary = m_dictionaries[i];
            HashMap entries = dictionary.m_localeEntries;
            String[] sortedStrs = new String[entries.size()];
            Iterator iter = entries.keySet().iterator();

            int j = 0;
            while (iter.hasNext()) {
                String key = (String) iter.next();
                sortedStrs[j++] = key + "=" + (String) entries.get(key);
            }
            Arrays.sort(sortedStrs);

            String path = m_pathPrefix + "international" + m_fileSep;
            (new File(path)).mkdirs();
            String filename = (i == DEFAULT_LANGUAGE) ? path + "DbResources.properties" : path
                    + "DbResources_" + dictionary.m_locale + ".properties";
            File file = new File(filename);
            file.delete();
            PrintWriter output = new PrintWriter(new FileWriter(file));
            output
                    .println("/*************************************************************************");
            output.println();
            output.println("Copyright (C) 2009 Grandite");
            output.println();
            output.println("This file is part of Open ModelSphere.");
            output.println();
            output
                    .println("Open ModelSphere is free software; you can redistribute it and/or modify");
            output.println("it under the terms of the GNU General Public License as published by");
            output.println("the Free Software Foundation; either version 3 of the License, or");
            output.println("(at your option) any later version.");
            output.println();
            output.println("This program is distributed in the hope that it will be useful,");
            output.println("but WITHOUT ANY WARRANTY; without even the implied warranty of");
            output.println("MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the");
            output.println("GNU General Public License for more details.");
            output.println();
            output.println("You should have received a copy of the GNU General Public License");
            output.println("along with this program; if not, write to the Free Software");
            output
                    .println("Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA ");
            output.println("or see http://www.gnu.org/licenses/.");
            output.println();
            output
                    .println("You can redistribute and/or modify this particular file even under the");
            output.println("terms of the GNU Lesser General Public License (LGPL) as published by");
            output.println("the Free Software Foundation; either version 3 of the License, or");
            output.println("(at your option) any later version.");
            output.println();
            output
                    .println("You should have received a copy of the GNU Lesser General Public License");
            output.println("(LGPL) along with this program; if not, write to the Free Software");
            output
                    .println("Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA");
            output.println("or see http://www.gnu.org/licenses/.");
            output.println();
            output.println("You can reach Grandite at: ");
            output.println();
            output.println("20-1220 Lebourgneuf Blvd.");
            output.println("Quebec, QC");
            output.println("Canada  G2K 2G4");
            output.println();
            output.println("or");
            output.println();
            output.println("open-modelsphere@grandite.com");
            output.println();
            output
                    .println("**********************************************************************/");
            output.println();
            for (j = 0; j < sortedStrs.length; j++) {
                output.println(sortedStrs[j]);
            }
            output.close();
            generatedFiles.add(filename);
            controller.println(filename);

        } //end for
    } //end genLocaleStrs()

    // ***
    // CLASSIFIER-BASED METHODS
    // ***
    private void genClassifier(Writer writer, DbObject dbo) throws DbException, IOException {
        PrintWriter output = new PrintWriter(writer);
        genClassHeader(output, dbo);
        if (dbo instanceof DbJVCompilationUnit) {
            DbJVCompilationUnit pack = (DbJVCompilationUnit) dbo;
            DbRelationN relationN = pack.getClasses();
            DbEnumeration dbEnum = relationN.elements(DbJVClass.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbJVClass claz = (DbJVClass) dbEnum.nextElement();
                genCommentClassifier(output, claz);
                genAdt(output, claz);
            }
            dbEnum.close();

        } else if (dbo instanceof DbJVClass) {
            genCommentClassifier(output, (DbJVClass) dbo);
            genAdt(output, (DbJVClass) dbo);
        }
    } //end genClassifier()

    private void collectChoices() throws DbException {
        DbEnumeration dbEnumClasses = m_dbPack.getComponents().elements(DbJVClass.metaClass);
        while (dbEnumClasses.hasMoreElements()) {
            DbJVClass cls = (DbJVClass) dbEnumClasses.nextElement();
            HashMap names = new HashMap(11);
            DbEnumeration dbEnumFields = cls.getComponents().elements(DbJVDataMember.metaClass);
            while (dbEnumFields.hasMoreElements()) {
                DbJVDataMember field = (DbJVDataMember) dbEnumFields.nextElement();
                DbOOAssociationEnd end = field.getAssociationEnd();
                if (end == null)
                    continue;
                DbOOAssociationEnd oppEnd = end.getOppositeEnd();
                if (end.getAggregation().getValue() == OOAggregation.COMPOSITE
                        || oppEnd.getAggregation().getValue() == OOAggregation.COMPOSITE)
                    continue;
                int mult = end.getMultiplicity().getValue();
                if (mult != SMSMultiplicity.EXACTLY_ONE && mult != SMSMultiplicity.OPTIONAL)
                    continue;
                String name = field.getName();
                DbJVDataMember firstField = (DbJVDataMember) names.get(name);
                if (firstField == null)
                    names.put(name, field);
                else {
                    DbJVDataMember[] oldFields = (DbJVDataMember[]) m_choices.get(firstField);
                    if (oldFields == null)
                        oldFields = new DbJVDataMember[] { firstField };
                    DbJVDataMember[] newFields = new DbJVDataMember[oldFields.length + 1];
                    System.arraycopy(oldFields, 0, newFields, 0, oldFields.length);
                    newFields[oldFields.length] = field;
                    m_choices.put(firstField, newFields);
                    m_choices.put(field, firstField);
                }
            }
            dbEnumFields.close();
        }
        dbEnumClasses.close();
    }

    private String capitalize(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    private String getOppositeRelationStr(DbJVDataMember member) throws DbException {
        String strValue = "f" + capitalize(member.getName());
        DbJVDataMember[] members = (DbJVDataMember[]) m_choices.get(member);
        if (members == null) {
            DbJVDataMember oppMember = getOppositeDataMember(member);
            strValue = strValue + ".setOppositeRel("
                    + ((DbJVClass) oppMember.getComposite()).getName() + ".f"
                    + capitalize(oppMember.getName()) + ");";
        } else {
            strValue = strValue + ".setOppositeRels(new MetaRelation[] {";
            for (int i = 0; i < members.length; i++) {
                if (i != 0)
                    strValue = strValue + ", ";
                DbJVDataMember oppMember = getOppositeDataMember(members[i]);
                strValue = strValue + ((DbJVClass) oppMember.getComposite()).getName() + ".f"
                        + capitalize(oppMember.getName());
            }
            strValue = strValue + "});";
        }
        return strValue;
    }

    private String getVisibilityStr(OOVisibility vis) throws DbException {
        switch (vis.getValue()) {
        case OOVisibility.PRIVATE:
            return "private ";
        case OOVisibility.PROTECTED:
            return "protected ";
        case OOVisibility.PUBLIC:
            return "public ";
        }
        return "";
    }

    private String getMultiplicityStr(DbJVDataMember member) throws DbException {
        switch (member.getAssociationEnd().getMultiplicity().getValue()) {
        case SMSMultiplicity.OPTIONAL:
            return "0";
        case SMSMultiplicity.EXACTLY_ONE:
            return "1";
        case SMSMultiplicity.MANY:
            return "0, MetaRelationN.cardN";
        case SMSMultiplicity.ONE_OR_MORE:
            return "1, MetaRelationN.cardN";
        }
        return "";
    }

    private static final String[] notAbstractClassFlagUDFs = new String[] { "access control",
            "cluster root", "naming root", "matchable", "no UDF" };
    private static final String[] notAbstractClassFlagStrs = new String[] { "ACCESS_CTL",
            "CLUSTER_ROOT", "NAMING_ROOT", "MATCHABLE", "NO_UDF" };

    private String getMetaClassFlagsStr(DbJVClass adt) throws DbException {
        String strValue = "";
        if (!adt.isAbstract()) {
            boolean[] classFlags = new boolean[notAbstractClassFlagUDFs.length];
            for (DbJVClass superAdt = adt; superAdt != null; superAdt = getSuperAdt(superAdt)) {
                for (int i = 0; i < notAbstractClassFlagUDFs.length; i++) {
                    Boolean flag = (Boolean) superAdt.getUDF(notAbstractClassFlagUDFs[i]);
                    if (flag != null && flag.booleanValue())
                        classFlags[i] = true;
                }
            }
            for (int i = 0; i < classFlags.length; i++) {
                if (classFlags[i])
                    strValue = strValue + (strValue.length() == 0 ? ", " : " | ") + "MetaClass."
                            + notAbstractClassFlagStrs[i];
            }
        }

        // This flag may apply to abstract MetaClass
        Boolean umlExtensibilityFilter = (Boolean) adt.getUDF("uml Extensibility Filter");
        if (umlExtensibilityFilter != null && umlExtensibilityFilter.booleanValue())
            strValue = strValue + (strValue.length() == 0 ? ", " : " | ") + "MetaClass."
                    + "UML_EXTENSIBILITY_FILTER";

        if (strValue.length() == 0)
            strValue = ", 0";
        return strValue;
    }

    private static final String[] fieldFlagUDFs = new String[] { "copy links", "huge relation N",
            "integrable", "write check", "no write check", "integrable by name" };
    private static final String[] fieldFlagStrs = new String[] { "COPY_REFS", "HUGE_RELN",
            "INTEGRABLE", "WRITE_CHECK", "NO_WRITE_CHECK", "INTEGRABLE_BY_NAME" };

    private String getFieldPropertiesStr(DbJVDataMember member) throws DbException {
        String name = "f" + capitalize(member.getName());
        String newLine = EOL + SP8;
        String strValue = "";
        for (int i = 0; i < fieldFlagUDFs.length; i++) {
            Boolean flag = (Boolean) member.getUDF(fieldFlagUDFs[i]);
            if (flag != null && flag.booleanValue())
                strValue = strValue
                        + (strValue.length() == 0 ? newLine + name + ".setFlags(" : " | ")
                        + "MetaField." + fieldFlagStrs[i];
        }
        if (strValue.length() != 0)
            strValue = strValue + ");";

        String screenOrder = (String) member.getUDF("screen order");
        if (screenOrder != null)
            strValue = strValue + newLine + name + ".setScreenOrder(\"" + screenOrder + "\");";

        String pluginName = (String) member.getUDF("plugin name");
        if (pluginName != null)
            strValue = strValue + newLine + name + ".setRendererPluginName(\"" + pluginName
                    + "\");";

        Boolean hideOnScreen = (Boolean) member.getUDF("hide on screen");
        if (hideOnScreen != null && hideOnScreen.booleanValue())
            strValue = strValue + newLine + name + ".setVisibleInScreen(false);";

        Boolean notEditable = (Boolean) member.getUDF("not editable");
        if (notEditable != null && notEditable.booleanValue())
            strValue = strValue + newLine + name + ".setEditable(false);";
        return strValue;
    }

    private String getMetaGUINameStr(DbSemanticalObject obj) throws DbException {
        LocaleDictionary enDictionary = m_dictionaries[ENGLISH];
        LocaleDictionary frDictionary = m_dictionaries[FRENCH];
        HashMap enEntries = enDictionary.m_localeEntries;
        HashMap frEntries = frDictionary.m_localeEntries;

        String name = obj.getName();
        String enName = obj.getAlias();
        String frName = (String) obj.getUDF("fr alias");

        if (enName != null && enName.length() != 0) {
            String guiName2 = (String) enEntries.get(name);
            if (guiName2 != null && !guiName2.equals(enName)) {
                name = obj.getComposite().getName() + "." + name;
            }
            enEntries.put(name, enName);
            frEntries.put(name, frName);
        }
        return "LocaleMgr.db.getString(\"" + name + "\")";
    }

    private String getString(DbObject dbo, MetaField field) throws DbException {
        String str = (String) dbo.get(field);
        return (str == null ? "" : str);
    }

    private String getTypeStr(DbObject dbo, MetaField field) throws DbException {
        DbOOAdt type = (DbOOAdt) dbo.get(field);
        return (type != null ? type.getName() : "void");
    }

    private String getFieldTypeStr(DbJVDataMember member) throws DbException {
        return getTypeStr(member, DbOODataMember.fType);
    }

    private String getAPITypeNameStr(DbJVDataMember member) throws DbException {
        String strValue = "void";
        DbOOAdt type = member.getType();
        if (type != null) {
            strValue = type.getName();
            if (type instanceof DbJVPrimitiveType) {
                if (strValue.equals("int"))
                    strValue = "Integer";
                else
                    strValue = capitalize(strValue);
            } else if (strValue.startsWith("Sr"))
                strValue = strValue.substring(2);
        }
        return strValue;
    }

    private DbJVClass getSuperAdt(DbJVClass adt) throws DbException {
        DbJVClass superAdt = null;
        Integer stereotype = new Integer(adt.getStereotype().getValue());
        DbEnumeration dbEnum = adt.getSuperInheritances().elements();
        while (dbEnum.hasMoreElements()) {
            DbJVClass supAdt = (DbJVClass) ((DbJVInheritance) dbEnum.nextElement()).getSuperClass();
            if (stereotype.equals(new Integer(supAdt.getStereotype().getValue()))) {
                superAdt = supAdt;
                break;
            }
        }
        dbEnum.close();
        return superAdt;
    }

    private int getFieldType(DbJVDataMember member) throws DbException {
        if (member.isStatic() || member.isTransient())
            return META_NONE;
        DbOOAssociationEnd end = member.getAssociationEnd();
        if (end == null)
            return META_FIELD;
        DbOOAssociationEnd oppEnd = end.getOppositeEnd();
        if (end.getAggregation().getValue() == OOAggregation.COMPOSITE
                || oppEnd.getAggregation().getValue() == OOAggregation.COMPOSITE)
            return META_NONE;
        Object value = m_choices.get(member);
        if (value == null) {
            int mult = end.getMultiplicity().getValue();
            if (mult == SMSMultiplicity.EXACTLY_ONE || mult == SMSMultiplicity.OPTIONAL)
                return META_REL1;
            return META_RELN;
        }
        if (value instanceof DbJVDataMember[])
            return META_CHOICE;
        return META_NONE;
    }

    private int getRelationType(DbJVDataMember member) throws DbException {
        DbJVDataMember oppMember = getOppositeDataMember(member);
        if (oppMember == null)
            return REL_NONE;
        Object value = m_choices.get(member);
        if (value != null)
            return (value instanceof DbJVDataMember[] ? REL_ONE_TO_MANY : REL_NONE);
        if (m_choices.get(oppMember) != null)
            return REL_MANY_TO_ONE;

        int mult = member.getAssociationEnd().getMultiplicity().getValue();
        int oppMult = oppMember.getAssociationEnd().getMultiplicity().getValue();
        if (mult == SMSMultiplicity.EXACTLY_ONE || mult == SMSMultiplicity.OPTIONAL) {
            if (oppMult == SMSMultiplicity.EXACTLY_ONE || oppMult == SMSMultiplicity.OPTIONAL)
                return REL_ONE_TO_ONE;
            else
                return REL_ONE_TO_MANY;
        } else {
            if (oppMult == SMSMultiplicity.EXACTLY_ONE || oppMult == SMSMultiplicity.OPTIONAL)
                return REL_MANY_TO_ONE;
            else
                return REL_MANY_TO_MANY;
        }
    }

    private DbJVDataMember getOppositeDataMember(DbJVDataMember member) throws DbException {
        DbOOAssociationEnd end = member.getAssociationEnd();
        if (end == null)
            return null;
        DbOOAssociationEnd oppEnd = end.getOppositeEnd();
        if (end.getAggregation().getValue() == OOAggregation.COMPOSITE
                || oppEnd.getAggregation().getValue() == OOAggregation.COMPOSITE)
            return null;
        DbJVDataMember oppMember = (DbJVDataMember) oppEnd.getAssociationMember();
        // if opposite is a choice, take the good field
        Object value = m_choices.get(oppMember);
        if (value instanceof DbJVDataMember)
            oppMember = (DbJVDataMember) value;
        return oppMember;
    }

    private void genClassHeader(PrintWriter output, DbObject dbo) throws DbException, IOException {
        output
                .println("/*************************************************************************");
        output.println();
        output.println("Copyright (C) 2008 Grandite");
        output.println();
        output.println("This file is part of Open ModelSphere.");
        output.println();
        output.println("Open ModelSphere is free software; you can redistribute it and/or modify");
        output.println("it under the terms of the GNU General Public License as published by");
        output.println("the Free Software Foundation; either version 3 of the License, or");
        output.println("(at your option) any later version.");
        output.println();
        output.println("This program is distributed in the hope that it will be useful,");
        output.println("but WITHOUT ANY WARRANTY; without even the implied warranty of");
        output.println("MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the");
        output.println("GNU General Public License for more details.");
        output.println();
        output.println("You should have received a copy of the GNU General Public License");
        output.println("along with this program; if not, write to the Free Software");
        output.println("Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA ");
        output.println("or see http://www.gnu.org/licenses/.");
        output.println();
        output.println("You can reach Grandite at: ");
        output.println();
        output.println("20-1220 Lebourgneuf Blvd.");
        output.println("Quebec, QC");
        output.println("Canada  G2K 2G4");
        output.println();
        output.println("or");
        output.println();
        output.println("open-modelsphere@grandite.com");
        output.println();
        output.println("**********************************************************************/");
        output.println("package " + m_packQualName + ";");
        output.println();
        output.println("import java.awt.*;");
        output.println("import " + JACK_PACKAGE + ".baseDb.meta.*;");
        output.println("import " + JACK_PACKAGE + ".baseDb.db.*;");
        output.println("import " + JACK_PACKAGE + ".baseDb.db.srtypes.*;");
        output.println("import " + m_packQualName + ".srtypes.*;");
        output.println("import " + m_packPrefix + ".international.LocaleMgr;");

        DbJVPackage pack = (DbJVPackage) m_dbPack.getComposite();
        while (true) {
            pack = (DbJVPackage) pack.getComposite();
            if (pack.getName().equals("modelsphere"))
                break;
            DbEnumeration dbEnum = pack.getComponents().elements(DbJVPackage.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbJVPackage subPack = (DbJVPackage) dbEnum.nextElement();
                if (subPack.getName().equals(DB_PACK_NAME)) {
                    String subPackName = subPack.getSemanticalName(DbObject.LONG_FORM);
                    output.println("import " + subPackName + ".*;");
                    output.println("import " + subPackName + ".srtypes.*;");
                    break;
                }
            }
            dbEnum.close();
        }

        if (dbo instanceof DbJVCompilationUnit) {
            DbEnumeration dbEnum = dbo.getComponents().elements(DbJVImport.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbJVImport imp = (DbJVImport) dbEnum.nextElement();
                DbSemanticalObject impObj = imp.getImportedObject();
                output.println("import " + impObj.getSemanticalName(DbObject.LONG_FORM)
                        + (impObj instanceof DbJVPackage || imp.isAll() ? ".*;" : ";"));
            }
            dbEnum.close();
        }
        output.println();
    }

    private void genAdt(PrintWriter output, DbJVClass adt) throws DbException, IOException {
        if (adt.isInterface())
            genInterface(output, adt);
        else
            genClass(output, adt);
    }

    private void genInterface(PrintWriter output, DbJVClass adt) throws DbException, IOException {
        output.write(getVisibilityStr(adt.getVisibility()) + "interface " + adt.getName());
        DbJVClass superAdt = getSuperAdt(adt);
        if (superAdt != null)
            output.write(" extends " + superAdt.getName());
        output.println(" {");
        output.println();
        genMethods(output, adt);
        output.println("}");
    }

    private void genClass(PrintWriter output, DbJVClass adt) throws DbException, IOException {
        output.write(getVisibilityStr(adt.getVisibility()));
        if (adt.isAbstract())
            output.write("abstract ");
        if (adt.getSubInheritances().size() == 0)
            output.write("final ");
        output.write("class " + adt.getName());
        DbJVClass superAdt = getSuperAdt(adt);
        if (superAdt != null)
            output.write(" extends " + superAdt.getName());

        DbEnumeration dbEnum = adt.getSuperInheritances().elements();
        int i = 0;
        while (dbEnum.hasMoreElements()) {
            DbJVClass supAdt = (DbJVClass) ((DbJVInheritance) dbEnum.nextElement()).getSuperClass();
            if (supAdt.isInterface()) {
                output.write((i == 0 ? " implements " : ", ") + supAdt.getName());
                i++;
            }
        }
        dbEnum.close();

        output.println(" {");
        output.println();
        genMetaFields(output, adt);
        genMetaClass(output, adt);
        genMetaStaticBlock(output, adt);
        genStaticFields(output, adt);
        genTransientFields(output, adt);
        genStaticBlock(output, adt);
        genInstanceVariables(output, adt);
        genConstructors(output, adt);
        genMethods(output, adt);
        genSetters(output, adt);
        genGetters(output, adt);
        output.println("}");
    }

    private void genMetaFields(PrintWriter output, DbJVClass adt) throws DbException, IOException {
        output.println(SP2 + "//Meta");
        output.println();
        DbEnumeration dbEnum = adt.getComponents().elements(DbJVDataMember.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVDataMember member = (DbJVDataMember) dbEnum.nextElement();
            genCommentMetaField(output, member);
            switch (getFieldType(member)) {
            case DbForwardEngineeringPlugin.META_FIELD:
                output.println(SP2 + "public static final MetaField f"
                        + capitalize(member.getName()));
                output.println(SP4 + "= new MetaField(" + getMetaGUINameStr(member) + ");");
                break;
            case DbForwardEngineeringPlugin.META_REL1:
                output.println(SP2 + "public static final MetaRelation1 f"
                        + capitalize(member.getName()));
                output.println(SP4 + "= new MetaRelation1(" + getMetaGUINameStr(member) + ", "
                        + getMultiplicityStr(member) + ");");
                break;
            case DbForwardEngineeringPlugin.META_RELN:
                output.println(SP2 + "public static final MetaRelationN f"
                        + capitalize(member.getName()));
                output.println(SP4 + "= new MetaRelationN(" + getMetaGUINameStr(member) + ", "
                        + getMultiplicityStr(member) + ");");
                break;
            case DbForwardEngineeringPlugin.META_CHOICE:
                output.println(SP2 + "public static final MetaChoice f"
                        + capitalize(member.getName()));
                output.println(SP4 + "= new MetaChoice(" + getMetaGUINameStr(member) + ", "
                        + getMultiplicityStr(member) + ");");
                break;
            }
        }
        dbEnum.close();
        output.println();
    }

    private void genMetaClass(PrintWriter output, DbJVClass adt) throws DbException, IOException {
        output.println(SP2 + "public static final MetaClass metaClass = new MetaClass(");
        output.println(SP4 + getMetaGUINameStr(adt) + ", " + adt.getName() + ".class,");
        output.print(SP4 + "new MetaField[] {");
        DbEnumeration dbEnum = adt.getComponents().elements(DbJVDataMember.metaClass);
        int i = 0;
        while (dbEnum.hasMoreElements()) {
            DbJVDataMember member = (DbJVDataMember) dbEnum.nextElement();
            if (getFieldType(member) == META_NONE)
                continue;
            if (i != 0) {
                output.println(",");
                output.print(SP6);
            }
            output.print("f" + capitalize(member.getName()));
            i++;
        }
        dbEnum.close();
        output.println("}" + getMetaClassFlagsStr(adt) + ");");
        output.println();
    }

    private void genClassProperties(PrintWriter output, DbJVClass adt) throws DbException,
            IOException {
        DbJVClass superAdt = getSuperAdt(adt);
        if (superAdt != null) {
            String name = superAdt.getName();
            if (name == null) {
                name = "";
            }
            output.println(SP6 + "metaClass.setSuperMetaClass(" + name + ".metaClass);");
        }

        String strValue = "";
        DbEnumeration dbEnum = adt.getComponents().elements(DbJVDataMember.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVDataMember member = (DbJVDataMember) dbEnum.nextElement();
            DbOOAssociationEnd end = member.getAssociationEnd();
            if (end == null || end.getAggregation().getValue() != OOAggregation.COMPOSITE)
                continue;
            DbJVDataMember oppMember = (DbJVDataMember) end.getOppositeEnd().getAssociationMember();
            if (strValue.length() == 0)
                strValue = SP6 + "metaClass.setComponentMetaClasses(new MetaClass[] {";
            else
                strValue = strValue + ", ";
            strValue = strValue + oppMember.getComposite().getName() + ".metaClass";
        }
        dbEnum.close();
        if (strValue.length() != 0)
            output.println(strValue + "});");

        String iconName = (String) adt.getUDF("icon name");
        if (iconName != null)
            output.println(SP6 + "metaClass.setIcon(\"" + iconName + "\");");
        output.println();
    }

    private void genStaticBlockFields(PrintWriter output, DbJVClass adt) throws DbException,
            IOException {
        DbEnumeration dbEnum = adt.getComponents().elements(DbJVDataMember.metaClass);
        int i = 0;
        while (dbEnum.hasMoreElements()) {
            DbJVDataMember member = (DbJVDataMember) dbEnum.nextElement();
            if (getFieldType(member) != META_NONE) {
                output.print(SP6 + "f" + capitalize(member.getName()) + ".setJField("
                        + adt.getName() + ".class.getDeclaredField(\"m_" + member.getName()
                        + "\"));");
                output.println(getFieldPropertiesStr(member));
                i++;
            }
        }
        dbEnum.close();
        if (i != 0)
            output.println();
    }

    private void genStaticBlockRelations(PrintWriter output, DbJVClass adt) throws DbException,
            IOException {
        DbEnumeration dbEnum = adt.getComponents().elements(DbJVDataMember.metaClass);
        int i = 0;
        while (dbEnum.hasMoreElements()) {
            DbJVDataMember member = (DbJVDataMember) dbEnum.nextElement();
            int type = getRelationType(member);
            if (type != REL_NONE && type != REL_MANY_TO_ONE) {
                output.println(SP6 + getOppositeRelationStr(member));
                i++;
            }
        }
        dbEnum.close();
        if (i != 0)
            output.println();
    }

    private void genMetaStaticBlock(PrintWriter output, DbJVClass adt) throws DbException,
            IOException {
        output.println("/**");
        output.println(SP4 + "For internal use only.");
        output.println(" **/");
        output.println(SP2 + "public static void initMeta() {");
        output.println(SP4 + "try {");
        genClassProperties(output, adt);
        genStaticBlockFields(output, adt);
        genStaticBlockRelations(output, adt);
        output.println(SP4 + "}");
        output.println(SP4 + "catch (Exception e) { throw new RuntimeException(\"Meta init\"); }");
        if (adt.findComponentByName(DbJVMethod.metaClass, INIT_STATIC) != null)
            output.println(SP4 + INIT_STATIC + "();");
        output.println(SP2 + "}");
        output.println();
    }

    private void genStaticFields(PrintWriter output, DbJVClass adt) throws DbException, IOException {
        DbEnumeration dbEnum = adt.getComponents().elements(DbJVDataMember.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVDataMember member = (DbJVDataMember) dbEnum.nextElement();
            if (member.isStatic())
                genFieldAux(output, member);
        }
        dbEnum.close();
    }

    private void genTransientFields(PrintWriter output, DbJVClass adt) throws DbException,
            IOException {
        DbEnumeration dbEnum = adt.getComponents().elements(DbJVDataMember.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVDataMember member = (DbJVDataMember) dbEnum.nextElement();
            if (member.isTransient() && !member.isStatic())
                genFieldAux(output, member);
        }
        dbEnum.close();
    }

    private void genFieldAux(PrintWriter output, DbJVDataMember member) throws DbException,
            IOException {
        output.write(SP2 + getVisibilityStr(member.getVisibility()));
        if (member.isStatic())
            output.write("static ");
        if (member.isFinal())
            output.write("final ");
        if (member.isTransient())
            output.write("transient ");
        output.write(getFieldTypeStr(member) + getString(member, DbOODataMember.fTypeUse) + " "
                + member.getName());
        String initVal = member.getInitialValue();
        if (initVal != null && initVal.length() != 0)
            output.print(" = " + initVal);
        output.println(";");
    }

    private void genStaticBlock(PrintWriter output, DbJVClass adt) throws DbException, IOException {
        DbEnumeration dbEnum = adt.getComponents().elements(DbJVInitBlock.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVInitBlock initBlock = (DbJVInitBlock) dbEnum.nextElement();
            if (initBlock.isStatic()) {
                output.println();
                output.println(initBlock.getBody());
            }
        }
        dbEnum.close();

        DbJVMethod method = (DbJVMethod) adt.findComponentByName(DbJVMethod.metaClass, INIT_STATIC);
        if (method != null) {
            output.println();
            output.println(SP2 + "private void " + INIT_STATIC + "() {");
            output.println(method.getBody());
            output.println(SP2 + "}");
        }
    }

    private void genInstanceVariables(PrintWriter output, DbJVClass adt) throws DbException,
            IOException {
        output.println();
        output.println(SP2 + "//Instance variables");
        DbEnumeration dbEnum = adt.getComponents().elements(DbJVDataMember.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVDataMember member = (DbJVDataMember) dbEnum.nextElement();
            int type = getFieldType(member);
            if (type == META_NONE)
                continue;

            // Check that the field type is not a primitive wrapper
            if (member.getType() != null) {
                String str = member.getType().getName();
                if (str.equals("Boolean") || str.equals("Integer") || str.equals("Long")
                        || str.equals("Float") || str.equals("Double"))
                    throw new RuntimeException("Invalid primitive type wrapper used for field "
                            + member.getSemanticalName(DbObject.LONG_FORM));
            }

            String typeStr = (type == META_RELN ? "DbRelationN" : getFieldTypeStr(member)
                    + getString(member, DbOODataMember.fTypeUse));
            output.println(SP2 + "" + typeStr + " m_" + member.getName() + ";");
        }
        dbEnum.close();
        output.println();
    }

    private void genSetters(PrintWriter output, DbJVClass adt) throws DbException, IOException {
        output.println(SP2 + "//Setters");
        output.println();
        DbEnumeration dbEnum = adt.getComponents().elements(DbJVDataMember.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVDataMember member = (DbJVDataMember) dbEnum.nextElement();
            int type = getFieldType(member);
            if (type == META_NONE)
                continue;
            String capName = capitalize(member.getName());
            if (adt.findComponentByName(DbJVMethod.metaClass, "set" + capName) != null)
                continue;
            if (type == META_RELN) {
                if (getRelationType(member) != REL_MANY_TO_MANY)
                    continue;

                genCommentListSetter(output, member, capName);
                output.println(SP2 + "public final void set" + capName + "("
                        + getFieldTypeStr(member) + " value, int op) throws DbException {");
                output.println(SP4 + "setRelationNN(f" + capName + ", value, op);");
                output.println(SP2 + "}");
                output.println();

                genAddToSetter(output, member, capName);
                genRemoveFromSetter(output, member, capName);
            } else {
                genSingleValueSetter(output, member, capName);
            }
        }
        dbEnum.close();

        //do not generate duplicated generic set(MetaField, Object)
        boolean genericSetterExist = findGenericSetter(adt);
        if (!genericSetterExist) {
            genGlobalSetter(output, adt);
        }

        genGlobalSetterN(output, adt);
        genGlobalReinsert(output, adt);
    }

    private boolean findGenericSetter(DbJVClass adt) throws DbException {
        boolean found = false;

        DbEnumeration dbEnum = adt.getComponents().elements(DbJVMethod.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVMethod method = (DbJVMethod) dbEnum.nextElement();
            String name = method.getName();
            if (name.endsWith("set")) {
                found = isGenericSetterParameters(method);
            } //end if

            if (found) {
                break;
            } //end if
        } //end while
        dbEnum.close();

        return found;
    } //end findGenericSetter()

    private boolean isGenericSetterParameters(DbJVMethod method) throws DbException {
        boolean param1 = false;
        boolean param2 = false;
        int paramIdx = 0;

        DbEnumeration dbEnum = method.getComponents().elements(DbJVParameter.metaClass);
        while (dbEnum.hasMoreElements()) {
            paramIdx++;
            DbJVParameter param = (DbJVParameter) dbEnum.nextElement();
            DbOOAdt type = param.getType();
            String name = type.getName();

            if ((paramIdx == 1) && name.equals("MetaField")) {
                param1 = true;
            }

            if ((paramIdx == 2) && name.equals("Object")) {
                param2 = true;
            }
        } //end while
        dbEnum.close();

        boolean genericSetter = param1 && param2;
        return genericSetter;
    } //end isGenericSetterParameters()

    private void genAddToSetter(PrintWriter output, DbJVDataMember member, String capName)
            throws DbException {
        genCommentAddToSetter(output, member);
        output.println(SP2 + "public final void addTo" + capName + "(" + getFieldTypeStr(member)
                + " value) throws DbException {");
        output.println(SP4 + "set" + capName + "(value, Db.ADD_TO_RELN);");
        output.println(SP2 + "}");
        output.println();
    }

    private void genRemoveFromSetter(PrintWriter output, DbJVDataMember member, String capName)
            throws DbException {
        genCommentRemoveFromSetter(output, member);
        output.println(SP2 + "public final void removeFrom" + capName + "("
                + getFieldTypeStr(member) + " value) throws DbException {");
        output.println(SP4 + "set" + capName + "(value, Db.REMOVE_FROM_RELN);");
        output.println(SP2 + "}");
        output.println();
    }

    private void genSingleValueSetter(PrintWriter output, DbJVDataMember member, String capName)
            throws DbException {
        genCommentSetter(output, member);
        output.println(SP2 + "public final void set" + capName + "(" + getAPITypeNameStr(member)
                + getString(member, DbOODataMember.fTypeUse) + " value) throws DbException {");
        output.println(SP4 + "basicSet(f" + capName + ", value);");
        output.println(SP2 + "}");
        output.println();
    }

    private void genGlobalSetter(PrintWriter output, DbJVClass adt) throws DbException, IOException {
        output.println("/**");
        output.println(SP4);
        output.println(" **/");
        output.println(SP2
                + "public void set(MetaField metaField, Object value) throws DbException {");
        output.println(SP4 + "if (metaField.getMetaClass() == metaClass) {");
        DbEnumeration dbEnum = adt.getComponents().elements(DbJVDataMember.metaClass);
        int i = 0;
        while (dbEnum.hasMoreElements()) {
            DbJVDataMember member = (DbJVDataMember) dbEnum.nextElement();
            int type = getFieldType(member);
            if (type == META_NONE)
                continue;
            String capName = capitalize(member.getName());
            if (type == META_RELN
                    || adt.findComponentByName(DbJVMethod.metaClass, "set" + capName) != null) {
                output
                        .println(SP6 + (i != 0 ? "else " : "") + "if (metaField == f" + capName
                                + ")");
                if (type == META_RELN) {
                    if (getRelationType(member) == REL_MANY_TO_MANY) {
                        output.println(SP8 + "set" + capName + "((" + getFieldTypeStr(member)
                                + ")value, Db.ADD_TO_RELN);");
                    } else {
                        DbJVDataMember oppMember = getOppositeDataMember(member);
                        output.println(SP8 + "((" + getFieldTypeStr(member) + ")value).set"
                                + capitalize(oppMember.getName()) + "(this);");
                    }
                } else {
                    output.println(SP8 + "set" + capName + "((" + getAPITypeNameStr(member)
                            + getString(member, DbOODataMember.fTypeUse) + ")value);");
                }
                i++;
            }
        }
        dbEnum.close();

        output.println(SP6 + (i != 0 ? "else " : "") + "basicSet(metaField, value);");
        output.println(SP4 + "}");
        output.println(SP4 + "else super.set(metaField, value);");
        output.println(SP2 + "}");
        output.println();
    }

    private void genGlobalSetterN(PrintWriter output, DbJVClass adt) throws DbException,
            IOException {
        output.println("/**");
        output.println(SP4);
        output.println(" **/");
        output
                .println(SP2
                        + "public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {");
        DbEnumeration dbEnum = adt.getComponents().elements(DbJVDataMember.metaClass);
        int i = 0;
        while (dbEnum.hasMoreElements()) {
            DbJVDataMember member = (DbJVDataMember) dbEnum.nextElement();
            if (getRelationType(member) != REL_MANY_TO_MANY)
                continue;
            String capName = capitalize(member.getName());
            output.println(SP4 + (i != 0 ? "else " : "") + "if (relation == f" + capName + ")");
            output.println(SP6 + "set" + capName + "((" + getFieldTypeStr(member)
                    + ")neighbor, op);");
            i++;
        }
        dbEnum.close();
        output.println(SP4 + (i != 0 ? "else " : "") + "super.set(relation, neighbor, op);");
        output.println(SP2 + "}");
        output.println();
    }

    private void genGlobalReinsert(PrintWriter output, DbJVClass adt) throws DbException,
            IOException {
        DbEnumeration dbEnum = adt.getComponents().elements(DbJVDataMember.metaClass);
        int i = 0;
        while (dbEnum.hasMoreElements()) {
            DbJVDataMember member = (DbJVDataMember) dbEnum.nextElement();
            int type = getFieldType(member);
            if (type != META_RELN)
                continue;
            String capName = capitalize(member.getName());
            if (adt.findComponentByName(DbJVMethod.metaClass, "reinsertIn" + capName) == null)
                continue;
            if (i == 0) {
                output
                        .println(SP2
                                + "public void reinsert(MetaRelationN relation, int oldIndex, int newIndex) throws DbException {");
                output.print(SP4);
            } else
                output.print(SP4 + "else ");
            output.println("if (relation == f" + capName + ")");
            output.println(SP6 + "reinsertIn" + capName + "(oldIndex, newIndex);");
            i++;
        }
        dbEnum.close();
        if (i != 0) {
            output.println(SP4 + "else super.reinsert(relation, oldIndex, newIndex);");
            output.println(SP2 + "}");
            output.println();
        }
    }

    private void genGetters(PrintWriter output, DbJVClass adt) throws DbException, IOException {
        output.println(SP2 + "//Getters");
        output.println();
        DbEnumeration dbEnum = adt.getComponents().elements(DbJVDataMember.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVDataMember member = (DbJVDataMember) dbEnum.nextElement();
            genGetter(output, member);
        }
        dbEnum.close();

        if (!adt.isAbstract()) {
            output.println(SP2 + "public MetaClass getMetaClass() {");
            output.println(SP4 + "return metaClass;");
            output.println(SP2 + "}");
            output.println();
        } //end if
    } //end genGetters()

    private void genGetter(PrintWriter output, DbJVDataMember member) throws DbException {
        String capName = capitalize(member.getName());
        int type = getFieldType(member);
        if (type == META_NONE) {
            return;
        }

        boolean isBooleanGetter = getFieldTypeStr(member).equals("boolean");
        boolean isMultiValued = (type == META_RELN);
        if (!isBooleanGetter) {
            if (isMultiValued) {
                genCommentMultiValuedGetter(output, member);
            } else {
                genCommentSingleValuedGetter(output, member);
            }
        } else {
            genCommentBooleanGetter(output, member, capName);
        }

        String typeStr = (isMultiValued ? "DbRelationN" : getAPITypeNameStr(member)
                + getString(member, DbOODataMember.fTypeUse));
        output.println(SP2 + "public final " + typeStr + " get" + capName
                + "() throws DbException {");
        output.println(SP4 + "return (" + typeStr + ")get(f" + capName + ");");
        output.println(SP2 + "}");
        output.println();

        if (isBooleanGetter) {
            genCommentIsGetter(output, member);
            output.println(SP2 + "public final boolean is" + capName + "() throws DbException {");
            output.println(SP4 + "return get" + capName + "().booleanValue();");
            output.println(SP2 + "}");
            output.println();
        }
    }

    // ***
    // OPERATION-BASED METHODS
    // ***
    private void genConstructors(PrintWriter output, DbJVClass adt) throws DbException, IOException {
        output.println(SP2 + "//Constructors");
        output.println();
        output.println("/**");
        output.println(SP4 + "Parameter-less constructor.  Required by Java Beans Conventions.");
        output.println(" **/");
        output.println(SP2 + "public " + adt.getName() + "() {}"); // the null constructor
        output.println();

        DbEnumeration dbEnum = adt.getComponents().elements(DbJVConstructor.metaClass);
        int i;
        for (i = 0; dbEnum.hasMoreElements(); i++) {
            genOperationAux(output, (DbJVConstructor) dbEnum.nextElement());
        }
        dbEnum.close();

        if (i == 0) { // if no explicit constructors, generate the default one
            output.println("/**");
            String pattern = "Creates an instance of {0}.";
            String title = MessageFormat.format(pattern, new Object[] { adt.getName() });
            output.println(SP4 + title);
            output.println(SP4
                    + "@param composite the object which will contain the newly-created instance");
            output.println(" **/");
            output.println(SP2 + "public " + adt.getName()
                    + "(DbObject composite) throws DbException {");
            output.println(SP4 + "super(composite);");
            output.println(SP4 + SET_DEFAULT_INITIAL_VALUES + "();");
            output.println(SP2 + "}");
            output.println();
        }

        output.println(SP2 + "private void " + SET_DEFAULT_INITIAL_VALUES
                + "() throws DbException {");
        dbEnum = adt.getComponents().elements(DbJVDataMember.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVDataMember member = (DbJVDataMember) dbEnum.nextElement();
            if (getFieldType(member) == META_NONE)
                continue;
            String initVal = member.getInitialValue();
            if (initVal != null && initVal.length() != 0)
                output.println(SP4 + "set" + capitalize(member.getName()) + "(" + initVal + ");");
        }
        dbEnum.close();
        DbJVMethod method = (DbJVMethod) adt.findComponentByName(DbJVMethod.metaClass,
                SET_DEFAULT_INITIAL_VALUES);
        if (method != null)
            output.println(method.getBody());
        output.println(SP2 + "}");
        output.println();
    }

    private void genMethods(PrintWriter output, DbJVClass adt) throws DbException, IOException {
        DbEnumeration dbEnum = adt.getComponents().elements(DbJVMethod.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVMethod method = (DbJVMethod) dbEnum.nextElement();
            String name = method.getName();
            if (name.equals(SET_DEFAULT_INITIAL_VALUES) || name.equals(INIT_STATIC))
                continue;
            genOperationAux(output, method);
        }
        dbEnum.close();
    }

    private void genOperationAux(PrintWriter output, DbOOAbstractMethod oper) throws DbException,
            IOException {
        genCommentMethod(output, oper);
        output.write(SP2 + getVisibilityStr(oper.getVisibility()));
        if (oper instanceof DbJVMethod) {
            DbJVMethod method = (DbJVMethod) oper;
            if (method.isStatic())
                output.write("static ");
            if (method.isFinal())
                output.write("final ");
            if (method.isAbstract())
                output.write("abstract ");
            output.write(getTypeStr(method, DbOOMethod.fReturnType)
                    + getString(method, DbOOMethod.fTypeUse) + " ");
        }
        output.write(oper.getName() + "(");
        DbEnumeration dbEnum = oper.getComponents().elements(DbJVParameter.metaClass);
        int i;
        for (i = 0; dbEnum.hasMoreElements(); i++) {
            DbJVParameter param = (DbJVParameter) dbEnum.nextElement();
            if (i != 0)
                output.write(", ");
            if (param.isFinal())
                output.write("final ");
            output.write(getTypeStr(param, DbOOParameter.fType)
                    + getString(param, DbOOParameter.fTypeUse) + " " + param.getName());
        }
        dbEnum.close();
        output.write(")");

        dbEnum = (oper instanceof DbJVMethod ? ((DbJVMethod) oper).getJavaExceptions().elements()
                : ((DbJVConstructor) oper).getJavaExceptions().elements());
        for (i = 0; dbEnum.hasMoreElements(); i++) {
            DbJVClass exc = (DbJVClass) dbEnum.nextElement();
            output.write((i == 0 ? " throws " : ", ") + exc.getName());
        }
        dbEnum.close();

        if (oper instanceof DbJVMethod
                && (((DbJVMethod) oper).isAbstract() || ((DbJVClass) oper.getComposite())
                        .isInterface())) {
            output.println(";");
            output.println();
        } else {
            String body = oper.getBody();
            if (body != null && body.length() != 0) {
                output.println(" {");
                output.println(body);
                output.println(SP2 + "}");
                output.println();
            } else {
                output.println(" {}");
                output.println();
            }
        }
    }

    // *********************************************************************
    // COMMENTS
    // *********************************************************************
    private static final boolean SINGULAR = true;
    private static final boolean PLURAL = false;

    private String getDisplayName(DbSemanticalObject obj, boolean number) throws DbException {
        String displayName;

        String alias = obj.getAlias();
        if (alias != null) {
            int separator = alias.indexOf(';');
            if (separator == -1) {
                displayName = alias;
                if ((number == PLURAL) && (displayName.charAt(displayName.length() - 1)) != 's') {
                    displayName += "s";
                }
            } else {
                displayName = (number == SINGULAR) ? alias.substring(0, separator) : alias
                        .substring(separator - 1);
            } //end if
        } else {
            displayName = obj.getName();
            if (number == PLURAL) {
                displayName += " objects";
            }
        } //end if

        displayName = displayName.toLowerCase();
        return displayName;
    } //end getDisplayName()

    private String getQualifiedName(DbOOAdt adt) throws DbException {
        String qualifiedName = "";
        boolean root_level = false;
        DbSemanticalObject current = adt;

        do {
            qualifiedName = current.getName() + qualifiedName;
            DbSemanticalObject composite = (DbSemanticalObject) current.getComposite();
            if ((composite == null)
                    || (!(composite instanceof DbJVPackage) && !(composite instanceof DbJVClass)))
                root_level = true;
            else {
                qualifiedName = "." + qualifiedName;
                current = composite;
            }
        } while (!root_level);

        return qualifiedName;
    }

    private int getPackageDepth(DbJVPackage pack) throws DbException {
        int depth = 0;

        DbObject composite = pack;
        do {
            composite = composite.getComposite();
            depth++;
        } while (composite instanceof DbJVPackage);

        return depth;
    }

    //generate : <A HREF="../../../../../../org/modelsphere/sms/oo/db/DbOOClass.html">org.modelsphere.sms.oo.db.DbOOClass</A>
    private String getClassName(DbOOAdt adt, DbJVPackage pack) throws DbException {
        String className;
        if (pack != null) {
            int depth = getPackageDepth(pack);
            String location = "";
            for (int i = 0; i < depth; i++) {
                location += "../";
            }

            String qualifiedName = getQualifiedName(adt);
            String pathName = qualifiedName.replace('.', '/');
            String pattern = "<A HREF=\"{0}.html\">{1}</A>";
            String baseName = qualifiedName.substring(1 + qualifiedName.lastIndexOf('.'));
            className = MessageFormat.format(pattern,
                    new Object[] { location + pathName, baseName });
        } else {
            className = getQualifiedName(adt);
        }

        return className;
    }

    // ***
    // CLASSIFIER-BASED COMMENTS
    // ***
    private static final int COMPOSITE = 1;
    private static final int COMPONENT = 2;

    private void displaySubClasses(PrintWriter output, DbJVClass claz) throws DbException {
        output.print("<b>Direct subclass(es)/subinterface(s) : </b>");
        boolean none = true;
        DbJVPackage pack = (DbJVPackage) claz.getCompositeOfType(DbJVPackage.metaClass);
        DbRelationN relationN = claz.getSubInheritances();
        DbEnumeration dbEnum = relationN.elements();
        while (dbEnum.hasMoreElements()) {
            DbOOInheritance inher = (DbOOInheritance) dbEnum.nextElement();
            DbJVClass subclass = (DbJVClass) inher.getSubClass();

            if (!none) {
                output.print(", ");
            }
            none = false;
            String className = getClassName(subclass, pack);
            output.print(className);

        } //end while
        dbEnum.close();
        if (none) {
            output.print("none");
        }
        output.println(".<br>");

    } //end displaySubClasses()

    private void findAssociations(DbJVClass claz, ArrayList assocList) throws DbException {
        DbRelationN relationN = claz.getComponents();
        DbEnumeration dbEnum = relationN.elements(DbJVDataMember.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVDataMember field = (DbJVDataMember) dbEnum.nextElement();
            DbOOAssociationEnd assocEnd = field.getAssociationEnd();
            if (assocEnd != null) {
                DbOOAssociationEnd oppAssocEnd = assocEnd.getOppositeEnd();
                OOAggregation aggr = assocEnd.getAggregation();
                if (aggr != null) {
                    int value = aggr.getValue();
                    if (value == OOAggregation.COMPOSITE) {
                        DbOODataMember oppField = oppAssocEnd.getAssociationMember();
                        DbJVClass associatedClass = (DbJVClass) oppField.getComposite();
                        AssociationStructure struct = new AssociationStructure(associatedClass,
                                COMPONENT);
                        assocList.add(struct);
                    }
                } //end if

                aggr = oppAssocEnd.getAggregation();
                if (aggr != null) {
                    int value = aggr.getValue();
                    if (value == OOAggregation.COMPOSITE) {
                        DbOODataMember oppField = oppAssocEnd.getAssociationMember();
                        DbJVClass associatedClass = (DbJVClass) oppField.getComposite();
                        AssociationStructure struct = new AssociationStructure(associatedClass,
                                COMPOSITE);
                        assocList.add(struct);
                    } //end if
                } //end if
            } //end if
        } //end while
        dbEnum.close();

        relationN = claz.getSuperInheritances();
        dbEnum = relationN.elements(DbOOInheritance.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbOOInheritance inheritance = (DbOOInheritance) dbEnum.nextElement();
            DbOOClass superAdt = inheritance.getSuperClass();
            if (superAdt instanceof DbJVClass) {
                DbJVClass superClaz = (DbJVClass) superAdt;
                findAssociations(superClaz, assocList);
            }
        } //end while
        dbEnum.close();
    } //end findAssociations()

    private void displayAssociations(PrintWriter output, ArrayList assocList, DbJVPackage pack)
            throws DbException {
        int nb = assocList.size();

        output.println();
        output.print(SP4 + "<b>Composites : </b>");
        boolean none = true;
        for (int i = 0; i < nb; i++) {
            AssociationStructure struct = (AssociationStructure) assocList.get(i);
            if (struct.m_kindOf == COMPOSITE) {
                if (!none) {
                    output.print(", ");
                }

                none = false;
                DbJVClass claz = struct.m_associatedClass;
                String className = getClassName(claz, pack);
                output.print(className);
            }
        } //end for
        if (none) {
            output.print("none");
        }
        output.println(".<br>");

        output.print(SP4 + "<b>Components : </b>");
        none = true;
        for (int i = 0; i < nb; i++) {
            AssociationStructure struct = (AssociationStructure) assocList.get(i);
            if (struct.m_kindOf == COMPONENT) {
                if (!none) {
                    output.print(", ");
                }

                none = false;
                DbJVClass claz = struct.m_associatedClass;
                String className = getClassName(claz, pack);
                output.print(className);
            }
        } //end for
        if (none) {
            output.print("none");
        }
        output.println(".<br>");

    } //end displayAssociations

    private void genCommentClassifier(PrintWriter output, DbJVClass claz) throws DbException {
        output.println("/**");
        String description = claz.getDescription();
        if ((description != null) && (description.length() != 0)) {
            output.println(SP4 + description);
        }
        ArrayList assocList = new ArrayList();
        displaySubClasses(output, claz);
        assocList.clear();
        findAssociations(claz, assocList);
        DbJVPackage pack = (DbJVPackage) claz.getCompositeOfType(DbJVPackage.metaClass);
        displayAssociations(output, assocList, pack);

        output.println(" **/");
    }

    // ***
    // METHOD-BASED COMMENTS
    // ***

    private void genCommentMethod(PrintWriter output, DbOOAbstractMethod oper) throws DbException {
        output.println("/**");
        String operType = "Operation";
        if (oper instanceof DbJVMethod) {
            operType = "Method";
        } else if (oper instanceof DbJVConstructor) {
            operType = "Constructor";
            DbOOAdt adt = (DbOOAdt) oper.getComposite();
            String pattern = "Creates an instance of {0}.";
            String title = MessageFormat.format(pattern, new Object[] { adt.getName() });
            output.println(SP4 + title);
        }

        String pattern = "{0} {1}.";
        String title = MessageFormat.format(pattern, new Object[] { operType, oper.getName() });
        //output.println(SP4 + title);
        String descr = oper.getDescription();
        if (descr != null && descr.length() != 0) {
            output.println(SP4 + descr);
        }
        output.println();

        //Write parameters' comment
        DbRelationN relN = oper.getComponents();
        DbEnumeration dbEnum = relN.elements(DbJVParameter.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVParameter param = (DbJVParameter) dbEnum.nextElement();
            output.print(SP4 + "@param " + getDisplayName(param, SINGULAR));
            descr = param.getDescription();
            if (descr != null && descr.length() != 0) {
                output.print(" " + descr);
            } else {
                DbOOAdt type = param.getType();
                String className = getClassName(type, null);
                output.print(" " + className);
            }
            output.println();
        } //end while
        dbEnum.close();

        if (oper instanceof DbJVMethod) {
            DbJVMethod method = (DbJVMethod) oper;
            DbOOAdt type = method.getReturnType();
            if (type != null) {
                output.println(SP4 + "@return " + getDisplayName(type, SINGULAR));
            }
        }

        output.println(" **/");
    }

    private void genCommentListSetter(PrintWriter output, DbJVDataMember member, String capName)
            throws DbException {
        DbJVClass claz = (DbJVClass) member.getComposite();
        String memberName = getDisplayName(member, PLURAL);
        output.println("/**");
        //String pattern = "Adds or removes an element to the list of {0} associated to a {1}{2}s instance.";
        String pattern = "Adds an element to or removes an element from the list of {0} associated to a {1}{2}s instance.";
        String title = MessageFormat.format(pattern,
                new Object[] { memberName, claz.getName(), "'" });
        output.println(SP4 + title);
        String description = member.getDescription();
        if ((description != null) && (description.length() != 0)) {
            output.println(SP4 + description);
        }
        output.println();
        output.println(SP4 + "@param value an element to be added to or removed from the list.");
        output.println(SP4 + "@param op Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN");
        //pattern = "@deprecated use addTo{0}() or removeFrom{0}() method instead.";
        //title = MessageFormat.format(pattern, new Object[] {capName});
        //output.println(SP4 + title);
        output.println(" **/");
    }

    private void genCommentAddToSetter(PrintWriter output, DbJVDataMember member)
            throws DbException {
        DbJVClass claz = (DbJVClass) member.getComposite();
        String memberName = getDisplayName(member, PLURAL);
        output.println("/**");
        String pattern = "Adds an element to the list of {0} associated to a {1}{2}s instance.";
        String title = MessageFormat.format(pattern,
                new Object[] { memberName, claz.getName(), "'" });
        output.println(SP4 + title);
        String description = member.getDescription();
        if ((description != null) && (description.length() != 0)) {
            output.println(SP4 + description);
        }
        output.println();
        output.println(SP4 + "@param value the element to be added.");
        output.println(" **/");
    }

    private void genCommentRemoveFromSetter(PrintWriter output, DbJVDataMember member)
            throws DbException {
        DbJVClass claz = (DbJVClass) member.getComposite();
        String memberName = getDisplayName(member, PLURAL);
        output.println("/**");
        String pattern = "Removes an element from the list of {0} associated to a {1}{2}s instance.";
        String title = MessageFormat.format(pattern,
                new Object[] { memberName, claz.getName(), "'" });
        output.println(SP4 + title);
        String description = member.getDescription();
        if ((description != null) && (description.length() != 0)) {
            output.println(SP4 + description);
        }
        output.println();
        output.println(SP4 + "@param value the element to be removed.");
        output.println(" **/");
    }

    private void genCommentSetter(PrintWriter output, DbJVDataMember member) throws DbException {
        int kindOf = getKindOf(member);
        DbJVClass claz = (DbJVClass) member.getComposite();
        output.println("/**");
        String titlePattern;
        String paramPattern;
        switch (kindOf) {
        case BOOLEAN:
            titlePattern = "Sets whether a {1}{2}s instance is {0} or not.";
            paramPattern = "@param value either Boolean.TRUE or Boolean.FALSE";
            break;
        case PRIMITIVE:
            titlePattern = "Sets the \"{0}\" property of a {1}{2}s instance.";
            paramPattern = "@param value the \"{0}\" property";
            break;
        case OBJECT:
            titlePattern = "Sets the \"{0}\" property of a {1}{2}s instance.";
            paramPattern = "@param value the \"{0}\" property";
            break;
        case DB_OBJECT:
        default:
            titlePattern = "Sets the {0} object associated to a {1}{2}s instance.";
            paramPattern = "@param value the {0} object to be associated";
            break;
        } //end switch

        String memberName = getDisplayName(member, SINGULAR);
        String title = MessageFormat.format(titlePattern, new Object[] { memberName,
                claz.getName(), "'" });
        String param = MessageFormat.format(paramPattern, new Object[] { memberName });
        output.println(SP4 + title);
        String description = member.getDescription();
        if ((description != null) && (description.length() != 0)) {
            output.println(SP4 + description);
        }
        output.println();
        output.println(SP4 + param);
        output.println();
        output.println(" **/");
    }

    private void genCommentIsGetter(PrintWriter output, DbJVDataMember member) throws DbException {
        DbJVClass claz = (DbJVClass) member.getComposite();
        output.println("/**");
        String pattern = "Tells whether a {0}{1}s instance is {2} or not.";
        String title = MessageFormat.format(pattern, new Object[] { claz.getName(), "'",
                member.getName() });
        output.println(SP4 + title);
        String description = member.getDescription();
        if ((description != null) && (description.length() != 0)) {
            output.println(SP4 + description);
        }
        output.println();
        output.println(SP4 + "@return boolean");
        output.println();
        output.println(" **/");
    }

    private static final int BOOLEAN = 0;
    private static final int PRIMITIVE = 1;
    private static final int DB_OBJECT = 2;
    private static final int OBJECT = 3;

    private int getKindOf(DbJVDataMember member) throws DbException {
        int kindOf = OBJECT;
        DbOOAdt type = member.getType();
        if (type != null) {
            String strValue = type.getName();
            if (type instanceof DbJVPrimitiveType) {
                kindOf = PRIMITIVE;
            } else if (strValue.startsWith("Db")) {
                kindOf = DB_OBJECT;
            } else if (strValue.equals("String")) {
                kindOf = PRIMITIVE;
            } else if (strValue.equals("boolean")) {
                kindOf = BOOLEAN;
            }
        } //end if
        return kindOf;
    }

    private void genCommentBooleanGetter(PrintWriter output, DbJVDataMember member, String capName)
            throws DbException {
        int kindOf = getKindOf(member);
        DbJVClass claz = (DbJVClass) member.getComposite();
        output.println("/**");
        String titlePattern = "Gets the \"{0}\" property{1}s Boolean value of a {2}{3}s instance.";
        String valuePattern = "@return the \"{0}\" property{1}s Boolean value";
        String memberName = getDisplayName(member, SINGULAR);
        String title = MessageFormat.format(titlePattern, new Object[] { memberName, "'",
                claz.getName(), "'" });
        String value = MessageFormat.format(valuePattern, new Object[] { memberName, "'" });
        output.println(SP4 + title);
        String description = member.getDescription();
        if ((description != null) && (description.length() != 0)) {
            output.println(SP4 + description);
        }
        output.println();
        output.println(SP4 + value);
        output.println();
        String pattern = "@deprecated use is{0}() method instead";
        String note = MessageFormat.format(pattern, new Object[] { capName });
        output.println(SP4 + note);
        output.println(" **/");
    }

    private void genCommentSingleValuedGetter(PrintWriter output, DbJVDataMember member)
            throws DbException {
        int kindOf = getKindOf(member);
        DbJVClass claz = (DbJVClass) member.getComposite();
        output.println("/**");
        String titlePattern;
        String valuePattern;
        switch (kindOf) {
        case PRIMITIVE:
            titlePattern = "Gets the \"{0}\" property of a {1}{2}s instance.";
            valuePattern = "@return the \"{0}\" property";
            break;
        case OBJECT:
            titlePattern = "Gets the \"{0}\" of a {1}{2}s instance.";
            valuePattern = "@return the \"{0}\"";
            break;
        case DB_OBJECT:
        default:
            titlePattern = "Gets the {0} object associated to a {1}{2}s instance.";
            valuePattern = "@return the {0} object";
            break;
        } //end switch

        String memberName = getDisplayName(member, SINGULAR);
        String title = MessageFormat.format(titlePattern, new Object[] { memberName,
                claz.getName(), "'" });
        String value = MessageFormat.format(valuePattern, new Object[] { memberName });
        output.println(SP4 + title);
        String description = member.getDescription();
        if ((description != null) && (description.length() != 0)) {
            output.println(SP4 + description);
        }
        output.println();
        output.println(SP4 + value);
        output.println();
        output.println(" **/");
    }

    private void genCommentMultiValuedGetter(PrintWriter output, DbJVDataMember member)
            throws DbException {
        DbJVClass claz = (DbJVClass) member.getComposite();
        output.println("/**");
        String memberName = getDisplayName(member, PLURAL);
        String titlePattern = "Gets the list of {0} associated to a {1}{2}s instance.";
        String valuePattern = "@return the list of {0}.";
        String title = MessageFormat.format(titlePattern, new Object[] { memberName,
                claz.getName(), "'" });
        String value = MessageFormat.format(valuePattern, new Object[] { memberName });
        output.println(SP4 + title);
        String description = member.getDescription();
        if ((description != null) && (description.length() != 0)) {
            output.println(SP4 + description);
        }
        output.println();
        output.println(SP4 + value);
        output.println();
        output.println(" **/");
    }

    // ***
    // FIELD-BASED COMMENTS
    // ***
    private void genCommentMetaField(PrintWriter output, DbJVDataMember member) throws DbException {
        String description = member.getDescription();
        if ((description != null) && (description.length() != 0)) {
            output.println("/**");
            output.println(SP4 + description);
            output.println(" **/");
        }
    }

    // ***
    // INNER CLASSES
    // ***
    private static class AssociationStructure {
        DbJVClass m_associatedClass;
        int m_kindOf;

        AssociationStructure(DbJVClass associatedClass, int kindOf) {
            m_associatedClass = associatedClass;
            m_kindOf = kindOf;
        }
    } //end AssociationStructure

    //
    // RULES
    //
    private static class ClassifierRule extends Rule {

        public boolean expand(Writer writer, Serializable obj, RuleOptions options) {
            boolean expanded = true;
            DbJVClass claz = (DbJVClass) obj;
            try {
                DbJVPackage pack = (DbJVPackage) claz.getComposite();
                DbForwardEngineeringPlugin forward = new DbForwardEngineeringPlugin();
                forward.init(pack);
                forward.genClassifier(writer, claz);
            } catch (DbException ex) {
                expanded = false;
            } catch (IOException ex) {
                expanded = false;
            }

            return expanded;
        }
    } //end ClassifierRule

    //
    // LocaleDictionary
    //
    private static class LocaleDictionary {
        private String m_locale; //A ISO language code
        private HashMap m_localeEntries = new HashMap();

        LocaleDictionary(String locale) {
            m_locale = locale;
        } //end LocaleDictionary()
    } //end LocaleDictionary

	@Override
	public OptionGroup getOptionGroup() {
		return null;
	}

	@Override
	public PluginAction getPluginAction() {
		return new PluginDefaultAction(this);
	}

} //end DbForward

