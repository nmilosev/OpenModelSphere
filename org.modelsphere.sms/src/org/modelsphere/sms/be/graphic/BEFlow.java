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

package org.modelsphere.sms.be.graphic;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.DbUDFValue;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.db.srtypes.ZoneJustification;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.LineEnd;
import org.modelsphere.jack.graphic.LineLabel;
import org.modelsphere.jack.srtool.graphic.SrLine;
import org.modelsphere.jack.srtool.graphic.SrLineLabel;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEFlowGo;
import org.modelsphere.sms.be.db.DbBEFlowQualifier;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEQualifier;
import org.modelsphere.sms.be.db.DbBESingleZoneDisplay;
import org.modelsphere.sms.be.db.DbBEStyle;
import org.modelsphere.sms.be.db.srtypes.BEDiscreteContinous;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSInheritanceGo;
import org.modelsphere.sms.db.DbSMSLineGo;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.graphic.NameAndDuplicateLabel;

public class BEFlow extends SrLine implements DbRefreshListener {

    private DbBEDiagram dbDiagram;
    private HashSet zonesSources;
    private DbBENotation notation;
    private ArrayList zones = null;
    private ArrayList allLabels = null;
    private BEFlowQualifier qualifiersGo = null;
    private ArrayList qualifiers = null;

    public BEFlow(Diagram diag, DbBEFlowGo newFlowGO, GraphicNode node1, GraphicNode node2)
            throws DbException {
        super(diag, newFlowGO, newFlowGO.getSO(), node1, node2);
        objectInit();
    }

    // overriden
    public boolean isStandAloneSupported() {
        return true;
    }

    private void objectInit() throws DbException {
        dbDiagram = (DbBEDiagram) getGraphicalObject().getCompositeOfType(DbBEDiagram.metaClass);
        dbDiagram.addDbRefreshListener(this);
        dbDiagram.getProject().addDbRefreshListener(this);
        MetaField.addDbRefreshListener(this, semObj.getProject(),
                new MetaField[] { DbUDFValue.fValue });
        semObj.addDbRefreshListener(this);
        lineGo.addDbRefreshListener(this);
        updateNotation();
        refreshLine();
        refreshLineEnds();
        refreshLablesVisibility();
    }

    // BEWARE: overriding methods must call super.delete as last action
    public void delete(boolean all) {
        dbDiagram.removeDbRefreshListener(this);
        dbDiagram.getProject().removeDbRefreshListener(this);
        MetaField.removeDbRefreshListener(this, new MetaField[] { DbUDFValue.fValue });
        lineGo.removeDbRefreshListener(this);
        semObj.removeDbRefreshListener(this);
        notation.removeDbRefreshListener(this);
        removeZonesListener();
        removeQualifiersListener();
        super.delete(all);
    }

    private void updateNotation() throws DbException {
        if (notation != null) {
            notation.removeDbRefreshListener(this);
        }
        notation = dbDiagram.findNotation();
        notation.addDbRefreshListener(this, DbRefreshListener.CALL_FOR_EVERY_FIELD);
    }

