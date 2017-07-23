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

    @Override
    public IObjectFormatter<Message> getMessageFormatter() {
        return getObjectFormatter(Message.class);
    }

    @Override
    public IObjectParser<Message> getMessageParser() {
        return (str)->gson.fromJson(str, Message.class);
    }

    @Override
    public IObjectFormatter<Product> getProductFormatter() {
        return getObjectFormatter(Product.class);
    }

    @Override
    public IObjectParser<Product> getProductParser() {
        return getObjectParser(Product.class);
    }

    @Override
    public IObjectFormatter<List<Product>> getProductListFormatter() {
        return getListFormatter(Product.class);
    }

    @Override
    public IObjectParser<List<Product>> getProductListParser() {
        return getListParser(Product.class);
    }

    @Override
    public IObjectFormatter<Sale> getSaleFormatter() {
        return getObjectFormatter(Sale.class);
    }

    @Override
    public IObjectParser<Sale> getSaleParser() {
        return null;
    }

    @Override
    public IObjectFormatter<PqtMember> getPqtMemberFormatter() {
        return getObjectFormatter(PqtMember.class);
    }

    @Override
    public IObjectParser<PqtMember> getPqtMemberParser() {
        return getObjectParser(PqtMember.class);
    }

    @Override
    public IObjectFormatter<ProductUpdate> getProductUpdateFormatter() {
        return getObjectFormatter(ProductUpdate.class);
    }

    @Override
    public IObjectParser<ProductUpdate> getProductUpdateParser() {
        return getObjectParser(ProductUpdate.class);
    }

    @Override
    public IObjectFormatter<MessageType> getMessageTypeFormatter() {
        return getObjectFormatter(MessageType.class);
    }

    @Override
    public IObjectParser<MessageType> getMessageTypeParser() {
        return getObjectParser(MessageType.class);
    }

    @Override
    public IObjectFormatter<PqtMemberType> getPqtMemberTypeFormatter() {
        return getObjectFormatter(PqtMemberType.class);
    }

    @Override
    public IObjectParser<PqtMemberType> getPqtMemberTypeParser() {
        return getObjectParser(PqtMemberType.class);
    }

    private <T> IObjectFormatter<T> getObjectFormatter(Class<T> clazz){
        return (obj)->gson.toJson(obj);
    }

    private <T> IObjectParser<T> getObjectParser(Class<T> clazz){
        return (str)->gson.fromJson(str, clazz);
    }

    private <T> IObjectFormatter<List<T>> getListFormatter(Class<T> clazz){
        return (obj)->gson.toJson(obj);
    }

    private <T> IObjectParser<List<T>> getListParser(Class<T> clazz){
        Type listType = new TypeToken<ArrayList<T>>(){}.getType();
        return (str)->gson.fromJson(str, listType);
    }
}
