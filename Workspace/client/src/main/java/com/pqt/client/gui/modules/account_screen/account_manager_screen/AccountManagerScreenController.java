package com.pqt.client.gui.modules.account_screen.account_manager_screen;

import com.pqt.client.gui.ressources.components.generics.validators.listeners.IValidatorComponentListener;
import com.pqt.core.entities.user_account.AccountLevel;

import javax.swing.event.EventListenerList;
import java.util.Arrays;

class AccountManagerScreenController {

    private EventListenerList listenerList;
    private AccountManagerScreenView view;
    private AccountManagerScreenModel model;

    AccountManagerScreenController(AccountManagerScreenModel model) {
        listenerList = new EventListenerList();
        this.model = model;
    }

    void setView(AccountManagerScreenView view) {
        this.view = view;
        this.view.lockUserNameField(model.hasInitialValue());
    }

    void updateView() {
        view.setLevelCollection(model.getAccountLevelCollection());
        view.setData(model.getCurrentValue());
    }

    void addListener(IValidatorComponentListener l) {
        listenerList.add(IValidatorComponentListener.class, l);
    }

    void removeListener(IValidatorComponentListener l) {
        listenerList.remove(IValidatorComponentListener.class, l);
    }

    void onNameChanged(String oldVal, String newVal) {
        model.changeName(newVal);
    }

    void onPasswordChanged(String oldVal, String newVal) {
        model.changePassword(newVal);
    }

    void onLevelChanged(AccountLevel oldVal, AccountLevel newVal) {
        model.changeLevel(newVal);
    }

    void onValidationEvent() {
        Arrays.stream(listenerList.getListeners(IValidatorComponentListener.class))
                .forEach(IValidatorComponentListener::onValidationEvent);
    }

    void onCancelEvent() {
        Arrays.stream(listenerList.getListeners(IValidatorComponentListener.class))
                .forEach(IValidatorComponentListener::onCancelEvent);
    }
}
