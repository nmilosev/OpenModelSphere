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
package org.modelsphere.sms.oo.java.actions;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.oo.db.DbOOAbsPackage;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.oo.java.features.JavaToDataModelConversion;
import org.modelsphere.sms.oo.java.features.JavaToDataModelParameters;
import org.modelsphere.sms.oo.java.international.LocaleMgr;
import org.modelsphere.sms.oo.java.screen.GenerateDataModelOptionFrame;

final class GenerateDataModelAction extends AbstractApplicationAction implements
        SelectionActionListener {
    private static final long serialVersionUID = 1L;

    // convert class model or package
    private DbOOAbsPackage ooPackage = null; // Java class model or Java package

    GenerateDataModelAction() {
        super(LocaleMgr.action.getString("generateDataModel"));
        this.setMnemonic(LocaleMgr.action.getMnemonic("generateDataModel"));
    }

    // Action is possible if a class model or a package is selected
    // multi-selection is not supported
    public final void updateSelectionAction() throws DbException {
        DbObject[] objects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();

        if (objects.length == 1) {
            if (objects[0] instanceof DbJVClassModel) {
                ooPackage = (DbJVClassModel) objects[0];
                setEnabled(true);
            } else if (objects[0] instanceof DbJVPackage) {
                ooPackage = (DbJVPackage) objects[0];
                setEnabled(true);
            } else {
                setEnabled(false);
            }

        } else {
            setEnabled(false);
        }
    }

    protected final void doActionPerformed() {
        DbObject[] objects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        try {
            objects[0].getDb().beginTrans(Db.READ_TRANS);
            GenerateDataModelOptionFrame createDataFrame = new GenerateDataModelOptionFrame(
                    ApplicationContext.getDefaultMainFrame(), ooPackage);
            objects[0].getDb().commitTrans();
            createDataFrame.setVisible(true);
            if (createDataFrame.cancel == false) {
                JavaToDataModelParameters params = createDataFrame.getParameters();
                DbObject destination = createDataFrame.destination;
                JavaToDataModelConversion conversion = new JavaToDataModelConversion(destination,
                        params);

                objects[0].getDb().beginTrans(Db.WRITE_TRANS,
                        LocaleMgr.action.getString("dataModelGeneration"));
                conversion.convert(ooPackage);
                objects[0].getDb().commitTrans();
            }

        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    protected int getFeatureSet() {
        return SMSFilter.RELATIONAL;
    }
}
