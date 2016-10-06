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

import java.awt.Container;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.DriverManager;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.HotKeysSupport;
import org.modelsphere.jack.debug.TestableWindow;
import org.modelsphere.jack.preference.DriverInfo;
import org.modelsphere.jack.preference.DriversManager;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.srtool.services.ConnectionMessage;

/**
 * 
 * User establishes a connection to an active database with a chosen driver
 * 
 */
//public for testing
public class ConnectDialog extends JDialog implements ActionListener, TestableWindow {
    private static final String PROPERTY_USER_NAME = "userName"; // NOT LOCALIZABLE
    private static final String PROPERTY_DATASOURCE = "dataSource"; // NOT LOCALIZABLE
    private static final String PASSWORD_FIELD = LocaleMgr.message.getString("passwordField");
    private static final String ConnectionInfo = LocaleMgr.message.getString("connectionInfo");
    private static final String SQLPrompt = LocaleMgr.message.getString("sqlPrompt");
    private static final String EXIT = LocaleMgr.message.getString("exit");
    private static final String kConnectTitle = LocaleMgr.screen.getString("ConnectTitle");
    private static final String kConnectedTo = LocaleMgr.screen.getString("ConnectedTo");
    private static final String kDriverNotProperlyInstalled = LocaleMgr.message
            .getString("DriverNotProperlyInstalled");

    private JPanel containerPanel = new JPanel();
    private JLabel userNameLabel = new JLabel(LocaleMgr.screen.getString("UserName_"));
    private JLabel passwordLabel = new JLabel(LocaleMgr.screen.getString("Password_"));
    private JLabel serverLabel = new JLabel(LocaleMgr.screen.getString("DataSourceName"));
    private JTextField userNameField = new JTextField();
    private JTextField serverField = new JTextField();
    private JButton cancelButton = new JButton(LocaleMgr.screen.getString("Cancel"));
    private JButton connectButton = new JButton(LocaleMgr.screen.getString("Connect"));
    private JPasswordField passwordField = new JPasswordField(PASSWORD_FIELD);
    private JPanel buttonPanel = new JPanel();
    private GridLayout gridLayout1 = new GridLayout();

    private String driver;
    private ConnectionMessage connectionMsg;
    private boolean useDataSource = true;

    private boolean cancel = true;

