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

package org.modelsphere.sms.preference;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.modelsphere.jack.awt.JackFileTextItem;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.preference.OptionPanel;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.international.LocaleMgr;

public class DirectoryOptionGroup extends OptionGroup {
    private static final String DDL_GENERATION_DIRECTORY = "DDLGenerationDirectory"; // NOT
    // LOCALIZABLE
    private static final String CODE_GENERATION_DIRECTORY = "CodeGenerationPrefLabel"; // NOT
    // LOCALIZABLE
    private static final String HTML_GENERATION_DIRECTORY = "HtmlGenerationDirectory"; // NOT

    // LOCALIZABLE

    private static class DirectoryOptionPanel extends OptionPanel implements ActionListener {
        JPanel freeSpacePanel = new JPanel();

        PreferenceFileTextItem defaultDirField = new PreferenceFileTextItem(
                ApplicationContext.class, ApplicationContext.DEFAULT_WORKING_DIRECTORY);
        PreferenceFileTextItem ddlGenDirField = new PreferenceFileTextItem(
                DirectoryOptionGroup.class, DDL_GENERATION_DIRECTORY);
        PreferenceFileTextItem codeGenDirField = new PreferenceFileTextItem(
                DirectoryOptionGroup.class, CODE_GENERATION_DIRECTORY);
        PreferenceFileTextItem htmlGenDirField = new PreferenceFileTextItem(
                DirectoryOptionGroup.class, HTML_GENERATION_DIRECTORY);

        JLabel defaultDirLabel = new JLabel(LocaleMgr.screen
                .getString("DefaultWorkingDirectoryPrefLabel"));
        JLabel ddlGenDirLabel = new JLabel(LocaleMgr.misc.getString("DDLGenerationPrefLabel"));
        JLabel codeGenDirLabel = new JLabel(LocaleMgr.misc.getString("CodeGenerationPrefLabel"));
        JLabel htmlGenDirLabel = new JLabel(LocaleMgr.misc.getString("HtmlGenerationPrefLabel"));
        JButton defButton = new JButton(LocaleMgr.misc.getString("Default"));

        DirectoryOptionPanel() {
            setLayout(new GridBagLayout());
            add(defaultDirLabel,
                    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                            GridBagConstraints.NONE, new Insets(18, 9, 5, 5), 0, 0));
            add(defaultDirField, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 9, 11, 5), 0, 0));

            add(ddlGenDirLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 9, 5, 5), 0, 0));
            add(ddlGenDirField, new GridBagConstraints(0, 3, 2, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 9, 11, 5), 0, 0));

            add(codeGenDirLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 9, 5, 5), 0, 0));
            add(codeGenDirField, new GridBagConstraints(0, 5, 2, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 9, 11, 5), 0, 0));

            add(htmlGenDirLabel, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 9, 5, 5), 0, 0));
            add(htmlGenDirField, new GridBagConstraints(0, 7, 2, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 9, 11, 5), 0, 0));

            add(Box.createVerticalGlue(),
                    new GridBagConstraints(0, 8, 2, 1, 1.0, 1.0, GridBagConstraints.SOUTH,
                            GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
            add(defButton, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                    GridBagConstraints.NONE, new Insets(6, 12, 6, 5), 0, 0));

            defButton.addActionListener(this);
            ddlGenDirLabel.setVisible(true);
            ddlGenDirField.setVisible(true);
            codeGenDirLabel.setVisible(true);
            codeGenDirField.setVisible(true);
        }

        public void actionPerformed(ActionEvent e) {
            defaultDirField.updatedFileFieldPerformed(getDefaultWorkingDirectory());
            ddlGenDirField.updatedFileFieldPerformed(getDefaultWorkingDirectory());
            codeGenDirField.updatedFileFieldPerformed(getDefaultWorkingDirectory());
            htmlGenDirField.updatedFileFieldPerformed(getDefaultWorkingDirectory());
        }

        private class PreferenceFileTextItem extends JackFileTextItem {
            private String key;
            private Class cls;

            public PreferenceFileTextItem(Class cls, String key) {
                super();
                this.key = key;
                this.cls = cls;
            }

            protected void updatedFileFieldPerformed(String value) {
                super.updatedFileFieldPerformed(value);
                PropertiesSet prefs = PropertiesManager.getPreferencePropertiesSet();
                fireOptionChanged(prefs, cls, key, value);
            }
        }

        public void init() {
            PropertiesSet prefs = PropertiesManager.getPreferencePropertiesSet();
            defaultDirField.setFileNameFieldText(ApplicationContext.getDefaultWorkingDirectory());
            ddlGenDirField.setFileNameFieldText(prefs.getPropertyString(DirectoryOptionGroup.class,
                    DDL_GENERATION_DIRECTORY, getDefaultWorkingDirectory()));
            codeGenDirField.setFileNameFieldText(prefs.getPropertyString(
                    DirectoryOptionGroup.class, CODE_GENERATION_DIRECTORY,
                    getDefaultWorkingDirectory()));
            htmlGenDirField.setFileNameFieldText(prefs.getPropertyString(
                    DirectoryOptionGroup.class, HTML_GENERATION_DIRECTORY,
                    getDefaultWorkingDirectory()));
        }
    };

    public DirectoryOptionGroup() {
        super(LocaleMgr.screen.getString("Directories"));
    }

    protected OptionPanel createOptionPanel() {
        return new DirectoryOptionPanel();
    }

    // Service methods

    public static String getDDLGenerationDirectory() {
        PropertiesSet prefs = PropertiesManager.getPreferencePropertiesSet();
        return prefs.getPropertyString(DirectoryOptionGroup.class, DDL_GENERATION_DIRECTORY,
                getDefaultWorkingDirectory());
    }

    public static String getJavaGenerationDirectory() {
        PropertiesSet prefs = PropertiesManager.getPreferencePropertiesSet();
        return prefs.getPropertyString(DirectoryOptionGroup.class, CODE_GENERATION_DIRECTORY,
                getDefaultWorkingDirectory());
    }

    public static String getHTMLGenerationDirectory() {
        PropertiesSet prefs = PropertiesManager.getPreferencePropertiesSet();
        return prefs.getPropertyString(DirectoryOptionGroup.class, HTML_GENERATION_DIRECTORY,
                getDefaultWorkingDirectory());
    }

    public static String getDefaultWorkingDirectory() {
        return ApplicationContext.getDefaultWorkingDirectory();
    }

}
