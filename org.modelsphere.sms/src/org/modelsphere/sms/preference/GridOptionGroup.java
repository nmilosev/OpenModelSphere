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

package org.modelsphere.sms.preference;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.modelsphere.jack.awt.NumericTextField;
import org.modelsphere.jack.graphic.Grid;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.preference.OptionPanel;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.international.LocaleMgr;

public class GridOptionGroup extends OptionGroup {
    static final String GRID_OPTIONS = LocaleMgr.misc.getString("GridOptions");

    private static class GridOptionPanel extends OptionPanel implements ActionListener,
            FocusListener, DocumentListener {
        private JLabel portNumberLabel = new JLabel(LocaleMgr.misc.getString("StartingPortNumber"));
        private JTextField portNumber = new NumericTextField(NumericTextField.INTEGER);
        private JButton defButton = new JButton(LocaleMgr.misc.getString("Default"));

        GridOptionPanel() {
            setLayout(new GridBagLayout());

            add(portNumberLabel,
                    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                            GridBagConstraints.NONE, new Insets(0, 0, 12, 6), 0, 0));
            add(portNumber, new GridBagConstraints(1, 2, 1, 1, 0.2, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(0, 0, 12, 5), 240, 0));
            add(Box.createVerticalGlue(), new GridBagConstraints(0, 3, 2, 1, 1.0, 1.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            add(defButton, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                    GridBagConstraints.NONE, new Insets(0, 0, 6, 5), 0, 0));

            portNumber.addFocusListener(this);
            portNumber.getDocument().addDocumentListener(this);
            defButton.addActionListener(this);
        }

        public void init() {
            PropertiesSet options = PropertiesManager.APPLICATION_PROPERTIES_SET;
            Integer value = options.getPropertyInteger(Grid.class, Grid.PROPERTY_NB_OF_CELLS_IN_X,
                    new Integer(Grid.PROPERTY_NB_OF_CELLS_IN_X_DEFAULT));
            portNumber.setText(value.toString());
        }

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == defButton) {
                portNumber.setText(new Integer(Grid.PROPERTY_NB_OF_CELLS_IN_X_DEFAULT).toString());
                fireOptionChanged(PropertiesManager.APPLICATION_PROPERTIES_SET,
                        GridOptionGroup.class, Grid.PROPERTY_NB_OF_CELLS_IN_X, new Integer(
                                Grid.PROPERTY_NB_OF_CELLS_IN_X_DEFAULT));
            }
        }

        public void focusGained(FocusEvent e) {
        }

        public void focusLost(FocusEvent e) {
            if (e.getSource() == portNumber) {
                int value = new Integer(portNumber.getText()).intValue();
                if (value < 1025 || value > 65529) { // Usually 65534 but we use
                    // 6 ports starting with
                    // the provide number
                    JOptionPane.showMessageDialog(this, LocaleMgr.message
                            .getString("invalidPortNumber"), ApplicationContext.getApplicationName(),
                            JOptionPane.ERROR_MESSAGE);
                    portNumber
                            .setText((PropertiesManager.APPLICATION_PROPERTIES_SET
                                    .getPropertyInteger(Grid.class, Grid.PROPERTY_NB_OF_CELLS_IN_X,
                                            new Integer(Grid.PROPERTY_NB_OF_CELLS_IN_X_DEFAULT))
                                    .toString()));
                }
            }
        }

        public void insertUpdate(DocumentEvent e) {
            validatePortValue();
        }

        public void removeUpdate(DocumentEvent e) {
            validatePortValue();
        }

        private void validatePortValue() {
            if (portNumber.getText().length() == 0)
                return;
            Integer value = new Integer(portNumber.getText());
            if (value.intValue() < 1025 || value.intValue() > 65529) { // Usually
                // 65534
                // but
                // we
                // use 6
                // ports
                // starting
                // with
                // the
                // provide
                // number
                return;
            }
            fireOptionChanged(PropertiesManager.APPLICATION_PROPERTIES_SET, GridOptionGroup.class,
                    Grid.PROPERTY_NB_OF_CELLS_IN_X, value);
        }

        public void changedUpdate(DocumentEvent e) {
        }
    };

    public GridOptionGroup() {
        super(GRID_OPTIONS);
    }

    protected OptionPanel createOptionPanel() {
        return new GridOptionPanel();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GridOptionGroup"); // NOT LOCALIZABLE, unit
        // test
        GridOptionGroup group = new GridOptionGroup();
        OptionPanel panel = group.createOptionPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    } // end main()

} // end GridOptionGroup

