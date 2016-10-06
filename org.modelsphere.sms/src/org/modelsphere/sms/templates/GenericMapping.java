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

package org.modelsphere.sms.templates;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.meta.MetaChoice;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelation1;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.plugins.PluginMgr;
import org.modelsphere.jack.srtool.forward.Rule;
import org.modelsphere.jack.srtool.forward.RuleException;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBEQualifier;
import org.modelsphere.sms.be.db.DbBEResource;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.db.DbSMSBuiltInTypeNode;
import org.modelsphere.sms.db.DbSMSBuiltInTypePackage;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOOAdt;
import org.modelsphere.sms.oo.db.DbOOAdtGo;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.java.db.DbJVAssociation;
import org.modelsphere.sms.oo.java.db.DbJVAssociationEnd;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVCompilationUnit;
import org.modelsphere.sms.oo.java.db.DbJVConstructor;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVImport;
import org.modelsphere.sms.oo.java.db.DbJVInheritance;
import org.modelsphere.sms.oo.java.db.DbJVInitBlock;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.oo.java.db.DbJVParameter;
import org.modelsphere.sms.oo.java.db.DbJVPrimitiveType;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORBuiltInType;
import org.modelsphere.sms.or.db.DbORCheck;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORConstraint;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORFKeyColumn;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbORIndexKey;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.db.DbORPackage;
import org.modelsphere.sms.or.db.DbORParameter;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORProcedure;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTableGo;
import org.modelsphere.sms.or.db.DbORTrigger;
import org.modelsphere.sms.or.db.DbORView;
import org.modelsphere.sms.or.ibm.db.DbIBMBufferPool;
import org.modelsphere.sms.or.ibm.db.DbIBMColumn;
import org.modelsphere.sms.or.ibm.db.DbIBMDbPartitionGroup;
import org.modelsphere.sms.or.ibm.db.DbIBMTablespace;
import org.modelsphere.sms.or.ibm.db.DbIBMTrigger;
import org.modelsphere.sms.or.informix.db.DbINFCheck;
import org.modelsphere.sms.or.informix.db.DbINFColumn;
import org.modelsphere.sms.or.informix.db.DbINFDatabase;
import org.modelsphere.sms.or.informix.db.DbINFDbspace;
import org.modelsphere.sms.or.informix.db.DbINFForeign;
import org.modelsphere.sms.or.informix.db.DbINFFragment;
import org.modelsphere.sms.or.informix.db.DbINFIndex;
import org.modelsphere.sms.or.informix.db.DbINFParameter;
import org.modelsphere.sms.or.informix.db.DbINFPrimaryUnique;
import org.modelsphere.sms.or.informix.db.DbINFProcedure;
import org.modelsphere.sms.or.informix.db.DbINFSbspace;
import org.modelsphere.sms.or.informix.db.DbINFTable;
import org.modelsphere.sms.or.informix.db.DbINFView;
import org.modelsphere.sms.or.oracle.db.DbORAAbsPartition;
import org.modelsphere.sms.or.oracle.db.DbORAAbsTable;
import org.modelsphere.sms.or.oracle.db.DbORACheck;
import org.modelsphere.sms.or.oracle.db.DbORAColumn;
import org.modelsphere.sms.or.oracle.db.DbORADataFile;
import org.modelsphere.sms.or.oracle.db.DbORADatabase;
import org.modelsphere.sms.or.oracle.db.DbORAFile;
import org.modelsphere.sms.or.oracle.db.DbORAForeign;
import org.modelsphere.sms.or.oracle.db.DbORAIndex;
import org.modelsphere.sms.or.oracle.db.DbORALobStorage;
import org.modelsphere.sms.or.oracle.db.DbORANestedTableStorage;
import org.modelsphere.sms.or.oracle.db.DbORAPackage;
import org.modelsphere.sms.or.oracle.db.DbORAParameter;
import org.modelsphere.sms.or.oracle.db.DbORAPartition;
import org.modelsphere.sms.or.oracle.db.DbORAPrimaryUnique;
import org.modelsphere.sms.or.oracle.db.DbORAProcedure;
import org.modelsphere.sms.or.oracle.db.DbORARedoLogFile;
import org.modelsphere.sms.or.oracle.db.DbORARedoLogGroup;
import org.modelsphere.sms.or.oracle.db.DbORARollbackSegment;
import org.modelsphere.sms.or.oracle.db.DbORASequence;
import org.modelsphere.sms.or.oracle.db.DbORASubPartition;
import org.modelsphere.sms.or.oracle.db.DbORATable;
import org.modelsphere.sms.or.oracle.db.DbORATablespace;
import org.modelsphere.sms.or.oracle.db.DbORATrigger;
import org.modelsphere.sms.or.oracle.db.DbORAView;

/**
 * This class provides a mapping between "repository" strings (such as "tableColumns", "tablename",
 * etc.) and Generic meta fields (such as "m_components", "m_name", etc.).
 * 
 * 
 * IMPORTANT NOTICE FOR THOSE WHO WANT TO ADD A REPOSITORY FUNCTION
 * 
 * 1-Follow the Repository Function Naming Conventions (see in SourceSafe, under
 * "Dossiers de conception" and Templates).
 * 
 * 2-Create all the special repository functions in putRepositoryFunctions(). These repository
 * functions will eventually be loaded dynamically.
 * 
 * 3-Keep all the repository functions related to a given concept together. For instance, keep all
 * the functions related to the concept "Project" together.
 * 
 * 4-Be consistent when you name your functions (see the previously defined functions), and give a
 * name close to the name defined in the meta (use Physicalname instead of Codedname).
 * 
 * 5-When you define a CONN function, always provide the expected type of the children. For
 * instance, define:
 * 
 * g_repositoryStringTable.put("ProjectPackages", new
 * GenericMappingStructure(DbSMSProject.fComponents, DbSMSPackage.metaClass));
 * 
 * rather than:
 * 
 * g_repositoryStringTable.put("ProjectPackages", new
 * GenericMappingStructure(DbSMSProject.fComponents));
 */

//Note: everything is static: no instance of GenericMapping is created
public class GenericMapping {

    private static final String DUPLICATE_NOT_ALLOWED_PATTRN = LocaleMgr.misc
            .getString("duplicatedNamesInRepositoryFunctions");
    private static final String REPOSITORY_FUNCTION_NOT_FOUND = LocaleMgr.misc
            .getString("repositoryFunctionNotFound");

    static RepositoryFunctionTable g_repositoryStringTable = new RepositoryFunctionTable();

    /**
     * <b> SECTION 1: GENERIC CONCEPTS (FOR ALL TARGET SYSTEMS). </b><br>
     * <br>
     * List of concepts:<br>
     * - Object: any occurrence is an Object.<br>
     * - Project: the root object containing all other objects (models, libraries, ..).<br>
     */
    //Object, Project, Typenode
    public static Section Section_1;

    /**
     * Returns the alias of an object. Only objects with a physical name attribute have also an
     * alias. <br>
     * Target System: <b>All</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry ObjectAlias = new GenericMappingEntry("ObjectAlias", //NOT LOCALIZABLE
            new GenericMappingProperty(DbSemanticalObject.fAlias));
    static {
        g_repositoryStringTable.put(ObjectAlias.m_name, ObjectAlias.m_struct);
    }

    /**
     * Returns the name of an object. <br>
     * Target System: <b>All</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry ObjectName = new GenericMappingEntry("ObjectName", //NOT LOCALIZABLE
            new GenericMappingProperty(DbSemanticalObject.fName));
    static {
        g_repositoryStringTable.put(ObjectName.m_name, ObjectName.m_struct);
    }

    /**
     * Returns the physical name of an object. <br>
     * Target System: <b>All</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry ObjectPhysicalname = new GenericMappingEntry(
            "ObjectPhysicalname", //NOT LOCALIZABLE
            new GenericMappingProperty(DbSemanticalObject.fPhysicalName));
    static {
        g_repositoryStringTable.put(ObjectPhysicalname.m_name, ObjectPhysicalname.m_struct);
    }

    /**
     * Returns the description of an object. <br>
     * Target System: <b>All</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry ObjectDescription = new GenericMappingEntry(
            "ObjectDescription", //NOT LOCALIZABLE
            new GenericMappingProperty(DbSemanticalObject.fDescription));
    static {
        g_repositoryStringTable.put(ObjectDescription.m_name, ObjectDescription.m_struct);
    }

    /**
     * Returns the composite of an object. <br>
     * Target System: <b>All</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ObjectComposite = new GenericMappingEntry("ObjectComposite", //NOT LOCALIZABLE
            new GenericMappingComposite(DbSemanticalObject.metaClass));
    static {
        g_repositoryStringTable.put(ObjectComposite.m_name, ObjectComposite.m_struct);
    }

    //Project-related features
    /**
     * Returns the name of a project. <br>
     * Target System: <b>All</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry ProjectName = new GenericMappingEntry("ProjectName", //NOT LOCALIZABLE
            new GenericMappingProperty(DbSMSProject.fName));
    static {
        g_repositoryStringTable.put(ProjectName.m_name, ProjectName.m_struct);
    }

    /**
     * Returns the physical name of a project. <br>
     * Target System: <b>All</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry ProjectPhysicalname = new GenericMappingEntry(
            "ProjectPhysicalname", //NOT LOCALIZABLE
            new GenericMappingProperty(DbSMSProject.fPhysicalName));
    static {
        g_repositoryStringTable.put(ProjectPhysicalname.m_name, ProjectPhysicalname.m_struct);
    }

    /**
     * Returns the description of a project. <br>
     * Target System: <b>All</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry ProjectDescription = new GenericMappingEntry(
            "ProjectDescription", //NOT LOCALIZABLE
            new GenericMappingProperty(DbSMSProject.fDescription));
    static {
        g_repositoryStringTable.put(ProjectDescription.m_name, ProjectDescription.m_struct);
    }

    /**
     * Lists all the packages contained under a project. <br>
     * Target System: <b>All</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ProjectPackages = new GenericMappingEntry("ProjectPackages", //NOT LOCALIZABLE
            new GenericMappingComponents(DbSMSPackage.metaClass));
    static {
        g_repositoryStringTable.put(ProjectPackages.m_name, ProjectPackages.m_struct);
    }

    /**
     * Lists all the data models contained under a project. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ProjectDatamodels = new GenericMappingEntry(
            "ProjectDatamodels", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORDataModel.metaClass));
    static {
        g_repositoryStringTable.put(ProjectDatamodels.m_name, ProjectDatamodels.m_struct);
    }

    /**
     * Lists all the class models contained under a project. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ProjectClassmodels = new GenericMappingEntry(
            "ProjectClassmodels", //NOT LOCALIZABLE
            new GenericMappingComponents(DbJVClassModel.metaClass));
    static {
        g_repositoryStringTable.put(ProjectClassmodels.m_name, ProjectClassmodels.m_struct);
    }

    /**
     * Lists all the class models contained under a project. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ProjectBemodels = new GenericMappingEntry("ProjectBemodels", //NOT LOCALIZABLE
            new GenericMappingComponents(DbBEModel.metaClass));
    static {
        g_repositoryStringTable.put(ProjectBemodels.m_name, ProjectBemodels.m_struct);
    }

    /**
     * Lists all the type nodes contained under a project. <br>
     * Target System: <b>All</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ProjectTypenodes = new GenericMappingEntry(
            "ProjectTypenodes", //NOT LOCALIZABLE
            new GenericMappingComponents(DbSMSBuiltInTypeNode.metaClass));
    static {
        g_repositoryStringTable.put(ProjectTypenodes.m_name, ProjectTypenodes.m_struct);
    }

    /**
     * Lists all the type packages contained under a project. <br>
     * Target System: <b>All</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry TypenodeTypepackages = new GenericMappingEntry(
            "TypenodeTypepackages", //NOT LOCALIZABLE
            new GenericMappingComponents(DbSMSBuiltInTypePackage.metaClass));
    static {
        g_repositoryStringTable.put(TypenodeTypepackages.m_name, TypenodeTypepackages.m_struct);
    }

    /**
     * Lists all the Java primitive types contained under a project. The Java primitive types are
     * also called built-in types. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry TypepackagePrimitivetypes = new GenericMappingEntry(
            "TypepackagePrimitivetypes", //NOT LOCALIZABLE
            new GenericMappingComponents(DbJVPrimitiveType.metaClass));
    static {
        g_repositoryStringTable.put(TypepackagePrimitivetypes.m_name,
                TypepackagePrimitivetypes.m_struct);
    }

    /**
     * <b> SECTION 2: BPM CONCEPTS. </b> <br>
     * <br>
     * List of Java concepts:<br>
     * - Process: a process, or use-case.<br>
     * - Actor: an actor, or external entity.<br>
     * - Flow: a link between processes, or between a process and another concept.<br>
     * - Store: a data store.<br>
     */
    public static Section Section_2;

    /**
     * Lists all the processes contained under a BE model. <br>
     * Target System: <b>Behavioral</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry BemodelProcesses = new GenericMappingEntry(
            "BemodelProcesses", //NOT LOCALIZABLE
            new GenericMappingComponents(DbBEUseCase.metaClass));
    static {
        g_repositoryStringTable.put(BemodelProcesses.m_name, BemodelProcesses.m_struct);
    }

    /**
     * Lists all the actors contained under a BE model. <br>
     * Target System: <b>Behavioral</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry BemodelActors = new GenericMappingEntry("BemodelActors", //NOT LOCALIZABLE
            new GenericMappingComponents(DbBEActor.metaClass));
    static {
        g_repositoryStringTable.put(BemodelActors.m_name, BemodelActors.m_struct);
    }

    /**
     * Lists all the data stores contained under a BE model. <br>
     * Target System: <b>Behavioral</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry BemodelStores = new GenericMappingEntry("BemodelStores", //NOT LOCALIZABLE
            new GenericMappingComponents(DbBEStore.metaClass));
    static {
        g_repositoryStringTable.put(BemodelStores.m_name, BemodelStores.m_struct);
    }

    /**
     * Lists all the data stores contained under a BE model. <br>
     * Target System: <b>Behavioral</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry BemodelFlows = new GenericMappingEntry("BemodelFlows", //NOT LOCALIZABLE
            new GenericMappingComponents(DbBEFlow.metaClass));
    static {
        g_repositoryStringTable.put(BemodelFlows.m_name, BemodelFlows.m_struct);
    }

    /**
     * Lists all the data stores contained under a BE model. <br>
     * Target System: <b>Behavioral</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry BemodelResources = new GenericMappingEntry(
            "BemodelResources", //NOT LOCALIZABLE
            new GenericMappingComponents(DbBEResource.metaClass));
    static {
        g_repositoryStringTable.put(BemodelResources.m_name, BemodelResources.m_struct);
    }

    /**
     * Lists all the data stores contained under a BE model. <br>
     * Target System: <b>Behavioral</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry BemodelQualifiers = new GenericMappingEntry(
            "BemodelQualifiers", //NOT LOCALIZABLE
            new GenericMappingComponents(DbBEQualifier.metaClass));
    static {
        g_repositoryStringTable.put(BemodelQualifiers.m_name, BemodelQualifiers.m_struct);
    }

    /**
     * <b> SECTION 3: JAVA CONCEPTS (ONLY FOR THE JAVA TARGET SYSTEM). </b> <br>
     * <br>
     * List of Java concepts:<br>
     * - Classmodel: the ultimate container of all Java concepts.<br>
     * - Package: the equivalent of a Java package.<br>
     * - Class: a Java classifier (class or interface).<br>
     * - Import: an import clause in Java.<br>
     * - Inheritance: an inheritance link between two classifiers (a super-classifier and a
     * sub-classifier).<br>
     * - Field: a Java field within a classifier.<br>
     * - Initblock: a statement block called at the initialization of a Java object.<br>
     * - Constructor: a special operation allowing the instantiation of a Java object.<br>
     * - Method: a Java method within a classifier.<br>
     * - Parameter: a Java method's argument.<br>
     * - Association: a link within two classifiers representing an aggregation, a composition or
     * another kind of association.<br>
     * - Associationend: one of the two ends of an association.<br>
     */
    public static Section Section_3;

    /**
     * Lists all the classifiers contained under a class model. Classifiers, formerly called
     * Abstract Data Types, include classes and interfaces. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     * 
     * @deprecated
     * @see org.modelsphere.sms.plugins.generic.GenericMapping#ClassmodelClassifiers
     */
    public static GenericMappingEntry ClassmodelAdts = new GenericMappingEntry("ClassmodelAdts", //NOT LOCALIZABLE
            new GenericMappingComponents(DbOOAdt.metaClass));
    static {
        g_repositoryStringTable.put(ClassmodelAdts.m_name, ClassmodelAdts.m_struct);
    }

