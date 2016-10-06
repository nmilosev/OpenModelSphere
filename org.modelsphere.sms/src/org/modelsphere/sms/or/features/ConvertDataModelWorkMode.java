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

package org.modelsphere.sms.or.features;

import java.awt.Polygon;
import java.util.ArrayList;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.gui.task.DefaultController;
import org.modelsphere.jack.gui.task.Worker;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.explorer.Explorer;
import org.modelsphere.jack.srtool.graphic.DbGraphic;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.SMSDeepCopyCustomizer;
import org.modelsphere.sms.SMSToolkit;
import org.modelsphere.sms.db.DbSMSClassifier;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSStyle;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.db.util.SMSMove;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORAssociationGo;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORNotation;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTableGo;
import org.modelsphere.sms.or.db.DbORView;
import org.modelsphere.sms.or.db.srtypes.ORConstraintType;
import org.modelsphere.sms.or.db.srtypes.ORValidationRule;
import org.modelsphere.sms.or.db.util.AnyORObject;
import org.modelsphere.sms.or.generic.db.DbGEColumn;
import org.modelsphere.sms.or.generic.db.DbGETable;
import org.modelsphere.sms.or.graphic.tool.ORAssociationTool;
import org.modelsphere.sms.or.international.LocaleMgr;

final class ConvertDataModelWorkMode {
    //static final int GENERATE_REPORT = 0;
    //static final int CONVERT_MODEL = 1;
    // conversion options
    static final int OR_TO_ER = 1;
    static final int ER_TO_OR_ABS = 2;
    static final int ER_TO_OR_NOABS = 3;

    private static final String SUCCESS_MSG = LocaleMgr.message
            .getString("DataModelSuccessfullyConverted");
    private static final String kConverted = LocaleMgr.screen.getString("converted");
    private static final String kReConverted = LocaleMgr.screen.getString("reConverted");
    private static final String kKeyUniqueKeyName = LocaleMgr.misc.getString("uniqueKey");
    private static final String kKeyPrimaryUniqueKeyName = LocaleMgr.misc
            .getString("primaryUniqueKey");

    //Messages du Worker
    private static final String wMsgConversionOption = LocaleMgr.message
            .getString("msgConversionOption");//1
    private static final String wMsgSubModelOption = LocaleMgr.message
            .getString("msgSubModelOption");
    private static final String wMsgCurrentModelOption = LocaleMgr.message
            .getString("msgCurrentModelOption");
    private static final String wMsgNotationOption = LocaleMgr.message
            .getString("msgNotationOption");
    private static final String wMsgAbsorptionOption = LocaleMgr.message
            .getString("msgAbsorptionOption");//5
    private static final String wMsgNoAbsorptionOption = LocaleMgr.message
            .getString("msgNoAbsorptionOption");
    private static final String wMsgModelSeparator = LocaleMgr.message
            .getString("msgModelSeparator");
    private static final String wMsgConvertingTo = LocaleMgr.message.getString("msgConvertingTo");
    private static final String wMsgHidenView = LocaleMgr.message.getString("msgHidenView");
    private static final String wMsgNoAssocWithView = LocaleMgr.message
            .getString("msgNoAssocWithView");//10
    private static final String wMsgErAssocCreation = LocaleMgr.message
            .getString("msgErAssocCreation");
    private static final String wMsgDependKeeped = LocaleMgr.message.getString("msgDependKeeped");
    private static final String wMsgDependNOTKeeped = LocaleMgr.message
            .getString("msgDependNOTKeeped");
    private static final String wMsgDependTernaryNOTKeeped = LocaleMgr.message
            .getString("msgDependTernaryNOTKeeped");

    private static final String wMsgNoneNavigable = LocaleMgr.message.getString("msgNoneNavigable");
    private static final String wMsgNavigableIsMaxN = LocaleMgr.message
            .getString("msgNavigableIsMaxN");
    private static final String wMsgAssocAbsorbed = LocaleMgr.message.getString("msgAssocAbsorbed");//15
    private static final String wMsgAttribAbsorbed = LocaleMgr.message
            .getString("msgAttribAbsorbed");
    private static final String wMsgNullPosAttribute = LocaleMgr.message
            .getString("msgNullPosAttribute");
    private static final String wMsgIntersectionTable = LocaleMgr.message
            .getString("msgIntersectionTable");
    private static final String wMsgPKDependAddedOn = LocaleMgr.message
            .getString("msgPKDependAddedOn");
    private static final String wMsgPKDependAll = LocaleMgr.message.getString("msgPKDependAll");//20
    private static final String wMsgPKDependOptional = LocaleMgr.message
            .getString("msgPKDependOptional");
    private static final String wMsgModelNotValidated = LocaleMgr.message
            .getString("msgModelNotValidated");
    private static final String wMsgKeyremoved = LocaleMgr.message.getString("msgKeyremoved");

    private DbObject m_semObjToConvert;
    private StringBuffer m_stringBuffer;
    private String m_message = SUCCESS_MSG;
    private int m_counter = 0;
    private boolean m_withSubModels;
    private String m_destinationNotation;
    private int m_convertOption;
    private String m_title;

    //constructor (called by ConvertDataModelWorkModeDialog)
    ConvertDataModelWorkMode(DbObject semObjToConvert, boolean withSubmodels,
            String destinationNotation, int convertOption, String title) {
        m_semObjToConvert = semObjToConvert;
        m_stringBuffer = new StringBuffer();
        m_withSubModels = withSubmodels;
        m_destinationNotation = destinationNotation;
        m_convertOption = convertOption;
        m_title = title;
    }

    //entry points (called by ConvertDataModelWorkModeDialog)
    void execute() throws DbException {
        ConvertDataModelWorker worker = new ConvertDataModelWorker(m_semObjToConvert,
                m_withSubModels, m_destinationNotation, m_convertOption, m_title, m_stringBuffer);
        DefaultController controller = new DefaultController(m_title, false, null);
        controller.start(worker);
        m_counter = worker.getCounter();
    } //end execute()

    String getReport() {
        String report = m_stringBuffer.toString();
        return report;
    } //end generateReport()

    String getMessage() {
        return m_message;
    } //end getMessage()

    // int getResult() {
    //    return m_result;
    // } //end getResult()

    int getCounter() {
        return m_counter;
    } //end getResult()

    ///////////////////////////////////////
    // INNER CLASSES
    ///////////////////////////////////////

    //
    // ConvertDataModelWorker
    //
    private static class ConvertDataModelWorker extends Worker {
        private String m_title;
        private DbObject m_semObjToConvert;
        private boolean m_withSubModels;
        private int m_convertOption;
        private String m_destinationNotation;
        private String msg;
        private int m_counter = 0;
        boolean m_absorptionRequired = false;
        boolean m_absorptionToDo = false;
        boolean m_childExactlyOneExist = false;
        boolean m_parentExactlyOneExist = false;
        boolean m_parentOptionalExist = false;
        boolean m_allParentMaxN = false;
        private ArrayList m_OriginModelList = new ArrayList();
        private int m_subModelIndex = 0;

        public ConvertDataModelWorker(DbObject semObjToConvert, boolean withSubmodels,
                String destinationNotation, int convertOption, String title,
                StringBuffer stringBuffer) {
            m_semObjToConvert = semObjToConvert;
            m_withSubModels = withSubmodels;
            m_destinationNotation = destinationNotation;
            m_convertOption = convertOption;
            m_title = title;
        }

        // Return this job's title
        protected String getJobTitle() {
            return m_title;
        }

        protected void runJob() throws Exception {
            m_semObjToConvert.getDb().beginWriteTrans("ConvertDataModelWorker");
            convertDataModel(m_semObjToConvert);
            if (!getController().checkPoint()) //if user has CANCELled
                m_semObjToConvert.getDb().abortTrans();
            else
                m_semObjToConvert.getDb().commitTrans();
        } //end runJob()

        int getCounter() {
            return m_counter;
        } //end getCounter()

        //
        // Private methods
        //

