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

package org.modelsphere.jack.srtool.reverse.file;

import java.awt.event.ActionEvent;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.plugins.*;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;

/**
 * This class provides common services for all file-based reverse engineering plug-ins. The
 * canReverse(File f) method returns true if a given plug-in is able to perform reverse engineering
 * on the file.
 * 
 */
public abstract class JackReverseEngineeringPlugin implements Plugin {

    // set by subclasses
    private static JackPositions g_positions = null;

    public JackReverseEngineeringPlugin() {
    }

    // Install the action in this frame menubar (optionnal)
    public final String installAction(DefaultMainFrame frame, MainFrameMenu menuManager) {
        return null;
    }

    public final Class[] getSupportedClasses() {
        return null;
    }

    public final boolean isSupportedObject(Object obj) {
        return true;
    }

    // public boolean isSupportedClass(Class claz);
    // public void execute(DbObject[] semObjs) throws DbException;

    // Execute the plugin for the supported classes returned by
    // getSupportedClasses().
    public final void execute(ActionEvent actEvent) throws Exception {
    }

    /**
     * Returns the name of the reverse plug-ins. When you select 'add files' in the reverse window,
     * we see a label named 'Files of type:' and a text area displaying the text: 'All files (*.*)'.
     * If you click on this text, you will see a list of all types of files that can be imported.
     * Among then, you should see the following type: 'Test files (*.test)'. The text 'Test files'
     * comes from the method getDisplayName(). If you change it, the title of the file type will be
     * changed
     */
    public abstract String getDisplayName();

    /**
     * Return the extension of the file type. Each time you create a reverse plug-in, you must
     * specify on which file type the reverse will be invoked. So, each time you select a file, the
     * application finds out which type of file is it, and choose the right reverse plug-in that
     * will be invoked for that type of file.
     */
    public abstract String getExtension();

    /**
     * This method is called each time you select a file to insure that this file is reverseable. It
     * can just verify the file extension. It can also verify the magic number, or the file
     * structure to be sure that a given file can be reversed.
     */
    public abstract boolean canReverse(File file) throws IOException;

    /**
     * Each kind of reverse must provide a kind of model on which the reversed object will be put in
     * (for example, objects reversed with the Java reverse module will be create in a Java model
     * package).
     */
    public abstract Class getKindOfModel();

    /**
     * This method returns the priority of a given reverse. Reverse with a higher priority will be
     * executed first. For example, we can define that the JavaReverse has a priority of 10 and
     * ClassReverse has a priority of 5 to reverse the .class files before the .java files.
     * 
     * 
     * @returns the priority (positive integer). '1' is the highest priority.
     * 
     */
    private int DEFAULT_PRIORITY = 10;

    public int getPriority() {
        return DEFAULT_PRIORITY;
    }

    public boolean canBeZipped() {
        return true;
    }

    // All reverses request graphical layout, by default
    public boolean requestLayout() {
        return true;
    }

    public Actions getActions() {
        return null;
    }

    // return null when no option are available for the reverse
    // override to return the option
    public ReverseParameters getRevParameters() {
        return null;
    }

    public static JackPositions getPositions() {
        return g_positions;
    }

    protected static void setPositions(JackPositions positions) {
        g_positions = positions;
    }

    protected Constructor getDefaultModelConstructor(Class modelClaz) throws NoSuchMethodException {
        Constructor constr = modelClaz.getConstructor(new Class[] { DbObject.class });
        return constr;
    }

    protected Object[] getDefaultConstructorObjects(DbProject proj) throws NoSuchMethodException,
            DbException {
        Object[] objs = new Object[] { proj };
        return objs;
    }

    protected long getActualtTaskSize(File file) {
        // by default, return the length of the file
        // we assume that a 10-K .java file takes the same time to reverse that
        // a 10-K .class file
        long len = file.length();

        return len;
    }

    public abstract DbObject createNewPackage(Class modelClaz, DbProject project)
            throws NoSuchMethodException, IllegalAccessException, InstantiationException,
            InvocationTargetException, DbException;

    public abstract String reverseFile(DbObject pack, StringWriter logBuffer,
            ReverseFileOptions options, File file) throws Exception;

    public void terminate() throws Exception {
    }

}
