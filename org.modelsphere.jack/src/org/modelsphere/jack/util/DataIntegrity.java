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

package org.modelsphere.jack.util;

import java.io.StringWriter;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.international.LocaleMgr;

public final class DataIntegrity {
    private DataIntegrity() {
    }

    public static boolean checkDataIntegrity(DbObject dbo, boolean fix, StringWriter report)
            throws DbException {
        return checkComposition(dbo, fix, report);
    }

    // if true, everyting is ok.
    private static boolean checkComposition(DbObject dbo, boolean fix, StringWriter report)
            throws DbException {
        boolean ok = true;
        if (dbo == null)
            return ok;
        MetaClass metaclass = dbo.getMetaClass();
        DbEnumeration dbEnum = dbo.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            DbObject component = dbEnum.nextElement();
            if (!component.getMetaClass().compositeIsAllowed(metaclass)) {
                // remove the object, this is an invalid object in the
                // composition tree
                if (report != null) {
                    report.write("<b><font color=\"#FF0000\">"
                            + LocaleMgr.message.getString("InvalidComposition_")
                            + "</font></b> <br>");
                    report.write("&nbsp;&nbsp;&nbsp;&nbsp;"
                            + LocaleMgr.message.getString("Component_")
                            + "&nbsp;<font color=\"#000080\">'"
                            + component.getSemanticalName(DbObject.LONG_FORM) + "'</font><br>"); // NOT LOCALIZABLE, html tag
                    report.write("&nbsp;&nbsp;&nbsp;&nbsp;"
                            + LocaleMgr.message.getString("ComponentMetaClass_") + "&nbsp;"
                            + component.getMetaClass().getGUIName() + "<br>");
                    report.write("&nbsp;&nbsp;&nbsp;&nbsp;"
                            + LocaleMgr.message.getString("CompositeMetaClass_") + "&nbsp;"
                            + metaclass.getGUIName() + "<br>");
                }
                if (fix) {
                    component.remove();
                    if (report != null)
                        report.write("&nbsp;&nbsp;&nbsp;&nbsp;<i><b>"
                                + LocaleMgr.message.getString("FIX_") + "</b> "
                                + LocaleMgr.message.getString("objectRemoved")
                                + " </i><br><br><br>");
                }
                ok = false;
                continue;
            }
            ok = (checkComposition(component, fix, report) && ok) ? true : false;
        }
        dbEnum.close();
        return ok;
    }
}
