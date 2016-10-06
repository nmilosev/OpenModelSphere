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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/
package org.modelsphere.sms.features.startupwizard;

import java.awt.Component;
import java.io.File;
import java.text.MessageFormat;
import java.util.*;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.gui.wizard2.*;
import org.modelsphere.jack.preference.*;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.FocusManager;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.be.db.*;
import org.modelsphere.sms.be.db.util.BEUtility;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.features.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.util.JavaUtility;
import org.modelsphere.sms.oo.java.db.util.JavaUtility.JavaInfo;
import org.modelsphere.sms.oo.java.features.JavaToolkit;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORNotation;
import org.modelsphere.sms.or.db.util.AnyORObject;
import org.modelsphere.sms.plugins.TargetSystem;
import org.modelsphere.sms.plugins.TargetSystemInfo;
import org.modelsphere.sms.plugins.TargetSystemManager;

/**
 * @author Grandite
 * 
 */
public class StartupWizardModel extends DefaultWizardModel implements PropertiesProvider {
    public static final String MODEL_CREATION_WIZARD_ENABLED_KEY = "ModelCreationWizardEnabled"; // NOT LOCALIZABLE, property key
    public static final Boolean MODEL_CREATION_WIZARD_ENABLED_DEFAULT = Boolean.TRUE;

    private static boolean wizardEnabled = true;

    private static final String kDefaultProjectName = LocaleMgr.screen.getString("newProjectName");
    static final String k0Creation = LocaleMgr.message.getString("0Creation");
    private static final String kRemove0 = LocaleMgr.message.getString("Remove0");
    private static final String kTransName = LocaleMgr.screen.getString("createModel");

    private static final String kMessageSelectModelType = LocaleMgr.message
            .getString("selectModelType");
    private static final String kMessageSpecifyName = LocaleMgr.message.getString("specifyName");
    private static final String kMessageSelectNotation = LocaleMgr.message
            .getString("selectNotation");
    private static final String kMessageSelectTargetSystem = LocaleMgr.message
            .getString("selectTargetSystem");
    private static final String kMessageSelectFile = LocaleMgr.message.getString("selectFile");
    private static final String kMessageSelectJRE = LocaleMgr.message.getString("selectJRE");

    static final ArrayList<String> DEFAULT_JRE_JARS = new ArrayList<String>(Arrays
            .asList(new String[] { "lib" + File.separator + "rt.jar",
                    "lib" + File.separator + "jsse.jar", "lib" + File.separator + "jce.jar",
                    "lib" + File.separator + "charsets.jar", }));

    // Step 0
    private boolean newModel = true;
    private String openFileName;
    private ArrayList<String> recentFiles = new ArrayList<String>();

    // Shared
    private boolean newProject;
    private String newProjectName = kDefaultProjectName;

    static final int MODEL_TYPE_DM = 0;
    static final int MODEL_TYPE_BPM = 1;
    static final int MODEL_TYPE_UML = 2;
    private int modelType = -1;

    private DbSMSStyle style = null;
    private DbSMSPackage model = null;
    private DbProject project = null;
    private DbSMSDiagram diagram = null;
    private DbSMSDiagram createdDiagram = null;
    private DbSMSNotation notation = null;

    private String orNotation = null;
    private String erNotation = null;
    private String dmNotation = null;
    private String beNotation = null;

    // DM Specifics
    static final int DATAMODEL_TYPE_ENTITY_RELATION = 0;
    static final int DATAMODEL_TYPE_LOGICAL = 1;
    static final int DATAMODEL_TYPE_PHYSICAL = 2;

    private int dataModelType = -1;

    private TargetSystemInfo targetSystemInfo;
    private int defaultTargetSystemID = -1;

    // ULM specific
    static final int UML_MODEL_TYPE_CLASS = 0;
    static final int UML_MODEL_TYPE_USECASE = 1;
    static final int UML_MODEL_TYPE_PACKAGE = 2;
    static final int UML_MODEL_TYPE_SEQUENCE = 3;
    static final int UML_MODEL_TYPE_STATEMENT = 4;
    static final int UML_MODEL_TYPE_COLLABORATOR = 5;
    static final int UML_MODEL_TYPE_ACTIVITY = 6;
    static final int UML_MODEL_TYPE_COMPONENT = 7;
    static final int UML_MODEL_TYPE_DEPLOY = 8;

