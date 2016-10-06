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

import java.text.MessageFormat;
import java.awt.Polygon;
import java.lang.reflect.*;
import java.util.*;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.util.DataConversion;

public final class XMLUtilities implements XMLConstants {
    private static HashMap idPool = null;

    static String getIndent(int indent) {
        if (indent <= 0)
            return "";

        indent *= 4;
        int nbTabs = indent / 8;
        int nbSpc = indent % 8;
        String sIndent = "";
        for (int i = 0; i < nbTabs; i++) {
            sIndent = sIndent + "\t"; // NOT LOCALIZABLE, escape code
        }
        for (int i = 0; i < nbSpc; i++) {
            sIndent = sIndent + " ";
        }
        return sIndent;
    }

    static String createIDREF(DbObject dbo) throws DbException {
        if (dbo == null)
            return "";
        return MessageFormat.format(IDREF_PATTERN, new Object[] {
                getShortFormName(dbo.getMetaClass()), dbo.getId() });
    }

    static String createIDREFS(DbObject dbo) throws DbException {
        if (dbo == null)
            return "";
        return MessageFormat.format(IDREFS_PATTERN, new Object[] {
                getShortFormName(dbo.getMetaClass()), dbo.getId() });
    }

    static String createIDREFS(DbObject[] dbos) throws DbException {
        if (dbos == null || dbos.length == 0)
            return "";
        String ref = "";
        ref += MessageFormat.format(IDREFS_PATTERN, new Object[] {
                getShortFormName(dbos[0].getMetaClass()), dbos[0].getId() });
        for (int i = 1; i < dbos.length; i++) {
            ref += " "
                    + MessageFormat.format(IDREFS_PATTERN, new Object[] {
                            getShortFormName(dbos[i].getMetaClass()), dbos[i].getId() });
        }
        return ref;
    }

    static String createAttributeName(MetaField metafield) {
        if (metafield == null)
            return null;
        String jName = metafield.getJField().getName();
        String attrName = jName;
        if (attrName.indexOf("m_") == 0) {
            attrName = attrName.substring(2, attrName.length());
        }
        return attrName;
    }

    static String createAttributeName(Field field) {
        if (field == null)
            return "";
        String jName = field.getName();
        return jName;
    }

    private static String getShortFormName(MetaClass metaclass) {
        if (idPool == null) {
            initIDPool();
        }
        return (String) idPool.get(metaclass);
    }

    private static void initIDPool() {
        idPool = new HashMap();
        ArrayList shortNameList = new ArrayList(); // validation only
        Enumeration allmetaclasses = MetaClass.enumMetaClasses();
        while (allmetaclasses.hasMoreElements()) {
            MetaClass mc = (MetaClass) allmetaclasses.nextElement();
            String name = mc.getJClass().getName();
            int index = name.lastIndexOf(".Db"); // NOT LOCALIZABLE
            if (index > -1) {
                name = name.substring(index + ".Db".length(), name.length()); // NOT
                // LOCALIZABLE
            }
            // validate for duplicate
            Debug.assert2(shortNameList.indexOf(name) == -1,
                    "XMLUtilities:  Duplicated name for metaclass.");
            shortNameList.add(name);
            idPool.put(mc, name);
        }
    }

    static XMLElement buildIDPool() {
        if (idPool == null) {
            initIDPool();
        }
        XMLElement idpool = new DefaultElement(TAG_ID_POOL);
        Iterator metaclasses = idPool.keySet().iterator();
        while (metaclasses.hasNext()) {
            MetaClass metaclass = (MetaClass) metaclasses.next();
            XMLElement id = new DefaultElement(TAG_ID);
            id.add(new DefaultAttribute(ID_SHORT, idPool.get(metaclass)));
            id.add(new DefaultAttribute(ID_LONG, metaclass.getJClass().getName()));
            // (TODO : special constructors?)
            idpool.add(id);
        }
        return idpool;
    } // end buildIDPool()

    static XMLElement buildHeader() {
        XMLElement header = new DefaultElement(TAG_HEADER);
        String converterClassname = "org.modelsphere.sms.SMSVersionConverter";
        try {
            Class converterClass = Class.forName(converterClassname);
            Field field = converterClass.getField("VERSION");
            int i = field.getInt(null); // null because is static
            header.add(new DefaultAttribute(XMLConstants.TAG_CONVERTER, converterClassname));
            header.add(new DefaultAttribute(XMLConstants.TAG_VERSION, Integer.toString(i)));
        } catch (Exception ex) {
            // Report exception in XML file
            header.add(new DefaultAttribute(XMLConstants.TAG_CONVERTER, ex.toString()));
            header.add(new DefaultAttribute(XMLConstants.TAG_VERSION, ex.toString()));
        }
        return header;
    } // end buildHeader()

