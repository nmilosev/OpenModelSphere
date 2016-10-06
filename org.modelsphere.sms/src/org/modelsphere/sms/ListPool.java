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

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Icon;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.list.ListColumn;
import org.modelsphere.jack.srtool.list.ListDescriptor;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBEQualifier;
import org.modelsphere.sms.be.db.DbBEResource;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.db.DbSMSAbstractPackage;
import org.modelsphere.sms.db.DbSMSBuiltInTypeNode;
import org.modelsphere.sms.db.DbSMSBuiltInTypePackage;
import org.modelsphere.sms.db.DbSMSLink;
import org.modelsphere.sms.db.DbSMSLinkModel;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSTypeClassifier;
import org.modelsphere.sms.db.DbSMSUmlConstraint;
import org.modelsphere.sms.db.DbSMSUmlExtensibility;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOOAssociationEnd;
import org.modelsphere.sms.oo.db.srtypes.OOAggregation;
import org.modelsphere.sms.oo.java.db.DbJVAssociation;
import org.modelsphere.sms.oo.java.db.DbJVAssociationEnd;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVCompilationUnit;
import org.modelsphere.sms.oo.java.db.DbJVConstructor;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVInheritance;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.oo.java.db.DbJVParameter;
import org.modelsphere.sms.oo.java.db.DbJVPrimitiveType;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORField;
import org.modelsphere.sms.or.db.DbORIndexKey;
import org.modelsphere.sms.or.generic.db.DbGECheck;
import org.modelsphere.sms.or.generic.db.DbGEColumn;
import org.modelsphere.sms.or.generic.db.DbGEDataModel;
import org.modelsphere.sms.or.generic.db.DbGEForeign;
import org.modelsphere.sms.or.generic.db.DbGEIndex;
import org.modelsphere.sms.or.generic.db.DbGEOperationLibrary;
import org.modelsphere.sms.or.generic.db.DbGEParameter;
import org.modelsphere.sms.or.generic.db.DbGEPrimaryUnique;
import org.modelsphere.sms.or.generic.db.DbGEProcedure;
import org.modelsphere.sms.or.generic.db.DbGETable;
import org.modelsphere.sms.or.generic.db.DbGETrigger;
import org.modelsphere.sms.or.generic.db.DbGEView;
import org.modelsphere.sms.or.ibm.db.DbIBMCheck;
import org.modelsphere.sms.or.ibm.db.DbIBMColumn;
import org.modelsphere.sms.or.ibm.db.DbIBMDataModel;
import org.modelsphere.sms.or.ibm.db.DbIBMForeign;
import org.modelsphere.sms.or.ibm.db.DbIBMIndex;
import org.modelsphere.sms.or.ibm.db.DbIBMOperationLibrary;
import org.modelsphere.sms.or.ibm.db.DbIBMParameter;
import org.modelsphere.sms.or.ibm.db.DbIBMPrimaryUnique;
import org.modelsphere.sms.or.ibm.db.DbIBMProcedure;
import org.modelsphere.sms.or.ibm.db.DbIBMTable;
import org.modelsphere.sms.or.ibm.db.DbIBMTrigger;
import org.modelsphere.sms.or.ibm.db.DbIBMView;
import org.modelsphere.sms.or.informix.db.DbINFCheck;
import org.modelsphere.sms.or.informix.db.DbINFColumn;
import org.modelsphere.sms.or.informix.db.DbINFDataModel;
import org.modelsphere.sms.or.informix.db.DbINFDatabase;
import org.modelsphere.sms.or.informix.db.DbINFDbspace;
import org.modelsphere.sms.or.informix.db.DbINFForeign;
import org.modelsphere.sms.or.informix.db.DbINFIndex;
import org.modelsphere.sms.or.informix.db.DbINFOperationLibrary;
import org.modelsphere.sms.or.informix.db.DbINFParameter;
import org.modelsphere.sms.or.informix.db.DbINFPrimaryUnique;
import org.modelsphere.sms.or.informix.db.DbINFProcedure;
import org.modelsphere.sms.or.informix.db.DbINFSbspace;
import org.modelsphere.sms.or.informix.db.DbINFTable;
import org.modelsphere.sms.or.informix.db.DbINFTrigger;
import org.modelsphere.sms.or.informix.db.DbINFView;
import org.modelsphere.sms.or.oracle.db.DbORACheck;
import org.modelsphere.sms.or.oracle.db.DbORAColumn;
import org.modelsphere.sms.or.oracle.db.DbORADataFile;
import org.modelsphere.sms.or.oracle.db.DbORADataModel;
import org.modelsphere.sms.or.oracle.db.DbORADatabase;
import org.modelsphere.sms.or.oracle.db.DbORAForeign;
import org.modelsphere.sms.or.oracle.db.DbORAIndex;
import org.modelsphere.sms.or.oracle.db.DbORALobStorage;
import org.modelsphere.sms.or.oracle.db.DbORANestedTableStorage;
import org.modelsphere.sms.or.oracle.db.DbORAPackage;
import org.modelsphere.sms.or.oracle.db.DbORAParameter;
import org.modelsphere.sms.or.oracle.db.DbORAPartition;
import org.modelsphere.sms.or.oracle.db.DbORAPrimaryUnique;
import org.modelsphere.sms.or.oracle.db.DbORAProcedure;
import org.modelsphere.sms.or.oracle.db.DbORAProgramUnitLibrary;
import org.modelsphere.sms.or.oracle.db.DbORARedoLogFile;
import org.modelsphere.sms.or.oracle.db.DbORARedoLogGroup;
import org.modelsphere.sms.or.oracle.db.DbORARollbackSegment;
import org.modelsphere.sms.or.oracle.db.DbORASequence;
import org.modelsphere.sms.or.oracle.db.DbORATable;
import org.modelsphere.sms.or.oracle.db.DbORATablespace;
import org.modelsphere.sms.or.oracle.db.DbORATrigger;
import org.modelsphere.sms.or.oracle.db.DbORAView;

