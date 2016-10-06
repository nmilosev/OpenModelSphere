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

import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.KeyStroke;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.graphic.Attachment;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.LineLabel;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.explorer.ExplorerView;
import org.modelsphere.sms.international.LocaleMgr;

final class RemoveGraphicAction extends AbstractApplicationAction implements
        SelectionActionListener {

    public static String kRemove = LocaleMgr.action.getString("removeGraphRep");
    public static Icon deleteIcon = LocaleMgr.action.getImageIcon("removeGraphRep");

    RemoveGraphicAction() {
        super(kRemove, deleteIcon);
        setMnemonic(LocaleMgr.action.getMnemonic("removeGraphRep"));
        setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        setEnabled(false);
        setVisibilityMode(0);
    }

    protected final void doActionPerformed() {
        Object[] objects = ApplicationContext.getFocusManager().getSelectedObjects();
        try {
            DbMultiTrans.beginTrans(Db.WRITE_TRANS, objects, LocaleMgr.action
                    .getString("removeGraphRep"));
            for (int i = 0; i < objects.length; i++) {
                if (!(objects[i] instanceof LineLabel || objects[i] instanceof Attachment))
                    ((ActionInformation) objects[i]).getGraphicalObject().remove();
            }
            DbMultiTrans.commitTrans(objects);
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    public final void updateSelectionAction() throws DbException {
        setVisible(false);
        setEnabled(false);
        if (ApplicationContext.getFocusManager().getActiveDiagram() == null)
            return;

        Object object = ApplicationContext.getFocusManager().getFocusObject();
        if (object instanceof ExplorerView) {
            setAccelerator(null);
        } else {
            setName(kRemove);
            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
            setVisible(true);
        }

        Object[] objects = ApplicationContext.getFocusManager().getSelectedObjects();
        int nb = 0;
        for (int i = 0; i < objects.length; i++) {
            if (!(objects[i] instanceof GraphicComponent))
                return;
            if (!(objects[i] instanceof LineLabel || objects[i] instanceof Attachment))
                nb++;
        }
        setEnabled(nb > 0);
    }
}
