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
 * HREF="../../../../../org/modelsphere/sms/or/oracle/db/DbORAForeign.html">DbORAForeign</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/ibm/db/DbIBMForeign.html">DbIBMForeign</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/informix/db/DbINFForeign.html">DbINFForeign</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/generic/db/DbGEForeign.html">DbGEForeign</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/or/db/DbORFKeyColumn.html">DbORFKeyColumn</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbORForeign extends DbORConstraint {

    //Meta

    public static final MetaRelation1 fAssociationEnd = new MetaRelation1(LocaleMgr.db
            .getString("associationEnd"), 1);
    public static final MetaRelation1 fIndex = new MetaRelation1(LocaleMgr.db.getString("index"), 0);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbORForeign"),
            DbORForeign.class, new MetaField[] { fAssociationEnd, fIndex },
            MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORConstraint.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbORFKeyColumn.metaClass });
            metaClass.setIcon("dborforeign.gif");

            fAssociationEnd.setJField(DbORForeign.class.getDeclaredField("m_associationEnd"));
            fAssociationEnd.setEditable(false);
            fIndex.setJField(DbORForeign.class.getDeclaredField("m_index"));
            fIndex.setRendererPluginName("DbORIndexConstraint");

            fAssociationEnd.setOppositeRel(DbORAssociationEnd.fMember);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbORAssociationEnd m_associationEnd;
    DbORIndex m_index;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORForeign() {
    }

    /**
     * Creates an instance of DbORForeign.
     * 
     * @param assocend
     *            org.modelsphere.sms.or.db.DbORAssociationEnd
     **/
    public DbORForeign(DbORAssociationEnd assocEnd) throws DbException {
        super(assocEnd.getClassifier());
        setAssociationEnd(assocEnd);
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
     * Sets the association role object associated to a DbORForeign's instance.
     * 
     * @param value
     *            the association role object to be associated
     **/
    public final void setAssociationEnd(DbORAssociationEnd value) throws DbException {
        basicSet(fAssociationEnd, value);
    }

    /**
     * Sets the index object associated to a DbORForeign's instance.
     * 
     * @param value
     *            the index object to be associated
     **/
    public final void setIndex(DbORIndex value) throws DbException {
        basicSet(fIndex, value);
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
     * Gets the association role object associated to a DbORForeign's instance.
     * 
     * @return the association role object
     **/
    public final DbORAssociationEnd getAssociationEnd() throws DbException {
        return (DbORAssociationEnd) get(fAssociationEnd);
    }

    /**
     * Gets the index object associated to a DbORForeign's instance.
     * 
     * @return the index object
     **/
    public final DbORIndex getIndex() throws DbException {
        return (DbORIndex) get(fIndex);
    }

}