    /**
     * Lists all the classifiers contained under a class model. Classifiers include classes and
     * interfaces. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ClassmodelClassifiers = new GenericMappingEntry(
            "ClassmodelClassifiers", //NOT LOCALIZABLE
            new GenericMappingComponents(DbOOAdt.metaClass));
    static {
        g_repositoryStringTable.put(ClassmodelClassifiers.m_name, ClassmodelClassifiers.m_struct);
    }

    /**
     * Lists all the diagrams contained under a class model. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ClassmodelDiagrams = new GenericMappingEntry(
            "ClassmodelDiagrams", //NOT LOCALIZABLE
            new GenericMappingComponents(DbOODiagram.metaClass));
    static {
        g_repositoryStringTable.put(ClassmodelDiagrams.m_name, ClassmodelDiagrams.m_struct);
    }

    /**
     * Lists all the graphical classes contained under a diagram. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DiagramGraphicalclasses = new GenericMappingEntry(
            "DiagramGraphicalclasses", //NOT LOCALIZABLE
            new GenericMappingComponents(DbOOAdtGo.metaClass));
    static {
        g_repositoryStringTable.put(DiagramGraphicalclasses.m_name,
                DiagramGraphicalclasses.m_struct);
    }

    /**
     * Gets the semantic class associated to a graphical class. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry GraphicalclassSemanticclass = new GenericMappingEntry(
            "GraphicalclassSemanticclass", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbOOAdtGo.fClassifier));
    static {
        g_repositoryStringTable.put(GraphicalclassSemanticclass.m_name,
                GraphicalclassSemanticclass.m_struct);
    }

    /**
     * Gets the semantic class associated to a graphical class. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     * 
     * @deprecated
     * @see org.modelsphere.sms.plugins.generic.GenericMapping#GraphicalclassSemanticclass
     */
    public static GenericMappingEntry GraphicalclassSemanticalclass = new GenericMappingEntry(
            "GraphicalclassSemanticalclass", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbOOAdtGo.fClassifier));
    static {
        g_repositoryStringTable.put(GraphicalclassSemanticalclass.m_name,
                GraphicalclassSemanticalclass.m_struct);
    }

    /**
     * Lists all the packages under a class model. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ClassmodelPackages = new GenericMappingEntry(
            "ClassmodelPackages", //NOT LOCALIZABLE
            new GenericMappingComponents(DbJVPackage.metaClass));
    static {
        g_repositoryStringTable.put(ClassmodelPackages.m_name, ClassmodelPackages.m_struct);
    }

    /**
     * Gets the version of a class model. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry ClassmodelVersion = new GenericMappingEntry(
            "ClassmodelVersion", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVClassModel.fVersion));
    static {
        g_repositoryStringTable.put(ClassmodelVersion.m_name, ClassmodelVersion.m_struct);
    }

    /**
     * Gets the author of a class model. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry ClassmodelAuthor = new GenericMappingEntry(
            "ClassmodelAuthor", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVClassModel.fAuthor));
    static {
        g_repositoryStringTable.put(ClassmodelAuthor.m_name, ClassmodelAuthor.m_struct);
    }

    /**
     * Gets the name of a Java package. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry PackageName = new GenericMappingEntry("PackageName", //NOT LOCALIZABLE
            new GenericMappingProperty(DbSMSPackage.fName));
    static {
        g_repositoryStringTable.put(PackageName.m_name, PackageName.m_struct);
    }

    /**
     * Lists all the diagrams contained under a Java package. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry PackageDiagrams = new GenericMappingEntry("PackageDiagrams", //NOT LOCALIZABLE
            new GenericMappingComponents(DbOODiagram.metaClass));
    static {
        g_repositoryStringTable.put(PackageDiagrams.m_name, PackageDiagrams.m_struct);
    }

    /**
     * Lists all the classifiers (formerly called Abstract Data Types) contained under a Java
     * package. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     * 
     * @deprecated
     * @see org.modelsphere.sms.plugins.generic.GenericMapping#PackageClassifiers
     */
    public static GenericMappingEntry PackageAdts = new GenericMappingEntry("PackageAdts", //NOT LOCALIZABLE
            new GenericMappingComponents(DbOOAdt.metaClass));
    static {
        g_repositoryStringTable.put(PackageAdts.m_name, PackageAdts.m_struct);
    }

    /**
     * Lists all the classifiers contained under a Java package. Classifiers include classes and
     * interfaces. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry PackageClassifiers = new GenericMappingEntry(
            "PackageClassifiers", //NOT LOCALIZABLE
            new GenericMappingComponents(DbOOAdt.metaClass));
    static {
        g_repositoryStringTable.put(PackageClassifiers.m_name, PackageClassifiers.m_struct);
    }

    /**
     * List all the sub packages contained under a Java package. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry PackagePackages = new GenericMappingEntry("PackagePackages", //NOT LOCALIZABLE
            new GenericMappingComponents(DbSMSPackage.metaClass));
    static {
        g_repositoryStringTable.put(PackagePackages.m_name, PackagePackages.m_struct);
    }

    /**
     * Gets the Java package containing the current package, if any. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry PackagePackage = new GenericMappingEntry("PackagePackage", //NOT LOCALIZABLE
            new GenericMappingComposite(DbSMSPackage.metaClass));
    static {
        g_repositoryStringTable.put(PackagePackage.m_name, PackagePackage.m_struct);
    }

    /**
     * Gets the version of a Java package. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry PackageVersion = new GenericMappingEntry("PackageVersion", //NOT LOCALIZABLE
            new GenericMappingProperty(DbSMSPackage.fVersion));
    static {
        g_repositoryStringTable.put(PackageVersion.m_name, PackageVersion.m_struct);
    }

    /**
     * Gets the author of a Java package. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry PackageAuthor = new GenericMappingEntry("PackageAuthor", //NOT LOCALIZABLE
            new GenericMappingProperty(DbSMSPackage.fAuthor));
    static {
        g_repositoryStringTable.put(PackageAuthor.m_name, PackageAuthor.m_struct);
    }

    /**
     * Lists all the classifiers associated to a compilation unit. Classifiers include classes and
     * interfaces. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry CompilationunitClasses = new GenericMappingEntry(
            "CompilationunitClasses", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbJVCompilationUnit.fClasses, DbJVClass.metaClass));
    static {
        g_repositoryStringTable.put(CompilationunitClasses.m_name, CompilationunitClasses.m_struct);
    }

    /**
     * Lists all the import clauses of a compilation unit. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry CompilationunitImports = new GenericMappingEntry(
            "CompilationunitImports", //NOT LOCALIZABLE
            new GenericMappingComponents(DbJVImport.metaClass));
    static {
        g_repositoryStringTable.put(CompilationunitImports.m_name, CompilationunitImports.m_struct);
    }

    /**
     * Gets the package imported by the import clause, if any. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ImportPackage = new GenericMappingEntry("ImportPackage", //NOT LOCALIZABLE
            new GenericMappingChoice(DbJVImport.fImportedObject, DbJVPackage.metaClass));
    static {
        g_repositoryStringTable.put(ImportPackage.m_name, ImportPackage.m_struct);
    }

    /**
     * Gets the classifier imported by the import clause, if any. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ImportClass = new GenericMappingEntry("ImportClass", //NOT LOCALIZABLE
            new GenericMappingChoice(DbJVImport.fImportedObject, DbJVClass.metaClass));
    static {
        g_repositoryStringTable.put(ImportClass.m_name, ImportClass.m_struct);
    }

    /**
     * Tells whether an import clause imports all the classes of a given package. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: true or false.
     */
    public static GenericMappingEntry ImportIsall = new GenericMappingEntry("ImportIsall", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVImport.fAll));
    static {
        g_repositoryStringTable.put(ImportIsall.m_name, ImportIsall.m_struct);
    }

    /**
     * Get the compilation unit associated to a classifier, if any. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ClassCompilationUnit = new GenericMappingEntry(
            "ClassCompilationUnit", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbJVClass.fCompilationUnit));
    static {
        g_repositoryStringTable.put(ClassCompilationUnit.m_name, ClassCompilationUnit.m_struct);
    }

    /**
     * Gets the stereotype of a classifier. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" if it is a class, but not an exception.<br>
     * - "2' if it is an interface.<br>
     * - "3" if it is an exception.<br>
     */
    public static GenericMappingEntry ClassStereotype = new GenericMappingEntry("ClassStereotype", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVClass.fStereotype));
    static {
        g_repositoryStringTable.put(ClassStereotype.m_name, ClassStereotype.m_struct);
    }

    /**
     * Lists all the fields contained under a classifier. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ClassFields = new GenericMappingEntry("ClassFields", //NOT LOCALIZABLE
            new GenericMappingComponents(DbJVDataMember.metaClass));
    static {
        g_repositoryStringTable.put(ClassFields.m_name, ClassFields.m_struct);
    }

    /**
     * Lists all the constructors contained under a classifier. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ClassConstructors = new GenericMappingEntry(
            "ClassConstructors", //NOT LOCALIZABLE
            new GenericMappingComponents(DbJVConstructor.metaClass));
    static {
        g_repositoryStringTable.put(ClassConstructors.m_name, ClassConstructors.m_struct);
    }

    /**
     * Lists all the methods contained under a classifier. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ClassMethods = new GenericMappingEntry("ClassMethods", //NOT LOCALIZABLE
            new GenericMappingComponents(DbJVMethod.metaClass));
    static {
        g_repositoryStringTable.put(ClassMethods.m_name, ClassMethods.m_struct);
    }

    /**
     * Lists all the generalization links associated to a classifier. A generalization link is an
     * inheritance link between a classifier and a superclassifier (superclass or superinterface). <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ClassSuperinheritances = new GenericMappingEntry(
            "ClassSuperinheritances", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbJVClass.fSuperInheritances, DbJVInheritance.metaClass));
    static {
        g_repositoryStringTable.put(ClassSuperinheritances.m_name, ClassSuperinheritances.m_struct);
    }

    /**
     * Lists all the specialization links associated to a classifier. A specialization link is an
     * inheritance link between a classifier and a subclassifier (subclass or subinterface). <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ClassSubinheritances = new GenericMappingEntry(
            "ClassSubinheritances", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbJVClass.fSubInheritances, DbJVInheritance.metaClass));
    static {
        g_repositoryStringTable.put(ClassSubinheritances.m_name, ClassSubinheritances.m_struct);
    }

    /**
     * Lists all the associations of a classifier with other classifiers. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ClassAssociations = new GenericMappingEntry(
            "ClassAssociations", //NOT LOCALIZABLE
            new GenericMappingComponents(DbJVAssociation.metaClass));
    static {
        g_repositoryStringTable.put(ClassAssociations.m_name, ClassAssociations.m_struct);
    }

    /**
     * Lists all the inheritance links associated to a classifier. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ClassInheritances = new GenericMappingEntry(
            "ClassInheritances", //NOT LOCALIZABLE
            new GenericMappingComponents(DbJVInheritance.metaClass));
    static {
        g_repositoryStringTable.put(ClassInheritances.m_name, ClassInheritances.m_struct);
    }

    /**
     * Lists all the inner classifiers of a given classifier. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ClassInnerclasses = new GenericMappingEntry(
            "ClassInnerclasses", //NOT LOCALIZABLE
            new GenericMappingComponents(DbOOAdt.metaClass));
    static {
        g_repositoryStringTable.put(ClassInnerclasses.m_name, ClassInnerclasses.m_struct);
    }

    /**
     * Lists all the initialization blocks of a classifier. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ClassInitblocks = new GenericMappingEntry("ClassInitblocks", //NOT LOCALIZABLE
            new GenericMappingComponents(DbJVInitBlock.metaClass));
    static {
        g_repositoryStringTable.put(ClassInitblocks.m_name, ClassInitblocks.m_struct);
    }

    /**
     * Tells whether a class is abstract. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry ClassIsabstract = new GenericMappingEntry("ClassIsabstract", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVClass.fAbstract));
    static {
        g_repositoryStringTable.put(ClassIsabstract.m_name, ClassIsabstract.m_struct);
    }

    /**
     * Tells whether a class is final. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry ClassIsfinal = new GenericMappingEntry("ClassIsfinal", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVClass.fFinal));
    static {
        g_repositoryStringTable.put(ClassIsfinal.m_name, ClassIsfinal.m_struct);
    }

    /**
     * Tells whether a class is static. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry ClassIsstatic = new GenericMappingEntry("ClassIsstatic", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVClass.fStatic));
    static {
        g_repositoryStringTable.put(ClassIsstatic.m_name, ClassIsstatic.m_struct);
    }

    /**
     * Tells whether a class is strictfp. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry ClassIsstrictfp = new GenericMappingEntry("ClassIsstrictfp", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVClass.fStrictfp));
    static {
        g_repositoryStringTable.put(ClassIsstrictfp.m_name, ClassIsstrictfp.m_struct);
    }

    /**
     * Gets the class' visibility. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for a PRIVATE visibility.<br>
     * - "2" for a PUBLIC visibility.<br>
     * - "3" for a PROTECTED visibility.<br>
     * - "4" for a PACKAGE visibility.<br>
     */
    public static GenericMappingEntry ClassVisibility = new GenericMappingEntry("ClassVisibility", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVClass.fVisibility));
    static {
        g_repositoryStringTable.put(ClassVisibility.m_name, ClassVisibility.m_struct);
    }

    /**
     * Gets the class' package. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ClassPackage = new GenericMappingEntry("ClassPackage", //NOT LOCALIZABLE
            new GenericMappingComposite(DbSMSPackage.metaClass));
    static {
        g_repositoryStringTable.put(ClassPackage.m_name, ClassPackage.m_struct);
    }

    /**
     * Lists all the constructors throwing this exception. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ClassThrowingConstructors = new GenericMappingEntry(
            "ClassThrowingConstructors", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbJVClass.fThrowingConstructors, DbJVConstructor.metaClass));
    static {
        g_repositoryStringTable.put(ClassThrowingConstructors.m_name,
                ClassThrowingConstructors.m_struct);
    }

    /**
     * Lists all the methods throwing this exception. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ClassThrowingMethods = new GenericMappingEntry(
            "ClassThrowingMethods", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbJVClass.fThrowingMethods, DbJVMethod.metaClass));
    static {
        g_repositoryStringTable.put(ClassThrowingMethods.m_name, ClassThrowingMethods.m_struct);
    }

    /**
     * Gets the superclass or the superinterface of an inheritance link. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     * 
     * @deprecated
     * @see org.modelsphere.sms.plugins.generic.GenericMapping#InheritanceSuperclassifier
     */
    public static GenericMappingEntry InheritanceSuperadt = new GenericMappingEntry(
            "InheritanceSuperadt", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbJVInheritance.fSuperClass));
    static {
        g_repositoryStringTable.put(InheritanceSuperadt.m_name, InheritanceSuperadt.m_struct);
    }

    /**
     * Gets the superclass or the superinterface of an inheritance link. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry InheritanceSuperclassifier = new GenericMappingEntry(
            "InheritanceSuperclassifier", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbJVInheritance.fSuperClass));
    static {
        g_repositoryStringTable.put(InheritanceSuperclassifier.m_name,
                InheritanceSuperclassifier.m_struct);
    }

    /**
     * Gets the subclass or the subinterface on an inheritance link. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     * 
     * @deprecated
     * @see org.modelsphere.sms.plugins.generic.GenericMapping#InheritanceSubclassifier
     */
    public static GenericMappingEntry InheritanceSubadt = new GenericMappingEntry(
            "InheritanceSubadt", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbJVInheritance.fSubClass));
    static {
        g_repositoryStringTable.put(InheritanceSubadt.m_name, InheritanceSubadt.m_struct);
    }

    /**
     * Gets the subclass or the subinterface on an inheritance link. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry InheritanceSubclassifier = new GenericMappingEntry(
            "InheritanceSubclassifier", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbJVInheritance.fSubClass));
    static {
        g_repositoryStringTable.put(InheritanceSubclassifier.m_name,
                InheritanceSubclassifier.m_struct);
    }

    /**
     * Tells whether the field is final. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry FieldIsfinal = new GenericMappingEntry("FieldIsfinal", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVDataMember.fFinal));
    static {
        g_repositoryStringTable.put(FieldIsfinal.m_name, FieldIsfinal.m_struct);
    }

    /**
     * Gets the field's initial value. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry FieldInitvalue = new GenericMappingEntry("FieldInitvalue", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVDataMember.fInitialValue));
    static {
        g_repositoryStringTable.put(FieldInitvalue.m_name, FieldInitvalue.m_struct);
    }

    /**
     * Tells whether the field is static. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry FieldIsstatic = new GenericMappingEntry("FieldIsstatic", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVDataMember.fStatic));
    static {
        g_repositoryStringTable.put(FieldIsstatic.m_name, FieldIsstatic.m_struct);
    }

    /**
     * Tells whether the field is transient. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry FieldIstransient = new GenericMappingEntry(
            "FieldIstransient", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVDataMember.fTransient));
    static {
        g_repositoryStringTable.put(FieldIstransient.m_name, FieldIstransient.m_struct);
    }

    /**
     * Gets the field's type use. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing its type use, like "[]", "[4][2]".<br>
     */
    public static GenericMappingEntry FieldTypeuse = new GenericMappingEntry("FieldTypeuse", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVDataMember.fTypeUse));
    static {
        g_repositoryStringTable.put(FieldTypeuse.m_name, FieldTypeuse.m_struct);
    }

    /**
     * Gets the field's type use style. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" if the type use is declared after the type, like "char[] buffer".<br>
     * - "2" if the type use is declared after the field, like "char buffer[].<br>
     */
    public static GenericMappingEntry FieldTypeusestyle = new GenericMappingEntry(
            "FieldTypeusestyle", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVDataMember.fTypeUseStyle));
    static {
        g_repositoryStringTable.put(FieldTypeusestyle.m_name, FieldTypeusestyle.m_struct);
    }

    /**
     * Gets the field's visibility. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for a PRIVATE visibility.<br>
     * - "2" for a PUBLIC visibility.<br>
     * - "3" for a PROTECTED visibility.<br>
     * - "4" for a PACKAGE visibility.<br>
     */
    public static GenericMappingEntry FieldVisibility = new GenericMappingEntry("FieldVisibility", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVDataMember.fVisibility));
    static {
        g_repositoryStringTable.put(FieldVisibility.m_name, FieldVisibility.m_struct);
    }

    /**
     * Tells whether the field is volatile. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry FieldIsvolatile = new GenericMappingEntry("FieldIsvolatile", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVDataMember.fVolatile));
    static {
        g_repositoryStringTable.put(FieldIsvolatile.m_name, FieldIsvolatile.m_struct);
    }

    /**
     * Gets the field's type. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry FieldType = new GenericMappingEntry("FieldType", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbJVDataMember.fType));
    static {
        g_repositoryStringTable.put(FieldType.m_name, FieldType.m_struct);
    }

    /**
     * Gets the field's element type. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry FieldElementtype = new GenericMappingEntry(
            "FieldElementtype", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbJVDataMember.fElementType));
    static {
        g_repositoryStringTable.put(FieldElementtype.m_name, FieldElementtype.m_struct);
    }

    /**
     * Gets the association end associated to a field, if any. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry FieldAssociationend = new GenericMappingEntry(
            "FieldAssociationend", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbJVDataMember.fAssociationEnd));
    static {
        g_repositoryStringTable.put(FieldAssociationend.m_name, FieldAssociationend.m_struct);
    }

    /**
     * Tells whether the initialization block is static. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry InitblockIsstatic = new GenericMappingEntry(
            "InitblockIsstatic", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVInitBlock.fStatic));
    static {
        g_repositoryStringTable.put(InitblockIsstatic.m_name, InitblockIsstatic.m_struct);
    }

    /**
     * Gets the initialization block's body. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry InitblockBody = new GenericMappingEntry("InitblockBody", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVInitBlock.fBody));
    static {
        g_repositoryStringTable.put(InitblockBody.m_name, InitblockBody.m_struct);
    }

    /**
     * Gets the constructor's visibility. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for a PRIVATE visibility.<br>
     * - "2" for a PUBLIC visibility.<br>
     * - "3" for a PROTECTED visibility.<br>
     * - "4" for a PACKAGE visibility.<br>
     */
    public static GenericMappingEntry ConstructorVisibility = new GenericMappingEntry(
            "ConstructorVisibility", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVConstructor.fVisibility));
    static {
        g_repositoryStringTable.put(ConstructorVisibility.m_name, ConstructorVisibility.m_struct);
    }

    /**
     * Lists all the constructor's exceptions. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ConstructorExceptions = new GenericMappingEntry(
            "ConstructorExceptions", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbJVConstructor.fJavaExceptions));
    static {
        g_repositoryStringTable.put(ConstructorExceptions.m_name, ConstructorExceptions.m_struct);
    }

    /**
     * Gets the constructor's body. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry ConstructorBody = new GenericMappingEntry("ConstructorBody", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVConstructor.fBody));
    static {
        g_repositoryStringTable.put(ConstructorBody.m_name, ConstructorBody.m_struct);
    }

    /**
     * Lists all the constructor's parameters. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ConstructorParameters = new GenericMappingEntry(
            "ConstructorParameters", //NOT LOCALIZABLE
            new GenericMappingComponents(DbJVParameter.metaClass));
    static {
        g_repositoryStringTable.put(ConstructorParameters.m_name, ConstructorParameters.m_struct);
    }

    /**
     * Lists all the method's parameters. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry MethodParameters = new GenericMappingEntry(
            "MethodParameters", //NOT LOCALIZABLE
            new GenericMappingComponents(DbJVParameter.metaClass));
    static {
        g_repositoryStringTable.put(MethodParameters.m_name, MethodParameters.m_struct);
    }

    /**
     * Lists all the method's exceptions. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry MethodExceptions = new GenericMappingEntry(
            "MethodExceptions", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbJVMethod.fJavaExceptions));
    static {
        g_repositoryStringTable.put(MethodExceptions.m_name, MethodExceptions.m_struct);
    }

    /**
     * Tells whether the method is abstract. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry MethodIsabstract = new GenericMappingEntry(
            "MethodIsabstract", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVMethod.fAbstract));
    static {
        g_repositoryStringTable.put(MethodIsabstract.m_name, MethodIsabstract.m_struct);
    }

    /**
     * Gets the method's body. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry MethodBody = new GenericMappingEntry("MethodBody", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVMethod.fBody));
    static {
        g_repositoryStringTable.put(MethodBody.m_name, MethodBody.m_struct);
    }

    /**
     * Tells whether the method is final. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry MethodIsfinal = new GenericMappingEntry("MethodIsfinal", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVMethod.fFinal));
    static {
        g_repositoryStringTable.put(MethodIsfinal.m_name, MethodIsfinal.m_struct);
    }

    /**
     * Tells whether the method is native. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry MethodIsnative = new GenericMappingEntry("MethodIsnative", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVMethod.fNative));
    static {
        g_repositoryStringTable.put(MethodIsnative.m_name, MethodIsnative.m_struct);
    }

    /**
     * Tells whether the method is static. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry MethodIsstatic = new GenericMappingEntry("MethodIsstatic", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVMethod.fStatic));
    static {
        g_repositoryStringTable.put(MethodIsstatic.m_name, MethodIsstatic.m_struct);
    }

    /**
     * Tells whether the method is synchronized. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry MethodIssynchronized = new GenericMappingEntry(
            "MethodIssynchronized", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVMethod.fSynchronized));
    static {
        g_repositoryStringTable.put(MethodIssynchronized.m_name, MethodIssynchronized.m_struct);
    }

    /**
     * Tells whether the method is strictfp. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry MethodIsstrictfp = new GenericMappingEntry(
            "MethodIsstrictfp", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVMethod.fStrictfp));
    static {
        g_repositoryStringTable.put(MethodIsstrictfp.m_name, MethodIsstrictfp.m_struct);
    }

    /**
     * Gets the method's visibility. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for a PRIVATE visibility.<br>
     * - "2" for a PUBLIC visibility.<br>
     * - "3" for a PROTECTED visibility.<br>
     * - "4" for a PACKAGE visibility.<br>
     */
    public static GenericMappingEntry MethodVisibility = new GenericMappingEntry(
            "MethodVisibility", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVMethod.fVisibility));
    static {
        g_repositoryStringTable.put(MethodVisibility.m_name, MethodVisibility.m_struct);
    }

    /**
     * Gets the method's return type. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry MethodType = new GenericMappingEntry("MethodType", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbJVMethod.fReturnType));
    static {
        g_repositoryStringTable.put(MethodType.m_name, MethodType.m_struct);
    }

    /**
     * Gets the method's return type use. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string like "[]", "[8]", "[8][4]"..
     */
    public static GenericMappingEntry MethodTypeuse = new GenericMappingEntry("MethodTypeuse", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVMethod.fTypeUse));
    static {
        g_repositoryStringTable.put(MethodTypeuse.m_name, MethodTypeuse.m_struct);
    }

    /**
     * Gets the methodd's type use style. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" if the type use is declared after the type, like "char[] method()".<br>
     * - "2" if the type use is declared after the method, like "char method()[].<br>
     */
    public static GenericMappingEntry MethodTypeusestyle = new GenericMappingEntry(
            "MethodTypeusestyle", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVMethod.fTypeUseStyle));
    static {
        g_repositoryStringTable.put(MethodTypeusestyle.m_name, FieldTypeusestyle.m_struct);
    }

    /**
     * Lists all the method's overriding methods. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry MethodOverridingmethods = new GenericMappingEntry(
            "MethodOverridingmethods", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbJVMethod.fOverridingFeatures));
    static {
        g_repositoryStringTable.put(MethodOverridingmethods.m_name,
                MethodOverridingmethods.m_struct);
    }

    /**
     * Lists all the method's overridden methods. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry MethodOverriddenmethods = new GenericMappingEntry(
            "MethodOverriddenmethods", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbJVMethod.fOverriddenFeatures));
    static {
        g_repositoryStringTable.put(MethodOverriddenmethods.m_name,
                MethodOverriddenmethods.m_struct);
    }

    /**
     * Gets the parameter's type. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ParameterType = new GenericMappingEntry("ParameterType", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbJVParameter.fType));
    static {
        g_repositoryStringTable.put(ParameterType.m_name, ParameterType.m_struct);
    }

    /**
     * Gets the parameter's type use. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing its type use, like "[]", "[4][2]".<br>
     */
    public static GenericMappingEntry ParameterTypeuse = new GenericMappingEntry(
            "ParameterTypeuse", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVParameter.fTypeUse));
    static {
        g_repositoryStringTable.put(ParameterTypeuse.m_name, ParameterTypeuse.m_struct);
    }

    /**
     * Gets the parameter's type use style. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" if the type use is declared after the type, like "char[] buffer".<br>
     * - "2" if the type use is declared after the field, like "char buffer[].<br>
     */
    public static GenericMappingEntry ParameterTypeusestyle = new GenericMappingEntry(
            "ParameterTypeusestyle", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVParameter.fTypeUseStyle));
    static {
        g_repositoryStringTable.put(ParameterTypeusestyle.m_name, ParameterTypeusestyle.m_struct);
    }

    /**
     * Tells whether the parameter is final. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry ParameterIsfinal = new GenericMappingEntry(
            "ParameterIsfinal", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVParameter.fFinal));
    static {
        g_repositoryStringTable.put(ParameterIsfinal.m_name, ParameterIsfinal.m_struct);
    }

    /**
     * Gets the parameter's passing convention. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * 
     * @deprecated
     */
    public static GenericMappingEntry ParameterPassingconvention = new GenericMappingEntry(
            "ParameterPassingconvention", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVParameter.fPassingConvention));
    static {
        g_repositoryStringTable.put(ParameterPassingconvention.m_name,
                ParameterPassingconvention.m_struct);
    }

    /**
     * Gets the parameter's default value. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry ParameterDefaultvalue = new GenericMappingEntry(
            "ParameterDefaultvalue", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVParameter.fDefaultValue));
    static {
        g_repositoryStringTable.put(ParameterDefaultvalue.m_name, ParameterDefaultvalue.m_struct);
    }

    /**
     * Gets the two association ends of a given association. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry AssociationAssociationends = new GenericMappingEntry(
            "AssociationAssociationends", //NOT LOCALIZABLE
            new GenericMappingComponents(DbJVAssociationEnd.metaClass));
    static {
        g_repositoryStringTable.put(AssociationAssociationends.m_name,
                AssociationAssociationends.m_struct);
    }

    /**
     * Tells whether the association end is navigable. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry AssociationendIsnavigable = new GenericMappingEntry(
            "AssociationendIsnavigable", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVAssociationEnd.fNavigable));
    static {
        g_repositoryStringTable.put(AssociationendIsnavigable.m_name,
                AssociationendIsnavigable.m_struct);
    }

    /**
     * Tells whether the association end is ordered. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry AssociationendIsordered = new GenericMappingEntry(
            "AssociationendIsordered", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVAssociationEnd.fOrdered));
    static {
        g_repositoryStringTable.put(AssociationendIsordered.m_name,
                AssociationendIsordered.m_struct);
    }

    /**
     * Gets the association end's aggregation type. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for NONE (association without aggregation).<br>
     * - "2" for AGGREGATE (aggregation association).<br>
     * - "3" for COMPOSITE (composition association).<br>
     */
    public static GenericMappingEntry AssociationendAggregation = new GenericMappingEntry(
            "AssociationendAggregation", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVAssociationEnd.fAggregation));
    static {
        g_repositoryStringTable.put(AssociationendAggregation.m_name,
                AssociationendAggregation.m_struct);
    }

    /**
     * Gets the association end's multiplicity. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "0" for an UNDEFINED multiplicity.<br>
     * - "1" for a MANY (0..*) multiplicity.<br>
     * - "2" for an ONE_OR_MORE (1..*) multiplicity.<br>
     * - "3" for an EXACTLY_ONE (1) multiplicity.<br>
     * - "4" for an OPTIONAL (0..1) multiplicity.<br>
     */
    public static GenericMappingEntry AssociationendMultiplicity = new GenericMappingEntry(
            "AssociationendMultiplicity", //NOT LOCALIZABLE
            new GenericMappingProperty(DbJVAssociationEnd.fMultiplicity));
    static {
        g_repositoryStringTable.put(AssociationendMultiplicity.m_name,
                AssociationendMultiplicity.m_struct);
    }

    /**
     * Gets the association end's type. <br>
     * Target System: <b>Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry AssociationendType = new GenericMappingEntry(
            "AssociationendType", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbJVAssociationEnd.fAssociationMember));
    static {
        g_repositoryStringTable.put(AssociationendType.m_name, AssociationendType.m_struct);
    }

    /**
     * <b> SECTION 4: RELATIONAL CONCEPTS (FOR ALL TARGET SYSTEMS EXCEPT JAVA). </b> <br>
     * <br>
     * List of relational concepts:<br>
     * - Datamodel: the ultimate container of all relational concepts.<br>
     * - Diagram: a graphical representation of a data model.<br>
     * - Graphicaltable: a graphical representation of relational table.<br>
     * - Database: the physical storage of relational tables.<br>
     * - Table: a relational table.<br>
     * - Column: a table's column.<br>
     * - Association: an association link between two tables.<br>
     * - Associationrole: one of the two ends of an association.<br>
     * - Associationend: idem.<br>
     * - Trigger: an action called at the creation, deletion or modification of a relational table.<br>
     * - Index: an index specified on a given table.<br>
     * - Indexkey: a link between an index and a table column.<br>
     * - Primaryuniquekey:either a primary or a unique key.<br>
     * - Foreignkey: a key referencing another table's primary or unique key.<br>
     * - Fkeycolumn: a link between a foreign key and a table's column.<br>
     * - Check: a check constraint defined of a given table.<br>
     */
    //Rgtables, View,
    public static Section Section_4;

    /**
     * Datamodel: The ultimate container of all relational concepts.<br>
     * <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Concept</b><br>
     */
    public static GenericMappingEntry Datamodel = new GenericMappingEntry("Datamodel", //NOT LOCALIZABLE
            new GenericMappingConcept(DbORDataModel.class, DbORDataModel.metaClass));
    static {
        g_repositoryStringTable.put(Datamodel.m_name, Datamodel.m_struct);
    }

    /**
     * Lists all the tables contained under a data model. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DatamodelTables = new GenericMappingEntry("DatamodelTables", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORTable.metaClass));
    static {
        g_repositoryStringTable.put(DatamodelTables.m_name, DatamodelTables.m_struct);
    }

    /**
     * Lists all the views contained under a data model. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DatamodelViews = new GenericMappingEntry("DatamodelViews", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORView.metaClass));
    static {
        g_repositoryStringTable.put(DatamodelViews.m_name, DatamodelViews.m_struct);
    }

    /**
     * Lists all the diagrams contained under a data model. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DatamodelDiagrams = new GenericMappingEntry(
            "DatamodelDiagrams", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORDiagram.metaClass));
    static {
        g_repositoryStringTable.put(DatamodelDiagrams.m_name, DatamodelDiagrams.m_struct);
    }

    /**
     * Lists all the graphical tables contained under a diagram. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DiagramGraphicaltables = new GenericMappingEntry(
            "DiagramGraphicaltables", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORTableGo.metaClass));
    static {
        g_repositoryStringTable.put(DiagramGraphicaltables.m_name, DiagramGraphicaltables.m_struct);
    }

    /**
     * Get the semantic table associated to a graphical table. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry GraphicaltableSemantictable = new GenericMappingEntry(
            "GraphicaltableSemantictable", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORTableGo.fClassifier));
    static {
        g_repositoryStringTable.put(GraphicaltableSemantictable.m_name,
                GraphicaltableSemantictable.m_struct);
    }

    /**
     * Get the semantic table asociated to a graphical table. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     * 
     * @depreated
     * @see org.modelsphere.sms.plugins.generic.GenericMapping#GraphicaltableSemantictable
     */
    public static GenericMappingEntry GraphicaltableSemanticaltable = new GenericMappingEntry(
            "GraphicaltableSemanticaltable", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORTableGo.fClassifier));
    static {
        g_repositoryStringTable.put(GraphicaltableSemanticaltable.m_name,
                GraphicaltableSemanticaltable.m_struct);
    }

    /**
     * Gets the version of a data model. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry DatamodelVersion = new GenericMappingEntry(
            "DatamodelVersion", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORDataModel.fVersion));
    static {
        g_repositoryStringTable.put(DatamodelVersion.m_name, DatamodelVersion.m_struct);
    }

    /**
     * Gets the author of a data model. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry DatamodelAuthor = new GenericMappingEntry("DatamodelAuthor", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORDataModel.fAuthor));
    static {
        g_repositoryStringTable.put(DatamodelAuthor.m_name, DatamodelAuthor.m_struct);
    }

    /**
     * Gets the target system associated to a data model. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DatamodelTargetsystem = new GenericMappingEntry(
            "DatamodelTargetsystem", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORDataModel.fTargetSystem));
    static {
        g_repositoryStringTable.put(DatamodelTargetsystem.m_name, DatamodelTargetsystem.m_struct);
    }

    /**
     * Lists all the data models associated to a database. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     * 
     * @deprecated
     * @see org.modelsphere.sms.plugins.generic.GenericMapping#DatabaseSchema
     */
    public static GenericMappingEntry DatabaseDataModels = new GenericMappingEntry(
            "DatabaseDataModels", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORDatabase.fSchema));
    static {
        g_repositoryStringTable.put(DatabaseDataModels.m_name, DatabaseDataModels.m_struct);
    }

    /**
     * Gets the schema (a data model) associated to a database. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DatabaseSchema = new GenericMappingEntry("DatabaseSchema", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORDatabase.fSchema));
    static {
        g_repositoryStringTable.put(DatabaseSchema.m_name, DatabaseSchema.m_struct);
    }

    /**
     * Lists all the operation libraries associated to a database. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     * 
     * @deprecated
     * @see org.modelsphere.sms.plugins.generic.GenericMapping#DatabaseOperationLibrary
     */
    public static GenericMappingEntry DatabaseOperationLibraries = new GenericMappingEntry(
            "DatabaseOperationLibraries", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORDatabase.fOperationLibrary));
    static {
        g_repositoryStringTable.put(DatabaseOperationLibraries.m_name,
                DatabaseOperationLibraries.m_struct);
    }

    /**
     * Gets the operation library associated to a database. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DatabaseOperationLibrary = new GenericMappingEntry(
            "DatabaseOperationLibrary", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORDatabase.fOperationLibrary));
    static {
        g_repositoryStringTable.put(DatabaseOperationLibrary.m_name,
                DatabaseOperationLibrary.m_struct);
    }

    /**
     * Table: a relational table.<br>
     * <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Concept</b><br>
     */
    public static GenericMappingEntry Table = new GenericMappingEntry("Table", //NOT LOCALIZABLE
            new GenericMappingConcept(DbORTable.class, DbORTable.metaClass));
    static {
        g_repositoryStringTable.put(Table.m_name, Table.m_struct);
    }

    /**
     * Gets the alias of a table. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry TableAlias = new GenericMappingEntry("TableAlias", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORTable.fAlias));
    static {
        g_repositoryStringTable.put(TableAlias.m_name, TableAlias.m_struct);
    }

    /**
     * Gets the name of a table. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry TableName = new GenericMappingEntry("TableName", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORTable.fName));
    static {
        g_repositoryStringTable.put(TableName.m_name, TableName.m_struct);
    }

    /**
     * Gets the physical name of a table. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry TablePhysicalname = new GenericMappingEntry(
            "TablePhysicalname", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORTable.fPhysicalName));
    static {
        g_repositoryStringTable.put(TablePhysicalname.m_name, TablePhysicalname.m_struct);
    }

    /**
     * Gets the description of a table. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry TableDescription = new GenericMappingEntry(
            "TableDescription", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORTable.fDescription));
    static {
        g_repositoryStringTable.put(TableDescription.m_name, TableDescription.m_struct);
    }

    /**
     * Gets the user who owns a table, if any. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry TableUser = new GenericMappingEntry("TableUser", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORTable.fUser));
    static {
        g_repositoryStringTable.put(TableUser.m_name, TableUser.m_struct);
    }

    /**
     * Lists all the columns contained under a table. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     * Note: The columns are ordered by their sequence.<br>
     */
    public static GenericMappingEntry TableColumns = new GenericMappingEntry("TableColumns", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORColumn.metaClass));
    static {
        g_repositoryStringTable.put(TableColumns.m_name, TableColumns.m_struct);
    }

    /**
     * Lists all the indexes contained under a table. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     * Note: The indexes are ordered by their sequence.<br>
     */
    public static GenericMappingEntry TableIndexes = new GenericMappingEntry("TableIndexes", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORIndex.metaClass));
    static {
        g_repositoryStringTable.put(TableIndexes.m_name, TableIndexes.m_struct);
    }

    /**
     * Lists all the triggers contained under a table. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     * Note: The triggers are ordered by their sequence.<br>
     */
    public static GenericMappingEntry TableTriggers = new GenericMappingEntry("TableTriggers", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORTrigger.metaClass));
    static {
        g_repositoryStringTable.put(TableTriggers.m_name, TableTriggers.m_struct);
    }

    /**
     * Gets the unlimited extents value of a table or nested storage table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry TableUnlimitedextents = new GenericMappingEntry(
            "TableUnlimitedextents", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAAbsTable.fUnlimitedExtents));
    static {
        g_repositoryStringTable.put(TableUnlimitedextents.m_name, TableUnlimitedextents.m_struct);
    }

    /**
     * Lists all the constraints contained under a table. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     * Note: The constraints are ordered by their sequence.<br>
     */
    public static GenericMappingEntry TableConstraints = new GenericMappingEntry(
            "TableConstraints", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORCheck.metaClass));
    static {
        g_repositoryStringTable.put(TableConstraints.m_name, TableConstraints.m_struct);
    }

    /**
     * Lists all the primary and unique keys contained under a table. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     * Note: The primary and unique keys are ordered by their sequence.<br>
     */
    public static GenericMappingEntry TablePrimaryuniquekeys = new GenericMappingEntry(
            "TablePrimaryuniquekeys", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORPrimaryUnique.metaClass));
    static {
        g_repositoryStringTable.put(TablePrimaryuniquekeys.m_name, TablePrimaryuniquekeys.m_struct);
    }

    /**
     * Lists all the foreign keys contained under a table. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     * Note: The foreign keys are ordered by their sequence.<br>
     */
    public static GenericMappingEntry TableForeignkeys = new GenericMappingEntry(
            "TableForeignkeys", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORForeign.metaClass));
    static {
        g_repositoryStringTable.put(TableForeignkeys.m_name, TableForeignkeys.m_struct);
    }

    //Combination related features
    /**
     * Repository Function <code>CombinationColumn</code> Description : From the current combination
     * column link, navigate to its column
     */
    //Combination Colum for Index
    //    g_repositoryStringTable.put("CombinationColumn",
    //      new GenericMappingStructure(DbORIndexKey.fIndexedElement, DbORColumn.metaClass));
    //g_repositoryStringTable.put("TableChoices",
    //  new GenericMappingStructure(DbGETable.fComponents, DbGECheck.metaClass));
    //TableAlternateCombination
    /**
     * Gets the actual table associated to a table's graphical representation. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry RgTablesTable = new GenericMappingEntry("RgTablesTable", //NOT LOCALIZABLE
            new GenericMappingComponents(DbSMSClassifierGo.metaClass));
    static {
        g_repositoryStringTable.put(RgTablesTable.m_name, RgTablesTable.m_struct);
    }

    /**
     * Gets the selection rule of a table. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Note: Applicable on views only.<br>
     * Returns: String<br>
     * 
     * @deprecated
     * @see org.modelsphere.sms.plugins.generic.GenericMapping#ViewSelectionrule
     */
    public static GenericMappingEntry TableSelectionrule = new GenericMappingEntry(
            "TableSelectionrule", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAbsTable.fSelectionRule));
    static {
        g_repositoryStringTable.put(TableSelectionrule.m_name, TableSelectionrule.m_struct);
    }

    /**
     * View: a relational view.<br>
     * <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Concept</b><br>
     */
    public static GenericMappingEntry View = new GenericMappingEntry("View", //NOT LOCALIZABLE
            new GenericMappingConcept(DbORView.class, DbORView.metaClass));
    static {
        g_repositoryStringTable.put(View.m_name, View.m_struct);
    }

    /**
     * Gets the selection rule of a view. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry ViewSelectionrule = new GenericMappingEntry(
            "ViewSelectionrule", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAbsTable.fSelectionRule));
    static {
        g_repositoryStringTable.put(ViewSelectionrule.m_name, ViewSelectionrule.m_struct);
    }

    /**
     * Lists all the columns contained under a view. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ViewColumns = new GenericMappingEntry("ViewColumns", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORColumn.metaClass));
    static {
        g_repositoryStringTable.put(ViewColumns.m_name, ViewColumns.m_struct);
    }

    /**
     * Column: a table's column.<br>
     * <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Concept</b><br>
     */
    public static GenericMappingEntry Column = new GenericMappingEntry("Column", //NOT LOCALIZABLE
            new GenericMappingConcept(DbORColumn.class, DbORColumn.metaClass));
    static {
        g_repositoryStringTable.put(Column.m_name, Column.m_struct);
    }

    /**
     * Gets the alias of a column. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry ColumnAlias = new GenericMappingEntry("ColumnAlias", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORColumn.fAlias));
    static {
        g_repositoryStringTable.put(ColumnAlias.m_name, ColumnAlias.m_struct);
    }

    /**
     * Gets the name of a column. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry ColumnName = new GenericMappingEntry("ColumnName", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORColumn.fName));
    static {
        g_repositoryStringTable.put(ColumnName.m_name, ColumnName.m_struct);
    }

    /**
     * Gets the physical name of a column. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry ColumnPhysicalname = new GenericMappingEntry(
            "ColumnPhysicalname", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORColumn.fPhysicalName));
    static {
        g_repositoryStringTable.put(ColumnPhysicalname.m_name, ColumnPhysicalname.m_struct);
    }

    /**
     * Gets the description of a column. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry ColumnDescription = new GenericMappingEntry(
            "ColumnDescription", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORColumn.fDescription));
    static {
        g_repositoryStringTable.put(ColumnDescription.m_name, ColumnDescription.m_struct);
    }

    /**
     * Retrieves the table to which a column belongs. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ColumnTable = new GenericMappingEntry("ColumnTable", //NOT LOCALIZABLE
            new GenericMappingComposite(DbORTable.metaClass));
    static {
        g_repositoryStringTable.put(ColumnTable.m_name, ColumnTable.m_struct);
    }

    /**
     * Retrieves the table to which a column belongs. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ColumnView = new GenericMappingEntry("ColumnView", //NOT LOCALIZABLE
            new GenericMappingComposite(DbORView.metaClass));
    static {
        g_repositoryStringTable.put(ColumnView.m_name, ColumnView.m_struct);
    }

    /**
     * Gets the data type of a column. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ColumnType = new GenericMappingEntry("ColumnType", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORColumn.fType));
    static {
        g_repositoryStringTable.put(ColumnType.m_name, ColumnType.m_struct);
    }

    /**
     * Gets the built-in type of a column, if any. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ColumnBuiltintype = new GenericMappingEntry(
            "ColumnBuiltintype", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORColumn.fType, DbORBuiltInType.metaClass));
    static {
        g_repositoryStringTable.put(ColumnBuiltintype.m_name, ColumnBuiltintype.m_struct);
    }

    /**
     * Gets the domain of a column, if any. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ColumnDomain = new GenericMappingEntry("ColumnDomain", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORColumn.fType, DbORDomain.metaClass));
    static {
        g_repositoryStringTable.put(ColumnDomain.m_name, ColumnDomain.m_struct);
    }

    /**
     * Lists all the constraints associated to a column. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ColumnConstraints = new GenericMappingEntry(
            "ColumnConstraints", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbORColumn.fChecks));
    static {
        g_repositoryStringTable.put(ColumnConstraints.m_name, ColumnConstraints.m_struct);
    }

    /**
     * Gets the common item of a column <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ColumnCommonitem = new GenericMappingEntry(
            "ColumnCommonitem", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORColumn.fCommonItem));
    static {
        g_repositoryStringTable.put(ColumnCommonitem.m_name, ColumnCommonitem.m_struct);
    }

    /**
     * Tells whether a column can be set to NULL. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry ColumnIsnullpossible = new GenericMappingEntry(
            "ColumnIsnullpossible", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORColumn.fNull));
    static {
        g_repositoryStringTable.put(ColumnIsnullpossible.m_name, ColumnIsnullpossible.m_struct);
    }

    /**
     * Returns the column's length. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry ColumnLength = new GenericMappingEntry("ColumnLength", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORColumn.fLength));
    static {
        g_repositoryStringTable.put(ColumnLength.m_name, ColumnLength.m_struct);
    }

    /**
     * Returns the column's decimal number. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry ColumnDecimal = new GenericMappingEntry("ColumnDecimal", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORColumn.fNbDecimal));
    static {
        g_repositoryStringTable.put(ColumnDecimal.m_name, ColumnDecimal.m_struct);
    }

    /**
     * Returns the column's initial value. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String.<br>
     */
    public static GenericMappingEntry ColumnDefault = new GenericMappingEntry("ColumnDefault", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORColumn.fInitialValue));
    static {
        g_repositoryStringTable.put(ColumnDefault.m_name, ColumnDefault.m_struct);
    }

    /**
     * Returns the column's reference. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry ColumnReference = new GenericMappingEntry("ColumnReference", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORColumn.fReference));
    static {
        g_repositoryStringTable.put(ColumnReference.m_name, ColumnReference.m_struct);
    }

    /**
     * Lists all the associations contained under a data model. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DatamodelAssociations = new GenericMappingEntry(
            "DatamodelAssociations", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORAssociation.metaClass));
    static {
        g_repositoryStringTable.put(DatamodelAssociations.m_name, DatamodelAssociations.m_struct);
    }

    /**
     * Gets the name of an association. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String.<br>
     */
    public static GenericMappingEntry AssociationName = new GenericMappingEntry("AssociationName", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAssociation.fName));
    static {
        g_repositoryStringTable.put(AssociationName.m_name, AssociationName.m_struct);
    }

    /**
     * Gets the two ends of an association. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry AssociationRoles = new GenericMappingEntry(
            "AssociationRoles", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORAssociationEnd.metaClass));
    static {
        g_repositoryStringTable.put(AssociationRoles.m_name, AssociationRoles.m_struct);
    }

    /**
     * Tells whether an association end is navigable. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry AssociationroleIsnavigable = new GenericMappingEntry(
            "AssociationroleIsnavigable", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAssociationEnd.fNavigable));
    static {
        g_repositoryStringTable.put(AssociationroleIsnavigable.m_name,
                AssociationroleIsnavigable.m_struct);
    }

    /**
     * Gets the insert rule of an association end. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String.<br>
     */
    public static GenericMappingEntry AssociationroleInsertrule = new GenericMappingEntry(
            "AssociationroleInsertrule", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAssociationEnd.fInsertRule));
    static {
        g_repositoryStringTable.put(AssociationroleInsertrule.m_name,
                AssociationroleInsertrule.m_struct);
    }

    /**
     * Gets the update rule of an association end. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String.<br>
     */
    public static GenericMappingEntry AssociationroleUpdaterule = new GenericMappingEntry(
            "AssociationroleUpdaterule", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAssociationEnd.fUpdateRule));
    static {
        g_repositoryStringTable.put(AssociationroleUpdaterule.m_name,
                AssociationroleUpdaterule.m_struct);
    }

    /**
     * Gets the delete rule of an association end. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String.<br>
     */
    public static GenericMappingEntry AssociationroleDeleterule = new GenericMappingEntry(
            "AssociationroleDeleterule", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAssociationEnd.fDeleteRule));
    static {
        g_repositoryStringTable.put(AssociationroleDeleterule.m_name,
                AssociationroleDeleterule.m_struct);
    }

    /**
     * Gets the classifier associated to an association end. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     */
    public static GenericMappingEntry AssociationroleClassifier = new GenericMappingEntry(
            "AssociationroleClassifier", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORAssociationEnd.fClassifier));
    static {
        g_repositoryStringTable.put(AssociationroleClassifier.m_name,
                AssociationroleClassifier.m_struct);
    }

    /**
     * Trigger: an action called at the creation, deletion or modification of a relational table.<br>
     * <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Concept</b><br>
     */
    public static GenericMappingEntry Trigger = new GenericMappingEntry("Trigger", //NOT LOCALIZABLE
            new GenericMappingConcept(DbORTrigger.class, DbORTrigger.metaClass));
    static {
        g_repositoryStringTable.put(Trigger.m_name, Trigger.m_struct);
    }

    /**
     * Gets the category of a trigger. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for an insert trigger<br>
     * - "2" for an update trigger<br>
     * - "3" for a delete trigger<br>
     * - "4" for an insert and update trigger<br>
     * - "5" for an insert and delete trigger<br>
     * - "6" for an update and delete trigger<br>
     * - "7" for an insert, update and delete trigger<br>
     */
    public static GenericMappingEntry TriggerCategory = new GenericMappingEntry("TriggerCategory", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORTrigger.fEvent));
    static {
        g_repositoryStringTable.put(TriggerCategory.m_name, TriggerCategory.m_struct);
    }

    /**
     * Gets the new alias of a trigger. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String <br>
     */
    public static GenericMappingEntry TriggerNewalias = new GenericMappingEntry("TriggerNewalias", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORTrigger.fNewAlias));
    static {
        g_repositoryStringTable.put(TriggerNewalias.m_name, TriggerNewalias.m_struct);
    }

    /**
     * Gets the old alias of a trigger. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String <br>
     */
    public static GenericMappingEntry TriggerOldalias = new GenericMappingEntry("TriggerOldalias", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORTrigger.fOldAlias));
    static {
        g_repositoryStringTable.put(TriggerOldalias.m_name, TriggerOldalias.m_struct);
    }

    /**
     * Gets the when condition of a trigger. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String <br>
     */
    public static GenericMappingEntry TriggerWhencondition = new GenericMappingEntry(
            "TriggerWhencondition", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORTrigger.fWhenCondition));
    static {
        g_repositoryStringTable.put(TriggerWhencondition.m_name, TriggerWhencondition.m_struct);
    }

    /**
     * Gets the body of a trigger. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String <br>
     */
    public static GenericMappingEntry TriggerBody = new GenericMappingEntry("TriggerBody", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORTrigger.fBody));
    static {
        g_repositoryStringTable.put(TriggerBody.m_name, TriggerBody.m_struct);
    }

    /**
     * Gets the user who owns a trigger, if any. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry TriggerUser = new GenericMappingEntry("TriggerUser", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORTrigger.fUser));
    static {
        g_repositoryStringTable.put(TriggerUser.m_name, TriggerUser.m_struct);
    }

    /**
     * Lists all the columns associated to a trigger. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry TriggerColumns = new GenericMappingEntry("TriggerColumns", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbORTrigger.fColumns));
    static {
        g_repositoryStringTable.put(TriggerColumns.m_name, TriggerColumns.m_struct);
    }

    /**
     * Index: an index on a table.<br>
     * <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Concept</b><br>
     */
    public static GenericMappingEntry Index = new GenericMappingEntry("Index", //NOT LOCALIZABLE
            new GenericMappingConcept(DbORIndex.class, DbORIndex.metaClass));
    static {
        g_repositoryStringTable.put(Index.m_name, Index.m_struct);
    }

    /**
     * Gets the alias of an index. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String <br>
     */
    public static GenericMappingEntry IndexAlias = new GenericMappingEntry("IndexAlias", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORIndex.fAlias));
    static {
        g_repositoryStringTable.put(IndexAlias.m_name, IndexAlias.m_struct);
    }

    /**
     * Gets the name of an index. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String <br>
     */
    public static GenericMappingEntry IndexName = new GenericMappingEntry("IndexName", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORIndex.fName));
    static {
        g_repositoryStringTable.put(IndexName.m_name, IndexName.m_struct);
    }

    /**
     * Gets the physical name of an index. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String <br>
     */
    public static GenericMappingEntry IndexPhysicalname = new GenericMappingEntry(
            "IndexPhysicalname", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORIndex.fPhysicalName));
    static {
        g_repositoryStringTable.put(IndexPhysicalname.m_name, IndexPhysicalname.m_struct);
    }

    /**
     * Gets the description of an index. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String <br>
     */
    public static GenericMappingEntry IndexDescription = new GenericMappingEntry(
            "IndexDescription", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORIndex.fDescription));
    static {
        g_repositoryStringTable.put(IndexDescription.m_name, IndexDescription.m_struct);
    }

    /**
     * Tells whether an index is unique. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry IndexUnique = new GenericMappingEntry("IndexUnique", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORIndex.fUnique));
    static {
        g_repositoryStringTable.put(IndexUnique.m_name, IndexUnique.m_struct);
    }

    /**
     * Retrieves the table to which an index belongs. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry IndexTable = new GenericMappingEntry("IndexTable", //NOT LOCALIZABLE
            new GenericMappingComposite(DbORTable.metaClass));
    static {
        g_repositoryStringTable.put(IndexTable.m_name, IndexTable.m_struct);
    }

    /**
     * Gets the user who owns an index, if any. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry IndexUser = new GenericMappingEntry("IndexUser", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORIndex.fUser));
    static {
        g_repositoryStringTable.put(IndexUser.m_name, IndexUser.m_struct);
    }

    /**
     * Gets the index's keys. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry IndexIndexkeys = new GenericMappingEntry("IndexIndexkeys", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORIndexKey.metaClass));
    static {
        g_repositoryStringTable.put(IndexIndexkeys.m_name, IndexIndexkeys.m_struct);
    }

    /**
     * Gets the index's constraints. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     * 
     * @deprecated
     * @see org.modelsphere.sms.plugins.generic.GenericMapping#IndexConstraint
     */
    public static GenericMappingEntry IndexKeyExpression = new GenericMappingEntry(
            "IndexKeyExpression", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORIndexKey.fExpression));
    static {
        g_repositoryStringTable.put(IndexKeyExpression.m_name, IndexKeyExpression.m_struct);
    }

    /**
     * Gets the index's constraints. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     * 
     * @deprecated
     * @see org.modelsphere.sms.plugins.generic.GenericMapping#IndexConstraint
     */
    public static GenericMappingEntry IndexConstraints = new GenericMappingEntry(
            "IndexConstraints", //NOT LOCALIZABLE
            new GenericMappingChoice(DbORIndex.fConstraint, DbORConstraint.metaClass));
    static {
        g_repositoryStringTable.put(IndexConstraints.m_name, IndexConstraints.m_struct);
    }

    /**
     * Gets the index's constraint, if any. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry IndexConstraint = new GenericMappingEntry("IndexConstraint", //NOT LOCALIZABLE
            new GenericMappingChoice(DbORIndex.fConstraint, DbORConstraint.metaClass));
    static {
        g_repositoryStringTable.put(IndexConstraint.m_name, IndexConstraint.m_struct);
    }

    /**
     * Gets the column associated to an index key. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry IndexkeyColumn = new GenericMappingEntry("IndexkeyColumn", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORIndexKey.fIndexedElement));
    static {
        g_repositoryStringTable.put(IndexkeyColumn.m_name, IndexkeyColumn.m_struct);
    }

    /**
     * Gets the index key's sort option. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for an ASCendant order<br>
     * - "2' for a DESCendant order<br>
     */
    public static GenericMappingEntry IndexkeySortoption = new GenericMappingEntry(
            "IndexkeySortoption", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORIndexKey.fSortOption));
    static {
        g_repositoryStringTable.put(IndexkeySortoption.m_name, IndexkeySortoption.m_struct);
    }

    /**
     * Primaryuniquekey: either primary or a unique key.<br>
     * <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Concept</b><br>
     */
    public static GenericMappingEntry Primaryuniquekey = new GenericMappingEntry(
            "Primaryuniquekey", //NOT LOCALIZABLE
            new GenericMappingConcept(DbORPrimaryUnique.class, DbORPrimaryUnique.metaClass));
    static {
        g_repositoryStringTable.put(Primaryuniquekey.m_name, Primaryuniquekey.m_struct);
    }

    /**
     * Tells whether a key is a primary key. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" of "false".<br>
     */
    public static GenericMappingEntry PrimaryuniquekeyIsprimary = new GenericMappingEntry(
            "PrimaryuniquekeyIsprimary", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORPrimaryUnique.fPrimary));
    static {
        g_repositoryStringTable.put(PrimaryuniquekeyIsprimary.m_name,
                PrimaryuniquekeyIsprimary.m_struct);
    }

    /**
     * Lists all the columns associated to a primary or a unique key. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry PrimaryuniquekeyColumns = new GenericMappingEntry(
            "PrimaryuniquekeyColumns", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbORPrimaryUnique.fColumns));
    static {
        g_repositoryStringTable.put(PrimaryuniquekeyColumns.m_name,
                PrimaryuniquekeyColumns.m_struct);
    }

    /**
     * Gets the index associated to a primary or a unique key, if any. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry PrimaryuniquekeyIndex = new GenericMappingEntry(
            "PrimaryuniquekeyIndex", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORPrimaryUnique.fIndex));
    static {
        g_repositoryStringTable.put(PrimaryuniquekeyIndex.m_name, PrimaryuniquekeyIndex.m_struct);
    }

    /**
     * Gets the index associated to a foreign key, if any. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ForeignkeyIndex = new GenericMappingEntry("ForeignkeyIndex", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORForeign.fIndex));
    static {
        g_repositoryStringTable.put(ForeignkeyIndex.m_name, ForeignkeyIndex.m_struct);
    }

    /**
     * Lists all the columns associated to a foreign key. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ForeignkeyColumns = new GenericMappingEntry(
            "ForeignkeyColumns", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORFKeyColumn.metaClass));
    static {
        g_repositoryStringTable.put(ForeignkeyColumns.m_name, ForeignkeyColumns.m_struct);
    }

    /**
     * Gets the association end of a foreign key. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ForeignkeyAssociationend = new GenericMappingEntry(
            "ForeignkeyAssociationend", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORForeign.fAssociationEnd));
    static {
        g_repositoryStringTable.put(ForeignkeyAssociationend.m_name,
                ForeignkeyAssociationend.m_struct);
    }

    /**
     * Gets the referenced constraint of a foreign key. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry AssociationendReferencedconstraint = new GenericMappingEntry(
            "AssociationendReferencedconstraint", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORAssociationEnd.fReferencedConstraint));
    static {
        g_repositoryStringTable.put(AssociationendReferencedconstraint.m_name,
                AssociationendReferencedconstraint.m_struct);
    }

    /**
     * Gets the delete rule of an association end. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String.<br>
     */
    public static GenericMappingEntry AssociationendDeleterule = new GenericMappingEntry(
            "AssociationendDeleterule", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAssociationEnd.fDeleteRule));
    static {
        g_repositoryStringTable.put(AssociationendDeleterule.m_name,
                AssociationendDeleterule.m_struct);
    }

    /**
     * Gets the update rule of an association end. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String.<br>
     */
    public static GenericMappingEntry AssociationendUpdaterule = new GenericMappingEntry(
            "AssociationendUpdaterule", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAssociationEnd.fUpdateRule));
    static {
        g_repositoryStringTable.put(AssociationendUpdaterule.m_name,
                AssociationendUpdaterule.m_struct);
    }

    /**
     * Gets the actual column associated to a foreign key's column. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry FkeycolumnColumn = new GenericMappingEntry(
            "FkeycolumnColumn", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORFKeyColumn.fColumn));
    static {
        g_repositoryStringTable.put(FkeycolumnColumn.m_name, FkeycolumnColumn.m_struct);
    }

    public static GenericMappingEntry FkeycolumnSourceColumn = new GenericMappingEntry(
            "FkeycolumnSourceColumn", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORFKeyColumn.fSourceColumn));
    static {
        g_repositoryStringTable.put(FkeycolumnSourceColumn.m_name, FkeycolumnSourceColumn.m_struct);
    }

    /**
     * A check constraint.<br>
     * <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Concept</b><br>
     */
    public static GenericMappingEntry Check = new GenericMappingEntry("Check", //NOT LOCALIZABLE
            new GenericMappingConcept(DbORCheck.class, DbORCheck.metaClass));
    static {
        g_repositoryStringTable.put(Check.m_name, Check.m_struct);
    }

    /**
     * Gets the condition of a check constraint. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String.<br>
     */
    public static GenericMappingEntry CheckCondition = new GenericMappingEntry("CheckCondition", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORCheck.fCondition));
    static {
        g_repositoryStringTable.put(CheckCondition.m_name, CheckCondition.m_struct);
    }

    /**
     * Gets the column associated to a check constraint. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry CheckColumn = new GenericMappingEntry("CheckColumn", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORCheck.fColumn));
    static {
        g_repositoryStringTable.put(CheckColumn.m_name, CheckColumn.m_struct);
    }

    //Choice-related features
    //ChoiceDirections

    //CombinationColumn
    //CombinationForeign
    //ColumnCombinationColumn
    //CombinationColumnCombination
    //CombinationDirection
    //DirectionDestinationTable
    //TablePrimaryCombination
    //CombinationColumns
    //CombinationColumn

    //ConnectorDirections
    //DirectionDestinationTable
    //DirectionCodedName
    //DirectionId
    //DirectionMinConn
    //DirectionMaxConn

    /**
     * Lists all the data models contained under a project. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ProjectDomainmodels = new GenericMappingEntry(
            "ProjectDomainmodels", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORDomainModel.metaClass));
    static {
        g_repositoryStringTable.put(ProjectDomainmodels.m_name, ProjectDomainmodels.m_struct);
    }

    /**
     * Lists all the domains contained under a domain model. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DomainmodelDomains = new GenericMappingEntry(
            "DomainmodelDomains", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORDomain.metaClass));
    static {
        g_repositoryStringTable.put(DomainmodelDomains.m_name, DomainmodelDomains.m_struct);
    }

    /**
     * Gets the source type of a domain. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DomainSourcetype = new GenericMappingEntry(
            "DomainSourcetype", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORDomain.fSourceType));
    static {
        g_repositoryStringTable.put(DomainSourcetype.m_name, DomainSourcetype.m_struct);
    }

    /**
     * Gets the source type of a domain, if the source type is itself a domain. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DomainSourcedomain = new GenericMappingEntry(
            "DomainSourcedomain", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORDomain.fSourceType, DbORDomain.metaClass));
    static {
        g_repositoryStringTable.put(DomainSourcedomain.m_name, DomainSourcedomain.m_struct);
    }

    /**
     * Gets the source type of a domain, if the source type is a built-in type. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DomainSourcebuiltintype = new GenericMappingEntry(
            "DomainSourcebuiltintype", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORDomain.fSourceType, DbORBuiltInType.metaClass));
    static {
        g_repositoryStringTable.put(DomainSourcebuiltintype.m_name,
                DomainSourcebuiltintype.m_struct);
    }

    /**
     * Gets the typed attributes associated to a domain. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DomainTypedattributes = new GenericMappingEntry(
            "DomainTypedattributes", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbORDomain.fTypedAttributes));
    static {
        g_repositoryStringTable.put(DomainTypedattributes.m_name, DomainTypedattributes.m_struct);
    }

    /**
     * Domain: a domain.<br>
     * <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Concept</b><br>
     */
    public static GenericMappingEntry Domain = new GenericMappingEntry("Domain", //NOT LOCALIZABLE
            new GenericMappingConcept(DbORDomain.class, DbORDomain.metaClass));
    static {
        g_repositoryStringTable.put(Domain.m_name, Domain.m_struct);
    }

    /**
     * Get the length of a domain. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String.<br>
     */
    public static GenericMappingEntry DomainLength = new GenericMappingEntry("DomainLength", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORDomain.fLength));
    static {
        g_repositoryStringTable.put(DomainLength.m_name, DomainLength.m_struct);
    }

    /**
     * Get the number of decimals of a domain. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String.<br>
     */
    public static GenericMappingEntry DomainNbdecimals = new GenericMappingEntry(
            "DomainNbdecimals", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORDomain.fNbDecimal));
    static {
        g_repositoryStringTable.put(DomainNbdecimals.m_name, DomainNbdecimals.m_struct);
    }

    /**
     * Lists all the operation libraries contained under a project. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ProjectOperationlibraries = new GenericMappingEntry(
            "ProjectOperationlibraries", //NOT LOCALIZABLE
            new GenericMappingComponents(DbOROperationLibrary.metaClass));
    static {
        g_repositoryStringTable.put(ProjectOperationlibraries.m_name,
                ProjectOperationlibraries.m_struct);
    }

    /**
     * Lists all the procedures contained under an operation library. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry OperationlibraryProcedures = new GenericMappingEntry(
            "OperationlibraryProcedures", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORProcedure.metaClass));
    static {
        g_repositoryStringTable.put(OperationlibraryProcedures.m_name,
                OperationlibraryProcedures.m_struct);
    }

    /**
     * Lists all the packages contained under an operation library. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry OperationlibraryPackages = new GenericMappingEntry(
            "OperationlibraryPackages", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORPackage.metaClass));
    static {
        g_repositoryStringTable.put(OperationlibraryPackages.m_name,
                OperationlibraryPackages.m_struct);
    }

    /**
     * Lists all the procedures contained under a package. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry PackageProcedures = new GenericMappingEntry(
            "PackageProcedures", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORProcedure.metaClass));
    static {
        g_repositoryStringTable.put(PackageProcedures.m_name, PackageProcedures.m_struct);
    }

    /**
     * Procedure: a procedure or a function.<br>
     * <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Concept</b><br>
     */
    public static GenericMappingEntry Procedure = new GenericMappingEntry("Procedure", //NOT LOCALIZABLE
            new GenericMappingConcept(DbORProcedure.class, DbORProcedure.metaClass));
    static {
        g_repositoryStringTable.put(Procedure.m_name, Procedure.m_struct);
    }

    /**
     * Tells whether the procedure is actually a function. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry ProcedureIsfunction = new GenericMappingEntry(
            "ProcedureIsfunction", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORProcedure.fFunction));
    static {
        g_repositoryStringTable.put(ProcedureIsfunction.m_name, ProcedureIsfunction.m_struct);
    }

    /**
     * Lists all the parameters of a procedure. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ProcedureOrparameters = new GenericMappingEntry(
            "ProcedureOrparameters", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORParameter.metaClass));
    static {
        g_repositoryStringTable.put(ProcedureOrparameters.m_name, ProcedureOrparameters.m_struct);
    }

    /**
     * Gets the return type of a function. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ProcedureReturntype = new GenericMappingEntry(
            "ProcedureReturntype", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORProcedure.fReturnType));
    static {
        g_repositoryStringTable.put(ProcedureReturntype.m_name, ProcedureReturntype.m_struct);
    }

    /**
     * Tells whether the return type of a function is referenced. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry ProcedureReturntypeisreferenced = new GenericMappingEntry(
            "ProcedureReturntypeisreferenced", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORProcedure.fReturnTypeReference));
    static {
        g_repositoryStringTable.put(ProcedureReturntypeisreferenced.m_name,
                ProcedureReturntypeisreferenced.m_struct);
    }

    /**
     * Retrieves the package to which a procedure belongs. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ProcedurePackage = new GenericMappingEntry(
            "ProcedurePackage", //NOT LOCALIZABLE
            new GenericMappingComposite(DbORPackage.metaClass));
    static {
        g_repositoryStringTable.put(ProcedurePackage.m_name, ProcedurePackage.m_struct);
    }

    /**
     * Gets the user who owns a procedure, if any. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ProcedureUser = new GenericMappingEntry("ProcedureUser", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORProcedure.fUser));
    static {
        g_repositoryStringTable.put(ProcedureUser.m_name, ProcedureUser.m_struct);
    }

    /**
     * Gets the Java method associated to a procedure, if any. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ProcedureJavamethod = new GenericMappingEntry(
            "ProcedureJavamethod", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORProcedure.fJavaMethod));
    static {
        g_repositoryStringTable.put(ProcedureJavamethod.m_name, ProcedureJavamethod.m_struct);
    }

    /**
     * Gets the body of a procedure. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry ProcedureBody = new GenericMappingEntry("ProcedureBody", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORProcedure.fBody));
    static {
        g_repositoryStringTable.put(ProcedureBody.m_name, ProcedureBody.m_struct);
    }

    /**
     * Gets the description of a procedure. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry ProcedureDescription = new GenericMappingEntry(
            "ProcedureDescription", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORProcedure.fDescription));
    static {
        g_repositoryStringTable.put(ProcedureDescription.m_name, ProcedureDescription.m_struct);
    }

    /**
     * Gets the passing convention of a procedure parameter. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for an IN parameter.<br>
     * - "2" for an OUT parameter.<br>
     * - "3" for an IN OUT parameter.<br>
     */
    public static GenericMappingEntry OrparameterPassingconvention = new GenericMappingEntry(
            "OrparameterPassingconvention", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORParameter.fPassingConvention));
    static {
        g_repositoryStringTable.put(OrparameterPassingconvention.m_name,
                OrparameterPassingconvention.m_struct);
    }

    /**
     * Gets the default value of a procedure parameter. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry OrparameterDefaultvalue = new GenericMappingEntry(
            "OrparameterDefaultvalue", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORParameter.fDefaultValue));
    static {
        g_repositoryStringTable.put(OrparameterDefaultvalue.m_name,
                OrparameterDefaultvalue.m_struct);
    }

    /**
     * Gets the type of a procedure parameter. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry OrparameterType = new GenericMappingEntry("OrparameterType", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORParameter.fType));
    static {
        g_repositoryStringTable.put(OrparameterType.m_name, OrparameterType.m_struct);
    }

    /**
     * Gets the length of a procedure parameter. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry OrparameterLength = new GenericMappingEntry(
            "OrparameterLength", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORParameter.fLength));
    static {
        g_repositoryStringTable.put(OrparameterLength.m_name, OrparameterLength.m_struct);
    }

    /**
     * Gets the number of decimals of a procedure parameter. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry OrparameterDecimal = new GenericMappingEntry(
            "OrparameterDecimal", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORParameter.fNbDecimal));
    static {
        g_repositoryStringTable.put(OrparameterDecimal.m_name, OrparameterDecimal.m_struct);
    }

    /**
     * Tells whether the type of a procedure parameter is referenced. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: true or false.<br>
     */
    public static GenericMappingEntry OrparameterIsreferenced = new GenericMappingEntry(
            "OrparameterIsreferenced", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORParameter.fReference));
    static {
        g_repositoryStringTable.put(OrparameterIsreferenced.m_name,
                OrparameterIsreferenced.m_struct);
    }

    /**
     * Gets the specification of a package. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry OrpackageSpecification = new GenericMappingEntry(
            "OrpackageSpecification", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORPackage.fSpecification));
    static {
        g_repositoryStringTable.put(OrpackageSpecification.m_name, OrpackageSpecification.m_struct);
    }

    /**
     * Gets the body of a package. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry OrpackageBody = new GenericMappingEntry("OrpackageBody", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORPackage.fBody));
    static {
        g_repositoryStringTable.put(OrpackageBody.m_name, OrpackageBody.m_struct);
    }

    /**
     * Gets the user who owns a package, if any. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry OrpackageUser = new GenericMappingEntry("OrpackageUser", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORPackage.fUser));
    static {
        g_repositoryStringTable.put(OrpackageUser.m_name, OrpackageUser.m_struct);
    }

    /**
     * Lists all the procedures contained under a package. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry OrpackageProcedures = new GenericMappingEntry(
            "OrpackageProcedures", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORProcedure.metaClass));
    static {
        g_repositoryStringTable.put(OrpackageProcedures.m_name, OrpackageProcedures.m_struct);
    }

    /**
     * Gets the category of a domain. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for a DISTINCT type<br>
     * - "2" for a STRUCTURED type<br>
     * - "3" for an OPAQUE type<br>
     * - "4" for a COLLECTION type<br>
     * - "5" for a DOMAIN type<br>
     * - "6" for a JAVA_OBJECT type<br>
     */
    public static GenericMappingEntry OrdomainCategory = new GenericMappingEntry(
            "OrdomainCategory", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORDomain.fCategory));
    static {
        g_repositoryStringTable.put(OrdomainCategory.m_name, OrdomainCategory.m_struct);
    }

    /**
     * Tells whether the domain is an ordered collection. <br>
     * Target System: <b>All Except Java</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry OrdomainOrderedcollection = new GenericMappingEntry(
            "OrdomainOrderedcollection", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORDomain.fOrderedCollection));
    static {
        g_repositoryStringTable.put(OrdomainOrderedcollection.m_name,
                OrdomainOrderedcollection.m_struct);
    }

    /**
     * <b>SECTION 5: ORACLE-SPECIFIC CONCEPTS (ONLY FOR THE ORACLE TARGET SYSTEM).</b> <br>
     */
    //tablespace, file, logfile, datafile, rollbacksegment, nestedtable, lobstorage, view
    //partition, subpartition, sequence, procedure, package
    public static Section Section_5;

    /**
     * Gets the archive log of a database. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry DatabaseArchivelog = new GenericMappingEntry(
            "DatabaseArchivelog", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORADatabase.fArchiveLog));
    static {
        g_repositoryStringTable.put(DatabaseArchivelog.m_name, DatabaseArchivelog.m_struct);
    }

    /**
     * Gets the character set of a database. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String.<br>
     */
    public static GenericMappingEntry DatabaseCharacterSet = new GenericMappingEntry(
            "DatabaseCharacterSet", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORADatabase.fCharacterSet));
    static {
        g_repositoryStringTable.put(DatabaseCharacterSet.m_name, DatabaseCharacterSet.m_struct);
    }

    /**
     * Gets the maximum number of data files allowed in a database. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String representing an integer.<br>
     */
    public static GenericMappingEntry DatabaseMaxDatafiles = new GenericMappingEntry(
            "DatabaseMaxDatafiles", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORADatabase.fMaxDataFiles));
    static {
        g_repositoryStringTable.put(DatabaseMaxDatafiles.m_name, DatabaseMaxDatafiles.m_struct);
    }

    /**
     * Gets the maximum number of instances allowed in a database. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String representing an integer.<br>
     */
    public static GenericMappingEntry DatabaseMaxinstances = new GenericMappingEntry(
            "DatabaseMaxinstances", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORADatabase.fMaxInstances));
    static {
        g_repositoryStringTable.put(DatabaseMaxinstances.m_name, DatabaseMaxinstances.m_struct);
    }

    /**
     * Gets the maximum number of log files allowed in a database. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String representing an integer.<br>
     */
    public static GenericMappingEntry DatabaseMaxlogfiles = new GenericMappingEntry(
            "DatabaseMaxlogfiles", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORADatabase.fMaxLogFiles));
    static {
        g_repositoryStringTable.put(DatabaseMaxlogfiles.m_name, DatabaseMaxlogfiles.m_struct);
    }

    /**
     * Gets the maximum length of log history allowed in a database. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String representing an integer.<br>
     */
    public static GenericMappingEntry DatabaseMaxloghistory = new GenericMappingEntry(
            "DatabaseMaxloghistory", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORADatabase.fMaxLogHistory));
    static {
        g_repositoryStringTable.put(DatabaseMaxloghistory.m_name, DatabaseMaxloghistory.m_struct);
    }

    /**
     * Gets the maximum number of log members allowed in a database. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String representing an integer.<br>
     */
    public static GenericMappingEntry DatabaseMaxlogmembers = new GenericMappingEntry(
            "DatabaseMaxlogmembers", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORADatabase.fMaxLogMembers));
    static {
        g_repositoryStringTable.put(DatabaseMaxlogmembers.m_name, DatabaseMaxlogmembers.m_struct);
    }

    /**
     * Gets the national character set used in a database. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry DatabaseNationalcharacterSet = new GenericMappingEntry(
            "DatabaseNationalcharacterSet", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORADatabase.fNationalCharacterSet));
    static {
        g_repositoryStringTable.put(DatabaseNationalcharacterSet.m_name,
                DatabaseNationalcharacterSet.m_struct);
    }

    /**
     * Tells whether the reuse control files option is true in a database. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry DatabaseReuseControlFiles = new GenericMappingEntry(
            "DatabaseReuseControlFiles", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORADatabase.fReuseControlFiles));
    static {
        g_repositoryStringTable.put(DatabaseReuseControlFiles.m_name,
                DatabaseReuseControlFiles.m_struct);
    }

    /**
     * Lists all the tablespaces contained in a database. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DatabaseTablespaces = new GenericMappingEntry(
            "DatabaseTablespaces", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORATablespace.metaClass));
    static {
        g_repositoryStringTable.put(DatabaseTablespaces.m_name, DatabaseTablespaces.m_struct);
    }

    /**
     * Lists all the data files contained in a tablespace. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry TablespaceDatafiles = new GenericMappingEntry(
            "TablespaceDatafiles", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORADataFile.metaClass));
    static {
        g_repositoryStringTable.put(TablespaceDatafiles.m_name, TablespaceDatafiles.m_struct);
    }

    /**
     * Gets the initial extend value of a tablespace. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry TablespaceDefobjinitialextent = new GenericMappingEntry(
            "TablespaceDefobjinitialextent", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATablespace.fDefObjInitialExtent));
    static {
        g_repositoryStringTable.put(TablespaceDefobjinitialextent.m_name,
                TablespaceDefobjinitialextent.m_struct);
    }

    /**
     * Gets the units of the initial extend value of a tablespace. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for Kilobyte size unit.<br>
     * - "2" for Megabyte size unit.<br>
     */
    public static GenericMappingEntry TablespaceDefobjinitialextentsizeunit = new GenericMappingEntry(
            "TablespaceDefobjinitialextentsizeunit", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATablespace.fDefObjInitialExtentSizeUnit));
    static {
        g_repositoryStringTable.put(TablespaceDefobjinitialextentsizeunit.m_name,
                TablespaceDefobjinitialextentsizeunit.m_struct);
    }

    /**
     * Gets the maximum extend value of a tablespace. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry TablespaceDefobjmaxextents = new GenericMappingEntry(
            "TablespaceDefobjmaxextents", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATablespace.fDefObjMaxExtents));
    static {
        g_repositoryStringTable.put(TablespaceDefobjmaxextents.m_name,
                TablespaceDefobjmaxextents.m_struct);
    }

    /**
     * Gets the minimum extend value of a tablespace. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry TablespaceDefobjminextents = new GenericMappingEntry(
            "TablespaceDefobjminextents", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATablespace.fDefObjMinExtents));
    static {
        g_repositoryStringTable.put(TablespaceDefobjminextents.m_name,
                TablespaceDefobjminextents.m_struct);
    }

    /**
     * Gets the next extend value of a tablespace. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry TablespaceDefobjnextextent = new GenericMappingEntry(
            "TablespaceDefobjnextextent", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATablespace.fDefObjNextExtent));
    static {
        g_repositoryStringTable.put(TablespaceDefobjnextextent.m_name,
                TablespaceDefobjnextextent.m_struct);
    }

    /**
     * Gets the units of the next extend value of a tablespace. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for Kilobyte size unit.<br>
     * - "2" for Megabyte size unit.<br>
     */
    public static GenericMappingEntry TablespaceDefobjnextextentsizeunit = new GenericMappingEntry(
            "TablespaceDefobjnextextentsizeunit", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATablespace.fDefObjNextExtentSizeUnit));
    static {
        g_repositoryStringTable.put(TablespaceDefobjnextextentsizeunit.m_name,
                TablespaceDefobjnextextentsizeunit.m_struct);
    }

    /**
     * Gets the increase percentage of a tablespace. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry TablespaceDefobjpctincrease = new GenericMappingEntry(
            "TablespaceDefobjpctincrease", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATablespace.fDefObjPctIncrease));
    static {
        g_repositoryStringTable.put(TablespaceDefobjpctincrease.m_name,
                TablespaceDefobjpctincrease.m_struct);
    }

    /**
     * Gets the unlimited extents value of a tablespace. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry TablespaceDefobjunlimitedextents = new GenericMappingEntry(
            "TablespaceDefobjunlimitedextents", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATablespace.fDefObjUnlimitedExtents));
    static {
        g_repositoryStringTable.put(TablespaceDefobjunlimitedextents.m_name,
                TablespaceDefobjunlimitedextents.m_struct);
    }

    /**
     * Gets the extent management of a tablespace. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for DICTIONNARY.<br>
     * - "2" for LOCAL.<br>
     * - "3" for LOCAL AUTOALLOCATE.<br>
     * - "4" for LOCAL UNIFORM.<br>
     */
    public static GenericMappingEntry TablespaceExtentmanagement = new GenericMappingEntry(
            "TablespaceExtentmanagement", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATablespace.fExtentManagement));
    static {
        g_repositoryStringTable.put(TablespaceExtentmanagement.m_name,
                TablespaceExtentmanagement.m_struct);
    }

    /**
     * Gets the log option value of a tablespace. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for LOGGING option.<br>
     * - "2" for NOLOGGING option.<br>
     */
    public static GenericMappingEntry TablespaceIslog = new GenericMappingEntry("TablespaceIslog", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATablespace.fLog));
    static {
        g_repositoryStringTable.put(TablespaceIslog.m_name, TablespaceIslog.m_struct);
    }

    /**
     * Tells whether a given tablespace is online. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for ONLINE.<br>
     * - "2" for OFFLINE.<br>
     */
    public static GenericMappingEntry TablespaceIsonline = new GenericMappingEntry(
            "TablespaceIsonline", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATablespace.fOnline));
    static {
        g_repositoryStringTable.put(TablespaceIsonline.m_name, TablespaceIsonline.m_struct);
    }

    /**
     * Tells whether a given tablespace is temporary. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true' of "false".<br>
     */
    public static GenericMappingEntry TablespaceIstemporary = new GenericMappingEntry(
            "TablespaceIstemporary", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATablespace.fTemporary));
    static {
        g_repositoryStringTable.put(TablespaceIstemporary.m_name, TablespaceIstemporary.m_struct);
    }

    /**
     * Gets the minimum extent value of a tablespace. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry TablespaceMinextent = new GenericMappingEntry(
            "TablespaceMinextent", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATablespace.fMinExtent));
    static {
        g_repositoryStringTable.put(TablespaceMinextent.m_name, TablespaceMinextent.m_struct);
    }

    /**
     * Gets the units of the minimum extent value of a tablespace. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for Kilobyte size unit.<br>
     * - "2" for Megabyte size unit.<br>
     */
    public static GenericMappingEntry TablespaceMinextentsizeunit = new GenericMappingEntry(
            "TablespaceMinextentsizeunit", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATablespace.fMinExtentSizeUnit));
    static {
        g_repositoryStringTable.put(TablespaceMinextentsizeunit.m_name,
                TablespaceMinextentsizeunit.m_struct);
    }

    /**
     * Gets the uniform extent size of a tablespace. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry TablespaceUniformextentsize = new GenericMappingEntry(
            "TablespaceUniformextentsize", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATablespace.fUniformExtentSize));
    static {
        g_repositoryStringTable.put(TablespaceUniformextentsize.m_name,
                TablespaceUniformextentsize.m_struct);
    }

    /**
     * Gets the units of the uniform extent size of a tablespace. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for Kilobyte size unit.<br>
     * - "2" for Megabyte size unit.<br>
     */
    public static GenericMappingEntry TablespaceUniformextentsizeUnit = new GenericMappingEntry(
            "TablespaceUniformextentsizeUnit", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATablespace.fUniformExtentSizeUnit));
    static {
        g_repositoryStringTable.put(TablespaceUniformextentsizeUnit.m_name,
                TablespaceUniformextentsizeUnit.m_struct);
    }

    /**
     * Gets the size of a file. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry FileSize = new GenericMappingEntry("FileSize", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAFile.fSize));
    static {
        g_repositoryStringTable.put(FileSize.m_name, FileSize.m_struct);
    }

    /**
     * Gets the units of a file size. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for Kilobyte size unit.<br>
     * - "2" for Megabyte size unit.<br>
     */
    public static GenericMappingEntry FileSizeunit = new GenericMappingEntry("FileSizeunit", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAFile.fSizeUnit));
    static {
        g_repositoryStringTable.put(FileSizeunit.m_name, FileSizeunit.m_struct);
    }

    /**
     * Tells whether a file can be reused. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry FileReuse = new GenericMappingEntry("FileReuse", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAFile.fReuse));
    static {
        g_repositoryStringTable.put(FileReuse.m_name, FileReuse.m_struct);
    }

    /**
     * Lists all the redo log groups contained in a database. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DatabaseRedologgroups = new GenericMappingEntry(
            "DatabaseRedologgroups", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORARedoLogGroup.metaClass));
    static {
        g_repositoryStringTable.put(DatabaseRedologgroups.m_name, DatabaseRedologgroups.m_struct);
    }

    /**
     * Lists all the redo log files contained in a redo log group. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     * 
     * @deprecated
     */
    public static GenericMappingEntry RedologproupFiles = new GenericMappingEntry(
            "RedologproupFiles", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORARedoLogFile.metaClass));
    static {
        g_repositoryStringTable.put(RedologproupFiles.m_name, RedologproupFiles.m_struct);
    }

    /**
     * Lists all the redo log files contained in a redo log group. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry RedologgroupRedologfiles = new GenericMappingEntry(
            "RedologgroupRedologfiles", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORARedoLogFile.metaClass));
    static {
        g_repositoryStringTable.put(RedologgroupRedologfiles.m_name,
                RedologgroupRedologfiles.m_struct);
    }

    /**
     * Retrieves the redo log group to which a redo log file belongs. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry RedologfileRedologgroup = new GenericMappingEntry(
            "RedologfileRedologgroup", //NOT LOCALIZABLE
            new GenericMappingComposite(DbORARedoLogGroup.metaClass));
    static {
        g_repositoryStringTable.put(RedologfileRedologgroup.m_name,
                RedologfileRedologgroup.m_struct);
    }

    /**
     * Gets the auto extent attribute of a data file. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry DatafileAutoextend = new GenericMappingEntry(
            "DatafileAutoextend", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORADataFile.fAutoExtend));
    static {
        g_repositoryStringTable.put(DatafileAutoextend.m_name, DatafileAutoextend.m_struct);
    }

    /**
     * Gets the extension size of a data file. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry DatafileExtensionsize = new GenericMappingEntry(
            "DatafileExtensionsize", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORADataFile.fExtensionsize));
    static {
        g_repositoryStringTable.put(DatafileExtensionsize.m_name, DatafileExtensionsize.m_struct);
    }

    /**
     * Gets the units of the extension size of a data file. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for Kilobyte size unit.<br>
     * - "2" for Megabyte size unit.<br>
     */
    public static GenericMappingEntry DatafileExtensionsizeunit = new GenericMappingEntry(
            "DatafileExtensionsizeunit", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORADataFile.fExtensionSizeUnit));
    static {
        g_repositoryStringTable.put(DatafileExtensionsizeunit.m_name,
                DatafileExtensionsizeunit.m_struct);
    }

    /**
     * Gets the maximum extension size of a data file. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry DatafileMaxextensionsize = new GenericMappingEntry(
            "DatafileMaxextensionsize", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORADataFile.fMaxExtensionsize));
    static {
        g_repositoryStringTable.put(DatafileMaxextensionsize.m_name,
                DatafileMaxextensionsize.m_struct);
    }

    /**
     * Gets the units of the maximum extension size of a data file. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for Kilobyte size unit.<br>
     * - "2" for Megabyte size unit.<br>
     */
    public static GenericMappingEntry DatafileMaxextensionsizeunit = new GenericMappingEntry(
            "DatafileMaxextensionsizeunit", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORADataFile.fMaxExtensionSizeUnit));
    static {
        g_repositoryStringTable.put(DatafileMaxextensionsizeunit.m_name,
                DatafileMaxextensionsizeunit.m_struct);
    }

    /**
     * Gets the unlimited extension size of a data file. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry DatafileUnlimitedextension = new GenericMappingEntry(
            "DatafileUnlimitedextension", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORADataFile.fUnlimitedExtension));
    static {
        g_repositoryStringTable.put(DatafileUnlimitedextension.m_name,
                DatafileUnlimitedextension.m_struct);
    }

    /**
     * Retrieves the tablespace to which a data file belongs. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DatafileTablespace = new GenericMappingEntry(
            "DatafileTablespace", //NOT LOCALIZABLE
            new GenericMappingComposite(DbORATablespace.metaClass));
    static {
        g_repositoryStringTable.put(DatafileTablespace.m_name, DatafileTablespace.m_struct);
    }

    /**
     * Gets the initial extent value of a rollback segment. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry RollbacksegmentInitialextent = new GenericMappingEntry(
            "RollbacksegmentInitialextent", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORARollbackSegment.fInitialExtent));
    static {
        g_repositoryStringTable.put(RollbacksegmentInitialextent.m_name,
                RollbacksegmentInitialextent.m_struct);
    }

    /**
     * Gets the units of the initial extent value of a rollback segment. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for Kilobyte size unit.<br>
     * - "2" for Megabyte size unit.<br>
     */
    public static GenericMappingEntry RollbacksegmentInitialextentsizeunit = new GenericMappingEntry(
            "RollbacksegmentInitialextentsizeunit", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORARollbackSegment.fInitialExtentSizeUnit));
    static {
        g_repositoryStringTable.put(RollbacksegmentInitialextentsizeunit.m_name,
                RollbacksegmentInitialextentsizeunit.m_struct);
    }

    /**
     * Gets the maximum extents value of a rollback segment. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry RollbacksegmentMaxextents = new GenericMappingEntry(
            "RollbacksegmentMaxextents", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORARollbackSegment.fMaxExtents));
    static {
        g_repositoryStringTable.put(RollbacksegmentMaxextents.m_name,
                RollbacksegmentMaxextents.m_struct);
    }

    /**
     * Gets the minimum extents value of a rollback segment. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry RollbacksegmentMinextents = new GenericMappingEntry(
            "RollbacksegmentMinextents", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORARollbackSegment.fMinExtents));
    static {
        g_repositoryStringTable.put(RollbacksegmentMinextents.m_name,
                RollbacksegmentMinextents.m_struct);
    }

    /**
     * Gets the next extents value of a rollback segment. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry RollbacksegmentNextextent = new GenericMappingEntry(
            "RollbacksegmentNextextent", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORARollbackSegment.fNextExtent));
    static {
        g_repositoryStringTable.put(RollbacksegmentNextextent.m_name,
                RollbacksegmentNextextent.m_struct);
    }

    /**
     * Gets the units of the next extents value of a rollback segment. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for Kilobyte size unit.<br>
     * - "2" for Megabyte size unit.<br>
     */
    public static GenericMappingEntry RollbacksegmentNextextentsizeunit = new GenericMappingEntry(
            "RollbacksegmentNextextentsizeunit", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORARollbackSegment.fNextExtentSizeUnit));
    static {
        g_repositoryStringTable.put(RollbacksegmentNextextentsizeunit.m_name,
                RollbacksegmentNextextentsizeunit.m_struct);
    }

    /**
     * Gets the optimal size of a rollback segment. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry RollbacksegmentOptimalsize = new GenericMappingEntry(
            "RollbacksegmentOptimalsize", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORARollbackSegment.fOptimalSize));
    static {
        g_repositoryStringTable.put(RollbacksegmentOptimalsize.m_name,
                RollbacksegmentOptimalsize.m_struct);
    }

    /**
     * Gets the units of the optimal size of a rollback segment. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for Kilobyte size unit.<br>
     * - "2" for Megabyte size unit.<br>
     */
    public static GenericMappingEntry RollbacksegmentOptimalsizeunit = new GenericMappingEntry(
            "RollbacksegmentOptimalsizeunit", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORARollbackSegment.fOptimalSizeUnit));
    static {
        g_repositoryStringTable.put(RollbacksegmentOptimalsizeunit.m_name,
                RollbacksegmentOptimalsizeunit.m_struct);
    }

    /**
     * Tells whether the rollback segment is public. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false". <br>
     */
    public static GenericMappingEntry RollbacksegmentIspublic = new GenericMappingEntry(
            "RollbacksegmentIspublic", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORARollbackSegment.fPublic));
    static {
        g_repositoryStringTable.put(RollbacksegmentIspublic.m_name,
                RollbacksegmentIspublic.m_struct);
    }

    /**
     * Gets the tablespace associated to a rollback segment. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry RollbacksegmentTablespace = new GenericMappingEntry(
            "RollbacksegmentTablespace", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORARollbackSegment.fTablespace));
    static {
        g_repositoryStringTable.put(RollbacksegmentTablespace.m_name,
                RollbacksegmentTablespace.m_struct);
    }

    /**
     * Gets the buffer pool option of an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for KEEP option. - "1" for RECYCLE option. - "2" for DEFAULT option.
     */
    public static GenericMappingEntry TableBufferpool = new GenericMappingEntry("TableBufferpool", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAAbsTable.fBufferPool));
    static {
        g_repositoryStringTable.put(TableBufferpool.m_name, TableBufferpool.m_struct);
    }

    /**
     * Gets the free lists value of an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry TableFreelists = new GenericMappingEntry("TableFreelists", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAAbsTable.fFreelists));
    static {
        g_repositoryStringTable.put(TableFreelists.m_name, TableFreelists.m_struct);
    }

    /**
     * Gets the free list groups value of an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry TableFreelistgroups = new GenericMappingEntry(
            "TableFreelistgroups", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAAbsTable.fFreelistGroups));
    static {
        g_repositoryStringTable.put(TableFreelistgroups.m_name, TableFreelistgroups.m_struct);
    }

    /**
     * Gets the initial extent value of an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry TableInitialextent = new GenericMappingEntry(
            "TableInitialextent", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAAbsTable.fInitialExtent));
    static {
        g_repositoryStringTable.put(TableInitialextent.m_name, TableInitialextent.m_struct);
    }

    /**
     * Gets the units of the initial extent value of an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for Kilobyte size unit.<br>
     * - "2" for Megabyte size unit.<br>
     */
    public static GenericMappingEntry TableInitialextentsizeunit = new GenericMappingEntry(
            "TableInitialextentsizeunit", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAAbsTable.fInitialExtentSizeUnit));
    static {
        g_repositoryStringTable.put(TableInitialextentsizeunit.m_name,
                TableInitialextentsizeunit.m_struct);
    }

    /**
     * Gets the initial trans value of an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry TableInitrans = new GenericMappingEntry("TableInitrans", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAAbsTable.fInitrans));
    static {
        g_repositoryStringTable.put(TableInitrans.m_name, TableInitrans.m_struct);
    }

    /**
     * Gets the maximum extents trans value of an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry TableMaxextents = new GenericMappingEntry("TableMaxextents", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAAbsTable.fMaxExtents));
    static {
        g_repositoryStringTable.put(TableMaxextents.m_name, TableMaxextents.m_struct);
    }

    /**
     * Gets the maximum trans value of an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry TableMaxtrans = new GenericMappingEntry("TableMaxtrans", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAAbsTable.fMaxtrans));
    static {
        g_repositoryStringTable.put(TableMaxtrans.m_name, TableMaxtrans.m_struct);
    }

    /*
     * Gets the minimum trans value of an Oracle table. <br> Target System: <b>Oracle</b><br> Type:
     * <b>Attribute</b><br> Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry TableMinextents = new GenericMappingEntry("TableMinextents", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAAbsTable.fMinExtents));
    static {
        g_repositoryStringTable.put(TableMinextents.m_name, TableMinextents.m_struct);
    }

    /**
     * Gets the next extent value of an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry TableNextextent = new GenericMappingEntry("TableNextextent", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAAbsTable.fNextExtent));
    static {
        g_repositoryStringTable.put(TableNextextent.m_name, TableNextextent.m_struct);
    }

    /**
     * Gets the units of the next extent value of an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for Kilobyte size unit.<br>
     * - "2" for Megabyte size unit.<br>
     */
    public static GenericMappingEntry TableNextextentsizeunit = new GenericMappingEntry(
            "TableNextextentsizeunit", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAAbsTable.fNextExtentSizeUnit));
    static {
        g_repositoryStringTable.put(TableNextextentsizeunit.m_name,
                TableNextextentsizeunit.m_struct);
    }

    /**
     * Gets the free percentage of an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry TablePctfree = new GenericMappingEntry("TablePctfree", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAAbsTable.fPctfree));
    static {
        g_repositoryStringTable.put(TablePctfree.m_name, TablePctfree.m_struct);
    }

    /**
     * Gets the increase percentage attribute of an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry TablePctincrease = new GenericMappingEntry(
            "TablePctincrease", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAAbsTable.fPctIncrease));
    static {
        g_repositoryStringTable.put(TablePctincrease.m_name, TablePctincrease.m_struct);
    }

    /**
     * Gets the percentage used attribute of an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry TablePctused = new GenericMappingEntry("TablePctused", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAAbsTable.fPctused));
    static {
        g_repositoryStringTable.put(TablePctused.m_name, TablePctused.m_struct);
    }

    /**
     * Lists all the tablespaces associated to an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry TableTablespaces = new GenericMappingEntry(
            "TableTablespaces", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbORATable.fTablespaces));
    static {
        g_repositoryStringTable.put(TableTablespaces.m_name, TableTablespaces.m_struct);
    }

    /**
     * Gets the cache attribute of an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for CACHE option.<br>
     * - "2" for NOCACHE option.<br>
     */
    public static GenericMappingEntry TableCache = new GenericMappingEntry("TableCache", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATable.fCache));
    static {
        g_repositoryStringTable.put(TableCache.m_name, TableCache.m_struct);
    }

    /**
     * Gets the commit action of an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for ON COMMIT DELETE ROWS option.<br>
     * - "2" for ON COMMIT PRESERVE ROWS option.<br>
     */
    public static GenericMappingEntry TableCommitaction = new GenericMappingEntry(
            "TableCommitaction", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATable.fCommitAction));
    static {
        g_repositoryStringTable.put(TableCommitaction.m_name, TableCommitaction.m_struct);
    }

    /**
     * Lists all the lob storages contained in an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry TableLobstorages = new GenericMappingEntry(
            "TableLobstorages", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORALobStorage.metaClass));
    static {
        g_repositoryStringTable.put(TableLobstorages.m_name, TableLobstorages.m_struct);
    }

    /**
     * Gets the log option of an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for LOGGING option.<br>
     * - "2" for NOLOGGING option.<br>
     */
    public static GenericMappingEntry TableLog = new GenericMappingEntry("TableLog", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATable.fLog));
    static {
        g_repositoryStringTable.put(TableLog.m_name, TableLog.m_struct);
    }

    /**
     * Gets the monitoring option of an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for MONITORING option.<br>
     * - "2" for NOMONITORING option.<br>
     */
    public static GenericMappingEntry TableMonitoring = new GenericMappingEntry("TableMonitoring", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATable.fMonitoring));
    static {
        g_repositoryStringTable.put(TableMonitoring.m_name, TableMonitoring.m_struct);
    }

    /**
     * Gets the parallel option of an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for NOPARALLEL option.<br>
     * - "2" for PARALLEL option.<br>
     */
    public static GenericMappingEntry TableParallel = new GenericMappingEntry("TableParallel", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATable.fParallel));
    static {
        g_repositoryStringTable.put(TableParallel.m_name, TableParallel.m_struct);
    }

    /**
     * Gets the parallel degree attribute of an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry TableParalleldegree = new GenericMappingEntry(
            "TableParalleldegree", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATable.fParallelDegree));
    static {
        g_repositoryStringTable.put(TableParalleldegree.m_name, TableParalleldegree.m_struct);
    }

    /**
     * Lists all the partitions contained in an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry TablePartitions = new GenericMappingEntry("TablePartitions", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORAPartition.metaClass));
    static {
        g_repositoryStringTable.put(TablePartitions.m_name, TablePartitions.m_struct);
    }

    /**
     * Lists all the nested table storages contained in an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry TableNestedtablestorages = new GenericMappingEntry(
            "TableNestedtablestorages", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORANestedTableStorage.metaClass));
    static {
        g_repositoryStringTable.put(TableNestedtablestorages.m_name,
                TableNestedtablestorages.m_struct);
    }

    /**
     * Gets the partitioning method of an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for PARTITION BY RANGE.<br>
     * - "2" for PARTITION BY HASH.<br>
     * - "3" for PARTITION BY LIST.<br>
     */

    public static GenericMappingEntry TablePartitioningmethod = new GenericMappingEntry(
            "TablePartitioningmethod", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATable.fPartitioningMethod));
    static {
        g_repositoryStringTable.put(TablePartitioningmethod.m_name,
                TablePartitioningmethod.m_struct);
    }

    /**
     * Gets the partitioning method of an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for PARTITION BY RANGE.<br>
     * - "2" for PARTITION BY HASH.<br>
     * - "3" for PARTITION BY LIST.<br>
     * 
     * @deprecated
     * @see org.modelsphere.sms.plugins.generic.GenericMapping#TablePartitioningmethod
     */
    public static GenericMappingEntry TablePartitionningmethod = new GenericMappingEntry(
            "TablePartitionningmethod", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATable.fPartitioningMethod));
    static {
        g_repositoryStringTable.put(TablePartitionningmethod.m_name,
                TablePartitionningmethod.m_struct);
    }

    /**
     * Lists all the partition key columns associated to an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry TablePartitionkeycolumns = new GenericMappingEntry(
            "TablePartitionkeycolumns", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbORATable.fPartitionKeyColumns));
    static {
        g_repositoryStringTable.put(TablePartitionkeycolumns.m_name,
                TablePartitionkeycolumns.m_struct);
    }

    /**
     * Lists all the subpartition key columns associated to an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry TableSubpartitionkeycolumns = new GenericMappingEntry(
            "TableSubpartitionkeycolumns", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbORATable.fSubpartitionKeyColumns));
    static {
        g_repositoryStringTable.put(TableSubpartitionkeycolumns.m_name,
                TableSubpartitionkeycolumns.m_struct);
    }

    /**
     * Gets the row movement of an Oracle table. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for ENABLE ROW MOVEMENT.<br>
     * - "2" for DISABLE ROW MOVEMENT.<br>
     */
    public static GenericMappingEntry TableRowmovement = new GenericMappingEntry(
            "TableRowmovement", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATable.fRowMovement));
    static {
        g_repositoryStringTable.put(TableRowmovement.m_name, TableRowmovement.m_struct);
    }

    /**
     * Tells whether an Oracle table is temporary. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry TableTemporary = new GenericMappingEntry("TableTemporary", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATable.fTemporary));
    static {
        g_repositoryStringTable.put(TableTemporary.m_name, TableTemporary.m_struct);
    }

    /**
     * Lists all the columns contained in an Oracle nested table storage. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry NestedtablestorageColumns = new GenericMappingEntry(
            "NestedtablestorageColumns", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORColumn.metaClass));
    static {
        g_repositoryStringTable.put(NestedtablestorageColumns.m_name,
                NestedtablestorageColumns.m_struct);
    }

    /**
     * Gets the nested item associated to an Oracle nested table storage. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry NestedtablestorageNesteditem = new GenericMappingEntry(
            "NestedtablestorageNesteditem", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORANestedTableStorage.fNestedItem));
    static {
        g_repositoryStringTable.put(NestedtablestorageNesteditem.m_name,
                NestedtablestorageNesteditem.m_struct);
    }

    /**
     * Gets the return value type of an Oracle nested table storage. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for RETURN AS LOCATOR option.<br>
     * - "2" for RETURN AS VALUE option.<br>
     */
    public static GenericMappingEntry NestedtablestorageReturnvaluetype = new GenericMappingEntry(
            "NestedtablestorageReturnvaluetype", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORANestedTableStorage.fReturnValueType));
    static {
        g_repositoryStringTable.put(NestedtablestorageReturnvaluetype.m_name,
                NestedtablestorageReturnvaluetype.m_struct);
    }

    /**
     * Gets the user who owns an Oracle nested table storage, if any. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry NestedtablestorageUser = new GenericMappingEntry(
            "NestedtablestorageUser", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORANestedTableStorage.fUser));
    static {
        g_repositoryStringTable.put(NestedtablestorageUser.m_name, NestedtablestorageUser.m_struct);
    }

    /**
     * Gets the table containing the Oracle nested table storage. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry NestedtablestorageTable = new GenericMappingEntry(
            "NestedtablestorageTable", //NOT LOCALIZABLE
            new GenericMappingComposite(DbORATable.metaClass));
    static {
        g_repositoryStringTable.put(NestedtablestorageTable.m_name,
                NestedtablestorageTable.m_struct);
    }

    /**
     * Gets the buffer pool of a lob storage. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for KEEP option. - "1" for RECYCLE option. - "2" for DEFAULT option.
     */
    public static GenericMappingEntry DbORALobStorageBufferpool = new GenericMappingEntry(
            "DbORALobStorageBufferpool", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORALobStorage.fBufferPool));
    static {
        g_repositoryStringTable.put(DbORALobStorageBufferpool.m_name,
                DbORALobStorageBufferpool.m_struct);
    }

    /**
     * Gets the cache option of a lob storage. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for CACHE option - "2" for NOCHACHE option
     */
    public static GenericMappingEntry DbORALobStorageCache = new GenericMappingEntry(
            "DbORALobStorageCache", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORALobStorage.fCache));
    static {
        g_repositoryStringTable.put(DbORALobStorageCache.m_name, DbORALobStorageCache.m_struct);
    }

    /**
     * Gets the chunk size of a lob storage. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry DbORALobStorageChunksize = new GenericMappingEntry(
            "DbORALobStorageChunksize", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORALobStorage.fChunkSize));
    static {
        g_repositoryStringTable.put(DbORALobStorageChunksize.m_name,
                DbORALobStorageChunksize.m_struct);
    }

    /**
     * Gets the free list groups value of a lob storage. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry DbORALobStorageFreelistgroups = new GenericMappingEntry(
            "DbORALobStorageFreelistgroups", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORALobStorage.fFreelistGroups));
    static {
        g_repositoryStringTable.put(DbORALobStorageFreelistgroups.m_name,
                DbORALobStorageFreelistgroups.m_struct);
    }

    /**
     * Gets the free lists value of a lob storage. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry DbORALobStorageFreelists = new GenericMappingEntry(
            "DbORALobStorageFreelists", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORALobStorage.fFreelists));
    static {
        g_repositoryStringTable.put(DbORALobStorageFreelists.m_name,
                DbORALobStorageFreelists.m_struct);
    }

    /**
     * Gets the initial extent value of a lob storage. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry DbORALobStorageInitialextent = new GenericMappingEntry(
            "DbORALobStorageInitialextent", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORALobStorage.fInitialExtent));
    static {
        g_repositoryStringTable.put(DbORALobStorageInitialextent.m_name,
                DbORALobStorageInitialextent.m_struct);
    }

    /**
     * Gets the units of the initial extent value of a lob storage. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for Kilobyte size unit.<br>
     * - "2" for Megabyte size unit.<br>
     */
    public static GenericMappingEntry DbORALobStorageInitialextentsizeunit = new GenericMappingEntry(
            "DbORALobStorageInitialextentsizeunit", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORALobStorage.fInitialExtentSizeUnit));
    static {
        g_repositoryStringTable.put(DbORALobStorageInitialextentsizeunit.m_name,
                DbORALobStorageInitialextentsizeunit.m_struct);
    }

    /**
     * Lists all the columns associated with the a lob storage. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DbORALobStorageLobitems = new GenericMappingEntry(
            "DbORALobStorageLobitems", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbORALobStorage.fLobItems));
    static {
        g_repositoryStringTable.put(DbORALobStorageLobitems.m_name,
                DbORALobStorageLobitems.m_struct);
    }

    /**
     * Gets the log option of a lob storage. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for LOGGING option.<br>
     * - "2" for NOLOGGING option.<br>
     */
    public static GenericMappingEntry DbORALobStorageLog = new GenericMappingEntry(
            "DbORALobStorageLog", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORALobStorage.fLog));
    static {
        g_repositoryStringTable.put(DbORALobStorageLog.m_name, DbORALobStorageLog.m_struct);
    }

    /**
     * Gets the maximum extent value of a lob storage. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry DbORALobStorageMaxextent = new GenericMappingEntry(
            "DbORALobStorageMaxextent", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORALobStorage.fMaxExtent));
    static {
        g_repositoryStringTable.put(DbORALobStorageMaxextent.m_name,
                DbORALobStorageMaxextent.m_struct);
    }

    /**
     * Gets the minimum extent value of a lob storage. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry DbORALobStorageMinextent = new GenericMappingEntry(
            "DbORALobStorageMinextent", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORALobStorage.fMinExtent));
    static {
        g_repositoryStringTable.put(DbORALobStorageMinextent.m_name,
                DbORALobStorageMinextent.m_struct);
    }

    /**
     * Gets the next extent value of a lob storage. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry DbORALobStorageNextextent = new GenericMappingEntry(
            "DbORALobStorageNextextent", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORALobStorage.fNextExtent));
    static {
        g_repositoryStringTable.put(DbORALobStorageNextextent.m_name,
                DbORALobStorageNextextent.m_struct);
    }

    /**
     * Gets the units of the next extent value of a lob storage. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for Kilobyte size unit.<br>
     * - "2" for Megabyte size unit.<br>
     */
    public static GenericMappingEntry DbORALobStorageNextextentsizeunit = new GenericMappingEntry(
            "DbORALobStorageNextextentsizeunit", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORALobStorage.fNextExtentSizeUnit));
    static {
        g_repositoryStringTable.put(DbORALobStorageNextextentsizeunit.m_name,
                DbORALobStorageNextextentsizeunit.m_struct);
    }

    /**
     * Gets the increase percentage value of a lob storage. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry DbORALobStoragePctincrease = new GenericMappingEntry(
            "DbORALobStoragePctincrease", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORALobStorage.fPctIncrease));
    static {
        g_repositoryStringTable.put(DbORALobStoragePctincrease.m_name,
                DbORALobStoragePctincrease.m_struct);
    }

    /**
     * Gets the percentage version of a lob storage. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry DbORALobStoragePctversion = new GenericMappingEntry(
            "DbORALobStoragePctversion", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORALobStorage.fPctversion));
    static {
        g_repositoryStringTable.put(DbORALobStoragePctversion.m_name,
                DbORALobStoragePctversion.m_struct);
    }

    /**
     * Gets the storage in rows option of a lob storage. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for ENABLE STORAGE IN ROW option.<br>
     * - "2" for DISABLE STORAGE IN ROW option.<br>
     */
    public static GenericMappingEntry DbORALobStorageStorageinrows = new GenericMappingEntry(
            "DbORALobStorageStorageinrows", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORALobStorage.fStorageInRow));
    static {
        g_repositoryStringTable.put(DbORALobStorageStorageinrows.m_name,
                DbORALobStorageStorageinrows.m_struct);
    }

    /**
     * Gets the tablespace associated to a lob storage. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DbORALobStorageTablespace = new GenericMappingEntry(
            "DbORALobStorageTablespace", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORALobStorage.fTablespace));
    static {
        g_repositoryStringTable.put(DbORALobStorageTablespace.m_name,
                DbORALobStorageTablespace.m_struct);
    }

    /**
     * Gets the unlimited extents value of a lob storage. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry DbORALobStorageUnlimitedextents = new GenericMappingEntry(
            "DbORALobStorageUnlimitedextents", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORALobStorage.fUnlimitedExtents));
    static {
        g_repositoryStringTable.put(DbORALobStorageUnlimitedextents.m_name,
                DbORALobStorageUnlimitedextents.m_struct);
    }

    /**
     * Gets the constraint type of a view. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for READ ONLY option. - "2" for CHECK OPTION.
     */
    public static GenericMappingEntry ViewConstrainttype = new GenericMappingEntry(
            "ViewConstrainttype", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAView.fConstraintType));
    static {
        g_repositoryStringTable.put(ViewConstrainttype.m_name, ViewConstrainttype.m_struct);
    }

    /**
     * Lists all the check constraints of a view. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ViewConstraints = new GenericMappingEntry("ViewConstraints", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORCheck.metaClass));
    static {
        g_repositoryStringTable.put(ViewConstraints.m_name, ViewConstraints.m_struct);
    }

    /**
     * Lists all the check constraints of a view. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     * 
     * @deprecated
     * @see org.modelsphere.sms.plugins.generic.GenericMapping#ViewConstraints
     */
    public static GenericMappingEntry ViewContraints = new GenericMappingEntry("ViewContraints", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORCheck.metaClass));
    static {
        g_repositoryStringTable.put(ViewContraints.m_name, ViewContraints.m_struct);
    }

    /**
     * Gets the lob storage of a column. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ColumnLobstorage = new GenericMappingEntry(
            "ColumnLobstorage", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORAColumn.fLobStorage));
    static {
        g_repositoryStringTable.put(ColumnLobstorage.m_name, ColumnLobstorage.m_struct);
    }

    /**
     * Gets the partition key table associated to a column. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ColumnPartitionkeytable = new GenericMappingEntry(
            "ColumnPartitionkeytable", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORAColumn.fPartitionKeyTable));
    static {
        g_repositoryStringTable.put(ColumnPartitionkeytable.m_name,
                ColumnPartitionkeytable.m_struct);
    }

    /**
     * Gets the storage table associated to a column, if any. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ColumnStoragetable = new GenericMappingEntry(
            "ColumnStoragetable", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORAColumn.fStorageTable));
    static {
        g_repositoryStringTable.put(ColumnStoragetable.m_name, ColumnStoragetable.m_struct);
    }

    /**
     * Gets the With Row ID attribute of a column. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry ColumnWithrowid = new GenericMappingEntry("ColumnWithrowid", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAColumn.fWithRowid));
    static {
        g_repositoryStringTable.put(ColumnWithrowid.m_name, ColumnWithrowid.m_struct);
    }

    /**
     * Gets the checking attribute of a primary or a unique key. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for INITIALLY IMMEDIATE.<br>
     * - "2" for INITIALLY DEFERRED.<br>
     */
    public static GenericMappingEntry PrimaryuniquekeyChecking = new GenericMappingEntry(
            "PrimaryuniquekeyChecking", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAPrimaryUnique.fChecking));
    static {
        g_repositoryStringTable.put(PrimaryuniquekeyChecking.m_name,
                PrimaryuniquekeyChecking.m_struct);
    }

    /**
     * Gets the deferrable attribute of a primary or a unique key. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry PrimaryuniquekeyDeferrable = new GenericMappingEntry(
            "PrimaryuniquekeyDeferrable", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAPrimaryUnique.fDeferrable));
    static {
        g_repositoryStringTable.put(PrimaryuniquekeyDeferrable.m_name,
                PrimaryuniquekeyDeferrable.m_struct);
    }

    /**
     * Gets the exception table associated to a primary or a unique key. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry PrimaryuniquekeyExceptiontable = new GenericMappingEntry(
            "PrimaryuniquekeyExceptiontable", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORAPrimaryUnique.fExceptionTable));
    static {
        g_repositoryStringTable.put(PrimaryuniquekeyExceptiontable.m_name,
                PrimaryuniquekeyExceptiontable.m_struct);
    }

    /**
     * Tells whether a primary or a unique key is enabled. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry PrimaryuniquekeyEnabled = new GenericMappingEntry(
            "PrimaryuniquekeyEnabled", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAPrimaryUnique.fEnabled));
    static {
        g_repositoryStringTable.put(PrimaryuniquekeyEnabled.m_name,
                PrimaryuniquekeyEnabled.m_struct);
    }

    /**
     * Tells whether a primary or a unique key is validated. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry PrimaryuniquekeyValidated = new GenericMappingEntry(
            "PrimaryuniquekeyValidated", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAPrimaryUnique.fValidated));
    static {
        g_repositoryStringTable.put(PrimaryuniquekeyValidated.m_name,
                PrimaryuniquekeyValidated.m_struct);
    }

    /**
     * Gets the checking attribute of a foreign key. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for INITIALLY IMMEDIATE.<br>
     * - "2" for INITIALLY DEFERRED.<br>
     */
    public static GenericMappingEntry ForeignkeyChecking = new GenericMappingEntry(
            "ForeignkeyChecking", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAForeign.fChecking));
    static {
        g_repositoryStringTable.put(ForeignkeyChecking.m_name, ForeignkeyChecking.m_struct);
    }

    /**
     * Gets the deferrable attribute of a foreign key. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry ForeignkeyDeferrable = new GenericMappingEntry(
            "ForeignkeyDeferrable", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAForeign.fDeferrable));
    static {
        g_repositoryStringTable.put(ForeignkeyDeferrable.m_name, ForeignkeyDeferrable.m_struct);
    }

    /**
     * Gets the exception table associated to a foreign key. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ForeignkeyExceptiontable = new GenericMappingEntry(
            "ForeignkeyExceptiontable", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORAForeign.fExceptionTable));
    static {
        g_repositoryStringTable.put(ForeignkeyExceptiontable.m_name,
                ForeignkeyExceptiontable.m_struct);
    }

    /**
     * Tells whether a foreign key is enabled. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry ForeignkeyEnabled = new GenericMappingEntry(
            "ForeignkeyEnabled", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAForeign.fEnabled));
    static {
        g_repositoryStringTable.put(ForeignkeyEnabled.m_name, ForeignkeyEnabled.m_struct);
    }

    /**
     * Tells whether a foreign key is validated. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry ForeignkeyValidated = new GenericMappingEntry(
            "ForeignkeyValidated", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAForeign.fValidated));
    static {
        g_repositoryStringTable.put(ForeignkeyValidated.m_name, ForeignkeyValidated.m_struct);
    }

    /**
     * Gets the checking attribute of a check constraint. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for INITIALLY IMMEDIATE.<br>
     * - "2" for INITIALLY DEFERRED.<br>
     */
    public static GenericMappingEntry CheckChecking = new GenericMappingEntry("CheckChecking", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORACheck.fChecking));
    static {
        g_repositoryStringTable.put(CheckChecking.m_name, CheckChecking.m_struct);
    }

    /**
     * Gets the deferrable attribute of a check constraint. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry CheckDeferrable = new GenericMappingEntry("CheckDeferrable", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORACheck.fDeferrable));
    static {
        g_repositoryStringTable.put(CheckDeferrable.m_name, CheckDeferrable.m_struct);
    }

    /**
     * Gets the exception table associated to a check constraint. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry CheckExceptiontable = new GenericMappingEntry(
            "CheckExceptiontable", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORACheck.fExceptionTable));
    static {
        g_repositoryStringTable.put(CheckExceptiontable.m_name, CheckExceptiontable.m_struct);
    }

    /**
     * Tells whether a check constraint is enabled. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry CheckEnabled = new GenericMappingEntry("CheckEnabled", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORACheck.fEnabled));
    static {
        g_repositoryStringTable.put(CheckEnabled.m_name, CheckEnabled.m_struct);
    }

    /**
     * Tells whether a check constraint is validated. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry CheckValidated = new GenericMappingEntry("CheckValidated", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORACheck.fValidated));
    static {
        g_repositoryStringTable.put(CheckValidated.m_name, CheckValidated.m_struct);
    }

    /**
     * Gets the bitmap attribute of an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry IndexBitmap = new GenericMappingEntry("IndexBitmap", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAIndex.fBitmap));
    static {
        g_repositoryStringTable.put(IndexBitmap.m_name, IndexBitmap.m_struct);
    }

    /**
     * Gets the buffer pool attribute of an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for KEEP option. - "1" for RECYCLE option. - "2" for DEFAULT option.
     */
    public static GenericMappingEntry IndexBufferpool = new GenericMappingEntry("IndexBufferpool", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAIndex.fBufferPool));
    static {
        g_repositoryStringTable.put(IndexBufferpool.m_name, IndexBufferpool.m_struct);
    }

    /**
     * Gets the category of an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for a GLOBAL index.<br>
     * - "2" for a LOCAL index.<br>
     */
    public static GenericMappingEntry IndexCategory = new GenericMappingEntry("IndexCategory", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAIndex.fCategory));
    static {
        g_repositoryStringTable.put(IndexCategory.m_name, IndexCategory.m_struct);
    }

    /**
     * Gets the free list of an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry IndexFreelist = new GenericMappingEntry("IndexFreelist", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAIndex.fFreelists));
    static {
        g_repositoryStringTable.put(IndexFreelist.m_name, IndexFreelist.m_struct);
    }

    /**
     * Gets the free list groups of an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry IndexFreelistgroups = new GenericMappingEntry(
            "IndexFreelistgroups", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAIndex.fFreelistGroups));
    static {
        g_repositoryStringTable.put(IndexFreelistgroups.m_name, IndexFreelistgroups.m_struct);
    }

    /**
     * Gets the initial extent attribute of an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry IndexInitialextent = new GenericMappingEntry(
            "IndexInitialextent", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAIndex.fInitialExtent));
    static {
        g_repositoryStringTable.put(IndexInitialextent.m_name, IndexInitialextent.m_struct);
    }

    /**
     * Gets the size of the initial extent attribute of an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for Kilobyte size unit.<br>
     * - "2" for Megabyte size unit.<br>
     */
    public static GenericMappingEntry IndexInitialextentsizeunit = new GenericMappingEntry(
            "IndexInitialextentsizeunit", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAIndex.fInitialExtentSizeUnit));
    static {
        g_repositoryStringTable.put(IndexInitialextentsizeunit.m_name,
                IndexInitialextentsizeunit.m_struct);
    }

    /**
     * Gets the initial trans attribute of an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry Indexinitrans = new GenericMappingEntry("Indexinitrans", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAIndex.fInitrans));
    static {
        g_repositoryStringTable.put(Indexinitrans.m_name, Indexinitrans.m_struct);
    }

    /**
     * Gets the log attribute of an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for LOGGING option.<br>
     * - "2" for NOLOGGING option.<br>
     */
    public static GenericMappingEntry IndexLog = new GenericMappingEntry("IndexLog", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAIndex.fLog));
    static {
        g_repositoryStringTable.put(IndexLog.m_name, IndexLog.m_struct);
    }

    /**
     * Gets the maximum extent attribute of an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry IndexMaxextent = new GenericMappingEntry("IndexMaxextent", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAIndex.fMaxExtent));
    static {
        g_repositoryStringTable.put(IndexMaxextent.m_name, IndexMaxextent.m_struct);
    }

    /**
     * Gets the maximum trans attribute of an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry IndexMaxtrans = new GenericMappingEntry("IndexMaxtrans", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAIndex.fMaxtrans));
    static {
        g_repositoryStringTable.put(IndexMaxtrans.m_name, IndexMaxtrans.m_struct);
    }

    /**
     * Gets the minimum extent attribute of an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry IndexMinextent = new GenericMappingEntry("IndexMinextent", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAIndex.fMinExtent));
    static {
        g_repositoryStringTable.put(IndexMinextent.m_name, IndexMinextent.m_struct);
    }

    /**
     * Gets the next extent attribute of an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry IndexNextextent = new GenericMappingEntry("IndexNextextent", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAIndex.fNextExtent));
    static {
        g_repositoryStringTable.put(IndexNextextent.m_name, IndexNextextent.m_struct);
    }

    /**
     * Gets the units of the next extent attribute of an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for Kilobyte size unit.<br>
     * - "2" for Megabyte size unit.<br>
     */
    public static GenericMappingEntry IndexNextextentsizeunit = new GenericMappingEntry(
            "IndexNextextentsizeunit", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAIndex.fNextExtentSizeUnit));
    static {
        g_repositoryStringTable.put(IndexNextextentsizeunit.m_name,
                IndexNextextentsizeunit.m_struct);
    }

    /**
     * Gets the parallel attribute of an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for NOPARALLEL option.<br>
     * - "2" for PARALLEL option.<br>
     */
    public static GenericMappingEntry IndexParallel = new GenericMappingEntry("IndexParallel", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAIndex.fParallel));
    static {
        g_repositoryStringTable.put(IndexParallel.m_name, IndexParallel.m_struct);
    }

    /**
     * Gets the parallel degree attribute of an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry IndexParalleldegree = new GenericMappingEntry(
            "IndexParalleldegree", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAIndex.fParallelDegree));
    static {
        g_repositoryStringTable.put(IndexParalleldegree.m_name, IndexParalleldegree.m_struct);
    }

    /**
     * Lists all the partitions in an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry IndexPartitions = new GenericMappingEntry("IndexPartitions", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORAPartition.metaClass));
    static {
        g_repositoryStringTable.put(IndexPartitions.m_name, IndexPartitions.m_struct);
    }

    /**
     * Lists all the partition key columns associated to an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry IndexPartitionkeycolumns = new GenericMappingEntry(
            "IndexPartitionkeycolumns", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbORAIndex.fPartitionKeyColumns));
    static {
        g_repositoryStringTable.put(IndexPartitionkeycolumns.m_name,
                IndexPartitionkeycolumns.m_struct);
    }

    /**
     * Gets the partitioning method of an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for PARTITION BY RANGE.<br>
     * - "2" for PARTITION BY HASH.<br>
     */
    public static GenericMappingEntry IndexPartitioningmethod = new GenericMappingEntry(
            "IndexPartitioningmethod", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAIndex.fPartitioningMethod));
    static {
        g_repositoryStringTable.put(IndexPartitioningmethod.m_name,
                IndexPartitioningmethod.m_struct);
    }

    /**
     * Gets the partitioning method of an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for PARTITION BY RANGE.<br>
     * - "2" for PARTITION BY HASH.<br>
     * 
     * @deprecated
     * @see org.modelsphere.sms.plugins.generic.GenericMapping#IndexPartitioningmethod
     */

    public static GenericMappingEntry IndexPctfree = new GenericMappingEntry("IndexPctfree", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAIndex.fPctfree));
    static {
        g_repositoryStringTable.put(IndexPctfree.m_name, IndexPctfree.m_struct);
    }

    /**
     * Gets the increase percentage of an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry IndexPctincrease = new GenericMappingEntry(
            "IndexPctincrease", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAIndex.fPctIncrease));
    static {
        g_repositoryStringTable.put(IndexPctincrease.m_name, IndexPctincrease.m_struct);
    }

    /**
     * Gets the sorting option of an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for NOSORT option.<br>
     * - "2" for SORT option.<br>
     */
    public static GenericMappingEntry IndexSorting = new GenericMappingEntry("IndexSorting", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAIndex.fSorting));
    static {
        g_repositoryStringTable.put(IndexSorting.m_name, IndexSorting.m_struct);
    }

    /**
     * Gets the tablespace associated to an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry IndexTablespace = new GenericMappingEntry("IndexTablespace", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORAIndex.fTablespace));
    static {
        g_repositoryStringTable.put(IndexTablespace.m_name, IndexTablespace.m_struct);
    }

    /**
     * Gets the unlimited extents value of an index. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry IndexUnlimitedextents = new GenericMappingEntry(
            "IndexUnlimitedextents", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAIndex.fUnlimitedExtents));
    static {
        g_repositoryStringTable.put(IndexUnlimitedextents.m_name, IndexUnlimitedextents.m_struct);
    }

    /**
     * Gets the parent alias of a trigger. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry TriggerParentalias = new GenericMappingEntry(
            "TriggerParentalias", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATrigger.fParentAlias));
    static {
        g_repositoryStringTable.put(TriggerParentalias.m_name, TriggerParentalias.m_struct);
    }

    /**
     * Tells whether a trigger is a row trigger. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry TriggerRowtrigger = new GenericMappingEntry(
            "TriggerRowtrigger", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATrigger.fRowTrigger));
    static {
        g_repositoryStringTable.put(TriggerRowtrigger.m_name, TriggerRowtrigger.m_struct);
    }

    /**
     * Gets the time attribute of a trigger. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for BEFORE option.<br>
     * - "2" for AFTER option.<br>
     * - "3" for INSTEAD OF option.<br>
     */
    public static GenericMappingEntry TriggerTime = new GenericMappingEntry("TriggerTime", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORATrigger.fTime));
    static {
        g_repositoryStringTable.put(TriggerTime.m_name, TriggerTime.m_struct);
    }

    /**
     * Gets the buffer pool option of a partition. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for KEEP option. - "1" for RECYCLE option. - "2" for DEFAULT option.
     */
    public static GenericMappingEntry PartitionBufferpool = new GenericMappingEntry(
            "PartitionBufferpool", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAPartition.fBufferPool));
    static {
        g_repositoryStringTable.put(PartitionBufferpool.m_name, PartitionBufferpool.m_struct);
    }

    /**
     * Gets the free lists value of a partition. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry PartitionFreelists = new GenericMappingEntry(
            "PartitionFreelists", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAPartition.fFreelists));
    static {
        g_repositoryStringTable.put(PartitionFreelists.m_name, PartitionFreelists.m_struct);
    }

    /**
     * Gets the free list groups value of a partition. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry PartitionFreelistgroups = new GenericMappingEntry(
            "PartitionFreelistgroups", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAPartition.fFreelistGroups));
    static {
        g_repositoryStringTable.put(PartitionFreelistgroups.m_name,
                PartitionFreelistgroups.m_struct);
    }

    /**
     * Retrieves the index to which a partition belongs. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry PartitionIndex = new GenericMappingEntry("PartitionIndex", //NOT LOCALIZABLE
            new GenericMappingComposite(DbORAIndex.metaClass));
    static {
        g_repositoryStringTable.put(PartitionIndex.m_name, PartitionIndex.m_struct);
    }

    /**
     * Gets the initial extent value of a partition. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry PartitionInitialextent = new GenericMappingEntry(
            "PartitionInitialextent", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAPartition.fInitialExtent));
    static {
        g_repositoryStringTable.put(PartitionInitialextent.m_name, PartitionInitialextent.m_struct);
    }

    /**
     * Gets the units of the initial extent value of a partition. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for Kilobyte size unit.<br>
     * - "2" for Megabyte size unit.<br>
     */
    public static GenericMappingEntry PartitionInitialextentsizeunit = new GenericMappingEntry(
            "PartitionInitialextentsizeunit", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAPartition.fInitialExtentSizeUnit));
    static {
        g_repositoryStringTable.put(PartitionInitialextentsizeunit.m_name,
                PartitionInitialextentsizeunit.m_struct);
    }

    /**
     * Gets the initial trans value of a partition. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry PartitionInitrans = new GenericMappingEntry(
            "PartitionInitrans", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAPartition.fInitrans));
    static {
        g_repositoryStringTable.put(PartitionInitrans.m_name, PartitionInitrans.m_struct);
    }

    /**
     * Lists all the lob storages of a partition. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry PartitionLobstorages = new GenericMappingEntry(
            "PartitionLobstorages", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORALobStorage.metaClass));
    static {
        g_repositoryStringTable.put(PartitionLobstorages.m_name, PartitionLobstorages.m_struct);
    }

    /**
     * Gets the log attribute of a partition. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for LOGGING option.<br>
     * - "2" for NOLOGGING option.<br>
     */
    public static GenericMappingEntry PartitionLog = new GenericMappingEntry("PartitionLog", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAPartition.fLog));
    static {
        g_repositoryStringTable.put(PartitionLog.m_name, PartitionLog.m_struct);
    }

    /**
     * Gets the maximum extents value of a partition. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry PartitionMaxextents = new GenericMappingEntry(
            "PartitionMaxextents", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAPartition.fMaxExtents));
    static {
        g_repositoryStringTable.put(PartitionMaxextents.m_name, PartitionMaxextents.m_struct);
    }

    /**
     * Gets the maximum trans value of a partition. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry PartitionMaxtrans = new GenericMappingEntry(
            "PartitionMaxtrans", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAPartition.fMaxtrans));
    static {
        g_repositoryStringTable.put(PartitionMaxtrans.m_name, PartitionMaxtrans.m_struct);
    }

    /**
     * Gets the minimum extents value of a partition. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry PartitionMinextents = new GenericMappingEntry(
            "PartitionMinextents", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAPartition.fMinExtents));
    static {
        g_repositoryStringTable.put(PartitionMinextents.m_name, PartitionMinextents.m_struct);
    }

    /**
     * Gets the next extents value of a partition. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry PartitionNextextent = new GenericMappingEntry(
            "PartitionNextextent", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAPartition.fNextExtent));
    static {
        g_repositoryStringTable.put(PartitionNextextent.m_name, PartitionNextextent.m_struct);
    }

    /**
     * Gets the units of the next extents value of a partition. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for Kilobyte size unit.<br>
     * - "2" for Megabyte size unit.<br>
     */
    public static GenericMappingEntry PartitionNextextentsizeunit = new GenericMappingEntry(
            "PartitionNextextentsizeunit", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAPartition.fNextExtentSizeUnit));
    static {
        g_repositoryStringTable.put(PartitionNextextentsizeunit.m_name,
                PartitionNextextentsizeunit.m_struct);
    }

    /**
     * Lists all the sub partitions of a given partition. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry PartitionSubPartitions = new GenericMappingEntry(
            "PartitionSubPartitions", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORAAbsPartition.metaClass));
    static {
        g_repositoryStringTable.put(PartitionSubPartitions.m_name, PartitionSubPartitions.m_struct);
    }

    /**
     * Gets the free percentage of a partition. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry PartitionPctfree = new GenericMappingEntry(
            "PartitionPctfree", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAPartition.fPctfree));
    static {
        g_repositoryStringTable.put(PartitionPctfree.m_name, PartitionPctfree.m_struct);
    }

    /**
     * Gets the increase percentage of a partition. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry PartitionPctincrease = new GenericMappingEntry(
            "PartitionPctincrease", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAPartition.fPctIncrease));
    static {
        g_repositoryStringTable.put(PartitionPctincrease.m_name, PartitionPctincrease.m_struct);
    }

    /**
     * Gets the percentage used attribute of a partition. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry PartitionPctused = new GenericMappingEntry(
            "PartitionPctused", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAPartition.fPctused));
    static {
        g_repositoryStringTable.put(PartitionPctused.m_name, PartitionPctused.m_struct);
    }

    /**
     * Retrieves the table to which a partition belongs. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry PartitionTable = new GenericMappingEntry("PartitionTable", //NOT LOCALIZABLE
            new GenericMappingComposite(DbORATable.metaClass));
    static {
        g_repositoryStringTable.put(PartitionTable.m_name, PartitionTable.m_struct);
    }

    /**
     * Gets the tablespace associated to a partition. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry PartitionTablespace = new GenericMappingEntry(
            "PartitionTablespace", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORAAbsPartition.fTablespace));
    static {
        g_repositoryStringTable.put(PartitionTablespace.m_name, PartitionTablespace.m_struct);
    }

    /**
     * Gets the unlimited extents value of a partition. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry PartitionUnlimitedextents = new GenericMappingEntry(
            "PartitionUnlimitedextents", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAPartition.fUnlimitedExtents));
    static {
        g_repositoryStringTable.put(PartitionUnlimitedextents.m_name,
                PartitionUnlimitedextents.m_struct);
    }

    /**
     * Gets the value of a partition. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry PartitionValue = new GenericMappingEntry("PartitionValue", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAPartition.fValueList));
    static {
        g_repositoryStringTable.put(PartitionValue.m_name, PartitionValue.m_struct);
    }

    /**
     * Lists all the subpartitions of a given partition. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry PartitionSubpartitions = new GenericMappingEntry(
            "PartitionSubpartitions", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORASubPartition.metaClass));
    static {
        g_repositoryStringTable.put(PartitionSubpartitions.m_name, PartitionSubpartitions.m_struct);
    }

    /**
     * Retrieve the partition of a given subpartition. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry SubpartitionPartition = new GenericMappingEntry(
            "SubpartitionPartition", //NOT LOCALIZABLE
            new GenericMappingComposite(DbORAPartition.metaClass));
    static {
        g_repositoryStringTable.put(SubpartitionPartition.m_name, SubpartitionPartition.m_struct);
    }

    /**
     * Lists all the sequences contained under a data model. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DatamodelSequences = new GenericMappingEntry(
            "DatamodelSequences", //NOT LOCALIZABLE
            new GenericMappingComponents(DbORASequence.metaClass));
    static {
        g_repositoryStringTable.put(DatamodelSequences.m_name, DatamodelSequences.m_struct);
    }

    /**
     * Gets the cache attribute of a sequence. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry SequenceCache = new GenericMappingEntry("SequenceCache", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORASequence.fCache));
    static {
        g_repositoryStringTable.put(SequenceCache.m_name, SequenceCache.m_struct);
    }

    /**
     * Gets the cache value attribute of a sequence. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry SequenceCachevalue = new GenericMappingEntry(
            "SequenceCachevalue", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORASequence.fCacheValue));
    static {
        g_repositoryStringTable.put(SequenceCachevalue.m_name, SequenceCachevalue.m_struct);
    }

    /**
     * Gets the cycle attribute of a sequence. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry SequenceCycle = new GenericMappingEntry("SequenceCycle", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORASequence.fCycle));
    static {
        g_repositoryStringTable.put(SequenceCycle.m_name, SequenceCycle.m_struct);
    }

    /**
     * Gets the increment attribute of a sequence. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry SequenceIncrement = new GenericMappingEntry(
            "SequenceIncrement", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORASequence.fIncrement));
    static {
        g_repositoryStringTable.put(SequenceIncrement.m_name, SequenceIncrement.m_struct);
    }

    /**
     * Gets the maximum value of a sequence. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry SequenceMaxvalue = new GenericMappingEntry(
            "SequenceMaxvalue", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORASequence.fMaxValue));
    static {
        g_repositoryStringTable.put(SequenceMaxvalue.m_name, SequenceMaxvalue.m_struct);
    }

    /**
     * Gets the minimum value of a sequence. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry SequenceMinvalue = new GenericMappingEntry(
            "SequenceMinvalue", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORASequence.fMinValue));
    static {
        g_repositoryStringTable.put(SequenceMinvalue.m_name, SequenceMinvalue.m_struct);
    }

    /**
     * Gets the order attribute of a sequence. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry SequenceOrder = new GenericMappingEntry("SequenceOrder", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORASequence.fOrder));
    static {
        g_repositoryStringTable.put(SequenceOrder.m_name, SequenceOrder.m_struct);
    }

    /**
     * Gets the start attribute of a sequence. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry SequenceStart = new GenericMappingEntry("SequenceStart", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORASequence.fStartValue));
    static {
        g_repositoryStringTable.put(SequenceStart.m_name, SequenceStart.m_struct);
    }

    /**
     * Gets the user who owns the sequence, if any. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry SequenceUser = new GenericMappingEntry("SequenceUser", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbORASequence.fUser));
    static {
        g_repositoryStringTable.put(SequenceUser.m_name, SequenceUser.m_struct);
    }

    /**
     * Gets the deterministic attribute of a procedure. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry ProcedureDeterministic = new GenericMappingEntry(
            "ProcedureDeterministic", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAProcedure.fDeterministic));
    static {
        g_repositoryStringTable.put(ProcedureDeterministic.m_name, ProcedureDeterministic.m_struct);
    }

    /**
     * Gets the invoker rights attribute of a procedure. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * "1" for CURRENT USER.<br>
     * "2" for DEFINER.<br>
     */
    public static GenericMappingEntry ProcedureInvokerrights = new GenericMappingEntry(
            "ProcedureInvokerrights", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAProcedure.fInvokerRights));
    static {
        g_repositoryStringTable.put(ProcedureInvokerrights.m_name, ProcedureInvokerrights.m_struct);
    }

    /**
     * Gets the language attribute of a procedure. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * "1" for PLSQL.<br>
     * "2" for JAVA.<br>
     */
    public static GenericMappingEntry ProcedureLanguage = new GenericMappingEntry(
            "ProcedureLanguage", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAProcedure.fLanguage));
    static {
        g_repositoryStringTable.put(ProcedureLanguage.m_name, ProcedureLanguage.m_struct);
    }

    /**
     * Gets the parallel attribute of a procedure. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry ProcedureParallel = new GenericMappingEntry(
            "ProcedureParallel", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAProcedure.fParallel));
    static {
        g_repositoryStringTable.put(ProcedureParallel.m_name, ProcedureParallel.m_struct);
    }

    /**
     * Tells whether the NOCOPY attribute of a procedure parameter is enabled. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: true or false.<br>
     */
    public static GenericMappingEntry OrparameterIsnocopy = new GenericMappingEntry(
            "OrparameterIsnocopy", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAParameter.fNocopy));
    static {
        g_repositoryStringTable.put(OrparameterIsnocopy.m_name, OrparameterIsnocopy.m_struct);
    }

    /**
     * Gets the invoker rights attribute of a package. <br>
     * Target System: <b>Oracle</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * "1" for CURRENT USER.<br>
     * "2" for DEFINER.<br>
     */
    public static GenericMappingEntry PackageInvokerrights = new GenericMappingEntry(
            "PackageInvokerrights", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORAPackage.fInvokerRights));
    static {
        g_repositoryStringTable.put(PackageInvokerrights.m_name, PackageInvokerrights.m_struct);
    }

    /**
     * <b>SECTION 6: DB2-SPECIFIC CONCEPTS (ONLY FOR THE DB2-UDB TARGET SYSTEM).</b> <br>
     */
    public static Section Section_6;

    /**
     * Gets the units of the lob size of a column. <br>
     * Target System: <b>DB2-UDB</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for Kilobyte size unit.<br>
     * - "2" for Megabyte size unit.<br>
     */
    public static GenericMappingEntry IbmColumnlobsizeunit = new GenericMappingEntry(
            "IbmColumnlobsizeunit", //NOT LOCALIZABLE
            new GenericMappingProperty(DbIBMColumn.fLobSizeUnit));
    static {
        g_repositoryStringTable.put(IbmColumnlobsizeunit.m_name, IbmColumnlobsizeunit.m_struct);
    }

    /**
     * Gets the time attribute of a trigger. <br>
     * Target System: <b>DB2-UDB</b><br>
     * Type: <b>Attribute</b><br>
     * Returns:<br>
     * - "1" for NOCASCADEBEFORE.<br>
     * - "2" for AFTER.<br>
     */
    public static GenericMappingEntry IbmTriggerTime = new GenericMappingEntry("IbmTriggerTime", //NOT LOCALIZABLE
            new GenericMappingProperty(DbIBMTrigger.fTime));
    static {
        g_repositoryStringTable.put(IbmTriggerTime.m_name, IbmTriggerTime.m_struct);
    }

    /**
     * Gets the old table alias of a trigger. <br>
     * Target System: <b>DB2-UDB</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry TriggerOldtablealias = new GenericMappingEntry(
            "TriggerOldtablealias", //NOT LOCALIZABLE
            new GenericMappingProperty(DbIBMTrigger.fOldTable));
    static {
        g_repositoryStringTable.put(TriggerOldtablealias.m_name, TriggerOldtablealias.m_struct);
    }

    /**
     * Gets the new table alias of a trigger. <br>
     * Target System: <b>DB2-UDB</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry Triggernewtablealias = new GenericMappingEntry(
            "Triggernewtablealias", //NOT LOCALIZABLE
            new GenericMappingProperty(DbIBMTrigger.fNewTable));
    static {
        g_repositoryStringTable.put(Triggernewtablealias.m_name, Triggernewtablealias.m_struct);
    }

    /**
     * DatabasePartitionGroup<br>
     * <br>
     * Target System: <b>DB2-UDB</b><br>
     * Type: <b>Concept</b><br>
     */
    public static GenericMappingEntry IBMDatabasePartitionGroup = new GenericMappingEntry(
            "IBMDatabasePartitionGroup", //NOT LOCALIZABLE
            new GenericMappingConcept(DbIBMDbPartitionGroup.class, DbIBMDbPartitionGroup.metaClass));
    static {
        g_repositoryStringTable.put(IBMDatabasePartitionGroup.m_name,
                IBMDatabasePartitionGroup.m_struct);
    }

    /**
     * BufferPool<br>
     * <br>
     * Target System: <b>DB2-UDB</b><br>
     * Type: <b>Concept</b><br>
     */
    public static GenericMappingEntry IBMBufferPool = new GenericMappingEntry("IBMBufferPool", //NOT LOCALIZABLE
            new GenericMappingConcept(DbIBMBufferPool.class, DbIBMBufferPool.metaClass));
    static {
        g_repositoryStringTable.put(IBMBufferPool.m_name, IBMBufferPool.m_struct);
    }

    /**
     * Tablespace<br>
     * <br>
     * Target System: <b>DB2-UDB</b><br>
     * Type: <b>Concept</b><br>
     */
    public static GenericMappingEntry IBMTablespace = new GenericMappingEntry("IBMTablespace", //NOT LOCALIZABLE
            new GenericMappingConcept(DbIBMTablespace.class, DbIBMTablespace.metaClass));
    static {
        g_repositoryStringTable.put(IBMTablespace.m_name, IBMTablespace.m_struct);
    }

    /**
     * <b>SECTION 7: INFORMIX-SPECIFIC CONCEPTS (ONLY FOR THE INFORMIX TARGET SYSTEM).</b> <br>
     */
    //procedure, parameter, fragment, dbspace, sbspace,
    public static Section Section_7;

    /**
     * Gets the logging option of a database. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for LOGGING option.<br>
     * - "2" for NOLOGGING option.<br>
     */
    public static GenericMappingEntry DatabaseLogging = new GenericMappingEntry("DatabaseLogging", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFDatabase.fLogging));
    static {
        g_repositoryStringTable.put(DatabaseLogging.m_name, DatabaseLogging.m_struct);
    }

    /**
     * Gets the dbspace associated to a database. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DatabaseDbspace = new GenericMappingEntry("DatabaseDbspace", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbINFDatabase.fDbspace));
    static {
        g_repositoryStringTable.put(DatabaseDbspace.m_name, DatabaseDbspace.m_struct);
    }

    /**
     * Gets the category of a table. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for RAW option.<br>
     * - "2" for STATIC option.<br>
     * - "3" for OPERATIONAL option.<br>
     * - "4" for TEMP option.<br>
     * - "5" for SCRATCH option.<br>
     * - "6" for STANDARD option.<br>
     */
    public static GenericMappingEntry INFTableCategory = new GenericMappingEntry(
            "INFTableCategory", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFTable.fCategory));
    static {
        g_repositoryStringTable.put(INFTableCategory.m_name, INFTableCategory.m_struct);
    }

    /**
     * Gets the With row IDs attribute of a table. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry INFTableWithrowids = new GenericMappingEntry(
            "INFTableWithrowids", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFTable.fWithRowIDs));
    static {
        g_repositoryStringTable.put(INFTableWithrowids.m_name, INFTableWithrowids.m_struct);
    }

    /**
     * Gets the dbspace associated to a table. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry INFTableDbspace = new GenericMappingEntry("INFTableDbspace", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbINFTable.fDbspace));
    static {
        g_repositoryStringTable.put(INFTableDbspace.m_name, INFTableDbspace.m_struct);
    }

    /**
     * Gets the extent size of a table. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry INFTableExtentsize = new GenericMappingEntry(
            "INFTableExtentsize", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFTable.fExtentSize));
    static {
        g_repositoryStringTable.put(INFTableExtentsize.m_name, INFTableExtentsize.m_struct);
    }

    /**
     * Gets the next extent size of a table. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry INFTableNextextentsize = new GenericMappingEntry(
            "INFTableNextextentsize", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFTable.fNextExtentSize));
    static {
        g_repositoryStringTable.put(INFTableNextextentsize.m_name, INFTableNextextentsize.m_struct);
    }

    /**
     * Gets the lock mode of a table. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for PAGE mode.<br>
     * - "2" for ROW mode.<br>
     * - "3" for TABLE mode.<br>
     */
    public static GenericMappingEntry INFTableLockmode = new GenericMappingEntry(
            "INFTableLockmode", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFTable.fLockMode));
    static {
        g_repositoryStringTable.put(INFTableLockmode.m_name, INFTableLockmode.m_struct);
    }

    /**
     * Gets the fragmentation distribution scheme of a table. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for EXPRESSION scheme.<br>
     * - "2" for HASH scheme.<br>
     * - "3" for HYBRID_HASH_EXPRESSION scheme.<br>
     * - "4" for ROUNDROBIN scheme.<br>
     * - "5" for RANGE scheme.<br>
     * - "6" for HYBRID_RANGE scheme.<br>
     */
    public static GenericMappingEntry INFTableFragmentationdistributionscheme = new GenericMappingEntry(
            "INFTableFragmentationdistributionscheme", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFTable.fFragmentationDistribScheme));
    static {
        g_repositoryStringTable.put(INFTableFragmentationdistributionscheme.m_name,
                INFTableFragmentationdistributionscheme.m_struct);
    }

    /**
     * Gets the fragmentation minimum range value of a table. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry INFTableFragmentationminrangevalue = new GenericMappingEntry(
            "INFTableFragmentationminrangevalue", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFTable.fFragmentationMinRangeValue));
    static {
        g_repositoryStringTable.put(INFTableFragmentationminrangevalue.m_name,
                INFTableFragmentationminrangevalue.m_struct);
    }

    /**
     * Gets the fragmentation maximum range value of a table. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry INFTableFragmentationmaxrangevalue = new GenericMappingEntry(
            "INFTableFragmentationmaxrangevalue", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFTable.fFragmentationMaxRangeValue));
    static {
        g_repositoryStringTable.put(INFTableFragmentationmaxrangevalue.m_name,
                INFTableFragmentationmaxrangevalue.m_struct);
    }

    /**
     * Gets the fragmentation key columns associated to a table. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry INFTableFragmentationkeycolumns = new GenericMappingEntry(
            "INFTableFragmentationkeycolumns", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbINFTable.fFragmentationKeyColumns));
    static {
        g_repositoryStringTable.put(INFTableFragmentationkeycolumns.m_name,
                INFTableFragmentationkeycolumns.m_struct);
    }

    /**
     * Lists all the fragments contained in a table. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry INFTableFragments = new GenericMappingEntry(
            "INFTableFragments", //NOT LOCALIZABLE
            new GenericMappingComponents(DbINFFragment.metaClass));
    static {
        g_repositoryStringTable.put(INFTableFragments.m_name, INFTableFragments.m_struct);
    }

    /**
     * Tells whether a view has a check option. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry ViewCheckoption = new GenericMappingEntry("ViewCheckoption", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFView.fCheckOption));
    static {
        g_repositoryStringTable.put(ViewCheckoption.m_name, ViewCheckoption.m_struct);
    }

    /**
     * Lists all the sbspaces associated to a column. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ColumnSbspaces = new GenericMappingEntry("ColumnSbspaces", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbINFColumn.fSbspaces));
    static {
        g_repositoryStringTable.put(ColumnSbspaces.m_name, ColumnSbspaces.m_struct);
    }

    /**
     * Gets the fragmentation key table associated to a column. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ColumnFragmentationkeytable = new GenericMappingEntry(
            "ColumnFragmentationkeytable", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbINFColumn.fFragmentationKeyTable));
    static {
        g_repositoryStringTable.put(ColumnFragmentationkeytable.m_name,
                ColumnFragmentationkeytable.m_struct);
    }

    /**
     * Lists all the fragmentation key indexes associated to a column. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ColumnFragmentationkeyindexes = new GenericMappingEntry(
            "ColumnFragmentationkeyindexes", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbINFColumn.fFragmentationKeyIndexes));
    static {
        g_repositoryStringTable.put(ColumnFragmentationkeyindexes.m_name,
                ColumnFragmentationkeyindexes.m_struct);
    }

    /**
     * Gets the mode of a primary or a unique key. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for DISABLED mode.<br>
     * - "2" for ENABLED mode.<br>
     * - "3" for FILTERING mode.<br>
     * - "4" for FILTERING_WITHOUT_ERROR mode.<br>
     * - "5" for FILTERING_WITH_ERROR mode.<br>
     */
    public static GenericMappingEntry PrimaryuniquekeyMode = new GenericMappingEntry(
            "PrimaryuniquekeyMode", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFPrimaryUnique.fMode));
    static {
        g_repositoryStringTable.put(PrimaryuniquekeyMode.m_name, PrimaryuniquekeyMode.m_struct);
    }

    /**
     * Gets the user who owns a primary or a unique key, if any. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry PrimaryuniquekeyUser = new GenericMappingEntry(
            "PrimaryuniquekeyUser", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbINFPrimaryUnique.fUser));
    static {
        g_repositoryStringTable.put(PrimaryuniquekeyUser.m_name, PrimaryuniquekeyUser.m_struct);
    }

    /**
     * Gets the mode of a foreign key. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for DISABLED mode.<br>
     * - "2" for ENABLED mode.<br>
     * - "3" for FILTERING mode.<br>
     * - "4" for FILTERING_WITHOUT_ERROR mode.<br>
     * - "5" for FILTERING_WITH_ERROR mode.<br>
     */
    public static GenericMappingEntry ForeignkeyMode = new GenericMappingEntry("ForeignkeyMode", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFForeign.fMode));
    static {
        g_repositoryStringTable.put(ForeignkeyMode.m_name, ForeignkeyMode.m_struct);
    }

    /**
     * Gets the user who owns a foreign key, if any. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry ForeignkeyUser = new GenericMappingEntry("ForeignkeyUser", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbINFForeign.fUser));
    static {
        g_repositoryStringTable.put(ForeignkeyUser.m_name, ForeignkeyUser.m_struct);
    }

    /**
     * Gets the mode of a check constraint. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for DISABLED mode.<br>
     * - "2" for ENABLED mode.<br>
     * - "3" for FILTERING mode.<br>
     * - "4" for FILTERING_WITHOUT_ERROR mode.<br>
     * - "5" for FILTERING_WITH_ERROR mode.<br>
     */
    public static GenericMappingEntry CheckMode = new GenericMappingEntry("CheckMode", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFCheck.fMode));
    static {
        g_repositoryStringTable.put(CheckMode.m_name, CheckMode.m_struct);
    }

    /**
     * Gets the user who owns a check constraint, if any. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry CheckUser = new GenericMappingEntry("CheckUser", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbINFCheck.fUser));
    static {
        g_repositoryStringTable.put(CheckUser.m_name, CheckUser.m_struct);
    }

    /**
     * Tells whether an index has a cluster. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry INFIndexCluster = new GenericMappingEntry("INFIndexCluster", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFIndex.fCluster));
    static {
        g_repositoryStringTable.put(INFIndexCluster.m_name, INFIndexCluster.m_struct);
    }

    /**
     * Gets the lock mode of an index. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for COARSE mode.<br>
     * - "2" for NORMAL mode.<br>
     */
    public static GenericMappingEntry INFIndexLockmode = new GenericMappingEntry(
            "INFIndexLockmode", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFIndex.fLockMode));
    static {
        g_repositoryStringTable.put(INFIndexLockmode.m_name, INFIndexLockmode.m_struct);
    }

    /**
     * Gets online type of an index. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry INFIndexOnline = new GenericMappingEntry("INFIndexOnline", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFIndex.fOnline));
    static {
        g_repositoryStringTable.put(INFIndexOnline.m_name, INFIndexOnline.m_struct);
    }

    /**
     * Gets the fill factor of an index. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry INFIndexFillfactor = new GenericMappingEntry(
            "INFIndexFillfactor", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFIndex.fFillFactor));
    static {
        g_repositoryStringTable.put(INFIndexFillfactor.m_name, INFIndexFillfactor.m_struct);
    }

    /**
     * Gets the bitmap storage of an index. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry INFIndexBitmapstorage = new GenericMappingEntry(
            "INFIndexBitmapstorage", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFIndex.fBitmapStorage));
    static {
        g_repositoryStringTable.put(INFIndexBitmapstorage.m_name, INFIndexBitmapstorage.m_struct);
    }

    /**
     * Gets the mode of an index. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for DISABLED mode.<br>
     * - "2" for ENABLED mode.<br>
     * - "3" for FILTERING mode.<br>
     * - "4" for FILTERING_WITHOUT_ERROR mode.<br>
     * - "5" for FILTERING_WITH_ERROR mode.<br>
     */
    public static GenericMappingEntry INFIndexMode = new GenericMappingEntry("INFIndexMode", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFIndex.fMode));
    static {
        g_repositoryStringTable.put(INFIndexMode.m_name, INFIndexMode.m_struct);
    }

    /**
     * Lists all the fragmentation key columns associated to an index. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry INFIndexFragmentationkeycolumns = new GenericMappingEntry(
            "INFIndexFragmentationkeycolumns", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbINFIndex.fFragmentationKeyColumns));
    static {
        g_repositoryStringTable.put(INFIndexFragmentationkeycolumns.m_name,
                INFIndexFragmentationkeycolumns.m_struct);
    }

    /**
     * Gets the fragmentation distribution scheme of an index. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for EXPRESSION scheme.<br>
     * - "2" for HASH scheme.<br>
     * - "3" for HYBRID_HASH_EXPRESSION scheme.<br>
     * - "4" for ROUNDROBIN scheme.<br>
     * - "5" for RANGE scheme.<br>
     * - "6" for HYBRID_RANGE scheme.<br>
     */
    public static GenericMappingEntry INFIndexFragmentationdistributionscheme = new GenericMappingEntry(
            "INFIndexFragmentationdistributionscheme", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFIndex.fFragmentationDistribScheme));
    static {
        g_repositoryStringTable.put(INFIndexFragmentationdistributionscheme.m_name,
                INFIndexFragmentationdistributionscheme.m_struct);
    }

    /**
     * Gets the dbspace associated to an index. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry INFIndexDbspace = new GenericMappingEntry("INFIndexDbspace", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbINFIndex.fDbspace));
    static {
        g_repositoryStringTable.put(INFIndexDbspace.m_name, INFIndexDbspace.m_struct);
    }

    /**
     * Lists all the fragments contained in an index. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry INFIndexFragments = new GenericMappingEntry(
            "INFIndexFragments", //NOT LOCALIZABLE
            new GenericMappingComponents(DbINFFragment.metaClass));
    static {
        g_repositoryStringTable.put(INFIndexFragments.m_name, INFIndexFragments.m_struct);
    }

    /**
     * Tells whether a procedure has a DBA access. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry INFProcedureDba = new GenericMappingEntry("INFProcedureDba", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFProcedure.fDba));
    static {
        g_repositoryStringTable.put(INFProcedureDba.m_name, INFProcedureDba.m_struct);
    }

    /**
     * Gets the language of a procedure. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for SPL language.<br>
     * - "2" for JAVA language.<br>
     * - "3" for C language.<br>
     */
    // ProcedureLanguage already exists (Oracle)
    public static GenericMappingEntry INFProcedureLanguage = new GenericMappingEntry(
            "INFProcedureLanguage", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFProcedure.fLanguage));
    static {
        g_repositoryStringTable.put(INFProcedureLanguage.m_name, INFProcedureLanguage.m_struct);
    }

    /**
     * Gets the specific name of a procedure. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry INFProcedureSpecificname = new GenericMappingEntry(
            "INFProcedureSpecificname", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFProcedure.fSpecificName));
    static {
        g_repositoryStringTable.put(INFProcedureSpecificname.m_name,
                INFProcedureSpecificname.m_struct);
    }

    /**
     * Gets the external name of a procedure. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry INFProcedureExternalname = new GenericMappingEntry(
            "INFProcedureExternalname", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFProcedure.fExternalName));
    static {
        g_repositoryStringTable.put(INFProcedureExternalname.m_name,
                INFProcedureExternalname.m_struct);
    }

    /**
     * Gets the document of a procedure. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry INFProcedureDocument = new GenericMappingEntry(
            "INFProcedureDocument", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFProcedure.fDocument));
    static {
        g_repositoryStringTable.put(INFProcedureDocument.m_name, INFProcedureDocument.m_struct);
    }

    /**
     * Gets the listing filename of a procedure. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry INFProcedureListingfilename = new GenericMappingEntry(
            "INFProcedureListingfilename", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFProcedure.fListingFileName));
    static {
        g_repositoryStringTable.put(INFProcedureListingfilename.m_name,
                INFProcedureListingfilename.m_struct);
    }

    /**
     * Tells whether a procedure has references. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry INFProcedureReferences = new GenericMappingEntry(
            "INFProcedureReferences", //NOT LOCALIZABLE
            new GenericMappingProperty(DbORProcedure.fReturnTypeReference));
    static {
        g_repositoryStringTable.put(INFProcedureReferences.m_name, INFProcedureReferences.m_struct);
    }

    /**
     * Gets the return type's length of a procedure. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry INFProcedureReturntypelength = new GenericMappingEntry(
            "INFProcedureReturntypelength", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFProcedure.fReturnTypeLength));
    static {
        g_repositoryStringTable.put(INFProcedureReturntypelength.m_name,
                INFProcedureReturntypelength.m_struct);
    }

    /**
     * Gets the return type's number of decimals of a procedure. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: a string representing an integer.<br>
     */
    public static GenericMappingEntry INFProcedureReturntypenbdecimal = new GenericMappingEntry(
            "INFProcedureReturntypenbdecimal", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFProcedure.fReturnTypeNbDecimal));
    static {
        g_repositoryStringTable.put(INFProcedureReturntypenbdecimal.m_name,
                INFProcedureReturntypenbdecimal.m_struct);
    }

    /**
     * Gets the variant attribute of a procedure. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: <br>
     * - "1" for VARIANT.<br>
     * - "2" for NOT_VARIANT.<br>
     */
    public static GenericMappingEntry INFProcedureVariant = new GenericMappingEntry(
            "INFProcedureVariant", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFProcedure.fVariant));
    static {
        g_repositoryStringTable.put(INFProcedureVariant.m_name, INFProcedureVariant.m_struct);
    }

    /**
     * Tells whether a procedure parameter can be NULL. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry ParameterDefaultnull = new GenericMappingEntry(
            "ParameterDefaultnull", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFParameter.fDefaultNull));
    static {
        g_repositoryStringTable.put(ParameterDefaultnull.m_name, ParameterDefaultnull.m_struct);
    }

    /**
     * Tells whether the procedure parameter is a return parameter. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry ParameterIsreturn = new GenericMappingEntry(
            "ParameterIsreturn", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFParameter.fReturn));
    static {
        g_repositoryStringTable.put(ParameterIsreturn.m_name, ParameterIsreturn.m_struct);
    }

    /**
     * Gets the expression of a fragment. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: String<br>
     */
    public static GenericMappingEntry FragmentExpression = new GenericMappingEntry(
            "FragmentExpression", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFFragment.fExpression));
    static {
        g_repositoryStringTable.put(FragmentExpression.m_name, FragmentExpression.m_struct);
    }

    /**
     * Gets the dbspace associated to a fragment. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry FragmentDbspace = new GenericMappingEntry("FragmentDbspace", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbINFFragment.fDbspace));
    static {
        g_repositoryStringTable.put(FragmentDbspace.m_name, FragmentDbspace.m_struct);
    }

    /**
     * Tells whether a fragment has a remainder attribute. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry FragmentRemainder = new GenericMappingEntry(
            "FragmentRemainder", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFFragment.fRemainder));
    static {
        g_repositoryStringTable.put(FragmentRemainder.m_name, FragmentRemainder.m_struct);
    }

    /**
     * Lists all the fragments associated to a dbspace. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DbspaceFragments = new GenericMappingEntry(
            "DbspaceFragments", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbINFDbspace.fFragments));
    static {
        g_repositoryStringTable.put(DbspaceFragments.m_name, DbspaceFragments.m_struct);
    }

    /**
     * Lists all the tables associated to a dbspace. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DbspaceTables = new GenericMappingEntry("DbspaceTables", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbINFDbspace.fTables));
    static {
        g_repositoryStringTable.put(DbspaceTables.m_name, DbspaceTables.m_struct);
    }

    /**
     * Gets the database associated to a dbspace. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry DbspaceDatabase = new GenericMappingEntry("DbspaceDatabase", //NOT LOCALIZABLE
            new GenericMappingRelation1(DbINFDbspace.fDatabase));
    static {
        g_repositoryStringTable.put(DbspaceDatabase.m_name, DbspaceDatabase.m_struct);
    }

    /**
     * Lists all the lob columns of a sbspace. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Connector</b><br>
     */
    public static GenericMappingEntry SbspaceLobcolumns = new GenericMappingEntry(
            "SbspaceLobcolumns", //NOT LOCALIZABLE
            new GenericMappingRelationN(DbINFSbspace.fLobColumns));
    static {
        g_repositoryStringTable.put(SbspaceLobcolumns.m_name, SbspaceLobcolumns.m_struct);
    }

    /**
     * Tells whether a sbspace has a blob space. <br>
     * Target System: <b>Informix</b><br>
     * Type: <b>Attribute</b><br>
     * Returns: "true" or "false".<br>
     */
    public static GenericMappingEntry SbspaceBlobSpace = new GenericMappingEntry(
            "SbspaceBlobSpace", //NOT LOCALIZABLE
            new GenericMappingProperty(DbINFSbspace.fBlobSpace));
    static {
        g_repositoryStringTable.put(SbspaceBlobSpace.m_name, SbspaceBlobSpace.m_struct);
    }

    /**** END OF REPOSITORY FUNCTIONS *****/

    private static final Class SUPER_CLASS = org.modelsphere.jack.srtool.forward.Rule.class;

    //TODO: dynamic loading
    private static final void putRepositoryFunctions() {
        PluginMgr mgr = PluginMgr.getSingleInstance();
        ArrayList repositoryList = mgr.getPluginsRegistry().getActivePluginInstances(SUPER_CLASS);

        Iterator iter = repositoryList.iterator();
        while (iter.hasNext()) {
            Rule repositoryFunction = (Rule) iter.next();
            Class claz = repositoryFunction.getClass();
            String name = claz.getName(); //get class name
            int idx = name.lastIndexOf('.');
            if (idx != -1) {
                name = name.substring(1 + idx); //remove package from name
            }

            g_repositoryStringTable.put(name, new GenericMappingFunction(name));
            Debug.trace("REPOSITORY FUNCTION NAME = " + name);
        } //end while

    } //end putRepositoryFunctions()

    private static void putSynonyms() {
        /**
         * Repository Function <code>TableId</code> Description : Returns the ID of the current
         * table.
         */
        g_repositoryStringTable.put("TableId", // NOT LOCALIZABLE
                new GenericMappingFunction("ObjectId")); // NOT LOCALIZABLE
        /**
         * Repository Function <code>ColumnId</code> Description : Returns the ID of the current
         * column.
         */
        g_repositoryStringTable.put("ColumnId", // NOT LOCALIZABLE
                new GenericMappingFunction("ObjectId")); // NOT LOCALIZABLE

    }

    private static boolean initialized = false;

    private static void lazy_init() throws RuleException {
        try {
            if (!initialized) {
                //g_repositoryStringTable.clear();
                //putBasicFunctions();
                putRepositoryFunctions(); //  Add repository functions
                putSynonyms();
                initialized = true;
            }
        } catch (RuntimeException ex) {
            throw new RuleException(ex.getMessage());
        }
    }

    //package visibility: to be used inside the same package
    //@param repositoryString: a repository string, such as
    //"tableColumns"or "tablename".
    public static final MetaField getMetaField(String repositoryString) throws RuleException {

        lazy_init();

        //try to find in the hash table
        GenericMappingStructure mappingStruct = (GenericMappingStructure) g_repositoryStringTable
                .get(repositoryString);

        if (mappingStruct == null) {
            int idx = repositoryString.indexOf(':');
            if (idx > 0) {
                int len = repositoryString.length();
                String classname = repositoryString.substring(1, idx);
                String fieldname = repositoryString.substring(idx + 1, len - 1);
                try {
                    Class claz = Class.forName(classname);
                    Field field = claz.getField(fieldname);
                    MetaField mf = (MetaField) field.get(null);
                    mappingStruct = new GenericMappingProperty(mf);

                } catch (IllegalAccessException ex) {
                    //leave mappingStruct == null  
                    //} catch (InstantiationException ex) {
                    //leave mappingStruct == null
                } catch (ClassNotFoundException ex) {
                    //leave mappingStruct == null
                } catch (NoSuchFieldException ex) {
                    //leave mappingStruct == null
                } //end try
            } //end if
        } //end if

        if (mappingStruct == null) {
            throw new RuleException("Repository function does not exist: " + repositoryString);
        }

        MetaField field = mappingStruct.m_metaField;
        return field;
    } //end getMetaField()

    public static final MetaRelationship getMetaRelation(String repositoryString)
            throws RuleException {
        lazy_init();

        //try to find in the hash table
        GenericMappingStructure mappingStruct = (GenericMappingStructure) g_repositoryStringTable
                .get(repositoryString);

        if (mappingStruct == null) {
            int idx = repositoryString.indexOf(':');
            if (idx > 0) {
                int len = repositoryString.length();
                String classname = repositoryString.substring(1, idx);
                String fieldname = repositoryString.substring(idx + 1, len - 1);
                try {
                    Class claz = Class.forName(classname);
                    Field field = claz.getField(fieldname);
                    MetaRelationship mf = (MetaRelationship) field.get(null);

                    if (mf instanceof MetaRelation1) {
                        mappingStruct = new GenericMappingRelation1((MetaRelation1) mf);
                    } else if (mf instanceof MetaRelationN) {
                        mappingStruct = new GenericMappingRelationN((MetaRelationN) mf);
                    } //end if 

                } catch (IllegalAccessException ex) {
                    //leave mappingStruct == null  
                    //} catch (InstantiationException ex) {
                    //leave mappingStruct == null
                } catch (ClassNotFoundException ex) {
                    //leave mappingStruct == null
                } catch (NoSuchFieldException ex) {
                    //leave mappingStruct == null
                } //end try
            } //end if
        } //end if

        if (mappingStruct == null) {
            return null;
        }

        MetaRelationship rel = mappingStruct.m_rel;
        if ((rel == null) && (mappingStruct.m_metaField instanceof MetaRelationship)) {
            rel = (MetaRelationship) mappingStruct.m_metaField;
        }

        return rel;
    }

    public static final Class getClassOf(String repositoryString) {
        //try to find in the hash table
        GenericMappingStructure mappingStruct = (GenericMappingStructure) g_repositoryStringTable
                .get(repositoryString);

        if (mappingStruct == null) {
            return null;
        }

        Class claz = mappingStruct.m_class;
        return claz;
    }

    public static final MetaClass getMetaClassOf(String repositoryString) {
        //try to find in the hash table
        GenericMappingStructure mappingStruct = (GenericMappingStructure) g_repositoryStringTable
                .get(repositoryString);

        if (mappingStruct == null) {
            return null;
        }

        MetaClass metaclass = mappingStruct.m_metaClass;
        return metaclass;
    }

    public static final MetaClass getChildrenMetaClass(String repositoryString)
            throws RuleException {
        lazy_init();

        //try to find in the hash table
        GenericMappingStructure mappingStruct = (GenericMappingStructure) g_repositoryStringTable
                .get(repositoryString);

        if (mappingStruct == null) {
            return null;
        }

        MetaClass childrenMetaClass = mappingStruct.m_childrenMetaClass;
        return childrenMetaClass;
    }

    public static final String getRepositoryFunction(String repositoryString) throws RuleException {
        lazy_init();

        //try to find in the hash table
        GenericMappingStructure mappingStruct = (GenericMappingStructure) g_repositoryStringTable
                .get(repositoryString);

        if (mappingStruct == null) {
            String msg = MessageFormat.format(REPOSITORY_FUNCTION_NOT_FOUND,
                    new Object[] { repositoryString });
            throw new RuleException(msg);
        } //end if

        String repositoryFunction = mappingStruct.m_repositoryFunctionName;

        if (repositoryFunction == null) {
            String msg = MessageFormat.format(REPOSITORY_FUNCTION_NOT_FOUND,
                    new Object[] { repositoryString });
            throw new RuleException(msg);
        } //end if

        return repositoryFunction;
    }

    private abstract static class GenericMappingStructure {
        MetaField m_metaField = null;
        MetaClass m_childrenMetaClass = null;
        MetaRelationship m_rel = null;
        String m_repositoryFunctionName = null;
        Class m_class = null;
        MetaClass m_metaClass = null;

        protected GenericMappingStructure() {
        }

        protected GenericMappingStructure(MetaChoice choice, MetaClass childrenMetaClass) {
            m_rel = choice;
            m_childrenMetaClass = childrenMetaClass;
        }

        protected GenericMappingStructure(MetaRelationN relN, MetaClass childrenMetaClass) {
            m_rel = relN;
            m_childrenMetaClass = childrenMetaClass;
        }

        protected GenericMappingStructure(MetaRelation1 rel1, MetaClass childrenMetaClass) {
            m_rel = rel1;
            m_childrenMetaClass = childrenMetaClass;
        }

        protected GenericMappingStructure(MetaRelation1 rel1, MetaField metaField) {
            m_rel = rel1;
            m_metaField = metaField;
        }

        protected GenericMappingStructure(MetaField metaField) {
            if (metaField instanceof MetaChoice)
                throw new IllegalArgumentException(LocaleMgr.misc
                        .getString("childMetaClassMustBeSpecified")
                        + " " + metaField.getGUIName());

            m_metaField = metaField;
        }

        protected GenericMappingStructure(Class claz, MetaClass metaClass) {
            m_class = claz;
            m_metaClass = metaClass;
        }

        protected GenericMappingStructure(String repositoryFunctionName) {
            m_repositoryFunctionName = repositoryFunctionName;
        }
    } //end of GenericMappingStructure

    private final static class GenericMappingFunction extends GenericMappingStructure {
        GenericMappingFunction(String repositoryFunctionName) {
            super(repositoryFunctionName);
        }
    }

    private final static class GenericMappingProperty extends GenericMappingStructure {
        GenericMappingProperty(MetaField metaField) {
            super(metaField);

            if (metaField instanceof MetaRelationship) {
                throw new IllegalArgumentException(LocaleMgr.misc
                        .getString("childMetaClassMustBeSpecified")
                        + " " + metaField.getGUIName());
            }
        }
    } // end GenericMappingProperty

    private final static class GenericMappingComposite extends GenericMappingStructure {
        GenericMappingComposite(MetaClass metaClass) {
            super(DbSemanticalObject.fComposite, metaClass);
        }
    } // end GenericMappingComposite

    private final static class GenericMappingComponents extends GenericMappingStructure {
        GenericMappingComponents(MetaClass metaClass) {
            super(DbSemanticalObject.fComponents, metaClass);
        }
    } // end GenericMappingComponent

    private final static class GenericMappingRelation1 extends GenericMappingStructure {
        GenericMappingRelation1(MetaRelation1 relation) {
            m_rel = relation;
        }

        GenericMappingRelation1(MetaRelation1 relation, MetaClass childrenMetaClass) {
            m_rel = relation;
            m_childrenMetaClass = childrenMetaClass;
        }

        GenericMappingRelation1(MetaRelation1 relation, MetaField metaField) {
            m_rel = relation;
            m_metaField = metaField;
        }
    } // end GenericMappingRelation

    private final static class GenericMappingChoice extends GenericMappingStructure {
        GenericMappingChoice(MetaChoice metaChoice, MetaClass metaClass) {
            super(metaChoice, metaClass);
        }
    } // end GenericMappingChoice

    private final static class GenericMappingRelationN extends GenericMappingStructure {
        GenericMappingRelationN(MetaRelationN relation, MetaClass childrenMetaClass) {
            super(relation, childrenMetaClass);
        }

        GenericMappingRelationN(MetaRelationN relation) {
            super(relation);
        }
    }

    private final static class GenericMappingConcept extends GenericMappingStructure {
        GenericMappingConcept(Class claz, MetaClass metaClass) {
            super(claz, metaClass);
        }
    } // end GenericMappingConcept

    private static class RepositoryFunctionTable extends Hashtable {
        RepositoryFunctionTable() {
        }

        public synchronized Object put(Object key, Object value) {
            Object obj;

            if (containsKey(key)) {
                String msg = MessageFormat.format(DUPLICATE_NOT_ALLOWED_PATTRN,
                        new Object[] { key });
                throw new RuntimeException(msg);
            }

            obj = super.put(key, value);
            return obj;
        } //end put()

    } //end of RepositoryFunctionTable

    private static class GenericMappingEntry {
        private String m_name;
        private GenericMappingStructure m_struct;

        public GenericMappingEntry(String name, GenericMappingStructure struct) {
            m_name = name;
            m_struct = struct;
        }
    }

    //A dummy inner class only used to mislead javadoc
    private static class Section {
    }

    //static Hashtable g_repositoryStringTable = new Hashtable();

    /*
     * //For unit testing public static void main(String[] args) { GenericMapping mapping = new
     * GenericMapping(); } //end main()
     */
}
