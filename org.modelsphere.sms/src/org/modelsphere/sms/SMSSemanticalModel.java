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

import java.awt.Image;
import java.awt.Point;
import java.awt.dnd.DnDConstants;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JOptionPane;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbGraphicalObjectI;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbRoot;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.DbUDF;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.baseDb.screen.model.ReflectionDescriptionModel;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.graphic.AssociationRoles;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.srtool.AddElement;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.SelectionChangedEvent;
import org.modelsphere.jack.srtool.SelectionListener;
import org.modelsphere.jack.srtool.SemanticalModel;
import org.modelsphere.jack.srtool.explorer.Explorer;
import org.modelsphere.jack.srtool.explorer.ExplorerView;
import org.modelsphere.jack.srtool.graphic.ApplDiagramView;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.list.ListDescriptor;
import org.modelsphere.jack.srtool.list.ListTableModel;
import org.modelsphere.sms.actions.SMSActionsStore;
import org.modelsphere.sms.actions.SendToDiagramAction;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEActorQualifier;
import org.modelsphere.sms.be.db.DbBEContextCell;
import org.modelsphere.sms.be.db.DbBEContextGo;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEFlowQualifier;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBEQualifier;
import org.modelsphere.sms.be.db.DbBEResource;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEStoreQualifier;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.DbBEUseCaseQualifier;
import org.modelsphere.sms.be.db.DbBEUseCaseResource;
import org.modelsphere.sms.be.db.util.BEUtility;
import org.modelsphere.sms.be.notation.ZoneEditor;
import org.modelsphere.sms.db.DbSMSAbstractPackage;
import org.modelsphere.sms.db.DbSMSAssociation;
import org.modelsphere.sms.db.DbSMSAssociationEnd;
import org.modelsphere.sms.db.DbSMSBuiltInTypeNode;
import org.modelsphere.sms.db.DbSMSBuiltInTypePackage;
import org.modelsphere.sms.db.DbSMSClassifier;
import org.modelsphere.sms.db.DbSMSCommonItem;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSInheritance;
import org.modelsphere.sms.db.DbSMSLink;
import org.modelsphere.sms.db.DbSMSLinkModel;
import org.modelsphere.sms.db.DbSMSNotice;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.db.DbSMSParameter;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.db.DbSMSTypeClassifier;
import org.modelsphere.sms.db.DbSMSUmlConstraint;
import org.modelsphere.sms.db.DbSMSUmlExtensibility;
import org.modelsphere.sms.db.DbSMSUserDefinedPackage;
import org.modelsphere.sms.db.util.AnySemObject;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOOAbsPackage;
import org.modelsphere.sms.oo.db.DbOOAbstractMethod;
import org.modelsphere.sms.oo.db.DbOOAssociation;
import org.modelsphere.sms.oo.db.DbOOAssociationEnd;
import org.modelsphere.sms.oo.db.DbOOClass;
import org.modelsphere.sms.oo.db.DbOOClassModel;
import org.modelsphere.sms.oo.db.DbOOConstructor;
import org.modelsphere.sms.oo.db.DbOODataMember;
import org.modelsphere.sms.oo.db.DbOOInheritance;
import org.modelsphere.sms.oo.db.DbOOInitBlock;
import org.modelsphere.sms.oo.db.DbOOMethod;
import org.modelsphere.sms.oo.db.DbOOOperation;
import org.modelsphere.sms.oo.db.DbOOPackage;
import org.modelsphere.sms.oo.db.DbOOParameter;
import org.modelsphere.sms.oo.db.DbOOPrimitiveType;
import org.modelsphere.sms.oo.java.db.DbJVAssociation;
import org.modelsphere.sms.oo.java.db.DbJVAssociationEnd;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVImport;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.oo.java.db.DbJVParameter;
import org.modelsphere.sms.oo.java.db.srtypes.JVClassCategory;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORAssociationGo;
import org.modelsphere.sms.or.db.DbORAttribute;
import org.modelsphere.sms.or.db.DbORBuiltInType;
import org.modelsphere.sms.or.db.DbORCheck;
import org.modelsphere.sms.or.db.DbORChoiceOrSpecialization;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORFKeyColumn;
import org.modelsphere.sms.or.db.DbORField;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbORIndexKey;
import org.modelsphere.sms.or.db.DbORModel;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.db.DbORPackage;
import org.modelsphere.sms.or.db.DbORParameter;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORProcedure;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTrigger;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.db.DbORUserNode;
import org.modelsphere.sms.or.db.DbORView;
import org.modelsphere.sms.or.db.util.AnyORObject;
import org.modelsphere.sms.or.generic.db.DbGEColumn;
import org.modelsphere.sms.or.generic.db.DbGEDataModel;
import org.modelsphere.sms.or.generic.db.DbGETable;
import org.modelsphere.sms.or.generic.db.DbGEView;
import org.modelsphere.sms.or.graphic.ORAssociation;
import org.modelsphere.sms.or.ibm.db.DbIBMContainerClause;
import org.modelsphere.sms.or.ibm.db.DbIBMContainerItem;
import org.modelsphere.sms.or.ibm.db.DbIBMDataModel;
import org.modelsphere.sms.or.ibm.db.DbIBMExceptClause;
import org.modelsphere.sms.or.ibm.db.DbIBMSequence;
import org.modelsphere.sms.or.ibm.db.DbIBMTable;
import org.modelsphere.sms.or.ibm.db.DbIBMView;
import org.modelsphere.sms.or.informix.db.DbINFDataModel;
import org.modelsphere.sms.or.informix.db.DbINFTable;
import org.modelsphere.sms.or.informix.db.DbINFView;
import org.modelsphere.sms.or.oracle.db.DbORADataModel;
import org.modelsphere.sms.or.oracle.db.DbORALobStorage;
import org.modelsphere.sms.or.oracle.db.DbORANestedTableStorage;
import org.modelsphere.sms.or.oracle.db.DbORAPartition;
import org.modelsphere.sms.or.oracle.db.DbORASequence;
import org.modelsphere.sms.or.oracle.db.DbORASubPartition;
import org.modelsphere.sms.or.oracle.db.DbORATable;
import org.modelsphere.sms.or.oracle.db.DbORAView;
import org.modelsphere.sms.screen.SMSScreenPerspective;

public final class SMSSemanticalModel extends SemanticalModel implements SelectionListener {

    public static final String kExternalSuffix = LocaleMgr.misc.getString("ExternalSuffix");
    public static final String kIncompletePaste = LocaleMgr.message.getString("incompletePaste");

    private ListPool listDescriptorsPool = null;

    public SMSSemanticalModel() {
        super();
    }

    public boolean isVisibleOnScreen(DbObject composite, DbObject dbo, Class context)
            throws DbException {
        boolean visible; 
        
        if (context != null && Explorer.class.isAssignableFrom(context)) {
            if (dbo instanceof DbSemanticalObject) {
                if (dbo instanceof DbOODataMember) {
                    DbOOAssociationEnd assocEnd = ((DbOODataMember) dbo).getAssociationEnd();
                    visible = (assocEnd == null || assocEnd.isNavigable());
                } else {
                    visible = !(dbo instanceof DbUDF || dbo instanceof DbSMSTargetSystem || dbo instanceof DbOOAssociation);
                }
            } else {
                visible = (dbo instanceof DbSMSDiagram || dbo instanceof DbORIndexKey || dbo instanceof DbORFKeyColumn)
                        || (dbo instanceof DbBEContextGo)
                        || (dbo instanceof DbBEContextCell)
                        || (dbo instanceof DbIBMContainerClause)
                        || (dbo instanceof DbIBMContainerItem)
                        || (dbo instanceof DbIBMExceptClause);
            }

            //do not show objects explicitly hidden from explorer
            if (visible) {
                if ((dbo.getHideFlags() & DbObject.HIDE_IN_EXPLORER) != 0) {
                    visible = false;
                }
            }
        } else {
            visible = (dbo instanceof DbSemanticalObject);
        } //end if
        
        if (visible) {
            visible = SMSScreenPerspective.isVisible(dbo); 
        }
        
        return visible;
    }

    public Terminology getTerminology() {

        // //
        // since we are not always notified on time by the selection listener
        // (other objects exist in the chain of notification
        // that may attempt to request the terminology), attempt to set the
        // Terminology to ensure we return consistent results

        DbObject dbObject = null;
        DbObject[] dbObjects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        Object[] objects = ApplicationContext.getFocusManager().getSelectedObjects();

        try {
            if (dbObjects != null)
                if (dbObjects.length > 0)
                    dbObject = (DbObject) dbObjects[0];

            if (dbObject != null)
                setTerminology(dbObject);
        } catch (Exception e) {/* do nothing */
        }

        return terminology;
    }

