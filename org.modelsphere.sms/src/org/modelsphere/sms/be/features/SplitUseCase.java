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

package org.modelsphere.sms.be.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingUtilities;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.DbUDF;
import org.modelsphere.jack.baseDb.db.DeepCopyCustomizer;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.gui.task.DefaultController;
import org.modelsphere.jack.gui.task.Worker;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEActorQualifier;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEFlowQualifier;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEQualifier;
import org.modelsphere.sms.be.db.DbBEQualifierLink;
import org.modelsphere.sms.be.db.DbBEResource;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEStoreQualifier;
import org.modelsphere.sms.be.db.DbBEStyle;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.DbBEUseCaseGo;
import org.modelsphere.sms.be.db.DbBEUseCaseQualifier;
import org.modelsphere.sms.be.db.DbBEUseCaseResource;
import org.modelsphere.sms.be.db.util.BEUtility;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSUmlConstraint;
import org.modelsphere.sms.db.DbSMSUmlExtensibility;

public final class SplitUseCase {
    private static final int REPORT_INDENT = 4;

    private DbBEUseCase usecase;
    private DbBEUseCase context;
    private DbBEModel sourceModel;
    private DbBEModel targetModel;
    private DeepCopyCustomizer copyCustomizer = new CopyCustomizer();
    // This hashmap contains source usecase - target usecase mapping for
    // usecases that become external.
    // This is used for restoring link between the usecase and other objects
    // involved in the split.
    private HashMap refUseCase = new HashMap();
    // Mapping for actors, stores, styles and notations (the Db mapping facility
    // does not match the 2 models
    private HashMap refComponents = new HashMap();

    private boolean sameProject = false;
    private DbSMSProject newproject = null;

    private DefaultController controler = null;

    private class CopyCustomizer implements DeepCopyCustomizer {
        CopyCustomizer() {
        }

        public MetaClass getDestMetaClass(DbObject srcObj, DbObject destComposite)
                throws DbException {
            return srcObj.getMetaClass();
        }

        public void initFields(DbObject srcObj, DbObject destObj, boolean namePrefixedWithCopyOf)
                throws DbException {
        }

        public DbObject resolveLink(DbObject srcObj, MetaRelationship metaRel, DbObject srcNeighbor)
                throws DbException {
            // if ((metaRel.getFlags() & MetaField.COPY_REFS) == 0)
            // return null;
            if (metaRel == DbBEUseCaseQualifier.fQualifier
                    || metaRel == DbBEActorQualifier.fQualifier
                    || metaRel == DbBEFlowQualifier.fQualifier
                    || metaRel == DbBEStoreQualifier.fQualifier) {
                return targetModel.findComponentByName(DbBEQualifier.metaClass, srcNeighbor
                        .getName());
            }
            if (metaRel == DbBEUseCaseResource.fResource) {
                return targetModel.findComponentByName(DbBEResource.metaClass, srcNeighbor
                        .getName());
            }
            // END BLOCK

            // Resolve styles or notations
            if ((srcNeighbor instanceof DbBENotation) || (srcNeighbor instanceof DbBEStyle)) {
                MetaClass metaclass = srcNeighbor instanceof DbBENotation ? DbBENotation.metaClass
                        : DbBEStyle.metaClass;
                if (newproject != null) {
                    return newproject.findComponentByName(metaclass, srcNeighbor.getName());
                }
            }

            // Resolve external process
            if (srcNeighbor instanceof DbBEUseCase) {
                DbObject targetNeighbor = srcNeighbor.findMatchingObject();
                if (srcNeighbor == usecase)
                    return context;
                if (targetNeighbor != null)
                    return targetNeighbor;
                return (DbObject) refUseCase.get(srcNeighbor);
            }

            DbObject targetNeighbor = srcNeighbor.findMatchingObject();
            if (targetNeighbor != null)
                return targetNeighbor;
            if (srcNeighbor instanceof DbBEActor || srcNeighbor instanceof DbBEStore) {
                return (DbObject) refComponents.get(srcNeighbor);
            }
            return targetNeighbor;
        }

        public void endCopy(ArrayList srcRoots) throws DbException {
        }
    }

