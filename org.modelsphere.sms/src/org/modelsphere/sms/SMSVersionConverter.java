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

package org.modelsphere.sms;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Locale;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbLoginNode;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.VersionConverter;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.be.db.DbBEContextCell;
import org.modelsphere.sms.be.db.DbBEContextGo;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEStyle;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.DbBEUseCaseQualifier;
import org.modelsphere.sms.be.db.util.BEUtility;
import org.modelsphere.sms.db.DbSMSAbstractPackage;
import org.modelsphere.sms.db.DbSMSBuiltInTypePackage;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSStyle;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.db.DbSMSUmlExtensibility;
import org.modelsphere.sms.db.DbSMSUserDefinedPackage;
import org.modelsphere.sms.db.srtypes.SMSHorizontalAlignment;
import org.modelsphere.sms.db.srtypes.SMSVerticalAlignment;
import org.modelsphere.sms.db.util.DbInitialization;
import org.modelsphere.sms.db.util.Extensibility;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOOStyle;
import org.modelsphere.sms.or.db.DbORBuiltInType;
import org.modelsphere.sms.or.db.DbORCommonItemStyle;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORDomainStyle;
import org.modelsphere.sms.or.db.DbORNotation;
import org.modelsphere.sms.or.db.DbORStyle;

public final class SMSVersionConverter implements VersionConverter {

    static final String STR_CUSTOM_IMPORTED = LocaleMgr.misc.getString("custom");//"(custom) "; 
    static final String FROM_VERSION = LocaleMgr.misc.getString("from_version"); //" (from version "; //MUST BE LOCALIZED

    ArrayList<String> m_arrayFrench = null;
    ArrayList<String> m_arrayEnglish = null;
    ArrayList<String> m_arrayFrenchStyles = null;
    ArrayList<String> m_arrayEnglishStyles = null;

    /**
     * Increment VERSION for each new commercial version, and add a case to the switch statement to
     * convert projects from the previous version. 1 = 1.0 (build > 100) 2 = 2.0 (build = 215) 3 =
     * 2.0 (build > 215) 4 = 2.1 (build >= 300) 5 = 2.2 (build >= 400) 6 = 2.3 (build >= 500) 7 =
     * 2.4 (build >= 600) 8 = 2.5 (build >= 700) 9 = 2.6 (build >= 800) 14 = 3.1 (build >= 907)
     * 15 = 3.2 (build >= 950)
     * 
     */

    TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

    public static final int VERSION = 15;

    // Distributor identifier (this id is part of the sms files header)
    // Default value is 0 for versions distributed via modelsphere.org.
    private static final int DISTRIBUTOR = 0;

    // Possible values for RELEASE_STATUS
    public static final int STATUS_ALPHA = 5;
    public static final int STATUS_BETA = 10;
    public static final int STATUS_RELEASE_CANDIDATE = 15;
    public static final int STATUS_RELEASE = 25;

    // Status identifier (this id is part of the sms files header)
    private static final int RELEASE_STATUS = STATUS_BETA;

    private int oldVersion = 0;
    private int oldReleaseStatus = -1; // -1 == Unknown (saved with version older than 907 - 3.1)
    private int oldDistributorID = -1; // -1 == Unknown (saved with version older than 907 - 3.1)
    private String oldDisplayVersion;

    public final int getCurrentVersion() {
        return VERSION;
    }

    public final void setOldVersion(int oldVersion) {
        this.oldVersion = oldVersion;

        switch (oldVersion) {
        case 1:
            oldDisplayVersion = "1.0";
            break;
        case 2:
            oldDisplayVersion = "2.0";
            break;
        case 3:
            oldDisplayVersion = "2.0";
            break;
        case 4:
            oldDisplayVersion = "2.1";
            break;
        case 5:
            oldDisplayVersion = "2.2";
            break;
        case 6:
            oldDisplayVersion = "2.3";
            break;
        case 7:
            oldDisplayVersion = "2.4";
            break;
        case 8:
            oldDisplayVersion = "2.5";
            break;
        case 9:
            oldDisplayVersion = "2.6";
            break;
        case 10:
            oldDisplayVersion = "2.7";
            break;
        case 11:
            oldDisplayVersion = "2.8";
            break;
        case 12:
            oldDisplayVersion = "2.9";
            break;
        case 13:
            oldDisplayVersion = "3.0";
            break;
        case 14:
            oldDisplayVersion = "3.1";
            break;
        case 15:
            oldDisplayVersion = "3.2";
            break;
        default:
            oldDisplayVersion = "<";
        }

    }

    public final DbObject convertAfterLoad(DbObject dbo) throws DbException {
        //Current version, no need to convert
        if (dbo instanceof DbSMSProject)
            convertProjectLocale((DbSMSProject) dbo);

        if (oldVersion == VERSION) {

            // allow convertion for debug purposes - will be applied on a repository only if flag convertRepository is specified
            //                                       will be applied on a non repository always
            if (dbo instanceof DbSMSProject && Debug.isDebug()) {
                convertProject((DbSMSProject) dbo);
            }
            return dbo;
        }

        if (dbo instanceof DbLoginNode)
            convertLoginNode((DbLoginNode) dbo);
        else if (dbo instanceof DbSMSProject)
            convertProject((DbSMSProject) dbo);

        return dbo;
    }

    private void convertLoginNode(DbLoginNode loginNode) throws DbException {
    }

    private void convertProject(DbSMSProject project) throws DbException {
        convertStyles(project);

        DbSMSProject newProject = (DbSMSProject) ApplicationContext.getDefaultMainFrame()
                .createDefaultProject(null);
        try { // this is just a check and create if not exists .. no need to check version.
            project.initUMLExtensibility();

            //initBEStyleAndNotation(project);
            switch (oldVersion) {
            case 1:
                convertToVersion2(project); //Convert to 2.0 (build = 215)
                // no break, cascade to next converter.
            case 2:
                convertToVersion3(project);//Convert to 2.0 (build > 215)
                // no break, cascade to next converter.
            case 3:
                convertToVersion4(project);//Convert to 2.1 (build >= 300)
                // no break, cascade to next converter.
            case 4:
                convertToVersion5(project);//Convert to 2.2 (build >= 400)
            case 5:
                convertToVersion6(project, newProject);//Convert to 2.3 (build >= 500)
                // no break, cascade to next converter.
            case 6: {// current version
                convertToVersion7(project, newProject);//Convert to 2.4 (build >= 600)
                if (oldVersion != getCurrentVersion()) {
                    markBuiltItStyles(project, newProject);
                    project.setOrDefaultStyle(null);
                    project.setErDefaultStyle(null);
                    project.setOoDefaultStyle(null);
                    project.setBeDefaultStyle(null);
                    DbInitialization.createDefaultStyles(project);
                    markBuiltItNotations(project, newProject);
                    synchronizeStereotypes(project, newProject);
                }
            }
            case 7: // current version
                convertToVersion8(project, newProject);//Convert to 2.5 (build >= 700)
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                convertToVersion14(project); //Convert to 3.1 (build >= 907)
            case 14:
                convertToVersion15(project); 
            case 15: // current version
                break;
            default:
                throw new RuntimeException("Unknown version: " + oldVersion); //NOT LOCALIZABLE, exception
            }
            ApplicationContext.getDefaultMainFrame().closeProject(newProject);
        } catch (DbException exc) {
            ApplicationContext.getDefaultMainFrame().closeProject(newProject);
            throw exc;
        }
    }

