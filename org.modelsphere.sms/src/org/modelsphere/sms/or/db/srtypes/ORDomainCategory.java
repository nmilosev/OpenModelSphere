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
// Possible Values :
// DISTINCT
// STRUCTURED
// OPAQUE
// COLLECTION
package org.modelsphere.sms.or.db.srtypes;

import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.international.LocaleMgr;

public class ORDomainCategory extends IntDomain {

    static final long serialVersionUID = 0;

    public static final int DISTINCT = 1;
    public static final int STRUCTURED = 2;
    public static final int OPAQUE = 3;
    public static final int COLLECTION = 4;
    public static final int DOMAIN = 5; // not generated
    public static final int JAVA_OBJECT = 6;

    public static final ORDomainCategory[] objectPossibleValues = new ORDomainCategory[] {
            new ORDomainCategory(DOMAIN), new ORDomainCategory(DISTINCT),
            new ORDomainCategory(STRUCTURED), new ORDomainCategory(OPAQUE),
            new ORDomainCategory(COLLECTION), new ORDomainCategory(JAVA_OBJECT), };

    public static final String[] stringPossibleValues = new String[] {
            LocaleMgr.misc.getString("domain"), LocaleMgr.misc.getString("distinct"),
            LocaleMgr.misc.getString("structured"), LocaleMgr.misc.getString("opaque"),
            LocaleMgr.misc.getString("collection"), LocaleMgr.misc.getString("javatype") };

    public static ORDomainCategory getInstance(int value) {
        return objectPossibleValues[objectPossibleValues[0].indexOf(value)];
    }

    protected ORDomainCategory(int value) {
        super(value);
    }

    //Parameterless constructor
    public ORDomainCategory() {
    }

    public final DbtAbstract duplicate() {
        return new ORDomainCategory(value);
    }

    public final Domain[] getObjectPossibleValues() {
        return objectPossibleValues;
    }

    public final String[] getStringPossibleValues() {
        return stringPossibleValues;
    }
}
