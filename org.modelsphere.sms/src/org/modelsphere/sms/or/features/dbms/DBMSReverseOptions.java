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

//Java
import java.util.ArrayList;

import org.modelsphere.jack.awt.tree.CheckTreeNode;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.plugins.generic.dbms.ObjectScope;
import org.modelsphere.jack.srtool.services.ConnectionMessage;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.or.international.LocaleMgr;

public class DBMSReverseOptions {
    private static final String kReverseOptions = LocaleMgr.misc.getString("ReverseOptions_");
    private static final String kRoot = LocaleMgr.misc.getString("Root_");
    private static final String kDomainModel = LocaleMgr.misc.getString("DomainModel_");
    private static final String kNewDomainModel = LocaleMgr.misc.getString("NewDomainModel");
    private static final String kNewProject = LocaleMgr.misc.getString("NewProject");
    private static final String kYes = LocaleMgr.misc.getString("yes");
    private static final String kNo = LocaleMgr.misc.getString("No");
    private static final String kReverseUser = LocaleMgr.screen.getString("ReverseUser");
    private static final String kUsingExtrFile0 = LocaleMgr.misc.getString("UsingExtrFile0");
    private static final String kConnection = LocaleMgr.misc.getString("Connection");

    private ConnectionMessage connection = null;
    public boolean isNewSchema = true;

    // objectsScope contains all objects supported by the SGBD,
    // the initialisation is made in the toolkit
    private ObjectScope[] objectsScope;

    // Contain the options specific to the DBMS
    // the initialisation is made in the toolkit
    private Object specificDBMSOptions;

    public ArrayList nameList = new ArrayList();

    // used for reading from extract files
    public boolean fromExtractFile = false;
    public String extractFilename;

    private String requestFile;

    public boolean reverseObjectUser = true;

    public int targetSystemId = -1;
    public DbObject root = null; // not always a project
    public DbObject domainModel = null;

    // synchro only options
    public boolean synchro = false;
    public DbObject synchroSourceDatabase = null;
    public DbObject synchroTargetDatabase = null;
    public CheckTreeNode fieldTree = null;
    public boolean genObjectWithUser = false;
    public boolean synchroUseUser = true;
    public boolean synchroOnline = true;

    public DBMSReverseOptions() {
    }

    public ConnectionMessage getConnection() {
        return connection;
    }

    public void setConnection(ConnectionMessage aConnection) {
        connection = aConnection;
    }

    public ArrayList getNameList() {
        return nameList;
    }

    public void setNameList(ArrayList aList) {
        nameList = aList;
    }

    public String getRequestFile() {
        return requestFile;
    }

    public void setRequestFile(String aRequestFile) {
        requestFile = aRequestFile;
    }

    public final void setSpecificDBMSOptions(Object specificOptions) {
        specificDBMSOptions = specificOptions;
    }

    public final Object getSpecificDBMSOptions() {
        return specificDBMSOptions;
    }

    public final void setObjectsScope(ObjectScope[] scope) {
        objectsScope = scope;
    }

    public final ObjectScope[] getObjectsScope() {
        return objectsScope;
    }

    public String toString() {
        String eol = "\n";
        String indent = "\t"; // NOT LOCALIZABLE
        String result = "";

        result = result.concat(kReverseOptions + eol + eol);
        result = result.concat(indent + kRoot + " "); // NOT LOCALIZABLE
        if (root != null) {
            try {
                root.getDb().beginTrans(Db.READ_TRANS);
                result = result.concat(root.getSemanticalName(DbObject.LONG_FORM) + eol);
                result = result.concat(indent + kDomainModel + " "); // NOT
                // LOCALIZABLE
                if (domainModel != null)
                    result = result.concat(domainModel.getSemanticalName(DbObject.LONG_FORM) + eol);
                else
                    result = result.concat(kNewDomainModel + eol);
                root.getDb().commitTrans();
            } catch (DbException e) {
            }
        } else
            result = result.concat(kNewProject + eol);

        result = result.concat(indent + kReverseUser + ":  "); // NOT
        // LOCALIZABLE
        result = result.concat((reverseObjectUser ? kYes : kNo) + eol);

        if (connection != null) {
            result += indent + kConnection + "  "; // NOT LOCALIZABLE
            result += connection.server;
            result += " (" + connection.databaseProductName; // NOT LOCALIZABLE
            result += ", " + connection.databaseProductVersion + ")"; // NOT
            // LOCALIZABLE
            result += eol;
        }

        if (requestFile != null) {
            result += indent + MessageFormat.format(kUsingExtrFile0, new Object[] { requestFile })
                    + eol;
        }

        result = result.concat(specificDBMSOptions == null ? "" : specificDBMSOptions.toString());

        return result;
    }
}
