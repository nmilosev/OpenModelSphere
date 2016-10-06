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

import java.awt.print.*;

public final class SrPageFormat extends SrType {

    static final long serialVersionUID = 7412323484122065462L;

    int orientation;
    double x, y, width, height;
    double sheetWidth, sheetHeight;

    // Parameterless constructor
    public SrPageFormat() {
    }

    public SrPageFormat(PageFormat format) {
        orientation = format.getOrientation();
        Paper paper = format.getPaper();
        x = paper.getImageableX();
        y = paper.getImageableY();
        width = paper.getImageableWidth();
        height = paper.getImageableHeight();
        sheetWidth = paper.getWidth();
        sheetHeight = paper.getHeight();
    }

    public final Object toApplType() {
        PageFormat format = new PageFormat();
        format.setOrientation(orientation);
        Paper paper = new Paper();
        paper.setImageableArea(x, y, width, height);
        paper.setSize(sheetWidth, sheetHeight);
        format.setPaper(paper);
        return format;
    }

    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof SrPageFormat))
            return false;
        SrPageFormat format = (SrPageFormat) obj;
        return (orientation == format.orientation && x == format.x && y == format.y
                && width == format.width && height == format.height
                && sheetWidth == format.sheetWidth && sheetHeight == format.sheetHeight);
    }
}
