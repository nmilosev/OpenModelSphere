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
import org.modelsphere.sms.oo.db.DbOOAdt;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.DbJVParameter;
import org.modelsphere.sms.oo.java.db.srtypes.JVVisibility;

public class DbMethodWrapper extends DbObjectWrapper {
    private DbJVMethod m_method;

    public DbMethodWrapper(DbJVMethod method) {
        m_method = method;
    }

    public List<String> getJavaVisibility() throws DbException {
        List<String> list = new ArrayList<String>();
        JVVisibility visib = (JVVisibility) m_method.getVisibility();
        list.add(toString(visib));
        return list;
    }

    public List<String> getJavaModifiers() throws DbException {
        List<String> list = new ArrayList<String>();

        if (m_method.isStatic()) {
            list.add("static");
        } //end if

        if (m_method.isFinal()) {
            list.add("final");
        }

        if (m_method.isAbstract()) {
            list.add("abstract");
        } //end if

        if (m_method.isNative()) {
            list.add("native");
        } //end if

        if (m_method.isSynchronized()) {
            list.add("synchronized");
        } //end if

        if (m_method.isStrictfp()) {
            list.add("strictfp");
        } //end if

        return list;
    }

    public String getType() throws DbException {
        DbOOAdt type = m_method.getReturnType();
        String typename = (type == null) ? "void" : type.getName();
        return typename;
    }

    public String getName() throws DbException {
        return m_method.getName();
    }

    public List<String> getParameters() throws DbException {
        List<String> list = new ArrayList<String>();
        DbRelationN relN = m_method.getComponents();
        DbEnumeration enu = relN.elements(DbJVParameter.metaClass);
        while (enu.hasMoreElements()) {
            DbJVParameter param = (DbJVParameter) enu.nextElement();
            DbOOAdt adt = param.getType();
            String typename = (adt == null) ? "???" : adt.getName();
            String name = param.getName();
            list.add(typename + " " + name);
        }
        enu.close();

        //String parameters = buildList("(", list, ", ", ")", "()"); 
        return list;
    }

    public List<String> getExceptions() throws DbException {
        List<String> list = new ArrayList<String>();
        DbRelationN relN = m_method.getJavaExceptions();
        DbEnumeration enu = relN.elements(DbJVClass.metaClass);
        while (enu.hasMoreElements()) {
            DbJVClass excep = (DbJVClass) enu.nextElement();
            list.add(excep.getName());
        }
        enu.close();

        //String exceptions = buildList(" throws ", list, ", ", "", ""); 
        return list;
    }

    public String getBody() throws DbException {
        List<String> list = new ArrayList<String>();
        String body = m_method.getBody();
        if (body != null) {
            list.add(body);
        }

        String text = buildList("", list, "", "\n", ";");
        return text;
    }

} //end ProjectWrapper