    public final void synchronizeStereotypes(DbSMSProject project, DbSMSProject newProject)
            throws DbException {
        ////
        // to synchronize stereotypes:
        // for each stereotype of a new project in the curretn version
        //   if the stereotype does no exist in the old project (name + concept + icon)
        // 	create it 
        //   update the icon in all cases (for each stereotype)

        //create the new project

        DbSMSProject oldProject = (DbSMSProject) project;

        newProject.getDb().beginReadTrans();
        oldProject.getDb().beginWriteTrans("");

        //for each stereotype of the new project
        DbSMSUmlExtensibility newUMLExtensibility = newProject.getUmlExtensibility();
        DbSMSUmlExtensibility oldUMLExtensibility = oldProject.getUmlExtensibility();

        DbEnumeration newStereotypesEnum = newUMLExtensibility.getComponents().elements(
                DbSMSStereotype.metaClass);

        while (newStereotypesEnum.hasMoreElements()) {
            DbEnumeration oldStereotypesEnum = oldUMLExtensibility.getComponents().elements(
                    DbSMSStereotype.metaClass);
            DbSMSStereotype newStereotype = (DbSMSStereotype) newStereotypesEnum.nextElement();
            String newName = newStereotype.getName();
            boolean foundNewStereotype = false;
            boolean hasmore = true;
            while (hasmore) { //changed stereotypes
                hasmore = oldStereotypesEnum.hasMoreElements();
                if (hasmore == false) { //if we reach the end, we have a brand new stereotype !
                    foundNewStereotype = true;
                    break;
                }
                DbSMSStereotype oldStereotype = (DbSMSStereotype) oldStereotypesEnum.nextElement();
                if (newName.equals(oldStereotype.getName())) {
                    if (oldStereotype.getMetaClassName().equals(newStereotype.getMetaClassName())) {
                        foundNewStereotype = false;
                        break;
                    }
                }
            }
            if (true == foundNewStereotype) { //stereotype not found create it... 
                DbSMSStereotype stereotype = new DbSMSStereotype(oldUMLExtensibility);
                stereotype.setBuiltIn(new Boolean(newStereotype.isBuiltIn()));
                stereotype.setName(newStereotype.getName()); //NOT LOCALIZABLE, stereotype name
                stereotype.setIcon(newStereotype.getIcon()); //NOT LOCALIZABLE, stereotype name
                stereotype.setMetaClassName(newStereotype.getMetaClassName());
            }
            oldStereotypesEnum.close();
        }
        newStereotypesEnum.close();

        newProject.getDb().commitTrans();
        oldProject.getDb().commitTrans();
    }

    /*
     * Initialize the new fields in all the styles with the default values.
     */
    public final void convertStyles(DbProject project) throws DbException {
        DbEnumeration dbEnum = project.getComponents().elements(DbSMSStyle.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSStyle style = (DbSMSStyle) dbEnum.nextElement();
            style.initNullOptions();
        }
        dbEnum.close();
    }

    private void initBEStyleAndNotation(DbSMSProject project) throws DbException {
        if (project.getBeDefaultStyle() == null) {
            project.createDefaultStyle();
        }
        if (project.getBeDefaultNotation() == null) {
            project.createBuiltInBENotations();
        }
    }

    /**
     * Convert to 2.0 (build = 215)
     */
    private void convertToVersion2(DbSMSProject project) throws DbException {
        // Set the default diagram styles
        // All null style on diagrams are considered as the normal style for the corresponding model type
        DbEnumeration dbEnum = project.componentTree(DbSMSDiagram.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSDiagram diag = (DbSMSDiagram) dbEnum.nextElement();
            if (diag.getStyle() != null)
                continue;
            diag.setStyle(diag.findStyle());
        }
        dbEnum.close();
    }

    /**
     * Convert to 2.0 (build > 215)
     */
    private void convertToVersion3(DbSMSProject project) throws DbException {
        DbSMSUmlExtensibility umlExtensibility = project.getUmlExtensibility();
        Extensibility.addMissingStereotypes(umlExtensibility);
    }

    /**
     * Convert to 2.1 (build >= 300)
     */
    private void convertToVersion4(DbSMSProject project) throws DbException {
    }

    /**
     * Convert to 2.2 (build >= 400)
     */
    private void convertToVersion5(DbSMSProject project) throws DbException {
        convertProjectOrPackageContent(project);
    }

    /**
     * Convert to 2.3 (build >= 500)
     */

    private void convertProjectLocale(DbSMSProject project) throws DbException {

        ////
        // determine project locale and current locale, and convert if necessary

        Locale runtimeLocale = org.modelsphere.jack.international.LocaleMgr
                .getLocaleFromPreferences(Locale.getDefault(), true);

        String projectLanguage = "en";
        DbEnumeration enumNotations = project.componentTree(DbBENotation.metaClass);
        while (enumNotations.hasMoreElements()) {
            DbBENotation notation = (DbBENotation) enumNotations.nextElement();
            if (notation.getName().equals("Functional Diagram")) {
                projectLanguage = "en";
                break;
            } else if (notation.getName().equals("Diagramme fonctionnel")) {
                projectLanguage = "fr";
                break;
            }
        }
        enumNotations.close();

        if (projectLanguage.equalsIgnoreCase("en") && runtimeLocale.equals(Locale.FRENCH))
            convertProjectLanguage(project, false);
        else if (projectLanguage.equalsIgnoreCase("fr") && !runtimeLocale.equals(Locale.FRENCH))
            convertProjectLanguage(project, true);
    }

