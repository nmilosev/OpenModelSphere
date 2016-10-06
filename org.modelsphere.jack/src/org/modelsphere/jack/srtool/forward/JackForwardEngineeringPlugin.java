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

package org.modelsphere.jack.srtool.forward;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.io.PathFile;
import org.modelsphere.jack.plugins.Plugin;
import org.modelsphere.jack.plugins.PluginSelectionListener;
import org.modelsphere.jack.plugins.PluginServices;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.JarUtil;

/**
 * This class provides common services for all forward engineering plug-ins.
 * 
 */
public abstract class JackForwardEngineeringPlugin implements Plugin {

    public static final String PROP_ROOT_DIR = "RootDir"; //NOT LOCALIZABLE, property key

    public static final int UNKNOWCOMPLEXITY = 1;

    // Private variables
    protected ForwardOutput g_forwardOutput = null;
    private Writer writer = null;
    private ForwardTask forwardTask = null;
    private static final String SEPARATOR = System.getProperty("file.separator");
    private static final String FILE_NOT_FOUND_PATTERN = LocaleMgr.message
            .getString("PLUGIN_FILE_NOT_FOUND");

    private static final String TEMPLATE_DIR = "templates"; //NOT LOCALIZABLE
    private static final String TEMPLATE_PATH = System.getProperty("user.dir") + SEPARATOR
            + TEMPLATE_DIR;
    
    //to be used by subclasses to initialize
    protected static URL tryToFind(Class subclass, String tplFile) {
    	//If a forward engineering module cannot find its .tpl file,
        //then abort initialization
        URL url = subclass.getResource(tplFile);
        return url;
    }

    /*
    //to be used by subclasses to initialize
    protected static String tryToFind(Class subclass, String tplFile) {
        //If a forward engineering module cannot find its .tpl file,
        //then abort initialization
        URL url = subclass.getResource(tplFile);

        if (url == null) {
            String msg = MessageFormat.format(FILE_NOT_FOUND_PATTERN, new Object[] {
                    subclass.toString(), url });
            throw new RuntimeException(msg);
        } //end if
        File tempFile = null;
        try {
            tempFile = JarUtil.createTemporaryFile(url.toString());
        } catch (Exception e) {
        }
        if (tempFile == null) { // not a jar file
            String retVal = null;
            try {
                retVal = URLDecoder.decode(url.getFile(), "UTF-8");
            } catch (Exception e) {
            }
            return retVal;
        }
        return tempFile.getAbsolutePath();
    }
    */
    
    public PropertyScreenPreviewInfo getPropertyScreenPreviewInfo() {
        return null;
    }

    public String installAction(DefaultMainFrame frame, MainFrameMenu menuManager) {
        return null;
    }

    public abstract void setOutputToASCIIFormat();

    public GenerateInFileInfo getGenerateInFileInfo() {
        return null;
    }

    public final String getTemplateDirectory() {
        return TEMPLATE_PATH;
    }

    //Tells whether 'claz' is supported by the forward module
    public boolean isSupportedClass(Class claz) {
        boolean supported = false;

        Class[] classes = getSupportedClasses();
        for (int i = 0; i < classes.length; i++) {
            if (classes[i].isAssignableFrom(claz)) {
                supported = true;
                break;
            } //end if
        } //end for

        return supported;
    } //end isSupportedClass()

    //
    // Get the rule which is the entry point for a given type of semantical
    // object.
    //
    //context = GENERATE_PREVIEW | GENERATE_FILE
    public Rule getRuleOf(DbObject so, int context) throws DbException {
        return null;
    }

    public static final int GENERATE_PREVIEW = 0;
    public static final int GENERATE_FILE = 1;

    public Rule getRuleOf(DbObject so) throws DbException {
        Rule rule = getRuleOf(so, GENERATE_PREVIEW);
        return rule;
    }

    //
    // Returns a value for incrementation of progressBar.
    //
    public int getComplexity(DbObject root) throws DbException {
        return UNKNOWCOMPLEXITY;
    }

    // Tell if the forward transaction is READ
    // override to change to false
    //
    protected boolean isReadOnly() {
        return true;
    }

    //
    //  Create output stream with filename
    //
    public ForwardOutput getForwardOutput(String filename) throws IOException {

        if (g_forwardOutput == null) {
            FileWriter output = null;

            //if file already exists, don't overwrite it, but rather backup it
            backupFile(filename);

            try {
                output = new FileWriter(filename);
            } catch (FileNotFoundException ex) {
                throw new org.modelsphere.jack.io.WriteAccessException(filename);
            }

            g_forwardOutput = new ForwardOutput(this, output, null);
        }

        return g_forwardOutput;
    }

    public void clearForwardOutput() {
        g_forwardOutput = null;
    }

