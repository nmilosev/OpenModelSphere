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

package org.modelsphere.sms.plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.or.db.util.AnyORObject;

// A TargetSystemInfo object stores all the information contained in a <.typ> file.
public final class TargetSystemInfo {

    private static final String kUserTargetDefaultName = LocaleMgr.screen
            .getString("UserTargetDefaultName");
    private static final String kUserTargetListName = LocaleMgr.screen
            .getString("UserTargetListName");

    private static class TypeInfo {
        String logicalType;
        int id; // aliases have same id

        TypeInfo(String logicalType, int id) {
            this.logicalType = logicalType;
            this.id = id;
        }
    }

    private String name = "";
    private String version = "";
    private int targetID = -1;
    private int rootID = -1;
    private HashMap typeMap = new HashMap(); // type to logical type mapping
    // (key = type name, object =
    // TypeInfo object)
    private HashMap logicalTypeMap = new HashMap(); // logical type to type

    // mapping (key = logical
    // type name, object = type
    // name)

    public TargetSystemInfo(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        // Parse properties section
        while (true) {
            line = getNextLineNoEOF(reader);
            int i = line.indexOf('=');
            if (i == -1)
                break;
            setProperty(line.substring(0, i).trim(), line.substring(i + 1).trim());
        }
        if (name.length() == 0 || targetID == -1 || rootID == -1)
            throw new RuntimeException("Missing properties");

        // Parse types section
        if (!line.equals("TYPES")) // NOT LOCALIZABLE
            throw new RuntimeException("Bad type section");
        parseTypes(reader);

        // Parse optional sections
        while (true) {
            line = getNextLine(reader);
            if (line == null) // no more sections
                break;
            if (line.equals("ALIASES")) // NOT LOCALIZABLE
                parseAliases(reader);
            else
                throw new RuntimeException("Unknown section " + line);
        }

        reader.close();
    }

    // TargetInfo pour les systemes cibles utilisateur
    public TargetSystemInfo(int newUserTargetID) {
        this(newUserTargetID, null, null);
    }

    // TargetInfo pour les systemes cibles utilisateur
    public TargetSystemInfo(int newUserTargetID, String targetName, String targetVersion) {
        // create properties
        name = targetName == null ? kUserTargetDefaultName : targetName;
        version = targetVersion == null ? "1.0" : targetVersion; // NOT
        // LOCALIZABLE
        targetID = newUserTargetID;
        rootID = 1000; // doit demeurer 1000
    }

    public void setUserTypeMap(String type) {
        typeMap.put(type, new TypeInfo(null, typeMap.size()));// type logique
        // null
    }

    private String getNextLineNoEOF(BufferedReader reader) throws IOException {
        String line = getNextLine(reader);
        if (line == null)
            throw new RuntimeException("File truncated");
        return line;
    }

    private String getNextLine(BufferedReader reader) throws IOException {
        String line;
        while (true) {
            line = reader.readLine();
            if (line == null) // EOF
                break;
            int i = line.indexOf("//");
            if (i != -1)
                line = line.substring(0, i);
            line = line.trim();
            if (line.length() != 0) // skip blank or comment lines
                break;
        }
        return line;
    }

    private void setProperty(String property, String value) {
        if (property.equals("NAME")) // NOT LOCALIZABLE
            name = value;
        else if (property.equals("VERSION")) // NOT LOCALIZABLE
            version = value;
        else if (property.equals("TARGET-ID")) { // NOT LOCALIZABLE
            targetID = Integer.parseInt(value);
            if (targetID == 999)
                name = kUserTargetListName;
        } else if (property.equals("ROOT-ID")) // NOT LOCALIZABLE
            rootID = Integer.parseInt(value);
        else
            throw new RuntimeException("Unknown property " + property);

        // SPECIAL CASE
        rootID = AnyORObject.getActualRootID(rootID, targetID);
    }

    // Line format for TYPE SECTION: <type, logical type> (logical type is
    // optional)
    private void parseTypes(BufferedReader reader) throws IOException {
        String line = getNextLineNoEOF(reader);
        if (!line.equals("{"))
            throw new RuntimeException("Bad type section");
        while (true) {
            line = getNextLineNoEOF(reader);
            if (line.equals("}"))
                break;
            StringTokenizer tokens = new StringTokenizer(line, ",");
            String type = tokens.nextToken().trim();
            String logicalType = null;
            if (tokens.hasMoreTokens()) {
                logicalType = tokens.nextToken().trim();
                if (logicalType.length() == 0)
                    logicalType = null;
            }
            typeMap.put(type, new TypeInfo(logicalType, typeMap.size()));
            // More than one types may map to the same logical; so map the
            // logical to the first one of them
            if (logicalType != null && !logicalTypeMap.containsKey(logicalType))
                logicalTypeMap.put(logicalType, type);
        }
    }

    // Line format for ALIAS SECTION: <alias1, alias2, ...>
    private void parseAliases(BufferedReader reader) throws IOException {
        String line = getNextLineNoEOF(reader);
        if (!line.equals("{"))
            throw new RuntimeException("Bad alias section");
        while (true) {
            line = getNextLineNoEOF(reader);
            if (line.equals("}"))
                break;
            StringTokenizer tokens = new StringTokenizer(line, ",");
            int id = -1;
            while (tokens.hasMoreTokens()) {
                String type = tokens.nextToken().trim();
                TypeInfo typeInfo = (TypeInfo) typeMap.get(type);
                if (typeInfo != null) {
                    if (id == -1)
                        id = typeInfo.id; // first alias
                    else
                        typeInfo.id = id; // set <id> of following aliases to
                    // the <id> of the first
                }
            }
        }
    }

    public final String getName() {
        return name;
    }

    public final String getVersion() {
        return version;
    }

    public final int getID() {
        return targetID;
    }

    public final int getRootID() {
        return rootID;
    }

    public final Set getTypeSet() {
        return typeMap.keySet();
    }

    public final String mapType(String type, TargetSystemInfo destTSInfo) {
        String logicalType = typeToLogical(type);
        return (logicalType != null ? destTSInfo.logicalToType(logicalType) : null);
    }

    public final String typeToLogical(String type) {
        TypeInfo typeInfo = (TypeInfo) typeMap.get(type);
        return (typeInfo != null ? typeInfo.logicalType : null);
    }

    public final String logicalToType(String logicalType) {
        return (String) logicalTypeMap.get(logicalType);
    }

    public final boolean isAlias(String type1, String type2) {
        TypeInfo typeInfo1 = (TypeInfo) typeMap.get(type1);
        TypeInfo typeInfo2 = (TypeInfo) typeMap.get(type2);
        return (typeInfo1 != null && typeInfo2 != null && typeInfo1.id == typeInfo2.id);
    }

    public final void setName(String value) {
        name = value;
    }

    public final void setVersion(String value) {
        version = value;
    }
}
