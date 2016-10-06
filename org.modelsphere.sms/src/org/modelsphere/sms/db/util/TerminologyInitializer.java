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
package org.modelsphere.sms.db.util;

import javax.swing.Icon;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.SMSExplorer;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEActorGo;
import org.modelsphere.sms.be.db.DbBEContextCell;
import org.modelsphere.sms.be.db.DbBEContextGo;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEStoreGo;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.DbBEUseCaseGo;
import org.modelsphere.sms.be.db.DbBEUseCaseResource;
import org.modelsphere.sms.db.util.or.ErTerminology;
import org.modelsphere.sms.db.util.or.OrTerminology;
import org.modelsphere.sms.db.util.or.RdmTerminology;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORFKeyColumn;
import org.modelsphere.sms.or.db.DbORNotation;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.generic.db.DbGEColumn;
import org.modelsphere.sms.or.generic.db.DbGEPrimaryUnique;
import org.modelsphere.sms.or.generic.db.DbGETable;
import org.modelsphere.sms.or.ibm.db.DbIBMColumn;
import org.modelsphere.sms.or.ibm.db.DbIBMPrimaryUnique;
import org.modelsphere.sms.or.ibm.db.DbIBMTable;
import org.modelsphere.sms.or.informix.db.DbINFColumn;
import org.modelsphere.sms.or.informix.db.DbINFPrimaryUnique;
import org.modelsphere.sms.or.informix.db.DbINFTable;
import org.modelsphere.sms.or.oracle.db.DbORAColumn;
import org.modelsphere.sms.or.oracle.db.DbORAPrimaryUnique;
import org.modelsphere.sms.or.oracle.db.DbORATable;

/**
 * @author Grandite
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences -
 *         Java - Code Style - Code Templates
 */
public class TerminologyInitializer {

    // private constants
    public static final String ACTION_FLOW = LocaleMgr.term.getString("ActionFlow");
    public static final String ACTION_STATE = LocaleMgr.term.getString("ActionState");
    public static final String ACTIVATION = LocaleMgr.term.getString("Activation");
    public static final String ACTIVITY = LocaleMgr.term.getString("Activity");
    public static final String ACTOR = LocaleMgr.term.getString("ActorActor");
    public static final String ASSOCIATION = LocaleMgr.term.getString("Association");
    public static final String ASSOCIATIONS = LocaleMgr.term.getString("Associations");
    public static final String ASSOCIATION_ROLE = LocaleMgr.term.getString("AssociationRole");
    public static final String COLLABORATION = LocaleMgr.term.getString("Collaboration");
    public static final String COLLABORATIONS = LocaleMgr.term.getString("Collaborations");
    public static final String COMPONENT = LocaleMgr.term.getString("Component");
    public static final String DEPENDENCY = LocaleMgr.term.getString("Dependency");
    public static final String DEPENDENCIES = LocaleMgr.term.getString("Dependencies");
    public static final String EVENT = LocaleMgr.term.getString("Event");
    public static final String EXTERNAL_ACTOR = LocaleMgr.term.getString("ExternalActor");
    public static final String EXTERNAL_AGENT = LocaleMgr.term.getString("ExternalAgent");
    public static final String EXTERNAL_ENTITY = LocaleMgr.term.getString("externalentity");
    public static final String EXTERNAL_ENTITIES = LocaleMgr.term.getString("externalentities");
    public static final String INTERNAL_ACTOR = LocaleMgr.term.getString("InternalActor");
    public static final String MESSAGE = LocaleMgr.term.getString("Message");
    public static final String MODULE = LocaleMgr.term.getString("Module");
    public static final String NODE = LocaleMgr.term.getString("Node");
    public static final String OBJECT = LocaleMgr.term.getString("Object");
    public static final String OPERATION = LocaleMgr.term.getString("Operation");
    public static final String PROCESS_UNIT = LocaleMgr.term.getString("ProcessUnit");
    public static final String PROCESSOR = LocaleMgr.term.getString("Processor");
    public static final String RELATIONSHIP = LocaleMgr.term.getString("Relationship");
    public static final String ROLE = LocaleMgr.term.getString("Role");
    public static final String STATE = LocaleMgr.term.getString("State");
    public static final String STORAGE = LocaleMgr.term.getString("Storage");
    public static final String STORE = LocaleMgr.term.getString("store");
    public static final String SWINLANE = LocaleMgr.term.getString("Swimlane");
    public static final String SWINLANES = LocaleMgr.term.getString("Swimlanes");
    public static final String TASK = LocaleMgr.term.getString("Task");
    public static final String TRANSITION = LocaleMgr.term.getString("Transition");
    public static final String USE_CASE = LocaleMgr.term.getString("UseCaseUseCase");
    public static final String WAREHOUSE = LocaleMgr.term.getString("Warehouse");
    public static final String BUSINESS_PROCESS_MODEL = LocaleMgr.term
            .getString("BusinessProcessModel");
    public static final String BUSINESS_PROCESS_DIAGRAM = LocaleMgr.term
            .getString("BusinessProcessDiagram");
    public static final String SYSTEM_PROCESS_MODEL = LocaleMgr.term
            .getString("SystemProcessModel");
    public static final String SYSTEM_PROCESS_DIAGARM = LocaleMgr.term
            .getString("SystemProcessDiagram");
    public static final String INFORMATION_SYSTEM_ARCHITECTURE = LocaleMgr.term
            .getString("InformationProcessArchitecture");
    public static final String UML_MODEL = LocaleMgr.term.getString("uml_model");
    public static final String FUNCTIONAL_MODEL = LocaleMgr.term.getString("functional_model");

