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
import org.modelsphere.sms.preference.DirectoryOptionGroup;

/* Note: requires the presence of the HTML plug-in */
/**
 * Returns the output directory of html files. <br>
 * Target System : <b>All</b><br>
 * Type : <b>User Function</b><br>
 * Parameters : none.<br>
 * Note : the output directory can be selected by the user in the menu tools:options:directories.<br>
 */
public final class HtmlOutputDirectory extends UserDefinedField {

    private static final PluginSignature signature = new PluginSignature("HtmlOutputDirectory",
            "$Revision: 4 $", ApplicationContext.APPLICATION_AUTHOR, "$Date: 2009/04/14 14:00p $",
            212); // NOT LOCALIZABLE

    public PluginSignature getSignature() {
        return signature;
    }

    public HtmlOutputDirectory() {
    } //Parameter-less constructor required by jack.io.Plugins

    public HtmlOutputDirectory(String ruleName, String subrule, Modifier[] modifiers)
            throws RuleException {
        super(ruleName, subrule, modifiers);
    }

    public boolean expand(Writer output, Serializable object, Rule.RuleOptions options)
            throws IOException, RuleException {
        boolean expanded = false;

        String defDir = DirectoryOptionGroup.getHTMLGenerationDirectory();

        Class htmlForwardToolkitClass = PluginMgr.getSingleInstance().findActivePluginClass(
                "org.modelsphere.sms.plugins.html.HtmlForward",
                "org.modelsphere.sms.plugins.html.forward.HtmlForwardToolkit");
        File file = null;

        if (htmlForwardToolkitClass == null)
            return false;

        try {
            //Overrides default HTML generation directory if user has choosen another one
            Object htmlForwardToolkit = htmlForwardToolkitClass.newInstance();
            if (htmlForwardToolkit == null)
                return false;
            Method getSelectedDirectoryMethod = htmlForwardToolkitClass.getMethod(
                    "getSelectedDirectory", new Class[] {});
            if (getSelectedDirectoryMethod == null)
                return false;
            file = (File) getSelectedDirectoryMethod.invoke(htmlForwardToolkit, null);
        } catch (Exception e) {
            return false;
        } //end try

        if (file != null) {
            defDir = file.getPath();
        }

        output.write(defDir);
        expanded = true;

        return expanded;
    } //end expand()

} //end HtmlOutputDirectory
