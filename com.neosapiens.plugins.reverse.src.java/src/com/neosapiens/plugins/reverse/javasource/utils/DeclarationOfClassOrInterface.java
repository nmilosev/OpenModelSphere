package com.neosapiens.plugins.reverse.javasource.utils;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Modifier;
import com.neosapiens.plugins.reverse.javasource.actions.SemanticAction;

public class DeclarationOfClassOrInterface implements SemanticDeclaration {

    private String currentPackage;
    private String currentClass;
    private String javadoc;    
    private boolean isInterface;
    private boolean isClass;
    private boolean isEnum;
    private int modifiers;
    private String classOrInterfaceName;
    private List<String> extendsList;
    private List<String> implementsList;

    /**
     * The constructor of DeclarationOfClass, initializing all the field of class.
     */
    public DeclarationOfClassOrInterface() {
        this.currentPackage = null;        
        this.javadoc = null;        
        //Type of instance
        this.isClass = false;
        this.isInterface = false;
        this.isEnum = false;
        //Name of instance        
        this.classOrInterfaceName = null;
        //Inheritance list and implementation list
        this.extendsList = new ArrayList<String>();
        this.implementsList = new ArrayList<String>();        
        //Modifiers
        this.modifiers = 0;
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
    
    public boolean isInterface() {
        return isInterface;
    }

    public void setInterface(boolean isInterface) {
        this.isInterface = isInterface;
    }

    public boolean isClass() {
        return isClass;
    }

    public void setClass(boolean isClass) {
        this.isClass = isClass;
    }

    public boolean isEnum() {
        return isEnum;
    }

    public void setEnum(boolean isEnum) {
        this.isEnum = isEnum;
    }

    public boolean isFinal() {
        return Modifier.isFinal(this.modifiers);
    }

    public boolean isStatic() {
        return Modifier.isStatic(this.modifiers);
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(this.modifiers);
    }

    public boolean isStrictfp() {
        return Modifier.isStrict(this.modifiers);
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

    public String getClassOrInterfaceName() {
        return classOrInterfaceName;
    }

    public void setClassOrInterfaceName(String classOrInterfaceName) {
        this.classOrInterfaceName = classOrInterfaceName;
    }

    public List<String> getExtendsList() {
        return extendsList;
    }

    public void setExtendsList(List<String> extendsList) {
        this.extendsList = extendsList;
    }

    public List<String> getImplementsList() {
        return implementsList;
    }

    public void setImplementsList(List<String> implementsList) {
        this.implementsList = implementsList;
    }

    @Override
    public void doAction(SemanticAction action) {
        action.createClass(this);
    }

}
