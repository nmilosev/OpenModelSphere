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

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.sms.db.DbSMSLinkModel;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.util.AnySemObject;
import org.modelsphere.sms.international.LocaleMgr;

/**
 * @author Nicolask
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences -
 *         Java - Code Style - Code Templates
 */
public class SMSLinkFrameExt extends JDialog {

    private JPanel jPanel = null;

    private JPanel jPanel1 = null;

    private JButton jButton = null;

    private JButton jButton1 = null;

    private JComboBox jComboBox = null;

    private JComboBox jComboBox1 = null;

    private JLabel jLabel1 = null;

    private JLabel jLabel3 = null;

    private JPanel jPanel2 = null;

    private JPanel jPanel3 = null;

    private JPanel jPanel4 = null;

    private JPanel jPanel5 = null;

    private JLabel jLabel = null;

    private static DbObject lastUsedLinkModel = null;

    public boolean cancel = true;

    private List<DbObject> objects = null;

    public DbObject sourceObject = null;

    public DbObject targetObject = null;

    public DbSMSLinkModel linkModel = null;

    List<DbSMSLinkModel> list = null;

    /**
     * @throws java.awt.HeadlessException
     */
    public SMSLinkFrameExt() throws HeadlessException {
        super();
        initialize();
    }

    /**
     * @param owner
     * @throws java.awt.HeadlessException
     */
    public SMSLinkFrameExt(Frame owner) throws HeadlessException {
        super(owner);
        initialize();
    }

    /**
     * @param owner
     * @param modal
     * @throws java.awt.HeadlessException
     */
    public SMSLinkFrameExt(Frame owner, List<DbObject> objects, boolean modal)
            throws HeadlessException {
        super(owner, modal);
        this.objects = objects;
        initialize();
    }

    /**
     * @param owner
     * @param title
     * @throws java.awt.HeadlessException
     */
    public SMSLinkFrameExt(Frame owner, String title) throws HeadlessException {
        super(owner, title);
        initialize();
    }

    /**
     * @param owner
     * @param title
     * @param modal
     * @throws java.awt.HeadlessException
     */
    public SMSLinkFrameExt(Frame owner, String title, boolean modal) throws HeadlessException {
        super(owner, title, modal);
        initialize();
    }

    /**
     * @param owner
     * @param title
     * @param modal
     * @param gc
     */
    public SMSLinkFrameExt(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
        initialize();
    }

    /**
     * @param owner
     * @throws java.awt.HeadlessException
     */
    public SMSLinkFrameExt(Dialog owner) throws HeadlessException {
        super(owner);
        initialize();
    }

    /**
     * @param owner
     * @param modal
     * @throws java.awt.HeadlessException
     */
    public SMSLinkFrameExt(Dialog owner, boolean modal) throws HeadlessException {
        super(owner, modal);
        initialize();
    }

    /**
     * @param owner
     * @param title
     * @throws java.awt.HeadlessException
     */
    public SMSLinkFrameExt(Dialog owner, String title) throws HeadlessException {
        super(owner, title);
        initialize();
    }

    /**
     * @param owner
     * @param title
     * @param modal
     * @throws java.awt.HeadlessException
     */
    public SMSLinkFrameExt(Dialog owner, String title, boolean modal) throws HeadlessException {
        super(owner, title, modal);
        initialize();
    }

