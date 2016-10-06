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

package org.modelsphere.jack.awt.themes;

import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import javax.swing.*;
import javax.swing.border.*;

import org.modelsphere.jack.international.LocaleMgr;

import java.awt.*;

public class BigContrastMetalTheme extends ContrastMetalTheme {

    public final String getName() {
        return LocaleMgr.misc.getString("BigContrastMetalThemeName");
    }

    private final FontUIResource controlFont = new FontUIResource("Dialog", Font.BOLD, 24); // NOT LOCALIZABLE, property key
    private final FontUIResource systemFont = new FontUIResource("Dialog", Font.PLAIN, 24); // NOT LOCALIZABLE, property key
    private final FontUIResource windowTitleFont = new FontUIResource("Dialog", Font.BOLD, 24);// NOT LOCALIZABLE, property key
    private final FontUIResource userFont = new FontUIResource("SansSerif", Font.PLAIN, 24); // NOT LOCALIZABLE, property key
    private final FontUIResource smallFont = new FontUIResource("Dialog", Font.PLAIN, 20); // NOT LOCALIZABLE, property key

    public final FontUIResource getControlTextFont() {
        return controlFont;
    }

    public final FontUIResource getSystemTextFont() {
        return systemFont;
    }

    public final FontUIResource getUserTextFont() {
        return userFont;
    }

    public final FontUIResource getMenuTextFont() {
        return controlFont;
    }

    public final FontUIResource getWindowTitleFont() {
        return windowTitleFont;
    }

    public final FontUIResource getSubTextFont() {
        return smallFont;
    }

    public final void addCustomEntriesToTable(UIDefaults table) {
        super.addCustomEntriesToTable(table);

        final int internalFrameIconSize = 30;
        table.put("InternalFrame.closeIcon", MetalIconFactory
                .getInternalFrameCloseIcon(internalFrameIconSize)); // NOT
        // LOCALIZABLE,
        // property
        // key
        table.put("InternalFrame.maximizeIcon", MetalIconFactory
                .getInternalFrameMaximizeIcon(internalFrameIconSize)); // NOT
        // LOCALIZABLE,
        // property
        // key
        table.put("InternalFrame.iconizeIcon", MetalIconFactory
                .getInternalFrameMinimizeIcon(internalFrameIconSize)); // NOT
        // LOCALIZABLE,
        // property
        // key
        table.put("InternalFrame.minimizeIcon", MetalIconFactory
                .getInternalFrameAltMaximizeIcon(internalFrameIconSize)); // NOT
        // LOCALIZABLE,
        // property
        // key

        Border blackLineBorder = new BorderUIResource(new MatteBorder(2, 2, 2, 2, Color.black));
        Border textBorder = blackLineBorder;

        table.put("ToolTip.border", blackLineBorder); // NOT LOCALIZABLE,
        // property key
        table.put("TitledBorder.border", blackLineBorder); // NOT LOCALIZABLE,
        // property key

        table.put("TextField.border", textBorder); // NOT LOCALIZABLE, property
        // key
        table.put("PasswordField.border", textBorder); // NOT LOCALIZABLE,
        // property key
        table.put("TextArea.border", textBorder); // NOT LOCALIZABLE, property
        // key
        table.put("TextPane.font", textBorder); // NOT LOCALIZABLE, property key

        table.put("ScrollPane.border", blackLineBorder); // NOT LOCALIZABLE,
        // property key

        table.put("ScrollBar.width", new Integer(25)); // NOT LOCALIZABLE,
        // property key
    }
}
