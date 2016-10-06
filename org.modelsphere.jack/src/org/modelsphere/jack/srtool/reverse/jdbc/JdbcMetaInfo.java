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

package org.modelsphere.jack.srtool.reverse.jdbc;

/**************************************
 * Should be adjusted if JDBC API changes Based on version: 2.0
 **************************************/
public final class JdbcMetaInfo {

    // JDBC String patterns
    public static String ZERO_OR_MORE = "%"; // name pattern
    // JDBC driver levels
    private static int LEVEL1DRIVER = 1;
    private static int LEVEL2DRIVER = 2;
    // JDBC Table types
    public static final String JDBCTABLE = "TABLE"; // NOT LOCALIZABLE
    public static final String JDBCVIEW = "VIEW"; // NOT LOCALIZABLE
    public static final String JDBCSYSTEMTABLE = "SYSTEM TABLE"; // NOT LOCALIZABLE
    public static final String JDBCTGLOBALTEMP = "GLOBAL TEMPORARY"; // NOT LOCALIZABLE
    public static final String JDBCLOCALTEMP = "LOCAL TEMPORARY"; // NOT LOCALIZABLE
    public static final String JDBCALIAS = "ALIAS"; // NOT LOCALIZABLE
    public static final String JDBCSYNONYM = "SYNONYM"; // NOT LOCALIZABLE

    // Singleton
    private static JdbcMetaInfo singleton;

    private JdbcMetaInfo() {
    };

    public static final JdbcMetaInfo getSingleton() {
        if (singleton == null)
            singleton = new JdbcMetaInfo();
        return singleton;
    }
}
