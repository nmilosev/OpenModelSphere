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
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.international.LocaleMgr;

public final class SetProjectDefaultBENotationAction extends AbstractDomainAction implements
        SelectionActionListener {

    private static final String kDefaultNotation = LocaleMgr.action
            .getString("ProjectDefaultBENotation");
    private static final String kSetDefaultNotation = LocaleMgr.action
            .getString("setdefaultBEnotation");

    private ArrayList notations = new ArrayList(0); // null object means

    // separator

    public SetProjectDefaultBENotationAction() {
        super(kDefaultNotation);
        this.setMnemonic(LocaleMgr.action.getMnemonic("ProjectDefaultBENotation"));
        setEnabled(false);
        setVisible(ScreenPerspective.isFullVersion());
    }

    protected final void doActionPerformed() {
        int selidx = getSelectedIndex();
        if (selidx != -1) {
            DbSMSProject project = (DbSMSProject) ApplicationContext.getFocusManager()
                    .getCurrentProject();
            DbBENotation notation = (DbBENotation) notations.get(selidx);
            try {
                project.getDb().beginTrans(Db.WRITE_TRANS, kSetDefaultNotation);
                if (notation != null)
                    project.setBeDefaultNotation(notation);
                project.getDb().commitTrans();
            } catch (DbException e) {
                org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                        ApplicationContext.getDefaultMainFrame(), e);
            }
        }
    }

    public final void updateSelectionAction() throws DbException {
        DbSMSProject project = (DbSMSProject) ApplicationContext.getFocusManager()
                .getCurrentProject();
        if (project == null) {
            setSelectedIndex(-1);
            setEnabled(false);
            return;
        }

        Object[] selobjs = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();

        if ((selobjs.length != 1) || !(selobjs[0] instanceof DbSMSProject)) {
            setSelectedIndex(-1);
            setEnabled(false);
            return;
        }

        int selidx = -1;
        notations.clear();
        DbBENotation selnotation = project.getBeDefaultNotation();

        DbEnumeration dbEnum = project.getComponents().elements(DbBENotation.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBENotation notation = (DbBENotation) dbEnum.nextElement();
            int masterNotationID = notation.getMasterNotationID().intValue();
            if (!(masterNotationID >= 13 && masterNotationID <= 19)) {
                notations.add(notation);
                if (notation == selnotation)
                    selidx = notations.size() - 1;
            }
        }
        dbEnum.close();

        Object[] items = new Object[notations.size()];
        for (int i = 0; i < notations.size(); i++) {
            items[i] = ((DbBENotation) notations.get(i)).getName();
        }

        setDomainValues(items);
        setSelectedIndex(selidx);
        setEnabled(true);
    }

    protected int getFeatureSet() {
        return SMSFilter.BPM;
    }
}
