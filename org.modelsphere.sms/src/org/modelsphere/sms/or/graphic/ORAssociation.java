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

package org.modelsphere.sms.or.graphic;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.graphic.CircleLineEnd;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.LineEnd;
import org.modelsphere.jack.graphic.LineLabel;
import org.modelsphere.jack.srtool.graphic.SrLine;
import org.modelsphere.jack.srtool.graphic.SrLineEndZone;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.db.DbSMSAssociationEnd;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSLineGo;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSUmlConstraint;
import org.modelsphere.sms.db.srtypes.SMSDisplayDescriptor;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.graphic.NameAndDirectionLabel;
import org.modelsphere.sms.graphic.NameAndDuplicateLabel;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORAssociationGo;
import org.modelsphere.sms.or.db.DbORChoiceOrSpecialization;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORNotation;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORStyle;
import org.modelsphere.sms.or.db.DbORTableGo;
import org.modelsphere.sms.or.db.srtypes.ORChoiceSpecializationCategory;
import org.modelsphere.sms.or.db.srtypes.ORConnectivitiesDisplay;
import org.modelsphere.sms.or.db.srtypes.ORConnectivityPosition;
import org.modelsphere.sms.or.db.srtypes.ORNotationSymbol;
import org.modelsphere.sms.or.db.srtypes.ORNumericRepresentation;
import org.modelsphere.sms.or.db.util.AnyORObject;

public final class ORAssociation extends SrLine implements DbRefreshListener {

    // private static final String ENTITY_RELATIONSHIP_TXT =
    // org.modelsphere.sms.or.international.LocaleMgr.misc.getString("EntityRelationship");

    private MultiplicityLabel frontConnLabel;
    private MultiplicityLabel backConnLabel;
    private SrLineEndZone frontConnSymbolicZone;
    private SrLineEndZone backConnSymbolicZone;
    private RoleDescriptorLabel frontRoleDescriptorLabel;
    private RoleDescriptorLabel backRoleDescriptorLabel;
    private DbORAssociationEnd frontEnd;
    private DbORAssociationEnd backEnd;
    private DbORDiagram dbDiagram;

    // notation infos
    private DbORNotation notation;
    private int numericDisplay;
    private int symbolicDisplay;
    private int numericPosition;
    private int symbolicPosition;
    private int symbolicDepPosition;
    private int min0Symbol;
    private int min1Symbol;
    private int max1Symbol;
    private int maxNSymbol;
    private int keyDepSymbol;
    private int childRoleSymbol;
    private int numericRepresentation;
    
    private enum AssociationCategory { ORDINARY, CHOICE, SPECIALIZATION};

    public ORAssociation(Diagram diag, DbORAssociationGo newAssGO, GraphicNode node1,
            GraphicNode node2) throws DbException {
        super(diag, newAssGO, newAssGO.getAssociation(), node1, node2);
        initDbFields();
        objectInit();
        updateNotation();
        refreshLineStyle();
        refreshLabels();
    }

    private void updateNotation() throws DbException {
        if (notation != null) {
            notation.removeDbRefreshListener(this);
        }
        notation = dbDiagram.findNotation();
        numericDisplay = notation.getNumericDisplay().getValue();
        symbolicDisplay = notation.getSymbolicDisplay().getValue();
        numericPosition = notation.getNumericPosition().getValue();
        symbolicPosition = notation.getSymbolicPosition().getValue();
        symbolicDepPosition = notation.getSymbolicChildRolePosition().getValue();
        min0Symbol = notation.getMin0Symbol().getValue();
        min1Symbol = notation.getMin1Symbol().getValue();
        max1Symbol = notation.getMax1Symbol().getValue();
        maxNSymbol = notation.getMaxNSymbol().getValue();
        keyDepSymbol = (notation.isKeyDependencyVisible() ? notation.getKeyDependencySymbol()
                .getValue() : ORNotationSymbol.NONE);
        childRoleSymbol = (notation.isChildRoleVisible() ? notation.getChildRoleSymbol().getValue()
                : ORNotationSymbol.NONE);
        numericRepresentation = notation.getNumericRepresentation().getValue();
        notation.addDbRefreshListener(this, DbRefreshListener.CALL_ONCE);
    }