        private void convertDataModel(DbObject srcModel) throws DbException {
            DbProject project = srcModel.getProject();
            DbObject composite = srcModel.getComposite();

            //Deepcopy the whole data model (and its submodels)
            getController().println();
            getController().println(wMsgConversionOption);

            if (m_withSubModels)
                getController().println("   - " + wMsgSubModelOption);
            else
                getController().println("   - " + wMsgCurrentModelOption);

            msg = MessageFormat.format(wMsgNotationOption, new Object[] { m_destinationNotation });
            getController().println("   - " + msg);
            if (m_convertOption == ConvertDataModelWorkMode.ER_TO_OR_ABS)
                getController().println("   - " + wMsgAbsorptionOption);
            else if (m_convertOption == ConvertDataModelWorkMode.ER_TO_OR_NOABS)
                getController().println("   - " + wMsgNoAbsorptionOption);

            getController().println();
            DbObject[] srcDbos = new DbObject[] { srcModel };
            ((DbORDataModel) srcModel).setIsLocked(Boolean.FALSE);
            DbObject[] destObjs = DbObject.deepCopy(srcDbos, project, new SMSDeepCopyCustomizer(
                    project, composite), false);//composite du modèle converti est toujours le projet 
            DbORDataModel convertedModel = (DbORDataModel) destObjs[0];

            //Si modèle courant seulement
            if (!m_withSubModels)
                removeSubModels(convertedModel);

            //Changer le formalisme et le style des diagrammes convertis
            setConvertedMode(convertedModel);

            // Convertir les associations du modèle
            getController().println(wMsgModelSeparator);
            msg = MessageFormat.format(wMsgConvertingTo, new Object[] { srcModel.getName(),
                    convertedModel.getName() });
            getController().println(msg);
            if (!((DbORDataModel) srcModel).isIsValidated())
                getController().println(wMsgModelNotValidated);
            getController().println();
            if (m_convertOption == ConvertDataModelWorkMode.OR_TO_ER)
                convertORRelationship(convertedModel);
            else
                convertERRelationship(convertedModel);

            m_OriginModelList.clear();
            m_subModelIndex = 0;
        } //end convertDataModel()

        private void removeSubModels(DbORDataModel convertedModel) throws DbException {
            DbRelationN relN = convertedModel.getComponents();
            DbEnumeration dbEnum = null;
            dbEnum = relN.elements(DbORDataModel.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbORDataModel subModel = (DbORDataModel) dbEnum.nextElement();
                subModel.remove();
            }
            dbEnum.close();
        }

        private void removeViewGOs(DbORDataModel model) throws DbException {
            DbEnumeration dbEnum = null;
            dbEnum = model.getComponents().elements(DbORView.metaClass);
            if (dbEnum.hasMoreElements()) {
                getController().println(wMsgHidenView);
            }
            while (dbEnum.hasMoreElements()) {
                DbORView view = (DbORView) dbEnum.nextElement();
                getController().println(view.getName());
                DbEnumeration dbEnum1 = null;
                dbEnum1 = view.getClassifierGos().elements();
                while (dbEnum1.hasMoreElements()) {
                    DbORTableGo viewGO = (DbORTableGo) dbEnum1.nextElement();
                    viewGO.remove();
                }
                dbEnum1.close();
            }
            dbEnum.close();
            getController().println();
        }

        private void setConvertedMode(DbORDataModel convertedModel) throws DbException {
            DbORNotation notation = null;
            DbProject project = convertedModel.getProject();

            // Changer le mode du modèle courant
            if (m_convertOption == ConvertDataModelWorkMode.OR_TO_ER)
                convertedModel.setLogicalMode(new Integer(
                        DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP));
            else
                convertedModel.setLogicalMode(new Integer(
                        DbORDataModel.LOGICAL_MODE_OBJECT_RELATIONAL));

            //Appliquer le suffixe s'il y lieu
            String modelName = convertedModel.getName();
            m_OriginModelList.add(convertedModel);
            boolean converted = convertedModel.isIsConverted();
            if (!converted) {
                convertedModel.setName(modelName + " (" + kConverted + ")");
            } else if ((modelName.endsWith(kConverted + ")"))
                    && (!(modelName.endsWith(kReConverted + ")")))) {
                String newName = modelName.replaceFirst(kConverted, kReConverted);
                convertedModel.setName(newName);
            }

            // Changer le formalisme et le style des diagrammes 
            notation = (DbORNotation) project.findComponentByName(DbORNotation.metaClass,
                    m_destinationNotation);
            DbEnumeration dbEnum1 = null;
            dbEnum1 = convertedModel.getComponents().elements(DbORDiagram.metaClass);
            while (dbEnum1.hasMoreElements()) {
                DbORDiagram diagram = (DbORDiagram) dbEnum1.nextElement();
                diagram.setNotation(notation);
                DbSMSStyle defaultStyle = notation.getDefaultStyle();
                /*
                 * if(convertedModel.getLogicalMode().intValue() ==
                 * DbORDataModel.LOGICAL_MODE_OBJECT_RELATIONAL) defaultStyle =
                 * (DbORStyle)project.getOrDefaultStyle(); else defaultStyle =
                 * (DbORStyle)project.getErDefaultStyle();
                 */
                diagram.setStyle(defaultStyle);
            }
            dbEnum1.close();

            // Traiter les sous-modèles
            if (m_withSubModels) {
                DbEnumeration enum2 = null;
                enum2 = convertedModel.getComponents().elements(DbORDataModel.metaClass);
                while (enum2.hasMoreElements()) {
                    DbORDataModel subModel = (DbORDataModel) enum2.nextElement();
                    setConvertedMode(subModel);
                }
                enum2.close();
            }
        }

