package com.pqt.client.module.gui.ressources.generics.validators;

import com.pqt.client.module.gui.ressources.generics.IFXComponent;
import com.pqt.client.module.gui.ressources.generics.validators.listeners.IValidatorComponentListener;

public interface IFXValidatorComponent extends IFXComponent{
    void addListener(IValidatorComponentListener l);
    void removeListener(IValidatorComponentListener l);
}
