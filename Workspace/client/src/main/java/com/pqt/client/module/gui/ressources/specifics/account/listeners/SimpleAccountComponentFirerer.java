package com.pqt.client.module.gui.ressources.specifics.account.listeners;

import com.pqt.client.module.gui.ressources.generics.displayers.listeners.SimpleDisplayerComponentFirerer;
import com.pqt.core.entities.user_account.Account;

public class SimpleAccountComponentFirerer extends SimpleDisplayerComponentFirerer<Account, IAccountComponentListener>{
    public SimpleAccountComponentFirerer() {
        super(IAccountComponentListener.class);
    }
}
