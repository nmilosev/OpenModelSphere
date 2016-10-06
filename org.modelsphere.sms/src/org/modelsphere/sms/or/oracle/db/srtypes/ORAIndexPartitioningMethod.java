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

/*
 * Created on Oct 3, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.modelsphere.sms.or.oracle.db.srtypes;

import org.modelsphere.jack.baseDb.db.srtypes.DbtAbstract;
import org.modelsphere.jack.baseDb.db.srtypes.Domain;
import org.modelsphere.jack.baseDb.db.srtypes.IntDomain;
import org.modelsphere.sms.or.oracle.international.LocaleMgr;

/**
 * @author nicolask
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences -
 *         Java - Code Style - Code Templates
 */
public class ORAIndexPartitioningMethod extends IntDomain {

    static final long serialVersionUID = 0;

    public static final int RANGE = 1;
    public static final int HASH = 2;

    public static final ORAIndexPartitioningMethod[] objectPossibleValues = new ORAIndexPartitioningMethod[] {
            new ORAIndexPartitioningMethod(RANGE), new ORAIndexPartitioningMethod(HASH) };

    public static final String[] stringPossibleValues = new String[] {
            LocaleMgr.misc.getString("range"), LocaleMgr.misc.getString("hash") };

    public static ORAIndexPartitioningMethod getInstance(int value) {
        return objectPossibleValues[objectPossibleValues[0].indexOf(value)];
    }

    protected ORAIndexPartitioningMethod(int value) {
        super(value);
    }

    // Parameterless constructor
    public ORAIndexPartitioningMethod() {
    }

    public DbtAbstract duplicate() {
        return new ORAIndexPartitioningMethod(value);
    }

    public Domain[] getObjectPossibleValues() {
        return objectPossibleValues;
    }

    public String[] getStringPossibleValues() {
        return stringPossibleValues;
    }

    public String getStringForNullValue() {
        return org.modelsphere.jack.baseDb.screen.DefaultRenderer.kUnspecified;
    }
}