    private void initDbFields() throws DbException {
        dbDiagram = (DbORDiagram) lineGo.getCompositeOfType(DbORDiagram.metaClass);
        frontEnd = ((DbORAssociation) semObj).getFrontEnd();
        backEnd = ((DbORAssociation) semObj).getBackEnd();
    }

    private void objectInit() throws DbException {
        frontEnd.addDbRefreshListener(this);
        backEnd.addDbRefreshListener(this);
        semObj.addDbRefreshListener(this);
        lineGo.addDbRefreshListener(this);
        dbDiagram.addDbRefreshListener(this);
        DbSMSProject.fOrDefaultNotation.addDbRefreshListener(this, lineGo.getProject());
        DbSMSSemanticalObject.fUmlStereotype.addDbRefreshListener(this, lineGo.getProject());
        DbSMSSemanticalObject.fUmlConstraints.addDbRefreshListener(this, lineGo.getProject());
    }

    // BEWARE: overriding methods must call super.delete as last action
    public void delete(boolean all) {
        frontEnd.removeDbRefreshListener(this);
        backEnd.removeDbRefreshListener(this);
        semObj.removeDbRefreshListener(this);
        lineGo.removeDbRefreshListener(this);
        dbDiagram.removeDbRefreshListener(this);
        DbSMSProject.fOrDefaultNotation.removeDbRefreshListener(this);
        DbSMSSemanticalObject.fUmlStereotype.removeDbRefreshListener(this);
        DbSMSSemanticalObject.fUmlConstraints.removeDbRefreshListener(this);

        if (notation != null) {
            notation.removeDbRefreshListener(this);
            notation = null;
        }
        super.delete(all);
    }

    public final DbORAssociationGo getAssociationGO() {
        return (DbORAssociationGo) lineGo;
    }

    public final DbORAssociation getAssociationSO() {
        return (DbORAssociation) semObj;
    }

    private void refreshLabels() throws DbException {
        refreshLineEnds();
        refreshNameAndDuplicate();
        refreshMultiplicityLabels();
        refreshDescriptorLabels();
        chainLabels();
        diagram.setComputePos(this);
    }

	private void refreshMultiplicityLabels() throws DbException {
        Font font = (Font) getAssociationGO().find(DbORStyle.fOr_associationConnectivitiesFont);
        boolean showNotNavigable = ((Boolean) getAssociationGO().find(
                DbORStyle.fOr_associationParentRoleConnectivitiesDisplay)).booleanValue();
        boolean showNavigable = ((Boolean) getAssociationGO().find(
                DbORStyle.fOr_associationChildRoleConnectivitiesDisplay)).booleanValue();
        boolean showAsRelationship = (Boolean)getAssociationGO().find(DbORStyle.fOr_associationAsRelationships);
        boolean showBackEnd = showBackEndConnector(backEnd);
        
        if (showAsRelationship) {
            frontConnLabel = refreshMultiplicityLabel(frontEnd, DbORAssociationGo.fMulti1Offset,
                    frontConnLabel, font, false, false);
		} else {
            frontConnLabel = refreshMultiplicityLabel(frontEnd, DbORAssociationGo.fMulti1Offset,
                    frontConnLabel, font, showNavigable, showNotNavigable);
		} //end if
		
        if (showBackEnd) {
            backConnLabel = refreshMultiplicityLabel(backEnd, DbORAssociationGo.fMulti2Offset,
        	    backConnLabel, font, showNavigable, showNotNavigable);
        } //end if   
    }

    private boolean showBackEndConnector(DbORAssociationEnd backEnd) throws DbException {
    	DbORAssociationEnd oppEnd = backEnd.getOppositeEnd();
    	AssociationCategory category = findAssociationCategory(oppEnd);
    	boolean showBackEnd = !(category == AssociationCategory.SPECIALIZATION) && 
    		!(category == AssociationCategory.CHOICE);
		return showBackEnd;
	}