    private void convertProjectLanguage(DbSMSProject project, boolean toEnglish) throws DbException {

        DbEnumeration enumNotations = project.componentTree(DbBENotation.metaClass);
        while (enumNotations.hasMoreElements()) {
            DbBENotation notation = (DbBENotation) enumNotations.nextElement();
            if (notation.isBuiltIn()) {
                String equivalent = translateString(notation.getName(), toEnglish);
                if (equivalent != null)
                    notation.setName(equivalent);
                else
                    throw new RuntimeException("Missing stem in translation table.");
            }
        }
        enumNotations.close();

        enumNotations = project.componentTree(DbBEStyle.metaClass);
        while (enumNotations.hasMoreElements()) {
            DbBEStyle style = (DbBEStyle) enumNotations.nextElement();
            if (style.isDefaultStyle()) {
                String equivalent = translateStringStyles(style.getName(), toEnglish);
                if (equivalent != null)
                    style.setName(equivalent);
                else
                    throw new RuntimeException("Missing stem in translation table.");

            }
        }
        enumNotations.close();

        enumNotations = project.componentTree(DbBEModel.metaClass);
        while (enumNotations.hasMoreElements()) {
            DbBEModel model = (DbBEModel) enumNotations.nextElement();
            boolean translate = true;
            if (model.getTerminologyName() == null)
                translate = false;
            else if (model.getTerminologyName().equals(""))
                translate = false;
            if (translate) {
                String equivalent = translateString(model.getTerminologyName(), toEnglish);
                if (equivalent != null)
                    model.setTerminologyName(equivalent);
                else
                    throw new RuntimeException("Missing stem in translation table.");
            }
        }
        enumNotations.close();

        enumNotations = project.componentTree(DbBEUseCase.metaClass);
        while (enumNotations.hasMoreElements()) {
            DbBEUseCase useCase = (DbBEUseCase) enumNotations.nextElement();
            boolean translate = true;
            if (useCase.getTerminologyName() == null)
                translate = false;
            else if (useCase.getTerminologyName().equals(""))
                translate = false;
            if (translate) {
                String equivalent = translateString(useCase.getTerminologyName(), toEnglish);
                if (equivalent != null)
                    useCase.setTerminologyName(equivalent);
                else
                    throw new RuntimeException("Missing stem in translation table.");
            }
        }
        enumNotations.close();

        enumNotations = project.componentTree(DbBENotation.metaClass);
        while (enumNotations.hasMoreElements()) {
            DbBENotation notation = (DbBENotation) enumNotations.nextElement();
            boolean translate = true;
            if (notation.getTerminologyName() == null)
                translate = false;
            else if (notation.getTerminologyName().equals(""))
                translate = false;
            if (translate) {
                String equivalent = translateString(notation.getTerminologyName(), toEnglish);
                if (equivalent != null)
                    notation.setTerminologyName(equivalent);
                else {
                    int masterID = notation.getMasterNotationID().intValue();
                    DbEnumeration enumNotations2 = project.componentTree(DbBENotation.metaClass);
                    while (enumNotations2.hasMoreElements()) {
                        DbBENotation notation2 = (DbBENotation) enumNotations2.nextElement();
                        if (notation2.getMasterNotationID().intValue() == masterID
                                && notation2.isBuiltIn()) {
                            String equivalent2 = translateString(notation2.getTerminologyName(),
                                    toEnglish);
                            if (equivalent2 != null) {
                                notation.setTerminologyName(equivalent2);
                                break;
                            } else
                                throw new RuntimeException("Missing stem in translation table.");
                        }
                    }
                    enumNotations2.close();
                }
            }
        }
        enumNotations.close();
    }

    private void loadTranslationMaps() {
        try {
            if (m_arrayFrench == null) {
                m_arrayFrench = new ArrayList<String>();
                m_arrayFrench.add("Datarun-Fonctionnement de l'organisation");
                m_arrayFrench.add("Datarun-Architecture du système d'information");
                m_arrayFrench.add("Datarun-Fonctionnement du système d'information");
                m_arrayFrench.add("Gane Sarson");
                m_arrayFrench.add("Merise");
                m_arrayFrench.add("Yourdon-DeMarco");
                m_arrayFrench.add("Ward-Mellor");
                m_arrayFrench.add("Diagramme fonctionnel");
                m_arrayFrench.add("Merise MCT");
                m_arrayFrench.add("Merise schéma de flux");
                m_arrayFrench.add("Merise OOM");
                m_arrayFrench.add("P+");
                m_arrayFrench.add("P+ OPAL");
                m_arrayFrench.add("Cycle de vie des objets");
                m_arrayFrench.add("Interaction de messages");

                m_arrayFrench.add("UML-Diagramme d'activité");
                m_arrayFrench.add("UML-Diagramme de collaboration");
                m_arrayFrench.add("UML-Diagramme de composants");
                m_arrayFrench.add("UML-Diagramme de classes");
                m_arrayFrench.add("UML-Diagramme de déploiement");
                m_arrayFrench.add("Modèle UML");
                m_arrayFrench.add("UML-Diagramme de séquence");
                m_arrayFrench.add("UML-Diagramme d'état");
                m_arrayFrench.add("UML-Diagramme d'état");
                m_arrayFrench.add("UML-Diagramme de cas d'utilisation");
                m_arrayFrench.add("UML - Cas d'utilisation");
                m_arrayFrench.add("UML-Diagramme de cas d'utilisation");
                m_arrayFrench.add("UML-Cas d'utilisation");

                m_arrayFrenchStyles = new ArrayList<String>();
                m_arrayFrenchStyles.add("Cas d'utilisation UML");
                m_arrayFrenchStyles.add("UML Cas d'utilisation");
                m_arrayFrenchStyles.add("BPM - Normal");
                m_arrayFrenchStyles.add("Séquences UML");
                m_arrayFrenchStyles.add("États UML");
                m_arrayFrenchStyles.add("Collaborations UML");
                m_arrayFrenchStyles.add("Activités UML");
                m_arrayFrenchStyles.add("Composantes UML");
                m_arrayFrenchStyles.add("Déploiement UML");
            }
            if (m_arrayEnglish == null) {
                m_arrayEnglish = new ArrayList<String>();
                m_arrayEnglish.add("Datarun Business Process Model");
                m_arrayEnglish.add("Datarun Information System Architecture");
                m_arrayEnglish.add("Datarun System Process Model");
                m_arrayEnglish.add("Gane Sarson");
                m_arrayEnglish.add("Merise");
                m_arrayEnglish.add("Yourdon-DeMarco");
                m_arrayEnglish.add("Ward-Mellor");
                m_arrayEnglish.add("Functional Diagram");
                m_arrayEnglish.add("Merise MCT");
                m_arrayEnglish.add("Merise Flow Schema");
                m_arrayEnglish.add("Merise OOM");
                m_arrayEnglish.add("P+");
                m_arrayEnglish.add("P+ OPAL");
                m_arrayEnglish.add("Object Life Cycle");
                m_arrayEnglish.add("Message Modeling");

                m_arrayEnglish.add("UML Activity Diagram");
                m_arrayEnglish.add("UML Collaboration Diagram");
                m_arrayEnglish.add("UML Component Diagram");
                m_arrayEnglish.add("UML Class Diagram");
                m_arrayEnglish.add("UML Deployment Diagram");
                m_arrayEnglish.add("UML Model");
                m_arrayEnglish.add("UML Sequence Diagram");
                m_arrayEnglish.add("UML Statechart Diagram");
                m_arrayEnglish.add("UML State Diagram");
                m_arrayEnglish.add("UML Use Case Diagram");
                m_arrayEnglish.add("UML Use Case Diagram");
                m_arrayEnglish.add("UML Use Case");
                m_arrayEnglish.add("UML Use Case Diagram");

                m_arrayEnglishStyles = new ArrayList<String>();
                m_arrayEnglishStyles.add("UML Use Case");
                m_arrayEnglishStyles.add("UML Use Case");
                m_arrayEnglishStyles.add("BPM - Normal");
                m_arrayEnglishStyles.add("UML Sequence");
                m_arrayEnglishStyles.add("UML Statechart");
                m_arrayEnglishStyles.add("UML Collaboration");
                m_arrayEnglishStyles.add("UML Activity");
                m_arrayEnglishStyles.add("UML Component");
                m_arrayEnglishStyles.add("UML Deployment");
            }
        } catch (Exception e) {
        }
    }