    public static final String FLOW = LocaleMgr.term.getString("flow");
    public static final String CONTEXT = LocaleMgr.term.getString("context");
    public static final String SYSTEM = LocaleMgr.term.getString("System");

    public static final String ATTRIBUTE_TXT = LocaleMgr.term.getString("Attribute");
    public static final String ATTRIBUTE_GROUP_TXT = LocaleMgr.term.getString("Attributes");
    public static final String ENTITY_TXT = LocaleMgr.term.getString("Entity");
    public static final String ENTITY_GROUP_TXT = LocaleMgr.term.getString("Entities");
    public static final String RELATIONSHIP_TXT = LocaleMgr.term.getString("Relationship");
    public static final String RELATIONSHIP_GROUP_TXT = LocaleMgr.term.getString("Relationships");
    public static final String ARCS_TXT = LocaleMgr.term.getString("Arc");
    public static final String ARCS_GROUP_TXT = LocaleMgr.term.getString("Arcs");
    public static final String ER_MODEL_TXT = LocaleMgr.term.getString("ConceptualDataModel");
    public static final String VIEW_TXT = LocaleMgr.term.getString("View");
    public static final String VIEW_GROUP_TXT = LocaleMgr.term.getString("Views");
    public static final String OR_MODEL_TXT = LocaleMgr.term.getString("ORModel");
    public static final String SUPERTABLE = LocaleMgr.term.getString("Supertable");
    public static final String SUPERATTRIBUTE = LocaleMgr.term.getString("Superattribute");
    public static final String SUPERENTITY = LocaleMgr.term.getString("Superentity");
    public static final String SUPERARC = LocaleMgr.term.getString("Superarc");
    public static final String SUPERASSOCIATION = LocaleMgr.term.getString("Superassociation");
    public static final String NULL = LocaleMgr.term.getString("NULL");
    public static final String PRIMARY_KEY = LocaleMgr.term.getString("PrimaryKey");
    public static final String PRIMARY_UNIQUE_KEYS = LocaleMgr.term.getString("PrimaryUniqueKeys");

    public static final String SUBCOLUMN = LocaleMgr.term.getString("Subcolumn");
    public static final String SUBINDEX = LocaleMgr.term.getString("Subindex");
    public static final String SUBTRIGGER = LocaleMgr.term.getString("Subtrigger");
    public static final String SUBCHECK = LocaleMgr.term.getString("Subcheck");
    public static final String FOREIGN_KEY = LocaleMgr.term.getString("ForeignKey");

    public static final String SUPERCOLUMN = LocaleMgr.term.getString("Supercolumn");
    public static final String SUPERVIEW = LocaleMgr.term.getString("Superview");
    public static final String SUPERKEY = LocaleMgr.term.getString("Superkey");
    public static final String SUPERCHECK = LocaleMgr.term.getString("Supercheck");
    public static final String SUPERINDEX = LocaleMgr.term.getString("Superindex");
    public static final String SUPERTRIGGER = LocaleMgr.term.getString("Supertrigger");
    public static final String SUPER_CHOICE_SPEC = LocaleMgr.term.getString("SuperChoiceSpec");

