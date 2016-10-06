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

package org.modelsphere.jack.debug;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.modelsphere.jack.awt.HotKeysSupport;

/**
 * This class provide a dialog for configuring the Log.
 * <p>
 * 
 * @see org.modelsphere.jack.debug.Log
 * 
 */

public final class LogDialog extends JDialog implements TestableWindow {
    private JPanel containerPanel = new JPanel();
    private JPanel controlBtnPanel = new JPanel();
    private JButton okButton = new JButton();
    private JButton cancelButton = new JButton();
    private JButton helpButton = new JButton();
    private GridLayout gridLayout1 = new GridLayout();
    private JCheckBox lLogInformations = new JCheckBox();
    private JCheckBox lGraphics = new JCheckBox();
    private JCheckBox lDb = new JCheckBox();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JCheckBox lActions = new JCheckBox();
    private JCheckBox lStat = new JCheckBox();
    private JCheckBox lSystem = new JCheckBox();
    private JCheckBox lException = new JCheckBox();
    private JCheckBox lOthers = new JCheckBox();
    private JCheckBox lPlugins = new JCheckBox();
    private JCheckBox lTrace = new JCheckBox();
    private JCheckBox lServices = new JCheckBox();
    private JLabel jLabel1 = new JLabel();
    private JTextField lSize = new JTextField();
    private JLabel jLabel2 = new JLabel();
    private JTextField lCleanup = new JTextField();