    // Block metafields to be displayed for the specified dbo
    // return true to allow the specified metafield to be displayed
    // Invoked within a read transaction
    // multiSelection indicates if the current DbObject selection has more than
    // one object
    public boolean isVisibleOnScreen(MetaClass metaclass, MetaField metafield, DbObject dbo,
            boolean multiSelection, Class context) throws DbException {
        if (metafield == null)
            return false;
        if (dbo instanceof DbBEUseCase
                && (metafield == DbBEUseCase.fSourceAlphanumericIdentifier || metafield == DbBEUseCase.fSourceNumericIdentifier)
                && !((DbBEUseCase) dbo).isExternal()) {
            return false;
        } else if (metafield == DbBEUseCase.fExternal && multiSelection)
            return false;
        else if ((metafield == DbBEUseCase.fSourceAlphanumericIdentifier || metafield == DbBEUseCase.fSourceNumericIdentifier)
                && multiSelection)
            return false;

        if (!metafield.isVisibleInScreen())
            return false;
        if (metafield == DbSMSSemanticalObject.fPhysicalName) {
            return AnySemObject.supportsPhysicalName(metaclass);
        } else if (metafield == DbSMSSemanticalObject.fSuperCopy) {
            return !multiSelection && AnySemObject.supportsSuper(metaclass);
        } else if (metafield == DbSMSParameter.fPassingConvention) {
            return (metaclass != DbJVParameter.metaClass);
        } else if (metaclass == DbJVAssociationEnd.metaClass) {
            MetaClass fieldClass = metafield.getMetaClass();
            if (fieldClass == DbSemanticalObject.metaClass
                    || fieldClass == DbSMSSemanticalObject.metaClass)
                return false;
        }
        // We must block this one for the design panel only. It may corrupt Db
        // on multiple objects selection.
        else if (multiSelection
                && (metafield == DbORIndex.fConstraint || metafield == DbORPrimaryUnique.fIndex || metafield == DbORForeign.fIndex)) {
            return false;
        }
        // We must block any metarelationship having an opposite max
        // connectivity of 1.
        // It may corrupt Db on multiple objects selection.
        else if (multiSelection && (metafield instanceof MetaRelationship)) {
            MetaRelationship relation = (MetaRelationship) metafield;
            MetaRelationship opposite = relation.getOppositeRel(null);
            return opposite == null ? true : opposite.getMaxCard() > 1;
        } else if (metafield == DbJVClass.fCompilationUnit) {
            return (dbo != null && dbo.getComposite() instanceof DbSMSPackage);
        } else if (metafield == DbORDiagram.fNotation || metafield == DbSMSDiagram.fStyle) {
            return false;
        }
        return true;
    }

    // Return true if the specified dbo's metafield can be edited
    public boolean isEditable(MetaField metafield, DbObject dbo, Class context) throws DbException {
        if (metafield == DbSemanticalObject.fName || metafield == DbSemanticalObject.fPhysicalName) {
            return isNameEditable(dbo, context);
        } else if (metafield == DbJVImport.fAll) {
            return ((DbJVImport) dbo).getImportedObject() instanceof DbJVClass;
        } else if (metafield == DbSMSStereotype.fMetaClassName)
            return !((DbSMSStereotype) dbo).isBuiltIn();
        else if (metafield == DbSMSUmlConstraint.fMetaClassName)
            return !((DbSMSUmlConstraint) dbo).isBuiltIn();
        if (metafield == DbSMSTargetSystem.fVersion)
            return !((DbSMSTargetSystem) dbo).isBuiltIn();
        else if (dbo instanceof DbBEUseCase
                && (metafield == DbBEUseCase.fSourceAlphanumericIdentifier || metafield == DbBEUseCase.fSourceNumericIdentifier)
                && !((DbBEUseCase) dbo).isExternal()) {
            return false;
        }
        if (metafield == DbSMSProject.fIsLocked)
            return false;

        return metafield.isEditable();
    }

    // Return true if the specified dbo's name or physical name can be edited
    public boolean isNameEditable(DbObject dbo, Class context) throws DbException {
        if (dbo instanceof DbSMSStereotype)
            return !((DbSMSStereotype) dbo).isBuiltIn();
        if (dbo instanceof DbSMSUmlConstraint)
            return !((DbSMSUmlConstraint) dbo).isBuiltIn();
        if (dbo instanceof DbORBuiltInType)
            return !((DbORBuiltInType) dbo).isBuiltIn();
        if (dbo instanceof DbSMSTargetSystem)
            return !((DbSMSTargetSystem) dbo).isBuiltIn();
        return !(dbo instanceof DbOOConstructor || dbo instanceof DbORIndexKey
                || dbo instanceof DbORFKeyColumn || dbo instanceof DbSMSBuiltInTypeNode
                || dbo instanceof DbSMSBuiltInTypePackage || dbo instanceof DbOOPrimitiveType);// ||
        // dbo
        // instanceof
        // DbSMSTargetSystem
        // ||
        // dbo
        // instanceof
        // DbORBuiltInType
    }

    public String getDisplayText(DbObject dbo, Class context) throws DbException {
        if (context != null && Explorer.class.isAssignableFrom(context)) {
            String identifier = "[", separator = " - ";
            if (dbo instanceof DbORAssociation) {
                DbORAssociation association = (DbORAssociation) dbo;
                DbORAbsTable frontTable = getAssociatedTable(association.getFrontEnd());
                DbORAbsTable backTable = getAssociatedTable(association.getBackEnd());

                identifier += frontTable.getName() + separator;
                identifier += backTable.getName() + "]";
                String name = association.getName();
                if (name != null)
                    if (name.length() != 0)
                        identifier = name + " : " + identifier;
                return identifier;
            } else if (dbo instanceof DbBEFlow) {
                DbBEFlow flow = (DbBEFlow) dbo;
                DbSMSClassifier class1 = flow.getFirstEnd();
                if (class1 != null)
                    identifier += class1.getName() + separator;
                else
                    identifier += separator;

                DbSMSClassifier class2 = flow.getSecondEnd();
                if (class2 != null)
                    identifier += class2.getName() + "]";
                else
                    identifier += " ]";

                String name = flow.getName();
                if (name != null)
                    if (name.length() != 0)
                        identifier = name + " : " + identifier;
                return identifier;
            } else if (dbo instanceof DbOOAssociation) {
                DbOOAssociation asociation = (DbOOAssociation) dbo;
                identifier += asociation.getFrontEnd().getName() + separator;
                identifier += asociation.getBackEnd().getName() + " ]";
                String name = asociation.getName();
                if (name != null)
                    if (name.length() != 0)
                        identifier = name + " : " + identifier;
                return identifier;
            } else if (dbo instanceof DbOOInheritance) {
                DbOOInheritance inheritance = (DbOOInheritance) dbo;
                identifier += inheritance.getSubClass().getName() + " -> ";
                identifier += inheritance.getSuperClass().getName() + " ]";
                String name = inheritance.getName();
                if (name != null)
                    if (name.length() != 0)
                        identifier = name + " : " + identifier;
                return identifier;
            }
        }
        return super.getDisplayText(dbo, context);
    }

    private DbORAbsTable getAssociatedTable(DbORAssociationEnd end) throws DbException {
        DbORAbsTable associated = end.getClassifier();

        if (associated instanceof DbORChoiceOrSpecialization) {
            DbORChoiceOrSpecialization choiceSpec = (DbORChoiceOrSpecialization) associated;
            associated = choiceSpec.getParentTable();
        }

        return associated;
    }

