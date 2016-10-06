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
package org.modelsphere.sms.or.ibm.db.util;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.sms.or.ibm.db.DbIBMBufferPool;
import org.modelsphere.sms.or.ibm.db.DbIBMCheck;
import org.modelsphere.sms.or.ibm.db.DbIBMColumn;
import org.modelsphere.sms.or.ibm.db.DbIBMContainerClause;
import org.modelsphere.sms.or.ibm.db.DbIBMContainerItem;
import org.modelsphere.sms.or.ibm.db.DbIBMDataModel;
import org.modelsphere.sms.or.ibm.db.DbIBMDatabase;
import org.modelsphere.sms.or.ibm.db.DbIBMDbPartitionGroup;
import org.modelsphere.sms.or.ibm.db.DbIBMExceptClause;
import org.modelsphere.sms.or.ibm.db.DbIBMForeign;
import org.modelsphere.sms.or.ibm.db.DbIBMIndex;
import org.modelsphere.sms.or.ibm.db.DbIBMOperationLibrary;
import org.modelsphere.sms.or.ibm.db.DbIBMParameter;
import org.modelsphere.sms.or.ibm.db.DbIBMPrimaryUnique;
import org.modelsphere.sms.or.ibm.db.DbIBMProcedure;
import org.modelsphere.sms.or.ibm.db.DbIBMSequence;
import org.modelsphere.sms.or.ibm.db.DbIBMTable;
import org.modelsphere.sms.or.ibm.db.DbIBMTablespace;
import org.modelsphere.sms.or.ibm.db.DbIBMTrigger;
import org.modelsphere.sms.or.ibm.db.DbIBMView;
import org.modelsphere.sms.or.ibm.db.srtypes.IBMFileOrDevice;
import org.modelsphere.sms.or.ibm.db.srtypes.IBMTablespaceManagedBy;
import org.modelsphere.sms.or.ibm.international.LocaleMgr;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class IBMUtility {

    //singleton
    private static IBMUtility g_singleInstance = null;

    private IBMUtility() {
    }

    public static IBMUtility getInstance() {
        if (g_singleInstance == null) {
            g_singleInstance = new IBMUtility();
        }

        return g_singleInstance;
    } //end getSingleInstance()

    public void setInitialValues(DbObject obj) throws DbException {
        if (obj instanceof DbIBMBufferPool) {
            setInitialValues((DbIBMBufferPool) obj);
        } else if (obj instanceof DbIBMCheck) {
            setInitialValues((DbIBMCheck) obj);
        } else if (obj instanceof DbIBMColumn) {
            setInitialValues((DbIBMColumn) obj);
        } else if (obj instanceof DbIBMContainerClause) {
            setInitialValues((DbIBMContainerClause) obj);
        } else if (obj instanceof DbIBMContainerItem) {
            setInitialValues((DbIBMContainerItem) obj);
        } else if (obj instanceof DbIBMDatabase) {
            setInitialValues((DbIBMDatabase) obj);
        } else if (obj instanceof DbIBMDataModel) {
            setInitialValues((DbIBMDataModel) obj);
        } else if (obj instanceof DbIBMDbPartitionGroup) {
            setInitialValues((DbIBMDbPartitionGroup) obj);
        } else if (obj instanceof DbIBMExceptClause) {
            setInitialValues((DbIBMExceptClause) obj);
        } else if (obj instanceof DbIBMForeign) {
            setInitialValues((DbIBMForeign) obj);
        } else if (obj instanceof DbIBMIndex) {
            setInitialValues((DbIBMIndex) obj);
        } else if (obj instanceof DbIBMOperationLibrary) {
            setInitialValues((DbIBMOperationLibrary) obj);
        } else if (obj instanceof DbIBMParameter) {
            setInitialValues((DbIBMParameter) obj);
        } else if (obj instanceof DbIBMPrimaryUnique) {
            setInitialValues((DbIBMPrimaryUnique) obj);
        } else if (obj instanceof DbIBMProcedure) {
            setInitialValues((DbIBMProcedure) obj);
        } else if (obj instanceof DbIBMSequence) {
            setInitialValues((DbIBMSequence) obj);
        } else if (obj instanceof DbIBMTable) {
            setInitialValues((DbIBMTable) obj);
        } else if (obj instanceof DbIBMTablespace) {
            setInitialValues((DbIBMTablespace) obj);
        } else if (obj instanceof DbIBMTrigger) {
            setInitialValues((DbIBMTrigger) obj);
        } else if (obj instanceof DbIBMView) {
            setInitialValues((DbIBMView) obj);
        } else {
            //throw exception!                   
        } //end if
    } //end setInitialValues()

    //***************************
    // private fields and methods
    //***************************

    //
    // Physical concepts
    //
    private void setInitialValues(DbIBMDbPartitionGroup pg) throws DbException {
        pg.setName(LocaleMgr.misc.getString("dbPartitionGroup"));
        pg.setDbPartitionNums("0");
    }

    private void setInitialValues(DbIBMBufferPool bp) throws DbException {
        bp.setName(LocaleMgr.misc.getString("bufferPool"));
        bp.setSize(new Integer(4096));
        bp.setBlockSize(new Integer(32));
        bp.setPageSize(new Integer(4096));
    }

    private void setInitialValues(DbIBMExceptClause ec) throws DbException {
        ec.setName(LocaleMgr.misc.getString("exceptOnDBClause"));
        ec.setFromPartition(new Integer(0));
        //ec.setToPartition(new Integer(99)); 
        ec.setNumberOfPages(new Integer(4096));
    }

    private void setInitialValues(DbIBMTablespace tabsp) throws DbException {
        IBMTablespaceManagedBy systemManaged = IBMTablespaceManagedBy.objectPossibleValues[IBMTablespaceManagedBy.SYSTEM];

        tabsp.setName(LocaleMgr.misc.getString("tablespace"));
        tabsp.setManagedBy(systemManaged);
        tabsp.setPageSize(new Integer(4096));
        tabsp.setExtentSize(new Integer(64));
        tabsp.setPrefetchSize(new Integer(32));
        tabsp.setOverhead(new Double(24.1));
        tabsp.setTransferRate(new Double(0.9));

        //add a container
        DbIBMContainerClause container = new DbIBMContainerClause(tabsp);
        setInitialValues(container);
    }

    private void setInitialValues(DbIBMContainerClause contCl) throws DbException {
        contCl.setName(LocaleMgr.misc.getString("containerClause"));
        contCl.setContainerString("'/dev/container $N'");
    }

    private void setInitialValues(DbIBMContainerItem contIt) throws DbException {
        IBMFileOrDevice file = IBMFileOrDevice.objectPossibleValues[IBMFileOrDevice.FILE];

        contIt.setName(LocaleMgr.misc.getString("containerItem"));
        contIt.setFileOrDevice(file);
        contIt.setContainerString("/dev/container $N");
        contIt.setNbOfPage(new Integer(4096));
    }

    //
    // Data concepts
    //

    private void setInitialValues(DbIBMTable tab) throws DbException {
        tab.setName(LocaleMgr.misc.getString("table"));
    }

    private void setInitialValues(DbIBMColumn col) throws DbException {
        col.setName(LocaleMgr.misc.getString("column"));
    }

    private void setInitialValues(DbIBMPrimaryUnique puk) throws DbException {
        puk.setName(LocaleMgr.misc.getString("primaryKey"));
    }

    private void setInitialValues(DbIBMForeign fk) throws DbException {
        fk.setName(LocaleMgr.misc.getString("foreignKey"));
    }

    private void setInitialValues(DbIBMCheck chk) throws DbException {
        chk.setName(LocaleMgr.misc.getString("check"));
    }

    private void setInitialValues(DbIBMIndex idx) throws DbException {
        idx.setName(LocaleMgr.misc.getString("index"));
    }

    private void setInitialValues(DbIBMTrigger trig) throws DbException {
        String body = getDefaultBody();

        trig.setName(LocaleMgr.misc.getString("trigger"));
        trig.setBody(body);
    }

    private void setInitialValues(DbIBMView view) throws DbException {
        view.setName(LocaleMgr.misc.getString("view"));
    }

    private void setInitialValues(DbIBMSequence seq) throws DbException {
        seq.setName(LocaleMgr.misc.getString("sequence"));
        seq.setStartWith(new Integer(1));
        seq.setIncrementBy(new Integer(1));
        seq.setMinValue(new Integer(1));
        seq.setMaxValue(new Integer(255));
        seq.setCache(new Integer(20));
    }

    //
    // Procedural concepts
    //
    private static final String LOOKUP_DESCRIPTION = "LookupDescription"; // NOT LOCALIZABLE renderer name

    private void setInitialValues(DbIBMProcedure proc) throws DbException {
        String body = getDefaultBody();
        proc.setName(LocaleMgr.misc.getString("procedure"));
        String rendererPluginName = DbIBMProcedure.fPredicate.getRendererPluginName();

        if (rendererPluginName == null) {
            DbIBMProcedure.fPredicate.setRendererPluginName(LOOKUP_DESCRIPTION);
        }

        proc.setBody(body);
    } //end setInitialValues()

    private void setInitialValues(DbIBMParameter param) throws DbException {
        param.setName(LocaleMgr.misc.getString("parameter"));
    }

    //
    // Aggregate concepts
    //  

    private void setInitialValues(DbIBMDatabase db) throws DbException {
        db.setName(LocaleMgr.misc.getString("database"));
    }

    private void setInitialValues(DbIBMDataModel dm) throws DbException {
        dm.setName(LocaleMgr.misc.getString("ibmDataModel"));
    }

    private void setInitialValues(DbIBMOperationLibrary ol) throws DbException {
        ol.setName(LocaleMgr.misc.getString("ibmOperLib"));
    }

    //
    // Other private methods
    //
    private String getDefaultBody() {
        String body = "BEGIN\n" + //NOT LOCALIZABLE
                "-- Insert your SQL statements here\n" + //NOT LOCALIZABLE
                "END"; //NOT LOCALIZABLE

        return body;
    } //end getDefaultBody(); 

} //end IBMUtility
