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
import java.awt.Image;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.awt.*;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public class SaveClipboardImageAction extends AbstractApplicationAction implements
        SelectionActionListener, SRSystemClipboardListener {

    public SaveClipboardImageAction() {
        super(LocaleMgr.action.getString("saveClipboardImage"));
        SRSystemClipboard.addSystemClipboardListener(this);
    }

    protected final void doActionPerformed() {
        Image image = SRSystemClipboard.getClipboardImage();
        if (image != null) {
            try {
                ExtensionFileFilter[] optionalFilters = null;
                AwtUtil.FileAndFilter selection = AwtUtil.showSaveAsDialog(ApplicationContext.getDefaultMainFrame(),
                        LocaleMgr.action.getString("saveClipboardImage"),
                        ExtensionFileFilter.jpgFileFilter, optionalFilters, null);
                File file = (selection == null) ? null : selection.getFile(); 
                
                if (file != null)
                    GraphicUtil
                            .saveImageToJPEGFile(GraphicUtil.toBufferedImage(image), file, 0.90f);
            } catch (Exception ex) {
                org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                        ApplicationContext.getDefaultMainFrame(), ex);
            }
        }
    }

    public final void updateSelectionAction() {
        systemClipboardContentTypeChanged();
    }

    @Override
    public void systemClipboardContentTypeChanged() {
        setEnabled(SRSystemClipboard.containsImage());
    }
}
