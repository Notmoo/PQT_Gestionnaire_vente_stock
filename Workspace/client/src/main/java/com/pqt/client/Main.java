package com.pqt.client;

import com.pqt.client.gui.FrameManager;
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
        new FrameManager(primaryStage).show();
    }
}
