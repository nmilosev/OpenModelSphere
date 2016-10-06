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

/**
 * @author nicolask
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/*
 * Created on Feb 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.Icon;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbDeadObjectException;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.screen.DbTreeLookupDialog;
import org.modelsphere.jack.baseDb.screen.model.DbTreeModel;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.DbBEUseCaseGo;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.util.TerminologyInitializer;
import org.modelsphere.sms.oo.db.DbOOAssociation;
import org.modelsphere.sms.oo.db.DbOOClassModel;
import org.modelsphere.sms.oo.db.DbOOInheritance;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORCheck;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbORNotation;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTrigger;
import org.modelsphere.sms.or.db.DbORView;
import org.modelsphere.sms.or.generic.db.DbGEView;
import org.modelsphere.sms.screen.DbSMSTreeLookupDialog;

/**
 * @author nicolask
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences -
 *         Java - Code Style - Code Templates
 */
public final class SMSTerminologyUtil extends TerminologyUtil {

    public static void initInstance() {
        if (g_singleInstance == null)
            g_singleInstance = new SMSTerminologyUtil();
    }

    public boolean isDataModel(DbObject dbObject) {
        return (dbObject instanceof DbORDataModel);
    }

    public boolean isObjectEntityOrAssociation(DbObject dbObject) {
        return dbObject instanceof DbORAbsTable;
    }

    public boolean isObjectArc(DbObject dbObject) {
        return dbObject instanceof DbORAssociation;
    }

    public Icon getAssociationIcon() {
        return SMSExplorer.associationTableIcon;
    }

    public Icon getDiagramIcon(ApplicationDiagram diagram) throws DbException {
        Icon icon = null;
        String notationName = "";
        DbObject dbo = diagram.getDiagramGO();
        if (dbo instanceof DbBEDiagram) {
            notationName = ((DbBEDiagram) dbo).findMasterNotation().getName();
            DbBEDiagram diag = (DbBEDiagram) diagram.getDiagramGO();
            icon = Terminology.findTerminologyByName(notationName).getIcon(diag.getMetaClass());
        } else
            icon = dbo.getSemanticalIcon(DbObject.SHORT_FORM);

        return icon;
    }

    public Icon getUseCaseIcon(DbObject object) throws DbException {
        Icon icon = null;
        if (!(object instanceof DbBEUseCase))
            throw new RuntimeException("Invalid object.");

        Terminology term = findModelTerminology(object);
        if (((DbBEUseCase) object).isContext())
            icon = term.getIcon(DbBEUseCaseGo.metaClass);
        else
            icon = term.getIcon(DbBEUseCase.metaClass);
        return icon;
    }

    public boolean isObjectUseCase(DbObject dbObject) {
        if (dbObject instanceof DbBEUseCase)
            return true;
        return false;
    }

    public boolean isObjectUseCaseContext(DbObject dbObject) throws DbException {
        if (!isObjectUseCase(dbObject))
            return false;

        boolean isContext = false;
        dbObject.getDb().beginReadTrans();
        isContext = ((DbBEUseCase) dbObject).isContext();
        dbObject.getDb().commitTrans();
        return isContext;
    }

    public boolean isObjectLine(DbObject dbObject) {
        if (dbObject instanceof DbORAssociation)
            return true;
        else if (dbObject instanceof DbBEFlow)
            return true;
        else if (dbObject instanceof DbOOAssociation)
            return true;
        else if (dbObject instanceof DbOOInheritance)
            return true;
        return false;
    }

    public boolean isObjectRole(DbObject dbObject) {
        return dbObject instanceof DbORAssociationEnd;
    }

