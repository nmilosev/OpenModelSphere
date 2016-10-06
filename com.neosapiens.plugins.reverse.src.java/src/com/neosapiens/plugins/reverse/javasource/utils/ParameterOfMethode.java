package com.neosapiens.plugins.reverse.javasource.utils;

public class ParameterOfMethode {

    private boolean isFinal;
    private String annotation;
    private String typeParameter;
    private String nameParameter;
    
    public ParameterOfMethode(){
        this.isFinal = false;
        this.annotation = null;
        this.typeParameter = null;
        this.nameParameter = null;
    }
    
    public void setVariableModifier(String variableModifier)
    {
        if(variableModifier == null)
        {
            this.setFinal(false);
            this.setAnnotation(null);
        }
        if(variableModifier.endsWith("final") || variableModifier.equalsIgnoreCase("final"))
        {
            this.setFinal(true);
        }
        if(variableModifier.startsWith("@"))
        {
            this.setAnnotation(variableModifier.substring(0, variableModifier.length() - 5));
        }       
    }
    
    public boolean isFinal() {
        return isFinal;
    }
    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }
    public String getAnnotation() {
        return annotation;
    }
    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
    public String getTypeParameter() {
        return typeParameter;
    }
    public void setTypeParameter(String typeParameter) {
        this.typeParameter = typeParameter;
    }
    public String getNameParameter() {
        return nameParameter;
    }
    public void setNameParameter(String nameParameter) {
        this.nameParameter = nameParameter;
    }
     
}
