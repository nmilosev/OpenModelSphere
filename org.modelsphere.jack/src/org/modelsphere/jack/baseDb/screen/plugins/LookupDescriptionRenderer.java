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

package org.modelsphere.jack.baseDb.screen.plugins;

import java.awt.*;
import javax.swing.*;

import org.modelsphere.jack.baseDb.screen.*;

public class LookupDescriptionRenderer extends DefaultRenderer {
    private static JPanel panel;
    private static JLabel label;
    private static JLabel labelSpace = new JLabel(" ");
    private static JButton textEditorBtn = new JButton();

    public static final Icon kTextToWrite = org.modelsphere.jack.srtool.international.LocaleMgr.screen
            .getImageIcon("TextToWrite");
    public static final Icon kTextWritten = org.modelsphere.jack.srtool.international.LocaleMgr.screen
            .getImageIcon("TextWritten");

    public static final LookupDescriptionRenderer singleton = new LookupDescriptionRenderer();

    protected LookupDescriptionRenderer() {
    }

    public Component getTableCellRendererComponent(ScreenView screenView, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (panel == null) {
            panel = new JPanel(new BorderLayout());
            label = new JLabel();
            textEditorBtn.setName("TextEditorBtn"); // NOT LOCALIZABLE - For QA
        }
        if (screenView instanceof DescriptionView) {
            panel.add(labelSpace, BorderLayout.WEST);
            panel.add(label, BorderLayout.CENTER);
            panel.add(textEditorBtn, BorderLayout.EAST);
        } else if (screenView instanceof ListView) {
            panel.remove(label);
            panel.remove(labelSpace);
            panel.add(textEditorBtn, BorderLayout.CENTER);
        }

        label.setText((String) value);
        if ((String) value == null) {
            textEditorBtn.setIcon(kTextToWrite);
        } else {
            textEditorBtn.setIcon(((String) value).length() > 0 ? kTextWritten : kTextToWrite);
        }

        textEditorBtn.setBackground(isSelected ? screenView.getSelectionBackground() : screenView
                .getBackground());
        label.setForeground(isSelected ? screenView.getSelectionForeground() : screenView
                .getForeground());
        label.setFont(screenView.getFont());
        panel.setBackground(isSelected ? screenView.getSelectionBackground() : screenView
                .getBackground());
        return panel;
    }

    public int getDisplayWidth() {
        return 60;
    }

    public void updateUI() {
        super.updateUI();
        if (panel != null)
            panel.updateUI();
        if (label != null)
            label.updateUI();
        if (labelSpace != null)
            labelSpace.updateUI();
        if (textEditorBtn != null)
            textEditorBtn.updateUI();
    }

}