    private int umlModelType = -1;

    private ArrayList<String> packages;

    private static final String PROPERTY_MODEL_TYPE = "modelType";
    private static final int PROPERTY_MODEL_TYPE_DEFAULT = -1;
    private static final String PROPERTY_DATA_MODEL_TYPE = "dataModelType";
    private static final int PROPERTY_DATA_MODEL_TYPE_DEFAULT = DATAMODEL_TYPE_LOGICAL;
    private static final String PROPERTY_UML_MODEL_TYPE = "umlModelType";
    private static final int PROPERTY_UML_MODEL_TYPE_DEFAULT = UML_MODEL_TYPE_CLASS;
    private static final String PROPERTY_TARGET_SYSTEM = "targetSystem";
    private static final int PROPERTY_TARGET_SYSTEM_DEFAULT = 1; // ID for Logical 1.0 

    private StartPage startPage = null;
    private ProjectModelPage projectModelPage = null;
    private DMModelTypePage dmModelTypePage = null;
    private BPMNotationPage bpmNotationPage = null;
    private UMLDiagramTypePage umlDiagramTypePage = null;
    private DMTargetPage dmTargetPage = null;
    private DMNotationPage dmNotationPage = null;
    private UMLJavaVMPage umlJavaVMPage = null;
    private UMLClassPackagePage umlClassPackagePage = null;
    private DMSummaryPage dmSummaryPage = null;
    private BPMSummaryPage bpmSummaryPage = null;
    private UMLSummaryPage umlSummaryPage = null;

    private AbstractPage undefinedPage = new UndefinedPage();

    private File jreHome;
    private File defaultJREHome;
    private File customJREHome;

    StartupWizardModel(boolean startup) {
        init(startup);
    }

    private void init(boolean startup) {
        loadOptions();

        String javaHome = System.getProperty("java.home");
        this.jreHome = this.defaultJREHome = new File(javaHome);

        startPage = new StartPage(this);
        projectModelPage = new ProjectModelPage(this);
        dmModelTypePage = new DMModelTypePage(this);
        bpmNotationPage = new BPMNotationPage(this);
        umlDiagramTypePage = new UMLDiagramTypePage(this);
        dmTargetPage = new DMTargetPage(this);
        dmNotationPage = new DMNotationPage(this);
        umlJavaVMPage = new UMLJavaVMPage(this);
        umlClassPackagePage = new UMLClassPackagePage(this);
        dmSummaryPage = new DMSummaryPage(this);
        bpmSummaryPage = new BPMSummaryPage(this);
        umlSummaryPage = new UMLSummaryPage(this);

        RecentFiles recentFiles = null;

        recentFiles = ApplicationContext.getDefaultMainFrame().getRecentFiles();
        int max = recentFiles.getNbVisibleRF() < recentFiles.size() ? recentFiles.getNbVisibleRF()
                : recentFiles.size();
        for (int i = 0; i < max; i++) {
            this.recentFiles.add(recentFiles.elementAt(i));
        }
        if (this.recentFiles.size() > 0 && startup) {
            newModel = false;
            openFileName = this.recentFiles.get(0);
        } else {
            newModel = true;
        }

        newProject = FocusManager.getSingleton().getCurrentProject() == null;

        if (!newProject) {
            project = ApplicationContext.getFocusManager().getCurrentProject();
        }

        updatePages();
    }

    //Implements PropertiesProvider
    public final void updateProperties() {
        PropertiesSet appPref = PropertiesManager.APPLICATION_PROPERTIES_SET;
        appPref.removeProperty(StartupWizardModel.class, MODEL_CREATION_WIZARD_ENABLED_KEY);
        appPref.setProperty(StartupWizardModel.class, MODEL_CREATION_WIZARD_ENABLED_KEY, Boolean
                .toString(wizardEnabled));
    }

    public static boolean getModelCreationWizardProperty() {
        return PropertiesManager.APPLICATION_PROPERTIES_SET.getPropertyBoolean(
                StartupWizardModel.class, MODEL_CREATION_WIZARD_ENABLED_KEY,
                MODEL_CREATION_WIZARD_ENABLED_DEFAULT).booleanValue();
    }

