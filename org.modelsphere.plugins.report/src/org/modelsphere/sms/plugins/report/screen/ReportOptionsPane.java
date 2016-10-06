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

package org.modelsphere.sms.plugins.report.screen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.text.*;

import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.awt.ImagePreviewer;
import org.modelsphere.jack.awt.tree.CheckTreeNode;
import org.modelsphere.jack.io.IoUtil;
import org.modelsphere.jack.util.StringUtil;
import org.modelsphere.sms.plugins.report.LocaleMgr;
import org.modelsphere.sms.plugins.report.model.ReportModel;

public class ReportOptionsPane extends JPanel implements ActionListener {

    // Components associated with each option
    private JLabel generateIndexLabel = new JLabel();
    private JCheckBox generateIndexCheckBox = new JCheckBox();

    private JCheckBox useBackgroundImageCheckBox = new JCheckBox(LocaleMgr.misc
            .getString("useBackgroundImage"));
    private JTextField backgroundImageTextField = new JTextField();
    private JButton backgroundImageBrowseButton = new JButton();

    private JLabel backgroundColorLabel = new JLabel();
    private JTextField backgroundColorTextField = new JTextField();
    private JButton backgroundColorBrowseButton = new JButton();

    private JLabel outputDirectoryLabel = new JLabel();
    private JTextField outputDirectoryTextField = new JTextField();
    private JButton outputDirectoryBrowseButton = new JButton();

    private JLabel diagramsDirectoryLabel = new JLabel();
    private JTextField diagramsDirectoryTextField = new JTextField() {
        protected Document createDefaultModel() {
            return new PlainDocument() {
                public void insertString(int offset, String str, AttributeSet a)
                        throws BadLocationException {
                    if (!StringUtil.getValideFileName(str, true).equals(str))
                        Toolkit.getDefaultToolkit().beep();
                    else
                        super.insertString(offset, str, a);
                }
            };
        }

    };

    private JLabel welcomeNodeLabel = new JLabel();
    private JTextField welcomeNodeTextField = new JTextField();
    private JButton welcomeNodeBrowseButton = new JButton();

    // Buttons

    // Layouts
    private GridBagLayout gridBagLayout = new GridBagLayout();

    private CheckTreeNode selectedNode;

    private ReportModel model;
    private Dialog mainDialog;

    public ReportOptionsPane(ReportModel model, Dialog mainDialog) {
        this.mainDialog = mainDialog;
        this.model = model;

        refreshOptions();

        jbInit();
    }

    public void refreshOptions() {
        refreshOptions(null);
    }

    public void refreshOptions(ReportModel newModel) {
        if (newModel != null)
            model = newModel;
        // initialize components associated options
        generateIndexCheckBox.setSelected(this.model.getOptions().getGenerateIndex());
        backgroundImageTextField.setText(this.model.getOptions().getBackgroundImage());
        diagramsDirectoryTextField.setText(this.model.getOptions().getDiagramDirectory());
        outputDirectoryTextField.setText(this.model.getOptions().getOutputDirectory());
        useBackgroundImageCheckBox.setSelected(this.model.getOptions().getUseBackgroundImage());
        backgroundImageBrowseButton.setEnabled(useBackgroundImageCheckBox.isSelected());
        backgroundImageTextField.setEnabled(useBackgroundImageCheckBox.isSelected());

        selectedNode = this.model.getOptions().getWelcomeConceptNode();
        if (selectedNode != null)
            welcomeNodeTextField.setText(selectedNode.toString());

    }

