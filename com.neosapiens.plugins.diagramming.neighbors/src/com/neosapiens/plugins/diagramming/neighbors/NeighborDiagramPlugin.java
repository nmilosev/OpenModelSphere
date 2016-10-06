/*************************************************************************

Copyright (C) 2009 by neosapiens inc.

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

You can reach neosapiens inc. at: 

neosapiens inc.
1236 Gaudias-Petitclerc
Qu&eacute;bec, Qc, G1Y 3G2
CANADA
Telephone: 418-561-8403
Fax: 418-650-2375
http://www.neosapiens.com/
Email: marco.savard@neosapiens.com
       gino.pelletier@neosapiens.com

 **********************************************************************/

package com.neosapiens.plugins.diagramming.neighbors;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginAction;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.srtool.*;
import org.modelsphere.jack.srtool.actions.PluginDefaultAction;
import org.modelsphere.jack.srtool.forward.VariableScope;
import org.modelsphere.jack.srtool.graphic.DbGraphic;
import org.modelsphere.jack.srtool.graphic.DiagramInternalFrame;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSInheritanceGo;
import org.modelsphere.sms.db.util.AnyGo;
import org.modelsphere.sms.oo.db.*;
import org.modelsphere.sms.oo.forward.OOForwardEngineeringPlugin;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.graphic.JVDiagramLayout;
import org.modelsphere.sms.or.db.*;
import org.modelsphere.sms.or.graphic.ORDiagramLayout;

/**
 * 
 * This is the entry point of the Neighbor Diagram plugin
 * 
 * @author Marco Savard
 * 
 */
public class NeighborDiagramPlugin extends OOForwardEngineeringPlugin implements Plugin2 {

    public PluginSignature getSignature() {
        return null;
    }

    public OptionGroup getOptionGroup() {
        return null;
    }

    public PluginAction getPluginAction() {
        return new PluginDefaultAction(this);
    }

    public Class[] getSupportedClasses() {
        return new Class[] { DbJVClass.class, DbORAbsTable.class };
    }

    public String installAction(DefaultMainFrame mf, MainFrameMenu menu) {
        return null;
    }

    @Override
    public VariableScope getVarScope() {
        return null;
    }

    @Override
    public void setVarScope(VariableScope varScope) {
    }

    public void execute(ActionEvent ev) throws Exception {
        final DbObject[] selectedobjs = FocusManager.getSingleton().getSelectedSemanticalObjects();
        if (selectedobjs == null || selectedobjs.length == 0) {
            return;
        }

        try {
            DbObject dbo = selectedobjs[0];
            String transaction = LocaleMgr.misc.getString("GenerateNeighborhoodDiagram");
            dbo.getDb().beginTrans(Db.WRITE_TRANS, transaction);

            // create diagram
            for (DbObject selecteddbo : selectedobjs) {
                if (selecteddbo instanceof DbJVClass) {
                    DbJVClass claz = (DbJVClass) selecteddbo;
                    createNeighborDiagram(claz);
                } else if (selecteddbo instanceof DbORAbsTable) {
                    DbORAbsTable table = (DbORAbsTable) selecteddbo;
                    createNeighborDiagram(table);
                }
            } // end for

            dbo.getDb().commitTrans();
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }

    } // end execute()

    //
    // private methods
    //
    private void createNeighborDiagram(DbJVClass claz) throws DbException {

        // create diagram
        DbObject dbo = claz.getComposite();
        DbOODiagram diagram = new DbOODiagram(dbo);
        String className = claz.getName();
        String pattern = LocaleMgr.misc.getString("ClassifierNeighborhood");
        String diagramName = MessageFormat.format(pattern, new Object[] { className });
        diagram.setName(diagramName);

        // add this class
        List<DbOOAdtGo> gos = new ArrayList<DbOOAdtGo>();
        DbOOAdtGo go = new DbOOAdtGo(diagram, claz);
        gos.add(go);
        createSuperClassGos(diagram, go, gos);
        createSubClassGos(diagram, go, gos);
        createNeighborGos(diagram, go, gos);

        // display diagram
        DefaultMainFrame mainFrame = ApplicationContext.getDefaultMainFrame();
        DiagramInternalFrame frame = mainFrame.addDiagramInternalFrame(diagram);

        // layout diagram & refresh
        GraphicComponent[] comps = getGraphicComponents(gos);
        new JVDiagramLayout(diagram, comps);
        frame.refresh();

    } // end createNeighborDiagram()

