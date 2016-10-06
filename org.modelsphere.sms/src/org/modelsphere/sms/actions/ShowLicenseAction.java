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

package org.modelsphere.sms.actions;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import javax.swing.JCheckBox;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.gui.ScrollableTextDialog2;
import org.modelsphere.jack.io.IoUtil;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.international.LocaleMgr;

@SuppressWarnings("serial")
public final class ShowLicenseAction extends AbstractApplicationAction {

    private ScrollableTextDialog2 m_dialog;
    private static final String kShow_startup = org.modelsphere.sms.features.international.LocaleMgr.screen
            .getString("Show_startup");

    ShowLicenseAction() {
        super(LocaleMgr.action.getString("License") + "...");
        this.setMnemonic(LocaleMgr.action.getMnemonic("License"));
    }

    //
    // ShowLicenseAtStartup property
    //
    public static final String SHOW_LICENSE_AT_STARTUP_KEY = "ShowLicenseAtStartup"; // NOT LOCALIZABLE, property key
    public static final Boolean SHOW_LICENSE_AT_STARTUP_KEY_DEFAULT = Boolean.TRUE;

    public final void doActionPerformed() {
        MainFrame frame = MainFrame.getSingleton();
        m_dialog = new ScrollableTextDialog2(frame);
        String title = LocaleMgr.action.getString("Licenses");
        m_dialog.setTitle(title);

        final JCheckBox showStartupCheck = new JCheckBox(kShow_startup);
        boolean showStartup = getShowLicenceAtStartupPreference();
        showStartupCheck.setSelected(showStartup);
        m_dialog.add(showStartupCheck,
                new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST,
                        GridBagConstraints.BOTH, new Insets(0, 3, 0, 0), 0, 0));

        showStartupCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                boolean selected = showStartupCheck.isSelected();
                setShowLicenseAtStartupPreference(selected);
            }
        });

        //add licence texts and show dialogs
        addOpenMSLicense(m_dialog);
        addGPLLicense(m_dialog);
        addProprietaryLicences(m_dialog);
        showDialog(m_dialog);
    } //end doActionPerformed()

    public boolean getShowLicenceAtStartupPreference() {
        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
        boolean showLicenceAtStartup = (preferences == null) ? false : preferences
                .getPropertyBoolean(ShowLicenseAction.class, SHOW_LICENSE_AT_STARTUP_KEY,
                        SHOW_LICENSE_AT_STARTUP_KEY_DEFAULT);
        return showLicenceAtStartup;
    } //end getShowLicenceAtStartupPreference()

    public void setShowLicenseAtStartupPreference(boolean value) {
        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
        if (preferences != null) {
            preferences.setProperty(ShowLicenseAction.class, SHOW_LICENSE_AT_STARTUP_KEY, value);
        }
    } //end setShowLicenseAtStartupPreference()

    public boolean isDialogVisible() {
        boolean visible = (m_dialog == null) ? false : m_dialog.isVisible();
        return visible;
    }

    //
    // private methods
    //
    private void addGPLLicense(ScrollableTextDialog2 dialog) {
        URL licenseURL = getGPLLicenseURL();
        dialog.addPanel("GPL", licenseURL); //NOT LOCALIZABLE
    }

    private void addOpenMSLicense(ScrollableTextDialog2 dialog) {
        URL licenseURL = getOpenMS_LicenseURL();
        dialog.addPanel("Open ModelSphere", licenseURL);
    }

    private void addProprietaryLicences(ScrollableTextDialog2 dialog) {
        String companyName = null;
        URL proprietaryUrl = null;
        Locale locale = LocaleMgr.getLocaleFromPreferences(Locale.getDefault());
        String lang = locale.getLanguage();
        String folderPath = System.getProperty("user.dir");
        folderPath = folderPath.concat("/plugins/bin/com");
        String filename = "ComPlugins.properties"; //NOT LOCALIZABLE		
        File file = new File(folderPath, filename);
        try {
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                while (line != null) {
                    if (line.startsWith("PluginCompany")) {
                        int i = line.indexOf("=");
                        companyName = line.substring(i + 1, line.length());
                        String companyFolderPath = folderPath.concat("/" + companyName);
                        String licenseFilename = "license_" + lang + ".txt";
                        File licenseFile = new File(companyFolderPath, licenseFilename);
                        try {
                            if (licenseFile.exists()) {
                                proprietaryUrl = licenseFile.toURL();
                                dialog.addPanel(companyName, proprietaryUrl);
                            }
                        } catch (IOException ioe) {
                        } // Invalid url access, skip	
                    }
                    line = reader.readLine();
                }
                reader.close();
            }
        } catch (Exception e) {
        }
    }

    private void showDialog(ScrollableTextDialog2 dialog) {
        dialog.setSize(500, 600);
        AwtUtil.centerWindow(m_dialog);
        dialog.setVisible(true);
    } //end showDialog()

    private static URL getGPLLicenseURL() {
        Locale locale = LocaleMgr.getLocaleFromPreferences(Locale.getDefault());
        String lang = locale.getLanguage();
        String filename = "gpl_" + lang + ".txt";
        String resource = "international/resources/" + filename;
        URL url = IoUtil.getURL(MainFrame.class, resource);
        return url;
    } //end getGPLLicenseURL()

    private static URL getOpenMS_LicenseURL() {
        Locale locale = LocaleMgr.getLocaleFromPreferences(Locale.getDefault());
        String lang = locale.getLanguage();
        String filename = "OpenMS_License_" + lang + ".txt";
        String resource = "international/resources/" + filename;
        URL url = IoUtil.getURL(MainFrame.class, resource);
        return url;
    } //end getOpenMS_LicenseURL()

    private String getCompanyFolderName() {
        String folderPath = System.getProperty("user.dir"); //NOT LOCALIZABLE, property
        folderPath = folderPath.concat("/plugins/bin/com");
        String filename = "ComPlugins.properties"; //NOT LOCALIZABLE, independant from current language
        String companyPath = null;
        File file = new File(folderPath, filename);
        try {
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                while (line != null) {
                    if (line.startsWith("PluginCompany")) {
                        int i = line.indexOf("=");
                        companyPath = line.substring(i + 1, line.length());
                        return companyPath;
                    }
                    line = reader.readLine();
                }
                reader.close();
            }
        } catch (Exception e) {
        }

        return companyPath;
    }

    //
    // DEMO
    //
    public static final void main(String[] args) {
        runDemo();
    } //end main()

    private static void runDemo() {
        ShowLicenseAction action = new ShowLicenseAction();
        action.doActionPerformed();
    } //end runDemo()

} //end ShowLicenseAction
