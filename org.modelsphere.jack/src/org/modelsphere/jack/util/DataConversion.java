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

package org.modelsphere.jack.util;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.StringTokenizer;

import org.modelsphere.jack.baseDb.db.srtypes.SrType;
import org.modelsphere.jack.baseDb.db.xml.XMLConstants;
import org.modelsphere.jack.baseDb.db.xml.XMLUtilities;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.io.UUEncoder;

/**
 * 
 * Provides functions to convert arrays of primitives into strings, and their reciprocal functions.
 * These functions have to be reliable, fast and preferablely should generate compact strings.
 * 
 * Considering: - A conversion function and its reciprocal should be encapsulated in the same class
 * (so changing the algorithm of conversion on a particular type doesn't affect client classes). - A
 * complete unit test for all kinds of conversions must be supplied. - ARRAY_ELEMENT_SEPARATOR is so
 * critical so it's preferable to give to it a private visibility. - This class does not depends of
 * jack.baseDb's stuff.
 * 
 * then all the methods previously dispatched in several jack.baseDb classes have been gathered in
 * this sole class.
 * 
 * TODO : short[] and long[] use the 'C-String' approach, ie use a terminator to end each element. A
 * better way (both more compact and faster) would be to take the 'Pascal-String' approach, ie use a
 * counter as first element (feasability?)
 */

public final class DataConversion {
    private static final String ARRAY_ELEMENT_SEPARATOR = ";"; // Cannot be
    // interpreted
    // as part of
    // float or
    // double
    // variable

    // Implements Singleton Design Pattern
    private static DataConversion g_singleInstance = null;

    private DataConversion() {
    }

    public static DataConversion getSingleton() {
        if (g_singleInstance == null)
            g_singleInstance = new DataConversion();

        return g_singleInstance;
    }

    //
    // BOOLEAN[] CONVERSION
    //
    public String fromBooleansToString(boolean[] array) {
        StringWriter value = new StringWriter();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                value.write(ARRAY_ELEMENT_SEPARATOR);
            }

