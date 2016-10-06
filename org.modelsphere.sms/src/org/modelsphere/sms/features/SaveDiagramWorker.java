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

package org.modelsphere.sms.features;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.gui.task.DefaultController;
import org.modelsphere.jack.gui.task.Worker;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DiagramInternalFrame;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.SMSToolkit;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.international.LocaleMgr;

public class SaveDiagramWorker extends Worker {

    private static final String kDiagramTooBigToBeSaved = LocaleMgr.message
            .getString("DiagramTooBigToBeSaved");

    private SaveDiagramOptions m_options;

    public SaveDiagramWorker(SaveDiagramOptions options) {
        if (options == null)
            throw new NullPointerException();

        m_options = options;
    }

    protected void runJob() throws Exception {
        /*
         * DbObject[] objects = m_options.getObjects(); ArrayList generatedFiles = new ArrayList();
         * DbObject firstObj = objects[0]; Db db = firstObj.getDb(); db.beginTrans(Db.READ_TRANS);
         * 
         * int nb = objects.length; for (int i=0; i<nb; i++) { DbObject semObj = objects[i];
         * 
         * if(semObj instanceof DbSMSDiagram){ System.out.println("Diagram: "+semObj); } } //end for
         * 
         * db.commitTrans();
         */

        // Object focusObject =
        // ApplicationContext.getFocusManager().getFocusObject();
        Object[] objects = m_options.getObjects();

        /*
         * if(focusObject instanceof ApplicationDiagram){ objects = new Object[1]; objects[0] =
         * focusObject; } else{ objects =
         * ApplicationContext.getFocusManager().getSelectedSemanticalObjects(); }
         */

        List<String> generatedFiles = new ArrayList<String>();
        // DbObject firstObj = (DbObject)objects[0];
        // Db db = firstObj.getDb();
        // db.beginTrans(Db.READ_TRANS);

        int nb = objects.length;
        for (int i = 0; i < nb; i++) {
            if (objects[i] instanceof DbSMSDiagram) {
                /*
                 * new org.modelsphere.sms.plugins.html.JpegFileCreator((DbSMSDiagram )objects[i]);
                 */
            	exportToImage((DbSMSDiagram) objects[i], m_options.getDialog().getScale(),
                        m_options.getDialog().getQuality(), m_options.getFilter(), generatedFiles);
            } else if (objects[i] instanceof ApplicationDiagram) {
                exportToImage((ApplicationDiagram) objects[i], m_options.getDialog().getScale(),
                        m_options.getDialog().getQuality(), m_options.getFilter(), generatedFiles);
            }
        } // end for

        // db.commitTrans();
    }

    // Return this job's title
    protected String getJobTitle() {
        return LocaleMgr.screen.getString("SaveDiagram");
    }

    // Use when action is performed from Explorer
    private void exportToImage(DbSMSDiagram diagram, int scale, float quality,
            ExtensionFileFilter filter, List<String> generatedFiles) throws Exception {
        SaveDiagramDialog dialog = m_options.getDialog();
        String file;
        int[] pages;
        DiagramInternalFrame diagramInternalFrame;
        ApplicationDiagram appDiagram;
        boolean deleteApplicationDiagram = false;

        // javax.swing.JOptionPane.showMessageDialog(MainFrame.getSingleton(),
        // diagram);

        diagram.getDb().beginTrans(Db.READ_TRANS);

        // there is no need to continue if the diagram is empty
        // unless we would like to write en empty file
        if (diagram.getComponents().size() == 0)
            return;

        diagramInternalFrame = MainFrame.getSingleton().getDiagramInternalFrame(diagram);

        if (diagramInternalFrame == null) {
            DbSemanticalObject so = (DbSemanticalObject) diagram.getComposite();
            SMSToolkit kit = SMSToolkit.getToolkit(so);

            appDiagram = new ApplicationDiagram(so, diagram, kit.createGraphicalComponentFactory(),
                    ApplicationContext.getDefaultMainFrame().getDiagramsToolGroup());
            // file = new File(diagram.getName()+jpegExtension);

            deleteApplicationDiagram = true;
        } else {
            appDiagram = diagramInternalFrame.getDiagram();
        }

        saveImageToFile(appDiagram, scale, quality, filter, generatedFiles);

        if (deleteApplicationDiagram)
            appDiagram.delete();

        diagram.getDb().commitTrans();
    }

    // Use when action is performed from diagramGO
    private void exportToImage(ApplicationDiagram appDiagram, int scale, float quality, ExtensionFileFilter filter,
            List<String> generatedFiles) throws Exception {
        SaveDiagramDialog dialog = m_options.getDialog();
        String file;
        int[] pages;

        appDiagram.getDiagramGO().getDb().beginTrans(Db.READ_TRANS);

        // there is no need to continue if the diagram is empty
        // unless we would like to write en empty file
        if (appDiagram.getDiagramGO().getComponents().size() == 0)
            return;

        saveImageToFile(appDiagram, scale, quality, filter, generatedFiles);

        appDiagram.getDiagramGO().getDb().commitTrans();
    }

    private void saveImageToFile(ApplicationDiagram appDiagram, int scale, float quality,
            ExtensionFileFilter filter, List<String> generatedFiles) throws Exception {
        SaveDiagramDialog dialog = m_options.getDialog();
        String filename;
        int[] pages;
        DefaultController controller = (DefaultController) getController();

        try {
            if (dialog.isMultiple()) {
                pages = new int[dialog.getLastPage() - dialog.getFirstPage() + 1];
                for (int i = 0; i < pages.length; i++) {
                    pages[i] = dialog.getFirstPage() + i;
                }
            } else {
                pages = new int[] { -1 };
            }

            for (int i = 0; i < pages.length && this.getController().checkPoint(); i++) {
                Image image;

                if (pages[i] == -1) {
                    image = appDiagram.createImage(null, scale);
                    filename = dialog.getFile();
                } else {
                    image = appDiagram.createImage(pages[i] - 1, scale);
                    filename = dialog.getFile().substring(0, dialog.getFile().lastIndexOf('.'))
                            + "_"
                            + pages[i]
                            + dialog.getFile().substring(dialog.getFile().lastIndexOf('.'),
                                    dialog.getFile().length());
                }

                if (image != null) {
                	File file = new File(filename); 
                	BufferedImage bi = GraphicUtil.toBufferedImage(image);
                	
                	if (ExtensionFileFilter.jpgFileFilter.equals(filter)) {
                		GraphicUtil.saveImageToJPEGFile(bi, file, quality / 100f);
                	} else {
                		GraphicUtil.saveImageToPNGFile(bi, file);
                	}
                	
                    generatedFiles.add(filename);
                    controller.println(filename.toString());
                }
            }
        } catch (OutOfMemoryError er) {
            controller.incrementErrorsCounter();
            String msg = MessageFormat.format(kDiagramTooBigToBeSaved, new Object[] { appDiagram
                    .getDiagramGO().getName() });
            controller.println(msg);
        }
    }
}