    private String translateString(String stringToTranslate, boolean toEnglish) {
        loadTranslationMaps();
        int indexOfMatchingTerm = 0;
        boolean wasFound = false;
        if (toEnglish) {
            for (indexOfMatchingTerm = 0; indexOfMatchingTerm < m_arrayFrench.size(); indexOfMatchingTerm++) {
                if (m_arrayFrench.get(indexOfMatchingTerm).equalsIgnoreCase(stringToTranslate)) {
                    wasFound = true;
                    break;
                }
            }
            if (wasFound)
                return m_arrayEnglish.get(indexOfMatchingTerm);
            else
                return null;
        } else {
            for (indexOfMatchingTerm = 0; indexOfMatchingTerm < m_arrayEnglish.size(); indexOfMatchingTerm++) {
                if (m_arrayEnglish.get(indexOfMatchingTerm).equalsIgnoreCase(stringToTranslate)) {
                    wasFound = true;
                    break;
                }
            }
            if (wasFound)
                return m_arrayFrench.get(indexOfMatchingTerm);
            else
                return null;
        }
    }

    private String translateStringStyles(String stringToTranslate, boolean toEnglish) {
        loadTranslationMaps();
        int indexOfMatchingTerm = 0;
        boolean wasFound = false;
        if (toEnglish) {
            for (indexOfMatchingTerm = 0; indexOfMatchingTerm < m_arrayFrenchStyles.size(); indexOfMatchingTerm++) {
                if (m_arrayFrenchStyles.get(indexOfMatchingTerm)
                        .equalsIgnoreCase(stringToTranslate)) {
                    wasFound = true;
                    break;
                }
            }
            if (wasFound)
                return m_arrayEnglishStyles.get(indexOfMatchingTerm);
            else
                return null;
        } else {
            for (indexOfMatchingTerm = 0; indexOfMatchingTerm < m_arrayEnglishStyles.size(); indexOfMatchingTerm++) {
                if (m_arrayEnglishStyles.get(indexOfMatchingTerm).equalsIgnoreCase(
                        stringToTranslate)) {
                    wasFound = true;
                    break;
                }
            }
            if (wasFound)
                return m_arrayFrenchStyles.get(indexOfMatchingTerm);
            else
                return null;
        }
    }

    private void convertToVersion6(DbSMSProject project, DbSMSProject newProject)
            throws DbException {

        ////
        // set all DbORDataModel and Notations instances to logical mode 2 (relational)

        DbEnumeration dbEnum = project.componentTree(DbORNotation.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORNotation nota = (DbORNotation) dbEnum.nextElement();
            nota.set(DbORNotation.fNotationMode, new Integer(
                    TerminologyUtil.LOGICAL_MODE_OBJECT_RELATIONAL));
        }
        dbEnum.close();

        dbEnum = project.componentTree(DbORDataModel.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORDataModel model = (DbORDataModel) dbEnum.nextElement();
            model.setLogicalMode(new Integer(DbORDataModel.LOGICAL_MODE_OBJECT_RELATIONAL));
        }
        dbEnum.close();

        dbEnum = project.componentTree(DbBEModel.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEModel model = (DbBEModel) dbEnum.nextElement();
            model.setTerminologyName(DbBENotation.GANE_SARSON);
        }
        dbEnum.close();

        //DbEnumeration dbEnum = project.componentTree(DbORStyle.metaClass);
        dbEnum = project.componentTree(DbORStyle.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORStyle style = (DbORStyle) dbEnum.nextElement();
            style.setOr_associationDescriptorDisplay(Boolean.TRUE);
        }
        dbEnum.close();

        dbEnum = project.componentTree(DbOOStyle.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbOOStyle oostyle = (DbOOStyle) dbEnum.nextElement();
            oostyle.setOojv_associationNameDisplayed(Boolean.TRUE);
        }
        dbEnum.close();
    } //end convertToVersion6()

    /**
     * Convert Model to 2.3 (build >= 500)
     */
    private void convertModelToVersion6(DbSMSPackage smsPackage) throws DbException {

        DbSMSProject project = (DbSMSProject) smsPackage.getProject();

        DbEnumeration dbEnum = smsPackage.componentTree(DbORNotation.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORNotation nota = (DbORNotation) dbEnum.nextElement();
            nota.set(DbORNotation.fNotationMode, new Integer(
                    TerminologyUtil.LOGICAL_MODE_OBJECT_RELATIONAL));
        }
        dbEnum.close();

        if (smsPackage instanceof DbBEModel)
            ((DbBEModel) smsPackage).setTerminologyName(DbBENotation.GANE_SARSON);

        dbEnum = project.componentTree(DbORStyle.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORStyle style = (DbORStyle) dbEnum.nextElement();
            style.setOr_associationDescriptorDisplay(Boolean.TRUE);
        }
        dbEnum.close();

        dbEnum = project.componentTree(DbOOStyle.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbOOStyle oostyle = (DbOOStyle) dbEnum.nextElement();
            oostyle.setOojv_associationNameDisplayed(Boolean.TRUE);
        }
        dbEnum.close();
    } //end convertModelToVersion6()

    /**
     * Convert to 2.4 (build >= 600)
     */
    private void convertToVersion7(DbSMSProject project, DbSMSProject newProject)
            throws DbException {

        //Set terminology fields for all usecase and model nodes.
        DbEnumeration dbEnum = project.getComponents().elements(DbBEModel.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEModel model = (DbBEModel) dbEnum.nextElement();
            model.setTerminologyName(DbBENotation.GANE_SARSON);

            DbEnumeration dbEnum2 = model.getComponents().elements(DbBEUseCase.metaClass);
            while (dbEnum2.hasMoreElements()) {
                DbObject currentUseCase = dbEnum2.nextElement();
                BEUtility.updateUseCaseTerminology((DbBEUseCase) currentUseCase);
            }
            dbEnum2.close();
        }
        dbEnum.close();

        ////
        // update the timestamps for all db objects

        Long now = new Long(System.currentTimeMillis());
        project.getDb().beginWriteTrans("");
        project.setModificationTime(now);
        project.set(DbObject.fCreationTime, now);
        project.getDb().commitTrans();

        updateTimeStamps(project);

        dbEnum = project.componentTree(DbBEModel.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEModel model = (DbBEModel) dbEnum.nextElement();
            model.setTerminologyName(DbBENotation.GANE_SARSON);
        }
        dbEnum.close();

        ////
        // reset the or styles because of the font size change

        dbEnum = project.componentTree(DbORDiagram.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORDiagram diagram = (DbORDiagram) dbEnum.nextElement();
            diagram.setStyle(null);
        }
        dbEnum.close();

    }// end convertToVersion7()

