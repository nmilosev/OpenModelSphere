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
import java.util.List;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.awt.JackPopupMenu;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginSelectionListener;
import org.modelsphere.jack.plugins.PluginSelectionListener2;
import org.modelsphere.jack.plugins.PluginServices;
import org.modelsphere.jack.plugins.xml.extensions.AbstractPluginExtension;
import org.modelsphere.jack.plugins.xml.extensions.PopupExtension;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.util.ExceptionHandler;

@SuppressWarnings("serial")
public class XmlPluginAction extends AbstractApplicationAction implements SelectionActionListener {
	
	private XmlPluginDescriptor m_descriptor;
	
	public XmlPluginAction(XmlPluginDescriptor descriptor) {
		super(descriptor.getActionLabel());
		
		boolean enabled = descriptor.isEnabled();
		setVisible(enabled);
		m_descriptor = descriptor;
	}
	
	public XmlPluginDescriptor getPluginDescriptor() {
		return m_descriptor;
	}
	
	@Override
    protected final void doActionPerformed(ActionEvent ev) {
		
		Plugin2 plugin = m_descriptor.getPluginInstance();
		
        // Secure the execution of this plugin for any Exception that may occur.
        try {
            plugin.execute(ev); 
        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
        }
    }
    
	@Override
	public void updateSelectionAction() throws DbException {
		XmlPlugin plugin = m_descriptor.getPluginInstance();
		
		if (plugin.isListeningSelection()) {
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
        Class<?>[] supportedclasses =  m_descriptor.getSupportedClasses(); //  plugin.getSupportedClasses();
        if (supportedclasses == null || supportedclasses.length == 0) {
            setEnabled(true);
            return;
        }

        //if active diagram is among the supported classes, enable the action
        ApplicationDiagram diag = PluginServices.getActiveDiagram();
        if (diag != null) {
            DbObject diagGo = diag.getDiagramGO();
            Class<?> claz = diagGo.getClass();
            if (isSupportedClass(supportedclasses, claz)) {
                setEnabled(true);
                if (claz.getName().equals("org.modelsphere.sms.be.db.DbBEDiagram")) {
                    if (TerminologyUtil.getInstance().isUML(diagGo))
                        setEnabled(false);
                }
                return;
            }
        } //end if
        
        DbObject[] selectedObjs = PluginServices.getSelectedSemanticalObjects();
        for (int i = 0; i < selectedObjs.length; i++) {
            if (TerminologyUtil.getInstance().isUML(selectedObjs[i])) {
                setEnabled(false);
                return;
            }
        } //end for
        
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
	}
		
	private boolean isSupportedClass(Class<?>[] supclasses, Class<?> c) {
        boolean supported = false;
        for (int i = 0; i < supclasses.length; i++) {
            if (supclasses[i].isAssignableFrom(c)) {
                supported = true;
                break;
            }
        }
        return supported;
    }
	
	@Override public void setEnabled(boolean newValue) {
		super.setEnabled(newValue);
	}

	public void addActionInPopup(JackPopupMenu popup, Object[] selObjects) {
	    
	    Object value = getValue(XmlPluginDescriptor.EXTENSION); 
		
	    if (value instanceof PopupExtension) {
	        PopupExtension pe = (PopupExtension)value;
	        pe.addActionInPopup(this, popup, selObjects);
	    }
	  
	} //end addActionInPopup()


}
