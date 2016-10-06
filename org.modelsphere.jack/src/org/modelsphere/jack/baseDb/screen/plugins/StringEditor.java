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
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import org.modelsphere.jack.awt.AbstractTableCellEditor;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.screen.*;
import org.modelsphere.jack.baseDb.screen.model.ScreenModel;

// Yhis editor may work with the DefaultRenderer or the DbSemObjFullNameRenderer
public class StringEditor extends JTextField implements Editor, DocumentListener {

    private ScreenView screenView;
    private DbObject parentDbo;
    private Renderer renderer;

    public final Component getTableCellEditorComponent(ScreenView screenView,
            AbstractTableCellEditor tableCellEditorListener, Object value, boolean isSelected,
            int row, int column) {
        this.screenView = screenView;
        ScreenModel model = screenView.getModel();
        parentDbo = (DbObject) model.getParentValue(row);
        renderer = model.getRenderer(row, column);
        String text = (String) renderer.unwrapValue(value);
        if (text != null)
            setText(text);
        addActionListener(tableCellEditorListener);
        getDocument().addDocumentListener(this);
        return this;
    }

    public final boolean stopCellEditing() {
        return true;
    }

    public final Object getCellEditorValue() {
        Object value = getText();
        if (renderer != DefaultRenderer.singleton) {
            try {
                parentDbo.getDb().beginTrans(Db.READ_TRANS);
                value = renderer.wrapValue(parentDbo, value);
                parentDbo.getDb().commitTrans();
            } catch (DbException e) {
            }
        }
        return value;
    }

    // /////////////////////////////////
    // DocumentListener SUPPORT
    //
    public final void insertUpdate(DocumentEvent e) {
        screenView.setHasChanged();
    }

    public final void removeUpdate(DocumentEvent e) {
        screenView.setHasChanged();
    }

    public final void changedUpdate(DocumentEvent e) {
    }

    //
    // End of DocumentListener SUPPORT
    // /////////////////////////////////

    @Override
    public int getClickCountForEditing() {
        return 2;
    }
}
