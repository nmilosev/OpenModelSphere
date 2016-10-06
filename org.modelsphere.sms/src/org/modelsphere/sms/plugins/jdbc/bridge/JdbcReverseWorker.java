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

package org.modelsphere.sms.plugins.jdbc.bridge;

import java.sql.SQLException;

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.gui.task.Worker;
import org.modelsphere.jack.srtool.plugins.generic.dbms.ObjectScope;
import org.modelsphere.jack.srtool.plugins.generic.dbms.ObjectSelection;
import org.modelsphere.jack.srtool.plugins.generic.dbms.UserInfo;
import org.modelsphere.jack.srtool.reverse.Actions;
import org.modelsphere.jack.srtool.reverse.engine.ReverseBuilder;
import org.modelsphere.jack.srtool.reverse.jdbc.JdbcMetaInfo;
import org.modelsphere.jack.srtool.reverse.jdbc.JdbcReader;
import org.modelsphere.jack.srtool.services.ConnectionMessage;
import org.modelsphere.sms.or.features.dbms.DBMSReverseOptions;
import org.modelsphere.sms.or.features.dbms.DBMSUtil;

public final class JdbcReverseWorker extends Worker {

    private String jobTitleKey = LocaleMgr.misc.getString("JdbcReverseEng");
    private DBMSReverseOptions options;
    private Actions actions;

    // Idem except for the option class
    public JdbcReverseWorker(DBMSReverseOptions options, Actions actions) {
        if ((options == null) || (actions == null))
            throw new NullPointerException();
        this.options = options;
        this.actions = actions;
    }

    protected String getJobTitle() {
        return jobTitleKey;
    }

    // perform the reverse
    public void runJob() throws Exception {
        if (actions instanceof ReverseBuilder)
            ((ReverseBuilder) actions).setController(getController());

        // process data - do the reverse here
        try {
            getJdbcMetadata();
            if (actions instanceof ReverseBuilder)
                ((ReverseBuilder) actions).buildORDiagram();
        } catch (Exception e) {
            throw e;
        }
    }

    private void getJdbcMetadata() throws Exception {
        getController().println();
        getController().println(options.toString());

        if ((options != null) && (options.root != null))
            actions.init(options.root.getProject(), options);
        else
            return;

        JdbcObjectBuilder jdbcBuilder = new JdbcObjectBuilder(actions);
        ConnectionMessage connectionMsg = options.getConnection();
        JdbcReader reader = new JdbcReader(jdbcBuilder, connectionMsg.connectionId);

        boolean done = false;
        while (!done && getController().checkPoint()) {
            ObjectScope[] scope = options.getObjectsScope();
            for (int i = 0; i < scope.length && getController().checkPoint(); i++) {
                if (ObjectScope.verifyConceptSelection(scope, scope[i])) {
                    try {
                        switch (scope[i].conceptID) {
                        case JdbcReverseToolkitPlugin.USERID:
                            reverseUsers(reader);
                            break;
                        case JdbcReverseToolkitPlugin.TYPEID:
                        case JdbcReverseToolkitPlugin.UDTID:
                            reverseTypes(reader, ObjectScope.findConceptInObjectScopeWithConceptID(
                                    scope, new Integer(JdbcReverseToolkitPlugin.TYPEID)), scope[i]);
                            break;
                        case JdbcReverseToolkitPlugin.TABLEID:
                        case JdbcReverseToolkitPlugin.COLUMNID:
                        case JdbcReverseToolkitPlugin.PKID:
                        case JdbcReverseToolkitPlugin.FKID:
                        case JdbcReverseToolkitPlugin.INDEXID:
                            reverseTables(reader, ObjectScope
                                    .findConceptInObjectScopeWithConceptID(scope, new Integer(
                                            JdbcReverseToolkitPlugin.USERID)), ObjectScope
                                    .findConceptInObjectScopeWithConceptID(scope, new Integer(
                                            JdbcReverseToolkitPlugin.TABLEID)), scope[i]);
                            break;
                        case JdbcReverseToolkitPlugin.VIEWID:
                        case JdbcReverseToolkitPlugin.COLUMNVIEWID:
                            reverseViews(reader, ObjectScope.findConceptInObjectScopeWithConceptID(
                                    scope, new Integer(JdbcReverseToolkitPlugin.USERID)),
                                    ObjectScope.findConceptInObjectScopeWithConceptID(scope,
                                            new Integer(JdbcReverseToolkitPlugin.VIEWID)), scope[i]);
                            break;
                        case JdbcReverseToolkitPlugin.PROCID:
                        case JdbcReverseToolkitPlugin.PROCCOLID:
                            reverseProcedures(reader, ObjectScope
                                    .findConceptInObjectScopeWithConceptID(scope, new Integer(
                                            JdbcReverseToolkitPlugin.PROCID)), scope[i]);
                            break;
                        }
                    } catch (AbstractMethodError absMthErr) {
                        Debug.trace(absMthErr.toString());
                    }
                }
            }
            done = true;
        }
        actions.exit();
    }

