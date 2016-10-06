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

import org.modelsphere.jack.awt.SRSystemClipboard;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public class CopyImageAction extends CopyAction {

    boolean isNonWindowsPlatform = true;

    public CopyImageAction() {
        super(LocaleMgr.action.getString("copyImage"));

        try {// if not windows platform, disable this item, hide if possible
            String osName = System.getProperty("os.name");
            String graphsEnv = System.getProperty("java.awt.graphicsenv");
            if (osName.contains("Windows") && graphsEnv.contains("Win32")) // NOT LOCALIZABLE
                isNonWindowsPlatform = false;
        } catch (Exception e) {
            setVisibilityMode(VISIBILITY_DEFAULT);
            setEnabled(false);
            setVisible(false);
        }

        if (isNonWindowsPlatform == false)
            this.setMnemonic(LocaleMgr.action.getMnemonic("copyImage"));
    }

    protected final void doActionPerformed() {
        try {
            emptyClipboards();
            ApplicationDiagram diag = (ApplicationDiagram) ApplicationContext.getFocusManager()
                    .getFocusObject();
            org.modelsphere.jack.awt.SRSystemClipboard.setClipboardImage(diag.createImage(100,
                    SRSystemClipboard.transparentImageCopySupported()));
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    public final void updateSelectionAction() throws DbException {
        if (isNonWindowsPlatform) {
            setEnabled(false);
            setVisible(false);
        } else {
            setVisible(true);
            setEnabled(isApplicationDiagramHaveFocus()
                    && ApplicationContext.getFocusManager().getSelectedObjects().length != 0);
        }

    }
}
