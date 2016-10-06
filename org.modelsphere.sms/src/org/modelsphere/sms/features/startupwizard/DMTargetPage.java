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
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Position;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.gui.wizard2.AbstractPage;
import org.modelsphere.jack.gui.wizard2.SectionHeader;
import org.modelsphere.jack.util.CollationComparator;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.jack.util.SrSort;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.features.international.LocaleMgr;
import org.modelsphere.sms.plugins.TargetSystem;
import org.modelsphere.sms.plugins.TargetSystemInfo;
import org.modelsphere.sms.plugins.TargetSystemManager;

@SuppressWarnings("serial")
class DMTargetPage extends AbstractPage {
    private static final String kChooseTargetSystem = LocaleMgr.screen
            .getString("Choose_a_Target_System");
    private static final String kTitle = LocaleMgr.screen.getString("TargetSystem");

    private JList targetSystemsList = new JList();
    private int targetSystemIndex = -1;
    private DefaultComparableElement[] targetSystemComparables;

    private StartupWizardModel model = null;

    DMTargetPage(StartupWizardModel model) {
        super(new GridBagLayout());
        this.model = model;
        init();
    }

    private void init() {
        JScrollPane scrollPane = new JScrollPane(targetSystemsList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        targetSystemsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        SectionHeader header = new SectionHeader(kChooseTargetSystem);

        add(header, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        add(scrollPane, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, new Insets(6, 24, 0, 0), 0, 0));

        targetSystemsList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (targetSystemComparables == null)
                    return;
                targetSystemIndex = targetSystemsList.getSelectedIndex();
                if (targetSystemIndex == -1)
                    model.setTargetSystemInfo(null);
                else {
                    model
                            .setTargetSystemInfo((TargetSystemInfo) targetSystemComparables[targetSystemIndex].object);
                }

            }
        });
    }

    JList getTargetsList() {
        return targetSystemsList;
    }

    @Override
    public void load() throws DbException {
        if (targetSystemsList.getModel().getSize() == 0) {
            initTargetSystemList();
        }

        int id = model.getDefaultTargetSystemID();
        if (targetSystemComparables != null) {
            for (int i = 0; i < targetSystemComparables.length; i++) {
                TargetSystemInfo targetInfo = (TargetSystemInfo) targetSystemComparables[i].object;
                if (targetInfo.getID() == id) {
                    getTargetsList().setSelectedValue(targetSystemComparables[i], true);
                    break;
                }
            }
        } else {
            // Set list or refresh last selection
            if (targetSystemIndex == -1)
                getTargetsList().setSelectedIndex(-1);
            else {
                getTargetsList().setSelectedIndex(targetSystemIndex);
                getTargetsList().ensureIndexIsVisible(targetSystemIndex);
            }
        }
    }

    private void initTargetSystemList() throws DbException {
        DbProject project = model.getChosenProject();

        project.getDb().beginReadTrans();
        TargetSystem targetSystem = TargetSystemManager.getSingleton();

        Iterator iter = targetSystem.getAllTargetSystemInfos().iterator();
        ArrayList<DefaultComparableElement> tsList = new ArrayList<DefaultComparableElement>();

        while (iter.hasNext()) {
            TargetSystemInfo targetSystemInfo = (TargetSystemInfo) iter.next();
            tsList.add(new DefaultComparableElement(targetSystemInfo, targetSystemInfo.getName()
                    + " " + targetSystemInfo.getVersion()));
        }
        targetSystemComparables = new DefaultComparableElement[tsList.size()];
        Object[] array = tsList.toArray(targetSystemComparables);
        CollationComparator comparator = new CollationComparator();
        SrSort.sortArray(targetSystemComparables, targetSystemComparables.length, comparator);
        getTargetsList().setListData(array);
        project.getDb().commitTrans();

    }

    @Override
    public void save() throws DbException {
    }

    @Override
    public void rollback() throws DbException {
        getTargetsList().setSelectedIndex(targetSystemIndex);
        getTargetsList().ensureIndexIsVisible(targetSystemIndex);
    }

    @Override
    public String getTitle() {
        return kTitle;
    }

}
