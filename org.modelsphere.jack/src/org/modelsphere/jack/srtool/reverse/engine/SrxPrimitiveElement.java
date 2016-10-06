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

package org.modelsphere.jack.srtool.reverse.engine;

import java.util.*;

public final class SrxPrimitiveElement implements SrxElement {
    // Fields used to access and convert the SRX Value to a compatible DB Value
    Class type; // The type of the parameter of the dbobject setter method.
    String fieldTag; // The tag representing the value

    public SrxPrimitiveElement(String fieldTag, Class type) {
        this.fieldTag = fieldTag;
        this.type = type;
    }

    public Object getValue(Object hookContainer, HashMap currentObject) throws Exception {
        if (fieldTag == null || currentObject == null)
            return null;
        String value = (String) currentObject.get(fieldTag);
        if (type != null)
            return convertStringValue(value);
        return null;
    }

    private Object convertStringValue(String s) throws Exception {
        if ((type == null) || (s == null) || s.trim().equals(""))
            return null;
        Object result = null;
        // try{
        if (type == Integer.class)
            result = new Integer(s);
        else if (type == Boolean.class)
            result = parseBoolean(s);
        else if (type == Double.class)
            result = new Double(s);
        else if (type == Long.class)
            result = new Long(s);
        else if (type == Float.class)
            result = new Float(s);
        else if (type == String.class)
            result = s;
        // }
        // catch (Exception e){ // No db transaction, catch all exception from
        // type convertions
        // result = ReverseBuilder.INVALID_OBJECT;
        // }
        return result;
    }

    // Parse this string value for a Boolean.
    private Boolean parseBoolean(String s) throws Exception {
        if (s == null) // ok to consider null as false????
            return Boolean.FALSE;

        String sTrim = s.trim().toLowerCase();

        // try with true, false, yes, no, and french values
        if (sTrim.equals("true"))
            return Boolean.TRUE; // NOT LOCALIZABLE - Detection of true false on
        // DBMS's
        if (sTrim.equals("false"))
            return Boolean.FALSE; // NOT LOCALIZABLE - Detection of true false
        // on DBMS's
        if (sTrim.equals("vrai"))
            return Boolean.TRUE; // NOT LOCALIZABLE - Detection of true false on
        // DBMS's
        if (sTrim.equals("faux"))
            return Boolean.FALSE; // NOT LOCALIZABLE - Detection of true false
        // on DBMS's

        if (sTrim.equals("yes"))
            return Boolean.TRUE; // NOT LOCALIZABLE - Detection of true false on
        // DBMS's
        if (sTrim.equals("no"))
            return Boolean.FALSE; // NOT LOCALIZABLE - Detection of true false
        // on DBMS's
        if (sTrim.equals("oui"))
            return Boolean.TRUE; // NOT LOCALIZABLE - Detection of true false on
        // DBMS's
        if (sTrim.equals("non"))
            return Boolean.FALSE; // NOT LOCALIZABLE - Detection of true false
        // on DBMS's

        if (sTrim.length() > 0) {
            // try with 0, 1, t, f, y, n, and french values
            if (sTrim.charAt(0) == '1' || sTrim.charAt(0) == 't' || sTrim.charAt(0) == 'v' // NOT LOCALIZABLE - Detection of
                    // true false on DBMS's
                    || sTrim.charAt(0) == 'y' || sTrim.charAt(0) == 'o') // NOT
                // LOCALIZABLE
                // -
                // Detection
                // of
                // true
                // false
                // on
                // DBMS's
                return Boolean.TRUE;
            if (sTrim.charAt(0) == '0' || sTrim.charAt(0) == 'f' || sTrim.charAt(0) == 'n') // NOT LOCALIZABLE - Detection of
                // true false on DBMS's
                return Boolean.FALSE;
        }

        // Try with the system default parsing method
        return Boolean.getBoolean(s) ? Boolean.TRUE : Boolean.FALSE;
    }

    public String getDebugInfo(HashMap currentObject) throws Exception {
        return "[SrxPrimitiveElement] tag= " + fieldTag + ", type= " + type + ", tagvalue= "
                + currentObject.get(fieldTag); // NOT
        // LOCALIZABLE
    }

}
