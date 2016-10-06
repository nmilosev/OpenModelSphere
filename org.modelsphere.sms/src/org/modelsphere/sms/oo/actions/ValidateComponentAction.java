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
package org.modelsphere.sms.oo.actions;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.oo.international.LocaleMgr;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.oo.java.validation.JavaValidation;

public final class ValidateComponentAction extends AbstractApplicationAction implements
        SelectionActionListener {

    private static final String IS_VALIDATED = org.modelsphere.sms.international.LocaleMgr.db
            .getString("isValidated");

    ValidateComponentAction() {
        super(LocaleMgr.action.getString("validateForJava"), LocaleMgr.action
                .getImageIcon("validateForJava"));
        this.setMnemonic(LocaleMgr.action.getMnemonic("validateForJava"));
        setEnabled(false);
        setDefaultToolBarVisibility(false);
        setVisible(ScreenPerspective.isFullVersion());
    }

    protected final void doActionPerformed() {
        DbObject[] semObjs = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        try {
            // get model name
            String modelName = getModelName(semObjs[0]);
            String transName = modelName + ": " + IS_VALIDATED;

            // start transaction
            semObjs[0].getDb().beginWriteTrans(transName);

            // validate
            JavaValidation validation = new JavaValidation();
            boolean validationFailed = validation.execute(semObjs);

            // set isValidated = true
            for (int i = 0; i < semObjs.length; i++) {

                // if it is a class model
                if (semObjs[i] instanceof DbJVClassModel) {
                    validateModel((DbJVClassModel) semObjs[i], validationFailed);
                } else { // if it is a class model component

                    // get the isValidated flag of its class model composite
                    DbJVClassModel classModel = (DbJVClassModel) semObjs[i]
                            .getCompositeOfType(DbJVClassModel.metaClass);
                    boolean isValidated = (classModel == null) ? false : classModel.isIsValidated();

                    if (classModel != null) {
                        if (isValidated && validationFailed) {
                            validateModel(classModel, validationFailed);
                        } // end if
                    } // end if
                } // end if
            } // end for

            // commit transaction
            semObjs[0].getDb().commitTrans();

        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        } // end try
    } // end doActionPerformed()

    private String getModelName(DbObject dbo) throws DbException {
        dbo.getDb().beginReadTrans();

        DbJVClassModel classModel;

        if (dbo instanceof DbJVClassModel) {
            classModel = (DbJVClassModel) dbo;
        } else {
            classModel = (DbJVClassModel) dbo.getCompositeOfType(DbJVClassModel.metaClass);
        } // end if

        String modelName = classModel.getName();
        dbo.getDb().commitTrans();
        return modelName;
    }

    private void validateModel(DbJVClassModel classModel, boolean validationFailed)
            throws DbException {
        classModel.setIsValidated(validationFailed ? Boolean.FALSE : Boolean.TRUE);
    } // end validateModel()

    // Check that all objects in the selection belong to a single project.
    public final void updateSelectionAction() throws DbException {
        DbObject[] semObjs = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        boolean state = false;
        if (semObjs.length > 0 && ApplicationContext.getFocusManager().getCurrentProject() != null) {
            state = true;
            for (int i = 0; i < semObjs.length; i++) {
                DbObject semObj = semObjs[i];
                if (!(semObj instanceof DbJVClass || semObj instanceof DbJVPackage || semObj instanceof DbJVClassModel)) {
                    state = false;
                    break;
                }
            }
        }
        setEnabled(state);
    }

    protected int getFeatureSet() {
        return SMSFilter.JAVA;
    }
}
