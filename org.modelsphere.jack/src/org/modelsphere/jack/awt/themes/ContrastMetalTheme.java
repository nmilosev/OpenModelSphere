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
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;
import javax.swing.*;
import javax.swing.border.*;

import org.modelsphere.jack.international.LocaleMgr;

public class ContrastMetalTheme extends DefaultMetalTheme {

    public String getName() {
        return LocaleMgr.misc.getString("ContrastMetalThemeName");
    }

    private final ColorUIResource primary1 = new ColorUIResource(0, 0, 0);
    private final ColorUIResource primary2 = new ColorUIResource(204, 204, 204);
    private final ColorUIResource primary3 = new ColorUIResource(255, 255, 255);
    private final ColorUIResource primaryHighlight = new ColorUIResource(102, 102, 102);

    private final ColorUIResource secondary2 = new ColorUIResource(204, 204, 204);
    private final ColorUIResource secondary3 = new ColorUIResource(255, 255, 255);
    private final ColorUIResource controlHighlight = new ColorUIResource(102, 102, 102);

    protected final ColorUIResource getPrimary1() {
        return primary1;
    }

    protected final ColorUIResource getPrimary2() {
        return primary2;
    }

    protected final ColorUIResource getPrimary3() {
        return primary3;
    }

    public final ColorUIResource getPrimaryControlHighlight() {
        return primaryHighlight;
    }

    protected final ColorUIResource getSecondary2() {
        return secondary2;
    }

    protected final ColorUIResource getSecondary3() {
        return secondary3;
    }

    public final ColorUIResource getControlHighlight() {
        return super.getSecondary3();
    }

    public final ColorUIResource getFocusColor() {
        return getBlack();
    }

    public final ColorUIResource getTextHighlightColor() {
        return getBlack();
    }

    public final ColorUIResource getHighlightedTextColor() {
        return getWhite();
    }

    public final ColorUIResource getMenuSelectedBackground() {
        return getBlack();
    }

    public final ColorUIResource getMenuSelectedForeground() {
        return getWhite();
    }

    public final ColorUIResource getAcceleratorForeground() {
        return getBlack();
    }

    public final ColorUIResource getAcceleratorSelectedForeground() {
        return getWhite();
    }

    public void addCustomEntriesToTable(UIDefaults table) {
        Border blackLineBorder = new BorderUIResource(new LineBorder(getBlack()));
        Border whiteLineBorder = new BorderUIResource(new LineBorder(getWhite()));

        Object textBorder = new BorderUIResource(new CompoundBorder(blackLineBorder,
                new BasicBorders.MarginBorder()));

        table.put("ToolTip.border", blackLineBorder); // NOT LOCALIZABLE,
        // property key
        table.put("TitledBorder.border", blackLineBorder); // NOT LOCALIZABLE,
        // property key
        table.put("Table.focusCellHighlightBorder", whiteLineBorder); // NOT
        // LOCALIZABLE,
        // property
        // key
        table.put("Table.focusCellForeground", getWhite()); // NOT LOCALIZABLE,
        // property key

        table.put("TextField.border", textBorder); // NOT LOCALIZABLE, property
        // key
        table.put("PasswordField.border", textBorder); // NOT LOCALIZABLE,
        // property key
        table.put("TextArea.border", textBorder); // NOT LOCALIZABLE, property
        // key
        table.put("TextPane.font", textBorder); // NOT LOCALIZABLE, property key
    }

}
