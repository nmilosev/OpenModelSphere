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
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.gui.wizard.WizardPage;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSUserDefinedPackage;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.international.LocaleMgr;

/**
 * 
 * For each kind of database object (tablespace, table, index, etc.), a user can decide if he/she
 * wants to reverse it. Only selected kinds of objects will be reverse engineered.
 * 
 */
public class DefaultReverseWizardStartPage extends WizardPage implements
        NodeSelectionChangeListener {
    private static final String kModelOptions = LocaleMgr.screen.getString("ModelOptions");
    private static final String kUseRoot = LocaleMgr.screen.getString("UseRoot");
    private static final String kNewProject = LocaleMgr.screen.getString("NewProject");
    private static final String kExistingProjUDPackage = LocaleMgr.screen
            .getString("ExistingProjUDPackage");
    private static final String kSelectProjectOrUDPackage = LocaleMgr.screen
            .getString("SelectProjectOrUDPackage");
    private static final String kUseDomainModel = LocaleMgr.screen.getString("UseDomainModel");
    private static final String kNewDomainModel = LocaleMgr.screen.getString("NewDomainModel");
    private static final String kExistingDomainModel = LocaleMgr.screen
            .getString("ExistingDomainModel");
    private static final String kSelectADomainModel = LocaleMgr.screen
            .getString("SelectADomainModel");
    private static final String kNoProjectSelected = LocaleMgr.message
            .getString("NoProjectSelected");
    private static final String kNoDomainSelected = LocaleMgr.message
            .getString("NoDomainModSelected");

    private TitledBorder optionsPanelBorder = new TitledBorder(kModelOptions);
    private JPanel optionsPanel = new JPanel(new GridBagLayout());
    private NodeSelectionPanel domainModelPanel;
    private NodeSelectionPanel rootModelPanel;

    public DefaultReverseWizardStartPage() {
        super();
    }

    private void initGUI(DBMSReverseOptions options) {
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

        rootModelPanel = new NodeSelectionPanel(new MetaClass[] { DbSMSProject.metaClass,
                DbSMSUserDefinedPackage.metaClass }, roots);
        rootModelPanel.setHeaderText(kUseRoot);
        rootModelPanel.setNewText(kNewProject);
        rootModelPanel.setExistingText(kExistingProjUDPackage);
        rootModelPanel.setLookupText(kSelectProjectOrUDPackage);
        rootModelPanel.addNodeSelectionChangeListener(this);

        domainModelPanel = new NodeSelectionPanel(new MetaClass[] { DbORDomainModel.metaClass },
                new DbObject[] { options.root });
        domainModelPanel.setTargetSystemId(options.targetSystemId);
        domainModelPanel.setHeaderText(kUseDomainModel);
        domainModelPanel.setNewText(kNewDomainModel);
        domainModelPanel.setExistingText(kExistingDomainModel);
        domainModelPanel.setLookupText(kSelectADomainModel);

        optionsPanel.setBorder(optionsPanelBorder);
        optionsPanel.add(rootModelPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 9, 9), 0, 0));
        optionsPanel.add(domainModelPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 9, 9), 0, 0));
        optionsPanel.add(Box.createVerticalGlue(), new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        add(optionsPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, new Insets(12, 12, 12, 12), 0, 0));

        setNextEnabled(true);
    }

    public final boolean initialize(Object opt) {
        DBMSReverseOptions options = (DBMSReverseOptions) opt;
        initGUI(options);
        // init GUI from options
        setNextEnabled(true);
        domainModelPanel.setSelectedNode(options.domainModel);
        rootModelPanel.setSelectedNode(options.root);
        if (options.root == null) {
            domainModelPanel.setSelectedNode(null);
            domainModelPanel.setEnabled(false);
        } else {
            domainModelPanel.setRoots(new DbObject[] { options.root.getProject() });
            domainModelPanel.setEnabled(true);
        }
        return true;
    }

    public final boolean terminate(Object opt, boolean saveData) {
        DBMSReverseOptions options = (DBMSReverseOptions) opt;
        if (!saveData)
            return true;
        // check root
        if (rootModelPanel.isUseExistingSelected() && (rootModelPanel.getSelectedNode() == null)) {
            int result = JOptionPane.showConfirmDialog(this, kNoProjectSelected,
                    ApplicationContext.getApplicationName(), JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.NO_OPTION)
                return false;
        }
        // check domain model
        if (rootModelPanel.isUseExistingSelected() && domainModelPanel.isUseExistingSelected()
                && (domainModelPanel.getSelectedNode() == null)) {
            int result = JOptionPane.showConfirmDialog(this, kNoDomainSelected,
                    ApplicationContext.getApplicationName(), JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.NO_OPTION)
                return false;
        }

        // save GUI setting
        options.root = rootModelPanel.getSelectedNode();
        options.domainModel = domainModelPanel.getSelectedNode();
        return true;
    }

    // only called for update on root selection
    public void nodeSelectionChanged(Object source, int eventId, DbObject node) {
        Object proj1 = domainModelPanel.getSelectedNode() != null ? domainModelPanel
                .getSelectedNode().getProject() : null;
        Object proj2 = node != null ? node.getProject() : null;

        if (proj1 != proj2 || proj1 == null) {
            domainModelPanel.setSelectedNode(null);
        }

        if (eventId == NodeSelectionChangeListener.USE_NEW || proj2 == null) {
            domainModelPanel.setSelectedNode(null);
            domainModelPanel.setEnabled(false);
        } else {
            domainModelPanel.setEnabled(true);
            if (node != null)
                domainModelPanel.setRoots(new DbObject[] { node.getProject() });
        }
    }

}
