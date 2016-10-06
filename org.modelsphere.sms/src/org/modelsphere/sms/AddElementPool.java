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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.Icon;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.screen.LookupDialog;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.srtool.AddElement;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.explorer.ExplorerView;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEContextCell;
import org.modelsphere.sms.be.db.DbBEContextGo;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEQualifier;
import org.modelsphere.sms.be.db.DbBEResource;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.DbBEUseCaseGo;
import org.modelsphere.sms.be.db.util.BEUtility;
import org.modelsphere.sms.be.db.util.CellUtility;
import org.modelsphere.sms.db.DbSMSBuiltInTypePackage;
import org.modelsphere.sms.db.DbSMSClassifier;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSLink;
import org.modelsphere.sms.db.DbSMSLinkModel;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.db.DbSMSUmlConstraint;
import org.modelsphere.sms.db.DbSMSUmlExtensibility;
import org.modelsphere.sms.db.DbSMSUserDefinedPackage;
import org.modelsphere.sms.db.util.DbInitialization;
import org.modelsphere.sms.db.util.TerminologyInitializer;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOOAbsPackage;
import org.modelsphere.sms.oo.db.DbOOClassModel;
import org.modelsphere.sms.oo.db.DbOOConstructor;
import org.modelsphere.sms.oo.db.DbOODataMember;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.db.DbOOInitBlock;
import org.modelsphere.sms.oo.db.DbOOMethod;
import org.modelsphere.sms.oo.db.DbOOParameter;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVCompilationUnit;
import org.modelsphere.sms.oo.java.db.DbJVConstructor;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.oo.java.db.srtypes.JVClassCategory;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORBuiltInType;
import org.modelsphere.sms.or.db.DbORCheck;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORConstraint;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORField;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.db.DbORParameter;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORProcedure;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTrigger;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.db.DbORUserNode;
import org.modelsphere.sms.or.db.DbORView;
import org.modelsphere.sms.or.db.util.AnyORObject;
import org.modelsphere.sms.or.ibm.db.DbIBMBufferPool;
import org.modelsphere.sms.or.ibm.db.DbIBMContainerClause;
import org.modelsphere.sms.or.ibm.db.DbIBMContainerItem;
import org.modelsphere.sms.or.ibm.db.DbIBMDataModel;
import org.modelsphere.sms.or.ibm.db.DbIBMDatabase;
import org.modelsphere.sms.or.ibm.db.DbIBMDbPartitionGroup;
import org.modelsphere.sms.or.ibm.db.DbIBMExceptClause;
import org.modelsphere.sms.or.ibm.db.DbIBMSequence;
import org.modelsphere.sms.or.ibm.db.DbIBMTablespace;
import org.modelsphere.sms.or.informix.db.DbINFDatabase;
import org.modelsphere.sms.or.informix.db.DbINFDbspace;
import org.modelsphere.sms.or.informix.db.DbINFSbspace;
import org.modelsphere.sms.or.oracle.db.DbORADataFile;
import org.modelsphere.sms.or.oracle.db.DbORADataModel;
import org.modelsphere.sms.or.oracle.db.DbORADatabase;
import org.modelsphere.sms.or.oracle.db.DbORAIndex;
import org.modelsphere.sms.or.oracle.db.DbORALobStorage;
import org.modelsphere.sms.or.oracle.db.DbORANestedTableStorage;
import org.modelsphere.sms.or.oracle.db.DbORAPackage;
import org.modelsphere.sms.or.oracle.db.DbORAPartition;
import org.modelsphere.sms.or.oracle.db.DbORAProgramUnitLibrary;
import org.modelsphere.sms.or.oracle.db.DbORARedoLogFile;
import org.modelsphere.sms.or.oracle.db.DbORARedoLogGroup;
import org.modelsphere.sms.or.oracle.db.DbORARollbackSegment;
import org.modelsphere.sms.or.oracle.db.DbORASequence;
import org.modelsphere.sms.or.oracle.db.DbORASubPartition;
import org.modelsphere.sms.or.oracle.db.DbORATable;
import org.modelsphere.sms.or.oracle.db.DbORATablespace;
import org.modelsphere.sms.plugins.TargetSystem;
import org.modelsphere.sms.plugins.TargetSystemManager;
import org.modelsphere.sms.preference.DiagramAutomaticCreationOptionGroup;

/**
 * For each kind of model elements, defines the possible components to allow the user to add
 * components in the explorer.
 * <p>
 * Order is important (will be displayed according to the order in the returned array) Include all
 * AddElement objects that will participate in the AddAction. For delegation of the creation to the
 * active SMSToolkit, create an AbstractAddElement instead. The SMSToolkit may take in charge the
 * object creation based on an abstract class. For example, the toolkit for Oracle data model will
 * create a specific Oracle table object (DbORATable) based on the super metaclass DbORTable. Thus
 * avoiding specifying an AddElement for each target system. Note: This array is constructed only
 * once on the first time the action performed is called on AddAction
 * </p>
 */
final class AddElementPool {
    static AddElementPool singleton;
    private AddElement[] pool = null;
    private static final String SUPER_0 = LocaleMgr.action.getString("super0");
    public static final Icon dbbeumlmodel = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbeumlmodel.gif");

    private static final String UML_MODEL = LocaleMgr.action.getString("UML_MODEL");
    private static final String JAVA_CLASS_MODEL = LocaleMgr.action.getString("JAVA_CLASS_MODEL");
    private static final String kItemType = LocaleMgr.action.getString("ItemType");

    private static Terminology oldTerminology = null;

    private static class AbstractAddElement extends AddElement {
        public AbstractAddElement(MetaClass componentMetaclass, MetaClass[] compositeMetaclasses,
                String transName) {
            super(componentMetaclass, compositeMetaclasses, transName, null, true, false);
        }

        public AbstractAddElement(MetaClass componentMetaclass, MetaClass[] compositeMetaclasses) {
            super(componentMetaclass, compositeMetaclasses, null, null, true, false);
        }

        public AbstractAddElement(MetaClass componentMetaclass, MetaClass compositeMetaclass) {
            super(componentMetaclass, compositeMetaclass);
        }

        public AbstractAddElement(MetaClass componentMetaclass, MetaClass[] compositeMetaclasses,
                boolean targetVisibleInName) {
            super(componentMetaclass, compositeMetaclasses, null, null, true, targetVisibleInName);
        }

        public AbstractAddElement(MetaClass componentMetaclass, MetaClass[] compositeMetaclasses,
                String name, String transName, Icon icon, Object[] parameters) {
            super(componentMetaclass, compositeMetaclasses, name, transName, icon, parameters);
        }

        public DbObject createElement(DbObject composite) throws DbException {
            preCreateElement(composite);
            DbObject component = SMSToolkit.getToolkit(composite).createDbObject(
                    componentMetaclass, composite, parameters);
            postCreateElement(component);
            return component;
        }
    }

    private abstract static class AddAbstractPackageElement extends AddElement {
        private List<Serializable> targets;
        private static final String kConceptual = LocaleMgr.action.getString("conceptual");
        private static final String kOthers = LocaleMgr.action.getString("Others_");

        public AddAbstractPackageElement(MetaClass componentMetaclass,
                MetaClass[] compositeMetaclasses, String transName) {
            super(componentMetaclass, compositeMetaclasses, transName, null, true, false);
        }

        public AddAbstractPackageElement(MetaClass componentMetaclass,
                MetaClass[] compositeMetaclasses) {
            super(componentMetaclass, compositeMetaclasses, null, null, true, false);
        }

        public AddAbstractPackageElement(MetaClass componentMetaclass, MetaClass compositeMetaclass) {
            super(componentMetaclass, compositeMetaclass);
        }

        public DbObject createElement(DbObject composite) throws DbException {
            Object selected = targets.get(choiceValuesSelectedIndex);
            boolean isConceptualSelection = false;

            if (selected instanceof String) {
                if (selected.toString() == kConceptual) {
                    DbProject project = ApplicationContext.getFocusManager().getCurrentProject();
                    selected = TargetSystem.getSpecificTargetSystem(project,
                            TargetSystem.SGBD_LOGICAL);
                    isConceptualSelection = true;
                } else {
                    DefaultMainFrame mf = ApplicationContext.getDefaultMainFrame();
                    ArrayList<DbSMSTargetSystem> newtarget = TargetSystemManager.getSingleton()
                            .addTargetSystem(mf, composite, false);
                    if (newtarget == null || newtarget.size() != 1 || newtarget.get(0) == null) {
                        return null;
                    }
                    selected = newtarget.get(0);
                }

            }
            DbSMSTargetSystem target = (DbSMSTargetSystem) selected;
            preCreateElement(composite);
            DbObject component = createTargetElement(composite, target, isConceptualSelection);
            postCreateElement(component);
            return component;
        }

