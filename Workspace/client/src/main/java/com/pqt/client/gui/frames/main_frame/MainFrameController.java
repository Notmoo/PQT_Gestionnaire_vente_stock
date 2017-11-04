package com.pqt.client.gui.frames.main_frame;

import com.pqt.client.gui.frames.main_frame.listeners.IMainFrameModelListener;
import com.pqt.client.gui.modules.IGuiModule;
import com.pqt.core.entities.user_account.AccountLevel;

class MainFrameController implements IMainFrameModelListener {

    private MainFrameModel model;
    private MainFrameView view;

    MainFrameController(MainFrameModel model) {
        this.model = model;
    }

    void setView(MainFrameView view) {
        this.view = view;
    }

    void updateView(){
        view.setCurrentAccount(model.getCurrentAccount());
        if(model.getCurrentAccount()!=null)
            view.updateModuleButtonLock(model.getCurrentAccount().getPermissionLevel());
        else
            view.updateModuleButtonLock(AccountLevel.getLowest());
    }

    void addModule(IGuiModule module, boolean activationRequired) {
        boolean activate = activationRequired
                && model.getCurrentAccount()!=null
                && model.getCurrentAccount().getPermissionLevel().compareTo(module.getLowestRequiredAccountLevel())>=0;
        this.view.addGuiModule(module.getModuleName(),module.getPane(), module.getLowestRequiredAccountLevel(), activate);
    }

    public void onAccountDisconnectionRequested() {
        model.disconnectCurrentAccount();
    }

    @Override
    public void onAccountDisconnectedEvent() {

    }
}
