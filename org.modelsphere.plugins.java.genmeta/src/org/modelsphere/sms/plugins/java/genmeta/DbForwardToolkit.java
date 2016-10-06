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

package org.modelsphere.sms.plugins.java.genmeta;

import java.io.File;

import javax.swing.JFileChooser;

import org.modelsphere.jack.srtool.forward.ForwardOptions;
import org.modelsphere.jack.srtool.forward.ForwardToolkitInterface;
import org.modelsphere.jack.srtool.forward.ForwardWorker;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.templates.BasicForwardToolkit;

final class DbForwardToolkit extends BasicForwardToolkit implements ForwardToolkitInterface {
    protected ForwardWorker createForwardWorker(ForwardOptions options) {
        ForwardWorker worker = new DbForwardWorker(options);
        return worker;
    } //end createForwardWorker() 

    public Class getForwardClass() {
        return DbForwardEngineeringPlugin.class;
    }

    /////////////////////////////////
    // OVERRIDES  BasicForwardToolkit
    private static File g_selectedDirectory = null; //last selected output directory property

    protected File selectActualDirectory(String defaultDirectory) {
        if (g_selectedDirectory == null) {
            g_selectedDirectory = new File(defaultDirectory);
        }

        JFileChooser fc = new JFileChooser(g_selectedDirectory);
        fc.setSelectedFile(g_selectedDirectory);
        fc.setDialogTitle("Select Meta generation directory");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        MainFrame mainFrame = MainFrame.getSingleton();
        String approveButtonText = "Select";
        int result = fc.showDialog(mainFrame, approveButtonText);
        File actualDirectory;
        if (result == JFileChooser.APPROVE_OPTION) {
            g_selectedDirectory = fc.getSelectedFile();
            actualDirectory = g_selectedDirectory;
        } else {
            actualDirectory = null;
        }

        return actualDirectory;
    } //end selectActualDirectory()

    public File getSelectedDirectory() {
        return g_selectedDirectory;
    }
    //
    /////////////////////////////////
} //end of DbForwardToolkit
