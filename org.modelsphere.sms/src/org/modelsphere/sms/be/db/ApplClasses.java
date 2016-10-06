package org.modelsphere.sms.be.db;

import org.modelsphere.jack.baseDb.meta.MetaClass;

public abstract class ApplClasses {
    private static MetaClass[] finalClasses = { DbBEUseCase.metaClass, DbBEActor.metaClass,
            DbBEStore.metaClass, DbBEFlow.metaClass, DbBEQualifier.metaClass,
            DbBEResource.metaClass, DbBEModel.metaClass, DbBEDiagram.metaClass,
            DbBEUseCaseGo.metaClass, DbBEStyle.metaClass, DbBEUseCaseResource.metaClass,
            DbBEUseCaseQualifier.metaClass, DbBEActorQualifier.metaClass,
            DbBEStoreQualifier.metaClass, DbBEFlowQualifier.metaClass, DbBEActorGo.metaClass,
            DbBEStoreGo.metaClass, DbBEFlowGo.metaClass, DbBENotation.metaClass,
            DbBESingleZoneDisplay.metaClass, DbBEContextGo.metaClass, DbBEContextCell.metaClass };

    public static MetaClass[] getFinalClasses() {
        return finalClasses;
    }
}
