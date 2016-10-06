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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.modelsphere.jack.gui.wizard2.Wizard;
import org.modelsphere.sms.features.international.LocaleMgr;

@SuppressWarnings("serial")
public final class StartupWizard extends Wizard {

    // resources shared by more than one pages
    static final String kChoose_a_notation = LocaleMgr.screen.getString("Choose_a_Notation");
    static final String kPreview = LocaleMgr.screen.getString("Preview");
    static final String kIconsPageTitle = LocaleMgr.screen.getString("IconsPageTitle");
    static final String kSummary = LocaleMgr.screen.getString("Summary");
    static final String kNotation = LocaleMgr.screen.getString("Notation");

    private static final String kShow_startup = LocaleMgr.screen.getString("Show_startup");
    private JCheckBox showAtStartupCheckBox = new JCheckBox(kShow_startup);

    /**
     * @param frame
     * @param title
     * @param startup
     *            we change default selection on the first page according to startup. if true, open
     *            existing project is selected by default.
     */
    public StartupWizard(Frame frame, String title, boolean startup) {
        super(frame);
        setTitle(title);
        try {
            init(startup);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void init(boolean startup) throws Exception {
        setModel(new StartupWizardModel(startup));

        JPanel optionsPanel = new JPanel(new GridBagLayout());
        optionsPanel.add(showAtStartupCheckBox, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        setOptionsPanel(optionsPanel);
        showAtStartupCheckBox.setSelected(StartupWizardModel.getModelCreationWizardProperty());
        showAtStartupCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                boolean selected = showAtStartupCheckBox.isSelected();
                StartupWizardModel.setModelCreationWizardProperty(selected);
            }
        });
        
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(700, 500);
    }

    @Override
    protected void cancel() {
        StartupWizardModel model = (StartupWizardModel) getModel();
        model.rollback(this);
    }

    @Override
    protected void finish() {
        StartupWizardModel model = (StartupWizardModel) getModel();
        model.commit(this);
    }

}
