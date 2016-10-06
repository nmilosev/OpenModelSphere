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

package org.modelsphere.sms.or.features.dbms;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.gui.task.DefaultController;
import org.modelsphere.jack.gui.wizard.WizardPage;
import org.modelsphere.jack.plugins.*;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;
import org.modelsphere.jack.srtool.forward.ForwardOptions;
import org.modelsphere.jack.srtool.forward.ForwardWorker;
import org.modelsphere.sms.db.DbSMSAbstractPackage;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.templates.SQLForwardEngineeringPlugin;

public abstract class ForwardToolkitPlugin implements Plugin2 {
    public static final String DEFAULT_LOG_FILENAME = ApplicationContext.getLogPath()
            + System.getProperty("file.separator") + "forward.log"; //NOT LOCALIZABLE
    public static final String ForwardEngineering = LocaleMgr.screen
            .getString("ForwardEngineering");

    // The default toolkit is considered as a Null Object.
    // Getting the toolkit doesn't need a (toolkit != null) verification.
    private static ForwardToolkitPlugin defaultToolkit = new DefaultForwardToolkit();

    // Field to identify the Toolkit to use.
    private static int activeDiagramTargetId = -1;

    // Optimization purposes
    private static ForwardToolkitPlugin lastKit = null;

    /**
     * A Default Class for Toolkit. The instance of this class represent a null toolkit that will be
     * returned from a call to getToolkit when no toolkit is applicable. Default or Generic behovior
     * should be implemented in this class
     */
    private static final class DefaultForwardToolkit extends ForwardToolkitPlugin {
        private PluginSignature signature;

        public PluginSignature getSignature() {
            if (signature == null) {
                signature = new PluginSignature(getTitle(), "$Revision: 4 $",
                        "$Date: 4/12/06 1:44p $", 150);
            }
            return signature;
        }

        protected ForwardWorker createForwardWorker(ForwardOptions options) {
            ForwardWorker worker = new DBMSForwardWorker((DBMSForwardOptions) options);
            return worker;
        } //end createForwardWorker()

        public Class getForwardClass() {
            return null;
        }

        public boolean forwardEnabled() {
            return false;
        }

    }

    private class KitTargetMapping {
        int targetId;
        ForwardToolkitPlugin kit;

        KitTargetMapping(int id, ForwardToolkitPlugin aKit) {
            targetId = id;
            kit = aKit;
        }
    }

    //Once created, the object stores itself in g_toolkitsTarget
    protected ForwardToolkitPlugin() {
    } //end ForwardToolkit()
    
    // Toolkits - DBMS name mapping
    private static List g_toolkitsTarget = null;
    private static List getToolkitTargets() {
    	if (g_toolkitsTarget == null) {
    		//init toolkit
    		g_toolkitsTarget = new ArrayList();
    		PluginMgr mgr = PluginMgr.getSingleInstance();
    		PluginsRegistry registry = mgr.getPluginsRegistry(); 
    		List<PluginDescriptor> allpluginDescriptors = registry.getAllPlugins();
    		
    		//for each declared plug-in
    		for (PluginDescriptor pluginDescriptor : allpluginDescriptors) {
    			
    			if (mgr.isValid(pluginDescriptor)) {
    				Class<?> claz = pluginDescriptor.getPluginClass();
    		
    				//if it's a toolkit
    				if (ForwardToolkitPlugin.class.isAssignableFrom(claz))  {
    					ForwardToolkitPlugin plugin = (ForwardToolkitPlugin)createInstance(claz);
    					if (plugin != null) {
    						plugin.init2();
    					}
    				} else if (SQLForwardEngineeringPlugin.class.isAssignableFrom(claz))  { //if it's a f/w eng 
    					SQLForwardEngineeringPlugin plugin = (SQLForwardEngineeringPlugin)createInstance(claz);
    					if (plugin != null) {
    						registry.getActivePluginInstances(null);
    					}
    				} //end if
    		    } //end if
    		} //end for
    		
    		
    	    //PluginDescriptor pluginDescriptor = registry.getPluginInfo(this);
    		
    	} //end if
    	
    	return g_toolkitsTarget;
    }
    
