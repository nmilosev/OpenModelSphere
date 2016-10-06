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

package org.modelsphere.jack.srtool.features;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.Serializable;

import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.explorer.ExplorerView;
import org.modelsphere.jack.srtool.graphic.ApplDiagramView;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DiagramInternalFrame;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.jack.util.ExceptionHandler;

public abstract class SrDragDrop {
    protected static final String kCopy = LocaleMgr.action.getString("copy");
    protected static final String kMove = LocaleMgr.action.getString("move");
    protected static final String kCopy0 = LocaleMgr.action.getString("copy0");
    protected static final String kMove0 = LocaleMgr.action.getString("move0");

    private static final int ALL_ACTIONS = DnDConstants.ACTION_COPY | DnDConstants.ACTION_MOVE
            | DnDConstants.ACTION_LINK;

    private JackDragGestureListener jackDGL = new JackDragGestureListener();
    private JackDragSourceListener jackDSL = new JackDragSourceListener();

    // There is only one drag&drop in progress at any one time.
    // If dragObjs is not null, this means that the current drag&drop comes from
    // here.
    private DbObject[] dragObjs = null;
    private int dragActions = 0;

    public final void enableDrag(Component comp) {
        DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(comp, ALL_ACTIONS,
                jackDGL);
        DragSource.getDefaultDragSource().addDragSourceListener(jackDSL);
    }

    public final void enableDrop(Component comp) {
        enableDrop(comp, null);
    }

    public abstract boolean wantHighlight(DbObject[] dragObjs, DbObject dropObj);

    public abstract void focusContainingFrame(ApplicationDiagram diagram);

    // For inactive diagrams, the GlassPane intercepts all events;
    // so we must set also the GlassPane as drop target.
    public final void enableDrop(Component comp, Component glassPane) {
        Debug.assert2(comp instanceof SrDropTarget,
                "drop target components must implement SrDropTarget");
        JackDropTargetListener jackDTL = new JackDropTargetListener();
        new DropTarget(comp, ALL_ACTIONS, jackDTL);
        if (glassPane != null)
            new DropTarget(glassPane, ALL_ACTIONS, jackDTL);
    }

    public final boolean isDragging() {
        return (dragObjs != null);
    }

    // Called in a read transaction for each object in <dragObjs>
    // Overridden
    protected int getActionsAllowed(DbObject[] dragObjs) throws DbException {
        int actions = 0;
        if (ApplicationContext.getSemanticalModel().isValidForDrag(dragObjs))
            actions |= ALL_ACTIONS;
        return actions;
    }

    // No transaction started when it is called
    // Overridden
    protected boolean isDropObjectAcceptable(DbObject[] dragObjs, DbObject dropObj, int action)
            throws DbException {
        if ((action & ALL_ACTIONS) != 0)
            return ApplicationContext.getSemanticalModel().isValidForDrop(dragObjs,
                    new DbObject[] { dropObj });
        return false;
    }

    // No transaction started when it is called
    // Overridden
    protected void performAction(DbObject[] dragObjs, DbObject dropObj, int action, Point location)
            throws DbException {
        if (action == DnDConstants.ACTION_COPY)
            ApplicationContext.getSemanticalModel().paste(dragObjs, new DbObject[] { dropObj },
                    LocaleMgr.action.getString("copy"), false);
    }

    private class JackDragGestureListener implements DragGestureListener {
        JackDragGestureListener() {
        }

        public final void dragGestureRecognized(DragGestureEvent dge) {
            ApplicationContext.getFocusManager().update(); // fire pending
            // events of focus
            // manager.
            DbObject[] semObjs = ApplicationContext.getFocusManager()
                    .getSelectedSemanticalObjects();
            if (semObjs.length == 0)
                return;
            try {
                DbMultiTrans.beginTrans(Db.READ_TRANS, semObjs, "");
                dragActions = getActionsAllowed(semObjs);
                DbMultiTrans.commitTrans(semObjs);
                if (dragActions == 0)
                    return;
            } catch (Exception e) {
                Db.abortAllTrans();
                return;
            }
            dragObjs = semObjs;
        }
    }

    private class JackDragSourceListener implements DragSourceListener {
        JackDragSourceListener() {
        }

        private boolean shiftDown = false;

        public final void dragEnter(DragSourceDragEvent dsde) {
        }

        public final void dragOver(DragSourceDragEvent dsde) {
        }

        public final void dropActionChanged(DragSourceDragEvent dsde) {
        }

        public final void dragExit(DragSourceEvent dse) {
        }

        public final void dragDropEnd(DragSourceDropEvent dsde) {
            dragObjs = null; // indicates drag&drop completed.
        }
    }

    private class JackDropTargetListener implements DropTargetListener {

        private boolean accepted;
        private DbObject dropObj;
        private int dropAction;

        JackDropTargetListener() {
        }

        public final void dragEnter(DropTargetDragEvent dtde) {
            accepted = false;
            dropObj = null;
            dragOver(dtde);
        }