    /**
     * Convert Model to 2.4 (build >= 600)
     */
    private void convertModelToVersion7(DbSMSPackage smsPackage) throws DbException {

        DbSMSProject project = (DbSMSProject) smsPackage.getProject();

        //Set terminology fields for all usecase and model nodes.

        if (smsPackage instanceof DbBEModel) {
            DbBEModel model = (DbBEModel) smsPackage;
            model.setTerminologyName(DbBENotation.GANE_SARSON);
            DbEnumeration dbEnum2 = model.getComponents().elements(DbBEUseCase.metaClass);
            while (dbEnum2.hasMoreElements()) {
                DbObject currentUseCase = dbEnum2.nextElement();
                BEUtility.updateUseCaseTerminology((DbBEUseCase) currentUseCase);
            }
            dbEnum2.close();
        }

        ////
        // update the timestamps for all db objects

        Long now = new Long(System.currentTimeMillis());
        project.getDb().beginWriteTrans("");
        project.setModificationTime(now);
        project.set(DbObject.fCreationTime, now);
        project.getDb().commitTrans();

        updateTimeStamps(smsPackage);

    }// end convertModelToVersion7()

    public void convertPre22ImportedModel(DbSMSPackage myModel) throws DbException {

        if (myModel instanceof DbBEModel)
            convertBeModel((DbBEModel) myModel);

        convertModelToVersion6(myModel);
        convertModelToVersion7(myModel);
        convertModelToVersion8(myModel);
    }

    public void convertPost22ImportedModel(DbSMSPackage myModel) throws DbException {

        convertModelToVersion6(myModel);
        convertModelToVersion7(myModel);
        convertModelToVersion8(myModel);
    }

    private void updateTimeStamps(DbObject dbo) throws DbException {
        Long now = new Long(System.currentTimeMillis());
        DbEnumeration dbObjectsEnum = dbo.getComponents().elements(DbObject.metaClass);
        while (dbObjectsEnum.hasMoreElements()) {
            DbObject object = (DbObject) dbObjectsEnum.nextElement();
            object.getDb().beginWriteTrans("");
            object.setModificationTime(now);
            object.set(DbObject.fCreationTime, now);
            object.getDb().commitTrans();
            updateTimeStamps(object);
        }
        dbObjectsEnum.close();
    }

    private void convertOldStrings(DbSMSProject project) throws DbException {

        ////
        // look for deprecated built-in strings and convert them to the current valid name.

        DbEnumeration enumNotations = project.componentTree(DbBENotation.metaClass);
        while (enumNotations.hasMoreElements()) {
            DbBENotation notation = (DbBENotation) enumNotations.nextElement();
            String name = notation.getName();
            boolean foundOldString = false;
            if (name.equals("UML - Cas d'utilisation"))
                foundOldString = true;
            else if (name.equals("UML-Cas d'utilisation"))
                foundOldString = true;
            else if (name.equals("UML-Cas d'utilisation"))
                foundOldString = true;
            else if (name.equals("UML-Diagramme de Cas d'utilisation"))
                foundOldString = true;
            if (true == foundOldString)
                notation.setName("UML-Diagramme de cas d'utilisation");
            else if (name.equals("UML Use Case"))
                notation.setName("UML Use Case Diagram");
            else if (name.equals("UML State Diagram"))
                notation.setName("UML Statechart Diagram");
        }
        enumNotations.close();

        enumNotations = project.componentTree(DbBENotation.metaClass);
        while (enumNotations.hasMoreElements()) {
            DbBENotation notation = (DbBENotation) enumNotations.nextElement();
            String name = notation.getTerminologyName();
            if (name != null) {
                boolean foundOldString = false;
                if (name.equals("UML - Cas d'utilisation"))
                    foundOldString = true;
                else if (name.equals("UML-Cas d'utilisation"))
                    foundOldString = true;
                else if (name.equals("UML-Cas d'utilisation"))
                    foundOldString = true;
                else if (name.equals("UML-Diagramme de Cas d'utilisation"))
                    foundOldString = true;
                if (true == foundOldString)
                    notation.setTerminologyName("UML-Diagramme de cas d'utilisation");
                else if (name.equals("UML Use Case"))
                    notation.setTerminologyName("UML Use Case Diagram");
                else if (name.equals("UML State Diagram"))
                    notation.setTerminologyName("UML Statechart Diagram");
            }
        }
        enumNotations.close();

        enumNotations = project.componentTree(DbBEModel.metaClass);
        while (enumNotations.hasMoreElements()) {
            DbBEModel model = (DbBEModel) enumNotations.nextElement();
            String name = model.getTerminologyName();
            if (name != null) {
                boolean foundOldString = false;
                if (name.equals("UML - Cas d'utilisation"))
                    foundOldString = true;
                else if (name.equals("UML-Cas d'utilisation"))
                    foundOldString = true;
                else if (name.equals("UML-Diagramme de Cas d'utilisation"))
                    foundOldString = true;
                if (true == foundOldString)
                    model.setTerminologyName("UML-Diagramme de cas d'utilisation");
                else if (name.equals("UML Use Case"))
                    model.setTerminologyName("UML Use Case Diagram");
                else if (name.equals("UML State Diagram"))
                    model.setTerminologyName("UML Statechart Diagram");
            }
        }
        enumNotations.close();

        enumNotations = project.componentTree(DbBEUseCase.metaClass);
        while (enumNotations.hasMoreElements()) {
            DbBEUseCase useCase = (DbBEUseCase) enumNotations.nextElement();
            String name = useCase.getTerminologyName();
            if (name != null) {
                boolean foundOldString = false;
                if (name.equals("UML - Cas d'utilisation"))
                    foundOldString = true;
                else if (name.equals("UML-Cas d'utilisation"))
                    foundOldString = true;
                else if (name.equals("UML-Cas d'utilisation"))
                    foundOldString = true;
                else if (name.equals("UML-Diagramme de Cas d'utilisation"))
                    foundOldString = true;
                if (true == foundOldString)
                    useCase.setTerminologyName("UML-Diagramme de cas d'utilisation");
                else if (name.equals("UML Use Case"))
                    useCase.setTerminologyName("UML Use Case Diagram");
                else if (name.equals("UML State Diagram"))
                    useCase.setTerminologyName("UML Statechart Diagram");
            }
        }
        enumNotations.close();

        enumNotations = project.componentTree(DbBEStyle.metaClass);
        while (enumNotations.hasMoreElements()) {
            DbBEStyle style = (DbBEStyle) enumNotations.nextElement();
            String name = style.getName();
            if (name.equals("UML Cas d'utilisation")) {
                style.setName("Cas d'utilisation UML");
                break;
            }
        }
        enumNotations.close();

    }