    private void reverseUsers(JdbcReader aReader) throws SQLException, Exception {
        aReader.getSchemaInfo();
    }

    private void reverseTypes(JdbcReader aReader, ObjectScope typeScope, ObjectScope aScope)
            throws SQLException, Exception {
        switch (aScope.conceptID) {
        case JdbcReverseToolkitPlugin.TYPEID:
            aReader.getTypeInfo();
            break;
        case JdbcReverseToolkitPlugin.UDTID:
            int[] udtTypes = JdbcReverseOptions.getUDT();
            String catalog = ((JdbcReverseOptions) options.getSpecificDBMSOptions())
                    .getCatalogOption();
            for (int i = 0; i < typeScope.occurences.size() && getController().checkPoint(); i++) {
                if (typeScope.occurences.get(i) instanceof ObjectSelection) {
                    ObjectSelection objSel = (ObjectSelection) typeScope.occurences.get(i);
                    if (objSel.getIsSelected())
                        aReader.getUDTInfo(catalog, null, objSel.name, udtTypes);
                }
            }
            break;
        }
    }

    private void reverseTables(JdbcReader aReader, ObjectScope userScope, ObjectScope tableScope,
            ObjectScope aScope) throws SQLException, Exception {

        String[] tableTypes = JdbcReverseOptions.getTableTypes();
        String catalog = ((JdbcReverseOptions) options.getSpecificDBMSOptions()).getCatalogOption();
        for (int i = 0; i < tableScope.occurences.size() && getController().checkPoint(); i++) {
            if (tableScope.occurences.get(i) instanceof UserInfo) {
                UserInfo userInfo = (UserInfo) tableScope.occurences.get(i);
                String userName = userInfo.username;
                ObjectSelection objSel = ObjectScope.findObjectOccurenceByName(userScope, userName);
                if ((objSel == null) || (objSel.getIsSelected() == false))
                    continue;
                for (int j = 0; j < userInfo.occurences.size() && getController().checkPoint(); j++) {
                    if (userInfo.occurences.get(j) instanceof ObjectSelection) {
                        ObjectSelection objectSelection = (ObjectSelection) userInfo.occurences
                                .get(j);
                        if (objectSelection.getIsSelected()) {
                            String tableName = (String) objectSelection.toString();
                            switch (aScope.conceptID) {
                            case JdbcReverseToolkitPlugin.TABLEID:
                                aReader.getTableInfo(catalog, userName != null
                                        && userName.equals(DBMSUtil.NULL_USER) ? null : userName,
                                        tableName, tableTypes);
                                break;
                            case JdbcReverseToolkitPlugin.COLUMNID:
                                aReader.getColumnInfo(catalog, userName != null
                                        && userName.equals(DBMSUtil.NULL_USER) ? null : userName,
                                        tableName, JdbcMetaInfo.ZERO_OR_MORE);
                                break;
                            case JdbcReverseToolkitPlugin.PKID:
                                aReader.getPrimaryKeyInfo(catalog, userName != null
                                        && userName.equals(DBMSUtil.NULL_USER) ? null : userName,
                                        tableName);
                                break;
                            case JdbcReverseToolkitPlugin.FKID:
                                aReader.getForeignKeyInfo(catalog, userName != null
                                        && userName.equals(DBMSUtil.NULL_USER) ? null : userName,
                                        tableName);
                                break;
                            case JdbcReverseToolkitPlugin.INDEXID:
                                aReader.getIndexInfo(catalog, userName != null
                                        && userName.equals(DBMSUtil.NULL_USER) ? null : userName,
                                        tableName, false, false);
                                break;
                            } // End switch
                        } // End Object is selected
                    } // End Is object selection
                } // End for each user
            } // End userinfo
        } // End for each table
    }

