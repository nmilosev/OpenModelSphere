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

package org.modelsphere.sms.or.actions;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.db.DbSMSLinkModel;
import org.modelsphere.sms.oo.db.DbOOAbsPackage;
import org.modelsphere.sms.oo.java.db.srtypes.JVVisibility;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.features.DataModelToJava;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.or.screen.GenerateClassModelOptionFrame;

final class GenerateClassModelAction extends AbstractApplicationAction implements
        SelectionActionListener {

    GenerateClassModelAction() {
        super(LocaleMgr.action.getString("generateClassModel"));
        this.setMnemonic(LocaleMgr.action.getMnemonic("generateClassModel"));
        setVisible(ScreenPerspective.isFullVersion()); 
    }

    protected final void doActionPerformed() {
        DbObject[] objects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        try {
            objects[0].getDb().beginTrans(Db.READ_TRANS);
            GenerateClassModelOptionFrame createClassFrame = new GenerateClassModelOptionFrame(
                    ApplicationContext.getDefaultMainFrame(), (DbORDataModel) objects[0]);
            objects[0].getDb().commitTrans();
            createClassFrame.setVisible(true);
            if (createClassFrame.cancel == false) {
                boolean createLink = createClassFrame.createLinks;
                DbSMSLinkModel linkModel = createLink == true ? createClassFrame.linkModel : null;
                JVVisibility visibility = createClassFrame.visibility;
                DbOOAbsPackage destinationPkg = createClassFrame.destinationPkg;
                objects[0].getDb().beginTrans(Db.WRITE_TRANS,
                        LocaleMgr.action.getString("classModelGeneration"));
                new DataModelToJava((DbORDataModel) objects[0], destinationPkg, linkModel,
                        visibility);
                objects[0].getDb().commitTrans();
            }
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    public final void updateSelectionAction() throws DbException {
        DbObject[] objects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        if (objects.length == 1 && objects[0] instanceof DbORDataModel)
            setEnabled(true);
        else
            setEnabled(false);

    }

    protected int getFeatureSet() {
        return SMSFilter.JAVA;
    }
}
