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

package org.modelsphere.jack.srtool;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.jack.util.SrVector;

public abstract class DbApplication {

    public static DbProject getFirstProjectFor(Db db) throws DbException {
        DbProject project = null;
        db.beginTrans(Db.READ_TRANS);
        DbEnumeration dbEnum = db.getRoot().getComponents().elements(DbProject.metaClass);
        if (dbEnum.hasMoreElements())
            project = (DbProject) dbEnum.nextElement();
        dbEnum.close();
        db.commitTrans();
        return project;
    }

    public static DbProject[] getProjects() {
        SrVector projects = new SrVector();
        try {
            Db[] dbs = Db.getDbs();
            for (int i = 0; i < dbs.length; i++) {
                dbs[i].beginTrans(Db.READ_TRANS);
                DbRoot root = dbs[i].getRoot();
                DbEnumeration dbEnum = root.getComponents().elements(DbProject.metaClass);
                while (dbEnum.hasMoreElements()) {
                    DbObject dbo = dbEnum.nextElement();
                    projects.addElement(new DefaultComparableElement(dbo, dbo
                            .getSemanticalName(DbObject.SHORT_FORM)));
                }
                dbEnum.close();
                dbs[i].commitTrans();
            }
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(null, ex);
        }
        projects.sort();
        DbProject[] projectArray = new DbProject[projects.size()];
        for (int i = 0; i < projectArray.length; i++) {
            projectArray[i] = (DbProject) ((DefaultComparableElement) projects.elementAt(i)).object;
        }
        return projectArray;
    }
}