    public void backupFile(File file) {
        if (file.exists()) {
            File directory = file.getParentFile();
            String filename = file.getName();
            File backupFile = new File(directory, filename + PathFile.BACKUP_EXTENSION);
            backupFile.delete();
            file.renameTo(backupFile);
        }
    }

    public void backupFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            File backupFile = new File(filename + PathFile.BACKUP_EXTENSION);
            backupFile.delete();
            file.renameTo(backupFile);
        }
    }

    // Each subclass of panel is responsible to create & return
    // the good kind of Writer used by forward panel.
    //
    public Writer createNewPanelWriter(boolean isHTMLformat) {
        return new StringWriter(); //default writer for forward panel, in plain text
    }

    public void setWriter(Writer aWriter) {
        writer = aWriter;
    }

    public Writer getWriter() {
        return writer;
    }

    public final ForwardTask getForwardTask() {
        return forwardTask;
    }

    protected String getForwardDirectory() {
        String defDir = ApplicationContext.getDefaultWorkingDirectory();
        return defDir;
    }

    public void execute(ActionEvent actEvent) throws Exception {
        DbObject[] semObjs = PluginServices.getSelectedSemanticalObjects();
        if ((semObjs == null) || (semObjs.length == 0)) {
            ApplicationDiagram diag = PluginServices.getActiveDiagram();
            if (diag != null) {
                semObjs = new DbObject[] { diag.getDiagramGO() };
            }
        } //end if

        execute(semObjs);
    }

    public void execute(DbObject[] semObjs) throws DbException {

        GenerateInFileInfo fileInfo = getGenerateInFileInfo();
        if (fileInfo != null) {
            boolean badRoot = true;
            String rootDir = getRootDirFromUserProp();
            String defDir = getForwardDirectory();
            File dir = new File(defDir);

            if (dir != null) {
                rootDir = dir.getAbsolutePath();
            }

            if (rootDir.length() != 0) {
                File dirFile = new File(rootDir);
                dirFile.mkdirs();
                badRoot = !dirFile.isDirectory();
            }

            if (badRoot) {
                JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(),
                        LocaleMgr.message.getString("forwardRootDirNotSpecified"), "",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        } //end if

        ForwardOptions options = createForwardOptions(semObjs);
        if (options != null) {
            ForwardToolkitInterface toolkit = getToolkit();
            toolkit.generateFile(options);
        }
    } //end execute()

    protected ForwardOptions createForwardOptions(DbObject[] semObjs) {
        return null;
    } //by default

    protected ForwardToolkitInterface getToolkit() {
        return null;
    } //by default

    public String getRootDirFromUserProp() {
        PropertiesSet appSet = PropertiesManager.APPLICATION_PROPERTIES_SET;
        return appSet.getPropertyString(JackForwardEngineeringPlugin.class, PROP_ROOT_DIR, "");
    }

    protected final DbObject[] cleanRedundantForward(DbObject[] semObjs) throws DbException {
        ArrayList tempVector = new ArrayList();
        for (int c = 0; c < semObjs.length; c++) {
            DbObject semObj = semObjs[c];
            boolean accept = true;
            int maxI = semObjs.length - 1;
            for (int i = 0; i < maxI; i++) {
                if (semObj.isDescendingFrom(semObjs[i])) {
                    accept = false;
                    break;
                }
            }
            if (accept)
                tempVector.add(semObj);
        }
        return (DbObject[]) tempVector.toArray(new DbObject[tempVector.size()]);
    }

    // For internal use only (called by ForwardTask)
    protected abstract void forwardTo(DbObject semObj, ArrayList generatedFiles)
            throws DbException, IOException, RuleException;

    public String getFeedBackMessage(ArrayList generatedList) {
        String message;
        int nbForwards = generatedList.size();

        //No files forwarded
        if (nbForwards == 0) {
            message = LocaleMgr.message.getString("noFileForwarded");
        } else if (nbForwards == 1) {
            String pattern = LocaleMgr.message.getString("oneFileGenerated");
            String filename = convertSeparators((String) generatedList.get(0));
            message = MessageFormat.format(pattern, new Object[] { filename });
        } else {
            String rootDir = getRootDirFromUserProp();
            String pattern = LocaleMgr.message.getString("nFilesForwardedIn");
            message = MessageFormat.format(pattern,
                    new Object[] { new Integer(nbForwards), rootDir });
        }

        return message;
    }

    public static String convertSeparators(String fileName) {
        String sep = System.getProperty("file.separator");

        if (sep.equals("\\"))
            fileName = fileName.replace('/', sep.charAt(0));
        else if (sep.equals("/"))
            fileName = fileName.replace('\\', sep.charAt(0));

        return fileName;
    }
    
    public boolean doListenSelection() { return (this instanceof PluginSelectionListener); }

} //end Forward
