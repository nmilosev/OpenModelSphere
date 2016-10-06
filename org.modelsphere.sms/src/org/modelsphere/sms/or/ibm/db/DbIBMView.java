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
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMDataModel.html">DbIBMDataModel</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMColumn.html">DbIBMColumn</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMForeign.html">DbIBMForeign</A>, <A
 * HREF
 * ="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMPrimaryUnique.html">DbIBMPrimaryUnique<
 * /A>, <A HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMCheck.html">DbIBMCheck</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMTrigger.html">DbIBMTrigger</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMIndex.html">DbIBMIndex</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbIBMView extends DbORView {

    //Meta

    public static final MetaField fIsFederated = new MetaField(LocaleMgr.db
            .getString("isFederated"));
    public static final MetaField fCheckOption = new MetaField(LocaleMgr.db
            .getString("checkOption"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbIBMView"),
            DbIBMView.class, new MetaField[] { fIsFederated, fCheckOption }, MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORView.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbIBMColumn.metaClass,
                    DbIBMForeign.metaClass, DbIBMPrimaryUnique.metaClass, DbIBMCheck.metaClass,
                    DbIBMTrigger.metaClass, DbIBMIndex.metaClass });

            fIsFederated.setJField(DbIBMView.class.getDeclaredField("m_isFederated"));
            fCheckOption.setJField(DbIBMView.class.getDeclaredField("m_checkOption"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    IBMFederatedOption m_isFederated;
    IBMViewCheckOption m_checkOption;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbIBMView() {
    }

    /**
     * Creates an instance of DbIBMView.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbIBMView(DbObject composite) throws DbException {
        super(composite);
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
     * Sets the "is federated" property of a DbIBMView's instance.
     * 
     * @param value
     *            the "is federated" property
     **/
    public final void setIsFederated(IBMFederatedOption value) throws DbException {
        basicSet(fIsFederated, value);
    }

    /**
     * Sets the "check option" property of a DbIBMView's instance.
     * 
     * @param value
     *            the "check option" property
     **/
    public final void setCheckOption(IBMViewCheckOption value) throws DbException {
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
     * Gets the "is federated" of a DbIBMView's instance.
     * 
     * @return the "is federated"
     **/
    public final IBMFederatedOption getIsFederated() throws DbException {
        return (IBMFederatedOption) get(fIsFederated);
    }

    /**
     * Gets the "check option" of a DbIBMView's instance.
     * 
     * @return the "check option"
     **/
    public final IBMViewCheckOption getCheckOption() throws DbException {
        return (IBMViewCheckOption) get(fCheckOption);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
