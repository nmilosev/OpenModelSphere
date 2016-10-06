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
import org.modelsphere.jack.baseDb.util.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/or/db/DbORIndex.html">DbORIndex</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public final class DbORIndexKey extends DbSMSIndexKey {

    //Meta

    public static final MetaRelation1 fIndexedElement = new MetaRelation1(LocaleMgr.db
            .getString("indexedElement"), 0);
    public static final MetaField fExpression = new MetaField(LocaleMgr.db.getString("expression"));
    public static final MetaField fSortOption = new MetaField(LocaleMgr.db.getString("sortOption"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbORIndexKey"),
            DbORIndexKey.class, new MetaField[] { fIndexedElement, fExpression, fSortOption },
            MetaClass.NO_UDF | MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSIndexKey.metaClass);
            metaClass.setIcon("dborindexkey.gif");

            fIndexedElement.setJField(DbORIndexKey.class.getDeclaredField("m_indexedElement"));
            fIndexedElement.setRendererPluginName("DbORColumn");
            fExpression.setJField(DbORIndexKey.class.getDeclaredField("m_expression"));
            fSortOption.setJField(DbORIndexKey.class.getDeclaredField("m_sortOption"));

            fIndexedElement.setOppositeRel(DbORColumn.fIndexKeys);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbORColumn m_indexedElement;
    String m_expression;
    ORIndexKeySort m_sortOption;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORIndexKey() {
    }

    /**
     * Creates an instance of DbORIndexKey.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORIndexKey(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        DbORDataModel dataModel = (DbORDataModel) getCompositeOfType(DbORDataModel.metaClass);
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology term = terminologyUtil.findModelTerminology(dataModel);
        setName(term.getTerm(metaClass));
    }

    /**
     * @param form
     *            int
     * @return string
     **/
    public final String getSemanticalName(int form) throws DbException {
        DbORColumn col = getIndexedElement();
        return (col != null ? col.getSemanticalName(form) : getExpression());
    }

    /**
     * @return string
     **/
    public final String getName() throws DbException {
        DbORColumn col = getIndexedElement();
        return (col != null ? col.getName() : getExpression());
    }

    //Setters

    /**
     * Sets the column object associated to a DbORIndexKey's instance.
     * 
     * @param value
     *            the column object to be associated
     **/
    public final void setIndexedElement(DbORColumn value) throws DbException {
        basicSet(fIndexedElement, value);
    }

    /**
     * Sets the "expression" property of a DbORIndexKey's instance.
     * 
     * @param value
     *            the "expression" property
     **/
    public final void setExpression(String value) throws DbException {
        basicSet(fExpression, value);
    }

    /**
     * Sets the "sort" property of a DbORIndexKey's instance.
     * 
     * @param value
     *            the "sort" property
     **/
    public final void setSortOption(ORIndexKeySort value) throws DbException {
        basicSet(fSortOption, value);
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
     * Gets the column object associated to a DbORIndexKey's instance.
     * 
     * @return the column object
     **/
    public final DbORColumn getIndexedElement() throws DbException {
        return (DbORColumn) get(fIndexedElement);
    }

    /**
     * Gets the "expression" property of a DbORIndexKey's instance.
     * 
     * @return the "expression" property
     **/
    public final String getExpression() throws DbException {
        return (String) get(fExpression);
    }

    /**
     * Gets the "sort" of a DbORIndexKey's instance.
     * 
     * @return the "sort"
     **/
    public final ORIndexKeySort getSortOption() throws DbException {
        return (ORIndexKeySort) get(fSortOption);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
