package org.modelsphere.jack.plugins.xml.extensions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;

import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.plugins.xml.XmlPlugin;
import org.modelsphere.jack.plugins.xml.XmlPluginDescriptor;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.util.ExceptionHandler;

public class OpenFileExtension extends AbstractPluginExtension {
    private String _fileExtension;
    private String _fileDescription;
    private XmlPluginDescriptor _descriptor;
    private static Map<String, OpenFileExtension> _extensions = new HashMap<String, OpenFileExtension>();

    public OpenFileExtension(XmlPluginDescriptor pluginDescriptor, Map<String, String> parameters) {
        super(pluginDescriptor, parameters);
        
        //get parameters
        _fileExtension = super.getParameter("fileExtension");
        _fileDescription = super.getParameter("fileDescription");
        _descriptor = pluginDescriptor;
        
        //add this extension to the collection of extensions
        _extensions.put(_fileExtension, this);
    }

    @Override
    public void createPluginAction(XmlPluginDescriptor pluginDescriptor) {  
        //no action created        
    }

    private void openFile(JFileChooser chooser, Db db, File file) {
        XmlPlugin plugin = _descriptor.getPluginInstance();
        
        try {
            ActionEvent ev = new OpenFileActionEvent(chooser, db, file);
            plugin.execute(ev);
        } catch (Exception ex) {
            DefaultMainFrame frame = ApplicationContext.getDefaultMainFrame();
            ExceptionHandler.processUncatchedException(frame, ex); 
        }
    }
    
    //
    // Static methods
    //
    
    //
    // Called by jack.srtool.DefaultMainFrame.doOpen()
    //
    public static void addPluginFileFilers(JFileChooser chooser) {
        
        //for each open file extension
        Set<String> keys = _extensions.keySet();
        for (String ext : keys) {
            OpenFileExtension openFileExtension = _extensions.get(ext);
            XmlPluginDescriptor descriptor = openFileExtension._xmlDescriptor;
            
            if (descriptor.isEnabled()) {
                String description = openFileExtension.getDescription();
                ExtensionFileFilter filter = new ExtensionFileFilter(ext, description);
                chooser.addChoosableFileFilter(filter);
            }
        } 
    } //end addPluginFileFilers()

    private String getDescription() {
        return _fileDescription;
    }

    public static boolean canOpen(String extension) {
        boolean openable = _extensions.containsKey(extension);
        return openable;
    }

    public static void openFile(String extension, JFileChooser chooser, Db db, File file) {
        OpenFileExtension openFileExtension = _extensions.get(extension); 
        openFileExtension.openFile(chooser, db, file);
    }
    
    //
    // inner class
    //
    @SuppressWarnings("serial")
    public static class OpenFileActionEvent extends ActionEvent {
        private Db _db; 
        private File _file;
        
        public OpenFileActionEvent(JFileChooser chooser, Db db, File file) {
            super(chooser, 0, (String)null);
            _db = db;
            _file = file;
        }

        public Db getDb() {
            return _db;
        }

        public File getFile() {
            return _file;
        }
        
    }

}