    public String getDisplayText(MetaClass metaclass, MetaField metafield, Class context) {
        if (metafield == DbSMSSemanticalObject.fSuperCopy) {
            Class thisClass = metaclass.getJClass();
            if (DbORTable.class.isAssignableFrom(thisClass)) {
                if (metaclass == DbGEView.metaClass)
                    return super.getDisplayText(DbGEView.metaClass, DbGEView.fSuperCopy, context);
                else if (metaclass == DbORAView.metaClass)
                    return super.getDisplayText(DbORAView.metaClass, DbORAView.fSuperCopy, context);
                else if (metaclass == DbINFView.metaClass)
                    return super.getDisplayText(DbINFView.metaClass, DbINFView.fSuperCopy, context);
                else if (metaclass == DbIBMView.metaClass)
                    return super.getDisplayText(DbIBMView.metaClass, DbIBMView.fSuperCopy, context);
                else
                    return super.getDisplayText(DbORTable.metaClass, DbORTable.fSuperCopy, context);
            }
            if (DbORColumn.class.isAssignableFrom(thisClass))
                return super.getDisplayText(DbORColumn.metaClass, DbORColumn.fSuperCopy, context);
            if (DbORIndex.class.isAssignableFrom(thisClass))
                return super.getDisplayText(DbORIndex.metaClass, DbORIndex.fSuperCopy, context);
            if (DbORTrigger.class.isAssignableFrom(thisClass))
                return super.getDisplayText(DbORTrigger.metaClass, DbORTrigger.fSuperCopy, context);
            if (DbORAssociation.class.isAssignableFrom(thisClass))
                return super.getDisplayText(DbORAssociation.metaClass, DbORAssociation.fSuperCopy,
                        context);
            if (DbBEFlow.class.isAssignableFrom(thisClass))
                return super.getDisplayText(DbBEFlow.metaClass, DbBEFlow.fSuperCopy, context);
        }

        if ((metafield == DbObject.fComposite)
                && (context != null && ReflectionDescriptionModel.class.isAssignableFrom(context))) {
            if ((metaclass == DbBEUseCaseResource.metaClass)
                    || (metaclass == DbBEUseCaseQualifier.metaClass))
                return LocaleMgr.screen.getString("useCaseHierIdentifier");
            if ((metaclass == DbBEActorQualifier.metaClass)
                    || (metaclass == DbBEStoreQualifier.metaClass))
                return super.getDisplayText(metaclass, DbSemanticalObject.fName, context);
            if (metaclass == DbBEFlowQualifier.metaClass)
                return super.getDisplayText(metaclass, DbBEFlow.fIdentifier, context);
        }

        if ((metaclass == DbBEUseCaseResource.metaClass) && (metafield == DbObject.fComposite)
                && (context != null && ReflectionDescriptionModel.class.isAssignableFrom(context))) {
            return LocaleMgr.screen.getString("useCaseHierIdentifier");
        }

        if (metafield == DbBEUseCaseResource.fResource
                && (context != null && ZoneEditor.class.isAssignableFrom(context))) {
            return getName(DbBEResource.metaClass, true);
        }

        if (metafield == null && metaclass != null
                && (context != null && ListTableModel.class.isAssignableFrom(context))) {
            if (metaclass != DbSemanticalObject.metaClass
                    && metaclass != DbSMSSemanticalObject.metaClass
                    && metaclass != DbObject.metaClass
                    && metaclass != DbSMSAbstractPackage.metaClass
                    && metaclass != DbOOAbsPackage.metaClass) {
                return super.getDisplayText(metaclass, metafield, context);
            }
            return LocaleMgr.screen.getString("composite");
        }

        return super.getDisplayText(metaclass, metafield, context);
    }

    // Display Text For a MetaField depending on a specific dbo context
    // No special cases at this time so the value returned is the same as using
    // getDisplayText(metaclass, metafield, callingObject)
    public final String getDisplayText(MetaClass metaclass, MetaField metafield, DbObject dbo,
            Class context) throws DbException {

        if (dbo instanceof DbORTable) {
            DbORTable table = (DbORTable) dbo;
            try {
                table.getDb().beginReadTrans();
                if (table.isIsAssociation() == true
                        && (metafield == DbORTable.fSuperCopy || metafield == DbGETable.fSuperCopy
                                || metafield == DbORATable.fSuperCopy
                                || metafield == DbINFTable.fSuperCopy
                                || metafield == DbIBMTable.fSuperCopy || metafield == null)) {
                    table.getDb().commitTrans();
                    return super.getDisplayText(DbOOClass.metaClass, metafield, context);
                } else {
                    table.getDb().commitTrans();
                    return super.getDisplayText(metaclass, metafield, context);
                }
            } catch (DbException ex) {
                org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(null, ex);
            }
        }

        return getDisplayText(metaclass, metafield, context);
    }

    public final String getDisplayText(MetaClass metaclass, MetaField metafield, DbObject dbo,
            Class context, boolean bIsGroup) throws DbException {

        if (dbo instanceof DbORTable) {
            DbORTable table = (DbORTable) dbo;
            try {
                table.getDb().beginReadTrans();
                if (table.isIsAssociation() == true
                        && (metafield == DbORTable.fSuperCopy || metafield == DbGETable.fSuperCopy
                                || metafield == DbORATable.fSuperCopy
                                || metafield == DbINFTable.fSuperCopy
                                || metafield == DbIBMTable.fSuperCopy || metafield == null)) {
                    table.getDb().commitTrans();
                    return super.getDisplayText(DbOOClass.metaClass, metafield, context, bIsGroup);
                } else {
                    table.getDb().commitTrans();
                    return getDisplayText(metaclass, metafield, context, bIsGroup);
                }
            } catch (DbException ex) {
                org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(null, ex);
            }
        }

        return super.getDisplayText(metaclass, metafield, context, bIsGroup);
    }

    public String getDisplayText(DbObject dbo, int form, DbObject root, Object target)
            throws DbException {
        if (dbo == null)
            return "";

        BEUtility util = BEUtility.getSingleInstance();

        if (dbo instanceof DbBEUseCase) {
            DbBEUseCase usecase = (DbBEUseCase) dbo;
            String displayText = util.getDisplayText(usecase);
            return displayText;
        }

        if (dbo instanceof DbBEStore) {
            DbBEStore store = (DbBEStore) dbo;
            String displayText = util.getDisplayText(store);
            return displayText;
        }

        if (dbo instanceof DbBEActor) {
            DbBEActor actor = (DbBEActor) dbo;
            String displayText = util.getDisplayText(actor);
            return displayText;
        }

        if (dbo instanceof DbBEFlow) {
            DbBEFlow flow = (DbBEFlow) dbo;
            String displayText = util.getDisplayText(flow);
            return displayText;
        }

        if (dbo instanceof DbBEContextGo) {
            DbBEContextGo frame = (DbBEContextGo) dbo;
            String displayText = util.getDisplayText(frame);
            return displayText;
        }

        if (dbo instanceof DbBEContextCell) {
            DbBEContextCell cell = (DbBEContextCell) dbo;
            String displayText = util.getDisplayText(cell);
            return displayText;
        }

        // OR concepts
        if (!(target instanceof Explorer)) {
            if (dbo instanceof DbORDomain) {
                DbORDomain domain = (DbORDomain) dbo;
                DbORDomainModel domainModel = (DbORDomainModel) domain
                        .getCompositeOfType(DbORDomainModel.metaClass);
                DbSMSTargetSystem ts = domainModel.getTargetSystem();
                String tsName = (ts == null) ? "" : ts.getName();
                String domainName = domain.getName() + " (" + domainModel.getName() + " <" + tsName
                        + ">)"; // NOT
                // LOCALIZABLE
                return domainName;
            }
        }

        if (target instanceof Explorer) {
            if (dbo instanceof DbORModel) {
                String name = dbo.getName();
                DbSMSTargetSystem dbts = ((DbORModel) dbo).getTargetSystem();
                if (dbts != null) {
                    String tsName = dbts.getName();
                    String version = dbts.getVersion();
                    name = name + " <" + tsName + " " + version + ">"; // NOT
                    // LOCALIZABLE
                }
                DbORDatabase deploymentDatabase = null;
                if (dbo instanceof DbORDataModel)
                    deploymentDatabase = ((DbORDataModel) dbo).getDeploymentDatabase();
                else if (dbo instanceof DbOROperationLibrary)
                    deploymentDatabase = ((DbOROperationLibrary) dbo).getDeploymentDatabase();
                else if (dbo instanceof DbORDomainModel)
                    deploymentDatabase = ((DbORDomainModel) dbo).getDeploymentDatabase();
                if (deploymentDatabase != null) {
                    name = "(" + deploymentDatabase.getName() + ") " + name; // NOT
                    // LOCALIZABLE
                }
                return name;
            } else if (dbo instanceof DbORParameter) {
                DbSMSTypeClassifier type = ((DbORParameter) dbo).getType();
                return (type == null ? dbo.getName() : dbo.getName() + " <" + type.getName() + ">"); // NOT LOCALIZABLE
            } else if (dbo instanceof DbORAttribute) {
                DbSMSTypeClassifier type = ((DbORAttribute) dbo).getType();
                return (type == null ? dbo.getName() : dbo.getName() + " <" + type.getName() + ">"); // NOT LOCALIZABLE
            } else if (dbo instanceof DbORCommonItem) {
                DbSMSTypeClassifier type = ((DbORCommonItem) dbo).getType();
                return (type == null ? dbo.getName() : dbo.getName() + " <" + type.getName() + ">"); // NOT LOCALIZABLE
            } else if (dbo instanceof DbOODataMember) {
                DbSMSTypeClassifier type = ((DbOODataMember) dbo).getType();
                return (type == null ? dbo.getName() : dbo.getName() + " <" + type.getName() + ">"); // NOT LOCALIZABLE
            } else if (dbo instanceof DbOOParameter) {
                DbSMSTypeClassifier type = ((DbOOParameter) dbo).getType();
                return (type == null ? dbo.getName() : dbo.getName() + " <" + type.getName() + ">"); // NOT LOCALIZABLE
            } else if (dbo instanceof DbORDomain) {
                DbSMSTypeClassifier type = ((DbORDomain) dbo).getSourceType();
                return (type == null ? dbo.getName() : dbo.getName() + " <" + type.getName() + ">"); // NOT LOCALIZABLE
            } else if (dbo instanceof DbORProcedure)
                return ((DbORProcedure) dbo).buildSignature();
            else if (dbo instanceof DbOOAbstractMethod)
                return ((DbOOAbstractMethod) dbo).buildSignature(DbObject.SHORT_FORM);
            else
                return dbo.getName();
        } // end target is explorer

        if (dbo instanceof DbORUser || dbo instanceof DbSMSStereotype
                || dbo instanceof DbSMSUmlConstraint) {
            return dbo.getName();
        }

        if (dbo instanceof DbSMSTargetSystem) {
            DbSMSTargetSystem dbts = ((DbSMSTargetSystem) dbo);
            return dbts.getName() + " " + dbts.getVersion(); // NOT LOCALIZABLE
        }

        if (form == NAME_CUSTOM_FORM) {
            String name = dbo.getName();
            while (!(dbo instanceof DbSMSClassifier) && !(dbo instanceof DbRoot) && (dbo != null)
                    && !(dbo instanceof DbProject) && (dbo != root)) {
                dbo = dbo.getComposite();
                if (!(dbo instanceof DbSMSClassifier) && !(dbo instanceof DbRoot) && (dbo != null)
                        && !(dbo instanceof DbProject) && (dbo != root))
                    name = dbo.getName() + '.' + name;
            }
            return name;
        }

        return dbo.getSemanticalName(form);
    }

