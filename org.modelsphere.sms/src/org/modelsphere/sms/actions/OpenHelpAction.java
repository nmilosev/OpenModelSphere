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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.actions;

import java.io.File;
import java.util.Locale;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.international.LocaleChangeManager;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.international.LocaleMgr;

/**
 * @author nicolask
 * Created on Mar 4, 2005
 * 
 * Implements the Help->User's Guide action
 */
@SuppressWarnings("serial")
public class OpenHelpAction extends AbstractApplicationAction implements SelectionActionListener {

    public void updateSelectionAction() throws DbException {
        
        //show user's guide only for full version on Windows platform
        boolean isWindowsPlatform = isWindowsPlatform();
        boolean visible = isWindowsPlatform && ScreenPerspective.isFullVersion(); 
        setEnabled(visible);
        setVisible(visible);
        
        if (! visible) {
            setVisibilityMode(VISIBILITY_DEFAULT);
        }
    }

    private static final String CMD_PATTRN = "cmd /c \"{0}\""; //NOT LOCALIZABLE

    OpenHelpAction() {
        super(LocaleMgr.action.getString("UserGuide"));
        boolean isWindowsPlatform = isWindowsPlatform();
        boolean visible = isWindowsPlatform && ScreenPerspective.isFullVersion(); 
        
        if (visible) {
            this.setMnemonic(LocaleMgr.action.getMnemonic("UserGuide"));
        }
        
        setEnabled(visible);
        setVisible(visible);
        
        if (! visible) {
            setVisibilityMode(VISIBILITY_DEFAULT);
        }
    }
    
    protected final void doActionPerformed() {
        // get the application path and launch the help file in a browser
        // using a shell command
        String folder = ApplicationContext.getApplicationDirectory();

        Locale loc = LocaleChangeManager.getLocale();
        String language = loc.getLanguage();
        String filename = (language.compareTo("fr") == 0) ? "\\Guide Utilisateur.html"
                : "\\User Guide.html";

        String path = folder + "\\doc" + filename;
        File file = new File(path);

        // if deployment version
        if (file.exists()) {
            String command = MessageFormat.format(CMD_PATTRN, new Object[] { path });
            execute(command);
        } else { // if development version
            String guidePath = folder + "\\..\\org.modelsphere.guide\\help_sms" + filename;
            file = new File(guidePath);
            String command = MessageFormat.format(CMD_PATTRN, new Object[] { guidePath });
            execute(command);
        }
    }
    
    //
    // private methods
    //
    private Boolean _isWindowsPlatform = null;
    public boolean isWindowsPlatform() {
        if (_isWindowsPlatform == null) {
            _isWindowsPlatform = false;
            
            try {// if not windows platform, disable this item, hide if possible
                String osName = System.getProperty("os.name");
                String graphsEnv = System.getProperty("java.awt.graphicsenv");
                if (osName.contains("Windows") && graphsEnv.contains("Win32")) // NOT
                    // LOCALIZABLE
                    _isWindowsPlatform = true;
            } catch (Exception e) {
                _isWindowsPlatform = false;
            }
        } //end if
        
        return _isWindowsPlatform;
    }

    private void execute(String command) {
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(command);
        } catch (Exception ex) {
            // do nothing
        }
    }

    /*
    private void doActionPerformedInDevelopment(String path, String filename) {
        String guidePath = path + "\\..\\org.modelsphere.guide" + filename;
        String command = MessageFormat.format(CMD_PATTRN, new Object[] { guidePath });

        // launch the shell command to "execute" this file
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(command);
        } catch (Exception ex) {
            // do nothing
        }

    } // end doActionPerformedInDevelopment()
    */
}
