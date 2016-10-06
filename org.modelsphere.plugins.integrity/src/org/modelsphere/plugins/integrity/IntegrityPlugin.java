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

package org.modelsphere.plugins.integrity;

import java.awt.event.ActionEvent;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.plugins.*;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.or.db.*;

public final class IntegrityPlugin extends AbstractIntegrity implements Plugin2, PluginSelectionListener {
    private static final String IS_VALIDATED = org.modelsphere.sms.international.LocaleMgr.db
            .getString("isValidated");
    private PluginAction action;

    public IntegrityPlugin() {
        action = new PluginAction(this, LocaleMgr.misc.getString("VerifyIntegr")) {
            public final int getFeatureSet() {
                return SMSFilter.BPM | SMSFilter.RELATIONAL;
            }
        };
        action.setIcon(LocaleMgr.misc.getImageIcon("VerifyIntegr"));
        action.setMnemonic(LocaleMgr.misc.getMnemonic("VerifyIntegr"));
    } //end Integrity()

    public PluginSignature getSignature() {
        return null;
    }

    public String installAction(DefaultMainFrame frame, MainFrameMenu menuManager) {
        return null;
    }

    public Class[] getSupportedClasses() {
        return new Class[] { DbORDataModel.class, DbORDomainModel.class, DbORCommonItemModel.class,
                DbOROperationLibrary.class, DbORDiagram.class, DbBEModel.class, DbBEDiagram.class, };
    }

    public void execute(ActionEvent actEvent) throws Exception {
        // PluginServices.multiDbBeginTrans(, null);
        String txName = org.modelsphere.plugins.integrity.LocaleMgr.misc
                .getString("titleIntegrity");
        PluginServices.multiDbBeginTrans(Db.WRITE_TRANS, txName);
        String cleanUpStr = org.modelsphere.sms.international.LocaleMgr.action
                .getString("CleanUpModel");
        String actionCmd = actEvent.getActionCommand();

        //Get selected objects
        DbObject[] objs;
        ApplicationDiagram diagram = PluginServices.getActiveDiagram();
        if (diagram != null) {
            objs = new DbObject[] { diagram.getDiagramGO() };
        } else {
            objs = PluginServices.getSelectedSemanticalObjects();
        } //end of

        //Sélection multiple mais pour le moment il y a un rapport par modele
        int nbErrorsFound = 0;
        for (int i = 0; i < objs.length; i++) {
            DbObject obj = objs[i];
            if (obj instanceof DbBEDiagram) {
                obj = (DbBEModel) obj.getCompositeOfType(DbBEModel.metaClass);
            } else if (obj instanceof DbORDiagram) {
                obj = (DbORModel) obj.getCompositeOfType(DbORModel.metaClass);

                if (obj == null) {
                    obj = (DbORCommonItemModel) objs[0]
                            .getCompositeOfType(DbORCommonItemModel.metaClass);
                }
            } //end if

            /* BPM MODELS */
            if (obj instanceof DbBEModel) {
                if (actionCmd.equals(cleanUpStr)) {
                    BeCleanUp beCleanUp = new BeCleanUp();
                    beCleanUp.cleanUpBeModel((DbBEModel) obj);
                } else {
                    BeIntegrity beInteg = new BeIntegrity();
                    beInteg.verifyBeModel((DbBEModel) obj);
                }
            }
            /*
             * OR MODELS (DbORDataModel, DbORDomainModel, DbORCommonItemModel, DbOROperationLibrary
             * )
             */
            else if ((obj instanceof DbORModel) || (obj instanceof DbORCommonItemModel)) {
                int modelType = getModelType(obj);
                if (actionCmd.equals(cleanUpStr)) {
                    OrCleanUp orCleanUp = new OrCleanUp();
                    orCleanUp.cleanUpOrModel((DbObject) obj, modelType);
                } else {
                    OrIntegrity orInteg = new OrIntegrity();
                    nbErrorsFound += orInteg.verifyIntegrityOrModel((DbObject) obj, modelType);
                }
            } //end if
        } //end for

        PluginServices.multiDbCommitTrans();

        //only first object is validated
        if (objs[0] instanceof DbORModel) {
            DbORModel model = (DbORModel) objs[0];
            validateModel(model, (nbErrorsFound == 0));
        } else { //if it a model component
            objs[0].getDb().beginReadTrans();
            DbORModel model = (DbORModel) objs[0].getCompositeOfType(DbORModel.metaClass);
            objs[0].getDb().commitTrans();
            if (model != null) {
                validateModel(model, (nbErrorsFound == 0));
            }
        } //end if  
    } //end execute()

    private void validateModel(DbORModel model, boolean isValidated) {
        try {
            model.getDb().beginReadTrans();
            String modelName = model.getName();
            boolean valid = model.isIsValidated();
            String transName = modelName + ": " + IS_VALIDATED;
            model.getDb().commitTrans();

            //No need to start a transaction if valid == validated
            if (valid != isValidated) {
                model.getDb().beginWriteTrans(transName);
                model.setIsValidated(isValidated);
                model.getDb().commitTrans();
            }
        } catch (DbException ex) {
            //ignore
        } //end try

    } //end validateModel()

    public OptionGroup getOptionGroup() {
        return null;
    }

    public PluginAction getPluginAction() {
        return action;
    }

    public boolean doListenSelection() { return (this instanceof PluginSelectionListener); }
    
	@Override
	public boolean selectionChanged() throws DbException {
		ApplicationDiagram diagram = PluginServices.getActiveDiagram();
		boolean enabled = (diagram != null);
		return enabled;
	}

}
