package org.modelsphere.jack.plugins;

import java.io.*;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.plugins.io.*;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.FileUtils;
import org.xml.sax.SAXException;

public class PluginInstaller {
    private static final String NEWER_VERSION = LocaleMgr.screen.getString("PluginNewer");
    private static final String PLUGINS_SAME01 = LocaleMgr.screen.getString("PluginSame01");
    private static final String PLUGINS_CHANGE0 = LocaleMgr.screen.getString("PluginChange0");
    private static final String NOT_VALID_ARCHIVE = LocaleMgr.screen.getString("NotValidArchive");
    private static final String FAILED_UNZIP0 = LocaleMgr.screen.getString("FailedInstall0");
    private static final String INVALID_ZIP0 = LocaleMgr.screen.getString("InvalidZip0");

    private ZipFile zipFile;
    private JDialog dialog;

    PluginInstaller(ZipFile zipFile, JDialog dialog) {
        this.zipFile = zipFile;
        this.dialog = dialog;
    }

    public void install() {
        try {
            // First check if a plugin.xml is found
            boolean valid = false;
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                if (zipEntry.isDirectory())
                    continue;
                String name = zipEntry.getName();
                if (name.toLowerCase().equals("plugin.xml")) {
                    valid = true;
                    break;
                }
            }

            if (!valid) {
                JOptionPane.showMessageDialog(dialog, NOT_VALID_ARCHIVE, dialog.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            } else {
                // Unzip to temp directory
                installImpl();
            }

            zipFile.close();
        } catch (ZipException e1) {
            JOptionPane.showMessageDialog(dialog, MessageFormat.format(INVALID_ZIP0,
                    new Object[] { zipFile.getName() }), dialog.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(dialog, MessageFormat.format("Error reading file '{0}'.",
                    new Object[] { zipFile.getName() }), dialog.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e1) {
            JOptionPane
                    .showMessageDialog(dialog, MessageFormat.format(INVALID_ZIP0,
                            new Object[] { zipFile.getName() })
                            + "\n" + e1.getLocalizedMessage(), dialog.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
        }

    }

    private void installImpl() throws IOException, XPathExpressionException,
            ParserConfigurationException, SAXException, InvalidSignatureException {
        File tempDirectory = File.createTempFile("modelsphere", "temp");
        if (tempDirectory.exists())
            FileUtils.deleteTree(tempDirectory);
        if (tempDirectory.mkdir()) {
            extract(zipFile, tempDirectory);
            File[] files = tempDirectory.listFiles();
            File xmlFile = null;
            for (File tempFile : files) {
                if (tempFile.isDirectory())
                    continue;
                String name = tempFile.getName();
                if (name.toLowerCase().equals("plugin.xml")) {
                    xmlFile = tempFile;
                    break;
                }
            }
            PluginDescriptor pluginDescriptor = XMLPluginUtilities.loadSignatureFile(xmlFile);

            if (pluginDescriptor != null) {
                // check if a plugin with the same id is already installed
                PluginsRegistry registry = PluginMgr.getSingleInstance().getPluginsRegistry();
                List<PluginDescriptor> existingPluginDescriptors = registry.getPluginDescriptors();
                int index = existingPluginDescriptors.indexOf(pluginDescriptor);
                PluginDescriptor existingPluginDescriptor = index > -1 ? existingPluginDescriptors
                        .get(index) : null;
                if (existingPluginDescriptor != null
                        && existingPluginDescriptor.getContext().getLoader() == pluginDescriptor
                                .getContext().getLoader()) {
                    String message = null;
                    int versioncompare = PluginLoader.compareVersions(existingPluginDescriptor,
                            pluginDescriptor);
                    if (versioncompare > 0)
                        message = NEWER_VERSION;
                    else if (versioncompare == 0)
                        message = MessageFormat.format(PLUGINS_SAME01, new Object[] {
                                existingPluginDescriptor.getVersion(),
                                pluginDescriptor.getVersion() });

                    if (message != null) {
                        int result = JOptionPane.showConfirmDialog(dialog, message, dialog
                                .getTitle(), JOptionPane.YES_NO_CANCEL_OPTION);
                        if (result != JOptionPane.YES_OPTION) {
                            return;
                        }
                    }

                    if (!checkLicense(pluginDescriptor)) {
                        return;
                    }

                    if (!PluginMgr.getSingleInstance().isValid(existingPluginDescriptor)) {
                        PluginMgr.getSingleInstance().installPlugin(pluginDescriptor);
                        PluginLoader.registerCommand(pluginDescriptor, new InstallCommand(
                                tempDirectory));
                    } else {
                        PluginLoader.registerCommand(existingPluginDescriptor, new InstallCommand(
                                tempDirectory));
                    }
                    PluginLoader.registerCommand(existingPluginDescriptor, new InstallCommand(
                            tempDirectory));
                    if (pluginDescriptor.getVersion() != null)
                        existingPluginDescriptor.setVersion(pluginDescriptor.getVersion());

                    if (versioncompare == 0) {
                        JOptionPane.showMessageDialog(dialog, MessageFormat.format(PLUGINS_CHANGE0,
                                new Object[] { ApplicationContext.getApplicationName() }), dialog
                                .getTitle(), JOptionPane.INFORMATION_MESSAGE);
                    }

                } else {
                    if (!checkLicense(pluginDescriptor)) {
                        return;
                    }
                    PluginMgr.getSingleInstance().installPlugin(pluginDescriptor);
                    PluginLoader.registerCommand(pluginDescriptor,
                            new InstallCommand(tempDirectory));
                }
            } else {
                JOptionPane.showMessageDialog(dialog, NOT_VALID_ARCHIVE, dialog.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(dialog, MessageFormat.format(FAILED_UNZIP0,
                    new Object[] { zipFile.getName() }), dialog.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    private boolean checkLicense(PluginDescriptor pluginDescriptor) {
        URL licenseURL = pluginDescriptor.getLicenseURL();
        if (licenseURL != null) {
            int result = LicenseAgreementDialog.showLicenseAgreement(dialog, pluginDescriptor,
                    false);
            if (result != LicenseAgreementDialog.ACCEPT_OPTION)
                return false;
        }
        return true;
    }

    private void extract(ZipFile zipFile, File toDirectory) throws IOException {
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            if (!zipEntry.isDirectory())
                continue;
            File directory = new File(toDirectory, zipEntry.getName());
            if (!directory.mkdir()) {
                throw new RuntimeException("Failed to extract archive");
            }
        }
        entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            if (zipEntry.isDirectory())
                continue;
            File file = new File(toDirectory, zipEntry.getName());
            if (!file.createNewFile()) {
                throw new RuntimeException("Failed to extract archive");
            }
            FileOutputStream out = null;
            InputStream in = null;
            try {
                out = new FileOutputStream(file);
                in = zipFile.getInputStream(zipEntry);
                copy(in, out);
            } finally {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            }
        }
    }

    private void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;

        while ((read = in.read(buffer)) > 0) {
            out.write(buffer, 0, read);
        }
    }
}
