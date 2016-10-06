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

package org.modelsphere.sms.be.actions;

import javax.swing.Icon;

import org.modelsphere.jack.actions.AbstractDomainAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.be.BEModule;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.international.LocaleMgr;

public final class DirectionAction extends AbstractDomainAction implements SelectionActionListener {
    private static Icon kArrowImage1 = GraphicUtil.loadIcon(BEModule.class,
            "international/resources/dir_1.gif");
    private static Icon kArrowImage2 = GraphicUtil.loadIcon(BEModule.class,
            "international/resources/dir_3.gif");
    private static Icon kArrowImage3 = GraphicUtil.loadIcon(BEModule.class,
            "international/resources/dir_2.gif");
    private static Icon kArrowImage4 = GraphicUtil.loadIcon(BEModule.class,
            "international/resources/dir_4.gif");

    DirectionAction() {
        super(LocaleMgr.action.getString("Direction"), new Object[] { kArrowImage1, kArrowImage2,
                kArrowImage3, kArrowImage4 }, true); // TODO
        // convert
        // Object[]
        // to
        // ImageIcon
        setEnabled(false);

    }

    protected final void doActionPerformed() {
        DbObject[] selobjs = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        if (selobjs == null || selobjs.length == 0) {
            return;
        }
        int index = getSelectedIndex();
        if (index == -1)
            return;
        try {
            // should not be executed on more than one diagram (ie one Db)
            selobjs[0].getDb().beginWriteTrans(LocaleMgr.action.getString("Direction"));
            for (int i = 0; i < selobjs.length; i++) {
                if (!(selobjs[i] instanceof DbBEFlow)) {
                    continue;
                }
                DbBEFlow flow = (DbBEFlow) selobjs[i];
                if (index == 0) {
                    flow.setArrowFirstEnd(Boolean.FALSE);
                    flow.setArrowSecondEnd(Boolean.FALSE);
                } else if (index == 1) {
                    flow.setArrowFirstEnd(Boolean.TRUE);
                    flow.setArrowSecondEnd(Boolean.FALSE);
                } else if (index == 2) {
                    flow.setArrowFirstEnd(Boolean.FALSE);
                    flow.setArrowSecondEnd(Boolean.TRUE);
                } else if (index == 3) {
                    flow.setArrowFirstEnd(Boolean.TRUE);
                    flow.setArrowSecondEnd(Boolean.TRUE);
                }
            }
            selobjs[0].getDb().commitTrans();
        } catch (DbException e) {
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
        }
    }

    public final void updateSelectionAction() throws DbException {
        DbObject[] selobjs = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        if (selobjs == null || selobjs.length == 0) {
            setEnabled(false);
            return;
        }

        for (int i = 0; i < selobjs.length; i++) {
            if (!(selobjs[i] instanceof DbBEFlow)) {
                setEnabled(false);
                return;
            }
        }

        int selIndex = getValueIndex((DbBEFlow) selobjs[0]);
        for (int i = 1; i < selobjs.length; i++) {
            int index = getValueIndex((DbBEFlow) selobjs[i]);
            if (selIndex != index) {
                selIndex = -1;
                break;
            }
        }

        setEnabled(true);
        setSelectedIndex(selIndex);
    }

    protected int getFeatureSet() {
        return SMSFilter.BPM;
    }

    private int getValueIndex(DbBEFlow flow) throws DbException {
        boolean arrow1 = flow.isArrowFirstEnd();
        boolean arrow2 = flow.isArrowSecondEnd();
        if (!arrow1 && !arrow2)
            return 0;
        if (arrow1 && arrow2)
            return 3;
        if (arrow1)
            return 1;
        return 2;
    }
}
