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

package org.modelsphere.sms.features.forward;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.beans.impl.PropertyDialog;
import org.modelsphere.jack.io.IoUtil;
import org.modelsphere.jack.srtool.forward.VariableScope;
import org.modelsphere.jack.templates.TemplateDialog;
import org.modelsphere.sms.features.international.LocaleMgr;
import org.modelsphere.sms.templates.GenericForwardEngineeringPlugin;

public class GenerateDialog extends JDialog {
    JPanel panel1 = new JPanel();
    private JLabel jLabel1 = new JLabel();
    private JLabel jLabel2 = new JLabel();
    private JButton jButton1 = new JButton();
    private JButton jButton2 = new JButton();
    private JButton jButton3 = new JButton();
    private JButton jButton4 = new JButton();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private static final String OUTPUT_DIRECTORY = LocaleMgr.screen.getString("OutputDirectory");
    private static final String SELECT = LocaleMgr.screen.getString("Select");
    private static final String GENERATE = LocaleMgr.screen.getString("Generate");
    private static final String CANCEL = LocaleMgr.screen.getString("Cancel");
    private static final String GENERATE_OPTIONS = LocaleMgr.screen.getString("GenerateOptions");
    private static final String CLOSE = LocaleMgr.screen.getString("Close");
    private static File g_selectedDirectory = null; //last selected output directory property
    private File m_actualDir;
    private ArrayList m_propertyList = null;
    private VariableScope m_variableList;

    //called by GenericDDLBasicForwardToolkit
    public GenerateDialog(Frame frame, GenericForwardEngineeringPlugin forward,
            String defaultDirectory, String title) {
        this(frame, title, true, (forward == null) ? null : forward.getVarScope());

        if (forward != null) {
            m_variableList = forward.getVarScope();

            if (g_selectedDirectory == null) {
                String directory = (forward == null) ? defaultDirectory : forward
                        .getRootDirFromUserProp();
                g_selectedDirectory = new File(directory);
            }

            jLabel2.setText(g_selectedDirectory.getAbsolutePath());
        }
    }

    //main internal constructor
    private GenerateDialog(Frame frame, String title, boolean modal, VariableScope variableList) {
        super(frame, title, modal);
        try {
            jbInit();
            init(variableList);
            addListeners();
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        panel1.setLayout(gridBagLayout1);
        jLabel1.setText(OUTPUT_DIRECTORY); //NOT LOCALIZABLE, unit test
        jLabel2.setBorder(BorderFactory.createLoweredBevelBorder());
        jButton1.setText(SELECT + "...");
        jButton2.setText(GENERATE_OPTIONS + "..."); //NOT LOCALIZABLE, unit test
        jButton3.setText(GENERATE); //NOT LOCALIZABLE, unit test
        jButton4.setText(CANCEL); //NOT LOCALIZABLE, unit test
        panel1.add(jButton1, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(6, 6, 6, 12), 0, 0));
        panel1.add(jLabel2, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 12, 6, 6), 0, 0));
        panel1.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(12, 12, 0, 6), 0,
                0));
        panel1.add(jButton2, new GridBagConstraints(0, 2, 1, 1, 0.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, new Insets(18, 12, 18, 6), 0, 0));
        panel1.add(jButton3, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(6, 6, 12, 6), 0,
                0));
        panel1.add(jButton4, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(12, 6, 12, 12),
                0, 0));
        this.getContentPane().add(panel1, BorderLayout.SOUTH);
    } //add jbInit()

    private void init(VariableScope variableList) {
        if (variableList == null) {
            jButton2.setEnabled(false);
            return;
        }

        m_propertyList = TemplateDialog.buildPropertyListFromVariables(variableList);
        if ((m_propertyList == null) || (m_propertyList.isEmpty())) {
            jButton2.setEnabled(false);
        } //end if
    } //end init()

    private void addListeners() {
        final JDialog thisDialog = this;
        //Select Button
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String approveButtonText = SELECT;
                String defaultDirectory = g_selectedDirectory.getAbsolutePath();
                File selectedDirectory = IoUtil.selectDirectory(thisDialog, defaultDirectory,
                        approveButtonText, approveButtonText);
                if (selectedDirectory != null) {
                    g_selectedDirectory = selectedDirectory;
                    jLabel2.setText(g_selectedDirectory.getAbsolutePath());
                }
            } //end actionPerformed()
        });

        //Generate Options Button
        jButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if (!m_propertyList.isEmpty()) {
                    //create property panel
                    JDialog dialog2 = new PropertyDialog(thisDialog, GENERATE_OPTIONS, CLOSE,
                            m_propertyList);

                    //and display it
                    dialog2.pack();
                    dialog2.setSize((int) (dialog2.getWidth() * 3), dialog2.getHeight()); //it was not wide enough
                    AwtUtil.centerWindow(dialog2);
                    dialog2.setVisible(true);

                    //reset variables' value according property list
                    TemplateDialog.resetValue(m_variableList, m_propertyList);
                } //end if
            } //end actionPerformed()
        });

        //OK Button
        jButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                m_actualDir = g_selectedDirectory;
                thisDialog.dispose();
            } //end actionPerformed()
        });

        //Cancel Button
        jButton4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                m_actualDir = null;
                thisDialog.dispose();
            } //end actionPerformed()
        });
    } //end addListeners()

    //Return null if user has cancelled, output directory otherwise
    public File getActualDirectory() {
        return m_actualDir;
    } //end getActualDirectory()

    //
    // Unit Test
    //
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        GenericForwardEngineeringPlugin forward = null;
        String defaultDirectory = "D:/java";
        GenerateDialog dialog = new GenerateDialog(frame, forward, defaultDirectory, "title");
        dialog.setVisible(true);

    } //end main()
} //end GenerateDialog
