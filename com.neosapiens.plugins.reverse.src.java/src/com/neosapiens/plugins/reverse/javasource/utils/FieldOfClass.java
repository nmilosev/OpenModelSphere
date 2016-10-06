package com.neosapiens.plugins.reverse.javasource.utils;

import java.lang.reflect.Modifier;

import com.neosapiens.plugins.reverse.javasource.actions.SemanticAction;

public class FieldOfClass implements SemanticDeclaration {
    private String currentPackage;
    private String currentClass;

    private String javadoc;
    
    private int modifiers;

    private String fieldType;
    private String fieldName;
    private String fieldValue;

    /**
     * The constructor of FieldOfClass, initializing all the fields of class.
     */
    public FieldOfClass() {
        this.modifiers = 0;
        this.fieldType = null;
        this.fieldName = null;
        this.fieldValue = null;
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

    public boolean isPublic() {
        return Modifier.isPublic(this.modifiers);
    }
    
    public boolean isPrivate() {
        return Modifier.isPrivate(this.modifiers);
    }

    public boolean isProtected() {
        return Modifier.isProtected(this.modifiers);
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    @Override
    public void doAction(SemanticAction action) {
        action.createField(this);
    }

}