    static void setModelCreationWizardProperty(boolean value) {
        PropertiesManager.APPLICATION_PROPERTIES_SET.setProperty(StartupWizardModel.class,
                MODEL_CREATION_WIZARD_ENABLED_KEY, value);
    }

    void setNotation(DbSMSNotation chosenNotation) {
        if (chosenNotation == this.notation)
            return;
        notation = chosenNotation;
        fireStateChanged();
    }

    DbSMSNotation getNotation() {
        return notation;
    }

    void setChosenStyle(DbSMSStyle chosenStyle) {
        style = chosenStyle;
    }

    DbSMSStyle getChosenStyle() {
        return style;
    }

    void setChosenModel(DbSMSPackage chosenModel) {
        model = chosenModel;
    }

    public DbSMSPackage getChosenModel() {
        return model;
    }

    void setChosenProject(DbProject project) {
        this.project = project;
    }

    DbProject getChosenProject() {
        return project;
    }

    void setCurrentDiagram(DbSMSDiagram diagram) {
        this.diagram = diagram;
    }

    DbSMSDiagram getCurrentDiagram() {
        return diagram;
    }

    void createProject() throws DbException {
        Db db = null;

        String pattern = k0Creation;
        String transName = MessageFormat.format(pattern, new Object[] { newProjectName });

        DefaultMainFrame mainframe = ApplicationContext.getDefaultMainFrame();
        project = mainframe.createDefaultProject(db);
        project.getDb().beginWriteTrans(transName);
        project.setName(newProjectName);

        //expand built-in nodes (normally one built-in node per project)
        DbRelationN relN = project.getComponents();
        DbEnumeration dbEnum = relN.elements(DbSMSBuiltInTypeNode.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSBuiltInTypeNode node = (DbSMSBuiltInTypeNode) dbEnum.nextElement();
            mainframe.findInExplorer(node);
        }
        dbEnum.close();
        project.getDb().commitTrans();

        project.getDb().resetHistory();
    }

    private void createBEModel() throws DbException {
        DbProject project = getChosenProject();
        DbBEModel bpmModel = new DbBEModel(project);
        setChosenModel(bpmModel);
    }

    private void createClassModel() throws DbException {
        //create class model
        DbProject project = getChosenProject();
        DbJVClassModel classModel = new DbJVClassModel(project);
        setChosenModel(classModel);
    } //end createClassModel()

    private void createDataModel() throws DbException {
        DbSMSTargetSystem targetSystem = null;
        DbProject project = getChosenProject();

        TargetSystem mgr = TargetSystemManager.getSingleton();
        if (dataModelType == DATAMODEL_TYPE_PHYSICAL) {
            targetSystem = TargetSystem.getSpecificTargetSystem(project, getTargetSystemInfo()
                    .getID());
            if (targetSystem == null)
                targetSystem = mgr.createTargetSystem(project, targetSystemInfo.getID());
        } else {
            targetSystem = TargetSystem.getSpecificTargetSystem(project, TargetSystem.SGBD_LOGICAL);
        }
        DbORDataModel datamodel = null;
        if (dataModelType == DATAMODEL_TYPE_ENTITY_RELATION)
            datamodel = AnyORObject.createConceptualDataModel(project, targetSystem);
        else {
            datamodel = AnyORObject.createDataModel(project, targetSystem);
        }
        setChosenModel(datamodel);
    }

    private void createOrDiagram(DbORDataModel datamodel) throws DbException {
        DbProject project = getChosenProject();

        DbORDiagram diagram = (DbORDiagram) datamodel.createComponent(DbORDiagram.metaClass);
        DbORNotation notation = (DbORNotation) project.findComponentByName(DbORNotation.metaClass,
                dmNotation);
        diagram.setNotation(notation);
        DbSMSStyle defaultStyle = (notation == null) ? null : notation.getDefaultStyle();
        if (defaultStyle != null) {
            diagram.setStyle(defaultStyle);
        }
        setNotation(notation);
        setCurrentDiagram(diagram);
        setChosenStyle(defaultStyle);
    }

    private void createClassDiagram(DbJVClassModel classModel) throws DbException {
        DbOODiagram diagram = (DbOODiagram) classModel.createComponent(DbOODiagram.metaClass);
        setCurrentDiagram(diagram);
    }

