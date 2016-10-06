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

import java.util.ArrayList;
import java.util.Arrays;

import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.SrLineLabel;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public final class SelectAllLabelsAction extends AbstractSelectAction implements
        SelectionActionListener {
    private static ArrayList EXCLUDED_MODELS = null;

    public SelectAllLabelsAction() {
        super(LocaleMgr.action.getString("selectAllLabels"));
        this.setMnemonic(LocaleMgr.action.getMnemonic("selectAllLabels"));
        ApplicationContext.getFocusManager().removeCurrentFocusListener(this);
    }

    protected final void doActionPerformed() {
        doActionPerformed(SrLineLabel.class);
    }

    public void updateSelectionAction() throws DbException {
        if (EXCLUDED_MODELS == null) {
            setEnabled(true);
            return;
        }

        Object focusobj = ApplicationContext.getFocusManager().getFocusObject();
        if (focusobj == null) {
            setEnabled(false);
            return;
        }
        if (!(focusobj instanceof ApplicationDiagram)) {
            setEnabled(false);
            return;
        }
        ApplicationDiagram diag = (ApplicationDiagram) focusobj;
        DbSemanticalObject model = diag.getSemanticalObject();
        if (model == null) {
            setEnabled(false);
            return;
        }

        if (EXCLUDED_MODELS.contains(model.getClass())) {
            setEnabled(false);
            return;
        }

        setEnabled(true);
    }

    public static final void setExcludedModels(Class[] models) {
        if (models != null)
            EXCLUDED_MODELS = new ArrayList(Arrays.asList(models));
    }

}
