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

package org.modelsphere.sms.plugins.report.model;

// Jack

// SMS

// JDK
import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.modelsphere.jack.awt.tree.CheckTreeModel;
import org.modelsphere.jack.awt.tree.CheckTreeNode;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.screen.model.DbListModel;
import org.modelsphere.jack.baseDb.screen.model.DbListModel.Partition;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.jack.util.SrVector;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.db.DbSMSUmlExtensibility;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.plugins.report.PropertiesSet;
import org.modelsphere.sms.plugins.report.screen.PropertiesTreeNode;

public class ReportModel {

    private static final File defaultFile = new File("report.properties"); // NOT LOCALIZABLE

    private DbObject[] m_entryPoints;
    private PropertiesSet propertiesSet = new PropertiesSet(defaultFile);

    private Concept[] concepts;
    private CheckTreeModel treeModel;

    private CheckTreeNode welcomeConceptNode = null;

    private ReportOptions m_options;

    // Options
    private boolean generateIndex = true;
    private File backgroundImage = new File("");

    public ReportModel(DbObject[] entryPoints) throws DbException {
        m_entryPoints = entryPoints;
        m_options = new ReportOptions(this);

        // Look for all the metaClasses
        // only the useful ones are returned (in alphabetic order)
        DbListModel.Partition[] metaClasses = getOrderedMetaClasses();
        
        //obtain concept list from metaClasses
        List<Concept> conceptList = getConceptList(metaClasses);
       
        //copy into array 
        concepts = new Concept[conceptList.size()];
        conceptList.toArray(concepts); 
       
        treeModel = createNewTreeModel(concepts);
    }

    // ***************************************************************************
    // methods to retrieve all metaClasses

	// adapted from org.modelsphere.jack.baseDb.screen.model.UDFListModel (loadPartitions())
    private final DbListModel.Partition[] getOrderedMetaClasses() throws DbException {
        SrVector vecParts = new SrVector();
        SrVector vecDiagramParts = new SrVector();

        loadParts(vecParts, DbSemanticalObject.metaClass);
        loadParts(vecDiagramParts, DbSMSDiagram.metaClass);

        vecParts.addAll(vecDiagramParts);
        vecParts.sort();
        DbListModel.Partition[] partitions = new DbListModel.Partition[vecParts.size()];
        vecParts.getElements(0, partitions.length, partitions, 0);

        return partitions;
    }
    
    private List<Concept> getConceptList(Partition[] metaClasses) {
    	List<Concept> conceptList = new ArrayList<Concept>();
    	List<MetaClass> filteredMetaClasses = getFilteredMetaClasses(); 
    	
    	for (int i = 0; i < metaClasses.length; i++) {
    		MetaClass mc = (MetaClass) metaClasses[i].partId;
    		boolean isFiltered = filteredMetaClasses.contains(mc); 
    		
    		if (! isFiltered) {
    			Concept concept = new Concept(m_entryPoints, mc);
    			conceptList.add(concept);
    		} //end if
        } //end for
    	
		return conceptList;
	} //end getConceptList()

    private List<MetaClass> getFilteredMetaClasses() {
    	List<MetaClass> filteredMetaClasses = new ArrayList<MetaClass>();
    	
    	//Data Model Viewer" excludes OO and BPM concepts 
    	if (! ScreenPerspective.isFullVersion()) {	 
    		buildRecursiveComponentListOf(filteredMetaClasses, DbJVClassModel.metaClass); 
    		buildRecursiveComponentListOf(filteredMetaClasses, DbBEModel.metaClass); 
    		buildRecursiveComponentListOf(filteredMetaClasses, DbSMSUmlExtensibility.metaClass);
    	}
    	
		return filteredMetaClasses;
	}

	//construct a list that contains all the possible components of 'composite', direct or indirect
    private void buildRecursiveComponentListOf(List<MetaClass> recursiveComponents, MetaClass composite) {
    	recursiveComponents.add(composite); 
    	MetaClass[] childComponents = composite.getComponentMetaClasses(true, false);
    	
    	for (MetaClass childComponent : childComponents) {
    		if (! recursiveComponents.contains(childComponent)) {
    			recursiveComponents.add(childComponent); 
    			buildRecursiveComponentListOf(recursiveComponents, childComponent); 
    		} //end if
    	} //end for
	} //end buildRecursiveComponentListOf()

