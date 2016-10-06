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

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.*;

/**
 * Returns the parameter in capital letters. <br>
 * Target System : <b>All</b><br>
 * Type : <b>User Function</b><br>
 * Parameter : a string to be converted in uppercase.<br>
 */
public final class UserDefinedProperty extends UserDefinedField {
    private static final PluginSignature signature = new PluginSignature("UserDefinedProperty",
            "$Revision: 8 $", ApplicationContext.APPLICATION_AUTHOR, "$Date: 2009/04/14 14:00p $",
            212); // NOT LOCALIZABLE

    public UserDefinedProperty() {
    } //Parameter-less constructor required by jack.io.Plugins

    //TODO: two kinds of user functions: property-based or parameter-based
    public UserDefinedProperty(String rulename, String subrule, Modifier[] modifiers)
            throws RuleException {
        super(rulename, subrule, modifiers);
    }

    public PluginSignature getSignature() {
        return signature;
    }

    public boolean expand(Writer output, Serializable object, Rule.RuleOptions options)
    throws IOException, RuleException {
boolean expanded = false;

//get the name of the UDP to extract the value from 
String propertyName = getPropertyName();

if (propertyName != null) {
	
    //user-defined properties apply to dbObject only
    if (object instanceof DbObject) {
        DbObject dbObject = (DbObject) object;
        Db db = dbObject.getDb();
        try {
            //get the property of object
            db.beginReadTrans();
            
            String udfVal = (String) dbObject.getUDF(propertyName);
            if (udfVal != null)
                output.write(udfVal);

            db.commitTrans();

        } catch (DbException ex) {
            //do not expand anything
        } //end try     
    } //end if

    expanded = true;
} //end if

//writer.close();
return expanded;
}

} //end of ToUpperCase
