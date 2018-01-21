package com.pqt.client.gui;

import com.pqt.client.gui.frames.main_frame.MainFrame;
import com.pqt.client.gui.frames.main_frame.listeners.IMainFrameModelListener;
import com.pqt.client.gui.modules.account_screen.AccountScreen;
import com.pqt.client.gui.modules.sale_screen.SaleScreen;
import com.pqt.client.gui.modules.stat_screen.StatScreen;
import com.pqt.client.gui.modules.stock_screen.StockScreen;
import com.pqt.client.gui.ressources.components.generics.toast.ToastFactory;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.client.gui.frames.startup_frame.StartupFrame;
import com.pqt.client.gui.frames.startup_frame.listeners.frame.IStartupFrameModelListener;
import com.pqt.client.module.ClientBackEndModuleManager;
import javafx.application.Platform;
import javafx.stage.Stage;

public class FrameManager {

    private FrameScene mainFrameScene;
    private FrameScene startupFrameScene;
    private Stage stage;
    private ClientBackEndModuleManager moduleManager;

    public FrameManager(Stage stage) {
        this.stage = stage;

        moduleManager = new ClientBackEndModuleManager(null);

        MainFrame mainFrame = new MainFrame(moduleManager.getAccountService());
        mainFrame.addModule(new SaleScreen(moduleManager.getAccountService(), moduleManager.getStockService(), moduleManager.getSaleService()), true);
        mainFrame.addModule(new StockScreen(moduleManager.getStockService(), moduleManager.getAccountService()));
        mainFrame.addModule(new StatScreen(moduleManager.getStatService()));
        mainFrame.addModule(new AccountScreen(moduleManager.getAccountService()));
        mainFrameScene = new FrameScene(mainFrame);

        StartupFrame startupFrame = new StartupFrame(moduleManager.getAccountService(), moduleManager.getNetworkService());
        startupFrameScene = new FrameScene(startupFrame);

        mainFrame.addFrameModelListener(getMainFrameListener());
        startupFrame.addFrameModelListener(getStartupFrameListener());

        ToastFactory.init(this.stage);
        this.stage.setTitle(GUIStringTool.getAppTitle());
        this.stage.setScene(startupFrameScene);
        this.stage.show();
    }

    public void show(){
        stage.show();
    }

    private IStartupFrameModelListener getStartupFrameListener(){
        return new IStartupFrameModelListener() {
            @Override
            public void onStartupValidated() {
                Platform.runLater(() -> trySwitchScene(stage, mainFrameScene, true));
            }

            @Override
            public void onStartupFailed() {

            }
        };
    }


    private IMainFrameModelListener getMainFrameListener(){
        return () -> Platform.runLater(()->trySwitchScene(stage, startupFrameScene, false));
    }

    private synchronized void trySwitchScene(Stage stage, FrameScene sceneToDisplay, boolean maximize){
        if(sceneToDisplay!=null) {
            stage.hide();
            stage.setScene(sceneToDisplay);
            stage.setMaximized(maximize);
            stage.show();
            sceneToDisplay.requestFrameUpdate();
        }else{
            Platform.exit();
        }
    }

    public void onCloseEvent() {
        moduleManager.shutdown();
    }
}