	// adapted from org.modelsphere.jack.baseDb.screen.model.UDFListModel
    private void loadParts(SrVector vecParts, MetaClass metaClass) {
        if ((!Modifier.isAbstract(metaClass.getJClass().getModifiers())
                && ((metaClass.getFlags() & MetaClass.NO_UDF) == 0) || (metaClass != DbSMSDiagram.metaClass && DbSMSDiagram.metaClass
                .isAssignableFrom(metaClass)))
                || metaClass == DbSMSTargetSystem.metaClass
                //|| metaClass == org.modelsphere.sms.db.DbSMSBuiltInTypeNode.metaClass
                || metaClass == org.modelsphere.sms.or.db.DbORBuiltInType.metaClass
                //|| metaClass == org.modelsphere.sms.or.db.DbORUserNode.metaClass
                || metaClass == org.modelsphere.sms.db.DbSMSBuiltInTypePackage.metaClass) {
            String guiName = ApplicationContext.getSemanticalModel()
                    .getDisplayTextForUDF(metaClass);
            vecParts.addElement(new DbListModel.Partition(metaClass, guiName));
        }

        MetaClass[] subClasses = metaClass.getSubMetaClasses();
        for (int i = 0; i < subClasses.length; i++)
            loadParts(vecParts, subClasses[i]);
    }

    //
    // ***************************************************************************

    private CheckTreeModel createNewTreeModel(Concept[] concepts) {
        PropertiesTreeNode root = new PropertiesTreeNode(m_options, true, true);

        for (int i = 0; i < concepts.length; i++) {
            PropertiesTreeNode node;

            node = new PropertiesTreeNode(new ConceptProperties(concepts[i]), true, false);
            root.add(node);

            if (DbSMSDiagram.metaClass.isAssignableFrom(concepts[i].getMetaClass())) {
                // In the case of a diagram, only 2 fields will be available:
                // Composite and Name
                PropertiesTreeNode compositeChild = new PropertiesTreeNode(
                        new ConceptAttributeProperties(concepts[i].getMetaClass(),
                                DbSMSDiagram.fComposite), true, true);
                node.add(compositeChild);

                PropertiesTreeNode nameChild = new PropertiesTreeNode(
                        new ConceptAttributeProperties(concepts[i].getMetaClass(),
                                DbSMSDiagram.fName), true, true);
                node.add(nameChild);
            } else {
                MetaField[] fields = concepts[i].getFields();
                for (int j = 0; j < fields.length; j++) {
                    PropertiesTreeNode child = new PropertiesTreeNode(
                            new ConceptAttributeProperties(concepts[i].getMetaClass(), fields[j]),
                            true, true);
                    node.add(child);
                }

                MetaClass[] components = concepts[i].getComponents();
                for (int j = 0; j < components.length; j++) {
                    ArrayList fieldList;

                    if (DbSMSDiagram.metaClass.isAssignableFrom(components[j])) {
                        fieldList = new ArrayList();
                        //fieldList.add(DbSMSDiagram.fComposite);
                        fieldList.add(DbSMSDiagram.fName);
                    } else
                        fieldList = components[j].getScreenMetaFields();

                    PropertiesTreeNode componentNode = new PropertiesTreeNode(
                            new ConceptComponentProperties(concepts[i].getMetaClass(),
                                    DbObject.fComponents, components[j]), true, true);
                    node.add(componentNode);

                    Iterator x = fieldList.iterator();
                    while (x.hasNext()) {
                        componentNode.add(new PropertiesTreeNode(new ComponentAttributeProperties(
                                concepts[i].getMetaClass(), components[j], (MetaField) x.next()),
                                true, true));
                    }
                }
            }
        }

        return new CheckTreeModel(root);
    }

    // remove this method
    public Concept[] getConcepts() {
        return concepts;
    }

    // and replace it with
    public CheckTreeModel getTreeReprentation() {
        return treeModel;
    }

    public CheckTreeNode getConceptTreeNodeFromName(String name) {
        CheckTreeNode root = (CheckTreeNode) treeModel.getRoot();

        Enumeration enumeration = root.children();
        while (enumeration.hasMoreElements()) {
            CheckTreeNode child = (CheckTreeNode) enumeration.nextElement();
            Object userObject = child.getUserObject();

            if (userObject instanceof ConceptProperties) {
                ConceptProperties properties = (ConceptProperties) userObject;
                if (properties.getConcept().getMetaClass().toString().equals(name))
                    return child;
            }
        }

        return null;
    }

    public CheckTreeNode getConceptTreeNodeFromMetaClass(MetaClass metaClass) {
        CheckTreeNode root = (CheckTreeNode) treeModel.getRoot();

        Enumeration enumeration = root.children();
        while (enumeration.hasMoreElements()) {
            CheckTreeNode child = (CheckTreeNode) enumeration.nextElement();
            Object userObject = child.getUserObject();

            if (userObject instanceof ConceptProperties) {
                ConceptProperties properties = (ConceptProperties) userObject;
                if (properties.getConcept().getMetaClass() == metaClass)
                    return child;
            }

            //if(child.toString().equals(name))
            //  return child;
        }

        return null;
    }

    public DbObject[] getEntryPoints() {
        return m_entryPoints;
    }

    public ReportOptions getOptions() {
        return m_options;
    }
}
