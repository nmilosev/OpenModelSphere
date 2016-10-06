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
import org.modelsphere.jack.baseDb.util.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORAAbsTable.html">DbORAAbsTable</A>,
 * <A HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORAView.html">DbORAView</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/db/DbORFKeyColumn.html">DbORFKeyColumn</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbORAForeign extends DbORForeign {

    //Meta

    public static final MetaField fChecking = new MetaField(LocaleMgr.db.getString("checking"));
    public static final MetaField fDeferrable = new MetaField(LocaleMgr.db.getString("deferrable"));
    public static final MetaField fEnabled = new MetaField(LocaleMgr.db.getString("enabled"));
    public static final MetaField fValidated = new MetaField(LocaleMgr.db.getString("validated"));
    public static final MetaRelation1 fExceptionTable = new MetaRelation1(LocaleMgr.db
            .getString("exceptionTable"), 0);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbORAForeign"),
            DbORAForeign.class, new MetaField[] { fChecking, fDeferrable, fEnabled, fValidated,
                    fExceptionTable }, MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORForeign.metaClass);

            fChecking.setJField(DbORAForeign.class.getDeclaredField("m_checking"));
            fDeferrable.setJField(DbORAForeign.class.getDeclaredField("m_deferrable"));
            fEnabled.setJField(DbORAForeign.class.getDeclaredField("m_enabled"));
            fValidated.setJField(DbORAForeign.class.getDeclaredField("m_validated"));
            fExceptionTable.setJField(DbORAForeign.class.getDeclaredField("m_exceptionTable"));
            fExceptionTable.setRendererPluginName("DbORTable");

            fExceptionTable.setOppositeRel(DbORATable.fForeignConstraints);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    ORAConstraintChecking m_checking;
    boolean m_deferrable;
    SrBoolean m_enabled;
    SrBoolean m_validated;
    DbORATable m_exceptionTable;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORAForeign() {
    }

    /**
     * Creates an instance of DbORAForeign.
     * 
     * @param assocend
     *            org.modelsphere.sms.or.db.DbORAssociationEnd
     **/
    public DbORAForeign(DbORAssociationEnd assocEnd) throws DbException {
        super(assocEnd);
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
     * Sets the "checking" property of a DbORAForeign's instance.
     * 
     * @param value
     *            the "checking" property
     **/
    public final void setChecking(ORAConstraintChecking value) throws DbException {
        basicSet(fChecking, value);
    }

    /**
     * Sets the "deferrable" property of a DbORAForeign's instance.
     * 
     * @param value
     *            the "deferrable" property
     **/
    public final void setDeferrable(Boolean value) throws DbException {
        basicSet(fDeferrable, value);
    }

    /**
     * Sets the "enabled" property of a DbORAForeign's instance.
     * 
     * @param value
     *            the "enabled" property
     **/
    public final void setEnabled(Boolean value) throws DbException {
        basicSet(fEnabled, value);
    }

    /**
     * Sets the "validated" property of a DbORAForeign's instance.
     * 
     * @param value
     *            the "validated" property
     **/
    public final void setValidated(Boolean value) throws DbException {
        basicSet(fValidated, value);
    }

    /**
     * Sets the exception table object associated to a DbORAForeign's instance.
     * 
     * @param value
     *            the exception table object to be associated
     **/
    public final void setExceptionTable(DbORATable value) throws DbException {
        basicSet(fExceptionTable, value);
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
     * Gets the "checking" of a DbORAForeign's instance.
     * 
     * @return the "checking"
     **/
    public final ORAConstraintChecking getChecking() throws DbException {
        return (ORAConstraintChecking) get(fChecking);
    }

    /**
     * Gets the "deferrable" property's Boolean value of a DbORAForeign's instance.
     * 
     * @return the "deferrable" property's Boolean value
     * @deprecated use isDeferrable() method instead
     **/
    public final Boolean getDeferrable() throws DbException {
        return (Boolean) get(fDeferrable);
    }

    /**
     * Tells whether a DbORAForeign's instance is deferrable or not.
     * 
     * @return boolean
     **/
    public final boolean isDeferrable() throws DbException {
        return getDeferrable().booleanValue();
    }

    /**
     * Gets the "enabled" of a DbORAForeign's instance.
     * 
     * @return the "enabled"
     **/
    public final Boolean getEnabled() throws DbException {
        return (Boolean) get(fEnabled);
    }

    /**
     * Gets the "validated" of a DbORAForeign's instance.
     * 
     * @return the "validated"
     **/
    public final Boolean getValidated() throws DbException {
        return (Boolean) get(fValidated);
    }

    /**
     * Gets the exception table object associated to a DbORAForeign's instance.
     * 
     * @return the exception table object
     **/
    public final DbORATable getExceptionTable() throws DbException {
        return (DbORATable) get(fExceptionTable);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
