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

package org.modelsphere.jack.srtool.reverse;

import java.io.Serializable;

public final class JdbcMetaColumn implements Serializable {
    // Attributes
    public static final String REMARKS = "REMARKS"; // NOT LOCALIZABLE
    public static final String TABLE_CAT = "TABLE_CAT"; // NOT LOCALIZABLE
    public static final String TABLE_SCHEM = "TABLE_SCHEM"; // NOT LOCALIZABLE
    public static final String TABLE_NAME = "TABLE_NAME"; // NOT LOCALIZABLE
    public static final String TABLE_TYPE = "TABLE_TYPE"; // NOT LOCALIZABLE

    // Types
    public static final int STRING_TYPE = 1;
    public static final int INT_TYPE = 2;
    public static final int SHORT_TYPE = 3;

    private int JdbcMetaColumnIndex;
    private String JdbctMetaColumnName;
    private int JdbcMetaColumnType;
    private boolean JdbcMetaColumnUsed = true;
    private String JdbcMetaColumnComment = null;

    public JdbcMetaColumn(int anIndex, String aName, int aType) {
        this.JdbcMetaColumnIndex = anIndex;
        this.JdbctMetaColumnName = aName;
        this.JdbcMetaColumnType = aType;
    }

    public JdbcMetaColumn(int anIndex, String aName, int aType, boolean used) {
        this(anIndex, aName, aType);
        this.JdbcMetaColumnUsed = used;
    }

    public JdbcMetaColumn(int anIndex, String aName, int aType, boolean used, String aComment) {
        this(anIndex, aName, aType, used);
        this.JdbcMetaColumnComment = aComment;
    }

    public int getJdbcMetaColumnIndex() {
        // Will return the index only if used
        if (JdbcMetaColumnUsed)
            return JdbcMetaColumnIndex;
        return -1;
    }

    public String getJdbcMetaColumnName() {
        // Will return the type only if used
        if (JdbcMetaColumnUsed)
            return JdbctMetaColumnName;
        return "";
    }

    public int getJdbcMetaColumnType() {
        // Will return the type only if used
        if (JdbcMetaColumnUsed)
            return JdbcMetaColumnType;
        return -1;
    }

}
