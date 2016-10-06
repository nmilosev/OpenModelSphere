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
import java.text.MessageFormat;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.xml.XMLBuilder;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.FocusManager;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;

public class ExportToXMLAction extends AbstractApplicationAction {
    private static final String SELECT = LocaleMgr.screen.getString("Select");
    private static final String SELECT_OUTPUT_DIR = LocaleMgr.message
            .getString("SelectAnOutputDirectory");
    private static final String ERROR = LocaleMgr.message.getString("Error");
    private static final String MSG_PATTERN = LocaleMgr.message
            .getString("successfullyCreatedPattern");
    private static final String SUCCESS = LocaleMgr.message.getString("Success");
    private static final String NO_OBJECT_SELECTED = LocaleMgr.message
            .getString("NoObjectSelected");
    private static final String XML = "xml"; // NOT LOCALIZABLE, file extension
    private static final String FILENAME = "export.xml"; // NOT LOCALIZABLE,

    // filename

    public ExportToXMLAction() {
        super(LocaleMgr.action.getString("ExportToXMLAction"), LocaleMgr.action
                .getImageIcon("ExportToXMLAction"));
        this.setEnabled(true);
        this.setVisible(ScreenPerspective.isFullVersion()); 
    }

    private static File g_previousFolder = null;

    protected final void doActionPerformed() {
        JFrame frame = ApplicationContext.getDefaultMainFrame();
        FocusManager manager = ApplicationContext.getFocusManager();
        DbObject[] semObjs = manager.getSelectedSemanticalObjects();

        if (semObjs.length != 1) {
            String msg = NO_OBJECT_SELECTED;
            JOptionPane.showMessageDialog(frame, msg, ERROR, JOptionPane.ERROR_MESSAGE);
            return;
        } // end if

        if (g_previousFolder == null) {
            g_previousFolder = new File(ApplicationContext.getDefaultWorkingDirectory());
        }

        // File selectedFolder = IoUtil.selectDirectory(frame,
        // g_previousFolder.getAbsolutePath(), SELECT_OUTPUT_DIR, SELECT);
        File selectedFile = null;
        JFileChooser chooser = new JFileChooser(g_previousFolder);
        FileFilter filter = new FileFilter() {
            public String getDescription() {
                return XML;
            }

            public boolean accept(File file) {
                boolean doAccept = false;
                if (file.isDirectory()) {
                    return true;
                }

                String name = file.getName();
                int idx = name.indexOf('.');
                if (idx > -1) {
                    String ext = name.substring(idx + 1);
                    if (ext.toLowerCase().equals(XML)) {
                        doAccept = true;
                    }
                } // end if
                return doAccept;
            }
        };
        chooser.setFileFilter(filter);
        chooser.setSelectedFile(new File(g_previousFolder, FILENAME));
        int result = chooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
        }

        if (selectedFile != null) {
            try {
                XMLBuilder builder = XMLBuilder.singleton;
                DbObject dbObj = ApplicationContext.getFocusManager()
                        .getSelectedSemanticalObjects()[0];
                DbProject project = dbObj.getProject(); // Export the whole
                // project
                dbObj.getDb().beginTrans(Db.READ_TRANS);
                String filename = selectedFile.getName();
                builder.toXML(project, selectedFile.getParentFile().getAbsolutePath(), filename);
                dbObj.getDb().commitTrans();
                String msg = MessageFormat.format(MSG_PATTERN, new Object[] { filename,
                        selectedFile.getParentFile().getAbsolutePath() });
                JOptionPane.showMessageDialog(frame, msg, SUCCESS, JOptionPane.INFORMATION_MESSAGE);
                g_previousFolder = selectedFile.getParentFile(); // Remember
                // selected
                // folder
                // for the
                // next time
            } catch (Exception e) {
                e.printStackTrace();
            } // end try
        } // end if
    } // end doActionPerformed()

} // end ExportToXMLAction