    public SplitUseCase(DbBEUseCase usecase) {
        if (usecase == null)
            return;
        this.usecase = usecase;
        try {
            newproject = (DbSMSProject) ApplicationContext.getDefaultMainFrame()
                    .createDefaultProject(null);
        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
            return;
        }
        Worker worker = new Worker() {
            protected void runJob() throws Exception {
                SplitUseCase.this.usecase.getDb().beginWriteTrans(
                        LocaleMgr.action.getString("Split"));
                sourceModel = (DbBEModel) SplitUseCase.this.usecase
                        .getCompositeOfType(DbBEModel.metaClass);
                if (!SplitUseCase.this.usecase.isContext()) {
                    if (newproject != null) {
                        newproject.getDb().beginWriteTrans(LocaleMgr.action.getString("Split"));
                        newproject.setName(LocaleMgr.misc.getString("SplitProject"));
                        targetModel = new DbBEModel(newproject);
                        targetModel.setTerminologyName(sourceModel.getTerminologyName());
                        targetModel.setName(LocaleMgr.misc.getString("SplitModel"));
                        doSplit();
                        newproject.getDb().commitTrans();
                        newproject.getDb().resetHistory();
                    }
                }
                SplitUseCase.this.usecase.getDb().commitTrans();
            }

            protected String getJobTitle() {
                return LocaleMgr.misc.getString("SplitJob");
            }
        };
        controler = new DefaultController(LocaleMgr.misc.getString("SplitTitle"), true, null);
        controler.start(worker);
        // If aborted or error, try removing the new project
        if (controler.getState() == Controller.STATE_ABORTED
                || controler.getState() == Controller.STATE_ERROR) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    try {
                        Db db = newproject.getDb();
                        db.beginTrans(Db.WRITE_TRANS);
                        newproject.remove();
                        db.commitTrans();
                        db.terminate();
                    } catch (Exception e) {
                        ExceptionHandler.processUncatchedException(ApplicationContext
                                .getDefaultMainFrame(), e);
                    }
                }
            });
        }
    }

    private void doSplit() throws DbException {
        // First copy all the components needed - order is important
        if (!controler.checkPoint(0))
            return;
        controler.println();
        controler.println(LocaleMgr.message.getString("CopyUDFs"));
        copyUDFs();
        if (!controler.checkPoint(10))
            return;
        controler.println();
        controler.println(LocaleMgr.message.getString("CopyNotations"));
        copyNotations();
        if (!controler.checkPoint(20))
            return;
        controler.println();
        controler.println(LocaleMgr.message.getString("CopyStyles"));
        copyStyles();
        if (!controler.checkPoint(30))
            return;
        controler.println();
        controler.println(LocaleMgr.message.getString("CopyUsedComponents"));
        copyModelComponents();

        // Copy root process
        if (!controler.checkPoint(50))
            return;
        controler.println();
        controler.println(LocaleMgr.message.getString("CreatingNewContext"));
        copyRootAsContext();

        // Copy all sub-process and external process
        if (!controler.checkPoint(60))
            return;
        controler.println();
        controler.println(LocaleMgr.message.getString("CreatingExternalProcesses"));
        boolean ok = copyExternalUseCases();
        /*
         * if (!ok){ controler.cancel(); }
         */
        if (!controler.checkPoint(70))
            return;
        controler.println();
        controler.println(LocaleMgr.message.getString("CreatingContextComponents"));
        copyContextComponents();

        // Clear all references to dbos
        if (!controler.checkPoint(80))
            return;
        refUseCase.clear();

        // Set the state of the splited process as external
        usecase.setExternal(Boolean.TRUE);

        // Remove components from the splited process
        if (!controler.checkPoint(90))
            return;
        controler.println();
        controler.println(LocaleMgr.message.getString("RemovingSourceComponents"));
        removeSourceComponents();
    }

    private void copyUDFs() throws DbException {
        copyUDFsFor(DbBEUseCase.metaClass);
        copyUDFsFor(DbBEActor.metaClass);
        copyUDFsFor(DbBEStore.metaClass);
        copyUDFsFor(DbBEFlow.metaClass);
        copyUDFsFor(DbBEResource.metaClass);
        copyUDFsFor(DbBEQualifier.metaClass);
        copyUDFsFor(DbBEActorQualifier.metaClass);
        copyUDFsFor(DbBEStoreQualifier.metaClass);
        copyUDFsFor(DbBEUseCaseQualifier.metaClass);
        copyUDFsFor(DbBEFlowQualifier.metaClass);
        copyUDFsFor(DbBEUseCaseResource.metaClass);
        copyUDFsFor(DbBEQualifierLink.metaClass);
    }

    private void copyUDFsFor(MetaClass metaclass) throws DbException {
        List udfs = Arrays.asList(DbUDF.getUDF(sourceModel.getProject(), metaclass));
        Iterator iter = udfs.iterator();
        while (iter.hasNext()) {
            println(iter.next(), REPORT_INDENT);
        }
        deepCopy(udfs, targetModel.getProject());
    }

    private void println(Object o, int indent) throws DbException {
        println(o, indent, false, null);
    }

    private void println(Object o, int indent, boolean includeObjectType) throws DbException {
        println(o, indent, includeObjectType, null);
    }

    private void println(Object o, int indent, boolean includeObjectType, String warning)
            throws DbException {
        if (o == null)
            return;
        if (o instanceof DbObject) {
            String line = "";
            for (int i = 0; i < indent; i++) {
                line += " "; // NOT LOCALIZABLE
            }
            line += "-"; // NOT LOCALIZABLE
            if (includeObjectType)
                line += "(" + ((DbObject) o).getMetaClass().getGUIName(false, false) + ") "; // NOT
            // LOCALIZABLE
            line += ApplicationContext.getSemanticalModel().getDisplayText((DbObject) o,
                    DbSemanticalObject.SHORT_FORM, null, null);
            controler.println(line);
        } else
            controler.println(o.toString());

        if (warning != null) {
            String line = "";
            for (int i = 0; i < indent; i++) {
                line += " "; // NOT LOCALIZABLE
            }
            line += "  "; // NOT LOCALIZABLE
            line += warning;
            controler.println(line);
            controler.incrementWarningsCounter();
        }
    }

    private void copyStyles() throws DbException {
        DbEnumeration dbEnum = sourceModel.getProject().getComponents().elements(
                DbBEStyle.metaClass);
        ArrayList styles = new ArrayList();
        while (dbEnum.hasMoreElements()) {
            // todo filter used styles only ???
            DbObject dbo = dbEnum.nextElement();
            if (!sameProject
                    && (targetModel.getProject().findComponentByName(DbBEStyle.metaClass,
                            dbo.getName()) != null))
                continue;
            styles.add(dbo);
            println(dbo, REPORT_INDENT);
        }
        dbEnum.close();
        deepCopy(styles, targetModel.getProject());
    }

    private void copyNotations() throws DbException {
        DbEnumeration dbEnum = sourceModel.getProject().getComponents().elements(
                DbBENotation.metaClass);
        ArrayList notations = new ArrayList();
        while (dbEnum.hasMoreElements()) {
            // todo filter used notations only ???
            DbObject dbo = dbEnum.nextElement();
            if (!sameProject
                    && (targetModel.getProject().findComponentByName(DbBENotation.metaClass,
                            dbo.getName()) != null))
                continue;
            println(dbo, REPORT_INDENT);
            notations.add(dbo);
        }
        dbEnum.close();
        deepCopy(notations, targetModel.getProject());

        // set the same default notation for the new project
        DbBENotation defaultNotation = (DbBENotation) sourceModel.getProject().get(
                DbSMSProject.fBeDefaultNotation);
        if (defaultNotation != null) {
            DbBENotation targetNotation = (DbBENotation) targetModel.getProject()
                    .findComponentByName(DbBENotation.metaClass, defaultNotation.getName());
            targetModel.getProject().set(DbSMSProject.fBeDefaultNotation, targetNotation);
        }
    }

    private void copyRootAsContext() throws DbException {
        context = duplicateIn(usecase, targetModel);
    }

    private DbBEUseCase duplicateIn(DbBEUseCase source, DbObject composite) throws DbException {
        if (source == null || composite == null)
            return null;

        BEUtility util = BEUtility.getSingleInstance();
        DbBENotation notation = findNotation(source);
        DbBEUseCase duplicate = util.createUseCase(composite, notation);

        MetaField[] metafields = DbBEUseCase.metaClass.getAllMetaFields();
        // copy non relationship metafields
        for (int i = 0; i < metafields.length; i++) {
            if (metafields[i] instanceof MetaRelationship)
                continue;
            duplicate.set(metafields[i], source.get(metafields[i]));
        }
        // copy metafields values
        DbUDF[] udfs = DbUDF.getUDF(source.getProject(), DbBEUseCase.metaClass);
        for (int i = 0; i < udfs.length; i++) {
            DbUDF targetUDF = DbUDF.getUDF(composite.getProject(), DbBEUseCase.metaClass, udfs[i]
                    .getName());
            if (targetUDF == null) // should not happen
                continue;
            duplicate.set(targetUDF, source.get(udfs[i]));
        }

        // copy all components excepts usecases and flows -
        // they must be copied after the mapping with external usecase has been
        // created.
        // external usecase relative to source can not be created without
        // creating their target composite (the root context in target)
        // Basically, those components are the link objects between the root
        // usecase and resources or qualifiers
        ArrayList components = new ArrayList();
        DbEnumeration dbEnum = source.getComponents().elements(DbBEUseCaseResource.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbObject dbo = dbEnum.nextElement();
            components.add(dbo);
            println(dbo, REPORT_INDENT, true);
        }
        dbEnum.close();

        dbEnum = source.getComponents().elements(DbBEUseCaseQualifier.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbObject dbo = dbEnum.nextElement();
            components.add(dbo);
            println(dbo, REPORT_INDENT, true);
        }
        dbEnum.close();

        deepCopy(components, duplicate);
        return duplicate;
    }

    private DbBENotation findNotation(DbBEUseCase source) throws DbException {
        DbBENotation notation = null;
        DbRelationN relN = source.getClassifierGos();
        DbEnumeration dbEnum = relN.elements(DbBEUseCaseGo.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEUseCaseGo go = (DbBEUseCaseGo) dbEnum.nextElement();
            DbBEDiagram diag = (DbBEDiagram) go.getCompositeOfType(DbBEDiagram.metaClass);
            DbBENotation n = diag.getNotation();
            if (n != null) {
                notation = n;
                break;
            } // end if

        } // end while
        dbEnum.close();

        return notation;
    }

    private boolean copyExternalUseCases() throws DbException {
        // First copy any usecases not component (or sub-component) of the
        // source usecase (we must check this and not just check for graphical
        // object
        // because a process can be link with a flow and without any graphical
        // representations.)
        ArrayList externalUseCases = new ArrayList();
        DbEnumeration dbEnum = usecase.componentTree(DbBEFlow.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEFlow flow = (DbBEFlow) dbEnum.nextElement();
            DbObject end1 = flow.getFirstEnd();
            DbObject end2 = flow.getSecondEnd();
            if (isExternal(end1, usecase))
                externalUseCases.add(end1);
            if (isExternal(end2, usecase))
                externalUseCases.add(end2);
        }
        dbEnum.close();

        // Second pass: Check for external process using graphical
        // representations.
        dbEnum = usecase.componentTree(DbBEUseCaseGo.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEUseCaseGo go = (DbBEUseCaseGo) dbEnum.nextElement();
            DbObject semObj = go.getSO();
            if (isExternal(semObj, usecase))
                externalUseCases.add(semObj);
        }
        dbEnum.close();

        // for each external use case identified in source root usecase, create
        // an empty duplicate (almost, link to qualifier and
        // resources must be included) and add an entry in the hashmap (will be
        // used to restore links in DeepCopyCustomizer)
        // We must also set the duplicated usecase (in target) as external and
        // backup source identifiers (for restoring the links
        // in GroupUseCase feature.
        Iterator iter = externalUseCases.iterator();
        while (iter.hasNext()) {
            DbObject dbo = (DbObject) iter.next();
            if (!(dbo instanceof DbBEUseCase))
                continue;
            DbBEUseCase sourceusecase = (DbBEUseCase) dbo;
            // check for duplicate
            if (refUseCase.containsKey(sourceusecase))
                continue;
            DbBEUseCase externalusecase = duplicateIn(sourceusecase, context);
            if (externalusecase == null)
                continue;
            externalusecase.setExternal(Boolean.TRUE);
            String alphaId = sourceusecase.getAlphanumericIdentifier();
            String numericId = sourceusecase.getNumericIdentifier() == null ? null : sourceusecase
                    .getNumericIdentifier().toString();
            DbObject composite = sourceusecase.getComposite();

            while (composite instanceof DbBEUseCase) {
                if (alphaId != null) {
                    String tempAlphaId = ((DbBEUseCase) composite).getAlphanumericIdentifier();
                    if (tempAlphaId == null) {
                        alphaId = null;
                    } else {
                        alphaId = "." + alphaId;
                        alphaId = tempAlphaId + alphaId;
                    }
                }
                if (numericId != null) {
                    Integer tempNumId = ((DbBEUseCase) composite).getNumericIdentifier();
                    if (tempNumId == null) {
                        numericId = null;
                    } else {
                        numericId = "." + numericId;
                        numericId = tempNumId.toString() + numericId;
                    }
                }
                composite = composite.getComposite();
            }
            externalusecase.setSourceAlphanumericIdentifier(alphaId);
            externalusecase.setSourceNumericIdentifier(numericId);

            if (((alphaId != null && alphaId.length() == 0) || alphaId == null)
                    && ((numericId != null && numericId.length() == 0) || numericId == null)) {
                println(dbo, REPORT_INDENT, true, LocaleMgr.message.getString("noExternalID"));
                // return false;
            } else
                println(dbo, REPORT_INDENT, true, null);
            refUseCase.put(sourceusecase, externalusecase);
        }
        return true;
    }

    // This will deep copy recursively the usecases, flows and diagrams of the
    // context object.
    // Note that for the direct components of context dbo, we must filter the
    // components already copied.
    // (DbBEUseCaseResource.metaClass, DbBEUseCaseQualifier.metaClass has been
    // processed previously)
    private void copyContextComponents() throws DbException {
        ArrayList dbos = new ArrayList();
        DbEnumeration dbEnum = usecase.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            DbObject dbo = (DbObject) dbEnum.nextElement();
            if (!(dbo instanceof DbBEUseCaseResource) && !(dbo instanceof DbBEUseCaseQualifier)) {
                dbos.add(dbo);
                println(dbo, REPORT_INDENT, true);
            }
        }
        dbEnum.close();
        deepCopy(dbos, context);
    }

    // will return true if dbo does not have relativeTo as composite or super
    // composite
    private boolean isExternal(DbObject dbo, DbObject relativeTo) throws DbException {
        if (dbo == null)
            return false;
        if (!(dbo instanceof DbBEUseCase))
            return false;
        DbObject composite = dbo.getComposite();
        while (composite != null && !(composite instanceof DbBEModel)) {
            if (composite == relativeTo)
                return false;
            composite = composite.getComposite();
        }
        return true;
    }

    private void removeSourceComponents() throws DbException {
        DbEnumeration dbEnum = usecase.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            DbObject dbo = dbEnum.nextElement();
            // preserve usecase-resource links and usecase-qualifier links
            if (dbo instanceof DbBEUseCaseQualifier || dbo instanceof DbBEUseCaseResource)
                continue;
            dbo.remove();
        }
        dbEnum.close();
    }

    // copy UML Extensibilities, actors, stores, qualifiers and resources
    private void copyModelComponents() throws DbException {
        ArrayList components = new ArrayList();
        DbEnumeration dbEnum = null;
        if (!sameProject) {
            // copy missing UML Extensibilities
            DbSMSUmlExtensibility targetUML = ((DbSMSProject) targetModel.getProject())
                    .getUmlExtensibility();
            dbEnum = ((DbSMSProject) sourceModel.getProject()).getUmlExtensibility()
                    .getComponents().elements(DbSMSUmlConstraint.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbSMSUmlConstraint dbo1 = (DbSMSUmlConstraint) dbEnum.nextElement();
                DbSMSUmlConstraint dbo2 = (DbSMSUmlConstraint) targetUML.findComponentByName(
                        DbSMSUmlConstraint.metaClass, dbo1.getName());
                if (dbo2 == null) {
                    components.add(dbo1);
                    println(dbo1, REPORT_INDENT, true);
                }
            }
            dbEnum.close();

            dbEnum = ((DbSMSProject) sourceModel.getProject()).getUmlExtensibility()
                    .getComponents().elements(DbSMSStereotype.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbSMSStereotype dbo1 = (DbSMSStereotype) dbEnum.nextElement();
                DbSMSStereotype dbo2 = (DbSMSStereotype) targetUML.findComponentByName(
                        DbSMSStereotype.metaClass, dbo1.getName());
                if (dbo2 == null) {
                    components.add(dbo1);
                    println(dbo1, REPORT_INDENT, true);
                }
            }
            dbEnum.close();
            deepCopy(components, targetUML);
            components.clear();
        }

        dbEnum = sourceModel.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            DbObject component = dbEnum.nextElement();
            if ((component instanceof DbBEStore) || (component instanceof DbBEActor)
                    || (component instanceof DbBEQualifier) || (component instanceof DbBEResource)) {
                components.add(component);
                println(component, REPORT_INDENT, true);
            }
        }
        dbEnum.close();
        DbObject[] targetDbos = deepCopy(components, targetModel);
        if (targetDbos == null)
            return;
        for (int i = 0; i < targetDbos.length; i++) {
            refComponents.put(components.get(i), targetDbos[i]);
        }
    }

    private DbObject[] deepCopy(List components, DbObject targetComposite) throws DbException {
        if (components == null || components.size() == 0)
            return null;
        DbObject[] sourceDbos = new DbObject[components.size()];
        for (int i = 0; i < sourceDbos.length; i++)
            sourceDbos[i] = (DbObject) components.get(i);

        return DbObject.deepCopy(sourceDbos, targetComposite, copyCustomizer, false);
    }
}
