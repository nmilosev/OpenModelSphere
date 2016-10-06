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

package org.modelsphere.jack.baseDb.db.xml;

import java.lang.reflect.Field;

import org.modelsphere.jack.baseDb.db.srtypes.SrType;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.util.DataConversion;

class SrTypeAttribute extends XMLAttribute {
    private SrType m_srTypeValue;
    private MetaField m_metaField = null;
    private Field m_field = null;
    private static final String ARRAY_ELEMENT_SEPARATOR = ";";

    SrTypeAttribute(MetaField field, SrType value) {
        super(XMLUtilities.createAttributeName(field));
        m_srTypeValue = value;
        m_metaField = field;
    }

    SrTypeAttribute(Field field, SrType value) {
        super(XMLUtilities.createAttributeName(field));
        m_srTypeValue = value;
        m_field = field;
    }

    // returns null if actual type is the same as m_srTypeValue's class
    String getActualType() {
        String specialType = null;

        if (m_metaField != null) {
            Field f = m_metaField.getJField();
            Class type = f.getType();
            if (type != m_srTypeValue.getClass()) {
                type = m_srTypeValue.getClass();
                specialType = type.getName();
            }
        } // end if

        return specialType;
    }

    String getValue() {
    	boolean value2;
    	if(this.name.equals("polyline"))
         	value2 = true;
        DataConversion conversion = DataConversion.getSingleton();
        String value = conversion.fromSrTypeToString(m_srTypeValue);
       
        return value;
    } // end getValue()

    private String toXML(boolean[] array) {
        DataConversion conversion = DataConversion.getSingleton();
        return conversion.fromBooleansToString(array);
    }

    private String toXML(short[] array) {
        DataConversion conversion = DataConversion.getSingleton();
        return conversion.fromShortsToString(array);
    }

    private String toXML(int[] array) {
        DataConversion conversion = DataConversion.getSingleton();
        return conversion.fromIntsToString(array);
    }

    private String toXML(long[] array) {
        DataConversion conversion = DataConversion.getSingleton();
        return conversion.fromLongsToString(array);
    }

    private String toXML(float[] array) {
        DataConversion conversion = DataConversion.getSingleton();
        return conversion.fromFloatsToString(array);
    }

    private String toXML(double[] array) {
        DataConversion conversion = DataConversion.getSingleton();
        return conversion.fromDoublesToString(array);
    }
}
