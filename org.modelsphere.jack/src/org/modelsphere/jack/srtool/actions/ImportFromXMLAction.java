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

import java.io.File;
import javax.swing.JFileChooser;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.xml.imports.XmlFileOpener;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;

public class ImportFromXMLAction extends AbstractApplicationAction {

    public ImportFromXMLAction() {
        super(LocaleMgr.action.getString("ImportFromXMLAction"), LocaleMgr.action
                .getImageIcon("ImportFromXMLAction"));
        this.setEnabled(true);
        this.setVisible(ScreenPerspective.isFullVersion()); 
    }

    private static File g_currentDir = null;

    protected final void doActionPerformed() {
        Db db = null;
        Object[] selObjs = ApplicationContext.getFocusManager().getSelectedObjects();
        if (selObjs != null && selObjs.length == 1 && selObjs[0] instanceof Db)
            db = (Db) selObjs[0];

        // same stategy as in OpenAction; is it correct? [MS]
        if (db == null) {
            db = new DbRAM();
        }

        DefaultMainFrame mainFrame = ApplicationContext.getDefaultMainFrame();
        JFileChooser chooser;
        if (g_currentDir == null) {
            chooser = new JFileChooser();
        } else {
            chooser = new JFileChooser(g_currentDir);
        } // end if

        chooser.setFileFilter(ExtensionFileFilter.xmlFileFilter);
        int returnVal = chooser.showOpenDialog(mainFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            g_currentDir = file.getParentFile();
            XmlFileOpener opener = XmlFileOpener.getSingleton();
            String filename = file.getAbsolutePath();
            opener.doOpenFile(db, filename, mainFrame);
        } // end if
    } // end doActionPerformed()
} // end ImportFromXMLAction

