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

package org.modelsphere.sms.oo.graphic;

//Java
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.LineEnd;
import org.modelsphere.jack.graphic.LineLabel;
import org.modelsphere.jack.srtool.graphic.SrLine;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.db.DbSMSAssociation;
import org.modelsphere.sms.db.DbSMSAssociationGo;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSLineGo;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSUmlConstraint;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.graphic.NameAndDirectionLabel;
import org.modelsphere.sms.graphic.NameAndDuplicateLabel;
import org.modelsphere.sms.graphic.UmlLineEndLabel;
import org.modelsphere.sms.oo.db.DbOOAdt;
import org.modelsphere.sms.oo.db.DbOOAssociation;
import org.modelsphere.sms.oo.db.DbOOAssociationEnd;
import org.modelsphere.sms.oo.db.DbOOAssociationGo;
import org.modelsphere.sms.oo.db.DbOODataMember;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.db.DbOOStyle;
import org.modelsphere.sms.oo.db.srtypes.OOAggregation;

public final class OOAssociation extends SrLine implements DbRefreshListener {

    private MultiplicityLabel frontMultiLabel = null;
    private MultiplicityLabel backMultiLabel = null;
    private RoleLabel frontRoleLabel = null;
    private RoleLabel backRoleLabel = null;
    private UmlLineEndLabel frontUmlLabel = null;
    private UmlLineEndLabel backUmlLabel = null;
    private DbOOAssociationEnd frontEnd;
    private DbOOAssociationEnd backEnd;
    private DbOODataMember dataMemberFE;
    private DbOODataMember dataMemberBE;
    private DbOODiagram dbDiagram;

    // toDo: when formalisme preference will be implemented, use appropriate fields
    private static int UML_NOTATION = 1;
    private static int SR_NOTATION = 2;
    private int notation = UML_NOTATION;

    public OOAssociation(Diagram diag, DbSMSAssociationGo newAssGO, GraphicNode node1,
            GraphicNode node2) throws DbException {
        super(diag, newAssGO, newAssGO.getAssociation(), node1, node2);
        initDbFields();
        updateLabelsVisibility();
        objectInit();
        refreshLine();
    }

    private void initDbFields() throws DbException {
        dbDiagram = (DbOODiagram) lineGo.getCompositeOfType(DbOODiagram.metaClass);
        frontEnd = ((DbOOAssociation) semObj).getFrontEnd();
        backEnd = ((DbOOAssociation) semObj).getBackEnd();
        dataMemberFE = frontEnd.getAssociationMember();
        dataMemberBE = backEnd.getAssociationMember();
        refreshOOAssociationLineEnd(frontEnd);
        refreshOOAssociationLineEnd(backEnd);
    }

    private void objectInit() throws DbException {
        dbDiagram.addDbRefreshListener(this);
        frontEnd.addDbRefreshListener(this);
        backEnd.addDbRefreshListener(this);
        dataMemberFE.addDbRefreshListener(this);
        dataMemberBE.addDbRefreshListener(this);
        lineGo.addDbRefreshListener(this);
        DbSemanticalObject.fName.addDbRefreshListener(this, lineGo.getProject());
        DbSMSSemanticalObject.fUmlStereotype.addDbRefreshListener(this, lineGo.getProject());
        DbSMSSemanticalObject.fUmlConstraints.addDbRefreshListener(this, lineGo.getProject());
    }

    // BEWARE: overriding methods must call super.delete as last action
    public void delete(boolean all) {
        dbDiagram.removeDbRefreshListener(this);
        frontEnd.removeDbRefreshListener(this);
        backEnd.removeDbRefreshListener(this);
        dataMemberFE.removeDbRefreshListener(this);
        dataMemberBE.removeDbRefreshListener(this);
        lineGo.removeDbRefreshListener(this);
        DbSemanticalObject.fName.removeDbRefreshListener(this);
        DbSMSSemanticalObject.fUmlStereotype.removeDbRefreshListener(this);
        DbSMSSemanticalObject.fUmlConstraints.removeDbRefreshListener(this);
        super.delete(all);
    }

    public final DbSMSAssociationGo getAssociationGO() {
        return (DbSMSAssociationGo) lineGo;
    }

    public final DbSMSAssociation getAssociationSO() {
        return (DbSMSAssociation) semObj;
    }

