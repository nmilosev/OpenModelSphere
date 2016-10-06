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

import javax.swing.Icon;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.JackForwardEngineeringPlugin;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.util.ExceptionHandler;

/**
 * 
 * <p>
 * This class is the basic class for all plugins actions.
 * 
 * <p>
 * This is the default Action class for the plugin package.
 * 
 * <p>
 * This class ultimately inherits from javax.swing.Action.
 * 
 * <p>
 * Added to a menu or a toolbar, it will result in a JMenuItem or JButton component in the
 * component.
 * 
 * <p>
 * This class implements org.modelsphere.jack.actions.SelectionActionListener. Any changes in the
 * selection will produce a call to updateSelectionAction(). The default behavior is to enable the
 * action if the selection corresponds to the plugin supported classes. If supported classes are
 * provided and at least one object isn't assignable to one of these classes, then the action will
 * be disabled. If there are no specified supported classes, the action will remain enabled.
 * 
 * 
 * @see javax.swing.Action
 * @see javax.swing.JMenu
 * @see javax.swing.JToolbar
 * @see Plugin
 * 
 * @version 1 10/15/2000
 * @author Gino Pelletier
 */

public class PluginAction extends AbstractApplicationAction implements SelectionActionListener {
    private static final String UNKNOWN = LocaleMgr.misc.getString("Unknown");

    private Plugin plugin;

    //private ;

    private PluginAction() {
        super(" ");
    }

    /**
     * <p>
     * Create a PluginAction for the specified Plugin object.
     * 
     * @param plugin
     *            the plugin associated with the PluginAction. Cannot be null.
     * @param name
     *            the action's name. If null, the plugin signature will be used.
     * @param mnemonic
     *            the action's mnemonic.
     * 
     */
    public PluginAction(Plugin plugin, String name, Integer mnemonic, Icon icon) {
        super(" ");

        if (plugin == null)
            throw new NullPointerException();

        if (name == null && plugin instanceof JackForwardEngineeringPlugin) {
            JackForwardEngineeringPlugin fwd = (JackForwardEngineeringPlugin) plugin;
            if (fwd.getGenerateInFileInfo() != null) {
                name = fwd.getGenerateInFileInfo().getPopupItemTitle();
                mnemonic = fwd.getGenerateInFileInfo().getPopupItemMnemonic();
            }
        }

        if (name == null || name.length() == 0) {
            PluginDescriptor descriptor = PluginMgr.getSingleInstance().getPluginsRegistry()
                    .getPluginInfo(plugin);
            if (descriptor != null)
                name = descriptor.getName();
        }

        if (name == null || name.length() == 0) {
            PluginSignature signature = plugin.getSignature();
            if (signature != null)
                name = plugin.getSignature().getName();
            else
                name = plugin.getClass().getName();
        }

        if (name == null || name.length() == 0) {
            name = UNKNOWN;
        }

        putValue(NAME, name);
        if (mnemonic != null)
            this.setMnemonicInt(mnemonic);
        if (icon != null)
            this.setIcon(icon);
        this.plugin = plugin;

        setEnabled(false);
    }

    /**
     * <p>
     * Create a PluginAction for the specified Plugin object.
     * 
     * @param plugin
     *            the plugin associated with the PluginAction. Cannot be null.
     * @param name
     *            the action's name. If null, the plugin signature will be used.
     * @param mnemonic
     *            the action's mnemonic.
     * 
     */
    public PluginAction(Plugin plugin, String name, Integer mnemonic) {
        this(plugin, name, null, null);
    }

    /**
     * <p>
     * Create a PluginAction for the specified Plugin object.
     * 
     * @param plugin
     *            the plugin associated with the PluginAction. Cannot be null.
     * @param name
     *            the action's name. If null, the plugin signature will be used.
     * 
     */
    public PluginAction(Plugin plugin, String name) {
        this(plugin, name, null, null);
    }

