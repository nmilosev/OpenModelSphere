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

package org.modelsphere.sms.screen;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.modelsphere.jack.awt.HotKeysSupport;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.screen.DbLookupDialog;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.sms.db.DbSMSLinkModel;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.util.AnySemObject;
import org.modelsphere.sms.international.LocaleMgr;

public class SMSLinkFrame extends JDialog {
    private static final String kUnspecified = LocaleMgr.screen.getString("Unspecified");
    private static final String kLinkModel = LocaleMgr.screen.getString("linkModel");

    private DbSMSProject project = null;
    public boolean cancel = true;
    private DbObject[] objects = null;
    public DbObject sourceObject = null;
    public DbObject targetObject = null;
    public DbSMSLinkModel linkModel = null;

    JPanel containerPanel = new JPanel();
    JLabel linkModelLabel = new JLabel();
    JPanel controlBtnPanel = new JPanel();
    JButton okButton = new JButton();
    JButton cancelButton = new JButton();
    GridLayout gridLayout1 = new GridLayout();
    JComboBox objectsComboBox = null;
    JLabel sourceLabel = new JLabel();
    JButton linkModelLookUp = new JButton();
    JLabel linkModelNameLabel = new JLabel();
    GridBagLayout gridBagLayout = new GridBagLayout();
    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == linkModelLookUp) {
                try {
                    project.getDb().beginTrans(Db.READ_TRANS); // close by
                    // DbLookupDialog
                    DefaultComparableElement item = DbLookupDialog.selectDbo(SMSLinkFrame.this,
                            kLinkModel, null, project.getDb(), AnySemObject
                                    .getAllDbSMSLinkModel(project), linkModel, null, false);
                    if (item != null) {
                        project.getDb().beginTrans(Db.READ_TRANS);
                        linkModel = (DbSMSLinkModel) item.object;
                        linkModelNameLabel.setText(linkModel.getName());
                        okButton.setEnabled(true);
                        project.getDb().commitTrans();
                    }
                } catch (DbException dbE) {
                    org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                            ApplicationContext.getDefaultMainFrame(), dbE);
                }
            } else if (e.getSource() == okButton) {
                if (objectsComboBox.getSelectedIndex() == 0) {
                    sourceObject = objects[0];
                    targetObject = objects[1];
                } else {
                    sourceObject = objects[1];
                    targetObject = objects[0];
                }
                cancel = false;
                dispose();
            } else if (e.getSource() == cancelButton) {
                dispose();
            }
        }
    };

    public SMSLinkFrame(Frame frame, String title, boolean modal, DbObject[] aObjects)
            throws DbException {
        super(frame, title, modal);
        objects = aObjects;
        project = (DbSMSProject) objects[0].getProject();
        objectsComboBox = new JComboBox(new String[] {
                objects[0].getSemanticalName(DbObject.LONG_FORM),
                objects[1].getSemanticalName(DbObject.LONG_FORM) });
        objectsComboBox.setSelectedIndex(0);
        jbInit();
        linkModel = project.getDefaultLinkModel();
        if (linkModel == null) {
            linkModelNameLabel.setText(kUnspecified);
            okButton.setEnabled(false);
        } else {
            linkModelNameLabel.setText(linkModel.getName());
        }
        this.pack();
        this.setLocationRelativeTo(frame);
    }

    public SMSLinkFrame(Frame frame, DbObject[] objects) throws DbException {
        this(frame, LocaleMgr.screen.getString("createLink"), true, objects);
    }

    void jbInit() throws DbException {
        linkModelLabel.setText(kLinkModel + " :"); // NOT LOCALIZABLE
        okButton.setText(LocaleMgr.screen.getString("Link"));
        cancelButton.setText(LocaleMgr.screen.getString("Cancel"));
        sourceLabel.setText(LocaleMgr.screen.getString("sourceObject"));
        linkModelLookUp.setText(LocaleMgr.screen.getString("ThreeDot"));

        containerPanel.setLayout(gridBagLayout);
        gridLayout1.setHgap(5);
        controlBtnPanel.setLayout(gridLayout1);

        containerPanel.setPreferredSize(new Dimension(375, 175));
        getContentPane().add(containerPanel);

        // Main Panel
        containerPanel.add(linkModelLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 12, 0, 3), 0, 0));
        containerPanel.add(sourceLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 12, 6, 3), 0, 0));

        // Control Button Panel
        containerPanel.add(controlBtnPanel, new GridBagConstraints(0, 2, 4, 1, 1.0, 1.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(12, 12, 12, 12),
                0, 0));

        controlBtnPanel.add(okButton, null);
        controlBtnPanel.add(cancelButton, null);
        containerPanel.add(objectsComboBox, new GridBagConstraints(1, 1, 3, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(6, 0, 6, 12), 0,
                0));
        containerPanel.add(linkModelLookUp, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(12, 0, 0, 12), 0, 0));
        containerPanel.add(linkModelNameLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 0, 0),
                0, 0));

        okButton.addActionListener(actionListener);
        cancelButton.addActionListener(actionListener);
        linkModelLookUp.addActionListener(actionListener);

        getRootPane().setDefaultButton(okButton);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        new HotKeysSupport(this, cancelButton, null);
    }

    // *************
    // DEMO FUNCTION
    // *************

    private static void runDemo() {
        /*
         * SMSLinkFrame dialogue = new SMSLinkFrame(); dialogue.setVisible(true); boolean done =
         * false; do { try { Thread.sleep(200); } catch (InterruptedException ex) {}
         * 
         * if (!dialogue.isShowing()) { dialogue.dispose(); dialogue = null; done = true; } } while
         * (! done); System.exit(0);
         */
    } // end runDemo()

    /*
     * //Run the demo public static void main(String[] args) { runDemo(); } //end main()
     */
} // end SMSLinkFrame