        // Common code for dragEnter, dragOver and dropActionChanged
        public final void dragOver(DropTargetDragEvent dtde) {
            Component comp = dtde.getDropTargetContext().getComponent();
            SrDropTarget target = getTarget(comp);
            if (dragObjs == null/*
                                 * || (dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) == 0
                                 */) {
                accepted = false;
                dropObj = null;
            } else {
                DbObject semObj = null;
                if (comp == (Component) target)
                    semObj = target.getSemObjAtLocation(dtde.getLocation());
                else {
                    // Translate the location from GlassPane coordinates to
                    // DiagramView coordinates
                    Point location = new Point(dtde.getLocation());
                    Point pt = comp.getLocationOnScreen();
                    location.x += pt.x;
                    location.y += pt.y;
                    JViewport viewPort = (JViewport) ((Component) target).getParent();
                    pt = viewPort.getLocationOnScreen();
                    location.x -= pt.x;
                    location.y -= pt.y;
                    Rectangle rect = viewPort.getViewRect();
                    location.x += rect.x;
                    location.y += rect.y;
                    if (rect.contains(location))
                        semObj = target.getSemObjAtLocation(location);
                }
                if (dropObj != semObj || dropAction != dtde.getDropAction()) {
                    dropObj = semObj;
                    dropAction = dtde.getDropAction();
                    try {
                        accepted = (dropObj != null && isDropObjectAcceptable(dragObjs, dropObj,
                                dropAction));
                    } catch (Exception e) {
                        Db.abortAllTrans();
                        accepted = false;
                    }
                }
            }
            boolean isDiagram = false;
            if (target instanceof ApplDiagramView) {
                ApplicationDiagram ad = (ApplicationDiagram) ((ApplDiagramView) target).getModel();
                focusContainingFrame(ad);
                isDiagram = true;
            }

            //A model viewer permits to drop objects, which would modify the model
            if (! ScreenPerspective.isFullVersion()) {
            	accepted = false;
            }
            
            if (accepted) {

                if (wantHighlight(dragObjs, dropObj))
                    target.highlightCell(true);

                if (isDiagram)
                    dtde.acceptDrag(DnDConstants.ACTION_COPY);
                else
                    // dtde.acceptDrag(DnDConstants.ACTION_MOVE);
                    dtde.acceptDrag(DnDConstants.ACTION_COPY);
            } else {

                target.highlightCell(false);
                dtde.rejectDrag();
            }
        }

        public final void dropActionChanged(DropTargetDragEvent dtde) {
            dragOver(dtde);
        }

        public final void dragExit(DropTargetEvent dte) {
            DropTargetContext dtc = dte.getDropTargetContext();
            SrDropTarget target = getTarget(dtc.getComponent());
            target.highlightCell(false);
        }

        public final void drop(DropTargetDropEvent dtde) {
            if (accepted && dropAction == dtde.getDropAction()) {
                dtde.acceptDrop(dropAction);
                try {

                    try {
                        dropAction = ApplicationContext.getSemanticalModel().getDefaultDropAction(
                                dragObjs, dropObj);
                    } catch (Exception e) {
                        dropAction = DnDConstants.ACTION_COPY;
                    }

                    DropTargetContext dtc = dtde.getDropTargetContext();
                    dtc.getDropTarget().setDefaultActions(dropAction);

                    performAction(dragObjs, dropObj, dropAction, dtde.getLocation());
                    dtde.dropComplete(true);
                    Component comp = dtde.getDropTargetContext().getComponent();
                    SrDropTarget target = getTarget(comp);
                    target.highlightCell(false);
                    comp.repaint();
                } catch (Exception e) {
                    dtde.dropComplete(false);
                    try {
                        Component comp = dtde.getDropTargetContext().getComponent();
                        SrDropTarget target = getTarget(comp);
                        target.highlightCell(false);
                        ExceptionHandler.processUncatchedException(ApplicationContext
                                .getDefaultMainFrame(), e);
                    } catch (Exception e2) {
                        dragObjs = null;
                    }
                }
            } else {
                dtde.rejectDrop();
            }
        }

        private SrDropTarget getTarget(Component comp) {
            if (comp instanceof SrDropTarget)
                return (SrDropTarget) comp;
            DiagramInternalFrame frame = (DiagramInternalFrame) SwingUtilities.getAncestorOfClass(
                    DiagramInternalFrame.class, comp);
            return (SrDropTarget) frame.getDiagram().getMainView();
        }
    }

    // Dummy, the transferable is not used.
    private static class StringTransferable implements Transferable, Serializable {

        private String string;

        public StringTransferable(String string) {
            this.string = string;
        }

        public final DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { DataFlavor.stringFlavor };
        }

        public final boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.equals(DataFlavor.stringFlavor);
        }

        public final Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (!flavor.equals(DataFlavor.stringFlavor))
                throw new UnsupportedFlavorException(flavor);
            return string;
        }
    }
}
