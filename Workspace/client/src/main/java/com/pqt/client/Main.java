package com.pqt.client;

import com.pqt.client.gui.main_frame.MainFrame;
import com.pqt.client.gui.modules.sale_screen.SaleScreen;
import com.pqt.client.gui.modules.stat_screen.StatScreen;
import com.pqt.client.gui.modules.stock_screen.StockScreen;
import com.pqt.client.gui.ressources.components.generics.others.SideBar;
import com.pqt.client.gui.ressources.components.generics.others.listeners.ISideBarListener;
import com.pqt.client.gui.ressources.css.GUICssTool;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.client.module.account.AccountService;
import com.pqt.client.module.sale.SaleService;
import com.pqt.client.module.stat.StatService;
import com.pqt.client.module.stock.StockService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SaleService saleService = new SaleService();
        StockService stockService = new StockService();
        AccountService accountService = new AccountService();
        StatService statService = new StatService();

        MainFrame mainFrame = new MainFrame(accountService);
        mainFrame.addModule(new SaleScreen(accountService, stockService, saleService));
        mainFrame.addModule(new StockScreen(stockService));
        mainFrame.addModule(new StatScreen(statService));

        Scene scene = new Scene(mainFrame.getPane(), 800, 600);
        scene.getStylesheets().clear();
        scene.getStylesheets().addAll(getClass().getResource(GUICssTool.getCssFilePath()).toExternalForm());

        primaryStage.setTitle(GUIStringTool.getAppTitle());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
