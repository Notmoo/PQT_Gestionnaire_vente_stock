package com.pqt.client.gui.main_frame;

import com.pqt.client.gui.main_frame.listeners.IMainFrameModelListener;
import com.pqt.client.gui.modules.IGuiModule;
import com.pqt.client.gui.ressources.components.generics.validators.listeners.IValidatorComponentListener;
import com.pqt.client.gui.ressources.components.specifics.account.listeners.IAccountComponentListener;
import com.pqt.core.entities.user_account.Account;
import javafx.event.Event;

class MainFrameController implements IMainFrameModelListener {

    private MainFrameModel model;
    private MainFrameView view;
    private IValidatorComponentListener accountManagerAccountListener;

    MainFrameController(MainFrameModel model) {
        this.model = model;
    }

    void setView(MainFrameView view) {
        this.view = view;
    }

    void addModule(IGuiModule module, boolean setActive) {
        this.view.addGuiModule(module.getModuleName(),module.getPane(), setActive);
    }

    IValidatorComponentListener getAccountManagerValidatorListener() {
        return new IValidatorComponentListener() {
            @Override
            public void onValidationEvent() {
                if(view.isAccountCreationPossible())
                    model.connectAccount(view.create());
            }

            @Override
            public void onCancelEvent() {
                model.disconnectCurrentAccount();
            }
        };
    }

    IAccountComponentListener getAccountManagerAccountListener() {
        return new IAccountComponentListener() {
            @Override
            public void onRefreshContentRequestEvent() {

            }

            @Override
            public void onContentClickEvent(Event event, Account eventTarget) {

            }

            @Override
            public void onAddContentRequestEvent() {

            }

            @Override
            public void onRemoveContentRequestEvent(Account content) {

            }

            @Override
            public void onDetailContentRequestEvent(Account content) {

            }
        };
    }

    @Override
    public void onAccountStatusChangedEvent(boolean status) {
        view.setCurrentAccount(model.getCurrentAccount());
    }

    @Override
    public void onAccountCollectionChangedEvent() {
        view.feedAccountCollectionToManager(model.getAccounts());
    }
}