    public final void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
        if (lineGo.getTransStatus() == Db.OBJ_REMOVED)
            return;
        if (DbSMSInheritanceGo.fStyle == event.metaField
                || event.metaField == DbSMSLineGo.fDashStyle
                || event.metaField == DbSMSLineGo.fHighlight
                || event.metaField == DbSMSLineGo.fLineColor) {
            refreshLineEnds();
            refreshLine();
            refreshLablesVisibility();
        } else if (event.dbo == semObj
                && (DbBEFlow.fArrowFirstEnd == event.metaField || DbBEFlow.fArrowSecondEnd == event.metaField))
            refreshLineEnds();
        else if (event.dbo == semObj && semObj.getTransStatus() != Db.OBJ_REMOVED
                && event.metaField != DbBEFlow.fSubCopies) {
            refreshLine();
            refreshLineEnds();
            refreshLablesVisibility();
        } else if ((event.dbo == dbDiagram && event.metaField == DbBEDiagram.fNotation)
                || (event.dbo == dbDiagram.getProject() && event.metaField == DbSMSProject.fBeDefaultNotation)
                || event.dbo == notation) {
            if (event.dbo == notation && event.neighbor instanceof DbBEDiagram
                    && event.neighbor != dbDiagram)
                return;
            updateNotation();
            refreshLine();
            refreshLineEnds();
            refreshLablesVisibility();
        } else if (event.dbo instanceof DbBESingleZoneDisplay
                && event.dbo.getTransStatus() == Db.OBJ_MODIFIED) {
            refreshLine();
            refreshLineEnds();
            refreshLablesVisibility();
        } else if ((event.dbo instanceof DbUDFValue && ((DbUDFValue) event.dbo).getDbObject() == semObj)
                || (event.dbo instanceof DbBEFlow && event.dbo == semObj && event.neighbor instanceof DbUDFValue))
            refreshLablesVisibility();
        else if (event.dbo instanceof DbBEQualifier
                && ((event.neighbor instanceof DbBEFlowQualifier && event.neighbor.getTransStatus() != Db.OBJ_REMOVED) || (event.neighbor == null)))
            refreshLablesVisibility();

    }

    public final void refreshLablesVisibility() throws DbException {
        if (zones != null)
            removeZonesListener();
        if (allLabels != null)
            removeLabels();
        allLabels = new ArrayList();

        // check style if label are visible
        boolean display = ((DbBEStyle) ((DbBEFlowGo) lineGo).findStyle()).getDisplayLabelDbBEFlow()
                .booleanValue();
        String name = semObj.getName();
        if (name == null || name.compareTo("") == 0) {
            display = false;
            ArrayList al = notation.getFlowZones();
            if (al.size() != 0) {
                DbBESingleZoneDisplay zonedisp = (DbBESingleZoneDisplay) al.get(0);
                if (zonedisp != null)
                    if (zonedisp.getValue(semObj) instanceof DbSMSStereotype)
                        display = true;
                    else if (zonedisp.getMetaField() == DbSemanticalObject.fName && al.size() == 1)
                        display = false;
                    else if (al.size() > 0) {
                        for (int i = 0; i < al.size(); i++) {
                            DbBESingleZoneDisplay disp = ((DbBESingleZoneDisplay) al.get(i));
                            Object o = disp.getValue(semObj);
                            String zname = (String) o;
                            if (zname == null)
                                continue;
                            if (zname.equals(""))
                                continue;
                            else
                                display = true;
                        }
                    }
            }
        }

        if (display) {
            zonesSources = new HashSet();
            zones = notation.getFlowZones();
            Iterator i = zones.iterator();
            BEFlowLabel labels = null;
            int zoneIndex = 0;
            while (i.hasNext()) {
                DbBESingleZoneDisplay zoneDisplay = (DbBESingleZoneDisplay) i.next();
                if (!zoneDisplay.isDisplayed()
                        || (zoneDisplay.getValue(semObj) == null && zoneDisplay.getMetaField() != DbSemanticalObject.fName))
                    continue;
                zonesSources.add(zoneDisplay.getSourceObject());
                MetaField offset;
                switch (zoneIndex) {
                case 0:
                    offset = DbBEFlowGo.fZone1Offset;
                    break;
                case 1:
                    offset = DbBEFlowGo.fZone2Offset;
                    break;
                case 2:
                    offset = DbBEFlowGo.fZone3Offset;
                    break;
                default:
                    offset = DbBEFlowGo.fZone4Offset;
                }
                BEFlowLabel label;
                String labelValue = "";
                Object value = zoneDisplay.getValue(semObj);

                String labelZoneName = "Name";
                if (value instanceof DbSMSStereotype)
                    labelZoneName = "Stereotype";

                MetaField mf = zoneDisplay.getMetaField();
                if (mf != null) { // Editable
                    if (mf != DbSMSSemanticalObject.fName && !(value instanceof DbSMSStereotype))
                        labelZoneName = "";
                    label = new BEFlowLabel(this.getDiagram(), semObj, this, offset, true, mf,
                            labelZoneName);
                } else
                    label = new BEFlowLabel(this.getDiagram(), semObj, this, offset, labelZoneName);

                label.setFont((Font) (((DbBEFlowGo) lineGo)
                        .find(getFontStyleMetaField(zoneDisplay))));
                label.setAlignment(ZoneJustification.getGraphicUtilAligmentValue(zoneDisplay
                        .getJustification().getValue()));
                // label.setTextColor((Color)(((DbBEFlowGo)lineGo).find(DbBEFlowGo.fLineColor)));
                // //match the line color....

                if (value instanceof DbSMSStereotype) {
                    DbSMSStereotype stereotype = (DbSMSStereotype) value;
                    labelValue = "«" + stereotype.getName() + "»";
                } else {
                    labelValue = value != null ? value.toString() : "";
                }

                label.setValue(labelValue);
                allLabels.add(label);

                zoneIndex++;
                if (labels == null)
                    labels = label;
                else
                    labels.pushNextLabel(label);
            }
            setCenterLabel(labels);
            addZonesListener();
        }
        refreshDuplicate();
        populateQualifiers();
        diagram.setComputePos(this);
    }

    private MetaField getFontStyleMetaField(DbBESingleZoneDisplay zoneDisplay) throws DbException {
        String suffix = "FontDbBEFlow";
        MetaField metaField = null;
        DbBEStyle style = (DbBEStyle) ((DbSMSLineGo) lineGo).findStyle();
        if (zoneDisplay.getMetaField() != null) {
            MetaField mf = zoneDisplay.getMetaField();
            metaField = style.getMetaField(mf.getJName() + suffix);
            if (metaField == null) {
                metaField = DbBEStyle.fNameFontDbBEFlow;
                if (Debug.isDebug())
                    System.out.println("*************** MetaField without FONT (" + mf.getJName()
                            + suffix + ")**************");
            }
        } else if (zoneDisplay.getUdf() != null)
            metaField = DbBEStyle.fUdfFontDbBEFlow;
        else {
            metaField = DbBEStyle.fNameFontDbBEFlow;
            if (Debug.isDebug())
                System.out.println("************* DbBESingleZoneDisplay without FONT ("
                        + (zoneDisplay.getUdf() != null ? "UDF)" : "OTHER)") + "*************");
        }
        return metaField;
    }

    private void removeLabels() {
        Iterator i = allLabels.iterator();
        while (i.hasNext()) {
            SrLineLabel label = (SrLineLabel) i.next();
            label.setNextLabel(null);
            label.delete(false);
        }
        setCenterLabel(null);
    }

    private void addZonesListener() {
        Iterator i = zones.iterator();
        while (i.hasNext()) {
            DbBESingleZoneDisplay zoneDisplay = (DbBESingleZoneDisplay) i.next();
            zoneDisplay.addDbRefreshListener(this);
        }
    }

    private void removeZonesListener() {
        if (zones == null)
            return;
        Iterator i = zones.iterator();
        while (i.hasNext()) {
            DbBESingleZoneDisplay zoneDisplay = (DbBESingleZoneDisplay) i.next();
            zoneDisplay.removeDbRefreshListener(this);
        }
    }

    private void addQualifiersListener() {
        if (qualifiers == null)
            return;
        Iterator i = qualifiers.iterator();
        while (i.hasNext()) {
            DbBEQualifier qualifier = (DbBEQualifier) i.next();
            qualifier.addDbRefreshListener(this);
        }
    }

    private void removeQualifiersListener() {
        if (qualifiers == null)
            return;
        Iterator i = qualifiers.iterator();
        while (i.hasNext()) {
            DbBEQualifier qualifier = (DbBEQualifier) i.next();
            qualifier.removeDbRefreshListener(this);
        }
        qualifiers = null;
    }

    private void populateQualifiers() throws DbException {
        if (qualifiersGo != null)
            qualifiersGo.delete(false);
        qualifiersGo = null;
        removeQualifiersListener();
        if (((DbBEStyle) ((DbBEFlowGo) lineGo).findStyle()).getDisplayQualiferDbBEFlow()
                .booleanValue()) {
            DbEnumeration dbEnum = semObj.getComponents().elements(DbBEFlowQualifier.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbBEFlowQualifier flowQualifier = (DbBEFlowQualifier) dbEnum.nextElement();
                if (qualifiers == null) {
                    qualifiers = new ArrayList();
                }
                qualifiers.add(flowQualifier.getQualifier());
            }
            dbEnum.close();
            if (qualifiers != null) {
                qualifiersGo = new BEFlowQualifier(diagram, semObj, this,
                        DbBEFlowGo.fQualifiersOffset, qualifiers);
                LineLabel label = this.getCenterLabel();
                if (label == null)
                    setCenterLabel(qualifiersGo);
                else
                    label.pushNextLabel(qualifiersGo);
                addQualifiersListener();
            }
        }
    }

    private final void refreshLine() throws DbException {
        Boolean dashStyle = ((DbBEFlow) semObj).isControl() == true ? Boolean.TRUE
                : (Boolean) (((DbBEFlowGo) lineGo).find(DbBEFlowGo.fDashStyle));
        setLineStyle((Boolean) (((DbBEFlowGo) lineGo).find(DbBEFlowGo.fHighlight)), dashStyle);
        setLineColor((Color) (((DbBEFlowGo) lineGo).find(DbBEFlowGo.fLineColor)));
    }

    private final void refreshLineEnds() throws DbException {
        if (((DbBEFlow) semObj).isArrowFirstEnd()) {
            if (((DbBEFlow) semObj).getDiscreteContinous().getValue() == BEDiscreteContinous.CONTINOUS)
                setEnd1(LineEnd.createDoubleArrowLineEnd((Color) (((DbBEFlowGo) lineGo)
                        .find(DbBEFlowGo.fLineColor))));
            else
                setEnd1(LineEnd.createArrowLineEnd((Color) (((DbBEFlowGo) lineGo)
                        .find(DbBEFlowGo.fLineColor))));
        } else
            setEnd1(null);
        if (((DbBEFlow) semObj).isArrowSecondEnd()) {
            if (((DbBEFlow) semObj).getDiscreteContinous().getValue() == BEDiscreteContinous.CONTINOUS)
                setEnd2(LineEnd.createDoubleArrowLineEnd((Color) (((DbBEFlowGo) lineGo)
                        .find(DbBEFlowGo.fLineColor))));
            else
                setEnd2(LineEnd.createArrowLineEnd((Color) (((DbBEFlowGo) lineGo)
                        .find(DbBEFlowGo.fLineColor))));
        } else
            setEnd2(null);
        diagram.setComputePos(this);
    }

    private final void refreshDuplicate() throws DbException {
        DbBEFlowGo flowGo = (DbBEFlowGo) lineGo;
        String duplicate = calculateDuplicate(((DbBEFlow) semObj).getFlowGos(), flowGo);

        LineLabel currentLabel = getCenterLabel();
        if (duplicate != "") {
            NameAndDuplicateLabel duplicateLabel = new NameAndDuplicateLabel(diagram, semObj, this,
                    DbBEFlowGo.fCenterOffset, false, flowGo, "Name");
            duplicateLabel.setValue(duplicate);
            if (currentLabel != null)
                currentLabel.pushNextLabel(duplicateLabel);
            else
                setCenterLabel(duplicateLabel);
            allLabels.add(duplicateLabel);
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
        String pattern = "({0}/{1})";
        return MessageFormat.format(pattern,
                new Object[] { new Integer(index), new Integer(count) });
    }

}
