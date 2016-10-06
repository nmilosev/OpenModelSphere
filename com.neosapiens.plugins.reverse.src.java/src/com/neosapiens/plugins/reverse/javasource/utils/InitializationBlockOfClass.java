package com.neosapiens.plugins.reverse.javasource.utils;

public class InitializationBlockOfClass {

    private String currentClass;
    private String currentPackage;
    private String javadoc;
    
    private String initializationBlockName;
    private boolean isStatic;
    
    private String body;
    
    /**
     * The constructor of InitializationBlockOfClass, initializing all the field of class.
     */
    public InitializationBlockOfClass(){
        this.currentClass = null;
        this.currentPackage = null;
        this.javadoc = null;
        this.initializationBlockName = null;
        this.isStatic = false;
        this.body = null;
    }

    //********
    // GETTER AND SETTER
    //******************
    
    public String getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(String currentClass) {
        this.currentClass = currentClass;
    }

    public String getCurrentPackage() {
        return currentPackage;
    }

    public void setCurrentPackage(String currentPackage) {
        this.currentPackage = currentPackage;
    }

    public String getJavadoc() {
        return javadoc;
    }

    public void setJavadoc(String javadoc) {
        this.javadoc = javadoc;
    }    
    
    public String getInitializationBlockName() {
        return initializationBlockName;
    }

    public void setInitializationBlockName(String initializationBlockName) {
        this.initializationBlockName = initializationBlockName;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    
    
    
}
