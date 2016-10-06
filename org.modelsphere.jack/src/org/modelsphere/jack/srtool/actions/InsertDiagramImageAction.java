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

import java.awt.event.ActionEvent;
import javax.swing.*;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.awt.ImagePreviewer;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DbGraphic;
import org.modelsphere.jack.srtool.international.LocaleMgr;

import java.awt.*;

public final class InsertDiagramImageAction extends AbstractApplicationAction implements
        SelectionActionListener {

    transient private static String defaultDirectory = ApplicationContext
            .getDefaultWorkingDirectory();

    public InsertDiagramImageAction() {
        super(LocaleMgr.action.getString("InsertDiagramImage"));
    }

    protected final void doActionPerformed() {
        doActionPerformed((ActionEvent) null);
    }

    protected final void doActionPerformed(ActionEvent evt) {
        try {
            ApplicationDiagram diag = (ApplicationDiagram) (ApplicationContext.getFocusManager()
                    .getFocusObject());
            Point clickPosition = getDiagramLocation(evt);
            if (clickPosition == null)
                clickPosition = GraphicUtil.rectangleGetCenter(diag.getMainView().getViewRect());
            JFileChooser chooser = new JFileChooser(defaultDirectory);
            chooser.setAccessory(new ImagePreviewer(chooser));
            chooser.addChoosableFileFilter(ExtensionFileFilter.allImagesFilter);
            int retval = chooser.showDialog(ApplicationContext.getDefaultMainFrame(), null);
            if (retval == JFileChooser.APPROVE_OPTION) {
                java.io.File theFile = chooser.getSelectedFile();
                defaultDirectory = theFile.getParent();
                if (theFile != null) {
                    Image image = null;
                    String imageName = "file:" + theFile.getAbsolutePath(); //NOT LOCALIZABLE
                    java.net.URL url = new java.net.URL(imageName);
                    image = java.awt.Toolkit.getDefaultToolkit().getImage(url);
                    org.modelsphere.jack.graphic.GraphicUtil.waitForImage(image);

                    ImageIcon icon = new ImageIcon(image);
                    int imageHeigth = icon.getIconHeight();
                    int imageWidth = icon.getIconWidth();

                    DbObject dbO = (DbObject) diag.getDiagramGO();
                    Db db = (Db) dbO.getDb();
                    db.beginTrans(Db.WRITE_TRANS, LocaleMgr.action.getString("InsertDiagramImage"));

                    DbObject imageGo = dbO.createComponent(DbGraphic.fImageGoDiagramImage
                            .getMetaClass());
                    imageGo.set(DbGraphic.fImageGoDiagramImage, image);
                    imageGo.set(DbGraphic.fGraphicalObjectRectangle, new Rectangle(clickPosition.x
                            - (imageWidth / 2), clickPosition.y - (imageHeigth / 2), imageWidth,
                            imageHeigth));
                    db.commitTrans();
                }
            }
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    public final void updateSelectionAction() throws org.modelsphere.jack.baseDb.db.DbException {
        this.setEnabled(this.isApplicationDiagramHaveFocus());
    }
}
