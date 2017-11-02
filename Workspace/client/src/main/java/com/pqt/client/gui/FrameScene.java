package com.pqt.client.gui;

import com.pqt.client.gui.ressources.components.generics.frames.IFXFrame;
import com.pqt.client.gui.ressources.css.GUICssTool;
import javafx.scene.Scene;

public class FrameScene extends Scene {

    private IFXFrame frame;

    public FrameScene(IFXFrame frame) {
        super(frame.getPane());

        this.frame = frame;

        getStylesheets().clear();
        getStylesheets().addAll(getClass().getResource(GUICssTool.getCssFilePath()).toExternalForm());
    }

    public void requestFrameUpdate(){
        frame.requestFrameUpdate();
    }
}
