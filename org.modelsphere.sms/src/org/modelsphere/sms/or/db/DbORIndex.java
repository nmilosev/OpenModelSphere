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
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.jack.baseDb.util.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../../org/modelsphere/sms/or/oracle/db/DbORAIndex.html">DbORAIndex</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/ibm/db/DbIBMIndex.html">DbIBMIndex</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/informix/db/DbINFIndex.html">DbINFIndex</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/generic/db/DbGEIndex.html">DbGEIndex</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/or/db/DbORIndexKey.html">DbORIndexKey</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbORIndex extends DbSMSIndex {

    //Meta

    public static final MetaField fUnique = new MetaField(LocaleMgr.db.getString("unique"));
    public static final MetaChoice fConstraint = new MetaChoice(LocaleMgr.db
            .getString("constraint"), 0);
    public static final MetaRelation1 fUser = new MetaRelation1(LocaleMgr.db.getString("user"), 0);
    public static final MetaField fPrimary = new MetaField(LocaleMgr.db.getString("primary"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbORIndex"),
            DbORIndex.class, new MetaField[] { fUnique, fConstraint, fUser, fPrimary }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSIndex.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbORIndexKey.metaClass });
            metaClass.setIcon("dborindex.gif");

            fUnique.setJField(DbORIndex.class.getDeclaredField("m_unique"));
            fConstraint.setJField(DbORIndex.class.getDeclaredField("m_constraint"));
            fConstraint.setRendererPluginName("DbORIndexConstraint");
            fUser.setJField(DbORIndex.class.getDeclaredField("m_user"));
            fUser.setFlags(MetaField.COPY_REFS);
            fPrimary.setJField(DbORIndex.class.getDeclaredField("m_primary"));

            fConstraint.setOppositeRels(new MetaRelation[] { DbORPrimaryUnique.fIndex,
                    DbORForeign.fIndex });
            fUser.setOppositeRel(DbORUser.fIndexes);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    boolean m_unique;
    DbORConstraint m_constraint;
    DbORUser m_user;
    boolean m_primary;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORIndex() {
    }

    /**
     * Creates an instance of DbORIndex.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORIndex(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setUnique(Boolean.FALSE);
        setPrimary(Boolean.FALSE);
        DbORDataModel dataModel = (DbORDataModel) getCompositeOfType(DbORDataModel.metaClass);
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology term = terminologyUtil.findModelTerminology(dataModel);
        setName(term.getTerm(metaClass));
    }

    /**
     * @param pufkey
     *            org.modelsphere.sms.or.db.DbORConstraint
     * @return boolean
     **/
    public final boolean matchesConstraint(DbORConstraint pufKey) throws DbException {
        DbRelationN indCols = getComponents();
        DbRelationN pufCols = (pufKey instanceof DbORPrimaryUnique ? ((DbORPrimaryUnique) pufKey)
                .getColumns() : pufKey.getComponents());
        int iCol = 0;
        for (int i = 0; i < indCols.size(); i++) {
            DbObject dbo = indCols.elementAt(i);
            if (!(dbo instanceof DbORIndexKey))
                continue;
            if (iCol == pufCols.size())
                return false;
            DbORColumn pufCol = (pufKey instanceof DbORPrimaryUnique ? (DbORColumn) pufCols
                    .elementAt(iCol) : ((DbORFKeyColumn) pufCols.elementAt(iCol)).getColumn());
            if (((DbORIndexKey) dbo).getIndexedElement() != pufCol)
                return false;
            iCol++;
        }
        return (iCol == pufCols.size());
    }

    /**
     * @return int
     **/
    protected final int getSecurityMask() {
        return SMSFilter.RELATIONAL;

    }

    //Setters

    /**
     * Sets the "unique" property of a DbORIndex's instance.
     * 
     * @param value
     *            the "unique" property
     **/
    public final void setUnique(Boolean value) throws DbException {
        basicSet(fUnique, value);
    }

    /**
     * Sets the constraint object associated to a DbORIndex's instance.
     * 
     * @param value
     *            the constraint object to be associated
     **/
    public final void setConstraint(DbORConstraint value) throws DbException {
        basicSet(fConstraint, value);
    }

    /**
     * Sets the user object associated to a DbORIndex's instance.
     * 
     * @param value
     *            the user object to be associated
     **/
    public final void setUser(DbORUser value) throws DbException {
        basicSet(fUser, value);
    }

    /**
     * Sets the "primary" property of a DbORIndex's instance.
     * 
     * @param value
     *            the "primary" property
     **/
    public final void setPrimary(Boolean value) throws DbException {
        basicSet(fPrimary, value);
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
     * Gets the "unique" property's Boolean value of a DbORIndex's instance.
     * 
     * @return the "unique" property's Boolean value
     * @deprecated use isUnique() method instead
     **/
    public final Boolean getUnique() throws DbException {
        return (Boolean) get(fUnique);
    }

    /**
     * Tells whether a DbORIndex's instance is unique or not.
     * 
     * @return boolean
     **/
    public final boolean isUnique() throws DbException {
        return getUnique().booleanValue();
    }

    /**
     * Gets the constraint object associated to a DbORIndex's instance.
     * 
     * @return the constraint object
     **/
    public final DbORConstraint getConstraint() throws DbException {
        return (DbORConstraint) get(fConstraint);
    }

    /**
     * Gets the user object associated to a DbORIndex's instance.
     * 
     * @return the user object
     **/
    public final DbORUser getUser() throws DbException {
        return (DbORUser) get(fUser);
    }

    /**
     * Gets the "primary" property's Boolean value of a DbORIndex's instance.
     * 
     * @return the "primary" property's Boolean value
     * @deprecated use isPrimary() method instead
     **/
    public final Boolean getPrimary() throws DbException {
        return (Boolean) get(fPrimary);
    }

    /**
     * Tells whether a DbORIndex's instance is primary or not.
     * 
     * @return boolean
     **/
    public final boolean isPrimary() throws DbException {
        return getPrimary().booleanValue();
    }

}
