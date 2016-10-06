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

package org.modelsphere.jack.srtool.reverse.jdbc;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.srtool.services.ConnectionMessage;

/**
 * 
 * User selects a driver among a list of drivers.
 * 
 */
public final class ConnectionInfoDialog extends JDialog implements ActionListener {
    private static final String kTitle = LocaleMgr.screen.getString("ConnectionInfo");
    private static final String kOK = LocaleMgr.screen.getString("OK");
    private static final String kDatabase = LocaleMgr.screen.getString("Database");
    private static final String kVersion = LocaleMgr.screen.getString("Version");
    private static final String kDataSourseName = LocaleMgr.screen.getString("DataSourceName");
    private static final String kUser = LocaleMgr.screen.getString("UserName_");
    public static final String kNotConnected = LocaleMgr.screen.getString("NotConnected");
    private static final String kDriverName = LocaleMgr.screen.getString("DriverName");
    private static final String kDriverVersion = LocaleMgr.screen.getString("DriverVersion");
    private static final String kDriverJDBCCompliant = LocaleMgr.screen
            .getString("DriverJDBCCompliant");

    private JButton okButton = new JButton(kOK);

    private JEditorPane info = new JEditorPane("text/html", ""); // NOT LOCALIZABLE - content type
    private JScrollPane scrollPane = new JScrollPane(info,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    public ConnectionInfoDialog(Frame owner, ConnectionMessage cm) {
        super(owner, kTitle, true);
        initInfo(cm);
        initGUI();
        pack();
    }

    public ConnectionInfoDialog(Dialog owner, ConnectionMessage cm) {
        super(owner, kTitle, true);
        initInfo(cm);
        initGUI();
        pack();
    }

    private void initInfo(ConnectionMessage cm) {
        String sInfo = toHtml(cm, "FFFFFF", false); // NOT LOCALIZABLE, color code
        info.setText(sInfo);
    }

    // backgroundColor in html formal ('FFFFFF')
    public static String toHtml(ConnectionMessage cm, String backgroundColor, boolean includeHtmlTag) {
        String sInfo = "";
        if (includeHtmlTag)
            sInfo += "<html>"; // NOT LOCALIZABLE - HTML
        sInfo += "<body"; // NOT LOCALIZABLE - HTML
        if (backgroundColor != null)
            sInfo += "bgcolor=\"#" + backgroundColor + "\">"; // NOT LOCALIZABLE
        // - HTML
        else
            sInfo += ">";
        sInfo += "<b>" + kDatabase + "</b><br>"
                + (cm == null ? kNotConnected : toHTML(cm.databaseProductName)) + "<br>"; // NOT LOCALIZABLE - HTML
        sInfo += "<b>" + kVersion + "</b><br>"
                + (cm == null ? kNotConnected : toHTML(cm.databaseProductVersion)) + "<br>"; // NOT LOCALIZABLE - HTML
        if (cm != null && cm.server != null && cm.server.length() > 0)
            sInfo += "<b>" + kDataSourseName + "</b><br>"
                    + (cm == null ? kNotConnected : cm.server) + "<br>"; // NOT LOCALIZABLE - HTML
        sInfo += "<b>" + kUser + "</b><br>" + (cm == null ? kNotConnected : cm.userName) + "<br>"; // NOT LOCALIZABLE - HTML
        sInfo += "<b>" + kDriverName + "</b><br>"
                + (cm == null ? kNotConnected : cm.jdbcDriverName) + "<br>"; // NOT LOCALIZABLE - HTML
        sInfo += "<b>"
                + kDriverVersion
                + "</b><br>"
                + (cm == null ? kNotConnected : cm.jdbcDriverMajorVersion + "."
                        + cm.jdbcDriverMinorVersion) + "<br>"; // NOT LOCALIZABLE - HTML
        sInfo += "<b>" + kDriverJDBCCompliant + "</b><br>"
                + (cm == null ? kNotConnected : new Boolean(cm.jdbcDriverJDBCCompliant).toString()); // NOT LOCALIZABLE - HTML
        sInfo += "</body>"; // NOT LOCALIZABLE - HTML
        if (includeHtmlTag)
            sInfo += "</html>"; // NOT LOCALIZABLE - HTML
        return sInfo;
    }

    private static String toHTML(String s) {
        if (s == null)
            return null;
        String result = "";
        StringTokenizer st = new StringTokenizer(s, "\n", true); // NOT LOCALIZABLE
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.equals("\n"))
                result += "<br>"; // NOT LOCALIZABLE
            else
                result += token;
        }
        return result;
    }

    private void initGUI() {
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());

        info.setEditable(false);
        info.setEnabled(false);

        scrollPane.setPreferredSize(new Dimension(400, 300));
        JPanel top = new JPanel(new GridBagLayout());

        top.add(scrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, new Insets(12, 12, 12, 12), 0, 0));

        JPanel bottom = new JPanel(new GridBagLayout());
        bottom.add(okButton,
                new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                        GridBagConstraints.NONE, new Insets(6, 12, 12, 12), 0, 0));

        contentPane.add(top, BorderLayout.CENTER);
        contentPane.add(bottom, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(okButton);

        okButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton)
            dispose();
    }

}