    public boolean isObjectAssociation(DbObject dbObject) {
        if (false == (dbObject instanceof DbORTable))
            return false;

        boolean isAssociation = false;
        try {
            DbORTable table = (DbORTable) dbObject;
            table.getDb().beginReadTrans();
            isAssociation = table.isIsAssociation();
            table.getDb().commitTrans();
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
        return isAssociation;
    }

    public void initTerminology(Terminology terminology, DbObject dbObject) throws DbException {
    	TerminologyInitializer.initTerminology(terminology, dbObject); 
    }

    public void initTerminology(Terminology terminology, String name) throws DbException {
        TerminologyInitializer.initTerminology(terminology, name);
    }

    public String getTerminologyName(DbObject notation) throws DbException {
        String name = null;
        if (notation instanceof DbBENotation) {
            DbBENotation benotation = (DbBENotation) notation;
            Db db = benotation.getDb();
            if (db.getTransMode() == Db.TRANS_NONE) {
                db.beginReadTrans();
                name = benotation.getTerminologyName();
                db.commitTrans();
            } else
                name = benotation.getTerminologyName();
        } else if (notation instanceof DbORNotation) {
            DbORNotation ornotation = (DbORNotation) notation;
            Db db = ornotation.getDb();
            if (db.getTransMode() == Db.TRANS_NONE) {
                db.beginReadTrans();
                name = ornotation.getTerminologyName();
                db.commitTrans();
            } else
                name = ornotation.getTerminologyName();
        }
        return name;
    }

    public Icon getConceptualModelIcon() {
        return SMSExplorer.erModelIcon;
    }

    public Icon getArcIcon() {
        return SMSExplorer.arcIcon;
    }

    public int getModelLogicalMode(DbObject dbObject) {
        int mode = LOGICAL_MODE_OBJECT_RELATIONAL;

        if (dbObject instanceof DbORDataModel) {
            try {
                DbORDataModel dataModel = (DbORDataModel) dbObject;
                Db db = dataModel.getDb();
                if (db.getTransMode() == Db.TRANS_NONE) {
                    db.beginReadTrans();
                    mode = dataModel.getOperationalMode();
                    db.commitTrans();
                } else
                    mode = dataModel.getOperationalMode();
            } catch (Exception e) {
                org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                        ApplicationContext.getDefaultMainFrame(), e);
            }
        }
        return mode;
    }

