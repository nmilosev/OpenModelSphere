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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.TextViewerDialog;
import org.modelsphere.jack.baseDb.objectviewer.Viewer;
import org.modelsphere.jack.debug.TechnicalSupport;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public final class ShowTechInfoAction extends AbstractApplicationAction {
    private static final String kTitle = LocaleMgr.action.getString("showTeckInfoTitle");

    // GANDALF keyword activator index
    private int next = 0;

    public ShowTechInfoAction() {
        super(LocaleMgr.action.getString("showTeckInfo"));
        this.setMnemonic(LocaleMgr.action.getMnemonic("showTeckInfo"));
        setEnabled(true);
    }

    protected void doActionPerformed() {
        String info = TechnicalSupport.getSupportInfo();
        final TextViewerDialog dialog = new TextViewerDialog(ApplicationContext
                .getDefaultMainFrame(), kTitle, info, false, true, true);
        dialog.getTextPanel().setEditable(false);
        dialog.getTextPanel().addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (!e.isControlDown() || !e.isAltDown()) {
                    next = 0;
                    return;
                }
                int c = e.getKeyCode();
                int[] gandalf = new int[] { KeyEvent.VK_G, KeyEvent.VK_A, KeyEvent.VK_N,
                        KeyEvent.VK_D, KeyEvent.VK_A, KeyEvent.VK_L, KeyEvent.VK_F };
                if (c == gandalf[next]) {
                    next++;
                    e.consume();
                } else
                    next = 0;
                if (next == gandalf.length) {
                    next = 0;
                    dialog.dispose();
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            Viewer.showViewer(ApplicationContext.getDefaultMainFrame(), null);
                        }
                    });
                }
            }
        });
        dialog.setVisible(true);
    }

}
