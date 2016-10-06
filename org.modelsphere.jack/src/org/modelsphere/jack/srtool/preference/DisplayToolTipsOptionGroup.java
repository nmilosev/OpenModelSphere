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

package org.modelsphere.jack.srtool.preference;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.beans.*;
import javax.swing.*;

import org.modelsphere.jack.awt.checklist.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.preference.*;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public class DisplayToolTipsOptionGroup extends OptionGroup {
    public static final String TOOLTIPS_FIELD_VISIBILITY_PREFIX = "visibleInToolTips"; // NOT LOCALIZABLE

    private static MetaField[] availableMetaFields = null;
    private static MetaField[] toolTipsMetaFields = null;

    static {
        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
        preferences.addPrefixPropertyChangeListener(DisplayToolTipsOptionGroup.class,
                TOOLTIPS_FIELD_VISIBILITY_PREFIX, new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent evt) {
                        toolTipsMetaFields = null;
                    }
                });
    }

    private static class DisplayToolTipsOptionPanel extends OptionPanel implements ActionListener,
            CheckListListener {
        private JPanel diagOptionPanel = new JPanel();
        private JButton defButton = new JButton(LocaleMgr.screen.getString("default"));
        private JLabel displayButtonsLabel = new JLabel(LocaleMgr.screen
                .getString("additionalToolTipsContents"));

        private CheckList list = new CheckList();
        private java.util.List items;

        DisplayToolTipsOptionPanel(java.util.List items) {
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

            add(listPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                    GridBagConstraints.BOTH, new Insets(18, 6, 11, 5), 100, 0));
            add(defButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(5, 0, 6, 5),
                    0, 0));

            defButton.addActionListener(this);
        }

        public void init() {
            PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                CheckListItem item = (CheckListItem) iter.next();
                item.setSelected(preferences.getPropertyBoolean(DisplayToolTipsOptionGroup.class,
                        getKey((MetaField) item.getUserObject()), Boolean.FALSE).booleanValue());
            }
            list.repaint();
        }

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == defButton) {
                Iterator iter = items.iterator();
                while (iter.hasNext()) {
                    CheckListItem item = (CheckListItem) iter.next();
                    item.setSelected(false);
                    PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
                    fireOptionChanged(preferences, DisplayToolTipsOptionGroup.class,
                            getKey((MetaField) item.getUserObject()), Boolean.FALSE);
                }
                toolTipsMetaFields = null;
                list.repaint();
            }
        }

        public void itemSelectedStateChanged(CheckListItem item) {
            PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
            fireOptionChanged(preferences, DisplayToolTipsOptionGroup.class,
                    getKey((MetaField) item.getUserObject()), item.isSelected() ? Boolean.TRUE
                            : Boolean.FALSE);
            toolTipsMetaFields = null;
        }
    };

    public DisplayToolTipsOptionGroup() {
        super(LocaleMgr.misc.getString("ToolTipsExplorer"));
    }

    private static String getKey(MetaField metafield) {
        String key = TOOLTIPS_FIELD_VISIBILITY_PREFIX + "_"
                + metafield.getMetaClass().getJClass().getName() + "_" + metafield.getJName(); // NOT LOCALIZABLE
        return key;
    }

    protected OptionPanel createOptionPanel() {
        if (availableMetaFields == null)
            return null;
        ArrayList items = new ArrayList();
        for (int i = 0; i < availableMetaFields.length; i++) {
            items.add(new CheckListItem(availableMetaFields[i].getMetaClass().getGUIName(false,
                    false)
                    + " - " + availableMetaFields[i].getGUIName(), null, false,
                    availableMetaFields[i])); // NOT LOCALIZABLE
        }
        return new DisplayToolTipsOptionPanel(items);
    }

    public static MetaField[] getAvailableMetaFields() {
        return availableMetaFields;
    }

    public static void setAvailableMetaFields(MetaField[] metaFields) {
        // avoid replacing config
        if (availableMetaFields == null)
            availableMetaFields = metaFields;
    }

    // Service Methods to access this options group values
    public static MetaField[] getToolTipsMetaFields() {
        if (toolTipsMetaFields != null)
            return toolTipsMetaFields;
        ArrayList tempfields = new ArrayList();
        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();

        for (int i = 0; i < availableMetaFields.length; i++) {
            if (!preferences.getPropertyBoolean(DisplayToolTipsOptionGroup.class,
                    getKey(availableMetaFields[i]), Boolean.FALSE).booleanValue())
                continue;
            tempfields.add(availableMetaFields[i]);
        }

        toolTipsMetaFields = new MetaField[tempfields.size()];
        for (int i = 0; i < toolTipsMetaFields.length; i++) {
            toolTipsMetaFields[i] = (MetaField) tempfields.get(i);
        }

        return toolTipsMetaFields;
    }

}