    public final String getClassDisplayText(DbObject dbo, Class context) throws DbException {
        if (dbo instanceof DbJVClass) {
            switch (((DbJVClass) dbo).getStereotype().getValue()) {
            case JVClassCategory.CLASS:
                return LocaleMgr.misc.getString("Class");
            case JVClassCategory.INTERFACE:
                return LocaleMgr.misc.getString("Interface");
            case JVClassCategory.EXCEPTION:
                return LocaleMgr.misc.getString("Exception");
            }
        }

        MetaClass mc = (dbo == null) ? null : dbo.getMetaClass();
        String name = (mc == null) ? "?" : getName(mc);
        return name;
    }

    // BEWARE: compare <metaClass> with final classes.
    // If this list becomes long, it is better to add a new UDF in the meta <GUI
    // name for UDF>
    public final String getDisplayTextForUDF(MetaClass metaClass) {
        if (metaClass == DbORAssociation.metaClass)
            return LocaleMgr.misc.getString("tableAssociationOrArc"); // tableAssociationOrArc
        if (metaClass == DbGETable.metaClass)
            return LocaleMgr.misc.getString("tableOrEntityOrRelationship"); // tableOrEntityOrRelationship
        if (metaClass == DbGEColumn.metaClass)
            return LocaleMgr.misc.getString("columnOrAttribute"); // columnOrAttribute
        if (metaClass == DbJVAssociation.metaClass)
            return LocaleMgr.misc.getString("classAssociation");
        if (metaClass == DbORField.metaClass)
            return LocaleMgr.misc.getString("domainField");
        if (metaClass == DbJVDataMember.metaClass)
            return LocaleMgr.misc.getString("classField");
        if (metaClass == DbJVPackage.metaClass)
            return LocaleMgr.misc.getString("javaPackage");
        if (metaClass == DbJVMethod.metaClass)
            return LocaleMgr.misc.getString("classMethod");
        if (metaClass == DbJVParameter.metaClass)
            return LocaleMgr.misc.getString("classMethodParameter");
        return metaClass.getGUIName(false, true);
    }

    // Special case workaround for stereotype (used by design panel only)
    public Object getDisplayValue(Object value, MetaField field, Class context) {
        if (field == DbSMSStereotype.fMetaClassName) {
            MetaClass metaclass = MetaClass.find((String) value);
            if (metaclass != null) {
                return getName(metaclass);
            }
        }
        return super.getDisplayValue(value, field, context);
    }

    public Image getImage(DbObject dbo, Class context) throws DbException {
        if (dbo instanceof DbSMSStereotype)
            return ((DbSMSStereotype) dbo).getIcon();
        return null;
    }

    public void selectionChanged(SelectionChangedEvent e) throws DbException {

        if (e == null)
            return;

        if (e.selSemObjs == null)
            return;
        if (e.selSemObjs.length <= 0)
            return;

        setTerminology(e.selSemObjs[0]);
    }

    private void setTerminology(DbObject firstSelectedObject) throws DbException {

        if (firstSelectedObject == null)
            return;

        DbObject composite = firstSelectedObject.getCompositeOfType(DbORDataModel.metaClass);
        if (composite instanceof DbORDataModel || firstSelectedObject instanceof DbORDataModel) {
            DbORDataModel dataModel = (DbORDataModel) (composite == null ? firstSelectedObject
                    : composite);
            TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
            terminology = terminologyUtil.findModelTerminology(dataModel);
            return;
        }

        composite = firstSelectedObject.getCompositeOfType(DbORModel.metaClass);
        if (composite instanceof DbORModel || firstSelectedObject instanceof DbORModel) {

            // get the project's OR default notation
            DbSMSProject project = (DbSMSProject) (composite == null ? firstSelectedObject
                    .getCompositeOfType(DbSMSProject.metaClass) : composite
                    .getCompositeOfType(DbSMSProject.metaClass));
            terminology = Terminology.findTerminology(project.getOrDefaultNotation());
            return;
        }

        composite = firstSelectedObject.getCompositeOfType(DbBEModel.metaClass);
        if (composite instanceof DbBEModel || firstSelectedObject instanceof DbBEModel) {
            if (firstSelectedObject instanceof DbBEUseCase) {
                TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
                terminology = terminologyUtil.findModelTerminology(firstSelectedObject);
            } else {
                DbBEModel beModel = (DbBEModel) (composite == null ? firstSelectedObject
                        : composite);
                TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
                terminology = terminologyUtil.findModelTerminology(beModel);
            }
            return;
        }

        composite = firstSelectedObject.getCompositeOfType(DbBEUseCase.metaClass);
        if (composite instanceof DbBEUseCase || firstSelectedObject instanceof DbBEUseCase) {
            DbBEUseCase useCase = (DbBEUseCase) (composite == null ? firstSelectedObject
                    : composite);
            TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
            terminology = terminologyUtil.findModelTerminology(useCase);
            return;
        }

        composite = firstSelectedObject.getCompositeOfType(DbOOClassModel.metaClass);
        if (composite instanceof DbOOClassModel || firstSelectedObject instanceof DbOOClassModel) {

            // thre is no notation for this module
            terminology = null;
        }

        terminology = null;
    }

