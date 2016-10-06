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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.modelsphere.jack.awt.AbstractTableCellEditor;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.screen.*;
import org.modelsphere.jack.baseDb.screen.model.*;

public class LookupDescriptionEditor implements Editor, ActionListener {

    public static final Icon kTextToWrite = org.modelsphere.jack.srtool.international.LocaleMgr.screen
            .getImageIcon("TextToWrite");
    public static final Icon kTextWritten = org.modelsphere.jack.srtool.international.LocaleMgr.screen
            .getImageIcon("TextWritten");

    private AbstractTableCellEditor editor;
    private String title = "";
    private MetaField mf;
    private String text;
    private ScreenView screenView;
    private JPanel panel;
    private JLabel label = new JLabel();
    private JLabel labelSpace = new JLabel(" ");
    private JButton textEditorBtn = new JButton();

    public final Component getTableCellEditorComponent(ScreenView screenView,
            AbstractTableCellEditor editor, Object value, boolean isSelected, int row, int column) {
        this.editor = editor;
        this.screenView = screenView;
        text = (value == null ? "" : (String) value);
        panel = new JPanel(new BorderLayout());
        textEditorBtn.setName("TextEditorBtn"); // NOT LOCALIZABLE - For QA
        if (screenView instanceof DescriptionView) {
            panel.add(labelSpace, BorderLayout.WEST);
            panel.add(label, BorderLayout.CENTER);
            panel.add(textEditorBtn, BorderLayout.EAST);
        } else if (screenView instanceof ListView) {
            panel.remove(label);
            panel.remove(labelSpace);
            panel.add(textEditorBtn, BorderLayout.CENTER);
        }

        ScreenModel model = screenView.getModel();
        if (model instanceof DbDescriptionModel) {
        	DbDescriptionModel descriptionModel = (DbDescriptionModel)model;
        	title = descriptionModel.getGUIName(row);
        	
        	DescriptionField df = descriptionModel.getDescriptionField(row); 
        	if (df instanceof MetaFieldDescriptionField) {
        		MetaFieldDescriptionField mfdf = (MetaFieldDescriptionField)df;
        		mf = mfdf.getMetaField();
        	}
        } else if (model instanceof DbListModel) {
            title = ((DbListModel) model).getColumnName(column);
        }
        textEditorBtn.addActionListener(this);

        label.setText(text);
        if (text == null) {
            textEditorBtn.setIcon(kTextToWrite);
        } else {
            textEditorBtn.setIcon(text.length() > 0 ? kTextWritten : kTextToWrite);
        }

        textEditorBtn.setBackground(screenView.getSelectionBackground());
        label.setForeground(screenView.getSelectionForeground());
        label.setFont(screenView.getFont());
        panel.setBackground(screenView.getSelectionBackground());

        return panel;
    }

    public final void actionPerformed(ActionEvent ev) {
        String mfName = (mf == null) ? null : mf.getJName();
        boolean doCheckSpell = "m_description".equals(mfName) || "Description".equals(title);
        TextEditorDialog descrDialog = new TextEditorDialog(screenView, text, title, doCheckSpell);
        descrDialog.setVisible(true);
        if (descrDialog.isTextModified()) {
            text = descrDialog.getText();
        }
        editor.stopCellEditing();
    }

    public final boolean stopCellEditing() {
        return true;
    }

    public final Object getCellEditorValue() {
        return text;
    }

    @Override
    public int getClickCountForEditing() {
        return 2;
    }

}
