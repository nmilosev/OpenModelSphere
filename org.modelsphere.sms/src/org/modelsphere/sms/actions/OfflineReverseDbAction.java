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

package org.modelsphere.sms.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFileChooser;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.or.features.dbms.DBMSReverseOptions;
import org.modelsphere.sms.or.features.dbms.ReverseToolkitPlugin;

/**
 * 
 * Must be used in debug only
 * 
 */
public final class OfflineReverseDbAction extends AbstractApplicationAction {

    public OfflineReverseDbAction() {
        super("Offline DBMS Reverse Engineering..."); // NOT LOCALIZABLE, hidden
        // feature
        this.setEnabled(true);
    }

    protected final void doActionPerformed() {
        JFileChooser chooser = new JFileChooser(ApplicationContext.getDefaultWorkingDirectory());

        ArrayList filters = ReverseToolkitPlugin.getExtensionFileFilters();
        Iterator iter = filters.iterator();
        while (iter.hasNext()) {
            ExtensionFileFilter filter = (ExtensionFileFilter) iter.next();
            chooser.addChoosableFileFilter(filter);
        }

        // chooser.setFileFilter(smsFileFilter);
        chooser.setDialogTitle(LocaleMgr.screen.getString("open"));
        int retval = chooser.showOpenDialog(ApplicationContext.getDefaultMainFrame());
        File theFile = chooser.getSelectedFile();
        if (retval == JFileChooser.APPROVE_OPTION && theFile != null) {
            String fileName = theFile.getAbsolutePath();
            String extension = ExtensionFileFilter.getExtension(theFile);

            ReverseToolkitPlugin kit = ReverseToolkitPlugin.getToolkitForExtension(extension);

            DBMSReverseOptions options = new DBMSReverseOptions();
            options.targetSystemId = kit.getTargetSystemIdForExtension(extension);
            options.fromExtractFile = true;
            options.extractFilename = fileName;
            options.reverseObjectUser = true;
            options.synchro = false;
            Object specificOptions = options.getSpecificDBMSOptions();
            if (specificOptions == null)
                options.setSpecificDBMSOptions(kit.createSpecificOptions());
            else {
                Object temp = kit.createSpecificOptions();
                if ((temp == null)
                        || (temp != null && !temp.getClass().isInstance(specificOptions)))
                    options.setSpecificDBMSOptions(temp);
            }
            kit.doReverse(options);
        }
    }
}
