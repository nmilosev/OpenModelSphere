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
import java.util.StringTokenizer;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.DbUDF;
import org.modelsphere.jack.baseDb.db.DeepCopyCustomizer;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.gui.task.DefaultController;
import org.modelsphere.jack.gui.task.Worker;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.text.MessageFormat;
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
import org.modelsphere.sms.be.db.DbBEUseCaseQualifier;
import org.modelsphere.sms.be.db.DbBEUseCaseResource;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSUmlConstraint;
import org.modelsphere.sms.db.DbSMSUmlExtensibility;

public final class MergeUseCase {
    private static final String kCouldNotResolve0 = LocaleMgr.message.getString("CouldNotResolve0");
    private static final String kNoNumId = LocaleMgr.message.getString("NoNumId");
    private static final String kNoAlphaId = LocaleMgr.message.getString("NoAlphaId");
    private static final int REPORT_INDENT = 4;

    private DbBEUseCase target;
    private DbBEUseCase source;
    private DbBEModel sourceModel;
    private DbBEModel targetModel;
    private DeepCopyCustomizer copyCustomizer = new CopyCustomizer();
    // This hashmap contains source usecase - target usecase mapping for
    // usecases external process
    // This is used for restoring link between the usecase and other objects
    // involved in the merge.
    private HashMap refUseCase = new HashMap();
    // Mapping for actors, stores, notations and styles (the Db mapping facility
    // does not match the 2 models
    private HashMap refComponents = new HashMap();

    private boolean sameProject = false;

    private boolean update;
    private boolean mergeQualifiers;
    private boolean mergeResources;
    private boolean mergeComments;

    private DefaultController controler = null;

    // contains MergeInfo objects to update at the end
    private ArrayList toMerge = new ArrayList();

    private class MergeInfo {
        DbObject source;
        DbObject target;

        MergeInfo(DbObject source, DbObject target) {
            this.source = source;
            this.target = target;
        }
    }

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

            // Resolve external process
            if (srcNeighbor instanceof DbBEUseCase) {
                DbObject targetNeighbor = srcNeighbor.findMatchingObject();
                if (srcNeighbor == source)
                    return target;
                if (targetNeighbor != null)
                    return targetNeighbor;
                return (DbObject) refUseCase.get(srcNeighbor);
            }

            // Resolve styles or notations
            if ((srcNeighbor instanceof DbBENotation) || (srcNeighbor instanceof DbBEStyle)) {
                MetaClass metaclass = srcNeighbor instanceof DbBENotation ? DbBENotation.metaClass
                        : DbBEStyle.metaClass;
                return targetModel.getProject().findComponentByName(metaclass,
                        srcNeighbor.getName());
            }

