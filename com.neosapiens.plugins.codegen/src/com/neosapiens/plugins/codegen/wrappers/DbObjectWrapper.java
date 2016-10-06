/*************************************************************************

Copyright (C) 2009 by neosapiens inc.

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

You can reach neosapiens inc. at: 

neosapiens inc.
1236 Gaudias-Petitclerc
Qu&eacute;bec, Qc, G1Y 3G2
CANADA
Telephone: 418-561-8403
Fax: 418-650-2375
http://www.neosapiens.com/
Email: marco.savard@neosapiens.com
       gino.pelletier@neosapiens.com

 **********************************************************************/

package com.neosapiens.plugins.codegen.wrappers;

import java.util.List;

import org.modelsphere.sms.oo.java.db.srtypes.JVVisibility;

public abstract class DbObjectWrapper {

    public String group(String prefix, Object o, String suffix, String ifEmpty) {
        String text;

        String str = (o == null) ? "" : o.toString();
        if (str.isEmpty()) {
            text = ifEmpty;
        } else {
            text = prefix + str + suffix;
        } //end if

        return text;
    }

    public String group(String prefix, List<Object> list, String separator, String suffix,
            String ifEmpty) {
        String text;

        if (list.isEmpty()) {
            text = ifEmpty;
        } else {
            text = prefix;

            for (int i = 0; i < list.size(); i++) {
                text += list.get(i).toString();
                if (i < list.size() - 1) {
                    text += separator;
                }
            } //end for
            text += suffix;
        } //end if

        return text;
    }

    protected String toString(JVVisibility visib) {
        String text;

        int value = visib.getValue();
        switch (value) {
        case JVVisibility.PUBLIC:
            text = "public";
            break;
        case JVVisibility.PROTECTED:
            text = "protected";
            break;
        case JVVisibility.PRIVATE:
            text = "private";
            break;
        default:
            text = "";
        }

        return text;
    }

    protected String buildList(String prefix, List<String> list, String separator, String suffix,
            String ifEmpty) {
        String text;

        if (list.isEmpty()) {
            text = ifEmpty;
        } else {
            text = prefix;

            for (int i = 0; i < list.size(); i++) {
                text += list.get(i);
                if (i < list.size() - 1) {
                    text += separator;
                }
            } //end for
            text += suffix;
        } //end if

        return text;
    }

}
