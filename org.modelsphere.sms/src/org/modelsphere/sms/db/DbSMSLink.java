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
package org.modelsphere.sms.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.international.LocaleMgr;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSLinkModel.html">DbSMSLinkModel</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbSMSLink extends DbSMSAbstractRelationship {

    //Meta

    public static final MetaRelationN fTargetObjects = new MetaRelationN(LocaleMgr.db
            .getString("targetObjects"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fSourceObjects = new MetaRelationN(LocaleMgr.db
            .getString("sourceObjects"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fLinkGos = new MetaRelationN(LocaleMgr.db
            .getString("linkGos"), 0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbSMSLink"),
            DbSMSLink.class, new MetaField[] { fTargetObjects, fSourceObjects, fLinkGos },
            MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSAbstractRelationship.metaClass);
            metaClass.setIcon("dbsmslink.gif");

            fTargetObjects.setJField(DbSMSLink.class.getDeclaredField("m_targetObjects"));
            fTargetObjects.setFlags(MetaField.WRITE_CHECK);
            fSourceObjects.setJField(DbSMSLink.class.getDeclaredField("m_sourceObjects"));
            fSourceObjects.setFlags(MetaField.WRITE_CHECK);
            fLinkGos.setJField(DbSMSLink.class.getDeclaredField("m_linkGos"));

            fTargetObjects.setOppositeRel(DbSMSSemanticalObject.fTargetLinks);
            fSourceObjects.setOppositeRel(DbSMSSemanticalObject.fSourceLinks);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbRelationN m_targetObjects;
    DbRelationN m_sourceObjects;
    DbRelationN m_linkGos;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSLink() {
    }

    /**
     * Creates an instance of DbSMSLink.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbSMSLink(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setName(LocaleMgr.misc.getString("link"));
    }

    //Setters

    /**
     * Adds an element to or removes an element from the list of target objects associated to a
     * DbSMSLink's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setTargetObjects(DbSMSSemanticalObject value, int op) throws DbException {
        setRelationNN(fTargetObjects, value, op);
    }

    /**
     * Adds an element to the list of target objects associated to a DbSMSLink's instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToTargetObjects(DbSMSSemanticalObject value) throws DbException {
        setTargetObjects(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of target objects associated to a DbSMSLink's instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromTargetObjects(DbSMSSemanticalObject value) throws DbException {
        setTargetObjects(value, Db.REMOVE_FROM_RELN);
    }

    /**
     * Adds an element to or removes an element from the list of source objects associated to a
     * DbSMSLink's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setSourceObjects(DbSMSSemanticalObject value, int op) throws DbException {
        setRelationNN(fSourceObjects, value, op);
    }

    /**
     * Adds an element to the list of source objects associated to a DbSMSLink's instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToSourceObjects(DbSMSSemanticalObject value) throws DbException {
        setSourceObjects(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of source objects associated to a DbSMSLink's instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromSourceObjects(DbSMSSemanticalObject value) throws DbException {
        setSourceObjects(value, Db.REMOVE_FROM_RELN);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fTargetObjects)
                setTargetObjects((DbSMSSemanticalObject) value, Db.ADD_TO_RELN);
            else if (metaField == fSourceObjects)
                setSourceObjects((DbSMSSemanticalObject) value, Db.ADD_TO_RELN);
            else if (metaField == fLinkGos)
                ((DbSMSLinkGo) value).setLink(this);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        if (relation == fTargetObjects)
            setTargetObjects((DbSMSSemanticalObject) neighbor, op);
        else if (relation == fSourceObjects)
            setSourceObjects((DbSMSSemanticalObject) neighbor, op);
        else
            super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the list of target objects associated to a DbSMSLink's instance.
     * 
     * @return the list of target objects.
     **/
    public final DbRelationN getTargetObjects() throws DbException {
        return (DbRelationN) get(fTargetObjects);
    }

    /**
     * Gets the list of source objects associated to a DbSMSLink's instance.
     * 
     * @return the list of source objects.
     **/
    public final DbRelationN getSourceObjects() throws DbException {
        return (DbRelationN) get(fSourceObjects);
    }

    /**
     * Gets the list of link graphical objects associated to a DbSMSLink's instance.
     * 
     * @return the list of link graphical objects.
     **/
    public final DbRelationN getLinkGos() throws DbException {
        return (DbRelationN) get(fLinkGos);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