    private void createNeighborDiagram(DbORAbsTable table) throws DbException {

        // create diagram
        DbObject dbo = table.getComposite();
        DbORDiagram diagram = new DbORDiagram(dbo);
        String tableName = table.getName();
        String pattern = LocaleMgr.misc.getString("ClassifierNeighborhood");
        String diagramName = MessageFormat.format(pattern, new Object[] { tableName });
        diagram.setName(diagramName);

        // add this class
        List<DbORTableGo> gos = new ArrayList<DbORTableGo>();
        DbORTableGo go = new DbORTableGo(diagram, table);
        gos.add(go);
        createNeighborGos(diagram, go, gos);

        // display diagram
        DefaultMainFrame mainFrame = ApplicationContext.getDefaultMainFrame();
        DiagramInternalFrame frame = mainFrame.addDiagramInternalFrame(diagram);

        // layout diagram & refresh
        GraphicComponent[] comps = getGraphicComponents(gos);
        new ORDiagramLayout(diagram, comps);
        frame.refresh();

    } // end createNeighborDiagram()

    private void createSuperClassGos(DbOODiagram diagram, DbOOAdtGo classGo, List<DbOOAdtGo> gos)
            throws DbException {
        DbJVClass claz = (DbJVClass) classGo.getSO();

        DbRelationN relN = claz.getSuperInheritances();
        DbEnumeration enu = relN.elements(DbOOInheritance.metaClass);
        while (enu.hasMoreElements()) {
            DbOOInheritance inher = (DbOOInheritance) enu.nextElement();
            DbJVClass superclass = (DbJVClass) inher.getSuperClass();
            DbOOAdtGo go = new DbOOAdtGo(diagram, superclass);
            gos.add(go);

            new DbSMSInheritanceGo(diagram, classGo, go, inher);
        } // end if
        enu.close();
    }

    private void createSubClassGos(DbOODiagram diagram, DbOOAdtGo classGo, List<DbOOAdtGo> gos)
            throws DbException {
        DbJVClass claz = (DbJVClass) classGo.getSO();

        DbRelationN relN = claz.getSubInheritances();
        DbEnumeration enu = relN.elements(DbOOInheritance.metaClass);
        while (enu.hasMoreElements()) {
            DbOOInheritance inher = (DbOOInheritance) enu.nextElement();
            DbJVClass subclass = (DbJVClass) inher.getSubClass();
            DbOOAdtGo go = new DbOOAdtGo(diagram, subclass);
            gos.add(go);

            new DbSMSInheritanceGo(diagram, go, classGo, inher);
        } // end if
        enu.close();
    }

    private void createNeighborGos(DbOODiagram diagram, DbOOAdtGo classGo, List<DbOOAdtGo> gos)
            throws DbException {
        DbJVClass claz = (DbJVClass) classGo.getSO();

        List<DbJVClass> neighbors = new ArrayList<DbJVClass>();
        neighbors.add(claz);

        // find neighbors
        DbRelationN relN = claz.getComponents();
        DbEnumeration enu = relN.elements(DbJVDataMember.metaClass);
        while (enu.hasMoreElements()) {
            DbJVDataMember field = (DbJVDataMember) enu.nextElement();
            DbOOAssociationEnd end = field.getAssociationEnd();
            if (end != null) {
                DbOOAssociationEnd oppEnd = end.getOppositeEnd();
                DbJVDataMember oppField = (DbJVDataMember) oppEnd.getAssociationMember();
                DbJVClass oppClass = (DbJVClass) oppField.getCompositeOfType(DbJVClass.metaClass);
                if (!neighbors.contains(oppClass)) {
                    neighbors.add(oppClass);
                }
            }
        } // end if
        enu.close();

        // create a GO for each neighbor
        for (DbJVClass neighbor : neighbors) {
            if (!neighbor.equals(claz)) {
                DbOOAdtGo neighborGo = new DbOOAdtGo(diagram, neighbor);
                createAssociationGos(diagram, classGo, neighborGo);
                gos.add(neighborGo);
            }
        } // end for

    } // end createNeighborGos

