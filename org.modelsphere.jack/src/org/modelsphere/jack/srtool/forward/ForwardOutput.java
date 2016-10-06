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

package org.modelsphere.jack.srtool.forward;

import java.io.*;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.srtool.forward.Rule.RuleOptions;

public final class ForwardOutput {
    JackForwardEngineeringPlugin forward;
    Writer writer;
    MetaField metaField;

    public ForwardOutput(JackForwardEngineeringPlugin aForward, Writer aWriter, MetaField aMetaField) {
        forward = aForward;
        writer = aWriter;
        metaField = aMetaField;
    }

    // can be overriden
    public void setWriter(Writer aWriter) {
        writer = aWriter;
    }

    public Writer getWriter() {
        return writer;
    }

    public boolean process(DbObject semObj) throws DbException, IOException, RuleException {
        boolean hasExpanded = false;

        // Forward compilation unit
        Rule rule = forward.getRuleOf(semObj);
        if (rule != null) {
            RuleOptions options = null;
            hasExpanded = rule.expand(writer, semObj, options);
        }
        writer.close();

        // If StringWriter
        if ((hasExpanded) && (metaField != null)) {
            StringWriter stringWriter = (StringWriter) writer;
            semObj.set(metaField, stringWriter.toString());
        }

        return hasExpanded;
    } // end process()

} // end of ForwardOutput