	private MultiplicityLabel refreshMultiplicityLabel(DbORAssociationEnd dbEnd,
            MetaField offsetMF, MultiplicityLabel label, Font font, boolean showNavigable,
            boolean showNotNavigable) throws DbException {
    	
        if (numericDisplay != ORConnectivitiesDisplay.NONE
                && (dbEnd.isNavigable() ? showNavigable : showNotNavigable)) {
            if (label == null)
                label = new MultiplicityLabel(diagram, dbEnd, this, offsetMF);
            label.setFont(font);
                        
            AssociationCategory category = findAssociationCategory(dbEnd);
            String str = getUKDependencyString(dbEnd);
            String mult = getLabelValueForMultiplicity(dbEnd); 
            
            if (category == AssociationCategory.SPECIALIZATION) {
            	mult =  "/" + mult + "/";
            } else if (category == AssociationCategory.CHOICE) {
            	mult =  "[" + mult + "]";
            }
            
            label.setValue(str, mult, hasDependencyPK(dbEnd));
        } else {
            if (label != null) {
                label.delete(false);
                label = null;
            }
        }
        return label;
    }

    private AssociationCategory findAssociationCategory(DbORAssociationEnd dbEnd) throws DbException {
    	AssociationCategory category = AssociationCategory.ORDINARY;
    	
        DbORAbsTable table = dbEnd.getClassifier();
        if (table instanceof DbORChoiceOrSpecialization) {
        	DbORChoiceOrSpecialization choiceSpecialization = (DbORChoiceOrSpecialization)table;
        	DbORAbsTable parent = choiceSpecialization.getParentTable();
        	DbORAssociationEnd oppEnd = dbEnd.getOppositeEnd();
        	DbORAbsTable oppTable = oppEnd.getClassifier();
        	if (parent.equals(oppTable)) {
                int kind = choiceSpecialization.getCategory().getValue();
                if (kind == ORChoiceSpecializationCategory.CHOICE) {
                    category = AssociationCategory.CHOICE;
                } else if (kind == ORChoiceSpecializationCategory.SPECIALIZATION) {
                    category = AssociationCategory.SPECIALIZATION;
                }
        	}
        } //end if
        
		return category;
	}

	private void refreshDescriptorLabels() throws DbException {
        Font font = (Font) getAssociationGO().find(DbORStyle.fOr_associationRoleDescriptorFont);
        boolean showNotNavigable = ((Boolean) getAssociationGO().find(
                DbORStyle.fOr_associationParentRoleDescriptorDisplay)).booleanValue();
        boolean showNavigable = ((Boolean) getAssociationGO().find(
                DbORStyle.fOr_associationChildRoleDescriptorDisplay)).booleanValue();

        frontRoleDescriptorLabel = refreshDescriptorLabel(frontEnd, DbORAssociationGo.fRole1Offset,
                frontRoleDescriptorLabel, font, showNavigable, showNotNavigable);
        backRoleDescriptorLabel = refreshDescriptorLabel(backEnd, DbORAssociationGo.fRole2Offset,
                backRoleDescriptorLabel, font, showNavigable, showNotNavigable);
    }

    private RoleDescriptorLabel refreshDescriptorLabel(DbORAssociationEnd dbEnd,
            MetaField offsetMF, RoleDescriptorLabel label, Font font, boolean showNavigable,
            boolean showNotNavigable) throws DbException {
        if (dbEnd.isNavigable() ? showNavigable : showNotNavigable) {
            if (label == null)
                label = new RoleDescriptorLabel(diagram, dbEnd, this, offsetMF);
            label.setValue(dbEnd.getName());
            label.setFont(font);
        } else {
            if (label != null) {
                label.delete(false);
                label = null;
            }
        }
        return label;
    }

