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

package org.modelsphere.sms.be.graphic.tool;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JOptionPane;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.be.BEModule;
import org.modelsphere.sms.be.db.DbBEContextCell;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.DbBEUseCaseGo;
import org.modelsphere.sms.be.db.util.BEUtility;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.be.text.BETextUtil;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSProject;

public class BEUseCaseTool extends BEContextTool {
    private String m_actionName;

    protected DbObject semObj = null;
    protected DbObject graphObj = null;

    public BEUseCaseTool(String text, Image image) {
        super(text, image);
        m_actionName = BETextUtil.getSingleton().getCreationText(DbBEUseCase.metaClass);
    }

    protected Cursor loadDefaultCursor() {
        return AwtUtil.createCursor(GraphicUtil.loadImage(BEModule.class,
                "db/resources/dbbeusecase.gif"), new Point(8, 8), "box", true); // NOT
        // LOCALIZABLE,
        // not
        // yet
    }

    public String getText() {
        return text;
    }

    public String getText(DbObject notation) {
        String pattern = LocaleMgr.action.getString("0Creation");

        Terminology terminology = null;
        try {
            if (notation != null) {
                notation.getDb().beginReadTrans();
                terminology = Terminology.findTerminology(notation);
                notation.getDb().commitTrans();
            }
        } catch (DbException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        MetaClass mc = DbBEUseCase.metaClass;
        String term = (terminology == null) ? mc.getGUIName() : terminology.getTerm(mc);
        String text = MessageFormat.format(pattern, new Object[] { term });
        return text;
    } // end getText()

    protected void createBox(int x, int y) {
        try {
            DbBEDiagram diagGO = (DbBEDiagram) ((ApplicationDiagram) model).getDiagramGO();
            diagGO.getDb().beginTrans(Db.WRITE_TRANS, m_actionName);
            DbSMSProject proj = (DbSMSProject) diagGO.getProject();
            DbSMSDiagram parent = diagGO.getParentDiagram();

            // if a tree diagram
            if (parent != null) {
                String ERROR = LocaleMgr.screen.getString("Error");
                JOptionPane.showMessageDialog(MainFrame.getSingleton(), BEActorTool.CANNOT_CREATE,
                        ERROR, JOptionPane.ERROR_MESSAGE);
            } else {
                // create use case
                DbObject composite = diagGO.getComposite();
                BEUtility util = BEUtility.getSingleInstance();
                DbBENotation notation = diagGO.getNotation();
                if (notation == null)
                    notation = proj.getBeDefaultNotation();

                DbBEUseCase ucSO = util.createUseCase(composite, notation);
                semObj = ucSO;

                // and its graphical representation
                if (ucSO != null) {
                    DbObject goComposite = util.getComposite(diagGO, x, y);
                    DbBEUseCaseGo ucGO = null;
                    DbBEContextCell cell = null;
                    if (goComposite instanceof DbSMSDiagram) {
                        ucGO = new DbBEUseCaseGo((DbSMSDiagram) goComposite, ucSO);

                    } else if (goComposite instanceof DbBEContextCell) {
                        cell = (DbBEContextCell) goComposite;
                        DbSMSDiagram diag = (DbSMSDiagram) cell
                                .getCompositeOfType(DbSMSDiagram.metaClass);
                        ucGO = new DbBEUseCaseGo(diag, ucSO);
                        ucGO.setCell(cell);
                    } // end if

                    if (ucGO != null) {

                        graphObj = ucGO;

                        int xPos = x - (notation.getUseCaseDefaultWidth().intValue() / 2);
                        int yPos = y - (notation.getUseCaseDefaultHeight().intValue() / 2);
                        int width = notation.getUseCaseDefaultWidth().intValue();
                        int height = notation.getUseCaseDefaultHeight().intValue();

                        // if box is inside a cell, then constraint position of
                        // the box within cell boundaries
                        if (cell != null) {
                            Rectangle cellRect = util.getCellRectangle(cell);
                            if (xPos < cellRect.x)
                                xPos = cellRect.x;

                            if (yPos < cellRect.y)
                                yPos = cellRect.y;

                            if ((xPos + width) > (cellRect.x + cellRect.width))
                                xPos = cellRect.x + cellRect.width - width;

                            if ((yPos + height) > (cellRect.y + cellRect.height))
                                yPos = cellRect.y + cellRect.height - height;
                        } // end if

                        Rectangle rect = new Rectangle(xPos, yPos, width, height);
                        ucGO.setRectangle(rect);
                        ApplicationDiagram.lockGridAlignment = true;

                        // no effect if notation.constraintCenter == false
                        cell = BESelectToolCommand.isInsideACell(diagGO, ucGO);
                        if (cell != null) {
                            Rectangle cellRect = util.getCellRectangle(cell);
                            BESelectToolCommand.stayCenterCell(diagGO, ucGO, cellRect);
                        } // end if
                    } // end if
                } // end if

            } // end if
            diagGO.getDb().commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        } // end try
    } // end createBox

} // end BEUseCaseTool