        /*
         * Conversion des modèles relationnels en modèles conceptuels
         */
        private void convertORRelationship(DbORDataModel model) throws DbException {
            ArrayList assocOriginList = new ArrayList();
            ArrayList assocWithViewList = new ArrayList();
            DbORAssociation originAssoc = null;
            DbORAssociation viewAssoc = null;
            DbORAssociationGo originAssocGo = null;
            DbORAbsTable srcClassifier = null;
            DbSMSClassifierGo srcClassifierGO = null;
            DbORAbsTable destClassifier = null;
            DbSMSClassifierGo destClassifierGO = null;
            DbORTable assocClassifier = null;
            DbSMSClassifierGo assocClassifierGO = null;
            DbSMSClassifier tempClassifier = null;
            DbORAssociationEnd srcEnd = null;
            DbORAssociationEnd dstEnd = null;
            DbORAssociationEnd srcArcEnd = null;
            DbORAssociationEnd dstArcEnd = null;
            SMSMultiplicity srcBackMult;
            SMSMultiplicity destBackMult;
            SMSMultiplicity hideMult = SMSMultiplicity.getInstance(SMSMultiplicity.UNDEFINED);

            //les colonnes des clés étrangères sont détruites 
            //puisque les clés sont détruites avec les associations d'origines
            DbEnumeration tabEnum = null;
            tabEnum = model.getComponents().elements(DbGETable.metaClass);
            while (tabEnum.hasMoreElements()) {
                DbGETable table = (DbGETable) tabEnum.nextElement();
                DbEnumeration fkColEnum = null;
                fkColEnum = table.getComponents().elements(DbGEColumn.metaClass);
                while (fkColEnum.hasMoreElements()) {
                    DbGEColumn column = (DbGEColumn) fkColEnum.nextElement();
                    if (column.isForeign())
                        column.remove();
                }
                fkColEnum.close();
            }
            tabEnum.close();

            DbEnumeration dbEnum = null;
            dbEnum = model.getComponents().elements(DbORAssociation.metaClass);
            //Les associations d'origines sont misent dans un array sinon on boucle sans fin en en créant des nouvelles
            while (dbEnum.hasMoreElements()) {
                originAssoc = (DbORAssociation) dbEnum.nextElement();
                srcClassifier = originAssoc.getFrontEnd().getClassifier();
                destClassifier = originAssoc.getBackEnd().getClassifier();
                if ((srcClassifier instanceof DbORView) || (destClassifier instanceof DbORView))
                    assocWithViewList.add(originAssoc);
                else
                    assocOriginList.add(originAssoc);
            }
            dbEnum.close();

            int nbViewAssoc = assocWithViewList.size();
            if (nbViewAssoc > 0) {
                getController().println(wMsgNoAssocWithView);
                for (int i = 0; i < nbViewAssoc; i++) {
                    viewAssoc = (DbORAssociation) assocWithViewList.get(i);
                    String dispName = getDisplayName(viewAssoc);
                    getController().println(dispName);

                    viewAssoc.remove();
                }
                getController().println();
            }

            int nb = assocOriginList.size();
            for (int i = 0; i < nb; i++) {
                if (!getController().checkPoint()) //if user has CANCELled
                    break;
                originAssoc = (DbORAssociation) assocOriginList.get(i);
                srcClassifier = originAssoc.getFrontEnd().getClassifier();
                destClassifier = originAssoc.getBackEnd().getClassifier();

                //les rôles associés
                srcEnd = originAssoc.getFrontEnd();
                dstEnd = originAssoc.getBackEnd();
                srcBackMult = srcEnd.getMultiplicity();
                destBackMult = dstEnd.getMultiplicity();

                // créer une table d'association et deux arcs pour remplacer la DbORAssociation d'origine  
                assocClassifier = AnyORObject.createERRelationship(model);
                copyORAssociationValues(assocClassifier, originAssoc);

                DbORAssociation srcArc = new DbORAssociation(srcClassifier, hideMult,
                        assocClassifier, srcBackMult);
                srcArc.setIsArc(Boolean.TRUE);
                srcArcEnd = srcArc.getArcEnd();
                copyRoleValues(srcArcEnd, srcEnd);
                srcArcEnd.setNavigable(new Boolean(srcEnd.isNavigable()));

                DbORAssociation dstArc = new DbORAssociation(destClassifier, hideMult,
                        assocClassifier, destBackMult);
                dstArc.setIsArc(Boolean.TRUE);
                dstArcEnd = dstArc.getArcEnd();
                copyRoleValues(dstArcEnd, dstEnd);
                dstArcEnd.setNavigable(new Boolean(dstEnd.isNavigable()));
                msg = MessageFormat.format(wMsgErAssocCreation, new Object[] {
                        assocClassifier.getName(), srcClassifier.getName(),
                        destClassifier.getName() });
                getController().println(msg);

                //transférer les dépendances sur les röles
                boolean dependencieExist = false;
                DbEnumeration srcDepEnum = srcEnd.getDependentConstraints().elements();
                while (srcDepEnum.hasMoreElements()) {
                    DbORPrimaryUnique key = (DbORPrimaryUnique) srcDepEnum.nextElement();
                    srcArcEnd.addToDependentConstraints(key);
                    dependencieExist = true;
                }
                srcDepEnum.close();
                if (dependencieExist) {
                    msg = MessageFormat.format(wMsgDependKeeped, new Object[] { srcEnd.getName() });
                    getController().println(msg);
                    dependencieExist = false;
                }

                DbEnumeration dstDepEnum = dstEnd.getDependentConstraints().elements();
                while (dstDepEnum.hasMoreElements()) {
                    DbORPrimaryUnique key = (DbORPrimaryUnique) dstDepEnum.nextElement();
                    dstArcEnd.addToDependentConstraints(key);
                    dependencieExist = true;
                }
                dstDepEnum.close();

                if (dependencieExist) {
                    msg = MessageFormat.format(wMsgDependKeeped, new Object[] { dstEnd.getName() });
                    getController().println(msg);
                    dependencieExist = false;
                }

                // les objets graphiques
                DbEnumeration dbEnum1 = null;
                dbEnum1 = originAssoc.getAssociationGos().elements();
                while (dbEnum1.hasMoreElements()) {
                    originAssocGo = (DbORAssociationGo) dbEnum1.nextElement();
                    DbSMSDiagram diagram = (DbSMSDiagram) originAssocGo
                            .getCompositeOfType(DbSMSDiagram.metaClass);
                    DbSMSGraphicalObject srcEndGo = originAssocGo.getFrontEndGo();
                    DbSMSGraphicalObject destEndGo = originAssocGo.getBackEndGo();
                    assocClassifierGO = new DbORTableGo(diagram, assocClassifier);

                    if ((srcEndGo != null) && (srcEndGo instanceof DbSMSClassifierGo)) {
                        tempClassifier = ((DbSMSClassifierGo) srcEndGo).getClassifier();
                        if (DbObject.valuesAreEqual(srcClassifier, tempClassifier))
                            srcClassifierGO = (DbSMSClassifierGo) srcEndGo;
                    }
                    if ((destEndGo != null) && (destEndGo instanceof DbSMSClassifierGo)) {
                        tempClassifier = ((DbSMSClassifierGo) destEndGo).getClassifier();
                        if (DbObject.valuesAreEqual(destClassifier, tempClassifier))
                            destClassifierGO = (DbSMSClassifierGo) destEndGo;
                    }
                    Polygon originPolygon = originAssocGo.getPolyline();
                    ORAssociationTool associationTool = new ORAssociationTool();
                    associationTool.convertAssociationGOtoRelationshipGO(diagram, originPolygon,
                            srcClassifierGO, assocClassifierGO, destClassifierGO, srcArc, dstArc,
                            originAssocGo);
                }
                dbEnum1.close();
                originAssoc.remove();
                getController().println();
            } //end for

            removeViewGOs(model);
            model.setIsConverted(Boolean.TRUE);

            // Traiter les sous-modèles
            if ((m_withSubModels) && (getController().checkPoint())) {
                DbEnumeration enum2 = null;
                enum2 = model.getComponents().elements(DbORDataModel.metaClass);
                while (enum2.hasMoreElements()) {
                    DbORDataModel subModel = (DbORDataModel) enum2.nextElement();
                    DbORDataModel originModel = (DbORDataModel) m_OriginModelList
                            .get(++m_subModelIndex);
                    String originName = originModel.getName();
                    getController().println(wMsgModelSeparator);
                    msg = MessageFormat.format(wMsgConvertingTo, new Object[] { originName,
                            subModel.getName() });
                    getController().println(msg);
                    if (!originModel.isIsValidated())
                        getController().println(wMsgModelNotValidated);
                    getController().println();
                    convertORRelationship(subModel);
                }
                enum2.close();
            }
        } //end convertORRelationship

        /*
         * Conversion des modèles conceptuels en modèles relationnels
         */
        private void convertERRelationship(DbORDataModel model) throws DbException {
            DbORTable assocTable = null;

            if (m_convertOption == ConvertDataModelWorkMode.ER_TO_OR_NOABS)
                m_absorptionRequired = false;
            else
                m_absorptionRequired = true;

            DbEnumeration dbEnum = null;
            dbEnum = model.getComponents().elements(DbORTable.metaClass);
            while (dbEnum.hasMoreElements()) {
                if (!getController().checkPoint()) //if user has CANCELled
                    break;
                assocTable = (DbORTable) dbEnum.nextElement();
                if (assocTable.isIsAssociation()) {
                    int nbArcs = assocTable.getAssociationEnds().size();
                    //Relation binaire
                    if (nbArcs == 2) {
                        convertBinaryRelationship(assocTable);
                        getController().println();
                    }
                    //Relation ternaire et plus
                    else if (nbArcs > 2) {
                        convertTernaryAndMoreRelationship(assocTable);
                        getController().println();
                    } else
                        //Association non liée ou liée incorrectement
                        //??? est-ce qu'on ne devrait pas détruire la table d'association ?
                        assocTable.setIsAssociation(Boolean.FALSE);
                }
            }
            dbEnum.close();

            model.setIsConverted(Boolean.TRUE);
            // Traiter les sous-modèles
            if ((m_withSubModels) && (getController().checkPoint())) {
                DbEnumeration enum2 = null;
                enum2 = model.getComponents().elements(DbORDataModel.metaClass);
                while (enum2.hasMoreElements()) {
                    DbORDataModel subModel = (DbORDataModel) enum2.nextElement();
                    DbORDataModel originModel = (DbORDataModel) m_OriginModelList
                            .get(++m_subModelIndex);
                    String originName = originModel.getName();
                    getController().println(wMsgModelSeparator);
                    msg = MessageFormat.format(wMsgConvertingTo, new Object[] { originName,
                            subModel.getName() });
                    getController().println(msg);
                    if (!originModel.isIsValidated())
                        getController().println(wMsgModelNotValidated);;
                    getController().println();
                    convertERRelationship(subModel);
                }
                enum2.close();
            }
        } //end convertERRelationship    

