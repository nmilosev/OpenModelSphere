/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/
package org.modelsphere.sms;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyManager;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.db.util.TerminologyInitializer;
import org.modelsphere.sms.or.db.DbORNotation;

/**
 * @author nicolask
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences -
 *         Java - Code Style - Code Templates
 */
public final class SMSTerminologyManager extends TerminologyManager {

    public static void initInstance() {
        if (g_singleInstance == null)
            g_singleInstance = new SMSTerminologyManager();
    }

    public void initTerminology(Terminology terminology, DbObject notation) throws DbException {
    	TerminologyInitializer.initTerminology(terminology, notation); 
    }

    public void initTerminology(Terminology terminology, String name) throws DbException {
        TerminologyInitializer.initTerminology(terminology, name);
    }

    public String getTerminologyName(DbObject notation) throws DbException {
        String name = null;
        if (notation instanceof DbBENotation) {
            DbBENotation benotation = (DbBENotation) notation;
            Db db = benotation.getDb();
            if (db.getTransMode() == Db.TRANS_NONE) {
                db.beginReadTrans();
                name = benotation.getTerminologyName();
                db.commitTrans();
            } else
                name = benotation.getTerminologyName();
        } else if (notation instanceof DbORNotation) {
            DbORNotation ornotation = (DbORNotation) notation;
            Db db = ornotation.getDb();
            if (db.getTransMode() == Db.TRANS_NONE) {
                db.beginReadTrans();
                name = ornotation.getTerminologyName();
                db.commitTrans();
            } else
                name = ornotation.getTerminologyName();
        }
        return name;
    }

}
