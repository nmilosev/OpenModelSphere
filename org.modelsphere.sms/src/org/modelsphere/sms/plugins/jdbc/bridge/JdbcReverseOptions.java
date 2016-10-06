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

package org.modelsphere.sms.plugins.jdbc.bridge;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.modelsphere.jack.srtool.reverse.jdbc.JdbcMetaInfo;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.StringUtil;
import org.modelsphere.sms.plugins.TargetSystem;
import org.modelsphere.sms.plugins.TargetSystemInfo;
import org.modelsphere.sms.plugins.TargetSystemManager;

public class JdbcReverseOptions {

    // These TS are supported by specific plugins.
    private static final int[] jdbcUnsupportedTargetSystems = new int[] {
            TargetSystem.SGBD_IBM_DB2_ROOT, TargetSystem.SGBD_INFORMIX_ROOT,
            TargetSystem.SGBD_JAVA, TargetSystem.SGBD_ORACLE_ROOT };

    private static final String[] jdbcSupportedTableTypes = new String[] { JdbcMetaInfo.JDBCTABLE,
            JdbcMetaInfo.JDBCSYSTEMTABLE, JdbcMetaInfo.JDBCTGLOBALTEMP, JdbcMetaInfo.JDBCLOCALTEMP };

    private static final String[] jdbcSupportedViewTypes = new String[] { JdbcMetaInfo.JDBCVIEW };

    private static final int[] supportedUDT = new int[] { Types.JAVA_OBJECT, Types.STRUCT,
            Types.DISTINCT };

    private String catalogName = null;
    private String catalogTerm = null;

    public JdbcReverseOptions() {
    }

    static String[] getTableTypes() {
        return jdbcSupportedTableTypes;
    }

    static String[] getViewTypes() {
        return jdbcSupportedViewTypes;
    }

    static int[] getUDT() {
        return supportedUDT;
    }

    /*
     * JDBC BUG *** For the moment when "" is specified for the catalog, consider it to be null. An
     * inconsistent behavior in JDBC driver catalog argument (2 driver give out different catalogs
     * for the same set of tables) A null argument drops the catalog criteria which at least
     * retreives all desired tables.
     */
    public void setCatalogOption(String catalogName) {
        this.catalogName = null;
        if (!StringUtil.isEmptyString(catalogName))
            this.catalogName = catalogName;
    }

    public String getCatalogOption() {
        return catalogName;
    }

    public List getTargetSystems() {
        ArrayList tsList = new ArrayList();
        TargetSystemInfo targetInfo = null;
        Iterator iter = TargetSystemManager.getSingleton().getAllTargetSystemInfos().iterator();
        while (iter.hasNext()) {
            targetInfo = (TargetSystemInfo) iter.next();
            boolean addTarget = true;
            for (int i = 0; i < jdbcUnsupportedTargetSystems.length; i++) {
                if (jdbcUnsupportedTargetSystems[i] == targetInfo.getRootID()) {
                    addTarget = false;
                    break;
                }
            }
            if (addTarget)
                tsList.add(targetInfo);
        }
        return tsList;
    }

    public String toString() {
        String s = "\t";
        s += (catalogName == null ? "" : MessageFormat.format(LocaleMgr.misc.getString("Catalog0"),
                new Object[] { catalogName }));
        return s;
    }
}
