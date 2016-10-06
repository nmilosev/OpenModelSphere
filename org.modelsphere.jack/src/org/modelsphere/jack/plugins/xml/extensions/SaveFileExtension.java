package org.modelsphere.jack.plugins.xml.extensions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.plugins.xml.XmlPlugin;
import org.modelsphere.jack.plugins.xml.XmlPluginDescriptor;
import org.modelsphere.jack.plugins.xml.extensions.OpenFileExtension.OpenFileActionEvent;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.util.ExceptionHandler;

public class SaveFileExtension extends AbstractPluginExtension {
    private String _fileExtension;
    private String _fileDescription;
    private XmlPluginDescriptor _descriptor;
    private static Map<String, SaveFileExtension> _extensions = new HashMap<String, SaveFileExtension>();
    
    public SaveFileExtension(XmlPluginDescriptor pluginDescriptor, Map<String, String> parameters) {
        super(pluginDescriptor, parameters);
        
        //get parameters
        _fileExtension = super.getParameter("fileExtension");
        _fileDescription = super.getParameter("fileDescription");
        
        //add this extension to the collection of extensions
        _extensions.put(_fileExtension, this);
    }

    @Override
    public void createPluginAction(XmlPluginDescriptor pluginDescriptor) {
        //no action created
        _descriptor = pluginDescriptor;
    }

    public static void addPlugableFileFilters(JFileChooser chooser, ExtensionFileFilter[] optionalFilters) {
        //for each extension    
        for (ExtensionFileFilter filter : optionalFilters) {
            chooser.addChoosableFileFilter(filter); 
        }
    } //end addPlugableFileFilters()

    private String getDescription() {
        return _fileDescription;
    }
    
    public static ExtensionFileFilter[] getFileFilters() {
        List<ExtensionFileFilter> filterList = new ArrayList<ExtensionFileFilter>(); 
        
        //for each extension
        Set<String> keys = _extensions.keySet();
        for (String key : keys) {
            SaveFileExtension extension = _extensions.get(key);
            XmlPluginDescriptor descriptor = extension._xmlDescriptor;
            
            if (descriptor.isEnabled()) {
                String description = extension.getDescription();
                ExtensionFileFilter filter = new ExtensionFileFilter(key, description);
                filterList.add(filter); 
            } //end if
        } //end for
        
        ExtensionFileFilter[] filters = new ExtensionFileFilter[filterList.size()]; 
        filterList.toArray(filters);
        return filters;
    }

    public static boolean canSaveFile(FileFilter filter) {
        boolean saveable = false;
        
        if (filter instanceof ExtensionFileFilter) {
            ExtensionFileFilter eff = (ExtensionFileFilter)filter; 
            String ext = eff.getExtension();
            saveable = _extensions.containsKey(ext);
        }
        
        return saveable;
    }

    public static void saveFile(FileFilter filter, File file) {
        if (filter instanceof ExtensionFileFilter) {
            ExtensionFileFilter eff = (ExtensionFileFilter)filter; 
            String ext = eff.getExtension();
            SaveFileExtension extension = _extensions.get(ext);
            extension.saveFile(file); 
        }
    }

    private void saveFile(File file) {
       XmlPlugin plugin = _descriptor.getPluginInstance();
        
       try {
           JFrame mainFrame = ApplicationContext.getDefaultMainFrame();
           ActionEvent ev = new SaveFileActionEvent(mainFrame, file);
           plugin.execute(ev);
       } catch (Exception ex) {
           DefaultMainFrame frame = ApplicationContext.getDefaultMainFrame();
           ExceptionHandler.processUncatchedException(frame, ex); 
       }
    }
    
    //
    // inner class
    //
    @SuppressWarnings("serial")
    public static class SaveFileActionEvent extends ActionEvent {
        private File _file;
        
        public SaveFileActionEvent(JFrame mainFrame, File file) {
            super(mainFrame, 0, (String)null);
            _file = file;
        }

        public File getFile() {
            return _file;
        }
        
    }


   

}
