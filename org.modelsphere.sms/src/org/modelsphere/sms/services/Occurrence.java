/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.services;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Vector;

import org.modelsphere.jack.debug.Debug;

/**
 * 
 * This file defines the abstract class AbstractOccurrence and its two subclasses: Occurrence and
 * MetaOccurrence. Occurrence represents each occurrence in an extract file. MetaOccurrence
 * represents each occurrence in the meta-extract file, which tells how to interprete an extract
 * file and which DB objects have to be created.
 * 
 */
/*
 * ABSTRACT OCCURRENCE
 */
abstract class AbstractOccurrence implements java.io.Serializable {

    /* Field inner class */
    static class Field implements java.io.Serializable {
        private String m_fieldName;
        private String m_value;

        Field(String aFieldName, String aValue) {
            m_fieldName = aFieldName;
            m_value = aValue;
        }
    }

    /* Static members */
    protected static AbstractOccurrence currentOccurrence = null;
    protected static ObjectOutputStream g_OutputStream = null;

    /* Object members */
    protected String m_name;
    // protected String m_typeName;
    protected Vector m_vecOfFields = new Vector();

    /*
     * OPERATIONS
     */
    public void setName(String aName) {
        m_name = aName;
    }

}

/*
 * META-OCCURRENCE
 */
final class MetaOccurrence extends AbstractOccurrence {
    private String m_className;
    private String m_superClassName;
    private String m_creationalClassName;
    private String m_pkName;
    private String m_pkDbName;
    private String m_fkReferencerField;
    private String m_fkReferencedOccurrence;

    static Vector g_vecOfMetaOccurrences = new Vector();

    /* Constructor */
    MetaOccurrence(String occurrenceName, String dbClassName, String dbSuperClassName,
            String dbCreationalClassName) {
        setName(occurrenceName);
        m_className = dbClassName;
        m_superClassName = dbSuperClassName;
        m_creationalClassName = dbCreationalClassName;
    }

    /* Public interface (called during parsing meta-extract file) */
    public static void addOccurrence(String occurrenceName, String dbClassName,
            String dbSuperClassName, String dbCreationalClassName) {
        // add current occurrence to vector
        if (Occurrence.currentOccurrence != null)
            g_vecOfMetaOccurrences.addElement(Occurrence.currentOccurrence);

        // set current occurrence (and lose previous one)
        Occurrence.currentOccurrence = new MetaOccurrence(occurrenceName, dbClassName,
                dbSuperClassName, dbCreationalClassName);

        Debug.trace("Occurrence " + occurrenceName + ": " + dbClassName + " " + dbSuperClassName
                + " " + dbCreationalClassName); // NOT
        // LOCALIZABLE
    }

    public static void setPk(String pkName, String pkDbName) {
        if ((Occurrence.currentOccurrence != null)
                && (Occurrence.currentOccurrence instanceof MetaOccurrence)) {
            MetaOccurrence mo = (MetaOccurrence) Occurrence.currentOccurrence;
            mo.m_pkName = pkName;
            mo.m_pkDbName = pkDbName;
        }
    }

    public static void setFk(String fkReferencerField, String fkReferencedOccurrence) {
        if ((Occurrence.currentOccurrence != null)
                && (Occurrence.currentOccurrence instanceof MetaOccurrence)) {
            MetaOccurrence mo = (MetaOccurrence) Occurrence.currentOccurrence;
            mo.m_fkReferencerField = fkReferencerField;
            mo.m_fkReferencedOccurrence = fkReferencedOccurrence;
        }
    }

    static MetaOccurrence findMetaOccurrence(String type) {
        Enumeration enumeration = g_vecOfMetaOccurrences.elements();
        MetaOccurrence mo = null;
        boolean found = false;

        while (enumeration.hasMoreElements()) {
            mo = (MetaOccurrence) enumeration.nextElement();
            if (mo.m_name.equals(type)) {
                found = true;
                break;
            }
        }

        if (found)
            return mo;
        else
            return null;
    }

    /*
     * OPERATIONS
     */
    protected String getClassName() {
        return m_className;
    }

    protected String getSuperClassName() {
        return m_superClassName;
    }

    protected String getCreationalClassName() {
        return m_creationalClassName;
    }

    protected String getReferencerField() {
        return m_fkReferencerField;
    }

    protected String getReferendedOccurrence() {
        return m_fkReferencedOccurrence;
    }
}

/*
 * OCCURRENCE
 */
public final class Occurrence extends AbstractOccurrence {

