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

package org.modelsphere.jack.srtool.actions;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.plugins.*;
import org.modelsphere.jack.srtool.forward.JackForwardEngineeringPlugin;

public class PluginGenerationAction extends PluginAction {
    private Plugin m_plugin = null;

    public PluginGenerationAction(JackForwardEngineeringPlugin plugin) {
        super(plugin);
        m_plugin = plugin;

    }

    public PluginGenerationAction(JackForwardEngineeringPlugin plugin, String name) {
        super(plugin, name);
        m_plugin = plugin;
    }

    public void updateSelectionAction() throws DbException {
        setEnabled(false);
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Object[] objects = PluginServices.getSelectedObjects();
        if (objects.length >= 1 && objects[0] instanceof DbObject) {
            DbObject dbo = terminologyUtil.isCompositeDataModel((DbObject) objects[0]);
            if (dbo != null || terminologyUtil.isDataModel((DbObject) objects[0])) {
                if (terminologyUtil.validateSelectionModel() != TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                    super.updateSelectionAction();
                else {
                    String sName = m_plugin.getClass().toString();
                    if (sName
                            .compareTo("class org.modelsphere.plugins.ansi.forward.GenericDDLForwardEngineeringPlugin") == 0)
                        setEnabled(false);
                    else if (sName
                            .compareTo("class org.modelsphere.sms.plugins.java.util.AccessorMethods") == 0)
                        setEnabled(false);
                    else if (sName
                            .compareTo("class org.modelsphere.sms.plugins.java.util.MissingCompilationUnits") == 0)
                        setEnabled(false);
                    else
                        setEnabled(true);
                }
            } else { // NOT A DATAMODEL
                String sName = m_plugin.getClass().toString();
                if (sName.compareTo("class org.modelsphere.sms.plugins.bpm.ProcessTree") == 0) {
                    Object[] objs = PluginServices.getSelectedObjects();
                    if (objs.length > 0) {
                        DbObject diagramGo = (DbObject) objs[0];
                        if (diagramGo != null && terminologyUtil.isObjectDiagram(diagramGo)) {
                            if (!terminologyUtil.isUML(diagramGo)) {
                                setEnabled(true);
                                return;
                            }
                        }
                    }
                } else {
                    super.updateSelectionAction();
                    return;
                }
            }
        }
    }
}