final class ListPool extends HashMap {
    private static final String NOT_NAVIGABLE = LocaleMgr.screen.getString("NotNavigable");

    ListPool(Terminology terminology) {
        super();

        if (terminology != null) {

            // BPM
            put(DbBEModel.metaClass, new ListDescriptor[] {
                    new ListDescriptor(DbBEUseCase.metaClass, false, terminology.getTerm(
                            DbBEUseCase.metaClass, true), terminology
                            .getIcon(DbBEUseCase.metaClass)),
                    new ListDescriptor(DbBEActor.metaClass, false, terminology.getTerm(
                            DbBEActor.metaClass, true), terminology.getIcon(DbBEActor.metaClass)),
                    new ListDescriptor(DbBEStore.metaClass, false, terminology.getTerm(
                            DbBEStore.metaClass, true), terminology.getIcon(DbBEStore.metaClass)),
                    new ListDescriptor(DbBEResource.metaClass),
                    new ListDescriptor(DbBEQualifier.metaClass),
                    new ListDescriptor(DbBEDiagram.metaClass, false, DbBEDiagram.metaClass
                            .getGUIName(true), terminology.getIcon(DbBEDiagram.metaClass)), });
            put(DbBEUseCase.metaClass, new ListDescriptor[] {
                    new ListDescriptor(DbBEUseCase.metaClass, false, terminology.getTerm(
                            DbBEUseCase.metaClass, true), terminology
                            .getIcon(DbBEUseCase.metaClass)),
                    new ListDescriptor(DbBEFlow.metaClass, false, terminology.getTerm(
                            DbBEFlow.metaClass, true), terminology.getIcon(DbBEFlow.metaClass)),
                    new ListDescriptor(DbBEDiagram.metaClass, false, DbBEDiagram.metaClass
                            .getGUIName(true), terminology.getIcon(DbBEDiagram.metaClass)), });
        } else {
            // BPM
            put(DbBEModel.metaClass, new ListDescriptor[] {
                    new ListDescriptor(DbBEUseCase.metaClass),
                    new ListDescriptor(DbBEActor.metaClass),
                    new ListDescriptor(DbBEStore.metaClass),
                    new ListDescriptor(DbBEResource.metaClass),
                    new ListDescriptor(DbBEQualifier.metaClass),
                    new ListDescriptor(DbBEDiagram.metaClass, true), });
            put(DbBEUseCase.metaClass, new ListDescriptor[] {
                    new ListDescriptor(DbBEUseCase.metaClass),
                    new ListDescriptor(DbBEFlow.metaClass),
                    new ListDescriptor(DbBEDiagram.metaClass, true), });
        }
    }

