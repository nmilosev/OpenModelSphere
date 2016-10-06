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

package org.modelsphere.jack.srtool.reverse.file;

import java.util.*;

import org.modelsphere.jack.awt.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.gui.task.DefaultController;
import org.modelsphere.jack.plugins.*;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public abstract class ReversePlugins {
    private static final String ERROR_DETAILS = org.modelsphere.jack.international.LocaleMgr.message
            .getString("errorDetails");
    private static final String title = LocaleMgr.message.getString("ReverseEngineering...");
    private static final String jobDescription = ApplicationContext.getLogPath()
            + System.getProperty("file.separator") + "javareverse.log"; // NOT LOCALIZABLE
    private static final String ALL_SUPPORTED_TYPES = LocaleMgr.screen
            .getString("AllSupportedTypes");

    // Returns the list of Reverse classes.
    public static final ArrayList getReverseList() {
        PluginMgr mgr = PluginMgr.getSingleInstance();
        List reverseList = mgr.getPluginsRegistry().getPluginClasses(
                JackReverseEngineeringPlugin.class);
        return new ArrayList(reverseList);
    }

    // Returns the list of Reverse objects.
    public final ArrayList getReverseObjectList() {
        PluginMgr mgr = PluginMgr.getSingleInstance();
        ArrayList reverseObjectList = mgr.getPluginsRegistry().getActivePluginInstances(
                JackReverseEngineeringPlugin.class);
        return reverseObjectList;
    }

    public final void setFilters(Vector filterVector, SourceChooser fileChooser) {
        Vector extVector = new Vector();
        ArrayList reverseList = getReverseObjectList();
        Iterator iter = reverseList.iterator();

        while (iter.hasNext()) {
            JackReverseEngineeringPlugin reverse = (JackReverseEngineeringPlugin) iter.next();
            String ext = reverse.getExtension();
            String def = reverse.getDisplayName();
            extVector.addElement(ext);
            ExtensionFileFilter filter = new ExtensionFileFilter(new String[] { ext }, def);
            filterVector.addElement(filter);
            fileChooser.addChoosableFileFilter(filter);
        } //end while

        String[] extList = new String[extVector.size()];
        for (int i = 0; i < extVector.size(); i++) {
            extList[i] = (String) extVector.elementAt(i);
        } //end for

        ExtensionFileFilter filter = new ExtensionFileFilter(extList, ALL_SUPPORTED_TYPES);
        filterVector.addElement(filter);
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setFileFilter(filter);
    }

    protected abstract ReverseTask getReverseTask(Vector aFileVector, DbProject proj);

    public final void reverseFiles(Vector aFileVector, DbProject proj, AbstractReverser reverser)
            throws DbException {
        Controller controller = new DefaultController(title, true, jobDescription);
        ArrayList reverseList = getReverseList();
        ReverseOptions options = new ReverseOptions(aFileVector, proj, reverseList, reverser);
        ReverseWorker worker = new ReverseWorker(options);
        controller.start(worker);
    } //end reverseFiles()

}
