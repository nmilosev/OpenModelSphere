package org.modelsphere.sms.db;

import org.modelsphere.jack.baseDb.meta.MetaClass;

public abstract class ApplClasses {
    private static MetaClass[] finalClasses = { DbSMSProject.metaClass,
            DbSMSUserDefinedPackage.metaClass, DbSMSLink.metaClass, DbSMSImageGo.metaClass,
            DbSMSStampGo.metaClass, DbSMSInheritanceGo.metaClass, DbSMSUserTextGo.metaClass,
            DbSMSPackageGo.metaClass, DbSMSTargetSystem.metaClass, DbSMSFreeLineGo.metaClass,
            DbSMSFreeFormGo.metaClass, DbSMSBuiltInTypeNode.metaClass,
            DbSMSBuiltInTypePackage.metaClass, DbSMSCommonItemGo.metaClass,
            DbSMSLinkModel.metaClass, DbSMSUmlExtensibility.metaClass, DbSMSStereotype.metaClass,
            DbSMSUmlConstraint.metaClass, DbSMSObjectImport.metaClass, DbSMSLinkGo.metaClass,
            DbSMSNotice.metaClass, DbSMSNoticeGo.metaClass };

    public static MetaClass[] getFinalClasses() {
        return finalClasses;
    }
}
