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

package org.modelsphere.sms.oo.java;

import java.util.Enumeration;
import java.util.Vector;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.sms.db.DbSMSFeature;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.util.AnyAdt;

public class JavaOverridingUpdate {

    private Vector adts = new Vector();
    private Vector circularAdts = new Vector();
    private static int LOOPS_MAX = 2048;

    // update overriding and hiding for all classes in the composition hierarchy
    // of all roots
    public JavaOverridingUpdate(DbObject[] roots) throws DbException {
        for (int i = 0; i < roots.length; i++) {
            if (roots[i] instanceof DbJVClass)
                adts.addElement(roots[i]);

            int size = adts.size();
            if (size > LOOPS_MAX) {
                break; // endless loop detection
            }

            AnyAdt.getClassesAux(adts, roots[i]);
        }
        doJavaOverridingUpdate();
    }

    private void doJavaOverridingUpdate() throws DbException {
        checkCircularity();
        removeAllOverridingLinks();
        for (Enumeration e1 = adts.elements(); e1.hasMoreElements();) {
            DbJVClass adt = (DbJVClass) e1.nextElement();
            setMemberHidingAndOverriding(adt);
        }
    }

    private void checkCircularity() throws DbException {
        Vector validatedAdts = new Vector();
        circularAdts = new Vector();
        for (Enumeration e1 = adts.elements(); e1.hasMoreElements();) {
            DbJVClass adt = (DbJVClass) e1.nextElement();
            circularAdts = AnyAdt.validateCircularity(adt, circularAdts, new Vector(),
                    validatedAdts, null);
        }
    }

    private void removeAllOverridingLinks() throws DbException {
        for (Enumeration e1 = adts.elements(); e1.hasMoreElements();) {
            DbJVClass adt = (DbJVClass) e1.nextElement();
            DbEnumeration e2 = adt.getComponents().elements(DbSMSFeature.metaClass);
            while (e2.hasMoreElements()) {
                DbSMSFeature member = (DbSMSFeature) e2.nextElement();
                DbEnumeration e3 = member.getOverriddenFeatures().elements();
                while (e3.hasMoreElements()) {
                    member.removeFromOverriddenFeatures((DbSMSFeature) e3.nextElement());
                }
                e3.close();
            }
            e2.close();
        }
    }

    // set hidding or overriding ONLY inside the same inheritance branch
    // for example: if in an adt, an inherited class method m1 implements
    // an inherited interface method m1, no link between class.m1 and
    // interface.m1
    private void setMemberHidingAndOverriding(DbJVClass adt) throws DbException {
        // a hiding or overriding member is linked with any and all overridden
        // members (and vice-versa)
        if (!circularAdts.contains(adt)) {
            DbEnumeration e1 = adt.getComponents().elements(DbSMSFeature.metaClass);
            while (e1.hasMoreElements()) {
                DbSMSFeature member = (DbSMSFeature) e1.nextElement();
                if (member instanceof DbJVDataMember || member instanceof DbJVMethod) {
                    for (Enumeration e2 = AnyAdt.foundDirectOverriddenHiddenMembers(member,
                            circularAdts).elements(); e2.hasMoreElements();) {
                        member.addToOverriddenFeatures((DbSMSFeature) e2.nextElement());
                    }
                }
            }
            e1.close();
        }
    }

}