    /**
     * Convert to 2.5 (build >= 700)
     */
    private void convertToVersion8(DbSMSProject project, DbSMSProject newProject)
            throws DbException {

        convertOldStrings(project);

        DbEnumeration dbTypeEnum = null;
        ////
        // set all locked attributes on DBSMSAbstractPackage(DbORModel, DbSMSBuiltInTypePackage and DbOOAbsPackage) 
        //and DbBEModel to false,  this has been disable in version 8(2.5) since it causes problems
        // and we don't want this functionnality just yet in the application,
        // it will be used for the Enterprise Manager

        DbEnumeration dbObjectsEnum = project.getComponents().elements(DbSMSPackage.metaClass);
        while (dbObjectsEnum.hasMoreElements()) {
            DbSMSPackage smsPackage = (DbSMSPackage) dbObjectsEnum.nextElement();
            if (smsPackage instanceof DbSMSAbstractPackage)
                ((DbSMSAbstractPackage) smsPackage).setIsLocked(Boolean.FALSE);
            else if (smsPackage instanceof DbBEModel)
                ((DbBEModel) smsPackage).setIsLocked(Boolean.FALSE);
        }
        dbObjectsEnum.close();

        DbEnumeration dbTargetEnum = project.getComponents().elements(DbSMSTargetSystem.metaClass);
        while (dbTargetEnum.hasMoreElements()) {
            DbSMSTargetSystem target = (DbSMSTargetSystem) dbTargetEnum.nextElement();
            int m_rootID = target.getRootID().intValue();
            DbSMSBuiltInTypePackage builtInTypePackage = target.getBuiltInTypePackage();
            if (m_rootID < 999 || m_rootID == 2000) {
                target.setBuiltIn(Boolean.TRUE);
                builtInTypePackage.setBuiltIn(Boolean.TRUE);
                dbTypeEnum = builtInTypePackage.getComponents().elements(DbORBuiltInType.metaClass);
                while (dbTypeEnum.hasMoreElements()) {
                    DbORBuiltInType type = (DbORBuiltInType) dbTypeEnum.nextElement();
                    type.setBuiltIn(Boolean.TRUE);
                }
                dbTypeEnum.close();
            }
        }
        dbTargetEnum.close();

        ////
        // new MasterNotationID on DbSMSNotation
        // this field must be set to the notation ID  from which it was created or is related now
        // new notation for this version will be all setm but we must set this value correctly for notation of the previous version

        // for all notation in the old project
        // use the ID of the notation to set the master ID
        // set user notations to gane sarson

        dbObjectsEnum = project.componentTree(DbBENotation.metaClass);
        while (dbObjectsEnum.hasMoreElements()) {
            DbBENotation oldNotation = (DbBENotation) dbObjectsEnum.nextElement();
            int notationId = oldNotation.getNotationID().intValue();
            if (notationId >= DbInitialization.DATARUN_BPM
                    && notationId <= DbInitialization.OBJECT_LIFE_CYCLE)
                oldNotation.setMasterNotationID(new Integer(notationId));
            else
                oldNotation.setMasterNotationID(new Integer(DbInitialization.GANE_SARSON));

            if (oldNotation.getDisplayFrameBox() == null)
                oldNotation.setDisplayFrameBox(Boolean.TRUE);
            if (oldNotation.getHasFrame() == null)
                oldNotation.setHasFrame(Boolean.FALSE);
        }
        dbObjectsEnum.close();

        //Set terminology fields for all usecase and model nodes.
        DbEnumeration dbEnum = project.componentTree(DbBEModel.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEModel model = (DbBEModel) dbEnum.nextElement();

            String retVal = model.getTerminologyName();
            if (retVal == null)
                model.setTerminologyName(DbBENotation.GANE_SARSON);
            else if (retVal.equals(""))
                model.setTerminologyName(DbBENotation.GANE_SARSON);

            DbEnumeration dbEnum2 = model.componentTree(DbBEUseCase.metaClass);
            while (dbEnum2.hasMoreElements()) {
                DbObject currentUseCase = dbEnum2.nextElement();
                BEUtility.updateUseCaseTerminology((DbBEUseCase) currentUseCase, false);
            }
            dbEnum2.close();
        }
        dbEnum.close();

        Long now = new Long(System.currentTimeMillis());
        project.setModificationTime(now);
        project.set(DbObject.fCreationTime, now);

    }

    /**
     * Convert to 3.1 (build >= 907)
     */
    private void convertToVersion14(DbSMSProject project) throws DbException {
    }
    
    /**
     * Convert to 3.2 (build >= 950)
     */
    private void convertToVersion15(DbSMSProject project) throws DbException {
        //for each OR style
        DbEnumeration dbEnum = project.getComponents().elements(DbORStyle.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORStyle style = (DbORStyle) dbEnum.nextElement();
            
            //add style element for choice/spec
            Color color = style.getBackgroundColorDbORChoiceOrSpecialization();
            if (color == null) {
                color = style.getBackgroundColorDbORTable();
                style.setBackgroundColorDbORChoiceOrSpecialization(color);
            }
            
            color = style.getLineColorDbORChoiceOrSpecialization(); 
            if (color == null) {
                color = style.getLineColorDbORTable();
                style.setLineColorDbORChoiceOrSpecialization(color);
            }
            
            //set default color for graphical link, if not already set
            color = style.getLineColorDbSMSGraphicalLink(); 
            if (color == null) {
            	color = style.getLineColorDbSMSAssociation();
            	style.setLineColorDbSMSGraphicalLink(color);
            }
        }
        dbEnum.close();
    }

    private DbSMSStyle createDefaultStyle(DbSMSStyle style) throws DbException {
        DbSMSStyle temp = null;
        if (style instanceof DbORStyle) {
            temp = new DbORStyle();
        } else if (style instanceof DbBEStyle) {
            temp = new DbBEStyle();
        } else if (style instanceof DbOOStyle) {
            temp = new DbOOStyle();
        } else if (style instanceof DbORCommonItemStyle) {
            temp = new DbORCommonItemStyle();
        } else if (style instanceof DbORDomainStyle) {
            temp = new DbORDomainStyle();
        }
        return temp;
    }

