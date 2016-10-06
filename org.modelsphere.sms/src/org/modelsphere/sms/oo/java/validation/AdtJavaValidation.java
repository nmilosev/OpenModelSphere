/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/
package org.modelsphere.sms.oo.java.validation;

import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Vector;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.SemanticalModel;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.oo.db.DbOOAbstractMethod;
import org.modelsphere.sms.oo.db.DbOOAdt;
import org.modelsphere.sms.oo.db.DbOOAssociationEnd;
import org.modelsphere.sms.oo.db.DbOOInheritance;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVCompilationUnit;
import org.modelsphere.sms.oo.java.db.DbJVConstructor;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.oo.java.db.DbJVParameter;
import org.modelsphere.sms.oo.java.db.srtypes.JVVisibility;
import org.modelsphere.sms.oo.java.db.util.AnyAdt;
import org.modelsphere.sms.oo.java.db.util.AnyAdt.AccessData;
import org.modelsphere.sms.oo.java.db.util.AnyAdt.Method;
import org.modelsphere.sms.oo.java.db.util.AnyAdt.MethodsWithSameSignature;
import org.modelsphere.sms.oo.java.international.LocaleMgr;

// do Java Validation for an adt
// note:  it's possible to optimize AdtJavaValidation code (for example, process
//        overriding or hiding methods in one shot) but messages will be less
//        "ordered" (for example, error messages will be not grouped by method)

public class AdtJavaValidation {

    private StringWriter writer;
    private DbJVClass adt;
    private boolean circularAdt;
    private int adtNbErrors = 0;
    private Vector adtFields = new Vector();
    private Vector adtOperations = new Vector();
    private Vector adtMethods = new Vector();
    private Vector adtConstructors = new Vector();
    private Vector adtNestedAdts = new Vector();
    private String adtInstanceType = "";
    private Vector absoluteMethods;

    private final static String INVALID_PART = LocaleMgr.message.getString("InvalidPart");
    private final static String INVALID_START = LocaleMgr.message.getString("InvalidStart");
    private final static String NAMELESS = LocaleMgr.message.getString("Nameless");
    private final static String RESERVED_WORD = LocaleMgr.message.getString("ReservedWord");

    public AdtJavaValidation(DbJVClass adtToValidate, boolean isCircularAdt,
            StringWriter errorsWriter) throws DbException {
        adt = adtToValidate;
        circularAdt = isCircularAdt;
        writer = errorsWriter;
        DbEnumeration e1 = adt.getComponents().elements();
        while (e1.hasMoreElements()) {
            Object obj = e1.nextElement();
            if (obj instanceof DbJVDataMember) {
                DbOOAssociationEnd assocEnd = ((DbJVDataMember) obj).getAssociationEnd();
                if (assocEnd == null || assocEnd.isNavigable())
                    adtFields.addElement(obj);
            } else if (obj instanceof DbOOAbstractMethod) {
                adtOperations.addElement(obj);
                if (obj instanceof DbJVConstructor) {
                    adtConstructors.addElement(obj);
                } else {
                    adtMethods.addElement(obj);
                }
            } else if (obj instanceof DbJVClass) {
                adtNestedAdts.addElement(obj);
            }
        }
        e1.close();
        SemanticalModel semanticalModel = ApplicationContext.getSemanticalModel();
        String classGuiName = semanticalModel.getClassDisplayText(adt, null);
        adtInstanceType = classGuiName.toLowerCase();
        absoluteMethods = AnyAdt.getAbsoluteMethods(adt);
        validate();
    }

    private void validate() throws DbException {

        //by default, no errors
        adt.setValidationStatus(DbObject.VALIDATION_OK);

        // validation errors
        validateAdtDeclaration();
        validateFields();
        validateOperations();
        validateConstructors();
        validateMethods();
        // toDo: validation warnings
    }

    // adt declaration validations
    private void validateAdtDeclaration() throws DbException {
        validateAdtName();
        validateCompilationUnitName();
        validateAdtVisibility();
        validateAdtStaticModifier();
        validateNestedAdt();
        validateFinalAndAbstractClass();
        validateAdtInheritance();
        validateComponentsDeclaration();
        validatePackage();
    }

    private void validatePackage() throws DbException {
        DbObject composite = adt.getComposite();
        if (composite instanceof DbJVPackage) {
            DbJVPackage javaPackage = (DbJVPackage) composite;
            String name = javaPackage.getName();
            validateIdentifierName(javaPackage, name);
        } //end if
    } //end validePackage()

    // adt name validations
    private void validateAdtName() throws DbException {
        String adtName = (adt.getName() != null ? adt.getName() : "");

        //Validate if adt has a valid Java identifier
        validateIdentifierName(adt, adtName);

        // validation: unicity of the adt name inside the containment hierarchy
        if (adt.getComposite() instanceof DbJVClass) {
            // validation: unicity of the nested/inner adt name inside the containment hierarchy
            Enumeration enclosingAdts = AnyAdt.getEnclosingAdts(adt).elements();
            while (enclosingAdts.hasMoreElements()) {
                DbJVClass enclosingAdt = (DbJVClass) enclosingAdts.nextElement();
                if ((adt != enclosingAdt) && (adtName.equals(enclosingAdt.getName()))) {
                    writeEnclosingAdtNameError(enclosingAdt);
                }
            }
            // validation: unicity of the nested/inner adt name inside nested/inner adts at the same level
            DbJVClass parentAdt = (DbJVClass) adt.getComposite();
            Vector sisterAdts = new Vector();
            DbEnumeration members = parentAdt.getComponents().elements(DbJVClass.metaClass);
            while (members.hasMoreElements()) {
                DbJVClass sister = (DbJVClass) members.nextElement();
                if (sister != adt) {
                    sisterAdts.addElement(sister);
                }
            }
            members.close();
            validateNestedAdtName(sisterAdts);
        }
        // validation: unicity of the adt name inside the same package
        else {
            DbObject adtContainer = adt.getComposite();
            DbEnumeration e1 = adtContainer.getComponents().elements(DbJVClass.metaClass);
            int nbDuplicates = 0;
            while (e1.hasMoreElements()) {
                DbJVClass otherAdt = (DbJVClass) e1.nextElement();
                if ((adt != otherAdt) && (adtName.equals(otherAdt.getName()))) {
                    nbDuplicates++;
                }
            }
            e1.close();
            if (nbDuplicates > 0) {
                writeAdtNameError(nbDuplicates);
            }
        }
    }

    private void validateCompilationUnitName() throws DbException {
        // validation: unicity of the compilation unit name inside the same package
        DbObject adtContainer = adt.getComposite();
        if (adtContainer instanceof DbSMSPackage && adt.getCompilationUnit() != null) {
            DbEnumeration e1 = adtContainer.getComponents().elements(DbJVClass.metaClass);
            int nbDuplicates = 0;
            DbJVCompilationUnit compil = adt.getCompilationUnit();
            String adtCompilUnitName = compil.getName();

            validateIdentifierName(compil, adtCompilUnitName);

            while (e1.hasMoreElements()) {
                DbJVClass otherAdt = (DbJVClass) (e1.nextElement());
                DbJVCompilationUnit otherCompil = otherAdt.getCompilationUnit();
                if (otherCompil != null && otherCompil != compil
                        && adtCompilUnitName.equals(otherCompil.getName())) {
                    nbDuplicates++;
                }
            }
            e1.close();
            if (nbDuplicates > 0) {
                writeCompilationUnitNameError(nbDuplicates);
            }
        }
    }

    ////////////////////////////////////////////////////////
    //Validate whether name is a valid Java identifier [MS]
    //
    private static final String[] reservedWords = new String[] { "abstract", "boolean", "break",
            "byte", //NOT LOCALIZABLE, Java keywords
            "case", "catch", "char", "class", "const", "continue", //NOT LOCALIZABLE, Java keywords
            "default", "do", "double", "else", "extends", //NOT LOCALIZABLE, Java keywords
            "false", "final", "finally", "float", "for", "goto", //NOT LOCALIZABLE, Java keywords
            "if", "implements", "import", "instanceof", "int", "interface", //NOT LOCALIZABLE, Java keywords
            "long", "native", "new", "null", //NOT LOCALIZABLE, Java keywords
            "package", "private", "protected", "public", //NOT LOCALIZABLE, Java keywords
            "return", "short", "static", "strictfp", "super", "switch", "synchronized", //NOT LOCALIZABLE, Java keywords
            "this", "throw", "throws", "transient", "true", "try", //NOT LOCALIZABLE, Java keywords
            "void", "volatile", "while" //NOT LOCALIZABLE, Java keywords
    };

    private void validateIdentifierName(DbSemanticalObject semObj, String name) throws DbException {

        //get hyperlink
        String hyperlink = JavaValidation.getHyperLinkSemanticalObject(semObj);

        //if name if null
        if ((name == null) || (name.length() == 0)) {
            String errorMsg = MessageFormat.format(NAMELESS, new Object[] { hyperlink });
            writeGenericError(errorMsg, semObj);
            return;
        } //end if

        //does start with a digit?
        char firstChar = name.charAt(0);
        if (!Character.isJavaIdentifierStart(firstChar)) {
            String errorMsg = MessageFormat.format(INVALID_START, new Object[] { hyperlink,
                    new Character(firstChar) });
            writeGenericError(errorMsg, semObj);
            return;
        } //end if

        //Is name a reserved word?
        boolean isReserved = false;
        for (int i = 0; i < reservedWords.length; i++) {
            if (name.equals(reservedWords[i])) {
                isReserved = true;
                break;
            }
        } //end for

        if (isReserved) {
            String errorMsg = MessageFormat.format(RESERVED_WORD, new Object[] { hyperlink, name });
            writeGenericError(errorMsg, semObj);
            return;
        } //end if

        //contains invalid character
        if (name.length() > 1) {
            for (int i = 1; i < name.length(); i++) {
                char ch = name.charAt(i);
                if (!Character.isJavaIdentifierPart(ch)) {
                    //a dot is a valid character in a compilation unit identifier (such as Applet.java)
                    if ((ch != '.') || !(semObj instanceof DbJVCompilationUnit)) {
                        String errorMsg = MessageFormat.format(INVALID_PART, new Object[] {
                                hyperlink, new Character(ch) });
                        writeGenericError(errorMsg, semObj);
                        return;
                    }
                } //end if
            } //end for
        } //end if
    } //end validateIdentifierName