	private static Plugin2 createInstance(Class<?> claz) {
    	
		Plugin2 plugin; 
    	
    	try {
    		plugin = (Plugin2)claz.newInstance();
    	} catch (InstantiationException ex) {
    		plugin = null;
    	} catch (IllegalAccessException e) {
    		plugin = null;
		}
    	
		return plugin;
	}

    private void init2() {
    	int[] targetIDs = getSupportedTargetIds();
		for (int targetID : targetIDs) {
			int elemNum = findMatchingAnsiTargetId(targetID);
			String newToolkit = this.getClass().getName();
			if ((elemNum >= 0) && (!(newToolkit.contains("ansi")))) {
				g_toolkitsTarget.remove(elemNum);
			}
			
			KitTargetMapping mapping = new KitTargetMapping(targetID, this);
			g_toolkitsTarget.add(mapping);
		}
		//
//		
//		boolean assignable2 = claz.isAssignableFrom(ForwardToolkitPlugin.class); 
//		//KitTargetMapping mapping = null;
//		int i=0; i++;
	}

	/**
     * This method is called after all plugins have been loaded by the plugin manager to avoid
     * requesting info from the manager during instantiation of this (the info is not valid yet at
     * that time since the manager must perform additional operations)
     */
    public void init() {
        int elemNum = -1;
        String newToolkit;
        PluginMgr mgr = PluginMgr.getSingleInstance();
        PluginDescriptor pluginDescriptor = mgr.getPluginsRegistry().getPluginInfo(this);
        
        /*
        if (mgr.isValid(pluginDescriptor)) {
            // Register this toolkit with supported dbmss names
            if (!(this instanceof DefaultForwardToolkit)) {
                int[] supportedTargets = getSupportedTargetIds();
                if (supportedTargets != null) {
                    for (int i = 0; i < supportedTargets.length; i++) {
                        elemNum = findMatchingAnsiTargetId(supportedTargets[i]);
                        newToolkit = this.getClass().getName();
                        if ((elemNum >= 0) && (!(newToolkit.contains("ansi")))) {
                            g_toolkitsTarget.remove(elemNum);
                            g_toolkitsTarget.add(new KitTargetMapping(supportedTargets[i], this));
                        } else {
                            g_toolkitsTarget.add(new KitTargetMapping(supportedTargets[i], this));
                        }
                    }
                }
            }

            // check if default toolkit
            if (this.isDefaultToolkit())
                defaultToolkit = this;
        }
        */
    }

    // Get the toolkit to handle both
    public static final ForwardToolkitPlugin getToolkit() {
        if (lastKit != null)
            return lastKit;

        if (activeDiagramTargetId < 0)
            return defaultToolkit;

        //search the kit
        Object[] map = getToolkitTargets().toArray();
        int nb = map.length;
        for (int i = 0; i < nb; i++) {
            KitTargetMapping mapping = (KitTargetMapping) map[i];
            int targetId = mapping.targetId;
            if (targetId == 999 && activeDiagramTargetId > 999)
                return mapping.kit;
            if (activeDiagramTargetId == targetId) {
                return mapping.kit;
            }
        } //end for
        return defaultToolkit;
    } //end getToolkit()

    private static final ArrayList findMatchingToolkitsForTargetId(int id) {
        ArrayList kits = new ArrayList(5);
        Object[] map = getToolkitTargets().toArray();
        for (int i = 0; i < map.length; i++) {
            if (id == ((KitTargetMapping) map[i]).targetId) {
                kits.add(((KitTargetMapping) map[i]).kit);
            }
        }
        return kits;
    }

