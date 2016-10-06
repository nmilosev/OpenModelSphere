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

import java.io.*;
import java.lang.reflect.Method;

import org.modelsphere.jack.plugins.PluginMgr;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.*;

/**
 * Returns the output directory of XMI files. <br>
 * Target System : <b>Java</b><br>
 * Type : <b>User Function</b><br>
 * Parameters : none.<br>
 * Note: requires XMI plug-in.<br>
 */
public final class XmiOutputDirectory extends UserDefinedField {

    private static final PluginSignature signature = new PluginSignature("XmiOutputDirectory",
            "$Revision: 4 $", ApplicationContext.APPLICATION_AUTHOR, "$Date: 2009/04/14 14:00p $",
            212); // NOT LOCALIZABLE

    public PluginSignature getSignature() {
        return signature;
    }

    public XmiOutputDirectory() {
    } //Parameter-less constructor required by jack.io.Plugins

    public XmiOutputDirectory(String ruleName, String subRule, Modifier[] modifiers)
            throws RuleException {
        super(ruleName, subRule, modifiers);
    }

    public boolean expand(Writer output, Serializable object, Rule.RuleOptions options)
            throws IOException, RuleException {
        boolean expanded = false;

        Class xmlForwardToolkitClass = PluginMgr.getSingleInstance().findActivePluginClass(
                "org.modelsphere.sms.plugins.xml.XmlForward",
                "org.modelsphere.sms.plugins.xml.forward.XmlForwardToolkit");
        File directory = null;
        if (xmlForwardToolkitClass == null)
            return false;
        try {
            Method getStaticSelectedDirectoryMethod = xmlForwardToolkitClass.getMethod(
                    "getStaticSelectedDirectory", new Class[] {});
            if (getStaticSelectedDirectoryMethod == null)
                return false;
            directory = (File) getStaticSelectedDirectoryMethod.invoke(null, null);
        } catch (Exception e) {
            return false;
        } //end try

        if (directory == null)
            return false;

        String dir = directory.getPath();
        output.write(dir);
        expanded = true;

        return expanded;
    } //end expand()

} //end JavaOutputDirectory