    private void chainLabels() {
        LineLabel frontLabel, backLabel;
        if (numericPosition == ORConnectivityPosition.CLOSE_BY) {
            frontLabel = frontConnLabel;
            backLabel = backConnLabel;
        } else {
            frontLabel = backConnLabel;
            backLabel = frontConnLabel;
        }
        if (frontLabel == null)
            frontLabel = frontRoleDescriptorLabel;
        else
            frontLabel.setNextLabel(frontRoleDescriptorLabel);
        if (backLabel == null)
            backLabel = backRoleDescriptorLabel;
        else
            backLabel.setNextLabel(backRoleDescriptorLabel);
        setLabel1(frontLabel);
        setLabel2(backLabel);
    }

    private boolean hasDependencyPK(DbORAssociationEnd end) throws DbException {
        DbORPrimaryUnique dependencypk = null;
        DbEnumeration dbEnum = end.getDependentConstraints().elements(DbORPrimaryUnique.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORPrimaryUnique key = (DbORPrimaryUnique) dbEnum.nextElement();
            if (key.isPrimary()) {
                dependencypk = key;
                break;
            }
        }
        dbEnum.close();
        return (dependencypk != null);
    }

    private String getUKDependencyString(DbORAssociationEnd end) throws DbException {
        String result = ""; // NOT LOCALIZABLE
        DbORPrimaryUnique dependencypk = null;
        DbORAbsTable classifier = (DbORAbsTable) end.getClassifier();
        boolean isERMode = (TerminologyUtil.getInstance().getModelLogicalMode(
                classifier.getCompositeOfType(DbORDataModel.metaClass)) == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP);
        GraphicNode node;
        
        if (isERMode)
        	node = end.isFrontEnd() ? node2 : node1;
        else
        	node = end.isFrontEnd() ?  node1 : node2;
            
        ORTableBox tableBox = (node instanceof ORTableBox) ? (ORTableBox)node : null;
        List<Integer> keyidx = new ArrayList<Integer>();

        if (tableBox != null) {
            DbEnumeration dbEnum = end.getDependentConstraints().elements(
                    DbORPrimaryUnique.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbORPrimaryUnique key = (DbORPrimaryUnique) dbEnum.nextElement();
                if (key.isPrimary())
                    continue;
                Integer keyID = tableBox.calculateUniqueConstraintID(key);
                keyidx.add(keyID);
            }
            dbEnum.close();
        }

        Object[] values = keyidx.toArray();
        Arrays.sort(values);

        for (int i = 0; i < values.length; i++) {
            result = result.concat("<" + ((Integer) values[i]).toString() + ">"); // NOT LOCALIZABLE
        }

        return result;
    }

    private String getLabelValueForMultiplicity(DbORAssociationEnd dbEnd) throws DbException {
        SMSMultiplicity multiplicity = dbEnd.getMultiplicity();
        String value = "";

        if (multiplicity.getValue() == SMSMultiplicity.SPECIFIC) {
            value = dbEnd.getSpecificRangeMultiplicity();
        } else {
            switch (numericDisplay) {
            case ORConnectivitiesDisplay.MIN:
                if (numericRepresentation == ORNumericRepresentation.DATARUN)
                    value = multiplicity.getDatarunMinimumMultiplicityLabel();
                else
                    value = multiplicity.getUMLMinimumMultiplicityLabel();
                break;
            case ORConnectivitiesDisplay.MAX:
                if (numericRepresentation == ORNumericRepresentation.DATARUN)
                    value = multiplicity.getDatarunMaximumMultiplicityLabel();
                else
                    value = multiplicity.getUMLMaximumMultiplicityLabel();
                break;
            case ORConnectivitiesDisplay.MIN_MAX:
                if (numericRepresentation == ORNumericRepresentation.DATARUN)
                    value = multiplicity.getDatarunMultiplicityLabel();
                else
                    value = multiplicity.getUMLMultiplicityLabel();
                break;
            }
        } // end if

        return value;
    }

