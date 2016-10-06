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

package org.modelsphere.sms.or.db.srtypes;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.DbtAbstract;

public final class ORDescriptorFormat extends DbtAbstract {

    static final long serialVersionUID = -4063930785505590525L;

    boolean bold;
    boolean italic;
    boolean underline;

    public ORDescriptorFormat(boolean bold, boolean italic, boolean underline) {
        this.bold = bold;
        this.italic = italic;
        this.underline = underline;
    }

    //Parameterless constructor
    public ORDescriptorFormat() {
    }

    public final DbtAbstract duplicate() {
        return new ORDescriptorFormat(bold, italic, underline);
    }

    public final boolean isBold() {
        return bold;
    }

    public final void setBold(boolean bold) {
        this.bold = bold;
    }

    public final boolean isItalic() {
        return italic;
    }

    public final void setItalic(boolean italic) {
        this.italic = italic;
    }

    public final boolean isUnderline() {
        return underline;
    }

    public final void setUnderline(boolean underline) {
        this.underline = underline;
    }

    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ORDescriptorFormat))
            return false;
        ORDescriptorFormat format = (ORDescriptorFormat) obj;
        return (bold == format.bold && italic == format.italic && underline == format.underline);
    }

    public String toString() {
        String str = "";
        str = str + "Bold: " + (bold == true ? "true" : "false") + "| Italic: "
                + (italic == true ? "true" : "false") + "| Underline: "
                + (underline == true ? "true" : "false");
        return str;
    }

    public final void dbFetch(Db db) throws DbException {
        db.fetch(this);
    }

    public final void dbCluster(Db db, Object parent) throws DbException {
        db.cluster(this, parent);
    }
}
