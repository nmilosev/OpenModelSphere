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

/*
 * Created on Apr 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.modelsphere.sms.preference;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.preference.OptionPanel;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.features.DisplayRecentModifications;
import org.modelsphere.sms.international.LocaleMgr;

/**
 * @author marco.savard
 * 
 *         Display Recent Modifications
 */
public class DisplayRecentModifsOptionGroup extends OptionGroup {

    protected OptionPanel createOptionPanel() {
        return new DisplayRecentModifsPanel();
    }

    public DisplayRecentModifsOptionGroup() {
        super(LocaleMgr.screen.getString("RecentModifications"));
    }

    public static class DisplayRecentModifsPanel extends OptionPanel implements ActionListener,
            ChangeListener {

        private static final long serialVersionUID = 1L;
        private JRadioButton option1 = new JRadioButton();
        private JRadioButton option2 = new JRadioButton();
        private JRadioButton option3 = new JRadioButton();
        private JRadioButton option4 = new JRadioButton();

        private JCheckBox checkBox1 = new JCheckBox();
        private JCheckBox checkBox2 = new JCheckBox();
        private JCheckBox checkBox3 = new JCheckBox();

        private JLabel label;
        private JPanel colorPanel;
        private JButton lineColorBtn;

        public DisplayRecentModifsPanel() {
            setLayout(new GridBagLayout());
            int row = 0;

            add(option1, new GridBagConstraints(0, row, 3, 1, 0.0, 0.0, GridBagConstraints.NORTH,
                    GridBagConstraints.BOTH, new Insets(12, 3, 3, 3), 0, 0));
            option1.setText(LocaleMgr.screen.getString("DoNotDisplay"));
            option1.addActionListener(this);
            row++;

            add(option2, new GridBagConstraints(0, row, 3, 1, 0.0, 0.0, GridBagConstraints.NORTH,
                    GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
            option2.setText(LocaleMgr.screen.getString("CurrentSession"));
            option2.addActionListener(this);
            row++;

            add(option3, new GridBagConstraints(0, row, 3, 1, 0.0, 0.0, GridBagConstraints.NORTH,
                    GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
            option3.setText(LocaleMgr.screen.getString("UnsavedModifications"));
            option3.addActionListener(this);
            row++;

            /*
             * add(option4, new GridBagConstraints(0, row, 3, 1, 0.0, 0.0, GridBagConstraints.NORTH,
             * GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
             * option4.setText("Since a Specific Date"); option4.addActionListener(this); row++;
             */

            ButtonGroup group = new ButtonGroup();
            group.add(option1);
            group.add(option2);
            group.add(option3);
            group.add(option4);

            label = new JLabel(LocaleMgr.screen.getString("HighlightColor"));
            colorPanel = new JPanel();
            lineColorBtn = new JButton();
            lineColorBtn.setText("...");
            lineColorBtn.addActionListener(this);

            JPanel innerPanel = new JPanel();
            add(innerPanel, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(18, 3, 6, 5),
                    0, 0));

            innerPanel.add(label, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(6, 3, 6, 5),
                    0, 0));
            innerPanel.add(colorPanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 12, 12), 0,
                    00));
            innerPanel.add(lineColorBtn, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 6, 0),
                    0, 0));
            colorPanel.setPreferredSize(new Dimension(lineColorBtn.getPreferredSize().height,
                    lineColorBtn.getPreferredSize().height));
            row++;

            JLabel label = new JLabel(LocaleMgr.screen.getString("Scope"));
            add(label, new GridBagConstraints(0, row, 3, 1, 0.0, 0.0, GridBagConstraints.NORTH,
                    GridBagConstraints.BOTH, new Insets(18, 6, 3, 3), 0, 0));
            row++;

            add(checkBox3, new GridBagConstraints(0, row, 3, 1, 0.0, 0.0, GridBagConstraints.NORTH,
                    GridBagConstraints.BOTH, new Insets(3, 18, 3, 3), 0, 0));
            checkBox3.setText(LocaleMgr.screen.getString("RecentlyModifiedColumns"));
            checkBox3.addActionListener(this);
            row++;

