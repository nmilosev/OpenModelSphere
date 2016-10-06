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

/*
 * Created on Nov 30, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.modelsphere.sms.plugins.statistics;

/**
 * @author Grandite
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

import java.awt.Dimension;
import java.io.File;
import java.io.FileWriter;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.modelsphere.jack.debug.ConceptPair;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.FocusManager;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.util.ExceptionMessage;

public class DiagramStatisticsSaveXML {

    private static final String PROLOG = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\"?>";
    private static final String SELECT = LocaleMgr.screen.getString("Select");
    private static final String SELECT_OUTPUT_DIR = LocaleMgr.message
            .getString("SelectAnOutputDirectory");
    private static final String ERROR = LocaleMgr.message.getString("Error");
    private static final String MSG_PATTERN = LocaleMgr.message
            .getString("successfullyCreatedPattern");
    private static final String SUCCESS = LocaleMgr.message.getString("Success");
    private static final String NO_OBJECT_SELECTED = LocaleMgr.message
            .getString("NoObjectSelected");
    private static final String XML = "xml"; //NOT LOCALIZABLE, file extension
    private static final String FILENAME = "DiagramStatistics.xml"; //NOT LOCALIZABLE, filename

    private DiagramStatisticsPlugin dgmStatsProvider;

    private static File g_previousFolder = null;

    public DiagramStatisticsSaveXML(DiagramStatisticsPlugin dgmStatsProvider) {
        this.dgmStatsProvider = dgmStatsProvider;
    }

    protected final void save() throws ExceptionMessage {

        if (dgmStatsProvider == null) {
            throw new RuntimeException("No statistics provider was specified.");
        }

        JFrame frame = ApplicationContext.getDefaultMainFrame();
        FocusManager manager = ApplicationContext.getFocusManager();

        if (g_previousFolder == null) {
            g_previousFolder = new File(ApplicationContext.getDefaultWorkingDirectory());
        }

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
                } //end if
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

                String filename = selectedFile.getName();
                File file = new File(selectedFile.getParentFile().getAbsolutePath(), filename);
                FileWriter filewriter = new FileWriter(file);
                filewriter.write(PROLOG);

                ////
                // write the provider data to disk in XML format

                int type = dgmStatsProvider.getType();
                String sType = "";

                switch (type) {
                case DiagramStatisticsPlugin.DIAGRAM_TYPE_OO_UML_CLASS:
                    sType += "Class";
                    break;
                case DiagramStatisticsPlugin.DIAGRAM_TYPE_BE_UML_USECASE:
                    sType += "Use Case";
                    break;
                case DiagramStatisticsPlugin.DIAGRAM_TYPE_BE_UML_STATECHART:
                    sType += "Statechart";
                    break;
                case DiagramStatisticsPlugin.DIAGRAM_TYPE_BE_UML_PROCESS:
                    sType += "Process";
                    break;
                case DiagramStatisticsPlugin.DIAGRAM_TYPE_BE_UML_COLLABORATION:
                    sType += "Collaboration";
                    break;
                case DiagramStatisticsPlugin.DIAGRAM_TYPE_BE_UML_ACTIVITY:
                    sType += "Activity";
                    break;
                case DiagramStatisticsPlugin.DIAGRAM_TYPE_BE_UML_SEQUENCE:
                    sType += "Sequence";
                    break;
                case DiagramStatisticsPlugin.DIAGRAM_TYPE_BE_UML_COMPONENT:
                    sType += "Component";
                    break;
                case DiagramStatisticsPlugin.DIAGRAM_TYPE_BE_UML_DEPLOYMENT:
                    sType += "Deployment";
                    break;
                case DiagramStatisticsPlugin.DIAGRAM_TYPE_OR_DATA:
                    sType += "Data";
                    break;
                case DiagramStatisticsPlugin.DIAGRAM_TYPE_OR_DOMAIN:
                    sType += "Domain";
                    break;
                case DiagramStatisticsPlugin.DIAGRAM_TYPE_OR_COMMONITEMS:
                    sType += "Common Item";
                    break;
                }

                String rootNode = "<Diagram Type =\"" + sType + "\">\n";
                filewriter.write(rootNode);

                String sNotation = "<Notation>" + dgmStatsProvider.getNotation() + "</Notation>\n";
                filewriter.write(sNotation);

                String sDefaultStyle = "<DefaultStyle>" + dgmStatsProvider.getDefaultStyle()
                        + "</DefaultStyle>\n";
                filewriter.write(sDefaultStyle);

                Dimension dim = dgmStatsProvider.getDimensions();
                String sDimensions = "<Dimensions Height=\"" + dim.getHeight() + "\" " + "Width=\""
                        + dim.getWidth() + "\"" + "/>\n";
                filewriter.write(sDimensions);

                String sNumberofSheets = "<NumberofSheets>" + dgmStatsProvider.getSheetsCount()
                        + "</NumberofSheets>\n";
                filewriter.write(sNumberofSheets);

                filewriter.flush();

                ////
                // common concepts

                filewriter.write("<CommonConcepts>\n");
                Vector results = dgmStatsProvider.getResults();
                Enumeration enumResults = results.elements();
                while (enumResults.hasMoreElements()) {
                    ConceptPair pair = (ConceptPair) enumResults.nextElement();
                    String name = pair.getConceptName();
                    int nCount = pair.getOccurrencesCount();
                    if (!pair.IsSpecialized()) {
                        filewriter.write("<Concept Name=\"" + name + "\" " + "Count=\"" + nCount
                                + "\"/>\n");
                    }
                }
                filewriter.write("</CommonConcepts>\n");

                filewriter.flush();

                ////
                // specialized concepts

                filewriter.write("<SpecializedConcepts>\n");
                enumResults = results.elements();
                while (enumResults.hasMoreElements()) {
                    ConceptPair pair = (ConceptPair) enumResults.nextElement();
                    String name = pair.getConceptName();
                    int nCount = pair.getOccurrencesCount();
                    if (pair.IsSpecialized()) {
                        filewriter.write("<Concept Name=\"" + name + "\" " + "Count=\"" + nCount
                                + "\"/>\n");
                    }
                }
                filewriter.write("</SpecializedConcepts>\n");
                filewriter.write("</Diagram>\n");

                filewriter.flush();

                filewriter.close();

                String msg = MessageFormat.format(MSG_PATTERN, new Object[] { filename,
                        selectedFile.getParentFile().getAbsolutePath() });
                JOptionPane.showMessageDialog(frame, msg, SUCCESS, JOptionPane.INFORMATION_MESSAGE);
                g_previousFolder = selectedFile.getParentFile(); //Remember selected folder for the next time
            } catch (Exception e) {
                throw new ExceptionMessage("Failed to save statistics to disk. Operation aborted.",
                        ExceptionMessage.E_ERROR, ExceptionMessage.L_NORMAL);

            } //end try
        } //end if
    } //end doActionPerformed()

} //end ExportToXMLAction

