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

package org.modelsphere.jack.srtool.actions;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.awt.SRSystemClipboard;
import org.modelsphere.jack.awt.SRSystemClipboardListener;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.clipboard.Clipboard;
import org.modelsphere.jack.baseDb.db.clipboard.ClipboardListener;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DbGraphic;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.jack.util.ExceptionHandler;

@SuppressWarnings("serial")
public class PasteAction extends AbstractApplicationAction implements SelectionActionListener,
        ClipboardListener, SRSystemClipboardListener {

    public PasteAction() {
        super(LocaleMgr.action.getString("Paste"), LocaleMgr.action.getImageIcon("Paste"));
        this.setAccelerator(KeyStroke.getKeyStroke(LocaleMgr.action.getAccelerator("Paste"),
                ActionEvent.CTRL_MASK));
        this.setMnemonic(LocaleMgr.action.getMnemonic("Paste"));
        ApplicationContext.getDefaultMainFrame().getClipboard().addClipboardListeners(this);
        SRSystemClipboard.addSystemClipboardListener(this);
        systemClipboardContentTypeChanged();
        setVisible(ScreenPerspective.isFullVersion());
    }

    protected final void doActionPerformed() {
        doActionPerformed((ActionEvent) null);
    }

    protected final void doActionPerformed(ActionEvent evt) {
        try {
            // The system clipboard has precedence over our semantical objects clipboard.
            boolean isDiagContainer = (isApplicationDiagramHaveFocus() && ApplicationContext
                    .getFocusManager().getSelectedObjects().length == 0);
            String text = SRSystemClipboard.getClipboardText();
            if (text != null && text.length() != 0) {
                if (isDiagContainer) {
                    ApplicationDiagram diag = (ApplicationDiagram) ApplicationContext
                            .getFocusManager().getFocusObject();
                    DbObject diagGo = diag.getDiagramGO();
                    Point clickPosition = getDiagramLocation(evt);
                    if (clickPosition == null)
                        clickPosition = GraphicUtil.rectangleGetCenter(diag.getMainView()
                                .getViewRect());

                    diagGo.getDb().beginTrans(Db.WRITE_TRANS,
                            LocaleMgr.action.getString("pasteFreeText"));
                    DbObject textGo = diagGo.createComponent(DbGraphic.fUserTextGoText
                            .getMetaClass());
                    textGo.set(DbGraphic.fUserTextGoText, text);
                    textGo.set(DbGraphic.fGraphicalObjectRectangle, new Rectangle(clickPosition.x,
                            clickPosition.y, 0, 0));
                    textGo.set(DbGraphic.fGraphicalObjectAutoFit, Boolean.TRUE);
                    diagGo.getDb().commitTrans();

                }
            } else {
                // if diagram and selection empty, paste in the diagram semantical object
                DbObject[] selSemObjs = ApplicationContext.getFocusManager()
                        .getSelectedSemanticalObjects();
                if (isDiagContainer) {
                    ApplicationDiagram diag = (ApplicationDiagram) ApplicationContext
                            .getFocusManager().getFocusObject();
                    selSemObjs = new DbObject[] { diag.getSemanticalObject() };
                }
                ApplicationContext.getDefaultMainFrame().getClipboard().pasteTo(selSemObjs);
            }
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    protected void update() throws DbException {
        // The system clipboard has precedence over our semantical objects clipboard.
        boolean isDiagContainer = (isApplicationDiagramHaveFocus() && ApplicationContext
                .getFocusManager().getSelectedObjects().length == 0);
        if (SRSystemClipboard.containsText()) {
            setEnabled(isDiagContainer);
        } else {
            DbObject[] selSemObjs = ApplicationContext.getFocusManager()
                    .getSelectedSemanticalObjects();
            if (isDiagContainer) {
                ApplicationDiagram diag = (ApplicationDiagram) ApplicationContext.getFocusManager()
                        .getFocusObject();
                selSemObjs = new DbObject[] { diag.getSemanticalObject() };
            }

            // Ensure a transaction is open (the FocusManager may not have open the transaction because the selection on the diagram is empty.
            DbMultiTrans.beginTrans(Db.READ_TRANS, selSemObjs, "");
            DefaultMainFrame mf = ApplicationContext.getDefaultMainFrame();
            Clipboard cb = mf.getClipboard();
            boolean pasteable = cb.isValidForPaste(selSemObjs); 
            setEnabled(pasteable);
            DbMultiTrans.commitTrans(selSemObjs);
        }
    }

    protected void updateOffline() {
        // The system clipboard has precedence over our semantical objects clipboard.
        boolean isDiagContainer = (isApplicationDiagramHaveFocus() && ApplicationContext
                .getFocusManager().getSelectedObjects().length == 0);
        if (SRSystemClipboard.containsText()) {
            setEnabled(isDiagContainer);
        } else {
            DbObject[] selSemObjs = ApplicationContext.getFocusManager()
                    .getSelectedSemanticalObjects();
            if (isDiagContainer) {
                ApplicationDiagram diag = (ApplicationDiagram) ApplicationContext.getFocusManager()
                        .getFocusObject();
                selSemObjs = new DbObject[] { diag.getSemanticalObject() };
            }
            setEnabled(ApplicationContext.getDefaultMainFrame().getClipboard()
                    .isValidForPasteOffline(selSemObjs));
        }
    }

    public final void updateSelectionAction() throws DbException {
        update();
    }

    public final void clipboardUpdated() {
        updateOffline();
    }

    @Override
    public void systemClipboardContentTypeChanged() {
        // The system clipboard has precedence over our semantical objects clipboard.
        boolean isDiagContainer = (isApplicationDiagramHaveFocus() && ApplicationContext
                .getFocusManager().getSelectedObjects().length == 0);
        if (SRSystemClipboard.containsText()) {
            setEnabled(isDiagContainer);
        } else {
            DbObject[] selSemObjs = ApplicationContext.getFocusManager()
                    .getSelectedSemanticalObjects();
            if (isDiagContainer) {
                ApplicationDiagram diag = (ApplicationDiagram) ApplicationContext.getFocusManager()
                        .getFocusObject();
                selSemObjs = new DbObject[] { diag.getSemanticalObject() };
            }

            // Ensure a transaction is open (the FocusManager may not have open the transaction because the selection on the diagram is empty.
            try {
                DbMultiTrans.beginTrans(Db.READ_TRANS, selSemObjs, "");
                setEnabled(ApplicationContext.getDefaultMainFrame().getClipboard().isValidForPaste(
                        selSemObjs));
                DbMultiTrans.commitTrans(selSemObjs);
            } catch (DbException e) {
                ExceptionHandler.processUncatchedException(
                        ApplicationContext.getDefaultMainFrame(), e);
            }
        }
    }
}
