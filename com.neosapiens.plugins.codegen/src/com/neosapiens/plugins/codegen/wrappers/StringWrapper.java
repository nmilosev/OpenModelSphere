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

public class StringWrapper {
    private String s;

    public StringWrapper(String s) {
        this.s = (s == null) ? "???" : s;
    }

    @Override
    public String toString() {
        return s;
    }

    //convert "camel_case" to "camelCase"
    public StringWrapper getCamelCase() {
        String result = "";
        boolean nextUpper = false;
        //String allLower = this.s.toLowerCase();
        for (int i = 0; i < this.s.length(); i++) {
            Character currentChar = this.s.charAt(i);
            if (currentChar == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    currentChar = Character.toUpperCase(currentChar);
                    nextUpper = false;
                }
                result = result.concat(currentChar.toString());
            }
        }

        return new StringWrapper(result);
    }

    public StringWrapper getCapitalized() {
        String capitalized = "???";

        if ((this.s != null) && (this.s.length() > 1)) {
            capitalized = Character.toUpperCase(this.s.charAt(0)) + this.s.substring(1);
        } //end if

        return new StringWrapper(capitalized);
    }

    public StringWrapper getUncapitalized() {
        String uncapitalized = "???";

        if ((this.s != null) && (this.s.length() > 1)) {
            uncapitalized = Character.toLowerCase(this.s.charAt(0)) + this.s.substring(1);
        } //end if
        return new StringWrapper(uncapitalized);
    }

}