            add(checkBox1, new GridBagConstraints(0, row, 3, 1, 0.0, 0.0, GridBagConstraints.NORTH,
                    GridBagConstraints.BOTH, new Insets(3, 18, 3, 3), 0, 0));
            checkBox1.setText(LocaleMgr.screen.getString("RecentlyModifiedFields"));
            checkBox1.addActionListener(this);
            row++;

            add(checkBox2, new GridBagConstraints(0, row, 3, 1, 0.0, 0.0, GridBagConstraints.NORTH,
                    GridBagConstraints.BOTH, new Insets(3, 18, 3, 3), 0, 0));
            checkBox2.setText(LocaleMgr.screen.getString("RecentlyModifiedMethodsAndConstructors"));
            checkBox2.addActionListener(this);
            checkBox2.setSelected(true);
            row++;

            add(Box.createVerticalGlue(), new GridBagConstraints(0, row, 1, 1, 1.0, 1.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.modelsphere.jack.preference.OptionPanel#init()
         */
        public void init() {
            // get properties
            PropertiesSet applOptions = PropertiesManager.APPLICATION_PROPERTIES_SET;
            int displayOption = applOptions.getPropertyInteger(DisplayRecentModifications.class,
                    DisplayRecentModifications.DISPLAY_RECENT_MODIFS_OPTION_PROPERTY,
                    DisplayRecentModifications.DO_NOT_DISPLAY_RECENT_MODIFS);

            int recentColor = applOptions.getPropertyInteger(DisplayRecentModifications.class,
                    DisplayRecentModifications.RECENT_MODIFS_COLOR_PROPERTY, Color.BLUE.getRGB());

            boolean recentlyModifiedColumns = applOptions.getPropertyBoolean(
                    DisplayRecentModifications.class,
                    DisplayRecentModifications.DISP_RECENTLY_MODIFIED_COLUMNS_PROPERTY, true);

            boolean recentlyModifiedFields = applOptions.getPropertyBoolean(
                    DisplayRecentModifications.class,
                    DisplayRecentModifications.DISP_RECENTLY_MODIFIED_FIELDS_PROPERTY, true);

            boolean recentlyModifiedMethods = applOptions.getPropertyBoolean(
                    DisplayRecentModifications.class,
                    DisplayRecentModifications.DISP_RECENTLY_MODIFIED_METHODS_PROPERTY, true);

            switch (displayOption) {
            case DisplayRecentModifications.DO_NOT_DISPLAY_RECENT_MODIFS:
                option1.setSelected(true);
                break;
            case DisplayRecentModifications.DISPLAY_SESSION_MODIFS:
                option2.setSelected(true);
                break;
            case DisplayRecentModifications.DISPLAY_UNSAVED_MODIFS:
                option3.setSelected(true);
                break;
            case DisplayRecentModifications.DISPLAY_MODIFS_SINCE:
                option4.setSelected(true);
                break;
            }

            Color c = new Color(recentColor);
            colorPanel.setBackground(c);

            checkBox3.setSelected(recentlyModifiedColumns);
            checkBox1.setSelected(recentlyModifiedFields);
            checkBox2.setSelected(recentlyModifiedMethods);

            enableWidgets(displayOption);
        } // end init()

        public void actionPerformed(ActionEvent e) {
            int option = getDisplayOption();

            if (option != -1) {
                fireOptionChanged(PropertiesManager.APPLICATION_PROPERTIES_SET,
                        DisplayRecentModifications.class,
                        DisplayRecentModifications.DISPLAY_RECENT_MODIFS_OPTION_PROPERTY, option);

                enableWidgets(option);
            } // end if

            if (e.getSource() == lineColorBtn) {
                final String TITLE = LocaleMgr.screen.getString("ChooseColorToHighlight");
                Color currentColor = colorPanel.getBackground();
                Color newColor = JColorChooser.showDialog(this, TITLE, currentColor);
                if (newColor != null) {
                    fireOptionChanged(PropertiesManager.APPLICATION_PROPERTIES_SET,
                            DisplayRecentModifications.class,
                            DisplayRecentModifications.RECENT_MODIFS_COLOR_PROPERTY, newColor
                                    .getRGB());

                    colorPanel.setBackground(newColor);
                }
            } // end if

            if (e.getSource() == checkBox3) {
                boolean displayRecentlyModifiedFields = checkBox3.isSelected();

                fireOptionChanged(PropertiesManager.APPLICATION_PROPERTIES_SET,
                        DisplayRecentModifications.class,
                        DisplayRecentModifications.DISP_RECENTLY_MODIFIED_COLUMNS_PROPERTY,
                        displayRecentlyModifiedFields);
            } // end if

            if (e.getSource() == checkBox1) {
                boolean displayRecentlyModifiedFields = checkBox1.isSelected();

                fireOptionChanged(PropertiesManager.APPLICATION_PROPERTIES_SET,
                        DisplayRecentModifications.class,
                        DisplayRecentModifications.DISP_RECENTLY_MODIFIED_FIELDS_PROPERTY,
                        displayRecentlyModifiedFields);
            } // end if

            if (e.getSource() == checkBox2) {
                boolean displayRecentlyModifiedMethods = checkBox2.isSelected();

                fireOptionChanged(PropertiesManager.APPLICATION_PROPERTIES_SET,
                        DisplayRecentModifications.class,
                        DisplayRecentModifications.DISP_RECENTLY_MODIFIED_METHODS_PROPERTY,
                        displayRecentlyModifiedMethods);
            } // end if
        }

        private void enableWidgets(int option) {
            boolean enabled = (option != DisplayRecentModifications.DO_NOT_DISPLAY_RECENT_MODIFS);
            label.setEnabled(enabled);
            lineColorBtn.setEnabled(enabled);
            checkBox3.setEnabled(enabled);
            checkBox1.setEnabled(enabled);
            checkBox2.setEnabled(enabled);
        }

        public void stateChanged(ChangeEvent e) {
        }

        public void terminate() {
            int option = getDisplayOption();
            PropertiesSet applOptions = PropertiesManager.APPLICATION_PROPERTIES_SET;

            if (option != -1) {
                applOptions.setProperty(DisplayRecentModifications.class,
                        DisplayRecentModifications.DISPLAY_RECENT_MODIFS_OPTION_PROPERTY, option);
            }

            // color of recently modified items
            Color currentColor = colorPanel.getBackground();
            applOptions.setProperty(DisplayRecentModifications.class,
                    DisplayRecentModifications.RECENT_MODIFS_COLOR_PROPERTY, currentColor.getRGB());

            // display fields and methods
            boolean displayRecentlyModifiedColumns = checkBox3.isSelected();
            boolean displayRecentlyModifiedFields = checkBox1.isSelected();
            boolean displayRecentlyModifiedMethods = checkBox2.isSelected();

            applOptions.setProperty(DisplayRecentModifications.class,
                    DisplayRecentModifications.DISP_RECENTLY_MODIFIED_COLUMNS_PROPERTY,
                    displayRecentlyModifiedColumns);
            applOptions.setProperty(DisplayRecentModifications.class,
                    DisplayRecentModifications.DISP_RECENTLY_MODIFIED_FIELDS_PROPERTY,
                    displayRecentlyModifiedFields);
            applOptions.setProperty(DisplayRecentModifications.class,
                    DisplayRecentModifications.DISP_RECENTLY_MODIFIED_METHODS_PROPERTY,
                    displayRecentlyModifiedMethods);

            // refresh diagrams
            DefaultMainFrame mf = MainFrame.getSingleton();
            mf.refreshAllDiagrams();
        }

        //
        // private methods
        //

        private int getDisplayOption() {
            int option = -1;

            if (option1.isSelected()) {
                option = DisplayRecentModifications.DO_NOT_DISPLAY_RECENT_MODIFS;
            } else if (option2.isSelected()) {
                option = DisplayRecentModifications.DISPLAY_SESSION_MODIFS;
            } else if (option3.isSelected()) {
                option = DisplayRecentModifications.DISPLAY_UNSAVED_MODIFS;
            } else if (option4.isSelected()) {
                option = DisplayRecentModifications.DISPLAY_MODIFS_SINCE;
            }

            return option;
        }

    }

}
