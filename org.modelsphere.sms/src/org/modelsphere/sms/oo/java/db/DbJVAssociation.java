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
package org.modelsphere.sms.oo.java.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.oo.java.db.srtypes.*;
import org.modelsphere.sms.oo.java.international.LocaleMgr;
import org.modelsphere.sms.oo.db.*;
import org.modelsphere.sms.oo.db.srtypes.*;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.SMSFilter;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/oo/db/DbOOClass.html">DbOOClass</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/oo/db/DbOOAssociationEnd.html">
 * DbOOAssociationEnd</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbJVAssociation extends DbOOAssociation {

    //Meta

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbJVAssociation"), DbJVAssociation.class, new MetaField[] {}, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbOOAssociation.metaClass);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbJVAssociation() {
    }

    /**
     * Creates an instance of DbJVAssociation.
     * 
     * @param frontendmember
     *            org.modelsphere.sms.oo.java.db.DbJVDataMember
     * @param frontendmult
     *            org.modelsphere.sms.db.srtypes.SMSMultiplicity
     * @param backendmember
     *            org.modelsphere.sms.oo.java.db.DbJVDataMember
     * @param backendmult
     *            org.modelsphere.sms.db.srtypes.SMSMultiplicity
     **/
    public DbJVAssociation(DbJVDataMember frontEndMember, SMSMultiplicity frontEndMult,
            DbJVDataMember backEndMember, SMSMultiplicity backEndMult) throws DbException {
        super(frontEndMember.getComposite());
        setDefaultInitialValues();
        setName(((DbJVClass) frontEndMember.getComposite()).getName() + "-"
                + ((DbJVClass) backEndMember.getComposite()).getName());
        new DbJVAssociationEnd(this, frontEndMember, frontEndMult);
        new DbJVAssociationEnd(this, backEndMember, backEndMult);
    }

    private void setDefaultInitialValues() throws DbException {
    }

    /**
     * @return boolean
     **/
    public final boolean hasWriteAccess() throws DbException {
        if (writeAccess == 0) {
            writeAccess = (getFrontEnd().hasWriteAccess() || getBackEnd().hasWriteAccess() ? ACCESS_GRANTED
                    : ACCESS_NOT_GRANTED);
        }
        return (writeAccess == ACCESS_GRANTED);
    }

    /**
     * @return int
     **/
    protected final int getFeatureSet() {
        return SMSFilter.JAVA;

    }

    //Setters

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

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
