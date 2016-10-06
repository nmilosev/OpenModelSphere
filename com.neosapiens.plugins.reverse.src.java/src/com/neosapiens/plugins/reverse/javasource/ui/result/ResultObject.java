package com.neosapiens.plugins.reverse.javasource.ui.result;

public class ResultObject {

    private String type;
    private String name;
    private String alias;
    private String description;
    
    public ResultObject(String type, String name, String alias, String description) {
        this.type = type;
        this.name = name;
        this.alias = alias;
        this.description = description;
    }
    

    public String getType() {
        return type;
    }
    

    public void setType(String type) {
        this.type = type;
    }
    

    public String getName() {
        return name;
    }
    

    public void setName(String name) {
        this.name = name;
    }
    

    public String getAlias() {
        return alias;
    }
    

    public void setAlias(String alias) {
        this.alias = alias;
    }
    

    public String getDescription() {
        return description;
    }
    

    public void setDescription(String description) {
        this.description = description;
    }
}