    private void createBEContext() throws DbException {
        DbSMSPackage smsPackage = getChosenModel();
        DbSMSNotation notation = getNotation();
        if (smsPackage instanceof DbBEModel) {
            if (notation instanceof DbBENotation) {
                DbBEModel beModel = (DbBEModel) smsPackage;
                DbBENotation beNotation = (DbBENotation) notation;

                BEUtility util = BEUtility.getSingleInstance();
                TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
                beModel.setTerminologyName(beNotation.getName());
                Terminology term = terminologyUtil.findModelTerminology(beModel);
                beModel.setName(term.getTerm(DbBEModel.metaClass));

                DbBEUseCase context = util.createUseCase(beModel, beNotation);
                DbBEDiagram diagram = util.createBEDiagram(context, beNotation);
                setCurrentDiagram(diagram);
            }
        }
    }

    void deleteProject() throws DbException {
        String pattern = kRemove0;
        String transName = MessageFormat.format(pattern, new Object[] { DbProject.metaClass });
        DbProject project = getChosenProject();
        Db db = project.getDb();
        db.beginWriteTrans(transName);
        project.doDeleteAction();
        db.commitTrans();
        db.terminate();
        setChosenProject(null);
    }

    public int getDataModelType() {
        return dataModelType;
    }

    public void setDataModelType(int dataModelType) {
        if (this.dataModelType == dataModelType)
            return;
        this.dataModelType = dataModelType;
        this.notation = null;
        updatePages();
        fireStateChanged();
    }

    public boolean isNewProject() {
        return newProject;
    }

    public void setNewProject(boolean newProject) {
        if (this.newProject == newProject)
            return;
        this.newProject = newProject;
        updatePages();
        fireStateChanged();
    }

    public String getNewProjectName() {
        return newProjectName;
    }

    public void setNewProjectName(String newProjectName) {
        if (newProjectName == null && this.newProjectName == null)
            return;
        if (newProjectName != null && this.newProjectName != null
                && newProjectName.equals(this.newProjectName))
            return;

        this.newProjectName = newProjectName;
        fireStateChanged();
    }

    public String getOrNotation() {
        return orNotation;
    }

    public void setOrNotation(String orNotation) {
        this.orNotation = orNotation;
    }

    public String getErNotation() {
        return erNotation;
    }

    public void setErNotation(String erNotation) {
        this.erNotation = erNotation;
    }

    public String getDmNotation() {
        return dmNotation;
    }

    public void setDmNotation(String dmNotation) {
        this.dmNotation = dmNotation;
    }

    public String getBeNotation() {
        return beNotation;
    }

    public void setBeNotation(String beNotation) {
        this.beNotation = beNotation;
    }

    public String getOpenFileName() {
        return openFileName;
    }

    public void setOpenFileName(String openFileName) {
        this.openFileName = openFileName;
        fireStateChanged();
    }

    public int getModelType() {
        return modelType;
    }

    public void setModelType(int modelType) {
        if (this.modelType == modelType)
            return;
        this.modelType = modelType;
        this.notation = null;
        updatePages();
        fireStateChanged();
    }

    private void updatePages() {
        Page[] pages = null;

        if (!newModel) {
            pages = new Page[] { startPage };
        } else {
            switch (modelType) {
            case MODEL_TYPE_DM:
                switch (dataModelType) {
                case DATAMODEL_TYPE_ENTITY_RELATION:
                    pages = new Page[] { startPage, projectModelPage, dmModelTypePage,
                            dmNotationPage, dmSummaryPage };
                    break;
                case DATAMODEL_TYPE_LOGICAL:
                    pages = new Page[] { startPage, projectModelPage, dmModelTypePage,
                            dmNotationPage, dmSummaryPage };
                    break;
                case DATAMODEL_TYPE_PHYSICAL:
                    pages = new Page[] { startPage, projectModelPage, dmModelTypePage,
                            dmTargetPage, dmNotationPage, dmSummaryPage };
                    break;
                }
                break;
            case MODEL_TYPE_BPM:
                pages = new Page[] { startPage, projectModelPage, bpmNotationPage, bpmSummaryPage };
                break;
            case MODEL_TYPE_UML:
                if (umlModelType == UML_MODEL_TYPE_CLASS || umlModelType == UML_MODEL_TYPE_PACKAGE)
                    pages = new Page[] { startPage, projectModelPage, umlDiagramTypePage,
                            umlJavaVMPage, umlClassPackagePage, umlSummaryPage };
                else
                    pages = new Page[] { startPage, projectModelPage, umlDiagramTypePage,
                            umlSummaryPage };
                break;
            default:
                pages = new Page[] { startPage, projectModelPage, undefinedPage };
                break;
            }
        }
        setPages(pages);
    }