    //
    //////////////////////////////////////////////////////////

    private void validateAdtVisibility() throws DbException {
        int visibility = adt.getVisibility().getValue();
        DbObject parent = adt.getComposite();
        // validation: a package adt cannot be declared private or protected
        if (parent instanceof DbSMSPackage) {
            if (visibility == JVVisibility.PRIVATE || visibility == JVVisibility.PROTECTED)
                writeAdtVisibilityError();
        }
        // validation: an interface member adt cannot be declared private, protected or package
        // note:  if any modifier is specified, in case of an interface member adt,
        //        interface adt access is public (and not package)
        else if (((DbJVClass) parent).isInterface()) {
            if (visibility == JVVisibility.PRIVATE || visibility == JVVisibility.PROTECTED)
                writeInterfaceNestedAdtVisibilityError();
        }
    }

    // validation: package adt cannot be declared static
    private void validateAdtStaticModifier() throws DbException {
        if (adt.getComposite() instanceof DbSMSPackage && adt.isStatic()) {
            writePackageStaticAdtError();
        }
    }

    // nested adt validation
    private void validateNestedAdt() throws DbException {
        if (AnyAdt.getCategory(adt) == AnyAdt.NESTED_ADT) {
            // validation: nested top-level class/interface cannot be declared within inner classes
            if (AnyAdt.getCategory((DbJVClass) adt.getComposite()) == AnyAdt.INNER_CLASS) {
                writeEnclosingInnerClassOfNestedAdtError();
            }
        }
    }

    // validation: a class cannot be final and abstract
    private void validateFinalAndAbstractClass() throws DbException {
        if (adt.isFinal() && adt.isAbstract()) {
            writeAbstractFinalClassError();
        }
    }

    // adt inheritance validations
    private void validateAdtInheritance() throws DbException {
        Vector directSuperClasses = new Vector();
        Vector directSuperInterfaces = new Vector();
        Vector invalidSuperInterfaces = new Vector();
        DbEnumeration e1 = adt.getSuperInheritances().elements();
        while (e1.hasMoreElements()) {
            DbJVClass adtSuper = (DbJVClass) ((DbOOInheritance) e1.nextElement()).getSuperClass();
            // validation: accessibility to superclass/superinterface
            if (!AnyAdt.isOtherAdtAccessible(adt, adtSuper)) {
                writeSuperAdtVisibilityError(adtSuper);
            }
            if (!adtSuper.isInterface()) { // adtSuper class or exception
                directSuperClasses.addElement(adtSuper);
                // validation: a superclass cannot be a final class
                if (adtSuper.isFinal()) {
                    writeFinalSuperClassError(adtSuper);
                }
                // validation: an interface cannot inherit from a superclass
                if (adt.isInterface()) {
                    writeInterfaceSuperClassError(adtSuper);
                }
                // validation: a class cannot inherit from one of its nested/inner class
                else {
                    if (adtSuper != adt && AnyAdt.getEnclosingAdts(adtSuper).contains(adt)) {
                        writeNestedSuperAdtError(adtSuper);
                    }
                }
            } else { //adtSuper interface
                if (directSuperInterfaces.contains(adtSuper)) {
                    if (!invalidSuperInterfaces.contains(adtSuper)) {
                        invalidSuperInterfaces.addElement(adtSuper);
                    }
                } else {
                    directSuperInterfaces.addElement(adtSuper);
                }
                // validation: an interface cannot inherit from one of its nested interface
                if (adt.isInterface()) {
                    if (adtSuper != adt && AnyAdt.getEnclosingAdts(adtSuper).contains(adt)) {
                        writeNestedSuperAdtError(adtSuper);
                    }
                }
            }
        } // end enumeration of adt ancestors
        e1.close();
        // validation: a class can only inherit from one direct superclass at a time
        if (directSuperClasses.size() > 1) {
            writeMoreOneSuperClassError(directSuperClasses);
        }
        // validation: a class/an interface cannot implement/extend
        //             the same direct superinterface more one time
        if (!invalidSuperInterfaces.isEmpty()) {
            writeMoreOneSameSuperInterfaceErrors(invalidSuperInterfaces);
        }
    }

    // validation: abstract supermethods implementation
    private void validateAbstractSuperMethodsImplementation() throws DbException {
        if (!circularAdt && !adt.isInterface() && !adt.isAbstract()) {
            for (Enumeration enumeration = absoluteMethods.elements(); enumeration
                    .hasMoreElements();) {
                MethodsWithSameSignature withSameSignature = (MethodsWithSameSignature) enumeration
                        .nextElement();
                validateAbstractSuperMethodsImplementation(withSameSignature);
            }
        }
    }

    private void validateAbstractSuperMethodsImplementation(
            MethodsWithSameSignature withSameSignature) throws DbException {
        Vector notOverriddenMethods = withSameSignature.getNotOverriddenNotHiddenMethods();
        for (Enumeration enumeration = notOverriddenMethods.elements(); enumeration
                .hasMoreElements();) {
            DbJVMethod notOverriddenMethod = ((Method) (enumeration.nextElement())).method;
            DbJVClass notOverriddenMethodAdt = (DbJVClass) notOverriddenMethod.getComposite();
            if (notOverriddenMethodAdt != adt
                    && (notOverriddenMethod.isAbstract() || notOverriddenMethodAdt.isInterface())) {
                writeAbstractSuperMethodNotImplementedError(notOverriddenMethod);
            }
        }

    }

    //adt components declaration validations
    private void validateComponentsDeclaration() throws DbException {
        validateFieldNameDuplicate();
        validateMemberNestedAdtNameDuplicate();
        if (!adt.isInterface()) { // class or exception
            validateOperationSignatureDuplicate(adtConstructors);
        }
        validateOperationSignatureDuplicate(adtMethods);
    }

    // validation: an adt cannot contain two fields with the same name
    private void validateFieldNameDuplicate() throws DbException {
        Vector validatedFields = new Vector();
        for (Enumeration enumerationFields = adtFields.elements(); enumerationFields
                .hasMoreElements();) {
            DbJVDataMember field = (DbJVDataMember) (enumerationFields.nextElement());
            if (!validatedFields.contains(field)) {
                validatedFields.addElement(field);
                String fieldName = (field.getName() != null ? field.getName() : "");
                int nbDuplicates = 0;
                for (Enumeration e = adtFields.elements(); e.hasMoreElements();) {
                    DbJVDataMember otherField = (DbJVDataMember) (e.nextElement());
                    if ((field != otherField) && (fieldName.equals(otherField.getName()))) {
                        nbDuplicates++;
                        validatedFields.addElement(otherField);
                    }
                }
                if (nbDuplicates > 0) {
                    writeFieldNameDuplicateError(field, nbDuplicates);
                }
            }
        }
    }

    // validation: an adt cannot contain two inner/nested adts at the same level with the same name
    private void validateMemberNestedAdtNameDuplicate() throws DbException {
        Vector validatedAdts = new Vector();
        for (Enumeration enumerationAdts = adtNestedAdts.elements(); enumerationAdts
                .hasMoreElements();) {
            DbJVClass nestedAdt = (DbJVClass) (enumerationAdts.nextElement());
            if (!validatedAdts.contains(nestedAdt)) {
                validatedAdts.addElement(nestedAdt);
                String nestedAdtName = nestedAdt.getName();
                int nbDuplicates = 0;
                for (Enumeration e = adtNestedAdts.elements(); e.hasMoreElements();) {
                    DbJVClass otherNestedAdt = (DbJVClass) (e.nextElement());
                    if ((nestedAdt != otherNestedAdt)
                            && (nestedAdtName.equals(otherNestedAdt.getName()))) {
                        nbDuplicates++;
                        validatedAdts.addElement(otherNestedAdt);
                    }
                }
                if (nbDuplicates > 0) {
                    writeNestedAdtNameDuplicateError(nestedAdt, nbDuplicates);
                }
            }
        }
    }

    // validation: an nested adt cannot contain a sister (at the same level ) inner/nested adt with the same name
    private void validateNestedAdtName(Vector sisterAdts) throws DbException {
        String adtName = adt.getName();
        int nbDuplicates = 0;
        for (Enumeration enumerationAdts = sisterAdts.elements(); enumerationAdts.hasMoreElements();) {
            DbJVClass nestedAdt = (DbJVClass) (enumerationAdts.nextElement());
            if ((nestedAdt != adt) && (adtName.equals(nestedAdt.getName()))) {
                nbDuplicates++;
            }
        }
        if (nbDuplicates > 0) {
            writeAdtNameError(nbDuplicates);
        }
    }

    // validation: an adt cannot contain two operations (two methods or two constructors)
    //             with the same signature
    private void validateOperationSignatureDuplicate(Vector operationList) throws DbException {
        Vector validatedOperations = new Vector();
        for (Enumeration enumerationOperations = operationList.elements(); enumerationOperations
                .hasMoreElements();) {
            DbOOAbstractMethod operation = (DbOOAbstractMethod) (enumerationOperations
                    .nextElement());
            if (!validatedOperations.contains(operation)) {
                validatedOperations.addElement(operation);
                int nbDuplicates = 0;
                for (Enumeration e = operationList.elements(); e.hasMoreElements();) {
                    DbOOAbstractMethod otherOperation = (DbOOAbstractMethod) (e.nextElement());
                    if (operation != otherOperation && operation.hasSameSignatureAs(otherOperation)) {
                        nbDuplicates++;
                        validatedOperations.addElement(otherOperation);
                    }
                }
                if (nbDuplicates > 0) {
                    writeOperationSignatureDuplicateError(operation, nbDuplicates);
                }
            }
        }
    }

