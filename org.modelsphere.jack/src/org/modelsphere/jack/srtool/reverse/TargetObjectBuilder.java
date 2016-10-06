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

package org.modelsphere.jack.srtool.reverse;

import java.io.IOException;

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.srtool.reverse.Actions;

public final class TargetObjectBuilder extends ObjectBuilder {

    private Actions actions;
    private int currentOccurrenceId = -1;
    private String currentOccIdentifier;
    private String currentCompositeIdentifier;

    // DBMS-specific action
    public TargetObjectBuilder(Actions someActions) {
        actions = someActions;
    }

    void makeComment(String cmt) throws Exception {
        Debug.trace("Comments: " + cmt);
    }

    public void makeOccurrence(String occ) throws Exception {
        // from occ, via Mapping, find dboj
        String lowercase = occ.toLowerCase();
        currentOccurrenceId = -1;

        try {
            // via actions, create it
            actions.processOccurrenceId(currentOccurrenceId, lowercase);
        } catch (IOException ex) {
        }
    }

    public void makeStringAttr(String attr, String val) throws Exception {

        // if occurrence's identifier (eg: table's name)
        if (attr.equals(currentOccIdentifier)) {
            actions.setOccIdentifier(val);
        } // else if composite's identifier (eg: column's table_name)
        else if ((currentCompositeIdentifier != null) && (attr.equals(currentCompositeIdentifier))) {
            actions.setCompositeIdentifier(val);
        } // else if an ordinary attribute (eg: column's initial value)
        else {
            actions.setAttribute(attr, val);
        }

        // Debug.trace("  attr " + attr + " = " + val);
    }

    public void makeTextAttr(String attr, String val) throws Exception {
        actions.setAttribute(attr, val);
    }
}
