package com.neosapiens.plugins.reverse.javasource.utils;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.neosapiens.plugins.reverse.javasource.actions.SemanticAction;

public class DeclarationOfMethod implements SemanticDeclaration {

    
    private String currentPackage;
    private String currentClass;

    private String javadoc;
    
    private boolean isConstructor;

    private int modifiers;

    private String methodReturnType;
    private String methodName;
    private List<String> methodExceptionsList;
    private List<ParameterOfMethode> methodParametersList;
    
    private String body;
    
    /**
     * The constructor of DeclarationOfMethod, initializing all the fields of class.
     */
    public DeclarationOfMethod() {
        this.isConstructor = false;
        this.modifiers = 0;
        this.methodExceptionsList = new ArrayList<String>();
        this.methodParametersList = new ArrayList<ParameterOfMethode>();
    }

    //********
    // GETTER AND SETTER
    //******************
    
    public void setModifiers(int modifiers) {        
        this.modifiers = modifiers;
    }

    public String getCurrentPackage() {
        return currentPackage;
    }

    public void setCurrentPackage(String currentPackage) {
        this.currentPackage = currentPackage;
    }

    public String getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(String currentClass) {
        this.currentClass = currentClass;
    }
    
    public String getJavadoc() {
        return javadoc;
    }

    public void setJavadoc(String javadoc) {
        this.javadoc = javadoc;
    }

    public boolean isConstructor() {
        return isConstructor;
    }

    public void setConstructor(boolean isConstructor) {
        this.isConstructor = isConstructor;
    }

    public boolean isFinal() {
        return Modifier.isFinal(this.modifiers);
    }

    public boolean isStatic() {
        return Modifier.isStatic(this.modifiers);
    }

    public boolean isTransient() {
        return Modifier.isTransient(this.modifiers);
    }

    public boolean isVolatile() {
        return Modifier.isVolatile(this.modifiers);
    }

    public boolean isNative() {
        return Modifier.isNative(this.modifiers);
    }

    public boolean isSynchronized() {
        return Modifier.isSynchronized(this.modifiers);
    }

    public boolean isPublic() {
        return Modifier.isPublic(this.modifiers);
    }
    
    public boolean isPrivate() {
        return Modifier.isPrivate(this.modifiers);
    }

    public boolean isProtected() {
        return Modifier.isProtected(this.modifiers);
    }

    public String getMethodReturnType() {
        return methodReturnType;
    }

    public void setMethodReturnType(String methodReturnType) {
        if (methodReturnType == null) {
            this.setConstructor(true);
        }
        this.methodReturnType = methodReturnType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getMethodExceptionsList() {
        return methodExceptionsList;
    }

    public void setMethodExceptionsList(List<String> methodExceptionsList) {
        this.methodExceptionsList = methodExceptionsList;
    }

    public List<ParameterOfMethode> getMethodParametersList() {
        return methodParametersList;
    }

    public void setMethodParametersList(List<ParameterOfMethode> methodParametersList) {
        this.methodParametersList = methodParametersList;
    }
    
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public void doAction(SemanticAction action) {
        action.createMethod(this);
    }

}