    // fields validations
    private void validateFields() throws DbException {
        for (Enumeration enumerationFields = adtFields.elements(); enumerationFields
                .hasMoreElements();) {
            DbJVDataMember field = (DbJVDataMember) (enumerationFields.nextElement());
            field.setValidationStatus(DbObject.VALIDATION_OK);

            validateFieldType(field);
            String name = field.getName();
            validateIdentifierName(field, name);

            if (adt.isInterface()) {
                validateInterfaceField(field);
            } else {
                validateClassField(field);
            }
        }
    }

    // field type validations
    private void validateFieldType(DbJVDataMember field) throws DbException {
        // validation: field must be typed
        DbOOAdt type = field.getType();
        if (type == null) {
            writeNotTypedFieldError(field);
        }
        // validation: accessibility to type of field
        else if (type instanceof DbJVClass && !AnyAdt.isOtherAdtAccessible(adt, (DbJVClass) type)) {
            writeFieldTypeVisibilityError(field);
        }
    }

    // class field validations
    private void validateClassField(DbJVDataMember field) throws DbException {
        // validation: a field of a class cannot be declared final and volatile
        if (field.isFinal() && field.isVolatile()) {
            writeClassVolatileFinalFieldError(field);
        }
        // validation: a field of an inner class cannot be declared static
        if (field.isStatic() && AnyAdt.getCategory(adt) == AnyAdt.INNER_CLASS) {
            writeInnerClassStaticMemberError(field);
        }
    }

    // interface field validations
    private void validateInterfaceField(DbJVDataMember field) throws DbException {
        // note:  a field of an interface is always final and static.
        //        If field.isFinaL() is false, in case of an interface field,
        //        modifier "final" will be not generate.
        //        If field.isStatiC() is false, in case of an interface field,
        //        modifier "static" will be not generate.

        // validation: a field of an interface cannot be declared private, protected or package
        // note:  if any modifier is specified, in case of an interface field,
        //        interface field access is public (and not package)
        if ((field.getVisibility().getValue() == JVVisibility.PRIVATE)
                || (field.getVisibility().getValue() == JVVisibility.PROTECTED)) {
            writeInterfaceFieldVisibilityError(field);
        }
        // validation: a field of an interface cannot be declared transient
        if (field.isTransient()) {
            writeInterfaceTransientFieldError(field);
        }
        // validation: a (final) field of an interface cannot be declared volatile
        if (field.isVolatile()) {
            writeInterfaceVolatileFieldError(field);
        }
    }

    //operations validations
    private void validateOperations() throws DbException {
        for (Enumeration enumerationOperations = adtOperations.elements(); enumerationOperations
                .hasMoreElements();) {
            DbOOAbstractMethod operation = (DbOOAbstractMethod) (enumerationOperations
                    .nextElement());
            operation.setValidationStatus(DbObject.VALIDATION_OK);

            if (!(operation instanceof DbJVConstructor && adt.isInterface())) {
                validateOperationParameters(operation);
            }
            validateOperationExceptionsVisibility(operation);

        }
    }

    /*** method parameters validations ***/
    private void validateOperationParameters(DbOOAbstractMethod operation) throws DbException {
        Vector validatedParameters = new Vector();
        DbEnumeration params = operation.getComponents().elements(DbJVParameter.metaClass);
        while (params.hasMoreElements()) {
            DbJVParameter param = (DbJVParameter) params.nextElement();

            // validation: parameter must be typed
            DbOOAdt type = param.getType();
            if (type == null) {
                writeNotTypedParameterError(param);
            }
            // validation: accessibility to type of parameter
            else if (type instanceof DbJVClass
                    && !AnyAdt.isOtherAdtAccessible(adt, (DbJVClass) type)) {
                writeParameterTypeVisibilityError(param);
            }
            // validation: two parameters of an adt method cannot have the same name
            if (!validatedParameters.contains(param)) {
                validatedParameters.addElement(param);
                String paramName = param.getName();
                DbEnumeration enumParams = operation.getComponents().elements(
                        DbJVParameter.metaClass);
                while (enumParams.hasMoreElements()) {
                    DbJVParameter otherParam = (DbJVParameter) enumParams.nextElement();
                    if ((param != otherParam) && (paramName.equals(otherParam.getName()))) {
                        writeParamNameDuplicateError(otherParam);
                        validatedParameters.addElement(otherParam);
                    }
                }
                enumParams.close();
            }
        }
        params.close();
    }

    private void validateConstructors() throws DbException {

        //for each constructor's parameter
        for (Enumeration enumConstructors = adtConstructors.elements(); enumConstructors
                .hasMoreElements();) {
            DbJVConstructor constructor = (DbJVConstructor) (enumConstructors.nextElement());
            constructor.setValidationStatus(DbObject.VALIDATION_OK);

            DbEnumeration params = constructor.getComponents().elements(DbJVParameter.metaClass);
            while (params.hasMoreElements()) {
                DbJVParameter param = (DbJVParameter) params.nextElement();
                param.setValidationStatus(DbObject.VALIDATION_OK);
                String paramName = param.getName();
                validateIdentifierName(param, paramName);
            } //end while
            params.close();
        } //end for

        if (adt.isInterface()) {
            if (!adtConstructors.isEmpty())
                writeInterfaceConstructorError();
        } else { // class or exception
            if (adtConstructors.isEmpty()) {
                validateDefaultConstructor();
            } else {
                validateConstructorsExceptions();
                for (Enumeration enumConstructors = adtConstructors.elements(); enumConstructors
                        .hasMoreElements();) {
                    DbJVConstructor constructor = (DbJVConstructor) (enumConstructors.nextElement());
                    validateClassConstructor(constructor);
                }
            }
        }
    }

    private void validateDefaultConstructor() throws DbException {
        DbEnumeration ancestors = adt.getSuperInheritances().elements();
        while (ancestors.hasMoreElements()) {
            DbJVClass superAdt = (DbJVClass) ((DbOOInheritance) ancestors.nextElement())
                    .getSuperClass();
            if (!superAdt.isInterface() && !hasOneConstructorWithoutParameters(superAdt)) {
                writeDefaultConstructorError(superAdt);
            }
        }
        ancestors.close();
    }

    private boolean hasOneConstructorWithoutParameters(DbJVClass cl) throws DbException {
        DbEnumeration constructors = cl.getComponents().elements(DbJVConstructor.metaClass);
        boolean oneWithoutParam = true;
        while (constructors.hasMoreElements()) {
            DbJVConstructor constr = (DbJVConstructor) constructors.nextElement();
            oneWithoutParam = false;
            if (!constr.hasParameter()) {
                oneWithoutParam = true;
                break;
            }
        }
        constructors.close();
        return oneWithoutParam;
    }

    private void validateConstructorsExceptions() throws DbException {
        Vector constructorsWithoutException = new Vector();
        for (Enumeration enumConstructors = adtConstructors.elements(); enumConstructors
                .hasMoreElements();) {
            DbJVConstructor c = (DbJVConstructor) (enumConstructors.nextElement());
            if (c.getNbNeighbors(DbJVConstructor.fJavaExceptions) == 0) {
                constructorsWithoutException.addElement(c);
            }
        }
        if (!constructorsWithoutException.isEmpty()) {
            validateConstructorsWithoutException(constructorsWithoutException);
        }
    }

    private void validateOperationExceptionsVisibility(DbOOAbstractMethod operation)
            throws DbException {
        MetaRelationN fJavaExceptions = (operation instanceof DbJVMethod ? DbJVMethod.fJavaExceptions
                : DbJVConstructor.fJavaExceptions);
        DbEnumeration exceptions = ((DbRelationN) operation.get(fJavaExceptions)).elements();
        while (exceptions.hasMoreElements()) {
            DbJVClass javaException = (DbJVClass) (exceptions.nextElement());
            if (!AnyAdt.isOtherAdtAccessible(adt, javaException)) {
                writeOperationExceptionVisibilityError(operation, javaException);
            }
        }
        exceptions.close();
    }

    private void validateConstructorsWithoutException(Vector constructorsWithoutException)
            throws DbException {
        DbEnumeration ancestors = adt.getSuperInheritances().elements();
        while (ancestors.hasMoreElements()) {
            DbJVClass superAdt = (DbJVClass) ((DbOOInheritance) ancestors.nextElement())
                    .getSuperClass();
            if (!superAdt.isInterface() && !hasOneConstructorWithoutExceptions(superAdt)) {
                writeConstructorsWithoutExceptionError(superAdt, constructorsWithoutException);
            }
        }
        ancestors.close();
    }

    private boolean hasOneConstructorWithoutExceptions(DbJVClass cl) throws DbException {
        DbEnumeration constructors = cl.getComponents().elements(DbJVConstructor.metaClass);
        boolean oneWithoutExcept = false;
        boolean noConstructor = true;
        while (constructors.hasMoreElements() && !oneWithoutExcept) {
            DbJVConstructor constr = (DbJVConstructor) constructors.nextElement();
            noConstructor = false;
            oneWithoutExcept = (constr.getNbNeighbors(DbJVConstructor.fJavaExceptions) == 0);
        }
        constructors.close();
        if (noConstructor) {
            oneWithoutExcept = true;
        }
        return oneWithoutExcept;
    }

    private void validateClassConstructor(DbJVConstructor constructor) throws DbException {
        validateConstructorName(constructor);
    }

    private void validateConstructorName(DbJVConstructor constructor) throws DbException {
        if (!adt.getName().equals(constructor.getName())) {
            writeConstructorNameError(constructor);
        }
    }

