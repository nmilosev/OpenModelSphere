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

package org.modelsphere.sms.or.db.util;

//Java
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.JOptionPane;

import org.modelsphere.jack.awt.TextViewerDialog;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.CollationComparator;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.jack.util.SrVector;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORAttribute;
import org.modelsphere.sms.or.db.DbORBuiltInType;
import org.modelsphere.sms.or.db.DbORChoiceOrSpecialization;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORConstraint;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORFKeyColumn;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.db.srtypes.ORChoiceSpecializationCategory;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.or.screen.DeleteKeysAndRulesFrame.DeleteKeysOptions;
import org.modelsphere.sms.plugins.TargetSystem;
import org.modelsphere.sms.plugins.TargetSystemInfo;
import org.modelsphere.sms.plugins.TargetSystemManager;

/**
 * 
 * Foreign keys management
 * 
 */
public final class ForeignKey {

    public static final int COPY_NAME = 0;
    public static final int COPY_PHYS_NAME = 1;
    public static final int COPY_ALIAS = 2;
    public static final int COPY_TYPE = 3;
    public static final int COPY_LENGTH = 4;
    public static final int COPY_NB_DEC = 5;
    public static final int COPY_COMMON_ITEM = 6;

    private static final MetaField[] copyFields = new MetaField[] { DbSemanticalObject.fName,
            DbSemanticalObject.fPhysicalName, DbSemanticalObject.fAlias, DbORAttribute.fType,
            DbORAttribute.fLength, DbORAttribute.fNbDecimal, DbORColumn.fCommonItem };

    public static int[] copyFlagsList = new int[] { 1 << COPY_NAME, 1 << COPY_PHYS_NAME,
            1 << COPY_ALIAS, 1 << COPY_TYPE | 1 << COPY_LENGTH | 1 << COPY_NB_DEC,
            1 << COPY_COMMON_ITEM, };

    public static final int CANCEL = 0;
    public static final int REPORTONLY = 1;
    public static final int GENERATE = 2;

    public static final int DELETE_ORPHAN = 0;
    public static final int KEEP_ORPHAN = 1;
    public static final int SET_ORPHAN_AS_BASIC = 2;

    private static final int I_DEL_COL = 0;
    private static final int I_ADD_COL = 1;
    private static final int I_MOD_COL = 2;
    private static final int I_DEL_KEY = 3;
    private static final int I_ADD_KEY = 4;
    private static final int I_MOD_KEY = 5;

    private static final String[] headers = new String[] {
            LocaleMgr.message.getString("deletedFCols"), LocaleMgr.message.getString("addedFCols"),
            LocaleMgr.message.getString("modifiedFCols"),
            LocaleMgr.message.getString("deletedFKeys"), LocaleMgr.message.getString("addedFKeys"),
            LocaleMgr.message.getString("modifiedPUFKeys") };

    private static final String kOrphanFCols = LocaleMgr.message.getString("orphanFCols");
    private static final String kModPUFKey = LocaleMgr.message.getString("modPUFKey");
    private static final String kGenFKeyChanges = LocaleMgr.message.getString("genFKeyChanges");
    private static final String kGenFKeyNoChanges = LocaleMgr.message.getString("genFKeyNoChanges");
    
    private static final String kGenSpecChoiceOnlyChanges = LocaleMgr.message.getString("genSpecChoiceOnlyChanges");
    private static final String kGenSpecChoiceTitle = LocaleMgr.message.getString("genSpecChoiceTitle");
    
    private static final String kGenSpecChoiceforTableMsgLine = LocaleMgr.message.getString("genSpecChoiceforTableMsgLine");
    
    private static final String kGenSpecLabel = LocaleMgr.message.getString("genSpecLabel");
    private static final String kGenChoiceLabel = LocaleMgr.message.getString("genChoiceLabel");
   // private static final String kGenFKeyNoChangesButSwitchColumns = LocaleMgr.message.getString("fKeyNoChangesButSwitchColumns");
    

    private DbORDataModel model;
    private int copyFlags;
    private int orphanAction;
    private boolean reorderPuCols;
    private MetaClass[] metaClasses;
    private List<DbORAssociationEnd> assocEnds; // ordered list of assocEnds to be generated
    private HashSet<DbORPrimaryUnique> puKeySet; // primary/unique keys with dependencies processed
    private SrVector[] changeLists;