    int getUmlModelType() {
        return umlModelType;
    }

    void setUmlModelType(int umlModelType) {
        if (this.umlModelType == umlModelType)
            return;
        this.umlModelType = umlModelType;
        updatePages();
        fireStateChanged();
    }

    void rollback(Component component) {
        if (newProject && project != null) {
            try {
                deleteProject();
            } catch (DbException e) {
                ExceptionHandler.processUncatchedException(component, e);
            }
        }
    }

    void commit(Component component) {
        if (!newModel) {
            ApplicationContext.getDefaultMainFrame().doOpenFromFile(openFileName);
        } else {
            try {
                if (project != null) {
                    project.getDb().beginWriteTrans(kTransName);
                    commitProjectComponents();
                    project.getDb().commitTrans();
                    if (newProject) {
                        project.getDb().resetHistory();
                    }
                }

            } catch (DbException e) {
                ExceptionHandler.processUncatchedException(component, e);
            }

            //expand the explorer
            if (createdDiagram != null) {
                DefaultMainFrame mainframe = ApplicationContext.getDefaultMainFrame();
                try {
                    createdDiagram.getDb().beginReadTrans();
                    mainframe.findInExplorer(createdDiagram);
                    //show the diagram
                    mainframe.addDiagramInternalFrame(createdDiagram);
                    createdDiagram.getDb().commitTrans();
                } catch (DbException e) {
                    ExceptionHandler.processUncatchedException(component, e);
                }
            }
        }
        saveOptions();
    }

    private void saveOptions() {
        PropertiesManager.getPreferencePropertiesSet().setProperty(StartupWizard.class,
                PROPERTY_MODEL_TYPE, modelType);
        PropertiesManager.getPreferencePropertiesSet().setProperty(StartupWizard.class,
                PROPERTY_DATA_MODEL_TYPE, dataModelType);
        PropertiesManager.getPreferencePropertiesSet().setProperty(StartupWizard.class,
                PROPERTY_UML_MODEL_TYPE, umlModelType);
        PropertiesManager.getPreferencePropertiesSet().setProperty(StartupWizard.class,
                PROPERTY_TARGET_SYSTEM, defaultTargetSystemID);
    }

    private void loadOptions() {
        wizardEnabled = PropertiesManager.APPLICATION_PROPERTIES_SET.getPropertyBoolean(
                StartupWizardModel.class, MODEL_CREATION_WIZARD_ENABLED_KEY,
                MODEL_CREATION_WIZARD_ENABLED_DEFAULT).booleanValue();

        modelType = PropertiesManager.getPreferencePropertiesSet().getPropertyInteger(
                StartupWizard.class, PROPERTY_MODEL_TYPE, PROPERTY_MODEL_TYPE_DEFAULT);
        dataModelType = PropertiesManager.getPreferencePropertiesSet().getPropertyInteger(
                StartupWizard.class, PROPERTY_DATA_MODEL_TYPE, PROPERTY_DATA_MODEL_TYPE_DEFAULT);
        umlModelType = PropertiesManager.getPreferencePropertiesSet().getPropertyInteger(
                StartupWizard.class, PROPERTY_UML_MODEL_TYPE, PROPERTY_UML_MODEL_TYPE_DEFAULT);
        defaultTargetSystemID = PropertiesManager.getPreferencePropertiesSet().getPropertyInteger(
                StartupWizard.class, PROPERTY_TARGET_SYSTEM, PROPERTY_TARGET_SYSTEM_DEFAULT);
    }

    private void commitProjectComponents() throws DbException {
        switch (modelType) {
        case MODEL_TYPE_DM:
            commitDMComponents();
            break;
        case MODEL_TYPE_BPM:
            commitBEComponents();
            break;

        case MODEL_TYPE_UML:
            commitUMLComponents();
            break;
        }
    }

