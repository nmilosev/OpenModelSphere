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

import java.awt.event.ActionEvent;

import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;

/**
 * Used for reverse and forward engineering, JDBC driver plug-ins and other features. It is the
 * common interface for adding functionalities to the application.
 * <p>
 * All Plugin classes should be considered as a singleton. Only one instance exists for each class
 * implementing Plugin.
 * <p>
 * All Plugin classes must be declared public and must be instanciable (not abstract) in order to be
 * loaded by the application.
 * <p>
 * Plugins are instantiated using the default public constructor of the class. Any other
 * constructors will be ignored.
 * 
 *<p>
 * The use of static initialisation code should be avoided. There is no guarantee regarding the
 * application's states during class loading and this could lead to undesired behaviors under
 * different configurations or versions. Initialisation should be performed during instantiation or
 * execution.
 * <p>
 * You should also be aware that the same instance of the plugin can be executed more than once
 * during the application lifetime. Therefore, the plugin must ensure that all useless references
 * are set to null to allow garbage collection. Also, the plugin should ensure that its state is
 * correct before returning from the execute() method.
 * <p>
 * If a plugin needs to stay alive (for examples using a non model report frame or dialog with hyper
 * link listener), we recommend that the execute() method delegate the execution to a new instance
 * object of another class for each call to execute(). Thus avoiding potential conflicts with other
 * calls to execute().
 * <p>
 * We recommend using a different package for each plugin. We also recommend using the Sun naming
 * convention for packages and to apply the Class' complete signature to the distribution zip file.
 * 
 * <p>
 * Note: Information for each plugin is provided within the application (Help Menu - Plugins Manager
 * - Details). This dialog provides informations contained in the plugin signature, location of the
 * plugin and any exceptions or errors that may have occurred at load time or instantiation.
 * 
 * 
 * @see PluginServices
 * @see PluginSignature
 * @see PluginAction
 * 
 * @author Gino Pelletier
 */

public interface Plugin {
    public static final String MENU_FILE = MainFrameMenu.MENU_FILE;
    public static final String MENU_EDIT = MainFrameMenu.MENU_EDIT;
    public static final String MENU_DISPLAY = MainFrameMenu.MENU_DISPLAY;
    public static final String MENU_FORMAT = MainFrameMenu.MENU_FORMAT;
    public static final String MENU_TOOLS = MainFrameMenu.MENU_TOOLS;
    public static final String MENU_HELP = MainFrameMenu.MENU_HELP;
    public static final String MENU_DEBUG = MainFrameMenu.MENU_DEBUG;

    /**
     * <p>
     * Return a non null signature for internal management of the plugin.
     * <p>
     * Note that the plugin's name visible in the application is the name within the
     * PluginSignature.
     * 
     * @see PluginSignature
     * @deprecated - As of 3.1, it is now strongly recommended to specify the signature in a
     *             'plugin.xml' file provided with the plugin. If the signature file is provided,
     *             the signature provided by this method is ignored and should be null.
     */
    public PluginSignature getSignature();

    /**
     * <p>
     * Install a javax.swing.Action in this frame menubar.
     * <p>
     * You can either:
     * <ul>
     * <li>- Manually install your plugin using the frame and menuManager;</li>
     * <li>- Or return a String representing the target menu (this will automatically install a
     * javax.swing.Action at a specific location in the menu represented by the key.</li>
     * </ul>
     * <p>
     * If you decide to manually add an action, we recommend using the class PluginAction.
     * 
     * @return a String identifying the Menu (MENU_FILE, MENU_EDIT, MENU_DISPLAY, ...). The returned
     *         value can be null.
     * @see PluginAction
     */
    public String installAction(DefaultMainFrame frame, MainFrameMenu menuManager);

    /**
     * <p>
     * Return an array of classes supported by this plugin.
     * <p>
     * A menu item will be added on the popup menu associated to these objects. On action performed,
     * the execute() method will be invoked.
     * 
     * 
     * @return an array representing the supported classes. The returned value can be null.
     * @see PluginSignature
     */
    public Class<? extends Object>[] getSupportedClasses();

    /**
     * <p>
     * Execute the plugin.
     * <p>
     * There should be no opened transaction when this method is invoked. The plugin developer must
     * manage all transactions for for accessing any database object (DbObject). The class
     * PluginServices offers some services related to transactions.
     * <p>
     * Note: Any exceptions that can't be handled properly by the Plugin should not be enclosed
     * within a try-catch statement. The application will take care of the Exception thrown by this
     * method and restore a valid application state.
     * 
     * 
     * @see PluginServices
     */
    public void execute(ActionEvent ev) throws Exception;
    
    public boolean doListenSelection();
}
