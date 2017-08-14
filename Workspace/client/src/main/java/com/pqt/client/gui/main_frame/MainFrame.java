package com.pqt.client.gui.main_frame;

import com.pqt.client.gui.modules.IGuiModule;
import com.pqt.client.gui.ressources.components.generics.IFXComponent;
import com.pqt.client.module.account.AccountService;
import javafx.scene.layout.Pane;

public class MainFrame implements IFXComponent {

    private MainFrameView view;
    private MainFrameController ctrl;

    public MainFrame(AccountService accountService) {
        MainFrameModel model = new MainFrameModel(accountService);
        ctrl = new MainFrameController(model);
        model.addListener(ctrl);

        view = new MainFrameView(ctrl);
        ctrl.setView(view);
    }

    public void addModule(IGuiModule module){
        ctrl.addModule(module);
    }

    @Override
    public Pane getPane() {
        return view.getPane();
    }
}
