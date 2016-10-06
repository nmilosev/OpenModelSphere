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

import java.awt.Container;
import java.awt.Window;
import java.util.Vector;

import javax.swing.JPanel;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.debug.TestableWindow;

public final class ReverseFrameWorker extends
        org.modelsphere.jack.srtool.reverse.file.ReverseFrameWorker implements TestableWindow {

    /*
     * called by org.modelsphere.sms.oo.actions.ReverseActions.doActionPerformed()
     */
    public static final void ReverseFrameWorker(DbProject project, JPanel aPanel) {
        try {
            Vector fileVector = new Vector();
            ReverseFrame frame = new ReverseFrame(fileVector, aPanel);

            /*
             * int nbCommands = project.getDb().getCommandHistory().getNbCommands(); if (nbCommands
             * > 0) { javax.swing.JOptionPane.showMessageDialog(
             * org.modelsphere.sms.oo.MainFrame.getSingleton(), LocaleMgr.message
             * .getString("Reverse process will make previous commands undoable." ),
             * LocaleMgr.screen.getString("Warning"), javax.swing.JOptionPane.WARNING_MESSAGE ); }
             */

            frame.setProject(project);
            AwtUtil.centerWindow(frame);
            frame.setVisible(true);

            // if (!frame.is_cancel()) {
            // Debug.trace("GOING TO DISPOSE SOURCE PANEL");
            // ReversePlugins plugins = new ReversePlugins();
            // plugins.reverseFiles(fileVector, project);
            // }
        } catch (Exception ex) {
            Debug.trace(ex.getMessage());
            // org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(org.modelsphere.sms.oo.MainFrame.getSingleton(),
            // ex);
        }
    }

    // /////////////////////////////////
    // SUPPORT WindowTestable
    public String getName() {
        return "";
    }

    public Window createTestWindow(Container owner) {
        DbProject project = null;
        Vector fileVector = new Vector();
        ReverseFrame frame = new ReverseFrame(fileVector, null);
        frame.setProject(project);
        AwtUtil.centerWindow(frame);
        return frame;
    } // end createTestWindow()

    //
    // /////////////////////////////////

    //
    // DEMO FUNCTION
    //
    private static void runDemo() {
        DbProject project = null;
        ReverseFrameWorker.ReverseFrameWorker(project, null);
    } // end runDemo()

    // /*
    // Run the demo
    public static void main(String[] args) {
        runDemo();
    } // end main()
    // */

} // edn ReverseFrameWorker
