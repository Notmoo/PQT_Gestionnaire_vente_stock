package com.pqt.client;

import com.pqt.client.gui.FrameManager;
import javafx.application.Application;
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
