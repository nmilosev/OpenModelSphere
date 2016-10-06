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
import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEStyle;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSStyle;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.db.DbOOStyle;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORCommonItemStyle;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORDomainStyle;
import org.modelsphere.sms.or.db.DbORStyle;
import org.modelsphere.sms.style.FormatFrame;

public final class SetGoStyleAction extends AbstractDomainAction implements SelectionActionListener {

    private static final String kSetStyle = LocaleMgr.action.getString("setstyle");
    private static final String kStyle = LocaleMgr.action.getString("style");
    private static final String kDiagramDefaultStyle = LocaleMgr.action
            .getString("DiagramDefaultStyle");

    private ArrayList dbos = new ArrayList(0);

    public SetGoStyleAction() {
        super(kStyle);
        this.setMnemonic(LocaleMgr.action.getMnemonic("style"));
        setEnabled(false);
    }

    protected final void doActionPerformed() {
        int selidx = getSelectedIndex();
        if (selidx != -1) {
            Object[] objects = ApplicationContext.getFocusManager().getSelectedObjects();
            DbSMSStyle style = null;
            if (selidx != 0) {
                style = (DbSMSStyle) dbos.get(selidx - 2);
            }
            try {
                Db db = ApplicationContext.getFocusManager().getCurrentProject().getDb();
                db.beginTrans(Db.WRITE_TRANS, kSetStyle);
                for (int i = 0; i < objects.length; i++) {
                    DbSMSGraphicalObject go = (DbSMSGraphicalObject) ((ActionInformation) objects[i])
                            .getGraphicalObject();
                    go.setStyle(style);
                }
                db.commitTrans();

                // reset the GO to the default style
                FormatFrame.resetToDefaultStyle(objects);
            } catch (DbException e) {
                org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                        ApplicationContext.getDefaultMainFrame(), e);
            }
        }
    }

    public final void updateSelectionAction() throws DbException {
        DbProject project = ApplicationContext.getFocusManager().getCurrentProject();
        ApplicationDiagram diag = ApplicationContext.getFocusManager().getActiveDiagram();
        if (diag == null) {
            setEnabled(false);
            return;
        }
        DbSMSDiagram diagGO = (DbSMSDiagram) diag.getDiagramGO();
        DbSemanticalObject semObj = diag.getSemanticalObject();
        if (semObj == null) {
            setEnabled(false);
            return;
        }

        Object[] objects = ApplicationContext.getFocusManager().getSelectedObjects();
        if ((diag == null) || (objects.length == 0)) {
            setSelectedIndex(-1);
            setEnabled(false);
            return;
        }

        for (int i = 0; i < objects.length; i++) {
            if (objects[i] instanceof ActionInformation
                    && ((ActionInformation) objects[i]).getSemanticalObject() != null)
                continue;
            setSelectedIndex(-1);
            setEnabled(false);
            return;
        }

        // update values
        dbos.clear();

        MetaClass styleMetaClass = null;
        if (diagGO instanceof DbOODiagram)
            styleMetaClass = DbOOStyle.metaClass;
        else if (semObj instanceof DbORDomainModel)
            styleMetaClass = DbORDomainStyle.metaClass;
        else if (semObj instanceof DbORCommonItemModel)
            styleMetaClass = DbORCommonItemStyle.metaClass;
        else if (semObj instanceof DbORDataModel)
            styleMetaClass = DbORStyle.metaClass;
        else if (diagGO instanceof DbBEDiagram)
            styleMetaClass = DbBEStyle.metaClass;

        DbEnumeration dbEnum = project.getComponents().elements(styleMetaClass);
        while (dbEnum.hasMoreElements()) {
            Object style = dbEnum.nextElement();
            dbos.add(style);
        }
        dbEnum.close();

        Object[] items = new Object[dbos.size() + 2]; // adding a the component
        // for the diag default
        // style

        items[0] = kDiagramDefaultStyle;
        items[1] = null;
        for (int i = 0; i < dbos.size(); i++) {
            items[i + 2] = ((DbSMSStyle) dbos.get(i)).getName();
        }

        DbSMSStyle selstyle = null;
        boolean diagDefaultStyle = true;

        // find the selected value
        for (int i = 0; i < objects.length; i++) {
            DbObject go = ((ActionInformation) objects[i]).getGraphicalObject();
            DbSMSStyle goStyle = ((DbSMSGraphicalObject) go).getStyle();
            if (selstyle == null)
                selstyle = goStyle;
            else if (selstyle != goStyle) {
                selstyle = null;
                break;
            }
            if (goStyle != null)
                diagDefaultStyle = false;
        }

        setDomainValues(items);
        if (diagDefaultStyle)
            setSelectedIndex(0);
        else if (selstyle == null)
            setSelectedIndex(-1);
        else
            setSelectedIndex(dbos.indexOf(selstyle) + 2);

        setEnabled(true);
    }

}
