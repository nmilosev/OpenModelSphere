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
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.text.MessageFormat;

import javax.swing.JOptionPane;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.tool.BoxTool;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.be.BEModule;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEActorGo;
import org.modelsphere.sms.be.db.DbBEContextCell;
import org.modelsphere.sms.be.db.DbBEContextGo;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.util.BEUtility;
import org.modelsphere.sms.be.db.util.CellUtility;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.be.text.BETextUtil;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.db.util.DbInitialization;

public class BEActorTool extends BoxTool {
    private String m_actionName;
    static final String CANNOT_CREATE = LocaleMgr.misc.getString("CreationNotAllowedInTreeDiagram");

    public BEActorTool(String text, Image image) {
        super(0, text, image);
        BETextUtil util = BETextUtil.getSingleton();
        m_actionName = util.getCreationText(DbBEActor.metaClass);
    }

    protected Cursor loadDefaultCursor() {
        return AwtUtil.createCursor(GraphicUtil.loadImage(BEModule.class,
                "db/resources/dbbeactor.gif"), new Point(8, 8), "box", true); // NOT
        // LOCALIZABLE,
        // not
        // yet
    }

    public String getText(DbObject notation) {
        String pattern = LocaleMgr.action.getString("0Creation");
        Terminology terminology = null;
        try {
            notation.getDb().beginReadTrans();
            terminology = Terminology.findTerminology(notation);
            notation.getDb().commitTrans();
        } catch (DbException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        MetaClass mc = DbBEActor.metaClass;
        String term = terminology.getTerm(mc);
        String text = MessageFormat.format(pattern, new Object[] { term });
        return text;
    } // end getText()

    protected final void createBox(int x, int y) {
        try {
            DbBEDiagram diagGO = (DbBEDiagram) ((ApplicationDiagram) model).getDiagramGO();
            diagGO.getDb().beginTrans(Db.WRITE_TRANS, m_actionName);
            DbSMSDiagram parent = diagGO.getParentDiagram();
            // if a tree diagram
            if (parent != null) {
                String ERROR = LocaleMgr.screen.getString("Error");
                JOptionPane.showMessageDialog(MainFrame.getSingleton(), BEActorTool.CANNOT_CREATE,
                        ERROR, JOptionPane.ERROR_MESSAGE);
            } else {
                // create actor
                DbBEActor actorSO = createActor(diagGO, x, y);

                if (actorSO != null) {
                    DbBENotation notation = diagGO.findNotation();
                    DbSMSClassifierGo actorGO = new DbBEActorGo(diagGO, actorSO);
                    actorGO.setRectangle(new Rectangle(x
                            - (notation.getActorDefaultWidth().intValue() / 2), y
                            - (notation.getActorDefaultWidth().intValue() / 2), notation
                            .getActorDefaultWidth().intValue(), notation.getActorDefaultHeight()
                            .intValue()));
                    ApplicationDiagram.lockGridAlignment = true;
                    DbBEContextCell cell = BESelectToolCommand.isInsideACell(diagGO, actorGO);
                } // end if
            } // end if
            diagGO.getDb().commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        } // end try
    } // end createBox()

    private DbBEActor createActor(DbBEDiagram diagGO, int x, int y) throws DbException {
        BEUtility util = BEUtility.getSingleInstance();
        DbSMSPackage pack = (DbSMSPackage) diagGO.getCompositeOfType(DbSMSPackage.metaClass);
        DbBENotation notation = diagGO.findNotation();
        DbBEActor actor = null;

        // is sequence diagram?
        boolean isSequenceDiagram = (notation.getNotationID().intValue() == DbInitialization.UML_SEQUENCE_DIAGRAM);

        if (isSequenceDiagram) {
            // create the column and the store (named 'role' in sequence
            // diagram)
            DbObject goComposite = util.getComposite(diagGO, x, y);
            if (goComposite instanceof DbBEContextCell) {
                DbBEContextCell cell = (DbBEContextCell) goComposite;
                DbBEContextGo frame = (DbBEContextGo) cell.getComposite();

                Dimension dim = CellUtility.getDimension(frame);
                CellUtility.insertColumn(frame, BEUtility.InsertAction.CREATE_ACTOR, dim.width,
                        dim.height);
            }
        } else {
            // create the actor only
            actor = util.createActor(pack, notation);
        }

        return actor;
    }
} // end BEActorTool
