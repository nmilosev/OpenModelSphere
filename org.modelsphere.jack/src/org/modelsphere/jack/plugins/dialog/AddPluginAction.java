package org.modelsphere.jack.plugins.dialog;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.MessageFormat;
import java.util.zip.ZipFile;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.plugins.PluginConfigurationHandler;
import org.modelsphere.jack.plugins.PluginInstaller;
import org.modelsphere.jack.plugins.PluginMgr;
import org.modelsphere.jack.preference.PropertiesManager;

@SuppressWarnings("serial")
class AddPluginAction extends AbstractAction {
    private static final String PROPERTY_DOWNLOAD_DIRECTORY = "downloadDirectory";

    private static final String ADD = LocaleMgr.screen.getString("Add_");
    private static final String FILE_CANT_READ0 = LocaleMgr.screen.getString("FileCantRead0");
    private static final String SELECT_ZIP = LocaleMgr.screen.getString("SelectZip");
    private static final String PLUGIN_ZIP = LocaleMgr.screen.getString("PluginZip");

    private PluginConfigurationHandler configurationHandler;

    AddPluginAction(PluginConfigurationHandler configurationHandler) {
        super(ADD);
        this.configurationHandler = configurationHandler;
        setEnabled(configurationHandler != null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        PluginsManagerDialog dialog = (PluginsManagerDialog) SwingUtilities.getAncestorOfClass(
                PluginsManagerDialog.class, (Component) e.getSource());
        File file = selectFile(dialog);
        if (file == null)
            return;
        if (!file.canRead()) {
            JOptionPane
                    .showMessageDialog(dialog, MessageFormat.format(FILE_CANT_READ0,
                            new Object[] { file.toString() }), dialog.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            ZipFile zipFile = new ZipFile(file);
            PluginInstaller installer = configurationHandler.createPluginInstaller(zipFile, dialog);
            installer.install();
        } catch (Exception ex) {
            Debug.trace(ex);
        }

    }

    private File selectFile(Component parent) {
        String downloadDirectory = PropertiesManager.PLUGINS_PROPERTIES_SET.getPropertyString(
                PluginMgr.class, PROPERTY_DOWNLOAD_DIRECTORY, System.getProperty("user.home"));
        File lastDirectory = new File(downloadDirectory);

        JFileChooser chooser = new JFileChooser(lastDirectory);
        chooser.setDialogTitle(SELECT_ZIP);

        FileFilter filter = new PluginFileFilter();
        chooser.setFileFilter(filter);
        
        int result = chooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            lastDirectory = file.getParentFile();
            if (lastDirectory != null)
                PropertiesManager.PLUGINS_PROPERTIES_SET.setProperty(PluginMgr.class,
                        PROPERTY_DOWNLOAD_DIRECTORY, lastDirectory.getPath());
            return file;
        }
        return null;
    }

    private static final String[] PLUGIN_EXTENSIONS = new String[] { "jar", "zip" };

    private static class PluginFileFilter extends ExtensionFileFilter {

        public PluginFileFilter() {
            super(PLUGIN_EXTENSIONS, PLUGIN_ZIP);
        }

    } //end FileFilter

}
