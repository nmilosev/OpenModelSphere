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

package org.modelsphere.sms.or.features.dbms;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.modelsphere.sms.or.international.LocaleMgr;

public class DatabaseNamePanel extends JPanel {
    private static final String LABEL_NAME = LocaleMgr.screen.getString("DatabaseForward"); // NOT LOCALIZABLE
    private JLabel databaseNameLabel = new JLabel(LABEL_NAME);
    private JTextField databaseTextField = new JTextField();

    public DatabaseNamePanel() {
        initGUI();
    }

    public DatabaseNamePanel(String name) {
        initGUI();
        setDatabaseName(name);
    }

    private void initGUI() {
        databaseTextField.setEditable(false);
        this.setLayout(new GridBagLayout());
        this
                .add(databaseNameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(6, 6, 12, 12), 0, 0));
        this.add(databaseTextField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(6, 0, 12, 6),
                0, 0));
    }

    public String getDatabaseName() {
        return databaseTextField.getText();
    }

    public void setDatabaseName(String name) {
        databaseTextField.setText(name);
    }
}