    private void commitUMLComponents() throws DbException {
        if (umlModelType == UML_MODEL_TYPE_CLASS || umlModelType == UML_MODEL_TYPE_PACKAGE) {
            createClassModel();
            DbJVClassModel classModel = (DbJVClassModel) getChosenModel();
            createClassDiagram(classModel);

            JavaUtility.JavaInfo info = new JavaUtility.JavaInfo(this.jreHome);
            createPackages(classModel, info);
            createdDiagram = getCurrentDiagram();
        } else {
            createBEModel();
            createBEContext();
            createdDiagram = getCurrentDiagram();
        }
    }

    private void createPackages(DbJVClassModel classModel, JavaInfo info) throws DbException {
        if (packages == null)
            return;

        JavaToolkit toolkit = JavaToolkit.getSingleton();

        ArrayList<String> classList = new ArrayList<String>();

        ArrayList<String> fileNames = new ArrayList<String>();

        for (int i = 0; i < DEFAULT_JRE_JARS.size(); i++) {
            fileNames.add(new File(jreHome, DEFAULT_JRE_JARS.get(i)).getPath());
        }

        toolkit.fillClassList(fileNames.toArray(), packages, classList);

        Iterator<String> iter = classList.iterator();
        JavaUtility utility = JavaUtility.getSingleton();

        while (iter.hasNext()) {
            String className = iter.next();
            int idx = className.lastIndexOf(".class"); //NOT LOCALIZABLE 
            if (idx != -1) {
                //create class
                className = className.substring(0, idx);
                utility.findReflectClass(classModel, className, info);
            }
        }
    }

    private void commitDMComponents() throws DbException {
        createDataModel();
        // Create Diagram
        DbSMSPackage modelO = getChosenModel();
        if (modelO instanceof DbORDataModel) {
            DbORDataModel datamodel = (DbORDataModel) modelO;
            createOrDiagram(datamodel);
        }
        DbORDiagram diagram = null;
        DbSMSPackage chosenModel = getChosenModel();
        if (chosenModel instanceof DbORDataModel) {
            DbSMSNotation notation = getNotation();
            DbSMSStyle style = getChosenStyle();
            if (notation instanceof DbORNotation) {
                DbORDataModel dataModel = (DbORDataModel) chosenModel;
                DbORNotation orNotation = (DbORNotation) notation;

                // does datamodel contain a diagram? 
                DbRelationN relN = dataModel.getComponents();
                DbEnumeration dbEnum = relN.elements(DbORDiagram.metaClass);
                diagram = dbEnum.hasMoreElements() ? (DbORDiagram) dbEnum.nextElement() : null;
                dbEnum.close();

                //if no diagram, create one
                if (diagram == null) {
                    diagram = new DbORDiagram(dataModel);
                }

                //apply notation on diagram
                diagram.setNotation(orNotation);

                //apply style, if any, on diagram
                if (style != null) {
                    diagram.setStyle(style);
                }
            }
        }
        createdDiagram = diagram;
    }

    private void commitBEComponents() throws DbException {
        createBEModel();
        createBEContext();
        DbBEDiagram diagram = null;
        DbSMSPackage chosenModel = getChosenModel();
        if (chosenModel instanceof DbBEModel) {
            DbSMSNotation notation = getNotation();
            if (notation instanceof DbBENotation) {
                DbBEModel beModel = (DbBEModel) chosenModel;
                DbBENotation beNotation = (DbBENotation) notation;

                DbRelationN relN = beModel.getComponents();
                DbEnumeration dbEnum = relN.elements(DbBEUseCase.metaClass);
                DbBEUseCase context = dbEnum.hasMoreElements() ? (DbBEUseCase) dbEnum.nextElement()
                        : null;
                dbEnum.close();

                //if no context, create one
                if (context == null) {
                    context = new DbBEUseCase(beModel);
                }

                //does context contain a diagram? 
                relN = context.getComponents();
                dbEnum = relN.elements(DbBEDiagram.metaClass);
                diagram = dbEnum.hasMoreElements() ? (DbBEDiagram) dbEnum.nextElement() : null;
                dbEnum.close();

                //if no diagram, create one
                if (diagram == null) {
                    diagram = new DbBEDiagram(context);
                }

                //apply notation on diagram
                diagram.setNotation(beNotation);
            }
        }
        createdDiagram = diagram;
    }

