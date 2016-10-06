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

package org.modelsphere.sms.plugins.report;

// JDK
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DiagramInternalFrame;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.StringUtil;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.SMSToolkit;
import org.modelsphere.sms.db.DbSMSDiagram;

public class JpegGenerator {

    private Controller controller;
    private static final String jpegExtension = "."
            + ExtensionFileFilter.jpgFileFilter.getExtension(); // NOT LOCALIZABLE

    private File generatedFile = null; // NOT LOCALIZABLE
    private DbSMSDiagram diagram;
    private File outputDirectory;

    public JpegGenerator(DbSMSDiagram diagram, File outputDirectory, Controller controller)
            throws DbException {
        this.diagram = diagram;
        this.outputDirectory = outputDirectory;
        this.controller = controller;

    }

    public void generate() throws DbException {
        exportToJPEG(diagram, 100, 100f);
    }

    private void exportToJPEG(DbSMSDiagram diagram, int scale, float quality) throws DbException {
        //String               htmlDirectory = DirectoryOptionGroup.getHTMLGenerationDirectory();
        File file;
        DiagramInternalFrame diagramInternalFrame;
        ApplicationDiagram appDiagram;
        boolean deleteApplicationDiagram = false;

        // there is no need to continue if the diagram is empty
        // unless we would like to write en empty file
        if (diagram.getComponents().size() == 0)
            return;

        diagramInternalFrame = MainFrame.getSingleton().getDiagramInternalFrame(diagram);

        if (diagramInternalFrame == null) {
            DbSemanticalObject so = (DbSemanticalObject) diagram.getComposite();
            SMSToolkit kit = SMSToolkit.getToolkit(so);

            appDiagram = new ApplicationDiagram(so, diagram, kit.createGraphicalComponentFactory(),
                    MainFrame.getSingleton().getDiagramsToolGroup());

            deleteApplicationDiagram = true;
        } else {
            appDiagram = diagramInternalFrame.getDiagram();
        }
        file = new File(outputDirectory, StringUtil.getValideFileName(diagram.getComposite()
                .getName()
                + "_" + diagram.getName() + jpegExtension, true)); //NOT LOCALIZABLE

        try {
            Image image = appDiagram.createImage(null, scale);
            if (image != null) {
                GraphicUtil.saveImageToJPEGFile(GraphicUtil.toBufferedImage(image), file,
                        quality / 100f);
                generatedFile = new File(file.getName());
            }
        } catch (IOException ioe) {
            controller.println(ioe.getMessage());
        } catch (OutOfMemoryError er) {
            controller.println(MessageFormat.format(LocaleMgr.misc
                    .getString("DiagramTooBigToBeSaved"), new Object[] { appDiagram.getDiagramGO()
                    .getName() }));
        }

        if (deleteApplicationDiagram)
            appDiagram.delete();
    }

    public File getGeneratedFile() {
        return generatedFile;
    }
}
