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

import java.awt.BorderLayout;
import java.awt.Container;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.*;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.event.DbListener;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.ApplicationContext;

public class DiagramInternalFrame extends CascadingJInternalFrame implements
        PropertyChangeListener, DbRefreshListener, DbListener {

    private static final String POINTCASCADENAME = "DIAGRAM_INTERNAL_FRAME"; // NOT LOCALIZABLE, property key

    private ApplicationDiagram model;

    public DiagramInternalFrame(ApplicationDiagram model) throws DbException {
        super("", POINTCASCADENAME, true);
        this.model = model;
        DiagramView view = model.getMainView();
        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
        view.setZoomFactor(preferences.getPropertyFloat(DiagramView.class,
                DiagramView.ZOOM_FACTOR_PROPERTY,
                new Float(DiagramView.ZOOM_FACTOR_PROPERTY_DEFAULT)).floatValue());

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(view), BorderLayout.CENTER);
        ((JViewport) view.getParent()).setScrollMode(JViewport.BLIT_SCROLL_MODE);
        // ((JComponent)view.getParent()).putClientProperty("EnableWindowBlit",
        // Boolean.TRUE); //NOT LOCALIZABLE property

        ApplicationContext.getDragDrop().enableDrop(view, getGlassPane());

        Icon ico = null;
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        if (terminologyUtil.isDataModel(model.semObj)) {
            if (terminologyUtil.getModelLogicalMode(model.semObj) == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                ico = terminologyUtil.getConceptualModelIcon();
            else
                ico = model.getSemanticalObject().getSemanticalIcon(DbObject.SHORT_FORM);
        } else {
            DbObject semObj = model.getSemanticalObject();
            ico = terminologyUtil.getDiagramIcon(model);
        }

        if (ico != null)
            setFrameIcon(ico);

        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addPropertyChangeListener(this);
        refreshTitle();
        addTriggers();
    }

    public final ApplicationDiagram getDiagram() {
        return model;
    }

    public final void propertyChange(PropertyChangeEvent ev) {
        if (ev.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY)
                && ((Boolean) ev.getNewValue()).booleanValue()) {
            removeTriggers();
            model.delete();
        }
    }

    public final void refreshTitle() throws DbException {
        String packName = model.getSemanticalObject().getFullDisplayName();
        String diagName = model.getDiagramGO().getName();
        setTitle(packName + ": " + diagName); // NOT LOCALIZABLE
    }

    private void addTriggers() {
        model.getDiagramGO().addDbRefreshListener(this);
        model.getSemanticalObject().addDbRefreshListener(this);
        Db.addDbListener(this);
    }

    private void removeTriggers() {
        model.getDiagramGO().removeDbRefreshListener(this);
        model.getSemanticalObject().removeDbRefreshListener(this);
        Db.removeDbListener(this);
    }

    public final void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
        if (event.dbo == model.getDiagramGO() && event.dbo.getTransStatus() == Db.OBJ_REMOVED) {
            close();
        } else if (event.metaField == DbSemanticalObject.fName) {
            refreshTitle();
        } else if (event.metaField == DbGraphic.fDiagramName) {
            refreshTitle();
        }
    }

    // db has been created.
    public void dbCreated(Db db) {
    }

    // db has been terminated. This is not possible to use this db.
    public void dbTerminated(Db db) {
        if (model.getDiagramGO() != null && model.getDiagramGO().getDb() == db) {
            close();
        }
    }

    public final void refresh() throws DbException {
        if (!model.getDiagramGO().isAlive()) {
            close();
            return;
        }
        refreshTitle();
        model.populateContent();
    }

    /**
     * To close the window, you must call this method (do not call dispose).
     */
    public final void close() {
        removeTriggers();
        try {
            setClosed(true);
        } catch (java.beans.PropertyVetoException e) {
        } // should not happen
    }
}