        /*
         * Conversion des relations conceptuels BINAIRES dans un modèle relationnel
         */
        private void convertBinaryRelationship(DbORTable assocTable) throws DbException {
            DbORAssociationEnd srcEnd = null;
            SMSMultiplicity srcMult = null;
            DbORAssociation srcArc = null;
            DbORAbsTable srcTable = null;
            DbORAssociationEnd dstEnd = null;
            SMSMultiplicity dstMult = null;
            DbORAssociation dstArc = null;
            DbORAbsTable dstTable = null;
            DbORAbsTable childTable = null;
            ArrayList assocTabEndList = new ArrayList();
            ArrayList assocTabColumnsList = new ArrayList();
            ArrayList arcsGOList = new ArrayList();
            ArrayList childTableUKList = new ArrayList();
            DbSMSClassifierGo assocTableGo = null;
            DbORAssociationEnd childEnd = null;
            int nbSrcDependence = 0;
            int nbDstDependence = 0;
            int nbDependenceToDo = 0;
            boolean dstEndPrimaryDepExist = false;
            boolean srcEndPrimaryDepExist = false;
            boolean dependenceOnPrimary = false;
            boolean dstIsChild = false;
            boolean dstByDeduction = false;
            boolean srcByDeduction = false;
            boolean childIsOptional = false;
            DbORPrimaryUnique childPK = null;

            DbEnumeration rolesEnum = assocTable.getAssociationEnds().elements();// toujours des backEnds
            while (rolesEnum.hasMoreElements()) {
                assocTabEndList.add(rolesEnum.nextElement());
            }
            rolesEnum.close();

            DbObject[] dboEnds = new DbObject[assocTabEndList.size()];
            for (int i = 0; i < dboEnds.length; i++) {
                dboEnds[i] = (DbObject) assocTabEndList.get(i);
            }
            //première entité
            srcEnd = (DbORAssociationEnd) dboEnds[0];
            srcMult = srcEnd.getMultiplicity();
            srcArc = (DbORAssociation) srcEnd.getComposite();
            srcTable = srcEnd.getOppositeEnd().getClassifier();
            //deuxième entité
            dstEnd = (DbORAssociationEnd) dboEnds[1];
            dstMult = dstEnd.getMultiplicity();
            dstArc = (DbORAssociation) dstEnd.getComposite();
            dstTable = dstEnd.getOppositeEnd().getClassifier();

            m_absorptionToDo = isBinaryAbsorptionNeeded(srcMult, dstMult);
            if (m_absorptionToDo) {
                DbORPrimaryUnique key = null;
                //comptabilisation des dépendances sur les arcs pour pouvoir les recréer sur la nouvelle association
                //Pour le moment on conserve les dépendances seulement pour les relations binaires
                DbEnumeration srcDepEnum = srcEnd.getDependentConstraints().elements();
                while (srcDepEnum.hasMoreElements()) {
                    key = (DbORPrimaryUnique) srcDepEnum.nextElement();
                    if (key.isPrimary())
                        srcEndPrimaryDepExist = true;
                    nbSrcDependence++;
                }
                srcDepEnum.close();
                DbEnumeration dstDepEnum = dstEnd.getDependentConstraints().elements();
                while (dstDepEnum.hasMoreElements()) {
                    key = (DbORPrimaryUnique) dstDepEnum.nextElement();
                    if (key.isPrimary())
                        dstEndPrimaryDepExist = true;
                    nbDstDependence++;
                }
                dstDepEnum.close();

                //Déterminer la table enfant
                //1er cas: les deux non navigables
                if ((!srcEnd.isNavigable()) && (!dstEnd.isNavigable())) {
                    String assocTableName = getDisplayName(assocTable);
                    if (srcMult.isMaxN()) {
                        dstByDeduction = true;
                        msg = MessageFormat.format(wMsgNoneNavigable, new Object[] {
                                assocTableName, dstTable.getName() });
                        getController().println(msg);
                    } else {
                        srcByDeduction = true;
                        msg = MessageFormat.format(wMsgNoneNavigable, new Object[] {
                                assocTableName, srcTable.getName() });
                        getController().println(msg);
                    }
                }
                if ((srcEnd.isNavigable()) || (srcByDeduction)) {
                    childTable = srcTable;
                    dstIsChild = false;
                    if ((srcMult.isMaxN()) && (srcEnd.isNavigable())) {
                        String arcName = getDisplayName(srcArc);
                        msg = MessageFormat.format(wMsgNavigableIsMaxN, new Object[] { arcName });
                        getController().println(msg);
                    } else
                        childIsOptional = srcMult.getValue() == SMSMultiplicity.OPTIONAL;
                    nbDependenceToDo = nbSrcDependence;
                    dependenceOnPrimary = srcEndPrimaryDepExist;
                } else if ((dstEnd.isNavigable()) || (dstByDeduction)) {
                    childTable = dstTable;
                    dstIsChild = true;
                    if ((dstMult.isMaxN()) && (dstEnd.isNavigable())) {
                        String arcName = getDisplayName(dstArc);
                        msg = MessageFormat.format(wMsgNavigableIsMaxN, new Object[] { arcName });
                        getController().println(msg);
                    } else
                        childIsOptional = dstMult.getValue() == SMSMultiplicity.OPTIONAL;
                    nbDependenceToDo = nbDstDependence;
                    dependenceOnPrimary = dstEndPrimaryDepExist;
                }

                //Déplacer les colonnes des asociations vers la table enfant
                DbEnumeration colEnum = assocTable.getComponents().elements();
                while (colEnum.hasMoreElements()) {
                    DbObject dbo = colEnum.nextElement();
                    if (dbo instanceof DbORColumn)
                        assocTabColumnsList.add(dbo);
                }
                colEnum.close();

                msg = MessageFormat.format(wMsgAssocAbsorbed, new Object[] { assocTable.getName(),
                        childTable.getName() });
                getController().println(msg);
                if (assocTabColumnsList.size() > 0) {
                    DbObject[] dbColumns = new DbObject[assocTabColumnsList.size()];
                    for (int i = 0; i < dbColumns.length; i++) {
                        dbColumns[i] = (DbObject) assocTabColumnsList.get(i);
                        msg = MessageFormat.format(wMsgAttribAbsorbed, new Object[] {
                                ((DbORColumn) dbColumns[i]).getName(), childTable.getName() });
                        getController().println(msg);
                        //Les attributs absorbés dans une table optionnelle (0,1) ne peuvent être NON NULL
                        if ((childIsOptional) && (((DbORColumn) dbColumns[i]).isNull())) {
                            ((DbORColumn) dbColumns[i]).setNull(Boolean.TRUE);
                            getController().println(wMsgNullPosAttribute);
                        }
                    }
                    SMSMove.move(dbColumns, childTable);
                }

                //Créer la nouvelle association  
                DbORAssociation orAssociation = new DbORAssociation(srcTable, srcMult, dstTable,
                        dstMult);
                orAssociation.setIsArc(Boolean.FALSE);
                copyTableAssociationValues(orAssociation, assocTable);
                orAssociation.setName(assocTable.getName());
                orAssociation.setPhysicalName(assocTable.getPhysicalName());
                DbORAssociationEnd newSrcEnd = orAssociation.getFrontEnd();
                copyRoleValues(newSrcEnd, srcEnd);
                //newSrcEnd.setNavigable(new Boolean(srcEnd.isNavigable()));                     
                DbORAssociationEnd newDstEnd = orAssociation.getBackEnd();
                copyRoleValues(newDstEnd, dstEnd);
                //newDstEnd.setNavigable(new Boolean(dstEnd.isNavigable()));            
                if (dstIsChild)
                    childEnd = newDstEnd;
                else
                    childEnd = newSrcEnd;

                /*
                 * dépendance sur clé unique sur le röle enfant version 2.3 build 504 : il a été
                 * décidé de ne pas générer de clé alternative lors de la conversion selon les
                 * anciennes règles de conversion mais seulement de propager les dépendances
                 * existantes
                 */

                //Build 507 : on ne transfert les dépendances sur le röle enfant 
                //que si la multiplicité est 1,1 selon les règles d'intégrité
                //          Optimiser le code suite au changements de transfert de dependance
                SMSMultiplicity childMult = childEnd.getMultiplicity();
                if (childMult.getValue() == SMSMultiplicity.EXACTLY_ONE) {
                    //les cles de la table enfant
                    DbEnumeration pukEnum = childTable.getComponents().elements();
                    while (pukEnum.hasMoreElements()) {
                        DbObject dbo = pukEnum.nextElement();
                        if ((dbo instanceof DbORPrimaryUnique)) {
                            if (((DbORPrimaryUnique) dbo).isPrimary())
                                childPK = (DbORPrimaryUnique) dbo;
                            else
                                childTableUKList.add(dbo);
                        }
                    }
                    pukEnum.close();

                    //dependance sur cle primaire
                    if (dependenceOnPrimary) {
                        if (childPK == null) {
                            DbORPrimaryUnique primaryKey = (DbORPrimaryUnique) SMSToolkit
                                    .getToolkit(assocTable).createDbObject(
                                            DbORPrimaryUnique.metaClass, childTable, null);
                            primaryKey.setPrimary(Boolean.TRUE);
                            primaryKey.setName(kKeyPrimaryUniqueKeyName);
                            childEnd.addToDependentConstraints(primaryKey);
                        } else {
                            childEnd.addToDependentConstraints(childPK);
                        }
                        nbDependenceToDo = nbDependenceToDo - 1;
                    }

                    //dependance sur cles uniques
                    int nbChildUK = childTableUKList.size();
                    DbObject[] dboUK = new DbObject[nbChildUK];
                    for (int i = 0; i < nbDependenceToDo; i++) {
                        if (i >= nbChildUK) {
                            DbORPrimaryUnique uniqueKey = (DbORPrimaryUnique) SMSToolkit
                                    .getToolkit(assocTable).createDbObject(
                                            DbORPrimaryUnique.metaClass, childTable, null);
                            uniqueKey.setPrimary(Boolean.FALSE);
                            uniqueKey.setName(kKeyUniqueKeyName);
                            childEnd.addToDependentConstraints(uniqueKey);
                        } else {
                            dboUK[i] = (DbObject) childTableUKList.get(i);
                            childEnd.addToDependentConstraints((DbORPrimaryUnique) dboUK[i]);
                        }
                    }
                    //Les dépendances 
                    if ((dependenceOnPrimary) || (nbDependenceToDo > 0)) {
                        msg = MessageFormat.format(wMsgDependKeeped, new Object[] { childEnd
                                .getName() });
                        getController().println(msg);
                    }
                } else {
                    msg = MessageFormat.format(wMsgDependNOTKeeped, new Object[] { childEnd
                            .getName() });
                    getController().println(msg);
                }

                //Créer les nouvelles associations graphiques
                DbEnumeration goEnum = null;
                goEnum = assocTable.getClassifierGos().elements();
                while (goEnum.hasMoreElements()) {
                    assocTableGo = (DbSMSClassifierGo) goEnum.nextElement();
                    DbSMSDiagram diagram = (DbSMSDiagram) assocTableGo
                            .getCompositeOfType(DbSMSDiagram.metaClass);
                    //trouver les 2 tableGo associées
                    DbEnumeration assocGOEnum = null;
                    assocGOEnum = assocTableGo.getBackEndLineGos().elements(); //aucune FrontEndLineGos en ER normalement
                    while (assocGOEnum.hasMoreElements()) {
                        arcsGOList.add(assocGOEnum.nextElement());
                    }
                    assocGOEnum.close();

                    //Les 2 arcs doivent être présents
                    if (arcsGOList.size() == 2) {
                        DbObject[] dbArcGOs = new DbObject[arcsGOList.size()];
                        for (int i = 0; i < dbArcGOs.length; i++) {
                            dbArcGOs[i] = (DbObject) arcsGOList.get(i);
                        }

                        DbSMSGraphicalObject firstTableGo = ((DbORAssociationGo) dbArcGOs[0])
                                .getFrontEndGo();
                        //Polygon polygon1 = ((DbORAssociationGo)dbAssocGOs[0]).getPolyline();
                        DbSMSGraphicalObject secondTableGo = ((DbORAssociationGo) dbArcGOs[1])
                                .getFrontEndGo();
                        //Polygon polygon2 = ((DbORAssociationGo)dbAssocGOs[1]).getPolyline();
                        DbORAssociationGo orAssocGO = new DbORAssociationGo(diagram, firstTableGo,
                                secondTableGo, orAssociation);
                        DbORAssociationGo arcGO = (DbORAssociationGo) dbArcGOs[0];
                        orAssocGO.setRightAngle(new Boolean(arcGO.isRightAngle()));
                        DbGraphic.createPolyline(orAssocGO, true);

                        /*
                         * pour le moment l'associationGo est en ligne droite entre les deux points
                         * TODO m/thode dans OrAssociationTool pour traiter les polygones des 2 arcs
                         * et l'appliquer a la nouvelle association
                         * 
                         * if ((srcEndGo != null) && (srcEndGo instanceof DbSMSClassifierGo)){
                         * tempClassifier = ((DbSMSClassifierGo)srcEndGo).getClassifier(); if
                         * (DbObject.valuesAreEqual(srcClassifier, tempClassifier)) srcClassifierGO
                         * = (DbSMSClassifierGo)srcEndGo; } if ((destEndGo != null) && (destEndGo
                         * instanceof DbSMSClassifierGo)){ tempClassifier =
                         * ((DbSMSClassifierGo)destEndGo).getClassifier(); if
                         * (DbObject.valuesAreEqual(destClassifier, tempClassifier))
                         * destClassifierGO = (DbSMSClassifierGo)destEndGo;
                         * 
                         * } Polygon originPolygon = originAssocGo.getPolyline(); ORAssociationTool
                         * associationTool = new ORAssociationTool();
                         * associationTool.convertAssociationGOtoRelationshipGO(diagram,
                         * originPolygon, srcClassifierGO, assocClassifierGO, destClassifierGO,
                         * srcArc, destArc, originAssocGo);
                         */
                    }
                    arcsGOList.clear();

                }
                goEnum.close();

                //TODO  la navigabilité est défini par défaut. On devrait assigner celle des arcs           
                //orAssociation.setIsIdentifying()TODO assigner la bonne multiplicité
                //si un des arc est dependant la nouvelle association devient d/pendante...

                assocTable.remove();
            } else { // pas d'absorption
                DbORAssociation childArc = null;
                DbORPrimaryUnique key = null;
                int nbSrcDep = 0;
                int nbDstDep = 0;
                assocTable.setIsAssociation(Boolean.FALSE);
                srcArc.setIsArc(Boolean.FALSE);
                dstArc.setIsArc(Boolean.FALSE);

                // Suppression obligatoires des dépendances existantes puisque de nouvelles sont créées pour la table d'intersection
                // ces dépendances étaient de toute façon erronées selon les règles d'intégrités
                DbEnumeration srcDepEnum = srcEnd.getDependentConstraints().elements();
                while (srcDepEnum.hasMoreElements()) {
                    key = (DbORPrimaryUnique) srcDepEnum.nextElement();
                    key.removeFromAssociationDependencies(srcEnd);
                    nbSrcDep++;
                }
                srcDepEnum.close();
                DbEnumeration dstDepEnum = dstEnd.getDependentConstraints().elements();
                while (dstDepEnum.hasMoreElements()) {
                    key = (DbORPrimaryUnique) dstDepEnum.nextElement();
                    key.removeFromAssociationDependencies(dstEnd);
                    nbDstDep++;
                }
                dstDepEnum.close();
                if (nbSrcDep > 0) {
                    msg = MessageFormat.format(wMsgDependNOTKeeped,
                            new Object[] { srcEnd.getName() });
                    getController().println(msg);
                }
                if (nbDstDep > 0) {
                    msg = MessageFormat.format(wMsgDependNOTKeeped,
                            new Object[] { dstEnd.getName() });
                    getController().println(msg);
                }

                // les frontEnds(visibles en relationnel) prennent la valeur des arcEnds
                DbORAssociationEnd srcFrontEnd = srcArc.getFrontEnd();
                invertRoleValues(srcEnd, srcFrontEnd);
                DbORAssociationEnd dstFrontEnd = dstArc.getFrontEnd();
                invertRoleValues(dstEnd, dstFrontEnd);

                // les arcEnds(backEnds) devenus les associationEnd de la table d'intersection sont modifiés
                srcEnd.setMultiplicity(SMSMultiplicity.getInstance(SMSMultiplicity.EXACTLY_ONE));
                srcEnd.setNavigable(Boolean.TRUE);
                srcFrontEnd.setNavigable(Boolean.FALSE);
                dstEnd.setMultiplicity(SMSMultiplicity.getInstance(SMSMultiplicity.EXACTLY_ONE));
                dstEnd.setNavigable(Boolean.TRUE);
                dstFrontEnd.setNavigable(Boolean.FALSE);

                msg = MessageFormat.format(wMsgIntersectionTable, new Object[] { assocTable
                        .getName() });
                getController().println(msg);

                // Création des dépendances sur les rôles

                // Les deux maximales N
                if ((srcMult.isMaxN()) && (dstMult.isMaxN())) {
                    DbORPrimaryUnique primaryKey = (DbORPrimaryUnique) SMSToolkit.getToolkit(
                            assocTable).createDbObject(DbORPrimaryUnique.metaClass, assocTable,
                            null);
                    primaryKey.setPrimary(Boolean.TRUE);
                    primaryKey.setName(kKeyPrimaryUniqueKeyName);
                    srcEnd.addToDependentConstraints(primaryKey);
                    dstEnd.addToDependentConstraints(primaryKey);
                    getController().println(wMsgPKDependAll);
                }
                // Les deux optionnels (0,1)
                else if ((srcMult.getValue() == SMSMultiplicity.OPTIONAL)
                        && (dstMult.getValue() == SMSMultiplicity.OPTIONAL)) {
                    DbORPrimaryUnique primaryKey = (DbORPrimaryUnique) SMSToolkit.getToolkit(
                            assocTable).createDbObject(DbORPrimaryUnique.metaClass, assocTable,
                            null);
                    primaryKey.setPrimary(Boolean.TRUE);
                    primaryKey.setName(kKeyPrimaryUniqueKeyName);
                    srcEnd.addToDependentConstraints(primaryKey);
                    String dispName = getDisplayName(srcArc);
                    msg = MessageFormat.format(wMsgPKDependAddedOn, new Object[] { dispName });
                    getController().println(msg);
                    // version 2.3 build 504 : il a été décidé de ne pas générer de clé alternative lors de la conversion 
                    // selon les anciennes règles de conversion.
                    /*
                     * DbORPrimaryUnique uniqueKey =
                     * (DbORPrimaryUnique)SMSToolkit.getToolkit(assocTable
                     * ).createDbObject(DbORPrimaryUnique.metaClass, assocTable, null);
                     * uniqueKey.setPrimary(Boolean.FALSE); uniqueKey.setName(kKeyUniqueKeyName);
                     * dstEnd.addToDependentConstraints(uniqueKey);
                     */
                }
                //MaxN et optionnel
                else if ((srcMult.getValue() == SMSMultiplicity.OPTIONAL) && (dstMult.isMaxN())
                        || (dstMult.getValue() == SMSMultiplicity.OPTIONAL) && (srcMult.isMaxN())) {
                    DbORPrimaryUnique primaryKey = (DbORPrimaryUnique) SMSToolkit.getToolkit(
                            assocTable).createDbObject(DbORPrimaryUnique.metaClass, assocTable,
                            null);
                    primaryKey.setPrimary(Boolean.TRUE);
                    primaryKey.setName(kKeyPrimaryUniqueKeyName);
                    if (srcMult.getValue() == SMSMultiplicity.OPTIONAL) {
                        srcEnd.addToDependentConstraints(primaryKey);
                        childArc = srcArc;
                    } else {
                        dstEnd.addToDependentConstraints(primaryKey);
                        childArc = dstArc;
                    }
                    String dispName = getDisplayName(childArc);
                    msg = MessageFormat.format(wMsgPKDependAddedOn, new Object[] { dispName });
                    getController().println(msg);
                }
            }

        }// end convertBinaryRelationship

