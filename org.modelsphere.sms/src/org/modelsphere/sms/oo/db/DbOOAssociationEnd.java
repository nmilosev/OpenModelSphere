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
import org.modelsphere.sms.db.srtypes.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../../org/modelsphere/sms/oo/java/db/DbJVAssociationEnd.html"
 * >DbJVAssociationEnd</A>.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/oo/db/DbOOAssociation.html">DbOOAssociation</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbOOAssociationEnd extends DbSMSAssociationEnd {

    //Meta

    public static final MetaRelation1 fAssociationMember = new MetaRelation1(LocaleMgr.db
            .getString("associationMember"), 1);
    public static final MetaField fNavigable = new MetaField(LocaleMgr.db.getString("navigable"));
    public static final MetaField fAggregation = new MetaField(LocaleMgr.db
            .getString("aggregation"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbOOAssociationEnd"), DbOOAssociationEnd.class, new MetaField[] {
            fAssociationMember, fNavigable, fAggregation }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSAssociationEnd.metaClass);

            fAssociationMember.setJField(DbOOAssociationEnd.class
                    .getDeclaredField("m_associationMember"));
            fAssociationMember.setScreenOrder("<multiplicity");
            fAssociationMember.setRendererPluginName("DbClassifierQualName");
            fAssociationMember.setEditable(false);
            fNavigable.setJField(DbOOAssociationEnd.class.getDeclaredField("m_navigable"));
            fNavigable.setScreenOrder(">multiplicity");
            fAggregation.setJField(DbOOAssociationEnd.class.getDeclaredField("m_aggregation"));

            fAssociationMember.setOppositeRel(DbOODataMember.fAssociationEnd);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbOODataMember m_associationMember;
    boolean m_navigable;
    OOAggregation m_aggregation;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbOOAssociationEnd() {
    }

    /**
     * Creates an instance of DbOOAssociationEnd.
     * 
     * @param composite
     *            org.modelsphere.sms.oo.db.DbOOAssociation
     * @param associationmember
     *            org.modelsphere.sms.oo.db.DbOODataMember
     * @param multiplicity
     *            org.modelsphere.sms.db.srtypes.SMSMultiplicity
     **/
    protected DbOOAssociationEnd(DbOOAssociation composite, DbOODataMember associationMember,
            SMSMultiplicity multiplicity) throws DbException {
        super(composite);
        basicSet(fAssociationMember, associationMember);
        setMultiplicity(multiplicity);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setNavigable(Boolean.TRUE);
        setAggregation(OOAggregation.getInstance(OOAggregation.NONE));
    }

    /**
     * @return association end
     **/
    public final DbOOAssociationEnd getOppositeEnd() throws DbException {
        DbOOAssociation assoc = (DbOOAssociation) getComposite();
        DbRelationN ends = assoc.getComponents();
        DbOOAssociationEnd oppEnd = (DbOOAssociationEnd) ends.elementAt(0);
        if (oppEnd == this)
            oppEnd = (DbOOAssociationEnd) ends.elementAt(1);
        return oppEnd;
    }

    /**
     * @return boolean
     **/
    public final boolean isFrontEnd() throws DbException {
        DbOOAssociation assoc = (DbOOAssociation) getComposite();
        DbRelationN ends = assoc.getComponents();
        return (this == ends.elementAt(0));
    }

    //Setters

    /**
     * Sets the field object associated to a DbOOAssociationEnd's instance.
     * 
     * @param value
     *            the field object to be associated
     **/
    public final void setAssociationMember(DbOODataMember value) throws DbException {
        basicSet(fAssociationMember, value);
    }

    /**
     * Sets the "navigable" property of a DbOOAssociationEnd's instance.
     * 
     * @param value
     *            the "navigable" property
     **/
    public final void setNavigable(Boolean value) throws DbException {
        basicSet(fNavigable, value);
    }

    /**
     * Sets the "aggregation" property of a DbOOAssociationEnd's instance.
     * 
     * @param value
     *            the "aggregation" property
     **/
    public final void setAggregation(OOAggregation value) throws DbException {
        basicSet(fAggregation, value);
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
     * Gets the field object associated to a DbOOAssociationEnd's instance.
     * 
     * @return the field object
     **/
    public final DbOODataMember getAssociationMember() throws DbException {
        return (DbOODataMember) get(fAssociationMember);
    }

    /**
     * Gets the "navigable" property's Boolean value of a DbOOAssociationEnd's instance.
     * 
     * @return the "navigable" property's Boolean value
     * @deprecated use isNavigable() method instead
     **/
    public final Boolean getNavigable() throws DbException {
        return (Boolean) get(fNavigable);
    }

    /**
     * Tells whether a DbOOAssociationEnd's instance is navigable or not.
     * 
     * @return boolean
     **/
    public final boolean isNavigable() throws DbException {
        return getNavigable().booleanValue();
    }

    /**
     * Gets the "aggregation" of a DbOOAssociationEnd's instance.
     * 
     * @return the "aggregation"
     **/
    public final OOAggregation getAggregation() throws DbException {
        return (OOAggregation) get(fAggregation);
    }

}
