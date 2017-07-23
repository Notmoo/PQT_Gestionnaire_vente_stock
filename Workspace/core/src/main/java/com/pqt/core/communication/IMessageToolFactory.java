package com.pqt.core.communication;

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

public interface IMessageToolFactory {

	<T> IObjectFormatter<T> getObjectFormatter(Class<T> clazz);

	<T> IObjectParser<T> getObjectParser(Class<T> clazz);

	<T> IObjectFormatter<List<T>> getListFormatter(Class<T> clazz);

	<T> IObjectParser<List<T>> getListParser(Class<T> clazz);
}
