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
import java.awt.*;

public class JackMetalTheme extends MetalTheme {

    private ColorUIResource primary1;
    private ColorUIResource primary2;
    private ColorUIResource primary3;

    private ColorUIResource secondary1;
    private ColorUIResource secondary2;
    private ColorUIResource secondary3;

    private FontUIResource controlFont;
    private FontUIResource systemFont;
    private FontUIResource userFont;
    private FontUIResource smallFont;

    private String name;

    public final String getName() {
        return name;
    }

    public JackMetalTheme(String name, Color p1, Color p2, Color p3, Color s1, Color s2, Color s3) {
        init(name, p1, p2, p3, s1, s2, s3);
        try {
            // retrieving default theme fonts
            DefaultMetalTheme defTheme = new DefaultMetalTheme();
            controlFont = defTheme.getControlTextFont();
            systemFont = defTheme.getSystemTextFont();
            userFont = defTheme.getUserTextFont();
            smallFont = defTheme.getSubTextFont();
        } catch (Exception e) {
            controlFont = new FontUIResource("Dialog", Font.BOLD, 12); // NOT
            // LOCALIZABLE,
            // property
            // key
            systemFont = new FontUIResource("Dialog", Font.PLAIN, 12); // NOT
            // LOCALIZABLE,
            // property
            // key
            userFont = new FontUIResource("Dialog", Font.PLAIN, 12); // NOT
            // LOCALIZABLE,
            // property
            // key
            smallFont = new FontUIResource("Dialog", Font.PLAIN, 10); // NOT
            // LOCALIZABLE,
            // property
            // key
        }
    }

    public JackMetalTheme(String name, Color p1, Color p2, Color p3, Color s1, Color s2, Color s3,
            Font controlF, Font systemF, Font userF, Font smallF) {
        init(name, p1, p2, p3, s1, s2, s3);
        try {
            controlFont = new FontUIResource(Font.getFont("swing.plaf.metal.controlFont", controlF)); // NOT
            // LOCALIZABLE,
            // property key
            systemFont = new FontUIResource(Font.getFont("swing.plaf.metal.systemFont", systemF)); // NOT
            // LOCALIZABLE,
            // property key
            userFont = new FontUIResource(Font.getFont("swing.plaf.metal.userFont", userF)); // NOT LOCALIZABLE,
            // property key
            smallFont = new FontUIResource(Font.getFont("swing.plaf.metal.smallFont", smallF)); // NOT LOCALIZABLE,
            // property key
        } catch (Exception e) {
            controlFont = new FontUIResource("Dialog", Font.BOLD, 12); // NOT
            // LOCALIZABLE,
            // property
            // key
            systemFont = new FontUIResource("Dialog", Font.PLAIN, 12); // NOT
            // LOCALIZABLE,
            // property
            // key
            userFont = new FontUIResource("Dialog", Font.PLAIN, 12); // NOT
            // LOCALIZABLE,
            // property
            // key
            smallFont = new FontUIResource("Dialog", Font.PLAIN, 10); // NOT
            // LOCALIZABLE,
            // property
            // key
        }
    }

    private void init(String name, Color p1, Color p2, Color p3, Color s1, Color s2, Color s3) {
        this.name = name;
        primary1 = new ColorUIResource(p1);
        primary2 = new ColorUIResource(p2);
        primary3 = new ColorUIResource(p3);
        secondary1 = new ColorUIResource(s1);
        secondary2 = new ColorUIResource(s2);
        secondary3 = new ColorUIResource(s3);
    }

    // these are blue in Metal Default Theme
    public final ColorUIResource getPrimary1() {
        return primary1;
    }

    public final ColorUIResource getPrimary2() {
        return primary2;
    }

    public final ColorUIResource getPrimary3() {
        return primary3;
    }

    // these are gray in Metal Default Theme
    public final ColorUIResource getSecondary1() {
        return secondary1;
    }

    public final ColorUIResource getSecondary2() {
        return secondary2;
    }

    public final ColorUIResource getSecondary3() {
        return secondary3;
    }

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
        return controlFont;
    }

    public final FontUIResource getSubTextFont() {
        return smallFont;
    }

}