        /*
         * Conversion des relations conceptuels TERNAIRES et plus en modèles relationnels
         */
        private void convertTernaryAndMoreRelationship(DbORTable assocTable) throws DbException {
            ArrayList assocTabColumnsList = new ArrayList();
            DbORAbsTable childTable = null;
            DbORAbsTable parentTable = null;
            boolean childIsOptional = false;
            boolean childTableFound = false;
            //boolean dependentEndFound = false;
            DbORAssociationEnd childEnd = null;
            //DbORAssociationEnd endWithDependencie = null;
            DbORAssociation childArc = null;
            DbORAssociationEnd assocEnd = null;
            SMSMultiplicity endMult = null;
            ArrayList childArcsGOList = new ArrayList();
            ArrayList assocArcsGOList = new ArrayList();
            DbSMSClassifierGo assocTableGo = null;
            DbSMSClassifierGo childTableGo = null;
            boolean assocTableGOFound = false;
            Object[][] parentWithAssoc;
            ArrayList childTableUKList = new ArrayList();
            DbORPrimaryUnique childPK = null;
            ArrayList assocTabEndList = new ArrayList();

            DbEnumeration rolesEnum = assocTable.getAssociationEnds().elements();// toujours des backEnds
            while (rolesEnum.hasMoreElements()) {
                assocTabEndList.add(rolesEnum.nextElement());
            }
            rolesEnum.close();

            DbObject[] dboEnds = new DbObject[assocTabEndList.size()];
            for (int i = 0; i < dboEnds.length; i++) {
                dboEnds[i] = (DbObject) assocTabEndList.get(i);
            }

            m_absorptionToDo = isTernaryAbsorptionNeeded(assocTabEndList);
            if (m_absorptionToDo) {// Au moins une multiplicité 1,1 ou 0,1 est présente
                // NOTE : Pour le moment on conserve les dépendances seulement pour les relations binaires
                // Les cas spécifiques qui pourraient déroger de la règle d'intégrité qui dit qu'une dépendance 
                // ne peut exister dans une relation ternaire pourront ëtre analyser dans une autre version.

                //Déterminer la table enfant
                for (int i = 0; i < dboEnds.length; i++) {
                    assocEnd = (DbORAssociationEnd) dboEnds[i];
                    endMult = assocEnd.getMultiplicity();
                    // Ici on ne vérifie pas si la table enfant est navigable. C'est moins significatif 
                    //parce que de toute façon le résultat converti(absorbé ou non) redéfini tous les röles 
                    //et la navigabillté est alors assignée correctement.
                    if (m_childExactlyOneExist) {
                        if ((endMult.getValue() == SMSMultiplicity.EXACTLY_ONE)
                                && (!childTableFound)) {
                            childTable = assocEnd.getOppositeEnd().getClassifier();
                            childArc = (DbORAssociation) assocEnd.getComposite();
                            childEnd = assocEnd;
                            childTableFound = true;
                        }
                    } else {
                        if ((endMult.getValue() == SMSMultiplicity.OPTIONAL) && (!childTableFound)) {
                            childTable = assocEnd.getOppositeEnd().getClassifier();
                            childArc = (DbORAssociation) assocEnd.getComposite();
                            childEnd = assocEnd;
                            childIsOptional = true;
                            childTableFound = true;
                        }
                    }
                }//end for

                msg = MessageFormat.format(wMsgAssocAbsorbed, new Object[] { assocTable.getName(),
                        childTable.getName() });
                getController().println(msg);

                //les cles de la table enfant
                DbEnumeration pukEnum = childTable.getComponents().elements();
                while (pukEnum.hasMoreElements()) {
                    DbObject dbo = pukEnum.nextElement();
                    if ((dbo instanceof DbORPrimaryUnique)) {
                        if (((DbORPrimaryUnique) dbo).isPrimary())
                            childPK = (DbORPrimaryUnique) dbo;
                        else
                            childTableUKList.add(dbo);
                    }
                }
                pukEnum.close();

                //Déplacer les colonnes des asociations vers la table enfant
                DbEnumeration colEnum = assocTable.getComponents().elements();
                while (colEnum.hasMoreElements()) {
                    DbObject dbo = colEnum.nextElement();
                    if (dbo instanceof DbORColumn)
                        assocTabColumnsList.add(dbo);
                }
                colEnum.close();

                if (assocTabColumnsList.size() > 0) {
                    DbObject[] dbColumns = new DbObject[assocTabColumnsList.size()];
                    for (int i = 0; i < dbColumns.length; i++) {
                        dbColumns[i] = (DbObject) assocTabColumnsList.get(i);
                        msg = MessageFormat.format(wMsgAttribAbsorbed, new Object[] {
                                ((DbORColumn) dbColumns[i]).getName(), childTable.getName() });
                        getController().println(msg);
                        //Les attributs absorbés dans une table optionnelle (0,1) ne peuvent être NON NULL
                        if ((childIsOptional) && (((DbORColumn) dbColumns[i]).isNull())) {
                            ((DbORColumn) dbColumns[i]).setNull(Boolean.TRUE);
                            getController().println(wMsgNullPosAttribute);
                        }
                    }
                    SMSMove.move(dbColumns, childTable);
                }

                //Créer les nouvelles associations sémantiques entre la table enfant et chaque table parent
                parentWithAssoc = new Object[dboEnds.length - 1][2];
                for (int i = 0, idx = 1; i < dboEnds.length; i++) {
                    DbORAssociationEnd parentEnd = (DbORAssociationEnd) dboEnds[i];
                    // Non transfert des dépendances existantes selon les règles d'intégrités
                    DbEnumeration depEnum = parentEnd.getDependentConstraints().elements();
                    if (depEnum.hasMoreElements()) {
                        msg = MessageFormat.format(wMsgDependTernaryNOTKeeped,
                                new Object[] { parentEnd.getName() });
                        getController().println(msg);
                    }
                    depEnum.close();

                    DbORAssociation parentArc = (DbORAssociation) parentEnd.getComposite();
                    if (parentArc.equals(childArc))
                        continue;
                    SMSMultiplicity parentMult = parentEnd.getMultiplicity();
                    parentTable = parentEnd.getOppositeEnd().getClassifier();
                    DbORAssociation orAssociation = new DbORAssociation(parentTable, parentMult,
                            childTable, childEnd.getMultiplicity());
                    copyTableAssociationValues(orAssociation, assocTable, idx);
                    orAssociation.setIsArc(Boolean.FALSE);
                    DbORAssociationEnd newParentEnd = orAssociation.getFrontEnd();
                    copyRoleValues(newParentEnd, parentEnd, idx);
                    newParentEnd.setNavigable(Boolean.FALSE);
                    DbORAssociationEnd newChildEnd = orAssociation.getBackEnd();
                    copyRoleValues(newChildEnd, childEnd);
                    newChildEnd.setNavigable(Boolean.TRUE);

                    //Déterminer si ce röle enfant aura une dépendance primaire 
                    // mais seulement s'il est EXACTLY_ONE
                    //Build 508 : Il a été décidé qu'aucune dépendance ne serait créée pour les relations ternaires 
                    //dans laquelle il y aurait un parent 1,1 ou 0,1. Ainsi on applique la même règle que si 
                    // c'étaient plusieurs relations binaires
                    /*
                     * if(m_childExactlyOneExist){ if(!dependentEndFound){
                     * if((m_parentExactlyOneExist) && (parentMult.getValue() ==
                     * SMSMultiplicity.EXACTLY_ONE)){ endWithDependencie = newChildEnd;
                     * dependentEndFound = true; msg = MessageFormat.format(wMsgPKDependAddedOn, new
                     * Object[] {orAssociation.getName()}); getController().println(msg); } else
                     * if((m_parentOptionalExist) && (parentMult.getValue() ==
                     * SMSMultiplicity.OPTIONAL)){ endWithDependencie = newChildEnd;
                     * dependentEndFound = true; msg = MessageFormat.format(wMsgPKDependAddedOn, new
                     * Object[] {orAssociation.getName()}); getController().println(msg); } } }
                     */
                    //matrice des associations et leur table parent pour les GOs              
                    parentWithAssoc[idx - 1][0] = parentTable;
                    parentWithAssoc[idx - 1][1] = orAssociation;
                    idx++;

                }//end for

                // Appliquer la dépendance primaire s'il y a lieu 
                //Build 508 : Il a été décidé qu'aucune dépendance ne serait créée pour les relations ternaires 
                //dans laquelle il y aurait un parent 1,1 ou 0,1. Ainsi on applique la même règle que si 
                // c'étaient plusieurs relations binaires
                /*
                 * if(dependentEndFound){ if (childPK == null){ DbORPrimaryUnique primaryKey =
                 * (DbORPrimaryUnique
                 * )SMSToolkit.getToolkit(assocTable).createDbObject(DbORPrimaryUnique.metaClass,
                 * childTable, null); primaryKey.setPrimary(Boolean.TRUE);
                 * primaryKey.setName(kKeyPrimaryUniqueKeyName);
                 * endWithDependencie.addToDependentConstraints(primaryKey); } else{
                 * endWithDependencie.addToDependentConstraints(childPK); } }
                 */

                //Créer les nouvelles associations graphiques 
                DbEnumeration childgoEnum = null;
                childgoEnum = childTable.getClassifierGos().elements();
                while (childgoEnum.hasMoreElements()) {
                    childTableGo = (DbSMSClassifierGo) childgoEnum.nextElement();
                    DbSMSDiagram diagram = (DbSMSDiagram) childTableGo
                            .getCompositeOfType(DbSMSDiagram.metaClass);

                    //LA TABLE D'ASSOCIATION GRAPHIQUE représentant la sémantique courante (assocTable)
                    //via la liste des arcs liés a la table enfant 
                    DbEnumeration childAssocGOEnum = null;
                    childAssocGOEnum = childTableGo.getFrontEndLineGos().elements();
                    while (childAssocGOEnum.hasMoreElements()) {
                        childArcsGOList.add(childAssocGOEnum.nextElement());
                    }
                    childAssocGOEnum.close();

                    DbObject[] dbChildArcGOs = new DbObject[childArcsGOList.size()];
                    for (int j = 0; j < dbChildArcGOs.length; j++) {
                        dbChildArcGOs[j] = (DbObject) childArcsGOList.get(j);
                        DbSMSGraphicalObject tempAssocTableGo = ((DbORAssociationGo) dbChildArcGOs[j])
                                .getBackEndGo();
                        if ((tempAssocTableGo.getSO().equals(assocTable)) && (!assocTableGOFound)) {
                            assocTableGOFound = true;
                            assocTableGo = (DbSMSClassifierGo) tempAssocTableGo;
                        }
                    }

                    //LES TABLES GRAPHIQUES à lier à la table enfant 
                    //via la liste des arcs liés à la table d'association graphique trouvée
                    if (assocTableGo != null) {
                        DbEnumeration assocGOEnum = null;
                        assocGOEnum = assocTableGo.getBackEndLineGos().elements();
                        while (assocGOEnum.hasMoreElements()) {
                            DbORAssociationGo tmpAssocGO = (DbORAssociationGo) assocGOEnum
                                    .nextElement();
                            DbSMSGraphicalObject tmpTableGo = tmpAssocGO.getFrontEndGo();
                            DbORAbsTable tmpTable = (DbORAbsTable) tmpTableGo.getSO();
                            if (!tmpTable.equals(childTable))
                                assocArcsGOList.add(tmpAssocGO);
                        }
                        assocGOEnum.close();

                        DbObject[] dbAssocArcGOs = new DbObject[assocArcsGOList.size()];
                        for (int k = 0; k < dbAssocArcGOs.length; k++) {
                            DbORAssociation keepOrAssociation = null;
                            dbAssocArcGOs[k] = (DbObject) assocArcsGOList.get(k);
                            DbSMSGraphicalObject parentTableGo = ((DbORAssociationGo) dbAssocArcGOs[k])
                                    .getFrontEndGo();
                            DbORAbsTable currentParentTable = (DbORAbsTable) parentTableGo.getSO();
                            //int nbParent = parentWithAssoc.length;
                            for (int n = 0; n < parentWithAssoc.length; n++) {
                                DbORAbsTable keepParentTable = (DbORAbsTable) parentWithAssoc[n][0];
                                if (keepParentTable.equals(currentParentTable)) {
                                    keepOrAssociation = (DbORAssociation) parentWithAssoc[n][1];
                                }
                            }
                            DbORAssociationGo orAssocGO = new DbORAssociationGo(diagram,
                                    parentTableGo, childTableGo, keepOrAssociation);
                            DbORAssociationGo arcGO = (DbORAssociationGo) dbAssocArcGOs[k];
                            orAssocGO.setRightAngle(new Boolean(arcGO.isRightAngle()));
                            DbGraphic.createPolyline(orAssocGO, true);
                        }
                    }
                    childArcsGOList.clear();
                    assocArcsGOList.clear();
                    assocTableGOFound = false;
                }
                childgoEnum.close();

                assocTable.remove();
            } else {// pas d'absorption
                DbORAssociationEnd assocTableEnd = null;
                DbORAssociation currentArc = null;
                DbORPrimaryUnique key = null;
                //SMSMultiplicity assocTableMult = null;
                ArrayList assocTableEndList = new ArrayList();
                boolean dependencieFound = false;

                msg = MessageFormat.format(wMsgIntersectionTable, new Object[] { assocTable
                        .getName() });
                getController().println(msg);
                assocTable.setIsAssociation(Boolean.FALSE);
                for (int i = 0; i < dboEnds.length; i++) {
                    int nbDep = 0;
                    assocTableEnd = (DbORAssociationEnd) dboEnds[i];

                    // Suppression obligatoires des dépendances existantes puisque de nouvelles sont créées pour la table d'intersection
                    // ces dépendances étaient de toute façon erronées selon les règles d'intégrités
                    DbEnumeration depEnum = assocTableEnd.getDependentConstraints().elements();
                    while (depEnum.hasMoreElements()) {
                        key = (DbORPrimaryUnique) depEnum.nextElement();
                        key.removeFromAssociationDependencies(assocTableEnd);
                        nbDep++;
                    }
                    depEnum.close();
                    if (nbDep > 0) {
                        msg = MessageFormat.format(wMsgDependTernaryNOTKeeped,
                                new Object[] { assocTableEnd.getName() });
                        getController().println(msg);
                    }

                    //assocTableMult = assocTableEnd.getMultiplicity();
                    currentArc = (DbORAssociation) assocTableEnd.getComposite();
                    currentArc.setIsArc(Boolean.FALSE);
                    DbORAssociationEnd entityEnd = currentArc.getFrontEnd();
                    invertRoleValues(assocTableEnd, entityEnd);
                    //entityEnd.setMultiplicity(assocTableMult);
                    entityEnd.setNavigable(Boolean.FALSE);
                    assocTableEnd.setMultiplicity(SMSMultiplicity
                            .getInstance(SMSMultiplicity.EXACTLY_ONE));
                    assocTableEnd.setNavigable(Boolean.TRUE);
                    assocTableEndList.add(assocTableEnd);

                }//end for

                // Création des dépendances sur les rôles
                DbORPrimaryUnique primaryKey = (DbORPrimaryUnique) SMSToolkit
                        .getToolkit(assocTable).createDbObject(DbORPrimaryUnique.metaClass,
                                assocTable, null);
                primaryKey.setPrimary(Boolean.TRUE);
                primaryKey.setName(kKeyPrimaryUniqueKeyName);
                // Toutes les maximales N
                if (m_allParentMaxN) {
                    DbObject[] assocEnds = new DbObject[assocTableEndList.size()];
                    for (int i = 0; i < assocEnds.length; i++) {
                        assocEnds[i] = (DbObject) assocTableEndList.get(i);
                        ((DbORAssociationEnd) assocEnds[i]).addToDependentConstraints(primaryKey);
                    }
                    getController().println(wMsgPKDependAll);
                } else {//Au moins une multiplicité 0,1 existe
                    DbObject[] assocEnds = new DbObject[assocTableEndList.size()];
                    for (int i = 0; i < assocEnds.length; i++) {
                        assocEnds[i] = (DbObject) assocTableEndList.get(i);
                        DbORAssociationEnd interTableEnd = (DbORAssociationEnd) assocEnds[i];
                        DbORAssociationEnd oppositeEnd = interTableEnd.getOppositeEnd();
                        SMSMultiplicity oppositeEndMult = oppositeEnd.getMultiplicity();
                        if ((oppositeEndMult.getValue() == SMSMultiplicity.OPTIONAL)
                                && (!dependencieFound)) {
                            interTableEnd.addToDependentConstraints(primaryKey);
                            dependencieFound = true;
                            getController().println(wMsgPKDependOptional);
                        }
                    }
                }
            }
        }//end convertTernaryAndMoreRelationship