    ListPool(int nMode, boolean isRelationship, boolean isModelConverted) {
        super();
        // Global
        put(DbORDomainModel.metaClass, new ListDescriptor[] {
                new ListDescriptor(DbORDomain.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbORField.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }, true), });
        put(DbORDomain.metaClass, new ListDescriptor[] { new ListDescriptor(DbORField.metaClass,
                new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
        put(DbORCommonItemModel.metaClass, new ListDescriptor[] { new ListDescriptor(
                DbORCommonItem.metaClass, new MetaClass[] { DbSMSAbstractPackage.metaClass }), });

        put(DbSMSLinkModel.metaClass, new ListDescriptor[] { new ListDescriptor(
                DbSMSLink.metaClass, new MetaClass[] { DbSMSAbstractPackage.metaClass }), });

        put(DbSMSBuiltInTypeNode.metaClass, new ListDescriptor[] { new ListDescriptor(
                DbSMSTypeClassifier.metaClass, true, LocaleMgr.screen.getString("TargetSystem"),
                LocaleMgr.action.getString("Types"), DbJVPrimitiveType.metaClass.getIcon()), });

        put(DbSMSBuiltInTypePackage.metaClass, new ListDescriptor[] { new ListDescriptor(
                DbSMSTypeClassifier.metaClass, LocaleMgr.action.getString("Types"),
                DbJVPrimitiveType.metaClass.getIcon()), });

        /*
         * put(DbSMSUserDefinedPackage.metaClass, new ListDescriptor[]{ new
         * ListDescriptor(DbSMSPackage.metaClass, new MetaClass[]{DbSMSPackage.metaClass}, false),
         * });
         */

        put(DbSMSUmlExtensibility.metaClass, new ListDescriptor[] {
                new ListDescriptor(DbSMSUmlConstraint.metaClass),
                new ListDescriptor(DbSMSStereotype.metaClass), });

        // Java

        // JV Association Front end and back end columns
        ListColumn frontEndColumn = new ListColumn() {
            public String getTitle() {
                return DbJVAssociationEnd.metaClass.getGUIName(false, false) + " 1";
            }

            public Object getValue(DbObject dbo, DbObject neighbor) throws DbException {
                if (neighbor == null)
                    return null;
                DbOOAssociationEnd end = ((DbJVAssociation) neighbor).getFrontEnd();
                if (end == null)
                    return null;
                DbObject type = end.getAssociationMember().getType();
                if (type == null)
                    return null;
                String value = "";
                value += type.getSemanticalName(DbObject.LONG_FORM);
                if (end.isNavigable()) {
                    DbObject member = end.getAssociationMember();
                    if (member != null)
                        value += " [" + member.getName() + "]"; // NOT
                    // LOCALIZABLE
                } else {
                    value += " [" + NOT_NAVIGABLE + "]"; // NOT LOCALIZABLE
                }
                if (!end.getAggregation().equals(OOAggregation.getInstance(OOAggregation.NONE)))
                    value += " (" + end.getAggregation().toString() + ")"; // NOT
                // LOCALIZABLE
                return value;
            }

            public String getRenderer() {
                return null;
            }

            public String getID() {
                return DbJVAssociationEnd.metaClass.getJClass().getName() + "1";
            }

            public int getDefaultWidth() {
                return -1;
            }
        };
        ListColumn backEndColumn = new ListColumn() {
            public String getTitle() {
                return DbJVAssociationEnd.metaClass.getGUIName(false, false) + " 2";
            }

            public Object getValue(DbObject dbo, DbObject neighbor) throws DbException {
                if (neighbor == null)
                    return null;
                DbOOAssociationEnd end = ((DbJVAssociation) neighbor).getBackEnd();
                if (end == null)
                    return null;
                DbObject type = end.getAssociationMember().getType();
                if (type == null)
                    return null;
                String value = "";
                value += type.getSemanticalName(DbObject.LONG_FORM);
                if (end.isNavigable()) {
                    DbObject member = end.getAssociationMember();
                    if (member != null)
                        value += " [" + member.getName() + "]"; // NOT
                    // LOCALIZABLE
                } else {
                    value += " [" + NOT_NAVIGABLE + "]"; // NOT LOCALIZABLE
                }
                if (!end.getAggregation().equals(OOAggregation.getInstance(OOAggregation.NONE)))
                    value += " (" + end.getAggregation().toString() + ")"; // NOT
                // LOCALIZABLE
                return value;
            }

            public String getRenderer() {
                return null;
            }

            public String getID() {
                return DbJVAssociationEnd.metaClass.getJClass().getName() + "2";
            }

            public int getDefaultWidth() {
                return -1;
            }
        };

        ArrayList columns = DbJVAssociation.metaClass.getScreenMetaFields();

        // if(nMode != DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP)
        columns.add(frontEndColumn);
        columns.add(backEndColumn);

        ListDescriptor descriptor = new ListDescriptor(DbJVAssociation.metaClass,
                new MetaClass[] { DbJVClassModel.metaClass }, columns.toArray());
        put(DbJVClassModel.metaClass, new ListDescriptor[] {
                new ListDescriptor(DbJVPackage.metaClass,
                        new MetaClass[] { DbJVClassModel.metaClass }, true),
                new ListDescriptor(DbJVClass.metaClass,
                        new MetaClass[] { DbJVClassModel.metaClass }, true),
                new ListDescriptor(DbJVDataMember.metaClass,
                        new MetaClass[] { DbJVClassModel.metaClass }, true),
                new ListDescriptor(DbJVMethod.metaClass,
                        new MetaClass[] { DbJVClassModel.metaClass }, true),
                new ListDescriptor(DbJVConstructor.metaClass,
                        new MetaClass[] { DbJVClassModel.metaClass }, true),
                new ListDescriptor(DbJVParameter.metaClass,
                        new MetaClass[] { DbJVClassModel.metaClass }, true),
                descriptor,
                new ListDescriptor(DbJVInheritance.metaClass,
                        new MetaClass[] { DbJVClassModel.metaClass }, false),
                new ListDescriptor(DbJVCompilationUnit.metaClass,
                        new MetaClass[] { DbJVClassModel.metaClass }, true), });

        descriptor = new ListDescriptor(DbJVAssociation.metaClass,
                new MetaClass[] { DbSMSAbstractPackage.metaClass }, columns.toArray());
        put(DbJVPackage.metaClass, new ListDescriptor[] {
                new ListDescriptor(DbJVClass.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbJVDataMember.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                new ListDescriptor(DbJVMethod.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                new ListDescriptor(DbJVConstructor.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                new ListDescriptor(DbJVParameter.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                descriptor,
                new ListDescriptor(DbJVInheritance.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbJVCompilationUnit.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
        put(DbJVClass.metaClass, new ListDescriptor[] {
                new ListDescriptor(DbJVClass.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbJVDataMember.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbJVMethod.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbJVConstructor.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbJVParameter.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }, true), });
        put(DbJVMethod.metaClass, new ListDescriptor[] { new ListDescriptor(
                DbJVParameter.metaClass, new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
        put(DbJVConstructor.metaClass, new ListDescriptor[] { new ListDescriptor(
                DbJVParameter.metaClass, new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
        put(DbJVCompilationUnit.metaClass, new ListDescriptor[] { new ListDescriptor(
                DbJVCompilationUnit.fClasses, DbJVClass.metaClass), });

        // Generic
        // OR Association Front end and back end columns
        frontEndColumn = new ListColumn() {
            public String getTitle() {
                return DbORAssociationEnd.metaClass.getGUIName(false, false) + " 1";
            }

            public Object getValue(DbObject dbo, DbObject neighbor) throws DbException {
                if (neighbor == null)
                    return null;
                DbORAssociationEnd end = ((DbORAssociation) neighbor).getFrontEnd();
                if (end == null)
                    return null;
                DbObject type = end.getClassifier();
                if (type == null)
                    return null;
                String value = "";
                /*
                 * value += type.getSemanticalName(DbObject.SHORT_FORM); if (end.isNavigable()){
                 * DbObject member = end.getMember(); if (member != null) value += " [" +
                 * member.getName() + "]"; //NOT LOCALIZABLE }
                 */
                value += end.getName();

                return value;
            }

            public String getRenderer() {
                return null;
            }

            public String getID() {
                return DbORAssociationEnd.metaClass.getJClass().getName() + "1";
            }

            public int getDefaultWidth() {
                return -1;
            }
        };
        backEndColumn = new ListColumn() {
            public String getTitle() {
                return DbORAssociationEnd.metaClass.getGUIName(false, false) + " 2";
            }

            public Object getValue(DbObject dbo, DbObject neighbor) throws DbException {
                if (neighbor == null)
                    return null;
                DbORAssociationEnd end = ((DbORAssociation) neighbor).getBackEnd();
                if (end == null)
                    return null;
                DbObject type = end.getClassifier();
                if (type == null)
                    return null;
                String value = "";
                // value += type.getSemanticalName(DbObject.SHORT_FORM);
                /*
                 * if (end.isNavigable()){ DbObject member = end.getMember(); if (member != null)
                 * value += " [" + member.getName() + "]"; //NOT LOCALIZABLE }
                 */
                value += end.getName();

                return value;
            }

            public String getRenderer() {
                return null;
            }

            public String getID() {
                return DbJVAssociationEnd.metaClass.getJClass().getName() + "2";
            }

            public int getDefaultWidth() {
                return -1;
            }
        };

        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

        columns = DbORAssociation.metaClass.getScreenMetaFields();
        if (nMode != DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP)
            columns.add(frontEndColumn);
        columns.add(backEndColumn);
        descriptor = new ListDescriptor(DbORAssociation.metaClass,
                new MetaClass[] { DbORDataModel.metaClass }, columns.toArray());

        Icon icon = null;
        ListDescriptor orEnd = new ListDescriptor(DbORAssociationEnd.metaClass,
                new MetaClass[] { DbORDataModel.metaClass }, true);
        if (nMode == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
            descriptor.setIcon(terminologyUtil.getArcIcon());
            if (isModelConverted == true)
                put(DbGEDataModel.metaClass, new ListDescriptor[] {
                        new ListDescriptor(DbGETable.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbGEView.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbGEColumn.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbGEPrimaryUnique.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbGEForeign.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbGECheck.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        descriptor,
                        orEnd,
                        new ListDescriptor(DbGEIndex.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbORIndexKey.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbGETrigger.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbORDataModel.metaClass, true, LocaleMgr.screen
                                .getString("superDataModel")), });
            else
                put(DbGEDataModel.metaClass, new ListDescriptor[] {
                        new ListDescriptor(DbGETable.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbGEColumn.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbGEPrimaryUnique.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbGECheck.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        descriptor,
                        orEnd,
                        new ListDescriptor(DbORDataModel.metaClass, true, LocaleMgr.screen
                                .getString("conceptualDataModel"), terminologyUtil
                                .getConceptualModelIcon()), });

            if (isRelationship == true)
                put(DbGETable.metaClass, new ListDescriptor[] {
                        new ListDescriptor(DbGEColumn.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbGECheck.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                // new ListDescriptor(DbGEPrimaryUnique.metaClass, new
                        // MetaClass[]{DbSMSAbstractPackage.metaClass}),
                        });
            else if (isModelConverted == true)
                put(DbGETable.metaClass, new ListDescriptor[] {
                        new ListDescriptor(DbGEColumn.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbGEPrimaryUnique.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbGEForeign.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbGECheck.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbGEIndex.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbORIndexKey.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbGETrigger.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
            else
                put(DbGETable.metaClass, new ListDescriptor[] {
                        new ListDescriptor(DbGEColumn.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbGECheck.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbGEPrimaryUnique.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
        } else {
            descriptor.setIcon(null);
            put(DbGEDataModel.metaClass, new ListDescriptor[] {
                    new ListDescriptor(DbGETable.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbGEView.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbGEColumn.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbGEPrimaryUnique.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbGEForeign.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbGECheck.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    descriptor,
                    orEnd,
                    new ListDescriptor(DbGEIndex.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbORIndexKey.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbGETrigger.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbORDataModel.metaClass, true, LocaleMgr.screen
                            .getString("superDataModel")), });
            put(DbGETable.metaClass, new ListDescriptor[] {
                    new ListDescriptor(DbGEColumn.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbGEPrimaryUnique.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbGEForeign.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbGECheck.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbGEIndex.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbORIndexKey.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbGETrigger.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
        }
        put(DbGEView.metaClass, new ListDescriptor[] {
                new ListDescriptor(DbGEColumn.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbGEPrimaryUnique.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbGEForeign.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbGECheck.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbGEIndex.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbORIndexKey.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                new ListDescriptor(DbGETrigger.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
        put(DbGEOperationLibrary.metaClass, new ListDescriptor[] {
                new ListDescriptor(DbGEProcedure.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbGEParameter.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }, true), });
        put(DbGEProcedure.metaClass, new ListDescriptor[] { new ListDescriptor(
                DbGEParameter.metaClass, new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
        put(DbGEIndex.metaClass, new ListDescriptor[] { new ListDescriptor(DbORIndexKey.metaClass,
                new MetaClass[] { DbSMSAbstractPackage.metaClass }), });

        // Oracle

        if (nMode == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
            descriptor.setIcon(terminologyUtil.getArcIcon());
            if (isModelConverted == true)
                put(DbORADataModel.metaClass, new ListDescriptor[] {
                        new ListDescriptor(DbORATable.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbORAView.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbORAColumn.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbORAPrimaryUnique.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbORAForeign.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbORACheck.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        descriptor,
                        orEnd,
                        new ListDescriptor(DbORAIndex.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbORIndexKey.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbORATrigger.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbORASequence.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbORANestedTableStorage.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbORALobStorage.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbORAPartition.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbORDataModel.metaClass, true, LocaleMgr.screen
                                .getString("superDataModel")), });
            else
                put(DbORADataModel.metaClass, new ListDescriptor[] {
                        new ListDescriptor(DbORATable.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbORAColumn.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbORAPrimaryUnique.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        descriptor,
                        orEnd,
                        new ListDescriptor(DbORDataModel.metaClass, true, LocaleMgr.screen
                                .getString("conceptualDataModel"), terminologyUtil
                                .getConceptualModelIcon()), });

            if (isRelationship == true)
                put(DbORATable.metaClass, new ListDescriptor[] {
                        new ListDescriptor(DbORAColumn.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbORAPrimaryUnique.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
            else if (isModelConverted == true)
                put(DbORATable.metaClass, new ListDescriptor[] {
                        new ListDescriptor(DbORAColumn.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbORAPrimaryUnique.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
            else
                put(DbORATable.metaClass, new ListDescriptor[] {
                        new ListDescriptor(DbORAColumn.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbORAPrimaryUnique.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbORAForeign.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbORACheck.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbORAIndex.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbORIndexKey.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbORATrigger.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbORANestedTableStorage.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbORALobStorage.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbORAPartition.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }), });

        } else {
            descriptor.setIcon(null);

            put(DbORADataModel.metaClass, new ListDescriptor[] {
                    new ListDescriptor(DbORATable.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbORAView.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbORAColumn.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbORAPrimaryUnique.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbORAForeign.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbORACheck.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    descriptor,
                    orEnd,
                    new ListDescriptor(DbORAIndex.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbORIndexKey.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbORATrigger.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbORASequence.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbORANestedTableStorage.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbORALobStorage.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbORAPartition.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbORDataModel.metaClass, true, LocaleMgr.screen
                            .getString("superDataModel")), });
            put(DbORATable.metaClass, new ListDescriptor[] {
                    new ListDescriptor(DbORAColumn.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbORAPrimaryUnique.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbORAForeign.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbORACheck.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbORAIndex.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbORIndexKey.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbORATrigger.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbORANestedTableStorage.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbORALobStorage.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbORAPartition.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
        }

        put(DbORAView.metaClass, new ListDescriptor[] {
                new ListDescriptor(DbORAColumn.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbORAPrimaryUnique.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbORAForeign.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbORACheck.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbORAIndex.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbORIndexKey.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                new ListDescriptor(DbORATrigger.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
        put(DbORAProgramUnitLibrary.metaClass, new ListDescriptor[] {
                new ListDescriptor(DbORAProcedure.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbORAPackage.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbORAParameter.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }, true), });
        put(DbORAProcedure.metaClass, new ListDescriptor[] { new ListDescriptor(
                DbORAParameter.metaClass, new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
        put(DbORAIndex.metaClass, new ListDescriptor[] { new ListDescriptor(DbORIndexKey.metaClass,
                new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
        put(DbORADatabase.metaClass, new ListDescriptor[] {
                new ListDescriptor(DbORATablespace.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbORADataFile.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbORARedoLogGroup.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbORARedoLogFile.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                new ListDescriptor(DbORARollbackSegment.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
        put(DbORATablespace.metaClass, new ListDescriptor[] { new ListDescriptor(
                DbORADataFile.metaClass, new MetaClass[] { DbSMSAbstractPackage.metaClass }), });

        // Informix

        if (nMode == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
            descriptor.setIcon(terminologyUtil.getArcIcon());
            if (isModelConverted == true)
                put(DbINFDataModel.metaClass, new ListDescriptor[] {
                        new ListDescriptor(DbINFTable.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbINFView.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbINFColumn.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbINFPrimaryUnique.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbINFForeign.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbINFCheck.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        descriptor,
                        orEnd,
                        new ListDescriptor(DbINFIndex.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbORIndexKey.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbINFTrigger.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbORDataModel.metaClass, true, LocaleMgr.screen
                                .getString("superDataModel")), });
            else
                put(DbINFDataModel.metaClass, new ListDescriptor[] {
                        new ListDescriptor(DbINFTable.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbINFColumn.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbINFPrimaryUnique.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        descriptor,
                        orEnd,
                        new ListDescriptor(DbORDataModel.metaClass, true, LocaleMgr.screen
                                .getString("conceptualDataModel"), terminologyUtil
                                .getConceptualModelIcon()), });

            if (isRelationship == true)
                put(DbINFTable.metaClass, new ListDescriptor[] {
                        new ListDescriptor(DbINFColumn.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbINFPrimaryUnique.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
            else if (isModelConverted == true)
                put(DbINFTable.metaClass, new ListDescriptor[] {
                        new ListDescriptor(DbINFColumn.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbINFPrimaryUnique.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
            else
                put(DbINFTable.metaClass, new ListDescriptor[] {
                        new ListDescriptor(DbINFColumn.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbINFPrimaryUnique.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbINFForeign.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbINFCheck.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbINFIndex.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbORIndexKey.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbINFTrigger.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
        } else {
            descriptor.setIcon(null);

            put(DbINFDataModel.metaClass, new ListDescriptor[] {
                    new ListDescriptor(DbINFTable.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbINFView.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbINFColumn.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbINFPrimaryUnique.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbINFForeign.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbINFCheck.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    descriptor,
                    orEnd,
                    new ListDescriptor(DbINFIndex.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbORIndexKey.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbINFTrigger.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbORDataModel.metaClass, true, LocaleMgr.screen
                            .getString("superDataModel")), });
            put(DbINFTable.metaClass, new ListDescriptor[] {
                    new ListDescriptor(DbINFColumn.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbINFPrimaryUnique.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbINFForeign.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbINFCheck.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbINFIndex.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbORIndexKey.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbINFTrigger.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
        }
        put(DbINFView.metaClass, new ListDescriptor[] {
                new ListDescriptor(DbINFColumn.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbINFPrimaryUnique.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbINFForeign.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbINFCheck.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbINFIndex.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbORIndexKey.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                new ListDescriptor(DbINFTrigger.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
        put(DbINFOperationLibrary.metaClass, new ListDescriptor[] {
                new ListDescriptor(DbINFProcedure.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbINFParameter.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }, true), });
        put(DbINFProcedure.metaClass, new ListDescriptor[] { new ListDescriptor(
                DbINFParameter.metaClass, new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
        put(DbINFIndex.metaClass, new ListDescriptor[] { new ListDescriptor(DbORIndexKey.metaClass,
                new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
        put(DbINFDatabase.metaClass, new ListDescriptor[] {
                new ListDescriptor(DbINFDbspace.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbINFSbspace.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }), });

        // IBM

        if (nMode == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
            descriptor.setIcon(terminologyUtil.getArcIcon());
            if (isModelConverted == true)
                put(DbIBMDataModel.metaClass, new ListDescriptor[] {
                        new ListDescriptor(DbIBMTable.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbIBMView.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbIBMColumn.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbIBMPrimaryUnique.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbIBMForeign.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbIBMCheck.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        descriptor,
                        orEnd,
                        new ListDescriptor(DbIBMIndex.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbORIndexKey.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbIBMTrigger.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbORDataModel.metaClass, true, LocaleMgr.screen
                                .getString("superDataModel")), });
            else
                put(DbIBMDataModel.metaClass, new ListDescriptor[] {
                        new ListDescriptor(DbIBMTable.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbIBMColumn.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbIBMPrimaryUnique.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        descriptor,
                        orEnd,
                        new ListDescriptor(DbORDataModel.metaClass, true, LocaleMgr.screen
                                .getString("conceptualDataModel"), terminologyUtil
                                .getConceptualModelIcon()), });

            if (isRelationship == true)
                put(DbIBMTable.metaClass, new ListDescriptor[] {
                        new ListDescriptor(DbIBMColumn.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbIBMPrimaryUnique.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
            else if (isModelConverted == true)
                put(DbIBMTable.metaClass, new ListDescriptor[] {
                        new ListDescriptor(DbIBMColumn.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbIBMPrimaryUnique.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
            else
                put(DbIBMTable.metaClass, new ListDescriptor[] {
                        new ListDescriptor(DbIBMColumn.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbIBMPrimaryUnique.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbIBMForeign.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbIBMCheck.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbIBMIndex.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                        new ListDescriptor(DbORIndexKey.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                        new ListDescriptor(DbIBMTrigger.metaClass,
                                new MetaClass[] { DbSMSAbstractPackage.metaClass }), });

        } else {
            descriptor.setIcon(null);

            put(DbIBMDataModel.metaClass, new ListDescriptor[] {
                    new ListDescriptor(DbIBMTable.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbIBMView.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbIBMColumn.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbIBMPrimaryUnique.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbIBMForeign.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbIBMCheck.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    descriptor,
                    orEnd,
                    new ListDescriptor(DbIBMIndex.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbORIndexKey.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbIBMTrigger.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbORDataModel.metaClass, true, LocaleMgr.screen
                            .getString("superDataModel")), });
            put(DbIBMTable.metaClass, new ListDescriptor[] {
                    new ListDescriptor(DbIBMColumn.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbIBMPrimaryUnique.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbIBMForeign.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbIBMCheck.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbIBMIndex.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                    new ListDescriptor(DbORIndexKey.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                    new ListDescriptor(DbIBMTrigger.metaClass,
                            new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
        }
        put(DbIBMView.metaClass, new ListDescriptor[] {
                new ListDescriptor(DbIBMColumn.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbIBMPrimaryUnique.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbIBMForeign.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbIBMCheck.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbIBMIndex.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbORIndexKey.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }, true),
                new ListDescriptor(DbIBMTrigger.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
        put(DbIBMOperationLibrary.metaClass, new ListDescriptor[] {
                new ListDescriptor(DbIBMProcedure.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }),
                new ListDescriptor(DbIBMParameter.metaClass,
                        new MetaClass[] { DbSMSAbstractPackage.metaClass }, true), });
        put(DbIBMProcedure.metaClass, new ListDescriptor[] { new ListDescriptor(
                DbIBMParameter.metaClass, new MetaClass[] { DbSMSAbstractPackage.metaClass }), });
        put(DbIBMIndex.metaClass, new ListDescriptor[] { new ListDescriptor(DbORIndexKey.metaClass,
                new MetaClass[] { DbSMSAbstractPackage.metaClass }), });

    }
}