    private final int findMatchingAnsiTargetId(int id) {
        Object[] map = getToolkitTargets().toArray();
        for (int i = 0; i < map.length; i++) {
            if (id == ((KitTargetMapping) map[i]).targetId)
                return i;
        }
        return -1;
    }

    // Calling this method will invalidate the active toolkit.
    public static final void setActiveDiagramTarget(int targetId) {
        if (activeDiagramTargetId == targetId)
            return;
        activeDiagramTargetId = targetId;
        lastKit = null;
    }

    /////////////////////////////////////////////////////////////////////////////////
    // Internal Toolkits management methods

    // return an array of int representing the Target id supported by this toolkit
    // these int will be used to identify wich toolkit can be used
    protected int[] getSupportedTargetIds() {
        return null;
    }

    // There should be only one default toolkit.  This toolkit must be able to handle any situations and target.
    protected boolean isDefaultToolkit() {
        return false;
    }

    // Enable or disable the feature
    public boolean forwardEnabled() {
        return true;
    }

    // End of Internal Toolkits management methods
    /////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////
    // Navigation methods

    protected boolean beforePageChange(int pageid, DBMSForwardOptions options) {
        return true;
    }

    protected boolean afterPageChange(int pageid, DBMSForwardOptions options) {
        return true;
    }

    // This method is called when the reverse process (wizard) is cancel
    // Override this method if the kit need to make some clean up
    protected boolean forwardCanceled() {
        return true;
    }

    // This method is called after completion of the reverse process
    // Kits can override this method to clean up references, ...
    //protected void reverseCompleted(){}

    // End of Navigation methods
    /////////////////////////////////////////////////////////////////////////////////

    // Configure the specific options for this dbms
    // Note:  options may already be configure for this toolkit.
    //        Check specific object instance before configuring otherwise options may be
    //        resset each time the wizard page is changed.
    public void configureSpecificOptions(DBMSForwardOptions options) {
    }

    public void initializeObjectsScope(DBMSForwardOptions options) {
    }

    public String getTitle() {
        return ForwardEngineering;
    }

    // Return the wizard page sequence
    /*
     * public int[] getPagesSequence(){ return new int[]{PAGE_FIRST, PAGE_OPTIONS,
     * PAGE_OBJECTS_SELECTION}; }
     */

    // Override this method if the toolkit need to override specific wizard pages
    public WizardPage getPage(int pageid) {
        return null;
    }

    // Override this method if the forward have more statement radio button
    public int[] getSupportedStatements() {
        return new int[] { DBMSForwardOptions.CREATE_STATEMENTS,
                DBMSForwardOptions.DROP_STATEMENTS, DBMSForwardOptions.DROP_CREATE_STATEMENTS };
    }

    // Return false if you do not want to generate object CREATE statements
    public boolean createPKUKIndex() {
        return true;
    }

    // Return false if you do not want to generate object DROP statements
    public boolean dropPKUKIndex() {
        return true;
    }

    public Object getDbEnumerationForConcept(MetaClass metaClass, DbSMSAbstractPackage pack)
            throws DbException {
        return null;
    }

    public abstract Class getForwardClass();

    // This method must do the forward job
    public void doForward(DBMSForwardOptions options) {
        String title = getTitle();
        Controller controller = new DefaultController(title, false, DEFAULT_LOG_FILENAME);
        DBMSForwardWorker worker = new DBMSForwardWorker(options);
        controller.start(worker);
    }

    /////////////////////////
    // Plugin implementation
    public final String installAction(DefaultMainFrame frame, MainFrameMenu menuManager) {
        return null;
    }

    public final Class[] getSupportedClasses() {
        return null;
    }

    public final void execute(ActionEvent actEvent) throws Exception {
    }
    
    public boolean doListenSelection() { return (this instanceof PluginSelectionListener); }
    
	@Override
	public OptionGroup getOptionGroup() {
		return null;
	}

	@Override
	public PluginAction getPluginAction() {
		return null;
	}
    //
    /////////////////////////
} //end ForwardToolkit

