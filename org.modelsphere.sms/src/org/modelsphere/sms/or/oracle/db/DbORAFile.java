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
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORADataFile.html">DbORADataFile</A>,
 * <A HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORARedoLogGroup.html">
 * DbORARedoLogGroup</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbORAFile extends DbSMSSemanticalObject {

    //Meta

    public static final MetaField fSize = new MetaField(LocaleMgr.db.getString("size"));
    public static final MetaField fSizeUnit = new MetaField(LocaleMgr.db.getString("sizeUnit"));
    public static final MetaField fReuse = new MetaField(LocaleMgr.db.getString("reuse"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbORAFile"),
            DbORAFile.class, new MetaField[] { fSize, fSizeUnit, fReuse },
            MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSSemanticalObject.metaClass);

            fSize.setJField(DbORAFile.class.getDeclaredField("m_size"));
            fSizeUnit.setJField(DbORAFile.class.getDeclaredField("m_sizeUnit"));
            fReuse.setJField(DbORAFile.class.getDeclaredField("m_reuse"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    SrInteger m_size;
    ORASizeUnit m_sizeUnit;
    boolean m_reuse;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORAFile() {
    }

    /**
     * Creates an instance of DbORAFile.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORAFile(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setReuse(Boolean.TRUE);
    }

    //Setters

    /**
     * Sets the "size" property of a DbORAFile's instance.
     * 
     * @param value
     *            the "size" property
     **/
    public final void setSize(Integer value) throws DbException {
        basicSet(fSize, value);
    }

    /**
     * Sets the "size unit" property of a DbORAFile's instance.
     * 
     * @param value
     *            the "size unit" property
     **/
    public final void setSizeUnit(ORASizeUnit value) throws DbException {
        basicSet(fSizeUnit, value);
    }

    /**
     * Sets the "reuse" property of a DbORAFile's instance.
     * 
     * @param value
     *            the "reuse" property
     **/
    public final void setReuse(Boolean value) throws DbException {
        basicSet(fReuse, value);
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
     * Gets the "size" of a DbORAFile's instance.
     * 
     * @return the "size"
     **/
    public final Integer getSize() throws DbException {
        return (Integer) get(fSize);
    }

    /**
     * Gets the "size unit" of a DbORAFile's instance.
     * 
     * @return the "size unit"
     **/
    public final ORASizeUnit getSizeUnit() throws DbException {
        return (ORASizeUnit) get(fSizeUnit);
    }

    /**
     * Gets the "reuse" property's Boolean value of a DbORAFile's instance.
     * 
     * @return the "reuse" property's Boolean value
     * @deprecated use isReuse() method instead
     **/
    public final Boolean getReuse() throws DbException {
        return (Boolean) get(fReuse);
    }

    /**
     * Tells whether a DbORAFile's instance is reuse or not.
     * 
     * @return boolean
     **/
    public final boolean isReuse() throws DbException {
        return getReuse().booleanValue();
    }

}
