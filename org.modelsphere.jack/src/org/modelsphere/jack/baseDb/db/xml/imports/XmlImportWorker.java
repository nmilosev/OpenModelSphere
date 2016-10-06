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

package org.modelsphere.jack.baseDb.db.xml.imports;

/**
 * <p>Title: Sms</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: Grandite</p>
 * @author unascribed
 * @version 1.0
 */

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.DbServices;
import org.modelsphere.jack.baseDb.db.VersionConverter;
import org.modelsphere.jack.baseDb.db.srtypes.IntDomain;
import org.modelsphere.jack.baseDb.db.srtypes.SrBoolean;
import org.modelsphere.jack.baseDb.db.srtypes.SrType;
import org.modelsphere.jack.baseDb.db.xml.XMLConstants;
import org.modelsphere.jack.baseDb.db.xml.XMLUtilities;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaChoice;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelation1;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.gui.task.GuiController;
import org.modelsphere.jack.gui.task.Worker;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.explorer.Explorer;
import org.modelsphere.jack.srtool.explorer.ExplorerPanel;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlImportWorker extends Worker {
    private Db m_db;
    private String m_filename;
    private DefaultMainFrame m_mainFrame;
    private String m_operationName;

    //Constants
    private static final String ID = "id"; //NOT LOCALIZABLE,  used only in XML file
    private static final String COMPOSITE = "m_composite"; //NOT LOCALIZABLE, used only in XML file
    private static final String MEMBER_PREFIX = "m_"; //NOT LOCALIZABLE, variable name prefix
    private static final String NAME_PROPERTY = "m_name"; //NOT LOCALIZABLE, variable name prefix
    private static final String META_CLASS = "metaClass"; //NOT LOCALIZABLE, variable name prefix
    private static final String IMPORT = LocaleMgr.message.getString("xmlImport");
    private static final String NOT_IMPORT_FILE_PATN = LocaleMgr.message
            .getString("WarningThisFileDoesNotSeem");

    //Each phase with an approximate percentage of job done per phase
    private static final int PARSING_PHASE = 25; //25% of job done when parsing is archieved
    private static final int DBOBJ_CREATION_PHASE = 60; //60% of job done when all the db objects have been created
    private static final int ATTR_SETTING_PHASE = 80; //80% of job done when setting of dbobjects' attrribute is archieved
    private static final int ASSOC_SETTING_PHASE = 90; //90% of job done when setting of dbobjects' associations is archieved

    public XmlImportWorker(Db db, String filename, DefaultMainFrame mainFrame, String operationName) {
        m_db = db;
        m_filename = filename;
        m_mainFrame = mainFrame;
        m_operationName = operationName;
    }

    //
    //THE ENTRY POINT
    //
    protected void runJob() throws Exception {
        Controller controller = getController();
        try {
            //Read the .xml file
            Document document = getDocument(m_filename); //long operation
            boolean inProgress = controller.checkPoint(PARSING_PHASE);
            if (!inProgress) {
                return;
            }

            ArrayList keyList = new ArrayList();
            HashMap elementMap = new HashMap();
            HashMap idMap = new HashMap();
            NodeReader.HeaderStructure struct = new NodeReader.HeaderStructure();
            NodeReader reader = new NodeReader(elementMap, keyList, idMap, struct);
            reader.readNode(document, null);

            if (!isImportFile(struct)) {
                String msg = MessageFormat.format(NOT_IMPORT_FILE_PATN,
                        new Object[] { ApplicationContext.getApplicationName() });
                controller.println(msg);
                controller.incrementWarningsCounter();
            } //end if

            if (m_db == null) {
                //display the element map (for unit testing)
                displayResult(elementMap, keyList, idMap);
            } else {
                //create the elements read in the XML file
                createElements(m_db, elementMap, keyList, idMap, m_mainFrame, struct);
            } //end if

        } catch (IOException ex) {
            controller.println(ex.toString());
            controller.incrementErrorsCounter();
        } catch (SAXException ex) {
            controller.println(ex.toString());
            controller.incrementErrorsCounter();
        } catch (ParserConfigurationException ex) {
            controller.println(ex.toString());
            controller.incrementErrorsCounter();
        } catch (Exception ex) {
            controller.println(ex.toString());
            controller.incrementErrorsCounter();
        } //end try

    } //end runJob()

    protected String getJobTitle() {
        return m_operationName;
    }

    //
    // Private methods
    //
    private boolean isImportFile(NodeReader.HeaderStructure struct) {
        boolean isImportFile = true;
        if (struct.converter == null) {
            isImportFile = false;
        }

        return isImportFile;
    }

    private Document getDocument(String filename) throws ParserConfigurationException,
            SAXException, IOException {
        Document document = null;
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

        File file = new File(filename);
        document = docBuilder.parse(file); //long atomic operation

        return document;
    } //end getDocument()

    //Called by the unit test
    private void displayResult(HashMap elementMap, ArrayList keyList, HashMap idMap) {
        Iterator iter = keyList.iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            ElementStructure struct = (ElementStructure) elementMap.get(key);
            System.out.println(key + "  " + struct);
        } //end while
    } //end displayResult()

    /**
     * @param db
     *            : the db into which objects are created
     * @param elementMap
     *            : a mapping of key-value pair, where key = "org.modelsphere.concept:id" and value
     *            instanceof ElementStructure
     * @param keyList
     *            : an array of keys (format : "org.modelsphere.concept:id") sorted in the same
     *            order as they appear in the XML file.
     * 
     *            NOTES : We assume that the first element is a DBSMSProject. We create all the
     *            objects without links, except composite.
     */
    private void createElements(Db db, HashMap elementMap, ArrayList keyList, HashMap idMap,
            final DefaultMainFrame mainFrame, NodeReader.HeaderStructure headerStruct) {
        GuiController controller = (GuiController) getController();
        int totalSize = elementMap.size(); //used to calculate progression in a progress bar
        HashMap dbobjMap = new HashMap();
        boolean doAbort = false;
        Explorer explorer = mainFrame.getExplorerPanel().getExplorer();
        explorer.hideContent();

        //PASS 2 : create dbObjects by calling their parameterless constructors
        try {
            db.beginWriteTrans(m_operationName);
            Iterator iter = keyList.iterator();
            int nbObjs = 0;
            while (iter.hasNext()) {
                String key = (String) iter.next();
                ElementStructure struct = (ElementStructure) elementMap.get(key);
                DbObject composite;
                if (struct.m_compositeId == null) {
                    //get db root (used as composite of DbSMSProject's constructor)
                    composite = db.getRoot();
                } else {
                    //get the dbObject previously added in the hashmap
                    composite = (DbObject) dbobjMap.get(struct.m_compositeId);
                } //end if

                try {
                    if (composite != null) { //if found
                        String idName = getClassName(key);
                        String className = (String) idMap.get(idName);

                        Class claz = Class.forName(className);
                        DbObject newObject = null;
                        if (DbProject.class.isAssignableFrom(claz)) {
                            //  newObject = project; //do not re-create current project (TODO: import into existing project)
                        }

                        if (newObject == null) {
                            newObject = createNewInstance(claz, composite, struct, dbobjMap);
                        } //end if

                        //add newly created object into the hashmap for furthur use (object referred by its unique key)
                        dbobjMap.put(key, newObject);
                        nbObjs++;
                    } //end if

                    //Update progress bar once at each 64 iterations
                    if ((nbObjs % 64) == 0) {
                        float jobDone = PARSING_PHASE + (DBOBJ_CREATION_PHASE - PARSING_PHASE)
                                * ((float) nbObjs / (float) totalSize);
                        boolean inProgress = getController().checkPoint((int) jobDone);
                        if (!inProgress)
                            break;
                    } //end if
                } catch (ClassNotFoundException ex) {
                    controller.println(ex.toString());
                    controller.incrementErrorsCounter();
                } catch (NoSuchMethodException ex) {
                    controller.println(ex.toString());
                    controller.incrementErrorsCounter();
                } catch (InstantiationException ex) {
                    controller.println(ex.toString());
                    controller.incrementErrorsCounter();
                } catch (IllegalAccessException ex) {
                    controller.println(ex.toString());
                    controller.incrementErrorsCounter();
                } catch (InvocationTargetException ex) {
                    controller.println(ex.toString());
                    controller.incrementErrorsCounter();
                } //end try
            } //end while

            boolean inProgress = getController().checkPoint(DBOBJ_CREATION_PHASE);
            if (!inProgress) {
                doAbort = true;
            }

            //PASS 3 : set fields & composite (but not other associations)
            DbProject project = null;
            if (!doAbort) {
                nbObjs = 0;
                iter = keyList.iterator();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    ElementStructure struct = (ElementStructure) elementMap.get(key);
                    DbObject obj = (DbObject) dbobjMap.get(key);
                    if (obj instanceof DbProject) {
                        project = (DbProject) obj;
                    }

                    setFields(db, project, obj, struct, dbobjMap);
                    nbObjs++;

                    //Update progress bar once at each 64 iterations
                    if ((nbObjs % 64) == 0) {
                        float jobDone = DBOBJ_CREATION_PHASE
                                + (ATTR_SETTING_PHASE - DBOBJ_CREATION_PHASE)
                                * ((float) nbObjs / (float) totalSize);
                        inProgress = getController().checkPoint((int) jobDone);
                        if (!inProgress)
                            break;
                    }
                } //end while

                inProgress = getController().checkPoint(ATTR_SETTING_PHASE);
                if (!inProgress) {
                    doAbort = true;
                } //end if
            } //end if

            //PASS 4 : set associations
            if (!doAbort) {
                nbObjs = 0;
                iter = keyList.iterator();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    ElementStructure struct = (ElementStructure) elementMap.get(key);
                    DbObject obj = (DbObject) dbobjMap.get(key);
                    setAssociations(db, obj, struct, dbobjMap);

                    //Update progress bar once at each 64 iterations
                    nbObjs++;
                    if ((nbObjs % 64) == 0) {
                        float jobDone = ATTR_SETTING_PHASE
                                + (ASSOC_SETTING_PHASE - ATTR_SETTING_PHASE)
                                * ((float) nbObjs / (float) totalSize);
                        inProgress = getController().checkPoint((int) jobDone);
                        if (!inProgress)
                            break;
                    } //end uf
                } //end while

                //Version conversion
                if (project != null) {
                    try {
                        int version = Integer.parseInt(headerStruct.version);
                        Class converterClass = Class.forName(headerStruct.converter);
                        VersionConverter converter = (VersionConverter) converterClass
                                .newInstance();
                        converter.setOldVersion(version);
                        converter.convertAfterLoad(project);
                    } catch (Exception ex) {
                        getController().println(ex.toString());
                    }
                } //end if

                inProgress = getController().checkPoint(ASSOC_SETTING_PHASE);
                if (!inProgress) {
                    doAbort = true;
                } //end if
            } //end if

            if (doAbort) {
                db.abortTrans();
            } else {
                String IMPORT_PATTERN = "{0} {1} {2}.";
                String msg = MessageFormat.format(IMPORT_PATTERN, new Object[] { IMPORT,
                        new Integer(nbObjs), DbSemanticalObject.metaClass.getGUIName(true) });
                getController().println(msg);
                db.commitTrans();
            } //end if

        } catch (RuntimeException ex) {
            printStackTrace(controller, ex);
            db.abortTrans();
        } catch (DbException ex) {
            //do nothing
        } //end try

        //Update explorer panel on window disposal
        explorer.showContent();
        controller.invokeOnDispose(new Runnable() {
            public void run() {
                ExplorerPanel panel = mainFrame.getExplorerPanel();
                Explorer explorer = panel.getExplorer();
                try {
                    explorer.reloadAll();
                } catch (DbException ex) {
                    //do nothing
                }
            }
        });
    } //end createElements()

    private void printStackTrace(Controller controller, Exception ex) {
        StringWriter buffer = new StringWriter();
        PrintWriter writer = new PrintWriter(buffer);
        ex.printStackTrace(writer);
        controller.print(buffer.toString());
    } //end printStackTrace

    private DbObject createNewInstance(Class claz, DbObject composite, ElementStructure struct,
            HashMap dbobjMap) throws ClassNotFoundException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException, DbException {
        DbObject newInstance = null;

        //USE PARAMETERLESS CONSTRUCTOR
        Constructor constr = claz.getConstructor(new Class[] {});
        newInstance = (DbObject) constr.newInstance(new Object[] {});

        return newInstance;
    } //end createNewInstance()

    //set fields & composite (but not other associations)
    private void setFields(Db db, DbProject project, DbObject obj, ElementStructure struct,
            HashMap dbobjMap) throws DbException {
        DbServices services = DbServices.getSingleton();
        db.fetch(obj);
        services.setDbOfObject(obj, db);

        DbObject composite = (DbObject) dbobjMap.get(struct.m_compositeId);
        if (composite == null) {
            composite = db.getRoot();
        }

        MetaClass mc = obj.getMetaClass();
        if (mc.compositeIsAllowed(composite.getMetaClass())) {
            services.setTransStatus(obj, Db.OBJ_ADDED);
            obj.setComposite(composite);
            services.setProject(obj, project);
        } //end if

        for (int i = 0; i < struct.m_props.length; i++) {
            ElementStructure.Property prop = struct.m_props[i];
            if (prop.m_name.equals(ID)) {
                //skip it
            } else if (prop.m_name.equals(COMPOSITE)) {
                //skip it
            } else {
                String propName = prop.m_name;

                if (!propName.regionMatches(0, MEMBER_PREFIX, 0, 2)) {
                    propName = MEMBER_PREFIX + propName;
                }

                //set meta field (meta-relationship & meta-choice will be processed later
                MetaField mf = mc.getMetaField(propName);
                if (mf != null) {
                    if (!(mf instanceof MetaRelationship)) {
                        Object value = getActualValue(mf, prop.m_value);
                        if (value != null) {
                            services.setValueOfObject(obj, mf, value);
                        }
                    }
                } //end if
            } //end if
        } //end for

        printFeedback(obj);
    } //end setFields()

    private void printFeedback(DbObject obj) throws DbException {
        //Output if it is not a leaf concept
        MetaClass mc = obj.getMetaClass();
        MetaClass[] cmps = mc.getComponentMetaClasses();
        if ((cmps != null) && (cmps.length > 0)) {
            String name = mc.getGUIName() + " : " + obj.getName() + "..";
            getController().println(name);
        } //end if
    }

    private void setAssociations(Db db, DbObject obj, ElementStructure struct, HashMap dbobjMap)
            throws DbException {
        DbServices services = DbServices.getSingleton();
        MetaClass mc = obj.getMetaClass();
        for (int i = 0; i < struct.m_props.length; i++) {
            ElementStructure.Property prop = struct.m_props[i];
            if (prop.m_name.equals(ID)) {
                //skip it
            } else if (prop.m_name.equals(COMPOSITE)) {
                //skip it
            } else {
                String propName = prop.m_name;
                if (!propName.regionMatches(0, MEMBER_PREFIX, 0, 2)) {
                    propName = MEMBER_PREFIX + propName;
                }

                MetaField mf = mc.getMetaField(propName);
                if (mf == null)
                    continue;

                if (mf == DbObject.fComponents || mf == DbObject.fUdfValues)
                    continue;

                if (mf instanceof MetaChoice) { //if it's a choice
                    MetaChoice choice = (MetaChoice) mf;
                    String key = prop.m_value.substring(1); //skip the 1st character (the '#' character)
                    DbObject refObj = (DbObject) dbobjMap.get(key);
                    obj.set(choice, refObj);
                } else if (mf instanceof MetaRelation1) { //if it's an association
                    MetaRelation1 relation = (MetaRelation1) mf;
                    String key = prop.m_value.substring(1); //skip the 1st character (the '#' character)
                    DbObject refObj = (DbObject) dbobjMap.get(key);
                    MetaRelationship oppositeRel = relation.getOppositeRel(refObj);
                    int nbNeighbors = refObj.getNbNeighbors(oppositeRel);
                    if (nbNeighbors < oppositeRel.getMaxCard()) {
                        obj.set(mf, refObj);
                    }

                } else if (mf instanceof MetaRelationN) {
                    MetaRelationN relN = (MetaRelationN) mf;
                    MetaRelationship oppRel = relN.getOppositeRel(null);
                    //skip the 1st character if it's '#'
                    String key = (prop.m_value.charAt(0) == '#') ? prop.m_value.substring(1)
                            : prop.m_value;
                    DbObject refObj = (DbObject) dbobjMap.get(key);

                    if (refObj != null) {
                        if (oppRel instanceof MetaRelationN) {
                            MetaRelationN oppRelN = (MetaRelationN) oppRel;
                            obj.set(relN, refObj, Db.ADD_TO_RELN);
                            refObj.set(oppRelN, obj, Db.ADD_TO_RELN);
                        } //end if
                    } //end if

                    DbRelationN dbRelN = (DbRelationN) services.getValueOfObject(obj, relN);
                    if (dbRelN == null)
                        continue;

                } //end if
            } //end if
        } //end for
    } //end setAssociations()

    private Object getActualValue(MetaField mf, String rawValue) {
        Object actualValue;

        try {
            Field f = mf.getJField();
            Class type = f.getType();
            if (type == String.class) {
                actualValue = rawValue;
            } else if (type == boolean.class) {
                boolean b = rawValue.equals("1") ? true : false;
                actualValue = new Boolean(b);
            } else if (type == int.class) {
                int i = Integer.parseInt(rawValue, 16);
                actualValue = new Integer(i);
            } else if (type == float.class) {
                float fl = Float.parseFloat(rawValue);
                actualValue = new Float(fl);
            } else if (type == double.class) {
                double d = Double.parseDouble(rawValue);
                actualValue = new Double(d);
            } else if (SrType.class.isAssignableFrom(type)) {
                actualValue = getSrActualValue(f, rawValue);
            } else {
                actualValue = null;
            } //end if
        } catch (Exception ex) {
            actualValue = null;
        }

        return actualValue;
    } //end getActualValue()

    private SrType getSrActualValue(Field field, String rawValue) {
        SrType actualValue = null;
        Class srType = field.getType();

        //if has an actual type different from the default one
        if (rawValue.charAt(0) == '(') {
            int idx = rawValue.indexOf(')');
            if (idx != -1) {
                String actualType = rawValue.substring(1, idx);
                try {
                    srType = Class.forName(actualType);
                    rawValue = rawValue.substring(idx + 1);
                } catch (ClassNotFoundException ex) {
                    Controller controller = getController();
                    controller.println(ex.toString());
                    controller.incrementErrorsCounter();
                } //end try
            } //end if
        } //end if

        try {
            Constructor constr = srType.getConstructor(new Class[] {});
            constr.setAccessible(true);
            actualValue = (SrType) constr.newInstance(new Object[] {});
            if (actualValue instanceof SrBoolean) {
                rawValue = normalizeBooleanRawValue(rawValue);
            }
            readTokens(rawValue, srType, actualValue);

        } catch (Exception ex) {
            Controller controller = getController();
            printStackTrace(controller, ex);
            controller.incrementErrorsCounter();
        } //end try

        return actualValue;
    } //end getSrActualValue()

    private String normalizeBooleanRawValue(String rawValue) {
        if ("0".equals(rawValue)) {
            rawValue = "value=0;";
        } else if ("1".equals(rawValue)) {
            rawValue = "value=1;";
        }
        return rawValue;
    }

    private void readTokens(String rawValue, Class srType, SrType actualValue)
            throws IllegalAccessException {
        String lastFieldName = null;

        StringTokenizer st = new XmlImportStringTokenizer(rawValue,
                XMLConstants.ATTR_NAME_VALUE_TERMINATOR);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            int idx = token.indexOf(';');
            if (idx != -1) { //if field has subfields
                int idx2 = token.indexOf('=');
                if (idx2 != -1) {
                    String fieldName = token.substring(0, idx2);
                    String fieldValue = token.substring(idx2 + 1);
                    Field field = findField(srType, fieldName);
                    if (field != null) {
                        int modifiers = field.getModifiers();
                        if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
                            field.setAccessible(true);
                            XMLUtilities.setXmlValue(fieldValue, field, actualValue);
                        } //end if
                    } //end if
                } //end if
            } else {
                lastFieldName = getSrActualField(token, lastFieldName, srType, actualValue);
            } //end if

        } //end while
    } //end readTokens()

    private String getSrActualField(String token, String lastFieldName, Class srType,
            SrType actualValue) throws IllegalAccessException {
        String currentFieldName = null;
        int idx = token.indexOf('=');

        if (idx == -1) {
            Field field = findField(srType, lastFieldName);
            if (field != null) {
                int modifiers = field.getModifiers();
                if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
                    field.setAccessible(true);
                    Object obj = null;
                    String fieldValue = XMLUtilities.getXmlValue(actualValue, field);
                    token = fieldValue + token;
                    idx = token.indexOf('=');
                    fieldValue = token.substring(idx + 1);
                    XMLUtilities.setXmlValue(fieldValue, field, actualValue);
                } //end if
            } //end if

        } else {
            String fieldName = token.substring(0, idx);
            String fieldValue = token.substring(idx + 1);
            currentFieldName = fieldName;

            Field field = findField(srType, fieldName);
            if (field != null) {
                int modifiers = field.getModifiers();
                if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
                    field.setAccessible(true);
                    XMLUtilities.setXmlValue(fieldValue, field, actualValue);
                } //end if
            } //end if
        } //end if

        return currentFieldName;
    } //end getSrActualField()

    private Field findField(Class srType, String fieldName) {

        try {
            Field field = srType.getDeclaredField(fieldName);
            return field;
        } catch (NoSuchFieldException ex) {
            Class superClass = srType.getSuperclass();
            if (superClass == null) {
                return null;
            } else {
                return findField(superClass, fieldName);
            }
        } //end if
    } //end findField()

    private DbObject newInstanceFromSpecialConstructor(String value, Class claz, HashMap dbobjMap)
            throws ClassNotFoundException, NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {

        //Count number of parameters
        int nbParams = 0;
        StringTokenizer st = new StringTokenizer(value, ",");
        while (st.hasMoreTokens()) {
            st.nextToken();
            nbParams++;
        } //end while

        Class[] classes = new Class[nbParams];
        Object[] objects = new Object[nbParams];

        nbParams = 0;
        st = new StringTokenizer(value, ",");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (nbParams == 0) { //1st parameter is always the composite
                int idx = token.indexOf('=');
                String className = token.substring(0, idx);
                classes[0] = Class.forName(className);

                String key = token.substring(idx + 2); // skip the 1st two character (the '=' and '#' character)
                DbObject actualComposite = (DbObject) dbobjMap.get(key);
                objects[0] = actualComposite;
            } else {
                Class paramClass = getClassFor(token);
                classes[nbParams] = paramClass;
                objects[nbParams] = getDefaultValueFor(paramClass, token);
            }
            nbParams++;
        } //end while

        Constructor constr = claz.getConstructor(classes);
        if (constr == null) {
            int i = 9; //not found!
            return null;
        } else {
            DbObject newInstance = (DbObject) constr.newInstance(objects);
            return newInstance;
        } //end if

    } //end newInstanceFromSpecialConstructor()

    private Class getClassFor(String type) {
        Class claz;
        if (type.equals("boolean")) {
            claz = boolean.class;
        } else {
            try {
                claz = Class.forName(type);
            } catch (ClassNotFoundException ex) {
                claz = null;
            } //end try
        } //end if

        return claz;
    } //end getClassFor()

    private Object getDefaultValueFor(Class claz, String type) {
        Object defValue;

        if (type.equals("boolean")) {
            defValue = Boolean.TRUE;
        } else if (IntDomain.class.isAssignableFrom(claz)) {
            try {
                //each intDomain has a getInstance() static method with an integer a sole parameter
                Method method = claz.getMethod("getInstance", new Class[] { int.class });
                //create a default instance
                defValue = method.invoke(null, new Object[] { new Integer(1) });
            } catch (Exception ex) {
                defValue = null;
            } //end try
        } else {
            defValue = null;
        }

        return defValue;
    } //end getDefaultValueFor

    private DbObject findNamedObject(Class cmpClass, DbObject composite, ElementStructure struct)
            throws DbException {
        DbObject namedObject = null;

        //Get the name of the object to be created
        ElementStructure.Property nameProperty = struct.getProperty(NAME_PROPERTY);
        if (nameProperty == null)
            return null;

        String name = nameProperty.m_value;

        //Get cmpClass' metaclass
        DbEnumeration dbEnum = null;
        try {
            Field field = cmpClass.getDeclaredField(META_CLASS);
            MetaClass mc = (MetaClass) field.get(null); //null because it's a static field

            //Finds out if composite has not already an object with the same name
            DbRelationN relN = composite.getComponents();
            dbEnum = relN.elements(mc);
            while (dbEnum.hasMoreElements()) {
                DbObject obj = dbEnum.nextElement();
                if (obj.getName().equals(name)) {
                    namedObject = obj;
                    break;
                } //end if
            } //end while

        } catch (NoSuchFieldException ex) {
            namedObject = null;
        } catch (IllegalAccessException ex) {
            namedObject = null;
        } finally {
            if (dbEnum != null) {
                dbEnum.close();
            }
        } //end try

        return namedObject;
    }

    //A key is made of the class name and a unique ID separated by a semi-colon character
    private String getClassName(String key) {
        String className = null;
        int idx = key.indexOf(':');
        if (idx != -1) {
            className = key.substring(0, idx);
        }

        return className;
    }

    //
    // INNER CLASSES
    //
    private static class XmlImportStringTokenizer extends StringTokenizer {
        private String m_str;
        private String m_delim;
        private int m_currentPosition;
        private boolean m_hasMore;

        XmlImportStringTokenizer(String str, String delim) {
            super(str, delim);
            m_str = str;
            m_delim = delim;
            m_currentPosition = 0;
        }

        //Overrides StringTokenizer
        public boolean hasMoreTokens() {
            m_hasMore = super.hasMoreTokens();
            return m_hasMore;
        }

        //Overrides StringTokenizer
        public String nextToken() {
            String token = super.nextToken();

            int nbEquals = countChar(token, '=');
            if (nbEquals > 1) { //if nested token
                String nestedToken;

                while (hasMoreTokens()) {
                    nestedToken = super.nextToken();
                    token = token + m_delim + nestedToken;
                } //end while 
            } //end if

            return token;
        } //end nextToken()

        private int countChar(String word, char c) {
            int nb = word.length();

            int nbEquals = 0;
            for (int i = 0; i < nb; i++) {
                if (word.charAt(i) == c) {
                    nbEquals++;
                }
            } //end for 

            return nbEquals;
        } //end countChar()

    } //end XmlImportStringTokenizer()

    //
    // MAIN
    //
    private static final String FILENAME = "C:/Documents and Settings/Open ModelSphere/xml/export.xml"; //NOT LOCALIZABLE, main()

    public static void test1() throws IOException {
        try {
            XmlFileOpener opener = XmlFileOpener.getSingleton();
            opener.doOpenFile(null, FILENAME, null);
        } catch (Exception ex) {
            System.out.println("exception: " + ex); //NOT LOCALIZABLE, main()
            ex.printStackTrace();
        }

        System.out.println("Press ENTER to Quit."); //NOT LOCALIZABLE, main()

        byte[] buf = new byte[256];
        System.in.read(buf, 0, 255);
    } //end test1()

    private static String text = "displayedComponent=2;text=;imageName=protected;image=width=11;height=11;pixels="
            + "so999659999N9999so999659999N9999so999659999N9999so999659999N9999so999659999N9999so999659999N9999so99"
            + "9659999N9999so999659999N9999so999659999N9999a@@@9659999N9999so999659999N9999so999659999N9999so999659"
            + "9999so999659999N9999so999659999N9999so999pDA;;";

    private static void test2() {
        StringTokenizer st = new XmlImportWorker.XmlImportStringTokenizer(text, ";");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            System.out.println("token = " + token);
        } //end while

    } //end test2()

    public static void main(String[] args) {
        //test1(); 
        test2();
    } //end main()

} //end XmlImportWorker
