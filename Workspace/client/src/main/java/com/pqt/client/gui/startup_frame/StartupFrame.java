package com.pqt.client.gui.startup_frame;

import com.pqt.client.gui.ressources.components.generics.IFXComponent;
import com.pqt.client.gui.ressources.components.generics.frames.IFXFrame;
import com.pqt.client.gui.startup_frame.listeners.frame.IStartupFrameModelListener;
import com.pqt.client.module.account.AccountService;
import com.pqt.client.module.network.NetworkService;
import javafx.scene.layout.Pane;

public class StartupFrame implements IFXFrame{

    private StartupFrameView view;
    private StartupFrameController ctrl;
    private StartupFrameModel model;

    public StartupFrame(AccountService accountService, NetworkService networkService) {
        model = new StartupFrameModel(accountService, networkService);
        ctrl = new StartupFrameController(model);
        model.addListener(ctrl);

        view = new StartupFrameView(ctrl);
        ctrl.setView(view);
        ctrl.updateView();
    }

    public void addFrameModelListener(IStartupFrameModelListener l){
        model.addListener(l);
    }

    @Override
    public Pane getPane() {
        return view.getPane();
    }

    @Override
    public void requestFrameUpdate() {
        ctrl.updateView();
    }
}