    /**
     * Convert Model to 2.5 (build >= 700)
     */
    private void convertModelToVersion8(DbSMSPackage smsPackage) throws DbException {

        ////
        // set all locked attributes on DBSMSAbstractPackage(DbORModel, DbSMSBuiltInTypePackage and DbOOAbsPackage) 
        // and DbBEModel to false,  this has been disabled in version 8(2.5) since it causes problems
        // and we don't want this functionnality just yet in the application,
        // it will be used when SMS becomes client to the Enterprise Repository 

        if (smsPackage instanceof DbSMSAbstractPackage)
            ((DbSMSAbstractPackage) smsPackage).setIsLocked(Boolean.FALSE);
        else if (smsPackage instanceof DbBEModel)
            ((DbBEModel) smsPackage).setIsLocked(Boolean.FALSE);

        ////
        // new MasterNotationID on DbSMSNotation
        // this field must be set to the notation ID  from which it was created or is related now
        // new notation for this version will be all setm but we must set this value correctly for notation of the previous version

        // for all notation in the old project
        // use the ID of the notation to set the master ID
        // set user notations to gane sarson

        DbEnumeration dbObjectsEnum = smsPackage.componentTree(DbBENotation.metaClass);
        while (dbObjectsEnum.hasMoreElements()) {
            DbBENotation oldNotation = (DbBENotation) dbObjectsEnum.nextElement();
            int notationId = oldNotation.getNotationID().intValue();
            if (oldNotation.getDisplayFrameBox() == null)
                oldNotation.setDisplayFrameBox(Boolean.TRUE);
            if (oldNotation.getHasFrame() == null)
                oldNotation.setHasFrame(Boolean.FALSE);
            if (notationId >= DbInitialization.DATARUN_BPM
                    && notationId <= DbInitialization.OBJECT_LIFE_CYCLE)
                oldNotation.setMasterNotationID(new Integer(notationId));
            else
                oldNotation.setMasterNotationID(new Integer(DbInitialization.GANE_SARSON));
        }
        dbObjectsEnum.close();

        if (smsPackage instanceof DbBEModel) {
            DbBEModel model = (DbBEModel) smsPackage;
            String retVal = model.getTerminologyName();
            if (retVal == null)
                model.setTerminologyName(DbBENotation.GANE_SARSON);
            else if (retVal.equals(""))
                model.setTerminologyName(DbBENotation.GANE_SARSON);

            DbEnumeration dbEnum2 = model.componentTree(DbBEUseCase.metaClass);
            while (dbEnum2.hasMoreElements()) {
                DbObject currentUseCase = dbEnum2.nextElement();
                BEUtility.updateUseCaseTerminology((DbBEUseCase) currentUseCase, false);
            }
            dbEnum2.close();
        }
    }

