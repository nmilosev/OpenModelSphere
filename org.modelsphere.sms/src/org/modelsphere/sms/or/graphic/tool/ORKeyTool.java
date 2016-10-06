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

package org.modelsphere.sms.or.graphic.tool;

import java.awt.Image;
import java.awt.Toolkit;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.tool.KeyTool;
import org.modelsphere.jack.graphic.tool.Tool;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.SrLineEndZone;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.SMSToolkit;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORNotation;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORView;
import org.modelsphere.sms.or.db.srtypes.ORConnectivityPosition;
import org.modelsphere.sms.or.graphic.MultiplicityLabel;
import org.modelsphere.sms.or.graphic.ORAssociation;
import org.modelsphere.sms.or.graphic.ORTableBox;
import org.modelsphere.sms.or.international.LocaleMgr;

public class ORKeyTool extends KeyTool {

    public static final String kKeyCreationTool = LocaleMgr.screen.getString("KeyCreationTool");
    private static final String kKeyUpdateTrans = LocaleMgr.action.getString("updateKey");
    private static final String kKeyPrimaryCreationTrans = LocaleMgr.action
            .getString("CreatePrimaryKey");
    private static final String kKeyUniqueCreationTrans = LocaleMgr.action
            .getString("CreateUniqueKey");
    private static final String kKeyPrimaryKeyName = LocaleMgr.misc.getString("primaryUniqueKey");
    private static final String kKeyUniqueKeyName = LocaleMgr.misc.getString("uniqueKey");
    private static final String kPKToolTips = LocaleMgr.screen.getString("PKToolTip");
    private static final String kUK1ToolTips = LocaleMgr.screen.getString("UK1ToolTip");
    private static final String kUK2ToolTips = LocaleMgr.screen.getString("UK2ToolTip");
    private static final String kUK3ToolTips = LocaleMgr.screen.getString("UK3ToolTip");
    private static final String kUK4ToolTips = LocaleMgr.screen.getString("UK4ToolTip");
    private static final String kUK5ToolTips = LocaleMgr.screen.getString("UK5ToolTip");
    public static final Image kImageKeyTool = GraphicUtil.loadImage(Tool.class, "resources/pk.gif"); // NOT LOCALIZABLE - tool image
    public static final Image kImageKey2Tool = GraphicUtil.loadImage(Tool.class,
            "resources/pk2.gif"); // NOT LOCALIZABLE - tool image
    public static final Image kImageUK1Tool = GraphicUtil
            .loadImage(Tool.class, "resources/uk1.gif"); // NOT LOCALIZABLE - tool image
    public static final Image kImageUK2Tool = GraphicUtil
            .loadImage(Tool.class, "resources/uk2.gif"); // NOT LOCALIZABLE - tool image
    public static final Image kImageUK3Tool = GraphicUtil
            .loadImage(Tool.class, "resources/uk3.gif"); // NOT LOCALIZABLE - tool image
    public static final Image kImageUK4Tool = GraphicUtil
            .loadImage(Tool.class, "resources/uk4.gif"); // NOT LOCALIZABLE - tool image
    public static final Image kImageUK5Tool = GraphicUtil
            .loadImage(Tool.class, "resources/uk5.gif"); // NOT LOCALIZABLE - tool image

    public ORKeyTool(String text, Image image) {
        super(0, text, new String[] { kPKToolTips, kUK1ToolTips, kUK2ToolTips, kUK3ToolTips,
                kUK4ToolTips, kUK5ToolTips }, image, new Image[] { kImageKeyTool, kImageUK1Tool,
                kImageUK2Tool, kImageUK3Tool, kImageUK4Tool, kImageUK5Tool });
        setVisible(ScreenPerspective.isFullVersion()); 
    }

