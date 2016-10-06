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

import org.modelsphere.jack.io.XmlWriter;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.*;

/**
     Returns a converted string according SGML rules.
     <br>
     Target System : <b>All</b><br>
     Type : <b>User Function</b><br>
     Parameters : a string to be converted.<br>
     Note : This function converts a ASCII text into a SGML text by replacing each
     '<' character by the metacharacter '&amp;lt;', each '>' character by the
     metacharacter '&amp;gt;' and each '&' character by the metacharacter '&amp;amp;'.
     Converts also French diacritical characters into valid SGML tags.
     Suitable when you have to generate a text (such as a Java method's body or
     a view's selection rule containing '<', '>' or '&' characters into a
     HTML or XML format.<br>
  */
public final class UtilXMLConverter extends UserDefinedField {
    private static final PluginSignature signature = new PluginSignature("UtilXMLConverter",
            "$Revision: 4 $", ApplicationContext.APPLICATION_AUTHOR, "$Date: 2009/04/14 14:00p $",
            212); // NOT LOCALIZABLE

    public UtilXMLConverter() {
    } //Parameter-less constructor required by jack.io.Plugins

    //TODO: two kinds of user functions: property-based or parameter-based
    public UtilXMLConverter(String rulename, String subRule, Modifier[] modifiers)
            throws RuleException {
        super(rulename, subRule, modifiers);
    }

    public PluginSignature getSignature() {
        return signature;
    }

    public boolean expand(Writer output, Serializable object, Rule.RuleOptions options)
            throws IOException, RuleException {
        boolean expanded = false;

        //get the raw text
        Rule r = getSubRule();

        if (r != null) {
            XmlWriter writer = new XmlWriter();
            r.expand(writer, object);
            String convertedText = writer.toString();

            //write the converted text
            output.write(convertedText);
            expanded = true;
        } //end if

        return expanded;
    }
} //end of UtilXMLConverter
