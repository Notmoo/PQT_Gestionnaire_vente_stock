package com.pqt.client.gui.ressources.components.specifics.account.listeners;

import com.pqt.client.gui.ressources.components.generics.displayers.listeners.SimpleDisplayerComponentFirerer;
import com.pqt.core.entities.user_account.Account;

public class SimpleAccountComponentFirerer extends SimpleDisplayerComponentFirerer<Account, IAccountComponentListener> {
    public SimpleAccountComponentFirerer() {
        super(IAccountComponentListener.class);
    }
}
