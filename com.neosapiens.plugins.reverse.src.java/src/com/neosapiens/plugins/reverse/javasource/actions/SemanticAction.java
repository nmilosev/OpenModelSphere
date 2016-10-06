package com.neosapiens.plugins.reverse.javasource.actions;

import com.neosapiens.plugins.reverse.javasource.utils.DeclarationOfClassOrInterface;
import com.neosapiens.plugins.reverse.javasource.utils.DeclarationOfMethod;
import com.neosapiens.plugins.reverse.javasource.utils.FieldOfClass;
import com.neosapiens.plugins.reverse.javasource.utils.InitializationBlockOfClass;

public interface SemanticAction {

    /**
     * This method create a package from a package name 
     * @param packageName
     */
    public void createPackage(String packageName);

    /**
     * This method create a class using the field define in the class DeclarationOfClassOrInterface
     * @param declarationOfClassOrInterface
     */
    public void createClass(DeclarationOfClassOrInterface declarationOfClassOrInterface);
    
    /**
     * This method create the field of a class using the field define in the class FieldOfClass
     * @param fieldOfClass
     */
    public void createField(FieldOfClass fieldOfClass);

    /**
     * This method create a method declaration using the field define in the class DeclarationOfMethod
     * @param declarationOfMethod
     */
    public void createMethod(DeclarationOfMethod declarationOfMethod);
    
    /**
     * This method create the list of import rules associate with a class
     * @param importName
     */
    public void createImport(String importName);
    
    /**
     * This method create a initialization block using the field define in the class InitializationBlockOfClass
     * @param initializationBlockOfClass
     */
    public void createInitializationBlock(InitializationBlockOfClass initializationBlockOfClass );
    
    public void finalizeCreation();
}
