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

package org.modelsphere.jack.preference.context;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JCheckBox;

import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.preference.OptionPanel;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;

@SuppressWarnings("serial")
class ContextOptionPanel extends OptionPanel {

    private JCheckBox loadLastProjects;
    private JCheckBox restoreWorkspace;

    ContextOptionPanel() {
        super(new GridBagLayout());

        loadLastProjects = new JCheckBox(LocaleMgr.screen.getString("loadLastProjects"));
        restoreWorkspace = new JCheckBox(LocaleMgr.screen.getString("restoreWorkspace"));

        add(loadLastProjects, new GridBagConstraints(0, 0, 3, 1, 1, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(12, 6, 6, 6), 0, 0));
        add(restoreWorkspace, new GridBagConstraints(0, 1, 3, 1, 1, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(0, 6, 6, 6), 0, 0));

        add(Box.createVerticalGlue(), new GridBagConstraints(0, 8, 3, 1, 1, 1,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        restoreWorkspace.setSelected(true);
        loadLastProjects.setSelected(true);

        loadLastProjects.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                restoreWorkspace.setEnabled(loadLastProjects.isSelected());
                PropertiesSet prefset = PropertiesManager.getPreferencePropertiesSet();
                fireOptionChanged(prefset, ContextOptionGroup.class,
                        ContextOptionGroup.LOAD_LAST_PROJECTS, loadLastProjects.isSelected());
            }
        });
        restoreWorkspace.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PropertiesSet prefset = PropertiesManager.getPreferencePropertiesSet();
                fireOptionChanged(prefset, ContextOptionGroup.class,
                        ContextOptionGroup.RESTORE_WORKSPACE, restoreWorkspace.isSelected());
            }
        });

    }

    @Override
    public void init() {
        PropertiesSet prefs = PropertiesManager.getPreferencePropertiesSet();
        loadLastProjects.setSelected((prefs.getPropertyBoolean(ContextOptionGroup.class,
                ContextOptionGroup.LOAD_LAST_PROJECTS,
                ContextOptionGroup.LOAD_LAST_PROJECTS_DEFAULT).booleanValue()));
        restoreWorkspace.setSelected((prefs.getPropertyBoolean(ContextOptionGroup.class,
                ContextOptionGroup.RESTORE_WORKSPACE, ContextOptionGroup.RESTORE_WORKSPACE_DEFAULT)
                .booleanValue()));
    }

}
