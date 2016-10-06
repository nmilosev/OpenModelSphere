package org.modelsphere.sms.or.oracle.db;

import org.modelsphere.jack.baseDb.meta.MetaClass;

public abstract class ApplClasses {
    private static MetaClass[] finalClasses = { DbORAView.metaClass, DbORAColumn.metaClass,
            DbORATrigger.metaClass, DbORAProcedure.metaClass, DbORAParameter.metaClass,
            DbORAPrimaryUnique.metaClass, DbORAForeign.metaClass, DbORACheck.metaClass,
            DbORAIndex.metaClass, DbORADataModel.metaClass, DbORAProgramUnitLibrary.metaClass,
            DbORADatabase.metaClass, DbORAPackage.metaClass, DbORATablespace.metaClass,
            DbORANestedTableStorage.metaClass, DbORATable.metaClass, DbORALobStorage.metaClass,
            DbORADataFile.metaClass, DbORARedoLogGroup.metaClass, DbORARollbackSegment.metaClass,
            DbORASequence.metaClass, DbORARedoLogFile.metaClass, DbORAPartition.metaClass,
            DbORASubPartition.metaClass };

    public static MetaClass[] getFinalClasses() {
        return finalClasses;
    }
}
