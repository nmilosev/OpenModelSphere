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
package org.modelsphere.sms.or.db.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.db.DbSMSBuiltInTypePackage;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORBuiltInType;
import org.modelsphere.sms.or.db.DbORChoiceOrSpecialization;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORFKeyColumn;

import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.srtypes.ORChoiceSpecializationCategory;

public class ChoiceSpecKeyGenerator {
	boolean isNewSpecOrChoice = false;
	private List<DbORChoiceOrSpecialization> choiceSpecs;

	public boolean isSpecOrChoiceModified() {
		return this.isNewSpecOrChoice;
	}

	public void generate(DbORDataModel model) throws DbException {
		DbEnumeration enu = model.getComponents().elements(DbORChoiceOrSpecialization.metaClass); 
		choiceSpecs = new ArrayList<DbORChoiceOrSpecialization>();
		while (enu.hasMoreElements()) {
			DbORChoiceOrSpecialization choiceSpec = (DbORChoiceOrSpecialization)enu.nextElement(); 
			generateChoiceSpecKeys(model, choiceSpec);
		}
		enu.close(); 
	} //end generate()

	private void generateChoiceSpecKeys(DbORDataModel model, DbORChoiceOrSpecialization choiceSpec) throws DbException {
		//add column in parent
		DbORTable parent = choiceSpec.getParentTable(); 
		DbORAssociationEnd parentAssocEnd = findParentAssociationEnd(choiceSpec); 
		if (choiceSpec.isGenerateColumn()) {
			addChoiceSpecColumns(model, parent, choiceSpec, parentAssocEnd); 
		}
		
		/*
		//add FK for each child association..
		DbEnumeration enu = choiceSpec.getAssociationEnds().elements(DbORAssociationEnd.metaClass); 
		while (enu.hasMoreElements()) { 
			DbORAssociationEnd assocEnd = (DbORAssociationEnd)enu.nextElement(); 
			DbORAssociationEnd oppAssoc = assocEnd.getOppositeEnd(); 
			if (! parent.equals(oppAssoc.getClassifier())) {
				if (AnyORObject.isForeignAssocEnd(oppAssoc)) {
					generateChildFK(model, choiceSpec, parent, oppAssoc);
				} else {
					generateParentFK(model, choiceSpec, parent, parentAssocEnd, oppAssoc);
				}
			} //end if
		} //end while
		enu.close();
		*/
	} //end generateChoiceSpecKeys()

	private DbORAssociationEnd findParentAssociationEnd(
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
		
		return parentAssocEnd;
	}

	private void addChoiceSpecColumns(DbORDataModel model, DbORTable parent,
			DbORChoiceOrSpecialization choiceSpec, DbORAssociationEnd parentAssocEnd) throws DbException {
				
		if(createOrModifyChoiceSpecColumn(parent, choiceSpec)){
	    	DbORColumn col = getChoiceSpecColumn(parent, choiceSpec);     	   	
	    	if (col == null) {
	    		int nbColumns = findNbChoiceSpecColumns(choiceSpec);
	    		if (nbColumns == 1) {
	    			String colName = choiceSpec.getName();
	    			DbORBuiltInType colType = getTextType(model);
	    			addChoiceSpecColumn(model, parent, choiceSpec, parentAssocEnd, colName, colType);
	    		} else {
	    			List<DbORAssociation> assocs = findAssociations(choiceSpec); 
	    			addChoiceSpecColumns(model, parent, choiceSpec, parentAssocEnd, assocs);
	    		} //end if
	    	} //end if	
	    	choiceSpecs.add(choiceSpec);
	    	this.isNewSpecOrChoice = true;
		}

	} //end addChoiceSpecColumn()

    public List<DbORChoiceOrSpecialization> getGeneratedChoiceSpecs() {
		return choiceSpecs;
	}

	private void addChoiceSpecColumns(DbORDataModel model, DbORTable parent,
			DbORChoiceOrSpecialization choiceSpec, DbORAssociationEnd parentAssocEnd, 
			List<DbORAssociation> assocs) throws DbException {
		for (DbORAssociation assoc : assocs) {
			DbORTable childTable = findChildTable(assoc, parent);
			if (childTable != null) {
				String colName = childTable.getName();
				DbORBuiltInType colType = getBooleanType(model);
			    addChoiceSpecColumn(model, parent, choiceSpec, parentAssocEnd, colName, colType);
			}
		} //end for
	} //end addChoiceSpecColumns()

