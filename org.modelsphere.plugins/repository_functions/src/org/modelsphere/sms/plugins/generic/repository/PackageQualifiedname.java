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

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.*;
import org.modelsphere.sms.oo.java.db.DbJVPackage;

/**
 * For a package, return its qualified name. <br>
 * Target System : <b>Java</b><br>
 * Type : <b>User Function</b><br>
 * Parameters : none.<br>
 * Note : for a package 'jack' under package 'com.silverun', return its qualified name, i.e. the
 * name of the package prefixed with its superpackage, which in this case is the string
 * ''org.modelsphere.jack''.<br>
 */
public final class PackageQualifiedname extends UserDefinedField {
    private static final PluginSignature signature = new PluginSignature("PackageQualifiedname",
            "$Revision: 4 $", ApplicationContext.APPLICATION_AUTHOR, "$Date: 2009/04/14 14:00p $",
            212); // NOT LOCALIZABLE

    public PackageQualifiedname() {
    } //Parameter-less constructor required by jack.io.Plugins

    //TODO: two kinds of user functions: property-based or parameter-based
    public PackageQualifiedname(String rulename, String subrule, Modifier[] modifiers)
            throws RuleException {
        super(rulename, subrule, modifiers);
    }

    public PluginSignature getSignature() {
        return signature;
    }

    public boolean expand(Writer output, Serializable object, Rule.RuleOptions options)
            throws IOException, RuleException {
        boolean expanded = false;
        boolean done = false;

        if (!(object instanceof DbJVPackage)) {
            return false;
        }

        DbJVPackage pack = (DbJVPackage) object;

        try {
            String packagePath = pack.getName();
            while (!done) {
                DbObject composite = pack.getComposite();
                if (composite instanceof DbJVPackage) {
                    pack = (DbJVPackage) composite;
                    packagePath = pack.getName() + "." + packagePath;
                    object = pack;
                } else {
                    done = true;
                } //end if
            } //end while

            //write the package clause
            output.write(packagePath);
            expanded = true;
        } catch (DbException ex) {
            throw new RuleException(ex.getMessage());
        } //end try

        return expanded;
    }
} //end of CompilationunitPackage