    public DbSMSStyle getStyleForBENotation(DbSMSProject project, DbBENotation notation)
            throws DbException {

        DbSMSStyle style = null;
        int notationId = notation.getNotationID().intValue();
        switch (notationId) {
        case DbInitialization.UML_USE_CASE: {
            style = (DbSMSStyle) project.findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.UML_USE_CASE_STYLE_NAME);
        }
            break;
        case DbInitialization.UML_ACTIVITY_DIAGRAM: {
            style = (DbSMSStyle) project.findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.UML_ACTIVITY_STYLE_NAME);
        }
            break;
        case DbInitialization.UML_COLLABORATION_DIAGRAM: {
            style = (DbSMSStyle) project.findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.UML_COLLABORATION_STYLE_NAME);
        }
            break;
        case DbInitialization.UML_COMPONENT_DIAGRAM: {
            style = (DbSMSStyle) project.findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.UML_COMPONENT_STYLE_NAME);
        }
            break;
        case DbInitialization.UML_DEPLOYMENT_DIAGRAM: {
            style = (DbSMSStyle) project.findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.UML_DEPLOYMENT_STYLE_NAME);
        }
            break;
        case DbInitialization.UML_SEQUENCE_DIAGRAM: {
            style = (DbSMSStyle) project.findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.UML_SEQUENCE_STYLE_NAME);
        }
            break;
        case DbInitialization.UML_STATE_DIAGRAM: {
            style = (DbSMSStyle) project.findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.UML_STATE_STYLE_NAME);
        }
            break;
        default: {
            style = (DbSMSStyle) project.findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.DEFAULT_STYLE_NAME);
        }
            break;
        }
        return style;
    }

    private void markBuiltItNotations(DbSMSProject project, DbSMSProject newProject)
            throws DbException {

        //unset the built in flag for all be notations and update notation ID's 

        DbEnumeration dbEnum = project.componentTree(DbBENotation.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBENotation notation = (DbBENotation) dbEnum.nextElement();
            if (notation.isBuiltIn()) {
                notation.setBuiltIn(Boolean.FALSE);
                notation.setNotationID(new Integer(getCurrentVersion() * 100000
                        + notation.getNotationID().intValue()));
            } else
                notation.setName(notation.getName() + STR_CUSTOM_IMPORTED);
        }
        dbEnum.close();

        project.setBeDefaultNotation(null);
        DbInitialization.initBeNotations(project);

        //transfer diagram notations from old version to new version if it exists 
        dbEnum = project.componentTree(DbBENotation.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBENotation notation = (DbBENotation) dbEnum.nextElement();
            if (!notation.isBuiltIn()) {
                DbBENotation newNotation = findBENotation(project, notation.getNotationID());
                if (newNotation != null) {
                    int notationId = newNotation.getNotationID().intValue();
                    DbEnumeration dbNotationEnum = notation.getDiagrams().elements();
                    while (dbNotationEnum.hasMoreElements()) {
                        DbBEDiagram diagram = (DbBEDiagram) dbNotationEnum.nextElement();
                        diagram.setNotation(newNotation);
                        DbBEStyle style = (DbBEStyle) diagram.getStyle();
                        if (style != null)
                            if (style.isDefaultStyle() && this.oldVersion > 6)
                                if (notationId >= 13 && notationId <= 19)
                                    diagram.setStyle(getStyleForBENotation(project, newNotation));
                    }
                    dbNotationEnum.close();
                }
                notation.setName(notation.getName() + " " + FROM_VERSION + oldDisplayVersion + ")");
            }
        }
        dbEnum.close();

        project.setBeDefaultNotation((DbBENotation) project.findComponentByName(
                DbBENotation.metaClass, DbBENotation.GANE_SARSON));

        /* ORNOTATIONS DO NOT HAVE THE BUILT-IN FLAG SET IN 2.2 */

        //unset the built in flag for all or notations and update notation ID's 
        dbEnum = project.componentTree(DbORNotation.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORNotation notation = (DbORNotation) dbEnum.nextElement();
            if (notation.isBuiltIn()) {
                notation.setBuiltIn(Boolean.FALSE);
                notation.setNotationID(new Integer(getCurrentVersion() * 100000
                        + notation.getNotationID().intValue()));
            } else
                notation.setName(notation.getName() + STR_CUSTOM_IMPORTED);
        }
        dbEnum.close();

        project.setOrDefaultNotation(null);
        DbInitialization.initOrNotations(project);

        //transfer diagram notations from old version to new version if it exists 
        dbEnum = project.componentTree(DbORNotation.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORNotation notation = (DbORNotation) dbEnum.nextElement();
            if (!notation.isBuiltIn()) {
                DbORNotation newNotation = findORNotation(project, notation.getNotationID());
                if (newNotation != null) {
                    DbEnumeration dbNotationEnum = notation.getDiagrams().elements();
                    while (dbNotationEnum.hasMoreElements()) {
                        DbORDiagram diagram = (DbORDiagram) dbNotationEnum.nextElement();
                        diagram.setNotation(newNotation);
                    }
                    dbNotationEnum.close();
                }
                notation.setName(notation.getName() + " " + FROM_VERSION + oldDisplayVersion + ")");
            }
        }
        dbEnum.close();
    }

    private DbBENotation findBENotation(DbSMSProject project, Integer notationID)
            throws DbException {
        int trueId = notationID.intValue() - (getCurrentVersion() * 100000);
        DbEnumeration dbEnum = project.componentTree(DbBENotation.metaClass);
        DbBENotation notation = null;
        boolean found = false;
        while (dbEnum.hasMoreElements()) {
            notation = (DbBENotation) dbEnum.nextElement();
            if (trueId == notation.getNotationID().intValue()) {
                found = true;
                break;
            }
        }
        dbEnum.close();

        return (found) ? notation : null;
    }

    private DbORNotation findORNotation(DbSMSProject project, Integer notationID)
            throws DbException {
        int trueId = notationID.intValue() - (getCurrentVersion() * 100000);
        DbEnumeration dbEnum = project.componentTree(DbORNotation.metaClass);
        DbORNotation notation = null;
        boolean found = false;
        while (dbEnum.hasMoreElements()) {
            notation = (DbORNotation) dbEnum.nextElement();
            if (trueId == notation.getNotationID().intValue()) {
                found = true;
                break;
            }
        }
        dbEnum.close();
        return (found) ? notation : null;
    }

    private void markBuiltItStyles(DbSMSProject project, DbSMSProject newProject)
            throws DbException {
        DbEnumeration dbEnum = project.componentTree(DbBEStyle.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEStyle style = (DbBEStyle) dbEnum.nextElement();
            style.setName(style.getName() + " " + FROM_VERSION + oldDisplayVersion + ")");
            style.setDefaultStyle(Boolean.FALSE);
        }
        dbEnum = project.componentTree(DbORStyle.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORStyle style = (DbORStyle) dbEnum.nextElement();
            style.setName(style.getName() + " " + FROM_VERSION + oldDisplayVersion + ")");
            style.setDefaultStyle(Boolean.FALSE);
        }
        dbEnum = project.componentTree(DbOOStyle.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbOOStyle style = (DbOOStyle) dbEnum.nextElement();
            style.setName(style.getName() + " " + FROM_VERSION + oldDisplayVersion + ")");
            style.setDefaultStyle(Boolean.FALSE);
        }
    }

    private void convertProjectOrPackageContent(DbSemanticalObject container) throws DbException {
        //for each BE model
        DbRelationN relN = container.getComponents();
        DbEnumeration dbEnum = relN.elements(DbBEModel.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEModel model = (DbBEModel) dbEnum.nextElement();
            convertBeModel(model);
        } //end while
        dbEnum.close();

        //for each user-defined package
        dbEnum = relN.elements(DbSMSUserDefinedPackage.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSUserDefinedPackage pack = (DbSMSUserDefinedPackage) dbEnum.nextElement();
            convertProjectOrPackageContent(pack);
        } //end while
        dbEnum.close();
    } //end convertBeModel()

    private void convertBeModel(DbBEModel model) throws DbException {
        //for each context
        DbRelationN relN = model.getComponents();
        DbEnumeration dbEnum = relN.elements(DbBEUseCase.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEUseCase usecase = (DbBEUseCase) dbEnum.nextElement();
            convertBeProcess(usecase);
        } //end while
        dbEnum.close();
    } //end convertBeModel()

    private void convertBeProcess(DbBEUseCase process) throws DbException {
        //for each subprocess
        DbRelationN relN = process.getComponents();
        DbEnumeration dbEnum = relN.elements(DbBEUseCase.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEUseCase usecase = (DbBEUseCase) dbEnum.nextElement();
            convertBeProcess(usecase);
        } //end while
        dbEnum.close();

        //for each diagram
        dbEnum = relN.elements(DbBEDiagram.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEDiagram diagram = (DbBEDiagram) dbEnum.nextElement();
            convertDiagram(diagram);
        } //end while
        dbEnum.close();

        //for each usecase qualifier
        dbEnum = relN.elements(DbBEUseCaseQualifier.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEUseCaseQualifier usercaseQualifier = (DbBEUseCaseQualifier) dbEnum.nextElement();
            String rate = usercaseQualifier.getRate();
            if (rate != null) {
                double rate2;
                try {
                    rate2 = Double.parseDouble(rate);
                } catch (NumberFormatException ex) {
                    rate2 = 0.0;
                } //end try

                usercaseQualifier.setRate2(new Double(rate2));
            }
        } //end while
        dbEnum.close();
    } //end convertBeProcess()

    private void convertDiagram(DbBEDiagram diagram) throws DbException {
        //for each context frame
        DbRelationN relN = diagram.getComponents();
        DbEnumeration dbEnum = relN.elements(DbBEContextGo.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEContextGo contextGo = (DbBEContextGo) dbEnum.nextElement();
            convertBeContextGo(contextGo);
        } //end while
        dbEnum.close();

    } //end convertDiagram() 

    private void convertBeContextGo(DbBEContextGo contextGo) throws DbException {
        //for each context cell
        DbRelationN relN = contextGo.getComponents();
        DbEnumeration dbEnum = relN.elements(DbBEContextCell.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEContextCell cell = (DbBEContextCell) dbEnum.nextElement();
            int horizontal = cell.getHorizontalJustification().intValue();
            int vertical = cell.getVerticalJustification().intValue();
            SMSHorizontalAlignment hAlign = DbInitialization
                    .convertToHorizontalAlignment(horizontal);
            SMSVerticalAlignment vAlign = DbInitialization.convertToVerticalAlignment(vertical);
            cell.setHorizontalJustification2(hAlign);
            cell.setVerticalJustification2(vAlign);
        } //end while
        dbEnum.close();
    } //end convertBeContextGo()

    @Override
    public int getCurrentDistributorID() {
        return DISTRIBUTOR;
    }

    @Override
    public int getCurrentReleaseStatus() {
        return RELEASE_STATUS;
    }

    @Override
    public void setOldDistributorID(int oldDistributorID) {
        this.oldDistributorID = oldDistributorID;
    }

    @Override
    public void setOldReleaseStatus(int oldReleaseStatus) {
        this.oldReleaseStatus = oldReleaseStatus;
    }

} //end SMSVersionConverter

