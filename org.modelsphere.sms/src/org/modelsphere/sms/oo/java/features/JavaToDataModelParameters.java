package org.modelsphere.sms.oo.java.features;

import java.util.HashMap;
import java.util.Map;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.sms.db.DbSMSBuiltInTypeNode;
import org.modelsphere.sms.db.DbSMSBuiltInTypePackage;
import org.modelsphere.sms.db.DbSMSLinkModel;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVPrimitiveType;
import org.modelsphere.sms.or.db.DbORBuiltInType;

public class JavaToDataModelParameters {
    public DbJVClassModel classModel;

    //target system
    private DbSMSTargetSystem targetSystem;

    public DbSMSTargetSystem getTargetSystem() {
        return targetSystem;
    }

    public void setTargetSystem(DbSMSTargetSystem target) {
        targetSystem = target;
    }

    //associate long type for each target system
    Map<DbSMSTargetSystem, DbORBuiltInType> longTypes = new HashMap<DbSMSTargetSystem, DbORBuiltInType>();

    public void putLongType(DbSMSTargetSystem target) throws DbException {
        DbSMSBuiltInTypePackage pack = target.getBuiltInTypePackage();
        DbORBuiltInType longType = (DbORBuiltInType) pack.findComponentByName(
                DbORBuiltInType.metaClass, "LONG");
        if (longType == null) {
            longType = (DbORBuiltInType) pack.findComponentByName(DbORBuiltInType.metaClass,
                    "LONG INTEGER");
        }

        if (longType != null) {
            longTypes.put(target, longType);
        }
    }

    public DbORBuiltInType getLongType(DbSMSTargetSystem target) {
        return longTypes.get(target);
    }

    public boolean showOptions = false;

    //inheritance mapping
    public enum InheritanceStrategy {
        ONE_TABLE_PER_CLASS, ONE_TABLE_PER_CONCRETE_CLASS, ONE_TABLE_PER_INHERITANCE_TREE
    };

    public InheritanceStrategy stategy = InheritanceStrategy.ONE_TABLE_PER_CLASS;
    public String category = "subtype";

    //surrogate key
    public boolean generatePK = true;
    public String id = "id";
    public DbORBuiltInType identifierType;

    //semantic link
    public boolean createLinks = false;
    public DbSMSLinkModel linkModel;

    public boolean generateDiagrams = true;
    public boolean generateFK = false;
    public boolean generateReferentialActions = false;

    private static DbJVPrimitiveType intType = null;

    public static DbJVPrimitiveType getIntType(DbJVClassModel model) throws DbException {
        if (intType != null) {
            return intType;
        }

        intType = getPrimitiveType(model, "int");
        return intType;
    }

    private static DbJVPrimitiveType javaLong;

    public static DbJVPrimitiveType getLongType(DbJVClassModel model) throws DbException {
        if (javaLong != null) {
            return javaLong;
        }

        javaLong = getPrimitiveType(model, "long");
        return javaLong;
    }

    //
    // primitive type
    //
    private static DbJVPrimitiveType getPrimitiveType(DbJVClassModel model, String primitiveName)
            throws DbException {
        DbJVPrimitiveType primitive = null;
        DbSMSBuiltInTypePackage typePackage = getJavaBuiltinTypePackage(model);
        DbRelationN relN = typePackage.getComponents();
        DbEnumeration enu = relN.elements(DbJVPrimitiveType.metaClass);

        while (enu.hasMoreElements()) {
            DbJVPrimitiveType type = (DbJVPrimitiveType) enu.nextElement();
            String typename = type.getName();
            if (primitiveName.equals(typename)) {
                primitive = type;
                break;
            }
        } //end while
        enu.close();

        return primitive;
    }

    private static DbSMSBuiltInTypePackage javaBuiltinTypePackage = null;

    private static DbSMSBuiltInTypePackage getJavaBuiltinTypePackage(DbJVClassModel model)
            throws DbException {
        if (javaBuiltinTypePackage != null) {
            return javaBuiltinTypePackage;
        }

        DbSMSProject project = (DbSMSProject) model.getCompositeOfType(DbSMSProject.metaClass);
        DbRelationN relN = project.getComponents();
        DbEnumeration enu = relN.elements(DbSMSBuiltInTypeNode.metaClass);

        while (enu.hasMoreElements()) {
            DbSMSBuiltInTypeNode typeNode = (DbSMSBuiltInTypeNode) enu.nextElement();
            DbRelationN relN2 = typeNode.getComponents();
            DbEnumeration enu2 = relN2.elements(DbSMSBuiltInTypePackage.metaClass);

            while (enu2.hasMoreElements()) {
                DbSMSBuiltInTypePackage typePackage = (DbSMSBuiltInTypePackage) enu2.nextElement();
                String name = typePackage.getName();
                if (name.startsWith("Java")) {
                    javaBuiltinTypePackage = typePackage;
                    break;
                } //end if
            } //end while
            enu2.close();
        } //end while
        enu.close();

        return javaBuiltinTypePackage;
    }

}
