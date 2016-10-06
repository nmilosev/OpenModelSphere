/*************************************************************************

Copyright (C) 2009 by neosapiens inc.

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

You can reach neosapiens inc. at: 

neosapiens inc.
1236 Gaudias-Petitclerc
Qu&eacute;bec, Qc, G1Y 3G2
CANADA
Telephone: 418-561-8403
Fax: 418-650-2375
http://www.neosapiens.com/
Email: marco.savard@neosapiens.com
       gino.pelletier@neosapiens.com

 **********************************************************************/
package com.neosapiens.plugins.codegen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.dirchooser.DirectoryChooser2;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.properties.PropertySet;
import org.modelsphere.jack.srtool.ApplicationContext;

import com.neosapiens.plugins.codegen.international.LocaleMgr;

public class CodeGenerationDialog extends JDialog implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static final String CODE_GENERATION = "CODE_GENERATION";
    private static final int FIX_WIDTH = 500;

    private JTextField folderText;
    private JButton folderSelectButton;
    private JComboBox m_combo;
    private JTextArea m_descriptionArea;
    private ButtonPanel buttonPanel;

    private PropertySet properties;
    private File selectedFile = null;
    private List<GenerationTarget> m_targets = null;
    private List<GenerationTarget> m_visibleTargets = null;
    private int m_selectedTargetSystem = 0;

    //
    // CONSTRUCTORS
    //

    public CodeGenerationDialog(JFrame parent) {
        super(parent, LocaleMgr.misc.getString("CodeGeneration"), true);
        properties = PropertySet.getInstance(CodeGenerationDialog.class);

        setLayout(new GridBagLayout());
        initContents();
        initButtons();

        this.pack();
        this.setSize(FIX_WIDTH, this.getSize().height);
        AwtUtil.centerWindow(this);
    }

    public void setTargets(List<GenerationTarget> targets, DbObject[] dbos) {
        m_targets = targets;
        m_visibleTargets = new ArrayList<GenerationTarget>();
        m_combo.removeAllItems();

        for (GenerationTarget target : m_targets) {
            if (target.doesSupport(dbos)) {
                m_combo.addItem(target);
                m_visibleTargets.add(target);
            }
        } //end for
    } //end setTeargets

    private void initContents() {
        int y = 0;
        int w = 1, h = 1;
        double wx = 0.0, wy = 0.0;
        int nofill = GridBagConstraints.NONE;
        Insets insets = new Insets(3, 3, 3, 3);

        //template name
        JLabel label = new JLabel(LocaleMgr.misc.getString("TargetLabel"));
        this.add(label, new GridBagConstraints(0, y, 1, h, wx, wy, GridBagConstraints.WEST, nofill,
                new Insets(6, 6, 6, 6), 0, 0));

        m_combo = new JComboBox();
        this.add(m_combo, new GridBagConstraints(1, y, 2, h, wx, wy, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 6, 6, 6), 0, 0));

        y++;

        //description
        label = new JLabel(LocaleMgr.misc.getString("DescriptionLabel"));
        this.add(label, new GridBagConstraints(0, y, 1, h, wx, wy, GridBagConstraints.NORTHWEST,
                nofill, new Insets(6, 6, 18, 6), 0, 0));

        m_descriptionArea = new JTextArea();
        m_descriptionArea.setEditable(false);
        m_descriptionArea.setEnabled(false);
        m_descriptionArea.setLineWrap(true);
        m_descriptionArea.setRows(3);
        m_descriptionArea.setWrapStyleWord(true);
        m_descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 11));

        JScrollPane scrollPane = new JScrollPane(m_descriptionArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(scrollPane, new GridBagConstraints(1, y, 2, h, wx, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, new Insets(6, 6, 18, 6), 0, 0));

        y++;

        //output folder
        JLabel instructionLabel = new JLabel(LocaleMgr.misc.getString("SelectOutputFolder"));
        this.add(instructionLabel, new GridBagConstraints(0, y, 2, h, wx, wy,
                GridBagConstraints.WEST, nofill, new Insets(6, 6, 0, 6), 0, 0));

        y++;

        JLabel folderLabel = new JLabel(LocaleMgr.misc.getString("FolderLabel"));
        this.add(folderLabel, new GridBagConstraints(0, y, w, 1, wx, wy, GridBagConstraints.WEST,
                nofill, new Insets(3, 6, 18, 6), 0, 0));

        folderText = new JTextField();
        this.add(folderText, new GridBagConstraints(1, y, 1, h, 1.0, wy, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(3, 3, 18, 6), 0, 0));

        folderSelectButton = new JButton("...");
        this.add(folderSelectButton, new GridBagConstraints(2, y, w, h, wx, wy,
                GridBagConstraints.WEST, nofill, new Insets(3, 3, 18, 6), 0, 0));

        y++;

        buttonPanel = new ButtonPanel(this);
        this.add(buttonPanel, new GridBagConstraints(1, y, 2, h, wx, wy,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.VERTICAL, insets, 0, 0));

        //add listeners
        m_combo.addActionListener(this);
        folderSelectButton.addActionListener(this);
    }

    private List<Object> movedObjects = new ArrayList<Object>();

    @Override
    public void actionPerformed(ActionEvent ev) {
        Object src = ev.getSource();

        if (src.equals(m_combo)) {
            GenerationTarget target = (GenerationTarget) m_combo.getSelectedItem();
            if (target != null) {
                String description = target.getDescription();
                m_descriptionArea.setText(description);
            }
        } else if (src.equals(folderSelectButton)) {
            String filename = properties.getProperty(CODE_GENERATION);
            if (filename == null) {
                filename = ApplicationContext.getDefaultWorkingDirectory();
            }
            File selectedFolder = (filename == null) ? null : new File(filename);
            String title = LocaleMgr.misc.getString("SelectOutputFolder");
            String selectText = LocaleMgr.misc.getString("Select");
            boolean showFiles = false;
            selectedFolder = DirectoryChooser2.selectDirectory(this, selectedFolder, title,
                    selectText, showFiles);

            if (selectedFolder != null) {
                filename = selectedFolder.toString();
                properties.setProperty(CODE_GENERATION, filename);
                folderText.setText(filename);
            }
        } //end if

        movedObjects.clear();
        initButtons();
    } //actionPerformed()

    private void initButtons() {
        String filename = properties.getProperty(CODE_GENERATION);
        if (filename != null) {
            folderText.setText(filename);
        }

        boolean enabled = ((m_combo.getSelectedIndex() > -1) && (filename != null));
        buttonPanel.setGenerateEnabled(enabled);
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            selectedFile = null;

            if (m_selectedTargetSystem < m_combo.getItemCount()) {
                m_combo.setSelectedIndex(m_selectedTargetSystem);
            }
            initButtons();
        }

        super.setVisible(visible);
    }

    public GenerationTarget getSelectedTarget() {
        m_selectedTargetSystem = m_combo.getSelectedIndex();
        GenerationTarget target = (m_selectedTargetSystem == -1) ? null : m_visibleTargets
                .get(m_selectedTargetSystem);
        return target;
    }

    private static class ButtonPanel extends JPanel implements ActionListener {
        private JButton importButton, cancelButton;
        private CodeGenerationDialog m_parent;

        public ButtonPanel(CodeGenerationDialog parent) {
            m_parent = parent;
            int x = 0, y = 0;
            int w = 1, h = 1;
            double wx = 0.0, wy = 0.0;
            //int anchor = GridBagConstraints.CENTER; 
            int nofill = GridBagConstraints.NONE;
            Insets insets = new Insets(3, 3, 3, 3);

            this.setLayout(new GridBagLayout());

            importButton = new JButton(LocaleMgr.misc.getString("Generate"));
            //b1.setToolTipText("Move All to Right");
            this.add(importButton, new GridBagConstraints(1, y, 1, h, 1.0, 1.0,
                    GridBagConstraints.SOUTHEAST, nofill, insets, 0, 0));

            cancelButton = new JButton(LocaleMgr.misc.getString("Cancel"));
            //b1.setToolTipText("Move All to Right");
            this.add(cancelButton, new GridBagConstraints(2, y, 2, h, wx, 1.0,
                    GridBagConstraints.SOUTHEAST, nofill, insets, 0, 0));

            AwtUtil.normalizeComponentDimension(new JComponent[] { importButton, cancelButton });

            importButton.addActionListener(this);
            cancelButton.addActionListener(this);
        }

        public void setGenerateEnabled(boolean b) {
            importButton.setEnabled(b);
        }

        public void actionPerformed(ActionEvent ev) {
            Object src = ev.getSource();

            //move all to right list
            if (src.equals(importButton)) {
                String text = m_parent.folderText.getText();
                m_parent.selectedFile = new File(text);
                m_parent.setVisible(false);
            } else if (src.equals(cancelButton)) {
                m_parent.setVisible(false);
            } //end if

        } //actionPerformed()
    }

    //    //
    //    // UNIT TEST (HOW TO USE IT)
    //    //
    //    public static void main(String[] args) {
    //        //Schedule a job for the event-dispatching thread:
    //        //creating and showing this application's GUI.
    //        javax.swing.SwingUtilities.invokeLater(new Runnable() {
    //            public void run() {
    //                createAndShowGUI();
    //            }
    //        });
    //    }
    //
    //    /**
    //     * Create the GUI and show it. For thread safety, this method should be invoked from the
    //     * event-dispatching thread.
    //     */
    //    private static void createAndShowGUI() {
    //        try {
    //            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    //        } catch (Exception ex) {
    //        }
    //
    //        //Create and set up the window.
    //        JFrame frame = new JFrame("ImportJavaClassFilesDemo");
    //        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //
    //        //Create and set up the content pane.
    //        CodeGenerationDialog dialog = new CodeGenerationDialog(frame);
    //        List<GenerationTarget> targets = GenerationTarget.getTargets();
    //        dialog.setTargets(targets, null);
    //        dialog.setVisible(true);
    //    }

    public File getSelectedFile() {
        return selectedFile;
    }

}
