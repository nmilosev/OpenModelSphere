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
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.services.ConnectionMessage;

/**
 * 
 * Generic wizard panel
 * 
 */
public final class DBMSDefaultReverseWizardModel implements WizardModel {
    // Default Page Id for DBMS Reverse Eng pages
    public static final int PAGE_FIRST = 0;
    public static final int PAGE_OPTIONS = 1;
    public static final int PAGE_OBJECTS_SELECTION = 2;

    private ConnectionMessage connectionMessage;
    private DBMSReverseOptions options = new DBMSReverseOptions();

    public DBMSDefaultReverseWizardModel(ConnectionMessage cm) {
        if (cm == null) // no actual support for offline reverse
            throw new NullPointerException("Null Connection");
        options.synchro = false;
        options.root = ApplicationContext.getFocusManager().getCurrentProject(); // can
        // be
        // change
        // latter
        // in
        // GUI
        // --
        // default
        // value
        setConnectionMessage(cm);
        ReverseToolkitPlugin.setActiveDiagramTarget(-1);

        if (connectionMessage == null)
            options.targetSystemId = -1;
        else {
            ReverseToolkitPlugin tk = ReverseToolkitPlugin.getToolkit();
            options.targetSystemId = tk.getTargetSystemId(connectionMessage.databaseProductVersion);
        }
    }

    private void setConnectionMessage(ConnectionMessage cm) {
        connectionMessage = cm;
        options.setConnection(connectionMessage);
        ReverseToolkitPlugin.setConnection(cm);
        if (connectionMessage != null) {
            ReverseToolkitPlugin kit = ReverseToolkitPlugin.getToolkit();
            Object specificOptions = options.getSpecificDBMSOptions();
            if (specificOptions == null)
                options.setSpecificDBMSOptions(kit.createSpecificOptions());
            else {
                Object temp = kit.createSpecificOptions();
                if ((temp == null)
                        || (temp != null && !temp.getClass().isInstance(specificOptions)))
                    options.setSpecificDBMSOptions(temp);
            }
            if (options.getObjectsScope() == null)
                options.setObjectsScope(kit.createObjectsScope());
        }
    }

    public int[] getPagesSequence() {
        return new int[] { PAGE_FIRST, PAGE_OPTIONS, PAGE_OBJECTS_SELECTION };
    }

    public WizardPage getPage(int pageid) {
        ReverseToolkitPlugin kit = ReverseToolkitPlugin.getToolkit();
        WizardPage page = kit.createWizardPage(pageid, options.synchro);
        return page;
    }

    public final String getTitle(int pageid) {
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
        connectionMessage = null;
        options = null;
    }

    public final void finish() {
        ReverseToolkitPlugin kit = ReverseToolkitPlugin.getToolkit();
        kit.doReverse(options);
        connectionMessage = null;
        options = null;
    }

}