    public static final String STORES = LocaleMgr.term.getString("Stores");
    public static final String OBJECTS = LocaleMgr.term.getString("Objects");
    public static final String FLOWS = FLOW + 's';
    public static final String CONTEXTS = LocaleMgr.term.getString("Contexts");
    public static final String BUSINESS_PROCESS_MODELS = LocaleMgr.term
            .getString("BUSINESS_PROCESS_MODELS");
    public static final String BUSINESS_PROCESS_DIAGRAMS = LocaleMgr.term
            .getString("BUSINESS_PROCESS_DIAGRAMS");
    public static final String WAREHOUSES = LocaleMgr.term.getString("Warehouses");
    public static final String EXTERNAL_AGENTS = LocaleMgr.term.getString("ExternalAgents");
    public static final String SYSTEM_PROCESS_MODELS = LocaleMgr.term
            .getString("SYSTEM_PROCESS_MODELS");
    public static final String SYSTEM_PROCESS_DIAGARMS = LocaleMgr.term
            .getString("SYSTEM_PROCESS_DIAGARMS");
    public static final String INFORMATION_SYSTEM_ARCHITECTURES = LocaleMgr.term
            .getString("INFORMATION_SYSTEM_ARCHITECTURES");
    public static final String MODULES = LocaleMgr.term.getString("Modules");
    public static final String EVENTS = LocaleMgr.term.getString("Events");
    public static final String OPERATIONS = LocaleMgr.term.getString("Operations");
    public static final String STATES = LocaleMgr.term.getString("States");
    public static final String INTERNAL_ACTORS = LocaleMgr.term.getString("InternalActors");
    public static final String EXTERNAL_ACTORS = LocaleMgr.term.getString("ExternalActors");
    public static final String PROCESS_UNITS = LocaleMgr.term.getString("ProcessUnits");
    public static final String ACTORS = LocaleMgr.term.getString("Actors");
    public static final String USE_CASES = LocaleMgr.term.getString("UseCases");
    public static final String UML_SEQUENCE_DIAGRAMS = LocaleMgr.term
            .getString("UML_SEQUENCE_DIAGRAMS");
    public static final String ROLES = LocaleMgr.term.getString("Roles");
    public static final String ACTIVATIONS = LocaleMgr.term.getString("Activations");
    public static final String TRANSITIONS = LocaleMgr.term.getString("Transitions");
    public static final String UML_STATE_DIAGRAMS = LocaleMgr.term.getString("UML_STATE_DIAGRAMS");
    public static final String SYSTEMS = LocaleMgr.term.getString("Systems");
    public static final String ASSOCIATION_ROLES = LocaleMgr.term.getString("AssociationRoles");
    public static final String UML_COLLABORATION_DIAGRAMS = LocaleMgr.term
            .getString("UML_COLLABORATION_DIAGRAMS");
    public static final String UML_MODELS = LocaleMgr.term.getString("umlModels");
    public static final String ACTION_FLOWS = LocaleMgr.term.getString("ActionFlows");
    public static final String ACTION_STATES = LocaleMgr.term.getString("ActionStates");
    public static final String UML_ACTIVITY_DIAGRAM_TXTS = LocaleMgr.term
            .getString("UML_ACTIVITY_DIAGRAM_TXTS");
    public static final String COMPONENTS = LocaleMgr.term.getString("Components");
    public static final String UML_COMPONENT_DIAGRAM_TXTS = LocaleMgr.term
            .getString("UML_COMPONENT_DIAGRAM_TXTS");
    public static final String NODES = LocaleMgr.term.getString("Nodes");
    public static final String UML_DEPLOYMENT_DIAGRAM_TXTS = LocaleMgr.term
            .getString("UML_DEPLOYMENT_DIAGRAM_TXTS");
    public static final String MESSAGES = LocaleMgr.term.getString("Messages");
    public static final String PROCESSORS = LocaleMgr.term.getString("Processors");
    public static final String FUNCTIONAL_MODELS = LocaleMgr.term.getString("FUNCTIONAL_MODELS");
    public static final String TASKS = LocaleMgr.term.getString("Tasks");
    public static final String STORAGES = LocaleMgr.term.getString("Storages");
    public static final String FOREIGN_KEYS = LocaleMgr.term.getString("ForeignKeys");
    public static final String RELATIONSHIPS = LocaleMgr.term.getString("Relationships");
    public static final String SUBINDEXES = LocaleMgr.term.getString("SubIndexes");
    public static final String SUBTRIGGERS = LocaleMgr.term.getString("SubTriggers");
    public static final String SUBCOLUMNS = LocaleMgr.term.getString("SubColumns");
    public static final String SUBCHECKS = LocaleMgr.term.getString("SubChecks");
    public static final String ACTIVITIES = LocaleMgr.term.getString("Activities");

    public static final String ATTRIBUTE = LocaleMgr.term.getString("Attribute");
    public static final String ATTRIBUTES = LocaleMgr.term.getString("Attributes");
    public static final String ENTITY = LocaleMgr.term.getString("Entity");
    public static final String ENTITIES = LocaleMgr.term.getString("Entities");
    public static final String CARDINALITY = LocaleMgr.term.getString("Cardinality");
    public static final String SUPERENTITIES = LocaleMgr.term.getString("Superentities");
    public static final String COLUMNS = LocaleMgr.term.getString("Columns");
    public static final String SUBENTITY = LocaleMgr.term.getString("Subentity");
    public static final String SUBENTITIES = LocaleMgr.term.getString("Subentities");
    public static final String CARDINALITIES = LocaleMgr.term.getString("Cardinalities");

    public static final Icon dbbeprocess = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbeprocess.gif");
    public static final Icon dbbeusecase = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbeusecase.gif");
    public static final Icon dbbeentity = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbeentity.gif");
    public static final Icon dbbeactor = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbeactor.gif");
    public static final Icon dbbesystem = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbesystem.gif");
    public static final Icon dbbeobject = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbeobject.gif");
    public static final Icon dbbeumlmodel = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbeumlmodel.gif");
    public static final Icon dbbeactorgroup = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbeactorgroup.gif");
    public static final Icon dbbeobjectgroup = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbeobjectgroup.gif");
    public static final Icon dbbeentitygroup = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbeentity.gif");
    public static final Icon dbbestoregroup = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbestore.gif");
    public static final Icon dbbeusecasediagram = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbeusecasediagram.gif");
    public static final Icon dbbeactivation = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbeactivation.gif");
    public static final Icon dbbecollaboration = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbecollaboration.gif");
    public static final Icon dbbecomponent = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbecomponent.gif");
    public static final Icon dbbeaction = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbeaction.gif");
    public static final Icon dbbedeployment = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbedeployment.gif");
    public static final Icon dbberole = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbberole.gif");
    public static final Icon dbbestate = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbestate.gif");

    public static final Icon dbbecomponentdiagram = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbecomponentdiagram.gif");
    public static final Icon dbbecollaborationdiagram = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbecollaborationdiagram.gif");
    public static final Icon dbbeactivitydiagram = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbeactivitydiagram.gif");
    public static final Icon dbbedeploymentdiagram = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbedeploymentdiagram.gif");
    public static final Icon dbberolegroup = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbberolegroup.gif");
    public static final Icon dbbestatediagram = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbestatediagram.gif");
    public static final Icon dbbesequencediagram = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbesequencediagram.gif");
    public static final Icon dbbecontext = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbecontext.gif");

