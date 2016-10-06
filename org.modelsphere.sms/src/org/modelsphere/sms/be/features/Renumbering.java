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

package org.modelsphere.sms.be.features;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.NumericTextField;
import org.modelsphere.jack.debug.TestableWindow;
import org.modelsphere.sms.be.international.LocaleMgr;

public final class Renumbering extends JDialog implements TestableWindow {
    private static final String COUNTER = "counter"; // NOT LOCALIZABLE,
    // property name
    private static final String INCREMENT = "increment"; // NOT LOCALIZABLE,
    // property name
    private static final String RENUMBER_FROM = LocaleMgr.misc.getString("renumberFrom");
    private static final String INCREMENT_BY = LocaleMgr.misc.getString("incrementBy");
    private static final String RENUMBER_FROM_DESC = LocaleMgr.misc.getString("renumberFromDesc");
    private static final String INCREMENT_BY_DESC = LocaleMgr.misc.getString("incrementByDesc");
    private static final String TITLE = LocaleMgr.action.getString("RenumberingParameters");
    private static final String CANCEL = org.modelsphere.jack.international.LocaleMgr.screen
            .getString("Cancel");
    private static final String OK = org.modelsphere.jack.international.LocaleMgr.screen
            .getString("OK");
    private static final String DEFAULT = LocaleMgr.screen.getString("default");

    private static final Integer DEFAULT_COUNTER = new Integer(0);
    private static final Integer DEFAULT_INCREMENT = new Integer(1);

    private Integer m_counter = DEFAULT_COUNTER; // default value
    private Integer m_increment = DEFAULT_INCREMENT; // default value

    private static Renumbering g_singleInstance = null;

    private Renumbering() {
    }

    public static Renumbering getSingleInstance() {
        if (g_singleInstance == null)
            g_singleInstance = new Renumbering();
        return g_singleInstance;
    }

    //
    // INNER CLASSES
    //
    private class RenumberingDialog extends JDialog implements ActionListener {

        private JButton okButton = new JButton(OK);
        private JButton cancelButton = new JButton(CANCEL);
        private JButton defaultButton = new JButton(DEFAULT);

        private JLabel counterLabel = new JLabel(RENUMBER_FROM);
        private JLabel incrementLabel = new JLabel(INCREMENT_BY);
        private JTextField counterField = new NumericTextField(NumericTextField.INTEGER, false);
        private JTextField incrementField = new NumericTextField(NumericTextField.INTEGER, false);

        RenumberingDialog(JFrame owner) {
            super(owner, TITLE, true);
            initGUI();
        }

        private void initGUI() {
            getContentPane().setLayout(new GridBagLayout());
            JPanel numberPanel = new JPanel(new GridBagLayout());
            JPanel buttonPanel = new JPanel(new GridBagLayout());

            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            getRootPane().setDefaultButton(cancelButton);

            AwtUtil.normalizeComponentDimension(new JButton[] { defaultButton, cancelButton,
                    okButton });

            counterLabel.setToolTipText(RENUMBER_FROM_DESC);
            counterField.setToolTipText(RENUMBER_FROM_DESC);
            incrementLabel.setToolTipText(INCREMENT_BY_DESC);
            incrementField.setToolTipText(INCREMENT_BY_DESC);

            getContentPane().add(
                    numberPanel,
                    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                            GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

            numberPanel.add(counterLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    new Insets(12, 12, 6, 6), 0, 0));
            numberPanel.add(counterField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    new Insets(12, 0, 6, 11), 70, 0));
            numberPanel.add(incrementLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    new Insets(12, 12, 6, 6), 0, 0));
            numberPanel.add(incrementField, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    new Insets(12, 0, 6, 11), 70, 0));

            numberPanel.add(Box.createVerticalGlue(), new GridBagConstraints(0, 2, 4, 1, 0.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                    new Insets(12, 0, 11, 0), 0, 0));

            getContentPane().add(
                    buttonPanel,
                    new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
                            GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

            buttonPanel.add(defaultButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 11, 0), 0,
                    0));
            buttonPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(1, 0, 1, 1, 1.0,
                    1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
                    0, 0));
            buttonPanel.add(okButton,
                    new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                            GridBagConstraints.NONE, new Insets(0, 0, 11, 6), 0, 0));
            buttonPanel.add(cancelButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 11, 12), 0,
                    0));

            counterField.setText(Renumbering.getSingleInstance().getCounter().toString());
            incrementField.setText(Renumbering.getSingleInstance().getIncrement().toString());

            defaultButton.addActionListener(this);
            cancelButton.addActionListener(this);
            okButton.addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == defaultButton) {
                counterField.setText(Renumbering.DEFAULT_COUNTER.toString());
                incrementField.setText(Renumbering.DEFAULT_INCREMENT.toString());
            } else if (source == cancelButton) {
                dispose();
            } else if (source == okButton) {
                try {
                    if (counterField.getText().length() != 0)
                        Renumbering.getSingleInstance().setCounter(
                                new Integer(Integer.parseInt(counterField.getText())));
                    if (incrementField.getText().length() != 0)
                        Renumbering.getSingleInstance().setIncrement(
                                new Integer(Integer.parseInt(incrementField.getText())));
                } catch (Exception ex) {
                } // This should not happen, already validated by the
                // NumericTextField
                dispose();
            }
        }
    }

    public Integer getCounter() {
        return m_counter;
    }

    public void setCounter(Integer counter) {
        m_counter = counter;
    }

    public Integer getIncrement() {
        return m_increment;
    }

    private void setIncrement(Integer increment) {
        m_increment = increment;
    }

    public static void showDialog(JFrame owner) {
        JDialog dialog = Renumbering.getSingleInstance().createDialog(owner);
        dialog.setVisible(true);
    }

    private JDialog createDialog(JFrame owner) {
        RenumberingDialog dialog = new RenumberingDialog(owner);
        dialog.pack();
        /*
         * Dimension dim = dialog.getSize(); if (dim != null){ dialog.setSize(Math.max(dim.width,
         * 400), Math.max(dim.height, 200)); }
         */
        AwtUtil.centerWindow(dialog);
        return dialog;
    }

    // ////////////////////////
    // SUPPORT TestableWindow
    // public String getName() { return ""; }
    public Window createTestWindow(Container owner) {
        JFrame mainframe = new JFrame("BeanInfo"); // NOT LOCALIZABLE, unit test
        JDialog dialog = Renumbering.getSingleInstance().createDialog(mainframe);
        return dialog;
    }

    //
    // /////////////////////////

    // UNIT TEST
    public static void main(String[] args) throws Exception {
        // Create a property dialog
        JFrame mainframe = new JFrame("BeanInfo"); // NOT LOCALIZABLE, unit test
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // get dialog
        showDialog(mainframe);
        System.exit(0);
    } // end main()
} // end Renumbering

