package com.neosapiens.plugins.reverse.javasource.actions;

import com.neosapiens.plugins.reverse.javasource.utils.InitializationBlockOfClass;
import com.neosapiens.plugins.reverse.javasource.utils.ParameterOfMethode;
import com.neosapiens.plugins.reverse.javasource.utils.FieldOfClass;
import com.neosapiens.plugins.reverse.javasource.utils.DeclarationOfMethod;
import com.neosapiens.plugins.reverse.javasource.utils.DeclarationOfClassOrInterface;


public class OutputSemanticAction implements SemanticAction {

    @Override
    public void createClass(DeclarationOfClassOrInterface declarationOfClassOrInterface) {
        
        if(declarationOfClassOrInterface.getJavadoc() != null)
        {
            System.out.println(declarationOfClassOrInterface.getJavadoc());
        }
        
        if (declarationOfClassOrInterface.isPublic()) {
            System.out.print("public");
            System.out.print(" ");
        }
        if (declarationOfClassOrInterface.isPrivate()) {
            System.out.print("private");
            System.out.print(" ");
        }
        if (declarationOfClassOrInterface.isProtected()) {
            System.out.print("protected");
            System.out.print(" ");
        }
        if (declarationOfClassOrInterface.isFinal()) {
            System.out.print("final");
            System.out.print(" ");
        }
        if (declarationOfClassOrInterface.isStatic()) {
            System.out.print("static");
            System.out.print(" ");
        }
        if (declarationOfClassOrInterface.isAbstract()){
            System.out.print("abstract");
            System.out.print(" ");
        }
        if (declarationOfClassOrInterface.isClass()) {
            System.out.print("class");
        }
        if (declarationOfClassOrInterface.isInterface()) {
            System.out.print("interface");
        }
        if (declarationOfClassOrInterface.isEnum()) {
            System.out.print("enum");
        }
        
        System.out.print(" ");        
        System.out.print(declarationOfClassOrInterface.getClassOrInterfaceName());

        if (!declarationOfClassOrInterface.getExtendsList().isEmpty()) {
            System.out.print(" ");
            System.out.print("extends");
            for (String extend : declarationOfClassOrInterface.getExtendsList()) {
                System.out.print(" ");
                System.out.print(extend);
            }
        }
        if (!declarationOfClassOrInterface.getImplementsList().isEmpty())
        {
            System.out.print(" ");
            System.out.print("implements ");
            for (String implement : declarationOfClassOrInterface.getImplementsList()) {
                System.out.print(" ");
                System.out.print(implement);
            }
        }      
        System.out.println("");
    }


    @Override
    public void createField(FieldOfClass fieldOfClass) {
        if(fieldOfClass.getJavadoc() != null)
        {
            System.out.println(fieldOfClass.getJavadoc());
        }
        
        if (fieldOfClass.isPublic()) {
            System.out.print("public");
            System.out.print(" ");
        }
        if (fieldOfClass.isPrivate()) {
            System.out.print("private");
            System.out.print(" ");
        }
        if (fieldOfClass.isProtected()) {
            System.out.print("protected");
            System.out.print(" ");
        }
        if (fieldOfClass.isFinal()) {
            System.out.print("final");
            System.out.print(" ");
        }
        if (fieldOfClass.isStatic()) {
            System.out.print("static");
            System.out.print(" ");
        }
        if (fieldOfClass.isTransient()){
            System.out.print("transient");
            System.out.print(" ");
        }
        if (fieldOfClass.isVolatile()){
            System.out.print("volatile");
            System.out.print(" ");
        }

        System.out.print(fieldOfClass.getFieldType());
        System.out.print(" ");
        System.out.print(fieldOfClass.getFieldName());
        if (fieldOfClass.getFieldValue() != null) {
            System.out.print(" = ");
            System.out.print(fieldOfClass.getFieldValue());
        }
        System.out.println("");
    }


    @Override
    public void createMethod(DeclarationOfMethod declarationOfMethod) {        
        if(declarationOfMethod.getJavadoc() != null)
        {
            System.out.println(declarationOfMethod.getJavadoc());
        }
        
        if (declarationOfMethod.isPublic()) {
            System.out.print("public");
            System.out.print(" ");
        }
        if (declarationOfMethod.isPrivate()) {
            System.out.print("private");
            System.out.print(" ");
        }
        if (declarationOfMethod.isProtected()) {
            System.out.print("protected");
            System.out.print(" ");
        }
        if (declarationOfMethod.isFinal()) {
            System.out.print("final");
            System.out.print(" ");
        }
        if (declarationOfMethod.isStatic()) {
            System.out.print("static");
            System.out.print(" ");
        }
        if (declarationOfMethod.isNative()){
            System.out.print("native");
            System.out.print(" ");
        }
        if (declarationOfMethod.isSynchronized()){
            System.out.print("synchronized");
            System.out.print(" ");
        }
        if (declarationOfMethod.isTransient()){
            System.out.print("transient");
            System.out.print(" ");
        }
        if (declarationOfMethod.isVolatile()){
            System.out.print("volatile");
            System.out.print(" ");
        }

        if (!declarationOfMethod.isConstructor()) {       
            System.out.print(declarationOfMethod.getMethodReturnType());
            System.out.print(" ");
        }

        System.out.print(declarationOfMethod.getMethodName());
        
        System.out.print(" ");
        System.out.print("(");
        for(ParameterOfMethode formalParameter : declarationOfMethod.getMethodParametersList())
        {
            if(formalParameter.isFinal())
            {
                System.out.print(" ");
                System.out.print("final");
                System.out.print(" ");
                System.out.print(formalParameter.getTypeParameter());
                System.out.print(" ");
                System.out.print(formalParameter.getNameParameter());
                if (!(declarationOfMethod.getMethodParametersList().get(declarationOfMethod.getMethodParametersList().size() - 1).getNameParameter().equalsIgnoreCase(formalParameter.getNameParameter())))
                {
                    System.out.print(",");
                }
            }else
            {
                System.out.print(" ");
                System.out.print(formalParameter.getTypeParameter());
                System.out.print(" ");
                System.out.print(formalParameter.getNameParameter());
                if (!(declarationOfMethod.getMethodParametersList().get(declarationOfMethod.getMethodParametersList().size() - 1).getNameParameter().equalsIgnoreCase(formalParameter.getNameParameter())))
                {
                    System.out.print(",");
                }
            }
        }
        System.out.print(" ");
        System.out.print(")");
        
        if (declarationOfMethod.getMethodExceptionsList().size() != 0)
        {
            System.out.print("throws");
        
        for(String exception : declarationOfMethod.getMethodExceptionsList())
        {
            System.out.print(" ");
            System.out.print(exception);
        }
        }
        System.out.println("");
        
        System.out.println(declarationOfMethod.getBody());
    }

    @Override
    public void createPackage(String packageName) {
        System.out.println(packageName);
    }
    
    @Override
    public void createImport(String importName){
       System.out.println(importName);
    }
    
    @Override
    public void createInitializationBlock(InitializationBlockOfClass initializationBlockOfClass) {
        if (initializationBlockOfClass.isStatic())
        {
            System.out.print("static");
        }
        System.out.print(" ");
        System.out.println("{");
        System.out.println(initializationBlockOfClass.getBody());
        System.out.println("}");
    }
    
    @Override
    public void finalizeCreation() {
    }





}
