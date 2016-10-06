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

import javax.swing.JPanel;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.gui.wizard2.AbstractPage;
import org.modelsphere.jack.gui.wizard2.SectionHeader;
import org.modelsphere.sms.be.db.DbBEModel;

@SuppressWarnings("serial")
class BPMSummaryPage extends AbstractPage {

    private StartupWizardModel model = null;
    private JPanel iconsPanel = new JPanel();

    BPMSummaryPage(StartupWizardModel model) {
        super(new GridBagLayout());
        this.model = model;
        init();
    }

    private void init() {

        SectionHeader header = new SectionHeader(StartupWizard.kIconsPageTitle);
        iconsPanel.setLayout(new GridBagLayout());

        add(header, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        add(iconsPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, new Insets(6, 24, 0, 0), 0, 0));

    }

    @Override
    public void load() throws DbException {
        iconsPanel.removeAll();
        StartupWizardUtilities.displayAvailableTools(model, iconsPanel, DbBEModel.metaClass);
    }

    @Override
    public void save() throws DbException {

    }

    @Override
    public void rollback() throws DbException {
    }

    @Override
    public String getTitle() {
        return StartupWizard.kSummary;
    }

}
