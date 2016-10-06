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

import org.modelsphere.jack.actions.AbstractDomainAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.be.db.DbBEStyle;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSStyle;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.db.DbOOStyle;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORCommonItemStyle;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORDomainStyle;
import org.modelsphere.sms.or.db.DbORStyle;

public final class SetDiagramDefaultStyleAction extends AbstractDomainAction implements
        SelectionActionListener {

    private static final String kDiagDefaultStyle = LocaleMgr.action
            .getString("DiagramDefaultStyle");
    private static final String kSetDefaultStyle = LocaleMgr.action.getString("setdefaultstyle");

    private ArrayList styles = new ArrayList(0); // null object means separator

    public SetDiagramDefaultStyleAction() {
        super(kDiagDefaultStyle);
        this.setMnemonic(LocaleMgr.action.getMnemonic("DiagramDefaultStyle"));
        setEnabled(false);
    }

    protected final void doActionPerformed() {
        int selidx = getSelectedIndex();
        if (selidx != -1) {
            DbSMSDiagram diag = (DbSMSDiagram) ApplicationContext.getFocusManager()
                    .getActiveDiagram().getDiagramGO();
            DbSMSStyle style = (DbSMSStyle) styles.get(selidx);
            try {
                diag.getDb().beginTrans(Db.WRITE_TRANS, kSetDefaultStyle);
                diag.setStyle(style);
                diag.getDb().commitTrans();
            } catch (DbException e) {
                org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                        ApplicationContext.getDefaultMainFrame(), e);
            }
        }
    }

    public final void updateSelectionAction() throws DbException {
        ApplicationDiagram diag = ApplicationContext.getFocusManager().getActiveDiagram();
        if (diag == null) {
            setSelectedIndex(-1);
            setEnabled(false);
            return;
        }

        DbSMSDiagram diagGO = (DbSMSDiagram) diag.getDiagramGO();
        DbSemanticalObject semObj = diag.getSemanticalObject();
        DbProject project = ApplicationContext.getFocusManager().getCurrentProject();
        diagGO.getDb().beginReadTrans();
        DbSMSStyle selstyle = diagGO.getStyle();
        diagGO.getDb().commitTrans();

        int selidx = -1;
        styles.clear();

        DbEnumeration dbEnum = project.getComponents().elements(DbSMSStyle.metaClass);
        while (dbEnum.hasMoreElements()) {
            Object style = dbEnum.nextElement();
            if (diagGO instanceof DbOODiagram) {
                if (style instanceof DbOOStyle)
                    styles.add(style);
            } else if (semObj instanceof DbORDomainModel) {
                if (style instanceof DbORDomainStyle)
                    styles.add(style);
            } else if (semObj instanceof DbORCommonItemModel) {
                if (style instanceof DbORCommonItemStyle)
                    styles.add(style);
            } else if (semObj instanceof DbBEUseCase) {
                if (style instanceof DbBEStyle)
                    styles.add(style);
            } else {
                if (style instanceof DbORStyle)
                    styles.add(style);
            }
            if (style == selstyle)
                selidx = styles.size() - 1;
        }
        dbEnum.close();

        Object[] items = new Object[styles.size()];
        for (int i = 0; i < styles.size(); i++) {
            items[i] = ((DbSMSStyle) styles.get(i)).getName();
        }

        setDomainValues(items);
        setSelectedIndex(selidx);
        setEnabled(true);
    }

}
