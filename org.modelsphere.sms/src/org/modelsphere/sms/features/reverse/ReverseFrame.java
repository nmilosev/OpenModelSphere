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

package org.modelsphere.sms.features.reverse;

import java.io.File;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.srtool.reverse.file.AbstractReverser;
import org.modelsphere.jack.srtool.reverse.file.FileListPreviewer;
import org.modelsphere.jack.srtool.reverse.file.SourceChooser;

public final class ReverseFrame extends org.modelsphere.jack.srtool.reverse.file.ReverseFrame {

    public ReverseFrame(JFrame owner, Vector aFileVector, JPanel aPanel) {
        super(owner, aFileVector, aPanel);
    }

    public ReverseFrame(Vector aFileVector, JPanel aPanel) {
        this(ApplicationContext.getDefaultMainFrame(), aFileVector, aPanel);
    }

    protected void init() {
        isDone = false;
        // super(ApplicationContext.getDefaultMainFrame(),
        // LocaleMgr.screen.getString("ReverseEngineering"), true);
        // fileVector = aFileVector;
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        /*
         * The following pops up a file chooser in the users home directory that only sees .java
         * files
         */
        m_FileChooser = new SourceChooser(filesPath);
        fileListToAdd = new FileListPreviewer(m_FileChooser, FileListPreviewer.FILES_ONLY);
        m_FileChooser.setAccessory(fileListToAdd);
        m_FileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        m_FileChooser.setDialogTitle(LocaleMgr.screen.getString("SelectFilesToReverse"));
        plugins = new ReversePlugins();
        plugins.setFilters(filterVector, m_FileChooser);

        // to do: enable multiple selection
        // False for now, Put to true when the bug of the MultipleSelection will
        // be solve ref #4170956
        m_FileChooser.setMultiSelectionEnabled(false);

        m_PackageChooser = new SourceChooser(dirPath);
        PackageListToAdd = new FileListPreviewer(m_PackageChooser,
                FileListPreviewer.DIRECTORIES_ONLY);
        m_PackageChooser.setAccessory(PackageListToAdd);
        m_PackageChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        m_PackageChooser.setDialogTitle(LocaleMgr.screen.getString("SelectDirectoriesToReverse"));

        // to do: enable multiple selection
        // False for now, Put to true when the bug of the MultipleSelection will
        // be solve ref #4170956
        m_PackageChooser.setMultiSelectionEnabled(false);

        init_actionListeners();
    } // init

    protected void terminate(boolean canceledByUser) {
        this.canceledByUser = canceledByUser;
        if (!canceledByUser) {
            updateParametersValues();

            // Fill fileVector
            Enumeration enumeration = m_model.elements();
            while (enumeration.hasMoreElements()) {
                File f = (File) enumeration.nextElement();
                fileVector.addElement(f);
            }

            ReversePlugins plugins = new ReversePlugins();
            try {
                AbstractReverser reverser = new ConcreteReverser();
                plugins.reverseFiles(fileVector, m_project, reverser);
            } catch (DbException ex) {
            }
            // TODO: setModel(m_model);

            // dispose reverse wizard if successful (not cancel)
            if (sourcePanel != null) {
                // WizardFrame frame = sourcePanel.getFrame();
                // frame.dispose();
            }
        }

        dispose();
    } // end of terminate()

    public static void main(String[] args) {
        // Create a property dialog
        JFrame owner = new JFrame("ReverseFrame"); // NOT LOCALIZABLE, unit test
        owner.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Locale.setDefault(Locale.ENGLISH);
        Vector fileVector = new Vector();
        JPanel panel = new JPanel();
        ReverseFrame reverseFrame = new ReverseFrame(owner, fileVector, panel);
        reverseFrame.setVisible(true);
    }

} // end of ReverseFrame
