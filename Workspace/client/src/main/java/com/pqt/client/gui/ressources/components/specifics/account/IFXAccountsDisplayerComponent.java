package com.pqt.client.gui.ressources.components.specifics.account;

import com.pqt.client.gui.ressources.components.generics.displayers.IFXDisplayerComponent;
import com.pqt.client.gui.ressources.components.specifics.account.listeners.IAccountComponentListener;
import com.pqt.core.entities.user_account.Account;

import java.util.Collection;

public interface IFXAccountsDisplayerComponent extends IFXDisplayerComponent<Collection<Account>, IAccountComponentListener> {

}
