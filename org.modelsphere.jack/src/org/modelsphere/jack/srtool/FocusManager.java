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

package org.modelsphere.jack.srtool;

import java.awt.Component;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.HashSet;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.screen.DbDataEntryFrame;
import org.modelsphere.jack.srtool.explorer.ExplorerPanel;
import org.modelsphere.jack.srtool.explorer.ExplorerView;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DiagramInternalFrame;
import org.modelsphere.jack.srtool.graphic.DiagramViewInternalFrame;
import org.modelsphere.jack.srtool.list.ListInternalFrame;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.jack.util.SrVector;

public final class FocusManager implements DbRefreshListener {

    public final static FocusManager singleton = new FocusManager();

    private final static int CHANGE_FOCUS = 0x0001;
    private final static int CHANGE_PROJECT = 0x0002;
    private final static int CHANGE_SELECTION = 0x0004;

    private DefaultMainFrame mf;
    private DbProject currentProject = null;
    private ExplorerView lastExplorer = null;
    private Object oldFocusObject = null;
    private Object focusObject = null;
    private Object[] selObjects = new Object[0];
    private DbObject[] selSemObjs = new DbObject[0];
    private SrVector currentProjectListeners = new SrVector();
    private SrVector currentFocusListeners = new SrVector();
    private SrVector selectionListeners = new SrVector();
    private boolean currentProjectHasChanged = false;
    private boolean currentFocusHasChanged = false;
    private boolean selectionHasChanged = false;
    private boolean guiLocked = false;

    private int pendingEvents = 0;
    private Runnable pendingEventsRunnable = new Runnable() {
        public void run() {
            update();
        }
    };

    private InternalFrameAdapter internalFrameListener = new InternalFrameAdapter() {
        public final void internalFrameActivated(InternalFrameEvent e) {
            postEvent(CHANGE_FOCUS);
        }

        public final void internalFrameDeactivated(InternalFrameEvent e) {
            postEvent(CHANGE_FOCUS);
        }
    };

    public static FocusManager getSingleton() {
        return singleton;
    }

    private FocusManager() {
        DbObject.fComposite.addDbRefreshListener(this);
    }

    /**
     * Return the object possessing the focus, this object can be one of the following types:
     * ExplorerView, ApplicationDiagram, DbPropertiesFrame, ListTableModel
     */
    public final Object getFocusObject() {
        return focusObject;
    }

    /**
     * Return the project associated to the focusObject. Return null if current selection spans more
     * than one project.
     */
    public final DbProject getCurrentProject() {
        return currentProject;
    }

    public final ApplicationDiagram getActiveDiagram() {
        return (focusObject instanceof ApplicationDiagram ? (ApplicationDiagram) focusObject : null);
    }

    public final Object[] getSelectedObjects() {
        return selObjects;
    }

    public final DbObject[] getSelectedSemanticalObjects() {
        return selSemObjs;
    }

    public final void setMainFrame(DefaultMainFrame newMf) {
        mf = newMf;
        JDesktopPane desktop = mf.getJDesktopPane();
        desktop.addContainerListener(new ContainerListener() {
            public void componentAdded(ContainerEvent e) {
                Component comp = e.getChild();
                if (comp instanceof DiagramInternalFrame
                        || comp instanceof DiagramViewInternalFrame
                        || comp instanceof DbDataEntryFrame || comp instanceof ListInternalFrame) {
                    JInternalFrame frame = (JInternalFrame) comp;
                    frame.addInternalFrameListener(internalFrameListener);
                    if (frame.isSelected()) {
                        postEvent(CHANGE_FOCUS);
                    }
                }
            }

            public void componentRemoved(ContainerEvent e) {
                Component comp = e.getChild();
                if (comp instanceof DiagramInternalFrame
                        || comp instanceof DiagramViewInternalFrame
                        || comp instanceof DbDataEntryFrame || comp instanceof ListInternalFrame) {
                    JInternalFrame frame = (JInternalFrame) comp;
                    frame.removeInternalFrameListener(internalFrameListener);
                    if (frame.isIcon() || frame.isClosed())
                        postEvent(CHANGE_FOCUS);
                }
            }
        });
    }

    // explorerView may be null, in which case we set the focus to the first
    // visible explorer
    public final void setFocusToExplorer(ExplorerView explorerView) {
        lastExplorer = explorerView;
        JInternalFrame iFrame = mf.getSelectedInternalFrame();
        if (iFrame != null) {
            try {
                iFrame.setSelected(false);
            } catch (java.beans.PropertyVetoException ex) {
            }
        }
        postEvent(CHANGE_FOCUS);
    }