	private DbORTable findChildTable(DbORAssociation assoc, DbORTable parent) throws DbException {
		DbORTable childTable;
		DbORAbsTable frontTable = assoc.getFrontEnd().getClassifier();
		DbORAbsTable backTable = assoc.getBackEnd().getClassifier();
		
		if (parent.equals(frontTable)) {
			childTable = (backTable instanceof DbORTable) ? (DbORTable)backTable : null;
		} else {
			childTable = (frontTable instanceof DbORTable) ? (DbORTable)frontTable : null;
		} //end if
		
		return childTable;
	}

	private void addChoiceSpecColumn(DbORDataModel model, DbORTable parent, 
    		DbORChoiceOrSpecialization choiceSpec, DbORAssociationEnd parentAssocEnd, String colName, DbORBuiltInType colType) throws DbException {
    	MetaClass[] metaClasses = AnyORObject.getTargetMetaClasses(model);
    	DbORColumn col = (DbORColumn)parent.createComponent(metaClasses[AnyORObject.I_COLUMN]); 
		col.setName(colName);
		col.setChoiceOrSpecialization(choiceSpec);
		col.setType(colType);
		
		int mult = parentAssocEnd.getOppositeEnd().getMultiplicity().getValue();
		boolean nullable = (mult == SMSMultiplicity.OPTIONAL) || (mult == SMSMultiplicity.MANY); 
		col.setNull(nullable); 
	}

	private int findNbChoiceSpecColumns(DbORChoiceOrSpecialization choiceSpec) throws DbException {
		SMSMultiplicity mult = choiceSpec.getMultiplicity();
    	int m = (mult == null) ? SMSMultiplicity.UNDEFINED : mult.getValue();
    	int nbChoiceSpecColumns;
    	
    	if ((m == SMSMultiplicity.OPTIONAL) || (m == SMSMultiplicity.EXACTLY_ONE)) {
    		nbChoiceSpecColumns = 1;
    	} else {
    		List<DbORAssociation> assocs = findAssociations(choiceSpec); 
    		nbChoiceSpecColumns = assocs.size()-1; 
    	}
    	
		return nbChoiceSpecColumns;
	}

	private List<DbORAssociation> findAssociations(DbORChoiceOrSpecialization choiceSpec) throws DbException {
		List<DbORAssociation> assocs = new ArrayList<DbORAssociation>();
		
		DbEnumeration enu = choiceSpec.getAssociations().elements(DbORAssociation.metaClass);
		while (enu.hasMoreElements()) {
			DbORAssociation assoc = (DbORAssociation)enu.nextElement();
			assocs.add(assoc); 
		} //end while
		enu.close();
		
		return assocs;
	}

	private static final String[] BOOLEAN_TYPES = new String[] {"BOOLEAN", "BOOL"};
	private DbORBuiltInType getBooleanType(DbORDataModel model) throws DbException {
		DbSMSTargetSystem ts = model.getTargetSystem();
		DbSMSBuiltInTypePackage pack = ts.getBuiltInTypePackage();
		DbORBuiltInType textType = null;
		
		for (String name : BOOLEAN_TYPES) {
			DbORBuiltInType type = (DbORBuiltInType)pack.findComponentByName(DbORBuiltInType.metaClass, name);
			if (type != null) {
				textType = type;
				break;
			}
		}
		
		return textType;
	}

	private static final String[] TEXT_TYPES = new String[] {"VARIABLE CHARACTER", "VARCHAR", "NVARCHAR", "TEXT"};
	private DbORBuiltInType getTextType(DbORDataModel model) throws DbException {
		DbSMSTargetSystem ts = model.getTargetSystem();
		DbSMSBuiltInTypePackage pack = ts.getBuiltInTypePackage();
		DbORBuiltInType textType = null;
		
		for (String name : TEXT_TYPES) {
			DbORBuiltInType type = (DbORBuiltInType)pack.findComponentByName(DbORBuiltInType.metaClass, name);
			if (type != null) {
				textType = type;
				break;
			}
		}
		
		return textType;
	}

