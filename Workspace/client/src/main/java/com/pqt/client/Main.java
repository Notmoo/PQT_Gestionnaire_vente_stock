package com.pqt.client;

import com.pqt.client.gui.main_frame.MainFrame;
import com.pqt.client.gui.modules.account_screen.AccountScreen;
import com.pqt.client.gui.modules.sale_screen.SaleScreen;
import com.pqt.client.gui.modules.stat_screen.StatScreen;
import com.pqt.client.gui.modules.stock_screen.StockScreen;
import com.pqt.client.gui.ressources.components.generics.toast.ToastFactory;
import com.pqt.client.gui.ressources.css.GUICssTool;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.client.gui.startup_frame.StartupFrame;
import com.pqt.client.module.ClientBackEndModuleManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //TODO ajouter écran de préloading : StartupFrame

        ClientBackEndModuleManager moduleManager = new ClientBackEndModuleManager(null);

        MainFrame mainFrame = new MainFrame(moduleManager.getAccountService());
        mainFrame.addModule(new SaleScreen(moduleManager.getAccountService(), moduleManager.getStockService(), moduleManager.getSaleService()), true);
        mainFrame.addModule(new StockScreen(moduleManager.getStockService(), moduleManager.getAccountService()));
        mainFrame.addModule(new StatScreen(moduleManager.getStatService()));
        mainFrame.addModule(new AccountScreen(moduleManager.getAccountService()));

        StartupFrame startupFrame = new StartupFrame(moduleManager.getAccountService(), moduleManager.getNetworkService());
        Scene scene = new Scene(startupFrame.getPane());
        scene.getStylesheets().clear();
        scene.getStylesheets().addAll(getClass().getResource(GUICssTool.getCssFilePath()).toExternalForm());

        ToastFactory.init(primaryStage);
        primaryStage.setTitle(GUIStringTool.getAppTitle());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
