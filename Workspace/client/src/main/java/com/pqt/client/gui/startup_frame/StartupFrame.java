package com.pqt.client.gui.startup_frame;

import com.pqt.client.gui.ressources.components.generics.IFXComponent;
import com.pqt.client.module.account.AccountService;
import com.pqt.client.module.network.NetworkService;
import javafx.scene.layout.Pane;

public class StartupFrame implements IFXComponent{

    private StartupFrameView view;
    private StartupFrameController ctrl;

    public StartupFrame(AccountService accountService, NetworkService networkService) {
        StartupFrameModel model = new StartupFrameModel(accountService, networkService);
        ctrl = new StartupFrameController(model);
        model.addListener(ctrl);

        view = new StartupFrameView(ctrl);
        ctrl.setView(view);
        ctrl.updateView();
    }

    @Override
    public Pane getPane() {
        return view.getPane();
    }
}
