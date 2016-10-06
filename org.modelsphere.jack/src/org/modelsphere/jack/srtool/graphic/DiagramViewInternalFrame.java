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
package org.modelsphere.jack.srtool.graphic;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.*;
import javax.swing.event.*;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.graphic.*;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.CurrentFocusListener;

public abstract class DiagramViewInternalFrame extends JInternalFrame implements
        PropertyChangeListener, CurrentFocusListener {

    protected DiagramInternalFrame diagFrame = null;
    protected JComponent viewComponent;
    protected DiagramView view;

    private InternalFrameAdapter internalFrameListener = new InternalFrameAdapter() {
        public void internalFrameClosed(InternalFrameEvent e) {
            deinstallDiagramView();
        }

        public void internalFrameIconified(InternalFrameEvent e) {
            deinstallDiagramView();
        }
    };

    public DiagramViewInternalFrame() {
        setSize(200, 200);
        setResizable(true);
        ApplicationContext.getFocusManager().addCurrentFocusListener(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addPropertyChangeListener(this);
    }

    /**
     * To close the window, you must call this method (do not call dispose).
     */
    public final void close() {
        try {
            setClosed(true);
        } catch (java.beans.PropertyVetoException e) {
        } // should not happen
    }

    public final void propertyChange(PropertyChangeEvent ev) {
        if (ev.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY)
                && ((Boolean) ev.getNewValue()).booleanValue()) {
            ApplicationContext.getFocusManager().removeCurrentFocusListener(this);
            deinstallDiagramView();
        }
    }

    // ////////////////////////////////////////////
    // CurrentFocusListener SUPPORT
    //
    public final void currentFocusChanged(Object oldFocusObject, Object focusObject)
            throws DbException {
        installDiagramView();
    }

    //
    // End of CurrentFocusListener SUPPORT
    // ///////////////////////////////////////////

    public final DiagramInternalFrame getDiagFrame() {
        return diagFrame;
    }

    public final void deinstallDiagramView() {
        if (diagFrame != null) {
            diagFrame.removeInternalFrameListener(internalFrameListener);
            getContentPane().remove(viewComponent);
            view.delete();
            diagFrame = null;
            validate();
            repaint();
        }
    }

    public final void installDiagramView() {
        DiagramInternalFrame frame = ApplicationContext.getDefaultMainFrame()
                .getSelectedDiagramInternalFrame();
        if (frame == null || frame == diagFrame)
            return;
        deinstallDiagramView();
        diagFrame = frame;
        createViewComponent(diagFrame.getDiagram().getMainView());
        getContentPane().add(viewComponent);
        validate();
        diagFrame.addInternalFrameListener(internalFrameListener);
    }

    protected abstract void createViewComponent(DiagramView diagView);
}
