package org.modelsphere.sms.oo.java.db;

import org.modelsphere.jack.baseDb.meta.MetaClass;

public abstract class ApplClasses {
    private static MetaClass[] finalClasses = { DbJVPrimitiveType.metaClass, DbJVClass.metaClass,
            DbJVDataMember.metaClass, DbJVMethod.metaClass, DbJVAssociationEnd.metaClass,
            DbJVInheritance.metaClass, DbJVAssociation.metaClass, DbJVConstructor.metaClass,
            DbJVParameter.metaClass, DbJVCompilationUnit.metaClass, DbJVImport.metaClass,
            DbJVPackage.metaClass, DbJVInitBlock.metaClass, DbJVClassModel.metaClass };

    public static MetaClass[] getFinalClasses() {
        return finalClasses;
    }
}
