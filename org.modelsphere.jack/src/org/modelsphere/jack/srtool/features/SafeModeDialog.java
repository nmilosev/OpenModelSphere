/*************************************************************************

Copyright (C) 2010 Grandite

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
package org.modelsphere.jack.srtool.features;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.UIManager;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.preference.context.ContextIO;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.features.SafeMode.UserAction;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;

@SuppressWarnings("serial")
public class SafeModeDialog extends JDialog implements ActionListener {
    private static final int EXTRA_WIDTH = 50;
    private JRadioButton _restartNormallyBtn;
    private JRadioButton _changeBtn;
    private JCheckBox _defaultCB;
    private JCheckBox _workspaceCB;
    private JButton _restartBtn; 
    private JButton _exitBtn;
    
    private SafeMode.UserAction _userAction = UserAction.EXIT; 
    UserAction getUserAction() { return _userAction; }
        
    private boolean _resetPreferences;
    public boolean doResetPreferences() { return _resetPreferences; }
    
    private boolean _reInitWorkspace;
    public boolean doReInitializeWorkspace() { return _reInitWorkspace; }
    
    public SafeModeDialog(SafeMode.ApplicationStatus status) {
        super((JFrame)null, getDialogTitle(), true);
        
        initContents(status);
        addListeners();
        resetValues();
        
        this.setLocationRelativeTo((JFrame)null);
    }
    
    private static String getDialogTitle() {
        String pattern = LocaleMgr.screen.getString("0SafeMode"); 
        String title = MessageFormat.format(pattern, new Object[] {ApplicationContext.getApplicationName()}); 
        return title;
    }
    
    private void initContents(SafeMode.ApplicationStatus status) {
        setLayout(new GridBagLayout());
        int row = 0;
        ButtonGroup group = new ButtonGroup();
        
        //get text values
        String propertyFolder = new File(ApplicationContext.getPropertiesFolderPath()).getPath();
        String workspaceFile = ContextIO.getWorkspaceFile().getPath();

        //row 
        boolean abnormalExit = (status == SafeMode.ApplicationStatus.EXITED_ABNORMALLY);
        String reason = abnormalExit ? LocaleMgr.screen.getString("AbnornalExitDetected") : 
        	LocaleMgr.screen.getString("ExplicitSafeMode"); 
        String ptn = LocaleMgr.screen.getString("0PleaseChooseOneOfTheFollowing");
        String msg = MessageFormat.format(ptn, new Object[] {reason});
        JLabel titleLabel = new JLabel(msg);
        add(titleLabel, new GridBagConstraints(0, row, 3, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(6, 6, 0, 6), 0, 0));
        row++;
        
        //Restart Normally
        String text = LocaleMgr.screen.getString("StartNormally"); 
        _restartNormallyBtn = new JRadioButton(text);
        add(_restartNormallyBtn, new GridBagConstraints(0, row, 3, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(6, 18, 0, 6), 0, 0));
        group.add(_restartNormallyBtn); 
        row++; 
        
        //Apply Changes and Restart
        _changeBtn = new JRadioButton(LocaleMgr.screen.getString("ApplyTheFollowingChangesAndStart"));
        add(_changeBtn, new GridBagConstraints(0, row, 3, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(6, 18, 0, 6), 0, 0));
        group.add(_changeBtn); 
        row++; 
        
        //Reset user preferences
        String folderSeparator = System.getProperty("file.separator");
        String pattern = LocaleMgr.screen.getString("ResetAllUserPreferences"); 
        msg = MessageFormat.format(pattern, new Object[] {propertyFolder, folderSeparator}); 
        _defaultCB = new JCheckBox(msg);
        add(_defaultCB, new GridBagConstraints(0, row, 3, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(6, 30, 0, 6), 0, 0));
        row++; 
        
        //Reset workspace
        pattern = LocaleMgr.screen.getString("ReinitializeLastSessionsWorkspace"); 
        msg = MessageFormat.format(pattern, new Object[] {workspaceFile}); 
        _workspaceCB = new JCheckBox(msg);
        add(_workspaceCB, new GridBagConstraints(0, row, 3, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(6, 30, 0, 6), 0, 0));
        row++; 
        
        _restartBtn = new JButton(LocaleMgr.screen.getString("Start"));
        _exitBtn = new JButton(LocaleMgr.screen.getString("Exit"));
        add(_restartBtn, new GridBagConstraints(1, row, 1, 1, 1.0, 1.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
                new Insets(18, 6, 6, 6), 0, 0));
        add(_exitBtn, new GridBagConstraints(2, row, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
                new Insets(18, 6, 6, 6), 0, 0));
        row++; 
        
        //set dimension
        AwtUtil.normalizeComponentDimension(new JComponent[] {_restartBtn, _exitBtn});
        pack();
        Dimension size = new Dimension(this.getWidth() + EXTRA_WIDTH, this.getHeight()); 
        this.setMinimumSize(size); 
    }
    
    private void addListeners() {
        _restartNormallyBtn.addActionListener(this);
        _changeBtn.addActionListener(this);
        _defaultCB.addActionListener(this);
        _workspaceCB.addActionListener(this);
        _restartBtn.addActionListener(this);
        _exitBtn.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        
        if (_restartNormallyBtn.equals(src)) {
            resetValues();
        } else if (_changeBtn.equals(src)) {
            resetValues();
        } else if (_defaultCB.equals(src)) {
            resetValues();
        } else if (_workspaceCB.equals(src)) {
            resetValues();
        } else if (_restartBtn.equals(src)) {
            _userAction = SafeMode.UserAction.RESTART;
            this.setVisible(false);
        } else if (_exitBtn.equals(src)) {
            _userAction = SafeMode.UserAction.EXIT; 
            this.setVisible(false);
        } //end if
    } //end actionPerformed()

    private void resetValues() {
        boolean changeBtnSelected = _changeBtn.isSelected(); 
        _defaultCB.setEnabled(changeBtnSelected);
        _workspaceCB.setEnabled(changeBtnSelected);
        
        boolean canRestart = _restartNormallyBtn.isSelected() || atLeastCheckboxOneSelected();
        _restartBtn.setEnabled(canRestart); 
        
        _resetPreferences = _defaultCB.isSelected();
        _reInitWorkspace = _workspaceCB.isSelected();
    }

    private boolean atLeastCheckboxOneSelected() {
        boolean atLeastOneSelected = _defaultCB.isSelected() || _workspaceCB.isSelected();
        return atLeastOneSelected;
    }
    
    static void confirmAnotherApplicationInUse() {
		DefaultMainFrame frame = ApplicationContext.getDefaultMainFrame();
		String ptn = LocaleMgr.screen.getString("AnotherSessionOf0IsCurrentlyRunning"); 
		String error = MessageFormat.format(ptn, new Object[] {ApplicationContext.getApplicationName()});
		String msg = error + LocaleMgr.screen.getString("RunningTwoSessionsAtATime"); 
		String title = ApplicationContext.getApplicationName();
		int option = JOptionPane.showConfirmDialog(frame, msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		
		if (option == JOptionPane.NO_OPTION) {
			System.exit(0);
		} 
	} //end confirmAnotherApplicationInUse()

    // *************
    // DEMO FUNCTION
    // *************
    private static void createAndShowGUI() throws DbException {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ex) {
        }

        // Create and set up the window.
        JFrame frame = new JFrame("SafeMode");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        SafeModeDialog dialog = new SafeModeDialog(SafeMode.ApplicationStatus.EXITED_ABNORMALLY);
        dialog.setVisible(true);
        SafeMode.UserAction userAction = dialog.getUserAction();
        System.out.println("User Action:" + userAction); 
    }

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                } catch (DbException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


}
