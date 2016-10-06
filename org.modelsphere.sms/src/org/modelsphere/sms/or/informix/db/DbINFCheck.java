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
package org.modelsphere.sms.or.informix.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.informix.db.srtypes.*;
import org.modelsphere.sms.or.informix.international.LocaleMgr;
import org.modelsphere.sms.or.db.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.jack.baseDb.util.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFTable.html">DbINFTable</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFView.html">DbINFView</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbINFCheck extends DbORCheck {

    //Meta

    public static final MetaField fMode = new MetaField(LocaleMgr.db.getString("mode"));
    public static final MetaRelation1 fUser = new MetaRelation1(LocaleMgr.db.getString("user"), 0);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbINFCheck"),
            DbINFCheck.class, new MetaField[] { fMode, fUser }, MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORCheck.metaClass);

            fMode.setJField(DbINFCheck.class.getDeclaredField("m_mode"));
            fUser.setJField(DbINFCheck.class.getDeclaredField("m_user"));
            fUser.setFlags(MetaField.COPY_REFS);

            fUser.setOppositeRel(DbORUser.fCheckConstraints);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    INFConstraintMode m_mode;
    DbORUser m_user;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbINFCheck() {
    }

    /**
     * Creates an instance of DbINFCheck.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbINFCheck(DbObject composite) throws DbException {
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
     * Sets the "mode" property of a DbINFCheck's instance.
     * 
     * @param value
     *            the "mode" property
     **/
    public final void setMode(INFConstraintMode value) throws DbException {
        basicSet(fMode, value);
    }

    /**
     * Sets the user object associated to a DbINFCheck's instance.
     * 
     * @param value
     *            the user object to be associated
     **/
    public final void setUser(DbORUser value) throws DbException {
        basicSet(fUser, value);
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
     * Gets the "mode" of a DbINFCheck's instance.
     * 
     * @return the "mode"
     **/
    public final INFConstraintMode getMode() throws DbException {
        return (INFConstraintMode) get(fMode);
    }

    /**
     * Gets the user object associated to a DbINFCheck's instance.
     * 
     * @return the user object
     **/
    public final DbORUser getUser() throws DbException {
        return (DbORUser) get(fUser);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