    public ListDescriptor[] getListDescriptors(DbObject composite) throws DbException {

        if (listDescriptorsPool == null)
            listDescriptorsPool = new ListPool(0, false, false);

        if (composite == null)
            return null;

        ApplicationContext.getFocusManager().addSelectionListener(this);

        MetaClass compositeMetaClass = composite.getMetaClass();
        DefaultMainFrame mainFrame = ApplicationContext.getDefaultMainFrame();
        ListDescriptor[] listDesc = null;

        boolean bIsRelationShip = false;
        DbORDataModel compositeDM = null;
        composite.getDb().beginReadTrans();
        DbObject obj = composite.getCompositeOfType(DbORDataModel.metaClass);

        if (composite instanceof DbORTable) {
            bIsRelationShip = ((DbORTable) composite).isIsAssociation();
        }

        composite.getDb().commitTrans();

        if (composite instanceof DbORDataModel) {

            listDesc = (ListDescriptor[]) listDescriptorsPool.get(compositeMetaClass);

            DbORDataModel dataModel = (DbORDataModel) composite;
            TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

            dataModel.getDb().beginReadTrans();
            boolean bShowPhysicalConcepts = TerminologyUtil
                    .getShowPhysicalConcepts(new DbObject[] { dataModel });

            int nMode = dataModel.getOperationalMode();
            listDescriptorsPool = new ListPool(nMode, false, bShowPhysicalConcepts);

            listDesc = (ListDescriptor[]) listDescriptorsPool.get(compositeMetaClass);
            terminology = terminologyUtil.findModelTerminology(dataModel);
            dataModel.getDb().commitTrans();

            // //
            // the next block of code below adds or remove a relationship item
            // to the list menu to support the ER mode

            if (listDesc.length > 1) // there can be a relationship item
            {
                ListDescriptor ld = listDesc[1];
                ListDescriptor ldEntity = listDesc[0];

                if (nMode == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) { // if
                    // we
                    // are
                    // in
                    // ER
                    // mode
                    if (ld.getIcon() != SMSExplorer.associationTableIcon) { // if
                        // the
                        // item
                        // is
                        // not
                        // present,
                        // add
                        // it

                        // add the relationships item //otherwise do nothing
                        ListDescriptor newDescriptor = new ListDescriptor(ldEntity, new Boolean(
                                true));
                        String sDisplayText = terminology.getTerm(DbOOClass.metaClass, true);
                        newDescriptor.setDisplayText(sDisplayText);
                        newDescriptor.setIcon(SMSExplorer.associationTableIcon);

                        // resize the list descriptor
                        int newSize = listDesc.length + 1;
                        ListDescriptor[] newArray = new ListDescriptor[newSize];

                        // copy the remaining elements
                        newArray[0] = listDesc[0];
                        newArray[1] = newDescriptor;
                        for (int i = 1; i < listDesc.length; i++)
                            newArray[i + 1] = listDesc[i];

                        listDesc = (ListDescriptor[]) newArray;

                        listDesc[0] = new ListDescriptor(listDesc[0], new Boolean(false));
                    }
                }
            }
        } else if (obj != null) {
            compositeDM = (DbORDataModel) obj;
            compositeDM.getDb().beginReadTrans();
            boolean bShowPhysicalConcepts = TerminologyUtil
                    .getShowPhysicalConcepts(new DbObject[] { obj });
            int nMode = compositeDM.getOperationalMode();

            listDescriptorsPool = new ListPool(nMode, bIsRelationShip, bShowPhysicalConcepts);
            compositeDM.getDb().commitTrans();

            listDesc = (ListDescriptor[]) listDescriptorsPool.get(compositeMetaClass);
        } else {/*
                 * obtain the default project notation for the corresponding kind of model
                 */

            listDesc = (ListDescriptor[]) listDescriptorsPool.get(compositeMetaClass);

            if (composite instanceof DbORModel) {

                // get the project's OR default notation
                composite.getDb().beginReadTrans();
                DbSMSProject project = (DbSMSProject) composite
                        .getCompositeOfType(DbSMSProject.metaClass);
                {
                    project.getDb().beginReadTrans();
                    terminology = Terminology.findTerminology(project.getOrDefaultNotation());
                    project.getDb().commitTrans();
                }
                composite.getDb().commitTrans();
            }

            else if (composite instanceof DbBEModel || composite instanceof DbBEUseCase) {

                TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
                terminology = terminologyUtil.findModelTerminology((DbObject) composite);
                listDescriptorsPool = new ListPool(terminology);
                listDesc = (ListDescriptor[]) listDescriptorsPool.get(compositeMetaClass);

                // get the project's BE default notation
                composite.getDb().beginReadTrans();
                DbSMSProject project = (DbSMSProject) composite.getProject();
                {
                    project.getDb().beginReadTrans();
                    terminology = Terminology.findTerminology(project.getBeDefaultNotation());
                    project.getDb().commitTrans();
                }
                composite.getDb().commitTrans();
            } else if (composite instanceof DbOOClassModel) {

                // thre is no notation for this module
                terminology = null;
            }
        }

        return listDesc;
    }

    /*
     * Check that the selection is valid for the copy operation, i.e. that all selected items belong
     * to the same project, and that no item is a descendant of another item of the selection.
     */
    public final boolean isValidForCopy(DbObject[] dbos) throws DbException {
        if (dbos.length == 0)
            return false;
        HashSet<DbObject> dboSet = new HashSet<DbObject>();
        DbProject project = dbos[0].getProject();
        int i;
        for (i = 0; i < dbos.length; i++) {
            DbObject dbo = dbos[i];
            if (dbo instanceof DbSemanticalObject) {
                if (dbo instanceof DbSMSStereotype && ((DbSMSStereotype) dbo).isBuiltIn())
                    return false;
                if (dbo instanceof DbSMSUmlConstraint && ((DbSMSUmlConstraint) dbo).isBuiltIn())
                    return false;

                if (dbo instanceof DbProject || dbo instanceof DbSMSBuiltInTypeNode
                        || dbo instanceof DbSMSBuiltInTypePackage || dbo instanceof DbORBuiltInType
                        || dbo instanceof DbOOPrimitiveType || dbo instanceof DbSMSTargetSystem
                        || dbo instanceof DbORUserNode || dbo instanceof DbSMSUmlExtensibility)
                    return false;
            } else if (!(dbo instanceof DbSMSDiagram))
                return false;
            if (project != dbo.getProject())
                return false;
            dboSet.add(dbo);
        }
        for (i = 0; i < dbos.length; i++) {
            DbObject parent = dbos[i];
            while (parent != project) {
                parent = parent.getComposite();
                if (dboSet.contains(parent)) /*
                                              * bad if one of its ancestor is in the selection
                                              */
                    return false;
            }
        }
        return true;
    }

    public final boolean isValidForDrag(DbObject[] dbos) throws DbException {
        if (dbos.length == 0)
            return false;
        Db db = dbos[0].getDb();
        db.beginTrans(Db.READ_TRANS);
        DbObject parent = dbos[0].getComposite();
        int i;
        for (i = 0; i < dbos.length; i++) {
            DbObject dbo = dbos[i];
            if (!(dbo instanceof DbSMSUserDefinedPackage || dbo instanceof DbORModel
                    || dbo instanceof DbORDataModel || dbo instanceof DbORCommonItemModel
                    || dbo instanceof DbSMSLinkModel || dbo instanceof DbSMSLink
                    || dbo instanceof DbOOAbsPackage || dbo instanceof DbJVClass
                    || dbo instanceof DbBEModel || dbo instanceof DbSMSNotice
                    || dbo instanceof DbORTable || dbo instanceof DbORPackage
                    || dbo instanceof DbORDomain || dbo instanceof DbORField
                    || dbo instanceof DbORCommonItem || dbo instanceof DbORAssociation
                    || dbo instanceof DbBEFlow || dbo instanceof DbBEUseCase
                    || dbo instanceof DbBEActor || dbo instanceof DbBEStore
                    || dbo instanceof DbORView || dbo instanceof DbORColumn
                    || dbo instanceof DbORProcedure || dbo instanceof DbORASequence
                    || dbo instanceof DbIBMSequence || dbo instanceof DbBEResource
                    || dbo instanceof DbBEQualifier || dbo instanceof DbORCheck
                    || dbo instanceof DbORProcedure || dbo instanceof DbORParameter
                    || dbo instanceof DbORTrigger || dbo instanceof DbORPrimaryUnique
                    || dbo instanceof DbORIndex || dbo instanceof DbORANestedTableStorage
                    || dbo instanceof DbORAPartition || dbo instanceof DbORALobStorage
                    || dbo instanceof DbORASubPartition || dbo instanceof DbOOClass
                    || dbo instanceof DbOOInitBlock || dbo instanceof DbOOOperation
                    || dbo instanceof DbOODataMember || dbo instanceof DbOOMethod))
                break;

            DbObject comp = dbo.getComposite();
            if (dbo.getDb() != db || comp != parent)
                break;
        }
        db.commitTrans();
        return (i == dbos.length);
    }

