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

package org.modelsphere.sms.plugins.report.screen;

// JDK
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

import org.modelsphere.sms.plugins.report.LocaleMgr;

// Sms

public class FileEditor extends JPanel implements Editor, ActionListener {

    private org.modelsphere.jack.awt.AbstractTableCellEditor editor;
    private JTextField textField;
    private JButton button;

    private ReportPropertiesFrame parent;

    public FileEditor(ReportPropertiesFrame parent) {
        this.parent = parent;
    }

    public final Component getTableCellEditorComponent(/* ScreenView screenView *//*
                                                                                   * ReportPropertiesFrame
                                                                                   * frame
                                                                                   */JTable table,
            org.modelsphere.jack.awt.AbstractTableCellEditor tableCellEditorListener, Object value,
            boolean isSelected, int row, int column) {

        this.editor = tableCellEditorListener;
        textField = new JTextField(value.toString());
        button = new JButton("...");
        button.setPreferredSize(new Dimension(20, 20));
        textField.addActionListener(this);
        button.addActionListener(this);
        setLayout(new BorderLayout());
        add(textField, BorderLayout.CENTER);
        add(button, BorderLayout.EAST);
        return this;
    }

    public final boolean stopCellEditing() {
        return true;
    }

    public final Object getCellEditorValue() {
        /*
         * Object value = null;//getText();
         * 
         * return value;
         */
        return new File(textField.getText());
    }

    public final void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            JFileChooser fc = new JFileChooser(textField.getText());
            fc.setSelectedFile(new File(textField.getText()));
            //fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (JFileChooser.APPROVE_OPTION == fc.showDialog(this, LocaleMgr.misc
                    .getString("Select"))) {
                File file = fc.getSelectedFile();
                if (file != null)
                    textField.setText(file.getAbsolutePath());
            }
        }

        editor.stopCellEditing();
    }

}
