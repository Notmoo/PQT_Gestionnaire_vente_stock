package com.pqt.client;

import com.pqt.client.gui.FrameManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main extends Application{

    public static void main(String[] args){
        launch(args);
    }

    private FrameManager fm;

    @Override
    public void start(Stage primaryStage) {
        fm = new FrameManager(primaryStage);

        primaryStage.setOnCloseRequest(event-> Platform.exit());

        fm.show();
    }

    @Override
    public void stop(){
        if(fm!=null)
            fm.onCloseEvent();
    }
}
