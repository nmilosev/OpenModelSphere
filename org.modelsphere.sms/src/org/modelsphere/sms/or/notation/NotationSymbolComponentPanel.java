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
package org.modelsphere.sms.or.notation;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import org.modelsphere.sms.notation.OptionComponent;
import org.modelsphere.sms.or.db.srtypes.ORConnectivitiesDisplay;
import org.modelsphere.sms.or.db.srtypes.ORConnectivityPosition;
import org.modelsphere.sms.or.db.srtypes.ORNotationSymbol;
import org.modelsphere.sms.or.international.LocaleMgr;

public final class NotationSymbolComponentPanel extends JPanel {
    private static final String kDisplay = LocaleMgr.screen.getString("displayAsNoun");
    private static final String kMinMaxPosition = LocaleMgr.screen.getString("minMaxPosition");
    private static final String kChildDepPosition = LocaleMgr.screen.getString("childDepPosition");
    private static final String kCloseBy = LocaleMgr.screen.getString("closeBy");
    private static final String kFarAway = LocaleMgr.screen.getString("farAway");
    private static final String kMaximum = LocaleMgr.screen.getString("maximum");
    private static final String kMinimum = LocaleMgr.screen.getString("minimum");
    private static final String kChildDirection = LocaleMgr.screen.getString("childRole");
    private static final String kKeyDependency = LocaleMgr.screen.getString("keyDependency");

    private static final String VALUE_INDEX_PROPERTY = "Value Index Property"; // NOT
    // LOCALIZABLE
    // --
    // property
    // key

    private static ImageIcon[] symbolPossibleValues = null;

    static {
        Image[] images = ORNotationSymbol.imagePossibleValues;
        symbolPossibleValues = new ImageIcon[images.length];
        for (int i = 0; i < images.length; i++) {
            symbolPossibleValues[i] = new ImageIcon(images[i]);
        }
    }

    private OptionComponent optionComponent;

    GridBagLayout thisLayout = new GridBagLayout();
    GridBagLayout symbolicPanelLayout = new GridBagLayout();
    GridBagLayout symbolicMinMaxPositionPanelLayout = new GridBagLayout();
    GridBagLayout symbolicChildDepPositionPanelLayout = new GridBagLayout();
    GridBagLayout symbolicDisplayPanelLayout = new GridBagLayout();

    JPanel symbolicPanel = new JPanel();
    JPanel symbolicMinMaxPositionPanel = new JPanel();
    JPanel symbolicChildDepPositionPanel = new JPanel();
    JPanel symbolicDisplayPanel = new JPanel();

    TitledBorder minMaxPositionTitle;
    TitledBorder childDepPositionTitle;
    TitledBorder displayTitle;
    TitledBorder symbolTitle;
    JCheckBox minimum = new JCheckBox();
    JCheckBox maximum = new JCheckBox();

    JRadioButton minMaxCloseBy = new JRadioButton();
    JRadioButton minMaxFarAway = new JRadioButton();
    ButtonGroup minMaxPositionGroup = new ButtonGroup();
    JRadioButton childDepCloseBy = new JRadioButton();
    JRadioButton childDepFarAway = new JRadioButton();
    ButtonGroup childDepPositionGroup = new ButtonGroup();

    JLabel min0Label = new JLabel();
    JLabel min1Label = new JLabel();
    JComboBox min0 = new JComboBox(symbolPossibleValues);
    JComboBox min1 = new JComboBox(symbolPossibleValues);
    JLabel max1Label = new JLabel();
    JLabel maxNLabel = new JLabel();
    JComboBox max1 = new JComboBox(symbolPossibleValues);
    JComboBox maxN = new JComboBox(symbolPossibleValues);

    JCheckBox childDirectionCheck = new JCheckBox();
    JComboBox childDirection = new JComboBox(symbolPossibleValues);
    JCheckBox keyDependencyCheck = new JCheckBox();
    JComboBox keyDependency = new JComboBox(symbolPossibleValues);