    private void validateMethods() throws DbException {
        for (Enumeration enumMethods = adtMethods.elements(); enumMethods.hasMoreElements();) {
            DbJVMethod method = (DbJVMethod) (enumMethods.nextElement());
            method.setValidationStatus(DbObject.VALIDATION_OK);

            String name = method.getName();
            validateIdentifierName(method, name);

            //for each method's parameter
            DbEnumeration params = method.getComponents().elements(DbJVParameter.metaClass);
            while (params.hasMoreElements()) {
                DbJVParameter param = (DbJVParameter) params.nextElement();
                param.setValidationStatus(DbObject.VALIDATION_OK);

                String paramName = param.getName();
                validateIdentifierName(param, paramName);
            } //end while
            params.close();

            validateMethodReturnType(method);
            if (adt.isInterface()) {
                validateInterfaceMethod(method);
            } else {
                validateClassMethod(method);
            }
            if (!circularAdt) {
                validateHidingAndOverridingMethod(method);
            }
        }
        validateIndirectHidingOverriding();
        validateAbstractSuperMethodsImplementation();
    }

    // method return type validations
    private void validateMethodReturnType(DbJVMethod method) throws DbException {
        DbOOAdt type = method.getReturnType();
        // validation: accessibility to return type of method
        if (type instanceof DbJVClass && !AnyAdt.isOtherAdtAccessible(adt, (DbJVClass) type)) {
            writeReturnTypeVisibilityError(method);
        }
    }

    // class method validations
    private void validateClassMethod(DbJVMethod method) throws DbException {
        // validation: a field of an inner class cannot be declared static
        if (method.isStatic() && AnyAdt.getCategory(adt) == AnyAdt.INNER_CLASS) {
            writeInnerClassStaticMemberError(method);
        }
        if (method.isAbstract()) {
            // validation: an abstract method must appear within an abstract class
            if (!adt.isAbstract()) {
                writeAbstractMethodOfNotAbstractClassError(method);
            }
            // validation: a class method cannot be declared abstract and private
            if (method.getVisibility().getValue() == JVVisibility.PRIVATE) {
                writeClassAbstractPrivateMethodError(method);
            }
            // validation: a class method cannot be declared abstract and final
            if (method.isFinal()) {
                writeClassAbstractFinalMethodError(method);
            }
            // validation: a class method cannot be declared abstract and static
            if (method.isStatic()) {
                writeClassAbstractStaticMethodError(method);
            }
            // validation: a class method cannot be declared abstract and synchronized
            if (method.isSynchronized()) {
                writeClassAbstractSynchronizeDMethodError(method);
            }
            // validation: a class method cannot be declared abstract and native
            if (method.isNative()) {
                writeClassAbstractNativEMethodError(method);
            }
            // validation: an abstract method cannot have body
            if ((method.getBody() != null) && (method.getBody().length()) > 0) { // toVerify: supprimer les espaces?? tester body == null ??
                writeClassAbstractOrNativeMethodWithBodyError(method);
            }
        } else if (method.isNative() && method.getBody() != null && method.getBody().length() > 0) { // toVerify: supprimer les espaces?? tester body == null ??
            writeClassAbstractOrNativeMethodWithBodyError(method);
        }
    }

    // interface method validations
    private void validateInterfaceMethod(DbJVMethod method) throws DbException {
        // note:  a method of an interface is always abstract.
        //        If method.isAbstracT() is false, in case of an interface method,
        //        modifier "abstract" will be not generate.

        // validation: a method of an interface cannot be declared private, protected or package
        // note:  if any modifier is specified, in case of an interface method,
        //        interface method access is public (and not package)
        if ((method.getVisibility().getValue() == JVVisibility.PRIVATE)
                || (method.getVisibility().getValue() == JVVisibility.PROTECTED)) {
            writeInterfaceMethodVisibilityError(method);
        }
        // validation: a (abstract) method of an interface cannot be declared final
        if (method.isFinal()) {
            writeInterfaceFinalMethodError(method);
        }
        // validation: a (abstract) method of an interface cannot be declared static
        if (method.isStatic()) {
            writeInterfaceStaticMethodError(method);
        }
        // validation: a (abstract) method of an interface cannot be declared synchronized
        if (method.isSynchronized()) {
            writeInterfaceSynchronizeDMethodError(method);
        }
        // validation: a (abstract) method of an interface cannot be declared native
        if (method.isNative()) {
            writeInterfaceNativEMethodError(method);
        }
        // validation: a (abstract) method of an interface cannot have body
        if ((method.getBody() != null) && (method.getBody().length() > 0)) { // toVerify: supprimer les espaces?? tester body == null ??
            writeInterfaceMethodWithBodyError(method);
        }
    }

    private void validateHidingAndOverridingMethod(DbJVMethod method) throws DbException {
        String signature = method.buildSignature(DbObject.LONG_FORM);
        MethodsWithSameSignature withSameSignature = null;
        for (Enumeration e1 = absoluteMethods.elements(); e1.hasMoreElements()
                && (withSameSignature == null);) {
            MethodsWithSameSignature elem1 = (MethodsWithSameSignature) e1.nextElement();
            if (signature.equals(elem1.signature)) {
                withSameSignature = elem1;
            }
        }
        if (withSameSignature != null) {
            Vector overriddenHiddenMethods = withSameSignature.getOverriddenHiddenMethodsBy(method);
            for (Enumeration e2 = overriddenHiddenMethods.elements(); e2.hasMoreElements();) {
                DbJVMethod superMethod = ((Method) e2.nextElement()).method;
                validateHidingAndOverriding(method, superMethod);
            }
        }
    }

    private void validateIndirectHidingOverriding() throws DbException {
        for (Enumeration e1 = absoluteMethods.elements(); e1.hasMoreElements();) {
            MethodsWithSameSignature withSameSignature = (MethodsWithSameSignature) e1
                    .nextElement();
            validateIndirectHidingOverriding(withSameSignature);
        }
    }

    private void validateIndirectHidingOverriding(MethodsWithSameSignature withSameSignature)
            throws DbException {
        for (Enumeration e1 = withSameSignature.getNotOverriddenNotHiddenMethods().elements(); e1
                .hasMoreElements();) {
            DbJVMethod meth = ((Method) e1.nextElement()).method;
            Vector overriddenHiddenMethods = withSameSignature.getOverriddenHiddenMethodsBy(meth);
            for (Enumeration e2 = overriddenHiddenMethods.elements(); e2.hasMoreElements();) {
                Method elem2 = (Method) e2.nextElement();
                if (!(elem2.directOverriding)) {
                    DbJVMethod superMethod = elem2.method;
                    validateHidingAndOverriding(meth, superMethod);
                }
            }
        }
    }

    private void validateHidingAndOverriding(DbJVMethod method, DbJVMethod superMethod)
            throws DbException {
        validateReturnTypeOverridingOrHidingMethod(method, superMethod);
        validateVisibilityOverridingOrHidingMethod(method, superMethod);
        validateNotFinalOverridenOrHiddenMethod(method, superMethod);
        validateStaticHiddenMethod(method, superMethod);
        validateNotStaticOverridenMethod(method, superMethod);
        validateExceptionOverridingOrHidingMethod(method, superMethod);
    }

    // validation: an overriding or hiding method must have the same return type
    //             than overridden method
    private void validateReturnTypeOverridingOrHidingMethod(DbJVMethod method,
            DbJVMethod superMethod) throws DbException {
        if (method.getReturnType() != superMethod.getReturnType()) {
            writeReturnTypeOverridingOrHidingMethodError(method, superMethod);
        }
    }

    // validation: a method cannot override or hide a method with weaker access privilege
    private void validateVisibilityOverridingOrHidingMethod(DbJVMethod method,
            DbJVMethod superMethod) throws DbException {
        DbJVClass ancestor = (DbJVClass) (superMethod.getComposite());
        int visibility = method.getVisibility().getValue();
        int superVisibility = superMethod.getVisibility().getValue();
        if (ancestor.isInterface()) {
            if (adt.isInterface()) {
                if (visibility != JVVisibility.PUBLIC && visibility != JVVisibility.DEFAULT)
                    writeVisibilityOverridingOrHidingMethodError(method, superMethod);
            } else {
                if (visibility != JVVisibility.PUBLIC)
                    writeVisibilityOverridingOrHidingMethodError(method, superMethod);
            }
        } else {
            if ((superVisibility == JVVisibility.PUBLIC) && (visibility != JVVisibility.PUBLIC)) {
                writeVisibilityOverridingOrHidingMethodError(method, superMethod);
            } else if ((superVisibility == JVVisibility.PROTECTED)
                    && (visibility != JVVisibility.PUBLIC)
                    && (visibility != JVVisibility.PROTECTED)) {
                writeVisibilityOverridingOrHidingMethodError(method, superMethod);
            } else if ((superVisibility == JVVisibility.DEFAULT)
                    && (visibility == JVVisibility.PRIVATE)) {
                writeVisibilityOverridingOrHidingMethodError(method, superMethod);
            }
        }
    }

    // validation: a final method of a class cannot be hidden or overriden
    private void validateNotFinalOverridenOrHiddenMethod(DbJVMethod method, DbJVMethod superMethod)
            throws DbException {
        DbJVClass ancestor = (DbJVClass) (superMethod.getComposite());
        if (!ancestor.isInterface() && superMethod.isFinal()) {
            writeFinalOverriddenOrHiddenMethodError(method, superMethod);
        }
    }

    // validation: a static method of a class cannot hide an instance method
    private void validateStaticHiddenMethod(DbJVMethod method, DbJVMethod superMethod)
            throws DbException {
        if (method.isStatic() && !adt.isInterface() && !superMethod.isStatic()) {
            writeNotStaticHiddenMethodError(method, superMethod);
        }
    }

    // validation: a instance method cannot override a static method of a class
    private void validateNotStaticOverridenMethod(DbJVMethod method, DbJVMethod superMethod)
            throws DbException {
        DbJVClass ancestor = (DbJVClass) (superMethod.getComposite());
        if (!method.isStatic() && !ancestor.isInterface() && superMethod.isStatic()) {
            writeStaticOverriddenMethodError(method, superMethod);
        }
    }

