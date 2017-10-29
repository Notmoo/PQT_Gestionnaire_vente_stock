package com.pqt.core.entities.server_config;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class ServerConfig {

    private Map<ConfigFields, Boolean> fields;

    public ServerConfig() {
        fields = new HashMap<>();
    }

    public ServerConfig(Map<ConfigFields, Boolean> fields) {
        this.fields = fields;
    }

    public ServerConfig(ConfigFields... configFields) {
        fields = new HashMap<>();
        Arrays.stream(configFields).forEach(field->fields.put(field, true));

        EnumSet.allOf(ConfigFields.class).stream().filter(field->!fields.containsKey(field)).forEach(field->fields.put(field, false));
    }

    public Map<ConfigFields, Boolean> getFields() {
        return fields;
    }

    public void setFields(Map<ConfigFields, Boolean> fields) {
        this.fields = fields;
    }

    public boolean isSupported(ConfigFields field){
        return fields.containsKey(field) && fields.get(field);
    }

    public void switchFieldValue(ConfigFields field){
        if(fields.containsKey(field)){
            fields.replace(field, !fields.get(field));
        }else{
            fields.put(field, true);
        }
    }

    public boolean add(ConfigFields field, boolean value){
        if(!fields.containsKey(field)){
            fields.put(field, value);
            return true;
        }
        return false;
    }
}
