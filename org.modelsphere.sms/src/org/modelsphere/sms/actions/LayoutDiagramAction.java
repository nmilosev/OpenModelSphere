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

package org.modelsphere.sms.actions;

import java.util.List;

import javax.swing.JComponent;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.FocusManager;
import org.modelsphere.jack.srtool.features.layout.LayoutException;
import org.modelsphere.jack.srtool.features.layout.NodesLayoutAlgorithm;
import org.modelsphere.jack.srtool.features.layout.BuiltinLayoutAlgorithm;
import org.modelsphere.jack.srtool.features.layout.graph.Graph;
import org.modelsphere.jack.srtool.features.layout.graph.Node;
import org.modelsphere.jack.srtool.features.layout.ui.LayoutDialog;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.international.LocaleMgr;

@SuppressWarnings("serial")
public final class LayoutDiagramAction extends AbstractApplicationAction implements SelectionActionListener {

	LayoutDiagramAction() {
		super(LocaleMgr.action.getString("layout_"));
	}

	protected final void doActionPerformed() {
		ApplicationDiagram diagram = FocusManager.getSingleton().getActiveDiagram();
		if (diagram == null)
			return;
		DbObject diagramGO = diagram.getDiagramGO();
		if (diagramGO instanceof DbSMSDiagram) {
		    Object[] selection = FocusManager.getSingleton().getSelectedObjects();
		    boolean useSelectionOption = false;
		    for (Object object : selection) {
                if (object instanceof ActionInformation){
                    useSelectionOption = true;
                    break;
                }
            }
		    
		    AbstractApplicationAction starLayoutAction = new LayoutSelectionAction();
		    NodesLayoutAlgorithm starLayoutAlgorithm = new BuiltinLayoutAlgorithm();
			LayoutDialog dialog = new LayoutDialog(ApplicationContext.getDefaultMainFrame(), diagram, useSelectionOption, starLayoutAction, starLayoutAlgorithm);
			dialog.setVisible(true);
		}
	}

	@Override
	public void updateSelectionAction() throws DbException {
		ApplicationDiagram diagram = FocusManager.getSingleton().getActiveDiagram();
		setEnabled(diagram != null);
	}
}