    // validation: an overriding or hiding method may not be declared to throw
    //             more checked exceptions than overridden or hidden method
    private void validateExceptionOverridingOrHidingMethod(DbJVMethod method, DbJVMethod superMethod)
            throws DbException {
        DbEnumeration exceptions = method.getJavaExceptions().elements();
        while (exceptions.hasMoreElements()) {
            DbJVClass exception = (DbJVClass) exceptions.nextElement();
            if (!isExceptionChecked(exception, superMethod)) {
                writeExceptionOverridingOrHidingMethodError(method, superMethod, exception);
            }
        }
        exceptions.close();
    }

    private boolean isExceptionChecked(DbJVClass exception, DbJVMethod method) throws DbException {
        boolean checked = false;
        Vector exceptionAncestors = (Vector) AnyAdt.findAncestors(exception, new Vector());
        DbEnumeration exceptions = method.getJavaExceptions().elements();
        while (exceptions.hasMoreElements() && !checked) {
            DbJVClass otherException = (DbJVClass) exceptions.nextElement();
            if (exceptionAncestors.contains(otherException)) {
                checked = true;
            }
        }
        exceptions.close();
        return checked;
    }

    private void writeErrorNb(int errorNb) {
        writer.write(MessageFormat.format(LocaleMgr.validation.getString("ErrorNbr0"),
                new Object[] { new Integer(errorNb) }));
    }

    private void writeAdtErrorTitle() throws DbException {
        String pattern = LocaleMgr.validation.getString("AdtErrorTitle");
        writer.write(MessageFormat.format(pattern, new Object[] { adtInstanceType,
                JavaValidation.getHyperLinkSemanticalObject(adt) }));
    }

    private void writeAdtNameError(int nbDuplicates) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(adt);

