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

import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.jack.baseDb.international.LocaleMgr;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelation1;

/**
 * 
 * Stores the value of a UDF.
 * 
 * @see org.modelsphere.jack.baseDb.db.UDF
 * 
 */
public final class DbUDFValue extends DbObject {

    static final long serialVersionUID = -6171895147984728557L;

    public static final MetaRelation1 fDbObject = new MetaRelation1(LocaleMgr.db
            .getString("dbObject"), 1);
    public static final MetaField fValue = new MetaField(LocaleMgr.db.getString("value"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbUDFValue"),
            DbUDFValue.class, new MetaField[] { fDbObject, fValue }, MetaClass.MATCHABLE
                    | MetaClass.NO_UDF);

    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbObject.metaClass);

            fDbObject.setJField(DbUDFValue.class.getDeclaredField("m_dbObject")); // NOT LOCALIZABLE field name
            fDbObject.setRendererPluginName("DbSemObjFullName"); // NOT LOCALIZABLE renderer name
            fDbObject.setEditable(false);
            fValue.setJField(DbUDFValue.class.getDeclaredField("m_value")); // NOT LOCALIZABLE field name
            fDbObject.setOppositeRel(DbObject.fUdfValues);
        } catch (Exception e) {
            throw new RuntimeException("Meta init"); // NOT LOCALIZABLE RuntimeException
        }
    }

    DbObject m_dbObject;
    SrType m_value;

    public DbUDFValue() {
    }

    public DbUDFValue(DbUDF udf, DbObject dbo, Object value) throws DbException {
        super(udf);
        setDbObject(dbo);
        setValue(value);
    }

    public final boolean hasWriteAccess() throws DbException {
        if (writeAccess == 0) {
            writeAccess = (getDbObject().hasWriteAccess() ? ACCESS_GRANTED : ACCESS_NOT_GRANTED);
        }
        return (writeAccess == ACCESS_GRANTED);
    }

    private static MetaRelation1[] dependencyRelations = new MetaRelation1[] { fDbObject,
            DbObject.fComposite };

    public final MetaRelation1[] getDependencyRelations() {
        return dependencyRelations;
    }

    public final boolean matches(DbObject dbo) throws DbException {
        return true;
    }

    public final void setDbObject(DbObject dbo) throws DbException {
        if (!((DbUDF) getComposite()).isAUserPropertyOf(dbo))
            throw new RuntimeException("Invalid object class for this UDF value"); // NOT LOCALIZABLE RuntimeException
        basicSet(fDbObject, dbo);
    }

    // Class cast exception if <value> is not of the appropriate type for this
    // UDF
    public final void setValue(Object value) throws DbException {
        SrType srValue = null;
        if (value == null)
            throw new RuntimeException("Null value in DbUDFValue.setValue"); // NOT LOCALIZABLE RuntimeException
        switch (((DbUDF) getComposite()).getValueType().getValue()) {
        case UDFValueType.BOOLEAN:
            srValue = new SrBoolean((Boolean) value);
            break;
        case UDFValueType.LONG:
            srValue = new SrLong((Long) value);
            break;
        case UDFValueType.DOUBLE:
            srValue = new SrDouble((Double) value);
            break;
        case UDFValueType.STRING:
        case UDFValueType.TEXT:
            srValue = new SrString((String) value);
            break;

        default:
            throw new RuntimeException("Missing case in DbUDFValue.setValue"); // NOT LOCALIZABLE RuntimeException
        }
        basicSet(fValue, srValue);
    }

    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fDbObject)
                setDbObject((DbObject) value);
            else if (metaField == fValue)
                setValue(value);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    public final DbObject getDbObject() throws DbException {
        return (DbObject) get(fDbObject);
    }

    public final Object getValue() throws DbException {
        return get(fValue);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }
}
