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
package org.modelsphere.sms.or.graphic;

import java.awt.Color;
import java.awt.Rectangle;

import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbRefreshPassListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.db.event.DbUpdateListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdatePassListener;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.shape.DiamondShape;
import org.modelsphere.jack.graphic.shape.GraphicShape;
import org.modelsphere.jack.graphic.shape.TriangleShape;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSStyle;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.db.srtypes.SMSNotationShape;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORChoiceOrSpecialization;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORNotation;
import org.modelsphere.sms.or.db.DbORStyle;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTableGo;
import org.modelsphere.sms.or.db.srtypes.ORChoiceSpecializationCategory;

public class ORChoiceSpecialization extends GraphicNode implements ActionInformation {
	private DbORTableGo _tableGo;
	private DbORChoiceOrSpecialization _choiceSpec;
	
	 static {
		 ChoiceSpecializationUpdater updater = new ChoiceSpecializationUpdater();
		 Db.addDbUpdatePassListener(updater); 
		 MetaField.addDbUpdateListener(updater, 0, new MetaField[] {
			 DbORChoiceOrSpecialization.fMultiplicity,
	         DbORChoiceOrSpecialization.fSpecificRangeMultiplicity,
	         DbORAssociationEnd.fMultiplicity,
             DbORAssociationEnd.fSpecificRangeMultiplicity,
		 }); 
		
		 ChoiceSpecializationRefresher refresher = new ChoiceSpecializationRefresher();
		 Db.addDbRefreshPassListener(refresher);
		 
		 MetaField.addDbRefreshListener(refresher, null, new MetaField[] {
                DbORChoiceOrSpecialization.fCategory, DbORTableGo.fBackgroundColor,
                DbORTableGo.fLineColor });
	}
	
	//
	// Factory methods and constructors
	//
	
	public static ORChoiceSpecialization createChoice(Diagram diagram, DbORTableGo go) throws DbException {
        GraphicShape shape = getNotationShape(go, ORChoiceSpecializationCategory.CHOICE);
        ORChoiceSpecialization choice = new ORChoiceSpecialization(diagram, go, shape);
		return choice;
	}
	
    public static ORChoiceSpecialization createSpecialization(Diagram diagram, DbORTableGo go)
            throws DbException {
        GraphicShape shape = getNotationShape(go, ORChoiceSpecializationCategory.SPECIALIZATION);
        ORChoiceSpecialization choice = new ORChoiceSpecialization(diagram, go, shape);
        return choice;
    }

    private static GraphicShape getNotationShape(DbORTableGo go, int category) throws DbException {
        DbORDiagram orDiag = (DbORDiagram) go.getCompositeOfType(DbORDiagram.metaClass);
        DbORNotation notation = orDiag.getNotation();
        SMSNotationShape notationShape;

        if (notation == null) {
            DbSMSProject prj = (DbSMSProject) orDiag.getCompositeOfType(DbSMSProject.metaClass);
            notation = prj.getOrDefaultNotation();
        }

        if (category == ORChoiceSpecializationCategory.CHOICE) {
            notationShape = notation.getChoiceShape();
        } else {
            notationShape = notation.getSpecializationShape();
        }

        if (notationShape == null) {
            if (category == ORChoiceSpecializationCategory.CHOICE) {
                notationShape = SMSNotationShape.getInstance(SMSNotationShape.DIAMOND);
            } else {
                notationShape = SMSNotationShape.getInstance(SMSNotationShape.TRIANGLE);
            }
        } //end if

        GraphicShape shape = SMSNotationShape.getShape(notationShape.getValue());
        return shape;
    }

	private ORChoiceSpecialization(Diagram diagram, DbORTableGo go, GraphicShape shape) throws DbException {
		super(diagram, shape);
		_tableGo = go;
		_choiceSpec = (DbORChoiceOrSpecialization)go.getClassifier();
		//setAutoFit(go.isAutoFit());
		setAutoFit(false); 
		
        objectInit();
	}
	
	//
	// Public methods
	//
	
