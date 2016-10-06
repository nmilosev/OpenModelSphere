/*************************************************************************

Copyright (C) 2010 Grandite

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

package org.modelsphere.sms.plugins.statistics;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Vector;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.debug.ConceptPair;
import org.modelsphere.jack.debug.StatisticsProviderBase;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.plugins.Plugin;
import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginAction;
import org.modelsphere.jack.plugins.PluginSelectionListener;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;
import org.modelsphere.jack.srtool.explorer.ExplorerView;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DiagramInternalFrame;
import org.modelsphere.jack.util.ExceptionMessage;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSNotation;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSStyle;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORDomainModel;

//import org.modelsphere.sms.db.DbSMSDiagram;

/**
 * @author Grandite
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences -
 *         Java - Code Style - Code Templates
 */
public class DiagramStatisticsPlugin extends StatisticsProviderBase implements Plugin2 {

    public static final int DIAGRAM_TYPE_OO_UML_CLASS = 0;
    public static final int DIAGRAM_TYPE_BE_UML_USECASE = 1;
    public static final int DIAGRAM_TYPE_BE_UML_STATECHART = 10;
    public static final int DIAGRAM_TYPE_BE_UML_PROCESS = 11;
    public static final int DIAGRAM_TYPE_BE_UML_COLLABORATION = 2;
    public static final int DIAGRAM_TYPE_BE_UML_ACTIVITY = 3;
    public static final int DIAGRAM_TYPE_BE_UML_SEQUENCE = 4;
    public static final int DIAGRAM_TYPE_BE_UML_COMPONENT = 5;
    public static final int DIAGRAM_TYPE_BE_UML_DEPLOYMENT = 6;
    public static final int DIAGRAM_TYPE_OR_DATA = 7;
    public static final int DIAGRAM_TYPE_OR_DOMAIN = 8;
    public static final int DIAGRAM_TYPE_OR_COMMONITEMS = 9;

    private int type = -1;
    private String notation = "";
    private String defaultStyle = "";
    private int sheetsCount = 0;
    private Dimension dimensions = new Dimension();

