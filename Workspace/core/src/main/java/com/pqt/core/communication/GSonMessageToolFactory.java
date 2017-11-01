package com.pqt.core.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
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
        return (str)->gson.fromJson(str, new Element<>(clazz));
    }
    public <T> IObjectFormatter<List<T>> getListFormatter(Class<T> clazz){
        return (obj)->gson.toJson(obj);
    }
    public <T> IObjectParser<List<T>> getListParser(Class<T> clazz){
        //Type listType = new TypeToken<ArrayList<T>>(){}.getType();
        return (str)->gson.fromJson(str, new ListWithElements<>(clazz));
    }

    private class Element<T> implements ParameterizedType {

        private Class<T> cl;

        public Element(Class<T> cl) {
            this.cl = cl;
        }

        public Type[] getActualTypeArguments() {
            return new Type[] {cl};
        }

        public Type getRawType() {
            return cl;
        }

        public Type getOwnerType() {
            return null;
        }
    }

    private class ListWithElements<T> implements ParameterizedType {

        private Class<T> elementsClass;

        public ListWithElements(Class<T> elementsClass) {
            this.elementsClass = elementsClass;
        }

        public Type[] getActualTypeArguments() {
            return new Type[] {elementsClass};
        }

        public Type getRawType() {
            return List.class;
        }

        public Type getOwnerType() {
            return null;
        }
    }
}