        /*
         * evalue si l'option d'absorption peut s'appliquer selon les multiplicités de la relation
         * ternaire et plus
         */
        private boolean isTernaryAbsorptionNeeded(ArrayList assocTabEndList) throws DbException {
            DbORAssociationEnd assocEnd = null;
            SMSMultiplicity endMult = null;
            int nbUndefined = 0;
            int nbMaxN = 0;
            int nbExactlyOne = 0;
            int nbOptional = 0;
            m_childExactlyOneExist = false;
            m_parentExactlyOneExist = false;
            m_parentOptionalExist = false;
            m_allParentMaxN = false;

            DbObject[] dboEnds = new DbObject[assocTabEndList.size()];
            for (int i = 0; i < dboEnds.length; i++) {
                dboEnds[i] = (DbObject) assocTabEndList.get(i);
                assocEnd = (DbORAssociationEnd) dboEnds[i];
                endMult = assocEnd.getMultiplicity();

                switch (endMult.getValue()) {
                case SMSMultiplicity.EXACTLY_ONE:
                    nbExactlyOne++;
                    break;
                case SMSMultiplicity.MANY:
                    nbMaxN++;
                    break;
                case SMSMultiplicity.ONE_OR_MORE:
                    nbMaxN++;
                    break;
                case SMSMultiplicity.OPTIONAL:
                    nbOptional++;
                    break;
                default: //SMSMultiplicity.UNDEFINED
                    nbUndefined++;
                    break;
                } //end switch() 	                   
            }//end for

            //  Une Undefined existe
            if (nbUndefined > 0)
                return false;
            // Toutes maximales N
            if (nbMaxN == assocTabEndList.size()) {
                m_allParentMaxN = true;
                return false;
            }
            // Une 1,1 existe
            if (nbExactlyOne > 0) {
                m_childExactlyOneExist = true;
                if (nbExactlyOne > 1)
                    m_parentExactlyOneExist = true;
                else if (nbOptional > 0)
                    m_parentOptionalExist = true;
                return true;
            }
            //Une 0,1 existe
            if (nbOptional > 0) {
                if (nbOptional > 1) {
                    m_parentOptionalExist = true;
                }
                return m_absorptionRequired;
            }

            else
                return m_absorptionRequired;
        }//end isTernaryAbsorptionNeeded

