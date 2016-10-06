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

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.screen.DbTreeLookupDialog;
import org.modelsphere.jack.baseDb.screen.model.DbTreeModelListener;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.jack.util.SrVector;
import org.modelsphere.sms.db.DbSMSAbstractPackage;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.plugins.TargetSystemInfo;
import org.modelsphere.sms.plugins.TargetSystemManager;

/**
 * 
 * For each kind of database object (tablespace, table, index, etc.), a user can decide if he/she
 * wants to reverse it. Only selected kinds of objects will be reverse engineered.
 * 
 */
public final class NodeSelectionPanel extends JPanel implements ActionListener {
    private static final String kSelect = LocaleMgr.screen.getString("Select_");
    private static final String kNew = LocaleMgr.screen.getString("new");
    private static final String kExisting = LocaleMgr.screen.getString("Existing");
    private static final String kSelectNode = LocaleMgr.screen.getString("Select");

    // GUI Components
    private JLabel useNodeLabel = new JLabel(kSelect);
    private JRadioButton selNew = new JRadioButton(kNew);
    private JRadioButton selExisting = new JRadioButton(kExisting);
    private JButton selbutton = new JButton(kSelectNode);
    private JTextField selName = new JTextField(" "); // NOT LOCALIZABLE
    private ButtonGroup radioGroup = new ButtonGroup();

    // Used to keep the states when this Panel is enabled/disabled
    private boolean useNodeLabelEnable = true;
    private boolean selNewEnable = true;
    private boolean selExistingEnable = true;
    private boolean selbuttonEnable = true;
    private boolean selNameEnable = true;

    private String lookupTitle;

    private DbObject selNode;
    private DbObject[] roots;
    private MetaClass[] allowedMetaClasses;
    private int targetSystemId = -1;

    private SrVector nodeSelectionChangeListeners = new SrVector();

    private boolean newAllowed = true;

    // Do not display and allow selection of object contains in this ArrayList
    private ArrayList excludedObjs = null;

    private DbTreeModelListener nodeFilter = new DbTreeModelListener() {
        public boolean filterNode(DbObject dbo) throws DbException {
            if (dbo == null)
                return false;
            if (dbo instanceof DbSMSAbstractPackage) {
                TargetSystemInfo tsinfo = TargetSystemManager.getSingleton().getTargetSystemInfo(
                        ((DbSMSAbstractPackage) dbo).getTargetSystem());
                if ((tsinfo != null) && (tsinfo.getID() == targetSystemId)) {
                    if (excludedObjs != null)
                        return !excludedObjs.contains(dbo);
                    return true;
                }
                return false;
            }
            if (excludedObjs != null)
                return !excludedObjs.contains(dbo);
            return true;
        }
    };

    public NodeSelectionPanel(MetaClass[] allowedMetaClasses, DbObject[] roots) {
        this(allowedMetaClasses, roots, true);
    }

    public NodeSelectionPanel(MetaClass[] allowedMetaClasses, DbObject[] roots, boolean newAllowed) {
        setLayout(new GridBagLayout());

        this.allowedMetaClasses = allowedMetaClasses != null ? allowedMetaClasses
                : new MetaClass[] {};
        this.roots = roots;
        this.newAllowed = newAllowed;

        if (newAllowed) {
            add(useNodeLabel, new GridBagConstraints(0, 0, 2, 1, 0, 0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(3, 12, 3, 3), 0, 0));
            add(selNew, new GridBagConstraints(0, 1, 2, 1, 0, 0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(3, 24, 3, 3), 0, 0));
            add(selExisting, new GridBagConstraints(0, 2, 2, 1, 0, 0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(0, 24, 3, 3), 0, 0));
            radioGroup.add(selNew);
            radioGroup.add(selExisting);
        }
        int leftInset = (newAllowed ? 42 : 12);
        add(selName, new GridBagConstraints(0, 3, 1, 1, 0.5, 0, GridBagConstraints.EAST,
                GridBagConstraints.HORIZONTAL, new Insets(3, leftInset, 3, 3), 0, 5));
        add(selbutton, new GridBagConstraints(1, 3, 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));

        // init states
        selName.setEditable(false);

        if (newAllowed) {
            selNameEnable = false;
            selbuttonEnable = false;
            selNew.setSelected(true);
            selNew.addActionListener(this);
            selExisting.addActionListener(this);
        }
        selbutton.addActionListener(this);
        updateComponentsState();
    }

