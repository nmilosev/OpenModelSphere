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
import javax.swing.border.TitledBorder;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.debug.TestableWindow;
import org.modelsphere.jack.io.PathFile;
import org.modelsphere.jack.preference.*;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.srtool.services.ConnectionMessage;
import org.modelsphere.jack.text.MessageFormat;

/**
 * 
 * User selects a driver among a list of drivers.
 * 
 */
public final class DriverDialog extends JDialog implements ActionListener, ListSelectionListener,
        TestableWindow {
    private static final String kConfirmRemove = LocaleMgr.message.getString("ConfirmRemove");
    private static final String kApplyChange = LocaleMgr.message.getString("ApplyChanges");
    private static final String kDriverSelection = LocaleMgr.screen.getString("DriverSelection");
    private static final String kNewDriver = LocaleMgr.screen.getString("newDriver");
    private static final String kName = LocaleMgr.screen.getString("name");
    private static final String kJdbcDriver = LocaleMgr.screen.getString("jdbcDriver");
    private static final String kUrl = LocaleMgr.screen.getString("url");
    private static final String kDescription = LocaleMgr.screen.getString("description");
    private static final String kUser = LocaleMgr.screen.getString("DefaultUser");
    private static final String kPassword = LocaleMgr.screen.getString("DefaultPassword");
    private static final String kDriverProperties = LocaleMgr.screen.getString("driverProperties0");
    private static final String kInvalidName = LocaleMgr.message.getString("invalidOrDuplName0");
    private static final String kNoDriverSpecified = LocaleMgr.message
            .getString("NoDriverSpecified");
    private static final String kDriverNotFound = LocaleMgr.message.getString("NoDriversFound");
    private static final String kDriverFound = LocaleMgr.message.getString("DriverFound");
    private static final String kDataSource = LocaleMgr.screen.getString("DataSourceName");
    private static final String kTest = LocaleMgr.screen.getString("test");
    private static final String kDuplicate = LocaleMgr.screen.getString("Duplicate");
    private static final String kDriverNotProperlyInstalled = LocaleMgr.message
            .getString("DriverNotProperlyInstalled");
    private static final String JDBC_ODBC_CLASSNAME = "sun.jdbc.odbc.JdbcOdbcDriver"; //NOT LOCALIZABLE, class name
    private static final String JDBC_ODBC = "jdbc:odbc:"; //NOT LOCALIZABLE, class name
    private static final String IS_ODBC = "ODBC";

    private JButton newButton = new JButton(LocaleMgr.screen.getString("new"));
    private JButton copyButton = new JButton(kDuplicate);
    private JButton applyButton = new JButton(LocaleMgr.screen.getString("apply"));
    private JButton removeButton = new JButton(LocaleMgr.screen.getString("remove"));
    private JButton selectButton = new JButton(LocaleMgr.screen.getString("Select"));
    private JButton closeButton = new JButton(LocaleMgr.screen.getString("close"));

    private JList driversList = new JList();

    private DriverPanel driverPanel = new DriverPanel();

    private DriverData selectedDriver = null;
    private ArrayList drivers = null;

    private boolean changed = false;

    private boolean manualSelection = false;

    private ConnectDialog connectDialog = null;

    //called by the unit test
    private DriverDialog(Frame owner) {
        super(owner, kDriverSelection, true);
        initGUI();
    }

    //called by the actual application
    public DriverDialog() throws Exception {
        this(ApplicationContext.getDefaultMainFrame());
        loadDriverList();
        refreshDriverList();
        refreshSelectedDriver();
        setSize(new Dimension(700, 400)); // AVOID pack() - Some big components are not visible yet.
        AwtUtil.centerWindow(this);
        validate();
    } //end DriverDialog()

    private void initGUI() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel contentPanel = (JPanel) this.getContentPane();

        contentPanel.setLayout(new GridBagLayout());

        JPanel bottomPanel = new JPanel(new GridBagLayout());
        JPanel topPanel = new JPanel(new GridBagLayout());

        bottomPanel.add(newButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 12, 12, 0), 0, 0));
        bottomPanel.add(copyButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 6, 12, 0), 0, 0));
        bottomPanel.add(removeButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 6, 12, 0), 0, 0));
        bottomPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(12, 6, 12, 0), 0, 0));
        bottomPanel.add(selectButton, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 6, 12, 0), 0, 0));
        bottomPanel.add(applyButton, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 6, 12, 0), 0, 0));
        bottomPanel.add(closeButton, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 6, 12, 12), 0, 0));

        topPanel.add(new JScrollPane(driversList) {
            public Dimension getPreferredSize() {
                return new Dimension(150, 50);
            }

            public Dimension getMinimumSize() {
                return new Dimension(150, 50);
            }
        }, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, new Insets(12, 12, 12, 0), 0, 0));
        topPanel
                .add(driverPanel, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.WEST, GridBagConstraints.BOTH,
                        new Insets(12, 12, 12, 12), 0, 0));

        contentPanel.add(topPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        contentPanel.add(bottomPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        AwtUtil.normalizeComponentDimension(new JButton[] { newButton, copyButton, removeButton,
                selectButton, closeButton, applyButton });

        applyButton.setEnabled(false);

        newButton.addActionListener(this);
        copyButton.addActionListener(this);
        removeButton.addActionListener(this);
        selectButton.addActionListener(this);
        closeButton.addActionListener(this);
        applyButton.addActionListener(this);

        driversList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        driversList.addListSelectionListener(this);
    } //end initGUI()

    private void loadDriverList() {
        drivers = new ArrayList();
        String[] driverkeys = DriversManager.getDrivers();
        for (int i = 0; i < driverkeys.length; i++) {
            DriverInfo driver = DriversManager.getDriver(driverkeys[i]);
            DriverData data = new DriverData();
            data.oldname = data.newname = driver.getPropertyString(DriverInfo.class,
                    DriverInfo.DRIVER_NAME, "");
            data.oldjdbc = data.newjdbc = driver.getPropertyString(DriverInfo.class,
                    DriverInfo.DRIVER_JDBC, DriverInfo.DEFAULT_JDBC_VALUE);
            data.oldurl = data.newurl = driver.getPropertyString(DriverInfo.class,
                    DriverInfo.DRIVER_URL, "");
            data.olddescription = data.newdescription = driver.getPropertyString(DriverInfo.class,
                    DriverInfo.DRIVER_DESCRIPTION, "");
            data.olduser = data.newuser = driver.getPropertyString(DriverInfo.class,
                    DriverInfo.DRIVER_DEFAULT_USER, "");
            data.oldpassword = data.newpassword = driver.getPropertyString(DriverInfo.class,
                    DriverInfo.DRIVER_DEFAULT_PASSWORD, "");
            data.olddatasource = data.newdatasource = driver.getPropertyString(DriverInfo.class,
                    DriverInfo.DRIVER_DATA_SOURCE, "");
            drivers.add(data);
        }

    }

    private void refreshDriverList() {
        manualSelection = true;
        Object[] items = drivers.toArray();
        Arrays.sort(items);
        driversList.setListData(items);
        driversList.setSelectedValue(selectedDriver, true);
        driverPanel.setSelectedDriver(selectedDriver);
        manualSelection = false;
    }

    private void refreshSelectedDriver() {
        driverPanel.setSelectedDriver(selectedDriver);
        if (selectedDriver == null) {
            removeButton.setEnabled(false);
            copyButton.setEnabled(false);
            selectButton.setEnabled(false);
        } else {
            removeButton.setEnabled(true);
            copyButton.setEnabled(true);
            selectButton.setEnabled(true);
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == closeButton) {
            connectDialog = null;
            selectedDriver = null;
            dispose();
        } else if (source == selectButton) {
            connectDialog = null;
            if (selectedDriver != null)
                connect();
        } else if (source == newButton) {
            addDriver();
        } else if (source == applyButton) {
            apply();
        } else if (source == removeButton) {
            removeDriver();
        } else if (source == copyButton) {
            copyDriver();
        } //end if
    } //end actionPerformed()

    private boolean checkName() {
        if (selectedDriver != null && !validateDriverName(selectedDriver)) {
            manualSelection = true;
            driversList.setSelectedValue(selectedDriver, true);
            manualSelection = false;
            JOptionPane
                    .showMessageDialog(DriverDialog.this, MessageFormat.format(kInvalidName,
                            new Object[] { selectedDriver.newname }), getTitle(),
                            JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void valueChanged(ListSelectionEvent e) {
        if (!manualSelection) {
            if (!checkName())
                return;
            selectedDriver = (DriverData) driversList.getSelectedValue();
        }
        refreshSelectedDriver();
    }

    // This is a workaround for a bug with JOptionPane and dispose() on JDialog.
    // Answering no caused a call to dispose on this dialog before disposing the main frame.
    private boolean skip = false;

    private boolean confirmClose() {
        if (skip)
            return true;
        if (applyButton.isEnabled()) {
            int confirm = JOptionPane.showConfirmDialog(this, kApplyChange,
                    ApplicationContext.getApplicationName(), JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (!saveDrivers())
                    return false;
            }
            skip = true;
        }
        return true;
    }

    public void dispose() {
        if (confirmClose()) {
            super.dispose();
        }
    }

    public void connect() {
        dispose();

        connectDialog = new ConnectDialog(selectedDriver.oldname);
        connectDialog.setVisible(true);
    }

    private boolean saveDrivers() {
        if (!checkName()) {
            return false;
        }

        if (drivers == null)
            return true;
        ArrayList toRename = new ArrayList();
        ArrayList toAdd = new ArrayList();
        Iterator iter = drivers.iterator();
        while (iter.hasNext()) {
            DriverData driverData = (DriverData) iter.next();

            if (driverData.newdriver) {
                toAdd.add(driverData);
            }

            DriverInfo driver = DriversManager.getDriver(driverData.oldname);
            if (driver == null) // Should not occurs
                continue;

            // check for rename
            if (!driverData.newname.equals(driverData.oldname)) {
                toRename.add(driverData);
            }

            //if is ODBC
            if (driverData.newjdbc.equals(JDBC_ODBC_CLASSNAME)) {
                driverData.newurl = JDBC_ODBC + driverData.newdatasource;
            } //end if

            // apply other changes
            driver.setProperty(DriverInfo.class, DriverInfo.DRIVER_URL, driverData.newurl);
            driver.setProperty(DriverInfo.class, DriverInfo.DRIVER_JDBC, driverData.newjdbc);
            driver.setProperty(DriverInfo.class, DriverInfo.DRIVER_DESCRIPTION,
                    driverData.newdescription);
            driver
                    .setProperty(DriverInfo.class, DriverInfo.DRIVER_DEFAULT_USER,
                            driverData.newuser);
            driver.setProperty(DriverInfo.class, DriverInfo.DRIVER_DEFAULT_PASSWORD,
                    driverData.newpassword);
            driver.setProperty(DriverInfo.class, DriverInfo.DRIVER_DATA_SOURCE,
                    driverData.newdatasource);

            driverData.oldjdbc = driverData.newjdbc = driver.getPropertyString(DriverInfo.class,
                    DriverInfo.DRIVER_JDBC, DriverInfo.DEFAULT_JDBC_VALUE);
            driverData.oldurl = driverData.newurl = driver.getPropertyString(DriverInfo.class,
                    DriverInfo.DRIVER_URL, "");
            driverData.olddescription = driverData.newdescription = driver.getPropertyString(
                    DriverInfo.class, DriverInfo.DRIVER_DESCRIPTION, "");
            driverData.olduser = driverData.newuser = driver.getPropertyString(DriverInfo.class,
                    DriverInfo.DRIVER_DEFAULT_USER, "");
            driverData.oldpassword = driverData.newpassword = driver.getPropertyString(
                    DriverInfo.class, DriverInfo.DRIVER_DEFAULT_PASSWORD, "");
            driverData.olddatasource = driverData.newdatasource = driver.getPropertyString(
                    DriverInfo.class, DriverInfo.DRIVER_DATA_SOURCE, "");
        } //end while

        iter = toRename.iterator();
        while (iter.hasNext()) {
            DriverData driverData = (DriverData) iter.next();
            DriversManager.renameDriver(driverData.oldname, driverData.oldname
                    + PathFile.BACKUP_EXTENSION);
        }

        iter = toRename.iterator();
        while (iter.hasNext()) {
            DriverData driverData = (DriverData) iter.next();
            if (DriversManager.renameDriver(driverData.oldname + PathFile.BACKUP_EXTENSION,
                    driverData.newname))
                driverData.oldname = driverData.newname;
        }

        iter = toAdd.iterator();
        while (iter.hasNext()) {
            DriverData driverData = (DriverData) iter.next();
            DriverInfo driver = DriversManager.addDriver(driverData.newname);

            if (driver == null) {
                continue;
            }

            driver.setProperty(DriverInfo.class, DriverInfo.DRIVER_URL, driverData.newurl);
            driver.setProperty(DriverInfo.class, DriverInfo.DRIVER_JDBC, driverData.newjdbc);
            driver.setProperty(DriverInfo.class, DriverInfo.DRIVER_DESCRIPTION,
                    driverData.newdescription);
            driver
                    .setProperty(DriverInfo.class, DriverInfo.DRIVER_DEFAULT_USER,
                            driverData.newuser);
            driver.setProperty(DriverInfo.class, DriverInfo.DRIVER_DEFAULT_PASSWORD,
                    driverData.newpassword);
            driver.setProperty(DriverInfo.class, DriverInfo.DRIVER_DATA_SOURCE,
                    driverData.newdatasource);

            driverData.oldname = driverData.newname = driver.getPropertyString(DriverInfo.class,
                    DriverInfo.DRIVER_NAME, "");
            driverData.oldjdbc = driverData.newjdbc = driver.getPropertyString(DriverInfo.class,
                    DriverInfo.DRIVER_JDBC, DriverInfo.DEFAULT_JDBC_VALUE);
            driverData.oldurl = driverData.newurl = driver.getPropertyString(DriverInfo.class,
                    DriverInfo.DRIVER_URL, "");
            driverData.olddescription = driverData.newdescription = driver.getPropertyString(
                    DriverInfo.class, DriverInfo.DRIVER_DESCRIPTION, "");
            driverData.olduser = driverData.newuser = driver.getPropertyString(DriverInfo.class,
                    DriverInfo.DRIVER_DEFAULT_USER, "");
            driverData.oldpassword = driverData.newpassword = driver.getPropertyString(
                    DriverInfo.class, DriverInfo.DRIVER_DEFAULT_PASSWORD, "");
            driverData.olddatasource = driverData.newdatasource = driver.getPropertyString(
                    DriverInfo.class, DriverInfo.DRIVER_DATA_SOURCE, "");
            driverData.newdriver = false;
        } //end while
        return true;
    }

    private void addDriver() {
        if (!checkName()) {
            return;
        }
        selectedDriver = new DriverData();
        selectedDriver.newdriver = true;
        selectedDriver.newname = kNewDriver;
        selectedDriver.newjdbc = "<classname>";
        selectedDriver.newurl = "jdbc:<driver_name>:<ip_address>:<ip_port>:<datasource>";
        drivers.add(selectedDriver);
        refreshDriverList();
        applyButton.setEnabled(true);
        driverPanel.name.grabFocus();
        driverPanel.name.selectAll();
    }

    private void apply() {
        if (saveDrivers())
            applyButton.setEnabled(false);
    }

    private void copyDriver() {
        if (selectedDriver == null)
            return;
        DriverData old = selectedDriver;
        addDriver();
        selectedDriver.oldname = selectedDriver.newname = (old.newname + " - " + kDuplicate);
        selectedDriver.oldjdbc = selectedDriver.newjdbc = old.newjdbc;
        selectedDriver.oldurl = selectedDriver.newurl = old.newurl;
        selectedDriver.olddescription = selectedDriver.newdescription = old.newdescription;
        selectedDriver.olduser = selectedDriver.newuser = old.newuser;
        selectedDriver.oldpassword = selectedDriver.newpassword = old.newpassword;
        selectedDriver.olddatasource = selectedDriver.newdatasource = old.newdatasource;
        refreshDriverList();
        applyButton.setEnabled(true);
        driverPanel.name.grabFocus();
        driverPanel.name.selectAll();
    }

    private void removeDriver() {
        int confirm = JOptionPane.showConfirmDialog(this, kConfirmRemove,
                ApplicationContext.getApplicationName(), JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.NO_OPTION)
            return;

        DriverData driverData = (DriverData) driversList.getSelectedValue();
        if (driverData == null)
            return;

        DriversManager.removeDriver(driverData.oldname);
        drivers.remove(driverData);

        selectedDriver = null;
        refreshDriverList();
        refreshSelectedDriver();
    }

    public ConnectionMessage getConnectionMessage() {
        if (connectDialog == null)
            return null;
        return connectDialog.getConnectionMessage();
    }

    private boolean validateDriverName(DriverData driver) {
        if (driver.newname == null)
            return false;
        if (driver.newname.length() == 0)
            return false;
        Iterator iterator = drivers.iterator();
        while (iterator.hasNext()) {
            DriverData driverData = (DriverData) iterator.next();
            if (driver != driverData && driverData.newname.equalsIgnoreCase(driver.newname)) {
                return false;
            }
        }
        return true;
    }

    private boolean testDriver(String classname) {
        if (classname == null || classname.length() == 0) {
            JOptionPane.showMessageDialog(this, kNoDriverSpecified, getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Class drv = Class.forName(classname);
            if (drv != null) {
                JOptionPane.showMessageDialog(this, kDriverFound, getTitle(),
                        JOptionPane.PLAIN_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, kDriverNotFound, getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, kDriverNotFound, getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (NoClassDefFoundError error) {
            JOptionPane.showMessageDialog(this, kDriverNotProperlyInstalled + "\n"
                    + error.getMessage(), getTitle(), JOptionPane.ERROR_MESSAGE); //NOT LOCALIZABLE, escape code
            return false;
        }
    }

    //
    // INNER CLASSES
    //

    private static class DriverData implements Comparable {
        String oldname = "";
        String newname = "";
        String oldurl = "";
        String newurl = "";
        String oldjdbc = "";
        String newjdbc = "";
        String olddescription = "";
        String newdescription = "";
        String olduser = "";
        String newuser = "";
        String oldpassword = "";
        String newpassword = "";
        String olddatasource = "";
        String newdatasource = "";

        boolean newdriver;

        DriverData() {
            newdriver = false;
        }

        public int compareTo(Object o) {
            if (o instanceof DriverData) {
                return newname.compareTo(((DriverData) o).newname);
            }
            return newname.compareTo("");
        }

        public String toString() {
            return newname;
        }
    } //end DriverData()

    private class DriverPanel extends JPanel implements FocusListener {

        private DriverData driver = null;
        private JPanel content = new JPanel(new GridBagLayout());

        JTextField name = new JTextField();
        JTextField jdbcDriver = new JTextField();
        JTextField datasource = new JTextField();
        JButton testjdbcButton = new JButton(kTest);
        JTextField url = new JTextField();
        JTextField description = new JTextField();
        JTextField user = new JTextField();
        JTextField password = new JTextField();
        private JCheckBox odbcDriver = new JCheckBox(IS_ODBC);
        private JLabel urlLabel = new JLabel(kUrl);
        private JLabel datasourceLabel = new JLabel(kDataSource);

        //boolean manualGrab = false;

        DriverPanel() {
            super(new BorderLayout());
            init();
        }

        private void init() {
            //Line 1 : name
            content.add(new JLabel(kName), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 12, 6, 6), 0,
                    0));
            content.add(name, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL, new Insets(12, 0, 6, 12), 0, 0));

            //Line 2 : description
            content.add(new JLabel(kDescription),
                    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                            GridBagConstraints.NONE, new Insets(0, 12, 6, 6), 0, 0));
            content.add(description, new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 6, 12), 0, 0));

            //Line 3 : user
            content.add(new JLabel(kUser),
                    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                            GridBagConstraints.NONE, new Insets(0, 12, 6, 6), 0, 0));
            content.add(user, new GridBagConstraints(1, 2, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL, new Insets(0, 0, 6, 12), 0, 0));

            //Line 4 : password
            content.add(new JLabel(kPassword), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 12, 6), 0,
                    0));
            content.add(password, new GridBagConstraints(1, 3, 2, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 12, 12), 0, 0));

            //Line 5 : ODBC checkbox
            content
                    .add(odbcDriver, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                            GridBagConstraints.EAST, GridBagConstraints.NONE,
                            new Insets(6, 6, 6, 6), 0, 0));

            //Line 6 : class name and verify
            content.add(new JLabel(kJdbcDriver),
                    new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                            GridBagConstraints.NONE, new Insets(0, 12, 6, 6), 0, 0));
            content.add(jdbcDriver, new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 6, 6),
                    0, 0));
            content.add(testjdbcButton, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 6, 12), 0, 0));

            //Line 7 : URL
            content.add(urlLabel,
                    new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                            GridBagConstraints.NONE, new Insets(0, 12, 6, 6), 0, 0));
            content.add(url, new GridBagConstraints(1, 6, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL, new Insets(0, 0, 6, 12), 0, 0));

            //Line 8 : Datasource
            content.add(datasourceLabel,
                    new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                            GridBagConstraints.NONE, new Insets(0, 12, 6, 6), 0, 0));
            content.add(datasource, new GridBagConstraints(1, 7, 2, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 6, 12), 0, 0));

            //Line 9 : glue
            content.add(Box.createVerticalGlue(), new GridBagConstraints(1, 8, 1, 1, 1.0, 1.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
                    0, 0));

            add(content);

            name.addFocusListener(this);
            jdbcDriver.addFocusListener(this);
            url.addFocusListener(this);
            description.addFocusListener(this);
            user.addFocusListener(this);
            datasource.addFocusListener(this);
            password.addFocusListener(this);
            testjdbcButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    testDriver(jdbcDriver.getText());
                }
            });

            datasource.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    if (odbcDriver.isSelected()) {
                        String newURL = JDBC_ODBC + datasource.getText();
                        url.setText(newURL);
                        saveData();
                    }
                } //end inputMethodTextChanged()
            });

            odbcDriver.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    boolean init = false;
                    useOdbcDriver(odbcDriver.isSelected(), init);
                }
            });
        } //end init()

        public void focusGained(FocusEvent e) {
        }

        public void focusLost(FocusEvent e) {
            if (e.getComponent() == name && !name.getText().equals(driver.oldname)) {
                applyButton.setEnabled(true);
                saveData();
                refreshDriverList();
            } else if (e.getComponent() == jdbcDriver
                    && !jdbcDriver.getText().equals(driver.oldjdbc)) {
                applyButton.setEnabled(true);
                saveData();
            } else if (e.getComponent() == url && !url.getText().equals(driver.oldurl)) {
                applyButton.setEnabled(true);
                saveData();
            } else if (e.getComponent() == description
                    && !description.getText().equals(driver.olddescription)) {
                applyButton.setEnabled(true);
                saveData();
            } else if (e.getComponent() == user && !user.getText().equals(driver.olduser)) {
                applyButton.setEnabled(true);
                saveData();
            } else if (e.getComponent() == password
                    && !password.getText().equals(driver.oldpassword)) {
                applyButton.setEnabled(true);
                saveData();
            } else if (e.getComponent() == datasource
                    && !datasource.getText().equals(driver.olddatasource)) {
                applyButton.setEnabled(true);
                saveData();
            }
        } //end focusLost()

        private String m_previousText;

        private void useOdbcDriver(boolean isOdbcDriver, boolean init) {
            url.setEnabled(!isOdbcDriver);
            urlLabel.setEnabled(!isOdbcDriver);
            datasource.setEnabled(isOdbcDriver);
            datasourceLabel.setEnabled(isOdbcDriver);

            if (init) {
                saveData();
                return;
            }

            if ((m_previousText == null) || (m_previousText.equals(""))) {
                m_previousText = jdbcDriver.getText();
            }

            if (isOdbcDriver) {
                m_previousText = jdbcDriver.getText();
                jdbcDriver.setText(JDBC_ODBC_CLASSNAME);
            } else {
                jdbcDriver.setText(m_previousText);
            }

            saveData();
        } //end useOdbcDriver

        private void saveData() {
            if (driver == null)
                return;
            driver.newname = name.getText();
            driver.newjdbc = jdbcDriver.getText();
            driver.newurl = url.getText();
            driver.newdescription = description.getText();
            driver.newuser = user.getText();
            driver.newpassword = password.getText();
            driver.newdatasource = datasource.getText();
        }

        void setSelectedDriver(DriverData driver) {
            this.driver = driver;
            if (driver == null) {
                content.setVisible(false);
                name.setText("");
                jdbcDriver.setText("");
                url.setText("");
                description.setText("");
                user.setText("");
                password.setText("");
                datasource.setText("");
            } else {
                content.setVisible(true);
                content.setBorder(new TitledBorder(" "
                        + MessageFormat.format(kDriverProperties, new Object[] { driver.newname })
                        + " "));
                if (!driver.newdriver) {
                    name.setEditable(false);
                } else {
                    name.setEditable(true);
                }
                name.setText(driver.newname);
                jdbcDriver.setText(driver.newjdbc);
                url.setText(driver.newurl);
                description.setText(driver.newdescription);
                user.setText(driver.newuser);
                password.setText(driver.newpassword);
                datasource.setText(driver.newdatasource);
            } //end if

            boolean useOdbcDriver = false;
            String classname = jdbcDriver.getText();
            if ((classname != null) && (classname.equals(JDBC_ODBC_CLASSNAME))) {
                useOdbcDriver = true;
            } //end if

            odbcDriver.setSelected(useOdbcDriver);
            boolean init = true;
            useOdbcDriver(useOdbcDriver, init);
        } //end setSelectedDriver()
    } //end DriverPanel

    //
    //Unit testing
    //
    private DriverDialog(String s) {
    }

    private static DriverDialog g_singleInstance = null;

    public static DriverDialog getSingleInstance() {
        if (g_singleInstance == null) {
            g_singleInstance = new DriverDialog("");
        }

        return g_singleInstance;
    } //end getSingleInstance()

    public Window createTestWindow(Container owner) {
        Window dialog = null;

        if (owner instanceof Frame) {
            dialog = new DriverDialog((Frame) owner);
        } else if (owner instanceof JButton) {
            while (!(owner instanceof Frame)) {
                owner = owner.getParent();
            } //end while
            dialog = new DriverDialog((Frame) owner);
        }

        return dialog;
    }

    public static void main(String args[]) {
        //create the dialog
        JFrame frame = new JFrame();
        DriverDialog dialog = new DriverDialog(frame);
        dialog.setSize(new Dimension(700, 400));
        dialog.setVisible(true);
    } //end if

} //end DriverDialog
