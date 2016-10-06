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
package org.modelsphere.jack.awt;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import org.modelsphere.jack.debug.Debug;

class ToolBarDialog extends JDialog {
    JToolBar toolbar;

    ToolBarDialog(JFrame owner, JToolBar toolbar) {
        super(owner, toolbar.getName(), false);
        this.toolbar = toolbar;
        toolbar.setFloatable(false);
        toolbar.setBorderPainted(false);
        if (toolbar.getComponentCount() > 0
                && toolbar.getComponentAtIndex(0) instanceof JackSeparator) {
            toolbar.remove(0);
        }
        // setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        toolbar.addComponentListener(new ComponentListener() {
            public void componentResized(ComponentEvent e) {
            }

            public void componentMoved(ComponentEvent e) {
            }

            public void componentShown(ComponentEvent e) {
                if (!ToolBarDialog.this.isVisible()) {
                    ToolBarDialog.this.setVisible(true);
                }
            }

            public void componentHidden(ComponentEvent e) {
                if (ToolBarDialog.this.isVisible()) {
                    ToolBarDialog.this.setVisible(false);
                }
            }
        });

        getContentPane().add(toolbar);
        pack();
    }

    public void setVisible(boolean b) {
        super.setVisible(b);
        if (toolbar != null && b != toolbar.isVisible()) {
            toolbar.setVisible(b);
        }
    }

    public void dispose() {
        Debug.trace("ToolBarDialog.dispose()");
    }

}
