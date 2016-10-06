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

package org.modelsphere.sms.preference;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.preference.OptionPanel;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.international.LocaleMgr;

public class DiagramAutomaticCreationOptionGroup extends OptionGroup {
    private static final String CREATE_DATA_MODEL_DIAGRAMS = "CreateDataModelDiagrams"; // NOT
    // LOCALIZABLE
    private static final Boolean CREATE_DATA_MODEL_DIAGRAMS_DEFAULT = Boolean.TRUE;
    private static final String CREATE_DOMAIN_MODEL_DIAGRAMS = "CreateDomainModelDiagrams"; // NOT
    // LOCALIZABLE
    private static final Boolean CREATE_DOMAIN_MODEL_DIAGRAMS_DEFAULT = Boolean.TRUE;
    private static final String CREATE_COMMONITEM_MODEL_DIAGRAMS = "CreateCommonItemModelDiagrams"; // NOT
    // LOCALIZABLE
    private static final Boolean CREATE_COMMONITEM_MODEL_DIAGRAMS_DEFAULT = Boolean.TRUE;
    private static final String CREATE_JAVA_DIAGRAMS = "CreateJavaDiagrams"; // NOT
    // LOCALIZABLE
    private static final Boolean CREATE_JAVA_DIAGRAMS_DEFAULT = Boolean.TRUE;
    private static final String CREATE_BPM_DIAGRAMS = "CreateBPMDiagrams"; // NOT
    // LOCALIZABLE
    private static final Boolean CREATE_BPM_DIAGRAMS_DEFAULT = Boolean.TRUE;
    private static final String CREATE_BPM_DIAGRAM_FRAMES = "CreateBPMDiagramsFrames"; // NOT
    // LOCALIZABLE
    private static final Boolean CREATE_BPM_DIAGRAM_FRAMES_DEFAULT = Boolean.TRUE;

