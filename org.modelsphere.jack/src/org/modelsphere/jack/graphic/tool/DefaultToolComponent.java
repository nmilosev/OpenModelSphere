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

package org.modelsphere.jack.graphic.tool;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;

public class DefaultToolComponent extends JButton implements ToolComponent {
    private int state = ToolComponent.STATE_NORMAL;

    DefaultToolComponent() {
        super();
        setText(null);
        setFocusable(false);
        setIconTextGap(0);
        setHideActionText(true);
    }

    public void setState(int newstate) {
        if (state == newstate) {
            return;
        }
        state = newstate;
        setSelected((state & (STATE_SELECTED | STATE_MASTER)) != 0);
        repaint();
    }

    public int getState() {
        return state;
    }

    public int getAuxiliaryIndex() {
        return -1; // No auxiliary selection
    }

    public void setAuxiliaryIndex(int auxiliaryIndex) {
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if ((state & ToolComponent.STATE_MASTER) != 0) {
            Tool.paintMasterToolEffect((Graphics2D) g, this);
        }
    }
}