            String s = (array[i] ? "1" : "0"); // "1" for true, "0" for false
            value.write(s);
        }
        return value.toString();
    } // end fromBooleansToString()

    public boolean[] fromStringToBooleans(String value) {
        int nb = 0;
        StringTokenizer st = new StringTokenizer(value, ARRAY_ELEMENT_SEPARATOR);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            nb++;
        } // end while
        boolean[] booleans = new boolean[nb];
        nb = 0;
        st = new StringTokenizer(value, ARRAY_ELEMENT_SEPARATOR);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            booleans[nb] = token.equals("1"); // true if "1", false if not
            nb++;
        } // end while
        return booleans;
    } // end fromStringToBooleans()

    //
    // BYTE[] CONVERSIOM
    //
    public String fromBytesToString(byte[] array) {
        UUEncoder encoder = UUEncoder.getSingleton();
        String str = encoder.fromBytesToString(array);
        return str;
    }

    public byte[] fromStringToBytes(String value) {
        UUEncoder encoder = UUEncoder.getSingleton();
        byte[] bytes = encoder.fromStringToBytes(value);
        return bytes;
    }

    //
    // CHAR[] CONVERSION
    //
    public String fromCharsToString(char[] array) {
        return String.valueOf(array);
    }

    public char[] fromStringToChars(String value) {
        return value.toCharArray();
    }

    //
    // SHORT[] CONVERSION
    //
    public String fromShortsToString(short[] array) {
        StringWriter value = new StringWriter();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                value.write(ARRAY_ELEMENT_SEPARATOR);
            }
            value.write(Short.toString(array[i]));
        }
        return value.toString();
    }

    public short[] fromStringToShorts(String value) {
        int nb = 0;
        StringTokenizer st = new StringTokenizer(value, ARRAY_ELEMENT_SEPARATOR);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            nb++;
        } // end while
        short[] shorts = new short[nb];
        nb = 0;
        st = new StringTokenizer(value, ARRAY_ELEMENT_SEPARATOR);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            shorts[nb] = Short.parseShort(token);
            nb++;
        } // end while
        return shorts;
    } // end fromStringToShorts()

    //
    // INT[] CONVERSION
    //
    public String fromIntsToString(int[] array) {
        UUEncoder encoder = UUEncoder.getSingleton();
        byte[] bytes = encoder.fromIntsToBytes(array);
        String str = encoder.fromBytesToString(bytes);
        return str;
    }

    public int[] fromStringToInts(String value) {
        UUEncoder encoder = UUEncoder.getSingleton();
        byte[] bytes = encoder.fromStringToBytes(value);
        int[] ints = encoder.fromBytesToInts(bytes);
        return ints;
    }

    //
    // LONG[] CONVERSION
    //
    public String fromLongsToString(long[] array) {
        StringWriter value = new StringWriter();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                value.write(ARRAY_ELEMENT_SEPARATOR);
            }
            value.write(Long.toString(array[i]));
        }
        return value.toString();
    } // end fromLongsToString()

    public long[] fromStringToLongs(String value) {
        int nb = 0;
        StringTokenizer st = new StringTokenizer(value, ARRAY_ELEMENT_SEPARATOR);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            nb++;
        } // end while
        long[] longs = new long[nb];
        nb = 0;
        st = new StringTokenizer(value, ARRAY_ELEMENT_SEPARATOR);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            longs[nb] = Long.parseLong(token);
            nb++;
        } // end while
        return longs;
    } // end fromStringToLongs()

    //
    // FLOAT[] CONVERSION
    //
    public String fromFloatsToString(float[] array) {
        StringWriter value = new StringWriter();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                value.write(ARRAY_ELEMENT_SEPARATOR);
            }
            value.write(Float.toString(array[i]));
        }
        return value.toString();
    } // end fromFloatsToString()

    public float[] fromStringToFloats(String value) {
        int nb = 0;
        StringTokenizer st = new StringTokenizer(value, ARRAY_ELEMENT_SEPARATOR);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            nb++;
        } // end while
        float[] floats = new float[nb];
        nb = 0;
        st = new StringTokenizer(value, ARRAY_ELEMENT_SEPARATOR);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            floats[nb] = Float.parseFloat(token);
            nb++;
        } // end while
        return floats;
    } // end fromStringToFloats()

    //
    // DOUBLE[] CONVERSION
    //
    public String fromDoublesToString(double[] array) {
        StringWriter value = new StringWriter();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                value.write(ARRAY_ELEMENT_SEPARATOR);
            }
            value.write(Double.toString(array[i]));
        }
        return value.toString();
    } // end fromFloatsToString()

    public double[] fromStringToDoubles(String value) {
        int nb = 0;
        StringTokenizer st = new StringTokenizer(value, ARRAY_ELEMENT_SEPARATOR);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            nb++;
        } // end while
        double[] doubles = new double[nb];
        nb = 0;
        st = new StringTokenizer(value, ARRAY_ELEMENT_SEPARATOR);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            doubles[nb] = Double.parseDouble(token);
            nb++;
        } // end while
        return doubles;
    } // end fromStringToFloats()

    //
    // OBJECT CONVERSION
    //
    private String fromObjectsToString(Object[] array) {
        StringWriter value = new StringWriter();
        for (int i = 0; i < array.length; i++) {
            if (i > 0)
                value.write(ARRAY_ELEMENT_SEPARATOR);
            if (array[i] == null)
                continue;
            // TODO SrType may be contained in a SrType ... there should be a
            // better way to handle this
            // We may get double quote problems
            if (array[i] instanceof SrType) {
                SrType srtype = (SrType) array[i];
                String srvalue = fromSrTypeToString(srtype);
                value.write(srvalue);
            } else
                value.write(array[i].toString());
        }
        return value.toString();
    } // end

    //
    // SRTYPE CONVERSION
    //
    public SrType fromStringToSrType(String value, Class srtypeClass) {
        SrType srtype;

        try {
            // create a new instance by reflection
            srtype = (SrType) srtypeClass.newInstance();

            // sett attributes
            StringTokenizer st = new StringTokenizer(value, XMLConstants.ATTR_NAME_VALUE_TERMINATOR);
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                int idx = token.indexOf('=');
                if (idx != -1) {
                    String fieldName = token.substring(0, idx);
                    String fieldStringValue = token.substring(idx + 1);
                    Field field = srtypeClass.getDeclaredField(fieldName);
                    if (field != null) {
                        field.setAccessible(true);
                        XMLUtilities.setXmlValue(fieldStringValue, field, srtype);
                    } // end if
                } // end if
            } // end while

            // getSrActualField(subtoken, srType, actualValue);
        } catch (InstantiationException ex) {
            srtype = null;
        } catch (IllegalAccessException ex) {
            srtype = null;
        } catch (NoSuchFieldException ex) {
            srtype = null;
        } // end try

        return srtype;
    } // end fromStringToSrType()

    public String fromSrTypeToString(SrType srtype) {
        if (srtype == null)
            return null;

        StringWriter xml = new StringWriter();

        Class c = srtype.getClass();
        while (c != null && c != SrType.class) {
            Field[] fields = c.getDeclaredFields();
            for (int i = 0; fields != null && i < fields.length; i++) {
                int modifiers = fields[i].getModifiers();
                if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers))
                    continue;

                Class type = null;
                Object value = null;
                try {
                    type = fields[i].getType();
                    if (!fields[i].isAccessible()) {
                        fields[i].setAccessible(true);
                    }

                    value = fields[i].get(srtype);
                } catch (Exception e1) {
                    Debug.trace(e1 + "\n" + "field=" + fields[i].getName() + ", type=" + type
                            + ", value=" + value); // NOT
                    // LOCALIZABLE
                } // end try

                if (type.isArray() && value != null) {
                    if (value instanceof int[]) {
                        value = fromIntsToString((int[]) value);
                    } else if (value instanceof boolean[]) {
                        value = fromBooleansToString((boolean[]) value);
                    } else if (value instanceof short[]) {
                        value = fromShortsToString((short[]) value);
                    } else if (value instanceof long[]) {
                        value = fromLongsToString((long[]) value);
                    } else if (value instanceof float[]) {
                        value = fromFloatsToString((float[]) value);
                    } else if (value instanceof Object[]) {
                        value = fromObjectsToString((Object[]) value);
                    } else {
                        Debug.trace("Unsupported array type for XML: " + type.getName()); // //NOT LOCALIZABLE
                        continue;
                    } // end if
                } // end if

                if (value == null) {
                    value = ""; // TODO maybe better to skip ?????
                }

                xml.write(fields[i].getName());
                xml.write(XMLConstants.ATTR_NAME_VALUE_SEPARATOR); // write '='
                // TODO SrType may be contained in a SrType ... there should be
                // a better way to handle this
                // We may get double quote problems
                String xmlValue = XMLUtilities.getXmlValue(value, fields[i]);
                xml.write(xmlValue); // xml.write(XMLConstants.QUOTE + xmlValue
                // + XMLConstants.QUOTE);
                xml.write(XMLConstants.ATTR_NAME_VALUE_TERMINATOR); // write ";"
            } // end for
            c = c.getSuperclass();

        } // end while

        String strValue = xml.toString();
        return strValue;
    } // end fromSrTypeToString()

    //
    // UNIT TEST
    //
    public static void main(String[] args) {
        DataConversion conversion = DataConversion.getSingleton();
        int[] ints = new int[] { 1, 2, 3, 5, 8, 13, 21, 50, 75, 130, 200, 250, 400, 600, 800, 1020,
                1300 };
        String s = conversion.fromIntsToString(ints);
        int[] int2 = conversion.fromStringToInts(s);

        System.out.println("Terminated");
    } // end main()

} // end DataConversion