    void jbInit() {
        generateIndexLabel.setText(LocaleMgr.misc.getString("GenerateIndexPage")); // NOT LOCALIZABLE
        outputDirectoryLabel.setText(LocaleMgr.misc.getString("OutputDirectory"));
        diagramsDirectoryLabel.setText(LocaleMgr.misc.getString("DiagramsDirectory")); // NOT LOCALIZABLE
        backgroundImageBrowseButton.setText(LocaleMgr.misc.getString("3dots")); // NOT LOCALIZABLE

        welcomeNodeLabel.setText(LocaleMgr.misc.getString("HomePage")); // NOT LOCALIZABLE
        welcomeNodeBrowseButton.setText(LocaleMgr.misc.getString("3dots")); // NOT LOCALIZABLE
        outputDirectoryBrowseButton.setText(LocaleMgr.misc.getString("3dots")); // NOT LOCALIZABLE

        backgroundImageTextField.setEditable(false);
        outputDirectoryTextField.setEditable(false);
        welcomeNodeTextField.setEditable(false);

        this.setLayout(gridBagLayout);
        GridBagLayout gridBagLayout1 = new GridBagLayout();
        JPanel panel = new JPanel(gridBagLayout1);

        // Main Panel
        panel.add(generateIndexLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 12, 12, 12),
                0, 0));
        panel.add(generateIndexCheckBox, new GridBagConstraints(1, 0, GridBagConstraints.REMAINDER,
                1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,
                        0, 0, 0), 0, 0));

        panel.add(welcomeNodeLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 12, 12, 12),
                0, 0));
        panel.add(welcomeNodeTextField, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 12, 12),
                0, 0));
        panel.add(welcomeNodeBrowseButton, new GridBagConstraints(2, 1,
                GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 12, 12), 0, 0));

        panel.add(outputDirectoryLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 12, 12, 12),
                0, 0));
        panel.add(outputDirectoryTextField, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 12, 12),
                0, 0));
        panel.add(outputDirectoryBrowseButton, new GridBagConstraints(2, 2,
                GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 12, 12), 0, 0));

        panel.add(diagramsDirectoryLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 12, 12, 12),
                0, 0));
        panel.add(diagramsDirectoryTextField, new GridBagConstraints(1, 3, 2, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 12, 12),
                0, 0));

        panel.add(useBackgroundImageCheckBox, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 12, 6, 12),
                0, 0));

        panel.add(backgroundImageTextField, new GridBagConstraints(0, 5, 2, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 24, 0, 12),
                0, 0));
        panel.add(backgroundImageBrowseButton, new GridBagConstraints(2, 5,
                GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 12), 0, 0));

        this.add(panel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        useBackgroundImageCheckBox.addActionListener(this);
        backgroundImageBrowseButton.addActionListener(this);
        welcomeNodeBrowseButton.addActionListener(this);
        outputDirectoryBrowseButton.addActionListener(this);

    }

    private void selectNode() {
        ConceptChooser chooser = new ConceptChooser(model);
        chooser.showDialog(mainDialog);

        selectedNode = chooser.getSelectedConceptNode();
        if (selectedNode != null)
            welcomeNodeTextField.setText(selectedNode.toString());
        else
            welcomeNodeTextField.setText(""); // NOT LOCALIZABLE
    }

    private void selectBackgroundImage() {
        String htmlDirectory = outputDirectoryTextField.getText();
        JFileChooser chooser = new JFileChooser(htmlDirectory);
        File chosenFile;

        chooser.setAccessory(new ImagePreviewer(chooser));
        chooser.addChoosableFileFilter(ExtensionFileFilter.allImagesFilter);
        chooser.setFileFilter(ExtensionFileFilter.allImagesFilter);
        chooser.setMultiSelectionEnabled(false);
        chooser.showDialog(this, LocaleMgr.misc.getString("Select")); // NOT LOCALIZABLE

        chosenFile = chooser.getSelectedFile();
        if (chosenFile != null) {
            backgroundImageTextField.setText(chosenFile.toString());
        }
    }

    private void selectOutputDirectory() {
        String htmlDirectory = outputDirectoryTextField.getText();
        File file = new File(htmlDirectory);
        String buttonName = LocaleMgr.misc.getString("Select");
        File chosenDir = IoUtil.selectDirectory(this.mainDialog, htmlDirectory, buttonName,
                buttonName);
        if (chosenDir != null && chosenDir.canWrite()) {
            outputDirectoryTextField.setText(chosenDir.toString());
        }
    }

    // ***************************************************************************
    // implements AtionListener
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == backgroundImageBrowseButton)
            selectBackgroundImage();
        else if (source == useBackgroundImageCheckBox) {
            backgroundImageBrowseButton.setEnabled(useBackgroundImageCheckBox.isSelected());
            backgroundImageTextField.setEnabled(useBackgroundImageCheckBox.isSelected());
        } else if (source == outputDirectoryBrowseButton)
            selectOutputDirectory();
        else if (source == welcomeNodeBrowseButton)
            selectNode();
    }

    // end - implements AtionListener
    // ***************************************************************************

    protected ReportModel setOptions(ReportModel model) {
        model.getOptions().setGenerateIndex(generateIndexCheckBox.isSelected());
        model.getOptions().setUseBackgroundImage(useBackgroundImageCheckBox.isSelected());
        model.getOptions().setBackgroundImage(backgroundImageTextField.getText());
        model.getOptions().setOutputDirectory(outputDirectoryTextField.getText());
        model.getOptions().setDiagramDirectory(diagramsDirectoryTextField.getText());
        model.getOptions().setWelcomeConceptNode(selectedNode);
        return model;
    }

}
