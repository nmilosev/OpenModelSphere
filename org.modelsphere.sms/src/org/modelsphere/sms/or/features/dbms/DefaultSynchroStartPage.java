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

package org.modelsphere.sms.or.features.dbms;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.gui.wizard.WizardPage;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.reverse.jdbc.ActiveConnectionManager;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.international.LocaleMgr;

/**
 * 
 * For each kind of database object (tablespace, table, index, etc.), a user can decide if he/she
 * wants to reverse it. Only selected kinds of objects will be reverse engineered.
 * 
 */
public class DefaultSynchroStartPage extends WizardPage {
    private static final String kGetTargetDbFrom = LocaleMgr.screen.getString("GetTargetDbFrom");
    private static final String kActiveDatabase = LocaleMgr.screen.getString("ActiveDatabase_");
    private static final String kDatabase_ = LocaleMgr.screen.getString("Database_");
    private static final String kCurrentConnection = LocaleMgr.screen
            .getString("CurrentConnection");
    private static final String kConnect = LocaleMgr.screen.getString("Connect");
    private static final String kProject = LocaleMgr.screen.getString("Project");
    private static final String kSelectDatabase = LocaleMgr.screen.getString("SelectDatabase");
    private static final String kSelect = LocaleMgr.screen.getString("Select");
    private static final String kSelectDatabaseMsg = LocaleMgr.message
            .getString("SelectDatabaseMsg");
    private static final String kConnectionNotCompatible = LocaleMgr.message
            .getString("ConnectionNotCompatible");
    private static final String kDisconnect = LocaleMgr.screen.getString("Disconnect");
    private static final String kNotConnected = LocaleMgr.screen.getString("NotConnected");
    private static final String kNoDatabaseSelected = LocaleMgr.screen
            .getString("NoDatabaseSelected");

    private TitledBorder dbFromPanelBorder = new TitledBorder(" " + kGetTargetDbFrom + " "); // NOT LOCALIZABLE
    private JPanel dbFromPanel = new JPanel(new GridBagLayout());
    private JPanel connectionPanel = new JPanel(new GridBagLayout());
    private NodeSelectionPanel databasePanel;
    private JLabel activeDbLabel = new JLabel(kActiveDatabase);
    private JLabel databaseLabel = new JLabel(kDatabase_);
    private JTextField activeDbModelTxFld = new JTextField("");
    private JTextField databaseTxFld = new JTextField("");
    private JTextField connectionInfoTxFld = new JTextField("");
    private JButton connectBtn = new JButton(kConnect);
    private JRadioButton dbFromConnectRadBtn = new JRadioButton(kCurrentConnection);
    private JRadioButton dbFromModelRadBtn = new JRadioButton(kProject);
    private ButtonGroup dbFromGroup = new ButtonGroup();
    private JButton referenceButton = new JButton(kDisconnect); // Button with

    // longer text

    public DefaultSynchroStartPage() {
        super();
    }