	private boolean createOrModifyChoiceSpecColumn(DbORTable parent, DbORChoiceOrSpecialization choiceSpec) throws DbException {
		int nbCurrentColumns = 0;
		int nbColumnToGenerate = 0;
		boolean isToGenerate = false;
		
		DbEnumeration enumeration = parent.getComponents().elements(DbORColumn.metaClass); 
		while (enumeration.hasMoreElements()) {
			DbORColumn col = (DbORColumn)enumeration.nextElement(); 
			DbORChoiceOrSpecialization ref = col.getChoiceOrSpecialization(); 
			if (choiceSpec.equals(ref)){
				nbCurrentColumns++;
			}			
		} 
		enumeration.close(); 
		nbColumnToGenerate = findNbChoiceSpecColumns(choiceSpec);
		
		if(nbCurrentColumns != nbColumnToGenerate){
			isToGenerate = true;
			//Remove old columns if exist
			DbEnumeration enu = parent.getComponents().elements(DbORColumn.metaClass); 
			while (enu.hasMoreElements()) {
				DbORColumn col = (DbORColumn)enu.nextElement(); 
				DbORChoiceOrSpecialization ref = col.getChoiceOrSpecialization(); 
				if (choiceSpec.equals(ref)){
					col.remove();
				}
			} 
			enu.close(); 	
		}
		return isToGenerate;
	}
	
	private DbORColumn getChoiceSpecColumn(DbORTable parent,
			DbORChoiceOrSpecialization choiceSpec) throws DbException {
		DbORColumn choiceSpecColumn = null; 
		
		DbEnumeration enu = parent.getComponents().elements(DbORColumn.metaClass); 
		while (enu.hasMoreElements()) {
			DbORColumn col = (DbORColumn)enu.nextElement(); 
			DbORChoiceOrSpecialization ref = col.getChoiceOrSpecialization(); 
			if (choiceSpec.equals(ref)) {
				choiceSpecColumn = col;
				break;
			}
		} 
		enu.close(); 
		
		return choiceSpecColumn;
	}

/*	private int getChoiceSpecColumn(DbORTable parent,
			DbORChoiceOrSpecialization choiceSpec) throws DbException {
		DbORColumn choiceSpecColumn = null; 
		
		DbEnumeration enu = parent.getComponents().elements(DbORColumn.metaClass); 
		while (enu.hasMoreElements()) {
			DbORColumn col = (DbORColumn)enu.nextElement(); 
			DbORChoiceOrSpecialization ref = col.getChoiceOrSpecialization(); 
			if (choiceSpec.equals(ref)) {
				choiceSpecColumn = col;
				break;
			}
		} 
		enu.close(); 
		
		return choiceSpecColumn;
	}*/
    
	public int deleteIfChoiceSpecColumn(DbORColumn col) throws DbException {
        int nbDeletion = 0;
        DbORChoiceOrSpecialization ref = col.getChoiceOrSpecialization(); 
        if (ref != null) {
            col.remove();
            nbDeletion = 1;
        }
        return nbDeletion;
    }

	/*
	private void generateParentFK(DbORDataModel model,
			DbORChoiceOrSpecialization choiceSpec, DbORTable parentTable,
			DbORAssociationEnd parentAssocEnd, DbORAssociationEnd childAssocEnd) throws DbException {
		//get child's PK
		DbORTable childTable = (DbORTable)childAssocEnd.getClassifier();
		DbORPrimaryUnique tablePk = getPrimaryKey(childTable); 
    	if (tablePk == null) {
    		return;
    	}
    	
    	//get parent side's FK, create it if not found
    	DbORForeign foreignKey = parentAssocEnd.getMember();
    	if (foreignKey == null) {
    		foreignKey = createFK(model, parentAssocEnd); 
    	}
    	
    	//add foreign columns
    	DbEnumeration enu =  tablePk.getColumns().elements(DbORColumn.metaClass); 
    	while (enu.hasMoreElements()) {
    		DbORColumn sourceColumn = (DbORColumn)enu.nextElement(); 
    		DbORColumn fc = createColumn(parentTable, sourceColumn); 
    		fc.setNull(true);
    		new DbORFKeyColumn(foreignKey, fc, sourceColumn);
    	} //end while
    	enu.close(); 
	}*/
	
