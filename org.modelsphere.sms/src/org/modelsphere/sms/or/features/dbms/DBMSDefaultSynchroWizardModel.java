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

package org.modelsphere.sms.or.features.dbms;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.gui.wizard.WizardModel;
import org.modelsphere.jack.gui.wizard.WizardPage;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORModel;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.international.LocaleMgr;

/**
 * 
 * Generic wizard panel
 * 
 */
public final class DBMSDefaultSynchroWizardModel implements WizardModel {
    private static final String kSynchro = LocaleMgr.screen.getString("Synchronization");

    // Default Page Id for DBMS Synchro pages
    public static final int PAGE_SYNCHRO_MODELS = 1;
    public static final int PAGE_OPTIONS = 2;
    public static final int PAGE_OBJECTS_SELECTION = 3;

    private DBMSReverseOptions options = new DBMSReverseOptions();

    public DBMSDefaultSynchroWizardModel(DbObject[] selObjs) {
        options.root = ApplicationContext.getFocusManager().getCurrentProject(); // can
        // be
        // change
        // latter
        // in
        // GUI
        // --
        // default
        // value
        options.synchro = true;
        ReverseToolkitPlugin.setConnection(null);
        ReverseToolkitPlugin.setActiveDiagramTarget(-1);

        if (selObjs == null || selObjs.length > 1 || selObjs.length == 0) {
            throw new RuntimeException("Expecting selection of one database");
        }

        DbORDatabase database = null;
        try {
            selObjs[0].getDb().beginTrans(Db.READ_TRANS);
            if (selObjs[0] instanceof DbORDatabase) {
                database = (DbORDatabase) selObjs[0];
            } else {
                DbORModel model = (selObjs[0] instanceof DbORModel) ? (DbORModel) selObjs[0]
                        : (DbORModel) selObjs[0].getCompositeOfType(DbORModel.metaClass);
                if (model != null) {
                    if (model instanceof DbORDataModel) {
                        database = ((DbORDataModel) model).getDeploymentDatabase();
                    } else if (model instanceof DbORDomainModel) {
                        database = ((DbORDomainModel) model).getDeploymentDatabase();
                    } else if (model instanceof DbOROperationLibrary) {
                        database = ((DbOROperationLibrary) model).getDeploymentDatabase();
                    }
                }
            }
            selObjs[0].getDb().commitTrans();
        } catch (DbException e) {
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
        } // end try

        if (database == null) {
            throw new RuntimeException("No active DbORDatabase. ");
        }

        try {
            database.getDb().beginTrans(Db.READ_TRANS);
            options.targetSystemId = database.getTargetSystem().getID().intValue();
            database.getDb().commitTrans();
        } catch (DbException e) {
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
        } // end try

        options.synchroSourceDatabase = database;
    } // end DBMSDefaultSynchroWizardModel

    public DBMSReverseOptions getOptions() {
        return options;
    }

    public int[] getPagesSequence() {
        if (options.getConnection() == null)
            return new int[] { PAGE_SYNCHRO_MODELS, PAGE_OPTIONS };
        return new int[] { PAGE_SYNCHRO_MODELS, PAGE_OPTIONS, PAGE_OBJECTS_SELECTION };
    }

    public WizardPage getPage(int pageid) {
        if (pageid == PAGE_SYNCHRO_MODELS)
            return new DefaultSynchroStartPage();
        return ReverseToolkitPlugin.getToolkit().createWizardPage(pageid, options.synchro);
    }

    public final String getTitle(int pageid) {
        if (pageid == PAGE_SYNCHRO_MODELS)
            return kSynchro;
        return ReverseToolkitPlugin.getToolkit().createTitle(options.synchro);
    }

    public final Object getConfiguration() {
        return options;
    }

    public final boolean beforePageChange(int pageid) {
        return ReverseToolkitPlugin.getToolkit().beforePageChange(pageid, options);
    }

    public final boolean afterPageChange(int pageid) {
        return ReverseToolkitPlugin.getToolkit().afterPageChange(pageid, options);
    }

    public final void cancel() {
        options = null;
    }

    public final void finish() {
        ReverseToolkitPlugin kit = ReverseToolkitPlugin.getToolkit();
        kit.doReverse(options);
        options = null;
    }

}
