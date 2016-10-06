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

import java.awt.Component;
import java.io.File;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import org.modelsphere.jack.preference.context.ContextIO;
import org.modelsphere.jack.properties.PropertySet;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;

public class SafeMode {
	private static final String EXITED_NORMALLY = "ExitedNormally"; //NOT LOCALIZABLE, property
	private static final String STILL_RUNNING_TIMESTAMP = "StillRunningTimeStamp"; //NOT LOCALIZABLE, property
	private static final String STILL_RUNNING_TIMESTAMP_FORMATTED = "StillRunningTimeStampFormatted"; //NOT LOCALIZABLE, property
	private static final String SAFE_MODE_REQUESTED = "SafeModeRequested"; //NOT LOCALIZABLE, property
    
    private static long _lastAccessTime;
    
    /**
     * EXITED_NORMALLY: if the user closes the application, or was closed properly
     *   by the Task Manager under Windows (TaskManager->Applications).
     * EXITED_ABNORMALLY: if the JVM has crashed (TaskManager->Processes).
     * ANOTHER_APPLICATION_IN_USE: if another application is still running
     * EXPLICIT_SAFEMODE: if the user explicitly start the application in SafeMode 
     *   (e.g. with the -safemode parameter).
     */
    enum ApplicationStatus {EXITED_NORMALLY, EXITED_ABNORMALLY, ANOTHER_APPLICATION_IN_USE, EXPLICIT_SAFEMODE}; 
    public enum UserAction {RESTART, EXIT};
    
    private static final long STILL_RUNNING_THREAD_FREQUENCY = 1500; //Runs at each 1.5 seconds 
    
    //Called by sms.Application
	public static void checkSafeMode() {
		SafeMode.ApplicationStatus status = SafeMode.getApplicationStatus();
        
        if (status == SafeMode.ApplicationStatus.ANOTHER_APPLICATION_IN_USE) {
        	SafeModeDialog.confirmAnotherApplicationInUse(); //user can exit here
        } else if (status != SafeMode.ApplicationStatus.EXITED_NORMALLY) {
            SafeMode.UserAction userAction = SafeMode.openDialog(status);
            if (userAction == SafeMode.UserAction.EXIT) {
                System.exit(0);                
            }
        } //end if
        
        //start is alive thread
        Thread thread = new IsAliveThread(); 
        thread.start(); 
	} //end checkSafeMode()
	
	//
	// private methods
	//
        
    private static ApplicationStatus getApplicationStatus() {
        //get time of last access
        PropertySet properties = PropertySet.getInstance(SafeMode.class);
        long currentTime = System.currentTimeMillis();
        String lastAccessText = properties.getProperty(STILL_RUNNING_TIMESTAMP, Long.toString(currentTime));
        _lastAccessTime = Long.parseLong(lastAccessText); 
        
        //have existed normally at the last session? 
        ApplicationStatus status; 
        
        //is safe mode explicitly requested?
        String safemode = properties.getProperty(SAFE_MODE_REQUESTED, Boolean.toString(false));
        if (Boolean.parseBoolean(safemode)) {
        	status = ApplicationStatus.EXPLICIT_SAFEMODE; 
        } else {
        	status = detectStatus();
        } //end if
       
        //set existedNormally to FALSE (ExitAction will set it to TRUE if this action
        //is invoked.
        properties.setProperty(EXITED_NORMALLY, Boolean.toString(false));
        
        return status;
    }

    //Abnormal exit if EXITED_NORMALLY = false and not another instance of Open ModelSphere is running
    private static ApplicationStatus detectStatus() {
    	PropertySet properties = PropertySet.getInstance(SafeMode.class);
    	String normalExit = properties.getProperty(EXITED_NORMALLY, Boolean.toString(true));
		boolean abnormalExit = !Boolean.parseBoolean(normalExit); 
		ApplicationStatus status = ApplicationStatus.EXITED_NORMALLY;
		
		//check if another instance of Open ModelSphere is running
		if (abnormalExit) {
			//last time properties was accessed
			long lastAccess = System.currentTimeMillis() - _lastAccessTime;
			
			//if last access was made recently, an another application is still running,
			//so it didn't abnormally exited yet
			boolean recentAccess = (lastAccess < STILL_RUNNING_THREAD_FREQUENCY*3) ; 
			status = recentAccess ? ApplicationStatus.ANOTHER_APPLICATION_IN_USE : ApplicationStatus.EXITED_ABNORMALLY; 
		} //end if
		
		return status;
	}
    
	//open a dialog and return user's desired action
    public static UserAction openDialog(SafeMode.ApplicationStatus status) {
        SafeModeDialog dialog = new SafeModeDialog(status);
        dialog.setVisible(true);
        
        if (dialog.doResetPreferences()) {
            resetPreferences();
        }
        
        if (dialog.doReInitializeWorkspace()) {
            reInitWorkspace();
        }
        
        UserAction userAction = dialog.getUserAction();
        return userAction;
    }

    private static void resetPreferences() {
        File propertyFolder = new File(ApplicationContext.getPropertiesFolderPath()); 
        String folderName = propertyFolder.getName() + ".bak"; 
        File newFolder = new File(propertyFolder.getParentFile(), folderName); 
        if (newFolder.exists()) {
            newFolder.delete();
        }
        
        propertyFolder.renameTo(newFolder); 
    }
    
    private static void reInitWorkspace() {
        File workspaceFile = ContextIO.getWorkspaceFile();
        String fileName = workspaceFile.getName() + ".bak"; 
        File newFile = new File(workspaceFile.getParentFile(), fileName); 
        if (newFile.exists()) {
            newFile.delete();
        }
        
        workspaceFile.renameTo(newFile); 
    }

    //Called by jack.srtool.DefaultMainFrame.prepareToExitApplication()
    public static void exitNormally(boolean normalExit) {
        PropertySet properties = PropertySet.getInstance(SafeMode.class);
        properties.setProperty(EXITED_NORMALLY, Boolean.toString(normalExit));
    }
    
    //
    // inner class
    //
    private static class IsAliveThread extends Thread {
    	
    	@Override
    	public void run() {
    		DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.DEFAULT);
    		PropertySet properties = PropertySet.getInstance(SafeMode.class);
    		
    		try {
    			//endless loop
    			while (true) {
    				//Sleep (normally 1.5 seconds)
    				Thread.sleep(STILL_RUNNING_THREAD_FREQUENCY);
    				
    				//save current time in properties
    				Date date = new Date();
    				long currentTime = date.getTime(); 
    				properties.setProperty(STILL_RUNNING_TIMESTAMP, Long.toString(currentTime));
    				
    				String formatted = timeFormatter.format(date);
    				properties.setProperty(STILL_RUNNING_TIMESTAMP_FORMATTED, formatted); 

    				//System.out.println("Still running at: " + formatted); 
    			}
			} catch (InterruptedException e) {
				//exit abnormally
				System.out.println("Abnormal exit"); 
				exitNormally(false); 
			} 
    	}
    } //end IsAliveThread




} //end SafeMode