    public List<String> getPackages() {
        return packages;
    }

    public void setPackages(ArrayList<String> packages) {
        this.packages = packages;
    }

    public boolean isNewModel() {
        return newModel;
    }

    public void setNewModel(boolean createNewModel) {
        if (this.newModel == createNewModel) {
            return;
        }
        this.newModel = createNewModel;
        updatePages();
        fireStateChanged();
    }

    public List<String> getRecentFiles() {
        return Collections.unmodifiableList(recentFiles);
    }

    @Override
    public boolean finishEnabled() {
        boolean enabled = super.finishEnabled();
        if (enabled) {
            int id = getPageID();
            if (id > -1) {
                Page page = getPage(id);
                if (page == startPage) {
                    if (!newModel) { // open file
                        enabled = openFileName != null;
                    }
                }
            }
        }
        return enabled;
    }

    @Override
    public boolean nextEnabled() {
        boolean enabled = super.nextEnabled();
        if (enabled) {
            if (newModel) {
                Page page = getPage();
                if (page == projectModelPage) {
                    if (modelType < 0) {
                        return false;
                    }
                    if (newProject) {
                        if (newProjectName == null || newProjectName.trim().length() == 0) {
                            return false;
                        }
                    }
                } else if (page == dmTargetPage) {
                    if (targetSystemInfo == null) {
                        return false;
                    }
                } else if (page == dmNotationPage) {
                    if (notation == null) {
                        return false;
                    }
                } else if (page == bpmNotationPage) {
                    if (notation == null) {
                        return false;
                    }
                } else if (page == umlJavaVMPage) {
                    if (jreHome == null) {
                        return false;
                    }
                }
            }
        }
        return enabled;
    }

    @Override
    public String getWarning() {
        if (newModel) {
            Page page = getPage();
            if (page == projectModelPage) {
                if (modelType < 0) {
                    return kMessageSelectModelType;
                }
                if (newProject) {
                    if (newProjectName == null || newProjectName.trim().length() == 0) {
                        return kMessageSpecifyName;
                    }
                }
            } else if (page == dmTargetPage) {
                if (targetSystemInfo == null) {
                    return kMessageSelectTargetSystem;
                }
            } else if (page == dmNotationPage) {
                if (notation == null) {
                    return kMessageSelectNotation;
                }
            } else if (page == bpmNotationPage) {
                if (notation == null) {
                    return kMessageSelectNotation;
                }
            } else if (page == umlJavaVMPage) {
                if (jreHome == null) {
                    return kMessageSelectJRE;
                }
            }
        } else {
            Page page = getPage();
            if (page == startPage) {
                if (openFileName == null || openFileName.trim().length() == 0) {
                    return kMessageSelectFile;
                }
            }
        }
        return null;
    }

    public TargetSystemInfo getTargetSystemInfo() {
        return targetSystemInfo;
    }

    public void setTargetSystemInfo(TargetSystemInfo targetSystemInfo) {
        if (this.targetSystemInfo == targetSystemInfo)
            return;
        this.targetSystemInfo = targetSystemInfo;
        if (this.targetSystemInfo == null) {
            defaultTargetSystemID = -1;
        } else {
            defaultTargetSystemID = this.targetSystemInfo.getID();
        }
        fireStateChanged();
    }

    public File getJREHome() {
        return jreHome;
    }

    public void setJREHome(File jreHome) {
        if (this.jreHome == jreHome)
            return;
        if (this.jreHome != null && jreHome != null && jreHome.equals(this.jreHome))
            return;
        this.jreHome = jreHome;
        fireStateChanged();
    }

    public File getDefaultJREHome() {
        return defaultJREHome;
    }

    public File getCustomJREHome() {
        return customJREHome;
    }

    public void setCustomJREHome(File customJREHome) {
        if (customJREHome == this.customJREHome)
            return;
        if (this.customJREHome != null && customJREHome != null
                && customJREHome.equals(this.customJREHome))
            return;
        this.customJREHome = customJREHome;
    }

    public int getDefaultTargetSystemID() {
        return defaultTargetSystemID;
    }

}
