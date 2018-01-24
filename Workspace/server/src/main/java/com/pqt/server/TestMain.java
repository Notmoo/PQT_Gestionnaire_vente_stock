package com.pqt.server;

import com.pqt.core.communication.GSonMessageToolFactory;
import com.pqt.core.communication.IMessageToolFactory;
import com.pqt.core.communication.IObjectFormatter;
import com.pqt.core.entities.messages.Message;
import com.pqt.core.entities.messages.MessageType;
import com.pqt.core.entities.product.Category;
import com.pqt.core.entities.product.Product;
import com.pqt.core.entities.user_account.Account;
import com.pqt.core.entities.user_account.AccountLevel;
import com.pqt.server.module.account.FileAccountDao;
import com.pqt.server.module.account.IAccountDao;
import com.pqt.server.module.stock.FileStockDao;
import com.pqt.server.module.stock.IStockDao;

import java.util.ArrayList;
import java.util.List;

public class TestMain {

    public static void main(String[] args){

        IMessageToolFactory messageToolFactory = new GSonMessageToolFactory();
        IObjectFormatter<Message> messageFormater = messageToolFactory.getObjectFormatter(Message.class);
        System.out.println(messageFormater.format(new Message(MessageType.QUERY_PING, null, null, null, null)));

        //generateAccountFile();
        //generateProductFile();
    }

    private static void generateProductFile(){
        System.out.println("PRODUCTS");
        List<Product> products = new ArrayList<>();

        Category mealsCat = new Category(0, "Meals");
        Category drinksCat = new Category(1, "Drinks");
        Category sidesCat = new Category(2, "Sides");

        //long id, String name, int amountRemaining, int amountSold, boolean sellable, double price, List<Long> components, Category category
        products.add(new Product(0, "Meal 1", 10, 0, true, 1d, null, mealsCat));
        products.add(new Product(0, "Meal 2", 20, 10, true, 1.5d, null, mealsCat));
        products.add(new Product(0, "Drink 1", 30, 20, true, 2d, null, drinksCat));
        products.add(new Product(0, "Side 1", 40, 30, true, 2.5d, null, sidesCat));

        IStockDao stockDao = new FileStockDao("G:\\temp");
        products.forEach(stockDao::addProduct);
    }

    private static void generateAccountFile(){
        System.out.println("ACCOUNTS");
        List<Account> accounts = new ArrayList<>();

        accounts.add(new Account("Master", "Master", AccountLevel.MASTER));
        accounts.add(new Account("Waiter", "Waiter", AccountLevel.WAITER));
        accounts.add(new Account("Guest", "Guest", AccountLevel.GUEST));
        accounts.add(new Account("Staff", "Staff", AccountLevel.STAFF));
        accounts.add(new Account("Lowest", "Lowest", AccountLevel.LOWEST));

        IAccountDao accountDao = new FileAccountDao("G:\\temp");
        accounts.forEach(accountDao::addAccount);
    }
}
