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
import javax.swing.Icon;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFDatabase.html">DbINFDatabase</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbINFSbspace extends DbSMSSemanticalObject {

    //Meta

    public static final MetaRelationN fLobColumns = new MetaRelationN(LocaleMgr.db
            .getString("lobColumns"), 0, MetaRelationN.cardN);
    public static final MetaField fBlobSpace = new MetaField(LocaleMgr.db.getString("blobSpace"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbINFSbspace"),
            DbINFSbspace.class, new MetaField[] { fLobColumns, fBlobSpace },
            MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSSemanticalObject.metaClass);
            metaClass.setIcon("dbinfsbspace.gif");

            fLobColumns.setJField(DbINFSbspace.class.getDeclaredField("m_lobColumns"));
            fBlobSpace.setJField(DbINFSbspace.class.getDeclaredField("m_blobSpace"));

            fLobColumns.setOppositeRel(DbINFColumn.fSbspaces);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;
    private static Icon blobSpaceIcon = org.modelsphere.jack.graphic.GraphicUtil.loadIcon(
            DbINFSbspace.class, "resources/dbinfblobspace.gif");
    private static Icon sbSpaceIcon = org.modelsphere.jack.graphic.GraphicUtil.loadIcon(
            DbINFSbspace.class, "resources/dbinfsbspace.gif");

    //Instance variables
    DbRelationN m_lobColumns;
    boolean m_blobSpace;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbINFSbspace() {
    }

    /**
     * Creates an instance of DbINFSbspace.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbINFSbspace(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setBlobSpace(Boolean.FALSE);
        setName(LocaleMgr.misc.getString("sbspace"));
    }

    /**
     * @param form
     *            int
     * @return icon
     **/
    public final Icon getSemanticalIcon(int form) throws DbException {
        if (isBlobSpace())
            return blobSpaceIcon;
        else
            return sbSpaceIcon;

    }

    //Setters

    /**
     * Adds an element to or removes an element from the list of lob columns associated to a
     * DbINFSbspace's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setLobColumns(DbINFColumn value, int op) throws DbException {
        setRelationNN(fLobColumns, value, op);
    }

    /**
     * Adds an element to the list of lob columns associated to a DbINFSbspace's instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToLobColumns(DbINFColumn value) throws DbException {
        setLobColumns(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of lob columns associated to a DbINFSbspace's instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromLobColumns(DbINFColumn value) throws DbException {
        setLobColumns(value, Db.REMOVE_FROM_RELN);
    }

    /**
     * Sets the "blobspace" property of a DbINFSbspace's instance.
     * 
     * @param value
     *            the "blobspace" property
     **/
    public final void setBlobSpace(Boolean value) throws DbException {
        basicSet(fBlobSpace, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fLobColumns)
                setLobColumns((DbINFColumn) value, Db.ADD_TO_RELN);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        if (relation == fLobColumns)
            setLobColumns((DbINFColumn) neighbor, op);
        else
            super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the list of lob columns associated to a DbINFSbspace's instance.
     * 
     * @return the list of lob columns.
     **/
    public final DbRelationN getLobColumns() throws DbException {
        return (DbRelationN) get(fLobColumns);
    }

    /**
     * Gets the "blobspace" property's Boolean value of a DbINFSbspace's instance.
     * 
     * @return the "blobspace" property's Boolean value
     * @deprecated use isBlobSpace() method instead
     **/
    public final Boolean getBlobSpace() throws DbException {
        return (Boolean) get(fBlobSpace);
    }

    /**
     * Tells whether a DbINFSbspace's instance is blobSpace or not.
     * 
     * @return boolean
     **/
    public final boolean isBlobSpace() throws DbException {
        return getBlobSpace().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