    public boolean isConvertedDataModel(DbObject dbObject) {
        if (!(dbObject instanceof DbORDataModel))
            return false;

        if (getModelLogicalMode(dbObject) != LOGICAL_MODE_ENTITY_RELATIONSHIP)
            return false;

        boolean isConvertedDataModel = false;
        if (dbObject instanceof DbORDataModel) {
            try {
                DbORDataModel dataModel = (DbORDataModel) dbObject;
                Db db = dataModel.getDb();
                if (db.getTransMode() == Db.TRANS_NONE) {
                    dataModel.getDb().beginReadTrans();
                    isConvertedDataModel = dataModel.isIsConverted();
                    dataModel.getDb().commitTrans();
                } else
                    isConvertedDataModel = dataModel.isIsConverted();
            } catch (Exception e) {
                org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                        ApplicationContext.getDefaultMainFrame(), e);
            }
        }
        return isConvertedDataModel;
    }

    public DbObject isCompositeDataModel(DbObject dbObject) {
        try {
            DbObject dbObjectModel = null;
            Db db = dbObject.getDb();
            if (db.getTransMode() == Db.TRANS_NONE) {
                db.beginReadTrans();
                dbObjectModel = dbObject.getCompositeOfType(DbORDataModel.metaClass);
                db.commitTrans();
            } else
                try {
                    dbObjectModel = dbObject.getCompositeOfType(DbORDataModel.metaClass);
                } catch (DbDeadObjectException dbe) {
                    return null;
                }

            return dbObjectModel;
        } catch (DbException dbe) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), dbe);
        }
        return null;
    }

    public boolean isObjectArcEndRole(DbObject dbObject) {
        if (!(dbObject instanceof DbORAssociationEnd))
            return false;

        boolean bIsIt = false;
        try {
            DbORAssociationEnd associationEnd = (DbORAssociationEnd) dbObject;
            associationEnd.getDb().beginReadTrans();
            DbORAssociation association = (DbORAssociation) associationEnd
                    .getCompositeOfType(DbORAssociation.metaClass);
            associationEnd.getDb().commitTrans();

            association.getDb().beginReadTrans();
            if (association.isIsArc() == true) {
                if (association.getArcEnd() == dbObject)
                    bIsIt = true;
            }
            association.getDb().commitTrans();
        } catch (DbException dbe) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), dbe);
        }

        return bIsIt;
    }

    public Terminology findModelTerminology(DbObject orModel) throws DbException {

        if (!(orModel instanceof DbORDataModel) || orModel == null) {
            if (orModel == null) {
                return Terminology.findTerminologyByName(DbORNotation.DATARUN);
            } else {
                try {
                    if (!(orModel instanceof DbBEModel) && !(orModel instanceof DbBEUseCase))
                        orModel = orModel.getCompositeOfType(DbBEModel.metaClass);

                    if (orModel instanceof DbBEModel) {
                        DbBEModel beModel = (DbBEModel) orModel;
                        beModel.getDb().beginReadTrans();
                        String name = beModel.getTerminologyName();
                        beModel.getDb().commitTrans();
                        if (name != null && !name.equals(""))
                            return Terminology.findTerminologyByName(name);
                        else
                            return Terminology.findTerminologyByName(DbBENotation.GANE_SARSON);
                    } else if (orModel instanceof DbBEUseCase) {
                        DbBEUseCase useCase = (DbBEUseCase) orModel;
                        useCase.getDb().beginReadTrans();
                        String name = useCase.getTerminologyName();
                        useCase.getDb().commitTrans();
                        if (name == null) {
                            DbBEUseCase explodedOrContext = (DbBEUseCase) useCase.getComposite();
                            name = explodedOrContext.getTerminologyName();
                        } else if (name.equals("")) {
                            if (useCase.isContext())
                                name = ((DbBEModel) useCase.getComposite()).getTerminologyName();
                            else
                                name = ((DbBEUseCase) useCase.getComposite()).getTerminologyName();
                        }
                        if (name == null)
                            throw new RuntimeException("Failed to get the terminology");
                        return Terminology.findTerminologyByName(name);
                    }
                } catch (Exception ex) {
                    DbSMSProject project = (DbSMSProject) ApplicationContext.getFocusManager()
                            .getCurrentProject();
                    try {
                        if (project != null) {
                            project.getDb().beginReadTrans();
                            DbBENotation notation = project.getBeDefaultNotation();
                            project.getDb().commitTrans();
                            return Terminology.findTerminology(notation);
                        } else {
                            return Terminology.findTerminologyByName(DbBENotation.GANE_SARSON);
                        }
                    } catch (Exception e) {
                        return Terminology.findTerminologyByName(DbBENotation.GANE_SARSON);
                    }
                }
            }

        }

        if (orModel == null) {
            return Terminology.findTerminologyByName(DbORNotation.DATARUN);
        }

        Terminology term = null;
        DbORDataModel model = (DbORDataModel) orModel;
        int operationalMode = DbORDataModel.LOGICAL_MODE_OBJECT_RELATIONAL;

        try {
            operationalMode = model.getOperationalMode();
            DbRelationN relN = model.getComponents();
            DbEnumeration dbEnum = relN.elements(DbORDiagram.metaClass);

            ArrayList array = new ArrayList();
            HashSet notationsCollection = new HashSet();

            while (dbEnum.hasMoreElements()) {
                DbObject object = dbEnum.nextElement();

                if (object instanceof DbORDiagram) {
                    DbORDiagram dgm = (DbORDiagram) object;
                    DbORNotation nota = dgm.getNotation();
                    if (nota == null) {
                        if (operationalMode == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                            nota = ((DbSMSProject) model.getCompositeOfType(DbSMSProject.metaClass))
                                    .getErDefaultNotation();
                        else
                            nota = ((DbSMSProject) model.getCompositeOfType(DbSMSProject.metaClass))
                                    .getOrDefaultNotation();
                    }
                    notationsCollection.add(nota);
                }
            }
            dbEnum.close();

            // //
            // if the set is empty, or there is more than one notation in the
            // set
            // return the terminology for the project's default notation

            if (notationsCollection.isEmpty() || notationsCollection.size() > 1) {
                DbORNotation notation = null;
                if (operationalMode == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                    notation = ((DbSMSProject) model.getCompositeOfType(DbSMSProject.metaClass))
                            .getErDefaultNotation();
                else
                    notation = ((DbSMSProject) model.getCompositeOfType(DbSMSProject.metaClass))
                            .getOrDefaultNotation();

                term = Terminology.findTerminology(notation);
            } else {
                DbORNotation notation = (DbORNotation) notationsCollection.iterator().next();
                term = Terminology.findTerminology(notation);
            }
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }

        return term;
    }

    public boolean isObjectUseCaseOrBEModel(DbObject dbObject) {

        if (dbObject instanceof DbBEUseCase || dbObject instanceof DbBEModel)
            return true;

        return false;
    }

    public boolean isPureERSet(DbObject[] dbos) {

        // //
        // find out if this set contains any restricted objects in the ER mode

        if (dbos == null)
            return false;

        if (dbos.length < 1)
            return false;

        boolean bPure = true;

        for (int i = 0; i < dbos.length; i++) {
            if (dbos[i] instanceof DbORTrigger) {
                bPure = false;
                break;
            } else if (dbos[i] instanceof DbORIndex) {
                bPure = false;
                break;
            } else if (dbos[i] instanceof DbORView) {
                bPure = false;
                break;
            }
        }
        if (bPure == true) { // View is a special case, this is the relational
            // concept
            try {
                for (int i = 0; i < dbos.length; i++) {
                    dbos[i].getDb().beginReadTrans();
                    if (dbos[i] instanceof DbORColumn) {
                        if (dbos[i].getComposite() instanceof DbGEView)
                            bPure = false;
                    }
                    if (dbos[i] instanceof DbORCheck) {
                        if (dbos[i].getComposite() instanceof DbGEView)
                            bPure = false;
                    }
                    if (dbos[i] instanceof DbORIndex) {
                        if (dbos[i].getComposite() instanceof DbGEView)
                            bPure = false;
                    }
                    if (dbos[i] instanceof DbORPrimaryUnique) {
                        if (dbos[i].getComposite() instanceof DbGEView)
                            bPure = false;
                    }
                    dbos[i].getDb().commitTrans();

                }
            } catch (DbException dbe) {
                bPure = false;
            }
        }
        return bPure;
    }

    // //
    // restricted to low-level use (core/jack)

    public int validateSelectionModel() {

        int nMode = LOGICAL_MODE_OBJECT_RELATIONAL;
        try {
            DbObject[] dbObject = ApplicationContext.getFocusManager()
                    .getSelectedSemanticalObjects();
            for (int i = 0; i < dbObject.length; i++) {
                dbObject[i].getDb().beginReadTrans();
                if (dbObject[i] instanceof DbORDataModel) {
                    DbORDataModel dataModel = (DbORDataModel) dbObject[i];
                    if (dataModel.getOperationalMode() == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                        nMode = LOGICAL_MODE_ENTITY_RELATIONSHIP;
                        dbObject[i].getDb().commitTrans();
                        break;
                    }
                } else {
                    DbObject otherdbObject = dbObject[i]
                            .getCompositeOfType(DbORDataModel.metaClass);
                    if (otherdbObject != null) {
                        DbORDataModel dataModel = (DbORDataModel) otherdbObject;
                        if (dataModel.getOperationalMode() == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                            nMode = LOGICAL_MODE_ENTITY_RELATIONSHIP;
                            dbObject[i].getDb().commitTrans();
                            break;
                        }
                    }
                }
                dbObject[i].getDb().commitTrans();
            }
        } catch (DbException dbe) {
            throw new RuntimeException("Could not determine the current OR logical mode.");
        }
        return nMode;
    }

    public DbTreeLookupDialog getTreeLookupDialog(Component comp, String title, DbTreeModel model,
            boolean multipleSelection, boolean isUML) {
        DbSMSTreeLookupDialog treeLookupDialog = new DbSMSTreeLookupDialog(comp, title, model,
                multipleSelection, isUML);
        return treeLookupDialog;
    }

    public boolean isUML(DbObject dbObject) throws DbException {

        if (dbObject instanceof DbBEModel) {
            DbBEModel model = (DbBEModel) dbObject;
            model.getDb().beginReadTrans();
            String terminoloyName = model.getTerminologyName();
            model.getDb().commitTrans();

            if (terminoloyName == null) // to prevent an exceptional case when
                // the user goes back in the wizard
                // pages
                return false;

            DbBENotation notation = (DbBENotation) model.getProject().findComponentByName(
                    DbBENotation.metaClass, terminoloyName);
            notation.getDb().beginReadTrans();
            int id = notation.getMasterNotationID().intValue();
            notation.getDb().commitTrans();
            if (id >= 14 && id <= 19)
                return true;
        }
        if (dbObject instanceof DbBEDiagram) {
            DbBEDiagram diagram = (DbBEDiagram) dbObject;
            diagram.getDb().beginReadTrans();
            DbBENotation notation = diagram.findNotation();
            diagram.getDb().commitTrans();
            notation.getDb().beginReadTrans();
            int id = notation.getMasterNotationID().intValue();
            notation.getDb().commitTrans();
            if (id >= 14 && id <= 19)
                return true;
        }
        return false;
    }

    public boolean isClassModel(DbObject dbObject) throws DbException {

        if (dbObject instanceof DbOOClassModel)
            return true;
        return false;
    }

    public boolean isObjectDiagram(DbObject dbObject) throws DbException {

        if (dbObject instanceof DbSMSDiagram)
            return true;
        return false;

    }

}