	/*
	private void generateChildFK(DbORDataModel model, DbORChoiceOrSpecialization choiceSpec, DbORTable parent, DbORAssociationEnd childAssocEnd) throws DbException {
		// get parent's PK
    	DbORPrimaryUnique tablePk = getPrimaryKey(parent); 
    	if (tablePk == null) {
    		return;
    	}
    	
    	//create foreign key on child side
    	DbORForeign foreignKey = createFK(model, childAssocEnd); 
    	
    	if (foreignKey != null) {
        	//create child table
        	DbORTable childTable = (DbORTable)childAssocEnd.getClassifier(); 
        	
        	//add dependency to specialization
        	ORChoiceSpecializationCategory categ = choiceSpec.getCategory(); 
        	if (categ.getValue() == ORChoiceSpecializationCategory.SPECIALIZATION) {
        		DbORPrimaryUnique childPk = getPrimaryKey(childTable); 
            	childAssocEnd.addToDependentConstraints(childPk);
        	}
        	
        	//is nullable
        	SMSMultiplicity mult = childAssocEnd.getMultiplicity();
        	boolean nullable = (mult.getValue() == SMSMultiplicity.OPTIONAL);
        	
        	//add foreign columns
        	DbEnumeration enu =  tablePk.getColumns().elements(DbORColumn.metaClass); 
        	while (enu.hasMoreElements()) {
        		DbORColumn sourceColumn = (DbORColumn)enu.nextElement(); 
        		DbORColumn foreignColumn = findForeignColumn(foreignKey, sourceColumn); 
        		
        		if (foreignColumn == null) {	
        			foreignColumn = createColumn(childTable, sourceColumn); 
        			foreignColumn.setNull(nullable);
        		}
        		
        		new DbORFKeyColumn(foreignKey, foreignColumn, sourceColumn);
        	} //end while
        	enu.close(); 
    	} 
	}*/

	/*
	private DbORColumn findForeignColumn(DbORForeign foreignKey,
			DbORColumn sourceColumn) throws DbException  {
		
		DbORColumn foundForeignColumn = null; 
		
		DbEnumeration enu = foreignKey.getComponents().elements(DbORFKeyColumn.metaClass); 
		while (enu.hasMoreElements()) {
			DbORFKeyColumn kc = (DbORFKeyColumn)enu.nextElement(); 
			DbORColumn col = kc.getSourceColumn();
			if (sourceColumn.equals(col)) {
				foundForeignColumn = kc.getColumn(); 
				break;
			}
		} //end while
    	enu.close();
    	
		return foundForeignColumn;
	}*/

	/*
	private DbORForeign createFK(DbORDataModel model, DbORAssociationEnd childAssoc) throws DbException  {
		MetaClass[] metaClasses = AnyORObject.getTargetMetaClasses(model);
    	DbORForeign foreignKey = null;
    	
    	try {
    		@SuppressWarnings("unchecked")
			Constructor<?> constructor = metaClasses[AnyORObject.I_FOREIGN].getJClass()
            	.getConstructor(new Class[] { DbORAssociationEnd.class });
    		 foreignKey = (DbORForeign) constructor
             	.newInstance(new Object[] { childAssoc });
    		
    	} catch (NoSuchMethodException ex) {
    		ExceptionHandler.throwRealException(ex);
    	} catch (InvocationTargetException ex) {
    		ExceptionHandler.throwRealException(ex);
    	} catch (IllegalAccessException ex) {
    		ExceptionHandler.throwRealException(ex);
    	} catch (InstantiationException ex) {
    		ExceptionHandler.throwRealException(ex);
    	} //end try
    	
		return foreignKey;
	}*/

	/*
	private DbORColumn createColumn(DbORTable childTable, DbORColumn sourceColumn) throws DbException {
		MetaClass mc = sourceColumn.getMetaClass(); 
		DbORColumn newCol = (DbORColumn)childTable.createComponent(mc); 
		DbORTable parentTable = (DbORTable)sourceColumn.getCompositeOfType(DbORTable.metaClass); 
		
		String newName = parentTable.getName() + " " + sourceColumn.getName(); 
		newCol.setName(newName); 
		newCol.setType(sourceColumn.getType());
		newCol.setLength(sourceColumn.getLength());
		newCol.setNbDecimal(sourceColumn.getNbDecimal());
		newCol.setInitialValue(sourceColumn.getInitialValue());
		
		return newCol;
	}*/

	/*
	private DbORPrimaryUnique getPrimaryKey(DbORTable parent) throws DbException {
		DbORPrimaryUnique pk = null;
		
		DbEnumeration enu = parent.getComponents().elements(DbORPrimaryUnique.metaClass); 
    	while (enu.hasMoreElements()) {
    		DbORPrimaryUnique key = (DbORPrimaryUnique)enu.nextElement(); 
    		if (key.isPrimary()) {
    			pk = key;
    			break;
    		}
    	} //end while
    	enu.close(); 
    	
		return pk;
	} //end getPrimaryKey()
	*/

}
