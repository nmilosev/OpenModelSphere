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

import java.awt.Font;

public final class SrFont extends SrType {

    static final long serialVersionUID = -3693522825687119652L;

    String name;
    int style;
    int size;
    /*
     * Since SrFont is immutable, we are sure the Font cannot be changed; so we can keep a transient
     * Java representation of the Font without bothering about refreshing it.
     */
    private transient Font jFont;

    // Parameterless constructor
    public SrFont() {
    }

    public SrFont(Font font) {
        name = font.getName();
        style = font.getStyle();
        size = font.getSize();
        jFont = font;
    }

    public final Object toApplType() {
        if (jFont == null) // needed for c/s
            jFont = new Font(name, style, size);
        return jFont;
    }

    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof SrFont))
            return false;
        SrFont font = (SrFont) obj;
        return (name.equals(font.name) && style == font.style && size == font.size);
    }

    public String toString() {
        String strStyle;
        if ((style & Font.BOLD) != 0)
            strStyle = ((style & Font.ITALIC) != 0 ? "bolditalic" : "bold"); // NOT
        // LOCALIZABLE
        else
            strStyle = ((style & Font.ITALIC) != 0 ? "italic" : "plain"); // NOT
        // LOCALIZABLE
        return "name=" + name + ", style=" + strStyle + ", size=" + size; // NOT
        // LOCALIZABLE
    }
}
