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

package org.modelsphere.sms.plugins.statistics;

import java.awt.*;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.*;

import org.modelsphere.jack.debug.ConceptPair;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.ExceptionMessage;

/**
 * @author Grandite
 * 
 */
@SuppressWarnings("serial")
public class DiagramStatisticsDialog extends JDialog {

    private javax.swing.JPanel jContentPane = null;

    private JPanel controlButtonPanel = null;
    private JButton buttonClose = null;
    private JButton buttonSave = null;
    private JPanel panelProperties = null;
    private JPanel panelOccurrences = null;
    private JList listOccurrences = null;
    private JPanel panelCommonConcepts = null;
    private JList listProperties = null;
    private JList listCommonConcepts = null;

    private DiagramStatisticsPlugin dgmStatsProvider;

    /**
     * This method initializes jPanel1
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getControlButtonPanel() {
        if (controlButtonPanel == null) {
            FlowLayout flowLayout10 = new FlowLayout();
            controlButtonPanel = new JPanel();
            controlButtonPanel.setLayout(flowLayout10);
            flowLayout10.setAlignment(java.awt.FlowLayout.RIGHT);
            controlButtonPanel.add(getButtonSave(), null);
            controlButtonPanel.add(getButtonClose(), null);
        }
        return controlButtonPanel;
    }

    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getButtonClose() {
        if (buttonClose == null) {
            buttonClose = new JButton();
            buttonClose.setText("Close");
            buttonClose.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            buttonClose.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    dispose();
                }
            });
        }
        return buttonClose;
    }

    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getButtonSave() {
        if (buttonSave == null) {
            buttonSave = new JButton();
            buttonSave.setText("Save...");
            buttonSave.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            buttonSave.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {

                    DiagramStatisticsSaveXML saveXML = new DiagramStatisticsSaveXML(
                            dgmStatsProvider);

                    try {
                        saveXML.save();
                    } catch (ExceptionMessage em) {
                        JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(), em
                                .toString(), "Open ModelSphere", ExceptionMessage.E_ERROR);
                    }

                }
            });
        }
        return buttonSave;
    }

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getPanelProperties() {
        if (panelProperties == null) {
            panelProperties = new JPanel();
            panelProperties.setLayout(new GridBagLayout());

            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.gridy = 1;
            gridBagConstraints5.weightx = 1.0;
            gridBagConstraints5.weighty = 1.0;
            gridBagConstraints5.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints5.ipadx = 0;
            gridBagConstraints5.ipady = 0;
            gridBagConstraints5.insets = new java.awt.Insets(0, 6, 12, 6);

            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 0.0;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints.ipadx = 0;
            gridBagConstraints.ipady = 0;
            gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);

            panelProperties.add(new JLabel("Diagram Properties:"), gridBagConstraints);
            panelProperties.add(new JScrollPane(getListProperties()), gridBagConstraints5);
        }
        return panelProperties;
    }

    /**
     * This method initializes jPanel1
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getPanelOccurrences() {
        if (panelOccurrences == null) {
            panelOccurrences = new JPanel();
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            panelOccurrences.setLayout(new GridBagLayout());
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.gridy = 1;
            gridBagConstraints5.weightx = 1.0;
            gridBagConstraints5.weighty = 1.0;
            gridBagConstraints5.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints5.ipadx = 0;
            gridBagConstraints5.ipady = 0;
            gridBagConstraints5.insets = new java.awt.Insets(0, 6, 12, 6);

            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            panelOccurrences.setLayout(new GridBagLayout());
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 0.0;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints.ipadx = 0;
            gridBagConstraints.ipady = 0;
            gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
            panelOccurrences.add(new JLabel("Concepts Occurrences:"), gridBagConstraints);
            panelOccurrences.add(new JScrollPane(getListOccurrences()), gridBagConstraints5);
        }
        return panelOccurrences;
    }

    /**
     * This method initializes jList2
     * 
     * @return javax.swing.JList
     */
    private JList getListOccurrences() {
        if (listOccurrences == null) {
            listOccurrences = new JList();
            listOccurrences.setEnabled(true);
        }
        return listOccurrences;
    }