    public DbObject getSelectedNode() {
        if ((newAllowed && selExisting.isSelected()) || !newAllowed)
            return selNode;
        return null;
    }

    public void setSelectedNode(DbObject node) {
        if (node == null) {
            selNode = null;
            if (newAllowed) {
                selNew.setSelected(true);
                selNameEnable = false;
                selbuttonEnable = false;
            }
            selName.setText(" ");
            if (newAllowed) {
                fireNodeSelectionChangeListeners(NodeSelectionChangeListener.USE_NEW);
            } else
                fireNodeSelectionChangeListeners(NodeSelectionChangeListener.USE_EXISTING);
        } else {
            try {
                selNode = node;
                Db db = selNode.getDb();
                db.beginTrans(Db.READ_TRANS);
                String name = selNode.getSemanticalName(DbObject.LONG_FORM);
                selName.setText(name);
                selName.setToolTipText(name);
                db.commitTrans();
                if (newAllowed) {
                    selExisting.setSelected(true);
                    selNameEnable = true;
                    selbuttonEnable = true;
                }
                fireNodeSelectionChangeListeners(NodeSelectionChangeListener.USE_EXISTING);
            } catch (DbException ex) {
                ExceptionHandler.processUncatchedException(this, ex);
                selNode = null;
                if (newAllowed) {
                    selNew.setSelected(true);
                    selNameEnable = false;
                    selbuttonEnable = false;
                }
                if (newAllowed) {
                    fireNodeSelectionChangeListeners(NodeSelectionChangeListener.USE_NEW);
                } else
                    fireNodeSelectionChangeListeners(NodeSelectionChangeListener.USE_EXISTING);
            }
        }
        updateComponentsState();
    }

    public void setExistingText(String text) {
        selExisting.setText(text);
        revalidate();
    }

    public void setRoots(DbObject[] roots) {
        this.roots = roots;
    }

    public void setNewText(String text) {
        selNew.setText(text);
        revalidate();
    }

    public void setHeaderText(String text) {
        useNodeLabel.setText(text);
        revalidate();
    }

    public void setLookupText(String text) {
        lookupTitle = text;
    }

    public void setTargetSystemId(int id) {
        targetSystemId = id;
    }

    public boolean isUseExistingSelected() {
        return selExisting.isSelected();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == selbutton) {
            Object node = DbTreeLookupDialog.selectOne(this, lookupTitle, roots,
                    allowedMetaClasses, nodeFilter, null, null);
            if (node != null) {
                setSelectedNode((DbObject) node);
            } else
                fireNodeSelectionChangeListeners(NodeSelectionChangeListener.USE_EXISTING);
        } else {
            boolean sel = selExisting.isSelected();
            selbuttonEnable = sel;
            selNameEnable = sel;
            updateComponentsState();
            fireNodeSelectionChangeListeners(sel ? NodeSelectionChangeListener.USE_EXISTING
                    : NodeSelectionChangeListener.USE_NEW);
        }
    }

    public void setButtonText(String text) {
        selbutton.setText(text);
    }

    public JButton getSelectionButton() {
        return selbutton;
    }

    public void setExcludedObjects(ArrayList excludedObjs) {
        this.excludedObjs = excludedObjs;
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        updateComponentsState();
    }

    private void updateComponentsState() {
        boolean enabled = isEnabled();
        if (enabled) {
            useNodeLabel.setEnabled(useNodeLabelEnable);
            selNew.setEnabled(selNewEnable);
            selExisting.setEnabled(selExistingEnable);
            selbutton.setEnabled(selbuttonEnable);
            selName.setEnabled(selNameEnable);
        } else {
            useNodeLabel.setEnabled(false);
            selNew.setEnabled(false);
            selExisting.setEnabled(false);
            selbutton.setEnabled(false);
            selName.setEnabled(false);
        }
    }

    public void addNodeSelectionChangeListener(NodeSelectionChangeListener listener) {
        if (nodeSelectionChangeListeners.indexOf(listener) == -1)
            nodeSelectionChangeListeners.addElement(listener);
    }

    public void removeNodeSelectionChangeListener(NodeSelectionChangeListener listener) {
        nodeSelectionChangeListeners.removeElement(listener);
    }

    private void fireNodeSelectionChangeListeners(int id) {
        int nb = nodeSelectionChangeListeners.size();
        while (--nb >= 0) {
            NodeSelectionChangeListener listener = (NodeSelectionChangeListener) nodeSelectionChangeListeners
                    .elementAt(nb);
            listener.nodeSelectionChanged(this, id, selNode);
        }
    }

}
