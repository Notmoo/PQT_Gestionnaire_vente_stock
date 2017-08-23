package com.pqt.client.gui.ressources.components.generics.toast;

import javafx.stage.Stage;

public class ToastFactory {

    private static ToastFactory INSTANCE = null;

    private Stage stage;
    private int delay, fadeInDelay, fadeOutDelay;

    private ToastFactory(Stage stage){
        this.stage = stage;
        delay = 3000;
        fadeInDelay = 250;
        fadeOutDelay = 250;
    }

    public void setToastDelay(int msDelay){
        this.delay = msDelay;
    }

    public void setToastFadeInDelay(int msDelay){
        this.fadeInDelay = msDelay;
    }

    public void setToastFadeOutDelay(int msDelay){
        this.fadeOutDelay = msDelay;
    }

    public void toast(String message){
        Toast.toast(stage, message, delay, fadeInDelay, fadeOutDelay);
    }

    public static void init(Stage stage){
        INSTANCE = new ToastFactory(stage);
    }

    public static boolean isInitialized(){
        return INSTANCE!=null;
    }

    public static ToastFactory getINSTANCE(){
        return INSTANCE;
    }
}
