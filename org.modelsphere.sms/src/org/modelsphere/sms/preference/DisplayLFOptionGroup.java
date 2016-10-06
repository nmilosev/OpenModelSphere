/*************************************************************************

Copyright (C) 2008 Grandite

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

package org.modelsphere.sms.preference;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.metal.MetalTheme;

import org.modelsphere.jack.awt.themes.ThemeBank;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.preference.OptionPanel;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.international.LocaleMgr;

public class DisplayLFOptionGroup extends OptionGroup {
    private static String CUSTOM_LF_PROPERTIES = "lf";
    private static ArrayList customLFs = null;

    private static class DisplayLFOptionPanel extends OptionPanel implements ActionListener {
        private JComboBox lfCombo;
        private JComboBox theme;
        private JLabel lfLabel = new JLabel();
        private JLabel themeLabel = new JLabel();
        private JButton defButton = new JButton(LocaleMgr.misc.getString("Default"));
        private Object[] lfValues;
        private Object[] themeValues;

        private class LFDescription implements Comparable {
            String lfStr;
            String lfClassName;

            LFDescription(String lf, String lfClassName) {
                this.lfStr = lf;
                this.lfClassName = lfClassName;
            }

            public boolean equals(Object obj) {
                if (obj == null || !(obj instanceof LFDescription))
                    return false;
                return this.lfClassName.equals(((LFDescription) obj).lfClassName);
            }

            public int compareTo(Object obj) {
                if (obj == null || !(obj instanceof LFDescription))
                    return 2;
                return this.lfClassName.compareTo(((LFDescription) obj).lfClassName);
            }

            public String toString() {
                return lfStr;
            }
        }

        private class ThemeDescription implements Comparable {
            String theme;

            ThemeDescription(String theme) {
                this.theme = theme;
            }

            public boolean equals(Object obj) {
                if (obj == null || !(obj instanceof ThemeDescription))
                    return false;
                return this.theme.equals(((ThemeDescription) obj).theme);
            }

            public int compareTo(Object obj) {
                if (obj == null || !(obj instanceof ThemeDescription))
                    return 2;
                return this.theme.compareTo(((ThemeDescription) obj).theme);
            }

            public String toString() {
                return theme;
            }
        }

        DisplayLFOptionPanel() {
            setLayout(new GridBagLayout());
            initValues();
            lfCombo = new JComboBox(lfValues);
            theme = new JComboBox(themeValues);

            lfLabel.setText(LocaleMgr.screen.getString("Look&Feel"));
            themeLabel.setText(LocaleMgr.screen.getString("theme"));
            lfCombo.setEditable(false);
            theme.setEditable(false);

            add(lfLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.BOTH, new Insets(18, 9, 0, 11), 0, 0));
            add(lfCombo, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(18, 0, 0, 5), 20, 0));

            add(themeLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.BOTH, new Insets(12, 9, 12, 11), 0, 0));
            add(theme, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(6, 0, 12, 5), 0, 0));

            add(Box.createVerticalGlue(), new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            add(defButton, new GridBagConstraints(0, 3, 2, 0, 0.0, 0.0,
                    GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 6, 5),
                    0, 0));

            defButton.addActionListener(this);
            lfCombo.addActionListener(this);
            theme.addActionListener(this);
        }

        private void initValues() {
            PropertiesManager.installPropertiesSet(CUSTOM_LF_PROPERTIES, CUSTOM_LF_PROPERTIES);

            LookAndFeelInfo[] lfs = UIManager.getInstalledLookAndFeels();
            MetalTheme[] themes = ThemeBank.themes;
            String osname = (String) System.getProperty("os.name"); // NOT
            // LOCALIZABLE
            ArrayList lfValues = new ArrayList();
            ArrayList themeValues = new ArrayList();
            if (osname == null)
                osname = ""; // NOT LOCALIZABLE
            for (int i = 0; i < lfs.length; i++) {
                if (((osname.toLowerCase().indexOf("windows") == -1) && lfs[i].getClassName()
                        .equals("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"))
                        || // NOT LOCALIZABLE
                        ((osname.toLowerCase().indexOf("sunos") == -1) && lfs[i].getClassName()
                                .equals("com.sun.java.swing.plaf.motif.MotifLookAndFeel"))) { // NOT
                    // LOCALIZABLE
                    continue;
                }
                LFDescription lfDescription = new LFDescription(lfs[i].getName(), lfs[i]
                        .getClassName());
                if (customLFs != null && customLFs.contains(lfDescription))
                    continue;
                lfValues.add(lfDescription);
                if (lfs[i].getClassName().equals("javax.swing.plaf.metal.MetalLookAndFeel")) { // NOT
                    // LOCALIZABLE
                    for (int j = 0; j < themes.length; j++) {
                        themeValues.add(new ThemeDescription(themes[j].getName()));
                    }
                }
            }

            if (customLFs == null) {
                customLFs = new ArrayList();
                int index = 0;
                PropertiesSet customLF = PropertiesManager.getPropertiesSet(CUSTOM_LF_PROPERTIES);
                String lfcustom = customLF.getPropertyString(javax.swing.LookAndFeel.class, "LF"
                        + index, "");
                try {
                    while (lfcustom.length() > 0) {
                        Class lfClass = Class.forName(lfcustom);
                        if (lfClass != null) {
                            LookAndFeel lkFeel = (LookAndFeel) lfClass.newInstance();
                            LFDescription lfDescription = new LFDescription(lkFeel.getName(),
                                    lfcustom);
                            if (!lfValues.contains(lfDescription)
                                    && !customLFs.contains(lfDescription)) {
                                customLFs.add(lfDescription);
                            }
                        }
                        index++;
                        lfcustom = customLF.getPropertyString(javax.swing.LookAndFeel.class, "LF"
                                + index, "");
                    }
                } catch (Exception e1) {
                } catch (Error e2) {
                }
            }

            lfValues.addAll(customLFs);

            Collections.sort(lfValues);
            Collections.sort(themeValues);
            this.lfValues = lfValues.toArray();
            this.themeValues = themeValues.toArray();

        }

        public void init() {
            PropertiesSet options = PropertiesManager.APPLICATION_PROPERTIES_SET;
            String sDefaultLF = UIManager.getSystemLookAndFeelClassName();
            if (sDefaultLF == null)
                sDefaultLF = UIManager.getLookAndFeel().getClass().getName();
            String sLf = options.getPropertyString(ApplicationContext.class,
                    ApplicationContext.LF_PROPERTY, sDefaultLF);
            int index = -1;
            LFDescription tempLF = new LFDescription("", sLf);
            for (int i = 0; i < lfValues.length; i++) {
                if (lfValues[i].equals(tempLF)) {
                    index = i;
                    break;
                }
            }
            if (index > -1)
                lfCombo.setSelectedIndex(index);

            String sTheme = options.getPropertyString(ApplicationContext.class,
                    ApplicationContext.THEME_PROPERTY, ThemeBank.themes[0].getName());
            index = -1;
            ThemeDescription tempTheme = new ThemeDescription(sTheme);
            for (int i = 0; i < themeValues.length; i++) {
                if (themeValues[i].equals(tempTheme)) {
                    index = i;
                    break;
                }
            }
            if (index > -1)
                theme.setSelectedIndex(index);

            updateTheme();
        }

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == lfCombo) {
                fireOptionChanged(PropertiesManager.APPLICATION_PROPERTIES_SET,
                        ApplicationContext.class, ApplicationContext.LF_PROPERTY,
                        ((LFDescription) lfCombo.getSelectedItem()).lfClassName);
                updateTheme();
            } else if (source == theme) {
                fireOptionChanged(PropertiesManager.APPLICATION_PROPERTIES_SET,
                        ApplicationContext.class, ApplicationContext.THEME_PROPERTY, theme
                                .getSelectedItem().toString());
                updateTheme();
            } else if (source == defButton) {
                setToDefault();
            } // end if
        } // end actionPerformed()

        public void setToDefault() {
            LFDescription defaultLF = new LFDescription("",
                    "javax.swing.plaf.metal.MetalLookAndFeel"); // NOT
            // LOCALIZABLE
            for (int i = 0; i < lfValues.length; i++) {
                if (lfValues[i].equals(defaultLF)) {
                    lfCombo.setSelectedIndex(i);
                    break;
                }
            } // end for
            updateTheme();
            ThemeDescription defTheme = new ThemeDescription(ThemeBank.themes[0].getName());
            for (int i = 0; i < themeValues.length; i++) {
                if (themeValues[i].equals(defTheme)) {
                    theme.setSelectedIndex(i);
                    break;
                }
            } // end for
        } // end setToDefault()

        private void updateTheme() {
            LFDescription lfDescr = (LFDescription) lfCombo.getSelectedItem();
            if (lfDescr.lfClassName.equals("javax.swing.plaf.metal.MetalLookAndFeel")) // NOT
                // LOCALIZABLE
                theme.setEnabled(true);
            else
                theme.setEnabled(false);
        }
    };

    public DisplayLFOptionGroup() {
        super(LocaleMgr.screen.getString("Look&Feel"));
    }

    protected OptionPanel createOptionPanel() {
        return new DisplayLFOptionPanel();
    }

}