    /**
     * @param owner
     * @param title
     * @param modal
     * @param gc
     * @throws java.awt.HeadlessException
     */
    public SMSLinkFrameExt(Dialog owner, String title, boolean modal, GraphicsConfiguration gc)
            throws HeadlessException {
        super(owner, title, modal, gc);
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setContentPane(getJPanel3());
        this.setModal(true);
        this.setTitle(LocaleMgr.screen.getString("createLinkModel"));
        this.setSize(335, 219);

    }

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jLabel = new JLabel();
            jLabel3 = new JLabel();
            jLabel1 = new JLabel();
            GridBagConstraints gridBagConstraints49 = new GridBagConstraints();
            GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
            GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
            GridBagConstraints gridBagConstraints34 = new GridBagConstraints();
            GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
            GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            gridBagConstraints26.gridx = 2;
            gridBagConstraints26.gridy = 6;
            gridBagConstraints26.weightx = 1.0D;
            gridBagConstraints26.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints26.insets = new java.awt.Insets(0, 10, 12, 12);
            gridBagConstraints26.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints27.gridx = 2;
            gridBagConstraints27.gridy = 7;
            gridBagConstraints27.weightx = 1.0;
            gridBagConstraints27.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints27.insets = new java.awt.Insets(0, 10, 12, 12);
            gridBagConstraints27.anchor = java.awt.GridBagConstraints.NORTHWEST;
            jPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            jPanel.setPreferredSize(new java.awt.Dimension(500, 500));
            jPanel.setMinimumSize(new java.awt.Dimension(200, 120));
            jPanel.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);

            gridBagConstraints32.gridx = 0;
            gridBagConstraints32.gridy = 7;
            gridBagConstraints32.insets = new java.awt.Insets(4, 12, 0, 0);
            gridBagConstraints32.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints32.anchor = java.awt.GridBagConstraints.NORTHWEST;
            jLabel1.setText(LocaleMgr.screen.getString("sourceObject") + ": ");

            gridBagConstraints33.gridx = 0;
            gridBagConstraints33.gridy = 6;
            gridBagConstraints33.insets = new java.awt.Insets(4, 12, 0, 0);
            gridBagConstraints33.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints33.anchor = java.awt.GridBagConstraints.NORTHWEST;
            jLabel3.setText(LocaleMgr.screen.getString("linkModel") + ": ");

            gridBagConstraints34.gridx = 2;
            gridBagConstraints34.gridy = 0;
            gridBagConstraints49.gridx = 0;
            gridBagConstraints49.gridy = 1;
            gridBagConstraints49.gridwidth = 3;
            gridBagConstraints49.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints49.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints49.insets = new java.awt.Insets(12, 12, 12, 12);
            jLabel.setText(LocaleMgr.screen.getString("createLinkComment"));
            jPanel.add(getJComboBox(), gridBagConstraints26);
            jPanel.add(jLabel, gridBagConstraints49);
            jPanel.add(jLabel1, gridBagConstraints32);
            jPanel.add(jLabel3, gridBagConstraints33);
            jPanel.add(getJComboBox1(), gridBagConstraints27);
            jPanel.add(getJPanel2(), gridBagConstraints34);
        }
        return jPanel;
    }

    /**
     * This method initializes jPanel1
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            jPanel1 = new JPanel();
            jPanel1.setPreferredSize(new java.awt.Dimension(160, 33));
            jPanel1.setMinimumSize(new java.awt.Dimension(160, 33));
            jPanel1.add(getJButton(), null);
            jPanel1.add(getJButton1(), null);
        }
        return jPanel1;
    }

    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setText("OK");
            jButton.setCursor(null);
            jButton.setPreferredSize(new java.awt.Dimension(67, 23));
            jButton.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            jButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    lastUsedLinkModel = list.get(jComboBox.getSelectedIndex());
                    linkModel = (DbSMSLinkModel) lastUsedLinkModel;
                    if (jComboBox1.getSelectedIndex() == 0) {
                        sourceObject = objects.get(0);
                        targetObject = objects.get(1);
                    } else {
                        sourceObject = objects.get(1);
                        targetObject = objects.get(0);
                    }
                    cancel = false;
                    dispose();

                }
            });
        }
        return jButton;
    }

    /**
     * This method initializes jButton1
     * 
     * @return javax.swing.JButton
     */
    private JButton getJButton1() {
        if (jButton1 == null) {
            jButton1 = new JButton();
            jButton1.setText(LocaleMgr.screen.getString("Cancel"));
            jButton1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jButton1.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
            jButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    dispose();
                }
            });
        }
        return jButton1;
    }

    /**
     * This method initializes jComboBox
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getJComboBox() {
        if (jComboBox == null) {
            try {
                DbSMSProject project = (DbSMSProject) objects.get(0).getProject();
                list = AnySemObject.getAllDbSMSLinkModel(project);
                if (list != null && list.size() != 0) {
                    Object[] objs = new Object[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        objs[i] = list.get(i).buildFullNameString();
                    }

                    jComboBox = new JComboBox(objs);
                    if (lastUsedLinkModel != null) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).equals(lastUsedLinkModel)) {
                                jComboBox.setSelectedIndex(i);
                                break;
                            }
                        }
                    }

                }
            } catch (DbException e) {

            }
            if (jComboBox == null)
                jComboBox = new JComboBox();
        }
        return jComboBox;
    }

    /**
     * This method initializes jComboBox1
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getJComboBox1() {
        if (jComboBox1 == null) {
            try {
                jComboBox1 = new JComboBox(new Object[] { objects.get(0).getName(),
                        objects.get(1).getName() });
            } catch (DbException dbe) {
            }
        }
        return jComboBox1;
    }

    /**
     * This method initializes jPanel2
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            jPanel2 = new JPanel();
            jPanel2.setLayout(null);
        }
        return jPanel2;
    }

    /**
     * This method initializes jPanel3
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel3() {
        if (jPanel3 == null) {
            jPanel3 = new JPanel();
            GridBagConstraints gridBagConstraints44 = new GridBagConstraints();
            GridBagConstraints gridBagConstraints45 = new GridBagConstraints();
            jPanel3.setLayout(new GridBagLayout());
            jPanel3.setPreferredSize(new java.awt.Dimension(100, 100));
            gridBagConstraints44.gridx = 0;
            gridBagConstraints44.gridy = 0;
            gridBagConstraints44.insets = new java.awt.Insets(0, 0, 0, 0);
            gridBagConstraints44.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints44.weightx = 1.0D;
            gridBagConstraints44.weighty = 0.0D;
            gridBagConstraints44.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints45.gridx = 0;
            gridBagConstraints45.gridy = 1;
            gridBagConstraints45.insets = new java.awt.Insets(0, 0, 0, 0);
            gridBagConstraints45.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints45.weighty = 1.0D;
            gridBagConstraints45.anchor = java.awt.GridBagConstraints.SOUTHEAST;
            gridBagConstraints45.weightx = 1.0D;
            gridBagConstraints45.gridheight = 2;
            jPanel3.add(getJPanel4(), gridBagConstraints44);
            jPanel3.add(getJPanel5(), gridBagConstraints45);
        }
        return jPanel3;
    }

    /**
     * This method initializes jPanel4
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel4() {
        if (jPanel4 == null) {
            jPanel4 = new JPanel();
            GridBagConstraints gridBagConstraints48 = new GridBagConstraints();
            jPanel4.setLayout(new GridBagLayout());
            gridBagConstraints48.gridx = 0;
            gridBagConstraints48.gridy = 0;
            gridBagConstraints48.ipadx = 0;
            gridBagConstraints48.ipady = 0;
            gridBagConstraints48.insets = new java.awt.Insets(0, 0, 0, 0);
            gridBagConstraints48.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints48.weightx = 1.0D;
            gridBagConstraints48.weighty = 0.0D;
            gridBagConstraints48.fill = java.awt.GridBagConstraints.BOTH;
            jPanel4.add(getJPanel(), gridBagConstraints48);
        }
        return jPanel4;
    }

    /**
     * This method initializes jPanel5
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel5() {
        if (jPanel5 == null) {
            jPanel5 = new JPanel();
            GridBagConstraints gridBagConstraints47 = new GridBagConstraints();
            jPanel5.setLayout(new GridBagLayout());
            gridBagConstraints47.gridx = 1;
            gridBagConstraints47.gridy = 1;
            gridBagConstraints47.insets = new java.awt.Insets(0, 0, 0, 0);
            gridBagConstraints47.anchor = java.awt.GridBagConstraints.SOUTHEAST;
            gridBagConstraints47.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints47.gridwidth = 1;
            gridBagConstraints47.weightx = 1.0D;
            gridBagConstraints47.weighty = 1.0D;
            jPanel5.setMaximumSize(new java.awt.Dimension(160, 20));
            jPanel5.setPreferredSize(new java.awt.Dimension(160, 20));
            jPanel5.setMinimumSize(new java.awt.Dimension(160, 20));
            jPanel5.add(getJPanel1(), gridBagConstraints47);
        }
        return jPanel5;
    }
} //  @jve:decl-index=0:visual-constraint="108,98"
