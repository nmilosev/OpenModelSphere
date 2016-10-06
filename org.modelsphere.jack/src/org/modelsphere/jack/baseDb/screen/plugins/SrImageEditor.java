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
import java.io.*;
import java.awt.event.*;
import javax.swing.*;

import org.modelsphere.jack.awt.AbstractTableCellEditor;
import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.awt.ImagePreviewer;
import org.modelsphere.jack.baseDb.screen.*;
import org.modelsphere.jack.graphic.GraphicUtil;

public class SrImageEditor implements Editor, ActionListener {

    private AbstractTableCellEditor editor;
    private ScreenView screenView;
    private JButton actionBtn = new JButton("");
    private Image image;

    public static File g_lastVisitedFile = null; // only used in the following

    // block

    public final Component getTableCellEditorComponent(ScreenView screenView,
            AbstractTableCellEditor editor, Object value, boolean isSelected, int row, int column) {
        this.editor = editor;
        this.screenView = screenView;
        if (value instanceof Image) {
            image = (Image) value;
        }
        actionBtn.addActionListener(this);
        actionBtn.setBackground(screenView.getSelectionBackground());
        return actionBtn;
    }

    public final void actionPerformed(ActionEvent ev) {
        JFileChooser fc = (g_lastVisitedFile == null) ? new JFileChooser() : new JFileChooser(
                g_lastVisitedFile);
        fc.setFileFilter(ExtensionFileFilter.allImagesFilter);
        fc.setAccessory(new ImagePreviewer(fc));

        Dimension dim = fc.getPreferredSize();
        fc.setPreferredSize(new Dimension(dim.width, dim.height + 75));

        int returnVal = fc.showOpenDialog(screenView);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            g_lastVisitedFile = fc.getSelectedFile();
            String filename = g_lastVisitedFile.getAbsolutePath();
            image = Toolkit.getDefaultToolkit().getImage(filename);
            // We must wait for the image to complete loading otherwise Db will
            // capture the loaded part of the image in SrImage.
            GraphicUtil.waitForImage(image);
        } // end if

        actionBtn.removeActionListener(this);
        editor.stopCellEditing();
    }

    public final boolean stopCellEditing() {
        return true;
    }

    public final Object getCellEditorValue() {
        return image;
    }

    @Override
    public int getClickCountForEditing() {
        return 2;
    }
}
