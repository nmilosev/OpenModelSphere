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

package org.modelsphere.jack.awt.choosers;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;

public class ExternalDocumentDialog extends JDialog implements ActionListener {
    private static final String EXTERNAL_REFERENCES = LocaleMgr.screen
            .getString("ExternalReference");
    private static final String BROWSE = LocaleMgr.screen.getString("Browse");
    private static final String CANCEL = LocaleMgr.screen.getString("Cancel");
    private static final String CLOSE = LocaleMgr.screen.getString("Close");
    private static final String APPLY = LocaleMgr.screen.getString("Apply");
    private static final String SELECT = LocaleMgr.screen.getString("Select");
    private static final String OPEN_DOCUMENT = LocaleMgr.screen.getString("OpenDocument");
    private static final String CMD_PATTRN = "cmd /c \"{0}\""; // NOT
    // LOCALIZABLE

    JPanel panel1 = new JPanel();
    private JLabel jLabel1 = new JLabel();
    private JTextField jTextField1 = new JTextField();
    private JButton jButton1 = new JButton();
    private JButton jButton2 = new JButton();
    private JButton jButton3 = new JButton();
    private JButton jButton4 = new JButton();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private BorderLayout borderLayout1 = new BorderLayout();
    private String m_command = null;

    public ExternalDocumentDialog(Frame frame, String title, boolean modal, String externalDocument) {
        super(frame, title, modal);
        try {
            m_command = externalDocument;
            jbInit(externalDocument);
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ExternalDocumentDialog() {
        this(null, "", false, null);
    }

    public String getCommand() {
        return m_command;
    }

    public void actionPerformed(ActionEvent ev) {
        Object src = ev.getSource();
        if (src.equals(jButton2)) {
            this.dispose();
        } else if (src.equals(jButton1)) {
            File workingDir = null;
            JFileChooser chooser = new JFileChooser(workingDir);
            int result = chooser.showDialog(this, SELECT);
            File file = chooser.getSelectedFile();
            if ((result != JFileChooser.CANCEL_OPTION) && file != null) {
                String filename = file.getAbsolutePath();
                m_command = MessageFormat.format(CMD_PATTRN, new Object[] { filename });
                jTextField1.setText(m_command);
                jButton2.setText(CANCEL);
                jButton3.setEnabled(true);
                jButton4.setEnabled(true);
            } // end if
        } else if (src.equals(jButton3)) {
            jButton3.setEnabled(false);
            jButton2.setText(CLOSE);
        } else if (src.equals(jButton4)) {
            invoke(m_command);
        } // end if
    } // end actionPerformed()

    // Called by org.modelsphere.sms.graphic.NoticeBox
    public static void invoke(String command) {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process proc = runtime.exec(command);
        } catch (IOException ex) {
            // ?
        } // end try
    } // end invoke()

    //
    // private methods
    //

    private void jbInit(String externalDocument) throws Exception {
        panel1.setLayout(gridBagLayout1);
        this.getContentPane().setLayout(borderLayout1);
        jLabel1.setText(EXTERNAL_REFERENCES);
        jTextField1.setText(externalDocument);
        jButton1.setText(BROWSE + "...");
        jButton2.setText(CLOSE);
        jButton3.setText(APPLY);
        jButton4.setText(OPEN_DOCUMENT);
        jButton3.setEnabled(false);
        jButton4.setEnabled(m_command != null);

        getContentPane().add(panel1, BorderLayout.CENTER);
        panel1.add(jLabel1, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(12, 12, 6, 12),
                0, 0));
        panel1.add(jTextField1, new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(6, 12, 12,
                        36), 0, 6));
        panel1.add(jButton1, new GridBagConstraints(2, 1, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(6, 6, 12, 12), 0,
                0));
        panel1.add(jButton2, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(12, 3, 12, 12),
                0, 0));
        panel1.add(jButton3, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(12, 12, 12, 3),
                5, 0));
        panel1.add(jButton4, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(36, 12, 12, 72),
                0, 0));

        // add action listeners
        jButton1.addActionListener(this);
        jButton2.addActionListener(this);
        jButton3.addActionListener(this);
        jButton4.addActionListener(this);
    } // end jbInit()
} // end ExternalDocumentDialog