        protected abstract DbObject createTargetElement(DbObject composite,
                DbSMSTargetSystem target, boolean withConceptualMode) throws DbException;

        public void update() throws DbException {
            DbProject project = ApplicationContext.getFocusManager().getCurrentProject();
            if (project == null) { // multi project not supported
                setEnabled(false);
                return;
            }
            DbObject[] selobjs = ApplicationContext.getFocusManager()
                    .getSelectedSemanticalObjects();
            if (selobjs == null || selobjs.length != 1) { // multi selection not supported
                setEnabled(false);
                return;
            }
            if (selobjs[0] instanceof DbSMSProject) {
                targets = TargetSystem.getORTargetSystem(project);

                if (this.componentMetaclass.getGUIName().equals(
                        DbORDataModel.metaClass.getGUIName()))
                    targets.add(kConceptual);
                targets.add(null);
                targets.add(kOthers);
            } else if (selobjs[0] instanceof DbORDataModel) {
                if (((DbORDataModel) selobjs[0]).getLogicalMode().intValue() == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                    if (targets == null) {
                        targets = new ArrayList<Serializable>();
                    } else
                        targets.clear();
                    targets.add(kConceptual);
                } else {
                    targets = TargetSystem.getORTargetSystem(project);
                    targets.add(null);
                    targets.add(kOthers);
                }
            } else {
                targets = TargetSystem.getORTargetSystem(project);
                targets.add(null);
                targets.add(kOthers);
            }

            if (targets == null)
                return;

            Object[] items = new Object[targets.size()];
            for (int i = 0; i < targets.size(); i++) {
                Object element = targets.get(i);
                if (!(element instanceof DbSMSTargetSystem)) {
                    items[i] = element;
                    continue;
                }
                DbSMSTargetSystem target = (DbSMSTargetSystem) element;
                items[i] = target.getName() + " " + target.getVersion();
            }
            setChoiceValues(items);
            setEnabled(targets.size() != 0);
        }
    }

