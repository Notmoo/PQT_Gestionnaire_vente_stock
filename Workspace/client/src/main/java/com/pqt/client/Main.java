package com.pqt.client;

import com.pqt.client.gui.main_frame.MainFrame;
import com.pqt.client.gui.main_frame.listeners.IMainFrameModelListener;
import com.pqt.client.gui.modules.account_screen.AccountScreen;
import com.pqt.client.gui.modules.sale_screen.SaleScreen;
import com.pqt.client.gui.modules.stat_screen.StatScreen;
import com.pqt.client.gui.modules.stock_screen.StockScreen;
import com.pqt.client.gui.ressources.components.generics.toast.ToastFactory;
import com.pqt.client.gui.ressources.css.GUICssTool;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.client.gui.startup_frame.StartupFrame;
import com.pqt.client.gui.startup_frame.listeners.frame.IStartupFrameModelListener;
import com.pqt.client.module.ClientBackEndModuleManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application{

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        ClientBackEndModuleManager moduleManager = new ClientBackEndModuleManager(null);


        MainFrame mainFrame = new MainFrame(moduleManager.getAccountService());
        mainFrame.addModule(new SaleScreen(moduleManager.getAccountService(), moduleManager.getStockService(), moduleManager.getSaleService()), true);
        mainFrame.addModule(new StockScreen(moduleManager.getStockService(), moduleManager.getAccountService()));
        mainFrame.addModule(new StatScreen(moduleManager.getStatService()));
        mainFrame.addModule(new AccountScreen(moduleManager.getAccountService()));
        Scene mainFrameScene = initScene(mainFrame.getPane());

        StartupFrame startupFrame = new StartupFrame(moduleManager.getAccountService(), moduleManager.getNetworkService());
        Scene startupFrameScene = initScene(startupFrame.getPane());

        mainFrame.addFrameModelListener(getMainFrameListener(primaryStage, startupFrameScene));
        startupFrame.addFrameModelListener(getStartupFrameListener(primaryStage, mainFrameScene));

        ToastFactory.init(primaryStage);
        primaryStage.setTitle(GUIStringTool.getAppTitle());
        primaryStage.setScene(startupFrameScene);
        primaryStage.show();
    }

    private Scene initScene(Pane pane){
        Scene scene = new Scene(pane);
        scene.getStylesheets().clear();
        scene.getStylesheets().addAll(getClass().getResource(GUICssTool.getCssFilePath()).toExternalForm());

        return scene;
    }

    private IStartupFrameModelListener getStartupFrameListener(Stage primaryStage, Scene sceneToDisplay){
        return () -> {
            Platform.runLater(()->trySwitchScene(primaryStage, sceneToDisplay, true));
        };
    }


    private IMainFrameModelListener getMainFrameListener(Stage primaryStage, Scene sceneToDisplay){
        return () -> trySwitchScene(primaryStage, sceneToDisplay, false);
    }

    private void trySwitchScene(Stage primaryStage, Scene sceneToDisplay, boolean maximize){
        if(sceneToDisplay!=null) {
            primaryStage.hide();
            primaryStage.setScene(sceneToDisplay);
            primaryStage.setMaximized(maximize);
            primaryStage.show();
        }else{
            Platform.exit();
        }
    }
}
