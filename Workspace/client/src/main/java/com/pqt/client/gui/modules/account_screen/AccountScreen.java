package com.pqt.client.gui.modules.account_screen;

import com.pqt.client.gui.modules.IGuiModule;
import com.pqt.client.gui.ressources.strings.GUIStringTool;
import com.pqt.client.module.account.AccountService;
import com.pqt.core.entities.user_account.AccountLevel;
import javafx.scene.layout.Pane;

public class AccountScreen implements IGuiModule {

    private AccountScreenView view;

    public AccountScreen(AccountService accountService) {
        AccountScreenModel model = new AccountScreenModel(accountService);
        AccountScreenController ctrl = new AccountScreenController(model);
        view = new AccountScreenView(ctrl);

        ctrl.setView(view);
        ctrl.updateView();
    }

    @Override
    public String getModuleName() {
        return GUIStringTool.getAccountGuiModuleName();
    }

    @Override
    public AccountLevel getLowestRequiredAccountLevel() {
        return AccountLevel.WAITER;
    }

    @Override
    public Pane getPane() {
        return view.getPane();
    }
}