    public static final Icon dbbeusecaseexternal = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbeusecaseexternal.gif");
    public static final Icon dbbecollaborationexternal = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbecollaborationexternal.gif");
    public static final Icon dbbeactivityexternal = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbeactivityexternal.gif");
    public static final Icon dbbecomponentexternal = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbecomponentexternal.gif");
    public static final Icon dbbenodeexternal = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbenodeexternal.gif");
    public static final Icon dbbestateexternal = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbestateexternal.gif");
    public static final Icon dbbeactivationexternal = GraphicUtil.loadIcon(DbBEUseCase.class,
            "resources/dbbeactivationexternal.gif");

    public static final Icon dbbeprocessexternal = GraphicUtil.loadIcon(SMSExplorer.class,
            "be/international/resources/externalusecase.gif");
    
    //
    //
    //
    
    //the entry point
    public static void initTerminology(Terminology terminology, DbObject notation) throws DbException {
    	
    	notation.getDb().beginReadTrans();
    	String notationName = notation.getName();
    	
    	if (notation instanceof DbORNotation) {
        	DbORNotation orNotation = (DbORNotation)notation; 
        	TerminologyInitializer defaultTerminology = getOrTerminology(orNotation);
        	defaultTerminology.defineTerminology(terminology);
        	defaultTerminology.defineTerminologyByName(terminology, notationName); 
        } else if (notation instanceof DbBENotation) {
        	TerminologyInitializer defaultTerminology = new TerminologyInitializer();
        	defaultTerminology.defineTerminology(terminology);
        	defaultTerminology.defineTerminologyByName(terminology, notationName); 
        }
    	
    	notation.getDb().commitTrans();
    } //end initTerminology()

    public static void initTerminology(Terminology terminology, String notationName) throws DbException {	
    	TerminologyInitializer defaultTerminology = new TerminologyInitializer();
    	defaultTerminology.defineTerminologyByName(terminology, notationName);
    } //end initTerminology()
    
	protected void defineTerminology(Terminology terminology) {
	}

	//
	// private methods
	//
    
    private static TerminologyInitializer getOrTerminology(DbORNotation notation) throws DbException {
    	TerminologyInitializer terminology;
    	int familyIdentifier = notation.getNotationMode().intValue();
    	
    	switch (familyIdentifier) {
    	case 0: 
    	case DbORNotation.OR_MODE: 
    	    terminology = ScreenPerspective.isOMSTerminology() ? new OrTerminology() : new RdmTerminology();
    	    break;
    	case DbORNotation.ER_MODE: 
    	    terminology = new ErTerminology();
    	    break;
    	default:
    	    terminology = null;	
    	}
    	
    	return terminology;
    } //end getOrTerminology()