    /* Static members */
    // private static Occurrence currentOccurrence = null;
    // private static ObjectOutputStream g_OutputStream = null;
    /* Object members */
    private org.modelsphere.jack.baseDb.db.DbSemanticalObject m_superObj = null;

    private String m_superName = null;
    private boolean m_isLast = false;

    private MetaOccurrence m_metaOcc = null;

    /*
     * CONSTRUCTOR
     */
    Occurrence(MetaOccurrence mo) {
        m_metaOcc = mo;
    }

    /*
     * STATIC FUNCTIONS
     */

    // Example: Method m = getMethod("java.io.PrintWriter",
    // "println", new String[] {"java.lang.String"});
    // m.invoke(System.out, new String[] {"HelloWorld"});
    private static java.lang.reflect.Method getMethod(String className, String methodName,
            String paramNames[]) {
        java.lang.Class cl = null;
        java.lang.reflect.Method m = null;

        try {
            // forName() here is mandatory because className is not known at
            // compile time.
            cl = java.lang.Class.forName(className);
        } catch (ClassNotFoundException e) {
            Debug.trace("A)" + e.getMessage());
        }

        try {
            int size = paramNames.length;
            java.lang.Class param[] = new java.lang.Class[size];
            for (int i = 0; i < size; i++) {
                // forName() here is mandatory because className is not known at
                // compiled time.
                param[i] = java.lang.Class.forName(paramNames[i]);
            }
            m = cl.getMethod(methodName, param);
        } catch (ClassNotFoundException e) {
            Debug.trace("B)" + e.getMessage());
        } catch (NoSuchMethodException e) {
            Debug.trace("C)" + e.getMessage());
        }

        return m;
    }

    private static org.modelsphere.jack.baseDb.db.DbSemanticalObject findSemanticalObject(
            String aSuperTypeName, String aSuperName) {
        // TODO:MetaClass mc;
        org.modelsphere.jack.baseDb.db.DbSemanticalObject so = null;
        boolean isFound = false;

        if (aSuperTypeName.equals("null")) { // NOT LOCALIZABLE
            return null;
        }

        org.modelsphere.jack.baseDb.db.Db currentDb = null; // TODO:Db.getSingleton();
        java.lang.reflect.Method m = getMethod("db.Db", "getMetaClass" + aSuperTypeName,
                new String[] {}); // NOT LOCALIZABLE
        // try {
        // TODO: mc = (MetaClass)m.invoke(currentDb, new String[] {});
        java.util.Enumeration enumeration = null; // TODO:mc.getInstances();

        while (enumeration.hasMoreElements()) {
            so = (org.modelsphere.jack.baseDb.db.DbSemanticalObject) enumeration.nextElement();
            try {
                String name = so.getName();
                if (name.equals(aSuperName)) {
                    isFound = true;
                    break;
                }
            } catch (org.modelsphere.jack.baseDb.db.DbException e) {
            }
        } /* end while */
        // } catch (java.lang.reflect.InvocationTargetException e)
        // {Debug.trace("D)" + e.getMessage());}
        // catch (IllegalAccessException e)
        // {Debug.trace("E)" + e.getMessage());}

        return (isFound ? so : null);
    }

    /* Public interface */
    // Flush current occurrence
    public static void flush() {
        Occurrence occ = null;

        if (Occurrence.currentOccurrence instanceof Occurrence)
            occ = (Occurrence) Occurrence.currentOccurrence;

        try {
            if ((occ != null) && (AbstractOccurrence.g_OutputStream != null))
                AbstractOccurrence.g_OutputStream.writeObject(occ);
        } catch (IOException e) {
            Debug.trace("Cannot write object: " + e.getMessage());
        }
    }

    public static void addOccurrence(String type) {
        Occurrence occ = null;

        if (Occurrence.currentOccurrence instanceof MetaOccurrence) {
            // add the last MetaOccurrence to the vector
            MetaOccurrence.g_vecOfMetaOccurrences
                    .addElement((MetaOccurrence) Occurrence.currentOccurrence);
        } else
            occ = (Occurrence) Occurrence.currentOccurrence;

        // flush current occurrence
        flush();

        // get model structure
        MetaOccurrence mo = MetaOccurrence.findMetaOccurrence(type);

        // set current occurrence (and lose previous one)
        if (mo != null)
            occ = new Occurrence(mo);
        else
            occ = null;

        Occurrence.currentOccurrence = occ;

        if (occ != null) {
            Debug.trace("Occurrence " + occ.getClassName() + " " + occ.getSuperClassName() + " "
                    + occ.getCreationalClassName()); // NOT LOCALIZABLE
        }
    }

