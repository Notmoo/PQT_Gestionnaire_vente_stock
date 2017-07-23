package com.pqt.core.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pqt.core.entities.members.PqtMember;
import com.pqt.core.entities.members.PqtMemberType;
import com.pqt.core.entities.messages.Message;
import com.pqt.core.entities.messages.MessageType;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.product.ProductUpdate;
import com.pqt.core.entities.sale.Sale;

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
