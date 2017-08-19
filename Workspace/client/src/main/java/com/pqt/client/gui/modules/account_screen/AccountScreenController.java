package com.pqt.client.gui.modules.account_screen;

import com.pqt.client.gui.ressources.components.generics.validators.listeners.IValidatorComponentListener;
import com.pqt.core.entities.user_account.Account;
import com.pqt.core.entities.user_account.AccountLevel;

class AccountScreenController {

    private AccountScreenModel model;
    private AccountScreenView view;

    AccountScreenController(AccountScreenModel model) {
        this.model = model;
    }

    void setView(AccountScreenView view) {
        this.view = view;
    }

    void updateView() {
        updateViewAccountCollection();
        updateViewActionLock();
    }

    private void updateViewAccountCollection(){
        view.setAccountCollection(model.getAccountCollection());
    }

    private void updateViewActionLock() {
        if (model.getCurrentAccount() != null) {
            view.setAddAccountActionLocked(model.getCurrentAccount().getPermissionLevel().compareTo(AccountLevel.MASTER) >= 0);
            view.setDetailAccountActionLocked(view.isItemSelected() && model.getCurrentAccount().getPermissionLevel().compareTo(AccountLevel.MASTER) >= 0);
            view.setRemoveAccountActionLocked(view.isItemSelected() && model.getCurrentAccount().getPermissionLevel().compareTo(AccountLevel.MASTER) >= 0);
        }else{
            view.setAddAccountActionLocked(true);
            view.setDetailAccountActionLocked(true);
            view.setRemoveAccountActionLocked(true);
        }
    }

    IValidatorComponentListener getDetailScreenValidationListener(){
        return new IValidatorComponentListener() {
            @Override
            public void onValidationEvent() {
                if(view.isDetailCreationPossible()){
                    if(view.getDetailScreenInitialValue()!=null){
                        model.modifyAccount(view.getDetailScreenInitialValue(), view.getDetailScreenCreatedValue());
                    }else{
                        model.addAccount(view.getDetailScreenCreatedValue());
                    }
                    view.hideDetailScreen();
                }
            }

            @Override
            public void onCancelEvent() {
                view.hideDetailScreen();
            }
        };
    }

    void onAddAccountRequested() {
        view.showDetailScreen(null, model.getLevels());
    }

    void onDetailAccountRequested() {
        if(view.isItemSelected())
            view.showDetailScreen(view.getSelectedItem(), model.getLevels());
    }

    void onRemoveAccountRequested() {
        if(view.isItemSelected())
            model.removeAccount(view.getSelectedItem());
    }

    void onRefreshAccountRequested() {
        this.updateView();
    }

    void onSelectedAccountChanged(Account oldVal, Account newVal) {
        updateViewActionLock();
    }
}
