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
package org.modelsphere.sms.be.actions;

import java.awt.Dimension;
import java.awt.Rectangle;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.be.db.DbBEContextCell;
import org.modelsphere.sms.be.db.DbBEContextGo;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEStoreGo;
import org.modelsphere.sms.be.db.DbBEUseCaseGo;
import org.modelsphere.sms.be.db.util.BEUtility;
import org.modelsphere.sms.be.db.util.CellUtility;
import org.modelsphere.sms.be.graphic.tool.BESelectToolCommand;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.db.srtypes.SMSHorizontalAlignment;
import org.modelsphere.sms.db.srtypes.SMSVerticalAlignment;
import org.modelsphere.sms.db.util.DbInitialization;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CellMngtAction extends AbstractApplicationAction {
    static final int INSERT_ROW = 0;
    static final int INSERT_COLUMN = 1;
    static final int REMOVE_ROW = 2;
    static final int REMOVE_COLUMN = 3;

    private static final String INSERT_OBJ = LocaleMgr.action.getString("Insert0");
    private static final String REMOVE_OBJ = LocaleMgr.action.getString("Remove0");
    private static final String ROW = LocaleMgr.action.getString("row");
    private static final String COLUMN = LocaleMgr.action.getString("column");

    private int m_action;

    CellMngtAction(int action) {
        super(getActionName(action));
        m_action = action;
        setEnabled(true);
    }

    private static String getActionName(int action) {
        String pattern = ((action == INSERT_ROW) || (action == INSERT_COLUMN)) ? INSERT_OBJ
                : REMOVE_OBJ;
        String obj = ((action == INSERT_ROW) || (action == REMOVE_ROW)) ? ROW : COLUMN;
        String actionName = MessageFormat.format(pattern, new Object[] { obj });
        return actionName;
    } // end getActionName()

    // Overrides AbstractApplicationAction
    // This method performs the action without the need for an ActionEvent.
    // Will be called by the static method performAction(actionkey)
    protected void doActionPerformed() {
        DbObject selectedDbObjs[] = ApplicationContext.getFocusManager()
                .getSelectedSemanticalObjects();
        try {
            selectedDbObjs[0].getDb().beginWriteTrans(getActionName(m_action));
            modifyContextFrame((DbBEContextGo) selectedDbObjs[0]);
            selectedDbObjs[0].getDb().commitTrans();
        } catch (DbException e) {
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
        } // end try
    } // end doActionPerformed()

    // Overrides AbstractApplicationAction
    public String getText(Object[] selObjects) {
        String text = null;

        try {
            if (selObjects[0] instanceof DbBEContextGo) {
                DbBEContextGo go = (DbBEContextGo) selObjects[0];
                DbBEDiagram diagram = (DbBEDiagram) go.getCompositeOfType(DbBEDiagram.metaClass);
                DbBENotation notation = diagram.getNotation();
                Terminology terminology = Terminology.findTerminology(notation);
                String term = (terminology == null) ? null : terminology
                        .getTerm(DbBEContextCell.metaClass);
                String pattern = ((m_action == INSERT_ROW) || (m_action == INSERT_COLUMN)) ? INSERT_OBJ
                        : REMOVE_OBJ;
                if ((term != null) && (term.equals(DbBEContextCell.metaClass.getGUIName()))) {
                    term = null; // if term == cell, let term be set to either
                    // 'row' or 'column'
                }

                if (term == null) {
                    term = ((m_action == INSERT_ROW) || (m_action == REMOVE_ROW)) ? ROW : COLUMN;
                } // end if

                text = MessageFormat.format(pattern, new Object[] { term });
            } // end if
        } catch (DbException ex) {
            text = null;
        } // end try

        return text;
    } // end getText()

    // Overrides AbstractApplicationAction
    public boolean isEnabled(Object[] selObjects) {
        boolean enabled = true;

        try {
            if (selObjects[0] instanceof DbBEContextGo) {
                DbBEContextGo go = (DbBEContextGo) selObjects[0];
                DbBEDiagram diagram = (DbBEDiagram) go.getCompositeOfType(DbBEDiagram.metaClass);
                DbBENotation notation = diagram.findNotation();
                String name = notation.getName();
                int masterNotationID = notation.getMasterNotationID().intValue();
                if (masterNotationID == DbInitialization.UML_SEQUENCE_DIAGRAM) {
                    enabled = (m_action == INSERT_ROW) || (m_action == REMOVE_ROW) ? false : true;
                } else if (masterNotationID == DbInitialization.UML_ACTIVITY_DIAGRAM) {
                    enabled = (m_action == INSERT_ROW) || (m_action == REMOVE_ROW) ? false : true;
                } // end if
            } // end if
        } catch (DbException ex) {
            enabled = true;
        } // end try

        return enabled;

    } // end isEnabled()

    //
    // private methods
    //
    private void modifyContextFrame(DbBEContextGo frame) throws DbException {
        Dimension dim = CellUtility.getDimension(frame);

        switch (m_action) {
        case INSERT_ROW:
            CellUtility.insertRow(frame, dim.width, dim.height);
            break;
        case INSERT_COLUMN:
            CellUtility
                    .insertColumn(frame, BEUtility.InsertAction.NO_ACTION, dim.width, dim.height);
            break;
        case REMOVE_ROW:
            CellUtility.removeRow(frame, dim.height);
            break;
        case REMOVE_COLUMN:
            CellUtility.removeColumn(frame, dim.width);
            break;
        } // end switch()

    } // end modifyContextFrame()

} // end CellMngtAction