    /**
     * This method initializes jPanel2
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getPanelCommonConcepts() {
        if (panelCommonConcepts == null) {
            panelCommonConcepts = new JPanel();
            panelCommonConcepts.setLayout(new GridBagLayout());

            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.gridy = 1;
            gridBagConstraints5.weightx = 1.0;
            gridBagConstraints5.weighty = 1.0;
            gridBagConstraints5.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints5.ipadx = 0;
            gridBagConstraints5.ipady = 0;
            gridBagConstraints5.insets = new java.awt.Insets(0, 6, 12, 6);

            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 0.0;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints.ipadx = 0;
            gridBagConstraints.ipady = 0;
            gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);

            panelCommonConcepts.add(new JLabel("Common Concepts:"), gridBagConstraints);
            panelCommonConcepts.add(new JScrollPane(getListCommonConcepts()), gridBagConstraints5);

        }
        return panelCommonConcepts;
    }

    /**
     * This method initializes jList
     * 
     * @return javax.swing.JList
     */
    private JList getListProperties() {
        if (listProperties == null) {
            listProperties = new JList();
            listProperties.setBackground(java.awt.Color.white);
            listProperties.setEnabled(true);
        }
        return listProperties;
    }

    /**
     * This method initializes jList1
     * 
     * @return javax.swing.JList
     */
    private JList getListCommonConcepts() {
        if (listCommonConcepts == null) {
            listCommonConcepts = new JList();
            listCommonConcepts.setBackground(java.awt.Color.white);
            listCommonConcepts.setEnabled(true);
        }
        return listCommonConcepts;
    }

    /**
     * This is the default constructor
     */
    public DiagramStatisticsDialog() {
        super();
        initialize();
    }

    public DiagramStatisticsDialog(Frame frame, String title, boolean modal) {
        super(frame, title, modal);
        this.pack();
        initialize();
        this.setLocationRelativeTo(frame);
    }

