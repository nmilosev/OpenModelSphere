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
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../../org/modelsphere/sms/or/oracle/db/DbORACheck.html">DbORACheck</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/ibm/db/DbIBMCheck.html">DbIBMCheck</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/informix/db/DbINFCheck.html">DbINFCheck</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/generic/db/DbGECheck.html">DbGECheck</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbORCheck extends DbORConstraint {

    //Meta

    public static final MetaField fCondition = new MetaField(LocaleMgr.db.getString("condition"));
    public static final MetaRelation1 fColumn = new MetaRelation1(LocaleMgr.db.getString("column"),
            0);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbORCheck"),
            DbORCheck.class, new MetaField[] { fCondition, fColumn },
            MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORConstraint.metaClass);
            metaClass.setIcon("dborcheck.gif");

            fCondition.setJField(DbORCheck.class.getDeclaredField("m_condition"));
            fCondition.setRendererPluginName("LookupDescription");
            fColumn.setJField(DbORCheck.class.getDeclaredField("m_column"));

            fColumn.setOppositeRel(DbORColumn.fChecks);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    String m_condition;
    DbORColumn m_column;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORCheck() {
    }

    /**
     * Creates an instance of DbORCheck.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORCheck(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        DbORDataModel dataModel = (DbORDataModel) getCompositeOfType(DbORDataModel.metaClass);
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology term = terminologyUtil.findModelTerminology(dataModel);
        setName(term.getTerm(metaClass));
    }

    //Setters

    /**
     * Sets the "condition" property of a DbORCheck's instance.
     * 
     * @param value
     *            the "condition" property
     **/
    public final void setCondition(String value) throws DbException {
        basicSet(fCondition, value);
    }

    /**
     * Sets the column object associated to a DbORCheck's instance.
     * 
     * @param value
     *            the column object to be associated
     **/
    public final void setColumn(DbORColumn value) throws DbException {
        basicSet(fColumn, value);
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
     * Gets the "condition" property of a DbORCheck's instance.
     * 
     * @return the "condition" property
     **/
    public final String getCondition() throws DbException {
        return (String) get(fCondition);
    }

    /**
     * Gets the column object associated to a DbORCheck's instance.
     * 
     * @return the column object
     **/
    public final DbORColumn getColumn() throws DbException {
        return (DbORColumn) get(fColumn);
    }

}