        /*
         * evalue si l'option d'absorption peut s'appliquer selon les multiplicités de la relation
         * binaire
         */
        private boolean isBinaryAbsorptionNeeded(SMSMultiplicity srcMult, SMSMultiplicity dstMult)
                throws DbException {
            // Une ou l'autre Undefined
            if ((srcMult.getValue() == SMSMultiplicity.UNDEFINED)
                    || (dstMult.getValue() == SMSMultiplicity.UNDEFINED))
                return false;
            //Les deux maximales N
            if ((srcMult.isMaxN()) && (dstMult.isMaxN()))
                return false;
            //Les deux minimales 1 (le cas 1,N---1,N est traité par la condition précédente)
            if ((srcMult.isMin1()) && (dstMult.isMin1()))
                return true;
            if ((srcMult.getValue() == SMSMultiplicity.EXACTLY_ONE)
                    || (dstMult.getValue() == SMSMultiplicity.EXACTLY_ONE))
                return true;
            else
                //si les deux 0,1
                return m_absorptionRequired;
        }//end isBinaryAbsorptionNeeded

        /*
         * retourne le nom à afficher dans les commentaires du worker (entre autre)
         */
        private String getDisplayName(DbObject dbo) throws DbException {
            String dboDisplayName = null;
            if (dbo.getName() != null) {
                if (dbo.getName().length() != 0)
                    dboDisplayName = dbo.getName();
                else {
                    dboDisplayName = ApplicationContext.getSemanticalModel().getDisplayText(dbo,
                            Explorer.class);
                }
            } else {
                dboDisplayName = ApplicationContext.getSemanticalModel().getDisplayText(dbo,
                        Explorer.class);
            }
            return dboDisplayName;
        }//end getDisplayName

