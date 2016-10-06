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

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.oo.db.DbOOAdt;
import org.modelsphere.sms.oo.db.DbOOAssociationEnd;
import org.modelsphere.sms.oo.db.DbOODataMember;
import org.modelsphere.sms.oo.db.srtypes.OOAggregation;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVPrimitiveType;
import org.modelsphere.sms.oo.java.db.srtypes.JVVisibility;

public class DbFieldWrapper extends DbObjectWrapper {
    private DbJVDataMember m_field;

    DbFieldWrapper(DbJVDataMember field) {
        m_field = field;
    }

    public List<String> getJavaVisibility() throws DbException {
        List<String> list = new ArrayList<String>();
        JVVisibility visib = (JVVisibility) m_field.getVisibility();
        list.add(toString(visib));
        return list;
    }

    public boolean isFinal() throws DbException {
        return m_field.isFinal();
    }

    public boolean isBoolean() throws DbException {
        boolean bool = false;
        DbOOAdt type = m_field.getType();

        if (type instanceof DbJVPrimitiveType) {
            DbJVPrimitiveType builtin = (DbJVPrimitiveType) type;
            String name = builtin.getName();
            bool = "boolean".equals(name);
        }

        return bool;
    }

    public List<String> getJavaModifiers() throws DbException {
        List<String> list = new ArrayList<String>();

        if (m_field.isStatic()) {
            list.add("static");
        }

        if (m_field.isFinal()) {
            list.add("final");
        }

        if (m_field.isTransient()) {
            list.add("transient");
        } //end if

        if (m_field.isVolatile()) {
            list.add("volatile");
        } //end if

        return list;
    }

    public List<String> getCsharpModifiers() throws DbException {
        List<String> list = new ArrayList<String>();
        JVVisibility visib = (JVVisibility) m_field.getVisibility();
        list.add(toString(visib));

        if (m_field.isStatic()) {
            list.add("static");
        }

        if (m_field.isFinal()) {
            list.add("sealed");
        }

        return list;
    }

    public String getType() throws DbException {
        DbOOAdt type = m_field.getType();
        DbOOAdt elementType = m_field.getElementType();
        String dimension = m_field.getTypeUse();
        String typename;

        if (elementType == null) {
            typename = (type == null) ? "???" : type.getName();
        } else {
            typename = (type == null) ? "???" : type.getName() + "<" + elementType.getName() + ">";
        } //end if

        if (dimension != null) {
            typename += dimension;
        }

        return typename;
    }

    public StringWrapper getName() throws DbException {
        StringWrapper s = new StringWrapper(m_field.getName());
        return s;
    }

    public String getInitialValue() throws DbException {
        String value = m_field.getInitialValue();
        return (value == null) ? "" : value;
    }

    //TODO returns [1], [+], etc.
    public String getEmfaticMultiplicity() {
        String mult = "[*]";
        return mult;
    }

    public String getContainment() throws DbException {
        DbOOAssociationEnd end = m_field.getAssociationEnd();
        OOAggregation aggr = (end == null) ? null : end.getAggregation();
        int kind = (aggr == null) ? OOAggregation.NONE : aggr.getValue();
        String value = (kind == OOAggregation.COMPOSITE) ? "true" : "";
        return value;
    }

    public boolean isContainment() throws DbException {
        DbOOAssociationEnd end = m_field.getAssociationEnd();
        OOAggregation aggr = (end == null) ? null : end.getAggregation();
        int kind = (aggr == null) ? OOAggregation.NONE : aggr.getValue();
        boolean containment = (kind == OOAggregation.COMPOSITE) ? true : false;
        return containment;
    }

    public String getLowerBound() throws DbException {
        String lowerBound = "";
        DbOOAssociationEnd end = m_field.getAssociationEnd();
        SMSMultiplicity mult = (end == null) ? null : end.getMultiplicity();
        if (mult != null) {
            int value = mult.getValue();
            if ((value == SMSMultiplicity.EXACTLY_ONE) || (value == SMSMultiplicity.ONE_OR_MORE)) {
                lowerBound = "1";
            }
        } //end if

        return lowerBound;
    }

    public String getUpperBound() throws DbException {
        String upperBound = "";
        DbOOAssociationEnd end = m_field.getAssociationEnd();
        SMSMultiplicity mult = (end == null) ? null : end.getMultiplicity();
        if (mult != null) {
            int value = mult.getValue();
            if ((value == SMSMultiplicity.EXACTLY_ONE) || (value == SMSMultiplicity.OPTIONAL)) {
                upperBound = "1";
            } else if (value == SMSMultiplicity.UNDEFINED) {
            	upperBound = "-2"; //EMF Unspecified
            } else {
            	upperBound = "-1"; //EMF Unbound
            }
        } //end if

        return upperBound;
    }
    
    public String getEmfOpposite() throws DbException {
    	String opposite = "";
    	DbOOAssociationEnd end = m_field.getAssociationEnd();
    	
    	if (end != null) {
    		DbOOAssociationEnd oppositeEnd = end.getOppositeEnd();
    		if (oppositeEnd != null) {
    			DbOODataMember oppMember = oppositeEnd.getAssociationMember();

	    		DbJVClass claz = (DbJVClass)oppMember.getCompositeOfType(DbJVClass.metaClass);
	    		String classname = claz.getName();
	    		
	    		String endName = oppMember.getName();
	    		opposite = "#//" + classname + "/" + endName;
    		}
    	}
    	
    	return opposite;
    }

    public String getEmfType() throws DbException {
        DbOOAdt type = m_field.getType();
        String typename;

        //if built-in type
        if (type instanceof DbJVPrimitiveType) {
            DbJVPrimitiveType primitive = (DbJVPrimitiveType) type;
            typename = getEmfPrimitive(primitive);
        } else {
            typename = (type == null) ? "int" : type.getName();
            if ("String".equals(typename)) { //special case
                typename = "ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//EString";
            } else {
                //if same package
                typename = "#//" + typename;
            }
        } //end if 

        return typename;
    }

    private String getEmfPrimitive(DbJVPrimitiveType primitive) throws DbException {
        String typename = primitive.getName();
        String emfPrimitive;

        if ("boolean".equals(typename)) {
            emfPrimitive = "ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//EBoolean";
        } else if ("byte".equals(typename)) {
            emfPrimitive = "ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//EByte";
        } else if ("double".equals(typename)) {
            emfPrimitive = "ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//EDouble";
        } else if ("float".equals(typename)) {
            emfPrimitive = "ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//EFloat";
        } else if ("int".equals(typename)) {
            emfPrimitive = "ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//EInt";
        } else if ("short".equals(typename)) {
            emfPrimitive = "ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//EShort";
        } else if ("long".equals(typename)) {
            emfPrimitive = "ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//ELong";
        } else {
            emfPrimitive = typename;
        } //end if

        return emfPrimitive;
    }

} //end ProjectWrapper