    private String getUMLStereotypeName(DbSMSSemanticalObject semObject) throws DbException {
        String stereotypeName = ""; //NOT LOCALIZABLE
        DbSMSStereotype stereotype = semObject.getUmlStereotype();
        if (stereotype != null) {
            stereotypeName = "«" + stereotype.getName() + "»"; //NOT LOCALIZABLE
        }

        return stereotypeName;
    }

    private String getUMLConstraintsName(DbSMSSemanticalObject semObject) throws DbException {
        String constraintsName = ""; //NOT LOCALIZABLE

        DbEnumeration dbEnum = semObject.getUmlConstraints().elements(DbSMSUmlConstraint.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSUmlConstraint constraint = (DbSMSUmlConstraint) dbEnum.nextElement();
            String constraintName = constraint.getName();
            if (constraintName == null)
                constraintName = ""; //NOT LOCALIZABLE
            else
                constraintName = "{" + constraintName + "}"; //NOT LOCALIZABLE
            constraintsName = constraintsName + constraintName;
        } //end while
        dbEnum.close();
        return constraintsName;
    }

    public void updateLabelsVisibility() throws DbException {
        refreshFrontMultiPresence();
        refreshBackMultiPresence();
        refreshFrontRolePresence();
        refreshBackRolePresence();
        refreshFrontUmlPresence();
        refreshBackUmlPresence();
        refreshNameAndDuplicate();
        chainLabels();
        setLabelsValue();
        diagram.setComputePos(this);
    }

    private void refreshFrontMultiPresence() throws DbException {
        Font font = (Font) getAssociationGO().find(DbOOStyle.fOojv_multiplicityFont);
        if (frontEnd.isNavigable()) {
            if (frontMultiLabel == null) {
                frontMultiLabel = new MultiplicityLabel(diagram, dataMemberFE, this,
                        DbSMSAssociationGo.fMulti1Offset);
                frontMultiLabel.setFont(font);
            }
        } else {
            if (frontMultiLabel != null) {
                frontMultiLabel.delete(false);
                frontMultiLabel = null;
            }
        }
    }

    private void refreshBackMultiPresence() throws DbException {
        Font font = (Font) getAssociationGO().find(DbOOStyle.fOojv_multiplicityFont);
        if (backEnd.isNavigable()) {
            if (backMultiLabel == null) {
                backMultiLabel = new MultiplicityLabel(diagram, dataMemberBE, this,
                        DbSMSAssociationGo.fMulti2Offset);
                backMultiLabel.setFont(font);
            }
        } else {
            if (backMultiLabel != null) {
                backMultiLabel.delete(false);
                backMultiLabel = null;
            }
        }
    }

    private void refreshFrontRolePresence() throws DbException {
        Font font = (Font) getAssociationGO().find(DbOOStyle.fOojv_associationAttributeLabelFont);
        boolean show = frontEnd.isNavigable();
        if (show) {
            boolean nameShown = ((Boolean) getAssociationGO().find(
                    DbOOStyle.fOojv_associationAttributeNameLabelDisplayed)).booleanValue();
            boolean typeShown = ((Boolean) getAssociationGO().find(
                    DbOOStyle.fOojv_associationAttributeTypeLabelDisplayed)).booleanValue();
            show = nameShown || typeShown;
        }

        if (show) {
            if (frontRoleLabel == null) {
                frontRoleLabel = new RoleLabel(diagram, dataMemberFE, this,
                        DbSMSAssociationGo.fRole1Offset);
                frontRoleLabel.setFont(font);
            }
        } else {
            if (frontRoleLabel != null) {
                frontRoleLabel.delete(false);
                frontRoleLabel = null;
            }
        } //end if
    } //end refreshFrontRolePresence()

    private void refreshBackRolePresence() throws DbException {
        Font font = (Font) getAssociationGO().find(DbOOStyle.fOojv_associationAttributeLabelFont);
        boolean show = backEnd.isNavigable();
        if (show) {
            boolean nameShown = ((Boolean) getAssociationGO().find(
                    DbOOStyle.fOojv_associationAttributeNameLabelDisplayed)).booleanValue();
            boolean typeShown = ((Boolean) getAssociationGO().find(
                    DbOOStyle.fOojv_associationAttributeTypeLabelDisplayed)).booleanValue();
            show = nameShown || typeShown;
        }

        if (show) {
            if (backRoleLabel == null) {
                backRoleLabel = new RoleLabel(diagram, dataMemberBE, this,
                        DbSMSAssociationGo.fRole2Offset);
                backRoleLabel.setFont(font);
            }
        } else {
            if (backRoleLabel != null) {
                backRoleLabel.delete(false);
                backRoleLabel = null;
            }
        } //end if
    } //end refreshBackRolePresence

