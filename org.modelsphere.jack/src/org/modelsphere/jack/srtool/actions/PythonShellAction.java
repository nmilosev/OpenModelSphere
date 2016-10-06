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

import java.awt.Cursor;
import java.beans.PropertyVetoException;
import javax.swing.JDesktopPane;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.script.PythonShell;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.jack.text.MessageFormat;

/**
 * 
 * Must be used in debug only
 * 
 */
public final class PythonShellAction extends AbstractApplicationAction {
    private static PythonShellAction singleton = null;
    private PythonShell scriptShell;
    private static final String PropertyVetoExceptionMessagePattern = LocaleMgr.message
            .getString("PropertyVetoExceptionMessagePattern");

    public static PythonShellAction getSingleton() {
        if (singleton == null)
            singleton = new PythonShellAction();
        return singleton;
    }

    private PythonShellAction() {
        super(LocaleMgr.action.getString("pythonShell")); // NOT LOCALIZABLE
        this.setMnemonic(LocaleMgr.action.getMnemonic("pythonShell"));
        setEnabled(true);
        setVisible(ScreenPerspective.isFullVersion());
        // this.setHelpText(LocaleMgr.action.getString("pythonShell"));
        // //LOCALIZABLE
    }

    protected final void doActionPerformed() {
        // get desktop pane & set wait cursor
        DefaultMainFrame mainFrame = ApplicationContext.getDefaultMainFrame();
        JDesktopPane pane = mainFrame.getJDesktopPane();
        Cursor cursor = pane.getCursor(); // keep current cursor
        mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        // create python shell
        scriptShell = new PythonShell();
        pane.add(scriptShell, DefaultMainFrame.PROPERTY_LAYER);

        scriptShell.setBounds(0, 0, (scriptShell.getDesktopPane().getSize().width * 75) / 100,
                scriptShell.getDesktopPane().getSize().height / 2);
        scriptShell.setVisible(true);

        try {
            scriptShell.setIcon(false);
            scriptShell.setSelected(true);
        } catch (PropertyVetoException e) {
            String msg = MessageFormat.format(PropertyVetoExceptionMessagePattern, new Object[] { e
                    .getLocalizedMessage() });
            javax.swing.JOptionPane.showMessageDialog(null, msg);
        } finally {
            mainFrame.setCursor(cursor); // restore previous cursor
        }

        scriptShell = null;
    } // end doActionPerformed()
} // end PythonShellAction
