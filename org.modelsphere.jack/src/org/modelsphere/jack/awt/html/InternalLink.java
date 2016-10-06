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

package org.modelsphere.jack.awt.html;

import java.net.URL;

import org.modelsphere.jack.baseDb.db.DbObject;

public final class InternalLink {
    int key;
    DbObject dbo;
    String action;
    String text;
    URL imageUrl;

    private InternalLink() {
    }

    InternalLink(int key, DbObject dbo, String action, String text, URL imageUrl) {
        if (dbo != null)
            this.dbo = dbo;
        this.key = key;
        this.action = action;
        if (text == null)
            text = "";
        this.text = text;
        this.imageUrl = imageUrl;
    }

    InternalLink(int key, DbObject dbo, String action, String text) {
        this(key, dbo, action, text, null);
    }

    InternalLink(int key, String action, String text, URL imageUrl) {
        this(key, null, action, text, imageUrl);
    }

    public DbObject getDbObject() {
        return dbo;
    }

    public String getAction() {
        return action;
    }

    public String toString() {
        if (imageUrl == null)
            return "<A HREF=\"key=" + new Integer(key).toString() + "\">" + text + "</A>"; // NOT LOCALIZABLE
        else
            return "<A HREF=\"key=" + new Integer(key).toString() + "\">"
                    + // NOT LOCALIZABLE
                    "<IMG SRC=\"" + imageUrl + "\" ALIGN=BOTTOM ALT=\"" + text + "\" BORDER=0>"
                    + "</A>"; // NOT LOCALIZABLE
    }

}
