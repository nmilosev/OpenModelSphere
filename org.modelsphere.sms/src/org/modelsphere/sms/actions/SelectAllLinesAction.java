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

import java.util.ArrayList;
import java.util.Arrays;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.actions.AbstractSelectAction;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.SrLine;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.text.BETextUtil;

public final class SelectAllLinesAction extends AbstractSelectAction {
    private static ArrayList EXCLUDED_MODELS = null;

    public static String kSelectAllLines = LocaleMgr.action.getString("selectAllLines");
    public static String kSelectAllArcs = LocaleMgr.action.getString("selectAllArcs");

    public static char kMnemonicAllLines = LocaleMgr.action.getMnemonic("selectAllLines");
    public static char kMnemonicAllArcs = LocaleMgr.action.getMnemonic("selectAllArcs");

    public SelectAllLinesAction() {
        super(kSelectAllLines);
    }

    protected final void doActionPerformed() {
        doActionPerformed(SrLine.class);
    }

    public final void currentFocusChanged(Object oldFocusObject, Object focusObject)
            throws DbException {
        Object focusobj = ApplicationContext.getFocusManager().getFocusObject();

        if (focusobj instanceof ApplicationDiagram) {
            DbObject diagram = ((ApplicationDiagram) focusobj).getDiagramGO();
            diagram.getDb().beginReadTrans();
            DbObject composite = diagram.getComposite();
            diagram.getDb().commitTrans();

            TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
            if (terminologyUtil.getModelLogicalMode(composite) == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                setMnemonic(kMnemonicAllArcs);
                setName(kSelectAllArcs);
            } else {
                setMnemonic(kMnemonicAllLines);
                if (terminologyUtil.isClassModel(composite))
                    setName(kSelectAllLines);
                else if (terminologyUtil.isDataModel(composite))
                    setName(kSelectAllLines);
                else
                    setName(BETextUtil.getSingleton().getSelectAllText(composite,
                            DbBEFlow.metaClass));

            }
        }

        if (EXCLUDED_MODELS == null) {
            setEnabled(true);
            return;
        }

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