    private void refreshFrontUmlPresence() throws DbException {
        Font font = (Font) getAssociationGO().find(DbOOStyle.fOojv_associationAttributeLabelFont);
        boolean show = ((((Boolean) getAssociationGO().find(DbOOStyle.fOojv_umlConstraintDisplayed))
                .booleanValue() || ((Boolean) getAssociationGO().find(
                DbOOStyle.fOojv_umlStereotypeDisplayed)).booleanValue()) && (dataMemberFE
                .getUmlConstraints().size() > 0 || dataMemberFE.getUmlStereotype() != null));

        if (show) {
            if (frontUmlLabel == null) {
                frontUmlLabel = new UmlLineEndLabel(diagram, dataMemberFE, this,
                        DbSMSAssociationGo.fUml1Offset);
                frontUmlLabel.setFont(font);
            }
        } else {
            if (frontUmlLabel != null) {
                frontUmlLabel.delete(false);
                frontUmlLabel = null;
            }
        }
    }

    private void refreshBackUmlPresence() throws DbException {
        Font font = (Font) getAssociationGO().find(DbOOStyle.fOojv_associationAttributeLabelFont);
        boolean show = ((((Boolean) getAssociationGO().find(DbOOStyle.fOojv_umlConstraintDisplayed))
                .booleanValue() || ((Boolean) getAssociationGO().find(
                DbOOStyle.fOojv_umlStereotypeDisplayed)).booleanValue()) && (dataMemberBE
                .getUmlConstraints().size() > 0 || dataMemberBE.getUmlStereotype() != null));

        if (show) {
            if (backUmlLabel == null) {
                backUmlLabel = new UmlLineEndLabel(diagram, dataMemberBE, this,
                        DbSMSAssociationGo.fUml2Offset);
                backUmlLabel.setFont(font);
            }
        } else {
            if (backUmlLabel != null) {
                backUmlLabel.delete(false);
                backUmlLabel = null;
            }
        }
    }

    private void chainLabels() {
        LineLabel frontLabel, backLabel;
        ArrayList frontLabelList = new ArrayList();
        ArrayList backLabelList = new ArrayList();

        if (frontMultiLabel != null)
            frontLabelList.add(frontMultiLabel);
        if (frontRoleLabel != null)
            frontLabelList.add(frontRoleLabel);
        if (frontUmlLabel != null)
            frontLabelList.add(frontUmlLabel);

        if (frontLabelList.size() > 0) {
            LineLabel lineLabel = null;
            LineLabel lastLabel = null;
            for (int i = 0; i < frontLabelList.size(); i++) {
                LineLabel label = (LineLabel) frontLabelList.get(i);
                if (lineLabel == null)
                    lineLabel = label;
                else
                    lastLabel.setNextLabel(label);
                lastLabel = label;
            }
            lastLabel.setNextLabel(null);
            frontLabel = lineLabel;

        } else
            frontLabel = frontRoleLabel;

        if (backMultiLabel != null)
            backLabelList.add(backMultiLabel);
        if (backRoleLabel != null)
            backLabelList.add(backRoleLabel);
        if (backUmlLabel != null)
            backLabelList.add(backUmlLabel);

        if (backLabelList.size() > 0) {
            LineLabel lineLabel = null;
            LineLabel lastLabel = null;
            for (int i = 0; i < backLabelList.size(); i++) {
                LineLabel label = (LineLabel) backLabelList.get(i);
                if (lineLabel == null)
                    lineLabel = label;
                else
                    lastLabel.setNextLabel(label);
                lastLabel = label;
            }
            lastLabel.setNextLabel(null);
            backLabel = lineLabel;

        } else
            backLabel = backRoleLabel;

        if (notation == SR_NOTATION) {
            setLabel1(frontLabel);
            setLabel2(backLabel);
        } else {
            setLabel1(backLabel);
            setLabel2(frontLabel);
        }
    }