    private void reverseViews(JdbcReader aReader, ObjectScope userScope, ObjectScope viewScope,
            ObjectScope aScope) throws SQLException, Exception {

        String[] viewTypes = JdbcReverseOptions.getViewTypes();
        String catalog = ((JdbcReverseOptions) options.getSpecificDBMSOptions()).getCatalogOption();
        for (int i = 0; i < viewScope.occurences.size() && getController().checkPoint(); i++) {
            if (viewScope.occurences.get(i) instanceof UserInfo) {
                UserInfo userInfo = (UserInfo) viewScope.occurences.get(i);
                String userName = userInfo.username;
                ObjectSelection objSel = ObjectScope.findObjectOccurenceByName(userScope, userName);
                if ((objSel == null) || (objSel.getIsSelected() == false))
                    continue;
                for (int j = 0; j < userInfo.occurences.size() && getController().checkPoint(); j++) {
                    if (userInfo.occurences.get(j) instanceof ObjectSelection) {
                        ObjectSelection objectSelection = (ObjectSelection) userInfo.occurences
                                .get(j);
                        if (objectSelection.getIsSelected()) {
                            String viewName = (String) objectSelection.toString();
                            switch (aScope.conceptID) {
                            case JdbcReverseToolkitPlugin.VIEWID:
                                aReader.getViewInfo(catalog, userName != null
                                        && userName.equals(DBMSUtil.NULL_USER) ? null : userName,
                                        viewName, viewTypes);
                                break;
                            case JdbcReverseToolkitPlugin.COLUMNVIEWID:
                                aReader.getViewColumnInfo(catalog, userName != null
                                        && userName.equals(DBMSUtil.NULL_USER) ? null : userName,
                                        viewName, JdbcMetaInfo.ZERO_OR_MORE);
                                break;
                            } // End switch
                        } // End Object is selected
                    } // End Is object selection
                } // End for each user
            } // End userinfo
        } // End for each view
    }

    private void reverseProcedures(JdbcReader aReader, ObjectScope procScope, ObjectScope aScope)
            throws SQLException, Exception {
        String catalog = ((JdbcReverseOptions) options.getSpecificDBMSOptions()).getCatalogOption();
        for (int i = 0; i < procScope.occurences.size() && getController().checkPoint(); i++) {
            if (procScope.occurences.get(i) instanceof UserInfo) {
                UserInfo userInfo = (UserInfo) procScope.occurences.get(i);
                if (userInfo.isSelected) {
                    String userName = userInfo.username;
                    for (int j = 0; j < userInfo.occurences.size() && getController().checkPoint(); j++) {
                        if (userInfo.occurences.get(j) instanceof ObjectSelection) {
                            ObjectSelection objectSelection = (ObjectSelection) userInfo.occurences
                                    .get(j);
                            if (objectSelection.getIsSelected()) {
                                String procName = (String) objectSelection.toString();
                                switch (aScope.conceptID) {
                                case JdbcReverseToolkitPlugin.PROCID:
                                    aReader.getProcedureInfo(catalog, userName != null
                                            && userName.equals(DBMSUtil.NULL_USER) ? null
                                            : userName, procName);
                                    break;
                                case JdbcReverseToolkitPlugin.PROCCOLID:
                                    aReader.getProcedureColumns(catalog, userName != null
                                            && userName.equals(DBMSUtil.NULL_USER) ? null
                                            : userName, procName, JdbcMetaInfo.ZERO_OR_MORE);
                                    break;
                                }
                            } // Proc occ is selected
                        } // is objct selection
                    } // all user occ.
                } // User selected
            }
        }
    }
}