    /**
     * <p>
     * Create a PluginAction for the specified Plugin object.
     * 
     * @param plugin
     *            the plugin associated with the PluginAction. Cannot be null.
     * 
     */
    public PluginAction(Plugin plugin) {
        this(plugin, null, null, null);
    }

    /**
     * <p>
     * This method is triggered when an action is performed on this action component. It results in
     * a call to the execute() method of the associated plugin.
     * 
     */
    protected final void doActionPerformed(ActionEvent ev) {
        // Secure the execution of this plugin for any Exception that may occur.
        try {
            plugin.execute(ev);
        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
        }
    }

    /**
     * <p>
     * Update the action (thus all the swing components created with this PluginAction) according to
     * a modification on the selected objects. The default behavior is to enable the action if the
     * selection correspond to this plugin supported classes. If no supported classes are specified,
     * it will stay enabled.
     * 
     * <p>
     * Subclasses should override this method to define a different behavior.
     * 
     * <p>
     * This method is invoked within a read transaction for all projects in the scope of the active
     * selection. You may access database objects (DbObject) without managing transactions.
     * 
     * <p>
     * Note: This method is called quite often and could result in lost of performance. Overriding
     * classes should keep this method as fast as possible.
     * 
     */
    public void updateSelectionAction() throws DbException {
    	boolean listenSelection; 
    	
    	try { 
    		listenSelection = plugin.doListenSelection(); 
    	} catch (AbstractMethodError er) {
    		//catch error for backward-compatibility
    		listenSelection = (plugin instanceof PluginSelectionListener); 
    	}
    	
        if (listenSelection) {
            try {
            	PluginSelectionListener listener = (PluginSelectionListener)plugin;
            	boolean enabled = listener.selectionChanged();
                setEnabled(enabled);
                
            	if (plugin instanceof PluginSelectionListener2) {
            		PluginSelectionListener2 listener2 = (PluginSelectionListener2)plugin;
            		listener2.onSelectionChanged(this);
            	}
            } catch (Exception e) {
                setEnabled(false);
            }
            return;
        }

        //if no classes supported in particular, enable the action all the time 
        Class[] supportedclasses = plugin.getSupportedClasses();
        if (supportedclasses == null || supportedclasses.length == 0) {
            setEnabled(true);
            return;
        }

        //if active diagram is among the supported classes, enable the action
        ApplicationDiagram diag = PluginServices.getActiveDiagram();
        if (diag != null) {
            DbObject diagGo = diag.getDiagramGO();
            Class claz = diagGo.getClass();
            if (isSupportedClass(supportedclasses, claz)) {
                setEnabled(true);
                if (claz.getName().equals("org.modelsphere.sms.be.db.DbBEDiagram")) {
                    if (TerminologyUtil.getInstance().isUML(diagGo))
                        setEnabled(false);
                }
                return;
            }
        }

        DbObject[] selectedObjs = PluginServices.getSelectedSemanticalObjects();
        for (int i = 0; i < selectedObjs.length; i++) {
            if (TerminologyUtil.getInstance().isUML(selectedObjs[i])) {
                setEnabled(false);
                return;
            }
        }

        //if no object selected, disable the action
        Object[] selobjs = PluginServices.getSelectedObjects();
        if (selobjs == null || selobjs.length == 0) {
            setEnabled(false);
            return;
        }

        //if among the selected object, one is not supported, then disable the action
        boolean enable = true;
        for (int i = 0; i < selobjs.length; i++) {
            Object obj = selobjs[i];
            if (!isSupportedClass(supportedclasses, obj.getClass())) {
                enable = false;
                break;
            }
        } //end for
        setEnabled(enable);
    } //end updateSelectionAction()

    private boolean isSupportedClass(Class[] supclasses, Class c) {
        boolean supported = false;
        for (int i = 0; i < supclasses.length; i++) {
            if (supclasses[i].isAssignableFrom(c)) {
                supported = true;
                break;
            }
        }
        return supported;
    }

    final Plugin getPlugin() {
        return plugin;
    }

}
