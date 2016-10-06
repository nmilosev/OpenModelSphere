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
import org.modelsphere.sms.or.db.util.AnyORObject;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.jack.baseDb.util.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../../org/modelsphere/sms/or/db/DbORView.html">DbORView</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/db/DbORTable.html">DbORTable</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbORAbsTable extends DbSMSClassifier {

    //Meta

    public static final MetaField fSelectionRule = new MetaField(LocaleMgr.db
            .getString("selectionRule"));
    public static final MetaRelationN fAssociationEnds = new MetaRelationN(LocaleMgr.db
            .getString("associationEnds"), 0, MetaRelationN.cardN);
    public static final MetaRelation1 fUser = new MetaRelation1(LocaleMgr.db.getString("user"), 0);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbORAbsTable"),
            DbORAbsTable.class, new MetaField[] { fSelectionRule, fAssociationEnds, fUser },
            MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSClassifier.metaClass);

            fSelectionRule.setJField(DbORAbsTable.class.getDeclaredField("m_selectionRule"));
            fSelectionRule.setRendererPluginName("LookupDescription");
            fAssociationEnds.setJField(DbORAbsTable.class.getDeclaredField("m_associationEnds"));
            fUser.setJField(DbORAbsTable.class.getDeclaredField("m_user"));
            fUser.setFlags(MetaField.COPY_REFS);

            fUser.setOppositeRel(DbORUser.fTables);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    String m_selectionRule;
    DbRelationN m_associationEnds;
    DbORUser m_user;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORAbsTable() {
    }

    /**
     * Creates an instance of DbORAbsTable.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORAbsTable(DbObject composite) throws DbException {
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
     * @param value
     *            org.modelsphere.sms.db.DbSMSSemanticalObject
     **/
    public void setSuperCopy(DbSMSSemanticalObject value) throws DbException {
        Object oldValue = get(fSuperCopy);
        if (oldValue != null && oldValue != value) {
            DbEnumeration dbEnum = getComponents().elements(DbSMSSemanticalObject.metaClass);
            while (dbEnum.hasMoreElements())
                ((DbSMSSemanticalObject) dbEnum.nextElement()).setSuperCopy(null);
            dbEnum.close();
            dbEnum = getAssociationEnds().elements();
            while (dbEnum.hasMoreElements())
                ((DbORAssociation) dbEnum.nextElement().getComposite()).setSuperCopy(null);
            dbEnum.close();
        }
        basicSet(fSuperCopy, value);
    }

    /**
     * @return int
     **/
    protected final int getSecurityMask() {
        return SMSFilter.RELATIONAL;

    }

    //Setters

    /**
     * Sets the "selection rule" property of a DbORAbsTable's instance.
     * 
     * @param value
     *            the "selection rule" property
     **/
    public final void setSelectionRule(String value) throws DbException {
        basicSet(fSelectionRule, value);
    }

    /**
     * Sets the user object associated to a DbORAbsTable's instance.
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
            if (metaField == fAssociationEnds)
                ((DbORAssociationEnd) value).setClassifier(this);
            else
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
     * Gets the "selection rule" property of a DbORAbsTable's instance.
     * 
     * @return the "selection rule" property
     **/
    public final String getSelectionRule() throws DbException {
        return (String) get(fSelectionRule);
    }

    /**
     * Gets the list of association roles associated to a DbORAbsTable's instance.
     * 
     * @return the list of association roles.
     **/
    public final DbRelationN getAssociationEnds() throws DbException {
        return (DbRelationN) get(fAssociationEnds);
    }

    /**
     * Gets the user object associated to a DbORAbsTable's instance.
     * 
     * @return the user object
     **/
    public final DbORUser getUser() throws DbException {
        return (DbORUser) get(fUser);
    }

}
