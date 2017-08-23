package com.pqt.client.gui.modules.account_screen.account_manager_screen;

import com.pqt.client.gui.ressources.components.generics.creators.IFXCreatorComponent;
import com.pqt.client.gui.ressources.components.generics.validators.IFXValidatorComponent;
import com.pqt.client.gui.ressources.components.generics.validators.listeners.IValidatorComponentListener;
import com.pqt.core.entities.user_account.Account;
import com.pqt.core.entities.user_account.AccountLevel;
import javafx.scene.layout.Pane;

import java.util.Collection;

//TODO à faire
public class AccountManagerScreen implements IFXValidatorComponent, IFXCreatorComponent<Account>{

    private AccountManagerScreenModel model;
    private AccountManagerScreenView view;
    private AccountManagerScreenController ctrl;

    public AccountManagerScreen(Account initialValue, Collection<AccountLevel> availableLevels) {
        model = new AccountManagerScreenModel(initialValue, availableLevels);
        ctrl = new AccountManagerScreenController(model);
        view = new AccountManagerScreenView(ctrl);

        ctrl.setView(view);
        ctrl.updateView();
    }

    public Account getInitialValue(){
        return model.getInitialValue();
    }

    @Override
    public Account create() {
        return model.getCurrentValue();
    }

    @Override
    public boolean isCreationPossible() {
        return model.isCurrentValueValid();
    }

    @Override
    public void addListener(IValidatorComponentListener l) {
        ctrl.addListener(l);
    }

    @Override
    public void removeListener(IValidatorComponentListener l) {
        ctrl.removeListener(l);
    }

    @Override
    public Pane getPane() {
        return view.getPane();
    }
}