            DbObject targetNeighbor = srcNeighbor.findMatchingObject();
            if (targetNeighbor != null)
                return targetNeighbor;
            if (srcNeighbor instanceof DbBEActor || srcNeighbor instanceof DbBEStore) {
                targetNeighbor = (DbObject) refComponents.get(srcNeighbor);
                if (targetNeighbor == null && srcNeighbor instanceof DbBEActor) {
                    targetNeighbor = targetModel.findComponentByName(DbBEActor.metaClass,
                            srcNeighbor.getName());
                }
                if (targetNeighbor == null && srcNeighbor instanceof DbBEStore) {
                    targetNeighbor = targetModel.findComponentByName(DbBEStore.metaClass,
                            srcNeighbor.getName());
                }
            }
            return targetNeighbor;
        }

        public void endCopy(ArrayList srcRoots) throws DbException {
        }
    }

    public MergeUseCase(DbBEUseCase source, DbBEUseCase target, boolean update,
            boolean mergeQualifiers, boolean mergeResources, boolean mergeComments) {
        if (source == null || target == null)
            return;
        this.source = source;
        this.target = target;
        this.update = update;
        this.mergeQualifiers = update && mergeQualifiers;
        this.mergeResources = update && mergeResources;
        this.mergeComments = update && mergeComments;
        Worker worker = new Worker() {
            protected void runJob() throws Exception {
                if (MergeUseCase.this.source.getDb() != MergeUseCase.this.target.getDb()) {
                    MergeUseCase.this.source.getDb().beginReadTrans();
                } else {
                    MergeUseCase.this.source.getDb().beginWriteTrans(
                            LocaleMgr.action.getString("Merge"));
                }
                MergeUseCase.this.target.getDb().beginWriteTrans(
                        LocaleMgr.action.getString("Merge"));
                MergeUseCase.this.sourceModel = (DbBEModel) MergeUseCase.this.source
                        .getCompositeOfType(DbBEModel.metaClass);
                MergeUseCase.this.targetModel = (DbBEModel) MergeUseCase.this.target
                        .getCompositeOfType(DbBEModel.metaClass);
                sameProject = MergeUseCase.this.target.getProject() == MergeUseCase.this.source
                        .getProject();
                doMerge();
                MergeUseCase.this.source.getDb().commitTrans();
                MergeUseCase.this.target.getDb().commitTrans();
            }

            protected String getJobTitle() {
                return LocaleMgr.screen.getString("MergeJob");
            }
        };
        controler = new DefaultController(LocaleMgr.screen.getString("MergeTitle"), true, null);
        controler.start(worker);
    }

    private void doMerge() throws DbException {
        // First copy all the components needed
        // NOTE: order of execution is important
        if (!controler.checkPoint(0))
            return;
        if (!sameProject) {
            controler.println();
            controler.println(LocaleMgr.message.getString("CopyMissingUDFs"));
            copyMissingUDFs();
        }
        if (!controler.checkPoint(10))
            return;

        if (!sameProject) {
            controler.println();
            controler.println(LocaleMgr.message.getString("CopyMissingNotations"));
            copyMissingNotations();
        }
        if (!controler.checkPoint(20))
            return;

        if (!sameProject) {
            controler.println();
            controler.println(LocaleMgr.message.getString("CopyMissingStyles"));
            copyMissingStyles();
        }
        if (!controler.checkPoint(30))
            return;

        // Set the state of the splited process as not external anymore (will
        // allow update trigger to
        // recalculate)
        target.setExternal(Boolean.FALSE);

        controler.println();
        controler.println(LocaleMgr.message.getString("CopyMissingUsedComponents"));
        copyMissingModelComponents();
        if (!controler.checkPoint(40))
            return;

        controler.println();
        controler.println(LocaleMgr.message.getString("ResolvingExternalProcessess"));
        matchExternalProcesses();
        if (!controler.checkPoint(55))
            return;

        controler.println();
        controler.println(LocaleMgr.message.getString("MergeRootProcess"));
        mergeRootProcess();
        if (!controler.checkPoint(70))
            return;

        controler.println();
        controler.println(LocaleMgr.message.getString("CopyingComponents"));
        copyComponents();
        if (!controler.checkPoint(80))
            return;

        if (update) {
            controler.println();
            controler.println(LocaleMgr.message.getString("UpdatingComponents"));
            mergeComponents();
        }
        if (!controler.checkPoint(95))
            return;

        // Clear all references to dbos
        refUseCase.clear();
    }

    private void println(Object o, int indent) throws DbException {
        println(o, indent, false);
    }

    private void println(Object o, int indent, boolean includeObjectType) throws DbException {
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
    }

    private void copyMissingUDFs() throws DbException {
        copyMissingUDFsFor(DbBEUseCase.metaClass);
        copyMissingUDFsFor(DbBEActor.metaClass);
        copyMissingUDFsFor(DbBEStore.metaClass);
        copyMissingUDFsFor(DbBEFlow.metaClass);
        copyMissingUDFsFor(DbBEResource.metaClass);
        copyMissingUDFsFor(DbBEQualifier.metaClass);
        copyMissingUDFsFor(DbBEActorQualifier.metaClass);
        copyMissingUDFsFor(DbBEStoreQualifier.metaClass);
        copyMissingUDFsFor(DbBEUseCaseQualifier.metaClass);
        copyMissingUDFsFor(DbBEFlowQualifier.metaClass);
        copyMissingUDFsFor(DbBEUseCaseResource.metaClass);
        copyMissingUDFsFor(DbBEQualifierLink.metaClass);
    }

    // iter.remove() can throw an UnsupportedOperationException, that was not
    // catched,
    // generating an error. The exception is catched now. [MS]
    private void copyMissingUDFsFor(MetaClass metaclass) throws DbException {
        List udfs = Arrays.asList(DbUDF.getUDF(sourceModel.getProject(), metaclass));

        ArrayList toCreate = new ArrayList();
        DbProject project = targetModel.getProject();
        Iterator iter = udfs.iterator();
        while (iter.hasNext()) {
            DbUDF udf = (DbUDF) iter.next();

            String name = udf.getName();
            if (DbUDF.getUDF(project, metaclass, name) != null) {
                continue;
            } // end if

            toCreate.add(udf);
            println(udf, REPORT_INDENT);
        } // end while

        deepCopy(toCreate, project);
    } // end copyMissingUDFsFor()

    private void copyMissingStyles() throws DbException {
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

    private void copyMissingNotations() throws DbException {
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
    }

    // copy UML Extensibilities, actors, stores, qualifiers and resources
    private void copyMissingModelComponents() throws DbException {
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
                DbObject targetComponent = targetModel.findComponentByName(
                        component.getMetaClass(), component.getName());
                if (targetComponent == null) {
                    components.add(component);
                    println(component, REPORT_INDENT, true);
                } else {
                    // merge
                    toMerge.add(new MergeInfo(component, targetComponent));
                }
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

    private void mergeRootProcess() throws DbException {
        // replace all components excepts usecases and flows -
        // they must be copied after the mapping with external usecase has been
        // created.
        // Basically, those components are the link objects between the root
        // usecase and resources or qualifiers
        /*
         * ArrayList components = new ArrayList(); DbEnumeration dbEnum =
         * target.getComponents().elements(DbBEUseCaseResource.metaClass); while
         * (dbEnum.hasMoreElements()){ DbObject dbo = dbEnum.nextElement(); components.add(dbo); }
         * dbEnum.close();
         * 
         * dbEnum = source.getComponents().elements(DbBEUseCaseQualifier.metaClass); while
         * (dbEnum.hasMoreElements()){ DbObject dbo = dbEnum.nextElement(); components.add(dbo); }
         * dbEnum.close(); // remove old components for (int i = 0; i< components.size(); i++)
         * ((DbObject)components.get(i)).remove();
         * 
         * // reinsert from source in target components.clear(); dbEnum =
         * target.getComponents().elements(DbBEUseCaseResource.metaClass); while
         * (dbEnum.hasMoreElements()){ DbObject dbo = dbEnum.nextElement(); components.add(dbo); }
         * dbEnum.close();
         * 
         * dbEnum = source.getComponents().elements(DbBEUseCaseQualifier.metaClass); while
         * (dbEnum.hasMoreElements()){ DbObject dbo = dbEnum.nextElement(); components.add(dbo); }
         * dbEnum.close(); deepCopy(components, target);
         */
        toMerge.add(new MergeInfo(source, target));
    }

    private void matchExternalProcesses() throws DbException {
        ArrayList externalUseCases = new ArrayList();
        // beware: sub external processes must be kept external. They are not
        // part of this split-merge operation
        DbEnumeration dbEnum = source.getComponents().elements(DbBEUseCase.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEUseCase usecase = (DbBEUseCase) dbEnum.nextElement();
            if (!usecase.isExternal())
                continue;
            externalUseCases.add(usecase);
        }
        dbEnum.close();

        Iterator iter = externalUseCases.iterator();
        while (iter.hasNext()) {
            DbBEUseCase usecase = (DbBEUseCase) iter.next();
            DbBEUseCase matchingUsecase = getMatchingDbBEUseCase(usecase);
            if (matchingUsecase == null) {
                String usecaseInfo = usecase.getName() + " ("; // NOT
                // LOCALIZABLE
                usecaseInfo += (usecase.getSourceNumericIdentifier() == null ? kNoNumId : usecase
                        .getSourceNumericIdentifier());
                usecaseInfo += " - "
                        + (usecase.getSourceAlphanumericIdentifier() == null ? kNoAlphaId : usecase
                                .getSourceAlphanumericIdentifier()) + ")"; // NOT LOCALIZABLE
                String line = MessageFormat.format(kCouldNotResolve0, new Object[] { usecaseInfo });
                controler.println(line);
            } else {
                refUseCase.put(usecase, matchingUsecase);
            }
        }
    }

    private DbBEUseCase getMatchingDbBEUseCase(DbBEUseCase usecase) throws DbException {
        // resolve source Ids.
        String numericId = usecase.getSourceNumericIdentifier();
        String alphaId = usecase.getSourceAlphanumericIdentifier();
        DbBEUseCase matchingUsecase = null;
        if (numericId != null && numericId.length() > 0) {
            matchingUsecase = resolveDbUseCaseID(targetModel, numericId, true);
        }
        if (matchingUsecase == null && alphaId != null && alphaId.length() > 0) {
            matchingUsecase = resolveDbUseCaseID(targetModel, alphaId, false);
        }
        return matchingUsecase;
    }

    private DbBEUseCase resolveDbUseCaseID(DbBEModel model, String sPath, boolean numeric)
            throws DbException {
        ArrayList path = getPath(sPath, numeric);
        if (path.size() == 0)
            return null;
        DbObject composite = model;
        for (int i = 0; i < path.size() && composite != null; i++) {
            Object pathElement = path.get(i);
            if (pathElement == null)
                return null;
            DbEnumeration dbEnum = composite.getComponents().elements(DbBEUseCase.metaClass);
            composite = null;
            while (dbEnum.hasMoreElements()) {
                DbBEUseCase usecase = (DbBEUseCase) dbEnum.nextElement();
                Object targetvalue = usecase.get(numeric ? DbBEUseCase.fNumericIdentifier
                        : DbBEUseCase.fAlphanumericIdentifier);
                if (targetvalue == null)
                    continue;
                if (targetvalue.equals(pathElement)) {
                    composite = usecase;
                }
            }
            dbEnum.close();
        }
        return (DbBEUseCase) composite;
    }

    private ArrayList getPath(String s, boolean numeric) {
        ArrayList path = new ArrayList();
        if (s == null || s.length() == 0)
            return path;
        StringTokenizer st = new StringTokenizer(s, ".", false);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            try {
                Object pathElement = null;
                if (numeric)
                    pathElement = new Integer(token);
                else
                    pathElement = token;
                path.add(pathElement);
            } catch (Exception e) {
                path.clear();
                return path;
            }
        }
        return path;
    }

    // This will deep copy recursively the usecases, flows and diagrams of the
    // context object.
    // Note that for the direct components of context dbo, we must filter the
    // components already copied.
    // (DbBEUseCaseResource.metaClass, DbBEUseCaseQualifier.metaClass has been
    // processed previously)
    private void copyComponents() throws DbException {
        ArrayList dbos = new ArrayList();
        DbEnumeration dbEnum = source.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            DbObject dbo = (DbObject) dbEnum.nextElement();
            if (dbo instanceof DbBEUseCase) {
                if (((DbBEUseCase) dbo).isExternal() && refUseCase.get(dbo) != null)
                    continue;
                dbos.add(dbo);
                println(dbo, REPORT_INDENT, true);
            } else if (dbo instanceof DbBEFlow || dbo instanceof DbBEDiagram) {
                dbos.add(dbo);
                println(dbo, REPORT_INDENT, true);
            }
        }
        dbEnum.close();
        deepCopy(dbos, target);
    }

    private DbObject[] deepCopy(List components, DbObject targetComposite) throws DbException {
        if (components == null || components.size() == 0)
            return null;
        DbObject[] sourceDbos = new DbObject[components.size()];
        for (int i = 0; i < sourceDbos.length; i++)
            sourceDbos[i] = (DbObject) components.get(i);

        return DbObject.deepCopy(sourceDbos, targetComposite, copyCustomizer, false);
    }

    private void mergeComponents() throws DbException {
        Iterator iter = toMerge.iterator();
        while (iter.hasNext()) {
            MergeInfo info = (MergeInfo) iter.next();
            println(info.target, REPORT_INDENT, true);
            merge(info.source, info.target);
        }
    }

    // merge properties & comments depending on options
    private void merge(DbObject source, DbObject target) throws DbException {
        if (!update)
            return;
        if (source == null || target == null)
            return;
        if (source.getMetaClass() != target.getMetaClass())
            return;
        MetaField[] metafields = source.getMetaClass().getAllMetaFields();
        for (int i = 0; i < metafields.length; i++) {
            if (metafields[i] == DbObject.fComponents || metafields[i] == DbObject.fComposite
                    || metafields[i] == DbBEUseCase.fExternal
                    || metafields[i] == DbBEUseCase.fSourceAlphanumericIdentifier
                    || metafields[i] == DbBEUseCase.fSourceNumericIdentifier)
                continue;
            if (metafields[i] == DbSemanticalObject.fDescription) {
                String sDescr = (String) source.get(DbSemanticalObject.fDescription);
                String tDescr = (String) target.get(DbSemanticalObject.fDescription);
                if (tDescr != null && sDescr != null && mergeComments && !sDescr.equals(tDescr))
                    target.set(DbSemanticalObject.fDescription, tDescr + "\n" + sDescr);
                else
                    target.set(DbSemanticalObject.fDescription, sDescr);
                continue;
            }
            if (metafields[i] == DbSMSSemanticalObject.fUmlStereotype) {
                DbObject sourcestereotype = (DbObject) source
                        .get(DbSMSSemanticalObject.fUmlStereotype);
                DbObject targetstereotype = sourcestereotype == null ? null
                        : ((DbSMSProject) target.getProject()).getUmlExtensibility()
                                .findComponentByName(DbSMSStereotype.metaClass,
                                        sourcestereotype.getName());
                target.set(DbSMSSemanticalObject.fUmlStereotype, targetstereotype);
                continue;
            }
            if (metafields[i] instanceof MetaRelationship) {
                continue;
            }
            target.set(metafields[i], source.get(metafields[i]));
        }

        if (mergeQualifiers) {
            // update existing and add missing
            ArrayList toCopy = new ArrayList();
            if (source instanceof DbBEActor) {
                DbEnumeration dbEnum = source.getComponents()
                        .elements(DbBEActorQualifier.metaClass);
                while (dbEnum.hasMoreElements()) {
                    DbBEActorQualifier sourceDbo = (DbBEActorQualifier) dbEnum.nextElement();
                    DbBEQualifier sourceQualifier = sourceDbo.getQualifier();
                    if (sourceQualifier == null)
                        continue;
                    DbEnumeration enum2 = target.getComponents().elements(
                            DbBEActorQualifier.metaClass);
                    DbBEActorQualifier targetDbo = null;
                    while (enum2.hasMoreElements()) {
                        targetDbo = (DbBEActorQualifier) enum2.nextElement();
                        DbBEQualifier targetQualifier = targetDbo.getQualifier();
                        if (targetQualifier != null && sourceQualifier.getName() != null
                                && sourceQualifier.getName().equals(targetQualifier.getName()))
                            break;
                        targetDbo = null;
                    }
                    enum2.close();
                    if (targetDbo != null) {
                        merge(sourceDbo, targetDbo);
                    } else {
                        toCopy.add(sourceDbo);
                    }
                }
                dbEnum.close();
            } else if (source instanceof DbBEUseCase) {
                DbEnumeration dbEnum = source.getComponents().elements(
                        DbBEUseCaseQualifier.metaClass);
                while (dbEnum.hasMoreElements()) {
                    DbBEUseCaseQualifier sourceDbo = (DbBEUseCaseQualifier) dbEnum.nextElement();
                    DbBEQualifier sourceQualifier = sourceDbo.getQualifier();
                    if (sourceQualifier == null)
                        continue;
                    DbEnumeration enum2 = target.getComponents().elements(
                            DbBEUseCaseQualifier.metaClass);
                    DbBEUseCaseQualifier targetDbo = null;
                    while (enum2.hasMoreElements()) {
                        targetDbo = (DbBEUseCaseQualifier) enum2.nextElement();
                        DbBEQualifier targetQualifier = targetDbo.getQualifier();
                        if (targetQualifier != null && sourceQualifier.getName() != null
                                && sourceQualifier.getName().equals(targetQualifier.getName()))
                            break;
                        targetDbo = null;
                    }
                    enum2.close();
                    if (targetDbo != null) {
                        merge(sourceDbo, targetDbo);
                    } else {
                        toCopy.add(sourceDbo);
                    }
                }
                dbEnum.close();
            } else if (source instanceof DbBEStore) {
                DbEnumeration dbEnum = source.getComponents()
                        .elements(DbBEStoreQualifier.metaClass);
                while (dbEnum.hasMoreElements()) {
                    DbBEStoreQualifier sourceDbo = (DbBEStoreQualifier) dbEnum.nextElement();
                    DbBEQualifier sourceQualifier = sourceDbo.getQualifier();
                    if (sourceQualifier == null)
                        continue;
                    DbEnumeration enum2 = target.getComponents().elements(
                            DbBEStoreQualifier.metaClass);
                    DbBEStoreQualifier targetDbo = null;
                    while (enum2.hasMoreElements()) {
                        targetDbo = (DbBEStoreQualifier) enum2.nextElement();
                        DbBEQualifier targetQualifier = targetDbo.getQualifier();
                        if (targetQualifier != null && sourceQualifier.getName() != null
                                && sourceQualifier.getName().equals(targetQualifier.getName()))
                            break;
                        targetDbo = null;
                    }
                    enum2.close();
                    if (targetDbo != null) {
                        merge(sourceDbo, targetDbo);
                    } else {
                        toCopy.add(sourceDbo);
                    }
                }
                dbEnum.close();
            }
            deepCopy(toCopy, target);
        }

        if (mergeResources) {
            // update existing and add missing
            ArrayList toCopy = new ArrayList();
            if (source instanceof DbBEUseCase) {
                DbEnumeration dbEnum = source.getComponents().elements(
                        DbBEUseCaseResource.metaClass);
                while (dbEnum.hasMoreElements()) {
                    DbBEUseCaseResource sourceDbo = (DbBEUseCaseResource) dbEnum.nextElement();
                    DbBEResource sourceResource = sourceDbo.getResource();
                    if (sourceResource == null)
                        continue;
                    DbEnumeration enum2 = target.getComponents().elements(
                            DbBEUseCaseResource.metaClass);
                    DbBEUseCaseResource targetDbo = null;
                    while (enum2.hasMoreElements()) {
                        targetDbo = (DbBEUseCaseResource) enum2.nextElement();
                        DbBEResource targetResource = targetDbo.getResource();
                        if (targetResource != null && sourceResource.getName() != null
                                && sourceResource.getName().equals(targetResource.getName()))
                            break;
                        targetDbo = null;
                    }
                    enum2.close();
                    if (targetDbo != null) {
                        merge(sourceDbo, targetDbo);
                    } else {
                        toCopy.add(sourceDbo);
                    }
                }
                dbEnum.close();
            }
            deepCopy(toCopy, target);
        }
    }

}
