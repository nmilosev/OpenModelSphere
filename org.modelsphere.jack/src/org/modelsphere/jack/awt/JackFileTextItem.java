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

package org.modelsphere.jack.awt;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.io.IoUtil;

public class JackFileTextItem extends JPanel implements ActionListener {

    private JTextField fileNameField;
    private JButton fileChooserButton;

    // TO DO Parametre au constructeur pour choisir un fichier ou un repertoire
    // ou les 2 si null
    public JackFileTextItem() {
        fileNameField = new JTextField();
        fileNameField.setEditable(false);
        fileChooserButton = new JButton("..."); // NOT LOCALIZABLE
        fileChooserButton.addActionListener(this);

        this.setLayout(new GridBagLayout());
        add(fileNameField, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 6), 0, 0));
        add(fileChooserButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0,
                -6));
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == fileChooserButton) {
            JFrame frame = (JFrame) AwtUtil.getParentFrame(this);
            String defaultDirectory = fileNameField.getText();
            String title = LocaleMgr.screen.getString("SelectDir");
            String approve = LocaleMgr.screen.getString("Select");
            File chosenDirectory = IoUtil.selectDirectory(frame, defaultDirectory, title, approve);
            if (chosenDirectory != null) {
                updatedFileFieldPerformed(chosenDirectory.getAbsolutePath());
            }
        }
    }

    // The subclass has the chance to customize the update behavior
    protected void updatedFileFieldPerformed(String value) {
        setFileNameFieldText(value);
    }

    public final void setFileNameFieldText(String value) {
        fileNameField.setText(value);
    }

}
