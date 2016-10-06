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

package org.modelsphere.jack.plugins;

import java.awt.Component;

import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.awt.TextViewerDialog;
import org.modelsphere.jack.awt.TextViewerFrame;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.FocusManager;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.util.ExceptionHandler;

/**
 * 
 * <p>
 * This class is a facade containing methods to ease access to the main application features.
 * 
 * @see Plugin
 * 
 * @version 1 10/15/2000
 * @author Gino Pelletier
 * 
 */

public final class PluginServices {

    /**
     * <p>
     * Get the focus manager instance.
     * 
     * @return the FocusManager
     */
    public static FocusManager getFocusManager() {
        return ApplicationContext.getFocusManager();
    }

    /**
     * <p>
     * Get the DefaultMainFrame instance (main frame of the application).
     * 
     * @return the DefaultMainFrame (JFrame)
     */
    public static DefaultMainFrame getDefaultMainFrame() {
        return ApplicationContext.getDefaultMainFrame();
    }

    /**
     * <p>
     * Get the active project.
     * 
     * @return the active DbProject.
     */
    public static DbProject getCurrentProject() {
        return ApplicationContext.getFocusManager().getCurrentProject();
    }

    /**
     * <p>
     * Returns the database (Db) instance.
     * <p>
     * Note: The database instance object will be null if there is no active project or if the
     * context implies more than one Db instance (this may be the case for multiple selection in the
     * explorer). In that case, use PluginServices.multiDbBeginTrans() and
     * PluginServices.multiDbCommitTrans() or use the FocusManager methods.
     * 
     * @return the active Db object.
     * 
     * @see #multiDbBeginTrans
     * @see #multiDbCommitTrans
     */
    public static Db getCurrentDb() {
        DbObject project = ApplicationContext.getFocusManager().getCurrentProject();
        return (project == null) ? null : project.getDb();
    }

    /**
     * <p>
     * Open a transaction for all active projects.
     * <p>
     * If multi selection, this will ensure that a transaction is opened to access all selected
     * objects.
     * 
     * 
     * @param access
     *            type of transaction (Db.READ_TRANS, Db.WRITE_TRANS).
     * @param transname
     *            name of the transaction. This value can't be null for a Db.WRITE_TRANS type
     *            transaction.
     * 
     * @see #multiDbCommitTrans
     */
    public static void multiDbBeginTrans(int access, String transname) throws DbException {
        if ((access != Db.READ_TRANS) && (access != Db.WRITE_TRANS))
            throw new IllegalArgumentException("Invalid access value in multiDbBeginTrans. "); // NOT LOCALIZABLE Exception
        Db db = getCurrentDb();
        Object[] selObjects = getSelectedObjects();
        if (selObjects != null) {
            if (db == null)
                DbMultiTrans.beginTrans(access, selObjects, transname);
            else
                db.beginTrans(access, transname);
        }
    }

    /**
     * <p>
     * Close the transaction for all active projects. Use with multiDbBeginTrans.
     * <p>
     * If a multiple selection, this will ensure that a transactions is commited for each selected
     * objects.
     * 
     * 
     * @see #multiDbBeginTrans
     */
    public static void multiDbCommitTrans() throws DbException {
        Db db = getCurrentDb();
        Object[] selObjects = getSelectedObjects();
        if (selObjects != null) {
            if (db == null)
                DbMultiTrans.commitTrans(selObjects);
            else
                db.commitTrans();
        }
    }

    /**
     * <p>
     * Returns all selected objects (from active explorer, properties or diagram windows). May
     * contains graphical objects (graphic packages) and/or database objects (DbObject).
     * <p>
     * This method is equivalent to ApplicationContext.getFocusManager().getSelectedObjects().
     * 
     * @return an array of all the selected objects.
     * 
     * @see #getSelectedSemanticalObjects
     */
    public static Object[] getSelectedObjects() {
        return ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
    }

    /**
     * <p>
     * Returns the selected semantical objects (from active explorer, properties or diagram
     * windows).
     * <p>
     * If a graphical object is selected, the semantical DbObject associated with this graphical
     * object will be returned instead of the graphical object.
     * <p>
     * This method is equivalent to
     * ApplicationContext.getFocusManager().getSelectedSemanticalObjects().
     * 
     * @return an array of all the selected semantical objects.
     * 
     * @see #getSelectedObjects
     */
    public static DbObject[] getSelectedSemanticalObjects() {
        return ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
    }

    /**
     * <p>
     * Returns the active diagram.
     * <p>
     * This method is equivalent to ApplicationContext.getFocusManager().getActiveDiagram().
     * 
     * 
     * @return the active org.modelsphere.jack.srtool.graphic.ApplicationDiagram object. Can be null
     *         if no active diagram.
     * 
     */
    public static ApplicationDiagram getActiveDiagram() {
        return ApplicationContext.getFocusManager().getActiveDiagram();
    }

    /**
     * <p>
     * Get the properties set for long term persistence of execution parameters.
     * <p>
     * These properties are stored in a local .properties file.
     * <p>
     * This method is equivalent to PropertiesManager.PLUGINS_PROPERTIES_SET.
     * 
     * 
     * @return the org.modelsphere.jack.preference.PropertiesSet object.
     * 
     */
    public static PropertiesSet getPropertiesSet() {
        return PropertiesManager.PLUGINS_PROPERTIES_SET;
    }

    /**
     * <p>
     * Safely handles an exception and restores the application's context.
     * 
     * <p>
     * This method is equivalent to ExceptionHandler.processUncatchedException(comp, th).
     * 
     * 
     * @param comp
     *            the base component used to display the user message. If null, the Application main
     *            frame will be used.
     * @param th
     *            the Throwable object to handle.
     * 
     */
    public static void handleException(Component comp, Throwable th) {
        ExceptionHandler.processUncatchedException(comp, th);
    }

    /**
     * <p>
     * This method will create and display a modal JDialog containing a Text component.
     * 
     * <p>
     * Open, Save and Print operations are provided by this dialog.
     * <p>
     * Note: This method should not be called within an opened transaction.
     * 
     * <p>
     * This method is equivalent to
     * org.modelsphere.jack.awt.TextViewerDialog.showTextDialog(ApplicationContext
     * .getDefaultMainFrame(), title, text, useHTML).
     * 
     * @param title
     *            the title of the dialog.
     * @param text
     *            the text to display.
     * @param useHTML
     *            true if the text component should use html content type.
     * 
     */
    public static void showTextDialog(String title, String text, boolean useHTML) {
        TextViewerDialog.showTextDialog(ApplicationContext.getDefaultMainFrame(), title, text,
                useHTML);
    }

    /**
     * <p>
     * This method will create and display a JInternalFrame instance containing a Text component.
     * 
     * <p>
     * Open, Save and Print operations are provided by this internal frame.
     * 
     * 
     * @param title
     *            the title of the internal frame.
     * @param text
     *            the text to display.
     * @param useHTML
     *            true if the text component should use html content type.
     * 
     * @return the created org.modelsphere.jack.awt.TextViewerFrame instance.
     */
    public static TextViewerFrame showTextInternalFrame(String title, String text, boolean useHTML) {
        TextViewerFrame frame = new TextViewerFrame(title, text, useHTML);
        frame.showTextViewerFrame(ApplicationContext.getDefaultMainFrame().getJDesktopPane(),
                DefaultMainFrame.PROPERTY_LAYER);
        return frame;
    }

}