    public final void selectionChanged(Object source) {
        if (source == focusObject)
            postEvent(CHANGE_SELECTION);
    }

    /*
     * Project added or removed. This listener is necessary only because of the distortion
     * introduced by getProjectIfOnlyOneRAM.
     */
    public final void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
        if (event.dbo instanceof DbProject)
            postEvent(CHANGE_PROJECT);
    }

    /*
     * We accumulate all the events occurring in a single event queue call, in order to fire only
     * once the focus manager listeners. Besides, all the listeners (project changed, diagram
     * changed, selection changed) are called in a single Db transaction (see fireEvents).
     */
    private void postEvent(int event) {
        if (pendingEvents == 0)
            SwingUtilities.invokeLater(pendingEventsRunnable);
        pendingEvents |= event;
    }

    // Also called by ApplicationPopupMenu to force processing of pending events
    // before building the popup menu
    public final void update() {
        if (guiLocked)
            return;

        if (pendingEvents == 0)
            return;
        int events = pendingEvents;
        pendingEvents = 0;
        if ((events & CHANGE_FOCUS) != 0) {
            oldFocusObject = focusObject;
            findFocusObject();
            if (oldFocusObject != focusObject) {
                currentFocusHasChanged = true;
                events |= CHANGE_SELECTION;
            }
        }
        if ((events & (CHANGE_PROJECT | CHANGE_SELECTION)) != 0) {
            setCurrentProject();
            if ((events & CHANGE_SELECTION) != 0)
                setSelection();
        }
        fireEvents();
    }

    private void findFocusObject() {
        focusObject = null;
        JInternalFrame frame = mf.getSelectedInternalFrame();
        if (frame != null) {
            if (frame instanceof DiagramInternalFrame)
                focusObject = ((DiagramInternalFrame) frame).getDiagram();
            else if (frame instanceof DiagramViewInternalFrame) {
                DiagramInternalFrame diagFrame = ((DiagramViewInternalFrame) frame).getDiagFrame();
                if (diagFrame != null)
                    focusObject = diagFrame.getDiagram();
            } else if (frame instanceof DbDataEntryFrame)
                focusObject = frame;
            else if (frame instanceof ListInternalFrame)
                focusObject = frame;
        }
        if (focusObject == null) {
            if (lastExplorer == null || !lastExplorer.isShown()) {
                ExplorerPanel panel = mf.getExplorerPanel();
                lastExplorer = (panel.isVisible() ? panel.getMainView() : null);
            }
            focusObject = lastExplorer;
        }
    }

    public void setNullProject() {
        currentProject = null;
        currentProjectHasChanged = true;
        try {
            fireCurrentProjectChanged();
        } catch (DbException dbe) {/* do nothing */
        }
        // update();
    }

    private void setCurrentProject() {
        DbProject project = getProjectIfOnlyOneRAM();
        if (project == null && focusObject != null) {
            if (focusObject instanceof ApplicationDiagram)
                project = ((ApplicationDiagram) focusObject).getProject();
            else if (focusObject instanceof DbDataEntryFrame)
                project = ((DbDataEntryFrame) focusObject).getProject();
            else if (focusObject instanceof ListInternalFrame)
                project = ((ListInternalFrame) focusObject).getProject();
            else if (focusObject instanceof ExplorerView) {
                Object obj = ((ExplorerView) focusObject).getSelectedProject();
                project = (obj instanceof DbProject ? (DbProject) obj : null);
            }
        }
        if (currentProject != project) {
            currentProject = project;
            currentProjectHasChanged = true;
        }
    }

    private DbProject getProjectIfOnlyOneRAM() {
        DbProject project = null;
        Db dbs[] = Db.getDbs();
        if (dbs.length == 1 && dbs[0] instanceof DbRAM) {
            try {
                dbs[0].beginTrans(Db.READ_TRANS);
                DbEnumeration dbEnum = dbs[0].getRoot().getComponents().elements(
                        DbProject.metaClass);
                if (dbEnum.hasMoreElements())
                    project = (DbProject) dbEnum.nextElement();
                dbEnum.close();
                dbs[0].commitTrans();
            } catch (DbException ex) {
            }
        }
        return project;
    }

    private void setSelection() {
        if (focusObject instanceof ApplicationDiagram)
            selObjects = ((ApplicationDiagram) focusObject).getSelectedObjects();
        else if (focusObject instanceof ExplorerView)
            selObjects = ((ExplorerView) focusObject).getSelection();
        else if (focusObject instanceof DbDataEntryFrame)
            selObjects = ((DbDataEntryFrame) focusObject).getSelection();
        else if (focusObject instanceof ListInternalFrame)
            selObjects = ((ListInternalFrame) focusObject).getSelection();
        else
            selObjects = new Object[0];
        selSemObjs = convertIntoSemanticalObjects(selObjects);
        selectionHasChanged = true;
    }

    // Fire all focus manager events in a single transaction.
    private void fireEvents() {
        if (this.guiLocked)
            return;

        try {
            if ((currentProjectHasChanged || currentFocusHasChanged) && currentProject != null)
                currentProject.getDb().beginTrans(Db.READ_TRANS);
            if (selectionHasChanged)
                DbMultiTrans.beginTrans(Db.READ_TRANS, selObjects, "");

            if (currentProjectHasChanged)
                fireCurrentProjectChanged();
            if (currentFocusHasChanged)
                fireCurrentFocusChanged();
            if (selectionHasChanged)
                fireSelectionChanged();

            if ((currentProjectHasChanged || currentFocusHasChanged) && currentProject != null)
                currentProject.getDb().commitTrans();
            if (selectionHasChanged)
                DbMultiTrans.commitTrans(selObjects);
        } catch (Exception e) {
            /* Db.abortAllTrans(); */
            e.printStackTrace();
            currentProject = null;
            focusObject = null;
            selObjects = new Object[0];
            selSemObjs = new DbObject[0];
            currentProjectHasChanged = false;
            currentFocusHasChanged = false;
            selectionHasChanged = false;
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
            try {
                fireCurrentProjectChanged();
                fireCurrentFocusChanged();
                fireSelectionChanged();
            } catch (Exception e2) {
                Db.abortAllTrans();
                e2.printStackTrace();
            }
        }
        currentProjectHasChanged = false;
        currentFocusHasChanged = false;
        selectionHasChanged = false;
    }

    public final void addCurrentFocusListener(CurrentFocusListener l) {
        if (currentFocusListeners.indexOf(l) == -1)
            currentFocusListeners.addElement(l);
    }

    public final void removeCurrentFocusListener(CurrentFocusListener l) {
        currentFocusListeners.removeElement(l);
    }

    private void fireCurrentFocusChanged() throws DbException {
        for (int i = currentFocusListeners.size(); --i >= 0;) {
            CurrentFocusListener listener = (CurrentFocusListener) currentFocusListeners
                    .elementAt(i);
            listener.currentFocusChanged(oldFocusObject, focusObject);
        }
    }

    public final void addCurrentProjectListener(CurrentProjectListener l) {
        if (currentProjectListeners.indexOf(l) == -1)
            currentProjectListeners.addElement(l);
    }

    public final void removeCurrentProjectListener(CurrentProjectListener l) {
        currentProjectListeners.removeElement(l);
    }

    private void fireCurrentProjectChanged() throws DbException {
        CurrentProjectEvent evt = new CurrentProjectEvent(currentProject);
        for (int i = currentProjectListeners.size(); --i >= 0;) {
            CurrentProjectListener listener = (CurrentProjectListener) currentProjectListeners
                    .elementAt(i);
            listener.currentProjectChanged(evt);
        }
    }

    public final void addSelectionListener(SelectionListener l) {
        if (!selectionListeners.contains(l))
            selectionListeners.addElement(l);
    }

    public final void removeSelectionListener(SelectionListener l) {
        selectionListeners.removeElement(l);
    }

    private void fireSelectionChanged() throws DbException {
        SelectionChangedEvent ev = new SelectionChangedEvent(focusObject, selObjects, selSemObjs);
        for (int i = 0; i < selectionListeners.size(); i++) {
            SelectionListener l = (SelectionListener) selectionListeners.elementAt(i);
            l.selectionChanged(ev);
        }
    }

    /*
     * Each semantical object appears only once in the result array, even if selected more than once
     * in the graphic.
     */
    private DbObject[] convertIntoSemanticalObjects(Object[] objs) {
        HashSet semObjSet = new HashSet(objs.length + objs.length / 2);
        for (int i = 0; i < objs.length; i++) {
            DbObject semObj = null;
            if (objs[i] instanceof DbObject)
                semObj = (DbObject) objs[i];
            else if (objs[i] instanceof ActionInformation)
                semObj = ((ActionInformation) objs[i]).getSemanticalObject();
            if (semObj != null)
                semObjSet.add(semObj);
        }
        DbObject[] semObjs = new DbObject[semObjSet.size()];
        return (DbObject[]) semObjSet.toArray(semObjs);
    }

    public boolean isGuiLocked() {
        return guiLocked;
    }

    public void setGuiLocked(boolean guiLocked) {
        this.guiLocked = guiLocked;
    }
}
