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
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.tool.Tool;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.be.BEModule;
import org.modelsphere.sms.be.db.DbBEContextGo;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEStyle;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.DbBEUseCaseGo;
import org.modelsphere.sms.be.db.util.BEUtility;
import org.modelsphere.sms.be.features.UpdateEnvironment;
import org.modelsphere.sms.be.graphic.BEUseCaseBox;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.db.util.DbInitialization;

public final class BEExplodeTool extends Tool {

    private Cursor cursor;
    private static final String EXTERNAL_PROCESS_CANNOT_EXPLODE = LocaleMgr.message
            .getString("AnExternal0CannotExplode");

    public BEExplodeTool(String text) {
        super(0, text, GraphicUtil.loadImage(BEModule.class, "international/resources/explode.gif"));
        cursor = loadDefaultCursor();
    }

    public Cursor getCursor() {
        return cursor;
    }

    protected Cursor loadDefaultCursor() {
        return AwtUtil.loadCursor(BEModule.class, "international/resources/explode.gif", new Point(
                8, 8), "box", false); // NOT LOCALIZABLE, not yet
    }

    public String getText(DbObject notation) {

        Terminology terminology = null;
        try {
            notation.getDb().beginReadTrans();
            terminology = Terminology.findTerminology(notation);
            notation.getDb().commitTrans();
        } catch (DbException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        MetaClass mc = DbBEUseCase.metaClass;
        String term = (terminology == null) ? mc.getGUIName() : terminology.getTerm(mc);
        String pattern = LocaleMgr.action.getString("0Explode");
        String text = MessageFormat.format(pattern, new Object[] { term });
        return text;
    } // end getText()

    public void mousePressed(MouseEvent e) {
        Point pt = e.getPoint();
        GraphicComponent gc = model.graphicAt(view, pt.x, pt.y, 1 << Diagram.LAYER_GRAPHIC, false);
        if (gc == null || !(gc instanceof BEUseCaseBox)) {
            Toolkit.getDefaultToolkit().beep();
        } else {
            BEUseCaseBox box = (BEUseCaseBox) gc;
            DbBEUseCase useCase = (DbBEUseCase) box.getSemanticalObject();
            explodeUseCase(useCase);
        } // end if

        view.toolCompleted();
    }

    // NOTE: public DbBEDiagram selectOneDiagram(DbBEUseCase usecase) and
    // DbBEDiagram addDiagramElement(DbBEUseCase usecase, DbBEDiagram
    // parentDiagram)
    // have been moved into org.modelsphere.sms.db.util.DbUtility

    private void explodeUseCase(DbBEUseCase usecase) {
        DefaultMainFrame mf = ApplicationContext.getDefaultMainFrame();
        try {
            DbBEDiagram diagGO = (DbBEDiagram) ((ApplicationDiagram) model).getDiagramGO();
            usecase.getDb().beginReadTrans();
            boolean isExternal = usecase.isExternal();
            DbObject composite = usecase.getComposite();
            usecase.getDb().commitTrans();
            Terminology terminology = TerminologyUtil.getInstance().findModelTerminology(composite);
            String term = terminology.getTerm(DbBEUseCase.metaClass);

            // check if external
            if (isExternal) {
                String msg = MessageFormat.format(EXTERNAL_PROCESS_CANNOT_EXPLODE,
                        new Object[] { term });
                JOptionPane.showMessageDialog(mf, msg, ApplicationContext.getApplicationName(),
                        JOptionPane.ERROR_MESSAGE);
            } else {
                // check if already exploded
                diagGO.getDb().beginReadTrans();
                DbEnumeration dbEnum = usecase.getComponents().elements(DbBEDiagram.metaClass);
                DbBEDiagram explodedDiagram = null;
                while (dbEnum.hasMoreElements()) {
                    DbObject comp = dbEnum.nextElement();
                    if (comp instanceof DbBEDiagram) {
                        explodedDiagram = (DbBEDiagram) comp;
                        break;
                    }
                } // end while
                dbEnum.close();
                diagGO.getDb().commitTrans();

                if (explodedDiagram != null) {
                    // show the diagram
                    diagGO.getDb().beginReadTrans();
                    ApplicationContext.getDefaultMainFrame().addDiagramInternalFrame(
                            explodedDiagram);
                    diagGO.getDb().commitTrans();
                } else {
                    // create a new diagram
                    diagGO.getDb().beginReadTrans();
                    DbObject notation = ((DbBEDiagram) diagGO).findMasterNotation();
                    terminology = Terminology.findTerminology(notation);
                    String term2 = terminology.getTerm(DbBEUseCase.metaClass);
                    String pattern = LocaleMgr.action.getString("0Explode");
                    String text = MessageFormat.format(pattern, new Object[] { term2 });
                    diagGO.getDb().commitTrans();

                    diagGO.getDb().beginTrans(Db.WRITE_TRANS, text);
                    BEUtility util = BEUtility.getSingleInstance();
                    usecase.getDb().beginWriteTrans("");
                    DbBENotation childNotation = util.chooseChildNotation(usecase,
                            (DbBENotation) notation);
                    if (childNotation != null) {
                        DbBEDiagram newDiagram = util.createBEDiagram(usecase, childNotation);
                        DbBEContextGo frame = getFrameIfAny(newDiagram);
                        util.setDiagramStyle(newDiagram, childNotation);
                        UpdateEnvironment updateEnv = new UpdateEnvironment();
                        updateEnv.execute(usecase, diagGO, frame);

                    } // end if
                    diagGO.getDb().commitTrans();
                    usecase.getDb().commitTrans();
                } // end if

            } // end if
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        } // end try
    } // end explodeUseCase()

    private DbBEContextGo getFrameIfAny(DbBEDiagram diagram) throws DbException {
        DbBEContextGo frame = null;

        DbRelationN relN = diagram.getComponents();
        DbEnumeration dbEnum = relN.elements(DbBEContextGo.metaClass);
        while (dbEnum.hasMoreElements()) {
            frame = (DbBEContextGo) dbEnum.nextElement();
            break;
        } // end while
        dbEnum.close();

        return frame;
    } // end getFrameIfAny()

} // end BEExplodeTool
