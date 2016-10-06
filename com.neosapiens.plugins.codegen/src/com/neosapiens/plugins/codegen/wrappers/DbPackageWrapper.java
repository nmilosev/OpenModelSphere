/*************************************************************************

Copyright (C) 2009 by neosapiens inc.

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

You can reach neosapiens inc. at: 

neosapiens inc.
1236 Gaudias-Petitclerc
Qu&eacute;bec, Qc, G1Y 3G2
CANADA
Telephone: 418-561-8403
Fax: 418-650-2375
http://www.neosapiens.com/
Email: marco.savard@neosapiens.com
       gino.pelletier@neosapiens.com

 **********************************************************************/

package com.neosapiens.plugins.codegen.wrappers;

import java.util.ArrayList;
import java.util.List;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVPackage;

public class DbPackageWrapper extends DbObjectWrapper {
    private DbJVPackage m_pack;

    public DbPackageWrapper(DbJVPackage pack) {
        m_pack = pack;
    }

    //e.g. "base", w/o prefix
    public StringWrapper getBaseName() {
        String name;

        try {
            name = m_pack.getName();
        } catch (DbException ex) {
            name = "???";
        }

        return new StringWrapper(name);
    }

    //e.g. "org.modelsphere.base", prefix included
    public StringWrapper getQualifiedName() {
        String name;

        try {
            name = (m_pack == null) ? "" : m_pack.buildFullNameString();
        } catch (DbException ex) {
            name = "???";
        }

        return new StringWrapper(name);
    }

    //e.g. "org.modelsphere", base name removed
    public StringWrapper getPrefix() {
        String prefix;

        try {
            String name = m_pack.buildFullNameString();
            int idx = name.lastIndexOf('.');
            prefix = (idx == -1) ? "" : name.substring(0, idx);
        } catch (DbException ex) {
            prefix = "???";
        }

        return new StringWrapper(prefix);
    }

    public StringWrapper getDeclaration() {
        if (m_pack == null)
            return new StringWrapper("");

        String name = null;
        try {
            name = m_pack.buildFullNameString();
        } catch (DbException ex) {
            name = "???";
        }
        String declaration = "package " + name + ";";

        return new StringWrapper(declaration);
    }

    public List<String> getSubpackages() throws DbException {
        List<String> subpackages = new ArrayList<String>();
        subpackages.add("modelsphere");
        subpackages.add("samples");
        subpackages.add("purchasing");
        return subpackages;
    }

    public List<DbClassWrapper> getClasses() throws DbException {
        List<DbClassWrapper> classes = new ArrayList<DbClassWrapper>();

        //for each field
        DbRelationN relN = m_pack.getComponents();
        DbEnumeration enu = relN.elements(DbJVClass.metaClass);
        while (enu.hasMoreElements()) {
            DbJVClass o = (DbJVClass) enu.nextElement();
            DbClassWrapper claz = new DbClassWrapper(o);
            classes.add(claz);
        } //end while
        enu.close();

        return classes;
    } //end getClasses()

} //end DbPackageWrapper
