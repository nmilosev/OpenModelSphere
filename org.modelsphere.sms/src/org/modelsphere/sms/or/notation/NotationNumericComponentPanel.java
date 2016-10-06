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
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import org.modelsphere.sms.notation.OptionComponent;
import org.modelsphere.sms.or.db.srtypes.ORConnectivitiesDisplay;
import org.modelsphere.sms.or.db.srtypes.ORConnectivityPosition;
import org.modelsphere.sms.or.db.srtypes.ORNumericRepresentation;
import org.modelsphere.sms.or.international.LocaleMgr;

public final class NotationNumericComponentPanel extends JPanel {
    private static final String kDisplay = LocaleMgr.screen.getString("displayAsNoun");
    private static final String kPosition = LocaleMgr.screen.getString("position");
    private static final String kNumericRepresentation = LocaleMgr.screen
            .getString("numericRepresentation");
    private static final String kCloseBy = LocaleMgr.screen.getString("closeBy");
    private static final String kFarAway = LocaleMgr.screen.getString("farAway");
    private static final String kMaximum = LocaleMgr.screen.getString("maximum");
    private static final String kMinimum = LocaleMgr.screen.getString("minimum");

    private static String[] numericRepresentations = ORNumericRepresentation.stringPossibleValues;

    private OptionComponent optionComponent;

    GridBagLayout thisLayout = new GridBagLayout();
    GridBagLayout numericPanelLayout = new GridBagLayout();
    GridBagLayout numericPositionPanelLayout = new GridBagLayout();
    GridBagLayout numericDisplayPanelLayout = new GridBagLayout();

    JPanel numericPanel = new JPanel();
    JPanel numericPositionPanel = new JPanel();
    JPanel numericDisplayPanel = new JPanel();

    TitledBorder positionTitle;
    TitledBorder displayTitle;
    JCheckBox minimum = new JCheckBox();
    JCheckBox maximum = new JCheckBox();
    JRadioButton closeBy = new JRadioButton();
    JRadioButton farAway = new JRadioButton();
    ButtonGroup positionGroup = new ButtonGroup();

    JLabel numericRepresentationLabel = new JLabel();
    JComboBox numericRepresentation = new JComboBox(numericRepresentations);

    Component numericPositionFill;
    Component numericDisplayFill;

    public NotationNumericComponentPanel(OptionComponent optionComponent) {
        this.optionComponent = optionComponent;
        jbInit();
        positionGroup.add(closeBy);
        positionGroup.add(farAway);
    }

    private void jbInit() {
        positionTitle = new TitledBorder(kPosition);
        displayTitle = new TitledBorder(kDisplay);
        numericPositionFill = Box.createVerticalBox();
        numericDisplayFill = Box.createVerticalBox();
        this.setLayout(thisLayout);
        numericPanel.setLayout(numericPanelLayout);
        numericPositionPanel.setLayout(numericPositionPanelLayout);
        numericPositionPanel.setBorder(positionTitle);
        numericDisplayPanel.setLayout(numericDisplayPanelLayout);
        numericDisplayPanel.setBorder(displayTitle);
        numericRepresentationLabel.setText(kNumericRepresentation);

        closeBy.setText(kCloseBy);
        closeBy.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                closeBy_actionPerformed(e);
            }
        });
        farAway.setText(kFarAway);
        farAway.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                farAway_actionPerformed(e);
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
        numericRepresentation.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                numericRepresentation_actionPerformed(e);
            }
        });
        this.add(numericPanel, new GridBagConstraints(0, 0, 1, 1, 0.5, 0.5,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        numericPanel.add(numericDisplayPanel, new GridBagConstraints(0, 0, 1, 1, 0.5, 0.5,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(12, 12, 6, 12), 0, 0));
        numericPanel.add(numericPositionPanel, new GridBagConstraints(0, 1, 1, 1, 0.5, 0.5,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(6, 12, 12, 12), 0, 0));

        numericDisplayPanel.add(minimum, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(6, 6, 3, 6), 0,
                0));
        numericDisplayPanel.add(maximum, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3, 6, 6, 6), 0,
                0));
        numericDisplayPanel.add(numericRepresentationLabel,
                new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.NONE, new Insets(3, 6, 6, 6), 0, 0));
        numericDisplayPanel.add(numericRepresentation, new GridBagConstraints(1, 2, 1, 1, 0.5, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 6, 6, 6), 10, 0));
        numericDisplayPanel.add(numericDisplayFill, new GridBagConstraints(0, 3, 2, 1, 0.5, 0.5,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        numericPositionPanel.add(closeBy, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(6, 6, 6, 6), 0,
                0));
        numericPositionPanel.add(farAway, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 6, 6, 6), 0,
                0));
        numericPositionPanel.add(numericPositionFill, new GridBagConstraints(0, 2, 1, 1, 0.5, 0.5,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    void initFields(Object[] values) {
        ORConnectivitiesDisplay display = (ORConnectivitiesDisplay) values[0];
        ORConnectivityPosition position = (ORConnectivityPosition) values[1];

        minimum.setSelected((display.getValue() & ORConnectivitiesDisplay.MIN) != 0);
        maximum.setSelected((display.getValue() & ORConnectivitiesDisplay.MAX) != 0);
        closeBy.setSelected(position.getValue() == ORConnectivityPosition.CLOSE_BY);
        farAway.setSelected(position.getValue() == ORConnectivityPosition.FAR_AWAY);
        numericRepresentation.setSelectedIndex(((ORNumericRepresentation) values[2]).getValue());

        if (!isEnabled()) {
            closeBy.setEnabled(false);
            farAway.setEnabled(false);
            minimum.setEnabled(false);
            maximum.setEnabled(false);
            numericRepresentation.setEnabled(false);
        } else {
            minimum.setEnabled(true);
            maximum.setEnabled(true);
            numericRepresentation.setEnabled(minimum.isSelected() | maximum.isSelected());
            closeBy.setEnabled(minimum.isSelected() | maximum.isSelected());
            farAway.setEnabled(minimum.isSelected() | maximum.isSelected());
        }

    }

    void minimum_actionPerformed(ActionEvent e) {
        updateDisplayValue();
    }

    void maximum_actionPerformed(ActionEvent e) {
        updateDisplayValue();
    }

    private void updateDisplayValue() {
        boolean minimumSelected = minimum.isSelected();
        boolean maximumSelected = maximum.isSelected();
        int newvalue = (minimumSelected ? ORConnectivitiesDisplay.MIN : 0)
                | (maximumSelected ? ORConnectivitiesDisplay.MAX : 0);
        optionComponent.setValue(ORConnectivitiesDisplay.getInstance(newvalue), 0);
        closeBy.setEnabled(minimum.isSelected() | maximum.isSelected());
        farAway.setEnabled(minimum.isSelected() | maximum.isSelected());
        numericRepresentation.setEnabled(minimum.isSelected() | maximum.isSelected());

    }

    void closeBy_actionPerformed(ActionEvent e) {
        optionComponent.setValue(ORConnectivityPosition
                .getInstance(ORConnectivityPosition.CLOSE_BY), 1);
    }

    void farAway_actionPerformed(ActionEvent e) {
        optionComponent.setValue(ORConnectivityPosition
                .getInstance(ORConnectivityPosition.FAR_AWAY), 1);
    }

    void numericRepresentation_actionPerformed(ActionEvent e) {
        int newSelection = numericRepresentation.getSelectedIndex();
        optionComponent.setValue(ORNumericRepresentation.getInstance(newSelection), 2);
    }

}
