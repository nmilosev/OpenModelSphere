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

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMContainerClause.html"
 * >DbIBMContainerClause</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public final class DbIBMContainerItem extends DbObject {

    //Meta

    public static final MetaField fFileOrDevice = new MetaField(LocaleMgr.db
            .getString("fileOrDevice"));
    public static final MetaField fContainerString = new MetaField(LocaleMgr.db
            .getString("DbIBMContainerItem.containerString"));
    public static final MetaField fNbOfPage = new MetaField(LocaleMgr.db.getString("nbOfPage"));
    public static final MetaField fNbOfPageSize = new MetaField(LocaleMgr.db
            .getString("nbOfPageSize"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbIBMContainerItem"), DbIBMContainerItem.class, new MetaField[] {
            fFileOrDevice, fContainerString, fNbOfPage, fNbOfPageSize }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbObject.metaClass);
            metaClass.setIcon("dbibmcontaineritem.gif");

            fFileOrDevice.setJField(DbIBMContainerItem.class.getDeclaredField("m_fileOrDevice"));
            fContainerString.setJField(DbIBMContainerItem.class
                    .getDeclaredField("m_containerString"));
            fNbOfPage.setJField(DbIBMContainerItem.class.getDeclaredField("m_nbOfPage"));
            fNbOfPageSize.setJField(DbIBMContainerItem.class.getDeclaredField("m_nbOfPageSize"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    IBMFileOrDevice m_fileOrDevice;
    String m_containerString;
    int m_nbOfPage;
    IBMSizeUnit m_nbOfPageSize;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbIBMContainerItem() {
    }

    /**
     * Creates an instance of DbIBMContainerItem.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbIBMContainerItem(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        org.modelsphere.sms.or.ibm.db.util.IBMUtility.getInstance().setInitialValues(this);
    }

    //Setters

    /**
     * Sets the "file or device" property of a DbIBMContainerItem's instance.
     * 
     * @param value
     *            the "file or device" property
     **/
    public final void setFileOrDevice(IBMFileOrDevice value) throws DbException {
        basicSet(fFileOrDevice, value);
    }

    /**
     * Sets the "database container text" property of a DbIBMContainerItem's instance.
     * 
     * @param value
     *            the "database container text" property
     **/
    public final void setContainerString(String value) throws DbException {
        basicSet(fContainerString, value);
    }

    /**
     * Sets the "number of pages" property of a DbIBMContainerItem's instance.
     * 
     * @param value
     *            the "number of pages" property
     **/
    public final void setNbOfPage(Integer value) throws DbException {
        basicSet(fNbOfPage, value);
    }

    /**
     * Sets the "units of number of pages" property of a DbIBMContainerItem's instance.
     * 
     * @param value
     *            the "units of number of pages" property
     **/
    public final void setNbOfPageSize(IBMSizeUnit value) throws DbException {
        basicSet(fNbOfPageSize, value);
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
     * Gets the "file or device" of a DbIBMContainerItem's instance.
     * 
     * @return the "file or device"
     **/
    public final IBMFileOrDevice getFileOrDevice() throws DbException {
        return (IBMFileOrDevice) get(fFileOrDevice);
    }

    /**
     * Gets the "database container text" property of a DbIBMContainerItem's instance.
     * 
     * @return the "database container text" property
     **/
    public final String getContainerString() throws DbException {
        return (String) get(fContainerString);
    }

    /**
     * Gets the "number of pages" property of a DbIBMContainerItem's instance.
     * 
     * @return the "number of pages" property
     **/
    public final Integer getNbOfPage() throws DbException {
        return (Integer) get(fNbOfPage);
    }

    /**
     * Gets the "units of number of pages" of a DbIBMContainerItem's instance.
     * 
     * @return the "units of number of pages"
     **/
    public final IBMSizeUnit getNbOfPageSize() throws DbException {
        return (IBMSizeUnit) get(fNbOfPageSize);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
