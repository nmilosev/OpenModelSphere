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

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.LineEnd;
import org.modelsphere.jack.graphic.LineLabel;
import org.modelsphere.jack.srtool.graphic.SrLine;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSInheritanceGo;
import org.modelsphere.sms.db.DbSMSLineGo;
import org.modelsphere.sms.graphic.NameAndDuplicateLabel;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.db.DbOOInheritance;
import org.modelsphere.sms.oo.db.DbOOStyle;
import org.modelsphere.sms.oo.java.db.DbJVClass;

public class OOInheritance extends SrLine implements DbRefreshListener {

    private DbOODiagram dbDiagram;

    public OOInheritance(Diagram diag, DbSMSInheritanceGo newInhGO, GraphicNode node1,
            GraphicNode node2) throws DbException {
        super(diag, newInhGO, newInhGO.getInheritance(), node1, node2);
        setEnd2(LineEnd.createArrowLineEnd(Color.white));
        objectInit(newInhGO);
    }

    private void objectInit(DbSMSInheritanceGo newInhGO) throws DbException {
        dbDiagram = (DbOODiagram) getGraphicalObject().getCompositeOfType(DbOODiagram.metaClass);
        dbDiagram.addDbRefreshListener(this);
        lineGo.addDbRefreshListener(this);
        refreshLine();
        refreshLablesVisibility();
    }

    // BEWARE: overriding methods must call super.delete as last action
    public void delete(boolean all) {
        dbDiagram.removeDbRefreshListener(this);
        lineGo.removeDbRefreshListener(this);
        super.delete(all);
    }

    public final void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
        if (DbSMSInheritanceGo.fStyle == event.metaField
                || event.metaField == DbSMSLineGo.fDashStyle
                || event.metaField == DbSMSLineGo.fHighlight
                || event.metaField == DbSMSLineGo.fLineColor) {
            refreshLine();
        } else if (event.dbo == dbDiagram
                && event.neighbor instanceof DbSMSInheritanceGo
                && (((DbSMSInheritanceGo) event.neighbor).getInheritance() == semObj || ((DbSMSInheritanceGo) event.neighbor)
                        .getInheritance() == null) && lineGo.getTransStatus() != Db.OBJ_REMOVED) {
            refreshLablesVisibility();
        }
    }

    public final void refreshLablesVisibility() throws DbException {
        refreshDuplicate();
        diagram.setComputePos(this);
    }

    private final void refreshLine() throws DbException {
        DbSMSInheritanceGo inherGo = (DbSMSInheritanceGo) lineGo;
        boolean realization = isRealization(inherGo);
        Boolean highlight = findHighlight(inherGo, realization);
        Boolean dashstyle = findDashstyle(inherGo, realization);
        setLineStyle(highlight, dashstyle);
        setLineColor((Color) (((DbSMSInheritanceGo) lineGo).find(DbSMSInheritanceGo.fLineColor)));
    } // end refreshLine()

    private Boolean findHighlight(DbSMSInheritanceGo inherGo, boolean realization)
            throws DbException {
        Boolean highlight = inherGo.getHighlight();

        if (highlight == null) {
            DbOOStyle style = getStyleFrom(inherGo);

            if (realization) {
                highlight = (style == null) ? Boolean.FALSE : style
                        .getHighlightRealizationInheritance();
            } else {
                highlight = (style == null) ? Boolean.FALSE : style.getHighlightDbSMSInheritance();
            }
        }

        return highlight;
    }

    private DbOOStyle getStyleFrom(DbSMSInheritanceGo inherGo) throws DbException {
        DbOOStyle style = (DbOOStyle) inherGo.getStyle();
        if (style == null) {
            DbOODiagram diagram = (DbOODiagram) inherGo.getCompositeOfType(DbOODiagram.metaClass);
            style = (DbOOStyle) diagram.getStyle();
        }

        return style;
    } // end getStyleFrom()

    // UML demands a dashed line from a class to an solid-box interface [MS]
    // (class-interface links are realization inheritance, subjects to
    // realization styles)
    // UML 1.4, section 3.29.4
    // A dashed generalization arrow from a class symbol to an interface symbol,
    // or a solid
    // line connecting a class symbol and an interface circle, maps into an
    // Abstraction
    // dependency with the «realize» stereotype between the corresponding
    // Classifier and
    // Interface elements.
    private Boolean findDashstyle(DbSMSInheritanceGo inherGo, boolean realization)
            throws DbException {
        Boolean dashstyle = inherGo.getDashStyle();

        if (dashstyle == null) {
            DbOOStyle style = getStyleFrom(inherGo);

            if (realization) {
                dashstyle = (style == null) ? Boolean.TRUE : style
                        .getDashStyleRealizationInheritance();
            } else {
                dashstyle = (style == null) ? Boolean.FALSE : style.getDashStyleDbSMSInheritance();
            }
        }

        return dashstyle;
    }

    private boolean isRealization(DbSMSInheritanceGo inherGo) throws DbException {
        DbOOInheritance inher = (DbOOInheritance) inherGo.getInheritance();
        DbJVClass subClass = (DbJVClass) inher.getSubClass();
        DbJVClass superClass = (DbJVClass) inher.getSuperClass();
        boolean isRealization = false;
        if (subClass.isInterface()) {
            if (!superClass.isInterface()) {
                // should not happen!
            }
        } else {
            if (superClass.isInterface()) {
                isRealization = true;
            }
        }

        return isRealization;
    } // end isRealization()

    private final void refreshDuplicate() throws DbException {
        DbSMSInheritanceGo inherGo = (DbSMSInheritanceGo) lineGo;
        String duplicate = calculateDuplicate(((DbOOInheritance) semObj).getInheritanceGos(),
                inherGo);

        LineLabel currentLabel = getCenterLabel();
        if (duplicate == "") {
            if (currentLabel != null) {
                setCenterLabel(null);
                currentLabel.delete(false);
            }
        } else {
            if (currentLabel == null) {
                currentLabel = new NameAndDuplicateLabel(diagram, semObj, this,
                        DbSMSInheritanceGo.fCenterOffset, false, inherGo, "");
                setCenterLabel(currentLabel);
            }
            ((NameAndDuplicateLabel) currentLabel).setValue(duplicate);
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

}
