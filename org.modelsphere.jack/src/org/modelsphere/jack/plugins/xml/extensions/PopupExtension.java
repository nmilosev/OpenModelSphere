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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.MenuElement;

import org.modelsphere.jack.actions.AbstractActionsStore;
import org.modelsphere.jack.awt.JackMenu;
import org.modelsphere.jack.awt.JackPopupMenu;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginAction;
import org.modelsphere.jack.plugins.xml.XmlPlugin;
import org.modelsphere.jack.plugins.xml.XmlPluginAction;
import org.modelsphere.jack.plugins.xml.XmlPluginDescriptor;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;

public class PopupExtension extends AbstractPluginExtension {

    public static final String ACTION_LABEL = "actionLabel";
    public static final String ACTION_ICON = "actionIcon";
    public static final String ACTION_NAME = "actionName";

    //private String m_label; 
    private String m_metaClasses; 
    private String m_popupSubmenu;

    public PopupExtension(XmlPluginDescriptor pluginDescriptor, Map<String, String> parameters) {
        super(pluginDescriptor, parameters);

        //get parameters
        m_metaClasses = super.getParameter("metaClasses");
        m_popupSubmenu = super.getParameter("popupSubmenu");
    }

    @Override
    public void createPluginAction(XmlPluginDescriptor pluginDescriptor) {

        //create plug-in
        XmlPlugin xmlPlugin = pluginDescriptor.getPluginInstance();

        //get action's name and label
        String actionLabel = super.getParameter(ACTION_LABEL);
        String actionIcon = super.getParameter(ACTION_ICON);
        String actionName = super.getParameter(ACTION_NAME);
        Image image = (actionIcon == null) ? pluginDescriptor.getImage() : pluginDescriptor.createImage(actionIcon);

        //create action
        Integer mnemonic = null;
        Image iconImage = image.getScaledInstance(20, 20, 0);
        Icon icon = (image == null) ? null : new ImageIcon(iconImage);
        XmlPluginAction action = new XmlPluginAction(pluginDescriptor);
        action.setName(actionLabel);
        action.setIcon(icon);
        boolean visible = pluginDescriptor.isEnabled();
        action.setVisible(visible);
        action.putValue(ACTION_NAME, actionName);
        action.putValue(ACTION_LABEL, actionLabel);
        action.putValue(XmlPluginDescriptor.EXTENSION, this);

        AbstractActionsStore actionstore = ApplicationContext.getActionStore();
        String actionKey = getActionKey(pluginDescriptor);
        //String actionkey = pluginDescriptor.getClassName();

        if (actionstore.get(actionKey) == null) {
            //action = new XmlPluginAction(pluginDescriptor);
            actionstore.put(actionKey, action);
        }
    }

    @Override
    public String getActionKey() {
        Class<?> claz = _xmlDescriptor.getPluginClass();
        String actionName = getParameter(PopupExtension.ACTION_NAME);
        String actionKey = claz.getName() + "." + actionName;
        return actionKey;
    }

    private String getActionKey(XmlPluginDescriptor pluginDescriptor) {
        String actionName = super.getParameter(ACTION_NAME);
        String actionKey = pluginDescriptor.getClassName() + "." + actionName;
        return actionKey;
    }

    private Class<? extends MetaClass>[] m_metaClassArray; 
    public Class<? extends MetaClass>[] getSupportedMetaClasses() { 
        if (m_metaClassArray == null) {
            m_metaClassArray = readMetaClasses(m_metaClasses);
        }

        return m_metaClassArray; 
    }

    public String getPopupSubmenu() { return m_popupSubmenu; }

    private Class<? extends MetaClass>[] readMetaClasses(String metaClassNames) {
        List<Class<? extends MetaClass>> metaClassList = new ArrayList<Class<? extends MetaClass>>();
        StringTokenizer st = new StringTokenizer(metaClassNames, ","); 
        while (st.hasMoreTokens()) {
            String metaClassName = st.nextToken();

            try {
                Class<? extends MetaClass> claz = (Class<? extends MetaClass>)Class.forName(metaClassName);
                metaClassList.add(claz);
            } catch (ClassNotFoundException ex) {

            }
        }

        int nb = metaClassList.size();
        Class<? extends MetaClass>[] metaClasses = new Class[nb];
        metaClassList.toArray(metaClasses);
        return metaClasses;
    }

    public void addActionInPopup(XmlPluginAction xmlPluginAction,
            JackPopupMenu popup, Object[] selObjects) {

        boolean enabled = xmlPluginAction.isEnabled(selObjects);
        if (enabled) {
            String submenuText = getPopupSubmenu();
            String label = getActionLabel(xmlPluginAction, selObjects);
            JMenuItem item; 

            if (submenuText == null) {
                item = popup.add(xmlPluginAction, label, selObjects);
            } else {
                JackMenu menu = getSubMenu(popup, submenuText);
                item = menu.add(xmlPluginAction);
            } //end if

            String actionName = getParameter(ACTION_NAME); 
            item.putClientProperty(ACTION_NAME, actionName);
            item.setText(label);
        } //end if
    }
    
    @Override 
    public String toString() {
        String name = getParameter(ACTION_NAME); 
        
        if (name == null) {
            name = _xmlDescriptor.getClassName();
        }
        
        return name;
    }

    private String getActionLabel(XmlPluginAction xmlPluginAction, Object[] selObjects) {
        String actionName = xmlPluginAction.getName();
            
        if (actionName == null) {
            actionName = getParameter(ACTION_LABEL);
        }

        if (actionName == null) {
            actionName = xmlPluginAction.getName();
        }

        return actionName;
    }

    private JackMenu getSubMenu(JackPopupMenu popup, String submenuText) {
        JackMenu submenu = null;

        MenuElement[] elements = popup.getSubElements();
        for (MenuElement element : elements) {
            if (element instanceof JackMenu) {
                JackMenu menu = (JackMenu)element;
                String text = menu.getText();
                if (text.equals(submenuText)) {
                    submenu = menu;
                }
            }
        } //end for

        if (submenu == null) {
            submenu = new JackMenu(submenuText);
            popup.add(submenu);
        }

        return submenu;
    }





}
