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

import org.modelsphere.jack.international.LocaleMgr;

public class BlueMetalTheme extends DefaultMetalTheme {

    public final String getName() {
        return LocaleMgr.misc.getString("BlueMetalThemeName");
    }

    private final ColorUIResource primary1 = new ColorUIResource(0, 0, 132);
    private final ColorUIResource primary2 = new ColorUIResource(82, 115, 173);
    private final ColorUIResource primary3 = new ColorUIResource(173, 173, 255);

    private final ColorUIResource secondary1 = new ColorUIResource(107, 107, 107);
    private final ColorUIResource secondary2 = new ColorUIResource(173, 173, 173);
    private final ColorUIResource secondary3 = new ColorUIResource(222, 222, 222);

    protected final ColorUIResource getPrimary1() {
        return primary1;
    }

    protected final ColorUIResource getPrimary2() {
        return primary2;
    }

    protected final ColorUIResource getPrimary3() {
        return primary3;
    }

    protected final ColorUIResource getSecondary1() {
        return secondary1;
    }

    protected final ColorUIResource getSecondary2() {
        return secondary2;
    }

    protected final ColorUIResource getSecondary3() {
        return secondary3;
    }

}