    @SuppressWarnings("serial")
    private static class DiagramAutomaticCreationOptionPanel extends OptionPanel implements
            ActionListener {
        //private JPanel diagOptionPanel = new JPanel();
        private JCheckBox createDataModelDiagramCBox = new JCheckBox();
        private JCheckBox createDomainModelDiagramCBox = new JCheckBox();
        private JCheckBox createCommonItemModelDiagramCBox = new JCheckBox();
        private JCheckBox createJavaDiagramCBox = new JCheckBox();
        private JCheckBox createBPMDiagramCBox = new JCheckBox();
        private JCheckBox createBPMDiagramFrameCBox = new JCheckBox();
        private JButton defButton = new JButton(LocaleMgr.misc.getString("Default"));

        DiagramAutomaticCreationOptionPanel() {
            setLayout(new GridBagLayout());
            JPanel diagramCreationPanel = new JPanel(new GridBagLayout());
            // diagramCreationPanel.setBorder(new TitledBorder(" " +
            // LocaleMgr.screen.getString("DiagramCreation") + " "));

            createDataModelDiagramCBox.setText(LocaleMgr.misc.getString("AutoDiagramDataModel"));
            createDomainModelDiagramCBox
                    .setText(LocaleMgr.misc.getString("AutoDiagramDomainModel"));
            createCommonItemModelDiagramCBox.setText(LocaleMgr.misc
                    .getString("AutoDiagramCommonItemModel"));
            createJavaDiagramCBox.setText(LocaleMgr.misc.getString("AutoDiagramJava"));
            createBPMDiagramCBox.setText(LocaleMgr.misc.getString("AutoDiagramBPM"));
            createBPMDiagramFrameCBox.setText(LocaleMgr.misc.getString("AutoDiagramFrameBPM"));

            createDataModelDiagramCBox.setVisible(true);
            createDomainModelDiagramCBox.setVisible(true);
            createCommonItemModelDiagramCBox.setVisible(true);
            createJavaDiagramCBox.setVisible(true);
            createBPMDiagramCBox.setVisible(true);
            createBPMDiagramFrameCBox.setVisible(true);

            diagramCreationPanel.add(createDataModelDiagramCBox, new GridBagConstraints(0, 0, 1, 1,
                    1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                    new Insets(18, 3, 6, 5), 0, 0));
            diagramCreationPanel.add(createDomainModelDiagramCBox, new GridBagConstraints(0, 1, 1,
                    1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 3, 6, 5), 0, 0));
            diagramCreationPanel.add(createCommonItemModelDiagramCBox, new GridBagConstraints(0, 2,
                    1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 3, 6, 5), 0, 0));
            
            
            if (ScreenPerspective.isFullVersion()) {
                diagramCreationPanel.add(createJavaDiagramCBox, new GridBagConstraints(0, 3, 1, 1, 1.0,
                        0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(
                                createDataModelDiagramCBox.isVisible() ? 0 : 6, 3, 6, 5), 0, 0));
                diagramCreationPanel.add(createBPMDiagramCBox, new GridBagConstraints(0, 4, 1, 1, 1.0,
                        0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(
                                createDataModelDiagramCBox.isVisible()
                                        || createJavaDiagramCBox.isVisible() ? 0 : 6, 3, 6, 5), 0, 0));
                diagramCreationPanel.add(createBPMDiagramFrameCBox, new GridBagConstraints(0, 5, 1, 1,
                        1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0,
                                3, 6, 5), 0, 0));
            } //end if

            add(diagramCreationPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
                            0, 5), 0, 0));
            add(Box.createVerticalGlue(), new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
                            0, 0), 0, 0));
            add(defButton, new GridBagConstraints(0, 3, 4, 0, 1.0, 1.0,
                    GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(12, 0, 6, 5),
                    0, 0));

            createDataModelDiagramCBox.addActionListener(this);
            createDomainModelDiagramCBox.addActionListener(this);
            createCommonItemModelDiagramCBox.addActionListener(this);
            createJavaDiagramCBox.addActionListener(this);
            createBPMDiagramCBox.addActionListener(this);
            createBPMDiagramFrameCBox.addActionListener(this);
            defButton.addActionListener(this);

        }

        public void init() {
            PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();

            createDataModelDiagramCBox.setSelected((preferences.getPropertyBoolean(
                    DiagramAutomaticCreationOptionGroup.class, CREATE_DATA_MODEL_DIAGRAMS,
                    CREATE_DATA_MODEL_DIAGRAMS_DEFAULT).booleanValue()));
            createDomainModelDiagramCBox.setSelected((preferences.getPropertyBoolean(
                    DiagramAutomaticCreationOptionGroup.class, CREATE_DOMAIN_MODEL_DIAGRAMS,
                    CREATE_DOMAIN_MODEL_DIAGRAMS_DEFAULT).booleanValue()));
            createCommonItemModelDiagramCBox.setSelected((preferences.getPropertyBoolean(
                    DiagramAutomaticCreationOptionGroup.class, CREATE_COMMONITEM_MODEL_DIAGRAMS,
                    CREATE_COMMONITEM_MODEL_DIAGRAMS_DEFAULT).booleanValue()));
            createJavaDiagramCBox.setSelected((preferences.getPropertyBoolean(
                    DiagramAutomaticCreationOptionGroup.class, CREATE_JAVA_DIAGRAMS,
                    CREATE_JAVA_DIAGRAMS_DEFAULT).booleanValue()));
            createBPMDiagramCBox.setSelected((preferences.getPropertyBoolean(
                    DiagramAutomaticCreationOptionGroup.class, CREATE_BPM_DIAGRAMS,
                    CREATE_BPM_DIAGRAMS_DEFAULT).booleanValue()));
            createBPMDiagramFrameCBox.setSelected((preferences.getPropertyBoolean(
                    DiagramAutomaticCreationOptionGroup.class, CREATE_BPM_DIAGRAM_FRAMES,
                    CREATE_BPM_DIAGRAM_FRAMES_DEFAULT).booleanValue()));

        }

        public void actionPerformed(ActionEvent e) {
            PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();

            Object source = e.getSource();
            if (source == createDataModelDiagramCBox)
                fireOptionChanged(preferences, DiagramAutomaticCreationOptionGroup.class,
                        CREATE_DATA_MODEL_DIAGRAMS, new Boolean(createDataModelDiagramCBox
                                .isSelected()));
            else if (source == createDomainModelDiagramCBox)
                fireOptionChanged(preferences, DiagramAutomaticCreationOptionGroup.class,
                        CREATE_DOMAIN_MODEL_DIAGRAMS, new Boolean(createDomainModelDiagramCBox
                                .isSelected()));
            else if (source == createCommonItemModelDiagramCBox)
                fireOptionChanged(preferences, DiagramAutomaticCreationOptionGroup.class,
                        CREATE_COMMONITEM_MODEL_DIAGRAMS, new Boolean(
                                createCommonItemModelDiagramCBox.isSelected()));
            else if (source == createJavaDiagramCBox)
                fireOptionChanged(preferences, DiagramAutomaticCreationOptionGroup.class,
                        CREATE_JAVA_DIAGRAMS, new Boolean(createJavaDiagramCBox.isSelected()));
            else if (source == createBPMDiagramCBox)
                fireOptionChanged(preferences, DiagramAutomaticCreationOptionGroup.class,
                        CREATE_BPM_DIAGRAMS, new Boolean(createBPMDiagramCBox.isSelected()));
            else if (source == createBPMDiagramFrameCBox)
                fireOptionChanged(preferences, DiagramAutomaticCreationOptionGroup.class,
                        CREATE_BPM_DIAGRAM_FRAMES, new Boolean(createBPMDiagramFrameCBox
                                .isSelected()));
            else if (source == defButton) {
                createDataModelDiagramCBox.setSelected(CREATE_DATA_MODEL_DIAGRAMS_DEFAULT
                        .booleanValue());
                createDomainModelDiagramCBox.setSelected(CREATE_DOMAIN_MODEL_DIAGRAMS_DEFAULT
                        .booleanValue());
                createCommonItemModelDiagramCBox
                        .setSelected(CREATE_COMMONITEM_MODEL_DIAGRAMS_DEFAULT.booleanValue());
                createJavaDiagramCBox.setSelected(CREATE_JAVA_DIAGRAMS_DEFAULT.booleanValue());
                createBPMDiagramCBox.setSelected(CREATE_BPM_DIAGRAMS_DEFAULT.booleanValue());
                createBPMDiagramFrameCBox.setSelected(CREATE_BPM_DIAGRAM_FRAMES_DEFAULT
                        .booleanValue());

                fireOptionChanged(preferences, DiagramAutomaticCreationOptionGroup.class,
                        CREATE_DATA_MODEL_DIAGRAMS, CREATE_DATA_MODEL_DIAGRAMS_DEFAULT);
                fireOptionChanged(preferences, DiagramAutomaticCreationOptionGroup.class,
                        CREATE_DOMAIN_MODEL_DIAGRAMS, CREATE_DOMAIN_MODEL_DIAGRAMS_DEFAULT);
                fireOptionChanged(preferences, DiagramAutomaticCreationOptionGroup.class,
                        CREATE_COMMONITEM_MODEL_DIAGRAMS, CREATE_COMMONITEM_MODEL_DIAGRAMS_DEFAULT);
                fireOptionChanged(preferences, DiagramAutomaticCreationOptionGroup.class,
                        CREATE_JAVA_DIAGRAMS, CREATE_JAVA_DIAGRAMS_DEFAULT);
                fireOptionChanged(preferences, DiagramAutomaticCreationOptionGroup.class,
                        CREATE_BPM_DIAGRAMS, CREATE_BPM_DIAGRAMS_DEFAULT);
                fireOptionChanged(preferences, DiagramAutomaticCreationOptionGroup.class,
                        CREATE_BPM_DIAGRAM_FRAMES, CREATE_BPM_DIAGRAM_FRAMES_DEFAULT);
            }
        }

    };

    public DiagramAutomaticCreationOptionGroup() {
        super(LocaleMgr.screen.getString("DiagramCreation"));
    }

    protected OptionPanel createOptionPanel() {
        return new DiagramAutomaticCreationOptionPanel();
    }

    // Service Methods to access this options group values

    public static boolean isCreateDataModelDiagram() {
        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
        return preferences.getPropertyBoolean(DiagramAutomaticCreationOptionGroup.class,
                CREATE_DATA_MODEL_DIAGRAMS, CREATE_DATA_MODEL_DIAGRAMS_DEFAULT).booleanValue();
    }

    public static boolean isCreateDomainModelDiagram() {
        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
        return preferences.getPropertyBoolean(DiagramAutomaticCreationOptionGroup.class,
                CREATE_DOMAIN_MODEL_DIAGRAMS, CREATE_DOMAIN_MODEL_DIAGRAMS_DEFAULT).booleanValue();
    }

    public static boolean isCreateCommonItemModelDiagram() {
        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
        return preferences.getPropertyBoolean(DiagramAutomaticCreationOptionGroup.class,
                CREATE_COMMONITEM_MODEL_DIAGRAMS, CREATE_COMMONITEM_MODEL_DIAGRAMS_DEFAULT)
                .booleanValue();
    }

    public static boolean isCreateJavaDiagram() {
        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
        return preferences.getPropertyBoolean(DiagramAutomaticCreationOptionGroup.class,
                CREATE_JAVA_DIAGRAMS, CREATE_JAVA_DIAGRAMS_DEFAULT).booleanValue();
    }

    public static boolean isCreateBPMModelContextDiagram() {
        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
        return preferences.getPropertyBoolean(DiagramAutomaticCreationOptionGroup.class,
                CREATE_BPM_DIAGRAMS, CREATE_BPM_DIAGRAMS_DEFAULT).booleanValue();
    }

    public static boolean isCreateBPMDiagramFrame() {
        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
        return preferences.getPropertyBoolean(DiagramAutomaticCreationOptionGroup.class,
                CREATE_BPM_DIAGRAM_FRAMES, CREATE_BPM_DIAGRAM_FRAMES_DEFAULT).booleanValue();
    }

}
