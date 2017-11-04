package com.pqt.client.gui.frames.main_frame;

import com.pqt.client.gui.frames.main_frame.listeners.IMainFrameModelListener;
import com.pqt.client.gui.modules.IGuiModule;
import com.pqt.client.gui.ressources.components.generics.frames.IFXFrame;
import com.pqt.client.module.account.AccountService;
import javafx.scene.layout.Pane;

public class MainFrame implements IFXFrame {

    private MainFrameView view;
    private MainFrameController ctrl;
    private MainFrameModel model;

    public MainFrame(AccountService accountService) {
        model = new MainFrameModel(accountService);
        ctrl = new MainFrameController(model);
        model.addListener(ctrl);

        view = new MainFrameView(ctrl);
        ctrl.setView(view);
        ctrl.updateView();
    }

    public void addModule(IGuiModule module, boolean setActive){
        ctrl.addModule(module, setActive);
    }

    public void addModule(IGuiModule module){
        ctrl.addModule(module, false);
    }

    public void addFrameModelListener(IMainFrameModelListener l){
        model.addListener(l);
    }

    public void requestFrameUpdate(){
        ctrl.updateView();
    }
    @Override
    public Pane getPane() {
        return view.getPane();
    }
}
