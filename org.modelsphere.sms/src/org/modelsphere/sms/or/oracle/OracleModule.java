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
package org.modelsphere.sms.or.oracle;

import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.sms.Module;
import org.modelsphere.sms.actions.ActionFactory;
import org.modelsphere.sms.or.ORModule;
import org.modelsphere.sms.or.oracle.actions.OracleActionFactory;
import org.modelsphere.sms.or.oracle.db.ApplClasses;
import org.modelsphere.sms.or.oracle.db.DbORACheck;
import org.modelsphere.sms.or.oracle.db.DbORAColumn;
import org.modelsphere.sms.or.oracle.db.DbORADataFile;
import org.modelsphere.sms.or.oracle.db.DbORADataModel;
import org.modelsphere.sms.or.oracle.db.DbORADatabase;
import org.modelsphere.sms.or.oracle.db.DbORAIndex;
import org.modelsphere.sms.or.oracle.db.DbORAPackage;
import org.modelsphere.sms.or.oracle.db.DbORAPartition;
import org.modelsphere.sms.or.oracle.db.DbORAProgramUnitLibrary;
import org.modelsphere.sms.or.oracle.db.DbORARedoLogFile;
import org.modelsphere.sms.or.oracle.db.DbORARedoLogGroup;
import org.modelsphere.sms.or.oracle.db.DbORARollbackSegment;
import org.modelsphere.sms.or.oracle.db.DbORASubPartition;
import org.modelsphere.sms.or.oracle.db.DbORATable;
import org.modelsphere.sms.or.oracle.db.DbORATablespace;
import org.modelsphere.sms.or.oracle.db.DbORATrigger;
import org.modelsphere.sms.or.oracle.db.DbORAView;
import org.modelsphere.sms.or.oracle.popup.ORAPopupMenuPool;

public final class OracleModule extends Module {
    private static OracleModule singleton;

    static {
        singleton = new OracleModule();
    }

    private OracleModule() {
    }

    public static final OracleModule getSingleton() {
        return singleton;
    }

    public Module getSuperModule() {
        return ORModule.getSingleton();
    }

    // ///////////////////////////////////////////////////////////////////
    // Initialisation methods

    public void loadMeta() {
        ApplClasses.getFinalClasses();
    }

    public void initMeta() {
    }

    public void initIntegrity() {
    }

    public void initToolkits() {
        new ORADataModelToolkit();
        new ORAOperationLibraryToolkit();
        // new ORATypeModelToolkit();
    }

    public void initAndInstallOtherToolBars(DefaultMainFrame frame) {
    }

    //
    // ///////////////////////////////////////////////////////////////////

    // ///////////////////////////////////////////////////////////////////
    // Method used during initialisation

    protected ActionFactory getActionFactory() {
        return OracleActionFactory.getSingleton();
    }

    public Object[] getPopupMenuMapping() {
        return new Object[] { DbORADataModel.class, ORAPopupMenuPool.oraDataModel,
                DbORAProgramUnitLibrary.class, ORAPopupMenuPool.oraOperationLibrary,
                DbORACheck.class, ORAPopupMenuPool.oraCheck, DbORAView.class,
                ORAPopupMenuPool.oraView, DbORAColumn.class, ORAPopupMenuPool.oraColumn,
                DbORAIndex.class, ORAPopupMenuPool.oraIndex, DbORATable.class,
                ORAPopupMenuPool.oraTable, DbORATrigger.class, ORAPopupMenuPool.oraTrigger,
                DbORADatabase.class, ORAPopupMenuPool.oraDatabase, DbORADataFile.class,
                ORAPopupMenuPool.oraDataFile, DbORAPartition.class, ORAPopupMenuPool.oraPartition,
                DbORASubPartition.class, ORAPopupMenuPool.oraSubPartition, DbORARedoLogFile.class,
                ORAPopupMenuPool.oraRedoLogFile, DbORARedoLogGroup.class,
                ORAPopupMenuPool.oraRedoLogGroup, DbORARollbackSegment.class,
                ORAPopupMenuPool.oraRollbackSegment, DbORATablespace.class,
                ORAPopupMenuPool.oraTablespace, DbORAPackage.class, ORAPopupMenuPool.oraPackage, };
    }

}
