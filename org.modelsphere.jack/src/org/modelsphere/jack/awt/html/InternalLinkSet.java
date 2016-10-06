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
import java.util.ArrayList;
import java.util.HashMap;

import org.modelsphere.jack.baseDb.db.DbObject;

public final class InternalLinkSet {
    HashMap map = new HashMap(); // Map to array List - the key is the dbo
    ArrayList links = new ArrayList();
    int nextKey = 0;

    public InternalLinkSet() {
    }

    public InternalLink createInternalLink(DbObject dbo, String action, String text) {
        return createInternalLink(dbo, action, text, null);
    }

    public InternalLink createInternalLink(DbObject dbo, String action, String text, URL imageUrl) {
        if (dbo == null)
            return null;

        ArrayList dbolinks = (ArrayList) map.get(dbo);
        if (dbolinks == null) {
            dbolinks = new ArrayList(2);
            map.put(dbo, dbolinks);
        }
        InternalLink link = new InternalLink(nextKey, dbo, action, text, imageUrl);
        nextKey++;
        links.add(link);
        dbolinks.add(link);
        return link;
    }

    /*
     * pm
     */
    public InternalLink createActionLink(String action, String text, URL imageUrl) {
        InternalLink link = new InternalLink(nextKey, action, text, imageUrl);
        nextKey++;
        links.add(link);
        return link;
    }

    public InternalLink getInternalLinkFor(String slink) {
        if (slink == null || slink.length() < "key=0".length()) // NOT
            // LOCALIZABLE
            return null;
        int idx = slink.toLowerCase().indexOf("key="); // NOT LOCALIZABLE
        slink = slink.substring(idx + "key=".length(), slink.length()); // NOT
        // LOCALIZABLE
        String number = "";
        idx = 0;
        while (idx < slink.length()) {
            char c = slink.charAt(idx);
            if (!Character.isDigit(c))
                break;
            number = number.concat(new String(new char[] { c }));
            idx++;
        }

        if (number.length() == 0)
            return null;

        int key = -1;
        try {
            key = Integer.parseInt(number);
        } catch (Exception e) {
            return null;
        }

        if (key < 0 || key >= links.size())
            return null;

        return (InternalLink) links.get(key);
    }

}
