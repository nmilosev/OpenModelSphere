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

import java.io.*;
import java.util.*;
import java.lang.reflect.Constructor;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.debug.Debug;

class DbObjectElement extends XMLElement {

    private DbObject dbo;

    DbObjectElement(DbObject dbo) throws DbException {
        super(dbo == null ? null : TAG_COMPONENT);
        this.dbo = dbo;
        if (dbo != null)
            populate();
    }

    private void populate() throws DbException {
        add(new IdAttribute(dbo)); // add the id

        MetaClass metaclass = dbo.getMetaClass();
        MetaField[] fields = metaclass.getAllMetaFields();
        for (int i = 0; i < fields.length; i++) {
            Object value = dbo.get(fields[i]);
            if (value == null)
                continue;

            Class type = fields[i].getJField().getType();

            if (fields[i] == DbObject.fComposite)
                continue;

            if (fields[i] == DbObject.fComponents) {
                DbRelationN components = (DbRelationN) value;
                DbEnumeration dbEnum = components.elements();
                while (dbEnum.hasMoreElements()) {
                    add(new DbObjectElement(dbEnum.nextElement()));
                }
                dbEnum.close();
                continue;
            }

            if (DbRelationN.class.isAssignableFrom(type)) {
                DbRelationN values = (DbRelationN) value;
                DbEnumeration dbEnum = values.elements();
                ArrayList elements = new ArrayList();
                while (dbEnum.hasMoreElements()) {
                    elements.add(dbEnum.nextElement());
                }
                dbEnum.close();
                DbObject[] dbos = new DbObject[elements.size()];
                for (int j = 0; j < dbos.length; j++)
                    dbos[j] = (DbObject) elements.get(j);
                if (dbos.length > 0)
                    add(new AssociationAttribute((MetaRelationship) fields[i], dbos));
                continue;
            }

            if (DbObject.class.isAssignableFrom(type)) {
                add(new AssociationAttribute((MetaRelationship) fields[i], (DbObject) value));
                continue;
            }

            // Process UDF values
            if (dbo instanceof DbUDFValue && fields[i] == DbUDFValue.fValue) {
                Object udfValue = null;
                switch (((DbUDF) dbo.getComposite()).getValueType().getValue()) {
                case UDFValueType.BOOLEAN:
                    udfValue = new SrBoolean((Boolean) value);
                    break;
                case UDFValueType.LONG:
                    udfValue = new SrLong((Long) value);
                    break;
                case UDFValueType.DOUBLE:
                    udfValue = new SrDouble((Double) value);
                    break;
                case UDFValueType.STRING:
                case UDFValueType.TEXT:
                    udfValue = new SrString((String) value);
                    break;
                default:
                    Debug
                            .trace("Missing case in DbObjectElement.populate() for DbUDFValue supported types.");
                }
                add(new SrTypeAttribute(fields[i], (SrType) udfValue));
                continue;
            }

            if (value instanceof String) {
                MetaField mf = fields[i];
                if (displayAsAttribute(mf)) {
                    add(new FieldAttribute(mf.getJField(), (String) value));
                } else {
                    add(new TextElement(mf, (String) value));
                }
                continue;
            }

            if (type.isPrimitive() || !SrType.class.isAssignableFrom(type)) {
                // process primitive
                add(new MetaFieldAttribute(fields[i], value));
                continue;
            }

            if (type.isInstance(value)) {
                add(new SrTypeAttribute(fields[i], (SrType) value));
                continue;
            }

            // process srtypes
            /*
             * Convert an AWT type to a SrType; a SrType must have only one constructor.
             */
            try {
                Constructor[] constructors = type.getConstructors();
                for (int j = 0; j < constructors.length; j++) {
                    Class[] parametersTypes = constructors[j].getParameterTypes();
                    if (parametersTypes.length == 0)
                        continue;
                    if (parametersTypes.length == 1) {
                        value = constructors[j].newInstance(new Object[] { value });
                        break;
                    }
                }
                add(new SrTypeAttribute(fields[i], (SrType) value));
            } catch (Exception e) {
                Debug.trace(e);
            }
            continue;
        }
    } // end populate()

    // Display as attribute by default
    // For field type that they are expected to hold a large value, like m_body,
    // display as TextElement instead
    private boolean displayAsAttribute(MetaField mf) {
        boolean displayAsAttribute = true; // default value, more compact

        String name = mf.getJField().getName();
        if (name.equals("m_body")) { // NOT LOCALIZABLE, attribute name
            displayAsAttribute = false;
        }
        return displayAsAttribute;
    } // end displayAsAttribute

    // OVERRIDES XMLElement
    void writeAttributes(XMLWriter writer) throws DbException, IOException {
        super.writeAttributes(writer);
    } // end writeAttributes()

} // end DbObjectElement