    protected void createKey(GraphicComponent gc, Object semobj) {
        ORTableBox tablebox = null;
        if (gc instanceof ORTableBox)
            tablebox = (ORTableBox) gc;
        if ((tablebox != null) && (semobj instanceof DbORColumn)) {
            if (tablebox.getSemanticalObject() instanceof DbORAbsTable) { // it can be a view
                DbORColumn column = (DbORColumn) semobj;
                try {
                    DbSMSDiagram diagGO = (DbSMSDiagram) ((ApplicationDiagram) model)
                            .getDiagramGO();
                    diagGO.getDb().beginTrans(Db.WRITE_TRANS, kKeyUpdateTrans);

                    DbORAbsTable table = (DbORAbsTable) column.getComposite();
                    if (table instanceof DbORTable) {
                        DbORTable dbORtable = (DbORTable) table;
                        if (dbORtable.isIsAssociation() == true) {
                            diagGO.getDb().commitTrans();
                            Toolkit.getDefaultToolkit().beep();
                            return;
                        }
                    }
                    DbORPrimaryUnique columnkey = tablebox.getUniqueConstraintAt(auxiliaryIndex);
                    if (columnkey == null) { // create the key
                        columnkey = (DbORPrimaryUnique) SMSToolkit.getToolkit(diagGO)
                                .createDbObject(DbORPrimaryUnique.metaClass, table, null);
                        if (columnkey != null) { // if null, the toolkit does not support DbORPrimaryUnique
                            if (auxiliaryIndex == 0) { // primary tool
                                columnkey.setPrimary(Boolean.TRUE);
                                ///columnkey.setName(kKeyPrimaryKeyName);      // replace default key name
                                diagGO.getDb().setTransName(kKeyPrimaryCreationTrans);
                            } else {
                                columnkey.setPrimary(Boolean.FALSE);
                                //columnkey.setName(kKeyUniqueKeyName);      // replace default key name
                                diagGO.getDb().setTransName(kKeyUniqueCreationTrans);
                            }
                        }
                    }

                    if (columnkey != null) { // if null, the toolkit does not support DbORPrimaryUnique
                        if (columnkey.getColumns().indexOf(column) == -1) { // link column
                            columnkey.setColumns(column, Db.ADD_TO_RELN);
                            column.setNull(Boolean.FALSE);
                        } else
                            // unlink column
                            columnkey.setColumns(column, Db.REMOVE_FROM_RELN);
                    }

                    diagGO.getDb().commitTrans();
                } catch (Exception ex) {
                    org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                            ApplicationContext.getDefaultMainFrame(), ex);
                }
            } else
                Toolkit.getDefaultToolkit().beep();
        } else
            Toolkit.getDefaultToolkit().beep();
    }
    
    protected void createDependency(GraphicComponent gc) {
		DbORDiagram diagGO = (DbORDiagram) ((ApplicationDiagram) model).getDiagramGO();
		
		DbORNotation notation;
		ORConnectivityPosition numPos = null;
		ORConnectivityPosition symbPos= null;
		
		try {
			diagGO.getDb().beginTrans(Db.WRITE_TRANS, kKeyUpdateTrans);
			DbSMSProject prj = (DbSMSProject) diagGO.getCompositeOfType(DbSMSProject.metaClass);
			notation = diagGO.getNotation();
			if (notation == null)
				notation = prj.getOrDefaultNotation();
			
    		numPos = notation.getNumericPosition();
    		symbPos = notation.getSymbolicPosition();
    		diagGO.getDb().commitTrans();
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	if (gc instanceof MultiplicityLabel) {
    		//user clicks multiplicity label on Datarun notation
    		MultiplicityLabel multilabel = (MultiplicityLabel)gc;
    		DbObject dbo = multilabel.getSemanticalObject();
            
    		if (dbo instanceof DbORAssociationEnd) {
    			ORAssociation association = (ORAssociation) multilabel.getLine();
    			createDependencyOnAssociatedEnd(association, (DbORAssociationEnd)dbo, numPos);
    		}
    	} else if (gc instanceof SrLineEndZone) {
    		//user clicks multiplicity label on IE+ notation
  		
    		SrLineEndZone end = (SrLineEndZone)gc;
    		DbObject dbo = end.getSemanticalObject();
    		
    		if (dbo instanceof DbORAssociationEnd) {
    			ORAssociation association = (ORAssociation) end.getLine();
    			createDependencyOnAssociatedEnd(association, (DbORAssociationEnd)dbo, symbPos);
    		}
    	} else {
            Toolkit.getDefaultToolkit().beep();
    	} //end if
    } //end createDependency()

    private void createDependencyOnAssociatedEnd(ORAssociation association, DbORAssociationEnd end, ORConnectivityPosition position) {
 
        ORTableBox tablebox = null;
        try {
            end.getDb().beginTrans(Db.WRITE_TRANS, kKeyUpdateTrans);
            DbORAbsTable classifier = (DbORAbsTable) end.getClassifier();
            DbORAssociationEnd oppositeEnd = end.getOppositeEnd();
            DbORAssociationEnd ORModeAppropriateEnd = (position.getValue() == ORConnectivityPosition.FAR_AWAY)? oppositeEnd : end;
  
            if (classifier instanceof DbORTable) {
                boolean isERMode = ((DbORTable) classifier).isIsAssociation();//Obligatoirement en ER Mode
                if (isERMode)
                    tablebox = end.isFrontEnd() ? (ORTableBox) association.getNode2()
                            : (ORTableBox) association.getNode1();
                else
                    tablebox = ORModeAppropriateEnd.isFrontEnd() ? (ORTableBox) association.getNode1()
                        		: (ORTableBox) association.getNode2();
                	
                DbORPrimaryUnique toolKey = tablebox.getUniqueConstraintAt(auxiliaryIndex);
                if (toolKey == null) {
                    toolKey = (DbORPrimaryUnique) SMSToolkit.getToolkit(
                            SMSToolkit.APPLICATION_DIAGRAM_MASK).createDbObject(
                            DbORPrimaryUnique.metaClass, tablebox.getSemanticalObject(), null);
                    if (auxiliaryIndex == 0) { // pk
                        toolKey.setPrimary(Boolean.TRUE);
                        //toolKey.setName(kKeyPrimaryKeyName);      // replace default key name
                        toolKey.getDb().setTransName(kKeyPrimaryCreationTrans);
                    } else {
                        toolKey.setPrimary(Boolean.FALSE);
                        //toolKey.setName(kKeyUniqueKeyName);      // replace default key name
                        toolKey.getDb().setTransName(kKeyUniqueCreationTrans);
                    }
                }
                
                if (ORModeAppropriateEnd.getDependentConstraints().indexOf(toolKey) == -1)
                	ORModeAppropriateEnd.addToDependentConstraints(toolKey);
                else
                	ORModeAppropriateEnd.removeFromDependentConstraints(toolKey);
            }
            end.getDb().commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                    ApplicationContext.getDefaultMainFrame(), ex);
        }
    }

}
