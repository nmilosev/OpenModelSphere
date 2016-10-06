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

package org.modelsphere.jack.srtool.reverse.engine;

import java.util.*;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.debug.Debug;

// This class is basically a wrapper of FieldMapping.  In this case, the FieldMapping is
// used as a constraint.  So the Db Value (DbElement) defined in the FieldMapping is compare with the srx value (SrxElement
// defined in the same FieldMapping.  This class will make sure that the value of the metafield
// equals the srx value for a specified DbObject.
//
// An example would be to check the owner of the table before associating this table with another object

public final class Constraint {
    public FieldMapping fieldMapping;

    // Define a constraint where the dbElement value is verified with the
    // srxElement value
    public Constraint(DbElement dbElement, SrxElement srxElement) {
        this(new FieldMapping(dbElement, srxElement));
    }

    public Constraint(FieldMapping fieldMapping) {
        if (fieldMapping == null)
            throw new NullPointerException("Constraint: null fieldMapping not allowed."); // NOT LOCALIZABLE
        this.fieldMapping = fieldMapping;
    }

    // Compare the db value represented by the metafield with the srx value
    public boolean dBEqualsSRX(Object hookContainer, HashMap currentObject, DbObject dbo)
            throws Exception {
        if (fieldMapping == null)
            return true;

        Object dbValue = fieldMapping.dbElement.getValue(hookContainer, dbo);
        Object srxValue = fieldMapping.srxElement.getValue(hookContainer, currentObject);

        if ((dbValue == null) && (srxValue == null)) {
            // Debug.trace("Concept: "+
            // currentObject.get(ReverseBuilder.CURRENT_OBJECT_CONCEPT_NAME)
            // + "  Current Obj: "+
            // currentObject.get(ReverseBuilder.CURRENT_OBJECT) +
            // "  DbValue="+dbValue+"  SRXValue="+srxValue);
            return true;
        }

        if ((dbValue == null) || (srxValue == null)) {
            Debug.trace("Concept: "
                    + currentObject.get(ReverseBuilder.CURRENT_OBJECT_CONCEPT_NAME) // NOT LOCALIZABLE
                    + "  Current Obj: " + currentObject.get(ReverseBuilder.CURRENT_OBJECT)
                    + "  DbValue=" + dbValue + "  SRXValue=" + srxValue); // NOT LOCALIZABLE
            return false;
        }

        if (dbValue.getClass().isPrimitive())
            return dbValue.equals(srxValue);

        return (dbValue == srxValue);
    }

    // Return an explicit info String for this constraint
    public String getDebugInfo(HashMap currentObject) throws Exception {
        String s = "Constraint="; // NOT LOCALIZABLE
        if (fieldMapping != null) {
            s += "\nMetafield = " + fieldMapping.dbElement.metaField.getJName(); // NOT LOCALIZABLE
            s += "\nSrxElement = " + fieldMapping.srxElement.getDebugInfo(currentObject); // NOT LOCALIZABLE
        }
        return s;
    }

}
