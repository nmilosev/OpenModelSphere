/*************************************************************************

Copyright (C) 2009 by neosapiens inc.

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

You can reach neosapiens inc. at: 

neosapiens inc.
1236 Gaudias-Petitclerc
Qu&eacute;bec, Qc, G1Y 3G2
CANADA
Telephone: 418-561-8403
Fax: 418-650-2375
http://www.neosapiens.com/
Email: marco.savard@neosapiens.com
       gino.pelletier@neosapiens.com

 **********************************************************************/
package org.modelsphere.jack.plugins.xml;

import java.awt.event.ActionEvent;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginAction;
import org.modelsphere.jack.plugins.PluginSelectionListener;
import org.modelsphere.jack.plugins.PluginSelectionListener2;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.plugins.io.PluginContext;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;

public class XmlPlugin implements Plugin2, PluginSelectionListener2 {
	private XmlPluginDescriptor m_pluginDescriptor;
	
	public XmlPlugin(XmlPluginDescriptor pluginDescriptor) {
		m_pluginDescriptor = pluginDescriptor;
	}

	@Override
	public OptionGroup getOptionGroup() {
		Plugin2 plugin = getNestedPlugin(); 
		return plugin.getOptionGroup();
	}

	@Override
	public PluginAction getPluginAction() {
		Plugin2 plugin = getNestedPlugin(); 
		return plugin.getPluginAction();
	}

	@Override
	public void execute(ActionEvent ev) throws Exception {
		Plugin2 plugin = getNestedPlugin(); 
		plugin.execute(ev); 
	}

	@Override
	public PluginSignature getSignature() {
		Plugin2 plugin = getNestedPlugin(); 
		return plugin.getSignature();
	}

	@Override
	public Class<? extends Object>[] getSupportedClasses() {
		return m_pluginDescriptor.getSupportedClasses();
	}
	
	public void setSupportedClasses(Class<? extends MetaClass>[] metaClasses) {
		m_metaClasses = metaClasses;
	}
	Class<? extends MetaClass>[] m_metaClasses = null; 

	@Override
	public String installAction(DefaultMainFrame frame,
			MainFrameMenu menuManager) {
		Plugin2 plugin = getNestedPlugin(); 
		return plugin.installAction(frame, menuManager); 
	}
	
	public boolean isListeningSelection() {
		Plugin2 plugin = getNestedPlugin(); 
		boolean listeningSelection = (plugin instanceof PluginSelectionListener); 
		return listeningSelection;
	}
	
	@Override
	public boolean selectionChanged() throws DbException {
		boolean changed = false; 
		Plugin2 plugin = getNestedPlugin(); 

		if (plugin instanceof PluginSelectionListener) {
			PluginSelectionListener listener = (PluginSelectionListener)plugin;
			changed = listener.selectionChanged();
		}
		
		return changed;
	}
	
	@Override
	public void onSelectionChanged(AbstractApplicationAction applAction) throws DbException {
		Plugin2 plugin = getNestedPlugin(); 
		
		if (plugin instanceof PluginSelectionListener2) {
			PluginSelectionListener2 listener = (PluginSelectionListener2)plugin;
			listener.onSelectionChanged(applAction);
		}
	}
	
	//
	// private methods
	//
	private Plugin2 m_nestedPlugin = null; 
	public Plugin2 getNestedPlugin() {
		if (m_nestedPlugin == null) {
			m_nestedPlugin = instantiatePlugin();
			PluginContext context = m_pluginDescriptor.getContext();
			context.setInstance(m_nestedPlugin); 
		}
		
		return m_nestedPlugin;
	}
	
	private Plugin2 instantiatePlugin() {
		Plugin2 instance = null;
		Class<?> pluginClass = m_pluginDescriptor.getPluginClass();
		
		if (pluginClass == null) {
			instance = null;
		} else {
			try {
				Object o = pluginClass.newInstance();
				if (o instanceof Plugin2) {
					instance = (Plugin2)o;
				}
			} catch (InstantiationException ex) {
				instance = null;
			} catch (IllegalAccessException ex) {
				instance = null;
			}
		}
	    
	   return instance;
	}

	public boolean doListenSelection() {
		 Plugin2 plugin = getNestedPlugin();
		 boolean listenSelection = (plugin instanceof PluginSelectionListener);
		 return listenSelection;
	}


}
