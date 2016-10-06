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

package org.modelsphere.sms.features.reverse;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Date;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.srtool.reverse.file.JackReverseEngineeringPlugin;
import org.modelsphere.jack.srtool.reverse.file.JackPositions;
import org.modelsphere.jack.srtool.reverse.file.ReverseFileOptions;
import org.modelsphere.jack.srtool.reverse.file.ReverseParameters;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.international.LocaleMgr;

/**
 * 
 * The common abstract class for reverse engineering plug-ins.
 * 
 */
public abstract class AbstractReverseEngineeringPlugin extends JackReverseEngineeringPlugin {
    private static final String TRANSACTION_PATTERN = LocaleMgr.message.getString("0Creation");
    private static final String DEFAULT_CLASS_MODEL_NAME = LocaleMgr.message
            .getString("ReverseEngineeredClassModel");
    private static final String REVERSED_AT_PTN = LocaleMgr.message
            .getString("ReverseEngineeredAt0");

    // set by subclasses
    private static JackPositions g_positions = null;

    public AbstractReverseEngineeringPlugin() {
    }

    /**
     * Returns the name of the reverse plugins. When you select 'add files' in the reverse window,
     * we see a label named 'Files of type:' and a text area displaying the text: 'All files (*.*)'.
     * If you click on this text, you will see a list of all types of files that can be imported.
     * Among then, you should see the following type: 'Test files (*.test)'. The text 'Test files'
     * comes from the method getDisplayName(). If you change it, the title of the file type will be
     * changed
     */
    public abstract String getDisplayName();

    /**
     * Return the extension of the file type. Each time you create a reverse plugin, you must
     * specify on which file type the reverse will be invoked. So, each time you select a file,
     * ModelSphere finds out which type of file is it, and choose the right reverse plugin that will
     * be invoked for that type of file.
     */
    public abstract String getExtension();

    /**
     * This method is called each time you select a file to insure that this file is reverseable. It
     * can just verify the file entension. It can also verify the magic number, or the file
     * structure to be sure that a given file can be reversed.
     * 
     * By default, returns false to avoid that a plug-in says is able to reverse a file that in fact
     * is not able to do so.
     */
    public boolean canReverse(File file) throws IOException {
        return false;
    }

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

    /**
     * This method is called on each file to be reversed.
     * 
     * @param file
     *            file to reverse (*.java, *.class, *.srx, etc.)
     * @param dboDestination
     *            model's entry point where model objects will be created
     * @param objectsToDeleteVector
     *            model objects to be deleted after calling reverseFile()
     * @param logBuffer
     *            log of error messages displayed if reverseFile() is errorneous
     * @param task
     *            each implementation is responsible of incrementation of the task's progress bar
     * 
     * @returns null if successful, and an error message if errorneous
     * 
     */
    public abstract String reverseFile(DbObject targetPack, StringWriter logBuffer,
            ReverseFileOptions options) throws Exception;

    // Note: this function is called by the previous one, and it is
    // also called in the case of an entry in a zipped file.
    public String reverseFile(DbObject targetPack, StringWriter logBuffer,
            ReverseFileOptions options, File file) throws Exception {
        options.setInput(file);
        String errMsg = reverseFile(targetPack, logBuffer, options);
        options.closeInput();

        return errMsg;
    }

    public boolean canBeZipped() {
        return true;
    }

    // All reverses request graphical layout, by default
    public boolean requestLayout() {
        return true;
    }

    public org.modelsphere.jack.srtool.reverse.file.Actions getActions() {
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

    // How to create a package under this modelClaz
    // For instance, if modelClaz=DbJVClassModel, create a class model
    // under project 'proj'.
    public DbObject createNewPackage(Class modelClaz, DbProject proj) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, InvocationTargetException, DbException {
        DbSMSPackage pack;
        Constructor constr = getDefaultModelConstructor(modelClaz);
        Db db = proj.getDb();
        String newPackageName = DEFAULT_CLASS_MODEL_NAME;
        String transactionName = MessageFormat.format(TRANSACTION_PATTERN,
                new Object[] { newPackageName });
        db.beginTrans(Db.WRITE_TRANS, transactionName);
        Object[] objs = getDefaultConstructorObjects(proj);
        pack = (DbSMSPackage) constr.newInstance(objs);
        pack.setName(newPackageName);
        String description = MessageFormat.format(REVERSED_AT_PTN, new Object[] { new Date() });
        pack.setDescription(description);
        db.commitTrans();
        return pack;
    }

    public void terminate() throws Exception {
    }
}
