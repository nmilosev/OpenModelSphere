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

import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.international.LocaleMgr;

public class PageNoPosition extends IntDomain {

    static final long serialVersionUID = 4771493871611056545L;

    public static final int PAGE_NO_NONE = 0;
    public static final int PAGE_NO_TOP_LEFT = Diagram.PAGE_NO_LEFT;
    public static final int PAGE_NO_TOP_CENTER = Diagram.PAGE_NO_MIDDLE;
    public static final int PAGE_NO_TOP_RIGHT = Diagram.PAGE_NO_RIGHT;
    public static final int PAGE_NO_BOTTOM_LEFT = Diagram.PAGE_NO_LEFT | Diagram.PAGE_NO_BOTTOM;
    public static final int PAGE_NO_BOTTOM_CENTER = Diagram.PAGE_NO_MIDDLE | Diagram.PAGE_NO_BOTTOM;
    public static final int PAGE_NO_BOTTOM_RIGHT = Diagram.PAGE_NO_RIGHT | Diagram.PAGE_NO_BOTTOM;

    public static final PageNoPosition[] objectPossibleValues = new PageNoPosition[] {
            new PageNoPosition(PAGE_NO_NONE), new PageNoPosition(PAGE_NO_TOP_LEFT),
            new PageNoPosition(PAGE_NO_TOP_CENTER), new PageNoPosition(PAGE_NO_TOP_RIGHT),
            new PageNoPosition(PAGE_NO_BOTTOM_LEFT), new PageNoPosition(PAGE_NO_BOTTOM_CENTER),
            new PageNoPosition(PAGE_NO_BOTTOM_RIGHT) };

    public static final String[] stringPossibleValues = new String[] {
            LocaleMgr.misc.getString("None"), LocaleMgr.misc.getString("TopLeft"),
            LocaleMgr.misc.getString("TopCenter"), LocaleMgr.misc.getString("TopRight"),
            LocaleMgr.misc.getString("BottomLeft"), LocaleMgr.misc.getString("BottomCenter"),
            LocaleMgr.misc.getString("BottomRight") };

    public static PageNoPosition getInstance(int value) {
        return objectPossibleValues[objectPossibleValues[0].indexOf(value)];
    }

    // Parameterless constructor
    public PageNoPosition() {
    }

    protected PageNoPosition(int value) {
        super(value);
    }

    public final DbtAbstract duplicate() {
        return new PageNoPosition(value);
    }

    public final Domain[] getObjectPossibleValues() {
        return objectPossibleValues;
    }

    public final String[] getStringPossibleValues() {
        return stringPossibleValues;
    }

}