	@Override
	public DbObject getSemanticalObject() {
		return _choiceSpec;
	}

	@Override
	public DbObject getGraphicalObject() {
		return _tableGo;
	}
	
	@Override
	public Db getDb() {
		Db db = _choiceSpec.getDb();
		return db;
	}
	
	protected void objectInit() throws DbException {
		_tableGo.setGraphicPeer(this);
        //_tableGo.addDbRefreshListener(listener)
        initCellRenderingOptions();
		
		Rectangle rect = _tableGo.getRectangle();
		rect.setSize(40, 20);
		//_tableGo.setRectangle(rect);
        setRectangle(rect);
        
		//_tableGo.addDbRefreshListener(tableGoRefreshTg);
	    //initCellRenderingOptions();
	    //setZones();

		
		//SingletonZone zone = new SingletonZone("OR");
		//zone.setHasBottomLine(false); 
		//Dimension Dimension = box.getMinimumSize();
		
		//ZoneCell cell = zone.getValue();
		
		//cell.setDataWidth(100);
		//this.addZone(zone);
        initStyleElements();
	}
	

    private void initCellRenderingOptions() throws DbException {

    }

    private void initStyleElements() throws DbException {
        setBoxColor();
        setLineStyle();
    }

    protected void setBoxColor() throws DbException {
        //get BG color
        Color bgColor = (Color) _tableGo.find(DbSMSClassifierGo.fBackgroundColor);
        if (bgColor == null) {
            DbSMSStyle style = (DbORStyle) _tableGo.getStyle();

            if (style == null) {
                DbORDiagram diagram = (DbORDiagram) _tableGo
                        .getCompositeOfType(DbORDiagram.metaClass);
                style = diagram.getStyle();
            }

            bgColor = (Color) style.find(DbORStyle.fBackgroundColorDbORChoiceOrSpecialization);
        } //end if
        setFillColor(bgColor);

        //get line color
        Color lineColor = (Color) _tableGo.find(DbSMSClassifierGo.fLineColor);
        if (lineColor == null) {
            DbSMSStyle style = (DbORStyle) _tableGo.getStyle();

            if (style == null) {
                DbORDiagram diagram = (DbORDiagram) _tableGo
                        .getCompositeOfType(DbORDiagram.metaClass);
                style = diagram.getStyle();
            }

            lineColor = (Color) style.find(DbORStyle.fLineColorDbORChoiceOrSpecialization);
        } //end if
        setLineColor(lineColor);
    }

    protected void setLineStyle() throws DbException {
        Boolean highlightElem = (Boolean) _tableGo.find(DbSMSClassifierGo.fHighlight);
        Boolean dashElem = (Boolean) _tableGo.find(DbSMSClassifierGo.fDashStyle);

        boolean highlight = (highlightElem == null) ? false : highlightElem.booleanValue();
        boolean dash = (dashElem == null) ? false : dashElem.booleanValue();

        this.setLineStyle(highlight, dash);
    }
    
    // BEWARE: overriding triggers must call super.delete as last action
    public void delete(boolean all) {
    	_tableGo.setGraphicPeer(null);
    	
    	super.delete(all);
    }
    
    //
    // inner classes
    //
    private static class ChoiceSpecializationUpdater implements DbUpdateListener, DbUpdatePassListener {

		@Override
		public void beforeUpdatePass(Db db) throws DbException {
		}

		@Override
		public void afterUpdatePass(Db db) throws DbException {
		}

		@Override
		public void dbUpdated(DbUpdateEvent event) throws DbException {
			DbObject dbo = event.dbo; 
			
			if (dbo instanceof DbORAssociationEnd) {
				updateAssociationEnd((DbORAssociationEnd)dbo);
			} else if (dbo instanceof DbORChoiceOrSpecialization) {
				updateChoiceSpec((DbORChoiceOrSpecialization)dbo);
			} 
		} //end dbUpdated()