    public static void addField(String field, String value) {
        Occurrence occ = null;

        if (Occurrence.currentOccurrence instanceof Occurrence)
            occ = (Occurrence) Occurrence.currentOccurrence;

        if (occ != null) {
            if (field.equals("name")) { // NOT LOCALIZABLE
                occ.setName(value);
            }

            /* If the field is a fk */
            String rf = occ.m_metaOcc.getReferencerField();
            if (field.equals(rf)) {
                occ.m_superName = value;
            }

            Field f = new Field(field, value);
            occ.m_vecOfFields.addElement(f);
            Debug.trace("  " + field + " " + value);
        }
    }

    public static void setOutputStream(ObjectOutputStream to_server) {
        g_OutputStream = to_server;
    }

    public static void startTransaction(String transactionName) {
        // TODO:Db.getSingleton().startTransaction(transactionName);
    }

    public static void commitTransaction() {
        // TODO:Db.getSingleton().commit();
    }

    /*
     * OPERATIONS
     */
    // public void setName(String aName)
    // {
    // m_name = aName;
    // }
    String getClassName() {
        return m_metaOcc.getClassName();
    }

    String getSuperClassName() {
        return m_metaOcc.getSuperClassName();
    }

    String getCreationalClassName() {
        return m_metaOcc.getCreationalClassName();
    }

    public void setSuperName(String aSuperName) {
        m_superName = aSuperName;
    }

    public void isLast(boolean isLast) {
        m_isLast = isLast;
    }

    public boolean isLast() {
        return m_isLast;
    }

    static boolean isCreated = false;

    private void setFields(org.modelsphere.jack.baseDb.db.DbSemanticalObject newOccurrence) {
        java.lang.reflect.Method m = null;

        // m = getMethod("java.io.PrintStream", "println", new String[]
        // {"java.lang.String"});
        m = Occurrence.getMethod("db.dbo.DataMember", "setStatiC", new String[] {}); // NOT LOCALIZABLE

        /*
         * if (newOccurrence instanceof org.modelsphere.sms.oo.db.DbDataMember) {
         * org.modelsphere.sms.oo.db.DbDataMember field =
         * (org.modelsphere.sms.oo.db.DbDataMember)newOccurrence; //TODO:field.setStatiC(true);
         * 
         * Boolean bool = new Boolean(true); Object args[] = { bool };
         * 
         * //try { // m.invoke(field, args); //} catch (java.lang.reflect.InvocationTargetException
         * e) {} // catch (java.lang.IllegalAccessException e) {} }
         */
    }

    public void create() {
        org.modelsphere.jack.baseDb.db.DbSemanticalObject so = null;

        org.modelsphere.jack.baseDb.db.Db db = null; // Db.getSingleton();

        if (m_superName == null) {
            m_superObj = null; // TODO:db.getCurrentProject();
        } else {
            so = Occurrence.findSemanticalObject(m_metaOcc.getSuperClassName(), m_superName);
            m_superObj = so;
        }

        java.lang.reflect.Method m = null;

        Occurrence.startTransaction("Reverse"); // NOT LOCALIZABLE

        String creationalClassName = m_metaOcc.getCreationalClassName();

        // m = getMethod("java.io.PrintStream", "println", new String[]
        // {"java.lang.String"});
        m = Occurrence.getMethod("db.Db", "create" + m_metaOcc.getClassName(), // NOT
                // LOCALIZABLE
                creationalClassName.equals("null") ? // NOT LOCALIZABLE
                new String[] {}
                        : new String[] { "db.dbo." + creationalClassName } // NOT
                // LOCALIZABLE
                );

        try {
            Object target = null; // Db.getSingleton();
            org.modelsphere.jack.baseDb.db.DbSemanticalObject newOccurrence;
            if (m_superObj == null) {
                Object args[] = {};
                newOccurrence = (org.modelsphere.jack.baseDb.db.DbSemanticalObject) m.invoke(
                        target, args);
            } else {
                Object args[] = { m_superObj };
                newOccurrence = (org.modelsphere.jack.baseDb.db.DbSemanticalObject) m.invoke(
                        target, args);
            }
            // db.add(newOccurrence);

            try {
                newOccurrence.setName(m_name);
            } catch (org.modelsphere.jack.baseDb.db.DbException e) {
            }

            setFields(newOccurrence);

        } catch (java.lang.reflect.InvocationTargetException e) {
            Debug.trace("3)" + e.getMessage());
        } catch (IllegalAccessException e) {
            Debug.trace("4)" + e.getMessage());
        }

        Occurrence.commitTransaction();
    }
}
