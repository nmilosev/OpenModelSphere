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
package org.modelsphere.sms.or.oracle.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.oracle.db.srtypes.*;
import org.modelsphere.sms.or.oracle.international.LocaleMgr;
import org.modelsphere.sms.or.db.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORATablespace.html"
 * >DbORATablespace</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbORADataFile extends DbORAFile {

    //Meta

    public static final MetaField fAutoExtend = new MetaField(LocaleMgr.db.getString("autoExtend"));
    public static final MetaField fExtensionsize = new MetaField(LocaleMgr.db
            .getString("extensionsize"));
    public static final MetaField fExtensionSizeUnit = new MetaField(LocaleMgr.db
            .getString("extensionSizeUnit"));
    public static final MetaField fUnlimitedExtension = new MetaField(LocaleMgr.db
            .getString("unlimitedExtension"));
    public static final MetaField fMaxExtensionsize = new MetaField(LocaleMgr.db
            .getString("maxExtensionsize"));
    public static final MetaField fMaxExtensionSizeUnit = new MetaField(LocaleMgr.db
            .getString("maxExtensionSizeUnit"));

    public static final MetaClass metaClass = new MetaClass(
            LocaleMgr.db.getString("DbORADataFile"), DbORADataFile.class, new MetaField[] {
                    fAutoExtend, fExtensionsize, fExtensionSizeUnit, fUnlimitedExtension,
                    fMaxExtensionsize, fMaxExtensionSizeUnit }, MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORAFile.metaClass);
            metaClass.setIcon("dboradatafile.gif");

            fAutoExtend.setJField(DbORADataFile.class.getDeclaredField("m_autoExtend"));
            fExtensionsize.setJField(DbORADataFile.class.getDeclaredField("m_extensionsize"));
            fExtensionSizeUnit.setJField(DbORADataFile.class
                    .getDeclaredField("m_extensionSizeUnit"));
            fUnlimitedExtension.setJField(DbORADataFile.class
                    .getDeclaredField("m_unlimitedExtension"));
            fMaxExtensionsize.setJField(DbORADataFile.class.getDeclaredField("m_maxExtensionsize"));
            fMaxExtensionSizeUnit.setJField(DbORADataFile.class
                    .getDeclaredField("m_maxExtensionSizeUnit"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    SrBoolean m_autoExtend;
    SrInteger m_extensionsize;
    ORASizeUnit m_extensionSizeUnit;
    SrBoolean m_unlimitedExtension;
    SrInteger m_maxExtensionsize;
    ORASizeUnit m_maxExtensionSizeUnit;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORADataFile() {
    }

    /**
     * Creates an instance of DbORADataFile.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORADataFile(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setUnlimitedExtension(Boolean.FALSE);
        setName(LocaleMgr.misc.getString("datafile"));
    }

    //Setters

    /**
     * Sets the "auto extend" property of a DbORADataFile's instance.
     * 
     * @param value
     *            the "auto extend" property
     **/
    public final void setAutoExtend(Boolean value) throws DbException {
        basicSet(fAutoExtend, value);
    }

    /**
     * Sets the "extension size" property of a DbORADataFile's instance.
     * 
     * @param value
     *            the "extension size" property
     **/
    public final void setExtensionsize(Integer value) throws DbException {
        basicSet(fExtensionsize, value);
    }

    /**
     * Sets the "extension size unit" property of a DbORADataFile's instance.
     * 
     * @param value
     *            the "extension size unit" property
     **/
    public final void setExtensionSizeUnit(ORASizeUnit value) throws DbException {
        basicSet(fExtensionSizeUnit, value);
    }

    /**
     * Sets the "unlimited extension" property of a DbORADataFile's instance.
     * 
     * @param value
     *            the "unlimited extension" property
     **/
    public final void setUnlimitedExtension(Boolean value) throws DbException {
        basicSet(fUnlimitedExtension, value);
    }

    /**
     * Sets the "maximum size" property of a DbORADataFile's instance.
     * 
     * @param value
     *            the "maximum size" property
     **/
    public final void setMaxExtensionsize(Integer value) throws DbException {
        basicSet(fMaxExtensionsize, value);
    }

    /**
     * Sets the "maximum size unit" property of a DbORADataFile's instance.
     * 
     * @param value
     *            the "maximum size unit" property
     **/
    public final void setMaxExtensionSizeUnit(ORASizeUnit value) throws DbException {
        basicSet(fMaxExtensionSizeUnit, value);
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
     * Gets the "auto extend" of a DbORADataFile's instance.
     * 
     * @return the "auto extend"
     **/
    public final Boolean getAutoExtend() throws DbException {
        return (Boolean) get(fAutoExtend);
    }

    /**
     * Gets the "extension size" of a DbORADataFile's instance.
     * 
     * @return the "extension size"
     **/
    public final Integer getExtensionsize() throws DbException {
        return (Integer) get(fExtensionsize);
    }

    /**
     * Gets the "extension size unit" of a DbORADataFile's instance.
     * 
     * @return the "extension size unit"
     **/
    public final ORASizeUnit getExtensionSizeUnit() throws DbException {
        return (ORASizeUnit) get(fExtensionSizeUnit);
    }

    /**
     * Gets the "unlimited extension" of a DbORADataFile's instance.
     * 
     * @return the "unlimited extension"
     **/
    public final Boolean getUnlimitedExtension() throws DbException {
        return (Boolean) get(fUnlimitedExtension);
    }

    /**
     * Gets the "maximum size" of a DbORADataFile's instance.
     * 
     * @return the "maximum size"
     **/
    public final Integer getMaxExtensionsize() throws DbException {
        return (Integer) get(fMaxExtensionsize);
    }

    /**
     * Gets the "maximum size unit" of a DbORADataFile's instance.
     * 
     * @return the "maximum size unit"
     **/
    public final ORASizeUnit getMaxExtensionSizeUnit() throws DbException {
        return (ORASizeUnit) get(fMaxExtensionSizeUnit);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