    public final int getDefaultDropAction(DbObject[] dragObjs, DbObject dropObj) throws DbException {
        return DnDConstants.ACTION_MOVE;
    }

    private boolean isParentOf(DbObject candidateParentObject, DbObject candidateChildObject)
            throws DbException {
        candidateChildObject.getDb().beginReadTrans();
        DbObject composite = candidateChildObject.getComposite();
        candidateChildObject.getDb().commitTrans();

        if (composite == null)
            return false;
        if (composite.equals(candidateParentObject))
            return true;
        else
            return isParentOf(candidateParentObject, composite);

    }

    // Assume isValidForDrop
    public final boolean isValidForDrop(DbObject[] dbos, DbObject[] composites) throws DbException {
        if (dbos.length == 0 || composites.length == 0)
            return false;

        DbObject targetObject = composites[0];

        for (int idx = 0; idx < dbos.length; idx++)
            if (targetObject.equals(dbos[idx]))
                return false;

        for (int idx = 0; idx < dbos.length; idx++)
            if (!targetObject.getProject().equals(dbos[idx].getProject()))
                return false;

        targetObject.getDb().beginReadTrans();
        DbObject objComposite = targetObject.getComposite();
        targetObject.getDb().commitTrans();
        if (targetObject instanceof DbORColumn || targetObject instanceof DbOODataMember)
            if (ApplicationContext.getFocusManager().getFocusObject() instanceof ExplorerView)
                return false;

        if (targetObject instanceof DbORTable
                || objComposite instanceof DbORTable // column
                || objComposite instanceof DbORView // columns for view
                || targetObject instanceof DbORView
                || objComposite instanceof DbOOClass // data member
                || targetObject instanceof DbOOClass || targetObject instanceof DbBEUseCase
                || targetObject instanceof DbBEActor || targetObject instanceof DbBEStore
                || targetObject instanceof DbBEFlow || targetObject instanceof DbORDomainModel
                || targetObject instanceof DbORDomain || targetObject instanceof DbORProcedure
                || targetObject instanceof DbOROperationLibrary
                || targetObject instanceof DbORCommonItemModel
                || targetObject instanceof DbORAbsTable || targetObject instanceof DbORAPartition
                || targetObject instanceof DbORASubPartition || targetObject instanceof DbOOClass
                || targetObject instanceof DbOOPackage
                || targetObject instanceof DbSMSUserDefinedPackage
                || targetObject instanceof DbSMSLinkModel || targetObject instanceof DbORDataModel
                || targetObject instanceof DbBEModel || targetObject instanceof DbOOClassModel
                || targetObject instanceof DbSMSProject) {

            if (dbos[0] instanceof DbORCommonItem) {
                if (!(isCreateCommonItems(dbos, composites[0])))
                    return isValidCopyOperation(dbos, composites[0]);
                else
                    return true;
            }
            return isValidCopyOperation(dbos, composites[0]);

        }
        if (targetObject instanceof DbSMSDiagram) {

            for (int i = 0; i < dbos.length; i++)
                if (dbos[i] instanceof DbORASequence)
                    return false;
                else if (dbos[i] instanceof DbIBMSequence)
                    return false;
                else if (dbos[i] instanceof DbBEResource)
                    return false;
                else if (dbos[i] instanceof DbBEQualifier)
                    return false;

            // //
            // the diagram must be part of the same model for all dragged
            // objects
            // 1) find the top level model node for the diagram object (if
            // ORModel, otherwise, immediate parent)

            DbProject project = targetObject.getProject();
            targetObject.getDb().beginReadTrans();
            DbObject modelComp = targetObject.getComposite();
            targetObject.getDb().commitTrans();

            DbObject previousModel = modelComp;
            if (previousModel instanceof DbJVClassModel || previousModel instanceof DbJVPackage
                    || previousModel instanceof DbBEUseCase) {
                while (!modelComp.equals(project)) {
                    modelComp.getDb().beginReadTrans();
                    DbObject dboComp = modelComp.getComposite();
                    modelComp.getDb().commitTrans();
                    previousModel = modelComp;
                    modelComp = dboComp;
                }
            }

            // //
            // 2) find the top level model node for the dragged objects

            for (int i = 0; i < dbos.length; i++) {
                dbos[i].getDb().beginReadTrans();
                DbObject objectComp = dbos[i].getComposite();
                dbos[i].getDb().commitTrans();

                DbObject previousObjectComp = objectComp;

                if (previousObjectComp instanceof DbJVClassModel // IMPORTANT,
                        // MetaClasses
                        // listed
                        // here
                        // define
                        || previousObjectComp instanceof DbJVPackage // the
                        // search
                        // boundary
                        // and
                        // blocks
                        // if
                        // one
                        // of
                        // the
                        // kind
                        || previousObjectComp instanceof DbBEUseCase // is
                // encountered
                // to
                // prevent
                // searching
                // beyond
                // the
                ) { // container class level (say to restrict the drag'n'drop
                    while (!objectComp.equals(project)) { // a submodel or
                        // package
                        objectComp.getDb().beginReadTrans();
                        DbObject dboComp = objectComp.getComposite();
                        objectComp.getDb().commitTrans();
                        previousObjectComp = objectComp;
                        objectComp = dboComp;
                    }
                }
                if (!previousObjectComp.equals(previousModel))
                    return false;
            }
            return true;
        }
        return false;
    }

    public boolean isLinkQualifierOrResource(DbObject[] dragObjs, DbObject dropObj) {

        // //
        // all objects must be of type qualifier or resource

        for (int i = 0; i < dragObjs.length; i++) {

            if (!(dragObjs[i] instanceof DbBEResource || dragObjs[i] instanceof DbBEQualifier))
                return false;
        }
        return true;

    }

    public boolean isCreateCommonItems(DbObject[] dragObjs, DbObject dropObj) {

        // //
        // source must be all commonitems, destination all tables

        if (dragObjs == null || dropObj == null)
            return false;

        if (dragObjs.length == 0)
            return false;

        for (int i = 0; i < dragObjs.length; i++)
            if (!(dragObjs[i] instanceof DbORCommonItem))
                return false;

        if (!(dropObj instanceof DbORTable || dropObj instanceof DbORColumn
                || dropObj instanceof DbORView || dropObj instanceof DbOOClass || dropObj instanceof DbOODataMember))
            return false;

        return true;

    }