    private AddElementPool(Terminology terminology) {
        List<AddElement> elements = new ArrayList<AddElement>();

        elements.add(new AddElement(DbSMSStereotype.metaClass, DbSMSUmlExtensibility.metaClass));
        elements.add(new AddElement(DbSMSUmlConstraint.metaClass, DbSMSUmlExtensibility.metaClass));
        elements.add(new AddElement(DbSMSLink.metaClass, DbSMSLinkModel.metaClass));
        elements.add(new AddElement(DbORBuiltInType.metaClass,
                new MetaClass[] { DbSMSBuiltInTypePackage.metaClass }, kItemType, kItemType,
                GraphicUtil.loadIcon(DbORBuiltInType.class, "resources/dborusertype.gif"), null) {
            public void update() throws DbException {
                DbObject[] dbos = ApplicationContext.getFocusManager()
                        .getSelectedSemanticalObjects();
                for (int i = 0; i < dbos.length; i++) {
                    if (((DbSMSBuiltInTypePackage) dbos[i]).isBuiltIn()) {
                        setEnabled(false);
                        return;
                    }
                }
                super.update();
            }
        });

        // OO - Java
        //  Check before adding the elements
        //(SMSFilter.JAVA)
        elements.add(new AbstractAddElement(DbOODataMember.metaClass, DbJVClass.metaClass));
        elements.add(new AbstractAddElement(DbOOParameter.metaClass, new MetaClass[] {
                DbJVMethod.metaClass, DbJVConstructor.metaClass }));
        elements.add(new AbstractAddElement(DbOOMethod.metaClass, DbJVClass.metaClass));
        elements.add(new AbstractAddElement(DbOOInitBlock.metaClass, DbJVClass.metaClass));
        elements.add(new AbstractAddElement(DbOOConstructor.metaClass, DbJVClass.metaClass));
        elements.add(new AddElement(DbJVPackage.metaClass, new MetaClass[] { DbJVPackage.metaClass,
                DbJVClassModel.metaClass }) {
            protected void postCreateElement(DbObject ooPackage) throws DbException {
                if (ooPackage != null) {
                    // Create the diagram according to the user option
                    if (DiagramAutomaticCreationOptionGroup.isCreateJavaDiagram()) {
                        new DbOODiagram(ooPackage);
                    }
                }
            }
        });
        elements.add(new AddElement(DbJVClass.metaClass, new MetaClass[] { DbJVPackage.metaClass,
                DbJVClassModel.metaClass, DbJVClass.metaClass }, LocaleMgr.action
                .getString("Class"), LocaleMgr.action.getString("addClass"), GraphicUtil.loadIcon(
                DbJVClass.class, "resources/dbjvclass.gif"), new Object[] { JVClassCategory
                .getInstance(JVClassCategory.CLASS) }));
        elements.add(new AddElement(DbJVClass.metaClass, new MetaClass[] { DbJVPackage.metaClass,
                DbJVClassModel.metaClass, DbJVClass.metaClass }, LocaleMgr.action
                .getString("Interface"), LocaleMgr.action.getString("addInterface"), GraphicUtil
                .loadIcon(DbJVClass.class, "resources/dbjvinterface.gif"),
                new Object[] { JVClassCategory.getInstance(JVClassCategory.INTERFACE) }));
        elements.add(new AddElement(DbJVClass.metaClass, new MetaClass[] { DbJVPackage.metaClass,
                DbJVClassModel.metaClass, DbJVClass.metaClass }, LocaleMgr.action
                .getString("Exception"), LocaleMgr.action.getString("addException"), GraphicUtil
                .loadIcon(DbJVClass.class, "resources/dbjvexception.gif"),
                new Object[] { JVClassCategory.getInstance(JVClassCategory.EXCEPTION) }));
        elements.add(new AddElement(DbJVCompilationUnit.metaClass, new MetaClass[] {
                DbJVClassModel.metaClass, DbJVClass.metaClass, DbJVPackage.metaClass }) {
            public void update() throws DbException {
                Object focusObject = ApplicationContext.getFocusManager().getFocusObject();
                if (!(focusObject instanceof ExplorerView)) {
                    setVisible(false);
                    return;
                }
                setVisible(true);

                DbObject[] objects = ApplicationContext.getFocusManager()
                        .getSelectedSemanticalObjects();
                boolean state = false;
                for (int i = 0; i < objects.length; i++) {
                    if (objects[i] instanceof DbJVClass) {
                        DbJVClass adt = (DbJVClass) objects[i];
                        if (!isAdtValid(adt, getCompilName(adt)))
                            continue;
                    }
                    state = true;
                    break;
                }
                setEnabled(state);
            }

            public DbObject createElement(DbObject composite) throws DbException {
                DbJVCompilationUnit compilUnit = null;
                if (composite instanceof DbOOAbsPackage) {
                    compilUnit = new DbJVCompilationUnit(composite);
                } else {
                    DbJVClass adt = (DbJVClass) composite;
                    String compilName = getCompilName(adt);
                    if (!isAdtValid(adt, compilName))
                        return null;
                    compilUnit = new DbJVCompilationUnit(adt.getComposite());
                    compilUnit.setName(compilName);
                    adt.setCompilationUnit(compilUnit);
                }
                return compilUnit;
            }

            // If adt selected, there must not be a compilation unit of the same name in the package.
            private boolean isAdtValid(DbJVClass adt, String compilName) throws DbException {
                if (adt.getCompilationUnit() != null)
                    return false;
                DbObject parent = adt.getComposite();
                if (!(parent instanceof DbOOAbsPackage))
                    return false;
                return (parent.findComponentByName(DbJVCompilationUnit.metaClass, compilName) == null);
            }

            private String getCompilName(DbJVClass adt) throws DbException {
                return (adt.getName() + ".java"); //NOT LOCALIZABLE
            }
        });
        elements.add(new AddElement(DbOODiagram.metaClass, new MetaClass[] { DbJVPackage.metaClass,
                DbJVClassModel.metaClass }));
        //elements.add(new AddElement(DbJVClassModel.metaClass, new MetaClass[]{DbSMSProject.metaClass, DbSMSUserDefinedPackage.metaClass}));
        //end (SMSFilter.JAVA)

        // Relational  
        //(SMSFilter.RELATIONAL))
        if (terminology == null) {
            // any target (abstract) concept

            elements.add(new AddAbstractPackageElement(DbORDataModel.metaClass, new MetaClass[] {
                    DbORDataModel.metaClass, DbSMSProject.metaClass,
                    DbSMSUserDefinedPackage.metaClass }) {
                protected DbObject createTargetElement(DbObject composite,
                        DbSMSTargetSystem target, boolean withConceptualMode) throws DbException {
                    DbORDataModel model = null;
                    if (withConceptualMode)
                        model = AnyORObject.createConceptualDataModel(composite, target);
                    else
                        model = AnyORObject.createDataModel(composite, target);

                    // Create the diagram according to the user option
                    if (DiagramAutomaticCreationOptionGroup.isCreateDataModelDiagram()) {
                        new DbORDiagram(model);
                    }
                    return model;
                }
            });

            elements.add(new AddAbstractPackageElement(DbOROperationLibrary.metaClass,
                    new MetaClass[] { DbSMSProject.metaClass, DbSMSUserDefinedPackage.metaClass }) {
                protected DbObject createTargetElement(DbObject composite,
                        DbSMSTargetSystem target, boolean withConceptualMode) throws DbException {
                    DbOROperationLibrary library = AnyORObject.createOperationLibrary(composite,
                            target);
                    return library;
                }
            });
            elements.add(new AddAbstractPackageElement(DbORDatabase.metaClass, new MetaClass[] {
                    DbSMSProject.metaClass, DbSMSUserDefinedPackage.metaClass }) {
                protected DbObject createTargetElement(DbObject composite,
                        DbSMSTargetSystem target, boolean withConceptualMode) throws DbException {
                    DbORDatabase database = AnyORObject.createDatabase(composite, target);
                    return database;
                }
            });

            elements.add(new AbstractAddElement(DbORTable.metaClass,
                    new MetaClass[] { DbORDataModel.metaClass }));
            elements.add(new AbstractAddElement(DbORView.metaClass,
                    new MetaClass[] { DbORDataModel.metaClass }));
            elements.add(new AddElement(DbORDiagram.metaClass,
                    new MetaClass[] { DbORDataModel.metaClass }, GraphicUtil.loadIcon(
                            DbORDiagram.class, "resources/dbordatadiagram.gif")));

            elements.add(new AddElement(DbORCommonItem.metaClass,
                    new MetaClass[] { DbORCommonItemModel.metaClass }));
            elements.add(new AddElement(DbORDomain.metaClass,
                    new MetaClass[] { DbORDomainModel.metaClass }));
            elements.add(new AddElement(DbORField.metaClass,
                    new MetaClass[] { DbORDomain.metaClass }));
            elements.add(new AbstractAddElement(DbORColumn.metaClass,
                    new MetaClass[] { DbORAbsTable.metaClass }));

            elements.add(new AbstractAddElement(DbORCheck.metaClass, new MetaClass[] {
                    DbORTable.metaClass, DbORView.metaClass }));

            elements.add(new AbstractAddElement(DbORTrigger.metaClass, new MetaClass[] {
                    DbORTable.metaClass, DbORView.metaClass }));
            elements.add(new AbstractAddElement(DbORProcedure.metaClass,
                    new MetaClass[] { DbOROperationLibrary.metaClass }));
            elements.add(new AbstractAddElement(DbORParameter.metaClass,
                    new MetaClass[] { DbORProcedure.metaClass }));

            elements.add(new AbstractAddElement(DbORPrimaryUnique.metaClass, new MetaClass[] {
                    DbORTable.metaClass, DbORView.metaClass }, LocaleMgr.action
                    .getString("PrimaryKey"), LocaleMgr.action.getString("addPrimaryKey"),
                    GraphicUtil.loadIcon(DbORPrimaryUnique.class, "resources/dborprimary.gif"),
                    null) {
                protected void postCreateElement(DbObject dbo) throws DbException {
                    if (dbo != null) {
                        ((DbORPrimaryUnique) dbo).setPrimary(Boolean.TRUE);
                        //((DbORPrimaryUnique)dbo).setName(LocaleMgr.action.getString("PrimaryUniqueKey"));// primaire vrai mais peut etre aussi <1>, <2>... en meme temps
                    }
                }
            });
            elements.add(new AbstractAddElement(DbORPrimaryUnique.metaClass, new MetaClass[] {
                    DbORTable.metaClass, DbORView.metaClass }, LocaleMgr.action
                    .getString("UniqueKey"), LocaleMgr.action.getString("addUniqueKey"),
                    GraphicUtil.loadIcon(DbORPrimaryUnique.class, "resources/dborunique.gif"), null) {
                protected void postCreateElement(DbObject dbo) throws DbException {
                    if (dbo != null) {
                        ((DbORPrimaryUnique) dbo).setPrimary(Boolean.FALSE);
                        ((DbORPrimaryUnique) dbo).setName(LocaleMgr.action.getString("UniqueKey"));
                    }
                }
            });

            elements.add(new AddElement(DbORIndex.metaClass, new MetaClass[] { DbORTable.metaClass,
                    DbORPrimaryUnique.metaClass, DbORForeign.metaClass, DbORView.metaClass }) {
                public void update() throws DbException {
                    DbObject[] semObjs = ApplicationContext.getFocusManager()
                            .getSelectedSemanticalObjects();
                    int nb = semObjs.length;
                    for (int i = 0; i < semObjs.length; i++) {
                        if (!(semObjs[i] instanceof DbORConstraint))
                            continue;
                        if (semObjs[i]
                                .get(semObjs[i] instanceof DbORPrimaryUnique ? DbORPrimaryUnique.fIndex
                                        : DbORForeign.fIndex) != null)
                            nb--;
                    }
                    setEnabled(nb == semObjs.length);
                }

                public DbObject createElement(DbObject composite) throws DbException {
                    MetaClass metaClass = AnyORObject.getTargetMetaClasses(composite)[AnyORObject.I_INDEX];
                    DbORIndex index = null;
                    if (composite instanceof DbORAbsTable) {
                        index = (DbORIndex) composite.createComponent(metaClass);
                    } else if (composite
                            .get(composite instanceof DbORPrimaryUnique ? DbORPrimaryUnique.fIndex
                                    : DbORForeign.fIndex) == null) {
                        DbORConstraint constraint = (DbORConstraint) composite;
                        index = (DbORIndex) constraint.getComposite().createComponent(metaClass);
                        index.setConstraint(constraint); // a trigger will initialize the index compostion from the constraint composition
                        if (constraint instanceof DbORPrimaryUnique)
                            index.setUnique(Boolean.TRUE);
                        index.setName(constraint.getName());
                        index.setPhysicalName(constraint.getPhysicalName());
                    }
                    return index;
                }
            });
            elements.add(new AddElement(DbORUser.metaClass,
                    new MetaClass[] { DbORUserNode.metaClass }));

            // Informix
            elements.add(new AddElement(DbINFDbspace.metaClass,
                    new MetaClass[] { DbINFDatabase.metaClass }));
            elements.add(new AddElement(DbINFSbspace.metaClass,
                    new MetaClass[] { DbINFDatabase.metaClass }));

            // Oracle
            elements.add(new AddElement(DbORADataFile.metaClass,
                    new MetaClass[] { DbORATablespace.metaClass }));
            elements.add(new AddElement(DbORARedoLogGroup.metaClass,
                    new MetaClass[] { DbORADatabase.metaClass }));
            elements.add(new AddElement(DbORARedoLogFile.metaClass,
                    new MetaClass[] { DbORARedoLogGroup.metaClass }));
            elements.add(new AddElement(DbORARollbackSegment.metaClass,
                    new MetaClass[] { DbORADatabase.metaClass }));
            elements.add(new AddElement(DbORATablespace.metaClass,
                    new MetaClass[] { DbORADatabase.metaClass }));
            elements.add(new AddElement(DbORANestedTableStorage.metaClass,
                    new MetaClass[] { DbORATable.metaClass }));
            elements.add(new AddElement(DbORASequence.metaClass,
                    new MetaClass[] { DbORADataModel.metaClass }));
            elements.add(new AddElement(DbORAPartition.metaClass, new MetaClass[] {
                    DbORATable.metaClass, DbORAIndex.metaClass }));
            elements.add(new AddElement(DbORASubPartition.metaClass,
                    new MetaClass[] { DbORAPartition.metaClass }));
            elements.add(new AddElement(DbORALobStorage.metaClass,
                    new MetaClass[] { DbORATable.metaClass }));
            elements.add(new AddElement(DbORAPackage.metaClass,
                    new MetaClass[] { DbORAProgramUnitLibrary.metaClass }));

            // DB2-UDB
            elements.add(new AddElement(DbIBMSequence.metaClass,
                    new MetaClass[] { DbIBMDataModel.metaClass }));
            elements.add(new AddElement(DbIBMDbPartitionGroup.metaClass,
                    new MetaClass[] { DbIBMDatabase.metaClass }));
            elements.add(new AddElement(DbIBMBufferPool.metaClass,
                    new MetaClass[] { DbIBMDatabase.metaClass }));
            elements.add(new AddElement(DbIBMExceptClause.metaClass,
                    new MetaClass[] { DbIBMBufferPool.metaClass }));
            elements.add(new AddElement(DbIBMTablespace.metaClass,
                    new MetaClass[] { DbIBMDatabase.metaClass }));
            elements.add(new AddElement(DbIBMContainerClause.metaClass,
                    new MetaClass[] { DbIBMTablespace.metaClass }));
            elements.add(new AddElement(DbIBMContainerItem.metaClass,
                    new MetaClass[] { DbIBMContainerClause.metaClass }));
        } else { //a terminology was specified, set the add elements to reflect the correct names and concepts

            DbORDataModel dataModel = null;
            int mode = DbORDataModel.LOGICAL_MODE_OBJECT_RELATIONAL;
            try {
                DbObject[] dbObject = ApplicationContext.getFocusManager()
                        .getSelectedSemanticalObjects();
                if (dbObject[0] instanceof DbORDataModel)
                    dataModel = (DbORDataModel) dbObject[0];
                else
                    dataModel = (DbORDataModel) dbObject[0]
                            .getCompositeOfType(DbORDataModel.metaClass);

                mode = dataModel.getOperationalMode();
            } catch (Exception e) {/* do nothing */
            }

            AddElement packageEle = new AddAbstractPackageElement(DbORDataModel.metaClass,
                    new MetaClass[] { DbORDataModel.metaClass, DbSMSProject.metaClass,
                            DbSMSUserDefinedPackage.metaClass }) {
                protected DbObject createTargetElement(DbObject composite,
                        DbSMSTargetSystem target, boolean withConceptualMode) throws DbException {
                    DbORDataModel model = null;
                    if (withConceptualMode)
                        model = AnyORObject.createConceptualDataModel(composite, target);
                    else
                        model = AnyORObject.createDataModel(composite, target);

                    // Create the diagram according to the user option
                    if (DiagramAutomaticCreationOptionGroup.isCreateDataModelDiagram()) {
                        new DbORDiagram(model);
                    }
                    return model;
                }
            };

            if (mode != DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                elements.add(new AddAbstractPackageElement(
                        DbOROperationLibrary.metaClass,
                        new MetaClass[] { DbSMSProject.metaClass, DbSMSUserDefinedPackage.metaClass }) {
                    protected DbObject createTargetElement(DbObject composite,
                            DbSMSTargetSystem target, boolean withConceptualMode)
                            throws DbException {
                        DbOROperationLibrary library = AnyORObject.createOperationLibrary(
                                composite, target);
                        return library;
                    }
                });
            } else {
                TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
                DbObject[] dbObjects = ApplicationContext.getFocusManager()
                        .getSelectedSemanticalObjects();

                if (dbObjects.length > 0)
                    if (false == (dbObjects[0] instanceof DbProject))
                        packageEle.setIcon(terminologyUtil.getConceptualModelIcon());
            }
            elements.add(packageEle);
            elements.add(new AddAbstractPackageElement(DbORDatabase.metaClass, new MetaClass[] {
                    DbSMSProject.metaClass, DbSMSUserDefinedPackage.metaClass }) {
                protected DbObject createTargetElement(DbObject composite,
                        DbSMSTargetSystem target, boolean withConceptualMode) throws DbException {
                    DbORDatabase database = AnyORObject.createDatabase(composite, target);
                    return database;
                }
            });

            AbstractAddElement abselement = new AbstractAddElement(DbORTable.metaClass,
                    new MetaClass[] { DbORDataModel.metaClass });
            abselement.setName(terminology.getTerm(DbORTable.metaClass));
            elements.add(abselement);

            if (mode != DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                abselement = new AbstractAddElement(DbORView.metaClass,
                        new MetaClass[] { DbORDataModel.metaClass });
                abselement.setName(terminology.getTerm(DbORView.metaClass));
                elements.add(abselement);
            }

            elements.add(new AddElement(DbORDiagram.metaClass,
                    new MetaClass[] { DbORDataModel.metaClass }, GraphicUtil.loadIcon(
                            DbORDiagram.class, "resources/dbordatadiagram.gif")));
            elements.add(new AddElement(DbORDiagram.metaClass,
                    new MetaClass[] { DbORCommonItemModel.metaClass }, GraphicUtil.loadIcon(
                            DbORDiagram.class, "resources/dborcommonitemdiagram.gif")));
            elements.add(new AddElement(DbORDiagram.metaClass,
                    new MetaClass[] { DbORDomainModel.metaClass }, GraphicUtil.loadIcon(
                            DbORDiagram.class, "resources/dbordomaindiagram.gif")));
            elements.add(new AddAbstractPackageElement(DbORDomainModel.metaClass, new MetaClass[] {
                    DbSMSProject.metaClass, DbSMSUserDefinedPackage.metaClass }) {
                protected DbObject createTargetElement(DbObject composite,
                        DbSMSTargetSystem target, boolean withConceptualMode) throws DbException {
                    DbORDomainModel model;
                    model = new DbORDomainModel(composite, target);

                    // Create the diagram according to the user option
                    if (DiagramAutomaticCreationOptionGroup.isCreateDomainModelDiagram()) {
                        new DbORDiagram(model);
                    }
                    return model;
                }
            });
            /*
             * elements.add(new AddElement(DbORCommonItemModel.metaClass, new
             * MetaClass[]{DbSMSProject.metaClass, DbSMSUserDefinedPackage.metaClass}){ protected
             * void postCreateElement(DbObject commonItemModel) throws DbException{ if
             * (commonItemModel != null){ // Create the diagram according to the user option if
             * (DiagramAutomaticCreationOptionGroup.isCreateCommonItemModelDiagram()){ new
             * DbORDiagram(commonItemModel); } } } });
             */
            elements.add(new AddElement(DbORCommonItem.metaClass,
                    new MetaClass[] { DbORCommonItemModel.metaClass }));
            elements.add(new AddElement(DbORDomain.metaClass,
                    new MetaClass[] { DbORDomainModel.metaClass }));
            elements.add(new AddElement(DbORField.metaClass,
                    new MetaClass[] { DbORDomain.metaClass }));

            abselement = new AbstractAddElement(DbORColumn.metaClass,
                    new MetaClass[] { DbORAbsTable.metaClass });
            abselement.setName(terminology.getTerm(DbORColumn.metaClass));
            elements.add(abselement);

            abselement = new AbstractAddElement(DbORCheck.metaClass, new MetaClass[] {
                    DbORTable.metaClass, DbORView.metaClass });
            abselement.setName(terminology.getTerm(DbORCheck.metaClass));
            elements.add(abselement);

            if (mode != DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                elements.add(new AbstractAddElement(DbORTrigger.metaClass, new MetaClass[] {
                        DbORTable.metaClass, DbORView.metaClass }));
                elements.add(new AbstractAddElement(DbORProcedure.metaClass,
                        new MetaClass[] { DbOROperationLibrary.metaClass }));
                elements.add(new AbstractAddElement(DbORParameter.metaClass,
                        new MetaClass[] { DbORProcedure.metaClass }));
            }

            boolean bHasAssociations = false;
            DbObject[] objects = ApplicationContext.getFocusManager()
                    .getSelectedSemanticalObjects();
            try {
                for (int idx = 0; idx < objects.length; idx++) {
                    if (objects[idx] instanceof DbORTable) {
                        objects[idx].getDb().beginReadTrans();
                        DbORTable table = (DbORTable) objects[idx];
                        if (table.isIsAssociation() == true) {
                            bHasAssociations = true;
                            objects[idx].getDb().commitTrans();
                            break;
                        }
                        objects[idx].getDb().commitTrans();
                    }
                }
            } catch (DbException dbe) {
                throw new RuntimeException("Could not determine association type.");
            }

            if (mode != DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP || bHasAssociations == false) {

                elements.add(new AbstractAddElement(DbORPrimaryUnique.metaClass, new MetaClass[] {
                        DbORTable.metaClass, DbORView.metaClass }, LocaleMgr.action
                        .getString("PrimaryKey"), LocaleMgr.action.getString("addPrimaryKey"),
                        GraphicUtil.loadIcon(DbORPrimaryUnique.class, "resources/dborprimary.gif"),
                        null) {
                    protected void postCreateElement(DbObject dbo) throws DbException {
                        if (dbo != null) {
                            ((DbORPrimaryUnique) dbo).setPrimary(Boolean.TRUE);
                            //((DbORPrimaryUnique)dbo).setName(LocaleMgr.action.getString("PrimaryUniqueKey")); // primaire vrai mais peut etre aussi <1>, <2>... en meme temps
                        }
                    }
                });
                elements.add(new AbstractAddElement(DbORPrimaryUnique.metaClass, new MetaClass[] {
                        DbORTable.metaClass, DbORView.metaClass }, LocaleMgr.action
                        .getString("UniqueKey"), LocaleMgr.action.getString("addUniqueKey"),
                        GraphicUtil.loadIcon(DbORPrimaryUnique.class, "resources/dborunique.gif"),
                        null) {
                    protected void postCreateElement(DbObject dbo) throws DbException {
                        if (dbo != null) {
                            ((DbORPrimaryUnique) dbo).setPrimary(Boolean.FALSE);
                            ((DbORPrimaryUnique) dbo).setName(LocaleMgr.action
                                    .getString("UniqueKey"));
                        }
                    }
                });
            }

            if (mode != DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                elements.add(new AddElement(DbORIndex.metaClass, new MetaClass[] {
                        DbORTable.metaClass, DbORPrimaryUnique.metaClass, DbORForeign.metaClass,
                        DbORView.metaClass }) {
                    public void update() throws DbException {
                        DbObject[] semObjs = ApplicationContext.getFocusManager()
                                .getSelectedSemanticalObjects();
                        int nb = semObjs.length;
                        for (int i = 0; i < semObjs.length; i++) {
                            if (!(semObjs[i] instanceof DbORConstraint))
                                continue;
                            if (semObjs[i]
                                    .get(semObjs[i] instanceof DbORPrimaryUnique ? DbORPrimaryUnique.fIndex
                                            : DbORForeign.fIndex) != null)
                                nb--;
                        }
                        setEnabled(nb == semObjs.length);
                    }

                    public DbObject createElement(DbObject composite) throws DbException {
                        MetaClass metaClass = AnyORObject.getTargetMetaClasses(composite)[AnyORObject.I_INDEX];
                        DbORIndex index = null;
                        if (composite instanceof DbORAbsTable) {
                            index = (DbORIndex) composite.createComponent(metaClass);
                        } else if (composite
                                .get(composite instanceof DbORPrimaryUnique ? DbORPrimaryUnique.fIndex
                                        : DbORForeign.fIndex) == null) {
                            DbORConstraint constraint = (DbORConstraint) composite;
                            index = (DbORIndex) constraint.getComposite()
                                    .createComponent(metaClass);
                            index.setConstraint(constraint); // a trigger will initialize the index compostion from the constraint composition
                            if (constraint instanceof DbORPrimaryUnique)
                                index.setUnique(Boolean.TRUE);
                            index.setName(constraint.getName());
                            index.setPhysicalName(constraint.getPhysicalName());
                        }

                        return index;
                    }
                });
            elements.add(new AddElement(DbORUser.metaClass,
                    new MetaClass[] { DbORUserNode.metaClass }));

            if (mode != DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                // Informix
                elements.add(new AddElement(DbINFDbspace.metaClass,
                        new MetaClass[] { DbINFDatabase.metaClass }));
                elements.add(new AddElement(DbINFSbspace.metaClass,
                        new MetaClass[] { DbINFDatabase.metaClass }));

                // Oracle
                elements.add(new AddElement(DbORADataFile.metaClass,
                        new MetaClass[] { DbORATablespace.metaClass }));
                elements.add(new AddElement(DbORARedoLogGroup.metaClass,
                        new MetaClass[] { DbORADatabase.metaClass }));
                elements.add(new AddElement(DbORARedoLogFile.metaClass,
                        new MetaClass[] { DbORARedoLogGroup.metaClass }));
                elements.add(new AddElement(DbORARollbackSegment.metaClass,
                        new MetaClass[] { DbORADatabase.metaClass }));
                elements.add(new AddElement(DbORATablespace.metaClass,
                        new MetaClass[] { DbORADatabase.metaClass }));
                elements.add(new AddElement(DbORANestedTableStorage.metaClass,
                        new MetaClass[] { DbORATable.metaClass }));
                elements.add(new AddElement(DbORASequence.metaClass,
                        new MetaClass[] { DbORADataModel.metaClass }));
                elements.add(new AddElement(DbORAPartition.metaClass, new MetaClass[] {
                        DbORATable.metaClass, DbORAIndex.metaClass }));
                elements.add(new AddElement(DbORASubPartition.metaClass,
                        new MetaClass[] { DbORAPartition.metaClass }));
                elements.add(new AddElement(DbORALobStorage.metaClass,
                        new MetaClass[] { DbORATable.metaClass }));
                elements.add(new AddElement(DbORAPackage.metaClass,
                        new MetaClass[] { DbORAProgramUnitLibrary.metaClass }));

                // DB2-UDB
                elements.add(new AddElement(DbIBMSequence.metaClass,
                        new MetaClass[] { DbIBMDataModel.metaClass }));
                elements.add(new AddElement(DbIBMDbPartitionGroup.metaClass,
                        new MetaClass[] { DbIBMDatabase.metaClass }));
                elements.add(new AddElement(DbIBMBufferPool.metaClass,
                        new MetaClass[] { DbIBMDatabase.metaClass }));
                elements.add(new AddElement(DbIBMExceptClause.metaClass,
                        new MetaClass[] { DbIBMBufferPool.metaClass }));
                elements.add(new AddElement(DbIBMTablespace.metaClass,
                        new MetaClass[] { DbIBMDatabase.metaClass }));
                elements.add(new AddElement(DbIBMContainerClause.metaClass,
                        new MetaClass[] { DbIBMTablespace.metaClass }));
                elements.add(new AddElement(DbIBMContainerItem.metaClass,
                        new MetaClass[] { DbIBMContainerClause.metaClass }));
            }
        }
        //end (SMSFilter.RELATIONAL)

        // BPM
        //(SMSFilter.BPM))       
        if (terminology == null) {

            elements.add(new AddElement(DbBEUseCase.metaClass,
                    new MetaClass[] { DbBEModel.metaClass }, LocaleMgr.action.getString("context"),
                    LocaleMgr.action.getString("addContext"), GraphicUtil.loadIcon(
                            DbBEUseCase.class, "resources/dbbecontext.gif"), null) {
                protected void postCreateElement(DbObject context) throws DbException {
                    if (context != null) {
                        // Create the diagram according to the user option
                        if (DiagramAutomaticCreationOptionGroup.isCreateBPMModelContextDiagram()) {
                            BEUtility util = BEUtility.getSingleInstance();
                            DbSMSProject proj = (DbSMSProject) context.getProject();
                            DbBENotation notation = (DbBENotation) proj.findComponentByName(
                                    DbBENotation.metaClass,
                                    ((DbBEModel) context.getComposite()).getTerminologyName());
                            if (notation == null)
                                notation = proj.getBeDefaultNotation();
                            util.createBEDiagram((DbBEUseCase) context, notation);
                        }
                    }
                }
            });
            elements.add(new AddElement(DbBEUseCase.metaClass,
                    new MetaClass[] { DbBEUseCase.metaClass }) {
                public void update() throws DbException {
                    DbObject[] dbos = ApplicationContext.getFocusManager()
                            .getSelectedSemanticalObjects();
                    for (int i = 0; i < dbos.length; i++) {
                        if (((DbBEUseCase) dbos[i]).isExternal()) {
                            setEnabled(false);
                            return;
                        }
                    }
                    super.update();
                }
            });
            elements.add(new AddElement(DbBEUseCase.metaClass,
                    new MetaClass[] { DbBEUseCase.metaClass }, MessageFormat.format(SUPER_0,
                            new Object[] { DbBEUseCase.metaClass.getGUIName() }), LocaleMgr.action
                            .getString("addSuperProcess"), GraphicUtil.loadIcon(DbBEUseCase.class,
                            "resources/dbbecontext.gif"), new Object[] {}) {
                public void update() throws DbException {
                    DbObject[] dbos = ApplicationContext.getFocusManager()
                            .getSelectedSemanticalObjects();
                    for (int i = 0; i < dbos.length; i++) {
                        if (((DbBEUseCase) dbos[i]).isExternal()) {
                            setEnabled(false);
                            return;
                        }
                    }
                    super.update();
                } //end update()

                public DbObject createElement(DbObject composite) throws DbException {
                    DbBEUseCase newLevel = null;

                    if (composite instanceof DbBEUseCase) {
                        DbBEUseCase currentProcess = (DbBEUseCase) composite;
                        DbObject supercomposite = currentProcess.getComposite();
                        newLevel = (DbBEUseCase) supercomposite
                                .createComponent(DbBEUseCase.metaClass);
                        currentProcess.setComposite(newLevel);

                        //set name
                        String name = MessageFormat.format(SUPER_0,
                                new Object[] { currentProcess.getName() });
                        newLevel.setName(name);

                        //set identifier
                        Integer id = currentProcess.getNumericIdentifier();
                        newLevel.setNumericIdentifier(id);
                        currentProcess.setNumericIdentifier(new Integer(1));

                        //create a diagram for each diagram in composite (using the same notation)
                        DbRelationN relN = currentProcess.getComponents();
                        DbEnumeration dbEnum = relN.elements(DbBEDiagram.metaClass);
                        while (dbEnum.hasMoreElements()) {
                            DbBEDiagram diag = (DbBEDiagram) dbEnum.nextElement();
                            DbBENotation notation = diag.getNotation();
                            DbBEDiagram newDiag = new DbBEDiagram(newLevel);
                            newDiag.setNotation(notation);
                        } //end while
                        dbEnum.close();

                        //for each graphical representation of currentProcess
                        relN = currentProcess.getClassifierGos();
                        dbEnum = relN.elements(DbBEUseCaseGo.metaClass);
                        while (dbEnum.hasMoreElements()) {
                            DbBEUseCaseGo go = (DbBEUseCaseGo) dbEnum.nextElement();
                            Rectangle rect = go.getRectangle();

                            //create a graphical representation for its composite
                            DbBEDiagram diag = (DbBEDiagram) go.getComposite();
                            DbBEUseCaseGo newGo = new DbBEUseCaseGo(diag, newLevel);
                            newGo.setRectangle(rect);

                            //delete the original go
                            go.remove();

                        } //end while
                        dbEnum.close();

                    } //end if

                    return newLevel;
                } //end createElement()
            });

            elements.add(new AddElement(DbBEActor.metaClass,
                    new MetaClass[] { DbBEModel.metaClass }));
            elements.add(new AddElement(DbBEStore.metaClass,
                    new MetaClass[] { DbBEModel.metaClass }));
            elements.add(new AddElement(DbBEQualifier.metaClass,
                    new MetaClass[] { DbBEModel.metaClass }));
            elements.add(new AddElement(DbBEResource.metaClass,
                    new MetaClass[] { DbBEModel.metaClass }));
            elements.add(new AddElement(DbBEContextGo.metaClass,
                    new MetaClass[] { DbBEDiagram.metaClass }, GraphicUtil.loadIcon(
                            DbBEContextGo.class, "resources/dbbecontextgo.gif")) {
                public DbObject createElement(DbObject composite) throws DbException {
                    BEUtility util = BEUtility.getSingleInstance();
                    DbBEContextGo component = util.createFrame((DbBEDiagram) composite,
                            (DbSMSClassifier) composite.getComposite());
                    ApplicationDiagram diagram = new ApplicationDiagram(
                            (DbSemanticalObject) composite.getComposite(), composite, SMSToolkit
                                    .getToolkit(composite).createGraphicalComponentFactory(),
                            MainFrame.getSingleton().getDiagramsToolGroup());
                    Dimension dim = Diagram.getPageSize(((DbSMSDiagram) composite).getPageFormat(),
                            ((DbSMSDiagram) composite).getPrintScale().intValue());
                    int x = (int) (dim.getWidth() * 0.2 / 2);
                    int y = (int) (dim.getHeight() * 0.15 / 2);
                    int width = (int) (dim.getWidth() * 0.8);
                    int height = (int) (dim.getHeight() * 0.85);
                    if (component != null)
                        component.setRectangle(new Rectangle(x, y, width, height));
                    diagram.delete();
                    return component;
                }
            });
            elements.add(new AddElement(DbBEDiagram.metaClass, DbBEUseCase.metaClass) {
                public void update() throws DbException {
                    DbObject[] dbos = ApplicationContext.getFocusManager()
                            .getSelectedSemanticalObjects();
                    for (int i = 0; i < dbos.length; i++) {
                        if (((DbBEUseCase) dbos[i]).isExternal()) {
                            setEnabled(false);
                            return;
                        }
                    }
                    super.update();
                }
            });

        } else {

            //context

            String term = terminology.getTerm(DbBEUseCaseGo.metaClass);
            Icon icon = terminology.getIcon(DbBEUseCaseGo.metaClass);
            AddElement elem = new AddElement(DbBEUseCase.metaClass,
                    new MetaClass[] { DbBEModel.metaClass }, term, "Add a " + term, icon, null) {
                protected void postCreateElement(DbObject context) throws DbException {
                    if (context != null) {
                        // Create the diagram according to the user option
                        if (DiagramAutomaticCreationOptionGroup.isCreateBPMModelContextDiagram()) {
                            BEUtility util = BEUtility.getSingleInstance();
                            DbSMSProject proj = (DbSMSProject) context.getProject();
                            DbBENotation notation = (DbBENotation) proj.findComponentByName(
                                    DbBENotation.metaClass,
                                    ((DbBEModel) context.getComposite()).getTerminologyName());
                            if (notation == null)
                                notation = proj.getBeDefaultNotation();
                            util.createBEDiagram((DbBEUseCase) context, notation);
                        }
                    }
                }
            };
            elements.add(elem);

            //use case
            elem = new AddElement(DbBEUseCase.metaClass, new MetaClass[] { DbBEUseCase.metaClass }) {
                public void update() throws DbException {
                    DbObject[] dbos = ApplicationContext.getFocusManager()
                            .getSelectedSemanticalObjects();
                    for (int i = 0; i < dbos.length; i++) {
                        if (((DbBEUseCase) dbos[i]).isExternal()) {
                            setEnabled(false);
                            return;
                        }
                    }
                    super.update();
                }
            };
            term = terminology.getTerm(DbBEUseCase.metaClass);
            icon = terminology.getIcon(DbBEUseCase.metaClass);
            elem.setIcon(icon);
            elem.setName(term);
            elements.add(elem);

            //context
            icon = terminology.getIcon(DbBEUseCase.metaClass);
            elem = new AddElement(DbBEUseCase.metaClass, new MetaClass[] { DbBEUseCase.metaClass },
                    "Super " + term, "Add Super " + term, icon, new Object[] {}) {
                public void update() throws DbException {
                    DbObject[] dbos = ApplicationContext.getFocusManager()
                            .getSelectedSemanticalObjects();
                    for (int i = 0; i < dbos.length; i++) {
                        if (((DbBEUseCase) dbos[i]).isExternal()) {
                            setEnabled(false);
                            return;
                        }
                    }
                    super.update();
                } //end update()

                public DbObject createElement(DbObject composite) throws DbException {
                    DbBEUseCase newLevel = null;

                    if (composite instanceof DbBEUseCase) {
                        DbBEUseCase currentProcess = (DbBEUseCase) composite;
                        DbObject supercomposite = currentProcess.getComposite();
                        newLevel = (DbBEUseCase) supercomposite
                                .createComponent(DbBEUseCase.metaClass);
                        currentProcess.setComposite(newLevel);

                        //set name
                        String name = MessageFormat.format(SUPER_0,
                                new Object[] { currentProcess.getName() });
                        newLevel.setName(name);

                        //set identifier
                        Integer id = currentProcess.getNumericIdentifier();
                        newLevel.setNumericIdentifier(id);
                        currentProcess.setNumericIdentifier(new Integer(1));

                        //create a diagram for each diagram in composite (using the same notation)
                        DbRelationN relN = currentProcess.getComponents();
                        DbEnumeration dbEnum = relN.elements(DbBEDiagram.metaClass);
                        while (dbEnum.hasMoreElements()) {
                            DbBEDiagram diag = (DbBEDiagram) dbEnum.nextElement();
                            DbBENotation notation = diag.getNotation();
                            DbBEDiagram newDiag = new DbBEDiagram(newLevel);
                            newDiag.setNotation(notation);
                        } //end while
                        dbEnum.close();

                        //for each graphical representation of currentProcess
                        relN = currentProcess.getClassifierGos();
                        dbEnum = relN.elements(DbBEUseCaseGo.metaClass);
                        while (dbEnum.hasMoreElements()) {
                            DbBEUseCaseGo go = (DbBEUseCaseGo) dbEnum.nextElement();
                            Rectangle rect = go.getRectangle();

                            //create a graphical representation for its composite
                            DbBEDiagram diag = (DbBEDiagram) go.getComposite();
                            DbBEUseCaseGo newGo = new DbBEUseCaseGo(diag, newLevel);
                            newGo.setRectangle(rect);

                            //delete the original go
                            go.remove();

                        } //end while
                        dbEnum.close();

                        BEUtility.updateUseCaseTerminology(newLevel);

                    } //end if

                    return newLevel;
                } //end createElement()
            };
            elements.add(elem);

            elem = new AddElement(DbBEActor.metaClass, new MetaClass[] { DbBEModel.metaClass });
            term = terminology.getTerm(DbBEActor.metaClass);
            icon = terminology.getIcon(DbBEActor.metaClass);
            elem.setIcon(icon);
            elem.setName(term);
            elements.add(elem);

            elem = new AddElement(DbBEStore.metaClass, new MetaClass[] { DbBEModel.metaClass });
            term = terminology.getTerm(DbBEStore.metaClass);
            icon = terminology.getIcon(DbBEStore.metaClass);
            elem.setIcon(icon);
            elem.setName(term);
            elements.add(elem);

            elements.add(new AddElement(DbBEQualifier.metaClass,
                    new MetaClass[] { DbBEModel.metaClass }));
            elements.add(new AddElement(DbBEResource.metaClass,
                    new MetaClass[] { DbBEModel.metaClass }));

            elements.add(new AddElement(DbBEContextGo.metaClass,
                    new MetaClass[] { DbBEDiagram.metaClass }) {
                public DbObject createElement(DbObject composite) throws DbException {
                    BEUtility util = BEUtility.getSingleInstance();
                    DbBEContextGo component = util.createFrame((DbBEDiagram) composite,
                            (DbSMSClassifier) composite.getComposite());
                    ApplicationDiagram diagram = new ApplicationDiagram(
                            (DbSemanticalObject) composite.getComposite(), composite, SMSToolkit
                                    .getToolkit(composite).createGraphicalComponentFactory(),
                            MainFrame.getSingleton().getDiagramsToolGroup());
                    Dimension dim = Diagram.getPageSize(((DbSMSDiagram) composite).getPageFormat(),
                            ((DbSMSDiagram) composite).getPrintScale().intValue());
                    int x = (int) (dim.getWidth() * 0.2 / 2);
                    int y = (int) (dim.getHeight() * 0.15 / 2);
                    int width = (int) (dim.getWidth() * 0.8);
                    int height = (int) (dim.getHeight() * 0.85);
                    if (component != null) {
                        component.setRectangle(new Rectangle(x, y, width, height));
                    }

                    diagram.delete();
                    return component;
                }
            });

            icon = terminology.getIcon(DbBEDiagram.metaClass);
            elem = new AddElement(DbBEDiagram.metaClass, DbBEUseCase.metaClass) {
                public void update() throws DbException {
                    DbObject[] dbos = ApplicationContext.getFocusManager()
                            .getSelectedSemanticalObjects();
                    for (int i = 0; i < dbos.length; i++) {
                        if (((DbBEUseCase) dbos[i]).isExternal()) {
                            setEnabled(false);
                            return;
                        }
                    }
                    super.update();
                }
            };
            elem.setIcon(icon);
            elem.setIcon(icon);
            elements.add(elem);
        }
        //end (SMSFilter.BPM)

        elements.add(new AddElement(DbBEModel.metaClass, new MetaClass[] { DbSMSProject.metaClass,
                DbSMSUserDefinedPackage.metaClass }, "-", null, DbBEModel.metaClass.getIcon(), null));

        //(SMSFilter.BPM))
        elements.add(new AddBPMModel(DbBEModel.metaClass, new MetaClass[] { DbSMSProject.metaClass,
                DbSMSUserDefinedPackage.metaClass }, DbBEModel.metaClass.getGUIName(),
                DbBEModel.metaClass.getIcon()));

        elements.add(new AddElement(DbBEModel.metaClass, new MetaClass[] { DbSMSProject.metaClass,
                DbSMSUserDefinedPackage.metaClass }, "-", null, DbBEModel.metaClass.getIcon(), null));

        //(SMSFilter.BPM))
        elements.add(new AddBPMModel(DbBEModel.metaClass, new MetaClass[] { DbSMSProject.metaClass,
                DbSMSUserDefinedPackage.metaClass }, UML_MODEL, dbbeumlmodel));

        // (SMSFilter.JAVA)){
        elements.add(new AddElement(DbJVClassModel.metaClass, new MetaClass[] {
                DbSMSProject.metaClass, DbSMSUserDefinedPackage.metaClass }, JAVA_CLASS_MODEL,
                null, DbJVClassModel.metaClass.getIcon(), null));

        elements.add(new AddElement(DbBEModel.metaClass, new MetaClass[] { DbSMSProject.metaClass,
                DbSMSUserDefinedPackage.metaClass }, "-", null, DbBEModel.metaClass.getIcon(), null));

        //(SMSFilter.RELATIONAL))
        elements.add(new AddElement(DbORCommonItemModel.metaClass, new MetaClass[] {
                DbSMSProject.metaClass, DbSMSUserDefinedPackage.metaClass }) {
            protected void postCreateElement(DbObject commonItemModel) throws DbException {
                if (commonItemModel != null) {
                    // Create the diagram according to the user option
                    if (DiagramAutomaticCreationOptionGroup.isCreateCommonItemModelDiagram()) {
                        new DbORDiagram(commonItemModel);
                    }
                }
            }
        });
        elements.add(new AddAbstractPackageElement(DbORDomainModel.metaClass, new MetaClass[] {
                DbSMSProject.metaClass, DbSMSUserDefinedPackage.metaClass }) {
            protected DbObject createTargetElement(DbObject composite, DbSMSTargetSystem target,
                    boolean withConceptualMode) throws DbException {
                DbORDomainModel model;
                model = new DbORDomainModel(composite, target);

                // Create the diagram according to the user option
                if (DiagramAutomaticCreationOptionGroup.isCreateDomainModelDiagram()) {
                    new DbORDiagram(model);
                }
                return model;
            }
        });

        elements.add(new AddElement(DbSMSUserDefinedPackage.metaClass, new MetaClass[] {
                DbSMSProject.metaClass, DbSMSUserDefinedPackage.metaClass }));
        elements.add(new AddElement(DbSMSLinkModel.metaClass, new MetaClass[] {
                DbSMSProject.metaClass, DbSMSUserDefinedPackage.metaClass, DbORDataModel.metaClass,
                DbOOClassModel.metaClass }));

        pool = new AddElement[elements.size()];
        for (int i = 0; i < pool.length; i++)
            pool[i] = (AddElement) elements.get(i);
    }

    static AddElement[] getAddElements(Terminology terminology) {

        boolean bUseExisting = true;
        DbObject[] dbObject = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        try {
            for (int i = 0; i < dbObject.length; i++) {
                dbObject[i].getDb().beginReadTrans();
                if (dbObject[i] instanceof DbORDataModel) {
                    DbORDataModel dataModel = (DbORDataModel) dbObject[i];
                    if (dataModel.getOperationalMode() == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                        bUseExisting = false;
                        dbObject[i].getDb().commitTrans();
                        break;
                    }
                } else {
                    DbObject otherdbObject = dbObject[i]
                            .getCompositeOfType(DbORDataModel.metaClass);
                    if (otherdbObject != null) {
                        DbORDataModel dataModel = (DbORDataModel) otherdbObject;
                        if (dataModel.getOperationalMode() == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                            bUseExisting = false;
                            dbObject[i].getDb().commitTrans();
                            break;
                        }
                    }
                }
                dbObject[i].getDb().commitTrans();
            }
        } catch (DbException dbe) {
            throw new RuntimeException("Add elements not accessible.");
        }

        if (bUseExisting) {
            if (singleton == null || terminology != oldTerminology) {
                singleton = new AddElementPool(terminology);
                oldTerminology = terminology;
            }
            return singleton.pool;
        } else {
            singleton = new AddElementPool(terminology);
            oldTerminology = terminology;
            return singleton.pool;
        }
    }

    //
    // Add a BPM model
    // 
    private static String DEFAULT = org.modelsphere.jack.srtool.international.LocaleMgr.screen
            .getString("default");

    private static class AddBPMModel extends AddElement {
        AddBPMModel(MetaClass componentMetaclass, MetaClass[] compositeMetaclass, String term,
                Icon icon) {
            super(componentMetaclass, compositeMetaclass, term, null, icon, null);
        }

        public synchronized void update() throws DbException {
            DbProject project = ApplicationContext.getFocusManager().getCurrentProject();
            if ((project == null) || !(project instanceof DbSMSProject)) { // multi project not supported
                setEnabled(false);
                return;
            }

            DbObject[] selobjs = ApplicationContext.getFocusManager()
                    .getSelectedSemanticalObjects();
            if (selobjs == null || selobjs.length != 1) { // multi selection not supported
                setEnabled(false);
                return;
            }

            project.getDb().beginReadTrans();
            String[] items = null;
            if (getIcon().equals(dbbeumlmodel))
                items = getBPMUmlNotationStrings((DbSMSProject) project);
            else
                items = getBPMNotationStrings((DbSMSProject) project);

            setChoiceValues(items);
            project.getDb().commitTrans();
            super.update();
        } //end update()

        public DbObject createElement(DbObject composite) throws DbException {
            MainFrame frame = MainFrame.getSingleton();
            DbProject project = ApplicationContext.getFocusManager().getCurrentProject();
            if ((project == null) || !(project instanceof DbSMSProject)) { // multi project not supported
                return null;
            }

            DbBENotation[] items = null;
            if (getIcon().equals(dbbeumlmodel))
                items = getBPMUmlNotations((DbSMSProject) project);
            else
                items = getBPMNotations((DbSMSProject) project);

            if (choiceValuesSelectedIndex < items.length) {
                DbBENotation selectedNotation = items[choiceValuesSelectedIndex];
                if (selectedNotation != null) {
                    DbObject elem = createElement(composite, selectedNotation);
                    int nId = selectedNotation.getMasterNotationID().intValue();
                    if (nId >= 13 && nId <= 19)
                        elem.setName(TerminologyInitializer.UML_MODEL);
                    return elem;
                } else {
                    return new DbJVClassModel(project);
                }
            } else {
                String title = DbBENotation.metaClass.getGUIName(true);
                String msg = "";
                List<DbBENotation> list = new ArrayList<DbBENotation>();
                DbRelationN relN = project.getComponents();
                DbEnumeration dbEnum = relN.elements(DbBENotation.metaClass);
                while (dbEnum.hasMoreElements()) {
                    DbBENotation notation = (DbBENotation) dbEnum.nextElement();
                    list.add(notation);
                } //end while
                dbEnum.close();

                Object[] notations = (Object[]) list.toArray();
                int nb = notations.length;
                List<DbBENotation> alobjs = new ArrayList<DbBENotation>();
                for (int i = 0; i < nb; i++) {
                    int masterNotationID = ((DbBENotation) notations[i]).getMasterNotationID()
                            .intValue();
                    if (!(masterNotationID >= 13 && masterNotationID <= 19)) {
                        alobjs.add(((DbBENotation) notations[i]));
                    }
                }
                List<String> al = new ArrayList<String>();
                for (int i = 0; i < alobjs.size(); i++)
                    al.add(alobjs.get(i).getName());

                Comparator<?> comp = null;
                int idx = LookupDialog.selectOne(frame, title, msg, al.toArray(), -1, comp);
                if (idx != -1) {
                    DbBENotation notation = (DbBENotation) alobjs.get(idx);
                    if (!notation.equals(((DbSMSProject) project).getBeDefaultNotation())) {
                        List<DbBENotation> commonNotations = DbInitialization
                                .getCommonNotations((DbSMSProject) project);
                        if (!commonNotations.contains(notation)) {
                            commonNotations.add(notation);
                        }
                    } //end if

                    DbObject elem = createElement(composite, notation);
                    if (notation != null) {
                        int nId = notation.getMasterNotationID().intValue();
                        if (nId >= 13 && nId <= 19)
                            elem.setName(TerminologyInitializer.UML_MODEL);
                    }
                    return elem;
                }
            } //end if
            return null;
        } //end createElement()

        private synchronized String[] getBPMNotationStrings(DbSMSProject project)
                throws DbException {
            DbBENotation[] notations = getBPMNotations(project);
            int nb = notations.length;
            String[] strings = new String[nb + 2];
            for (int i = 0; i < nb; i++) {
                strings[i] = notations[i].getName();
            } //end for

            strings[0] += " (" + DEFAULT + ")";
            strings[nb] = null;
            strings[nb + 1] = LocaleMgr.action.getString("Others_");
            return strings;
        } //end getBPMNotationStrings()

        private synchronized String[] getBPMUmlNotationStrings(DbSMSProject project)
                throws DbException {
            DbBENotation[] notations = getBPMUmlNotations(project);
            int nb = notations.length;
            String[] strings = new String[nb];
            strings[0] = DbBENotation.UML_CLASS_DIAGRAM;
            for (int i = 1; i < nb; i++) {
                strings[i] = notations[i].getName();
            } //end for

            return strings;
        } //end getBPMNotationStrings()

        private DbBENotation[] getBPMNotations(DbSMSProject project) throws DbException {
            List<DbBENotation> notations = DbInitialization.getCommonNotations(project);
            int nb = notations.size();
            DbBENotation[] items = new DbBENotation[nb + 1];
            items[0] = project.getBeDefaultNotation();
            for (int i = 0; i < nb; i++) {
                items[i + 1] = (DbBENotation) notations.get(i);
            }
            return items;
        }

        private DbBENotation[] getBPMUmlNotations(DbSMSProject project) throws DbException {
            List<DbBENotation> notations = DbInitialization.getCommonUmlNotations(project);
            int nb = notations.size() + 1;
            DbBENotation[] items = new DbBENotation[nb];
            items[0] = null; //indicate the uml class diagram special case
            for (int i = 1; i < nb; i++) {
                items[i] = (DbBENotation) notations.get(i - 1);
            }
            return items;
        }

        private DbObject createElement(DbObject composite, DbBENotation notation)
                throws DbException {
            DbBEModel model = (DbBEModel) super.createElement(composite);

            ////
            // set the terminoilogy name according to the master notation

            notation = BEUtility.getSingleInstance().getMasterNotation(notation);
            model.setTerminologyName(notation.getName());
            BEUtility util = BEUtility.getSingleInstance();
            DbBEUseCase context = util.createUseCase(model, notation);
            DbBEDiagram diagram = util.createBEDiagram(context, notation);

            //init cells according default values defined in notation
            DbRelationN relN = diagram.getComponents();
            DbEnumeration dbEnum = relN.elements(DbBEContextGo.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbBEContextGo frame = (DbBEContextGo) dbEnum.nextElement();
                DbRelationN relN2 = frame.getComponents();
                DbEnumeration dbEnum2 = relN2.elements(DbBEContextCell.metaClass);
                while (dbEnum2.hasMoreElements()) {
                    DbBEContextCell cell = (DbBEContextCell) dbEnum2.nextElement();
                    CellUtility.initValue(cell, notation);
                } //end while
                dbEnum2.close();

            } //end while
            dbEnum.close();

            util.initAccordingNotation(diagram);

            return model;
        } //end createElement()  
    } //end AddBPMModel

} //end AddEleemntPool