    private void refreshLineEnds() throws DbException {
        frontConnSymbolicZone = refreshMultiplicitySymbols(frontEnd, frontConnSymbolicZone);
        backConnSymbolicZone = refreshMultiplicitySymbols(backEnd, backConnSymbolicZone);
        refreshDependencySymbol(frontEnd);
        refreshDependencySymbol(backEnd);
    }

    private SrLineEndZone refreshMultiplicitySymbols(DbORAssociationEnd dbEnd, SrLineEndZone endZone)
            throws DbException {
        LineEnd end = null;

        // Max -- max is always closer to the classifier than the min
        // Set the maximum symbol first
        if ((symbolicDisplay & ORConnectivitiesDisplay.MAX) != 0) {
            switch (dbEnd.getMultiplicity().getValue()) {
            case SMSMultiplicity.OPTIONAL:
            case SMSMultiplicity.EXACTLY_ONE:
                end = createLineEndForSymbol(max1Symbol);
                break;
            case SMSMultiplicity.MANY:
            case SMSMultiplicity.ONE_OR_MORE:
                end = createLineEndForSymbol(maxNSymbol);
                break;
            }
        }

        // Append or set the minimum symbol
        if ((symbolicDisplay & ORConnectivitiesDisplay.MIN) != 0) {
            switch (dbEnd.getMultiplicity().getValue()) {
            case SMSMultiplicity.OPTIONAL:
            case SMSMultiplicity.MANY:
                end = pushNextEnd(end, createLineEndForSymbol(min0Symbol));
                break;
            case SMSMultiplicity.EXACTLY_ONE:
            case SMSMultiplicity.ONE_OR_MORE:
                end = pushNextEnd(end, createLineEndForSymbol(min1Symbol));
                break;
            }
        }

        if (end != null) {
            if (endZone == null)
                endZone = new SrLineEndZone(diagram, dbEnd, this);
        } else {
            if (endZone != null) {
                endZone.delete(false);
                endZone = null;
            }
        }
        if (symbolicPosition == (dbEnd.isFrontEnd() ? ORConnectivityPosition.CLOSE_BY
                : ORConnectivityPosition.FAR_AWAY)) {
            setEnd1(end);
            setLineEndZone1(endZone);
        } else {
            setEnd2(end);
            setLineEndZone2(endZone);
        }
        return endZone;
    }

    private void refreshDependencySymbol(DbORAssociationEnd dbEnd) throws DbException {
        LineEnd end;
        if (keyDepSymbol != ORNotationSymbol.NONE && hasDependencyPK(dbEnd))
            end = createLineEndForSymbol(keyDepSymbol);
        else if (childRoleSymbol != ORNotationSymbol.NONE && AnyORObject.isForeignAssocEnd(dbEnd)) // if key dep not
            // shown, display
            // child role symbol
            end = createLineEndForSymbol(childRoleSymbol);
        else
            return;

        if (symbolicDepPosition == (dbEnd.isFrontEnd() ? ORConnectivityPosition.CLOSE_BY
                : ORConnectivityPosition.FAR_AWAY))
            setEnd1(pushNextEnd(getEnd1(), end));
        else
            setEnd2(pushNextEnd(getEnd2(), end));
    }

