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

package org.modelsphere.jack.graphic.zone;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.modelsphere.jack.graphic.*;
import org.modelsphere.jack.util.StringUtil;

public abstract class TextFieldCellEditor implements CellEditor {

    protected Diagram diagram;
    protected ZoneBox box;
    protected CellID cellID;
    protected ZoneCell value;
    protected JTextField textField;

    // overridden
    public JComponent getComponent(ZoneBox box, CellID cellID, ZoneCell value, DiagramView view,
            Rectangle cellRect) {
        this.box = box;
        this.cellID = cellID;
        this.value = value;
        diagram = box.getDiagram();
        textField = new JTextField();
        setText();
        Dimension size = textField.getPreferredSize();
        textField.setPreferredSize(new Dimension(size.width + 50, size.height));
        textField.selectAll();
        textField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    diagram.removeEditor(CellEditor.CANCEL);
                    break;
                case KeyEvent.VK_UP:
                    diagram.removeEditor(CellEditor.UP_ARROW_ENDING);
                    break;
                case KeyEvent.VK_DOWN:
                    diagram.removeEditor(CellEditor.DOWN_ARROW_ENDING);
                    break;
                case KeyEvent.VK_ENTER:
                    diagram.removeEditor(e.isControlDown() ? CellEditor.INSERT_ROW_ENDING
                            : CellEditor.NORMAL_ENDING);
                    break;
                }
            }
        });
        return textField;
    }

    // overridden
    public void setText() {
        Object data = value.getCellData();
        String text = StringUtil.getDisplayString(data);
        textField.setText(text);
    }

    public abstract void stopEditing(int endCode);
}