        DbObject parent = adt.getComposite();
        String containerString = ApplicationContext.getSemanticalModel().getClassDisplayText(
                parent, null).toLowerCase();
        if (parent instanceof DbJVClass)
            containerString = containerString + LocaleMgr.validation.getString("AtSameLevel");
        String pattern;
        if (nbDuplicates > 1) {
            if (adt.isInterface())
                pattern = LocaleMgr.validation.getString("Nb0DupAttr1NamesAttr1");
            else
                pattern = LocaleMgr.validation.getString("Nb0DupClas1NamesClas1");
        } else {
            if (adt.isInterface())
                pattern = LocaleMgr.validation.getString("Nb0DupAttr1NameAttr1");
            else
                pattern = LocaleMgr.validation.getString("Nb0DupClas1NameClas1");
        }
        writer.write(MessageFormat.format(pattern, new Object[] { new Integer(nbDuplicates),
                adtInstanceType }));
        pattern = LocaleMgr.validation.getString("CannotSameNameClassInSameContainer0");
        writer.write(MessageFormat.format(pattern, new Object[] { containerString }));
    }

    private void writeCompilationUnitNameError(int nbDuplicates) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(adt);
        String containerString = ApplicationContext.getSemanticalModel().getClassDisplayText(
                adt.getComposite(), null).toLowerCase();

        String pattern;
        if (nbDuplicates > 1) {
            if (adt.isInterface())
                pattern = LocaleMgr.validation.getString("AnXxx0DupCompilNamesXxx1_Xxx2");
            else
                pattern = LocaleMgr.validation.getString("AXxx0DupCompilNamesXxx1_Xxx2");
        } else {
            if (adt.isInterface())
                pattern = LocaleMgr.validation.getString("AnXxx0DupCompilNameXxx1_Xxx2");
            else
                pattern = LocaleMgr.validation.getString("AXxx0DupCompilNameXxx1_Xxx2");
        }
        writer.write(MessageFormat.format(pattern, new Object[] {
                new Integer(nbDuplicates),
                JavaValidation.getHyperLinkSemanticalObject(adt.getCompilationUnit(),
                        DbObject.SHORT_FORM), adtInstanceType }));
        pattern = LocaleMgr.validation.getString("CannotSameNameCompilInSameContainer0");
        writer.write(MessageFormat.format(pattern, new Object[] { containerString }));
    }

    private void writeEnclosingAdtNameError(DbJVClass enclosingAdt) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(adt);
        String pattern = LocaleMgr.validation
                .getString("Adt0CannotSameNameAsEnclosingClass1Encladt2");
        writer.write(MessageFormat.format(pattern, new Object[] {
                adtInstanceType,
                ApplicationContext.getSemanticalModel().getClassDisplayText(enclosingAdt, null)
                        .toLowerCase(),
                JavaValidation.getHyperLinkSemanticalObject(enclosingAdt, AnyAdt
                        .getNameWithEnclosingAdts(enclosingAdt)) }));

    }

    private void writeAdtVisibilityError() throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(adt);
        String containerType = ApplicationContext.getSemanticalModel().getClassDisplayText(
                adt.getComposite(), null).toLowerCase();
        String pattern = containerType
                + LocaleMgr.validation.getString("MustBePublic_Class0CannotBeVis1");
        writer.write(MessageFormat.format(pattern, new Object[] { adtInstanceType,
                adt.getVisibility().toString() }));
    }

    private void writeInterfaceNestedAdtVisibilityError() throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(adt);
        String pattern = LocaleMgr.validation.getString("Class0CannotBeVis1");
        writer.write(MessageFormat.format(pattern, new Object[] { adtInstanceType,
                adt.getVisibility().toString() }));
        if (adt.isInterface())
            pattern = LocaleMgr.validation.getString("AnMethod0OfInterfaceIsPublic");
        else
            pattern = LocaleMgr.validation.getString("AMethod0OfInterfaceIsPublic");
        writer.write(MessageFormat.format(pattern, new Object[] { adtInstanceType }));
    }

    private void writePackageStaticAdtError() throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(adt);
        String containerType = ApplicationContext.getSemanticalModel().getClassDisplayText(
                adt.getComposite(), null).toLowerCase();
        String pattern = LocaleMgr.validation.getString("Package0Class1CannotBeStatic");
        writer
                .write(MessageFormat.format(pattern,
                        new Object[] { containerType, adtInstanceType }));
    }

    private void writeEnclosingInnerClassOfNestedAdtError() throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(adt);
        String pattern = LocaleMgr.validation.getString("NestedTopClass0CannotBeInInnerXxx1");
        DbJVClass innerEnclosingAdt = (DbJVClass) (adt.getComposite());
        writer.write(MessageFormat.format(pattern, new Object[] {
                adtInstanceType,
                JavaValidation.getHyperLinkSemanticalObject(innerEnclosingAdt, AnyAdt
                        .getNameWithEnclosingAdts(innerEnclosingAdt)) }));
    }

    private void writeAbstractFinalClassError() throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(adt);
        writer.write(LocaleMgr.validation.getString("ClassCannotBeFinalAbstract"));
    }

    private void writeSuperAdtVisibilityError(DbJVClass adtSuper) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(adt);
        String pattern = LocaleMgr.validation.getString("CannotInheritFromClass0Xxx1VisErr2");
        writer.write(MessageFormat.format(pattern, new Object[] {
                ApplicationContext.getSemanticalModel().getClassDisplayText(adtSuper, null)
                        .toLowerCase(), JavaValidation.getHyperLinkSemanticalObject(adtSuper),
                getAccessibilityErrorMessage(adtSuper) }));
    }

    private String getAccessibilityErrorMessage(DbJVClass adtToAccess) throws DbException {
        // ex1: "class A cannot be accessed: it is declared "package-visible" in package X."
        // ex2: "class A.B.C cannot be accessed: enclosing class A.B is declared private."
        String pattern = LocaleMgr.validation.getString("Class0Encladt1CannotBeAccessed");

        String header = MessageFormat.format(pattern, new Object[] {
                ApplicationContext.getSemanticalModel().getClassDisplayText(adtToAccess, null),
                AnyAdt.getNameWithEnclosingAdts(adtToAccess) });

        String buildPhrase;
        if (adtToAccess.getComposite() instanceof DbJVClass) {
            AccessData accessData = AnyAdt.getAccessData(adtToAccess);
            if (accessData.adt != adtToAccess) {
                adtToAccess = accessData.adt;
                pattern = LocaleMgr.validation.getString("EnclosingClass0Encladt1");
                buildPhrase = MessageFormat
                        .format(pattern, new Object[] {
                                ApplicationContext.getSemanticalModel().getClassDisplayText(
                                        adtToAccess, null).toLowerCase(),
                                AnyAdt.getNameWithEnclosingAdts(adtToAccess) });
            } else {
                buildPhrase = LocaleMgr.validation.getString("It");
            }
        } else {
            buildPhrase = LocaleMgr.validation.getString("It");
        }
        JVVisibility accessVisibility = adtToAccess.getVisibility();
        String visibility;
        if (accessVisibility.getValue() == JVVisibility.DEFAULT) {
            visibility = LocaleMgr.validation.getString("PackageVisible");
        } else {
            visibility = accessVisibility.toString();
        }
        String where = "";
        DbSemanticalObject adtToAccessContainer = (DbSemanticalObject) adtToAccess
                .getCompositeOfType(DbSMSPackage.metaClass);
        if (adt.getCompositeOfType(DbSMSPackage.metaClass) != adtToAccessContainer) {
            pattern = LocaleMgr.validation.getString("InPackageXxx0");
            where = MessageFormat.format(pattern, new Object[] { JavaValidation
                    .getHyperLinkSemanticalObject(adtToAccessContainer, DbObject.SHORT_FORM) });
        }
        pattern = LocaleMgr.validation.getString("Xxx0Xxx1IsDeclaredVis2Where3");
        return MessageFormat.format(pattern,
                new Object[] { header, buildPhrase, visibility, where });
    }

    private String getInheritedFrom(DbJVClass methodAdt) throws DbException {
        String pattern = LocaleMgr.validation.getString("InheritedFromMethod0Xxx1");
        return MessageFormat.format(pattern, new Object[] {
                ApplicationContext.getSemanticalModel().getClassDisplayText(methodAdt, null)
                        .toLowerCase(), JavaValidation.getHyperLinkSemanticalObject(methodAdt) });
    }

    //Generic method to write an error [MS]
    private void writeGenericError(String errorMsg, DbSemanticalObject semObj) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(adt);
        writer.write(errorMsg);

        semObj.setValidationStatus(DbObject.VALIDATION_ERROR);
    }

    private void writeFinalSuperClassError(DbJVClass adtSuper) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(adt);
        String pattern = LocaleMgr.validation.getString("CannotExtendClassXxx0");
        writer.write(MessageFormat.format(pattern, new Object[] {
                JavaValidation.getHyperLinkSemanticalObject(adtSuper),
                AnyAdt.getNameWithEnclosingAdts(adtSuper) }));
    }

    private void writeInterfaceSuperClassError(DbJVClass adtSuper) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(adt);
        String pattern = LocaleMgr.validation.getString("CannotInheritClassXxx0");
        writer.write(MessageFormat.format(pattern, new Object[] { JavaValidation
                .getHyperLinkSemanticalObject(adtSuper) }));
    }

    private void writeNestedSuperAdtError(DbJVClass adtSuper) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(adt);
        String pattern;
        if (adt.isInterface()) {
            pattern = LocaleMgr.validation.getString("AnCyclicInheritanceInvolvingXxx0_Xxx1_Xxx2");
        } else {
            pattern = LocaleMgr.validation.getString("ACyclicInheritanceInvolvingXxx0_Xxx1_Xxx2");
        }
        writer.write(MessageFormat.format(pattern, new Object[] {
                ApplicationContext.getSemanticalModel().getClassDisplayText(adtSuper, null)
                        .toLowerCase(),
                JavaValidation.getHyperLinkSemanticalObject(adtSuper, AnyAdt
                        .getNameWithEnclosingAdts(adtSuper)), adtInstanceType }));

        if (AnyAdt.getCategory(adtSuper) == AnyAdt.INNER_CLASS) {
            pattern = LocaleMgr.validation.getString("CannotInheritFromInnerClassXxx0");
        } else { // == AnyAdt.NESTED_ADT
            pattern = LocaleMgr.validation.getString("CannotInheritFromNestedAdtXxx0");
        }
        writer.write(MessageFormat.format(pattern, new Object[] { ApplicationContext
                .getSemanticalModel().getClassDisplayText(adtSuper, null).toLowerCase() }));
    }

    private void writeMoreOneSuperClassError(Vector directSuperClasses) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(adt);
        writer.write(LocaleMgr.validation.getString("CannotInheritMoreOneSuperclass"));
        boolean first = true;
        for (Enumeration e = directSuperClasses.elements(); e.hasMoreElements();) {
            if (!first) {
                writer.write(", ");
            }
            writer.write(JavaValidation.getHyperLinkSemanticalObject((DbJVClass) e.nextElement()));
            first = false;
        }
        writer.write(LocaleMgr.validation.getString("CannotBeAllTogetherSuperclasses"));
    }

    private void writeMoreOneSameSuperInterfaceErrors(Vector invalidSuperInterfaces)
            throws DbException {
        for (Enumeration e = invalidSuperInterfaces.elements(); e.hasMoreElements();) {
            if (adtNbErrors == 0) {
                writeAdtErrorTitle();
            }

            reportError(adt);
            String heritMode;
            if (adt.isInterface()) {
                heritMode = LocaleMgr.validation.getString("Extend");
            } else {
                heritMode = LocaleMgr.validation.getString("Implement");
            }
            String pattern = LocaleMgr.validation
                    .getString("CannotImplement0DirectlyInterfaceXxx1MoreOneTime");
            writer.write(MessageFormat.format(pattern, new Object[] { heritMode,
                    JavaValidation.getHyperLinkSemanticalObject((DbJVClass) e.nextElement()) }));
        }
    }

    private void writeFieldNameDuplicateError(DbJVDataMember field, int nbDuplicates)
            throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(field);

        String pattern;
        if (nbDuplicates > 1) {
            if (adt.isInterface())
                pattern = LocaleMgr.validation.getString("AnXxx0DupFNamesXxx1_Xxx2");
            else
                pattern = LocaleMgr.validation.getString("AXxx0DupFNamesXxx1_Xxx2");
        } else {
            if (adt.isInterface())
                pattern = LocaleMgr.validation.getString("AnXxx0DupFNameXxx1_Xxx2");
            else
                pattern = LocaleMgr.validation.getString("AXxx0DupFNameXxx1_Xxx2");
        }
        writer.write(MessageFormat.format(pattern, new Object[] { new Integer(nbDuplicates),
                JavaValidation.getHyperLinkSemanticalObject(field, DbObject.SHORT_FORM),
                adtInstanceType }));
        writer.write(LocaleMgr.validation.getString("CannotHaveTwoFieldsSameName"));
    }

    private void writeNestedAdtNameDuplicateError(DbJVClass nestedAdt, int nbDuplicates)
            throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(nestedAdt);
        String pattern;
        if (nbDuplicates > 1) {
            if (adt.isInterface())
                pattern = LocaleMgr.validation.getString("AnXxx0DupCINamesXxx1_Xxx2");
            else
                pattern = LocaleMgr.validation.getString("AXxx0DupCINamesXxx1_Xxx2");
        } else {
            if (adt.isInterface())
                pattern = LocaleMgr.validation.getString("AnXxx0DupCINameXxx1_Xxx2");
            else
                pattern = LocaleMgr.validation.getString("AXxx0DupCINameXxx1_Xxx2");
        }
        writer.write(MessageFormat.format(pattern, new Object[] { new Integer(nbDuplicates),
                JavaValidation.getHyperLinkSemanticalObject(nestedAdt, DbObject.SHORT_FORM),
                adtInstanceType }));
        writer.write(LocaleMgr.validation.getString("CannotHaveTwoMemberCISameName"));
    }

    private void writeOperationSignatureDuplicateError(DbOOAbstractMethod operation,
            int nbDuplicates) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(operation);

        String pattern;
        if (nbDuplicates > 1) {
            if (adt.isInterface())
                pattern = LocaleMgr.validation.getString("AnXxx0DupMethods1SigXxx2_Xxx3");
            else
                pattern = LocaleMgr.validation.getString("AXxx0DupMethods1SigXxx2_Xxx3");
        } else {
            if (adt.isInterface())
                pattern = LocaleMgr.validation.getString("AnXxx0DupMethod1SigXxx2_Xxx3");
            else
                pattern = LocaleMgr.validation.getString("AXxx0DupMethod1SigXxx2_Xxx3");
        }

        SemanticalModel semanticalModel = ApplicationContext.getSemanticalModel();
        String classGUIName = semanticalModel.getClassDisplayText(operation, null);
        String operationInstanceType = classGUIName.toLowerCase();
        writer.write(MessageFormat.format(pattern, new Object[] { new Integer(nbDuplicates),
                operationInstanceType, JavaValidation.getHyperLinkSemanticalObject(operation),
                adtInstanceType }));
        pattern = LocaleMgr.validation.getString("CannotHaveTwoMethod0sSameSignature");
        writer.write(MessageFormat.format(pattern, new Object[] { operationInstanceType }));
    }

    private void writeNotTypedFieldError(DbJVDataMember field) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(field);
        String pattern = LocaleMgr.validation.getString("FieldXxx0CantNoType");
        writer.write(MessageFormat.format(pattern, new Object[] { JavaValidation
                .getHyperLinkSemanticalObject(field, DbObject.SHORT_FORM) }));
    }

    private void writeFieldTypeVisibilityError(DbJVDataMember field) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(field);

        DbJVClass type = (DbJVClass) field.getType();
        String fieldTypeInstanceType = ApplicationContext.getSemanticalModel().getClassDisplayText(
                type, null).toLowerCase();
        String pattern = LocaleMgr.validation.getString("FieldXxx0CantXxx1Xxx2AsTypeVis3");
        writer.write(MessageFormat.format(pattern, new Object[] {
                JavaValidation.getHyperLinkSemanticalObject(field, DbObject.SHORT_FORM),
                fieldTypeInstanceType, JavaValidation.getHyperLinkSemanticalObject(type),
                getAccessibilityErrorMessage(type) }));
    }

    private void writeClassVolatileFinalFieldError(DbJVDataMember field) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(field);
        String pattern = LocaleMgr.validation.getString("FieldXxx0CantFinalVolatile");
        writer.write(MessageFormat.format(pattern, new Object[] { JavaValidation
                .getHyperLinkSemanticalObject(field, DbObject.SHORT_FORM) }));
    }

    private void writeInnerClassStaticMemberError(DbSemanticalObject member) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(member);
        String type = ApplicationContext.getSemanticalModel().getClassDisplayText(member, null)
                .toLowerCase();
        String pattern = LocaleMgr.validation
                .getString("Method0Xxx1CantStatic_InnerCantStaticMembers");
        writer.write(MessageFormat.format(pattern, new Object[] { type,
                JavaValidation.getHyperLinkSemanticalObject(member, DbObject.SHORT_FORM) }));
    }

    private void writeInterfaceFieldVisibilityError(DbJVDataMember field) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(field);
        String pattern = LocaleMgr.validation
                .getString("FieldXxx0CantVis1_InterfaceFieldImplicitlyPublic");
        writer.write(MessageFormat.format(pattern, new Object[] {
                JavaValidation.getHyperLinkSemanticalObject(field, DbObject.SHORT_FORM),
                field.getVisibility().toString() }));
    }

    private void writeInterfaceTransientFieldError(DbJVDataMember field) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(field);
        String pattern = LocaleMgr.validation
                .getString("FieldXxx0CantTransient_InterfaceFieldImplicitlyStatic");
        writer.write(MessageFormat.format(pattern, new Object[] { JavaValidation
                .getHyperLinkSemanticalObject(field, DbObject.SHORT_FORM) }));
    }

    private void writeInterfaceVolatileFieldError(DbJVDataMember field) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(field);
        String pattern = LocaleMgr.validation
                .getString("FieldXxx0CantVolatile_InterfaceFieldImplicitlyFinal");
        writer.write(MessageFormat.format(pattern, new Object[] { JavaValidation
                .getHyperLinkSemanticalObject(field, DbObject.SHORT_FORM) }));
    }

    private void writeNotTypedParameterError(DbJVParameter param) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(param);

        DbOOAbstractMethod operation = (DbOOAbstractMethod) (param.getComposite());
        String methodHref = JavaValidation.getHyperLinkSemanticalObject(operation, operation
                .buildDisplayString());
        String pattern = LocaleMgr.validation.getString("ParamXxx0InClass1Xxx2CantNoType");
        writer.write(MessageFormat.format(pattern, new Object[] {
                JavaValidation.getHyperLinkSemanticalObject(param, DbObject.SHORT_FORM),
                ApplicationContext.getSemanticalModel().getClassDisplayText(operation, null)
                        .toLowerCase(), methodHref }));
    }

    private void writeParameterTypeVisibilityError(DbJVParameter param) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(param);

        DbJVClass type = (DbJVClass) param.getType();
        DbOOAbstractMethod operation = (DbOOAbstractMethod) (param.getComposite());
        String parameterTypeInstanceType = ApplicationContext.getSemanticalModel()
                .getClassDisplayText(type, null).toLowerCase();
        String methodHref = JavaValidation.getHyperLinkSemanticalObject(operation, operation
                .buildDisplayString());
        String pattern = LocaleMgr.validation.getString("S2ParamXxx0InClass1Xxx2CantXxx3Xxx4Type");
        writer.write(MessageFormat.format(pattern, new Object[] {
                JavaValidation.getHyperLinkSemanticalObject(param, DbObject.SHORT_FORM),
                ApplicationContext.getSemanticalModel().getClassDisplayText(operation, null)
                        .toLowerCase(), methodHref, parameterTypeInstanceType,
                JavaValidation.getHyperLinkSemanticalObject(type),
                getAccessibilityErrorMessage(type) }));
    }

    private void writeParamNameDuplicateError(DbJVParameter otherParam) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(otherParam);

        DbOOAbstractMethod operation = (DbOOAbstractMethod) (otherParam.getComposite());
        String operationInstanceType = ApplicationContext.getSemanticalModel().getClassDisplayText(
                operation, null).toLowerCase();
        String methodHref = JavaValidation.getHyperLinkSemanticalObject(operation, operation
                .buildDisplayString());
        String pattern = LocaleMgr.validation.getString("DupParamXxx0InMethod1Xxx2Xxx1");
        writer.write(MessageFormat.format(pattern, new Object[] {
                JavaValidation.getHyperLinkSemanticalObject(otherParam, DbObject.SHORT_FORM),
                operationInstanceType, methodHref }));
    }

    private void writeInterfaceConstructorError() throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        String msg;
        if (adtConstructors.size() > 1)
            msg = LocaleMgr.validation.getString("InvalidConstDecls");
        else
            msg = LocaleMgr.validation.getString("InvalidConstDecl");
        writer.write(msg);
        boolean first = true;
        for (Enumeration constructors = adtConstructors.elements(); constructors.hasMoreElements();) {
            if (!first) {
                writer.write(", ");
            }
            DbJVConstructor constructor = (DbJVConstructor) (constructors.nextElement());
            reportError(constructor);
            writer.write(JavaValidation.getHyperLinkSemanticalObject(constructor));
            first = false;
        }
        writer.write(LocaleMgr.validation.getString("InterfaceCantHaveConstr"));
    }

    private void writeDefaultConstructorError(DbJVClass superClass) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(superClass);
        String pattern = LocaleMgr.validation
                .getString("NotPossibleProvideConstructor_Superclass0NoConstr1WithoutNoParam");
        writer.write(MessageFormat.format(pattern, new Object[] {
                JavaValidation.getHyperLinkSemanticalObject(superClass), superClass.getName() }));
    }

    private void writeConstructorsWithoutExceptionError(DbJVClass superClass,
            Vector constructorsWithoutException) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        String msg;
        if (constructorsWithoutException.size() > 1)
            msg = LocaleMgr.validation.getString("UnrepExInConstrs");
        else
            msg = LocaleMgr.validation.getString("UnrepExInConstr");
        writer.write(msg);
        boolean first = true;
        for (Enumeration constructors = constructorsWithoutException.elements(); constructors
                .hasMoreElements();) {
            if (!first) {
                writer.write(", ");
            }
            DbJVConstructor constructor = (DbJVConstructor) (constructors.nextElement());
            reportError(constructor);
            writer.write(JavaValidation.getHyperLinkSemanticalObject(constructor));
            first = false;
        }
        String pattern = LocaleMgr.validation.getString("AllConstrSuperclassXxx0HaveThrows");
        writer.write(MessageFormat.format(pattern, new Object[] { JavaValidation
                .getHyperLinkSemanticalObject(superClass) }));
    }

    private void writeConstructorNameError(DbJVConstructor constructor) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(constructor);
        String pattern = LocaleMgr.validation.getString("Constr0InvalidName");
        writer.write(MessageFormat.format(pattern, new Object[] { JavaValidation
                .getHyperLinkSemanticalObject(constructor) }));
    }

    private void writeOperationExceptionVisibilityError(DbOOAbstractMethod operation,
            DbJVClass javaException) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(operation);
        String pattern = LocaleMgr.validation
                .getString("OperationXxx0_Xxx1CantThrowsXxx2_Xxx2Exception_Xxx3");
        writer.write(MessageFormat.format(pattern, new Object[] {
                ApplicationContext.getSemanticalModel().getClassDisplayText(operation, null)
                        .toLowerCase(),
                JavaValidation.getHyperLinkSemanticalObject(operation),
                ApplicationContext.getSemanticalModel().getClassDisplayText(javaException, null)
                        .toLowerCase(), JavaValidation.getHyperLinkSemanticalObject(javaException),
                getAccessibilityErrorMessage(javaException) }));
    }

    private void writeReturnTypeVisibilityError(DbJVMethod method) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(method);
        DbJVClass type = (DbJVClass) method.getReturnType();
        String operationTypeInstanceType = ApplicationContext.getSemanticalModel()
                .getClassDisplayText(type, null).toLowerCase();
        String pattern = LocaleMgr.validation.getString("Method0CantHaveXxx1Xxx2ReturnType_Xxx3");
        writer.write(MessageFormat.format(pattern, new Object[] {
                JavaValidation.getHyperLinkSemanticalObject(method), operationTypeInstanceType,
                JavaValidation.getHyperLinkSemanticalObject(type),
                getAccessibilityErrorMessage(type) }));
    }

    private void writeAbstractMethodOfNotAbstractClassError(DbJVMethod method) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(method);
        String pattern = LocaleMgr.validation
                .getString("ClassCantNotAbstractContainsAbstracMethod0");
        writer.write(MessageFormat.format(pattern, new Object[] { JavaValidation
                .getHyperLinkSemanticalObject(method) }));
    }

    private void writeClassAbstractPrivateMethodError(DbJVMethod method) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(method);
        String pattern = LocaleMgr.validation.getString("Method0CantPrivateAbstract");
        writer.write(MessageFormat.format(pattern, new Object[] { JavaValidation
                .getHyperLinkSemanticalObject(method) }));
    }

    private void writeClassAbstractFinalMethodError(DbJVMethod method) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(method);
        String pattern = LocaleMgr.validation.getString("Method0CantFinalAbstract");
        writer.write(MessageFormat.format(pattern, new Object[] { JavaValidation
                .getHyperLinkSemanticalObject(method) }));
    }

    private void writeClassAbstractStaticMethodError(DbJVMethod method) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(method);
        String pattern = LocaleMgr.validation.getString("Method0CantStatAbstract");
        writer.write(MessageFormat.format(pattern, new Object[] { JavaValidation
                .getHyperLinkSemanticalObject(method) }));
    }

    private void writeClassAbstractSynchronizeDMethodError(DbJVMethod method) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(method);
        String pattern = LocaleMgr.validation.getString("Method0CantSyncAbstract");
        writer.write(MessageFormat.format(pattern, new Object[] { JavaValidation
                .getHyperLinkSemanticalObject(method) }));
    }

    private void writeClassAbstractNativEMethodError(DbJVMethod method) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(method);
        String pattern = LocaleMgr.validation.getString("Method0CantNatAbstract");
        writer.write(MessageFormat.format(pattern, new Object[] { JavaValidation
                .getHyperLinkSemanticalObject(method) }));
    }

    private void writeClassAbstractOrNativeMethodWithBodyError(DbJVMethod method)
            throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(method);

        String pattern = LocaleMgr.validation.getString("Abstract0Method1CantBody");
        String modifier = DbJVMethod.fAbstract.getGUIName().toLowerCase();
        if (!method.isAbstract() && method.isNative()) {
            pattern = LocaleMgr.validation.getString("Native0Method1CantBody");
            modifier = DbJVMethod.fNative.getGUIName().toLowerCase();
        }
        writer.write(MessageFormat.format(pattern, new Object[] { modifier,
                JavaValidation.getHyperLinkSemanticalObject(method) }));
    }

    private void writeInterfaceMethodVisibilityError(DbJVMethod method) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(method);
        String pattern = LocaleMgr.validation.getString("Method0Cant1");
        writer.write(MessageFormat.format(pattern, new Object[] {
                JavaValidation.getHyperLinkSemanticalObject(method),
                method.getVisibility().toString() }));
    }

    private void writeInterfaceFinalMethodError(DbJVMethod method) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(method);
        String pattern = LocaleMgr.validation.getString("Method0CantFinal");
        writer.write(MessageFormat.format(pattern, new Object[] { JavaValidation
                .getHyperLinkSemanticalObject(method) }));
    }

    private void writeInterfaceStaticMethodError(DbJVMethod method) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(method);
        String pattern = LocaleMgr.validation.getString("Method0CantStatic");
        writer.write(MessageFormat.format(pattern, new Object[] { JavaValidation
                .getHyperLinkSemanticalObject(method) }));
    }

    private void writeInterfaceSynchronizeDMethodError(DbJVMethod method) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(method);
        String pattern = LocaleMgr.validation.getString("Method0CantSynchronized");
        writer.write(MessageFormat.format(pattern, new Object[] { JavaValidation
                .getHyperLinkSemanticalObject(method) }));
    }

    private void writeInterfaceNativEMethodError(DbJVMethod method) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(method);
        String pattern = LocaleMgr.validation.getString("Method0CantNative");
        writer.write(MessageFormat.format(pattern, new Object[] { JavaValidation
                .getHyperLinkSemanticalObject(method) }));
    }

    private void writeInterfaceMethodWithBodyError(DbJVMethod method) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(method);
        String pattern = LocaleMgr.validation.getString("AbstractMethod0CantBody");
        writer.write(MessageFormat.format(pattern, new Object[] { JavaValidation
                .getHyperLinkSemanticalObject(method) }));
    }

    private void writeReturnTypeOverridingOrHidingMethodError(DbJVMethod method,
            DbJVMethod superMethod) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(method);
        DbJVClass methodAdt = (DbJVClass) method.getComposite();
        DbJVClass adtSuper = (DbJVClass) superMethod.getComposite();
        DbOOAdt adtType = method.getReturnType();
        boolean hiding = (method.isStatic() && !methodAdt.isInterface());
        String pattern = LocaleMgr.validation.getString("methodRettype0Xxx1");
        String returnType = (adtType != null ? JavaValidation.getHyperLinkSemanticalObject(adtType)
                : LocaleMgr.validation.getString("Void"));
        writer.write(MessageFormat.format(pattern, new Object[] { returnType,
                JavaValidation.getHyperLinkSemanticalObject(method) }));
        if (methodAdt != adt) {
            writer.write(getInheritedFrom(methodAdt));
        }
        if (superMethod.getReturnType() != null) {
            returnType = JavaValidation.getHyperLinkSemanticalObject(superMethod.getReturnType(),
                    DbObject.SHORT_FORM);
        } else {
            returnType = LocaleMgr.validation.getString("Void");
        }
        pattern = LocaleMgr.validation
                .getString("CantHide0MethodXxx1Xxx2InXxx3Xxx4DifferentRettype");
        writer.write(MessageFormat.format(pattern, new Object[] {
                (hiding ? LocaleMgr.validation.getString("Hide") : LocaleMgr.validation
                        .getString("Override")),
                returnType,
                JavaValidation.getHyperLinkSemanticalObject(superMethod),
                ApplicationContext.getSemanticalModel().getClassDisplayText(adtSuper, null)
                        .toLowerCase(), JavaValidation.getHyperLinkSemanticalObject(adtSuper) }));
    }

    private void writeVisibilityOverridingOrHidingMethodError(DbJVMethod method,
            DbJVMethod superMethod) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(method);

        DbJVClass methodAdt = (DbJVClass) method.getComposite();
        DbJVClass adtSuper = (DbJVClass) superMethod.getComposite();
        boolean hiding = (method.isStatic() && !methodAdt.isInterface());
        String pattern = LocaleMgr.validation.getString("Visibility0Method1");
        writer.write(MessageFormat.format(pattern, new Object[] {
                method.getVisibility().toString(),
                JavaValidation.getHyperLinkSemanticalObject(method) }));

        if (methodAdt != adt) {
            writer.write(getInheritedFrom(methodAdt));
        }
        String superMethodVis;
        String lastWord;
        if (adtSuper.isInterface()) {
            superMethodVis = "";
            lastWord = LocaleMgr.validation.getString("InterfaceMethodIsPublic");
        } else {
            superMethodVis = superMethod.getVisibility().toString();
            lastWord = LocaleMgr.validation.getString("DotReturn");
        }
        pattern = LocaleMgr.validation.getString("CantXxx0Xxx1Method2InXxx3Xxx4WeakerPrivilege5");
        writer.write(MessageFormat.format(pattern, new Object[] {
                (hiding ? LocaleMgr.validation.getString("Hide") : LocaleMgr.validation
                        .getString("Override")),
                superMethodVis,
                JavaValidation.getHyperLinkSemanticalObject(superMethod),
                ApplicationContext.getSemanticalModel().getClassDisplayText(adtSuper, null)
                        .toLowerCase(), JavaValidation.getHyperLinkSemanticalObject(adtSuper),
                lastWord }));
    }

    private void writeFinalOverriddenOrHiddenMethodError(DbJVMethod method, DbJVMethod superMethod)
            throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(method);
        DbJVClass methodAdt = (DbJVClass) method.getComposite();
        boolean hiding = (method.isStatic() && !methodAdt.isInterface());
        String inheritedAdt = methodAdt == adt ? "" : getInheritedFrom(methodAdt);
        String pattern = LocaleMgr.validation
                .getString("Method0Xxx1CantXxx2FinalMethodXxx3InClass4_CantBe5");
        writer.write(MessageFormat.format(pattern, new Object[] {
                JavaValidation.getHyperLinkSemanticalObject(method),
                inheritedAdt,
                (hiding ? LocaleMgr.validation.getString("Hide") : LocaleMgr.validation
                        .getString("Override")),
                JavaValidation.getHyperLinkSemanticalObject(superMethod),
                JavaValidation.getHyperLinkSemanticalObject((DbSemanticalObject) superMethod
                        .getComposite()),
                (method.isStatic() ? LocaleMgr.validation.getString("Hidden")
                        : LocaleMgr.validation.getString("Overridden")) }));
    }

    private void writeNotStaticHiddenMethodError(DbJVMethod method, DbJVMethod superMethod)
            throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(method);

        DbJVClass methodAdt = (DbJVClass) method.getComposite();
        DbJVClass adtSuper = (DbJVClass) superMethod.getComposite();

        String inheritedAdt = methodAdt == adt ? "" : getInheritedFrom(methodAdt);
        String pattern = LocaleMgr.validation
                .getString("StaticMethod0Xxx1CantHideNotStaticMethod2InXxx3Xxx4");
        writer.write(MessageFormat.format(pattern, new Object[] {
                JavaValidation.getHyperLinkSemanticalObject(method),
                inheritedAdt,
                JavaValidation.getHyperLinkSemanticalObject(superMethod),
                ApplicationContext.getSemanticalModel().getClassDisplayText(adtSuper, null)
                        .toLowerCase(), JavaValidation.getHyperLinkSemanticalObject(adtSuper) }));
    }

    private void writeStaticOverriddenMethodError(DbJVMethod method, DbJVMethod superMethod)
            throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(method);

        DbJVClass methodAdt = (DbJVClass) method.getComposite();
        DbJVClass adtSuper = (DbJVClass) superMethod.getComposite();
        String inheritedAdt = methodAdt == adt ? "" : getInheritedFrom(methodAdt);
        String pattern = LocaleMgr.validation
                .getString("NotStaticMethod0Xxx1CantOverrideStaticMethod2InXxx3");
        writer.write(MessageFormat.format(pattern, new Object[] {
                JavaValidation.getHyperLinkSemanticalObject(method), inheritedAdt,
                JavaValidation.getHyperLinkSemanticalObject(superMethod),
                JavaValidation.getHyperLinkSemanticalObject(adtSuper) }));
    }

    private void writeExceptionOverridingOrHidingMethodError(DbJVMethod method,
            DbJVMethod superMethod, DbJVClass exception) throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(method);

        DbJVClass methodAdt = (DbJVClass) method.getComposite();
        DbJVClass adtSuper = (DbJVClass) superMethod.getComposite();
        boolean hiding = (method.isStatic() && !methodAdt.isInterface());
        String inheritedAdt = methodAdt == adt ? "" : getInheritedFrom(methodAdt);
        String pattern = LocaleMgr.validation
                .getString("Method0Yyy1CantXxx2Xxx3InXxx4Xxx5_Xxx6MethodNotThrowExc7");
        writer.write(MessageFormat.format(pattern, new Object[] {
                JavaValidation.getHyperLinkSemanticalObject(method),
                inheritedAdt,
                (hiding ? LocaleMgr.validation.getString("Hide") : LocaleMgr.validation
                        .getString("Override")),
                JavaValidation.getHyperLinkSemanticalObject(superMethod),
                ApplicationContext.getSemanticalModel().getClassDisplayText(adtSuper, null)
                        .toLowerCase(),
                JavaValidation.getHyperLinkSemanticalObject(adtSuper),
                (hiding ? LocaleMgr.validation.getString("Hidden") : LocaleMgr.validation
                        .getString("Overridden")),
                JavaValidation.getHyperLinkSemanticalObject(exception) }));
    }

    private void writeAbstractSuperMethodNotImplementedError(DbJVMethod superMethod)
            throws DbException {
        if (adtNbErrors == 0) {
            writeAdtErrorTitle();
        }

        reportError(adt);

        DbJVClass adtSuper = (DbJVClass) superMethod.getComposite();
        String pattern = LocaleMgr.validation
                .getString("ClassCantBeNotAbstractAndNotImplementAbstractMethod0InXxx1Xxx2");
        writer.write(MessageFormat.format(pattern, new Object[] {
                JavaValidation.getHyperLinkSemanticalObject(superMethod),
                ApplicationContext.getSemanticalModel().getClassDisplayText(adtSuper, null)
                        .toLowerCase(), JavaValidation.getHyperLinkSemanticalObject(adtSuper) }));
    }

    private void reportError(DbSemanticalObject dbo) throws DbException {
        adtNbErrors++;
        writeErrorNb(adtNbErrors);

        dbo.setValidationStatus(DbObject.VALIDATION_ERROR);
    }

}