    private void defineTerminologyByName(Terminology terminology, String name)
            throws DbException {

        /*
         * TERM AND ICONS APPLACABLE TO ALL TERMINOLOGIES
         */

        // TERMS
        terminology.define(DbBEActor.metaClass, EXTERNAL_ENTITY, EXTERNAL_ENTITIES);
        terminology.define(DbBEStore.metaClass, STORE, STORES);
        terminology.define(DbBEFlow.metaClass, FLOW, FLOWS);
        terminology.define(DbBEUseCaseGo.metaClass, CONTEXT, CONTEXTS);
        terminology.define(DbBEModel.metaClass, BUSINESS_PROCESS_MODEL, BUSINESS_PROCESS_MODEL);

        // ICONS

        terminology.define(DbBEUseCase.metaClass, dbbeprocess);
        terminology.define(DbBEActor.metaClass, dbbeentity);
        terminology.define(DbBEActorGo.metaClass, dbbeentitygroup);
        terminology.define(DbBEStoreGo.metaClass, dbbestoregroup);
        terminology.define(DbBEUseCaseGo.metaClass, dbbecontext);
        terminology.define(DbBEUseCaseResource.metaClass, dbbeprocessexternal);

        /************************************************************************
         * DATARUN_BPM
         * 
         * */

        if (name.equals(DbBENotation.DATARUN_BPM)) {
            terminology
                    .define(DbBEModel.metaClass, BUSINESS_PROCESS_MODEL, BUSINESS_PROCESS_MODELS);
            terminology.define(DbBEDiagram.metaClass, BUSINESS_PROCESS_DIAGRAM,
                    BUSINESS_PROCESS_DIAGRAMS);
            terminology.define(DbBEStore.metaClass, WAREHOUSE, WAREHOUSES);
            terminology.define(DbBEActor.metaClass, EXTERNAL_AGENT, EXTERNAL_AGENTS);
        }

        /************************************************************************
         * DATARUN_SPM
         * 
         * */

        else if (name.equals(DbBENotation.DATARUN_SPM)) {
            terminology.define(DbBEModel.metaClass, SYSTEM_PROCESS_MODEL, SYSTEM_PROCESS_MODELS);
            terminology.define(DbBEDiagram.metaClass, SYSTEM_PROCESS_DIAGARM,
                    SYSTEM_PROCESS_DIAGARMS);
            terminology.define(DbBEStore.metaClass, WAREHOUSE, WAREHOUSES);
            terminology.define(DbBEActor.metaClass, EXTERNAL_AGENT, EXTERNAL_AGENTS);
        }

        /************************************************************************
         * DATARUN_ISA
         * 
         * */

        else if (name.equals(DbBENotation.DATARUN_ISA)) {
            terminology.define(DbBEModel.metaClass, INFORMATION_SYSTEM_ARCHITECTURE,
                    INFORMATION_SYSTEM_ARCHITECTURES);
            terminology.define(DbBEDiagram.metaClass, INFORMATION_SYSTEM_ARCHITECTURE,
                    INFORMATION_SYSTEM_ARCHITECTURES);
            terminology.define(DbBEUseCase.metaClass, MODULE, MODULES);
            terminology.define(DbBEStore.metaClass, WAREHOUSE, WAREHOUSES);
            terminology.define(DbBEActor.metaClass, EXTERNAL_AGENT, EXTERNAL_AGENTS);
        }

        /************************************************************************
         * MERISE_MCT
         * 
         * */

        else if (name.equals(DbBENotation.MERISE_MCT)) {
            terminology.define(DbBEFlow.metaClass, EVENT, EVENTS);
            terminology.define(DbBEUseCase.metaClass, OPERATION, OPERATIONS);
            terminology.define(DbBEStore.metaClass, STATE, STATES);
            terminology.define(DbBEActor.metaClass, ACTOR, ACTORS);
        }

        /************************************************************************
         * MERISE_FLOW_SCHEMA
         * 
         * */

        else if (name.equals(DbBENotation.MERISE_FLOW_SCHEMA)) {
            terminology.define(DbBEFlow.metaClass, EVENT, EVENTS);
            terminology.define(DbBEUseCase.metaClass, INTERNAL_ACTOR, INTERNAL_ACTORS);
            terminology.define(DbBEStore.metaClass, STORE, STORES);
            terminology.define(DbBEActor.metaClass, EXTERNAL_ACTOR, EXTERNAL_ACTORS);
        }

        /************************************************************************
         * MERISE_OOM
         * 
         * */

        else if (name.equals(DbBENotation.MERISE_OOM)) {
            terminology.define(DbBEUseCase.metaClass, OPERATION, OPERATIONS);
            terminology.define(DbBEStore.metaClass, OBJECT, OBJECTS);
            terminology.define(DbBEActor.metaClass, EVENT, EVENTS);
        }

        /************************************************************************
         * P_PLUS
         * 
         * */

        else if (name.equals(DbBENotation.P_PLUS)) {
            terminology.define(DbBEUseCase.metaClass, PROCESS_UNIT, PROCESS_UNITS);
            terminology.define(DbBEActor.metaClass, ACTOR, ACTORS);
        }

        /************************************************************************
         * P_PLUS_OPAL
         * 
         * */

        else if (name.equals(DbBENotation.P_PLUS_OPAL)) {
            terminology.define(DbBEActor.metaClass, ACTOR, ACTORS);
        }

        /************************************************************************
         * UML_USE_CASE
         * 
         * */

        else if (name.equals(DbBENotation.UML_USE_CASE)) {
            terminology.define(DbBEModel.metaClass, UML_MODEL, UML_MODELS);
            terminology.define(DbBEUseCaseGo.metaClass, SYSTEM, SYSTEMS);
            terminology.define(DbBEFlow.metaClass, ASSOCIATION, ASSOCIATIONS);
            terminology.define(DbBEUseCase.metaClass, USE_CASE, USE_CASES);
            terminology.define(DbBEDiagram.metaClass, DbBENotation.UML_USE_CASE/*
                                                                                * , UML_USE_CASES
                                                                                */);
            terminology.define(DbBEActor.metaClass, ACTOR, ACTORS);
            terminology.define(DbBEStore.metaClass, OBJECT, OBJECTS);

            // icons
            terminology.define(DbBEUseCase.metaClass, dbbeusecase);
            terminology.define(DbBEActor.metaClass, dbbeactor);
            terminology.define(DbBEUseCaseGo.metaClass, dbbesystem);
            terminology.define(DbBEUseCaseResource.metaClass, dbbeusecaseexternal);

            terminology.define(DbBEStore.metaClass, dbbeobject);
            terminology.define(DbBEModel.metaClass, dbbeumlmodel);
            terminology.define(DbBEActorGo.metaClass, dbbeactorgroup);
            terminology.define(DbBEStoreGo.metaClass, dbbeobjectgroup);
            terminology.define(DbBEDiagram.metaClass, dbbeusecasediagram);

        }

        /************************************************************************
         * UML_SEQUENCE_DIAGRAM
         * 
         * */

        else if (name.equals(DbBENotation.UML_SEQUENCE_DIAGRAM)) {
            terminology.define(DbBEUseCaseGo.metaClass, SYSTEM, SYSTEMS);
            terminology.define(DbBEModel.metaClass, UML_MODEL, UML_MODELS);
            terminology.define(DbBEFlow.metaClass, MESSAGE, MESSAGES);
            terminology.define(DbBEUseCase.metaClass, ACTIVATION, ACTIVATIONS);
            terminology.define(DbBEStore.metaClass, ROLE, ROLES);
            terminology.define(DbBEActor.metaClass, ACTOR, ACTORS);
            terminology.define(DbBEContextGo.metaClass, ROLES);
            terminology.define(DbBEContextCell.metaClass, ROLE);
            terminology.define(DbBEDiagram.metaClass, DbBENotation.UML_SEQUENCE_DIAGRAM,
                    UML_SEQUENCE_DIAGRAMS);

            terminology.define(DbBEUseCase.metaClass, dbbeactivation);
            terminology.define(DbBEUseCaseGo.metaClass, dbbesystem); // //change
            // this
            // icon
            // for
            // each
            // uml
            // notation
            terminology.define(DbBEActor.metaClass, dbbeactor);
            terminology.define(DbBEStore.metaClass, dbberole);
            terminology.define(DbBEModel.metaClass, dbbeumlmodel);
            terminology.define(DbBEActorGo.metaClass, dbbeactorgroup);
            terminology.define(DbBEStoreGo.metaClass, dbberolegroup);
            terminology.define(DbBEDiagram.metaClass, dbbesequencediagram);
            terminology.define(DbBEUseCaseResource.metaClass, dbbeactivationexternal);

        }

        /************************************************************************
         * UML_STATE_DIAGRAM
         * 
         * */

        else if (name.equals(DbBENotation.UML_STATE_DIAGRAM)) {

            terminology.define(DbBEUseCase.metaClass, dbbestate);
            terminology.define(DbBEUseCaseGo.metaClass, SYSTEM, SYSTEMS);
            terminology.define(DbBEModel.metaClass, UML_MODEL, UML_MODELS);
            terminology.define(DbBEFlow.metaClass, TRANSITION, TRANSITIONS);
            terminology.define(DbBEUseCase.metaClass, STATE, STATES);
            terminology.define(DbBEDiagram.metaClass, DbBENotation.UML_STATE_DIAGRAM,
                    UML_STATE_DIAGRAMS);
            terminology.define(DbBEActor.metaClass, ACTOR, ACTORS);
            terminology.define(DbBEStore.metaClass, OBJECT, OBJECTS);

            terminology.define(DbBEUseCaseGo.metaClass, dbbesystem);
            terminology.define(DbBEActor.metaClass, dbbeactor);
            terminology.define(DbBEStore.metaClass, dbbeobject);
            terminology.define(DbBEModel.metaClass, dbbeumlmodel);
            terminology.define(DbBEActorGo.metaClass, dbbeactorgroup);
            terminology.define(DbBEStoreGo.metaClass, dbbeobjectgroup);
            terminology.define(DbBEDiagram.metaClass, dbbestatediagram);
            terminology.define(DbBEUseCaseResource.metaClass, dbbestateexternal);
        }

        /************************************************************************
         * UML_COLLABORATION_DIAGRAM
         * 
         * */

        else if (name.equals(DbBENotation.UML_COLLABORATION_DIAGRAM)) {
            terminology.define(DbBEUseCaseGo.metaClass, SYSTEM, SYSTEMS);
            terminology.define(DbBEModel.metaClass, UML_MODEL, UML_MODELS);
            terminology.define(DbBEFlow.metaClass, MESSAGE, MESSAGES);
            terminology.define(DbBEUseCase.metaClass, COLLABORATION, COLLABORATION + 's');
            terminology.define(DbBEDiagram.metaClass, DbBENotation.UML_COLLABORATION_DIAGRAM,
                    UML_COLLABORATION_DIAGRAMS);
            terminology.define(DbBEActor.metaClass, ACTOR, ACTORS);
            terminology.define(DbBEStore.metaClass, OBJECT, OBJECTS);

            terminology.define(DbBEUseCase.metaClass, dbbecollaboration);
            terminology.define(DbBEUseCaseGo.metaClass, dbbesystem);
            terminology.define(DbBEActor.metaClass, dbbeactor);
            terminology.define(DbBEStore.metaClass, dbbeobject);
            terminology.define(DbBEModel.metaClass, dbbeumlmodel);
            terminology.define(DbBEActorGo.metaClass, dbbeactorgroup);
            terminology.define(DbBEStoreGo.metaClass, dbbeobjectgroup);
            terminology.define(DbBEDiagram.metaClass, dbbecollaborationdiagram);
            terminology.define(DbBEUseCaseResource.metaClass, dbbecollaborationexternal);

            /************************************************************************
             * UML_ACTIVITY_DIAGRAM_TXT
             * 
             * */

        } else if (name.equals(DbInitialization.UML_ACTIVITY_DIAGRAM_TXT)) {
            terminology.define(DbBEUseCase.metaClass, dbbeaction);
            terminology.define(DbBEUseCaseGo.metaClass, SYSTEM, SYSTEMS);
            terminology.define(DbBEModel.metaClass, UML_MODEL, UML_MODELS);
            terminology.define(DbBEFlow.metaClass, TRANSITION, TRANSITIONS);
            terminology.define(DbBEUseCase.metaClass, ACTION_STATE, ACTION_STATES);
            terminology.define(DbBEStore.metaClass, OBJECT, OBJECTS);
            terminology.define(DbBEActor.metaClass, ACTOR, ACTORS);
            terminology.define(DbBEContextGo.metaClass, SWINLANES);
            terminology.define(DbBEContextCell.metaClass, SWINLANE);
            terminology.define(DbBEDiagram.metaClass, DbInitialization.UML_ACTIVITY_DIAGRAM_TXT,
                    UML_ACTIVITY_DIAGRAM_TXTS);

            terminology.define(DbBEUseCaseGo.metaClass, dbbesystem);
            terminology.define(DbBEActor.metaClass, dbbeactor);
            terminology.define(DbBEStore.metaClass, dbbeobject);
            terminology.define(DbBEModel.metaClass, dbbeumlmodel);
            terminology.define(DbBEActorGo.metaClass, dbbeactorgroup);
            terminology.define(DbBEStoreGo.metaClass, dbbeobjectgroup);
            terminology.define(DbBEDiagram.metaClass, dbbeactivitydiagram);
            terminology.define(DbBEUseCaseResource.metaClass, dbbeactivityexternal);

        }

        /************************************************************************
         * UML_COMPONENT_DIAGRAM_TXT
         * 
         * */

        else if (name.equals(DbInitialization.UML_COMPONENT_DIAGRAM_TXT)) {
            terminology.define(DbBEUseCaseGo.metaClass, SYSTEM, SYSTEMS);
            terminology.define(DbBEModel.metaClass, UML_MODEL, UML_MODELS);
            terminology.define(DbBEFlow.metaClass, DEPENDENCY, DEPENDENCIES);
            terminology.define(DbBEUseCase.metaClass, COMPONENT, COMPONENTS);
            terminology.define(DbBEStore.metaClass, OBJECT, OBJECTS);
            terminology.define(DbBEActor.metaClass, ACTOR, ACTORS);
            terminology.define(DbBEDiagram.metaClass, DbInitialization.UML_COMPONENT_DIAGRAM_TXT,
                    UML_COMPONENT_DIAGRAM_TXTS);

            terminology.define(DbBEUseCase.metaClass, dbbecomponent);
            terminology.define(DbBEUseCaseGo.metaClass, dbbesystem);
            terminology.define(DbBEActor.metaClass, dbbeactor);
            terminology.define(DbBEStore.metaClass, dbbeobject);
            terminology.define(DbBEModel.metaClass, dbbeumlmodel);
            terminology.define(DbBEActorGo.metaClass, dbbeactorgroup);
            terminology.define(DbBEStoreGo.metaClass, dbbeobjectgroup);
            terminology.define(DbBEDiagram.metaClass, dbbecomponentdiagram);
            terminology.define(DbBEUseCaseResource.metaClass, dbbecomponentexternal);

        }

        /************************************************************************
         * UML_DEPLOYMENT_DIAGRAM_TXT
         * 
         * */

        else if (name.equals(DbInitialization.UML_DEPLOYMENT_DIAGRAM_TXT)) {
            terminology.define(DbBEUseCaseGo.metaClass, SYSTEM, SYSTEMS);
            terminology.define(DbBEModel.metaClass, UML_MODEL, UML_MODELS);
            terminology.define(DbBEFlow.metaClass, ASSOCIATION, ASSOCIATIONS);
            terminology.define(DbBEUseCase.metaClass, NODE, NODES);
            terminology.define(DbBEStore.metaClass, COMPONENT, COMPONENTS);
            terminology.define(DbBEActor.metaClass, ACTOR, ACTORS);

            terminology.define(DbBEUseCase.metaClass, dbbedeployment);
            terminology.define(DbBEUseCaseGo.metaClass, dbbesystem);
            terminology.define(DbBEActor.metaClass, dbbeactor);
            terminology.define(DbBEStore.metaClass, dbbecomponent);
            terminology.define(DbBEModel.metaClass, dbbeumlmodel);
            terminology.define(DbBEActorGo.metaClass, dbbeactorgroup);
            terminology.define(DbBEStoreGo.metaClass, dbbecomponent);
            terminology.define(DbBEDiagram.metaClass, dbbedeploymentdiagram);
            terminology.define(DbBEDiagram.metaClass, DbInitialization.UML_DEPLOYMENT_DIAGRAM_TXT,
                    UML_DEPLOYMENT_DIAGRAM_TXTS);
            terminology.define(DbBEUseCaseResource.metaClass, dbbenodeexternal);

        }

        /************************************************************************
         * MESSAGE_MODELING
         * 
         * */

        else if (name.equals(DbBENotation.MESSAGE_MODELING)) {
            terminology.define(DbBEFlow.metaClass, MESSAGE, MESSAGES);
            terminology.define(DbBEUseCase.metaClass, PROCESSOR, PROCESSORS);
            terminology.define(DbBEStore.metaClass, STORE, STORES);
            terminology.define(DbBEActor.metaClass, EXTERNAL_ENTITY, EXTERNAL_ENTITIES);
        }

        /************************************************************************
         * OBJECT_LIFE_CYCLE
         * 
         * */

        else if (name.equals(DbBENotation.OBJECT_LIFE_CYCLE)) {
            terminology.define(DbBEFlow.metaClass, TRANSITION, TRANSITIONS);
            terminology.define(DbBEUseCase.metaClass, ACTIVITY, ACTIVITIES);
            terminology.define(DbBEStore.metaClass, STATE, STATES);
            terminology.define(DbBEActor.metaClass, EVENT, EVENTS);
        }

        /************************************************************************
         * FUNCTIONAL_DIAGRAM
         * 
         * */

        else if (name.equals(DbBENotation.FUNCTIONAL_DIAGRAM)) {
            terminology.define(DbBEModel.metaClass, FUNCTIONAL_MODEL, FUNCTIONAL_MODELS);
            terminology.define(DbBEUseCase.metaClass, TASK, TASKS);
            terminology.define(DbBEStore.metaClass, STORAGE, STORAGES);
            terminology.define(DbBEActor.metaClass, EXTERNAL_ACTOR, EXTERNAL_ACTORS);
        }

        else if (name.equals(DbORNotation.INFORMATION_ENGINEERING)
                || name.equals(DbORNotation.INFORMATION_ENGINEERING_PLUS)) {

            terminology.define(DbORAssociation.metaClass, RELATIONSHIP, LocaleMgr.term
                    .getString("Relationships"));
            terminology.define(DbORColumn.metaClass, ATTRIBUTE, ATTRIBUTES);
            terminology.define(DbORTable.metaClass, ENTITY, ENTITIES);
            terminology.define(DbORAssociationEnd.metaClass, CARDINALITY, CARDINALITIES);

            terminology.define(DbGEColumn.metaClass, ATTRIBUTE, ATTRIBUTES);
            terminology.define(DbGETable.metaClass, ENTITY, ENTITIES);

            terminology.define(DbORAColumn.metaClass, ATTRIBUTE, ATTRIBUTES);
            terminology.define(DbORATable.metaClass, ENTITY, ENTITIES);

            terminology.define(DbINFColumn.metaClass, ATTRIBUTE, ATTRIBUTES);
            terminology.define(DbINFTable.metaClass, ENTITY, ENTITIES);

            terminology.define(DbIBMColumn.metaClass, ATTRIBUTE, ATTRIBUTES);
            terminology.define(DbIBMTable.metaClass, ENTITY, ENTITIES);

            // super-copy for entity
            terminology.define(DbORTable.metaClass, DbORTable.fSuperCopy, SUPERENTITY,
                    SUPERENTITIES);
            terminology.define(DbGETable.metaClass, DbGETable.fSuperCopy, SUPERENTITY,
                    SUPERENTITIES);
            terminology.define(DbORATable.metaClass, DbORATable.fSuperCopy, SUPERENTITY,
                    SUPERENTITIES);
            terminology.define(DbINFTable.metaClass, DbINFTable.fSuperCopy, SUPERENTITY,
                    SUPERENTITIES);
            terminology.define(DbIBMTable.metaClass, DbIBMTable.fSuperCopy, SUPERENTITY,
                    SUPERENTITIES);

            // subcopies for entity
            terminology.define(DbORTable.metaClass, DbORTable.fSubCopies, SUBENTITIES, SUBENTITIES);
            terminology.define(DbGETable.metaClass, DbGETable.fSubCopies, SUBENTITIES, SUBENTITIES);
            terminology.define(DbORATable.metaClass, DbORATable.fSubCopies, SUBENTITIES,
                    SUBENTITIES);
            terminology.define(DbINFTable.metaClass, DbINFTable.fSubCopies, SUBENTITIES,
                    SUBENTITIES);
            terminology.define(DbIBMTable.metaClass, DbIBMTable.fSubCopies, SUBENTITIES,
                    SUBENTITIES);

            // subcopies and super-copy for relationships
            terminology.define(DbORAssociation.metaClass, DbORTable.fSuperCopy, SUPERENTITY,
                    SUPERENTITY);
            terminology.define(DbORAssociation.metaClass, DbORTable.fSubCopies, SUBENTITIES,
                    SUBENTITIES);

            // subcopies and super-copy for relationships
            terminology.define(DbORColumn.metaClass, DbORPrimaryUnique.fColumns, ATTRIBUTES,
                    ATTRIBUTES);
            terminology.define(DbGEColumn.metaClass, DbGEPrimaryUnique.fColumns, ATTRIBUTES,
                    ATTRIBUTES);
            terminology.define(DbORAColumn.metaClass, DbORAPrimaryUnique.fColumns, ATTRIBUTES,
                    ATTRIBUTES);
            terminology.define(DbINFColumn.metaClass, DbINFPrimaryUnique.fColumns, ATTRIBUTES,
                    ATTRIBUTES);
            terminology.define(DbIBMColumn.metaClass, DbIBMPrimaryUnique.fColumns, ATTRIBUTES,
                    ATTRIBUTES);

            // components
            terminology.define(DbORFKeyColumn.metaClass, DbORFKeyColumn.fComponents, COLUMNS,
                    COLUMNS);

            // super-copy for attribute
            terminology.define(DbORColumn.metaClass, DbORTable.fSuperCopy, SUPERENTITY,
                    SUPERENTITIES);
            terminology.define(DbGEColumn.metaClass, DbGETable.fSuperCopy, SUPERENTITY,
                    SUPERENTITIES);
            terminology.define(DbORAColumn.metaClass, DbORATable.fSuperCopy, SUPERENTITY,
                    SUPERENTITIES);
            terminology.define(DbINFColumn.metaClass, DbINFTable.fSuperCopy, SUPERENTITY,
                    SUPERENTITIES);
            terminology.define(DbIBMColumn.metaClass, DbIBMTable.fSuperCopy, SUPERENTITY,
                    SUPERENTITIES);

            // subcopies for attribute
            terminology
                    .define(DbORColumn.metaClass, DbORTable.fSubCopies, SUBENTITIES, SUBENTITIES);
            terminology
                    .define(DbGEColumn.metaClass, DbGETable.fSubCopies, SUBENTITIES, SUBENTITIES);
            terminology.define(DbORAColumn.metaClass, DbORATable.fSubCopies, SUBENTITIES,
                    SUBENTITIES);
            terminology.define(DbINFColumn.metaClass, DbINFTable.fSubCopies, SUBENTITIES,
                    SUBENTITIES);
            terminology.define(DbIBMColumn.metaClass, DbIBMTable.fSubCopies, SUBENTITIES,
                    SUBENTITIES);

            // cardinalities on relationships
            terminology.define(DbORAssociationEnd.metaClass, DbORAssociation.fComponents,
                    CARDINALITIES, CARDINALITIES);
            terminology.define(DbORAssociationEnd.metaClass, DbORAssociation.fComponents,
                    CARDINALITIES, CARDINALITIES);
        }

    }
}
