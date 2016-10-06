/*************************************************************************

Copyright (C) 2009 by neosapiens inc.

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

You can reach neosapiens inc. at: 

neosapiens inc.
1236 Gaudias-Petitclerc
Qu&eacute;bec, Qc, G1Y 3G2
CANADA
Telephone: 418-561-8403
Fax: 418-650-2375
http://www.neosapiens.com/
Email: marco.savard@neosapiens.com
       gino.pelletier@neosapiens.com

 **********************************************************************/
package com.neosapiens.plugins.reverse.javasource.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.text.MessageFormat;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.modelsphere.jack.gui.task.Controller;
import com.neosapiens.plugins.reverse.javasource.ReverseJavaSourcecodeParameters;
import com.neosapiens.plugins.reverse.javasource.international.JavaSourceReverseLocaleMgr;

public class ReverseJavaSourcecodeWizardPage2 extends WizardPage {

    private static final long serialVersionUID = 1L;
    private JLabel progressLabel;
    private JLabel nbFilesLabel;
    private JLabel totalSizeLabel;
    private JLabel resultLabel;
    private JCheckBox createDiagramsBox, createFieldsBox, createMethodsBox;
    
    
    public ReverseJavaSourcecodeWizardPage2(Wizard<ReverseJavaSourcecodeParameters> wizard) {
        super(wizard);
        setLayout(new GridBagLayout());
        initContents();
    }
    
    
    private void initContents() {
        int y = 0;
        int h = 1;
        double wx = 0.0, wy = 0.0;
        int nofill = GridBagConstraints.NONE;

        //add label
        progressLabel = new JLabel();
        this.add(progressLabel, new GridBagConstraints(0, y, 2, h, wx, wy,
                GridBagConstraints.NORTHWEST, nofill, new Insets(6, 6, 0, 3), 0, 0));

        y++;

        //add label
        nbFilesLabel = new JLabel();
        this.add(nbFilesLabel, new GridBagConstraints(0, y, 2, h, wx, wy,
                GridBagConstraints.NORTHWEST, nofill, new Insets(6, 6, 0, 3), 0, 0));

        y++;

        //add label
        totalSizeLabel = new JLabel();
        this.add(totalSizeLabel, new GridBagConstraints(0, y, 2, h, wx, wy,
                GridBagConstraints.NORTHWEST, nofill, new Insets(6, 6, 0, 3), 0, 0));

        y++;

        //add label
        resultLabel = new JLabel();
        this.add(resultLabel, new GridBagConstraints(0, y, 2, h, wx, wy,
                GridBagConstraints.NORTHWEST, nofill, new Insets(6, 6, 0, 3), 0, 0));

        y++;

        //add checkbox
        createDiagramsBox = new JCheckBox(JavaSourceReverseLocaleMgr.misc.getString("CreateDiagrams"));
        this.add(createDiagramsBox, new GridBagConstraints(0, y, 2, h, 1, 0,
                GridBagConstraints.NORTHWEST, nofill, new Insets(24, 6, 0, 3), 0, 0));

        y++;

        //add checkbox
        createFieldsBox = new JCheckBox(JavaSourceReverseLocaleMgr.misc.getString("CreateFields"));
        this.add(createFieldsBox, new GridBagConstraints(0, y, 2, h, 1, 0,
                GridBagConstraints.NORTHWEST, nofill, new Insets(3, 6, 0, 3), 0, 0));

        y++;

        //add checkbox
        createMethodsBox = new JCheckBox(JavaSourceReverseLocaleMgr.misc.getString("CreateMethods"));
        this.add(createMethodsBox, new GridBagConstraints(0, y, 2, h, 1, 1,
                GridBagConstraints.NORTHWEST, nofill, new Insets(3, 6, 0, 3), 0, 0));

        y++;
    }
    
    
    public void beforeShowing() {
        ReverseJavaSourcecodeParameters params = (ReverseJavaSourcecodeParameters) getWizard()
                .getParameters();
        createDiagramsBox.setSelected(params.createDiagrams);
        createFieldsBox.setSelected(params.createFields);
        createMethodsBox.setSelected(params.createMethods);
    }
    
    
    public void onShowing() {

        final ReverseJavaSourcecodeParameters params = (ReverseJavaSourcecodeParameters) getWizard()
                .getParameters();
        final File selectedFolder = params.getSelectedFolder();
        final Controller controller = new ScanningController(params, progressLabel, nbFilesLabel,
                totalSizeLabel);
        final WizardPage thisPage = this;

        progressLabel.setText("");
        nbFilesLabel.setText("");
        totalSizeLabel.setText("");
        resultLabel.setText("");
        thisPage.setCanContinue(false);

        //long task runs in a separate thread
        Thread worker = new Thread() {

            public void run() {
                //long operation
                scanDirectory(selectedFolder, params, controller);

                //refresh the result
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        String pattern = JavaSourceReverseLocaleMgr.misc.getString("0FilesAnd1FoldersScanned");
                        String msg = MessageFormat.format(pattern, params.nbFilesScanned,
                                params.nbFoldersScanned);
                        progressLabel.setText(msg);

                        pattern = JavaSourceReverseLocaleMgr.misc.getString("Found0JavaFiles");
                        msg = MessageFormat.format(pattern, params.nbJavaFiles);
                        nbFilesLabel.setText(msg);

                        resultLabel.setText(JavaSourceReverseLocaleMgr.misc.getString("ReadyToProceed"));
                        thisPage.setCanContinue(true);
                    }
                });
            }
        };
        worker.start(); // So we don't hold up the dispatch thread.
    }
    
    
    public void afterShowing() {
        ReverseJavaSourcecodeParameters params = (ReverseJavaSourcecodeParameters) getWizard()
                .getParameters();
        params.createDiagrams = createDiagramsBox.isSelected();
        params.createFields = createFieldsBox.isSelected();
        params.createMethods = createMethodsBox.isSelected();
    }
    
    
    private void scanDirectory(File folder, ReverseJavaSourcecodeParameters params,
            Controller controller) {
        controller.checkPoint(0);
        boolean finished = controller.isFinalState();
        if (finished) {
            return;
        }

        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                params.nbFoldersScanned++;
                scanDirectory(file, params, controller);
            } else {
                params.nbFilesScanned++;
                String filename = file.toString();
                int idx = filename.lastIndexOf('.');
                String ext = (idx == -1) ? null : filename.substring(idx + 1);
                
                if("java".equals(ext)) {
                    params.addFileToImport(file);
                    params.totalSize += file.length();
                    params.nbJavaFiles++;
                }
                /*
                if ("class".equals(ext)) {
                    params.addFileToImport(file);
                    params.totalSize += file.length();
                    params.nbClassFiles++;
                } else if ("jar".equals(ext)) {
                    params.addFileToImport(file);
                    params.totalSize += file.length();
                    params.nbJarFiles++;
                }
                */
            }
        }

        controller.checkPoint(0);
    }
    
    
    private class ScanningController extends Controller {
        private JLabel m_label1, m_label2, m_label3;
        private ReverseJavaSourcecodeParameters m_params;

        ScanningController(ReverseJavaSourcecodeParameters params, JLabel label1, JLabel label2,
                JLabel label3) {
            m_label1 = label1;
            m_label2 = label2;
            m_label3 = label3;
            m_params = params;
            m_params.nbFilesScanned = 0;
            m_params.nbFoldersScanned = 0;
            m_params.nbJavaFiles = 0;
            /*
            m_params.nbClassFiles = 0;
            m_params.nbJarFiles = 0;
            */
            m_params.totalSize = 0L;
            m_params.clearFilesToImport();
        }

        public synchronized boolean checkPoint(int jobDone) {

            String pattern = JavaSourceReverseLocaleMgr.misc.getString("Scanning0FilesA1Folders");
            String msg = MessageFormat.format(pattern, m_params.nbFilesScanned,
                    m_params.nbFoldersScanned);
            m_label1.setText(msg);

            pattern = JavaSourceReverseLocaleMgr.misc.getString("Found0JavaFilesSoFar");
            msg = MessageFormat.format(pattern, m_params.nbJavaFiles);
            m_label2.setText(msg);

            pattern = JavaSourceReverseLocaleMgr.misc.getString("TotalSizeOfSourcecode0KB");
            msg = MessageFormat.format(pattern, m_params.totalSize / 1024);
            m_label3.setText(msg);

            boolean b = super.checkPoint(jobDone);
            return b;
        }
    }
}
