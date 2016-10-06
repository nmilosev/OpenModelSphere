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

package org.modelsphere.jack.script;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.international.LocaleMgr;
import org.python.core.*;
import org.python.util.PythonInterpreter;

public final class PythonShell extends Shell {
    private static final String kTitle = LocaleMgr.screen.getString("PythonShell"); // NOT LOCALIZABLE
    private static final String kPromptTitle = "PYTHON"; // NOT LOCALIZABLE, language name
    private static final String kExt = "py"; // NOT LOCALIZABLE, file extension
    private static final String kFileDesc = LocaleMgr.screen.getString("PythonScripts"); // NOT LOCALIZABLE

    private static PythonInterpreter jpythonInterp = null;
    private Shell.ShellInitializer shellInitializer = new Shell.ShellInitializer(kTitle,
            kPromptTitle + " {0} > ", kExt, kFileDesc);

    // public static String jPythonHomeDir = new File(new File(new File(new
    // File(new File(new
    // File(PythonShell.class.getResource(".").getFile()).getParent()).getParent()).getParent()).getParent()).getParent()).getAbsolutePath();//PythonShell.class.getResource("../../../..").getFile();
    public static String jPythonHomeDir = new File(System.getProperty("user.dir"))
            .getAbsolutePath();// NOT LOCALIZABLE
    // //PythonShell.class.getResource("../../../..").getFile();

    public static String libDir;

    static {
        System.setProperty("python.home", jPythonHomeDir); // NOT LOCALIZABLE
        File appHomeDir = new File(System.getProperty("user.dir"));// NOT LOCALIZABLE
        File libraryDir = new File(appHomeDir, "lib"); // NOT LOCALIZABLE, directory name
        libDir = libraryDir.getAbsolutePath() + System.getProperty("file.separator");
    }

    public PythonShell() {
        super();
        super.init(shellInitializer);
        jbInit();
    }

    private void jbInit() {
        // Setup the basic python system state from these options
        Properties properties = new Properties();
        String[] argv = new String[] {};
        PySystemState.initialize(System.getProperties(), properties, argv);

        jpythonInterp = new PythonInterpreter(null);
        addDirectoryToSystemPath(jpythonInterp, convertSeparators(libDir));
        jpythonInterp.setOut(buffer);
    }

    // Execute and get results
    public String execute(String command) throws ShellException {
        String result = null;

        try {
            buffer.reset();
            jpythonInterp.exec(command);
            result = buffer.toString();
        } catch (PyException ex) {
            throw new PythonShellException(ex.toString());
        }

        return result;
    }

    // ************************************************************************
    // //
    // add a directory to the path (module sys)
    // - usefull when launching a script that imports module in the same
    // directory
    // - the directory of the script is not necessarily the current directory
    // note: the directory is not added if it is already in the path
    // ************************************************************************
    // //
    public static void addDirectoryToSystemPath(PythonInterpreter interpreter, String dir) {
        // dir contains both path and file name so we have to chop it
        if (dir.indexOf("\\\\") > -1) // Windows //NOT LOCALIZABLE
            dir = dir.substring(0, dir.lastIndexOf("\\\\")); // NOT LOCALIZABLE
        else
            dir = dir.substring(0, dir.lastIndexOf("/")); // NOT LOCALIZABLE

        // javax.swing.JOptionPane.showMessageDialog(null, dir);
        // the following hard coded strings cannot be LOCALIZED
        interpreter.exec("import sys"); // NOT LOCALIZABLE
        PyObject returnObject = interpreter.eval("sys.path.count(\"" + dir + "\")"); // NOT LOCALIZABLE
        // javax.swing.JOptionPane.showMessageDialog(null, returnObject);
        PyInteger pyInteger = (PyInteger) returnObject;
        // javax.swing.JOptionPane.showMessageDialog(null, new
        // Integer(pyInteger.getValue()));
        if (pyInteger.getValue() == 0)
            interpreter.exec("sys.path.append(\"" + dir + "\")"); // NOT LOCALIZABLE
        // javax.swing.JOptionPane.showMessageDialog(null,
        // "sys.path.append(\""+dir+"\")"); //NOT LOCALIZABLE
    }

    // Overrides Shell for performance purposes.
    protected void executeFile(File file) {
        final String kExecFile = "execfile"; // NOT LOCALIZABLE, python statement
        String filename = file.getAbsolutePath();
        String pythonFileName = "";
        // addDirectoryToSystemPath(file.getParent()); //+

        pythonFileName = convertSeparators(filename);
        addDirectoryToSystemPath(jpythonInterp, pythonFileName);

        String command = kExecFile + "('" + pythonFileName + "')"; // NOT LOCALIZABLE
        commandLine.append(command);
        resultArea.append(commandLine.getPrompt() + command + NEWLINE);
        buffer.reset();
        String result = null;
        try {
            jpythonInterp.execfile(filename);
            result = buffer.toString();
        } catch (PyException ex) {
            result = ex.toString();
        }

        commandLine.addHistory(command);
        commandLine.refresh();
        resultArea.append(result);
    }

    public static String convertSeparators(String fileName) {
        String separator = "\\\\"; // Note: this separator should not be
        // replaced on UNIX //NOT LOCALIZABLE
        String resultFileName = ""; // NOT LOCALIZABLE

        // if it uses '\' as separator (Windows), replace '\' by '\\',
        // othersise (UNIX), stay unchanged.
        StringTokenizer st = new StringTokenizer(fileName, "\\"); // NOT LOCALIZABLE
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            resultFileName = resultFileName + token;
            if (st.hasMoreTokens())
                resultFileName = resultFileName + separator;
        }

        return resultFileName;
    }

    /*
     * public static DbSMSProject getProject(org.modelsphere.jack.baseDb.db.Db db) throws
     * org.modelsphere.jack.baseDb.db.DbException { DbSMSProject proj = new
     * DbSMSProject(db.getRoot()); return proj; }
     */

    // INNER CLASS
    protected static class PythonShellException extends Shell.ShellException {
        PythonShellException(String msg) {
            super(msg);
        }
    }

    // *************
    // DEMO FUNCTION
    // *************

    private static void runDemo() throws IOException {
        try {
            // org.modelsphere.jack.baseDb.db.Db db = new
            // org.modelsphere.jack.baseDb.db.DbRAM();
            // db.beginTrans(org.modelsphere.jack.baseDb.db.Db.WRITE_TRANS);

            // DbSMSProject proj = getProject(db);

            PythonShell scriptShell = new PythonShell();
            scriptShell.setSize(600, 400);
            scriptShell.setVisible(true);
            // scriptShell.setVisible(true);

        } catch (Exception ex) {
            Debug.trace("Exception : " + ex.toString()); // NOT LOCALIZABLE
        }

        byte[] buf = new byte[256];
        System.in.read(buf, 0, 256);
    } // end runDemo()

    /*
     * public static final void main(String[] args) throws IOException { runDemo(); } //end main()
     */
} // end PythonShell
