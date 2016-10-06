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
package org.modelsphere.sms.or.ibm.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.ibm.db.srtypes.*;
import org.modelsphere.sms.or.ibm.international.LocaleMgr;
import org.modelsphere.sms.or.db.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.jack.baseDb.util.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMTable.html">DbIBMTable</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMView.html">DbIBMView</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbIBMColumn extends DbORColumn {

    //Meta

    public static final MetaField fLobSizeUnit = new MetaField(LocaleMgr.db
            .getString("lobSizeUnit"));
    public static final MetaField fForBitData = new MetaField(LocaleMgr.db.getString("forBitData"));
    public static final MetaField fLogged = new MetaField(LocaleMgr.db.getString("logged"));
    public static final MetaField fCompact = new MetaField(LocaleMgr.db.getString("compact"));
    public static final MetaField fLinkTypeURL = new MetaField(LocaleMgr.db
            .getString("linkTypeURL"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbIBMColumn"),
            DbIBMColumn.class, new MetaField[] { fLobSizeUnit, fForBitData, fLogged, fCompact,
                    fLinkTypeURL }, MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORColumn.metaClass);

            fLobSizeUnit.setJField(DbIBMColumn.class.getDeclaredField("m_lobSizeUnit"));
            fForBitData.setJField(DbIBMColumn.class.getDeclaredField("m_forBitData"));
            fLogged.setJField(DbIBMColumn.class.getDeclaredField("m_logged"));
            fCompact.setJField(DbIBMColumn.class.getDeclaredField("m_compact"));
            fLinkTypeURL.setJField(DbIBMColumn.class.getDeclaredField("m_linkTypeURL"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    IBMSizeUnit m_lobSizeUnit;
    boolean m_forBitData;
    boolean m_logged;
    boolean m_compact;
    boolean m_linkTypeURL;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbIBMColumn() {
    }

    /**
     * Creates an instance of DbIBMColumn.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbIBMColumn(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setForBitData(Boolean.FALSE);
        setLogged(Boolean.TRUE);
        setCompact(Boolean.FALSE);
        DbORDataModel dataModel = (DbORDataModel) getCompositeOfType(DbORDataModel.metaClass);
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology term = terminologyUtil.findModelTerminology(dataModel);
        setName(term.getTerm(metaClass));
    }

    //Setters

    /**
     * Sets the "size unit" property of a DbIBMColumn's instance.
     * 
     * @param value
     *            the "size unit" property
     **/
    public final void setLobSizeUnit(IBMSizeUnit value) throws DbException {
        basicSet(fLobSizeUnit, value);
    }

    /**
     * Sets the "for bit data" property of a DbIBMColumn's instance.
     * 
     * @param value
     *            the "for bit data" property
     **/
    public final void setForBitData(Boolean value) throws DbException {
        basicSet(fForBitData, value);
    }

    /**
     * Sets the "logged" property of a DbIBMColumn's instance.
     * 
     * @param value
     *            the "logged" property
     **/
    public final void setLogged(Boolean value) throws DbException {
        basicSet(fLogged, value);
    }

    /**
     * Sets the "compact" property of a DbIBMColumn's instance.
     * 
     * @param value
     *            the "compact" property
     **/
    public final void setCompact(Boolean value) throws DbException {
        basicSet(fCompact, value);
    }

    /**
     * Sets the "link type url" property of a DbIBMColumn's instance.
     * 
     * @param value
     *            the "link type url" property
     **/
    public final void setLinkTypeURL(Boolean value) throws DbException {
        basicSet(fLinkTypeURL, value);
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
     * Gets the "size unit" of a DbIBMColumn's instance.
     * 
     * @return the "size unit"
     **/
    public final IBMSizeUnit getLobSizeUnit() throws DbException {
        return (IBMSizeUnit) get(fLobSizeUnit);
    }

    /**
     * Gets the "for bit data" property's Boolean value of a DbIBMColumn's instance.
     * 
     * @return the "for bit data" property's Boolean value
     * @deprecated use isForBitData() method instead
     **/
    public final Boolean getForBitData() throws DbException {
        return (Boolean) get(fForBitData);
    }

    /**
     * Tells whether a DbIBMColumn's instance is forBitData or not.
     * 
     * @return boolean
     **/
    public final boolean isForBitData() throws DbException {
        return getForBitData().booleanValue();
    }

    /**
     * Gets the "logged" property's Boolean value of a DbIBMColumn's instance.
     * 
     * @return the "logged" property's Boolean value
     * @deprecated use isLogged() method instead
     **/
    public final Boolean getLogged() throws DbException {
        return (Boolean) get(fLogged);
    }

    /**
     * Tells whether a DbIBMColumn's instance is logged or not.
     * 
     * @return boolean
     **/
    public final boolean isLogged() throws DbException {
        return getLogged().booleanValue();
    }

    /**
     * Gets the "compact" property's Boolean value of a DbIBMColumn's instance.
     * 
     * @return the "compact" property's Boolean value
     * @deprecated use isCompact() method instead
     **/
    public final Boolean getCompact() throws DbException {
        return (Boolean) get(fCompact);
    }

    /**
     * Tells whether a DbIBMColumn's instance is compact or not.
     * 
     * @return boolean
     **/
    public final boolean isCompact() throws DbException {
        return getCompact().booleanValue();
    }

    /**
     * Gets the "link type url" property's Boolean value of a DbIBMColumn's instance.
     * 
     * @return the "link type url" property's Boolean value
     * @deprecated use isLinkTypeURL() method instead
     **/
    public final Boolean getLinkTypeURL() throws DbException {
        return (Boolean) get(fLinkTypeURL);
    }

    /**
     * Tells whether a DbIBMColumn's instance is linkTypeURL or not.
     * 
     * @return boolean
     **/
    public final boolean isLinkTypeURL() throws DbException {
        return getLinkTypeURL().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