    public boolean isValidCopyOperation(DbObject[] dragObjs, DbObject dropObj) throws DbException {

        // //
        // 

        DbObject previous = null;
        dragObjs[0].getDb().beginReadTrans();
        DbObject dragComposite = previous = dragObjs[0].getComposite();
        dragObjs[0].getDb().commitTrans();

        if (dropObj.equals(dragComposite))
            return false;

        if (dragObjs[0] instanceof DbSMSPackage || dragObjs[0] instanceof DbBEUseCase) { // check for drag is
            // parent of drop
            // container and
            // deny if found

            dragObjs[0].getDb().beginReadTrans();
            dragComposite = dragObjs[0].getComposite();
            dragObjs[0].getDb().commitTrans();

            dropObj.getDb().beginReadTrans();
            DbObject dropComposite = dropObj.getComposite();
            dropObj.getDb().commitTrans();

            if (isParentOf(dragObjs[0], dropObj))
                return false;
        }
        // process modeling containers and contained objects
        if (dropObj instanceof DbBEUseCase) {
            for (int i = 0; i < dragObjs.length; i++) {
                DbBEUseCase usecase = ((DbBEUseCase) dropObj);
                usecase.getDb().beginReadTrans();
                boolean ext = usecase.isExternal();
                usecase.getDb().commitTrans();
                if (ext)
                    return false;
                if (dragObjs[i] instanceof DbBEUseCase || dragObjs[i] instanceof DbBEFlow)
                    continue;
                else if (dragObjs[i] instanceof DbBEQualifier
                        || dragObjs[i] instanceof DbBEResource) {
                    dropObj.getDb().beginReadTrans();
                    DbObject dropComposite = dropObj.getCompositeOfType(DbBEModel.metaClass);
                    dropObj.getDb().commitTrans();

                    dragObjs[i].getDb().beginReadTrans();
                    DbObject dragComp = dragObjs[i].getCompositeOfType(DbBEModel.metaClass);
                    dragObjs[i].getDb().commitTrans();
                    if (!dragComp.equals(dropComposite))
                        return false;
                } else
                    return false;
            }
            return true;
        } else if (dropObj instanceof DbBEActor || dropObj instanceof DbBEStore
                || dropObj instanceof DbBEFlow) {
            for (int i = 0; i < dragObjs.length; i++) {
                if (dragObjs[i] instanceof DbBEQualifier) {
                    dropObj.getDb().beginReadTrans();
                    DbObject dropComposite = dropObj.getCompositeOfType(DbBEModel.metaClass);
                    dropObj.getDb().commitTrans();

                    dragObjs[i].getDb().beginReadTrans();
                    DbObject dragComp = dragObjs[i].getCompositeOfType(DbBEModel.metaClass);
                    dragObjs[i].getDb().commitTrans();
                    if (!dragComp.equals(dropComposite))
                        return false;
                } else
                    return false;
            }
            return true;
        }

        // data modeling
        else if (dropObj instanceof DbORDomain) {
            for (int i = 0; i < dragObjs.length; i++)
                if (!(dragObjs[i] instanceof DbORField))
                    return false;
            return true;
        } else if (dropObj instanceof DbORDomainModel) {
            for (int i = 0; i < dragObjs.length; i++)
                if (!(dragObjs[i] instanceof DbORDomain))
                    return false;
            return true;
        } else if (dropObj instanceof DbORCommonItemModel) {
            for (int i = 0; i < dragObjs.length; i++)
                if (!(dragObjs[i] instanceof DbORCommonItem))
                    return false;
            return true;
        } else if (dropObj instanceof DbSMSLinkModel) {
            for (int i = 0; i < dragObjs.length; i++)
                if (!(dragObjs[i] instanceof DbSMSLink))
                    return false;
            return true;
        } else if (dropObj instanceof DbORAPartition) {
            for (int i = 0; i < dragObjs.length; i++)
                if (!(dragObjs[i] instanceof DbORASubPartition))
                    return false;
            return true;
        } else if (dropObj instanceof DbORAbsTable) {
            for (int i = 0; i < dragObjs.length; i++) {
                if (dropObj instanceof DbORATable
                        && (dragObjs[i] instanceof DbORANestedTableStorage
                                || dragObjs[i] instanceof DbORALobStorage || dragObjs[i] instanceof DbORAPartition))
                    return true;
                if (!(dragObjs[i] instanceof DbORColumn || dragObjs[i] instanceof DbORCheck
                        || dragObjs[i] instanceof DbORTrigger
                        || dragObjs[i] instanceof DbORPrimaryUnique || dragObjs[i] instanceof DbORIndex))
                    return false;
            }
            return true;
        }

        // class modeling
        else if (dropObj instanceof DbOOClass) {
            for (int i = 0; i < dragObjs.length; i++)
                if (!(dragObjs[i] instanceof DbOODataMember || dragObjs[i] instanceof DbOOOperation
                        || dragObjs[i] instanceof DbOOMethod || dragObjs[i] instanceof DbOOClass || dragObjs[i] instanceof DbOOInitBlock))
                    return false;
            return true;
        }

        else if (dropObj instanceof DbSMSUserDefinedPackage || dropObj instanceof DbSMSProject) {
            for (int i = 0; i < dragObjs.length; i++)
                if (!(dragObjs[i] instanceof DbORDataModel || dragObjs[i] instanceof DbOOClassModel
                        || dragObjs[i] instanceof DbORCommonItemModel
                        || dragObjs[i] instanceof DbORDomainModel
                        || dragObjs[i] instanceof DbBEModel
                        || dragObjs[i] instanceof DbOROperationLibrary
                        || dragObjs[i] instanceof DbORDatabase
                        || dragObjs[i] instanceof DbSMSLinkModel || dragObjs[i] instanceof DbSMSUserDefinedPackage))
                    return false;
            return true;
        }

        else if (dropObj instanceof DbOOPackage) {
            for (int i = 0; i < dragObjs.length; i++)
                if (!(dragObjs[i] instanceof DbOOClass || dragObjs[i] instanceof DbOOPackage))
                    return false;
            return true;
        }

        else if (dropObj instanceof DbBEModel) {
            for (int i = 0; i < dragObjs.length; i++)
                if (!(dragObjs[i] instanceof DbBEUseCase || dragObjs[i] instanceof DbBEActor || dragObjs[i] instanceof DbBEStore))
                    return false;
            return true;
        } else if (dropObj instanceof DbORDataModel) {
            for (int i = 0; i < dragObjs.length; i++)
                if (dragObjs[i] instanceof DbORANestedTableStorage)
                    return false;

            if (dropObj instanceof DbIBMDataModel) {
                for (int i = 0; i < dragObjs.length; i++) {
                    if (dragObjs[i] instanceof DbIBMSequence)
                        return true;
                    if (!(dragObjs[i] instanceof DbORTable || dragObjs[i] instanceof DbORView || dragObjs[i] instanceof DbORDataModel))
                        return false;
                }
                return true;
            } else if (dropObj instanceof DbINFDataModel) {
                for (int i = 0; i < dragObjs.length; i++) {
                    if (!(dragObjs[i] instanceof DbORTable || dragObjs[i] instanceof DbORView || dragObjs[i] instanceof DbORDataModel))
                        return false;
                }
                return true;
            } else if (dropObj instanceof DbORADataModel) {
                for (int i = 0; i < dragObjs.length; i++) {
                    if (dragObjs[i] instanceof DbORASequence)
                        return true;
                    if (!(dragObjs[i] instanceof DbORTable || dragObjs[i] instanceof DbORView || dragObjs[i] instanceof DbORDataModel))
                        return false;
                }
                return true;
            } else if (dropObj instanceof DbGEDataModel) {
                for (int i = 0; i < dragObjs.length; i++) {
                    if (!(dragObjs[i] instanceof DbORTable || dragObjs[i] instanceof DbORView || dragObjs[i] instanceof DbORDataModel))
                        return false;
                }
                return true;
            }
        } else if (dropObj instanceof DbOOClassModel) {
            for (int i = 0; i < dragObjs.length; i++)
                if (!(dragObjs[i] instanceof DbOOClass || dragObjs[i] instanceof DbOOPackage))
                    return false;
            return true;
        }

        else if (dropObj instanceof DbOROperationLibrary) {
            for (int i = 0; i < dragObjs.length; i++)
                if (!(dragObjs[i] instanceof DbORProcedure))
                    return false;
            return true;
        } else if (dropObj instanceof DbORProcedure) {
            for (int i = 0; i < dragObjs.length; i++)
                if (!(dragObjs[i] instanceof DbORParameter))
                    return false;
            return true;
        }

        return false;
    }

    // Assume isValidForCopy(dbos) was called before
    public final boolean isValidForPaste(DbObject[] dbos, DbObject[] composites) throws DbException {
        for (int i = 0; i < composites.length; i++) {
            if (composites[i] instanceof DbBEUseCase) {
                if (composites[i].getDb().getTransCount() == 0) {
                    System.out.println(composites[i]);
                }
                if (((DbBEUseCase) composites[i]).isExternal())
                    return false;
            }
        }
        return isValidForPasteOffline(dbos, composites);
    }

    // Assume isValidForCopy(dbos) was called before
    public final boolean isValidForPasteOffline(DbObject[] dbos, DbObject[] composites) {
        for (int i = 0; i < composites.length; i++) {
            MetaClass compositeMetaClass = composites[i].getMetaClass();
            int rootID = AnyORObject.getMetaClassRootID(compositeMetaClass);
            for (int j = 0; j < dbos.length; j++) {
                MetaClass metaClass = dbos[j].getMetaClass();
                if (rootID != -1) // translate metaClass to target system of
                    // composite before checking composite
                    // allowed.
                    metaClass = AnyORObject.translateMetaClass(metaClass, rootID);
                if (!metaClass.compositeIsAllowed(compositeMetaClass))
                    return false;
            }
        }
        return true;
    }