    public DiagramStatisticsDialog(Frame frame, String title, DiagramStatisticsPlugin provider,
            boolean modal) {
        super(frame, title, modal);
        this.pack();
        dgmStatsProvider = provider;
        initialize();
        this.setLocationRelativeTo(frame);
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        //this.setTitle("Diagram statistics");
        this.setContentPane(getJContentPane());
        this.setSize(600, 450);
        this.setContentPane(getJContentPane());

        //// 
        // initialize the dialog with the provider data 

        if (dgmStatsProvider == null) {
            throw new RuntimeException("No statistics provider was specified.");
        }

        ////
        // populate the list of properties (common to all diagrams)

        DefaultListModel listModelProperties = new DefaultListModel();

        int type = dgmStatsProvider.getType();
        String sType = "Type: ";

        switch (type) {
        case DiagramStatisticsPlugin.DIAGRAM_TYPE_OO_UML_CLASS:
            sType += "Class";
            break;
        case DiagramStatisticsPlugin.DIAGRAM_TYPE_BE_UML_USECASE:
            sType += "Use Case";
            break;
        case DiagramStatisticsPlugin.DIAGRAM_TYPE_BE_UML_STATECHART:
            sType += "Statechart";
            break;
        case DiagramStatisticsPlugin.DIAGRAM_TYPE_BE_UML_PROCESS:
            sType += "Process";
            break;
        case DiagramStatisticsPlugin.DIAGRAM_TYPE_BE_UML_COLLABORATION:
            sType += "Collaboration";
            break;
        case DiagramStatisticsPlugin.DIAGRAM_TYPE_BE_UML_ACTIVITY:
            sType += "Activity";
            break;
        case DiagramStatisticsPlugin.DIAGRAM_TYPE_BE_UML_SEQUENCE:
            sType += "Sequence";
            break;
        case DiagramStatisticsPlugin.DIAGRAM_TYPE_BE_UML_COMPONENT:
            sType += "Component";
            break;
        case DiagramStatisticsPlugin.DIAGRAM_TYPE_BE_UML_DEPLOYMENT:
            sType += "Deployment";
            break;
        case DiagramStatisticsPlugin.DIAGRAM_TYPE_OR_DATA:
            sType += "Data";
            break;
        case DiagramStatisticsPlugin.DIAGRAM_TYPE_OR_DOMAIN:
            sType += "Domain";
            break;
        case DiagramStatisticsPlugin.DIAGRAM_TYPE_OR_COMMONITEMS:
            sType += "Common Item";
            break;
        }

        listModelProperties.addElement(sType);

        listModelProperties.addElement("Notation: " + dgmStatsProvider.getNotation());
        listModelProperties.addElement("Default style: " + dgmStatsProvider.getDefaultStyle());
        Dimension dim = dgmStatsProvider.getDimensions();
        listModelProperties.addElement("Dimensions: " + dim.getHeight() + " x " + dim.getWidth());
        listModelProperties.addElement("Number of sheets: " + dgmStatsProvider.getSheetsCount());
        listProperties.setModel(listModelProperties);

        ////
        // populate the list of common concepts (specific to a kind of diagrams)

        DefaultListModel listModelCommonConcepts = new DefaultListModel();
        DefaultListModel listModelOccurrences = new DefaultListModel();

        Vector results = dgmStatsProvider.getResults();
        Enumeration enumResults = results.elements();
        while (enumResults.hasMoreElements()) {
            ConceptPair pair = (ConceptPair) enumResults.nextElement();
            String name = pair.getConceptName();
            int nCount = pair.getOccurrencesCount();
            if (pair.IsSpecialized()) {
                listModelOccurrences.addElement(name + ": " + nCount);
            } else {
                listModelCommonConcepts.addElement(name + ": " + nCount);
            }
        }
        listCommonConcepts.setModel(listModelCommonConcepts);
        listOccurrences.setModel(listModelOccurrences);

    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJContentPane() {
        if (jContentPane == null) {
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(new GridBagLayout());
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.gridy = 2;
            gridBagConstraints6.gridwidth = 2;
            gridBagConstraints6.ipadx = 0;
            gridBagConstraints6.ipady = 0;
            gridBagConstraints6.insets = new java.awt.Insets(0, 6, 6, 6);
            gridBagConstraints6.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints6.weighty = 0.0D;
            gridBagConstraints7.gridx = 0;
            gridBagConstraints7.gridy = 0;
            gridBagConstraints7.ipadx = 0;
            gridBagConstraints7.ipady = 0;
            gridBagConstraints7.insets = new java.awt.Insets(12, 6, 6, 6);
            gridBagConstraints7.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints7.weighty = 1.0D;
            gridBagConstraints7.weightx = 1.0D;
            gridBagConstraints8.gridx = 1;
            gridBagConstraints8.gridy = 0;
            gridBagConstraints8.gridheight = 2;
            gridBagConstraints8.ipadx = 0;
            gridBagConstraints8.ipady = 0;
            gridBagConstraints8.insets = new java.awt.Insets(12, 0, 6, 6);
            gridBagConstraints8.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints8.weighty = 1.0D;
            gridBagConstraints8.weightx = 1.0D;
            gridBagConstraints9.gridx = 0;
            gridBagConstraints9.gridy = 1;
            gridBagConstraints9.ipadx = 0;
            gridBagConstraints9.ipady = 0;
            gridBagConstraints9.insets = new java.awt.Insets(0, 6, 6, 6);
            gridBagConstraints9.weighty = 1.0D;
            gridBagConstraints9.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints9.weightx = 1.0D;
            jContentPane.add(getControlButtonPanel(), gridBagConstraints6);
            jContentPane.add(getPanelProperties(), gridBagConstraints7);
            jContentPane.add(getPanelOccurrences(), gridBagConstraints8);
            jContentPane.add(getPanelCommonConcepts(), gridBagConstraints9);
        }
        return jContentPane;
    }
} //  @jve:decl-index=0:visual-constraint="1,12"
