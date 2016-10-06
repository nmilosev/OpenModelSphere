package org.modelsphere.sms.or.informix.db;

import org.modelsphere.jack.baseDb.meta.MetaClass;

public abstract class ApplClasses {
    private static MetaClass[] finalClasses = { DbINFTable.metaClass, DbINFView.metaClass,
            DbINFColumn.metaClass, DbINFParameter.metaClass, DbINFTrigger.metaClass,
            DbINFProcedure.metaClass, DbINFPrimaryUnique.metaClass, DbINFForeign.metaClass,
            DbINFCheck.metaClass, DbINFIndex.metaClass, DbINFDataModel.metaClass,
            DbINFDatabase.metaClass, DbINFOperationLibrary.metaClass, DbINFDbspace.metaClass,
            DbINFFragment.metaClass, DbINFSbspace.metaClass };

    public static MetaClass[] getFinalClasses() {
        return finalClasses;
    }
}