    private DbSMSDiagram diagramGO = null;

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.debug.StatisticsProviderBase#compute()
     */
    public void compute() throws Exception {

        m_vConcepts = new Vector();

        ////
        // get the active diagram and populate the data members 

        diagramGO = null; //we reuse the same instance of the plugin,
        //so reset the diagram each time we enter this method

        Object focusObject = ApplicationContext.getFocusManager().getFocusObject();

        if (focusObject instanceof ExplorerView) {
            MainFrame mainFrame = MainFrame.getSingleton();
            DiagramInternalFrame diagramInternalFrame;
            DbObject[] dbObjects = ApplicationContext.getFocusManager()
                    .getSelectedSemanticalObjects();

            for (int i = 0; i < dbObjects.length; i++) //find the first diagram in the selection
            {
                if (dbObjects[i] instanceof DbSMSDiagram) {
                    diagramGO = (DbSMSDiagram) dbObjects[i];
                    break;
                }
            }
        } else if (focusObject instanceof ApplicationDiagram) {
            diagramGO = (DbSMSDiagram) ((ApplicationDiagram) focusObject).getDiagramGO();
        }

        if (diagramGO == null) {
            throw new ExceptionMessage("No diagram is selected.", ExceptionMessage.E_INFORMATION,
                    ExceptionMessage.L_NORMAL);
        }

        ////
        // populate data members with the common data and specialized concepts

        collectProperties();
        collectConcepts();

        ////
        // instanciate the dialog and hand the provider object

        String dialogTitle = "Diagram Statistics - ";
        String diagramName = "";
        try {
            Db db = diagramGO.getDb();
            db.beginReadTrans();

            diagramName = diagramGO.getName();

            db.commitTrans();
        } catch (DbException e) {
            throw new ExceptionMessage("Failed to extract the diagram name. Operation aborted.",
                    ExceptionMessage.E_ERROR, ExceptionMessage.L_NORMAL);
        }

        DiagramStatisticsDialog dialog = new DiagramStatisticsDialog(MainFrame.getSingleton(),
                dialogTitle + diagramName, this, true);
        dialog.setVisible(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.plugins.Plugin#execute(java.awt.event.ActionEvent)
     */
    public void execute(ActionEvent ev) throws Exception {
        compute();
    }

    private void collectProperties() throws Exception {

        Db db = diagramGO.getDb();
        try {
            db.beginReadTrans();

            //set style and dimensions
            DbSMSStyle style = findDiagramStyle(); 
        	String styleName = (style == null) ? LocaleMgr.misc.getString("Unknown") : style.getName();
            setDefaultStyle(styleName);
            
            Dimension dimensions = diagramGO.getNbPages();
            setSheetsCount((int) (dimensions.getWidth() * dimensions.getHeight()));
            setDimensions(dimensions);

            ////
            // Behavioral Diagram

            if (diagramGO instanceof DbBEDiagram) {
                DbBEDiagram diag = (DbBEDiagram) diagramGO;
                DbSMSNotation smsNotation = diag.findNotation();
                if (smsNotation == null) { //the notation is null, extract the project's default notation
                    smsNotation = ((DbSMSProject) style.getProject()).getBeDefaultNotation();
                    setNotation(smsNotation.getName() + " (project defaut)");
                } else
                    setNotation(smsNotation.getName());

                if (smsNotation != null) {
                    String beNotationName = ((DbBENotation) smsNotation).getName();
                    if (0 == beNotationName.compareTo(DbBENotation.UML_COLLABORATION_DIAGRAM))
                        setType(DIAGRAM_TYPE_BE_UML_COLLABORATION);
                    else if (0 == beNotationName.compareTo(DbBENotation.UML_SEQUENCE_DIAGRAM))
                        setType(DIAGRAM_TYPE_BE_UML_SEQUENCE);
                    else if (0 == beNotationName.compareTo(DbBENotation.UML_STATE_DIAGRAM))
                        setType(DIAGRAM_TYPE_BE_UML_STATECHART);
                    else if (0 == beNotationName.compareTo(DbBENotation.UML_USE_CASE))
                        setType(DIAGRAM_TYPE_BE_UML_USECASE);
                    else if (0 == beNotationName.compareTo("UML Activity Diagram"))
                        setType(DIAGRAM_TYPE_BE_UML_ACTIVITY);
                    else if (0 == beNotationName.compareTo("UML Component Diagram"))
                        setType(DIAGRAM_TYPE_BE_UML_COMPONENT);
                    else if (0 == beNotationName.compareTo("UML Deployment Diagram"))
                        setType(DIAGRAM_TYPE_BE_UML_DEPLOYMENT);
                    else
                        setType(DIAGRAM_TYPE_BE_UML_PROCESS);
                }
            }

            ////
            // Object Relational Diagram

            else if (diagramGO instanceof DbORDiagram) {
                DbORDiagram diag = (DbORDiagram) diagramGO;
                DbSMSNotation smsNotation = diag.getNotation();
                if (smsNotation == null) { //the notation is null, extract the project's default notation
                    DbORDataModel dataModel = (DbORDataModel)diag.getCompositeOfType(DbORDataModel.metaClass);
                    int mode = (dataModel == null) ? DbORDataModel.LOGICAL_MODE_OBJECT_RELATIONAL : dataModel.getOperationalMode(); 
                    DbSMSProject project = (DbSMSProject)style.getProject(); 
                    
                    if (mode == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                        notation = project.getErDefaultNotation().getName();
                    } else {
                        notation = project.getOrDefaultNotation().getName();
                    }
                    
                    setNotation(notation + " (project defaut)");
                } else
                    setNotation(smsNotation.getName());

                DbObject obj = diag.getComposite();
                if (obj instanceof DbORDomainModel)
                    setType(DIAGRAM_TYPE_OR_DOMAIN);
                else if (obj instanceof DbORCommonItemModel)
                    setType(DIAGRAM_TYPE_OR_COMMONITEMS);
                else if (obj instanceof DbORDataModel)
                    setType(DIAGRAM_TYPE_OR_DATA);
            }

            ////
            // Object Oriented (Class) Diagram

            else if (diagramGO instanceof DbOODiagram) {
                DbOODiagram diag = (DbOODiagram) diagramGO;
                setType(DIAGRAM_TYPE_OO_UML_CLASS);
                setNotation("Not applicable.");

            } else { //critical: we failed to extract the diagram type, abort
                throw new ExceptionMessage(
                        "This feature is not implemented for this type of diagram.",
                        ExceptionMessage.E_ERROR, ExceptionMessage.L_NORMAL);
            }

            db.commitTrans();
        } catch (DbException e) {
            throw new ExceptionMessage("Failed to extract diagram properties. Operation aborted.",
                    ExceptionMessage.E_ERROR, ExceptionMessage.L_NORMAL);
        }
    }

    private DbSMSStyle findDiagramStyle() throws DbException {
    	DbSMSStyle style = diagramGO.getStyle();
    	
        if (style == null) {
        	DbSMSProject project = (DbSMSProject)diagramGO.getCompositeOfType(DbSMSProject.metaClass); 
        	
        	if (diagramGO instanceof DbBEDiagram) {
        		style = project.getBeDefaultStyle();
        	} else if (diagramGO instanceof DbORDiagram) {
        		style = project.getOrDefaultStyle(); 
        	} else if (diagramGO instanceof DbOODiagram) {
        		style = project.getOoDefaultStyle(); 
        	}
        } //end if 
        
        return style; 
	}

	private void collectConcepts() throws Exception {

        ////
        // trouver le nombre d'instances des concepts

        if (diagramGO == null) {
            throw new ExceptionMessage("No diagram was set.", ExceptionMessage.E_ERROR,
                    ExceptionMessage.L_NORMAL);
        }

        try {
            diagramGO.getDb().beginReadTrans();

            DbRelationN relationN = null;

            if (diagramGO instanceof DbBEDiagram)
                relationN = ((DbBEDiagram) diagramGO).getComponents();
            else if (diagramGO instanceof DbORDiagram)
                relationN = ((DbORDiagram) diagramGO).getComponents();
            else if (diagramGO instanceof DbOODiagram)
                relationN = ((DbOODiagram) diagramGO).getComponents();

            if (relationN == null) {
                throw new ExceptionMessage("Failed to extract components from the diagram.",
                        ExceptionMessage.E_ERROR, ExceptionMessage.L_NORMAL);
            }

            DbEnumeration dbEnumAll = relationN.elements();

            while (dbEnumAll.hasMoreElements()) {

                DbObject dbo = dbEnumAll.nextElement();
                String conceptName = getConceptName(dbo); 

                boolean bIsSpecialized = true;
                String name = dbo.getName(); 
               
                if (0 == conceptName.compareTo("Stamp") || 0 == conceptName.compareTo("Image")
                        || 0 == conceptName.compareTo("Free Line")
                        || 0 == conceptName.compareTo("Free Text")
                        || 0 == conceptName.compareTo("Notice")
                        || 0 == conceptName.compareTo("Link")
                        || 0 == conceptName.compareTo("User Text")
                        || 0 == conceptName.compareTo("Free Form")) {
                    bIsSpecialized = false;
                }

                ConceptPair conceptPair = new ConceptPair(conceptName, 1, bIsSpecialized);

                int nSize = m_vConcepts.size();
                if (nSize == 0) { //initial case, add the only element
                    m_vConcepts.add(conceptPair);
                } else { //general case, check if exist, is so increment, otherwise add - there is no obvious sorting facility
                    boolean bFound = false;
                    for (int i = 0; i < nSize; i++) {
                        ConceptPair pair = (ConceptPair) m_vConcepts.elementAt(i);
                        if (0 == pair.getConceptName().compareTo(conceptPair.getConceptName())) {
                            ((ConceptPair) m_vConcepts.elementAt(i)).setOccurrencesCount(pair
                                    .getOccurrencesCount() + 1);
                            bFound = true;
                        }
                    }
                    if (bFound == false) {
                        m_vConcepts.add(conceptPair);
                    }
                }
            }

            dbEnumAll.close();

            diagramGO.getDb().commitTrans();

        } catch (DbException e) {
            throw new ExceptionMessage("Failed to extract concepts data. Operation aborted.",
                    ExceptionMessage.E_ERROR, ExceptionMessage.L_NORMAL);
        }
    }

    private String getConceptName(DbObject dbo) throws DbException {
    	String name = null;
    	
    	if (dbo instanceof DbSMSGraphicalObject) {
    		DbSMSGraphicalObject go = (DbSMSGraphicalObject)dbo;
    		DbObject so = go.getSO();
    		MetaClass mc = (so == null) ? go.getMetaClass() : so.getMetaClass();
    		name = mc.getGUIName();
    	} else {
    		MetaClass mc = DbObject.metaClass;
    		name = mc.getGUIName();
    	}
    	
    	name = name.replaceAll(" Graphical Object", "");

		return name;
	}

	/*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.plugins.Plugin#getSignature()
     */
    public PluginSignature getSignature() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.plugins.Plugin#getSupportedClasses()
     */
    public Class[] getSupportedClasses() {
        return new Class[] { DbSMSDiagram.class };
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.modelsphere.jack.plugins.Plugin#installAction(org.modelsphere.jack.srtool.DefaultMainFrame
     * , org.modelsphere.jack.srtool.MainFrameMenu)
     */
    public String installAction(DefaultMainFrame frame, MainFrameMenu menuManager) {

        return Plugin.MENU_DEBUG;
    }

    /**
     * @return Returns the defaultStyle.
     */
    public String getDefaultStyle() {
        return defaultStyle;
    }

    /**
     * @param defaultStyle
     *            The defaultStyle to set.
     */
    public void setDefaultStyle(String defaultStyle) {
        this.defaultStyle = defaultStyle;
    }

    /**
     * @return Returns the dimensions.
     */
    public Dimension getDimensions() {
        return dimensions;
    }

    /**
     * @param dimensions
     *            The dimensions to set.
     */
    public void setDimensions(Dimension dimensions) {
        this.dimensions = dimensions;
    }

    /**
     * @return Returns the notation.
     */
    public String getNotation() {
        return notation;
    }

    /**
     * @param notation
     *            The notation to set.
     */
    public void setNotation(String notation) {
        this.notation = notation;
    }

    /**
     * @return Returns the sheetsCount.
     */
    public int getSheetsCount() {
        return sheetsCount;
    }

    /**
     * @param sheetsCount
     *            The sheetsCount to set.
     */
    public void setSheetsCount(int sheetsCount) {
        this.sheetsCount = sheetsCount;
    }

    /**
     * @return Returns the type.
     */
    public int getType() {
        return type;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setType(int type) {
        this.type = type;
    }
    
    @Override
    public boolean doListenSelection() { return (this instanceof PluginSelectionListener); }

	@Override
	public OptionGroup getOptionGroup() {
		return null;
	}

	@Override
	public PluginAction getPluginAction() {
		return null;
	}
}
