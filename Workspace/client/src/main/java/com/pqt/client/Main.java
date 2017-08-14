package com.pqt.client;

import com.pqt.client.gui.modules.sale_screen.SaleScreen;
import com.pqt.client.gui.ressources.css.GUICssTool;
import com.pqt.client.module.account.AccountService;
import com.pqt.client.module.sale.SaleService;
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

        SaleScreen saleScreen = new SaleScreen(accountService, stockService, saleService);
        Scene scene = new Scene(saleScreen.getPane(), 800, 600);
        scene.getStylesheets().clear();
        scene.getStylesheets().addAll(getClass().getResource(GUICssTool.getCssFilePath()).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
