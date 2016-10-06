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

import javax.swing.Action;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public class PageBreaksAction extends AbstractApplicationAction implements SelectionActionListener {

    public PageBreaksAction() {
        super(LocaleMgr.action.getString("hidePageBreaks"));
    }

    protected final void doActionPerformed() {
        ApplicationDiagram diag = (ApplicationDiagram) ApplicationContext.getFocusManager()
                .getFocusObject();
        DiagramView view = diag.getMainView();
        view.setPageBreak(!view.hasPageBreak());
    }

    public final void updateSelectionAction() {
        if (isApplicationDiagramHaveFocus()) {
            setEnabled(true);
            putValue(Action.NAME, getItemName((ApplicationDiagram) ApplicationContext
                    .getFocusManager().getFocusObject()));
        } else {
            setEnabled(false);
        }
    }

    public static String getItemName(ApplicationDiagram diag) {
        if (diag.getMainView().hasPageBreak())
            return LocaleMgr.action.getString("hidePageBreaks");
        else
            return LocaleMgr.action.getString("showPageBreaks");
    }
}
