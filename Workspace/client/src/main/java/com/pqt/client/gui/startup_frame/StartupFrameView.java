package com.pqt.client.gui.startup_frame;

import com.pqt.client.gui.ressources.components.generics.IFXComponent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class StartupFrameView implements IFXComponent{

    private BorderPane mainPane;
    private final StartupFrameController ctrl;

    public StartupFrameView(StartupFrameController ctrl) {
        this.ctrl = ctrl;
        initGui();
    }

    private void initGui() {
        mainPane = new BorderPane();
        //TODO ajouter GUI StartupFrameView
    }

    @Override
    public Pane getMainPane() {
        return mainPane;
    }
}
