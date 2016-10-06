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

import org.modelsphere.jack.gui.wizard.WizardModel;
import org.modelsphere.jack.gui.wizard.WizardPage;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.db.DbSMSAbstractPackage;
import org.modelsphere.sms.or.international.LocaleMgr;

/**
 * 
 * Forward wizard model
 * 
 */
public final class DBMSDefaultForwardWizardModel implements WizardModel {
    // Default Page Id for DBMS Forward Eng pages
    public static final int PAGE_FIRST = 0;
    public static final int PAGE_OBJECTS_SELECTION = 2;

    private static final String TARGET_SYSTEM_NOT_SET = LocaleMgr.screen
            .getString("TargetSystemNotSet");
    private static final String NOT_LOADED_PATTERN = LocaleMgr.screen.getString("NotLoadedPattern");

    private DBMSForwardOptions options = new DBMSForwardOptions();

    public DBMSDefaultForwardWizardModel(int tsId) throws InstantiationException {

        if (tsId == -1) {
            throw new InstantiationException(TARGET_SYSTEM_NOT_SET);
        }

        options.setTargetSystemId(tsId);
        ForwardToolkitPlugin.setActiveDiagramTarget(options.getTargetSystemId());
        ForwardToolkitPlugin toolkit = ForwardToolkitPlugin.getToolkit();
        try {
            toolkit.initializeObjectsScope(options);
        } catch (NoClassDefFoundError err) {
            String title = toolkit.getTitle();
            String msg = MessageFormat.format(NOT_LOADED_PATTERN, new Object[] { title });
            throw new InstantiationException(msg);
        }
    }

    public void setAbstractPackage(DbSMSAbstractPackage pack) {
        options.setAbstractPackage(pack);
    }

    public void setDatabaseName(String dbName) {
        options.databaseName = dbName;
    }

    public int[] getPagesSequence() {
        return new int[] { PAGE_FIRST, PAGE_OBJECTS_SELECTION };
    }

    public WizardPage getPage(int pageid) {
        ForwardToolkitPlugin toolkit = ForwardToolkitPlugin.getToolkit();
        WizardPage page = toolkit.getPage(pageid);
        if (page == null) {
            String msg = "Toolkit returns null page"; // NOT LOCALIZABLE,
            // exception message
            throw new RuntimeException(msg);
        }
        return page;
    }

    public final String getTitle(int pageid) {
        return ForwardToolkitPlugin.getToolkit().getTitle();
    }

    public final Object getConfiguration() {
        return options;
    }

    public final boolean beforePageChange(int pageid) {
        return ForwardToolkitPlugin.getToolkit().beforePageChange(pageid, options);
    }

    public final boolean afterPageChange(int pageid) {
        return ForwardToolkitPlugin.getToolkit().afterPageChange(pageid, options);
    }

    public final void cancel() {
        options = null;
    }

    public final void finish() {
        ForwardToolkitPlugin toolkit = ForwardToolkitPlugin.getToolkit();
        toolkit.doForward(options);
        options = null;
    }

}