    private void createNeighborGos(DbORDiagram diagram, DbORTableGo tableGo, List<DbORTableGo> gos)
            throws DbException {
        //list of neighbors to add in the diagram
        List<DbORAbsTable> neighbors = new ArrayList<DbORAbsTable>();
        DbORAbsTable table = (DbORAbsTable) tableGo.getSO();       
        neighbors.add(table);
        
        //list of associations to add
        List<DbORAssociation> assocList = new ArrayList<DbORAssociation>();
        
        // find neighbors
        DbRelationN relN = table.getAssociationEnds();
        DbEnumeration enu = relN.elements(DbORAssociationEnd.metaClass);
        while (enu.hasMoreElements()) {
            DbORAssociationEnd end = (DbORAssociationEnd) enu.nextElement();
            DbORAssociationEnd oppEnd = end.getOppositeEnd();
            DbORAbsTable oppTable = (DbORAbsTable) oppEnd.getClassifier();

            if (! neighbors.contains(oppTable)) {
                neighbors.add(oppTable);
            }
            
            DbORAssociation assoc = (DbORAssociation)end.getCompositeOfType(DbORAssociation.metaClass); 
            if (! assocList.contains(assoc)) {
                assocList.add(assoc);
            }
            
            DbORChoiceOrSpecialization choiceSpec = assoc.getChoiceOrSpecialization();
            if (choiceSpec != null) {
                if (! neighbors.contains(choiceSpec)) {
                    neighbors.add(choiceSpec);
                }
                
                assoc = findParentAssociation(choiceSpec); 
                if (! assocList.contains(assoc)) {
                    assocList.add(assoc);
                }
            }
        } // end if
        enu.close();
        
        //add choice's neighbors
        if (table instanceof DbORChoiceOrSpecialization) {
            DbORChoiceOrSpecialization choiceSpec = (DbORChoiceOrSpecialization)table;
            
            
            enu = choiceSpec.getAssociations().elements(DbORAssociation.metaClass); 
            while (enu.hasMoreElements()) {
                DbORAssociation assoc = (DbORAssociation)enu.nextElement(); 
                DbORAbsTable frontTable = assoc.getFrontEnd().getClassifier();
                if (! neighbors.contains(frontTable)) {
                    neighbors.add(frontTable);
                }
                
                DbORAbsTable backTable = assoc.getBackEnd().getClassifier();
                if (! neighbors.contains(backTable)) {
                    neighbors.add(backTable);
                }
                
                if (! assocList.contains(assoc)) {
                    assocList.add(assoc);
                }
            } //end while
            enu.close();
        } // end if

        // create a GO for each neighbor
        for (DbORAbsTable neighbor : neighbors) {
            if (!neighbor.equals(table)) {
                DbORTableGo neighborGo = new DbORTableGo(diagram, neighbor);
                //createAssociationGos(diagram, tableGo, neighborGo);
                gos.add(neighborGo);
            }
        } // end for
        
        //create a GO for each neighbor's assoc 
        for (DbORAssociation assoc : assocList) {
            AnyGo.createORAssociationGo(diagram, assoc, (Point)null);
        }
     
    } // end createNeighborGos

    private DbORAssociation findParentAssociation(
            DbORChoiceOrSpecialization choiceSpec) throws DbException {
        DbORAssociationEnd parentAssocEnd = null;
        DbORTable parent = choiceSpec.getParentTable(); 
        DbEnumeration enu = choiceSpec.getAssociationEnds().elements(DbORAssociationEnd.metaClass); 
        while (enu.hasMoreElements()) { 
            DbORAssociationEnd assocEnd = (DbORAssociationEnd)enu.nextElement(); 
            DbORAssociationEnd oppAssoc = assocEnd.getOppositeEnd(); 
            
            if (parent.equals(oppAssoc.getClassifier())) {
                parentAssocEnd = oppAssoc;
                break;
            }
        } //end while
        enu.close();
        
        DbORAssociation parentAssoc = (parentAssocEnd == null) ? null : (DbORAssociation)parentAssocEnd.getCompositeOfType(DbORAssociation.metaClass); 
        return parentAssoc;
    }


    private void createAssociationGos(DbOODiagram diagram, DbOOAdtGo classGo, DbOOAdtGo neighborGo)
            throws DbException {
        DbJVClass claz = (DbJVClass) classGo.getSO();
        DbJVClass neighbor = (DbJVClass) neighborGo.getSO();

        DbRelationN relN = neighbor.getComponents();
        DbEnumeration enu = relN.elements(DbJVDataMember.metaClass);
        while (enu.hasMoreElements()) {
            DbJVDataMember field = (DbJVDataMember) enu.nextElement();
            DbOOAssociationEnd end = field.getAssociationEnd();
            if (end != null) {
                DbOOAssociationEnd oppEnd = end.getOppositeEnd();
                DbJVDataMember oppField = (DbJVDataMember) oppEnd.getAssociationMember();
                DbJVClass oppClass = (DbJVClass) oppField.getCompositeOfType(DbJVClass.metaClass);

                if (oppClass.equals(claz)) {
                    DbOOAssociation assoc = (DbOOAssociation) oppEnd
                            .getCompositeOfType(DbOOAssociation.metaClass);
                    DbOOClass frontAdt = (DbOOClass) assoc.getFrontEnd().getAssociationMember()
                            .getComposite();
                    DbOOClass backAdt = (DbOOClass) assoc.getBackEnd().getAssociationMember()
                            .getComposite();

                    DbOOAdtGo frontEnd = frontAdt.equals(claz) ? classGo : neighborGo;
                    DbOOAdtGo backEnd = backAdt.equals(claz) ? classGo : neighborGo;
                    new DbOOAssociationGo(diagram, frontEnd, backEnd, assoc);
                }
            }
        } // end if

        enu.close();
    } // end createAssociationGos()

