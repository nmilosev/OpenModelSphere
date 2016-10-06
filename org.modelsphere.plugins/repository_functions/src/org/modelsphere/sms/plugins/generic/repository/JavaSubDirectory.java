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
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVCompilationUnit;
import org.modelsphere.sms.oo.java.db.DbJVPackage;

/**
 * For a compilation unit, returns its subdirectory. <br>
 * Target System : <b>Java</b><br>
 * Type : <b>User Function</b><br>
 * Parameters : none.<br>
 * Note : for the compilation unit Application under packager org.modelsphere.sms, this function
 * will return the subdirectory where Application.java should be generated, which is in this case
 * ''org\\grandite\\sms''. The directory separator ('\\' on Windows) varies according the operating
 * system..<br>
 */
public final class JavaSubDirectory extends UserDefinedField {

    private static final String SEPARATOR = System.getProperty("file.separator"); // NOT LOCALIZABLE - key
    private static final PluginSignature signature = new PluginSignature("JavaSubDirectory",
            "$Revision: 10 $", ApplicationContext.APPLICATION_AUTHOR, "$Date: 2009/04/14 14:00p $",
            212); // NOT LOCALIZABLE

    public PluginSignature getSignature() {
        return signature;
    }

    public JavaSubDirectory() {
    } //Parameter-less constructor required by jack.io.Plugins

    public JavaSubDirectory(String ruleName, String subrule, Modifier[] modifiers)
            throws RuleException {
        super(ruleName, subrule, modifiers);
    }

    private String getPackageName(DbJVClass claz) throws DbException {
        String subDir = "";
        boolean done = false;
        DbObject object = claz;

        while (!done) {
            DbObject composite = object.getComposite();
            if (composite instanceof DbJVPackage) {
                DbJVPackage pack = (DbJVPackage) composite;
                subDir = pack.getName() + SEPARATOR + subDir;
                object = pack;
            } else {
                done = true;
            } //end if
        } //end while
        return subDir;
    }

    public boolean expand(Writer output, Serializable object, Rule.RuleOptions options)
            throws IOException, RuleException {
        boolean expanded = false;

        try {
            //get the first class associated to this compilation unit
            DbJVCompilationUnit compilUnit = (DbJVCompilationUnit) object;
            DbRelationN relN = compilUnit.getClasses();
            DbEnumeration dbEnum = relN.elements();
            DbJVClass claz = null;
            if (dbEnum.hasMoreElements()) {
                claz = (DbJVClass) dbEnum.nextElement();
            }

            while (dbEnum.hasMoreElements()) {
                dbEnum.nextElement(); //just skip other classes
            } //end while
            dbEnum.close();

            //get class' package (anyway, all classes contained in the same
            //compilation unit should belong tp the same package!)
            String subDir = getPackageName(claz);
            output.write(subDir);
            expanded = true;

        } catch (DbException ex) {
            throw new RuleException(ex.getMessage());
        } //end try

        return expanded;
    } //end expand()

} //JavaSubDirectory
