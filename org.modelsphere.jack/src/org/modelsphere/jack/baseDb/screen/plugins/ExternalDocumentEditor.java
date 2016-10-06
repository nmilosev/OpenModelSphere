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
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import org.modelsphere.jack.awt.AbstractTableCellEditor;
import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.choosers.ExternalDocumentDialog;
import org.modelsphere.jack.baseDb.screen.Editor;
import org.modelsphere.jack.baseDb.screen.ScreenView;
import org.modelsphere.jack.baseDb.screen.model.ScreenModel;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExternalDocumentEditor implements Editor, ActionListener {
    private AbstractTableCellEditor editor;
    private ScreenView screenView;
    private JButton actionBtn = new JButton("");
    private String m_externalDocument;
    private int row;
    private static final String EXTERNAL_DOCUMENT = LocaleMgr.screen.getString("ExternalDocument");

    // implements Editor
    public final boolean stopCellEditing() {
        return true;
    }

    // implements Editor
    public final Object getCellEditorValue() {
        return m_externalDocument;
    }

    public ExternalDocumentEditor() {
        int i = 0;
    }

    // implements Editor
    public final Component getTableCellEditorComponent(ScreenView screenView,
            AbstractTableCellEditor editor, Object value, boolean isSelected, int row, int column) {
        this.editor = editor;
        this.screenView = screenView;
        this.row = row;

        if (value instanceof String) {
            m_externalDocument = (String) value;
            if (m_externalDocument != null) {
                actionBtn.setText(m_externalDocument);
            }
        } // end if
        actionBtn.addActionListener(this);
        return actionBtn;
    } // end getTableCellEditorComponent()

    // implements ActionListener
    public final void actionPerformed(ActionEvent ev) {
        if (m_externalDocument == null) {
            ScreenModel model = screenView.getModel();
            Object obj = model.getRenderer(row, 1);
            if (obj instanceof ExternalDocumentRenderer) {
                ExternalDocumentRenderer renderer = (ExternalDocumentRenderer) obj;
                m_externalDocument = (String) renderer.getValue();
            }
        }

        DefaultMainFrame mf = ApplicationContext.getDefaultMainFrame();
        m_externalDocument = doActionPerformed(mf, row, m_externalDocument, actionBtn);
    } // end actionPerformed()

    //
    // protected methods
    //
    protected static String doActionPerformed(Frame parent, int row, String externalDocument,
            JButton button) {
        ExternalDocumentDialog dialog = new ExternalDocumentDialog(parent, EXTERNAL_DOCUMENT, true,
                externalDocument);
        AwtUtil.centerWindow(dialog);
        dialog.setVisible(true);
        String command = dialog.getCommand();
        return command;
    } // end doActionPerformed()

    @Override
    public int getClickCountForEditing() {
        return 2;
    }

} // end ExternalDocumentEditor()