    private final LineEnd createLineEndForSymbol(int symbol) {
        LineEnd end = null;
        switch (symbol) {
        case ORNotationSymbol.CARRET:
            end = LineEnd.createCursorLineEnd();
            break;
        case ORNotationSymbol.EMPTY_DOUBLE_ARROW:
            end = LineEnd.createDoubleArrowLineEnd(null);
            break;
        case ORNotationSymbol.EMPTY_HALF_LARGE_CIRCLE:
            end = CircleLineEnd.createHalfCircleLineEnd(null);
            break;
        case ORNotationSymbol.EMPTY_HALF_LARGE_DIAMOND:
            end = LineEnd.createHalfDiamondLineEnd(null);
            break;
        case ORNotationSymbol.EMPTY_HALF_LARGE_SQUARE:
            end = LineEnd.createHalfSquareLineEnd(null);
            break;
        case ORNotationSymbol.EMPTY_SINGLE_ARROW:
            end = LineEnd.createArrowLineEnd(null);
            break;
        case ORNotationSymbol.EMPTY_SMALL_CIRCLE:
            end = CircleLineEnd.createSmallCircleLineEnd(Color.white);
            break;
        case ORNotationSymbol.EMPTY_SMALL_DIAMOND:
            end = LineEnd.createSmallDiamondLineEnd(Color.white);
            break;
        case ORNotationSymbol.EMPTY_SMALL_SQUARE:
            end = LineEnd.createSquareLineEnd(Color.white);
            break;
        case ORNotationSymbol.FILLED_DOUBLE_ARROW:
            end = LineEnd.createDoubleArrowLineEnd(Color.black);
            break;
        case ORNotationSymbol.FILLED_SINGLE_ARROW:
            end = LineEnd.createArrowLineEnd(Color.black);
            break;
        case ORNotationSymbol.FILLED_SMALL_CIRCLE:
            end = CircleLineEnd.createSmallCircleLineEnd(Color.black);
            break;
        case ORNotationSymbol.FILLED_SMALL_DIAMOND:
            end = LineEnd.createSmallDiamondLineEnd(Color.black);
            break;
        case ORNotationSymbol.FILLED_SMALL_SQUARE:
            end = LineEnd.createSquareLineEnd(Color.black);
            break;
        case ORNotationSymbol.SINGLE_LINE:
            end = LineEnd.createCrossLineEnd();
            break;
        }
        return end;
    }

    private LineEnd pushNextEnd(LineEnd firstEnd, LineEnd nextEnd) {
        if (firstEnd == null)
            firstEnd = nextEnd;
        else
            firstEnd.pushNextEnd(nextEnd);
        return firstEnd;
    }

    private final void refreshLineStyle() throws DbException {
        DbORAssociationGo lineGo = getAssociationGO();
        boolean showUnidentifyingAsDashed = doShowUnidentifyingAsDashed(lineGo); 
        boolean dashstyle = false;

        if (showUnidentifyingAsDashed) {
            DbORAssociation assoc = (DbORAssociation) lineGo.getSO();
            dashstyle = !(assoc.isIsIdentifying());
        } // end if

        if (!dashstyle) {
            dashstyle = ((Boolean) (lineGo.find(DbSMSLineGo.fDashStyle))).booleanValue();
        }

        Boolean highlight = (Boolean) (lineGo.find(DbSMSLineGo.fHighlight));
        setLineStyle(highlight.booleanValue(), dashstyle);
        setLineColor((Color) (lineGo.find(DbSMSLineGo.fLineColor)));
    } // end refreshLineStyle()
    
    private static boolean doShowUnidentifyingAsDashed(DbORAssociationGo lineGo) throws DbException {
    	//get notation
    	DbORDiagram diag = (DbORDiagram)lineGo.getCompositeOfType(DbORDiagram.metaClass); 
    	DbORNotation notation = diag.getNotation();
    	Boolean b = (notation == null) ? null : notation.getUnidentifyingAssociationsAreDashed();
    	boolean showUnidentifyingAsDashed = (b == null) ? false : b.booleanValue(); 
    	
    	if (! showUnidentifyingAsDashed) {
    		b = (Boolean) lineGo.find(DbORStyle.fOr_UnidentifyingAssociationsAreDashed);
    		showUnidentifyingAsDashed = (b == null) ? false : b.booleanValue();
    	}
    	
	    return showUnidentifyingAsDashed;
	}


    private final String getDisplayName(DbSemanticalObject semObj, DbORAssociationGo lineGo)
            throws DbException {
        String displayName = null;
        int value = ((SMSDisplayDescriptor) lineGo.find(DbORStyle.fOr_nameDescriptor)).getValue();
        switch (value) {
        case SMSDisplayDescriptor.NAME:
            displayName = semObj.getName();
            break;
        case SMSDisplayDescriptor.PHYSICAL_NAME:
            displayName = semObj.getPhysicalName();
            break;
        case SMSDisplayDescriptor.ALIAS:
            displayName = semObj.getAlias();
            break;
        } // end switch

        if (displayName == null)
            displayName = "";

        return displayName;
    }

