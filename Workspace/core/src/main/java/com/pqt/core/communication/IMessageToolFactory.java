package com.pqt.core.communication;

import com.pqt.core.entities.members.PqtMember;
import com.pqt.core.entities.members.PqtMemberType;
import com.pqt.core.entities.messages.Message;
import com.pqt.core.entities.messages.MessageType;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.product.ProductUpdate;
import com.pqt.core.entities.sale.Sale;

import java.util.List;

public interface IMessageToolFactory {

	IObjectFormatter<Message> getMessageFormatter();

	IObjectParser<Message> getMessageParser();

	IObjectFormatter<Product> getProductFormatter();

	IObjectParser<Product> getProductParser();

    IObjectFormatter<List<Product>> getProductListFormatter();

    IObjectParser<List<Product>> getProductListParser();

	IObjectFormatter<Sale> getSaleFormatter();

	IObjectParser<Sale> getSaleParser();

	IObjectFormatter<PqtMember> getPqtMemberFormatter();

	IObjectParser<PqtMember> getPqtMemberParser();

	IObjectFormatter<ProductUpdate> getProductUpdateFormatter();

	IObjectParser<ProductUpdate> getProductUpdateParser();

	IObjectFormatter<MessageType> getMessageTypeFormatter();

	IObjectParser<MessageType> getMessageTypeParser();

	IObjectFormatter<PqtMemberType> getPqtMemberTypeFormatter();

	IObjectParser<PqtMemberType> getPqtMemberTypeParser();
}