    /*
    private void createAssociationGo(DbORDiagram diagram, DbORTableGo tableGo, DbORTableGo neighborGo, DbORAssociation assoc) throws DbException {
        DbORAssociationEnd frontEnd = assoc.getFrontEnd();
        DbORAssociationEnd backEnd = assoc.getBackEnd();
        DbORAbsTable frontTable = frontEnd.getClassifier();
        DbORAbsTable backTable = backEnd.getClassifier();
        DbORChoiceOrSpecialization choiceSpec = assoc.getChoiceOrSpecialization();
        boolean directAssociation = true;
        
        if (choiceSpec != null) {
            if (!choiceSpec.equals(frontTable) && !choiceSpec.equals(backTable)) {
                directAssociation = false;
            }
        } //end if
       
        
        DbORAbsTable table = (DbORAbsTable) tableGo.getSO();
        if (directAssociation) {
            //DbORTableGo frontEndGo = frontTable.equals(table) ? tableGo : neighborGo;
            //DbORTableGo backEndGo = backTable.equals(table) ? tableGo : neighborGo;
            AnyGo.createORAssociationGo(diagram, assoc, (Point)null);
            //new DbORAssociationGo(diagram, backEndGo, frontEndGo, assoc);
        } else {
            if (DbGraphic.getFirstGraphicalObject(diagram, assoc) == null) {
                AnyGo.createORAssociationGo(diagram, assoc, (Point)null);
            }
        }
    } //end if
    */
    
    /*
    private void createDirectAssociationGo(DbORDiagram diagram, DbORTableGo frontEndGo, DbORTableGo backEndGo, DbORAssociation assoc) throws DbException {
        new DbORAssociationGo(diagram, backEndGo, frontEndGo, assoc);
    }
    
    private void createChoiceAssociationGos(DbORDiagram diagram, DbORTableGo tableGo, DbORTableGo neighborGo, DbORAssociation assoc, DbORChoiceOrSpecialization choiceSpec) throws DbException {
        
        DbEnumeration enu = choiceSpec.getAssociations().elements(DbORAssociation.metaClass);
        while (enu.hasMoreElements()) {
            DbORAssociation choiceAssoc = (DbORAssociation)enu.nextElement(); 
            if (! choiceAssoc.equals(assoc)) {
                DbORAbsTable oppTable = findOppositeTable(choiceAssoc, (DbORAbsTable)neighborGo.getSO());
                DbORTableGo oppTableGo = getGo(oppTable);
                createAssociationGo(diagram, neighborGo, oppTableGo, choiceAssoc);
            }
        } //end while
        enu.close(); 
    }*/

    private DbORAbsTable findOppositeTable(DbORAssociation assoc, DbORAbsTable table) throws DbException {
        DbORAssociationEnd frontEnd = assoc.getFrontEnd();
        DbORAssociationEnd backEnd = assoc.getBackEnd();
        DbORAbsTable frontTable = frontEnd.getClassifier();
        DbORAbsTable backTable = backEnd.getClassifier();
        DbORAbsTable oppTable;
        
        if (frontTable.equals(table)) {
            oppTable = backTable;
        } else {
            oppTable = backTable;
        }
        
        return oppTable;
    }

    private DbORTableGo getGo(DbORAbsTable table) throws DbException {
        DbORTableGo tableGo = null;
     
        DbEnumeration enu = table.getClassifierGos().elements(DbORTableGo.metaClass);
        while (enu.hasMoreElements()) {
            tableGo = (DbORTableGo)enu.nextElement(); 
            break;
        }
        enu.close(); 
        
        return tableGo;
    }

    private DbORAbsTable findChildTable(DbORChoiceOrSpecialization choiceSpec,
            DbORAbsTable frontTable, DbORAbsTable backTable) throws DbException {
        DbORAbsTable parent = choiceSpec.getParentTable();
        DbORAbsTable child; 
        
        if (parent.equals(frontTable)) {
            child = backTable; 
        } else {
            child = frontTable; 
        }
        
        return child;
    }

    private GraphicComponent[] getGraphicComponents(List<? extends DbSMSClassifierGo> gos)
            throws DbException {
        List<GraphicComponent> components = new ArrayList<GraphicComponent>();

        for (DbSMSClassifierGo go : gos) {
            Object peer = go.getGraphicPeer();
            if (peer instanceof GraphicComponent) {
                components.add((GraphicComponent) peer);
            }
        } // end for

        GraphicComponent[] componentArray = new GraphicComponent[components.size()];
        components.toArray(componentArray);

        return componentArray;
    }

}
