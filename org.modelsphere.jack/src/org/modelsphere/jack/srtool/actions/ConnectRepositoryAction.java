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

package org.modelsphere.jack.srtool.actions;

import java.util.Properties;

import javax.swing.JOptionPane;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbRAM;
import org.modelsphere.jack.baseDb.db.VersionConverter;
import org.modelsphere.jack.baseDb.db.event.DbListener;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public final class ConnectRepositoryAction extends AbstractApplicationAction implements DbListener {
    private static final String kConnect = LocaleMgr.action.getString("ConnectRepository");
    private static final String kDisconnect = LocaleMgr.action.getString("DisconnectRepository");
    private static final String kConnectFailed = LocaleMgr.misc.getString("connectRepoFailed");
    private static final String kConnectSuccess = LocaleMgr.misc.getString("connectRepoSuccess");
    private static final String kCheckConnectString = LocaleMgr.message
            .getString("CheckConnectString");

    private static VersionConverter converter = null;
    private boolean connect = true;

    public ConnectRepositoryAction() {
        super(kConnect);
        setEnabled(false);
        setVisibilityMode(VISIBILITY_DEFAULT);
        Db.addDbListener(this);
    }

    protected final void doActionPerformed() {
        // Connect to repository
        if (connect) {
            String connectString = Db.getConnectionString();
            if (connectString == null || connectString.trim().length() == 0) {
                JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(),
                        kCheckConnectString, ApplicationContext.getApplicationName(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Properties props = ApplicationContext.getCommandLineProperties(); 
            boolean convert = props.getProperty("convertrepository") != null;
            Db dbRepos = Db.createDbRepository(ApplicationContext.getDefaultMainFrame(),
                    ApplicationContext.getApplicationName(), ApplicationContext.REPOSITORY_ROOT_NAME,
                    converter, convert);
            if (dbRepos != null) {
                ApplicationContext.setDbRepos(dbRepos);
                ApplicationContext.getDefaultMainFrame().getStatusBar().getModel().setMessage(
                        kConnectSuccess);
            } else {
                ApplicationContext.getDefaultMainFrame().getStatusBar().getModel().setMessage(
                        kConnectFailed);
            }
            // }
        } else {
            Db[] dbs = Db.getDbs();
            for (int i = 0; i < dbs.length; i++) {
                if (dbs[i] instanceof DbRAM)
                    continue;
                dbs[i].terminate();
            }
        }
    }

    public void dbCreated(Db db) {
        if (db instanceof DbRAM) {
            return;
        }
        setName(kDisconnect);
        connect = false;
    }

    public void dbTerminated(Db db) {
        if (db instanceof DbRAM) {
            return;
        }
        setName(kConnect);
        connect = true;
    }

    public static void setVersionConverter(VersionConverter converter) {
        if (ConnectRepositoryAction.converter == null)
            ConnectRepositoryAction.converter = converter;
    }
}