    Component symbolicMinMaxPositionFill;
    Component symbolicChildDepPositionFill;
    Component symbolicDisplayFill;
    Component symbolicSymbolFill;

    public NotationSymbolComponentPanel(OptionComponent optionComponent) {
        this.optionComponent = optionComponent;
        jbInit();
        minMaxPositionGroup.add(minMaxCloseBy);
        minMaxPositionGroup.add(minMaxFarAway);
        childDepPositionGroup.add(childDepCloseBy);
        childDepPositionGroup.add(childDepFarAway);

        // init a client property so that we can use one method to deal with all
        // symbol combo
        // The object value represent the index in the 'values' array.
        min0.putClientProperty(VALUE_INDEX_PROPERTY, new Integer(3));
        min1.putClientProperty(VALUE_INDEX_PROPERTY, new Integer(4));
        max1.putClientProperty(VALUE_INDEX_PROPERTY, new Integer(5));
        maxN.putClientProperty(VALUE_INDEX_PROPERTY, new Integer(6));
        keyDependency.putClientProperty(VALUE_INDEX_PROPERTY, new Integer(8));
        childDirection.putClientProperty(VALUE_INDEX_PROPERTY, new Integer(10));

    }

    private void jbInit() {
        minMaxPositionTitle = new TitledBorder(kMinMaxPosition);
        childDepPositionTitle = new TitledBorder(kChildDepPosition);
        displayTitle = new TitledBorder(kDisplay);
        symbolicMinMaxPositionFill = Box.createVerticalBox();
        symbolicChildDepPositionFill = Box.createVerticalBox();
        symbolicDisplayFill = Box.createVerticalBox();
        symbolicSymbolFill = Box.createVerticalBox();
        this.setLayout(thisLayout);
        symbolicPanel.setLayout(symbolicPanelLayout);
        symbolicMinMaxPositionPanel.setLayout(symbolicMinMaxPositionPanelLayout);
        symbolicMinMaxPositionPanel.setBorder(minMaxPositionTitle);
        symbolicMinMaxPositionPanel.setName("symbolicMinMaxPositionPanel"); // NOT
        // LOCALIZABLE
        // -
        // QA
        symbolicChildDepPositionPanel.setLayout(symbolicChildDepPositionPanelLayout);
        symbolicChildDepPositionPanel.setBorder(childDepPositionTitle);
        symbolicChildDepPositionPanel.setName("symbolicChildDepPositionPanel"); // NOT
        // LOCALIZABLE
        // -
        // QA
        symbolicDisplayPanel.setLayout(symbolicDisplayPanelLayout);
        symbolicDisplayPanel.setBorder(displayTitle);

        minMaxCloseBy.setName("minMaxCloseBy"); // NOT LOCALIZABLE - QA
        minMaxFarAway.setName("minMaxFarAway"); // NOT LOCALIZABLE - QA
        childDepCloseBy.setName("childDepCloseBy"); // NOT LOCALIZABLE - QA
        childDepFarAway.setName("childDepFarAway"); // NOT LOCALIZABLE - QA

        min0Label.setText("0"); // NOT LOCALIZABLE
        min1Label.setText("1"); // NOT LOCALIZABLE
        max1Label.setText("1"); // NOT LOCALIZABLE
        maxNLabel.setText("N"); // NOT LOCALIZABLE

        childDirectionCheck.setText(kChildDirection);
        childDirectionCheck.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                childDirectionCheck_actionPerformed(e);
            }
        });
        keyDependencyCheck.setText(kKeyDependency);
        keyDependencyCheck.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                keyDependencyCheck_actionPerformed(e);
            }
        });

        minMaxCloseBy.setText(kCloseBy);
        minMaxCloseBy.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                minMaxCloseBy_actionPerformed(e);
            }
        });
        childDepCloseBy.setText(kCloseBy + " ");
        childDepCloseBy.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                childDepCloseBy_actionPerformed(e);
            }
        });
        minMaxFarAway.setText(kFarAway);
        minMaxFarAway.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                minMaxFarAway_actionPerformed(e);
            }
        });
        childDepFarAway.setText(kFarAway + " ");
        childDepFarAway.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                childDepFarAway_actionPerformed(e);
            }
        });
        minimum.setText(kMinimum);
        minimum.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                minimum_actionPerformed(e);
            }
        });
        maximum.setText(kMaximum);
        maximum.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                maximum_actionPerformed(e);
            }
        });

        ActionListener comboListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                combo_actionPerformed(e);
            }
        };
        min0.addActionListener(comboListener);
        min1.addActionListener(comboListener);
        max1.addActionListener(comboListener);
        maxN.addActionListener(comboListener);
        keyDependency.addActionListener(comboListener);
        childDirection.addActionListener(comboListener);

        this.add(symbolicPanel, new GridBagConstraints(0, 0, 2, 2, 0.5, 0.5,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        symbolicPanel.add(symbolicDisplayPanel, new GridBagConstraints(0, 0, 2, 1, 0.5, 0.5,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(12, 12, 6, 12), 0, 0));
        symbolicPanel.add(symbolicMinMaxPositionPanel, new GridBagConstraints(0, 1, 1, 1, 0.5, 0.5,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(6, 12, 12, 6), 0, 0));
        symbolicPanel.add(symbolicChildDepPositionPanel, new GridBagConstraints(1, 1, 1, 1, 0.5,
                0.5, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(6, 6, 12, 12), 0,
                0));

        symbolicDisplayPanel.add(minimum, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(6, 6, 3, 3), 0,
                0));
        symbolicDisplayPanel.add(min0Label, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(3, 24, 3, 6), 0, 0));
        symbolicDisplayPanel.add(min0, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 6, 3, 6), 0, 0));
        symbolicDisplayPanel.add(min1Label, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(3, 24, 3, 6), 0, 0));
        symbolicDisplayPanel.add(min1, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 6, 3, 6), 0, 0));

        symbolicDisplayPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(2, 0, 1, 1,
                0.5, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(6, 6,
                        3, 3), 0, 0));
        symbolicDisplayPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(5, 0, 1, 1,
                0.5, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(6, 6,
                        3, 3), 0, 0));

        symbolicDisplayPanel.add(maximum, new GridBagConstraints(3, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(6, 6, 3, 3), 0,
                0));
        symbolicDisplayPanel.add(max1Label, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(3, 24, 3, 6), 0, 0));
        symbolicDisplayPanel.add(max1, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 6, 3, 6), 0, 0));
        symbolicDisplayPanel.add(maxNLabel, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(3, 24, 3, 6), 0, 0));
        symbolicDisplayPanel.add(maxN, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 6, 3, 6), 0, 0));

        symbolicDisplayPanel
                .add(childDirectionCheck, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                        new Insets(9, 6, 6, 3), 0, 0));
        symbolicDisplayPanel.add(childDirection, new GridBagConstraints(1, 5, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 6, 6, 6), 0, 0));

        /*
         * symbolicDisplayPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(2, 4, 1, 1,
         * 1.0, 0.0 ,GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(9, 6, 6, 3),
         * 0, 0)); symbolicDisplayPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(5, 4,
         * 1, 1, 1.0, 0.0 ,GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(9, 6, 6,
         * 3), 0, 0));
         */
        symbolicDisplayPanel
                .add(keyDependencyCheck, new GridBagConstraints(3, 4, 2, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                        new Insets(9, 6, 6, 3), 0, 0));
        symbolicDisplayPanel.add(keyDependency, new GridBagConstraints(4, 5, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 6, 6, 6), 0, 0));

        symbolicDisplayPanel.add(symbolicDisplayFill, new GridBagConstraints(0, 6,
                GridBagConstraints.RELATIVE, 1, 0.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        symbolicMinMaxPositionPanel.add(minMaxCloseBy, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(6, 6, 0, 6), 0,
                0));
        symbolicMinMaxPositionPanel.add(minMaxFarAway, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 6, 3, 6), 0,
                0));
        symbolicMinMaxPositionPanel.add(symbolicMinMaxPositionFill, new GridBagConstraints(0, 2, 1,
                1, 0.5, 0.5, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0,
                        0), 0, 0));

        symbolicChildDepPositionPanel.add(childDepCloseBy, new GridBagConstraints(0, 0, 1, 1, 0.0,
                0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(6, 6, 0, 6), 0, 0));
        symbolicChildDepPositionPanel.add(childDepFarAway, new GridBagConstraints(0, 1, 1, 1, 0.0,
                0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 6, 3, 6), 0, 0));
        symbolicChildDepPositionPanel.add(symbolicChildDepPositionFill, new GridBagConstraints(0,
                2, 1, 1, 0.5, 0.5, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
                        0, 0, 0), 0, 0));

    }

    void initFields(Object[] values) {
        ORConnectivitiesDisplay display = (ORConnectivitiesDisplay) values[0];
        ORConnectivityPosition minMaxposition = (ORConnectivityPosition) values[1];
        ORConnectivityPosition childDepposition = (ORConnectivityPosition) values[2];

        minimum.setSelected((display.getValue() & ORConnectivitiesDisplay.MIN) != 0);
        maximum.setSelected((display.getValue() & ORConnectivitiesDisplay.MAX) != 0);
        minMaxCloseBy.setSelected(minMaxposition.getValue() == ORConnectivityPosition.CLOSE_BY);
        minMaxFarAway.setSelected(minMaxposition.getValue() == ORConnectivityPosition.FAR_AWAY);
        childDepCloseBy.setSelected(childDepposition.getValue() == ORConnectivityPosition.CLOSE_BY);
        childDepFarAway.setSelected(childDepposition.getValue() == ORConnectivityPosition.FAR_AWAY);

        min0.setSelectedIndex(((ORNotationSymbol) values[3]).getValue());
        min1.setSelectedIndex(((ORNotationSymbol) values[4]).getValue());
        max1.setSelectedIndex(((ORNotationSymbol) values[5]).getValue());
        maxN.setSelectedIndex(((ORNotationSymbol) values[6]).getValue());
        keyDependencyCheck.setSelected(((Boolean) values[7]).booleanValue());
        keyDependency.setSelectedIndex(((ORNotationSymbol) values[8]).getValue());
        childDirectionCheck.setSelected(((Boolean) values[9]).booleanValue());
        childDirection.setSelectedIndex(((ORNotationSymbol) values[10]).getValue());

        if (!isEnabled()) {
            minimum.setEnabled(false);
            maximum.setEnabled(false);
            minMaxCloseBy.setEnabled(false);
            minMaxFarAway.setEnabled(false);
            childDepCloseBy.setEnabled(false);
            childDepFarAway.setEnabled(false);
            min0.setEnabled(false);
            min1.setEnabled(false);
            max1.setEnabled(false);
            maxN.setEnabled(false);
            keyDependencyCheck.setEnabled(false);
            keyDependency.setEnabled(false);
            childDirectionCheck.setEnabled(false);
            childDirection.setEnabled(false);
        } else {
            minimum.setEnabled(true);
            maximum.setEnabled(true);
            keyDependencyCheck.setEnabled(true);
            childDirectionCheck.setEnabled(true);
            updateMinMaxPositionEnable();
            updateChildDepPositionEnable();
            updateMinSymbolEnable();
            updateMaxSymbolEnable();
            updateChildDirectionSymbolEnable();
            updateKeyDependencySymbolEnable();
        }

    }

    void minimum_actionPerformed(ActionEvent e) {
        updateMinMaxDisplayValue();
    }

    void maximum_actionPerformed(ActionEvent e) {
        updateMinMaxDisplayValue();
    }

    private void updateMinMaxDisplayValue() {
        boolean minimumSelected = minimum.isSelected();
        boolean maximumSelected = maximum.isSelected();
        int newvalue = (minimumSelected ? ORConnectivitiesDisplay.MIN : 0)
                | (maximumSelected ? ORConnectivitiesDisplay.MAX : 0);
        optionComponent.setValue(ORConnectivitiesDisplay.getInstance(newvalue), 0);
        updateMinMaxPositionEnable();
        updateMinSymbolEnable();
        updateMaxSymbolEnable();
    }

    void minMaxCloseBy_actionPerformed(ActionEvent e) {
        optionComponent.setValue(ORConnectivityPosition
                .getInstance(ORConnectivityPosition.CLOSE_BY), 1);
    }

    void minMaxFarAway_actionPerformed(ActionEvent e) {
        optionComponent.setValue(ORConnectivityPosition
                .getInstance(ORConnectivityPosition.FAR_AWAY), 1);
    }

    void childDepCloseBy_actionPerformed(ActionEvent e) {
        optionComponent.setValue(ORConnectivityPosition
                .getInstance(ORConnectivityPosition.CLOSE_BY), 2);
    }

    void childDepFarAway_actionPerformed(ActionEvent e) {
        optionComponent.setValue(ORConnectivityPosition
                .getInstance(ORConnectivityPosition.FAR_AWAY), 2);
    }

    void childDirectionCheck_actionPerformed(ActionEvent e) {
        boolean selected = childDirectionCheck.isSelected();
        optionComponent.setValue(selected ? Boolean.TRUE : Boolean.FALSE, 9);
        updateChildDirectionSymbolEnable();
        updateChildDepPositionEnable();
    }

    void keyDependencyCheck_actionPerformed(ActionEvent e) {
        boolean selected = keyDependencyCheck.isSelected();
        optionComponent.setValue(selected ? Boolean.TRUE : Boolean.FALSE, 7);
        updateKeyDependencySymbolEnable();
        updateChildDepPositionEnable();
    }

    void updateChildDepPositionEnable() {
        boolean keyDependencySelected = keyDependencyCheck.isSelected();
        boolean childDirectionSelected = childDirectionCheck.isSelected();
        childDepCloseBy.setEnabled(keyDependencySelected | childDirectionSelected);
        childDepFarAway.setEnabled(keyDependencySelected | childDirectionSelected);
    }

    void updateMinMaxPositionEnable() {
        boolean minimumSelected = minimum.isSelected();
        boolean maximumSelected = maximum.isSelected();
        minMaxCloseBy.setEnabled(minimumSelected | maximumSelected);
        minMaxFarAway.setEnabled(minimumSelected | maximumSelected);
    }

    void updateMinSymbolEnable() {
        boolean selected = minimum.isSelected();
        min0.setEnabled(selected);
        min1.setEnabled(selected);
    }

    void updateMaxSymbolEnable() {
        boolean selected = maximum.isSelected();
        max1.setEnabled(selected);
        maxN.setEnabled(selected);
    }

    void updateChildDirectionSymbolEnable() {
        boolean selected = childDirectionCheck.isSelected();
        childDirection.setEnabled(selected);
    }

    void updateKeyDependencySymbolEnable() {
        boolean selected = keyDependencyCheck.isSelected();
        keyDependency.setEnabled(selected);
    }

    void combo_actionPerformed(ActionEvent e) {
        JComboBox source = (JComboBox) e.getSource();
        int newSelection = source.getSelectedIndex();
        int valueIdx = ((Integer) source.getClientProperty(VALUE_INDEX_PROPERTY)).intValue();
        optionComponent.setValue(ORNotationSymbol.getInstance(newSelection), valueIdx);
    }

}
