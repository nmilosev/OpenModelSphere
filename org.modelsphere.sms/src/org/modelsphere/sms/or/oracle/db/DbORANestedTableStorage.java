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
package org.modelsphere.sms.or.oracle.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.oracle.db.srtypes.*;
import org.modelsphere.sms.or.oracle.international.LocaleMgr;
import org.modelsphere.sms.or.db.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORATable.html">DbORATable</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORAColumn.html">DbORAColumn</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORAForeign.html">DbORAForeign</A>, <A
 * HREF ="../../../../../../org/modelsphere/sms/or/oracle/db/DbORAPrimaryUnique.html">
 * DbORAPrimaryUnique </A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORACheck.html">DbORACheck</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORAIndex.html">DbORAIndex</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORATrigger.html">DbORATrigger</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbORANestedTableStorage extends DbORAAbsTable {

    //Meta

    public static final MetaRelation1 fNestedItem = new MetaRelation1(LocaleMgr.db
            .getString("nestedItem"), 0);
    public static final MetaField fReturnValueType = new MetaField(LocaleMgr.db
            .getString("returnValueType"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbORANestedTableStorage"), DbORANestedTableStorage.class, new MetaField[] {
            fNestedItem, fReturnValueType }, MetaClass.MATCHABLE
            | MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORAAbsTable.metaClass);
            metaClass.setIcon("dboranestedtable.gif");

            fNestedItem.setJField(DbORANestedTableStorage.class.getDeclaredField("m_nestedItem"));
            fNestedItem.setScreenOrder(">alias");
            fNestedItem.setRendererPluginName("DbORANestedItem");
            fReturnValueType.setJField(DbORANestedTableStorage.class
                    .getDeclaredField("m_returnValueType"));
            fReturnValueType.setScreenOrder("<description");

            fNestedItem.setOppositeRel(DbORAColumn.fStorageTable);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbORAColumn m_nestedItem;
    ORAReturnValueType m_returnValueType;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORANestedTableStorage() {
    }

    /**
     * Creates an instance of DbORANestedTableStorage.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORANestedTableStorage(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setName(LocaleMgr.misc.getString("nestedTable"));
    }

    //Setters

    /**
     * Sets the nested item object associated to a DbORANestedTableStorage's instance.
     * 
     * @param value
     *            the nested item object to be associated
     **/
    public final void setNestedItem(DbORAColumn value) throws DbException {
        basicSet(fNestedItem, value);
    }

    /**
     * Sets the "return value type" property of a DbORANestedTableStorage's instance.
     * 
     * @param value
     *            the "return value type" property
     **/
    public final void setReturnValueType(ORAReturnValueType value) throws DbException {
        basicSet(fReturnValueType, value);
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
     * Gets the nested item object associated to a DbORANestedTableStorage's instance.
     * 
     * @return the nested item object
     **/
    public final DbORAColumn getNestedItem() throws DbException {
        return (DbORAColumn) get(fNestedItem);
    }

    /**
     * Gets the "return value type" of a DbORANestedTableStorage's instance.
     * 
     * @return the "return value type"
     **/
    public final ORAReturnValueType getReturnValueType() throws DbException {
        return (ORAReturnValueType) get(fReturnValueType);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
