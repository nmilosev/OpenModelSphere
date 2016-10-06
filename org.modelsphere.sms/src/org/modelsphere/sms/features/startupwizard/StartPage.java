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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/
package org.modelsphere.sms.features.startupwizard;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.gui.wizard2.AbstractPage;
import org.modelsphere.jack.gui.wizard2.SectionHeader;
import org.modelsphere.sms.features.international.LocaleMgr;

@SuppressWarnings("serial")
class StartPage extends AbstractPage {
    private static final String kOpen_an_existing_model = LocaleMgr.screen
            .getString("Open_an_existing_model");
    private static final String kCreate_a_new_Model = LocaleMgr.screen
            .getString("Create_a_new_Model");
    private static final String kTitle = LocaleMgr.screen.getString("SelectTask");

    private JRadioButton createNewButton = new JRadioButton(kCreate_a_new_Model);
    private JRadioButton openExistingButton = new JRadioButton(kOpen_an_existing_model);
    private JList recentFilesList = new JList();

    private StartupWizardModel model = null;

    StartPage(StartupWizardModel model) {
        super(new GridBagLayout());
        this.model = model;
        init();
    }

    private void init() {
        ButtonGroup buttonGroup = new ButtonGroup();
        JScrollPane recentFilesScrollPane = new JScrollPane(recentFilesList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        SectionHeader titleHeader = new SectionHeader(kTitle);

        buttonGroup.add(createNewButton);
        buttonGroup.add(openExistingButton);

        add(titleHeader, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        add(createNewButton, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 24, 0, 0), 0, 0));
        add(openExistingButton, new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(6, 24, 0, 0), 0,
                0));

        add(recentFilesScrollPane, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(6, 48, 0, 0), 0, 0));
        add(Box.createHorizontalGlue(), new GridBagConstraints(1, 3, 1, 1, 0.05, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(6, 48, 0, 0), 0, 0));
        add(Box.createVerticalGlue(), new GridBagConstraints(0, 4, 2, 1, 1.0, 0.1,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(6, 48, 0, 0), 0, 0));

        createNewButton.setSelected(true);

        recentFilesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        createNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                model.setNewModel(true);
            }
        });

        openExistingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                model.setNewModel(false);
            }
        });

        recentFilesList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                openExistingButton.setSelected(true);
                Object selection = recentFilesList.getSelectedValue();
                model.setNewModel(false);
                if (selection == null)
                    model.setOpenFileName(null);
                else {
                    model.setOpenFileName(selection.toString());
                }
            }
        });
    }

    @Override
    public void load() throws DbException {
        java.util.List<String> recentFiles = model.getRecentFiles();
        Object[] array = recentFiles.toArray();
        recentFilesList.setListData(array);
        this.createNewButton.setSelected(model.isNewModel());
        this.openExistingButton.setEnabled(recentFiles.size() > 0);
        this.openExistingButton.setSelected(!model.isNewModel());
        recentFilesList.setSelectedValue(model.getOpenFileName(), true);
    }

    @Override
    public void save() throws DbException {
        if (recentFilesList.getSelectedIndex() < 0)
            model.setOpenFileName(null);
        else
            model.setOpenFileName(recentFilesList.getSelectedValue().toString());
    }

    @Override
    public void rollback() throws DbException {
    }

    @Override
    public String getTitle() {
        return kTitle;
    }

}
