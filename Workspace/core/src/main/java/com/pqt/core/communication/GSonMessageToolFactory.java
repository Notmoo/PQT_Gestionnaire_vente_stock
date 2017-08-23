package com.pqt.core.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//TODO écrire Javadoc
public class GSonMessageToolFactory implements IMessageToolFactory {
    private Gson gson;

    public GSonMessageToolFactory() {
        gson = new GsonBuilder().create();
    }

    public <T> IObjectFormatter<T> getObjectFormatter(Class<T> clazz){
        return (obj)->gson.toJson(obj);
    }
    public <T> IObjectParser<T> getObjectParser(Class<T> clazz){
        return (str)->gson.fromJson(str, clazz);
    }
    public <T> IObjectFormatter<List<T>> getListFormatter(Class<T> clazz){
        return (obj)->gson.toJson(obj);
    }
    public <T> IObjectParser<List<T>> getListParser(Class<T> clazz){
        Type listType = new TypeToken<ArrayList<T>>(){}.getType();
        return (str)->gson.fromJson(str, listType);
    }
}
