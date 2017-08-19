package com.pqt.client.gui.modules;

import com.pqt.client.gui.ressources.components.generics.IFXComponent;
import com.pqt.core.entities.user_account.AccountLevel;

public interface IGuiModule extends IFXComponent{
    String getModuleName();
    AccountLevel getLowestRequiredAccountLevel();
}
