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
package org.modelsphere.jack.plugins.xml.extensions;

import java.awt.Image;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.modelsphere.jack.actions.AbstractActionsStore;
import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.awt.JackMenu;
import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginAction;
import org.modelsphere.jack.plugins.xml.XmlPlugin;
import org.modelsphere.jack.plugins.xml.XmlPluginDescriptor;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;

public class MenuExtension extends AbstractPluginExtension {
	private String m_icon;
	private String m_label;
	private String m_menuPath;
	private String m_menuInsert;
	
	static enum MenuPosition {BEFORE, AFTER, UNDER};
	

	public MenuExtension(XmlPluginDescriptor pluginDescriptor, Map<String, String> parameters) {
		super(pluginDescriptor, parameters);
		
		//get parameters
		m_icon = super.getParameter("actionIcon");
		m_label = super.getParameter("actionLabel");
		m_menuPath = super.getParameter("menuPath");
		m_menuInsert = super.getParameter("menuInsert");
	}
	
	@Override
	public void createPluginAction(XmlPluginDescriptor pluginDescriptor) {

		//create action
		String label = super.getParameter("actionLabel");
		Integer mnemonic = null;
		Image image =  pluginDescriptor.createImage(m_icon);
		Image iconImage = image.getScaledInstance(16, 16, 0);
		Icon icon = (image == null) ? null : new ImageIcon(iconImage);
		XmlPlugin xmlPlugin = pluginDescriptor.getPluginInstance();
	    PluginAction action = new PluginAction(xmlPlugin, label, mnemonic, icon);
	    boolean visible = pluginDescriptor.isEnabled();
	    action.setVisible(visible);
	     
	    //insert action in menu
	    if (visible) {
	        //get menu position
	        MenuPosition pos = getMenuPosition();
	        
	        insertInMenu(action, pos);
	    }
	}
	
	private MenuPosition getMenuPosition() {
		MenuPosition position; 
		
		if ("after".equals(m_menuInsert)) {
			position = MenuPosition.AFTER;
	    } else if ("before".equals(m_menuInsert)) {
	    	position = MenuPosition.BEFORE;
	    } else {
	    	position = MenuPosition.UNDER;
	    }
		
		return position;
	}

	private void insertInMenu(Action action, MenuPosition pos) {
		
		StringTokenizer st = new StringTokenizer(m_menuPath, "/");
		MenuLocation location = null;
		JMenu parentMenu = null; 
		
		while (st.hasMoreTokens()) {
			String token = st.nextToken(); 
			
			if (parentMenu == null) {
				parentMenu = findNamedMainMenu(token);
			} else {
				location = findInSubMenu(token, parentMenu);
				parentMenu = location.getMenu();
			}
		} //end while
		
		//insert in specified location
		if (location != null) {
			JMenu menu = location.getMenu();
			int menuIndex = location.getMenuIndex();
			
			if ((pos == MenuPosition.BEFORE) && (menu != null)) {
				menu.insert(action, menuIndex); 
			} else if ((pos == MenuPosition.AFTER) && (menu != null)) {
				menu.insert(action, menuIndex+1); 
			} else {
				menu.add(action);
				//actionItem = parentMenu.add(action);
			} //end if
		}
	} //end insertInMenu()
	

	private JackMenu findNamedMainMenu(String menuKey) {
		DefaultMainFrame frame = ApplicationContext.getDefaultMainFrame();
		MainFrameMenu menuManager = frame.getMainFrameMenu();
		JMenu m = menuManager.getMenuForKey(menuKey);
		JackMenu namedMenu = null;
		
		if (m instanceof JackMenu) {
			namedMenu = (JackMenu)m;
		}
		
		return namedMenu;
	}

	private MenuLocation findInSubMenu(String className, JMenu parentMenu) {
		
		//String actionKey = findActionKey(className); 
		JMenu actionMenu = parentMenu; 

		int nbItems = parentMenu.getItemCount(); 
		int menuIndex = 0;
		for (int i = 0; i < nbItems; i++) {
	        JMenuItem item = parentMenu.getItem(i);
	        String simpleName; 
	        
	        if (item instanceof JackMenu) {	
	        	JackMenu menu = (JackMenu)item;
	        	simpleName = menu.getMenuKey();
	        	if (className.equals(simpleName)) {
	        		actionMenu = menu;
	        		break;
		        }
	        } else {
	        	String n = (item == null) ? null : item.getName();
		        int idx = (n == null) ? -1 : n.lastIndexOf('.');
		        simpleName = (idx == -1) ? null : n.substring(1+idx);
		        if (className.equals(simpleName)) {
		        	menuIndex = i;
		        	break;
		        }
	        }
		}
		
		MenuLocation loc = new MenuLocation(actionMenu, menuIndex);

	    return loc;
	}

	private String findActionKey(String className) {
		String actionKey = null;
		AbstractActionsStore store = ApplicationContext.getActionStore();
	    Iterator iter = store.keySet().iterator(); 
	    while (iter.hasNext()) {
	    	Object key = iter.next();
	    	Object value = store.get(key);
	    	if (value instanceof AbstractApplicationAction) {
	    		AbstractApplicationAction action = (AbstractApplicationAction)value;
	    		Class cl = action.getClass(); 
	    		String n = cl.getSimpleName(); 
	    		if (className.equals(n)) {
		    		actionKey = (String)key;
		    		break;
		    	}
	    	}
	    } //end while
	     
		return actionKey;
	}
	
	 @Override 
	    public String toString() {
	        String name = m_label;
	       
	        return name;
	    }
	
	private static class MenuLocation {
		private JMenu m_menu; 
		private int m_menuIndex; 
		
		MenuLocation(JMenu menu, int menuIndex) {
			m_menu = menu;
			m_menuIndex = menuIndex;
		}

		public int getMenuIndex() {
			return m_menuIndex;
		}

		public JMenu getMenu() {
			return m_menu;
		}
	}

}