		private void updateAssociationEnd(DbORAssociationEnd end) throws DbException {
			DbORAbsTable classifier = end.getClassifier(); 
			if (classifier instanceof DbORChoiceOrSpecialization) {
				DbORChoiceOrSpecialization choiceSpec = (DbORChoiceOrSpecialization)classifier; 
				choiceSpec.setMultiplicity(end.getMultiplicity()); 
	    		choiceSpec.setSpecificRangeMultiplicity(end.getSpecificRangeMultiplicity());
			} 			
		} //end updateAssociationEnd()

		private void updateChoiceSpec(DbORChoiceOrSpecialization choiceSpec) throws DbException {
			DbORTable parentTable = choiceSpec.getParentTable();
			SMSMultiplicity mult = choiceSpec.getMultiplicity(); 
			String sm = choiceSpec.getSpecificRangeMultiplicity();
			
			DbEnumeration enu = choiceSpec.getAssociationEnds().elements(DbORAssociationEnd.metaClass); 
			while (enu.hasMoreElements()) {
				DbORAssociationEnd end = (DbORAssociationEnd)enu.nextElement();
				DbORAbsTable oppTable = end.getOppositeEnd().getClassifier();
				
				if (parentTable.equals(oppTable)) {
					end.setMultiplicity(mult); 
					end.setSpecificRangeMultiplicity(sm);
				}
			}
			enu.close();
		} //end updateChoiceSpec()
    	
    }
    
    private static class ChoiceSpecializationRefresher implements DbRefreshListener, DbRefreshPassListener {

        @Override
        public void beforeRefreshPass(Db db) throws DbException {  
        }

        @Override
        public void afterRefreshPass(Db db) throws DbException {  
        }

        @Override
        public void refreshAfterDbUpdate(DbUpdateEvent ev) throws DbException {
            refreshAfterDbUpdate(ev.dbo, ev.metaField);
        }

        private void refreshAfterDbUpdate(DbObject dbo, MetaField mf) throws DbException {
            if (dbo instanceof DbORChoiceOrSpecialization) {
                DbORChoiceOrSpecialization spec = (DbORChoiceOrSpecialization) dbo;
                if (mf == DbORChoiceOrSpecialization.fCategory) {
                    ORChoiceSpecializationCategory categ = (ORChoiceSpecializationCategory) spec
                            .get(mf);
                    refreshChoiceSpec(spec, categ);
                } //end if
            } else if (dbo instanceof DbORTableGo) {
                DbORTableGo specGo = (DbORTableGo) dbo;
                if (mf == DbORTableGo.fLineColor) {
                    refreshGo(specGo);
                } else if (mf == DbORTableGo.fBackgroundColor) {
                    refreshGo(specGo);
                } //end if
            } //end if
        } //end refreshAfterDbUpdate()

		private void refreshChoiceSpec(DbORChoiceOrSpecialization spec, ORChoiceSpecializationCategory categ) throws DbException {
            GraphicShape newShape = null;
            int value = (categ == null) ? 0 : categ.getValue();

            if (value == ORChoiceSpecializationCategory.SPECIALIZATION) {
                newShape = TriangleShape.singleton;
            } else if (value == ORChoiceSpecializationCategory.CHOICE) {
                newShape = DiamondShape.singleton;
            } //end if
            
            DbRelationN specGos = spec.getClassifierGos();
            int nb = specGos.size();
            for (int i = 0; i < nb; i++) {
                DbORTableGo specGo = (DbORTableGo) specGos.elementAt(i);
                GraphicNode node = (GraphicNode) specGo.getGraphicPeer();

                if (node instanceof ORChoiceSpecialization) {
                    ORChoiceSpecialization specNode = (ORChoiceSpecialization) node;
                    specNode.setShape(newShape);
                }
            }
        } //end refreshSpec()
        
        private void refreshGo(DbORTableGo specGo) throws DbException {
            GraphicNode node = (GraphicNode) specGo.getGraphicPeer();
            if (node instanceof ORChoiceSpecialization) {
                ORChoiceSpecialization specNode = (ORChoiceSpecialization) node;
                specNode.setBoxColor();
            }
        } //end refreshGo()

    } //end ChoiceSpecializationRefreshTg

} //end ORChoiceSpecialization
