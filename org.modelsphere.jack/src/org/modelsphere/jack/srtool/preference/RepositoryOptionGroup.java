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

package org.modelsphere.jack.srtool.preference;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.preference.*;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public class RepositoryOptionGroup extends OptionGroup {
    private static class RepositoryOptionPanel extends OptionPanel implements ActionListener,
            FocusListener, DocumentListener {
        private JLabel connectionStringLabel = new JLabel(LocaleMgr.screen
                .getString("connectionString"));
        private JTextField connectionString = new JTextField("");

        RepositoryOptionPanel() {
            setLayout(new GridBagLayout());

            add(connectionStringLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(12, 12, 12, 6), 0,
                    0));
            add(connectionString, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 12,
                            12), 100, 0));
            add(Box.createHorizontalGlue(), new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0,
                    GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
            // add(defButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            // ,GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new
            // Insets(0, 0, 6, 6), 0, 0));

            // defButton.addActionListener(this);
            connectionString.addFocusListener(this);
            connectionString.getDocument().addDocumentListener(this);
        }

        public void init() {
            PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
            connectionString.setText(preferences.getPropertyString(Db.class,
                    Db.PROPERTY_REPOSITORY_CONNECTION_STRING,
                    Db.PROPERTY_REPOSITORY_CONNECTION_STRING_DEFAULT));
        }

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            // if (source == defButton){
            // }
        }

        public void focusGained(FocusEvent e) {
        }

        public void focusLost(FocusEvent e) {
            if (e.getSource() == connectionString) {
                validatePortValue();
            }
        }

        public void insertUpdate(DocumentEvent e) {
            validatePortValue();
        }

        public void removeUpdate(DocumentEvent e) {
            validatePortValue();
        }

        private void validatePortValue() {
            PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
            fireOptionChanged(preferences, Db.class, Db.PROPERTY_REPOSITORY_CONNECTION_STRING,
                    connectionString.getText());
        }

        public void changedUpdate(DocumentEvent e) {
        }
    };

    public RepositoryOptionGroup() {
        super(LocaleMgr.misc.getString("Repository"));
    }

    protected OptionPanel createOptionPanel() {
        return new RepositoryOptionPanel();
    }

}
