package com.pqt.client.gui.ressources.components.generics.validators;

import com.pqt.client.gui.ressources.components.generics.IFXComponent;
import com.pqt.client.gui.ressources.components.generics.validators.listeners.IValidatorComponentListener;

public interface IFXValidatorComponent extends IFXComponent{
    void addListener(IValidatorComponentListener l);
    void removeListener(IValidatorComponentListener l);
}
