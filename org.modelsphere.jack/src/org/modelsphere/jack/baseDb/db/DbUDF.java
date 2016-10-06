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

package org.modelsphere.jack.baseDb.db;

import java.util.ArrayList;

import org.modelsphere.jack.baseDb.db.srtypes.UDFValueType;
import org.modelsphere.jack.baseDb.international.LocaleMgr;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.util.SrVector;

/**
 * 
 * Implements a User-Defined Field. Users may define their other field to any ModelSphere concept.
 * The type of a UDF must be one of the following:
 * 
 * <li>UDFValueType.STRING <li>UDFValueType.LONG <li>UDFValueType.DOUBLE <li>
 * UDFValueType.TEXT <li>UDFValueType.BOOLEAN <br>
 * <br>
 * The static method <code>DbUDF.getUDF(project, metaClass, udfName)</code> returns the DbUDF object
 * corresponding to the UDF <code>udfName</code> in the class <metaClass>, or null if this UDF is
 * not defined in the project. The two following methods on DbObject, <code>get(dbUDF)</code> and
 * <code>getUDF(udfName)</code>, return the UDF value for this object (null if no value). The method
 * <code>set(dbUDF, value)</code> on DbObject sets the UDF value for this object (value may be
 * null).
 * 
 */
public final class DbUDF extends DbSemanticalObject {

    static final long serialVersionUID = -5270758492654153009L;
    private transient MetaClass udfMetaClass;

