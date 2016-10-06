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

***********************************************************************/

package com.neosapiens.plugins.reverse.javasource.ui;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.modelsphere.jack.awt.dirchooser.DirectoryChooser2;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbRoot;
import org.modelsphere.jack.properties.PropertySet;
import org.modelsphere.sms.db.DbSMSProject;

import com.neosapiens.plugins.reverse.javasource.ReverseJavaSourcecodeParameters;
import com.neosapiens.plugins.reverse.javasource.international.JavaSourceReverseLocaleMgr;

public class ReverseJavaSourcecodeWizardPage1 extends WizardPage 
    implements ActionListener, KeyListener {
    
    private static final long serialVersionUID = 1L;
    private static final String JAVA_IMPORT_FILE = "JAVA_IMPORT_FILE";
    private PropertySet properties;
    private JRadioButton newProjectBtn;
    private JRadioButton existingProjectBtn;
    private JComboBox prjList;
    private JTextField folderText;
    private JButton folderSelectButton;

    
    ReverseJavaSourcecodeWizardPage1(Wizard<? extends WizardParameters> wizard) {
        super(wizard);
        properties = PropertySet.getInstance(ReverseJavaSourcecodeWizardPage1.class);
        setLayout(new GridBagLayout());
        initContents();
        initCombo();
    }
    
    
    private void initContents() {
        int y = 0;
        int w = 1, h = 1;
        double wx = 0.0, wy = 0.0;
        int nofill = GridBagConstraints.NONE;

        //add label
        String title = JavaSourceReverseLocaleMgr.misc.getString("ReverseEngineerJavaFilesLabel");
        JLabel instructionLabel = new JLabel(title);
        this.add(instructionLabel, new GridBagConstraints(0, y, 2, h, wx, wy,
                GridBagConstraints.WEST, nofill, new Insets(6, 6, 0, 3), 0, 0));

        y++;

        //add folder chooser
        folderText = new JTextField();
        this.add(folderText, new GridBagConstraints(0, y, 2, h, 1.0, wy, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(3, 9, 18, 3), 0, 0));

        folderSelectButton = new JButton("...");
        this.add(folderSelectButton, new GridBagConstraints(2, y, w, h, wx, wy,
                GridBagConstraints.WEST, nofill, new Insets(3, 3, 18, 6), 0, 0));

        y++;

        //add label
        title = JavaSourceReverseLocaleMgr.misc.getString("ReverseEngineerClassesIntoLabel");
        JLabel label1 = new JLabel(title);
        this.add(label1, new GridBagConstraints(0, y, 2, h, wx, wy, GridBagConstraints.WEST,
                nofill, new Insets(6, 6, 0, 3), 0, 0));

        y++;

        //add Radio Button Group
        ButtonGroup group = new ButtonGroup();
        newProjectBtn = new JRadioButton(JavaSourceReverseLocaleMgr.misc.getString("NewProject"));
        existingProjectBtn = new JRadioButton(JavaSourceReverseLocaleMgr.misc.getString("ExistingProject"));
        group.add(newProjectBtn);
        group.add(existingProjectBtn);

        this.add(newProjectBtn, new GridBagConstraints(0, y, 2, h, wx, wy, GridBagConstraints.WEST,
                nofill, new Insets(0, 9, 0, 3), 0, 0));
        y++;

        this.add(existingProjectBtn, new GridBagConstraints(0, y, 1, h, wx, 1,
                GridBagConstraints.NORTHWEST, nofill, new Insets(0, 9, 12, 3), 0, 0));

        prjList = new JComboBox(new Object[] {});
        this.add(prjList, new GridBagConstraints(1, y, 2, h, wx, 1, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 9, 12, 3), 0, 0));

        y++;

        //add listeners
        newProjectBtn.addActionListener(this);
        existingProjectBtn.addActionListener(this);
        folderSelectButton.addActionListener(this);
        folderText.addKeyListener(this);
    }
    
    
    @Override
    public void beforeShowing() {
        String filename = properties.getProperty(JAVA_IMPORT_FILE, null);
        if (filename != null) {
            folderText.setText(filename);
            File selectedFolder = new File(filename);
            ReverseJavaSourcecodeParameters params = (ReverseJavaSourcecodeParameters) this.getWizard()
                    .getParameters();
            params.setSelectedFolder(selectedFolder);
        }

        setCanContinue(filename != null);
    }
    
    
    @Override
    public void afterShowing() {
        ProjectItem item = (ProjectItem) prjList.getSelectedItem();
        ReverseJavaSourcecodeParameters params = (ReverseJavaSourcecodeParameters) this.getWizard()
                .getParameters();

        if (item == null) {
            params.setOutputProject(null);
        } else {
            DbSMSProject project = item.getProject();
            params.setOutputProject(project);
        }
    }

    
    @Override
    public void actionPerformed(ActionEvent ev) {
        Object src = ev.getSource();
        ReverseJavaSourcecodeParameters params = (ReverseJavaSourcecodeParameters) this.getWizard()
                .getParameters();

        //which button has been pressed
        if (src.equals(newProjectBtn)) {
            prjList.setEnabled(false);
            params.createNewProject = true;
        } else if (src.equals(existingProjectBtn)) {
            prjList.setEnabled(true);
            params.createNewProject = false;
        } else if (src.equals(folderSelectButton)) {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            String filename = properties.getProperty(JAVA_IMPORT_FILE, null);

            File selectedFolder = (filename == null) ? null : new File(filename);
            String selectText = JavaSourceReverseLocaleMgr.misc.getString("Select");
            boolean showFiles = true;
            String chooserFolderText = JavaSourceReverseLocaleMgr.misc.getString("ChooseFolder");
            selectedFolder = DirectoryChooser2.selectDirectory(getWizard().getDialog(),
                    selectedFolder, chooserFolderText, selectText, showFiles);
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            if (selectedFolder != null) {
                filename = selectedFolder.toString();
                properties.setProperty(JAVA_IMPORT_FILE, filename);
                folderText.setText(filename);
                params.setSelectedFolder(selectedFolder);
                this.setCanContinue(true);
            }
        } else if (src.equals(folderText)) {
            String s = folderText.getText();
            super.setCanContinue(!s.isEmpty());
        }
    }
    
    
    @Override
    public void keyPressed(KeyEvent ev) {
    }
    

    @Override
    public void keyTyped(KeyEvent ev) {
    }
    

    @Override
    public void keyReleased(KeyEvent ev) {
        Object src = ev.getSource();

        if (src.equals(folderText)) {
            String s = folderText.getText();
            super.setCanContinue(!s.isEmpty());
        }
    }
    
    
    private void initCombo() {
        prjList.removeAllItems();
        Db[] dbs = Db.getDbs();

        //for each project
        for (Db db : dbs) {
            DbRoot root = db.getRoot();
            try {
                db.beginReadTrans();
                DbRelationN relN = root.getComponents();
                DbEnumeration enu = relN.elements(DbSMSProject.metaClass);
                while (enu.hasMoreElements()) {
                    DbSMSProject proj = (DbSMSProject) enu.nextElement();
                    ProjectItem item = new ProjectItem(proj);
                    prjList.addItem(item);
                }
                enu.close();
                db.commitTrans();
            } catch (DbException ex) {
                //do not add to the list
            } //end try()
        } //end for    

        //if no existing project
        ReverseJavaSourcecodeParameters params = (ReverseJavaSourcecodeParameters) this.getWizard()
                .getParameters();
        if (dbs.length == 0) {
            newProjectBtn.setSelected(true);
            existingProjectBtn.setEnabled(false);
            prjList.setEnabled(false);
            params.createNewProject = true;
        } else {
            existingProjectBtn.setSelected(true);
            existingProjectBtn.setEnabled(true);
            prjList.setEnabled(true);
            params.createNewProject = false;
        }
    }
    
    
    private static class ProjectItem {
        private DbSMSProject m_proj;

        ProjectItem(DbSMSProject proj) {
            m_proj = proj;
        }

        @Override
        public String toString() {
            String s;

            try {
                m_proj.getDb().beginReadTrans();
                s = m_proj.getName();
                m_proj.getDb().commitTrans();
            } catch (DbException ex) {
                s = "???";
            }

            return s;
        }

        public DbSMSProject getProject() {
            return m_proj;
        }
    }
}
