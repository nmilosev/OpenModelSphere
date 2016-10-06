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

package org.modelsphere.sms.or.features.dbms;

import java.io.StringWriter;
import java.util.StringTokenizer;

import org.modelsphere.jack.srtool.services.ConnectionMessage;
import org.modelsphere.sms.or.international.LocaleMgr;

public final class DBMSUtil {
    public static final String NULL_USER = LocaleMgr.misc.getString("UnknownUser");

    private DBMSUtil() {
    }

    public static String getConnectionAsString(ConnectionMessage cm) {
        if (cm == null)
            return "";

        String dbinfo = "";
        dbinfo += cm.server;
        dbinfo += " (" + cm.databaseProductName; // NOT LOCALIZABLE
        dbinfo += ", " + cm.databaseProductVersion + ")"; // NOT LOCALIZABLE

        return dbinfo;
    }

    public static String enquoteDBMSValue(String value, String quote) {
        return enquoteDBMSValue(value, quote, null, null, null);
    }

    public static String enquoteDBMSValue(String value, String quote, String type,
            String[] quotedTypes, String[] systemFunctions) {
        // Type requires quotes
        String quotedValue = value;
        boolean enquote = false;

        if (quote == null || quote.length() == 0 || value == null || value.length() == 0)
            return quotedValue;

        if ((type != null) && (quotedTypes != null)) {
            for (int i = 0; i < quotedTypes.length; i++) {
                String typeName = quotedTypes[i];
                if (type.equalsIgnoreCase(typeName)) {
                    if (systemFunctions == null)
                        enquote = true;
                    else {
                        for (int j = 1; j < systemFunctions.length; j++) {
                            String systemFunction = (String) systemFunctions[j];
                            if (value.indexOf(systemFunction, 0) == -1)
                                enquote = true;
                            else {
                                enquote = false;
                                break;
                            }
                        } // en for
                    }
                    break;
                }// End type is found
            }
        } else
            // Type are not checked, systematically enquote
            enquote = true;

        if (value.startsWith(quote) && value.endsWith(quote))
            enquote = false;

        if (enquote) {
            String buffer = new String(quote);
            String delims = quote;
            StringTokenizer st = new StringTokenizer(value, delims, true);
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if (token.equals(quote))
                    buffer += quote;
                buffer += token;
            }
            buffer += quote;
            quotedValue = buffer;
        }
        return quotedValue;
    }

    // dequote the following String.
    public static String dequoteDBMSValue(String oldvalue) {
        String value = oldvalue == null ? null : new String(oldvalue);
        try {
            if (value == null)
                return null;
            if (value.length() == 0 || (value.indexOf("'") == -1 && value.indexOf("\"") == -1))
                return value;
            value = value.trim();
            int lastsinglequoteidx = value.lastIndexOf("'");
            int firstsinglequoteidx = value.indexOf("'");
            int lastdoublequoteidx = value.lastIndexOf("\"");
            int firstdoublequoteidx = value.indexOf("\"");
            if ((lastsinglequoteidx == firstsinglequoteidx && lastdoublequoteidx == firstdoublequoteidx)
                    || (lastsinglequoteidx == -1 && lastdoublequoteidx == -1))
                return value;

            char quotedchar = lastsinglequoteidx > lastdoublequoteidx ? '\'' : '\"';
            // Remove last quote
            if (value.charAt(value.length() - 1) != quotedchar)
                return value;
            int lastidx = lastsinglequoteidx > lastdoublequoteidx ? lastsinglequoteidx
                    : lastdoublequoteidx;
            value = value.substring(0, value.length() - 1);

            // Remove the first quotedchar not doubled and replace all double
            // quotedchar with a single quotedchar
            String sQuotedChar = new String(new char[] { quotedchar });
            StringTokenizer st = new StringTokenizer(value, sQuotedChar, true);
            StringWriter writer = new StringWriter();
            boolean first = true;
            boolean previousIsQuotedChar = false;
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if (token.equals(sQuotedChar)) {
                    if (first) { // First quote - skip
                        first = false;
                    } else if (previousIsQuotedChar) { // doubled - skip
                        previousIsQuotedChar = false;
                    } else {
                        writer.write(token);
                        previousIsQuotedChar = true;
                    }
                } else
                    writer.write(token);
            }
            value = writer.toString();
            return value;
        } catch (Exception e) {
            return oldvalue;
        }
    }

}
