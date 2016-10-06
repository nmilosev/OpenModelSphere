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

package org.modelsphere.jack.actions;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;

import org.modelsphere.jack.awt.JackComboBox;
import org.modelsphere.jack.awt.PopupColorPanel;
import org.modelsphere.jack.awt.PopupCommandHistoryPanel;
import org.modelsphere.jack.awt.checklist.CheckList;
import org.modelsphere.jack.awt.checklist.CheckListItem;
import org.modelsphere.jack.awt.checklist.CheckListListener;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.preference.*;

public class ActionsOptionGroup extends OptionGroup {
    private JToolBar toolbar;

    private static class ActionsOptionPanel extends OptionPanel implements ActionListener,
            CheckListListener {
        private JPanel diagOptionPanel = new JPanel();
        private JButton defButton = new JButton(LocaleMgr.misc.getString("Default"));
        private JLabel displayButtonsLabel = new JLabel(LocaleMgr.screen
                .getString("displayButtons"));

        private CheckList list = new CheckList();
        private java.util.List items;

        ActionsOptionPanel(java.util.List items) {
            setLayout(new GridBagLayout());
            this.items = items;
            list.setListData(items.toArray());
            list.addCheckListListener(this);

            JPanel listPanel = new JPanel(new GridBagLayout());
            listPanel
                    .add(displayButtonsLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                            GridBagConstraints.WEST, GridBagConstraints.BOTH,
                            new Insets(0, 0, 6, 0), 0, 0));
            listPanel
                    .add(new JScrollPane(list), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                            GridBagConstraints.WEST, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));

            add(listPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.5, GridBagConstraints.WEST,
                    GridBagConstraints.BOTH, new Insets(12, 6, 5, 5), 100, 0));
            add(Box.createHorizontalGlue(), new GridBagConstraints(0, 1, 3, 1, 0.5, 0.5,
                    GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
            add(defButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 6, 5),
                    0, 0));

            defButton.addActionListener(this);
        }

        public void init() {
            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                CheckListItem item = (CheckListItem) iter.next();
                item.setSelected(((AbstractApplicationAction) item.getUserObject())
                        .getToolBarVisibilityOption());
            }
            list.repaint();
        }

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == defButton) {
                Iterator iter = items.iterator();
                while (iter.hasNext()) {
                    CheckListItem item = (CheckListItem) iter.next();
                    item.setSelected(((AbstractApplicationAction) item.getUserObject())
                            .getDefaultToolBarVisibility());
                    PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
                    fireOptionChanged(preferences, getKey(item.getUserObject()),
                            AbstractApplicationAction.TOOLBAR_VISIBLE_OPTION,
                            item.isSelected() ? Boolean.TRUE : Boolean.FALSE);
                }
                list.repaint();
            }
        }

        private String getKey(Object action) {
            return AbstractApplicationAction.getToolBarVisibilityKey(action);
        }

        public void itemSelectedStateChanged(CheckListItem item) {
            PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
            fireOptionChanged(preferences, getKey(item.getUserObject()),
                    AbstractApplicationAction.TOOLBAR_VISIBLE_OPTION,
                    item.isSelected() ? Boolean.TRUE : Boolean.FALSE);
        }
    };

    public ActionsOptionGroup(JToolBar toolbar) {
        super(toolbar.getName());
        this.toolbar = toolbar;
    }

    protected OptionPanel createOptionPanel() {
        Component[] components = toolbar.getComponents();
        ArrayList items = new ArrayList();
        AbstractApplicationAction applAction = null;
        for (int i = 0; i < components.length; i++) {
            Action action = null;
            if (components[i] instanceof AbstractButton)
                action = ((AbstractButton) components[i]).getAction();
            else if (components[i] instanceof JackComboBox)
                action = ((JackComboBox) components[i]).getAction();
            else if (components[i] instanceof PopupColorPanel)
                action = ((PopupColorPanel) components[i]).getAction();
            else if (components[i] instanceof PopupCommandHistoryPanel)
                action = ((PopupCommandHistoryPanel) components[i]).getAction();
            else
                continue;
            if (action instanceof AbstractApplicationAction) {
                applAction = (AbstractApplicationAction) action;
                items.add(new CheckListItem((String) applAction.getValue(Action.NAME),
                        (Icon) applAction.getValue(Action.SMALL_ICON), applAction
                                .getToolBarVisibilityOption(), applAction));
            }
        }
        return new ActionsOptionPanel(items);
    }

}
