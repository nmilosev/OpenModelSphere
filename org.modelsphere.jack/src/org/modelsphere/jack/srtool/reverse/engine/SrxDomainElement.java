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

public final class SrxDomainElement implements SrxElement {
    // Fields used to access and convert the SRX Value to a compatible DB Value
    private DomainMapping domainMapping; // The domain mapping to use for
    // setting the correct object value
    // on the metafield
    private String fieldTag; // The tag representing the value

    public SrxDomainElement(String fieldTag, DomainMapping domainMapping) {
        this.fieldTag = fieldTag;
        this.domainMapping = domainMapping;
    }

    public Object getValue(Object hookContainer, HashMap currentObject) throws Exception {
        if (fieldTag == null || domainMapping == null)
            return null;
        String value = (String) currentObject.get(fieldTag);
        return domainMapping.get(value);
    }

    public String getDebugInfo(HashMap currentObject) throws Exception {
        return "[SrxDomainElement] tag= " + fieldTag + ", domainMapping= " + domainMapping
                + ", tagvalue= " + currentObject.get(fieldTag); // NOT LOCALIZABLE
    }
}