    private void setLabelsValue() throws DbException {
        SMSMultiplicity multi = frontEnd.getMultiplicity();

        String frontMulti = getMultiplicityLabel(frontEnd);
        String backMulti = getMultiplicityLabel(backEnd);

        if (frontMultiLabel != null && multi != null) {
            frontMultiLabel.setValue(frontMulti);
        }

        multi = backEnd.getMultiplicity();
        if (backMultiLabel != null && multi != null) {
            backMultiLabel.setValue(backMulti);
        }

        if (frontRoleLabel != null) {
            String labelValue = getLabelValue(dataMemberFE.buildTypeDisplayString(), dataMemberFE
                    .buildDisplayString());
            frontRoleLabel.setValue(labelValue);
        }

        if (backRoleLabel != null) {
            String labelValue = getLabelValue(dataMemberBE.buildTypeDisplayString(), dataMemberBE
                    .buildDisplayString());
            backRoleLabel.setValue(labelValue);
        }

        if (frontUmlLabel != null) {
            frontUmlLabel.setValue(buildUmlLineEndDisplayString(dataMemberFE));
        }

        if (backUmlLabel != null) {
            backUmlLabel.setValue(buildUmlLineEndDisplayString(dataMemberBE));
        }
    } //end setLabelsValue()

    private String getMultiplicityLabel(DbOOAssociationEnd end) throws DbException {
        SMSMultiplicity multi = end.getMultiplicity();
        String label;

        if (multi.getValue() == SMSMultiplicity.SPECIFIC) {
            label = end.getSpecificRangeMultiplicity();
        } else {
            label = multi.getUMLMultiplicityLabel();
        }

        return label;
    }

    private String getLabelValue(String type, String name) throws DbException {
        DbSMSAssociationGo assocGo = getAssociationGO();
        boolean nameShown = (((Boolean) assocGo
                .find(DbOOStyle.fOojv_associationAttributeNameLabelDisplayed)).booleanValue());
        boolean typeShown = (((Boolean) assocGo
                .find(DbOOStyle.fOojv_associationAttributeTypeLabelDisplayed)).booleanValue());

        String value = "";
        if (nameShown && typeShown) {
            boolean typeBefore = (((Boolean) assocGo.find(DbOOStyle.fOojv_umlTypeBeforeName))
                    .booleanValue());
            if (typeBefore) {
                value = type + "\n" + name; //NOT LOCALIZABLE
            } else {
                value = name + " :\n" + type; //NOT LOCALIZABLE
            }
        } else if (nameShown) {
            value = name;
        } else if (typeShown) {
            value = type;
        } //end if

        return value;
    }

    private String buildUmlLineEndDisplayString(DbOODataMember member) throws DbException {
        String display = "";
        if (((Boolean) getAssociationGO().find(DbOOStyle.fOojv_umlStereotypeDisplayed))
                .booleanValue())
            display = this.getUMLStereotypeName(member);
        if (((Boolean) getAssociationGO().find(DbOOStyle.fOojv_umlConstraintDisplayed))
                .booleanValue()) {
            if (!display.equals(""))
                display = display + "  "; //NOT LOCALIZABLE
            display = display + this.getUMLConstraintsName(member);
        }
        return display;
    }

    private void refreshOOAssociationLineEnd(DbOOAssociationEnd dbEnd) throws DbException {
        LineEnd end = null;
        OOAggregation aggreg = dbEnd.getAggregation();
        if (aggreg.getValue() == OOAggregation.AGGREGATE)
            end = LineEnd.createDiamondLineEnd(Color.white);
        else if (aggreg.getValue() == OOAggregation.COMPOSITE)
            end = LineEnd.createDiamondLineEnd((Color) (getAssociationGO()
                    .find(DbSMSLineGo.fLineColor)));
        if (!dbEnd.isNavigable() && dbEnd.getOppositeEnd().isNavigable()) {
            LineEnd nextEnd = LineEnd.createArrowLineEnd(null);
            if (end != null)
                end.pushNextEnd(nextEnd);
            else
                end = nextEnd;
        }
        if (dbEnd.isFrontEnd())
            setEnd1(end);
        else
            setEnd2(end);
    }

    private boolean isTypedBy(DbOOAdt type) throws DbException {
        return (dataMemberBE.getType() == type || dataMemberBE.getElementType() == type
                || dataMemberFE.getType() == type || dataMemberFE.getElementType() == type);
    }

    private final void refreshLine() throws DbException {

        setLineStyle((Boolean) (getAssociationGO().find(DbSMSLineGo.fHighlight)),
                (Boolean) (getAssociationGO().find(DbSMSLineGo.fDashStyle)));
        setLineColor((Color) (getAssociationGO().find(DbSMSLineGo.fLineColor)));
    }

