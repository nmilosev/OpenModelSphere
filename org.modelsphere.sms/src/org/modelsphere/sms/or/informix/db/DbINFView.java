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
import org.modelsphere.jack.baseDb.util.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFDataModel.html"
 * >DbINFDataModel</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFColumn.html">DbINFColumn</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFForeign.html">DbINFForeign</A>,
 * <A HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFPrimaryUnique.html">
 * DbINFPrimaryUnique</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFCheck.html">DbINFCheck</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFTrigger.html">DbINFTrigger</A>,
 * <A HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFIndex.html">DbINFIndex</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbINFView extends DbORView {

    //Meta

    public static final MetaField fCheckOption = new MetaField(LocaleMgr.db
            .getString("checkOption"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbINFView"),
            DbINFView.class, new MetaField[] { fCheckOption }, MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORView.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbINFColumn.metaClass,
                    DbINFForeign.metaClass, DbINFPrimaryUnique.metaClass, DbINFCheck.metaClass,
                    DbINFTrigger.metaClass, DbINFIndex.metaClass });

            fCheckOption.setJField(DbINFView.class.getDeclaredField("m_checkOption"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    boolean m_checkOption;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbINFView() {
    }

    /**
     * Creates an instance of DbINFView.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbINFView(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setCheckOption(Boolean.FALSE);
        DbORDataModel dataModel = (DbORDataModel) getCompositeOfType(DbORDataModel.metaClass);
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology term = terminologyUtil.findModelTerminology(dataModel);
        setName(term.getTerm(metaClass));
    }

    //Setters

    /**
     * Sets the "with check option" property of a DbINFView's instance.
     * 
     * @param value
     *            the "with check option" property
     **/
    public final void setCheckOption(Boolean value) throws DbException {
        basicSet(fCheckOption, value);
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
     * Gets the "with check option" property's Boolean value of a DbINFView's instance.
     * 
     * @return the "with check option" property's Boolean value
     * @deprecated use isCheckOption() method instead
     **/
    public final Boolean getCheckOption() throws DbException {
        return (Boolean) get(fCheckOption);
    }

    /**
     * Tells whether a DbINFView's instance is checkOption or not.
     * 
     * @return boolean
     **/
    public final boolean isCheckOption() throws DbException {
        return getCheckOption().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
