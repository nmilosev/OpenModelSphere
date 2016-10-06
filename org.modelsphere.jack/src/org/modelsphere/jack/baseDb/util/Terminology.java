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

import javax.swing.Icon;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public final class Terminology {

    private HashMap metaClassMap = new HashMap(); // per instance
    private HashMap metaClassGroupsMap = new HashMap(); // per instance
    private HashMap metaFieldMap = new HashMap(200, 50); // per instance
    private HashMap metaFieldGroupsMap = new HashMap(200, 50); // per instance

    private HashMap metaClassIconMap = new HashMap(); // per instance

    Terminology() {
    } // package-visible: instantiated by TerminologyManager

    //
    // static methods
    //
    public static Terminology createTerminology(DbObject notation) {
        TerminologyManager mgr = TerminologyManager.getInstance();
        Terminology terminology = mgr.createTerminology(notation);
        return terminology;
    }

    public static Terminology findTerminology(DbObject notation) throws DbException {
        TerminologyManager mgr = TerminologyManager.getInstance();
        Terminology terminology = mgr.findTerminology(notation);
        return terminology;
    }

    public static Terminology findTerminologyByName(String terminologyName) {
        TerminologyManager mgr = TerminologyManager.getInstance();
        Terminology terminology = mgr.findTerminologyByName(terminologyName);
        return terminology;
    }

    // //////////////////////////////////////////////////////////////////////
    // meta fields methods

    // this methods returns null when a field does not apply (it should be
    // disabled in the UI)
    public String getTerm(MetaClass metaClass, MetaField mf) {
        String sKey = metaClass.getGUIName() + "." + mf.getGUIName();
        String term = (String) metaFieldMap.get(sKey);

        if (term == null)
            term = mf.getGUIName();
        else {
            if (term.compareTo("NULL") == 0)
                term = null;
        }

        return term;
    }

    // this method returns null when a field does not apply (it should be
    // disabled in the UI)
    public String getTerm(MetaClass metaClass, MetaField mf, boolean bIsGroup) {

        if (metaClass == null)
            return mf.getGUIName();

        String sKey = metaClass.getGUIName() + "." + mf.getGUIName();
        String term = null;
        if (bIsGroup == true)
            term = (String) metaFieldGroupsMap.get(sKey);
        else {
            term = (String) metaFieldMap.get(sKey);
        }
        if (term == null)
            term = mf.getGUIName();
        else {
            if (term.compareTo("NULL") == 0)
                term = null;
        }
        return term;
    } // end getTerm()

    public void define(MetaClass mc, MetaField mf, String termSingle, String termGroup) {
        String sKey = mc.getGUIName() + "." + mf.getGUIName();
        metaFieldMap.put(sKey, termSingle);
        metaFieldGroupsMap.put(sKey, termGroup);
    }

    public void define(MetaClass mc, MetaField mf, String term) {
        String sKey = mc.getGUIName() + "." + mf.getGUIName();
        metaFieldMap.put(sKey, term);
    }

    public void define(MetaClass mc, MetaField mf, String term, boolean bIsGroup) {
        String sKey = mc.getGUIName() + "." + mf.getGUIName();
        if (bIsGroup == true)
            metaFieldGroupsMap.put(sKey, term);
        else
            define(mc, mf, term);
    }

    // //////////////////////////////////////////////////////////////////////
    // icon methods

    public Icon getIcon(MetaClass mc) {
        Icon icon = (Icon) metaClassIconMap.get(mc);
        if (icon == null)
            icon = mc.getIcon();
        return icon;
    }

    public void define(MetaClass mc, Icon icon) {
        if (icon == null)
            return;
        metaClassIconMap.put(mc, icon);
    }

    // //////////////////////////////////////////////////////////////////////
    // meta classes methods

    public String getTerm(MetaClass mc) {
        String term = (String) metaClassMap.get(mc);
        if (term == null)
            term = mc.getGUIName(false, false);

        return term;
    } // end getTerm()

    public void define(MetaClass mc, String term) {

        if (term == null)
            return;

        metaClassMap.put(mc, term);
    }

    public void define(MetaClass mc, String termSingle, String termGroup) {
        define(mc, termSingle);
        define(mc, termGroup, true);
    }

    public void define(MetaClass mc, String term, boolean bIsGroup) {

        if (term == null)
            return;

        if (true == bIsGroup)
            metaClassGroupsMap.put(mc, term);
        else
            define(mc, term);
    }

    public String getTerm(MetaClass mc, boolean bIsGroup) {

        String term = null;

        if (true == bIsGroup) {
            term = (String) metaClassGroupsMap.get(mc);
            if (term == null)
                term = mc.getGUIName(true, false);
        } else {
            term = getTerm(mc);
        }

        return term;
    } // end getTerm()

} // end Terminology()
