package com.pqt.client;

import com.pqt.client.gui.FrameManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application{

    private static Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args){
        launch(args);
    }

    private FrameManager fm;

    @Override
    public void start(Stage primaryStage) {
        LOGGER.info("Instanciation de l'application JFX du client");
        fm = new FrameManager(primaryStage);

        primaryStage.setOnCloseRequest(event-> Platform.exit());

        fm.show();
    }

    @Override
    public void stop(){
        LOGGER.info("Fermeture de l'application JFX du client");
        if(fm!=null)
            fm.onCloseEvent();
    }
}
