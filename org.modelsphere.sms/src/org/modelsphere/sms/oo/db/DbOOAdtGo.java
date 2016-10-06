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
package org.modelsphere.sms.oo.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.oo.db.srtypes.*;
import org.modelsphere.sms.oo.international.LocaleMgr;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.srtypes.JVClassCategory;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/oo/db/DbOODiagram.html">DbOODiagram</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public final class DbOOAdtGo extends DbSMSClassifierGo {

    //Meta

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbOOAdtGo"),
            DbOOAdtGo.class, new MetaField[] {}, MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSClassifierGo.metaClass);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbOOAdtGo() {
    }

    /**
     * Creates an instance of DbOOAdtGo.
     * 
     * @param composite
     *            org.modelsphere.sms.db.DbSMSDiagram
     * @param adt
     *            org.modelsphere.sms.oo.db.DbOOAdt
     **/
    public DbOOAdtGo(DbSMSDiagram composite, DbOOAdt adt) throws DbException {
        super(composite, adt);
        basicSet(fClassifier, adt);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    /**
     * @param metafield
     *            org.modelsphere.jack.baseDb.meta.MetaField
     * @return object
     **/
    public final Object find(MetaField metaField) throws DbException {

        if (this.hasField(metaField) && get(metaField) != null)
            return get(metaField);

        DbSMSStyle style = this.findStyle();
        MetaField styleMF = null;
        String nameMetaField = null;
        String nameGoField = metaField.getJName();

        styleMF = style.getMetaField(nameGoField);
        if (styleMF != null)
            return style.find(styleMF);

        DbObject so = this.getSO();
        if (so != null) {
            if (so instanceof DbJVClass) {
                String field = null;
                switch (((DbJVClass) so).getStereotype().getValue()) {
                case JVClassCategory.CLASS:
                    field = nameGoField.concat("Class");
                    break;
                case JVClassCategory.EXCEPTION:
                    field = nameGoField.concat("Exception");
                    break;
                case JVClassCategory.INTERFACE:
                    field = nameGoField.concat("Interface");
                    break;
                }
                styleMF = style.getMetaField(field);
                if (styleMF != null)
                    return style.find(styleMF);
            } else {
                MetaClass soMetaClass = so.getMetaClass();
                while (soMetaClass != null) {
                    nameMetaField = soMetaClass.getJClass().getName();
                    nameMetaField = nameMetaField.substring(nameMetaField.lastIndexOf('.') + 1);
                    styleMF = style.getMetaField(nameGoField.concat(nameMetaField));
                    if (styleMF != null)
                        return style.find(styleMF);
                    soMetaClass = soMetaClass.getSuperMetaClass();
                }
            }
        }

        return null;
    }

    //Setters

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        super.set(relation, neighbor, op);
    }

    //Getters

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
