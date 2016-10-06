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

package org.modelsphere.jack.baseDb.screen.plugins;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.ArrayList;
import javax.swing.JButton;

import org.modelsphere.jack.awt.AbstractTableCellEditor;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.screen.*;
import org.modelsphere.jack.baseDb.screen.model.ScreenModel;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.util.DefaultComparableElement;

public abstract class DbSemanticalObjectEditor implements Editor, ActionListener {

    private AbstractTableCellEditor editor;
    private Object value;
    private DbObject parentDbo;
    private Renderer renderer;
    private Component comp;

    public final Component getTableCellEditorComponent(ScreenView screenView,
            AbstractTableCellEditor editor, Object value, boolean isSelected, int row, int column) {
        this.editor = editor;
        this.value = value;
        ScreenModel model = screenView.getModel();
        parentDbo = (DbObject) model.getParentValue(row);
        renderer = model.getRenderer(row, column);
        JButton button = new JButton(value.toString());
        button.addActionListener(this);
        button.setBackground(screenView.getSelectionBackground());

        comp = button;
        return comp;
    }

    public final void actionPerformed(ActionEvent e) {
        try {
            Db db = parentDbo.getDb();
            db.beginTrans(Db.READ_TRANS);
            Collection dbos = getSelectionSet(parentDbo);
            DbObject currentDbo = (DbObject) renderer.unwrapValue(value);
            // <selectDbo> takes care of closing the transaction.
            DefaultComparableElement item = DbLookupDialog.selectDbo(comp, getTitle(), null, db,
                    dbos, currentDbo, getStringForNullValue(), isDisplayFullName());
            if (item != null) {
                db.beginTrans(Db.READ_TRANS);
                value = renderer.wrapValue(parentDbo, item.object);
                db.commitTrans();
            }
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(comp, ex);
        }
        editor.stopCellEditing();
    }

    public final boolean stopCellEditing() {
        return true;
    }

    public final Object getCellEditorValue() {
        return value;
    }

    /*
     * Following methods are overwrittable by subclasses.
     */
    protected String getStringForNullValue() {
        return DefaultRenderer.kUnspecified;
    }

    protected Collection getSelectionSet(DbObject parentDbo) throws DbException {
        ArrayList dbos = new ArrayList();
        DbEnumeration dbEnum = getSelectionEnum(parentDbo);
        if (dbEnum != null) {
            while (dbEnum.hasMoreElements())
                dbos.add(dbEnum.nextElement());
            dbEnum.close();
        }
        return dbos;
    }

    protected DbEnumeration getSelectionEnum(DbObject parentDbo) throws DbException {
        return null;
    }

    protected boolean isDisplayFullName() {
        return true;
    }

    protected String getTitle() {
        return LocaleMgr.screen.getString("SelectNewCellValue");
    }

    @Override
    public int getClickCountForEditing() {
        return 2;
    }
}
