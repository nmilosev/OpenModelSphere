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

import java.io.File;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JFileChooser;

import org.modelsphere.jack.awt.AbstractTableCellEditor;
import org.modelsphere.jack.baseDb.screen.ScreenView;
import org.modelsphere.jack.international.LocaleMgr;

/* toDo: we need a mean to parameterize this editor
 (display only directories, display files with certain exrensions, etc...).
 By default, we display only directories.
 */
public class FileNameEditor extends JPanel implements org.modelsphere.jack.baseDb.screen.Editor,
        ActionListener {

    private ScreenView screenView;
    private AbstractTableCellEditor editor;
    private JTextField textField;
    private JButton button;

    public final Component getTableCellEditorComponent(ScreenView screenView,
            AbstractTableCellEditor editor, Object value, boolean isSelected, int row, int column) {
        this.screenView = screenView;
        this.editor = editor;
        textField = new JTextField((String) value);
        button = new JButton("...");
        button.setPreferredSize(new Dimension(20, 20));
        textField.addActionListener(this);
        button.addActionListener(this);
        setLayout(new BorderLayout());
        add(textField, BorderLayout.CENTER);
        add(button, BorderLayout.EAST);

        return this;
    }

    public final void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            JFileChooser fc = new JFileChooser(textField.getText());
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (JFileChooser.APPROVE_OPTION == fc.showDialog(this, LocaleMgr.screen
                    .getString("Select"))) {
                File file = fc.getSelectedFile();
                if (file != null)
                    textField.setText(file.getAbsolutePath());
            }
        }
        editor.stopCellEditing();
    }

    public final boolean stopCellEditing() {
        return true;
    }

    public final Object getCellEditorValue() {
        return textField.getText();
    }

    @Override
    public int getClickCountForEditing() {
        return 2;
    }
}