    // Assume isValidForPaste(dbos, composites) was called before
    public final void paste(DbObject[] dbos, DbObject[] composites, String actionName,
            boolean prefixAllowed) throws DbException {
        // Start a write transaction on the source Db in case it is also a dest Db.
        ArrayList<DbSMSGraphicalObject> graphicComponents = new ArrayList<DbSMSGraphicalObject>();

        dbos[0].getDb().beginTrans(Db.WRITE_TRANS, actionName);
        int i, k;
        boolean selectDiagramObjects = false;
        boolean nullObjectFound = false;
        Object focus = ApplicationContext.getFocusManager().getFocusObject();
        for (i = 0; i < composites.length; i++) {
            composites[i].getDb().beginTrans(Db.WRITE_TRANS, actionName);
            DbObject[] selObjs = DbObject.deepCopy(dbos, composites[i], new SMSDeepCopyCustomizer(
                    dbos[0].getProject(), composites[i]), prefixAllowed);
            int nb = getPastableObjNb(selObjs);
            DbObject[] newDbos = new DbObject[nb];
            for (i = 0, k = 0; i < selObjs.length; i++) {
                if (selObjs[i] != null) {
                    newDbos[k] = ((DbObject) selObjs[i]);
                    k++;
                } else
                    nullObjectFound = true;
            }
            if (focus instanceof ApplicationDiagram) {
                DbObject objectAtLocation = ((ApplDiagramView) ((ApplicationDiagram) focus)
                        .getMainView()).getSemObjAtLocation(new Point(1, 1));
                // this is a paste in the diagram, duplicate RG in the diagram
                SendToDiagramAction sendTo = (SendToDiagramAction) ApplicationContext
                        .getActionStore().getAction(SMSActionsStore.SEND_TO_DIAGRAM);
                if (sendTo != null) {
                    graphicComponents = sendTo.sendToDiagram((ApplicationDiagram) focus, newDbos);
                    selectDiagramObjects = true;
                }
            }
            ApplicationContext.getDefaultMainFrame().findInExplorer(newDbos);
        }
        if (nullObjectFound) {
            JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(),
                    kIncompletePaste, ApplicationContext.getApplicationName(),
                    JOptionPane.WARNING_MESSAGE);
        }

        for (i = 0; i < composites.length; i++)
            composites[i].getDb().commitTrans();
        dbos[0].getDb().commitTrans();

        ////
        // select the new graphical objects in the diagram

        if (selectDiagramObjects) {
            ApplicationDiagram diagModel = (ApplicationDiagram) focus;
            DbSMSDiagram diagGO = (DbSMSDiagram) diagModel.getDiagramGO();

            diagGO.getDb().beginTrans(Db.WRITE_TRANS, "");
            diagModel.deselectAll();
            for (int j = 0; j < graphicComponents.size(); j++) {
                DbSMSGraphicalObject go = graphicComponents.get(j);
                if (go != null) {
                    GraphicComponent gc = (GraphicComponent) ((DbGraphicalObjectI) go)
                            .getGraphicPeer();
                    if (gc != null)
                        gc.setSelected(true);
                }
            }
            diagGO.getDb().commitTrans();
        }
    }

    public final void paste(DbObject[] dbos, DbObject[] composites, String actionName,
            boolean prefixAllowed, Point location) throws DbException {
        // Start a write transaction on the source Db in case it is also a dest Db.
        ArrayList<DbSMSGraphicalObject> graphicComponents = new ArrayList<DbSMSGraphicalObject>();

        dbos[0].getDb().beginTrans(Db.WRITE_TRANS, actionName);
        int i, k;
        boolean selectDiagramObjects = false;
        boolean nullObjectFound = false;
        Object focus = ApplicationContext.getFocusManager().getFocusObject();
        for (i = 0; i < composites.length; i++) {
            composites[i].getDb().beginTrans(Db.WRITE_TRANS, actionName);
            DbObject[] selObjs = DbObject.deepCopy(dbos, composites[i], new SMSDeepCopyCustomizer(
                    dbos[0].getProject(), composites[i]), prefixAllowed);
            int nb = getPastableObjNb(selObjs);
            DbObject[] newDbos = new DbObject[nb];
            for (i = 0, k = 0; i < selObjs.length; i++) {
                if (selObjs[i] != null) {
                    newDbos[k] = ((DbObject) selObjs[i]);
                    k++;
                } else
                    nullObjectFound = true;
            }
            if (focus instanceof ApplicationDiagram) {
                // this is a paste in the diagram, duplicate RG in the diagram

                DbObject objectAtLocation = ((ApplDiagramView) ((ApplicationDiagram) focus)
                        .getMainView()).getSemObjAtLocation(location);
                if (objectAtLocation instanceof DbSMSDiagram) {
                    SendToDiagramAction sendTo = (SendToDiagramAction) ApplicationContext
                            .getActionStore().getAction(SMSActionsStore.SEND_TO_DIAGRAM);
                    if (sendTo != null) {
                        graphicComponents = sendTo.sendToDiagram((ApplicationDiagram) focus,
                                newDbos);
                        selectDiagramObjects = true;
                    }
                }
            }
            ApplicationContext.getDefaultMainFrame().findInExplorer(newDbos);
        }
        for (i = 0; i < composites.length; i++)
            composites[i].getDb().commitTrans();
        dbos[0].getDb().commitTrans();

        ////
        // select the new graphical objects in the diagram

        if (selectDiagramObjects) {
            ApplicationDiagram diagModel = (ApplicationDiagram) focus;
            DbSMSDiagram diagGO = (DbSMSDiagram) diagModel.getDiagramGO();

            diagGO.getDb().beginTrans(Db.WRITE_TRANS, "");
            diagModel.deselectAll();
            for (int j = 0; j < graphicComponents.size(); j++) {
                DbSMSGraphicalObject go = graphicComponents.get(j);
                if (go != null) {
                    GraphicComponent gc = (GraphicComponent) ((DbGraphicalObjectI) go)
                            .getGraphicPeer();
                    if (gc != null)
                        gc.setSelected(true);
                }
            }
            diagGO.getDb().commitTrans();
        }
    }

    public ArrayList<AssociationRoles> getLineLabels(DbObject semObj) throws DbException {
        if (semObj instanceof DbSMSAssociationEnd) {
            semObj.getDb().beginReadTrans();
            DbORAssociation ass = (DbORAssociation) semObj.getComposite();
            semObj.getDb().commitTrans();
            if (ass != null) {
                ArrayList<AssociationRoles> arrayList = new ArrayList<AssociationRoles>();
                ass.getDb().beginReadTrans();
                DbRelationN relN = ass.getAssociationGos();
                DbEnumeration dbEnum = relN.elements();
                DbORAssociationGo assGo = null;
                while (dbEnum.hasMoreElements()) {
                    assGo = (DbORAssociationGo) dbEnum.nextElement();
                    if (assGo != null) {
                        ORAssociation orAss = (ORAssociation) assGo.getGraphicPeer();
                        AssociationRoles ar = new AssociationRoles();
                        ar.setAssociationGo(assGo);

                        if (ass != null) {
                            DbSMSAssociationEnd end = ass.getBackEnd();
                            if (end != null) {
                                if (end.equals(semObj)) {
                                    ar.setLineLabel1(null);
                                    if (orAss != null)
                                        ar.setLineLabel2(orAss.getLabel2());
                                } else {
                                    if (orAss != null)
                                        ar.setLineLabel1(orAss.getLabel1());
                                    ar.setLineLabel2(null);
                                }
                                arrayList.add(ar);
                            }
                        }
                    }
                }
                dbEnum.close();
                ass.getDb().commitTrans();
                return arrayList;
            } else
                return null;
        }
        return null;
    }

    public final DbRelationN getGos(DbObject semObj) throws DbException {
        MetaRelationN gosField = null;
        if (semObj instanceof DbSMSClassifier)
            gosField = DbSMSClassifier.fClassifierGos;
        else if (semObj instanceof DbSMSPackage)
            gosField = DbSMSPackage.fPackageGos;
        else if (semObj instanceof DbSMSAssociation)
            gosField = DbSMSAssociation.fAssociationGos;
        else if (semObj instanceof DbSMSInheritance)
            gosField = DbSMSInheritance.fInheritanceGos;
        else if (semObj instanceof DbSMSCommonItem)
            gosField = DbSMSCommonItem.fCommonItemGos;
        else if (semObj instanceof DbSMSNotice)
            gosField = DbSMSNotice.fNoticeGos;
        else if (semObj instanceof DbBEFlow)
            gosField = DbBEFlow.fFlowGos;
        return (gosField != null ? (DbRelationN) semObj.get(gosField) : null);
    }

    public AddElement[] getAddElements() {
        AddElement[] poolElements = AddElementPool.getAddElements(terminology);
        return poolElements;
    }

    private final int getPastableObjNb(DbObject[] selObjs) throws DbException {
        int nb = 0;
        for (int i = 0; i < selObjs.length; i++) {
            if (selObjs[i] != null) {
                nb++;
            }
        }
        return nb;
    }
}
