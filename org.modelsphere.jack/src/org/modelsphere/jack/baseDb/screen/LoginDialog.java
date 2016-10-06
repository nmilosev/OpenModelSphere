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

package org.modelsphere.jack.baseDb.screen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.HotKeysSupport;
import org.modelsphere.jack.international.LocaleMgr;

public class LoginDialog extends JDialog implements ActionListener {

    private JTextField userNameTextField = new JTextField();
    private JPasswordField passwordTextField = new JPasswordField();

    private JLabel userNameLabel = new JLabel(LocaleMgr.screen.getString("UserName_"));
    private JLabel passwordLabel = new JLabel(LocaleMgr.screen.getString("Password_"));

    private JButton okButton = new JButton(LocaleMgr.screen.getString("OK"));
    private JButton cancelButton = new JButton(LocaleMgr.screen.getString("Cancel"));

    private boolean cancelled = false;
    private boolean ok = false;

    public LoginDialog(JFrame owner, String title, String name) {
        super(owner, title, true);
        jbInit();
        userNameTextField.setText(name);
        pack();
        AwtUtil.centerWindow(this);
    }

    public LoginDialog(JDialog owner, String title, String name) {
        super(owner, title, true);
        jbInit();
        userNameTextField.setText(name);
        pack();
        AwtUtil.centerWindow(this);
    }

    private void jbInit() {
        JPanel containerPanel = new JPanel(new GridBagLayout());
        getContentPane().add(containerPanel);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getRootPane().setDefaultButton(okButton);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 0, 5, 0));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        containerPanel.add(userNameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 12, 6, 0), 0, 0));
        containerPanel.add(passwordLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 12, 0), 0, 0));
        containerPanel.add(userNameTextField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 12, 6, 12),
                0, 0));
        containerPanel.add(passwordTextField, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 12, 12, 12),
                0, 0));
        containerPanel.add(Box.createVerticalGlue(), new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 12, 12, 12),
                250, 60));
        containerPanel.add(buttonPanel, new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(10, 12, 12, 12),
                0, 0));

        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        getRootPane().setDefaultButton(okButton);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        new HotKeysSupport(this, cancelButton, null);
    }

    public final void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            ok = true;
            dispose();
        } else if (e.getSource() == cancelButton) {
            cancelled = true;
            dispose();
        }
    }

    public final boolean cancelled() {
        return cancelled;
    }

    public void dispose() {
        super.dispose();
        if (!ok)
            cancelled = true;
    }

    public final String getUserName() {
        return userNameTextField.getText();
    }

    public final String getPassword() {
        return new String(passwordTextField.getPassword());
    }
}
