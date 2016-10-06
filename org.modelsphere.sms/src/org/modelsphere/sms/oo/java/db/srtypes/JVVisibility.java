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

package org.modelsphere.sms.oo.java.db.srtypes;

import org.modelsphere.jack.baseDb.db.srtypes.DbtAbstract;
import org.modelsphere.jack.baseDb.db.srtypes.Domain;
import org.modelsphere.jack.util.DiscreteValuesComparator;
import org.modelsphere.sms.oo.db.srtypes.OOVisibility;
import org.modelsphere.sms.oo.java.international.LocaleMgr;

public final class JVVisibility extends OOVisibility {

    static final long serialVersionUID = 0;

    //range from (1 to LAST_OO_VISIBILITY) reserved in superclass
    public static final int DEFAULT = LAST_OO_VISIBILITY + 1; //package visibility, java specific
    public static final int PRIVATE_PROTECTED = LAST_OO_VISIBILITY + 2; //required in Java 1.0, if we support it

    public static final JVVisibility[] objectPossibleValues = new JVVisibility[] {
            new JVVisibility(PUBLIC), new JVVisibility(PRIVATE), new JVVisibility(PROTECTED),
            new JVVisibility(DEFAULT) };

    public static final String[] stringPossibleValues = new String[] {
            LocaleMgr.misc.getString("public"), LocaleMgr.misc.getString("private"),
            LocaleMgr.misc.getString("protected"), LocaleMgr.misc.getString("defaultAccess") };

    private static DiscreteValuesComparator comparator = null;

    public static final DiscreteValuesComparator getComparator() {
        if (comparator == null) {
            comparator = new DiscreteValuesComparator(new Object[] { getInstance(PRIVATE),
                    getInstance(DEFAULT), getInstance(PROTECTED), getInstance(PUBLIC) });
        }
        return comparator;
    }

    public static JVVisibility getInstance(int value) {
    	int visib = objectPossibleValues[0].indexOf(value);
    	if (visib == -1) {
    		visib = 0;
    	}
        return objectPossibleValues[visib];
    }

    //Constructors
    protected JVVisibility(int value) {
        super(value);
    }

    //Parameterless constructor
    public JVVisibility() {
    }

    public DbtAbstract duplicate() {
        return new JVVisibility(value);
    }

    public Domain[] getObjectPossibleValues() {
        return objectPossibleValues;
    }

    public String[] getStringPossibleValues() {
        return stringPossibleValues;
    }
}