    //
    // getXmlValue()
    //
    public static String getXmlValue(Object value, Field field) {
        String xmlValue;

        if (value instanceof SrType) {
        	if (value instanceof SrPolygon) {
                SrTypeAttribute attr = new SrTypeAttribute(field, (SrType) value);
                xmlValue = attr.getValue();    
            }else if (value instanceof SrImage) {
                SrTypeAttribute attr = new SrTypeAttribute(field, (SrType) value);
                xmlValue = attr.getValue();    
            }else{
            SrTypeAttribute attr = new SrTypeAttribute(field, (SrType) value);
            xmlValue = attr.getValue();
            }
            // if a boolean, write "0" or "1"
        } else if (value instanceof Boolean) {
            Boolean b = (Boolean) value;
            xmlValue = b.booleanValue() ? "1" : "0";
        } else if (value instanceof SrBoolean) {
            SrBoolean b = (SrBoolean) value;
            xmlValue = b.toString().equals("true") ? "1" : "0";
        } else if (value instanceof Integer) {
            Integer iVal = (Integer) value;
            int i = iVal.intValue();
            if (i >= 0) {
                // if an integer, write in hexa (more compact)
                xmlValue = Integer.toHexString(i);
            } else {
                // a negative integer, writeten in hexa
                xmlValue = "-" + Integer.toHexString(Math.abs(i));
            } // end if
        } else if (value instanceof Float) {
            xmlValue = value.toString();
        } else if (value instanceof Double) {
            xmlValue = value.toString();
        } else if (value instanceof Polygon) {
            xmlValue = value.toString();     
        } else { // for all other cases (Float, Double, String, ..)
            xmlValue = value.toString();
        } // end if

        return xmlValue;
    } // end getXmlValue()

    // the getXmlValue()'s reciprocal function
    public static void setXmlValue(String fieldValue, Field field, Object obj)
            throws IllegalAccessException {
        Class type = field.getType();
        DataConversion conversion = DataConversion.getSingleton();

        if (type == boolean.class) {
            boolean b = fieldValue.equals("1") ? true : false;
            field.setBoolean(obj, b);
        } else if (type == byte.class) {
            byte b = Byte.parseByte(fieldValue);
            field.setByte(obj, b);
        } else if (type == short.class) {
            short s = Short.parseShort(fieldValue);
            field.setShort(obj, s);
        } else if (type == int.class) {
            int i = Integer.parseInt(fieldValue, 16); // base 16 for hexadecimal
            field.setInt(obj, i);
        } else if (type == long.class) {
            long l = Long.parseLong(fieldValue);
            field.setLong(obj, l);
        } else if (type == float.class) {
            float f = Float.parseFloat(fieldValue);
            field.setFloat(obj, f);
        } else if (type == double.class) {
            double d = Double.parseDouble(fieldValue);
            field.setDouble(obj, d);
        } else if (type == boolean[].class) {
            boolean[] booleans = conversion.fromStringToBooleans(fieldValue);
            field.set(obj, booleans);
        } else if (type == byte[].class) {
            byte[] bytes = conversion.fromStringToBytes(fieldValue);
            field.set(obj, bytes);
        } else if (type == short[].class) {
            short[] shorts = conversion.fromStringToShorts(fieldValue);
            field.set(obj, shorts);
        } else if (type == int[].class) {
            int[] ints = conversion.fromStringToInts(fieldValue);
            field.set(obj, ints);
        } else if (type == long[].class) {
            long[] longs = conversion.fromStringToLongs(fieldValue);
            field.set(obj, longs);
        } else if (type == float[].class) {
            float[] floats = conversion.fromStringToFloats(fieldValue);
            field.set(obj, floats);
        } else if (type == double[].class) {
            double[] doubles = conversion.fromStringToDoubles(fieldValue);
            field.set(obj, doubles);
        } else if (type == String.class) {
            String s = (String) fieldValue;
            field.set(obj, s);
        } else if (type == SrImage.class) {
            SrImage image = (SrImage) conversion.fromStringToSrType(fieldValue, SrImage.class);
            field.set(obj, image);
        } else if (type == SrPolygon.class) {
           //WTF
        	SrPolygon polygon = (SrPolygon) conversion.fromStringToSrType(fieldValue, SrPolygon.class);
        	field.set(obj, polygon);
        } else {
            if (Debug.isDebug()) {
                System.out.println("Unsupported type in XML import: " + type.toString());
            }
        } // end type
    } // end setFieldValue

} // end XmlUtilities