    // Generate the foreign keys on a complete model.
    // The caller must not start a transaction for this entry point.
    public final void generate(DbORDataModel model, String actionName, int copyFlags,
            int orphanAction, boolean reorderPuCols, boolean reportOnly) throws DbException {
    	
        this.model = model;
        this.copyFlags = copyFlags;
        this.orphanAction = orphanAction;
        this.reorderPuCols = reorderPuCols;
        Db db = model.getDb();
        db.beginTrans(Db.WRITE_TRANS, actionName);
        metaClasses = AnyORObject.getTargetMetaClasses(model);
        changeLists = new SrVector[headers.length];
        buildAssocEndList();
        puKeySet = new HashSet<DbORPrimaryUnique>();
       
        
        for (int i = 0; i < assocEnds.size(); i++) {
            DbORAssociationEnd end = (DbORAssociationEnd) assocEnds.get(i);
            DbORAbsTable classifier = end.getClassifier();

            if (classifier instanceof DbORTable) {
                genForeign(end);
            }
        } //end for
        
        //Generate choice/spec columns
        ChoiceSpecKeyGenerator gen = new ChoiceSpecKeyGenerator();
        gen.generate(model); 
 
        cleanup();
        StringBuffer report = buildReport(gen);

        if (reportOnly)
            db.abortTrans();
        else
            db.commitTrans();

        if (report == null){
        	/*if (gen.isSpecOrChoiceModified())
        		JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(), kGenFKeyNoChangesButSwitchColumns);        		
        	else*/
        	JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(), kGenFKeyNoChanges);
        }
        else
            TextViewerDialog.showTextDialog(ApplicationContext.getDefaultMainFrame(), actionName, report.toString());
    }

    // Build a list of the associationEnds for which a foreign key can be generated.
    // The list is ordered so that dependent constraints come after the constraints
    // on which they depend.
    private void buildAssocEndList() throws DbException {
        assocEnds = new ArrayList<DbORAssociationEnd>();
        HashSet<DbORAssociationEnd> assocEndSet = new HashSet<DbORAssociationEnd>(); // visited assocEnds
        DbEnumeration dbEnum = model.getComponents().elements(DbORAssociation.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORAssociation assoc = (DbORAssociation) dbEnum.nextElement();
            buildAssocEndListAux(assocEndSet, new ArrayList<DbORAssociationEnd>(), assoc.getFrontEnd());
            buildAssocEndListAux(assocEndSet, new ArrayList<DbORAssociationEnd>(), assoc.getBackEnd());
        }
        dbEnum.close();
    }

    // Recursive method.
    private void buildAssocEndListAux(HashSet<DbORAssociationEnd> assocEndSet, List<DbORAssociationEnd> dependStack,
            DbORAssociationEnd assocEnd) throws DbException {
        if (!assocEndSet.add(assocEnd)) // add to the set of visited assocEnds; if already visited, return.
            return;

        if (!canBeGenerated(assocEnd)) { // if not generable, remove the foreign key if any.
            DbORForeign foreignKey = assocEnd.getMember();
            if (foreignKey != null) {
                addToChangeList(I_DEL_KEY, foreignKey, null);
                foreignKey.remove();
            }
            return;
        }

        // If there are dependencies, process the source constraints before this one.
        DbORPrimaryUnique sourceKey = assocEnd.getReferencedConstraint();
        if (sourceKey.getNbNeighbors(DbORPrimaryUnique.fAssociationDependencies) != 0) {
            dependStack.add(assocEnd); // dependency stack is used to detect dependency circularity.
            DbEnumeration dbEnum = sourceKey.getAssociationDependencies().elements();
            while (dbEnum.hasMoreElements()) {
                DbORAssociationEnd sourceAssocEnd = (DbORAssociationEnd) dbEnum.nextElement();
                checkCircularity(dependStack, sourceAssocEnd);
                buildAssocEndListAux(assocEndSet, dependStack, sourceAssocEnd);
            }
            dbEnum.close();
            dependStack.remove(dependStack.size() - 1);
        }

        assocEnds.add(assocEnd); // add to the list after adding the sources on which it depends.
    }

    public static boolean canBeGenerated(DbORAssociationEnd assocEnd) throws DbException {
        if (!AnyORObject.isForeignAssocEnd(assocEnd))
            return false;

        // If source key not yet assigned, assign the primary key as source key.
        DbORPrimaryUnique sourceKey = assocEnd.getReferencedConstraint();
        if (sourceKey == null) {
            sourceKey = AnyORObject.getPrimaryKey(assocEnd.getOppositeEnd().getClassifier());
            if (sourceKey == null)
                return false;
            assocEnd.setReferencedConstraint(sourceKey);
        }
        return true;
    }

    private void checkCircularity(List<DbORAssociationEnd> dependStack, DbORAssociationEnd assocEnd)
            throws DbException {
        int i = dependStack.indexOf(assocEnd);
        if (i != -1) {
            String text = "";
            for (; i < dependStack.size(); i++) {
                if (text.length() != 0)
                    text = text + ", ";
                text = text + ((DbORAssociationEnd) dependStack.get(i)).getName();
            }
            throw new DbException(assocEnd.getDb(), MessageFormat.format(LocaleMgr.message
                    .getString("dependCircularity"), new Object[] { text }));
        }
    }

    // Synchronize a foreign key with its source key
    private void genForeign(DbORAssociationEnd assocEnd) throws DbException {
        boolean is_null = (assocEnd.getMultiplicity().getValue() == SMSMultiplicity.OPTIONAL);
        String colsStr = null;
        boolean pk_valid= false;
        DbORColumn sourceCol = null;
        DbORForeign foreignKey = assocEnd.getMember();
        DbORPrimaryUnique sourceKey = assocEnd.getReferencedConstraint();
        // Call now for the case where we have a primary key without column but generated by dependency 
        genDependentCols(sourceKey);
        
        DbRelationN sourceCols = sourceKey.getColumns();
        pk_valid = (sourceCols.size()> 0)? true : false;       	
        if (pk_valid)
        	sourceCol = (DbORColumn) sourceCols.elementAt(0);
	        
        if (foreignKey == null) {
        	if (pk_valid)
        		foreignKey = addForeignKey(assocEnd, sourceCol);
        } else {
            colsStr = getColsString(foreignKey);
        }
        
        // First of all, synchronize the source key with the foreign keys on which it depends
        //genDependentCols(sourceKey);

        // Foreign key synchro: if source column has a corresponding foreign column, give the foreign column the same order
        // as the source column;  otherwise, create a foreign column for the source (with the same order as the source)
        int i;
        for (i = 0; i < sourceCols.size(); i++) {
            sourceCol = (DbORColumn) sourceCols.elementAt(i);
            int i2 = findInForeignKey(foreignKey, sourceCol, i);
            if (i2 != -1)
                foreignKey.reinsert(DbObject.fComponents, i2, i);
            else
                addForeignCol(foreignKey, sourceCol, i);

            DbORColumn foreignCol = ((DbORFKeyColumn) foreignKey.getComponents().elementAt(i))
                    .getColumn();
            foreignCol.setNull(is_null ? Boolean.TRUE : Boolean.FALSE);
            if (i2 != -1) {
                int modFlags = copyAttributes(sourceCol, foreignCol, copyFlags);
                if (modFlags != 0)
                    addToChangeList(I_MOD_COL, foreignCol, getAttrLabel(modFlags));
            }
        }
        // Disconnect from the foreign key all the following foreign columns (they have no source)
        if (foreignKey != null){
            DbRelationN foreignCols = foreignKey.getComponents();
            while (i < foreignCols.size())
                foreignCols.elementAt(i).remove();

            if (colsStr != null) {
                String newColsStr = getColsString(foreignKey);
                if (!colsStr.equals(newColsStr))
                    addToChangeList(I_MOD_KEY, foreignKey, MessageFormat.format(kModPUFKey,
                            new Object[] { colsStr, newColsStr }));
            }       	
        }
    }

    private int findInForeignKey(DbORForeign foreignKey, DbORColumn sourceCol, int i)
            throws DbException {
        DbRelationN foreignCols = foreignKey.getComponents();
        for (; i < foreignCols.size(); i++) {
            if (sourceCol == ((DbORFKeyColumn) foreignCols.elementAt(i)).getSourceColumn())
                return i;
        }
        return -1;
    }

    private String foreignKeyNameComputation(DbORAssociationEnd assocEnd) throws DbException {
        String term = DbORForeign.metaClass.getGUIName();
        try {
            term = TerminologyUtil.getInstance().findModelTerminology(assocEnd.getComposite())
                    .getTerm(DbORForeign.metaClass);
        } catch (Exception ex) {

        }
        return term;
    }

    private DbORForeign addForeignKey(DbORAssociationEnd assocEnd, DbORColumn sourceCol) throws DbException {
        try {
        	@SuppressWarnings("unchecked")
            Constructor<?> constructor = metaClasses[AnyORObject.I_FOREIGN].getJClass()
                    .getConstructor(new Class[] { DbORAssociationEnd.class });
            DbORForeign foreignKey = (DbORForeign) constructor
                    .newInstance(new Object[] { assocEnd });
            
            //get naming pattern
            PropertiesSet applOptions = PropertiesManager.APPLICATION_PROPERTIES_SET;
            String fkDefaultName = foreignKeyNameComputation(assocEnd);
    		String fkPattern = applOptions.getPropertyString(ForeignKeyNamingPanel.class, 
    			ForeignKeyNamingPanel.FOREIGN_KEY_NAME_PATTERN, 
    			fkDefaultName); 
    		
    		//set logical name
    		DbORTable parent = (DbORTable) sourceCol.getCompositeOfType(DbORTable.metaClass);
            String sourceName = sourceCol.getName();
            String parentName = parent.getName();
            String assocName = foreignKey.getAssociationEnd().getName();

            if ((sourceName != null) && (parentName != null) && (assocName != null)) {
            	String foreignKeyName = MessageFormat.format(fkPattern, new Object[] {parentName, sourceName, assocName});
            	foreignKey.setName(foreignKeyName);
            }
            
            addToChangeList(I_ADD_KEY, foreignKey, null);
            return foreignKey;
        } catch (Exception e) {
            ExceptionHandler.throwRealException(e);
            return null; // never executed
        }
    }

    private DbORFKeyColumn addForeignCol(DbORForeign foreignKey, DbORColumn sourceCol, int i)
            throws DbException {
        DbORColumn foreignCol = (DbORColumn) foreignKey.getComposite().createComponent(
                metaClasses[AnyORObject.I_COLUMN]);
        copyAttributes(sourceCol, foreignCol, 0xffffffff);
        setNameOfForeignColumn(foreignKey, sourceCol, foreignCol);
        foreignCol.setForeign(Boolean.TRUE);
        addToChangeList(I_ADD_COL, foreignCol, null);
        DbORFKeyColumn fKeyCol = new DbORFKeyColumn(foreignKey, foreignCol, sourceCol);
        foreignKey.reinsert(DbObject.fComponents, -1, i);
        return fKeyCol;
    }

    //If foreign table is "Employee", and its PK is "id", 
    //then foreign column should be named "Employee id", not just "id".
    //[Marco Savard]
    private void setNameOfForeignColumn(DbORForeign foreignKey, DbORColumn sourceCol, DbORColumn foreignCol)
            throws DbException {
        DbORTable parent = (DbORTable) sourceCol.getCompositeOfType(DbORTable.metaClass);
        
        //get naming pattern
        PropertiesSet applOptions = PropertiesManager.APPLICATION_PROPERTIES_SET;
		String fcPattern = applOptions.getPropertyString(ForeignKeyNamingPanel.class, 
			ForeignKeyNamingPanel.FOREIGN_COLUMN_NAME_PATTERN, 
			ForeignKeyNamingPanel.TABLE_COLUMN); 
		
        //set logical name
        String sourceName = sourceCol.getName();
        String parentName = parent.getName();
        String assocName = foreignKey.getAssociationEnd().getName();

        if ((sourceName != null) && (parentName != null) && (assocName != null)) {
        	String foreignColumnName = MessageFormat.format(fcPattern, new Object[] {parentName, sourceName, assocName});
            foreignCol.setName(foreignColumnName);
        }

        //set physical name
        sourceName = sourceCol.getPhysicalName();
        parentName = parent.getPhysicalName();
        assocName = foreignKey.getAssociationEnd().getPhysicalName();
        
        if ((sourceName != null) && (parentName != null) && (assocName != null)) {
            String foreignColumnName = MessageFormat.format(fcPattern, new Object[] {parentName, sourceName, assocName});
            foreignColumnName = foreignColumnName.replace(' ', '_'); 
            foreignCol.setPhysicalName(foreignColumnName);
        }
    } //end setNameOfForeignColumn()

    // Synchronize the composition of a primary/unique key according to the foreign keys on which it depends.
    // Order of the key columns: the foreign columns first (in the order of dependencies), then the normal columns.
    private void genDependentCols(DbORPrimaryUnique puKey) throws DbException {
        if (!puKeySet.add(puKey))
            return; // primary key already synchronized

        String colsStr = getColsString(puKey);
        int iCol = 0;
        SrVector dependColList = new SrVector();
        DbRelationN dependAssocEnds = puKey.getAssociationDependencies();
        for (int i = 0; i < dependAssocEnds.size(); i++) {
            DbORForeign dependKey = (DbORForeign) ((DbORAssociationEnd) dependAssocEnds
                    .elementAt(i)).getMember();
            if (dependKey == null)
                continue;
            DbRelationN dependCols = dependKey.getComponents();
            for (int j = 0; j < dependCols.size(); j++) {
                DbORColumn dependCol = ((DbORFKeyColumn) dependCols.elementAt(j)).getColumn();
                int i2 = puKey.getColumns().indexOf(dependCol);
                if (i2 == -1) { // if not in the list, insert it in the list at the current position
                    puKey.addToColumns(dependCol);
                    puKey.reinsert(DbORPrimaryUnique.fColumns, -1, iCol);
                    dependColList.add(dependCol);
                    iCol++;
                }
                // If in the list, check if it is a merged column present in the previous dependency;
                // if not, reinsert at the current position
                else if (!dependColList.contains(dependCol)) {
                    if (reorderPuCols)
                        puKey.reinsert(DbORPrimaryUnique.fColumns, i2, iCol);
                    else
                        iCol = i2;
                    dependColList.add(dependCol);
                    iCol++;
                }
            }
        }
        // Remove from the list the foreign columns that do not belong anymore to a dependency
        DbRelationN puCols = puKey.getColumns();
        for (iCol = puCols.size(); --iCol >= 0;) {
            DbORColumn puCol = (DbORColumn) puCols.elementAt(iCol);
            if (puCol.getFKeyColumns().size() != 0 ? (!dependColList.contains(puCol)) : (puCol
                    .isForeign() && orphanAction == DELETE_ORPHAN))
                puKey.removeFromColumns(puCol);
        }

        String newColsStr = getColsString(puKey);
        if (!colsStr.equals(newColsStr))
            addToChangeList(I_MOD_KEY, puKey, MessageFormat.format(kModPUFKey, new Object[] {
                    colsStr, newColsStr }));
    }

    private void cleanup() throws DbException {
        DbEnumeration dbEnum = model.getComponents().elements(DbORAbsTable.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbEnumeration enum2 = dbEnum.nextElement().getComponents().elements();
            while (enum2.hasMoreElements()) {
                DbObject dbo = enum2.nextElement();
                if (dbo instanceof DbORPrimaryUnique)
                    genDependentCols((DbORPrimaryUnique) dbo);
                else if (dbo instanceof DbORColumn) {
                    DbORColumn column = (DbORColumn) dbo;
                    if (column.isForeign() && column.getFKeyColumns().size() == 0) {
                        addToChangeList(I_DEL_COL, column, null);
                        if (orphanAction == DELETE_ORPHAN) {
                            // If obsolete column still in prim/unique keys, it means these keys have not yet been processed;
                            // process them before removing the column, in order to have the correct old composition in the report.
                            DbRelationN puKeys = column.getPrimaryUniques();
                            while (puKeys.size() != 0)
                                genDependentCols((DbORPrimaryUnique) puKeys.elementAt(0));
                            column.remove();
                        } else if (orphanAction == SET_ORPHAN_AS_BASIC)
                            column.setForeign(Boolean.FALSE);
                    }
                }
            }
            enum2.close();
        }
        dbEnum.close();
    }

    private void addToChangeList(int iList, DbObject dbo, String detail) throws DbException {
        String line = dbo.getComposite().getName() + '.' + dbo.getName(); //NOT LOCALIZABLE
        if (detail != null)
            line = line + ": " + detail; //NOT LOCALIZABLE
        line = line + '\n'; //NOT LOCALIZABLE
        if (changeLists[iList] == null)
            changeLists[iList] = new SrVector();
        changeLists[iList].add(line);
    }

    private String getColsString(DbORConstraint pufKey) throws DbException {
        String str = "";
        DbRelationN pufCols = (DbRelationN) pufKey
                .get(pufKey instanceof DbORPrimaryUnique ? DbORPrimaryUnique.fColumns
                        : DbObject.fComponents);
        for (int i = 0; i < pufCols.size(); i++) {
            if (i != 0)
                str = str + ", "; //NOT LOCALIZABLE
            str = str + pufCols.elementAt(i).getName();
        }
        return '(' + str + ')'; //NOT LOCALIZABLE
    }

    private StringBuffer buildReport(ChoiceSpecKeyGenerator gen) throws DbException {
        StringBuffer report = null;
        CollationComparator comparator = new CollationComparator();
        for (int i = 0; i < changeLists.length; i++) {
            SrVector changeList = changeLists[i];
            if (changeList == null)
                continue;
            if (report == null) {
                report = new StringBuffer(1000);
                String title = MessageFormat.format(kGenFKeyChanges, new Object[] { model
                        .getFullDisplayName() });
                report.append(title + '\n'); //NOT LOCALIZABLE
            }
            String header = headers[i];
            if (i == I_DEL_COL && orphanAction != DELETE_ORPHAN)
                header = kOrphanFCols;
            report.append('\n' + header + "\n\n"); //NOT LOCALIZABLE
            changeList.sort(comparator);
            for (int j = 0; j < changeList.size(); j++)
                report.append((String) changeList.get(j));
        }
        
        List<DbORChoiceOrSpecialization> choiceSpecs = gen.getGeneratedChoiceSpecs();
        if (choiceSpecs != null && choiceSpecs.size()> 0 ){        	
        	String choicespecStr = null;
            if (report == null) {
                report = new StringBuffer(1000);
                String title = MessageFormat.format(kGenSpecChoiceOnlyChanges, new Object[] { model
                        .getFullDisplayName() });
                report.append(title + "\n\n"); //NOT LOCALIZABLE
            }
            else
            	 report.append("\n\n" + kGenSpecChoiceTitle + "\n\n"); //NOT LOCALIZABLE
        	
            for (int i = 0; i < choiceSpecs.size(); i++) {             	
            	DbORChoiceOrSpecialization choiceSpec = (DbORChoiceOrSpecialization) choiceSpecs.get(i);
            	ORChoiceSpecializationCategory category = choiceSpec.getCategory();
            	if (category.getValue() == ORChoiceSpecializationCategory.CHOICE)
            		choicespecStr = kGenChoiceLabel;
            	else
            		choicespecStr = kGenSpecLabel;	
                DbORAbsTable parentTable = choiceSpec.getParentTable();
                String specChoiceLine = MessageFormat.format(kGenSpecChoiceforTableMsgLine, new Object[] { choicespecStr, parentTable.getName() });
                report.append(specChoiceLine + '\n'); //NOT LOCALIZABLE
                //report.append('\n' + "Génération de colonnes de " + choicespecStr +" pour la table " + parentTable.getName());//NOT LOCALIZABLE
         
            } //end for
        }
        	
        
        return report;
    }

    public static int deleteForeignKeys(DbORDataModel model, DeleteKeysOptions options)
            throws DbException {
        int nCount = 0;
        ChoiceSpecKeyGenerator gen = new ChoiceSpecKeyGenerator();
        DbEnumeration dbEnum = model.getComponents().elements(DbORAbsTable.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbEnumeration enum2 = dbEnum.nextElement().getComponents().elements();
            while (enum2.hasMoreElements()) {
                DbObject dbo = enum2.nextElement();
                if (dbo instanceof DbORForeign) {
                    dbo.remove();
                    nCount++;
                }

                if (options.deleteForeignColumns) {
                    if (dbo instanceof DbORColumn) {
                        DbORColumn col = (DbORColumn)dbo;
                        if (col.isForeign()) {
                            dbo.remove();
                            nCount++;
                        } else {
                            nCount += gen.deleteIfChoiceSpecColumn(col);
                        }
                    } //end if
                } //end if
            } //end while
            enum2.close();
        } //end while
        dbEnum.close();
        return nCount;
    }

    // Merge <sourceCol> into <destCol>: copy all <sourceCol> links to <destCol>, then remove <sourceCol>.
    public static void mergeColumns(DbORColumn sourceCol, DbORColumn destCol) throws DbException {
        if (sourceCol.isForeign())
            destCol.setForeign(Boolean.TRUE);
        copyIntersectLinks(sourceCol, destCol, DbORColumn.fIndexKeys);
        copyIntersectLinks(sourceCol, destCol, DbORColumn.fFKeyColumns);
        copyLinks(sourceCol, destCol, DbORColumn.fTriggers);
        copyLinks(sourceCol, destCol, DbORColumn.fChecks);
        copyLinks(sourceCol, destCol, DbORColumn.fPrimaryUniques);
        copyIntersectLinks(sourceCol, destCol, DbORColumn.fDestFKeyColumns);
        sourceCol.remove();
    }

    // Copy links in a N-N association.
    private static void copyLinks(DbORColumn sourceCol, DbORColumn destCol, MetaRelationN relN)
            throws DbException {
        MetaRelationship oppRel = relN.getOppositeRel(null);
        DbRelationN links = (DbRelationN) sourceCol.get(relN);
        while (links.size() != 0) {
            DbObject link = links.elementAt(0);
            if (oppRel.getMaxCard() == 1) {
                link.set(oppRel, destCol);
            } else {
                DbRelationN oppLinks = (DbRelationN) link.get(oppRel);
                // Replace <sourceCol> by <destCol> in the collection; if <destCol> already there, just unlink <sourceCol>.
                int ind = oppLinks.indexOf(sourceCol);
                link.set((MetaRelationN) oppRel, sourceCol, Db.REMOVE_FROM_RELN);
                if (oppLinks.indexOf(destCol) == -1) {
                    link.set((MetaRelationN) oppRel, destCol, Db.ADD_TO_RELN);
                    link.reinsert((MetaRelationN) oppRel, -1, ind);
                }
            }
        }
    }

    // Copy links in an <intersection table> association.
    private static void copyIntersectLinks(DbORColumn sourceCol, DbORColumn destCol,
            MetaRelationN relN) throws DbException {
        MetaRelationship oppRel = relN.getOppositeRel(null);
        DbRelationN links = (DbRelationN) sourceCol.get(relN);
        nextLink: while (links.size() != 0) {
            DbObject link = links.elementAt(0);
            // Replace <sourceCol> by <destCol> in the intersect object;
            // if there is another intersect object with <destCol>, remove the intersect object
            DbRelationN siblings = link.getComposite().getComponents();
            for (int i = 0; i < siblings.size(); i++) {
                if (destCol == siblings.elementAt(i).get(oppRel)) {
                    link.remove();
                    continue nextLink;
                }
            }
            link.set(oppRel, destCol);
        }
    }

    public static DbORColumn splitMergedColumn(DbORColumn mergedCol) throws DbException {
        DbORColumn newCol = (DbORColumn) mergedCol.getComposite().createComponent(
                mergedCol.getMetaClass());
        DbORFKeyColumn fKeyCol = (DbORFKeyColumn) mergedCol.getFKeyColumns().elementAt(0);
        fKeyCol.setColumn(newCol);
        copyAttributes(fKeyCol.getSourceColumn(), newCol, 0xffffffff);
        newCol.setForeign(Boolean.TRUE);
        return newCol;
    }

    public static void propagAttributes(DbORColumn sourceCol, int copyFlags) throws DbException {
        DbRelationN destCols = sourceCol.getDestFKeyColumns();
        for (int i = 0; i < destCols.size(); i++) {
            DbORColumn destCol = ((DbORFKeyColumn) destCols.elementAt(i)).getColumn();
            copyAttributes(sourceCol, destCol, copyFlags);
            propagAttributes(destCol, copyFlags);
        }
    }

    private static int copyAttributes(DbORColumn sourceCol, DbORColumn destCol, int copyFlags)
            throws DbException {
        int modFlags = 0;
        for (int i = 0; i < copyFields.length; i++) {
            if ((copyFlags & 1 << i) == 0)
                continue;
            Object value = sourceCol.get(copyFields[i]);
            Object customTypeValue = customCopyTypeAttribute(sourceCol, destCol, copyFields[i]);
            if (customTypeValue != null) {
                if (DbObject.valuesAreEqual(customTypeValue, destCol.get(copyFields[i])))
                    continue;
                else
                    destCol.set(copyFields[i], customTypeValue);
            } else {
                if (DbObject.valuesAreEqual(value, destCol.get(copyFields[i])))
                    continue;
                destCol.set(copyFields[i], value);
            }
            modFlags |= 1 << i;
        }
        return modFlags;
    }

    // Add specific type copy features here
    private static DbORTypeClassifier customCopyTypeAttribute(DbORColumn sourceCol,
            DbORColumn destCol, MetaField metaField) throws DbException {
        // INFORMIX type
        // SERIAL and SERIAL8 to INTEGER and INT8
        DbSMSTargetSystem ts = AnyORObject.getTargetSystem(sourceCol);

        int rootID = AnyORObject.getRootIDFromTargetSystem(ts);
        if ((rootID != TargetSystem.SGBD_INFORMIX_ROOT) || (metaField != DbORAttribute.fType))
            return null;

        TargetSystemInfo tsi = TargetSystemManager.getSingleton().getTargetSystemInfo(ts);

        DbORTypeClassifier sourceColType = AnyORObject.getTargetSystemType(sourceCol);
        DbORTypeClassifier destColType = AnyORObject.getTargetSystemType(destCol);

        if (sourceColType != null) {
            // SERIAL
            if (sourceColType.getName().compareToIgnoreCase("SERIAL") == 0) {
                if ((destColType == null)
                        || (!tsi.isAlias("INTEGER", destColType.getName().toUpperCase())))
                    return getINFTypeFromString(ts, sourceCol, "INTEGER");
                else
                    return (DbORTypeClassifier) destCol.get(metaField);
            }
            // SERIAL8
            //
            else if (sourceColType.getName().compareToIgnoreCase("SERIAL8") == 0) {
                if ((destColType == null)
                        || (!tsi.isAlias("INT8", destColType.getName().toUpperCase())))
                    return getINFTypeFromString(ts, sourceCol, "INT8");
                else
                    return (DbORTypeClassifier) destCol.get(metaField);
            }
        }
        return null;
    }

    // Works only with Informix
    // Returns null if any parameter is null or if type is not found
    private static DbORTypeClassifier getINFTypeFromString(DbSMSTargetSystem ts, DbORColumn srcCol,
            String typeName) throws DbException {
        DbORTypeClassifier type = srcCol.getType();

        int rootID = AnyORObject.getRootIDFromTargetSystem(ts);
        if ((type == null) || (srcCol == null) || (typeName == null) || (ts == null)
                || (rootID != TargetSystem.SGBD_INFORMIX_ROOT))
            return null;

        if (type instanceof DbORBuiltInType)
            return (DbORBuiltInType) ts.getBuiltInTypePackage().findComponentByName(
                    DbORBuiltInType.metaClass, typeName);

        if (type instanceof DbORDomain) {
            // Optimization: Try to find in the same Domain model before listing all domain models of the target system
            DbORDomain foundDom = null;
            DbORDomainModel model = (DbORDomainModel) type
                    .getCompositeOfType(DbORDomainModel.metaClass);
            DbEnumeration enumDomain = model.getComponents().elements(DbORDomain.metaClass);
            while (enumDomain.hasMoreElements()) {
                DbORDomain domain = (DbORDomain) enumDomain.nextElement();
                if (domain.getName().compareToIgnoreCase(typeName) == 0) {
                    foundDom = domain;
                    break;
                }
            }
            enumDomain.close();
            // Not found, do a complete search
            if (foundDom == null) {
                DbEnumeration enumModel = ts.getPackages().elements(DbORDomainModel.metaClass);
                while (enumModel.hasMoreElements()) {
                    DbORDomainModel model2 = (DbORDomainModel) enumModel.nextElement();
                    if (model == model2) // Optimization, this one is already searched before
                        continue;
                    enumDomain = model2.getComponents().elements(DbORDomain.metaClass);
                    while (enumDomain.hasMoreElements()) {
                        DbORDomain domain = (DbORDomain) enumDomain.nextElement();
                        if (domain.getName().compareToIgnoreCase(typeName) == 0) {
                            foundDom = domain;
                            break;
                        }
                    }
                    enumDomain.close();
                    if (foundDom != null)
                        break;
                }
                enumModel.close();
            }
            if (foundDom != null)
                return foundDom;
        }
        return (DbORBuiltInType) ts.getBuiltInTypePackage().findComponentByName(
                DbORBuiltInType.metaClass, typeName);
    }

    public static String getAttrLabel(int copyFlags) {
        String label = "";
        for (int i = 0; i < copyFields.length; i++) {
            if ((copyFlags & 1 << i) != 0) {
                if (label.length() != 0)
                    label = label + ", ";
                label = label + copyFields[i].getGUIName();
            }
        }
        return label;
    }

    public static DbORColumn getUltimateSource(DbORColumn foreignCol) throws DbException {
        DbORColumn sourceCol = foreignCol;
        while (true) {
            DbRelationN keyCols = sourceCol.getFKeyColumns();
            if (keyCols.size() == 0)
                break;
            sourceCol = ((DbORFKeyColumn) keyCols.elementAt(0)).getSourceColumn();
        }
        return sourceCol;
    }
}