    private final void refreshNameAndDuplicate() throws DbException {
        //get style elements
        DbORAssociationGo go = (DbORAssociationGo) lineGo;
        Font font = (Font) go.find(DbORStyle.fOr_associationDescriptorFont);
        Boolean assocDirElem = (Boolean) go.find(DbORStyle.fOr_umlAssociationDirection);
        Boolean assocAsRelElem = (Boolean) go.find(DbORStyle.fOr_associationAsRelationships);
        Boolean displayUmlStereotypeElem = (Boolean) go.find(DbORStyle.fOr_umlStereotypeDisplayed);
        Boolean displayUmlConstraintElem = (Boolean) go.find(DbORStyle.fOr_umlConstraintDisplayed);
        Boolean descriptorDisplayedElem = ((Boolean) ((DbORAssociationGo) lineGo)
                .find(DbORStyle.fOr_associationDescriptorDisplay)).booleanValue();
        int nDescriptor = ((SMSDisplayDescriptor) go.find(DbORStyle.fOr_nameDescriptor)).getValue();
        String duplicate = calculateDuplicate(((DbORAssociation) semObj).getAssociationGos(),
                (DbORAssociationGo) lineGo);
        String label = "";

        //convert to basic types
        boolean displayUmlStereotype = (displayUmlStereotypeElem == null) ? true
                : displayUmlStereotypeElem.booleanValue();
        boolean descriptorDisplayed = (descriptorDisplayedElem == null) ? true
                : descriptorDisplayedElem.booleanValue();
        boolean displayUmlConstraint = (displayUmlConstraintElem == null) ? true
                : displayUmlConstraintElem.booleanValue();
        boolean assocDir = (assocDirElem == null) ? true : assocDirElem.booleanValue();

        // add uml stereotype, if any
        if (displayUmlStereotype) {
            DbSMSStereotype stereotype = ((DbORAssociation) semObj).getUmlStereotype();
            if (stereotype != null) {
                label += "«" + stereotype.getName() + "» ";
            }
        } // end if

        // add name, if any
        String name = getDisplayName((DbSemanticalObject) semObj, (DbORAssociationGo) lineGo);
        if (name.compareTo("") == 0) {
            descriptorDisplayed = false;
        }

        if (descriptorDisplayed) {
            label += name;
        }

        // add uml constraint, if any
        if (displayUmlConstraint) {
            DbRelationN relN = ((DbORAssociation) semObj).getUmlConstraints();
            DbEnumeration enu = relN.elements(DbSMSUmlConstraint.metaClass);
            boolean atLeastOne = enu.hasMoreElements();
            label += atLeastOne ? "{" : "";

            while (enu.hasMoreElements()) {
                DbSMSUmlConstraint constr = (DbSMSUmlConstraint) enu.nextElement();
                String constraintName = constr.getName();
                label += constraintName;

                if (enu.hasMoreElements()) {
                    label += ",";
                }
            } // end while
            enu.close();

            label += atLeastOne ? "}" : "";
        } // end if

        // add duplicate
        if (label.length() != 0) {
            label += "  " + duplicate; // NOT LOCALIZABLE
        } else {
            label = duplicate;
        } // end if

        NameAndDuplicateLabel currentLabel = (NameAndDuplicateLabel) getCenterLabel();

        if (label == "") { // NOT LOCALIZABLE
            if (currentLabel != null) {
                setCenterLabel(null);
                currentLabel.delete(false);
            }
        } else {
            if (currentLabel == null) {
                if (assocDir) {
                    if (nDescriptor == SMSDisplayDescriptor.NAME)
                        currentLabel = new NameAndDirectionLabel(getDiagram(), semObj, this,
                                DbORAssociationGo.fCenterOffset, descriptorDisplayed, go, "Name");
                    else
                        currentLabel = new NameAndDirectionLabel(getDiagram(), semObj, this,
                                DbORAssociationGo.fCenterOffset, descriptorDisplayed, go, "");
                } else {
                    if (nDescriptor == SMSDisplayDescriptor.NAME)
                        currentLabel = new NameAndDuplicateLabel(getDiagram(), semObj, this,
                                DbORAssociationGo.fCenterOffset, descriptorDisplayed, go, "Name");
                    else
                        currentLabel = new NameAndDuplicateLabel(getDiagram(), semObj, this,
                                DbORAssociationGo.fCenterOffset, descriptorDisplayed, go, "");
                }

                setCenterLabel(currentLabel);
            }
            currentLabel.setFont(font);
            currentLabel.setValue(label);
        }
    }

