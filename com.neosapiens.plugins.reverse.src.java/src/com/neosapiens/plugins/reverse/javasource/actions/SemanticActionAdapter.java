package com.neosapiens.plugins.reverse.javasource.actions;

import com.neosapiens.plugins.reverse.javasource.utils.DeclarationOfClassOrInterface;
import com.neosapiens.plugins.reverse.javasource.utils.DeclarationOfMethod;
import com.neosapiens.plugins.reverse.javasource.utils.FieldOfClass;
import com.neosapiens.plugins.reverse.javasource.utils.InitializationBlockOfClass;

public class SemanticActionAdapter implements SemanticAction {

    @Override
    public void createPackage(String packageName) {
    }

    @Override
    public void createClass(DeclarationOfClassOrInterface declarationOfClassOrInterface) {
    }

    @Override
    public void createField(FieldOfClass fieldOfClass) {
    }

    @Override
    public void createMethod(DeclarationOfMethod declarationOfMethod) {
    }

    @Override
    public void finalizeCreation() {
    }

    @Override
    public void createImport(String importName) {

    }

    @Override
    public void createInitializationBlock(InitializationBlockOfClass initializationBlockOfClass) {
        
    }

}
