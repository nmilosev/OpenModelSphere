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
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JComboBox;

import org.modelsphere.jack.awt.AbstractTableCellEditor;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.screen.*;
import org.modelsphere.jack.baseDb.screen.model.ScreenModel;
import org.modelsphere.jack.util.DefaultComparableElement;

public abstract class DbSemanticalObjectShortEditor implements Editor {

    private Object oldValue;
    private DbObject parentDbo;
    private Renderer renderer;
    private Component comp = null;

    public final Component getTableCellEditorComponent(ScreenView screenView,
            AbstractTableCellEditor editor, Object value, boolean isSelected, int row, int column) {
        oldValue = value;
        ScreenModel model = screenView.getModel();
        parentDbo = (DbObject) model.getParentValue(row);
        renderer = model.getRenderer(row, column);
        try {
            JComboBox combo = new JComboBox();
            parentDbo.getDb().beginTrans(Db.READ_TRANS);
            DbObject currentDbo = (DbObject) renderer.unwrapValue(value);
            int selInd = -1;
            Collection dbos = getSelectionSet(parentDbo);
            Iterator iter = dbos.iterator();
            while (iter.hasNext()) {
                DbObject dbo = (DbObject) iter.next();
                combo.addItem(new DefaultComparableElement(dbo, dbo.getName(), getUserObject(dbo)));
                if (dbo == currentDbo)
                    selInd = combo.getItemCount() - 1;
            }
            parentDbo.getDb().commitTrans();

            combo.addItem(new DefaultComparableElement(null, getStringForNullValue()));
            combo.setMaximumRowCount(10);
            combo.setSelectedIndex(selInd != -1 ? selInd : combo.getItemCount() - 1);
            combo.addActionListener(editor);
            comp = combo;
            configureJComboBox(combo);
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(screenView, ex);
        }
        return comp;
    }

    public final boolean stopCellEditing() {
        return true;
    }

    public final Object getCellEditorValue() {
        try {
            parentDbo.getDb().beginTrans(Db.READ_TRANS);
            Object value = ((DefaultComparableElement) ((JComboBox) comp).getSelectedItem()).object;
            value = renderer.wrapValue(parentDbo, value);
            parentDbo.getDb().commitTrans();
            return value;
        } catch (Exception ex) {
            return oldValue;
        }
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

    protected Object getUserObject(DbObject dbo) throws DbException {
        return null;
    }

    protected void configureJComboBox(JComboBox combo) {
    }

    @Override
    public int getClickCountForEditing() {
        return 2;
    }
}
