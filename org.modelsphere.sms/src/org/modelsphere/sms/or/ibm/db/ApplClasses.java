package org.modelsphere.sms.or.ibm.db;

import org.modelsphere.jack.baseDb.meta.MetaClass;

public abstract class ApplClasses {
    private static MetaClass[] finalClasses = { DbIBMTable.metaClass, DbIBMView.metaClass,
            DbIBMColumn.metaClass, DbIBMTrigger.metaClass, DbIBMProcedure.metaClass,
            DbIBMParameter.metaClass, DbIBMPrimaryUnique.metaClass, DbIBMForeign.metaClass,
            DbIBMCheck.metaClass, DbIBMIndex.metaClass, DbIBMDataModel.metaClass,
            DbIBMDatabase.metaClass, DbIBMOperationLibrary.metaClass, DbIBMTablespace.metaClass,
            DbIBMDbPartitionGroup.metaClass, DbIBMBufferPool.metaClass, DbIBMSequence.metaClass,
            DbIBMContainerClause.metaClass, DbIBMContainerItem.metaClass,
            DbIBMExceptClause.metaClass };

    public static MetaClass[] getFinalClasses() {
        return finalClasses;
    }
}