    public ConnectDialog(String aDriver) {
        super(ApplicationContext.getDefaultMainFrame(), kConnectTitle, true);
        driver = aDriver;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // DO NOT UNCOMMENT THE NEXT LINE:  THE VM (1.3.1RC2) CRASH WITH JDBC_ODBC (Using informix)
        // DriverManager.setLoginTimeout(30);      // doesn't work for all drivers

        java.sql.Driver jdbcdriver = null;
        DriverInfo driverinfo = DriversManager.getDriver(driver);

        try {
            Class c = Class.forName(driverinfo.getPropertyString(DriverInfo.class,
                    DriverInfo.DRIVER_JDBC, DriverInfo.DEFAULT_JDBC_VALUE));
            // check if jdbc driver is already loaded
            Enumeration enumeration = DriverManager.getDrivers();
            while (enumeration.hasMoreElements()) {
                Object loadedDriver = enumeration.nextElement();
                if (c.isInstance(loadedDriver)) {
                    jdbcdriver = (java.sql.Driver) loadedDriver;
                    break;
                }
            }
            if (jdbcdriver == null) {
                jdbcdriver = (java.sql.Driver) c.newInstance();
                if (jdbcdriver != null) {
                    DriverManager.registerDriver(jdbcdriver);
                }
            }

            serverField.setEditable(false);
            String osname = (String) System.getProperty("os.name"); // NOT LOCALIZABLE
            if (osname == null)
                osname = ""; // NOT LOCALIZABLE
            if (osname.toLowerCase().indexOf("mac") == -1) {
                if (!(jdbcdriver instanceof sun.jdbc.odbc.JdbcOdbcDriver)) {
                    serverLabel.setVisible(false);
                    serverField.setVisible(false);
                    useDataSource = false;
                }
            }

            try {
                jbInit();
                String userName = driverinfo.getPropertyString(DriverInfo.class,
                        DriverInfo.DRIVER_DEFAULT_USER, "");
                String password = driverinfo.getPropertyString(DriverInfo.class,
                        DriverInfo.DRIVER_DEFAULT_PASSWORD, "");
                String datasourceName = driverinfo.getPropertyString(DriverInfo.class,
                        DriverInfo.DRIVER_DATA_SOURCE, "");
                //String datasourceName = PropertiesManager.APPLICATION_PROPERTIES_SET.getPropertyString(ConnectFrame.class, PROPERTY_DATASOURCE, "");

                userNameField.setText(userName);
                passwordField.setText(password);
                if (useDataSource)
                    serverField.setText(datasourceName);

                if (userName.length() > 0) {
                    passwordField.grabFocus();
                }
                pack();

                AwtUtil.centerWindow(this);
            } catch (Exception e) {
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(),
                    kDriverNotProperlyInstalled + "\n" //NOT LOCALIZABLE, escape code
                            + e.getMessage(), ApplicationContext.getApplicationName(),
                    JOptionPane.ERROR_MESSAGE);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    dispose();
                }
            });
        } catch (Error error) {
            JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(),
                    kDriverNotProperlyInstalled + "\n" //NOT LOCALIZABLE, escape code
                            + error.getMessage(), ApplicationContext.getApplicationName(),
                    JOptionPane.ERROR_MESSAGE);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    dispose();
                }
            });
        }

    }

    public void dispose() {
        if (cancel) {
            connectionMsg = null;
        }
        super.dispose();
    }

    private void jbInit() {
        containerPanel.setLayout(new GridBagLayout());
        gridLayout1.setHgap(5);
        buttonPanel.setLayout(gridLayout1);
        getContentPane().add(containerPanel);

        //Main Panel
        containerPanel.add(userNameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(12, 12, 6, 12),
                0, 0));
        containerPanel.add(userNameField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 12,
                        12), 120, 0));

        containerPanel.add(passwordLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 12, 6, 12), 0,
                0));
        containerPanel.add(passwordField, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 12,
                        12), 120, 0));

        containerPanel.add(serverLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 12, 12, 12),
                0, 0));
        containerPanel.add(serverField, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 12,
                        12), 120, 0));

        // Control Button Panel
        containerPanel.add(buttonPanel, new GridBagConstraints(0, 3, GridBagConstraints.REMAINDER,
                1, 0.0, 1.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(5,
                        12, 12, 12), 0, 0));
        buttonPanel.add(connectButton, null);
        buttonPanel.add(cancelButton, null);

        cancelButton.addActionListener(this);
        connectButton.addActionListener(this);
        connectButton.setDefaultCapable(true);

        new HotKeysSupport(this, cancelButton, null);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == connectButton) {
            String user = userNameField.getText();
            String pw = String.valueOf(passwordField.getPassword());
            String server = serverField.getText();
            connectionMsg = doConnect(driver, user, pw, server, ConnectDialog.this);
            if (connectionMsg == null)
                return;
            if (connectionMsg.errorMessage == null) {
                cancel = false;
                ConnectDialog.this.dispose();
            }
        } else if (source == cancelButton) {
            connectionMsg = null;
            ConnectDialog.this.dispose();
        }
    }

    public static ConnectionMessage doConnect(String driver, String user, String pw, String server,
            JDialog dialog) {
        ConnectionMessage connectionMessage = null;
        String errMsg = null;
        dialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        String jdbcDriverName = DriversManager.getDriver(driver).getPropertyString(
                DriverInfo.class, DriverInfo.DRIVER_JDBC, DriverInfo.DEFAULT_JDBC_VALUE);

        try {
            String addressIP = org.modelsphere.jack.srtool.services.ServiceProtocolList
                    .getServerIP();
            int port = org.modelsphere.jack.srtool.services.ServiceProtocolList.CONNECTION_SERVICE;
            java.net.Socket s = new java.net.Socket(addressIP, port);
            ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
            connectionMessage = new ConnectionMessage(driver, user, pw, server, driver);
            output.writeObject(connectionMessage);
            ObjectInputStream input = new ObjectInputStream(s.getInputStream());
            connectionMessage = (ConnectionMessage) input.readObject();
            errMsg = connectionMessage.errorMessage;

        } catch (IOException ex) {
            errMsg = ex.toString();
        } catch (ClassNotFoundException ex) {
            errMsg = ex.toString();
        }

        dialog.setCursor(Cursor.getDefaultCursor());

        if (errMsg != null) {
            javax.swing.JOptionPane.showMessageDialog(dialog, errMsg);
            if (connectionMessage != null) {
                connectionMessage.errorMessage = errMsg;
            }
        } else {
            ConnectionInfoDialog diag = new ConnectionInfoDialog(dialog, connectionMessage);
            AwtUtil.centerWindow(diag);
            diag.setVisible(true);

            if (connectionMessage != null) {
                connectionMessage.errorMessage = null;
            }
        }

        return connectionMessage;
    }

    //public Connection getConnection() {
    public ConnectionMessage getConnectionMessage() {
        return connectionMsg;
    }

    //Unit testing
    public Window createTestWindow(Container owner) {
        ConnectDialog dialog = new ConnectDialog("test");
        return dialog;
    }
}