    public static final MetaField fMetaClassName = new MetaField(LocaleMgr.db
            .getString("metaClassName"));
    public static final MetaField fValueType = new MetaField(LocaleMgr.db.getString("valueType"));
    public static final MetaRelationN fDisplayZones = new MetaRelationN(LocaleMgr.db
            .getString("displayZones"), 0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbUDF"),
            DbUDF.class, new MetaField[] { fMetaClassName, fValueType, fDisplayZones },
            MetaClass.MATCHABLE | MetaClass.NO_UDF);

    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSemanticalObject.metaClass);

            metaClass.setComponentMetaClasses(new MetaClass[] { DbUDFValue.metaClass });
            fMetaClassName.setJField(DbUDF.class.getDeclaredField("m_metaClassName")); // NOT LOCALIZABLE
            // field name
            fValueType.setJField(DbUDF.class.getDeclaredField("m_valueType")); // NOT
            // LOCALIZABLE
            // field
            // name
            fDisplayZones.setJField(DbUDF.class.getDeclaredField("m_displayZones")); // NOT LOCALIZABLE
            // field name

            fMetaClassName.setVisibleInScreen(false);
            fValueType.setRendererPluginName("UDFValueType"); // NOT LOCALIZABLE
            // renderer name
        } catch (Exception e) {
            throw new RuntimeException("Meta init"); // NOT LOCALIZABLE
            // RuntimeException
        }
    }

    String m_metaClassName;
    UDFValueType m_valueType;
    DbRelationN m_displayZones;

    /*
     * Returns the DbUDF <udfName> for metaClass <metaClass>; returns null if it does not exist.
     */
    public static DbUDF getUDF(DbProject project, MetaClass metaClass, String udfName)
            throws DbException {
        DbUDF udfFound = null;
        DbEnumeration dbEnum = project.getComponents().elements(DbUDF.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbUDF udf = (DbUDF) dbEnum.nextElement();
            if (metaClass == udf.getUDFMetaClass() && udfName.equals(udf.getName())) {
                udfFound = udf;
                break;
            }
        }
        dbEnum.close();
        return udfFound;
    }

    /*
     * Returns all DbUDF for metaClass <metaClass>;
     */
    public static DbUDF[] getUDF(DbProject project, MetaClass metaClass) throws DbException {
        SrVector tempUdfs = new SrVector();
        DbEnumeration dbEnum = project.getComponents().elements(DbUDF.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbUDF udf = (DbUDF) dbEnum.nextElement();
            if (metaClass == udf.getUDFMetaClass())
                tempUdfs.add(udf);
        }
        dbEnum.close();
        DbUDF[] udfs = new DbUDF[tempUdfs.size()];
        for (int i = 0; i < udfs.length; i++) {
            udfs[i] = (DbUDF) tempUdfs.get(i);
        }
        return udfs;
    }

    public DbUDF() {
    }

    public DbUDF(DbProject project, MetaClass metaClass) throws DbException {
        super(project);
        udfMetaClass = metaClass;
        basicSet(fMetaClassName, metaClass.getJClass().getName());
        setName(LocaleMgr.db.getString("myProperty"));
        setValueType(UDFValueType.getInstance(UDFValueType.STRING));
    }

    public final boolean isHugeRelN(MetaRelationN metaRelN) {
        if (metaRelN == DbObject.fComponents)
            return true;
        return super.isHugeRelN(metaRelN);
    }

    public final String getSemanticalName(int form) throws DbException {
        String name = getName();
        if (form == LONG_FORM)
            name = getUDFMetaClass().getGUIName() + "." + name;
        return name;
    }

    public final MetaClass getUDFMetaClass() throws DbException {
        if (udfMetaClass == null) {
            udfMetaClass = MetaClass.find(getMetaClassName());
            if (udfMetaClass == null)
                throw new RuntimeException("UDF with unknown metaClass " + getMetaClassName()); // NOT LOCALIZABLE
            // RuntimeException
        }
        return udfMetaClass;
    }

    public final Class getValueClass() throws DbException {
        switch (getValueType().getValue()) {
        case UDFValueType.BOOLEAN:
            return Boolean.class;
        case UDFValueType.LONG:
            return Long.class;
        case UDFValueType.DOUBLE:
            return Double.class;
        case UDFValueType.STRING:
        case UDFValueType.TEXT:
            return String.class;

        default:
            throw new RuntimeException("Missing case in DbUDF.getValueClass"); // NOT
            // LOCALIZABLE
            // RuntimeException
        }
    }

    public final boolean matches(DbObject dbo) throws DbException {
        return (getUDFMetaClass() == ((DbUDF) dbo).getUDFMetaClass()
                && DbObject.valuesAreEqual(getName(), ((DbUDF) dbo).getName()) && DbObject
                .valuesAreEqual(getValueType(), ((DbUDF) dbo).getValueType()));
    }

    public final void setMetaClassName(String value) throws DbException {
        throw new RuntimeException("Cannot change the metaClass of a UDF"); // NOT
        // LOCALIZABLE
        // RuntimeException
    }

    public final void setValueType(UDFValueType value) throws DbException {
        if (basicSet(fValueType, value)) {
            // If UDF value type changes, remove all the UDF values.
            DbEnumeration dbEnum = getComponents().elements(DbUDFValue.metaClass);
            while (dbEnum.hasMoreElements())
                dbEnum.nextElement().remove();
            dbEnum.close();
        }
    }

    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fMetaClassName)
                setMetaClassName((String) value);
            else if (metaField == fValueType)
                setValueType((UDFValueType) value);
            else if (metaField == fDisplayZones)
                ((DbSingleZoneDisplay) value).setUdf(this);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    public final String getMetaClassName() throws DbException {
        return (String) get(fMetaClassName);
    }

    public final UDFValueType getValueType() throws DbException {
        return (UDFValueType) get(fValueType);
    }

    public final DbRelationN getDisplayZones() throws DbException {
        return (DbRelationN) get(fDisplayZones);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

    // UDF's are not inherited by subclasses.
    public final boolean isAUserPropertyOf(DbObject dbo) throws DbException {
        return getUDFMetaClass() == dbo.getMetaClass();
    }

    // This class allows for fast look-up in the UDFs of a project.
    // BEWARE: if you want to add a new UDF in the project, you must do it
    // through the method <add>.
    public static class UDFMap {

        private DbProject project;
        private ArrayList[] udfsByClass;

        public UDFMap(DbProject project) throws DbException {
            this.project = project;
            udfsByClass = new ArrayList[MetaClass.getNbMetaClasses()];
            DbEnumeration dbEnum = project.getComponents().elements(DbUDF.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbUDF udf = (DbUDF) dbEnum.nextElement();
                int index = udf.getUDFMetaClass().getSeqNo();
                if (udfsByClass[index] == null)
                    udfsByClass[index] = new ArrayList();
                udfsByClass[index].add(udf);
            }
            dbEnum.close();
        }

        public final DbUDF find(MetaClass metaClass, String udfName, UDFValueType valueType)
                throws DbException {
            DbUDF udfFound = null;
            ArrayList udfs = udfsByClass[metaClass.getSeqNo()];
            if (udfs != null) {
                for (int i = 0; i < udfs.size(); i++) {
                    DbUDF udf = (DbUDF) udfs.get(i);
                    if (udfName.equals(udf.getName()) && valueType.equals(udf.getValueType())) {
                        udfFound = udf;
                        break;
                    }
                }
            }
            return udfFound;
        }

        public final DbUDF add(MetaClass metaClass, String udfName, UDFValueType valueType)
                throws DbException {
            DbUDF udf = new DbUDF(project, metaClass);
            udf.setName(udfName);
            udf.setValueType(valueType);
            int index = metaClass.getSeqNo();
            if (udfsByClass[index] == null)
                udfsByClass[index] = new ArrayList();
            udfsByClass[index].add(udf);
            return udf;
        }

        public final DbUDF findAdd(MetaClass metaClass, String udfName, UDFValueType valueType)
                throws DbException {
            DbUDF udf = find(metaClass, udfName, valueType);
            if (udf == null)
                udf = add(metaClass, udfName, valueType);
            return udf;
        }
    } // End of class UDFMap

}