    private final void refreshNameAndDuplicate() throws DbException {
        DbOOAssociationGo assocGo = (DbOOAssociationGo) lineGo;
        Font font = (Font) assocGo.find(DbOOStyle.fOojv_associationNameFont);
        Boolean assocDir = (Boolean) assocGo.find(DbOOStyle.fOojv_umlAssociationDirection);

        String duplicate = calculateDuplicate(((DbOOAssociation) semObj).getAssociationGos(),
                (DbOOAssociationGo) lineGo);
        String name = semObj.getName();
        String label = ""; // NOT LOCALIZABLE
        boolean nameDisplayed = ((Boolean) ((DbOOAssociationGo) lineGo)
                .find(DbOOStyle.fOojv_associationNameDisplayed)).booleanValue();
        if (name == null)
            nameDisplayed = false;
        else if (name.compareTo("") == 0)
            nameDisplayed = false;
        if (nameDisplayed)
            label = (name == null) ? duplicate : name.concat("  " + duplicate);

        if (((Boolean) getAssociationGO().find(DbOOStyle.fOojv_umlStereotypeDisplayed))
                .booleanValue()) {
            String stereotypeName = getUMLStereotypeName((DbOOAssociation) semObj);
            if (stereotypeName != "")
                label = stereotypeName + " " + label; // NOT LOCALIZABLE
        }
        if (((Boolean) getAssociationGO().find(DbOOStyle.fOojv_umlConstraintDisplayed))
                .booleanValue()) {
            String constraintsName = getUMLConstraintsName((DbOOAssociation) semObj);
            if (constraintsName != "")
                label = label + " " + constraintsName; // NOT LOCALIZABLE
        }

        NameAndDuplicateLabel currentLabel = (NameAndDuplicateLabel) getCenterLabel();

        if (label == "") { // NOT LOCALIZABLE
            if (currentLabel != null) {
                setCenterLabel(null);
                currentLabel.delete(false);
            }
        } else {
            if (currentLabel == null) {
                if (assocDir.booleanValue())
                    currentLabel = new NameAndDirectionLabel(getDiagram(), semObj, this,
                            DbOOAssociationGo.fCenterOffset, nameDisplayed, assocGo, "Name");
                else
                    currentLabel = new NameAndDuplicateLabel(getDiagram(), semObj, this,
                            DbOOAssociationGo.fCenterOffset, nameDisplayed, assocGo, "Name");

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

    ///////////////////////////////////////////////
    // DbRefreshListener SUPPORT
    //
    public final void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
        if (DbSMSGraphicalObject.fStyle == event.metaField
                || event.metaField == DbSMSLineGo.fDashStyle
                || event.metaField == DbSMSLineGo.fHighlight
                || event.metaField == DbSMSLineGo.fLineColor) {
            refreshOOAssociationLineEnd(frontEnd);
            refreshOOAssociationLineEnd(backEnd);
            refreshLine();
            diagram.setComputePos(this);
        } else if (event.metaField == DbOOAssociation.fName
                || event.metaField == DbSMSSemanticalObject.fUmlConstraints
                || event.metaField == DbSMSSemanticalObject.fUmlStereotype) {
            updateLabelsVisibility();
        } else if (event.dbo == frontEnd || event.dbo == backEnd || event.dbo == dataMemberFE
                || event.dbo == dataMemberBE) {
            if (event.metaField == DbOOAssociationEnd.fAggregation) {
                refreshOOAssociationLineEnd((DbOOAssociationEnd) event.dbo);
            } else if (event.metaField == DbOOAssociationEnd.fNavigable) {
                refreshOOAssociationLineEnd((DbOOAssociationEnd) event.dbo);
                refreshOOAssociationLineEnd(((DbOOAssociationEnd) event.dbo).getOppositeEnd());
                updateLabelsVisibility();
            }

            else {
                setLabelsValue();
            }
            diagram.setComputePos(this);
        } else if (event.dbo instanceof DbOOAdt && isTypedBy((DbOOAdt) event.dbo)) {
            setLabelsValue();
            diagram.setComputePos(this);
        } else if (event.dbo == dbDiagram
                && event.neighbor instanceof DbOOAssociationGo
                && (((DbOOAssociationGo) event.neighbor)
                        .getAccordingToStatus(DbOOAssociationGo.fAssociation) == semObj)
                && lineGo.getTransStatus() != Db.OBJ_REMOVED) {
            updateLabelsVisibility();
        }

    }
    //
    // End of DbRefreshListener SUPPORT
    ///////////////////////////////////////////////
}
