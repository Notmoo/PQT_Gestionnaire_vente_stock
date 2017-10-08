package com.pqt.client.gui.startup_frame;

import com.pqt.client.gui.startup_frame.listeners.IStartupFrameModelListener;

public class StartupFrameController implements IStartupFrameModelListener {

    private final StartupFrameModel model;
    private StartupFrameView view;

    public StartupFrameController(StartupFrameModel model) {
        this.model = model;
    }

    public void setView(StartupFrameView view) {
        this.view = view;
    }

    public void updateView() {
        //TODO écrire corps méthd StartupFrameController.updateView()
    }
}
