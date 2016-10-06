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

package org.modelsphere.jack.srtool.list;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.event.DbListener;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.SemanticalModel;
import org.modelsphere.jack.srtool.graphic.CascadingJInternalFrame;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;

public class ListInternalFrame extends CascadingJInternalFrame implements DbRefreshListener,
        DbListener {
    private static final String kListTitle01 = LocaleMgr.screen.getString("ListTitle01");

    private ListTable table;
    private JScrollPane scrollPane;

    private DbObject root;
    private DbProject project;
    private MetaClass neighborsMetaClass;
    Boolean bIsRelationships = new Boolean(false);

    public Boolean isRelationships() {
        return bIsRelationships;
    }

    public ListInternalFrame(ListTableModel model, Boolean bIsRelationships) throws DbException {
        super();
        this.bIsRelationships = bIsRelationships;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(true);
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        this.root = model.root;
        this.neighborsMetaClass = model.neighborsMetaClass;

        // set default icon
        Icon icon = model.descriptor.getIcon();
        if (icon == null)
            icon = new ImageIcon(ApplicationContext.APPLICATION_IMAGE_ICON);
        setFrameIcon(icon);
        getContentPane().setLayout(new BorderLayout());
        table = new ListTable(model);

        scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(UIManager.getColor("Table.background")); // NOT LOCALIZABLE
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        project = root.getProject();
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting())
                    return;
                ApplicationContext.getFocusManager().selectionChanged(ListInternalFrame.this);
            }
        });

        root.addDbRefreshListener(this);
        Db.addDbListener(this);
        refreshTitle();
    }

    private void refreshTitle() throws DbException {
        ListTableModel model = table.getListTableModel();

        SemanticalModel SM = ApplicationContext.getSemanticalModel();
        String concept = model.descriptor.toString();
        String title = MessageFormat.format(kListTitle01, new Object[] {
                root.getSemanticalName(DbObject.LONG_FORM), concept });
        if (title == null)
            title = "";
        setTitle(title);
    }

    public void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
        if (event.dbo == root && (event.dbo.getTransStatus() == Db.OBJ_REMOVED)) {
            dispose();
        } else if (event.dbo == root && event.metaField == DbSemanticalObject.fName) {
            refreshTitle();
        }
    }

    // db has been created.
    public void dbCreated(Db db) {
    }

    // db has been terminated. This is not possible to use this db.
    public void dbTerminated(Db db) {
        if (root == null)
            return;
        if (root.getDb() == db) {
            dispose();
        }
    }

    public void refresh() throws DbException {
        if (!root.isAlive()) {
            dispose();
        }
        if (table == null)
            return;
        ListTableModel model = table.getListTableModel();
        if (model == null)
            return;
        model.refresh();
    }

    public DbObject getSemanticalObject() {
        return root;
    }

    public DbProject getProject() {
        return project;
    }

    public Object[] getSelection() {
        if (table == null)
            return new Object[0];
        ListTableModel model = (ListTableModel) table.getModel();
        if (model == null)
            return new Object[0];
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows == null || selectedRows.length == 0)
            return new Object[0];
        ArrayList selection = new ArrayList();
        for (int i = 0; i < selectedRows.length; i++) {
            selection.add(model.getDbObjectAt(selectedRows[i]));
        }
        Object[] selarray = new Object[selection.size()];
        for (int i = 0; i < selarray.length; i++) {
            selarray[i] = selection.get(i);
        }
        return selarray;
    }

    public MetaClass getNeighborsMetaClass() {
        return neighborsMetaClass;
    }

    public void dispose() {
        if (root != null) {
            root.removeDbRefreshListener(this);
        }
        Db.removeDbListener(this);
        ListTableModel model = (ListTableModel) table.getModel();
        model.removeDbListeners();
        table.uninstall();
        super.dispose();
    }

    // Allow install or uninstall of refresh listener for the list
    public void lockGUI() {
        if (table == null || table.getListTableModel() == null)
            return;
        table.getListTableModel().lockGUI();
    }

    public void unlockGUI() {
        if (root.getTransStatus() == Db.OBJ_REMOVED) {
            dispose();
        }
        if (table == null || table.getListTableModel() == null)
            return;
        table.getListTableModel().unlockGUI();
    }

    public void updateUI() {
        super.updateUI();

        if (scrollPane != null && scrollPane.getViewport() != null) {
            scrollPane.getViewport().setBackground(UIManager.getColor("Table.background")); // NOT LOCALIZABLE
        }
    }

}
