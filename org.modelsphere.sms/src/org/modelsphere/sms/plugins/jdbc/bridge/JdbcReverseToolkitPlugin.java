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

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.gui.task.Worker;
import org.modelsphere.jack.gui.wizard.WizardPage;
import org.modelsphere.jack.plugins.PluginSelectionListener;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.plugins.generic.dbms.ObjectScope;
import org.modelsphere.jack.srtool.reverse.Actions;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.features.dbms.DBMSDefaultReverseWizardModel;
import org.modelsphere.sms.or.features.dbms.DBMSReverseOptions;
import org.modelsphere.sms.or.features.dbms.DefaultReverseWizardObjectsPage;
import org.modelsphere.sms.or.features.dbms.DefaultReverseWizardStartPage;
import org.modelsphere.sms.or.features.dbms.ReverseToolkitPlugin;
import org.modelsphere.sms.or.generic.db.DbGEForeign;
import org.modelsphere.sms.or.generic.db.DbGEIndex;
import org.modelsphere.sms.or.generic.db.DbGEPrimaryUnique;
import org.modelsphere.sms.or.generic.db.DbGEProcedure;
import org.modelsphere.sms.or.generic.db.DbGETable;
import org.modelsphere.sms.or.generic.db.DbGEView;
import org.modelsphere.sms.plugins.TargetSystem;
import org.modelsphere.sms.plugins.jdbc.bridge.LocaleMgr;

public class JdbcReverseToolkitPlugin extends ReverseToolkitPlugin {
    private static final String jobTitleKey = LocaleMgr.misc.getString("JdbcReverseEng");

    // Concept IDs
    // Start with 0 and must be unique
    public static final int USERID = 0;
    public static final int TYPEID = 1;
    public static final int TABLEID = 2;
    public static final int COLUMNID = 3;
    public static final int PKID = 4;
    public static final int FKID = 5;
    public static final int INDEXID = 6;
    public static final int PROCID = 7;
    public static final int PROCCOLID = 8;
    public static final int UDTID = 9;
    public static final int VIEWID = 10;
    public static final int COLUMNVIEWID = 11;
    // Concept Names
    public static final String USER = "user";
    public static final String TYPE = "data_type";
    public static final String TABLE = "table";
    public static final String COLUMN = "column";
    public static final String PK = "primary_key";
    public static final String FK = "foreign_key";
    public static final String INDEX = "index";
    public static final String PROC = "procedure";
    public static final String PROCCOL = "procedure_col";
    public static final String UDT = "user_defined_type";
    public static final String VIEW = "view";
    public static final String VIEW_COLUMN = "view_column";

    private PluginSignature signature;

    public JdbcReverseToolkitPlugin() {
    }

    public PluginSignature getSignature() {
        if (signature == null) {
            signature = new PluginSignature(createTitle(), null, "3.1.0",
                    ApplicationContext.APPLICATION_AUTHOR,
                    ApplicationContext.APPLICATION_AUTHOR_URI,
                    ApplicationContext.APPLICATION_AUTHOR_CONTACT, "05-12-2009", null, 905);
        }
        return signature;
    }

    // JDBC is the default toolkit
    protected boolean isDefaultToolkit() {
        return true;
    }

    public String createTitle() {
        return jobTitleKey;
    }

    public Actions createActions() {
        return new JdbcReverseBuilder();
    }

    protected int getTargetSystemId(String version) {
        Debug.trace(version);
        if (version == null)
            return -1;
        if (version.toLowerCase().indexOf("microsoft sql server") > -1) { // NOT LOCALIZABLE
            if (version.toLowerCase().indexOf("8.") > -1)
                return TargetSystem.SGBD_SQLSERVER2000;
            if (version.toLowerCase().indexOf("7.") > -1)
                return TargetSystem.SGBD_SQLSERVER7;
            if (version.toLowerCase().indexOf("6.5") > -1)
                return TargetSystem.SGBD_SQLSERVER65;
            if (version.toLowerCase().indexOf("6.") > -1)
                return TargetSystem.SGBD_SQLSERVER60;
            if (version.toLowerCase().indexOf("4.") > -1)
                return TargetSystem.SGBD_SQLSERVER42;
            else
                return TargetSystem.SGBD_SQLSERVER2000;
        }

        return -1;
    }

    public WizardPage createWizardPage(int pageid, boolean synchro) {
        WizardPage page = null;
        switch (pageid) {
        case DBMSDefaultReverseWizardModel.PAGE_FIRST:
            return new DefaultReverseWizardStartPage();
        case DBMSDefaultReverseWizardModel.PAGE_OPTIONS:
            return new JdbcReverseSpecificOptionPage();
        case DBMSDefaultReverseWizardModel.PAGE_OBJECTS_SELECTION:
            return new DefaultReverseWizardObjectsPage();
        }
        return page;
    }

    public Object createSpecificOptions() {
        return new JdbcReverseOptions();
    }

    // Add all concepts to be reversed
    public ObjectScope[] createObjectsScope() {
        return new ObjectScope[] {
                //                    Concept Name                                          ConceptID  ParentID             ShowConcept  ShowOccurences  IsOwnedObject  SQLGetID
                new ObjectScope(DbORUser.metaClass.getGUIName(), USERID, null, false, false, false,
                        new Integer(0)),
                new ObjectScope(LocaleMgr.misc.getString("dataType"), TYPEID, null, true, true,
                        false, new Integer(1)),
                new ObjectScope(UDTID, new Integer(TYPEID)),
                new ObjectScope(DbGETable.metaClass.getGUIName(false, false), TABLEID, null, true,
                        true, true, new Integer(2)),
                new ObjectScope(COLUMNID, new Integer(TABLEID)),
                new ObjectScope(DbGEPrimaryUnique.metaClass.getGUIName(false, false), PKID,
                        new Integer(TABLEID), true, false, true, null),
                new ObjectScope(DbGEForeign.metaClass.getGUIName(false, false), FKID, new Integer(
                        TABLEID), true, false, true, null),
                new ObjectScope(DbGEIndex.metaClass.getGUIName(false, false), INDEXID, new Integer(
                        TABLEID), true, false, true, null),
                new ObjectScope(DbGEProcedure.metaClass.getGUIName(false, false), PROCID, null,
                        true, true, true, new Integer(3), false),
                new ObjectScope(PROCCOLID, new Integer(PROCID)),
                new ObjectScope(DbGEView.metaClass.getGUIName(false, false), VIEWID, null, true,
                        true, true, new Integer(4), false),
                new ObjectScope(COLUMNVIEWID, new Integer(VIEWID)), };
    }

    protected Worker createReverseWorker(DBMSReverseOptions options) {
        Actions actions = createActions();
        if (actions == null)
            return null;
        return new JdbcReverseWorker(options, actions);
    }
    
    public boolean doListenSelection() { return (this instanceof PluginSelectionListener); }

}