    private void initGUI(final DBMSReverseOptions options) {
        setLayout(new GridBagLayout());
        // get all possible roots
        Db[] dbs = Db.getDbs();
        ArrayList projects = new ArrayList();
        try {
            for (int i = 0; i < dbs.length; i++) {
                dbs[i].beginTrans(Db.READ_TRANS);
                DbEnumeration dbEnum = dbs[i].getRoot().getComponents().elements(
                        DbProject.metaClass);
                while (dbEnum.hasMoreElements())
                    projects.add(dbEnum.nextElement());
                dbEnum.close();
                dbs[i].commitTrans();
            }
        } catch (DbException e) {
            ExceptionHandler.processUncatchedException(this, e);
            projects = new ArrayList();
        }
        DbObject[] roots = new DbObject[projects.size()];
        for (int i = 0; i < projects.size(); i++) {
            roots[i] = (DbObject) projects.get(i);
        }

        dbFromGroup.add(dbFromConnectRadBtn);
        dbFromGroup.add(dbFromModelRadBtn);
        dbFromConnectRadBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                update(options);
            }
        });
        dbFromModelRadBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                update(options);
            }
        });

        connectBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ActiveConnectionManager.performConnection();
                options.setConnection(ActiveConnectionManager.getActiveConnectionMessage());
                if (options.getConnection() != null)
                    dbFromConnectRadBtn.setSelected(true);
                update(options);
            }
        });

        activeDbModelTxFld.setEditable(false);
        databaseTxFld.setEditable(false);
        connectionInfoTxFld.setEditable(false);

        try {
            options.synchroSourceDatabase.getDb().beginTrans(Db.READ_TRANS);
            activeDbModelTxFld.setText(options.synchroSourceDatabase
                    .getSemanticalName(DbObject.LONG_FORM));
            options.synchroSourceDatabase.getDb().commitTrans();
        } catch (DbException e) {
            ExceptionHandler.processUncatchedException(this, e);
        }

        databasePanel = new NodeSelectionPanel(new MetaClass[] { DbORDatabase.metaClass }, roots,
                false);
        databasePanel.setTargetSystemId(options.targetSystemId);
        databasePanel.setHeaderText(null);
        databasePanel.setLookupText(kSelectDatabase);
        databasePanel.setButtonText(kSelect);
        databasePanel.setEnabled(true);
        ArrayList excludedObjs = new ArrayList();
        excludedObjs.add(options.synchroSourceDatabase);
        databasePanel.setExcludedObjects(excludedObjs);
        databasePanel.addNodeSelectionChangeListener(new NodeSelectionChangeListener() {
            public void nodeSelectionChanged(Object source, int eventId, DbObject node) {
                if (node != null)
                    dbFromModelRadBtn.setSelected(true);
                update(options);
            }
        });
        dbFromPanel.setBorder(dbFromPanelBorder);

        add(activeDbLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(12, 12, 9,
                        12), 0, 0));
        add(activeDbModelTxFld, new GridBagConstraints(1, 0, 1, 1, 0.5, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(12, 0, 9, 12), 200, 0));
        add(databaseLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 12, 6,
                        12), 0, 0));
        add(databaseTxFld, new GridBagConstraints(1, 1, 1, 1, 0.5, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, new Insets(0, 0, 6, 12), 200, 0));

        add(dbFromPanel, new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST,
                GridBagConstraints.BOTH, new Insets(6, 12, 12, 12), 0, 0));
        dbFromPanel.add(dbFromConnectRadBtn, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(6, 12, 6, 6), 0,
                0));
        dbFromPanel.add(connectionPanel, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(0, 18, 6, 6), 0,
                0));
        connectionPanel.add(connectionInfoTxFld, new GridBagConstraints(0, 0, 1, 1, 0.5, 0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(3, 12, 3, 3), 0,
                5));
        connectionPanel.add(connectBtn,
                new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
        dbFromPanel.add(dbFromModelRadBtn, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(3, 12, 6, 6), 0,
                0));
        dbFromPanel.add(databasePanel, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(0, 18, 6, 6), 0,
                0));
        dbFromPanel.add(Box.createVerticalGlue(),
                new GridBagConstraints(0, 4, 2, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST,
                        GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        setNextEnabled(true);
    }

    public final boolean initialize(Object opt) {
        DBMSReverseOptions options = (DBMSReverseOptions) opt;

        // invalidate actif toolkit
        // ReverseToolkit.setConnection(null);
        // ReverseToolkit.setActiveDiagramTarget(-1);

        initGUI(options);

        if (options.getConnection() == null)
            dbFromModelRadBtn.setSelected(true);
        else {
            dbFromConnectRadBtn.setSelected(true);
        }

        if (options.synchroTargetDatabase != null)
            databasePanel.setSelectedNode(options.synchroTargetDatabase);

        options.setConnection(ActiveConnectionManager.getActiveConnectionMessage());
        if (options.getConnection() != null && options.synchroTargetDatabase == null)
            dbFromConnectRadBtn.setSelected(true);
        // init GUI from options
        update(options);
        return true;
    }

    public final boolean terminate(Object opt, boolean saveData) {
        DBMSReverseOptions options = (DBMSReverseOptions) opt;
        if (!saveData)
            return true;

        if (dbFromModelRadBtn.isSelected()) {
            if (databasePanel.getSelectedNode() == null) {
                JOptionPane.showMessageDialog(this, kSelectDatabaseMsg,
                        ApplicationContext.getApplicationName(), JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            options.setConnection(null);
        }

        if (options.getConnection() != null) {
            ReverseToolkitPlugin kitcurrent = ReverseToolkitPlugin.getToolkitForConnection(options
                    .getConnection());
            ReverseToolkitPlugin kitmodel = ReverseToolkitPlugin
                    .getToolkitForTargetSystem(options.targetSystemId);
            if (kitcurrent != kitmodel) {
                JOptionPane.showMessageDialog(this, kConnectionNotCompatible,
                        ApplicationContext.getApplicationName(), JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            options.targetSystemId = kitcurrent
                    .getTargetSystemId(options.getConnection().databaseProductVersion);
        }

        ReverseToolkitPlugin kitold = ReverseToolkitPlugin.getToolkit();
        ReverseToolkitPlugin.setConnection(options.getConnection());
        ReverseToolkitPlugin.setActiveDiagramTarget(options.targetSystemId);
        ReverseToolkitPlugin kitnew = ReverseToolkitPlugin.getToolkit();
        if (kitold != kitnew) { // Avoid dropping scope if user came back on
            // this page
            Object specificOptions = options.getSpecificDBMSOptions();
            if (specificOptions == null)
                options.setSpecificDBMSOptions(kitnew.createSpecificOptions());
            else {
                Object temp = kitnew.createSpecificOptions();
                if ((temp == null)
                        || (temp != null && !temp.getClass().isInstance(specificOptions)))
                    options.setSpecificDBMSOptions(temp);
            }
            if (options.getObjectsScope() == null)
                options.setObjectsScope(kitnew.createObjectsScope());
        }

        options.synchroTargetDatabase = dbFromModelRadBtn.isSelected() ? databasePanel
                .getSelectedNode() : null;
        return true;
    }

    private void update(DBMSReverseOptions options) {
        String dbtext = "";
        boolean nextEnable = false;
        if (options.getConnection() != null) {
            connectBtn.setText(kDisconnect);
        } else {
            connectBtn.setText(kConnect);
        }
        // AwtUtil.normalizeButtonDimension(new JButton[]{connectBtn,
        // databasePanel.getSelectionButton()});
        // connectBtn.setMaximumSize(connectBtn.getPreferredSize());
        // databasePanel.getSelectionButton().setMaximumSize(databasePanel.getSelectionButton().getPreferredSize());
        // databasePanel.doLayout();

        String dbinfo = "";
        dbinfo += DBMSUtil.getConnectionAsString(options.getConnection()); // NOT
        // LOCALIZABLE
        connectionInfoTxFld.setText(dbinfo);

        if (dbFromConnectRadBtn.isSelected()) {
            if (options.getConnection() != null) {
                dbtext += DBMSUtil.getConnectionAsString(options.getConnection());
                nextEnable = true;
            } else {
                dbtext = kNotConnected;
            }
        } else {
            if (databasePanel.getSelectedNode() == null) {
                dbtext = kNoDatabaseSelected;
            } else {
                nextEnable = true;
                try {
                    DbObject selobj = databasePanel.getSelectedNode();
                    selobj.getDb().beginTrans(Db.READ_TRANS);
                    dbtext = selobj.getSemanticalName(DbObject.LONG_FORM);
                    selobj.getDb().commitTrans();
                } catch (DbException e) {
                    ExceptionHandler.processUncatchedException(this, e);
                }
            }
        }
        databaseTxFld.setText(dbtext);
        setNextEnabled(nextEnable);
        doLayout();
    }

}
