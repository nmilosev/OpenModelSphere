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

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.gui.wizard2.AbstractPage;
import org.modelsphere.jack.gui.wizard2.SectionHeader;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.features.international.LocaleMgr;
import org.modelsphere.sms.or.db.DbORNotation;

@SuppressWarnings("serial")
class DMModelTypePage extends AbstractPage {
    private static final String kLogical_Data_Model = LocaleMgr.screen
            .getString("Logical_Data_Model");
    private static final String kPhysical_Data_Model = LocaleMgr.screen
            .getString("Physical_Data_Model");
    private static final String ENTITY_RELATIONSHIP = LocaleMgr.screen
            .getString("Entity_Relationship");
    private static final String kData_Model_Type = LocaleMgr.screen.getString("Data_Model_Type");

    private JRadioButton ERButton = new JRadioButton(ENTITY_RELATIONSHIP);
    private JRadioButton LDMButton = new JRadioButton(kLogical_Data_Model);
    private JRadioButton PDMButton = new JRadioButton(kPhysical_Data_Model);

    private StartupWizardModel model = null;

    DMModelTypePage(StartupWizardModel model) {
        super(new GridBagLayout());
        this.model = model;
        init();
    }

    private void init() {
        ButtonGroup radioGroup = new ButtonGroup();

        SectionHeader header = new SectionHeader(kData_Model_Type);

        radioGroup.add(LDMButton);
        radioGroup.add(PDMButton);
        radioGroup.add(ERButton);
        LDMButton.setSelected(true);

        add(header, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        add(ERButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(6, 24, 0, 0), 0, 0));
        add(LDMButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(6, 24, 0, 0), 0, 0));
        add(PDMButton, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(6, 24, 0, 0), 0, 0));
        add(Box.createVerticalGlue(), new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        switch (model.getDataModelType()) {
        case StartupWizardModel.DATAMODEL_TYPE_LOGICAL:
            LDMButton.setSelected(true);
            model.setDmNotation(model.getOrNotation());
            break;
        case StartupWizardModel.DATAMODEL_TYPE_ENTITY_RELATION:
            ERButton.setSelected(true);
            model.setDmNotation(model.getErNotation());
            break;
        case StartupWizardModel.DATAMODEL_TYPE_PHYSICAL:
            PDMButton.setSelected(true);
            model.setDmNotation(model.getOrNotation());
            break;

        default:
            break;
        }

        ///////// Action Listeners  
        ERButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                model.setDmNotation(model.getErNotation());
                model.setDataModelType(StartupWizardModel.DATAMODEL_TYPE_ENTITY_RELATION);
            }
        });

        LDMButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                model.setDmNotation(model.getOrNotation());
                model.setDataModelType(StartupWizardModel.DATAMODEL_TYPE_LOGICAL);
            }
        });

        PDMButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                model.setDmNotation(model.getOrNotation());
                model.setDataModelType(StartupWizardModel.DATAMODEL_TYPE_PHYSICAL);
            }
        });
    }

    @Override
    public void load() throws DbException {
        if (model.getOrNotation() == null)
            model.setOrNotation(getDefaultORNotationStr());
        if (model.getDmNotation() == null)
            model.setDmNotation(model.getOrNotation());
        if (model.getErNotation() == null)
            model.setErNotation(getDefaultErNotationStr());
    }

    private String getDefaultORNotationStr() throws DbException {
        DbProject project = model.getChosenProject();

        project.getDb().beginReadTrans();
        DbORNotation orNotation = ((DbSMSProject) project).getOrDefaultNotation();
        String notationStr = orNotation.getName();
        project.getDb().commitTrans();
        return notationStr;
    }

    private String getDefaultErNotationStr() throws DbException {
        DbProject project = model.getChosenProject();

        project.getDb().beginReadTrans();
        DbORNotation erNotation = ((DbSMSProject) project).getErDefaultNotation();
        String notationStr = erNotation.getName();
        project.getDb().commitTrans();
        return notationStr;
    }

    @Override
    public void save() throws DbException {
    }

    @Override
    public void rollback() throws DbException {
    }

    @Override
    public String getTitle() {
        return kData_Model_Type;
    }

}
