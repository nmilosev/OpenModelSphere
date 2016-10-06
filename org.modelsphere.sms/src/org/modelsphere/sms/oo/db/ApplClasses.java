package org.modelsphere.sms.oo.db;

import org.modelsphere.jack.baseDb.meta.MetaClass;

public abstract class ApplClasses {
    private static MetaClass[] finalClasses = { DbOOAdtGo.metaClass, DbOODiagram.metaClass,
            DbOOStyle.metaClass, DbOOAssociationGo.metaClass };

    public static MetaClass[] getFinalClasses() {
        return finalClasses;
    }
}
