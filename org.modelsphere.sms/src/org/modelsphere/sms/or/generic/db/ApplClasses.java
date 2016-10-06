package org.modelsphere.sms.or.generic.db;

import org.modelsphere.jack.baseDb.meta.MetaClass;

public abstract class ApplClasses {
    private static MetaClass[] finalClasses = { DbGETable.metaClass, DbGEView.metaClass,
            DbGEColumn.metaClass, DbGETrigger.metaClass, DbGEProcedure.metaClass,
            DbGEParameter.metaClass, DbGEPrimaryUnique.metaClass, DbGEForeign.metaClass,
            DbGECheck.metaClass, DbGEIndex.metaClass, DbGEDataModel.metaClass,
            DbGEDatabase.metaClass, DbGEOperationLibrary.metaClass };

    public static MetaClass[] getFinalClasses() {
        return finalClasses;
    }
}