        /*
         * Inverse les valeurs des röles lorsque non absorption
         */
        private void invertRoleValues(DbORAssociationEnd arcEnd, DbORAssociationEnd frontEnd)
                throws DbException {

            Object[] roleValues = new Object[11];
            roleValues[0] = (Object) arcEnd.getName();
            roleValues[1] = (Object) arcEnd.getPhysicalName();
            roleValues[2] = (Object) arcEnd.getAlias();
            roleValues[3] = (Object) arcEnd.getUmlStereotype();
            roleValues[4] = (Object) arcEnd.getMultiplicity();
            roleValues[5] = (Object) arcEnd.getSpecificRangeMultiplicity();
            roleValues[6] = (Object) arcEnd.getInsertRule();
            roleValues[7] = (Object) arcEnd.getUpdateRule();
            roleValues[8] = (Object) arcEnd.getDeleteRule();
            roleValues[9] = (Object) arcEnd.getConstraintType();
            roleValues[10] = (Object) arcEnd.getDescription();

            arcEnd.setName(frontEnd.getName());
            arcEnd.setPhysicalName(frontEnd.getPhysicalName());
            arcEnd.setAlias(frontEnd.getAlias());
            arcEnd.setUmlStereotype(frontEnd.getUmlStereotype());
            arcEnd.setMultiplicity(frontEnd.getMultiplicity());
            arcEnd.setSpecificRangeMultiplicity(frontEnd.getSpecificRangeMultiplicity());
            arcEnd.setInsertRule(frontEnd.getInsertRule());
            arcEnd.setUpdateRule(frontEnd.getUpdateRule());
            arcEnd.setDeleteRule(frontEnd.getDeleteRule());
            arcEnd.setConstraintType(frontEnd.getConstraintType());
            arcEnd.setDescription(frontEnd.getDescription());

            frontEnd.setName((String) roleValues[0]);
            frontEnd.setPhysicalName((String) roleValues[1]);
            frontEnd.setAlias((String) roleValues[2]);
            frontEnd.setUmlStereotype((DbSMSStereotype) roleValues[3]);
            frontEnd.setMultiplicity((SMSMultiplicity) roleValues[4]);
            frontEnd.setSpecificRangeMultiplicity((String) roleValues[5]);
            frontEnd.setInsertRule((ORValidationRule) roleValues[6]);
            frontEnd.setUpdateRule((ORValidationRule) roleValues[7]);
            frontEnd.setDeleteRule((ORValidationRule) roleValues[8]);
            frontEnd.setConstraintType((ORConstraintType) roleValues[9]);
            frontEnd.setDescription((String) roleValues[10]);

        }//end invertRoleValues

        /*
         * Copie les valeurs des röles lorsque absorption
         */
        private void copyRoleValues(DbORAssociationEnd newEnd, DbORAssociationEnd originEnd)
                throws DbException {
            this.copyRoleValues(newEnd, originEnd, 0);

        }//end copyRoleValues

        /*
         * Copie les valeurs des röles lorsque absorption
         */
        private void copyRoleValues(DbORAssociationEnd newEnd, DbORAssociationEnd originEnd, int idx)
                throws DbException {
            if (idx == 0) {
                newEnd.setName(getDisplayName(originEnd));
                newEnd.setPhysicalName(originEnd.getPhysicalName());
                newEnd.setAlias(originEnd.getAlias());
            } else {
                newEnd.setName(getDisplayName(originEnd) + "-" + idx);
                newEnd.setPhysicalName(originEnd.getPhysicalName() + "-" + idx);
                newEnd.setAlias(originEnd.getAlias() + "-" + idx);
            }
            newEnd.setUmlStereotype(originEnd.getUmlStereotype());
            newEnd.setMultiplicity(originEnd.getMultiplicity());
            newEnd.setSpecificRangeMultiplicity(originEnd.getSpecificRangeMultiplicity());
            newEnd.setInsertRule(originEnd.getInsertRule());
            newEnd.setUpdateRule(originEnd.getUpdateRule());
            newEnd.setDeleteRule(originEnd.getDeleteRule());
            newEnd.setConstraintType(originEnd.getConstraintType());
            newEnd.setDescription(originEnd.getDescription());

        }//end copyRoleValues

        /*
         * Copie les valeurs de la table d'association vers l'association relationnelle
         */
        private void copyTableAssociationValues(DbORAssociation orAssociation, DbORTable assocTable)
                throws DbException {
            this.copyTableAssociationValues(orAssociation, assocTable, 0);

        }

        /*
         * Copie les valeurs de la table d'association vers l'association relationnelle
         */
        private void copyTableAssociationValues(DbORAssociation orAssociation,
                DbORTable assocTable, int idx) throws DbException {
            if (idx == 0) {
                orAssociation.setName(getDisplayName(assocTable));
                orAssociation.setPhysicalName(assocTable.getPhysicalName());
                orAssociation.setAlias(assocTable.getAlias());
            } else {
                orAssociation.setName(getDisplayName(assocTable) + "-" + idx);
                orAssociation.setPhysicalName(assocTable.getPhysicalName() + "-" + idx);
                orAssociation.setAlias(assocTable.getAlias() + "-" + idx);
            }
            orAssociation.setDescription(assocTable.getDescription());

        }//end copyTableAssociationValues

        /*
         * Copie les valeurs de l'association relationnelle vers table d'association conceptuelle
         */
        private void copyORAssociationValues(DbORTable assocTable, DbORAssociation orAssociation)
                throws DbException {
            assocTable.setName(getDisplayName(orAssociation));
            assocTable.setPhysicalName(orAssociation.getPhysicalName());
            assocTable.setAlias(orAssociation.getAlias());
            assocTable.setDescription(orAssociation.getDescription());

        }//end copyTableAssociationValues

    } //end ConvertDataModelWorker

    //
    // UNIT TEST
    //
    public static void main(String args[]) {

    } //end main()
}//This is the end 
