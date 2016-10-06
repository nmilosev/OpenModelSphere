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
package org.modelsphere.jack.baseDb.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class TerminologyManager {

    private HashMap notationMap = null;
    protected static TerminologyManager g_singleInstance = null; // singleton

    //
    // protected methods
    // 
    protected TerminologyManager() {
        notationMap = new HashMap();
    }

    //
    // abstract methods
    // 
    abstract public void initTerminology(Terminology terminology, DbObject dbObject)
            throws DbException;

    abstract public void initTerminology(Terminology terminology, String name) throws DbException;

    abstract public String getTerminologyName(DbObject notation) throws DbException;

    //
    // public methods
    // 
    public static TerminologyManager getInstance() {
        if (g_singleInstance == null) {
            throw new RuntimeException("There is no instance.");
        }

        return g_singleInstance;
    } // getSingleInstance()

    public Terminology createTerminology(DbObject notation) {
        Terminology terminology;
        try {
            notation.getDb().beginReadTrans();
            String key = getTerminologyName(notation);
            terminology = new Terminology();
            notationMap.put(key, terminology);
            initTerminology(terminology, notation);
            notation.getDb().commitTrans();
        } catch (DbException ex) {
            terminology = null;
        }

        return terminology;
    }

    public Terminology createTerminology(String name) {
        Terminology terminology;
        try {
            terminology = new Terminology();
            notationMap.put(name, terminology);
            initTerminology(terminology, name);
        } catch (DbException ex) {
            terminology = null;
        }

        return terminology;
    }

    public Terminology findTerminology(DbObject notation) throws DbException {
        if (notation == null)
            return null;

        Terminology terminology;

        String key = getTerminologyName(notation);
        terminology = (Terminology) notationMap.get(key);

        if (terminology == null) // creates "default" if required
            terminology = createTerminology(notation);

        return terminology;
    } // end findTerminology()

    public Terminology findTerminologyByName(String terminologyName) {
        Set keys = notationMap.keySet();
        Terminology terminology = null;
        if (terminologyName == null)
            return null;

        boolean done = false;
        Iterator iter = keys.iterator();
        while (iter.hasNext() && !done) {
            String notationName = (String) iter.next();
            if (notationName != null) {
                if (notationName.equals(terminologyName)) {
                    terminology = (Terminology) notationMap.get(notationName);
                    done = true;
                }
            }
        }
        if (terminology == null) // creates "default" if required
            terminology = createTerminology(terminologyName);

        return terminology;
    } // end findTerminologyByName()

}
