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
package org.modelsphere.sms.or.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import javax.swing.Icon;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A HREF="../../../../../org/modelsphere/sms/db/DbSMSBuiltInTypePackage.html">
 * DbSMSBuiltInTypePackage</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbORBuiltInType extends DbORTypeClassifier {

    //Meta

    public static final MetaField fBuiltIn = new MetaField(LocaleMgr.db.getString("builtIn"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbORBuiltInType"), DbORBuiltInType.class, new MetaField[] { fBuiltIn },
            MetaClass.MATCHABLE | MetaClass.NO_UDF | MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORTypeClassifier.metaClass);
            metaClass.setIcon("dborbuiltintype.gif");

            fBuiltIn.setJField(DbORBuiltInType.class.getDeclaredField("m_builtIn"));
            fBuiltIn.setEditable(false);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;
    private static Icon builtInKeyIcon = org.modelsphere.jack.graphic.GraphicUtil.loadIcon(
            DbORBuiltInType.class, "resources/dborbuiltintype.gif");
    private static Icon userKeyIcon = org.modelsphere.jack.graphic.GraphicUtil.loadIcon(
            DbORBuiltInType.class, "resources/dborusertype.gif");

    //Instance variables
    boolean m_builtIn;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORBuiltInType() {
    }

    /**
     * Creates an instance of DbORBuiltInType.
     * 
     * @param builtintypepackage
     *            org.modelsphere.sms.db.DbSMSBuiltInTypePackage
     * @param name
     *            java.lang.String
     **/
    public DbORBuiltInType(DbSMSBuiltInTypePackage builtInTypePackage, String name)
            throws DbException {
        super(builtInTypePackage);
        setDefaultInitialValues();
        setName(name);
        setPhysicalName(name);
    }

    /**
     * Creates an instance of DbORBuiltInType.
     * 
     * @param composite
     *            org.modelsphere.jack.baseDb.db.DbObject
     **/
    public DbORBuiltInType(DbObject composite) throws DbException {
        this((DbSMSBuiltInTypePackage) composite, LocaleMgr.db.getString("type"));
        setBuiltIn(Boolean.FALSE);

    }

    private void setDefaultInitialValues() throws DbException {
        setBuiltIn(Boolean.TRUE);
    }

    /**
     * @return boolean
     **/
    public final boolean isDeletable() throws DbException {
        return !isBuiltIn();
    }

    /**
     * @param form
     *            int
     * @return icon
     **/
    public final Icon getSemanticalIcon(int form) throws DbException {
        if (isBuiltIn())
            return builtInKeyIcon;
        else
            return userKeyIcon;
    }

    //Setters

    /**
     * Sets the "built in?" property of a DbORBuiltInType's instance.
     * 
     * @param value
     *            the "built in?" property
     **/
    public final void setBuiltIn(Boolean value) throws DbException {
        basicSet(fBuiltIn, value);
    }

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

    /**
     * Gets the "built in?" property's Boolean value of a DbORBuiltInType's instance.
     * 
     * @return the "built in?" property's Boolean value
     * @deprecated use isBuiltIn() method instead
     **/
    public final Boolean getBuiltIn() throws DbException {
        return (Boolean) get(fBuiltIn);
    }

    /**
     * Tells whether a DbORBuiltInType's instance is builtIn or not.
     * 
     * @return boolean
     **/
    public final boolean isBuiltIn() throws DbException {
        return getBuiltIn().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
