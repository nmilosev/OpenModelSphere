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

package org.modelsphere.sms.plugins.generic.repository;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.HashMap;

import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.*;

/**
 * For an object, returns a unique identification number. <br>
 * Target System : <b>All</b><br>
 * Type : <b>User Function</b><br>
 * Parameters : none.<br>
 * Returns : A string representing an integer.<br>
 * Note : This function maintains a table of all previous objects called with this method. Each time
 * this function is called, it looks up into its table to see whether the current object has already
 * be processed. If so, returns the same ID used at the first call. If not, increment a sequence
 * number and associates this unique ID to the current object.
 */
public final class ObjectId extends UserDefinedField {
    private static final PluginSignature signature = new PluginSignature("ObjectId",
            "$Revision: 4 $", ApplicationContext.APPLICATION_AUTHOR, "$Date: 2009/04/14 14:00p $",
            212); // NOT LOCALIZABLE

    static HashMap map = new HashMap();
    private static int counter = 0;

    public static void reset() {
        counter = 0;
        map.clear();
    }

    public ObjectId() {
    } //Parameter-less constructor required by jack.io.Plugins

    public ObjectId(String ruleName, String subRule, Modifier[] modifiers) throws RuleException {
        super(ruleName, subRule, modifiers);
    }

    public PluginSignature getSignature() {
        return signature;
    }

    public boolean expand(Writer output, Serializable object, Rule.RuleOptions options)
            throws IOException, RuleException {
        boolean expanded = false;

        Integer id;
        if (map.containsKey(object)) {
            id = (Integer) map.get(object);
        } else {
            id = new Integer(counter++);
            map.put(object, id);
        }

        if (prefixModifier != null) {
            prefixModifier.expand(output, object, options);
        }

        //write the converted text
        output.write(id.toString());
        expanded = true;

        if (suffixModifier != null) {
            suffixModifier.expand(output, object, options);
        }

        return expanded;
    }
} //end of ObjectID
