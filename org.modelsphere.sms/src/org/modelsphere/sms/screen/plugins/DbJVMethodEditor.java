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

package org.modelsphere.sms.screen.plugins;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import org.modelsphere.jack.awt.AbstractTableCellEditor;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.screen.DbTreeLookupDialog;
import org.modelsphere.jack.baseDb.screen.DefaultRenderer;
import org.modelsphere.jack.baseDb.screen.Editor;
import org.modelsphere.jack.baseDb.screen.Renderer;
import org.modelsphere.jack.baseDb.screen.ScreenView;
import org.modelsphere.jack.baseDb.screen.model.ScreenModel;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.java.db.DbJVMethod;

public final class DbJVMethodEditor implements Editor, ActionListener {

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
            DbObject currentDbo = (DbObject) renderer.unwrapValue(value);
            Object selItem = DbTreeLookupDialog.selectOne(comp, LocaleMgr.screen
                    .getString("SelectNewCellValue"), new DbObject[] { parentDbo.getProject() },
                    new MetaClass[] { DbJVMethod.metaClass }, null, DefaultRenderer.kUnspecified,
                    currentDbo);
            if (selItem != null) {
                parentDbo.getDb().beginTrans(Db.READ_TRANS);
                value = renderer.wrapValue(parentDbo,
                        (selItem instanceof DbObject ? selItem : null));
                parentDbo.getDb().commitTrans();
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

    @Override
    public int getClickCountForEditing() {
        return 2;
    }
}