    public LogDialog(Frame frame, String title, boolean modal) {
        super(frame, title, modal);
        jbInit();
        this.pack();
        setSize(getWidth(), getHeight() + 30);
        this.setLocationRelativeTo(frame);
        initValues();
        KeyAdapter keyAdapter = new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char car = e.getKeyChar();
                if (!Character.isDigit(car) && "-+\b".indexOf(car) == -1) { // NOT LOCALIZABLE
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    e.consume();
                }
            }
        };
        helpButton.setEnabled(false);
        lSize.addKeyListener(keyAdapter);
        lCleanup.addKeyListener(keyAdapter);
    }

    public LogDialog(Frame frame, String title) {
        this(frame, title, false);
    }

    public LogDialog(Frame frame) {
        this(frame, "Log Configuration", false); // NOT LOCALIZABLE - this dialog is available in debug only
    }

    private void jbInit() {
        containerPanel.setLayout(gridBagLayout1);
        gridLayout1.setHgap(5);
        controlBtnPanel.setLayout(gridLayout1);
        lActions.setText("Action"); // NOT LOCALIZABLE - this dialog is available in debug only
        lStat.setText("Statistics"); // NOT LOCALIZABLE - this dialog is available in debug only
        lSystem.setText("System"); // NOT LOCALIZABLE - this dialog is available in debug only
        lException.setText("Exceptions"); // NOT LOCALIZABLE - this dialog is available in debug only
        lOthers.setText("Others"); // NOT LOCALIZABLE - this dialog is available in debug only
        lPlugins.setText("Plugins"); // NOT LOCALIZABLE - this dialog is available in debug only
        lServices.setText("Services"); // NOT LOCALIZABLE - this dialog is available in debug only
        lTrace.setText("Trace"); // NOT LOCALIZABLE - this dialog is available in debug only
        jLabel1.setText("Log File Size (KBytes)"); // NOT LOCALIZABLE - this dialog is available in debug only
        jLabel2.setText("Cleanup Size (KBytes)"); // NOT LOCALIZABLE - this dialog is available in debug only
        okButton.setText("OK"); // NOT LOCALIZABLE - this dialog is available in debug only
        cancelButton.setText("Cancel"); // NOT LOCALIZABLE - this dialog is available in debug only
        helpButton.setText("Help"); // NOT LOCALIZABLE - this dialog is available in debug only
        lLogInformations.setText("Log Informations"); // NOT LOCALIZABLE - this dialog is available in debug only
        lGraphics.setText("Graphics"); // NOT LOCALIZABLE - this dialog is available in debug only
        lDb.setText("Db"); // NOT LOCALIZABLE - this dialog is available in debug only
        lSize.setHorizontalAlignment(SwingConstants.RIGHT);
        lCleanup.setHorizontalAlignment(SwingConstants.RIGHT);
        getContentPane().add(containerPanel);

        // Main Panel
        containerPanel.add(lLogInformations, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 12, 0, 0), 0, 0));

        // Control Button Panel
        controlBtnPanel.add(okButton, null);
        controlBtnPanel.add(cancelButton, null);
        //HIDEHELPforV1//controlBtnPanel.add(helpButton, null);
        containerPanel.add(lStat, new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 0), 0, 0));
        containerPanel.add(lException, new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 0), 0, 0));
        containerPanel.add(lOthers, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 12), 0, 0));
        containerPanel.add(lDb, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 0), 0, 0));
        containerPanel.add(lGraphics, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 12, 0, 12), 0, 0));
        containerPanel.add(lSystem, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 12), 0, 0));
        containerPanel.add(lActions, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 12), 0, 0));
        containerPanel.add(lPlugins, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 12), 0, 0));
        containerPanel.add(lTrace, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 12), 0, 0));
        containerPanel.add(lServices, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 12), 0, 0));
        containerPanel.add(jLabel1, new GridBagConstraints(0, 6, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 12, 0, 0), 0, 0));
        containerPanel.add(lSize, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 12, 0, 12),
                0, 0));
        containerPanel.add(jLabel2, new GridBagConstraints(0, 7, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 12, 0, 0), 0, 0));
        containerPanel.add(lCleanup, new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(6, 12, 0, 12),
                0, 0));
        containerPanel.add(controlBtnPanel, new GridBagConstraints(0, 8,
                GridBagConstraints.REMAINDER, 2, 1.0, 1.0, GridBagConstraints.SOUTHEAST,
                GridBagConstraints.NONE, new Insets(17, 12, 12, 12), 0, 0));

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveConfig();
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        helpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

        getRootPane().setDefaultButton(okButton);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        new HotKeysSupport(this, cancelButton, helpButton);
    }

    private void initValues() {
        int config = Log.getConfiguration();
        lActions.setSelected((config & Log.LOG_ACTION) != 0);
        lStat.setSelected((config & Log.LOG_STATISTIC) != 0);
        lSystem.setSelected((config & Log.LOG_SYSTEM) != 0);
        lException.setSelected((config & Log.LOG_EXCEPTION) != 0);
        lOthers.setSelected((config & Log.LOG_OTHER) != 0);
        lLogInformations.setSelected((config & Log.LOG_INFO) != 0);
        lGraphics.setSelected((config & Log.LOG_GRAPHIC) != 0);
        lDb.setSelected((config & Log.LOG_DB) != 0);
        lPlugins.setSelected((config & Log.LOG_PLUGIN) != 0);
        lTrace.setSelected((config & Log.LOG_TRACE) != 0);
        lServices.setSelected((config & Log.LOG_SERVICES) != 0);
        lSize.setText(new Integer(Log.getMaxSize()).toString());
        lCleanup.setText(new Integer(Log.getCleanupSize()).toString());
    }

    private void saveConfig() {
        int newConfig = 0;
        newConfig += lActions.isSelected() ? Log.LOG_ACTION : 0;
        newConfig += lStat.isSelected() ? Log.LOG_STATISTIC : 0;
        newConfig += lSystem.isSelected() ? Log.LOG_SYSTEM : 0;
        newConfig += lException.isSelected() ? Log.LOG_EXCEPTION : 0;
        newConfig += lOthers.isSelected() ? Log.LOG_OTHER : 0;
        newConfig += lLogInformations.isSelected() ? Log.LOG_INFO : 0;
        newConfig += lGraphics.isSelected() ? Log.LOG_GRAPHIC : 0;
        newConfig += lDb.isSelected() ? Log.LOG_DB : 0;
        newConfig += lPlugins.isSelected() ? Log.LOG_PLUGIN : 0;
        newConfig += lTrace.isSelected() ? Log.LOG_TRACE : 0;
        newConfig += lServices.isSelected() ? Log.LOG_SERVICES : 0;
        Log.configureOptions(newConfig);
        String lSizeText = lSize.getText();
        String lCleanupText = lCleanup.getText();
        int newsize = Log.getMaxSize();
        int newcleanupsize = Log.getCleanupSize();
        try {
            newsize = new Integer(Integer.parseInt(lSizeText)).intValue();
        } catch (NumberFormatException ex) {
        }
        try {
            newcleanupsize = new Integer(Integer.parseInt(lCleanupText)).intValue();
        } catch (NumberFormatException ex) {
        }
        Log.configureMaxSize(newsize, newcleanupsize);
    }

    // *************
    // DEMO FUNCTION
    // *************

    public Window createTestWindow(Container owner) {
        LogDialog dialog = new LogDialog(null);
        return dialog;
    }

    private static void runDemo() {
        LogDialog dialog = new LogDialog(null);
        dialog.setVisible(true);

        boolean done = false;
        do {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
            }

            if (!dialog.isShowing()) {
                dialog.dispose();
                dialog = null;
                done = true;
            }
        } while (!done);
        System.exit(0);
    } //end runDemo()

    /*
     * public static void main(String[] args) { runDemo(); } //end main()
     */

} //end LogDialog
