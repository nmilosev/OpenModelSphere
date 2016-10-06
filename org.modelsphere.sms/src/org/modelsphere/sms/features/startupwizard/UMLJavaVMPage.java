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
import java.io.File;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.swing.*;

import org.modelsphere.jack.awt.dirchooser.DirectoryChooser2;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.gui.wizard2.AbstractPage;
import org.modelsphere.jack.gui.wizard2.SectionHeader;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.features.international.LocaleMgr;

@SuppressWarnings("serial")
class UMLJavaVMPage extends AbstractPage {
    private static final String kJava_Virtual_Machine = LocaleMgr.screen
            .getString("Java_Virtual_Machine");
    private static final String kUse_Default_Virtual_Machine = LocaleMgr.screen
            .getString("Use_Default_Virtual_Machine");
    private static final String kUse_another_Virtual_machine = LocaleMgr.screen
            .getString("Use_another_Virtual_machine");

    private JRadioButton defaultVMButton = new JRadioButton(kUse_Default_Virtual_Machine);
    private JRadioButton userVMButton = new JRadioButton(kUse_another_Virtual_machine);
    private JButton userVMBrowseBtn = new JButton("...");
    private JLabel userVMField = new JLabel();
    private JLabel currentVMField = new JLabel();

    private StartupWizardModel model = null;

    UMLJavaVMPage(StartupWizardModel model) {
        super(new GridBagLayout());
        this.model = model;
        init();
    }

    private void init() {
        ButtonGroup radioGroup = new ButtonGroup();

        SectionHeader header = new SectionHeader(kJava_Virtual_Machine);

        try {
            currentVMField.setText(getJREID(model.getDefaultJREHome()));
        } catch (IOException e) {
            currentVMField.setText("");
        }
        try {
            userVMField.setText(getJREID(model.getCustomJREHome()));
        } catch (IOException e) {
            userVMField.setText("");
        }

        currentVMField.setBorder(BorderFactory.createLineBorder(getBackground().darker(), 1));
        userVMField.setBorder(BorderFactory.createLineBorder(getBackground().darker(), 1));

        radioGroup.add(defaultVMButton);
        radioGroup.add(userVMButton);
        defaultVMButton.setSelected(true);

        add(header, new GridBagConstraints(0, 0, 4, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        add(defaultVMButton, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(6, 24, 0, 0), 0, 0));
        add(currentVMField, new GridBagConstraints(0, 2, 2, 1, 0.1, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 48, 0, 0), 0, 0));
        add(userVMButton, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(9, 24, 0, 0), 0, 0));
        add(userVMBrowseBtn, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(9, 6, 0, 0), 0, 0));
        add(userVMField, new GridBagConstraints(0, 4, 2, 1, 0.1, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 48, 0, 0), 0, 0));

        add(Box.createHorizontalGlue(), new GridBagConstraints(2, 1, 2, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        add(Box.createVerticalGlue(), new GridBagConstraints(0, 10, 4, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        defaultVMButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                model.setJREHome(model.getDefaultJREHome());
            }
        });
        userVMButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                model.setJREHome(model.getCustomJREHome());
            }
        });
        userVMBrowseBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                selectCustomVM();
            }
        });

    }

    private void selectCustomVM() {
        JDialog parent = (JDialog) SwingUtilities.getAncestorOfClass(JDialog.class, this);
        File selected = model.getDefaultJREHome();
        if (model.getCustomJREHome() != null) {
            selected = model.getCustomJREHome();
        }

        parent.getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        File file = DirectoryChooser2.selectDirectory(parent, selected,
                "Select the JRE home directory");
        parent.getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        if (file != null) {
            File jreHome = null;
            if (file.exists()) {
                // convert to parent folder if bin or lib folder
                if (file.isDirectory()
                        && (file.getName().equals("bin") || file.getName().equals("lib"))) {
                    file = file.getParentFile();
                }

                if (!file.isDirectory() && file.getName().equals("rt.jar")) {
                    // rt.jar selected
                    jreHome = file.getParentFile().getParentFile();
                } else if (file.isDirectory()) {
                    // exact JRE home
                    File rtFile = new File(file, "lib" + File.separator + "rt.jar");
                    if (rtFile.exists()) {
                        jreHome = file;
                    } else {
                        // check JDK case
                        rtFile = new File(file, "jre" + File.separator + "lib" + File.separator
                                + "rt.jar");
                        if (rtFile.exists()) {
                            jreHome = new File(file, "jre");
                        }
                    }
                }
            }

            if (jreHome == null && file != null) {
                JOptionPane.showMessageDialog(this, "No JRE found in the specified directory",
                        ApplicationContext.getApplicationName(), JOptionPane.ERROR_MESSAGE);
                // do not change model, preserve current values
            } else if (jreHome != null) {
                model.setCustomJREHome(jreHome);
                model.setJREHome(jreHome);
                try {
                    userVMField.setText(getJREID(jreHome));
                } catch (IOException e) {
                    userVMField.setText("");
                }
                userVMButton.setSelected(true);
            }
        }

    }

    private String getJREID(File jreHome) throws IOException {
        String id = null;
        if (jreHome != null && jreHome.exists()) {
            File rtfile = new File(jreHome, "lib" + File.separator + "rt.jar");
            if (rtfile.exists()) {
                JarFile rtjar = new JarFile(rtfile);
                try {
                    Manifest manifest = rtjar.getManifest();
                    if (manifest != null) {
                        Attributes attributes = manifest.getMainAttributes();
                        if (attributes != null) {
                            String vendor = attributes.getValue("Implementation-Vendor");
                            String title = attributes.getValue("Implementation-Title");
                            String version = attributes.getValue("Implementation-Version");
                            if (title == null)
                                title = "";
                            if (version == null)
                                version = "";
                            if (vendor == null)
                                vendor = "";

                            id = "<html><body><b>" + title + " " + version + "</b><br>";
                            id += "<i>" + vendor + "</i><br>";
                            id += jreHome.getPath() + "</body></html>";
                        }
                    }
                } finally {
                    rtjar.close();
                }
            }
        }

        if (id == null) {
            id = "<html><body> <br> <br> </body></html>";
        }
        return id;
    }

    @Override
    public void load() throws DbException {
    }

    @Override
    public void save() throws DbException {
    }

    @Override
    public void rollback() throws DbException {
    }

    @Override
    public String getTitle() {
        return kJava_Virtual_Machine;
    }

}
