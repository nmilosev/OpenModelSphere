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

package org.modelsphere.jack.baseDb.db.srtypes;

import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.international.LocaleMgr;

public class ZoneJustification extends IntDomain {

    static final long serialVersionUID = 0;

    public static final int LEFT = 1;
    public static final int CENTER = 2;
    public static final int RIGHT = 3;

    public static final ZoneJustification[] objectPossibleValues = new ZoneJustification[] {
            new ZoneJustification(LEFT), new ZoneJustification(CENTER),
            new ZoneJustification(RIGHT) };

    public static final String[] stringPossibleValues = new String[] {
            LocaleMgr.misc.getString("Left"), LocaleMgr.misc.getString("Center"),
            LocaleMgr.misc.getString("Right") };

    public static ZoneJustification getInstance(int value) {
        return objectPossibleValues[objectPossibleValues[0].indexOf(value)];
    }

    // Parameterless constructor
    public ZoneJustification() {
    }

    protected ZoneJustification(int value) {
        super(value);
    }

    public final DbtAbstract duplicate() {
        return new ZoneJustification(value);
    }

    public final Domain[] getObjectPossibleValues() {
        return objectPossibleValues;
    }

    public final String[] getStringPossibleValues() {
        return stringPossibleValues;
    }

    public static final int getGraphicUtilAligmentValue(int value) {
        int alignment;
        switch (value) {
        case CENTER:
            alignment = GraphicUtil.CENTER_ALIGNMENT;
            break;
        case RIGHT:
            alignment = GraphicUtil.RIGHT_ALIGNMENT;
            break;
        default:
            alignment = GraphicUtil.LEFT_ALIGNMENT;
        }
        return alignment;
    }
}