    private String calculateDuplicate(DbRelationN gosRelation, DbSMSGraphicalObject dboG)
            throws DbException {
        DbObject diag = dboG.getComposite();
        int index = 0;
        int count = 0;
        DbEnumeration dbEnum = gosRelation.elements();
        while (dbEnum.hasMoreElements()) {
            DbSMSGraphicalObject elem = (DbSMSGraphicalObject) dbEnum.nextElement();
            if (elem.getComposite() == diag)
                count++;
            if (elem == dboG)
                index = count;
        }
        dbEnum.close();
        if (count < 2)
            return "";
        String pattern = "{0}/{1}";
        return MessageFormat.format(pattern,
                new Object[] { new Integer(index), new Integer(count) });
    }

    // /////////////////////////////////////////////
    // DbRefreshListener SUPPORT
    //
    public final void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
        if (lineGo.getTransStatus() == Db.OBJ_REMOVED)
            return;

        verifySpecific(event);

        if (DbSMSGraphicalObject.fStyle == event.metaField
                || event.metaField == DbSMSLineGo.fDashStyle
                || event.metaField == DbSMSLineGo.fHighlight
                || event.metaField == DbSMSLineGo.fLineColor) {
            refreshLineStyle();
            refreshLabels();
        } else if (event.dbo == frontEnd || event.dbo == backEnd || event.dbo == semObj) {
            refreshLabels();
        } else if (event.metaField == DbORDiagram.fNotation
                || event.metaField == DbSMSProject.fOrDefaultNotation || event.dbo == notation) {
            updateNotation();
            refreshLabels();
        } else if (event.dbo == dbDiagram
                && event.neighbor instanceof DbORAssociationGo
                && (((DbORAssociationGo) event.neighbor)
                        .getAccordingToStatus(DbORAssociationGo.fAssociation) == semObj)) {
            refreshLabels();
        } // end if

        /*
         * //refresh bubbles if style is EntityRelationship if (event.dbo instanceof
         * DbORAssociationGo) { DbORAssociationGo assocGo = (DbORAssociationGo)event.dbo;
         * DbSMSDiagram diag = (DbSMSDiagram)assocGo.getCompositeOfType(DbSMSDiagram.metaClass);
         * DbSMSStyle style = diag.getStyle(); String name = style.getName(); if
         * (name.equals(ENTITY_RELATIONSHIP_TXT)) { refreshLabels(); } } //end if
         */

    } // end refreshAfterDbUpdate()

    //
    // End of DbRefreshListener SUPPORT
    // /////////////////////////////////////////////

    private void verifySpecific(DbUpdateEvent event) throws DbException {
        if (event.metaField == null)
            return;
        if (event.metaField.equals(DbSMSAssociationEnd.fMultiplicity)) {
            Object value = event.dbo.get(event.metaField);
            if (value instanceof SMSMultiplicity) {
                SMSMultiplicity mult = (SMSMultiplicity) value;
                int v = mult.getValue();

            } // end if
        } else if (event.metaField.equals(DbSMSAssociationEnd.fSpecificRangeMultiplicity)) {
            SMSMultiplicity mult = SMSMultiplicity.getInstance(SMSMultiplicity.SPECIFIC);
            // event.dbo.set(DbSMSAssociationEnd.fMultiplicity, mult);

        } // end if

    } // end verifySpecific()
}
